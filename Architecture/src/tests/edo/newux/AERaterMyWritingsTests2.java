package tests.edo.newux;

import Interfaces.TestCaseParams;
import Objects.Course;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import pageObjects.edo.NewUXLoginPage;
import pageObjects.edo.NewUxAssignmentsPage;
import pageObjects.edo.NewUxLearningArea2;
import pageObjects.tms.TmsHomePage;
import testCategories.edoNewUX.ReleaseTests;
import testCategories.edoNewUX.SanityTests;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


public class AERaterMyWritingsTests2 extends BasicNewUxTest {

	
	NewUXLoginPage loginPage;
	NewUxLearningArea2 learningArea2;
	NewUxAssignmentsPage assignPage; 
    TmsHomePage tmsHomePage;	
	
	List<Course> courses = null;
	List<String> writingIdForDelete = new ArrayList<String>();
	
	String writingId = null;
	String newWritingId = null;
	private static final String erater_task_instruction = "Type your answer in the text box and click submit.";

//--igb 2018.08.12-------------	
	private static final String textFile = "files/assayFiles/text2.txt";
	
	boolean bTestFailed = false;  // igb 2018.08.06

	@Before
	public void setup() throws Exception {
		//institutionName=institutionsName[19];
		super.setup();
		
	}

	@Test
	@TestCaseParams(testCaseID = "40884", skippedBrowsers = {"firefox"})
	public void testMyAssigmentAlert()throws Exception {

		bTestFailed = false;  // igb 2018.08.06
		
		try {
			report.startStep("Init test data");
			int courseIndex = 1;
			int unitNumber = 7;
			int lessonNumber = 1;
			int stepNumber = 2;
			int taskNumber = 5;
			
			report.startStep("Using file: " + textFile);
				
			report.startStep("Create and login to ED as student");
				String classNameER = configuration.getProperty("classname.openSpeech");
				//String institutionId = institutionId; //configuration.getInstitutionId();
				studentId = pageHelper.createUSerUsingSP(institutionId, classNameER);
				String studentUserNameER = dbService.getUserNameById(studentId, institutionId);
						
				loginPage = new NewUXLoginPage(webDriver,testResultService);
				homePage = loginPage.loginAsStudent(studentUserNameER, "12345");
				homePage.closeAllNotifications();
				homePage.waitHomePageloadedFully();
				
			report.startStep("Navigate to E-Rater Task in B2-U1-L2");
				learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, courseCodes[courseIndex], unitNumber, lessonNumber);
				sleep(1);
				learningArea2.clickOnStep(stepNumber);
				learningArea2.clickOnTaskByNumber(taskNumber);
				sleep(1);
		
			report.startStep("Get header & left side text");
				//String header1 = learningArea2.getLessonNameFromHeader();
				//String resourceText1 = learningArea2.checkThatReadingTextIsDisplayed2();
				//sleep(2);
				
			report.startStep("Verify instruction text");
				report.report("Component cookie is: " + webDriver.getCookie("Component"));
				report.report("Course cookie is: " + webDriver.getCookie("Course"));
				report.report("Erater cookie is: " + webDriver.getCookie("ERater"));
				learningArea2.VerificationOfQuestionInstruction(erater_task_instruction);
						
			report.startStep("Submit text with unique label");
			String uniquelabel= submitTextToWritingArea();

			report.startStep("Check Submit btn is disabled");
				learningArea2.checkThatSubmitEraterBtnIsDisabled();
				
			report.startStep("Check automation evaluation attempt indication");
				String attemptNum = learningArea2.getAttemptNumberOfAutomatedEvaluation();
				testResultService.assertEquals("1", attemptNum, "Current attempt count not correct");
				
			report.startStep("Get words count");
				String wordsCountLA = learningArea2.getERaterWordsCountOnSubmit();
				
			report.startStep("Checking the xml and json");
				writingId = eraterService.getWritingIdByUserIdAndTextStart(studentId, uniquelabel);
				writingIdForDelete.add(writingId);
				//sleep(5);
		
			report.startStep("Open and Close My Assignment Page and check assigment alert on");
				waitTillEaraterProcessed();
				sleep(3);
				learningArea2.clickToOpenNavigationBar();
				sleep(3);
				assignPage = learningArea2.openAssignmentsPage(false);
				sleep(5);
				assignPage.close();
				sleep(5);
				learningArea2.waitToAssignmentAlert();
				
			report.startStep("Open My Assignment Page and select Course with latest assignment");
				assignPage = learningArea2.openAssignmentsPage(false);
				sleep(3);
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
				learningArea2.clickOnOKButton();
				assignPage.clickOnFeedbackSubmitBtn();
				assignPage.close();
				learningArea2.withOutAlert(true);
		}
		catch(Exception e)
		{
			bTestFailed = true;
			report.reportFailure(e.getMessage());
		}
	}

	private String submitTextToWritingArea() throws Exception {

		String assayText = textService.getTextFromFile(textFile, Charset.defaultCharset());

		String uniquelabel = "";

		for(int i = 0; i < 8; i++) {
			uniquelabel += (char)(Math.random() * 26 + 97);
		}

		String textToSubmit = uniquelabel + " " + assayText;
		learningArea2.submitTextToERater(textToSubmit);

		return uniquelabel;
	}

	@Test
	@TestCaseParams(testCaseID = "27019", skippedBrowsers = {"firefox"})
	public void testERaterAssignmentsPageFlow() throws Exception {

		bTestFailed = false;  // igb 2018.08.06
		
		try {
			report.startStep("Init test data");
			int courseIndex = 2;
			int unitNumber = 1;
			int lessonNumber = 2;
			int stepNumber = 2;
			int taskNumber = 5;
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
			homePage.closeAllNotifications();
			homePage.waitHomePageloadedFully();

			report.startStep("Navigate to E-Rater Task in B2-U1-L2");
			learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, courseCodes[courseIndex], unitNumber, lessonNumber);
			sleep(1);
			learningArea2.clickOnStep(stepNumber);
			learningArea2.clickOnTaskByNumber(taskNumber);
			sleep(1);
			
			report.startStep("Get header & left side text");
			String header1 = learningArea2.getLessonNameFromHeader();
			String resourceText1 = learningArea2.checkThatReadingTextIsDisplayed();
		
			report.startStep("Verify instruction text");
			learningArea2.VerificationOfQuestionInstruction(erater_task_instruction);
					
			report.startStep("Submit text with unique label");
			String assayText = textService.getTextFromFile(textFile, Charset.defaultCharset());
							
			String uniquelabel = "";
			
			for(int i = 0; i < 8; i++) {
			   	uniquelabel += (char)(Math.random() * 26 + 97);
			}
			
			String textToSubmit = uniquelabel + " " + assayText;
			learningArea2.submitTextToERater(textToSubmit);
			
			report.startStep("Check Submit btn is disabled");
			learningArea2.checkThatSubmitEraterBtnIsDisabled();
			
			report.startStep("Check automation evaluation attempt indication");
			String attemptNum = learningArea2.getAttemptNumberOfAutomatedEvaluation();
			testResultService.assertEquals("1", attemptNum, "Current attempt count not correct");
		
			report.startStep("Get words count");
			String wordsCountLA = learningArea2.getERaterWordsCountOnSubmit();
			
			report.startStep("Checking the xml and json");
			writingId = eraterService.getWritingIdByUserIdAndTextStart(studentId, uniquelabel);
			writingIdForDelete.add(writingId);
			//report.report("writing id is: " + writingId);
			//eraterService.compareJsonAndXmlByWritingId(writingId);
			
			report.startStep("Open My Assignment Page and select Course with latest assignment");
			learningArea2.clickToOpenNavigationBar();
			for (int i = 0 ; i < 120 ; i++) { // omz 6.5.2019
				if (dbService.getLastestEraterProcessedStatus(studentId).equals("1")) {
					sleep(2);
					break;
				}
				else 
					sleep(1);
			}
			//sleep(10);
			assignPage = learningArea2.openAssignmentsPage(false);
			sleep(5);
			assignPage.clickOnMyWritingsTab(true);
			assignPage.selectCourseInMyWritings(coursesNames[courseIndex], true);
			
			//report.startStep("Verify writing status"); -- igb 2018.10.09 --> close as not actual report
			//assignPage.verifyWritingStatus(WritingStatus.autoFeedback, expSubDate, true);
			
			report.startStep("Click on See Feedback link");
			assignPage.clickOnSeeFeedbackTryAgainInMyWritings();
			sleep(1);
			assignPage.switchToFeedbackFrameInMyWritings();
			
			report.startStep("Validate Statistics Pop Up");
			assignPage.validateStatisticsPopUp(wordsCountLA, true);
				
			report.startStep("Click on More Details button");
			assignPage.clickOnFeedbackMoreDetails();
			sleep(1);
				
			report.startStep("Verify Menu Features");
			expSubDate = pageHelper.getCurrentDateByFormat("dd.MM.yyyy");
			learningArea2.clickOnOKButton();
			verifyMenuFeaturesInERFeedback(wordsCountLA, resourceText1, expSubDate, true);
					
			report.startStep("Edit Text and Submit Again");
			String newText = " this is new text";
			assignPage.editFeedbackAssignmentText(newText, true);
			wordsCountLA = assignPage.getERaterWordsCountOnSubmit();
			assignPage.clickOnFeedbackSavedraftBtn();
			assignPage.clickOnFeedbackSubmitBtn();
			sleep(3);
			
			report.startStep("Verify writing status");
			expSubDate = pageHelper.getCurrentDateByFormat("dd/MM/yyyy");
			//assignPage.verifyWritingStatus(WritingStatus.submitted, expSubDate, false);
			
			report.startStep("Wait for writing to be processed again");
			newWritingId = eraterService.getWritingIdByUserIdAndTextStart(studentId, newText, true);
		
			report.startStep("Press on View link and check redirection to original lesson");
			assignPage.clickOnViewInMyWritings();
			sleep(2);
			String header2 = learningArea2.getLessonNameFromHeader();
			learningArea2.clickOnNextButton();
			String resourceText2 = learningArea2.checkThatReadingTextIsDisplayed();
			testResultService.assertEquals(header1, header2, "Redirected to wrong lesson after View pressed");
			testResultService.assertEquals(resourceText1, resourceText2, "Redirected to wrong lesson after View pressed");
			sleep(10);		
			report.startStep("Navigate to E-Rater task and check message");				
			learningArea2.clickOnStep(stepNumber);
			learningArea2.clickOnTaskByNumber(taskNumber);
			sleep(1);
			//learningArea2.validateAlertModalByMessage(learningArea2.E_Rater_Confirmation_3, true);
			
			report.startStep("Check Submit btn is disabled");
			learningArea2.checkThatSubmitEraterBtnIsDisabled();
			
			report.startStep("Check automation evaluation attempt indication");
			attemptNum = learningArea2.getAttemptNumberOfAutomatedEvaluation();
			testResultService.assertEquals("2", attemptNum, "Current attempt count not correct");
		
			report.startStep("Logout");
			learningArea2.clickOnLogoutLearningArea();
			loginPage = new NewUXLoginPage(webDriver, testResultService);
			testResultService.assertEquals(false, loginPage.isSubmitButtonEnabled(), "Checking that ED Login Page displayed");
					
			report.startStep("Login as teacher, add comment, rate and send feedback");
			for (int i = 0 ; i < 120 ; i++) { // omz 16.9.2018
				if (dbService.getLastestEraterProcessedStatus(studentId).equals("1")) {
					sleep(2);
					break;
				}
				else 
					sleep(1);
			}
			String commentText = enterTMSandSendFeedbackOnWriting(studentUserNameER, courseIndex, rate);
			
			report.startStep("Login as student");
			loginPage.loginAsStudent(studentUserNameER,"12345");
			homePage.waitHomePageloaded();
			
			report.startStep("Open My Assignment Page and select Course with latest assignment");
			assignPage = learningArea2.openAssignmentsPage(false);
			sleep(3);
			assignPage.clickOnMyWritingsTab(true);
			assignPage.selectCourseInMyWritings(coursesNames[courseIndex], true);
			
			report.startStep("Verify writing status");
			//assignPage.verifyWritingStatus(WritingStatus.teacherFeedback, expSubDate, true);
				
			report.startStep("Click on See Feedback link");
			assignPage.clickOnSeeFeedback();
			sleep(1);
			assignPage.switchToFeedbackFrameInMyWritings();
			sleep(1);
			assignPage.checkRatingFromTeacher(rate, false);
					
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
			
			report.startStep("Close Feedback page");	
			assignPage.closeWritingFeedbackPage();
			
			report.startStep("Verify writing status");
			expSubDate = pageHelper.getCurrentDateByFormat("dd/MM/yyyy");
			webDriver.switchToMainWindow();
			homePage.switchToFrameInModal();
		//	assignPage.verifyWritingStatus(WritingStatus.teacherFeedback, expSubDate, false);
							
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
	
	@Test
	@TestCaseParams(testCaseID = "26211", skippedBrowsers = {"firefox"})
	public void testERaterTaskPageFlow() throws Exception {

		bTestFailed = false;  // igb 2018.08.06
		
		try {
			report.startStep("Init test data");
			int courseIndex = 2;
			int unitNumber = 1;
			int lessonNumber = 2;
			int stepNumber = 2;
			int taskNumber = 5;
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
			homePage.closeAllNotifications();
			homePage.waitHomePageloadedFully();
			
			report.startStep("Navigate to E-Rater Task in B2-U1-L2");
			learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, courseCodes[courseIndex], unitNumber, lessonNumber);
			
			learningArea2.clickOnStep(stepNumber);
			sleep(2);
			learningArea2.clickOnTaskByNumber(taskNumber);
			sleep(2);
		
			report.startStep("Get header & left side text");
			String resourceText1 = learningArea2.checkThatReadingTextIsDisplayed();
			
			report.startStep("Verify instruction text");
			learningArea2.VerificationOfQuestionInstruction(erater_task_instruction);
			//sleep(1);
			report.startStep("Submit text with unique label");
			String assayText = textService.getTextFromFile(textFile, Charset.defaultCharset());
							
			String uniquelabel = "";
			
			for(int i = 0; i < 8; i++) {
			   	uniquelabel += (char)(Math.random() * 26 + 97);
			}
			
			String textToSubmit = uniquelabel + " " + assayText;
			learningArea2.submitTextToERater(textToSubmit);
			
			report.startStep("Check Submit btn is disabled");
			learningArea2.checkThatSubmitEraterBtnIsDisabled();
		
			report.startStep("Check automation evaluation attempt indication");
			String attemptNum = learningArea2.getAttemptNumberOfAutomatedEvaluation();
			testResultService.assertEquals("1", attemptNum, "Current attempt count not correct");
			
			report.startStep("Get words count");
			String wordsCountLA = learningArea2.getERaterWordsCountOnSubmit();
			
			report.startStep("Checking the xml and json");
			writingId = eraterService.getWritingIdByUserIdAndTextStart(studentId, uniquelabel);
			//writingIdForDelete.add(writingId);
			//report.report("writing id is: " + writingId);
			//eraterService.compareJsonAndXmlByWritingId(writingId);
					
			report.startStep("Navigate out and back to E-Rater task");
			learningArea2.clickOnStep(stepNumber-1);
			sleep(2);
			learningArea2.clickOnStep(stepNumber);
			sleep(1);
			for (int i = 0 ; i < 180 ; i++) {  // omz 6.5.2019
				if (dbService.getLastestEraterProcessedStatus(studentId).equals("1"))
				{
					sleep(1);
					break;
				}
				else 
					sleep(1);
			}
			learningArea2.clickOnTaskByNumber(taskNumber);
			sleep(1);
		
			report.startStep("Click on See Feedback btn and switch to frame");
			learningArea2.clickOnCancel();
			learningArea2.clickOnSeeFeedbackInERater();
			homePage.switchToFrameInModal();
					
			report.startStep("Validate Statistics Pop Up");
			assignPage = new NewUxAssignmentsPage(webDriver, testResultService);
			sleep(1);
			assignPage.validateStatisticsPopUp(wordsCountLA, true);
					
			report.startStep("Click on More Details button");
			assignPage.clickOnFeedbackMoreDetails();
			sleep(1);
			
			report.startStep("Verify Menu Features");
			expSubDate = pageHelper.getCurrentDateByFormat("dd.MM.yyyy");
			learningArea2.clickOnOKButton();
			verifyMenuFeaturesInERFeedback(wordsCountLA, resourceText1, expSubDate, true);
				
			report.startStep("Edit Text and Submit Again");
			String newText = " this is new text";
			assignPage.editFeedbackAssignmentText(newText, false);
			wordsCountLA = assignPage.getERaterWordsCountOnSubmit();
			assignPage.clickOnFeedbackSubmitInLearningArea();
			//webDriver.printScreen("Before final approval");
			learningArea2.closeConfirmAlertModalByAccept();
			Thread.sleep(2000);
			webDriver.closeAlertByAccept();
			Thread.sleep(1000);
			learningArea2.clickOnOKButton();
			webDriver.switchToMainWindow();
			//learningArea2.closeAlertModalByAccept();
			Thread.sleep(2000);
			/*learningArea2.clickOnSeeFeedbackInERater();
			report.startStep("Wait for writing to be processed again");
			newWritingId = eraterService.getWritingIdByUserIdAndTextStart(studentId, newText, true);*/
					
			report.startStep("Check Submit btn is disabled");
			learningArea2.checkThatSubmitEraterBtnIsDisabled();
			
			report.startStep("Check automation evaluation attempt indication");
			attemptNum = learningArea2.getAttemptNumberOfAutomatedEvaluation();
			testResultService.assertEquals("2", attemptNum, "Current attempt count not correct");
			
			report.startStep("Logout");
			learningArea2.clickOnLogoutLearningArea();
			loginPage = new NewUXLoginPage(webDriver, testResultService);
			testResultService.assertEquals(false, loginPage.isSubmitButtonEnabled(), "Checking that ED Login Page displayed");
			sleep(3);
			report.startStep("Login as teacher, add comment, rate and send feedback");
			String commentText = enterTMSandSendFeedbackOnWriting(studentUserNameER, courseIndex, rate);
					
			report.startStep("Login as student");
			loginPage.loginAsStudent(studentUserNameER,"12345");
			homePage.waitHomePageloadedFully();
		
			report.startStep("Navigate to E-Rater Task in B2-U1-L2");
			homePage.navigateToRequiredCourseOnHomePage(coursesNames[courseIndex]);
			learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, courseCodes[courseIndex], unitNumber, lessonNumber);
			sleep(1);
			learningArea2.clickOnStep(stepNumber);
			sleep(1);
			learningArea2.clickOnTaskByNumber(taskNumber);
			sleep(1);
			
			report.startStep("Click on See Feedback btn");
			learningArea2.clickOnCancel();
			learningArea2.clickOnSeeFeedbackInERater();
			homePage.switchToFrameInModal();
							
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
			learningArea2.closeModalPopUp();
		}
		catch(Exception e)
		{
			bTestFailed = true;
			testStatus="Failed";
			report.reportFailure(e.getMessage());
		}
	}
	
	// E-Rater full cycle

	@Test
	@Category({ReleaseTests.class,SanityTests.class})
	//@Category({ReleaseTests.class)
	@TestCaseParams(testCaseID = "40880", skippedBrowsers = {"firefox"})
	public void testERaterFullCycle() throws Exception {

		bTestFailed = false;  // igb 2018.08.06
		
		/*
		institutionName = institutionsName[20]; // qa31
		pageHelper.initializeData();
		pageHelper.restartBrowserInNewURL(institutionName, true);
		*/
		
		try {
			report.startStep("Init test data");
			int courseIndex = 1;
			int unitNumber = 7;
			int lessonNumber = 1;
			int stepNumber = 2;
			int taskNumber = 5;
			int rate = 1;
			
			String expSubDate = pageHelper.getCurrentDateByFormat("dd/MM/yyyy");
		
			report.startStep("Using file: " + textFile);
				
			report.startStep("Create and login to ED as student");
				String classNameER = configuration.getProperty("classname.openSpeech");
				studentId = pageHelper.createUSerUsingSP(institutionId, classNameER);
				String studentUserNameER = dbService.getUserNameById(studentId, institutionId);
						
				loginPage = new NewUXLoginPage(webDriver,testResultService);
				homePage = loginPage.loginAsStudent(studentUserNameER, "12345");
				homePage.closeAllNotifications();
				homePage.waitHomePageloadedFully();

				
			report.startStep("Navigate to E-Rater Task in B2-U1-L2");
				learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, courseCodes[courseIndex], unitNumber, lessonNumber);
				learningArea2.waitToLearningAreaLoaded();
				learningArea2.clickOnStep(stepNumber);
				learningArea2.clickOnTaskByNumber(taskNumber);
				sleep(1);
		
			report.startStep("Get header & left side text");
				String resourceText1 = learningArea2.checkThatReadingTextIsDisplayed2();
				
			report.startStep("Verify instruction text");
				learningArea2.VerificationOfQuestionInstruction(erater_task_instruction);
						
			report.startStep("Submit text with unique label");
				String assayText = textService.getTextFromFile(textFile, Charset.defaultCharset());
				String uniquelabel = textService.getRandomString(8);
				String textToSubmit = uniquelabel + " " + assayText;
				//sleep(1);
				learningArea2.submitTextToERater(textToSubmit);
		
			report.startStep("Check Submit btn is disabled");
				learningArea2.checkThatSubmitEraterBtnIsDisabled();
				
			report.startStep("Check automation evaluation attempt indication");
				String attemptNum = learningArea2.getAttemptNumberOfAutomatedEvaluation();
				testResultService.assertEquals("1", attemptNum, "Current attempt count not correct");
				
			report.startStep("Get words count");
				String wordsCountLA = learningArea2.getERaterWordsCountOnSubmit();
				
			report.startStep("Checking the xml and json");
				writingId = eraterService.getWritingIdByUserIdAndTextStart(studentId, uniquelabel);
		
			report.startStep("Open and Close My Assignment Page and check assigment alert on");
				learningArea2.clickToOpenNavigationBar();
				waitTillEaraterProcessed();
				
				assignPage = learningArea2.openAssignmentsPage(false);
				Thread.sleep(4000); // it necessary to wait till the Alert will come from server and not only the element loaded
				assignPage.close();
				
				learningArea2.waitToAssignmentAlert();
				//learningArea2.clickToOpenNavigationBar();
				assignPage = learningArea2.openAssignmentsPage(false);
				assignPage.clickOnMyWritingsTab(true);
				assignPage.selectCourseInMyWritings(coursesNames[courseIndex], true);
		
			report.startStep("Click on See Feedback link");
				assignPage.clickOnSeeFeedbackTryAgainInMyWritings();
				Thread.sleep(1000);
				assignPage.switchToFeedbackFrameInMyWritings();
				
			report.startStep("Validate Statistics Pop Up");
				assignPage.validateStatisticsPopUp(wordsCountLA, true);
						
			report.startStep("Click on More Details button");
				assignPage.clickOnFeedbackMoreDetails();
				sleep(1);
			
			report.startStep("Verify Menu Features");
				expSubDate = pageHelper.getCurrentDateByFormat("dd.MM.yyyy");
				learningArea2.clickOnOKButton();
				verifyMenuFeaturesInERFeedback(wordsCountLA, resourceText1, expSubDate, true);
				
			report.startStep("Edit Text and Submit Again");
				String newText = " this is new text";
				assignPage.editFeedbackAssignmentText(newText, true);
				wordsCountLA = assignPage.getERaterWordsCountOnSubmit();
				assignPage.clickOnFeedbackSubmitBtn();
				sleep(3);
				assignPage.close();
				learningArea2.withOutAlert(true);
				learningArea2.clickToOpenNavigationBar();
				
			report.startStep("Wait for writing to be processed again");
				newWritingId = eraterService.getWritingIdByUserIdAndTextStart(studentId, newText, true);
						
			report.startStep("Check Submit btn is disabled");
				learningArea2.checkThatSubmitEraterBtnIsDisabled();
				
			report.startStep("Logout");
				learningArea2.clickOnLogoutLearningArea();
				loginPage = new NewUXLoginPage(webDriver, testResultService);
				testResultService.assertEquals(false, loginPage.isSubmitButtonEnabled(), "Checking that ED Login Page displayed");
				
			report.startStep("Login as teacher, add comment, rate and send feedback");
				//waitTillEaraterProcessed();
				String commentText = enterTMSandSendFeedbackOnWriting(studentUserNameER, courseIndex, rate);
						
			report.startStep("Login as student");
				loginPage.loginAsStudent(studentUserNameER,"12345");
				sleep(1);
				homePage.closeAllNotifications();
				homePage.waitHomePageloadedFully();
				learningArea2.withOutAlert(true);
				
			report.startStep("Navigate to E-Rater Task in B2-U1-L2");
				homePage.navigateToRequiredCourseOnHomePage(coursesNames[courseIndex]);
				learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, courseCodes[courseIndex], unitNumber, lessonNumber);

				learningArea2.clickOnCancel();
				//learningArea2.clickOnOKButton();
				learningArea2.clickOnSeeFeedbackInERater();
				homePage.switchToFrameInModal();
						
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
				//assignPage.checkForTeacherCommentById("t0_1_0", commentText);
				assignPage.checkForTeacherCommentById("tComments", commentText);
												
			report.startStep("Close Feedback Page");
				webDriver.switchToTopMostFrame();
				learningArea2.closeModalPopUp();
		}
		catch(Exception e)
		{
			bTestFailed = true;
			testStatus="Failed";
			report.reportFailure(e.getMessage());
		}
	}
		
	private void waitTillEaraterProcessed() throws Exception {
		boolean result = false;

		for (int i = 0 ; i <180 && !result; i++) {
			try {
				result=dbService.getLastestEraterProcessedStatus(studentId).equals("1");
				sleep(1);

			} catch (Exception e) {
				e.printStackTrace();
			}
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
		
		// click on Sample rate (rate)
		// switch if needed to new div
		// compare text or check it's not empty
		// close div and switch back to main div if needed
		
		
		tmsHomePage.rateAssignment(rate);
		sleep(2);
		
		tmsHomePage.clickOnApproveAssignmentButton();
		sleep(2);
				
		tmsHomePage.sendFeedback();
		sleep(1);
		webDriver.closeAlertByAccept(2);
		tmsHomePage.closeStudentEraterAssignment();
		
		webDriver.printScreen("After clicking send to all");
		
		//eraterService.checkWritingIsReviewed(newWritingId);

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
		pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(configuration.getProperty("teacher.username"),institutionId));
		tmsHomePage = loginPage.enterTeacherUserAndPassword();
		//pageHelper.skipOptin();
		homePage.closeAllNotifications();
		tmsHomePage.waitForPageToLoad();
		
		//sleep(3);
		tmsHomePage.switchToMainFrame();
		//sleep(3);
		report.startStep("Open student's assignment");
		tmsHomePage.clickOnWritingAssignments();
		sleep(10);

		report.startStep("refresh the page by click n GO, sometimes the date not received");
		clickOnGoAndGetTheStudentAssignment();

		report.startStep("click on Sort and student assignment");
		tmsHomePage.clickOnStudentAssignment(studentName, coursesNames[courseIndex]);
		sleep(3);
		
		report.startStep("Add teacher's comment, rate and submit feedback");
		String commentText = sendTeacherFeedback(false, rate);
		sleep(1);
		
		report.startStep("Logout as teacher");
		webDriver.switchToMainWindow();
		tmsHomePage.switchToMainFrame();
		tmsHomePage.clickOnExit();
		boolean status = loginPage.waitAndCheckEDLoginLoaded();
		testResultService.assertEquals(true, status, "Checking that ED Login Page displayed");
		
		return commentText;
	}

	private void clickOnGoAndGetTheStudentAssignment() throws Exception {

		webDriver.switchToTopMostFrame();
		tmsHomePage.switchToMainFrame();
		tmsHomePage.switchToFormFrame();
		tmsHomePage.clickOnGo();
		sleep(10);
		webDriver.switchToTopMostFrame();
		tmsHomePage.switchToMainFrame();
	}


	@After
	public void tearDown() throws Exception {

		//if(!bTestFailed)
		//{
			//report.startStep("Set progress to first FD course item");
			//studentService.createSingleProgressRecored(studentId,"20000", "31515", 10, false, 1);
		//}
		
		super.tearDown();
	}	
}
	
