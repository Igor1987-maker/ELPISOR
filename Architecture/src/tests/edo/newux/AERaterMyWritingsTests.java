package tests.edo.newux;

import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.WebElement;

import Enums.SubComponentName;
import Enums.WritingStatus;
import Interfaces.TestCaseParams;
import Objects.Course;
import Objects.Student;
import pageObjects.DragAndDropSection;
import pageObjects.EdoHomePage;
import pageObjects.edo.NewUXLoginPage;
import pageObjects.edo.NewUxAssignmentsPage;
import pageObjects.edo.NewUxDragAndDropSection;
import pageObjects.edo.NewUxLearningArea;
import pageObjects.tms.TmsHomePage;
import testCategories.NonAngularLearningArea;
import testCategories.inProgressTests;
import testCategories.reg2;
import testCategories.reg3;
import testCategories.unstableTests;

@Category(NonAngularLearningArea.class)
public class AERaterMyWritingsTests extends BasicNewUxTest {

	
	NewUXLoginPage loginPage;
	NewUxLearningArea learningArea;
	NewUxAssignmentsPage assignPage; 
    TmsHomePage tmsHomePage;	
	
	List<Course> courses = null;
	List<String> writingIdForDelete = new ArrayList<String>();
	
	String writingId = null;
	String newWritingId = null;
	
//--igb 2018.08.12-------------	
	private static final String textFile = "files/assayFiles/text2.txt";
	
	boolean bTestFailed = false;  // igb 2018.08.06

	@Before
	public void setup() throws Exception {
		super.setup();
		
	}

