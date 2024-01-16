package tests.tms;

import Enums.ByTypes;
import Enums.PLTStartLevel;
import Interfaces.TestCaseParams;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.Color;
import pageObjects.edo.*;
import pageObjects.tms.TmsAssessmentsTestsAssignmentPage;
import pageObjects.tms.TmsAssessmentsTestsConfigurationPage;
import pageObjects.tms.TmsHomePage;
import testCategories.edoNewUX.ReleaseTests;
import testCategories.edoNewUX.SanityTests;
import testCategories.inProgressTests;
import tests.edo.newux.BasicNewUxTest;
import tests.edo.newux.SpeechRecognitionBasicTestNewUX;

import java.util.ArrayList;
import java.util.List;

public class NewUxAssessmentsTests extends SpeechRecognitionBasicTestNewUX {

	
	NewUXLoginPage loginPage;
	TmsHomePage tmsHomePage;
	NewUxAssessmentsPage testsPage;
	NewUxClassicTestPage classicTest;
	TestEnvironmentPage testEnvironmentPage;

	@Before
	public void setup() throws Exception {
		super.setup();
	}
		
	
	@Test
	@TestCaseParams(testCaseID = { "33720" })
	public void testAbilityToRemovePLT() throws Exception {

		int iteration = 1;
		//institutionId = dbService.getInstituteIdByName(institutionsName[3]); 
		
		institutionName=institutionsName[3];//
		
		report.startStep("Change to Courses Instituion");
		pageHelper.restartBrowserInNewURL(institutionName, true); 
		institutionId = dbService.getInstituteIdByName(institutionName);
		sleep(2);
		
		//instID = configuration.getProperty("local_institutionId");
		
		report.startStep("Open New UX Login Page");
		loginPage = new NewUXLoginPage(webDriver, testResultService);
		
		while (iteration<=2) {
		
		report.startStep("Login as Admin");
		pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(configuration.getProperty("schoolAdmin.user"),institutionId));
		sleep(1);
		tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
		sleep(3);
		tmsHomePage.switchToMainFrame();
		
		report.startStep("Open PLT settings page");
		//tmsHomePage.clickOnAssessment();
		//sleep(5);
		//tmsHomePage.clickOnAutomatedTests();
		TmsAssessmentsTestsConfigurationPage tmsAssessmentsTestsConfigurationPage = new TmsAssessmentsTestsConfigurationPage(webDriver, testResultService);
		tmsAssessmentsTestsConfigurationPage.goToAssessmentTestsConfiguration();
		sleep(3);
		webDriver.switchToTopMostFrame();
		tmsHomePage.switchToMainFrame();
		tmsHomePage.switchToFormFrame();
		tmsHomePage.selectFeature("PT");
		sleep(3);
		tmsHomePage.clickOnGo();
		sleep(2);
		webDriver.switchToTopMostFrame();
		tmsHomePage.switchToMainFrame();
		if (iteration == 1 ) testResultService.assertEquals(true, tmsHomePage.isPltSettingsDisplayed(), "PLT settings hiden");
		else testResultService.assertEquals(false, tmsHomePage.isPltSettingsDisplayed(), "PLT settings not hiden");
		
		report.startStep("Change Assign PLT option and check PLT settings view");
		tmsHomePage.checkUncheckAssignPLT();
		if (iteration == 1 ) testResultService.assertEquals(false, tmsHomePage.isPltSettingsDisplayed(), "PLT settings not hiden");
		else testResultService.assertEquals(true, tmsHomePage.isPltSettingsDisplayed(), "PLT settings hiden");
		
		report.startStep("Press Save");
		tmsHomePage.clickOnSave();
		
		report.startStep("Exit TMS");
		tmsHomePage.clickOnExit();
		sleep(3);
		
		/*report.startStep("Get student details of institution Local");
		String[] userDetails = getLocalUser();
		
		String userName = userDetails[0];
		String password = userDetails[1];
		studentId = userDetails[2];
		
		report.startStep("Login as student");
		NewUXLoginPage loginPage = new NewUXLoginPage(webDriver,testResultService);
		homePage = loginPage.loginAsStudent(userName,password);*/
		
		report.startStep("Login as student");
		String className = configuration.getProperty("local_className");
		createUserAndLoginNewUXClass(className, institutionId);
		
		/*report.startStep("Login as student");
		String className = configuration.getProperty("classname.CourseTest");
		createUserAndLoginNewUXClass(className, coursesInstId);*/
		
		pageHelper.skipOnBoardingHP();
		
		report.startStep("Open Assessments");
		testsPage = homePage.openAssessmentsPage(false);
		//sleep(2);
				
		report.startStep("Check Counter value in ALL sections");
		if (iteration == 1 ) testsPage.checkItemsCounterBySection("1", "0");// Available 
		else testsPage.checkItemsCounterBySection("1", "1");// Available
		testsPage.checkItemsCounterBySection("2", "0");// Upcoming
		testsPage.checkItemsCounterBySection("3", "0");// Test Results
		
		report.startStep("Verify Placement Test presence in Available section");
		if (iteration == 1 ) testsPage.checkPLTNotDisplayedInSectionByTestName(false);
		else testsPage.checkPLTNotDisplayedInSectionByTestName(true);
		
		report.startStep("Close Assessments");
		testsPage.close();
		sleep(2);
		
		report.startStep("Logout as student");
		homePage.clickOnLogOut();
		sleep(2);
		webDriver.refresh();
		
		sleep(1);
					
		iteration++;	
		}
		
	}
		
	private void logoutFromTmsAndCheckEdTabsClosed() throws Exception {
		webDriver.switchToMainWindow();
		tmsHomePage.switchToMainFrame();
		tmsHomePage.clickOnExit();
		sleep(2);
		testResultService.assertEquals(false, loginPage.isSubmitButtonEnabled(), "Checking that Login Page displayed");
		webDriver.checkNumberOfTabsOpened(1);
	}
				

	@Test
	@TestCaseParams(testCaseID = { "44239","27320","27321" })
	public void testAbilityToCustomizePLTSettings() throws Exception {
	
		int iteration = 1;
		//instID = configuration.getProperty("local_institutionId");
		
		String alertExpected = "You cannot take the placement test at this time. "
				+ "If you would like to be assigned a placement test, please contact your program coordinator.";
		
		report.startStep("Open New UX Login Page");
		loginPage = new NewUXLoginPage(webDriver, testResultService);
		
		while (iteration<=2) {
		
		report.startStep("Login as Admin");
		//pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(configuration.getProperty("schoolAdmin.user"),instID));
		sleep(1);
		tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
		sleep(3);
		tmsHomePage.switchToMainFrame();
		
		report.startStep("Open PLT settings page");
		/*tmsHomePage.clickOnAssessment();
		sleep(3);
		tmsHomePage.clickOnAutomatedTests();
		sleep(3);*/
		TmsAssessmentsTestsConfigurationPage tmsAssessmentsTestsConfigurationPage = new TmsAssessmentsTestsConfigurationPage(webDriver, testResultService);
		tmsAssessmentsTestsConfigurationPage.goToAssessmentTestsConfiguration();
		webDriver.switchToTopMostFrame();
		tmsHomePage.switchToMainFrame();
		tmsHomePage.switchToFormFrame();
		tmsHomePage.selectFeature("PT");
		sleep(3);
		tmsHomePage.clickOnGo();
		sleep(2);
		webDriver.switchToTopMostFrame();
		tmsHomePage.switchToMainFrame();
		
		webDriver.waitForElement("OnlyOncePT", ByTypes.id).click();
		
		List<WebElement> chkRbtns  = webDriver.getElementsByXpath("//*[@id='PTSettings']/table/tbody/tr/td/input");
		if (iteration == 1 )
		{
			for (int i = 0; i < chkRbtns.size(); i++) {
				if(chkRbtns.get(i).getAttribute("value").equals("0") && chkRbtns.get(i).getAttribute("name").equals("ShowPTResults")) {
					chkRbtns.get(i).click();
					break;
				}
			}
		}
		else {
			for (int i = 0; i < chkRbtns.size(); i++) {
				if(chkRbtns.get(i).getAttribute("value").equals("1") && chkRbtns.get(i).getAttribute("name").equals("ShowPTResults")) {
					chkRbtns.get(i).click();
					break;
				}
			}
		}
		
		report.startStep("Press Save");
		tmsHomePage.clickOnSave();
		
		report.startStep("Exit TMS");
		tmsHomePage.clickOnExit();
		sleep(3);
		
		report.startStep("Get student details of institution Local with PLT score");
		String[] usersData = pageHelper.getUsersDidPlt(institutionId);
		
		String userName = usersData[0];
		String password = usersData[1];
		studentId = usersData[2];
		
		report.startStep("Login as student");
		NewUXLoginPage loginPage = new NewUXLoginPage(webDriver,testResultService);
		homePage = loginPage.loginAsStudent(userName,password);
		homePage.closeModalPopUp();
		
		report.startStep("Check if student has mid term test performed as well (in order to check counter later)");
		String didMidTerm = dbService.getDidTestByUserId(studentId);
		String testsCounterAssessments = "";
		String completedTestFirstIteration = "";
		if (didMidTerm.equals("1")) {
			testsCounterAssessments = "2";
			completedTestFirstIteration = "1";
		} else if (didMidTerm.equals("0")) {
			testsCounterAssessments = "1";
			completedTestFirstIteration = "0";
		}
		
		report.startStep("Open Assessments");
		testsPage = homePage.openAssessmentsPage(false);
		
		report.startStep("Verify see results status");
		if (iteration == 1 ) {
			testsPage.checkItemsCounterBySection("3", completedTestFirstIteration);// Test Results
		} else {
			testsPage.checkItemsCounterBySection("3", testsCounterAssessments);// Test Results 
			testsPage.clickOnArrowToOpenSection("3");
			testsPage.checkTestDisplayedInSectionByTestName("Placement Test", "3", testsCounterAssessments); 
			testsPage.clickOnArrowToOpenSection("1");
		}
		
		report.startStep("Press on Start PLT");
		classicTest = testsPage.clickOnStartTest("1", "1");
		if (iteration == 1 ) {
			sleep(1);
			String alertMessage = webDriver.getAlertText(15);
			testResultService.assertEquals(alertMessage, alertExpected, "Different alert appeared");
			webDriver.closeAlertByAccept();
			report.report("Closing Assessments tab");
			testsPage.close();
			sleep(2);
		}
		else
		{	
			sleep(2);
			webDriver.switchToNewWindow();
			classicTest.switchToTestAreaIframe();
			webDriver.waitForElement("//*[@id='exitCell']/table/tbody/tr/td[2]/img", ByTypes.xpath).click();
			webDriver.switchToMainWindow();
		}		
		
		report.startStep("Logout as student");
		homePage.clickOnLogOut();
		sleep(2);
					
		iteration++;	
		}
		
	}

	@Test
	@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "7368" })
	public void testAssignMidTermTest() throws Exception {
	
		report.startStep("Init Needed Vars");
			String className = "assessmentClass";
			String userName = "stForAssessment";
			studentId = dbService.getUserIdByUserName(userName, institutionId);
			String classId = dbService.getUserClassId(studentId);
		report.startStep("Verifying if any student from this class assigned to a group");	
		
		//String[]student = pageHelper.getStudentAssignedToGroup(classId);
			String groupName = "";
			String[]groupInfo =	dbService.getGroupNameAndGroupIdOfSpecificClass(classId);
			String groupId = groupInfo[0];
			pageHelper.assignStudentToGroup(classId,studentId,groupInfo[0]);
			groupName = groupInfo[2];
			className = dbService.getClassNameByClassId(classId);

			String userFirstName = dbService.getUserFirstNameByUserId(studentId);
			String UserFullName = userFirstName + " " + dbService.getUserLastNameByUserId(studentId);
			//String className = dbService.getClassNameByClassId(classId);
			String TestFullName = "Basic 1 Midterm Test";
			String courseName =  coursesNames[1];
			String currentDate = pageHelper.getCurrentDateByFormat("dd/MM/yyyy");
			//String groupName = "";
		report.startStep("Login as Admin");
			loginPage = new NewUXLoginPage(webDriver, testResultService);
			pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(configuration.getProperty("schoolAdmin.user"),institutionId));
			sleep(1);
			tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
			sleep(3);

		// Initialize tms assessments automated tests page
			TmsAssessmentsTestsAssignmentPage tmsAssessmentsAutomatedTestsPage = new TmsAssessmentsTestsAssignmentPage(webDriver, testResultService);
			
		// Go to assign test page and choose feature, class and student
			tmsAssessmentsAutomatedTestsPage.goToAssignTestPageAndChooseDetails("MT", className, UserFullName,groupName, false);
	
		report.startStep("Select Student from List");
			tmsAssessmentsAutomatedTestsPage.clickUserCheckbox(userFirstName);
			
		report.startStep("Set Test Level");
			tmsAssessmentsAutomatedTestsPage.selectTestLevel(courseName);
		
		report.startStep("Set Test Time");
			String startTime = "00:00";
			String endTime = "23:59";
			tmsAssessmentsAutomatedTestsPage.setTime(currentDate, currentDate, startTime, endTime);

		String testId="";
		try{
		report.startStep("Click Save");
			tmsHomePage.clickOnSave();
			sleep(2);

		//	if (tmsAssessmentsAutomatedTestsPage.validateConflictsLegendIsDisplayed() != null)
		//		tmsAssessmentsAutomatedTestsPage.clickSaveAnyWayButton();

			webDriver.closeAlertByAccept();

			report.startStep("Log out of TMS and Get the assigned test Id");
				tmsHomePage.clickOnExit();
				testId = dbService.getStudentAssignedTestId(studentId,2);
				sleep(2);
				testResultService.assertEquals(false, loginPage.isSubmitButtonEnabled(), "Checking that Login Page displayed");
				
			report.startStep("Log in as student");
				pageHelper.setUserLoginToNull(studentId);
				loginPage.loginAsStudent("stForAssessment","12345");
			//	pageHelper.skipOptin();
				homePage.skipNotificationWindow();
			//	pageHelper.skipOnBoardingHP();
				homePage.closeModalPopUp();
				sleep(2);
				
			report.startStep("Open assessments tab and verify test is available.");
				testsPage = homePage.openAssessmentsPage(false);
				testsPage.checkItemsCounterBySection("1", "2");// Available 
				testsPage.checkTestDisplayedInSectionByTestName(TestFullName, "1", "2");

			report.startStep("Start test and submit first section with correct answer and check test button alert");
				classicTest = testsPage.clickOnStartTest("1", "2");
				webDriver.closeAlertByAccept();
				sleep(2);
				webDriver.switchToNewWindow();
				classicTest.switchToTestAreaIframe();
				classicTest.pressOnStartTest();
				classicTest.waitForTestPageLoaded();


			report.startStep("Close Test and logout");
				//testsPage.close();
				//sleep(2);
				classicTest.close();
				homePage.clickOnLogOut();
				

		} catch (Exception e) {
			testResultService.addFailTest("Fail to continue test as student");
			dbService.deleteExitTestForUserIdAndTestType(studentId, 2);

		}
		finally {
			report.startStep("Mid-Term Test Removal");
			//dbService.updateCompletedTest(studentId,testId,"2","1");
			dbService.deleteExitTestForUserId(studentId);
		}
	}
	
	@Test
	@TestCaseParams(testCaseID = { "51357" })
	public void testAssignRandomTestForClass() throws Exception {
		
		report.startStep("Init Test Data");
			String CourseId = "44845"; // Intermediate 1
			String TestId = "20116"; // Basic 1 Mid-term
			int testType = 2; //3 means CT ; 2 means mid-term
			int i;
			List<String> testIDList;
			//String instID = configuration.getProperty("institution.id");
			String className = configuration.getProperty("classname.progress");
			String classId = dbService.getClassIdByName(className, institutionId);
			String courseName = "Basic 1";
			String currentDate = pageHelper.getCurrentDateByFormat("dd/MM/yyyy");
			
		try {	
			report.startStep("Add additional test in TestBank For Course");
				dbService.addTestToCourseByIds(CourseId,TestId,testType);

			report.startStep("Turn On 'Allow Multi-test' for Institution");
				dbService.enableMultiTestForInstitution(institutionId);

			report.startStep("Login to TMS as admin");
				loginPage = new NewUXLoginPage(webDriver, testResultService);
				pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(configuration.getProperty("schoolAdmin.user"),institutionId));
				sleep(1);
				tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
				sleep(3);
				
			// Initialize tms assessments automated tests page
				TmsAssessmentsTestsAssignmentPage tmsAssessmentsAutomatedTestsPage = new TmsAssessmentsTestsAssignmentPage(webDriver, testResultService);
				
			report.startStep("Go to Assessments -> Automated Tests");
				tmsAssessmentsAutomatedTestsPage.goToAssessmentTestsAssignment();
				
			report.startStep("Select Feature and Class Name");
				tmsAssessmentsAutomatedTestsPage.selectFeatureAndClass("MT", className);
				
			report.startStep("Select All Students in Scope");
				tmsAssessmentsAutomatedTestsPage.selectAllStudentsInScope();
				
			report.startStep("Set Test Level");
				tmsAssessmentsAutomatedTestsPage.selectTestLevel(courseName);
			
			report.startStep("Set Test Time");
				String startTime = "00:00";
				String endTime = "23:59";
				tmsAssessmentsAutomatedTestsPage.setTime(currentDate, currentDate, startTime, endTime);
				
			report.startStep("Click Save");
				tmsHomePage.clickOnSave();
				webDriver.closeAlertByAccept();
			
			report.startStep("Get all test Ids assigned to class");
				testIDList = dbService.getAssignedExitTestOfEntireClass(classId, testType);
				
			report.startStep("Make sure at least one student received different test");
				for (i=1 ; i< testIDList.size() ; i++) {
					if (!testIDList.get(0).equals(testIDList.get(i)))
						break;
				}
				if (i == testIDList.size())
					testResultService.addFailTest("All values are identical and assignment wasn't random. Tests IDs: " + testIDList);
		} finally {
			report.startStep("Undo DB changes");
				//dbService.deleteExitTestForEntireClass(classId,testType);
				//instID = configuration.getProperty("institution.id");
				//dbService.disableMultiTestForInstitution(instID);
				//dbService.removeTestFromCourseByIds(CourseId,TestId,testType);
		}
	}
	
	
	private void setHourValueForTest(String field, String Text) throws Exception {
		
		//webDriver.waitForElement("//*[@id='" +  field + "']", ByTypes.xpath).clear();
		webDriver.waitForElement("//*[@id='" +  field + "']", ByTypes.xpath).sendKeys("\u0008\u0008");
		webDriver.waitForElement("//*[@id='" +  field + "']", ByTypes.xpath).sendKeys(Text);
	}
	
	private void setDateAsTodayById(String field) throws Exception {
		webDriver.waitForElement(field, ByTypes.id).click();
		webDriver.switchToPopup();
		webDriver.waitForElement("//a[@class='todaylink']", ByTypes.xpath).click();
		webDriver.switchToMainWindow();
		tmsHomePage.switchToMainFrame();
	}
	
	@Test
	//@TestCaseParams(testCaseID = { "79355", "79477" })
	public void testStartMidTermTest2minBeforeExpiration() throws Exception {
		
		institutionName=institutionsName[1];//
		
	report.startStep("Change to Courses Instituion");
		pageHelper.restartBrowserInNewURL(institutionName, true); 
		sleep(2);
		
	report.startStep("Init Needed Vars");
		String className = configuration.getProperty("classname.CourseTest");
		studentId = pageHelper.createUSerUsingSP(institutionId, className);
		sleep(2);
		int testTypeId = 2;
		String userFirstName = dbService.getUserFirstNameByUserId(studentId);
		String classId = dbService.getUserClassId(studentId);
		String courseName = coursesNames[1];
		String TestFullName = courseName + " " + pageHelper.getImplementationTypeName(2,"ED");
		String courseId = courses[1];
		
		try {
		
		report.startStep("Login as Admin");
			loginPage = new NewUXLoginPage(webDriver, testResultService);
			pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(configuration.getProperty("schoolAdmin.user"),configuration.getProperty("institution.id")));
			sleep(1);
			tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
			sleep(3);
			//homePage.skipNotificationWindow();
			tmsHomePage.waitForPageToLoad();
	
		report.startStep("Go to Assessments -> Automated Tests");
			TmsAssessmentsTestsAssignmentPage tmsAssessmentsTestsAssignmentPage = new TmsAssessmentsTestsAssignmentPage(webDriver, testResultService);
			tmsAssessmentsTestsAssignmentPage.goToAssessmentTestsAssignment();
			tmsAssessmentsTestsAssignmentPage.selectFeatureAndClass("MT", className);

		report.startStep("Get Expected Start Date (Current Date) and End Date (By Institution Settings)");
			String currentDate = pageHelper.getCurrentDateByFormat("yyyy-MM-dd HH:mm:ss.SS");
			int daysInInstSettings = pageHelper.getEndDateOffSetOfInstitution(institutionId);
			String expectedEndDate = textService.updateTime(currentDate,"add", "day",daysInInstSettings);
			expectedEndDate = textService.convertDateToDifferentFormat(expectedEndDate, "yyyy-MM-dd HH:mm:ss.SS", "dd/MM/yyyy");
			currentDate = textService.convertDateToDifferentFormat(currentDate, "yyyy-MM-dd HH:mm:ss.SS", "dd/MM/yyyy");
			
		report.startStep("Validate Start And End Date Are Correct");
			tmsAssessmentsTestsAssignmentPage.validateStartAndEndDateAreCorrect(currentDate, expectedEndDate);
			
		report.startStep("Validate Start And End Time Are Correct");
			tmsAssessmentsTestsAssignmentPage.validateStartAndEndTimeAreCorrect("00", "00", "00", "00");
			
		report.startStep("Validate No Course is Chosen");
			tmsAssessmentsTestsAssignmentPage.validateNoCourseIsChosen();
			sleep(4);
			
		report.startStep("Select Student from List");
			tmsAssessmentsTestsAssignmentPage.clickUserCheckbox(userFirstName);
			
		report.startStep("Set Test Level");
			tmsAssessmentsTestsAssignmentPage.selectTestLevel(courseName);
		
		report.startStep("Set Test Time");
			String startTime = "00:00";
			String endTime = "23:59";
			tmsAssessmentsTestsAssignmentPage.setTime(currentDate, currentDate, startTime, endTime);
			
		report.startStep("Validate Test Length against DB");
			tmsAssessmentsTestsAssignmentPage.validateTestLengthTestEnvironment(testTypesId[1]);
			
		report.startStep("Edit the Test len to 50 minutes");
			tmsAssessmentsTestsAssignmentPage.setOldTMSCourseTestdurationInMinutes("50");
			testEnvironmentPage = new TestEnvironmentPage(webDriver, testResultService);
			
		report.startStep("Click Save");
			tmsHomePage.clickOnSave();
			
		report.startStep("Validate Messsage is Displayed");
			tmsAssessmentsTestsAssignmentPage.validateMessageContent("You assigned "+TestFullName +" to 1 student.\nThe test will be available from "+currentDate+" "+startTime+" to "+currentDate+" "+endTime+".");
			sleep(3); 
			currentDate = textService.convertDateToDifferentFormat(currentDate, "dd/MM/yyyy", "MM/dd/yyyy"); 
			if (currentDate.startsWith("0")) {
				currentDate = currentDate.substring(1);
			}
			currentDate = currentDate.replaceAll("/0", "/");
			
		report.startStep("Validate Stored Token Is Correct");
			List<String[]> userTestSettingsDetails = dbService.getUserExitTestSettings(studentId, testTypeId);
			String tokenFromDB = userTestSettingsDetails.get(0)[2];
			testResultService.assertEquals(false, tokenFromDB.equals(null),"Stored Token Is Incorrect.");
			
		report.startStep("Validate Status in TMS");
			String testID = userTestSettingsDetails.get(0)[1];
			String TestFullNameTms = dbService.getCourseNameById(testID);
			tmsAssessmentsTestsAssignmentPage.validateTestIsAssignedInTMS(userFirstName, TestFullNameTms, currentDate, currentDate);
			
		
		report.startStep("Log out of TMS");
			//webDriver.switchToMainWindow();
			//tmsHomePage.switchToMainFrame();
			tmsHomePage.clickOnExit();
			sleep(2);
			testResultService.assertEquals(false, loginPage.isSubmitButtonEnabled(), "Checking that Login Page displayed");
			sleep(2);
			
		report.startStep("Log in as student");
			loginPage.loginAsStudent(userFirstName,"12345");
			homePage.closeModalPopUp();
			homePage.waitHomePageloaded();
			
		report.startStep("Change expiration UTC time up to 2 min");
			String expirationPLTtime = dbService.addMinutesToUTCtime(2);
			expirationPLTtime = expirationPLTtime.split("[.]")[0];
			dbService.updateEnd_UTC_DateInUserExitTestSetting(expirationPLTtime,studentId);
			
		report.startStep("Open assessments tab and verify test is available.");
			testsPage = homePage.openAssessmentsPage(false);
			testsPage.checkItemsCounterBySection("1", "1");
			testsPage.checkTestDisplayedInSectionByTestNameAndCourseId(TestFullName, "1",courseId);
			sleep(2);
									
		report.startStep("Click Start Test Button");
			int testSequence = testsPage.getTestSequenceByCourseId(courseId);
			testsPage.clickStartTest(1, testSequence);
			webDriver.closeAlertByAccept(3);
			sleep(4);
			testEnvironmentPage.clickStartTest();
			sleep(5);
					
		report.startStep("Validate Timer is Correct");
			String expectedEdTimeFormat = testEnvironmentPage.convertTimeInMinutesToEDTimerFormat(50-1);
			testEnvironmentPage.getAndValidateEdTestTimer(expectedEdTimeFormat);
		
		report.startStep("Wait 4 min till time will be expired and validate alert is not apeared");
			sleep(240);
			testEnvironmentPage.clickNext();
			int i = 0;
			WebElement expirationTestAlert = webDriver.waitForElement("btnOk", ByTypes.id, 5, false);//div[contains(@class,'TinyScrollbarW')]/p
			while(expirationTestAlert==null && i<5) {
				expirationTestAlert = webDriver.waitForElement("btnOk", ByTypes.id, 5, false);
				i++;
				}
			if(expirationTestAlert != null){
				testResultService.addFailTest("Student had time per test but it was terminated", false, true);
				expirationTestAlert.click();
				
				}else {
					testEnvironmentPage.clickClose();
					homePage.waitHomePageloaded();
				}
			
			homePage.logOutOfED();
			
		}finally {
			report.startStep("Mid-Term Test Removal");
			dbService.deleteExitTestForUserIdAndTestType(studentId, testTypeId);
			dbService.deleteUserIdByClassId(classId, studentId);
			pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(configuration.getProperty("schoolAdmin.user"),institutionId));
		}
	}
	
	@Test
	@TestCaseParams(testCaseID = { "79355", "79477" })
	public void testAssignMidTermTestNewTE() throws Exception {
		
		institutionName=institutionsName[1];//
		
		report.startStep("Change to Courses Instituion");
		pageHelper.restartBrowserInNewURL(institutionName, true); //
		sleep(2);
		
		report.startStep("Init Needed Vars");
		String className = configuration.getProperty("classname.CourseTest");
				//String UserID = pageHelper.createUSerUsingSP(configuration.getProperty("institution.id"), configuration.getProperty("classname.assessment"));
		studentId = pageHelper.createUSerUsingSP(institutionId, className);
		sleep(2);
		int testTypeId = 2;
		List<String[]> student = dbService.getUserNameAndPasswordByUserId(studentId);
		String userFirstName = student.get(0)[3];//dbService.getUserFirstNameByUserId(UserID);
		userName = student.get(0)[0];
		String classId = dbService.getUserClassId(studentId);
		//String className = configuration.getProperty("classname.assessment"); //dbService.getClassNameByClassId(classId);
		String courseName = coursesNames[1];
		String TestFullName = courseName + " " + pageHelper.getImplementationTypeName(2,"ED");//courseName + " " + testTypes[1] + " Test";
		String courseId = courses[1];
		
		report.startStep("Pre Condition = Create Students in Status Incomplete");
		String userIdOfInProgress = pageHelper.createUSerUsingSP(institutionId, className);
		
		// Assign B1 Mid-Term Test to student
		pageHelper.assignAutomatedTestToStudetInTestEnvironment(userIdOfInProgress, courseId, testTypeId,0,0,1);
		String[] testAdministration = pageHelper.getAssignedCourseTestIdForStudent(userIdOfInProgress,courseId, testTypeId);
		String testId = testAdministration[3];
			
		// Change status to incomplete
		dbService.updateDidTest(userIdOfInProgress, testId, String.valueOf(testTypeId), "1");
		String userStartedUTC = pageHelper.getCurrentDateByFormat("yyyy-MM-dd HH:mm:ss");
		dbService.updateUserStartedUTC(userIdOfInProgress, testId, String.valueOf(testTypeId), userStartedUTC);
		sleep(3);
		
		try {
			
			report.startStep("Login as Admin");
			loginPage = new NewUXLoginPage(webDriver, testResultService);
			pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(configuration.getProperty("schoolAdmin.user"),configuration.getProperty("institution.id")));
			sleep(1);
			tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
			//sleep(5);
			
			// Initialize tms assessments automated tests page
			TmsAssessmentsTestsAssignmentPage tmsAssessmentsTestsAssignmentPage = new TmsAssessmentsTestsAssignmentPage(webDriver, testResultService);
			//webDriver.waitUntilElementAppears("TopNavBg", ByTypes.className, 30);
			
			report.startStep("Go to Assessments -> Automated Tests");
			tmsAssessmentsTestsAssignmentPage.goToAssessmentTestsAssignment();
			
			// Go to Assign test and choose Feature, className, user
			tmsAssessmentsTestsAssignmentPage.selectFeatureAndClass("MT", className);

			report.startStep("Validate All Students are Unchecked"); 
			//tmsAssessmentsTestsAssignmentPage.validateAllStudentsCheckboxesAreUnchecked();
			//sleep(2);
			
			report.startStep("Validate Students with Status In Progress Are Disabled"); 
			//tmsAssessmentsTestsAssignmentPage.checkInProgressStudentAreDisabled();
			//sleep(2);
			
			report.startStep("Get Expected Start Date (Current Date) and End Date (By Institution Settings)");
			String currentDate = pageHelper.getCurrentDateByFormat("yyyy-MM-dd HH:mm:ss.SS");
			
			//int daysInInstSettings = tmsAssessmentsAutomatedTestsPage.getDaysInInstitutionSettings("ET");
			//tmsAssessmentsAutomatedTestsPage.selectFeatureAndClass("MT", className);
			int daysInInstSettings = pageHelper.getEndDateOffSetOfInstitution(institutionId);
			
			String expectedEndDate = textService.updateTime(currentDate,"add", "day",daysInInstSettings);
			expectedEndDate = textService.convertDateToDifferentFormat(expectedEndDate, "yyyy-MM-dd HH:mm:ss.SS", "dd/MM/yyyy");
			currentDate = textService.convertDateToDifferentFormat(currentDate, "yyyy-MM-dd HH:mm:ss.SS", "dd/MM/yyyy");
			
			report.startStep("Validate Start And End Date Are Correct");
			tmsAssessmentsTestsAssignmentPage.validateStartAndEndDateAreCorrect(currentDate, expectedEndDate);
			
			report.startStep("Validate Start And End Time Are Correct");
			tmsAssessmentsTestsAssignmentPage.validateStartAndEndTimeAreCorrect("00", "00", "00", "00");
			
			report.startStep("Validate No Course is Chosen");
			tmsAssessmentsTestsAssignmentPage.validateNoCourseIsChosen();
			sleep(4);
			
			report.startStep("Select Student from List");
			tmsAssessmentsTestsAssignmentPage.clickUserCheckbox(userFirstName);
			//tmsHomePage.findStudentInSearchSection(className,userName);
			report.startStep("Set Test Level");
			tmsAssessmentsTestsAssignmentPage.selectTestLevel(courseName);
		
			report.startStep("Set Test Time");
			String startTime = "00:00";
			String endTime = "23:59";
			tmsAssessmentsTestsAssignmentPage.setTime(currentDate, currentDate, startTime, endTime);
			
			report.startStep("Validate Test Length against DB");
			//tmsAssessmentsTestsAssignmentPage.validateOldTMsTestLength(institutionName);
			tmsAssessmentsTestsAssignmentPage.validateTestLengthTestEnvironment(testTypesId[1]); // relevant for new te- US: set Default Time //
			
			report.startStep("Edit the Test len to 50 minutes");
			tmsAssessmentsTestsAssignmentPage.setOldTMSCourseTestdurationInMinutes("50");
			
			// Initialize Test Environment Page
			testEnvironmentPage = new TestEnvironmentPage(webDriver, testResultService);
			
			report.startStep("Click Save");
			tmsHomePage.clickOnSave();
			
			report.startStep("Validate Messsage is Displayed");
			tmsAssessmentsTestsAssignmentPage.validateMessageContent("You assigned "+TestFullName +" to 1 student.\nThe test will be available from "+currentDate+" "+startTime+" to "+currentDate+" "+endTime+".");
			sleep(3); 
			
			currentDate = textService.convertDateToDifferentFormat(currentDate, "dd/MM/yyyy", "MM/dd/yyyy"); 
			if (currentDate.startsWith("0")) {
				currentDate = currentDate.substring(1);
			}
			currentDate = currentDate.replaceAll("/0", "/");
			
			report.startStep("Validate Mid Assignment Appears in DB");
			//List<String[]> userAdministrationRecords = dbService.getUserTestAdministrationsByStudentId(UserID);
			//String[] UserAdministration = testEnvironmentPage.getUserAdministrationRecordsByCourseId(userAdministrationRecords,userAdministrationRecords.get(0)[2]);
			
			testAdministration = pageHelper.getAssignedCourseTestIdForStudent(studentId,courseId, testTypeId);
			int courseTestLen = Integer.parseInt(testAdministration[3]);
		
			report.startStep("Validate Stored Token Is Correct");
			//String courseId = dbService.getCourseIdByNameAndVersion(courseName, "4.33"); // not retrieving the correct course id so its hardcoded- need to change
			//String courseId = courses[1];
			List<String[]> userTestSettingsDetails = dbService.getUserExitTestSettings(studentId, testTypeId);
			//String userExitSettingsId = userTestSettingsDetails.get(0)[0];
			String tokenFromDB = userTestSettingsDetails.get(0)[2];
			//String token = pageHelper.getTokenFromConsoleWithParameters("1", UserID, testId, userExitSettingsId); 
			testResultService.assertEquals(false, tokenFromDB.equals(null),"Stored Token Is Incorrect.");
			
			report.startStep("Validate Status in TMS");
			String testID = userTestSettingsDetails.get(0)[1];
			String TestFullNameTms = dbService.getCourseNameById(testID);
			tmsAssessmentsTestsAssignmentPage.validateTestIsAssignedInTMS(userFirstName, TestFullNameTms, currentDate, currentDate);
			
			report.startStep("Select Student Again and Click Save Again and Validate New Token is Generated");
			tmsAssessmentsTestsAssignmentPage.clickUserCheckbox(userFirstName);
			tmsHomePage.clickOnSave();
			webDriver.closeAlertByAccept();
			userTestSettingsDetails = dbService.getUserExitTestSettings(studentId, testTypeId);
			String secondTokenFromDB = userTestSettingsDetails.get(0)[2];
			testResultService.assertEquals(false, tokenFromDB.equals(secondTokenFromDB), "Same token generated for different assignments.");
			sleep(2);
			
			report.startStep("Log out of TMS");
			//webDriver.switchToMainWindow();
			//tmsHomePage.switchToMainFrame();
			tmsHomePage.clickOnExit();
			sleep(2);
			testResultService.assertEquals(false, loginPage.isSubmitButtonEnabled(), "Checking that Login Page displayed");
			sleep(2);
			
			report.startStep("Log in as student");
			//pageHelper.setUserLoginToNull(UserID);
			loginPage.loginAsStudent(userFirstName,"12345");
			homePage.closeModalPopUp();
			homePage.waitHomePageloaded();
			
			report.startStep("Open assessments tab and verify test is available.");
			testsPage = homePage.openAssessmentsPage(false);
			testsPage.checkItemsCounterBySection("1", "1");
			//TestFullName = "Basic 1 Mid-Term Test";
			testsPage.checkTestDisplayedInSectionByTestNameAndCourseId(TestFullName, "1",courseId);
			sleep(2);
			
			report.startStep("Click Start Test Button");
			int testSequence = testsPage.getTestSequenceByCourseId(courseId);
			testsPage.clickStartTest(1, testSequence);
			webDriver.closeAlertByAccept(3);
			sleep(4);
			testEnvironmentPage.clickStartTest();
			sleep(5);
			
			report.startStep("Validate Timer is Correct");
			String expectedEdTimeFormat = testEnvironmentPage.convertTimeInMinutesToEDTimerFormat(courseTestLen-1);
			testEnvironmentPage.getAndValidateEdTestTimer(expectedEdTimeFormat);
			
			report.startStep("Close Assessments and logout");
			testEnvironmentPage.clickClose();
			sleep(2);
			homePage.clickOnLogOut();
			sleep(1);
			
		} finally {
			report.startStep("Mid-Term Test Removal");
			dbService.deleteExitTestForUserIdAndTestType(studentId, testTypeId);
			dbService.deleteUserIdByClassId(classId, studentId);
			pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(configuration.getProperty("schoolAdmin.user"),institutionId));
		}
	}
	
	@Test
	//@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "79488", "79489", "79490" })
	public void testAssignIncorrectInputValidations() throws Exception {
		
		report.startStep("Init Needed Vars");
		//instID = dbService.getInstituteIdByName(institutionName);
		studentId = pageHelper.createUSerUsingSP(institutionId, configuration.getProperty("classname.assessment"));
		String userFirstName = dbService.getUserFirstNameByUserId(studentId);
		String UserFullName = userFirstName + " " + dbService.getUserLastNameByUserId(studentId);
		String classId = dbService.getUserClassId(studentId);
		
		
		try {
				
			String className = dbService.getClassNameByClassId(classId);
			String courseName = coursesNames[1];
			String startTime = "00:00";
			String endTime = "23:59";
			
			report.startStep("Login as Admin");
			loginPage = new NewUXLoginPage(webDriver, testResultService);
			pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(configuration.getProperty("schoolAdmin.user"),institutionId));
			sleep(1);
			tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
			tmsHomePage.waitForPageToLoad();
			
			// Initialize tms assessments automated tests page
			TmsAssessmentsTestsAssignmentPage tmsAssessmentsAutomatedTestsPage = new TmsAssessmentsTestsAssignmentPage(webDriver, testResultService);
	
			////////////////////////////////////////////////////////////////////////
			///////  VALIDATE DATES ///////
			
			tmsAssessmentsAutomatedTestsPage.goToAssignTestPageAndChooseDetails("MT", className, UserFullName,null, false);
			
			report.startStep("Select Student from List");
			tmsAssessmentsAutomatedTestsPage.clickUserCheckbox(userFirstName);
			
			report.startStep("Set Test Level");
			tmsAssessmentsAutomatedTestsPage.selectTestLevel(courseName);
			
			report.startStep("TEST: Dates in The Past");
			
			report.startStep("Set Test Dates in The Past");
			String currentDate = pageHelper.getCurrentDateByFormat("yyyy-MM-dd HH:mm:ss.SS");
			String yesterdaysDate = textService.updateTime(currentDate,"reduce", "day",1);
			yesterdaysDate = textService.convertDateToDifferentFormat(yesterdaysDate, "yyyy-MM-dd HH:mm:ss.SS", "dd/MM/yyyy");
			tmsAssessmentsAutomatedTestsPage.setTime(yesterdaysDate, yesterdaysDate, startTime, endTime);
			
			report.startStep("Click Save");
			tmsHomePage.clickOnSave();
		
			report.startStep("Validate Error Messsage is Displayed");
			tmsAssessmentsAutomatedTestsPage.validateMessageContent("Warning: The selected dates are in the past.");
	
			report.startStep("TEST: Start Date in The Past");
			
			report.startStep("Select Start Test Date in The Past");
			currentDate = textService.convertDateToDifferentFormat(currentDate, "yyyy-MM-dd HH:mm:ss.SS", "dd/MM/yyyy");
			tmsAssessmentsAutomatedTestsPage.setTime(yesterdaysDate, currentDate, startTime, endTime);
	
			report.startStep("Click Save");
			tmsHomePage.clickOnSave();
			
			report.startStep("Validate Error Messsage is Displayed");
			tmsAssessmentsAutomatedTestsPage.validateMessageContent("Warning: The Start Date is in the past.");
			
			report.startStep("TEST: Start Date Later Than End Date");
			
			report.startStep("Select Start Test Date Later than End Date");
			tmsAssessmentsAutomatedTestsPage.setTime(currentDate, yesterdaysDate, startTime, endTime);
	
			report.startStep("Click Save");
			tmsHomePage.clickOnSave();
			
			report.startStep("Validate Error Messsage is Displayed");
			tmsAssessmentsAutomatedTestsPage.validateMessageContent("The Start Date cannot be later than the End Date.");
			
			report.startStep("TEST: Current Date With Time in The Past");
			
			report.startStep("Set Test Dates in The Past");
			currentDate = pageHelper.getCurrentDateByFormat("dd/MM/yyyy");
			tmsAssessmentsAutomatedTestsPage.setTime(currentDate, currentDate, startTime, "02:00");
			
			report.startStep("Click Save");
			tmsHomePage.clickOnSave();
		
			//report.startStep("Validate Error Messsage is Displayed");
			//tmsAssessmentsAutomatedTestsPage.validateMessageContent("Warning: The selected dates are in the past."); // this was the original warning but we decided to allow the save in order to unassign tests
	
			report.startStep("Validate Error Messsage is Displayed");
			tmsAssessmentsAutomatedTestsPage.validateMessageContent("You assigned " + coursesNames[1] + " Midterm Test to 1 student.");
			////////////////////////////////////////////////////////////////////////
			///////  VALIDATE STUDENT ///////
			
			report.startStep("TEST: Not Selecting Student");
			
			tmsAssessmentsAutomatedTestsPage.goToAssignTestPageAndChooseDetails("MT", className, UserFullName,null, true);
			
			report.startStep("Set Correct Time");
			tmsAssessmentsAutomatedTestsPage.setTime(currentDate, currentDate, startTime, endTime);
			
			report.startStep("Set Test Level");
			tmsAssessmentsAutomatedTestsPage.selectTestLevel(courseName);
			
			report.startStep("Click Save Without Selecting Student");
			tmsHomePage.clickOnSave();
			
			report.startStep("Validate Error Messsage is Displayed");
			//tmsAssessmentsAutomatedTestsPage.validateMessageContent("You have not selected any students.\nPlease select the student/students to whom you would like to assign this test.");
			tmsAssessmentsAutomatedTestsPage.validateMessageContent("You have not selected any students. \nPlease select the student/s to whom you would like to assign this test.");
			
			////////////////////////////////////////////////////////////////////////
			///////  VALIDATE COURSE ///////
			
			report.startStep("TEST: Not Selecting Course");
			
			tmsAssessmentsAutomatedTestsPage.goToAssignTestPageAndChooseDetails("MT", className, UserFullName,null, true);
	
			report.startStep("Set Correct Time");
			tmsAssessmentsAutomatedTestsPage.setTime(currentDate, currentDate, startTime, endTime);
			
			report.startStep("Select Student from List");
			tmsAssessmentsAutomatedTestsPage.clickUserCheckbox(userFirstName);
			
			report.startStep("Click Save Without Selecting Course");
			tmsHomePage.clickOnSave();
			
			report.startStep("Validate Error Messsage is Displayed");
			tmsAssessmentsAutomatedTestsPage.validateMessageContent("Please select a course.");
			
			////////////////////////////////////////////////////////////////////////
			
			//report.startStep("Logout");
			//tmsHomePage.clickOnExit();
			sleep(1);
		} finally {
			report.startStep("Mid-Term Test Removal");
			dbService.deleteExitTestForUserId(studentId);
			dbService.deleteUserIdByClassId(classId, studentId);
		}	
	}
	
	@Test
	//@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "79563" })
	public void testAssignMidTermConflictsOldTe() throws Exception {
		
		report.startStep("Init Needed Vars");
		studentId = pageHelper.createUSerUsingSP(institutionId, configuration.getProperty("classname.assessment"));
		List<String> usersIdWithSpecialStatus = new ArrayList<String>();
		String classId = dbService.getUserClassId(studentId);
		String courseName = coursesNames[1];
		String TestFullName = courseName + " " + pageHelper.getImplementationTypeName(2,"ED"); //"Basic 1 Mid-Term Test";
		
		try {
		
			String className = dbService.getClassNameByClassId(classId);
			//String courseName = coursesNames[1];
			String startTime = "00:00";
			String endTime = "23:59";
			String courseId = courses[1];
			int testTypeId = 2;
			
			String location;
			String actualScreen;
			String wantedScreen;
			boolean isMsgDisplayed;
			
			// Pre Conditions
			// have student with status done
			// have student with status expired
			// have student with status assigned to test that is different than mid term
			// have student with status unassigned
			// have student with status assigned
			
			pageHelper.createUSerUsingSP(institutionId, configuration.getProperty("classname.assessment"));
			//pageHelper.createUSerUsingSP(institutionId, configuration.getProperty("classname.assessment"));
			
			for (int i = 0; i < 3; i++) {
				report.startStep("Pre Condition - Create Students in Status In Progress");
				String userIdOfInProgress = pageHelper.createUSerUsingSP(institutionId, configuration.getProperty("classname.assessment"));
				
				// Assign B1 Test to student
				pageHelper.assignAutomatedTestToStudetInTestEnvironment(userIdOfInProgress, courseId, testTypeId,0,0,1);

				usersIdWithSpecialStatus.add(userIdOfInProgress);
			}
			
			report.startStep("Login as Admin");
			loginPage = new NewUXLoginPage(webDriver, testResultService);
			pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(configuration.getProperty("schoolAdmin.user"),institutionId));
			sleep(1);
			tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
			sleep(3);
			
			// Initialize tms assessments automated tests page
			TmsAssessmentsTestsAssignmentPage tmsAssessmentsAutomatedTestsPage = new TmsAssessmentsTestsAssignmentPage(webDriver, testResultService);
			
			/*report.startStep("Go to Assessments -> Automated Tests");
			tmsAssessmentsAutomatedTestsPage.goToAssessmentAutomatedTests();
			
			report.startStep("Select Feature & Class & User");	
			tmsAssessmentsAutomatedTestsPage.selectFeatureAndClass("MT", className);*/
			//tmsAssessmentsAutomatedTestsPage.selectFeatureAndClass("MT", "SchedulerClass2"); // temp until multi page is working
		
			///////////////////////////////////////////////////////////////////////////
			// select user with status expired
			
			report.startStep("TEST: Select User With Status 'Expired' And Check Conflicts");
			
			report.startStep("Create Student in Status Expired");
			String testId = pageHelper.getAssignedTestIdForStudent(usersIdWithSpecialStatus.get(0),courseId, testTypeId);
			String currentDate = pageHelper.getCurrentDateByFormat("yyyy-MM-dd HH:mm:ss");
			String twoDaysAgo = textService.updateTime(currentDate,"reduce", "day",2);
			String yesterday = textService.updateTime(currentDate,"reduce", "day",1);
			currentDate = textService.convertDateToDifferentFormat(currentDate, "yyyy-MM-dd HH:mm:ss", "dd/MM/yyyy");
			dbService.updateStartAndEndDateInUserExitTestSetting(usersIdWithSpecialStatus.get(0), testId, String.valueOf(testTypeId), twoDaysAgo, yesterday);

			tmsAssessmentsAutomatedTestsPage.goToAssignTestPageAndChooseFeatureAndClass("MT", className, false);
			sleep(2);
			
			// Assign test to student in status Expired
			String userId = tmsAssessmentsAutomatedTestsPage.assignTestToStudentInSpecificStatus("Expired", currentDate, currentDate, startTime, endTime, courseName);
	
			if (userId.equals("")) {
		    	testResultService.addFailTest("Student with Status Expired Doesn't Exist", false, true);
		    } else {
				report.startStep("Validate Conflicts Messsage is Displayed");
				isMsgDisplayed = tmsAssessmentsAutomatedTestsPage.validateNonPopupMessageContent("There are conflicts for 1 student. Would you like to check the conflicts ?");
				
				if (isMsgDisplayed) {
					report.startStep("Click Check conflicts Button");
					tmsAssessmentsAutomatedTestsPage.clickCheckConflictsButton();
					
					report.startStep("Validate Conflict Sign is Correct by Comparing Images");
					location = tmsAssessmentsAutomatedTestsPage.getLocationOfConflictByUser(userId);
					actualScreen = pageHelper.getPartialScreensShot(Integer.parseInt(location.split(",")[0]), Integer.parseInt(location.split(",")[1]), 20, 20);
					//String wantedScreen = "‪C:\\Users\\bodek1\\Desktop\\blueConflict.png";
					//wantedScreen = "C:\\Users\\bodek1\\Desktop\\ConflictsSigns\\blueConflict.png";
					wantedScreen = "files/tms/blueConflict.png";
					
					pageHelper.compareScreenShots(wantedScreen, actualScreen);
				       
				    report.startStep("Delete Screen Shot");
				    pageHelper.runConsoleProgram("cmd /c del " + actualScreen);
				    
				    report.startStep("Validate Conflicts Legend is Displayed");
				    tmsAssessmentsAutomatedTestsPage.validateConflictsLegendIsDisplayed();
				} else {
					testResultService.addFailTest("Conflict Message is Not Displayed", false, true);
				}
		    } 
			
			///////////////////////////////////////////////////////////////////////////
			// select user with status Incomplete
			
			report.startStep("TEST: Select User With Status 'Incomplete' And Check Conflicts");
			
			report.startStep("Create Student in Status Incomplete");
			testId = pageHelper.getAssignedTestIdForStudent(usersIdWithSpecialStatus.get(2),courseId, testTypeId);
			dbService.updateDidTest(usersIdWithSpecialStatus.get(2), testId, String.valueOf(testTypeId), "1");
			currentDate = pageHelper.getCurrentDateByFormat("yyyy-MM-dd HH:mm:ss");
			String threeDaysAgo = textService.updateTime(currentDate,"reduce", "day",3);
			//yesterday = textService.updateTime(currentDate,"reduce", "day",1);
			currentDate = textService.convertDateToDifferentFormat(currentDate, "yyyy-MM-dd HH:mm:ss", "dd/MM/yyyy");
			dbService.updateStartAndEndDateInUserExitTestSetting(usersIdWithSpecialStatus.get(2), testId, String.valueOf(testTypeId), threeDaysAgo, threeDaysAgo);
			dbService.insertRecordToUserExistTestSettingLog(usersIdWithSpecialStatus.get(2), courseId, testId, threeDaysAgo, String.valueOf(testTypeId), "100", "0", null);
			//String userStartedUTC = pageHelper.getCurrentDateByFormat("yyyy-MM-dd HH:mm:ss");
			//dbService.updateUserStartedUTC(userIdOfInProgress, testId, String.valueOf(testTypeId), userStartedUTC);
			
			//tmsAssessmentsAutomatedTestsPage.goToAssignTestPageAndChooseFeatureAndClass("MT", className, true);
			//tmsAssessmentsAutomatedTestsPage.selectFeatureAndClass("MT",className);
			clickGoInTms();
			
			// Assign test to student in status Incomplete
			userId = tmsAssessmentsAutomatedTestsPage.assignTestToStudentInSpecificStatus("Incomplete", currentDate, currentDate, startTime, endTime, courseName);
			
			if (userId.equals("")) {
				testResultService.addFailTest("Student with Status Incomplete Doesn't Exist", false, true);
			} else {
				report.startStep("Validate Conflicts Messsage is Displayed");
				isMsgDisplayed = tmsAssessmentsAutomatedTestsPage.validateNonPopupMessageContent("There are conflicts for 1 student. Would you like to check the conflicts ?");
				
				if (isMsgDisplayed) {
						report.startStep("Click Check conflicts Button");
						tmsAssessmentsAutomatedTestsPage.clickCheckConflictsButton();
						
						report.startStep("Validate Conflict Sign is Correct by Comparing Images");
						location = tmsAssessmentsAutomatedTestsPage.getLocationOfConflictByUser(userId);
						actualScreen = pageHelper.getPartialScreensShot(Integer.parseInt(location.split(",")[0]), Integer.parseInt(location.split(",")[1]), 20, 20);
						//String wantedScreen = "‪C:\\Users\\bodek1\\Desktop\\blueConflict.png";
						//wantedScreen = "C:\\Users\\bodek1\\Desktop\\ConflictsSigns\\blueConflict.png";
						wantedScreen = "files/tms/blueConflict.png";
						pageHelper.compareScreenShots(wantedScreen, actualScreen);
						
						report.startStep("Delete Screen Shot");
						pageHelper.runConsoleProgram("cmd /c del " + actualScreen);
						
						report.startStep("Validate Conflicts Legend is Displayed");
						tmsAssessmentsAutomatedTestsPage.validateConflictsLegendIsDisplayed();
				} else {
					testResultService.addFailTest("Conflict Message is Not Displayed", false, true);
				}
			}
		    
			///////////////////////////////////////////////////////////////////////////
	    	// select user with status done
			
			report.startStep("TEST: Select User With Status 'Done' And Check Conflicts");  
			
			report.startStep("Create Student in Status Done");
			testId = pageHelper.getAssignedTestIdForStudent(usersIdWithSpecialStatus.get(1),courseId, testTypeId);
			dbService.updateDidTest(usersIdWithSpecialStatus.get(1), testId, String.valueOf(testTypeId), "1");
			String userStartedUTC = pageHelper.getCurrentDateByFormat("yyyy-MM-dd HH:mm:ss");
			dbService.updateUserStartedUTC(usersIdWithSpecialStatus.get(1), testId, String.valueOf(testTypeId), userStartedUTC);
			currentDate = pageHelper.getCurrentDateByFormat("yyyy-MM-dd HH:mm:ss");
			dbService.insertRecordToUserExistTestSettingLog(usersIdWithSpecialStatus.get(1), courseId, testId, currentDate, String.valueOf(testTypeId), "100", "1", null);
			currentDate = textService.convertDateToDifferentFormat(currentDate, "yyyy-MM-dd HH:mm:ss", "dd/MM/yyyy");

			//tmsAssessmentsAutomatedTestsPage.goToAssignTestPageAndChooseFeatureAndClass("MT", className, true);
			clickGoInTms();
			
			// Assign Test to Student in Status Done
		    userId = tmsAssessmentsAutomatedTestsPage.assignTestToStudentInSpecificStatus("Done", currentDate, currentDate, startTime, endTime, courseName);
	
		    if (userId.equals("")) {
		    	testResultService.addFailTest("Student with Status Done Doesn't Exist", false, true);
		    } else {
		    
		 		report.startStep("Validate Conflicts Messsage is Displayed");
		 		isMsgDisplayed = tmsAssessmentsAutomatedTestsPage.validateNonPopupMessageContent("There are conflicts for 1 student. Would you like to check the conflicts ?");
				
				if (isMsgDisplayed) {
					report.startStep("Click Check conflicts Button");
					tmsAssessmentsAutomatedTestsPage.clickCheckConflictsButton();
					
					report.startStep("Validate Conflict Sign is Correct by Comparing Images");
					location = tmsAssessmentsAutomatedTestsPage.getLocationOfConflictByUser(userId);
					actualScreen = pageHelper.getPartialScreensShot(Integer.parseInt(location.split(",")[0]), Integer.parseInt(location.split(",")[1]), 20, 20);
					//wantedScreen = "C:\\Users\\bodek1\\Desktop\\ConflictsSigns\\redConflict.png"; 
					wantedScreen = "files/tms/redConflict.png";
					pageHelper.compareScreenShots(wantedScreen, actualScreen);
				       
					report.startStep("Delete Screen Shot");
					pageHelper.runConsoleProgram("cmd /c del " + actualScreen);
					
				    report.startStep("Validate Conflicts Legend is Displayed");
				    tmsAssessmentsAutomatedTestsPage.validateConflictsLegendIsDisplayed();
				} else {
					testResultService.addFailTest("Conflict Message is Not Displayed", false, true);
				}
		    }
		    
			///////////////////////////////////////////////////////////////////////////
			// select user that has another test assigned
			
			report.startStep("TEST: Select User That Has Another Test Assigned (Basic 2 Mid Term) And Check Conflicts");
			
			//tmsAssessmentsAutomatedTestsPage.goToAssignTestPageAndChooseFeatureAndClass("MT", className, true);
			clickGoInTms();
		    
		    // Assign Basic 2 Mid Term Test to Student 
		    String userIdWithAnotherAssignedTest = tmsAssessmentsAutomatedTestsPage.assignTestToStudentInSpecificStatus("Unassigned", currentDate, currentDate, startTime, endTime, coursesNames[2]);
		    webDriver.closeAlertByAccept();
		    
			String userNameWithAnotherAssignedTest = dbService.getUserNameById(userIdWithAnotherAssignedTest, configuration.getProperty("institution.id")); 
		    
		    report.startStep("Check User Checkbox");
			userId = tmsAssessmentsAutomatedTestsPage.clickUserCheckbox(userNameWithAnotherAssignedTest); 
			
			report.startStep("Set Correct Time");
			tmsAssessmentsAutomatedTestsPage.setTime(currentDate, currentDate, startTime, endTime);
			
			report.startStep("Set Test Level");
			tmsAssessmentsAutomatedTestsPage.selectTestLevel(courseName);
			
			report.startStep("Click Save");
			tmsHomePage.clickOnSave();
			
			if (userId.equals("")) {
				testResultService.addFailTest("Student with Another Test Assigned Doesn't Exist", false, true);
			} else {
			
				report.startStep("Validate Conflicts Messsage is Displayed");
				isMsgDisplayed = tmsAssessmentsAutomatedTestsPage.validateNonPopupMessageContent("There are conflicts for 1 student. Would you like to check the conflicts ?");
				
				if (isMsgDisplayed) {
					report.startStep("Click Check conflicts Button");
				
					tmsAssessmentsAutomatedTestsPage.clickCheckConflictsButton();
					
					report.startStep("Validate Conflict Sign is Correct by Comparing Images");
					location = tmsAssessmentsAutomatedTestsPage.getLocationOfConflictByUser(userId);
					actualScreen = pageHelper.getPartialScreensShot(Integer.parseInt(location.split(",")[0]), Integer.parseInt(location.split(",")[1]), 20, 20);
					//wantedScreen = "C:\\Users\\bodek1\\Desktop\\ConflictsSigns\\yellowConflict.png"; 
					wantedScreen = "files/tms/yellowConflict.png";
					pageHelper.compareScreenShots(wantedScreen, actualScreen);
					
					report.startStep("Delete Screen Shot");
					pageHelper.runConsoleProgram("cmd /c del " + actualScreen);
					
				    report.startStep("Validate Conflicts Legend is Displayed");
				    tmsAssessmentsAutomatedTestsPage.validateConflictsLegendIsDisplayed();
				} else {
					testResultService.addFailTest("Conflict Message is Not Displayed", false, true);
				}
			}
		  
			///////////////////////////////////////////////////////////////////////////
			// select user with status unassigned
			
			//tmsAssessmentsAutomatedTestsPage.goToAssignTestPageAndChooseFeatureAndClass("MT", className, true);
			clickGoInTms();
			
			report.startStep("TEST: Select user With Status Unassigned and Check no Message is Displayed");

			tmsAssessmentsAutomatedTestsPage.assignTestToStudentInSpecificStatus("Unassigned", currentDate, currentDate, startTime, endTime, courseName);
			
			report.startStep("Validate Success Messsage is Displayed");
			tmsAssessmentsAutomatedTestsPage.validateMessageContent("You assigned "+TestFullName +" to 1 student.\nThe test will be available from "+currentDate+" "+startTime+" to "+currentDate+" "+endTime+".");
		
			//report.startStep("Validate no Message is Displayed");
			//tmsAssessmentsAutomatedTestsPage.validateNonPopupMessageIsNotDisplayed();
			
			///////////////////////////////////////////////////////////////////////////
			// select user with status assigned
			
			//tmsAssessmentsAutomatedTestsPage.goToAssignTestPageAndChooseFeatureAndClass("MT", className, true);
			clickGoInTms();
			
			report.startStep("TEST: Select user With Status Assigned and Check no Message is Displayed");
			
			tmsAssessmentsAutomatedTestsPage.assignTestToStudentWithBasic1MTAssigned("Basic 1", currentDate, currentDate, startTime, endTime, courseName);
			
			report.startStep("Validate Success Messsage is Displayed");
			tmsAssessmentsAutomatedTestsPage.validateMessageContent("You assigned "+TestFullName +" to 1 student.\nThe test will be available from "+currentDate+" "+startTime+" to "+currentDate+" "+endTime+".");
		
			//report.startStep("Validate no Message is Displayed");
			//tmsAssessmentsAutomatedTestsPage.validateNonPopupMessageIsNotDisplayed();
				
			
			
			// validate conflicts:
			// ! (red) next to status done. text: "The student already did the test and have score. If the student will take the test again, current score will be deleted."
			// ! (blue) next to status expired. text:  "The student was already assigned to this test at the past, and didn’t take it. you can assign the test to the student for second chance to take the test".
			// ! (yellow) next to status is assigned to another test with different content. text: "The student is assigned to another test type of different course. By assigned the new test, the other test will be unassigned."
			// order is: red, yellow, blue, without alert
		} finally {
			/*report.startStep("Mid-Term Test Removal");
			dbService.deleteExitTestForUserId(UserID);	
			for (int i = 0; i < usersIdWithSpecialStatus.size(); i++) {
				dbService.deleteExitTestForUserId(usersIdWithSpecialStatus.get(i));
				dbService.deleteUserIdByClassId(classId, usersIdWithSpecialStatus.get(i));
			}*/
		}
	}
	
	@Test
	//@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "79563" })
	public void testAssignMidTermConflictsNewTe() throws Exception {

		report.startStep("Init Needed Vars");
		institutionName=institutionsName[1];
		pageHelper.restartBrowserInNewURL(institutionName,true);
		List<String> usersIdWithSpecialStatus = new ArrayList<String>();
		String className = configuration.getProperty("classname.CourseTest"); //dbService.getClassNameByClassId(classId);
		String classId = dbService.getUserClassId(studentId);
		String userIdOfUnAssign = pageHelper.createUSerUsingSP(institutionId, className);
		String courseName = coursesNames[1];		
	//courseName = coursesNames[1] + " V1"; //production while the flag is Off
		String TestFullName = courseName + " " + pageHelper.getImplementationTypeName(2,"ED"); //" Mid-Term Test";
		
		//report.startStep("Retrieve the URL");
		//String url = webDriver.getUrl();
		
		webDriver.closeBrowser();
		webDriver.init();
		webDriver.maximize();
		pageHelper.openCILatestUXLink();
		
	
		//report.startStep("Replace 'Automation' to 'Courses'");
		//url = url.replace("automation", "courses");
		
		//report.startStep("Change to Correct URL");
		//webDriver.openUrl(url);
		
		try {
		
			String startTime = "00:00";
			String endTime = "23:59";
			String courseId = courses[1];
	//courseId = "3"; //production while the flag is Off
			int testTypeId = 2;
			
			String location;
			String actualScreen;
			String wantedScreen;
			boolean isMsgDisplayed;
			
			// Pre Conditions
			// have student with status done
			// have student with status expired
			// have student with status assigned to test that is different than mid term
			// have student with status unassigned
			// have student with status assigned
			
			
			for (int i = 0; i < 3; i++) {
				report.startStep("Pre Condition - Create Students in Status In Progress");
				String userIdOfInProgress = pageHelper.createUSerUsingSP(institutionId, className);
				
				// Assign B1 Test to student
				pageHelper.assignAutomatedTestToStudetInTestEnvironment(userIdOfInProgress, courseId, testTypeId,0,0,1);

				usersIdWithSpecialStatus.add(userIdOfInProgress);
			}
			
			report.startStep("Login as Admin");
			loginPage = new NewUXLoginPage(webDriver, testResultService);
			pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(configuration.getProperty("schoolAdmin.user"),institutionId));
			sleep(1);
			tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
			sleep(3);
			
			// Initialize tms assessments automated tests page
			TmsAssessmentsTestsAssignmentPage tmsAssessmentsAutomatedTestsPage = new TmsAssessmentsTestsAssignmentPage(webDriver, testResultService);
			
			/*report.startStep("Go to Assessments -> Automated Tests");
			tmsAssessmentsAutomatedTestsPage.goToAssessmentAutomatedTests();
			
			report.startStep("Select Feature & Class & User");	
			tmsAssessmentsAutomatedTestsPage.selectFeatureAndClass("MT", className);*/
			//tmsAssessmentsAutomatedTestsPage.selectFeatureAndClass("MT", "SchedulerClass2"); // temp until multi page is working
		
			///////////////////////////////////////////////////////////////////////////
			// select user with status expired
			
			report.startStep("TEST: Select User With Status 'Expired' And Check Conflicts");
			
			report.startStep("Create Student in Status Expired");
			String testId = pageHelper.getAssignedTestIdForStudent(usersIdWithSpecialStatus.get(0),courseId, testTypeId);
			String currentDate = pageHelper.getCurrentDateByFormat("yyyy-MM-dd HH:mm:ss");
			String twoDaysAgo = textService.updateTime(currentDate,"reduce", "day",2);
			String yesterday = textService.updateTime(currentDate,"reduce", "day",1);
			currentDate = textService.convertDateToDifferentFormat(currentDate, "yyyy-MM-dd HH:mm:ss", "dd/MM/yyyy");
			dbService.updateStartAndEndDateInUserExitTestSetting(usersIdWithSpecialStatus.get(0), testId, String.valueOf(testTypeId), twoDaysAgo, yesterday);

			tmsAssessmentsAutomatedTestsPage.goToAssignTestPageAndChooseFeatureAndClass("MT", className, false);
			sleep(2);
			
			// Assign test to student in status Expired
			
			String userId = tmsAssessmentsAutomatedTestsPage.assignTestToStudentInSpecificStatus("Expired", currentDate, currentDate, startTime, endTime, courseName);
	
			if (userId.equals("")) {
		    	testResultService.addFailTest("Student with Status Expired Doesn't Exist", false, true);
		    } else {
				report.startStep("Validate Conflicts Messsage is Displayed");
				isMsgDisplayed = tmsAssessmentsAutomatedTestsPage.validateNonPopupMessageContent("There are conflicts for 1 student. Would you like to check the conflicts ?");
				
				if (isMsgDisplayed) {
					report.startStep("Click Check conflicts Button");
					tmsAssessmentsAutomatedTestsPage.clickCheckConflictsButton();
					
					report.startStep("Validate Conflict Sign is Correct by Comparing Images");
					//location = tmsAssessmentsAutomatedTestsPage.getLocationOfConflictByUser(userId);
					//actualScreen = pageHelper.getPartialScreensShot(Integer.parseInt(location.split(",")[0]), Integer.parseInt(location.split(",")[1]), 20, 20);
					//String wantedScreen = "‪C:\\Users\\bodek1\\Desktop\\blueConflict.png";
					//wantedScreen = "C:\\Users\\bodek1\\Desktop\\ConflictsSigns\\blueConflict.png";
					//wantedScreen = "files/tms/blueConflict.png";
					compareConflictColors(userId,"#003FE0");
								
					//pageHelper.compareScreenShots(wantedScreen, actualScreen);
				       
				    report.startStep("Delete Screen Shot");
				    //pageHelper.runConsoleProgram("cmd /c del " + actualScreen);
				    
				    report.startStep("Validate Conflicts Legend is Displayed");
				    tmsAssessmentsAutomatedTestsPage.validateConflictsLegendIsDisplayed();
				} else {
					testResultService.addFailTest("Conflict Message is Not Displayed", false, true);
				}
		    } 
			
			///////////////////////////////////////////////////////////////////////////
			// select user with status Incomplete
			
			report.startStep("TEST: Select User With Status 'Incomplete' And Check Conflicts");
			
			report.startStep("Create Student in Status Incomplete");
			testId = pageHelper.getAssignedTestIdForStudent(usersIdWithSpecialStatus.get(2),courseId, testTypeId);
			dbService.updateDidTest(usersIdWithSpecialStatus.get(2), testId, String.valueOf(testTypeId), "1");
			currentDate = pageHelper.getCurrentDateByFormat("yyyy-MM-dd HH:mm:ss");
			String threeDaysAgo = textService.updateTime(currentDate,"reduce", "day",3);
			//yesterday = textService.updateTime(currentDate,"reduce", "day",1);
			currentDate = textService.convertDateToDifferentFormat(currentDate, "yyyy-MM-dd HH:mm:ss", "dd/MM/yyyy");
			dbService.updateStartAndEndDateInUserExitTestSetting(usersIdWithSpecialStatus.get(2), testId, String.valueOf(testTypeId), threeDaysAgo, threeDaysAgo);
			dbService.insertRecordToUserExistTestSettingLog(usersIdWithSpecialStatus.get(2), courseId, testId, threeDaysAgo, String.valueOf(testTypeId), "100", "0", null);
			//String userStartedUTC = pageHelper.getCurrentDateByFormat("yyyy-MM-dd HH:mm:ss");
			//dbService.updateUserStartedUTC(userIdOfInProgress, testId, String.valueOf(testTypeId), userStartedUTC);
			
			//tmsAssessmentsAutomatedTestsPage.goToAssignTestPageAndChooseFeatureAndClass("MT", className, true);
			//tmsAssessmentsAutomatedTestsPage.selectFeatureAndClass("MT",className);
			clickGoInTms();
			
			// Assign test to student in status Incomplete
			userId = tmsAssessmentsAutomatedTestsPage.assignTestToStudentInSpecificStatus("Incomplete", currentDate, currentDate, startTime, endTime, courseName);
			
			if (userId.equals("")) {
				testResultService.addFailTest("Student with Status Incomplete Doesn't Exist", false, true);
			} else {
				report.startStep("Validate Conflicts Messsage is Displayed");
				isMsgDisplayed = tmsAssessmentsAutomatedTestsPage.validateNonPopupMessageContent("There are conflicts for 1 student. Would you like to check the conflicts ?");
				
				if (isMsgDisplayed) {
						report.startStep("Click Check conflicts Button");
						tmsAssessmentsAutomatedTestsPage.clickCheckConflictsButton();
						
						report.startStep("Validate Conflict Sign is Correct by Comparing Images");
						//location = tmsAssessmentsAutomatedTestsPage.getLocationOfConflictByUser(userId);
						//actualScreen = pageHelper.getPartialScreensShot(Integer.parseInt(location.split(",")[0]), Integer.parseInt(location.split(",")[1]), 20, 20);
						//String wantedScreen = "‪C:\\Users\\bodek1\\Desktop\\blueConflict.png";
						//wantedScreen = "C:\\Users\\bodek1\\Desktop\\ConflictsSigns\\blueConflict.png";
						//wantedScreen = "files/tms/blueConflict.png";
						compareConflictColors(userId,"#003FE0");
						//pageHelper.compareScreenShots(wantedScreen, actualScreen);
						
						report.startStep("Delete Screen Shot");
						//pageHelper.runConsoleProgram("cmd /c del " + actualScreen);
						
						report.startStep("Validate Conflicts Legend is Displayed");
						tmsAssessmentsAutomatedTestsPage.validateConflictsLegendIsDisplayed();
				} else {
					testResultService.addFailTest("Conflict Message is Not Displayed", false, true);
				}
			}
		    
			///////////////////////////////////////////////////////////////////////////
	    	// select user with status done
			
			report.startStep("TEST: Select User With Status 'Done' And Check Conflicts");  
			
			report.startStep("Create Student in Status Done");
			testId = pageHelper.getAssignedTestIdForStudent(usersIdWithSpecialStatus.get(1),courseId, testTypeId);
			dbService.updateDidTest(usersIdWithSpecialStatus.get(1), testId, String.valueOf(testTypeId), "1");
			String userStartedUTC = pageHelper.getCurrentDateByFormat("yyyy-MM-dd HH:mm:ss");
			dbService.updateUserStartedUTC(usersIdWithSpecialStatus.get(1), testId, String.valueOf(testTypeId), userStartedUTC);
			currentDate = pageHelper.getCurrentDateByFormat("yyyy-MM-dd HH:mm:ss");
			dbService.insertRecordToUserExistTestSettingLog(usersIdWithSpecialStatus.get(1), courseId, testId, currentDate, String.valueOf(testTypeId), "100", "1", null);
			currentDate = textService.convertDateToDifferentFormat(currentDate, "yyyy-MM-dd HH:mm:ss", "dd/MM/yyyy");

			//tmsAssessmentsAutomatedTestsPage.goToAssignTestPageAndChooseFeatureAndClass("MT", className, true);
			clickGoInTms();
			
			// Assign Test to Student in Status Done
		    userId = tmsAssessmentsAutomatedTestsPage.assignTestToStudentInSpecificStatus("Done", currentDate, currentDate, startTime, endTime, courseName);
	
		    if (userId.equals("")) {
		    	testResultService.addFailTest("Student with Status Done Doesn't Exist", false, true);
		    } else {
		    
		 		report.startStep("Validate Conflicts Messsage is Displayed");
		 		isMsgDisplayed = tmsAssessmentsAutomatedTestsPage.validateNonPopupMessageContent("There are conflicts for 1 student. Would you like to check the conflicts ?");
				
				if (isMsgDisplayed) {
					report.startStep("Click Check conflicts Button");
					tmsAssessmentsAutomatedTestsPage.clickCheckConflictsButton();
					
					report.startStep("Validate Conflict Sign is Correct by Comparing Images");
					//location = tmsAssessmentsAutomatedTestsPage.getLocationOfConflictByUser(userId);
					//actualScreen = pageHelper.getPartialScreensShot(Integer.parseInt(location.split(",")[0]), Integer.parseInt(location.split(",")[1]), 20, 20);
					//wantedScreen = "C:\\Users\\bodek1\\Desktop\\ConflictsSigns\\redConflict.png"; 
					//wantedScreen = "files/tms/redConflict.png";
					compareConflictColors(userId,"#D95353");
					//pageHelper.compareScreenShots(wantedScreen, actualScreen); // failure of comparing red round & white squere
				       
					report.startStep("Delete Screen Shot");
					//pageHelper.runConsoleProgram("cmd /c del " + actualScreen);
					
				    report.startStep("Validate Conflicts Legend is Displayed");
				    tmsAssessmentsAutomatedTestsPage.validateConflictsLegendIsDisplayed();
				} else {
					testResultService.addFailTest("Conflict Message is Not Displayed", false, true);
				}
		    }
		    
			///////////////////////////////////////////////////////////////////////////
			// select user that has another test assigned
			
			report.startStep("TEST: Select User That Has Another Test Assigned (Basic 2 Mid Term) And Check Conflicts");
			
			//tmsAssessmentsAutomatedTestsPage.goToAssignTestPageAndChooseFeatureAndClass("MT", className, true);
			clickGoInTms();
		    
		    // Assign Basic 2 Mid Term Test to Student 
		    String userIdWithAnotherAssignedTest = tmsAssessmentsAutomatedTestsPage.assignTestToStudentInSpecificStatus("Unassigned", currentDate, currentDate, startTime, endTime, coursesNames[2]);
		    webDriver.closeAlertByAccept();
		    
			String userNameWithAnotherAssignedTest = dbService.getUserNameById(userIdWithAnotherAssignedTest, institutionId);
		    
		    report.startStep("Check User Checkbox");
			userId = tmsAssessmentsAutomatedTestsPage.clickUserCheckbox(userNameWithAnotherAssignedTest); 
			
			report.startStep("Set Correct Time");
			tmsAssessmentsAutomatedTestsPage.setTime(currentDate, currentDate, startTime, endTime);
			
			report.startStep("Set Test Level");
			tmsAssessmentsAutomatedTestsPage.selectTestLevel(courseName);
			
			report.startStep("Click Save");
			tmsHomePage.clickOnSave();
			
			if (userId.equals("")) {
				testResultService.addFailTest("Student with Another Test Assigned Doesn't Exist", false, true);
			} else {
			
				report.startStep("Validate Conflicts Messsage is Displayed");
				isMsgDisplayed = tmsAssessmentsAutomatedTestsPage.validateNonPopupMessageContent("There are conflicts for 1 student. Would you like to check the conflicts ?");
				
				if (isMsgDisplayed) {
					report.startStep("Click Check conflicts Button");
				
					tmsAssessmentsAutomatedTestsPage.clickCheckConflictsButton();
					
					report.startStep("Validate Conflict Sign is Correct by Comparing Images");
					//location = tmsAssessmentsAutomatedTestsPage.getLocationOfConflictByUser(userId);
					//actualScreen = pageHelper.getPartialScreensShot(Integer.parseInt(location.split(",")[0]), Integer.parseInt(location.split(",")[1]), 20, 20);
					//wantedScreen = "C:\\Users\\bodek1\\Desktop\\ConflictsSigns\\yellowConflict.png"; 
					//wantedScreen = "files/tms/yellowConflict.png";
					compareConflictColors(userId,"#F8C734");
					//pageHelper.compareScreenShots(wantedScreen, actualScreen);
					
					report.startStep("Delete Screen Shot");
					//pageHelper.runConsoleProgram("cmd /c del " + actualScreen);
					
				    report.startStep("Validate Conflicts Legend is Displayed");
				    tmsAssessmentsAutomatedTestsPage.validateConflictsLegendIsDisplayed();
				} else {
					testResultService.addFailTest("Conflict Message is Not Displayed", false, true);
				}
			}
		  
			///////////////////////////////////////////////////////////////////////////
			// select user with status unassigned
			
			//tmsAssessmentsAutomatedTestsPage.goToAssignTestPageAndChooseFeatureAndClass("MT", className, true);
			clickGoInTms();
			
			report.startStep("TEST: Select user With Status Unassigned and Check no Message is Displayed");

			tmsAssessmentsAutomatedTestsPage.assignTestToStudentInSpecificStatus("Unassigned", currentDate, currentDate, startTime, endTime, courseName);
			
			report.startStep("Validate Success Messsage is Displayed");
			tmsAssessmentsAutomatedTestsPage.validateMessageContent("You assigned "+TestFullName +" to 1 student.\nThe test will be available from "+currentDate+" "+startTime+" to "+currentDate+" "+endTime+".");
		
			//report.startStep("Validate no Message is Displayed");
			//tmsAssessmentsAutomatedTestsPage.validateNonPopupMessageIsNotDisplayed();
			
			///////////////////////////////////////////////////////////////////////////
			// select user with status assigned
			
			//tmsAssessmentsAutomatedTestsPage.goToAssignTestPageAndChooseFeatureAndClass("MT", className, true);
			clickGoInTms();
			
			report.startStep("TEST: Select user With Status Assigned and Check no Message is Displayed");
			
			tmsAssessmentsAutomatedTestsPage.assignTestToStudentWithBasic1MTAssigned("Basic 1", currentDate, currentDate, startTime, endTime, courseName);
			
			report.startStep("Validate Success Messsage is Displayed");
			tmsAssessmentsAutomatedTestsPage.validateMessageContent("You assigned "+TestFullName +" to 1 student.\nThe test will be available from "+currentDate+" "+startTime+" to "+currentDate+" "+endTime+".");
		
			//report.startStep("Validate no Message is Displayed");
			//tmsAssessmentsAutomatedTestsPage.validateNonPopupMessageIsNotDisplayed();
				
			
			
			// validate conflicts:
			// ! (red) next to status done. text: "The student already did the test and have score. If the student will take the test again, current score will be deleted."
			// ! (blue) next to status expired. text:  "The student was already assigned to this test at the past, and didn’t take it. you can assign the test to the student for second chance to take the test".
			// ! (yellow) next to status is assigned to another test with different content. text: "The student is assigned to another test type of different course. By assigned the new test, the other test will be unassigned."
			// order is: red, yellow, blue, without alert
		} finally {
			/*report.startStep("Mid-Term Test Removal");
			dbService.deleteExitTestForUserId(UserID);	
			for (int i = 0; i < usersIdWithSpecialStatus.size(); i++) {
				dbService.deleteExitTestForUserId(usersIdWithSpecialStatus.get(i));
				dbService.deleteUserIdByClassId(classId, usersIdWithSpecialStatus.get(i));
				dbService.deleteUserIdByClassId(classId, userIdOfUnAssign);
			}*/
		}
	}
	
	private void compareConflictColors(String userId, String expectedColor) throws Exception {
		//*[@id="tr52332190009775"]/td[1]
		//*[@id="tr52332190009775"]
		webDriver.switchToFrame("tableFrame");
		String wantedEl = "'#tr"+userId+" td:first-child'";
		//WebElement wantedUser = webDriver.waitForElement("tr" +userId, ByTypes.id, false, 5);
		//WebElement beforeElement = webDriver.waitForElement("//*[@id='"+wantedEl+"']/td[1]", ByTypes.xpath, false, 5);
		JavascriptExecutor js = (JavascriptExecutor) webDriver.getWebDriver();
        //String content =  (String) js.executeScript("return window.getComputedStyle(arguments[0],'::before').getPropertyValue('background-color');",beforeElement.toString());
        String content =  (String) js.executeScript("return window.getComputedStyle(document.querySelector("+wantedEl+"),'::before').getPropertyValue('background-color')");
        String color = Color.fromString(content).asHex().toString();
        System.out.println(color);
        textService.assertEquals("", true, color.equalsIgnoreCase(expectedColor));
        webDriver.switchToTopMostFrame();
		tmsHomePage.switchToMainFrame();
        
        //System.out.println(Color.fromString(content).asRgba());
        
        //Color col = Color.fromString(content);
        //System.out.println(col.getColor().toString());
        //System.out.println(col.getColor());
        //col.getColor();
       
	}


	@Test 
	@TestCaseParams(testCaseID = { "79477" })
	public void testValidateInProgressStatusStudentsAreDisabled() throws Exception {
		
		report.startStep("Create Student");
		String userIdOfInProgress = pageHelper.createUSerUsingSP(institutionId, configuration.getProperty("classname.assessment"));
		
		report.startStep("Init Data");
		String classId = dbService.getUserClassId(userIdOfInProgress);
		String className = dbService.getClassNameByClassId(classId);
		String courseId = courses[1];
		int testTypeId = 2;
		
		try {
		
			report.startStep("Assign B1 Mid-Term Test to student and change status to Incomplete");
			pageHelper.assignAutomatedTestToStudetInTestEnvironment(userIdOfInProgress, courseId, testTypeId,0,0,1);
			String testId = pageHelper.getAssignedTestIdForStudent(userIdOfInProgress,courseId, testTypeId);
			dbService.updateDidTest(userIdOfInProgress, testId, String.valueOf(testTypeId), "1");
			String userStartedUTC = pageHelper.getCurrentDateByFormat("yyyy-MM-dd HH:mm:ss");
			dbService.updateUserStartedUTC(userIdOfInProgress, testId, String.valueOf(testTypeId), userStartedUTC);
			
			report.startStep("Login as Admin");
			loginPage = new NewUXLoginPage(webDriver, testResultService);
			pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(configuration.getProperty("schoolAdmin.user"),institutionId));
			sleep(1);
			tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
			sleep(3);
			
			// Initialize tms assessments automated tests page
			TmsAssessmentsTestsAssignmentPage tmsAssessmentsAutomatedTestsPage = new TmsAssessmentsTestsAssignmentPage(webDriver, testResultService);
			
			report.startStep("Go to Assessments -> Automated Tests");
			tmsAssessmentsAutomatedTestsPage.goToAssessmentTestsAssignment();
			
			// Go to Assign test and choose Feature, className, user
			tmsAssessmentsAutomatedTestsPage.selectFeatureAndClass("MT", className);
			
			report.startStep("Validate Students with Status In Progress Are Disabled"); 
			tmsAssessmentsAutomatedTestsPage.checkInProgressStudentAreDisabled();
			sleep(3);
			
			report.startStep("Click Select All in Scope and Validate In Progress Status Students are Unchecked");
			// click select all scope
			tmsAssessmentsAutomatedTestsPage.selectAllStudentsInScope();
			sleep(6);
			tmsAssessmentsAutomatedTestsPage.validateInProgressStudentAreNotSelected();
			sleep(2);
			
			report.startStep("Click Clear All in Scope");
			tmsAssessmentsAutomatedTestsPage.unselectAllStudentsInScope();
			sleep(2);
			
			report.startStep("Click Select All in Page and Validate In Progress Status Students are Unchecked");
			// click select all page
			tmsAssessmentsAutomatedTestsPage.selectAllStudentsInPage();
			tmsAssessmentsAutomatedTestsPage.validateInProgressStudentInCurrentPageAreNotSelected();
			
			report.startStep("Click Clear All in Page");
			tmsAssessmentsAutomatedTestsPage.unselectAllStudentsInPage();
			sleep(2);
			
			report.startStep("Check students with Status In Progress and Validate they are not selected");
			tmsAssessmentsAutomatedTestsPage.checkInProgressStudentCheckboxesAndValidateNotChecked();
			sleep(2);
			
		} finally {
			report.startStep("Mid-Term Test Removal");
			dbService.deleteExitTestForUserIdAndTestType(userIdOfInProgress, testTypeId);
			dbService.deleteUserIdByClassId(classId, userIdOfInProgress);
		}
	}
	
	@Test // feature is not entirely developed yet by development team
	@TestCaseParams(testCaseID = { "79557" })
	public void testSortingData() throws Exception {
		
		String className = configuration.getProperty("classname.assessment");
		
		report.startStep("Login as Admin");
		loginPage = new NewUXLoginPage(webDriver, testResultService);
		pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(configuration.getProperty("schoolAdmin.user"),institutionId));
		sleep(1);
		tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
		tmsHomePage.waitForPageToLoad();
		
		// Initialize tms assessments automated tests page
		TmsAssessmentsTestsAssignmentPage tmsAssessmentsAutomatedTestsPage = new TmsAssessmentsTestsAssignmentPage(webDriver, testResultService);
		
		report.startStep("Go to Assessments -> Automated Tests");
		tmsAssessmentsAutomatedTestsPage.goToAssessmentTestsAssignment();
		
		// Go to Assign test and choose Feature, className, user
		tmsAssessmentsAutomatedTestsPage.selectFeatureAndClass("MT", className);
		
		report.startStep("Validate List is Sorted by Status Desc (Default)");
		ArrayList<String> columnList = tmsAssessmentsAutomatedTestsPage.getListFromAssignCourseTestTableByCoulmn("status");
		pageHelper.validateListIsOrdered("desc", columnList);
		
		report.startStep("Click on First Name Column Header");
		tmsAssessmentsAutomatedTestsPage.clickOnColumnHeader("first name");
		
		report.startStep("Validate List is Sorted by First Name Asc");
		columnList = tmsAssessmentsAutomatedTestsPage.getListFromAssignCourseTestTableByCoulmn("first name");
		pageHelper.validateListIsOrdered("asc", columnList);
		
		report.startStep("Click on First Name Column Header Again");
		tmsAssessmentsAutomatedTestsPage.clickOnColumnHeader("first name");
		
		report.startStep("Validate List is Sorted by First Name Desc");
		columnList = tmsAssessmentsAutomatedTestsPage.getListFromAssignCourseTestTableByCoulmn("first name");
		pageHelper.validateListIsOrdered("desc", columnList);
		
		report.startStep("Click on Last Name Column Header");
		tmsAssessmentsAutomatedTestsPage.clickOnColumnHeader("last name");
		
		report.startStep("Validate List is Sorted by Last Name Asc");
		columnList = tmsAssessmentsAutomatedTestsPage.getListFromAssignCourseTestTableByCoulmn("last name");
		pageHelper.validateListIsOrdered("asc", columnList);
		
		report.startStep("Click on Last Name Column Header Again");
		tmsAssessmentsAutomatedTestsPage.clickOnColumnHeader("last name");
		
		report.startStep("Validate List is Sorted by Last Name Desc");
		columnList = tmsAssessmentsAutomatedTestsPage.getListFromAssignCourseTestTableByCoulmn("last name");
		pageHelper.validateListIsOrdered("desc", columnList);
		
		report.startStep("Click on User Name Column Header");
		tmsAssessmentsAutomatedTestsPage.clickOnColumnHeader("user name");
		
		report.startStep("Validate List is Sorted by User Name Asc");
		columnList = tmsAssessmentsAutomatedTestsPage.getListFromAssignCourseTestTableByCoulmn("user name");
		pageHelper.validateListIsOrdered("asc", columnList);
		
		report.startStep("Click on User Name Column Header Again");
		tmsAssessmentsAutomatedTestsPage.clickOnColumnHeader("user name");
		
		report.startStep("Validate List is Sorted by User Name Desc");
		columnList = tmsAssessmentsAutomatedTestsPage.getListFromAssignCourseTestTableByCoulmn("user name");
		pageHelper.validateListIsOrdered("desc", columnList);
		
		report.startStep("Click on Test Name Column Header");
		tmsAssessmentsAutomatedTestsPage.clickOnColumnHeader("test name");
		
		report.startStep("Validate List is Sorted by Test Name Asc");
		columnList = tmsAssessmentsAutomatedTestsPage.getListFromAssignCourseTestTableByCoulmn("test name");
		pageHelper.validateListIsOrdered("asc", columnList);
		
		report.startStep("Click on Test Name Column Header Again");
		tmsAssessmentsAutomatedTestsPage.clickOnColumnHeader("test name");
		
		report.startStep("Validate List is Sorted by Test Name Desc");
		columnList = tmsAssessmentsAutomatedTestsPage.getListFromAssignCourseTestTableByCoulmn("test name");
		pageHelper.validateListIsOrdered("desc", columnList);
		
		/*report.startStep("Click on Start Date Column Header");
		tmsAssessmentsAutomatedTestsPage.clickOnColumnHeader("start date");
		
		report.startStep("Validate List is Sorted by Start Date Asc");
		columnList = tmsAssessmentsAutomatedTestsPage.getListFromTableByCoulmn("start date");
		columnList = (ArrayList<String>) pageHelper.getDatesOnlyFromDateTimeList(columnList);
		pageHelper.validateListIsOrdered("asc", columnList);
		//pageHelper.validateDatesListIsOrdered("asc", columnList);
		
		report.startStep("Click on Start Date Column Header Again");
		tmsAssessmentsAutomatedTestsPage.clickOnColumnHeader("start date");
		
		report.startStep("Validate List is Sorted by Start Date Desc");
		columnList = tmsAssessmentsAutomatedTestsPage.getListFromTableByCoulmn("start date");
		columnList = (ArrayList<String>) pageHelper.getDatesOnlyFromDateTimeList(columnList);
		pageHelper.validateListIsOrdered("desc", columnList);
		//pageHelper.validateDatesListIsOrdered("desc", columnList);
		
		report.startStep("Click on End Date Column Header");
		tmsAssessmentsAutomatedTestsPage.clickOnColumnHeader("end date");
		
		report.startStep("Validate List is Sorted by End Date Asc");
		columnList = tmsAssessmentsAutomatedTestsPage.getListFromTableByCoulmn("end date");
		columnList = (ArrayList<String>) pageHelper.getDatesOnlyFromDateTimeList(columnList);
		pageHelper.validateListIsOrdered("asc", columnList);
		//pageHelper.validateDatesListIsOrdered("asc", columnList);
		
		report.startStep("Click on End Date Column Header Again");
		tmsAssessmentsAutomatedTestsPage.clickOnColumnHeader("end date");
		
		report.startStep("Validate List is Sorted by End Date Desc");
		columnList = tmsAssessmentsAutomatedTestsPage.getListFromTableByCoulmn("end date");
		columnList = (ArrayList<String>) pageHelper.getDatesOnlyFromDateTimeList(columnList);
		pageHelper.validateListIsOrdered("desc", columnList);
		//pageHelper.validateDatesListIsOrdered("desc", columnList);*/
		
		report.startStep("Click on Status Column Header");
		tmsAssessmentsAutomatedTestsPage.clickOnColumnHeader("status");
		
		report.startStep("Validate List is Sorted by Status Asc");
		columnList = tmsAssessmentsAutomatedTestsPage.getListFromAssignCourseTestTableByCoulmn("status");
		pageHelper.validateListIsOrdered("asc", columnList);
		
		report.startStep("Click on Status Column Header Again");
		tmsAssessmentsAutomatedTestsPage.clickOnColumnHeader("status");
		
		report.startStep("Validate List is Sorted by Status Desc");
		columnList = tmsAssessmentsAutomatedTestsPage.getListFromAssignCourseTestTableByCoulmn("status");
		pageHelper.validateListIsOrdered("desc", columnList);
		
	}
	
	@Test
	@Category(ReleaseTests.class)
	@TestCaseParams(testCaseID = { "78672" })
	public void testAssignPltNewTE() throws Exception {
		
	//	String userID = null;
		String classId = null;
		try {
		
			//String className = configuration.getProperty("classname.CourseTest");
			String className = "assessmentClass";
			report.startStep("Change to Courses Instituion");
			institutionName=institutionsName[1];
			pageHelper.restartBrowserInNewURL(institutionName, true);
		
			report.startStep("Init Needed Vars");
			studentId = pageHelper.createUSerUsingSP(institutionId, className);
			String userFirstName = dbService.getUserNameById(studentId, institutionId);
			classId = dbService.getClassIdByName(className, institutionId);
			
			String groupName = "";
			String[]groupInfo =	dbService.getGroupNameAndGroupIdOfSpecificClass(classId);
			String groupId = groupInfo[0];
			pageHelper.assignStudentToGroup(classId,studentId,groupInfo[0]);
			groupName = groupInfo[2];
			
			NewUXLoginPage loginPage = new NewUXLoginPage(webDriver, testResultService);
			
			report.startStep("Login as Admin");
			pageHelper.setUserLoginToNull(studentId);
			tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
			sleep(3);
			TmsAssessmentsTestsAssignmentPage tmsAssessmentsTestsAssignmentPage = new TmsAssessmentsTestsAssignmentPage(webDriver, testResultService);
		
			report.startStep("Go to Tests Assignemnt Page");
			tmsAssessmentsTestsAssignmentPage.goToAssessmentTestsAssignment();
			
			report.startStep("Choose PLT option and Click Go");
			tmsAssessmentsTestsAssignmentPage.selectFeatureAndGo("PL");
			sleep(2);
			
			report.startStep("Select Class");
			tmsAssessmentsTestsAssignmentPage.selectPltClass(className);
			sleep(1);
			
			report.startStep("Select Group");
			tmsAssessmentsTestsAssignmentPage.selectPltGroup(groupName);
			sleep(1);
			
			report.startStep("Select Student");
			tmsAssessmentsTestsAssignmentPage.selectPltStudent(userFirstName);	
			
			report.startStep("Validate Test Length against DB");
			//tmsAssessmentsAutomatedTestsPage.validateTestLength(institutionName);
			tmsAssessmentsTestsAssignmentPage.validateTestLengthTestEnvironment(testTypesId[0]);
			
			report.startStep("Edit the Test len to 50 minutes");
			tmsAssessmentsTestsAssignmentPage.setCourseTestdurationInMinutes("50");
			
			report.startStep("Click Save");
			tmsHomePage.clickOnSave();
			webDriver.closeAlertByAccept();
			
			testEnvironmentPage = new TestEnvironmentPage(webDriver, testResultService);
			report.startStep("Validate PLT Assignment Appears in DB");
			List<String[]> userAdministrationRecords = dbService.getUserTestAdministrationsByStudentId(studentId);
			String[] UserAdministration = testEnvironmentPage.getUserAdministrationRecordsByCourseId(userAdministrationRecords,userAdministrationRecords.get(0)[2]);
			String userTestAdministrationId = UserAdministration[0]; //dbService.getUserTestAdministrationByStudentId(userID);
			testResultService.assertEquals(false, userTestAdministrationId == null, "PLT Assignment Doesn't appear in DB");
			
			report.startStep("Log out of TMS");
			tmsHomePage.clickOnExit();
			sleep(2);
			testResultService.assertEquals(false, loginPage.isSubmitButtonEnabled(), "Checking that Login Page displayed");
			
			report.startStep("Log in as student");
			pageHelper.setUserLoginToNull(studentId);
			loginPage.loginAsStudent(userFirstName,"12345");
			homePage.closeAllNotifications();
			homePage.closeModalPopUp();
			homePage.waitHomePageloaded();
			
			report.startStep("Open assessments tab and verify test is available.");
			testsPage = homePage.openAssessmentsPage(false);
			//sleep(3);
			testsPage.checkItemsCounterBySection("1", "1");
			String TestFullName = testTypes[0]; //"Placement Test"; 
			testsPage.checkTestDisplayedInSectionByTestName(TestFullName, "1", "1");

			report.startStep("Click on the Start test on Assessment");
			//List<String[]> userAdministrationRecords = dbService.getUserTestAdministrationsByStudentId(userID);
			//String[] UserAdministration = testEnvironmentPage.getUserAdministrationRecordsByCourseId(userAdministrationRecords,userAdministrationRecords.get(0)[2]);
			int courseTestLen = Integer.parseInt(UserAdministration[3]);
			
			int testSequence = testsPage.getTestSequenceByCourseId(UserAdministration[1]);
			testsPage.clickStartTest(1, testSequence);
			sleep(2);
			testEnvironmentPage = new TestEnvironmentPage(webDriver, testResultService);
		  
			report.startStep("Choose PLT Level then click on Start Test Button");
		    testEnvironmentPage.choosePltLevel("Basic");
		    testEnvironmentPage.pressOnStartTest();
		   
			String expectedEdTimeFormat = testEnvironmentPage.convertTimeInMinutesToEDTimerFormat(courseTestLen-1); //reduce one minute for the first seconds that the timer start to run count down 
			
			report.startStep("Validate the Timer");
			sleep(2);
			testEnvironmentPage.getAndValidateEdTestTimer(expectedEdTimeFormat);
			
			report.startStep("Close and logout");
			testEnvironmentPage.clickClose();
			sleep(2);
			homePage.clickOnLogOut();
			sleep(1);
		} finally {
			report.startStep("Student Removal");
			//dbService.deleteExitTestForUserId(UserID);	
			dbService.deleteUserIdByClassId(classId, studentId);
		}
	
	}


	// need to complete this test once product is developed and debug
	@Test
	//@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "78672" })
	public void testPltValidationsNewTE() throws Exception {
		
		try {
			
			report.startStep("Change to Courses Instituion");
			institutionName=institutionsName[1];
			pageHelper.restartBrowserInNewURL(institutionName, true);
			
			report.startStep("Init Data");
			String className = configuration.getProperty("classname.CourseTest");
			String firstUserID = pageHelper.createUSerUsingSP(institutionId, className);
			String firstUserName = dbService.getUserNameById(firstUserID, institutionId);
			String startLevelName = PLTStartLevel.Basic.toString();
			
			String secondUserID = pageHelper.createUSerUsingSP(institutionId, className);
			String secondUserName = dbService.getUserNameById(secondUserID, institutionId);
			loginPage = new NewUXLoginPage(webDriver, testResultService);
			
			report.startStep("Login as Admin");
			tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
			sleep(3);
		
			report.startStep("Go to Tests Configuration Page");
			TmsAssessmentsTestsConfigurationPage tmsAssessmentsTestsConfigurationPage = new TmsAssessmentsTestsConfigurationPage(webDriver, testResultService);
			tmsAssessmentsTestsConfigurationPage.goToAssessmentTestsConfiguration();
			
			report.startStep("Select PLT and Click Go");
			tmsAssessmentsTestsConfigurationPage.selectPltAndGo();
			
			report.startStep("Click Take Test Once");
			tmsAssessmentsTestsConfigurationPage.clickTakeOnce();
			
			report.startStep("Click Save");
			tmsHomePage.clickOnSave();
			
			TmsAssessmentsTestsAssignmentPage tmsAssessmentsTestsAssignmentPage = new TmsAssessmentsTestsAssignmentPage(webDriver, testResultService);
			
			report.startStep("Go to Tests Assignemnt Page");
			tmsAssessmentsTestsAssignmentPage.goToAssessmentTestsAssignment();
			
			report.startStep("Choose PLT option and Click Go");
			tmsAssessmentsTestsAssignmentPage.selectFeatureAndGo("PL");
			sleep(2);
			
			report.startStep("Select Class");
			tmsAssessmentsTestsAssignmentPage.selectPltClass(className);
			sleep(3);
			
			report.startStep("Select Student");
			tmsAssessmentsTestsAssignmentPage.selectPltStudent(firstUserName);
			sleep(3);
			
			report.startStep("Click Save");
			tmsHomePage.clickOnSave();
			
			report.startStep("Validate Successfull Assignment Message is Displayed");
			testResultService.assertEquals(true, webDriver.getPopUpText().equals("Placement Test was assigned successfully."),"Assignment Message is Incorrect.");
			webDriver.closeAlertByAccept();
			
			report.startStep("Logout as Admin");
			tmsHomePage.clickOnExit();
			
			report.startStep("Log in as student");
			pageHelper.setUserLoginToNull(firstUserID);
			loginPage.loginAsStudent(firstUserName,"12345");
			loginPage.waitLoginAfterRestartAppPool();
			pageHelper.skipOptin();
			homePage.closeAllNotifications();
			pageHelper.skipOnBoardingHP();
			homePage.waitHomePageloadedFully();
			
			report.startStep("Open assessments tab and verify test is available.");
			testsPage = homePage.openAssessmentsPage(false);
			sleep(3);
			testsPage.clickStartTest(1, 1);
			sleep(2);
			
			// Initialize Test Environment Page
			TestEnvironmentPage testEnvironmentPage = new TestEnvironmentPage(webDriver, testResultService);
			sleep(3);
			
			report.startStep("Choose Level");
			String levelSymbol = testEnvironmentPage.choosePlacementStatusNewTe(startLevelName);
			String levelCode = testEnvironmentPage.convertPltLevelStringToLevelCode(levelSymbol);
			
			report.startStep("Click on Start Test Button");
			testEnvironmentPage.pressOnStartTest();
			
			report.startStep("Click Exit Button");
			testEnvironmentPage.clickExitPlt();
			
			report.startStep("Update isCompleted to 1 in DB");
			String firstUserTestAdministrationId = dbService.getUserTestAdministrationByStudentId(firstUserID);
			dbService.updateCompletedInUserTestProgress(firstUserTestAdministrationId, "1");
			dbService.updateSubmitReasonInUserTestProgress(firstUserTestAdministrationId, "1");
			dbService.updateLearningStartLevelInUserTestProgress(firstUserTestAdministrationId, levelCode);
			
			report.startStep("Logout as Student");
			homePage.clickOnLogOut();
	
			report.startStep("Login as Admin");
			tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
			
			report.startStep("Go to Tests Assignemnt Page");
			tmsAssessmentsTestsAssignmentPage.goToAssessmentTestsAssignment();
			
			report.startStep("Choose PLT option and Click Go");
			tmsAssessmentsTestsAssignmentPage.selectFeatureAndGo("PL");
			sleep(2);
			
			report.startStep("Select Class");
			tmsAssessmentsTestsAssignmentPage.selectPltClass(className);
			sleep(3);
			
			report.startStep("Select Student");
			tmsAssessmentsTestsAssignmentPage.selectPltStudent(firstUserName);	
			
			report.startStep("Click Save");
			tmsHomePage.clickOnSave();
			
			report.startStep("Validate Message of One Assignment is Displayed");
			testResultService.assertEquals(true, webDriver.getPopUpText().equals("The selected students already took the test and won't be allowed to take the test again."),"Take Once Error Message is Incorrect.");
			//tmsAssessmentsTestsAssignmentPage.validateMessageContent("Note that some of the students already took the test and cannot be assigned to the test again");
			webDriver.closeAlertByAccept();
			
			report.startStep("Logout as Admin");
			tmsHomePage.clickOnExit();
			
			report.startStep("Log in as student");
			pageHelper.setUserLoginToNull(firstUserID);
			loginPage.loginAsStudent(firstUserName,"12345");
			homePage.closeModalPopUp();
			homePage.waitHomePageloaded();
			
			report.startStep("Open assessments tab and verify test is available.");
			testsPage = homePage.openAssessmentsPage(false);
			sleep(3);
			testsPage.checkIfTestIsDisplayedInAvailableTests(testTypes[0]);
			testsPage.clickStartTest(1, 1);
			sleep(2);
			
			report.startStep("Validate Error Message (test was already taken) is Displayed");
			testResultService.assertEquals(true, webDriver.getPopUpText().equals("You cannot take the placement test at this time. If you would like to be assigned a placement test, please contact your program coordinator."),"Take Once Error Message is Incorrect.");
			webDriver.closeAlertByAccept();
			
			report.startStep("Close Assessments Page");
			testsPage.close();
			
			report.startStep("Logout as Student");
			homePage.clickOnLogOut();
	
			report.startStep("Login as Admin");
			tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
			
			report.startStep("Go to Configuration Page");
			tmsAssessmentsTestsConfigurationPage.goToAssessmentTestsConfiguration();
			
			report.startStep("Select PLT and Click Go");
			tmsAssessmentsTestsConfigurationPage.selectPltAndGo();
			
			report.startStep("Uncheck Take Test Once button");
			tmsAssessmentsTestsConfigurationPage.uncheckTakeOnce();
			
			report.startStep("Click Save");
			tmsHomePage.clickOnSave();
			
			report.startStep("Go to Tests Assignemnt Page");
			tmsAssessmentsTestsAssignmentPage.goToAssessmentTestsAssignment();
			
			report.startStep("Choose PLT option and Click Go");
			tmsAssessmentsTestsAssignmentPage.selectFeatureAndGo("PL");
			sleep(2);
			
			report.startStep("Select Class");
			tmsAssessmentsTestsAssignmentPage.selectPltClass(className);
			sleep(3);
			
			report.startStep("Select Student from List");
			tmsAssessmentsTestsAssignmentPage.selectPltStudent(secondUserName);
			
			report.startStep("Click Save");
			tmsHomePage.clickOnSave();
			
			report.startStep("Validate Successfull Assignment Message is Displayed");
			testResultService.assertEquals(true, webDriver.getPopUpText().equals("Placement Test was assigned successfully."),"Assignment Message is Incorrect.");
			webDriver.closeAlertByAccept();
			
			report.startStep("Logout as Admin");
			tmsHomePage.clickOnExit();
			
			report.startStep("Log in as student");
			pageHelper.setUserLoginToNull(secondUserID);
			loginPage.loginAsStudent(secondUserName,"12345");
			homePage.closeModalPopUp();
			homePage.waitHomePageloaded();
			
			report.startStep("Open assessments tab and verify test is available.");
			testsPage = homePage.openAssessmentsPage(false);
			sleep(3);
			testsPage.clickStartTest(1, 1);
			sleep(3);
					
			report.startStep("Choose Level");
			levelSymbol = testEnvironmentPage.choosePlacementStatusNewTe(startLevelName);
			levelCode = testEnvironmentPage.convertPltLevelStringToLevelCode(levelSymbol);
			
			report.startStep("Click on Start Test Button");
			testEnvironmentPage.pressOnStartTest();
			
			report.startStep("Click Close Button");
			testEnvironmentPage.clickClose();
			
			report.startStep("Update isCompleted to 1 in DB");
			String secondUserTestAdministrationId = dbService.getUserTestAdministrationByStudentId(secondUserID);			
			dbService.updateCompletedInUserTestProgress(secondUserTestAdministrationId, "1");
			dbService.updateSubmitReasonInUserTestProgress(secondUserTestAdministrationId, "1");
			dbService.updateLearningStartLevelInUserTestProgress(secondUserTestAdministrationId, levelCode);
			
			report.startStep("Logout as Student");
			homePage.clickOnLogOut();
	
			report.startStep("Login as Admin");
			tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
			
			report.startStep("Go to Tests Assignemnt Page");
			tmsAssessmentsTestsAssignmentPage.goToAssessmentTestsAssignment();
			
			report.startStep("Choose PLT option and Click Go");
			tmsAssessmentsTestsAssignmentPage.selectFeatureAndGo("PL");
			sleep(2);
			
			report.startStep("Select Class");
			tmsAssessmentsTestsAssignmentPage.selectPltClass(className);
			sleep(3);
			
			report.startStep("Select Student");
			tmsAssessmentsTestsAssignmentPage.selectPltStudent(secondUserName);	
			
			report.startStep("Click Save");
			tmsHomePage.clickOnSave();
			
			report.startStep("Validate Successfull Assignment Message is Displayed");
			testResultService.assertEquals(true, webDriver.getPopUpText().equals("Placement Test was assigned successfully."),"Assignment Message is Incorrect.");
			webDriver.closeAlertByAccept();
			
			report.startStep("Logout as Admin");
			tmsHomePage.clickOnExit();
			
			report.startStep("Log in as student");
			pageHelper.setUserLoginToNull(secondUserID);
			loginPage.loginAsStudent(secondUserName,"12345");
			homePage.closeModalPopUp();
			homePage.waitHomePageloaded();
			
			report.startStep("Open assessments tab and verify test is available.");
			testsPage = homePage.openAssessmentsPage(false);
			sleep(3);
			testsPage.checkIfTestIsDisplayedInAvailableTests(testTypes[0]);
			testsPage.clickStartTest(1, 1);
			sleep(2);
			
			report.startStep("Validate Error Message (test was already taken) is Displayed");
			testEnvironmentPage.validateTestName(testTypes[0]);
			
			report.startStep("Click Exit Button");
			testEnvironmentPage.clickExitPlt();
			
			report.startStep("Logout as Student");
			homePage.clickOnLogOut();
	
		} catch (Exception e) {
			
		} finally {
			report.startStep("Change to Courses Instituion");
			institutionName=institutionsName[1];
			pageHelper.restartBrowserInNewURL(institutionName, true); 
		
			report.startStep("Login as Admin");
			NewUXLoginPage loginPage = new NewUXLoginPage(webDriver, testResultService);
			//pageHelper.setUserLoginToNull(userID);
			tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
			
			report.startStep("Go to Tests Configuration Page");
			TmsAssessmentsTestsConfigurationPage tmsAssessmentsTestsConfigurationPage = new TmsAssessmentsTestsConfigurationPage(webDriver, testResultService);
			tmsAssessmentsTestsConfigurationPage.goToAssessmentTestsConfiguration();
			
			report.startStep("Select PLT and Click Go");
			tmsAssessmentsTestsConfigurationPage.selectPltAndGo();
			
			report.startStep("Uncheck Take Test Once button");
			tmsAssessmentsTestsConfigurationPage.uncheckTakeOnce();
			
			report.startStep("Click Save");
			tmsHomePage.clickOnSave();
			
			report.startStep("Log Out");
			tmsHomePage.clickOnExit();
			sleep(2);
	
		}
				
	}
	
	@Test
	//@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "" })
	public void testTimeConfigurationMidTerm() throws Exception {
		
		String userID = null;
		//String classId = null;
		String defaultTime = dbService.getTestLengthByTestTypeId(testTypesId[1]);
		String defaultDays = "";
		
		try {
		
			String className = configuration.getProperty("classname.CourseTest");
			
			report.startStep("Change to Courses Instituion");
			institutionName=institutionsName[1];
			pageHelper.restartBrowserInNewURL(institutionName, true); 
		
			report.startStep("Init Needed Vars");
			studentId = pageHelper.createUSerUsingSP(institutionId, className);
			//classId = dbService.getClassIdByName(className, instID);
			String newDuration = "40";
			String newDays = "5";
			String UserID = pageHelper.createUSerUsingSP(institutionId, className);
			String userFirstName = dbService.getUserFirstNameByUserId(UserID);
			String courseName = coursesNames[1];
			String TestFullName = courseName + " " + pageHelper.getImplementationTypeName(2,"ED");//courseName + " " + testTypes[1] + " Test";
			String currentDate = pageHelper.getCurrentDateByFormat("dd/MM/yyyy");
			String courseId = courses[1];
			defaultDays = Integer.toString((pageHelper.getEndDateOffSetOfInstitution(institutionId)));
			
			NewUXLoginPage loginPage = new NewUXLoginPage(webDriver, testResultService);
			
			report.startStep("Login as Admin");
			pageHelper.setUserLoginToNull(userID);
			tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
			sleep(3);
			TmsAssessmentsTestsAssignmentPage tmsAssessmentsTestsAssignmentPage = new TmsAssessmentsTestsAssignmentPage(webDriver, testResultService);
		
			report.startStep("Go to Tests Assignemnt Page");
			tmsAssessmentsTestsAssignmentPage.goToAssessmentTestsAssignment();
			
			report.startStep("Choose Mid Term option and Click Go");
			tmsAssessmentsTestsAssignmentPage.selectFeatureAndClass("MT", className);
			sleep(2);
			
			report.startStep("Validate Test Length in Assignment Page match Test Duration in DB Table TestTypeSettings");
			tmsAssessmentsTestsAssignmentPage.validateTestLengthTestEnvironment(testTypesId[1]); 
			
			report.startStep("Go to Tests Configuration Page");
			TmsAssessmentsTestsConfigurationPage tmsAssessmentsTestsConfigurationPage = new TmsAssessmentsTestsConfigurationPage(webDriver, testResultService);
			tmsAssessmentsTestsConfigurationPage.goToAssessmentTestsConfiguration();
			
			report.startStep("Select Mid Term and Final Test, and Click GO");
			tmsAssessmentsTestsConfigurationPage.selectMidTermAndFinalAndGo();
			
			report.startStep("Update Mid Term Test Time");
			tmsAssessmentsTestsConfigurationPage.updateMidtermTestDuration(newDuration);
			
			report.startStep("Validate Days Against DB");
			tmsAssessmentsTestsConfigurationPage.validateDays(defaultDays);

			report.startStep("Update Days");
			tmsAssessmentsTestsConfigurationPage.updateDays(newDays);
			
			report.startStep("Click Save");
			tmsHomePage.clickOnSave();
			
			report.startStep("Validate New Test Duration Stored Correctly in DB (InstitutionTestSettings Table)");
			String testDurationByInstDB = dbService.getTestLengthByInstitution(BasicNewUxTest.institutionId, testTypesId[1]);
			testResultService.assertEquals(newDuration, testDurationByInstDB, "Test Duration in DB Table: InstitutionTestSettings is Incorrect");
			
			report.startStep("Validate New Days Stored Correctly in DB (InstitutionTestSettings Table)");
			String daysByInstDB = dbService.getDaysByInstitution(BasicNewUxTest.institutionId, testTypesId[1]);
			testResultService.assertEquals(newDays, daysByInstDB, "Days in DB Table: InstitutionTestSettings is Incorrect");
			
			report.startStep("Go to Tests Assignemnt Page");
			tmsAssessmentsTestsAssignmentPage.goToAssessmentTestsAssignment();
			
			report.startStep("Choose Mid Term option and Click Go");
			tmsAssessmentsTestsAssignmentPage.selectFeatureAndClass("MT", className);
			sleep(2);
			
			report.startStep("Validate Test Length in Assignment Page is the New Duration");
			tmsAssessmentsTestsAssignmentPage.validateTestLengthTestEnvironmentNotDefault(testTypesId[1], newDuration); 
			
			report.startStep("Select Student from List");
			tmsAssessmentsTestsAssignmentPage.clickUserCheckbox(userFirstName);
			
			report.startStep("Set Test Level");
			tmsAssessmentsTestsAssignmentPage.selectTestLevel(courseName);
		
			// Initialize Test Environment Page
			testEnvironmentPage = new TestEnvironmentPage(webDriver, testResultService);
			
			report.startStep("Click Save");
			tmsHomePage.clickOnSave();
			
			// Retrieve assignment data 
			String startTime = "00:00";
			String endTime = "00:00";
			currentDate = pageHelper.getCurrentDateByFormat("yyyy-MM-dd HH:mm:ss.SS");			
			String endDate = textService.updateTime(currentDate,"add", "day",Integer.parseInt(newDays));
			endDate = textService.convertDateToDifferentFormat(endDate, "yyyy-MM-dd HH:mm:ss.SS", "dd/MM/yyyy");
			currentDate = textService.convertDateToDifferentFormat(currentDate, "yyyy-MM-dd HH:mm:ss.SS", "dd/MM/yyyy");
			
			report.startStep("Validate Messsage is Displayed");
			tmsAssessmentsTestsAssignmentPage.validateMessageContent("You assigned "+TestFullName +" to 1 student.\nThe test will be available from "+currentDate+" "+startTime+" to "+endDate+" "+endTime+".");
			sleep(3); 
			
			report.startStep("Validate New Test Duration Stored Correctly in DB (UserExitTestSettings Table)");
			String testId = pageHelper.getAssignedTestIdForStudent(UserID, courseId, 2);
			String userExitSettingsId = dbService.getUserExitSettingsId(UserID, testId, courseId, "2");
			String durationInExitTestSettings = dbService.getTestDurationByUserExitSettingsId(userExitSettingsId);
			testResultService.assertEquals(newDuration, durationInExitTestSettings, "Test Duration in DB Table: UserExitTestSettings is Incorrect");
			
			report.startStep("Try To assign Test in a period of time that is Bigger than Days in Settings");
			currentDate = pageHelper.getCurrentDateByFormat("yyyy-MM-dd HH:mm:ss.SS");			
			endDate = textService.updateTime(currentDate, "add", "day",Integer.parseInt(newDays)+1);
			endDate = textService.convertDateToDifferentFormat(endDate, "yyyy-MM-dd HH:mm:ss.SS", "dd/MM/yyyy");
			currentDate = textService.convertDateToDifferentFormat(currentDate, "yyyy-MM-dd HH:mm:ss.SS", "dd/MM/yyyy");
			tmsAssessmentsTestsAssignmentPage.setTime(currentDate, endDate, startTime, endTime);
			
			report.startStep("Select Student from List");
			tmsAssessmentsTestsAssignmentPage.clickUserCheckbox(userFirstName);
			
			report.startStep("Click Save");
			tmsHomePage.clickOnSave();
			
			report.startStep("Validate Messsage is Displayed");
			tmsAssessmentsTestsAssignmentPage.validateMessageContent("The assignment time is greater than the maximum settings  (maximum:"+newDays+" days)");
			sleep(3); 
			
			report.startStep("Log Out");
			tmsHomePage.clickOnExit();
			sleep(2);
			
			report.startStep("Log in as student");
			loginPage.loginAsStudent(userFirstName,"12345");
			homePage.closeModalPopUp();
			homePage.waitHomePageloaded();
			
			report.startStep("Open assessments tab and verify test is available.");
			testsPage = homePage.openAssessmentsPage(false);
			testsPage.checkItemsCounterBySection("1", "1");
			testsPage.checkTestDisplayedInSectionByTestNameAndCourseId(TestFullName, "1", courseId);
			sleep(2);
			
			report.startStep("Click Start Test Button");
			int testSequence = testsPage.getTestSequenceByCourseId(courseId);
			testsPage.clickStartTest(1, testSequence);
			webDriver.closeAlertByAccept(3);
			sleep(4);
			testEnvironmentPage.clickStartTest();
			sleep(5);
			
			report.startStep("Validate Timer is Correct");
			String expectedEdTimeFormat = testEnvironmentPage.convertTimeInMinutesToEDTimerFormat(Integer.parseInt(newDuration)-1);
			testEnvironmentPage.getAndValidateEdTestTimer(expectedEdTimeFormat);
			
			report.startStep("Close Assessments and logout");
			testEnvironmentPage.clickClose();
			sleep(2);
			homePage.clickOnLogOut();
			sleep(1);
			
		} catch (Exception e) {
			
		} finally {
			report.startStep("Change to Courses Instituion");
			institutionName=institutionsName[1];
			pageHelper.restartBrowserInNewURL(institutionName, true); 
		
			report.startStep("Login as Admin");
			NewUXLoginPage loginPage = new NewUXLoginPage(webDriver, testResultService);
			pageHelper.setUserLoginToNull(userID);
			tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
			
			report.startStep("Go to Tests Configuration Page");
			TmsAssessmentsTestsConfigurationPage tmsAssessmentsTestsConfigurationPage = new TmsAssessmentsTestsConfigurationPage(webDriver, testResultService);
			tmsAssessmentsTestsConfigurationPage.goToAssessmentTestsConfiguration();
			
			report.startStep("Select Mid Term and Final Test, and Click GO");
			tmsAssessmentsTestsConfigurationPage.selectMidTermAndFinalAndGo();
			
			report.startStep("Update Mid Term Test Time");
			tmsAssessmentsTestsConfigurationPage.updateMidtermTestDuration(defaultTime);
			
			report.startStep("Update Mid Term Test Days");
			tmsAssessmentsTestsConfigurationPage.updateDays(defaultDays);
			
			report.startStep("Click Save");
			tmsHomePage.clickOnSave();
			
			report.startStep("Log Out");
			tmsHomePage.clickOnExit();
			sleep(2);
	
		}
	}
	
	@Test
	@TestCaseParams(testCaseID = { "79355", "79477" })
	public void testAssignMidTermTestOldTE() throws Exception {
		
		//institutionName=institutionsName[1];//
		
		//report.startStep("Change to Courses Instituion");
		//pageHelper.restartBrowserInNewURL(institutionName, true); //
		
		report.startStep("Init Needed Vars");
		String className = configuration.getProperty("classname");
				//String UserID = pageHelper.createUSerUsingSP(configuration.getProperty("institution.id"), configuration.getProperty("classname.assessment"));
		String UserID = pageHelper.createUSerUsingSP(institutionId, className);
		int testTypeId = 2;
		String userFirstName = dbService.getUserFirstNameByUserId(UserID);
		String classId = dbService.getUserClassId(UserID);
		//String className = configuration.getProperty("classname.assessment"); //dbService.getClassNameByClassId(classId);
		String courseName = coursesNames[1];
		String TestFullName = courseName + " " + pageHelper.getImplementationTypeName(2,"ED");//courseName + " " + testTypes[1] + " Test";
		String courseId = courses[1];
		int newDuration = 50;
		
		report.startStep("Pre Condition = Create Students in Status Incomplete");
		String userIdOfInProgress = pageHelper.createUSerUsingSP(institutionId, className);
		
		// Assign B1 Mid-Term Test to student
		pageHelper.assignAutomatedTestToStudetInTestEnvironment(userIdOfInProgress, courseId, testTypeId,0,0,1);
		String[] testAdministration = pageHelper.getAssignedCourseTestIdForStudent(userIdOfInProgress,courseId, testTypeId);
		String testId = testAdministration[3];
			
		// Change status to incomplete
		dbService.updateDidTest(userIdOfInProgress, testId, String.valueOf(testTypeId), "1");
		String userStartedUTC = pageHelper.getCurrentDateByFormat("yyyy-MM-dd HH:mm:ss");
		dbService.updateUserStartedUTC(userIdOfInProgress, testId, String.valueOf(testTypeId), userStartedUTC);
		
		try {
			
			report.startStep("Login as Admin");
			loginPage = new NewUXLoginPage(webDriver, testResultService);
			pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(configuration.getProperty("schoolAdmin.user"),configuration.getProperty("institution.id")));
			sleep(1);
			tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
			
			// Initialize tms assessments automated tests page
			TmsAssessmentsTestsAssignmentPage tmsAssessmentsTestsAssignmentPage = new TmsAssessmentsTestsAssignmentPage(webDriver, testResultService);
			
			report.startStep("Go to Assessments -> Test Assignment");
			tmsAssessmentsTestsAssignmentPage.goToAssessmentTestsAssignment();
			
			// Go to Assign test and choose Feature, className, user
			tmsAssessmentsTestsAssignmentPage.selectFeatureAndClass("MT", className);

			report.startStep("Validate All Students are Unchecked"); 
			tmsAssessmentsTestsAssignmentPage.validateAllStudentsCheckboxesAreUnchecked();
			sleep(2);
			
			report.startStep("Validate Students with Status In Progress Are Disabled"); 
			tmsAssessmentsTestsAssignmentPage.checkInProgressStudentAreDisabled();
			sleep(2);
			
			report.startStep("Get Expected Start Date (Current Date) and End Date (By Institution Settings)");
			String currentDate = pageHelper.getCurrentDateByFormat("yyyy-MM-dd HH:mm:ss.SS");
			
			int daysInInstSettings = pageHelper.getEndDateOffSetOfInstitution(configuration.getProperty("institution.id"));
			
			String expectedEndDate = textService.updateTime(currentDate,"add", "day",daysInInstSettings);
			expectedEndDate = textService.convertDateToDifferentFormat(expectedEndDate, "yyyy-MM-dd HH:mm:ss.SS", "dd/MM/yyyy");
			currentDate = textService.convertDateToDifferentFormat(currentDate, "yyyy-MM-dd HH:mm:ss.SS", "dd/MM/yyyy");
			
			report.startStep("Validate Start And End Date Are Correct");
			tmsAssessmentsTestsAssignmentPage.validateStartAndEndDateAreCorrect(currentDate, expectedEndDate);
			
			report.startStep("Validate Start And End Time Are Correct");
			tmsAssessmentsTestsAssignmentPage.validateStartAndEndTimeAreCorrect("00", "00", "00", "00");
			
			report.startStep("Validate No Course is Chosen");
			tmsAssessmentsTestsAssignmentPage.validateNoCourseIsChosen();
			sleep(3);
			
			report.startStep("Select Student from List");
			tmsAssessmentsTestsAssignmentPage.clickUserCheckbox(userFirstName);
			
			report.startStep("Set Test Level");
			tmsAssessmentsTestsAssignmentPage.selectTestLevel(courseName);
		
			report.startStep("Set Test Time");
			String startTime = "00:00";
			String endTime = "23:59";
			tmsAssessmentsTestsAssignmentPage.setTime(currentDate, currentDate, startTime, endTime);
			
			report.startStep("Validate Test Length against DB");
			//tmsAssessmentsTestsAssignmentPage.validateOldTMsTestLength(institutionName);
			tmsAssessmentsTestsAssignmentPage.validateTestLengthTestEnvironment(testTypesId[1]); // relevant for new te- US: set Default Time //
			
			report.startStep("Edit the Test len to 50 minutes");
			tmsAssessmentsTestsAssignmentPage.setOldTMSCourseTestdurationInMinutes(Integer.toString(newDuration));
			
			// Initialize Test Environment Page
			testEnvironmentPage = new TestEnvironmentPage(webDriver, testResultService);
			
			report.startStep("Click Save");
			tmsHomePage.clickOnSave();
			
			report.startStep("Validate Messsage is Displayed");
			tmsAssessmentsTestsAssignmentPage.validateMessageContent("You assigned "+TestFullName +" to 1 student.\nThe test will be available from "+currentDate+" "+startTime+" to "+currentDate+" "+endTime+".");
			sleep(3); 
			
			currentDate = textService.convertDateToDifferentFormat(currentDate, "dd/MM/yyyy", "MM/dd/yyyy"); 
			if (currentDate.startsWith("0")) {
				currentDate = currentDate.substring(1);
			}
			currentDate = currentDate.replaceAll("/0", "/");
			
			report.startStep("Validate Mid Assignment Appears in DB");
			testAdministration = pageHelper.getAssignedCourseTestIdForStudent(UserID,courseId, testTypeId);
			int courseTestLen = Integer.parseInt(testAdministration[3]);
		
			report.startStep("Validate Stored Token Is Correct");
			List<String[]> userTestSettingsDetails = dbService.getUserExitTestSettings(UserID, testTypeId);
			String tokenFromDB = userTestSettingsDetails.get(0)[2];
			testResultService.assertEquals(false, tokenFromDB.equals(null),"Stored Token Is Incorrect.");
			
			report.startStep("Validate Status in TMS");
			String testID = userTestSettingsDetails.get(0)[1];
			String TestFullNameTms = dbService.getCourseNameById(testID);
			tmsAssessmentsTestsAssignmentPage.validateTestIsAssignedInTMS(userFirstName, TestFullNameTms, currentDate, currentDate);
			
			report.startStep("Select Student Again and Click Save Again and Validate New Token is Generated");
			tmsAssessmentsTestsAssignmentPage.clickUserCheckbox(userFirstName);
			tmsHomePage.clickOnSave();
			webDriver.closeAlertByAccept();
			userTestSettingsDetails = dbService.getUserExitTestSettings(UserID, testTypeId);
			String secondTokenFromDB = userTestSettingsDetails.get(0)[2];
			testResultService.assertEquals(false, tokenFromDB.equals(secondTokenFromDB), "Same token generated for different assignments.");
			sleep(2);
			
			report.startStep("Log out of TMS");
			tmsHomePage.clickOnExit();
			sleep(2);
			testResultService.assertEquals(false, loginPage.isSubmitButtonEnabled(), "Checking that Login Page displayed");
			sleep(2);
			
			report.startStep("Log in as student");
			loginPage.loginAsStudent(userFirstName,"12345");
			homePage.closeModalPopUp();
			homePage.skipNotificationWindow();
			homePage.waitHomePageloaded();
			
			report.startStep("Open assessments tab and verify test is available.");
			testsPage = homePage.openAssessmentsPage(false);
			
			report.startStep("Start Test from Assessments");
			testsPage.clickStartTest(1, 2);
			webDriver.closeAlertByAccept();
			sleep(2);
			webDriver.switchToNewWindow();
			Thread.sleep(5000);
			webDriver.switchToTopMostFrame();
			webDriver.switchToFrame(webDriver.waitForElement("oed__iframe", ByTypes.className,false,webDriver.getTimeout()));
			
			report.startStep("Validate Timer");
			NewUxClassicTestPage classicTest = new NewUxClassicTestPage(webDriver, testResultService);
			classicTest.validateTimer(newDuration);
			
			report.startStep("Close Test");
			classicTest.close();
			
			report.startStep("Log Out");
			homePage.logOutOfED();
		
			
			
			
			
			
		
		} finally {
			report.startStep("Mid-Term Test Removal");
			dbService.deleteExitTestForUserIdAndTestType(UserID, testTypeId);
			dbService.deleteUserIdByClassId(classId, UserID);
			pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(configuration.getProperty("schoolAdmin.user"),institutionId));
		}
	}


	@Test
	@Category({SanityTests.class,ReleaseTests.class})
	@TestCaseParams(testCaseID = { "" })
	public void testAssignMidtermTestAndVerifyOn_ED_newTE() throws Exception {
		
	report.startStep("Change to Courses Instituion");
		institutionName=institutionsName[1]; 
		pageHelper.restartBrowserInNewURL(institutionName, true);
		sleep(2);
		
	report.startStep("Init Needed Vars");
		String className = configuration.getProperty("classname.CourseTest");
		//String className = configuration.getProperty("classname");
		studentId = pageHelper.createUSerUsingSP(institutionId, className);
		List<String[]> user = dbService.getUserNameAndPasswordByUserId(studentId);
		userName = user.get(0)[0];
		String password = user.get(0)[1];
		String userFirsName = user.get(0)[3];
		int testTypeId = 2; // 2 it's midterm 3 it's final
		String courseName =  coursesNames[1];
		String TestFullName = "Basic 1 Midterm Test";//"Basic 1 Mid-Term Test";
		String courseId = getCourseIdByCourseCode(courseCodes[1]);
		classId = dbService.getClassIdByName(className, institutionId);

	try {
		report.startStep("Login as Admin");
		loginPage = new NewUXLoginPage(webDriver, testResultService);
		//pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(configuration.getProperty("schoolAdmin.user"),configuration.getProperty("institution.id")));
		sleep(1);
		tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
		homePage.closeAllNotifications();
		
		// Initialize tms assessments automated tests page
		TmsAssessmentsTestsAssignmentPage tmsAssessmentsTestsAssignmentPage = new TmsAssessmentsTestsAssignmentPage(webDriver, testResultService);
		
		report.startStep("Go to Assessments -> Test Assignment");
		tmsAssessmentsTestsAssignmentPage.goToAssessmentTestsAssignment();
		
		// Go to Assign test and choose Feature, className, user
		//tmsAssessmentsTestsAssignmentPage.selectFeatureAndClass("MT", className);
		report.startStep("Go to Assign test and choose Feature, className, user:" + userName);
		tmsAssessmentsTestsAssignmentPage.selectFeatureClassAndUser("MT", className, null, null);
	//	webDriver.switchToFrame("tableFrame");
	
		tmsHomePage.switchToFormFrame();
		tmsHomePage.clickOnSearch();
		tmsHomePage.findStudentInSearchSection(className, userName);
		//report.startStep("Select Student from List");
		tmsHomePage.switchToMainFrame();
		tmsHomePage.switchToFormFrame();
		tmsHomePage.clickOnGo();
		Thread.sleep(2000);
		webDriver.switchToTopMostFrame();
		tmsHomePage.switchToMainFrame();
		sleep(2);
		tmsAssessmentsTestsAssignmentPage.selectUserCheckbox();
		webDriver.switchToTopMostFrame();
		tmsHomePage.switchToMainFrame();
		sleep(2);
		tmsAssessmentsTestsAssignmentPage.selectTestLevel(courseName);
		sleep(2);
		report.startStep("Click Save");
		tmsHomePage.clickOnSave();
		sleep(2);
		webDriver.closeAlertByAccept();
		sleep(2);
	
		report.startStep("Log out of TMS");
			tmsHomePage.clickOnExit();
			sleep(3);
			//testResultService.assertEquals(false, loginPage.isSubmitButtonEnabled(), "Checking that Login Page displayed");
			
		report.startStep("Log in as student");
			//pageHelper.setUserLoginToNull(UserID);
			loginPage.loginAsStudent(userName,password);
			pageHelper.skipOptin();
			homePage.closeAllNotifications();
			//homePage.waitHomePageloadedFully();
		//	homePage.skipNotificationWindow();
		//	pageHelper.skipOnBoardingHP();
		//	homePage.closeModalPopUp();
			homePage.waitHomePageloadedFully();
			
		report.startStep("Open assessments tab and verify test is available.");
			testsPage = homePage.openAssessmentsPage(false);
			testsPage.checkItemsCounterBySection("1", "1");// Available 
			testsPage.checkTestDisplayedInSectionByTestName(TestFullName, "1", "1");
			int testSequence = testsPage.getTestSequenceByCourseId(courseId);
			String testName = testsPage.clickStartTest(1, testSequence);
			webDriver.closeAlertByAccept();
			sleep(3);
			
		report.startStep("Validate Test Name");
			TestEnvironmentPage testEnvironmentPage = new TestEnvironmentPage(webDriver, testResultService);
			testEnvironmentPage.validateTestName(testName);
			sleep(2);
			report.startStep("Validate Lesson Intro");
		//	validateIntro(jsonObj, localizationJson, lessonsIds.get(j), testName);
			testEnvironmentPage.clickStartTest();
			sleep(2);
			testEnvironmentPage.clickNext();
			sleep(2);
			report.startStep("Click Close Test");
			testEnvironmentPage.clickClose();
			
			report.startStep("Open Assessments Page");
			testsPage = homePage.openAssessmentsPage(false);
		    sleep(3);
			report.startStep("Validate Resume Button Appears and Click it");
			ArrayList<WebElement> startTestButtons = (ArrayList<WebElement>) webDriver.getWebDriver().findElements(By.xpath("//td[contains(@class,\"assessments__testlink\")]/a"));
			testResultService.assertEquals(true, startTestButtons.get(0).getText().contains("Resume Test"),"Resume Test Button is not Displayed.");
			testsPage.close();
			sleep(1);
			homePage.clickOnLogOut();
			//testsPage.validateButtonTextAndClickIt(1, "Resume Test");
			//testEnvironmentPage.clickNext();
		
	} finally {
		report.startStep("Mid-Term Test Removal");
		dbService.deleteExitTestForUserIdAndTestType(studentId, testTypeId);
		dbService.deleteUserIdByClassId(classId, studentId);
		pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(configuration.getProperty("schoolAdmin.user"),institutionId));
	}
	}


	@Test
	@Category(ReleaseTests.class)
	@TestCaseParams(testCaseID = { "" })
	public void testAssignMidtermTestAndVerifyOn_ED_OLD_TE() throws Exception {
		
	report.startStep("Init Needed Vars");
		//List<String[]> classes = dbService.getClassesNameByInstitutionId(institutionId);//.getProperty("classname");
		String className = configuration.getProperty("classname.assessment");
		//studentId = pageHelper.createUSerUsingSP(institutionId, className);
		String[] user = createUserAndReturnDetails(institutionId,className);
		userName = user[0];
		String password = user[1];
		studentId = user[2];
		String userFirsName = dbService.getUserFirstNameByUserId(studentId);
		//List<String[]> user_firstName = dbService.getUserNameAndPasswordByUserId(studentId);
		//String userFirsName = user_firstName[3];
		int testTypeId = 2;
		String classId = "";// = dbService.getUserClassId(UserID);
		sleep(1);
		String courseName = coursesNames[1];
		String TestFullName = courseName + " " + pageHelper.getImplementationTypeName(2,"ED");//courseName + " " + testTypes[1] + " Test";
		String courseId = courses[1];
	
		try {
			report.startStep("Login as Admin");
				loginPage = new NewUXLoginPage(webDriver, testResultService);
				//pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(configuration.getProperty("schoolAdmin.user"),configuration.getProperty("institution.id")));
				sleep(2);
				tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
				homePage.closeAllNotifications();
			// Initialize tms assessments automated tests page
			TmsAssessmentsTestsAssignmentPage tmsAssessmentsTestsAssignmentPage = new TmsAssessmentsTestsAssignmentPage(webDriver, testResultService);
			
			report.startStep("Go to Assessments -> Test Assignment");
				tmsAssessmentsTestsAssignmentPage.goToAssessmentTestsAssignment();
				
				// Go to Assign test and choose Feature, className, user
				//tmsAssessmentsTestsAssignmentPage.selectFeatureAndClass("MT", className);
				tmsAssessmentsTestsAssignmentPage.selectFeatureClassAndUser("MT", className, userFirsName+" "+userFirsName,null);
			//	webDriver.switchToFrame("tableFrame");
			
				//report.startStep("Select Student from List");
			tmsAssessmentsTestsAssignmentPage.selectUserCheckbox();
			webDriver.switchToTopMostFrame();
			tmsHomePage.switchToMainFrame();
			sleep(2);
			tmsAssessmentsTestsAssignmentPage.selectTestLevel(courseName);
			sleep(2);
			report.startStep("Click Save");
			tmsHomePage.clickOnSave();
			sleep(2);
			webDriver.closeAlertByAccept();
			sleep(2);
		
			report.startStep("Log out of TMS");
				tmsHomePage.clickOnExit();
				sleep(3);
				//testResultService.assertEquals(false, loginPage.isSubmitButtonEnabled(), "Checking that Login Page displayed");
				
			report.startStep("Log in as student");
				//pageHelper.setUserLoginToNull(UserID);
				webDriver.switchToMainWindow();
				loginPage.loginAsStudent(userName,password);
				pageHelper.skipOptin();
			//	homePage.skipNotificationWindow();
			//	pageHelper.skipOnBoardingHP();
			//	homePage.closeModalPopUp();
				sleep(20);
				
			report.startStep("Open assessments tab and verify test is available.");
				testsPage = homePage.openAssessmentsPage(false);
				testsPage.checkItemsCounterBySection("1", "2");// Available 
				testsPage.checkTestDisplayedInSectionByTestName(TestFullName, "1", "2");
				int testSequence = testsPage.getTestSequenceByCourseId(courseId);
				String testName = testsPage.clickStartTest(1, testSequence);
				webDriver.closeAlertByAccept();
				sleep(3);
				webDriver.switchToNewWindow();
				Thread.sleep(5000);
				//webDriver.switchToTopMostFrame();
				//webDriver.switchToFrame(webDriver.waitForElement("oed__iframe", ByTypes.className,false,webDriver.getTimeout()));
				
			report.startStep("Validate Timer");
				NewUxClassicTestPage classicTest = new NewUxClassicTestPage(webDriver, testResultService);
				classicTest.switchToTestAreaIframe();
				WebElement timer = webDriver.waitForElement("remTime", ByTypes.id);
				String time = timer.getText();
				String actualTime = classicTest.convertTimeInMinutesToEDTimerFormatOldTe(60);
				testResultService.assertEquals(actualTime+"0", time);
				//classicTest.validateTimer(60);
				//TestFullName
			
			report.startStep("Validate title");
				classicTest.verifyMidtermTitleOldTE(TestFullName);
				classicTest.pressOnStartTest();
				
			report.startStep("Close Test");
				classicTest.close();
				
			report.startStep("Log Out");
				homePage.logOutOfED();
				//classicTest.switchToTestAreaIframe();
			report.startStep("Retrieve classId in order to clean test-data");
				classId = dbService.getUserClassId(studentId);

		} finally {
			
			report.startStep("Mid-Term Test Removal");
				dbService.deleteExitTestForUserIdAndTestType(studentId, testTypeId);
				dbService.deleteUserIdByClassId(classId, studentId);
				pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(configuration.getProperty("schoolAdmin.user"),institutionId));
		}
	}
	
	@Test
	//@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "" })
	public void testTmsAdminCreateTestEventPerObjectiveByTypeAndProctoring() throws Exception {
	
	report.startStep("Initilize required test varibles");
		String test = "TOEIC Test";
		String typeOfTest1 = "Toeic Online";
		String typeOfTest2 = "Toeic Bridge";
		
	report.startStep("Restart browser with new institution");
		String instName = institutionsName[16];
		String instId = dbService.getInstituteIdByName(instName);
		pageHelper.restartBrowserInNewURL(instName, true);
		
		
	report.startStep("Login as Admin");
		loginPage = new NewUXLoginPage(webDriver, testResultService);
		pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(configuration.getProperty("schoolAdmin.user"),configuration.getProperty("institution.id")));
		sleep(1);
		tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
	
	report.startStep("Go to Assessments -> Test Configuration");
		TmsAssessmentsTestsAssignmentPage tmsAssessmentsTestsAssignmentPage = 
				new TmsAssessmentsTestsAssignmentPage(webDriver, testResultService);
		tmsAssessmentsTestsAssignmentPage.goToTestConfiguration();
		
	report.startStep("Select Test Configuration from dropdown");
		tmsAssessmentsTestsAssignmentPage.selectTestObjectives(test,typeOfTest1,typeOfTest2,true);
		//webDriver.switchToTopMostFrame();
		
		List<String[]> configuration = dbService.getToeicTestObjectives(instId);
		String[] certificationTest = configuration.get(0);
		String[] placementTetst = configuration.get(1);
		
	report.startStep("Change Test Configuration from dropdown");
		tmsAssessmentsTestsAssignmentPage.selectTestObjectives(test,typeOfTest2,"English Discoveries",false);
		configuration = dbService.getToeicTestObjectives(instId);
		String[] certificationTest2 = configuration.get(0);
		String[] placementTetst2 = configuration.get(1);
		
		
	report.startStep("Compare changes are stored in DB");
		textService.assertTrue("ToeicTestObjectiveSettingsId doesn't updated on DataBase", !certificationTest[2].equals(certificationTest2[2]));
		textService.assertTrue("AllowTeacherSetObjectives doesn't updated on DataBase", !certificationTest[4].equals(certificationTest2[4]));
		
		textService.assertTrue("ToeicTestObjectiveSettingsId doesn't updated on DataBase", !placementTetst[2].equals(placementTetst2[2]));
		textService.assertTrue("Proctored doesn't updated on DataBase", !placementTetst[3].equals(placementTetst2[3]));
		textService.assertTrue("AllowTeacherSetObjectives doesn't updated on DataBase", !placementTetst[4].equals(placementTetst2[4]));
		//textService.assertNotSame(certificationTest2, placementTetst2);
	
	report.startStep("Exit from");	
		tmsHomePage.clickOnExitTMS();
	}
		
		
	@Test
	//@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "" })
	public void testAssignLargeAmountOfStudentsPLT() throws Exception {
		
		institutionName=institutionsName[1];//
		
		report.startStep("Change to Courses Instituion");
		pageHelper.restartBrowserInNewURL(institutionName, true); //
		sleep(2);
		
		String className = configuration.getProperty("classname.Class_LargeStudentAmount");
		String classId=dbService.getClassIdByName(className, institutionId);
		
		report.startStep("Delete Users Tokens");
		dbService.deleteUsersTokens(classId);
		
		report.startStep("Login as Admin");
		loginPage = new NewUXLoginPage(webDriver, testResultService);
		pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(configuration.getProperty("schoolAdmin.user"), institutionId));
		sleep(1);
		tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
		sleep(5);
		TmsAssessmentsTestsAssignmentPage tmsAssessmentsTestsAssignmentPage = new TmsAssessmentsTestsAssignmentPage(webDriver, testResultService);
		
		report.startStep("Go to Tests Assignemnt Page");
		tmsAssessmentsTestsAssignmentPage.goToAssessmentTestsAssignment();
		
		report.startStep("Choose PLT option and Click Go");
		tmsAssessmentsTestsAssignmentPage.selectFeatureAndGo("PL");
		sleep(2);
		
		report.startStep("Select Class");
		tmsAssessmentsTestsAssignmentPage.selectPltClass(className);
		sleep(1);
		
		report.startStep("Click Save");
		tmsHomePage.clickOnSave(); 
		
		report.startStep("Wait until loading is over and close allert");
		sleep(10);
		webDriver.closeAlertByAccept();
	
		report.startStep("Validate no invalid tokens were stored in DB");
		int invalidTokenCount = Integer.parseInt(dbService.getStudentsCountWithInvalidToken(classId));
		testResultService.assertEquals(false, invalidTokenCount > 0, "Invalid Token was found in PLT assignment");
		
		report.startStep("Logout");
		tmsHomePage.clickOnExit();
		
	}


	
	public void clickGoInTms() throws Exception {
		webDriver.switchToTopMostFrame();
		tmsHomePage.switchToMainFrame();
		tmsHomePage.switchToFormFrame();
		sleep(1);
		tmsHomePage.clickOnGo();	
		
		webDriver.switchToTopMostFrame();
		tmsHomePage.switchToMainFrame();
	}
	
	@After
	public void tearDown() throws Exception {
		report.startStep("Restore default PLT settings");
		if (institutionId == "")
			institutionId = configuration.getProperty("institution.id");
		pageHelper.assignPltToInstitution(institutionId);
		institutionName="";
		super.tearDown();
	}
}
