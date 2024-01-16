package pageObjects.edo;

import java.util.List;
import java.util.Random;

import org.apache.bcel.generic.GOTO;
import org.apache.tools.ant.taskdefs.Sleep;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.xbill.DNS.NXTRecord;

import com.microsoft.tfs.core.clients.reporting.Report;

import Enums.ByTypes;
import Enums.InteractStatus;
import Enums.NavBarItems;
import Enums.StepProgressBox;
import Enums.TaskTypes;
import Enums.expectedConditions;
import drivers.GenericWebDriver;
import pageObjects.GenericPage;
import services.TestResultService;

import java.util.ArrayList;
import java.util.Date;



public class NewUxLearningArea2 extends NewUxLearningArea {
			
	NewUxDragAndDropSection2 dragAndDrop2;
	NewUxLearningArea2 learningArea2;
	
	
	private static final String Lessons_Header_Xpath = "//section[@id='learning__dropDownListTitleW_lessons']";
	private static final String Lessons_List_Xpath = "//section[@id='learning__dropDownListW_lessons']//div[@id='mCSB_1_container']";
	private static final String Steps_Header_Xpath = "//section[@id='learning__dropDownListTitleW_steps']";
	private static final String Steps_List_Xpath = "//section[@id='learning__dropDownListW_steps']//div[@id='mCSB_2_container']";
	private static final String LA_NavBar = "sitemenu__openMenuBtn";
	
	public void checkCardIsInBank( String card) throws Exception {
		
		bankMGame.findElement(By.id("card1"));
	}
	
	public void checkCardIsInGameBoard (String IncorrectCard){
		gameBoard.findElement(By.id(IncorrectCard));	
	}
	
	public void checkAudioFileAng(String AngularMediaPlayer, String audioFile) throws Exception {

		WebElement audioElement = webDriver.waitForElement("//audio[@id='" + AngularMediaPlayer + "']", ByTypes.xpath, webDriver.getTimeout(), true, "audio element", 1000, expectedConditions.precence);
				
		String fileName = webDriver.getElementSrc(audioElement);
		testResultService.assertEquals(true, fileName.contains(audioFile), "Audio file source not found or not correct");	
	}
		  
	public void CorrectnessVMCQ () throws Exception{
		webDriver.waitUntilElementAppears(allAnswers,3);
		allAnswers.findElement(By.className("selection--v"));
    
	}
	
	public void CorrectnessXMCQ () throws Exception{
		webDriver.waitUntilElementAppears(allAnswers,3);
		allAnswers.findElement(By.className("selection--x"));
	    
		}
	
	public void SelectRadioBtn (String  answerId) throws Exception{
		webDriver.waitUntilElementAppears(allAnswers,3);
		
		allAnswers.findElement(By.xpath("//*[@id='" + answerId + "']")).click();
	}
	
	private WebElement getReadingTextElement2() throws Exception {
		return webDriver.waitForElement("//div[contains(@class,'learning__RAIWrapper left')]", ByTypes.xpath, false, smallTimeOut);
	}
	
	public String checkThatReadingTextIsDisplayed2() throws Exception {
		WebElement element = getReadingTextElement2();
		if (element == null) {
			testResultService.addFailTest("Text is not displayed", false, true);
		}
		return element.getText();
	}
	
	// lesson list elements
	@FindAll(@FindBy(xpath = Lessons_List_Xpath + "/div[contains(@class,'learning__dropDownList_item')]"))
	public List <WebElement> lessons;
	
	@FindBy(xpath = Lessons_Header_Xpath + "//div[@class='learning__dropDownListTitle']")
	public WebElement lessonNameInHeader;
	
	@FindBy(xpath = Lessons_Header_Xpath + "//div[@id='learning__dropDownListBtn']")
	public WebElement lessonListOpenButton;
	
	@FindBy(className = "learning__dropDownList_unitImage")
	public WebElement lessonListUnitImage;
		
	@FindBy(className = "learning__dropDownList_unitName")
	public WebElement lessonListUnitName;
	
	@FindBy(className = "learning__dropDownList_courseName")
	public WebElement lessonListCourseName;
	
	@FindBy(className = "utils__siteOverlay")
	public WebElement overlay;
	
	// step list elements
	
	@FindAll(@FindBy(xpath = Steps_List_Xpath + "/div[contains(@class,'learning__dropDownList_item')]"))
	public List <WebElement> steps;
		
	@FindBy(xpath = Steps_Header_Xpath + "//div[@class='learning__dropDownListTitle']")
	public WebElement stepNameInHeader;   //div[@class='learning__dropDownListTitle']"
	
	@FindBy(className = "learning__stepInstructionsTitle")
	public WebElement stepNameInIntro;
		
	@FindBy(className = "learning__stepInstructionsText")
	public WebElement stepIntroInstruction;
		
	@FindBy(xpath = Steps_Header_Xpath + "//div[@id='learning__dropDownListBtn']")
	public WebElement stepListOpenButton;
		
	// task instruction 
	
	@FindBy(className = "learning__headerIW")
	public WebElement taskInstruction;
	
	// task panel 
	
	@FindBy(className = "learning__taskNavPagerCounter")
	public WebElement taskBarOpenButton;
	
	@FindBy(className = "learning__tasksNav_instructionsW")
	public WebElement stepInstruction;
	
	@FindBy(id = "learning__tasksNav_pager_intro")
	public WebElement stepIntroBtn;
	
	private static final String reviewTestBtn = "learning__testReview";
	@FindBy(id = reviewTestBtn)
	public WebElement reviewTestResultsBtn;
	
