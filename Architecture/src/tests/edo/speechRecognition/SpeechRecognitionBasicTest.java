package tests.edo.speechRecognition;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;

import pageObjects.EdoHomePage;
import pageObjects.RecordPanel;
import Enums.AutoParams;
import Objects.Course;
import Objects.Recording;
import tests.misc.EdusoftWebTest;

public class SpeechRecognitionBasicTest extends EdusoftWebTest {

	@Before
	public void setup() throws Exception {
	
		//for chrome to allow media 
		System.setProperty("chromeMedia", "true");
		super.setup();
	}
	
//	@After
//	public void tearDowb()throws Exception{
//		super.tearDown();
//	}

	public void testRecordYourselfIntegrated(int courseId, int scriptSection,
			int recordingId, float sampleRate) throws Exception {
		startStep("Init test data");
		int timeoutBeforeRecording = 2;
		// int courseId = 13;
		// int scriptSection = 1;
		Recording recording = pageHelper.getRecordings().get(recordingId);
		Course course = pageHelper.initCouse(courseId);
		// Recording recording=pageHelper.getRecordings().get(1);

		String[] expectedWordLevels = null;
		int expectedSentenceLevel = 0;
		boolean SRDebug = false;
		String[] words = null;
		int numOfRecordingsInTest = 6;
		List<String[]> recWordLevel = new ArrayList<String[]>();
		List<Integer> sentenceLevels = new ArrayList<Integer>();
		startStep("Login to EDO as student");
		EdoHomePage edoHomePage = pageHelper.loginAsStudent();
		
		edoHomePage.clickOnCourses();
		
		edoHomePage.clickOnCourseByName(course.getName());
		edoHomePage.clickOnCourseUnit(course.getCourseUnit());
		edoHomePage.clickOntUnitComponent(course.getUnitComponent(), "Explore");

		startStep("Click on recored yourself");
		edoHomePage.clickOnSeeScript();
		sleep(3);

		edoHomePage.selectTextFromContainer(scriptSection);
		RecordPanel recordPanel = edoHomePage.clickOnRecordYourself();
		sleep(3);
		edoHomePage.switchToFrameByClassName("cboxIframe");
		startStep("Click on record and send audio file to microphone");

		words = recordPanel.getSentenceText(textService);

		sleep(3);
		report.report("Check record panel message");
		String message = recordPanel.getRecordPanelMessage();
		checkIfErrorAppear(message);
		recordPanel.clickOnRecordButton();
		String status = recordPanel.getRecordPanelStatus();
		testResultService.assertEquals("SPEAK", status);
		// sleep(2);
		// recordPanel.waitForSpeakStatus();
		audioService.sendSoundToVirtualMic(recording.getFiles().get(0),
				sampleRate);

		startStep("Check that recording ended");
		sleep(timeoutBeforeRecording);
		recordPanel.waitForRecordingToEnd(1);
		webDriver.printScreen("After recording ended");

		compareDebugSLAndWLtoExpected(recordPanel, recording, words);

		// int debugSentenceLevel = recordPanel.getDebugSentenceLevel();
		//
		// expectedSentenceLevel = recording.getSL().get(0);
		//
		// report.report("Sentence level is: " + expectedSentenceLevel);
		// String[] debugWordLevels = textService.splitStringToArray(recordPanel
		// .getWordsScoring("wl"));
		// expectedWordLevels = recording.getWL().get(0);
		// report.report("Word level is: " + expectedWordLevels.toString());
		// boolean SLMatch = testResultService
		// .assertEquals(String.valueOf(expectedSentenceLevel),
		// String.valueOf(debugSentenceLevel),
		// "Exptected sentence level and actual sentence level are not the same");
		// startStep("Check word level and sentence level");
		// recordPanel.checkWordsLevels(words, debugWordLevels, textService);
		// if (SLMatch == true) {
		// recordPanel.checckSentenceLevelLightBulbs(expectedSentenceLevel);
		// recordPanel.checkSentenceScoreRatingText(expectedSentenceLevel);
		// recordPanel.checkSentenceScoreText(expectedSentenceLevel);
		// }
		//
		// recordPanel.checkThatWlIsCloseToExpectedWL(expectedWordLevels,
		// textService.splitStringToArray(recordPanel
		// .getWordsScoring("wl")));

	}

	public void compareDebugSLAndWLtoExpected(RecordPanel recordPanel,
			Recording recording, String[] words) throws Exception {
		report.report("checking if srdebug is true");
		boolean srdebug = isSRDebugTrue();
		if (srdebug == false) {
			report.report("srdebug is not true. exiting method");
		} else {
			report.report("Starting to compare SL");

			int debugSentenceLevel = recordPanel.getDebugSentenceLevel();

			int expectedSentenceLevel = recording.getSL().get(0);

			report.report("Sentence level is: " + expectedSentenceLevel);
			String[] debugWordLevels = textService
					.splitStringToArray(recordPanel.getWordsScoring("wl"));
			String[] expectedWordLevels = recording.getWL().get(0);
			System.out.println("expected Word levels are : "
					+ textService.printStringArray(expectedWordLevels));
			boolean SLMatch = testResultService
					.assertEquals(String.valueOf(expectedSentenceLevel),
							String.valueOf(debugSentenceLevel),
							"Exptected sentence level and actual sentence level are not the same");
			startStep("Check word level and sentence level");
			recordPanel.checkWordsLevels(words, debugWordLevels, textService);
			if (SLMatch == true) {

				System.out.println("**************Checking SL");
				recordPanel
						.checckSentenceLevelLightBulbs(expectedSentenceLevel);
				recordPanel.checkSentenceScoreRatingText(expectedSentenceLevel);
				recordPanel.checkSentenceScoreText(expectedSentenceLevel);
			}

			recordPanel.checkThatWlIsCloseToExpectedWL(expectedWordLevels,
					textService.splitStringToArray(recordPanel
							.getWordsScoring("wl")));

		}
	}

	public boolean isSRDebugTrue() throws Exception {
		String srdebug = configuration.getAutomationParam(
				AutoParams.srdebug.toString(), "srdebug");
		try {
			if (srdebug.equals("true") == false) {
				System.out
						.println("srdebug is not true. exiting method. debug SL and WL will not be tested");
				return false;
			} else {
				System.out.println("srdebug is true");
				return true;
			}
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			System.out
					.println("srdebug was null. debug SL and WL will not be tested");
			return false;
		}
	}

	public void checkIfErrorAppear(String message) throws Exception {

		String []errorCodes=new String[]{"10","-1","-10","-20","-40","-50","-60"};
		for(int i=0;i<errorCodes.length;i++){
			if (message.contains("("+errorCodes[i]+")")) {
				System.out.println(message);
				testResultService.addFailTest(message,
						true,true);
				break;
			}
		}
		
		
		

		
	}

}
