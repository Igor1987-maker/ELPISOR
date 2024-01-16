package tests.edo.newux;

import javax.swing.JProgressBar;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import pageObjects.edo.NewUxHomePage;
import pageObjects.edo.NewUxLearningArea;
import Enums.ByTypes;
import testCategories.performanceTestsUX;
import testCategories.edoNewUX.HomePage;

@Category(performanceTestsUX.class)
public class PerformanceTests extends BasicNewUxTest {

	@Before
	public void setup() throws Exception {
		enableProxy();
		super.setup();
	}

	@Test
	//@Category(performanceTestsUX.class)
	public void testPerformanceLogin_LogoutStudent() throws Exception {
		report.startStep("Open Browser and login student");

		createUserAndLoginNewUXClass();

		homePage.clickOnLogOut();
		sleep(1);

		report.startStep("Save performance log");
		netService.printSlowRequests(webDriver.getHar(), 300, "LogoutStudentFromHP");
	}
	
	@Test
	//@Category(performanceTestsUX.class)
	public void testPerformanceLogin_LogoutStudentFromLA() throws Exception {
		report.startStep("Open Browser and login student");

		createUserAndLoginNewUXClass();

		report.startStep("Enter to LA:" + courses[1]);
		homePage.clickOnContinueButton();

		homePage.clickOnLogOut();
		sleep(2);

		report.startStep("Save performance log");
		netService.printSlowRequests(webDriver.getHar(), 300, "LogoutStudentFromLA");
	}
	
	@Test
	//@Category(performanceTestsUX.class)
	public void testPerformanceLogin_LogoutStudentFromTest() throws Exception {

		NewUxLearningArea learningArea = new NewUxLearningArea(webDriver, testResultService);

		report.startStep("Create Student");
		String userId = pageHelper.createUSerUsingSP();

		report.startStep("Create Progress" + courses[7]);
		studentService.setProgressInFirstUnitInCourse(courses[7], userId);

		report.startStep("Login");
		loginAsStudent(userId);
		sleep(2);

		report.startStep("Enter to LA:" + courses[7]);
		homePage.clickOnContinueButton();
		sleep(1);

		report.startStep("Navgation To The Test Section");
		learningArea.clickOnStep(3);
		learningArea.getTestStartButton().click();
		sleep(2);

		homePage.clickOnLogOut();
		sleep(1);

		report.startStep("Save performance log");
		netService.printSlowRequests(webDriver.getHar(), 300, "LogoutStudentFromTest");
	}

	@Test
	//@Category(performanceTestsUX.class)
	public void testPerformanceLoginHpWithProgress() throws Exception {

		report.startStep("Create User");
		String userId = pageHelper.createUSerUsingSP();

		report.startStep("Create progress:" + courses[7]);
		studentService.setProgressInFirstUnitInCourse(courses[7], userId);

		report.startStep("Login");
		loginAsStudent(userId);
		sleep(2);

		report.startStep("save performance log");
		netService.printSlowRequests(webDriver.getHar(), 300, "LoginHpWithProgress");
	}

	@Test
	//@Category(performanceTestsUX.class)
	public void testPerformanceBackToHPFromLA() throws Exception {

		NewUxLearningArea learningArea = new NewUxLearningArea(webDriver, testResultService);

		// webDriver.limitConnectionBandwidth(10);
		report.startStep("Create Student");
		String userId = pageHelper.createUSerUsingSP();

		report.startStep("Create Progress" + courses[1]);
		studentService.setProgressInFirstUnitInCourse(courses[7], userId);

		report.startStep("Login");
		loginAsStudent(userId);
		sleep(2);

		report.startStep("Enter to LA:" + courses[1]);
		homePage.clickOnContinueButton();

		report.startStep("Navigate to Home Page");
		learningArea.clickToOpenNavigationBar();
		learningArea.clickOnHomeButton();
		sleep(2);

		report.startStep("save performance log");
		netService.printSlowRequests(webDriver.getHar(), 300, "BackToHP_FromLA");
	}