	@FindBy(id = "learning__tasksNav_submitTest")
	public WebElement submitTestInTaskBar;
		
	@FindBy(id = "learning__submitTestItem")
	public WebElement submitTestInToolBar;
	
	// resource tools elements
	
	@FindBy(id = "ref")
	public WebElement refWordsTool;
	
	@FindBy(id = "mi")
	public WebElement mainIdeaTool;
	
	@FindBy(id = "kw")
	public WebElement keyWordsTool;
	
	@FindBy(id = "all_")
	public WebElement hearAllTool;
	
	@FindBy(id = "seeExplanation")
	public WebElement seeExplanationTool;
	
	@FindBy(id = "seeAll")
	public WebElement seeTextTool;
	
	@FindBy(id = "hearLI")
	public WebElement hearVocabTool;
	
	@FindBy(id = "getTransIcon")
	public WebElement vocabTransTool;
	
	private static final String viewResBtn = "learning__allToolsVRBW";
	@FindBy(className = viewResBtn)
	public WebElement viewResource;
	
	@FindBy(className = "learning__viewResourceCloseBtn")
	public WebElement viewResourceCloseBtn;
		
	// component test elements
	
	@FindBy(partialLinkText = "Your Answer")
	public WebElement yourAnswerTab;
	
	@FindBy(partialLinkText = "Correct Answer")
	public WebElement correctAnswerTab;	
	
	// practice tools, back & next elements
		
	private static final String nextBtnId = "learning__nextItem";
	@FindBy(id = nextBtnId)
	public WebElement nextBtn;
	
	private static final String backBtnId = "learning__prevItem";
	@FindBy(id = backBtnId)
	public WebElement backBtn;
	
	@FindBy(id = "CheckAnswer")
	public WebElement checkAnswer;
	
	@FindBy(id = "SeeAnswer")
	public WebElement seeAnswer;
	
	@FindBy(id = "Restart")
	public WebElement clearAnswer;
	
	@FindBy(className = "learning__taskNavPCCurrentTask")
	public WebElement currentTask;
	
	
	//New MCQ

	@FindBy(className = "prMCQ__answersW")
	public WebElement allAnswers;
	
	// New sound
	
	@FindBy(className = "speakerPlayBtn")
	public WebElement sound;
	
	// element for matching game
	
	@FindBy (className = "memoG__boardIW")
	public WebElement gameBoard;
	@FindBy (id = "card2")
	
	public WebElement Card2;
	
	@FindBy (id = "card8")
	public WebElement Card8;
	
	@FindBy (id = "card3")
	public WebElement Card3;
	
	@FindBy (id = "card7")
	public WebElement Card7;
	
	@FindBy (id = "card1")
	public WebElement Card1;
	
	@FindBy (className = "memoG__answersContainerW")
	public WebElement bankMGame;
	
	
	@FindBy(className = "learning__taskNavPCTotalTasks")
	public WebElement totalTasks;
	
	@FindBy(className = "learning__taskNavPagerCounterIW")
	public WebElement taskCounter;
		
	@FindBy (id = "mainAreaTD")
	public WebElement learningAreaBody;
	
	@FindBy(className = "learning__taskNavPCCurrentTask")
	public WebElement lastTaskWithProgress;
	
	@FindBy(className = "layout__radio")
	public List<WebElement> radioButtonAnswers;
	
	@FindBy(className = "lessonMultipleCheck")
	public List<WebElement> checkboxAnswers;
	
	
	@FindBy(className = "prOpenEnded__qaItemText")
	public List<WebElement> textArea;
	
	@FindBy(className = "learning__headerIW")
	public WebElement taskName;
	//
	
	//E-Rater elements and methods
	
	
	//New Fill in the blank
	
	public void selectAnswerFromDropDown2(String questonId, String answerText) throws Exception {
		webDriver.waitForElement("//div[@id='" + questonId + "']", ByTypes.xpath).click();
		List<WebElement> answers = webDriver.getElementsByXpath("//div[contains(@id,'DDLOptions__listItem_aid')]");
		//answers.get(0).click();
		
		String text="";
		for (int i=0;i< answers.size();i++){
			text = answers.get(i).getAttribute("id");
			
			if (text.equalsIgnoreCase(answerText)){
				answers.get(i).click();
				break;
			}
		}
		//webDriver.waitForElement("//div[@id='" + answerText + "']", ByTypes.xpath).click();
	}
	public void checkAnswersSelectedFillTheBlanks2( String answerid ) throws Exception {
		WebElement element = webDriver.waitForElement("//div[@id='" + answerid + "']", ByTypes.xpath);
		if (element == null) {
			testResultService.addFailTest("Answer/s Not Selected", false, true);
		}
	}
	
public void verifyAnswerFillTheBlankSelectedCorrect2 (String questonId) throws Exception {
		
		WebElement element = webDriver.waitForElement("//div[@id='" + questonId + "'][contains(@class,'prFITB__DDLOptionsW prFITB__DDLOptionsW--correct')]", ByTypes.xpath,1,false);
		testResultService.assertEquals(true, element.isDisplayed(),	"Answer is Not Selected Wrong");
	}
public void verifyAnswerFillTheBlankSelectedWrong2 (String questonId) throws Exception {
	
	WebElement element = webDriver.waitForElement("//div[@id='" + questonId + "'][contains(@class,'prFITB__DDLOptionsW prFITB__DDLOptionsW--incorrect')]", ByTypes.xpath);
	testResultService.assertEquals(true, element.isDisplayed(),	"Answer is Not Selected Wrong");
}
	
