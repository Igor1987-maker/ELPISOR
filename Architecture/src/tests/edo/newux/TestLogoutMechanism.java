package tests.edo.newux;

import org.junit.After;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

//import com.thoughtworks.selenium.webdriven.commands.WaitForPageToLoad;




import pageObjects.edo.NewUXLoginPage;
import pageObjects.edo.NewUxLearningArea;
import Enums.ByTypes;
import Interfaces.TestCaseParams;
import testCategories.AngularLearningArea;
import testCategories.NonAngularLearningArea;
import testCategories.inProgressTests;
import testCategories.reg3;
import testCategories.unstableTests;

@Category(NonAngularLearningArea.class)
public class TestLogoutMechanism extends BasicNewUxTest {

	NewUXLoginPage loginPage;
	NewUxLearningArea learningArea;
	
	private static final String sessionTimedOutMessage = "Due to inactivity, your session has timed out.\nTo access this page again, please log back into the program.";

	
	@Before
	public void setup() throws Exception {
		super.setup();
		learningArea = new NewUxLearningArea(webDriver, testResultService);
	}

	@Test
	@TestCaseParams(testCaseID = { "24406" })
	public void testLogoutFromLearningArea() throws Exception {
		
		report.startStep("Get user new user and login");
		getUserAndLoginNewUXClass();

		homePage.navigateToCourseUnitAndLesson(courseCodes, "FD", 1, 1);
		sleep(2);
		report.startStep("Click on logout");
		learningArea.clickOnLogoutLearningArea();
		sleep(3);

		report.startStep("Check that user is redirected to the login page");
		String currentUrl = webDriver.getUrl();
		testResultService.assertTrue(
				"User was not redirected to the login page",
				currentUrl.contains("login"));

		report.startStep("Check in the DB that user is logged out");
		testResultService.assertEquals(false,
				studentService.getUserLoginStatus(studentId));

		report.startStep("Try to login again");
		loginAsStudent(studentId);
		sleep(2);
		homePage.navigateToCourse(2);

	}

	@Test
	@TestCaseParams(testCaseID = { "24413" })
	public void testStudentLogoutDuringTest() throws Exception {
		report.startStep("Get user, login and navigate");
		getUserAndLoginNewUXClass();

		report.startStep("Start a test");
		homePage.navigateToRequiredCourseOnHomePage("First Discoveries");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "B1", 1, 1);
		sleep(2);
		learningArea.clickOnStep(3);
		sleep(2);
		learningArea.clickOnStartTest();
		
		sleep(2);
		report.startStep("Click on Logout and cancel");
		learningArea.clickOnLogoutLearningArea();
		sleep(3);
		learningArea.cancelTestConfirmationDialog();
		webDriver.switchToTopMostFrame();
		
		report.startStep("Click on logout and approve");
		sleep(2);
		learningArea.clickOnLogoutLearningArea();
		sleep(3);
		learningArea.approveTest();
		
		sleep(2);
		report.startStep("Check that user is redirected to the login page");
		String currentUrl = webDriver.getUrl();
		testResultService.assertTrue(
				"User was not redirected to the login page",
				currentUrl.contains("login"));

		report.startStep("Check in the DB that user is logged out");
		testResultService.assertEquals(false,
				studentService.getUserLoginStatus(studentId));

		report.startStep("Try to login again");
		loginAsStudent(studentId);
		sleep(1);
		homePage.navigateToCourse(2);
			
		report.startStep("Set progress to first FD course item");
		studentService.createSingleProgressRecored(studentId,"20000", "31515", 10, false, 1);
	
	}
	
	@Test
	@TestCaseParams(testCaseID = { "28858" })
	public void testSessionTimeout() throws Exception {
		
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
			sleep(150);
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
			testResultService.assertEquals(false, loginPage.isSubmitButtonEnabled(), "Checking that ED Login Page displayed");
						
		} finally {
			report.startStep("Set default ED session timeout 120 min");
			pageHelper.setSessionTimeoutForED(120);
		}
		
	
	}
			
	@After
	public void tearDown() throws Exception {
				
		super.tearDown();


	}
	
}
