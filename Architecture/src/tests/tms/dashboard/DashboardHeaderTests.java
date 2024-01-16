package tests.tms.dashboard;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import pageObjects.EdoHomePage;
import pageObjects.tms.DashboardPage;
import testCategories.unstableTests;
import Enums.UserType;
import Interfaces.TestCaseParams;

public class DashboardHeaderTests extends DashboardBasicTest {
	// fixed 11.12.14
	@Test
	@Category(unstableTests.class)
	@TestCaseParams(testCaseID = { "16908" })
	public void TestKeepSelectedClassAndCourseWhenNavigating() throws Exception {

		String classToSelect = "class015";
		String courseToSelect = "Basic 2 V1";

		startStep("Login as a teacher and open the dashboard");
		EdoHomePage edoHomePage = pageHelper.loginAsTeacher();
		DashboardPage dashboardPage = (DashboardPage) edoHomePage
				.clickOnTeachersCorner(true);

		startStep("Select class and course");
		// dashboardPage.ClickOnBar();
		webDriver.waitForJqueryToFinish();
		dashboardPage.hideSelectionBar();
		dashboardPage.selectClassInDashBoard(classToSelect);
		sleep(2);
		dashboardPage.selectCourseInDashboard(courseToSelect);
		dashboardPage.clickOnDashboardGoButton();
//		webDriver.waitForJqueryToFinish();

		startStep("Check number of students");
		testResultService.assertElementText(dashboardPage
				.getNumberOfStudentsPerClass(), dbService
				.getNumberOfStudentsInClass(classToSelect,
						autoInstitution.getInstitutionId()));

		startStep("Navigate to another report");
		dashboardPage.clickOnReports();

		startStep("Click on the Home button");
		dashboardPage.clickTmsHome();
		sleep(3);
		dashboardPage.hideSelectionBar();
		
//		webDriver.waitForJqueryToFinish();
		startStep("Check thet the class and course that where selected, are still appear as selected");
		String currentSelectedClass = dashboardPage.getSelectedClass();
		testResultService.assertEquals(classToSelect, currentSelectedClass,
				"Selected class is not: " + classToSelect);
		String currentSelectedCourse = dashboardPage.getSelectedCourse();
		testResultService.assertEquals(courseToSelect, currentSelectedCourse,
				"Selected course is not: " + courseToSelect);
	}

	@Test
	@TestCaseParams(testCaseID = { "16991" })
	public void testNavigateToDashboardUsingTheHomeButtonAsTeacher()
			throws Exception {
		startStep("Login as a teacher and open the dashboard");
		EdoHomePage edoHomePage = pageHelper.loginAsTeacher();
		DashboardPage dashboardPage = (DashboardPage) edoHomePage
				.clickOnTeachersCorner(true);

		startStep("Go out of the dashboard");
		sleep(3);
		dashboardPage.clickOnReports();
		sleep(2);

		startStep("Click the Home button");
		dashboardPage.ClickTheHomeButton();
		// dashboardPage
		webDriver.waitForJqueryToFinish();
		
		dashboardPage.hideSelectionBar();
		startStep("Check that the dashboard opens");
		String selectedClass = dashboardPage.getSelectedClass();
		testResultService.assertTrue("Classes combo box",
				selectedClass.equals(null) == false);

	}

	@Test
	@TestCaseParams(testCaseID = { "17493" })
	public void testNavigateToDashboardUsingTheHomeButtonAsSchoolAdmin()
			throws Exception {
		startStep("Login as a school admin and open the dashboard");
		EdoHomePage edoHomePage = pageHelper.loginAsSchoolAdmin();
		sleep(5);
		webDriver.closeAlertByAccept();
		DashboardPage dashboardPage = (DashboardPage) edoHomePage
				.clickOnTeachersCorner(true);

		startStep("Go out of the dashboard");
		sleep(3);
		dashboardPage.clickOnReports();
		sleep(2);

		startStep("Click the Home button");
		dashboardPage.ClickTheHomeButton();
		
		webDriver.waitForJqueryToFinish();
		
		
		dashboardPage.hideSelectionBar();
		startStep("Check that the dashboard opens");
		String selectedClass = dashboardPage.getSelectedClass();
		testResultService.assertTrue("Classes combo box",
				selectedClass.equals(null) == false);

	}

	// http://vstf2013:8080/tfs/DefaultCollection/EdusoftDev/_workitems#_a=edit&id=17894
	@Test
	@TestCaseParams(testCaseID = { "16997" })
	public void testCheckClassAndCourseWithLastProgressAreSelectedAsTeacher()
			throws Exception {
		EdoHomePage edoHomePage = pageHelper.loginAsTeacher();

		DashboardPage dashboardPage = (DashboardPage) edoHomePage
				.clickOnTeachersCorner(true);

		getClassAndCourseWithLastProgress(autoInstitution.getTeacherUserName(),
				UserType.Teacher);
		sleep(5);

		startStep("Check class and course labels");
		String classInLabel = dashboardPage.getClassLabelText();
		String courseInLabel = dashboardPage.getCourseLabelText();

		testResultService.assertEquals(classWithLastProgress, classInLabel,
				"class name not found in label");

		testResultService.assertEquals(courseWithLastProgress, courseInLabel,
				"course name not found in label");

		webDriver.waitForJqueryToFinish();
		
		dashboardPage.hideSelectionBar();
		String selectedClass = dashboardPage.getSelectedClass();
		testResultService.assertEquals(classWithLastProgress, selectedClass);

		String selectedCourse = dashboardPage.getSelectedCourse();

		testResultService.assertEquals(courseWithLastProgress, selectedCourse);
	}

