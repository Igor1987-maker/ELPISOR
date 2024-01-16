package tests.tms;

import org.junit.Before;
import org.junit.Test;

import Interfaces.TestCaseParams;
import pageObjects.edo.NewUXLoginPage;
import pageObjects.edo.NewUxHomePage;
import services.PageHelperService;
import services.StudentService;
import tests.edo.newux.SpeechRecognitionBasicTestNewUX;

public class ResetRestoreActivityTests extends SpeechRecognitionBasicTestNewUX{

//	public StudentService studentService;
	//PageHelperService pageHelper;
	NewUXLoginPage loginPage;
	NewUxHomePage homePage;
	
	@Before
	public void setup() throws Exception {
		super.setup();
	//	pageHelper = new PageHelperService();
		loginPage =  new NewUXLoginPage(webDriver, testResultService);
	}
	
	@Test
	@TestCaseParams(testCaseID = { "" })
	public void resetStudentActivity() throws Exception {
		//createUserAndLoginNewUXClass(className, institutionId);
		String className = configuration.getProperty("classname");
		studentId = pageHelper.createUSerUsingSP(institutionId,className);
		homePage = loginPage.loginAsStudent(
				dbService.getUserNameById(studentId, institutionId), "12345");
		studentService.setProgressForCourse(courses[1], studentId, null, 60, true, true);
		//loginPage.clickOnLogout();
		homePage.logOutOfED();
		loginPage.waitForPageToLoad();
		pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(configuration.getProperty("schoolAdmin.user"),configuration.getProperty("institution.id")));
		sleep(1);
		//pageHelper.loginAsSchoolAdmin();
		loginPage.enterSchoolAdminUserAndPassword();
		
		
		
		
	}
	
	
	
}
