package pageObjects.tms;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import Enums.ByTypes;
import drivers.GenericWebDriver;
import pageObjects.GenericPage;
import services.TestResultService;

public class StudentRecordingsPage extends GenericPage {

	@FindBy(tagName = "h1")
	public WebElement mainTitle;
	
	@FindBy(className = "headerInfo")
	public WebElement mainInstruction;
	
	@FindBy(id = "playBtnStdRecImg")
	public WebElement playStudentRecBtn;	
	
	@FindBy(id = "stopBtnStdRecImg")
	public WebElement stopStudentRecBtn;	
	
	@FindBy(id = "playBtnMasterRecImg")
	public WebElement playOriginalRecBtn;
	
	@FindBy(id = "stopBtnMasterRec")
	public WebElement stopOriginalRecBtn;
	
	@FindBy(id = "RateScore")
	public WebElement finalScore;	
	
	@FindBy(id = "rbRec")
	public WebElement recording;
	
	@FindBy(xpath = "//span[text()[contains(.,'Record No.1')]]/preceding-sibling::input")
	
	public WebElement firstRecording;
	@FindBy(className = "rightSideOS")
	public WebElement rightPanelOpenSpeech;
	
	@FindBy(linkText = "Save Score")
	public WebElement saveScore;
	
	@FindBy(xpath = "//div[@class='rightSide']/div[1]/h3")
	public WebElement originalRecTitle;
	
	@FindBy(xpath = "//div[@class='rightSide']/div[2]/h3")
	public WebElement originalTextTitle;
	
	@FindBy(how = How.XPATH, using = "//div[@class='rightSide']//div[@class='inputScore']/h3")
	public List<WebElement> autoScoreTitle;
		
	@FindBy(id = "RecTxt")
	public WebElement originalText;
	
	@FindBy(xpath = "//div[@class='studentDetails']//span[1]")
	public WebElement studentFirstLastName;
	
	@FindBy(how = How.XPATH, using = "//div[@class='studentDetails']//span")
	public List<WebElement> studentDetails;
	
	@FindBy(xpath = "//div[@class='studentDetails']//span[2]")
	public WebElement className;
	
	@FindBy(xpath = "//div[@id='SliderPanel']/h3")
	public WebElement sliderTitle;
	
	@FindBy(name = "tRecGrd")
	public WebElement recordGrade;
	
	@FindBy(name = "date")
	public WebElement recordDate;
			
	@FindBy(how = How.ID, using = "AutoScore")
	public List<WebElement> autoScore;	
	
	@FindBy(how = How.XPATH, using = "//div[@id='RatePanel']//h4")
	public List<WebElement> scoreCategories;
		
	/*@FindAll(@FindBy(className = "magazine__articleItemW"))
	public List <WebElement> magazineArticles;*/
	
	int scoreOrder;
	private static final String MAIN_TITLE = "Student's Recordings";
	private static final String MAIN_INSTRUCTION = "Select a recording from the list on the left-hand side of the screen. Listen to the student's recording and provide a score for each of three categories. You can listen to the original recording or view the speaking task on the right-hand side of the screen.";
	private static final String ORIG_REC_TITLE = "Original Recording";
	private static final String ORIG_TEXT_TITLE = "Recording Text";
	private static final String AUTO_SCORE_TITLE = "Automated Score:";
	private static final String SCORE_CAT_OS_1 = "Content";
	private static final String SCORE_CAT_OS_2 = "Use of English";
	private static final String SCORE_CAT_SR_1 = "Phonemes";
	private static final String SCORE_CAT_SR_2 = "Intonation and Stress";
	private static final String SCORE_CAT_3 = "Overall Pronunciation";
	
	
	public StudentRecordingsPage(GenericWebDriver webDriver,TestResultService testResultService) throws Exception {
		super(webDriver,testResultService);
		PageFactory.initElements(webDriver.getWebDriver(), this);
		
		// TODO Auto-generated constructor stub
	}

	@Override
	public GenericPage waitForPageToLoad() throws Exception {
		webDriver.waitForElement("Student's Recordings", ByTypes.linkText);
		return this;
	}

	@Override
	public GenericPage OpenPage(String url) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public StudentRecordingsPage playStudentRecording() throws Exception {
		playStudentRecBtn.click();
		return this;
	}

	public void scorePhonemes(int score) throws Exception {
		scoreOrder = getScoreOrder(score);
		webDriver.waitForElement("//*[@id='RatePanel']/div[1]/ul/li[" + scoreOrder
				+ "]/div/input", ByTypes.xpath).click();
	}

	public void scoreIntonationAndStress(int score) throws Exception {
		scoreOrder = getScoreOrder(score);
		webDriver.waitForElement("//*[@id='RatePanel']/div[2]/ul/li[" + scoreOrder
				+ "]/div/input", ByTypes.xpath).click();
	}

	public void scoreOverallPronunciation(int score) throws Exception {
		scoreOrder = getScoreOrder(score);
		webDriver.waitForElement("//*[@id='RatePanel']/div[3]/ul/li[" + scoreOrder
				+ "]/div/input", ByTypes.xpath).click();
	}

