package tests.edo.newux;

import java.util.ArrayList;
import java.util.List;
import java.lang.String;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runners.MethodSorters;

import Enums.ByTypes;
import Enums.FeaturesList;
import Enums.InteractStatus;
import Enums.StepProgressBox;
import Interfaces.TestCaseParams;
import Objects.Course;
import Objects.Recording;
import pageObjects.InteractSection;
import pageObjects.RecordPanel;
import pageObjects.edo.NewUXLoginPage;
import pageObjects.edo.NewUxAssignmentsPage;
import pageObjects.edo.NewUxCommunityPage;
import pageObjects.edo.NewUxLearningArea2;
import pageObjects.edo.NewUxTalkIdiomsPage;
import pageObjects.tms.StudentRecordingsPage;
import pageObjects.tms.TmsHomePage;
import pageObjects.tms.TmsReportsPage;
import services.PageHelperService;
import testCategories.AngularLearningArea;
import testCategories.inProgressTests;
import testCategories.reg2;

@Category(AngularLearningArea.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RecoredYourselfNewUX2 extends SpeechRecognitionBasicTestNewUX {

	NewUXLoginPage loginPage;
	NewUxLearningArea2 learningArea2;
	NewUxCommunityPage communityPage;
	NewUxTalkIdiomsPage idiomsPage;
	TmsHomePage tmsHomePage;
	NewUxAssignmentsPage assignPage;
	StudentRecordingsPage tmsRecPage;
	static String instID;
	static String classNameSR;	
	static String userNameSR;
	boolean virtualMicSuccess = true;
	
//	static final String defaultUserNameSR = "stud85166839"; -- igb 2018.08.21
	static final String defaultUserNameSR = "donotdelete";
	
	boolean deleteRecordOnTestEnd = true;
	
	private static final String RECORDING_WAV = "recording.wav";
	private static String RECORDINGS_FOLDER = "smb://frontqa2016//SharedUpload//Attachments//";
	
	@Before
	public void setup() throws Exception {
		super.setup();
		setPrintResults(true);
		System.setProperty("chromeMedia", "true");
		classNameSR = configuration.getProperty("classname.openSpeech");
		instID = institutionId; //configuration.getProperty("institution.id");
				
		String sr_Engine = configuration.getAutomationParam("sr", "sr");
		
		if (sr_Engine != null && !sr_Engine.isEmpty()) {
			pageHelper.setFeaturesListPerInstallation(FeaturesList.SR_ENGINE, sr_Engine.toUpperCase());
		
		}
		webDriver.deleteCookiesAndRefresh();

		RECORDINGS_FOLDER = RECORDINGS_FOLDER + PageHelperService.recFolder +"//Recordings";

	}
	
	@Test
	@TestCaseParams(testCaseID = { "24233","24175" } )
	public void testRecordYourselfPanelBasic() throws Exception {
		
		testRecordYourselfIntegrated("FD",4,2, 1, 7, 88200);
		
		report.startStep("Set progress to first FD course item");
		setProgressInFDToStudent();
	}
	
	@Test
	@TestCaseParams(testCaseID = { "24223" })
	public void testRecordYourselfPanelVocabulary() throws Exception {
		
		report.startStep("Init test data");
		Course course = pageHelper.initCouse(9);
		String[] words = new String[] { "twenty-one" };
		List<String[]> wordsScoreList = new ArrayList<String[]>();
		List<Integer> sentenceLevels = new ArrayList<Integer>();
				
		int recordingId = 33; // segment: twenty-one
		float sampleRate = 88200;
		Recording recording = pageHelper.getRecordings().get(recordingId);
		
		report.startStep("Login to ED as student and navigate to Vocabulary lesson");
		getUserAndLoginNewUXClass();
		sleep(1);
		learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "FD", 2, 5);
		sleep(1);
		learningArea2.clickOnStep(2);
		sleep(2);
		
		report.startStep("Click on Recored Yourself Tool and send recording");
		learningArea2.clickOnRecordYourselfVocab();
		sleep(2);
		RecordPanel recordPanel=new RecordPanel(webDriver, testResultService);
		switchToRecordPanelAndClickRecord(recordPanel);
		sleep(2);
		audioService.sendSoundToVirtualMic(recording.getFiles().get(0), sampleRate);
				
		report.startStep("Check that recording ended");
		recordPanel.waitForRecordingToEnd(1);
		webDriver.printScreen("After recording ended");
		
		report.startStep("Set progress to first FD course item");
		setProgressInFDToStudent();

	}
	
	@Test
	@TestCaseParams(testCaseID = { "24241", "24242" })
	public void testInteract1_FD() throws Exception {
		
		startStep("Init test data");
		Recording recording = pageHelper.getRecordings().get(4);
		InteractSection interactSection = new InteractSection(webDriver,testResultService);
				
		report.startStep("Get user and login to ED");
		getUserAndLoginNewUXClass();
				
		report.startStep("Navigate to Interact 1 in FD-U2-L2");
		learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "FD", 2, 2);
		learningArea2.clickOnStep(4);
						
		report.startStep("Select right speaker");
		interactSection.waitForSpeakersArrowsEnabledInInteract1();
		interactSection.checkInstructionText(interactSection.instructionText0, true);
		sleep(1);
		interactSection.hoverOnSpeaker(2);
		interactSection.checkThatSpeakerTextIsHighlighted(2);
		interactSection.selectRightSpeaker();
		sleep(1);
		interactSection.checkInstructionText(interactSection.instructionText1, true);
		sleep(1);

		report.startStep("Check if Start Button is enabled and click it");
		interactSection.clickTheStartButton();
		interactSection.checkIfInteract1StatusChanged(1, InteractStatus.speaker, 7);
		interactSection.checkInstructionText(interactSection.instructionText3, true);

		report.startStep("Wait for 3 seconds");

		interactSection.checkIfInteract1StatusChanged(2, InteractStatus.counter, 3);
		interactSection.checkInstructionText(interactSection.instructionText4, true);
		sleep(3);

		interactSection.checkIfInteract1StatusChanged(2, InteractStatus.recorder, 20);
		interactSection.checkInstructionText(interactSection.instructionText5, true);
		sleep(1);

		report.startStep("Send the 1st recording to the mic");
		audioService.sendSoundToVirtualMic(recording.getFiles().get(0), 8000.0F);
		interactSection.waitForInstructionToEnd(interactSection.instructionText5);
		
		report.startStep("Wait for next recording");
		interactSection.checkInstructionText(interactSection.instructionText9, true);
		interactSection.checkIfInteract1StatusChanged(1,
				InteractStatus.speaker, 5);
		interactSection.checkInstructionText(interactSection.instructionText3, true);
		interactSection.checkIfInteract1StatusChanged(2,
				InteractStatus.counter, 5);
		interactSection.checkInstructionText(interactSection.instructionText4, true);
		sleep(3);

		interactSection.checkIfInteract1StatusChanged(2,
				InteractStatus.recorder, 20);
		
		report.startStep("Send the 2nd recording to the mic");
		interactSection.checkInstructionText(interactSection.instructionText5, true);
		audioService.sendSoundToVirtualMic(recording.getFiles().get(1), 0);
		sleep(1);
		interactSection.waitForInstructionToEnd(interactSection.instructionText5);
						
		report.startStep("Click on See Feedback button");
		interactSection.checkInstructionText(interactSection.instructionText10, true);
		interactSection.clickOnSeeFeedback();
		
		report.startStep("Click on Hear All button");
		interactSection.waitForHearAllButtomToBecomeEnabled();
		interactSection.clickOnListenToAllButton();
		
		report.startStep("Click on restart and select left speaker");
		interactSection.waitForRestartBtnIsEnabledOnFeedbackPage();
		interactSection.clickTheRestartButtonOnFeedbackPage();
		interactSection.checkThatStartButtonIsDisabled();
		interactSection.selectLeftSpeaker();
		interactSection.checkThatSpeakerTextIsHighlighted(1);
		interactSection.clickTheStartButton();
		sleep(3);
		
		report.startStep("Set progress to first FD course item");
		setProgressInFDToStudent();

	}
	
	@Test
	@TestCaseParams(testCaseID = { "38880" })
	public void testInteract1_MP4() throws Exception {
		
		startStep("Init test data");
		Recording recording = pageHelper.getRecordings().get(39);
		InteractSection interactSection = new InteractSection(webDriver,testResultService);
				
		report.startStep("Get user and login to ED");
		getUserAndLoginNewUXClass();
		sleep(2);
		
		report.startStep("Navigate to Interact 1 in B1-U9-L4-S4");
		learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "B1", 9, 4);
		learningArea2.clickOnStep(4);
		sleep(2);
		
		report.startStep("Select right speaker");
		interactSection.waitForSpeakersArrowsEnabledInInteract1();
		interactSection.checkInstructionText(interactSection.instructionText0, true);
		sleep(1);
		interactSection.hoverOnSpeaker(2);
		interactSection.checkThatSpeakerTextIsHighlighted(2);
		interactSection.selectRightSpeaker();
		sleep(1);
		interactSection.checkInstructionText(interactSection.instructionText1, true);
		sleep(1);

		report.startStep("Check if Start Button is enabled and click it");
		interactSection.clickTheStartButton();
		interactSection.checkIfInteract1StatusChanged(1, InteractStatus.speaker, 7);
		interactSection.checkInstructionText(interactSection.instructionText3, true);

		report.startStep("Wait for 3 seconds");
		interactSection.checkIfInteract1StatusChanged(2, InteractStatus.counter, 3);
		interactSection.checkInstructionText(interactSection.instructionText4, true);
		sleep(3);

		interactSection.checkIfInteract1StatusChanged(2, InteractStatus.recorder, 20);
		interactSection.checkInstructionText(interactSection.instructionText5, true);
		sleep(1);

		report.startStep("Send the 1st recording to the mic");
		audioService.sendSoundToVirtualMic(recording.getFiles().get(0), 88200);
		sleep(1);
				
		report.startStep("Wait for next recording");
		checkInteract1StatusAfterRecording(interactSection);
		
		report.startStep("Send the 2nd recording to the mic");
		interactSection.checkInstructionText(interactSection.instructionText5, true);
		audioService.sendSoundToVirtualMic(recording.getFiles().get(1), 88200);
		sleep(1);
					
		report.startStep("Wait for next recording");
		checkInteract1StatusAfterRecording(interactSection);
				
		report.startStep("Send the 3nd recording to the mic");
		interactSection.checkInstructionText(interactSection.instructionText5, true);
		audioService.sendSoundToVirtualMic(recording.getFiles().get(2), 88200);
		sleep(1);
		interactSection.waitForInstructionToEnd(interactSection.instructionText5);
		
		report.startStep("Wait for next recording");
		interactSection.checkInstructionText(interactSection.instructionText9, true);
		interactSection.checkIfInteract1StatusChanged(1,InteractStatus.speaker, 5);
				
		report.startStep("Click on See Feedback button");
		interactSection.waitForSeeFeedbackButtonToBecomeEnabled();
		interactSection.checkInstructionText(interactSection.instructionText10, true);
		interactSection.clickOnSeeFeedback();
		
		report.startStep("Click on Hear All button");
		interactSection.waitForHearAllButtomToBecomeEnabled();
		interactSection.clickOnListenToAllButton();
		
		report.startStep("Click on restart and select left speaker");
		interactSection.waitForRestartBtnIsEnabledOnFeedbackPage();
		interactSection.clickTheRestartButtonOnFeedbackPage();
		interactSection.checkThatStartButtonIsDisabled();
		interactSection.selectLeftSpeaker();
		interactSection.checkThatSpeakerTextIsHighlighted(1);
		interactSection.clickTheStartButton();
		sleep(3);
		
		report.startStep("Set progress to first FD course item");
		setProgressInFDToStudent();

	}
	
	@Test
	@TestCaseParams(testCaseID = { "36965" })
	public void testInteract1_JPG() throws Exception {
		
		startStep("Init test data");
		Recording recording = pageHelper.getRecordings().get(42);
		InteractSection interactSection = new InteractSection(webDriver,testResultService);
				
		report.startStep("Get user and login to ED");
		getUserAndLoginNewUXClass();
		sleep(1);
						
		report.startStep("Navigate to Interact 1 in A2-U1-L3-S3");
		learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "A2", 1, 3);
		learningArea2.clickOnStep(3);
		sleep(2);
		
		report.startStep("Select right speaker");
		interactSection.waitForSpeakersArrowsEnabledInInteract1();
		interactSection.checkInstructionText(interactSection.instructionText0, true);
		sleep(1);
		interactSection.hoverOnSpeaker(2);
		interactSection.checkThatSpeakerTextIsHighlighted(2);
		interactSection.selectRightSpeaker();
		sleep(1);
		interactSection.checkInstructionText(interactSection.instructionText1, true);
		sleep(1);

		report.startStep("Check if Start Button is enabled and click it");
		interactSection.clickTheStartButton();
		interactSection.checkIfInteract1StatusChanged(1, InteractStatus.speaker, 3);
		interactSection.checkInstructionText(interactSection.instructionText3, true);
		sleep(3);

		report.startStep("Wait for 3 seconds");
		interactSection.checkIfInteract1StatusChanged(2, InteractStatus.counter, 3);
		interactSection.checkInstructionText(interactSection.instructionText4, true);
		sleep(3);

		interactSection.checkIfInteract1StatusChanged(2, InteractStatus.recorder, 20);
		interactSection.checkInstructionText(interactSection.instructionText5, true);
		sleep(1);

		report.startStep("Send the 1st recording to the mic");
		audioService.sendSoundToVirtualMic(recording.getFiles().get(0), 88200);
				
		report.startStep("Wait for next recording");
		checkInteract1StatusAfterRecording(interactSection);
			
		report.startStep("Send the 2nd recording to the mic");
		interactSection.checkInstructionText(interactSection.instructionText5, true);
		audioService.sendSoundToVirtualMic(recording.getFiles().get(1), 88200);
		sleep(1);
					
		report.startStep("Wait for next recording");
		checkInteract1StatusAfterRecording(interactSection);
				
		report.startStep("Send the 3nd recording to the mic");
		interactSection.checkInstructionText(interactSection.instructionText5, true);
		audioService.sendSoundToVirtualMic(recording.getFiles().get(2), 88200);
		sleep(1);
		interactSection.waitForInstructionToEnd(interactSection.instructionText5);
						
		report.startStep("Click on See Feedback button");
		interactSection.waitForSeeFeedbackButtonToBecomeEnabled();
		interactSection.checkInstructionText(interactSection.instructionText10, true);
		interactSection.clickOnSeeFeedback();
		
		report.startStep("Click on Hear All button");
		interactSection.waitForHearAllButtomToBecomeEnabled();
		interactSection.clickOnListenToAllButton();
		
		report.startStep("Click on restart and select left speaker");
		interactSection.waitForRestartBtnIsEnabledOnFeedbackPage();
		interactSection.clickTheRestartButtonOnFeedbackPage();
		interactSection.checkThatStartButtonIsDisabled();
		interactSection.selectLeftSpeaker();
		interactSection.checkThatSpeakerTextIsHighlighted(1);
		interactSection.clickTheStartButton();
		sleep(2);
		
		report.startStep("Set progress to first FD course item");
		setProgressInFDToStudent();

	}
	
	@Test
	@TestCaseParams(testCaseID = { "24238", "24239" })
	public void testInteract2_FD() throws Exception {
		
		startStep("Init test data");
		Recording recording = pageHelper.getRecordings().get(17);
		InteractSection interactSection = new InteractSection(webDriver,testResultService);
		
		report.startStep("Get user and login");
		getUserAndLoginNewUXClass();
		
		report.startStep("Navigate to Interact 2 in FD-U3-L2");
		learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "FD", 3, 2);
		learningArea2.clickOnStep(5);
						
		report.startStep("Click on Start button");
		interactSection.waitForStartBtnIsEnabledInPracticeMCQ();
		interactSection.checkInstructionText(interactSection.instructionText1, true);
		sleep(2);
		
		interactSection.clickInteract2StartButtn();
		sleep(1);
		
		interactSection.checkInstructionText(interactSection.instructionText20, true);
		
		report.startStep("Check that Speaker's text is highlighted");
		interactSection.checkThatSpeakerTextIsHighlighted();
				
		report.startStep("Check for 1st counter and recorder");
		interactSection.checkifInteract2StatusChanged(InteractStatus.counter, 10);
		interactSection.checkInstructionText(interactSection.instructionText4, true);
		interactSection.checkifInteract2StatusChanged(InteractStatus.recorder, 5);
		interactSection.checkInstructionText(interactSection.instructionText5_1, true);

		report.startStep("Send Sound and check status & instruction change");
		audioService.sendSoundToVirtualMic(recording.getFiles().get(0), 88200);
		interactSection.checkifInteract2StatusChanged(InteractStatus.counter, 5);
		interactSection.checkInstructionText(interactSection.instructionText9, true);
		sleep(2);
		
		report.startStep("Check that Speaker's text is highlighted");
		interactSection.checkThatSpeakerTextIsHighlighted();
				
		report.startStep("Check for 2nd counter and recorder");
		interactSection.checkifInteract2StatusChanged(InteractStatus.recorder,10);
		
		report.startStep("Send Sound and check status & instruction change");
		interactSection.checkInstructionText(interactSection.instructionText5_1, true);
		audioService.sendSoundToVirtualMic(recording.getFiles().get(1), 88200);
		webDriver.printScreen();
		
		report.startStep("Wait untill playback ends and click See Feedback");
		sleep(5);
		interactSection.checkInstructionText(interactSection.instructionText10, true);
		interactSection.clickOnSeeFeedback();

		report.startStep("Click Hear All btn");
		interactSection.clickOnListenToAllButton();
		
		report.startStep("Opening Recored Panel To Improve Recording");
		interactSection.waitForRestartBtnIsEnabledOnFeedbackPage();
		RecordPanel recordPanel = interactSection.clickOnRepairButton(2);
		sleep(2);
		
		report.startStep("Add new recording to improve current recording");
		switchToRecordPanelAndClickRecord(recordPanel);
		String status = recordPanel.getRecordPanelStatus();
		testResultService.assertEquals("SPEAK", status);
		audioService.sendSoundToVirtualMic(recording.getFiles().get(0), 88200);

		startStep("Check that recording ended and press Select button");
		sleep(2);
		recordPanel.waitForRecordingToEnd(1);
		sleep(3);
		recordPanel.clickOnSelectRecordButton();
		sleep(3);
		
		report.startStep("Click on restart and check Start btn status");
		interactSection.clickTheRestartButtonOnFeedbackPage();
		interactSection.checkThatStartButtonIsDisabled();
			
		report.startStep("Set progress to first FD course item");
		setProgressInFDToStudent();
		
	}
	
	@Test
	@Category(reg2.class)
	@TestCaseParams(testCaseID = { "38881" })
	public void testInteract2_MP4() throws Exception {
		
		startStep("Init test data");
		Recording recording = pageHelper.getRecordings().get(40);
		InteractSection interactSection = new InteractSection(webDriver,testResultService);
		
		report.startStep("Get user and login");
		getUserAndLoginNewUXClass();
		sleep(2);
			
		report.startStep("Navigate to Interact 2 in B1-U9-L4");
		learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "B1", 9, 4);
		learningArea2.clickOnStep(6);
		sleep(2);
								
		report.startStep("Click on Start button");
		interactSection.waitForStartBtnIsEnabledInPracticeMCQ();
		interactSection.checkInstructionText(interactSection.instructionText1, true);
		sleep(2);
		
		interactSection.clickInteract2StartButtn();
		sleep(1);
		
		interactSection.checkInstructionText(interactSection.instructionText20, true);
		
		report.startStep("Check that Speaker's text is highlighted");
		interactSection.checkThatSpeakerTextIsHighlighted();
				
		report.startStep("Check for 1st page counter and recorder");
		checkInteract2StatusBeforeRecording(interactSection);

		report.startStep("Send Sound and check status & instruction change");
		audioService.sendSoundToVirtualMic(recording.getFiles().get(0), 88200);
		checkInteract2StatusAfterRecording(interactSection);
		sleep(3);
		
		report.startStep("Check that Speaker's text is highlighted");
		interactSection.checkThatSpeakerTextIsHighlighted();
				
		report.startStep("Check for 2nd page counter and recorder");
		checkInteract2StatusBeforeRecording(interactSection);
				
		report.startStep("Send Sound and check status & instruction change");
		audioService.sendSoundToVirtualMic(recording.getFiles().get(1), 88200);
		checkInteract2StatusAfterRecording(interactSection);
		sleep(3);
		
		report.startStep("Check that Speaker's text is highlighted");
		interactSection.checkThatSpeakerTextIsHighlighted();
				
		report.startStep("Check for 3rd page counter and recorder");
		checkInteract2StatusBeforeRecording(interactSection);
				
		report.startStep("Send Sound and check status & instruction change");
		audioService.sendSoundToVirtualMic(recording.getFiles().get(2), 88200);
		checkInteract2StatusAfterRecording(interactSection);
		sleep(3);
		
		report.startStep("Check that Speaker's text is highlighted");
		interactSection.checkThatSpeakerTextIsHighlighted();
		
		report.startStep("Wait untill playback ends and click See Feedback");
		interactSection.waitForSeeFeedbackButtonToBecomeEnabled();
		sleep(2);
		interactSection.checkInstructionText(interactSection.instructionText10, true);
		interactSection.clickOnSeeFeedback();
		sleep(1);
		
		report.startStep("Click Hear All btn");
		interactSection.clickOnListenToAllButton();
		sleep(1);
				
		report.startStep("Opening Recored Panel To Improve Recording");
		interactSection.waitForRestartBtnIsEnabledOnFeedbackPage();
		RecordPanel recordPanel = interactSection.clickOnRepairButton(2);
		sleep(2);
		
		report.startStep("Add new recording to improve current recording");
		switchToRecordPanelAndClickRecord(recordPanel);
		String status = recordPanel.getRecordPanelStatus();
		testResultService.assertEquals("SPEAK", status);
		audioService.sendSoundToVirtualMic(recording.getFiles().get(0), 88200);

		startStep("Check that recording ended and press Select button");
		sleep(3);
		recordPanel.waitForRecordingToEnd(1);
		sleep(3);
		recordPanel.clickOnSelectRecordButton();
		sleep(3);
		
		report.startStep("Click on restart and check Start btn status");
		interactSection.clickTheRestartButtonOnFeedbackPage();
		interactSection.checkThatStartButtonIsDisabled();
				
		report.startStep("Set progress to first FD course item");
		setProgressInFDToStudent();
		
	}
	
	@Test
	@TestCaseParams(testCaseID = { "36936" })
	public void testInteract2_JPG() throws Exception {
		
		startStep("Init test data");
		int i = 1;
		Recording recording = pageHelper.getRecordings().get(41);
		InteractSection interactSection = new InteractSection(webDriver,testResultService);
		
		report.startStep("Get user and login");
		getUserAndLoginNewUXClass();
		sleep(2);
		
		report.startStep("Navigate to Interact 2 in A2-U1-L3");
		learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "A2", 1, 3);
		learningArea2.clickOnStep(4);
		sleep(2);
				
		report.startStep("Click on Start button");
		interactSection.waitForStartBtnIsEnabledInPracticeMCQ();
		interactSection.checkInstructionText(interactSection.instructionText1, true);
		sleep(2);
		
		interactSection.clickInteract2StartButtn();
		sleep(1);
		
		interactSection.checkInstructionText(interactSection.instructionText20, true);
		
		report.startStep("Check that Speaker's text is highlighted");
		interactSection.checkThatSpeakerTextIsHighlighted();
				
		report.startStep("Check for 1st page counter and recorder");
		checkInteract2StatusBeforeRecording(interactSection);

		do {
			report.startStep("Send Sound and check status & instruction change for the " + i + " time");
				audioService.sendSoundToVirtualMic(recording.getFiles().get(0), 88200);
				checkInteract2StatusAfterRecording(interactSection);
				i++;
		} while (!virtualMicSuccess);
		sleep(3);
		
		i = 1;
		report.startStep("Check that Speaker's text is highlighted");
		interactSection.checkThatSpeakerTextIsHighlighted();
				
		report.startStep("Check for 2nd page counter and recorder");
		checkInteract2StatusBeforeRecording(interactSection);
		
		do {
			report.startStep("Send Sound and check status & instruction change for the " + i + " time");
				audioService.sendSoundToVirtualMic(recording.getFiles().get(1), 88200);
				checkInteract2StatusAfterRecording(interactSection);
				i++;
		} while (!virtualMicSuccess);
		sleep(3);
		
		i = 1;
		report.startStep("Check that Speaker's text is highlighted");
		interactSection.checkThatSpeakerTextIsHighlighted();
				
		report.startStep("Check for 3rd page counter and recorder");
		checkInteract2StatusBeforeRecording(interactSection);
		
		do {
			report.startStep("Send Sound and check status & instruction change for the " + i + " time");
				audioService.sendSoundToVirtualMic(recording.getFiles().get(2), 88200);
				sleep(4);
				virtualMicSuccess = interactSection.checkInstructionTextWithReturn(interactSection.instructionText10, true);
				if (!virtualMicSuccess) {
					webDriver.waitForElement("//a[contains(@class,'button continue retry')]", ByTypes.xpath).click();
					sleep(3);
				}
				i++;
		} while (!virtualMicSuccess);
				
		report.startStep("Wait untill playback ends and click See Feedback");
		interactSection.waitForSeeFeedbackButtonToBecomeEnabled();
		interactSection.checkInstructionText(interactSection.instructionText10, true);
		interactSection.clickOnSeeFeedback();
		sleep(1);
		
		report.startStep("Click Hear All btn");
		interactSection.clickOnListenToAllButton();
		sleep(1);
				
		report.startStep("Opening Recored Panel To Improve Recording");
		interactSection.waitForRestartBtnIsEnabledOnFeedbackPage();
		RecordPanel recordPanel = interactSection.clickOnRepairButton(2);
		sleep(2);
		
		report.startStep("Add new recording to improve current recording");
		switchToRecordPanelAndClickRecord(recordPanel);
		String status = recordPanel.getRecordPanelStatus();
		testResultService.assertEquals("SPEAK", status);
		audioService.sendSoundToVirtualMic(recording.getFiles().get(0), 88200);

		startStep("Check that recording ended and press Select button");
		sleep(3);
		recordPanel.waitForRecordingToEnd(1);
		recordPanel.clickOnSelectRecordButton();
		sleep(3);
		
		report.startStep("Click on restart and check Start btn status");
		interactSection.clickTheRestartButtonOnFeedbackPage();
		interactSection.checkThatStartButtonIsDisabled();
				
		setProgressInFDToStudent();
		
	}
			
	@Test
	@TestCaseParams(testCaseID = { "37902" })
	public void testRecordYourselfPanelTalkingIdiomsNew() throws Exception {
				
			report.startStep("Init test data");
			String segment = "a couch potato";
			int recordingId = 34; // segment: a couch potato
			float sampleRate = 88200;
			Recording recording = pageHelper.getRecordings().get(recordingId);
			int expectedSentenceLevel = recording.getSL().get(0);
						
			report.startStep("Login to ED as student and navigate to Talking Idioms");
			getUserAndLoginNewUXClass();
			sleep(1);
			
			report.startStep("Open New Community page and press on Talking Idioms link");
			communityPage = homePage.openNewCommunityPage(false);
			idiomsPage = communityPage.openIdiomsPage();
			sleep(2);
						
			report.startStep("Click Record Yourself");
			idiomsPage.recordBtn.click();
						
			report.startStep("Verify original text and send recording audio");
//			sleep(10);
			RecordPanel recordPanel = new RecordPanel(webDriver, testResultService);
			sleep(10);
			switchToRecordPanelAndClickRecordInTalkinIdioms(recordPanel);
			sleep(2);
			
			String [] wordsToJoin = recordPanel.getSentenceText(textService);
			String originalText = "";
			
			for (int i = 0; i < wordsToJoin.length; i++) {
				originalText = originalText + wordsToJoin[i] + " ";
			}
			audioService.sendSoundToVirtualMic(recording.getFiles().get(0), sampleRate);
			
			testResultService.assertEquals(segment, originalText.trim(),"Text in record panel does not match to selected idiom");
					
			report.startStep("Check Recording SL feedback");
			recordPanel.waitForRecordingToEnd(1);
			recordPanel.checkSentenceScoreRatingText(expectedSentenceLevel);
			
			report.startStep("Close Record Panel");
			idiomsPage.closeRecordPanel();
			idiomsPage.verifyIdiomsHeader();
		}
		
	@Test
	@TestCaseParams(testCaseID = { "31800", "23650" })
	public void testRecoredYourselfPanelPronunciation()
				throws Exception {
			
			RecordPanel recordPanel=new RecordPanel(webDriver, testResultService);
			
			report.startStep("Init test data");
			int recordingId = 35; // segment: Hi
			float sampleRate = 88200;
			Recording recording = pageHelper.getRecordings().get(recordingId);
			
			report.startStep("Login to ED as student and navigate to Pronunciation");
			getUserAndLoginNewUXClass();
			sleep(2);
			
			report.startStep("Navigate to Explore in FD-U2-L2");
			learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "FD", 2, 2);
			learningArea2.clickOnStep(2);
			sleep(2);
			
			report.startStep("Press on See Text and press on Pronunciation btn");
			learningArea2.clickOnSeeText();
			learningArea2.clickOnPronunciation();
			sleep(2);
			
			report.startStep("Send Recording");
			switchToRecordPanelAndClickRecord(recordPanel);
			sleep(2);
			audioService.sendSoundToVirtualMic(recording.getFiles().get(0), sampleRate);
			sleep(3);
			
			report.startStep("Select next segment and check it placed for recording");
			recordPanel.checkNextPronunciationSegmentSelection(2);
						
			report.startStep("Check that recording ended");
			recordPanel.waitForRecordingToEnd(1);
			webDriver.printScreen("After recording ended");
			
			report.startStep("Set progress to first FD course item");
			setProgressInFDToStudent();
			
		}
		
	@Test
	@TestCaseParams(testCaseID = { "24220","24227" })
	public void testRecordMoreThenEightTimes() throws Exception {

			report.startStep("Init test data");
			int failCounter = 0;
			boolean found;
			Recording recording = pageHelper.getRecordings().get(5);
			float sampleRate = 88200;
			String[] words = new String[] { "Hi", "I'm", "Tom", "Smith" };
			int numOfRecordingsInTest = 8;
			List<String[]> wordsScoreList = new ArrayList<String[]>();
			List<Integer> sentenceLevels = new ArrayList<Integer>();
		
			report.startStep("Login to ED as student and navigate to FD-U2-L2");
			getUserAndLoginNewUXClass();
			learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "FD", 2, 2);
			sleep(2);
			
			report.startStep("Open Recored Yourself Panel");
			learningArea2.clickOnStep(2);
			learningArea2.clickOnSeeText();
			learningArea2.selectTextForRecord(1);
			learningArea2.clickOnRecordYourselfSegmentTool();
			sleep(3);

			report.startStep("Send recording 8 times");
			sleep(2);
			learningArea2.switchToRecordPanel();
			RecordPanel recordPanel=new RecordPanel(webDriver, testResultService);

			for (int i = 0; i < numOfRecordingsInTest; i++) {
				sleep(2);
				recordPanel.clickOnRecordButton();
				sleep(2);
				
				String status = recordPanel.getRecordPanelStatus();
				testResultService.assertEquals("SPEAK", status);
				report.startStep("Sending recording for: "+ (i+1) +" time");
				audioService.sendSoundToVirtualMic(recording.getFiles().get(0),sampleRate);

				report.startStep("Check that recording ended");
				sleep(2);
				recordPanel.waitForRecordingToEnd(1);
				sleep(4);
				
				webDriver.printScreen("After recording ended_" + i);
				
				if (isSRDebugTrue()) {
					String[] wordsScoring = textService
							.splitStringToArray(recordPanel.getWordsScoring("wl"));
					wordsScoreList.add(wordsScoring);
					int sentenceLevel = recordPanel.getDebugSentenceLevel();
					sentenceLevels.add(sentenceLevel);
					recordPanel.checkAddedRecordingToList(sentenceLevel, i);
					startStep("Check SL according to word levels");
					pageHelper.calculateSLbyWL(wordsScoring,
							String.valueOf(sentenceLevel));
				}
				// OMZ 7.4.2019 if record wasn't sent correctly, try again instead of failing test
				try {
					webDriver.findElementByXpath("//div[@class='scoreExpWrapper'][text()='Your recording cannot be processed. Please try again.']", ByTypes.xpath);
					found = true;
				} catch (Exception e) {
					found = false;
				}
				if(found) {
					if (failCounter < 5) {
						i--;
						failCounter ++;
					}
				}
				// OMZ 7.4.2019 End
			}
			
			report.startStep("Try to add the 9th recording and check message window");
			recordPanel.clickOnRecordButton();
			recordPanel.checkSendStatusMessage();
			recordPanel.clickOnSendStatusCancelButton();
			recordPanel.clickOnRecordButton();
			recordPanel.clickOnSendStatusRecordButton();
			sleep(1);
			
			report.startStep("Record one more time");
			String status = recordPanel.getRecordPanelStatus();
			testResultService.assertEquals("SPEAK", status);
			audioService.sendSoundToVirtualMic(recording.getFiles().get(0),sampleRate);

			report.startStep("Check that recording ended");
			recordPanel.waitForRecordingToEnd(1);
			sleep(4);
			String[] wordsScoring = null;
			if (isSRDebugTrue()) {
				wordsScoreList.remove(0);
				wordsScoring = textService.splitStringToArray(recordPanel
						.getWordsScoring("wl"));

			}

			if (isSRDebugTrue()) {
				textService.printStringArray(wordsScoring);
				wordsScoreList.add(wordsScoring);
				int sentenceLevel = recordPanel.getDebugSentenceLevel();
				sentenceLevels.remove(0);
				sentenceLevels.add(sentenceLevel);
			}
		
			sleep(3);
			report.startStep("Select all recordings one by one");
			sleep(5);
			for (int i = 0; i < numOfRecordingsInTest; i++) {

				int index = i + 1;
				recordPanel.selectRecording(String.valueOf(index));
				Thread.sleep(2000);
				if (isSRDebugTrue()) {
					textService.printStringArray(wordsScoreList.get(i));
					recordPanel.checkWordsLevels(words, wordsScoreList.get(i),
							textService);
					recordPanel.checkSentenceScoreRatingText(sentenceLevels.get(i));

				}
				
			}
			
			report.startStep("Set progress to first FD course item");
			studentService.createSingleProgressRecored(studentId,"20000", "31515", 10, false, 1);

		}
			
	@Test
	@TestCaseParams(testCaseID = { "24266","33883","14636" })
	public void testMultipleChoiceSRPractice() throws Exception {
			
			report.startStep("Init test data");
			String wrongAnswer = "A double room, please.";
			String correctAnswer = "I don't know.";
			InteractSection interactSection = new InteractSection(webDriver,testResultService);
			
			int recordingIdCorrect = 36; // segment: I don't know.
			int recordingIdWrong = 37; // segment: A double room, please.
			float sampleRate = 44100;
			Recording recordingCorrect = pageHelper.getRecordings().get(recordingIdCorrect);
			Recording recordingWrong = pageHelper.getRecordings().get(recordingIdWrong);
						
			report.startStep("Login to ED as student and navigate to SR MCQ");
			getUserAndLoginNewUXClass();
			sleep(2);
			learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "B1", 1, 3);
			sleep(2);
			learningArea2.clickOnStep(2);
			sleep(1);
			learningArea2.clickOnTaskByNumber(5);
			
			report.startStep("Wait till Start btn enabled and check UI task instruction");
			interactSection.waitForStartBtnIsEnabledInPracticeMCQ();
			sleep(1);
			interactSection.checkInstructionText(interactSection.instructionText22, false);
			learningArea2.VerificationOfQuestionInstruction(interactSection.taskInstructionText1);
			sleep(1);
			
			report.startStep("Press on Start btn and check task instruction & record MIC icon highlighted");
			interactSection.clickTheStartButton();
			sleep(1);
			interactSection.checkMCQPracticeStatusChanged(InteractStatus.recorder, 2);
			interactSection.checkInstructionText(interactSection.instructionText5, false);
			learningArea2.VerificationOfQuestionInstruction(interactSection.taskInstructionText1);
			sleep(2);
			
			report.startStep("Don't send any record and check try again");
			interactSection.waitForRetryBtndInPracticeMCQ();
			interactSection.checkInstructionText(interactSection.instructionText6_1, false);
			interactSection.clickTheTryAgainButton();
			sleep(2);
			
			report.startStep("Send wrong recording and check the recorded sentence highlighted");
			audioService.sendSoundToVirtualMic(recordingWrong.getFiles().get(0), sampleRate);
			interactSection.checkRecordedAnswerIsHighlighted(wrongAnswer);
			
			report.startStep("Check MIC status changed to speaker and instruction changed");
			interactSection.checkMCQPracticeStatusChanged(InteractStatus.speaker, 3);
			interactSection.checkInstructionText(interactSection.instructionText22, false);
			learningArea2.VerificationOfQuestionInstruction(interactSection.taskInstructionText1);
			
			report.startStep("Click on Check Answer and check X sign placed correctly");
			learningArea2.clickOnCheckAnswer();
			sleep(2);
			learningArea2.verifySRAnswerSelectedWrong(1,3);
			
			report.report("Verify Practice Tools State and press Clear tool");
			learningArea2.verifyPracticeToolsStateOnCheckAnswer();
			learningArea2.clickOnClearAnswer();
			sleep(2);
			
			report.startStep("Press on Start btn and check task instruction & record MIC icon highlighted");
			interactSection.clickTheStartButton();
			sleep(1);
			interactSection.checkMCQPracticeStatusChanged(InteractStatus.recorder, 1);
			interactSection.checkInstructionText(interactSection.instructionText5, false);
			learningArea2.VerificationOfQuestionInstruction(interactSection.taskInstructionText1);
			sleep(1);
			
			report.startStep("Send correct recording and check the recorded sentence highlighted");
			audioService.sendSoundToVirtualMic(recordingCorrect.getFiles().get(0), sampleRate);
			interactSection.checkRecordedAnswerIsHighlighted(correctAnswer);
			
			report.startStep("Check MIC status changed to speaker and instruction changed");
			interactSection.checkMCQPracticeStatusChanged(InteractStatus.speaker, 3);
			interactSection.checkInstructionText(interactSection.instructionText22, false);
			learningArea2.VerificationOfQuestionInstruction(interactSection.taskInstructionText1);
			
			report.startStep("Click on Check Answer and check V sign placed correctly");
			learningArea2.clickOnCheckAnswer();
			sleep(2);
			learningArea2.verifySRAnswerSelectedCorrect(1,1);
			
			report.report("Verify Practice Tools State and press Clear tool");
			learningArea2.verifyPracticeToolsStateOnCheckAnswer();
			learningArea2.clickOnClearAnswer();
			sleep(2);
			
			report.startStep("Set progress to first FD course item");
			setProgressInFDToStudent();
			
		}
		
	@Test
	@TestCaseParams(testCaseID = { "34332","34438" })
	public void testOpenSpeechPracticeED() throws Exception {
			
			report.startStep("Init test data");
			InteractSection interactSection = new InteractSection(webDriver,testResultService);
			
			int recordingIdCorrect = 7; // segment: What do you do Sarah
			float sampleRate = 88200;
			Recording recordingCorrect = pageHelper.getRecordings().get(recordingIdCorrect);
									
			report.startStep("Create and login to ED as student and navigate to Open Speech task");
			studentId = pageHelper.createUSerUsingSP(instID, classNameSR);
			userNameSR = dbService.getUserNameById(studentId, instID);
						
			loginPage = new NewUXLoginPage(webDriver,testResultService);
			homePage = loginPage.loginAsStudent(userNameSR, "12345");
			sleep(2);						
			learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "B3", 9, 2);
			sleep(2);
			learningArea2.clickOnStep(5);
						
			report.startStep("Wait till Start btn enabled and check UI task instruction");
			interactSection.waitForStartBtnIsEnabledInPracticeOpenSpeech();
			interactSection.checkOpenSpeechPanelInstruction(interactSection.openSpeechPanelInst0);
						
			report.startStep("Verify no practice tools displayed");
			learningArea2.checkThatCheckAnswerToolIsNotDisplayed();
			learningArea2.checkThatSeeAnswerToolIsNotDisplayed();
			learningArea2.checkThatClearToolIsNotDisplayed();
			
			report.startStep("Press on Start btn");
			interactSection.clickOpenSpeechStartButton();
						
			report.startStep("Check status changed and stage 1 - Prepare displayed");
			interactSection.checkOpenSpeechPracticeStatusChanged(InteractStatus.prepare, 1);
			interactSection.checkOpenSpeechPanelStageView(1);
			sleep(1);
			
			report.startStep("Click on Skip btn");
			interactSection.clickOpenSpeechSkipButton();
			sleep(1);
			
			report.startStep("Check Resource Tools disabled");
			learningArea2.checkThatSeeTextIsDisabled();
		
			report.startStep("Check status changed and stage 2 - Record displayed");
			interactSection.checkOpenSpeechPracticeStatusChanged(InteractStatus.record, 1);
			interactSection.checkOpenSpeechPanelStageView(2);
			sleep(1);
			
			report.startStep("Send recording");
			audioService.sendSoundToVirtualMic(recordingCorrect.getFiles().get(0), sampleRate);
			sleep(1);
			
			report.startStep("Click on Stop btn");
			interactSection.clickOpenSpeechStopButton();
			sleep(1);
			
			report.startStep("Click on Hear btn");
			interactSection.clickOpenSpeechHearButton();
			sleep(1);
			
			report.startStep("Click on Retry btn");
			interactSection.clickOpenSpeechRetryButton();
			sleep(1);
			
			report.startStep("Verify alert message");
			learningArea2.validateConfirmModalByMessage(interactSection.openSpeechPanelAlert1, true);
			
			report.startStep("Check Resource Tools disabled");
			learningArea2.checkThatSeeTextIsDisabled();
						
			report.startStep("Check status changed and stage 2 - Record displayed");
			interactSection.checkOpenSpeechPracticeStatusChanged(InteractStatus.record, 1);
			interactSection.checkOpenSpeechPanelStageView(2);
			sleep(1);
			
			report.startStep("Send recording again");
			audioService.sendSoundToVirtualMic(recordingCorrect.getFiles().get(0), sampleRate);
			sleep(1);
			
			report.startStep("Click on Stop btn");
			interactSection.clickOpenSpeechStopButton();
			sleep(1);
			
			report.startStep("Check status changed and stage 3 - Send displayed");
			interactSection.checkOpenSpeechPracticeStatusChanged(InteractStatus.send, 1);
			interactSection.checkOpenSpeechPanelStageView(3);
			sleep(1);
					
			report.startStep("Click on Send to teacher btn");
			interactSection.clickOpenSpeechSendToTeacherButton();
			sleep(1);
			
			report.startStep("Verify alert message");
			learningArea2.validateAlertModalByMessage(interactSection.openSpeechPanelAlert2, true);
			
			report.startStep("Wait 2 min and open Assignments page");
		//--igb 2018.06.24-----------
		//	sleep(120);
			chkUserRecMp3(studentId, 120);
		//--igb 2018.06.24-----------
			
			homePage.clickToOpenNavigationBar();
			assignPage = learningArea2.openAssignmentsPage(false);
			sleep(3);
			
			report.startStep("Select recording");
			assignPage.selectRecordingByCourseUnitAndLessonNameAndGetScore("Basic 3", "Saving Energy", "How to Save Energy" , 1);
			sleep(1);
			
			report.startStep("Press on play btn and check media loaded");
			assignPage.clickOnPlay();
			pageHelper.checkAudioFileLoadedByFolderName("MediaPlayer", studentId);
			
			report.startStep("Close Assignment page and log out");
			assignPage.close();
			sleep(1);
			homePage.clickToOpenNavigationBar();
			sleep(1);
			learningArea2.clickOnLogoutLearningArea();
			testResultService.assertEquals(false, loginPage.isSubmitButtonEnabled(), "Checking that ED Login Page displayed");
			
			deleteRecordOnTestEnd = false;
	
	}
	
	@Test
	@TestCaseParams(testCaseID = { "34332","34438" })
	public void testOpenSpeechPracticeTMS() throws Exception { // THIS TEST IS NOT FULLY INDEPENDENT - BEST RUN AFTER PREVIOUS TEST AS JAVA CLASS EXECUTION
			
			report.startStep("Init test data");
				String unitId = "44297";			 
				String compId = "44299";
				
			report.startStep("Login as Teacher");
			loginPage = new NewUXLoginPage(webDriver,testResultService);
			tmsHomePage = loginPage.enterTeacherUserAndPassword();
			sleep(5);
			tmsHomePage.switchToMainFrame();
		
			report.startStep("Open Student Recordings page");
			tmsHomePage.clickOnReports();
			sleep(2);
			tmsHomePage.clickOnCourseReports();
			sleep(2);
			tmsHomePage.switchToFormFrame();
			tmsHomePage.selectReport("11");
			sleep(1);
			tmsHomePage.clickOnSearch();
		
			if (userNameSR == null) userNameSR = defaultUserNameSR;
						
			tmsHomePage.findStudentInSearchSection(classNameSR, userNameSR);
			sleep(2);
			tmsRecPage = tmsHomePage.openStudentRecordPageByPressOnGo();
			sleep(2);
			webDriver.switchToNewWindow();
			sleep(2);
							
			report.startStep("Select recording in TMS");
			String courseName = "Basic 3";
			String unitName = "Saving Energy";
			String lessonName = "How to Save Energy";
			
			tmsRecPage.selectFirstRecordingByCourseUnitLesson(courseName, unitName, lessonName);
			
			report.startStep("Verify main labels");
			tmsRecPage.verifyMainTitle();
			tmsRecPage.verifyMainInstruction();
			tmsRecPage.verifyScoreCategoriesOpenSpeech();
		
			report.startStep("Verify OS title in original question area");
			tmsRecPage.verifyOpenSpeechOriginalQuestionTitle();
		
			report.startStep("Score recording, save and get final score");
			tmsRecPage.scorePhonemes(5);
			tmsRecPage.scoreIntonationAndStress(4);
			tmsRecPage.scoreOverallPronunciation(3);
			tmsRecPage.pressSaveScore();
			String teacherScore = tmsRecPage.getFinalScore();
		
			report.startStep("Close TMS recordings page and logout");
			webDriver.closeNewTab(1);
			webDriver.switchToMainWindow();
			tmsHomePage.switchToMainFrame();
			tmsHomePage.clickOnExit();
			testResultService.assertEquals(false, loginPage.isSubmitButtonEnabled(), "Checking that ED Login Page displayed");
		
			report.startStep("Login as student and check recording score displayed in Assignment page");
			homePage = loginPage.loginAsStudent(userNameSR, "12345");
			studentId = dbService.getUserIdByUserName(userNameSR, instID);
			sleep(3);
			assignPage = homePage.openAssignmentsPage(false);
			String studentScore = assignPage.selectRecordingByCourseUnitAndLessonNameAndGetScore("Basic 3", "Saving Energy", "How to Save Energy" , 1);
			testResultService.assertEquals(teacherScore, studentScore, "Teacher score does not match to student score");
		
			if(userNameSR == defaultUserNameSR) {
				report.startStep("Remove score from student Assignment");
					String userId = dbService.getUserIdByUserName(defaultUserNameSR,configuration.getProperty("institution.id"));
					dbService.deleteStudentRecordScore(userId,unitId,compId);
			}
	}
	
	@Test
	@TestCaseParams(testCaseID = {"39204"})
	public void testStudentRecordingsPageED() throws Exception {

		
		report.startStep("Init test data");
		String courseName = coursesNames[2];
		String unitId = "43943";
		String unitName = dbService.getUnitNameById(unitId); 
		String compId = "43956";
		String compName = dbService.getComponentNameById(compId);
		String subCompId = "1";
		String segmentId = "5";
		String resName = "b2loaa02";
		String autoScore = "91";
		String segmentText = "Does the rent include utilities?";
		
		report.startStep("Create and login to ED as student");
		studentId = pageHelper.createUSerUsingSP(instID, classNameSR);
		userNameSR = dbService.getUserNameById(studentId, instID);
		loginPage = new NewUXLoginPage(webDriver,testResultService);
		homePage = loginPage.loginAsStudent(userNameSR, "12345");
		homePage.waitHomePageloaded();				
		
		report.startStep("Register student recording in DB");
		//simulate recording sending to teacher & saving as old SR - IE method with wav to mp3 convert
		String recId = pageHelper.sendRecordingToTeacherSR(studentId, unitId, compId, subCompId, segmentId, autoScore,resName);
				
		report.startStep("Create / check if user folder exists in Recordings");
		textService.getCreateFolderInPath(RECORDINGS_FOLDER, studentId);
		sleep(1);
		
		report.startStep("Copy and rename the wav recording to user folder");
		String source = "smb://" + configuration.getGlobalProperties("logserverName") + "//AutoLogs//ToolsAndResources//testFiles//" + RECORDING_WAV;
		String destination = RECORDINGS_FOLDER + "//"+ studentId + "//"+ recId+ ".wav";
		textService.copyFileToFolder(source, destination, true, netService.getDomainAuth(), netService.getDomainAuth());
		sleep(1);
						
		report.startStep("Update student recording status in DB and check it converted");
		Boolean isConverted = pageHelper.updateSaveRecordingAndCheckConvert(recId, studentId);
		if (!isConverted) {
			testResultService.addFailTest("Recording not saved or not converted", true, false);
		}
		
		report.startStep("Open Assignments page");
		assignPage = homePage.openAssignmentsPage(false);
		sleep(3);
		
		report.startStep("Select recording");
		assignPage.selectRecordingByCourseUnitAndLessonNameAndGetScore(courseName, unitName, compName , 1);
		sleep(1);
		
		report.startStep("Press on play btn and check media loaded");
		assignPage.clickOnPlay();
		pageHelper.checkAudioFileLoadedByFolderName("MediaPlayer", studentId);
		
		report.startStep("Close Assignment page and log out");
		assignPage.close();
		sleep(2);
		homePage.clickOnLogOut();
		sleep(3);
		testResultService.assertEquals(false, loginPage.isSubmitButtonEnabled(), "Checking that ED Login Page displayed");
						
		deleteRecordOnTestEnd = false;
	}
	
	@Test
	@TestCaseParams(testCaseID = {"39204"})
	public void testStudentRecordingsPageTMS() throws Exception { // THIS TEST IS NOT FULLY INDEPENDENT - BEST RUN AFTER PREVIOUS TEST AS JAVA CLASS EXECUTION
		
		report.startStep("Init test data");
		String courseName = coursesNames[2];
		String unitId = "43943";
		String unitName = dbService.getUnitNameById(unitId); 
		String compId = "43956";
		String compName = dbService.getComponentNameById(compId);
		String subCompId = "1";
		String segmentId = "5";
		String resName = "b2loaa02";
		String autoScore = "91";
		String segmentText = "Does the rent include utilities?";
		//String className =configuration.getProperty("classname.openSpeech");
				
		report.startStep("Login as Teacher");		
		loginPage = new NewUXLoginPage(webDriver, testResultService);
		pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(configuration.getProperty("teacher.username"),configuration.getProperty("institution.id")));
		tmsHomePage = loginPage.enterTeacherUserAndPassword();
		tmsHomePage.switchToMainFrame();
				
		try
		{
			report.startStep("Open Student Recordings page");
			tmsHomePage.clickOnReports();
			sleep(2);
			tmsHomePage.clickOnCourseReports();
			sleep(2);
			tmsHomePage.switchToFormFrame();
			tmsHomePage.selectReport("11");
			sleep(1);
			
			/*tmsHomePage.clickOnSearch();
	
			if (userNameSR == null) {
				userNameSR = defaultUserNameSR;
				autoScore = "83";
			}
			
			tmsHomePage.findStudentInSearchSection(classNameSR, userNameSR);
			sleep(2);
			tmsRecPage = tmsHomePage.openStudentRecordPageByPressOnGo();
			sleep(5);*/
			
			report.startStep("Select Class and press GO");
			tmsHomePage.selectClass(classNameSR,false,true);
			
			report.startStep("Open first student's recording");
			TmsReportsPage tmsReportsPage = new TmsReportsPage(webDriver, testResultService);
			userNameSR = tmsReportsPage.openStudentRecordingInStudentsRecordingsReport(0);
			
			webDriver.printScreen("Before switching to new window");
			webDriver.switchToNewWindow();
			sleep(5);
		
			tmsRecPage = new StudentRecordingsPage(webDriver, testResultService);
			
			report.startStep("Verify main labels");
			tmsRecPage.verifyMainTitle();
			tmsRecPage.verifyMainInstruction();
			tmsRecPage.verifyOriginalRecTitleSR();
			tmsRecPage.verifyOriginalTextTitleSR();
			tmsRecPage.verifyAutoScoreTitleSR();
			tmsRecPage.verifyScoreCategoriesSR();
		
			report.startStep("Select recording in TMS");
			tmsRecPage.selectFirstRecordingByCourseUnitLesson(courseName, unitName, compName);
			sleep(2);
					
			report.startStep("Verify student and class details");
			String actStudentFirstLastName = tmsRecPage.getStudentFirstLastName();
			String expStudentFirstLastName = userNameSR;// +" "+userNameSR;
			testResultService.assertEquals(expStudentFirstLastName, actStudentFirstLastName, "Student first & last name not correct");
			testResultService.assertEquals(classNameSR, tmsRecPage.getClassName(), "Classname not correct");
			
			report.startStep("Verify Original Text appears in Right Panel");
			testResultService.assertEquals(segmentText, tmsRecPage.getOriginalSegmentText(), "Original Text does not match to expeceted segment");
			
			report.startStep("Verify auto score displayed");
			testResultService.assertEquals(autoScore, tmsRecPage.getAutomatedScore(), "Auto score does not match to expeceted value");
		
			report.startStep("Press on play student & original recording");
			tmsRecPage.playStudentRecBtn.click();
			sleep(2);
			tmsRecPage.stopStudentRecBtn.click();
			sleep(2);
			tmsRecPage.playOriginalRecBtn.click();
			sleep(2);
			tmsRecPage.stopOriginalRecBtn.click();
			sleep(2);
		
			report.startStep("Score recording, save and get final score");
			tmsRecPage.scorePhonemes(5);
			sleep(1);
			tmsRecPage.scoreIntonationAndStress(4);
			sleep(1);
			tmsRecPage.scoreOverallPronunciation(3);
			sleep(1);
			tmsRecPage.pressSaveScore();
			sleep(1);
			String teacherGrade = tmsRecPage.getFinalScore();
			
			report.startStep("Check the grade appears for recordings in left side");
			String actRecordGrade = tmsRecPage.getRecordScore();
			testResultService.assertEquals(teacherGrade, actRecordGrade, "Checking that Teacher's grade displayed in left side for recording");
			
			report.startStep("Close TMS recordings page and check grade in table");
			webDriver.closeNewTab(1);
			sleep(1);
			webDriver.switchToMainWindow();
			tmsHomePage.switchToMainFrame();
			tmsHomePage.switchToFormFrame();
			tmsHomePage.clickOnSearch();
			
			String userFirstName = userNameSR.split(" ")[0];
			
			tmsHomePage.findStudentInSearchSection(classNameSR, userFirstName);
			sleep(2);
			//tmsHomePage.clickOnGo();
			//sleep(4);
			webDriver.switchToMainWindow();
			tmsHomePage.switchToReviewRecordingsFrame();
			String gradeInTable = tmsHomePage.getStudentRecordGradeByName(userNameSR);
			testResultService.assertEquals(teacherGrade, gradeInTable, "Checking that Teacher's grade displayed in recordings table");
		
			report.startStep("Exit TMS");
			webDriver.switchToTopMostFrame();
			tmsHomePage.switchToMainFrame();
			tmsHomePage.clickOnExit();
			testResultService.assertEquals(false, loginPage.isSubmitButtonEnabled(), "Checking that ED Login Page displayed");
			
			report.startStep("Login as student and check final score displayed in Assignment page");
			homePage = loginPage.loginAsStudent(userFirstName, "12345");
			studentId = dbService.getUserIdByUserName(userFirstName, instID);
			sleep(3);
			homePage.closeModalPopUp();
			
			assignPage = homePage.openAssignmentsPage(false);
			String studentScore = assignPage.selectRecordingByCourseUnitAndLessonNameAndGetScore(courseName, unitName, compName , 1);
			testResultService.assertEquals(teacherGrade, studentScore, "Teacher score does not match to student score");
			
			if(userNameSR == defaultUserNameSR) {
				report.startStep("Remove score from student Assignment");
					String userId = dbService.getUserIdByUserName(defaultUserNameSR,configuration.getProperty("institution.id"));
					dbService.deleteStudentRecordScore(userId,unitId,compId);
			}
		}
		catch(Exception e)
		{
			deleteRecordOnTestEnd = false;
			testResultService.addFailTest("Exepction Encountered", false, true);
		}
	}
	
	@Test
	@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = {"39204"})
	public void testStudentRecordingsPageTMS_SPal() throws Exception {

		
		report.startStep("Init test data");
		// B1-U9-L3-S3
		int courseIndex = 1;
		int unitNum = 9;
		int lessonNum = 3;
		int stepNum = 3;
		int segmentNum = 3;
		int recordingId = 43; // It's OK. Our trainers can show you what to do.
		float sampleRate = 11025;
		String autoScore = "100";
		String segmentText = "It's OK. Our trainers can show you what to do.";
		String resName = "b2loaa02";		
		
		Recording recording = pageHelper.getRecordings().get(recordingId);
		int timeoutBeforeRecording = 2;
		
		String courseName = coursesNames[courseIndex];
		String courseId = courses[courseIndex];
		
		String unitId = dbService.getCourseUnits(courseId).get(unitNum-1);
		String unitName = dbService.getUnitNameById(unitId); 
		String compId = dbService.getComponentDetailsByUnitId(unitId).get(lessonNum-1)[1];
		String compName = dbService.getComponentNameById(compId);
							
		report.startStep("Create and login to ED as student");
		studentId = pageHelper.createUSerUsingSP(instID, classNameSR);
		userNameSR = dbService.getUserNameById(studentId, instID);
		loginPage = new NewUXLoginPage(webDriver,testResultService);
		homePage = loginPage.loginAsStudent(userNameSR, "12345");
		sleep(2);
						
		report.startStep("Navigate to B1-U9-L3-S3");
		learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, courseCodes[courseIndex], unitNum, lessonNum);
		sleep(2);
		learningArea2.clickOnStep(stepNum);
		sleep(2);
		
		report.startStep("Press on See Text, select segment and click on Recored Yourself");
		learningArea2.clickOnSeeText();
		sleep(2);
		learningArea2.selectTextForRecord(segmentNum);
		sleep(1);
		learningArea2.clickOnRecordYourselfSegmentTool();
		sleep(3);
		
		RecordPanel recordPanel = new RecordPanel(webDriver, testResultService);
		sleep(2);
		report.startStep("Switch to the record panel and start recording");
		switchToRecordPanelAndClickRecord(recordPanel);
		sleep(1);
		audioService.sendSoundToVirtualMic(recording.getFiles().get(0),	sampleRate);
		
		report.startStep("Check that recording ended");
		sleep(timeoutBeforeRecording);
		recordPanel.waitForRecordingToEnd(1);
		webDriver.printScreen("After recording ended");
		
		report.startStep("Select record and press Send To Teacher");
		recordPanel.selectRecording("1");
		recordPanel.clickOnSendToTeacher();
		sleep(2);
		recordPanel.checkSendToTeacherSuccsessText();
		recordPanel.clickOnSendStatusBtnOK();
		sleep(2);
		recordPanel.closeRecordPanel();
		sleep(1);
						
		report.startStep("Open Assignments page");
		homePage.clickToOpenNavigationBar();
		sleep(1);
		assignPage = homePage.openAssignmentsPage(false);
		sleep(3);
		
		report.startStep("Select recording");
		assignPage.selectRecordingByCourseUnitAndLessonNameAndGetScore(courseName, unitName, compName , 1);
		sleep(1);
		
		report.startStep("Press on play btn and check media loaded");
		assignPage.clickOnPlay();
		pageHelper.checkAudioFileLoadedByFolderName("MediaPlayer", studentId);
		
		report.startStep("Close Assignment page and log out");
		assignPage.close();
		sleep(1);
		homePage.clickToOpenNavigationBar();
		sleep(2);
		homePage.clickOnLogOut();
		sleep(3);
		testResultService.assertEquals(false, loginPage.isSubmitButtonEnabled(), "Checking that ED Login Page displayed");
						
		report.startStep("Login as Teacher");		
		loginPage = new NewUXLoginPage(webDriver, testResultService);
		pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(configuration.getProperty("teacher.username"),configuration.getProperty("institution.id")));
		tmsHomePage = loginPage.enterTeacherUserAndPassword();
		tmsHomePage.switchToMainFrame();
				
		report.startStep("Open Student Recordings page");
		tmsHomePage.clickOnReports();
		sleep(2);
		tmsHomePage.clickOnCourseReports();
		sleep(2);
		tmsHomePage.switchToFormFrame();
		tmsHomePage.selectReport("11");
		sleep(1);
		tmsHomePage.clickOnSearch();
	
		tmsHomePage.findStudentInSearchSection(classNameSR, userNameSR);
		sleep(2);
		tmsRecPage = tmsHomePage.openStudentRecordPageByPressOnGo();
		sleep(5);
		webDriver.switchToNewWindow();
		sleep(5);
	
		report.startStep("Verify main labels");
		tmsRecPage.verifyMainTitle();
		tmsRecPage.verifyMainInstruction();
		tmsRecPage.verifyOriginalRecTitleSR();
		tmsRecPage.verifyOriginalTextTitleSR();
		tmsRecPage.verifyAutoScoreTitleSR();
		tmsRecPage.verifyScoreCategoriesSR();
		
		report.startStep("Select recording in TMS");
		tmsRecPage.selectFirstRecording();
		
		report.startStep("Verify student and class details");
		String actStudentFirstLastName = tmsRecPage.getStudentFirstLastName();
		String expStudentFirstLastName = userNameSR +" "+userNameSR;
		testResultService.assertEquals(expStudentFirstLastName, actStudentFirstLastName, "Student first & last name not correct");
		testResultService.assertEquals(classNameSR, tmsRecPage.getClassName(), "Classname not correct");
		
		report.startStep("Verify Original Text appears in Right Panel");
		testResultService.assertEquals(segmentText, tmsRecPage.getOriginalSegmentText(), "Original Text does not match to expeceted segment");
		
		report.startStep("Verify auto score displayed");
		testResultService.assertEquals(autoScore, tmsRecPage.getAutomatedScore(), "Auto score does not match to expeceted value");
				
		report.startStep("Press on play student rec and check correct media loaded");
		tmsRecPage.playStudentRecBtn.click();
		sleep(2);
		pageHelper.checkAudioFileLoadedByFolderName("srec", studentId);
		tmsRecPage.stopStudentRecBtn.click();
		sleep(2);
		
		
		report.startStep("Press on play original segment and check correct media loaded");
		tmsRecPage.playOriginalRecBtn.click();
		sleep(2);
		pageHelper.checkAudioFileLoadedByFolderName("MediaPlayer", resName);
		tmsRecPage.stopOriginalRecBtn.click();
		sleep(2);
				
		report.startStep("Score recording, save and get final score");
		tmsRecPage.scorePhonemes(5);
		sleep(1);
		tmsRecPage.scoreIntonationAndStress(4);
		sleep(1);
		tmsRecPage.scoreOverallPronunciation(3);
		sleep(1);
		tmsRecPage.pressSaveScore();
		sleep(1);
		String teacherGrade = tmsRecPage.getFinalScore();
		
		report.startStep("Check the grade appears for recordings in left side");
		String actRecordGrade = tmsRecPage.getRecordScore();
		testResultService.assertEquals(teacherGrade, actRecordGrade, "Checking that Teacher's grade displayed in left side for recording");
			
		report.startStep("Close TMS recordings page and check grade in table");
		webDriver.closeNewTab(1);
		sleep(1);
		webDriver.switchToMainWindow();
		tmsHomePage.switchToMainFrame();
		tmsHomePage.switchToFormFrame();
		tmsHomePage.clickOnGo();
		sleep(2);
		webDriver.switchToMainWindow();
		tmsHomePage.switchToReviewRecordingsFrame();
		String gradeInTable = tmsHomePage.getStudentRecordGradeByName(userNameSR);
		testResultService.assertEquals(teacherGrade, gradeInTable, "Checking that Teacher's grade displayed in recordings table");
		
		report.startStep("Exit TMS");
		webDriver.switchToTopMostFrame();
		tmsHomePage.switchToMainFrame();
		tmsHomePage.clickOnExit();
		testResultService.assertEquals(false, loginPage.isSubmitButtonEnabled(), "Checking that ED Login Page displayed");
		
		report.startStep("Login as student and check final score displayed in Assignment page");
		homePage = loginPage.loginAsStudent(userNameSR, "12345");
		sleep(3);
		homePage.openAssignmentsPage(false);
		String studentScore = assignPage.selectRecordingByCourseUnitAndLessonNameAndGetScore(courseName, unitName, compName , 1);
		testResultService.assertEquals(teacherGrade, studentScore, "Teacher score does not match to student score");
		
		
	}
	
	@Test
	@TestCaseParams(testCaseID = { "34424" })
	public void testSetProgressPracticeOpenSpeech() throws Exception {
		
		report.startStep("Create user and login");
		createUserAndLoginNewUXClass();
		
		int taskNumber = 1;
		int stepNum = 5;
		
		learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "B3", 9, 2);
		sleep(2);
		learningArea2.clickOnStep(stepNum);
		sleep(2);
		
		report.startStep("Make action go to prev step and back and check task status current");
		InteractSection interactSection = new InteractSection(webDriver,testResultService);
		interactSection.clickOpenSpeechStartButton();
		sleep(1);
		learningArea2.clickOnBackButton();
		webDriver.closeAlertByAccept(2);
		sleep(1);
		learningArea2.clickOnBackButton();
		learningArea2.clickOnStep(stepNum);
		learningArea2.openTaskBar();
		learningArea2.checkIfTaskHasDoneState(taskNumber, false);
				
		report.startStep("Refresh and back and check task status current");
		webDriver.refresh();
		sleep(1);
		learningArea2.clickOnStep(stepNum);
		sleep(1);
		learningArea2.openTaskBar();
		learningArea2.checkIfTaskHasDoneState(taskNumber, false);
		learningArea2.closeTaskBar();
		
		report.startStep("Check the progress not saved in the DB");
		String courseId = getCourseIdByCourseCode("B3");
		String itemId = pageHelper.getItemIdByCourseUnitLessonStepAndTask(courseId, 9, 2, stepNum, taskNumber);
		studentService.checkStudentHasNoProgress(studentId, courseId, itemId);
		
		report.startStep("Make progress trigger action, go to prev step and back and check task status done");
		interactSection.clickOpenSpeechStartButton();
		interactSection.checkOpenSpeechPracticeStatusChanged(InteractStatus.prepare, 1);
		interactSection.checkOpenSpeechPanelStageView(1);
		sleep(1);
		interactSection.clickOpenSpeechSkipButton();
		sleep(1);
		learningArea2.clickOnBackButton();
		webDriver.closeAlertByAccept(2);
		sleep(1);
		learningArea2.clickOnBackButton();
		learningArea2.clickOnStep(stepNum);
		learningArea2.openTaskBar();
		learningArea2.checkIfTaskHasDoneState(taskNumber, true);
		sleep(2);
		learningArea2.closeTaskBar();
				
		report.startStep("Refresh and back and check task status status done");
		webDriver.refresh();
		sleep(1);
		learningArea2.clickOnStep(stepNum);
		sleep(1);
		learningArea2.openTaskBar();
		learningArea2.checkIfTaskHasDoneState(taskNumber, true);
		learningArea2.closeTaskBar();
		
		report.startStep("Navigate to Home Page");
		//learningArea2.clickToOpenNavigationBar();  Removed since Home button no longer part of navigation bar
		sleep(1);
		learningArea2.clickOnHomeButton();
		homePage.waitHomePageloaded();
		
		report.startStep("Check the progress saved in the DB");
		studentService.checkStudentProgress(studentId, courseId, itemId);
		
		report.startStep("Verify comletion value updated in student statistics");
		String completionProgressFD = homePage.getCompletionWidgetValue();
		testResultService.assertTrue("No progress displayed in student statistics",!completionProgressFD.equalsIgnoreCase("0"));		

	}
		
	@Test
	@TestCaseParams(testCaseID = { "22973" }, allowMedia = true)
	public void testSetProgressPracticeSpeakingInteract2() throws Exception {

		report.startStep("Init Test Data");
		String courseCode = "FD";
		int unitNum = 3;
		int lessonNum = 2;
		int stepNum = 5;
		int taskNum = 1;
		
		report.startStep("Create user and login");
		createUserAndLoginNewUXClass();
				
		try {
			
			report.startStep("Navigate to FD-U3-L2-S5");
			learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "FD", unitNum, lessonNum);
			sleep(2);
			learningArea2.clickOnStep(stepNum);
			sleep(3);
			
			report.startStep("Press Start");
			learningArea2.clickOnInteractStartButton();
			sleep(3);
			
			report.startStep("Check the progress in the DB");
			String courseId = getCourseIdByCourseCode(courseCode);
			String itemId = pageHelper.getItemIdByCourseUnitLessonStepAndTask(courseId, unitNum, lessonNum, stepNum, taskNum);
			studentService.checkStudentProgress(studentId, courseId, itemId);
		
			report.startStep("Check task status Done in Task Bar");
			learningArea2.openTaskBar();
			learningArea2.checkIfTaskHasDoneState(taskNum, true);
			learningArea2.closeTaskBar();
			
			report.startStep("Check Step In Progress");
			learningArea2.openStepsList();
			learningArea2.checkProgressIndicationInStepList(stepNum, StepProgressBox.done, false, "undefined");
			learningArea2.closeStepsList();
			
			report.startStep("Check Lesson In Progress");
			learningArea2.openLessonsList();
			learningArea2.checkProgressIndicationInLessonList(lessonNum, 1);
			learningArea2.closeLessonsList();
			
			report.startStep("Refresh");
			webDriver.refresh();
			sleep(2);
			learningArea2.clickOnStep(stepNum);
			
			report.startStep("Check task status Done in Task Bar");
			learningArea2.openTaskBar();
			learningArea2.checkIfTaskHasDoneState(taskNum, true);
			learningArea2.closeTaskBar();
			
			report.startStep("Check Step In Progress");
			learningArea2.openStepsList();
			learningArea2.checkProgressIndicationInStepList(stepNum, StepProgressBox.done, false, "undefined");
			learningArea2.closeStepsList();
			
			report.startStep("Check Lesson In Progress");
			learningArea2.openLessonsList();
			learningArea2.checkProgressIndicationInLessonList(lessonNum, 1);
			learningArea2.closeLessonsList();
			
			report.startStep("Go to Home Page and check course completion value in widget");
			//homePage.clickToOpenNavigationBar();
			sleep(1);
			homePage.clickOnHomeButton();
			homePage.waitHomePageloaded();
			
			String completionProgressFD = homePage.getCompletionWidgetValue();
			testResultService.assertTrue("No progress displayed in student statistics",!completionProgressFD.equalsIgnoreCase("0"));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			testResultService.addFailTest(e.toString(), false, true);
		}

		finally {
			System.clearProperty("chromeMedia");
		}

	}
	
	@Test
	@TestCaseParams(testCaseID = { "22971" }, allowMedia = true)
	public void testSetProgressPracticeSpeakingInteract1() throws Exception {
			
		InteractSection interactSection = new InteractSection(webDriver,testResultService);
		
		report.startStep("Init Test Data");
		String courseCode = "FD";
		int unitNum = 3;
		int lessonNum = 2;
		int stepNum = 4;
		int taskNum = 1;
		
		report.startStep("Create user and login");
		createUserAndLoginNewUXClass();
				
		try {
			
			report.startStep("Navigate to FD-U3-L2-S4");
			learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, courseCode, unitNum, lessonNum);
			sleep(2);
			learningArea2.clickOnStep(stepNum);
			sleep(2);
			
			report.startStep("Select speaker and press Start");
			interactSection.waitForSpeakersArrowsEnabledInInteract1();
			interactSection.selectRightSpeaker();
			sleep(3);
			interactSection.clickTheStartButton();
			sleep(3);
			
			report.startStep("Check the progress in the DB");
			String courseId = getCourseIdByCourseCode(courseCode);
			String itemId = pageHelper.getItemIdByCourseUnitLessonStepAndTask(courseId, unitNum, lessonNum, stepNum, taskNum);
			studentService.checkStudentProgress(studentId, courseId, itemId);
					
			report.startStep("Check task status Done in Task Bar");
			learningArea2.openTaskBar();
			learningArea2.checkIfTaskHasDoneState(taskNum, true);
			learningArea2.closeTaskBar();
			
			report.startStep("Check Step In Progress");
			learningArea2.openStepsList();
			learningArea2.checkProgressIndicationInStepList(stepNum, StepProgressBox.done, false, "undefined");
			learningArea2.closeStepsList();
			
			report.startStep("Check Lesson In Progress");
			learningArea2.openLessonsList();
			learningArea2.checkProgressIndicationInLessonList(lessonNum, 1);
			learningArea2.closeLessonsList();
			
			report.startStep("Refresh");
			webDriver.refresh();
			sleep(2);
			learningArea2.clickOnStep(stepNum);
			
			report.startStep("Check task status Done in Task Bar");
			learningArea2.openTaskBar();
			learningArea2.checkIfTaskHasDoneState(taskNum, true);
			learningArea2.closeTaskBar();
			
			report.startStep("Check Step In Progress");
			learningArea2.openStepsList();
			learningArea2.checkProgressIndicationInStepList(stepNum, StepProgressBox.done, false,"undefined");
			learningArea2.closeStepsList();
			
			report.startStep("Check Lesson In Progress");
			learningArea2.openLessonsList();
			learningArea2.checkProgressIndicationInLessonList(lessonNum, 1);
			learningArea2.closeLessonsList();
			
			report.startStep("Go to Home Page and check course completion value in widget");
			//homePage.clickToOpenNavigationBar();
			sleep(1);
			homePage.clickOnHomeButton();
			homePage.waitHomePageloaded();
			
			String completionProgressFD = homePage.getCompletionWidgetValue();
			testResultService.assertTrue("No progress displayed in student statistics",!completionProgressFD.equalsIgnoreCase("0"));
			

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			testResultService.addFailTest(e.toString(), false, true);
		}

		finally {
			System.clearProperty("chromeMedia");
		}

	}
		
	@Test
	@TestCaseParams(testCaseID = { "28638" })
	public void testNoSendToTeacherInLessonPreviewFromTMS() throws Exception {

		//--igb 2018.08.14 --- replace code to Init-block
			report.startStep("Init test data");

			int recordingId = 38; // segment: would you like some cake?
			float sampleRate = 88200;
			Recording recording = pageHelper.getRecordings().get(recordingId);
		//--igb 2018.08.14 --- replace code to Init-block
			
			report.startStep("Open New UX Login Page");
			studentId = dbService.getUserIdByUserName(configuration.getProperty("teacher.username"),configuration.getProperty("institution.id"));
			loginPage = new NewUXLoginPage(webDriver, testResultService);
			pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(configuration.getProperty("teacher.username"),configuration.getProperty("institution.id")));

			report.startStep("Login as Teacher");
			tmsHomePage = loginPage.enterTeacherUserAndPassword();
			sleep(3);
			tmsHomePage.switchToMainFrame();
			
			report.startStep("Open ED Preview page for B2 -> U2 -> L2");
			tmsHomePage.clickOnCurriculum();
			sleep(2);
			tmsHomePage.clickOnAssignCourses();
			sleep(2);
			tmsHomePage.selectClass(configuration.getProperty("classname.openSpeech"));
			sleep(2);
					
			learningArea2 = tmsHomePage.openPreviewByCourseUnitLessonLA2(courseCodes, "B2", 2, 2);
			webDriver.switchToNextTab();
						
			report.startStep("Open Lesson List and select Lesson 3");
			sleep(2);
			learningArea2.openLessonsList();
			sleep(1);
			learningArea2.clickOnLessonByNumber(3);
								
			report.startStep("Press See Text, select segment and open Record Panel");
			learningArea2.clickOnNextButton();
			learningArea2.clickOnSeeText();
			sleep(3);
			learningArea2.selectSegmentInSpeakingExploreByNumber(1);
			sleep(3);
			learningArea2.clickOnRecordYourselfSegmentTool();
			sleep(3);

			report.startStep("Send audio sound");
			RecordPanel recordPanel = new RecordPanel(webDriver, testResultService);
			switchToRecordPanelAndClickRecord(recordPanel);
			sleep(2);
			
		//--igb 2018.08.14 --- replace code to Init-block
		//	int recordingId = 38; // segment: would you like some cake?
		//	float sampleRate = 88200;
		//	Recording recording = pageHelper.getRecordings().get(recordingId);
		//--igb 2018.08.14 --- replace code to Init-block
			
			audioService.sendSoundToVirtualMic(recording.getFiles().get(0), sampleRate);
			
			report.startStep("Check that recording ended");
			recordPanel.waitForRecordingToEnd(1);
			webDriver.printScreen("After recording ended");
					
			report.startStep("Check Send To Teacher btn not displayed");
			recordPanel.checkThatSendToTeacherBtnIsNotDisplayed();
			
			report.startStep("Close Record Panel");
			recordPanel.closeRecordPanel(true);
		}
		
	@Test
	@TestCaseParams(testCaseID = { "32560" })
	public void testNoSendToTeacherInAccessToEdFromTMS() throws Exception {

	//--igb 2018.08.08 --- replace code to Init-block
		report.startStep("Init test data");
		
		int recordingId = 38; // segment: would you like some cake?
		float sampleRate = 88200;
		Recording recording = pageHelper.getRecordings().get(recordingId);
	//--igb 2018.08.08 --- replace code to Init-block
		
		studentId = dbService.getUserIdByUserName(configuration.getProperty("teacher.username"),configuration.getProperty("institution.id"));
		report.startStep("Open New UX Login Page");
		
		loginPage = new NewUXLoginPage(webDriver, testResultService);
		
		pageHelper.setUserLoginToNull(studentId);

		report.startStep("Login as Teacher");
		tmsHomePage = loginPage.enterTeacherUserAndPassword();
		sleep(3);
		tmsHomePage.switchToMainFrame();
		
		report.startStep("Press on 'Access To ED Platform' link");
		tmsHomePage.clickOnCurriculum();
		sleep(2);
		homePage = tmsHomePage.clickOnAccessToEd();
		sleep(5);  // Bypassing chrome driver issue.
		webDriver.switchToNextTab();
		
				
		report.startStep("Verify user details in ED header");
		String firstName = dbService.getUserFirstNameByUserId(studentId);
		testResultService.assertEquals("Hello " + firstName, homePage.getUserDataText(), "Verify First Name in Header of Home Page");
		
		report.startStep("Navigate to B2 - U2 - L2");
		homePage.navigateToRequiredCourseOnHomePage(coursesNames[2]);
		homePage.clickOnUnitLessons(2);
		learningArea2 = homePage.clickOnLesson2(1, 2);
		
		report.startStep("Open Lesson List and select Lesson 3");
		learningArea2.openLessonsList();
		learningArea2.clickOnLessonByNumber(3);
		sleep(1);
							
		report.startStep("Press See Text, select segment and open Record Panel");		
		learningArea2.clickOnNextButton();
		sleep(1);
		learningArea2.clickOnSeeText();
		sleep(3);
		learningArea2.selectSegmentInSpeakingExploreByNumber(1);
		sleep(3);
		learningArea2.clickOnRecordYourselfSegmentTool();
		sleep(3);

		report.startStep("Send audio sound");
		RecordPanel recordPanel = new RecordPanel(webDriver, testResultService);
		switchToRecordPanelAndClickRecord(recordPanel);
		sleep(2);
		
	//--igb 2018.08.08 --- replace code to Init-block
	//	int recordingId = 38; // segment: would you like some cake?
	//	float sampleRate = 88200;
	//	Recording recording = pageHelper.getRecordings().get(recordingId);
	//--igb 2018.08.08 --- replace code to Init-block
		
		audioService.sendSoundToVirtualMic(recording.getFiles().get(0), sampleRate);
		
		report.startStep("Check that recording ended");
		recordPanel.waitForRecordingToEnd(1);
		webDriver.printScreen("After recording ended");
				
		report.startStep("Check Send To Teacher btn not displayed");
		recordPanel.checkThatSendToTeacherBtnIsNotDisplayed();
		
		report.startStep("Close Record Panel");
		recordPanel.closeRecordPanel(true);
	}
	
	public void calculateSLbyWL(String[] WL, int SL) {
			double wordLevels = 0;
			for (int i = 0; i < WL.length; i++) {
				wordLevels += Integer.valueOf(WL[i]);
			}
			wordLevels = wordLevels / WL.length;
			wordLevels = Math.ceil(wordLevels);

		}
		
	private void setProgressInFDToStudent() throws Exception {
		
		report.startStep("Set progress to first FD course item");
		studentService.createSingleProgressRecored(studentId,"20000", "31515", 10, false, 1);
	}
	
	private void checkInteract2StatusBeforeRecording(InteractSection interactSection) throws Exception {
		
		interactSection.checkifInteract2StatusChanged(InteractStatus.counter, 10);
		interactSection.checkInstructionText(interactSection.instructionText4, true);
		interactSection.checkifInteract2StatusChanged(InteractStatus.recorder, 5);
		interactSection.checkInstructionText(interactSection.instructionText5_1, true);
	}
	
	private void checkInteract2StatusAfterRecording(InteractSection interactSection) throws Exception {
		
		interactSection.checkifInteract2StatusChanged(InteractStatus.counter, 8);
		virtualMicSuccess = interactSection.checkInstructionTextWithReturn(interactSection.instructionText9, true);
		if (!virtualMicSuccess) {
			webDriver.waitForElement("//a[contains(@class,'button continue retry')]", ByTypes.xpath).click();
			sleep(3);
		}
	}
	
	private void checkInteract1StatusAfterRecording(InteractSection interactSection) throws Exception {
		
		interactSection.waitForInstructionToEnd(interactSection.instructionText5);
		interactSection.checkInstructionText(interactSection.instructionText9, true);
		interactSection.checkIfInteract1StatusChanged(1,InteractStatus.speaker, 5);
		interactSection.checkInstructionText(interactSection.instructionText3, true);
		interactSection.checkIfInteract1StatusChanged(2,InteractStatus.counter, 5);
		interactSection.checkInstructionText(interactSection.instructionText4, true);
		sleep(3);
		interactSection.checkIfInteract1StatusChanged(2,InteractStatus.recorder, 20);
	}
					
	private void testRecordYourselfIntegrated(String courseCode, int unitNumber,
			int lessonNumber, int scriptSection, int recordingId,
			float sampleRate) throws Exception {
		
		report.startStep("Init test data");
		int timeoutBeforeRecording = 2;
		Recording recording = pageHelper.getRecordings().get(recordingId);
		String[] expectedWordLevels = null;
		int expectedSentenceLevel = recording.getSL().get(0);
	
		report.startStep("Login to EDO as student");
		getUserAndLoginNewUXClass();
		
		report.startStep("Navigate to lesson");
		learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, courseCode,	unitNumber, lessonNumber);

		report.startStep("Click on recored yourself");

		learningArea2.clickOnStep(2);
		sleep(2);
		learningArea2.clickOnSeeText();
		sleep(2);
		learningArea2.selectTextForRecord(1);
		sleep(1);
		learningArea2.clickOnRecordYourselfSegmentTool();
		sleep(3);

		RecordPanel recordPanel = new RecordPanel(webDriver, testResultService);
		sleep(5);
		report.startStep("Switch to the record panel and start recording");
		switchToRecordPanelAndClickRecord(recordPanel);
		sleep(2);
		
		audioService.sendSoundToVirtualMic(recording.getFiles().get(0),
				sampleRate);
		
		 startStep("Check that recording ended");
		 sleep(timeoutBeforeRecording);
		 recordPanel.waitForRecordingToEnd(1);
		 webDriver.printScreen("After recording ended");
		
		 recordPanel.checkSentenceScoreRatingText(expectedSentenceLevel);
	}
	
	//--igb 2018.06.24------------------------------
		private void chkUserRecMp3(String studentId, int maxWaitSec) throws Exception {
			
		    String recId = dbService.getUserSRid(studentId);
		    
		    if(recId.equals("0"))
		    {
		    	report.reportFailure("Not found Db SR rec info for student:" + studentId);
		    	return;
		    }
			    
		    int waitTime = 0;
		    
		    while(waitTime < maxWaitSec + 1)
		    {
		       if(textService.checkIfFileExist(RECORDINGS_FOLDER.replaceAll("smb:", "") + "/" + studentId + "/" + recId + ".mp3"))
		    	   return;
		       
		       waitTime += 10;
		       sleep(10);
		    }
		}
	//--igb 2018.06.24-------

	@After
	public void tearDown() throws Exception {
		
		super.tearDown();
		
		if (studentId != null && userNameSR != null && userNameSR != defaultUserNameSR && deleteRecordOnTestEnd){
			pageHelper.deleteRecordingsRegisterInDB(studentId);
			pageHelper.deleteStudentRecordingsFolder(RECORDINGS_FOLDER, studentId);
		}
	}
}
