package tests.edo.newux;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.apache.tools.ant.filters.TokenFilter.Trim;
import org.apache.tools.ant.taskdefs.Sleep;
import org.jboss.netty.handler.codec.http.websocket.WebSocketFrameDecoder;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
//import org.openqa.selenium.lift.match.DisplayedMatcher;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;

import drivers.ChromeWebDriver;
import drivers.FirefoxWebDriver;
import drivers.GenericWebDriver;
import junit.framework.Assert;
import Enums.BrowsersConfigs;
import Enums.ByTypes;
import Interfaces.TestCaseParams;
import pageObjects.edo.NewUXLoginPage;
import pageObjects.edo.NewUxDragAndDropSection2;
import pageObjects.edo.NewUxHomePage;
import pageObjects.edo.NewUxLearningArea;
import pageObjects.edo.NewUxLearningArea2;
import pageObjects.edo.NewUxMyProfile;
import pageObjects.edo.NewUxSantillanaEntrance;
import pageObjects.tms.DashboardPage;
import pageObjects.tms.TmsHomePage;
import pagesObjects.oms.PageUtils;
import services.PageHelperService;
import testCategories.AngularLearningArea;
import testCategories.NonAngularLearningArea;
import testCategories.inProgressTests;
import testCategories.reg3;
import testCategories.unstableTests;
import testCategories.edoNewUX.LoginPage;
@Category(reg3.class)
public class NewUxLoginTestsClientSide extends BasicNewUxTest {

	NewUXLoginPage loginPage;
	NewUxLearningArea learningArea;
	NewUxLearningArea2 learningArea2;
	TmsHomePage tmsHomePage;
	FirefoxDriver wd;
	
	private static final String UserIsLoggedIn = "This username is currently logged into the system. If you feel you received this message in error, please contact your system administrator.";
	private static final String waitForLoginMessage = "Please wait while you are being logged into the system.\nThis may take a few minutes.";
	private static final String improperLoginAlert = "Your last session was closed improperly and some information might have been lost. Please close future learning sessions by using the Logout button.";
	private static final String userLoggedInMessage = "This user name is currently logged into the system.\nIf you feel you have received this message by mistake, please contact your system administrator.";
	
	private final String userNameLessThen5Chars = "Your name is required to be at least 5 characters";
	private final String userNameMoreThen15Chars = "Your name cannot be longer than 15 characters";
	private final String passCharsAndNumbersOnly = "The password only accepts English characters or numbers.";
	private final String missingUserName = "Please enter your user name.";
	private static final String ErrorUserName = "siteLogin__messageText";
	

	private final String usernameCharsAndNumbersOnly = "The user name only accepts English characters or numbers.";
	private final String passLessThen5chars = "Your password must have at least 5 characters.";
	private final String passMoreThen15chars = "";
	private final String extLoginIncorrectUserName = "Your user name or password is incorrect. Please try again.";
	private final String extLoginIncorrectUserNamePortuguese = "Lo Username o la Password non sono corretti. Prova di nuovo.";
	public static  final String retuenUrl = "https://ed.engdis.com/ed2016#/login";
	

	@Before
	public void setup() throws Exception {
		super.setup();
	}

	
	protected NewUxHomePage createUserAndLoginForOptIn(String className,
			String institutionid) throws Exception {
		
		studentId = pageHelper.createUSerUsingOptIn(institutionid, className);
		
		//dbService.setUserOptIn(studentId, true);
		
		NewUXLoginPage loginPage = new NewUXLoginPage(webDriver, testResultService);
		
		homePage = loginPage.loginAsStudent(
				dbService.getUserNameById(studentId, institutionid), "12345");

//		dbService.setUserOptIn(studentId, true);
		
		return homePage;
	}
	
//Test for OPTIN
	
	@Test
	@Category(AngularLearningArea.class)
	@TestCaseParams(testCaseID = { "50334", "50345", "50335", "50343" },testMultiple=true)
	public void testLoginWithOptIn() throws Exception {
		
	report.startStep("Initial phase");
		loginPage = new NewUXLoginPage(webDriver, testResultService);
		learningArea2 = new NewUxLearningArea2(webDriver, testResultService);
		
	report.startStep("Create user and refresh on OptIn");
		createUserAndLoginForOptIn();
		webDriver.waitForElementAndClick("optInPrivacyStatement__checkBoxW", ByTypes.className);
		webDriver.waitForElementAndClick("optInPrivacyStatement__messageLink--PrivacyStatement", ByTypes.id);
		webDriver.refresh();
		sleep(4);
		
	report.startStep("Check that user is redirected to the login page");
		String currentUrl = webDriver.getUrl();
		testResultService.assertTrue("User was not redirected to the login page",currentUrl.contains("optIn"));
		
	report.startStep("Functional of the Cancel button on OptIn");	
	     //loginAsStudent(studentId);
	     webDriver.waitForElementAndClick("optInPrivacyStatement__checkBoxW", ByTypes.className);
		 webDriver.waitForElementAndClick("optInPrivacyStatement__cancel", ByTypes.id);
		 		 
	report.startStep("Check that user is redirected to the login page");
		 webDriver.waitUntilElementAppearsAndReturn("siteLogin__loginBoxInnerW", ByTypes.className, 10);
		 currentUrl = webDriver.getUrl();
		 testResultService.assertTrue("User was not redirected to the login page",currentUrl.contains("login"));
		
	report.startStep("Try to login again via OptIn");
		loginAsStudent(studentId);
		webDriver.waitUntilElementAppears("optInPrivacyStatement__continue", ByTypes.id, 10);
		webDriver.waitForElementAndClick("optInPrivacyStatement__checkBoxW", ByTypes.className);
		webDriver.waitForElementAndClick("optInPrivacyStatement__continue", ByTypes.id);
		homePage.closeAllNotifications();
		pageHelper.skipOnBoardingHP();
		homePage.waitHomePageloadedFully();
		homePage.navigateToCourse(2);
		
	report.startStep("Click on logout and login again without OptIn");
		//learningArea2.clickOnLogoutLearningArea();
		homePage.logOutOfED();
		loginAsStudent(studentId);
		sleep(1);
		homePage.closeAllNotifications();
		homePage.waitHomePageloadedFully();
		homePage.navigateToCourse(2);
	}
	
	
	
