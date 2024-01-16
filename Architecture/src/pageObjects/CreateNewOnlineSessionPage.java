package pageObjects;

import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import Enums.ByTypes;
import drivers.GenericWebDriver;
import pageObjects.tms.TmsHomePage;
import services.TestResultService;

public class CreateNewOnlineSessionPage extends GenericPage {
	
	@FindBy(id ="onlineSession__title")
	public WebElement onlineSessionTitle_txt;
	
	@FindBy(xpath = "/html/body/div[3]/div[2]/div/div/p")
	public WebElement chooseClass_dropdownlist; 
	//html/body/div[3]/div[4]/div/div/p
	
	@FindBy(xpath = "/html/body/div[3]/div[4]/div/div/p")
	public WebElement selectStudent; 
	
	@FindBy(xpath = "/html/body/div[3]/div[2]/div/div/div/div/p[1]")
	public WebElement okClass_btn;
	
	@FindBy(xpath = "/html/body/div[3]/div[4]/div/div/div/div/p[1]")
	public WebElement okClass_btn2;
	
		
	
	@FindBy(id ="onlineSessionItem__dateStartTbx")
	public WebElement onlineSessionStartDate_txt;
	
	@FindBy(id ="onlineSessionItem__timeStartTbx")
	public WebElement onlineSessionStartTime_txt;
	
	@FindBy(id ="onlineSessionItem__dateEndTbx")
	public WebElement onlineSessionEndDate_txt;
	
	@FindBy(id ="onlineSessionItem__timeEndTbx")
	public WebElement onlineSessionEndTime_txt;
	
	@FindBy(id ="onlineSession__customLink")
	public WebElement link_txt;

	@FindBy(id = "save")
	public WebElement save_btn;
	
	@FindBy(linkText = "Cancel")
	public WebElement cancel_btn;
	
	TmsHomePage tmsHomePage = new TmsHomePage(webDriver, testResultService);

	public CreateNewOnlineSessionPage(GenericWebDriver webDriver, TestResultService testResultService)
			throws Exception {
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

	public void enterOnlineSessionTitle(String title) {
		webDriver.SendKeys(onlineSessionTitle_txt, title);
	}
	
	public void selectOption(String names,String whatToSelect) throws Exception {
	//	Select selectClass = new Select(webDriver.getWebDriver().findElement(By.id("selectClass")));
	//	selectClass.selectByVisibleText(classesNames);;
	//	webDriver.ClickElement(okClass_btn);	
		
		
		try {
			if(whatToSelect.equalsIgnoreCase("Class")) {
			webDriver.ClickElement(chooseClass_dropdownlist);
			Thread.sleep(2000);
			}
			else {
				webDriver.ClickElement(selectStudent);
				Thread.sleep(2000);
			}
			ArrayList<WebElement> dropdownOptions = (ArrayList<WebElement>) webDriver.getWebDriver().findElements(By.className("opt"));
	
			for (int j = 0; j < dropdownOptions.size(); j++) {
				if (dropdownOptions.get(j).getText().contains(names)) {
					dropdownOptions.get(j).click();
					Thread.sleep(1000);
					break;
				}
			}
			
		/*		
			for (int i = 0; i < classesNames.size(); i++) {
				for (int j = 0; j < dropdownOptions.size(); j++) {
					if (dropdownOptions.get(j).getText().equals(classesNames.get(i))) {
						dropdownOptions.get(j).click();
						break;
					}
				}
			}*/
			if(whatToSelect.equalsIgnoreCase("Class")) {
					webDriver.ClickElement(okClass_btn);
					Thread.sleep(1000);
				}
				else {
					webDriver.ClickElement(okClass_btn2);
					Thread.sleep(1000);
				}
		
			Thread.sleep(2);
		} catch (Exception e) {
			testResultService.addFailTest("FAIL: Choose Classes From DropDown List. Error: " + e);
		}
	}
	
	public void enterDate(String startDateTime, String EndDateTime) throws Exception {
		String startDate = startDateTime.split(" ")[0];
		String StartTime = startDateTime.split(" ")[1];
		String endDate = EndDateTime.split(" ")[0];
		String endTime = EndDateTime.split(" ")[1];
		
		webDriver.SendKeys(onlineSessionStartDate_txt, startDate);
		webDriver.sendKey(Keys.ENTER);
		webDriver.SendKeys(onlineSessionStartTime_txt, StartTime);
		webDriver.sendKey(Keys.ENTER);
		webDriver.SendKeys(onlineSessionEndDate_txt, endDate);
		webDriver.sendKey(Keys.ENTER);
		webDriver.SendKeys(onlineSessionEndTime_txt, endTime);
		webDriver.sendKey(Keys.ENTER);
	}
	
	public void enterLink(String link) {
		webDriver.SendKeys(link_txt, link);
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
	
	public void createNewOnlineSession(String title, String className, 
			String startDateTime, String EndDateTime, String link, String userName) throws Exception {
		
		try {
			
			report.startStep("Open New Online Session Popup");
			tmsHomePage.clickOnPromotionAreaMenuButton("New");
			webDriver.switchToPopup();
			
			report.startStep("Enter Online Session Name");
			enterOnlineSessionTitle(title);
			
			report.startStep("Choose Classes");
			selectOption(className,"Class");
			
			report.startStep("Choose Student");
			selectOption(userName,"Student");
			
			report.startStep("Enter Date");
			enterDate(startDateTime, EndDateTime);
		
			report.startStep("Enter Link");
			enterLink(link);
			
			report.startStep("Click On Save");
			clickOnSave();
						
			report.startStep("Click On Cancel");
			clickOnCancel();
			
		} catch (Exception e) {
			testResultService.addFailTest("FAIL: Create New Online Session. Error: " + e);
		} finally {
			webDriver.switchToMainWindow();
			tmsHomePage.switchToMainFrame();
		}
	}
}