	@Test
	@TestCaseParams(testCaseID = "27019", skippedBrowsers = {"firefox"})
	public void testERaterAssignmentsPageFlow()	throws Exception {

		bTestFailed = false;  // igb 2018.08.06
		
		try {
			report.startStep("Init test data");
			int courseIndex = 2;
			int rate = 1;
		
			String expSubDate = pageHelper.getCurrentDateByFormat("dd/MM/yyyy");
			
			report.startStep("Using file: " + textFile);
			
			report.startStep("Create and login to ED as student");
			String classNameER = configuration.getProperty("classname.openSpeech");
			String institutionId = configuration.getInstitutionId();
			studentId = pageHelper.createUSerUsingSP(institutionId, classNameER);
			String studentUserNameER = dbService.getUserNameById(studentId, institutionId);
					
			loginPage = new NewUXLoginPage(webDriver,testResultService);
			homePage = loginPage.loginAsStudent(studentUserNameER, "12345");
			homePage.waitHomePageloaded();
			
			report.startStep("Navigate to E-Rater Task in B2-U1-L2");
			learningArea = homePage.navigateToCourseUnitAndLesson(courseCodes, courseCodes[courseIndex], 1, 2);
			sleep(1);
			learningArea.clickOnStep(2);
			sleep(1);
			learningArea.clickOnTask(4);
			sleep(1);
		
			report.startStep("Get header & left side text");
			String header1 = learningArea.getHeaderTitle();
			String resourceText1 = learningArea.checkThatReadingTextIsDisplayed();
			
			report.startStep("Submit text with unique label");
			String assayText = textService.getTextFromFile(textFile, Charset.defaultCharset());
							
			String uniquelabel = "";
			
			for(int i = 0; i < 8; i++) {
		    	uniquelabel += (char)(Math.random() * 26 + 97);
			}
		
			String textToSubmit = uniquelabel + " " + assayText;
			learningArea.submitTextToERater(textToSubmit);
			
			report.startStep("Check Submit btn is disabled");
			learningArea.checkThatSubmitEraterBtnIsDisabled();
			
			report.startStep("Check automation evaluation attempt indication");
			String attemptNum = learningArea.getAttemptNumberOfAutomatedEvaluation();
			testResultService.assertEquals("1", attemptNum, "Current attempt count not correct");
			
			report.startStep("Get words count");
			String wordsCountLA = learningArea.getERaterWordsCountOnSubmit();
			
			report.startStep("Checking the xml and json");
			writingId = eraterService.getWritingIdByUserIdAndTextStart(studentId, uniquelabel);
			writingIdForDelete.add(writingId);
			report.report("writing id is: " + writingId);
			eraterService.compareJsonAndXmlByWritingId(writingId);
			
			report.startStep("Open My Assignment Page and select Course with latest assignment");
			learningArea.clickToOpenNavigationBar();
			assignPage = learningArea.openAssignmentsPage(false);
			sleep(8);
			assignPage.clickOnMyWritingsTab(true);
			assignPage.selectCourseInMyWritings(coursesNames[courseIndex], true);
			sleep(2);
			
			//report.startStep("Verify writing status"); -- igb 2018.10.09 --> equal to new-LA test
			//assignPage.verifyWritingStatus(WritingStatus.autoFeedback, expSubDate, true);
			
			report.startStep("Click on See Feedback link");
			assignPage.clickOnSeeFeedbackTryAgainInMyWritings();
			sleep(3);
			assignPage.switchToFeedbackFrameInMyWritings();
			sleep(3);
		
			report.startStep("Validate Statistics Pop Up");
			assignPage.validateStatisticsPopUp(wordsCountLA, true);
					
			report.startStep("Click on More Details button");
			assignPage.clickOnFeedbackMoreDetails();
			sleep(2);
				
			report.startStep("Verify Menu Features");
			expSubDate = pageHelper.getCurrentDateByFormat("dd.MM.yyyy");
			learningArea.clickOnOKButton();
			verifyMenuFeaturesInERFeedback(wordsCountLA, resourceText1, expSubDate, true);
					
			report.startStep("Edit Text and Submit Again");
			String newText = " this is new text";
			assignPage.editFeedbackAssignmentText(newText, true);
			wordsCountLA = assignPage.getERaterWordsCountOnSubmit();
			assignPage.clickOnFeedbackSubmitBtn();
			sleep(3);
		
			report.startStep("Verify writing status");
			expSubDate = pageHelper.getCurrentDateByFormat("dd/MM/yyyy");
			assignPage.verifyWritingStatus(WritingStatus.submitted, expSubDate, false);
			
			report.startStep("Wait for writing to be processed again");
			newWritingId = eraterService.getWritingIdByUserIdAndTextStart(studentId, newText, true);
			
			report.startStep("Press on View link and check redirection to original lesson");
			assignPage.clickOnViewInMyWritings();
			sleep(3);
			String header2 = learningArea.getHeaderTitle();
			String resourceText2 = learningArea.checkThatReadingTextIsDisplayed();
			testResultService.assertEquals(header1, header2, "Redirected to wrong lesson after View pressed");
			testResultService.assertEquals(resourceText1, resourceText2, "Redirected to wrong lesson after View pressed");
					
			report.startStep("Navigate to E-Rater task and check message");				
			learningArea.clickOnStep(2);
			learningArea.clickOnTask(4);
			sleep(1);
			learningArea.validateAlertModalByMessage(learningArea.E_Rater_Confirmation_3, true);
			
			report.startStep("Check Submit btn is disabled");
			learningArea.checkThatSubmitEraterBtnIsDisabled();
			
			report.startStep("Check automation evaluation attempt indication");
			attemptNum = learningArea.getAttemptNumberOfAutomatedEvaluation();
			testResultService.assertEquals("2", attemptNum, "Current attempt count not correct");
			
			report.startStep("Logout");
			learningArea.clickOnLogoutLearningArea();
			loginPage = new NewUXLoginPage(webDriver, testResultService);
			testResultService.assertEquals(false, loginPage.isSubmitButtonEnabled(), "Checking that ED Login Page displayed");
					
			report.startStep("Login as teacher, add comment, rate and send feedback");
			String commentText = enterTMSandSendFeedbackOnWriting(studentUserNameER, courseIndex, rate);
			
			report.startStep("Login as student");
			loginPage.loginAsStudent(studentUserNameER,"12345");
			homePage.waitHomePageloaded();
			
			report.startStep("Open My Assignment Page and select Course with latest assignment");
			assignPage = learningArea.openAssignmentsPage(false);
			sleep(1);
			assignPage.clickOnMyWritingsTab(true);
			assignPage.selectCourseInMyWritings(coursesNames[courseIndex], true);
			
			report.startStep("Verify writing status");
			assignPage.verifyWritingStatus(WritingStatus.teacherFeedback, expSubDate, true);
					
			report.startStep("Click on See Feedback link");
			assignPage.clickOnSeeFeedback();
			sleep(1);
			assignPage.switchToFeedbackFrameInMyWritings();
			sleep(1);
			assignPage.checkRatingFromTeacher(rate, false);
			sleep(1);
					
			report.startStep("Validate Statistics Pop Up");
			assignPage.validateStatisticsPopUp(wordsCountLA, false);
			
			report.startStep("Click on More Details button");
			assignPage.clickOnFeedbackMoreDetails();
			sleep(2);
			
			report.startStep("Verify Menu Features");
			expSubDate = pageHelper.getCurrentDateByFormat("dd.MM.yyyy");
			verifyMenuFeaturesInERFeedback(wordsCountLA, resourceText1, expSubDate, false);
					
			report.startStep("Close Feedback Page Menu");		
			assignPage.clickOnFeedbackPageMenu(); 
						
			report.startStep("Select teachers comment and verify text");	
			assignPage.checkForTeacherCommentById("tComments", commentText);
			
			report.startStep("Close Feedback page");	
			assignPage.closeWritingFeedbackPage();
			
			report.startStep("Verify writing status");
			expSubDate = pageHelper.getCurrentDateByFormat("dd/MM/yyyy");
			webDriver.switchToMainWindow();
			homePage.switchToFrameInModal();
			assignPage.verifyWritingStatus(WritingStatus.teacherFeedback, expSubDate, false);
							
			report.startStep("Close Assignments page");
			webDriver.switchToTopMostFrame();
			homePage.switchToFrameInModal();
			assignPage.close();
		}
		catch(Exception e)
		{
			bTestFailed = true;
			report.reportFailure(e.getMessage());
		}
	}
	@Category(inProgressTests.class)
	@Test
	@TestCaseParams(testCaseID = "40884", skippedBrowsers = {"firefox"})
	public void testMyAssigmentAlertOldLA()throws Exception {
         bTestFailed = false;  // igb 2018.08.06
		
		try {
			int courseIndex = 2;
//			int rate = 1;
			
//			String expSubDate = pageHelper.getCurrentDateByFormat("dd/MM/yyyy");
					
			report.startStep("Create and login to ED as student");
			String classNameER = configuration.getProperty("classname.openSpeech");
			String institutionId = configuration.getInstitutionId();
			studentId = pageHelper.createUSerUsingSP(institutionId, classNameER);
			String studentUserNameER = dbService.getUserNameById(studentId, institutionId);
					
			loginPage = new NewUXLoginPage(webDriver,testResultService);
			homePage = loginPage.loginAsStudent(studentUserNameER, "12345");
					
			report.startStep("Navigate to E-Rater Task in B2-U1-L2");
			learningArea = homePage.navigateToCourseUnitAndLesson(courseCodes, courseCodes[courseIndex], 1, 2);
			sleep(2);
			learningArea.clickOnStep(2);
			sleep(2);
			learningArea.clickOnTask(4);
			sleep(4);
			
			report.startStep("Get header & left side text");
			String resourceText1 = learningArea.checkThatReadingTextIsDisplayed();
		
			report.startStep("Submit text with unique label");
			report.report("Getting text from file");
			String assayText = textService.getTextFromFile(textFile, Charset.defaultCharset());
							
			String uniquelabel = "";
			
			for(int i = 0; i < 8; i++) {
			   	uniquelabel += (char)(Math.random() * 26 + 97);
			}
			    
			String textToSubmit = uniquelabel + " " + assayText;
			report.report("Entering submit text to erater and pressing submit function");
			learningArea.submitTextToERater(textToSubmit);
			
			report.startStep("Check Submit btn is disabled");
			learningArea.checkThatSubmitEraterBtnIsDisabled();
			
			report.startStep("Check automation evaluation attempt indication");
			String attemptNum = learningArea.getAttemptNumberOfAutomatedEvaluation();
			testResultService.assertEquals("1", attemptNum, "Current attempt count not correct");
			
			report.startStep("Get words count");
			String wordsCountLA = learningArea.getERaterWordsCountOnSubmit();
		
			report.startStep("Checking the xml and json");
			writingId = eraterService.getWritingIdByUserIdAndTextStart(studentId, uniquelabel);
			writingIdForDelete.add(writingId);
			report.report("writing id is: " + writingId);
			eraterService.compareJsonAndXmlByWritingId(writingId);
			
			report.startStep("Open and Close My Assignment Page and check assigment alert on");
			learningArea.clickToOpenNavigationBar();
			sleep(5);
			assignPage = learningArea.openAssignmentsPage(false);
			sleep(5);
			assignPage.close();
			sleep(2);
			learningArea.clickOnAlert();
			
			report.startStep("Open My Assignment Page and select Course with latest assignment");
			assignPage = learningArea.openAssignmentsPage(false);
			assignPage.clickOnMyWritingsTab(true);
			assignPage.selectCourseInMyWritings(coursesNames[courseIndex], true);
			
		report.startStep("Click on See Feedback link");
			assignPage.clickOnSeeFeedbackTryAgainInMyWritings();
			sleep(1);
			assignPage.switchToFeedbackFrameInMyWritings();
			sleep(2);
	
		report.startStep("Validate Statistics Pop Up");
			assignPage.validateStatisticsPopUp(wordsCountLA, true);
			sleep(1);
			
		report.startStep("Click on More Details button and check assigment alert off");
			assignPage.clickOnFeedbackMoreDetails();
			sleep(1);
			learningArea.clickOnOKButton();
			assignPage.clickOnFeedbackSubmitBtn();
			assignPage.close();
			learningArea.withOutAlert(true);
		}
		catch(Exception e)
		{
			bTestFailed = true;
			report.reportFailure(e.getMessage());
		}
	}

	
	@Test
	@TestCaseParams(testCaseID = "26211", skippedBrowsers = {"firefox"})
	public void testERaterTaskPageFlow() throws Exception {
	
		bTestFailed = false;  // igb 2018.08.06
		
		try {
			report.startStep("Init test data");
			int courseIndex = 2;
			int rate = 1;
			
			String expSubDate = pageHelper.getCurrentDateByFormat("dd/MM/yyyy");
					
			report.startStep("Create and login to ED as student");
			String classNameER = configuration.getProperty("classname.openSpeech");
			String institutionId = configuration.getInstitutionId();
			studentId = pageHelper.createUSerUsingSP(institutionId, classNameER);
			String studentUserNameER = dbService.getUserNameById(studentId, institutionId);
					
			loginPage = new NewUXLoginPage(webDriver,testResultService);
			homePage = loginPage.loginAsStudent(studentUserNameER, "12345");
					
			report.startStep("Navigate to E-Rater Task in B2-U1-L2");
			learningArea = homePage.navigateToCourseUnitAndLesson(courseCodes, courseCodes[courseIndex], 1, 2);
			sleep(2);
			learningArea.clickOnStep(2);
			sleep(2);
			learningArea.clickOnTask(4);
			sleep(4);
			
			report.startStep("Get header & left side text");
			String resourceText1 = learningArea.checkThatReadingTextIsDisplayed();
		
			report.startStep("Submit text with unique label");
			report.report("Getting text from file");
			String assayText = textService.getTextFromFile(textFile, Charset.defaultCharset());
							
			String uniquelabel = "";
			
			for(int i = 0; i < 8; i++) {
			   	uniquelabel += (char)(Math.random() * 26 + 97);
			}
			    
			String textToSubmit = uniquelabel + " " + assayText;
			report.report("Entering submit text to erater and pressing submit function");
			learningArea.submitTextToERater(textToSubmit);
			
			report.startStep("Check Submit btn is disabled");
			learningArea.checkThatSubmitEraterBtnIsDisabled();
			
			report.startStep("Check automation evaluation attempt indication");
			String attemptNum = learningArea.getAttemptNumberOfAutomatedEvaluation();
			testResultService.assertEquals("1", attemptNum, "Current attempt count not correct");
			
			report.startStep("Get words count");
			String wordsCountLA = learningArea.getERaterWordsCountOnSubmit();
		
			report.startStep("Checking the xml and json");
			writingId = eraterService.getWritingIdByUserIdAndTextStart(studentId, uniquelabel);
			writingIdForDelete.add(writingId);
			report.report("writing id is: " + writingId);
			eraterService.compareJsonAndXmlByWritingId(writingId);
					
			report.startStep("Navigate out and back to E-Rater task");
			learningArea.clickOnBackButton();
			sleep(1);
			learningArea.clickOnNextButton();
			sleep(1);
			
			report.startStep("Click on See Feedback btn and switch to frame");
			learningArea.clickOnOKButton();
			//learningArea.clickOnSeeFeedbackInERater();
			sleep(3);
			homePage.switchToFrameInModal();
					
			report.startStep("Validate Statistics Pop Up");
			assignPage = new NewUxAssignmentsPage(webDriver, testResultService);
			sleep(3);
			assignPage.validateStatisticsPopUp(wordsCountLA, true);
				
			report.startStep("Click on More Details button");
			assignPage.clickOnFeedbackMoreDetails();
			sleep(1);
				
			report.startStep("Verify Menu Features");
			expSubDate = pageHelper.getCurrentDateByFormat("dd.MM.yyyy");
			learningArea.clickOnOKButton();
			verifyMenuFeaturesInERFeedback(wordsCountLA, resourceText1, expSubDate, true);
					
			report.startStep("Edit Text and Submit Again");
			String newText = " this is new text";
			assignPage.editFeedbackAssignmentText(newText, false);
			wordsCountLA = assignPage.getERaterWordsCountOnSubmit();
			assignPage.clickOnFeedbackSubmitInLearningArea();
			learningArea.closeConfirmAlertModalByAccept();
			Thread.sleep(5000);
			webDriver.closeAlertByAccept();
			Thread.sleep(5000);
			webDriver.switchToMainWindow();
			learningArea.closeAlertModalByAccept();
			Thread.sleep(5000);
					
			report.startStep("Wait for writing to be processed again");
			newWritingId = eraterService.getWritingIdByUserIdAndTextStart(studentId, newText, true);
					
			report.startStep("Check Submit btn is disabled");
			learningArea.checkThatSubmitEraterBtnIsDisabled();
			
			report.startStep("Check automation evaluation attempt indication");
			attemptNum = learningArea.getAttemptNumberOfAutomatedEvaluation();
			testResultService.assertEquals("2", attemptNum, "Current attempt count not correct");
			
			report.startStep("Logout");
			learningArea.clickOnLogoutLearningArea();
			loginPage = new NewUXLoginPage(webDriver, testResultService);
			testResultService.assertEquals(false, loginPage.isSubmitButtonEnabled(), "Checking that ED Login Page displayed");
					
			report.startStep("Login as teacher, add comment, rate and send feedback");
			String commentText = enterTMSandSendFeedbackOnWriting(studentUserNameER, courseIndex, rate);
					
			report.startStep("Login as student");
			loginPage.loginAsStudent(studentUserNameER,"12345");
			homePage.waitHomePageloaded();
			
			report.startStep("Navigate to E-Rater Task in B2-U1-L2");
			homePage.navigateToCourseUnitAndLesson(courseCodes, "B2", 1, 2);
			sleep(2);
			learningArea.clickOnStep(2);
			sleep(2);
			learningArea.clickOnTask(4);
			sleep(4);
		
			report.startStep("Click on See Feedback btn");
			learningArea.clickOnOKButton();
			homePage.switchToFrameInModal();
			sleep(1);
			
			report.startStep("Validate Statistics Pop Up");
			assignPage.validateStatisticsPopUp(wordsCountLA, false);
			
			report.startStep("Click on More Details button");
			assignPage.clickOnFeedbackMoreDetails();
			sleep(1);
			
			report.startStep("Verify Menu Features");
			expSubDate = pageHelper.getCurrentDateByFormat("dd.MM.yyyy");
			verifyMenuFeaturesInERFeedback(wordsCountLA, resourceText1, expSubDate, false);
					
			report.startStep("Close Feedback Page Menu");		
			assignPage.clickOnFeedbackPageMenu(); 
						
			report.startStep("Select teachers comment and verify text");	
			assignPage.checkForTeacherCommentById("tComments", commentText);
											
			report.startStep("Close Feedback Page");
			webDriver.switchToTopMostFrame();
			learningArea.closeModalPopUp();
		}
		catch(Exception e)
		{
			bTestFailed = true;
			report.reportFailure(e.getMessage());
		}
	}
		
