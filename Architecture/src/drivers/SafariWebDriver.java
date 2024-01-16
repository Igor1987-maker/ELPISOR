package drivers;

import io.appium.java_client.ios.IOSDriver;

import java.net.URL;

import org.openqa.selenium.Alert;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import services.DbService;

public class SafariWebDriver extends GenericWebDriver {

	@Override
	public void init(String remoteUrl, boolean useProxy) throws Exception {
		setBrowserName("safari");
		setInitialized(true);
		dbService = new DbService();
		reporter.report("Remote url from pom file is: " + remoteUrl);
		// logsFolder = folderName;
		try {
			if (remoteUrl == null) {
				// remoteUrl = configuration.getProperty("remote.machine");
			}
			// report.report("Initializing SafariWebDriver",
			// Reporter.EnumReportLevel.CurrentPlace);

			// DesiredCapabilities capabilities = new
			// DesiredCapabilities("firefox", "29.0.1", Platform.WINDOWS);

			DesiredCapabilities capabilities = new DesiredCapabilities();
			
//			capabilities.setCapability("appium-version", "1.4.1");
//			capabilities.setCapability("platformName", "iOS");
//			capabilities.setCapability("deviceName", "Tahir's iPhone");
//			capabilities.setCapability("udid", "your_udid");
////			capabilities.setCapability("bundleId", bundle);
//			capabilities.setCapability("browserName", "safari");
//			webDriver = new IOSDriver(new URL("http://10.1.0.51:4723/wd/hub"), capabilities); 

			// capabilities.setCapability(CapabilityType.HAS_NATIVE_EVENTS,
			// false);
			// capabilities.setCapability(
			// CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, "accept");
//			capabilities.setCapability("deviceName", "iPhone 6");
//	        capabilities.setCapability("platformName", "iOS");
//	        capabilities.setCapability("platformVersion", "8.1");
	      
	      
//	        capabilities.setCapability("browserName", "safari");
		
			
			//for ipad
//			webDriver = new RemoteWebDriver(new URL("http://10.1.0.51:4723/wd/hub"),
//					capabilities);
			
			webDriver=new RemoteWebDriver(new URL(remoteUrl+"/wd/hub"), capabilities);
			
		

			// webDriver = new RemoteWebDriver( capabilities);
			// setPageLoadTimeOut();

			setPageLoadTimeOut();
			setScriptLoadTimeOut();
			reporter.stopLevel();
		} catch (Exception e) {
			logger.error("Cannot register node or start the remote driver! ", e);
		}
	}

	@Override
	public void hoverOnElement(WebElement element) throws Exception {
		// TODO Auto-generated method stub
		String mouseOverScript = "if(document.createEvent){var evObj = document.createEvent('MouseEvents');evObj.initEvent('mouseover',true, false); arguments[0].dispatchEvent(evObj);} else if(document.createEventObject) { arguments[0].fireEvent('onmouseover');}";
		executeJsScript(mouseOverScript, element);
	}

	@Override
	public void scrollToElement(WebElement element) throws Exception {
		// TODO Auto-generated method stub
		executeJsScript("arguments[0].scrollIntoView(true);", element);
	}

	@Override
	public void scrollToElement(WebElement element, int yOffset)
			throws Exception {
		// TODO Auto-generated method stub
		executeJsScript("arguments[0].scrollIntoView(true);", element);
	}

	@Override
	public void maximize() {
		// TODO Auto-generated method stub
		// super.maximize();
	}

	@Override
	public String getPopUpText() throws Exception {
		Alert alert = null;
		try {
			WebDriverWait wait = new WebDriverWait(webDriver, getTimeout());
			wait.until(ExpectedConditions.alertIsPresent());

			alert = webDriver.switchTo().alert();

		} catch (UnhandledAlertException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (alert != null) {
			return alert.getText();
		} else
			return null;

	}

}
