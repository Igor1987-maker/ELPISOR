package tests.toeic.oms;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import Interfaces.TestCaseParams;
import pageObjects.edo.AssessmentsFullReportPage;
import pageObjects.edo.NewUXLoginPage;
import pageObjects.edo.NewUxAssessmentsPage;
import pageObjects.edo.NewUxClassicTestPage;
import pageObjects.edo.NewUxLearningArea2;
import pageObjects.edo.NewUxMyProgressPage;
import pageObjects.edo.NewUxUnitObjectivesPage;
import pageObjects.toeic.toeicQuestionPage;
import pageObjects.toeic.toeicResultsPage;
import pageObjects.toeic.toeicStartPage;
import pagesObjects.oms.LoginPage;
import services.PageHelperService;
import testCategories.inProgressTests;
import tests.edo.newux.BasicNewUxTest;

public class completeUserProgressAndVerifyInOMS extends BasicNewUxTest{
	NewUXLoginPage loginPage;
	NewUxClassicTestPage classicTest;
	NewUxAssessmentsPage testsPage;
	NewUxLearningArea2 learningArea2;
	NewUxUnitObjectivesPage unitObjPage;

	@Before
	public void setup() throws Exception {
		super.setup();
		
	report.startStep("Open Toiec");
		webDriver.deleteCookiesAndCache();
		webDriver.quitBrowser();
		webDriver.init();
		webDriver.maximize();
		webDriver.openUrl(PageHelperService.toeicUrl);
	//	Thread.sleep(4000);
		
	report.startStep("Login to Toiec");	
		loginPage = new NewUXLoginPage(webDriver,testResultService);
		loginPage.waitEDToeicLoginLoaded();
		homePage = getToeicUserAndLogin(PageHelperService.userFilePath);
		pageHelper.skipOptin();
		classicTest = new NewUxClassicTestPage(webDriver,testResultService);//skip onboarding
		report.startStep("Wait until loading message dissappers (if its displayed)");
		homePage.waitUntilLoadingMessageIsOver();
		sleep(2);
		//webDriver.waitUntilElementAppears("//div[@class='notificationsCenter_hideSlide']", 100);
	
	report.startStep("Close All Notifications");	
		homePage.closeAllNotifications();

	report.startStep("Skip the Walkthrough");
		pageHelper.skipOnBoardingHP();
		homePage.waitHomePageloadedFully();
		sleep(1);
		
	report.startStep("Close modal pop up (if it appears)");
		homePage.closeModalPopUp();
		
	}
	
	@Test
	@Category (inProgressTests.class)
	@TestCaseParams(testCaseID = { "72175", "56848" })
	public void testCheckUserProgressStoredCorrectlyInDB() throws Exception {
	
		report.startStep("Init test data");	
			String readingScore = "5-15"; // 60-95
			String listeningScore = "5-60"; // 85-145
			//String distributorId = "1";
			String courseName = coursesNamesTOEIC[0];
			String courseId = coursesNamesTOEICOLPC[0];
			
			
			/*
			report.startStep("Delete student progress from TOEIC by store procedure");
			String sql = pageHelper.readFile("deleteStudentProgressInTOEIC.txt");
			boolean success = studentService.resetStudentCourseProgress(userName, courseId, sql);
			if (!success)
				testResultService.addFailTest("delete progress failed", false, false);
			*/
			pageHelper.deleteUserProgressFromTOEICbyCourseIDandUserName(userName,courseId);
					
			report.startStep("Delete student progress from ED by store procedure");
			String sql = pageHelper.readFile("files/sqlFiles/deleteStudentProgress.txt");
			boolean success = studentService.resetStudentCourseProgress(studentId, courseId, sql);
			
			if (!success)
				testResultService.addFailTest("delete progress failed", false, false);
			
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
					sleep(1);
					
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
					sleep(3);
				
				// Initialize report page and check URL and answers
					AssessmentsFullReportPage assessmentsFullReportPage = new AssessmentsFullReportPage(webDriver,testResultService);
					assessmentsFullReportPage.checkAnswersInViewFullReportPopUp(clickedTestName, currentDate, readingScore, listeningScore, finalAnswersArray);
					sleep(2);
					
				report.startStep("Close 'Full Report' Page");
					testsPage.close();
					sleep(1);
			} else {
				testResultService.addFailTest("'View Full Report' Button is Not Displayed in My Progress Page (skipping test)");
			}
			
			sleep(2);
			
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
			
		report.startStep("Open new browser and login to OMS");
			webDriver.closeBrowser();
			webDriver.init();
			webDriver.maximize();
			webDriver.openUrl(PageHelperService.toeicOMSUrl);
			webDriver.getWebDriver().manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		report.startStep("Retrieve users progress from DB");
			List<String[]> progress = pageHelper.getUserProgressByUserNameAndCourseIdFromTOEIC(userName,courseId);
			String courseSessionMinutes = progress.get(0)[5];
			double courseCompletion = Double.parseDouble(progress.get(0)[3]);
			double componentCompletion = Double.parseDouble(progress.get(0)[7]);
			String componentGrade = progress.get(0)[8];
		
		report.startStep("Verify user progress in DB saved correctly");	
			textService.assertTrue("Wrong course progress",courseCompletion > 10.0);
			textService.assertNotNull("Failure, course session minutes null", courseSessionMinutes);
			textService.assertTrue("Wrong component progress", componentCompletion>10.0);
		
		report.startStep("Verify user progress in OMS");
		new LoginPage(webDriver, testResultService)
			.enterCredentials("1ffa61d", "6b9e43f")
			.openUserReport()
			.fillDataOfWantedUser(null, userName)
			.checkProgressDisplayedCorrectly("Pesoneel","14")
			.closeProgressReportWindow()
			.logOut()
			.verifyUserIsLoggedOut();
	}
	
	
		
	
	
	
}
