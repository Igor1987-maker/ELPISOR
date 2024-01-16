package pageObjects.edo;

import java.security.PublicKey;
import java.util.List;
import java.util.Random;

import javax.sound.midi.Sequence;

import net.lightbody.bmp.core.har.Har;

import org.apache.bcel.generic.IF_ACMPEQ;
import org.apache.tools.ant.taskdefs.Sleep;
import org.hamcrest.core.IsNull;
import org.openqa.selenium.By;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.NeedsLocalLogs;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.google.sitebricks.client.Web;

import Enums.ByTypes;
import Enums.CourseCodes;
import Enums.InteractStatus;
import Enums.MemoryGamePractice;
import Enums.Skill;
import Enums.expectedConditions;
import Objects.testResult;
import drivers.GenericWebDriver;
import pageObjects.GenericPage;
import pageObjects.InteractSection;
import services.Reporter;
import services.TestResultService;


public class NewUxLearningArea extends NewUxHomePage {

	private static final String TaskXpath = ".//div[contains(@class,'learning__progressMeterItemIW')]";

	private static final int maxNumOfTasks = 7;
	
	private static final String FOOTER_ED_LOGO_XPATH = "//footer/div[4]/img";
	
	private static final String E_Rater_Confirmation_1 = "Are you sure your writing assignment is ready to be sent for evaluation?";
	
	private static final String E_Rater_Confirmation_2 = "Your writing assignment was submitted for evaluation. Feedback will be available shortly in the 'Assignments' section.";
	
	public final String E_Rater_Confirmation_3 = "This writing assignment is being evaluated. You will be notified when feedback is ready.";
	
	

	private List<WebElement> memoryGameCards;
		
	private int cardID;
	
	NewUxDragAndDropSection dragAndDrop = new NewUxDragAndDropSection (webDriver, testResultService);
	InteractSection interactSection = new InteractSection(webDriver, testResultService);
	
	
	public NewUxLearningArea(GenericWebDriver webDriver, TestResultService testResultService) throws Exception {
		super(webDriver, testResultService);
		// TODO Auto-generated constructor stub
	}