	@Test
	//@Category(performanceTestsUX.class)
	public void testPerformanceCourseAreaNavigation() throws Exception {

		report.startStep("Open Browser and login student");

		createUserAndLoginNewUXClass();

		report.startStep("Navigate to all courses");
		for (int i = 0; i <= 8; i++) {
			homePage.carouselNavigateNext();
			sleep(1);
		}

		report.startStep("save performance log");
		netService.printSlowRequests(webDriver.getHar(), 300, "CoursesAreaNavigation");
	}

	@Test
	//@Category(performanceTestsUX.class)
	public void testPerformanceNavigateFromExploreToPractice() throws Exception {

		NewUxLearningArea learningArea = new NewUxLearningArea(webDriver, testResultService);

		report.startStep("Create Student");
		String userId = pageHelper.createUSerUsingSP();

		report.startStep("Create Progress" + courses[1]);
		studentService.setProgressInFirstUnitInCourse(courses[7], userId);

		report.startStep("Login");
		loginAsStudent(userId);
		sleep(1);

		report.startStep("Enter to LA:" + courses[1]);
		homePage.clickOnContinueButton();
		sleep(1);

		report.startStep("Navigating from Explore to Practice");
		learningArea.clickOnNextButton();
		sleep(1);

		report.startStep("save performance log");
		netService.printSlowRequests(webDriver.getHar(), 300, "ExploreToPractice");
	}

	@Test
	//@Category(performanceTestsUX.class)
	public void testPerformanceNavigateAllPracticeTasks() throws Exception {

		NewUxLearningArea learningArea = new NewUxLearningArea(webDriver, testResultService);

		report.startStep("Create Student");
		String userId = pageHelper.createUSerUsingSP();

		report.startStep("Create Progress" + courses[1]);
		studentService.setProgressInFirstUnitInCourse(courses[7], userId);

		report.startStep("Login");
		loginAsStudent(userId);
		sleep(2);

		report.startStep("Enter to LA:" + courses[1]);
		homePage.clickOnContinueButton();
		sleep(1);

		report.startStep("Navigating from Explore to Practice");
		learningArea.clickOnNextButton();
		sleep(1);

		report.startStep("Navgation Between All Practice Tasks");
		int numOfTasks = learningArea.getNumberOfTasks();

		for (int i = 1; i < numOfTasks; i++) {
			learningArea.clickOnNextButton();
			sleep(1);
		}

		report.startStep("save performance log");
		netService.printSlowRequests(webDriver.getHar(), 300, "NavigateAllPracticesTasks");
	}

	@Test
	//@Category(performanceTestsUX.class)
	public void testPerformanceNavigateFromPracticeToTest() throws Exception {

		NewUxLearningArea learningArea = new NewUxLearningArea(webDriver, testResultService);

		report.startStep("Create Student Progress and Login");
		String userId = pageHelper.createUSerUsingSP();

		// report.startStep("Create Progress" + courses[7]);
		studentService.setProgressInFirstUnitInCourse(courses[7], userId);

		// report.startStep("Login");
		loginAsStudent(userId);
		sleep(1);

		report.startStep("Enter to LA:" + courses[7]);
		homePage.clickOnContinueButton();
		sleep(1);

		report.startStep("Navigating from Explore to Practice");
		learningArea.clickOnNextButton();
		sleep(1);

		report.startStep("Navgation Between All Practices Tasks");
		int numOfTasks = learningArea.getNumberOfTasks();

		for (int i = 1; i < numOfTasks; i++) {
			learningArea.clickOnNextButton();
			sleep(1);
		}

		report.startStep("Move to Test Step and Start");
		learningArea.clickOnNextButton();
		sleep(1);
		learningArea.getTestStartButton().click();
		sleep(1);

		report.startStep("save performance log");
		netService.printSlowRequests(webDriver.getHar(), 300, "FromPracticeToTest");
	}

