package pageObjects;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import drivers.GenericWebDriver;
import pageObjects.tms.TmsHomePage;
import services.TestResultService;

public class TMSForumPage extends GenericPage {
	
	@FindBy(xpath = "//*[@id='con']/tr/td[4]")
	public List<WebElement> forumsNames;
	
	@FindBy(xpath = "//*[@id='con']/tr/td[3]")
	public List<WebElement> forumsCheckBoxes;
	
	TmsHomePage tmsHomePage = new TmsHomePage(webDriver, testResultService);

	public TMSForumPage(GenericWebDriver webDriver, TestResultService testResultService) throws Exception {
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
	
	public void goToForums() throws Exception {
		report.startStep("Click on Communication");
		tmsHomePage.switchToMainFrame();
		tmsHomePage.clickOnCommunication();
		Thread.sleep(3000);
	
		report.startStep("Click on My Forums");
		tmsHomePage.clickOnMyForums();
		Thread.sleep(2000);
	}

	public boolean validateForumIsDisplayedInList(String name) throws Exception {
		boolean isDisplayed = false;
		for (int i = 0; i < forumsNames.size(); i++) {
			if (forumsNames.get(i).getText().equals(name)) {
				isDisplayed = true;
				break;
			}
		}
		testResultService.assertEquals(true, isDisplayed,"The forum: '"+name+"' is not displayed in Forums List");
		return isDisplayed;
	}
	
	public void deleteForum(String name) throws Exception {
		
		boolean isDisplayed = false;
		for (int i = 0; i < forumsNames.size(); i++) {
			if (forumsNames.get(i).getText().equals(name)) {
				webDriver.ClickElement(forumsCheckBoxes.get(i));
				isDisplayed = true;
				break;
			}
		}
		
		if (isDisplayed) {
			tmsHomePage.clickOnPromotionAreaMenuButton("Delete");
			webDriver.closeAlertByAccept();
		} else {
			testResultService.addFailTest("Forum: '"+name+"' is not displayed in list and therefore cannot be deleted.");
		}
	}
	
	public boolean validateForumIsNotDisplayedInList(String name) throws Exception {
		boolean isDisplayed = false;
		for (int i = 0; i < forumsNames.size(); i++) {
			if (forumsNames.get(i).getText().equals(name)) {
				isDisplayed = true;
				break;
			}
		}
		testResultService.assertEquals(false, isDisplayed,"The forum: '"+name+"' is displayed in Forums List");
		if (isDisplayed) {
			return false;
		} else {
			return true;
		}
	}
}