	public void clickOnOKButton() throws Exception {
		WebElement element = webDriver.waitUntilElementAppears("btnOk", ByTypes.id, 10);
		element.click();
		//webDriver.waitForElement("btnOk", ByTypes.id).click();

	}
	
	public void clickOnCancel() throws Exception {
		webDriver.waitForElement("btnCancel", ByTypes.id).click();

	}
	
	public void waitToAssignmentAlert() throws Exception {
		
		boolean status= webDriver.waitUntilElementAppearsAndReturn("G10", ByTypes.id, 180);
		
		if (!status)
			testResultService.addFailTest("Alert Notification via Nav bar doesn't display", false, true);
	}
	
	public void withOutAlert (boolean stateOfAlert) throws Exception {
		if(stateOfAlert== false){
			waitToAssignmentAlert();
		}
		else {
			tearDown();
		}
}

	//////
	public NewUxLearningArea2(GenericWebDriver webDriver, TestResultService testResultService) throws Exception {
		super(webDriver, testResultService);
		PageFactory.initElements(new AjaxElementLocatorFactory(webDriver.getWebDriver(), 10), this);
				
	}

	@Override
	public GenericPage waitForPageToLoad() throws Exception {
		// TODO Auto-generated method stub
		//webDriver.waitForElementToBeVisisble(lessonListOpenButton);
		webDriver.waitUntilElementAppears(lessonListOpenButton, 30);
		/*
		int i = 0;
		
		while (i<=30 && lessonListOpenButton==null){
			Thread.sleep(1000);
			i++;
		}
		*/
		return null;
	}

	@Override
	public GenericPage OpenPage(String url) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	// general methods
	
	public void checkUnitLessonStepNameOnLanding(String expectedUnitName, String expectedLessonName, String expectedStepName) throws Exception {
		
		openLessonsList();
		String currentUnitName = getUnitTitleInLessonList();
		testResultService.assertEquals(expectedUnitName, currentUnitName, "Unit name not found/do not match");
		closeLessonsList();
		
		String currentLessonName = getLessonNameFromHeader();
		testResultService.assertEquals(expectedLessonName, currentLessonName, "Lesson name not found/do not match");
		
		String currentStepName = getStepNameFromHeader();
		testResultService.assertEquals(expectedStepName, currentStepName, "Step name not found/do not match");
		
		}
	
	@Override
	public void VerificationOfQuestionInstruction(String expectedString) throws Exception {
		
		webDriver.waitUntilElementAppears("learning__headerIW",ByTypes.className, 20);
		//webDriver.waitUntilElementAppears("elm1_word_c",ByTypes.id, 20);
		WebElement element = webDriver.waitForElement("learning__headerIW", ByTypes.className,false,smallTimeOut);
		
		if (element != null){
			String actual = taskInstruction.getText();
			testResultService.assertEquals(expectedString, actual , " Task instruction was not found or not correct");
		}
		else{
			testResultService.addFailTest("Instructions not display");
		}
	}
	
	// lesson list methods
	
	@Override
	public String getLessonNameFromHeader() throws Exception {
		
		String lessonName = "undefined";
		String [] nameParts = lessonNameInHeader.getText().split(":");
		if (nameParts.length>2) lessonName = nameParts[1].concat(":" + nameParts[2]).trim(); 
		else lessonName = nameParts[1].trim();
		
		return lessonName;
	 
	}
	
	public String getLessonNumberFromHeader() throws Exception {
		
		return lessonNameInHeader.getText().split(":")[0].replace("Lesson", "").trim();
	}
	
	public void waitToLearningAreaLoaded() throws Exception {
		
		/*WebElement element = null;
		
		for (int i = 0;i<=10 && element==null;i++){
			element = webDriver.waitForElement("//div[@class='learning__dropDownListTitle']", ByTypes.xpath, false, 1);
		}
		*/
		webDriver.waitUntilElementAppears(stepNameInHeader, 15);
		webDriver.waitUntilElementAppears(learningAreaBody, 15);
	}

	@Override
	public NewUxLearningArea2 openLessonsList() throws Exception {
		webDriver.waitUntilElementAppears(lessonListOpenButton, 10);
		lessonListOpenButton.click();
		Thread.sleep(500);
		return this;
	}
	
	@Override
	public void closeLessonsList() throws Exception {
		overlay.click();
		Thread.sleep(500);
	}
	
	@Override
	public void validateLessonListUnitImage(String unitId) throws Exception {
		
		String style = lessonListUnitImage.getAttribute("style");
		testResultService.assertTrue("Image not found", style.contains("/Runtime/Graphics/Units/U_" + unitId + ".jpg"));

	}
	
		
	public void clickOnLessonByNumber(int lessonNumber) throws Exception {
		lessons.get(lessonNumber-1).click();
	}
	
	public String getLessonNameFromListByNumber(int lessonNumber) throws Exception {
		
		String text = lessons.get(lessonNumber-1).getText();
		String name = text.split("\\.")[1].trim(); 
		
		return name;
	}
	
	public void verifyLessonIsCurrentByNumber(int lessonNumber) throws Exception {
		boolean state = lessons.get(lessonNumber-1).getAttribute("class").contains("current");
		testResultService.assertEquals(true, state, "Lesson is not marked as current");
	}
	
	public String getLessonNumberFromList(int lessonNumber) throws Exception {
		return lessons.get(lessonNumber-1).getText().split("\\.")[0].trim();
	}
	
	public String getUnitTitleInLessonList() throws Exception {
		return lessonListUnitName.getText();
	}
	
	public String getCourseNameInLessonList() throws Exception {
		return lessonListCourseName.getText();
	}
	
