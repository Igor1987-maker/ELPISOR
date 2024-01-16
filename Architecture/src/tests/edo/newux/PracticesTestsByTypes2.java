package tests.edo.newux;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.tools.ant.Task;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.google.inject.util.Types;

import Enums.ByTypes;
import Enums.TaskTypes;
import drivers.FirefoxWebDriver;
import pageObjects.edo.NewUXLoginPage;
import pageObjects.edo.NewUxDragAndDropSection;
import pageObjects.edo.NewUxDragAndDropSection2;
import pageObjects.edo.NewUxHomePage;
import pageObjects.edo.NewUxLearningArea;
import pageObjects.edo.NewUxLearningArea2;
import pageObjects.edo.NewUxMessagesPage;
import pageObjects.tms.TeacherReadMessagePage;
import pageObjects.tms.TeacherWriteMessagePage;
import pageObjects.tms.TmsHomePage;
import testCategories.AngularLearningArea;
import testCategories.edProduction;
import testCategories.inProgressTests;
import testCategories.edoNewUX.SanityTests;
import Interfaces.TestCaseParams;

//@Category(AngularLearningArea.class)
public class PracticesTestsByTypes2 extends BasicNewUxTest {

	NewUxLearningArea2 learningArea2;
	NewUxLearningArea learningArea;
	NewUxDragAndDropSection dragAndDrop;
	NewUxDragAndDropSection2 dragAndDrop2;
	NewUXLoginPage loginPage;
	NewUxMessagesPage inboxPage;
	TmsHomePage tmsHomePage;
	TeacherReadMessagePage tRead;
	TeacherWriteMessagePage tWrite;
		
	private static final String freeWriteAlertAfterSubmit = "Your answer has been submitted to your teacher.";
	private static final String openEndedSegmentsInstruction = "Type the sentences that you hear in the dictation. Pay attention to punctuation.";
	private static final String openEndedWritingInstruction = "Rewrite the sentences using the words in parentheses.";
	//private static final String markTheTrueInstruction = "Read the ad for Village Tours, and then answer the question.";
	private static final String MarkTheTrueInstruction = "What can Village Tours do for you?";
	private static final String fillInTheBlanksSelectionInstruction = "Listen to the clips of the radio program, and complete the sentences.";
	private static final String fillInTheBlanksInstruction = "After reading the article, complete the following sentences.";
	private static final String mcqSelectionInstruction = "Listen to the clips of the ad, and answer the questions.";
	private static final String NewMcq = "Choose the correct answer.";
	private static final String MultiCorrectAns = "Choose all the correct answers.";
	//private static final String fillInTheBlanksWithImage= "Fill in the blank/s with the correct answer/s."; // Aak Maxim and Nadav
	private static final String fillInTheBlanksWithImage= "Select the correct answer from the drop-down list.";
	private static final String FillInTheBlanksTestMode = "Complete the descriptions of three students' different learning needs.";
	private static final String MultiCorrectAnsMedia = "Choose the correct answers.";
	private static final String Multi3CorrectAnsMedia = "Choose three correct answers.";
	private static final String NewMcqImage = "Choose the correct picture.";
	private static final String NewMcqImageSegmented = "Listen and choose the correct answer.";
	private static final String NewMcqMedia = "What's the best response to the question?";
	private static final String NewMcqMediaTest = "What is the best response to the question?";
	private static final String matchingDragAndDropInstruction = "Listen to the news about President Stirling and collocate the words taken from the review.";
	private static final String closeDragAndDropMediaInstruction = "Complete the conversation. Drag the correct answers into place.";
	private static final String closeDragAndDropInstruction2 = "The wife is telling a neighbor what happened to Buster. Complete the conversation by dragging the correct answers into place.";
	private static final String closeDragAndDropTestModeInstruction="Complete the text chat between three friends.";
	//private static final String classificationDragAndDropInstruction = "After you have read the story about Cindy, put the phrases about each person into the table.";
	private static final String classificationDragAndDropInstruction2 = "Remember! We steal things, but we rob people and places. Match the nouns to the correct verbs.";
	private static final String classificationDragAndDropInstruction3 = "After you have read the story about Cindy, put the phrases about each person into the table.";
	private static final String alphabetQuestionInstruction = "Match the capital letters to the small letters.";
	private static final String SequenceSentenceQuestionInstruction = "Read Bill's letter about his stay in Portugal. Then put the events into the right order.";
	private static final String SequenceSentenceQuestionInstructionTest = "Put the sentences in the correct order to make the dialogue you listened to at the beginning of the lesson.";
	private static final String SequenceImageQuestionInstruction = "Read the story \"Mystery Girl.\" Then show what happened by putting the pictures into the correct order.";
	private static final String FreeWriteQuestionInstruction = "Write your own application form and send your answer to your teacher.";
	private static final String matchTextToPicDragAndDropInstruction = "Drag the word to the correct letter.";
	private static final String matchTextToPicDragAndDropInstructionTM = "Match the words and phrases from the lecture to the correct pictures.";
	private static final String closeWholeBmpDragAndDropInstruction = "Listen and drag the correct answer/s into place.";
	private static final String openEndedSmallSegmentsInstruction = "Ray is writing to his friend Kevin about the radio interview. Complete Ray's email.";
	private static final String openEndedBank ="Complete Lisa's shopping list. Use the words in the bank to help you.";
	private static final String insertTextInstruction = "Select the place in the text where this sentence best fits.";
	private static final String insertTextAnswerSentence = "\"When you jump for half an hour, you burn about 160 calories.\"";
	private static final String editTextInstruction = "The following activity will help you get ready to write. Correct this paragraph about a way to use kinetic energy. The errors are highlighted in the paragraph on the left side of the screen. Correct the errors by typing in the boxes on the right side of the screen.";
	private static final String highlightTextInstruction = "Choose the correct answer.";
	private static final String selectTextInstruction = "Which sentence uses the imperative? Select the correct sentence in the text.";
	private static final String openWritingtInstruction = "Type your answer in the text box and click submit.";
	private static final String openWritingConfirmationAlert = "Your answer has been submitted to your teacher.";
	private static final String classificationDragAndDropTestInstruction = "Which words are activities? Which are places to do activities? Complete the table.";
	private static final String MatchingDragAndDropTestInstruction="Complete the collocations. Match the words below to the correct words in column A.";		
	List<WebElement> markers;
	List<WebElement> content;
	List<WebElement> rightContentInputsElements;
	List<WebElement> leftContentInputsElements;
	List<WebElement> highlightedText;
	List<WebElement> highlightedAnswer;
	private Object WebElement;
	
	
	
	@Before
	public void setup() throws Exception {
		super.setup();
					
		report.startStep("Get user and login to ED");
		getUserAndLoginNewUXClass();
		//webDriver.maximize();
		dragAndDrop2 = new NewUxDragAndDropSection2(webDriver, testResultService);
	//	homePage.closeAllNotifications();
	//	pageHelper.skipOnBoardingHP();
	//	homePage.waitHomePageloadedFully();
	}
	
	// Test new template Close with sound
	
