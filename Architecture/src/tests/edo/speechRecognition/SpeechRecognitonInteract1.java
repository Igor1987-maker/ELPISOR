package tests.edo.speechRecognition;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebElement;

import Enums.ByTypes;
import Enums.InteractStatus;
import Interfaces.TestCaseParams;
import Objects.Course;
import Objects.Recording;
import pageObjects.EdoHomePage;
import pageObjects.InteractSection;
import pageObjects.RecordPanel;
import tests.misc.EdusoftWebTest;

public class SpeechRecognitonInteract1 extends SpeechRecognitionBasicTest {

	@Before
	public void setup() throws Exception {
		super.setup();
		enableConsoleLoggin();
		setLogFilter("Incomming message");

	}

	// Test case 13309
	// First Discoveries,Unit 1 - About Me,I'm Tom Smith,6
	
	@TestCaseParams(testCaseID = { "13309" })
	public void testInteract1() throws Exception {
		startStep("Init test data");
		Course course = pageHelper.initCouse(8);

		String[] words;
		Recording recording = pageHelper.getRecordings().get(4);

		// List<String[]> recWordLevel = new ArrayList<String[]>();
		// List<Integer> sentenceLevels = new ArrayList<Integer>();

		startStep("Login to Edo");
		EdoHomePage edoHomePage = pageHelper.loginAsStudent();
		edoHomePage.clickOnCourses();
		edoHomePage.clickOnCourseByName(course.getName());
		edoHomePage.clickOnCourseUnit(course.getCourseUnit());
		edoHomePage.clickOntUnitComponent(course.getUnitComponent(),
				"Interact 1");
		InteractSection interactSection = new InteractSection(webDriver,
				testResultService);

		startStep("Select right speaker");

		// interactSection.approveFlash();
		// interactSection.checkInstructionText(interactSection.instructionText0);

		sleep(1);

		sleep(1);
		interactSection.hoverOnSpeaker(2);
		interactSection.checkThatSpeakerTextIsHighlighted(2);
		interactSection.selectRightSpeaker();
		interactSection.checkInstructionText(interactSection.instructionText8,true);

		interactSection.checkInstructionText(interactSection.instructionText1,true);
		sleep(1);

		startStep("Check of start button is enabled and click it");
		interactSection.clickTheStartButton();
		interactSection.checkIfInteract1StatusChanged(1,
				InteractStatus.speaker, 2);
		interactSection.checkInstructionText(interactSection.instructionText3,true);

		startStep("Wait for 3 seconds and start sending sound to the mic");

		interactSection.checkIfInteract1StatusChanged(2,
				InteractStatus.counter, 3);
		interactSection.checkInstructionText(interactSection.instructionText4,true);
		sleep(5);

		interactSection.checkIfInteract1StatusChanged(2,
				InteractStatus.recorder, 20);
		interactSection.checkInstructionText(interactSection.instructionText5,true);
		sleep(1);

		startStep("Send the 1st recording");
		words = interactSection.getCurrentSpeakerText(2, textService);
		audioService
				.sendSoundToVirtualMic(recording.getFiles().get(0), 8000.0F);
		// interactSection.waitUntilRecordingEnds(4, 2);

		interactSection
				.waitForInstructionToEnd(interactSection.instructionText5);

		// interactSection.checkStatus(InteractStatus.recorder, 2);

		startStep("Check the words level of the recording");
		String[] wordLevels = textService.splitStringToArray(interactSection
				.getWordsScoring("debugScore"));
		System.out.println("Got words levels." + System.currentTimeMillis());
		textService.printStringArray(words);
		interactSection.checkInteract1WordsLevels(words, wordLevels,
				textService, 2);

		startStep("Wait for next recording");
		interactSection.checkInstructionText(interactSection.instructionText9,true);
		interactSection.checkIfInteract1StatusChanged(1,
				InteractStatus.speaker, 5);
		interactSection.checkInstructionText(interactSection.instructionText3,true);
		interactSection.checkIfInteract1StatusChanged(2,
				InteractStatus.counter, 5);
		interactSection.checkInstructionText(interactSection.instructionText4,true);
		sleep(3);

		interactSection.checkIfInteract1StatusChanged(2,
				InteractStatus.recorder, 20);
		interactSection.checkInstructionText(interactSection.instructionText5,true);
		audioService.sendSoundToVirtualMic(recording.getFiles().get(1), 0);
		sleep(1);
		interactSection
				.waitForInstructionToEnd(interactSection.instructionText5);
		// interactSection.waitUntilRecordingEnds(5, 2);
		interactSection.checkInstructionText(interactSection.instructionText10,true);
		startStep("Check the 2nd recording");
		words = interactSection.getCurrentSpeakerText(2, textService);
		wordLevels = textService.splitStringToArray(interactSection
				.getWordsScoring("debugScore"));
		System.out.println("Got words levels." + System.currentTimeMillis());
		textService.printStringArray(words);
		interactSection.checkInteract1WordsLevels(words, wordLevels,
				textService, 2);
		sleep(1);

		startStep("Click on restart and select left speaker");
		sleep(15);
		interactSection.clickTheRestartButton();
		interactSection.checkThatStartButtonIsDisabled();
		interactSection.selectLeftSpeaker();
		interactSection.checkThatSpeakerTextIsHighlighted(1);
		interactSection.clickTheStartButton();
		sleep(15);

	}

	
	
