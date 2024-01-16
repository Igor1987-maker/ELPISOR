package pageObjects.tms;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import drivers.GenericWebDriver;
import pageObjects.GenericPage;
import services.TestResultService;

public class RegistrationTeachersPage extends GenericPage {
	
	@FindBy(xpath = "//tbody[@id='con']/tr/td[3]")
	public List<WebElement> teacherNames;
	
	@FindBy(xpath = "//tbody[@id='con']/tr/td[2]")
	public List<WebElement> teacherCheckBoxes;


	TmsHomePage tmsHomePage = new TmsHomePage(webDriver, testResultService);
	TeacherDetailsPage teacherDetailedPage = new TeacherDetailsPage(webDriver,testResultService);

	public RegistrationTeachersPage(GenericWebDriver webDriver, TestResultService testResultService) throws Exception {
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

	public void goToRegistrationTeachers() throws Exception {
		report.startStep("Click on Registration");
		tmsHomePage.switchToMainFrame();
		tmsHomePage.clickOnRegistration();
		Thread.sleep(1000);
		
		report.startStep("Click on Teachers");
		tmsHomePage.clickOnTeachers();
		Thread.sleep(3000);
	}
	
	public void addNewTeacher(String teacherName) throws Exception {
		report.startStep("Click on Add New Teacher");
		tmsHomePage.clickOnAddNewTeacher();
		webDriver.switchToPopup();
		Thread.sleep(2000);
		tmsHomePage.switchToFormFrame();
	
	report.startStep("Enter First,Last,UserName and Password");
		teacherDetailedPage.typeTeacherFirstName(teacherName);
		teacherDetailedPage.typeTeacherLastName(teacherName);
		teacherDetailedPage.typeTeacherUserName(teacherName);
		teacherDetailedPage.typeTeacherPassword("12345");
		teacherDetailedPage.typeTeacherEmail(teacherName + "@edus.com");
		
	report.startStep("Assign the first class to teacher");
		webDriver.switchToMainWindow();
		webDriver.switchToPopup();
		tmsHomePage.switchToFormFrame();
		teacherDetailedPage.assignClassToTeacher();
		
		
	report.startStep("Click on Submit");
		clicOnSubmitTeacherForm();
		
		returnToMainPage();
		Thread.sleep(2000);
	
	}
	
	public void openTeacherFormAndVerifyDetails(String userName) throws Exception {
		report.startStep("Open Teacher created info card");
		WebElement element = tmsHomePage.getFirstInfoNode();
		element.click();
		webDriver.switchToPopup();
		Thread.sleep(1000);
		tmsHomePage.switchToFormFrame();
	
		report.startStep("Verify Techer user name and Class assigned");
		tmsHomePage.verifyInfoCardDetails("UserName",userName);
		webDriver.switchToMainWindow();
		webDriver.switchToPopup();
		tmsHomePage.switchToFormFrame();	
		tmsHomePage.verifyAssignedElementToTeacher("Class");
		clicOnSubmitTeacherForm();
		returnToMainPage();
	}
	
	public void clicOnSubmitTeacherForm() throws Exception{
		webDriver.switchToMainWindow();
		webDriver.switchToPopup();
		teacherDetailedPage.clickOnSubmit();
	}
	
	public void returnToMainPage() throws Exception {
		webDriver.switchToMainWindow();
		tmsHomePage.switchToMainFrame();
	}
	
	public void deleteTeacher(String userId) throws Exception {
		tmsHomePage.markTeacher(userId);
		tmsHomePage.clickOnDelete();
		webDriver.closeAlertByAccept();
		Thread.sleep(6000);
	}
	
	public void deleteTeacher2(String userName) throws Exception {
		int index = getTeacherIndexByName(userName);
		webDriver.ClickElement(teacherCheckBoxes.get(index));
		tmsHomePage.clickOnDelete();
		webDriver.closeAlertByAccept();
		Thread.sleep(6000);
	}
	
	public int getTeacherIndexByName(String teacherName) {
		int wantedIndex = 0;
		for (int i = 0; i < teacherNames.size(); i++) {
			if (teacherNames.get(i).getText().equals(teacherName)) {
				wantedIndex = i;
				break;
			}
		}
		return wantedIndex;
	}
	
	public boolean verifyTeacherDoesNotExist(String teacherName) throws Exception {
		boolean isDisplayed = false;
		for (int i = 0; i < teacherNames.size(); i++) {
			if (teacherNames.get(i).getText().equals(teacherName)) {
				isDisplayed = true;
				break;
			}
		}
		testResultService.assertEquals(false, isDisplayed, "Teacher '"+teacherName+"' is displayed");
		return !isDisplayed;
	}
}
