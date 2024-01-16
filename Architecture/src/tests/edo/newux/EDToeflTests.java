package tests.edo.newux;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.WebElement;

import Enums.ByTypes;
import Interfaces.TestCaseParams;
import pageObjects.edo.AssessmentsFullReportPage;
import pageObjects.edo.NewUXLoginPage;
import pageObjects.edo.NewUxAssessmentsPage;
import pageObjects.edo.NewUxClassicTestPage;
import pageObjects.edo.NewUxMyProgressPage;
import pageObjects.toefl.ToeflStartPage;
import pageObjects.toeic.toeicResultsPage;
import testCategories.inProgressTests;

public class EDToeflTests extends BasicNewUxTest {
	//String url = ""; // not beta
	String url = "https://edux.edusoftrd.com/ed2016#"; // beta
	
	NewUXLoginPage loginPage;
	NewUxClassicTestPage classicTest;
	NewUxAssessmentsPage testsPage;
	
	public static void printResults(Process process) throws IOException {
	    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
	    String line = "";
	    while ((line = reader.readLine()) != null) {
	        System.out.println(line);
	    }
	}
	
	public void cmdCopyFile(String from, String to) throws IOException{
		String command= "cmd /c copy "+ from+" "+ to;
		Process process = Runtime.getRuntime().exec(command);
		printResults(process);
	}

	
	@Before
	public void setup() throws Exception {
		
		System.setProperty("chromeMedia", "true");
		super.setup();
		
		// BETA users
		//st(18-21)
		
		report.startStep("Change Files of Instructions and TImers");
		changeToSilenceRecordings();
		changeToShortTimers();
		
		report.startStep("Open Toefl");
		webDriver.deleteCookiesAndCache();
		
		webDriver.openUrl(url);
		loginPage = new NewUXLoginPage(webDriver,testResultService);
		loginPage.waitEDToeicLoginLoaded(); 
		
		// NOT BETA
		//homePage = getToeicUserAndLogin("files/TOEIC/Users.csv");
		
		// BETA
		//homePage = loginPage.loginAsStudent("st18", "12345");
		
		// TOEFL
		homePage = loginPage.loginAsStudent("dog7", "12345");
		
		classicTest = new NewUxClassicTestPage(webDriver,testResultService);
		
		report.startStep("Wait until loading message dissappers (if its displayed)");
		homePage.waitUntilLoadingMessageIsOver();
			
		report.startStep("Close All Notifications");	
		homePage.closeAllNotifications();
		
		homePage.waitHomePageloaded();
		
		report.startStep("Close modal pop up (if it appears)");
		homePage.closeModalPopUp();
		
		report.startStep("Skip the Walkthrough");
		pageHelper.skipOnBoardingHP();
	}
	