	@Test
	//@Category(performanceTestsUX.class)
	public void testPerformanceNavigateFromTestResultToNextLesson() throws Exception {

		NewUxLearningArea learningArea = new NewUxLearningArea(webDriver, testResultService);

		report.startStep("Create Student, Progress and Login");
		String userId = pageHelper.createUSerUsingSP();

		report.startStep("Create Progress" + courses[7]);
		studentService.setProgressInFirstUnitInCourse(courses[7], userId);

		// report.startStep("Login");
		loginAsStudent(userId);
		sleep(1);

		report.startStep("Enter to LA:" + courses[7]);
		homePage.clickOnContinueButton();
		sleep(1);

		report.startStep("Navigating from Explore to end of Practice");
		learningArea.clickOnNextButton();
		sleep(1);

		report.startStep("Navigation Between All Practices Tasks");
		int numOfTasks = learningArea.getNumberOfTasks();

		for (int i = 0; i < numOfTasks; i++) {
			learningArea.clickOnNextButton();
			sleep(1);
		}

		report.startStep("Click on Start Test");
		learningArea.getTestStartButton().click();
		int numOfTestSteps = learningArea.getNumberOfTasks();

		for (int i = 1; i < numOfTestSteps; i++) {
			learningArea.clickOnNextButton();
			sleep(1);
		}

		learningArea.submitTest(true);
		sleep(1);

		learningArea.clickOnNextButton();
		sleep(1);

		report.startStep("save performance log");
		netService.printSlowRequests(webDriver.getHar(), 300, "TestResultToNextLesson");
	}

	@Test
	//@Category(performanceTestsUX.class)
	public void testPerformanceNavigateFromLastLessonToNextUnit() throws Exception {

		NewUxLearningArea learningArea = new NewUxLearningArea(webDriver, testResultService);

		report.startStep("Create Student");
		String userId = pageHelper.createUSerUsingSP();

		report.startStep("Create Progress" + courses[7]);
		studentService.setProgressInFirstUnitInCourse(courses[7], userId);

		report.startStep("Login");
		loginAsStudent(userId);
		sleep(2);

		report.startStep("Enter to LA:" + courses[7]);

		homePage.clickOnUnitLessons(3);
		homePage.clickOnLesson(2, 5);
		sleep(2);

		report.startStep("Navgation To The Test Section");
		learningArea.clickOnStep(3);

		learningArea.getTestStartButton().click();
		int numOfTestSteps = learningArea.getNumberOfTasks();

		for (int i = 0; i < numOfTestSteps; i++) {
			learningArea.clickOnNextButton();
		}

		learningArea.submitTest(true);

		learningArea.clickOnNextButton();
		sleep(2);

		report.startStep("save performance log");
		netService.printSlowRequests(webDriver.getHar(), 300, "FromLastLessonToNextUnit");
	}

	@Test
	//@Category(performanceTestsUX.class)
	public void testPerformanceOpenMyProfileWindow() throws Exception {

		report.startStep("Create Student");
		String userId = pageHelper.createUSerUsingSP();

		report.startStep("Create Progress" + courses[7]);
		studentService.setProgressInFirstUnitInCourse(courses[7], userId);

		report.startStep("Login");
		loginAsStudent(userId);
		sleep(2);

		report.startStep("Open 'My Profile' window");
		homePage.clickOnMyProfile();

		report.startStep("save performance log");
		netService.printSlowRequests(webDriver.getHar(), 300, "OpenMyProfile");
	}

