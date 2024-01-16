package pageObjects;

import java.io.File;
import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import services.TestResultService;
import services.TextService;
import Enums.ByTypes;
import Enums.InteractStatus;
import Enums.SRWordLevel;
import drivers.GenericWebDriver;

public class InteractSection extends SRpage {

	public final String instructionText0 = "Click on the arrow next to the character you would like to practice.";
	public final String instructionText1 = "Click 'Start' to begin the conversation.";
	public final String instructionText2 = "Click 'Hear all' to hear the whole conversation. ";
	//public final String instructionText3 = "Listen to the first speaker and prepare to speak.";
	public final String instructionText3 = "Prepare to speak...";
	public final String instructionText4 = "Prepare to speak...";
	public final String instructionText5 = "Speak now.";
	public final String instructionText5_1 = "Speak now.";
	public final String instructionText6 = "Click 'Try again' to repeat your response.";
	public final String instructionText6_1 = "Click 'Try again' to record your answer again.";
	public final String instructionText7 = "Your answer is unclear. Let's move on...";
	public final String instructionText8 = "Click 'Start' to begin the conversation.";
	public final String instructionText9 = "Good! Let's move on...";
	public final String instructionText10 = "You have completed the conversation. Click 'See feedback' below to view detailed feedback on your responses.";
	public final String instructionText11 = "Listen to the response.";
	public final String instructionText12 = "Start";
	public final String instructionText13 = "Try again";
	public final String instructionText14 = "See feedback";
	public final String instructionText15 = "Please follow the instructions provided by your browser in order to activate your microphone.";
	public final String instructionText20 = "Listen to the first speaker and choose a response.";
	public final String instructionText21 = "Please wait while your Speech Recognition session is being established.";
	public final String instructionText22 = "Click 'Start' to record the correct answer.";
	public final String instructionText23 = "Look at the feedback on your conversation. Click 'Hear all' to listen to the conversation.";
	public final String instructionText24 = "Listen to the conversation.";
	public final String taskInstructionText1 = "What\'s the best response to the question?";
	public final String taskInstructionText2 = "";
	public final String openSpeechPanelInst0 = "Click \'Start\' when you are ready to begin the activity.";
	public final String openSpeechPanelInst1 = "You have 1 minute to prepare.";
	public final String openSpeechPanelInst2 = "You have 45 seconds to record yourself.";
	public final String openSpeechPanelInst3 = "When you are happy with your recording, send it to your teacher.";
	public final String openSpeechPanelAlert1 = "The current recording will be deleted and replaced with your new recording.\nDo not show again.";
	public final String openSpeechPanelAlert2 = "The recording has been sent to your teacher. It will be available shortly to listen to in the \'Assignments\' section.";

	public InteractSection(GenericWebDriver webDriver,
			TestResultService testResultService) throws Exception {
		super(webDriver, testResultService);
		// TODO Auto-generated constructor stub
	}

	public void selectRightSpeaker() throws Exception {
		//WebElement speakerElement = webDriver.waitForElement("//div[@class='bgImgContainerWrapper']//div[3]//a",ByTypes.xpath);
		WebElement speakerElement = webDriver.waitForElement("//div[contains(@class,'rightIcon')]//a",ByTypes.xpath);
		speakerElement.click();
	}

	public void selectLeftSpeaker() throws Exception {
		WebElement speakerElement = webDriver.waitForElement(
				"//div[@class='bgImgContainerWrapper']//div[1]//a",
				ByTypes.xpath);
		speakerElement.click();
	}

	public void hoverOnSpeaker(int speaker) throws Exception {
		boolean left = false;
		boolean right = false;
		
		if (speaker == 1) {
			left = true;
		}
		if (speaker == 2) {
			right = true;
		}

		if (left) {
			WebElement speakerElement = webDriver.waitForElement("//div[contains(@class,'leftIcon')]//a", ByTypes.xpath);
			webDriver.hoverOnElement(speakerElement);
		}
		
		if (right) {
			WebElement speakerElement = webDriver.waitForElement("//div[contains(@class,'rightIcon')]//a", ByTypes.xpath);
			webDriver.hoverOnElement(speakerElement);
		}
		

	}