	private String sendTeacherFeedback(boolean clickOnContinue, int rate) throws Exception {
		
		tmsHomePage.clickOnXFeedback();
		sleep(1);
				
		tmsHomePage.checkAddCommentButtonStatus(true);
		
		tmsHomePage.clickAddCommentButton();
		sleep(1);
		
		tmsHomePage.clickOnTextArea(20, 20);
		sleep(1);
		
		String commentText = "comment" + dbService.sig(5);
		
		tmsHomePage.enterTeacherCommentText(commentText);
		sleep(1);
		
		tmsHomePage.clickAddCommentDoneButton();
		sleep(1);
		
		tmsHomePage.clickOnAssignmentSummary();
		sleep(2);
		
		if (clickOnContinue) {
			tmsHomePage.clickOnTeacherFeedbackContinueButton();
			sleep(2);
		}
		
		tmsHomePage.clickOnRateAssignmentButton();
		sleep(1);
		
		tmsHomePage.rateAssignment(rate);
		sleep(2);
		
		tmsHomePage.clickOnApproveAssignmentButton();
		sleep(4);
				
		tmsHomePage.sendFeedback();
		sleep(3);
		webDriver.closeAlertByAccept(2);
		tmsHomePage.closeStudentEraterAssignment();
		
		webDriver.printScreen("After clicking send to all");
		
		eraterService.checkWritingIsReviewed(newWritingId);

		return commentText;
	}
	