	@Test
	//@Category(performanceTestsUX.class)
	public void testPerformanceUpdateMyProfile() throws Exception {

		report.startStep("Create Student");
		String userId = pageHelper.createUSerUsingSP();

		report.startStep("Create Progress" + courses[7]);
		studentService.setProgressInFirstUnitInCourse(courses[7], userId);

		report.startStep("Login");
		loginAsStudent(userId);
		sleep(2);

		report.startStep("Open 'My Profile' window");
		homePage.clickOnMyProfile();

		report.startStep("Update some info in 'My Profile' section");
		homePage.switchToMyProfile();

		homePage.UpdateMyProfile();

		report.startStep("save performance log");
		netService.printSlowRequests(webDriver.getHar(), 300, "UpdateMyProfile");
	}

	@Test
	//@Category(performanceTestsUX.class)
	public void testPerformanceOpenLegalNotices() throws Exception {

		report.startStep("Create Student");
		String userId = pageHelper.createUSerUsingSP();

		report.startStep("Create Progress" + courses[7]);
		studentService.setProgressInFirstUnitInCourse(courses[7], userId);

		report.startStep("Login");
		loginAsStudent(userId);
		sleep(2);

		report.startStep("Open 'Legal Notices' window");
		homePage.clickOnLegalNotice();
		sleep(2);

		report.startStep("save performance log");
		netService.printSlowRequests(webDriver.getHar(), 300, "OpenLegalNotices");
	}

	@Test
	//@Category(performanceTestsUX.class)
	public void testPerformanceOpenPrivacyStatement() throws Exception {

		report.startStep("Create Student");
		String userId = pageHelper.createUSerUsingSP();

		report.startStep("Create Progress" + courses[7]);
		studentService.setProgressInFirstUnitInCourse(courses[7], userId);

		report.startStep("Login");
		loginAsStudent(userId);
		sleep(2);

		report.startStep("Open 'Legal Notices' window");
		homePage.clickOnPrivacyStatement();
		sleep(2);

		report.startStep("save performance log");
		netService.printSlowRequests(webDriver.getHar(), 300, "OpenPrivacyStatement");
	}

	@Test
	//@Category(performanceTestsUX.class)
	public void testPerformanceNavigationToListeningTV() throws Exception {

		report.startStep("Create Student");
		String userId = pageHelper.createUSerUsingSP();

		report.startStep("Create Progress" + courses[7]);
		studentService.setProgressInFirstUnitInCourse(courses[7], userId);

		report.startStep("Login");
		loginAsStudent(userId);
		sleep(2);

		report.startStep("Enter to LA:" + courses[7]);

		homePage.clickOnUnitLessons(1);
		homePage.clickOnLesson(0, 1);
		sleep(2);

		homePage.clickOnLogOut();

		report.startStep("save performance log");
		netService.printSlowRequests(webDriver.getHar(), 300, "NavigationToListeningTV");
	}

	@Test
	//@Category(performanceTestsUX.class)
	public void testPerformanceNavigationToListeningRadio() throws Exception {

		report.startStep("Create Student");
		String userId = pageHelper.createUSerUsingSP();

		report.startStep("Create Progress" + courses[9]);
		studentService.setProgressInFirstUnitInCourse(courses[9], userId);

		report.startStep("Login");
		loginAsStudent(userId);
		sleep(2);

		report.startStep("Enter to LA:" + courses[9]);

		homePage.clickOnUnitLessons(6);
		homePage.clickOnLesson(5, 1);
		sleep(2);

		homePage.clickOnLogOut();

		report.startStep("save performance log");
		netService.printSlowRequests(webDriver.getHar(), 300, "NavigationToListeningRadio");
	}

	@Test
	//@Category(performanceTestsUX.class)
	public void testPerformanceNavigationToReading() throws Exception {

		report.startStep("Create Student");
		String userId = pageHelper.createUSerUsingSP();

		report.startStep("Create Progress" + courses[9]);
		studentService.setProgressInFirstUnitInCourse(courses[9], userId);

		report.startStep("Login");
		loginAsStudent(userId);
		sleep(2);

		report.startStep("Enter to LA:" + courses[9]);

		homePage.clickOnUnitLessons(5);
		homePage.clickOnLesson(4, 2);
		sleep(2);

		homePage.clickOnLogOut();

		report.startStep("save performance log");
		netService.printSlowRequests(webDriver.getHar(), 300, "NavigationToReading");
	}