	@Override
	public GenericPage waitForPageToLoad() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GenericPage OpenPage(String url) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public void approveFlash() throws Exception {
		long flashTimeout = 2000;
		webDriver.waitForElement("//div[@id='srcont']//embed", ByTypes.xpath)
				.click();
		Thread.sleep(flashTimeout);
		webDriver.sendKey(Keys.TAB);
		Thread.sleep(flashTimeout);
		webDriver.sendKey(Keys.TAB);
		Thread.sleep(flashTimeout);
		webDriver.sendKey(Keys.ENTER);
		Thread.sleep(flashTimeout);
		// Click Tab 7 times
		int index = 7;
		for (int i = 0; i < index; i++) {
			webDriver.sendKey(Keys.TAB);
			Thread.sleep(flashTimeout);
		}

		webDriver.sendKey(Keys.ENTER);

	}

	public void clickTheStartButton() throws Exception {
		WebElement startButton = webDriver.waitForElement("Start",
				ByTypes.linkText);
		Assert.assertTrue(startButton.isEnabled());
		startButton.click();

	}
	
	public void clickTheTryAgainButton() throws Exception {
		WebElement startButton = webDriver.waitForElement("Try again",
				ByTypes.linkText);
		Assert.assertTrue(startButton.isEnabled());
		startButton.click();

	}
	
	public void clickOpenSpeechStartButton() throws Exception {
		WebElement button = webDriver.waitForElement("//div[contains(@class,'openSpeech__btnStart')]", ByTypes.xpath);
		button.click();

	}
	
	public void clickOpenSpeechSkipButton() throws Exception {
		WebElement button = webDriver.waitForElement("//div[contains(@class,'openSpeech__btnSkip')]", ByTypes.xpath);
		button.click();

	}
	
	public void clickOpenSpeechStopButton() throws Exception {
		WebElement button = webDriver.waitForElement("//div[contains(@class,'openSpeech__btnStop')]", ByTypes.xpath);
		button.click();

	}
	
	public void clickOpenSpeechRetryButton() throws Exception {
		WebElement button = webDriver.waitForElement("//div[contains(@class,'openSpeech__btnRetry')]", ByTypes.xpath);
		button.click();

	}
	
	public void clickOpenSpeechHearButton() throws Exception {
		WebElement button = webDriver.waitForElement("//div[contains(@class,'openSpeech__btnHear')]", ByTypes.xpath);
		button.click();

	}

	public void clickOpenSpeechSendToTeacherButton() throws Exception {
		WebElement button = webDriver.waitForElement("//div[contains(@class,'openSpeech__SentTTBtn')]", ByTypes.xpath);
		button.click();

	}
	
	public void clickTheRestartButton() throws Exception {
		
		webDriver.waitForElement("//a[@class='button startAgain']", ByTypes.xpath).click();
		
		
	}
	
	public void clickTheRestartButtonOnFeedbackPage() throws Exception {
		
		webDriver.waitForElement("//div[contains(@class,'improveYourself')]//a[@class='button startAgain']", ByTypes.xpath).click();
		
		
	}

	public void checkRecordIndicator() throws Exception {

	}

	public void checkInstructionText(String text, boolean isInteract) throws Exception {
		String actualText = null;
		if (isInteract) {
			actualText = webDriver.waitForElement("questionInstructions", ByTypes.className, true, webDriver.getTimeout()).getText();
		}
		else {
			actualText = webDriver.waitForElement("questionInstructionsSR", ByTypes.className, true, webDriver.getTimeout()).getText();
		}
		testResultService.assertEquals(text, actualText,"SR Instruction text did not matched");
	}
	
	public boolean checkInstructionTextWithReturn(String text, boolean isInteract) throws Exception {
		String actualText = null;
		if (isInteract) {
			actualText = webDriver.waitForElement("questionInstructions", ByTypes.className, true, webDriver.getTimeout()).getText();
			Assert.assertTrue("Virtual Mic Failed for 3 times", !actualText.contains("Your answer is unclear. Let's move on"));
		}
		else {
			actualText = webDriver.waitForElement("questionInstructionsSR", ByTypes.className, true, webDriver.getTimeout()).getText();
		}
		boolean assertResult = testResultService.assertEqualsWithReturn(text, actualText);
		return assertResult;
	}
	
	public void checkSpecificTaskInstructionText(String text) throws Exception {
		String actualText = webDriver.waitForElement("questionInstructions", ByTypes.className, true, webDriver.getTimeout()).getText();
		testResultService.assertEquals(text, actualText,"Task Instruction text did not matched");
	}
	
	public void checkOpenSpeechPanelInstruction(String text) throws Exception {
		String actualText = webDriver.waitForElement("//div[@class='openSpeech__stateInst']", ByTypes.xpath, true, webDriver.getTimeout()).getText();
		testResultService.assertEquals(text, actualText,"Open Speech panel instruction text did not matched");
	}
	
