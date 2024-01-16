package tests.edo.newux;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.WebElement;

import Enums.SubComponentName;
import Interfaces.TestCaseParams;
import Objects.Course;
import pageObjects.DragAndDropSection;
import pageObjects.EdoHomePage;
import pageObjects.edo.NewUXLoginPage;
import pageObjects.edo.NewUxDragAndDropSection;
import pageObjects.edo.NewUxLearningArea;
import pageObjects.edo.NewUxMyProfile;
import testCategories.AngularLearningArea;
import testCategories.inProgressTests;
import testCategories.reg2;
import testCategories.reg3;
import testCategories.unstableTests;

//Category(reg2.class)
@Category(AngularLearningArea.class)
public class ForgotMyPasswordTests extends BasicNewUxTest {

	NewUXLoginPage loginPage; 
//	NewUxLearningArea learningArea;
	NewUxMyProfile myProfile;
	
	private static final String INSTRUCTION_1 = "No need to worry! Please enter your user name and click \'Submit.\'";
	private static final String INSTRUCTION_2 = "An e-mail with your password will be sent shortly. You may need to check your spam or junk mail folder.";

	@Before
	public void setup() throws Exception {
		super.setup();
		pageHelper.updateInstitutionMyProfileSettings(institutionId, "2");
		report.startStep("Get user and Login");
		getUserAndLoginNewUXClass();

//		learningArea = new NewUxLearningArea(webDriver, testResultService);
		loginPage = new NewUXLoginPage(webDriver, testResultService);
	}
	
	@Test
	@TestCaseParams(testCaseID = { "33775" })
	public void forgotMyPasswordValidFlow() throws Exception {
		
		report.startStep("Get user data for test");
		String userName = dbService.getUserNameById(studentId, institutionId);
		String newPass = "123456";
		
		report.startStep("Open My Profile page");
		myProfile = homePage.clickOnMyProfile();
		homePage.switchToMyProfile();

		report.startStep("Insert email");
		myProfile.changeEmail("igors@edusoftlearning.com");

		report.startStep("Click on change password link");
		myProfile.clickOnChangePassword();
		sleep(1);
				
		report.startStep("Change password and close My Profile");
		myProfile.enterNewtNewPassword(newPass);
		myProfile.enterConfirmPassword(newPass);
		myProfile.enterOldPassword("12345");
		myProfile.clickOnSubmit();
		sleep(2);
		webDriver.switchToMainWindow();
		
		homePage.switchToMyProfile();
		myProfile.clickOnUpdate();
		sleep(2);
		myProfile.close();
		
		report.startStep("Logout");
		webDriver.switchToMainWindow();
		sleep(1);
		homePage.clickOnLogOut();
		
		report.startStep("Click on Forgot Password link and check instruction");
		webDriver.switchToMainWindow();
		loginPage.clickOnForgotPassword();
		loginPage.checkForgotPasswordInstruction(INSTRUCTION_1);
		
		report.startStep("Enter username and submit");
		loginPage.enterUserNameInForgotPasswordForm(userName);
		loginPage.clickOnSubmitInForgotPasswordForm();
		sleep(3);
				
		report.startStep("Check instruction and press OK");
		loginPage.checkForgotPasswordInstruction(INSTRUCTION_2);
		loginPage.clickOnSubmitInForgotPasswordForm();
				
		report.startStep("Login");
		loginPage.enterUserName(userName);
		loginPage.enterPassowrd(newPass);
		loginPage.clickOnSubmit();
		testResultService.assertEquals("Hello " + userName, homePage.getUserDataText(), "Verify First Name in Header of Home Page");
				
	}
	
	@After
	public void tearDown() throws Exception {
		report.startStep("Reset the Password");
		dbService.changeUserPassword(studentId);
		super.tearDown();
	}	
	
}
	
