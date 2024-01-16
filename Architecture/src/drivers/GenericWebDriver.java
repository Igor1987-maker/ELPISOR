package drivers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.Dimension;

import javax.imageio.ImageIO;

import jcifs.smb.NtlmPasswordAuthentication;

//import net.lightbody.bmp.BrowserMobProxy;
//import net.lightbody.bmp.BrowserMobProxyServer;
//import net.lightbody.bmp.client.ClientUtil;

import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.proxy.ProxyServer;

import org.apache.http.HttpHost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.util.EntityUtils;
//import org.browsermob.proxy.ProxyServer;
import org.json.JSONObject;
import org.junit.Assert;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import services.DbService;
import services.GenericService;
import services.NetService;
import services.Reporter;
import services.TestResultService;
import services.TextService;
import Enums.AutoParams;
import Enums.ByTypes;
import Enums.TestRunnerType;
import Enums.expectedConditions;

@SuppressWarnings("deprecation")
@Service
public abstract class GenericWebDriver extends GenericService {

	protected static final Logger logger = LoggerFactory
			.getLogger(GenericWebDriver.class);
	private String sutUrl = null;
	private String sutSubDomain = null;
	private String institutionnName = null;
	protected Boolean openIncognitoChrome=false;
	
	private static final String SCREENSHOT_FOLDER = "automationScreenshots";
	
	private String CIServerName = null;

	protected RemoteWebDriver webDriver;

	// for applitools
	// protected WebDriver webDriver;

	public WebDriver eyesDriver;

	protected int timeout = 10;
	private String browserName;
	List<String> currentWindowHandles;
	
	private boolean initialized;
	// private Config configuration;
	protected String remoteMachine;
	protected boolean enableConsoleLog;
	protected boolean useProxy;
	
	String scrFileExt = "png";

	Proxy proxy;
	ProxyServer server;

	@Autowired
	private services.Configuration configuration;

	private TextService textService;

	@Autowired
	protected services.Reporter reporter;

	@Autowired
	TestResultService testResultService;

	@Autowired
	NetService netService;

	protected String logsFolder;
	private boolean failureAdded;

	// Eyes eyes = new Eyes();
	// boolean eyesOpen = false;

	// This is your api key, make sure you use it in all your tests.

	// abstract public void init(String remoteUrl, String folderName)
	// throws Exception;

	abstract public void init(String remoteUrl, boolean startProxy)
			throws Exception;

	public void init() throws Exception {
		init(false);
	}
	
