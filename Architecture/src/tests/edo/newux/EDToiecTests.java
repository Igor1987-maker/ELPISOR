package tests.edo.newux;

import Enums.ByTypes;
import Interfaces.TestCaseParams;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import pageObjects.FirefoxLoginPage;
import pageObjects.edo.*;
import pageObjects.toeic.ToeicLearningAreaAndProgressPage;
import pageObjects.toeic.toeicQuestionPage;
import pageObjects.toeic.toeicResultsPage;
import pageObjects.toeic.toeicStartPage;
import pagesObjects.oms.LoginPage;
import services.DbService;
import services.PageHelperService;
import testCategories.edoNewUX.ReleaseTests;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class EDToiecTests extends BasicNewUxTest {


	NewUXLoginPage loginPage;
	NewUxClassicTestPage classicTest;
	NewUxAssessmentsPage testsPage;
	NewUxLearningArea2 learningArea2;
	NewUxUnitObjectivesPage unitObjPage;
	ToeicLearningAreaAndProgressPage makeProgressInCoursePage;
	FirefoxLoginPage firefoxPage;
	private static final String REPORTS_FOLDER = "courseErrorsReports";
	List<String[]> errorList;
	List<String[]> units;
	String unitName;
	List<String[]> components;
	String [] expectedLessonName;
	int totalLessonsCompleted = 0;
	String actCompletion = "";
	String progressOfUnit;
	JSONObject jsonObj = new JSONObject();
	
	@Before
	public void setup() throws Exception {
		super.setup();
		// BETA users
		//001000567293	dd2452
		//001000567294	68a165

		report.startStep("Open Toiec");
		institutionName = institutionsName[11];
		pageHelper.initializeData();
		webDriver.deleteCookiesAndCache();
		
		pageHelper.restartBrowser();
		
		
		/*
		 * webDriver.quitBrowser(); 
		 * webDriver.init(); 
		 * webDriver.maximize();
		 */
		webDriver.openUrl(PageHelperService.toeicUrl); 
		
		loginPage = new NewUXLoginPage(webDriver,testResultService);
		loginPage.waitEDToeicLoginLoaded();
	
		homePage = getToeicUserAndLogin(PageHelperService.userFilePath);
		pageHelper.skipOptin();
		// BETA
		//homePage = loginPage.loginAsStudent("001000567293", "dd2452");
		classicTest = new NewUxClassicTestPage(webDriver,testResultService);
		
		report.startStep("Wait until loading message dissappers (if its displayed)");
		homePage.waitUntilLoadingMessageIsOver();
		//sleep(2);
		//webDriver.waitUntilElementAppears("//div[@class='notificationsCenter_hideSlide']", 5);
			
		report.startStep("Close All Notifications");	
		homePage.closeAllNotifications();

		report.startStep("Skip the Walkthrough");
		pageHelper.skipOnBoardingHP();
		
		homePage.waitHomePageloadedFully();
		sleep(1);
		
		report.startStep("Close modal pop up (if it appears)");
		homePage.closeModalPopUp();
		
		NewUxMyProfile myProfile = new NewUxMyProfile(webDriver, testResultService);
		myProfile.setEnglishLanguageInMyProfile();
	}
	
	@Test
	//@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "72175", "56848" })
	public void testAssessmentsPageTOEICTestResults() throws Exception {
	
		report.startStep("Init test data");	
			String readingScore = "5-15"; // 60-95
			String listeningScore = "5-60"; // 85-145
			//String distributorId = "1";
			String courseName = coursesNamesTOEIC[0];
			
		report.startStep("Open Assessments Page");
			testsPage = homePage.openAssessmentsPage(false);
			
		//report.startStep("Open First Section");
			//testsPage.clickOnArrowToOpenSection("1");
				
		report.startStep("Check Counter value in The First Section is the same one as in The DB");
		//String disTributorTestCount = dbService.getToeicDistributorTestCount(distributorId); // failing with error: Invalid object name 'DistributorTestBank'
			testsPage.checkItemsCounterBySection("1", "6");
			
		report.startStep("Click Start Test on the First Test");
			String clickedTestName = testsPage.clickStartTest(1, 1);
			sleep(1);
		
		// change focus to the new opened pop up
			webDriver.switchToPopup();
			
		// Initialize toeic start page
			toeicStartPage toeicStartpage = new toeicStartPage(webDriver,testResultService);
			toeicStartpage.waitForPageToLoad();
			
		report.startStep("Validate the URL is Correct");
			webDriver.validateURL("/Runtime/Test.aspx"); 
			//sleep(1);
			
		report.startStep("Press Start New Test if 'Resume Test' Option is Present");
			toeicStartpage.pressStartNewTestInResumePopUp();
		
		report.startStep("Validate the Title is the Name of the First Test");
			toeicStartpage.validateTitle(clickedTestName);
			
		report.startStep("Click Go Button");
			toeicStartpage.clickGoButton();
			
		report.startStep("Validate 'Test Your Sound' Button is Clickable");
			toeicStartpage.checkTestSoundButtonIsClickable();
		
		report.startStep("Validate the Welcome Text is Not Null");
			toeicStartpage.validateTheWelcomeTextIsNotNull();
			
		report.startStep("Click Start");
			toeicStartpage.clickStart();
			
		// Initialize toeic questions page
			toeicQuestionPage toeicQuestionpage = new toeicQuestionPage(webDriver,testResultService);
			
		report.startStep("Click Next");
			toeicQuestionpage.clickNext();
			sleep(2);
			
		// Answer All Questions in Section 1
			ArrayList<String> finalAnswersArray = toeicQuestionpage.answerQuestionsInSeveralSections(1, "files/TOEIC/finalTestAnswers.csv");
			
		report.startStep("Click Submit");
			toeicQuestionpage.submit(false);
			sleep(1);
			toeicQuestionpage.submit(true);
			sleep(2);
			
		// Change focus to main window
			webDriver.switchToMainWindow();
			sleep(1);
			
		report.startStep("Open Assessments Page");
			testsPage = homePage.openAssessmentsPage(false);
			
		report.startStep("Check Counter value in The First section is the same one as in The DB");
			//String disTributorTestCount = dbService.getToeicDistributorTestCount(distributorId); // failing with error: Invalid object name 'DistributorTestBank'
			testsPage.checkItemsCounterBySection("1", "6");
		
		report.startStep("Close First Section");
			testsPage.clickOnArrowToOpenSection("1"); 
			//sleep(1);
			
		report.startStep("Check Counter Value in The Second Section is 1");
			testsPage.checkItemsCounterBySection("2", "1");
			
		report.startStep("Open Tests Results Section");
			testsPage.clickOnArrowToOpenSection("2"); 
			//sleep(1);
				
		report.startStep("Verify '" + clickedTestName + "' is Displayed in Tests Results Section");
			testsPage.checkTestDisplayedInSectionByTestName(clickedTestName, "2", "1"); // Might need a new function for TOEIC tests or changing this one
									
		report.startStep("Check Date of Submission");
			String currentDate = webDriver.getCurrentDate();
			testsPage.checkSubmissionDateForPastTests("1",currentDate); // New function when the test wasn't done today
				
		report.startStep("Check Score in Test Results"); 
			//testsPage.checkScoreForTOEICTest("1", readingScore, listeningScore); // not working since the canvas properties does not hold any text
			testsPage.checkScoreForToeicTestIsDisplayed();
			
		report.startStep("Check 'Full Report' and 'View Results' Buttons Are Available");
			boolean isFullReportButtonDisplayed = testsPage.checkViewFullReportDisplayed("1");
			boolean isResultButtonDisplayed = testsPage.checkViewResultsDisplayed("1");
			
			if (isFullReportButtonDisplayed) {
				
				report.startStep("Click 'View Full Report' Button");
					testsPage.clickViewFullReport(1);
					sleep(3);
					
				// Initialize full report page and check answers
					AssessmentsFullReportPage assessmentsFullReportPage = new AssessmentsFullReportPage(webDriver,testResultService);
					assessmentsFullReportPage.checkAnswersInViewFullReportPopUp(clickedTestName, currentDate, readingScore, listeningScore, finalAnswersArray);
					
				report.startStep("Close 'Full Report' Page");
					testsPage.close();
					sleep(1);
			} else {
				testResultService.addFailTest("'View Full Report' Button is Not Displayed (skipping test)");
			}
			
			if (isResultButtonDisplayed) {
				
				report.startStep("Open Assessments Page");
					testsPage = homePage.openAssessmentsPage(false);
				
				// click on view results (directly)
				report.startStep("Close First Section");
					testsPage.clickOnArrowToOpenSection("1"); 
					//sleep(1);
					
				report.startStep("Open Tests Results Section");
					testsPage.clickOnArrowToOpenSection("2"); 
					//sleep(1);
					
				report.startStep("Click 'View Results' Button");
					testsPage.clickViewResults(1);
					//sleep(1);
				
					toeicResultsPage toeicResultspage = new toeicResultsPage(webDriver,testResultService);
					toeicResultspage.checkAnswersInViewResultsPage(finalAnswersArray);
					
			} else {
				testResultService.addFailTest("'View Results' Button is Not Displayed (skipping test)");
			}
			
		// Initialize my progress page
			NewUxMyProgressPage myProgress = new NewUxMyProgressPage(webDriver, testResultService);
					
		report.startStep("Enter "+courseName+" My Progress Section");	
			homePage.navigateToRequiredCourseOnHomePage(courseName);
			sleep(2);
			myProgress = homePage.clickOnMyProgress();
			sleep(1);
			myProgress.waitForPageToLoad();
			sleep(1);
			
		report.startStep("Check Test Name is Displayed Correctly in My Progress Section");
			myProgress.checkTestNameInMyProgressPage(clickedTestName);
			
		report.startStep("Check Score in Test Results"); 
			//testsPage.checkScoreForTOEICTest("1", readingScore, listeningScore); // not working since the canvas properties does not hold any text
			testsPage.checkScoreForToeicTestIsDisplayed();
			sleep(1);
			
		//report.startStep("Scroll down to buttons"); 
		//	myProgress.scrollDownToReportButtons();
		
		report.startStep("Check 'Full Report' and 'View Results' Buttons are Displayed in My Progress Section");
			isFullReportButtonDisplayed = false;
			isResultButtonDisplayed = false;
			isFullReportButtonDisplayed = myProgress.checkViewFullReportDisplayedInMyProgressPage();
			isResultButtonDisplayed = myProgress.checkViewResultsDisplayedInMyProgressPage();
			
			if (isFullReportButtonDisplayed) {
				
				report.startStep("Click 'View Full Report' Button in My Progress Section");
					myProgress.clickViewFullReportInMyProgressPage();
				//	sleep(3);
				
				// Initialize report page and check URL and answers
					AssessmentsFullReportPage assessmentsFullReportPage = new AssessmentsFullReportPage(webDriver,testResultService);
					assessmentsFullReportPage.checkAnswersInViewFullReportPopUp(clickedTestName, currentDate, readingScore, listeningScore, finalAnswersArray);
			//		sleep(2);
					
				report.startStep("Close 'Full Report' Page");
					testsPage.close();
					sleep(1);
			} else {
				testResultService.addFailTest("'View Full Report' Button is Not Displayed in My Progress Page (skipping test)");
			}
			
		//	sleep(2);
			
			if (isResultButtonDisplayed) {
				report.startStep("Click 'View Results' Button in My Progress Section");
					myProgress.clickViewResultsInMyProgressPage();
					//sleep(2);
					
					toeicResultsPage toeicResultspage = new toeicResultsPage(webDriver,testResultService);
					toeicResultspage.checkAnswersInViewResultsPage(finalAnswersArray);
			} else {
				testResultService.addFailTest("'View Results' Button is Not Displayed in My Progress Page (skipping test)");
			}
			
		report.startStep("Log Out");
			myProgress.clickOnLogOut();			
	}
	
	/*private void checkTestCookieFalse() throws Exception {
		// TODO Auto-generated method stub
		String cookieTest= webDriver.getCookie("Personal");
		int i = 0;
		while (cookieTest.contains("InTest*true") && i <= 23){
			sleep(3);
			cookieTest = webDriver.getCookie("Personal");
			i = i+1;
		}
		
	}*/
	
	@Test
	@TestCaseParams(testCaseID = { "56485", "56491" })
	public void testUnitObjectivesFromHomePageAndLearningArea() throws Exception {
		
		report.startStep("Init test Data");
			String courseName = coursesNamesTOEIC[0];
			String courseCode = courseCodesTOEIC[0];
			int firstUnitToPress = 2;
			int secondUnitToPress = 5;
			unitObjPage = new NewUxUnitObjectivesPage(webDriver, testResultService);
			
			//sleep(3);
		report.startStep("Navigate to " + courseName);
			homePage.navigateToRequiredCourseOnHomePage(courseName);
			sleep(2);
			
		report.startStep("Open "+firstUnitToPress+"nd unit's objectives");
			String objectiveName = homePage.openUnitObjectiveById(firstUnitToPress);
			sleep(2);
			unitObjPage.switchToUnitOjbjectiveFrame();
			
		report.startStep("Check the names are displyed correctly in Unit Objectives Page for Units ("+firstUnitToPress+") and ("+secondUnitToPress+")");
			unitObjPage.checkUnitObjectivesPage(firstUnitToPress, secondUnitToPress, objectiveName);
		
		report.startStep("Close the Unit Objectives Page");
			unitObjPage.close();
			
		report.startStep("Navigate to " + courseName + ", unit " + firstUnitToPress + " and lesson 5");
			learningArea2 = homePage.navigateToCourseUnitLessonLA2TOEIC(courseCodesTOEIC, courseCode, firstUnitToPress, 5, false, courseName);
			learningArea2.waitToLearningAreaLoaded();
			
		report.startStep("Open Unit Objective from learning Area");	
			unitObjPage = learningArea2.openUnitObjectiveInLA(); //open unit objectives once implemented
			sleep(2);
			unitObjPage.switchToUnitOjbjectiveFrame();
			
		report.startStep("Check the names are displyed correctly in Unit Objectives Page for Units ("+firstUnitToPress+") and ("+secondUnitToPress+")");
			unitObjPage.checkUnitObjectivesPage(firstUnitToPress, secondUnitToPress, objectiveName);
			
		report.startStep("Close the Unit Objectives Page");
			unitObjPage.close();
			sleep(2);
// add here more progress			
		report.startStep("Return to Home Page");
			learningArea2.clickOnHomeButton();
			homePage.waitHomePageloadedFully();
			
		report.startStep("Navigate to " + courseName);
			homePage.navigateToRequiredCourseOnHomePage(courseName);
			homePage.waitHomePageloadedFully();
			
		report.startStep("Open "+secondUnitToPress+"nd unit's objectives");
			objectiveName = homePage.openUnitObjectiveById(secondUnitToPress);
			sleep(2);
			unitObjPage.switchToUnitOjbjectiveFrame();
			
		report.startStep("Verify Selected Unit Name (on the left side bar) and the Title");
			unitObjPage.checkUnitNameIsDisplayedCorrectly(secondUnitToPress, objectiveName);
			
		report.startStep("Close the Units page");
			unitObjPage.close();
			sleep(2);
			
		report.startStep("Navigate to " + courseName + ", unit " + secondUnitToPress + " and lesson 5");
			learningArea2 = homePage.navigateToCourseUnitLessonLA2TOEIC(courseCodesTOEIC, courseCode, secondUnitToPress, 5, false, courseName);
			learningArea2.waitToLearningAreaLoaded();
		
		report.startStep("Open Unit Objective from learning Area");	
			unitObjPage = learningArea2.openUnitObjectiveInLA(); //open unit objectives once implemented
			sleep(2);
			unitObjPage.switchToUnitOjbjectiveFrame();
			
		report.startStep("Verify Selected Unit Name (on the left side bar) and the Title");
			unitObjPage.checkUnitNameIsDisplayedCorrectly(secondUnitToPress, objectiveName);
			
		report.startStep("Close the Units page");
			unitObjPage.close();
			sleep(2);
			
		report.startStep("Click Log Out");
			homePage.clickOnLogOut();
	}

	@Test
	@TestCaseParams(testCaseID = { "" })
	public void testUnitTestFullCycle() throws Exception {
	
	
		
		// Right now the assumption is that we can't delete user data from TOEIC.
		// This causes us to get the same test result on every run.
		// You can add checking last score and then answering incorrectly to see score change.
		// This test also assumes test was done on TOEIC at least once.
		// Change relevant parts accordingly.
		
		report.startStep("Init test data");	
			int numOfSections = numOfSectionsAnsweredTOEIC[3];
			String expectedTestResult = testScoreTOEIC[3]; // The score depends on the num of sections- if more sections will be answered- the score will be higher
			String courseName = coursesNamesTOEIC[0];
			int unitID = 1;
			
		report.startStep("Open Unit Test of Course " + courseName + ", Unit 1");
			homePage.openUnitTestForCourse(unitID,courseName); // First param is unit.
			String testName = courseName + " Unit Test - Unit " + unitID;
			//sleep(3);
			
		// Initialize toeic start page
			toeicStartPage toeicStartpage = new toeicStartPage(webDriver,testResultService);
			toeicStartpage.waitForPageToLoad();
		
		report.startStep("Validate the URL is Correct");
			webDriver.validateURL("/Runtime/Test.aspx"); 
			//sleep(1);

		report.startStep("Press Start New Test if 'Resume Test' Option is Present");
			toeicStartpage.pressStartNewTestInResumePopUp();
		
		report.startStep("Validate the Title is the Name of the First Test");
			toeicStartpage.validateTitle(testName);
			
		report.startStep("Validate 'Test Your Sound' Button is Clickable");
			toeicStartpage.checkTestSoundButtonIsClickable();
		
		report.startStep("Validate the Welcome Text is Not Null");
			toeicStartpage.validateTheWelcomeTextIsNotNull();
			
		report.startStep("Click Start");
			toeicStartpage.clickStart();
			sleep(1);
			
		// Initialize toeic questions page
			toeicQuestionPage toeicQuestionpage = new toeicQuestionPage(webDriver,testResultService);
			
		report.startStep("Click Next");
			toeicQuestionpage.clickNext();
			
		// Answer All Questions until Section "+numOfSections+" (including)
		report.startStep("Answer Questions");
			String pathToAnswers = "files/TOEIC/UnitTestAnswers2.csv";		
				if(pageHelper.linkED.contains("ednewb.engdis.com")) {
					pathToAnswers = "files/TOEIC/UnitTestAnswersBeta.csv";
				}
			ArrayList<String> finalAnswersArray = toeicQuestionpage.answerQuestionsInSeveralSections(numOfSections, pathToAnswers);
			
		report.startStep("Click Submit");
			sleep(2);
			toeicQuestionpage.submit(true);
			sleep(1);
			
		report.startStep("Validate the Score is Displayed Correctly");
			toeicQuestionpage.validateScoreIsDisplayedCorrectly(expectedTestResult);
			
		report.startStep("Close The Test");
			toeicQuestionpage.clickCloseButton();
			webDriver.switchToMainWindow();
		
/*		report.startStep("Open Unit: "+unitID+", Scroll down, Validate 'View Results' button is Displayed and Click it");
			homePage.openUnitAndScrollDown(courseName, unitID);
			sleep(2);
			boolean clickedViewResultsButton = homePage.clickViewResultButtonInsideUnit();
			//sleep(2); //add smart wait to test page
			if (clickedViewResultsButton) {
			
				// Initialize results page and check answers
					toeicResultsPage toeicResultspage = new toeicResultsPage(webDriver,testResultService);
					toeicResultspage.checkAnswersInViewResultsPage(finalAnswersArray);
			} else {
				testResultService.addFailTest("'View Results' button is not displayed (skipping test)");
			}
*/
		// Initialize my progress page
			NewUxMyProgressPage myProgress = new NewUxMyProgressPage(webDriver, testResultService);
		
		report.startStep("Enter " + courseName + " My Progress Section");	
			homePage.navigateToRequiredCourseOnHomePage(courseName);
			//sleep(2);
			myProgress = homePage.clickOnMyProgress();
			myProgress.waitForPageToLoad();
		
		report.startStep("Verify Unit Test Score is Displayed in My Progress Page");
			myProgress.clickToOpenUnitLessonsProgress(1);
			testResultService.assertEquals(expectedTestResult.toString(), myProgress.checkUnitTestScoreOfSpecificUnit(unitID),"Scores aren't identical");
			sleep(2);
			
		report.startStep("Click 'View Results' Button is My Progress Page For Specific Unit");
			myProgress.clickViewResultsInMyProgressPageForSpecificUnit();
			
		// Initialize results page and check answers
		//	toeicResultsPage toeicResultspage = new toeicResultsPage(webDriver,testResultService);
		//	toeicResultspage.checkAnswersInViewResultsPage(finalAnswersArray);
			
		report.startStep("Click 'Take Again' Button is My Progress Page For Specific Unit");
			myProgress.scrollToTopOfMyProgressPage();
			myProgress.clickToOpenUnitLessonsProgress(1);
			myProgress.clickTakeTestAgainInMyProgressPageForSpecificUnit();
			
		// Validate URl, name, click Start and close the window
			toeicStartpage.validateDetailsInToeicStartPage(testName);
			
		report.startStep("Click 'Start' Button is My Progress Page For Specific Unit");
			myProgress.scrollToTopOfMyProgressPage();
			myProgress.clickToOpenUnitLessonsProgress(1);
			myProgress.clickStartTestInMyProgressPageForSpecificUnit();
			sleep(2);
			
		// Validate URl, name, click Start and close the window
			toeicStartpage.validateDetailsInToeicStartPage(testName);
	
		report.startStep("Log Out");
			homePage.clickOnLogOut();
	}
	
	@Test
	@TestCaseParams(testCaseID = { "56618" })
	public void testSetProgressExploreGettingToKnowTOEIC() throws Exception {
		
		String courseName = coursesNamesTOEIC[1];
		String courseId = coursesNamesTOEICOLPC[1];
		
		report.startStep("Navigate to "+ coursesNamesTOEIC[1] +" - Unit "+ 2 +" - Lesson "+ 4 +" - Step "+ 1 +" - Task "+ 1);
			learningArea2 = homePage.navigateToTOEICTask(courseCodesTOEIC[1], 2, 4, 1, 1);
			learningArea2.waitToLearningAreaLoaded();
			
		report.startStep("Validate Title is Correct");
			//homePage.validateTaskTitleIsCorrect("Preparing for Part 2: Question-Response");
		homePage.validateTaskTitleContainsCorrect("Part 2");
			report.startStep("Validate Body is Not Null");
			homePage.validateBodyIsNotNull();

			
		report.startStep("Log Out");
			homePage.clickOnLogOut();
	}
	
	
	@Test
//	@TestCaseParams(testCaseID = { "56618" })
	public void testDoProgressAndVerifyInDb_TOEIC() throws Exception {
		
		/*
		 * 1. Clean Progres from TOEIC DB - Done
		 * 1.1 Clean progress and time from ED DB -- low Priority
		 * 2. Do a Progress in ED  TOEIC Explore, Practice and Test and back to Home - Done
		 * 3. wait 70 seconds - Done
		 * 4. Get and verify progress saved in TOEIC DB - Done
		 * 5. Get and verify progress saved in ED DB  
		 */
		
		report.startStep("Remove the User and Course Progress: " + userName +"");
			String courseName = coursesNamesTOEIC[0];
			String toeicCourseId = coursesNamesTOEICOLPC[0];
			String edCourseId = coursesTOEIC[0];
			int unitNumber = 2;
			
			report.startStep("Get the component count in the selected unit");
			List<String[]> units = dbService.getCourseUnitDetils(edCourseId);
			String unitId = units.get(1)[0];
			List<String[]> unitLesson = dbService.getComponentDetailsByUnitId(unitId);
			int componentCount = unitLesson.size();
			
			
		report.startStep("Reading store procedure from file to deleate student progress from TOEIC");
			String sql = pageHelper.readFromTXTfile("files/sqlFiles/deleteStudentProgressInTOEIC.txt");
			pageHelper.resetStudentCourseProgressInTOEIC(userName, toeicCourseId, sql);
			boolean success = false;
			
			//if (!success)
			//	testResultService.addFailTest("delete progress failed", false, false);
			
			//pageHelper.deleteUserProgressFromTOEICbyCourseIDandUserName(userName,toeicCourseId);
		
		report.startStep("Reading store procedure from file to deleate student progress from ED");
			sql = pageHelper.readFromTXTfile("files/sqlFiles/deleteStudentProgress.txt");
			success = studentService.resetStudentCourseProgress(studentId, edCourseId, sql);
			webDriver.refresh();
			homePage.waitHomePageloadedFully();

			if (!success)
				testResultService.addFailTest("delete progress failed", false, false);
				
		report.startStep("Navigate to "+ courseName +" - Unit "+ 2 +" - Lesson "+ 1 +" - Step "+ 1 +" - Task "+ 1);
			learningArea2 = homePage.navigateToTOEICTask(courseCodesTOEIC[0], unitNumber, 1, 1, 1);
			learningArea2.waitToLearningAreaLoaded();
			
		report.startStep("Do the Explore");	
			learningArea2.clickOnPlayVideoButton();
		
		report.startStep("Do the Practice");
			learningArea2.clickOnNextButton(3);
			String correctAnswer = "question-1_answer-1";
			
			for (int i=0;i<4;i++){
				learningArea2.SelectRadioBtn(correctAnswer);
				learningArea2.clickOnNextButton(1);
			}
			
			
		report.startStep("Do the test component");
			learningArea2.clickOnStartTest();
			
			for (int i=0;i<4;i++){
				learningArea2.SelectRadioBtn(correctAnswer);
				learningArea2.clickOnNextButton(1);
			}
			learningArea2.submitTest(true);
			// get the Test score and compare in DB in following steps - componentGrade
			String actualComponetGrade= learningArea2.getTestScore();
			actualComponetGrade = actualComponetGrade.replace("%", "");
			
		report.startStep("Back To Home from LA");
			homePage.clickOnHomeButton();
			sleep(70);
				
	 	//4
		report.startStep("Retrieve users progress from TOEIC DB");
			List<String[]> progress = pageHelper.getUserProgressByUserNameAndCourseIdFromTOEIC(userName,toeicCourseId);
			String courseSessionMinutes = progress.get(0)[5];
			int courseInvestTime = Integer.parseInt(progress.get(0)[4]);
			double courseCompletion = Double.parseDouble(progress.get(0)[3]);
			double componentCompletion = Double.parseDouble(progress.get(0)[7]);
			String componentGrade = progress.get(0)[8];
			double unitCompletion = Double.parseDouble(progress.get(0)[6]); // need to check
			int componentInvestTime = Integer.parseInt(progress.get(0)[9]);
			String actualCourseId = progress.get(0)[2];
			
		report.startStep("Verify user progress in DB saved correctly");	
			textService.assertTrue("Wrong course progress",courseCompletion >= 1.0);
			textService.assertTrue("Wrong course progress",unitCompletion >= 1.0);
			//textService.assertNotNull("Failure, course session minutes null", courseSessionMinutes);
			textService.assertTrue("Wrong component progress", componentCompletion>60.0);
			textService.assertTrue("Wrong course Time Invested",courseInvestTime >= 1);
			textService.assertTrue("Wrong course Time Invested",componentInvestTime >= 1);
			textService.assertTrue("Wrong Component Test",actualComponetGrade.equalsIgnoreCase(componentGrade));
			textService.assertTrue("Wrong course progress",actualCourseId.equalsIgnoreCase(toeicCourseId));
			
			//5
		//	report.startStep("Retrieve users progress from ED DB");
		//	Table: TimeOnLesson, UserCourseProgress
	
		
		report.startStep("Log Out");
			homePage.clickOnLogOut();
	}

	@Category(ReleaseTests.class)
	@Test
	@TestCaseParams(testCaseID = {"86008"})
	public void testVerifyUnitTestProgressInOMS() throws Exception {// main environment currently doesn't work

		report.startStep("Init test data");
		int numOfSections = numOfSectionsAnsweredTOEIC[3];
		int unitID = 1;
		String expectedTestResult = testScoreTOEIC[3]; // The score depends on the num of sections- if more sections will be answered- the score will be higher
		String courseName = coursesNamesTOEIC[0];
		//String toeicCourseId = coursesNamesTOEICOLPC[0];
		//String edCourseId = coursesTOEIC[0];
		//studentId = dbService.getUserIdByUserName(userName, institutionId, true);

		try {
			/*report.startStep("Reading store procedure from file to deleate student progress from TOEIC");
			String sql = pageHelper.readFromTXTfile("files/sqlFiles/deleteStudentProgressInTOEIC.txt");
			pageHelper.resetStudentCourseProgressInTOEIC(userName, toeicCourseId, sql);
			boolean success = false;


			report.startStep("Reading store procedure from file to deleate student progress from ED");
			sql = pageHelper.readFromTXTfile("files/sqlFiles/deleteStudentProgress.txt");
			success = studentService.resetStudentCourseProgress(studentId, edCourseId, sql);
			webDriver.refresh();
			homePage.waitHomePageloadedFully();
			if (!success)
				testResultService.addFailTest("delete progress failed", false, false);*/

			report.startStep("Accomplish unit test");
			String pathToAnswers = "files/TOEIC/UnitTestAnswers2.csv";
			if (pageHelper.linkED.contains("ednewb.engdis.com")) {
				pathToAnswers = "files/TOEIC/UnitTestAnswersBeta.csv";
			}
			makeProgressInCoursePage = new ToeicLearningAreaAndProgressPage(webDriver, testResultService);
			ArrayList<String> UnitTestAnswers2 = makeProgressInCoursePage.accomplishUnitTest(unitID, courseName, expectedTestResult, numOfSections, pathToAnswers);

			report.startStep("Initialize my progress page");
			NewUxMyProgressPage myProgress = new NewUxMyProgressPage(webDriver, testResultService);

			report.startStep("Enter " + courseName + " My Progress Section");
			homePage.navigateToRequiredCourseOnHomePage(courseName);
			//sleep(2);
			myProgress = homePage.clickOnMyProgress();
			myProgress.waitForPageToLoad();

			report.startStep("Verify Unit Test Score is Displayed in My Progress Page");
			myProgress.clickToOpenUnitLessonsProgress(1);
			testResultService.assertEquals(expectedTestResult.toString(), myProgress.checkUnitTestScoreOfSpecificUnit(unitID), "Scores aren't identical");
			sleep(2);
			homePage.clickToOpenNavigationBar();


			report.startStep("Log out from ED Toeic");
			myProgress.clickOnLogOut();
			webDriver.closeBrowser();
			sleep(2);


			report.startStep("Open new browser and log in to OMS");
			webDriver.init();
			webDriver.maximize();
			webDriver.openUrl(PageHelperService.toeicOMSUrl);

			report.startStep("Verify user progress as teacher from OMS");
			LoginPage loginPage = new LoginPage(webDriver, testResultService);
			loginPage.waitForPageToLoad();

			String[] teacher = null;
			teacher = getToeicTeacherCredentials(PageHelperService.teachersOMSFilePath);

			loginPage
					.enterCredentials(teacher[0], teacher[1])//.enterCredentials("1ffa61d", "6b9e43f")
					.openUserReport()
					.fillDataOfWantedUser(null, userName)
					.checkUnitAndFinalTestProgress(expectedTestResult)
					//.clickOnFinalTestReport()
					//.checkProgressDisplayedCorrectly()
					//.checkFinalTestReportPageOpen()
					//.checkReadingAndListeningScores(readingScore, listeningScore)
					.closeProgressReportWindow()
					.logOut()
					.verifyUserIsLoggedOut();

		} catch (Exception e) {
			e.printStackTrace();
			testResultService.addFailTest(e.getMessage(), true, true);
		} catch (AssertionError e) {
			e.printStackTrace();
			testResultService.addFailTest(e.getMessage(), true, true);
		} finally {
			deleteUsedToeicUser();
		}
	}

	@Category(ReleaseTests.class)
	@Test
	@TestCaseParams(testCaseID = {"86008"})
	public void testVerifyFinalTestProgressInOMS() throws Exception {// main environment currently doesn't work
		
	report.startStep("Init test data");	
	
		String courseName = coursesNamesTOEIC[0];
		String toeicCourseId = coursesNamesTOEICOLPC[0];
		String edCourseId = coursesTOEIC[0];
		//studentId = dbService.getUserIdByUserName(userName,institutionId);
		studentId = dbService.getUserIdByUserName(userName,institutionId, true);
		ArrayList<String> finalAnswersArray = null;

	try{
	/*report.startStep("Reading store procedure from file to deleate student progress from TOEIC");
		String sql = pageHelper.readFromTXTfile("files/sqlFiles/deleteStudentProgressInTOEIC.txt");
		pageHelper.resetStudentCourseProgressInTOEIC(userName, toeicCourseId, sql);
		boolean success = false;

	
	report.startStep("Reading store procedure from file to deleate student progress from ED");
		sql = pageHelper.readFromTXTfile("files/sqlFiles/deleteStudentProgress.txt");
		success = studentService.resetStudentCourseProgress(studentId, edCourseId, sql);
		webDriver.refresh();
		homePage.waitHomePageloadedFully();
		if (!success)
			testResultService.addFailTest("delete progress failed", false, false);	*/
		
		
	report.startStep("Init test data for Final test");	
			String readingScore = "5-15"; // 60-95
			String listeningScore = "5-60"; // 85-145
			
	report.startStep("Open Assessments Page");
		testsPage = homePage.openAssessmentsPage(false);
		

	report.startStep("Check Counter value in The First Section is the same one as in The DB");
	//String disTributorTestCount = dbService.getToeicDistributorTestCount(distributorId); // failing with error: Invalid object name 'DistributorTestBank'
	//	testsPage.checkItemsCounterBySection("1", "6");
		
	report.startStep("Click Start Test on the First Test");
		String clickedTestName = testsPage.clickStartTest(1, 1);
		sleep(1);
		// change focus to the new opened pop up
		webDriver.switchToPopup();
		// Initialize toeic start page
		toeicStartPage toeicStartpage = new toeicStartPage(webDriver,testResultService);
		toeicStartpage.waitForPageToLoad();
		
	report.startStep("Validate the URL is Correct");
		webDriver.validateURL("/Runtime/Test.aspx"); 
		//sleep(1);
		
	report.startStep("Press Start New Test if 'Resume Test' Option is Present");
		toeicStartpage.pressStartNewTestInResumePopUp();
	
	report.startStep("Validate the Title is the Name of the First Test");
		toeicStartpage.validateTitle(clickedTestName);
		
	report.startStep("Click Go Button");
		toeicStartpage.clickGoButton();
		
	report.startStep("Validate 'Test Your Sound' Button is Clickable");
		toeicStartpage.checkTestSoundButtonIsClickable();
	
	report.startStep("Validate the Welcome Text is Not Null");
		toeicStartpage.validateTheWelcomeTextIsNotNull();
		
	report.startStep("Click Start");
		toeicStartpage.clickStart();
		
	// Initialize toeic questions page
		toeicQuestionPage toeicQuestionpage = new toeicQuestionPage(webDriver,testResultService);
		
	report.startStep("Click Next");
		toeicQuestionpage.clickNext();
		sleep(2);
		
	// Answer All Questions in Section 1
		finalAnswersArray = toeicQuestionpage.answerQuestionsInSeveralSections(1, "files/TOEIC/finalTestAnswers.csv");
		
	report.startStep("Click Submit");
		toeicQuestionpage.submit(false);
		sleep(1);
		toeicQuestionpage.submit(true);
		sleep(2);
		
	// Change focus to main window
		webDriver.switchToMainWindow();
		sleep(1);
		
	report.startStep("Open Assessments Page");
		testsPage = homePage.openAssessmentsPage(false);
		
	report.startStep("Check Counter value in The First section is the same one as in The DB");
		//String disTributorTestCount = dbService.getToeicDistributorTestCount(distributorId); // failing with error: Invalid object name 'DistributorTestBank'
		//testsPage.checkItemsCounterBySection("1", "6");
	
	report.startStep("Close First Section");
		testsPage.clickOnArrowToOpenSection("1"); 
		//sleep(1);
		
	report.startStep("Check Counter Value in The Second Section is 1");
		testsPage.checkItemsCounterBySection("2", "1");
		
	report.startStep("Open Tests Results Section");
		testsPage.clickOnArrowToOpenSection("2"); 
		//sleep(1);
			
	report.startStep("Verify '" + clickedTestName + "' is Displayed in Tests Results Section");
		testsPage.checkTestDisplayedInSectionByTestName(clickedTestName, "2", "1"); // Might need a new function for TOEIC tests or changing this one
								
	report.startStep("Check Date of Submission");
		String currentDate = webDriver.getCurrentDate();
		testsPage.checkSubmissionDateForPastTests("1",currentDate); // New function when the test wasn't done today
			
	//report.startStep("Check Score in Test Results"); 
		//testsPage.checkScoreForTOEICTest("1", readingScore, listeningScore); // not working since the canvas properties does not hold any text
	//	testsPage.checkScoreForToeicTestIsDisplayed();
		
	report.startStep("Check 'Full Report' and 'View Results' Buttons Are Available");
		boolean isFullReportButtonDisplayed = testsPage.checkViewFullReportDisplayed("1");
		boolean isResultButtonDisplayed = testsPage.checkViewResultsDisplayed("1");
		
		if (isFullReportButtonDisplayed) {
			
		report.startStep("Click 'View Full Report' Button");
				testsPage.clickViewFullReport(1);
				Thread.sleep(1000);
									
				// Change frame to full report frame
				webDriver.switchToFrame("bsModal__iframe");
				Thread.sleep(1000);
				
				AssessmentsFullReportPage assessmentPage =
						new AssessmentsFullReportPage(webDriver, testResultService);
				
		report.startStep("Validate Test Name in 'Full Report' Page");
				assessmentPage.validateNameInFullReport(clickedTestName);
					Thread.sleep(3000);
					
		report.startStep("Validate Test Date in 'Full Report' Page");
				assessmentPage.validateDateInFullReport(currentDate);
			
		report.startStep("Validate Reading and Listening Scores in 'Full Report' Page");
				assessmentPage.checkReadingAndListeningScoresInFullReport(readingScore, listeningScore);
				
		report.startStep("Close 'Full Report' Page");
			
				webDriver.getWebDriver().switchTo().parentFrame();
				//webDriver.getWebDriver().switchTo().defaultContent();
				testsPage.close();
				sleep(2);
			}
		
	report.startStep("Log out from ED Toeic");
			NewUxMyProgressPage myProgress = new NewUxMyProgressPage(webDriver, testResultService);
			myProgress.clickOnLogOut();	
			webDriver.closeBrowser();
			sleep(2);
		
	
	report.startStep("Open new browser and log in to OMS");	
		webDriver.init();
		webDriver.maximize();
		webDriver.openUrl(PageHelperService.toeicOMSUrl);
		
	report.startStep("Verify user progress as teacher due OMS");
		sleep(7);
		LoginPage loginPage = new LoginPage(webDriver, testResultService);
		loginPage.waitForPageToLoad();
		
		String [] teacher = null;
		teacher = getToeicTeacherCredentials(PageHelperService.teachersOMSFilePath);
		
		loginPage
			.enterCredentials(teacher[0], teacher[1])
			.openUserReport()
			.fillDataOfWantedUser(null, userName)
			.clickOnFinalTestReport()
			.checkFinalTestReportPageOpen()
			.checkReadingAndListeningScores(readingScore, listeningScore)
			.closeProgressReportWindow()
			.logOut()
			.verifyUserIsLoggedOut();
		
	}catch (Exception e) {
		e.printStackTrace();
		testResultService.addFailTest(e.getMessage(), true, true);
	} catch (AssertionError e) {
		e.printStackTrace();
		testResultService.addFailTest(e.getMessage(), true, true);
	} finally {
		deleteUsedToeicUser();
	}
}

	@Category(ReleaseTests.class)
	@Test
	@TestCaseParams(testCaseID = {"86008"})
	public void testVerifyStudentLessonProgressInOMS() throws Exception {// main environment currently doesn't work

	try {
		int componentInvestTime = 0;
		int courseInvestTime = 0;
		String[] teacher = null;
		teacher = getToeicTeacherCredentials(PageHelperService.teachersOMSFilePath);

		report.startStep("Init test data");
		int wantedCourse = 1;
		String courseId = "";
		String courseName = coursesNamesTOEIC[0];
		String toeicCourseId = coursesNamesTOEICOLPC[0];
		String edCourseId = coursesTOEIC[0];
		studentId = dbService.getUserIdByUserName(userName, institutionId, DbService.edMerge2DB.length()>0);

/*		report.startStep("Reading store procedure from file to deleate student progress from TOEIC");
		String sql = pageHelper.readFromTXTfile("files/sqlFiles/deleteStudentProgressInTOEIC.txt");
		pageHelper.resetStudentCourseProgressInTOEIC(userName, toeicCourseId, sql);
		sleep(1);
		boolean success = false;


		report.startStep("Reading store procedure from file to deleate student progress from ED");
		sql = pageHelper.readFromTXTfile("files/sqlFiles/deleteStudentProgress.txt");
		success = studentService.resetStudentCourseProgress(studentId, edCourseId, sql);
		sleep(1);
		webDriver.refresh();
		homePage.waitHomePageloadedFully();
		if (!success)
			testResultService.addFailTest("delete progress failed", false, false);
*/

		report.startStep("Get users courses and choose one of them");
		//courseName = "";
		String[] userCourses = pageHelper.getUserAssignedCourses(studentId,true);

		for (int i = 0; i < 1; i++) {

			courseId = userCourses[i];

			//courseName = dbService.getCourseNameById(courseId);
			courseName = courseName.toString().replace(" ", "").trim();
			String actualCourse = homePage.getCurrentCourseName();
			//courseId= "136413";

			report.startStep("Navigating to Course: " + courseName);
			int count = 1;
			while (count < 20 && !(actualCourse.toString().replace(" ", "").trim().equals(courseName))) {
				homePage.carouselNavigateNext();
				sleep(1);
				actualCourse = homePage.getCurrentCourseName();
				count++;
			}

			report.startStep("Perform course according corseName and courseId");
			homePage.clickOnUnitLessons(wantedCourse);
			homePage.clickOnLesson(0, 1);
			ToeicLearningAreaAndProgressPage makeProgressInCoursePage = new ToeicLearningAreaAndProgressPage(webDriver, testResultService);
			makeProgressInCoursePage.makeProgressInToeicCourse(courseName, coursesTOEIC[0], false, true, true, true);
			unitName = makeProgressInCoursePage.unitName;
			progressOfUnit = homePage.getUnitProgress(wantedCourse);
			//performCourse(courseName, courseId, false, true, true, true);

			report.startStep("Retrieve users progress from TOEIC DB");
			List<String[]> progress = pageHelper.getUserProgressByUserNameAndCourseIdFromTOEIC(userName, toeicCourseId);
			if (progress == null) {
				testResultService.addFailTest("Progress doesn't saved at DB or no access to DB");
			}
			try {
				componentInvestTime = Integer.parseInt(progress.get(0)[9]);
				courseInvestTime = Integer.parseInt(progress.get(0)[4]);

				//textService.assertEquals("Time is wrong", componentInvestTime, courseInvestTime);
			} catch (Exception e) {
				testResultService.addFailTest("Data from DB not match");
			} catch (AssertionError e) {
				testResultService.addFailTest("Data from DB not match");
			}


			report.startStep("Retrieve time spend on test from UI");
			List<WebElement> time = webDriver.getWebDriver()
					.findElements(By.cssSelector(".home__studentWidgetHrMinHrTxt.ng-binding"));
			String minutes = time.get(1).getText().toString();
			Character min;
			if (minutes.startsWith("0")) {
				min = minutes.charAt(1);
				minutes = min.toString();
			}
			int timeFromUI = Integer.parseInt(minutes);
			//textService.assertEquals("Time from UI doesn't match time from DB", componentInvestTime, timeFromUI);

			report.startStep("Log out from ED Toeic");
			//homePage.logOutOfED();
			homePage.clickOnLogOut();
			//myProgress.clickOnLogOut();	
			webDriver.closeBrowser();
			sleep(2);


			report.startStep("Open new browser and log in to OMS");
			webDriver.init();
			webDriver.maximize();
			sleep(2);
			webDriver.openUrl(PageHelperService.toeicOMSUrl);

			report.startStep("Verify user progress as teacher due OMS");
			LoginPage loginPage = new LoginPage(webDriver, testResultService);
			loginPage.waitForPageToLoad();

			//String [] teacher = null;
			//	if(PageHelperService.branchCI.equalsIgnoreCase("EDUI_CI_main")) {
			teacher = getToeicTeacherCredentials(PageHelperService.teachersOMSFilePath);
			//	}
			//	if(PageHelperService.branchCI.contains("EDUI_CI_RC_2022_")) {
			//		teacher = getToeicTeacherCredentials(PageHelperService.teachersOMSFilePathProd);

			//	}
			report.startStep("Login to OMS");

			loginPage
					.enterCredentials(teacher[0], teacher[1])
					.openUserReport()
					.fillDataOfWantedUser(null, userName)
					.checkProgressDisplayedCorrectly(unitName, progressOfUnit)
					.checkTimeSpendedOnTestMatch(minutes)
					.closeProgressReportWindow()
					.logOut()
					.verifyUserIsLoggedOut();

		}
	}catch (Exception e){
		e.printStackTrace();
		testResultService.addFailTest(e.getMessage(),true,true);
	}catch (AssertionError e){
		e.printStackTrace();
		testResultService.addFailTest(e.getMessage(),true,true);
	}
	finally {
		deleteUsedToeicUser();
	}

	}
	
	@Test
	@TestCaseParams(testCaseID = {"91551"})	
	public void testSessionTimeOutFromTOEIC() throws Exception {			

	try {			
	
	report.startStep("Change token in local storage to get session time out");
		String token = pageHelper.runJavaScriptCommand("return localStorage.getItem(\"EDAPPToken\")");
		pageHelper.runJavaScriptCommand("localStorage.setItem('EDAPPToken','"+token+"11')");
		
	report.startStep("Wait till session get expired");
		homePage.waitTillInActivityPageOrTimeExpirationApears(70);
		
		/*
	report.startStep("Log out from ED before changing expiration time");	
		homePage.clickOnLogOut();		
				
	try {	
		
	report.startStep("Set expiration time to service via appjson settings (minutes)");
				pageHelper.setSessionExpirationTime("2");
											
	report.startStep("restart the application pool");
				pageHelper.restartWebServiceApplicationPool();
				sleep(2);
				webDriver.refresh();
				loginPage.waitForPageToLoad();
	
		
	report.startStep("Login to ED and close all notifications");	
		loginPage.loginAsStudent(userName, password);
		loginPage.waitLoginAfterRestartAppPool();
		homePage.waitNotificationsAfterRestartPool();
		homePage.closeAllNotifications();
		homePage.waitHomePageloadedFully();
		
		
	report.startStep("Wait until Session time out is displayed after 140 seconds");
		homePage.waitTillInActivityPageOrTimeExpirationApears(140);*/
		
	report.startStep("Verify user get the inactivity page and click OK");
		String message = webDriver.waitForElement("h1", ByTypes.tagName).getText();
		testResultService.assertEquals("This session is no longer active on this device.", message, "Wrong session timeOut message");
		loginPage.clickOK_onSessionIsNoLongerActive();
		sleep(3);
		//testResultService.assertEquals(false, loginPage.isSubmitButtonEnabled(),"Checking that ED Login Page displayed");
		
	
	report.startStep("Login to ED again after session had expired");	
		homePage = loginPage.loginAsStudent(userName, password);
		loginPage.verifyAndConfirmAlertMessage();
		homePage.closeAllNotifications();
		homePage.waitHomePageloadedFully();
		sleep(1);
		
	report.startStep("Verify user logged-in");
		testResultService.assertEquals("Hello Student", homePage.getUserDataText(),
				"First Name in Header of Home Page not match");	
		
	report.startStep("Verify user logged-in");
		homePage.clickOnLogOut();
		
		}catch (Exception e) {
			e.printStackTrace();
			testResultService.addFailTest(e.getMessage(), false, true);			
		}catch (AssertionError e) {
			e.printStackTrace();
			testResultService.addFailTest(e.getMessage(), false, true);	
		}
		/*finally {
			report.startStep("Return normall value (120 min) to session expiration time and restart the app-pool");
			pageHelper.setSessionExpirationTime("120");
			pageHelper.restartWebServiceApplicationPool();
			
		}*/
	}

	@Test
	@TestCaseParams(testCaseID = "91553")
	public void testForceLoginInEDTOEIC() throws Exception {
		
		try {
		report.startStep("logIn with fireFox");
			System.setProperty("webdriver.gecko.driver",configuration.getPathToFirefoxDriver());
			firefoxPage = new FirefoxLoginPage(PageHelperService.toeicUrl);
			firefoxPage.loginToEDwithFirefox(userName, password);
			firefoxPage.verifyAlertUserLoggedInTOEIC();
		/*	firefoxPage.verifyAndConfirmAlertMessage();
			firefoxPage.closeNotificationsInFirefox();
			firefoxPage.verifyUserLogedInFirefox("Student");
				
		report.startStep("Verify user logged-out from previous browser");	
			String url = webDriver.getUrl();
	    	textService.assertEquals("User doesn't logedOut from first browser",url.contains("SessionTimeout"), true);
	    	//loginPage = new NewUXLoginPage(webDriver, testResultService);
	    	loginPage.clickOK_onSessionIsNoLongerActive();
	    	WebElement submit = webDriver.waitForElement("submit1", ByTypes.id, true, 10);
			textService.assertEquals("Logout incorrect", true, submit.isDisplayed());
		
		report.startStep("LogOut from second browser");  		
			firefoxPage.logOutFromEdWithSecondBrowser();
			*/
		}catch (Exception e) {
				e.printStackTrace();
				testResultService.addFailTest(e.getMessage(), true, true);
				
			}finally{
				firefoxPage.closeSession();
				}
		
	}
	
	
	
	
	
	
//	@Test
//	@Category(inProgressTests.class)
//	@TestCaseParams(testCaseID = { "" })
	public void test001GetJson_temp() throws Exception {
		jsonObj = netService.getJsonObject("smb://"+PageHelperService.physicalPath+"/Runtime/MetaData/Courses/ToeicEDMapper/", "1.json", false);
		
		try{
			String edCourseId = getEdCourseId(jsonObj,"edId");
			List<String> unitId = getUnitIds(jsonObj); 
			String[]arr = unitId.get(0).split(",");
			List<String> componentId = getToeicEdComponentId(arr[0]);
			
			report.addTitle("ED CourseId:" + edCourseId);
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		/*
		testEnvironmentPage = new TestEnvironmentPage(webDriver, testResultService);
		System.out.println("Employee ID: "+jsonObj.getInt("name"));   
		System.out.println("Employee ID: "+jsonObj.getInt("toeicId"));
		*/
		//	String introSettings = testEnvironmentPage.getValueOfWantedKeyInSpecificTestId(jsonObj, "name", "edId");
	}
	
	
	private List<String> getUnitIds(JSONObject mainJsonObject) {
		
		List<String> allUnitId = new LinkedList<>();
		String[] oneUnitIds = new String[2];
		String ids ="";
		
		try{
				
			// get all units block
			JSONArray allUnitsBlock = mainJsonObject.getJSONArray("childNodes");
			JSONObject specificUnitJsonObject= new JSONObject();
			
			int count = allUnitsBlock.length();
			
			report.addTitle("Get the Units Ids");
			
			for (int i=0;i<count;i++){
				String jsonArrayString =  allUnitsBlock.get(i).toString();
				specificUnitJsonObject = new JSONObject(jsonArrayString);
				String toeicId = specificUnitJsonObject.get("toeicId").toString();
				String edId = specificUnitJsonObject.get("edId").toString();
				ids = toeicId+","+edId;
				allUnitId.add(ids);
	
				report.report("Unit " + (i+1) + ":" + toeicId + "," + edId);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			
		}
		
		return allUnitId;
	}


	private List<String> getToeicEdComponentId(String toeicUnitId) {
		
		// get all Unit block
		JSONArray allUnitNodesJson = jsonObj.getJSONArray("childNodes");
		// get a specific unit block
		JSONObject singleUnitJsonObject= new JSONObject();
		//JSONArray allUnitChildNodesJson; //= jsonObj.getJSONArray("childNodes");
		JSONArray allComponentChildNodesJson;
		JSONObject tempComponentJsonObject= new JSONObject();
		String tempComponentIds = "";
		
		List<String> componentId = new ArrayList<String>();
		//String[] componentIds = {"",""}; 
		
		int count = allUnitNodesJson.length();
		
		report.addTitle("Get the Component Ids");
		for (int i=0;i<count;i++){
			
			//System.out.println("block" + i + ":" + abc.get(i).toString());
			String jsonArrayString =  allUnitNodesJson.get(i).toString();
			singleUnitJsonObject = new JSONObject(jsonArrayString);
			
			String JsonToeicUnitId = getEdCourseId(singleUnitJsonObject,"toeicId");
			
			
			if (JsonToeicUnitId.equals(toeicUnitId)){
				
				allComponentChildNodesJson = singleUnitJsonObject.getJSONArray("childNodes");
				
				int compCount = allComponentChildNodesJson.length();
						
				for (int j=0;j<compCount;j++){
				
					String JsonToeicComponent =  allComponentChildNodesJson.get(j).toString();
					tempComponentJsonObject = new JSONObject(JsonToeicComponent);
					
					String teicId = getEdCourseId(tempComponentJsonObject,"toeicId");
					String edId = getEdCourseId(tempComponentJsonObject,"edId");
					
					tempComponentIds = teicId+","+edId;
					componentId.add(tempComponentIds);
					
					report.report("Component:" + (j+1) + ":" + teicId + "," + edId);
				}
				break;
			}
		}
		
		return componentId;
	}

	private String getEdCourseId(JSONObject jsonObj2, String prameters) {
	
	String nodeId="";
	try{
		nodeId = jsonObj2.optString(prameters).toString();
	}
	catch (Exception e) {
		e.printStackTrace();
	}
	
	return nodeId;
	}
	

/*	public void performCourse(String courseName, String courseId, boolean checkConsoleErrors, boolean checkProgress, boolean checkUnitLessonNames, boolean checkStepNames) throws Exception {
	
	report.startStep("Get Course Units by courseId");
		learningArea2 = new NewUxLearningArea2(webDriver, testResultService);
		units = dbService.getCourseUnitDetils(courseId);
				
		for (int i = 0; i < 1; i++) {
			
			unitName = units.get(i)[1].trim().replace("  ", " ");
			report.startStep("Get Unit Components by unitId");
			components = dbService.getComponentDetailsByUnitId(units.get(i)[0]);

	report.startStep("Go over unit: " + units.get(i)[1]);
			
			int unitIndex = i + 1;
			int lessonIndex = 1;
			int stepIndex = 1;
			int taskIndex = 1;
			sleep(1);
			homePage.clickOnUnitLessons(unitIndex);
			sleep(2);
			expectedLessonName = new String [components.size()];		
			homePage.clickOnLesson(i, lessonIndex); 
			learningArea2.waitUntilLearningAreaLoaded();
     		//for (int j = 0; j < components.size(); j++) { // PRODUCTION
	
	report.startStep("Go over lesson");		
		for (int j = 0; j < 1; j++) {
			//for (int j = 13; j < components.size(); j++) {	
			report.startStep("Get Component SubComponents(Steps) by componentId");
				List<String[]> subComp = dbService.getSubComponentsDetailsByComponentId(components.get(j)[1]);
				boolean isTest = false;
				boolean isInteract = false;
				boolean stepHasIntro = false;
				String stepName = "undefined";
				stepIndex = 1;
				taskIndex = 1;
						
				for (int h = 0; h < subComp.size(); h++) {
				
					taskIndex = 1;
					stepName = subComp.get(h)[0];
					
					// define if Step is Interact
					if (stepName.contains("Practice 1") || stepName.contains("Practice 2") || stepName.contains("Interact 1") || stepName.contains("Interact 2") ) {
						isInteract = true;
					}
										
					// define if Step is Test
					if (stepName.contains("Test") || stepName.contains("Let's review")) {
						isTest = true;
					}
					
					// define if Step has Intro page
					if (learningArea2.isTaskCounterHasIntroMode()) {
						stepHasIntro = true;
					}
					
					if (h > 0) {
					
						stepIndex = h + 1;
											
						report.startStep("Click on Step: " + stepIndex);
												
						if (isTest) {
							learningArea2.clickOnStep(stepIndex, false);	
							learningArea2.clickOnStartTest();
						} else learningArea2.clickOnStep(stepIndex);	
						sleep(1);
												
						if (isInteract) learningArea2.closeAlertModalByAccept();
			
						
				} else 
						if (stepHasIntro) learningArea2.clickOnNextButton();
													
					if (checkConsoleErrors) {
						report.startStep("Enable console watcher");
						errorList = pageHelper.checkTaskForConsoleErrorsAddToList(errorList, courseName, unitIndex, lessonIndex, stepIndex, taskIndex);
					}
					
					report.startStep("Get SubComponets Items (Tasks) by subComponentId");
										
					List<String[]> expectdTasks = dbService.getSubComponentItems(subComp.get(h)[1]);
					
					report.startStep("Go over every Task / Number of Tasks is :"+expectdTasks.size());
					
					taskIndex = 1;	
					
					int itemTypeId = Integer.parseInt(expectdTasks.get(0)[2]); 
					String itemCode = expectdTasks.get(0)[1];
										
					for (int k = 0; k < expectdTasks.size(); k++) {
						
						itemTypeId = Integer.parseInt(expectdTasks.get(k)[2]);
						itemCode = expectdTasks.get(k)[1];
						
						if (k > 0) {
							learningArea2.clickOnNextButton();
						}
												
						if (checkConsoleErrors) {
							report.startStep("Enable console watcher");
							errorList = pageHelper.checkTaskForConsoleErrorsAddToList(errorList, courseName, unitIndex, lessonIndex, stepIndex, taskIndex);
						}
						
						if (checkProgress) {
							report.startStep("Making progress in Task "+taskIndex+" / Checking status of Task " + taskIndex);
							try {
								learningArea2.makeProgressActionByItemType(itemTypeId, itemCode);
							} catch (Exception e) {
								testResultService.addFailTest("Cannot make progress in "+courseName+" : "+"Unit "+unitIndex+" - "+"Lesson "+ lessonIndex +" - "+"Step "+stepIndex+" - "+"Task "+ taskIndex, false, true);
								e.printStackTrace();
							}
							
						}
						
						taskIndex++;
																				
					}
						
				}
		report.startStep("Submit lesson");			
			if (isTest && checkProgress) 
						learningArea2.submitTest(false);
		
		report.startStep("Go back to Home Page");
				
					learningArea2.clickOnHomeButton();
					if (!checkProgress && isTest) learningArea2.approveTest();
					homePage.waitHomePageloaded();
							
					if (checkProgress) {
		report.startStep("Check Unit "+unitIndex+" Progress Bar");
						progressOfUnit = homePage.getUnitProgress(unitName);
						
		report.startStep("Waiting until progress stores in DB");
					sleep(70);
						
				}
			}
		}
	}*/
	
	
	

	@After
	public void tearDown() throws Exception {
		
		super.tearDown();
	}
}