	@Test
	@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "" })
	public void testAssessmentsPageTOEFLprTestResults() throws Exception {
	
		report.startStep("Init test data");	
			String readingScore = ""; 
			String listeningScore = ""; 
			String courseName = coursesNamesTOEIC[0];
			
		report.startStep("Open Assessments Page");
			testsPage = homePage.openAssessmentsPage(false);
				
		report.startStep("Check Counter value in The First Section is the same one as in The DB");
		//String disTributorTestCount = dbService.getToeicDistributorTestCount(distributorId); // failing with error: Invalid object name 'DistributorTestBank'
			testsPage.checkItemsCounterBySection("1", "2");
			
			testsPage.checkItemsCounterBySection("3", "0");
			
		report.startStep("Click Start Test on the PR Test");
			String clickedTestName = testsPage.clickStartTest(1, 2);
			sleep(1);
			
		// change focus to the new opened pop up
			webDriver.switchToPopup();
			sleep(2);
			
		report.startStep("Validate the URL is Correct");
			webDriver.validateURL("ToeflPr"); //
			sleep(1);
			
		// Initialize toefl start page
			ToeflStartPage toeflStartPage = new ToeflStartPage(webDriver,testResultService);
			
		report.startStep("Validate the Title is 'Welcome' and the Text is Not Null");
			toeflStartPage.validateTitle("Welcome");
			toeflStartPage.validateTheWelcomeTextIsNotNull();
			toeflStartPage.clickBeginButton();
			
		report.startStep("Click Begin Button");
		toeflStartPage.clickBeginButton();
			
		
			/*
		// Initialize toeic questions page
			toeicQuestionPage toeicQuestionpage = new toeicQuestionPage(webDriver,testResultService);
			
		report.startStep("Click Next");
			toeicQuestionpage.clickNext();
			sleep(1);
			
		// Answer All Questions in Section 1
			ArrayList<String> finalAnswersArray = toeicQuestionpage.answerQuestionsInSeveralSections(1, "files/TOEIC/finalTestAnswers.csv");
			
		report.startStep("Click Submit");
			toeicQuestionpage.submit(false);
			toeicQuestionpage.submit(true);
			sleep(1);
			
			*/
			
		// Change focus to main window
			webDriver.switchToMainWindow();
			
		report.startStep("Open Assessments Page");
			testsPage = homePage.openAssessmentsPage(false);
			
		report.startStep("Check Counter value in The First section is the same one as in The DB");
			//String disTributorTestCount = dbService.getToeicDistributorTestCount(distributorId); // failing with error: Invalid object name 'DistributorTestBank'
			testsPage.checkItemsCounterBySection("1", "2");
		
		report.startStep("Close First Section");
			testsPage.clickOnArrowToOpenSection("1"); 
			sleep(1);
			
		report.startStep("Check Counter Value in The Second Section is 1");
			testsPage.checkItemsCounterBySection("3", "1");
			
		report.startStep("Open Tests Results Section");
			testsPage.clickOnArrowToOpenSection("3"); 
			sleep(1);
				
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
					//assessmentsFullReportPage.checkAnswersInViewFullReportPopUp(clickedTestName, currentDate, readingScore, listeningScore, finalAnswersArray);
					
				report.startStep("Close 'Full Report' Page");
					testsPage.close();
					Thread.sleep(1000);
			} else {
				testResultService.addFailTest("'View Full Report' Button is Not Displayed (skipping test)");
			}
			
			if (isResultButtonDisplayed) {
				
				report.startStep("Open Assessments Page");
					testsPage = homePage.openAssessmentsPage(false);
					//sleep(3);
				
				// click on view results (directly)
				report.startStep("Close First Section");
					testsPage.clickOnArrowToOpenSection("1"); 
					sleep(1);
					
				report.startStep("Open Tests Results Section");
					testsPage.clickOnArrowToOpenSection("2"); 
					sleep(1);
					
				report.startStep("Click 'View Results' Button");
					testsPage.clickViewResults(1);
					sleep(1);
					
					boolean isErrorDisplayed = testsPage.closeErrorPopUpIfItsDisplayed();
					if (!isErrorDisplayed) {
						// Initialize results page and check answers
							toeicResultsPage toeicResultspage = new toeicResultsPage(webDriver,testResultService);
							//toeicResultspage.checkAnswersInViewResultsPage(finalAnswersArray);
					} else {
						testResultService.addFailTest("'View Results' Button was Clicked and an Error pop up Appeared (skipping test)");
					}
			} else {
				testResultService.addFailTest("'View Results' Button is Not Displayed (skipping test)");
			}
			
		// Initialize my progress page
			NewUxMyProgressPage myProgress = new NewUxMyProgressPage(webDriver, testResultService);
					
		report.startStep("Enter "+courseName+" My Progress Section");	
			homePage.navigateToRequiredCourseOnHomePage(courseName);
			sleep(2);
			myProgress = homePage.clickOnMyProgress();
			sleep(2);
			
		report.startStep("Check Test Name is Displayed Correctly in My Progress Section");
			myProgress.checkTestNameInMyProgressPage(clickedTestName);
			
		report.startStep("Check Score in Test Results"); 
			//testsPage.checkScoreForTOEICTest("1", readingScore, listeningScore); // not working since the canvas properties does not hold any text
			testsPage.checkScoreForToeicTestIsDisplayed();
			sleep(2);
			
		report.startStep("Check 'Full Report' and 'View Results' Buttons are Displayed in My Progress Section");
			isFullReportButtonDisplayed = false;
			isResultButtonDisplayed = false;
			isFullReportButtonDisplayed = myProgress.checkViewFullReportDisplayedInMyProgressPage();
			isResultButtonDisplayed = myProgress.checkViewResultsDisplayedInMyProgressPage();
			
			if (isFullReportButtonDisplayed) {
				
				report.startStep("Click 'View Full Report' Button in My Progress Section");
					myProgress.clickViewFullReportInMyProgressPage();
				
				// Initialize report page and check URL and answers
					AssessmentsFullReportPage assessmentsFullReportPage = new AssessmentsFullReportPage(webDriver,testResultService);
					//assessmentsFullReportPage.checkAnswersInViewFullReportPopUp(clickedTestName, currentDate, readingScore, listeningScore, finalAnswersArray);
					sleep(2);
					
				report.startStep("Close 'Full Report' Page");
					testsPage.close();
					Thread.sleep(1000);
			} else {
				testResultService.addFailTest("'View Full Report' Button is Not Displayed in My Progress Page (skipping test)");
			}
			
			sleep(2);
			
			if (isResultButtonDisplayed) {
				report.startStep("Click 'View Results' Button in My Progress Section");
					myProgress.clickViewResultsInMyProgressPage();
					sleep(2);
				
				// Initialize results page and check answers
					toeicResultsPage toeicResultspage = new toeicResultsPage(webDriver,testResultService);
					//toeicResultspage.checkAnswersInViewResultsPage(finalAnswersArray);
			} else {
				testResultService.addFailTest("'View Results' Button is Not Displayed in My Progress Page (skipping test)");
			}
			
		report.startStep("Log Out");
			myProgress.clickOnLogOut();			
	}
	
	@Test
	@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "" })
	public void testMicTOEFLTest() throws Exception {
		
		// Test is not stable yet. Need to fix errors.
		
		report.startStep("InitDate");
		String userType = "Junior"; // Junior / Primary
		
		report.startStep("Quit Browser and Init new One");
		webDriver.quitBrowser();
		sleep(2);
		webDriver.init();
		sleep(2);
		webDriver.maximize();
	
		report.startStep("Navigate To Url");
		webDriver.openUrl("https://edux.edusoftrd.com/ed2016#/login");
		
		report.startStep("Login with Student");
		homePage = loginPage.loginAsStudent("dog7", "12345");
				
		report.startStep("Wait until loading message dissappers (if its displayed)");
		homePage.waitUntilLoadingMessageIsOver();
			
		report.startStep("Close All Notifications");	
		homePage.closeAllNotifications();
		homePage.waitHomePageloaded();
		
		report.startStep("Close modal pop up (if it appears)");
		homePage.closeModalPopUp();
		
		report.startStep("Skip the Walkthrough");
		pageHelper.skipOnBoardingHP();
	
		report.startStep("Open Assessments Page");
		testsPage = homePage.openAssessmentsPage(false);
			
		report.startStep("Check Counter value in The First Section is 2");
		testsPage.checkItemsCounterBySection("1", "2");
		
		report.startStep("Click Start Test on the Second Test");
		String clickedTestName = testsPage.clickStartTest(1, 2);
		sleep(1);
		
		// Change focus to the new opened pop up
		webDriver.switchToPopup();
		sleep(2);
		
		report.startStep("Validate the URL is Correct");
		webDriver.validateURL("Runtime/ToeflTest.aspx"); 
		sleep(1);
		
		try {
			
		
		// Initialize TOEFL page
		ToeflStartPage toeflStartPage = new ToeflStartPage(webDriver, testResultService);
		
		report.startStep("Validate Header");
		toeflStartPage.validateHeader("Speaking");
		
		report.startStep("Validate Title");
		//toeflStartPage.validateTitle("Welcome to the TOEFL "+userType+"© Speaking Practice Test.");
		toeflStartPage.validateTitle("Welcome to the " + clickedTestName);
		
		report.startStep("Validate Picture src");
		toeflStartPage.checkPictureSrc("Graphics/Lessons/br0tagt101i1.jpg");
		
		report.startStep("Validate Text is Correct");
		toeflStartPage.validateTextContainsWantedMessage("During the practice test");
		
		report.startStep("Validate Next Button is Enabled");
		toeflStartPage.checkNextButtonIsEnabled();
		
		report.startStep("Click Next Button");
		toeflStartPage.clickNext();
		
		report.startStep("Validate Text is Correct");
		toeflStartPage.validateTextContainsWantedMessage("Now put on your headset");
	
		report.startStep("Validate Picture src");
		toeflStartPage.checkPictureSrc("Graphics/Lessons/br0tagt102i1.gif");
		
		report.startStep("Validate Next Button is Enabled");
		toeflStartPage.checkNextButtonIsEnabled();
		
		report.startStep("Click Next Button");
		toeflStartPage.clickNext();
		
		// Check timers
		String expectedMessage = "In order to make sure your microphone works properly";
		String expectedPicture = "Graphics/br0tag/br0tagt103i1.gif";
		toeflStartPage.checkTimers(expectedMessage, expectedPicture);
		
		report.startStep("Click Next Button");
		toeflStartPage.clickNext();
		
		report.startStep("Wait While Instruction Recording is Playing");
		sleep(3);
		
		report.startStep("Wait While Instruction Recording is Playing");
		sleep(3);
		
		// Check timers
		expectedMessage = "When did people start wearing blue clothing?";
		expectedPicture = "Graphics/br0tag/br0tagt202i1.png";
		toeflStartPage.checkTimers(expectedMessage, expectedPicture);

		report.startStep("Click OK Popup");
		webDriver.closeAlertByAccept();
		
		report.startStep("Wait While Instruction Recording is Playing");
		sleep(3);
		
		// Check timers
		expectedMessage= "The six pictures below show a story about";
		expectedPicture="Graphics/br0tau/br0taut201i1.png";
		toeflStartPage.checkTimers(expectedMessage, expectedPicture);

		try {
			report.startStep("Click OK Popup");
			webDriver.closeAlertByAccept();
			
			report.startStep("Wait While Instruction Recording is Playing");
			//sleep(3);
			
			report.startStep("Wait While Instruction Recording is Playing");
			//sleep(3);
			toeflStartPage.waitUntilFirstTimerIsDisplayed();
			
			// Check timers
			expectedMessage = null;//"Speak about what you heard.";
			expectedPicture = null;
			toeflStartPage.checkTimers(expectedMessage, expectedPicture);
		
			report.startStep("Click OK Popup");
			webDriver.closeAlertByAccept();
		} catch (Exception e) {
			System.out.println("Catched exception: " + e);
		}
		
		report.startStep("Wait While Instruction Recording is Playing");
		sleep(3);
		
		report.startStep("Wait While Instruction Recording is Playing");
		sleep(3);
		
		report.startStep("Wait Unit Timer of Step 1 is Enabled");
		toeflStartPage.waitUntilFirstTimerIsDisplayed();
		toeflStartPage.waitUnilFirstStepTimerIsEnabled();
		
		// Check timers
		expectedPicture="Graphics/Lessons/br0tawt202.jpg";
		expectedMessage = "Talk about the three stages";
		toeflStartPage.checkTimers(expectedMessage, expectedPicture);
		
		report.startStep("Click OK Popup");
		webDriver.closeAlertByAccept();
		
		report.startStep("Click Next Button end of test");
		toeflStartPage.clickNext();
		
		report.startStep("Change Focus to Main Window");
		webDriver.switchToMainWindow();
		
		report.startStep("Log out");
		homePage.logOutOfED();
		
		report.startStep("Log in as teacher");
		loginPage.loginAsTmsUser("toeflteacher", "12345");
		
		
		// go to reports -> speaking test recordings
		
		// click on student jr3/4 (the one logged in)
		
		// change focus to new window
		
		// validate student name and class
		
		// add some other validations
		
		// choose speaking 1
		
		// score recordings and save the score
		
		// do the same for all recordings?
		
		// save score
		
		// close
		
		// log out
		
		// log in as student
		
		// open assessments -> test results
		
		// validate score is correct
		} catch (Exception e) {
			System.out.println(e);
			webDriver.printScreen("e: "+e);
		}
				
		
		
		
	
		
		// go back to previous tab and log out
		webDriver.closeNewTab(2);
		homePage.logOutOfED();
		
	}
	
	public void clickOnDisabledNext() throws Exception{
		sleep(1);
		WebElement element  = webDriver.waitForElement("//button[contains(@class,'courseBtnext')]",  ByTypes.xpath);
		pageHelper.runJavaScriptCommandWithArguments("arguments[0].removeAttribute('disabled','disabled')", element);
		element.click();	
	}
	
	@After
	public void tearDown() throws Exception {
		
		report.startStep("Change Files of Instructions and TImers");
		changeToOriginalRecordings();
		changeToOriginalTimers();
		super.tearDown();
	}
	
	public void changeToSilenceRecordings() throws IOException{
		Process process = Runtime.getRuntime().exec("cmd /c copy \\\\frontqa2016\\wwwroot\\WebUx\\Runtime\\ToeflJr\\Runtime\\Media\\Silence\\br0tagt104.mp3 \\\\frontqa2016\\wwwroot\\WebUx\\Runtime\\ToeflJr\\Runtime\\Media\\Lessons");
		printResults(process);
		
		process = Runtime.getRuntime().exec("cmd /c copy \\\\frontqa2016\\wwwroot\\WebUx\\Runtime\\ToeflJr\\Runtime\\Media\\Silence\\br0tagt201.mp3 \\\\frontqa2016\\wwwroot\\WebUx\\Runtime\\ToeflJr\\Runtime\\Media\\Lessons");
		printResults(process);
		
		process = Runtime.getRuntime().exec("cmd /c copy \\\\frontqa2016\\wwwroot\\WebUx\\Runtime\\ToeflJr\\Runtime\\Media\\Silence\\br0taut101.mp3 \\\\frontqa2016\\wwwroot\\WebUx\\Runtime\\ToeflJr\\Runtime\\Media\\Lessons");
		printResults(process);
		
		process = Runtime.getRuntime().exec("cmd /c copy \\\\frontqa2016\\wwwroot\\WebUx\\Runtime\\ToeflJr\\Runtime\\Media\\Silence\\br0tavt101.mp3 \\\\frontqa2016\\wwwroot\\WebUx\\Runtime\\ToeflJr\\Runtime\\Media\\Lessons");
		printResults(process);
		
		process = Runtime.getRuntime().exec("cmd /c copy \\\\frontqa2016\\wwwroot\\WebUx\\Runtime\\ToeflJr\\Runtime\\Media\\Silence\\br0tawt202.mp3 \\\\frontqa2016\\wwwroot\\WebUx\\Runtime\\ToeflJr\\Runtime\\Media\\Lessons");
		printResults(process);
				
		process = Runtime.getRuntime().exec("cmd /c copy \\\\frontqa2016\\wwwroot\\WebUx\\Runtime\\ToeflJr\\Runtime\\Media\\Silence\\br0tav\\br0tav.mp3 \\\\frontqa2016\\wwwroot\\WebUx\\Runtime\\ToeflJr\\Runtime\\Media\\br0tav");
		printResults(process);
					
		process = Runtime.getRuntime().exec("cmd /c copy \\\\frontqa2016\\wwwroot\\WebUx\\Runtime\\ToeflJr\\Runtime\\Media\\Silence\\br0taw\\br0taw.mp3 \\\\frontqa2016\\wwwroot\\WebUx\\Runtime\\ToeflJr\\Runtime\\Media\\br0taw");
		printResults(process);
		
		process = Runtime.getRuntime().exec("cmd /c copy \\\\frontqa2016\\wwwroot\\WebUx\\Runtime\\ToeflJr\\Runtime\\Media\\Silence\\br0tawt101.mp3 \\\\frontqa2016\\wwwroot\\WebUx\\Runtime\\ToeflJr\\Runtime\\Media\\Lessons");
		printResults(process);
		
		
	}
	
	public void changeToOriginalRecordings() throws IOException{
		Process process = Runtime.getRuntime().exec("cmd /c copy \\\\frontqa2016\\wwwroot\\WebUx\\Runtime\\ToeflJr\\Runtime\\Media\\Original\\br0tagt104.mp3 \\\\frontqa2016\\wwwroot\\WebUx\\Runtime\\ToeflJr\\Runtime\\Media\\Lessons");
		printResults(process);
		
		process = Runtime.getRuntime().exec("cmd /c copy \\\\frontqa2016\\wwwroot\\WebUx\\Runtime\\ToeflJr\\Runtime\\Media\\Original\\br0tagt201.mp3 \\\\frontqa2016\\wwwroot\\WebUx\\Runtime\\ToeflJr\\Runtime\\Media\\Lessons");
		printResults(process);
		
		process = Runtime.getRuntime().exec("cmd /c copy \\\\frontqa2016\\wwwroot\\WebUx\\Runtime\\ToeflJr\\Runtime\\Media\\Original\\br0taut101.mp3 \\\\frontqa2016\\wwwroot\\WebUx\\Runtime\\ToeflJr\\Runtime\\Media\\Lessons");
		printResults(process);
		
		process = Runtime.getRuntime().exec("cmd /c copy \\\\frontqa2016\\wwwroot\\WebUx\\Runtime\\ToeflJr\\Runtime\\Media\\Original\\br0tavt101.mp3 \\\\frontqa2016\\wwwroot\\WebUx\\Runtime\\ToeflJr\\Runtime\\Media\\Lessons");
		printResults(process);
		
		process = Runtime.getRuntime().exec("cmd /c copy \\\\frontqa2016\\wwwroot\\WebUx\\Runtime\\ToeflJr\\Runtime\\Media\\Original\\br0tawt202.mp3 \\\\frontqa2016\\wwwroot\\WebUx\\Runtime\\ToeflJr\\Runtime\\Media\\Lessons");
		printResults(process);
				
		process = Runtime.getRuntime().exec("cmd /c copy \\\\frontqa2016\\wwwroot\\WebUx\\Runtime\\ToeflJr\\Runtime\\Media\\Original\\br0tav\\br0tav.mp3 \\\\frontqa2016\\wwwroot\\WebUx\\Runtime\\ToeflJr\\Runtime\\Media\\br0tav");
		printResults(process);
					
		process = Runtime.getRuntime().exec("cmd /c copy \\\\frontqa2016\\wwwroot\\WebUx\\Runtime\\ToeflJr\\Runtime\\Media\\Original\\br0taw\\br0taw.mp3 \\\\frontqa2016\\wwwroot\\WebUx\\Runtime\\ToeflJr\\Runtime\\Media\\br0taw");
		printResults(process);
		
		process = Runtime.getRuntime().exec("cmd /c copy \\\\frontqa2016\\wwwroot\\WebUx\\Runtime\\ToeflJr\\Runtime\\Media\\Original\\br0tawt101.mp3 \\\\frontqa2016\\wwwroot\\WebUx\\Runtime\\ToeflJr\\Runtime\\Media\\Lessons");
		printResults(process);
	}
	
	public void changeToShortTimers() throws IOException{
		
		// CHANGE TIMERS
		Process process = Runtime.getRuntime().exec("cmd /c copy \\\\frontqa2016\\wwwroot\\EdResDeploy\\Runtime\\Content\\ShortTimers\\br0tagt103.js \\\\frontqa2016\\wwwroot\\EdResDeploy\\Runtime\\Content\\br0tag");
		printResults(process);
		
		process = Runtime.getRuntime().exec("cmd /c copy \\\\frontqa2016\\wwwroot\\EdResDeploy\\Runtime\\Content\\ShortTimers\\br0tagt202.js \\\\frontqa2016\\wwwroot\\EdResDeploy\\Runtime\\Content\\br0tag");
		printResults(process);
		
		process = Runtime.getRuntime().exec("cmd /c copy \\\\frontqa2016\\wwwroot\\EdResDeploy\\Runtime\\Content\\ShortTimers\\br0taut201.js \\\\frontqa2016\\wwwroot\\EdResDeploy\\Runtime\\Content\\br0tau");
		printResults(process);
		
		process = Runtime.getRuntime().exec("cmd /c copy \\\\frontqa2016\\wwwroot\\EdResDeploy\\Runtime\\Content\\ShortTimers\\br0tavt202.js \\\\frontqa2016\\wwwroot\\EdResDeploy\\Runtime\\Content\\br0tav");
		printResults(process);
		
		process = Runtime.getRuntime().exec("cmd /c copy \\\\frontqa2016\\wwwroot\\EdResDeploy\\Runtime\\Content\\ShortTimers\\br0tawt202.js \\\\frontqa2016\\wwwroot\\EdResDeploy\\Runtime\\Content\\br0taw");
		printResults(process);
	}
	
	public void changeToOriginalTimers() throws IOException {
		Process process = Runtime.getRuntime().exec("cmd /c copy \\\\frontqa2016\\wwwroot\\EdResDeploy\\Runtime\\Content\\OriginalTimers\\br0tagt103.js \\\\frontqa2016\\wwwroot\\EdResDeploy\\Runtime\\Content\\br0tag");
		printResults(process);
		
		process = Runtime.getRuntime().exec("cmd /c copy \\\\frontqa2016\\wwwroot\\EdResDeploy\\Runtime\\Content\\OriginalTimers\\br0tagt202.js \\\\frontqa2016\\wwwroot\\EdResDeploy\\Runtime\\Content\\br0tag");
		printResults(process);
		
		process = Runtime.getRuntime().exec("cmd /c copy \\\\frontqa2016\\wwwroot\\EdResDeploy\\Runtime\\Content\\OriginalTimers\\br0taut201.js \\\\frontqa2016\\wwwroot\\EdResDeploy\\Runtime\\Content\\br0tau");
		printResults(process);
		
		process = Runtime.getRuntime().exec("cmd /c copy \\\\frontqa2016\\wwwroot\\EdResDeploy\\Runtime\\Content\\OriginalTimers\\br0tavt202.js \\\\frontqa2016\\wwwroot\\EdResDeploy\\Runtime\\Content\\br0tav");
		printResults(process);
		
		process = Runtime.getRuntime().exec("cmd /c copy \\\\frontqa2016\\wwwroot\\EdResDeploy\\Runtime\\Content\\OriginalTimers\\br0tawt202.js \\\\frontqa2016\\wwwroot\\EdResDeploy\\Runtime\\Content\\br0taw");
		printResults(process);
}
	
	
	
	
	

}
