package pageObjects;

import drivers.GenericWebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import pageObjects.tms.TmsHomePage;
import services.TestResultService;

import java.util.List;

public class NotificationsPage extends GenericPage{
	
	@FindBy(xpath = "//*[contains(@id,'jqg')]/td[2]")//"#notificationSlidesGrid tr:nth-child(2) td:nth-child(2)")
	public List<WebElement> notificationNameTable;
	
	@FindBy(xpath = "//*[contains(@id,'jqg')]/td[3]")//"#notificationSlidesGrid tr:nth-child(2) td:nth-child(3)")
	public List<WebElement> notificationStatusTable;

	@FindBy(linkText = "Push Notifications Center")
	public WebElement pushNotifications;
	
	TmsHomePage tmsHomePage = new TmsHomePage(webDriver, testResultService);

	public NotificationsPage(GenericWebDriver webDriver, TestResultService testResultService) throws Exception {
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
	
	public void goToNotificationCenter() throws Exception {
		try {
			report.startStep("Click on Communication");
			tmsHomePage.switchToMainFrame();
			tmsHomePage.clickOnCommunication();
			
			report.startStep("Click on Notification Center");
			tmsHomePage.clickOnNotificationCenter();
			
			// check notification center page is displayed with fields: new, publish, edit, delete
			//
		} catch (Exception e) {
			testResultService.addFailTest("FAIL: Go to Notification Center. Error: " + e);
		}
	}

	public void goToPushNotificationsCenter() throws Exception {
		try {
			report.startStep("Click on Communication");
				tmsHomePage.switchToMainFrame();
				tmsHomePage.clickOnCommunication();
				Thread.sleep(1000);

			report.startStep("Click on Push Notification Center");
				//tmsHomePage.clickOnNotificationCenter();
				pushNotifications.click();

		} catch (Exception e) {
			testResultService.addFailTest("FAIL: Go to Notification Center. Error: " + e);
		}


	}
	
	public void checkNewNotificationAppearsHiddenInTable(String notificationName) throws Exception {
		try {
			if (notificationNameTable.get(0).getText().equals(notificationName)) {
				if (notificationStatusTable.get(0).getText().equals("Hidden")) {
					testResultService.assertEquals(true, notificationStatusTable.get(0).getText().equals("Hidden"), "Check the new added notification appears as hidden in the table.");
				} else {
					testResultService.addFailTest("The notification: " +notificationName+ " was added with a wrong status.");
				}
			} else {
				testResultService.addFailTest("The notification: " +notificationName+ " is not displayed first in the list.");
			}
		} catch (Exception e) {
			testResultService.addFailTest("FAIL: Check New Notification is Displayed in the Notification Table as Hidden. Error : " + e);
		}
	}
	
	public void publishNotification(String notificationName) {
		try {
			if (notificationNameTable.get(0).getText().equals(notificationName)) {
				webDriver.ClickElement(notificationStatusTable.get(0));
				tmsHomePage.clickOnPromotionAreaMenuButton("Publish");
			} else {
				testResultService.addFailTest("The notification: " +notificationName+ " is not first in the notifications list");
			}
		} catch (Exception e) {
			testResultService.addFailTest("FAIL: Publish Notification. Error: " + e);
		}
	}
	
	public void deleteNotification(String notificationName) throws Exception {
		try {	
			boolean isNotificationFound = false;
			for (int i = 0; i < notificationNameTable.size(); i++) {
				if (notificationNameTable.get(i).getText().equals(notificationName)) {
					webDriver.ClickElement(notificationNameTable.get(i));
					tmsHomePage.clickOnPromotionAreaMenuButton("Hide"); 
					tmsHomePage.clickOnPromotionAreaMenuButton("Delete");
					webDriver.closeAlertByAccept();
					isNotificationFound = true;
					break;
				}
			}
			
			if (!isNotificationFound) {
				testResultService.addFailTest("The notification: " +notificationName+ " was not found in the notifications list");
			}
		} catch (Exception e) {
			testResultService.addFailTest("FAIL: Delete Notification. Error: " + e);
		}
	}
	
	public boolean verifyNotificationIsNotDisplayedInTheTable(String notificationName) throws Exception {
		try {
			boolean notificationDisplayed = false;
			for (int i = 0; i <notificationNameTable.size(); i++) {
				if (notificationNameTable.get(i).getText().equals(notificationName)) {
					notificationDisplayed = true;
					break;
				}
			}
	
			testResultService.assertEquals(false, notificationDisplayed, "Check the Notification: " +notificationName+ " is not displayed in the Notification List.");
			if (notificationDisplayed) {
				return false;
			} else {
				return true;
			}
		} catch (Exception e) {
			testResultService.addFailTest("FAIL: Check Notification: " +notificationName+ " is not displayed in the Notifiaction List. Error: " + e);
			return false;
		}
	}
}
