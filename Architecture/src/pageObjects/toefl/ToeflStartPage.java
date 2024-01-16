package pageObjects.toefl;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Enums.ByTypes;
import drivers.GenericWebDriver;
import pageObjects.GenericPage;
import services.TestResultService;

public class ToeflStartPage extends GenericPage {
	
	@FindBy(xpath = "//div[@class='testEnvIntroTxtWrapper']/h3") 
	public WebElement introTitle;
	
	@FindBy(xpath = "//div[@class='testEnvIntroTxtWrapper']/p") 
	public WebElement introMessage;
	
	@FindBy(xpath = "//div[@class='testEnvIntroTxtWrapper']//a[@class='button']") 
	public WebElement beginButton;

	@FindBy(xpath = "//div[@class='finalTestTitle']/h1") 
	public WebElement headerText;
	
	@FindBy(xpath = "//img") 
	public WebElement picture;
	
	@FindBy(xpath = "//button[contains(@class,'courseBtnext')]") 
	public WebElement nextButton;
	
	@FindBy(xpath = "//div[@class='speakingTimedPanels']//div[contains(@class,'left')]//div[contains(@class,'panelTimer timer_')]") 
	public WebElement stepOneTimer;
	
	@FindBy(xpath = "//div[@class='speakingTimedPanels']//div[contains(@class,'right')]//div[contains(@class,'panelTimer timer_')]") 
	public WebElement stepTwoTimer;
	
	@FindBy(xpath = "//div[@class='speakingTimedPanels']//div[contains(@class,'left')]") 
	public WebElement stepOne;
	
	@FindBy(xpath = "//div[@class='speakingTimedPanels']//div[contains(@class,'right')]") 
	public WebElement stepTwo;
	