	@Test
	//@Category(performanceTestsUX.class)
	public void testPerformanceNavigationToGrammer() throws Exception {

		report.startStep("Create Student");
		String userId = pageHelper.createUSerUsingSP();

		report.startStep("Create Progress" + courses[5]);
		studentService.setProgressInFirstUnitInCourse(courses[5], userId);

		report.startStep("Login");
		loginAsStudent(userId);
		sleep(2);

		report.startStep("Enter to LA:" + courses[5]);

		homePage.clickOnUnitLessons(5);
		homePage.clickOnLesson(4, 4);
		sleep(2);

		homePage.clickOnLogOut();

		report.startStep("save performance log");
		netService.printSlowRequests(webDriver.getHar(), 300, "NavigationToGrammer");
	}

	@Test
	//@Category(performanceTestsUX.class)
	public void testPerformanceNavigationToSpeakingFD_CashCredit_Preaper() throws Exception {

		report.startStep("Create Student");
		String userId = pageHelper.createUSerUsingSP();

		report.startStep("Create Progress" + courses[0]);
		studentService.setProgressInFirstUnitInCourse(courses[0], userId);

		report.startStep("Login");
		loginAsStudent(userId);
		sleep(2);

		report.startStep("Enter to LA:" + courses[0]);

		homePage.clickOnUnitLessons(3);
		homePage.clickOnLesson(2, 2);
		sleep(2);

		homePage.clickOnLogOut();

		report.startStep("save performance log");
		netService.printSlowRequests(webDriver.getHar(), 300, "NavigationToSpeakingFD_CashCredit_Preaper");
	}

	@Test
	//@Category(performanceTestsUX.class)
	public void testPerformanceNavigationToSpeakingFD_CashCredit_Explore() throws Exception {

		NewUxLearningArea learningArea = new NewUxLearningArea(webDriver, testResultService);

		report.startStep("Create Student");
		String userId = pageHelper.createUSerUsingSP();

		report.startStep("Create Progress" + courses[0]);
		studentService.setProgressInFirstUnitInCourse(courses[0], userId);

		report.startStep("Login");
		loginAsStudent(userId);
		sleep(2);

		report.startStep("Enter to LA:" + courses[0]);

		homePage.clickOnUnitLessons(3);
		homePage.clickOnLesson(2, 2);
		sleep(2);

		learningArea.clickOnNextButton();

		homePage.clickOnLogOut();

		report.startStep("save performance log");
		netService.printSlowRequests(webDriver.getHar(), 300, "NavigationToSpeakingFD_CashCredit_Explore");
	}

	@Test
	//@Category(performanceTestsUX.class)
	public void testPerformanceNavigationToSpeaking_B3() throws Exception {

		report.startStep("Create Student");
		String userId = pageHelper.createUSerUsingSP();

		report.startStep("Create Progress" + courses[3]);
		studentService.setProgressInFirstUnitInCourse(courses[3], userId);

		report.startStep("Login");
		loginAsStudent(userId);
		sleep(2);

		report.startStep("Enter to LA:" + courses[3]);

		homePage.clickOnUnitLessons(4);
		homePage.clickOnLesson(3, 3);
		sleep(2);

		homePage.clickOnLogOut();

		report.startStep("save performance log");
		netService.printSlowRequests(webDriver.getHar(), 300, "NavigationToSpeaking_B3");
	}