	@Test
	@Category(LoginPage.class)
	@TestCaseParams(testCaseID = {"18281","20390","19927","24412" },testMultiple=true)
	public void loginValidationFields() throws Exception {

		report.startStep("Initial phase");
		loginPage = new NewUXLoginPage(webDriver, testResultService);

		testResultService.assertEquals(false,
				loginPage.isSubmitButtonEnabled(),
				"Submit button was enabled 1");

		report.startStep("Test Blank user name");

		loginPage.enterPassowrd("12345");
		loginPage.clickOnUserNameTextBpx();
		loginPage.clickOnPassTextBox();
		validateErrorMessage(missingUserName, "Missing user name",
				"error.required");

		report.startStep("Special char in user name");
		loginPage.enterUserName("aaaaaaaa#");
		validateErrorMessage(usernameCharsAndNumbersOnly,
				"Special char in user name", "error.pattern");
		testResultService.assertEquals(false,
				loginPage.isSubmitButtonEnabled(),
				"Submit button was enabled 2");

		report.startStep("No english chars");
		loginPage.enterUserName("aaaa�zzz");
		validateErrorMessage(usernameCharsAndNumbersOnly, "No english chars",
				"error.pattern");
		testResultService.assertEquals(false,
				loginPage.isSubmitButtonEnabled(),
				"Submit button was enabled 3");

		report.startStep("Password with special chars");
		loginPage.enterPassowrd("123$4444");
		validateErrorMessage(passCharsAndNumbersOnly,
				"Password with special chars",
				"loginForm.password.$error.pattern");
		testResultService.assertEquals(false,
				loginPage.isSubmitButtonEnabled(),
				"Submit button was enabled 4");

		report.startStep("Login as Valid user name and password");
		getUserAndLoginNewUXClass();
	//	homePage.closeAllNotifications();
		
		testResultService.assertEquals(true, loginPage.isLogOutDisplayed(),
				"Log out is not displayed");

	}

	@Test
	@Category(LoginPage.class)
	@TestCaseParams(testCaseID = { "18237" },testMultiple=true)
	public void testSchoolAdminValidUserNameAndPassword() throws Exception {
		
		report.startStep("Open New UX login page");
		loginPage = new NewUXLoginPage(webDriver, testResultService);

		pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(
				configuration.getProperty("schoolAdmin.user"),institutionId));
		
		report.startStep("Enter Admin's user name and password");
		tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
		tmsHomePage.waitForPageToLoad();

		report.startStep("Check that the dashbaord is displayed");

		DashboardPage dashboardPage = new DashboardPage(webDriver,
				testResultService);
		dashboardPage.switchToMainFrame();
		