	@Override
	public void checkProgressIndicationInLessonList(int lessonNumber, int progressPercent) throws Exception {
				
		boolean isCorrect = false; 
		
		String indication = lessons.get(lessonNumber-1).findElement(By.className("progressBar__progress")).getAttribute("class");
		
		if (progressPercent == 0)
			isCorrect = indication.contains("notStarted");
		
		else if (1 <= progressPercent && progressPercent < 100)
			isCorrect = indication.contains("inProgress");
		
		else if (progressPercent == 100)
			isCorrect = indication.contains("done");
		
		testResultService.assertEquals(true, isCorrect, "Learning Area: progress indication in lesson "+lessonNumber+" not valid");
	}
	
	@Override
	public void checkLessonsCompletedInLessonList(int startOnLesson, int stopOnLesson) throws Exception {
		for (int i = (startOnLesson-1); i < stopOnLesson; i++) {
			checkProgressIndicationInLessonList(i+1, 100);
		}
	}
	
	// step list methods 
	
	@Override
	public String getStepName(int stepNumber) throws Exception {
		
		String text = steps.get(stepNumber-1).findElement(By.className("learning__dropDownList_itemName")).getText();
		String name = text.split("\\.")[1].trim(); 
		
		return name;
	}
	
	@Override
	public String getStepNumber(int stepNumber) throws Exception {
		return steps.get(stepNumber-1).findElement(By.className("learning__dropDownList_itemName")).getText().split("\\.")[0].trim();
	}

	public void checkProgressIndicationInStepList(int stepNumber, StepProgressBox expected, boolean isTest, String testScoreAsText) throws Exception {
		
		boolean isCorrect = false; 
		String indication = "undefined";
		List <WebElement> progressBar = steps.get(stepNumber-1).findElements(By.className("progressBar__progress"));
		
		// check progress indication if Test - should be score in % 
		if (isTest && progressBar.size() == 0) {
			WebElement progress = steps.get(stepNumber-1).findElement(By.className("learning__dropDownList_itemProgress"));
			isCorrect = progress.getText().equalsIgnoreCase(testScoreAsText);
		} else indication = progressBar.get(0).getAttribute("class");
		
		// check progress indication if Not Test - should be progress bar with states: done / not Started / in Progress  
		if (expected == StepProgressBox.empty  && !isTest ) isCorrect = indication.contains("notStarted");
		
		else if (expected == StepProgressBox.half && !isTest) isCorrect = indication.contains("inProgress");
		
		else if (expected == StepProgressBox.done && !isTest) isCorrect = indication.contains("done");
		
		testResultService.assertEquals(true, isCorrect, "Progress indication in Step "+ stepNumber +" not valid");
	}
	
	public void verifyStepIsCurrentByNumber(int stepNumber) throws Exception {
		boolean state = steps.get(stepNumber-1).getAttribute("class").contains("current");
		testResultService.assertEquals(true, state, "Step is not marked as current");
	}
	
	@Override
	public void clickOnStep(int stepNumber) throws Exception {
		clickOnStep(stepNumber, true);
	}
	
	public void clickOnStep(int stepNumber, boolean skipIntro) throws Exception {
		webDriver.waitUntilElementAppears("open", ByTypes.id, 30);
		
		if (stepListOpenButton.findElements(By.id("open")).size() > 0)
				openStepsList();
		
		steps.get(stepNumber-1).click();
		Thread.sleep(500);
		
		if (skipIntro && isTaskCounterHasIntroMode())
			clickOnNextButton(); // to skip Step Intro page
	}
	
	public NewUxUnitObjectivesPage openUnitObjectiveInLA() throws Exception {
		openLessonsList();
		WebElement unitObjectiveIcon = webDriver.getElement(By.xpath("//div[contains(@class,'unitInfoIcon')]"));
		webDriver.hoverOnElement(unitObjectiveIcon);
		Thread.sleep(1500);
		WebElement unitObjectiveText = webDriver.getElement(By.xpath("//div[contains(@class,'unitInfoText')]"));
		testResultService.assertEquals("Unit Objectives",unitObjectiveText.getText());
		unitObjectiveIcon.click();
		return new NewUxUnitObjectivesPage(webDriver, testResultService);
	}

	public void openStepsList() throws Exception {
		stepListOpenButton.click();
		Thread.sleep(500);
	}
	
	public void closeStepsList() throws Exception {
		overlay.click();
	}
	
	public String getStepNameFromHeader() throws Exception {
		
		String stepName = "undefined";
		String [] nameParts = stepNameInHeader.getText().split(":");
		if (nameParts.length>2) stepName = nameParts[1].concat(":" + nameParts[2]).trim(); 
		else stepName = nameParts[1].trim();
		
		return stepName;
	 
	}
	
	public String getStepNameFromIntro() throws Exception {
		
		String stepName = "undefined";
		String [] nameParts = stepNameInIntro.getText().split(":");
		if (nameParts.length>2) stepName = nameParts[1].concat(":" + nameParts[2]).trim(); 
		else stepName = nameParts[1].trim();
		
		return stepName;
	 
	}
	
	public String getStepNumberFromIntro() throws Exception {
		
		return stepNameInIntro.getText().split(":")[0].replace("Step", "").trim();
	 
	}
	
	public String getStepNumberFromHeader() throws Exception {
		
		return stepNameInHeader.getText().split(":")[0].replace("Step", "").trim();
	 
	}
	
	// task bar methods 
	
	public void openTaskBar() throws Exception {
		taskBarOpenButton.click();
		Thread.sleep(1000);
	}
	
