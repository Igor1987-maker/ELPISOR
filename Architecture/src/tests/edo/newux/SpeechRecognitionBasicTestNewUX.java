package tests.edo.newux;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;

import pageObjects.EdoHomePage;
import pageObjects.RecordPanel;
import pageObjects.edo.NewUxCommunityPage;
import pageObjects.edo.NewUxLearningArea;
import Enums.AutoParams;
import Objects.Course;
import Objects.Recording;
import tests.misc.EdusoftWebTest;

public class SpeechRecognitionBasicTestNewUX extends BasicNewUxTest {

	NewUxLearningArea learningArea;
	NewUxCommunityPage communityPage; 

	@Before
	public void setup() throws Exception {

		// for chrome to allow media
		System.setProperty("chromeMedia", "true");
		super.setup();
		learningArea = new NewUxLearningArea(webDriver, testResultService);
		communityPage = new NewUxCommunityPage(webDriver, testResultService);
	}

	// @After
	// public void tearDowb()throws Exception{
	// super.tearDown();
	// }

	public void switchToRecordPanelAndClickRecord(RecordPanel recordPanel)
			throws Exception {
		
		learningArea.switchToRecordPanel();
		sleep(1);
		
		recordPanel.clickOnRecordButton();
		//sleep(1);
		
		//String message = recordPanel.getRecordPanelMessage();
		//checkIfErrorAppear(message);
		
		//String status = recordPanel.getRecordPanelStatus();
		//testResultService.assertEquals("SPEAK", status);
	}
	
	protected void switchToRecordPanelAndClickRecordInTalkinIdioms(RecordPanel recordPanel)
			throws Exception {
				
		homePage.switchToFrameInModal();
		recordPanel.clickOnRecordButton();
		sleep(1);
		
		String message = recordPanel.getRecordPanelMessage();
		checkIfErrorAppear(message);
		
		String status = recordPanel.getRecordPanelStatus();
		testResultService.assertEquals("SPEAK", status);
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

		String[] errorCodes = new String[] { "10", "-1", "-10", "-20", "-40",
				"-50", "-60" };
		for (int i = 0; i < errorCodes.length; i++) {
			if (message.contains("(" + errorCodes[i] + ")")) {
				System.out.println(message);
				testResultService.addFailTest(message, true, true);
				break;
			}
		}

	}

}