	// fails due t bug 17984
	
	@TestCaseParams(testCaseID = { "17347" })
	public void testOpenDashboardAsSupervisorAndSelectTeaccher()
			throws Exception {
		String teacherName = autoInstitution.getTeacherUserName();

		report.report("Login as supervisor");
		EdoHomePage edoHomePage = pageHelper.loginAsSupervisor();
		webDriver.closeAlertByAccept();
		report.report("Click on Teachers corner");
		DashboardPage dashboardPage = (DashboardPage) edoHomePage
				.clickOnTeachersCorner(true);
		report.report("Select a teacher from the combo box");
		getClassAndCourseWithLastProgress(teacherName, UserType.SchoolAdmin);
		dashboardPage.hideSelectionBar();
		dashboardPage.selectTeacherInDashboard(teacherName);
		report.report("Check that class and course with last update are selected");

		webDriver.waitForJqueryToFinish();
		
		String selectedClass = dashboardPage.getSelectedClass();
		testResultService.assertEquals(classWithLastProgress, selectedClass);

		String selectedCourse = dashboardPage.getSelectedCourse();
		testResultService.assertEquals(courseWithLastProgress, selectedCourse);

	}

	@Test
	@TestCaseParams(testCaseID = { "17492" })
	public void testOpenDashboardAsSchoolAdminAndCheckLastProgressClassAndCourse()
			throws Exception {

		getClassAndCourseWithLastProgress(autoInstitution.getTeacherUserName(),
				UserType.SchoolAdmin);

		report.report("Login as School admin");
		EdoHomePage edoHomePage = pageHelper.loginAsSchoolAdmin();
		report.report("Click on Teachers corner");
		webDriver.closeAlertByAccept();
		DashboardPage dashboardPage = (DashboardPage) edoHomePage
				.clickOnTeachersCorner(true);
		webDriver.waitForJqueryToFinish();
		dashboardPage.hideSelectionBar();
		report.report("Check selected class and course");
		String selectedClass = dashboardPage.getSelectedClass();
		testResultService.assertEquals(classWithLastProgress, selectedClass);

		String selectedCourse = dashboardPage.getSelectedCourse();
		testResultService.assertEquals(courseWithLastProgress, selectedCourse);

	}

	@Ignore
	@TestCaseParams(testCaseID = { "17005" })
	public void dashboardGridTest() throws Exception {
		startStep("Login as a teacher and open the dashboard");
		EdoHomePage edoHomePage = pageHelper.loginAsTeacher();
		DashboardPage dashboardPage = (DashboardPage) edoHomePage
				.clickOnTeachersCorner(true);

		startStep("Check dashbaord width");
		int currentWidth = webDriver.getWindowWidth();
		// testResultService.assertEquals(getDashboardwidth(), currentWidth,
		// "Current width is not 1024");

		startStep("Check the width of widgets");
		testResultService.assertEquals("50%",
				dashboardPage.getWidgetWidth(1, 1), "wrong width of widget");

		testResultService.assertEquals("50%",
				dashboardPage.getWidgetWidth(1, 2), "wrong width of widget");

	}


	@TestCaseParams(testCaseID = { "17382", "17383" })
	public void testHeaderHiddenAndDIsplayed() throws Exception {
		startStep("Login as a teacher and open the dashboard");
		EdoHomePage edoHomePage = pageHelper.loginAsTeacher();
		DashboardPage dashboardPage = (DashboardPage) edoHomePage
				.clickOnTeachersCorner(true);

		startStep("Check that header is not displayed by default");
		webDriver.waitForJqueryToFinish();
		dashboardPage.checkThatClassComboBoxIsNotDisplayed();

		startStep("Click the handle");
		
		webDriver.waitForJqueryToFinish();
		dashboardPage.hideSelectionBar();

		startStep("Select only class and check that Go button is disabled");
		dashboardPage.selectClassInDashBoard("class1");
		sleep(3);
		boolean isEnabled = dashboardPage.getDashboardGoButtonStatus();
		testResultService
				.assertEquals(false, isEnabled, "Go button is enabled");

		startStep("Select course and click the Go button");

		startStep("Check that header becomes hidden");

	}


	public void testDashboardLoading() throws Exception {
		startStep("Login as a teacher and open the dashboard");
		EdoHomePage edoHomePage = pageHelper.loginAsTeacher();
		DashboardPage dashboardPage = (DashboardPage) edoHomePage
				.clickOnTeachersCorner(true);

		startStep("Check that header is not displayed by default");
		// webDriver.waitForJqueryToFinish();
		

	}

}
