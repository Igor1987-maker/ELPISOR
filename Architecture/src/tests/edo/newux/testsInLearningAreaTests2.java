package tests.edo.newux;

import Enums.ByTypes;
import Enums.StepProgressBox;
import Enums.TaskTypes;
import Interfaces.TestCaseParams;
import org.junit.After;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import pageObjects.edo.*;
import pageObjects.tms.TmsCourseTestReport;
import pageObjects.tms.TmsHomePage;
import testCategories.edoNewUX.ReleaseTests;
import testCategories.inProgressTests;

import java.text.SimpleDateFormat;
import java.util.Date;


//@Category(AngularLearningArea.class)
public class testsInLearningAreaTests2 extends BasicNewUxTest {

	NewUXLoginPage loginPage;
	NewUxDragAndDropSection dragAndDrop;
	
	NewUxMyProgressPage myProgress;
	TmsHomePage tmsHomePage;
	TmsCourseTestReport tmsCTR;
	NewUxLearningArea2 learningArea2;
	NewUxDragAndDropSection2 dragAndDrop2;

	
	protected static final String feedbackTextUnderTestScore = "Please review your answers.";
	protected static final String feedbackTextBlueCube = "To review your answers, click on the Review button below.\nTo go to the next lesson click on the next arrow.";
	protected static final String alreadyCompletedTestOnce = "You have already completed this test. Please continue to your next assignment.";
	protected static final String testExitAlert = "You haven't submitted your test. If you exit now, your test results will not be recorded.";
	
	
	@Test
	@TestCaseParams(testCaseID = { "22672" })
	public void testStartButtonFunnctionality() throws Exception {

		report.startStep("Get user and login");
		getUserAndLoginNewUXClass();

		report.startStep("Navigate to B1-U2-L2");
		learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "B1", 2, 2);
		
		report.startStep("Go to Test Step and check elements on Start Test Page");
		learningArea2.clickOnStep(3,false);
		learningArea2.checkthatTestButtonIsDisplayed();
		learningArea2.checkThatNextButtonIsNotDisplayedInLA();
		learningArea2.checkThatBackButtonIsNotDisplayedInLA();

		report.startStep("Go to previous step and click next to get to Test again");
		learningArea2.clickOnStep(2);
		learningArea2.clickOnTaskByNumber(6);
		learningArea2.clickOnNextButton();
		learningArea2.checkthatTestButtonIsDisplayed();
		learningArea2.checkThatNextButtonIsNotDisplayedInLA();
		learningArea2.checkThatBackButtonIsNotDisplayedInLA();

		report.startStep("Press Start Test and check Submit btn in task bar");
		learningArea2.clickTheStartTestButon();
		learningArea2.openTaskBar();
		testResultService.assertEquals(true, learningArea2.submitTestInTaskBar.isDisplayed(), "Submit btn in task bar not found or disabled");
		learningArea2.closeTaskBar();
		
		report.startStep("Check Back btn disabled in 1st task of Test");
		learningArea2.checkThatBackButtonIsDisabledInLA();
		learningArea2.clickOnNextButton();
		learningArea2.clickOnBackButton();
		learningArea2.checkThatBackButtonIsDisabledInLA();
		
