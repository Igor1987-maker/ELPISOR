package pageObjects.tms;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Enums.ByTypes;
import Enums.TaskTypes;
import drivers.GenericWebDriver;
import pageObjects.GenericPage;
import pageObjects.edo.NewUxDragAndDropSection2;
import pageObjects.edo.NewUxLearningArea2;
import services.TestResultService;

public class CurriculumAuthoringToolPage extends GenericPage {
	
	@FindBy(xpath = "//td[contains(@class,'instructions')]") 
	public WebElement popUpHeaderText;
	
	@FindBy(xpath = "//div[contains(@class,'sectionBg')]/table") 
	public List<WebElement> popUpTitlesNames;
	
	@FindBy(id = "cmpTitle") 
	public WebElement popupComponentName;
	
	@FindBy(xpath = "//*[@id='sbcmpTitle']") 
	public List<WebElement> popUpSubTitlesNames;
	
	@FindBy(xpath = "//*[@id='chbSbCmp']") 
	public List<WebElement> popUpSubTitlesCheckboxes;
	
	@FindBy(id = "button2") 
	public WebElement popupNextButton;
	
	@FindBy(id = "btnNext") 
	public WebElement popupNextButton2;
	
	@FindBy(id = "explanation") 
	public WebElement addNewQuestionInstructionField;
	
	@FindBy(xpath = "//*[@id='answers_row']/tbody/tr[1]/td[1]") 
	public WebElement addNewQuestionFirstAnswerCheckbox;
	
	@FindBy(xpath = "//*[@id='answers_row']/tbody/tr/td[3]/div") 
	public List<WebElement> addNewQuestionAnswersFields;
	
	@FindBy(xpath = "//input[contains(@name,'btnApply')]") 
	public WebElement addNewQuestionApplytBttton;
	
	@FindBy(id = "btnAddAnswer") 
	public WebElement addNewQuestionAddAnswerButton;
	
	@FindBy(xpath = "//*[@id='txt_div']") 
	public List<WebElement> addNewQuestionMatchingPairsFields;
	
	@FindBy(id = "btnFinish") 
	public WebElement popupFinishButton;
	
	@FindBy(xpath = "//tr[contains(@id,'comp')]//td[5]//a") 
	public List<WebElement> componentsNames;
	
	@FindBy(xpath = "//tr[contains(@id,'comp')]//td[1]//img") 
	public List<WebElement> componentsEyeSigns;
	
	@FindBy(xpath = "//tr[contains(@id,'comp')]//td[3]//input[2]") 
	public List<WebElement> componentsCheckBoxes;
	
	TmsHomePage tmsHomePage = new TmsHomePage(webDriver, testResultService);
	NewUxLearningArea2 learningArea2;