	public void init(boolean useProxy) throws Exception {
		// this.testResultService = testResultService;
		try {
			// eyes.setApiKey("tsN45rbyinZ1084MxMVSzumAgD106Qn3MOpBcr101hiyVEpSY110");

			textService = new TextService();

			// get remote machine from extendedRunner
			remoteMachine = System.getProperty("remoteMachine");

			try {
				if (remoteMachine.equals(null) || remoteMachine.equals("")) {
					remoteMachine = configuration.getAutomationParam(
							AutoParams.remoteMachine.toString(), "machine");

				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				remoteMachine = configuration.getAutomationParam(
						AutoParams.remoteMachine.toString(), "machine");
			}
			setSutUrl(configuration.getAutomationParam(
					AutoParams.sutUrl.toString(), "suturl"));
			setSutSubDomain(configuration.getProperty("institution.name"));
			setInstitutionName(configuration.getProperty("institution.name"));
			System.setProperty("remoteMachine", "");

			// String useProxy = System.getProperty("useProxy");
			if (useProxy == true) {
				init(remoteMachine, true);
			} else {
				init(remoteMachine, false);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getSutUrl() {
		return sutUrl;
	}

	public void setSutUrl(String sutUrl) {
		this.sutUrl = sutUrl;
	}

	public void getFucus() throws Exception {
		webDriver.switchTo().window(webDriver.getWindowHandle());
	}

	public void openUrl(String url) throws Exception, TimeoutException {

		try {
			webDriver.get(url);
			if (useProxy) {
				startProxyLister(url);
				//webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			}
			reporter.report("Opening link: " + url);
		} catch (UnhandledAlertException e) {
			getUnexpectedAlertDetails();
			// closeAlertByAccept();
		} catch (Exception e) {
			reporter.report(e.toString());

		}
		//webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	public void navigate(String url) throws Exception {

		webDriver.navigate().to(url);
	}

	public WebElement waitForElement(String idValue, ByTypes byType,
			int timeout, boolean isElementMandatory) throws Exception {
		return waitForElement(idValue, byType, timeout, isElementMandatory,
				null);
	}

	public WebElement waitForElement(String idValue, ByTypes byType,
			int timeout, boolean isElementMandatory, String message)
			throws Exception {

		return waitForElement(idValue, byType, timeout, isElementMandatory,
				message, 1000);
	}

	public WebElement waitForElement(String idValue, ByTypes byType,
			int timeout, boolean isElementMandatory, String message, int sleepMS)
			throws Exception {
		return waitForElement(idValue, byType, timeout, isElementMandatory,
				message, sleepMS, expectedConditions.visible);
	}

	@SuppressWarnings("finally")
	public WebElement waitForElement(String idValue, ByTypes byType,
			int timeout, boolean isElementMandatory, String message,
			int sleepMS, expectedConditions conditions) throws Exception {
		// reporter.report("waiting for element " + idValue + " by trpe "
		// + byType + " for " + timeout + " seconds");
		reporter.report("waiting for element " + idValue + " by type " + byType
				+ " for " + timeout + " seconds");
		WebElement element = null;

		long startTime = System.currentTimeMillis();

		try {
			WebDriverWait wait = new WebDriverWait(webDriver, timeout, sleepMS);

			switch (byType) {
			case className:
				wait.until(ExpectedConditions.visibilityOfElementLocated(By
						.className(idValue)));
				element = webDriver.findElement(By.className(idValue));
				;
				break;
			case linkText:
				wait.until(ExpectedConditions.visibilityOfElementLocated(By
						.linkText(idValue)));
				element = webDriver.findElement(By.linkText(idValue));
				break;
			case id:
				wait.until(ExpectedConditions.visibilityOfElementLocated(By
						.id(idValue)));
				element = webDriver.findElement(By.id(idValue));
				;
				break;
			case name:
				wait.until(ExpectedConditions.visibilityOfElementLocated(By
						.name(idValue)));
				element = webDriver.findElement(By.name(idValue));
				;
				break;
			case partialLinkText:
				wait.until(ExpectedConditions.visibilityOfElementLocated(By
						.partialLinkText(idValue)));
				element = webDriver.findElement(By.partialLinkText(idValue));
				break;
			case xpath:
				if (conditions.equals(expectedConditions.visible)) {
					wait.until(ExpectedConditions.visibilityOfElementLocated(By
							.xpath(idValue)));
					element = webDriver.findElement(By.xpath(idValue));
					break;
				} else if (conditions.equals(expectedConditions.precence)) {
					wait.until(ExpectedConditions.presenceOfElementLocated(By
							.xpath(idValue)));
					element = webDriver.findElement(By.xpath(idValue));
					break;
				}
			case tagName:
				if (conditions.equals(expectedConditions.precence)) {
					wait.until(ExpectedConditions.presenceOfElementLocated(By
							.tagName(idValue)));
					element = webDriver.findElement(By.tagName(idValue));
				} else if (conditions.equals(expectedConditions.visible)) {
					wait.until(ExpectedConditions.visibilityOfElementLocated(By
							.tagName(idValue)));
					element = webDriver.findElement(By.tagName(idValue));
				}
			case cssSelector:
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(idValue)));
				element = webDriver.findElement(By.cssSelector(idValue));
				
				break;
			}

		} catch (UnhandledAlertException e) {
			reporter.report("Closing alert and trying again");
			closeAlertByAccept();
			getUnexpectedAlertDetails();
			// waitForElement(idValue, byType);
		} catch (NoSuchElementException e) {

			if (isElementMandatory == true) {
				Assert.fail("Exception when waiting for element:" + idValue
						+ ".| " + e.toString() + " Description: " + message);
				failureAdded = true;
			}

		} catch (TimeoutException e) {
			if (isElementMandatory == true) {
				testResultService.addFailTest("Element " + idValue
						+ " was not found after the specified timeout: "
						+ timeout + " Description of element:" + message);
				failureAdded = true;
			}
		} catch (InvalidElementStateException e) {
			testResultService.addFailTest("Element " + idValue
					+ " was in invalid state " + " Description of element:"
					+ message);
			failureAdded = true;
		} catch (WebDriverException e) {
			printScreen("Web driver exception found");
			reporter.report(e.toString());
		}

		catch (Exception e) {
			reporter.report("Unknown exception was found:" + e.toString()
					+ " while watining for element with description: "
					+ message);
		}

		finally {
			if (isElementMandatory == true && element == null) {
				// if (message != null) {
				// // reporter.report(message);
				// }
				if (failureAdded == false) {
					testResultService.addFailTest("Element: " + idValue
							+ " not found. Description:" + message);
				}
				String idValueForPrintScreen = idValue.replaceAll("/", "_");

				printScreen("Element " + idValueForPrintScreen + " _not_found ");
				testResultService.addFailTest("Element_ " + idValue
						+ " not found " + message, true, true);

				// super.printLogs();
				org.junit.Assert.fail("Test failed due to missing web element");

			}
			long endTime = System.currentTimeMillis();
			long elapsedTime = endTime - startTime;
			if (element != null) {
				reporter.report("Element " + idValue + " found after: "
						+ elapsedTime + " ms");
			}
			return element;
		}
	}

	public WebElement waitForElement(String idValue, ByTypes byType,
			String message) throws Exception {
		return waitForElement(idValue, byType, timeout, true, message);
	}
	
	// new
	public boolean isDisplayed(WebElement element) {
        try {
            element.isDisplayed();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
	
	// new
	public void waitForElementToBeVisisble(WebElement element) {
		try {
			WebDriverWait wait = new WebDriverWait(webDriver, timeout, 3000);
			wait.until(ExpectedConditions.visibilityOf(element));
		} catch (Exception e) {
			testResultService.addFailTest("FAIL: Wait for Element to be Visible. Error: " + e);
		}
	}
	
	// new
	public void SendKeys(WebElement element, String keys) {
		try {
			waitForElementToBeVisisble(element);
			element.sendKeys(keys);
		} catch (Exception e) {
			testResultService.addFailTest("FAIL: Send keys to Element. Error: " + e);
		}
	}
	
	// new
	public void ClickElement(WebElement element) {
		try {
			waitForElementToBeVisisble(element);
			element.click();
		} catch (Exception e) {
			testResultService.addFailTest("FAIL: Click Element. Error: " + e);
		}
	}
	
	public String getTextFromElement(WebElement element) {
		try {
			waitForElementToBeVisisble(element);
			return element.getText();
		} catch (Exception e) {
			testResultService.addFailTest("FAIL: Get Text From Element. Error: " + e);
			return null;
		}
	
	}
	
	// new
	public void waitForElementByWebElement(WebElement element,
			String elementName,boolean isElementMandatory, long sleepMS) throws Exception {
		long startTime = System.currentTimeMillis();
		try {
			WebDriverWait wait = new WebDriverWait(webDriver, timeout, sleepMS);
			wait.until(ExpectedConditions.visibilityOf(element));
		} catch (UnhandledAlertException e) {
			reporter.report("Closing alert and trying again");
			closeAlertByAccept();
			getUnexpectedAlertDetails();
		} catch (NoSuchElementException e) {
			if (isElementMandatory == true) {
				Assert.fail("Exception when waiting for element:" + elementName
						+ ".| " + e.toString());
				failureAdded = true;
			}
		} catch (TimeoutException e) {
			if (isElementMandatory == true) {
				testResultService.addFailTest("Element " + elementName
						+ " was not found after the specified timeout: "
						+ timeout);
				failureAdded = true;
			}
		} catch (InvalidElementStateException e) {
			testResultService.addFailTest("Element " + elementName
					+ " was in invalid state ");
			failureAdded = true;
		} catch (WebDriverException e) {
			printScreen("Web driver exception found");
			reporter.report(e.toString());
		} catch (Exception e) {
			reporter.report("Unknown exception was found:" + e.toString()
					+ " while watining for element: " + elementName);
		} finally {
			if (isElementMandatory == true && element == null) {
				// if (message != null) {
				// // reporter.report(message);
				// }
				if (failureAdded == false) {
					testResultService.addFailTest("Element: " + elementName
							+ " not found.");
				}
				//String idValueForPrintScreen = idValue.replaceAll("/", "_");

				//printScreen("Element " + idValueForPrintScreen + " _not_found ");
				//testResultService.addFailTest("Element_ " + idValue
				//		+ " not found ", true, true);

				org.junit.Assert.fail("Test failed due to missing web element");
			}
			long endTime = System.currentTimeMillis();
			long elapsedTime = endTime - startTime;
			if (element != null) {
				reporter.report("Element " + elementName + " found after: "
						+ elapsedTime + " ms");
			}
		}
	}
	
	
	/**
	 * @param idValue
	 *            - the xpath expression/class name/id/link text etc.
	 * @param byType
	 *            - the type of the identifier
	 * @return the WebElement by the ByType and value. Uses default timeout,
	 *         Mandatory element=true and print no message
	 * @exception UnhandledAlertException
	 * @exception NoSuchElementException
	 */

	public WebElement waitForElement(String idValue, ByTypes byType)
			throws Exception {
		return waitForElement(idValue, byType, this.timeout, true, null);
	}

	public void waitForElementAndClick(String idValue, ByTypes byType)
			throws Exception {
		waitForElement(idValue, byType, timeout, true, null).click();
	}

	public void waitForElementAndClick(String idValue, ByTypes byType,int timeOut)
			throws Exception {
		waitForElement(idValue, byType, timeOut, true, null).click();
	}
	
	public void waitForElementAndSendEnter(String idValue, ByTypes byType)
			throws Exception {
		waitForElement(idValue, byType, timeout, true, null).sendKeys(
				Keys.ENTER);
	}

	public WebElement waitForElement(String idValue, ByTypes byType,
			boolean isElementMandatory, int timeout) throws Exception {
		return waitForElement(idValue, byType, timeout, isElementMandatory,
				null);
	}

	public void sendKey(Keys keys) throws Exception {
		webDriver.switchTo().activeElement().sendKeys(keys);
	}

	public void sendKey(String keys) throws Exception {
		webDriver.switchTo().activeElement().sendKeys(keys);
	}

	public boolean checkElementEnabledAndClickable(String xpath)
			throws Exception {

		WebElement element = null;
		try {
			WebDriverWait wait = new WebDriverWait(webDriver, timeout, 1000);
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
			element = webDriver.findElement(By.xpath(xpath));
			} catch (Exception e) {

			// Assert.fail("Element not found or element is not Clickable");
			testResultService
					.addFailTest("Element not found or element is not Clickable");
			return false;
		}
		if (element != null) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean checkElementEnabled(String xpath)
			throws Exception {
		boolean res = false; 
		WebElement element = null;
		try {
			WebDriverWait wait = new WebDriverWait(webDriver, timeout, 1000);
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
			element = webDriver.findElement(By.xpath(xpath));
			element.click();
			} catch (Exception e) {

			return false;
			}
		if (element!=null) {
			element.click();
			return true;
		} 
		return false;

	}
	
	
	
	
	

	public void quitBrowser() throws Exception {
		reporter.report("Quit broswer");
		if (initialized == true) {
			try {

				// deleteCookiesAndCache();
				stopProxyListen();
				webDriver.quit();

			} catch (Exception e) {
				reporter.report("Closing " + this.getBrowserName() + "failed. "
						+ e.toString());
			}
		}
		// if (eyesOpen) {
		// eyes.close();
		// }

	}

	public void closeBrowser() throws Exception {
		try {

			deleteCookiesAndCache();

			webDriver.close();

		} catch (Exception e) {
			reporter.report("Closing " + this.getBrowserName() + "failed. "
					+ e.toString());
		}
	}

	public void refresh() throws Exception {
		webDriver.navigate().refresh();
	}

	public void deleteCookiesAndRefresh() throws Exception {

		try {
			webDriver.manage().deleteAllCookies();
			webDriver.navigate().refresh();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out
					.println("Failed when trying to delete cookies and refresh");
			e.printStackTrace();
		}
	}

	public void deleteCookiesAndCache() throws Exception {
		webDriver.manage().deleteAllCookies();

	}
	
	public String getCookie(String cookie) throws Exception {
		return webDriver.manage().getCookieNamed(cookie).toString();
		
	}

	public String getAllCookies() throws Exception {
		return webDriver.manage().getCookies().toString();
	}
	
	public void sendKey(Keys keys, int iterations) throws Exception {
		for (int i = 0; i < iterations; i++) {
			sendKey(keys);

		}
	}

	public void swithcToFrameAndSendKeys(String xpathExpression, String keys,
			String frameId) throws Exception {
		swithcToFrameAndSendKeys(xpathExpression, keys, false, frameId);
	}

	public void swithcToFrameAndSendKeys(String xpathExpression, String keys,
			boolean clear, String frameId) throws Exception {
		reporter.report("Finding needed window");
		String currentWindow = webDriver.getWindowHandle();
		reporter.report("Switching to needed window");
		Thread.sleep(1000);
		webDriver.switchTo().frame(frameId);
		// webDriver.findElement(By.xpath(xpathExpression)).click();
		// webDriver.findElement(By.xpath(xpathExpression)).sendKeys(keys);
		reporter.report("Finding relevant element");
		WebElement element = webDriver.findElement(By.xpath(xpathExpression));
		reporter.report("Clicking on found element");
		element.click();
		if (clear == true) {
			element.clear();
		}
		//
		// sendKey(keys);
		// element.sendKeys(keys);
		reporter.report("About to send keys");
		element.sendKeys(keys);
		webDriver.switchTo().window(currentWindow);
		reporter.report("Finished sending keys");
	}

	public String switchToFrame(String frameName) throws Exception {
		String currentWindow = null;
		try {
			currentWindow = webDriver.getWindowHandle();
			WebDriverWait wait = new WebDriverWait(webDriver, timeout);
			wait.until(ExpectedConditions
					.frameToBeAvailableAndSwitchToIt(frameName));
		} catch (TimeoutException e) {
			// Assert.fail("Frame waw not found");
			reporter.report(e.toString());
			testResultService.addFailTest("Frame was not found", true, true);
		} catch (UnhandledAlertException e) {
			String alertText = getAlertText(timeout);
			testResultService.addFailTest("Unexpected alter with text: "
					+ alertText + " was displayed", false, true);
		}

		finally {

		}
		return currentWindow;
	}

	public String switchToFrame(String frameName,boolean terminateTest) throws Exception {
		String currentWindow = null;
		try {
			currentWindow = webDriver.getWindowHandle();
			WebDriverWait wait = new WebDriverWait(webDriver, timeout);
			wait.until(ExpectedConditions
					.frameToBeAvailableAndSwitchToIt(frameName));
		} catch (TimeoutException e) {
			// Assert.fail("Frame waw not found");
			reporter.report(e.toString());
			testResultService.addFailTest("Frame was not found", terminateTest, true);
		} catch (UnhandledAlertException e) {
			String alertText = getAlertText(timeout);
			testResultService.addFailTest("Unexpected alter with text: "
					+ alertText + " was displayed", false, true);
		}

		finally {

		}
		return currentWindow;
	}
	
	public String switchToFrame(WebElement element) throws Exception {
		String currentWindow = webDriver.getWindowHandle();
		webDriver.switchTo().frame(element);
		return currentWindow;
	}

	public String switchToFrame(int index) throws Exception {
		String currentWindow = webDriver.getWindowHandle();
		webDriver.switchTo().frame(index);
		return currentWindow;
	}

	public void getFrameNames() throws Exception {
		webDriver.switchTo().frame(1);
		WebDriverWait wait = new WebDriverWait(webDriver, timeout, 1000);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By
				.id("feedform_user_message")));
		WebElement element = webDriver.findElement(By
				.id("feedform_user_message"));
		element.sendKeys("Test Frame");

		// WebElement element=
		// webDriver.findElement(By.xpath("//input[@name='publish',@value='send',@type='submit']"));

		element.click();

	}

	public String switchToPopUp(int index) throws Exception {
		String parentWindowhandle = webDriver.getWindowHandle();
		WebDriver popup = null;
		Set<String> openWindowsList = webDriver.getWindowHandles();
		String popupWindowHandle = null;
		int i = 0;
		for (String windowHandle : openWindowsList) {
			if (!windowHandle.equals(parentWindowhandle))
				if (i == index) {
					popupWindowHandle = windowHandle;
					break;
				}
			i++;
		}
		webDriver.switchTo().window(popupWindowHandle);
		return parentWindowhandle;
	}

	public String switchToPopup() throws Exception {
		String parentWindowhandle = webDriver.getWindowHandle();
		WebDriver popup = null;
		Set<String> openWindowsList = webDriver.getWindowHandles();
		String popupWindowHandle = null;
		for (String windowHandle : openWindowsList) {
			if (!windowHandle.equals(parentWindowhandle))
				popupWindowHandle = windowHandle;
		}
		try {
			webDriver.switchTo().window(popupWindowHandle);

		} catch (Exception e) {
			e.printStackTrace();
			testResultService.addFailTest(e.getMessage(), true, true);
		}
		return parentWindowhandle;
	}

	public void switchToMainWindow(String windowName) throws Exception {
		webDriver.switchTo().window(windowName);
		switchToTopMostFrame();

	}

	public String switchToNewWindow() throws Exception {
		return switchToNewWindow(1);
	}

	public String switchToNewWindow(int windowId) throws Exception {

		Set<String> winhandles = webDriver.getWindowHandles();
		List<String> windows = new ArrayList<String>();
		windows.addAll(winhandles);
		reporter.report("before switch: " + webDriver.getWindowHandle());
		String oldWindow = webDriver.getWindowHandle();
		sleep(5);
		webDriver.switchTo().window(windows.get(windowId));
		reporter.report("after switch: " + webDriver.getWindowHandle());
		return oldWindow;

	}

	public void checkElementNotExist(String xpath, String message)
			throws Exception {
		// boolean elementFound = false;
		// try {
		// WebElement element = waitForElement(xpath, ByTypes.xpath, false,
		// timeout);
		// if (element != null) {
		// elementFound = true;
		// printScreen(message);
		// }
		//
		// } catch (Exception e) {
		// reporter.report("Exceptin found during checkElementNotExist "
		// + e.toString());
		// } finally {
		//
		// testResultService.assertTrue("Element with xpath " + xpath
		// + " found when it should not", elementFound == false);
		// }
		WebDriverWait wait = new WebDriverWait(webDriver, timeout, 1000);

		try {
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By
					.xpath(xpath)));
			WebElement element = waitForElement(xpath, ByTypes.xpath, 10, false);
			if (element != null) {
				testResultService.addFailTest("Element with xpath: " + xpath
						+ " was found when it should not");
				printScreen();
			}
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			testResultService.addFailTest("Element with xpath: " + xpath
					+ " was found when it should not");
			printScreen();
		}

	}

	public void checkElementNotExist(String xpath) throws Exception {
		checkElementNotExist(xpath, "Element with xpath: " + xpath
				+ " was found when it should not");
	}
	
	public boolean checkElementAndResponseIfExists(String xpath) throws Exception {
		boolean status=true;
		status = waitUntilElementAppears(xpath, 2);
		return status;
	}
	
	public void verifyThatElementNotDisplayed(By locator, int timeout) throws Exception {
		
		WebDriverWait wait = new WebDriverWait(webDriver, timeout, 1000);
		WebElement element = null;
		
		try {
			element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
		}
		if (element!=null) testResultService.addFailTest("Element with locator: " + locator.toString() + " was found though it SHOULD NOT BE DISPLAYED");								
		
	}
	
	public void verifyThatElementNotClickable(By locator, int timeout) throws Exception {
		
		WebDriverWait wait = new WebDriverWait(webDriver, timeout, 1000);
		WebElement element = null;
		
		try {
			element = wait.until(ExpectedConditions.elementToBeClickable(locator));
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
		}
		if (element!=null) testResultService.addFailTest("Element with locator: " + locator.toString() + " was clickable though it SHOULD NOT BE ClICKABLE");								
		
	}
	
	

	public WebElement getElement(By by) {
		WebElement element = webDriver.findElement(by);
		return element;
	}

	public void switchToAlert() {
		try {
			WebDriverWait wait = new WebDriverWait(webDriver, timeout, 1000);

			if (wait.until(ExpectedConditions.alertIsPresent()) != null) {
				webDriver.switchTo().alert();
			}
		} catch (NoAlertPresentException e) {
			e.printStackTrace();
			Assert.fail("Alert not found");
		}
	}

	public void closeAlertByAccept() {
		closeAlertByAccept(getTimeout());
	}

	public void closeAlertByAccept(int _timeout) {
		try {
			reporter.report("Closing alert");
			WebDriverWait wait = new WebDriverWait(webDriver, _timeout, 1000);
			if (wait.until(ExpectedConditions.alertIsPresent()) != null) {
				webDriver.switchTo().alert().accept();
			}
			reporter.report("Finished closing alert");
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			reporter.report("Alert was not found after 10 seconds");
		}

	}

	public void closeAlertByDismiss() {
		WebDriverWait wait = new WebDriverWait(webDriver, timeout, 1000);

		if (wait.until(ExpectedConditions.alertIsPresent()) != null) {
			webDriver.switchTo().alert().dismiss();
		}
	}

	public String getAlertText(int timeOut) {
		WebDriverWait wait = new WebDriverWait(webDriver, timeOut, 1000);
		String text = null;
		try {
			if (wait.until(ExpectedConditions.alertIsPresent()) != null) {
				Alert alert = webDriver.switchTo().alert();
				text = alert.getText();
			}
		} catch (Exception e) {
			System.out
					.println("Could not get alert text. might have timed out");
		}
		return text;
	}

	public String printScreen() throws Exception {
		return printScreen("");
	}

	// public String printScreen(String message) throws Exception {
	// return printScreen(message, null);
	// }

	public File printScreenAsFile() throws Exception {
		WebDriver driver = webDriver;
		driver = new Augmenter().augment(driver);
		File file = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		return file;
	}

	/**
	 * 
	 * @param message
	 *            the message will be part of the screenshot file name The
	 *            screen shot will be saved in the automation server screenshots
	 *            folder
	 * @return the full path of the screenshot file
	 * @throws Exception
	 */
	public String printScreen(String message) throws Exception {
		// File screenShot;
		// log levels: 0-only failes tests, 1-save all screenshots

		String sig = dbService.getShortCurrentDate() + "_" + getBrowserName().replace(" ", "")
				+ "_";
		String path = null;
		String newFileName = null;
		String logFileLink = null;
		
		try {
			WebDriver driver = webDriver;
			driver = new Augmenter().augment(driver);
			byte[] decodedScreenshot = org.apache.commons.codec.binary.Base64
					.decodeBase64(((TakesScreenshot) driver).getScreenshotAs(
							OutputType.BASE64).getBytes());
			// TestRunnerType runner = getTestRunner();
			TestRunnerType runner = TestRunnerType.CI;
			// If test is running using jenkins ci
			if (runner == TestRunnerType.CI) {

				path = getScrPathForCI(message, sig, "png");

			} else if (runner == TestRunnerType.local) {
				path = getScrPathForLocal(message, sig, "png");

				logFileLink = path;
			}

			if (runner == TestRunnerType.CI) {
				// **printscreen using smbFile

				message = message.replace(":", "_");
				message = message.replace("''", "");
				message = message.replace("'", "");
				message = message.substring(0, message.length()/2);
				
				String sFileName = "scr_" + sig + message.replace(" ", "")
						+ ".png";
				
				/*
				SmbFile smbFile = new SmbFile("smb://" + configuration.getLogerver() + "/AutoLogs/"+SCREENSHOT_FOLDER+"/" + sFileName, netService.getDomainAuth());
				SmbFileOutputStream smbFileOutputStream = new SmbFileOutputStream(smbFile);
				smbFileOutputStream.write(decodedScreenshot);
				smbFileOutputStream.close();
				*/
				
				String pathToSceenShot = "\\\\"+configuration.getGlobalProperties("logserverName")+"\\AutoLogs\\"+SCREENSHOT_FOLDER+"\\" + sFileName;
				File file = new File(pathToSceenShot);
				FileOutputStream output = new FileOutputStream(file);
				output.write(decodedScreenshot);
				
				reporter.report("https://"+configuration.getGlobalProperties("urlLogFiles")+"/"+SCREENSHOT_FOLDER+"/"
						+ sFileName);
				logFileLink = "https://"+configuration.getGlobalProperties("urlLogFiles")+"/"+SCREENSHOT_FOLDER+"/"
						+ sFileName;
				
				/* //Done by Igor when we had SMB error 06/02/2023
				 
				//SmbFile smbFile = new SmbFile("smb://" + configuration.getLogerver() + "/"+SCREENSHOT_FOLDER+"/" + sFileName, netService.getDomainAuth());
				//SmbFileOutputStream smbFileOutputStream = new SmbFileOutputStream(smbFile);
				
				 FileOutputStream fos = new FileOutputStream(new
				 File(newFileName));
				 fos.write(decodedScreenshot);
				//smbFileOutputStream.write(decodedScreenshot);
				//smbFileOutputStream.close();
				
				String pathToSceenShot = "\\\\"+configuration.getGlobalProperties("logserverName")+"\\"+SCREENSHOT_FOLDER+"\\" + sFileName;
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				DataOutputStream out = new DataOutputStream(baos);
				
				File file = new File(pathToSceenShot);
				FileOutputStream output = new FileOutputStream(file);
				output.write(decodedScreenshot);
				
				
				reporter.report("http://"+configuration.getGlobalProperties("logserverName")+"/"+SCREENSHOT_FOLDER+"/"
						+ sFileName);
				//logFileLink = "http://"+configuration.getGlobalProperties("logserverName")+"/"+SCREENSHOT_FOLDER+"/"
					//	+ sFileName;
				logFileLink=pathToSceenShot;
				*/
			} else {
				path = getScrPathForLocal(message, sig, "png");

				FileOutputStream fos = new FileOutputStream(new File(
						newFileName));
				fos.write(decodedScreenshot);
			}

		} catch (Exception e) {
			reporter.report("Taking the screenshot failed: " + e.toString());
		}

		finally {
			reporter.addLink(logFileLink, "Screenshot taken");
		}

		return path;

	}

	private String getScrPathForLocal(String message, String sig, String fileExt) {
		String path;
		String newFileName;
		newFileName = System.getProperty("user.dir") + "/log//current/"
				+ "ScreenShot" + message.replace(" ", "") + sig + "." + fileExt;
		path = System.getProperty("user.dir") + "//" + "log//current//"
				+ "ScreenShot" + message.replace(" ", "") + sig + "." + fileExt;
		return path;
	}

	private String getScrPathForCI(String message, String timeStamp,
			String extension) {
		String path;
/*		String newFileName;
		newFileName = "\\\\" + configuration.getGlobalProperties("logserverName") + "\\AutoLogs\\"
				+ configuration.getProperty("screenshotFolder")
				+ "\\scr_" + message.replace(" ", "") + timeStamp + "."
				+ extension;
		reporter.report("File path is :" + newFileName);
*/
		path = "http://"
				+ configuration.getGlobalProperties("logserverName").replace("\\",
						"") + "/"
				+ configuration.getProperty("screenshotFolder") + "/"
				+ message.replace(" ", "") + timeStamp + "." + extension;
		return path;
	}

	public WebElement getTableTdByName(String tableId, String text)
			throws Exception {
		WebElement result = null;
		WebElement table = waitForElement(tableId, ByTypes.xpath);
		List<WebElement> allrows = table.findElements(By.tagName("tr"));
		for (WebElement row : allrows) {
			List<WebElement> cells = row.findElements(By.tagName("td"));
			for (WebElement cell : cells) {
				reporter.report(cell.getText());
				if (cell.getText().contains(text)) {
					result = cell;
					break;
				}
			}
		}
		return result;
	}

	public String getBrowserName() {
		return browserName;
	}

	public void setBrowserName(String browserName) {
		this.browserName = browserName;
	}

	public boolean isInitialized() {
		return initialized;
	}

	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

	/**
	 * get the current url that the browser has in the address bar
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getUrl() throws Exception {
		// reporter.report(webDriver.getCurrentUrl());

		boolean found = false;
		int elapsedTime = 0;
		String url = null;
		while (found == false) {
			url = webDriver.getCurrentUrl();
			if (url.length() > 0) {
				found = true;
			} else {
				Thread.sleep(1000);
				elapsedTime++;
				if (elapsedTime >= getTimeout() + 10) {
					break;
				}
			}
		}

		return url;

	}

	public DbService getDbService() {
		return dbService;
	}

	public void clickOnElement(WebElement td) {
		td.click();

	}

	public void switchToTopMostFrame() {
		webDriver.switchTo().defaultContent();
		//ToDo, added by David: check how to add wait. Many test failed here. when back from tms to ED before login
	}

	public void switchToMainWindow() throws Exception {
		webDriver.switchTo().window(
				(String) webDriver.getWindowHandles().toArray()[0]);
		switchToTopMostFrame();

	}

	/**
	 * Perform drag and drop between two element. This is mostly used to answer
	 * Questions
	 * 
	 * @param from
	 * @param to
	 */
	public void dragAndDropElement(WebElement from, WebElement to) {

		(new Actions(webDriver)).dragAndDrop(from, to).perform();
	}

	/**
	 * maximize the browser window. Will not work on Android and Safari
	 */
	public void maximize() {
		try {
			webDriver.manage().window().maximize();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setSize(int x, int y) {
		try {
			Dimension d = new Dimension(x, y);
			webDriver.manage().window().setSize(d);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public WebElement getChildElementByCss(WebElement element, String css)
			throws Exception {
		WebElement childElement = null;
		try {
			WebDriverWait wait = new WebDriverWait(webDriver, timeout);
			wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By
					.cssSelector(css)));

			childElement = element.findElement(By.cssSelector(css));
		} catch (NoSuchElementException e) {
			// TODO Auto-generated catch block
			testResultService.addFailTest("Child element with css :" + css
					+ " was not found");
			printScreen("child element not found");

		}
		return childElement;
	}

	public WebElement getChildElementByXpath(WebElement element, String xpath)
			throws Exception {
		WebElement childElement = null;
		try {
			WebDriverWait wait = new WebDriverWait(webDriver, timeout);
			wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By
					.xpath(xpath)));

			childElement = element.findElement(By.xpath(xpath));
		} catch (NoSuchElementException e) {
			// TODO Auto-generated catch block
			testResultService.addFailTest("Child element with xpath :" + xpath
					+ " was not found");
			printScreen("child element not found");

		} catch (TimeoutException e) {
			testResultService.addFailTest("Child element with xpath :" + xpath
					+ " was not found");
			printScreen("child element not found");
		}
		return childElement;
	}

	public List<WebElement> getChildElementsByXpath(WebElement element,
			String xpath) {
		return element.findElements(By.xpath(xpath));
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public String getSutSubDomain() {
		return sutSubDomain;
	}

	public void setSutSubDomain(String sutSubDomain) {
		this.sutSubDomain = sutSubDomain;
	}

	public void setInstitutionName(String name) {
		this.institutionnName = name;
	}

	public String getIntitutionName() {
		return this.institutionnName;
	}

	public String getCssValue(WebElement element, String cssParam)
			throws Exception {
		String value = null;
		try {
			value = element.getCssValue(cssParam);
		} catch (Exception e) {
			testResultService.addFailTest("Element with css param " + cssParam
					+ " was null");
		}
		return value;
	}

	public void getElementLocation(WebElement element) {
		Point p = element.getLocation();
		reporter.report("X is: " + p.getX() + " and Y is: " + p.getY());
	}

	/**
	 * Try to avoid. will not work on IE
	 * 
	 * @param element
	 * @return
	 * @throws Exception
	 */
	public String getElementHTML(WebElement element) throws Exception {
		String html = element.getAttribute("innerHTML");
		reporter.report("Element HTMl is: " + html);

		return html;
	}

	public JSONObject getWebDriverJson() throws Exception {
		// String hub = "grid_server_host"; //IP or hostname of GRID

		int port = 4444; // port no.

		HttpHost host = new HttpHost("10.1.0.56", port);

		DefaultHttpClient client = new DefaultHttpClient();

		String url = host + "/grid/api/testsession?session=";

		URL session = new URL(url
				+ ((RemoteWebDriver) webDriver).getSessionId());

		BasicHttpEntityEnclosingRequest req;

		req = new BasicHttpEntityEnclosingRequest("POST",
				session.toExternalForm());

		org.apache.http.HttpResponse response = client.execute(host, req);

		JSONObject object = new JSONObject(EntityUtils.toString(response
				.getEntity()));

		return object;
	}

	public String getRemoteMachine() {
		return remoteMachine;
	}

	public void setRemoteMachine(String remoteMachine) {
		this.remoteMachine = remoteMachine;
	}

	/**
	 * use to click on a specific plave in an element or next to/outside an
	 * element
	 * 
	 * @param element
	 * @param X_offset
	 *            positive - right to, negative - left to
	 * @param Y_offset
	 *            posotive - under, negative - above
	 * @throws Exception
	 */
	public void clickOnElementWithOffset(WebElement element, int X_offset,
			int Y_offset) throws Exception {
		try {
			Actions builder = new Actions(webDriver);
			Action action = builder.moveToElement(element, X_offset, Y_offset)
					.click().build();
			action.perform();
		} catch (Exception e) {
			testResultService.addFailTest(
					"Failed while clickOnElementWithOffset", false, true);
		}
	}

	public boolean isEnableConsoleLog() {
		return enableConsoleLog;
	}

	/**
	 * set True to use browser console logs
	 * 
	 * @param enableConsoleLog
	 */
	public void setEnableConsoleLog(boolean enableConsoleLog) {
		this.enableConsoleLog = enableConsoleLog;
	}

	public LogEntries getConsoleLogEntries() {
		LogEntries logEntries = webDriver.manage().logs().get(LogType.BROWSER);

		// for (LogEntry entry : logEntries) {
		// reporter.report(entry.getMessage());
		// // do something useful with the data
		// }
		return logEntries;
	}

	public void hoverOnElement(WebElement element) throws Exception {
		Actions builder = new Actions(webDriver);
		builder.moveToElement(element).perform();

		// Thread.sleep(2000);

	}

	// public void hoverOnElementAndClickOnOtherElement(WebElement hoverElement,
	// String xpathToClick, int xOffset) throws Exception {
	// Actions builder = new Actions(webDriver);
	// builder.moveToElement(hoverElement, xOffset, 0)
	// .click(webDriver.findElement(By.xpath(xpathToClick))).build()
	// .perform();
	//
	// }

	// public void clickOnElementAndThenClickOnOtherElemenet(
	// WebElement baseElement, String xpathOfOtherElement)
	// throws Exception {
	// Actions builder = new Actions(webDriver);
	// builder.click(baseElement)
	// .click(webDriver.findElementByXPath(xpathOfOtherElement))
	// .build().perform();
	// }

	public void hoverOnElement(WebElement element, int x, int y)
			throws Exception {
		Actions builder = new Actions(webDriver);
		builder.moveToElement(element, x, y).perform();
		Thread.sleep(2000);

	}

	/**
	 * execute javascript code on the browser
	 * 
	 * @param script
	 * @throws Exception
	 */
	public void executeJsScript(String script) throws Exception {
		executeJsScript(script, null);
	}

	/**
	 * 
	 * @param script
	 * @param arguments
	 * @return
	 * @throws Exception
	 */
	public String executeJsScript(String script, Object... arguments)
			throws Exception {
		// ScriptEngineManager factory = new ScriptEngineManager();
		// // create a JavaScript engine
		// ScriptEngine engine = factory.getEngineByName("JavaScript");
		// engine.eval(script);
		// evaluate JavaScript code from String
		if (arguments != null) {
			return (String) ((JavascriptExecutor) webDriver).executeScript(
					script, arguments);
		} else {
			return (String) ((JavascriptExecutor) webDriver)
					.executeScript(script);
		}

	}

	public void waitForJSFunctionToEnd(String function) {
		String script = "var callback = arguments[arguments.length - 1];"
				+ "callback(" + function + "());";

		try {
			webDriver.manage().timeouts()
					.setScriptTimeout(15, TimeUnit.SECONDS);
			((JavascriptExecutor) webDriver).executeAsyncScript(script);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Wait until an element is actually clickable (genral waitForElement waits
	 * until an element is visible)
	 * 
	 * @param xpath
	 * @param timeout
	 * @return
	 */
	public boolean waitUntilElementClickable(String xpath, int timeout) {
		WebDriverWait wait = new WebDriverWait(webDriver, timeout);
		WebElement element = wait.until(ExpectedConditions
				.elementToBeClickable(By.xpath(xpath)));

		if (element == null) {
			testResultService.addFailTest("Element was not clickable after "
					+ timeout);
			return false;
		} else {
			return true;
		}

	}

	/**
	 * Use to inject values into the browser cookie, for example to bypass
	 * login. Example: webdriver.addValuesToCookie( "Student",
	 * "^StudentID*3000025000321^Language*spa^LangSupLevel*3^Courses*0^FName*TMS^LName*Domain^SID*37537^CMode*L^UserType*da^Type*2^LCE*"
	 * );
	 * 
	 * @param cookieName
	 * @param value
	 */
	public void addValuesToCookie(String cookieName, String value) {
		try {
			Cookie cookie = new Cookie(cookieName, value);
			webDriver.manage().addCookie(cookie);
			reporter.report("Cookie added");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// public RemoteWebDriver getWebDriver() {
	// return webDriver;
	// }

	public WebDriver getWebDriver() {
		return webDriver;
	}

	// public void setReporter(services.Reporter reporter) {
	// this.reporter = reporter;
	// }

	public void setTestResultService(TestResultService testResultService) {
		this.testResultService = testResultService;

	}

	public void selectElementFromComboBox(String comboboxName,
			String optionValue) throws Exception {
		selectElementFromComboBox(comboboxName, ByTypes.id, optionValue);
	}

	public void selectElementFromComboBox(String comboboxName, ByTypes byType,
			String optionValue) throws Exception {
		//waitUntilComboBoxIsPopulated(comboboxName, byType);
		selectElementFromComboBox(comboboxName, optionValue, byType, false);
	}
	
	public void selectValueFromComboBox(String comboboxName, String optionValue) throws Exception {
		selectValueFromComboBox(comboboxName, optionValue, ByTypes.id);
	}

	public void selectElementFromComboBoByIndex(String comboboxName, int index)
			throws Exception {
		selectElementFromComboBoByIndex(comboboxName, ByTypes.id, index);
	}

	public void selectElementFromComboBoByIndex(String comboboxName,
			ByTypes byType, int index) throws Exception {
		boolean selected = false;
		{
			try {
				Select select = new Select(waitForElement(comboboxName, byType));
				List<WebElement> options = select.getOptions();
				select.selectByIndex(index);

			}

			catch (UnhandledAlertException e) {
				getUnexpectedAlertDetails();
			}

			catch (Exception e) {
				printScreen("problem selecting from combo box");
				testResultService.addFailTest(
						"problem selecting from combo box", true, true);
				e.printStackTrace();
			}

			reporter.report("Selected " + comboboxName + ": " + selected);
		}
	}

	public void selectElementFromComboBox(String comboboxName,
			String optionValue, boolean contains) throws Exception {
		selectElementFromComboBox(comboboxName, optionValue, ByTypes.id,contains);
		
	}

	public void selectElementFromComboBox(String comboboxName,
			String optionValue, ByTypes byType, boolean contains)
			throws Exception {
		boolean selected = false;

		String[] optionValues = null;
		{
			try {
				Select select = new Select(waitForElement(comboboxName, byType));
				// waitForElement(comboboxName, ByTypes.id);
				List<WebElement> options = select.getOptions();
				optionValues = new String[options.size()];

				for (int j = 0; j < options.size(); j++) {
					optionValues[j] = (options.get(j).getText());
					reporter.report("option number " + j + " is: " + optionValues[j]);
				}
				for (int i = 0; i < options.size(); i++) {

					if (contains == false) {
						
						if (options.get(i).getText().equalsIgnoreCase(optionValue)) {
							select.selectByIndex(i);
							reporter.report("option " + optionValue
									+ " selected");
							selected = true;
							break;
						}
					} else {
						if (options.get(i).getText().contains(optionValue)) {
							select.selectByIndex(i);
							reporter.report("option " + optionValue
									+ " selected");
							selected = true;
							break;
						}
					}
				}
				if (selected == false) {
					testResultService.addFailTest(optionValue
							+ " was not found in the combo box", true, true);
				}
			}

			catch (UnhandledAlertException e) {
				getUnexpectedAlertDetails();
			}

			catch (Exception e) {
				reporter.report(e.toString());
				reporter.report("Options were: "
						+ textService.printStringArray(optionValues));
				printScreen("problem selecting from combo box");
				testResultService.addFailTest(
						"problem selecting from combo box", true, true);
				e.printStackTrace();
			}

			reporter.report("Selected " + comboboxName + ": " + selected);
		}
	}
	
	public void selectValueFromComboBox(String comboboxName,String optionValue, ByTypes byType) throws Exception {
		boolean selected = false;

		{
			try {
				Select select = new Select(waitForElement(comboboxName, byType));
				select.selectByVisibleText(optionValue);
				reporter.report("option <B>" + optionValue + "</B> selected");
				selected = true;

				if (selected == false) {
					testResultService.addFailTest(optionValue + " was not found in the combo box", true, true);
				}
			}

			catch (UnhandledAlertException e) {
				getUnexpectedAlertDetails();
			}

			catch (Exception e) {
				reporter.report(e.toString());
				printScreen("problem selecting from combo box");
				testResultService.addFailTest(
						"problem selecting from combo box", true, true);
				e.printStackTrace();
			}

			reporter.report("Selected " + comboboxName + ": " + selected);
		}
	}

	public List<String> getConsoleLogs() throws Exception {
		return getConsoleLogs(null, false);
	}

	public List<String> getConsoleLogs(String logFilter, boolean useFllter)
			throws Exception {
		List<String> logList = null;
		textService = new TextService();
		LogEntries logEntries = getConsoleLogEntries();
		logList = textService.getListFromLogEntries(logEntries, logFilter,
				useFllter);
		return logList;
	}

	public List<String> printConsoleLogs(String logFilter, boolean useFllter)
			throws Exception {
		List<String> logList = null;
		try {
			logList = getConsoleLogs(logFilter, useFllter);
			// TD DO change to SMB auth
			// NetService netService = new NetService();

			// String tempCsvFile = "files/csvFiles/temp" + dbService.sig(6);
			// SmbFile sFile = new SmbFile(tempCsvFile);
			// textService.writeArrayistToCSVFile(tempCsvFile, logList);
			NtlmPasswordAuthentication auto = netService.getAuth();

			String path = "smb://" + configuration.getLogerver()
					+ "Logs/automationLogs/consoleLog" + dbService.sig() + ".csv";
			textService.writeListToSmbFile(path, logList, netService.getAuth());

			// SmbFileOutputStream outputStream = new
			// SmbFileOutputStream(smbFile);
			// outputStream.write(b);
			// textService.writeArrayistToCSVFile(path, logList);
			reporter.report("Console log can be found in: " + path);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return logList;
	}

	public String getSelectedValueFromComboBox(String comboBoxId)
			throws Exception {
		WebDriverWait wait = new WebDriverWait(webDriver, timeout + 10, 1000);
		WebElement option = null;
		try {
			waitUntilComboBoxIsPopulated(comboBoxId);
			wait.until(ExpectedConditions.elementToBeClickable(By
					.id(comboBoxId)));
			Select select = new Select(webDriver.findElement(By.id(comboBoxId)));

			option = select.getFirstSelectedOption();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			testResultService.addFailTest("getting value from combo box: "
					+ comboBoxId + " failed");
			printScreen();
		}

		return option.getText();
	}
	
	
	public String getSelectedAttributeValueFromComboBox(String comboBoxId)
			throws Exception {
		WebDriverWait wait = new WebDriverWait(webDriver, timeout + 10, 1000);
		WebElement option = null;
		try {
			waitUntilComboBoxIsPopulated(comboBoxId);
			wait.until(ExpectedConditions.elementToBeClickable(By
					.id(comboBoxId)));
			Select select = new Select(webDriver.findElement(By.id(comboBoxId)));

			option = select.getFirstSelectedOption();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			testResultService.addFailTest("getting value from combo box: "
					+ comboBoxId + " failed");
			printScreen();
		}

		return option.getAttribute("value");
	}

	/**
	 * Set the Selenium WebDriver default wait for page load time
	 */
	public void setPageLoadTimeOut() {

		String ignoreTimeOut = System.getProperty("ignoreTimeOuts");

		if (ignoreTimeOut != null && ignoreTimeOut.equals("true")) {
			webDriver.manage().timeouts()
					.pageLoadTimeout(timeout + 10, TimeUnit.SECONDS);
		}

	}

	public void setScriptLoadTimeOut() {
		String ignoreTimeOut = System.getProperty("ignoreTimeOuts");

		if (ignoreTimeOut != null && ignoreTimeOut.equals("true"))
			webDriver.manage().timeouts()
					.setScriptTimeout(timeout, TimeUnit.SECONDS);
	}

	public String getPopUpText() throws Exception {

		String alertText = null;
		try {

			WebDriverWait wait = new WebDriverWait(webDriver, timeout, 1000);
			if (wait.until(ExpectedConditions.alertIsPresent()) != null) {
				alertText = webDriver.switchTo().alert().getText();
			}
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			testResultService.addFailTest("Alert was not found", false, true);
		}
		return alertText;

	}

	public void waitUntilComboBoxIsPopulated(String comboBoxId)

	throws Exception {
		waitUntilComboBoxIsPopulated(comboBoxId, ByTypes.id);
	}

	/**
	 * Waits until all element are loaded in the combovox
	 * 
	 * @param comboBoxId
	 * @param byType
	 * @throws Exception
	 */
	public void waitUntilComboBoxIsPopulated(String comboBoxId, ByTypes byType)
			throws Exception {
		final Select combo = new Select(waitForElement(comboBoxId, byType));
	
		try {
			new FluentWait<WebDriver>(webDriver)
					.withTimeout(20, TimeUnit.SECONDS)
					//.pollingEvery(1, TimeUnit.SECONDS).until(ExpectedConditions.visibilityOfAllElements(combo.getOptions()))
					/*.until(new Predicate<WebDriver>() {
						.until(new Predicate<WebDriver>() {

						public boolean apply(WebDriver webdriver) {
							// TODO Auto-generated method stub
							return (!combo.getOptions().isEmpty());
						}
					})*/;
		} catch (UnhandledAlertException e) {
			// TODO Auto-generated catch block
			getUnexpectedAlertDetails();
		} catch (TimeoutException e) {
			testResultService
					.addFailTest("Combobox is not filled after 20 seconds");
		}
	}

	/**
	 * Use this method to get alert text, when unexpected alert opens in the
	 * browser
	 * 
	 * @throws Exception
	 */
	public void getUnexpectedAlertDetails() throws Exception {

		String alertText = getAlertText(5);
		reporter.report("Alert text was: " + alertText);

	}

	public WebElement findElementByXpath(String value, ByTypes byType) {
		return webDriver.findElement(By.xpath(value));
	}

	/**
	 * @deprecated do not use. use selectElementFromComboBox instead
	 * @param hoverElement
	 * @param comboboxName
	 * @param value
	 * @throws Exception
	 */
	public void HoverOnElementAndmoveToComboBoxElementAndSelectValue(
			WebElement hoverElement, String comboboxName, String value)
			throws Exception {
		// Actions actions = new Actions(webDriver);
		// actions.moveToElement(hoverElement)
		// .moveToElement(webDriver.findElementById(comboboxName))
		// .clickAndHold().perform();
		// selectElementFromComboBox(comboboxName, value);

		Actions actions = new Actions(webDriver);
		actions.moveToElement(hoverElement)
				.moveToElement(webDriver.findElement(By.id(comboboxName)))
				.clickAndHold().perform();
		selectElementFromComboBox(comboboxName, value);

	}

	public int getWindowWidth() {
		return webDriver.manage().window().getSize().getWidth();
	}

	public int getWindowHeight() {
		return webDriver.manage().window().getSize().getHeight();
	}

	/**
	 * Use this to highligh element what you are writing tests to make sure you
	 * are using the correct element. Also may be used when taking screenshots
	 * 
	 * @param element
	 */

	/**
	 * Does not work in firefox.
	 * 
	 * @param element
	 */
	public void highlightElement(WebElement element) {
		JavascriptExecutor js = (JavascriptExecutor) webDriver;
		js.executeScript("arguments[0].setAttribute('style', arguments[1]);",

		element, "color: blue; border: 2px solid red;");
	}

	public void changeElementStyle(WebElement element, String style) {
		JavascriptExecutor js = (JavascriptExecutor) webDriver;
		js.executeScript("arguments[0].setAttribute('style', arguments[1]);",
				element, style);
	}

	/**
	 * Use this method to wait for all Jquery request to finish to validate that
	 * all elements in the page are loaded
	 * 
	 * @throws Exception
	 */
	public void waitForJqueryToFinish() throws Exception {
		try {
			long startime = System.currentTimeMillis();
			// reporter.report("startted: "+System.currentTimeMillis());
		
		/*	 JavascriptExecutor js = (JavascriptExecutor)webDriver;
			 boolean isReady = (Boolean) js.executeScript("return jQuery.active == 0");
			int c=0;
			*/
			
			new WebDriverWait(webDriver, 1800)
					.until(new ExpectedCondition<Boolean>() {
						public Boolean apply(WebDriver driver) {
							 JavascriptExecutor js = (JavascriptExecutor)driver;
							 return (Boolean) js.executeScript("return jQuery.active == 0");
						//	return runJavascript("return jQuery.active == 0");

						}
					});
			long finishTime = System.currentTimeMillis();
			finishTime = finishTime - startime;
			reporter.report("finished. took: " + finishTime + "ms");
		} 
		
		catch (TimeoutException e) {
			testResultService.addFailTest("Jquery timed out", false, true);
		}
		
		catch (WebDriverException e) {
			// TODO Auto-generated catch block
		//	printScreen();
		//	e.printStackTrace();
		} 
	}

	public boolean runJavascript(String script) {
		JavascriptExecutor js = (JavascriptExecutor) webDriver;
		return (Boolean) js.executeScript(script);
	}

	public void checkForBrokenImages() {
		// List<WebElement> imageElements =
		// webDriver.findElementsByTagName("img");
		// for (int i = 0; i < imageElements.size(); i++) {
		//
		//
		// isImageLoaded(imageElements.get(i));
		// }

		List<WebElement> imageElements = webDriver.findElements(By
				.tagName("img"));
		for (int i = 0; i < imageElements.size(); i++) {

			isImageLoaded(imageElements.get(i));
		}
	}

	public boolean isImageLoaded(WebElement image) {

		Boolean imageLoaded = (Boolean) ((JavascriptExecutor) webDriver)
				.executeScript(
						"return (typeof arguments[0].naturalWidth!=\"undefined\""
								+ " && arguments[0].naturalWidth>0)", image);
		
		if (imageLoaded) {
			reporter.report("image found");
		} else {
			reporter.report("image not found");
		}
		
		return imageLoaded;
	}

	public void setBrowserWidth(int width) throws Exception {
		try {
			int height = webDriver.manage().window().getSize().getHeight();
			webDriver.manage().window().setSize(new Dimension(width, height));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * waits until element text if longer then 0
	 * 
	 * @param xpath
	 * @throws Exception
	 */
	public void waitUntilTextIsLoadedInElement(final String xpath)
			throws Exception {
		new WebDriverWait(webDriver, 60)
				.until(new ExpectedCondition<Boolean>() {
					public Boolean apply(WebDriver w) {
						return w.findElement(By.xpath(xpath)).getText()
								.length() > 0;
					}

				});
	}

	public void waitUntilTextIsClearedFromElement(final String xpath)
			throws Exception {
		new WebDriverWait(webDriver, 60)
				.until(new ExpectedCondition<Boolean>() {
					public Boolean apply(WebDriver w) {
						return w.findElement(By.xpath(xpath)).getText()
								.length() == 0;
					}

				});
	}

	/**
	 * use this to log request and responses to analyze test. works only when
	 * the tests run on the lcal machine (you can use jenkins slave)
	 * 
	 * @param url
	 * @throws Exception
	 */
	public void startProxyServer() throws Exception {
		// System.out.println("Starting proxy server");
		// report.startStep("Starting proxy server");
		setUseProxy(true);
		String PROXY = "localhost:4040";
		server = new ProxyServer(4040);
		server.start();
		proxy = server.seleniumProxy();
		proxy.setHttpProxy(PROXY).setSslProxy(PROXY);

		// proxy.setHttpProxy(PROXY).setSslProxy(PROXY);

	}

	// public void limitConnectionBandwidth(int kbps) throws Exception {
	// ((BrowserMobProxy) server).setReadBandwidthLimit(kbps * 1024);
	// }

	public void startProxyLister(String site) {
		server.newHar(site);
	}

	public void stopProxyListen() throws Exception {
		try {
			if (useProxy) {
				Har har = getHar();
				if (har != null) {
					String file = "files/proxyOutput" + dbService.sig(6)
							+ ".txt";
					// FileOutputStream fos = new FileOutputStream(file);
					// har.writeTo(fos);
				}
				server.stop();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Har getHar() {
		return server.getHar();

	}

	public void printProxyOutputToFile() throws Exception {

		List<String[]> list = new ArrayList<String[]>();

		Har har = getHar();
		for (int i = 0; i < har.getLog().getEntries().size(); i++) {
			HarEntry entry = har.getLog().getEntries().get(i);

			list.add(new String[] { entry.getRequest().getUrl(),
					String.valueOf(entry.getResponse().getStatus()),
					entry.getResponse().getStatusText() });
		}
		textService.writeArrayistToCSVFile(
				"files/proxyOutput/netLog" + dbService.sig(5) + ".csv", list);
	}

	public boolean isUseProxy() {
		return useProxy;
	}

	public void setUseProxy(boolean useProxy) {
		this.useProxy = useProxy;
	}

	public List<WebElement> getElementsByXpath(String xpath) throws Exception {
		return webDriver.findElements(By.xpath(xpath));
	}
	
	public List<WebElement> getElementsByLocator(By locator) throws Exception {
		return webDriver.findElements(locator);
	}

	public String getBrowserVersion() {
		// Capabilities capabilities = webDriver.getCapabilities();
		// String version = capabilities.getVersion();
		// return version;
		return null;
	}

	public Screenshot takeElementScreenShot(WebElement element)
			throws Exception {
		Screenshot screenshot = new AShot().takeScreenshot(webDriver, element);
		BufferedImage image = screenshot.getImage();
		File file = new File(System.getProperty("user.dir") + "/log//current/"
				+ element.getAttribute("name") + dbService.sig(6) + ".jpg");
		ImageIO.write(image, "jpg", file);

		return screenshot;
	}

	// public String takeElementScreenshot(WebElement element, String message)
	// throws Exception {
	//
	// TestRunnerType runner = TestRunnerType.CI;
	// String path = null;
	// // If test is running using jenkins ci
	// if (runner == TestRunnerType.CI) {
	//
	// path = getScrPathForCI(message, dbService.sig(), "png");
	// } else {
	// path = getScrPathForLocal(message, dbService.sig(), "png");
	// }
	//
	// WrapsDriver wrapsDriver = (WrapsDriver) element;
	// File screenshot = ((TakesScreenshot) wrapsDriver.getWrappedDriver())
	// .getScreenshotAs(OutputType.FILE);
	// Rectangle rectangle = new Rectangle(element.getSize().width,
	// element.getSize().height);
	// Point location = element.getLocation();
	// BufferedImage bufferedImage = ImageIO.read(screenshot);
	// BufferedImage destImage = bufferedImage.getSubimage(location.x,
	// location.y, rectangle.width, rectangle.height);
	// ImageIO.write(destImage, "png", screenshot);
	//
	// if (runner == TestRunnerType.local) {
	// File file = new File(path);
	// FileUtils.copyFile(screenshot, file);
	// } else {
	// NetService netService = new NetService();
	// String sFileName = "scr_" + dbService.sig(8)
	// + message.replace(" ", "") + ".png";
	// SmbFile smbFile = new SmbFile("smb://"
	// + configuration.getLogerver() + "/automationScreenshots/"
	// + sFileName, netService.getAuth());
	// SmbFileOutputStream smbFileOutputStream = new SmbFileOutputStream(
	// smbFile);
	// // FileOutputStream fos = new FileOutputStream(new
	// // File(newFileName));
	// // fos.write(decodedScreenshot);
	// // smbFileOutputStream.write(decodedScreenshot);
	// // smbFileOutputStream.write(bufferedImage.get);
	// smbFileOutputStream.close();
	// }
	//
	// return path;
	// }

	public void saveImage(String message, BufferedImage bufferedImage)
			throws Exception {
		String fileExt = "png";
		TestRunnerType runner = getTestRunner();
		String sig = dbService.sig();
		String newFileName;
		String path;
		runner = runnerType.CI;

		try {
			if (runner == TestRunnerType.CI) {

				newFileName = "\\\\" + configuration.getGlobalProperties("logserverName")
						+ "\\AutoLogs\\"
						+ configuration.getProperty("screenshotFolder")
						+ "\\automationScreenshots" + message.replace(" ", "") + sig + "."
						+ scrFileExt;
				reporter.report("File path is :" + newFileName);

				path = "https://"
						+ configuration.getGlobalProperties("urlLogFiles")
								.replace("\\", "") + "/"
						+ configuration.getProperty("screenshotFolder")
						+ "/ScreenShot" + message.replace(" ", "") + sig + "."
						+ scrFileExt;

			} else if (runner == TestRunnerType.local) {
				newFileName = System.getProperty("user.dir") + "/log//current/"
						+ "ScreenShot" + message.replace(" ", "") + sig + "."
						+ scrFileExt;
				path = System.getProperty("user.dir") + "//" + "log//current//"
						+ "ScreenShot" + message.replace(" ", "") + sig + "."
						+ scrFileExt;
			}

			// if (runner == TestRunnerType.CI) {
			// // **printscreen using smbFile
			// NetService netService = new NetService();
			// String sFileName = "scr_" + dbService.sig(8)
			// + message.replace(" ", "") + "." + scrFileExt;
			// SmbFile smbFile = new SmbFile("smb://"
			// + configuration.getLogerver()
			// + "/automationScreenshots/" + sFileName,
			// netService.getAuth());
			// SmbFileOutputStream smbFileOutputStream = new
			// SmbFileOutputStream(
			// smbFile);
			// // FileOutputStream fos = new FileOutputStream(new
			// // File(newFileName));
			// // fos.write(decodedScreenshot);
			// // smbFileOutputStream.write(decodedScreenshot);
			// smbFileOutputStream.close();
			//
			// ImageIO.write(bufferedImage, scrFileExt, smbFileOutputStream);
			// reporter.report("http://newjenkins/automationScreenshots/"
			// + sFileName);
			// } else {
			newFileName = System.getProperty("user.dir") + "/log//current/"
					+ "ScreenShot" + message.replace(" ", "") + sig + "."
					+ scrFileExt;
			path = System.getProperty("user.dir") + "//" + "log//current//"
					+ "ScreenShot" + message.replace(" ", "") + sig + "."
					+ scrFileExt;

			// File file = new File(path);
			File file = new File("C:\\" + message + ".png");
			// FileOutputStream fos = new FileOutputStream(new File(
			// newFileName));
			// fos.write(decodedScreenshot);
			ImageIO.write(bufferedImage, scrFileExt, file);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Reporter getReporter() {
		return this.reporter;
	}

	/**
	 * switch to the next tab that will open in the browser
	 * @throws InterruptedException 
	 */
	public void switchToNextTab() throws InterruptedException {
		List<String> tabs = new ArrayList<String>();
		tabs = new ArrayList<String>(webDriver.getWindowHandles());
		
		for (int i=1; tabs.size()<2 && i<15; i++){
			tabs = new ArrayList<String>(webDriver.getWindowHandles());
			reporter.report("Number of tabs: "+tabs.size());
			Thread.sleep(500);
		}
		webDriver.switchTo().window(tabs.get(1));
	}
	
	public void switchToPreviousTab() {
		List<String> tabs = new ArrayList<String>(webDriver.getWindowHandles());
		// reporter.report("Number of tabs: "+tabs.size());
		webDriver.switchTo().window(tabs.get(0));

	}
	
	public void closeNewTab(int numOfTabs) {
		Set<String> winSet = webDriver.getWindowHandles();
		List<String> winList = new ArrayList<String>(winSet);
		String originalTab = winList.get(winList.size()-2);
		webDriver.close(); // close the new tab
		webDriver.switchTo().window(originalTab); // switch to original tab
		
	}
	
	public void checkNumberOfTabsOpened(int number) throws Exception { 
		List<String> tabs = new ArrayList<String>(webDriver.getWindowHandles());
		testResultService.assertEquals(number, tabs.size(), "Checking that only "+number+" one tab opened");
			
	}
	

	public services.Configuration getConfiguration() {
		return configuration;
	}

	public String waitForSpecificCurrentUrl(String currentUrl, String prefix)
			throws Exception {
		for (int i = 0; i < timeout; i++) {
			currentUrl = getUrl();
			// reporter.report("currentUrl: "+currentUrl);
			if (currentUrl.contains(prefix)) {
				break;
			} else {
				sleep(1);
			}
		}
		return currentUrl;
	}

	public String waitForSpecificCurrentUrl(String expectedUrl)
			throws Exception {
		
		return this.waitForSpecificCurrentUrl("", expectedUrl);
	}
	public void setCheckBoxState(boolean setChecked, String id)
			throws Exception {

		WebElement checkboxElemnt = waitForElement(id, ByTypes.id);
		boolean state = checkboxElemnt.isSelected();
		if (state == false && setChecked == true) {
			// click to check
			checkboxElemnt.click();
		} else if (state == true && setChecked == false) {
			// click to uncheck
			checkboxElemnt.click();
		}
	}

	public void waitForAngularToFinish() throws Exception {
		executeJsScript("var callback = arguments[arguments.length - 1];"
				+ "angular.element(document.body).injector().get('$browser').notifyWhenNoOutstandingRequests(callback);");

	}

	public void scrollToElement(WebElement element) throws Exception {
		scrollToElement(element, 50);
	}

	/**
	 * 
	 * @param element
	 * @param yOffset
	 *            negative - above, positive - under
	 * @throws Exception
	 */
	public void scrollToElement(WebElement element, int yOffset)
			throws Exception {
		// executeJsScript("arguments[0].scrollIntoView(true);", element);
		
		Actions actions = new Actions(webDriver);
		sleep(2);
		try {
			actions.moveToElement(element, 0, yOffset).build().perform();
		}catch (Exception e) {
			System.out.println("=================");
		}
	}
	
	public void hideFooter(String selector)throws Exception {
		JavascriptExecutor js = (JavascriptExecutor)webDriver;
		js.executeScript("document.querySelector('"+selector+"').style.display = 'none'");
	
	}
	
	

	// public void scollInElement(WebElement element, int yOffset)
	// throws Exception {
	// executeJsScript("arguments[0].scrollTop = arguments[1];", new Object[] {
	// element, -100 });
	//
	// }

	public void scrollToTopOfPage() throws Exception {
		executeJsScript("window.scrollTo(0, 0)");

	}

	public void scrollToBottomOfPage() throws Exception {
		executeJsScript("window.scrollTo(0, 2000)");

	}
	
	public void dragScrollElement(WebElement element, int yOffset) {
		Actions dragger = new Actions(webDriver);
		dragger.moveToElement(element).clickAndHold().moveByOffset(0, yOffset)
				.release().perform();
	}

	public void moveToElementAndClick(WebElement element) {
		Actions actions = new Actions(webDriver);
		actions.moveToElement(element).click().perform();

	}

	/**
	 * Scroll to specific point in the screen
	 * 
	 * @param x
	 * @param y
	 * @throws Exception
	 */
	public void scrollToTo(int x, int y) throws Exception {
		executeJsScript("window.scrollTo(" + x + ", " + y + ")");

	}

	/**
	 * 
	 * 
	 * 
	 * @param element
	 * @return String: waits until text is displayed in the element and returns
	 *         it. if text is not displayed after the default timeout, return
	 *         null
	 * @throws Exception
	 */
	public String getElementText(WebElement element) throws Exception {
		String text = null;
		int elapsedTime = 0;
		boolean textFound = false;
		while (textFound == false) {
			if (element.getText().length() > 0) {
				text = element.getText();
				textFound = true;
			} else {
				Thread.sleep(1000);
				elapsedTime++;
				if (elapsedTime >= getTimeout()) {
					testResultService.addFailTest(
							"Text not found after timeout", false, true);
					break;
				}
			}
		}
		return text;
	}

	public String getElementSrc(WebElement audioPlayer) throws Exception {
		return executeJsScript("return arguments[0].attributes['src'].value;",
				audioPlayer);

	}

	public static BufferedImage loadImage(String path) {
		try {
			File file = new File(path);
			BufferedImage image = ImageIO.read(file);
			return image;

		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	public void checkConsoleLogsForErrors() throws Exception {
		List<String> list = getConsoleLogs();
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).contains("Failed")) {
				testResultService.addFailTest("Failed entry in console log: "
						+ list.get(i));
			}
		}

	}
	
	public void clickOnElementByJavaScript(WebElement element) {
		
		JavascriptExecutor js = (JavascriptExecutor) webDriver;
		js.executeScript("arguments[0].click();", element);
		//js.executeScript("document.getElementsByClassName('modal-close')[0].click();");
				
		
	}
	
	public WebElement getElementByJavaScript(String elementId) {
		
		JavascriptExecutor js = (JavascriptExecutor) webDriver;
		return (WebElement) js.executeScript("return document.getElementById('"+elementId+"');");
			
	}
	
	public void setBrowserResolution(int width, int height) {
		
		webDriver.manage().window().setSize(new Dimension(width, height));
		
	}
	
	public void validateURL(String wantedURL) throws Exception {
		String actualURL = getUrl();
		testResultService.assertEquals(true, actualURL.contains(wantedURL), "URL is Not Correct");
	}		
	
	public String getCurrentDate() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");  
		LocalDateTime now = LocalDateTime.now(); 
		return dtf.format(now).toString();
	}
	
	public void insertText(String text,String elementId) throws Exception{
		waitForElement(elementId,ByTypes.id).sendKeys(text);
	}
	
	public void clickAndHoldOnElement(WebElement element) throws Exception {
		Actions action = new Actions(webDriver);
		action.clickAndHold(element).build().perform();
	}
	
	public String getTextFromHoverMessageByHoverElement(WebElement elementToHoverOn, String hoverElementXpath) throws Exception {
		try {
			Thread.sleep(1000);
			
			//report.addTitle("Element" + elementToHoverOn + "; Display: " + isDisplayed(elementToHoverOn));
			
			if (isDisplayed(elementToHoverOn)) {
				hoverOnElement(elementToHoverOn);
				Thread.sleep(7000);
				WebElement hoverElement=null;
			//	WebElement hoverElement = waitForElement("//div[contains(@class,'progressPopover__InnerWrapper')]", ByTypes.xpath, false, 3);
				
				for (int i=0;i<=10 && hoverElement==null;i++){
					hoverElement = waitForElement(hoverElementXpath, ByTypes.xpath, false, 1);
				}
				//WebElement hoverElement = waitForElement(hoverElementXpath, ByTypes.xpath, false, 3);
				
				
				if (hoverElement != null) {
					return hoverElement.getText();
				} else {
					testResultService.addFailTest("Hover Text is Not Displayed When Hovering Over The Element.", false, true);
					return null;
				}
			} else {
				testResultService.addFailTest("Element To Hover On is Not Displayed.", false, true);
				return null;
			}	
			
			
	    }
		catch(Exception e)
		{
			System.out.println(e);
			return null;
		}
	
	}
	
	public String getTextFromHoverMessageByClickAndHoldElement(WebElement elementToHoverOn) throws Exception {
		if (isDisplayed(elementToHoverOn)) {
			clickAndHoldOnElement(elementToHoverOn);
			
			//Thread.sleep(2000);
			
			WebElement hoverElement = waitForElement("//div[contains(@class,'progressPopover__InnerWrapper')]", ByTypes.xpath, false, 3);
			if (hoverElement != null) {
				return hoverElement.getText();
			} else {
				testResultService.addFailTest("Hover Text is Not Displayed When Hovering Over The Element.");
				return null;
			}
		} else {
			testResultService.addFailTest("Element To Hover On is Not Displayed.");
			return null;
		}	
	}
	
	public void openIncognitoChromeWindow() throws Exception {
		openIncognitoChrome=true;
	} 
	
	// This function waits until element appears, and then continues (secToWait will stop once element is found)
	public boolean waitUntilElementAppears(String xpath, int secToWait) {
		WebElement element=null;
		boolean status;
		
		try {
			for (int i=0;i<=secToWait && element==null;i++){
				element = waitForElement(xpath, ByTypes.xpath, 1, false, "Element not loaded after: " + i + "seconds");
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (element !=null)
			status=true;
		else
			status = false;
	
		return status;
	}
	
	public WebElement waitUntilElementAppearsAndReturnElement(String xpath, int secToWait) {
		WebElement element=null;
		try {
			for (int i=0;i<=secToWait && element==null;i++){
				element = waitForElement(xpath, ByTypes.xpath, 1, false, "Element not loaded after: " + i + "seconds");
			}	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return element;
	}
	
	public WebElement waitUntilElementAppearsAndReturnElementByType(String value, ByTypes byType, int secToWait) {
		WebElement element=null;
		try {
			for (int i=0;i<secToWait && element==null;i++){
				element = waitForElement(value, byType, 1, false, "Element not loaded after: " + i + "seconds");
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return element;
	}
	
	// This function waits until element appears, and then continues (secToWait will stop once element is found)
		public WebElement waitUntilElementAppears(String elementValue,ByTypes byType) {
			int secToWait=5;
			WebElement element=null;
			try {
				for (int i=0;i<secToWait && element==null;i++){
					element = waitForElement(elementValue, byType, 1, false, "Element not loaded after: " + i + "seconds");
			}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return element;
		}
		
		
		public WebElement waitUntilElementAppears(String elementValue,ByTypes byType, int secToWait) {
			WebElement element=null;
			try {
				for (int i=0;i<=secToWait && element==null;i++){
					element = waitForElement(elementValue, byType, 1, false, "Element:" +elementValue+ ",Login area not loaded after: " + i + "seconds");
			}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			return element;
		}
		
		
		public boolean waitUntilElementAppearsAndReturn(String elementValue,ByTypes byType, int secToWait) {
			WebElement element=null;
			boolean status=false;
			
			try {
				for (int i=0;i<=secToWait && element==null;i++){
					element = waitForElement(elementValue, byType, 1, false, "Element not loaded after: " + i + "seconds");
					
					if (element!=null)
						status=true;
			}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return status;
		}
		
		
		public void waitUntilElementAppears(WebElement element, int secToWait) {
			boolean isDisplay = false;
			try {
				for (int i=0;i<=secToWait && !isDisplay;i++){
					isDisplay = isDisplayed(element);
					//webDriver. webDriver (elementValue, elementType, 1, false, "Element not loaded after: " + i + "seconds");
					sleep(1);
			}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public void waitUntilElementAppears(List<WebElement> element, int secToWait) {
			boolean isDisplay = false;
			try {
				for (int i=0;i<=secToWait && !isDisplay;i++){
					isDisplay = isDisplayed(element.get(0));
					//webDriver. webDriver (elementValue, elementType, 1, false, "Element not loaded after: " + i + "seconds");
					sleep(1);
			}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		}
		
		public boolean checkElementIsDisabled(WebElement element) throws Exception{
			boolean isDisabled = !element.isEnabled();
			testResultService.assertEquals(true, isDisabled, "Element is not Disabled");
			return isDisabled;
		}
		
		public boolean checkElementIsNotDisabled(WebElement element) throws Exception{
			boolean isDisabled = !element.isEnabled();
			testResultService.assertEquals(false, isDisabled, "Element is Disabled");
			return isDisabled;
		}
		
		public boolean checkElementIsDisabledByAttribute(WebElement element) throws Exception{
			boolean isDisabled = element.getAttribute("className").contains("--disabled");
			testResultService.assertEquals(true, isDisabled, "Element is not Disabled");
			return isDisabled;
		}
		
		public boolean checkElementIsNotDisabledByAttribute(WebElement element) throws Exception{
			boolean isDisabled = element.getAttribute("className").contains("--disabled");
			testResultService.assertEquals(false, isDisabled, "Element is Disabled");
			return !isDisabled;
		}
		
		public String getPopUpTextIfExists() throws Exception {

			String alertText = null;
			try {

				WebDriverWait wait = new WebDriverWait(webDriver, timeout, 1000);
				if (wait.until(ExpectedConditions.alertIsPresent()) != null) {
					alertText = webDriver.switchTo().alert().getText();
				}
			} catch (TimeoutException e) {
				// TODO Auto-generated catch block
				//testResultService.addFailTest("Alert was not found", false, true);
			}
			return alertText;

		}

		public void scrollToElementWith_JS_Executor(WebElement element) {
			JavascriptExecutor js = (JavascriptExecutor)webDriver;
			js.executeScript("arguments[0].scrollIntoView(true);", element);
		}
		
		public void switchToTab(int tabIndex) {
	        currentWindowHandles = new ArrayList<>(webDriver.getWindowHandles());
	        if (tabIndex >= 0 && tabIndex < currentWindowHandles.size()) {
	            webDriver.switchTo().window(currentWindowHandles.get(tabIndex));
	        } else {
	            throw new IllegalArgumentException("Invalid tab index");
	        }
	    }

	    public void closeTab(int tabIndex) {
	        switchToTab(tabIndex);
	        webDriver.close();
	    }
	    
	    
	    public void closeTabAndSwitchBack(int tabIndexToClose) {
	        String currentTabHandle = webDriver.getWindowHandle();
	        closeTab(tabIndexToClose);
	        
	        try {
	            webDriver.switchTo().window(currentTabHandle);
	        } catch (NoSuchWindowException e) {
	            throw new IllegalStateException("Current tab has been closed");
	        }
	    }

	    
}
