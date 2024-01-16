package tests.tms;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Interfaces.TestCaseParams;
import pageObjects.edo.NewUXLoginPage;
import pageObjects.edo.NewUxMyProfile;
import pageObjects.tms.DashboardPage;
import pageObjects.tms.TmsCreateNewCoursePage;
import pageObjects.tms.TmsHomePage;
import tests.edo.newux.SpeechRecognitionBasicTestNewUX;


public class NewUxCEFR extends SpeechRecognitionBasicTestNewUX {

		
	NewUXLoginPage loginPage;
	DashboardPage dashboardPage;
	TmsHomePage tmsHomePage;
	TmsCreateNewCoursePage tmsNewCourse;
	NewUxMyProfile myProfile;
	
	String className;
	
	
	@Before
	public void setup() throws Exception {
		super.setup();
		
		String instId = autoInstitution.getInstitutionId();
		pageHelper.setInstitutionSupportCEFR(instId, true);
		sleep(2);
		webDriver.deleteCookiesAndRefresh();
		
		className = configuration.getProperty("classname.progress"); 
		
		report.startStep("Login as Teacher");
		pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(configuration.getProperty("teacher.username"), autoInstitution.getInstitutionId()));
		loginPage = new NewUXLoginPage(webDriver, testResultService);
		sleep(2);
		tmsHomePage = loginPage.enterTeacherUserAndPassword();
		//pageHelper.skipOptin();
		homePage.skipNotificationWindow();
	//	pageHelper.skipOnBoardingHP();
		sleep(2);
		tmsHomePage.switchToMainFrame();
		
		
	}
	
	@Test
	@TestCaseParams(testCaseID = {"45583","45587"})
	public void testCefrLabelsInDashboard() throws Exception {
		
		dashboardPage = new DashboardPage(webDriver,testResultService);
				
		report.startStep("Check CEFR in PLT widget list");
		tmsHomePage.verifyCefrInPLtWidgetList(coursesNames);
		
		report.startStep("Check CEFR in Dashboard header");
		//webDriver.waitForJqueryToFinish();
		dashboardPage.hideSelectionBar();
		tmsHomePage.verifyCefrInDashboardHeader();
			
	}
	
	@Test
	@TestCaseParams(testCaseID = {"45575"})
	public void testCefrLabelsInAssignCourses() throws Exception {
		
		report.startStep("Check CEFR in Assign Courses");
		tmsHomePage.clickOnCurriculum();
		sleep(2);
		tmsHomePage.clickOnAssignCourses();
		sleep(2);
		tmsHomePage.selectClass(className);
		sleep(2);
		tmsHomePage.verifyCefrInAssignCourses(coursesNames);
						
	}
	
	@Test
	@TestCaseParams (testCaseID = {"45580"})
	public void testCefrLabelsInPLTReports () throws Exception {
		
	report.startStep("Init test data");
		String className = configuration.getProperty("classname.progress");
		
	report.startStep("Check CEFR in Placement Test Report");
		tmsHomePage.clickOnReports();
		sleep(1);
		tmsHomePage.clickOnPLTReports();
		tmsHomePage.switchToFormFrame();
		sleep(1);
		tmsHomePage.selectPLTReport("2");
		webDriver.selectElementFromComboBox("SelectClass", className);
		sleep(1);
		tmsHomePage.selectPeriod ("27");
		sleep(1);
		tmsHomePage.ClickToGo();
	//	webDriver.switchToTopMostFrame();
	//	tmsHomePage.switchToMainFrame();
	//	sleep (1);
	//	tmsHomePage.CheckCefrLevel();

		
	}
	
	@Test
	@TestCaseParams(testCaseID = {"45577"})
	public void testCefrLabelsInViewAllCourses() throws Exception {
			
		report.startStep("Check CEFR in View ALL Courses");
		tmsHomePage.clickOnCurriculum();
		sleep(2);
		tmsHomePage.clickOnViewAllCourses();
		sleep(40);
		tmsHomePage.openPackageByName("FD-A3_10_Units");
		sleep(2);
		tmsHomePage.verifyCefrInViewAllCourses(coursesNames);
			
		
	}
	
	@Test
	@TestCaseParams(testCaseID = {"45591"})
	public void testCefrLabelsInAssessments() throws Exception {
			
		report.startStep("Navigate To Automated Tests");
			tmsHomePage.clickOnAssessment();
			sleep(2);
			//tmsHomePage.clickOnAutomatedTests();
			tmsHomePage.clickOnTestsAssignment();
			sleep(3);

		report.startStep("Select 'Assign Test'");
			webDriver.switchToTopMostFrame();
			tmsHomePage.switchToMainFrame();
			tmsHomePage.switchToFormFrame();
			tmsHomePage.selectFeature("CT");
			sleep(2);
		
		report.startStep("Select Test type");
			String className = configuration.getProperty("classname.progress");
			webDriver.selectElementFromComboBox("SelectClass",className);
			sleep(1);
			//webDriver.selectElementFromComboBox("SelectTestType", "Course Test");
			tmsHomePage.clickOnGo();
		
		report.startStep("Verify CEFR in Assessments tab for FD-A3 Courses");	
			webDriver.switchToTopMostFrame();
			tmsHomePage.switchToMainFrame();
			tmsHomePage.verifyCefrInAssessments(coursesNames);
			
		report.startStep("Switch to none-CEFR-courses class");
			tmsHomePage.switchToFormFrame();
			webDriver.selectElementFromComboBox("SelectClass", configuration.getProperty("classname.caba"));
			sleep(1);
			tmsHomePage.clickOnGo();
	 
	// TODO Uncomment this section once bug 52599 is fixed
		report.startStep("Verify no CEFR in none FD-A3 Courses");
			webDriver.switchToTopMostFrame();
			tmsHomePage.switchToMainFrame();
			webDriver.checkElementNotExist("//span[@class='cefrLevel']");
	
	}
	
	@Test
	@TestCaseParams(testCaseID = {"45579"})
	public void testCefrLabelsInCourseBuilder() throws Exception {
				
		report.startStep("Check CEFR in Course Builder New Course");
		tmsHomePage.clickOnCurriculum();
		sleep(2);
		tmsHomePage.clickOnCourseBuilder();
		sleep(2);
		tmsNewCourse = tmsHomePage.clickOnNewInCourseBuilder();
		sleep(3);
		webDriver.switchToNextTab();
		tmsHomePage.verifyCefrInCourseBuilderDropDownList(coursesNames, "coursesSel");
		
		report.startStep("Check CEFR in Course Builder Course Edit");
		tmsNewCourse.enterNewCourseName("new");
		sleep(1);
		tmsNewCourse.selectBasedOnOption();
		sleep(1);
		tmsNewCourse.clickOnOK();
		sleep(1);
		webDriver.switchToMainWindow();
		tmsHomePage.switchToMainFrame();
		tmsHomePage.verifyCefrInCourseBuilderDropDownList(coursesNames, "selectLevel1_1");
		
	}
				
	@After
	public void tearDown() throws Exception {
		pageHelper.setInstitutionSupportCEFR(autoInstitution.getInstitutionId(), false);
		super.tearDown();
		
	}
		
	
}
