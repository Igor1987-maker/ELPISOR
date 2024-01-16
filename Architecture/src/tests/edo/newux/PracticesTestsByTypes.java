package tests.edo.newux;


import java.util.Date;
import java.util.List;

//import net.sf.uadetector.internal.data.domain.Browser;




import org.apache.tools.ant.types.resources.selectors.InstanceOf;
import org.hsqldb.jdbc.jdbcBlob;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import drivers.ChromeWebDriver;
import drivers.FirefoxWebDriver;
import pageObjects.InteractSection;
import pageObjects.edo.NewUXLoginPage;
import pageObjects.edo.NewUxAssignmentsPage;
import pageObjects.edo.NewUxDragAndDropSection;
import pageObjects.edo.NewUxLearningArea;
import pageObjects.edo.NewUxMessagesPage;
import pageObjects.tms.StudentRecordingsPage;
import pageObjects.tms.TeacherReadMessagePage;
import pageObjects.tms.TeacherWriteMessagePage;
import pageObjects.tms.TmsHomePage;
import testCategories.NonAngularLearningArea;
import testCategories.inProgressTests;
import testCategories.unstableTests;
import Enums.InteractStatus;
import Interfaces.TestCaseParams;
import Objects.Recording;

@Category(NonAngularLearningArea.class)
public class PracticesTestsByTypes extends BasicNewUxTest {

	NewUxLearningArea learningArea;
	NewUxDragAndDropSection dragAndDrop;
	NewUXLoginPage loginPage;
	NewUxMessagesPage inboxPage;
	TmsHomePage tmsHomePage;
	TeacherReadMessagePage tRead;
	TeacherWriteMessagePage tWrite;
	
	boolean bTestFailed = false;  // igb 2018.08.07
		
	private static final String freeWriteAlertAfterSubmit = "Your answer has been submitted to your teacher.";
	private static final String closeDragAndDropInstruction = "Listen to the clips of the news program, and find the missing words.";
	private static final String closeDragAndDropTest = "Complete this e-mail from Ella to her mother. Use the words below to complete the sentences.";
	private static final String openEndedSegmentsInstruction = "Type the sentences that you hear in the dictation. Pay attention to punctuation.";
	private static final String openEndedWritingInstruction = "Rewrite the sentences using the words in parentheses.";
	private static final String markTheTrueInstruction = "Read the ad for Village Tours, and then answer the question.";
	private static final String fillInTheBlanksSelectionInstruction = "Listen to the clips of the radio program, and complete the sentences.";
	private static final String  fillInTheBlanksWithImage = "Fill in the blank/s with the correct answer/s.";
	private static final String fillInTheBlanksTest = "Complete the paragraph about Rachel's job. Choose the correct words from the drop-downs.";
	private static final String mcqSelectionInstruction = "Listen to the clips of the ad, and answer the questions.";
	private static final String matchingDragAndDropInstruction = "Listen to the news about President Stirling and collocate the words taken from the review.";
	private static final String matchingDragAndDropTestMode ="Complete the collocations. Match the words below to the correct words in column A.";
	private static final String closeDragAndDropMediaInstruction = "Complete the conversation. Drag the correct answers into place.";
	private static final String classificationDragAndDropInstruction = "After you have read the story about Cindy, put the phrases about each person into the table.";
	private static final String alphabetQuestionInstruction = "Match the capital letters to the small letters.";
	private static final String SequenceSentenceQuestionInstruction = "Read Bill's letter about his stay in Portugal. Then put the events into the right order.";
	private static final String SequenceSentenceTestMode = "Put the sentences in the correct order to make a dialogue.";
	private static final String SequenceImageQuestionInstruction = "Read the story \"Mystery Girl.\" Then show what happened by putting the pictures into the correct order.";
	private static final String FreeWriteQuestionInstruction = "Write your own application form and send your answer to your teacher.";
	private static final String matchTextToPicDragAndDropInstruction = "Drag the word to the correct letter.";
	private static final String matchTextToPicDragAndDropTestMode = "Match the words and phrases from the lecture to the correct pictures.";
	private static final String closeWholeBmpDragAndDropInstruction = "Listen and drag the correct answer/s into place.";
	private static final String openEndedSmallSegmentsInstruction = "Ray is writing to his friend Kevin about the radio interview. Complete Ray's email.";
	private static final String insertTextInstruction = "Select the place in the text where this sentence best fits.";
	private static final String insertTextAnswerSentence = "\"When you jump for half an hour, you burn about 160 calories.\"";
	private static final String editTextInstruction = "The following activity will help you get ready to write. Correct this paragraph about a way to use kinetic energy. The errors are highlighted in the paragraph on the left side of the screen. Correct the errors by typing in the boxes on the right side of the screen.";
	private static final String highlightTextInstruction = "Choose the correct answer.";
	private static final String selectTextInstruction = "According to the text, choose the place Ted Benson worked as an accounts executive.";
	private static final String openWritingtInstruction = "Type your answer in the text box and click submit.";
	private static final String openWritingConfirmationAlert = "Your answer has been submitted to your teacher.";
	private static final String classificationDragAndDropTestInstruction = "Which words are activities? Which are places to do activities? Complete the table.";
			
	List<WebElement> markers;
	List<WebElement> content;
	List<WebElement> rightContentInputsElements;
	List<WebElement> leftContentInputsElements;
	List<WebElement> highlightedText;
	List<WebElement> highlightedAnswer;
	
	
	@Before
	public void setup() throws Exception {
		super.setup();
		
		learningArea = new NewUxLearningArea(webDriver, testResultService);
		dragAndDrop = new NewUxDragAndDropSection(webDriver, testResultService);
		
		report.startStep("Get user and login to ED");
		getUserAndLoginNewUXClass();
					
		report.startStep("Navigate To FD");
		homePage.navigateToRequiredCourseOnHomePage("First Discoveries");
		sleep(1);

		
	}

	@Test
	@TestCaseParams(testCaseID = { "24624" })
	@Category(unstableTests.class)
	public void CloseDragAndDrop() throws Exception {

		webDriver.maximize();
		String[] words = new String[] { "events", "humanity", "recall", "accomplishments", "century", "repeat",
				"mistakes", "technological", "capacity", "basic" };

		report.startStep("Navigate A3-U6-L1-P2");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "A3", 6, 1);
		learningArea.clickOnStep(2);
		learningArea.clickOnTask(1);

		report.startStep("Verify instruction text");
		learningArea.VerificationOfQuestionInstruction(closeDragAndDropInstruction);

		report.startStep("Drag all tiles to droptargets");
		dragAndDrop.dragAndDropCloseAnswerByTextToId("events", "1_1");
		sleep(1);
		dragAndDrop.dragAndDropCloseAnswerByTextToId("humanity", "1_2");
		sleep(1);
		dragAndDrop.dragAndDropCloseAnswerByTextToId("recall", "1_3");
		sleep(1);
		dragAndDrop.dragAndDropCloseAnswerByTextToId("accomplishments", "2_1");
		sleep(1);
		dragAndDrop.dragAndDropCloseAnswerByTextToId("century", "2_2");
		sleep(1);
		dragAndDrop.dragAndDropCloseAnswerByTextToId("repeat", "2_3");
		sleep(1);
		dragAndDrop.dragAndDropCloseAnswerByTextToId("mistakes", "2_4");
		sleep(1);
		dragAndDrop.dragAndDropCloseAnswerByTextToId("technological", "3_1");
		sleep(1);
		dragAndDrop.dragAndDropCloseAnswerByTextToId("capacity", "3_2");
		sleep(1);
		dragAndDrop.dragAndDropCloseAnswerByTextToId("basic", "3_3");
		sleep(1);

		report.startStep("Click on Clear and check all tiles back to bank");
		learningArea.clickOnClearAnswer();
		for (int i = 0; i < words.length; i++) {
			dragAndDrop.checkCloseTileIsBackToBank(words[i]);
		}