	public void checkOpenSpeechPanelStageView(int stageNum) throws Exception {
				
		String stateTitle = webDriver.waitForElement("openSpeech__stateText", ByTypes.className, true, webDriver.getTimeout()).getText();
		WebElement stateElement = webDriver.waitForElement("//div[@class='openSpeech__statesW']/div["+stageNum+"]", ByTypes.xpath, true, webDriver.getTimeout());
		boolean isCurrent = false;
		String currentStageNum = "";
		String expectedStageTitle = "";
		String expectedPanelInstruction = "";
		
		switch (stageNum) {
			
			case 1:
				isCurrent = stateElement.getAttribute("class").contains("current");
				currentStageNum = stateElement.getText();
				expectedStageTitle = "Preparation Time";
				expectedPanelInstruction = openSpeechPanelInst1;
				break;
			case 2:
				isCurrent = stateElement.getAttribute("class").contains("current");
				currentStageNum = stateElement.getText();
				expectedStageTitle = "Recording Time";
				expectedPanelInstruction = openSpeechPanelInst2;
				break;
			case 3:
				isCurrent = stateElement.getAttribute("class").contains("current");
				currentStageNum = stateElement.getText();
				expectedStageTitle = "Send Recording";
				expectedPanelInstruction = openSpeechPanelInst3;
				break;
			
		}
			
		
		testResultService.assertEquals(true, isCurrent, "Stage is not current");
		testResultService.assertEquals(String.valueOf(stageNum), currentStageNum, "Stage number is wrong");
		testResultService.assertEquals(expectedStageTitle, stateTitle, "Stage title is wrong");
		checkOpenSpeechPanelInstruction(expectedPanelInstruction);
		
	}

	public void checkThatSpeakerTextIsHighlighted(int speaker) throws Exception {

		webDriver.waitForElement("//div[@class='recordingPanelWrapper']//div["
				+ speaker + "]", ByTypes.xpath, 20,
				true, "speaker element highlighted");
	}

	public void checkThatStartButtonIsDisabled() throws Exception {
		WebElement startButton = webDriver.waitForElement("Start",
				ByTypes.linkText);
		Assert.assertTrue(startButton.getAttribute("class").contains("disable") == false);
	}

	public void checkStatus(InteractStatus status, int speaker)
			throws Exception {
		webDriver.waitForElement("//div[@class='recordingPanelWrapper']//div["
				+ speaker + "][contains(@class,'" + status.toString() + "')]",
				ByTypes.xpath);
	}

	public String[] getCurrentSpeakerText(int speaker, TextService textService)
			throws Exception {
		String text = null;
		text = webDriver.waitForElement(
				"//div[@class='recordingPanelWrapper']//div[" + speaker
						+ "]//div", ByTypes.xpath).getText();
	
		text = text.substring(1, text.length());
		text = text.replace(".", "");
		String[] words = textService.splitStringToArray(text, "\\s+");
		return words;
	}

	public String[] getInteract2RecordedText(TextService textService, int option)
			throws Exception {
		String text = webDriver.waitForElement(
				"//div[@class='recordingPanelSentencesWrapper']//div[" + option
						+ "]", ByTypes.xpath).getText();
		text = text.substring(1, text.length());
		System.out.println("Recorded text is: " + text);
		String[] words = textService.splitStringToArray(text, "\\s+");
		return words;
	}

	public void checkIfInteract1StatusChanged(int speaker,
			InteractStatus status, int timeOut) throws Exception {
		
		waitUntilStatusChanges(status, timeOut, "//div[@class='recordingPanelWrapper']//div[" + speaker + "]");
	}

	public void checkifInteract2StatusChanged(InteractStatus status, int timeOut)
			throws Exception {
		
		waitUntilStatusChanges(status, timeOut, "//div[@class='speakingInteract']//div[2]");
	}
	
	public void checkMCQPracticeStatusChanged(InteractStatus status, int timeOut)
			throws Exception {
		
		waitUntilStatusChanges(status, timeOut, "//div[contains(@class,'questionAnswersSet')]");
	}
	
	public void checkOpenSpeechPracticeStatusChanged(InteractStatus status, int timeOut)
			throws Exception {
		
		waitUntilStatusChanges(status, timeOut, "//div[contains(@class,'openSpeech__AnsW')]");
	}

