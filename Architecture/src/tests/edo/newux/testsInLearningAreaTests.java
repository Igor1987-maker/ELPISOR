package tests.edo.newux;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.After;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import pageObjects.edo.NewUXLoginPage;
import pageObjects.edo.NewUxDragAndDropSection;
import pageObjects.edo.NewUxHomePage;
import pageObjects.edo.NewUxLearningArea;
import pageObjects.edo.NewUxMyProgressPage;
import pageObjects.tms.TmsCourseTestReport;
import pageObjects.tms.TmsHomePage;
import testCategories.NonAngularLearningArea;
import Interfaces.TestCaseParams;

@Category(NonAngularLearningArea.class)
public class testsInLearningAreaTests extends BasicNewUxTest {

	NewUXLoginPage loginPage;
	NewUxLearningArea learningArea;
	NewUxDragAndDropSection dragAndDrop;
	NewUxMyProgressPage myProgress;
	TmsHomePage tmsHomePage;
	TmsCourseTestReport tmsCTR;
	
	
	protected static final String feedbackTextUnderTestScore = "Please review your answers.";
	protected static final String feedbackTextAboveTestScore = "To review your answers, click on the test navigation bar above.";
	protected static final String alreadyCompletedTestOnce = "You have already completed this test. Please continue to your next assignment.";
	protected static final String testExitAlert = "You haven't submitted your test. If you exit now, your test results will not be recorded.";
	
	
	@Test
	@TestCaseParams(testCaseID = { "22672" })
	public void testStartButtonFinctionality() throws Exception {

		report.startStep("Create new user and login");
		createUserAndLoginNewUXClass();

		report.startStep("Navigate to Basic 1, Against the Law, The Diamond Necklace");

		homePage.navigateToCourseUnitAndLesson(courseCodes, "B1", 2, 2);
		learningArea = new NewUxLearningArea(webDriver,
				testResultService);

		report.startStep("Go to Test step and check");
		learningArea.clickOnStep(3);

		learningArea.checkthatTestButtonIsDisplayed();

		for (int i = 0; i < 5; i++) {
			learningArea.checkThatTaskIsDisabled(i);
		}
		learningArea.checkThatNextButtonIsNotDisplayedInLA();

		report.startStep("Go to practive step and click next to the to the tests");
		learningArea.clickOnStep(2);

		for (int i = 0; i < 6; i++) {

			Thread.sleep(400);
			learningArea.clickOnNextButton();
		}

		learningArea.checkthatTestButtonIsDisplayed();

		learningArea.checkThatNextButtonIsNotDisplayedInLA();

		learningArea.clickTheStartTestButon();

		learningArea.checkThatSubmitButtonIsDisplayed();

		learningArea.checkThatNextButtonIsEnabled();

		learningArea.checkThatTaskIsVisitedAndCurrent(0);

		report.startStep("Navigate to last task");

		for (int i = 0; i < 4; i++) {
			learningArea.clickOnNextButton();
			Thread.sleep(400);
			learningArea.checkThatSubmitButtonIsDisplayed();
		}

		learningArea.checkThatNextButtonIsDisabled();

	}
	
	@Test
	@TestCaseParams(testCaseID = { "22678" })
	public void testQuitTestWithoutSubmit() throws Exception {

		report.startStep("Create new user and login");
		createUserAndLoginNewUXClass();

		report.startStep("Navigate to B1-U1-L1-S3");
		learningArea = homePage.navigateToCourseUnitAndLesson(courseCodes, "B1", 1, 1);
		sleep(1);
		learningArea.clickOnStep(3);
		
		report.startStep("Click on Start Test");
		learningArea.clickTheStartTestButon();
		sleep(1);
		
		report.startStep("Press on Step 1");
		learningArea.clickOnStep(1);

		report.startStep("Check alert and press Cancel");
		learningArea.validateQuitPopupMessageinTest(testExitAlert, false);
		
		report.startStep("Check remain in Test");
		learningArea.checkThatStepNameIsDisplayed("Test");
		
		report.startStep("Press on Step 2");
		learningArea.clickOnStep(2);
		sleep(1);
		
		report.startStep("Check alert and press Continue");
		learningArea.validateQuitPopupMessageinTest(testExitAlert, true);
		
		report.startStep("Check navigation to Step 2");
		learningArea.checkThatStepNameIsDisplayed("Practice");



	}