	public String getFinalScore() throws Exception {
		
		return finalScore.getAttribute("value");
	}
	
	public String getRecordScore() throws Exception {
		
		return recordGrade.getText();
	}

	public String getAutomatedScore() throws Exception {
			
		return autoScore.get(0).getAttribute("value");
		
	}
	
	public void checkThatAutomatedScoreNotDisplayed() throws Exception {
		//WebElement autoScore = webDriver.waitForElement("AutoScore", ByTypes.id, 5, false, "Autoscore section");
		
		if (autoScore.size() != 0) testResultService.addFailTest("Autoscore section displayed though it should not");
	}

	public StudentRecordingsPage selectRecording() throws Exception {
		recording.click();
		return this;
	}
	
	public void selectFirstRecording() throws Exception {
		
		//WebElement rec = webDriver.waitForElement("//span[text()[contains(.,'Record No.1')]]/preceding-sibling::input", ByTypes.xpath,60,true);
		firstRecording.click();
				
	}
	
	public void selectFirstRecordingByCourseUnitLesson(String courseName, String unitName, String lessonName) throws Exception {
		
		WebElement course = webDriver.waitForElement("//li[.//span/b[text()[contains(.,'"+courseName+"')]]]", ByTypes.xpath, 60, true);
		WebElement unit = course.findElement(By.xpath(".//following-sibling::ul[.//span/b[text()[contains(.,'"+unitName+"')]]]"));
		WebElement lesson = unit.findElement(By.xpath(".//li[.//span/b[text()[contains(.,'"+lessonName+"')]]]"));
				
		WebElement record = lesson.findElement(By.xpath(".//following-sibling::ul//span[text()[contains(.,'Record No.1')]]/preceding-sibling::input"));
		
		record.click();
					
	}
	
	public void verifyOpenSpeechOriginalQuestionTitle() throws Exception {
		//String titleString = webDriver.waitForElement("rightSideOS", ByTypes.className).findElement(By.tagName("h3")).getText();
		String titleString = rightPanelOpenSpeech.findElement(By.tagName("h3")).getText();
		testResultService.assertEquals("Open speech task:", titleString, "Title of Open Speech question is not valid");
	}
	
	public void verifyOriginalRecTitleSR() throws Exception {
		
		testResultService.assertEquals(ORIG_REC_TITLE, originalRecTitle.getText(), "Title of Original Rec is not valid");
	}
	
	public void verifyOriginalTextTitleSR() throws Exception {
		
		testResultService.assertEquals(ORIG_TEXT_TITLE, originalTextTitle.getText(), "Title of Original Text is not valid");
	}
	
	public void verifyAutoScoreTitleSR() throws Exception {
		
		testResultService.assertEquals(AUTO_SCORE_TITLE, autoScoreTitle.get(0).getText(), "Title of Auto Score is not valid");
	}
	
	public void verifyMainTitle() throws Exception {
		
		testResultService.assertEquals(MAIN_TITLE, mainTitle.getText(), "Main title is not valid");
	}
	
	public void pressSaveScore() throws Exception {
		saveScore.click();
	}
	
	public String getOriginalSegmentText() throws Exception {
		return originalText.getText();
	}
	
	public String getStudentFirstLastName() throws Exception {
		
		return studentDetails.get(0).getText();
		
	}
	
	public String getClassName() throws Exception {
		
		return studentDetails.get(1).getText();
	}
	
	public void verifyMainInstruction() throws Exception {
		
		testResultService.assertEquals(MAIN_INSTRUCTION, mainInstruction.getText(), "Main instruction text is not correct");
		
		
	}
	
	public void verifyScoreCategoriesOpenSpeech() throws Exception {
		
		testResultService.assertEquals(SCORE_CAT_OS_1, scoreCategories.get(0).getText(), "Open Speech score category 1 not correct");
		testResultService.assertEquals(SCORE_CAT_OS_2, scoreCategories.get(1).getText(), "Open Speech score category 2 not correct");
		testResultService.assertEquals(SCORE_CAT_3, scoreCategories.get(2).getText(), "Open Speech score category 3 not correct");
				
	}
	
	public void verifyScoreCategoriesSR() throws Exception {
		
		testResultService.assertEquals(SCORE_CAT_SR_1, scoreCategories.get(0).getText(), "Speech Recognition score category 1 not correct");
		testResultService.assertEquals(SCORE_CAT_SR_2, scoreCategories.get(1).getText(), "Speech Recognition score category 2 not correct");
		testResultService.assertEquals(SCORE_CAT_3, scoreCategories.get(2).getText(), "Speech Recognition score category 3 not correct");
				
	}
		
	
	private int getScoreOrder (int expectedScore) throws Exception {
		
		int order = 0;
		
		switch (expectedScore){
			case 1:
			order = 5;
			break;
			case 2:
			order = 4;
			break;
			case 3:
			order = 3;
			break;
			case 4:
			order = 2;
			break;
			case 5:
			order = 1;
			break;
						
		}
		
		return order;
		
	}
	

}
