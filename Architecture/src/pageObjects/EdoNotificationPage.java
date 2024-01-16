package pageObjects;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Enums.ByTypes;
import drivers.GenericWebDriver;
import pageObjects.edo.NewUxHomePage;
import services.TestResultService;

public class EdoNotificationPage extends GenericPage {
	
	@FindBy(id = "home__marketingSlideOW_0")
	public WebElement firstNotification;
	
	@FindBy(xpath = "//div[contains(@id,'home__marketingSlideOW_')]")
	public List<WebElement> notificationTab;
	
	@FindBy(className = "home__marketingMagMonth")
	public List<WebElement> notificationDate;
	
	@FindBy(className = "home__marketingMagTitle")
	public List<WebElement> notificationTitle;
	
	@FindBy(className = "home__marketingMagText")
	public List<WebElement> notificationMessage;
	
	@FindBy(className = "home__marketingMagBtn")
	public List<WebElement> notificationButton;
	
	@FindBy(id = "notificationsCenter_dontShowAgain")
	public WebElement dontShowAgainButton;
	
	@FindBy(className = "notificationsCenter_hideSlide")
	public WebElement closeNotificationButton;
	
	@FindBy(xpath = "//*[contains(@id,\"home__marketingSlideOW\")]")
	public List<WebElement> notifications;
	
	@FindBy(className = "carousel-control-next-icon")
	public WebElement nextNotificationButton;
	
	@FindBy(className = "notificationsCenter_counter")
	public WebElement numOfNotifications;
	
	NewUxHomePage homePage = new NewUxHomePage(webDriver, testResultService);
	
	public EdoNotificationPage(GenericWebDriver webDriver, TestResultService testResultService) throws Exception {
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
	
	public void checkNotificationIsDisplayed(int notificationIndex, String date, String title, String message, String button, boolean dontShowAgain, boolean closeNotification) {
		try {
			report.startStep("Check Notification is Displayed");
			WebElement notifications = webDriver.waitForElement("//*[contains(@id,'home__marketingSlide')]", ByTypes.xpath, false, webDriver.getTimeout());
			
			boolean isNotificationDisplayed = webDriver.isDisplayed(notificationTab.get(notificationIndex));
			if (isNotificationDisplayed) {
				
				report.startStep("Check Notification Date is Correct");
				testResultService.assertEquals(true, notificationDate.get(notificationIndex).getText().equals(date), "Check Notification Date is Correct.");

				report.startStep("Check Notification Title is Correct");
				testResultService.assertEquals(true, notificationTitle.get(notificationIndex).getText().equals(title), "Check Notification Title is Correct.");
				
				report.startStep("Check Notification Message is Correct");
				testResultService.assertEquals(true, notificationMessage.get(notificationIndex).getText().equals(message), "Check Notification Message is Correct.");
			
				// Check notification button
				if (button == null) {
					// no button was added- not checking
					System.out.println("not checking button");
				} else {
					report.startStep("Check Notification Button is Correct");
					testResultService.assertEquals(true, notificationButton.get(notificationIndex).getText().equals(button), "Check Notification Button is Correct.");
				}
				
				// Press don't show again
				if (dontShowAgain) {
					report.startStep("Press 'Don't Show Again' Button");
					dontShowAgainButton.click();
				}
				
				if (closeNotification) {
					report.startStep("Close the Notification");
					homePage.closeAllNotifications();	
				}
				
			} else {
				testResultService.addFailTest("Notification: " +notificationTitle+ " is not displayed.");
			} 
		} catch (Exception e) {
			testResultService.addFailTest("FAIL: Check notification: " +notificationTitle+ " is displayed correctly. Error: " + e);	
		}
	}
	
	public void checkNotificationIsNotDisplayed(String title) throws Exception {
		try {
			
			boolean isAnyNotificationDisplayed = webDriver.isDisplayed(firstNotification);
			if (isAnyNotificationDisplayed) {
				// return the number of notifications
				String notificationsNumber = numOfNotifications.getText().split(" / ")[1];
				int number = Integer.parseInt(notificationsNumber);
				
				// run through all notifications
				boolean isNotificationDisplayed = false;
				for (int i = 0; i < number; i++) {
					if (notificationTitle.get(i).getText().equals(title)) {
						testResultService.addFailTest("The notification: " + title + " is displayed after deletion");
						isNotificationDisplayed = true;
						break;
					} else {
						testResultService.assertEquals(false, notificationTitle.get(i).getText().equals(title), "Check notification is not displayed");	
					}
					nextNotificationButton.click();
				}
				
				testResultService.assertEquals(false, isNotificationDisplayed, "Notification is displayed after deletion");
			} else {
				testResultService.assertEquals(false, isAnyNotificationDisplayed);
			}	
		} catch (Exception e) {
			testResultService.addFailTest("FAIL: Check Notification: " +title+ " is Not Displayed. Error: " + e);
		} finally {
			// close all notifications
			homePage.closeAllNotifications();
		}
	}
	
	public void moveToNextNotification() {
		webDriver.ClickElement(nextNotificationButton);
	}
}
