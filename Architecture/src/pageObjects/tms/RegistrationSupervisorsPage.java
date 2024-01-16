package pageObjects.tms;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import drivers.GenericWebDriver;
import pageObjects.GenericPage;
import services.TestResultService;

public class RegistrationSupervisorsPage extends GenericPage {
	
	@FindBy(xpath = "//*[@name='FirstName']")
	public WebElement firstNameField;
	
	@FindBy(xpath = "//*[@name='LastName']")
	public WebElement lastNameField;
	
	@FindBy(xpath = "//*[@name='UserName']")
	public WebElement userNameField;
	
	@FindBy(xpath = "//*[@name='Password']")
	public WebElement passwordField;
	
	@FindBy(xpath = "//*[@name='listLeft']/option[1]")
	public WebElement firstTeacherInAssignList;
	
	@FindBy(xpath = "//*[@value='    Add >   ']")
	public WebElement addTeacherButton;

	TmsHomePage tmsHomePage = new TmsHomePage(webDriver, testResultService);
	RegistrationTeachersPage registrationTeachersPage = new RegistrationTeachersPage(webDriver,testResultService);

	public RegistrationSupervisorsPage(GenericWebDriver webDriver, TestResultService testResultService)
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

	public void goToRegistrationSupervisors() throws Exception {
		report.startStep("Click on Registration");
		tmsHomePage.switchToMainFrame();
		tmsHomePage.clickOnRegistration();
		Thread.sleep(1000);
		
		report.startStep("Click on Supervisors");
		tmsHomePage.clickOnSupervisor();
		Thread.sleep(2000);
	}
	
	public void addNewSupervisor(String supervisorName) throws Exception {
		report.startStep("Click on Add New Supervisor");
		tmsHomePage.clickOnAddNewSupervisor();
		webDriver.switchToPopup();
		Thread.sleep(2000);
		tmsHomePage.switchToFormFrame();
	
		report.startStep("Enter First,Last,UserName and Password");
		setFirstName(supervisorName);
		setLastName(supervisorName);
		setUserName(supervisorName);
		setPassword("12345");
		
		report.startStep("Assign the first teacher to supervisor");
		webDriver.switchToMainWindow();
		webDriver.switchToPopup();
		tmsHomePage.switchToFormFrame();
		assignTeacherToSupervisor();
					
		report.startStep("Click on Submit");
		registrationTeachersPage.clicOnSubmitTeacherForm();
	}
	
	public void setFirstName(String firstName) {
		webDriver.SendKeys(firstNameField, firstName);
	}
	
	public void setLastName(String lastName) {
		webDriver.SendKeys(lastNameField, lastName);
	}
	
	public void setUserName(String userName) {
		webDriver.SendKeys(userNameField, userName);
	}
	
	public void setPassword(String password) {
		webDriver.SendKeys(passwordField, password);
	}
	
	public void assignTeacherToSupervisor() throws Exception {
		webDriver.ClickElement(firstTeacherInAssignList);
		webDriver.ClickElement(addTeacherButton);
	}
	
	public void verifySupervisorDetails(String supervisorId, String supervisorName) throws Exception {
		report.startStep("Open Supervisor created info card");	
		tmsHomePage.clickOnInfoCard("tr",supervisorId);
		webDriver.switchToPopup();
		Thread.sleep(1000);
		tmsHomePage.switchToFormFrame();
	
		report.startStep("Verify Supervisor User name");
		tmsHomePage.verifyInfoCardDetails("UserName", supervisorName);
	
		report.startStep("Verify Teacher assigned");
		webDriver.switchToMainWindow();
		webDriver.switchToPopup();
		tmsHomePage.switchToFormFrame();	
		tmsHomePage.verifyAssignedElementToTeacher("Teacher");
		registrationTeachersPage.clicOnSubmitTeacherForm();
	}
	
	public void deleteSupervisor(String userId) throws Exception {
		tmsHomePage.markSupervisor(userId);
		tmsHomePage.clickOnDelete();
		webDriver.closeAlertByAccept();
		Thread.sleep(2000);
	}
}