	public void waitUntilStatusChanges(InteractStatus after, int timeOut,
			String xpath) throws Exception {
		
		long timeBefore = System.currentTimeMillis();
		
		WebElement afterElement;
		afterElement = webDriver.waitForElement(xpath + "[contains(@class,'" + after.toString() + "')]", ByTypes.xpath, timeOut, false, null, 250);
		if (afterElement == null)
			afterElement = webDriver.waitForElement(xpath + "[contains(@class,'retry')]", ByTypes.xpath, timeOut, false, null, 250);
		
		//webDriver.printScreen("StatusChangedTo" + after.toString());
		
		Assert.assertNotNull("Status did not changed", afterElement);
		
		long timeAfter = System.currentTimeMillis();
		
		long time = timeAfter - timeBefore;
						
		Assert.assertTrue("Status did not changed in time", time < timeOut * 1100);
		

	}

	public void waitUntilRecordingEnds(int timeOut, int speaker)
			throws Exception {
		webDriver.printScreen("Checking of recording ended");
		long timeBefore = System.currentTimeMillis();
		webDriver.waitForElement("//div[@class='recordingPanelWrapper']//div["
				+ speaker + "]//div//div//div//span", ByTypes.xpath);
		webDriver.printScreen("recording ended");
		long timeAfter = System.currentTimeMillis();
		long time = timeAfter - timeBefore;
	
		Assert.assertTrue("Status did not changed in time",
				time < timeOut * 1100);

	}

	public void checkFinalViewWordLevels(String[] words, String[] wordLevels,
			TextService textService, int sentenceNumber) throws Exception {
		for (int i = 0; i < words.length; i++) {
			words[i] = 	words[i].replaceAll("[-.!,?]", "");
			checkFinalViewWordLevel(words[i], Integer.valueOf(wordLevels[i]),
					textService, sentenceNumber);
		}
	}

	public void checkInteract1WordsLevels(String[] words, String[] wordLevels,
			TextService textService, int speaker) throws NumberFormatException,
			Exception {
		// webDriver.printScreen("checkngWordScore");
		// webDriver.getElementHTML(webDriver.waitForElement(
		// "//div[@class='recordingPanelWrapper']//div[2]//div",
		// ByTypes.xpath));
		// System.out.println("Starting to chec word levels."
		// + System.currentTimeMillis());
		for (int i = 0; i < words.length; i++) {
			checkInteract1WordScore(words[i], Integer.valueOf(wordLevels[i]),
					textService, speaker);
		}

	}

	public void checkInteract2WordsLevels(String[] words, String[] wordLevels,
			TextService textService, int speaker) throws NumberFormatException,
			Exception {
		for (int i = 0; i < words.length; i++) {
			checkInteract2WordScore(words[i], Integer.valueOf(wordLevels[i]),
					textService, speaker);
		}

	}

	private void checkInteract2WordScore(String word, int expectedWordLevel,
			TextService textService, int speaker) throws Exception {

		CheckInteractWordScore(word, expectedWordLevel, textService,
				"//div[@class='recordingPanelSentenceText']//");
	}

	private void checkInteract1WordScore(String word, int expectedWordLevel,
			TextService textService, int speaker) throws Exception {
		
		CheckInteractWordScore(word, expectedWordLevel, textService,
				"//div[@class='recordingPanelWrapper']//div[" + speaker
						+ "]//div//div//");
	}

	private void checkFinalViewWordLevel(String word, int expectedWordLevel,
			TextService textService, int sentenceNumber) throws Exception {
		CheckInteractWordScore(word, expectedWordLevel, textService,
				"//div[@class='speakingInteractPanelsWrapper']//div//div["
						+ sentenceNumber + "]//div//div//div//");
	}

	private void CheckInteractWordScore(String word, int expectedWordLevel,
			TextService textService, String xpath) throws Exception {
		boolean found = false;
		SRWordLevel wordLevel = null;
		if (expectedWordLevel <= 3) {
			wordLevel = SRWordLevel.failed;
		} else if (expectedWordLevel >= 4) {
			wordLevel = SRWordLevel.success;
		}
		// webDriver.waitForElement("//div[@class='recordingPanelWrapper']//div["+
		// speaker + "]//div//div//div//span[contains(text(),"
		// + textService.resolveAprostophes(word) + ")]", ByTypes.xpath);

		webDriver.waitForElement(
				xpath + "span[contains(text(),"
						+ textService.resolveAprostophes(word) + ")][@class='"
						+ wordLevel + "']", ByTypes.xpath);

	}

