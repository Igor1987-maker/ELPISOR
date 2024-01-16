package pagesObjects.oms;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import Enums.ByTypes;
import drivers.GenericWebDriver;
import pageObjects.GenericPage;
import services.TestResultService;

public class HomePage extends GenericPage {
	
	@FindBy(css = "div[class='pull-left loginName'] span[class='ng-binding']")
	public WebElement helloMessage;
	
	@FindBy(xpath = "//li[@class='currentRegister mega hasNodes']")
	public WebElement reports;////*[text()='Documentation and Training']
	
	@FindBy(linkText = "User Report")
	public WebElement userReport;
	
	@FindBy(xpath = "//input[@ng-click='showStudentProgress(studentId)']")
	public WebElement goButton;
	
	@FindBy(id = "groupName")
	public WebElement groupName;
	
	@FindBy(xpath = "//a[normalize-space()='Logout']")
	public WebElement logOut;
	
	public HomePage(GenericWebDriver webDriver, TestResultService testResultService) throws Exception {
		super(webDriver, testResultService);
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
	
	
	public String verifyUserLoggedIn() throws Exception {
		report.startStep("Checking that user logged in");
		 webDriver.waitForElementToBeVisisble(helloMessage);
				return helloMessage.getText();
			}
	
	
	public HomePage openUserReport() throws Exception {
	
	report.startStep("Open report page");
	Thread.sleep(8000);
		webDriver.waitForElementByWebElement(reports, "Element not found", true, 40000);
		int wait = 0;
		while(reports==null&&wait<3) {
			webDriver.waitForElementByWebElement(reports, "Element not found", true, 40000);
		}
		Actions action = new Actions(webDriver.getWebDriver());
		action.moveToElement(reports);
		reports.click();
		userReport.click();
		Thread.sleep(4000);
		return this;
		
	}
	
	
	public ProgressReportPage fillDataOfWantedUser(String nameOfGroup,String studentId ) throws Exception {
		report.startStep("Filling data of wanted user to retrieve his progress");
		if(nameOfGroup!=null) {
			Select select = new Select(groupName);
			select.selectByValue(nameOfGroup);
		}
		Thread.sleep(3000);
		WebElement userNameField = webDriver.waitForElement("studentId",  ByTypes.id, true, 4000);
		for(int i = 0;i<3&&userNameField==null;i++) {
			userNameField = webDriver.waitForElement("studentId",  ByTypes.id, true, 4000);
		}
		PageUtils.type(studentId, userNameField);
		goButton.click();
		Thread.sleep(8000);
		return new ProgressReportPage(webDriver, testResultService);
	}

	
	public LoginPage logOut() throws Exception {
		report.startStep("Click on log out");
		webDriver.waitForElementByWebElement(logOut, "No such element", true, 10000);;
		logOut.click();
				return new LoginPage(webDriver, testResultService);
		}
}
