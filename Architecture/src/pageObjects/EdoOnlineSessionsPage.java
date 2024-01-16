package pageObjects;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import drivers.GenericWebDriver;
import services.TestResultService;

public class EdoOnlineSessionsPage extends GenericPage {
	
	@FindBy(xpath = "//div[contains(@class,'onlineSessions__scheduleRow ng-scope')]/div[contains(@class,'name')]")
	public List<WebElement> onlineSessionsTitles;
	
	@FindBy(xpath = "//div[contains(@class,'onlineSessions__scheduleRow ng-scope')]/div[contains(@class,'date')]")
	public List<WebElement> onlineSessionsDates;
	
	@FindBy(xpath = "//div[contains(@class,'onlineSessions__scheduleRow ng-scope')]/div[contains(@class,'enterSessionW ')]//a")
	public List<WebElement> onlineSessionsLinks;
	
	public EdoOnlineSessionsPage(GenericWebDriver webDriver, TestResultService testResultService) throws Exception {
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

	public void validateOnlineSessionsAppearsInTable(String title) throws Exception {
		boolean isDisplayed = false;
		for (int i = 0; i < onlineSessionsTitles.size();i ++) {
			if (onlineSessionsTitles.get(i).getText().equals(title)) {
				isDisplayed = true;
				break;
			}
		}
		
		testResultService.assertEquals(true, isDisplayed, "Online Session: '"+title+"' is not Displayed in the table in ED");
	}
	
	public void validateOnlineSessionsDoesntAppearsInTable(String title) throws Exception {
		boolean isDisplayed = false;
		for (int i = 0; i < onlineSessionsTitles.size();i ++) {
			if (onlineSessionsTitles.get(i).getText().equals(title)) {
				isDisplayed = true;
				break;
			}
		}
		
		testResultService.assertEquals(false, isDisplayed, "Online Session: '"+title+"' is Displayed in the table in ED");
	}
	
	public void validateOnlineSessionsTableIsEmpty() throws Exception {
		testResultService.assertEquals(0, onlineSessionsTitles.size(), "Online Session table is not empty. Size: " + onlineSessionsTitles.size());
	}
	
	public void checkOnlineSessionDetailsInED(String title, String startDate, String endDate, String link) throws Exception {
		
		int index = 0;
		for (int i = 0; i < onlineSessionsTitles.size(); i++) {
			if (onlineSessionsTitles.get(i).getText().equals(title)) {
				index = i;
				break;
			}
		}
		
		// check date- need to add distinctive ids for start date and end dates
		testResultService.assertEquals(true,onlineSessionsDates.get(index).getText().replace("0", "").contains(startDate.replace("0", "")),"Start Date is Not Displayed Correctly.");
		testResultService.assertEquals(true,onlineSessionsDates.get(index+1).getText().replace("0", "").contains(endDate.replace("0", "")), "End Date is Not Displayed Correctly.");
		
		// check the link
		webDriver.ClickElement(onlineSessionsLinks.get(index));
		webDriver.switchToNextTab();
		Thread.sleep(2000);
		webDriver.validateURL(link);
		webDriver.closeNewTab(2);
		Thread.sleep(2000);
	}
}