	public void testInteractWithFinalView() throws Exception {
		startStep("Init test data");
		Course course = pageHelper.initCouse(8);

		String[] words;
		List<String[]> speakerText = new ArrayList<String[]>();
		List<String[]> wordLevels = new ArrayList<String[]>();
		Recording recording = pageHelper.getRecordings().get(4);

		// List<String[]> recWordLevel = new ArrayList<String[]>();
		// List<Integer> sentenceLevels = new ArrayList<Integer>();

		startStep("Login to Edo");
		EdoHomePage edoHomePage = pageHelper.loginAsStudent();
		edoHomePage.clickOnCourses();
		edoHomePage.clickOnCourseByName(course.getName());
		edoHomePage.clickOnCourseUnit(course.getCourseUnit());
		edoHomePage.clickOntUnitComponent(course.getUnitComponent(),
				"Interact 1");
		InteractSection interactSection = new InteractSection(webDriver,
				testResultService);

		startStep("Select right speaker");
		sleep(3);
		// interactSection.approveFlash();
		interactSection.checkInstructionText(interactSection.instructionText0,true);

		sleep(1);
		interactSection.selectRightSpeaker();
		sleep(2);
		// interactSection.hoverOnSpeaker(2);
		// interactSection.checkThatSpeakerTextIsHighlighted(2);
		Thread.sleep(500);
		interactSection.checkInstructionText(interactSection.instructionText8,true);
		sleep(2);

		startStep("Check of start button is enabled and click it");
		webDriver.printScreen("Bofore start button click");
		interactSection.clickTheStartButton();
		sleep(2);
		interactSection.checkIfInteract1StatusChanged(1,
				InteractStatus.speaker, 2);
		interactSection.checkInstructionText(interactSection.instructionText4,true);

		startStep("Wait for 3 seconds and start sending sound to the mic");

		interactSection.checkIfInteract1StatusChanged(2,
				InteractStatus.counter, 3);
		interactSection.checkInstructionText(interactSection.instructionText4,true);
		sleep(5);

		interactSection.checkIfInteract1StatusChanged(2,
				InteractStatus.recorder, 20);
		interactSection.checkInstructionText(interactSection.instructionText5,true);
		sleep(1);

		startStep("Send the 1st recording");

		// words = interactSection.getCurrentSpeakerText(2, textService);
		String[] str = interactSection.getCurrentSpeakerText(2, textService);
		speakerText.add(str);
		audioService.sendSoundToVirtualMic(recording.getFiles().get(0), 16000);
		// interactSection.waitUntilRecordingEnds(4, 2);

		interactSection
				.waitForInstructionToEnd(interactSection.instructionText5);

		// interactSection.checkStatus(InteractStatus.recorder, 2);

		startStep("Check the words level of the recording");
		String[] WL = textService.splitStringToArray(interactSection
				.getWordsScoring("debugScore"));
		wordLevels.add(WL);
		System.out.println("Got words levels." + System.currentTimeMillis());
		textService.printStringArray(speakerText.get(0));
		 interactSection.checkInteract1WordsLevels(speakerText.get(0),
		 wordLevels.get(0), textService, 2);

		startStep("Wait for next recording");
		interactSection.checkInstructionText(interactSection.instructionText9,true);
		interactSection.checkIfInteract1StatusChanged(1,
				InteractStatus.speaker, 5);
		interactSection.checkInstructionText(interactSection.instructionText4,true);
		interactSection.checkIfInteract1StatusChanged(2,
				InteractStatus.counter, 5);
		interactSection.checkInstructionText(interactSection.instructionText4,true);
		sleep(3);
		interactSection.checkIfInteract1StatusChanged(2,
				InteractStatus.recorder, 20);
		interactSection.checkInstructionText(interactSection.instructionText5,true);
		audioService.sendSoundToVirtualMic(recording.getFiles().get(1), 88200);
		sleep(1);
		interactSection
				.waitForInstructionToEnd(interactSection.instructionText5);
		// interactSection.waitUntilRecordingEnds(5, 2);
		interactSection.checkInstructionText(interactSection.instructionText10,true);
		startStep("Check the 2nd recording");
		words = interactSection.getCurrentSpeakerText(2, textService);
		str = interactSection.getCurrentSpeakerText(2, textService);
		speakerText.add(str);
		wordLevels.add(textService.splitStringToArray(interactSection
				.getWordsScoring("debugScore")));
		System.out.println("Got words levels." + System.currentTimeMillis());
		textService.printStringArray(speakerText.get(1));
		// interactSection.checkInteract1WordsLevels(speakerText.get(1),
		// wordLevels.get(1), textService, 2);
		sleep(1);
		interactSection.clickOnSeeFeedback();
		startStep("Starting to check final view");

		startStep("Check that all sentences are displayed");

		startStep("Check Listen to all button");
		interactSection.clickOnListenToAllButton();
		
		

		startStep("Check recoreded text's word levels");
		interactSection.checkFinalViewWordLevels(speakerText.get(0),
				wordLevels.get(0), textService, 2);
		interactSection.checkFinalViewWordLevels(speakerText.get(1),
				wordLevels.get(1), textService, 4);
		report.report("Opening recored panel and check current recording");
		sleep(30);
//		interactSection.waitForHearAllButtomToBecomeEnabled();
		RecordPanel recordPanel = interactSection.clickOnRepairButton(2);
		sleep(5);
		edoHomePage.switchToFrameByClassName("cboxIframe");
		sleep(5);
		recordPanel.checkSentenceScoreRatingText(recording.getSL().get(0),"srPanelWrapper");
		recordPanel.checkWordsLevels(speakerText.get(1), wordLevels.get(1), textService);
		
		
		report.report("Add new recording");
		recordPanel.clickOnRecordButton();
		String status = recordPanel.getRecordPanelStatus();
		testResultService.assertEquals("SPEAK", status);
		
		audioService.sendSoundToVirtualMic(recording.getFiles().get(0),
				16000);

		startStep("Check that recording ended");
		sleep(2);
		recordPanel.waitForRecordingToEnd(1);
		webDriver.printScreen("After recording ended");

		
		compareDebugSLAndWLtoExpected(recordPanel, recording, words);
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

}