	@Test
	@TestCaseParams(testCaseID = { "46064" }, skippedBrowsers = {"firefox"}) // Actions API not supported by geckodriver)
	public void CloseDragAndDrop_Media2() throws Exception {

		
	report.startStep("Init Test Data");
		String[] words = new String[] { "diamond", "nice,", "United States", "this", "meet", "How" };

	report.startStep("Navigate B1-U2-L1-P3");
		learningArea2 = homePage.navigateToTask("B1", 2, 1, 2, 3);
		sleep(3);
		
	report.startStep("Verify instruction text");
		learningArea2.VerificationOfQuestionInstruction(closeDragAndDropMediaInstruction);

	report.startStep("Drag all tiles to droptargets");
		for (int i = 0; i < words.length; i++) {
			
			if (i==3){
				dragAndDrop2.scrollCustomElement(TaskTypes.Close, 130); // after 3 tiles scroll down if needed
			}
			
			dragAndDrop2.dragAndDropAnswerByTextToTarget(words[i], i+1, TaskTypes.Close);
			sleep(1);
						
		}

		
	report.startStep("Click on Clear and check all tiles back to bank");
		learningArea2.clickOnClearAnswer();
		for (int i = 0; i < words.length; i++) {
			dragAndDrop2.checkTileIsInBank(words[i]);
		}


	report.startStep("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnClear();

	report.startStep("Click on See answers and check tiles placed");
		learningArea2.clickOnSeeAnswer();
		for (int i = 0; i < words.length; i++) {
			dragAndDrop2.checkTileIsPlaced(words[i], TaskTypes.Close);
		}

	report.startStep("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnSeeAnswer();

	report.startStep("Click on See answers and check tiles back to bank");
		learningArea2.clickOnSeeAnswer();
		for (int i = 0; i < words.length; i++) {
			dragAndDrop2.checkTileIsInBank(words[i]);
		}

	report.startStep("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnClear();


	report.startStep("Drag 1 tile to correct place and 1 to wrong place");
		dragAndDrop2.scrollCustomElement(TaskTypes.Close, -130);
		String correctAns = words[0];
		int correctTarget = 1;
		String wrongAns = words[5];
		int wrongTarget = 2;
		dragAndDrop2.dragAndDropAnswerByTextToTarget(correctAns, correctTarget, TaskTypes.Close);
		dragAndDrop2.dragAndDropAnswerByTextToTarget(wrongAns, wrongTarget, TaskTypes.Close);

	report.startStep("Click on Check Answer and check V/X signs placed correctly");
		learningArea2.clickOnCheckAnswer();
		dragAndDrop2.checkDragAndDropAnswerMark(correctAns, 1, true, TaskTypes.Close);
		dragAndDrop2.checkDragAndDropAnswerMark(wrongAns, 2, false, TaskTypes.Close);
		

	report.startStep("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnCheckAnswer();
		
	report.startStep("Click on Check Answer and check correct tile remains and wrong tile back to bank");
		learningArea2.clickOnCheckAnswer();
		dragAndDrop2.checkTileIsInBank(wrongAns);
		dragAndDrop2.checkTileIsPlaced(correctAns, TaskTypes.Close);
		learningArea2.clickOnClearAnswer();
		
	report.startStep("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnClear();

		
	report.startStep("Check you can replace current tile by the next tile by drag on it");
		dragAndDrop2.scrollCustomElement(TaskTypes.Close, 130);
		String currentTile = words[2];
		String nextTile = words[3];
		int targetId = 4;
		dragAndDrop2.dragAndDropAnswerByTextToTarget(currentTile, targetId, TaskTypes.Close);
		dragAndDrop2.checkTileIsPlaced(currentTile, TaskTypes.Close);
		sleep(1);
		dragAndDrop2.dragAndDropAnswerByTextToTarget(nextTile, targetId, TaskTypes.Close);
		dragAndDrop2.checkTileIsPlaced(nextTile, TaskTypes.Close);
		dragAndDrop2.checkTileIsInBank(currentTile);

	report.startStep("Press on Headphone btn and check media");
		dragAndDrop2.scrollCustomElement(TaskTypes.Close, -130);
		learningArea2.sound.click();
		learningArea2.checkAudioFile("AngularMediaPlayer", "b1ltbqp03.mp3");
	}
	
	
	// new test for D&D new Close Template
	@Test
	@TestCaseParams(testCaseID = { "24624","45113" }, skippedBrowsers = {"firefox"}) // Actions API not supported by geckodriver)
	public void CloseDragAndDrop2() throws Exception {
		
		report.startStep("Init Test Data");
		String[] words = new String[] {"walk", "disappeared","searched", "whistled","thieves","pedigree","realized","guilty"};

		report.startStep("Navigate A1-U2-L1-P2-T-14");
		learningArea2 = homePage.navigateToTask("A1", 2, 1, 2, 14);
		
		report.startStep("Verify instruction text");
		learningArea2.VerificationOfQuestionInstruction(closeDragAndDropInstruction2);

		report.startStep("Drag all tiles to droptargets");
		dragAndDrop2 = new NewUxDragAndDropSection2(webDriver, testResultService);
		
				
		report.startStep("Drag all tiles to droptargets");
		for (int i = 0; i < words.length; i++) {
			
			dragAndDrop2.dragAndDropAnswerByTextToTarget(words[i], i+1, TaskTypes.Close);
			sleep(1);			
		}
		
		report.startStep("Click on Clear and check all tiles back to bank");
		learningArea2.clickOnClearAnswer();
		for (int i = 0; i < words.length; i++) {
			dragAndDrop2.checkTileIsInBank(words[i]);
		}

		report.startStep("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnClear();

	report.startStep("Click on See answers and check tiles placed");
		learningArea2.clickOnSeeAnswer();
		for (int i = 0; i < words.length; i++) {
			dragAndDrop2.checkTileIsPlaced(words[i], TaskTypes.Close);
		}

	report.startStep("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnSeeAnswer();

	report.startStep("Click on See answers and check tiles back to bank");
		learningArea2.clickOnSeeAnswer();
		for (int i = 0; i < words.length; i++) {
			dragAndDrop2.checkTileIsInBank(words[i]);
		}

	report.startStep("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnClear();

	report.startStep("Drag 1 tile to correct place and 1 to wrong place");
		String correctAns = words[0];
		int correctTarget = 1;
		String wrongAns = words[1];
		int wrongTarget = 3;
		dragAndDrop2.dragAndDropAnswerByTextToTarget(correctAns, correctTarget, TaskTypes.Close);
		dragAndDrop2.dragAndDropAnswerByTextToTarget(wrongAns, wrongTarget, TaskTypes.Close);

	report.startStep("Click on Check Answer and check V/X signs placed correctly");
		learningArea2.clickOnCheckAnswer();
		dragAndDrop2.checkDragAndDropAnswerMark(correctAns, 1, true, TaskTypes.Close);
		dragAndDrop2.checkDragAndDropAnswerMark(wrongAns, 3, false, TaskTypes.Close);

	report.startStep("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnCheckAnswer();

	report.startStep("Click on Check Answer and check correct tile remains and wrong tile back to bank");
		learningArea2.clickOnCheckAnswer();
		dragAndDrop2.checkTileIsInBank(wrongAns);
		dragAndDrop2.checkTileIsPlaced(correctAns, TaskTypes.Close);
		learningArea2.clickOnClearAnswer();

		report.startStep("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnClear();

		report.startStep("Check you can replace current tile by the next tile by drag on it");
		String currentTile = words[2];
		String nextTile = words[3];
		int targetId = 4;
		dragAndDrop2.dragAndDropAnswerByTextToTarget(currentTile, targetId, TaskTypes.Close);
		dragAndDrop2.checkTileIsPlaced(currentTile, TaskTypes.Close);
		sleep(1);
		dragAndDrop2.dragAndDropAnswerByTextToTarget(nextTile, targetId, TaskTypes.Close);
		dragAndDrop2.checkTileIsPlaced(nextTile, TaskTypes.Close);
		dragAndDrop2.checkTileIsInBank(currentTile);

		
	}
	
	@Test
	@TestCaseParams(testCaseID = { "44436" }, skippedBrowsers = {"firefox"}) // Actions API not supported by geckodriver)
	public void ClassificationDragAndDrop2_2Col() throws Exception {

		report.startStep("Init Test Data");
		dragAndDrop2 = new NewUxDragAndDropSection2(webDriver, testResultService);
		String[] words = new String[] { "a house", "a necklace", "a car", "a suitcase", "some money", "a woman", "a man", "a bank", "a store", "a wallet" };
		String[] correctC1 = new String[] {"a car", "a wallet","some money","a necklace","a suitcase"};
		String[] correctC2 = new String[] {"a house","a bank","a store","a man","a woman"};
		
		int wordsTotal = words.length;
		int numOfColumns = 2;
		int tilesInColumn = wordsTotal/numOfColumns; 
		
		report.startStep("Navigate B1-U2-L6-S2-T2");
		learningArea2 = homePage.navigateToTask("B1", 2, 6, 2, 2);
				
		report.startStep("Verify instruction text");
		learningArea2.VerificationOfQuestionInstruction(classificationDragAndDropInstruction2);
		
		report.startStep("Check all tiles counters are empty by default");
		for (int i = 0; i < numOfColumns; i++) {
			checkTilesCounters(tilesInColumn, i+1, false);
		}
				
		report.startStep("Drag all tiles to droptargets");
		int column =1;
		for (int i = 0; i < wordsTotal; i++) {
			
			if (i%2==0) column = 1;
			else column = 2;
			
			dragAndDrop2.dragAndDropAnswerByIndexToTarget(column, TaskTypes.Classification, 1);
			sleep(1);
						
		}
		
		report.startStep("Check all tiles counters are active after all placed");
		for (int i = 0; i < numOfColumns; i++) {
			checkTilesCounters(tilesInColumn, i+1, true);
		}
				
		report.startStep("Click on Clear and check all tiles back to bank");
		learningArea2.clickOnClearAnswer();
		sleep(1);
		for (int i = 0; i < words.length; i++) {
			dragAndDrop2.checkTileIsInBank(words[i]);
		}
				
		report.startStep("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnClear();
		
		report.startStep("Click on See answers and check tiles placed");
		learningArea2.clickOnSeeAnswer();
		sleep(1);
		for (int i = 0; i < words.length; i++) {
			dragAndDrop2.checkTileIsPlaced(words[i], TaskTypes.Classification);
		}

		report.startStep("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnSeeAnswer();

		report.startStep("Click on See answers and check tiles back to bank");
		learningArea2.clickOnSeeAnswer();
		sleep(1);
		for (int i = 0; i < words.length; i++) {
			dragAndDrop2.checkTileIsInBank(words[i]);
		}

		report.startStep("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnClear();
		
		/*
		report.startStep("Drag 1 tile to correct place and 1 to wrong place - check tile counters");
		int visibleCorrectWord = getVisibleWord(column);  
		
		String correctAns = words[9];
		int correctCol = 1;
		String wrongAns = words[2];
		int wrongCol = 2;
		dragAndDrop2.dragAndDropAnswerByTextToTarget(correctAns, correctCol, TaskTypes.Classification);
		sleep(1);
		dragAndDrop2.checkTileCounterStateIsActive(correctCol, 1);
		dragAndDrop2.checkTileCounterStateIsEmpty(correctCol, 2);
			
		dragAndDrop2.dragAndDropAnswerByTextToTarget(wrongAns, wrongCol, TaskTypes.Classification);
		sleep(1);
		dragAndDrop2.checkTileCounterStateIsActive(wrongCol, 1);
		dragAndDrop2.checkTileCounterStateIsEmpty(wrongCol, 2);
		
		
		report.startStep("Click on Check Answer and check V/X signs placed correctly");
		learningArea2.clickOnCheckAnswer();
		dragAndDrop2.checkDragAndDropAnswerMark(correctAns, correctCol, true, TaskTypes.Classification);
		dragAndDrop2.checkDragAndDropAnswerMark(wrongAns, wrongCol, false, TaskTypes.Classification);

		report.startStep("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnCheckAnswer();

		report.startStep("Click on Check Answer and check correct tile remains and wrong tile back to bank");
		learningArea2.clickOnCheckAnswer();
		dragAndDrop2.checkTileIsInBank(wrongAns);
		dragAndDrop2.checkTileIsPlaced(correctAns, TaskTypes.Classification);
		learningArea2.clickOnClearAnswer();
*/
		report.startStep("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnClear();

		report.startStep("Check you cannot place more tiles in column then its capacity");
		int lastItem = 0;
		for (int i = 0; i < wordsTotal/numOfColumns; i++) {
			dragAndDrop2.dragAndDropAnswerByTextToTarget(words[i], column, TaskTypes.Classification);
			sleep(1);
			lastItem = i;
		}
		dragAndDrop2.dragAndDropAnswerByTextToTarget(words[lastItem+1], column, TaskTypes.Classification);
		dragAndDrop2.checkTileIsInBank(words[lastItem+1]);
		
	}
	
	@Test
	@TestCaseParams(testCaseID = { "24636" }, skippedBrowsers = {"firefox"}) // Actions API not supported by geckodriver)
	public void ClassificationDragAndDrop2_3Col() throws Exception {

		report.startStep("Init Test Data");
		dragAndDrop2 = new NewUxDragAndDropSection2(webDriver, testResultService);
		String[] words = new String[] { "had made up her mind to be in the concert",
										"had never sung in public before",
										"suggested Cindy try out for the concert",
										"had a full, deep voice",
										"hoped to get the main part",
										"was very encouraging" };
		int wordsTotal = words.length;
		int numOfColumns = 3;
		int tilesInColumn = wordsTotal/numOfColumns; 
		
		report.startStep("Navigate I2-U5-L2-S2-T2");
		learningArea2 = homePage.navigateToTask("I2", 5, 2, 2, 2);
		sleep(2);
				
		report.startStep("Verify instruction text");
		learningArea2.VerificationOfQuestionInstruction(classificationDragAndDropInstruction3);
		
		report.startStep("Check all tiles counters are empty by default");
		for (int i = 0; i < numOfColumns; i++) {
			checkTilesCounters(tilesInColumn, i+1, false);
		}
				
		report.startStep("Drag all tiles to droptargets");
		int column = 1;
		int resetNum = wordsTotal/numOfColumns;
		
		for (int i = 0; i < wordsTotal; i++) {
			if (i<resetNum) column = 1;
			else if (i<resetNum*2) column = 2;
			else if (i<resetNum*3) column = 3; 
			dragAndDrop2.dragAndDropAnswerByTextToTarget(words[i], column, TaskTypes.Classification);
			sleep(1);
						
		}
		
		report.startStep("Check all tiles counters are active after all placed");
		for (int i = 0; i < numOfColumns; i++) {
			checkTilesCounters(tilesInColumn, i+1, true);
		}
		
		report.startStep("Click on Clear and check all tiles back to bank");
		learningArea2.clickOnClearAnswer();
		sleep(1);
		for (int i = 0; i < words.length; i++) {
			dragAndDrop2.checkTileIsInBank(words[i]);
		}
				
		report.startStep("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnClear();
		
		report.startStep("Click on See answers and check tiles placed");
		learningArea2.clickOnSeeAnswer();
		sleep(1);
		for (int i = 0; i < words.length; i++) {
			dragAndDrop2.checkTileIsPlaced(words[i], TaskTypes.Classification);
		}

		report.startStep("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnSeeAnswer();

		report.startStep("Click on See answers and check tiles back to bank");
		learningArea2.clickOnSeeAnswer();
		sleep(1);
		for (int i = 0; i < words.length; i++) {
			dragAndDrop2.checkTileIsInBank(words[i]);
		}

		report.startStep("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnClear();
		
		report.startStep("Drag 1 tile to correct place and 1 to wrong place - check tile counters");
		String correctAns = words[2];
		int correctCol = 1;
		String wrongAns = words[5];
		int wrongCol = 2;
		dragAndDrop2.dragAndDropAnswerByTextToTarget(correctAns, correctCol, TaskTypes.Classification);
		sleep(1);
		dragAndDrop2.checkTileCounterStateIsActive(correctCol, 1);
		dragAndDrop2.checkTileCounterStateIsEmpty(correctCol, 2);
				
		dragAndDrop2.dragAndDropAnswerByTextToTarget(wrongAns, wrongCol, TaskTypes.Classification);
		sleep(1);
		dragAndDrop2.checkTileCounterStateIsActive(wrongCol, 1);
		dragAndDrop2.checkTileCounterStateIsEmpty(wrongCol, 2);
		
		
		report.startStep("Click on Check Answer and check V/X signs placed correctly");
		learningArea2.clickOnCheckAnswer();
		dragAndDrop2.checkDragAndDropAnswerMark(correctAns, correctCol, true, TaskTypes.Classification);
		dragAndDrop2.checkDragAndDropAnswerMark(wrongAns, wrongCol, false, TaskTypes.Classification);

		report.startStep("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnCheckAnswer();

		report.startStep("Click on Check Answer and check correct tile remains and wrong tile back to bank");
		learningArea2.clickOnCheckAnswer();
		dragAndDrop2.checkTileIsInBank(wrongAns);
		dragAndDrop2.checkTileIsPlaced(correctAns, TaskTypes.Classification);
		learningArea2.clickOnClearAnswer();

		report.startStep("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnClear();

		report.startStep("Check you cannot place more tiles in column then its capacity");
		int lastItem = 0;
		for (int i = 0; i < wordsTotal/numOfColumns; i++) {
			dragAndDrop2.dragAndDropAnswerByTextToTarget(words[i], column, TaskTypes.Classification);
			sleep(1);
			lastItem = i;
		}
		dragAndDrop2.dragAndDropAnswerByTextToTarget(words[lastItem+1], column, TaskTypes.Classification);
		dragAndDrop2.checkTileIsInBank(words[lastItem+1]);
		
	}
		
	
	//New Matching DragAndDrop Template
	
	@Test
	@TestCaseParams(testCaseID = { "45238" }, skippedBrowsers = {"firefox"}) // Actions API not supported by geckodriver)
	public void MatchingDragAndDrop2() throws Exception {
		
		report.startStep("Init Test Data");
		String[] words = new String[] { "dispute", "approach", "politicians", "era", "speech", "career" };

		report.startStep("Navigate A3-U6-L1-P2-T5");
		learningArea2 = homePage.navigateToTask("A3", 6, 1, 2, 5);
				
		report.startStep("Verify instruction text");
		learningArea2.VerificationOfQuestionInstruction(matchingDragAndDropInstruction);

		report.startStep("Drag all tiles to droptargets");
		dragAndDrop2 = new NewUxDragAndDropSection2(webDriver, testResultService);
		
		report.startStep("Drag all tiles to droptargets");
		for(int i=0;i<words.length;i++){
			dragAndDrop2.dragAndDropAnswerByTextToTarget(words[i], i+1,TaskTypes.Matching);
		}

		report.startStep("Click on Clear and check all tiles back to bank");
		learningArea2.clickOnClearAnswer();
		for (int i = 0; i < words.length; i++) {
			dragAndDrop2.checkTileIsInBank(words[i]);;
		}

		report.report("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnClear();

		report.report("Click on See answers and check tiles placed");
		learningArea2.clickOnSeeAnswer();
		for (int i = 0; i < words.length; i++) {
			dragAndDrop2.checkTileIsPlaced(words[i],TaskTypes.Matching);
		}

		report.report("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnSeeAnswer();

		report.report("Click on See answers and check tiles back to bank");
		learningArea2.clickOnSeeAnswer();
		for (int i = 0; i < words.length; i++) {
			dragAndDrop2.checkTileIsInBank(words[i]);
		}

		report.report("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnClear();

		report.startStep("Drag 1 tile to correct place and 1 to wrong place");
		String correctAns = words[0];
		int correctRow = 4;
		String wrongAns = words[1];
		int wrongRow = 1;
		dragAndDrop2.dragAndDropAnswerByTextToTarget(correctAns, correctRow, TaskTypes.Matching);
		sleep(1);
		dragAndDrop2.dragAndDropAnswerByTextToTarget(wrongAns, wrongRow, TaskTypes.Matching);
		sleep(1);

		report.startStep("Click on Check Answer and check V/X signs placed correctly");
		learningArea2.clickOnCheckAnswer();
		dragAndDrop2.checkDragAndDropAnswerMark(correctAns, correctRow, true, TaskTypes.Matching);
		dragAndDrop2.checkDragAndDropAnswerMark(wrongAns, wrongRow, false, TaskTypes.Matching);

		report.report("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnCheckAnswer();

		report.startStep("Click on Check Answer and check correct tile remains and wrong tile back to bank");
		learningArea2.clickOnCheckAnswer();
		dragAndDrop2.checkTileIsInBank(wrongAns);
		dragAndDrop2.checkTileIsPlaced(correctAns, TaskTypes.Matching);
		learningArea2.clickOnClearAnswer();

		report.report("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnClear();

		
	}
	
	@Test
	@TestCaseParams(testCaseID = { "24578" })
	public void MCQSelection_Media() throws Exception {

		
	report.startStep("Navigate A1-U5-L1-P1");
		learningArea2 = homePage.navigateToTask("A1", 5, 1, 2, 1);
		String [] answers = new String [] {"question-1_answer-1","question-2_answer-4","question-2_answer-5","question-1_answer-3"};
				
	report.startStep("Verify instruction text");
		learningArea2.VerificationOfQuestionInstruction(mcqSelectionInstruction);

	report.startStep("Select all answers");
		learningArea2.SelectRadioBtn(answers[0]);
		sleep(1);
		dragAndDrop2.scrollCustomElement(TaskTypes.MCQ,40);
		learningArea2.SelectRadioBtn(answers[1]);

	report.startStep("Click on Clear and check all answers unselected");
		learningArea2.clickOnClearAnswer();
		sleep(1);
		learningArea2.checkAllAnswersUnselectedMCQ();

	report.report("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnClear();

	report.report("Click on See answers and check answers selected");
		learningArea2.clickOnSeeAnswer();
		sleep(1);
		learningArea2.SelectRadioBtn(answers[0]);
		learningArea2.SelectRadioBtn(answers[1]);

	report.report("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnSeeAnswer();

	report.report("Click on See answers check all answers unselected");
		learningArea2.clickOnSeeAnswer();
		sleep(1);
		learningArea2.checkAllAnswersUnselectedMCQ();

	report.report("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnClear();

	report.startStep("Select 1 correct 1 wrong answer");
		learningArea2.SelectRadioBtn(answers[0]);
		sleep(1);
		learningArea2.SelectRadioBtn(answers[2]);
		sleep(1);

	report.startStep("Select 1 correct click on Check Answer and check V/X signs placed correctly");
	    learningArea2.SelectRadioBtn(answers[0]);
	    sleep(2);
		learningArea2.clickOnCheckAnswer();
		sleep(2);
		learningArea2.CorrectnessVMCQ();
		learningArea2.clickOnClearAnswer();
		
	report.startStep("Select 1 wromg click on Check Answer and check V/X signs placed correctly");
	    learningArea2.SelectRadioBtn(answers[3]);
		learningArea2.clickOnCheckAnswer();
		learningArea2.CorrectnessXMCQ();
		sleep(1);
		learningArea2.clickOnClearAnswer();
		
	report.report("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnClear();

	report.startStep("Press on Headphone btn and check media");
		dragAndDrop2.scrollCustomElement(TaskTypes.MCQ,-40);
		learningArea2.sound.click();
		learningArea2.checkAudioFile("syncAudioPlayer", "a1lrade.mp3");
		
	}
	
	
	@Test
	@TestCaseParams(testCaseID = { "47809" })
	public void NewMCQ() throws Exception {

		String correctAnswer = "question-1_answer-1";
		String wrongAnswer = "question-1_answer-3";
		
	report.startStep("Navigate B3-U9-L2-S2-T2");
		learningArea2 = homePage.navigateToTask("B3", 9, 2, 2, 2);
				
	report.startStep("Verify instruction text");
		learningArea2.VerificationOfQuestionInstruction(NewMcq);

	report.startStep("Select answers. Click on Clear and check  answers selected");
	    learningArea2.SelectRadioBtn(correctAnswer);
	    sleep(2);
		learningArea2.clickOnClearAnswer();
		learningArea2.clickOnSeeAnswer();
		sleep(1);
		learningArea2.clickOnSeeAnswer();
		
	report.startStep("Select 1 correct click on Check Answer and check V/X signs placed correctly");
	    learningArea2.SelectRadioBtn(correctAnswer);
	    sleep(2);
		learningArea2.clickOnCheckAnswer();
		sleep(2);
		learningArea2.CorrectnessVMCQ();
		learningArea2.clickOnClearAnswer();
		
	report.startStep("Select 1 wromg click on Check Answer and check V/X signs placed correctly");
	    learningArea2.SelectRadioBtn(wrongAnswer);
		learningArea2.clickOnCheckAnswer();
		learningArea2.CorrectnessXMCQ();
		sleep(1);

	report.report("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnCheckAnswer();
		learningArea2.clickOnCheckAnswer();
		
	report.startStep("Check next selection unselect current");
	   learningArea2.SelectRadioBtn(correctAnswer);
	   sleep(1);
	   learningArea2.SelectRadioBtn(wrongAnswer);
	}
	
	@Test
	@Category(SanityTests.class)
	@TestCaseParams(testCaseID = { "48501" })
	public void NewMultipleCorrectAnswer() throws Exception {

		String correctAnswer = "question-1_answer-1";
		String wrongAnswer = "question-1_answer-3";
		
	report.startStep("Navigate I1-U10-L5-S3-T2");
		learningArea2 = homePage.navigateToTask("I1", 10, 5, 3, 2);
				
	report.startStep("Verify instruction text");
		learningArea2.VerificationOfQuestionInstruction(MultiCorrectAns);

	report.startStep("Select answers. Click on Clear and check  answers selected");
	    learningArea2.SelectRadioBtn(correctAnswer);
	    sleep(2);
		learningArea2.clickOnClearAnswer();
		learningArea2.clickOnSeeAnswer();
		sleep(1);
		learningArea2.clickOnSeeAnswer();
		
	report.startStep("Select 1 correct click on Check Answer and check V/X signs placed correctly");
	    learningArea2.SelectRadioBtn(correctAnswer);
	    sleep(2);
		learningArea2.clickOnCheckAnswer();
		sleep(2);
		learningArea2.CorrectnessVMCQ();
		learningArea2.clickOnClearAnswer();
		
	report.startStep("Select 1 wromg click on Check Answer and check V/X signs placed correctly");
	    learningArea2.SelectRadioBtn(wrongAnswer);
		learningArea2.clickOnCheckAnswer();
		learningArea2.CorrectnessXMCQ();
		sleep(1);

	report.report("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnCheckAnswer();
		learningArea2.clickOnCheckAnswer();
		
	report.startStep("Check next selection unselect current");
	   learningArea2.SelectRadioBtn(correctAnswer);
	   sleep(1);
	   learningArea2.SelectRadioBtn(wrongAnswer);
	   
		homePage.clickOnHomeButton();
		homePage.waitHomePageloadedFully();
		sleep(1);
		String actualCompletion = homePage.getCourseCompletionDisplayedOnHomePage();
		assertEquals("1", actualCompletion);
	}
	
	// MultipleCorrectAnswerMedia 11.07.18
	
	@Test
	@TestCaseParams(testCaseID = { "48501" })
	public void NewMultipleCorrectAnswerMedia() throws Exception {

		String correctAnswer = "question-1_answer-1";
		String wrongAnswer = "question-1_answer-4";
		
	report.startStep("Navigate FD-U3-L1-S3-T4");
		learningArea2 = homePage.navigateToTask("FD", 3, 1, 3, 4);
				
	report.startStep("Verify instruction text");
		learningArea2.VerificationOfQuestionInstruction(MultiCorrectAnsMedia);

	report.startStep("Select answers. Click on Clear and check  answers selected");
	    learningArea2.SelectRadioBtn(correctAnswer);
	    sleep(2);
		learningArea2.clickOnClearAnswer();
		learningArea2.clickOnSeeAnswer();
		sleep(1);
		learningArea2.clickOnSeeAnswer();
		
		report.startStep("Select 1 correct click on Check Answer and check V/X signs placed correctly");
	    learningArea2.SelectRadioBtn(correctAnswer);
	    sleep(2);
		learningArea2.clickOnCheckAnswer();
		sleep(2);
		learningArea2.CorrectnessVMCQ();
		learningArea2.clickOnClearAnswer();
		
	report.startStep("Select 1 wromg click on Check Answer and check V/X signs placed correctly");
	    learningArea2.SelectRadioBtn(wrongAnswer);
		learningArea2.clickOnCheckAnswer();
		learningArea2.CorrectnessXMCQ();
		sleep(1);

	report.report("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnCheckAnswer();
		learningArea2.clickOnCheckAnswer();
		
		report.startStep("Check next selection unselect current");
		   learningArea2.SelectRadioBtn(correctAnswer);
		   sleep(1);
		   learningArea2.SelectRadioBtn(wrongAnswer);
		   learningArea2.sound.click();
		   learningArea2.checkAudioFile("AngularMediaPlayer", "s1ltabp04.mp3");
		
		
	}
	
	@Test
	@TestCaseParams(testCaseID = { "48501" })
	public void NewMultipleCorrectAnswer_TestMode() throws Exception {
			
		int taskNum = 6;
		String expectedScore = "14%";
		String correctAnswer = "question-1_answer-1";
		
	report.startStep("Navigate I1-U10-L1-T6");
		learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "I1", 10, 1);
		sleep(1);
		NavigateToTestTask(5, taskNum);
		
	report.startStep("Verify instruction text");
		learningArea2.VerificationOfQuestionInstruction(MultiCorrectAns);
		
	report.startStep("Select answers, navigate to other task and back and check correct answer is selected");
	    learningArea2.checkAllAnswersUnselectedMCQ();
	    learningArea2.SelectRadioBtn(correctAnswer);	
		sleep (1);
		learningArea2.clickOnBackButton();
		learningArea2.clickOnNextButton();
		
		
	report.startStep("Submit test and check score");
		learningArea2.submitTest(true);
		String score = learningArea2.getTestScore();
		testResultService.assertEquals(expectedScore, score, "Checking Test Score validity");
		
	report.startStep("Press on Review and open task bar");
		learningArea2.clickOnReviewTestResults();
		learningArea2.openTaskBar();
		
	report.startStep("Check MCQ task indication is correct");
		learningArea2.checkTestTaskMarkIsPartlyCorrect(taskNum);
		learningArea2.clickOnTaskByNumber(taskNum);
			
	report.startStep("Check Your Answer Tab");
		learningArea2.clickOnYourAnswerTab();
		learningArea2.CorrectnessVMCQ();

	report.startStep("Check Correct Answer Tab");
		learningArea2.clickOnCorrectAnswerTab();
		
	}
	
	
	@Test
	@TestCaseParams(testCaseID = { "48279" })
	public void NewMCQImage() throws Exception {
        
		String correctAnswer = "question-1_answer-1";
		String wrongAnswer = "question-1_answer-3";
		
	report.startStep("Navigate B2-U10-L4-S3-T2");
		learningArea2 = homePage.navigateToTask("B2", 10, 4, 3, 2);
				
	report.startStep("Verify instruction text");
		learningArea2.VerificationOfQuestionInstruction(NewMcqImage);

	report.startStep("Select answers. Click on Clear and check  answers selected");
	    learningArea2.SelectRadioBtn(correctAnswer);
	    sleep(1);
		learningArea2.clickOnClearAnswer();
		learningArea2.clickOnSeeAnswer();
		sleep(1);
		learningArea2.clickOnSeeAnswer();
		
	report.startStep("Select 1 correct click on Check Answer and check V/X signs placed correctly");
	    learningArea2.SelectRadioBtn(correctAnswer);
	    sleep(1);
		learningArea2.clickOnCheckAnswer();
		sleep(1);
		learningArea2.CorrectnessVMCQ();
		learningArea2.clickOnClearAnswer();
		
	report.startStep("Select 1 wromg click on Check Answer and check V/X signs placed correctly");
	    learningArea2.SelectRadioBtn(wrongAnswer);
		learningArea2.clickOnCheckAnswer();
		learningArea2.CorrectnessXMCQ();
		sleep(1);

	report.report("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnCheckAnswer();
		learningArea2.clickOnCheckAnswer();
		
	report.startStep("Check next selection unselect current");
	   learningArea2.SelectRadioBtn(correctAnswer);
	   sleep(1);
	   learningArea2.SelectRadioBtn(wrongAnswer);
	}
	
	
	@Test
	@TestCaseParams(testCaseID = { "48501" })
	public void NewMCQImageSegmented_TestMode() throws Exception {
			
		int taskNum = 5;
		String expectedScore = "14%";
		String correctAnswer = "question-1_answer-2";
		String correctAnswer2 = "question-1_answer-1";
		String incorrectAnswer1 = "question-1_answer-6";
		
	report.startStep("Navigate B1-U9-L5-T5");
		learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "B1", 9, 5);
		learningArea2.waitForPageToLoad();
		NavigateToTestTask(5, taskNum);
		
	report.startStep("Verify instruction text");
		learningArea2.VerificationOfQuestionInstruction(Multi3CorrectAnsMedia);
		
	report.startStep("Select answers, navigate to other task and back and check correct answer is selected");
	    learningArea2.checkAllAnswersUnselectedMCQ();
	    learningArea2.SelectRadioBtn(correctAnswer);	
		sleep(1);
		learningArea2.SelectRadioBtn(correctAnswer2);
		learningArea2.SelectRadioBtn(incorrectAnswer1);
		learningArea2.clickOnBackButton();
		learningArea2.clickOnNextButton();
		
		
	report.startStep("Submit test and check score");
		learningArea2.submitTest(true);
		String score = learningArea2.getTestScore();
		testResultService.assertEquals(expectedScore, score, "Checking Test Score validity");
		
	report.startStep("Press on Review and open task bar");
		learningArea2.clickOnReviewTestResults();
		learningArea2.openTaskBar();
		
	report.startStep("Check MCQ task indication is correct");
		learningArea2.checkTestTaskMarkIsPartlyCorrect(taskNum);
		learningArea2.clickOnTaskByNumber(taskNum);
		
			
	report.startStep("Check Your Answer Tab");
		learningArea2.clickOnYourAnswerTab();
		learningArea2.CorrectnessVMCQ();

	report.startStep("Check Correct Answer Tab");
		learningArea2.clickOnCorrectAnswerTab();
		
	}
	
	@Test
	@TestCaseParams(testCaseID = { "48724" })
	public void NewMCQImageSegemented() throws Exception {

		String correctAnswer = "question-1_answer-2";
		String wrongAnswer = "question-1_answer-1";
		
	report.startStep("Navigate FD-U1-L5-S3-T3");
		learningArea2 = homePage.navigateToTask("FD", 1, 5, 3, 3);
				
	report.startStep("Verify instruction text");
		learningArea2.VerificationOfQuestionInstruction(NewMcqImageSegmented);

	report.startStep("Select answers. Click on Clear and check  answers selected");
	    learningArea2.SelectRadioBtn(correctAnswer);
	    sleep(2);
		learningArea2.clickOnClearAnswer();
		learningArea2.clickOnSeeAnswer();
		sleep(1);
		learningArea2.clickOnSeeAnswer();
		
	report.startStep("Select 1 correct click on Check Answer and check V/X signs placed correctly");
	    learningArea2.SelectRadioBtn(correctAnswer);
	    sleep(2);
		learningArea2.clickOnCheckAnswer();
		sleep(2);
		learningArea2.CorrectnessVMCQ();
		learningArea2.clickOnClearAnswer();
		
	report.startStep("Select 1 wromg click on Check Answer and check V/X signs placed correctly");
	    learningArea2.SelectRadioBtn(wrongAnswer);
		learningArea2.clickOnCheckAnswer();
		learningArea2.CorrectnessXMCQ();
		sleep(1);

	report.report("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnCheckAnswer();
		learningArea2.clickOnCheckAnswer();
		
	report.startStep("Check next selection unselect current");
	   learningArea2.SelectRadioBtn(correctAnswer);
	   sleep(1);
	   learningArea2.SelectRadioBtn(wrongAnswer);
	   learningArea2.sound.click();
	   learningArea2.checkAudioFile("AngularMediaPlayer", "s1veaap03.mp3");
	}
	
	
	
	@Test
	@TestCaseParams(testCaseID = { "48464" })
	public void NewMCQMedia() throws Exception {

		String correctAnswer = "question-1_answer-1";
		String wrongAnswer = "question-1_answer-3";
		
	report.startStep("Navigate I1-U10-L4-S5-T4");
		learningArea2 = homePage.navigateToTask("I1", 10, 4, 5, 4);
				
	report.startStep("Verify instruction text");
		learningArea2.VerificationOfQuestionInstruction(NewMcqMedia);

	report.startStep("Select answers. Click on Clear and check  answers selected");
	    learningArea2.SelectRadioBtn(correctAnswer);
	    sleep(2);
		learningArea2.clickOnClearAnswer();
		learningArea2.clickOnSeeAnswer();
		sleep(1);
		learningArea2.clickOnSeeAnswer();
		
	report.startStep("Select 1 correct click on Check Answer and check V/X signs placed correctly");
	    learningArea2.SelectRadioBtn(correctAnswer);
	    sleep(2);
		learningArea2.clickOnCheckAnswer();
		sleep(2);
		learningArea2.CorrectnessVMCQ();
		learningArea2.clickOnClearAnswer();
		
	report.startStep("Select 1 wromg click on Check Answer and check V/X signs placed correctly");
	    learningArea2.SelectRadioBtn(wrongAnswer);
		learningArea2.clickOnCheckAnswer();
		learningArea2.CorrectnessXMCQ();
		sleep(1);

	report.report("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnCheckAnswer();
		learningArea2.clickOnCheckAnswer();

	report.startStep("Check next selection unselect current");
	   learningArea2.SelectRadioBtn(correctAnswer);
	   sleep(1);
	   learningArea2.SelectRadioBtn(wrongAnswer);
	   learningArea2.sound.click();
	   learningArea2.checkAudioFile("AngularMediaPlayer", "i1soabp504.mp3");
	
	}
	
	@Test
	@TestCaseParams(testCaseID = { "48415", "48416" })
	public void NewMCQMedia_TestMode2Negative() throws Exception {
			
		int taskNum = 5;
		String expectedScore = "0%";
		String wrongAnswer = "question-1_answer-2";
		
	report.startStep("Navigate I1-U9-L4-T5");
		learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "I1", 9, 4);
		sleep(1);
		NavigateToTestTask(7, taskNum);
		
	report.startStep("Verify instruction text");
		learningArea2.VerificationOfQuestionInstruction(NewMcqMediaTest);
		
	report.startStep("Select answers, navigate to other task and back and check correct answer is selected");
	    learningArea2.checkAllAnswersUnselectedMCQ();
	    learningArea2.SelectRadioBtn(wrongAnswer);
	    sleep(1);
	    learningArea2.sound.click();
	    learningArea2.checkAudioFile("AngularMediaPlayer", "i1soaat105.mp3");
		sleep (1);
		learningArea2.clickOnNextButton();
		learningArea2.clickOnBackButton();
		
	report.startStep("Submit test and check score");
		learningArea2.submitTest(true);
		sleep(2);
		String score = learningArea2.getTestScore();
		testResultService.assertEquals(expectedScore, score, "Checking Test Score validity");
		
	report.startStep("Press on Review and open task bar");
		learningArea2.clickOnReviewTestResults();
		learningArea2.openTaskBar();
		
	report.startStep("Check MCQ task indication is correct");
		learningArea2.checkTestTaskMark(taskNum, false);
		learningArea2.clickOnTaskByNumber(taskNum);
	
		
	report.startStep("Check Your Answer Tab");
		learningArea2.clickOnYourAnswerTab();
		learningArea2.CorrectnessXMCQ();

	report.startStep("Check Correct Answer Tab");
		learningArea2.clickOnCorrectAnswerTab();
		learningArea2.CorrectnessVMCQ();
	}
	
	
	
	@Test
	@TestCaseParams(testCaseID = { "48415", "48416" })
	public void NewMCQMedia_TestMode2Positive() throws Exception {
			
		int taskNum = 5;
		String expectedScore = "17%";
		String correctAnswer = "question-1_answer-1";
		
	report.startStep("Navigate I1-U9-L4-T5");
		learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "I1", 9, 4);
		sleep(5);
		NavigateToTestTask(7, taskNum);
		
	report.startStep("Verify instruction text");
		learningArea2.VerificationOfQuestionInstruction(NewMcqMediaTest);
		
	report.startStep("Select answers, navigate to other task and back and check correct answer is selected");
	    learningArea2.checkAllAnswersUnselectedMCQ();
	    learningArea2.SelectRadioBtn(correctAnswer);	
		sleep(1);
		learningArea2.sound.click();
		learningArea2.checkAudioFile("AngularMediaPlayer", "i1soaat105.mp3");
		learningArea2.clickOnNextButton();
		learningArea2.clickOnBackButton();
		
	report.startStep("Submit test and check score");
		learningArea2.submitTest(true);
		String score = learningArea2.getTestScore();
		sleep(2);
		testResultService.assertEquals(expectedScore, score, "Checking Test Score validity");
		
	report.startStep("Press on Review and open task bar");
		learningArea2.clickOnReviewTestResults();
		learningArea2.openTaskBar();
		
	report.startStep("Check MCQ task indication is correct");
		learningArea2.checkTestTaskMark(taskNum, true);
		learningArea2.clickOnTaskByNumber(taskNum);
			
	report.startStep("Check Your Answer Tab");
		learningArea2.clickOnYourAnswerTab();
		learningArea2.CorrectnessVMCQ();

	report.startStep("Check Correct Answer Tab");
		learningArea2.clickOnCorrectAnswerTab();
		
	}
	
	@Test
	@TestCaseParams(testCaseID = { "48028" })
	public void NewMCQ_TestMode2Positive() throws Exception {
			
		int taskNum = 1;
		String expectedScore = "20%";
		String correctAnswer = "question-1_answer-1";
		
	report.startStep("Navigate B3-U9-L2-T3");
		learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "B3", 9, 2);
		sleep(1);
		learningArea2.clickOnStep(6, false);
		learningArea2.clickOnStartTest();
		
	report.startStep("Verify instruction text");
		learningArea2.VerificationOfQuestionInstruction(NewMcq);
		
	report.startStep("Select answers, navigate to other task and back and check correct answer is selected");
	    learningArea2.checkAllAnswersUnselectedMCQ();
	    learningArea2.SelectRadioBtn(correctAnswer);	
		sleep (1);
		learningArea2.clickOnNextButton();
		learningArea2.clickOnBackButton();
		
	report.startStep("Submit test and check score");
		learningArea2.submitTest(true);
		sleep(1);
		String score = learningArea2.getTestScore();
		testResultService.assertEquals(expectedScore, score, "Checking Test Score validity");
		
	report.startStep("Press on Review and open task bar");
		learningArea2.clickOnReviewTestResults();
		learningArea2.openTaskBar();
		
	report.startStep("Check MCQ task indication is correct");
		learningArea2.checkTestTaskMark(taskNum, true);
		taskNum=2;
		learningArea2.clickOnTaskByNumber(taskNum);
		learningArea2.clickOnBackButton();
		
	report.startStep("Check Your Answer Tab");
		learningArea2.clickOnYourAnswerTab();
		learningArea2.CorrectnessVMCQ();

	report.startStep("Check Correct Answer Tab");
		learningArea2.clickOnCorrectAnswerTab();
		
	}
	@Test
	@TestCaseParams(testCaseID = { "48028" })
	public void NewMCQ_TestMode2Negative() throws Exception {
			
		int taskNum = 1;
		String expectedScore = "0%";
		String wrongAnswer = "question-1_answer-2";
		
	report.startStep("Navigate B3-U9-L2-T3");
		learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "B3", 9, 2);
		sleep(1);
		learningArea2.clickOnStep(6, false);
		learningArea2.clickOnStartTest();
		
	report.startStep("Verify instruction text");
		learningArea2.VerificationOfQuestionInstruction(NewMcq);
		
	report.startStep("Select answers, navigate to other task and back and check correct answer is selected");
	    learningArea2.checkAllAnswersUnselectedMCQ();
	    learningArea2.SelectRadioBtn(wrongAnswer);
		sleep (1);
		learningArea2.clickOnNextButton();
		learningArea2.clickOnBackButton();
		
	report.startStep("Submit test and check score");
		learningArea2.submitTest(true);
		sleep(1);
		String score = learningArea2.getTestScore();
		testResultService.assertEquals(expectedScore, score, "Checking Test Score validity");
		
	report.startStep("Press on Review and open task bar");
		learningArea2.clickOnReviewTestResults();
		learningArea2.openTaskBar();
		
	report.startStep("Check MCQ task indication is correct");
		learningArea2.checkTestTaskMark(taskNum, false);
		taskNum=2;
		learningArea2.clickOnTaskByNumber(taskNum);
		learningArea2.clickOnBackButton();
  sleep(2);
		
	report.startStep("Check Your Answer Tab");
		learningArea2.clickOnYourAnswerTab();
		learningArea2.CorrectnessXMCQ();

	report.startStep("Check Correct Answer Tab");
		learningArea2.clickOnCorrectAnswerTab();
		learningArea2.CorrectnessVMCQ();
		 

	}
	
	//Fill in the blank with image
	@Test
	@TestCaseParams(testCaseID = { "24626" })
	public void FillInTheBlanksWithImage() throws Exception {
		
	report.startStep("Init Test Data");
		
		String correct1_1 = "this"; // correct
		String correct1_2 = "that"; // correct
		String wrong1_2 = "these";
		String correctplace = "1_1";
		String wrongPlace = "1_2";

	report.startStep("Navigate B1-U2-L5-S2-T7");
		learningArea2 = homePage.navigateToTask("B1", 2, 5, 2, 7);
				
	report.startStep("Verify instruction text");
		learningArea2.VerificationOfQuestionInstruction(fillInTheBlanksWithImage);

	report.startStep("Select all answers");
		learningArea2.selectAnswerFromDropDown(correctplace, correct1_1);
		learningArea2.selectAnswerFromDropDown(wrongPlace, wrong1_2);

	report.startStep("Click on Clear and check all answers unselected");
		learningArea2.clickOnClearAnswer();
		learningArea2.checkAnswersUnselectedFillTheBlanks(correctplace, correct1_1);
		learningArea2.checkAnswersUnselectedFillTheBlanks(wrongPlace, wrong1_2);

	report.report("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnClear();

	report.report("Click on See answers and check answers selected");
		learningArea2.clickOnSeeAnswer();
		learningArea2.checkAnswersSelectedFillTheBlanks(correctplace, correct1_1);
		learningArea2.checkAnswersSelectedFillTheBlanks(wrongPlace, correct1_2);

	report.report("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnSeeAnswer();

	report.report("Click on See answers check all answers unselected");
		learningArea2.clickOnSeeAnswer();
		learningArea2.checkAnswersUnselectedFillTheBlanks(correctplace, correct1_1);
		learningArea2.checkAnswersUnselectedFillTheBlanks(wrongPlace, correct1_2);

	report.report("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnClear();

	report.startStep("Select 1 correct 1 wrong answer");
		learningArea2.selectAnswerFromDropDown(correctplace, correct1_1);
		learningArea2.selectAnswerFromDropDown(wrongPlace, wrong1_2);

	report.startStep("Click on Check Answer and check V/X signs placed correctly");
		learningArea2.clickOnCheckAnswer();
		learningArea2.verifyAnswerFillTheBlankSelectedCorrect(correctplace);
		learningArea2.verifyAnswerFillTheBlankSelectedWrong(wrongPlace);

	report.report("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnCheckAnswer();

	report.startStep("Click on Check Answer and check correct remains and wrong unselected");
		learningArea2.clickOnCheckAnswer();
		learningArea2.checkAnswersSelectedFillTheBlanks(correctplace, correct1_1);
		learningArea2.checkAnswersUnselectedFillTheBlanks(wrongPlace, wrong1_2);
		learningArea2.clickOnClearAnswer();

	report.report("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnClear();
			
	}
	
	
	@Test
	@TestCaseParams (testCaseID= {"51990"})
	public void FillInTheBlanksTestMode () throws Exception {
		
	report.startStep("Init Test Data");
		int taskNum = 4;
		String expectedScore = "5%";
		String [] seeAnswer = {"DDLOptions__selected_aid_79","DDLOptions__selected_aid_58"}; // 2 correct answers
		String [] answers = {"DDLOptions__listItem_aid_69","DDLOptions__listItem_aid_20","DDLOptions__listItem_aid_92"}; //last is wrong answer
		String [] places = {"prFITB__DDLOptionsW_Q1_L1","prFITB__DDLOptionsW_Q1_L2", "prFITB__DDLOptionsW_Q1_L3"}; // 2 drop down lists
		
	report.startStep("Navigate A3-U9-L1-S6-T4");
		learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "A3", 9, 1);
		sleep(1);
		NavigateToTestTask(6, taskNum);
		
	report.startStep("Verify instruction text");
		learningArea2.VerificationOfQuestionInstruction(FillInTheBlanksTestMode);
		
	report.startStep("Select answers, navigate to other task and back and check correct answer is selected");
	    dragAndDrop2.scrollCustomElement(TaskTypes.FillInTheBlank, 30);
		learningArea2.checkAllAnswersUnselectedMCQ();
	    for (int i=0; i < places.length;i++ ){
	    learningArea2.selectAnswerFromDropDown2(places[i],answers[i]);	
	    }
		sleep (1);
		
	report.startStep("Submit test and check score");
		learningArea2.submitTest(true);
		sleep(1);
		String score = learningArea2.getTestScore();
		testResultService.assertEquals(expectedScore, score, "Checking Test Score validity");
		
	report.startStep("Press on Review and open task bar");
		learningArea2.clickOnReviewTestResults();
		learningArea2.openTaskBar();
		
	report.startStep("Check matching task indication - partly correct");
		learningArea2.checkTestTaskMarkIsPartlyCorrect(taskNum);
		
	report.startStep("Press on Task 4");
		learningArea2.clickOnTaskByNumber(taskNum);
		sleep(1);
	report.startStep("Check Your Answer Tab");
		learningArea2.clickOnYourAnswerTab();
		sleep(1);
		dragAndDrop2.scrollCustomElement(TaskTypes.FillInTheBlank, 30);
		learningArea2.verifyAnswerFillTheBlankSelectedCorrect2(places[0]);
		learningArea2.verifyAnswerFillTheBlankSelectedWrong2(places[1]);
		learningArea2.verifyAnswerFillTheBlankSelectedWrong2(places[2]);
		
	report.startStep("Check Correct Answer Tab");
		learningArea2.clickOnCorrectAnswerTab();
		learningArea2.checkAnswersUnselectedFillTheBlanks(places[0],seeAnswer[0]);
		learningArea2.checkAnswersUnselectedFillTheBlanks(places[1],seeAnswer[1]);
	}
	
	
	@Test
	@TestCaseParams(testCaseID = { "51187","51189","51191","51980" })
	public void FillInTheBlanksSelection_Media2() throws Exception {
		
	report.startStep("Init Test Data");
		String [] seeAnswer = {"DDLOptions__selected_aid_79","DDLOptions__selected_aid_58"}; // 2 correct answers
		String [] answers = {"DDLOptions__listItem_aid_79","DDLOptions__listItem_aid_58","DDLOptions__listItem_aid_92"}; //last is wrong answer
		String [] places = {"prFITB__DDLOptionsW_Q1_L1","prFITB__DDLOptionsW_Q2_L1"}; // 2 drop down lists
		
	report.startStep("Navigate A3-U6-L1-P3");
		learningArea2 = homePage.navigateToTask("A3", 6, 1, 2, 3);
				
	report.startStep("Verify instruction text");
		learningArea2.VerificationOfQuestionInstruction(fillInTheBlanksSelectionInstruction);

	report.startStep("Select all answers");
		learningArea2.selectAnswerFromDropDown2(places[0],answers[0]);
		learningArea2.selectAnswerFromDropDown2(places[1],answers[2]);

	report.startStep("Click on Clear and check all answers unselected");
		learningArea2.clickOnClearAnswer();
		learningArea2.checkAnswersUnselectedFillTheBlanks(places[0],answers[0]);
		learningArea2.checkAnswersUnselectedFillTheBlanks(places[1],answers[2]);

	report.report("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnClear();

	report.report("Click on See answers and check answers selected");
		learningArea2.clickOnSeeAnswer();
		learningArea2.checkAnswersSelectedFillTheBlanks2 (seeAnswer[0]);
		learningArea2.checkAnswersSelectedFillTheBlanks2(seeAnswer[1]);

	report.report("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnSeeAnswer();

	report.report("Click on See answers check all answers unselected");
		learningArea2.clickOnSeeAnswer();
		learningArea2.checkAnswersUnselectedFillTheBlanks(places[0],answers[0]);
		learningArea2.checkAnswersUnselectedFillTheBlanks(places[1],answers[1]);

	report.report("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnClear();

	report.startStep("Select 1 correct 1 wrong answer");
		learningArea2.selectAnswerFromDropDown2(places[0],answers[0]);
		learningArea2.selectAnswerFromDropDown2(places[1],answers[2]);

	report.startStep("Click on Check Answer and check V/X signs placed correctly");
		learningArea2.clickOnCheckAnswer();
		learningArea2.verifyAnswerFillTheBlankSelectedCorrect2(places[0]);
		learningArea2.verifyAnswerFillTheBlankSelectedWrong2(places[1]);

	report.report("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnCheckAnswer();

	report.startStep("Click on Check Answer and check correct remains and wrong unselected");
		learningArea2.clickOnCheckAnswer();
		learningArea2.checkAnswersSelectedFillTheBlanks2 (seeAnswer[0]);
		learningArea2.checkAnswersUnselectedFillTheBlanks(places[1],answers[2]);
		learningArea2.clickOnClearAnswer();

	report.report("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnClear();
		
	report.startStep("Press on Headphone btn and check media");
		learningArea2.sound.click();
		sleep(1);
		learningArea2.checkAudioFile("syncAudioPlayer", "a3lrnwe.mp3");
	}
	
	
	@Test
	@TestCaseParams(testCaseID = { "51187","51189","51191"})
	@Category(edProduction.class)
	public void FillInTheBlanks2() throws Exception {
		
		report.startStep("Init Test Data");
		String [] seeAnswer = {"DDLOptions__selected_aid_91","DDLOptions__selected_aid_69"}; // 2 correct answers
		String [] answers = {"DDLOptions__listItem_aid_91","DDLOptions__listItem_aid_69","DDLOptions__listItem_aid_80"}; //last is wrong answer
		String [] places = {"prFITB__DDLOptionsW_Q1_L1","prFITB__DDLOptionsW_Q1_L2"}; // 2 drop down lists
		
	report.startStep("Navigate A3-U6-L1-P3");
		learningArea2 = homePage.navigateToTask("A3", 6, 2, 2, 3);
				
	report.startStep("Verify instruction text");
		learningArea2.VerificationOfQuestionInstruction(fillInTheBlanksInstruction);

	report.startStep("Select all answers");
		learningArea2.selectAnswerFromDropDown2(places[0],answers[0]);
		learningArea2.selectAnswerFromDropDown2(places[1],answers[2]);

	report.startStep("Click on Clear and check all answers unselected");
		learningArea2.clickOnClearAnswer();
		learningArea2.checkAnswersUnselectedFillTheBlanks(places[0],answers[0]);
		learningArea2.checkAnswersUnselectedFillTheBlanks(places[1],answers[2]);

	report.report("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnClear();

	report.report("Click on See answers and check answers selected");
		learningArea2.clickOnSeeAnswer();
		learningArea2.checkAnswersSelectedFillTheBlanks2 (seeAnswer[0]);
		learningArea2.checkAnswersSelectedFillTheBlanks2(seeAnswer[1]);

	report.report("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnSeeAnswer();

	report.report("Click on See answers check all answers unselected");
		learningArea2.clickOnSeeAnswer();
		learningArea2.checkAnswersUnselectedFillTheBlanks(places[0],answers[0]);
		learningArea2.checkAnswersUnselectedFillTheBlanks(places[1],answers[1]);

	report.report("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnClear();

	report.startStep("Select 1 correct 1 wrong answer");
		learningArea2.selectAnswerFromDropDown2(places[0],answers[0]);
		learningArea2.selectAnswerFromDropDown2(places[1],answers[2]);

	report.startStep("Click on Check Answer and check V/X signs placed correctly");
		learningArea2.clickOnCheckAnswer();
		learningArea2.verifyAnswerFillTheBlankSelectedCorrect2(places[0]);
		learningArea2.verifyAnswerFillTheBlankSelectedWrong2(places[1]);

	report.report("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnCheckAnswer();

	report.startStep("Click on Check Answer and check correct remains and wrong unselected");
		learningArea2.clickOnCheckAnswer();
		learningArea2.checkAnswersSelectedFillTheBlanks2 (seeAnswer[0]);
		learningArea2.checkAnswersUnselectedFillTheBlanks(places[1],answers[2]);
		learningArea2.clickOnClearAnswer();

	report.report("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnClear();
		
	}
	
	@Test
	@TestCaseParams(testCaseID = { "24629" })
	public void MarkTheTrueSelection() throws Exception {

		// answers test data
		// correct check: answerID = 1, 4, 7

		report.startStep("Navigate B3-U3-L2-P2");
		learningArea2 = homePage.navigateToTask("B3", 3, 2, 2, 4);
		
		report.startStep("Verify instruction text");
		learningArea2.VerificationOfQuestionInstruction(MarkTheTrueInstruction);
		
		report.startStep("Select all answers");

		for (int i = 1; i < 9; i++) {
			learningArea2.checkCheckBox(i);
		}
		for (int i = 1; i < 9; i++) {
			learningArea2.checkSingleAnswerSelectedMCQ(1, i);
		}

		report.startStep("Click on Clear and check all answers unselected");
		learningArea2.clickOnClearAnswer();
		for (int i = 1; i < 8; i++) {
			learningArea2.checkSingleAnswerUnselectedMCQ(1, i);
		}

		report.startStep("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnClear();

		report.startStep("Click on See answers and check answers selected");
		learningArea2.clickOnSeeAnswer();
		learningArea2.verifyAnswerRadioSelectedCorrect(1, 2);
		learningArea2.verifyAnswerRadioSelectedCorrect(1, 3);
		
		report.startStep("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnSeeAnswer();

		report.startStep("Click on See answers check all answers unselected");
		learningArea2.clickOnSeeAnswer();
		for (int i = 1; i < 8; i++) {
			learningArea2.checkSingleAnswerUnselectedMCQ(1, i);
		}

		report.startStep("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnClear();

		report.startStep("Select 1 correct 1 wrong answer");
		learningArea2.selectAnswerRadioByTextNum(1, 2);
		learningArea2.selectAnswerRadioByTextNum(1, 1);

		report.startStep("Click on Check Answer and check V/X signs placed correctly");
		learningArea2.clickOnCheckAnswer();
		learningArea2.verifyAnswerRadioSelectedCorrect(1, 2);
		learningArea2.verifyAnswerRadioSelectedWrong(1, 1);

		report.startStep("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnCheckAnswer();

		report.startStep("Click on Check Answer and check correct remains and wrong unselected");
		learningArea2.clickOnCheckAnswer();
		learningArea2.checkSingleAnswerSelectedMCQ(1, 2);
		learningArea2.checkSingleAnswerUnselectedMCQ(1, 1);
		learningArea2.clickOnClearAnswer();

		report.startStep("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnClear();

		report.startStep("Check next selection DOES NOT unselect current");
		learningArea2.selectAnswerRadioByTextNum(1, 1);
		learningArea2.selectAnswerRadioByTextNum(1, 2);
		learningArea2.checkSingleAnswerSelectedMCQ(1, 1);
	}
	
	@Category(inProgressTests.class)
	@Test
	@TestCaseParams(testCaseID = { "25133" })
	
	public void OpenEndedWriting() throws Exception {
	
		report.startStep("Init Test Data");
		// answers test data
		String correct0 = "He politely turned down the nomination.";
		String correct1 = "We should remember the importance of our basic values.";
		String correct2 = "We will remember this era with fondness.";

		report.startStep("Navigate A3-U6-L1-P6");
		learningArea2 = homePage.navigateToTask("A3", 6, 1, 2, 6);
		
		report.startStep("Verify instruction text");
		learningArea2.VerificationOfQuestionInstruction(openEndedWritingInstruction);

		report.startStep("Select all answers");
		learningArea2.enterAnswerTextByIndex("1", correct0);
		learningArea2.enterAnswerTextByIndex("2", correct1);
		learningArea2.enterAnswerTextByIndex("3", correct2);

		report.startStep("Click on Clear and check all answers unselected");
		learningArea2.clickOnClearAnswer();
		// TO DO Cannot verify if text placed in input box - need solution
		// learningArea.verifyNoTextInputOpenEndedByIndex("0", correct0);
		// learningArea.verifyNoTextInputOpenEndedByIndex("1", correct1);
		// learningArea.verifyNoTextInputOpenEndedByIndex("2", correct2);
		 
		report.report("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnClear();

		report.report("Click on See answers and check answers selected");
		learningArea2.clickOnSeeAnswer();
		sleep(1);
		// TO DO Cannot verify if text placed in input box - need solution

		report.report("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnSeeAnswer();

		report.report("Click on See answers check all answers unselected");
		learningArea2.clickOnSeeAnswer();
		// TO DO Cannot verify if text placed in input box - need solution

		report.report("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnClear();

		report.startStep("Select 1 correct 1 wrong answer");
		learningArea2.enterAnswerTextByIndex("1", correct0);
		learningArea2.enterAnswerTextByIndex("2", correct1 + "NOT CORRECT");

		report.startStep("Click on Check Answer and check V/X signs placed correctly");
		learningArea2.clickOnCheckAnswer();
		learningArea2.verifyAnswerOpenEndedCorrect(0);
		learningArea2.verifyAnswerOpenEndedWrong(1);

		report.report("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnCheckAnswer();

		report.startStep("Click on Check Answer and check correct remains and wrong unselected");
		// TO DO Cannot verify if text placed in input box - need solution
		learningArea2.clickOnCheckAnswer();
		learningArea2.clickOnClearAnswer();

		report.report("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnClear();
	}
	
	
	//methods for matching game
	public  void Retrieve4GameCards() throws Exception   {
		 learningArea2.Card1.click();
		 learningArea2.Card8.click();
		 sleep(3);
		 learningArea2.Card3.click();
		 learningArea2.Card7.click();
		 sleep(3);
	}
	public void Select2CorrectCard(){
		 learningArea2.Card2.click();
		 learningArea2.Card1.click(); 
	}
		
	public void Select2IncorrectCards(){
		 learningArea2.Card8.click();
		 learningArea2.Card3.click();
	}
	
	// Test for Matching game new template
	
	@Test
	@TestCaseParams(testCaseID = { "46046" })
	public void AlphabetNewMatchingGame_Media() throws Exception {

			String  card = "card2";
			String IncorrectCard = "card3";
				
	report.startStep("Navigate FD-U1-L1-P2");
		 learningArea2 = homePage.navigateToTask("FD", 1, 1, 3, 3);
		
	report.startStep("Open question verification instruction");
		 learningArea2.VerificationOfQuestionInstruction(alphabetQuestionInstruction);
	     sleep(2);
		
	report.startStep(" Ability to retrieval matching game cards");
	     Retrieve4GameCards();
	     learningArea2.checkCardIsInGameBoard(IncorrectCard);
	report.startStep("Correct selection and verification of the correct game cards in the bank and check media");
		 Select2CorrectCard();
		 sleep(3);
		 learningArea2.checkCardIsInBank(card);
		 sleep(3);
	report.startStep("Press on sound btn and check media");
		 learningArea2.sound.click();
		 learningArea2.checkAudioFileAng("AngularMediaPlayer", "s1alaap03.mp3");
		 learningArea2.clickOnClearAnswer();
	report.startStep("Incorrect selection and verification of the correct game cards");	 
	     Select2IncorrectCards();
	     learningArea2.checkCardIsInGameBoard(IncorrectCard);
	     sleep(2);
       
	}
	
	
	@Test
	@TestCaseParams(testCaseID = { "24635" }, skippedBrowsers = {"firefox"}) // Actions API not supported by geckodriver)
	public void SequenceSentenceDragAndDrop2() throws Exception {
		
		
		report.startStep("Navigate A1-U6-L2-P2");
		learningArea2 = homePage.navigateToTask("A1", 6, 2, 2, 2);
				
		report.startStep("Verify scope of elements displayed in RIGHT SIDE ");
		learningArea2.verifyPracticeToolsStateOnClear();
		learningArea2.VerificationOfQuestionInstruction(SequenceSentenceQuestionInstruction);
		
		dragAndDrop = new NewUxDragAndDropSection(webDriver, testResultService);
		dragAndDrop.retrieveDraggableNewSeqText();
		dragAndDrop.verifySixDraggableSegmentsInOrder();
		
		report.startStep("Practice Tools Basic Functionality Verification");
		learningArea2.verifyPracticeToolsStateOnClear();
		webDriver.refresh();
		learningArea2.clickOnStep(2);
		learningArea2.clickOnTaskByNumber(2);
		sleep(3);
		dragAndDrop.verifyDragAndDropsImagesRandomPlacementAfterRefreshNewSeqText();
		dragAndDrop.dragAndDropReadingSequenceSentenceNewSeq();
		
		report.startStep("Drag 1 tile to correct place, press Check Answer and verify the answer checked as correct");
		String imageNameCorrect = "89";
		String correctSeq = "prSeqImg__container_1";
		
		dragAndDrop.dragAndDropSequenceAnswerByImageNameToPlaceInOrderNewSeqText(imageNameCorrect, correctSeq);
		learningArea2.clickOnCheckAnswer();
		learningArea2.verifyPracticeToolsStateOnCheckAnswer();
		dragAndDrop.checkDragAndDropAnswerForNewSeqText(imageNameCorrect, true);
		learningArea2.clickOnClearAnswer();
		
		
		webDriver.refresh();
		learningArea2.waitToLearningAreaLoaded();
		learningArea2.clickOnStep(2);
		learningArea2.clickOnTaskByNumber(2);
		sleep(2);
		learningArea2.closeTaskBar();
		report.startStep("Drag 1 tile to wrong place, press Check Answer and verify the answer checked as wrong");
		String imageNameWrong = "89";
		String wrongSeq = "prSeqImg__container_3";
	
		
		dragAndDrop.dragAndDropSequenceAnswerByImageNameToPlaceInOrderNewSeqText(imageNameCorrect, wrongSeq);
		learningArea2.clickOnCheckAnswer();
		learningArea2.verifyPracticeToolsStateOnCheckAnswer();
		dragAndDrop.checkDragAndDropAnswerForNewSeqText(imageNameWrong, false);
		learningArea2.clickOnClearAnswer();
		sleep(1);

		report.report("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnClear();
				
	}
	
	
	@Test
	@TestCaseParams(testCaseID = { "36820" })
	public void SequenceSentenceDragAndDrop_TestMode2() throws Exception {
		
	report.startStep("Init test data");
		int taskNum = 2;
		String expectedScore = "6%";
		String  [] textid = {"1","8","28","56","63","64","77"};
		String[] places = {"undefined_0","undefined_1","undefined_2","undefined_3","undefined_4","undefined_5","undefined_6" };
		//String[] places = {"prSeqImg__container_1","prSeqImg__container_2","prSeqImg__container_3","prSeqImg__container_4","prSeqImg__container_5","prSeqImg__container_6","prSeqImg__container_7" };
			
		
	report.startStep("Navigate B1-U9-L4-S8-T2");
		learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "B1", 9, 4);
		sleep(2);
		NavigateToTestTask(8, taskNum);
				
	report.startStep("Verify instructions text");
		learningArea2.VerificationOfQuestionInstruction(SequenceSentenceQuestionInstructionTest);
			
	report.startStep("Verify scope of elements displayed in RIGHT SIDE ");
		dragAndDrop = new NewUxDragAndDropSection(webDriver, testResultService);
		dragAndDrop.retrieveDraggableNewSeqText();
		dragAndDrop.verifyNumOfDraggableSegmentsInOrder(7);	
			
	report.startStep("Drag all tile t");
		for (int i=0; i<places.length;i++){
			sleep(1);
			dragAndDrop.dragAndDropSequenceAnswerByImageNameToPlaceInOrderNewSeqText(textid[i], places[i]);
			}
		
		dragAndDrop.dragAndDropSequenceAnswerByImageNameToPlaceInOrderNewSeqText(textid[0], places[0]);
	report.startStep("Navigate to next task and return");
		learningArea2.clickOnNextButton();
		sleep(1);
		learningArea2.clickOnBackButton();
		dragAndDrop.verifyNumOfDraggableSegmentsInOrder(7);	
			
	report.startStep("Submit test and check score");
		learningArea2.submitTest(true);
		String score = learningArea2.getTestScore();	
		testResultService.assertEquals(expectedScore, score, "Checking Test Score validity");
			
	report.startStep("Press on Review and open task bar");
		learningArea2.clickOnReviewTestResults();
		learningArea2.openTaskBar();
			
	report.startStep("Check matching task indication - partly correct");
		learningArea2.checkTestTaskMarkIsPartlyCorrect(taskNum);
			
	report.startStep("Press on Task " + taskNum);
		learningArea2.clickOnTaskByNumber(taskNum);
		sleep(4); 
		report.startStep("Check the answers");
		dragAndDrop.checkDragAndDropAnswerForNewSeqText(textid[0], true);
		dragAndDrop.checkDragAndDropAnswerForNewSeqText(textid[1], false);
		
	report.startStep("Check Correct Answer Tab");
		learningArea2.clickOnCorrectAnswerTab();
		sleep(1);
		dragAndDrop.verifyNumOfDraggableSegmentsInOrder(7);	
	}
	
	@Category(inProgressTests.class)
	@Test
	@TestCaseParams(testCaseID = { "36820" })
	public void SequenceSentenceDragAndDrop_TestMode() throws Exception {
		
		report.startStep("Init test data");
			int taskNum = 2;
			String expectedScore = "15%";
			int wrongSequence_1 = 4;
			int wrongSequence_2 = 5;
			String [] answers = new String [] {"Salesclerk: Good morning! How can I help you?",
											"Customer: I'm looking for some sports equipment for my kids.",
											"Salesclerk: Do you want indoor or outdoor equipment?",
											"Salesclerk: How about table tennis?",
											"Customer: Indoor - so they can use it all year round.",
											"a good idea!",
											"Salesclerk: Great. Follow me."};
			//Customer: I'm looking for some sports equipment for my kids. 
			                //looking for some sports equipment for my kids.
		report.startStep("Navigate B1-U9-L4-S8-T2");
			learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "B1", 9, 4);
			sleep(1);
			NavigateToTestTask(8, taskNum);
				
		report.startStep("Verify instructions text");
			learningArea2.VerificationOfQuestionInstruction(SequenceSentenceQuestionInstructionTest);
		
		report.startStep("Initialize elements");
			dragAndDrop = new NewUxDragAndDropSection(webDriver, testResultService);
			dragAndDrop.retrieveDraggableSegments();
			dragAndDrop.verifyNumOfDraggableSegmentsInOrder(7);
		
		report.startStep("Drag 1 tile to correct place and 1 to wrong place");
		
			for (int i = 0; i < answers.length; i++) {
	
			dragAndDrop.dragAndDropSequenceAnswerByTextToPlaceInOrder(answers[i], i+1);
			
			}
	
		report.startStep("Navigate to next task and return");
			learningArea2.clickOnNextButton();
			sleep(1);
			learningArea2.clickOnBackButton();
		
		report.startStep("Make sure order of sentences remains");
			sleep(2);
			List<WebElement> sequenceDraggableItems = webDriver.getElementsByXpath("//div[@class='dnditem draggable']");
			testResultService.assertEquals(sequenceDraggableItems.get(3).getText().trim(), answers[3], "Answer order didn't change");
			testResultService.assertEquals(sequenceDraggableItems.get(4).getText().trim(), answers[4], "Answer order didn't change");
		
		
		
			/*testResultService.assertEquals(webDriver.waitForElement("//table[@class='textTable']//td[@id='" + wrongSequence_1 + "']/div/div/div",
					ByTypes.xpath).getText(), answers[3], "Answer order didn't change");
			testResultService.assertEquals(webDriver.waitForElement("//table[@class='textTable']//td[@id='" + wrongSequence_2 + "']/div/div/div",
					ByTypes.xpath).getText(), answers[4], "Answer order didn't change");
		*/
		report.startStep("Submit test and check score");
			learningArea2.submitTest(true);
			String score = learningArea2.getTestScore();	
			testResultService.assertEquals(expectedScore, score, "Checking Test Score validity");
			
		report.startStep("Press on Review and open task bar");
			learningArea2.clickOnReviewTestResults();
			learningArea2.openTaskBar();
			
		report.startStep("Check matching task indication - partly correct");
			learningArea2.checkTestTaskMarkIsPartlyCorrect(taskNum);
			
		report.startStep("Press on Task " + taskNum);
			learningArea2.clickOnTaskByNumber(taskNum);
			sleep(1);
			
		report.startStep("Check Your Answer Tab");
			learningArea2.clickOnYourAnswerTab();
			sleep(2);
			List<WebElement> correctAnswers = webDriver.getElementsByXpath("//h2[contains(@class,'containerHeader containerHeader--txt header--')]");
			//List<String> textOfAnswers = correctAnswers.stream().map(el->el.getText()).collect(Collectors.toList());
			for(int i = 0;i<correctAnswers.size()-1;i++) {
				if(i<3||i>4) {
					textService.assertTrue("Incorrect answer sign", correctAnswers.get(i).getAttribute("class").contains("header--v"));	
				}else {
					textService.assertTrue("Incorrect answer sign", correctAnswers.get(i).getAttribute("class").contains("header--x"));
				}
			}
			
			
			//textService.assertEquals("There are uncorrect answers", sequenceDraggableItems.size(), correctAnswers.size());
			//dragAndDrop.checkDragAndDropCorrectAnswerForSequenceSentence(answers[0]);
			//dragAndDrop.checkDragAndDropCorrectAnswerForSequenceSentence(answers[1]);
			//dragAndDrop.checkDragAndDropCorrectAnswerForSequenceSentence(answers[2]);
			//dragAndDrop.checkDragAndDropWrongAnswerForSequenceSentence(answers[3]);
			//dragAndDrop.checkDragAndDropWrongAnswerForSequenceSentence(answers[4]);
			//dragAndDrop.checkDragAndDropCorrectAnswerForSequenceSentence(answers[5]);
			//dragAndDrop.checkDragAndDropCorrectAnswerForSequenceSentence(answers[6]);
		
		report.startStep("Check Correct Answer Tab");
			learningArea2.clickOnCorrectAnswerTab();
			sleep(1);
			List<WebElement> checkCorrectAnswers = webDriver.getElementsByXpath("//div[@class='dnditem']");
			testResultService.assertEquals(answers[4],checkCorrectAnswers.get(3).getText().trim(), "Correct answer didn't change order");
			testResultService.assertEquals(answers[3],checkCorrectAnswers.get(4).getText().trim(),"Correct answer didn't change order");
			
		report.startStep("Log out from LA");
			learningArea2.logOutOfED();
	}
	
	@Test
	@TestCaseParams(testCaseID = { "24634" }, skippedBrowsers = {"firefox"}) // Actions API not supported by geckodriver)
	public void NewSequenceImageDragAndDrop() throws Exception {
		
		report.startStep("Navigate A3-U5-L2-P2");
		learningArea2 = homePage.navigateToTask("A3", 5, 2, 2, 2);
				
		report.startStep("Verify scope of elements displayed in RIGHT SIDE");
		learningArea2.verifyPracticeToolsStateOnClear();
		learningArea2.VerificationOfQuestionInstruction(SequenceImageQuestionInstruction);
		dragAndDrop = new NewUxDragAndDropSection(webDriver, testResultService);
		dragAndDrop.retrieveDraggableNewSeq();
	    dragAndDrop.verifySixDraggableSegmentsInOrderNewSeq();
		
		report.startStep("Practice Tools Basic Functionality Verification");
		webDriver.refresh();
		learningArea2.clickOnStep(2);
		learningArea2.clickOnTaskByNumber(2);
		sleep(3);
		dragAndDrop.verifyDragAndDropsImagesRandomPlacementAfterRefreshNewSeq();
		dragAndDrop.dragAndDropReadingSequenceSentenceNewSeq();
		
		report.startStep("Drag 1 tile to correct place, press Check Answer and verify the answer checked as correct");
		String imageNameCorrect = "A3RSMGPGP3";
		String correctSeq = "prSeqImg__container_3";
		
		dragAndDrop.dragAndDropSequenceAnswerByImageNameToPlaceInOrderNewSeq(imageNameCorrect, correctSeq);
		learningArea2.clickOnCheckAnswer();
		learningArea2.verifyPracticeToolsStateOnCheckAnswer();
		dragAndDrop.checkDragAndDropAnswerForNewSeq(imageNameCorrect, true);
		learningArea2.clickOnClearAnswer();
		
		report.startStep("Drag 1 tile to wrong place, press Check Answer and verify the answer checked as wrong");
		String imageNameWrong = "A3RSMGPGP6";
		String wrongSeq = "prSeqImg__container_5";
		dragAndDrop.dragAndDropSequenceAnswerByImageNameToPlaceInOrderNewSeq(imageNameWrong, wrongSeq);
		learningArea2.clickOnCheckAnswer();
		learningArea2.verifyPracticeToolsStateOnCheckAnswer();
		dragAndDrop.checkDragAndDropAnswerForNewSeq(imageNameWrong, false);
		learningArea2.clickOnClearAnswer();
						
	}
	
	
	
	
	@Test
	@TestCaseParams(testCaseID = { "25136" })
	public void FreeWrite() throws Exception {

		report.startStep("Init Test Data");
		String texInput = "Hello";
		String sectionNum = "1";
		
		report.startStep("Navigate to First Discoveries ->Unit 2-About Me->Steve's Application Form-> Practice -> Task 3");
		learningArea2 = homePage.navigateToTask("FD", 2, 4, 3, 3);
		
		report.startStep("Verify Task Instruction");
		learningArea2.VerificationOfQuestionInstruction(FreeWriteQuestionInstruction);
		
		report.startStep("Verify Send To Teacher btn displayed");
		learningArea2.checkThatSendToTeacherBtnIsDisplayed();
		
		report.startStep("Check only Clear tool displayed");
		learningArea2.checkThatClearToolIsDisplayed();
		learningArea2.checkThatSeeAnswerToolIsNotDisplayed();
		learningArea2.checkThatCheckAnswerToolIsNotDisplayed();
				
		report.startStep("Enter Text and press Clear");
		learningArea2.enterFreeWritingTextBySection(sectionNum, texInput);
		learningArea2.clickOnClearAnswer();
		
		report.startStep("Check text cleared");
		learningArea2.verifyNoTextInputByWritingSection(sectionNum);
				
		report.startStep("Enter Text and press Send To Teacher");
		learningArea2.enterFreeWritingTextBySection(sectionNum, texInput);
		learningArea2.clickOnSendToTeacher();
		
		report.startStep("Validate alert message and close it");
		learningArea2.validateAlertModalByMessage(freeWriteAlertAfterSubmit, true);
		sleep(2);
		
		report.startStep("Validate alert message closed");
		learningArea2.checkThatAlertMessageClosed();
		

	}
	
	
	@Test
	@TestCaseParams(testCaseID = { "46682" }, skippedBrowsers = {"firefox"}) // Actions API not supported by geckodriver)
	public void NewMatchTextToPicDragAndDrop_Media2() throws Exception {
	
		report.startStep("Init Test Data");
		String[] words = new String[] {  "bed","apple","dog", "cat", "frog", "girl" };

		report.startStep("Navigate FD-U1-L1-P4");
		learningArea2 = homePage.navigateToTask("FD", 1, 1, 3, 4);
		sleep(2);
		
		report.startStep("Verify Task Instruction");
		learningArea2.VerificationOfQuestionInstruction(matchTextToPicDragAndDropInstruction);

		report.startStep("Drag all tiles to droptargets");
        dragAndDrop2 = new NewUxDragAndDropSection2(webDriver, testResultService);
		
		report.startStep("Drag all tiles to droptargets");
		for(int i=0;i<words.length;i++){
			dragAndDrop2.dragAndDropAnswerByTextToTargetMTTP(words[i], i+1,TaskTypes.MTTP);
		}

		report.startStep("Click on Clear and check all tiles back to bank");
		learningArea2.clickOnClearAnswer();
		for (int i = 0; i < words.length; i++) {
			dragAndDrop2.checkTilesIsInBankMTTP(words[i]);
		}

		report.report("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnClear();

		report.report("Click on See answers and check tiles placed");
		learningArea2.clickOnSeeAnswer();
		for (int i = 0; i < words.length; i++) {
			dragAndDrop2.checkTileIsPlacedMTTP(words[i], TaskTypes.MTTP);
		}

		report.report("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnSeeAnswer();

		report.report("Click on See answers and check tiles back to bank");
		learningArea2.clickOnSeeAnswer();
		for (int i = 0; i < words.length; i++) {
			dragAndDrop2.checkTilesIsInBankMTTP(words[i]);
		}

		report.report("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnClear();

		report.startStep("Drag 1 tile to correct place and 1 to wrong place");
		String correctAns = words[1];
		int correctTarget = 1;
		String wrongAns = words[3];
		int wrongTarget = 2;
		dragAndDrop2.dragAndDropAnswerByTextToTargetMTTP(correctAns, correctTarget, TaskTypes.MTTP);
		sleep(1);
		dragAndDrop2.dragAndDropAnswerByTextToTargetMTTP(wrongAns, wrongTarget, TaskTypes.MTTP);
		sleep(1);

		report.startStep("Click on Check Answer and check V/X signs placed correctly");
		learningArea2.clickOnCheckAnswer();
		dragAndDrop2.checkDragAndDropAnswerMarkMTTP(correctAns, correctTarget, true, TaskTypes.MTTP);
	    dragAndDrop2.checkDragAndDropAnswerMarkMTTP(wrongAns, wrongTarget, false, TaskTypes.MTTP);

		report.report("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnCheckAnswer();

		report.startStep("Click on Check Answer and check correct tile remains and wrong tile back to bank");
		learningArea2.clickOnCheckAnswer();
		dragAndDrop2.checkTilesIsInBankMTTP(wrongAns);
		dragAndDrop2.checkTileIsPlacedMTTP(correctAns, TaskTypes.MTTP);
		learningArea2.clickOnClearAnswer();

		report.report("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnClear();

		report.startStep("Check you can replace current tile by the next tile by drag on it");
		String currentTile = words[2];
		String nextTile = words[3];
		int targetId = 4;
		dragAndDrop2.dragAndDropAnswerByTextToTargetMTTP(currentTile, targetId, TaskTypes.MTTP);
		dragAndDrop2.checkTileIsPlacedMTTP(currentTile, TaskTypes.MTTP);
		sleep(1);
		dragAndDrop2.dragAndDropAnswerByTextToTargetMTTP(nextTile, targetId, TaskTypes.MTTP);
		dragAndDrop2.checkTileIsPlacedMTTP(nextTile, TaskTypes.MTTP);
		dragAndDrop2.checkTilesIsInBankMTTP(currentTile);

		//report.startStep("Hover on tile and check media");
		//learningArea2.sound.click();
		//learningArea2.checkAudioFile("AngularMediaPlayer", "s1alaap04.mp3");
		
	}
		
	
	
	//New Match Text To Picture Test Mode
	
	@Test
	@TestCaseParams(testCaseID = { "47153" }, skippedBrowsers = {"firefox"}) // Actions API not supported by geckodriver)
	public void NewMatchTextToPicDragAndDrop_TestMode2() throws Exception {
		
	report.startStep("Init Test Data");
		String[] words = new String[] { "pollution", "solar panels", "coal", "wind turbine" };
		
		int taskNum = 3;
		String expectedScore = "5%";
		String wrongTile = words[1];
		String correctTile = words[3];
		int correctRow = 1;
					
	report.startStep("Navigate B3-U9-L6-S4-T3");
		learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "B3", 9, 6);
		sleep(1);
		NavigateToTestTask(4, taskNum);
		
	report.startStep("Verify instruction text");
		learningArea2.VerificationOfQuestionInstruction(matchTextToPicDragAndDropInstructionTM);

	report.startStep("Drag all tiles to droptargets");
		dragAndDrop2 = new NewUxDragAndDropSection2(webDriver, testResultService);
				
		for(int i=0;i<words.length;i++){
			dragAndDrop2.dragAndDropAnswerByTextToTargetMTTP(words[i], i+1,TaskTypes.MTTP);			
		}
		
	report.startStep("Drag all tiles to bank");
		for (int i = 0; i < words.length; i++) {
			dragAndDrop2.dragAndDropAnswerByTextToBankMTTP(words[i], TaskTypes.MTTP );
			sleep(1);
		}
	report.startStep("Place tile to correct row");
		dragAndDrop2.dragAndDropAnswerByTextToTargetMTTP(correctTile, correctRow, TaskTypes.MTTP);
		dragAndDrop2.checkTileIsPlacedMTTP(correctTile, TaskTypes.MTTP);

	report.startStep("Navigate to other task and back and check correct tile still placed");
		learningArea2.clickOnNextButton();
		sleep(1);
		learningArea2.clickOnBackButton();
		sleep(1);
		dragAndDrop2.checkTileIsPlacedMTTP(correctTile, TaskTypes.MTTP);
		
	report.startStep("Place wrong tile to wrong row");
		dragAndDrop2.dragAndDropAnswerByTextToTargetMTTP(wrongTile, correctRow+1, TaskTypes.MTTP);
				
	report.startStep("Submit test and check score");
		learningArea2.submitTest(true);
		String score = learningArea2.getTestScore();
		testResultService.assertEquals(expectedScore, score, "Checking Test Score validity");
		
	report.startStep("Press on Review and open task bar");
		learningArea2.clickOnReviewTestResults();
		learningArea2.openTaskBar();
		
	report.startStep("Check matching task indication - partly correct");
		learningArea2.checkTestTaskMarkIsPartlyCorrect(taskNum);
				
	report.startStep("Press on Task 3");
		learningArea2.clickOnTaskByNumber(taskNum);
		sleep(1);
		
	report.startStep("Check Your Answer Tab");
		learningArea2.clickOnYourAnswerTab();
		sleep(1);
		dragAndDrop2.checkDragAndDropAnswerMarkMTTP(correctTile, correctRow, true, TaskTypes.MTTP);
		dragAndDrop2.checkDragAndDropAnswerMarkMTTP(wrongTile, correctRow+1, false, TaskTypes.MTTP);
		sleep(1);
	report.startStep("Check Correct Answer Tab");
		learningArea2.clickOnCorrectAnswerTab();
		for (int i = 0; i < words.length; i++) {
			dragAndDrop2.checkTileIsPlacedMTTP(words[i], TaskTypes.MTTP);
		}
	}
	
	@Test
	@TestCaseParams(testCaseID = { "24605" })
	public void CloseWholeBmpDragAndDrop_Media() throws Exception {

		webDriver.maximize();
		
	report.startStep("Init Test Data");
		String[] words = new String[] { "When", "study","Tuesday", "English", "Thursday" };

	report.startStep("Navigate FD-U5-L7-P1");
		learningArea2 = homePage.navigateToTask("FD", 5, 7, 3, 1);
			
	report.startStep("Verify Task Instruction");
		learningArea2.VerificationOfQuestionInstruction(closeWholeBmpDragAndDropInstruction);
		
		report.startStep("Drag all tiles to droptargets");
		dragAndDrop = new NewUxDragAndDropSection(webDriver, testResultService);

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
		
		learningArea = new NewUxLearningArea(webDriver, testResultService);
		
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
	
	// Changes according close  with image new template 
	@Category (inProgressTests.class)
	@Test
	@TestCaseParams(testCaseID = { "24605" })
	public void CloseWholeBmpDragAndDrop_Media2() throws Exception {

		webDriver.maximize();
		
	report.startStep("Init Test Data");
		String[] words = new String[] { "When", "study","Tuesday", "English", "Thursday" };

	report.startStep("Navigate FD-U5-L7-P1");
		learningArea2 = homePage.navigateToTask("FD", 5, 7, 3, 1);
			
	report.startStep("Verify Task Instruction");
		learningArea2.VerificationOfQuestionInstruction(closeWholeBmpDragAndDropInstruction);
		
	report.startStep("Drag all tiles to droptargets");
		dragAndDrop2 = new NewUxDragAndDropSection2(webDriver, testResultService);
		
		for(int i=0;i<words.length;i++){
			dragAndDrop2.dragAndDropAnswerByTextToTarget(words[i], i+1, TaskTypes.Close);
			sleep(1);
		}
		
	report.startStep("Click on Clear and check all tiles back to bank");
		learningArea2.clickOnClearAnswer();
		for (int i = 0; i < words.length; i++) {
			dragAndDrop2.checkTileIsInBank(words[i]);

	report.report("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnClear();

	report.report("Click on See answers and check tiles placed");
		learningArea2.clickOnSeeAnswer();
		for (i = 0; i < words.length; i++) {
			dragAndDrop2.checkTileIsPlaced(words[i], TaskTypes.Close);
		}

	report.report("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnSeeAnswer();

	report.report("Click on See answers and check tiles back to bank");
		learningArea2.clickOnSeeAnswer();
		for (i = 0; i < words.length; i++) {
			dragAndDrop2.checkTileIsInBank(words[i]);
		}

	report.report("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnClear();

	report.startStep("Drag 1 tile to correct place and 1 to wrong place");
		String correctAns = words[0];
		int correctTarget = 1;
		String wrongAns = words[2];
		int wrongTarget = 2;
		dragAndDrop2.dragAndDropAnswerByTextToTarget(correctAns, correctTarget, TaskTypes.Close);
		dragAndDrop2.dragAndDropAnswerByTextToTarget(wrongAns, wrongTarget, TaskTypes.Close);

	report.startStep("Click on Check Answer and check V/X signs placed correctly");
		learningArea2.clickOnCheckAnswer();
		dragAndDrop2.checkDragAndDropAnswerMark(correctAns, 1, true, TaskTypes.Close);
		dragAndDrop2.checkDragAndDropAnswerMark(wrongAns, 2, false, TaskTypes.Close);

	report.report("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnCheckAnswer();

	report.startStep("Click on Check Answer and check correct tile remains and wrong tile back to bank");
	    learningArea2.clickOnCheckAnswer();
	    dragAndDrop2.checkTileIsInBank(wrongAns);
	    dragAndDrop2.checkTileIsPlaced(correctAns, TaskTypes.Close);
	    learningArea2.clickOnClearAnswer();

	report.report("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnClear();

	report.startStep("Check you can replace current tile by the next tile by drag on it");
		String currentTile = words[2];
		String nextTile = words[3];
		int targetId = 4;
		dragAndDrop2.dragAndDropAnswerByTextToTarget(currentTile, targetId, TaskTypes.Close);
		dragAndDrop2.checkTileIsPlaced(currentTile, TaskTypes.Close);
		sleep(1);
		dragAndDrop2.dragAndDropAnswerByTextToTarget(nextTile, targetId, TaskTypes.Close);
		dragAndDrop2.checkTileIsPlaced(nextTile, TaskTypes.Close);
		dragAndDrop2.checkTileIsInBank(currentTile);


	report.startStep("Press on Headphone btn and check media");
		learningArea2.sound.click();
		sleep(1);
		learningArea2.checkAudioFile("AngularMediaPlayer", "s1vealp01.mp3");
	
	}}
	

@Test
@TestCaseParams (testCaseID ={"51979"})

public void OpenEndedBank2 () throws Exception {
	
	String [] correctAnswers = {"Dear","eggs","cheese","Thanks"};
	String [] textarea ={"qId_1_alId_1","qId_1_alId_2","qId_1_alId_3","qId_1_alId_4"};

	report.startStep("Navigate FD-U3-L4-S3-T2");
		learningArea2 = homePage.navigateToTask("FD", 3, 4, 3, 2);
	
	report.startStep("Verify instruction text");
		learningArea2.VerificationOfQuestionInstruction(openEndedBank);
	
	
	report.startStep("Input all answers");
		for (int i = 0; i < correctAnswers.length; i++) {
		learningArea2.enterOpenSmallSegmentAnswerTextByIndex2(textarea[i], correctAnswers[i]);
		}
	report.startStep("Click on Clear and check all answers unselected");
		learningArea2.clickOnClearAnswer();
		for ( int i = 0; i < correctAnswers.length; i++) {
		learningArea2.verifyNoTextInputInOpenSmallSegmentByIndex2(textarea[i]);
		}
	report.startStep("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnClear();
		
	report.startStep("Click on See answers and check answers placed");
		learningArea2.clickOnSeeAnswer();
		sleep(1);
		for (int i = 0; i < correctAnswers.length; i++) {
		String actualAnswer = learningArea2
				.getTextInputInOpenSmallSegmentByIndex2(textarea[i]);
		testResultService.assertEquals(correctAnswers[i], actualAnswer,
				"Answer wrong or not placed into input box");
		}
		
		report.startStep("Verify Practice Tools State");
		learningArea2.clickOnClearAnswer();
		learningArea2.verifyPracticeToolsStateOnClear();

	report.startStep("Select 1 correct 1 wrong answer");
		learningArea2.enterOpenSmallSegmentAnswerTextByIndex2(textarea[0], correctAnswers[0]);
		learningArea2.enterOpenSmallSegmentAnswerTextByIndex2(textarea[1], correctAnswers[0]);
		
		report.startStep("Click on Check Answer and check V/X signs placed correctly");
		learningArea2.clickOnCheckAnswer();
		learningArea2.verifySmallAnswerOpenEndedCorrect2("0");
		learningArea2.verifySmallAnswerOpenEndedWrong2("1");
		
		report.startStep("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnCheckAnswer();
		
		report.startStep("Click on Check Answer and check correct remains and wrong unselected");
		learningArea2.clickOnCheckAnswer();
		String actualAnswer = learningArea2.getTextInputInOpenSmallSegmentByIndex2(textarea[0]);
		testResultService.assertEquals(correctAnswers[0], actualAnswer,	"Answer wrong or not placed into input box");
		learningArea2.getTextInputInOpenSmallSegmentByIndex2(textarea[1]);
		learningArea2.clickOnClearAnswer();

	report.startStep("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnClear();
		
	}



@Test
@TestCaseParams(testCaseID = { "51974" })
public void OpenEndedSegments_Media2() throws Exception {

	
report.startStep("Init Test Data");
	
	String correct1 = "The police officers are arresting the man.";
	String correct2 = "The woman is telling her story in court today.";
	String correct3 = "I have a fine for parking in front of a \"No Parking\" sign.";
	String correct4 = "The judge is sending the thief to prison.";
	String correct5 = "This parking ticket says I must pay $20.";
	String vCheck ="prOpenEnded__qaItem--textareaW prOpenEnded__qaItem--textareaW--vCheck";
	String xCheck ="prOpenEnded__qaItem--textareaW prOpenEnded__qaItem--textareaW--vCheck";
	
	String [] correctAnswers = new String [] {correct1,correct2,correct3,correct4};
	String [] textarea ={"qId_1_alId_1","qId_2_alId_1","qId_3_alId_1","qId_4_alId_1"};
	
	
report.startStep("Navigate B1-U2-L6-P3");
	learningArea2 = homePage.navigateToTask("B1", 2, 6, 2, 3);
			
report.startStep("Verify instruction text");
	learningArea2.VerificationOfQuestionInstruction(openEndedSegmentsInstruction);
	
report.startStep("Input all answers");
	for (int i = 0; i < correctAnswers.length; i++) {	
		learningArea2.enterOpenSmallSegmentAnswerTextByIndex2(textarea[i], correctAnswers[i]);
	}
	
report.startStep("Click on Clear and check all answers unselected");
	learningArea2.clickOnClearAnswer();
	dragAndDrop2.scrollCustomElement(TaskTypes.OpenEnded, -80);
	for (int i = 0; i < correctAnswers.length; i++) {
		learningArea2.verifyNoTextInputInOpenSmallSegmentByIndex2(textarea[i]);
	}
	
report.startStep("Verify Practice Tools State");
	learningArea2.verifyPracticeToolsStateOnClear();

report.startStep("Click on See answers and check answers placed");
	learningArea2.clickOnSeeAnswer();
	sleep(1);
	for (int i = 0; i < correctAnswers.length; i++) {
		String actualAnswer = learningArea2
				.getTextInputInOpenSmallSegmentByIndex2(textarea[i]);
		testResultService.assertEquals(correctAnswers[i], actualAnswer,
				"Answer wrong or not placed into input box");
	}
	
report.startStep("Verify Practice Tools State");
	learningArea2.verifyPracticeToolsStateOnSeeAnswer();

report.startStep("Click on See answers check all answers removed");
	learningArea2.clickOnSeeAnswer();
	for (int i = 0; i < correctAnswers.length; i++) {
		learningArea2.verifyNoTextInputInOpenSmallSegmentByIndex2(textarea[i]);
	}
report.startStep("Verify Practice Tools State");
	learningArea2.verifyPracticeToolsStateOnClear();
	
report.startStep("Select 1 correct 1 wrong answer");
	learningArea2.enterOpenSmallSegmentAnswerTextByIndex2(textarea[0], correctAnswers[0]);
	learningArea2.enterOpenSmallSegmentAnswerTextByIndex2(textarea[1], correctAnswers[0]);
	
report.startStep("Click on Check Answer and check V/X signs placed correctly");
	learningArea2.clickOnCheckAnswer();
	learningArea2.verifyAnswerOpenEndedCorrectNew ("0", vCheck);
	//learningArea2.verifyAnswerOpenEndedCorrect2("0");
	learningArea2.verifyAnswerOpenEndedCorrectNew ("1", xCheck);
	
report.startStep("Verify Practice Tools State");
	learningArea2.verifyPracticeToolsStateOnCheckAnswer();

report.startStep("Click on Check Answer and check correct remains and wrong unselected");
	learningArea2.clickOnCheckAnswer();
	String actualAnswer = learningArea2.getTextInputInOpenSmallSegmentByIndex2(textarea[0]);
	testResultService.assertEquals(correctAnswers[0], actualAnswer,	"Answer wrong or not placed into input box");
	learningArea2.getTextInputInOpenSmallSegmentByIndex2(textarea[1]);
	learningArea2.clickOnClearAnswer();
	
	learningArea2.clickOnClearAnswer();

report.startStep("Verify Practice Tools State");
	learningArea2.verifyPracticeToolsStateOnClear();
	
	

report.startStep("Press on Headphone btn and check media");
		learningArea2.sound.click();
		learningArea2.checkAudioFile("AngularMediaPlayer", "b1veafp03.mp3");
	
}


@Test
@TestCaseParams(testCaseID = { "51974" })
public void OpenEndedSmallSegments2() throws Exception {

	
	report.startStep("Init Test Data");	
	String correct1 = "player";
	String correct2 = "win";
	String correct3 = "championship";
	String correct4 = "greatest";
	String correct5 = "motivates";
	String correct6 = "motivation";
	String correct7 = "diploma";
	String correct8 = "passing";
	
	String [] correctAnswers = new String [] {correct1,correct2,correct3,correct4,correct5,correct6,correct7};
	String [] textarea ={"qId_1_alId_1","qId_1_alId_2","qId_1_alId_3","qId_1_alId_4","qId_1_alId_5","qId_1_alId_6","qId_1_alId_7"};

	report.startStep("Navigate I1-U1-L1-P5");
	learningArea2 = homePage.navigateToTask("I1", 1, 1, 2, 5);
			
	report.startStep("Verify instruction text");
	learningArea2.VerificationOfQuestionInstruction(openEndedSmallSegmentsInstruction);
	
	report.startStep("Input all answers");
	for (int i = 0; i < correctAnswers.length; i++) {
		if (i==4){
			dragAndDrop2.scrollCustomElement(TaskTypes.OpenEnded, 150);
		}
		learningArea2.enterOpenSmallSegmentAnswerTextByIndex2(textarea[i], correctAnswers[i]);
	}
	
report.startStep("Click on Clear and check all answers unselected");
	learningArea2.clickOnClearAnswer();
	dragAndDrop2.scrollCustomElement(TaskTypes.OpenEnded, -150);
	
	for (int i = 0; i < correctAnswers.length; i++) {
		
		if (i==4){
			dragAndDrop2.scrollCustomElement(TaskTypes.OpenEnded, 150);
		}
		
		learningArea2.verifyNoTextInputInOpenSmallSegmentByIndex2(textarea[i]);
	
}
	report.startStep("Verify Practice Tools State");
	learningArea2.verifyPracticeToolsStateOnClear();

report.startStep("Click on See answers and check answers placed");
	dragAndDrop2.scrollCustomElement(TaskTypes.OpenEnded, -150);	
	learningArea2.clickOnSeeAnswer();
	sleep(1);
	for (int i = 0; i < correctAnswers.length; i++) {
		
		if (i==4){
			dragAndDrop2.scrollCustomElement(TaskTypes.OpenEnded, 150);
		}
		
		String actualAnswer = learningArea2
				.getTextInputInOpenSmallSegmentByIndex2(textarea[i]);
		testResultService.assertEquals(correctAnswers[i], actualAnswer,
				"Answer wrong or not placed into input box");
	}
	
report.startStep("Verify Practice Tools State");
	learningArea2.verifyPracticeToolsStateOnSeeAnswer();

report.startStep("Click on See answers check all answers removed");
	learningArea2.clickOnSeeAnswer();
		dragAndDrop2.scrollCustomElement(TaskTypes.OpenEnded, -150);
	
	for (int i = 0; i < correctAnswers.length; i++) {
		if (i==4){
			dragAndDrop2.scrollCustomElement(TaskTypes.OpenEnded, 150);
		}
		learningArea2.verifyNoTextInputInOpenSmallSegmentByIndex2(textarea[i]);
	}
report.startStep("Verify Practice Tools State");
	learningArea2.verifyPracticeToolsStateOnClear();
	
report.startStep("Select 1 correct 1 wrong answer");
	dragAndDrop2.scrollCustomElement(TaskTypes.OpenEnded, -150);
	learningArea2.enterOpenSmallSegmentAnswerTextByIndex2(textarea[0], correctAnswers[0]);
	learningArea2.enterOpenSmallSegmentAnswerTextByIndex2(textarea[1], correctAnswers[0]);
	
report.startStep("Click on Check Answer and check V/X signs placed correctly");
	learningArea2.clickOnCheckAnswer();
	learningArea2.verifySmallAnswerOpenEndedCorrect2("0");
	learningArea2.verifySmallAnswerOpenEndedWrong2("1");
	
report.startStep("Verify Practice Tools State");
	learningArea2.verifyPracticeToolsStateOnCheckAnswer();

report.startStep("Click on Check Answer and check correct remains and wrong unselected");
	learningArea2.clickOnCheckAnswer();
	String actualAnswer = learningArea2.getTextInputInOpenSmallSegmentByIndex2(textarea[0]);
	testResultService.assertEquals(correctAnswers[0], actualAnswer,	"Answer wrong or not placed into input box");
	learningArea2.getTextInputInOpenSmallSegmentByIndex2(textarea[1]);
	learningArea2.clickOnClearAnswer();
	
	learningArea2.clickOnClearAnswer();

report.startStep("Verify Practice Tools State");
	learningArea2.verifyPracticeToolsStateOnClear();
	
	
}


@Test
@TestCaseParams(testCaseID = { "25133" })

public void OpenEndedWriting2() throws Exception {

	report.startStep("Init Test Data");
	// answers test data
	String correct0 = "He politely turned down the nomination.";
	String correct1 = "We should remember the importance of our basic values.";
	String correct2 = "We will remember this era with fondness.";
	
	String [] textarea ={"qId_1_alId_1","qId_1_alId_2","qId_1_alId_3"};
	String [] correctAnswers = new String [] {correct0,correct1,correct2};

	report.startStep("Navigate A3-U6-L1-P6");
	learningArea2 = homePage.navigateToTask("A3", 6, 1, 2, 6);
	
	report.startStep("Verify instruction text");
	learningArea2.VerificationOfQuestionInstruction(openEndedWritingInstruction);

	report.startStep("Input all answers");
	for (int i = 0; i < correctAnswers.length; i++) {
		
		learningArea2.enterOpenSmallSegmentAnswerTextByIndex2(textarea[i], correctAnswers[i]);
	}

	
report.startStep("Click on Clear and check all answers unselected");
	learningArea2.clickOnClearAnswer();
	for (int i = 0; i < correctAnswers.length; i++) {
		learningArea2.verifyNoTextInputInOpenSmallSegmentByIndex2(textarea[i]);

}
	
	
	report.startStep("Click on See answers and check answers placed");
	learningArea2.clickOnSeeAnswer();
	sleep(1);
	for (int i = 0; i < correctAnswers.length; i++) {
		String actualAnswer = learningArea2
				.getTextInputInOpenSmallSegmentByIndex2(textarea[i]);
		testResultService.assertEquals(correctAnswers[i], actualAnswer,
				"Answer wrong or not placed into input box");
	}
	
report.startStep("Verify Practice Tools State");
	learningArea2.verifyPracticeToolsStateOnSeeAnswer();

report.startStep("Click on See answers check all answers removed");
	learningArea2.clickOnSeeAnswer();
	for (int i = 0; i < correctAnswers.length; i++) {
		learningArea2.verifyNoTextInputInOpenSmallSegmentByIndex2(textarea[i]);
	}
report.startStep("Verify Practice Tools State");
	learningArea2.verifyPracticeToolsStateOnClear();
	
report.startStep("Select 1 correct 1 wrong answer");
	learningArea2.enterOpenSmallSegmentAnswerTextByIndex2(textarea[0], correctAnswers[0]);
	learningArea2.enterOpenSmallSegmentAnswerTextByIndex2(textarea[1], correctAnswers[0]);
	
report.startStep("Click on Check Answer and check V/X signs placed correctly");
	learningArea2.clickOnCheckAnswer();
	learningArea2.verifyAnswerOpenEndedCorrect2("0");
	learningArea2.verifyAnswerOpenEndedWrong2("1");
	
report.startStep("Verify Practice Tools State");
	learningArea2.verifyPracticeToolsStateOnCheckAnswer();

report.startStep("Click on Check Answer and check correct remains and wrong unselected");
	learningArea2.clickOnCheckAnswer();
	String actualAnswer = learningArea2.getTextInputInOpenSmallSegmentByIndex2(textarea[0]);
	testResultService.assertEquals(correctAnswers[0], actualAnswer,	"Answer wrong or not placed into input box");
	learningArea2.getTextInputInOpenSmallSegmentByIndex2(textarea[1]);
	learningArea2.clickOnClearAnswer();
	
	learningArea2.clickOnClearAnswer();

report.startStep("Verify Practice Tools State");
	learningArea2.verifyPracticeToolsStateOnClear();

	
}


	
	@Test
	@TestCaseParams(testCaseID = { "33783" }, skippedBrowsers = {"firefox"}) // Actions API not supported by geckodriver)
	public void InsertTextNewTemp() throws Exception {
				
		report.startStep("Init test data");
		int taskNum = 6;
		int wrongMarkerNum = 1; 
		int correctMarkerNum = 2; 
		int numOfMarkers = 4;
					
		
		report.startStep("Navigate B1-U9-L5-S3");
		learningArea2 = homePage.navigateToTask("B1", 9, 5, 3, taskNum);
		
		report.startStep("Verify instruction text");
		learningArea2.VerificationOfQuestionInstruction(insertTextInstruction);
			
		report.startStep("Check Text Resource contains insert buttons");
		markers = learningArea2.getInsertTextButtonsElements();
		testResultService.assertEquals(numOfMarkers, markers.size(), "Insert text buttons not found or their number is not as expected");
			
		report.startStep("Check Sentence answer");
		WebElement answerSentence = learningArea2.getInsertSentenceAnswerElement();
		testResultService.assertEquals(true, answerSentence.isDisplayed(), "Insert text answer not found");
		testResultService.assertEquals(insertTextAnswerSentence, answerSentence.getText(), "Insert text answer not correct");
			
		report.startStep("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnClear();
			
		report.startStep("Mouse hover on insert btn highlight");
		String imagePosition = learningArea2.hoverOnInsertBtnAndGetImageSpritePositionY(2);
		testResultService.assertEquals("550px", imagePosition, "Button is not highlighted after hover");
			
		report.startStep("Click on markers and check sentence inserted & answer greyed out on right side");
			for (int i = 0; i < markers.size(); i++) {
				learningArea2.clickOnMarkerAndVerifySentenceInsert(i+1,insertTextAnswerSentence);
					
			}
			
		report.startStep("Click on clear and verify no sentence in left side");
		learningArea2.clickOnClearAnswer();
		checkInsertTextTemplateDefaultState();
			
		report.startStep("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnClear();
			
		report.startStep("Click on See Answer and verify correct answer placed");
		learningArea2.clickOnSeeAnswer();
		sleep(2);
		learningArea2.verifySentenceInsertOnSeeAnswer(correctMarkerNum, insertTextAnswerSentence);
			
		report.startStep("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnSeeAnswer();
			
		report.startStep("Click on See Answer and verify no sentence in left side");
		learningArea2.clickOnSeeAnswer();
		checkInsertTextTemplateDefaultState();
			
		report.startStep("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnClear();
			
		report.startStep("Click on correct marker");
		learningArea2.clickOnMarkerAndVerifySentenceInsert(correctMarkerNum,insertTextAnswerSentence);

		report.startStep("Click on Check Answer and verify V mark placed");
		learningArea2.clickOnCheckAnswer();
		learningArea2.verifySentenceMarkedAsCorrectOnCheckAnswer(correctMarkerNum);
			
		report.startStep("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnCheckAnswer();
			
		report.startStep("Click on Check Answer and verify no sentence in left side");
		learningArea2.clickOnCheckAnswer();
		checkInsertTextTemplateDefaultState();
			
		report.startStep("Click on wrong marker");
		learningArea2.clickOnMarkerAndVerifySentenceInsert(wrongMarkerNum,insertTextAnswerSentence);

		report.startStep("Click on Check Answer and verify X mark placed");
		learningArea2.clickOnCheckAnswer();
		learningArea2.verifySentenceMarkedAsWrongOnCheckAnswer(wrongMarkerNum);
			
		report.startStep("Click on Check Answer and verify no sentence in left side");
		learningArea2.clickOnCheckAnswer();
		checkInsertTextTemplateDefaultState();
			
		report.startStep("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnClear();
			
		report.startStep("Go to previous task and back - check default state");
		learningArea2.clickOnBackButton();
		learningArea2.clickOnTaskByNumber(taskNum);
		sleep(1);
		checkInsertTextTemplateDefaultState();
			
				
	}
		
	@Test
	@TestCaseParams(testCaseID = { "33638" })
	public void EditTextNewTemp() throws Exception {
		
		report.startStep("Init test data");
		int taskNum = 1;
		int correctInputBoxNum = 1;
		int wrongInputBoxNum = 2;
							
		String[] correctWords = new String[] { "When", "are", "goes","walk", "call" };
				
		report.startStep("Navigate B3-U9-L5-S4");
		learningArea2 = homePage.navigateToTask("B3", 9, 5, 4, taskNum);
							
		report.startStep("Verify instruction text");
		learningArea2.VerificationOfQuestionInstruction(editTextInstruction);
					
		report.startStep("Check the same content in both sides");
		checkEditTextDefaultState();
					
		report.startStep("Check input boxes can be edited");
		for (int i = 0; i < rightContentInputsElements.size();  i++) {
			learningArea2.editTextByInputBoxNumber(i+1, "sample");
		}
			
		report.startStep("Click on clear and verify default state");
		learningArea2.clickOnClearAnswer();
		checkEditTextDefaultState();
			
		report.startStep("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnClear();
			
		report.startStep("Click on See Answer and verify correct answer placed");
		learningArea2.clickOnSeeAnswer();
		sleep(1);
		for (int i = 0; i < rightContentInputsElements.size();  i++) {
			testResultService.assertEquals(correctWords[i], rightContentInputsElements.get(i).getAttribute("value"), "Correct text not inserted or wrong");
		}
			
		report.startStep("Click on See Answer and verify no sentence in left side");
		learningArea2.clickOnSeeAnswer();
		checkEditTextDefaultState();
			
		report.startStep("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnClear();
			
		report.startStep("Insert correct edit text");
		learningArea2.editTextByInputBoxNumber(correctInputBoxNum, correctWords[0]);
			
		report.startStep("Click on Check Answer and verify V mark placed");
		learningArea2.clickOnCheckAnswer();
		learningArea2.verifyEditTextCorrectMarkByInputNumber(correctInputBoxNum);
			
		report.startStep("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnCheckAnswer();
			
		report.startStep("Click on Check Answer and verify correct answer left");
		learningArea2.clickOnCheckAnswer();
		testResultService.assertEquals(correctWords[0], rightContentInputsElements.get(0).getAttribute("value"), "Correct text not inserted or wrong");
			
		report.startStep("Insert wrong edit text");
		learningArea2.editTextByInputBoxNumber(wrongInputBoxNum, correctWords[0]+"incorrect");
			
		report.startStep("Click on Check Answer and verify X mark placed");
		learningArea2.clickOnCheckAnswer();
		learningArea2.verifyEditTextWrongtMarkByInputNumber(wrongInputBoxNum);
			
		report.startStep("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnCheckAnswer();
			
		report.startStep("Click on Check Answer and verify wrong answer not left");
		learningArea2.clickOnCheckAnswer();
		testResultService.assertEquals(leftContentInputsElements.get(1).getText().trim(), rightContentInputsElements.get(1).getAttribute("value").trim(), "Wrong text not removed");
					
		report.startStep("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnClear();
				
		report.startStep("Go to prev task and back - check default state");
		learningArea2.clickOnBackButton();
		sleep(1);
		learningArea2.clickOnNextButton();
		checkEditTextDefaultState();
			
	}
	
	
	@Test
	@TestCaseParams(testCaseID = { "33903" })
	public void HighlightTextNewTemp() throws Exception {

		report.startStep("Init Test Data");
		int taskNum = 2;
		int stepNum = 3;
		String [] answer = new String [] {"question-1_answer-1","question-1_answer-3","question-1_answer-4"};
						
		report.startStep("Navigate I1-U10-L2-S3");
		learningArea2 = homePage.navigateToTask("I1", 10, 2, stepNum, taskNum);
											
		report.startStep("Verify instruction text");
		learningArea2.VerificationOfQuestionInstruction(highlightTextInstruction);
		sleep(2);
					
		report.startStep("Check the highlighted words in text correspond to highlighted word in question");
		highlightedText = learningArea2.getHighlightedTextElementsNewMcq();
		highlightedAnswer = learningArea2.getHighlightedQuestionElementsNewMcq();
		for (int i = 0; i < highlightedText.size(); i++) {
			testResultService.assertEquals(highlightedText.get(i).getText().toLowerCase(),highlightedAnswer.get(0).getText().toLowerCase(),"Check highlighted texts in right & lift side");
		}
			
		report.startStep("Select answers. Click on Clear and check  answers selected");
		   
		    learningArea2.SelectRadioBtn(answer[0]);
		    sleep(2);
			learningArea2.clickOnClearAnswer();
			learningArea2.clickOnSeeAnswer();
			sleep(1);
			learningArea2.clickOnSeeAnswer();
			
		report.startStep("Select 1 correct click on Check Answer and check V/X signs placed correctly");
		    learningArea2.SelectRadioBtn(answer[2]);
		    sleep(2);
			learningArea2.clickOnCheckAnswer();
			sleep(2);
			learningArea2.CorrectnessVMCQ();
			learningArea2.clickOnClearAnswer();
			
		report.startStep("Select 1 wromg click on Check Answer and check V/X signs placed correctly");
		    learningArea2.SelectRadioBtn(answer[1]);
			learningArea2.clickOnCheckAnswer();
			learningArea2.CorrectnessXMCQ();
			sleep(1);

		report.report("Verify Practice Tools State");
			learningArea2.verifyPracticeToolsStateOnCheckAnswer();
			learningArea2.clickOnCheckAnswer();

			
		report.startStep("Check next selection unselect current");
		   learningArea2.SelectRadioBtn(answer[0]);
		   sleep(1);
		   learningArea2.SelectRadioBtn(answer[1]);
		
	}
	
	@Test
	@TestCaseParams(testCaseID = { "33847" })
	public void SelectTextNewTemp() throws Exception {
						
		report.startStep("Init test data");
		int taskNum = 2;
		String correctAnswer = "Walk your dog in the morning."; 
		int correctMarkerNum = 3;
		int wrongMarkerNum = 1;
						
		report.startStep("Navigate B1-U9-L1-S5");
		learningArea2 = homePage.navigateToTask("B1", 9, 1, 5, taskNum);
		learningArea2.waitToLearningAreaLoaded();
		
		report.startStep("Verify instruction text");
		learningArea2.checkSelectTextTaskInstruction(selectTextInstruction);
								
		report.startStep("Check Select Text item default state");
		checkSelectTextTemplateDefaultState();
			
		report.startStep("Click on markers and check sentence selected & placed in answer area in right side");
		for (int i = 0; i < markers.size(); i++) {
			learningArea2.clickOnMarkerAndVerifySentenceSelect(i+1);
					
		}
			
		report.startStep("Click on clear and verify no sentence in left side");
		learningArea2.clickOnClearAnswer();
		checkSelectTextTemplateDefaultState();
					
		report.startStep("Click on See Answer and verify correct answer placed");
		learningArea2.clickOnSeeAnswer();
		sleep(1);
		learningArea2.verifySentenceSelectOnSeeAnswer(correctAnswer);
			
		report.startStep("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnSeeAnswer();
			
		report.startStep("Click on See Answer and verify no sentence in left side");
		learningArea2.clickOnSeeAnswer();
		checkSelectTextTemplateDefaultState();
					
		report.startStep("Click on correct marker");
		learningArea2.clickOnMarkerAndVerifySentenceSelect(correctMarkerNum);

		report.startStep("Click on Check Answer and verify V mark placed");
		learningArea2.clickOnCheckAnswer();
		learningArea2.verifySentenceSelectedAsCorrectOnCheckAnswer(correctMarkerNum);
			
		report.startStep("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnCheckAnswer();
			
		report.startStep("Click on Check Answer and verify no sentence in right side");
		learningArea2.clickOnCheckAnswer();
		checkSelectTextTemplateDefaultState();
			
		report.startStep("Click on wrong marker");
		learningArea2.clickOnMarkerAndVerifySentenceSelect(wrongMarkerNum);

		report.startStep("Click on Check Answer and verify X mark placed");
		learningArea2.clickOnCheckAnswer();
		learningArea2.verifySentenceSelectedAsWrongOnCheckAnswer(wrongMarkerNum);
			
		report.startStep("Click on Check Answer and verify no sentence in left side");
		learningArea2.clickOnCheckAnswer();
		checkSelectTextTemplateDefaultState();
					
		report.startStep("Go to next task and back - check default state");
		learningArea2.clickOnNextButton();
		learningArea2.clickOnTaskByNumber(taskNum);
		sleep(1);
		checkSelectTextTemplateDefaultState();
				
	}
		
	@Test
	@TestCaseParams(testCaseID = { "34160" })
	public void OpenWritingNewTemp() throws Exception {
	
	report.startStep("Create and login to ED as student and navigate to Open Writing task");
	homePage.clickOnLogOut();
	webDriver.switchToTopMostFrame();
	String classNameOR = configuration.getProperty("classname.openSpeech");
	//String institutionId = configuration.getInstitutionId();
	//studentId = pageHelper.createUSerUsingSP(institutionId, classNameOR);
	String studentUserNameOR = dbService.getUserNameById(studentId, institutionId);
					
	loginPage = new NewUXLoginPage(webDriver,testResultService);
	homePage = loginPage.loginAsStudent(studentUserNameOR, "12345");
	homePage.closeAllNotifications();
	homePage.waitHomePageloadedFully();
		
	report.startStep("Navigate to B1-U9-L3-S4");
	learningArea2 = homePage.navigateToTask("B1", 9, 3, 4, 3);
	sleep(2);
	
	report.startStep("Verify instruction text");
	learningArea2.VerificationOfQuestionInstruction(openWritingtInstruction);
	
	report.startStep("Enter answer text and submit");
	Date date = new Date();
	String uniqueLabel = date.toString();
	
	String answerOW = "automated answer for open writing: "+ uniqueLabel;
	webDriver.swithcToFrameAndSendKeys("//body[@id='tinymce']", answerOW, "elm1_ifr");
	sleep(2);
	webDriver.switchToTopMostFrame();
	learningArea2.clickOnSubmitToTeacherOW();
	sleep(2);
	learningArea2.validateAlertModalByMessage(openWritingConfirmationAlert,true);
	
	report.startStep("Logout as a student");
	learningArea2.clickOnLogoutLearningArea();
	webDriver.switchToTopMostFrame();
	loginPage = new NewUXLoginPage(webDriver, testResultService);
	testResultService.assertEquals(false, loginPage.isSubmitButtonEnabled(), "Checking that ED Login Page displayed");
	
	report.startStep("Login as Teacher");
	String tName = configuration.getProperty("teacher.username");
	String tID = dbService.getUserIdByUserName(tName, institutionId);
	pageHelper.setUserLoginToNull(tID);
	tmsHomePage = loginPage.enterTeacherUserAndPassword();
	homePage.closeAllNotifications();
	tmsHomePage.waitForPageToLoad();
		
	tmsHomePage = new TmsHomePage(webDriver, testResultService);
	tmsHomePage.switchToMainFrame();

	report.startStep("Open teachers Inbox page");
	tmsHomePage.clickOnCommunication();
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
	sleep(4);
	
	report.startStep("Logout as teacher");
	webDriver.switchToMainWindow();
	tmsHomePage.switchToMainFrame();
	tmsHomePage.clickOnExit();
	tmsHomePage.verifyExitNoDisplay();
	webDriver.switchToTopMostFrame();
	testResultService.assertEquals(false, loginPage.isSubmitButtonEnabled(), "Checking that ED Login Page displayed");
	sleep(1);
	
	report.startStep("Login as student");
	loginPage.loginAsStudent(studentUserNameOR,"12345");
	homePage.closeAllNotifications();
	homePage.waitHomePageloaded();
	
	report.startStep("Click on Inbox");
	inboxPage = homePage.openInboxPage(false);
	inboxPage.switchToInboxFrame();
	inboxPage.verifyInboxPageTitle();
	
	report.startStep("Verify recieved teacher reply");
	inboxPage.clickOnElement("//table[@id='tblInbox']/tbody/tr[5]/td[3]/a");
	webDriver.switchToPopup();
	webDriver.switchToFrame("ReadWrite");
	testResultService.assertEquals(answerOW, inboxPage.getInboxMessageText(), "Teacher reply does not match to actually sent by teacher");
		
	}
	
	@Test
	@TestCaseParams(testCaseID = { "36819" }, skippedBrowsers = {"firefox"}) // Actions API not supported by geckodriver)
	public void ClassificationDragAndDropTestMode2() throws Exception {
				
		report.startStep("Init Test Data");
		String[] words = new String[] { "swimming pool", "gym", "sports field", "dancing", "lifting weights", "swimming","park","running" };
		
		int taskNum = 3;
		String expectedScore = "3%";
		String wrongTile = words[4];
		String correctTile = words[3];
		int correctCol = 1;
					
		report.startStep("Navigate B1-U9-L1-T3");
		learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "B1", 9, 1);
		sleep(1);
		NavigateToTestTask(6, taskNum);
		report.startStep("Verify instruction text");
		learningArea2.VerificationOfQuestionInstruction(classificationDragAndDropTestInstruction);
		report.startStep("Drag all tiles to droptargets");
		dragAndDrop2 = new NewUxDragAndDropSection2(webDriver, testResultService);
				
		report.startStep("Drag all tiles to droptargets");
		int column =1;
		
		for (int i = 0; i < words.length; i++) {
			
			if (i%2==0) column = 1;
			else column = 2; 
			dragAndDrop2.dragAndDropAnswerByTextToTarget(words[i], column, TaskTypes.Classification);
			sleep(1);
						
		}
		
		report.startStep("Drag all tiles to bank");
		for (int i = 0; i < words.length; i++) {
			dragAndDrop2.dragAndDropAnswerByTextToBank(words[i], TaskTypes.Classification);
			sleep(1);
		}
		
		report.startStep("Place tile to correct column");
		dragAndDrop2.dragAndDropAnswerByTextToTarget(correctTile, correctCol, TaskTypes.Classification);
		dragAndDrop2.checkTileIsPlaced(correctTile, TaskTypes.Classification);
	
		report.startStep("Navigate to other task and back and check correct tile still placed");
		learningArea2.clickOnBackButton();
		sleep(1);
		learningArea2.clickOnNextButton();
		sleep(1);
		dragAndDrop2.checkTileIsPlaced(correctTile, TaskTypes.Classification);
		
		report.startStep("Place wrong tile to wrong column");
		dragAndDrop2.dragAndDropAnswerByTextToTarget(wrongTile, correctCol+1, TaskTypes.Classification);
				
		report.startStep("Submit test and check score");
		learningArea2.submitTest(true);
		String score = learningArea2.getTestScore();
		testResultService.assertEquals(expectedScore, score, "Checking Test Score validity");
		
		report.startStep("Press on Review and open task bar");
		learningArea2.clickOnReviewTestResults();
		learningArea2.openTaskBar();
		
		report.startStep("Check classification task indication - partly correct");
		learningArea2.checkTestTaskMarkIsPartlyCorrect(taskNum);
				
		report.startStep("Press on Task 3");
		learningArea2.clickOnTaskByNumber(taskNum);
		sleep(1);
		
		report.startStep("Check Your Answer Tab");
		learningArea2.clickOnYourAnswerTab();
		sleep(5);
		dragAndDrop2.checkDragAndDropAnswerMark(correctTile, correctCol, true, TaskTypes.Classification);
		dragAndDrop2.checkDragAndDropAnswerMark(wrongTile, correctCol+1, false, TaskTypes.Classification);
		sleep(5);
		
		report.startStep("Check Correct Answer Tab");
		learningArea2.clickOnCorrectAnswerTab();
		
		for (int i = 0; i < words.length; i++) {
			dragAndDrop2.checkTileIsPlaced(words[i], TaskTypes.Classification);
		}
				
	}
	
	
	
	///Matching on the test mode new template
	
	@Test
	@TestCaseParams(testCaseID = { "45870" }, skippedBrowsers = {"firefox"}) // Actions API not supported by geckodriver)
	public void MatchingDragAndDropTestMode2() throws Exception {
				
		report.startStep("Init Test Data");
		String[] words = new String[] { "film", "designer", "artist", "effects", "set" };
		
		int taskNum = 3;
		String expectedScore = "4%";
		String wrongTile = words[3];
		String correctTile = words[4];
		int correctRow = 1;
					
		report.startStep("Navigate I2-U10-L1-T3");
		learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "I2", 10, 1);
		sleep(1);
		NavigateToTestTask(5, taskNum);
		
		report.startStep("Verify instruction text");
		learningArea2.VerificationOfQuestionInstruction(MatchingDragAndDropTestInstruction);

		report.startStep("Drag all tiles to droptargets");
		dragAndDrop2 = new NewUxDragAndDropSection2(webDriver, testResultService);
				
		report.startStep("Drag all tiles to droptargets");

		for(int i=0;i<words.length;i++){
			dragAndDrop2.dragAndDropAnswerByTextToTarget(words[i], i+1,TaskTypes.Matching);
						
		}
		
		report.startStep("Drag all tiles to bank");
		for (int i = 0; i < words.length; i++) {
			dragAndDrop2.dragAndDropAnswerByTextToBank(words[i], TaskTypes.Matching );
			sleep(1);
		}
	
		report.startStep("Place tile to correct row");
		dragAndDrop2.dragAndDropAnswerByTextToTarget(correctTile, correctRow, TaskTypes.Matching);
		dragAndDrop2.checkTileIsPlaced(correctTile, TaskTypes.Matching);
	
		report.startStep("Navigate to other task and back and check correct tile still placed");
		learningArea2.clickOnBackButton();
		sleep(1);
		learningArea2.clickOnNextButton();
		sleep(1);
		dragAndDrop2.checkTileIsPlaced(correctTile, TaskTypes.Matching);
		
		report.startStep("Place wrong tile to wrong row");
		dragAndDrop2.dragAndDropAnswerByTextToTarget(wrongTile, correctRow+1, TaskTypes.Matching);
				
		report.startStep("Submit test and check score");
		learningArea2.submitTest(true);
		String score = learningArea2.getTestScore();
		testResultService.assertEquals(expectedScore, score, "Checking Test Score validity");
		
		report.startStep("Press on Review and open task bar");
		learningArea2.clickOnReviewTestResults();
		learningArea2.openTaskBar();
		
		report.startStep("Check matching task indication - partly correct");
		learningArea2.checkTestTaskMarkIsPartlyCorrect(taskNum);
				
		report.startStep("Press on Task 3");
		learningArea2.clickOnTaskByNumber(taskNum);
		sleep(1);
		
		report.startStep("Check Your Answer Tab");
		learningArea2.clickOnYourAnswerTab();
		sleep(1);
		dragAndDrop2.checkDragAndDropAnswerMark(correctTile, correctRow, true, TaskTypes.Matching);
		dragAndDrop2.checkDragAndDropAnswerMark(wrongTile, correctRow+1, false, TaskTypes.Matching);
		sleep(1);
		
		report.startStep("Check Correct Answer Tab");
		learningArea2.clickOnCorrectAnswerTab();
		
		for (int i = 0; i < words.length; i++) {
			dragAndDrop2.checkTileIsPlaced(words[i], TaskTypes.Matching);
		}
				
	}
	// Close with sound test mode
	
	@Category (inProgressTests.class)
	@Test
	@TestCaseParams(testCaseID= {""}, skippedBrowsers={"firefox"})// Actions API not supported by geckodriver)
	public void CloseDragAndDropMedia_TestMode2() throws Exception{
		
		report.startStep("Init test Data");
		
		int taskNum= 2;
		String expectedScore = "3%";
		
		String[] words = new String[] {}; //??????????????????????????????????????????????
		String wrongTile = words[3];
		String correctTile = words[4];
		
		int correctSection =6;
		
		report.startStep("Navigate ----------");
		learningArea2 =homePage.navigateToCourseUnitLessonLA2(courseCodes, "I2", 1, 1);
		sleep(1);
		learningArea2.clickOnStep(6, false);
		learningArea2.clickOnStartTest();
		sleep (1);
		learningArea2.clickOnTaskByNumber(taskNum);
		sleep(1);
		
		report.startStep("Verify Instruction text");
		learningArea2.VerificationOfQuestionInstruction(closeDragAndDropTestModeInstruction);
		
		report.startStep("Drag all tiles to droptargets");
		dragAndDrop2= new NewUxDragAndDropSection2 (webDriver, testResultService);
		for(int i=0; i < words.length;i++){
			dragAndDrop2.dragAndDropAnswerByTextToTarget(words[i], i+1, TaskTypes.Close);		
		}
		
		report.startStep("Drag all tiles to the bank");
		for (int i=0; i< words.length; i++){
			dragAndDrop2.dragAndDropAnswerByTextToBank(words[i], TaskTypes.Close);
		}
		report.startStep("Place tile to correct section");
		dragAndDrop2.dragAndDropAnswerByTextToTarget(correctTile, correctSection, TaskTypes.Close);
		dragAndDrop2.checkTileIsPlaced(correctTile, TaskTypes.Close);
		
		report.startStep("Navigate to other task and back, check  correct tile is still placed");
		learningArea2.clickOnBackButton();
		sleep(1);
		learningArea2.clickOnNextButton();
		sleep(1);
		dragAndDrop2.checkTileIsPlaced(correctTile, TaskTypes.Close);
		
		report.startStep("Place wrong tile  to wrong section");
		dragAndDrop2.dragAndDropAnswerByTextToTarget(wrongTile, correctSection-1, TaskTypes.Close);
		
		/*report.startStep("Press on Headphone btn and check media");
		learningArea2.clickOnMediaById("1");
		sleep(1);
		learningArea2.checkAudioFile("PMediaPlayer", "b1ltbqp03.mp3");*/
		
		report.startStep("Submit test and check score");
		learningArea2.submitTest(true);
		String score = learningArea2.getTestScore();
		testResultService.assertEquals(expectedScore, score, "Checking Test Score validity");
		
		report.startStep("Press on review and open task bar");
		learningArea2.clickOnReviewTestResults();
		learningArea2.openTaskBar();
		
		report.startStep("Check closetask indication-partly correct");
		learningArea2.checkTestTaskMarkIsPartlyCorrect(taskNum);
		
		report.startStep("Press on Task 2");
		learningArea2.clickOnTaskByNumber(taskNum);
		sleep(1);
		
		report.startStep("Check your answer tab");
		learningArea2.clickOnYourAnswerTab();
		sleep(1);
		dragAndDrop2.checkDragAndDropAnswerMark(correctTile, correctSection, true, TaskTypes.Close);
		dragAndDrop2.checkDragAndDropAnswerMark(wrongTile, correctSection-2, false, TaskTypes.Close);
		sleep(1);
		
		report.startStep("Check Correct Answer Tab");
		learningArea2.clickOnCorrectAnswerTab();
		for (int i=0; i<words.length;i++){
			dragAndDrop2.checkTileIsPlaced(words[i], TaskTypes.Close);
		}
		
	}
	//Close on test mode new template
	
	@Test
	@TestCaseParams(testCaseID = { "45880" }, skippedBrowsers = {"firefox"}) // Actions API not supported by geckodriver)
	public void CloseDragAndDropTestMode2() throws Exception {
				
		report.startStep("Init Test Data");
		String[] words = new String[] { "reduce", "workshops", "containers", "donate", "bins","upcycling" };
		
		int taskNum = 2;
		String expectedScore = "3%";
		String wrongTile = words[4];
		String correctTile = words[3];
		int correctSection = 6;
					
		report.startStep("Navigate I2-U9-L1-T2");
		learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "I2", 9, 1);
		sleep(10);
		NavigateToTestTask(5, taskNum);
		
		report.startStep("Verify instruction text");
		learningArea2.VerificationOfQuestionInstruction(closeDragAndDropTestModeInstruction);

		report.startStep("Drag all tiles to droptargets");
		dragAndDrop2 = new NewUxDragAndDropSection2(webDriver, testResultService);
				
		report.startStep("Drag all tiles to droptargets");

		for(int i=0;i<words.length;i++){
			if (i==2){
				dragAndDrop2.scrollCustomElement(TaskTypes.MCQ,120);
			}
			dragAndDrop2.dragAndDropAnswerByTextToTarget(words[i], i+1,TaskTypes.Close);				
		}
		
		report.startStep("Drag all tiles to bank");
		dragAndDrop2.scrollCustomElement(TaskTypes.Close,-120);
		for (int i = 0; i < words.length; i++) {
			
			if (i==2){
				dragAndDrop2.scrollCustomElement(TaskTypes.Close,120);
			}
			
			dragAndDrop2.dragAndDropAnswerByTextToBank(words[i], TaskTypes.Close );
			sleep(1);
		}
	
		report.startStep("Place tile to correct Section");
		dragAndDrop2.dragAndDropAnswerByTextToTarget(correctTile, correctSection, TaskTypes.Close);
		dragAndDrop2.checkTileIsPlaced(correctTile, TaskTypes.Close);
	
		report.startStep("Navigate to other task and back and check correct tile still placed");
		learningArea2.clickOnBackButton();
		sleep(1);
		learningArea2.clickOnNextButton();
		sleep(1);
		dragAndDrop2.scrollCustomElement(TaskTypes.Close,120);
		sleep(1);
		dragAndDrop2.checkTileIsPlaced(correctTile, TaskTypes.Close);
		
		report.startStep("Place wrong tile to wrong section");
		dragAndDrop2.dragAndDropAnswerByTextToTarget(wrongTile, correctSection-2, TaskTypes.Close);
				
		report.startStep("Submit test and check score");
		learningArea2.submitTest(true);
		String score = learningArea2.getTestScore();
		testResultService.assertEquals(expectedScore, score, "Checking Test Score validity");
		
		report.startStep("Press on Review and open task bar");
		learningArea2.clickOnReviewTestResults();
		learningArea2.openTaskBar();
		
		report.startStep("Check close task indication - partly correct");
		learningArea2.checkTestTaskMarkIsPartlyCorrect(taskNum);
				
		report.startStep("Press on Task 2");
		learningArea2.clickOnTaskByNumber(taskNum);
		sleep(1);
		
		report.startStep("Check Your Answer Tab");
		learningArea2.clickOnYourAnswerTab();
		dragAndDrop2.scrollCustomElement(TaskTypes.Close,120);
		sleep(1);
		dragAndDrop2.checkDragAndDropAnswerMark(correctTile, correctSection, true, TaskTypes.Close);
		dragAndDrop2.checkDragAndDropAnswerMark(wrongTile, correctSection-2, false, TaskTypes.Close);
		sleep(1);
		
		report.startStep("Check Correct Answer Tab");
		learningArea2.clickOnCorrectAnswerTab();
		
		for (int i = 0; i < words.length; i++) {
			dragAndDrop2.checkTileIsPlaced(words[i], TaskTypes.Close);
		}
				
	}
	
	
		
	private void checkInsertTextTemplateDefaultState() throws Exception {
	learningArea2.clickOnClearAnswer();
		for (int i = 0; i < markers.size(); i++) {
			learningArea2.verifyNoSentenceInsertedByMarkerNumber(i+1);
		}
		learningArea2.verifyInsertSentenceAnswerEnabled();
	
		report.startStep("Check Text Resource tools hidden in Left Side");
		learningArea2.checkThatHearAllIsNotDisplayed();
		//learningArea.checkThatMainIdeaToolIsNotDisplayed();
		//learningArea.checkThatKeywordsToolIsNotDisplayed();
		//learningArea.checkThatReferenceWordsToolIsNotDisplayed();
		learningArea2.checkThatPrintIsNotDisplayed();
		
		/*report.startStep("Check Text Segment tools not active in Left Side"); // UNCOMMENT: will work with real text and segments
		learningArea.selectSegmentInReadingByNumber(1,2);
		learningArea.checkThatSeeTranslationNotDisplayed();
		learningArea.checkThatHearPartIsNotDisplayed();*/
		
	}
	
	private void checkSelectTextTemplateDefaultState() throws Exception {
		
		markers = learningArea2.getSelectTextButtonsElements();
		testResultService.assertEquals(true, markers.size()<=5 && markers.size() != 0 , "Select text buttons not found or their number is more then 5");
		for (int i = 0; i < markers.size(); i++) {
			Boolean isSelected = markers.get(i).getAttribute("class").contains("selected");
			testResultService.assertEquals(false, isSelected, "Text was selected though it should not");
		}
						
		WebElement answerSentence = learningArea2.getSelectSentenceAnswerElement();
		testResultService.assertEquals(true, answerSentence.getAttribute("class").contains("disabled"), "Select Text answer area not disabled");
		testResultService.assertEquals("Select Text", answerSentence.getText(), "Select Text answer area label not correct");
		
		report.startStep("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnClear();
		
		report.startStep("Verify instruction text");
		//learningArea.VerificationOfQuestionInstruction(selectTextInstruction);

		report.startStep("Check Text Resource tools hidden in Left Side");
		learningArea2.checkThatHearAllIsNotDisplayed();
		//learningArea.checkThatMainIdeaToolIsNotDisplayed();
		//learningArea.checkThatKeywordsToolIsNotDisplayed();
		//learningArea.checkThatReferenceWordsToolIsNotDisplayed();
		learningArea2.checkThatPrintIsNotDisplayed();
		
		/*report.startStep("Check Text Segment tools not active in Left Side"); // UNCOMMENT: will work with real text and segments
		learningArea.selectSegmentInReadingByNumber(1,3);
		learningArea.checkThatSeeTranslationNotDisplayed();
		learningArea.checkThatHearPartIsNotDisplayed();*/
				
		}
	
	private void checkEditTextDefaultState() throws Exception { 
	
	content = learningArea2.getEditTextContentElements();
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
	
	private void checkTilesCounters(int tilesInColumn, int numberOfColumn, boolean allTilesPlacedToTarget) throws Exception { 
				
		for (int i = 0; i < tilesInColumn; i++) {
			
				if (allTilesPlacedToTarget) dragAndDrop2.checkTileCounterStateIsActive(numberOfColumn, i+1);
				else dragAndDrop2.checkTileCounterStateIsEmpty(numberOfColumn, i+1);
					
		}
						
	}
			
	private void NavigateToTestTask (int stepNumber, int taskNumber) throws Exception{
		learningArea2.clickOnStep(stepNumber, false);
		learningArea2.clickOnStartTest();
		Thread.sleep(5000);
		learningArea2.clickOnTaskByNumber(taskNumber);
		Thread.sleep(2000);
		
	}
	
	private int getVisibleWord(int coloumn) throws Exception{
		int i = 0;
		//ToDO
		return i;
	}
	
	@After
	public void tearDown() throws Exception {
		
//		report.startStep("Set progress to first FD course item");
//		studentService.createSingleProgressRecored(studentId,"20000", "31515", 10, false, 1);
		
		super.tearDown();
	}
	
}