	public void closeTaskBar() throws Exception {
		overlay.click();
		Thread.sleep(500);
	}
	
	public void clickOnIntro() throws Exception {
		stepIntroBtn.click();
		Thread.sleep(500);
	}
	
	public void clickOnReviewTestResults() throws Exception {
		reviewTestResultsBtn.click();
		Thread.sleep(1000);
	}
	
	public void checkIfTaskHasCurrentState(int taskNumber, boolean expectCurrent) throws Exception {
		
		boolean isCurrent = getTasksElements().get(taskNumber-1).getAttribute("class").contains("current");
				
		if (expectCurrent)
			testResultService.assertEquals(true, isCurrent, "Task "+taskNumber+" state is not Current");
		else
			testResultService.assertEquals(false, isCurrent, "Task "+taskNumber+" state is Current though should not be current");
	}
	
	public void checkIfTaskHasDoneState(int taskNumber, boolean expectDone) throws Exception {
		
		boolean isDone = getTasksElements().get(taskNumber-1).getAttribute("class").contains("done");
		
		if (expectDone)
			testResultService.assertEquals(true, isDone, "Task "+taskNumber+" state is not Done");
		else
			testResultService.assertEquals(false, isDone, "Task "+taskNumber+" state is Done though should not be");
	}
	
	private List<WebElement> getTasksElements() throws Exception {
		WebElement tasksParent = webDriver.waitForElement("learning__tasksNav_tasksW", ByTypes.id);
		List<WebElement> tasks = tasksParent.findElements(By.className("learning__tasksNav_task"));
		

		return tasks;
	}
	
	@Override
	public void clickOnStartTest() throws Exception {
		//WebElement element = webDriver.waitForElement("//a[@class='btnStartTest']", ByTypes.xpath, false, webDriver.getTimeout());
		WebElement element = webDriver.waitForElement("Start Test", ByTypes.linkText, false, webDriver.getTimeout());
		if (element != null) {
			element.click();
		} else {
			testResultService.addFailTest("Start test element not found", false, true);
		}

	}
	
	@Override
	public void submitTest(boolean approveSubmit) throws Exception {
		openTaskBar();
		submitTestInTaskBar.click();
		if (approveSubmit) {
			approveTest();
		}
		Thread.sleep(2000);

	}
	
	public void submitTestInLastTask(boolean approveSubmit) throws Exception {
		
		submitTestInToolBar.click();
		if (approveSubmit) {
			approveTest();
		}
		Thread.sleep(2000);

	}

	@Override
	public void approveTest() throws Exception {
		
		try{
			WebElement parentElement = webDriver.waitForElement("EdoFrameBoxContent", ByTypes.id,false,2);
			 
			if (parentElement != null){
				WebElement iframe = parentElement.findElement(By.tagName("iframe"));
				webDriver.switchToFrame(iframe);
				
				webDriver.waitForElement("btnOk", ByTypes.id).click();
				webDriver.switchToMainWindow();	
			}
			else
				testResultService.addFailTest("Alert not display", false, true);	
		}
		catch (Exception e) {
			e.printStackTrace();
		}
					
	}
	
	public void clickOnTaskByNumber(int taskNumber) throws Exception {

		List<WebElement> taskBarIsOpen = taskBarOpenButton.findElements(By.id("open"));
		if (taskBarIsOpen.size()!=0) openTaskBar();		
		getTasksElements().get(taskNumber-1).click();
		Thread.sleep(500);
		
	}
		
	public void checkTestTaskMark(int taskNumber, boolean expectedMark) throws Exception {
				
		String locator = getTasksElements().get(taskNumber-1).findElement(By.className("learning__tasksNav_taskVX")).getAttribute("class");
		
		if (expectedMark) {
			testResultService.assertEquals(true, locator.contains("vCheck"), "Task mark is not CORRECT as expected");	
		} else testResultService.assertEquals(true, locator.contains("xCheck"), "Task mark is not WRONG as expected");	
		
	}
	
	public void checkTestTaskMarkIsPartlyCorrect(int taskNumber) throws Exception {
		
		String locator = getTasksElements().get(taskNumber-1).findElement(By.className("learning__tasksNav_taskVX")).getAttribute("class");
		testResultService.assertEquals(true, locator.contains("vxCheck"), "Task mark is not PARTLY CORRECT as expected");	
		
	}
	
	// component test LA methods
	
	@Override
	public void clickOnYourAnswerTab() throws Exception {
		yourAnswerTab.click();

	}
	
	@Override
	public void clickOnCorrectAnswerTab() throws Exception {
		correctAnswerTab.click();

	}
	
	// navigation bar LA methods
	
	public void openAboutUsPage(boolean clickOnTooltip) throws Exception {
		
		if (clickOnTooltip) {
			hoverOnNavBarItemAndVerivyTooltip(NavBarItems.sitemenu__itemAboutUs.toString());
			clickButtonTooltipOnNavBar(NavBarItems.sitemenu__itemAboutUs.toString());			
		} else clickButtonOnNavBar(NavBarItems.sitemenu__itemAboutUs.toString());
					
	}
	
	public void closeAboutUsPage() throws Exception {
		closeModalPopUp();	
		Thread.sleep(1000);
	}
		
	// Resource Tools methods
	
	@Override
	public void clickOnReferenceWords() throws Exception {
		//refWordsTool.click();
		
		WebElement element = webDriver.waitForElement("ref", ByTypes.id, true, webDriver.getTimeout());
		
		if (element ==null){
			testResultService.addFailTest("Reference Word element not found", false, true);
		} else {
			element.click();
		}
	}
	
