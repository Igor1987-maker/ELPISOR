package tests.toeic.oms;

import org.junit.Test;

import Interfaces.TestCaseParams;
import pagesObjects.oms.HomePage;
import pagesObjects.oms.LoginPage;

public class LoginToOMS extends OMSbase{

	@Test
	public void login() throws Exception {
		
		//String[] student = dbService.getStudentOfTOEIC();
		String helloUser = new LoginPage(webDriver, testResultService)
							.enterCredentials("Name", "password")
							.verifyUserLoggedIn();
		
		assertNotNull(helloUser);
		assertEquals("Name", helloUser);
	}
	
	@Test
	@TestCaseParams(testCaseID = { "111111" }, testTimeOut = "10")
	public void verifyUserProgress() throws Exception {
		//String[] student = dbService.getStudentOfTOEIC();
		//dbService.insertProgressToStudent();
		int i = 0;
		new LoginPage(webDriver, testResultService)
			.enterCredentials("814167f", "d919f0a")
			.openUserReport()
			.fillDataOfWantedUser(null, "001000589337")
			.checkProgressDisplayedCorrectly("Personnel","10")
			.checkTimeSpendedOnTestMatch("4")
			.closeProgressReportWindow()
			.logOut()
			.verifyUserIsLoggedOut();
		
	/*	
		new LoginPage(webDriver, testResultService)
			.enterCredentials("1ffa61d", "6b9e43f")
			.openUserReport()
			.fillDataOfWantedUser(null, "001000589235")
			.checkUnitAndFinalTestProgress("40")
			.clickOnFinalTestReport()
			.checkFinalTestReportPageOpen()
			.checkReadingAndListeningScores("15", "60")
			.closeProgressReportWindow()
			.logOut()
			.verifyUserIsLoggedOut();
		*/
	}
	
	
}
