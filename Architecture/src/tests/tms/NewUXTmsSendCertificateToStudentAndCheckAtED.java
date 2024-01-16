package tests.tms;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import Interfaces.TestCaseParams;
import pageObjects.edo.NewUXLoginPage;
import pageObjects.edo.NewUxMessagesPage;
import pageObjects.edo.NewUxMyProgressPage;
import pageObjects.tms.TmsHomePage;
import pageObjects.tms.TmsMatrixPage;
import pagesObjects.oms.LoginPage;
import services.PageHelperService;
import tests.edo.newux.BasicNewUxTest;
import tests.edo.newux.SpeechRecognitionBasicTestNewUX;

public class NewUXTmsSendCertificateToStudentAndCheckAtED 
											extends SpeechRecognitionBasicTestNewUX{

	NewUXLoginPage loginPage;
	NewUxMyProgressPage myProgressPage;
	TmsHomePage tmsHomePage;
	NewUxMessagesPage messagesPage; 
	
	@Before
	public void setup() throws Exception {
		
	institutionName = BasicNewUxTest.institutionsName[0];
	super.setup();
		loginPage = new NewUXLoginPage(webDriver, testResultService);
	
		if (institutionName.equalsIgnoreCase("ED2016")) {
			loginPage.loginAsStudent("admin2016","12345");
			tmsHomePage = new TmsHomePage(webDriver, testResultService);
			tmsHomePage.waitForPageToLoad();
		}else {
		tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
		tmsHomePage.waitForPageToLoad();
		}
		
	}
	
	
	@Test
	@TestCaseParams(testCaseID = { "84903" })
	public void teacherSendCertificateToStudentAndVerifyInMyProfile() throws Exception {
		
		String studentName = "";
		String password = "";
		String className = "";
		String courseLevel = "Basic 1";
		
		for(int i = 0;i < 2;i++) {
			
			if(i==0) {
				String[]student = pageHelper.getStudentWithProgressFinalGrade("698");
				studentName = student[0];
				password = student[1];
				studentId = student[2];
				className = pageHelper.getClassNameOfParticularUser(studentId); 
				
				}
			if(i==1) {
				String[]student = pageHelper.getStudentWithProgressFinalGrade("640");
				studentName = student[0];
				password = student[1]; 
				studentId = student[2];
				className = pageHelper.getClassNameOfParticularUser(studentId);
				
				
				institutionName = BasicNewUxTest.institutionsName[10];
				pageHelper.openCILatestUXLink();
				loginPage.loginAsStudent("admin2016","12345");
				tmsHomePage = new TmsHomePage(webDriver, testResultService);
				tmsHomePage.waitForPageToLoad();
			}
			
		sleep(3);
		tmsHomePage.switchToMainFrame();
		
	report.startStep("Open Student Progress report");
		tmsHomePage.clickOnReports();
		sleep(2);
		tmsHomePage.clickOnCourseReports();
		sleep(2);
		tmsHomePage.switchToFormFrame();
		sleep(1);
		tmsHomePage.selectReport("9"); // 9 it's end of course Matrix
		sleep(5);
		tmsHomePage.selectClass(className, false, true);
		sleep(2);
		//tmsHomePage.clickOnGo();
		webDriver.switchToPopup();
		
		TmsMatrixPage matrixPage = new TmsMatrixPage(webDriver, testResultService);
		
	report.startStep("Send certificate to student");
		matrixPage.choseCourseLevel(courseLevel);
		matrixPage.findStudentAndSentHimCertificate(studentName);
		webDriver.getWebDriver().close();
		webDriver.switchToMainWindow();
		tmsHomePage.clickOnExitTMS();
	//	sleep(2);
	//	NewUXLoginPage loginPage = new NewUXLoginPage(webDriver,
		//		testResultService);
		loginPage.waitForPageToLoad();
	
	report.startStep("Login as student");
		//webDriver.openUrl("https://EDUI-CI-main-20220803-1.edusoftrd.com/ED2016");
	//	sleep(2);
	//	loginPage.waitForPageToLoad();
		pageHelper.setUserLoginToNull(studentId);
		sleep(2);
		homePage = loginPage.loginAsStudent(studentName,password);
		pageHelper.skipOptin();
		homePage.skipNotificationWindow();
		pageHelper.skipOnBoardingHP();
		homePage.closeModalPopUp();
		sleep(2);
		
	report.startStep("Open Inbox page");
		messagesPage = homePage.openInboxPage(false);
		messagesPage.switchToInboxFrame();
	
	report.startStep("Check certificate are inbox, and delete all messages");	
		messagesPage = messagesPage.checkTitleDateAndCertificateOfMessage();
		messagesPage.deleteAllMessages();
		
	report.startStep("Log out of ED");			
		webDriver.getWebDriver().close();
		webDriver.switchToMainWindow();
		homePage.logOutOfED();
		}
	}
}
