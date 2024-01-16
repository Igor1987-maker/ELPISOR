package tests.edo.newux;

import Enums.ByTypes;
import Interfaces.TestCaseParams;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import pageObjects.edo.NewUXLoginPage;
import pageObjects.edo.NewUxLearningArea2;
import services.PageHelperService;
import testCategories.AngularLearningArea;
import testCategories.inProgressTests;

import java.io.IOException;

@Category(AngularLearningArea.class)
public class TestLogoutMechanism2 extends BasicNewUxTest {

	NewUXLoginPage loginPage;
	NewUxLearningArea2 learningArea2;

	private static final String sessionTimedOutMessage = "Due to inactivity, your session has timed out.\nTo access this page again, please log back into the program.";

	public TestLogoutMechanism2() {
		
	}
	
	@Before
	public void setup() throws Exception {
		super.setup();
	}

	@Test
	@TestCaseParams(testCaseID = { "24406" })
	public void testLogoutFromLearningArea() throws Exception {

		report.startStep("Get user new user and login");
		getUserAndLoginNewUXClass();

		report.startStep("Navigate to  LA");
		learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "FD", 1, 1);
		sleep(2);
		learningArea2.clickOnStep(2);

		report.startStep("Click on logout");
		learningArea2.clickOnLogoutLearningArea();
		sleep(3);

		report.startStep("Check that user is redirected to the login page");
		String currentUrl = webDriver.getUrl();
		testResultService.assertTrue("User was not redirected to the login page", currentUrl.contains("login"));

		report.startStep("Check in the DB that user is logged out");
		testResultService.assertEquals(false, studentService.getUserLoginStatus(studentId));