	@Override
	public void clickOnMainIdea() throws Exception {
		WebElement element=null;
		for (int i=0; i<=10 && element ==null;i++){
			element = webDriver.waitForElement("//li[@id='mi']", ByTypes.xpath, false, 1);
		}
			
		mainIdeaTool.click();

	}
	
	@Override
	public void clickOnVocabTransTool() throws Exception {
		vocabTransTool.click();

	}
		
	@Override
	public void clickOnKeyWords() throws Exception {
		keyWordsTool.click();

	}
	
	@Override
	public void clickOnHearAll() throws Exception {
		//hearAllTool.click();
		webDriver.ClickElement(hearAllTool);

	}

	@Override
	public void clickOnSeeExplanation() throws Exception {
		//webDriver.clickOnElement(seeExplanationTool);
		webDriver.ClickElement(seeExplanationTool);

	}
	
	@Override
	public void clickOnSeeText() throws Exception {
		webDriver.waitUntilElementAppears(seeTextTool, 5);
		seeTextTool.click();

	}
	
	@Override
	public void clickOnToolHear() throws Exception {
		hearVocabTool.click();

	}
	
	public void clickOnViewResource() throws Exception {
		viewResource.click();
		webDriver.sleep(1);

	}
	
	public void checkViewResourceNotClickable() throws Exception {
		
		webDriver.verifyThatElementNotClickable(By.className(viewResBtn), 3);
		
	}
	
	public void closeResourceLayer() throws Exception {
		viewResourceCloseBtn.click();
		webDriver.sleep(1);

	}
	
	
	public void validateQuitPopupMessageinTest(String text, boolean toConfirm) throws Exception {
		//webDriver.switchToFrame(2);
		webDriver.switchToFrame(webDriver.waitForElement("colorBoxIframe", ByTypes.id));
		String message = webDriver.waitForElement("//div[@class='hebDir']", ByTypes.xpath).getText();
		
		
		if (message.contains(text) == false) {
			testResultService.addFailTest("Text : " + text + " was not displayed in the popup message", false, true);
		}
		
		String buttonID = toConfirm ? "btnOk" : "btnCancel";
		 
		webDriver.waitForElement(buttonID, ByTypes.id).click();
		webDriver.switchToMainWindow();

	}
	
	// Practice Tools, Back & Next methods
	
	@Override
	public void clickOnNextButton() throws Exception {
		clickOnNextButton(1);
	}
	
	@Override
	public void clickOnNextButton(int times) throws Exception {
		
		Thread.sleep(500);
		for (int i = 0; i < times; i++) {
			if (times>1)
				webDriver.waitForElement("//*[@id='rightDiv']/ed-la-practicearea/div[2]",ByTypes.xpath,false,1);
			nextBtn.click();
			Thread.sleep(1000);
			}
			//webDriver.waitUntilElementAppears(nextBtn, 10);
			//nextBtn.click();
		}
	
	@Override
	public void clickOnBackButton() throws Exception {
		webDriver.waitUntilElementAppears(backBtn, 10);
		backBtn.click();

		//Thread.sleep(500);
		//webDriver.waitForElement("learning__prevItem", ByTypes.id).click();
	}
	
	@Override
	public void checkThatNextButtonIsNotDisplayedInLA() throws Exception {
						
		webDriver.verifyThatElementNotDisplayed(By.id(nextBtnId),3);
				
	}
		
	public void checkThatBackButtonIsNotDisplayedInLA() throws Exception {
						
		webDriver.verifyThatElementNotDisplayed(By.id(backBtnId),3);
				
	}
	
	public void checkThatReviewTestButtonIsNotDisplayed() throws Exception {
		
		webDriver.verifyThatElementNotDisplayed(By.id(reviewTestBtn),3);
				
	}
	
	public void checkThatBackButtonIsDisabledInLA() throws Exception {

		boolean isDisabled = backBtn.findElement(By.tagName("a")).getAttribute("class").contains("disabled");
		testResultService.assertEquals(true, isDisabled, "Back btn is not disabled");

	}
	
	@Override
	public void verifyTaskCounterValues(int currentExpected, int totalExpected) throws Exception {
		
		try {
			currentTask.isDisplayed();
			totalTasks.isDisplayed();
			
			int currentActual = Integer.parseInt(currentTask.getText());
			int totalActual = Integer.parseInt(totalTasks.getText());
			testResultService.assertEquals(currentActual, currentExpected, "Current task in counter is wrong");
			testResultService.assertEquals(totalActual, totalExpected, "Total task in counter is wrong");
			
        } catch (NoSuchElementException e) {
        	testResultService.addFailTest("Task Counters not found", false, true);
           
        }
						
		
	}
	
	public boolean isTaskCounterHasIntroMode() {
	
		boolean containsIntro=false;
		
		try {
			containsIntro = taskCounter.getText().contains("Intro");
            return containsIntro;
            
        } catch (Exception e) {
            return containsIntro;
        }
	}
	
	@Override
	public void clickOnClearAnswer() throws Exception {
		clearAnswer.click();
		}
	
	@Override
	public void clickOnCheckAnswer() throws Exception {
		checkAnswer.click();
	}
	
	@Override
	public void clickOnSeeAnswer() throws Exception {
		seeAnswer.click();
	}
	