	private void verifyMenuFeaturesInERFeedback(String expWordsCount, String expResourceText, String expSubDate, Boolean isFirstFeedback) throws Exception {
		
		report.startStep("Verify General Feedback in Menu");
		assignPage.clickOnFeedbackPageMenu();
		sleep(1);
		assignPage.clickOnGeneralFeedbackInMenu();
		sleep(2);
		assignPage.validateStatisticsInGeneralFeedback(expWordsCount, isFirstFeedback);
		assignPage.closeItemInMenu();
		
		report.startStep("Verify Explore Text in Menu");
		assignPage.clickOnFeedbackPageMenu();
		sleep(1);
		assignPage.clickOnExploreTextInMenu();
		sleep(1);
		// TODO bug reported - uncomment this step when bug fixed
		// testResultService.assertEquals(expResourceText, assignPage.getExploreTextInMenu(), "Resource texts do not match: learning area & e-rater feedback page");
		// Bug 39218: E-Rater / Missing Title of Original Explore Text in Feedback Page (My Writing Assignments)
		
		assignPage.closeItemInMenu();
		
		report.startStep("Verify Previous Versions");
		assignPage.clickOnFeedbackPageMenu();
		sleep(1);
		assignPage.clickOnPrevVersionsInMenu();
		sleep(1);
		assignPage.verifyVersionsDetails(isFirstFeedback, expSubDate);
		assignPage.clickOnPrevVersionsInMenu();
		
		report.startStep("Verify Printed Versions");		
		//assignPage.clickOnPrinterVersionInMenu(); 
	}
	