		report.startStep("Try to login again");
		loginAsStudent(studentId);
		sleep(2);
		homePage.closeAllNotifications();
		homePage.navigateToCourse(2);

	}

	@Test
	@TestCaseParams(testCaseID = { "24413" })
	public void testStudentLogoutDuringTest() throws Exception {
		report.startStep("Get user, login and navigate");
		homePage = getUserAndLoginNewUXClass();

		report.startStep("Start a test");
		learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "B1", 1, 1);
		sleep(2);
		learningArea2.clickOnStep(3, false);
		sleep(2);
		learningArea2.clickOnStartTest();
		sleep(2);

		report.startStep("Click on Logout and cancel");
		learningArea2.clickOnLogoutLearningArea();
		sleep(3);
		learningArea2.cancelTestConfirmationDialog();
		webDriver.switchToTopMostFrame();

		report.startStep("Click on logout and approve");
		sleep(2);
		learningArea2.clickOnLogoutLearningArea();
		sleep(3);
		learningArea2.approveTest();

		sleep(2);
		report.startStep("Check that user is redirected to the login page");
		String currentUrl = webDriver.getUrl();
		testResultService.assertTrue("User was not redirected to the login page", currentUrl.contains("login"));

		report.startStep("Check in the DB that user is logged out");
		testResultService.assertEquals(false, studentService.getUserLoginStatus(studentId));

		report.startStep("Try to login again");
		loginAsStudent(studentId);
		sleep(1);
		homePage.closeAllNotifications();
		homePage.navigateToCourse(2);

		report.startStep("Set progress to first FD course item");
		studentService.createSingleProgressRecored(studentId, "20000", "31515", 10, false, 1);

	}
	
	@Test
	@TestCaseParams(testCaseID = { "84444" })
	public void testAutoLogOutOfInactiveUser() throws Exception {
	try {
			
				
			report.startStep("LogIn");
				NewUXLoginPage loginPage = new NewUXLoginPage(webDriver, testResultService);
				loginPage.waitForPageToLoad();			
				getUserAndLoginNewUXClass();
				
			report.startStep("Change token in local storage to get session time out");
				String token = pageHelper.runJavaScriptCommand("return localStorage.getItem(\"EDAPPToken\")");
				pageHelper.runJavaScriptCommand("localStorage.setItem('EDAPPToken','"+token+"11')");//localStorage.setItem('EDAPPToken','');
				
			report.startStep("Wait till session get expired");
				homePage.waitTillInActivityPageOrTimeExpirationApears(60);
					
			report.startStep("Verify user get the inactivity page and click OK");
				//loginPage.verifyUserLoggedInMessage(sessionTimedOutMessage, true);
				String message = webDriver.waitForElement("h1", ByTypes.tagName).getText();
				testResultService.assertEquals("This session is no longer active on this device.", message, "Wrong session timeOut message");
				loginPage.clickOK_onSessionIsNoLongerActive();
				sleep(1);
				testResultService.assertEquals(false, loginPage.isSubmitButtonEnabled(),"Checking that ED Login Page displayed");
						 
			report.startStep("verify the user logged out in DB");
				boolean status = studentService.getUserLoginStatus(studentId);
				testResultService.assertEquals(true, status,"Checking that user logged out from DB as well");
			
			report.startStep("Relogin and verify user landed to home page");
				homePage = loginPage.loginAsStudent(userName, password);
				sleep(1);
				loginPage.verifyAndConfirmAlertMessage();
				homePage.closeAllNotifications();
				homePage.waitHomePageloaded();
			
			report.startStep("Log out of ED");
				homePage.logOutOfED();

		} catch (IOException e) {
			e.printStackTrace();
			testResultService.addFailTest(e.getMessage(), true, true);
		} catch (InterruptedException e) {
			e.printStackTrace();
			testResultService.addFailTest(e.getMessage(), true, true);
		} catch (Exception e) {
			e.printStackTrace();
			testResultService.addFailTest(e.getMessage(), true, true);
		} 
	}
	

	@Test
	@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "84444" })
	public void testManuallyAutoLogOut() throws Exception {

		try {
			
			NewUXLoginPage loginPage = new NewUXLoginPage(webDriver, testResultService);
			loginPage.waitForPageToLoad();

			
			if(pageHelper.linkED.contains("engdis.com")) {
				getUserAndLoginNewUXClass();
				String token = pageHelper.runJavaScriptCommand("return localStorage.getItem(\"EDAPPToken\")");
				pageHelper.runJavaScriptCommand("localStorage.setItem('EDAPPToken','"+token+"11')");//localStorage.setItem('EDAPPToken','');
				homePage.waitTillInActivityPageOrTimeExpirationApears(60);
			}else {
			
			report.startStep("Set expiration time to service appjson settings (minutes)");
				pageHelper.setSessionExpirationTime("2");
							
			report.startStep("restart the application pool");
				pageHelper.restartWebServiceApplicationPool();
				webDriver.refresh();
				
						
			report.startStep("Get user and login");
				//getUserAndLoginNewUXClass();
				String[] userDetails = pageHelper.getUserFromDbByInstitutionIdAndClassName(BasicNewUxTest.institutionId, configuration.getClassName());
				userName = userDetails[0];
				password = userDetails[1];
			    studentId = userDetails[2];
				dbService.setUserOptIn(studentId, true);
				homePage = loginPage.loginAsStudent(userName, password);
				loginPage.waitLoginAfterRestartAppPool();
				homePage.waitNotificationsAfterRestartPool();
				homePage.closeAllNotifications();
				pageHelper.skipOnBoardingHP();
				homePage.waitHomePageloadedFully();
			
		
			report.startStep("Wait until Session time out is displayed or 140 seconds");
				homePage.waitTillInActivityPageOrTimeExpirationApears(140);
			}
			
			report.startStep("Verify user get the inactivity page and click OK");
				loginPage = new NewUXLoginPage(webDriver, testResultService);
				//loginPage.verifyUserLoggedInMessage(sessionTimedOutMessage, true);
				String message = webDriver.waitForElement("h1", ByTypes.tagName).getText();
				testResultService.assertEquals("This session is no longer active on this device.", message, "Wrong session timeOut message");
				loginPage.clickOK_onSessionIsNoLongerActive();
				sleep(1);
				testResultService.assertEquals(false, loginPage.isSubmitButtonEnabled(),"Checking that ED Login Page displayed");
			
			 
			report.startStep("verify the user logged out in DB");
				boolean status = studentService.getUserLoginStatus(studentId);
				testResultService.assertEquals(true, status,"Checking that user logged out from DB as well");
			
			report.startStep("Relogin and verify user landed to home page");
				//getUserAndLoginNewUXClass();
				//homePage.closeAllNotifications();
				homePage = loginPage.loginAsStudent(userName, password);
				sleep(1);
				loginPage.verifyAndConfirmAlertMessage();
				homePage.closeAllNotifications();
				pageHelper.skipOnBoardingHP();
				homePage.waitHomePageloaded();
			
			report.startStep("Log out of ED");
				homePage.logOutOfED();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(!pageHelper.linkED.contains("engdis.com")) {
					setSessionExpirationTime("120");
			try {
				pageHelper.restartWebServiceApplicationPool();			
			} catch (Exception e) {
				e.printStackTrace();
				}
			}
		}
	}
	
	private void waitTillInActivityPageOrTimeExpirationApears(int time) throws Exception {
		int steps = time/30;
				
		for(int i = 0; i<steps; i++) {
			String url = pageHelper.getCurrentUrl();
			System.out.println(url);
			boolean islogOut = url.endsWith("SessionTimeout");
			if(islogOut) {
				break;
			}
			sleep(4);
			
		}
		
	}

	private void restartWebServiceApplicationPool() {
		try {
			pageHelper.runConsoleProgram(PageHelperService.batFilesRestartPoolPath + "StopAppPoolRemotely.bat");
			sleep(4);
		
			//	Runtime.getRuntime().exec("C:/automation/BatFiles/StartAppPoolRemotely.bat");
			pageHelper.runConsoleProgram(PageHelperService.batFilesRestartPoolPath + "StartAppPoolRemotely.bat");
			sleep(4);
		
		} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	private void setSessionExpirationTime(String time) {
		
		textService.updateJsonFileByKeyValue(PageHelperService.edUiServicePath,"appsettings","Jwt","ExpirationInMinutes",time);
		
	}

	
	@Ignore
	@Test
	@TestCaseParams(testCaseID = { "28858" })
	public void testSessionTimeout() throws Exception {

		report.startStep("Set ED session timeout for 2 min");
		pageHelper.setSessionTimeoutForED(2);

		try {
			report.startStep("Get user, login and navigate");
			webDriver.deleteCookiesAndRefresh();
			pageHelper.restartBrowserInNewURL(institutionName, false);
			getUserAndLoginNewUXClass();

			report.startStep("Enter Learning Area");
			homePage.navigateToCourseUnitAndLesson(courseCodes, "FD", 1, 1);
			sleep(2);

			report.startStep("Wait 3 min for session timeout");
			sleep(180);
			loginPage = new NewUXLoginPage(webDriver, testResultService);

			report.startStep("Verifiy that ED logo is displayed in Footer and banner in Header");
			testResultService.assertEquals(true, loginPage.getfFooterImageDisplayed(), "Footer image is not displayed");
			loginPage.verifyDefaultBannerExist();

			report.startStep("Check Session Timeout message displayed and click OK");
			loginPage.verifyUserLoggedInMessage(sessionTimedOutMessage, true);
			sleep(2);

			report.startStep("Check that user is redirected to the login page");
			String currentUrl = webDriver.getUrl();
			testResultService.assertTrue("User was not redirected to the login page", currentUrl.contains("login"));
			testResultService.assertEquals(false, loginPage.isSubmitButtonEnabled(),
					"Checking that ED Login Page displayed");

		} finally {
			report.startStep("Set default ED session timeout 120 min");
			pageHelper.setSessionTimeoutForED(120);
		}

	}
	

// igb 2018.11.28 ---------------------------
	@Ignore
	@Test
	@TestCaseParams(testCaseID = { "53200" })
	public void testSessionTimeoutNew() throws Exception {

		report.startStep("Set ED session timeout for 2 min");
		pageHelper.setSessionTimeoutForED(2);

		try {
			report.startStep("Get user, login and navigate");
			webDriver.deleteCookiesAndRefresh();
			getUserAndLoginNewUXClass();

			report.startStep("Enter Learning Area");
			homePage.navigateToCourseUnitAndLesson(courseCodes, "FD", 1, 1);
			sleep(2);

			report.startStep("Wait 3 min for session timeout");
			sleep(180);
			String LastUserLogOut01 = dbService.getLastUserLogOut(studentId);
			loginPage = new NewUXLoginPage(webDriver, testResultService);

			report.startStep("Verifiy that ED logo is displayed in Footer and banner in Header");
			testResultService.assertEquals(true, loginPage.getfFooterImageDisplayed(), "Footer image is not displayed");
			loginPage.verifyDefaultBannerExist();

			report.startStep("Check Session Timeout message displayed, Wait 2 min and Click OK");
			verifyUserLoggedInMessage(sessionTimedOutMessage, 90, true);
			String LastUserLogOut02 = dbService.getLastUserLogOut(studentId);
			testResultService.assertEquals(LastUserLogOut01, LastUserLogOut02, "LogOut was changed", false);
			sleep(2);

			report.startStep("Check that user is redirected to the login page");
			String currentUrl = webDriver.getUrl();
			testResultService.assertTrue("User was not redirected to the login page", currentUrl.contains("login"));
			testResultService.assertEquals(false, loginPage.isSubmitButtonEnabled(),
					"Checking that ED Login Page displayed");

		} finally {
			report.startStep("Set default ED session timeout 120 min");
			pageHelper.setSessionTimeoutForED(120);
		}

	}

	private void verifyUserLoggedInMessage(String expected, int waitSec, boolean clickOK) throws Exception {

		String actual = webDriver.waitForElement("waitForLogin__messageW", ByTypes.className, "Message not found")
				.getText();

		testResultService.assertEquals(expected, actual, "Message is not valid");

		if (waitSec > 0)
			sleep(waitSec);

		if (clickOK)
			webDriver.waitForElement("OK", ByTypes.linkText, "OK btn not found").click();
	}
// igb 2018.11.28 ---------------------------

	@After
	public void tearDown() throws Exception {

		super.tearDown();

	}

}