	@Test
	//@Category(performanceTestsUX.class)
	public void testPerformanceNavigationToWriting_FD() throws Exception {

		report.startStep("Create Student");
		String userId = pageHelper.createUSerUsingSP();

		report.startStep("Create Progress" + courses[0]);
		studentService.setProgressInFirstUnitInCourse(courses[0], userId);

		report.startStep("Login");
		loginAsStudent(userId);
		sleep(2);

		report.startStep("Enter to LA:" + courses[0]);

		homePage.clickOnUnitLessons(2);
		homePage.clickOnLesson(1, 4);
		sleep(2);

		homePage.clickOnLogOut();

		report.startStep("save performance log");
		netService.printSlowRequests(webDriver.getHar(), 300, "NavigationToWriting_FD");
	}

	@Test
	//@Category(performanceTestsUX.class)
	public void testPerformanceNavigationToWriting_FD_Explore() throws Exception {

		NewUxLearningArea learningArea = new NewUxLearningArea(webDriver, testResultService);

		report.startStep("Create Student");
		String userId = pageHelper.createUSerUsingSP();

		report.startStep("Create Progress" + courses[0]);
		studentService.setProgressInFirstUnitInCourse(courses[0], userId);

		report.startStep("Login");
		loginAsStudent(userId);
		sleep(2);

		report.startStep("Enter to LA:" + courses[0]);

		homePage.clickOnUnitLessons(2);
		homePage.clickOnLesson(1, 4);
		sleep(2);

		learningArea.clickOnNextButton();

		homePage.clickOnLogOut();

		report.startStep("save performance log");
		netService.printSlowRequests(webDriver.getHar(), 300, "NavigationToWriting_FD_Explore");
	}

	@Test
	//@Category(performanceTestsUX.class)
	public void testPerformanceNavigationToAlphabet() throws Exception {

		report.startStep("Create Student");
		String userId = pageHelper.createUSerUsingSP();

		report.startStep("Create Progress" + courses[0]);
		studentService.setProgressInFirstUnitInCourse(courses[0], userId);

		report.startStep("Login");
		loginAsStudent(userId);
		sleep(2);

		report.startStep("Enter to LA:" + courses[0]);

		homePage.clickOnUnitLessons(1);
		homePage.clickOnLesson(0, 1);
		sleep(2);

		homePage.clickOnLogOut();

		report.startStep("save performance log");
		netService.printSlowRequests(webDriver.getHar(), 300, "NavigationToAlphabet");
	}

	@Test
	//@Category(performanceTestsUX.class)
	public void testPerformanceNavigationTo_OldVocabulary() throws Exception {

		report.startStep("Create Student");
		String userId = pageHelper.createUSerUsingSP();

		report.startStep("Create Progress" + courses[0]);
		studentService.setProgressInFirstUnitInCourse(courses[0], userId);

		report.startStep("Login");
		loginAsStudent(userId);
		sleep(2);

		report.startStep("Enter to LA:" + courses[0]);

		homePage.clickOnUnitLessons(1);
		homePage.clickOnLesson(0, 5);
		sleep(2);

		homePage.clickOnLogOut();

		report.startStep("save performance log");
		netService.printSlowRequests(webDriver.getHar(), 300, "NavigationTo_OldVocabulary");
	}

	@Test
	//@Category(performanceTestsUX.class)
	public void testPerformanceNavigationTo_NewVocabulary() throws Exception {

		report.startStep("Create Student");
		String userId = pageHelper.createUSerUsingSP();

		report.startStep("Create Progress" + courses[1]);
		studentService.setProgressInFirstUnitInCourse(courses[1], userId);

		report.startStep("Login");
		loginAsStudent(userId);
		sleep(2);

		report.startStep("Enter to LA:" + courses[1]);

		homePage.clickOnUnitLessons(2);
		homePage.clickOnLesson(1, 6);
		sleep(2);

		homePage.clickOnLogOut();

		report.startStep("save performance log");
		netService.printSlowRequests(webDriver.getHar(), 300, "NavigationTo_NewVocabulary");
	}
	@After
	public void tearDown() throws Exception {
			super.tearDown();
	}
	
}