	@Test
	@TestCaseParams(testCaseID = { "22674" })
	public void testSubmitButtonFunctionalityFullNavigation() throws Exception {
		
		report.startStep("Create user and login");
		createUserAndLoginNewUXClass();
		int numOfTestTasks = 5;

		report.startStep("Navigate to Basic 3, Getting a job, Drama");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "B3", 1, 1);

		report.startStep("Click on Test step");
		learningArea = new NewUxLearningArea(webDriver, testResultService);
		learningArea.clickOnStep(3);
		learningArea.clickTheStartTestButon();

		report.startStep("Complete the test");

		for (int i = 0; i < numOfTestTasks; i++) {
			learningArea.selectMultipleAnswer(1);
			if (i!=numOfTestTasks-1) learningArea.clickOnNextButton();
			
		}
		
		learningArea.submitTest(false);

		report.startStep("Check text result and relevent controls");

		learningArea.checkThatTestWasSubmitted();

	}

	@Test
	@TestCaseParams(testCaseID = { "22675" })
	public void testMoveToNextLessonAfterSubmit() throws Exception {

		report.startStep("Create user and login");
		createUserAndLoginNewUXClass();
		
		report.startStep("Navigate to Basic 1, Against the Law, Follow That Man");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "B1", 2, 2);
		sleep(1);
		
		report.startStep("Click on Next for 6 times");
		learningArea = new NewUxLearningArea(webDriver, testResultService);
		learningArea.clickOnNextButton(6);

		report.startStep("Click on Next - Start test button should be displyaed");
		learningArea.clickOnNextButton();
		sleep(3);
		learningArea.clickTheStartTestButon();
		sleep(1);

		report.startStep("Navigate to task 3 and click on Submit");
		learningArea.clickOnTask(4);
		learningArea.getSubmitButton().click();
		sleep(1);
		learningArea.validateSubmitPopupMessageAndSubmit("Are you sure");
		webDriver.switchToTopMostFrame();
		sleep(1);

		report.startStep("Click on the next button");
		
		learningArea.clickOnNextButton();
		sleep(5);

		String currentLesson = learningArea.getLessonName();
		String headerTitle = learningArea.getHeaderTitle();

		testResultService.assertEquals("No Parking", currentLesson,"Lesson name is not correct", true);
		testResultService.assertEquals("Unit 2: Against The Law", headerTitle,"Unit name is not correct", true);
	}

	@Test
	@TestCaseParams(testCaseID = { "22682","40255" }, skippedBrowsers = {"firefox"}) // Actions API not supported by geckodriver)
	public void testTestResultsPageAndWidgets() throws Exception {
		
		learningArea = new NewUxLearningArea(webDriver, testResultService);
		dragAndDrop = new NewUxDragAndDropSection(webDriver, testResultService);
		
		report.startStep("Init test data");
		int courseIndex = 1; // Basic 1
		int unitNumber = 1;
		int lessonNumber = 1;
		
		report.startStep("Create user and login");
		createUserAndLoginNewUXClass();
		sleep(3);
		
		report.startStep("Navigate to Test, submit and check score");
		String lastScore = navigateToTestSubmitAndCheckScore(homePage, learningArea, dragAndDrop);
		
		report.startStep("Check Test Results & Answers Preview Pages");
		checkTestResultsPages(studentId, false);
						
		report.startStep("Go To Home Page and check Average Test Score");
		//learningArea.clickToOpenNavigationBar();
		learningArea.clickOnHomeButton();
		sleep(2);
		testResultService.assertEquals(lastScore.replace("%", ""), homePage.getScoreWidgetValue(), "Checking Test Score in Student widget on Home Page");
					
		report.startStep("Open My Progress page and check FIRST test score displayed");
		myProgress = homePage.clickOnMyProgress();
		sleep(1);
		myProgress.clickToOpenUnitLessonsProgress(unitNumber);
		sleep(1);
		String actLastScore = myProgress.getLessonLastTestScore(lessonNumber);
		testResultService.assertEquals(lastScore, actLastScore, "First Test Score in My Progress is wrong");
		
		report.startStep("Submit test again and check LAST test score displayed");
		studentService.submitTestsForCourse(studentId, courses[courseIndex], false);
		webDriver.refresh();
		sleep(1);
		myProgress.clickToOpenUnitLessonsProgress(unitNumber);
		sleep(1);
		lastScore = "100%";
		actLastScore = myProgress.getLessonLastTestScore(lessonNumber);
		testResultService.assertEquals(lastScore, actLastScore, "Last Test Score in My Progress is wrong");
	}
	
	@Test
	@TestCaseParams(testCaseID = { "33183" }, testMultiple = true, skippedBrowsers = {"firefox"}) // Actions API not supported by geckodriver)
	public void testTestResultsSeeAnswerDisable() throws Exception {
	
		learningArea = new NewUxLearningArea(webDriver, testResultService);
		dragAndDrop = new NewUxDragAndDropSection(webDriver, testResultService);
		homePage = new NewUxHomePage(webDriver, testResultService);
		
		String className = configuration.getProperty("classname.locked");
		String instID = configuration.getInstitutionId();
		
		report.startStep("Disable See Answers option for class");
		pageHelper.disableSeeTestAnswersToClass(className,instID);
				
		report.startStep("Create new student and login");
		studentId = pageHelper.createUSerUsingSP(instID, className);
		String userName = dbService.getUserNameById(studentId, instID);
		NewUXLoginPage loginPage = new NewUXLoginPage(webDriver, testResultService);
		loginPage.loginAsStudent(userName, "12345");
		
		report.startStep("Navigate to Test, submit and check score");
		navigateToTestSubmitAndCheckScore(homePage, learningArea, dragAndDrop);
		
		report.startStep("Check feedback NOT displayed");
		learningArea.checkNoBottomFeedbackinTest();
		learningArea.checkNoTopFeedbackInTest();
		
		report.startStep("Check tasks indication, status and if it is disabled");
		for (int i = 0; i < 5; i++) {
			if (i<2) { 
				learningArea.checkThatTestTaskMarkCorrect(i);
				learningArea.checkThatTaskIsDone(i);
				learningArea.checkThatTestTaskUnclickable(i);
			}
			else {
				learningArea.checkThatTestTaskMarkWrong(i);
				learningArea.checkThatTaskIsDone(i);
				learningArea.checkThatTestTaskUnclickable(i);
			}
		}
				
		report.startStep("Check Next btn displayed");
		learningArea.checkThatNextButtonIsEnabled();
		
		report.startStep("Enable See Answers option for class - restore default state");
		pageHelper.enableSeeTestAnswersToClass(className, instID);
		
	}
	
	@Test
	@TestCaseParams(testCaseID = { "33611","33626" }, testMultiple = true, skippedBrowsers = {"firefox"}) // Actions API not supported by geckodriver)
	public void testTestResultsAllowOnlyOnce() throws Exception {
	
		learningArea = new NewUxLearningArea(webDriver, testResultService);
		dragAndDrop = new NewUxDragAndDropSection(webDriver, testResultService);
		homePage = new NewUxHomePage(webDriver, testResultService);
		
		String className = configuration.getProperty("classname.locked");
		String instID = configuration.getInstitutionId();
		
		report.startStep("Enable 'Allow Test To Be Taken Only Once' for class");
		pageHelper.enableAllowTestOnlyOnceToClass(className,instID);
				
		report.startStep("Create new student and login");
		studentId = pageHelper.createUSerUsingSP(instID, className);
		String userName = dbService.getUserNameById(studentId, instID);
		NewUXLoginPage loginPage = new NewUXLoginPage(webDriver, testResultService);
		loginPage.loginAsStudent(userName, "12345");
		
		report.startStep("Navigate to Test, submit and check score");
		navigateToTestSubmitAndCheckScore(homePage, learningArea, dragAndDrop);
		
		report.startStep("Press on Step 1 and then on Step 3 again");
		learningArea.clickOnStep(1);
		sleep(1);
		learningArea.clickOnStep(3);
				
		report.startStep("Press on Start Test and verify alert message");
		learningArea.checkThatStartTestBtnDisabled();
		learningArea.validateTestCompletedMessage(alreadyCompletedTestOnce);
						
		report.startStep("Press on Next btn and verify next lesson displayed");
		learningArea.clickOnNextButton();
		learningArea.checkUnitLessonStepNameOnLanding("Unit 1: Meet A Rock Star", "Meet Me!", "Explore");
		
		report.startStep("Navigate to Home Page and back to the same test");
		//learningArea.clickToOpenNavigationBar();
		learningArea.clickOnHomeButton();
		homePage.navigateToRequiredCourseOnHomePage("First Discoveries");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "B1", 1, 1);
		learningArea.clickOnStep(3);
		learningArea.checkThatStartTestBtnDisabled();
		learningArea.validateTestCompletedMessage(alreadyCompletedTestOnce);
		
		report.startStep("Logout - Login and back to the same test");
		learningArea.clickOnLogoutLearningArea();
		sleep(1);
		loginPage.loginAsStudent(userName, "12345");
		homePage.navigateToRequiredCourseOnHomePage("First Discoveries");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "B1", 1, 1);
		learningArea.clickOnStep(3);
		learningArea.checkThatStartTestBtnDisabled();
		learningArea.validateTestCompletedMessage(alreadyCompletedTestOnce);
		
		report.startStep("Disable 'Allow Test To Be Taken Only Once' for class");
		pageHelper.disableAllowTestOnlyOnceToClass(className,instID);
		
	}
	
	@Test
	@TestCaseParams(testCaseID = { "32796" }, skippedBrowsers = {"firefox"}) // Actions API not supported by geckodriver)
	public void testComponentTestReportFromTMS() throws Exception {
				
		report.startStep("Create new user and get student details / test data");
		String institutionId = configuration.getInstitutionId();
		String className = configuration.getProperty("classname.openSpeech");
		String courseCode = "B1";
		studentId = pageHelper.createUSerUsingSP(institutionId, className);
		String userName = dbService.getUserNameById(studentId, institutionId);
		Date date = new Date();
		String currentDate= new SimpleDateFormat("dd/MM/yyyy").format(date);
					
		report.startStep("Login as student");
		loginPage = new NewUXLoginPage(webDriver,testResultService);
		homePage = loginPage.loginAsStudent(userName, "12345");
		learningArea = new NewUxLearningArea(webDriver, testResultService);
		dragAndDrop = new NewUxDragAndDropSection(webDriver, testResultService);
						
		report.startStep("Submit test");
		String actualScore = navigateToTestSubmitAndCheckScore(homePage, learningArea, dragAndDrop);
				
		report.startStep("Logout as student");
		homePage.clickOnLogOut();
		testResultService.assertEquals(false, loginPage.isSubmitButtonEnabled(), "Checking that ED Login Page displayed");
				
		report.startStep("Login as teacher");
		String tName = configuration.getProperty("teacher.username");
		String tID = dbService.getUserIdByUserName(tName, institutionId);
		pageHelper.setUserLoginToNull(tID);
		loginPage.loginAsStudent(tName, "12345");
		sleep(3);
		
		report.startStep("Select Student Progress Report");
		tmsHomePage = new TmsHomePage(webDriver, testResultService);
		tmsHomePage.switchToMainFrame();
		tmsHomePage.clickOnReports();
		sleep(2);
		tmsHomePage.clickOnCourseReports();
		sleep(2);
		tmsHomePage.switchToFormFrame();
		sleep(2);
		tmsHomePage.selectReport("1");
		sleep(1);
		
		report.startStep("Select Class");
		tmsHomePage.selectClass(className,false,false);
		
		report.startStep("Search Student");
		tmsHomePage.clickOnSearch();
		tmsHomePage.findStudentInSearchSection(className, userName);
		sleep(5);
		
		report.startStep("Select course");
		tmsHomePage.switchToMainFrame();
		tmsHomePage.selectCourse(courses[homePage.getCourseIndexByCode(courseCodes, courseCode)]);
//		sleep(5); -- igb 2018.05.17 -- tmp increse sleep interval [Db performance relation]
		sleep(10);
		
		report.startStep("Press on Component Test score");
		String tmsScore = tmsHomePage.openUnitReportAndPressOnTestScore("Meet A Rock Star", "Art");
		testResultService.assertEquals(actualScore.replace("%",""), tmsScore, "Score in TMS does not match to test result");
		sleep(3);
				
		report.startStep("Check test results view of section 1 - First Test");
		webDriver.switchToNewWindow();
		tmsCTR = new TmsCourseTestReport(webDriver, testResultService);
		tmsCTR.verifySectionsTitleComponentTest(userName, currentDate, true, false);
				
		report.startStep("Check Test Result Pages");
		tmsCTR.switchToFirstComponentTestResultFrame();
		checkTestResultsPages(studentId, true);
				
	}
	
	public String navigateToTestSubmitAndCheckScore(NewUxHomePage homePage, NewUxLearningArea learningArea, NewUxDragAndDropSection dragAndDrop) throws Exception { 
				
		report.startStep("Navigate to Basic 1, Meet A Rock Star, Art");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "B1", 1, 1);
		sleep(1);
		
		report.startStep("Click on Test step");
		learningArea.clickOnStep(3);

		report.startStep("Click on Start Button");
		learningArea.clickTheStartTestButon();

		report.startStep("Drag 'New York' to drop target and press Next");
		dragAndDrop.dragAndDropCloseAnswerByTextToId("New York", "1_1");
		learningArea.clickOnNextButton();
		
		report.startStep("Drag 'reporter' to drop target and press Next");
		dragAndDrop.dragAndDropCloseAnswerByTextToId("reporter", "1_1");
		learningArea.clickOnNextButton();

		report.startStep("Select 'true' and press Submit");
		learningArea.selectMultipleAnswer(2);
		learningArea.submitTest(true);

		report.startStep("Check Test Score is 40%");
		String score = learningArea.getTestScore();
		testResultService.assertEquals("40%", score, "Checking Test Score validity");
	
		return score;
		
	}
	
	private String checkTestResultsPages(String studentId, boolean isPreviewModeTMS) throws Exception { 
		
		learningArea = new NewUxLearningArea(webDriver, testResultService);
		dragAndDrop = new NewUxDragAndDropSection(webDriver, testResultService);
		
		String score = learningArea.getTestScore();
		String firstName = dbService.getUserFirstNameByUserId(studentId);
		
		report.startStep("Check user name above test score");
		testResultService.assertEquals(firstName, learningArea.getTestResultUserName(), "Checking user name above test score");
		
		report.startStep("Check feedback text under test score");
		testResultService.assertEquals(feedbackTextUnderTestScore, learningArea.getTestResultFeedbackBottom(), "Checking Test feedback text under score");
		
		report.startStep("Check feedback text above test score");
		testResultService.assertEquals(feedbackTextAboveTestScore, learningArea.getTestResultFeedbackTop(), "Checking Test feedback text above score");
		
		report.startStep("Check tasks indication correct/wrong");
		for (int i = 0; i < 5; i++) {
			if (i<2) learningArea.checkThatTestTaskMarkCorrect(i);
			else learningArea.checkThatTestTaskMarkWrong(i);
		}
		
		report.startStep("Check all tasks done");
		for (int i = 0; i < 5; i++) {
			learningArea.checkThatTaskIsDone(i);
		}
		
		report.startStep("Check Next btn displayed");
		if (isPreviewModeTMS) learningArea.checkThatNextButtonIsNotDisplayedInLA();
		else learningArea.checkThatNextButtonIsEnabled();
		
		report.startStep("Press on Task 1 and check Your Answer / Correct Answer values");
		learningArea.clickOnTask(0);
		sleep(1);
		learningArea.clickOnYourAnswerTab();
		dragAndDrop.checkTileIsPlacedInTestYourAnswerAndCorrect("New York");
		
		learningArea.clickOnCorrectAnswerTab();
		dragAndDrop.checkTileIsPlaced("New York");
		
		
		report.startStep("Press on Task 2 and check Your Answer / Correct Answer values");
		learningArea.clickOnTask(1);
		sleep(1);
		learningArea.clickOnYourAnswerTab();
		dragAndDrop.checkTileIsPlacedInTestYourAnswerAndCorrect("reporter");
		
		learningArea.clickOnCorrectAnswerTab();
		dragAndDrop.checkTileIsPlaced("reporter");
		
		report.startStep("Press on Task 3 and check Your Answer / Correct Answer values");
		learningArea.clickOnTask(2);
		sleep(1);
		learningArea.clickOnYourAnswerTab();
		learningArea.verifyAnswerRadioSelectedWrong(1, 2);
		
		learningArea.clickOnCorrectAnswerTab();
		learningArea.verifyAnswerRadioSelectedCorrect(1, 1);
		
		report.startStep("Press on Task 4 and check Your Answer / Correct Answer values");
		learningArea.clickOnTask(3);
		sleep(1);
		learningArea.clickOnYourAnswerTab();
		dragAndDrop.checkCloseTileIsBackToBank("jazz music");
		dragAndDrop.checkCloseTileIsBackToBank("movie stars");
		dragAndDrop.checkCloseTileIsBackToBank("rock music");
		
		learningArea.clickOnCorrectAnswerTab();
		dragAndDrop.checkTileIsPlaced("rock music");
				
		report.startStep("Press on Task 5 and check Your Answer / Correct Answer values");
		learningArea.clickOnTask(4);
		sleep(1);
		learningArea.clickOnYourAnswerTab();
		learningArea.checkAllAnswersUnselectedMCQ();
		
		learningArea.clickOnCorrectAnswerTab();
		learningArea.verifyAnswerRadioSelectedCorrect(1, 3);
	
		
		return score;
	}
	
	@After
	public void tearDown() throws Exception {
		report.startStep("Set progress to FD item");
		studentService.createSingleProgressRecored(studentId,"20000", "31515", 10, false, 1);	
		super.tearDown();


	}

}