		report.report("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnClear();

		report.report("Click on See answers and check tiles placed");
		learningArea.clickOnSeeAnswer();
		for (int i = 0; i < words.length; i++) {
			dragAndDrop.checkTileIsPlaced(words[i]);
		}

		report.report("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnSeeAnswer();

		report.report("Click on See answers and check tiles back to bank");
		learningArea.clickOnSeeAnswer();
		// WebElement //*[@id="pmContainer"]/div[2]/div/div[1]/div
		for (int i = 0; i < words.length; i++) {
			dragAndDrop.checkCloseTileIsBackToBank(words[i]);
		}

		report.report("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnClear();

		report.startStep("Drag 1 tile to correct place and 1 to wrong place");
		String correctAns = words[0];
		String correctTarget = "1_1";
		String wrongAns = words[1];
		String wrongTarget = "1_2";
		dragAndDrop.dragAndDropCloseAnswerByTextToId(correctAns, correctTarget);
		dragAndDrop.dragAndDropCloseAnswerByTextToId(wrongAns, wrongTarget);

		report.startStep("Click on Check Answer and check V/X signs placed correctly");
		learningArea.clickOnCheckAnswer();
		dragAndDrop.checkDragAndDropCorrectAnswerCloze(correctAns);
		dragAndDrop.checkDragAndDropWrongAnswerCloze(wrongAns);

		report.report("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnCheckAnswer();

		report.startStep("Click on Check Answer and check correct tile remains and wrong tile back to bank");
		learningArea.clickOnCheckAnswer();
		dragAndDrop.checkCloseTileIsBackToBank(wrongAns);
		dragAndDrop.checkTileIsPlaced(correctAns);
		learningArea.clickOnClearAnswer();

		report.report("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnClear();

		report.startStep("Check you can replace current tile by the next tile by drag on it");
		String currentTile = words[2];
		String nextTile = words[3];
		String targetId = "1_3";
		dragAndDrop.dragAndDropCloseAnswerByTextToId(currentTile, targetId);
		dragAndDrop.checkTileIsPlaced(currentTile);
		sleep(1);
		dragAndDrop.dragAndDropCloseAnswerByTextToId(nextTile, targetId);
		dragAndDrop.checkTileIsPlaced(nextTile);
		dragAndDrop.checkCloseTileIsBackToBank(currentTile);

	}
	
	@Test
	@TestCaseParams(testCaseID = { "24624" }, skippedBrowsers = {"firefox"}) // Actions API not supported by geckodriver)
	public void CloseDragAndDrop_Media() throws Exception {

		webDriver.maximize();
		String[] words = new String[] { "diamond", "nice,", "United States", "this", "meet", "How" };

		report.startStep("Navigate B1-U2-L1-P3");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "B1", 2, 1);
		learningArea.clickOnStep(2);
		learningArea.clickOnTask(2);

		report.startStep("Verify instruction text");
		learningArea.VerificationOfQuestionInstruction(closeDragAndDropMediaInstruction);

		report.startStep("Drag all tiles to droptargets");
		dragAndDrop.dragAndDropCloseAnswerByTextToId("diamond", "1_1");
		sleep(1);
		dragAndDrop.dragAndDropCloseAnswerByTextToId("nice,", "1_2");
		sleep(1);
		dragAndDrop.dragAndDropCloseAnswerByTextToId("United States", "1_3");
		sleep(1);
		dragAndDrop.dragAndDropCloseAnswerByTextToId("this", "1_4");
		sleep(1);
		dragAndDrop.dragAndDropCloseAnswerByTextToId("meet", "1_5");
		sleep(1);
		dragAndDrop.dragAndDropCloseAnswerByTextToId("How", "1_6");
		sleep(1);
		
		report.startStep("Click on Clear and check all tiles back to bank");
		learningArea.clickOnClearAnswer();
		for (int i = 0; i < words.length; i++) {
			dragAndDrop.checkCloseTileIsBackToBank(words[i]);
		}

		report.report("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnClear();

		report.report("Click on See answers and check tiles placed");
		learningArea.clickOnSeeAnswer();
		for (int i = 0; i < words.length; i++) {
			dragAndDrop.checkTileIsPlaced(words[i]);
		}

		report.report("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnSeeAnswer();

		report.report("Click on See answers and check tiles back to bank");
		learningArea.clickOnSeeAnswer();
		for (int i = 0; i < words.length; i++) {
			dragAndDrop.checkCloseTileIsBackToBank(words[i]);
		}

		report.report("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnClear();

		report.startStep("Drag 1 tile to correct place and 1 to wrong place");
		String correctAns = words[0];
		String correctTarget = "1_1";
		String wrongAns = words[1];
		String wrongTarget = "1_3";
		dragAndDrop.dragAndDropCloseAnswerByTextToId(correctAns, correctTarget);
		dragAndDrop.dragAndDropCloseAnswerByTextToId(wrongAns, wrongTarget);

		report.startStep("Click on Check Answer and check V/X signs placed correctly");
		learningArea.clickOnCheckAnswer();
		dragAndDrop.checkDragAndDropCorrectAnswerCloze(correctAns);
		dragAndDrop.checkDragAndDropWrongAnswerCloze(wrongAns);

		report.report("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnCheckAnswer();

		report.startStep("Click on Check Answer and check correct tile remains and wrong tile back to bank");
		learningArea.clickOnCheckAnswer();
		dragAndDrop.checkCloseTileIsBackToBank(wrongAns);
		dragAndDrop.checkTileIsPlaced(correctAns);
		learningArea.clickOnClearAnswer();

		report.report("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnClear();

		report.startStep("Check you can replace current tile by the next tile by drag on it");
		String currentTile = words[2];
		String nextTile = words[3];
		String targetId = "1_4";
		dragAndDrop.dragAndDropCloseAnswerByTextToId(currentTile, targetId);
		dragAndDrop.checkTileIsPlaced(currentTile);
		sleep(1);
		dragAndDrop.dragAndDropCloseAnswerByTextToId(nextTile, targetId);
		dragAndDrop.checkTileIsPlaced(nextTile);
		dragAndDrop.checkCloseTileIsBackToBank(currentTile);

		report.startStep("Press on Headphone btn and check media");
		learningArea.clickOnMediaById("1");
		sleep(1);
		learningArea.checkAudioFile("PMediaPlayer", "b1ltbqp03.mp3");
		
	}

	//Cloze drag and Drop test mode
	
	@Test
	@TestCaseParams(testCaseID = { "24624" }, skippedBrowsers = {"firefox"}) // Actions API not supported by geckodriver)
	public void CloseDragAndDrop_TestMode() throws Exception {
		
	report.startStep("Init Test Data");
		webDriver.maximize();
		String[] words = new String[] { "job", "Flowers", "part-time", "enjoy", "fresh", "arrange", "bouquets", "choose","orders" };
		int taskIndex = 4;
		String expectedScore = "5%";
		
	report.startStep("Navigate B1-U10-L3-S5");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "B1", 10, 3);
		sleep(1);
		learningArea.clickOnStep(5);
		learningArea.clickOnStartTest();
		sleep(1);
		learningArea.clickOnTask(taskIndex);
		
	report.startStep("Verify instruction text");
		learningArea.VerificationOfQuestionInstruction(closeDragAndDropTest);
		
	report.startStep("Drag all tiles to droptargets");
		dragAndDrop.dragAndDropCloseAnswerByTextToId(words[0], "1_1");
		sleep(1);
		dragAndDrop.dragAndDropCloseAnswerByTextToId(words[1], "1_2");
		sleep(1);
		dragAndDrop.dragAndDropCloseAnswerByTextToId(words[3], "1_3");
		sleep(1);
	report.startStep("Navigate to other task and back and check correct tile still placed");
		learningArea.clickOnBackButton();
		sleep(1);
		learningArea.clickOnNextButton();
		sleep(1);
		dragAndDrop.checkTileIsPlaced(words[1]);
		
	report.startStep("Submit test and check score");
		learningArea.submitTest(true);
		String score = learningArea.getTestScore();
		testResultService.assertEquals(expectedScore, score, "Checking Test Score validity");
		
	report.startStep("Check cloze task indication correct/wrong");
		learningArea.checkThatTestTaskMarkPartlyCorrect(taskIndex);
		
	report.startStep("Press on Task  and check Your Answer / Correct Answer values");
		learningArea.clickOnTask(taskIndex);
		learningArea.clickOnYourAnswerTab();
		dragAndDrop.checkTileIsPlacedInTestYourAnswerAndCorrect(words[0]);
		dragAndDrop.checkTileIsPlacedInTestYourAnswerAndWrong(words[3]);
		sleep(1);
		
		learningArea.clickOnCorrectAnswerTab();
		for (int i = 0; i < words.length; i++) {
			dragAndDrop.checkTileIsPlaced(words[i]);
		}
	}
	
	@Test
	@TestCaseParams(testCaseID = { "24636" }, skippedBrowsers = {"firefox"}) // Actions API not supported by geckodriver)
	public void ClassificationDragAndDrop() throws Exception {

		String[] words = new String[] { "had never", "was very", "hoped to", "had made", "suggested Cindy",
				"had a full" };

		report.startStep("Navigate I2-U5-L2-P2");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "I2", 5, 2);
		sleep(1);
		learningArea.clickOnStep(2);
		sleep(1);
		learningArea.clickOnTask(1);
		
		report.startStep("Verify instruction text");
		learningArea.VerificationOfQuestionInstruction(classificationDragAndDropInstruction);

		report.startStep("Drag all tiles to droptargets");
		dragAndDrop.dragAndDropClassificationAnswerByTextToColumn("had never", 1, 1);
		sleep(1);
		dragAndDrop.dragAndDropClassificationAnswerByTextToColumn("was very", 1, 2);
		sleep(1);
		dragAndDrop.dragAndDropClassificationAnswerByTextToColumn("hoped to", 1, 3);
		sleep(1);
		dragAndDrop.dragAndDropClassificationAnswerByTextToColumn("had made", 2, 1);
		sleep(1);
		dragAndDrop.dragAndDropClassificationAnswerByTextToColumn("suggested Cindy", 2, 2);
		sleep(1);
		dragAndDrop.dragAndDropClassificationAnswerByTextToColumn("had a full", 2, 3);
		sleep(1);

		report.startStep("Click on Clear and check all tiles back to bank");
		learningArea.clickOnClearAnswer();
		for (int i = 0; i < words.length; i++) {
			dragAndDrop.checkCloseTileIsBackToBank(words[i]);
		}

		report.report("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnClear();

		report.report("Click on See answers and check tiles placed");
		learningArea.clickOnSeeAnswer();
		for (int i = 0; i < words.length; i++) {
			dragAndDrop.checkTileIsPlaced(words[i]);
		}

		report.report("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnSeeAnswer();

		report.report("Click on See answers and check tiles back to bank");
		learningArea.clickOnSeeAnswer();
		for (int i = 0; i < words.length; i++) {
			dragAndDrop.checkCloseTileIsBackToBank(words[i]);
		}

		report.report("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnClear();

		report.startStep("Drag 1 tile to correct place and 1 to wrong place");
		String correctAns = words[0];
		int correctRow = 1;
		int correctCol = 2;
		String wrongAns = words[1];
		int wrongRow = 1;
		int wrongCol = 3;
		dragAndDrop.dragAndDropClassificationAnswerByTextToColumn(correctAns, correctRow, correctCol);
		sleep(1);
		dragAndDrop.dragAndDropClassificationAnswerByTextToColumn(wrongAns, wrongRow, wrongCol);
		sleep(1);

		report.startStep("Click on Check Answer and check V/X signs placed correctly");
		learningArea.clickOnCheckAnswer();
		dragAndDrop.checkDragAndDropCorrectAnswer(correctAns);
		dragAndDrop.checkDragAndDropWrongAnswer(wrongAns);

		report.report("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnCheckAnswer();

		report.startStep("Click on Check Answer and check correct tile remains and wrong tile back to bank");
		learningArea.clickOnCheckAnswer();
		dragAndDrop.checkCloseTileIsBackToBank(wrongAns);
		dragAndDrop.checkTileIsPlaced(correctAns);
		learningArea.clickOnClearAnswer();

		report.report("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnClear();

		report.startStep("Check you can replace current tile by the next tile by drag on it");
		String currentTile = words[2];
		String nextTile = words[3];
		int targetRow = 1, targetCol = 3;
		dragAndDrop.dragAndDropClassificationAnswerByTextToColumn(currentTile, targetRow, targetCol);
		dragAndDrop.checkTileIsPlaced(currentTile);
		sleep(1);
		dragAndDrop.dragAndDropClassificationAnswerByTextToColumn(nextTile, targetRow, targetCol);
		dragAndDrop.checkTileIsPlaced(nextTile);
		dragAndDrop.checkCloseTileIsBackToBank(currentTile);

	}
	
	@Test
	@TestCaseParams(testCaseID = { "40908" }, skippedBrowsers = {"firefox"}) // Actions API not supported by geckodriver)
	public void ClassificationDragAndDropFullScreen() throws Exception {

		String[] words = new String[] {
				"Pre-school children can", 
				"Children today are",
				"Formal education can", 
				"Children should be",
				"Competition in pre-school is good",
				"Formal education at",
				"Competition in pre-school is bad",
				"Pre-school children should"
				};

		report.startStep("Navigate I1-U1-L2-P1");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "I1", 1, 2);
		learningArea.clickOnStep(2);
				
		report.startStep("Drag all tiles to droptargets");
		int rowCount = 0;
		int columnCount = 0;
		
		for (int i = 0; i < words.length; i++) {
			
			if (i < 4) {
				rowCount = i+1; columnCount = 1;
			} else {
				rowCount = i-3; columnCount = 2;
			}
			
			dragAndDrop.dragAndDropClassificationAnswerByTextToColumn(words[i], rowCount, columnCount);
			
			sleep(1);
		}
		
		
		report.startStep("Click on Clear and check all tiles back to bank");
		learningArea.clickOnClearAnswer();
		for (int i = 0; i < words.length; i++) {
			dragAndDrop.checkCloseTileIsBackToBank(words[i]);
		}

		report.report("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnClear();

		report.report("Click on See answers and check tiles placed");
		learningArea.clickOnSeeAnswer();
		for (int i = 0; i < words.length; i++) {
			dragAndDrop.checkTileIsPlaced(words[i]);
		}

		report.report("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnSeeAnswer();

		report.report("Click on See answers and check tiles back to bank");
		learningArea.clickOnSeeAnswer();
		for (int i = 0; i < words.length; i++) {
			dragAndDrop.checkCloseTileIsBackToBank(words[i]);
		}

		report.report("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnClear();

		report.startStep("Drag 1 tile to correct place and 1 to wrong place");
		String correctAns = words[0];
		int correctRow = 1;
		int correctCol = 1;
		String wrongAns = words[1];
		int wrongRow = 1;
		int wrongCol = 2;
		dragAndDrop.dragAndDropClassificationAnswerByTextToColumn(correctAns, correctRow, correctCol);
		sleep(1);
		dragAndDrop.dragAndDropClassificationAnswerByTextToColumn(wrongAns, wrongRow, wrongCol);
		sleep(1);

		report.startStep("Click on Check Answer and check V/X signs placed correctly");
		learningArea.clickOnCheckAnswer();
		dragAndDrop.checkDragAndDropCorrectAnswer(correctAns);
		dragAndDrop.checkDragAndDropWrongAnswer(wrongAns);

		report.report("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnCheckAnswer();

		report.startStep("Click on Check Answer and check correct tile remains and wrong tile back to bank");
		learningArea.clickOnCheckAnswer();
		dragAndDrop.checkCloseTileIsBackToBank(wrongAns);
		dragAndDrop.checkTileIsPlaced(correctAns);
		learningArea.clickOnClearAnswer();

		report.report("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnClear();

		report.startStep("Check you can replace current tile by the next tile by drag on it");
		String currentTile = words[2];
		String nextTile = words[3];
		int targetRow = 1, targetCol = 2;
		dragAndDrop.dragAndDropClassificationAnswerByTextToColumn(currentTile, targetRow, targetCol);
		dragAndDrop.checkTileIsPlaced(currentTile);
		sleep(1);
		dragAndDrop.dragAndDropClassificationAnswerByTextToColumn(nextTile, targetRow, targetCol);
		dragAndDrop.checkTileIsPlaced(nextTile);
		dragAndDrop.checkCloseTileIsBackToBank(currentTile);

	}
	
	// matching drag and drop test mode
	
	@Test
	@TestCaseParams(testCaseID = { "24627" }, skippedBrowsers = {"firefox"}) // Actions API not supported by geckodriver)
	public void MatchingDragAndDropTestMode() throws Exception {
		
	report.startStep("Init Test Data");
		webDriver.maximize();
		String[] words = new String[] { "film", "designer", "effects", "artist", "set"};
		int taskIndex = 2;
		String expectedScore = "7%";
		
	report.startStep("Navigate I2-U10-L1-S5");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "I2", 10, 1);
		sleep(1);
		learningArea.clickOnStep(5);
		learningArea.clickOnStartTest();
		sleep(1);
		learningArea.clickOnTask(taskIndex);
		
	report.startStep("Verify instruction text");
		learningArea.VerificationOfQuestionInstruction(matchingDragAndDropTestMode);
		
	report.startStep("Drag all tiles to droptargets");
		dragAndDrop.dragAndDropClassificationAnswerByTextToColumn(words[0], 1, 2);
		sleep(1);
		dragAndDrop.dragAndDropClassificationAnswerByTextToColumn(words[1], 2, 2);
		sleep(1);
		dragAndDrop.dragAndDropClassificationAnswerByTextToColumn(words[2], 3, 2);
		sleep(1);
		dragAndDrop.dragAndDropClassificationAnswerByTextToColumn(words[3], 4, 2);
		sleep(1);
		dragAndDrop.dragAndDropClassificationAnswerByTextToColumn(words[4], 5, 2);
		sleep(1);
		
	report.startStep("Navigate to other task and back and check correct tile still placed");
		learningArea.clickOnBackButton();
		sleep(1);
		learningArea.clickOnNextButton();
		sleep(1);
		dragAndDrop.checkTileIsPlaced(words[1]);
		
	report.startStep("Submit test and check score");
		learningArea.submitTest(true);
		String score = learningArea.getTestScore();
		testResultService.assertEquals(expectedScore, score, "Checking Test Score validity");
		
	report.startStep("Check  task indication correct/wrong");
		learningArea.checkThatTestTaskMarkPartlyCorrect(taskIndex);
		
	report.startStep("Press on Task and check Your Answer / Correct Answer values");
		learningArea.clickOnTask(taskIndex);
		learningArea.clickOnYourAnswerTab();
		dragAndDrop.checkTileIsPlacedInTestYourAnswerAndCorrect(words[1]);
		dragAndDrop.checkTileIsPlacedInTestYourAnswerAndWrong(words[0]);
		sleep(1);
		
		learningArea.clickOnCorrectAnswerTab();
		for (int i = 0; i < words.length; i++) {
			dragAndDrop.checkTileIsPlaced(words[i]);
		}
	}
	
	@Test
	@TestCaseParams(testCaseID = { "24627" }, skippedBrowsers = {"firefox"}) // Actions API not supported by geckodriver)
	// @Category(inProgressTests.class)
	public void MatchingDragAndDrop() throws Exception {

		String[] words = new String[] { "dispute", "approach", "politicians", "era", "speech", "career" };

		report.startStep("Navigate A3-U6-L1-P5");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "A3", 6, 1);
		learningArea.clickOnStep(2);
		learningArea.clickOnTask(4);
		
		report.startStep("Verify instruction text");
		learningArea.VerificationOfQuestionInstruction(matchingDragAndDropInstruction);

		report.startStep("Drag all tiles to droptargets");
		dragAndDrop.dragAndDropClassificationAnswerByTextToColumn("dispute", 1, 2);
		sleep(1);
		dragAndDrop.dragAndDropClassificationAnswerByTextToColumn("approach", 2, 2);
		sleep(1);
		dragAndDrop.dragAndDropClassificationAnswerByTextToColumn("politicians", 3, 2);
		sleep(1);
		dragAndDrop.dragAndDropClassificationAnswerByTextToColumn("era", 4, 2);
		sleep(1);
		dragAndDrop.dragAndDropClassificationAnswerByTextToColumn("speech", 5, 2);
		sleep(1);
		dragAndDrop.dragAndDropClassificationAnswerByTextToColumn("career", 6, 2);
		sleep(1);

		report.startStep("Click on Clear and check all tiles back to bank");
		learningArea.clickOnClearAnswer();
		for (int i = 0; i < words.length; i++) {
			dragAndDrop.checkCloseTileIsBackToBank(words[i]);
		}

		report.report("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnClear();

		report.report("Click on See answers and check tiles placed");
		learningArea.clickOnSeeAnswer();
		for (int i = 0; i < words.length; i++) {
			dragAndDrop.checkTileIsPlaced(words[i]);
		}

		report.report("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnSeeAnswer();

		report.report("Click on See answers and check tiles back to bank");
		learningArea.clickOnSeeAnswer();
		for (int i = 0; i < words.length; i++) {
			dragAndDrop.checkCloseTileIsBackToBank(words[i]);
		}

		report.report("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnClear();

		report.startStep("Drag 1 tile to correct place and 1 to wrong place");
		String correctAns = words[0];
		int correctRow = 4;
		int correctCol = 2;
		String wrongAns = words[1];
		int wrongRow = 1;
		int wrongCol = 2;
		dragAndDrop.dragAndDropClassificationAnswerByTextToColumn(correctAns, correctRow, correctCol);
		sleep(1);
		dragAndDrop.dragAndDropClassificationAnswerByTextToColumn(wrongAns, wrongRow, wrongCol);
		sleep(1);

		report.startStep("Click on Check Answer and check V/X signs placed correctly");
		learningArea.clickOnCheckAnswer();
		dragAndDrop.checkDragAndDropCorrectAnswer(correctAns);
		dragAndDrop.checkDragAndDropWrongAnswer(wrongAns);

		report.report("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnCheckAnswer();

		report.startStep("Click on Check Answer and check correct tile remains and wrong tile back to bank");
		learningArea.clickOnCheckAnswer();
		dragAndDrop.checkCloseTileIsBackToBank(wrongAns);
		dragAndDrop.checkTileIsPlaced(correctAns);
		learningArea.clickOnClearAnswer();

		report.report("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnClear();

		report.startStep("Check you can replace current tile by the next tile by drag on it");
		String currentTile = words[2];
		String nextTile = words[3];
		int targetRow = 3, targetCol = 2;
		dragAndDrop.dragAndDropClassificationAnswerByTextToColumn(currentTile, targetRow, targetCol);
		dragAndDrop.checkTileIsPlaced(currentTile);
		sleep(1);
		dragAndDrop.dragAndDropClassificationAnswerByTextToColumn(nextTile, targetRow, targetCol);
		dragAndDrop.checkTileIsPlaced(nextTile);
		dragAndDrop.checkCloseTileIsBackToBank(currentTile);

	}
// Test for MCQ Image test mode
	
	@Test
	@TestCaseParams(testCaseID = { "24578" })
	// @Category(inProgressTests.class)
	public void MCQImageTestMode() throws Exception {
		
		report.startStep("Init Test Data");
		webDriver.maximize();
		// static number of answer in HTML by its starting word
				int In = 1; // correct
				int On = 2;
				int At = 3;
				
		int taskIndex = 2;
		String expectedScore = "20%";
		
	report.startStep("Navigate B1-U9-L5-S5");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "B1", 9, 5);
		sleep(1);
		learningArea.clickOnStep(5);
		learningArea.clickOnStartTest();
		sleep(1);
		learningArea.clickOnTask(taskIndex);
		
	report.startStep("Verify instruction text");
		learningArea.VerificationOfQuestionInstruction(highlightTextInstruction);
		
		learningArea.selectAnswerRadioByTextNum(1, 1);
		report.startStep("Navigate to other task and back and check correct tile still placed");
		learningArea.clickOnBackButton();
		sleep(1);
		learningArea.clickOnNextButton();
		sleep(1);
	
		report.startStep("Submit test and check score");
		learningArea.submitTest(true);
		String score = learningArea.getTestScore();
		testResultService.assertEquals(expectedScore, score, "Checking Test Score validity");
		
	report.startStep("Check task indication correct/wrong");
		learningArea.checkThatTestTaskMarkCorrect(taskIndex);
		
	report.startStep("Press on Task  and check Your Answer / Correct Answer values");
		learningArea.clickOnTask(taskIndex);
		learningArea.clickOnYourAnswerTab();
		learningArea.clickOnCorrectAnswerTab();
	}
	
	
	@Test
	@TestCaseParams(testCaseID = { "24578" })
	// @Category(inProgressTests.class)
	public void MCQSelection_Media() throws Exception {

		// static number of answer in HTML by its starting word
		int high = 1; // correct
		int checks = 2;
		int overdrawing = 3;
		int toHelp = 4; // correct
		int toBounce = 5;
		int toPay = 6;

		report.startStep("Navigate A1-U5-L1-P1");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "A1", 5, 1);
		learningArea.clickOnStep(2);
		sleep(1);
		
		report.startStep("Verify instruction text");
		learningArea.VerificationOfQuestionInstruction(mcqSelectionInstruction);

		report.startStep("Select all answers");
		learningArea.selectAnswerRadioByTextNum(1, 1);
		sleep(1);
		learningArea.selectAnswerRadioByTextNum(2, 6);

		report.startStep("Click on Clear and check all answers unselected");
		learningArea.clickOnClearAnswer();
		sleep(1);
		learningArea.checkAllAnswersUnselectedMCQ();

		report.report("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnClear();

		report.report("Click on See answers and check answers selected");
		learningArea.clickOnSeeAnswer();
		sleep(1);
		learningArea.verifyAnswerRadioSelectedCorrect(1, 1);
		learningArea.verifyAnswerRadioSelectedCorrect(2, 4);

		report.report("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnSeeAnswer();

		report.report("Click on See answers check all answers unselected");
		learningArea.clickOnSeeAnswer();
		sleep(1);
		learningArea.checkAllAnswersUnselectedMCQ();

		report.report("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnClear();

		report.startStep("Select 1 correct 1 wrong answer");
		learningArea.selectAnswerRadioByTextNum(1, 1);
		sleep(1);
		learningArea.selectAnswerRadioByTextNum(2, 5);
		sleep(1);

		report.startStep("Click on Check Answer and check V/X signs placed correctly");
		learningArea.clickOnCheckAnswer();
		sleep(1);
		learningArea.verifyAnswerRadioSelectedCorrect(1, 1);
		learningArea.verifyAnswerRadioSelectedWrong(2, 5);

		report.report("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnCheckAnswer();

		report.startStep("Click on Check Answer and check correct remains and wrong unselected");
		learningArea.clickOnCheckAnswer();
		sleep(1);
		learningArea.checkSingleAnswerSelectedMCQ(1, 1);
		learningArea.checkSingleAnswerUnselectedMCQ(2, 5);
		learningArea.clickOnClearAnswer();
		sleep(1);

		report.report("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnClear();

		report.startStep("Check next selection unselect current");
		learningArea.selectAnswerRadioByTextNum(1, 1);
		sleep(1);
		learningArea.selectAnswerRadioByTextNum(1, 2);
		sleep(1);
		learningArea.checkSingleAnswerUnselectedMCQ(1, 1);
		
		report.startStep("Press on Headphone btn and check media");
		learningArea.clickOnMediaById("2");
		sleep(1);
		learningArea.checkAudioFile("syncAudioPlayer", "a1lrade.mp3");
		sleep(3);
		
	}

	@Test
	@TestCaseParams (testCaseID ={""})
	public void FillInTheBlanksWithImage () throws Exception {
		
		String [] answers = {"this","that","those"}; 
		String [] dropList ={"1_1","1_2"};
		
	report.startStep("Navigate  B1-U2-L5-S2-T7");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "B1", 2, 5);
		learningArea.clickOnStep(2);
		learningArea.clickOnTask(6);
		sleep(1);
	report.startStep("Verify instruction text");
		learningArea.VerificationOfQuestionInstruction(fillInTheBlanksWithImage);
		
	report.startStep("Select all answers");
		learningArea.selectAnswerFromDropDown(dropList[0], answers[0]);
		learningArea.selectAnswerFromDropDown(dropList[1], answers[2]);
		
	report.startStep("Click on Clear and check all answers unselected");
		learningArea.clickOnClearAnswer();
		learningArea.checkAnswersUnselectedFillTheBlanks(dropList[0], answers[0]);
		learningArea.checkAnswersUnselectedFillTheBlanks(dropList[1], answers[2]);

	report.report("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnClear();
		
	report.report("Click on See answers and check answers selected");
		learningArea.clickOnSeeAnswer();
		learningArea.checkAnswersSelectedFillTheBlanks(dropList[0], answers[0]);
		learningArea.checkAnswersSelectedFillTheBlanks(dropList[1], answers[1]);

	report.report("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnSeeAnswer();
		
	report.report("Click on See answers check all answers unselected");
		learningArea.clickOnSeeAnswer();
		learningArea.checkAnswersUnselectedFillTheBlanks(dropList[0], answers[0]);
		learningArea.checkAnswersUnselectedFillTheBlanks(dropList[1], answers[1]);

	report.report("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnClear();

	report.startStep("Select 1 correct 1 wrong answer");
		learningArea.selectAnswerFromDropDown(dropList[0], answers[0]);
		learningArea.selectAnswerFromDropDown(dropList[1], answers[2]);
		
	report.startStep("Click on Check Answer and check V/X signs placed correctly");
		learningArea.clickOnCheckAnswer();
		learningArea.verifyAnswerFillTheBlankSelectedCorrect(dropList[0]);
		learningArea.verifyAnswerFillTheBlankSelectedWrong(dropList[1]);

	report.report("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnCheckAnswer();

	report.startStep("Click on Check Answer and check correct remains and wrong unselected");
		learningArea.clickOnCheckAnswer();
		learningArea.checkAnswersSelectedFillTheBlanks(dropList[0], answers[0]);
		learningArea.checkAnswersUnselectedFillTheBlanks(dropList[1], answers[2]);
		learningArea.clickOnClearAnswer();

	report.report("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnClear();
	}
	
	@Test
	@TestCaseParams(testCaseID = { "24626" })
	public void FillInTheBlanksSelection_Media() throws Exception {

		// answers test data
		String correct1_1 = "emotional reaction"; // correct
		String correct2_1 = "fond"; // correct
		String wrong2_1 = "fair";

		report.startStep("Navigate A3-U6-L1-P3");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "A3", 6, 1);
		learningArea.clickOnStep(2);
		learningArea.clickOnTask(2);
		sleep(1);
		
		report.startStep("Verify instruction text");
		learningArea.VerificationOfQuestionInstruction(fillInTheBlanksSelectionInstruction);

		report.startStep("Select all answers");
		learningArea.selectAnswerFromDropDown("1_1", correct1_1);
		learningArea.selectAnswerFromDropDown("2_1", wrong2_1);

		report.startStep("Click on Clear and check all answers unselected");
		learningArea.clickOnClearAnswer();
		learningArea.checkAnswersUnselectedFillTheBlanks("1_1", correct1_1);
		learningArea.checkAnswersUnselectedFillTheBlanks("2_1", wrong2_1);

		report.report("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnClear();

		report.report("Click on See answers and check answers selected");
		learningArea.clickOnSeeAnswer();
		learningArea.checkAnswersSelectedFillTheBlanks("1_1", correct1_1);
		learningArea.checkAnswersSelectedFillTheBlanks("2_1", correct2_1);

		report.report("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnSeeAnswer();

		report.report("Click on See answers check all answers unselected");
		learningArea.clickOnSeeAnswer();
		learningArea.checkAnswersUnselectedFillTheBlanks("1_1", correct1_1);
		learningArea.checkAnswersUnselectedFillTheBlanks("2_1", correct2_1);

		report.report("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnClear();

		report.startStep("Select 1 correct 1 wrong answer");
		learningArea.selectAnswerFromDropDown("1_1", correct1_1);
		learningArea.selectAnswerFromDropDown("2_1", wrong2_1);

		report.startStep("Click on Check Answer and check V/X signs placed correctly");
		learningArea.clickOnCheckAnswer();
		learningArea.verifyAnswerFillTheBlankSelectedCorrect("1_1");
		learningArea.verifyAnswerFillTheBlankSelectedWrong("2_1");

		report.report("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnCheckAnswer();

		report.startStep("Click on Check Answer and check correct remains and wrong unselected");
		learningArea.clickOnCheckAnswer();
		learningArea.checkAnswersSelectedFillTheBlanks("1_1", correct1_1);
		learningArea.checkAnswersUnselectedFillTheBlanks("2_1", wrong2_1);
		learningArea.clickOnClearAnswer();

		report.report("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnClear();
		
		report.startStep("Press on Headphone btn and check media");
		learningArea.clickOnMediaById("1");
		sleep(1);
		learningArea.checkAudioFile("syncAudioPlayer", "a3lrnwe.mp3");
	}
	
	//Fill in the blank test mode OLd LA
	
		@Test
		@TestCaseParams(testCaseID = { "24626" })
		public void FillInTheBlanks_TestMode() throws Exception {
			
		report.startStep("Init Test Data");
			String correct1_1 = "teaches"; // correct
			String correct1_9 = "start"; // correct
			String wrong1_2 = "not";
			int taskIndex = 2;
			String expectedScore = "5%";
			
		report.startStep("Navigate B1-U10-L1-S6");
			homePage.navigateToCourseUnitAndLesson(courseCodes, "B1", 10, 1);
			sleep(1);
			learningArea.clickOnStep(6);
			learningArea.clickOnStartTest();
			sleep(1);
			learningArea.clickOnTask(taskIndex);
			
		report.startStep("Verify instruction text");
			learningArea.VerificationOfQuestionInstruction(fillInTheBlanksTest);
			
		report.startStep("Select answers");
			learningArea.selectAnswerFromDropDown("1_1", correct1_1);
			learningArea.selectAnswerFromDropDown("1_9", correct1_9);
			learningArea.selectAnswerFromDropDown("1_2", wrong1_2);
			
		report.startStep("Navigate to other task and back and check correct tile still placed");
			learningArea.clickOnBackButton();
			sleep(1);
			learningArea.clickOnNextButton();
			sleep(1);
			dragAndDrop.checkTileIsPlacedFillInTheBlank(correct1_1);
			
		report.startStep("Submit test and check score");
			learningArea.submitTest(true);
			String score = learningArea.getTestScore();
			testResultService.assertEquals(expectedScore, score, "Checking Test Score validity");
			
		report.startStep("Check  task indication correct/wrong");
			learningArea.checkThatTestTaskMarkPartlyCorrect(taskIndex);
			
		report.startStep("Press on Task  and check Your Answer / Correct Answer values");
			learningArea.clickOnTask(taskIndex);
			learningArea.clickOnYourAnswerTab();
			dragAndDrop.checkTileIsPlacedInTestYourAnswerAndCorrectFillInTheBlank(correct1_1);
			learningArea.clickOnCorrectAnswerTab();
			dragAndDrop.checkTileIsPlacedInTestYourAnswerAndCorrectFillInTheBlank(correct1_1);
		}
		
	@Test
	@TestCaseParams(testCaseID = { "24629" })
	public void MarkTheTrueSelection() throws Exception {

		// answers test data
		// correct check: answerID = 1, 4, 7

		report.startStep("Navigate B3-U3-L2-P2");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "B3", 3, 2);
		learningArea.clickOnStep(2);
		learningArea.clickOnTask(1);
		sleep(1);

		report.startStep("Verify instruction text");
		learningArea.VerificationOfQuestionInstruction(markTheTrueInstruction);
		
		report.startStep("Select all answers");

		for (int i = 1; i < 5; i++) {
			learningArea.checkCheckBox(i);
		}
		for (int i = 1; i < 5; i++) {
			learningArea.checkSingleAnswerSelectedMCQ(1, i);
		}

		report.startStep("Click on Clear and check all answers unselected");
		learningArea.clickOnClearAnswer();
		for (int i = 1; i < 5; i++) {
			learningArea.checkSingleAnswerUnselectedMCQ(1, i);
		}

		report.startStep("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnClear();

		report.startStep("Click on See answers and check answers selected");
		learningArea.clickOnSeeAnswer();
		learningArea.verifyAnswerRadioSelectedCorrect(1, 2);
		learningArea.verifyAnswerRadioSelectedCorrect(1, 3);
		
		report.startStep("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnSeeAnswer();

		report.startStep("Click on See answers check all answers unselected");
		learningArea.clickOnSeeAnswer();
		for (int i = 1; i < 5; i++) {
			learningArea.checkSingleAnswerUnselectedMCQ(1, i);
		}

		report.startStep("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnClear();

		report.startStep("Select 1 correct 1 wrong answer");
		learningArea.selectAnswerRadioByTextNum(1, 2);
		learningArea.selectAnswerRadioByTextNum(1, 1);

		report.startStep("Click on Check Answer and check V/X signs placed correctly");
		learningArea.clickOnCheckAnswer();
		learningArea.verifyAnswerRadioSelectedCorrect(1, 2);
		learningArea.verifyAnswerRadioSelectedWrong(1, 1);

		report.startStep("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnCheckAnswer();

		report.startStep("Click on Check Answer and check correct remains and wrong unselected");
		learningArea.clickOnCheckAnswer();
		learningArea.checkSingleAnswerSelectedMCQ(1, 2);
		learningArea.checkSingleAnswerUnselectedMCQ(1, 1);
		learningArea.clickOnClearAnswer();

		report.startStep("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnClear();

		report.startStep("Check next selection DOES NOT unselect current");
		learningArea.selectAnswerRadioByTextNum(1, 1);
		learningArea.selectAnswerRadioByTextNum(1, 2);
		learningArea.checkSingleAnswerSelectedMCQ(1, 1);
	}

	@Test
	@TestCaseParams(testCaseID = { "25133" })
	// @Category(inProgressTests.class)
	public void OpenEndedWriting() throws Exception {

		// answers test data
		String correct0 = "He politely turned down the nomination.";
		String correct1 = "We should remember the importance of our basic values.";
		String correct2 = "We will remember this era with fondness.";

		report.startStep("Navigate A3-U6-L1-P6");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "A3", 6, 1);
		learningArea.clickOnStep(2);
		learningArea.clickOnTask(5);
		sleep(1);
		
		report.startStep("Verify instruction text");
		learningArea.VerificationOfQuestionInstruction(openEndedWritingInstruction);

		report.startStep("Select all answers");
		learningArea.enterAnswerTextByIndex("0", correct0);
		learningArea.enterAnswerTextByIndex("1", correct1);
		learningArea.enterAnswerTextByIndex("2", correct2);

		report.startStep("Click on Clear and check all answers unselected");
		learningArea.clickOnClearAnswer();
		// TO DO Cannot verify if text placed in input box - need solution
		/*
		 * learningArea.verifyNoTextInputOpenEndedByIndex("0", correct0);
		 * learningArea.verifyNoTextInputOpenEndedByIndex("1", correct1);
		 * learningArea.verifyNoTextInputOpenEndedByIndex("2", correct2);
		 */

		report.report("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnClear();

		report.report("Click on See answers and check answers selected");
		learningArea.clickOnSeeAnswer();
		sleep(1);
		// TO DO Cannot verify if text placed in input box - need solution

		report.report("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnSeeAnswer();

		report.report("Click on See answers check all answers unselected");
		learningArea.clickOnSeeAnswer();
		// TO DO Cannot verify if text placed in input box - need solution

		report.report("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnClear();

		report.startStep("Select 1 correct 1 wrong answer");
		learningArea.enterAnswerTextByIndex("0", correct0);
		learningArea.enterAnswerTextByIndex("1", correct1 + "NOT CORRECT");

		report.startStep("Click on Check Answer and check V/X signs placed correctly");
		learningArea.clickOnCheckAnswer();
		learningArea.verifyAnswerOpenEndedCorrect(0);
		learningArea.verifyAnswerOpenEndedWrong(1);

		report.report("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnCheckAnswer();

		report.startStep("Click on Check Answer and check correct remains and wrong unselected");
		// TO DO Cannot verify if text placed in input box - need solution
		learningArea.clickOnCheckAnswer();
		learningArea.clickOnClearAnswer();

		report.report("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnClear();

	}

	@Test
	@TestCaseParams(testCaseID = { "25096" })
	public void AlphabetMatchingGame_Media() throws Exception {

		report.startStep("Navigate FD-U1-L1-P2");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "FD", 1, 1);
		sleep(1);
		learningArea.clickOnStep(3);
		sleep(1);
		learningArea.clickOnTask(2);

		report.startStep("Open question verification instruction");
		learningArea.VerificationOfQuestionInstruction(alphabetQuestionInstruction);
		
		report.startStep("Retrieval and verification of the game cards");
		learningArea.RetrieveMemoryGameCards();
		learningArea.VerifyTwelveGameCardPresent();
		
		report.startStep("Correct selection and verification of the correct game cards");
		learningArea.SelectTwoCorrectCards();
		learningArea.VerifyHeadSetOnCorrectlySelectedCards();
		
		report.startStep("Incorrect selection and verification of the correct game cards");
		learningArea.SelectTwoIncorrectCards();
		learningArea.VerifyTheCorrectAnswersDidNotDisappear();
		
		report.startStep("Press on Headphone btn and check media");
		learningArea.clickOnClearAnswer();
		learningArea.SelectTwoCorrectCardsAndPressOnHeadphone();
		sleep(1);
		learningArea.checkAudioFile("PMediaPlayer", "s1alaap03.mp3");
		
	}
	
	// Sequence sentence test mode
	
	@Test
	@TestCaseParams(testCaseID = { "24635" }, skippedBrowsers = {"firefox"}) // Actions API not supported by geckodriver)
	public void SequenceSentenceDragAndDropTestMode() throws Exception {
		
	report.startStep("Init Test Data");
		webDriver.maximize();
	    int taskIndex = 1;
		String expectedScore = "15%";
		String [] answers = new String [] {" what kind of student housing are you interested in?",
				"I think I want to live in a dormitory.",
				"Would you like your own ",
				"d like to share a room",
				"Do you want one or two roommates?",
				"ll put you in a triple room.",
				"I love meeting new people"};
		
	report.startStep("Navigate B2-U9-L4-S7");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "B2",9, 4);
		sleep(1);
		learningArea.clickOnStep(7);
		learningArea.clickOnStartTest();
		sleep(1);
		learningArea.clickOnTask(taskIndex);
		
	report.startStep("Verify instruction text");
		learningArea.VerificationOfQuestionInstruction(SequenceSentenceTestMode);
		
	report.startStep("Drag all tiles to designated place");
		for (int i = 1 ; i <= answers.length ; i++) {
			dragAndDrop.dragAndDropSequenceAnswerByTextToPlaceInOrder(answers[i-1], i);
		}
		
	report.startStep("Navigate to other task and back and check correct tile still placed");
		learningArea.clickOnBackButton();
		sleep(1);
		learningArea.clickOnNextButton();
		sleep(1);
		dragAndDrop.checkTileIsPlacedSequenceSentence(answers[1]);
		
	report.startStep("Submit test and check score");
		learningArea.submitTest(true);
		String score = learningArea.getTestScore();
		testResultService.assertEquals(expectedScore, score, "Checking Test Score validity");
		learningArea.clickOnTask(taskIndex);
		learningArea.clickOnYourAnswerTab();
		
	report.startStep("Check task indication correct/wrong");
		learningArea.checkThatTestTaskMarkPartlyCorrect(taskIndex);
		learningArea.clickOnCorrectAnswerTab();
		dragAndDrop.checkTileIsPlacedSequenceSentence(answers[1]);
		
	}
	
	@Test
	@TestCaseParams(testCaseID = { "24635" }, skippedBrowsers = {"firefox"}) // Actions API not supported by geckodriver)
	public void SequenceSentenceDragAndDrop() throws Exception {
		
		report.startStep("Navigate A1-U6-L2-P2");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "A1", 6, 2);
		learningArea.clickOnStep(2);
		learningArea.clickOnTask(1);
		
		report.startStep("Verify scope of elements displayed in RIGHT SIDE ");
		learningArea.verifyPracticeToolsStateOnClear();
		learningArea.VerificationOfQuestionInstruction(SequenceSentenceQuestionInstruction);
		dragAndDrop.retrieveDraggableSegments();
		dragAndDrop.verifySixDraggableSegmentsInOrder();
		
		report.startStep("Practice Tools Basic Functionality Verification");
		learningArea.verifyPracticeToolsStateOnClear();
		webDriver.refresh();
		learningArea.clickOnStep(2);
		learningArea.clickOnTask(1);
		sleep(3);
		dragAndDrop.verifyDragAndDropsSentenceRandomPlacementAfterRefresh();
		dragAndDrop.dragAndDropReadingSequenceSentence();
		
		report.startStep("Drag 1 tile to correct place and 1 to wrong place");
		String correctAns = "A family invited Bill for a meal.";
		int correctSeq = 1;
		String wrongAns = "Bill was disappointed because the family had made roast beef.";
		int wrongSeq = 2;
		dragAndDrop.dragAndDropSequenceAnswerByTextToPlaceInOrder(correctAns, correctSeq);
		dragAndDrop.dragAndDropSequenceAnswerByTextToPlaceInOrder(wrongAns, wrongSeq);
		
		report.startStep("Click on Check Answer and check V/X signs placed correctly");
		learningArea.clickOnCheckAnswer();
		dragAndDrop.checkDragAndDropCorrectAnswerForSequenceSentence(correctAns);
		dragAndDrop.checkDragAndDropWrongAnswerForSequenceSentence(wrongAns);

		report.report("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnCheckAnswer();
				
	}
	
	@Test
	@TestCaseParams(testCaseID = { "24634" }, skippedBrowsers = {"firefox"}) // Actions API not supported by geckodriver)
	public void SequenceImageDragAndDrop() throws Exception {
		
		report.startStep("Navigate A3-U5-L2-P2");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "A3", 5, 2);
		learningArea.clickOnStep(2);
		learningArea.clickOnTask(1);
		
		report.startStep("Verify scope of elements displayed in RIGHT SIDE");
		learningArea.verifyPracticeToolsStateOnClear();
		learningArea.VerificationOfQuestionInstruction(SequenceImageQuestionInstruction);
		dragAndDrop.retrieveDraggableImages();
		dragAndDrop.verifySixDraggableSegmentsInOrder();
		
		report.startStep("Practice Tools Basic Functionality Verification");
		webDriver.refresh();
		learningArea.clickOnStep(2);
		learningArea.clickOnTask(1);
		sleep(3);
		dragAndDrop.verifyDragAndDropsImagesRandomPlacementAfterRefresh();
		dragAndDrop.dragAndDropReadingSequenceSentence();
		
		report.startStep("Drag 1 tile to correct place, press Check Answer and verify the answer checked as correct");
		String imageNameCorrect = "A3RSMGPGP3";
		int correctSeq = 3;
		
		dragAndDrop.dragAndDropSequenceAnswerByImageNameToPlaceInOrder(imageNameCorrect, correctSeq);
		learningArea.clickOnCheckAnswer();
		learningArea.verifyPracticeToolsStateOnCheckAnswer();
		dragAndDrop.checkDragAndDropAnswerForSequenceImage(imageNameCorrect, true);
		learningArea.clickOnClearAnswer();
		
		report.startStep("Drag 1 tile to wrong place, press Check Answer and verify the answer checked as wrong");
		String imageNameWrong = "A3RSMGPGP6";
		int wrongSeq = 1;
		dragAndDrop.dragAndDropSequenceAnswerByImageNameToPlaceInOrder(imageNameWrong, wrongSeq);
		learningArea.clickOnCheckAnswer();
		learningArea.verifyPracticeToolsStateOnCheckAnswer();
		dragAndDrop.checkDragAndDropAnswerForSequenceImage(imageNameWrong, false);
		learningArea.clickOnClearAnswer();
						
	}
	
	@Test
	@TestCaseParams(testCaseID = { "25136" })
	//@Category(inProgressTests.class)
	public void FreeWrite() throws Exception {

		report.startStep("Prepare test data");
		String texInput = "Hello";
		String sectionNum = "1";
		
		report.startStep("Navigate to First Discoveries ->Unit 2-About Me->Steve's Application Form-> Practice -> Task 3");
		learningArea = homePage.navigateToCourseUnitAndLesson(courseCodes, "FD", 2, 4);
		learningArea.clickOnStep(3);
		sleep(2);
		learningArea.clickOnTask(2);
		
		report.startStep("Verify Task Instruction");
		learningArea.VerificationOfQuestionInstruction(FreeWriteQuestionInstruction);
		
		report.startStep("Verify Send To Teacher btn displayed");
		learningArea.checkThatSendToTeacherBtnIsDisplayed();
		
		report.startStep("Check only Clear tool displayed");
		learningArea.checkThatClearToolIsDisplayed();
		learningArea.checkThatSeeAnswerToolIsNotDisplayed();
		learningArea.checkThatCheckAnswerToolIsNotDisplayed();
				
		report.startStep("Enter Text and press Clear");
		learningArea.enterFreeWritingTextBySection(sectionNum, texInput);
		learningArea.clickOnClearAnswer();
		
		report.startStep("Check text cleared");
		learningArea.verifyNoTextInputByWritingSection(sectionNum);
				
		report.startStep("Enter Text and press Send To Teacher");
		learningArea.enterFreeWritingTextBySection(sectionNum, texInput);
		learningArea.clickOnSendToTeacher();
		
		report.startStep("Validate alert message and close it");
		learningArea.validateAlertModalByMessage(freeWriteAlertAfterSubmit, true);
		sleep(2);
		
		report.startStep("Validate alert message closed");
		learningArea.checkThatAlertMessageClosed();
		

	}
	
	@Test
	@TestCaseParams(testCaseID = { "25135" })
	public void OpenEndedSegments_Media() throws Exception {

		// answers test data
		
		String correct1 = "The police officers are arresting the man.";
		String correct2 = "The woman is telling her story in court today.";
		String correct3 = "I have a fine for parking in front of a \"No Parking\" sign.";
		String correct4 = "The judge is sending the thief to prison.";
		String correct5 = "This parking ticket says I must pay $20.";
		String [] correctAnswers = new String [] {correct1,correct2,correct3,correct4,correct5};

		report.startStep("Navigate B1-U2-L6-P3");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "B1", 2, 6);
		sleep(1);
		learningArea.clickOnStep(2);
		learningArea.clickOnTask(2);
		sleep(1);
		
		report.startStep("Verify instruction text");
		learningArea.VerificationOfQuestionInstruction(openEndedSegmentsInstruction);

		report.startStep("Input all answers");
		for (int i = 0; i < correctAnswers.length; i++) {
			learningArea.enterOpenSegmentAnswerTextByIndex(i+1, correctAnswers[i]);
		}
		
		report.startStep("Click on Clear and check all answers unselected");
		learningArea.clickOnClearAnswer();
		for (int i = 0; i < correctAnswers.length; i++) {
			learningArea.verifyNoTextInputInOpenSegmentByIndex(i+1);
		}
		report.startStep("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnClear();

		report.startStep("Click on See answers and check answers placed");
		learningArea.clickOnSeeAnswer();
		sleep(1);
		for (int i = 0; i < correctAnswers.length; i++) {
			String actualAnswer = learningArea
					.getTextInputInOpenSegmentByIndex(i + 1);
			testResultService.assertEquals(correctAnswers[i], actualAnswer,
					"Answer wrong or not placed into input box");
		}
		report.startStep("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnSeeAnswer();

		report.startStep("Click on See answers check all answers removed");
		learningArea.clickOnSeeAnswer();
		for (int i = 0; i < correctAnswers.length; i++) {
			learningArea.verifyNoTextInputInOpenSegmentByIndex(i+1);
		}

		report.startStep("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnClear();

		report.startStep("Select 1 correct 1 wrong answer");
		learningArea.enterOpenSegmentAnswerTextByIndex(1, correctAnswers[0]);
		learningArea.enterOpenSegmentAnswerTextByIndex(2, correctAnswers[0]);

		report.startStep("Click on Check Answer and check V/X signs placed correctly");
		learningArea.clickOnCheckAnswer();
		learningArea.verifyAnswerOpenEndedCorrect(0);
		learningArea.verifyAnswerOpenEndedWrong(1);

		report.startStep("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnCheckAnswer();

		report.startStep("Click on Check Answer and check correct remains and wrong unselected");
		learningArea.clickOnCheckAnswer();
		String actualAnswer = learningArea.getTextInputInOpenSegmentByIndex(1);
		testResultService.assertEquals(correctAnswers[0], actualAnswer,	"Answer wrong or not placed into input box");
		learningArea.verifyNoTextInputInOpenSegmentByIndex(2);
		
		learningArea.clickOnClearAnswer();

		report.startStep("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnClear();
		
		report.startStep("Press on Headphone btn and check media");
		learningArea.clickOnMediaById("3");
		sleep(1);
		learningArea.checkAudioFile("PMediaPlayer", "b1veafp03.mp3");
				

	}
	
	//test for MTTP test mode
	@Test
	@TestCaseParams(testCaseID = { "24577" }, skippedBrowsers = {"firefox"}) // Actions API not supported by geckodriver)
	public void MatchTextToPicDragAndDropTestMode() throws Exception {
		
	report.startStep("Init Test Data");
		webDriver.maximize();
		String[] words = new String[] { "coal","wind turbine", "solar panels"}; //"pollution"};
		int taskIndex = 2;
		String expectedScore = "5%";
		
	report.startStep("Navigate B3-U9-L6-S4");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "B3",9, 6);
		sleep(1);
		learningArea.clickOnStep(4);
		learningArea.clickOnStartTest();
		sleep(1);
		learningArea.clickOnTask(taskIndex);
		
	report.startStep("Verify instruction text");
		learningArea.VerificationOfQuestionInstruction(matchTextToPicDragAndDropTestMode);
		
	report.startStep("Drag all tiles to droptargets");
		for (int i = 0; i < words.length; i++) {
			dragAndDrop.dragAndDropMatchTextToPicAnswerByTextToId(words[i], i+2);
			sleep(1);}
		
	report.startStep("Navigate to other task and back and check correct tile still placed");
		learningArea.clickOnBackButton();
		sleep(1);
		learningArea.clickOnNextButton();
		sleep(1);
		dragAndDrop.checkTileIsPlaced(words[1]);
		
	report.startStep("Submit test and check score");
		learningArea.submitTest(true);
		String score = learningArea.getTestScore();
		testResultService.assertEquals(expectedScore, score, "Checking Test Score validity");
		
	report.startStep("Check task indication correct/wrong");
		learningArea.checkThatTestTaskMarkPartlyCorrect(taskIndex);
		
	report.startStep("Press on Task  and check Your Answer / Correct Answer values");
		learningArea.clickOnTask(taskIndex);
		learningArea.clickOnYourAnswerTab();
		dragAndDrop.checkTileIsPlacedInTestYourAnswerAndCorrect(words[1]);
		dragAndDrop.checkTileIsPlacedInTestYourAnswerAndWrong(words[0]);
		sleep(1);
		learningArea.clickOnCorrectAnswerTab();
		for (int i = 0; i < words.length; i++) {
			dragAndDrop.checkTileIsPlaced(words[i]);
		}
		
	}
		
	@Test
	@TestCaseParams(testCaseID = { "24577" }, skippedBrowsers = {"firefox"}) // Actions API not supported by geckodriver)
	public void MatchTextToPicDragAndDrop_Media() throws Exception {

		webDriver.maximize();
		String[] words = new String[] { "bed", "apple","dog", "cat", "frog", "girl" };

		report.startStep("Navigate FD-U1-L1-P4");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "FD", 1, 1);
		learningArea.clickOnStep(3);
		learningArea.clickOnTask(3);
		
		report.startStep("Verify Task Instruction");
		learningArea.VerificationOfQuestionInstruction(matchTextToPicDragAndDropInstruction);

		report.startStep("Drag all tiles to droptargets");
		for (int i = 0; i < words.length; i++) {
			dragAndDrop.dragAndDropMatchTextToPicAnswerByTextToId(words[i], i+2);
			sleep(1);
		}
		report.startStep("Click on Clear and check all tiles back to bank");
		learningArea.clickOnClearAnswer();
		for (int i = 0; i < words.length; i++) {
			dragAndDrop.checkCloseTileIsBackToBank(words[i]);
		}

		report.report("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnClear();

		report.report("Click on See answers and check tiles placed");
		learningArea.clickOnSeeAnswer();
		for (int i = 0; i < words.length; i++) {
			dragAndDrop.checkTileIsPlaced(words[i]);
		}

		report.report("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnSeeAnswer();

		report.report("Click on See answers and check tiles back to bank");
		learningArea.clickOnSeeAnswer();
		for (int i = 0; i < words.length; i++) {
			dragAndDrop.checkCloseTileIsBackToBank(words[i]);
		}

		report.report("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnClear();

		report.startStep("Drag 1 tile to correct place and 1 to wrong place");
		String correctAns = words[0];
		int correctTarget = 3;
		String wrongAns = words[1];
		int wrongTarget = 5;
		dragAndDrop.dragAndDropMatchTextToPicAnswerByTextToId(correctAns, correctTarget);
		dragAndDrop.dragAndDropMatchTextToPicAnswerByTextToId(wrongAns, wrongTarget);

		report.startStep("Click on Check Answer and check V/X signs placed correctly");
		learningArea.clickOnCheckAnswer();
		dragAndDrop.checkDragAndDropCorrectAnswerCloze(correctAns);
		dragAndDrop.checkDragAndDropWrongAnswerCloze(wrongAns);

		report.report("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnCheckAnswer();

		report.startStep("Click on Check Answer and check correct tile remains and wrong tile back to bank");
		learningArea.clickOnCheckAnswer();
		dragAndDrop.checkCloseTileIsBackToBank(wrongAns);
		dragAndDrop.checkTileIsPlaced(correctAns);
		learningArea.clickOnClearAnswer();

		report.report("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnClear();

		report.startStep("Check you can replace current tile by the next tile by drag on it");
		String currentTile = words[2];
		String nextTile = words[3];
		int targetId = 4;
		dragAndDrop.dragAndDropMatchTextToPicAnswerByTextToId(currentTile, targetId);
		dragAndDrop.checkTileIsPlaced(currentTile);
		sleep(1);
		dragAndDrop.dragAndDropMatchTextToPicAnswerByTextToId(nextTile, targetId);
		dragAndDrop.checkTileIsPlaced(nextTile);
		dragAndDrop.checkCloseTileIsBackToBank(currentTile);

		report.startStep("Hover on tile and check media");
		dragAndDrop.hoverOnMatchTextToPicAnswerByTextToId(words[0]);
		learningArea.checkAudioFile("PMediaPlayer", "s1alaap04.mp3");
		
		
	}
	
	@Test
	@TestCaseParams(testCaseID = { "24605" })
	public void CloseWholeBmpDragAndDrop_Media() throws Exception {

		webDriver.maximize();
		String[] words = new String[] { "When", "study","Tuesday", "English", "Thursday" };

		report.startStep("Navigate FD-U5-L7-P1");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "FD", 5, 7);
		learningArea.clickOnStep(3);
		
		report.startStep("Verify Task Instruction");
		learningArea.VerificationOfQuestionInstruction(closeWholeBmpDragAndDropInstruction);
		
		report.startStep("Drag all tiles to droptargets");
		dragAndDrop.dragAndDropCloseAnswerByTextToId(words[0], "1_1");
		sleep(1);
		dragAndDrop.dragAndDropCloseAnswerByTextToId(words[1], "1_2");
		sleep(1);
		dragAndDrop.dragAndDropCloseAnswerByTextToId(words[2], "1_3");
		sleep(1);
		dragAndDrop.dragAndDropCloseAnswerByTextToId(words[3], "2_1");
		sleep(1);
		dragAndDrop.dragAndDropCloseAnswerByTextToId(words[4], "2_2");
		sleep(1);
				
		
		report.startStep("Click on Clear and check all tiles back to bank");
		learningArea.clickOnClearAnswer();
		for (int i = 0; i < words.length; i++) {
			dragAndDrop.checkCloseTileIsBackToBank(words[i]);
		}

		report.report("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnClear();

		report.report("Click on See answers and check tiles placed");
		learningArea.clickOnSeeAnswer();
		for (int i = 0; i < words.length; i++) {
			dragAndDrop.checkTileIsPlaced(words[i]);
		}

		report.report("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnSeeAnswer();

		report.report("Click on See answers and check tiles back to bank");
		learningArea.clickOnSeeAnswer();
		for (int i = 0; i < words.length; i++) {
			dragAndDrop.checkCloseTileIsBackToBank(words[i]);
		}

		report.report("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnClear();

		report.startStep("Drag 1 tile to correct place and 1 to wrong place");
		String correctAns = words[0];
		String correctTarget = "1_1";
		String wrongAns = words[1];
		String wrongTarget = "2_1";
		dragAndDrop.dragAndDropCloseAnswerByTextToId(correctAns, correctTarget);
		dragAndDrop.dragAndDropCloseAnswerByTextToId(wrongAns, wrongTarget);

		report.startStep("Click on Check Answer and check V/X signs placed correctly");
		learningArea.clickOnCheckAnswer();
		dragAndDrop.checkDragAndDropCorrectAnswerCloze(correctAns);
		dragAndDrop.checkDragAndDropWrongAnswerCloze(wrongAns);

		report.report("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnCheckAnswer();

		report.startStep("Click on Check Answer and check correct tile remains and wrong tile back to bank");
		learningArea.clickOnCheckAnswer();
		dragAndDrop.checkCloseTileIsBackToBank(wrongAns);
		dragAndDrop.checkTileIsPlaced(correctAns);
		learningArea.clickOnClearAnswer();

		report.report("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnClear();

		report.startStep("Check you can replace current tile by the next tile by drag on it");
		String currentTile = words[2];
		String nextTile = words[3];
		String targetId = "1_2";
		dragAndDrop.dragAndDropCloseAnswerByTextToId(currentTile, targetId);
		dragAndDrop.checkTileIsPlaced(currentTile);
		sleep(1);
		dragAndDrop.dragAndDropCloseAnswerByTextToId(nextTile, targetId);
		dragAndDrop.checkTileIsPlaced(nextTile);
		dragAndDrop.checkCloseTileIsBackToBank(currentTile);

		report.startStep("Press on Headphone btn and check media");
		learningArea.clickOnMediaById("1");
		sleep(1);
		learningArea.checkAudioFile("PMediaPlayer", "s1vealp01.mp3");
	
	}

	@Test
	@TestCaseParams(testCaseID = { "25137" })
	public void OpenEndedSmallSegments() throws Exception {

		// answers test data
		
		String correct1 = "player";
		String correct2 = "win";
		String correct3 = "championship";
		String correct4 = "greatest";
		String correct5 = "motivates";
		String correct6 = "motivation";
		String correct7 = "diploma";
		String correct8 = "passing";
		
		String [] correctAnswers = new String [] {correct1,correct2,correct3,correct4,correct5,correct6,correct7,correct8};

		report.startStep("Navigate I1-U1-L1-P5");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "I1", 1, 1);
		learningArea.clickOnStep(2);
		learningArea.clickOnTask(4);
		sleep(1);
		
		report.startStep("Verify instruction text");
		learningArea.VerificationOfQuestionInstruction(openEndedSmallSegmentsInstruction);

		report.startStep("Input all answers");
		for (int i = 0; i < correctAnswers.length; i++) {
			learningArea.enterOpenSmallSegmentAnswerTextByIndex(i+1, correctAnswers[i]);
			sleep(1);
		}
		
		report.startStep("Click on Clear and check all answers unselected");
		learningArea.clickOnClearAnswer();
		for (int i = 0; i < correctAnswers.length; i++) {
			learningArea.verifyNoTextInputInOpenSmallSegmentByIndex(i+1);
		}
		report.startStep("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnClear();

		report.startStep("Click on See answers and check answers placed");
		learningArea.clickOnSeeAnswer();
		sleep(1);
		for (int i = 0; i < correctAnswers.length; i++) {
			String actualAnswer = learningArea
					.getTextInputInOpenSmallSegmentByIndex(i + 1);
			testResultService.assertEquals(correctAnswers[i], actualAnswer,
					"Answer wrong or not placed into input box");
		}
		report.startStep("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnSeeAnswer();

		report.startStep("Click on See answers check all answers removed");
		learningArea.clickOnSeeAnswer();
		for (int i = 0; i < correctAnswers.length; i++) {
			learningArea.verifyNoTextInputInOpenSmallSegmentByIndex(i+1);
		}

		report.startStep("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnClear();

		report.startStep("Select 1 correct 1 wrong answer");
		learningArea.enterOpenSmallSegmentAnswerTextByIndex(1, correctAnswers[0]);
		learningArea.enterOpenSmallSegmentAnswerTextByIndex(2, correctAnswers[0]);

		report.startStep("Click on Check Answer and check V/X signs placed correctly");
		learningArea.clickOnCheckAnswer();
		learningArea.verifyAnswerOpenEndedCorrect(0);
		learningArea.verifyAnswerOpenEndedWrong(1);

		report.startStep("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnCheckAnswer();

		report.startStep("Click on Check Answer and check correct remains and wrong unselected");
		learningArea.clickOnCheckAnswer();
		String actualAnswer = learningArea.getTextInputInOpenSmallSegmentByIndex(1);
		testResultService.assertEquals(correctAnswers[0], actualAnswer,	"Answer wrong or not placed into input box");
		learningArea.verifyNoTextInputInOpenSmallSegmentByIndex(2);
		
		learningArea.clickOnClearAnswer();

		report.startStep("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnClear();
						

	}
	
	@Test
	//@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "33783" }, skippedBrowsers = {"firefox"}) // Actions API not supported by geckodriver)
	public void InsertTextNewTemp() throws Exception {

		//loginToEnvAT();
		
		report.startStep("Init test data");
		int taskIndex = 5;
		int wrongMarkerNum = 1; 
		int correctMarkerNum = 2; 
		int numOfMarkers = 4;
					
		
		report.startStep("Navigate B1-U9-L5-S3");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "B1", 9, 5);
			
			/*homePage.navigateToRequiredCourseOnHomePage("Basic 1");
			homePage.clickOnUnitLessons(9);
			homePage.clickOnLesson(8, 5);*/
		sleep(1);
		learningArea.clickOnStep(3);
		sleep(1);
		learningArea.clickOnTask(taskIndex);
		sleep(1);
			
		report.startStep("Verify instruction text");
		learningArea.VerificationOfQuestionInstruction(insertTextInstruction);
			
		report.startStep("Check Text Resource contains insert buttons");
		markers = learningArea.getInsertTextButtonsElements();
		testResultService.assertEquals(numOfMarkers, markers.size(), "Insert text buttons not found or their number is not as expected");
			
		report.startStep("Check Sentence answer");
		WebElement answerSentence = learningArea.getInsertSentenceAnswerElement();
		testResultService.assertEquals(true, answerSentence.isDisplayed(), "Insert text answer not found");
		testResultService.assertEquals(insertTextAnswerSentence, answerSentence.getText(), "Insert text answer not correct");
			
		report.startStep("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnClear();
			
		report.startStep("Mouse hover on insert btn highlight");
		String imagePosition = learningArea.hoverOnInsertBtnAndGetImageSpritePositionY(2);
		testResultService.assertEquals("550px", imagePosition, "Button is not highlighted after hover");
			
		report.startStep("Click on markers and check sentence inserted & answer greyed out on right side");
			for (int i = 0; i < markers.size(); i++) {
				learningArea.clickOnMarkerAndVerifySentenceInsert(i+1,insertTextAnswerSentence);
					
			}
			
		report.startStep("Click on clear and verify no sentence in left side");
		learningArea.clickOnClearAnswer();
		checkInsertTextTemplateDefaultState();
			
		report.startStep("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnClear();
			
		report.startStep("Click on See Answer and verify correct answer placed");
		learningArea.clickOnSeeAnswer();
		sleep(1);
		learningArea.verifySentenceInsertOnSeeAnswer(correctMarkerNum, insertTextAnswerSentence);
			
		report.startStep("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnSeeAnswer();
			
		report.startStep("Click on See Answer and verify no sentence in left side");
		learningArea.clickOnSeeAnswer();
		checkInsertTextTemplateDefaultState();
			
		report.startStep("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnClear();
			
		report.startStep("Click on correct marker");
		learningArea.clickOnMarkerAndVerifySentenceInsert(correctMarkerNum,insertTextAnswerSentence);

		report.startStep("Click on Check Answer and verify V mark placed");
		learningArea.clickOnCheckAnswer();
		learningArea.verifySentenceMarkedAsCorrectOnCheckAnswer(correctMarkerNum);
			
		report.startStep("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnCheckAnswer();
			
		report.startStep("Click on Check Answer and verify no sentence in left side");
		learningArea.clickOnCheckAnswer();
		checkInsertTextTemplateDefaultState();
			
		report.startStep("Click on wrong marker");
		learningArea.clickOnMarkerAndVerifySentenceInsert(wrongMarkerNum,insertTextAnswerSentence);

		report.startStep("Click on Check Answer and verify X mark placed");
		learningArea.clickOnCheckAnswer();
		learningArea.verifySentenceMarkedAsWrongOnCheckAnswer(wrongMarkerNum);
			
		report.startStep("Click on Check Answer and verify no sentence in left side");
		learningArea.clickOnCheckAnswer();
		checkInsertTextTemplateDefaultState();
			
		report.startStep("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnClear();
			
		report.startStep("Go to previous task and back - check default state");
		learningArea.clickOnBackButton();
		learningArea.clickOnTask(taskIndex);
		sleep(1);
		checkInsertTextTemplateDefaultState();
			
		/*	report.startStep("Logout");
			learningArea.clickOnLogoutLearningArea();
			testResultService.assertEquals(false, loginPage.isSubmitButtonEnabled(), "Checking that ED Login Page displayed");
		*/
				
	}
		
	@Test
	//@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "33638" })
	public void EditTextNewTemp() throws Exception {

		
		//loginToEnvAT();
		
		report.startStep("Init test data");
		int taskIndex = 0;
		int correctInputBoxNum = 1;
		int wrongInputBoxNum = 2;
							
		String[] correctWords = new String[] { "When", "are", "goes","walk", "call" };
				
		report.startStep("Navigate B3-U9-L5-S4");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "B3", 9, 5);
			/*homePage.navigateToRequiredCourseOnHomePage("Basic 3");
			homePage.clickOnUnitLessons(9);
			homePage.clickOnLesson(8, 5);*/
		sleep(1);
		learningArea.clickOnStep(4);
		sleep(1);
		learningArea.clickOnTask(taskIndex);
		sleep(1);
					
		report.startStep("Verify instruction text");
		learningArea.VerificationOfQuestionInstruction(editTextInstruction);
					
		report.startStep("Check the same content in both sides");
		checkEditTextDefaultState();
					
		report.startStep("Check input boxes can be edited");
		for (int i = 0; i < rightContentInputsElements.size();  i++) {
			learningArea.editTextByInputBoxNumber(i+1, "sample");
		}
			
		report.startStep("Click on clear and verify default state");
		learningArea.clickOnClearAnswer();
		checkEditTextDefaultState();
			
		report.startStep("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnClear();
			
		report.startStep("Click on See Answer and verify correct answer placed");
		learningArea.clickOnSeeAnswer();
		sleep(1);
		for (int i = 0; i < rightContentInputsElements.size();  i++) {
			testResultService.assertEquals(correctWords[i], rightContentInputsElements.get(i).getAttribute("value"), "Correct text not inserted or wrong");
		}
			
		report.startStep("Click on See Answer and verify no sentence in left side");
		learningArea.clickOnSeeAnswer();
		checkEditTextDefaultState();
			
		report.startStep("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnClear();
			
		report.startStep("Insert correct edit text");
		learningArea.editTextByInputBoxNumber(correctInputBoxNum, correctWords[0]);
			
		report.startStep("Click on Check Answer and verify V mark placed");
		learningArea.clickOnCheckAnswer();
		learningArea.verifyEditTextCorrectMarkByInputNumber(correctInputBoxNum);
			
		report.startStep("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnCheckAnswer();
			
		report.startStep("Click on Check Answer and verify correct answer left");
		learningArea.clickOnCheckAnswer();
		testResultService.assertEquals(correctWords[0], rightContentInputsElements.get(0).getAttribute("value"), "Correct text not inserted or wrong");
			
		report.startStep("Insert wrong edit text");
		learningArea.editTextByInputBoxNumber(wrongInputBoxNum, correctWords[0]+"incorrect");
			
		report.startStep("Click on Check Answer and verify X mark placed");
		learningArea.clickOnCheckAnswer();
		learningArea.verifyEditTextWrongtMarkByInputNumber(wrongInputBoxNum);
			
		report.startStep("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnCheckAnswer();
			
		report.startStep("Click on Check Answer and verify wrong answer not left");
		learningArea.clickOnCheckAnswer();
		testResultService.assertEquals(leftContentInputsElements.get(1).getText().trim(), rightContentInputsElements.get(1).getAttribute("value").trim(), "Wrong text not removed");
					
		report.startStep("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnClear();
				
		report.startStep("Go to prev task and back - check default state");
		learningArea.clickOnBackButton();
		sleep(2);
		learningArea.clickOnNextButton();
		sleep(1);
		learningArea.clickOnTask(taskIndex);
		checkEditTextDefaultState();
		
	/*		report.startStep("Logout");
			learningArea.clickOnLogoutLearningArea();
			testResultService.assertEquals(false, loginPage.isSubmitButtonEnabled(), "Checking that ED Login Page displayed");
	*/	
				
	}
	
	@Test
	@TestCaseParams(testCaseID = { "33903" })
	public void HighlightTextNewTemp() throws Exception {

		
		int taskIndex = 1;
		int stepNum = 3;
		int correctAnswerNum = 4;
		
		//loginToEnvAT();
				
			report.startStep("Navigate I1-U10-L2-S3");
			homePage.navigateToCourseUnitAndLesson(courseCodes, "I1", 10, 2);
			sleep(1);
			learningArea.clickOnStep(stepNum);
			sleep(1);
			learningArea.clickOnTask(taskIndex);
			sleep(1);
					
			report.startStep("Verify instruction text");
			learningArea.VerificationOfQuestionInstruction(highlightTextInstruction);
					
			report.startStep("Check the highlighted words in text correspond to highlighted word in question");
			highlightedText = learningArea.getHighlightedTextElements();
			highlightedAnswer = learningArea.getHighlightedQuestionElements();
			for (int i = 0; i < highlightedText.size(); i++) {
				testResultService.assertEquals(highlightedText.get(i).getText().toLowerCase(),highlightedAnswer.get(0).getText().toLowerCase(),"Check highlighted texts in right & lift side");
			}
			
			
			report.startStep("Select all answers");
			learningArea.selectAnswerRadioByTextNum(1, 1);
			
			report.startStep("Click on Clear and check all answers unselected");
			learningArea.clickOnClearAnswer();
			learningArea.checkAllAnswersUnselectedMCQ();

			report.startStep("Verify Practice Tools State");
			learningArea.verifyPracticeToolsStateOnClear();

			report.startStep("Click on See answers and check answers selected");
			learningArea.clickOnSeeAnswer();
			learningArea.verifyAnswerRadioSelectedCorrect(1, correctAnswerNum);
			
			report.startStep("Verify Practice Tools State");
			learningArea.verifyPracticeToolsStateOnSeeAnswer();

			report.startStep("Click on See answers check all answers unselected");
			learningArea.clickOnSeeAnswer();
			learningArea.checkAllAnswersUnselectedMCQ();

			report.startStep("Verify Practice Tools State");
			learningArea.verifyPracticeToolsStateOnClear();

			report.startStep("Select 1 correct answer");
			learningArea.selectAnswerRadioByTextNum(1, correctAnswerNum);
			
			report.startStep("Click on Check Answer and check V/X signs placed correctly");
			learningArea.clickOnCheckAnswer();
			learningArea.verifyAnswerRadioSelectedCorrect(1, correctAnswerNum);

			report.startStep("Verify Practice Tools State");
			learningArea.verifyPracticeToolsStateOnCheckAnswer();

			report.startStep("Click on Check Answer and check correct remains and wrong unselected");
			learningArea.clickOnCheckAnswer();
			learningArea.checkSingleAnswerSelectedMCQ(1, correctAnswerNum);
			learningArea.clickOnClearAnswer();

			report.startStep("Verify Practice Tools State");
			learningArea.verifyPracticeToolsStateOnClear();

			report.startStep("Check next selection unselect current");
			learningArea.selectAnswerRadioByTextNum(1, 1);
			learningArea.selectAnswerRadioByTextNum(1, 2);
			learningArea.checkSingleAnswerUnselectedMCQ(1, 1);
		
			/*report.startStep("Logout");
			learningArea.clickOnLogoutLearningArea();
			testResultService.assertEquals(false, loginPage.isSubmitButtonEnabled(), "Checking that ED Login Page displayed");
			 */
					
		
	}
	
	@Test
	//@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "33847" })
	public void SelectTextNewTemp() throws Exception {

		// temporary opening of AT environment
		//loginToEnvAT();
						
		report.startStep("Init test data");
		int taskIndex = 1;
		String correctAnswer = "Walk your dog in the morning."; 
		int correctMarkerNum = 3;
		int wrongMarkerNum = 1;
				
		
		report.startStep("Navigate B1-U9-L1-S5");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "B1", 9, 1);
		//homePage.navigateToRequiredCourseOnHomePage("Basic 1");
		//homePage.clickOnUnitLessons(9);
		//homePage.clickOnLesson(8, 1);
		sleep(1);
					
		learningArea.clickOnStep(5);
		sleep(1);
						
		learningArea.clickOnTask(taskIndex);
		sleep(1);
						
		report.startStep("Check Select Text item default state");
		checkSelectTextTemplateDefaultState();
			
		report.startStep("Click on markers and check sentence selected & placed in answer area in right side");
			for (int i = 0; i < markers.size(); i++) {
				learningArea.clickOnMarkerAndVerifySentenceSelect(i+1);
					
			}
			
		report.startStep("Click on clear and verify no sentence in left side");
		learningArea.clickOnClearAnswer();
		checkSelectTextTemplateDefaultState();
					
		report.startStep("Click on See Answer and verify correct answer placed");
		learningArea.clickOnSeeAnswer();
		sleep(1);
		learningArea.verifySentenceSelectOnSeeAnswer(correctAnswer);
			
		report.startStep("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnSeeAnswer();
			
		report.startStep("Click on See Answer and verify no sentence in left side");
		learningArea.clickOnSeeAnswer();
		checkSelectTextTemplateDefaultState();
					
		report.startStep("Click on correct marker");
		learningArea.clickOnMarkerAndVerifySentenceSelect(correctMarkerNum);

		report.startStep("Click on Check Answer and verify V mark placed");
		learningArea.clickOnCheckAnswer();
		learningArea.verifySentenceSelectedAsCorrectOnCheckAnswer(correctMarkerNum);
			
		report.startStep("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnCheckAnswer();
			
		report.startStep("Click on Check Answer and verify no sentence in right side");
		learningArea.clickOnCheckAnswer();
		checkSelectTextTemplateDefaultState();
			
		report.startStep("Click on wrong marker");
		learningArea.clickOnMarkerAndVerifySentenceSelect(wrongMarkerNum);

		report.startStep("Click on Check Answer and verify X mark placed");
		learningArea.clickOnCheckAnswer();
		learningArea.verifySentenceSelectedAsWrongOnCheckAnswer(wrongMarkerNum);
			
		report.startStep("Click on Check Answer and verify no sentence in left side");
		learningArea.clickOnCheckAnswer();
		checkSelectTextTemplateDefaultState();
					
		report.startStep("Go to next task and back - check default state");
		learningArea.clickOnNextButton();
		learningArea.clickOnTask(taskIndex);
		sleep(1);
		checkSelectTextTemplateDefaultState();
				
		/*report.startStep("Logout");
		learningArea.clickOnLogoutLearningArea();
		testResultService.assertEquals(false, loginPage.isSubmitButtonEnabled(), "Checking that ED Login Page displayed");
		*/
	}
		
	@Test
	//@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "34160" })
	public void OpenWritingNewTemp() throws Exception {
	
		bTestFailed = false;  // igb 2018.08.06
		
		try {
			report.startStep("Create and login to ED as student and navigate to Open Writing task");
			sleep(2);
			homePage.clickOnLogOut();
			sleep(2);
			webDriver.switchToTopMostFrame();
			String classNameOR = configuration.getProperty("classname.openSpeech");
			String institutionId = configuration.getInstitutionId();
			studentId = pageHelper.createUSerUsingSP(institutionId, classNameOR);
			String studentUserNameOR = dbService.getUserNameById(studentId, institutionId);
							
			loginPage = new NewUXLoginPage(webDriver,testResultService);
			homePage = loginPage.loginAsStudent(studentUserNameOR, "12345");
			sleep(1);
	
			report.startStep("Navigate to B1-U9-L3-S4");
			homePage.navigateToCourseUnitAndLesson(courseCodes, "B1", 9, 3);
			learningArea.clickOnStep(4);
			learningArea.clickOnTask(2);
			sleep(1);
		
			report.startStep("Verify instruction text");
			learningArea.VerificationOfQuestionInstruction(openWritingtInstruction);
	
			report.startStep("Enter answer text and submit");
			Date date = new Date();
			String uniqueLabel = date.toString();
			
			String answerOW = "automated answer for open writing: "+ uniqueLabel;
			webDriver.swithcToFrameAndSendKeys("//body[@id='tinymce']", answerOW, "elm1_ifr");
			webDriver.switchToTopMostFrame();
			learningArea.clickOnSubmitToTeacherOW();
			learningArea.validateAlertModalByMessage(openWritingConfirmationAlert,true);
			
			report.startStep("Logout as a student");
			learningArea.clickOnLogoutLearningArea();
			webDriver.switchToTopMostFrame();
			loginPage = new NewUXLoginPage(webDriver, testResultService);
			testResultService.assertEquals(false, loginPage.isSubmitButtonEnabled(), "Checking that ED Login Page displayed");
			
			report.startStep("Login as Teacher");
			String tName = configuration.getProperty("teacher.username");
			String tID = dbService.getUserIdByUserName(tName, institutionId);
			pageHelper.setUserLoginToNull(tID);
			sleep(5);
			tmsHomePage = loginPage.enterTeacherUserAndPassword();
			sleep(2);
		
			tmsHomePage = new TmsHomePage(webDriver, testResultService);
			tmsHomePage.switchToMainFrame();
		
			report.startStep("Open teachers Inbox page");
			tmsHomePage.clickOnCommunication();
			sleep(10);
			tmsHomePage.clickOnInbox();
			sleep(3);
			
			report.startStep("Open latest students assignment");
			tmsHomePage.switchToTableFrame();
			tRead = tmsHomePage.openLatestStudentMessage();
			sleep(1);
			webDriver.switchToNextTab();
			
			report.startStep("Verify recieved student answer");
			testResultService.assertEquals(answerOW, tRead.getStudentAnswerText(), "Student answer does not match to actually sent by student");
				
			report.startStep("Reply to student");
			tWrite = tRead.clickOnReply();
			webDriver.switchToPopup();
			
			tWrite.enterSubject("Reply: "+ uniqueLabel);
			tWrite.enterTextIntoMessageBody(answerOW);
			tRead = tWrite.clickOnSend();
			webDriver.switchToNextTab();
			tRead.clickOnClose();
			sleep(1);
	
			report.startStep("Logout as teacher");
			webDriver.switchToMainWindow();
			tmsHomePage.switchToMainFrame();
			tmsHomePage.clickOnExit();
			webDriver.switchToTopMostFrame();
			testResultService.assertEquals(false, loginPage.isSubmitButtonEnabled(), "Checking that ED Login Page displayed");
			sleep(5);
			
			report.startStep("Login as student");
			loginPage.loginAsStudent(studentUserNameOR,"12345");
			sleep(5);
			
			report.startStep("Click on Inbox");
			inboxPage = homePage.openInboxPage(false);
			sleep(5);
			inboxPage.switchToInboxFrame();
			inboxPage.verifyInboxPageTitle();
			
			report.startStep("Verify recieved teacher reply");
			inboxPage.clickOnElement("//table[@id='tblInbox']/tbody/tr[5]/td[3]/a");
			webDriver.switchToPopup();
			webDriver.switchToFrame("ReadWrite");
			testResultService.assertEquals(answerOW, inboxPage.getInboxMessageText(), "Teacher reply does not match to actually sent by teacher");
		}
		catch(Exception e)
		{
			bTestFailed = true;
			report.reportFailure(e.getMessage());
		}
	}
	
	@Test
	@TestCaseParams(testCaseID = { "36819" }, skippedBrowsers = {"firefox"}) // Actions API not supported by geckodriver)
	public void ClassificationDragAndDropTestMode() throws Exception {
		
		// temporary opening of AT environment
		//loginToEnvAT();
			
		
		report.startStep("Init Test Data");
		String[] words = new String[] { "swimming pool", "gym", "sports field", "dancing", "lifting weights", "swimming","park","running" };
		
		int taskIndex = 2;
		String expectedScore = "3%";
		String wrongTile = words[4];
		String correctTile = words[3];
		int correctRow = 1, correctCol = 1;
		
		
			
		report.startStep("Navigate B1-U9-L1-T3");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "B1", 9, 1);
		/*homePage.navigateToRequiredCourseOnHomePage("Basic 1");
		sleep(1);
		homePage.clickOnUnitLessons(9);
		homePage.clickOnLesson(8, 1);*/
		sleep(1);
		learningArea.clickOnStep(6);
		learningArea.clickOnStartTest();
		sleep(1);
		learningArea.clickOnTask(taskIndex);
		
		report.startStep("Verify instruction text");
		learningArea.VerificationOfQuestionInstruction(classificationDragAndDropTestInstruction);

		report.startStep("Drag all tiles to droptargets");
		int row = 1;
		int column = 1;
		for (int i = 0; i < words.length; i++) {
					
			if (i%2==0) { 
				if (i!=0) {
					row++;
					column = 1;
					}
			} else column = 2; 
			
			dragAndDrop.dragAndDropClassificationAnswerByTextToColumn(words[i], row, column);
			sleep(1);
						
		}
		
		report.startStep("Drag all tiles to bank");
		for (int i = 0; i < words.length; i++) {
			dragAndDrop.dragClassificationAnswerToBankCloze(words[i]);
			sleep(1);
		}
		
		report.startStep("Check you can replace current tile by the next tile by drag on it");
	
		dragAndDrop.dragAndDropClassificationAnswerByTextToColumn(wrongTile, correctRow, correctCol);
		dragAndDrop.checkTileIsPlaced(wrongTile);
		sleep(1);
		dragAndDrop.dragAndDropClassificationAnswerByTextToColumn(correctTile, correctRow, correctCol);
		dragAndDrop.checkTileIsPlaced(correctTile);
		dragAndDrop.checkCloseTileIsBackToBank(wrongTile);
	
		report.startStep("Navigate to other task and back and check correct tile still placed");
		learningArea.clickOnBackButton();
		sleep(1);
		learningArea.clickOnNextButton();
		sleep(1);
		dragAndDrop.checkTileIsPlaced(correctTile);
		
		report.startStep("Place wrong tile to wrong column");
		dragAndDrop.dragAndDropClassificationAnswerByTextToColumn(wrongTile, correctRow, correctCol+1);
		
		report.startStep("Submit test and check score");
		learningArea.submitTest(true);
		String score = learningArea.getTestScore();
		testResultService.assertEquals(expectedScore, score, "Checking Test Score validity");
		
		report.startStep("Check classification task indication correct/wrong");
		learningArea.checkThatTestTaskMarkPartlyCorrect(taskIndex);
		
		report.startStep("Press on Task 3 and check Your Answer / Correct Answer values");
		learningArea.clickOnTask(taskIndex);
		learningArea.clickOnYourAnswerTab();
		dragAndDrop.checkTileIsPlacedInTestYourAnswerAndCorrect(correctTile);
		dragAndDrop.checkTileIsPlacedInTestYourAnswerAndWrong(wrongTile);
		sleep(1);
		
		learningArea.clickOnCorrectAnswerTab();
		for (int i = 0; i < words.length; i++) {
			dragAndDrop.checkTileIsPlaced(words[i]);
		}
			
		/*
		report.startStep("Logout");
		learningArea.clickToOpenNavigationBar();
		learningArea.clickOnHomeButton();
		sleep(1);
		homePage.clickOnLogOut();
		testResultService.assertEquals(false, loginPage.isSubmitButtonEnabled(), "Checking that ED Login Page displayed");
		*/
		
	}
			
	private void checkInsertTextTemplateDefaultState() throws Exception {
	learningArea.clickOnClearAnswer();
		for (int i = 0; i < markers.size(); i++) {
			learningArea.verifyNoSentenceInsertedByMarkerNumber(i+1);
		}
		learningArea.verifyInsertSentenceAnswerEnabled();
	
		report.startStep("Check Text Resource tools hidden in Left Side");
		learningArea.checkThatHearAllIsNotDisplayed();
		//learningArea.checkThatMainIdeaToolIsNotDisplayed();
		//learningArea.checkThatKeywordsToolIsNotDisplayed();
		//learningArea.checkThatReferenceWordsToolIsNotDisplayed();
		learningArea.checkThatPrintIsNotDisplayed();
		
		/*report.startStep("Check Text Segment tools not active in Left Side"); // UNCOMMENT: will work with real text and segments
		learningArea.selectSegmentInReadingByNumber(1,2);
		learningArea.checkThatSeeTranslationNotDisplayed();
		learningArea.checkThatHearPartIsNotDisplayed();*/
		
	}
	
	private void checkSelectTextTemplateDefaultState() throws Exception {
		
		markers = learningArea.getSelectTextButtonsElements();
		testResultService.assertEquals(true, markers.size()<=5 && markers.size() != 0 , "Select text buttons not found or their number is more then 5");
		for (int i = 0; i < markers.size(); i++) {
			Boolean isSelected = markers.get(i).getAttribute("class").contains("selected");
			testResultService.assertEquals(false, isSelected, "Text was selected though it should not");
		}
						
		WebElement answerSentence = learningArea.getSelectSentenceAnswerElement();
		testResultService.assertEquals(true, answerSentence.getAttribute("class").contains("disabled"), "Select Text answer area not disabled");
		testResultService.assertEquals("Select Text", answerSentence.getText(), "Select Text answer area label not correct");
		
		report.startStep("Verify Practice Tools State");
		learningArea.verifyPracticeToolsStateOnClear();
		
		report.startStep("Verify instruction text");
		//learningArea.VerificationOfQuestionInstruction(selectTextInstruction);

		report.startStep("Check Text Resource tools hidden in Left Side");
		learningArea.checkThatHearAllIsNotDisplayed();
		//learningArea.checkThatMainIdeaToolIsNotDisplayed();
		//learningArea.checkThatKeywordsToolIsNotDisplayed();
		//learningArea.checkThatReferenceWordsToolIsNotDisplayed();
		learningArea.checkThatPrintIsNotDisplayed();
		
		/*report.startStep("Check Text Segment tools not active in Left Side"); // UNCOMMENT: will work with real text and segments
		learningArea.selectSegmentInReadingByNumber(1,3);
		learningArea.checkThatSeeTranslationNotDisplayed();
		learningArea.checkThatHearPartIsNotDisplayed();*/
				
		}
	
	private void checkEditTextDefaultState() throws Exception { 
	
	content = learningArea.getEditTextContentElements();
	leftContentInputsElements = content.get(0).findElements(By.xpath("//div[@class='writingEditResourceSide']//span[contains(@class,'et 220')]"));
	//leftContentInputsElements = content.get(0).findElements(By.xpath("//span[contains(@class,'et 220')]"));
	rightContentInputsElements = content.get(1).findElements(By.tagName("input"));
	
		
	String leftContent = content.get(0).getText();
	String rightContent = content.get(1).getText();
			
	String leftContentAfterReplace = leftContent.replace(" ", "").replace("\"", "").replaceAll("\\t", "");
	String rightContentAfterReplace = rightContent.replace(" ", "").replace("\"", "").replaceAll("\\t", "");
	String rightContentHtml = content.get(1).getAttribute("innerHTML").toString().replace(" ", "");
	String [] segments = new String [] {};
	if (webDriver instanceof FirefoxWebDriver) segments = rightContentHtml.split("type=\"text\">");
	else segments = rightContentHtml.split("px;\">");
	
	String segment = "";
	for (int i = 0; i < leftContentInputsElements.size(); i++) {
		leftContentAfterReplace = leftContentAfterReplace.replace(leftContentInputsElements.get(i).getText().trim(),rightContentInputsElements.get(i).getAttribute("value").trim()).replaceAll("\n", "");
		segment = segments [i+1] .substring(0,20).replace("\n","").replace("</p><p>","").replace("</p>","").replace("\t", "").replace("<","").replace(">", "").replace("/", "").replace("span", "").replace("class", "").replace("=", "").replace("\"", "").replace("unSegment", "").replace("<p>", "").replace("unSeg", "").replace(" ", "").trim();
		rightContentAfterReplace = rightContentAfterReplace.replace(segment, leftContentInputsElements.get(i).getText() + segment).replaceAll("\n", "");
	}
	
	String left = leftContentAfterReplace.replace(" ", "").trim().replace("\n", "");;
	String right = rightContentAfterReplace.replace(" ", "").trim().replace("\n", "");
	testResultService.assertEquals(left, right, "Content is different in right and left side");
	}
	
	private void loginToEnvAT() throws Exception { 
		
		report.startStep("Login to ED as student - AT env");
		sleep(2);
		homePage.clickOnLogOut();
		loginPage = new NewUXLoginPage(webDriver,testResultService);
		testResultService.assertEquals(false, loginPage.isSubmitButtonEnabled(), "Checking that ED Login Page displayed");
		sleep(2);
		webDriver.openUrl(configuration.getSutUrl());
		sleep(2);
		String userName = configuration.getStudentUserName();
		homePage = loginPage.loginAsStudent(userName, configuration.getStudentPassword());
		report.startStep("Skip Walkthrough tour");        

		for (int n = 0; n < 12; n++) {
             sleep(1);
             webDriver.sendKey(Keys.ARROW_RIGHT);                
          }
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