	public ToeflStartPage(GenericWebDriver webDriver, TestResultService testResultService) throws Exception {
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

	public void validateTitle(String testName) {
		String actualTitle = introTitle.getText();
		testResultService.assertEquals(testName, actualTitle,"Title is not correct");
	}
	
	public void validateTheWelcomeTextIsNotNull() throws Exception {
		String text = introMessage.getText();
		testResultService.assertEquals(true, text != null, "Welcome Text is Null");
	}
	
	public void clickBeginButton() {
		webDriver.ClickElement(beginButton);
	}
	
	public void validateHeader(String headerName) {
		String actualHeader = headerText.getText();
		testResultService.assertEquals(headerName, actualHeader,"Header is not correct");
	}
	
	public void checkPictureSrc(String src) throws Exception {
		//String pictureSrc = picture.getAttribute("src");
		//testResultService.assertEquals(true, pictureSrc.contains(src),"Picture src is Incorrect");
		
		WebElement pictureSrc = webDriver.waitForElement("//img", ByTypes.xpath, false, 3);
		if (src == null) {
			testResultService.assertEquals(true, pictureSrc==null, "There is a picture on screen even though there shouldnt be.");
		} else {
			if (pictureSrc == null) {
				testResultService.addFailTest("There is no picture on screen. Expected: " + src, false, true);
			} else {
				testResultService.assertEquals(true, pictureSrc.getAttribute("src").contains(src), "Picture src is Incorrect");
			}
		}
	}
	
	public void checkNextButtonIsEnabled() throws Exception {
		testResultService.assertEquals(true, nextButton.isEnabled(), "Next Button is not Enabled");
	}
	
	public void clickNext() throws InterruptedException {
		webDriver.ClickElement(nextButton);
		Thread.sleep(1000);
	}
	
	public void validateTextContainsWantedMessage(String wantedMessage) throws Exception {
		//String actualText = introMessage.getText();
		WebElement actualText = webDriver.waitForElement("//div[@class='qText']//p", ByTypes.xpath, false, 3);
		if (actualText == null) {
			actualText = webDriver.waitForElement("//div[@class='testEnvIntroTxtWrapper']//p", ByTypes.xpath, false, 3);
		}
		
		if (wantedMessage == null) {
			if (actualText != null) {
				testResultService.assertEquals(true, actualText.getText().isEmpty(), "Tjere is text m=Message on screen even tough there shouldn't be");
			} else {
				testResultService.assertEquals(true, actualText==null, "There is text Message on screen even though there shouldnt be.");
			}
		} else {
			if (actualText == null) {
				testResultService.addFailTest("There is no message on screen. Expected: " + wantedMessage, false, true);
			} else {
				testResultService.assertEquals(true, actualText.getText().contains(wantedMessage), "Text Doesn't Contain the Wanted Message");
			}
		}
	}
	
	public int getStepTimerCount(int stepNumber) {
		String time = "";
		if (stepNumber == 1) {
			time = stepOneTimer.getText();
		} else if (stepNumber == 2) {
			time = stepTwoTimer.getText();
		}
		String seconds = time.split(":")[1];
		if (time.split(":")[0].equals("01") && time.split(":")[1].equals("00")) {
			seconds="60";
		}
		return Integer.parseInt(seconds);
	}
	
	public void validateStepOneIsDisabled() throws Exception {
		String elementClassName = stepOne.getAttribute("className");
		testResultService.assertEquals(true, elementClassName.contains("disable"), "Step One is Not Disabled");
	}
	
	public void validateStepTwoIsDisabled() throws Exception {
		String elementClassName = stepTwo.getAttribute("className");
		testResultService.assertEquals(true, elementClassName.contains("disable"), "Step Two is Not Disabled");
	}
	
	public void validateStepOneIsEnabled() throws Exception {
		String elementClassName = stepOne.getAttribute("className");
		testResultService.assertEquals(false, elementClassName.contains("disable"), "Step One is Not Enabled");
	}
	
	public void validateStepTwoIsEnabled() throws Exception {
		String elementClassName = stepTwo.getAttribute("className");
		testResultService.assertEquals(false, elementClassName.contains("disable"), "Step Two is Not Enabled");
	}
	
	public void checkTimers(String expectedMessage, String expectedPicture) throws Exception {
		
		waitUntilFirstTimerIsDisplayed();
		
		report.startStep("Get Step One Timer Count");
		int stepOneTimerCount = getStepTimerCount(1);
		
		report.startStep("Get Step Two Timer Count");
		int stepTwoTimerCount = getStepTimerCount(2);
		
		report.startStep("Validate Text is Correct");
		validateTextContainsWantedMessage(expectedMessage);
		
		report.startStep("Validate Picture src");
		checkPictureSrc(expectedPicture);
		
		report.startStep("Validate Step 1 is enabled");
		validateStepOneIsEnabled();
		
		report.startStep("Validate Step 2 is disabled");
		validateStepTwoIsDisabled();
		
		report.startStep("Wait for the Time In the Counter to be Over");
		int timeToWait = stepOneTimerCount * 1000;
		Thread.sleep(timeToWait);
		
		report.startStep("Validate Step 1 is Disabled");
		validateStepOneIsDisabled();
		
		report.startStep("Validate Step 2 is enabled");
		validateStepTwoIsEnabled();
		
		report.startStep("Wait for the Time In the Counter to be Over");
		timeToWait = stepTwoTimerCount * 1000;
		Thread.sleep(timeToWait);
	}
	
	public void waitUnilFirstStepTimerIsEnabled() throws Exception{
		String timeClassName = stepOne.getAttribute("className");
		int i = 0;
		while (timeClassName.contains("disable") && i < 100) {
			timeClassName = stepOne.getAttribute("className");
			i++;
		}	
	}
	
	public void waitUntilFirstTimerIsDisplayed(){
		boolean isDisplayed = webDriver.isDisplayed(stepOneTimer);  
		int i = 0;
		while (!isDisplayed && i < 30) {
			isDisplayed = webDriver.isDisplayed(stepOneTimer);
		}
	}
}
