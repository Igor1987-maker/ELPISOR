package pageObjects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Enums.ByTypes;
import drivers.GenericWebDriver;
import pageObjects.tms.TmsHomePage;
import services.TestResultService;

public class TMSModerateForumsPage extends GenericPage{
	
	@FindBy(xpath = "//input[@value='  GO  ']")
	public WebElement goButton;
	
	@FindBy(xpath = "//*[@id='PosY2']/tbody/tr/td/div/table[1]/tbody/tr/td/img")
	public List<WebElement> messageRevealIcon;
	
	@FindBy(xpath = "//*[@id='PosY2']/tbody/tr/td/div/table[1]/tbody/tr/td[2]/div")
	public List<WebElement> messageTitleText;
	
	@FindBy(xpath = "//*[@id='PosY2']/tbody/tr/td/div/table[2]/tbody/tr/td/p")
	public WebElement messageContentText;
	
	@FindBy(xpath = "//*[@id='PosY2']/tbody/tr/td/div/table[2]/tbody/tr/td/div/table/tbody/tr/td[6]")
	public WebElement messageDelete;
	
	@FindBy(xpath = "//tr//td[2]//div")
	public List<WebElement> messagesTitles;
	
	//String messageRevealIconXPath = "//*[@id='PosY2']/tbody/tr/td/div/table[1]/tbody/tr/td/img";
	//String messageTitleXPath = "//*[@id='PosY2']/tbody/tr/td/div/table[1]/tbody/tr/td[2]/div";
	//String messageContentXPath = "//*[@id='PosY2']/tbody/tr/td/div/table[2]/tbody/tr/td/p";
	//String MessageDeleteXPath = "//*[@id='PosY2']/tbody/tr/td/div/table[2]/tbody/tr/td/div/table/tbody/tr/td[6]";
	
	
	TmsHomePage tmsHomePage = new TmsHomePage(webDriver, testResultService);

	public TMSModerateForumsPage(GenericWebDriver webDriver, TestResultService testResultService) throws Exception {
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
	
	public void goToModerateForums() throws Exception {
		tmsHomePage.switchToMainFrame();
		tmsHomePage.clickOnCommunication();
		Thread.sleep(3000);
		
		report.startStep("Click on Moderate Forum");
		tmsHomePage.clickOnModerateForums();
		Thread.sleep(2000);
	}
	
	public void selectClassAndForum(String className, String forumName) throws Exception {
		tmsHomePage.switchToFormFrame();
		tmsHomePage.selectClass(className, false, false);
		tmsHomePage.switchToFormFrame();
		webDriver.selectElementFromComboBox("SelectForum",forumName,ByTypes.id, false);
		clickGoButton();
		Thread.sleep(3000);
		webDriver.switchToTopMostFrame();
		tmsHomePage.switchToMainFrame();
	}
	
	public void clickGoButton() {
		webDriver.ClickElement(goButton);
	}
	
	public void checkMessageWasAddedCorrectly(String messageTitle, String messageContent) throws Exception {
		webDriver.switchToFrame(webDriver.getElement(By.id("threadMessages")));
		int index = getMessageIndexByName(messageTitle);
		validateMessageIsDisplayedInList(messageTitle);
		webDriver.ClickElement(messageRevealIcon.get(index));
		
		report.startStep("Compare message content and contract message");
		testResultService.assertEquals(messageContent, messageContentText.getText());
		webDriver.ClickElement(messageRevealIcon.get(index));
	}
	
	public void deleteMessage(String messageTitle) {
		int index = getMessageIndexByName(messageTitle);
		webDriver.ClickElement(messageRevealIcon.get(index));
		webDriver.ClickElement(messageDelete);
		webDriver.closeAlertByAccept();
	}
	
	public void validateMessageIsDisplayedInList(String messageTitle) throws Exception {
		boolean isDisplayed = false;
		for (int i = 0; i < messagesTitles.size(); i++) {
			if (messagesTitles.get(i).getText().equals(messageTitle)) {
				isDisplayed = true;
				break;
			}
		}
		testResultService.assertEquals(true, isDisplayed,"The message: '"+messageTitle+"' is not displayed in Messages List");
	}
	
	public void validateMessageIsNotDisplayedInList(String messageTitle) throws Exception {
		webDriver.switchToFrame(webDriver.getElement(By.id("threadMessages")));
		boolean isDisplayed = false;
		for (int i = 0; i < messagesTitles.size(); i++) {
			if (messagesTitles.get(i).getText().equals(messageTitle)) {
				isDisplayed = true;
				break;
			}
		}
		testResultService.assertEquals(false, isDisplayed,"The message: '"+messageTitle+"' is not displayed in Messages List");
		webDriver.switchToTopMostFrame();
		tmsHomePage.switchToMainFrame();
	}
	
	public int getMessageIndexByName(String messageTitle) {
		int index = 0;
		for (int i = 0; i < messagesTitles.size(); i++) {
			if (messagesTitles.get(i).getText().equals(messageTitle)) {
				index = i;
				break;
			}
		}
		return index;
	}
}
