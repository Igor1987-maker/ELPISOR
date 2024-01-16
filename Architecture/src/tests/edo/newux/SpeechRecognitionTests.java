package tests.edo.newux;

import java.util.Random;

import org.junit.After;
import org.junit.Before;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import pageObjects.RecordPanel;
import pageObjects.edo.NewUXLoginPage;
import pageObjects.edo.NewUxAssignmentsPage;
import pageObjects.edo.NewUxCommunityPage;
import pageObjects.edo.NewUxLearningArea;
import pageObjects.edo.NewUxLearningArea2;
import pageObjects.edo.NewUxTalkIdiomsPage;
import pageObjects.tms.StudentRecordingsPage;
import pageObjects.tms.TmsHomePage;
import Enums.FeaturesList;
import Interfaces.TestCaseParams;
import Objects.Recording;
import testCategories.inProgressTests;
import testCategories.reg1;
import testCategories.reg2;


@Category(reg1.class)

//public class SpeechRecognitionTests extends BasicNewUxTest {
public class SpeechRecognitionTests extends SpeechRecognitionBasicTestNewUX {

// 	NewUxLearningArea learningArea;

	NewUXLoginPage loginPage;
	NewUxCommunityPage communityPage;
	NewUxTalkIdiomsPage idiomsPage;
	TmsHomePage tmsHomePage;
	NewUxAssignmentsPage assignPage;
	StudentRecordingsPage tmsRecPage;
	
	static String instID;
	static String classNameSR;	
	static String userNameSR;

	static final String defaultUserNameSR = "stud85177899";
	boolean deleteRecordOnTestEnd = true;
	
//	private static final String RECORDING_WAV = "recording.wav";
//	private static final String RECORDINGS_FOLDER = "smb://frontqa3//SharedUpload//Attachments//EDUX//Recordings//";
	
	
	@Before
	public void setup() throws Exception {
		super.setup();
		
		setPrintResults(true);
		System.setProperty("chromeMedia", "true");
		
//		instID = configuration.getProperty("institution.id");
//		classNameSR = configuration.getProperty("classname.openSpeech");
		
		String sr_Engine = configuration.getAutomationParam("sr", "sr");
		
		if (sr_Engine != null && !sr_Engine.isEmpty()) {
			pageHelper.setFeaturesListPerInstallation(FeaturesList.SR_ENGINE, sr_Engine.toUpperCase());
		}
		
//		webDriver.deleteCookiesAndRefresh();
	}
	
	@Test
	public void testRecordYourself() throws Exception {
		testRecordYourselfIntegrated("FD", 4, 2, 1, 7, 88200);
		
		
	report.startStep("Click on logout");
		learningArea.clickOnLogoutLearningArea();
		sleep(2);
	}

	@After
	public void tearDown() throws Exception {
		
		super.tearDown();
		
//		if (userNameSR != null && userNameSR != defaultUserNameSR && deleteRecordOnTestEnd){
//			pageHelper.deleteRecordingsRegisterInDB(studentId);
//			pageHelper.deleteStudentRecordingsFolder(RECORDINGS_FOLDER, studentId);
//		}
	}
	
	private void testRecordYourselfIntegrated(String courseCode, int unitNumber,
			int lessonNumber, int scriptSection, int recordingId,
			float sampleRate) throws Exception {
		
		report.startStep("Init test data");
		int timeoutBeforeRecording = 2;
		Recording recording = pageHelper.getRecordings().get(recordingId);
//		String[] expectedWordLevels = null;
		int expectedSentenceLevel = recording.getSL().get(0);
	
// 1. igb-		
		loginPage = new NewUXLoginPage (webDriver, testResultService);
//		learningArea = new NewUxLearningArea(webDriver, testResultService);
		learningArea = new NewUxLearningArea2(webDriver, testResultService);
	 //.............
		
		report.startStep("Login to Ed as student");
//		getUserAndLoginNewUXClass();
		
		
		Random rand = new Random();
		int  n = rand.nextInt(7)+1;
		
// 2. igb-		
		webDriver.openUrl("https://ed.engdis.com/qa2018");
		sleep(2);
		loginPage.enterUserName("st"+ n);
		loginPage.enterPassowrd("12345");
		loginPage.PressOnLogin();
	 //.............
		
		report.startStep("Close All Notifications");	
		homePage.closeAllNotifications();		
		
		learningArea.closeAlertModalByAccept();
		
		report.startStep("Navigate to lesson");
		homePage.navigateToCourseUnitAndLesson(courseCodes, courseCode,	unitNumber, lessonNumber);

		report.startStep("Click on recored yourself");

		learningArea.clickOnStep(2);
		sleep(1);
		learningArea.clickOnSeeText();
		sleep(1);
		learningArea.selectTextForRecord(1);
		sleep(1);
		learningArea.clickOnRecordYourselfSegmentTool();
		sleep(1);

		RecordPanel recordPanel = new RecordPanel(webDriver, testResultService);
		sleep(2);
		report.startStep("Switch to the record panel and start recording");
		switchToRecordPanelAndClickRecord(recordPanel);
		//sleep(1);

		audioService.sendSoundToVirtualMic(recording.getFiles().get(0),sampleRate);

		startStep("Check that recording ended");
		sleep(timeoutBeforeRecording);
		recordPanel.waitForRecordingToEnd(1);
		
		webDriver.printScreen("After recording ended");

		recordPanel.checkSentenceScoreRatingText(expectedSentenceLevel);
		recordPanel.closeRecordPanel();
	}
}