		report.startStep("Navigate to the last task in test and check Submit btn in Tool Bar");
		learningArea2.clickOnNextButton(4);
		testResultService.assertEquals(true, learningArea2.submitTestInToolBar.isDisplayed(), "Submit btn in tool bar not found or disabled");

	}
	
	@Test
	@TestCaseParams(testCaseID = { "22678" })
	public void testQuitTestWithoutSubmit() throws Exception {

		report.startStep("Create new user and login");
		getUserAndLoginNewUXClass();

		report.startStep("Navigate to B1-U1-L1-S3");
		learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "B1", 1, 1);
		sleep(1);
		learningArea2.clickOnStep(3, false);
		
		report.startStep("Click on Start Test");
		learningArea2.clickTheStartTestButon();
		sleep(1);
		
		report.startStep("Press on Step 1");
		learningArea2.clickOnStep(1);

		report.startStep("Check alert and press Cancel");
		learningArea2.validateQuitPopupMessageinTest(testExitAlert, false);
		
		report.startStep("Check remain in Test");
		testResultService.assertEquals("Test", learningArea2.getStepNameFromHeader(), "Current Step is not Test");
		
		report.startStep("Press on Step 2");
		learningArea2.clickOnStep(2);
		sleep(1);
		
		report.startStep("Check alert and press Continue");
		learningArea2.validateQuitPopupMessageinTest(testExitAlert, true);
		
		report.startStep("Check navigation to Step 2");
		testResultService.assertEquals("Practice", learningArea2.getStepNameFromHeader(), "Current Step is not Practice");
		testResultService.assertEquals("2", learningArea2.getStepNumberFromHeader(), "Current Step is not Step 2");



	}
	
	@Test
	@TestCaseParams(testCaseID = { "22674" })
	public void testSubmitButtonFunctionalityFullNavigation() throws Exception {
		
		report.startStep("Create user and login");
		homePage = createUserAndLoginNewUXClass();
		homePage.closeAllNotifications();
		
		report.startStep("Navigate to B3-U1-L1");
		learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "B3", 1, 1);

		report.startStep("Click on Test Step and Start Test");
		learningArea2.clickOnStep(3, false);
		learningArea2.clickTheStartTestButon();

		report.startStep("Complete the test");
	
		learningArea2.SelectRadioBtn("question-1_answer-1");
		learningArea2.clickOnNextButton();
		
		learningArea2.SelectRadioBtn("question-1_answer-1");
		learningArea2.clickOnNextButton();
	
		learningArea2.SelectRadioBtn("question-1_answer-1");
		learningArea2.clickOnNextButton();
		
		learningArea2.SelectRadioBtn("question-1_answer-1");
		learningArea2.clickOnNextButton();
		
		learningArea2.SelectRadioBtn("question-1_answer-1");
		sleep (3);
		
		report.startStep("Submit test by Submit btn. in task bar");
		learningArea2.submitTest(false);

		report.startStep("Check test result displayed");
		learningArea2.checkThatTestWasSubmitted();

	}
	
	@Test
	@TestCaseParams(testCaseID = { "22674" })
	public void testSubmitButtonFunctionalityInLastTask() throws Exception {
		
		report.startStep("Create user and login");
		createUserAndLoginNewUXClass();
		pageHelper.skipOptin();
		homePage.closeAllNotifications();
		pageHelper.skipOnBoardingHP();
        String [] answer = {"question-1_answer-1","question-1_answer-2","question-1_answer-3","question-1_answer-4"};
		report.startStep("Navigate to B3-U1-L1");
		learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "B3", 1, 1);

		report.startStep("Click on Test Step and Start Test");
		learningArea2.clickOnStep(3, false);
		learningArea2.clickTheStartTestButon();

		report.startStep("Complete the test");
		learningArea2.SelectRadioBtn(answer[1]);
		learningArea2.clickOnNextButton();
		learningArea2.SelectRadioBtn(answer[2]);
		learningArea2.clickOnNextButton();
		learningArea2.SelectRadioBtn(answer[0]);
		learningArea2.clickOnNextButton();
		learningArea2.SelectRadioBtn(answer[3]);
		learningArea2.clickOnNextButton();
		learningArea2.SelectRadioBtn(answer[0]);
		
		report.startStep("Submit test by Submit btn. in tool bar");
		learningArea2.submitTestInLastTask(false);

		report.startStep("Check test result displayed");
		learningArea2.checkThatTestWasSubmitted();

	}
		
	@Test
	@TestCaseParams(testCaseID = { "52887" })
	public void testCloseDDLessonAndStepList() throws Exception {
		
	report.startStep("Init test data");
		String expectedLessonName;
		String currentLessonName;
		String expectedStepName;
		String currentStepName;
		
	report.startStep("Get user and login");
		getUserAndLoginNewUXClass();
		
	report.startStep("Check lesson close DD");
		learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "B1", 2, 2);
		expectedLessonName  = learningArea2.getLessonNameFromHeader();
		learningArea2.lessonListOpenButton.click();
		sleep(2);
		learningArea2.clickOnLessonByNumber(2);
		currentLessonName = learningArea2.getLessonNameFromHeader();
		testResultService.assertEquals(expectedLessonName, currentLessonName, "Lesson name not found/do not match");
		
	report.startStep("Check step close DD");
		expectedStepName  = learningArea2.getStepNameFromHeader();
		learningArea2.stepListOpenButton.click();
		sleep(2);
		learningArea2.clickOnStep(1);
		currentStepName = learningArea2.getStepNameFromHeader();
		testResultService.assertEquals(expectedStepName, currentStepName, "Step name not found/do not match");
		
		
	}
	
	@Test
	@TestCaseParams(testCaseID = { "44431" })
	public void testMoveToNextLessonAfterSubmit() throws Exception {

		report.startStep("Get user and login");
		getUserAndLoginNewUXClass();
		
		report.startStep("Navigate to B1-U2-L2");
		learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "B1", 2, 2);
		sleep(2);
		
		report.startStep("Navigate to Test and press Start");
		learningArea2.clickOnStep(3, false);
		learningArea2.clickTheStartTestButon();
		sleep(1);

		report.startStep("Navigate to Task 3 and click on Submit");
		learningArea2.clickOnTaskByNumber(3);
		learningArea2.submitTest(true);
		webDriver.switchToTopMostFrame();
		sleep(1);

		report.startStep("Click on Next button and check landing on next lesson");
		learningArea2.clickOnNextButton();
		sleep(3);
		String nextLesson = learningArea2.getLessonNameFromHeader();
		testResultService.assertEquals("No Parking", nextLesson, "Lesson name is not correct");

	}
	
	@Test
	@TestCaseParams(testCaseID = { "44031" })
	public void testMoveToPrevStepAfterSubmit() throws Exception {

		report.startStep("Get user and login");
		getUserAndLoginNewUXClass();
		
		report.startStep("Navigate to B1-U2-L2");
		learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "B1", 2, 2);
		sleep(2);
		String currentLesson = learningArea2.getLessonNameFromHeader();
				
		report.startStep("Navigate to Test and press Start");
		learningArea2.clickOnStep(3, false);
		learningArea2.clickTheStartTestButon();
		sleep(1);
		int testStepNum = Integer.valueOf(learningArea2.getStepNumberFromHeader());

		report.startStep("Navigate to Task 3 and click on Submit");
		learningArea2.clickOnTaskByNumber(3);
		learningArea2.submitTest(true);
		webDriver.switchToTopMostFrame();
		sleep(1);

		report.startStep("Click on Back button and check landing on prev step");
		learningArea2.clickOnBackButton();
		sleep(3);
		String lessonNameAfterBack = learningArea2.getLessonNameFromHeader();
		int stepNumAfterBack = Integer.valueOf(learningArea2.getStepNumberFromHeader());
		testResultService.assertEquals(currentLesson, lessonNameAfterBack, "Lesson Name after Back is not correct");
		testResultService.assertEquals(testStepNum-1, stepNumAfterBack, "Step Number after Back is not correct");

	}

	@Test
	@TestCaseParams(testCaseID = { "22682","40255", "44071" }, skippedBrowsers = {"firefox"}) // Actions API not supported by geckodriver)
	public void testTestResultsPageAndWidgets() throws Exception {
		
		report.startStep("Init test data");
		int courseIndex = 1; // Basic 1
		int unitNumber = 1;
		int lessonNumber = 1;
		
		report.startStep("Create user and login");
		createUserAndLoginNewUXClass();
		homePage.skipNotificationWindow();
		homePage.waitHomePageloadedFully();
		
		report.startStep("Navigate to Test, Submit and check score");
		String lastScore = navigateToTestSubmitAndCheckScore();
	
		report.startStep("Open Step List and check Test Result updated");
		learningArea2.openStepsList();
		learningArea2.checkProgressIndicationInStepList(3, StepProgressBox.empty, true, lastScore);
		learningArea2.closeStepsList();
		
		report.startStep("Check Test Results & Answers Preview Pages");
		checkTestResultsPages(studentId, false);
							
		report.startStep("Go To Home Page and check Average Test Score");
		//learningArea2.clickToOpenNavigationBar();
		sleep(2);
		learningArea2.clickOnHomeButton();

		homePage.waitHomePageloadedFully();
		
		testResultService.assertEquals(lastScore.replace("%", ""), homePage.getScoreWidgetValue(), "Checking Test Score in Student widget on Home Page");
					
		report.startStep("Open My Progress page and check FIRST test score displayed");
		myProgress = homePage.clickOnMyProgress();
		myProgress.waitForPageToLoad();
		
		webDriver.scrollToTopOfPage();
		
		myProgress.clickToOpenUnitLessonsProgress(unitNumber);
		sleep(1);
		String actLastScore = myProgress.getLessonLastTestScore(lessonNumber);
		testResultService.assertEquals(lastScore, actLastScore, "First Test Score in My Progress is wrong");
		
		report.startStep("Submit test again and check LAST test score displayed");
		studentService.submitTestsForCourse(studentId, courses[courseIndex], true);
		webDriver.refresh();
		sleep(1);
		//webDriver.refresh();
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
			
		String className = configuration.getProperty("classname.locked");
		String instID = configuration.getInstitutionId();
		
		report.startStep("Disable See Answers option for class");
		pageHelper.disableSeeTestAnswersToClass(className,instID);
						
		try {
			report.startStep("Create new student and login");
			studentId = pageHelper.createUSerUsingSP(instID, className);
			String userName = dbService.getUserNameById(studentId, instID);
			NewUXLoginPage loginPage = new NewUXLoginPage(webDriver, testResultService);
			homePage = loginPage.loginAsStudent(userName, "12345");
			
			report.startStep("Navigate to Test, submit and check score");
			navigateToTestSubmitAndCheckScore();
			
			learningArea2 = new NewUxLearningArea2(webDriver, testResultService);
			
			report.startStep("Check feedback NOT displayed");
			learningArea2.checkNoBottomFeedbackinTest();
			learningArea2.checkNoTopFeedbackInTest();
			
			report.startStep("Check Test Review btn not displayed");
			learningArea2.checkThatReviewTestButtonIsNotDisplayed();
						
			report.startStep("Check Next btn displayed");
			testResultService.assertEquals(true, learningArea2.nextBtn.isDisplayed(), "Next btn disabled or not found");
			
		} catch (Exception e) {
			testResultService.addFailTest(e.toString(), false, true);
		} finally {
			report.startStep("Enable See Answers option for class - restore default state");
			pageHelper.enableSeeTestAnswersToClass(className, instID);
		}
	}
	
	@Test
	@TestCaseParams(testCaseID = { "33611","33626" }, testMultiple = true, skippedBrowsers = {"firefox"}) // Actions API not supported by geckodriver)
	public void testTestResultsAllowOnlyOnce() throws Exception {
			
		String className = configuration.getProperty("classname.locked");
		String instID = configuration.getInstitutionId();
		
		report.startStep("Enable 'Allow Test To Be Taken Only Once' for class");
		pageHelper.enableAllowTestOnlyOnceToClass(className,instID);
				
		try {
			report.startStep("Create new student and login");
			studentId = pageHelper.createUSerUsingSP(instID, className);
			String userName = dbService.getUserNameById(studentId, instID);
			NewUXLoginPage loginPage = new NewUXLoginPage(webDriver, testResultService);
			homePage = loginPage.loginAsStudent(userName, "12345");
			
			report.startStep("Navigate to Test, submit and check score");
			navigateToTestSubmitAndCheckScore();
			
			report.startStep("Press on Step 1 and then on Step 3 again");
			learningArea2.clickOnStep(1);
			sleep(1);
			learningArea2.clickOnStep(3, false);
					
			report.startStep("Press on Start Test and verify alert message");
			learningArea2.checkThatStartTestBtnDisabled();
			learningArea2.validateTestCompletedMessage(alreadyCompletedTestOnce);
							
			report.startStep("Navigate to Next lesson and verify next lesson displayed");
			learningArea2.openLessonsList();
			learningArea2.clickOnLessonByNumber(2);
			sleep(1);
			learningArea2.checkUnitLessonStepNameOnLanding("Unit 1: Meet A Rock Star", "Meet Me!", "Explore");
			
			report.startStep("Navigate to Home Page and back to the same test, and verify Start btn / alert message");
			//learningArea2.clickToOpenNavigationBar();
			sleep(1);
			learningArea2.clickOnHomeButton();
			homePage.waitHomePageloaded();
			homePage.navigateToCourseUnitAndLesson(courseCodes, "B1", 1, 1);
			learningArea2.clickOnStep(3, false);
			learningArea2.checkThatStartTestBtnDisabled();
			learningArea2.validateTestCompletedMessage(alreadyCompletedTestOnce);
			
			report.startStep("Logout - Login and back to the same test, and verify Start btn / alert message");
			learningArea2.clickOnLogoutLearningArea();
			sleep(1);
			homePage = loginPage.loginAsStudent(userName, "12345");
			//homePage.navigateToRequiredCourseOnHomePage("First Discoveries");
			homePage.navigateToCourseUnitAndLesson(courseCodes, "B1", 1, 1);
			learningArea2.clickOnStep(3, false);
			learningArea2.checkThatStartTestBtnDisabled();
			learningArea2.validateTestCompletedMessage(alreadyCompletedTestOnce);
			
		} catch (Exception e) {
			testResultService.addFailTest(e.toString(), false, true);
		} finally {
			report.startStep("Disable 'Allow Test To Be Taken Only Once' for class");
			pageHelper.disableAllowTestOnlyOnceToClass(className,instID);
		}
				
		
	}
    @Category(inProgressTests.class)
	@Test
	@TestCaseParams(testCaseID = { "" }, skippedBrowsers = {"firefox"}) // Actions API not supported by geckodriver)
	public void testTimeOnTest() throws Exception {
				
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
								
	report.startStep("Submit test");
		String actualScore = navigateToTestSubmitAndCheckScore();
				
	report.startStep("Logout as student");
		homePage.clickOnLogOut();
		testResultService.assertEquals(false, loginPage.isSubmitButtonEnabled(), "Checking that ED Login Page displayed");
				
	report.startStep("Login as teacher");
		String tName = configuration.getProperty("teacher.username");
		String tID = dbService.getUserIdByUserName(tName, institutionId);
		pageHelper.setUserLoginToNull(tID);
		loginPage.loginAsStudent(tName, "12345");
		sleep(3);
		homePage.closeAllNotifications();
		
	report.startStep("Select Student Progress Report");
		tmsHomePage = new TmsHomePage(webDriver, testResultService);
		tmsHomePage.switchToMainFrame();
		tmsHomePage.clickOnReports();
		sleep(2);
		tmsHomePage.clickOnCourseReports();
		sleep(2);
		tmsHomePage.switchToFormFrame();
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
		sleep(7);
	}

	@Category(ReleaseTests.class)
	@Test
	@TestCaseParams(testCaseID = { "32796, 44411" }, skippedBrowsers = {"firefox"}) // Actions API not supported by geckodriver)
	public void testComponentTestReportFromTMS() throws Exception {


		if (webDriver.getUrl().contains("engdis.com")){
			institutionName = institutionsName[18]; // 18=ed41, 19=ed51, 20=ed31
			pageHelper.initializeData();
			String url = "https://" + BasicNewUxTest.CannonicalDomain;
			pageHelper.closeBrowserAndOpenInCognito(false);
			webDriver.openUrl(url);
		}

	report.startStep("Create new user and get student details / test data");
	
		String className = configuration.getProperty("classname.openSpeech");
		String courseCode = "B1";
		studentId = pageHelper.createUSerUsingSP(institutionId, className);
		String userName = dbService.getUserNameById(studentId, institutionId);
		Date date = new Date();
					
	report.startStep("Login as student");
		loginPage = new NewUXLoginPage(webDriver,testResultService);
		homePage = loginPage.loginAsStudent(userName, "12345");
		homePage.waitHomePageloadedFully();	
		homePage.skipNotificationWindow();
		
	report.startStep("Submit test");
		String actualEdScore = navigateToTestSubmitAndCheckScore();
		String currentDate= new SimpleDateFormat("dd/MM/yyyy").format(date);
				
	report.startStep("Logout as student");
		homePage.clickOnLogOut();
		testResultService.assertEquals(false, loginPage.isSubmitButtonEnabled(), "Checking that ED Login Page displayed");
				
	report.startStep("Login as teacher");
		String tName = configuration.getProperty("teacher.username");
		String tID = dbService.getUserIdByUserName(tName, institutionId);
		pageHelper.setUserLoginToNull(tID);
		loginPage.loginAsStudent(tName, "12345");
		homePage.skipNotificationWindow();
		
	report.startStep("Select Student Progress Report");
		tmsHomePage = new TmsHomePage(webDriver, testResultService);
		tmsHomePage.switchToMainFrame();
		
		tmsHomePage.clickOnReports();
		sleep(2);
		tmsHomePage.clickOnCourseReports();
		sleep(2);
		tmsHomePage.switchToFormFrame();
		tmsHomePage.selectReport("1");
		sleep(1);
		
	report.startStep("Select Class");
		tmsHomePage.selectClass(className,false,false);
		
	report.startStep("Search Student");
		tmsHomePage.clickOnSearch();
		tmsHomePage.findStudentInSearchSection(className, userName);
		sleep(3);
		
	report.startStep("Select course");
		tmsHomePage.switchToMainFrame();
		tmsHomePage.selectCourse(courses[homePage.getCourseIndexByCode(courseCodes, courseCode)]);
		sleep(3);
		
	report.startStep("Press on Component Test score");
		String tmsScore = tmsHomePage.openUnitReportAndPressOnTestScore("Meet A Rock Star", "Art");
		checkTestScore(actualEdScore,tmsScore);
		
	report.startStep("Check test results view of section 1 - First Test");
		webDriver.switchToNewWindow();
		tmsCTR = new TmsCourseTestReport(webDriver, testResultService);
		tmsCTR.verifySectionsTitleComponentTest(userName, currentDate, true, false);
				
	report.startStep("Check Test Result Pages");
		tmsCTR.switchToFirstComponentTestResultFrame();
		checkTestResultsPages(studentId, true);
				
	}
	
	private void checkTestScore(String actualEdScore, String tmsScore) {
		
		try {
			webDriver.waitUntilElementAppears("learning__testResultScoreboard",ByTypes.className,30);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		testResultService.assertEquals(actualEdScore.replace("%",""), tmsScore, "Score in TMS does not match to test result");
	}

	@Test
	@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "" }) 
	public void testComponentTestReportScoreZeroFromTMS() throws Exception {
				
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
								
		report.startStep("Navigate to Basic 1, Meet A Rock Star, Art");
		learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "B1", 1, 1);
		sleep(1);
		
		report.startStep("Click on Test step");
		learningArea2.clickOnStep(3, false);
	
		report.startStep("Click on Start Button");
		learningArea2.clickTheStartTestButon();
		
		report.startStep("Drag 'Los Angeles' to drop target and press Next (Incorrect Answer)");
		dragAndDrop2 = new NewUxDragAndDropSection2(webDriver, testResultService);
		dragAndDrop2.dragAndDropAnswerByTextToTarget("Los Angeles", 1, TaskTypes.Close);
		learningArea2.clickOnNextButton();
		
		report.startStep("Don't Answer Second Question (Empty Answer)");
		learningArea2.clickOnNextButton();

		report.startStep("Select 'false' and press Submit (Incorrect Answer)");
		learningArea2.SelectRadioBtn("question-1_answer-2");
		learningArea2.clickOnNextButton();
		
		report.startStep("Don't Answer Fourth Question (Empty Answer)");
		learningArea2.clickOnNextButton();
		
		report.startStep("Click on Submit");
		learningArea2.submitTest(true);
		
		report.startStep("Check Test Score is 0%");
		String actualScore = learningArea2.getTestScore();
		testResultService.assertEquals("0%", actualScore, "Checking Test Score validity");
				
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
		sleep(7);
		
		report.startStep("Press on Component Test score");
		String tmsScore = tmsHomePage.openUnitReportAndPressOnTestScore("Meet A Rock Star", "Art");
		sleep(3);
		testResultService.assertEquals(actualScore.replace("%",""), tmsScore, "Score in TMS does not match to test result");
		sleep(3);
				
		report.startStep("Check test results view of section 1 - First Test");
		webDriver.switchToNewWindow();
		tmsCTR = new TmsCourseTestReport(webDriver, testResultService);
		tmsCTR.verifySectionsTitleComponentTest(userName, currentDate, true, false);
				
		report.startStep("Check Test Result Pages");
		tmsCTR.switchToFirstComponentTestResultFrame();
		checkTestResultsPagesScoreZero(studentId, true);
				
	}
	
	private String navigateToTestSubmitAndCheckScore() throws Exception { 
				
		report.startStep("Navigate to Basic 1, Meet A Rock Star, Art");
		learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "B1", 1, 1);
		learningArea2.waitToLearningAreaLoaded();
		
		report.startStep("Click on Test step");
		learningArea2.clickOnStep(3, false);

		report.startStep("Click on Start Button");
		learningArea2.clickTheStartTestButon();

		report.startStep("Drag 'New York' to drop target and press Next");
		dragAndDrop2 = new NewUxDragAndDropSection2(webDriver, testResultService);
		dragAndDrop2.dragAndDropAnswerByTextToTarget("New York", 1, TaskTypes.Close);
		learningArea2.clickOnNextButton();
		
		report.startStep("Drag 'reporter' to drop target and press Next");
		dragAndDrop2.dragAndDropAnswerByTextToTarget("reporter", 1, TaskTypes.Close);
		learningArea2.clickOnNextButton();

		report.startStep("Select 'true' and press Submit");
		 learningArea2.SelectRadioBtn("question-1_answer-2");	
		learningArea2.submitTest(true);

		report.startStep("Check Test Score is 40%");
		String score = learningArea2.getTestScore();
		testResultService.assertEquals("40%", score, "Checking Test Score validity");
	
		return score;
		
	}
	
	private String checkTestResultsPages(String studentId, boolean isPreviewModeTMS) throws Exception { 
				
		String score = learningArea2.getTestScore();
		String firstName = dbService.getUserFirstNameByUserId(studentId);
		
		report.startStep("Check user name above test score");
		testResultService.assertEquals(firstName, learningArea2.getTestResultUserName(), "Checking user name above test score");
		
		report.startStep("Check feedback text under test score");
		testResultService.assertEquals(feedbackTextUnderTestScore, learningArea2.getTestResultFeedbackBottom(), "Checking Test feedback text under score");
		
		report.startStep("Check feedback text below test score cube");
		testResultService.assertEquals(feedbackTextBlueCube, learningArea2.getTestResultFeedbackTop(), "Checking Test feedback text below score cube");
		
		report.startStep("Press on Review and open task bar");
		learningArea2.clickOnReviewTestResults();
		learningArea2.openTaskBar();
				
		report.startStep("Check tasks indication correct/wrong");
		for (int i = 0; i < 5; i++) {
			if (i<2) learningArea2.checkTestTaskMark(i+1, true);
			else learningArea2.checkTestTaskMark(i+1, false);
		}
		
		report.startStep("Check all tasks done");
		for (int i = 0; i < 5; i++) {
			learningArea2.checkIfTaskHasDoneState(i+1, true);
		}
		
		learningArea2.closeTaskBar();
		
		report.startStep("Check Your Answer / Correct Answer values");
		sleep(1);
		
		learningArea2.clickOnYourAnswerTab();
		//dragAndDrop2.checkTileIsPlaced("New York", TaskTypes.Close);
		dragAndDrop2.validateSingleDragAndDropAnswerSign("New York", true); 
		
		learningArea2.clickOnCorrectAnswerTab();
		//dragAndDrop2.checkTileIsPlaced("New York", TaskTypes.Close);
		dragAndDrop2.validateSingleDragAndDropAnswerInCorrectTab("New York"); 
		sleep(1);
		
		report.startStep("Press on Task 2 and check Your Answer / Correct Answer values");
		learningArea2.clickOnTaskByNumber(2);
		sleep(1);
		learningArea2.clickOnYourAnswerTab();
		//dragAndDrop2.checkTileIsPlaced("reporter", TaskTypes.Close);
		dragAndDrop2.validateSingleDragAndDropAnswerSign("reporter", true); 
		
		learningArea2.clickOnCorrectAnswerTab();
		//dragAndDrop2.checkTileIsPlaced("reporter", TaskTypes.Close);
		dragAndDrop2.validateSingleDragAndDropAnswerInCorrectTab("reporter"); 
		sleep(1);
		
		report.startStep("Press on Task 3 and check Your Answer / Correct Answer values");
		learningArea2.clickOnTaskByNumber(3);
		sleep(1);
		learningArea2.clickOnYourAnswerTab();
		//learningArea2.CorrectnessXMCQ();
		learningArea2.validateSingleRadioAnswerSign("True", false); 
		
		learningArea2.clickOnCorrectAnswerTab();
		//learningArea2.CorrectnessVMCQ(); 
		learningArea2.validateSingleRadioAnswerSign("True", false); 
		learningArea2.validateSingleRadioAnswerSign("False", true); 
		sleep(1);
		
		report.startStep("Press on Task 4 and check Your Answer / Correct Answer values");
		learningArea2.clickOnTaskByNumber(4);
		sleep(1);
		learningArea2.clickOnYourAnswerTab();
	
		String[] expectedWordsInBank = {"jazz music", "movie stars", "rock music"};
		dragAndDrop2.validateDragAndfDropNotAnswered(expectedWordsInBank); 
		
		//dragAndDrop2.checkTileIsInBank("jazz music");
		//dragAndDrop2.checkTileIsInBank("movie stars");
		//dragAndDrop2.checkTileIsInBank("rock music");
		
		learningArea2.clickOnCorrectAnswerTab();
		
		//dragAndDrop2.checkTileIsPlaced("rock music", TaskTypes.Close);
		dragAndDrop2.validateSingleDragAndDropAnswerInCorrectTab("rock music"); 
		sleep(1);	
		
		report.startStep("Press on Task 5 and check Your Answer / Correct Answer values");
		learningArea2.clickOnTaskByNumber(5);
		sleep(1);
		learningArea2.clickOnYourAnswerTab();
		//learningArea2.checkAllAnswersUnselectedMCQ();
		learningArea2.validateRadioQuestionIsNotAnswered();//
		
		learningArea2.clickOnCorrectAnswerTab();
		//learningArea2.verifyAnswerRadioSelectedCorrect(1, 3);
		learningArea2.verifyAnswerRadioSelectedCorrectNew("At the radio station");
		sleep(1);
		
		return score;
	}
	
	private String checkTestResultsPagesScoreZero(String studentId, boolean isPreviewModeTMS) throws Exception { 
		
		String score = learningArea2.getTestScore();
		String firstName = dbService.getUserFirstNameByUserId(studentId);
		
		report.startStep("Check user name above test score");
		testResultService.assertEquals(firstName, learningArea2.getTestResultUserName(), "Checking user name above test score");
		
		report.startStep("Check feedback text under test score");
		testResultService.assertEquals(feedbackTextUnderTestScore, learningArea2.getTestResultFeedbackBottom(), "Checking Test feedback text under score");
		
		report.startStep("Check feedback text below test score cube");
		testResultService.assertEquals(feedbackTextBlueCube, learningArea2.getTestResultFeedbackTop(), "Checking Test feedback text below score cube");
		
		report.startStep("Press on Review and open task bar");
		learningArea2.clickOnReviewTestResults();
		learningArea2.openTaskBar();
				
		report.startStep("Check tasks indication correct/wrong");
		for (int i = 0; i < 5; i++) {
			learningArea2.checkTestTaskMark(i+1, false);
		}
		
		report.startStep("Check all tasks done");
		for (int i = 0; i < 5; i++) {
			learningArea2.checkIfTaskHasDoneState(i+1, true);
		}
		
		learningArea2.closeTaskBar();
		sleep(1);
		
		report.startStep("Check Your Answer / Correct Answer values");
		learningArea2.clickOnYourAnswerTab();
		//dragAndDrop2.checkTileIsPlaced("Los Angeles", TaskTypes.Close);
		dragAndDrop2.validateSingleDragAndDropAnswerSign("Los Angeles", false); 
		
		learningArea2.clickOnCorrectAnswerTab();
		//dragAndDrop2.checkTileIsPlaced("New York", TaskTypes.Close);
		dragAndDrop2.validateSingleDragAndDropAnswerInCorrectTab("New York"); 
		sleep(1);
		
		report.startStep("Press on Task 2 and check Your Answer / Correct Answer values");
		learningArea2.clickOnTaskByNumber(2);
		sleep(1);
		learningArea2.clickOnYourAnswerTab();
		String[] expectedWordsInBank = {"reporter", "rock star", "singer"};
		dragAndDrop2.validateDragAndfDropNotAnswered(expectedWordsInBank); 
		//dragAndDrop2.checkTileIsInBank("reporter"); 
		//dragAndDrop2.checkTileIsInBank("rock star");
		//dragAndDrop2.checkTileIsInBank("singer");
		
		learningArea2.clickOnCorrectAnswerTab();
		//dragAndDrop2.checkTileIsPlaced("reporter", TaskTypes.Close);
		dragAndDrop2.validateSingleDragAndDropAnswerInCorrectTab("reporter"); 
		sleep(1);
		
		report.startStep("Press on Task 3 and check Your Answer / Correct Answer values");
		learningArea2.clickOnTaskByNumber(3);
		sleep(1);
		learningArea2.clickOnYourAnswerTab();
		//learningArea2.CorrectnessXMCQ(); 
		learningArea2.validateSingleRadioAnswerSign("True", false); 
		
		learningArea2.clickOnCorrectAnswerTab();
		//learningArea2.CorrectnessVMCQ();
		learningArea2.validateSingleRadioAnswerSign("True", false);
		learningArea2.validateSingleRadioAnswerSign("False", true);
		sleep(1);
		
		report.startStep("Press on Task 4 and check Your Answer / Correct Answer values");
		learningArea2.clickOnTaskByNumber(4);
		sleep(1);
		learningArea2.clickOnYourAnswerTab();
		
		String[] expectedWordsInBank2 = {"jazz music", "movie stars", "rock music"};
		dragAndDrop2.validateDragAndfDropNotAnswered(expectedWordsInBank2); 
		
		//dragAndDrop2.checkTileIsInBank("jazz music"); 
		//dragAndDrop2.checkTileIsInBank("movie stars");
		//dragAndDrop2.checkTileIsInBank("rock music");
		
		learningArea2.clickOnCorrectAnswerTab();
		//dragAndDrop2.checkTileIsPlaced("rock music", TaskTypes.Close);
		dragAndDrop2.validateSingleDragAndDropAnswerInCorrectTab("rock music"); 
		sleep(1);	
		
		report.startStep("Press on Task 5 and check Your Answer / Correct Answer values");
		learningArea2.clickOnTaskByNumber(5);
		sleep(1);
		learningArea2.clickOnYourAnswerTab();
		//learningArea2.checkAllAnswersUnselectedMCQ();// need to change function
		learningArea2.validateRadioQuestionIsNotAnswered();
		
		learningArea2.clickOnCorrectAnswerTab();
		//learningArea2.verifyAnswerRadioSelectedCorrect(1, 3);
		learningArea2.verifyAnswerRadioSelectedCorrectNew("At the radio station");
		sleep(1);
		
		return score;
	}
	
	
	@After
	public void tearDown() throws Exception {
		//report.startStep("Set progress to FD item");
		//studentService.createSingleProgressRecored(studentId,"20000", "31515", 10, false, 1);	
		super.tearDown();


	}

}