	public void clickOnOKButton() throws Exception {
		webDriver.waitForElement("btnOk", ByTypes.id).click();

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

	public WebElement getLearningTaskElement(String taskName, int index) throws Exception {
		return webDriver.waitForElement(
				"//div[@class='learning__tasks']//div[" + index + "]//span//span[2]//span[text()='" + taskName + "']",
				ByTypes.xpath, false, webDriver.getTimeout());
	}

	public String getHeaderTitle() throws Exception {
		//WebElement element = webDriver.waitForElement("//div[contains(@class,'learning__userBarTitleIW')]//h1",
			//	ByTypes.xpath, false, webDriver.getTimeout());
		WebElement element = webDriver.waitForElement("learning__dropDownList_unitName", ByTypes.className);
		if (!(element == null)) {
			// return element.getText();
			return webDriver.getElementText(element);
		} else
			webDriver.printScreen("Header element not found");
		return null;
	}

	public String getLessonNumber() throws Exception {
		WebElement element = webDriver.waitForElement("//div[@class='learning__lessonsSequence']", ByTypes.xpath, false,
				webDriver.getTimeout());

		if (element != null) {
			return element.getText();
		}
		return null;
	}

	public String getLessonName() throws Exception {
		WebElement element = webDriver.waitForElement("//section[contains(@class,'-lessons')]//div[contains(@title,'Lesson')]", ByTypes.xpath, false, webDriver.getTimeout()); // //div[contains(@class,'learning__LessonName')]

		if (element != null) {
			return element.getText().split(": ")[1];
		} 
		return null;
	}

	public void clickOnAlert() throws Exception {
		webDriver.waitForElement("G10", ByTypes.id,false,180);
		
	}
	
	public void withOutAlert (boolean stateOfAlert) throws Exception {
		if(stateOfAlert== false){
			clickOnAlert();
		}
		else {
			tearDown();
		}
}
	
	public void checkFirstStepOpened() throws Exception {

		webDriver.waitForElement(
				"//div[@class='learning__tasks']/div[1][contains(@class,'learning__taskW learning__taskW--open')]",
				ByTypes.xpath);

	}
	
	public void clickOnCancel() throws Exception {
		webDriver.waitForElement("btnCancel", ByTypes.id).click();

	}

	public String getLessonSequence() throws Exception {
		String text = null;
		WebElement element = null;
		try {
			element = webDriver.waitForElement("learning__lessonsSequence", ByTypes.className);
			text = element.getText();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			testResultService.addFailTest("Problem getting lesson sequence");
			webDriver.printScreen();
		}
		return text;
	}

	public int getNumberOfTasks() throws Exception {

		List<WebElement> elements = webDriver.getElementsByXpath("//div[contains(@class, 'learning__progressMeterItemOW')]");

		// Get number of tasks procedure - Hayk
		/*
		 * WebElement element = webDriver.waitForElement(
		 * "//div[@class='learning__progressMeterIW']", ByTypes.xpath,
		 * "Lesson steps container");
		 * 
		 * // webDriver.highlightElement(element);
		 * 
		 * List<WebElement> tasksElements = webDriver.getChildElementsByXpath(
		 * element, TaskXpath);
		 */
		return elements.size();

	}

	/**
	 * 
	 * @param index
	 *            1st step=1
	 * @throws Exception
	 */
	public void clickOnStep(int index) throws Exception {
		try {
			
			//webDriver.waitForElement("//section[contains(@class,'learning__dropDownListBtn')]",ByTypes.xpath,false,smallTimeOut);
			webDriver.waitUntilElementAppears("//div[contains(@class,'learning__dropDownListBtn')]", ByTypes.xpath, 5);
			
			webDriver.waitForElement("//section[contains(@class,'--steps')]", ByTypes.xpath, true, webDriver.getTimeout()).click();
			webDriver.waitForElement("//section[contains(@class,'--steps')]//div[contains(text(),'"+index+".')]", ByTypes.xpath, true, webDriver.getTimeout()).click();

			/*index = index - 1;
			webDriver.waitUntilElementAppears("//div[contains(@class,'learning__taskW')]", ByTypes.xpath, 30);
			WebElement element = webDriver.waitForElement(
					"//div[contains(@class,'learning__taskW')][@index=" + index + "]//div//div[1]", ByTypes.xpath,
					false, smallTimeOut);

			if (element != null) {
				webDriver.highlightElement(element);
				element.click();
			} else {
				testResultService.addFailTest("Failed clicking on step", false, true);
			}*/
		} catch (Exception e) {
			// TODO Auto-generated catch block
			testResultService.addFailTest("Failed clicking on step", false, true);
		}

	}
	
	public void clickOnStepNew(int index) throws Exception{
		WebElement stepsList = webDriver.waitForElement("//section[contains(@class,'--steps')][1]", ByTypes.xpath, false, webDriver.getTimeout());
		webDriver.ClickElement(stepsList);
		List<WebElement> steps = webDriver.getWebDriver().findElements(By.xpath("//section[contains(@class,'--open')]//div[@class='learning__dropDownList_itemNameText']"));
		webDriver.ClickElement(steps.get(index));
	}

	public boolean checkThatTaskIsVisitedAndCurrent(int taskIndex) throws Exception {
		WebElement element = webDriver.waitForElement(
				"//div[@index='" + taskIndex + "'][contains(@class,'active')]//div//div[contains(@class,'half')]",
				ByTypes.xpath, 1, false, "Task in progress");
		
		/*if (element == null) {
			testResultService.addFailTest("Task Element wasn't displayed or the sent task element index was incorrect.", true, true);
			testResultService.assertTrue("Task Element wasn't displayed or the sent task element index was incorrect.", element.isDisplayed());
		}*/
		
		if (element != null) {
			return true;
		} else {
			//webDriver.printScreen();
			return false;
		}

	}

	/**
	 * 
	 * @param taskIndex
	 *            1st task=0
	 * @throws Exception
	 */
	public String clickOnTask(int taskIndex) throws Exception {

		String itemId="undefined";
		
		try {
			if (taskIndex <= 6) {
				
				WebElement element = webDriver.waitForElement(
						"//div[@class='learning__progressMeterIW']//div[@index='" + taskIndex + "']", ByTypes.xpath,
						false, webDriver.getTimeout());
				
			/*	WebElement element = webDriver.waitForElement(
						"//div[@class='learning__taskNavPagerCounterIW learning__taskNavPagerCounterIW--intro']", ByTypes.xpath,
						false, webDriver.getTimeout());
				element.click();
				webDriver.findElementByXpath("//span[normalize-space()='"+ taskIndex +"']", ByTypes.xpath).click();
*/
				if (element != null) {
					element.click();
					Thread.sleep(1000);
					itemId = element.getAttribute("id");
				} else {
					testResultService.addFailTest("task element was null", false, true);
				}
			} else {
				//int tasksPage = taskIndex / 6;
				//int currentPgae = getCurrentTasksPage();
			}
		} catch (UnhandledAlertException e) {
			// TODO Auto-generated catch block
			testResultService.addFailTest("Aleet was open", false, true);
		}
		
		return itemId;

	}

	public int getCurrentTasksPage() throws Exception {
		// TODO Auto-generated method stub
		for (int i = 1; i < 5; i++) {
			WebElement element = webDriver.waitForElement(
					"//div[@class='learning__progressMeterWrapper']//div[" + i + "]//div//div[1]", ByTypes.xpath);
			
			if (element != null && element.isDisplayed()) {
				return i - 1;
			}
		}
		return -1;
	}

	/**
	 * 
	 * @param taskIndex
	 * @param taskPage
	 * @throws Exception
	 */
	public String clickOnTask(int taskIndex, int taskPage) throws Exception {
		
		String itemId="undefined";
		
		try {
			if (taskPage==0) {
				itemId = clickOnTask(taskIndex);
			}
			else {
			/*WebElement element = webDriver.waitForElement(
					"//div[@class='learning__progressMeterWrapper']//div[@id='tasksPage" + taskPage
							+ "']//div//div[@index='" + taskIndex + "']//div//div//div",
					ByTypes.xpath, false, webDriver.getTimeout());*/
			
			WebElement element = webDriver.waitForElement(
					"//div[@class='learning__progressMeterWrapper']//div[@id='tasksPage" + taskPage
							+ "']//div//div[@index='" + taskIndex + "']",
					ByTypes.xpath, false, webDriver.getTimeout());
			
			

			if (element != null) {
				// webDriver.highlightElement(element);
				try {
					element.click();
					itemId = element.getAttribute("id");
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					testResultService.addFailTest("Could not click on task", false, true);
				}
			} else {
				testResultService.addFailTest("Task " + taskIndex + "in page " + taskPage + "  was not found", false,
						true);
				}
			}
		} catch (UnhandledAlertException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return itemId;
		
	}

	public String getStepName(int index) throws Exception {
		WebElement element = webDriver
				.waitForElement("//div[@class='learning__tasks']//div[" + index + "]//div//div[2]//div", ByTypes.xpath); //section[contains(@class,'-steps')]//div[contains(@title,'Step')]
		// element.click();
		return webDriver.getElementText(element);

	}
	
	public String getStepNumber(int index) throws Exception {
		WebElement element = webDriver
				.waitForElement("//div[@class='learning__tasks']//div[" + index + "]//div//div[1]", ByTypes.xpath);
		// element.click();
		return webDriver.getElementText(element);

	}

	// public void clickOnStep(int index) throws Exception {
	// webDriver.waitForElement("//div[@class='learning__tasks']",
	// ByTypes.xpath).click();
	// }

	public int getNumberOfSteps() throws Exception {
		WebElement element = webDriver.waitForElement("//div[@class='learning__tasks']", ByTypes.xpath);

		List<WebElement> child = webDriver.getChildElementsByXpath(element,
				"//div[contains(@class,'learning__taskW')]");
		return child.size();
	}

	public void clickOnNextButton() throws Exception {
		clickOnNextButton(1);
	}
	
	public void clickOnBackButton() throws Exception {
		clickOnBackButton(1);
	}

	public void clickOnNextButton(int times) throws Exception {
		for (int i = 0; i < times; i++) {
			getNextButtonElement().click();
			Thread.sleep(1000);
		}

	}

	public void clickOnBackButton(int times) throws Exception {
		for (int i = 0; i < times; i++) {
			getBackButtonElement().click();
			Thread.sleep(1000);
		}

	}
	
	public WebElement getNextButtonElement() throws Exception {
		//return webDriver.waitForElement("//div[@class='learning__nextItem']//a", ByTypes.xpath, false, smallTimeOut);
		return webDriver.waitForElement("//a[contains(@class,'learning__nextItemLink')]", ByTypes.xpath, false, smallTimeOut);
	}
	
	public WebElement getBackButtonElement() throws Exception {
		
		return webDriver.waitForElement("//a[contains(@class,'learning__prevItemLink')]", ByTypes.xpath, false, smallTimeOut);
	}

	public void clickOnCheckAnswer() throws Exception {
		webDriver.waitForElement("CheckAnswer", ByTypes.id).click();
	}
	
	
	public void clickOnSeeAnswer() throws Exception {
		webDriver.waitForElement("SeeAnswer", ByTypes.id).click();
	}

	public void clickOnClearAnswer() throws Exception {
		webDriver.waitForElement("Restart", ByTypes.id).click();
	}

	public boolean checkIfSeeTranslationIsEnabled() throws Exception {
		WebElement seeTranslation = getSeeTranslationElement();
		return seeTranslation.isEnabled();
	}

	private WebElement getSeeTranslationElement() throws Exception {
		// return webDriver.waitForElement("tempLink1", ByTypes.id);
		WebElement element = webDriver.waitUntilElementAppearsAndReturnElementByType("getTransIcon", ByTypes.id, 1);
		return element;
				//webDriver.waitForElement("getTransIcon", ByTypes.id, false, smallTimeOut); // igors
	}

	public void checkAudioFile(String mediaPlayerId, String audioFile) throws Exception {

		// WebElement audioPlayer =
		// webDriver.waitForElement("//audio[@id='MediaPlayer1']",
		// ByTypes.xpath,);
		WebElement audioElement = webDriver.waitForElement("//audio[@id='" + mediaPlayerId + "']", ByTypes.xpath,
				webDriver.getTimeout(), true, "audio element", 1000, expectedConditions.precence);

		String fileName = webDriver.getElementSrc(audioElement);
		// System.out.println(fileName);
		testResultService.assertEquals(true, fileName.contains(audioFile), "Audio file source not found or not correct");

	}
	
	public void checkVideoFile(String mediaPlayerId, String videoFile) throws Exception {

		
		WebElement videoElement = webDriver.waitForElement("//video[@id='" + mediaPlayerId + "']/source", ByTypes.xpath,
				webDriver.getTimeout(), true, "video element", 1000, expectedConditions.precence);

		String fileName = webDriver.getElementSrc(videoElement);
		testResultService.assertEquals(true, fileName.contains(videoFile), "Video file source not found or not correct");

	}

	public void checkImage(String id, String imageFile) throws Exception {
		WebElement imageElement = webDriver.waitForElement(id, ByTypes.id);
		testResultService.assertEquals(true, imageElement.getAttribute("src").contains(imageFile));

	}

	public void clickOnMediaById(String id) throws Exception {
		webDriver.waitForElement("//a[@id='" + id + "'][@class='clickMedia headphonesBt']", ByTypes.xpath).click();

	}

	public void clickOnStartTest() throws Exception {
		//WebElement element = webDriver.waitForElement("//a[@class='btnStartTest']", ByTypes.xpath, false, webDriver.getTimeout());
		WebElement element = webDriver.waitForElement("Start Test", ByTypes.linkText, false, webDriver.getTimeout());
		if (element != null) {
			element.click();
		} else {
			testResultService.addFailTest("Start test element not found", false, true);
		}

	}

	public void checkThatStepNameIsDisplayed(String stepName) throws Exception {
		WebElement element = webDriver.waitForElement(
				"//div[@class='learning__tasks']//div[contains(@class,'open')]//div//div[2]//div", ByTypes.xpath);

		testResultService.assertElementText(element, ". " + stepName);

	}

	public void checkThatPracticeStepIsDisplayed() throws Exception {
		WebElement element = webDriver.waitForElement("//span[@title='Practice'", ByTypes.xpath);

		testResultService.assertElementText(element, ". Practice");

	}

	public void checkIfTaskIsActive(int taskNumber) throws Exception {
		WebElement taskElement = webDriver.waitForElement(
				"//div[@class='learning__progressMeterIW']//div[" + taskNumber + "][contains(@class,'active')]",
				ByTypes.xpath, webDriver.getTimeout(), false);
		if (taskElement == null) {
			testResultService.addFailTest("Task " + taskNumber + " was not highlghted", false, true);
		}
	}

	public void checkThatTaskIsDisabled(int i) throws Exception {
		WebElement taskElement = webDriver.waitForElement("//div[@class='learning__progressMeterIW']//div[@index='" + i
				+ "']//div//div[contains(@class,'disabled')]", ByTypes.xpath, webDriver.getTimeout(), false);
		if (taskElement == null) {
			testResultService.addFailTest("Task: " + i + " was not disabled", false, true);
		}

	}

	public void checkThatNextButtonIsDisabled() throws Exception {

		WebElement child = webDriver.getChildElementByXpath(getNextButtonElement(), "//a[@disabled='disabled']");

		if (child == null) {
			testResultService.addFailTest("Next button is not disabled", false, true);
		}

	}
	
		
	public void checkThatNextButtonIsNotDisplayedInLA() throws Exception {
		
		WebElement buttonNext = getNextButtonElement();
				
		if (buttonNext != null ) {
			testResultService.addFailTest("Next btn was displayed while it should have been hidden", false, true);
		
		}

	}

	public void checkthatTestButtonIsDisplayed() throws Exception {
		WebElement element = getTestStartButton();
		if (element.isDisplayed() != true) {
			testResultService.addFailTest("Start test button is not displayed", false, true);
		}

	}

	public WebElement getTestStartButton() throws Exception {
		WebElement startButton = webDriver.waitForElement("//div[@id='testIntro']//a", ByTypes.xpath); 
		return startButton;
	}

	public void clickTheStartTestButon() throws Exception {
		try {
			webDriver.sleep(2);
			webDriver.clickOnElement(getTestStartButton());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}

	}
	
	public void checkThatStartTestBtnDisabled() throws Exception {
			
		WebElement button = getTestStartButton();
		button.click();
		boolean isDisabled = button.getAttribute("class").contains("disabled");
		testResultService.assertEquals(true, isDisabled, "Start Test btn not disabled");
		
	}

	public void checkThatSubmitButtonIsDisplayed() throws Exception {
		WebElement element = getSubmitButton();
		if (element.isDisplayed() != true) {
			testResultService.addFailTest("Submit button is not displayed", false, true);
		}

	}

	public WebElement getSubmitButton() throws Exception {
		// TODO Auto-generated method stub
		return webDriver.waitForElement("//a[contains(@class,'submitTest')]", ByTypes.xpath); // old xpath (not working): //a[@class='submitTest button']
	}

	public void checkThatNextButtonIsEnabled() throws Exception {
		WebElement buttonnElement = getNextButtonElement();

		if (buttonnElement.isDisplayed() != true) {
			testResultService.addFailTest("Next button is not enabled", false, true);
		}

	}

	public void dragAndDropAnswerById(String id) throws Exception {
		//WebElement answerPlaceHolderElement = webDriver.waitForElement("//div[@class='TextDiv'][@id='1_1']//span",ByTypes.xpath);
		WebElement answerPlaceHolderElement = webDriver.waitForElement("//span[contains(@class,'droptarget')][@data-id='1_1']",ByTypes.xpath);
		
		WebElement answer = webDriver.waitForElement(
				"//div[@class='wordsBankWrapper']//div//div//div[@data-id='" + id + "']", ByTypes.xpath);

		webDriver.dragAndDropElement(answer, answerPlaceHolderElement);

	}

	public void selectMultipleAnswer(int i) throws Exception {
		if (webDriver.getBrowserName().equals("chrome")) {
			webDriver.waitForElement("//*[@id='q1a" + i + "']/div[1]/div/div[1]/input", ByTypes.xpath).click();
		}else {
			webDriver.waitForElement("//*[@id='q1a" + i + "']/div[1]/div/div[1]/label", ByTypes.xpath).click();
		}
	}

	public void submitTest(boolean clickOk) throws Exception {
		getSubmitButton().click();
		Thread.sleep(1000);
		if (clickOk) {
			approveTest();
		}
		Thread.sleep(4000);

	}

	public void approveTest() throws Exception {
		//webDriver.switchToFrame(1);
		//WebElement logout = webDriver.waitForElement("EdoFrameBoxContent", ByTypes.id);
		//webDriver.switchToFrame(logout.findElement(By.tagName("iframe")));
		
		WebElement parentElement = webDriver.waitForElement("EdoFrameBoxContent", ByTypes.id);
		WebElement iframe = parentElement.findElement(By.tagName("iframe")); 
		webDriver.switchToFrame(iframe);
		
		webDriver.waitForElement("btnOk", ByTypes.id).click();
		webDriver.switchToMainWindow();
	}

	public void cancelTestConfirmationDialog() throws Exception {
		//WebElement logout = webDriver.waitForElement("EdoFrameBoxContent", ByTypes.id);
		//webDriver.switchToFrame(logout.findElement(By.tagName("iframe")));
		
		WebElement parentElement = webDriver.waitForElement("EdoFrameBoxContent", ByTypes.id);
		WebElement iframe = parentElement.findElement(By.tagName("iframe")); 
		webDriver.switchToFrame(iframe);

		//webDriver.waitForElement("btnCancel", ByTypes.id).click();
		webDriver.waitForElement("btnCancel", ByTypes.id).click();
		webDriver.switchToMainWindow();
	}

	public void checkThatTestWasSubmitted() throws Exception {
		
		if (getTestScoreResultElement() == null) {
			testResultService.addFailTest("Test was not submitted", true, true);

		}

	}
	
	public String getTestScore() throws Exception {
		
		String score = getTestScoreResultElement().getText();
		
		return score;
	}
	
	public String getTestResultFeedbackBottom() throws Exception {
		
		String feedbackBottom = getTestResultFeedbackBottomElement().getText();
		
		return feedbackBottom;
	}
	
	public String getTestResultUserName() throws Exception {
		
		String firstName = webDriver.waitForElement("learning__testResultUsername", ByTypes.className, "First name in test result page").getText();
		
		return firstName;
	}
	
	public void checkNoBottomFeedbackinTest() throws Exception {
		
		if (getTestResultFeedbackBottomElement() != null) {
			testResultService.addFailTest("Feedback is displayed though it should not", false, true);
		}
	}
	
	public void checkNoTopFeedbackInTest() throws Exception {
		
		if (getTestResultFeedbackTopElement() != null) {
			testResultService.addFailTest("Feedback is displayed though it should not", false, true);
		}
	}
	
	public String getTestResultFeedbackTop() throws Exception {
		
		String feedbackUpper = getTestResultFeedbackTopElement().getText().trim();
		
		return feedbackUpper;
	}
	
	private WebElement getTestScoreResultElement() throws Exception {
		
		return webDriver.waitForElement("//div[contains(@class, 'testScoreResult')]", ByTypes.xpath, webDriver.getTimeout(), false);
	}
	
	private WebElement getTestResultFeedbackBottomElement() throws Exception {
		
		return webDriver.waitForElement("//span[contains(@class, 'testFeedbackText')]", ByTypes.xpath, smallTimeOut, false);
	}
	
	private WebElement getTestResultFeedbackTopElement() throws Exception {
		
		return webDriver.waitForElement("//div[@class='feedbackText']", ByTypes.xpath, smallTimeOut, false);
	}
	
	public void validateSubmitPopupMessageAndSubmit(String text) throws Exception {
		webDriver.switchToFrame(2);
		String message = webDriver.waitForElement("//div[@class='hebDir']", ByTypes.xpath).getText();
		if (message.contains(text) == false) {
			testResultService.addFailTest("Text : " + text + " was not displayed in the popup message", false, true);
		}
		webDriver.waitForElement("btnOk", ByTypes.id).click();
		webDriver.switchToMainWindow();

	}
	
	public void validateQuitPopupMessageinTest(String text, boolean toConfirm) throws Exception {
		webDriver.switchToFrame(2);
		String message = webDriver.waitForElement("//div[@class='hebDir']", ByTypes.xpath).getText();
		
		if (message.contains(text) == false) {
			testResultService.addFailTest("Text : " + text + " was not displayed in the popup message", false, true);
		}
		
		String buttonID = toConfirm ? "btnOk" : "btnCancel";
		 
		webDriver.waitForElement(buttonID, ByTypes.id).click();
		webDriver.switchToMainWindow();

	}

	public NewUxLearningArea openLessonsList() throws Exception {
		webDriver.waitForElement("//section[@id='learning__dropDownListTitleW_lessons']//div[1]", ByTypes.xpath, "Open lesson list button")
				.click(); //   //div[@title='Open lesson list']
		return this;
	}
	
	public void closeLessonsList() throws Exception {
		webDriver.waitForElement("//div[@title='Close lesson list']", ByTypes.xpath, "Open lesson list button")
				.click();
	}

	public String getLessonNameFromListByIndex(int index) throws Exception {
		WebElement element = getLessonElementByIndex(index);

		return webDriver.getElementText(element);

	}

	public void clickOnLessoneByIndex(int index) throws Exception {
		getLessonElementByIndex(index).click();
	}

	private WebElement getLessonElementByIndex(int index) throws Exception {
		return webDriver.waitForElement("//ul[@class='learning__courseListItemsW']//li[" + index + "]//a//div//span[2]",
				ByTypes.xpath, "Lesson list element text");
	}

	public void clickOnPlayVideoButton() throws Exception {
		//webDriver.waitForElement("CTrackerPlayBtn", ByTypes.id).click();
		webDriver.waitForElement("layout__mediaPlayPause", ByTypes.className).click();

	}
	
	public void clickOnPlayNewVideoButton() throws Exception {
		//webDriver.waitForElement("play-pause", ByTypes.id).click();
		webDriver.waitForElement("learning__RAMRButton", ByTypes.className).click();
		
	}

	/**
	 * 
	 * @param index
	 *            1st task=0
	 * @throws Exception
	 */
	@SuppressWarnings("null")
	public void checkThatTaskIsDone(int index) throws Exception {
		WebElement taskElement = webDriver.waitForElement("//div[@class='learning__progressMeterIW']//div[@index='"
				+ index + "']//div//div[contains(@class,'full')]", ByTypes.xpath, smallTimeOut, false);

		if (taskElement == null) {
			testResultService.addFailTest("Task status is not Done (Completed)", true, true);
			
		}
	}
	
	public void checkThatTestTaskMarkCorrect(int taskIndex) throws Exception {
		webDriver.waitForElement("//div[@class='learning__progressMeterIW']//div[@index='"
				+ taskIndex + "']//div[@class='check']", ByTypes.xpath, "Task mark indication is not correct");
		}
	
	public void checkThatTestTaskUnclickable(int taskIndex) throws Exception {
		webDriver.waitForElement("//div[@class='learning__progressMeterIW']//div[@index='"
				+ taskIndex + "']//div[contains(@class,'disabled')]", ByTypes.xpath, "Task is not disabled");
		}
	
	public void checkThatTestTaskMarkWrong(int taskIndex) throws Exception {
		webDriver.waitForElement("//div[@class='learning__progressMeterIW']//div[@index='"
				+ taskIndex + "']//div[@class='xcheck']", ByTypes.xpath, "Task mark indication is not correct");
		}
	
	public void checkThatTestTaskMarkPartlyCorrect(int taskIndex) throws Exception {
		webDriver.waitForElement("//div[@class='learning__progressMeterIW']//div[@index='"
				+ taskIndex + "']//div[@class='hcheck']", ByTypes.xpath, "Task mark indication is not correct");
		}
	

	public void clickOnPlayVideoButtonPrepare() throws Exception {
		getPlayButtonPrepare().click();

	}
	
	public WebElement getPlayButtonPrepare() throws Exception {
		return webDriver.waitForElement("//a[@class='play learning__RAMRButton']", ByTypes.xpath, false, webDriver.getTimeout());

	}

	public void openCard(int index) throws Exception {
		webDriver.waitForElement("//ul[@id='GameBoardUl']//li[" + index + "]//div//span", ByTypes.xpath).click();

	}

	public void checkCheckBox(int index) throws Exception {
		if (webDriver.getBrowserName().equals("chrome")) {
			webDriver.waitForElement("//div[@class='multChoiceQ']//div//div[" + index + "]//div[1]//div//div//input",
					ByTypes.xpath).click();
		} else {
			webDriver.waitForElement("//div[@class='multChoiceQ']//div//div[" + index + "]//div[1]//div//div//label",
					ByTypes.xpath).click();
		}
	}

	public void selectAnswerFromDropDown(String questonId, String answerText) throws Exception {
		webDriver.waitForElement("//span[@id='" + questonId + "']//div[2]", ByTypes.xpath).click();

		WebElement element = webDriver.waitForElement("//div[@selectedspan='" + questonId + "']", ByTypes.xpath);
		WebElement childElement = webDriver.getChildElementByXpath(element, "//td[text()='" + answerText + "']");
		childElement.click();

	}
	
	public void selectFirstAnswerFromDropDown(String questonId) throws Exception {
		webDriver.waitForElement("//span[@id='" + questonId + "']//div[2]", ByTypes.xpath).click();

		WebElement element = webDriver.waitForElement("//div[@selectedspan='" + questonId + "']//td", ByTypes.xpath);
		//WebElement childElement = webDriver.getChildElementByXpath(element, "/table/tbody/tr/td");
		element.click();

	}

	public void selectAnswerRadio(int questionIndex, int answerIndex) throws Exception {
		if (webDriver.getBrowserName().equals("chrome")) {
			webDriver.waitForElement("//div[contains(@class,'lessonAnswersWrapper')][@id='q" + questionIndex
					+ "']//div[" + answerIndex + "]//div[2]//div//div[2]//input", ByTypes.xpath).click();
		} else {
			webDriver.waitForElement("//div[contains(@class,'lessonAnswersWrapper')][@id='q" + questionIndex
					+ "']//div[" + answerIndex + "]//div[2]//div//div[2]//label", ByTypes.xpath).click();
		}
	}

	public void clickOnSubmitText() throws Exception {
		Thread.sleep(1000);
		webDriver.waitForElement("//a[@title='Submit']", ByTypes.xpath).click();
		Thread.sleep(1000);
		//webDriver.switchToFrame(webDriver.waitForElement("//div[@id='EdoFrameBoxContent']//iframe", ByTypes.xpath));
		//WebElement okBtn = webDriver.waitForElement("btnOk", ByTypes.id);
		//webDriver.highlightElement(okBtn);
		//okBtn.click();
		validateConfirmModalByMessage(E_Rater_Confirmation_1, true);
		//webDriver.closeAlertByAccept();
		Thread.sleep(3000);
		//okBtn = webDriver.waitForElement("btnOk", ByTypes.id);
		//okBtn.click();
		validateAlertModalByMessage(E_Rater_Confirmation_2, true);
		webDriver.switchToMainWindow();

	}

	public void clickOnPage2() throws Exception {
		webDriver.waitForElement("//div[@class='innerWrapper']//ul//li[2]", ByTypes.xpath).click();

	}

	public void clickOSeeScript() throws Exception {
		getSeeTextElement().click();

	}

	protected WebElement getSeeTextElement() throws Exception {
		return webDriver.waitForElement("//li[@id='seeAll']", ByTypes.xpath, false, smallTimeOut);
	}

	public void clickOnHearPart() throws Exception {
		webDriver.waitForElement("//li[@id='part']", ByTypes.xpath).click();

	}

	public void clickOnHearAll() throws Exception {
		getHearAllElement().click();

	}

	private WebElement getHearAllElement() throws Exception {
		return webDriver.waitForElement("all_", ByTypes.id, false, 1);
	}

	public void clickOnKeyWords() throws Exception {
		getKeywordElement().click();

	}

	private WebElement getKeywordElement() throws Exception {
		return webDriver.waitForElement("kw", ByTypes.id,false, 1);
	}

	public void clickOnWordByIndex(int index) throws Exception {
		webDriver.waitForElement("//ul[@id='wordList']//li[" + index + "]", ByTypes.xpath).click();

	}

	public void clickOnToolHear() throws Exception {
		WebElement toolElement = webDriver.waitForElement("//div[@id='wordTools']/ul/li[1]", ByTypes.xpath);

		toolElement.click();

	}

	public void enterAnswerTextByIndex(String index, String text) throws Exception {
		//webDriver.waitForElement("//input[@type='text'][@id='"+index+"']", ByTypes.xpath).sendKeys(text);
		webDriver.waitForElement("prOpenEnded__qaItemText--qId_1_alId_"+index, ByTypes.id).sendKeys(text); //new version of item
	}
	
	public void enterOpenSegmentAnswerTextByIndex(int index, String text) throws Exception {
		getOpenSegmentInboxElementByIndex(index).sendKeys(text);

	}
	
	public void enterOpenSmallSegmentAnswerTextByIndex(int index, String text) throws Exception {
		getOpenSmallSegmentInboxElementByIndex(index).sendKeys(text);

	}
	
	public void enterOpenSmallSegmentAnswerTextByIndex2(String index, String text) throws Exception {
		getOpenSmallSegmentInboxElementByIndex2(index).sendKeys(text);

	}
	
	public void enterOpenSegmentAnswerTextByIndex2(String index, String text) throws Exception {
		getOpenSegmentInboxElementByIndex2(index).sendKeys(text);

	}
	
	
	public void verifyNoTextInputInOpenSegmentByIndex(int index) throws Exception {
		Boolean isInboxEmpty = getOpenSegmentInboxElementByIndex(index).getAttribute("value").isEmpty();
		testResultService.assertEquals(true, isInboxEmpty, "Input section is not empty");
	}
	
	public void verifyNoTextInputInOpenSmallSegmentByIndex(int index) throws Exception {
		Boolean isInboxEmpty = getOpenSmallSegmentInboxElementByIndex(index).getAttribute("value").isEmpty();
		testResultService.assertEquals(true, isInboxEmpty, "Input section is not empty");
	}
	
	public void verifyNoTextInputInOpenSmallSegmentByIndex2(String index) throws Exception {
		Boolean isInboxEmpty = getOpenSmallSegmentInboxElementByIndex2(index).getAttribute("value").isEmpty();
		testResultService.assertEquals(true, isInboxEmpty, "Input section is not empty");
	}
	public void verifyNoTextInputInOpenSegmentByIndex2(String index) throws Exception {
		Boolean isInboxEmpty = getOpenSegmentInboxElementByIndex2(index).getAttribute("value").isEmpty();
		testResultService.assertEquals(true, isInboxEmpty, "Input section is not empty");
	}
	
	public String getTextInputInOpenSegmentByIndex(int index) throws Exception {
		String text = getOpenSegmentInboxElementByIndex(index).getAttribute("value");
		
		return text;
	}
	
	public String getTextInputInOpenSmallSegmentByIndex(int index) throws Exception {
		String text = getOpenSmallSegmentInboxElementByIndex(index).getAttribute("value");
		
		return text;
	}
	
	public String getTextInputInOpenSmallSegmentByIndex2(String index) throws Exception {
		String text = getOpenSmallSegmentInboxElementByIndex2(index).getAttribute("value");
		
		return text;
	}
	
	public String getTextInputInOpenSegmentByIndex2(String index) throws Exception {
		String text = getOpenSegmentInboxElementByIndex2(index).getAttribute("value");
		
		return text;
	}
	
	public void enterFreeWritingTextBySection(String section, String text) throws Exception {
		getWritingInboxElementBySection(section).sendKeys(text);

	}
	
	public void verifyNoTextInputByWritingSection(String section) throws Exception {
		Boolean isInboxEmpty = getWritingInboxElementBySection(section).getAttribute("value").isEmpty();
		testResultService.assertEquals(true, isInboxEmpty, "Input section is not empty");
	}
		
	private WebElement getWritingInboxElementBySection(String section) throws Exception {
		return webDriver.waitForElement("//div[@class='multiTextLinesWrapper']/div["+section+"]//input[@type='text']", ByTypes.xpath);
	}
	
	private WebElement getOpenSegmentInboxElementByIndex(int index) throws Exception {
		
		WebElement indicator = webDriver.waitForElement("nobr", ByTypes.tagName, 1,false, "Search for tag 'nobr'");
		if (indicator!=null){
			return webDriver.waitForElement("//div[contains(@class,'writingEditeInnerWrapper')]/div/nobr["+index+"]//input", ByTypes.xpath);
		} else {
			return webDriver.waitForElement("//div[contains(@class,'writingEditeInnerWrapper')]/div["+index+"]//textarea", ByTypes.xpath);
		}
	}
	
	private WebElement getOpenSmallSegmentInboxElementByIndex(int index) throws Exception {
		return webDriver.waitForElement("//div[contains(@class,'writingEditeInnerWrapper')]//div//nobr["+index+"]//input", ByTypes.xpath);
	}
	private WebElement getOpenSmallSegmentInboxElementByIndex2(String  index) throws Exception {
		WebElement web = webDriver.waitUntilElementAppearsAndReturnElementByType("prOpenEnded__qaItemText--"+index,ByTypes.id,2);
		//return webDriver.waitForElement("//div[@class='prOpenEnded__mainIW']//input[@id='prOpenEnded__qaItemText--qId_1_alId_"+index+"']",ByTypes.xpath);
		return web; //webDriver.waitForElement("//*[@id='prOpenEnded__qaItemText--"+index+"']",ByTypes.xpath);//input[@id ='prOpenEnded__qaItemText--qId_1_alId_1']
		//*[@id='prOpenEnded__qaItemText--prOpenEnded__qaItemText--qId_1_alId_1']
	}
	
private WebElement getOpenSegmentInboxElementByIndex2(String  index) throws Exception {
		
		return webDriver.waitForElement("//*[@id='prOpenEnded__qaItemText--"+index+"']",ByTypes.xpath);
		
	}
	
	public String getLessonNameFromHeader() throws Exception {
		WebElement element = webDriver.waitForElement("//span[@class='textFitted']//div[2]", ByTypes.xpath);

		return webDriver.getElementText(element);
	}

	public void clickPlayNewPlayer() throws Exception {

	}

	public WebElement getPlayPauseButton() throws Exception {
		WebElement element = webDriver.waitForElement("play-pause", ByTypes.id, false, webDriver.getTimeout());

		return element;
	}

	public WebElement getMuteBytton() throws Exception {
		WebElement element = webDriver.waitForElement("mute", ByTypes.id, "Mute button");
		return element;
	}

	public void checkThatSeeExplanationIsDisplayed() throws Exception {
		WebElement element = getSeeExplanation();
		if (element == null) {
			testResultService.addFailTest("See explanation is not displayed", false, true);
		}
		
	}
	
	public void checkThatSeeExplanationIsNotDisplayed() throws Exception {
		//WebElement element = getSeeExplanation();
		//if (element != null) {
			//testResultService.addFailTest("See Explanation displayed when it should not", false, true);
		//}
		boolean actual = checkResourceTool();
		if (actual){
			testResultService.assertEquals(false, actual);
		}
		
	}

	private WebElement getSeeExplanation() throws Exception {
		return webDriver.waitForElement("seeExplanation", ByTypes.id, false, webDriver.getTimeout());
	}

	private boolean checkResourceTool() throws Exception {
		List<WebElement> element = webDriver.getWebDriver().findElements(By.xpath("//*[@id='bgContainer']/div[2]/div[2]/ul"));
		boolean actual = element.isEmpty();
		//return webDriver.waitForElement("seeExplanation", ByTypes.id, false, webDriver.getTimeout());
		if (actual){
			actual = false;
		}
		return actual;
	}
	
	public void checkThatPrintIsDisplayed() throws Exception {
		WebElement element = getPrintElement();
		if (element == null) {
			testResultService.addFailTest("Print is not displayed", false, true);
			
		}
		
	}
	
	public void checkThatPrintIsNotDisplayed() throws Exception {
/*		WebElement element = getPrintElement();
		if (element != null) {
			testResultService.addFailTest("Print displayed when it should not", false, true);
			
		}
*/
		boolean actual = checkResourceTool();
		if (actual){
			testResultService.assertEquals(false, actual);
		}
		
	}
	
	private WebElement getPrintElement() throws Exception {
		return webDriver.waitForElement("printTxt", ByTypes.id, false, webDriver.getTimeout());
	}

	public void checkThatHearAllIsDisplayed() throws Exception {
		WebElement element = getHearAllElement();
		if (element == null){
			testResultService.addFailTest("Hear all is not displayed when it should be", false, true);	
		}
		

	}
	
	public void checkThatHearAllIsNotDisplayed() throws Exception {
		WebElement element = getHearAllElement();
		if (element != null) {
			testResultService.addFailTest("Hear All tool displayed when it should not", false, true);
			
		}
		
	}

	public void checkThatMainIdeaToolIsDisplayed() throws Exception {
		WebElement element = getMainIdeaToolElement();
		if (element == null){
			testResultService.addFailTest("Main idea tool is not displayed", false, true);	
		}
		else {
			testResultService.assertEquals(true, element.isDisplayed(), "Main idea tool is not displayed");
		}
	}
	
	public void checkThatMainIdeaToolIsNotDisplayed() throws Exception {
		WebElement element = getMainIdeaToolElement();
		if (element != null) {
			testResultService.addFailTest("Main Idea displayed when it should not", false, true);
		}

	}

	private WebElement getMainIdeaToolElement() throws Exception {
		return webDriver.waitForElement("mi", ByTypes.id, false, smallTimeOut);
	}
	
	private WebElement getVocabTransToolElement() throws Exception {
		return webDriver.waitForElement("getTransIcon", ByTypes.id, false, smallTimeOut);
	}

	public void checkThatKeywordsToolIsDisplayed() throws Exception {
		WebElement element = getKeywordElement();
		
		if (element == null){
			testResultService.addFailTest("Keyword tool is not displayed", false, true);
		} else{
			testResultService.assertEquals(true, element.isDisplayed(), "Keyword tool is not displayed");	
		}
		

	}
	
	public void checkThatKeywordsToolIsNotDisplayed() throws Exception {
		WebElement element = getKeywordElement();
		if (element != null) {
			testResultService.addFailTest("Keywords tool displayed when it should not", false, true);
		}

	}

	public void checkThatReferenceWordsToolIsDisplayed() throws Exception {
		WebElement element = getReferenceWordElement();
		
		if (element == null) {
			testResultService.addFailTest("Reference tool is not displayed", false, true);
		} else {
			testResultService.assertEquals(true, element.isDisplayed(), "Reference tool is not displayed");	
		}
	}
	
	public void checkThatReferenceWordsToolIsNotDisplayed() throws Exception {
		WebElement element = getReferenceWordElement();
		if (element != null) {
			testResultService.addFailTest("Ref Words tool displayed when it should not", false, true);
		}

	}

	private WebElement getReferenceWordElement() throws Exception {
		return webDriver.waitForElement("ref", ByTypes.id, false, smallTimeOut);
	}

	public void checkThatSeeTextIsDisplayed() throws Exception {
		WebElement element = getSeeTextElement();
		
		if (element == null){
			testResultService.addFailTest("See text tool is not displayed", false, true);
		} else{
			testResultService.assertEquals(true, element.isDisplayed(), "See text tool is not displayed");	
		}
		
	}
	
	public void checkThatSeeTextIsNotDisplayed() throws Exception {
		WebElement element = getSeeTextElement();
		if (element != null) {
			boolean toolNotDisplayed = false;
			testResultService.assertEquals(true, toolNotDisplayed, "See Text tool displayed when it should not");
		}
	}
	
	public void checkThatSeeTextIsDisabled() throws Exception {
		WebElement element = getSeeTextElement();
		if (!element.getAttribute("class").contains("disabled")) {
			boolean toolDisabled = false;
			testResultService.assertEquals(true, toolDisabled, "See Text tool enabled when it should be disabled");
		}
	}
		
	public void checkThatSeeTranslationDisplayed() throws Exception {
		
		boolean isDisplayed = false; 
		if (getSeeTranslationElement() != null) isDisplayed = true;
		testResultService.assertEquals(true, isDisplayed, "See Translation tool is not displayed");
	}
	
	public void checkThatSeeTranslationNotDisplayed() throws Exception {
		WebElement element = getSeeTranslationElement();
		if (element != null) {
			testResultService.addFailTest("See Translation tool displayed when it should not", false, true);
		}
	}
	
	public void checkThatResourceToolsDisabled() throws Exception {
		
		WebElement toolsBar = webDriver.waitForElement("//div[contains(@class,'learning__RATextTools')]", ByTypes.xpath);
		List <WebElement> tools = toolsBar.findElements(By.tagName("li"));
		testResultService.assertEquals(true, toolsBar.getAttribute("class").contains("disabled"), "Resource tools bar not disabled");
		
			for (int i = 0; i < tools.size(); i++) {
				testResultService.assertEquals(true, tools.get(i).getAttribute("class").contains("disabled"), "Resource tool not disabled");
			}
	}
	
	public void checkThatSegmentToolsDisabled() throws Exception {
		
		WebElement wrapperLeft = webDriver.waitForElement("bgContainer", ByTypes.id);
		testResultService.assertEquals(true, wrapperLeft.getAttribute("class").contains("disable_openSpeech"), "Segment tools not disabled");
			
	}

	public void selectTextForRecord(int index) throws Exception {

		getSegmentTextByIndex(index).click();

	}

	private WebElement getSegmentTextByIndex(int index) throws Exception {
		return webDriver.waitForElement(
				"//div[@class='seeTxtContainer']//table//tbody//tr[" + index + "]//td[2]//p//span", ByTypes.xpath);
	}

	public void selectSegmentInReadingByNumber(int paragNumber, int segmentNum, boolean isNewReadingTemplate) throws Exception {
		
		webDriver.waitUntilElementAppears("//div[contains(@class,'readingExploreMainContent')]", 20);
		
		if (isNewReadingTemplate) {
			webDriver.waitForElement(
					"//div[contains(@class,'readingExploreMainContent')]//p[" + paragNumber + "]//span[" + segmentNum + "]",
					ByTypes.xpath).click();
		} else {
		
		webDriver.waitForElement(
				"//div[contains(@class,'readingExploreMainContent')]//p[" + paragNumber + "]//span[" + segmentNum + "]/span",
				ByTypes.xpath).click();
		}

	}

	// public void selectSegmentInReadingExploreByNumber(int segmentNumber)
	// throws Exception {
	// webDriver.waitForElement(
	// "//div[@class='seeTextContainer']//table//tbody//tr["+segmentNumber+"]//td[2]//span",
	// ByTypes.xpath,
	// "Segment in explore reading").click();
	// ;
	// }

	public void selectSegmentInReadingExploreByNumber(int segmentNumber) throws Exception {
		webDriver.waitForElement("//div[@class='readingExploreMainContent']//p[2]//span[" + segmentNumber + "]",
				ByTypes.xpath, "Segment in explore reading").click();
		
	}
	
	public void selectSegmentInSpeakingExploreByNumber(int segmentNumber) throws Exception {
		webDriver.waitForElement("//div[@class='seeTextContainer']//span[" + segmentNumber + "]",
				ByTypes.xpath, "Segment in explore speaking").click();
		
	}

	public void clickOnSeeTranslationTool() throws Exception {
		getSeeTranslationElement().click();

	}

	public String getSeeTranslationText() throws Exception {
		String text = webDriver.waitForElement("keywordExampleText", ByTypes.className, "See Translation").getText();
		return text;
	}

	public void closeSeeTranslationTool() throws Exception {
		//WebElement closeButton = webDriver.waitForElement("closeBtn", ByTypes.className, "See Trans Bubble Close Button");
		//WebElement closeButton = webDriver.waitForElement("//div[contains(@class, 'closeBtn')]/a", ByTypes.xpath, "See Trans Bubble Close Button");
		//WebElement closeButton = webDriver.waitForElement("//*[@id='mainAreaTD']/div[3]/div[1]/div[2]/a", ByTypes.xpath, "See Trans Bubble Close Button");
		WebElement closeButton = webDriver.waitForElement("//div[contains(@class,'componentPanelHeader')]//a", ByTypes.xpath, "See Trans Bubble Close Button");	
		closeButton.click();
		
	}
		
	private WebElement getSeeTranslationBubble() throws Exception {
		WebElement element = webDriver.waitForElement("componentPanelContent", ByTypes.className, false,
				smallTimeOut);
		return element;
	}

	public boolean checkIfSeeTranslationBubbleDisplayed() throws Exception {

		WebElement element = getSeeTranslationBubble();

		if (element == null) {
			return false;
		} else

			return element.isDisplayed();

	}

	public void checkThatHearPartIsDisplayed() throws Exception {
		WebElement element = getHearPartElement();
		if (element == null) {
			testResultService.addFailTest("Hear Part tool not displayed", false, true);
		}
	}
	
	public void checkThatHearPartIsNotDisplayed() throws Exception {
		WebElement element = getHearPartElement();
		if (element != null) {
			testResultService.addFailTest("Hear Part tool displayed when it should not", false, true);
		}

	}

	private WebElement getHearPartElement() throws Exception {
		return webDriver.waitForElement("hearPartIcon", ByTypes.id,  1, false,"Hear Part tool not found");
	}

	public void checkThatRecordYourselfIsDisplayed() throws Exception {
		WebElement element = getRecordYourselfTool();

		testResultService.assertEquals(true, element.isDisplayed(), "Record yourself tool is not displayed");

	}

	private WebElement getRecordYourselfTool() throws Exception {
		return webDriver.waitForElement("SRIcon", ByTypes.id,  smallTimeOut, false, "Record yourself tool is not displayed");
	}

	public void clickOnSeeText() throws Exception {
		getSeeTextElement().click();

	}

	public void clickOnRecordYourselfSegmentTool() throws Exception {
		getRecordYourselfTool().click();

	}

	public void switchToRecordPanel() throws Exception {
		//webDriver.switchToFrame(2);
		webDriver.switchToFrame(webDriver.waitForElement("cboxIframe", ByTypes.className));

	}

	public void checkThatTextDisplayed(boolean expected) throws Exception {
		if (getTextElementExistance()!= null)
		{
			boolean actual = true;
			testResultService.assertEquals(expected, actual, "There is problem with the resource text");
		}
	}
	
	private WebElement getTextElementExistance() throws Exception {
		
		return webDriver.waitForElement("//div[@class='seeTxtContainer']//table//tbody//tr[1]//td[2]//p//span[1]", ByTypes.xpath, false, 2);
	}
	
	public void checkThatTextIsDisplayed() throws Exception {
		WebElement element = getTextElement();
		if (element == null) {
			testResultService.addFailTest("Text is not displayed", false, true);
		}
		
	}
	
	public String checkThatReadingTextIsDisplayed() throws Exception {
		WebElement element = getReadingTextElement();
		if (element == null) {
			testResultService.addFailTest("Text is not displayed", false, true);
		}
		return element.getText();
	}

	private WebElement getTextElement() throws Exception {
		return webDriver.waitForElement("//div[@class='seeTxtContainer']//table//tbody//tr[1]//td[2]//p//span[1]",
				ByTypes.xpath, false, webDriver.getTimeout());
	}
	
	private WebElement getReadingTextElement() throws Exception {
		return webDriver.waitForElement("//div[contains(@class,'readingExploreWrapper')]", ByTypes.xpath, false, smallTimeOut);
	}

	public void checkThatTextIsNotDisplayed() throws Exception {
		if (getTextElement() != null) {
			testResultService.addFailTest("Text was displayed while it should have been hidden", false, true);
		}

	}

	public void checkSeeTextDisplayed(boolean expected) throws Exception {
		boolean actual = true;
		if (getSeeTextElement() == null) actual = false;
		testResultService.assertEquals(expected, actual, "See Text btn visibility not as expected");
	}
		
	public void clickOnSeeExplanation() throws Exception {
		webDriver.clickOnElement(getSeeExplanation());

	}

	public void checkThatSeeExplanationIsOpen() throws Exception {
		// webDriver
		// .waitForElement(
		// "//div[@class='modal learning__SeeExp
		// in']//div//div//div[2]//div//center//span",
		// ByTypes.xpath).isDisplayed();
		webDriver.waitForElement("//div[contains(@class,'learning__SeeExp')]", ByTypes.xpath).isDisplayed();

	}

	public void clickOnMainIdea() throws Exception {
		getMainIdeaToolElement().click();

	}
	
	public void clickOnVocabTransTool() throws Exception {
		getVocabTransToolElement().click();

	}

	public void checkThatMainIdeaIsHighlighted(String stratsWith, String endsWith) throws Exception {
		WebElement element = webDriver.waitForElement(
				"//div[@class='readingExploreMainContent']//p[3]//span[contains(@class,'mi')]", ByTypes.xpath); // //div[@class='readingExploreMainContent']//p[3]//span[contains(@class,'active')]
		
		testResultService.assertEquals(true, element.getAttribute("class").contains("active"), "Main Idea is Highlighted");

		String mainIdeaText = element.getText();
		boolean startWith = mainIdeaText.startsWith(stratsWith);
		testResultService.assertEquals(true, mainIdeaText.startsWith(stratsWith));

		testResultService.assertEquals(true, mainIdeaText.endsWith(endsWith));

	}

	public void checkThatMainIdeaIsNotHighlighted(String stratsWith, String endsWith) throws Exception {
		WebElement element = webDriver
				.waitForElement("//div[@class='readingExploreMainContent']//p[3]//span[contains(@class,'mi')]", ByTypes.xpath); // //div[@class='readingExploreMainContent']//p[3]//span[@class='mi']
		
		testResultService.assertEquals(false, element.getAttribute("class").contains("active"), "Main Idea is Highlighted");
		
		String mainIdeaText = element.getText();
		boolean startWith = mainIdeaText.startsWith(stratsWith);
		testResultService.assertEquals(true, mainIdeaText.startsWith(stratsWith));

		testResultService.assertEquals(true, mainIdeaText.endsWith(endsWith));

	}

	public void checkThatSpecificKeyWordIsHighlighted(String keyword) throws Exception {
		WebElement element = webDriver.waitForElement("//span[contains(@class,'kw')][text()='" + keyword + "']",
				ByTypes.xpath);
		
		testResultService.assertEquals(true, element.getAttribute("class").contains("active"), "Key word is Highlighted");

		testResultService.assertTrue("Text was not found/not highlighted", element != null);

	}

	public void clickOnWordByText(String wordText) throws Exception {
		webDriver.waitForElement("//span[text()='" + wordText + "']", ByTypes.xpath).click();
		;
	}

	public void checkThatKeyWordOpupOpens(String keyWord) throws Exception {
		WebElement element = webDriver.waitForElement("//*[@id='kwStruct']/h5", ByTypes.xpath);

		testResultService.assertEquals(keyWord, element.getText());

	}

	public void closeKeyWorPopup() throws Exception {
		webDriver.waitForElement("//div[@class='closeBtn']//a", ByTypes.xpath).click();

	}

	public void checkThatSpecificKeyWordIsNotHighlighted(String keyWord) throws Exception {
		WebElement element = webDriver.waitForElement("//span[contains(@class,'kw')][text()='"+keyWord+"']", ByTypes.xpath, false, webDriver.getTimeout()); // //span[@class='kw'][text()='" + keyWord + "']
		
		testResultService.assertEquals(false, element.getAttribute("class").contains("active"), "Key word is Highlighted");
		testResultService.assertTrue("Text was not found/not highlighted", element != null);

	}

	public void clickOnRecordYourselfVocab() throws Exception {
		webDriver.waitForElement("//td[@id='tempLink2']//span", ByTypes.xpath).click();

	}

	public void clickOnReferenceWords() throws Exception {
		getReferenceWordElement().click();

	}

	public void clickOnLogoutLearningArea() throws Exception {
		webDriver.waitForElement("//li[contains(@class,'settingsMenu__personal')]", ByTypes.xpath).click();
		webDriver.waitForElement("Logout", ByTypes.linkText).click();
		//getLogOutElement().click();
		webDriver.sleep(3);
		webDriver.switchToFrame(webDriver.waitForElement("//div[@id='EdoFrameBoxContent']/iframe[1]", ByTypes.xpath));
		webDriver.sleep(2);
		webDriver.waitForElement("btnOk", ByTypes.name).click();
		webDriver.switchToMainWindow();
		
	}

	private WebElement getLogOutElement() throws Exception {
		return webDriver.waitForElement("//div[@id='logout']//a", ByTypes.xpath);
	}

	public boolean checkThatTaskIsVisited(int taskIndex) throws Exception {
		return validateTaskStatus(taskIndex, "half");

	}

	public boolean validateTaskStatus(int taskIndex, String expectedStatus) throws Exception {
		WebElement element = webDriver.waitForElement(
				"//div[@index='" + taskIndex + "']//div//div[contains(@class,'" + expectedStatus + "')]", ByTypes.xpath,
				webDriver.getTimeout(), false, "Task in progress");
		
		
		if (element != null) {
			return true;
		} else {
			testResultService.addFailTest("Task did not have visited status");
			webDriver.printScreen();
			return false;
		}
	}

	public void chechThatTaskIsNotVisited(int index) throws Exception {
		validateTaskStatus(index, "empty");

	}

	public void clickOnVocabWordByIndex(int index) throws Exception {
		webDriver.waitForElement("//div[@class='fdVocListWrapper']//table//tbody//tr[" + index + "]//td", ByTypes.xpath)
				.click();

	}

	public void validateLessonListUnitImage(String unitId) throws Exception {
		WebElement element = webDriver.waitForElement("//div[@class='learning__courseListItemsImageW']", ByTypes.xpath, true, 15);
		String style = element.getAttribute("style");
		testResultService.assertTrue("Image not found", style.contains("/Runtime/Graphics/Units/U_" + unitId + ".jpg"));

	}

	public void clickOnPlaMediaByIndex(String index) throws Exception {
		webDriver.waitForElement("//a[@class='clickMedia headphonesBt'][@id='" + index + "']", ByTypes.xpath).click();

	}

	public void dragAndDropAnswerFromPractice(String dataId, int questionId) throws Exception {
		WebElement answer = webDriver
				.waitForElement("//div[@class='draggable wordBankTile'][@data-id='" + dataId + "']", ByTypes.xpath);

		WebElement question = webDriver.waitForElement("//div[@class='TextDiv']//span[" + questionId + "]",
				ByTypes.xpath);

		webDriver.dragAndDropElement(answer, question);

	}

	public void dragAndDropAnswerFromPracticeBackToAnswerBank(String dataId) throws Exception {
		WebElement answer = webDriver
				.waitForElement("//div[@class='draggable wordBankTile'][@data-id='" + dataId + "']", ByTypes.xpath);
		WebElement target = webDriver
				.waitForElement("//div[@class='draggable wordBankTile'][@data-id='" + dataId + "']", ByTypes.xpath);

		webDriver.dragAndDropElement(answer, target);

	}

	public void openDropDown(int questonId) throws Exception {
		webDriver.waitForElement("//div[@class='fitbSentence']//span//div[2]", ByTypes.xpath).click();
		;

	}

	public void clickOnTasksNext() throws Exception {
		webDriver.waitForElement("//div[@id='forTaskPageBtn']", ByTypes.xpath).click();

	}

	/**
	 * 1=left 3=right
	 * 
	 * @param i
	 * @throws Exception
	 */
	public void selectInteractSpeaker(int index) throws Exception {
		webDriver.waitForElement("//div[@class='bgImgContainerWrapper']//div[" + index + "]//a", ByTypes.xpath).click();

	}

	public void clickOnInteractStartButton() throws Exception {
		webDriver.waitForElement("Start", ByTypes.linkText).click();

	}

	public void checkVocabularySelectionSync(int index, String letter, String word) throws Exception {
		webDriver.waitForElement("//tbody[@id='oTBody']/tr[" + index + "]/td", ByTypes.xpath).click();
		String actualLetter = webDriver.waitForElement("//tbody[@id='oTBody']/tr[" + index + "]/td", ByTypes.xpath)
				.getText();
		String actualWord = webDriver.waitForElement("//td[@class='VocTarget']/div", ByTypes.xpath).getText();
		testResultService.assertEquals(letter, actualLetter, "String in the list is wrong");
		testResultService.assertEquals(word, actualWord, "String in the target above image is wrong");
	}

	public void checkNewVocabularySelectionSync(int index, String word) throws Exception {
		webDriver.waitForElement("//div[@class='termsSelectionWrapper']/ul/li[" + index + "]/a", ByTypes.xpath).click();

		String actualWordList = webDriver
				.waitForElement("//div[@class='termsSelectionWrapper']/ul/li[" + index + "]/a", ByTypes.xpath)
				.getText();
		String actualWordTarget = webDriver.waitForElement("//div[@class='left']/h3", ByTypes.xpath).getText();
		testResultService.assertEquals(word, actualWordList, "String in the list is wrong");
		testResultService.assertEquals(actualWordTarget, actualWordList, "String in the target above image is wrong");
	}

	public void checkNewVocabularySelectionSyncInPractice(int index, String word) throws Exception {

		webDriver.selectElementFromComboBoByIndex("wordList", index);
		// String actualWordList =
		// webDriver.waitForElement("//*[@id='wordList']/option["+index+"]",
		// ByTypes.xpath).getText();
		String actualWordTarget = webDriver.waitForElement("//div[@class='left']/h3", ByTypes.xpath).getText();
		// testResultService.assertEquals(word, actualWordList, "String in the
		// list is wrong");
		testResultService.assertEquals(word, actualWordTarget, "String in the target above image is wrong");
	}

	public void checkThatRecordYourselfIsDisplayedInOldVocabulary() throws Exception {
		WebElement element = getRecordYourselfToolInOldVoc();
		testResultService.assertEquals(true, element.isDisplayed(), "Record yourself tool is not displayed");

	}

	private WebElement getRecordYourselfToolInOldVoc() throws Exception {
		//return webDriver.waitForElement("tempLink2", ByTypes.id);
		return webDriver.waitUntilElementAppearsAndReturnElementByType("tempLink2", ByTypes.id, 5);
	}

	public void checkThatSeeTransToolIsDisplayedInOldVocabulary() throws Exception {
		WebElement element = getSeeTransToolInOldVoc();
		testResultService.assertEquals(true, element.isDisplayed(), "See Translation tool is not displayed");

	}

	public void checkThatSeeTransToolIsNotDisplayedInOldVocabulary() throws Exception {
		if (getSeeTransToolInOldVoc() != null) {
			testResultService.addFailTest("See Translation was displayed while it should have been hidden", false,
					true);
		}
	}
	
	public void clickOnSeeTransToolInOldVocabulary() throws Exception {
		getSeeTransToolInOldVoc().click();
		
	}

	private WebElement getSeeTransToolInOldVoc() throws Exception {
		// return webDriver.waitForElement("//span[@title=' See Translation']",
		// ByTypes.xpath, false, webDriver.getTimeout());
		return webDriver.waitForElement("tempLink1", ByTypes.id, false, smallTimeOut);
	}

	public void checkThatRecordYourselfIsDisplayedInNewVocabulary() throws Exception {
		WebElement element1 = getRecordYourselfToolInNewVocWord();
		testResultService.assertEquals(true, element1.isDisplayed(), "Record yourself tool is not displayed Near Word");

		WebElement element2 = getRecordYourselfToolInNewVocExam();
		testResultService.assertEquals(true, element2.isDisplayed(),
				"Record yourself tool is not displayed Near Example");

	}

	private WebElement getRecordYourselfToolInNewVocWord() throws Exception {
		return webDriver.waitForElement("//div[@id='wordTools']/ul/li[2]", ByTypes.xpath);

	}

	private WebElement getRecordYourselfToolInNewVocExam() throws Exception {
		return webDriver.waitForElement("//div[@id='examTools']/ul/li[2]", ByTypes.xpath);

	}

	public void checkThatHearIsDisplayedInNewVocabulary() throws Exception {
		WebElement element1 = getHearToolInNewVocWord();
		testResultService.assertEquals(true, element1.isDisplayed(), "Record yourself tool is not displayed Near Word");

		WebElement element2 = getHearToolInNewVocExam();
		testResultService.assertEquals(true, element2.isDisplayed(),
				"Record yourself tool is not displayed Near Example");

	}

	private WebElement getHearToolInNewVocWord() throws Exception {
		return webDriver.waitForElement("//div[@id='wordTools']/ul/li[1]", ByTypes.xpath);

	}

	private WebElement getHearToolInNewVocExam() throws Exception {
		return webDriver.waitForElement("//div[@id='examTools']/ul/li[1]", ByTypes.xpath);

	}

	public void checkThatDefaultImageIsDisplayedInNewVocabularyTest() throws Exception {
		WebElement element1 = webDriver.waitForElement("//div[@class='leftSideImageDefault']", ByTypes.xpath);
		testResultService.assertEquals(true, element1.isDisplayed(), "Default Image is not displayed Near Word");
	}
	
	public void selectAnswerRadioByTextNum (int questionIndex, int answerNum) throws Exception {
		
		//webDriver.waitForElement("//div[@id='q"+questionIndex+"a"+answerNum+"']/div/div/div[1]/label", ByTypes.xpath).click(); // this works in FF
		//verifyAnswerRadioSelected(questionIndex, answerNum);
		
		if (webDriver.getBrowserName().equals("chrome")) {
			webDriver.waitForElement("//div[@id='q"+questionIndex+"a"+answerNum+"']/div/div/div/input", ByTypes.xpath).click();
		} else {
			webDriver.waitForElement("//div[@id='q"+questionIndex+"a"+answerNum+"']/div/div/div/label", ByTypes.xpath).click();
		}
		
		
	
	}
	
	public void selectAnswerRadioByImage (int questionIndex, int answerNum) throws Exception {
				
			webDriver.waitForElement("//div[@id='q"+questionIndex+"a"+answerNum+"']/following-sibling::div/img", ByTypes.xpath).click();
			
	}
	
	
	
	
	public void checkAllAnswersUnselectedMCQ() throws Exception {
				
		WebElement element = webDriver.waitForElement("//div[contains(@class,'check')][contains(@id,'q')][contains(@id,'a')]", ByTypes.xpath, smallTimeOut, false, "Check Answers Not Selected");
		if (element != null) {
			testResultService.addFailTest("Answer/s Still Selected", false, true);
		}
	}
	
	public void checkSingleAnswerUnselectedMCQ(int questionIndex, int answerNum) throws Exception {
		
		WebElement element = webDriver.waitForElement("//div[contains(@class,'check')][@id='q"+questionIndex+"a"+answerNum+"']", ByTypes.xpath,	1, false, "Check Answer Not Selected");
		if (element != null) {
			testResultService.addFailTest("Answer/s Still Selected", false, true);
		}
	}
	
	public void checkSingleAnswerSelectedMCQ(int questionIndex, int answerNum) throws Exception {
		
		WebElement element = webDriver.waitForElement("//div[contains(@class,'check')][@id='q"+questionIndex+"a"+answerNum+"']", ByTypes.xpath,1,false);
		if (element == null) {
			testResultService.addFailTest("Answer/s Is Not Selected", false, true);
		}
	}
	
	public void verifyPracticeToolsStateOnClear() throws Exception {
		
		verifyPracticeToolState("SeeAnswer", "enabled");
		verifyPracticeToolState("CheckAnswer", "enabled");
		verifyPracticeToolState("Restart", "enabled");
		
	}
	
	public void verifyPracticeToolsStateOnSeeAnswer() throws Exception {
		verifyPracticeToolState("SeeAnswer", "selected");
		verifyPracticeToolState("CheckAnswer", "disabled");
		verifyPracticeToolState("Restart", "enabled");
	
	}
	
	public void verifyPracticeToolsStateOnCheckAnswer() throws Exception {
		
		verifyPracticeToolState("SeeAnswer", "disabled");
		verifyPracticeToolState("CheckAnswer", "selected");
		verifyPracticeToolState("Restart", "enabled");
	
	}
		
	private void verifyPracticeToolState(String toolId, String validState) throws Exception {
		
		WebElement element = null;
		if (toolId == "Restart"){
			//element = webDriver.waitForElement("//li[@id='"+toolId+"'][@class='group push']", ByTypes.xpath, false,webDriver.getTimeout());
			element = webDriver.waitForElement("//li[@id='"+toolId+"']", ByTypes.xpath, false,smallTimeOut);
		
		}	else if (validState == "enabled" && toolId != "Restart"){
				element = webDriver.waitForElement("//li[@id='"+toolId+"'][contains(@class,'group')]", ByTypes.xpath, false,smallTimeOut);
			}	
		
		else 
			element = webDriver.waitForElement("//li[@id='"+toolId+"'][contains(@class,'"+validState+"')]", ByTypes.xpath, false,smallTimeOut);
				
		if (element == null) {
			testResultService.addFailTest("Practice Tool: "+toolId+" State is Wrong", false, true);
		}
	}
	
	public void verifyAnswerRadioSelectedCorrect (int questionIndex, int answerNum) throws Exception {
				
		WebElement element = webDriver.waitForElement("//div[contains(@class,'vCheck')][@id='q"+questionIndex+"a"+answerNum+"']", ByTypes.xpath);
		testResultService.assertEquals(true, element.isDisplayed(),	"Answer is Not Selected");
	}
	
	public void verifyAnswerRadioSelectedWrong (int questionIndex, int answerNum) throws Exception {
		
		WebElement element = webDriver.waitForElement("//div[contains(@class,'xCheck')][@id='q"+questionIndex+"a"+answerNum+"']", ByTypes.xpath);
		testResultService.assertEquals(true, element.isDisplayed(),	"Answer is Not Selected");
	}
	
	public void verifySRAnswerSelectedWrong (int questionIndex, int answerNum) throws Exception {
		
		WebElement element = webDriver.waitForElement("//div[@id='q"+questionIndex+"a"+answerNum+"']/div[contains(@class,'incorrect')]", ByTypes.xpath);
		testResultService.assertEquals(true, element.isDisplayed(),	"Answer is Not Marked by X");
	}
	
	public void verifySRAnswerSelectedCorrect (int questionIndex, int answerNum) throws Exception {
		
		WebElement element = webDriver.waitForElement("//div[@id='q"+questionIndex+"a"+answerNum+"']/div[contains(@class,'correct')]", ByTypes.xpath);
		testResultService.assertEquals(true, element.isDisplayed(),	"Answer is Not Marked by V");
	}
	
	public void checkAnswersUnselectedFillTheBlanks(String questonId, String answerText ) throws Exception {
		WebElement element = webDriver.waitForElement("//span[@id='" + questonId + "']//div[text()[contains(.,'" + answerText + "')]]", ByTypes.xpath,	1, false, "Check Answers Not Selected");
		if (element != null) {
			testResultService.addFailTest("Answer/s Still Selected", false, true);
		}
	}
	
	public void checkAnswersSelectedFillTheBlanks(String questonId, String answerText ) throws Exception {
		WebElement element = webDriver.waitForElement("//span[@id='" + questonId + "']//div[text()[contains(.,'" + answerText + "')]]", ByTypes.xpath);
		if (element == null) {
			testResultService.addFailTest("Answer/s Not Selected", false, true);
		}
	}
	
	public void verifyAnswerFillTheBlankSelectedWrong (String questonId) throws Exception {
		
		WebElement element = webDriver.waitForElement("//span[@id='" + questonId + "'][contains(@class,'xCheck')]", ByTypes.xpath);
		testResultService.assertEquals(true, element.isDisplayed(),	"Answer is Not Selected Wrong");
	}
	
	public void verifyAnswerFillTheBlankSelectedCorrect (String questonId) throws Exception {
		
		WebElement element = webDriver.waitForElement("//span[@id='" + questonId + "'][contains(@class,'vCheck')]", ByTypes.xpath);
		testResultService.assertEquals(true, element.isDisplayed(),	"Answer is Not Selected Wrong");
	}
	
	public void verifyNoTextInputOpenEndedByIndex(String index, String text) throws Exception {
		//WebElement element = webDriver.waitForElement("//input[@type='text'][@id='"+index+"'][@value='"+text+"']", ByTypes.xpath,	1, false, "Check No Text Inserted");
		WebElement element = webDriver.waitForElement("//input[@type='text'][@id='"+index+"'][text()='"+text+"')]", ByTypes.xpath,	1, false, "Check No Text Inserted");
		if (element != null) {
			testResultService.addFailTest("Text Still Inputed", false, true);
		}
		
	}
	
	public void verifyAnswerOpenEndedWrong (int questonId) throws Exception {
		List<WebElement> el = webDriver.getElementsByXpath("//*[@class='prOpenEnded__qaItem']"); //webDriver.waitForElement("prOpenEnded__qaItem", ByTypes.xpath);
		try {
		WebElement answerX = el.get(questonId).findElement(By.className("prOpenEnded__qaItem_inputW--xCheck"));
		textService.assertTrue("", answerX!=null);
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}catch(AssertionError e) {
			System.out.println(e.getMessage());
		}
		//webDriver.waitForElement("//div[@id='res"+questonId+"'][@class='traceXVCheck xCheck']", ByTypes.xpath, "Verify X check placed for wrong answer");
	}
	
	public void verifyAnswerOpenEndedCorrect (int questonId) throws Exception {
		List<WebElement> el = webDriver.getElementsByXpath("//*[@class='prOpenEnded__qaItem']"); //webDriver.waitForElement("prOpenEnded__qaItem", ByTypes.xpath);
		try {
		WebElement answerV = el.get(questonId).findElement(By.className("prOpenEnded__qaItem_inputW--vCheck"));
		textService.assertTrue("", answerV!=null);
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}catch(AssertionError e) {
			System.out.println(e.getMessage());
		}
		//webDriver.waitForElement("//div[@id='res"+questonId+"'][@class='traceXVCheck vCheck']", ByTypes.xpath, "Verify V check placed for correct answer");
		
		//webDriver.waitForElement("prOpenEnded__qaItem_inputW prOpenEnded__qaItem_inputW--vCheck", ByTypes.className, "Verify V check placed for correct answer");
	}
	
	public void verifyAnswerOpenEndedCorrect2 (String questonId) throws Exception {
		
		//webDriver.waitForElement("//div[@id='mCSB_5_container']//div[@class='prOpenEnded__qaItem_inputW prOpenEnded__qaItem_inputW--vCheck']", ByTypes.xpath, "Verify V check placed for correct answer");
		webDriver.waitForElement("//*[@class='prOpenEnded__qaItem_inputW prOpenEnded__qaItem_inputW--vCheck']", ByTypes.xpath, "Verify V check placed for correct answer");
	
	}
	
	public void verifyAnswerOpenEndedCorrectNew (String questonId, String textArea) throws Exception {
		//webDriver.waitForElement("//div[@id='mCSB_5_container']//div[@class='prOpenEnded__qaItem_inputW prOpenEnded__qaItem_inputW--vCheck']", ByTypes.xpath, "Verify V check placed for correct answer");
		webDriver.waitForElement("//*[@class='prOpenEnded__qaItem "+textArea+"']", ByTypes.xpath, "Verify V check placed for correct answer");
	
	}
	
	public void verifySmallAnswerOpenEndedCorrect2 (String questonId) throws Exception {
		//webDriver.waitForElement("//div[@id='mCSB_5_container']//div[@class='prOpenEnded__qaItem_inputW prOpenEnded__qaItem_inputW--vCheck']", ByTypes.xpath, "Verify V check placed for correct answer");
		webDriver.waitForElement("//*[@class='prOpenEnded__qaItem_inputW prOpenEnded__qaItem_inputW--vCheck']", ByTypes.xpath, "Verify V check placed for correct answer");
	
	}
	
	public void verifySmallAnswerOpenEndedWrong2 (String questonId) throws Exception {
		//webDriver.waitForElement("//div[@id='mCSB_5_container']//div[@class='prOpenEnded__qaItem_inputW prOpenEnded__qaItem_inputW--vCheck']", ByTypes.xpath, "Verify V check placed for correct answer");
		webDriver.waitForElement("//*[@class='prOpenEnded__qaItem_inputW prOpenEnded__qaItem_inputW--xCheck']", ByTypes.xpath, "Verify V check placed for correct answer");
	
	}
	public void verifyAnswerOpenEndedWrong2 (String questonId) throws Exception {
		webDriver.waitForElement("//*[@class='prOpenEnded__qaItem_inputW prOpenEnded__qaItem_inputW--xCheck']", ByTypes.xpath, "Verify V check placed for correct answer");
		
	}
	
	public void clickOnFooterArrowBtn () throws Exception {
		webDriver.waitForElement("//div[contains(@class,'footerHandle')]", ByTypes.xpath).click();
	}
	
	public void verifyFooterLinksNotDisplayed () throws Exception {
		WebElement element = webDriver.waitForElement("About Edusoft", ByTypes.linkText, 1, false, "Check Footer Links Not Displayed");
		if (element != null) {
			testResultService.addFailTest("Footer Links Still Displayed", false, true);	
		}
	}
	
	public void closeFooterByClickOutside () throws Exception {
		webDriver.waitForElement("//div[contains(@class,'footerOverlay')]", ByTypes.xpath).click();
	}
	
	public boolean verifyEdLogoDisplayedInFooter() throws Exception {
		boolean imageFound;
		WebElement element = getEdLogoElement();
		if (element.isDisplayed() == false) {
			imageFound = false;
		} else {
			imageFound = webDriver.isImageLoaded(element);
		}
		if (imageFound) {
			return true;
		} else {
			webDriver.printScreen("Image not found");
			return false;
		}

	}
	
	public void verifyImageOnlyResourceDisplayedInTask(String imageName) throws Exception {
		
		boolean imageFound;
				
		WebElement element = getImageOnlyElement();
		imageFound = element.findElement(By.className("imageOnlyEl")).getAttribute("style").contains(imageName);
				
		if (element.isDisplayed() != true) imageFound = false;
				
		if (!imageFound) testResultService.addFailTest("Image not found");
				
	}
	
	public void verifyAudioImageResourceDisplayedInTask(String imageName) throws Exception {
		
		boolean imageFound;
				
		WebElement element = getAudioImageElement();
		imageFound = element.getAttribute("src").contains(imageName);
				
		if (element.isDisplayed() != true) imageFound = false;
				
		if (!imageFound) testResultService.addFailTest("Image not found");
				
	}
	
	public void verifyImageOnlyTooltip(String expected) throws Exception {
		
		String actual = getImageOnlyElement().getAttribute("title");
		testResultService.assertEquals(expected, actual, "Title text is not valid");
				
	}

	public WebElement getEdLogoElement() throws Exception {
		WebElement element = webDriver.waitForElement(FOOTER_ED_LOGO_XPATH,
				ByTypes.xpath);
		return element;
	}
	
	public WebElement getImageOnlyElement() throws Exception {
		WebElement element = webDriver.waitForElement("imageOnlyElW", ByTypes.className, "Image Only element not found");
		return element;
	}
	
	public WebElement getAudioImageElement() throws Exception {
		WebElement element = webDriver.waitForElement("imgSpkImgWrap", ByTypes.id, "Audio image element not found");
		return element;
	}
	
	public void checkThatPlayButtonIsDisplayed() throws Exception {
		WebElement element = webDriver.waitForElement("CTrackerPlayBtn", ByTypes.id);
		testResultService.assertEquals(true, element.isDisplayed(), "Play button is not displayed");
	}
		
	public void verifyMyProfileLinkIsDisabled () throws Exception {
		WebElement element = webDriver.waitForElement("//ul[contains(@class,'settingsMenu__listItemW_personal')]//li[1][contains(@class,'disabled')]", ByTypes.xpath, webDriver.getTimeout(),false);
		if (!element.getAttribute("class").contains("disabled")) {
			testResultService.addFailTest("My Profile link enabled when it should not", false, true);
		}
			
		
	}
	
	public void verifyStudyPlannerLinkIsDisabled () throws Exception {
		WebElement element = webDriver.waitForElement("settingsMenu__listItem_studyPlanner", ByTypes.id, smallTimeOut,false);
		if (!element.getAttribute("class").contains("disabled")) {
			testResultService.addFailTest("Study Planner link enabled when it should not", false, true);
		}
	}
	
	public void verifyOnlineSessionLinkIsDisabled () throws Exception {
		WebElement element = webDriver.waitForElement("settingsMenu__listItem_onlineSessions", ByTypes.id,false,2);
		if (!element.getAttribute("class").contains("disabled")) {
			testResultService.addFailTest("Online Session link enabled when it should not", false, true);
		}
		
		// Verify it's not clickable
		element.click();
		
		WebElement session = webDriver.waitForElement("//*[@id='mainCtrl']/div[3]/div/div/div[1]/h2", ByTypes.xpath, false, smallTimeOut);
		if (session != null){
			testResultService.addFailTest("Online Session link is clickable and opened", false, true);
			webDriver.waitForElement("//*[@id='mainCtrl']/div[3]/div/div/div[1]/a", ByTypes.xpath, false, 2).click();
		}
	}
	
	public void verifyNotificationsLinkIsDisabled () throws Exception {
		WebElement element = webDriver.waitForElement("settingsMenu__listItem_NotificationCenter", ByTypes.id, smallTimeOut,false);
		
		if (!element.getAttribute("class").contains("disabled")) {
			testResultService.addFailTest("Notifications link enabled when it should not", false, true);
		}

		// Verify it's not clickable
		element.click();

		WebElement notifications = webDriver.waitForElement("home__marketingSlideOW_0", ByTypes.id, false, smallTimeOut);
		if (notifications != null){
			testResultService.addFailTest("Notification is clickable and opened", false, false);
			notifications.click();
		}
	}
	
	public void verifyWalkthroughLinkIsDisabled () throws Exception {
		//WebElement element = webDriver.waitForElement("//ul[contains(@class,'settingsMenu__listItemW_help')]//li[1][contains(@class,'disabled')]", ByTypes.xpath, smallTimeOut,false);
		WebElement element = webDriver.waitForElement("//*[@id='settingsMenu__listItem_OnBoarding']",ByTypes.xpath,false,smallTimeOut);
		if (!element.getAttribute("class").contains("disabled")) {
			testResultService.addFailTest("Walkthrough link enabled when it should not", false, true);
		}
	}
	

	public void VerificationOfQuestionInstruction(String expectedString) throws Exception {
		
		String actual = webDriver.waitForElement("questionInstructions", ByTypes.className, true, 15).getText();
		testResultService.assertEquals(expectedString ,actual , "The question instruction was displayed incorrectly");
	}
	
	public void checkSelectTextTaskInstruction(String expectedString) throws Exception {
		
		String actual = webDriver.waitForElement("learning__PAQuestionText", ByTypes.className, true, 15).getText();
		testResultService.assertEquals(expectedString ,actual , "The question instruction was displayed incorrectly");
	}

	public void RetrieveMemoryGameCards() throws Exception {
		
		memoryGameCards = webDriver.waitForElement("GameBoardUl", ByTypes.id, true, 15).findElements(By.tagName("li"));
	}

	public void SelectTwoCorrectCards() throws Exception {
		
		SelectRandomGameCardID();
		webDriver.waitForElement("GameBoardUl", ByTypes.id, false, smallTimeOut).findElement(By.id(Integer.toString(cardID))).click();
		
		if (cardID%2 == 0) {
			webDriver.waitForElement("GameBoardUl", ByTypes.id, false, smallTimeOut).findElement(By.id(Integer.toString(cardID + 1))).click();
		} else {
			webDriver.waitForElement("GameBoardUl", ByTypes.id, false, smallTimeOut).findElement(By.id(Integer.toString(cardID - 1))).click();
		}
	}
	
	public void SelectTwoCorrectCardsAndPressOnHeadphone() throws Exception {
		
		SelectTwoCorrectCards();		
		webDriver.waitForElement("//div[@class='MemoGCardcontainer'][@id='"+cardID+"']/input", ByTypes.xpath).click();
	}

	public void SelectRandomGameCardID() {
		Random random = new Random();
		int min = 2;
		int max = 13;
		cardID = random.nextInt((max - min) + 1) + min;
	}

	public void VerifyTwelveGameCardPresent() throws Exception {
		
		testResultService.assertTrue("The displayed card amount was not equal to twelve", memoryGameCards.size() == 12);
		for (WebElement memoryCard : memoryGameCards) {
			testResultService.assertEquals(true, memoryCard.isDisplayed(), "The memory card was not displayed as it should have");
		}
	}

	public void VerifyHeadSetOnCorrectlySelectedCards() throws Exception {
		
		webDriver.waitForElement("GameBoardUl", ByTypes.id, true, 15).findElement(By.id(Integer.toString(cardID))).findElement(By.tagName("input")).isDisplayed();
		
		if (cardID%2 == 0) {
			webDriver.waitForElement("GameBoardUl", ByTypes.id, true, 15).findElement(By.id(Integer.toString(cardID + 1))).findElement(By.tagName("input")).isDisplayed();
		} else {
			webDriver.waitForElement("GameBoardUl", ByTypes.id, true, 15).findElement(By.id(Integer.toString(cardID - 1))).findElement(By.tagName("input")).isDisplayed();
		}
	}

	public void SelectTwoIncorrectCards() throws Exception {
		
		SelectRandomGameCardID();
		WebElement firstCardToSelect = webDriver.waitForElement("GameBoardUl", ByTypes.id, true, 15).findElement(By.id(Integer.toString(cardID)));
		String cardToSelectClass = firstCardToSelect.getAttribute("class");
		
		while (cardToSelectClass.contains("correctAnswer")) {
			
			SelectRandomGameCardID();
			firstCardToSelect = webDriver.waitForElement("GameBoardUl", ByTypes.id, true, 15).findElement(By.id(Integer.toString(cardID)));
			cardToSelectClass = firstCardToSelect.getAttribute("class");
		}
		
		int firstIncorrectAnswer = cardID;
		int secondIncorrectAnswer = firstIncorrectAnswer;
		if (firstIncorrectAnswer % 2 == 0) {
			secondIncorrectAnswer++;
		} else {
			secondIncorrectAnswer--;
		}
		
		firstCardToSelect.click();
		SelectRandomGameCardID();
		
		WebElement secondCardToSelect = webDriver.waitForElement("GameBoardUl", ByTypes.id, true, 15).findElement(By.id(Integer.toString(cardID)));
		cardToSelectClass = secondCardToSelect.getAttribute("class");
		
		while ((cardToSelectClass.contains("correctAnswer") || cardToSelectClass.contains("isOpen")) && cardID != secondIncorrectAnswer) {
			
			SelectRandomGameCardID();
			secondCardToSelect = webDriver.waitForElement("GameBoardUl", ByTypes.id, true, 15).findElement(By.id(Integer.toString(cardID)));
			cardToSelectClass = secondCardToSelect.getAttribute("class");
		}
		
		secondCardToSelect.click();
		Thread.sleep(5000);
		
		VerifyIncorrecrCardSwitchedBack(firstCardToSelect);
		VerifyIncorrecrCardSwitchedBack(secondCardToSelect);
	}

	public void VerifyIncorrecrCardSwitchedBack(WebElement CardToSelect) throws Exception {
		testResultService.assertEquals(false, CardToSelect.getAttribute("class").contains("isOpen"), "The card should not be 'Open' while selecting incorrect twin card", true);
		testResultService.assertEquals(false, CardToSelect.getAttribute("class").contains("correctAnswer"), "The card should not be 'Open' while selecting incorrect twin card", true);
	}

	public void VerifyTheCorrectAnswersDidNotDisappear() throws Exception {
		
		int correctAnswersCount = 0;
		WebElement card;
		for (WebElement element : memoryGameCards) {
			
			card = element.findElement(By.tagName("img"));
			if (card.getAttribute("class").contains("correctAnswer")) {
				correctAnswersCount++;
			}
		}
		
		testResultService.assertEquals(true, correctAnswersCount == 2, "The amount of correct answers was incorrectly displayed", true);
	}
		
	public void clickOnPronunciation() throws Exception {
		getPronunciationElement().click();

	}

	private WebElement getPronunciationElement() throws Exception {
		return webDriver.waitForElement("//li[@id='pf']", ByTypes.xpath, false, webDriver.getTimeout());
	}
	
	public void checkThatERaterSubmitBtnIsNotDisplayed() throws Exception {
		if (getERaterSubmitBtnElement() != null) {
			testResultService.addFailTest("Submit btn was displayed while it should have been hidden", false, true);
		}

	}
	
	private WebElement getERaterSubmitBtnElement() throws Exception {
		return webDriver.waitForElement("//a[@title='Submit']", ByTypes.xpath, false, smallTimeOut);
	}
	
	public void clickOnYourAnswerTab() throws Exception {
		getYourAnswerTabElement().click();

	}
	
	private WebElement getYourAnswerTabElement() throws Exception {
		return webDriver.waitForElement("Your Answer", ByTypes.linkText, false, smallTimeOut);
	}
	
	public void clickOnCorrectAnswerTab() throws Exception {
		getCorrectAnswerTabElement().click();

	}
	
	private WebElement getCorrectAnswerTabElement() throws Exception {
		return webDriver.waitForElement("Correct Answer", ByTypes.linkText, false, smallTimeOut);
	}
	
	public void checkThatNavBarIsNotDisplayed() throws Exception {
		if (getNavBarElement() != null) {
			testResultService.addFailTest("Nav Bar was displayed while it should have been hidden", false, true);
		}

	}
	
	private WebElement getNavBarElement() throws Exception {
		return webDriver.waitForElement(UL_ID_SITEMENU_ITEM_HOME, ByTypes.xpath, false, smallTimeOut);
	}
	
	public void checkThatUserMenuIsNotDisplayed() throws Exception {
		if (getUserMenuElement() != null) {
			testResultService.addFailTest("User Menu was displayed while it should have been hidden", false, true);
		}

	}
	
	private WebElement getUserMenuElement() throws Exception {
		return webDriver.waitForElement(USER_MENU, ByTypes.xpath, false, smallTimeOut);
	}
	
	public void checkThatFooterIsNotDisplayed() throws Exception {
		if (getFooterElement() != null) {
			testResultService.addFailTest("Footer was displayed while it should have been hidden", false, true);
		}

	}
	
	private WebElement getFooterElement() throws Exception {
		return webDriver.waitForElement("footer", ByTypes.id, false, smallTimeOut);
	}
		
	public void checkUnitLessonStepNameOnLanding(String expectedUnitName, String expectedLessonName, String expectedStepName) throws Exception {
	
	String currentUnitName = getHeaderTitle();
	testResultService.assertEquals(expectedUnitName, currentUnitName, "Unit name not found/do not match");
	
	String currentLessonName = getLessonName();
	testResultService.assertEquals(expectedLessonName, currentLessonName, "Lesson name not found/do not match");
	
	String currentStepName = getStepName(1).replace(". ", "");
	testResultService.assertEquals(expectedStepName, currentStepName, "Step name not found/do not match");
	
	}
	
	public void clickOnSendToTeacher() throws Exception {
		getSubmitToTeacherBtnElement().click();
			
	}
	
	public void clickOnSubmitToTeacherOW() throws Exception {
		getSendToTeacherBtnElement().click();
			
	}
	
	public void checkThatSendToTeacherBtnIsNotDisplayed() throws Exception {
		if (getSendToTeacherBtnElement() != null) {
			testResultService.addFailTest("Send To Teacher btn displayed though it should have not", false, true);
		}
	}
	
	public void checkThatSubmitToTeacherBtnIsDisabled() throws Exception {
	WebElement element = webDriver.waitForElement("//div[contains(@class,'buttonWrapper disable')]", ByTypes.xpath,	smallTimeOut, false);
		if (element == null) {
		testResultService.addFailTest("Submit To Teacher btn is NOT disabled", false, true);
		}
	}
	
	public void checkThatSubmitEraterBtnIsDisabled() throws Exception {
		if (!getSendToTeacherBtnElement().getAttribute("class").contains("disable")) {
			testResultService.addFailTest("Submit btn is NOT disabled", false, true);
		}
	}
		
	public void checkThatSendToTeacherBtnIsDisplayed() throws Exception {
		if (getSubmitToTeacherBtnElement() == null) {
			testResultService.addFailTest("Send To Teacher btn is not displayed", false, true);
		}
	}
	
	private WebElement getSendToTeacherBtnElement() throws Exception {
		return webDriver.waitForElement("Submit", ByTypes.linkText, false, smallTimeOut);
	}
	
	private WebElement getSubmitToTeacherBtnElement() throws Exception {
		return webDriver.waitForElement("Send To Teacher", ByTypes.linkText, false, smallTimeOut);
	}
		
	public void checkThatClearToolIsDisplayed() throws Exception {
		WebElement element = getClearToolElement();
		testResultService.assertEquals(true, element.isDisplayed(), "Clear btn tool is not displayed");

	}
	
	public void checkThatSeeAnswerToolIsNotDisplayed() throws Exception {
		
		if (getSeeAnswerToolElement() != null) {
			testResultService.addFailTest("See Answer btn tool is displayed though it should not", false, true);
		}
		
		/*
		Boolean isElementBlocked = element.getAttribute("style").trim().equalsIgnoreCase("display: block;");
		testResultService.assertEquals(true, isElementBlocked, "See Answer btn tool is displayed though it should not");
		*/
		 
	}
	
	public void checkThatClearToolIsNotDisplayed() throws Exception {
		
		if (getClearToolElement() != null) {
			testResultService.addFailTest("Clear btn tool is displayed though it should not", false, true);
		}
		
	}
	
	public void checkThatCheckAnswerToolIsNotDisplayed() throws Exception {
		if (getCheckAnswerToolElement() != null) {
			testResultService.addFailTest("Check Answer btn tool is displayed though it should not", false, true);
		}
				
	}
	
	private WebElement getClearToolElement() throws Exception {
		return webDriver.waitForElement("Restart", ByTypes.id, false, smallTimeOut);
	}
	
	private WebElement getSeeAnswerToolElement() throws Exception {
		return webDriver.waitForElement("SeeAnswer", ByTypes.id, false, smallTimeOut);
		
	}
	
	private WebElement getCheckAnswerToolElement() throws Exception {
		return webDriver.waitForElement("CheckAnswer", ByTypes.id, false, smallTimeOut);
	}
	
	public void validateAlertModalByMessage(String expectedMessage, Boolean pressOK) throws Exception {
		
		WebElement element = null;
		
		for (int i = 0; element==null && i <10;i++){
			element = getAlertModalElement();
		}
		String actualMessage = getAlertModalElement().getText();
		
		testResultService.assertEquals(expectedMessage, actualMessage,"Alert text does not match to expected",true);
		if (pressOK) webDriver.waitForElement("btnOk", ByTypes.id,false,smallTimeOut).click();
		
	}
	
	public void validateConfirmModalByMessage(String expectedMessage, Boolean pressContinue) throws Exception {
		
		String actualMessage = getConfirmModalElement().findElement(By.xpath("//div[@class='modal-body']//div[contains(@class,'layout__unlistPull')]")).getText().replace("Cancel","").trim();
		testResultService.assertEquals(expectedMessage, actualMessage,"Alert text does not match to expected");
		if (pressContinue) webDriver.waitForElement("btnOk", ByTypes.id).click();
		

	}
	
	public void validateTestCompletedMessage(String expectedMessage) throws Exception {
		
		String actualMessage = webDriver.waitForElement("learning__startTestMessage", ByTypes.className, true, webDriver.getTimeout()).getText();
		testResultService.assertEquals(expectedMessage, actualMessage,"Message text does not match to expected");
	
	}
	
	public void checkThatAlertMessageClosed() throws Exception {
		if (getAlertModalElement() != null) {
			testResultService.addFailTest("Alert message is not closed", false, true);
		}
				
	}
	
	public void closeAlertModalByAccept() throws Exception {
		if (getAlertModalElement()!=null) webDriver.waitForElement("btnOk", ByTypes.id).click();
		
	}
	
	public void closeConfirmAlertModalByAccept() throws Exception {
		if (getConfirmModalElement()!=null)
			webDriver.waitForElement("btnOk", ByTypes.id).click();
		
	}
	
	private WebElement getAlertModalElement() throws Exception {
		return webDriver.waitForElement("alertModal", ByTypes.id, false, 1);
	}
	
	private WebElement getConfirmModalElement() throws Exception {
		return webDriver.waitUntilElementAppears("confirmModal", ByTypes.id, 15);
	}
	
	public boolean isCurrentPracticeHasSR() throws Exception {
		boolean isSR = false;
		WebElement practiceSR = webDriver.waitForElement("//div[@class='multChoiceQ']//div[@class='status']", ByTypes.xpath, false, 1);
		if (practiceSR != null) isSR = true;
		return isSR;
	}
	
	public void makeProgressActionByItemType(int itemTypeId, String itemCode) throws Exception {
		
		String skill = itemCode.substring(2, 3);
		
		switch (itemTypeId) {
				
			case 38: case 40: // new video explore
				clickOnPlayNewVideoButton();
								
				
				break;
			case 25: case 11: case 10: case 3: // MCQ
				if (isCurrentPracticeHasSR()) {
					Thread.sleep(2000);					
					interactSection.waitForStartBtnIsEnabledInPracticeMCQ();
					interactSection.clickTheStartButton();
					Thread.sleep(10000);
					
					} else if (webDriver.waitForElement("pictureFrame", ByTypes.className, false, 1) !=null) {
						selectAnswerRadioByImage(1, 1);					
						
					} else	selectAnswerRadioByTextNum(1, 1);
				break;
			case 1: case 7: // D&D classification 2 columns / D&D matching
				//dragAndDrop.dragAndDropClassificationAnswerByTextToColumn("e", 1, 2);
				dragAndDrop.dragAndDropClassificationFirstAnswer();
				break;
			case 23: case 2: case 12: case 18: // D&D close
				dragAndDrop.dragAndDropCloseFirstAnswer();
				break;
			case 21: // E-rater / Open Writing / Free Writing
				Thread.sleep(2000);
				
				if (getSubmitToTeacherBtnElement()!=null) {				
						enterFreeWritingTextBySection("1", "Hello");
						clickOnSendToTeacher();
						closeAlertModalByAccept();
						Thread.sleep(1000);
				} else {
					webDriver.swithcToFrameAndSendKeys("//body[@id='tinymce']",	"Some text bla bla Some text bla bla Some text bla bla Some text bla bla", "elm1_ifr");
						if (getAutomatedEvaluationCountElement()!=null) {
							clickOnSubmitText();
						} else {
							clickOnSubmitToTeacherOW();
							closeAlertModalByAccept();
						}
				}
				
				break;
			case 26: case 15: // Open Ended Segments
				if (!skill.equals("v") || itemCode.contains("p40")) { 
					enterOpenSmallSegmentAnswerTextByIndex(1, "e");
				}
				else enterOpenSegmentAnswerTextByIndex(1, "e");
				break;
			case 9: case 53: case 45: case 34: case 29: case 33: case 32: // reading explore / writing explore / vocabulary explore / audio speaking explore / audio grammar explore / audio listening explore / alphabet explore
				if (!skill.equals("r") && !skill.equals("v") && !skill.equals("a") && !skill.equals("w")) {
					clickOnPlayVideoButton();
				}
				break;
				
			case 63: // Empty Task
				break;
				
			case 48: case 43: // Interact 1
				Thread.sleep(3000);
				interactSection.waitForSpeakersArrowsEnabledInInteract1();
				interactSection.hoverOnSpeaker(2);
				interactSection.selectRightSpeaker();
				Thread.sleep(3000);
				interactSection.clickTheStartButton();
				Thread.sleep(3000);
				break;	
			case 20: case 44: // Interact 2
				Thread.sleep(3000);
				interactSection.waitForStartBtnIsEnabledInPracticeMCQ();
				interactSection.clickInteract2StartButtn();
				Thread.sleep(1000);
				break;	
			case 22: // D&D match text to picture
				//dragAndDrop.dragAndDropMatchTextToPicAnswerByTextToId("e", 2);
				dragAndDrop.dragAndDropMatchTextToPicFirstAnswer();
				break;
			case 51: case 19: case 16: // Fill in the blanks
				selectFirstAnswerFromDropDown("1_1");
				break;
			case 17: // Sequence Sentence
				dragAndDrop.dragAndDropSequenceAnswerByTextToPlaceInOrder(dragAndDrop.getFirstTileText().substring(0,1), 2);
				break;
			case 5: // Sequence Image
				dragAndDrop.retrieveDraggableImages();
				dragAndDrop.dragAndDropReadingSequenceSentence();
				break;
			case 27: // Matching Game
				//RetrieveMemoryGameCards();
				Thread.sleep(1000);
				SelectTwoCorrectCards();
				break;
				
			case 37: // Prepare
				if (getPlayButtonPrepare()!=null) {
					clickOnPlayVideoButtonPrepare();
				}
				break;
				
			case 58: // Open Speech
				interactSection.clickOpenSpeechStartButton();
				interactSection.checkOpenSpeechPracticeStatusChanged(InteractStatus.prepare, 1);
				interactSection.checkOpenSpeechPanelStageView(1);
				Thread.sleep(1000);
				interactSection.clickOpenSpeechSkipButton();
				Thread.sleep(1000);
				interactSection.clickOpenSpeechStopButton();
				interactSection.clickOpenSpeechSendToTeacherButton();
				closeAlertModalByAccept();
				break;
				
			case 59: // Select Text
				clickOnMarkerAndVerifySentenceSelect(1);
				break;
				
			case 57: // Insert Text
				clickOnMarkerAndVerifySentenceInsert(1, getInsertSentenceAnswerElement().getText());
				break;
				
			case 62: // Edit Text
				getEditTextInputElements().get(1).sendKeys("sample");
				break;
				
			default:
                testResultService.addFailTest("Invalid or not implemented item type", false, true);
                break;
				
		}
		
		Thread.sleep(500);
				
	}
	
	public List<WebElement> getInsertTextButtonsElements() throws Exception {
	
		List<WebElement> list = webDriver.getElementsByXpath("//div[contains(@class,'readingExploreMainContent')]//span[contains(@class,'addinTxt')]");
	
		return list;
	}
	
	public List<WebElement> getSelectTextButtonsElements() throws Exception {
		
		//List<WebElement> list = webDriver.getElementsByXpath("//div[contains(@class,'readingExploreMainContent')]//span[contains(@class,'selectTxt')]");
		List<WebElement> list = webDriver.getElementsByXpath("//span[contains(@class,'selectTxt')]");
		
		
		return list;
	}
	
	public List<WebElement> getEditTextContentElements() throws Exception {
		
		List<WebElement> list = webDriver.getElementsByXpath("//div[@id='rightDiv']//div[contains(@class,'readingExploreMainContent')]");
	
		return list;
	}
	
	public List<WebElement> getHighlightedTextElements() throws Exception {
		
		List<WebElement> list = webDriver.getElementsByXpath("//div[@class='readingExploreMainContent']//span[contains(@class,'highlightedText')]");
	
		return list;
	}
	
	public List<WebElement> getHighlightedQuestionElements() throws Exception {
		
		List<WebElement> list = webDriver.getElementsByXpath("//div[@class='qTextWrapper']//span[contains(@class,'highlightedText')]");
	
		return list;
	}
	
	
public List<WebElement> getHighlightedTextElementsNewMcq() throws Exception {
		
		List<WebElement> list = webDriver.getElementsByXpath("//div[@class='prMCQ__questionText']//span[contains(@class,'highlighted 407 highlightedText')]");
	
		return list;
	}
	
	public List<WebElement> getHighlightedQuestionElementsNewMcq() throws Exception {
		
		List<WebElement> list = webDriver.getElementsByXpath("//div[@class='prMCQ__questionText']//span[contains(@class,'highlighted 407 highlightedText')]");
	
		return list;
	}
	
	
	public List<WebElement> getEditTextInputElements() throws Exception {
		
		List<WebElement> rightContentInputsElements = getEditTextContentElements().get(1).findElements(By.tagName("input"));
	
		return rightContentInputsElements;
	}
	
	public void editTextByInputBoxNumber(int inputBoxNum, String newText) throws Exception {
		
		WebElement element = getEditTextContentElements().get(1).findElements(By.tagName("input")).get(inputBoxNum-1);
		element.clear();
		element.sendKeys(newText);
		
	}
		
	public void verifyEditTextCorrectMarkByInputNumber(int inputBoxNum) throws Exception {
		
		String markCorrect = getEditTextInputElements().get(inputBoxNum-1).getAttribute("class");
		testResultService.assertEquals(true, markCorrect.equals("vCheck"), "Correct mark not found");
		
	}
	
	public void verifyEditTextWrongtMarkByInputNumber(int inputBoxNum) throws Exception {
		
		String markWrong = getEditTextInputElements().get(inputBoxNum-1).getAttribute("class");
		testResultService.assertEquals(true, markWrong.equals("xCheck"), "Wrong mark not found");
		
	}
	
	public WebElement getInsertSentenceAnswerElement() throws Exception {
		
		WebElement element = webDriver.waitForElement("sentenceNoteWrapper", ByTypes.className, smallTimeOut, true, "Insert text sentence answer in right side");
	
		return element;
	}
	
	public WebElement getSelectSentenceAnswerElement() throws Exception {
		
		return getInsertSentenceAnswerElement();
	}
	
	public String hoverOnInsertBtnAndGetImageSpritePositionY(int markerNumber) throws Exception {
		
		WebElement element = getInsertTextButtonsElements().get(markerNumber-1);
		webDriver.hoverOnElement(element);
		Thread.sleep(1000);
		String btnColor = element.getCssValue("background-position");
		btnColor = btnColor.split("-")[1];
		
		return btnColor;	
	}
	
	public void clickOnMarkerAndVerifySentenceInsert(int markerNumber, String answerSentence) throws Exception {
		
		WebElement element = getInsertTextButtonsElements().get(markerNumber-1);
		webDriver.clickOnElement(element);
		Thread.sleep(2000);
		String insertedSent = element.findElement(By.className("ITNewText")).getText();
		testResultService.assertEquals(answerSentence, insertedSent, "Inserted sentence is not correct");
		String disabled = getInsertSentenceAnswerElement().getAttribute("class");
		testResultService.assertEquals(true, disabled.contains("disabled"), "Answer Sentence not greyed out");
				
	}
	
	public void clickOnMarkerAndVerifySentenceSelect(int markerNumber) throws Exception {
		
		WebElement elementText = getSelectTextButtonsElements().get(markerNumber-1);
		webDriver.clickOnElement(elementText);
		Thread.sleep(1000);
		WebElement elementAnswer = getSelectSentenceAnswerElement();
		testResultService.assertEquals(elementText.getText(), elementAnswer.getText(), "Inserted sentence is not correct");
		String answerState = getInsertSentenceAnswerElement().getAttribute("class");
		String textState = elementText.getAttribute("class");
		testResultService.assertEquals(true, answerState.contains("selected") && textState.contains("selected") , "Text or/and Answer area not selected");
				
	}
	
	public void verifyNoSentenceInsertedByMarkerNumber(int markerNumber) throws Exception {
		
		WebElement element = getInsertTextButtonsElements().get(markerNumber-1);
		String insertedSent = element.findElement(By.className("ITNewText")).getText();
		testResultService.assertEquals(true, insertedSent.isEmpty(), "Sentence inserted though it should not");
						
	}
	
	public void verifyInsertSentenceAnswerEnabled() throws Exception {
				
		String disabled = getInsertSentenceAnswerElement().getAttribute("class");
		testResultService.assertEquals(false, disabled.contains("disabled"), "Answer Sentence greyed out though it should not");
				
	}
	
	public void verifySentenceInsertOnSeeAnswer(int markerNumber, String answerSentence) throws Exception {
		
		WebElement element = getInsertTextButtonsElements().get(markerNumber-1);
		String insertedSent = element.findElement(By.className("ITNewText")).getText();
		testResultService.assertEquals(answerSentence, insertedSent, "Inserted sentence is not correct");
		String seeAnswer = element.getAttribute("class");
		testResultService.assertEquals(true, seeAnswer.contains("seeAnswer"), "Inserted sentence not marked blue");
				
	}
	
	public void verifySentenceSelectOnSeeAnswer(String answerSentence) throws Exception {
		
		WebElement element = getSelectSentenceAnswerElement();
		String insertedSent = element.getText();
		testResultService.assertEquals(answerSentence, insertedSent, "Inserted sentence is not correct");
		String seeAnswer = element.getAttribute("class");
		testResultService.assertEquals(true, seeAnswer.contains("seeAnswer"), "Inserted sentence not marked blue");
				
	}
	
	public void verifySentenceMarkedAsCorrectOnCheckAnswer(int markerNumber) throws Exception {
		
		WebElement element = getInsertTextButtonsElements().get(markerNumber-1);
		String mark = element.getAttribute("class");
		testResultService.assertEquals(true, mark.contains("vCheck"), "Inserted sentence not marked as correct");
				
	}
	
	public void verifySentenceSelectedAsCorrectOnCheckAnswer(int markerNumber) throws Exception {
		
		WebElement element = getSelectTextButtonsElements().get(markerNumber-1);
		String textMark = element.getAttribute("class");
		element = getSelectSentenceAnswerElement();
		String answerMark = element.getAttribute("class");
		testResultService.assertEquals(true, textMark.contains("vCheck") && answerMark.contains("vCheck"), "Inserted sentence not marked as correct");
				
	}
	
	public void verifySentenceSelectedAsWrongOnCheckAnswer(int markerNumber) throws Exception {
		
		WebElement element = getSelectTextButtonsElements().get(markerNumber-1);
		String textMark = element.getAttribute("class");
		element = getSelectSentenceAnswerElement();
		String answerMark = element.getAttribute("class");
		testResultService.assertEquals(true, textMark.contains("xCheck") && answerMark.contains("xCheck"), "Inserted sentence not marked as correct");
				
	}
	
	public void verifySentenceMarkedAsWrongOnCheckAnswer(int markerNumber) throws Exception {
		
		WebElement element = getInsertTextButtonsElements().get(markerNumber-1);
		String mark = element.getAttribute("class");
		testResultService.assertEquals(true, mark.contains("xCheck"), "Inserted sentence not marked as wrong");
				
	}
	
	public void verifyTaskCounterValues(int currentExpected, int totalExpected) throws Exception {
		
		int currentActual = Integer.parseInt(webDriver.waitForElement("learning__PATC_currentTask", ByTypes.className, true, webDriver.getTimeout()).getText());
		int totalActual = Integer.parseInt(webDriver.waitForElement("learning__PATC_totalTasks", ByTypes.className, true, webDriver.getTimeout()).getText());
		testResultService.assertEquals(currentActual, currentExpected, "Current task in counter is wrong");
		testResultService.assertEquals(totalActual, totalExpected, "Total task in counter is wrong");
				
	}
	
	public List<WebElement> getMultiResTabsElements() throws Exception {
		
		List<WebElement> elements = getMultiResHeaderElement().findElements(By.className("learning__RAMultiResTab"));
	
		return elements;
	}
	
	public WebElement getMultiResHeaderElement() throws Exception {
		
		WebElement element = webDriver.waitForElement("learning__RAMultiResTabsW", ByTypes.className,"Find Multiple Resources Header");
	
		return element;
	}
	
	public void verifytMultiResTabTitle(int tabNumberLTR, String expectedTitle) throws Exception {
		
		int tabIndex = tabNumberLTR-1;
		String actualTitle = getMultiResTabsElements().get(tabIndex).findElement(By.className("learning__RAMultiResTitleW")).getText();
		testResultService.assertEquals(expectedTitle, actualTitle, "Multiple resource tab title is wrong");
				
	}
	
	public void verifytMultiResTabType(int tabNumberLTR, String expectedType) throws Exception {
		
		int tabIndex = tabNumberLTR -1;
		String actualType = getMultiResTabsElements().get(tabIndex).getAttribute("class");
		boolean isTypeCorrect = actualType.contains(expectedType);
		testResultService.assertEquals(true, isTypeCorrect, "Multiple resource tab type is wrong");
				
	}
	
	public void verifyMultiResTabSelected(int tabNum) {
		
		int tabIndex = tabNum - 1;
		boolean isSelected = false;
		try{
			isSelected = getMultiResTabsElements().get(tabIndex).getAttribute("class").contains("current");
			testResultService.assertEquals(true, isSelected, "Tab: " +tabNum+ " is not selected");
		}
		catch (Exception e) {
			e.printStackTrace();
			//testResultService.addFailTest("Tab: " +tabNum+ " is not selected", false, true);
		}
	}
	
	public void verifyResourceSetLoadedByType(Skill type, String itemCode, boolean testMode) throws Exception {
		
		switch (type) {
		
			case Reading:
				
				if (!testMode) {
					selectSegmentInReadingByNumber(1, 1, true);
					checkThatHearPartIsDisplayed();
					checkThatReadingTextIsDisplayed();
					checkThatHearAllIsDisplayed();
					//checkThatMainIdeaToolIsDisplayed();
					checkThatKeywordsToolIsDisplayed();
					//checkThatReferenceWordsToolIsDisplayed();
					checkThatPrintIsDisplayed();
					clickOnHearAll();
					checkAudioFile("syncAudioPlayer", itemCode); 
					} else {
					selectSegmentInReadingByNumber(1,1, true);
					checkThatSeeTranslationNotDisplayed();
					checkThatHearPartIsNotDisplayed();
					checkThatHearAllIsNotDisplayed();
					//checkThatMainIdeaToolIsNotDisplayed();
					checkThatKeywordsToolIsNotDisplayed();
					//checkThatReferenceWordsToolIsNotDisplayed();
					checkThatPrintIsNotDisplayed();
					}
				
				
			break;
		
			case ListeningVideo:
				
				if (!testMode) {
					checkThatSeeTextIsDisplayed();
					clickOnSeeText();
					checkThatTextIsDisplayed();
					selectTextForRecord(1);
					checkThatHearPartIsDisplayed();
					checkThatRecordYourselfIsDisplayed();
					checkThatPrintIsDisplayed();
					clickOnPlayNewVideoButton();
					webDriver.sleep(1);
					checkVideoFile("MP", itemCode); 
				} else {
					checkThatSeeTextIsNotDisplayed();
					checkThatTextDisplayed(false);
					}
				
			break;
			
			case Listening:
				
				if (!testMode) {
					checkThatSeeTextIsDisplayed();
					clickOnSeeText();
					checkThatTextIsDisplayed();
					selectTextForRecord(1);
					checkThatHearPartIsDisplayed();
					checkThatRecordYourselfIsDisplayed();
					checkThatPrintIsDisplayed();
					clickOnPlayVideoButton();
					webDriver.sleep(1);
					checkAudioFile("syncAudioPlayer", itemCode); // TODO uncommented when real lesson.js generated with res set order (01,02,03) suffix
				} else {
					checkThatSeeTextIsNotDisplayed();
					checkThatTextDisplayed(false);
					}	
								
			break;
			
			default: testResultService.addFailTest("Skill type is not valid");
			
			break;
				
			
		}
		
		
				
	}
		
	public void clickOnMultiResTab(int tabNum) throws Exception {
		
		int tabIndex = tabNum -1;
		WebElement element = getMultiResTabsElements().get(tabIndex);
		element.click();
		webDriver.hoverOnElement(element,50,200);
				
	}
	
	public void hoverOnMultiResHeaderAndCheckExpand() throws Exception {
		
		WebElement element = getMultiResHeaderElement();
		
		webDriver.hoverOnElement(element);
		webDriver.sleep(1);
		element = getMultiResHeaderElement();
		testResultService.assertEquals(true, element.getAttribute("class").contains("hover"), "Header element not expanded");
		
		webDriver.hoverOnElement(element, 50, 200);
		webDriver.sleep(1);
		element = getMultiResHeaderElement();
		testResultService.assertEquals(true, !element.getAttribute("class").contains("hover"), "Header element not collapsed");
		
	}
	
	public void submitTextToERater(String text) throws Exception {
		
		webDriver.swithcToFrameAndSendKeys("//body[@id='tinymce']",	text, "elm1_ifr");
		webDriver.sleep(3);
		clickOnSubmitText();
		//webDriver.sleep(15);
		webDriver.sleep(2);
		
	}
	
	public String getAttemptNumberOfAutomatedEvaluation() throws Exception {
		
		return getAutomatedEvaluationCountElement().getText();
		
	}
	
	public WebElement getAutomatedEvaluationCountElement() throws Exception {
		
		return webDriver.waitForElement("submitIconOn", ByTypes.className, smallTimeOut, false, "Attempt Number not found");
		
	}
	
	public String getERaterWordsCountOnSubmit() throws Exception {
		
		return webDriver.waitForElement("wordsNumber", ByTypes.className, "Words count not found").getText();
		
	}
	
	
	public void clickOnPlayFlashVideo() throws Exception { // need to be developed 
		/*Actions actions = new Actions((WebDriver) webDriver);
		actions.moveToElement(webDriver.waitForElement("MP", ByTypes.id).findElement(By.id("MP"))).clickAndHold().perform();
		actions.release().perform(); */
	}
	
	public void clickOnSeeFeedbackInERater() throws Exception { 
		
		webDriver.waitForElement("See Feedback", ByTypes.linkText, "See Feedback link in E-Rater task").click();
		
	}	
	
	// This function gets the actual task number (not its index)
	public void ClickOnTask(int taskNum) throws Exception {
		List<WebElement> tasks = webDriver.getWebDriver().findElements(By.xpath("//div[contains(@class,'learning__tasksNav_task')]"));
		tasks.get(taskNum).click();
	}
	
	//new
	public void verifyAnswerRadioSelectedCorrectNew(String answer) throws Exception {
		
		List<WebElement> answers = webDriver.getWebDriver().findElements(By.xpath("//div[contains(@class,'multiRadioWrapper')]//div[contains(@class,'multiRadio')]"));
		for (int i = 0; i < answers.size(); i++) {
			if (answers.get(i).getText().equals(answer)) {
				testResultService.assertEquals(true, answers.get(i).getAttribute("className").contains("correct"),"Correct Answer is Not Selected");
				break;
			}
		}
	}
	
	public void validateRadioQuestionIsNotAnswered() throws Exception {
		List<WebElement> answersCheckBoxes = webDriver.getWebDriver().findElements(By.xpath("//input[contains(@id,'question')]"));
		for (int i = 0; i < answersCheckBoxes.size(); i++) {
			testResultService.assertEquals(true,answersCheckBoxes.get(i).getAttribute("checked") == null,"Answer " + (i+1) + " is checked");	
		}
	}
}
