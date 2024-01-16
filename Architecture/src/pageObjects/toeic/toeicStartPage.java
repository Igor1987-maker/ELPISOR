package pageObjects.toeic;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Enums.ByTypes;
import drivers.GenericWebDriver;
import pageObjects.GenericPage;
import services.TestResultService;

public class toeicStartPage extends GenericPage {
	
	@FindBy(tagName = "h1")
	public WebElement title;
	
	@FindBy(id = "modeGo")
	public WebElement goButton;
	
	@FindBy(id = "volumeCheckBtn")
	public WebElement testYourSoundButton;
	
	@FindBy(xpath = "//div[@class='StartBT']/a")
	public WebElement startButton;
	
	@FindBy(xpath = "//div[@class='welcomeText']") 
	public WebElement welcomeText;
	
	@FindBy(id = "cboxContent")
	public WebElement resumeTestMessage;
	
	@FindBy(id = "rdChooseTest2")
	public WebElement startNewTestOption;
	
	@FindBy(id = "resumeTestBtn")
	public WebElement goButtonInResume;
	
	@FindBy(id = "exitTestBtn")
	public WebElement okButtonOnExitMsg;
	
	@FindBy(xpath = "//div[@class='unitIntro']") 
	public WebElement introText;
	
	public toeicStartPage(GenericWebDriver webDriver,
			TestResultService testResultService) throws Exception {
		super(webDriver, testResultService);
		// TODO Auto-generated constructor stub
		PageFactory.initElements(webDriver.getWebDriver(), this);
	}

	@Override
	public toeicStartPage OpenPage(String url) throws Exception {
		// TODO Auto-generated method stub
		webDriver.openUrl(url);
		return this;
	}

	@Override
	public GenericPage waitForPageToLoad() throws Exception {
		// TODO Auto-generated method stub
		webDriver.waitUntilElementAppears("//div[@class='toeicTestTitle']//h1", 100);
		return null;
	}
	
	public void validateTitle(String testName) {
		webDriver.waitUntilElementAppears(title,60);
		String actualTitle = title.getText();
		testResultService.assertEquals(testName, actualTitle,"Title is not correct");
	}

	public void clickGoButton() {
		webDriver.ClickElement(goButton);
	}

	public void checkTestSoundButtonIsClickable() throws Exception {
		webDriver.waitUntilElementAppears(testYourSoundButton, 10);
		boolean isClickable = testYourSoundButton.isEnabled();
		testResultService.assertEquals(true, isClickable,"'Test Your Sound' Button is Not Clickable");
	}
	
	public void validateTheWelcomeTextIsNotNull() throws Exception {
		webDriver.waitUntilElementAppears(welcomeText,10);
		String text = welcomeText.getText();
		testResultService.assertEquals(true, text != null, "Welcome Text is Null");
	}
	
	public void clickStart() {
		webDriver.waitUntilElementAppears(startButton,10);
		webDriver.ClickElement(startButton);
	}
	
	public void pressStartNewTestInResumePopUp() {
		webDriver.waitUntilElementAppears(resumeTestMessage, 10);
		if (resumeTestMessage.getText().contains("The system detected that you stopped in the middle of a test.")) {
			startNewTestOption.click();
			goButtonInResume.click();
		}
	}
	
	public void clickOkOnExitMessage() {
		webDriver.ClickElement(okButtonOnExitMsg);
	}
	
	public void validateDetailsInToeicStartPage(String testName) throws Exception {
		// validate url, name and click start
			webDriver.switchToNewWindow();
			waitForPageToLoad();
					
		report.startStep("Validate the URL is Correct");
			webDriver.validateURL("/Runtime/Test.aspx"); // https://toeicolpc1.edusoftrd.com/Runtime/Test.aspx
			Thread.sleep(1000);
	
		report.startStep("Validate the Title is the Name of the First Test");
			validateTitle(testName);
			
		report.startStep("Validate 'Test Your Sound' Button is Clickable");
			checkTestSoundButtonIsClickable();
		
		report.startStep("Validate the Welcome Text is Not Null");
			validateTheWelcomeTextIsNotNull();
			
		report.startStep("Click Start");
			clickStart();
			
		report.startStep("Validate the Intro Text is Not Null");
			validateIntroTextIsNotNull();
	
		// Initialize toeic results page
			toeicResultsPage toeicResultspage = new toeicResultsPage(webDriver,testResultService);

		// close with the small x
			report.startStep("Close the Toeic Results Page");
			webDriver.ClickElement(toeicResultspage.closeButton);
	
			clickOkOnExitMessage();
			
			webDriver.switchToPreviousTab();
			Thread.sleep(3000);		
	}
	
	public void validateIntroTextIsNotNull() throws Exception {
		webDriver.waitUntilElementAppears(introText,10);
		String text = introText.getText();
		testResultService.assertEquals(true, text != null, "Intro Text is Null");
	}
}
