package tests.tms.reports;

import org.junit.Before;

import Enums.UserType;
import Interfaces.TestCaseParams;
import pageObjects.EdoHomePage;
import pageObjects.tms.ReportPage;
import pageObjects.tms.TmsHomePage;
import tests.misc.EdusoftWebTest;

public class LicenseUsageReportTests extends EdusoftWebTest {

	@Before
	public void setup() throws Exception {
		super.setup();
	}

	
	@TestCaseParams(testCaseID = { "16157" })
	public void testLicenseUsageReportAsSchoolAdmin() throws Exception {
		testLicenseUsageReportAsTMSDomain_16168(UserType.SchoolAdmin);
	}

	
	@TestCaseParams(testCaseID = { "16156" })
	public void testLicenseUsageReportAsTeacher() throws Exception {
		testLicenseUsageReportAsTMSDomain_16168(UserType.Teacher);
	}

	
	@TestCaseParams(testCaseID = { "16169" })
	public void testLicenseUsageReportAsSuperVisor() throws Exception {
		testLicenseUsageReportAsTMSDomain_16168(UserType.Supervisor);
	}

	
	@TestCaseParams(testCaseID = { "16168" })
	public void testLicenseUsageReportAsTMSadmin() throws Exception {
		testLicenseUsageReportAsTMSDomain_16168(UserType.TMSAdmin);
	}

	public void testLicenseUsageReportAsTMSDomain_16168(UserType userType)
			throws Exception {
		String packageName = "FD-A3/6m_7m";
		String classForTest = "class1";
		String studentFirstName = configuration.getProperty("student");

		// Login as TMSDOMAIN
		report.report("Login as " + userType.toString());

		TmsHomePage tmsHomePage = null;
		EdoHomePage edoHomePage = null;

		switch (userType) {

		case TMSAdmin:
			tmsHomePage = pageHelper.loginToTmsAsAdmin();
			break;
		case Supervisor:
			edoHomePage = pageHelper.loginAsSupervisor();
			edoHomePage.waitForPageToLoad();
			tmsHomePage = (TmsHomePage) edoHomePage.openTeachersCorner();
			break;
		case Teacher:
			edoHomePage = pageHelper.loginAsTeacher();
			edoHomePage.waitForPageToLoad();
			tmsHomePage = (TmsHomePage) edoHomePage.openTeachersCorner();
			break;
		case SchoolAdmin:
			edoHomePage = pageHelper.loginAsSchoolAdmin();
			edoHomePage.waitForPageToLoad();
			tmsHomePage = (TmsHomePage) edoHomePage.openTeachersCorner();
			break;
		}

		// Open reports
		report.report("Click on reports");
		tmsHomePage.clickOnReports();
		// Select Course reports
		report.report("Click on course reports");
		tmsHomePage.clickOnCourseReports();
		// Select License Usage reports
		report.report("Click on license usage");
		tmsHomePage.selectCourseReport("License Usage");
		sleep(1);
		if (userType == UserType.TMSAdmin) {
			report.report("Select institution");
			tmsHomePage.selectInstitute(autoInstitution.getInstitutionName(),
					autoInstitution.getInstitutionId(), false, false);
			sleep(1);
		}
		if (userType == UserType.Supervisor) {
			report.report("Select teacher");
			tmsHomePage.selectTeacher(pageHelper.getTeacher().getUserName());
			sleep(1);
		}

		report.report("Select class");
		tmsHomePage.selectClass(classForTest, false, false);
		sleep(2);
		report.report("Select package name");
		tmsHomePage.selectPackageByName(packageName);
		sleep(2);
		tmsHomePage.clickOnGo();
		sleep(5);
		// webDriver.printScreen();
		tmsHomePage.switchToReportFrame();

		ReportPage reportPage = new ReportPage(webDriver, testResultService);
		String packageNameFromSammary = reportPage.getSammaryPackageName();
		String classNameFromSammary = reportPage.getSammaryClassName();

		testResultService.assertEquals(packageName, packageNameFromSammary,
				"Package name not found in report sammary");

		testResultService.assertEquals(classForTest, classNameFromSammary,
				"class name not found in report sammary");

		report.report("Check for specific student details");
		String studentFirstAndLastName = reportPage
				.getStduentNameFromLicenseReport(studentFirstName);
		testResultService.assertEquals(studentFirstName + " "
				+ studentFirstName, studentFirstAndLastName,
				"Student name not found");

		report.report("Check number of assigned students");
		String studentsNumber = dbService.getNumberOfStudentsInClass(
				classForTest, autoInstitution.getInstitutionId());
		testResultService.assertEquals(studentsNumber,
				reportPage.getNumberOfStudentsInClass(),
				"Number of students incorrect or not found");

	}
}
