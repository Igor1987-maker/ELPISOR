package pageObjects;

import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import drivers.GenericWebDriver;
import pageObjects.tms.TmsHomePage;
import services.TestResultService;

public class CreateNewNotificationPage extends GenericPage {

	TmsHomePage tmsHomePage = new TmsHomePage(webDriver, testResultService);

	@FindBy(id ="slideName")
	public WebElement notificationName_txt;
	
	@FindBy(css = ".tmsSlideEditor__slideAudianceSelectW:nth-of-type(1) p.CaptionCont.SelectBox.search")
	public WebElement chooseClass_dropdownlist; // xpath: /html/body/div/div[1]/div[2]/div[1]/div/p
	
	@FindBy(css = "body > div > div.tmsSlideEditor__header > div.tmsSlideEditor__slideAudiancesW > div:nth-child(1) > div > div > div > p.btnOk")
	public WebElement okClass_btn;
	
	@FindBy(css = ".tmsSlideEditor__slideAudianceSelectW:nth-of-type(2) p.CaptionCont.SelectBox.search")
	public WebElement chooseRole_dropdownlist; // xpath: /html/body/div/div[1]/div[2]/div[2]/div/p
	
	@FindBy(css = "body > div > div.tmsSlideEditor__header > div.tmsSlideEditor__slideAudiancesW > div:nth-child(2) > div > div > div > p.btnOk")
	public WebElement okRole_btn;
	
	@FindBy(id = "home__marketingMagMonth")
	public WebElement date_txt;
	
	@FindBy(id = "home__marketingMagTypeTitle")
	public WebElement title_txt;
	
	@FindBy(id ="home__marketingMagText")
	public WebElement message_txt;
	
	@FindBy(linkText = "Save")
	public WebElement save_btn;
	
	@FindBy(linkText = "Cancel")
	public WebElement cancel_btn;
	
	public CreateNewNotificationPage(GenericWebDriver webDriver, TestResultService testResultService)
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
	
	public void enterNotificationName(String notificationName) {
		webDriver.SendKeys(notificationName_txt, notificationName);
	}
	
	public void selectClass(ArrayList<String> classesNames) throws Exception {
		try {
			webDriver.ClickElement(chooseClass_dropdownlist);
			ArrayList<WebElement> dropdownOptions = (ArrayList<WebElement>) webDriver.getWebDriver().findElements(By.className("opt"));
	
			for (int i = 0; i < classesNames.size(); i++) {
				for (int j = 0; j < dropdownOptions.size(); j++) {
					if (dropdownOptions.get(j).getText().equals(classesNames.get(i))) {
						dropdownOptions.get(j).click();
						break;
					}
				}
			}
			webDriver.ClickElement(okClass_btn);	
		} catch (Exception e) {
			testResultService.addFailTest("FAIL: Choose Classes From DropDown List. Error: " + e);
		}
	}
	
	public void selectRoles(ArrayList<String> roles) throws Exception {
		try {
			webDriver.ClickElement(chooseRole_dropdownlist);
			ArrayList<WebElement> dropdownOptions = (ArrayList<WebElement>) webDriver.getWebDriver().findElements(By.className("opt"));
			
			for (int i = 0; i < roles.size(); i++) {
				for (int j = 0; j < dropdownOptions.size(); j++) {
					if (dropdownOptions.get(j).getText().equals(roles.get(i))) {
						dropdownOptions.get(j).click();
						break;
					}
				}
			}
			webDriver.ClickElement(okRole_btn);	
		} catch (Exception e) {
			testResultService.addFailTest("FAIL: Choose Roles From DropDown List. Error: " + e);
		}
	}
	
	public void enterDate(String date) {
		webDriver.SendKeys(date_txt, date);
	}
	
	public void enterTitle(String title) {
		webDriver.SendKeys(title_txt, title);
	}
	
	public void enterMessage(String message) throws Exception {
		webDriver.SendKeys(message_txt, message);
	}
	
	public void clickOnSave() throws Exception {
		try {
			webDriver.ClickElement(save_btn);
			
			report.startStep("Close the Alert");
			webDriver.closeAlertByAccept();
		} catch (Exception e) {
			testResultService.addFailTest("FAIL: Click on Save. Error: " + e);
		}
	}
	
	public void clickOnCancel() throws Exception {
		try {
		webDriver.ClickElement(cancel_btn);
		
		report.startStep("Close the Alert");
		webDriver.closeAlertByAccept();
		} catch (Exception e) {
			testResultService.addFailTest("FAil: Click on Cancel. Error: " + e);
		}
	}
	
	public void createNewNotification(String notificationName, String sendingUser, 
			ArrayList<String> classesNames, ArrayList<String> roles, String date, String title, String message) throws Exception {
		
		try {
			
			report.startStep("Open New Message Popup");
			tmsHomePage.clickOnPromotionAreaMenuButton("New");
			webDriver.switchToPopup();
			
			report.startStep("Enter Notification Name");
			enterNotificationName(notificationName);
			
			// Choose audience
			if (sendingUser.toLowerCase() == "admin") {
				report.startStep("Choose Classes");
				selectClass(classesNames);
				
				report.startStep("Choose Roles");
				selectRoles(roles);	
			} else if (sendingUser.toLowerCase() == "teacher") {
				report.startStep("Choose Classes");
				selectClass(classesNames);
			}
			
			report.startStep("Enter Date");
			enterDate(date);
			
			report.startStep("Enter Title");
			enterTitle(title);
		
			report.startStep("Enter Message");
			enterMessage(message);
			
			report.startStep("Click On Save");
			clickOnSave();
						
			report.startStep("Click On Cancel");
			clickOnCancel();
			
		} catch (Exception e) {
			testResultService.addFailTest("FAIL: Create New Notification. Error: " + e);
		} finally {
			webDriver.switchToMainWindow();
			tmsHomePage.switchToMainFrame();
		}
	}
}
