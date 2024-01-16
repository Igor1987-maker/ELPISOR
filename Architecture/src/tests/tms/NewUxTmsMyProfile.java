package tests.tms;


import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import Enums.ByTypes;
import Enums.PLTStartLevel;
import Interfaces.TestCaseParams;
import testCategories.inProgressTests;
import Objects.Recording;
import pageObjects.EdoLoginPage;
import pageObjects.RecordPanel;
import pageObjects.edo.NewUXLoginPage;
import pageObjects.edo.NewUxAssessmentsPage;
import pageObjects.edo.NewUxClassicTestPage;
import pageObjects.edo.NewUxHomePage;
import pageObjects.edo.NewUxInstitutionPage;
import pageObjects.edo.NewUxLearningArea;
import pageObjects.edo.NewUxMyProfile;
//import pageObjects.edo.NewUxMyProfile;
import pageObjects.edo.NewUxCommunityPage;
import pageObjects.edo.NewUxSelfRegistrationPage;
import pageObjects.tms.DashboardPage;
import pageObjects.tms.TmsHomePage;
import tests.edo.newux.SpeechRecognitionBasicTestNewUX;

public class NewUxTmsMyProfile extends SpeechRecognitionBasicTestNewUX {

		
	NewUXLoginPage loginPage;
	DashboardPage dashboardPage;
	TmsHomePage tmsHomePage;
	NewUxMyProfile myProfile;
	
	
	@Before
	public void setup() throws Exception {
		super.setup();
		
		
	}
	
	@Test
	@TestCaseParams(testCaseID = {"33141"})
	public void testMyProfileInTMS() throws Exception {

		
	report.startStep("Login as Teacher");
		pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(configuration.getProperty("teacher.username"), autoInstitution.getInstitutionId()));
		loginPage = new NewUXLoginPage(webDriver, testResultService);
		sleep(2);
		tmsHomePage = loginPage.enterTeacherUserAndPassword();
		sleep(2);
		tmsHomePage.switchToMainFrame();
		
	report.startStep("Click On My Profile");
		myProfile = tmsHomePage.clickOnMyProfile();
		webDriver.switchToPopup();
		
	report.startStep("Verify The UserName");
		String exp_userName = configuration.getProperty("teacher.username");
		String act_UserName = myProfile.getUserName();		
		testResultService.assertEquals(exp_userName, act_UserName, "The expected UserName doesn't match to actual UserName");
		
	report.startStep("Change value and click on Update");
		String org_FirstName = myProfile.getFirstName();	
		String newFirstName = "New_firstName";
		myProfile.changeFirstName(newFirstName);
		myProfile.clickOnUpdateInTMS();
		sleep(1);
		
	report.startStep("Reopen My Profile and verify the change");
		webDriver.switchToMainWindow();
		tmsHomePage.switchToMainFrame();
		sleep(1);
		tmsHomePage.clickOnMyProfile();
		webDriver.switchToPopup();
		testResultService.assertEquals(newFirstName, "New_firstName", "The expected FirstName doesn't match to actual FirstName");
		
	report.startStep("Restore the original value and update");
		myProfile.changeFirstName(org_FirstName);
		myProfile.clickOnUpdateInTMS();
		webDriver.switchToMainWindow();
		tmsHomePage.switchToMainFrame();
		tmsHomePage.clickOnExit();
		
	}
	
				
	@After
	public void tearDown() throws Exception {
		
		super.tearDown();
	}
	
	
}
