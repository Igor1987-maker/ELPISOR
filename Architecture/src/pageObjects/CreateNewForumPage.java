package pageObjects;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import drivers.GenericWebDriver;
import pageObjects.tms.TmsHomePage;
import services.TestResultService;

public class CreateNewForumPage extends GenericPage {
	
	@FindBy(xpath = "//*[@id='con']/tr/td[2]/a")
	public List<WebElement> editForumButtons;

	TmsHomePage tmsHomePage = new TmsHomePage(webDriver, testResultService);

	public CreateNewForumPage(GenericWebDriver webDriver, TestResultService testResultService) throws Exception {
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
	
	public List<WebElement> createNewForum(String forumName, String firstMessage) throws Exception {
		
		report.startStep("Open new forum popup");
		tmsHomePage.clickOnPromotionAreaMenuButton("New");
		webDriver.switchToPopup();
		
		List<WebElement> textInputs = webDriver.getElementsByXpath("//tr/td/input");
		List<WebElement> textAreas = webDriver.getElementsByXpath("//tr/td/textarea");
		for (int i = 0; i < 3; i++) {
			if(textInputs.get(i).getAttribute("name").equals("topicTitle")) {
				textInputs.get(i).sendKeys(forumName);
			} else {
				if(textInputs.get(i).getAttribute("name").equals("welcomeTitle")) {
					textInputs.get(i).sendKeys(firstMessage);
				} else {
					textInputs.get(i).click();
					break;
				}
			}
			
			if(textAreas.get(i).getAttribute("name").equals("introText")) {
				textAreas.get(i).sendKeys(forumName);
			} else {
				textAreas.get(i).sendKeys(firstMessage);
			}
		}
		return textInputs;
	}
	
	public String editForum(List<WebElement> textInputs, String forumName) throws Exception {
		webDriver.switchToMainWindow();
		tmsHomePage.switchToMainFrame();
		webDriver.ClickElement(editForumButtons.get(0));
		webDriver.switchToPopup();
		textInputs = webDriver.getElementsByXpath("//tr/td/input");
		forumName = forumName + "123";
		String newForumName = forumName;
		textInputs.get(0).clear();
		Thread.sleep(1000);
		textInputs.get(0).sendKeys(forumName);
		Thread.sleep(1000);
		textInputs.get(2).click();
		Thread.sleep(1000);
		webDriver.switchToMainWindow();
		tmsHomePage.switchToMainFrame();
		return newForumName;
	}

}
