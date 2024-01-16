package pageObjects.tms;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Enums.ByTypes;
import drivers.GenericWebDriver;
import pageObjects.GenericPage;
import services.TestResultService;

public class RegistrationStudentsPage extends GenericPage {
	
	@FindBy(xpath = "//*[@id='tblBody']/tr/td[5]") 
	public List<WebElement> studentNames;
	
	@FindBy(xpath = "//input[@value='  Move  ']") 
	public WebElement moveButtonInPopup;

	@FindBy(xpath = "//*[@id='tblBody']")
	public WebElement studentelement;

	@FindBy(xpath = "//input[@value='  Export  ']")
	public WebElement exportButton;

	@FindBy(xpath = "//span[text()='Export']")
	public WebElement exportTool;


	TmsHomePage tmsHomePage = new TmsHomePage(webDriver, testResultService);

	public RegistrationStudentsPage(GenericWebDriver webDriver, TestResultService testResultService) throws Exception {
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

	public void goToRegistrationStudents() throws Exception {
		report.startStep("Click on Registration");
		tmsHomePage.switchToMainFrame();
		tmsHomePage.clickOnRegistration();
		Thread.sleep(1000);
		
		report.startStep("Click on Students");
		tmsHomePage.clickOnStudents();
		Thread.sleep(3000);
	}
	
	public void selectClass(String className) throws Exception {
		webDriver.switchToFrame("FormFrame");
		tmsHomePage.selectClass(configuration.getProperty(className), false, true);
		Thread.sleep(2000);
	}
	
	public void closeStudentInfoCard() throws Exception {
		webDriver.waitForElement("Submit2", ByTypes.name).click();
		Thread.sleep(2000);
		webDriver.switchToMainWindow();	
		tmsHomePage.switchToMainFrame();
	}
	
	public void verifyNewStudentName(String userName) throws Exception {
		webDriver.switchToFrame("FormFrame");
		tmsHomePage.switchToStudentDetailedPage();
		//String newActualUserName = studentNames.get(0).getText();
		verifyUsersExistsInRegistrationPage(studentNames,userName);
		//String newActualUserName = webDriver.waitForElement("//*[@id='tblBody']/tr/td[5]", ByTypes.xpath).getText();
		//testResultService.assertEquals(userName, newActualUserName);
		//return userName.equals(newActualUserName);
	}
	
	public void moveStudentToOtherClass(String userId, String targetClassId) throws Exception {
		//tmsHomePage.markClassForDelete(userId);
		tmsHomePage.markStudentForDelete(userId);
		webDriver.switchToTopMostFrame();
		tmsHomePage.switchToMainFrame();
		tmsHomePage.clickOnPromotionAreaMenuButton("Move");
		webDriver.closeAlertByAccept();
		Thread.sleep(1000);
		webDriver.switchToNewWindow();
		webDriver.switchToFrame("DataFrame");
		tmsHomePage.selectClassToMoveGroup(targetClassId);
		Thread.sleep(3000);
		webDriver.switchToTopMostFrame();
		//webDriver.waitForElement("//input[@value='  Move  ']", ByTypes.xpath).click();
		webDriver.ClickElement(moveButtonInPopup);
		Thread.sleep(3000);
	}
	
	public void deleteStudent(String userId) throws Exception {
		tmsHomePage.switchToStudentDetailedPage();
		//tmsHomePage.markClassForDelete(userId);
		tmsHomePage.markStudentForDelete(userId);
		
		tmsHomePage.clickOnDelete();
		webDriver.closeAlertByAccept();
		Thread.sleep(2000);
	}
	
	public void waitUntilStudentIsAdded(String userName) throws Exception {
		webDriver.switchToFrame("FormFrame");
		tmsHomePage.switchToStudentDetailedPage();
		String newActualUserName = "";
		boolean FLAG = false;
		int counter = 0;

		while (!FLAG && counter < 30) {
			newActualUserName = studentNames.get(0).getText();
			if (userName.equals(newActualUserName)) {
				FLAG = true;
			}
			counter++;
			Thread.sleep(1000);
		}
	}
	
	public String validateStudentWasAddedAndEditName(String studentName) throws Exception {
		String newName = "";
		tmsHomePage.switchToStudentDetailedPage();
		Thread.sleep(8000);
		
		tmsHomePage.getFirstInfoNode().click();
		webDriver.switchToPopup();
		Thread.sleep(2000);
		webDriver.switchToFrame("FormFrame");
		String actualUserName = webDriver.waitForElement("UserName", ByTypes.name).getAttribute("value");
		tmsHomePage.verifyInfoCardDetails("UserName",actualUserName);
		
		report.startStep("Edit UserName");
		webDriver.waitForElement("UserName", ByTypes.name).sendKeys("77");
		Thread.sleep(2000);
		newName = studentName + "77";

		report.startStep("Close Student info card");	
		webDriver.switchToMainWindow();
		webDriver.switchToPopup();
		closeStudentInfoCard();
	
		return newName;
	}


	public void verifyUsersExistsInRegistrationPage(List<WebElement> childElements, String userName) {
		boolean found = false;

		for (int j=0;j<childElements.size();j++){
			if (childElements.get(j).getText().contains(userName)) {
				found = true;
				break;
			}
		}
		if (!found)
			testResultService.addFailTest("users not found in Registration: " + userName);
	}
}