	private String enterTMSandSendFeedbackOnWriting(String studentName, int courseIndex, int rate) throws Exception {
		
		report.startStep("Login as Teacher");
		sleep(10);  // E-rater takes a few more sec than always to process request.
		tmsHomePage = loginPage.enterTeacherUserAndPassword();
		sleep(10);
		tmsHomePage.switchToMainFrame();
		
		report.startStep("Open student's assignment");
		tmsHomePage.clickOnWritingAssignments();
		sleep(2);
		tmsHomePage.clickOnStudentAssignment(studentName, coursesNames[courseIndex]);
		sleep(2);
		
		report.startStep("Add teacher's comment, rate and submit feedback");
		String commentText = sendTeacherFeedback(false, rate);
		sleep(2);
		
		report.startStep("Logout as teacher");
		webDriver.switchToMainWindow();
		tmsHomePage.switchToMainFrame();
		tmsHomePage.clickOnExit();
		testResultService.assertEquals(false, loginPage.isSubmitButtonEnabled(), "Checking that ED Login Page displayed");	
		
		return commentText;
	}
	
	
	@After
	public void tearDown() throws Exception {
		
		if(!bTestFailed)
		{
			report.startStep("Set progress to first FD course item");
			studentService.createSingleProgressRecored(studentId,"20000", "31515", 10, false, 1);
		}
		
		super.tearDown();
	}	
}
	
