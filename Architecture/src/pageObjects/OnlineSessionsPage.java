package pageObjects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import drivers.GenericWebDriver;
import pageObjects.tms.TmsHomePage;
import services.TestResultService;

public class OnlineSessionsPage extends GenericPage{
	
	@FindBy(xpath = "//div[contains(@id,'appointment')]/div[contains(@class,'name')]")
	public List<WebElement> onlineSessionsNamesTable;
	
	@FindBy(xpath = "//div[contains(@id,'appointment')]/div[contains(@class,'date')]")
	public List<WebElement> onlineSessionsDatesTable;
	
	@FindBy(xpath = "//div[contains(@id,'appointment')]/div[contains(@class,'participents')]")
	public List<WebElement> onlineSessionsParticipantsTable;
	
	@FindBy(xpath = "//div[contains(@id,'appointment')]/div[contains(@class,'enterSessionW')]//a")
	public List<WebElement> onlineSessionsLinksTable;
	
	TmsHomePage tmsHomePage = new TmsHomePage(webDriver, testResultService);

	public OnlineSessionsPage(GenericWebDriver webDriver, TestResultService testResultService) throws Exception {
		super(webDriver, testResultService);
		PageFactory.initElements(webDriver.getWebDriver(), this);	
		// TODO Auto-generated constructor stub
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
	
	public void goToOnlineSessions() throws Exception {
		try {
			report.startStep("Click on Communication");
			tmsHomePage.switchToMainFrame();
			tmsHomePage.clickOnCommunication();
			
			report.startStep("Click on Online Sessions");
			tmsHomePage.clickOnOnlineSessions();
		} catch (Exception e) {
			testResultService.addFailTest("FAIL: Go to Notification Center. Error: " + e);
		}
	}
	
	public void checkNewOnlineSessionAppearsInTable(String title) throws Exception {
		try {
			boolean onlineSessionExist = false;
			for (int i = 0; i <onlineSessionsNamesTable.size(); i++) {
				if (onlineSessionsNamesTable.get(i).getText().equals(title)) {
					onlineSessionExist = true;
					break;
				} 
			}
			
			testResultService.assertEquals(true, onlineSessionExist, "Online Session: '" + title + "' does not appear in the online sessions table.");
		} catch (Exception e) {
			testResultService.addFailTest("FAIL: Check New Online Session is Displayed in the Online Session Table. Error : " + e);
		}
	}

	public void deleteOnlineSession(String title) throws Exception {
		try {	
			boolean isOnlineSessionFound = false;
			for (int i = 0; i < onlineSessionsNamesTable.size(); i++) {
				if (onlineSessionsNamesTable.get(i).getText().equals(title)) {
					webDriver.ClickElement(onlineSessionsNamesTable.get(i));
					tmsHomePage.clickOnPromotionAreaMenuButton("Delete");
					webDriver.closeAlertByAccept();
					isOnlineSessionFound = true;
					break;
				}
			}
			
			if (!isOnlineSessionFound) {
				testResultService.addFailTest("The Online Session: '" +title+ "' was not found in the online sessions list");

			}
		} catch (Exception e) {
			testResultService.addFailTest("FAIL: Delete Online Session. Error: " + e);
		}
	}
	
	public boolean verifyOnlineSessionIsNotDisplayedInTheTable(String title) throws Exception {
		try {
			boolean onlineSessionDisplayed = false;
			for (int i = 0; i <onlineSessionsNamesTable.size(); i++) {
				if (onlineSessionsNamesTable.get(i).getText().equals(title)) {
					onlineSessionDisplayed = true;
					break;
				}
			}
	
			testResultService.assertEquals(false, onlineSessionDisplayed, "The Online Session: '" +title+ "' is displayed in the Online Sessions List.");
			if (onlineSessionDisplayed) {
				return false;
			} else {
				return true;
			}
		} catch (Exception e) {
			testResultService.addFailTest("FAIL: Check Online Session: '" +title+ "' is not displayed in the Online Sessions List. Error: " + e);
			return false;
		}
	}
	
	public void checkOnlineSessionDetails(String title, String startDate, String endDate,String classes, String link) throws Exception {
		int index = 0;
		List<WebElement> wbe = onlineSessionsNamesTable;
		for (int i = 0; i < onlineSessionsNamesTable.size(); i++) {
			if (onlineSessionsNamesTable.get(i).getText().equals(title)) {
				index = i;
				break;
			}
		}
		
		// check date
		testResultService.assertEquals(true,onlineSessionsDatesTable.get(index).getText().replace("0", "").contains(startDate.replace("0", "")),"Start Date is Not Displayed Correctly.");
		testResultService.assertEquals(true,onlineSessionsDatesTable.get(index+1).getText().replace("0", "").contains(endDate.replace("0", "")), "End Date is Not Displayed Correctly.");

		// check classes
		//testResultService.assertEquals(String.join(", ", classes), onlineSessionsParticipantsTable.get(index).getText(),"Participants are Not Displayed Correctly.");
		testResultService.assertEquals(onlineSessionsParticipantsTable.get(index).getText().contains(String.join(", ", classes)), true);
		// check the link
		webDriver.ClickElement(onlineSessionsLinksTable.get(index));
		webDriver.switchToNextTab();
		Thread.sleep(1000);
		webDriver.validateURL(link);
		webDriver.closeNewTab(2);
		tmsHomePage.switchToMainFrame();
		Thread.sleep(2000);
	}
}
