package tests.edo.speechRecognition;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pageObjects.EdoHomePage;
import pageObjects.InteractSection;
import pageObjects.RecordPanel;
import Enums.InteractStatus;
import Objects.Course;
import Objects.Recording;
import tests.misc.EdusoftWebTest;

public class SpeechRecognitionInteract2 extends SpeechRecognitionBasicTest {

	@Before
	public void setup() throws Exception {
		super.setup();
		enableConsoleLoggin();
		setLogFilter("Incomming message");
	}


	public void testInteract2() throws Exception {
		startStep("Init test data");
		Recording recording = pageHelper.getRecordings().get(17);
		Course course = pageHelper.initCouse(10);
		List<String[]> recordedText = new ArrayList<String[]>();
		List<String[]> speakerText = new ArrayList<String[]>();
		List<String[]> wordLevels = new ArrayList<String[]>();
		String[] words = null;

		startStep("Open course unit");
		EdoHomePage edoHomePage = pageHelper.loginAsStudent();
		edoHomePage.clickOnCourses();
		edoHomePage.clickOnCourseByName(course.getName());
		edoHomePage.clickOnCourseUnit(course.getCourseUnit());
		edoHomePage.clickOntUnitComponent(course.getUnitComponent(),
				"Interact 2");
		InteractSection interactSection = new InteractSection(webDriver,
				testResultService);
		startStep("Click on Interact2 - start button");
		webDriver.printScreen("waitingForStartText");
		interactSection.checkInstructionText(interactSection.instructionText15,true);
		sleep(2);
		interactSection.clickInteract2StartButtn();
		interactSection.checkInstructionText(interactSection.instructionText20,true);
		startStep("Check that speaker text is highlighted");
		interactSection.checkThatSpeakerTextIsHighlighted();
		// sleep(3);
		startStep("Check for 1st counter and recorder");
		interactSection.checkifInteract2StatusChanged(InteractStatus.counter,
				10);
		interactSection.checkInstructionText(interactSection.instructionText4,true);
		interactSection.checkifInteract2StatusChanged(InteractStatus.recorder,
				5);
		interactSection.checkInstructionText(interactSection.instructionText5,true);

		startStep("Check that the correct text is highlighted and check score");

		audioService.sendSoundToVirtualMic(recording.getFiles().get(0), 88200);
		interactSection
				.checkifInteract2StatusChanged(InteractStatus.counter, 1);
		
		recordedText.add(interactSection.getInteract2RecordedText(textService,
				1));
		webDriver.printScreen("GotDebugScore");
		String[] WL = textService.splitStringToArray(interactSection
				.getWordsScoring("debugScore"));
		String[] str = interactSection.getCurrentSpeakerText(1, textService);
		speakerText.add(str);
		wordLevels.add(WL);
//		webDriver.printScreen("Checking word levels");
		interactSection.checkInteract2recorderText(1, speakerText.get(0),
				wordLevels.get(0), textService);
		
		interactSection.checkInstructionText(interactSection.instructionText9,true);
		
		//check WL 
		
		
		

		startStep("Check for 2nd counter and recorder");
		interactSection.checkThatSpeakerTextIsHighlighted();

		// interactSection.checkInstructionText(interactSection.instructionText4);
		sleep(3);
		interactSection.checkifInteract2StatusChanged(InteractStatus.recorder,
				5);
		audioService.sendSoundToVirtualMic(recording.getFiles().get(1), 88200);
//		interactSection.checkInstructionText(interactSection.instructionText5);
		recordedText.add(interactSection.getInteract2RecordedText(textService,
				1));
		WL=null;
		sleep(1);
		webDriver.printScreen("GotDebugScore");
		WL = textService.splitStringToArray(interactSection
				.getWordsScoring("debugScore"));
		wordLevels.add(WL);
//		webDriver.printScreen("Checking word levels");
		str = interactSection.getCurrentSpeakerText(2, textService);
		speakerText.add(str);
//		interactSection.checkInteract2recorderText(2, speakerText.get(1), wordLevels.get(1),
//				textService);

		startStep("wait untile playback ends");

		sleep(2);
		interactSection.checkInstructionText(interactSection.instructionText10,true);
		interactSection.clickOnSeeFeedback();

		startStep("Check interact final view");
		interactSection.clickOnListenToAllButton();

		startStep("Check recoreded text's word levels");
		interactSection.checkFinalViewWordLevels(recordedText.get(0),
				wordLevels.get(0), textService, 2);
		interactSection.checkFinalViewWordLevels(recordedText.get(1),
				wordLevels.get(1), textService, 4);
		report.report("Opening recored panel and check current recording");
		sleep(30);
		// interactSection.waitForHearAllButtomToBecomeEnabled();
		RecordPanel recordPanel = interactSection.clickOnRepairButton(2);
		sleep(5);
		edoHomePage.switchToFrameByClassName("cboxIframe");
		sleep(5);
		recordPanel.checkSentenceScoreRatingText(recording.getSL().get(0), "srPanelWrapper");
		recordPanel.checkWordsLevels(speakerText.get(1), wordLevels.get(1),
				textService);

		report.report("Add new recording");
		recordPanel.clickOnRecordButton();
		String status = recordPanel.getRecordPanelStatus();
		testResultService.assertEquals("SPEAK", status);

		audioService.sendSoundToVirtualMic(recording.getFiles().get(0), 88200);

		startStep("Check that recording ended");
		sleep(2);
		recordPanel.waitForRecordingToEnd(1);
		webDriver.printScreen("After recording ended");

		compareDebugSLAndWLtoExpected(recordPanel, recording, speakerText.get(1));

	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

}
