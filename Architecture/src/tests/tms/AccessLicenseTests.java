package tests.tms;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pageObjects.EdoLoginPage;
import pageObjects.tms.TmsHomePage;
import Interfaces.TestCaseParams;
import tests.edo.newux.BasicNewUxTest;

public class AccessLicenseTests extends BasicNewUxTest {

	String testSchool = "auto2";
	String testClass = "class1";

	TmsHomePage tmsPage = null;

	@Before
	public void setup() throws Exception {
		super.setup();
	}

	@Test
	@TestCaseParams(testCaseID = { "16830" })
	public void deactivateStudent() throws Exception {
		String studentUserName = "Stud" + dbService.sig(6);
		report.report("Student user name is: " + studentUserName);
		System.out.println(studentUserName);
		String schoolClass = "class005";
		pageHelper.addStudent(studentUserName, schoolClass);

		TmsHomePage tmsHomePage = pageHelper.loginToTmsAsAdmin();

		report.report("Open licences");
		tmsHomePage.clickOnLicenses();
		sleep(2);
		report.report("Select institution, class and student");
		tmsHomePage.selectInstitute(autoInstitution.getInstitutionName(),
				autoInstitution.getInstitutionId(), false, true);

		tmsHomePage.selectClass(schoolClass, true, false);
		sleep(2);
		tmsHomePage.selecStudent(studentUserName);
		tmsHomePage.clickOnGo();
		tmsHomePage.switchToMainFrame();
		report.report("Deactivate the student");
		tmsHomePage.deactivateStudent();
		webDriver.switchToMainWindow();
		tmsHomePage.switchToMainFrame();

		tmsHomePage.clickOnSave();

		report.report("Try to login with the deactivated student");
		webDriver.deleteCookiesAndRefresh();
		EdoLoginPage edoLoginPage = new EdoLoginPage(webDriver,
				testResultService);
		edoLoginPage.OpenPage(getSutAndSubDomain());
		edoLoginPage.typeUserNameAndPass(studentUserName, "12345");
		edoLoginPage.submitLogin();
		String popupText = edoLoginPage.getPopupText();
		testResultService
				.assertEquals(
						"Your access license is no longer active. Please contact your administrator for activation.",
						popupText, "popup text not found");

	}

	@Test
	@TestCaseParams(testCaseID = { "16744" })
	public void testCreateUserUsingUiWhenThereAreNoActiveLicenses()
			throws Exception {

		limitActiveUsers();

		startStep("Try to add new user");
		webDriver.switchToMainWindow();
		tmsPage.switchToMainFrame();
		tmsPage.clickOnStudents();

		tmsPage.selectInstitute(testSchool, null, true);
		Thread.sleep(1000);
		tmsPage.selectClass(testClass);

		tmsPage.enterStudentDetails("Student" + dbService.sig(4));

		String popupText = tmsPage.getPopupText();

		testResultService
				.assertEquals(
						"There are not enough licenses to create more users. Please deactivate some users or contact your Administrator.",
						popupText, "Error message was not displayed");

	}

	@Test
	@TestCaseParams(testCaseID = { "17024" })
	public void testCreateUserUsingAPIWhenThereAreNoActiveLicenses()
			throws Exception {

		String instId = dbService.getInstituteIdByName(testSchool);
		String studentUserName = "students" + dbService.sig(6);
		limitActiveUsers();

		startStep("Try to add student using API");
		pageHelper.createUserUsingApi(configuration.getProperty("sut.url"),
				studentUserName, "fname", "lname", "12345", instId, testClass);

		startStep("Check that user was not created");
		String dbResult = dbService.getUserIdByUserName(studentUserName, instId);
		testResultService.assertEquals(true, dbResult==null,"User was created in DB, when it should not");
	}

	@After
	public void tearDowb() throws Exception {
		super.tearDown();
	}

	public void limitActiveUsers() throws Exception {
		startStep("limit the number of access Licences in the institution");
		tmsPage = pageHelper.loginToTmsAsAdmin();
		tmsPage.clickOnInstitutions();
		String testSchoolId = dbService.getInstituteIdByName(testSchool);
		tmsPage.clickOnInstitutionDetails(testSchoolId);
		sleep(2);
		webDriver.switchToNewWindow();
		tmsPage.switchToFormFrame();
		tmsPage.setActiveLicencesUnlimited(false);
		tmsPage.setNumberOfActiveLicences("1");
		webDriver.switchToTopMostFrame();
		tmsPage.clickOnInstSettingSubmitButton();
	}

}