	@Override
	public void makeProgressActionByItemType(int itemTypeId, String itemCode) throws Exception {
		
		//dragAndDrop2 = new NewUxDragAndDropSection2 (webDriver, testResultService);
		//learningArea2 = new NewUxLearningArea2 (webDriver, testResultService);
		
		String skill = itemCode.substring(2, 3);
		
		switch (itemTypeId) {
				
			case 38: case 40: // new video explore
				clickOnPlayNewVideoButton();
								
				
				break;
			 case 11:  case 10: case 25:// MCQ
				if (isCurrentPracticeHasSR()) {
					Thread.sleep(1000);					
					interactSection.waitForStartBtnIsEnabledInPracticeMCQ();
					interactSection.clickTheStartButton();
					Thread.sleep(3000);
					
					} else if (webDriver.waitForElement("pictureFrame", ByTypes.className, false, 1) !=null) {
						selectAnswerRadioByImage(1, 1);					
						
					} else	//selectAnswerRadioByTextNum(1, 1);
				
						SelectRadioBtn("question-1_answer-1");
				break;
				case 3:
		
			selectAnswerRadioByTextNum(1, 1);
		
				break;
				
	
				
			case 1:  // D&D classification 2 columns 
				dragAndDrop2 = new NewUxDragAndDropSection2 (webDriver, testResultService);
				dragAndDrop2.dragAndDropFirstAnswer(TaskTypes.Classification);
				break;
				// Case for the new matching
			case 7: 
				dragAndDrop2 = new NewUxDragAndDropSection2 (webDriver, testResultService);
				dragAndDrop2.dragAndDropFirstAnswer(TaskTypes.Matching);
			break;
			case 23: case 2: case 12: case 18: // D&D close
				dragAndDrop2 = new NewUxDragAndDropSection2 (webDriver, testResultService);
				dragAndDrop2.dragAndDropFirstAnswer(TaskTypes.Close);
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
				if (!skill.equals("v") || itemCode.contains("p40")) { //input[@id ='prOpenEnded__qaItemText--qId_1_alId_1']
					enterOpenSmallSegmentAnswerTextByIndex2("prOpenEnded__qaItemText--qId_1_alId_1", "e");
				}
				else enterOpenSmallSegmentAnswerTextByIndex2("prOpenEnded__qaItemText--qId_1_alId_1", "e");
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
				//dragAndDrop.dragAndDropMatchTextToPicFirstAnswer();
				dragAndDrop2 = new NewUxDragAndDropSection2 (webDriver, testResultService);
				dragAndDrop2.dragAndDropFirstAnswer(TaskTypes.MTTP);
				
			
				break;
			case 51: case 19: case 16: // Fill in the blanks
				String place ="prFITB__DDLOptionsW_Q1_L1";
				String answer = "DDLOptions__listItem_aid_29";
				learningArea2.selectAnswerFromDropDown2(place,answer);
				
				
				break;
			case 17: // Sequence Sentence
				dragAndDrop.retrieveDraggableNewSeqText();
				//dragAndDrop.dragAndDropSequenceAnswerByTextToPlaceInOrder(dragAndDrop.getFirstTileText().substring(0,1), 2);
				break;
			case 5: // Sequence Image
				//dragAndDrop.retrieveDraggableImages();
				dragAndDrop.verifyDragAndDropsImagesRandomPlacementAfterRefreshNewSeq();
				dragAndDrop.dragAndDropReadingSequenceSentence();
				break;
			case 27: // Matching Game
				//RetrieveMemoryGameCards();
				Thread.sleep(2000);
				//SelectTwoCorrectCards();
				Card7.click();
				
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
	 
	private WebElement getSubmitToTeacherBtnElement() throws Exception {
		return webDriver.waitForElement("Send To Teacher", ByTypes.linkText, false, smallTimeOut);
	}
	
	public int retrieveNumOfQuestions() throws Exception {
		WebElement numOfQuestions = webDriver.waitForElement("//span[@class='learning__taskNavPCTotalTasks']", ByTypes.xpath, true, 3);
		return Integer.parseInt(numOfQuestions.getText());
	}
	
	public void navigateToLastTaskInStep() throws Exception {
		
		// Retrieve the Number of Questions in The Test
		int numOfQuestions = retrieveNumOfQuestions();
		
		// Click Next button "+(numOfQuestions-1)+" times
		clickOnNextButton(numOfQuestions-1);
	}

	public void setUnitReflectionAndContinue(boolean setRate) {
		try {
			WebElement element = webDriver.waitForElement("//*[contains(@class,'unitReflection')]",ByTypes.xpath, false, smallTimeOut);
			
			Random rand = new Random(); 
			int value1 = rand.nextInt(3) + 1; 
			int value2 = rand.nextInt(3) + 1;
			int value3 = rand.nextInt(3) + 1;
			
			if (element != null && setRate){
				webDriver.waitForElement("//input[contains(@class,'unitReflection__LessonRadio')][contains(@class,'idx_0_rate_"+ value1 +"')]",ByTypes.xpath, false, 1).click();
				webDriver.waitForElement("//input[contains(@class,'unitReflection__LessonRadio')][contains(@class,'idx_1_rate_"+ value2 +"')]",ByTypes.xpath, false, 1).click();
				webDriver.waitForElement("//input[contains(@class,'unitReflection__LessonRadio')][contains(@class,'idx_2_rate_"+ value3 +"')]",ByTypes.xpath, false, 1).click();
				
				webDriver.waitForElement("unitReflection__save",ByTypes.id, false, 1).click();
			}
			
			clickOnNextButton();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/*public void setUnitReflection(int fillingRow) {
		try {
			WebElement element = webDriver.waitForElement("//*[contains(@class,'unitReflection')]",ByTypes.xpath, false, smallTimeOut);
			
			int rateIndex=0;
			Random rand = new Random(); 
			
			if (element != null) {
				for (int rowIndex=0;rowIndex<fillingRow;rowIndex++) {
					rateIndex = rand.nextInt(3) +1 ;
					webDriver.waitForElement("//*[contains(@class,'idx_"+rowIndex+"_rate_"+ rateIndex +"')]",ByTypes.xpath, false, 1).click();
				}
			
				webDriver.waitForElement("unitReflection__save",ByTypes.id, false, 1).click();
			}	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}*/
	
	private int getMaxUnitReflectionrows() {
		int maxRows=0;
		
		try {
			maxRows = Integer.parseInt(
					(webDriver.waitForElement("unitReflection_unitImageCell", ByTypes.className).getAttribute("rowspan")));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return maxRows;
	}

	public void checkAndBackFromUnitReflection() {
			WebElement element;
			try {
				element = webDriver.waitForElement("//*[contains(@class,'unitReflection')]",ByTypes.xpath, false, smallTimeOut);
				
				if (element != null)
					clickOnBackButton();
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
	public void submitTestWithoutAnswers(boolean setUnitReflection) throws Exception {
		
		// Go to step Test
		clickOnStep(3, false); 
	
		// Click on Start Test
		clickOnStartTest();
		
		// Navigate to Last Task In Step
		navigateToLastTaskInStep();
		
		// Click Submit
		submitTest(true);
		
		// Click Next and Set Unit Reflection
		clickOnNextButton();
		Thread.sleep(2000);
		
		if (setUnitReflection) {
			setUnitReflectionAndContinue(false);
		}
	}
	
	public int getTasksCount() throws Exception {
		List<WebElement> tasks = null;
		try{
			WebElement tasksParent = webDriver.waitForElement("learning__tasksNav_tasksW", ByTypes.id);
			tasks = tasksParent.findElements(By.className("learning__tasksNav_task"));	
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return tasks.size();
	}

	public void IsNavBarClosed() {
		// TODO Auto-generated method stub
		
	}
	
	public void validateSingleRadioAnswerSign(String answer, boolean isCorrect) throws Exception {
		List<WebElement> answersText = webDriver.getWebDriver().findElements(By.xpath("//div[contains(@class,'multiRadioWrapper')]//div [contains(@class,'multiRadio')]"));
		List<WebElement> answersSigns = webDriver.getWebDriver().findElements(By.xpath("//div[contains(@class,'multiRadioWrapper')]//div[contains(@class,'checkAnswerIcon')]"));
		
		for (int i = 0; i < answersText.size(); i++) {
			if (answersText.get(i).getText().equals(answer)) {
				if (isCorrect) {
					testResultService.assertEquals(true, answersSigns.get(i).getAttribute("className").contains("--v"), "Answer Has X sign even though it is correct.");
				} else {
					testResultService.assertEquals(true, answersSigns.get(i).getAttribute("className").contains("--x"), "Answer Has V sign even though it is correct.");
				}
			}
		}
		
	}

	public void waitUntilLearningAreaLoaded() throws Exception {
		webDriver.waitUntilElementAppears(learningAreaBody, 20);
	}

	public int getNumberOfLastTaskWithProgress() {
		
		return Integer.parseInt(lastTaskWithProgress.getText());
	}

	public boolean verifyAnswerStored(String type)throws Exception {
		List<WebElement> elemnts = new ArrayList<WebElement>(); //= radioButtonAnswers;
		if(type.equalsIgnoreCase("radioButton")) {
			elemnts = radioButtonAnswers;
		}
		if(type.equalsIgnoreCase("checkBoxes")) {
			elemnts = checkboxAnswers;
		}
		boolean stored = false;
		for(WebElement el:elemnts) {
			stored = el.isSelected();
			if(stored)
				break;
		}
		//textService.assertEquals("Answer doesn't stored",true, stored);
		return stored;
	}

	public boolean verifyCheckBoxAnswerStored() {
		boolean stored = false;
		for(WebElement el:checkboxAnswers) {
			stored = el.isSelected();
			if(stored)
				break;
		}
		//textService.assertEquals("Answer doesn't stored",true, stored);
		return stored;
		
		
		
	}

	public void typeIntoTextArea(String text) {
		WebElement typeArea = textArea.get(0);
		typeArea.click();
		typeArea.clear();
		typeArea.sendKeys(text);
		
	}

	public boolean verifyValueStoredAtTextArea(String text) {
		boolean stored = false;
		String textFromTextArea = textArea.get(0).getAttribute("ng-reflect-model");
		if(textFromTextArea!=null && textFromTextArea.length()>0 && textFromTextArea.equalsIgnoreCase(text)) {
			stored = true;
		}
		return stored;
	}

	

	public String getTaskName() throws Exception {
		
		String task = taskName.getText();
				return 	task;
	}

	public void verifyAnswersMarked() throws Exception {
		
		int startFromTask = 2;
		List<WebElement> tasks = webDriver.getElementsByXpath("//div[@id='learning__tasksNav_tasksW']/div[contains(@class,'learning__tasksNav_task')]");
		List<WebElement> taskTooltips = webDriver.getElementsByXpath("//span[@class='learning__tasksNav_taskTooltip']");
		for(int i=0;i<taskTooltips.size();i++) {
			webDriver.hoverOnElement(tasks.get(i));
			String taskStatus = taskTooltips.get(i).getText();
			if(i==startFromTask-1||i==startFromTask) {
				textService.assertEquals("Question "+i+" unmarked", "Completed task", taskStatus);
			}else {
				textService.assertEquals("Question "+i+" unmarked", "Task not started", taskStatus);
			}							
		}		
	}
}
