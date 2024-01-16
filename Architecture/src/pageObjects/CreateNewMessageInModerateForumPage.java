package pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Enums.ByTypes;
import drivers.GenericWebDriver;
import pageObjects.tms.TmsHomePage;
import services.TestResultService;

public class CreateNewMessageInModerateForumPage extends GenericPage{
	
	@FindBy(id = "postTitle")
	public WebElement title;
	
	@FindBy(id = "tinymce")
	public WebElement content;
	
	@FindBy(xpath = "//*[@id='leftSide']/table/tbody/tr/td[2]/div/table/tbody/tr/td[1]")
	public WebElement sendButton;
	
	TmsHomePage tmsHomePage = new TmsHomePage(webDriver, testResultService);

	public CreateNewMessageInModerateForumPage(GenericWebDriver webDriver, TestResultService testResultService)
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

	public void addNewMessage(String messageTitle, String messageContent) throws Exception {
		tmsHomePage.clickOnPromotionAreaMenuButton("Add new message");
		webDriver.switchToPopup();
		Thread.sleep(3000);
			
		report.startStep("Compose And Submit New Message");	
		webDriver.SendKeys(title, messageTitle);
		webDriver.switchToFrame(webDriver.getElement(By.id("postText_ifr")));
		webDriver.SendKeys(content, messageContent);
		webDriver.switchToTopMostFrame();
		webDriver.ClickElement(sendButton);
		Thread.sleep(2000);
		webDriver.switchToMainWindow();
		tmsHomePage.switchToMainFrame();
	}
}