	public CurriculumAuthoringToolPage(GenericWebDriver webDriver, TestResultService testResultService)
			throws Exception {
		super(webDriver, testResultService);
		// TODO Auto-generated constructor stub
		PageFactory.initElements(webDriver.getWebDriver(), this);
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

	public void goToAuthoringTool() throws Exception {
		tmsHomePage.switchToMainFrame();
		tmsHomePage.clickOnCurriculum();
		Thread.sleep(2000);
		tmsHomePage.clickOnAuthoringTool();
		Thread.sleep(2000);
	}
	
	public void createNewComponent(String expectedInstructionText, String[] expectedtemplateTitles, String componentName, String[] expectedSubComponents) throws Exception {
		report.startStep("Open new component editor");
		tmsHomePage.clickOnPromotionAreaMenuButton("New");
//		String popUpMsg = webDriver.getPopUpText();
//		if (popUpMsg != null) {
//			testResultService.addFailTest("Error Message Appears", true, true);
//		}
		webDriver.switchToPopup();
		Thread.sleep(1000);
	
		report.startStep("Verify Header, Template names and select template");
		String tempText = popUpHeaderText.getText();
		testResultService.assertEquals(tempText,expectedInstructionText,"External AT instructions aren't equal");
		verifyTemplateAndSelect(expectedtemplateTitles);
		
		report.startStep("Enter component title");
		webDriver.switchToFrame("cmp_details");
		popupComponentName.clear();
		webDriver.SendKeys(popupComponentName, componentName);
		
		report.startStep("Select component steps and click Next");
		for (int i = 0 ; i< popUpSubTitlesNames.size() ; i++){
			testResultService.assertEquals(popUpSubTitlesNames.get(i).getAttribute("value"), expectedSubComponents[i],"subComp name not as expected");
			if (i < 2) {
				popUpSubTitlesCheckboxes.get(i).click();
			}
		}
		webDriver.switchToTopMostFrame();
		webDriver.ClickElement(popupNextButton);
		Thread.sleep(2000);
		
		createATQuestions();
	
		report.startStep("Enter Test step creation");
		webDriver.switchToTopMostFrame();
		webDriver.ClickElement(popupNextButton2);
		Thread.sleep(2000);
		
		createATQuestions();	

		report.startStep("Click on Finish");	
		webDriver.switchToTopMostFrame();
		webDriver.ClickElement(popupFinishButton);
		webDriver.switchToMainWindow();
		tmsHomePage.switchToMainFrame();
	}
	
	public void verifyTemplateAndSelect(String[] expectedtemplateTitles) throws Exception {
		for (int i = 0 ; i < popUpTitlesNames.size(); i++){
			testResultService.assertEquals(popUpTitlesNames.get(i).getAttribute("title"), expectedtemplateTitles[i],"template text not as expected");
			if (i == 1) { // Template "Story"
				popUpTitlesNames.get(i).click();
			}
		}
	}
	
	private void createATQuestions() throws Exception {
		Thread.sleep(2000);
		report.startStep("Add an MCQ practice");
		addItemInAT("Add closed question");

		report.startStep("Edit MCQ values");	
		webDriver.switchToTopMostFrame();
		webDriver.switchToFrame(webDriver.waitForElement("//*[@id='cboxLoadedContent']/iframe", ByTypes.xpath));
		webDriver.SendKeys(addNewQuestionInstructionField, "QuestionTitle");
		webDriver.ClickElement(addNewQuestionFirstAnswerCheckbox);
		
		for (int i = 0 ; i < 3 ; i++) {
			webDriver.SendKeys(addNewQuestionAnswersFields.get(i), "\ue010" + i);
		}
		webDriver.ClickElement(addNewQuestionApplytBttton);
		
		report.startStep("Add Matching practice");
		webDriver.switchToTopMostFrame();
		addItemInAT("Add matching question");

		report.startStep("Edit Matching values");
		webDriver.switchToTopMostFrame();
		webDriver.switchToFrame(webDriver.waitForElement("//*[@id='cboxLoadedContent']/iframe", ByTypes.xpath));
		addNewQuestionInstructionField.clear();
		webDriver.SendKeys(addNewQuestionInstructionField, "Matching Instructions");
		
		for (int c = 0; c < 3; c++){	// Add 3 questions
			webDriver.ClickElement(addNewQuestionAddAnswerButton);
		}
		
		for (int i = 0 ; i < addNewQuestionMatchingPairsFields.size(); i++ ){
			webDriver.SendKeys(addNewQuestionMatchingPairsFields.get(i), "\ue010" + (i + 1));
		}
		webDriver.ClickElement(addNewQuestionApplytBttton);
		Thread.sleep(1000);
	}
	
	public void addItemInAT(String itemName) throws Exception {
		webDriver.switchToFrame("editArea_ifr");
		webDriver.selectElementFromComboBox("itemsSelector", itemName);	
		webDriver.waitForElementAndClick("//*[@id='questionsCell']/div[1]/button", ByTypes.xpath);
		Thread.sleep(2000);
	}
	
	public void deleteComponent(String componentName) throws Exception {
		//webDriver.waitForElement("checkBoxComp" + compId, ByTypes.id).click();
		int index = getComponentIndexByName(componentName);
		webDriver.ClickElement(componentsCheckBoxes.get(index));
		tmsHomePage.clickOnPromotionAreaMenuButton("Delete");
		webDriver.closeAlertByAccept();
	}
	
	public void validateComponentAddedSuccessfully(String componentName) throws Exception {
		
		boolean isDisplayed = checkComponentIsDisplayed(componentName);
		if (isDisplayed) {
			report.startStep("Enter Preview Mode");	
			//webDriver.waitForElement("//*[@id='comp" + compId + "']/td[1]/img", ByTypes.xpath).click();
			clickOnEyeSignOfSpecificComponent(componentName);
			webDriver.switchToNextTab();
			learningArea2 = new NewUxLearningArea2(webDriver, testResultService);
			learningArea2.waitUntilLearningAreaLoaded();
			webDriver.printScreen("After entring preview mode");
			learningArea2.clickOnNextButton();
		
			report.startStep("Verify Practice Step");
			verifyATQuestions();
	
			report.startStep("Click on Start Test");
			learningArea2.clickOnNextButton();
			Thread.sleep(2000);
			learningArea2.clickOnStartTest();
			Thread.sleep(2000);
		
			report.startStep("Verify Test Step");
			verifyATQuestions();
	
			report.startStep("Close Preview and Delete component");
			webDriver.closeNewTab(0);
			webDriver.switchToMainWindow();
			tmsHomePage.switchToMainFrame();
			Thread.sleep(1000);
		} else {
			testResultService.addFailTest("Component '"+componentName+"' was not found");
		}
	}
	
	public void verifyATQuestions() throws Exception {
		testResultService.assertEquals("QuestionTitle", webDriver.waitForElement("//div[contains(@class,'prMCQ__questionText')]", ByTypes.xpath).getText());
		List<WebElement> mcqAnswers = webDriver.getElementsByXpath("//label[contains(@class,'prMCQ__answerLabel')]");
		for (int i = 0 ; i < 3 ; i++) {
			testResultService.assertEquals("Write text here" +i,mcqAnswers.get(i).getText());
		}
		
		learningArea2 = new NewUxLearningArea2(webDriver, testResultService);
		learningArea2.clickOnNextButton();
		learningArea2.VerificationOfQuestionInstruction("Matching Instructions");
		NewUxDragAndDropSection2 dragAndDrop2 = new NewUxDragAndDropSection2(webDriver, testResultService);
		for (int i = 0 ; i < 3 ; i ++) {
			dragAndDrop2.dragAndDropAnswerByTextToTarget("Write text here" + (i + 4), i+1,TaskTypes.Matching);
		}	
	}
	
	public int getComponentIndexByName(String componentName) {
		int index = 0;
		for (int i = 0; i < componentsNames.size(); i++) {
			if (componentsNames.get(i).getText().equals(componentName)) {
				index = i;
				break;
			}
		}
		return index;
	}
	
	public void clickOnEyeSignOfSpecificComponent(String componentName) {
		int index = getComponentIndexByName(componentName);
		webDriver.ClickElement(componentsEyeSigns.get(index));
	}
	
	public boolean checkComponentIsNotDisplayed(String componentName) throws Exception {
		boolean isDisplayed = false;
		for (int i = 0; i < componentsNames.size(); i++) {
			if (componentsNames.get(i).getText().equals(componentName)) {
				isDisplayed = true;
				break;
			}
		}
		testResultService.assertEquals(false, isDisplayed, "Component '"+componentName+"' is Displayed.");
		return !isDisplayed;
	}
	
	public boolean checkComponentIsDisplayed(String componentName) throws Exception {
		boolean isDisplayed = false;
		for (int i = 0; i < componentsNames.size(); i++) {
			if (componentsNames.get(i).getText().equals(componentName)) {
				isDisplayed = true;
				break;
			}
		}
		testResultService.assertEquals(true, isDisplayed, "Component '"+componentName+"' is Not Displayed.");
		return isDisplayed;
	}
}