	public void clickInteract2StartButtn() throws Exception {
		webDriver.waitForElement("Start", ByTypes.linkText).click();

	}

	public void checkThatSpeakerTextIsHighlighted() throws Exception {
		webDriver
				.waitForElement(
						"//div[@class='mediaContainer']//div[1][contains(@class,'speaker')]",
						ByTypes.xpath);

	}

	public void checkInteract2recorderText(int option, String[] words,

	String[] wordLevels, TextService textService) throws Exception {
		// WebElement element = webDriver.waitForElement(
		// "//div[@class='recordingPanelSentencesWrapper']//div[" + option
		// + "]//div//div//span", ByTypes.xpath);

		checkInteract2WordsLevels(words, wordLevels, textService, 2);

	}

	public void clickOnSeeFeedback() throws Exception {
		webDriver.waitForElement("See feedback", ByTypes.linkText).click();
	}

	public void waitForInstructionToEnd(String instructionText)
			throws Exception {
		int elapsedTime = 0;
		int timeOut = 10;
		while (elapsedTime < timeOut) {

			String actualText = webDriver.waitForElement(
					"//div[contains(@class,'questionInstructions')]", ByTypes.xpath,
					"instruction text not found").getText();

			if (actualText.equals(instructionText)) {
				Thread.sleep(1000);
				elapsedTime++;
				System.out
						.println("Instruction text did not changed. sleeping for 1000ms");
				continue;
			} else {
				break;
			}
		}
//		System.out.println("Instruction text changed."
//				+ System.currentTimeMillis());

	}

	public void clickOnListenToAllButton() throws Exception {
		webDriver.waitForElement("Hear all", ByTypes.linkText).click();

	}

	public RecordPanel clickOnRepairButton(int sentenceNumber) throws Exception {
		WebElement element = webDriver.waitForElement(
				"//div[@class='speakingInteractPanelsWrapper']//div//div["
						+ sentenceNumber + "]//div//div[2]//div//a",
				ByTypes.xpath);
		element.click();

		return new RecordPanel(webDriver, testResultService);

	}

	public void waitForHearAllButtomToBecomeEnabled() throws Exception {
		//
		// webDriver.waitUntilElementEnabled(webDriver.waitForElement(
		// "//div[@class='speakingInteractButtonWrapper']//div[2]",
		// ByTypes.xpath), 20);
		webDriver.waitUntilElementClickable("//div[@class='speakingInteractButtonWrapper']//div[2]", 20);

	}
	
	public void waitForSeeFeedbackButtonToBecomeEnabled() throws Exception {
		
		webDriver.waitUntilElementClickable("//a[@class='button continue feedback']", 20);

	}
	
	public void waitForRestartBtnIsEnabledOnFeedbackPage() throws Exception {
		
		webDriver.waitForElement("//div[contains(@class,'improveYourself')]//div[contains(@class,'startAgainWrapper') and not(contains(@class,'disable'))]", ByTypes.xpath, true, 60);
				
	}
	
	public void waitForStartBtnIsEnabledInPracticeMCQ() throws Exception {
		
		webDriver.waitForElement("//div[contains(@class,'buttonWrapper') and not(contains(@class,'disable'))]", ByTypes.xpath, true, 30);
				
	}
	
	public void waitForRetryBtndInPracticeMCQ() throws Exception {
		
		webDriver.waitForElement("//a[contains(@class,'button continue retry')]", ByTypes.xpath, true, 30);
				
	}
	
	public void waitForStartBtnIsEnabledInPracticeOpenSpeech() throws Exception {
		
		webDriver.waitForElement("//div[contains(@class,'openSpeech__btnStart') and not(contains(@class,'disabled'))]", ByTypes.xpath, true, 15);
				
	}
	
	public void checkRecordedAnswerIsHighlighted(String recordedAnswer) throws Exception {
		
		String hlAnswer = webDriver.waitForElement("//span[contains(@class,'multiTextInline') and (contains(@class,'hl'))]",ByTypes.xpath).getText();
		testResultService.assertEquals(recordedAnswer, hlAnswer,"Highlighted answer did not matched the recorded");
	}
	
	public void waitForSpeakersArrowsEnabledInInteract1() throws Exception {
		
		webDriver.waitForElement("//div[contains(@class,'generalIcon') and not(contains(@class,'disable'))]", ByTypes.xpath, true, 15);
				
	}
	

}