		testResultService.assertEquals(true,
				dashboardPage.getDashboardNavBarDisplayStatus(),
				"Dashboard is not displayed");

	}

	@Test
	@Category(LoginPage.class)
	@TestCaseParams(testCaseID = { "18236" },testMultiple=true)
	public void testTeacherValidUserNameAndPassword() throws Exception {
		
		report.report("Open New UX login page");
		loginPage = new NewUXLoginPage(webDriver, testResultService);
		
		pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(
				configuration.getProperty("teacher.username"),institutionId));

		report.startStep("Enter Teacher's user name and password");
		loginPage.enterTeacherUserAndPassword();

		report.startStep("Check that the dashbaord is displayed");
		homePage.skipNotificationWindow();
		
		DashboardPage dashboardPage = new DashboardPage(webDriver,testResultService);
		
		dashboardPage.switchToMainFrame();
		
		testResultService.assertEquals(true,dashboardPage.getDashboardNavBarDisplayStatus(),
				"Dashboard is not displayed");
	}

	@Test
	@TestCaseParams(testCaseID = { "18290" })
	public void testImprperLogout() throws Exception {
		
		loginPage = new NewUXLoginPage(webDriver,testResultService);
		learningArea = new NewUxLearningArea(webDriver, testResultService);
		
		report.startStep("Get user new user and login");
		getUserAndLoginNewUXClass();
		String userName = dbService.getUserFirstNameByUserId(studentId);
	//	homePage.waitHomePageloaded();
		
		report.startStep("Verify user details on Home Page");
		testResultService.assertEquals("Hello " + userName, homePage.getUserDataText(), "First Name in Header of Home Page not valid");
				
		report.startStep("Close browser and open again");
		webDriver.quitBrowser();
		sleep(2);
		webDriver.init();
		sleep(2);
		webDriver.maximize();
		//sleep(2);
		
		report.startStep("Login again with the same user");
		webDriver.openUrl(pageHelper.CILink);
		homePage = loginPage.loginAsStudent(userName, "12345");

		report.startStep("Verifiy that ED logo is displayed in Footer and banner in Header");
		testResultService.assertEquals(true, loginPage.getfFooterImageDisplayed(), "Footer image is not displayed");	
		loginPage.verifyDefaultBannerExist();
		
		report.startStep("Check and confirm alert of multi-login");
		loginPage.verifyAndConfirmAlertMessage();
		sleep(1);
		homePage.closeAllNotifications();
		//loginPage.verifyWaitForLoginMessage(waitForLoginMessage);
		//loginPage.waitForLoginProgressBarEnds(120);
		
		//report.startStep("Check improper login alert on Home Page");
		//learningArea.validateAlertModalByMessage(improperLoginAlert, true);
		//sleep(1);
		
		report.startStep("Verify user details on Home Page");
		testResultService.assertEquals("Hello " + userName, homePage.getUserDataText(), "First Name in Header of Home Page not valid");
		
		report.startStep("Navigate to next Course on Home Page");
		homePage.carouselNavigateNext();
		
	}
	
	@Test
	@TestCaseParams(testCaseID = { "88986" })
	public void testMultiLogin() throws Exception {
	
		try {
		report.startStep("Create user and login to Chrome BROWSER No.1");
		pageHelper.closeBrowserAndOpenInCognito(true);
		webDriver.openUrl(pageHelper.linkED);
		homePage = createUserAndLoginNewUXClass();
		homePage.waitNotificationsAfterRestartPool();
		homePage.closeAllNotifications();
		homePage.waitHomePageloaded();
		String userName = dbService.getUserNameById(studentId, institutionId);
		
		report.startStep("Login to Firefox with same user BROWSER No.2");
		firefoxDriver = (FirefoxWebDriver) ctx.getBean("FirefoxWebDriver");
		firefoxDriver.init();
		firefoxDriver.maximize();
		firefoxDriver.openUrl(pageHelper.linkED);
		
		NewUXLoginPage loginPage2 = new NewUXLoginPage(firefoxDriver, testResultService);
		NewUxHomePage homePage2 = loginPage2.loginAsStudent(userName, "12345");
		sleep(3);
		
		report.startStep("Verify and confirm alert of Force login");
		loginPage2.verifyAndConfirmAlertMessage();
		homePage2.closeAllNotifications();
		homePage2.waitHomePageloaded();
					
		report.startStep("Verify user logged out from firs browser");
		NewUXLoginPage loginPage = new NewUXLoginPage(webDriver, testResultService);
		loginPage.verifyUserGetSessionTimeOut();
	
		homePage2.logOutOfED();
	
		}catch(Exception e) {
			
		}finally {
			dbService.deleteStudentByName(institutionId, userName);
			firefoxDriver.quitBrowser();
			firefoxDriver.getWebDriver().quit();
			firefoxDriver.tearDown();
		}
	}
	
	
	@Test
	@TestCaseParams(testCaseID = { "88986" })
	public void testMultiLoginWithFirefox() throws Exception {
		
	report.startStep("Create user and login to Chrome BROWSER No.1");
		homePage = createUserAndLoginNewUXClass();
		homePage.waitNotificationsAfterRestartPool();
		homePage.closeAllNotifications();
		String userName = dbService.getUserNameById(studentId, institutionId);
		
	 try {
		report.startStep("Create new instance of webDriver and login with same user into Firefox BROWSER No.2");
			initFirefoxBrowser();
			loginToED(userName);
			
		report.startStep("Accept alert to stay loggedIn with second browser");  
			verifyAndConfirmAlertMessage();
			verifyUserLoggedInNewBrowser(userName);
		
		report.startStep("Verify inactive session at first browser");  	
			verifyUserLoggedOutFromPrevoiusBrowser();
		
		report.startStep("LogOut from second browser");  		
			logOutFromEdWithSecondBrowser();
		}catch (Exception e) {
				e.printStackTrace();
				
			}finally{
				//wd.close();
				wd.quit();
				}
		
	/*	String mainWindow = webDriver.getWebDriver().getWindowHandle();
		//element.sendKeys(Keys.CONTROL+"n");
		webDriver.sendKey(Keys.chord(Keys.CONTROL,"n"));
		pageHelper.runJavaScriptCommand("window.open('"+pageHelper.linkED+"');");
		webDriver.switchToNewWindow();
		webDriver.getWebDriver().manage().deleteAllCookies();
		sleep(7);
	*/	
		
		
		
	/*	GenericWebDriver webDriver2 = (ChromeWebDriver) ctx.getBean("ChromeWebDriver");
		//webDriver.openIncognitoChromeWindow();
		webDriver2.init();
		webDriver2.maximize();
		webDriver2.openUrl(pageHelper.linkED);
		homePage.clickOnMyProfile();
		*/
	
	
		
		
	//	LinkedHashSet<String> windows = (LinkedHashSet<String>) webDriver.getWebDriver().getWindowHandles();
	//	ArrayList<String> arl = new ArrayList<>(windows);
	//	webDriver.getWebDriver().switchTo().window(arl.get(0));

	}
	
	
	
	private void logOutFromEdWithSecondBrowser() throws Exception {
		
		WebElement element = null;
		wait(By.xpath("//li[contains(@class,'settingsMenu__personal')]")).click();
		wait(By.xpath("//li[contains(@class,'settingsMenu__listItem_logout')]")).click();
		wait(By.xpath("//div[@id='EdoFrameBoxContent']/iframe"));
		wd.switchTo().frame(wd.findElementByXPath("//div[@id='EdoFrameBoxContent']/iframe"));
		sleep(1);
		wd.findElementByName("btnOk").click();
		sleep(1);
		
		
		//new WebDriverWait(wd, 5).until(ExpectedConditions.visibilityOfElementLocated(
    			//By.xpath("//li[contains(@class,'settingsMenu__personal')]")));
		//wd.findElementByXPath("//li[contains(@class,'settingsMenu__personal')]").click();
		//new WebDriverWait(wd, 5).until(ExpectedConditions.visibilityOfElementLocated(
    		//	By.xpath("//li[contains(@class,'settingsMenu__listItem_logout')]")));
		//wd.findElementByXPath("//li[contains(@class,'settingsMenu__listItem_logout')]").click();
		//new WebDriverWait(wd, 8).until(ExpectedConditions.visibilityOfElementLocated(
    		//	By.xpath("//div[@id='EdoFrameBoxContent']/iframe")));
	}


	private void verifyUserLoggedOutFromPrevoiusBrowser() throws Exception {
		sleep(3);
		String url = webDriver.getUrl();
		for(int i=0;i<30&&!url.contains("SessionTimeout");i++) {
			Thread.sleep(2000);
			url = webDriver.getUrl();
		}
    	textService.assertEquals("User doesn't logedOut from first browser",url.contains("SessionTimeout"), true);
    	loginPage = new NewUXLoginPage(webDriver, testResultService);
    	loginPage.clickOK_onSessionIsNoLongerActive();
    	WebElement submit = webDriver.waitForElement("submit1", ByTypes.id, true, 10);
		textService.assertEquals("Logout incorrect", true, submit.isDisplayed());
	}


	private void verifyUserLoggedInNewBrowser(String userName) {
		WebElement userNameOnHomePage = wait(By.xpath("//span[contains(@class,'home__userName')]"));
	   	String name = userNameOnHomePage.getText().trim();
    	testResultService.assertEquals("Hello " + userName, name, "First Name in Header of Home Page not valid");
		
	}


	public void verifyAndConfirmAlertMessage()  {
		
		try {
			WebElement alertText = wd.findElementByXPath("//div[contains(@class,'TinyScrollbarW')]");
			String text = alertText.getText().trim();
			testResultService.assertEquals(text.contains("You are already logged in to English Discoveries"),true,"Wrong message or alert not apeared!");
			wd.findElement(By.id("btnOk")).click();
			sleep(2);
			WebElement notifications = wait(By.className("notificationsCenter_hideSlide"));
			while(notifications!=null) {
				notifications.click();
				notifications = wait(By.className("notificationsCenter_hideSlide"));
				}
		}catch(Exception e) {
			}
	}


	private void loginToED(String userName) throws Exception {
		WebElement name = wait(By.xpath("//input[@name='userName']"));
		PageUtils.type(userName, name);
		WebElement password = wait(By.xpath("//input[@name='password']"));
		PageUtils.type("12345", password);
		wd.findElement(By.id("submit1")).click();
		sleep(3);
	}
	
	private WebElement wait(By locator) {
		WebElement element = null;
		new WebDriverWait(wd, 10).until(ExpectedConditions.visibilityOfElementLocated(locator));
			element = wd.findElement(locator);
		return element;
	}


	private void initFirefoxBrowser() {
		setPathToFirefoxDriver(configuration.getPathToFirefoxDriver());
	    FirefoxOptions options = new FirefoxOptions();
	   // options.setBinary("C:\\Program Files\\Mozilla Firefox\\firefox.exe");
	    wd = new FirefoxDriver(options);
	    wd.manage().window().maximize();
	    wd.get(pageHelper.linkED);
		
	}


	@Category (inProgressTests.class)
	@Test
	@TestCaseParams(testCaseID = { "18280" }, skippedBrowsers={"IE10","IE11","firefox","chromAndroid","safariMac"})
	public void testNoAbilityFor2ParallelSessions() throws Exception {

		report.startStep("Get user new user and login via BROWSER No.1");
		getUserAndLoginNewUXClass();
		String userName = dbService.getUserFirstNameByUserId(studentId);
	//	sleep(2);
		
		report.startStep("Verify user details on Home Page");
		testResultService.assertEquals("Hello " + userName, homePage.getUserDataText(), "First Name in Header of Home Page not valid");
	//	sleep(3);
		
		//FirefoxWebDriver webDriver2 = null;
		GenericWebDriver webDriver2 = null;	
		try {
		
		report.startStep("Try to login with the same user via BROWSER No.2");
		
		
	
		/*
	    System.setProperty("webdriver.gecko.driver", "C:\\SeleniumServer\\geckodriver.exe");
        FirefoxOptions options = new FirefoxOptions();
        options.setBinary("C:\\Program Files\\Mozilla Firefox\\firefox.exe");
        WebDriver driver3 =  new FirefoxDriver(options);
        driver3.get("https://stackoverflow.com");
        */
		
        webDriver2 = (FirefoxWebDriver) ctx.getBean("FirefoxWebDriver");
       // webDriver2.setBrowserName("firefox");
		//System.setProperty("browserName", "firefox");
		//getTfsConfigId("firefox");
		//setInitialized(true);
		
		
		webDriver2.init();
		webDriver2.openUrl(pageHelper.CILink);
		loginPage = new NewUXLoginPage(webDriver2, testResultService);
		loginPage.loginAsStudent(userName, "12345");
		
		report.startStep("Verifiy that ED logo is displayed in Footer and banner in Header");
		testResultService.assertEquals(true, loginPage.getfFooterImageDisplayed(), "Footer image is not displayed");	
		loginPage.verifyDefaultBannerExist();
		
		report.startStep("Check Wait for Login message & progress bar");
		loginPage.verifyWaitForLoginMessage(waitForLoginMessage);
		loginPage.waitForLoginProgressBarEnds(125);
		
		report.startStep("Check User already logged in message & click OK");
		sleep(1);
		loginPage.verifyUserLoggedInMessage(userLoggedInMessage, true);
		// check login status here
		
		report.startStep("Logout in BROWSER No.1");
		homePage.clickOnLogOut();
		webDriver.switchToTopMostFrame();
		loginPage = new NewUXLoginPage(webDriver, testResultService);
		testResultService.assertEquals(false, loginPage.isSubmitButtonEnabled(), "Checking that ED Login Page displayed");
		
		report.startStep("Login in BROWSER No.2 and check success");
		loginPage = new NewUXLoginPage(webDriver2, testResultService);
		homePage = loginPage.loginAsStudent(userName, "12345");
		testResultService.assertEquals("Hello " + userName, homePage.getUserDataText(), "First Name in Header of Home Page not valid");
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			
			webDriver.quitBrowser();
			sleep(2);
			webDriver2.quitBrowser();
		}	
	}
	

	@Test
	@Category(LoginPage.class)
	@TestCaseParams(testCaseID = { "20385" },testMultiple=true, skippedBrowsers={"safariMac"})
	public void testDeletedStudentCannotLogin() throws Exception {

		String userName = dbService.sig(6);
		String pass = "12345";
		report.startStep("Create new user and login");
		pageHelper.createUSerUsingSP(configuration.getInstitutionId(),
				userName, userName, userName, pass,
				userName + "@edusoft.co.il",
				configuration.getProperty("classname"));
		String userId = dbService.getUserIdByUserName(userName, configuration.getInstitutionId());
		pageHelper.setOnBoardingToVisited(userId);
		loginPage = new NewUXLoginPage(webDriver, testResultService);
		NewUxHomePage homePage = loginPage.loginAsStudent(userName, pass);
		//webDriver.waitForElementAndClick("optInPrivacyStatement__checkBoxW", ByTypes.className);
		//webDriver.waitForElementAndClick("optInPrivacyStatement__continue", ByTypes.id);
		pageHelper.skipOptin();
		homePage.closeAllNotifications();
		homePage.clickOnLogOut();

		webDriver.switchToMainWindow();
		
		
		report.startStep("Delete the user and try to login");
		studentService.deleteStudent(
				dbService.getUserIdByUserName(userName,
						configuration.getInstitutionId()),
				configuration.getInstitutionId());
		loginPage.loginAsStudent(userName, pass);
	//	homePage.closeAllNotifications();
		String UserOrPassNotCorrect = "Your user name or password is incorrect. Please try again.";
		String textFromPopUp = loginPage.getPopUpMessageText();
		testResultService.assertEquals(true, textFromPopUp.contains(UserOrPassNotCorrect),
				"Message about bad login was not displayed");
		report.report("Create the same user again and try to login");

	}
		
	
	@Test
	@Category(LoginPage.class)
	@TestCaseParams(testCaseID = { "50505" } )
	public void testExtLoginTeacherOptIn() throws Exception {

		report.startStep("Open browser");
			pageHelper.closeBrowserAndOpenInCognito(false);
			
		report.startStep("Inint test data");
			String teacherUserName = "optInTeacher";
			//String instID = configuration.getInstitutionId(); 
			String teacherId = dbService.getOptinTeacher(institutionId);
			pageHelper.setUserLoginToNull(teacherId);
			dbService.setUserOptIn(teacherId, false);
			String pass = "12345";
			String refUrl = retuenUrl; 
			String baseUrl = pageHelper.CILink.split(".com")[0] + ".com/";
					
		report.startStep("Verify Optin display for ExtLogin Teachers and Cancel it");
			String loginUrl = baseUrl+"extlogin.aspx?IID="+institutionId+"&UserName="+teacherUserName+"&Password="+pass+"&Link="+refUrl; 
			webDriver.openUrl(loginUrl);
			sleep(2);
			webDriver.waitForElementAndClick("optInPrivacyStatement__cancel", ByTypes.id);
		
		report.startStep("Verify redirect to referrer URL from parameters");
			String currentUrl = "";
			currentUrl = webDriver.waitForSpecificCurrentUrl(currentUrl, "engdis");
			testResultService.assertEquals(retuenUrl, currentUrl,"Referrer site not displayed");
			
		report.startStep("Continue for optIn in Extlogin as teacher");
			webDriver.openUrl(loginUrl);
			sleep(1);
			webDriver.waitForElementAndClick("optInPrivacyStatement__checkBoxW", ByTypes.className);
			webDriver.waitForElementAndClick("optInPrivacyStatement__continue", ByTypes.id);
			sleep(3);
			tmsHomePage = new TmsHomePage(webDriver, testResultService);
			
		report.startStep("Logout from tms to referrer URL");
			tmsHomePage.switchToMainFrame();
			tmsHomePage.clickOnExit();
			currentUrl = "";
			currentUrl = webDriver.waitForSpecificCurrentUrl(currentUrl, "engdis");
			testResultService.assertEquals(retuenUrl, currentUrl,"Referrer site not displayed");
			
		report.startStep("Login as teacher without optIn prompt");
			webDriver.openUrl(loginUrl);
			webDriver.checkElementNotExist("//*[@id='optInPrivacyStatement__cancel']");
	}
	
		
	private void waitTillSantilianaPageWillLoaded() throws Exception {
		// TODO Auto-generated method stub
		WebElement element = null;
		for (int i=0;i<=10 && element==null;i++){
			try {
				element = webDriver.waitForElement("content", ByTypes.className, false, 1);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
	
	private void verifySantilianaPageNotLoaded() throws Exception {
		// TODO Auto-generated method stub
		WebElement element = null;
		for (int i=0;i<=10 && element==null;i++){
			try {
				element = webDriver.waitForElement("content", ByTypes.className, false, 1);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				}
				
			}
			if (element!=null) {
				testResultService.addFailTest("Banner element has loaded while it shouldn't", true, true);
		}
	}

	


	@Test
	@TestCaseParams(testCaseID = {"42013"})
	@Category(NonAngularLearningArea.class)
	public void testExtLinkLandingLA() throws Exception{
		
		OpenBrowser();
		
		report.startStep("Browse to External Link of Learning Area");
			String baseUrl = pageHelper.CILink.split(".com")[0] + ".com";
			webDriver.openUrl(baseUrl + "/Runtime/template.html?qs=eyJpZCI6IjMwMDAwMjUwMDAzMjEiLCJjb3Vyc2VJZCI6IjQ1MTc4IiwidW5pdElkIjoiNDUyMzEiLCJjb21wb25lbnRJZCI6IjQ1MjM1Iiwic2tpbGwiOiJTcGVha2luZyIsImNvbXBvbmVudE5hbWUiOiJIb21lIFJlY3ljbGluZyBTdXJ2ZXkgIiwiY29tcG9uZW50VHlwZSI6IjEiLCJJSUQiOiIzMDAwMDI1IiwiU0lEIjoiMzAwMDAyNTAwMDMyMSJ9&t=1497436891818");
	
		report.startStep("Verify it opened and Unit header");
			NewUxLearningArea learningArea = new NewUxLearningArea(webDriver,testResultService);
			testResultService.assertEquals("Unit 9: Recycling",learningArea.getHeaderTitle(),
				"Header title not found or do not match");
		
		report.startStep("Verify Lesson Name in header");
			String expectedLessonName = dbService.getLessonNameByCourseUnitAndLessonNumber(getCourseIdByCourseCode("I2"), 8, 4);
			expectedLessonName = expectedLessonName.trim();
			String lessonName = learningArea.getLessonName();
			testResultService.assertEquals(expectedLessonName, lessonName, "Lesson name is not the same");
			
		report.startStep("Verify task Instruction");
			learningArea.VerificationOfQuestionInstruction("Choose the correct answer.");
		
		report.startStep("Make some action in the Task");
			learningArea.selectAnswerRadioByTextNum(1, 2);
			learningArea.clickOnCheckAnswer();
			learningArea.clickOnClearAnswer();
			learningArea.clickOnSeeAnswer();
			learningArea.clickOnNextButton();
			
		
		report.startStep("Click on Next button and make some action in the Task");
			learningArea.selectAnswerRadioByTextNum(1, 1);
			learningArea.clickOnCheckAnswer();
			learningArea.clickOnClearAnswer();
			learningArea.clickOnSeeAnswer();
		
		report.startStep("Select another leasson and verify its content header");
			learningArea.openLessonsList();
			learningArea.clickOnLessoneByIndex(2);
			webDriver.waitForElement("//*[@id='textContainer']/div/div/div[1]/h2/span", ByTypes.xpath, "The content header not display");
			
	}
	
	@Test
	@TestCaseParams(testCaseID = {"42013","42029"})
	@Category(AngularLearningArea.class)
	public void testExtLinkLandingAngularLA() throws Exception{
		
		report.startStep("Init test data");
			String correctAnswer = "question-1_answer-1";
			String expectedLessonName = dbService.getLessonNameByCourseUnitAndLessonNumber(getCourseIdByCourseCode("I2"), 8, 4).trim();
			
		report.startStep("Browse to External Link of Learning Area");
			String baseUrl = pageHelper.CILink.split(".com")[0] + ".com";
			webDriver.openUrl(baseUrl + "/Runtime/template.html?qs=eyJpZCI6IjMwMDAwMjUwMDAzMjEiLCJjb3Vyc2VJZCI6IjQ1MTc4IiwidW5pdElkIjoiNDUyMzEiLCJjb21wb25lbnRJZCI6IjQ1MjM1Iiwic2tpbGwiOiJTcGVha2luZyIsImNvbXBvbmVudE5hbWUiOiJIb21lIFJlY3ljbGluZyBTdXJ2ZXkgIiwiY29tcG9uZW50VHlwZSI6IjEiLCJJSUQiOiIzMDAwMDI1IiwiU0lEIjoiMzAwMDAyNTAwMDMyMSJ9&t=1497436891818");			
	
		report.startStep("Verify External link Unit, Lesson and Step name");
			learningArea2 = new NewUxLearningArea2(webDriver,testResultService);
			learningArea2.waitForPageToLoad();
			learningArea2.checkUnitLessonStepNameOnLanding("Unit 9: Recycling",expectedLessonName,"Getting ready to watch a video");
		
		for (int i =0 ; i < 2 ; i++)
		{
			report.startStep("Verify task Instruction");
				learningArea2.clickOnNextButton();
				sleep(3);
				learningArea2.VerificationOfQuestionInstruction("Choose the correct answer.");
			
			report.startStep("Make some action in the Task");
				learningArea2.SelectRadioBtn(correctAnswer);
				learningArea2.clickOnCheckAnswer();
				learningArea2.clickOnClearAnswer();
				learningArea2.clickOnSeeAnswer();
				learningArea2.clickOnNextButton();
		}	
		
		report.startStep("Select another leasson and verify its content header");
			learningArea2.openLessonsList();
			learningArea2.clickOnLessonByNumber(2);
			expectedLessonName = dbService.getLessonNameByCourseUnitAndLessonNumber(getCourseIdByCourseCode("I2"), 8, 2);
			learningArea2.checkUnitLessonStepNameOnLanding("Unit 9: Recycling",expectedLessonName,"Focusing on comprehension");
	}
	
	
	@Test
	@Category(LoginPage.class)
	@TestCaseParams(testCaseID = { "11111" },testMultiple=true, skippedBrowsers={"safariMac"})
	public void testLoginAsNotExistsUser() throws Exception {

		String userName = dbService.sig(6);
		String pass = "12345";
		
		report.startStep("Try login is as not exists user");
		
		loginPage = new NewUXLoginPage(webDriver, testResultService);
		loginPage.loginAsStudent(userName, pass);
		
		String UserOrPassNotCorrect = "Your user name or password is incorrect. Please try again.";
		testResultService.assertEquals(true, loginPage.getPopUpMessageText()
				.contains(UserOrPassNotCorrect),
				"Message about bad login was not displayed");
	}
	
		@Test
	@Category(AngularLearningArea.class)
	@TestCaseParams(testCaseID = { "87244" },testMultiple=true)
	public void testCheckClientTypeAndOperatingSystem() throws Exception {
		
	report.startStep("Initial phase");
		loginPage = new NewUXLoginPage(webDriver, testResultService);
		learningArea2 = new NewUxLearningArea2(webDriver, testResultService);
		
	report.startStep("Create user and refresh on OptIn");
		createUserAndLoginForOptIn();
		webDriver.waitForElementAndClick("optInPrivacyStatement__checkBoxW", ByTypes.className);
		webDriver.waitForElementAndClick("optInPrivacyStatement__messageLink--PrivacyStatement", ByTypes.id);
		homePage.closeAllNotifications();
		webDriver.refresh();
		sleep(4);
		
	report.startStep("Check that user is redirected to the login page");
		String currentUrl = webDriver.getUrl();
		testResultService.assertTrue("User was not redirected to the login page",currentUrl.contains("optIn"));
		
	report.startStep("Functional of the Cancel button on OptIn");	
	     //loginAsStudent(studentId);
	     webDriver.waitForElementAndClick("optInPrivacyStatement__checkBoxW", ByTypes.className);
		 webDriver.waitForElementAndClick("optInPrivacyStatement__cancel", ByTypes.id);
		 sleep(4);
		 homePage.closeAllNotifications();
		 pageHelper.skipOnBoardingHP();
	report.startStep("Check that user is redirected to the login page");
		 webDriver.waitUntilElementAppearsAndReturn("siteLogin__loginBoxInnerW", ByTypes.className, 10);
		 currentUrl = webDriver.getUrl();
		 testResultService.assertTrue("User was not redirected to the login page",currentUrl.contains("login"));
		
	report.startStep("Try to login again via OptIn");
		loginAsStudent(studentId);
		webDriver.waitUntilElementAppears("optInPrivacyStatement__continue", ByTypes.id, 10);
		webDriver.waitForElementAndClick("optInPrivacyStatement__checkBoxW", ByTypes.className);
		webDriver.waitForElementAndClick("optInPrivacyStatement__continue", ByTypes.id);
		homePage.closeAllNotifications();
		pageHelper.skipOnBoardingHP();
		homePage.waitHomePageloadedFully();
		homePage.navigateToCourse(2);
		
		report.startStep("Check Client type and Operating system");
		testResultService.assertEquals(true, dbService.verifyClientTypeAndOperatingSystemOfProgress(studentId));
		
		homePage.logOutOfED();
		
	}
	
	private void closeBrowserAndOpenAgain() throws Exception {
				
		webDriver.quitBrowser();
		sleep(2);
		webDriver.init();
		sleep(2);
		webDriver.maximize();
		sleep(2);
				
	}
	
	private void verifyExtLoginParametersAfterLoginToefl(String studentUserName, String firstName, String lastName, boolean executeLogout) throws Exception {
		
		webDriver.waitForElement("My Profile", ByTypes.linkText).click();
		sleep(2);
		webDriver.switchToFrame(0);
		String actualUserName = webDriver.waitForElement("UserName",ByTypes.id).getAttribute("value");
		String actualFirstName = webDriver.waitForElement("FirstName",ByTypes.id).getAttribute("value");
		String actualLastName = webDriver.waitForElement("LastName",ByTypes.id).getAttribute("value");
		testResultService.assertEquals(studentUserName, actualUserName,"User Name is wrong");
		testResultService.assertEquals(firstName, actualFirstName,"First Name is wrong");
		testResultService.assertEquals(lastName, actualLastName,"Last Name is wrong");
		sleep(2);
		webDriver.switchToMainWindow();
		webDriver.waitForElement("cboxClose", ByTypes.id).click();
		sleep(2);
		
		if(executeLogout) {
		
		report.startStep("Logout");
		webDriver.waitForElement("Logout", ByTypes.linkText).click();
		webDriver.closeAlertByAccept(webDriver.getTimeout());
		}		
	}
	
	private void verifyUserDetailsInToeflTMS(String firstName, String lastName, boolean executeLogout) throws Exception {
		
		String expectedUserDetails = "Welcome, "+ firstName + " " + lastName.trim();
		String actualUserDetails = webDriver.waitForElement("cxtWellcome", ByTypes.id).getText();
		testResultService.assertEquals(expectedUserDetails, actualUserDetails, "User Details are not correct");
		
		if(executeLogout) {
		
		report.startStep("Logout");
		webDriver.waitForElement("Logout", ByTypes.linkText).click();
		webDriver.closeAlertByAccept(webDriver.getTimeout());
		}		
	}
	
	private void verifyExtLoginParametersAfterLogin(String studentUserName, String firstName, String lastName, boolean redirectOnLogout) throws Exception {
		
		report.startStep("Verify First Name is correct on Home Page");
		NewUxHomePage homePage = new NewUxHomePage(webDriver,testResultService);
		
		testResultService.assertEquals("Hello " + firstName, homePage.getUserDataText(), "Verify First Name in Header of Home Page");
		
		/*
		report.startStep("Verify no progress in widgets");
		String completionNoProgress = homePage.getCompletionWidgetValue();
		String timeNoProgress = homePage.getTimeOnTaskWidgetValue();
		testResultService.assertEquals("0", completionNoProgress,"Check no progress in Completion widget");
		testResultService.assertEquals("00:00", timeNoProgress, "Check no progress in Time On Task widget");
		*/
		
		report.startStep("Open My profile");
		homePage.clickOnMyProfile();
		homePage.switchToMyProfile();
		sleep(3);
		
		report.startStep("Check user parameters");
		NewUxMyProfile myProfile = new NewUxMyProfile(webDriver,testResultService);
				
		testResultService.assertEquals(studentUserName, myProfile.getUserName(),"Username does not match");
		testResultService.assertEquals(firstName, myProfile.getFirstName(),"FirstName does not match");
		testResultService.assertEquals(lastName, myProfile.getLastName(),"LastName does not match");
		myProfile.close(true);
		sleep(3);
		
		if(redirectOnLogout) {
		
		report.startStep("Logout");
		homePage.clickOnLogOut();
		sleep(3);
		
		report.startStep("Verify redirect to referrer URL from parameters");
		String currentUrl = "";
		currentUrl = webDriver.waitForSpecificCurrentUrl(currentUrl, "edusoft");
		testResultService.assertEquals(homePage.CorpUrl, currentUrl,"Referrer site not displayed");
		}		
	}
		
	private void validateThatNoUserNameErrorMessageIsDIsplayed()
			throws Exception {
		WebElement errorElement = webDriver.waitForElement(
				"//div[@class='error'][contains(@ng-show,'userName')]",
				ByTypes.xpath, webDriver.getTimeout(), false);
		List<WebElement> classErrors = webDriver.getChildElementsByXpath(
				errorElement, "//small");
		for (int i = 0; i < classErrors.size(); i++) {
			if (classErrors.get(i).isDisplayed()) {
				testResultService
						.addFailTest("Error text appeared when it should not. text was: "
								+ classErrors.get(i).getText());
			}
		}
		webDriver.printScreen("During validation");

	}

	public void validateErrorMessage(String messageText,
			String validatationStage, String errorType) throws Exception {

		messageText = messageText.replace("&nbsp;", " ");
		WebElement errorElement = webDriver.waitForElement(
				"//li[@class='siteLogin__messageText ng-binding'][contains(@ng-show,'"
						+ errorType + "')]", ByTypes.xpath,
				webDriver.getTimeout(), false);

		if (errorElement == null || errorElement.isDisplayed() == false) {
			testResultService.addFailTest("Error with text: " + messageText
					+ " was not found " + validatationStage);
			webDriver.printScreen(validatationStage);
		} else {
			testResultService.assertEquals(messageText, errorElement.getText(),
					"Text did not mached");

		}

	}
	
	private void setPathToFirefoxDriver(String pathToFirefoxDriver) {
		System.setProperty("webdriver.gecko.driver",pathToFirefoxDriver);
	}
	
	public void OpenBrowser() throws Exception {
		report.startStep("Open browser");
		webDriver.quitBrowser();
		sleep(2);
		webDriver.init();
		sleep(2);
		webDriver.maximize();
		sleep(2);
	}

	@After
	public void tearDown() throws Exception {
				
		super.tearDown();

	}
	
	
	
}
