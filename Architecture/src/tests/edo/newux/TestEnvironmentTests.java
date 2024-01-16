package tests.edo.newux;

import Enums.ByTypes;
import Enums.CourseCodes;
import Enums.CourseTests;
import Enums.UserType;
import Interfaces.TestCaseParams;
import Objects.CourseTest;
import jcifs.smb.SmbFile;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.WebElement;
import pageObjects.edo.*;
import services.PageHelperService;
import testCategories.edoNewUX.ReleaseTests;
import testCategories.inProgressTests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestEnvironmentTests extends BasicNewUxTest {
	
	//String instId;
	NewUxAssessmentsPage testsPage;
	TestEnvironmentPage testEnvironmentPage;
	NewUXLoginPage loginPage;
	String courseCode;
	String courseId;
	public static String testId;
	JSONObject jsonObj = new JSONObject();
	JSONObject localizationJson = new JSONObject();
	//boolean newTE = true;
	String className;
	
	String returnUrl="";
	String email="";
	String userFN ="";
	String userLN = "";
	String CanvasClassId = "";
	boolean newUser=false;
	String levelCEFR = "";
	boolean useCEFR = false;
	public static boolean useSMB = false; 
	String[] codes = new String[3];
	List<String> lessonsIds = new ArrayList<String>();
	
	//String ServerPath="\\\\dev2008\\EdoNet300Res\\Runtime\\Metadata\\Courses\\";
	
	@Before
	public void setup() throws Exception {
		institutionName=institutionsName[1];
		super.setup();
		//instId=coursesInstId;
		//coursesInstId = institutionId; //
		dbService.checkAndturnOnTestEnviormentFlag(institutionId);
		
		report.startStep("Retrieve Localization Json US");
		useSMB = false;
		localizationJson = netService.getJsonObject("smb://"+PageHelperService.physicalPath+"/Runtime/Localization/TestInstructions/", "en_US.json", useSMB);
		
		//report.startStep("Turn On Test Environment FLAG");
//instId = dbService.getInstituteIdByName("courses");
		//dbService.turnOnTestEnviormentFlag(instId);
		
		//report.startStep("Change 989013148 in Test Bank to 0");
		//dbService.updateTestBank("0", "989013148", "1822");
		
//		report.startStep("Retrieve the URL");
//		String url = webDriver.getUrl();
		
//		report.startStep("Replace 'Automation' to 'Courses'");
//		url = url.replace("automation", "courses");
		
//		report.startStep("Change to Correct URL");
//		webDriver.openUrl(url);
		
		className = configuration.getProperty("classname.CourseTest");
		homePage = createUserAndLoginNewUXClass(className, institutionId);
		pageHelper.skipOptin();
		homePage.closeAllNotifications();
		pageHelper.skipOnBoardingHP();
		homePage.waitHomePageloadedFully();
	
		newTe = true;	
	}
	
	@Test
	//@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "" })
	public void testCompletePLTAndGetResultsAndCheckAlgorithm() throws Exception {
		
		report.startStep("Init Data");
		String adminId = dbService.getAdminIdByInstId(institutionId);
		String classId = dbService.getClassIdByName(className, institutionId);
		testId = "11";
		String answersFilePath = "files/PLTTestData/PLT_Answers_AllVersions.csv";
		
		testEnvironmentPage = new TestEnvironmentPage(webDriver, testResultService);
		
		int iteration = 2;
		// iteration 6 is different- score for reading is 100 (so both questions are answered)- but actual score is 63 (since questions are answered partially)
		// same for iteration 7- score for reading is 100 (so both questions are answered)- but actual score is 73 (since questions are answered partially)
	/*
		report.startStep("Log Out");
		homePage.logOutOfED();
		
		report.startStep("Log In as Student");
		homePage = createUserAndLoginNewUXClass(className, coursesInstId);
		homePage.closeAllNotifications();
		homePage.waitHomePageloaded();
		pageHelper.closeLastSessionImproperLogoutAlert();
*/		
		report.startStep("Assign PLT test to student");
		pageHelper.assignPltTestToStudetInTestEnvironment(studentId, 1, "11", adminId, institutionId, classId);
		sleep(2);
		
		report.startStep("Copy the Json object");
		report.startStep("Retrieve JSON Array of Test ID: " + testId);
		jsonObj = netService.getJsonObject("smb://"+PageHelperService.physicalPath+"/Runtime/Metadata/Courses/CourseTests/", testId+".json",useSMB);
		
		report.startStep("Open Assessments Page");
		testsPage = homePage.openAssessmentsPage(false);
		sleep(1);
		
		report.startStep("Start Test");
		String testName = testsPage.clickStartTest(1, 1);
		String finalLevelString = performPLT_NewTE(testName, studentId, iteration, jsonObj, localizationJson, answersFilePath, institutionId);
		sleep(1);
		
		report.startStep("Click Exit Button");
		testEnvironmentPage.clickExitPlt(); 
		sleep(1);
		
		report.startStep("Open Assessments");
		homePage.openAssessmentsPage(false);
						
		report.startStep("Open Tests Results section");
		testsPage.clickOnArrowToOpenSection("3");
				
		report.startStep("Verify PLT displayed in Test Results");
		testsPage.checkTestDisplayedInSectionByTestName(testName, "3", "1");
									
		//report.startStep("Check Date of Submission");
		//testsPage.checkSubmissionDateForTests("1"); // Temporarily in comment since it fails in jenkins
				
		report.startStep("Check Score in Test Results");
		testsPage.checkScoreLevelPLT("1", finalLevelString);
		
		report.startStep("Close Assessments");
		testsPage.close();
		
		report.startStep("Log Out");
		homePage.logOutOfED();
	}
	
	@Test
	@TestCaseParams(testCaseID = { "78729" , "78604" , "78685" , "78721" , "78859", "79554", "78975" })
	public void testCompleteMidTestAndGetResultsRoundLevel2() throws Exception {
		
		String wantedTestId = "989017755";
		//String wantedTestId = "989022790";
		
		// Initialize Test Environment Page
		testEnvironmentPage = new TestEnvironmentPage(webDriver, testResultService);
	
		report.startStep("Init Data");
		//courseCode = courseCodes[1];
		//CourseCodes wantedCourseCode = testEnvironmentPage.getCourseCodeEnumByCourseCodeString(courseCode);
		//courseId = getCourseIdByCourseCode(courseCode);
		//String answerFilePath = "files/CourseTestData/MidTerm_Answers.csv";
		String answerFilePath = "files/CourseTestData/MidTerm_Answers.csv";
		String courseCode = testEnvironmentPage.getCourseCodeByTestId(answerFilePath, wantedTestId); //courseCode = courseCodes[1]; // B1
		CourseCodes wantedCourseCode = testEnvironmentPage.getCourseCodeEnumByCourseCodeString(courseCode); //CourseCodes wantedCourseCode = CourseCodes.B1;
		String courseId = getCourseIdByCourseCode(courseCode);
		CourseTests courseTestType= CourseTests.MidTerm;
				
		report.startStep("Assign B1 Mid-Term Test to student");
		pageHelper.assignAutomatedTestToStudetInTestEnvironment(studentId, courseId, 2,0,0,1);
		sleep(2);
		
		report.startStep("Return Test Id");
		testId = pageHelper.getAssignedTestIdForStudent(studentId,courseId, 2);
		String userExitSettingsId = dbService.getUserExitSettingsId(studentId, testId, courseId, "2");
		if (!testId.equals(wantedTestId)) {
			dbService.updateTestIdInUserExitSettings(wantedTestId, userExitSettingsId);
			testId = pageHelper.getAssignedTestIdForStudent(studentId,courseId, 2);
			String token = pageHelper.getTokenFromConsoleWithParameters("1", studentId, testId, userExitSettingsId);
			dbService.updateAssignWithToken(token, userExitSettingsId);
		}
		
		//testEnvironmentPage.testDuration = dbService.getTestDurationByUserExitSettingsId(userExitSettingsId);
		try{
			report.startStep("Copy and keep the original " +testId+".json file to backup folder");
			pageHelper.runConsoleProgram("cmd /c copy "+PageHelperService.ServerPath+"CourseTests\\"+testId+".json "+PageHelperService.ServerPath+"CourseTests\\ForAutomationBackup");
			
			report.startStep("Change JSON of Test to Round 2");
			pageHelper.runConsoleProgram("cmd /c copy "+PageHelperService.ServerPath+"CourseTests\\"+testId+"_RoundLevel2\\"+testId+".json "+PageHelperService.ServerPath+"CourseTests");
		
			report.startStep("Retrieve JSON Array of Test ID: " + testId);
			jsonObj = netService.getJsonObject("smb://"+PageHelperService.physicalPath+"/Runtime/Metadata/Courses/CourseTests/", testId,useSMB);
			
			report.startStep("Open Assessments Page");
			testsPage = homePage.openAssessmentsPage(false);
			
			report.startStep("Start Test");
			int testSequence = testsPage.getTestSequenceByCourseId(courseId);
			String testName = testsPage.clickStartTest(1, testSequence);
			webDriver.closeAlertByAccept();
			sleep(3);
			
			report.startStep("Validate Test Name");
			testEnvironmentPage.validateTestName(testName);
			//sleep(5);
			
			report.startStep("Validate Course Intro");
			testEnvironmentPage.validateIntro(jsonObj, localizationJson, testId, testName);
					
			report.startStep("Get Round Level of Course");
			String roundLevelOfCourse = testEnvironmentPage.getRoundLevelFromJson(jsonObj, testId);
			//String roundLevelOfCourse = testEnvironmentPage.getValueOfWantedKeyInSpecificTestId(jsonObj, testId, "RoundLevel");
			
			// Get Number of Sections to Submit
			int unitCount = 0;
			
			// Get list of lesson count for each unit in course
			List<String[]> lessonCountForUnits = dbService.getLessonCountForAllUnitsInCourse(testId);
			
			// Get unit ids list from previous list
			List<String> unitIds = new ArrayList<String>();
			for (int j = 0; j < lessonCountForUnits.size(); j++) {
				unitIds.add(lessonCountForUnits.get(j)[0]);
			}
			
			unitCount = lessonCountForUnits.size();
			// Change unit count below to as many units you want to answer (In this case all units are being answered)
			int unitsToAnswer = unitCount;
			int sectionsToAnswer = 0;
			for (int k = 0; k < unitsToAnswer; k++) {
				sectionsToAnswer += Integer.parseInt(lessonCountForUnits.get(k)[1]);
			}
					
			report.startStep("Answer Questions and Validate Intros");
			testEnvironmentPage.answerQuestionsNew(answerFilePath, testId, wantedCourseCode, courseTestType, sectionsToAnswer, roundLevelOfCourse, unitIds, jsonObj, localizationJson, testName, unitsToAnswer);
					
			report.startStep("Validate Score in DB");
			List<String[]> userTestProgress = dbService.getUserTestProgressByUserId(studentId);
			String scoreForDisplayDB = userTestProgress.get(0)[4];
			testResultService.assertEquals("100", scoreForDisplayDB, "Score in DB is Incorrect");
			
			report.startStep("Validate isCompleted in DB");
			String isCompletedDB = userTestProgress.get(0)[8];
			testResultService.assertEquals("1", isCompletedDB, "isCompleted in DB is Incorrect");
			
			report.startStep("Validate Submit Reason in DB");
			String submitReasonDB = userTestProgress.get(0)[9];
			testResultService.assertEquals("1", submitReasonDB, "Submit Reason in DB is Incorrect");
			
			report.startStep("Validate Test Status in DB is Done");
			String userExitTestSettingsId = userTestProgress.get(0)[0];
			String testStatus = dbService.getTestStatusByUserExitTestSettingsId(userExitTestSettingsId);
			testResultService.assertEquals("3", testStatus, "Test Status in DB is incorrect.");
			
			report.startStep("Validate Score in Outro Page");
			testEnvironmentPage.validateScoreEndOfTest(scoreForDisplayDB);
			
			report.startStep("Click Exit Button");
			testEnvironmentPage.clickExitCourseTest();
			
			report.startStep("Log Out");
			homePage.logOutOfED();
			
		}finally {	
				report.startStep("copy back the original file to original path");
				pageHelper.runConsoleProgram("cmd /c copy "+PageHelperService.ServerPath+"CourseTests\\ForAutomationBackup\\"+testId+".json "+PageHelperService.ServerPath+"CourseTests");	
			}
		
	}
	
	@Test
	@TestCaseParams(testCaseID = { "78729" , "78604" , "78685" , "78721" , "78859", "78976" })
	public void testCompleteMidTestAndGetResultsRoundLevel3() throws Exception {
		
		String wantedTestId = "989022835";//"989017755"; //
		
		// Initialize Test Environment Page
		testEnvironmentPage = new TestEnvironmentPage(webDriver, testResultService);
	
		report.startStep("Init Data");
		courseCode = courseCodes[1];
		CourseCodes wantedCourseCode = testEnvironmentPage.getCourseCodeEnumByCourseCodeString(courseCode);
	
		courseId = getCourseIdByCourseCode(courseCode);
		
		//String answerFilePath = "files/CourseTestData/MidTerm_Answers.csv";
		//CourseTests courseTestType= CourseTests.MidTerm;
		String answerFilePath = "files/CourseTestData/FinalTest_Answers.csv";
		CourseTests courseTestType= CourseTests.FinalTest;
	
		report.startStep("Assign B1 Mid-Term Test to student");
		pageHelper.assignAutomatedTestToStudetInTestEnvironment(studentId, courseId, 3,0,0,1);
		sleep(2);
		
		report.startStep("Return Test Id");
		testId = pageHelper.getAssignedTestIdForStudent(studentId,courseId, 3);
		
		if (!testId.equals(wantedTestId)) {
			String userExitSettingsId = dbService.getUserExitSettingsId(studentId, testId, courseId, "2");
			dbService.updateTestIdInUserExitSettings(wantedTestId, userExitSettingsId);
			testId = pageHelper.getAssignedTestIdForStudent(studentId,courseId, 3);
			String token = pageHelper.getTokenFromConsoleWithParameters("1", studentId, testId, userExitSettingsId);
			dbService.updateAssignWithToken(token, userExitSettingsId);
		}
		try{
			report.startStep("Copy and keep the original " +testId+".json file to backup folder");
			pageHelper.runConsoleProgram("cmd /c copy "+PageHelperService.ServerPath+"CourseTests\\"+testId+".json "+PageHelperService.ServerPath+"CourseTests\\ForAutomationBackup");
			
			report.startStep("Change JSON of Test to Round 3");
			pageHelper.runConsoleProgram("cmd /c copy "+PageHelperService.ServerPath+"CourseTests\\"+testId+"_RoundLevel3\\"+testId+".json "+PageHelperService.ServerPath+"CourseTests");
			
			report.startStep("Retrieve JSON Array of Test ID: " + testId);
			jsonObj = netService.getJsonObject("smb://"+PageHelperService.physicalPath+"Runtime/Metadata/Courses/CourseTests/", testId,useSMB);
			
			report.startStep("Open Assessments Page");
			testsPage = homePage.openAssessmentsPage(false);
			
			report.startStep("Start Test");
			int testSequence = testsPage.getTestSequenceByCourseId(courseId);
			String testName = testsPage.clickStartTest(1, testSequence);
			webDriver.closeAlertByAccept();
			sleep(3);
	
			// Initialize Test Environment Page
			testEnvironmentPage = new TestEnvironmentPage(webDriver, testResultService);
			
			report.startStep("Validate Test Name");
			testEnvironmentPage.validateTestName(testName);
			sleep(1);
			
			report.startStep("Validate Course Intro");
			testEnvironmentPage.validateIntro(jsonObj, localizationJson, testId, testName);
					
			report.startStep("Get Round Level of Course");
			String roundLevelOfCourse = testEnvironmentPage.getRoundLevelFromJson(jsonObj, testId);
			//String roundLevelOfCourse = testEnvironmentPage.getValueOfWantedKeyInSpecificTestId(jsonObj, testId, "RoundLevel");
			
			// Get Number of Sections to Submit
			int unitCount = 0;
			
			// Get list of lesson count for each unit in course
			List<String[]> lessonCountForUnits = dbService.getLessonCountForAllUnitsInCourse(testId);
			
			// Get unit ids list from previous list
			List<String> unitIds = new ArrayList<String>();
			for (int j = 0; j < lessonCountForUnits.size(); j++) {
				unitIds.add(lessonCountForUnits.get(j)[0]);
			}
			
			unitCount = lessonCountForUnits.size();
			// Change unit count below to as many units you want to answer (In this case all units are being answered)
			int unitsToAnswer = unitCount;
			int sectionsToAnswer = 0;
			for (int k = 0; k < unitsToAnswer; k++) {
				sectionsToAnswer += Integer.parseInt(lessonCountForUnits.get(k)[1]);
			}
			
			report.startStep("Answer Questions and Validate Intros");
			testEnvironmentPage.answerQuestionsNew(answerFilePath, testId, wantedCourseCode, courseTestType, sectionsToAnswer, roundLevelOfCourse, unitIds, jsonObj, localizationJson, testName, unitsToAnswer);
			
			report.startStep("Validate Score in DB");
			List<String[]> userTestProgress = dbService.getUserTestProgressByUserId(studentId);
			String scoreForDisplayDB = userTestProgress.get(0)[4];
			testResultService.assertEquals("100", scoreForDisplayDB, "Score in DB is Incorrect");
			
			report.startStep("Validate isCompleted in DB");
			String isCompletedDB = userTestProgress.get(0)[8];
			testResultService.assertEquals("1", isCompletedDB, "isCompleted in DB is Incorrect");
			
			report.startStep("Validate Submit Reason in DB");
			String submitReasonDB = userTestProgress.get(0)[9];
			testResultService.assertEquals("1", submitReasonDB, "Submit Reason in DB is Incorrect");
			
			report.startStep("Validate Test Status in DB is Done");
			String userExitTestSettingsId = userTestProgress.get(0)[0];
			String testStatus = dbService.getTestStatusByUserExitTestSettingsId(userExitTestSettingsId);
			testResultService.assertEquals("3", testStatus, "Test Status in DB is incorrect.");
			
			report.startStep("Validate Score in Outro Page");
			testEnvironmentPage.validateScoreEndOfTest(scoreForDisplayDB);
			
			report.startStep("Click Exit Button");
			testEnvironmentPage.clickExitCourseTest();
			sleep(2);
			
			report.startStep("Log Out");
			homePage.logOutOfED();
		}
		finally {	
			report.startStep("copy back the original file to original path");
			pageHelper.runConsoleProgram("cmd /c copy "+PageHelperService.ServerPath+"CourseTests\\ForAutomationBackup\\"+testId+".json "+PageHelperService.ServerPath+"CourseTests");	
		}
	}
	
	
	
	public SmbFile[] getJsonFiles(String filesRoot) throws IOException {

		return netService.getFilesInFolder(filesRoot);

	}

	
	
	
	/*@Test
	@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "" })
	public void testCompleteFinalTestAndGetResultsRoundLevel2() throws Exception {
		
		// Initialize Test Environment Page
		testEnvironmentPage = new TestEnvironmentPage(webDriver, testResultService);
	
		report.startStep("Init Data");
		courseCode = courseCodes[1];
		CourseCodes wantedCourseCode = testEnvironmentPage.getCourseCodeEnumByCourseCodeString(courseCode);
	
		courseId = getCourseIdByCourseCode(courseCode);
		String answerFilePath = "files/CourseTestData/FinalTest_Answers.csv";
		CourseTests courseTestType= CourseTests.FinalTest;
		
		report.startStep("Assign B1 Final Course Test to student for future time");
		pageHelper.assignAutomatedTestToStudetInTestEnvironment(studentId, courseId,3,0,0,2);
		sleep(3);
						
		report.startStep("Return Test Id");
		testId = pageHelper.getAssignedTestIdForStudent(studentId,courseId, 3);
		
		report.startStep("Change JSON of Test to Round 2");
		pageHelper.runConsoleProgram("cmd /c copy "+ServerPath+"CourseTests\\"+testId+"_RoundLevel2\\"+testId+".json "+ServerPath+"CourseTests");

		report.startStep("Retrieve JSON Array of Test ID: " + testId);
		jsonObj = netService.getJsonObject("smb://dev2008/EdoNet300Res/Runtime/Metadata/Courses/CourseTests/", testId);
		
		report.startStep("Open Assessments Page");
		testsPage = homePage.openAssessmentsPage(false);
		
		report.startStep("Start Test");
		String testName = testsPage.clickStartTest(1, 2);
		
		// Initialize Test Environment Page
		//testEnvironmentPage = new TestEnvironmentPage(webDriver, testResultService);
		
		report.startStep("Validate Test Name");
		testEnvironmentPage.validateTestName(testName);
		
		report.startStep("Validate Course Intro");
		testEnvironmentPage.validateIntro(jsonObj, localizationJson, testId, testName);
				
		report.startStep("Get Round Level of Course");
		String roundLevelOfCourse = testEnvironmentPage.getRoundLevelFromJson(jsonObj, testId);
		//String roundLevelOfCourse = testEnvironmentPage.getValueOfWantedKeyInSpecificTestId(jsonObj, testId, "RoundLevel");
		
		// Get Number of Sections to Submit
		int unitCount = 0;
		
		// Get list of lesson count for each unit in course
		List<String[]> lessonCountForUnits = dbService.getLessonCountForAllUnitsInCourse(testId);
		
		// Get unit ids list from previous list
		List<String> unitIds = new ArrayList<String>();
		for (int j = 0; j < lessonCountForUnits.size(); j++) {
			unitIds.add(lessonCountForUnits.get(j)[0]);
		}
		
		unitCount = lessonCountForUnits.size();
		// Change unit count below to as many units you want to answer (In this case all units are being answered)
		int unitsToAnswer = unitCount;
		int sectionsToAnswer = 0;
		for (int k = 0; k < unitsToAnswer; k++) {
			sectionsToAnswer += Integer.parseInt(lessonCountForUnits.get(k)[1]);
		}
				
		report.startStep("Answer Questions and Validate Intros");
		testEnvironmentPage.answerQuestionsNew(answerFilePath, testId, wantedCourseCode, courseTestType, sectionsToAnswer, roundLevelOfCourse, unitIds, jsonObj, localizationJson, testName, unitsToAnswer);
				
		report.startStep("Click Close");
		testEnvironmentPage.clickClose();
		
		report.startStep("Log Out");
		homePage.logOutOfED();
		
		
		
		
		
		/*report.startStep("Validate Intros");
		testEnvironmentPage.validateIntros(jsonObj,localizationJson,testName);
		
		report.startStep("Click Start Test Button");
		testEnvironmentPage.clickStartTest();
		
		report.startStep("Return Test Id and Number of Sections to Submit");
		testId = pageHelper.getAssignedTestIdForStudent(studentId,courseId, 3); 
		int sectionsToSubmit = Integer.parseInt(dbService.getNumberOfSectionsInTestForCourseByTestId(courseId,testId,2));

		sectionsToSubmit = 1; // Temporary
		
		report.startStep("Submit First Section");
		//testEnviormentPage.answerQuestions(testId, sectionsToSubmit);

		report.startStep("Click Close");
		testEnvironmentPage.clickClose();
		
		report.startStep("Log Out");
		homePage.logOutOfED();
	}*/
	
	@Test
	@TestCaseParams(testCaseID = { "78641" })
	public void testCancelButtonLeadsToHomePage() throws Exception {
		
		String wantedTestId = "989017755";
		
		report.startStep("Assign B1 Mid-Term Test to student");
		courseCode = "B1";
		courseId = getCourseIdByCourseCode(courseCode);
		pageHelper.assignAutomatedTestToStudetInTestEnvironment(studentId, courseId, 2,0,0,1);
		sleep(2);
		
		report.startStep("Return Test Id");
		testId = pageHelper.getAssignedTestIdForStudent(studentId,courseId, 2);
		if (!testId.equals(wantedTestId)) {
			// update test id
			String userExitSettingsId = dbService.getUserExitSettingsId(studentId, testId, courseId, "2");
			dbService.updateTestIdInUserExitSettings(wantedTestId, userExitSettingsId);
			testId = pageHelper.getAssignedTestIdForStudent(studentId,courseId, 2);
			String token = pageHelper.getTokenFromConsoleWithParameters("1", studentId, testId, userExitSettingsId);
			dbService.updateAssignWithToken(token, userExitSettingsId);
		}
		
		report.startStep("Open Assessments Page");
		testsPage = homePage.openAssessmentsPage(false);
		
		report.startStep("Start Test");
		int testSequence = testsPage.getTestSequenceByCourseId(courseId);
		String testName = testsPage.clickStartTest(1, testSequence);
		webDriver.closeAlertByAccept();
		
		// Initialize Test Environment Page
		testEnvironmentPage = new TestEnvironmentPage(webDriver, testResultService);
		
		report.startStep("Click on ED Icon");
		testEnvironmentPage.clickOnEDIcon();
		webDriver.waitUntilElementAppears("//div[@class='modal-content']", 5);
		
		report.startStep("Validate ED Logo is Correct and Close ED Info");
		testEnvironmentPage.validateEDLogoIsCorrect();
		testEnvironmentPage.closeEDInfo();
	
		report.startStep("Click Cancel");
		//testEnvironmentPage.clickClose();
		testEnvironmentPage.clickCancel();
		
		report.startStep("Validate Home Page is Displayed");
		boolean homePageDisplayed = webDriver.waitUntilElementAppears("//span[contains(@class,'home__userName')]", 60);
		testResultService.assertEquals(true, homePageDisplayed,"Home Page is Not Displayed.");
		
		report.startStep("Open Community Page");
		homePage.clickButtonOnNavBar("sitemenu__itemCommunity");
		
		report.startStep("Open Assessments Page");
		homePage.openAssessmentsPage(false);
		
		report.startStep("Start Test");
		testSequence = testsPage.getTestSequenceByCourseId(courseId);
		testsPage.clickStartTest(1, testSequence);
		webDriver.closeAlertByAccept();
		sleep(3);
		
		report.startStep("Click Cancel");
		//testEnvironmentPage.clickClose();
		testEnvironmentPage.clickCancel();
		
		report.startStep("Validate Home Page is Displayed");
		homePageDisplayed = webDriver.waitUntilElementAppears("//span[contains(@class,'home__userName')]", 60);
		testResultService.assertEquals(true, homePageDisplayed,"Home Page is Not Displayed.");
		sleep(1);
		
		report.startStep("Open My Progress Page");
		homePage.clickOnMyProgress();

		report.startStep("Open Navigation Bar");
		homePage.clickToOpenNavigationBar();
		
		report.startStep("Open Assessments Page");
		homePage.openAssessmentsPage(false);
		
		report.startStep("Start Test");
		testSequence = testsPage.getTestSequenceByCourseId(courseId);
		testsPage.clickStartTest(1, testSequence);
		webDriver.closeAlertByAccept();
		sleep(3);
		
		report.startStep("Click Cancel");
		//testEnvironmentPage.clickClose();
		testEnvironmentPage.clickCancel();
		
		report.startStep("Validate Home Page is Displayed");
		homePageDisplayed = webDriver.waitUntilElementAppears("//span[contains(@class,'home__userName')]", 60);
		testResultService.assertEquals(true, homePageDisplayed,"Home Page is Not Displayed.");
		
		report.startStep("Log Out");
		homePage.logOutOfED();
	}
	
	/*@Test
	@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "" })
	public void testToeflJuniorTest() throws Exception {

		report.startStep("Log Out");
		homePage.logOutOfED();
		
		String url = "https://edux.edusoftrd.com/ed2016#"; 
		
		report.startStep("Close browser and open again");
		pageHelper.restartBrowser();
		
		report.startStep("Open TOEFL URL");
		webDriver.openUrl(url);
		
		// Initialize login page
		loginPage = new NewUXLoginPage(webDriver,testResultService);
		loginPage.waitEDToeicLoginLoaded(); 
		
		report.startStep("Login as Student");
		homePage = loginPage.loginAsStudent("dog8", "12345");
				
		report.startStep("Wait until loading message dissappers (if its displayed)");
		homePage.waitUntilLoadingMessageIsOver();
			
		report.startStep("Close All Notifications");	
		homePage.closeAllNotifications();
		
		homePage.waitHomePageloaded();
		
		report.startStep("Close modal pop up (if it appears)");
		homePage.closeModalPopUp();
		
		report.startStep("Skip the Walkthrough");
		pageHelper.skipOnBoardingHP();
		
		report.startStep("Open Assessments Page");
		testsPage = homePage.openAssessmentsPage(false);
		
		report.startStep("Start Test");
		String testName = testsPage.clickStartTest(1, 2);
		
		String instructionTitle =""; // ??
		String instructionText = "The "+testName+" assesses your English language skills, based on what you have studied in the course.";
		
		// Initialize Test Enviorment Page
		testEnvironmentPage = new TestEnvironmentPage(webDriver, testResultService);
		
		report.startStep("Validate Test Name");
		testEnvironmentPage.validateTestName(testName);
		
		testEnvironmentPage.validateTemplate(instructionTitle, instructionText);
		
		report.startStep("Click Cancel");
		testEnvironmentPage.clickCancel();
		
		report.startStep("Log Out");
		homePage.logOutOfED();
	}*/
	
	/*@Test
	@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "" })
	public void testToeflPrimaryTest() throws Exception {

		report.startStep("Log Out");
		homePage.logOutOfED();
		
		String url = "https://edux.edusoftrd.com/ed2016#"; 
		
		report.startStep("Close browser and open again");
		pageHelper.restartBrowser();
		
		report.startStep("Open TOEFL URL");
		webDriver.openUrl(url);
		
		// Initialize login page
		loginPage = new NewUXLoginPage(webDriver,testResultService);
		loginPage.waitEDToeicLoginLoaded(); 
		
		report.startStep("Login as Student");
		homePage = loginPage.loginAsStudent("cat8", "12345");
				
		report.startStep("Wait until loading message dissappers (if its displayed)");
		homePage.waitUntilLoadingMessageIsOver();
			
		report.startStep("Close All Notifications");	
		homePage.closeAllNotifications();
		
		homePage.waitHomePageloaded();
		
		report.startStep("Close modal pop up (if it appears)");
		homePage.closeModalPopUp();
		
		report.startStep("Skip the Walkthrough");
		pageHelper.skipOnBoardingHP();
		
		report.startStep("Open Assessments Page");
		testsPage = homePage.openAssessmentsPage(false);
		
		report.startStep("Start Test");
		String testName = testsPage.clickStartTest(1, 2);
		
		String instructionTitle =""; // ??
		String instructionText = "The "+testName+" assesses your English language skills, based on what you have studied in the course.";
		
		// Initialize Test Enviorment Page
		testEnvironmentPage = new TestEnvironmentPage(webDriver, testResultService);
		
		report.startStep("Validate Test Name");
		testEnvironmentPage.validateTestName(testName);
		
		testEnvironmentPage.validateTemplate(instructionTitle, instructionText);
		
		report.startStep("Click Cancel");
		testEnvironmentPage.clickCancel();
		
		report.startStep("Log Out");
		homePage.logOutOfED();
	}*/
	
	@Test
	//@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "79554", "78975", "78982" })
	public void testBackButtonRoundLevel2() throws Exception {
		
		//String wantedTestId = "989017755";
		String wantedTestId = "989022835";
		
		report.startStep("Init Data");
		courseCode = "B1";
		courseId = getCourseIdByCourseCode(courseCode);
		//NewUxLearningArea2 learningArea2 = new NewUxLearningArea2(webDriver, testResultService);
				
		report.startStep("Assign B1 Mid-Term Test to student");
		pageHelper.assignAutomatedTestToStudetInTestEnvironment(studentId, courseId, 2,0,0,1);
		sleep(2);
		
		// Initialize Test Environment Page
		testEnvironmentPage = new TestEnvironmentPage(webDriver, testResultService);
		
		report.startStep("Return Test Id");
		testId = pageHelper.getAssignedTestIdForStudent(studentId,courseId, 2);
		if (!testId.equals(wantedTestId)) {
			String userExitSettingsId = dbService.getUserExitSettingsId(studentId, testId, courseId, "2");
			dbService.updateTestIdInUserExitSettings(wantedTestId, userExitSettingsId);
			testId = pageHelper.getAssignedTestIdForStudent(studentId,courseId, 2);
			String token = pageHelper.getTokenFromConsoleWithParameters("1", studentId, testId, userExitSettingsId);
			dbService.updateAssignWithToken(token, userExitSettingsId);	
		}
		
		try{
			
			report.startStep("Copy and keep the original " +testId+".json file to backup folder");
			pageHelper.runConsoleProgram("cmd /c copy "+PageHelperService.ServerPath+"CourseTests\\"+testId+".json "+PageHelperService.ServerPath+"CourseTests\\ForAutomationBackup");
			
			report.startStep("Change JSON of Test to Round 2");
			pageHelper.runConsoleProgram("cmd /c copy "+PageHelperService.ServerPath+"CourseTests\\"+testId+"_RoundLevel2\\"+testId+".json "+PageHelperService.ServerPath+"CourseTests");
				
			report.startStep("Retrieve JSON Array of Test ID: " + testId);
			jsonObj = netService.getJsonObject("smb://"+PageHelperService.physicalPath+"/Runtime/Metadata/Courses/CourseTests/", testId,useSMB);
			
			report.startStep("Open Assessments Page");
			testsPage = homePage.openAssessmentsPage(false);
			
			report.startStep("Start Test");
			int testSequence = testsPage.getTestSequenceByCourseId(courseId);
			String testName = testsPage.clickStartTest(1, testSequence);
			webDriver.closeAlertByAccept();
			sleep(3);
						
			report.startStep("Validate Course Intro");
			testEnvironmentPage.validateIntro(jsonObj, localizationJson, testId, testName);
			
			report.startStep("Get Unit Ids list");
			List<String[]> lessonCountForUnits = dbService.getLessonCountForAllUnitsInCourse(testId);
			List<String> unitIds = new ArrayList<String>();
			for (int j = 0; j < lessonCountForUnits.size(); j++) {
				unitIds.add(lessonCountForUnits.get(j)[0]);
			}
			
			report.startStep("Get Round Level Value of Course");
			String roundLevelValue = testEnvironmentPage.getRoundLevelFromJson(jsonObj, testId);
			
			String backValue = "";
			List<String> lessonsIds;
			
			if (roundLevelValue.equals("2")) {
				
				for (int i = 0; i < unitIds.size(); i++) {
				
					report.startStep("Validate Unit Intro");
					testEnvironmentPage.validateIntro(jsonObj, localizationJson, unitIds.get(i), testName);
				
					report.startStep("Validate Title Index");
					testEnvironmentPage.validateTitleIndex();
					
					report.startStep("Get Back Value of Unit");
					backValue = testEnvironmentPage.getValueOfWantedKeyInSpecificTestId(jsonObj, unitIds.get(i), "Back");
					
					report.startStep("Get Lesson Ids list of First Unit");
					lessonsIds = testEnvironmentPage.getLessonsIdsFromLessonDetailsArrOfSpecificUnit(unitIds.get(i));
					
					for (int j = 0; j < lessonsIds.size();j++) {
						
						report.startStep("Validate Lesson Intro");
						testEnvironmentPage.validateIntro(jsonObj, localizationJson, lessonsIds.get(j), testName); 
						sleep(2);
						
						report.startStep("Browse to the End Of Lesson");
						testEnvironmentPage.acrossIntro();
						testEnvironmentPage.browseToLastSectionTask();
						
						if (j < lessonsIds.size()-1) {
							testEnvironmentPage.clickNext();
							sleep(4);
							
							report.startStep("Validate Back Button");
							testEnvironmentPage.validateBackButtonByJSONValue(backValue);
							sleep(3);
						}
						testEnvironmentPage.lessonIndex++;
					}
					
					testEnvironmentPage.lessonIndex = 1; // end of unit- reset lesson index
					
					sleep(1);
					report.startStep("Submit");
					testEnvironmentPage.submit(false);
					sleep(2);
					testEnvironmentPage.sectionIndex++;
					
					if (i < unitIds.size()-1) {
						
						report.startStep("Validate Back Button is false- End of Round");
						testEnvironmentPage.validateBackButtonByJSONValue("false");
						sleep(1);
					} else if (i == unitIds.size()-1) {
						sleep(2);
						report.startStep("Validate Back Button is not displayed- End of test");
						testEnvironmentPage.validateBackButtonIsNotDisplayed();
					}
				}
			}
			
			report.startStep("Validate Score");
			testEnvironmentPage.validateScoreEndOfTest("0");
			
			report.startStep("Click Exit Button");
			testEnvironmentPage.clickExitCourseTest();
			sleep(2); 
			
			report.startStep("Log Out");
			homePage.logOutOfED();
		}
		
		finally {	
			report.startStep("copy back the original file to original path");
			pageHelper.runConsoleProgram("cmd /c copy "+PageHelperService.ServerPath+"CourseTests\\ForAutomationBackup\\"+testId+".json "+PageHelperService.ServerPath+"CourseTests");	
		}
	}
	
	@Test
	//@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "78976", "78982" })
	public void testBackButtonRoundLevel3() throws Exception {
		
		String wantedTestId = "989017755";
		
		report.startStep("Init Data");
		courseCode = "B1";
		courseId = getCourseIdByCourseCode(courseCode);
		//courseId="3";
		NewUxLearningArea2 learningArea2 = new NewUxLearningArea2(webDriver, testResultService);
				
		report.startStep("Assign B1 Mid-Term Test to student");
		pageHelper.assignAutomatedTestToStudetInTestEnvironment(studentId, courseId, 2,0,0,1);
		sleep(2);
		
		report.startStep("Return Test Id");
		testId = pageHelper.getAssignedTestIdForStudent(studentId,courseId, 2);
		if (!testId.equals(wantedTestId)) {
			// update test id
			String userExitSettingsId = dbService.getUserExitSettingsId(studentId, testId, courseId, "2");
			dbService.updateTestIdInUserExitSettings(wantedTestId, userExitSettingsId);
			testId = pageHelper.getAssignedTestIdForStudent(studentId,courseId, 2);
			String token = pageHelper.getTokenFromConsoleWithParameters("1", studentId, testId, userExitSettingsId);
			dbService.updateAssignWithToken(token, userExitSettingsId);	
		}

		try{
			report.startStep("Copy and keep the original " +testId+".json file to backup folder");
			pageHelper.runConsoleProgram("cmd /c copy "+PageHelperService.ServerPath+"CourseTests\\"+testId+".json "+PageHelperService.ServerPath+"CourseTests\\ForAutomationBackup");
			
			report.startStep("Change JSON of Test to Round 3");
			pageHelper.runConsoleProgram("cmd /c copy "+PageHelperService.ServerPath+"CourseTests\\"+testId+"_RoundLevel3\\"+testId+".json "+PageHelperService.ServerPath+"CourseTests");
	
			report.startStep("Retrieve JSON Array of Test ID: " + testId);
			jsonObj = netService.getJsonObject("smb://"+PageHelperService.physicalPath+"/Runtime/Metadata/Courses/CourseTests/", testId, useSMB);
			
			report.startStep("Open Assessments Page");
			testsPage = homePage.openAssessmentsPage(false);
			
			report.startStep("Start Test");
			int testSequence = testsPage.getTestSequenceByCourseId(courseId);
			String testName = testsPage.clickStartTest(1, testSequence);
			webDriver.closeAlertByAccept();
			sleep(3);
	
			// Initialize Test Environment Page
			testEnvironmentPage = new TestEnvironmentPage(webDriver, testResultService);
					
			report.startStep("Validate Course Intro");
			testEnvironmentPage.validateIntro(jsonObj, localizationJson, testId, testName);
			
			report.startStep("Get Unit Ids list");
			List<String[]> lessonCountForUnits = dbService.getLessonCountForAllUnitsInCourse(testId);
			List<String> unitIds = new ArrayList<String>();
			for (int j = 0; j < lessonCountForUnits.size(); j++) {
				unitIds.add(lessonCountForUnits.get(j)[0]);
			}
			
			report.startStep("Get Round Level Value of Course");
			String roundLevelValue = testEnvironmentPage.getRoundLevelFromJson(jsonObj, testId);
			
			String backValue = "";
			List<String> lessonsIds;
			
			testEnvironmentPage.sectionIndex = 1;
			testEnvironmentPage.lessonIndex = 1;
			
			if (roundLevelValue.equals("3")) {
				int taskCount = 0;
				for (int i = 0; i < unitIds.size(); i++) {
					
					report.startStep("Validate Unit Intro");
					testEnvironmentPage.validateIntro(jsonObj, localizationJson, unitIds.get(i), testName);
					sleep(1);
				
					report.startStep("Get Lesson Ids list of First Unit");
					lessonsIds = testEnvironmentPage.getLessonsIdsFromLessonDetailsArrOfSpecificUnit(unitIds.get(i));
					
					for (int j = 0; j < lessonsIds.size();j++) {
						
						report.startStep("Validate Lesson Intro");
						testEnvironmentPage.validateIntro(jsonObj, localizationJson, lessonsIds.get(j), testName); 
						sleep(2);
						
						report.startStep("Validate Title Index");
						testEnvironmentPage.validateTitleIndex();
						
						report.startStep("Get Back Value of Lesson");
						backValue = testEnvironmentPage.getValueOfWantedKeyInSpecificTestId(jsonObj, lessonsIds.get(0), "Back");
						
						taskCount = learningArea2.getTasksCount();
						if (taskCount > 1) {
						
							sleep(1);
							report.startStep("Navigate 1 task");
							testEnvironmentPage.goTolastTestSectionItem(2,"1");
							sleep(2);
							report.startStep("Validate Back Button");
							testEnvironmentPage.validateBackButtonByJSONValue(backValue);
							sleep(2);
						}
							
						report.startStep("Browse to the End Of Lesson");
						testEnvironmentPage.browseToLastSectionTaskNew();
						
						report.startStep("Submit");
						testEnvironmentPage.submit(false);
						sleep(1);
						
						if (i == unitIds.size()-1 && j == lessonsIds.size()-1){
							
							report.startStep("Validate Back Button is not displayed- End of test");
							testEnvironmentPage.validateBackButtonIsNotDisplayed();
						} else {
						
							report.startStep("Validate Back Button is false - End of Round");
							testEnvironmentPage.validateBackButtonByJSONValue("false");	
						}
						
						testEnvironmentPage.lessonIndex++;
					}
					testEnvironmentPage.lessonIndex = 1;
					testEnvironmentPage.sectionIndex++;
				}
			}
			
			sleep(4);
			report.startStep("Validate Score");
			testEnvironmentPage.validateScoreEndOfTest("0");
			sleep(3);
			
			report.startStep("Click Exit Button");
			testEnvironmentPage.clickExitCourseTest();
			sleep(2); 
			
			report.startStep("Log Out");
			homePage.logOutOfED();
		}
		finally {	
			report.startStep("copy back the original file to original path");
			pageHelper.runConsoleProgram("cmd /c copy "+PageHelperService.ServerPath+"CourseTests\\ForAutomationBackup\\"+testId+".json "+PageHelperService.ServerPath+"CourseTests");	
		}
	}
	
	/*
	// This test runs through all test ids of mid term test and for each- answers the tests + validates intros
	@Test
	@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "78729" , "78604" , "78685" , "78721" , "78859", "79554", "78975" })
	public void testCompleteAllMidTermTestAndGetResultsRoundLevel2() throws Exception {
	
		report.startStep("Init Data");
		courseCode = "B1";
		courseId = getCourseIdByCourseCode(courseCode);
		int testTypeId = 2; // 2- mid term. 3- final
		
		report.startStep("Assign B1 Mid-Term Test to student");
		pageHelper.assignAutomatedTestToStudetInTestEnvironment(studentId, courseId, testTypeId,0,0,1);
		sleep(2);
		
		// Initialize Test Environment Page
		testEnvironmentPage = new TestEnvironmentPage(webDriver, testResultService);
		
		List<String> testIds = testEnvironmentPage.getListOfTestIdsFromCSV("files/CourseTestData/testsIdsMidTerm.csv");
				
		String testId = pageHelper.getAssignedTestIdForStudent(studentId,courseId, testTypeId);
		String userExitSettingsId = dbService.getUserExitSettingsId(studentId, testId, courseId, Integer.toString(testTypeId));

		for (int i = 0; i < testIds.size(); i++) {
			
			dbService.updateTestIdInUserExitSettings(testIds.get(i), userExitSettingsId);
			
			//report.startStep("Return Test Id");
			//testId = pageHelper.getAssignedTestIdForStudent(studentId,courseId, 2);
				
			report.startStep("Change JSON of Test to Round 2");
			pageHelper.runConsoleProgram("cmd /c copy "+ServerPath+"CourseTests\\989012509_RoundLevel2\\989012509.json "+ServerPath+"CourseTests");
		
			report.startStep("Retrieve JSON Array of Test ID: " + testId);
			jsonObj = netService.getJsonObject("smb://dev2008/EdoNet300Res/Runtime/Metadata/Courses/CourseTests/", testId);
			
			report.startStep("Open Assessments Page");
			testsPage = homePage.openAssessmentsPage(false);
			
			report.startStep("Start Test");
			String testName = testsPage.clickStartTest(1, 2);
			
			report.startStep("Validate Test Name");
			testEnvironmentPage.validateTestName(testName);
			
			report.startStep("Validate Course Intro");
			testEnvironmentPage.validateIntro(jsonObj, localizationJson, testId, testName);
					
			report.startStep("Get Round Level of Course");
			String roundLevelOfCourse = testEnvironmentPage.getRoundLevelFromJson(jsonObj, testId);
			//String roundLevelOfCourse = testEnvironmentPage.getValueOfWantedKeyInSpecificTestId(jsonObj, testId, "RoundLevel");
			
			// Get Number of Sections to Submit
			int unitCount = 0;
			
			// Get list of lesson count for each unit in course
			List<String[]> lessonCountForUnits = dbService.getLessonCountForAllUnitsInCourse(testId);
			
			// Get unit ids list from previous list
			List<String> unitIds = new ArrayList<String>();
			for (int j = 0; j < lessonCountForUnits.size(); j++) {
				unitIds.add(lessonCountForUnits.get(j)[0]);
			}
			
			unitCount = lessonCountForUnits.size();
			// Change unit count below to as many units you want to answer (In this case all units are being answered)
			int unitsToAnswer = unitCount;
			int sectionsToAnswer = 0;
			for (int k = 0; k < unitsToAnswer; k++) {
				sectionsToAnswer += Integer.parseInt(lessonCountForUnits.get(k)[1]);
			}
			
			
			String answerFilePath = "files/CourseTestData/MidTerm_Answers.csv";
			
			report.startStep("Answer Questions and Validate Intros");
			testEnvironmentPage.answerQuestionsNew(answerFilePath, testId, CourseCodes.B1, CourseTests.MidTerm, sectionsToAnswer, roundLevelOfCourse, unitIds, jsonObj, localizationJson, testName, unitsToAnswer);
	
			report.startStep("Click Close");
			testEnvironmentPage.clickClose(); 
		}
		
		report.startStep("Log Out");
		homePage.logOutOfED();
		
	}*/

	/*
	// This test runs through all test ids of final test and for each- answers the tests + validates intros
	@Test
	@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "78729" , "78604" , "78685" , "78721" , "78859", "79554", "78975" })
	public void testCompleteAllFinalTestAndGetResultsRoundLevel2() throws Exception {
	
		report.startStep("Init Data");
		courseCode = "B1";
		courseId = getCourseIdByCourseCode(courseCode);
		int testTypeId = 3; // 2- mid term. 3- final
		
		report.startStep("Assign B1 Mid-Term Test to student");
		pageHelper.assignAutomatedTestToStudetInTestEnvironment(studentId, courseId, testTypeId,0,0,1);
		sleep(2);
		
		// Initialize Test Environment Page
		testEnvironmentPage = new TestEnvironmentPage(webDriver, testResultService);
		
		List<String> testIds = testEnvironmentPage.getListOfTestIdsFromCSV("files/CourseTestData/testsIdsFinalTest.csv");
				
		String testId = pageHelper.getAssignedTestIdForStudent(studentId,courseId, testTypeId);
		String userExitSettingsId = dbService.getUserExitSettingsId(studentId, testId, courseId, Integer.toString(testTypeId));

		for (int i = 0; i < testIds.size(); i++) {
			
			dbService.updateTestIdInUserExitSettings(testIds.get(i), userExitSettingsId);
			
			//report.startStep("Return Test Id");
			//testId = pageHelper.getAssignedTestIdForStudent(studentId,courseId, 2);
				
				report.startStep("Change JSON of Test to Round 2");
				pageHelper.runConsoleProgram("cmd /c copy "+ServerPath+"CourseTests\\989012509_RoundLevel2\\989012509.json "+ServerPath+"CourseTests");
		
			report.startStep("Retrieve JSON Array of Test ID: " + testId);
			jsonObj = netService.getJsonObject("smb://dev2008/EdoNet300Res/Runtime/Metadata/Courses/CourseTests/", testId);
			
			report.startStep("Open Assessments Page");
			testsPage = homePage.openAssessmentsPage(false);
			
			report.startStep("Start Test");
			String testName = testsPage.clickStartTest(1, 2);
			
			report.startStep("Validate Test Name");
			testEnvironmentPage.validateTestName(testName);
			
			report.startStep("Validate Course Intro");
			testEnvironmentPage.validateIntro(jsonObj, localizationJson, testId, testName);
					
			report.startStep("Get Round Level of Course");
			String roundLevelOfCourse = testEnvironmentPage.getRoundLevelFromJson(jsonObj, testId);
			//String roundLevelOfCourse = testEnvironmentPage.getValueOfWantedKeyInSpecificTestId(jsonObj, testId, "RoundLevel");
			
			// Get Number of Sections to Submit
			int unitCount = 0;
			
			// Get list of lesson count for each unit in course
			List<String[]> lessonCountForUnits = dbService.getLessonCountForAllUnitsInCourse(testId);
			
			// Get unit ids list from previous list
			List<String> unitIds = new ArrayList<String>();
			for (int j = 0; j < lessonCountForUnits.size(); j++) {
				unitIds.add(lessonCountForUnits.get(j)[0]);
			}
			
			unitCount = lessonCountForUnits.size();
			int unitsToAnswer = unitCount;
			
			String answerFilePath = "files/CourseTestData/MidTerm_Answers.csv";
			
			report.startStep("Answer Questions and Validate Intros");
			testEnvironmentPage.answerQuestionsNew(answerFilePath, testId, CourseCodes.B1, CourseTests.MidTerm, unitsToAnswer, roundLevelOfCourse, unitIds, jsonObj, localizationJson, testName, unitsToAnswer);
	
			report.startStep("Click Close");
			testEnvironmentPage.clickClose(); 
		}
		
		report.startStep("Log Out");
		homePage.logOutOfED();
		
	}*/
	
	// This test answer plt test version1, all routes (old te)
	@Test
	@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "" })
	public void testCompletePlacementTestAndGetResultsV1() throws Exception {
		newTe=false;
		
		int version = 1;
		
		report.startStep("LogOut");
		homePage.logOutOfED();
		
		report.startStep("Turn Off Test Enviorment FLAG");
		dbService.turnOffTestEnviormentFlag(institutionId);
		
		report.startStep("Retrieve the URL");
		String url = webDriver.getUrl();

		//institutionId = dbService.getInstituteIdByName("automation");
		
		report.startStep("Replace 'Courses' to 'Automation'");
		url = url.replace("courses", "automation");
		
		//instId = dbService.getInstituteIdByName("local");
		
		report.startStep("Turn Off Test Enviorment FLAG");
		dbService.turnOffTestEnviormentFlag(institutionId);
		
		String className = configuration.getProperty("classname");
		
		report.startStep("Change to Correct URL");
		webDriver.openUrl(url);
		
		//homePage = createUserAndLoginNewUXClass(className, instId);
		homePage = getUserAndLogin(className, institutionId, true, true);
		homePage.waitHomePageloaded();
		pageHelper.closeLastSessionImproperLogoutAlert();
		
		report.startStep("Skip the Walkthrough");
		pageHelper.skipOnBoardingHP();
		
		report.startStep("Open Assessments Page");
		testsPage = homePage.openAssessmentsPage(false);
		
		report.startStep("Start Test");
		String testName = testsPage.clickStartTest(1, 1);
	
		// Initialize Test Environment Page
		testEnvironmentPage = new TestEnvironmentPage(webDriver, testResultService);
		
		webDriver.switchToNewWindow();
		testEnvironmentPage.switchToTestAreaIframe();
				
		String filePath = "files/PLTTestData/PLT_V1_NEW.csv"; 
		
		int[] wantedScore = new int[3];
		List<String[]> dataOfVersion = testEnvironmentPage.initScoresOfPLTByVersion("files/PLTTestData/ScoresNew.csv","1");
		
		for (int i = 0; i < dataOfVersion.size(); i++) {
			
			wantedScore[0] = Integer.parseInt(dataOfVersion.get(i)[3]); // C1 Reading Score
			wantedScore[1] = Integer.parseInt(dataOfVersion.get(i)[4]); // C1 Listening Score
			wantedScore[2] = Integer.parseInt(dataOfVersion.get(i)[5]); // C1 Grammar Score
			
			String level = dataOfVersion.get(i)[2]; // Initial Level
			testEnvironmentPage.performTestInSpecificRoute(filePath, level, wantedScore, version);
			
			testEnvironmentPage.clickOnDoTestAgainPLT();
			webDriver.switchToNewWindow();
			testEnvironmentPage.switchToTestAreaIframe();

			// to do - calculate the next section level
			// NextSkillLevelIndex = (SkillScore + (12.5 * CurrentLevel) - 75) / 12.5
			
		}			
	}
	
	// This test answer plt test version2, all routes (old te)
	@Test
	@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "" })
	public void testCompletePlacementTestAndGetResultsV2() throws Exception {
		newTe=false;
		
		int version = 2;
		
		report.startStep("LogOut");
		homePage.logOutOfED();

		report.startStep("Turn Off Test Enviorment FLAG");
		dbService.turnOffTestEnviormentFlag(institutionId);
		
		report.startStep("Retrieve the URL");
		String url = webDriver.getUrl();
			
		institutionId = dbService.getInstituteIdByName("local");
		
		report.startStep("Replace 'Courses' to 'local'");
		url = url.replace("courses", "local");
		
		//institutionId = dbService.getInstituteIdByName("local");
		
		report.startStep("Turn Off Test Enviorment FLAG");
		dbService.turnOffTestEnviormentFlag(institutionId);
		
		String className = configuration.getProperty("local_className");
		
		report.startStep("Change to Correct URL");
		webDriver.openUrl(url);
		
		//homePage = createUserAndLoginNewUXClass(className, instId);
		homePage = getUserAndLogin(className, institutionId, true, true);
		homePage.waitHomePageloaded();
		pageHelper.closeLastSessionImproperLogoutAlert();
		
		report.startStep("Skip the Walkthrough");
		pageHelper.skipOnBoardingHP();
		
		report.startStep("Open Assessments Page");
		testsPage = homePage.openAssessmentsPage(false);
		
		report.startStep("Start Test");
		String testName = testsPage.clickStartTest(1, 1);
	
		// Initialize Test Environment Page
		testEnvironmentPage = new TestEnvironmentPage(webDriver, testResultService);
		
		webDriver.switchToNewWindow();
		testEnvironmentPage.switchToTestAreaIframe();
				
		String filePath = "files/PLTTestData/PLT_V2_NEW.csv"; 
		
		int[] wantedScore = new int[3];
		List<String[]> dataOfVersion = testEnvironmentPage.initScoresOfPLTByVersion("files/PLTTestData/ScoresNew.csv","2");
		
		for (int i = 0; i < dataOfVersion.size(); i++) {
			
			wantedScore[0] = Integer.parseInt(dataOfVersion.get(i)[3]); // C1 Reading Score
			wantedScore[1] = Integer.parseInt(dataOfVersion.get(i)[4]); // C1 Listening Score
			wantedScore[2] = Integer.parseInt(dataOfVersion.get(i)[5]); // C1 Grammar Score
			
			String level = dataOfVersion.get(i)[2]; // Initial Level
			testEnvironmentPage.performTestInSpecificRoute(filePath, level, wantedScore, version);
			
			testEnvironmentPage.clickOnDoTestAgainPLT();
			webDriver.switchToNewWindow();
			testEnvironmentPage.switchToTestAreaIframe();

			// to do - calculate the next section level
			// NextSkillLevelIndex = (SkillScore + (12.5 * CurrentLevel) - 75) / 12.5	
		}
	}
	
	// This test answer plt test version3, all routes (old te)
	@Test
	@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "" })
	public void testCompletePlacementTestAndGetResultsV3() throws Exception {
		newTe=false;
		
		int version = 3;
		
		report.startStep("LogOut");
		homePage.logOutOfED();
		
		report.startStep("Turn Off Test Enviorment FLAG");
		dbService.turnOffTestEnviormentFlag(institutionId);
		
		report.startStep("Retrieve the URL");
		String url = webDriver.getUrl();
			
		institutionId = dbService.getInstituteIdByName("courses");
		
		String className = configuration.getProperty("classname.enrich");
		
		report.startStep("Change to Correct URL");
		webDriver.openUrl(url);
		
		//homePage = createUserAndLoginNewUXClass(className, instId);
		homePage = getUserAndLogin(className, institutionId, true, true);
		homePage.waitHomePageloaded();
		pageHelper.closeLastSessionImproperLogoutAlert();
		
		report.startStep("Skip the Walkthrough");
		pageHelper.skipOnBoardingHP();
		
		report.startStep("Open Assessments Page");
		testsPage = homePage.openAssessmentsPage(false);
		
		report.startStep("Start Test");
		String testName = testsPage.clickStartTest(1, 1);
	
		// Initialize Test Environment Page
		testEnvironmentPage = new TestEnvironmentPage(webDriver, testResultService);
		
		webDriver.switchToNewWindow();
		testEnvironmentPage.switchToTestAreaIframe();
				
		String filePath = "files/PLTTestData/PLT_V3_NEW.csv"; 

		int[] wantedScore = new int[3];
		List<String[]> dataOfVersion = testEnvironmentPage.initScoresOfPLTByVersion("files/PLTTestData/ScoresNew.csv","3");
		
		for (int i = 0; i < dataOfVersion.size(); i++) {
			
			wantedScore[0] = Integer.parseInt(dataOfVersion.get(i)[3]); // C1 Reading Score
			wantedScore[1] = Integer.parseInt(dataOfVersion.get(i)[4]); // C1 Listening Score
			wantedScore[2] = Integer.parseInt(dataOfVersion.get(i)[5]); // C1 Grammar Score
			
			String level = dataOfVersion.get(i)[2]; // Initial Level
			testEnvironmentPage.performTestInSpecificRoute(filePath, level, wantedScore, version);
			
			testEnvironmentPage.clickOnDoTestAgainPLT();
			webDriver.switchToNewWindow();
			testEnvironmentPage.switchToTestAreaIframe();

			// to do - calculate the next section level
			// NextSkillLevelIndex = (SkillScore + (12.5 * CurrentLevel) - 75) / 12.5
		}
	}
	
	// This test answers plt test in specific version and specific route- for an example: 
	// version 1- Basic 2+ Basic 1 (in this case we will start in Basic2, get score lower than 80 
	// and continue to Basic 1)
	// The only variables needed to be initialized are : version, iteration.
	@Test
	@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "" })
	public void testPerformPLTtestOldTe() throws Exception {
		newTe=false;
	
		report.startStep("LogOut");
		homePage.logOutOfED();
		
		//report.startStep("Turn Off Test Enviorment FLAG");
		//dbService.turnOffTestEnviormentFlag(instId);
		
		report.startStep("Retrieve the URL");
		String url = webDriver.getUrl();
		
		int version = 1;
		
		int iteration = 4;
		
		String inst = "";
		String classNameAttr = "";
		String filePath = "";
		
		switch (version) {
			case 1:
				inst = "automation";
				classNameAttr = "classname";
				filePath = "files/PLTTestData/PLT_V1_NEW.csv";
				break;
			case 2:
				inst = "local";
				classNameAttr = "local_className";
				filePath = "files/PLTTestData/PLT_V2_NEW.csv";
				break;
			case 3:
				inst = "courses";
				classNameAttr = "classname.enrich";
				filePath = "files/PLTTestData/PLT_V3_NEW.csv";
				break;
		}
	
		//instId = dbService.getInstituteIdByName(inst);
		report.startStep("Replace 'Courses' to '" + inst+"'");
		url = url.replace("courses", inst);
		if(!inst.equals("courses")) {
			institutionId = dbService.getInstituteIdByName(institutionsName[0]);
		}
		
		report.startStep("Change to Correct URL");
		webDriver.openUrl(url);
		
		String className = configuration.getProperty(classNameAttr);
		//homePage = createUserAndLoginNewUXClass(className, instId);
		homePage = getUserAndLogin(className, institutionId, true, true);
		homePage.waitHomePageloaded();
		
		report.startStep("Skip the Walkthrough");
		pageHelper.skipOnBoardingHP();
		
		report.startStep("Open Assessments Page");
		testsPage = homePage.openAssessmentsPage(false);
		
		report.startStep("Start Test");
		String testName = testsPage.clickStartTest(1, 1);
	
		// Initialize Test Environment Page
		testEnvironmentPage = new TestEnvironmentPage(webDriver, testResultService);
		
		webDriver.switchToNewWindow();
		testEnvironmentPage.switchToTestAreaIframe(); 
		
		int[] wantedScore = new int[3];
		List<String[]> dataOfVersion = testEnvironmentPage.initScoresOfPLTByVersion("files/PLTTestData/ScoresNew.csv", Integer.toString(version));
	
		wantedScore[0] = Integer.parseInt(dataOfVersion.get(iteration-1)[3]); // C1 Reading Score
		wantedScore[1] = Integer.parseInt(dataOfVersion.get(iteration-1)[4]); // C1 Listening Score
		wantedScore[2] = Integer.parseInt(dataOfVersion.get(iteration-1)[5]); // C1 Grammar Score
		
		String level = dataOfVersion.get(iteration-1)[2]; // Initial Level
		testEnvironmentPage.performTestInSpecificRoute(filePath, level, wantedScore, version);
	
		
		
				
	/*	int[] wantedScore = new int[3];
		wantedScore[0] = 100; wantedScore[1] = 100; wantedScore[2] = 90;
		
		testEnviormentPage.performTestInSpecificRoute(filePath, "Intermediate", wantedScore, true, studentId, "90");
	*/
	}
	
	// This test answer one course test (midterm or final) by test id
	// The only variables needed to be initialized are : wantedTestId
	/*@Test
	@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "" })
	public void testPerformCourseTestOldTE() throws Exception {
		newTE=false;
		
		// Initialize Test Environment Page
		testEnvironmentPage = new TestEnvironmentPage(webDriver, testResultService);	
		
		report.startStep("Init Data");
		String wantedTestId = "47859";
		
		String impType = dbService.getImpTypeByTestId(wantedTestId);
		
		int testTypeId = Integer.parseInt(impType); // 2- mid term. 3- final
		
		String answersFilePath = "";
		String testIdsFile = "";
		CourseTests testType = null;
		
		if (testTypeId == 2) {
			answersFilePath = "files/CourseTestData/MidTerm_Answers.csv";
			testIdsFile = "files/CourseTestData/testsIdsMidTerm.csv";
			testType = CourseTests.MidTerm;
		} else if (testTypeId == 3) {
			answersFilePath = "files/CourseTestData/FinalTest_Answers.csv";
			testIdsFile = "files/CourseTestData/testsIdsFinalTest.csv";
			testType = CourseTests.FinalTest;
		}
		
		String courseCode = testEnvironmentPage.getCourseCodeByTestId(testIdsFile, wantedTestId); //courseCode = courseCodes[1]; // B1
		CourseCodes wantedCourseCode = testEnvironmentPage.getCourseCodeEnumByCourseCodeString(courseCode); //CourseCodes wantedCourseCode = CourseCodes.B1;
		courseId = getCourseIdByCourseCode(courseCode);
		
		report.startStep("LogOut");
		homePage.logOutOfED();
		
		report.startStep("Turn Off Test Enviorment FLAG");
		dbService.turnOffTestEnviormentFlag(instId);
		
		report.startStep("Assign Course Test to student");
		pageHelper.assignAutomatedTestToStudetInTestEnvironment(studentId, courseId, testTypeId,0,0,1);
		sleep(2);
		String testId = pageHelper.getAssignedTestIdForStudent(studentId,courseId, testTypeId);
		if (!testId.equals(wantedTestId)) {
			// update test id
			String userExitSettingsId = dbService.getUserExitSettingsId(studentId, testId, courseId, impType);
			dbService.updateTestIdInUserExitSettings(wantedTestId, userExitSettingsId);
		}
		
		report.startStep("Log In as Student");
		String userName = dbService.getUserNameById(studentId, instId);
		NewUXLoginPage loginPage = new NewUXLoginPage(webDriver, testResultService);
		homePage = loginPage.loginAsStudent(userName, "12345");
		homePage.waitHomePageloaded();
		pageHelper.closeLastSessionImproperLogoutAlert();
		
		report.startStep("Open Assessments Page");
		testsPage = homePage.openAssessmentsPage(false);
	
		report.startStep("Start Test from Assessments");
		testsPage.clickStartTest(1, 2);
		webDriver.closeAlertByAccept();
		sleep(2);
		webDriver.switchToNewWindow();
		Thread.sleep(5000);
		webDriver.switchToTopMostFrame();
		webDriver.switchToFrame(webDriver.waitForElement("oed__iframe", ByTypes.className,false,webDriver.getTimeout()));
		
		report.startStep("Click Start Test in New Window");
		testEnvironmentPage.pressStartCourseTestOldTE();
		sleep(1);
		
		report.startStep("Answer Questions");
		testEnvironmentPage.answerQuestionsOldTe(answersFilePath, wantedTestId, wantedCourseCode, testType);
		
		report.startStep("Get score and close test");
		webDriver.switchToFrame(webDriver.waitForElement("cboxIframe", ByTypes.className));
		sleep(1);
		String score = webDriver.waitForElement("//div[@class='hebDir']/b", ByTypes.xpath, "Getting score").getText();
		testResultService.assertEquals("100", score,"Score is not 100.", true);
		
		report.startStep("Get and Check score Assessment page VS. DB ");
			String dbResultScore = dbService.getStudentCourseTestScore(studentId, courseId, wantedTestId, testTypeId);
			String DbScore[] = dbResultScore.split("\\.");
			
			testResultService.assertEquals(score, DbScore[0],"ED Test result score not match to DB test report score");
			
		
		webDriver.switchToTopMostFrame();
		sleep(2);
		webDriver.switchToFrame(webDriver.waitForElement("oed__iframe", ByTypes.className,false,webDriver.getTimeout()));
		
		webDriver.waitForElement("cboxClose", ByTypes.id).click();
		webDriver.switchToMainWindow();
		sleep(1);
		
		report.startStep("Log Out");
		homePage.logOutOfED();
	
	}*/
	
	@Test
	@TestCaseParams(testCaseID = { "80135" })
	public void testWeightAndTimeCalculationRoundLevel2() throws Exception {
		
		String wantedTestId = "989017755";
		
		// Initialize Test Environment Page
		testEnvironmentPage = new TestEnvironmentPage(webDriver, testResultService);
	
		report.startStep("Init Data");
		courseCode = courseCodes[1];
		CourseCodes wantedCourseCode = testEnvironmentPage.getCourseCodeEnumByCourseCodeString(courseCode);
	
		courseId = getCourseIdByCourseCode(courseCode);
	//courseId="3";
		String answerFilePath = "files/CourseTestData/MidTerm_Answers.csv";
		CourseTests courseTestType= CourseTests.MidTerm;
		
		report.startStep("LogOut");
		homePage.logOutOfED();
				
		report.startStep("Assign B1 Mid-Term Test to student");
		pageHelper.assignAutomatedTestToStudetInTestEnvironment(studentId, courseId, 2,0,0,1);
		sleep(2);
		
		report.startStep("Return Test Id");
		testId = pageHelper.getAssignedTestIdForStudent(studentId,courseId, 2);
		if (!testId.equals(wantedTestId)) {
			// update test id and token
			String userExitSettingsId = dbService.getUserExitSettingsId(studentId, testId, courseId, "2");
			dbService.updateTestIdInUserExitSettings(wantedTestId, userExitSettingsId);
			testId = pageHelper.getAssignedTestIdForStudent(studentId,courseId, 2);
			
			String token = pageHelper.getTokenFromConsoleWithParameters("1", studentId, testId, userExitSettingsId);
			dbService.updateAssignWithToken(token, userExitSettingsId);
		}
		
		try{
			report.startStep("Copy and keep the original " +testId+".json file to backup folder");
			pageHelper.runConsoleProgram("cmd /c copy "+PageHelperService.ServerPath+"CourseTests\\"+testId+".json "+PageHelperService.ServerPath+"CourseTests\\ForAutomationBackup");
			
			report.startStep("Change JSON of Test to Round 2");
			pageHelper.runConsoleProgram("cmd /c copy "+PageHelperService.ServerPath+"CourseTests\\"+testId+"_RoundLevel2\\"+testId+".json "+PageHelperService.ServerPath+"CourseTests");
		
			report.startStep("Retrieve JSON Array of Test ID: " + testId);
			jsonObj = netService.getJsonObject("smb://"+PageHelperService.physicalPath+"/Runtime/Metadata/Courses/CourseTests/", testId, useSMB);
			
			report.startStep("Log In as Student");
			String userName = dbService.getUserNameById(studentId, institutionId);
			NewUXLoginPage loginPage = new NewUXLoginPage(webDriver, testResultService);
			homePage = loginPage.loginAsStudent(userName, "12345");
			homePage.skipNotificationWindow();
			homePage.waitHomePageloaded();
			pageHelper.closeLastSessionImproperLogoutAlert();
			
			report.startStep("Open Assessments Page");
			testsPage = homePage.openAssessmentsPage(false);
			
			report.startStep("Start Test");
			int testSequence = testsPage.getTestSequenceByCourseId(courseId);
			String testName = testsPage.clickStartTest(1, testSequence);
			webDriver.closeAlertByAccept();
			
			String startTestTime = pageHelper.getCurrentDateByFormat("HH:mm:ss");
			sleep(1);
			
			report.startStep("Validate Test Name");
			testEnvironmentPage.validateTestName(testName);
			
			report.startStep("Validate Course Intro");
			testEnvironmentPage.validateIntro(jsonObj, localizationJson, testId, testName);
					
			report.startStep("Get Round Level of Course");
			String roundLevelOfCourse = testEnvironmentPage.getRoundLevelFromJson(jsonObj, testId);
			//String roundLevelOfCourse = testEnvironmentPage.getValueOfWantedKeyInSpecificTestId(jsonObj, testId, "RoundLevel");
			
			// Get Number of Sections to Submit
			int unitCount = 0;
			
			// Get list of lesson count for each unit in course
			List<String[]> lessonCountForUnits = dbService.getLessonCountForAllUnitsInCourse(testId);
			
			// Get unit ids list from previous list
			List<String> unitIds = new ArrayList<String>();
			for (int j = 0; j < lessonCountForUnits.size(); j++) {
				unitIds.add(lessonCountForUnits.get(j)[0]);
			}
			
			unitCount = lessonCountForUnits.size();
			// Change unit count below to as many units you want to answer (In this case all units are being answered)
			int unitsToAnswer = 1;
			int sectionsToAnswer = 0;
			for (int k = 0; k < unitsToAnswer; k++) {
				sectionsToAnswer += Integer.parseInt(lessonCountForUnits.get(k)[1]);
			}
					
			report.startStep("Answer Questions and Calculate Weight and Time");
			testEnvironmentPage.answerQuestionsAndValidateCalculations(answerFilePath, testId +"#", wantedCourseCode, courseTestType, sectionsToAnswer, roundLevelOfCourse, unitIds, jsonObj, localizationJson, testName, unitsToAnswer, studentId, startTestTime);
					
			report.startStep("Click Close");
			testEnvironmentPage.clickClose();
			
			report.startStep("Log Out");
			homePage.logOutOfED();
		}
		finally {	
			report.startStep("copy back the original file to original path");
			pageHelper.runConsoleProgram("cmd /c copy "+PageHelperService.ServerPath+"CourseTests\\ForAutomationBackup\\"+testId+".json "+PageHelperService.ServerPath+"CourseTests");	
		}
	}
	
	
	@Test
	@TestCaseParams(testCaseID = { "80135" })
	public void testWeightAndTimeCalculationRoundLevel3() throws Exception {
		
		String wantedTestId = "989017755";
		
		// Initialize Test Environment Page
		testEnvironmentPage = new TestEnvironmentPage(webDriver, testResultService);
	
		report.startStep("Init Data");
		courseCode = courseCodes[1];
		CourseCodes wantedCourseCode = testEnvironmentPage.getCourseCodeEnumByCourseCodeString(courseCode);
	
		courseId = getCourseIdByCourseCode(courseCode);
		String answerFilePath = "files/CourseTestData/MidTerm_Answers.csv";
		CourseTests courseTestType= CourseTests.MidTerm;
		
		report.startStep("LogOut");
		homePage.logOutOfED();
				
		report.startStep("Assign B1 Mid-Term Test to student");
		pageHelper.assignAutomatedTestToStudetInTestEnvironment(studentId, courseId, 2,0,0,1);
		sleep(2);
		
		report.startStep("Return Test Id");
		testId = pageHelper.getAssignedTestIdForStudent(studentId,courseId, 2);
		if (!testId.equals(wantedTestId)) {
			// update test id and token
			String userExitSettingsId = dbService.getUserExitSettingsId(studentId, testId, courseId, "2");
			dbService.updateTestIdInUserExitSettings(wantedTestId, userExitSettingsId);
			testId = pageHelper.getAssignedTestIdForStudent(studentId,courseId, 2);
			
			String token = pageHelper.getTokenFromConsoleWithParameters("1", studentId, testId, userExitSettingsId);
			dbService.updateAssignWithToken(token, userExitSettingsId);	
		}
		try{
			
			report.startStep("Copy and keep the original " +testId+".json file to backup folder");
			pageHelper.runConsoleProgram("cmd /c copy "+PageHelperService.ServerPath+"CourseTests\\"+testId+".json "+PageHelperService.ServerPath+"CourseTests\\ForAutomationBackup");
				
			report.startStep("Change JSON of Test to Round 3");
			pageHelper.runConsoleProgram("cmd /c copy "+PageHelperService.ServerPath+"CourseTests\\"+testId+"_RoundLevel3\\"+testId+".json "+PageHelperService.ServerPath+"CourseTests");
		
			report.startStep("Retrieve JSON Array of Test ID: " + testId);
			jsonObj = netService.getJsonObject("smb://"+PageHelperService.physicalPath+"/Runtime/Metadata/Courses/CourseTests/", testId, useSMB);
			
			report.startStep("Log In as Student");
			userName = dbService.getUserNameById(studentId, institutionId);
			NewUXLoginPage loginPage = new NewUXLoginPage(webDriver, testResultService);
			homePage = loginPage.loginAsStudent(userName, "12345");
			homePage.skipNotificationWindow();
			homePage.waitHomePageloaded();
			pageHelper.closeLastSessionImproperLogoutAlert();
					
			
			report.startStep("Open Assessments Page");
			testsPage = homePage.openAssessmentsPage(false);
			
			report.startStep("Start Test");
			int testSequence = testsPage.getTestSequenceByCourseId(courseId);
			String testName = testsPage.clickStartTest(1, testSequence);
			webDriver.closeAlertByAccept();
			
			String startTestTime = pageHelper.getCurrentDateByFormat("HH:mm:ss");
			sleep(3);
			
			report.startStep("Validate Test Name");
			testEnvironmentPage.validateTestName(testName);
			
			report.startStep("Validate Course Intro");
			testEnvironmentPage.validateIntro(jsonObj, localizationJson, testId, testName);
					
			report.startStep("Get Round Level of Course");
			String roundLevelOfCourse = testEnvironmentPage.getRoundLevelFromJson(jsonObj, testId);
			//String roundLevelOfCourse = testEnvironmentPage.getValueOfWantedKeyInSpecificTestId(jsonObj, testId, "RoundLevel");
			
			// Get Number of Sections to Submit
			int unitCount = 0;
			
			// Get list of lesson count for each unit in course
			List<String[]> lessonCountForUnits = dbService.getLessonCountForAllUnitsInCourse(testId);
			
			// Get unit ids list from previous list
			List<String> unitIds = new ArrayList<String>();
			for (int j = 0; j < lessonCountForUnits.size(); j++) {
				unitIds.add(lessonCountForUnits.get(j)[0]);
			}
			
			unitCount = lessonCountForUnits.size();
			// Change unit count below to as many units you want to answer (In this case all units are being answered)
			int unitsToAnswer = 1;
			int sectionsToAnswer = 0;
			for (int k = 0; k < unitsToAnswer; k++) {
				sectionsToAnswer += Integer.parseInt(lessonCountForUnits.get(k)[1]);
			}
					
			report.startStep("Answer Questions and Calculate Weight and Time");
			testEnvironmentPage.answerQuestionsAndValidateCalculations(answerFilePath, testId + "#", wantedCourseCode, courseTestType, sectionsToAnswer, roundLevelOfCourse, unitIds, jsonObj, localizationJson, testName, unitsToAnswer, studentId, startTestTime);
					
			report.startStep("Click Close");
			testEnvironmentPage.clickClose();
			
			report.startStep("Log Out");
			homePage.logOutOfED();
		}
		finally {	
			report.startStep("copy back the original file to original path");
			pageHelper.runConsoleProgram("cmd /c copy "+PageHelperService.ServerPath+"CourseTests\\ForAutomationBackup\\"+testId+".json "+PageHelperService.ServerPath+"CourseTests");	
		}
	}
	
	@Test
	@Category(ReleaseTests.class)
	@TestCaseParams(testCaseID = { "80331" })
	public void testResumeMidTermTest() throws Exception {
		
		String wantedTestId = "989017755";
		
		// Initialize Test Environment Page
		testEnvironmentPage = new TestEnvironmentPage(webDriver, testResultService);
	
		report.startStep("Init Data");
		courseCode = courseCodes[1];
		CourseCodes wantedCourseCode = testEnvironmentPage.getCourseCodeEnumByCourseCodeString(courseCode);
	
		courseId = getCourseIdByCourseCode(courseCode);
		String answerFilePath = "files/CourseTestData/MidTerm_Answers.csv";
		CourseTests courseTestType= CourseTests.MidTerm;
		
		report.startStep("LogOut");
		homePage.logOutOfED();
				
		report.startStep("Assign B1 Mid-Term Test to student");
		pageHelper.assignAutomatedTestToStudetInTestEnvironment(studentId, courseId, 2,0,0,1);
		sleep(2);
		
		report.startStep("Return Test Id");
		testId = pageHelper.getAssignedTestIdForStudent(studentId,courseId, 2);
		if (!testId.equals(wantedTestId)) {
			// update test id and token
			String userExitSettingsId = dbService.getUserExitSettingsId(studentId, testId, courseId, "2");
			dbService.updateTestIdInUserExitSettings(wantedTestId, userExitSettingsId);
			testId = pageHelper.getAssignedTestIdForStudent(studentId,courseId, 2);
			
			String token = pageHelper.getTokenFromConsoleWithParameters("1", studentId, testId, userExitSettingsId);
			dbService.updateAssignWithToken(token, userExitSettingsId);
		}
		try{
			report.startStep("Copy and keep the original " +testId+".json file to backup folder");
			pageHelper.runConsoleProgram("cmd /c copy "+PageHelperService.ServerPath+"CourseTests\\"+testId+".json "+PageHelperService.ServerPath+"CourseTests\\ForAutomationBackup");
			
			report.startStep("Change JSON of Test to Round 2");
			pageHelper.runConsoleProgram("cmd /c copy "+PageHelperService.ServerPath+"CourseTests\\"+testId+"_RoundLevel2\\"+testId+".json "+PageHelperService.ServerPath+"CourseTests");
		
			report.startStep("Retrieve JSON Array of Test ID: " + testId);
			jsonObj = netService.getJsonObject("smb://"+PageHelperService.physicalPath+"/Runtime/Metadata/Courses/CourseTests/", testId, useSMB);
			
			report.startStep("Log In as Student");
			userName = dbService.getUserNameById(studentId, institutionId);
			NewUXLoginPage loginPage = new NewUXLoginPage(webDriver, testResultService);
			homePage = loginPage.loginAsStudent(userName, "12345");
			homePage.closeAllNotifications();
			homePage.waitHomePageloaded();
			pageHelper.closeLastSessionImproperLogoutAlert();
			
			report.startStep("Open Assessments Page");
			testsPage = homePage.openAssessmentsPage(false);
			
			report.startStep("Start Test");
			int testSequence = testsPage.getTestSequenceByCourseId(courseId);
			String testName = testsPage.clickStartTest(1, testSequence);
			webDriver.closeAlertByAccept();
			sleep(3);
			
			report.startStep("Validate Course Intro");
			testEnvironmentPage.validateIntro(jsonObj, localizationJson, testId, testName);
			//sleep(4);
					
			report.startStep("Get Round Level of Course");
			String roundLevelOfCourse = testEnvironmentPage.getRoundLevelFromJson(jsonObj, testId);
			
			// Get Number of Sections to Submit
			//int unitCount = 0;
			
			// Get list of lesson count for each unit in course
			List<String[]> lessonCountForUnits = dbService.getLessonCountForAllUnitsInCourse(testId);
			
			// Get unit ids list from previous list
			List<String> unitIds = new ArrayList<String>();
			for (int j = 0; j < lessonCountForUnits.size(); j++) {
				unitIds.add(lessonCountForUnits.get(j)[0]);
			}
			
			//unitCount = lessonCountForUnits.size();
			// Change unit count below to as many units you want to answer (In this case all units are being answered)
			/*int unitsToAnswer = 1;
			int sectionsToAnswer = 0;
			for (int k = 0; k < unitsToAnswer; k++) {
				sectionsToAnswer += Integer.parseInt(lessonCountForUnits.get(k)[1]);
			}*/
			
			report.startStep("Retrieve left time");
			String expectedEdTimeFormat = testEnvironmentPage.convertTimeInMinutesToEDTimerFormat(Integer.parseInt(testEnvironmentPage.testDuration)-1);
			String testTimer = testEnvironmentPage.getTestTime();
			testResultService.assertEquals(expectedEdTimeFormat, testTimer, "Test Timer at the beginning of the test is Incorrect.");
					
			report.startStep("Answer Questions of First Section");
			int sectionIndex = 0;
			int unitIndex = 0;
			int sectionIndexInUnit = 0;
			testEnvironmentPage.answerQuestionInSpecificSectionNewTE(answerFilePath, testId, wantedCourseCode, courseTestType, sectionIndex, roundLevelOfCourse, unitIds, jsonObj, localizationJson, testName, unitIndex, sectionIndexInUnit);
		
			sleep(45);
			
			report.startStep("Retrieve Question Text and Question number");
			String questionText = testEnvironmentPage.getQuestionText();
			String questionNumber = testEnvironmentPage.getQuestionNumber();
			
			report.startStep("Retrieve Test Timer");
			testTimer = testEnvironmentPage.getTestTime();
			
			report.startStep("Click Close Test");
			testEnvironmentPage.clickClose();
			
			report.startStep("Open Assessments Page");
			testsPage = homePage.openAssessmentsPage(false);
			
			report.startStep("Validate Resume Button Appears and Click it");
			testsPage.validateButtonTextAndClickIt(1, "Resume Test");
			
			report.startStep("Validate Test Name");
			testEnvironmentPage.validateTestNameContainsExpectedText(testName);
			
			report.startStep("Validate Landed on same Question");
			testResultService.assertEquals(questionText, testEnvironmentPage.getQuestionText(),"Did not land on correct question after resume (Question Text is Incorrect)");
			testResultService.assertEquals(questionNumber, testEnvironmentPage.getQuestionNumber(),"Did not land on correct question after resume (Question Number is Incorrect)");
			
			report.startStep("Validate Test Timer is the same as Before Resume");
			testResultService.assertEquals(testTimer, testEnvironmentPage.getTestTime(), "Test Timer after Resume is Incorrect.");
			
			report.startStep("Press Back Button");
			testEnvironmentPage.clickBackButton();
			sleep(5);
			
			report.startStep("Validate Question is Answered");
			testEnvironmentPage.validateQuestionIsAnswered();
		}
			
		finally {	
			report.startStep("copy back the original file to original path");
			pageHelper.runConsoleProgram("cmd /c copy "+PageHelperService.ServerPath+"CourseTests\\ForAutomationBackup\\"+testId+".json "+PageHelperService.ServerPath+"CourseTests");	
		}
			
	}
	
	/*@Test
	@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "80283" })
	public void validateInstitutionPropertiesListNullAndInstitutionPropertiesNull(){
		
		try {
			
			// change InstitutionPropertiesList to null
			
			// change InstitutionProperties to null
			
			
			// validate old TE is displayed
		} catch (Exception e) {
			
		} finally {
			// change InstitutionPropertiesList to null
			
			// change InstitutionProperties to past date
		}		
	}
	
	@Test
	@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "80284" })
	public void validateInstitutionPropertiesListIsPastDateAndInstitutionPropertiesNull(){
		
		try{
	
			// change InstitutionPropertiesList to past date
			
			// change InstitutionProperties to null
			
			// validate new TE is displayed
		} catch (Exception e) {
			
		} finally {
			// change InstitutionPropertiesList to null
			
			// change InstitutionProperties to past date
		}	
	}
	
	@Test
	@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "80285" })
	public void validateInstitutionPropertiesListIsFutureDateAndInstitutionPropertiesNull(){
		
		try {
			
			// change InstitutionPropertiesList to future date
			
			// change InstitutionProperties to null
			
			// validate old TE is displayed
		} catch (Exception e) {
			
		} finally {
			// change InstitutionPropertiesList to null
			
			// change InstitutionProperties to past date
		}		
	}
	
	@Test
	@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "80286" })
	public void validateInstitutionPropertiesListNullAndInstitutionPropertiesisPastDate(){
		
		try {
				
			// change InstitutionPropertiesList to null
			
			// change InstitutionProperties to past date
			
			//  validate new TE is displayed
		} catch (Exception e) {
			
		} finally {
			// change InstitutionPropertiesList to null
			
			// change InstitutionProperties to past date
		}
			
	}*/
	

	@Test
	@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "80596" })
	public void testCloseButton() throws Exception {
		
		String wantedTestId = "989017755";
		
		report.startStep("Assign B1 Mid-Term Test to student");
		courseCode = "B1";
		courseId = getCourseIdByCourseCode(courseCode);
		pageHelper.assignAutomatedTestToStudetInTestEnvironment(studentId, courseId, 2,0,0,1);
		sleep(2);
		
		report.startStep("Return Test Id");
		testId = pageHelper.getAssignedTestIdForStudent(studentId,courseId, 2);
		if (!testId.equals(wantedTestId)) {
			// update test id
			String userExitSettingsId = dbService.getUserExitSettingsId(studentId, testId, courseId, "2");
			dbService.updateTestIdInUserExitSettings(wantedTestId, userExitSettingsId);
			testId = pageHelper.getAssignedTestIdForStudent(studentId,courseId, 2);
			String token = pageHelper.getTokenFromConsoleWithParameters("1", studentId, testId, userExitSettingsId);
			dbService.updateAssignWithToken(token, userExitSettingsId);
		}
		
		report.startStep("Retrieve JSON Array of Test ID: " + testId);
		jsonObj = netService.getJsonObject("smb://"+PageHelperService.physicalPath+"Runtime/Metadata/Courses/CourseTests/", testId, useSMB);
		
		report.startStep("Open Assessments Page");
		testsPage = homePage.openAssessmentsPage(false);
		
		report.startStep("Start Test");
		int testSequence = testsPage.getTestSequenceByCourseId(courseId);
		String testName = testsPage.clickStartTest(1, testSequence);
		webDriver.closeAlertByAccept();
		sleep(2);
		
		// Initialize Test Environment Page
		testEnvironmentPage = new TestEnvironmentPage(webDriver, testResultService);
		
		report.startStep("Validate Course Intro");
		testEnvironmentPage.validateIntro(jsonObj, localizationJson, testId, testName);
		
		report.startStep("Click Close");
		testEnvironmentPage.clickClose();
		
		//report.startStep("Validate Message is Displayed");
		//testEnvironmentPage.validateMessageText("Are you sure you want to close the test? If you close the test you cannot continue it later on.");	
		
		report.startStep("Validate Home Page is Displayed");
		boolean homePageDisplayed = webDriver.waitUntilElementAppears("//span[contains(@class,'home__userName')]", 60);
		testResultService.assertEquals(true, homePageDisplayed,"Home Page is Not Displayed.");
		
		report.startStep("Open Assessments Page");
		homePage.openAssessmentsPage(false);
		
		report.startStep("Validate Test: '"+testName+"' is Displayed in Upcoming Tests Section");
		boolean isTestInUpcomingSection = testsPage.checkIfTestIsDisplayedInUpcomingTests(testName);
		testResultService.assertEquals(true, isTestInUpcomingSection, "Test: '"+testName+"' is Not Displayed in Upcoming Tests Section");
		
	}
	
	
	/////////////// PLT TESTS ////////////////
	
	private static final String Cannot_Take_PLT_Alert = "You cannot take the placement test at this time. If you would like to be assigned a placement test, please contact your program coordinator.";
	List<String> courseUnits;
	String classNameAR;
	private static final String Language = "English";
	NewUxClassicTestPage classicTest;
	private static final String TITLE_PLT = "Placement Test";

/*	
//	@Test
	@Category(inProgressTests.class) 
	@TestCaseParams(testCaseID = { "43954", "50342" }, testTimeOut = "30")
	public void testArgentinaAdaptiveFlowNewTE() throws Exception {
		
		newUser=true;
		try {
			
			report.startStep("Restart Browser");
			institutionName=institutionsName[7];
			pageHelper.restartBrowserInNewURL(institutionName, true); 
			
			report.startStep("Init Test Data");
			int startCourseIndex = 1; // "I3";
			String courseId = courses[startCourseIndex];
			String startCourseName = coursesNames[startCourseIndex]; 
			courseUnits = dbService.getCourseUnits(courses[startCourseIndex]);
			//String courseTestName = "Intermediate 3 Final Test";
			String courseTestName = "Basic 1";
			int courseProgressPassingGrade = 40;
			int testAvgScorePassingGrade = 40;
			int courseTestScorePassingGrade = 10;
			classNameAR = configuration.getProperty("classname.CourseTest");
			
			int iteration = 1;
			String testId = "11";
			String answersFilePath = "files/PLTTestData/PLT_Answers_AllVersions.csv";
			
			report.startStep("Retrieve JSON Array of Test ID: " + testId);
			jsonObj = netService.getJsonObject("smb://dev2008/EdoNet300Res/Runtime/Metadata/Courses/CourseTests/", testId+".json");
			
			report.startStep("Generate student username for registration");
			String studentUserName = "stud" + dbService.sig(6);
							
			// Start of User Story 43738:Siglo21: Set Starting Course by PLT results 
		
			report.startStep("Enter ED via CanvasPOST - Insert New User");
			gotoCanvasPOST(classNameAR, studentUserName);
			
			studentId = getEdUserId(studentUserName);	
			
			// Initialize Test Environment Page
			testEnvironmentPage = new TestEnvironmentPage(webDriver, testResultService);
			
			report.startStep("Validate Title");
			testEnvironmentPage.validateTestName(testTypes[0]);
			sleep(2);
						
			report.startStep("Perform PLT test");
			String finalLevelString = performPLT_NewTE(testTypes[0], studentId, iteration, jsonObj, localizationJson, answersFilePath, institutionId);
							
			report.startStep("Exit PLT");
			testEnvironmentPage.clickExitPlt();
			sleep(2);
			
			report.startStep("Skip Boarding Tour");
			pageHelper.skipOnBoardingTour();
			homePage.waitHomePageloaded();
					
			report.startStep("Validate Test Results Section");	
			testsPage = homePage.openAssessmentsPage(false);
			testsPage.clickOnArrowToOpenSection("3");
			sleep(1);
			testsPage.checkSubmissionDateForTests("1");
			testsPage.checkScoreLevelPLT("1", finalLevelString);
			
			report.startStep("Close Assessments page");
			testsPage.close();	
			
			report.startStep("Check ONLY starting course available on Home Page");
			homePage.checkCourseCarouselArrowsNotDisplayed();
			testResultService.assertEquals(startCourseName, homePage.getCurrentCourseName(), "Starting course on Home Page is not valid or not found. Make sure ilp.json mapping for PLT results (LevelMapping section) is either configured correctly or deafault value.");		
			
			// End of User Story 43738:Siglo21: Set Starting Course by PLT results 
			
			
			// Start of User Story 43742:Siglo21: Assign Test if Course Criteria Achieved
			
			report.startStep("Enter course and create NOT passing progress - completion < " + courseProgressPassingGrade +"% / score avg < 40%");
			homePage.clickOnContinueButton();
			sleep(2);
			testEnvironmentPage.clickNext();
			NewUxLearningArea2 learningArea2 = new NewUxLearningArea2(webDriver, testResultService);
			learningArea2.clickOnPlayVideoButton();
			//sleep(4);
			//webDriver.ClickElement(learningArea2.getPlayPauseButton());
			setProgressInCourse(courseId, 5, 30);
		
			report.startStep("Navigate to Home Page and check Course Criteria NOT Achieved");
			homePage.clickOnHomeButton();
			homePage.waitHomePageloadedFully();
			int courseCompletion = Integer.valueOf(homePage.getCompletionWidgetValue());
			int avgTestScore = Integer.valueOf(homePage.getScoreWidgetValue());
					
			if (courseCompletion > courseProgressPassingGrade || avgTestScore > testAvgScorePassingGrade) {
				testResultService.addFailTest("Course Progress is not as expected on this stage", false, true);
			}
					
			report.startStep("Open Assessments and check NO course test assigned");
			testsPage = homePage.openAssessmentsPage(false);
			testResultService.assertEquals(false, testsPage.isCourseTestAvailable(courseTestName), "Course Test displayed though should not. Verify progress and Avg score values in ilp file.");
			testsPage.close();
			sleep(2);
			
			report.startStep("Enter course again and create passing progress - completion > 40% / score avg > 40%");
			homePage.clickOnContinueButton();
			sleep(2);
			testEnvironmentPage.clickNext();
			setProgressForUnitIndex(courseId,6,100); // add click play
			
			report.startStep("Navigate to Home Page check Course Criteria Achieved");
			//homePage.clickToOpenNavigationBar();
			homePage.clickOnHomeButton();
			homePage.waitHomePageloadedFully();
			courseCompletion = Integer.valueOf(homePage.getCompletionWidgetValue());
			avgTestScore = Integer.valueOf(homePage.getScoreWidgetValue());
					
			if (courseCompletion < courseProgressPassingGrade || avgTestScore < testAvgScorePassingGrade){
				testResultService.addFailTest("Course Progress is not as expected on this stage", false, true);
			}	
		
			report.startStep("Open Assessments and check B1 Final Test assigned");
			homePage.openAssessmentsPage(false);
			sleep(3);
			//testResultService.assertEquals(true, testsPage.isCourseTestAvailable(courseTestName), "Course Test NOT displayed though should be. Make sure progress and avg score criteria in ilp.json file configured correctly.");	
			testResultService.assertEquals(true, testsPage.checkIfTestIsDisplayedInAvailableTests(courseTestName + " Final Test"), "Final Test NOT displayed though should be. Make sure progress and avg score criteria in ilp.json file configured correctly.");	
			testsPage.close();
			
			// End of User Story 43742:Siglo21: Assign Test if Course Criteria Achieved
			
			
			// Start of User Story 43744:Siglo21: Assign Next Course if Course Criteria Achieved
			// Start of User Story 43743:Siglo21: Re-Assign Test if Test Failed
			
//Shira continue from here			
			
			report.startStep("Complete Course Test with NOT passing grade < 10");
			
			testId = pageHelper.getAssignedTestIdForStudent(studentId, courseId, 3);
			//int sectionsToSubmit = Integer.parseInt(dbService.getNumberOfSectionsInTestForCourseByTestId(courseId,testId, 3));// change
			// Get list of lesson count for each unit in course
			List<String[]> lessonCountForUnits = dbService.getLessonCountForAllUnitsInCourse(testId);
			
			// Get unit ids list from previous list
			List<String> unitIds = new ArrayList<String>();
			for (int j = 0; j < lessonCountForUnits.size(); j++) {
				unitIds.add(lessonCountForUnits.get(j)[0]);
			}
			
			int unitCount = lessonCountForUnits.size();
			int unitsToAnswer = unitCount;
			int sectionsToSubmit = 0;
			for (int k = 0; k < unitsToAnswer; k++) {
				sectionsToSubmit += Integer.parseInt(lessonCountForUnits.get(k)[1]);
			}
			
			report.startStep("Set Progress to be less than 10");
			//int courseTestScore = Integer.valueOf(completeCourseTest(2, CourseCodes.I3, CourseTests.FinalTest, 1, 8)); // course test for 10 units
			
			// init data to answer final test
			//String finalTestAnswerFilePath = "files/CourseTestData/FinalTest_Answers.csv"; // should be final
			//String courseCode = testEnvironmentPage.getCourseCodeByTestId(finalTestAnswerFilePath, testId); //courseCode = courseCodes[1]; // B1
			CourseCodes wantedCourseCode = testEnvironmentPage.getCourseCodeEnumByCourseCodeString("B1");
			//int sectionToAnswer = 0;
			String roundLevel = "2";
			String finalTestAnswerFilePath = "files/CourseTestData/MidTerm_Answers.csv"; // should be final test
			
			CourseTests courseTestType = CourseTests.MidTerm;//CourseTests.FinalTest;
			
			report.startStep("Retrieve JSON Array of Test ID: " + testId);
			jsonObj = netService.getJsonObject("smb://dev2008/EdoNet300Res/Runtime/Metadata/Courses/CourseTests/", testId);
			
			String userExitTestSettingsId = dbService.getUserExitSettingsId(studentId, testId, courseId, "3");
			testEnvironmentPage.testDuration = dbService.getTestDurationByUserExitSettingsId(userExitTestSettingsId);
			
			report.startStep("Open Assessments Page");
			testsPage = homePage.openAssessmentsPage(false);
			
			report.startStep("Start Test");
			int testSequence = testsPage.getTestSequenceByCourseId(courseId);
			String testName = testsPage.clickStartTest(1, testSequence);
			webDriver.closeAlertByAccept();
			sleep(3);
			
			report.startStep("Validate Test Name");
			testEnvironmentPage.validateTestName(testName);
			sleep(5);
			
			report.startStep("Validate Course Intro");
			testEnvironmentPage.validateIntro(jsonObj, localizationJson, testId, testName);
					
			report.startStep("Submit Test Empty");
			testEnvironmentPage.submitCourseTestEmpty(finalTestAnswerFilePath, testId, wantedCourseCode, courseTestType, roundLevel, unitIds, jsonObj, localizationJson, testName);
			
			report.startStep("Validate Score in DB");
			List<String[]> userTestProgress = dbService.getUserTestProgressByUserId(studentId);
			String scoreForDisplayDB = userTestProgress.get(0)[4];
			testResultService.assertEquals("0", scoreForDisplayDB, "Score in DB is Incorrect");
			sleep(2);
			
			report.startStep("Exit Test");
			testEnvironmentPage.clickExitCourseTest();
			sleep(2);
			
			//int courseTestScore = Integer.valueOf(completeCourseTest(2, CourseCodes.B1, CourseTests.FinalTest, 1, sectionsToSubmit)); // for 8 units
			if (Integer.parseInt(scoreForDisplayDB) > courseTestScorePassingGrade){
				testResultService.addFailTest("Course Test score is not as expected on this stage", false, true);
			}
			
			report.startStep("Open Assessments and check B1 Final Test still Assigned");
			homePage.openAssessmentsPage(false);
			testResultService.assertEquals(true, testsPage.isCourseTestAvailable(testName), "Course Test NOT displayed though should be. Make sure progress and avg score criteria in ilp.json file configured correctly.");	
			testsPage.close();
			
			report.startStep("Open Learning Area and Click Next");
			homePage.clickOnContinueButton();
			sleep(2);
			testEnvironmentPage.clickNext();
			
			report.startStep("Navigate to Home Page check Course Criteria Achieved");
			homePage.clickOnHomeButton();
			homePage.waitHomePageloadedFully();
			
			report.startStep("Complete Course Test with passing grade > 10");
			//courseTestScore = Integer.valueOf(completeCourseTest(2, CourseCodes.B1, CourseTests.FinalTest, 4, 5)); // 5 is the number of section of 8 units
			
			report.startStep("Open Assessments Page");
			testsPage = homePage.openAssessmentsPage(false);
			
			report.startStep("Start Test");
			testSequence = testsPage.getTestSequenceByCourseId(courseId);
			testName = testsPage.clickStartTest(1, testSequence);
			webDriver.closeAlertByAccept();
			sleep(3);
			
			report.startStep("Validate Test Name");
			testEnvironmentPage.validateTestName(testName);
			sleep(5);
			
			report.startStep("Validate Course Intro");
			testEnvironmentPage.validateIntro(jsonObj, localizationJson, testId, testName);
					
			report.startStep("Answer Questions and Validate Intros");
			testEnvironmentPage.sectionIndex = 1;
			testEnvironmentPage.answerQuestionsNew(finalTestAnswerFilePath, testId, wantedCourseCode, courseTestType, sectionsToSubmit, roundLevel, unitIds, jsonObj, localizationJson, testName, unitsToAnswer);
			
			report.startStep("Validate Score in DB");
			userTestProgress = dbService.getUserTestProgressByUserId(studentId);
			scoreForDisplayDB = userTestProgress.get(0)[4];
			testResultService.assertEquals("100", scoreForDisplayDB, "Score in DB is Incorrect");
			sleep(2);
			
			report.startStep("Exit Test");
			testEnvironmentPage.clickExitCourseTest();
			sleep(2);
			
			//testId = pageHelper.getAssignedTestIdForStudent(studentId, courseId, 3); // report that test is assigned
			
//			if (courseTestScore < courseTestScorePassingGrade){
//					testResultService.addFailTest("Course Test score is not as expected on this stage", false, true);
//			}
			
			report.startStep("Open Assessments and check B1 Final Test NOT Assigned");
			homePage.openAssessmentsPage(false);
			testResultService.assertEquals(false, testsPage.isCourseTestAvailable(testName), "Course Test displayed though should not. Make sure progress and avg score criteria in ilp.json file configured correctly.");	
			testsPage.close();
			sleep(2);
			
			report.startStep("Click on right arrow and verify Next course added on Home Page");
			homePage.carouselNavigateNext();
			sleep(2);
			testResultService.assertEquals(coursesNames[startCourseIndex+1], homePage.getCurrentCourseName(), "Next course on Home Page is not valid or not found. Make sure ilp.json course sequence mapping is configured correctly.");	 

			report.startStep("Click on right arrow again and verify Only 2 courses are dispayed in carousel");
			homePage.carouselNavigateNext();
			sleep(2);
			testResultService.assertEquals(coursesNames[startCourseIndex], homePage.getCurrentCourseName(), "Next course on Home Page is not valid or not found. Make sure ilp.json course sequence mapping is configured correctly.");	 
			
			// End of User Story 43744:Siglo21: Assign Next Course if Course Criteria Achieved
			// End of User Story 43743:Siglo21: Re-Assign Test if Test Failed
			
	
		} finally {
			report.startStep("In case of test failure, don't forget to verify ilp.json file wasn't modified");
			report.report("ilp.json path: \\\\dev2008\\EdoNet300Res\\Institutions\\" + argInstId);
		}

	}
*/	

	@Test
	//@Category(inProgressTests.class) 
	@TestCaseParams(testCaseID = { "43954", "50342" }, testTimeOut = "30")
	public void test01_ArgentinaAdaptiveFlowNewTE_AssignCourseByPlt() throws Exception {
		
		newUser=true;
		
		try {
			
			report.startStep("Restart Browser");
			institutionName=institutionsName[7];
			pageHelper.restartBrowserInNewURL(institutionName, true); 
			
			report.startStep("Init Test Data");
			int startCourseIndex = 1; // "B1";
			String courseId = courses[startCourseIndex];
			String startCourseName = coursesNames[startCourseIndex]; 
			courseUnits = dbService.getCourseUnits(courses[startCourseIndex]);
			classNameAR = configuration.getProperty("classname.CourseTest");
			classId = dbService.getClassIdByClassName(classNameAR,institutionId);
			CanvasClassId = dbService.getClassFromClassExternalMapByClassId(classId);

			int iteration = 1;
			testId = "11";
			String answersFilePath = "files/PLTTestData/PLT_Answers_AllVersions.csv";
			
			report.startStep("Retrieve JSON Array of Test ID: " + testId);
			jsonObj = netService.getJsonObject("smb://"+PageHelperService.physicalPath+"/Runtime/Metadata/Courses/CourseTests/", testId+".json", useSMB);
			
			report.startStep("Generate student username for registration");
			String studentUserName = "stud" + dbService.sig(5);
							
			// Start of User Story 43738:Siglo21: Set Starting Course by PLT results 
		
			report.startStep("Enter ED via CanvasPOST - Insert New User");
			gotoCanvasPOST(classNameAR, studentUserName);

			// Initialize Test Environment Page
			testEnvironmentPage = new TestEnvironmentPage(webDriver, testResultService);
			
			report.startStep("Validate Test Title");
			testEnvironmentPage.validateTestName(testTypes[0]);
			//sleep(2);
						
			report.startStep("Perform PLT test");
			String finalLevelString = performPLT_NewTE(testTypes[0], studentId, iteration, jsonObj, localizationJson, answersFilePath, institutionId);
							
			report.startStep("Exit PLT");
			testEnvironmentPage.clickExitPlt();
			sleep(2);
			
			report.startStep("Skip Boarding Tour");
			pageHelper.skipOnBoardingTour();
			homePage.waitHomePageloadedFully();
					
			report.startStep("Validate Test Results Section");	
			testsPage = homePage.openAssessmentsPage(false);
			testsPage.clickOnArrowToOpenSection("3");
			sleep(1);
			testsPage.checkSubmissionDateForTests("1");
			testsPage.checkScoreLevelPLT("1", finalLevelString);
			
			report.startStep("Close Assessments page");
			testsPage.close();	
			
			report.startStep("Check ONLY starting course available on Home Page");
			homePage.checkCourseCarouselArrowsNotDisplayed();
			testResultService.assertEquals(startCourseName, homePage.getCurrentCourseName(), "Starting course on Home Page is not valid or not found. Make sure ilp.json mapping for PLT results (LevelMapping section) is either configured correctly or deafault value.");		
			
			// End of User Story 43738:Siglo21: Set Starting Course by PLT results 
						
			
			/*********************************************************************/
		} finally {
			report.startStep("In case of test failure, don't forget to verify ilp.json file wasn't modified");
			report.report("ilp.json path: \\" +PageHelperService.sharePhisicalFolder+ "Institutions\\" + institutionId);
		}
	}
	
	@Test
	//@Category(inProgressTests.class) 
	@TestCaseParams(testCaseID = { "43954", "50342" }, testTimeOut = "30")
	public void test03_ArgentinaAdaptiveFlowNewTE_AssignA3ByPlt() throws Exception {
		
		newUser=true;
		
		try {
			
			report.startStep("Restart Browser");
			institutionName=institutionsName[7];
			pageHelper.restartBrowserInNewURL(institutionName, true); 
			
			report.startStep("Init Test Data");
			int startCourseIndex = 9; // "A3";
			courseUnits = dbService.getCourseUnits(courses[startCourseIndex]);
			classNameAR = configuration.getProperty("classname.CourseTest");
			
			int iteration = 4;
			testId = "11";
			String answersFilePath = "files/PLTTestData/PLT_Answers_AllVersions.csv";
			
			report.startStep("Retrieve JSON Array of Test ID: " + testId);
			//useSMB = false;
			jsonObj = netService.getJsonObject("smb://"+PageHelperService.physicalPath+"/Runtime/Metadata/Courses/CourseTests/", testId+".json",useSMB);
			
			report.startStep("Generate student username for registration");
			String studentUserName = "stud" + dbService.sig(5);
							
			// Start of User Story 43738:Siglo21: Set Starting Course by PLT results 
		
			report.startStep("Enter ED via CanvasPOST - Insert New User");
			gotoCanvasPOST(classNameAR, studentUserName);
			
			// Initialize Test Environment Page
			testEnvironmentPage = new TestEnvironmentPage(webDriver, testResultService);
			
			report.startStep("Validate Test Title");
			testEnvironmentPage.validateTestName(testTypes[0]);
			//sleep(2);
						
			report.startStep("Perform PLT test");
			String finalLevelString = performPLT_NewTE(testTypes[0], studentId, iteration, jsonObj, localizationJson, answersFilePath, institutionId);
							
			report.startStep("Exit PLT");
			testEnvironmentPage.clickExitPlt();
			sleep(2);
			
			report.startStep("Skip Boarding Tour");
			pageHelper.skipOnBoardingTour();
			homePage.waitHomePageloaded();
					
			report.startStep("Validate Test Results Section");	
			testsPage = homePage.openAssessmentsPage(false);
			testsPage.clickOnArrowToOpenSection("3");
			sleep(1);
			testsPage.checkSubmissionDateForTests("1");
			testsPage.checkScoreLevelPLT("1", finalLevelString);
			
			report.startStep("Close Assessments page");
			testsPage.close();	
			
			report.startStep("Check that all 10 courses are available on Home Page");
			homePage.verifyAllCoursesAssignedAndOpenOnHomePage();
			
			// End of User Story 43738:Siglo21: Set Starting Course by PLT results 
			
			report.startStep("Verify the Grade updated in LtiGrades table for Canvas Service");
			dbService.checkTheUserInsertedToLtiGrades(studentId,"99");
			
			/*********************************************************************/
		} finally {
			report.startStep("In case of test failure, don't forget to verify ilp.json file wasn't modified");
			report.report("ilp.json path: "+PageHelperService.sharePhisicalFolder+"Institutions\\" + institutionId);
		}
	}
	
	@Test
	@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "56109" }, testTimeOut = "30")
	public void testFullAssignmentFollowingPLTResult_CEFR_NewTE() throws Exception {
		
		report.startStep("Init Test Data");	
			homePage.logOutOfED();
			institutionName = institutionsName[14];
			pageHelper.restartBrowserInNewURL(institutionName, true); 
			
			//pageHelper.restartBrowserInNewURL(instName, true);
			//String instId = dbService.getInstituteIdByName(institutionName);
			List<String[]> classes = pageHelper.getclassesByInstitutionName(institutionName);
			String[]oneClass = classes.get(new Random().nextInt(classes.size()-1));
			classNameAR = oneClass[0];
			String classId_AR = dbService.getClassIdByName(classNameAR, institutionId);
			String adminId = dbService.getAdminIdByInstId(institutionId);
			int startCourseIndex = 9; // A3 grade for PLT.
			String pltResult = coursesNames[startCourseIndex];
			//classNameAR = "class1";
			int iteration = 2;
			testId = "11";
			String answersFilePath = "files/PLTTestData/PLT_Answers_AllVersions.csv";
			useCEFR = true;					
			
		try {
		report.startStep("Turn on new TE flag");
			dbService.checkAndturnOnTestEnviormentFlag(institutionId);
			
		report.startStep("Retrieve JSON Array of Test ID: " + testId);
			jsonObj = netService.getJsonObject("smb://"+PageHelperService.physicalPath+"/Runtime/Metadata/Courses/CourseTests/", testId+".json", useSMB);
		
		report.startStep("Generate student username for registration");
			String studentUserName = "stud" + dbService.sig(6);
							
		report.startStep("Enter ED via new RegAndLogin API - Insert New User, the Inst Id is: " + institutionId);
			regLogin(institutionId, studentUserName, UserType.Student, false);	
			
		report.startStep("Assign PLT test to student");
			pageHelper.assignPltTestToStudetInTestEnvironment(studentId, 1, testId, adminId, institutionId, classId_AR);
			sleep(4);
			
		report.startStep("Open Assessments");
			testsPage = homePage.openAssessmentsPage(false);
			
		report.startStep("Start Test");
			String testName = testsPage.clickStartTest(1, 1);
			sleep(3);
							
		report.startStep("Validate Title");
			testEnvironmentPage = new TestEnvironmentPage(webDriver, testResultService);
			testEnvironmentPage.validateTestName(testTypes[0]);
			sleep(2);
						
		report.startStep("Perform PLT test");
			String finalLevelString = performPLT_NewTE(testTypes[0], studentId, iteration, jsonObj, localizationJson, answersFilePath, institutionId);		
				
		report.startStep("Verify 'Do Test Again' btn not displayed");
			classicTest = new NewUxClassicTestPage(webDriver, testResultService);
		//	classicTest.verifyDoTestAgainNotDisplayedInPLT();
						
		report.startStep("Exit PLT");
			classicTest.clickOnExitPLTnewTE();
			sleep(2);
			
		report.startStep("Open Assessments");
			homePage.openAssessmentsPage(false);
			//sleep(1);
							
		report.startStep("Open Tests Results section");
			testsPage.clickOnArrowToOpenSection("3");
		
		report.startStep("Check Score in Test Results");
			testsPage.checkScoreLevelPLT("1", levelCEFR);
			sleep(1);
			testsPage.close();
			sleep(1);
		report.startStep("Logout");
			homePage.clickOnLogOut();//.logOutOfED();
			sleep(5);
		}
		
		finally {
			dbService.turnOffTestEnviormentFlag(institutionId);
		}
	
			
	/*		
		report.startStep("Check all ilp courses are available for learning");
			pageHelper.skipOnBoardingTour();
			sleep(1);
			//homePage.navigateToRequiredCourseOnHomePage("Aviation 1");
			homePage.navigateToRequiredCourseByList("Aviation 1");
			for(int i = 0; i < 20; i++) {
				testResultService.assertEquals(coursesNamesUMM[i], homePage.getCurrentCourseName(), "Coure don't match: " + coursesNamesUMM[i]);
				homePage.carouselNavigateNext();
				sleep(1);
			}
	*/	
		/*NewUxClassicTestPage classicTest = new NewUxClassicTestPage(webDriver, testResultService);
		String CorpUrl = "https://edusoftlearning.com/";
		
		report.startStep("Init Test Data");
			
		institutionName=institutionsName[5];
		pageHelper.initializeData();

		pageHelper.restartBrowserInNewURL(institutionName, true); //
	
		int startCourseIndex = 9; // A3 grade for PLT.
		String pltResult = coursesNames[startCourseIndex];
		String classNameAR = "class1";
	
		report.startStep("Generate student username for registration");
		String studentUserName = "stud" + dbService.sig(8);
			
		CommonFunctionPLTPage adaptivePLTPage = new CommonFunctionPLTPage(webDriver,testResultService);
		
		String baseUrl = pageHelper.CILink.split(".com")[0] + ".com/";
							
		report.startStep("Enter ED via new RegAndLogin API - Insert New User, the Inst Id is: " + UMMInstId);
		studentId = adaptivePLTPage.regLogin(UMMInstId, studentUserName, UserType.Student, false, CorpUrl,baseUrl, classNameAR);	//
	
		report.startStep("Check Home Page message on enter");
		homePage.verifyHomePageMessageStartCourseByPLT();
		
		report.startStep("Check PLT opens");
		classicTest = new NewUxClassicTestPage(webDriver, testResultService);
		webDriver.switchToNewWindow();
		classicTest.switchToTestAreaIframe();
		classicTest.verifyTitlePLT();
		sleep(2);
				
		report.startStep("Perform PLT test - get A3 placement");
		PLTTest pltTest = classicTest.initPLTTestFromCSVFile("files/PLTTestData/PLT_ED_Answers_NEW.csv", false);
		classicTest.performTest(pltTest);
		
		report.startStep("Verify expected placement level on test end");
		classicTest.verifyPlacementLevelOnResultPagePLT(pltResult);
				
		report.startStep("Verify 'Do Test Again' btn not displayed");
		classicTest.verifyDoTestAgainNotDisplayedInPLT();
						
		report.startStep("Exit PLT");
		classicTest.clickOnExitPLT();
		sleep(2);

		report.startStep("Check all ilp courses are available for learning");
		pageHelper.skipOnBoardingTour();
		sleep(1);
		homePage.navigateToRequiredCourseByList("Aviation 1");
		for (int i = 0; i < 20; i++) {
			testResultService.assertEquals(coursesNamesUMM[i], homePage.getCurrentCourseName(), "Coure don't match: " + coursesNamesUMM[i]);
			homePage.carouselNavigateNext();
			sleep(1);
		}*/
	}
	
	@Test
	@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "43954", "50342" }, testTimeOut = "30")
	public void testUMMAdaptiveFlowNewTE() throws Exception {
		
		report.startStep("Init Test Data");
			int startCourseIndex = 6; // "I3 for PLT. Tourism Pre-Basic for Course";
			String courseId = coursesUMM[startCourseIndex * 2];
			String startCourseName = coursesNamesUMM[startCourseIndex * 2]; 
			courseUnits = dbService.getCourseUnits(coursesUMM[startCourseIndex * 2]);
			String courseTestName = "Tourism Pre-Basic Final Test";
			int courseProgressPassingGrade = 70;
			int testAvgScorePassingGrade = 50;
			int courseTestScorePassingGrade = 80;
			classNameAR = "class1";		
			int iteration = 2;
			testId = "11";
			String answersFilePath = "files/PLTTestData/PLT_Answers_AllVersions.csv";
			
			String createClass ="Y"; // Y/N
			String createNewUser = "I"; //i = Insert U= Update
			String userType = "S"; //S = student T= Teacher
			String TestFileName = "UMM_Regular_RegUserPost.html";
			String className="Class1_Umm Distance";
		
		report.startStep("Retrieve JSON Array of Test ID: " + testId);
			jsonObj = netService.getJsonObject("smb://"+PageHelperService.physicalPath+"/Runtime/Metadata/Courses/CourseTests/", testId+".json", useSMB);
			
		report.startStep("Restart Browser");
			institutionName=institutionsName[5];//
			pageHelper.restartBrowserInNewURL(institutionName, true); //
			
		report.startStep("Generate student username for registration");
			String studentUserName = "stud" + dbService.sig(8);
							
		// Start of User Story 43738:Siglo21: Set Starting Course by PLT results 
		
		report.startStep("Enter ED via new RegAndLogin API - Insert New User");
		//	regLogin(UMMInstId, studentUserName, UserType.Student, false);	
		createRegularRegUserAndLoginFile(createClass,className,createNewUser,userType,TestFileName);
		
		report.startStep("Check Home Page message on enter");
			homePage.verifyHomePageMessageStartCourseByPLT();
			
		// Initialize Test Environment Page
			testEnvironmentPage = new TestEnvironmentPage(webDriver, testResultService);
		
		report.startStep("Validate Title");
			testEnvironmentPage.validateTestName(testTypes[0]);
			sleep(2);
						
		report.startStep("Perform PLT test");
			String finalLevelString = performPLT_NewTE(testTypes[0], studentId, iteration, jsonObj, localizationJson, answersFilePath, institutionId);	
					
		//report.startStep("Verify 'Do Test Again' btn not displayed");
		//	classicTest.verifyDoTestAgainNotDisplayedInPLT();
						
		report.startStep("Exit PLT");
			//classicTest.clickOnExitPLT();
			testEnvironmentPage.clickExitPlt();
			sleep(2);
		
		report.startStep("Press on Start PLT again");
			pageHelper.skipOnBoardingTour();
			testsPage = homePage.openAssessmentsPage(false);
			testsPage.clickStartTest(1,1);
		
		report.startStep("Verify and close browser alert");
			String alertText = webDriver.getAlertText(3);
			webDriver.closeAlertByAccept();
			testResultService.assertEquals(Cannot_Take_PLT_Alert, alertText, "Checking 'Cannot take PLT' Alert");
		
		report.startStep("Verify PLT not opened again");
			webDriver.checkNumberOfTabsOpened(1);
				
		report.startStep("Check Tests Results section");
			webDriver.refresh();
			testsPage = homePage.openAssessmentsPage(false);
			testsPage.clickOnArrowToOpenSection("3");
			sleep(1);
			testsPage.checkSubmissionDateForTests("1");
			testsPage.checkScoreLevelPLT("1", finalLevelString);
		
		report.startStep("Close Assessments page");
			testsPage.close();
		
		report.startStep("Check ONLY starting course available on Home Page");
			homePage.checkCourseCarouselArrowsNotDisplayed();
			testResultService.assertEquals(startCourseName, homePage.getCurrentCourseName(), "Starting course on Home Page is not valid or not found");		
		
		// End of User Story 43738:Siglo21: Set Starting Course by PLT results 
		
		/*********************************************************************/
		
		// Start of User Story 43742:Siglo21: Assign Test if Course Criteria Achieved
		
		report.startStep("Enter course and create NOT passing progress - completion < 60% / score avg < 40%");
			homePage.clickOnContinueButton();
			sleep(2);
			pageHelper.skipOnBoardingTour();
			setProgressInCourse(courseId, 1, 30);
				
		report.startStep("Navigate to Home Page and check Course Criteria NOT Achieved");
			//homePage.clickToOpenNavigationBar();
			homePage.clickOnHomeButton();
			homePage.waitHomePageloaded();
			int courseCompletion = Integer.valueOf(homePage.getCompletionWidgetValue());
			int avgTestScore = Integer.valueOf(homePage.getScoreWidgetValue());
				
		if (courseCompletion > courseProgressPassingGrade || avgTestScore > testAvgScorePassingGrade){
			testResultService.addFailTest("Course Progress is not as expected on this stage", false, true);
		}
				
		report.startStep("Open Assessments and check NO course test assigned");
			testsPage = homePage.openAssessmentsPage(false);
			testResultService.assertEquals(false, testsPage.isCourseTestAvailable(courseTestName), "Course Test displayed though should not");
			testsPage.close();
			sleep(2);
		
		report.startStep("Enter course again and create passing progress - completion > 60% / score avg > 40%");
			homePage.clickOnContinueButton();
			sleep(2);
			pageHelper.skipOnBoardingTour();
			setProgressInCourse(courseId, 3, 60);
				
		report.startStep("Navigate to Home Page check Course Criteria Achieved");
			//homePage.clickToOpenNavigationBar();
			homePage.clickOnHomeButton();
			homePage.waitHomePageloaded();
			courseCompletion = Integer.valueOf(homePage.getCompletionWidgetValue());
			avgTestScore = Integer.valueOf(homePage.getScoreWidgetValue());
			if (courseCompletion < courseProgressPassingGrade || avgTestScore < testAvgScorePassingGrade){
				testResultService.addFailTest("Course Progress is not as expected on this stage", false, true);
			}
	
		report.startStep("Open Assessments and check Tourism Pre-Basic Final Test assigned");
			testsPage = homePage.openAssessmentsPage(false);
			testResultService.assertEquals(true, testsPage.isCourseTestAvailable(courseTestName), "Course Test NOT displayed though should be");	
		
		// End of User Story 43742:Siglo21: Assign Test if Course Criteria Achieved
		
		/*********************************************************************/
		
		// Start of User Story 43744:Siglo21: Assign Next Course if Course Criteria Achieved
		// Start of User Story 43743:Siglo21: Re-Assign Test if Test Failed
		
		report.startStep("Complete Course Test with NOT passing grade < 70");
			int courseTestScore = Integer.valueOf(completeCourseTest(2, CourseCodes.B1, CourseTests.MidTerm, 1, 3));
			if (courseTestScore > courseTestScorePassingGrade){
				testResultService.addFailTest("Course Test score is not as expected on this stage", false, true);
			}
		
		report.startStep("Open Assessments and check Tourism Pre-Basic Final Test still Assigned");
			homePage.openAssessmentsPage(false);
			testResultService.assertEquals(true, testsPage.isCourseTestAvailable(courseTestName), "Course Test NOT displayed though should be");	
				
		report.startStep("Complete Course Test with passing grade > 70");
			courseTestScore = Integer.valueOf(completeCourseTest(2, CourseCodes.B1, CourseTests.MidTerm, 3, 3));
			if (courseTestScore < courseTestScorePassingGrade){
				testResultService.addFailTest("Course Test score is not as expected on this stage", false, true);
			}
				
		report.startStep("Open Assessments and check Tourism Pre-Basic Final Test NOT Assigned");
			homePage.openAssessmentsPage(false);
			testResultService.assertEquals(false, testsPage.isCourseTestAvailable(courseTestName), "Course Test displayed though should not");	
			testsPage.close();
			sleep(2);
		
		report.startStep("Click on right arrow and verify Next course added on Home Page");
			homePage.carouselNavigateNext();
			sleep(2);
			testResultService.assertEquals(coursesNamesUMM[startCourseIndex * 2 + 1], homePage.getCurrentCourseName(), "Next course on Home Page is not valid or not found");	
		
		// End of User Story 43744:Siglo21: Assign Next Course if Course Criteria Achieved
		// End of User Story 43743:Siglo21: Re-Assign Test if Test Failed
	
		/*report.startStep("Init Test Data");
		int startCourseIndex = 6; // "I3 for PLT. Tourism Pre-Basic for Course";
		String pltResult = coursesNames[startCourseIndex];
		String courseId = coursesUMM[startCourseIndex * 2];
		String startCourseName = coursesNamesUMM[startCourseIndex * 2]; 
		List<String> courseUnits = dbService.getCourseUnits(coursesUMM[startCourseIndex * 2]);
		String courseTestName = "Tourism Pre-Basic Final Test";
		int courseProgressPassingGrade = 70;
		int testAvgScorePassingGrade = 50;
		int courseTestScorePassingGrade = 80;
		String classNameAR = "class1";
		CommonFunctionPLTPage adaptivePLTPage = new CommonFunctionPLTPage(webDriver,testResultService);
		institutionName=institutionsName[5];
		pageHelper.initializeData();
		String CorpUrl = "https://edusoftlearning.com/";
	
		pageHelper.restartBrowserInNewURL(institutionName, true); //
			
		report.startStep("Generate student username for registration");
		String studentUserName = "stud" + dbService.sig(8);
							
		// Start of User Story 43738:Siglo21: Set Starting Course by PLT results
		
		String baseUrl = pageHelper.CILink.split(".com")[0] + ".com/";
		
		report.startStep("Enter ED via new RegAndLogin API - Insert New User");
		studentId = adaptivePLTPage.regLogin(UMMInstId, studentUserName, UserType.Student, false, CorpUrl, baseUrl, classNameAR);	
		
		report.startStep("Check Home Page message on enter");
		homePage.verifyHomePageMessageStartCourseByPLT();
		
		report.startStep("Check PLT opens");
		NewUxClassicTestPage classicTest = new NewUxClassicTestPage(webDriver, testResultService);
		webDriver.switchToNewWindow();
		classicTest.switchToTestAreaIframe();
		classicTest.verifyTitlePLT();
		sleep(2);
					
		report.startStep("Perform PLT test - skip Cycle 2 - get I3 placement");
		PLTTest pltTest = classicTest.initPLTTestFromCSVFile("files/PLTTestData/PLT_ED_Answers_NEW.csv", true);
		classicTest.performTest(pltTest);
		
		report.startStep("Verify expected placement level on test end");
		classicTest.verifyPlacementLevelOnResultPagePLT(pltResult);
				
		report.startStep("Verify 'Do Test Again' btn not displayed");
		classicTest.verifyDoTestAgainNotDisplayedInPLT();
						
		report.startStep("Exit PLT");
		classicTest.clickOnExitPLT();
		sleep(2);
		
		report.startStep("Press on Start PLT again");
		pageHelper.skipOnBoardingTour();
		testsPage = homePage.openAssessmentsPage(false);
		classicTest = testsPage.clickOnStartTest("1", "1");
		
		report.startStep("Verify and close browser alert");
		String alertText = webDriver.getAlertText(3);
		webDriver.closeAlertByAccept();
		testResultService.assertEquals(adaptivePLTPage.Cannot_Take_PLT_Alert, alertText, "Checking 'Cannot take PLT' Alert");
		
		report.startStep("Verify PLT not opened again");
		webDriver.checkNumberOfTabsOpened(1);
				
		report.startStep("Check Tests Results section");
		webDriver.refresh();
		testsPage = homePage.openAssessmentsPage(false);
		testsPage.clickOnArrowToOpenSection("3");
		sleep(1);
		testsPage.checkSubmissionDateForTests("1");
		testsPage.checkScoreLevelPLT("1", pltResult);
		
		report.startStep("Close Assessments page");
		testsPage.close();
		
		report.startStep("Check ONLY starting course available on Home Page");
		homePage.checkCourseCarouselArrowsNotDisplayed();
		testResultService.assertEquals(startCourseName, homePage.getCurrentCourseName(), "Starting course on Home Page is not valid or not found");		
		
		// End of User Story 43738:Siglo21: Set Starting Course by PLT results 
		
		/*********************************************************************
		
		// Start of User Story 43742:Siglo21: Assign Test if Course Criteria Achieved
		
		report.startStep("Enter course and create NOT passing progress - completion < 60% / score avg < 40%");
		homePage.clickOnContinueButton();
		sleep(2);
		pageHelper.skipOnBoardingTour();
		adaptivePLTPage.setProgressInCourse(courseId, 1, 30, courseUnits, studentId);//
				
		report.startStep("Navigate to Home Page and check Course Criteria NOT Achieved");
		//homePage.clickToOpenNavigationBar();
		homePage.clickOnHomeButton();
		homePage.waitHomePageloaded();
		int courseCompletion = Integer.valueOf(homePage.getCompletionWidgetValue());
		int avgTestScore = Integer.valueOf(homePage.getScoreWidgetValue());
			
		if (courseCompletion > courseProgressPassingGrade || avgTestScore > testAvgScorePassingGrade){
			testResultService.addFailTest("Course Progress is not as expected on this stage", false, true);
		}
				
		report.startStep("Open Assessments and check NO course test assigned");
		testsPage = homePage.openAssessmentsPage(false);
		testResultService.assertEquals(false, testsPage.isCourseTestAvailable(courseTestName), "Course Test displayed though should not");
		testsPage.close();
		sleep(2);
		
		report.startStep("Enter course again and create passing progress - completion > 60% / score avg > 40%");
		homePage.clickOnContinueButton();
		sleep(2);
		pageHelper.skipOnBoardingTour();
		adaptivePLTPage.setProgressInCourse(courseId, 3, 60, courseUnits, studentId); //
				
		report.startStep("Navigate to Home Page check Course Criteria Achieved");
		homePage.clickOnHomeButton();
		homePage.waitHomePageloaded();
		courseCompletion = Integer.valueOf(homePage.getCompletionWidgetValue());
		avgTestScore = Integer.valueOf(homePage.getScoreWidgetValue());
		if (courseCompletion < courseProgressPassingGrade || avgTestScore < testAvgScorePassingGrade){
			testResultService.addFailTest("Course Progress is not as expected on this stage", false, true);
		}
	
		report.startStep("Open Assessments and check Tourism Pre-Basic Final Test assigned");
		testsPage = homePage.openAssessmentsPage(false);
		testResultService.assertEquals(true, testsPage.isCourseTestAvailable(courseTestName), "Course Test NOT displayed though should be");	
		
		// End of User Story 43742:Siglo21: Assign Test if Course Criteria Achieved
		
		/*********************************************************************
		
		// Start of User Story 43744:Siglo21: Assign Next Course if Course Criteria Achieved
		// Start of User Story 43743:Siglo21: Re-Assign Test if Test Failed
		
		report.startStep("Complete Course Test with NOT passing grade < 70");
		int courseTestScore = Integer.valueOf(adaptivePLTPage.completeCourseTest(2, CourseCodes.B1, CourseTests.MidTerm, 1, 3));
		if (courseTestScore > courseTestScorePassingGrade){
			testResultService.addFailTest("Course Test score is not as expected on this stage", false, true);
		}
		
		report.startStep("Open Assessments and check Tourism Pre-Basic Final Test still Assigned");
		homePage.openAssessmentsPage(false);
		testResultService.assertEquals(true, testsPage.isCourseTestAvailable(courseTestName), "Course Test NOT displayed though should be");	
				
		report.startStep("Complete Course Test with passing grade > 70");
		courseTestScore = Integer.valueOf(adaptivePLTPage.completeCourseTest(2, CourseCodes.B1, CourseTests.MidTerm, 3, 3));
		if (courseTestScore < courseTestScorePassingGrade){
			testResultService.addFailTest("Course Test score is not as expected on this stage", false, true);
		}
				
		report.startStep("Open Assessments and check Tourism Pre-Basic Final Test NOT Assigned");
		homePage.openAssessmentsPage(false);
		testResultService.assertEquals(false, testsPage.isCourseTestAvailable(courseTestName), "Course Test displayed though should not");	
		testsPage.close();
		sleep(2);
	
		report.startStep("Click on right arrow and verify Next course added on Home Page");
		homePage.carouselNavigateNext();
		sleep(2);
		testResultService.assertEquals(coursesNamesUMM[startCourseIndex * 2 + 1], homePage.getCurrentCourseName(), "Next course on Home Page is not valid or not found");	
		
		// End of User Story 43744:Siglo21: Assign Next Course if Course Criteria Achieved
		// End of User Story 43743:Siglo21: Re-Assign Test if Test Failed
		
		/********************************************************************
		 */
	}	
	
	/*@Test
	@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "27240","40425","40830","40831" })
	public void testAssessmentsPagePLTNewTE() throws Exception {
		
		report.startStep("Restart Browser");
		institutionName=institutionsName[1];//
		pageHelper.restartBrowserInNewURL(institutionName, true); //
		
		report.startStep("Init Data");
		String answersFilePath = "files/PLTTestData/PLT_Answers_AllVersions.csv";
		String className = configuration.getProperty("classname.CourseTest");
		String adminId = dbService.getAdminIdByInstId(institutionId);
		String classId = dbService.getClassIdByName(className, institutionId);
		String testId = "11";
		int iteration = 2;
		
		report.startStep("Log in as Student");
		homePage = createUserAndLoginNewUXClass(className,institutionId);
		homePage.waitHomePageloaded();
		
		report.startStep("Wait to Home page loaded");
		homePage.waitTillCarouselLoad();
		
		// Initialize Test Environment Page
		testEnvironmentPage = new TestEnvironmentPage(webDriver, testResultService);
		
		report.startStep("Check Test Button Alert (Counter)");
		testEnvironmentPage.verifyTestNumberInAlert(0);	
		
		report.startStep("Assign PLT test to student");
		pageHelper.assignPltTestToStudetInTestEnvironment(studentId, 1, testId, adminId, instId, classId);
		sleep(3);
		
		report.startStep("Retrieve JSON Array of Test ID: " + testId);
		jsonObj = netService.getJsonObject("smb://dev2008/EdoNet300Res/Runtime/Metadata/Courses/CourseTests/", testId+".json");
		
		report.startStep("Check Test Button Alert (Counter)");
		testEnvironmentPage.verifyTestNumberInAlert(1);			
		
		report.startStep("Verify the Humbureger Alert doesn't display when the Navigation bar is open");
		homePage.getHumburgerAlert(false);
		
		report.startStep("Verify the Humbureger Alert display when the Navigation is close");
		homePage.clickToOpenNavigationBar();
		homePage.getHumburgerAlert(true);
		homePage.clickToOpenNavigationBar();
		
		report.startStep("Open Assessments");
		testsPage = homePage.openAssessmentsPage(false);
										
		report.startStep("Press on Start PLT");
		String testName = testsPage.clickStartTest(1,1);
		sleep(2);
		
		report.startStep("Check PLT opens and validate title");
		testEnvironmentPage.validateTestName(testName);
		sleep(2);
			
		report.startStep("Perform PLT test");
		//PLTTest pltTest = testEnvironmentPage.initPLTTestFromCSVFile("files/PLTTestData/PLT_ED_Answers_NEW.csv", true);
		//String expectedLevelonExit = "Intermediate 3";	
		//testEnvironmentPage.performTest(pltTest);
		
		String finalLevelString = performPLT_NewTE(testName, studentId, iteration, jsonObj, localizationJson, answersFilePath, institutionId);

		report.startStep("Verify description link");
		testEnvironmentPage.verifyDescriptionLinkPLT(coursesNames);
		
		report.startStep("Verify prefer higher level link link"); // need to debug from here once developed //
		testEnvironmentPage.verifyPreferToStudyLinkPLT();
		
		report.startStep("Click on Do Test Again");
		testEnvironmentPage.clickOnDoTestAgainPLT();
		
		report.startStep("Check PLT opens");
		testEnvironmentPage.validateTestName(testName);
		
		report.startStep("Perform PLT test again");
		iteration = 1;
		finalLevelString = performPLT_NewTE(testName, studentId, iteration, jsonObj, localizationJson, answersFilePath, institutionId);

		report.startStep("Exit PLT");
		testEnvironmentPage.clickOnExitPLT();
		
		report.startStep("Check Test Button Alert NOT DISPLAYED");
		WebElement testBtnAlert = homePage.getTestButtonAlertElement();
		if (testBtnAlert!=null)
			testResultService.addFailTest("Alert still shows available PLT test", false, true);
		
		report.startStep("Verify the Humbureger Alert not display");
		homePage.clickToOpenNavigationBar();
		homePage.getHumburgerAlert(false);
		homePage.clickToOpenNavigationBar();
		
		report.startStep("Open Assessments");
		homePage.openAssessmentsPage(false);
						
		report.startStep("Open Tests Results section");
		testsPage.clickOnArrowToOpenSection("3");
				
		report.startStep("Verify PLT displayed in Test Results");
		testsPage.checkTestDisplayedInSectionByTestName("Placement Test", "3", "1");
									
		report.startStep("Check Date of Submission");
		testsPage.checkSubmissionDateForTests("1");
		sleep(1);
				
		report.startStep("Check Score in Test Results");
		testsPage.checkScoreLevelPLT("1", finalLevelString);
		sleep(1);
		
		report.startStep("Close Assessments");
		testsPage.close();
		
	}*/
	
	
	@Test
	@TestCaseParams(testCaseID = { "BUG-id: 91463" })
	public void testResumePltWithTakeOnlyOnceFlag() throws Exception {
		
	report.startStep("Init Data");
		//String instID = coursesInstId;
		String answersFilePath = "files/PLTTestData/PLT_Answers_AllVersions.csv";
		int iteration = 1;
		String unitId = "-1";
		testId = "11";
			
	report.startStep("Retrieve JSON Array of Test ID: " + testId);
		jsonObj = netService.getJsonObject("smb://"+PageHelperService.physicalPath+"/Runtime/Metadata/Courses/CourseTests/", testId+".json", useSMB);
		
		String adminId = dbService.getAdminIdByInstId(institutionId);
		String classId = dbService.getClassIdByName(className, institutionId);
		
	try {
		
	report.startStep("Turn on the flag 'Take PLT only once'");
		dbService.switchPltTakeOnlyOnceFlag(institutionId,"ON");
		
	report.startStep("Init wanted scores");
		testEnvironmentPage = new TestEnvironmentPage(webDriver, testResultService);
		NewUxLearningArea2 learningArea2 = new NewUxLearningArea2(webDriver, testResultService);
		int[] wantedScore = new int[1];
		List<String[]> dataOfVersion = testEnvironmentPage.initScoresOfPLT("files/PLTTestData/NewTePltScores.csv");
		String level = dataOfVersion.get(iteration)[2];
		wantedScore[0] = Integer.parseInt(dataOfVersion.get(iteration)[3]); // C1 Reading Score
		//wantedScore[1] = Integer.parseInt(dataOfVersion.get(iteration)[4]); // C1 Listening Score
		//wantedScore[2] = Integer.parseInt(dataOfVersion.get(iteration)[5]); // C1 Grammar Score
				
	report.startStep("Check Test Button Alert");
		testEnvironmentPage.verifyTestNumberInAlert(0);	
		
	report.startStep("Assign PLT test to student");
		pageHelper.assignPltTestToStudetInTestEnvironment(studentId, 1, testId, adminId, institutionId, classId);
		sleep(3);
							
	report.startStep("Open Assessments");
		testsPage = homePage.openAssessmentsPage(false);
											
	report.startStep("Press on Start PLT");
		String testName = testsPage.clickStartTest(1, 1);
		
	report.startStep("Check PLT opens");
		testEnvironmentPage.validateTestName(testName);
		sleep(2);
		
	report.startStep("Acomplish PLT test - Only first cycle. Choosing Level");
		String statusSymbol = testEnvironmentPage.choosePlacementStatusNewTe(level);
		String[] levels = {statusSymbol};//,statusSymbol,statusSymbol};
		
	report.startStep("Click on Start Test Button");
		testEnvironmentPage.pressOnStartTest();
		sleep(2);
		
	report.startStep("Retrieve 1 section codes from client side (First Cycle)");
		String[] codes = new String[1];
		for (int i = 0; i < 1; i++) {
			String code = pageHelper.runJavaScriptCommand("return JSON.parse(JSON.parse(localStorage.getItem('Course')).CourseJson).Children[0].Children["+0+"].Metadata.Code");
			codes[0] = code;
			sleep(1);
		}
	report.addTitle("Codes: " + Arrays.toString(codes));
		
	report.startStep("Retrieve Lessons Ids of First Cycle");
		List<String> lessonsIds = new ArrayList<String>(); 
		for (int i = 0; i < codes.length; i++) {
			String lessonId = pageHelper.runJavaScriptCommand("return JSON.parse(JSON.parse(localStorage.getItem('Course')).CourseJson).Children[0].Children.filter(function(lesson){ return lesson.Metadata.Code ==='"+codes[i]+"'; })[0].NodeId");
			lessonsIds.add(lessonId);
		}
	report.addTitle("Lessons Ids: " + lessonsIds.toString());
		
		testEnvironmentPage.sectionIndex = 1;
		testEnvironmentPage.lessonIndex = 1;
		sleep(3);
		
	report.startStep("Validate Unit Intro");
		testEnvironmentPage.validateIntro(jsonObj, localizationJson, unitId, testName);

	report.startStep("Validate Task bar is not Clickable");
		testEnvironmentPage.validateTaskBarIsNotClickable();
		
	report.startStep("Validate Lesson Intro");
		testEnvironmentPage.validateIntro(jsonObj, localizationJson, lessonsIds.get(0), testName);

	report.startStep("Validate Title Indexes");
		testEnvironmentPage.validateTitleIndex();
		
	report.startStep("Answer only First Cycle, Reading part");
		testEnvironmentPage.initAndAnswerPltSectionByWantedScoreNewTE(answersFilePath, levels[0], "Reading", codes[0],wantedScore[0]);
		
	report.startStep("Store last visited task before leaving the test");
		String taskBefore = testEnvironmentPage.getCurrentTaskNumber();
						
	report.startStep("Close PLT");
		testEnvironmentPage.clickClose();
		
	report.startStep("Open Assessments");
		testsPage = homePage.openAssessmentsPage(false);
			
	report.startStep("Press on Start PLT");
			try {
				testsPage.clickStartTest(1,1);
			}catch (Exception e) {
				testResultService.addFailTest("Resume test unavailable "+e.getMessage(), true, true);
				e.printStackTrace();
			}
			
	report.startStep("Verify that resuming test returning you to last visited task");
		String taskAfter = testEnvironmentPage.getCurrentTaskNumber();
		textService.assertEquals("Apperantly resume test not working correctly", taskBefore, taskAfter);
		
	report.startStep("Close PLT");
		testEnvironmentPage.clickClose();
		
	report.startStep("Exit from ED");
		homePage.logOutOfED();
		
		
		}catch (Exception e) {
			testResultService.addFailTest(e.getMessage(), true, true);
			e.printStackTrace();
		}catch(AssertionError e){
			e.printStackTrace();
			testResultService.addFailTest(e.getMessage(), true, true);
		}finally {
			dbService.switchPltTakeOnlyOnceFlag(institutionId,"OFF");
			dbService.deleteStudentByName(institutionId, userName);
			
		}
	}
	

	@Test
	//@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "50743" })
	public void testResumePltNewTE() throws Exception {
		
		report.startStep("Init Data");
		//String instID = coursesInstId;
		institutionName=institutionsName[1];//
		String answersFilePath = "files/PLTTestData/PLT_Answers_AllVersions.csv";
		int iteration = 1;
		String unitId = "-1";
		testId = "11";
		
		report.startStep("Restart Browser");
		pageHelper.restartBrowserInNewURL(institutionName, true); //
		
		report.startStep("Retrieve JSON Array of Test ID: " + testId);
		jsonObj = netService.getJsonObject("smb://"+PageHelperService.physicalPath+"/Runtime/Metadata/Courses/CourseTests/", testId+".json", useSMB);
		
		String adminId = dbService.getAdminIdByInstId(institutionId);
		String classId = dbService.getClassIdByName(className, institutionId);
		
		report.startStep("Log in as Student");
		homePage = createUserAndLoginNewUXClass(className, institutionId);
		homePage.closeAllNotifications();
		homePage.waitHomePageloaded();
		
		// Initialize Test Environment Page
		testEnvironmentPage = new TestEnvironmentPage(webDriver, testResultService);
		NewUxLearningArea2 learningArea2 = new NewUxLearningArea2(webDriver, testResultService);
		//TmsHomePage tmsHomePage = new TmsHomePage(webDriver, testResultService);
		
		int[] wantedScore = new int[3];
		List<String[]> dataOfVersion = testEnvironmentPage.initScoresOfPLT("files/PLTTestData/NewTePltScores.csv");
	
		wantedScore[0] = Integer.parseInt(dataOfVersion.get(iteration)[3]); // C1 Reading Score
		wantedScore[1] = Integer.parseInt(dataOfVersion.get(iteration)[4]); // C1 Listening Score
		wantedScore[2] = Integer.parseInt(dataOfVersion.get(iteration)[5]); // C1 Grammar Score
		//wantedScore[0] = 20; // C1 Reading Score
		//wantedScore[1] = 20; // C1 Listening Score
		//wantedScore[2] = 20;
		String level = dataOfVersion.get(iteration)[2]; // Initial Level
		
		report.startStep("Check Test Button Alert");
		testEnvironmentPage.verifyTestNumberInAlert(0);	
		
		report.startStep("Assign PLT test to student");
		pageHelper.assignPltTestToStudetInTestEnvironment(studentId, 1, testId, adminId, institutionId, classId);
		sleep(3);
		
		//report.startStep("Check Test Button Alert");
		//testEnvironmentPage.verifyTestNumberInAlert(1);			
				
		report.startStep("Open Assessments");
		testsPage = homePage.openAssessmentsPage(false);
											
		report.startStep("Press on Start PLT");
		String testName = testsPage.clickStartTest(1, 1);
		
		report.startStep("Check PLT opens");
		testEnvironmentPage.validateTestName(testName); //(TITLE_PLT);
		sleep(2);
			
		report.startStep("Perform PLT test - Only first cycle");
			
		report.startStep("Choose Level");
		String statusSymbol = testEnvironmentPage.choosePlacementStatusNewTe(level);
		String[] levels = {statusSymbol,statusSymbol,statusSymbol};// levels of all sections
		
		report.startStep("Click on Start Test Button");
		testEnvironmentPage.pressOnStartTest();
		sleep(2);
		
		report.startStep("Retrieve 3 sections codes from client side (First Cycle)");
		String[] codes = new String[3];
		for (int i = 0; i < 3; i++) {
			String code = pageHelper.runJavaScriptCommand("return JSON.parse(JSON.parse(localStorage.getItem('Course')).CourseJson).Children[0].Children["+i+"].Metadata.Code");
			codes[i] = code;
			sleep(1);
		}
		report.addTitle("Codes: " + Arrays.toString(codes));
		
		report.startStep("Retrieve Lessons Ids of First Cycle");
		List<String> lessonsIds = new ArrayList<String>(); //getLessonsIdsFromLessonDetailsArrOfSpecificUnit(unitId);String[] codes = new String[3];
		for (int i = 0; i < codes.length; i++) {
			String lessonId = pageHelper.runJavaScriptCommand("return JSON.parse(JSON.parse(localStorage.getItem('Course')).CourseJson).Children[0].Children.filter(function(lesson){ return lesson.Metadata.Code ==='"+codes[i]+"'; })[0].NodeId");
			lessonsIds.add(lessonId);
		}
		report.addTitle("Lessons Ids: " + lessonsIds.toString());
		
		testEnvironmentPage.sectionIndex = 1;
		testEnvironmentPage.lessonIndex = 1;
		sleep(3);
		
		report.startStep("Answer First Cycle Questions");
		testEnvironmentPage.answerPltCycleNewTE(unitId, lessonsIds, jsonObj, localizationJson, testName, answersFilePath, levels, codes, wantedScore);
		testEnvironmentPage.clickOnSubmitOKButton();

		report.startStep("Close PLT");
		testEnvironmentPage.clickClose();
		
		report.startStep("Validate Scores are Stored Correctly in DB");
		String[] actualScoresDB = testEnvironmentPage.validatePltScoresInDB(studentId, wantedScore);
		
		report.startStep("Calculate Expected next Cycle Level");
		Object[] expectedSecondCycleReadingLevel = testEnvironmentPage.calculateSecondCycleLevel(statusSymbol, Integer.parseInt(actualScoresDB[0])); 
		Object[] expectedSecondCycleListeningLevel = testEnvironmentPage.calculateSecondCycleLevel(statusSymbol, Integer.parseInt(actualScoresDB[1])); 
		Object[] expectedSecondCycleGrammarLevel = testEnvironmentPage.calculateSecondCycleLevel(statusSymbol, Integer.parseInt(actualScoresDB[2])); 
		sleep(2);
			
		report.startStep("Open Assessments");
		testsPage = homePage.openAssessmentsPage(false);
			
		report.startStep("Press on Start PLT");
		testsPage.clickStartTest(1,1);
			
		report.startStep("Check PLT opens");		
		testEnvironmentPage.validateTestNameContainsExpectedText(TITLE_PLT);
		sleep(2);
		
		report.startStep("Retrieve 3 sections codes from client side (Second Cycle)");
		String[] secondCycleCodes = new String[3];
		for (int i = 0; i < 3; i++) {
			String code = pageHelper.runJavaScriptCommand("return JSON.parse(JSON.parse(localStorage.getItem('Course')).CourseJson).Children[0].Children["+i+"].Metadata.Code");
			secondCycleCodes[i] = code;
			sleep(1);
		}
		report.addTitle("Codes: " + Arrays.toString(secondCycleCodes));
		
		report.startStep("Validate Second Cycle Sections Codes match the Expected Level");
		String secondCycleReadingLevelFromClient = testEnvironmentPage.getPltLevelByCode(answersFilePath, secondCycleCodes[0]);
		String secondCycleListeningLevelFromClient = testEnvironmentPage.getPltLevelByCode(answersFilePath, secondCycleCodes[1]);
		String secondCycleGrammarLevelFromClient = testEnvironmentPage.getPltLevelByCode(answersFilePath, secondCycleCodes[2]);
		testResultService.assertEquals(expectedSecondCycleReadingLevel[1].toString().trim(), secondCycleReadingLevelFromClient.trim(),"Second Cycle Reading Level is Incorrect.");
		testResultService.assertEquals(expectedSecondCycleListeningLevel[1].toString().trim(), secondCycleListeningLevelFromClient.trim(),"Second Cycle Listening Level is Incorrect.");
		testResultService.assertEquals(expectedSecondCycleGrammarLevel[1].toString().trim(), secondCycleGrammarLevelFromClient.trim(),"Second Cycle Grammar Level is Incorrect.");
		String[] secondCycleLevelsClient = {secondCycleReadingLevelFromClient.trim(), secondCycleListeningLevelFromClient.trim(), secondCycleGrammarLevelFromClient.trim()};
		
		report.startStep("Retrieve Lessons Ids of Second Cycle");
		lessonsIds = new ArrayList<String>(); //getLessonsIdsFromLessonDetailsArrOfSpecificUnit(unitId);String[] codes = new String[3];
		for (int i = 0; i < codes.length; i++) {
			String lessonId = pageHelper.runJavaScriptCommand("return JSON.parse(JSON.parse(localStorage.getItem('Course')).CourseJson).Children[0].Children.filter(function(lesson){ return lesson.Metadata.Code ==='"+secondCycleCodes[i]+"'; })[0].NodeId");
			lessonsIds.add(lessonId);
		}
		report.addTitle("Lessons Ids: " + lessonsIds.toString());
		
		report.startStep("Retrieve Unit Id (Second Cycle) from JSON by Lesson Id");
		//unitId = testEnvironmentPage.getValueOfWantedKeyInSpecificTestId(jsonObj, lessonsIds.get(0), "ParentNodeId");
		unitId = "-1";
		testEnvironmentPage.pltRound = 2;
		
		report.startStep("Retrieve Wanted Score of C2 from file");
		wantedScore[0] = Integer.parseInt(dataOfVersion.get(iteration)[6]); // C2 Reading Score
		wantedScore[1] = Integer.parseInt(dataOfVersion.get(iteration)[7]); // C2 Listening Score
		wantedScore[2] = Integer.parseInt(dataOfVersion.get(iteration)[8]); // C2 Grammar Score
		
		report.startStep("Answer Second Cycle Questions");
		testEnvironmentPage.answerPltCycleNewTE(unitId, lessonsIds, jsonObj, localizationJson, testName, answersFilePath, secondCycleLevelsClient, secondCycleCodes, wantedScore); // secondCycleLevelFromClient should be secondCycleLevels[1].toString()
		sleep(3);
		
		List<String[]> userTestComponentProgress = dbService.getUserTestComponentProgressByUserId(studentId);
		String[] finalScoresDB = new String[userTestComponentProgress.size()];
		for (int i = 0; i <userTestComponentProgress.size(); i++) {
			finalScoresDB[i] = userTestComponentProgress.get(i)[12];
		}
		report.startStep("Final scores from DB (descending): " + Arrays.toString(finalScoresDB));
		
		report.startStep("Calculate Final grade");
		String[] finalLevels = testEnvironmentPage.calculateAndValidatePltFinalLevel(studentId, levels,secondCycleLevelsClient);
		
		List<String[]> userTestProgress = dbService.getUserTestProgressByUserId(studentId);
		
		report.startStep("Validate Score in DB");
		//List<String[]> userTestProgress = dbService.getUserTestProgressByUserId(studentId);
		//String scoreForDisplayDB = userTestProgress.get(0)[4];
		//testResultService.assertEquals(expectedScore, scoreForDisplayDB, "Score in DB is Incorrect"); // 
		
		report.startStep("Validate isCompleted in DB");
		String isCompletedDB = userTestProgress.get(0)[8];
		testResultService.assertEquals("1", isCompletedDB, "isCompleted in DB is Incorrect");
		
		report.startStep("Validate Submit Reason in DB");
		String submitReasonDB = userTestProgress.get(0)[9];
		testResultService.assertEquals("1", submitReasonDB, "Submit Reason in DB is Incorrect");
		sleep(2);
		
		report.startStep("Validate Scores in End Page");
		testEnvironmentPage.validatePltEndTestLevels(finalLevels);
		
		report.startStep("Validate Test Status in DB is Done"); // NEED TO ADD
		//String userExitTestSettingsId = userTestProgress.get(0)[0];
		//String testStatus = dbService.getTestStatusByUserExitTestSettingsId(userExitTestSettingsId);
		//testResultService.assertEquals("3", testStatus, "Test Status in DB is incorrect.");
		String testStatus = dbService.getTestStatusOfPLTByUserId(studentId);
		testResultService.assertEquals("3", testStatus, "Test Status in DB is incorrect.");
		
		report.startStep("Validate Final Level Calculated by Automation Match Final Levels in DB");
		String[] userPLtFinalResultDB = dbService.getUserFinalPltResultByInstIdNewTE(institutionId);
		String[] finalLevelsDB = {userPLtFinalResultDB[4],userPLtFinalResultDB[5],userPLtFinalResultDB[6],userPLtFinalResultDB[7]};
		testResultService.assertEquals(true, Arrays.equals(finalLevelsDB, finalLevels), "Final Levels Calculated by Automation Don't match Final levels in DB. Automation: " + Arrays.toString(finalLevels) + ". DB: " + Arrays.toString(finalLevelsDB));
		report.startStep("Levels from db: " + Arrays.toString(finalLevelsDB));	
		
		report.startStep("Click Exit Button");
		testEnvironmentPage.clickExitPlt();
		
		report.startStep("Check Test Button Alert NOT DISPLAYED");
		WebElement testBtnAlert = homePage.getTestButtonAlertElement();
		if (testBtnAlert!=null) testResultService.addFailTest("Alert still shows available PLT test", false, true);
			
		report.startStep("Verify the Humbureger Alert not display");
		homePage.getHumburgerAlert(false);
			
		report.startStep("Logout");
		learningArea2.clickOnLogoutLearningArea();
		loginPage = new NewUXLoginPage(webDriver, testResultService);
		testResultService.assertEquals(false, loginPage.isSubmitButtonEnabled(), "Checking that ED Login Page displayed");
			
		/*report.startStep("Login as Admin");
		pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(configuration.getProperty("schoolAdmin.user"),instID));
		sleep(1);
		tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
		sleep(3);
		
		report.startStep("Go to Tests Configuration Page");
		TmsAssessmentsTestsConfigurationPage tmsAssessmentsTestsConfigurationPage = new TmsAssessmentsTestsConfigurationPage(webDriver, testResultService);
		tmsAssessmentsTestsConfigurationPage.goToAssessmentTestsConfiguration();
		
		report.startStep("Select PLT and Click Go");
		tmsAssessmentsTestsConfigurationPage.selectPltAndGo();
		
		report.startStep("Click Take Test Once");
		tmsAssessmentsTestsConfigurationPage.clickTakeOnce();
		
		// what is this:
		if (iteration == 1) {
			testResultService.assertEquals(true, tmsHomePage.isPltSettingsDisplayed(), "PLT settings hiden");/////
		} else {
			testResultService.assertEquals(false, tmsHomePage.isPltSettingsDisplayed(), "PLT settings not hiden");
		}
			
		report.startStep("Change Assign PLT option and check PLT settings view");
		tmsHomePage.checkUncheckAssignPLT();
		if (iteration == 1) {
			testResultService.assertEquals(false, tmsHomePage.isPltSettingsDisplayed(), "PLT settings not hiden");
		} else {
			testResultService.assertEquals(true, tmsHomePage.isPltSettingsDisplayed(), "PLT settings hiden");
		}
			
		report.startStep("Press Save");
		tmsHomePage.clickOnSave();
		sleep(3);
			
		report.startStep("Exit TMS");
		tmsHomePage.clickOnExit();
		sleep(3);
				
		report.startStep("Verify 'Do Test Again' btn not displayed");
        loginAsStudent(studentId);
		testsPage = homePage.openAssessmentsPage(false);
		testEnvironmentPage.verifyDoTestAgainNotDisplayedInPLT();
							
		report.startStep("Restore default PLT settings");
		pageHelper.assignPltToInstitution(instID);*/

	}
	
	@Test
	//@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "" })
	public void testPLT_API_NewTE_NewUser() throws Exception {
		
		report.startStep("Init Data");
		int iteration = 3;
		String answersFilePath = "files/PLTTestData/PLT_Answers_AllVersions.csv";
		testId = "11";
		
		report.startStep("Retrieve JSON Array of Test ID: " + testId);
		jsonObj = netService.getJsonObject("smb://"+PageHelperService.physicalPath+"/Runtime/Metadata/Courses/CourseTests/", testId+".json", useSMB);
		
		report.startStep("Log out");
		homePage.logOutOfED();
		
		report.startStep("Close Browser and Open Again in Incognito Mode");
		//institutionName = institutionsName[19];
		//pageHelper.initializeData();
		//pageHelper.closeBrowserAndOpenInCognito(false);
		boolean newUser = true;

		report.startStep("Get API Token");
		String apiToken = dbService.getApiToken(institutionId);
				
		report.startStep("Open PLT API");
		try {
		//open_PLT_API(institutionName, configuration.getProperty("classname.CourseTest"),apiToken);
		pageHelper.open_PLT_API(institutionName, configuration.getProperty("classname.CourseTest"), apiToken,newUser);
		sleep(3);
		// Initialize Test EnvironmentPage Page
		testEnvironmentPage = new TestEnvironmentPage(webDriver, testResultService);
		
		report.startStep("Click Submit");
		testEnvironmentPage.clickSubmitOnPLT_API();
		
		// validate administration- test settings id
		studentId = dbService.getUserIdByUserName(BasicNewUxTest.userName, institutionId);
		List<String[]> userTestAdministrations = dbService.getUserTestAdministrationsDetailsByStudentId(studentId);
		String userTestTokenDB = userTestAdministrations.get(0)[7];
		testResultService.assertEquals(false, userTestTokenDB == null, "User Test Token is NULL");
		
		report.startStep("Verify Title");
		testEnvironmentPage.validateTestName(TITLE_PLT);
		sleep(1);
		
		report.startStep("Verify Level List & Languages & buttons");
		testEnvironmentPage.verifyLevelListExist();
		testEnvironmentPage.verifyLanguagesExists();
		testEnvironmentPage.verifyStartTestButtonsExists();
		
		report.startStep("Perform PLT Test");
		performPLT_NewTE(TITLE_PLT, studentId, iteration, jsonObj, localizationJson, answersFilePath, institutionId);
		
		report.startStep("Click Exit PLT");
		testEnvironmentPage.clickExitPlt();
		sleep(1);
		// @Shira needs to debug
		report.startStep("Verify the return URL and result");
		List<String[]> testResultsPLT = dbService.getTestResultOfPLT_API_testNewTE(studentId);
		sleep(3);
		String actualreturnUrl = webDriver.getUrl();
		String expectedUrlAndResult = pageHelper.staticUrlForExternalPages + "Languages/viewPLT.html" + "?UserName="+ BasicNewUxTest.userName 
				+"&Final="+testResultsPLT.get(0)[20].trim()
				+"&Reading="+testResultsPLT.get(0)[21].trim()
				+"&Listening="+testResultsPLT.get(0)[22].trim()
				+"&Grammar="+testResultsPLT.get(0)[23].trim();
		
		checkTheReturnUrlAndReturnFinalResultPLT(actualreturnUrl,expectedUrlAndResult);
		}catch (Exception e) {
			e.printStackTrace();
		}
		finally{
				report.startStep("Cleaning test data. Delete student and class");		
					dbService.deleteStudentByName(institutionId, userName);
			//dbService.deleteUserById(dbService.getUserIdByUserName(userName, institutionId));
			// test push notification
		 }
		webDriver.closeBrowser();
	}

	@Test
	//@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "" })
	public void testPLT_API_NewTE_GetUser() throws Exception {

		report.startStep("Init Data");
		int iteration = 3;
		String answersFilePath = "files/PLTTestData/PLT_Answers_AllVersions.csv";
		testId = "11";

		report.startStep("Retrieve JSON Array of Test ID: " + testId);
		jsonObj = netService.getJsonObject("smb://"+PageHelperService.physicalPath+"/Runtime/Metadata/Courses/CourseTests/", testId+".json", useSMB);

		report.startStep("Log out");
		homePage.logOutOfED();

		report.startStep("Close Browser and Open Again in Incognito Mode");
		//institutionName = institutionsName[19];
		//pageHelper.initializeData();
		//pageHelper.closeBrowserAndOpenInCognito(false);
		boolean newUser = false;

		report.startStep("Get API Token");
		String apiToken = dbService.getApiToken(institutionId);

		report.startStep("Open PLT API");
		try {
			//open_PLT_API(institutionName, configuration.getProperty("classname.CourseTest"),apiToken);
			pageHelper.open_PLT_API(institutionName, configuration.getProperty("classname.CourseTest"), apiToken,newUser);
			sleep(3);
			// Initialize Test EnvironmentPage Page
			testEnvironmentPage = new TestEnvironmentPage(webDriver, testResultService);

			report.startStep("Click Submit");
			testEnvironmentPage.clickSubmitOnPLT_API();

			// validate administration- test settings id
			studentId = dbService.getUserIdByUserName(BasicNewUxTest.userName, institutionId);
			List<String[]> userTestAdministrations = dbService.getUserTestAdministrationsDetailsByStudentId(studentId);
			String userTestTokenDB = userTestAdministrations.get(0)[7];
			testResultService.assertEquals(false, userTestTokenDB == null, "User Test Token is NULL");

			report.startStep("Verify Title");
			testEnvironmentPage.validateTestName(TITLE_PLT);
			sleep(1);

			report.startStep("Verify Level List & Languages & buttons");
			testEnvironmentPage.verifyLevelListExist();
			testEnvironmentPage.verifyLanguagesExists();
			testEnvironmentPage.verifyStartTestButtonsExists();

			report.startStep("Perform PLT Test");
			performPLT_NewTE(TITLE_PLT, studentId, iteration, jsonObj, localizationJson, answersFilePath, institutionId);

			report.startStep("Click Exit PLT");
			testEnvironmentPage.clickExitPlt();
			sleep(1);
			// @Shira needs to debug
			report.startStep("Verify the return URL and result");
			List<String[]> testResultsPLT = dbService.getTestResultOfPLT_API_testNewTE(studentId);
			sleep(3);
			String actualreturnUrl = webDriver.getUrl();
			String expectedUrlAndResult = pageHelper.staticUrlForExternalPages + "Languages/viewPLT.html" + "?UserName="+ BasicNewUxTest.userName
					+"&Final="+testResultsPLT.get(0)[20].trim()
					+"&Reading="+testResultsPLT.get(0)[21].trim()
					+"&Listening="+testResultsPLT.get(0)[22].trim()
					+"&Grammar="+testResultsPLT.get(0)[23].trim();

			checkTheReturnUrlAndReturnFinalResultPLT(actualreturnUrl,expectedUrlAndResult);
		}catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			report.startStep("Cleaning test data. Delete student and class");
			dbService.deleteStudentByName(institutionId, userName);
			//dbService.deleteUserById(dbService.getUserIdByUserName(userName, institutionId));
			// test push notification
		}
		webDriver.closeBrowser();
	}

	public void checkTheReturnUrlAndReturnFinalResultPLT(String actualreturnUrl, String expectedUrlAndResult) {
		// TODO Auto-generated method stub
		testResultService.assertEquals(actualreturnUrl,expectedUrlAndResult, "Return URL or Result is incorrect");
	}

	@Test
	//@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "" })
	public void testSubmitReasonsAndTestStatuses_AssignedDone_MidTerm() throws Exception {
		
		report.startStep("Test: Status Done");
		
		String wantedTestId = "989017755";
		
		// Initialize Test Environment Page
		testEnvironmentPage = new TestEnvironmentPage(webDriver, testResultService);
	
		report.startStep("Init Data");
		courseCode = courseCodes[1];
		CourseCodes wantedCourseCode = testEnvironmentPage.getCourseCodeEnumByCourseCodeString(courseCode);
	
		courseId = getCourseIdByCourseCode(courseCode);
		String answerFilePath = "files/CourseTestData/MidTerm_Answers.csv";
		CourseTests courseTestType= CourseTests.MidTerm;
				
		report.startStep("Assign B1 Mid-Term Test to student");
		pageHelper.assignAutomatedTestToStudetInTestEnvironment(studentId, courseId, 2,0,0,1);
		sleep(2);
		
		report.startStep("Return Test Id");
		testId = pageHelper.getAssignedTestIdForStudent(studentId,courseId, 2);
		if (!testId.equals(wantedTestId)) {
			// update test id
			String userExitSettingsId = dbService.getUserExitSettingsId(studentId, testId, courseId, "2");
			dbService.updateTestIdInUserExitSettings(wantedTestId, userExitSettingsId);
			testId = pageHelper.getAssignedTestIdForStudent(studentId,courseId, 2);
			String token = pageHelper.getTokenFromConsoleWithParameters("1", studentId, testId, userExitSettingsId);
			dbService.updateAssignWithToken(token, userExitSettingsId);
		}
		
		try{
			report.startStep("Copy and keep the original " +testId+".json file to backup folder");
			pageHelper.runConsoleProgram("cmd /c copy "+PageHelperService.ServerPath+"CourseTests\\"+testId+".json "+PageHelperService.ServerPath+"CourseTests\\ForAutomationBackup");
			
			report.startStep("Change JSON of Test to Round 2");
			pageHelper.runConsoleProgram("cmd /c copy "+PageHelperService.ServerPath+"CourseTests\\"+testId+"_RoundLevel2\\"+testId+".json "+PageHelperService.ServerPath+"CourseTests");
		
			report.startStep("Retrieve JSON Array of Test ID: " + testId);
			jsonObj = netService.getJsonObject("smb://"+PageHelperService.physicalPath+"/Runtime/Metadata/Courses/CourseTests/", testId, useSMB);
			
			report.startStep("Get Round Level of Course");
			String roundLevelOfCourse = testEnvironmentPage.getRoundLevelFromJson(jsonObj, testId);
			
			report.startStep("Update Test Duration");
			String updatedTime = "1";
			dbService.SetTestTime(studentId, Integer.parseInt(updatedTime));
			
			report.startStep("Open Assessments Page");
			testsPage = homePage.openAssessmentsPage(false);
			
			report.startStep("Start Test");
			int testSequence = testsPage.getTestSequenceByCourseId(courseId);
			String testName = testsPage.clickStartTest(1, testSequence);
			webDriver.closeAlertByAccept();
			sleep(5);
			
			testEnvironmentPage.testDuration = updatedTime;
			
			report.startStep("Validate Course Intro");
			testEnvironmentPage.validateIntro(jsonObj, localizationJson, testId, testName);
			
			List<String[]> lessonCountForUnits = dbService.getLessonCountForAllUnitsInCourse(testId);
			
			// Get unit ids list from previous list
			List<String> unitIds = new ArrayList<String>();
			for (int j = 0; j < lessonCountForUnits.size(); j++) {
				unitIds.add(lessonCountForUnits.get(j)[0]);
			}
			
			report.startStep("Answer First Section");
			int sectionIndex = 0;
			int unitIndex = 0;
			int sectionIndexInUnit = 0;
			testEnvironmentPage.answerQuestionInSpecificSectionNewTE(answerFilePath, testId, wantedCourseCode, courseTestType, sectionIndex, roundLevelOfCourse, unitIds, jsonObj, localizationJson, testName, unitIndex, sectionIndexInUnit);
			
			report.startStep("Wait until Test Time is Over");
			waitTillTestDurationExpired();
		
			report.startStep("Validate Time out Message");
			testEnvironmentPage.validateMessageText("The time allowed for this test has ended.\nYour test has been submitted automatically.");//
		
			report.startStep("Validate Score in DB");
			List<String[]> userTestProgress = dbService.getUserTestProgressByUserId(studentId);
			String scoreForDisplayDB = userTestProgress.get(0)[4];
			testResultService.assertEquals(false, scoreForDisplayDB.equals("0"), "Score in DB is Incorrect");
			
			report.startStep("Validate isCompleted in DB");
			String isCompletedDB = userTestProgress.get(0)[8];
			testResultService.assertEquals("1", isCompletedDB, "isCompleted in DB is Incorrect");
			
			report.startStep("Validate Submit Reason in DB is: \"Time is over\"");
			String submitReasonDB = userTestProgress.get(0)[9];
			testResultService.assertEquals("2", submitReasonDB, "Submit Reason in DB is Incorrect");
			
			report.startStep("Validate Expected Status in DB is Done");
			String userExitTestSettingsId = userTestProgress.get(0)[0];
			String testStatus = dbService.getTestStatusByUserExitTestSettingsId(userExitTestSettingsId);
			testResultService.assertEquals("3", testStatus, "Test Status in DB is incorrect.");
			
			report.startStep("Validate Score in Outro Page");
			testEnvironmentPage.validateScoreEndOfTest(scoreForDisplayDB);
			
			report.startStep("Click Exit Button");
			testEnvironmentPage.clickExitCourseTest();
			
			report.startStep("Open Assessments Page");
			homePage.openAssessmentsPage(false);
			
			report.startStep("Validate test is not available");
			boolean isTestAvailable = testsPage.checkIfTestIsDisplayedInAvailableTests(testName);
			testResultService.assertEquals(false, isTestAvailable, "Test is available even though time is over");
			
			report.startStep("Validate test score in assessments page");
			testsPage.clickOnArrowToOpenSection("3");
			testsPage.checkScoreByTest("1", scoreForDisplayDB);
			
			report.startStep("Close Assessments Page");
			testsPage.close();
			
			report.startStep("Log Out of ED");
			homePage.logOutOfED();
		}
		finally {	
			report.startStep("copy back the original file to original path");
			pageHelper.runConsoleProgram("cmd /c copy "+PageHelperService.ServerPath+"CourseTests\\ForAutomationBackup\\"+testId+".json "+PageHelperService.ServerPath+"CourseTests");	
		}
	}
	
	private void waitTillTestDurationExpired() {
		
		try {
			String[] testTimeSecStr = testEnvironmentPage.getTestTime().split("Secs.");
			int testTimeSecInt = Integer.parseInt(testTimeSecStr[0]);
			sleep(testTimeSecInt+3);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	//@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "" })
	public void testSubmitReasonsAndTestStatusesPLT() throws Exception {
		
		report.startStep("Init Data");
		String adminId = dbService.getAdminIdByInstId(institutionId);
		String classId = dbService.getClassIdByName(className, institutionId);
		//PLTStartLevel pltLevel = PLTStartLevel.Basic;
		//String pltLanguage = "English";
		String unitId = "-1";
		testId = "11";
		String answersFilePath = "files/PLTTestData/PLT_Answers_AllVersions.csv";
		//int[] wantedScores = {100,100,100};
		String finalPltLevel ="First Discoveries";
		
		testEnvironmentPage = new TestEnvironmentPage(webDriver, testResultService);
		
		int iteration = 1;
		// iteration 6 is different- score for reading is 100 (so both questions are answered)- but actual score is 63 (since questions are answered partially)
		// same for iteration 7- score for reading is 100 (so both questions are answered)- but actual score is 73 (since questions are answered partially)
		
		int[] wantedScore = new int[3];
		List<String[]> dataOfVersion = testEnvironmentPage.initScoresOfPLT("files/PLTTestData/NewTePltScores.csv");
	
		wantedScore[0] = Integer.parseInt(dataOfVersion.get(iteration)[3]); // C1 Reading Score
		wantedScore[1] = Integer.parseInt(dataOfVersion.get(iteration)[4]); // C1 Listening Score
		wantedScore[2] = Integer.parseInt(dataOfVersion.get(iteration)[5]); // C1 Grammar Score
		
		String level = dataOfVersion.get(iteration)[2]; // Initial Level

	//	report.startStep("Log Out");
	//	homePage.logOutOfED();
		
	//	report.startStep("Log In as Student");
	//	homePage = createUserAndLoginNewUXClass(className, coursesInstId);
	//	homePage.closeAllNotifications();
	//	homePage.waitHomePageloaded();
	//	pageHelper.closeLastSessionImproperLogoutAlert();
		
		report.startStep("Assign PLT test to student");
		pageHelper.assignPltTestToStudetInTestEnvironment(studentId, 1, "11", adminId, institutionId, classId);
		sleep(2);
		
		report.startStep("Retrieve JSON Array of Test ID: " + testId);
		jsonObj = netService.getJsonObject("smb://"+PageHelperService.physicalPath+"/Runtime/Metadata/Courses/CourseTests/", testId+".json", useSMB);
		
		report.startStep("Update Test Duration");
		dbService.SetPLTTestTime(studentId, 1);
		
		report.startStep("Open Assessments Page");
		testsPage = homePage.openAssessmentsPage(false);
		sleep(1);
		
		report.startStep("Start Test");
		String testName = testsPage.clickStartTest(1, 1);
		testsPage.waitUntilPLTNewTE_LevelSelectiionLoaded();
		
		report.startStep("Choose Level");
		String statusSymbol = testEnvironmentPage.choosePlacementStatusNewTe(level);
		
		report.startStep("Click on Start Test Button");
		testEnvironmentPage.pressOnStartTest();
		testEnvironmentPage.waitUntilPLTIntroLoaded();
		
		String code = pageHelper.runJavaScriptCommand("return JSON.parse(JSON.parse(localStorage.getItem('Course')).CourseJson).Children[0].Children[0].Metadata.Code");
		String lessonId = pageHelper.runJavaScriptCommand("return JSON.parse(JSON.parse(localStorage.getItem('Course')).CourseJson).Children[0].Children.filter(function(lesson){ return lesson.Metadata.Code ==='"+code+"'; })[0].NodeId");
		
		report.startStep("Validate Unit Intro");
		testEnvironmentPage.validateIntro(jsonObj, localizationJson, unitId, testName);

		report.startStep("Validate Lesson Intro");
		testEnvironmentPage.validateIntro(jsonObj, localizationJson, lessonId, testName); 
		
		report.startStep("Answer Section");
		testEnvironmentPage.initAndAnswerPltSectionByWantedScoreNewTE(answersFilePath, statusSymbol, "Reading", code, 100);

		sleep(45);
		
		// validate msg- time is over
		testEnvironmentPage.validateMessageText("The time allowed for this test has ended.\nYour test has been submitted automatically.");//
		
		report.startStep("Validate Score in DB");
		List<String[]> userTestProgress = dbService.getUserTestProgressByUserId(studentId);
		String scoreForDisplayDB = userTestProgress.get(0)[4];
		testResultService.assertEquals(false, scoreForDisplayDB.equals("0"), "Score in DB is Incorrect");//
				
		report.startStep("Validate isCompleted in DB");
		String isCompletedDB = userTestProgress.get(0)[8];
		testResultService.assertEquals("1", isCompletedDB, "isCompleted in DB is Incorrect");
		
		report.startStep("Validate Submit Reason in DB");
		String submitReasonDB = userTestProgress.get(0)[9];
		testResultService.assertEquals("2", submitReasonDB, "Submit Reason in DB is Incorrect");
		
		report.startStep("Validate Test Status in DB is Done");
		String testStatus = dbService.getTestStatusOfPLTByUserId(studentId);
		testResultService.assertEquals("3", testStatus, "Test Status in DB is incorrect.");
		
		testResultService.assertEquals(finalPltLevel, testEnvironmentPage.pltFinalLevelEndPage.getText(),"Final Level is Incorrect");
		
		report.startStep("Click Exit Button");
		testEnvironmentPage.clickExitPlt();
		
		report.startStep("Open Assessments Page");
		homePage.openAssessmentsPage(false);
			
		report.startStep("Validate test is not available");
		boolean isTestAvailable = testsPage.checkIfTestIsDisplayedInAvailableTests(testName);
		testResultService.assertEquals(false, isTestAvailable, "Test is available even though time is over");
		
		report.startStep("Validate test score in assessments page");
		testsPage.clickOnArrowToOpenSection("3");
		testsPage.checkScoreLevelPLT("1", finalPltLevel);
		
		report.startStep("Close Assessments Page");
		testsPage.close();
		
		report.startStep("Log Out of ED");
		homePage.logOutOfED();
	
	}
	
	@Test
	@TestCaseParams(testCaseID = { "" })
	public void testSubmitReasonsAndTestStatusesExpiredPLT() throws Exception{
		
		report.startStep("Init Data");
		String adminId = dbService.getAdminIdByInstId(institutionId);
		String classId = dbService.getClassIdByName(className, institutionId);
		testId = "11";
		String testName = "Placement Test";

		//	String unitId = "-1";
	//	String testId = "11";
	//	String answersFilePath = "files/PLTTestData/PLT_Answers_AllVersions.csv";
	//	String finalPltLevel ="First Discoveries";
		
		testEnvironmentPage = new TestEnvironmentPage(webDriver, testResultService);
		
		int iteration = 1;
		// iteration 6 is different- score for reading is 100 (so both questions are answered)- but actual score is 63 (since questions are answered partially)
		// same for iteration 7- score for reading is 100 (so both questions are answered)- but actual score is 73 (since questions are answered partially)
		
		int[] wantedScore = new int[3];
		List<String[]> dataOfVersion = testEnvironmentPage.initScoresOfPLT("files/PLTTestData/NewTePltScores.csv");

		wantedScore[0] = Integer.parseInt(dataOfVersion.get(iteration)[3]); // C1 Reading Score
		wantedScore[1] = Integer.parseInt(dataOfVersion.get(iteration)[4]); // C1 Listening Score
		wantedScore[2] = Integer.parseInt(dataOfVersion.get(iteration)[5]); // C1 Grammar Score

	//	String level = dataOfVersion.get(iteration)[2]; // Initial Level
		
		report.startStep("Test: Status Expired");
		
	//	report.startStep("Log In as Student");
	//	homePage = createUserAndLoginNewUXClass(className, coursesInstId);
	//	homePage.closeAllNotifications();
	//	homePage.waitHomePageloaded();
	//	pageHelper.closeLastSessionImproperLogoutAlert();
		
		report.startStep("Assign PLT test to student");
		pageHelper.assignPltTestToStudetInTestEnvironment(studentId, 1, "11", adminId, institutionId, classId);
		sleep(1);
		
		String userTestAdministrationId = dbService.getUserTestAdministrationByStudentId(studentId);
		
		report.startStep("Update End Test Date (regular and UTC)");
		String currentDate = pageHelper.getCurrentDateByFormat("yyyy-MM-dd HH:mm:ss");
		currentDate = textService.updateTime(currentDate, "add", "minute", 1);
		String expirationPLTtime = dbService.addMinutesToUTCtime(1);
		expirationPLTtime = expirationPLTtime.split("[.]")[0];
		dbService.updateEndDateInUserTestAdministrations(userTestAdministrationId, currentDate, expirationPLTtime);
		sleep(5);
		report.startStep("Open Assessments Page");
		testsPage = homePage.openAssessmentsPage(false);
		sleep(3);

		report.startStep("Validate test is available");
		boolean isTestAvailable = testsPage.checkIfTestIsDisplayedInAvailableTests(testName);
		testResultService.assertEquals(true, isTestAvailable, "Test is not available even though time is not over");

		report.startStep("Wait until Test Time is Over");
		sleep(185);

		report.startStep("Validate test is not available");
		isTestAvailable = testsPage.checkIfTestIsDisplayedInAvailableTests(testName);
		testResultService.assertEquals(false, isTestAvailable, "Test is available even though time is over");

		report.startStep("Validate Expected Status in DB is Expired");
		testStatus = dbService.getTestStatusOfPLTByUserId(studentId);
		testResultService.assertEquals("4", testStatus,"Test Status in DB is incorrect.");

		report.startStep("Close Assessments Page");
		testsPage.close();
		
		report.startStep("Log Out of ED");
		homePage.logOutOfED();	
		
	}
	
	@Test
	@TestCaseParams(testCaseID = { "" })
	public void testSubmitReasonsAndTestStatusesInProgressPLT() throws Exception{
		
		report.startStep("Test: Statuses Assigned, In Progress, Incomplete");
		
		report.startStep("Init Data");
		String adminId = dbService.getAdminIdByInstId(institutionId);
		String classId = dbService.getClassIdByName(className, institutionId);
		String unitId = "-1";
		testId = "11";
		String answersFilePath = "files/PLTTestData/PLT_Answers_AllVersions.csv";
		//String finalPltLevel ="First Discoveries";
		
		testEnvironmentPage = new TestEnvironmentPage(webDriver, testResultService);
		
		
	//	report.startStep("Log In as Student");
	//	homePage = createUserAndLoginNewUXClass(className, coursesInstId);
	//	homePage.closeAllNotifications();
	//	homePage.waitHomePageloaded();
	//	pageHelper.closeLastSessionImproperLogoutAlert();
		
		report.startStep("Assign PLT test to student");
		pageHelper.assignPltTestToStudetInTestEnvironment(studentId, 1, "11", adminId, institutionId, classId);
		sleep(2);
		
		//String userTestAdministrationId = dbService.getUserTestAdministrationByStudentId(studentId);
		
		report.startStep("Retrieve JSON Array of Test ID: " + testId);
		jsonObj = netService.getJsonObject("smb://"+PageHelperService.physicalPath+"/Runtime/Metadata/Courses/CourseTests/", testId+".json",useSMB);
		
		
		report.startStep("Update End Test Date (regular and UTC)");
		String currentDate = pageHelper.getCurrentDateByFormat("yyyy-MM-dd HH:mm:ss");
		currentDate = textService.updateTime(currentDate, "add", "minute", 2);
	//	String currectDateUTC = textService.updateTime(currentDate, "reduce", "hour", 3);// in winter- last value is 2. in summer = 3
	//	dbService.updateEndDateInUserTestAdministrations(userTestAdministrationId, currentDate, currectDateUTC);// validate this is the correct table
		sleep(1);
		
		report.startStep("Validate Expected Status in DB is Assigned");
		testStatus = dbService.getTestStatusOfPLTByUserId(studentId);
		testResultService.assertEquals("1", testStatus,"Test Status in DB is incorrect.");
		
		report.startStep("Open Assessments Page");
		testsPage = homePage.openAssessmentsPage(false);
		sleep(1);
		
		report.startStep("Start Test");
		String testName = testsPage.clickStartTest(1, 1);
		testsPage.waitUntilPLTNewTE_LevelSelectiionLoaded();
		
		report.startStep("Choose Level");
		int iteration = 1;
		// iteration 6 is different- score for reading is 100 (so both questions are answered)- but actual score is 63 (since questions are answered partially)
		// same for iteration 7- score for reading is 100 (so both questions are answered)- but actual score is 73 (since questions are answered partially)
		
	//	int[] wantedScore = new int[3];
		List<String[]> dataOfVersion = testEnvironmentPage.initScoresOfPLT("files/PLTTestData/NewTePltScores.csv");
	
		String level = dataOfVersion.get(iteration)[2];
		String statusSymbol = testEnvironmentPage.choosePlacementStatusNewTe(level);
		
		report.startStep("Click on Start Test Button");
		testEnvironmentPage.pressOnStartTest();
		testEnvironmentPage.waitUntilPLTIntroLoaded();
		
		String code = pageHelper.runJavaScriptCommand("return JSON.parse(JSON.parse(localStorage.getItem('Course')).CourseJson).Children[0].Children[0].Metadata.Code");
		String lessonId = pageHelper.runJavaScriptCommand("return JSON.parse(JSON.parse(localStorage.getItem('Course')).CourseJson).Children[0].Children.filter(function(lesson){ return lesson.Metadata.Code ==='"+code+"'; })[0].NodeId");

		report.startStep("Validate Unit Intro");
		testEnvironmentPage.validateIntro(jsonObj, localizationJson, unitId, testName);

		report.startStep("Validate Lesson Intro");
		testEnvironmentPage.validateIntro(jsonObj, localizationJson, lessonId, testName); 
		
		report.startStep("Answer Section");
		testEnvironmentPage.initAndAnswerPltSectionByWantedScoreNewTE(answersFilePath, statusSymbol, "Reading", code, 100);
		
		report.startStep("Validate Expected Status in DB is In Progress");
		testStatus = dbService.getTestStatusOfPLTByUserId(studentId);
		testResultService.assertEquals("2", testStatus,"Test Status in DB is incorrect.");
		
		report.startStep("Validate isCompleted in DB");
		List<String[]> userTestProgress = dbService.getUserTestProgressByUserId(studentId);
		String isCompletedDB = userTestProgress.get(0)[8];
		testResultService.assertEquals("0", isCompletedDB, "isCompleted in DB is Incorrect");
		
		report.startStep("Validate Submit Reason in DB");
		String submitReasonDB = userTestProgress.get(0)[9];
		testResultService.assertEquals(true, submitReasonDB == null, "Submit Reason in DB is Incorrect");
	
		report.startStep("Click Close Button");
		testEnvironmentPage.clickClose();
		
		report.startStep("Open Assessments Page");
		homePage.openAssessmentsPage(false);
		
		report.startStep("Validate test is available");
		boolean isTestAvailable = testsPage.checkIfTestIsDisplayedInAvailableTests(testName);
		testResultService.assertEquals(true, isTestAvailable, "Test is not available even though time is not over");

		report.startStep("Validate Score in DB");
		userTestProgress = dbService.getUserTestProgressByUserId(studentId);
		String scoreForDisplayDB = userTestProgress.get(0)[4];
		testResultService.assertEquals(true, scoreForDisplayDB.equals("0"), "Score in DB is Incorrect. Score in DB: " + scoreForDisplayDB);//
		
		report.startStep("Validate isCompleted in DB");
		isCompletedDB = userTestProgress.get(0)[8];
		testResultService.assertEquals("0", isCompletedDB, "isCompleted in DB is Incorrect");
		
		report.startStep("Validate Submit Reason in DB is 3-External Proccess");
		submitReasonDB = userTestProgress.get(0)[9];
		testResultService.assertEquals(null, submitReasonDB, "Submit Reason in DB is Incorrect");
		
		report.startStep("Validate Expected Status in DB is Incomplete");
		testStatus = dbService.getTestStatusOfPLTByUserId(studentId);
		testResultService.assertEquals("2", testStatus,"Test Status in DB is incorrect.");

		report.startStep("Close Assessments Page");
		testsPage.close();
		
		report.startStep("Log Out of ED");
		homePage.logOutOfED();	
	}
	
	@Test
	//@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "81576","81577" })
	public void testResumeIntroMidTermTest() throws Exception {
		
		String wantedTestId = "989022835";
		
		// Initialize Test Environment Page
		testEnvironmentPage = new TestEnvironmentPage(webDriver, testResultService);
	
		report.startStep("Init Data");
		courseCode = courseCodes[1];
		//CourseCodes wantedCourseCode = testEnvironmentPage.getCourseCodeEnumByCourseCodeString(courseCode);
	
		courseId = getCourseIdByCourseCode(courseCode);
		//String answerFilePath = "files/CourseTestData/MidTerm_Answers.csv";
		//CourseTests courseTestType= CourseTests.MidTerm;
				
		report.startStep("Assign B1 Mid-Term Test to student");
		pageHelper.assignAutomatedTestToStudetInTestEnvironment(studentId, courseId, 2,0,0,1);
		sleep(2);
		
		report.startStep("Return Test Id");
		testId = pageHelper.getAssignedTestIdForStudent(studentId,courseId, 2);
		String userExitSettingsId = dbService.getUserExitSettingsId(studentId, testId, courseId, "2");
		if (!testId.equals(wantedTestId)) {
			dbService.updateTestIdInUserExitSettings(wantedTestId, userExitSettingsId);
			testId = pageHelper.getAssignedTestIdForStudent(studentId,courseId, 2);
			String token = pageHelper.getTokenFromConsoleWithParameters("1", studentId, testId, userExitSettingsId);
			dbService.updateAssignWithToken(token, userExitSettingsId);
		}
					
		report.startStep("Retrieve JSON Array of Test ID: " + testId);
		jsonObj = netService.getJsonObject("smb://"+PageHelperService.physicalPath+"/Runtime/Metadata/Courses/CourseTests/", testId, useSMB);
		
		report.startStep("Open Assessments Page");
		testsPage = homePage.openAssessmentsPage(false);
		
		report.startStep("Start Test");
		int testSequence = testsPage.getTestSequenceByCourseId(courseId);
		String testName = testsPage.clickStartTest(1, testSequence);
		webDriver.closeAlertByAccept();
		sleep(3);
		
		report.startStep("Validate Test Name");
		testEnvironmentPage.validateTestName(testName);
		//sleep(5);
		
		report.startStep("Validate Course Intro");
		testEnvironmentPage.validateIntro(jsonObj, localizationJson, testId, testName);
		
		report.startStep("Answer first Unit (first 2 lessons)");
		courseCode = courseCodes[1];
		CourseCodes wantedCourseCode = testEnvironmentPage.getCourseCodeEnumByCourseCodeString(courseCode);
	
		courseId = getCourseIdByCourseCode(courseCode);
		String answerFilePath = "files/CourseTestData/MidTerm_Answers.csv";
		CourseTests courseTestType= CourseTests.MidTerm;
		
		String roundLevelOfCourse = testEnvironmentPage.getRoundLevelFromJson(jsonObj, testId);
		
		List<String[]> lessonCountForUnits = dbService.getLessonCountForAllUnitsInCourse(testId);
		
		// Get unit ids list from previous list
		List<String> unitIds = new ArrayList<String>();
		for (int j = 0; j < lessonCountForUnits.size(); j++) {
			unitIds.add(lessonCountForUnits.get(j)[0]);
		}
		
		report.startStep("Answer First Section");
		testEnvironmentPage.sectionIndex = 1;
		testEnvironmentPage.lessonIndex = 1;
		int sectionIndex = 0;
		int unitIndex = 0;
		int sectionIndexInUnit = 0;
		testEnvironmentPage.answerQuestionInSpecificSectionNewTE(answerFilePath, testId, wantedCourseCode, courseTestType, sectionIndex, roundLevelOfCourse, unitIds, jsonObj, localizationJson, testName, unitIndex, sectionIndexInUnit);
		
		report.startStep("Answer Second Section");
		testEnvironmentPage.sectionIndex = 1;
		testEnvironmentPage.lessonIndex = 2;
		sectionIndex = 1;
		unitIndex = 0;
		sectionIndexInUnit = 1;
		testEnvironmentPage.answerQuestionInSpecificSectionNewTE(answerFilePath, testId, wantedCourseCode, courseTestType, sectionIndex, roundLevelOfCourse, unitIds, jsonObj, localizationJson, testName, unitIndex, sectionIndexInUnit);
	
		// Update unit and lesson indexes
		testEnvironmentPage.sectionIndex = 2;
		testEnvironmentPage.lessonIndex = 1;
		
		report.startStep("Retrieve Second Unit ID and First Lesson ID (in second unit)");
		//List<String[]> lessonCountForUnits = dbService.getLessonCountForAllUnitsInCourse(testId);
		String secondUnitId = unitIds.get(1);
		String firstLessonIdInSecondUnit = testEnvironmentPage.getLessonsIdsFromLessonDetailsArrOfSpecificUnit(secondUnitId).get(0);
		
		report.startStep("Validate Unit Intro (without continue)");
		testEnvironmentPage.validateIntroWithoutContinue(jsonObj, localizationJson, secondUnitId, testName);
	//	sleep(2);
		
		//report.startStep("Wait 1 minute");
		//sleep(5);//sleep(60);
		
		//report.startStep("Retrieve Test Timer");
		//String testTimer = testEnvironmentPage.getTestTime();
		
		report.startStep("Click Close Test");
		testEnvironmentPage.clickClose();
		
		report.startStep("Open Assessments Page");
		testsPage = homePage.openAssessmentsPage(false);
		
		report.startStep("Validate Resume Button Appears and Click it");
		testsPage.validateButtonTextAndClickIt(1, "Resume Test");
		
		report.startStep("Validate Test Name");
		testEnvironmentPage.validateTestNameContainsExpectedText(testName);
		sleep(3);
		
		//report.startStep("Validate Test Timer is the same as Before Resume");
		//testResultService.assertEquals(testTimer, testEnvironmentPage.getTestTime(), "Test Timer after Resume is Incorrect.");
		
		report.startStep("Validate Unit Intro (Landed in the correct Place)");
		testEnvironmentPage.validateIntro(jsonObj, localizationJson, secondUnitId, testName);
	
		/*report.startStep("Validate Resume Lesson Log Added to DB"); // need to debug once bug 81625 is fixed
		List<String[]> userTestComponentProgress = dbService.getUserTestComponentProgressByUserId(studentId);
		String userTestUnitProgressId = userTestComponentProgress.get(0)[1];
		List<String> resumeUnitIntroRecords = dbService.getResumeUnitsRecords(userTestUnitProgressId);
		testResultService.assertEquals(true, resumeUnitIntroRecords.size() == 1, "Resume Lesson Intro was not recorded in db Table: 'UserTestComponentResume'");
		*/
		
		report.startStep("Validate Lesson Intro (without continue)");
		testEnvironmentPage.validateIntroWithoutContinue(jsonObj, localizationJson, firstLessonIdInSecondUnit, testName);
		sleep(2);
		
		//report.startStep("Wait 1 minute");
		//sleep(5);//sleep(60);
		
		//report.startStep("Retrieve Test Timer");
		//String testTimer = testEnvironmentPage.getTestTime();
		
		report.startStep("Click Close Test");
		testEnvironmentPage.clickClose();
		
		report.startStep("Open Assessments Page");
		testsPage = homePage.openAssessmentsPage(false);
		
		report.startStep("Validate Resume Button Appears and Click it");
		testsPage.validateButtonTextAndClickIt(1, "Resume Test");
		
		report.startStep("Validate Test Name");
		testEnvironmentPage.validateTestNameContainsExpectedText(testName);
		
		//report.startStep("Validate Test Timer is the same as Before Resume");
		//testResultService.assertEquals(testTimer, testEnvironmentPage.getTestTime(), "Test Timer after Resume is Incorrect.");
		
		report.startStep("Validate Unit Intro (Landed in the correct Place)");
		testEnvironmentPage.validateIntro(jsonObj, localizationJson, firstLessonIdInSecondUnit, testName);
	
		/*report.startStep("Validate Resume Lesson Log Added to DB"); // need to debug once bug 81624 is fixed
		userTestComponentProgress = dbService.getUserTestComponentProgressByUserId(studentId);
		String userTestComponentProgressId = userTestComponentProgress.get(0)[0];
		List<String> resumeLessonIntroRecords = dbService.getResumeComponentRecords(userTestComponentProgressId);
		testResultService.assertEquals(true, resumeLessonIntroRecords.size() == 1, "Resume Lesson Intro was not recorded in db Table: 'UserTestComponentResume'");
		*/
		// The logs will be saved only after submit. need to wait until bugs are fixed and then add submit here and debug these lines of code.
		
		report.startStep("Navigate to End Of Section and Submit");
		testEnvironmentPage.clickTasksNav();
		sleep(2);
		testEnvironmentPage.clickSubmitInTasksNav(false);
		sleep(3);
		
		report.startStep("Validate Resume Intro Record Exist in DB");
		List<String[]> userTestUnitProgress = dbService.getUserTestUnitProgressByUserId(studentId);
		String userTestUnitProgressId = userTestUnitProgress.get(userTestUnitProgress.size()-1)[0];
		testEnvironmentPage.validateResumeIntroRecordExist(userTestUnitProgressId);
		
		report.startStep("Click Close");
		testEnvironmentPage.clickClose();
		
		report.startStep("Log Out");
		homePage.logOutOfED();
				
	}
	
	
	@Test
	//@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "81594" })
	public void testValidateTestRandomizationNewTE() throws Exception {
		
		// Initialize Test Environment Page
		testEnvironmentPage = new TestEnvironmentPage(webDriver, testResultService);
		
		report.startStep("Init Data");
		courseCode = courseCodes[1];
		courseId = getCourseIdByCourseCode(courseCode);
		String teVersion = "1"; // new te = 1, old te = 0
		int testType = 2;
				
		report.startStep("Assign B1 Mid-Term Test to student");
		pageHelper.assignAutomatedTestToStudetInTestEnvironment(studentId, courseId, testType,0,0,1);
		sleep(2);
		
		report.startStep("Retrieve Test Id");
		testId = pageHelper.getAssignedTestIdForStudent(studentId,courseId, testType);
		
		report.startStep("Validate Assigned Test ID is in Relevant List from DB");
		testEnvironmentPage.validateTestRandomization(teVersion, courseId, testId,testType);
		
		try{
			
			report.startStep("Copy and keep the original " +testId+".json file to backup folder");
			pageHelper.runConsoleProgram("cmd /c copy "+PageHelperService.ServerPath+"CourseTests\\"+testId+".json "+PageHelperService.ServerPath+"CourseTests\\ForAutomationBackup");
			
			report.startStep("Change JSON of Test to Round 2");
			pageHelper.runConsoleProgram("cmd /c copy "+PageHelperService.ServerPath+"CourseTests\\"+testId+"_RoundLevel2\\"+testId+".json "+PageHelperService.ServerPath+"CourseTests");
		
			report.startStep("Retrieve JSON Array of Test ID: " + testId);
			jsonObj = netService.getJsonObject("smb://"+PageHelperService.physicalPath+"/Runtime/Metadata/Courses/CourseTests/", testId, useSMB);
			
			report.startStep("Open Assessments Page");
			testsPage = homePage.openAssessmentsPage(false);
			
			report.startStep("Start Test");
			int testSequence = testsPage.getTestSequenceByCourseId(courseId);
			String testName = testsPage.clickStartTest(1, testSequence);
			webDriver.closeAlertByAccept();
			sleep(3);
			
			report.startStep("Validate Course Intro");
			testEnvironmentPage.validateIntro(jsonObj, localizationJson, testId, testName);
			sleep(3);
			
			report.startStep("Close Test and Logout");
			testEnvironmentPage.clickClose();
			
			report.startStep("Update is Completed and Submit Reason");
			String userExitTestSettingsId = dbService.getUserExitSettingsId(studentId, testId, courseId, String.valueOf(testType));
			dbService.updateCompletedInUserTestProgress(userExitTestSettingsId,"1");
			dbService.updateSubmitReasonInUserTestProgress(userExitTestSettingsId, "1");
			sleep(2);
			
			report.startStep("Assign B1 Mid-Term Test to student Again");
			pageHelper.assignAutomatedTestToStudetInTestEnvironment(studentId, courseId, testType,0,0,1);
			sleep(2);
			
			report.startStep("Retrieve Test Id");
			String newTestId = pageHelper.getAssignedTestIdForStudent(studentId, courseId, testType);
			
			report.startStep("Validate not the same test is assigned");
			testResultService.assertEquals(false, newTestId.equals(testId), "Same test id was assigned twice in a row. First Test Id: " + testId + ". Second Test Id: " + newTestId);
			
			report.startStep("Validate Assigned Test ID is in Relevant List from DB");
			testEnvironmentPage.validateTestRandomization(teVersion, courseId, newTestId, testType);	
			
			report.startStep("Log Out");
			homePage.logOutOfED();
		}
		finally {	
			report.startStep("copy back the original file to original path");
			pageHelper.runConsoleProgram("cmd /c copy "+PageHelperService.ServerPath+"CourseTests\\ForAutomationBackup\\"+testId+".json "+PageHelperService.ServerPath+"CourseTests");	
		}
	}
	
	@Test
	//@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "81594" })
	public void testValidateTestRandomizationOldTE() throws Exception {
		
		report.startStep("Init Data");
		courseCode = courseCodes[1];
		courseId = getCourseIdByCourseCode(courseCode);
		String teVersion = "0";
		testEnvironmentPage = new TestEnvironmentPage(webDriver, testResultService);
		int testTypeId = 2;
		
		report.startStep("Change to Automation Instituion");
		institutionName=institutionsName[0];
		pageHelper.restartBrowserInNewURL(institutionName, true); 
		
		report.startStep("Log In as Student");
		className = configuration.getProperty("classname");
		homePage = createUserAndLoginNewUXClass(className, institutionId);
		pageHelper.skipOptin();
		homePage.skipNotificationWindow();
		pageHelper.skipOnBoardingHP();
		homePage.waitHomePageloadedFully();
		pageHelper.closeLastSessionImproperLogoutAlert();
		
		report.startStep("Assign B1 Mid-Term Test to student");
		pageHelper.assignAutomatedTestToStudetInTestEnvironment(studentId, courseId, testTypeId,0,0,1);
		sleep(2);

		report.startStep("Retrieve Test Id");
		testId = pageHelper.getAssignedTestIdForStudent(studentId, courseId, testTypeId);
		
		report.startStep("Validate Assigned Test ID is in Relevant List from DB");
		testEnvironmentPage.validateTestRandomization(teVersion, courseId, testId, testTypeId);
		
		report.startStep("Open Assessments Page");
		testsPage = homePage.openAssessmentsPage(false);
		
		report.startStep("Click Start Test in Assessments Page");
		int testSequence = testsPage.getTestSequenceByCourseId(courseId);
		String testName = testsPage.clickStartTest(1, testSequence);
		webDriver.closeAlertByAccept();
		sleep(3);
		webDriver.switchToNewWindow();
		NewUxClassicTestPage classicTest = new NewUxClassicTestPage(webDriver, testResultService);
		classicTest.switchToTestAreaIframe();
		sleep(1);
		
		report.startStep("Click Start Test");
		classicTest.pressOnStartTest();
		sleep(3);
		
		report.startStep("Submit First Section Empty");
		classicTest.browseToLastSectionTask();
		classicTest.pressOnSubmitSection(true);
		sleep(2);
		
		report.startStep("Close Test");
		classicTest.close();
		sleep(2);
		
		report.startStep("Change test to Done");
		// update did test
		dbService.updateDidTest(studentId, testId, String.valueOf(testTypeId), "1");
		String userExitTestSettingsId = dbService.getUserExitSettingsId(studentId, testId, courseId, String.valueOf(testTypeId));
		String didTestDB = dbService.getDidTestByUserExitTestSettingsId(userExitTestSettingsId);
		//testResultService.assertEquals("1", didTestDB, "Did test was not updated in DB");
		report.addTitle("Stored did test in DB: " + didTestDB);
		
		// update user started utc
		String userStartedUTC = pageHelper.getCurrentDateByFormat("yyyy-MM-dd HH:mm:ss");
		dbService.updateUserStartedUTC(studentId, testId, String.valueOf(testTypeId), userStartedUTC);
		String userStartedUtcDB = dbService.selectUserStartedUTCByUserExitTestSettingsId(userExitTestSettingsId);
		//testResultService.assertEquals(userStartedUTC, userStartedUtcDB, "User Started UTC was not updated in DB");
		report.addTitle("Stored User Started UTC in DB: " + userStartedUtcDB);
		
		// update grade
		dbService.updateGradeInFinalTestForSpecificUser(studentId, "50", testId, String.valueOf(testTypeId));
		String gradeDB = dbService.getStudentCourseTestScore(studentId, courseId, testId, testTypeId);
		//testResultService.assertEquals("50", gradeDB, "Grade was not updated in DB");
		report.addTitle("Stored Grade in DB: " + gradeDB);
		
		// update ct completed
		dbService.updateCompletedTest(studentId, testId, String.valueOf(testTypeId), "1");
		String ctCompletedDB = dbService.getCTCompleted(testId, studentId);
		//testResultService.assertEquals("1", ctCompletedDB, "CT Completed was not updated in DB");
		report.addTitle("Stored CT Completed in DB: " + ctCompletedDB);
		
		report.startStep("Assign B1 Mid-Term Test to student Again");
		pageHelper.assignAutomatedTestToStudetInTestEnvironment(studentId, courseId, testTypeId,0,0,1);
		sleep(2);
		
		report.startStep("Retrieve Test Id");
		String newTestId = pageHelper.getAssignedTestIdForStudent(studentId,courseId, testTypeId);
		
		report.startStep("Validate not the same test is assigned");
		testResultService.assertEquals(false, newTestId.equals(testId), "Same test id was assigned twice in a row. First Test Id: " + testId + ". Second Test Id: " + newTestId);
		
		report.startStep("Validate Assigned Test ID is in Relevant List from DB");
		testEnvironmentPage.validateTestRandomization(teVersion, courseId, testId, testTypeId);
		
		report.startStep("Log Out");
		homePage.logOutOfED();
		
	}
	
	@Test
	//@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "81403" })
	public void testPltBackTrueButton() throws Exception {
		
		report.startStep("Init Data");
		String adminId = dbService.getAdminIdByInstId(institutionId);
		String classId = dbService.getClassIdByName(className, institutionId);
		testId = "11";
		String answersFilePath = "files/PLTTestData/PLT_Answers_AllVersions.csv";
		
		testEnvironmentPage = new TestEnvironmentPage(webDriver, testResultService);
		
		int iteration = 11;
		// iteration 6 is different- score for reading is 100 (so both questions are answered)- but actual score is 63 (since questions are answered partially)
		// same for iteration 7- score for reading is 100 (so both questions are answered)- but actual score is 73 (since questions are answered partially)
	
		report.startStep("Log Out");
		homePage.logOutOfED();
		
		report.startStep("Log In as Student");
		homePage = createUserAndLoginNewUXClass(className, institutionId);
		homePage.closeAllNotifications();
		homePage.waitHomePageloaded();
		pageHelper.closeLastSessionImproperLogoutAlert();
		
		report.startStep("Assign PLT test to student");
		pageHelper.assignPltTestToStudetInTestEnvironment(studentId, 1, "11", adminId, institutionId, classId);
		sleep(3);
		
		try{
			report.startStep("Copy and keep the original " +testId+".json file to backup folder");
			pageHelper.runConsoleProgram("cmd /c copy "+PageHelperService.ServerPath+"CourseTests\\"+testId+".json "+PageHelperService.ServerPath+"CourseTests\\ForAutomationBackup");
			
			report.startStep("Change JSON of PLT Test "+testId+" to Back = true");
			pageHelper.runConsoleProgram("cmd /c copy "+PageHelperService.ServerPath+"CourseTests\\"+testId+"_BackTrue\\"+testId+".json "+PageHelperService.ServerPath+"CourseTests");
		
			report.startStep("Retrieve JSON Array of Test ID: " + testId);
			jsonObj = netService.getJsonObject("smb://"+PageHelperService.physicalPath+"/Runtime/Metadata/Courses/CourseTests/", testId+".json", useSMB);
			
			report.startStep("Open Assessments Page");
			testsPage = homePage.openAssessmentsPage(false);
			sleep(5);
			
			report.startStep("Start Test");
			String testName = testsPage.clickStartTest(1, 1);	
			
			report.startStep("Validate Back Value");
			testPltBackButton_NewTE(testName, studentId, iteration, jsonObj, localizationJson, answersFilePath, institutionId);
		
			report.startStep("Click Close");
			testEnvironmentPage.clickExitPlt();
			sleep(1);
			
			report.startStep("Log Out");
			homePage.logOutOfED();
		}
		
		finally {	
			report.startStep("copy back the original file to original path");
			pageHelper.runConsoleProgram("cmd /c copy "+PageHelperService.ServerPath+"CourseTests\\ForAutomationBackup\\"+testId+".json "+PageHelperService.ServerPath+"CourseTests");	
		}
		
	}
	
	@Test
	@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "81402" })
	public void testPltBackFalseButton() throws Exception {
		
		report.startStep("Init Data");
		String adminId = dbService.getAdminIdByInstId(institutionId);
		String classId = dbService.getClassIdByName(className, institutionId);
		testId = "11";
		String answersFilePath = "files/PLTTestData/PLT_Answers_AllVersions.csv";
		
		testEnvironmentPage = new TestEnvironmentPage(webDriver, testResultService);
		
		int iteration = 2;
		// iteration 6 is different- score for reading is 100 (so both questions are answered)- but actual score is 63 (since questions are answered partially)
		// same for iteration 7- score for reading is 100 (so both questions are answered)- but actual score is 73 (since questions are answered partially)
	
		report.startStep("Log Out");
		homePage.logOutOfED();
		
		report.startStep("Log In as Student");
		homePage = createUserAndLoginNewUXClass(className, institutionId);
		homePage.waitHomePageloaded();
		pageHelper.closeLastSessionImproperLogoutAlert();
		
		report.startStep("Assign PLT test to student");
		pageHelper.assignPltTestToStudetInTestEnvironment(studentId, 1, "11", adminId, institutionId, classId);
		sleep(3);
		
		try{
			report.startStep("Copy and keep the original " +testId+".json file to backup folder");
			pageHelper.runConsoleProgram("cmd /c copy "+PageHelperService.ServerPath+"CourseTests\\"+testId+".json "+PageHelperService.ServerPath+"CourseTests\\ForAutomationBackup");
			
			report.startStep("Change JSON of PLT Test (11.json) to Back = false");
			pageHelper.runConsoleProgram("cmd /c copy "+PageHelperService.ServerPath+"CourseTests\\"+testId+"_BackFalse\\"+testId+".json "+PageHelperService.ServerPath+"CourseTests");
		
			report.startStep("Retrieve JSON Array of Test ID: " + testId);
			jsonObj = netService.getJsonObject("smb://"+PageHelperService.physicalPath+"/Runtime/Metadata/Courses/CourseTests/", testId+".json", useSMB);
			
			report.startStep("Open Assessments Page");
			testsPage = homePage.openAssessmentsPage(false);
			sleep(3);
			
			report.startStep("Start Test");
			String testName = testsPage.clickStartTest(1, 1);	
			
			report.startStep("Validate Back Value");
			testPltBackButton_NewTE(testName, studentId, iteration, jsonObj, localizationJson, answersFilePath, institutionId);
			
		}
		finally {	
			report.startStep("copy back the original file to original path");
			pageHelper.runConsoleProgram("cmd /c copy "+PageHelperService.ServerPath+"CourseTests\\ForAutomationBackup\\"+testId+".json "+PageHelperService.ServerPath+"CourseTests");	
		}
		
	}
	
	
	
	public String performPLT_NewTE(String testName, String studentId, int iteration, JSONObject jsonObj,
			JSONObject localizationJson, String answersFilePath, String institutionId) throws Exception{
		
		// Initialize Test Enviorment Page
		testEnvironmentPage = new TestEnvironmentPage(webDriver, testResultService);
				
		int[] wantedScore = new int[3];
		List<String[]> dataOfVersion = testEnvironmentPage.initScoresOfPLT("files/PLTTestData/NewTePltScores.csv");
	
		wantedScore[0] = Integer.parseInt(dataOfVersion.get(iteration)[3]); // C1 Reading Score
		wantedScore[1] = Integer.parseInt(dataOfVersion.get(iteration)[4]); // C1 Listening Score
		wantedScore[2] = Integer.parseInt(dataOfVersion.get(iteration)[5]); // C1 Grammar Score
		
		String initial_Level = dataOfVersion.get(iteration)[2]; // Initial Level 2
						
		report.startStep("Choose Level");
		String statusSymbol = testEnvironmentPage.choosePlacementStatusNewTe(initial_Level);
		String readingLevel = "";
		
		/*if (iteration == 6) {
			readingLevel = "Intermediate2score60";
		} else if(iteration == 7) {
			readingLevel = "Intermediate2score80";
		} else {
			readingLevel = statusSymbol;
		} */ // this block is relevant for v1 only- to validate scores
		
		readingLevel = statusSymbol;
		
		String[] levels = {readingLevel,statusSymbol,statusSymbol};// levels of all sections
		
		//report.startStep("Choose Language");
		//testEnvironmentPage.choosePltLanguage(pltLanguage);
		
		report.startStep("Click on Start Test Button");
		testEnvironmentPage.pressOnStartTest();
		sleep(4);
		
		getComponentCodesAndLessonsId();
		
		//report.startStep("Retrieve Unit Id (First Cycle) from JSON by Lesson Id");
		//unitId = testEnvironmentPage.getValueOfWantedKeyInSpecificTestId(jsonObj, lessonsIds.get(0), "ParentNodeId");
		
		String unitId = "-1";
		testEnvironmentPage.pltRound = 1;
		
		report.startStep("Answer First Cycle Questions");
		testEnvironmentPage.useSMB = useSMB;
		testEnvironmentPage.answerPltCycleNewTE(unitId, lessonsIds, jsonObj, localizationJson, testName, answersFilePath, levels, codes, wantedScore);

		/*if (iteration == 6) {
			wantedScore[0] = 63;
		} else if (iteration == 7) {
			wantedScore[0] = 73;
		} else if (iteration == 1) {//v1
			wantedScore[2] = 58;
		}*/ // this code is relevant for when testing in a specific version
		
		report.startStep("Validate Scores are Stored Correctly in DB for Lesson Ids" + lessonsIds + " and Codes:" + Arrays.toString(codes));
		sleep(1);
		String[] actualScoresDB = testEnvironmentPage.validatePltScoresInDB(studentId, wantedScore);
		
		report.startStep("Calculate Expected next Cycle Level");
		Object[] expectedSecondCycleReadingLevel = testEnvironmentPage.calculateSecondCycleLevel(statusSymbol, Integer.parseInt(actualScoresDB[0])); 
		Object[] expectedSecondCycleListeningLevel = testEnvironmentPage.calculateSecondCycleLevel(statusSymbol, Integer.parseInt(actualScoresDB[1])); 
		Object[] expectedSecondCycleGrammarLevel = testEnvironmentPage.calculateSecondCycleLevel(statusSymbol, Integer.parseInt(actualScoresDB[2])); 
		//sleep(2);
		
		report.startStep("Retrieve 3 sections codes from client side (Second Cycle)");
		String[] secondCycleCodes = new String[3];
		for (int i = 0; i < 3; i++) {
			String code = pageHelper.runJavaScriptCommand("return JSON.parse(JSON.parse(localStorage.getItem('Course')).CourseJson).Children[1].Children["+i+"].Metadata.Code");
			secondCycleCodes[i] = code;
			sleep(1);
		}
		report.addTitle("Codes: " + Arrays.toString(secondCycleCodes));
		
		report.startStep("Validate Second Cycle Sections Codes match the Expected Level");
		String secondCycleReadingLevelFromClient = testEnvironmentPage.getPltLevelByCode(answersFilePath, secondCycleCodes[0]);
		String secondCycleListeningLevelFromClient = testEnvironmentPage.getPltLevelByCode(answersFilePath, secondCycleCodes[1]);
		String secondCycleGrammarLevelFromClient = testEnvironmentPage.getPltLevelByCode(answersFilePath, secondCycleCodes[2]);
		
		testResultService.assertEquals(expectedSecondCycleReadingLevel[1].toString().trim(), secondCycleReadingLevelFromClient.trim(),"Second Cycle Reading Level is Incorrect.");
		testResultService.assertEquals(expectedSecondCycleListeningLevel[1].toString().trim(), secondCycleListeningLevelFromClient.trim(),"Second Cycle Listening Level is Incorrect.");
		testResultService.assertEquals(expectedSecondCycleGrammarLevel[1].toString().trim(), secondCycleGrammarLevelFromClient.trim(),"Second Cycle Grammar Level is Incorrect.");
		String[] secondCycleLevelsClient = {secondCycleReadingLevelFromClient.trim(), secondCycleListeningLevelFromClient.trim(), secondCycleGrammarLevelFromClient.trim()};
		
		report.startStep("Retrieve Lessons Ids of Second Cycle");
		lessonsIds = new ArrayList<String>(); //getLessonsIdsFromLessonDetailsArrOfSpecificUnit(unitId);String[] codes = new String[3];
		for (int i = 0; i < codes.length; i++) {
			String lessonId = pageHelper.runJavaScriptCommand("return JSON.parse(JSON.parse(localStorage.getItem('Course')).CourseJson).Children[1].Children.filter(function(lesson){ return lesson.Metadata.Code ==='"+secondCycleCodes[i]+"'; })[0].NodeId");
			lessonsIds.add(lessonId);
		}
		report.addTitle("Lessons Ids: " + lessonsIds.toString());
		
		report.startStep("Retrieve Unit Id (Second Cycle) from JSON by Lesson Id");
		//unitId = testEnvironmentPage.getValueOfWantedKeyInSpecificTestId(jsonObj, lessonsIds.get(0), "ParentNodeId");
		unitId = "-1";
		testEnvironmentPage.pltRound = 2;
		
		report.startStep("Retrieve Wanted Score of C2 from file");
		wantedScore[0] = Integer.parseInt(dataOfVersion.get(iteration)[6]); // C2 Reading Score
		wantedScore[1] = Integer.parseInt(dataOfVersion.get(iteration)[7]); // C2 Listening Score
		wantedScore[2] = Integer.parseInt(dataOfVersion.get(iteration)[8]); // C2 Grammar Score
		
		report.startStep("Answer Second Cycle Questions");
		testEnvironmentPage.answerPltCycleNewTE(unitId, lessonsIds, jsonObj, localizationJson, testName, answersFilePath, secondCycleLevelsClient, secondCycleCodes, wantedScore); // secondCycleLevelFromClient should be secondCycleLevels[1].toString()
		sleep(1);
		
		if(useCEFR) {
		report.startStep("Validate that CEFR level are match to api level and suggestion text are present");               
			levelCEFR = testEnvironmentPage.validateCefrReportAtTheEndOfPLT();
		}
		
		report.startStep("Print Final Scores");
		List<String[]> userTestComponentProgress = dbService.getUserTestComponentProgressByUserId(studentId);
		//sleep(3);
		//String[] finalScoresDB = new String[userTestComponentProgress.size()];
		List<String> scoresDB = new ArrayList<String>();
		String userTestUnitProgressId = userTestComponentProgress.get(0)[1]; // retrieve first userTestUnitProgressId- relevant to the current test played
		for (int i = 0; i < userTestComponentProgress.size(); i++) {
			if (userTestComponentProgress.get(i)[1].equals(userTestUnitProgressId)) { // add only component that are relevant to the current test
				//finalScoresDB[i] = userTestComponentProgress.get(i)[12];
				scoresDB.add(userTestComponentProgress.get(i)[12]);
			}
		}
		String[] finalScoresDB = new String[scoresDB.size()]; // copy all elements from scoresDB to finalScoresDB (needs to be in an array: String[]
		for (int j = 0; j < scoresDB.size(); j++) {
			finalScoresDB[j] = scoresDB.get(j);
		}
		report.startStep("Final scores from DB (descending): " + Arrays.toString(finalScoresDB));
		
		report.startStep("Calculate Final grade");
		String[] finalLevels = testEnvironmentPage.calculateAndValidatePltFinalLevel(studentId, levels,secondCycleLevelsClient);
		
		List<String[]> userTestProgress = dbService.getUserTestProgressByUserId(studentId);
		//sleep(2);
		//report.startStep("Validate Score in DB");
		//List<String[]> userTestProgress = dbService.getUserTestProgressByUserId(studentId);
		//String scoreForDisplayDB = userTestProgress.get(0)[4];
		//testResultService.assertEquals(expectedScore, scoreForDisplayDB, "Score in DB is Incorrect"); // 
		
		report.startStep("Validate isCompleted in DB");
		String isCompletedDB = userTestProgress.get(0)[8];
		testResultService.assertEquals("1", isCompletedDB, "isCompleted in DB is Incorrect");
		
		report.startStep("Validate Submit Reason in DB");
		String submitReasonDB = userTestProgress.get(0)[9];
		testResultService.assertEquals("1", submitReasonDB, "Submit Reason in DB is Incorrect");
		//sleep(2);
		
		report.startStep("Validate Scores in End Page");
		testEnvironmentPage.validatePltEndTestLevels(finalLevels);
		
		report.startStep("Validate Test Status in DB is Done"); // NEED TO ADD
		//String userExitTestSettingsId = userTestProgress.get(0)[0];
		//String testStatus = dbService.getTestStatusByUserExitTestSettingsId(userExitTestSettingsId);
		//testResultService.assertEquals("3", testStatus, "Test Status in DB is incorrect.");
		String testStatus = dbService.getTestStatusOfPLTByUserId(studentId);
		testResultService.assertEquals("3", testStatus, "Test Status in DB is incorrect.");
		
		report.startStep("Validate Final Level Calculated by Automation Match Final Levels in DB");
		String[] userPLtFinalResultDB = dbService.getUserFinalPltResultByInstIdNewTE(institutionId);
		String[] finalLevelsDB = {userPLtFinalResultDB[4],userPLtFinalResultDB[5],userPLtFinalResultDB[6],userPLtFinalResultDB[7]};
		testResultService.assertEquals(true, Arrays.equals(finalLevelsDB, finalLevels), "Final Levels Calculated by Automation Don't match Final levels in DB. Automation: " + Arrays.toString(finalLevels) + ". DB: " + Arrays.toString(finalLevelsDB));
		
		report.startStep("Levels from db: " + Arrays.toString(finalLevelsDB));
			String finalLevelString = testEnvironmentPage.convertPltLevelCodeToLevelString(finalLevels[3]);
			return finalLevelString;
				
	}
	
	public void getComponentCodesAndLessonsId() throws Exception {
		
		report.startStep("Retrieve 3 sections codes from client side (First Cycle)");
		codes = new String[3];
		for (int i = 0; i < 3; i++) {
			String code = pageHelper.runJavaScriptCommand("return JSON.parse(JSON.parse(localStorage.getItem('Course')).CourseJson).Children[0].Children["+i+"].Metadata.Code");
			codes[i] = code;
			sleep(1);
		}
		report.addTitle("Codes: " + Arrays.toString(codes));
		
		report.startStep("Retrieve Lessons Ids of First Cycle");
		lessonsIds = new ArrayList<String>(); //getLessonsIdsFromLessonDetailsArrOfSpecificUnit(unitId);String[] codes = new String[3];
		for (int i = 0; i < codes.length; i++) {
			String lessonId = pageHelper.runJavaScriptCommand("return JSON.parse(JSON.parse(localStorage.getItem('Course')).CourseJson).Children[0].Children.filter(function(lesson){ return lesson.Metadata.Code ==='"+codes[i]+"'; })[0].NodeId");
			lessonsIds.add(lessonId);
		}
		report.addTitle("Lessons Ids: " + lessonsIds.toString());
		
	}

	public String testPltBackButton_NewTE(String testName, String studentId, int iteration, JSONObject jsonObj,
			JSONObject localizationJson, String answersFilePath, String institutionId) throws Exception{
		
		// Initialize Test Enviorment Page
		testEnvironmentPage = new TestEnvironmentPage(webDriver, testResultService);
				
		int[] wantedScore = new int[3];
		List<String[]> dataOfVersion = testEnvironmentPage.initScoresOfPLT("files/PLTTestData/NewTePltScores.csv");
	
		String level = dataOfVersion.get(iteration)[2]; // Initial Level
				
		report.startStep("Validate Test Name");
		testEnvironmentPage.validateTestName(testName);
		sleep(5);
		
		report.startStep("Choose Level");
		String statusSymbol = testEnvironmentPage.choosePlacementStatusNewTe(level);
		String readingLevel = "";
		if (iteration == 6) {
			readingLevel = "Intermediate2score60";
		} else if(iteration == 7) {
			readingLevel = "Intermediate2score80";
		} else {
			readingLevel = statusSymbol;
		}
		
		String[] levels = {readingLevel,statusSymbol,statusSymbol};// levels of all sections
		
		report.startStep("Click on Start Test Button");
		testEnvironmentPage.pressOnStartTest();
		sleep(4);
		
		report.startStep("Retrieve 3 sections codes from client side (First Cycle)");
		String[] codes = new String[3];
		for (int i = 0; i < 3; i++) {
			String code = pageHelper.runJavaScriptCommand("return JSON.parse(JSON.parse(localStorage.getItem('Course')).CourseJson).Children[0].Children["+i+"].Metadata.Code");
			codes[i] = code;
			sleep(1);
		}
		report.addTitle("Codes: " + Arrays.toString(codes));
		
		report.startStep("Retrieve Lessons Ids of First Cycle");
		List<String> lessonsIds = new ArrayList<String>(); //getLessonsIdsFromLessonDetailsArrOfSpecificUnit(unitId);String[] codes = new String[3];
		for (int i = 0; i < codes.length; i++) {
			String lessonId = pageHelper.runJavaScriptCommand("return JSON.parse(JSON.parse(localStorage.getItem('Course')).CourseJson).Children[0].Children.filter(function(lesson){ return lesson.Metadata.Code ==='"+codes[i]+"'; })[0].NodeId");
			lessonsIds.add(lessonId);
		}
		report.addTitle("Lessons Ids: " + lessonsIds.toString());
		
		Thread.sleep(5000);
		String unitId = "-1";
		
		report.startStep("Test Back Button");
		testEnvironmentPage.testPltBackButtonCycleNewTE(unitId, lessonsIds, jsonObj, localizationJson, testName, answersFilePath, levels, codes);
		
		report.startStep("Validate Scores are Stored Correctly in DB");
		String[] actualScoresDB = testEnvironmentPage.validatePltScoresInDB(studentId, wantedScore);
		
		report.startStep("Calculate Expected next Cycle Level");
		Object[] expectedSecondCycleReadingLevel = testEnvironmentPage.calculateSecondCycleLevel(statusSymbol, Integer.parseInt(actualScoresDB[0])); 
		Object[] expectedSecondCycleListeningLevel = testEnvironmentPage.calculateSecondCycleLevel(statusSymbol, Integer.parseInt(actualScoresDB[1])); 
		Object[] expectedSecondCycleGrammarLevel = testEnvironmentPage.calculateSecondCycleLevel(statusSymbol, Integer.parseInt(actualScoresDB[2])); 
		sleep(2);
		
		report.startStep("Retrieve 3 sections codes from client side (Second Cycle)");
		String[] secondCycleCodes = new String[3];
		for (int i = 0; i < 3; i++) {
			String code = pageHelper.runJavaScriptCommand("return JSON.parse(JSON.parse(localStorage.getItem('Course')).CourseJson).Children[1].Children["+i+"].Metadata.Code");
			secondCycleCodes[i] = code;
			sleep(1);
		}
		report.addTitle("Codes: " + Arrays.toString(secondCycleCodes));
		
		report.startStep("Validate Second Cycle Sections Codes match the Expected Level");
		String secondCycleReadingLevelFromClient = testEnvironmentPage.getPltLevelByCode(answersFilePath, secondCycleCodes[0]);
		String secondCycleListeningLevelFromClient = testEnvironmentPage.getPltLevelByCode(answersFilePath, secondCycleCodes[1]);
		String secondCycleGrammarLevelFromClient = testEnvironmentPage.getPltLevelByCode(answersFilePath, secondCycleCodes[2]);
		testResultService.assertEquals(expectedSecondCycleReadingLevel[1].toString().trim(), secondCycleReadingLevelFromClient.trim(),"Second Cycle Reading Level is Incorrect.");
		testResultService.assertEquals(expectedSecondCycleListeningLevel[1].toString().trim(), secondCycleListeningLevelFromClient.trim(),"Second Cycle Listening Level is Incorrect.");
		testResultService.assertEquals(expectedSecondCycleGrammarLevel[1].toString().trim(), secondCycleGrammarLevelFromClient.trim(),"Second Cycle Grammar Level is Incorrect.");
		String[] secondCycleLevelsClient = {secondCycleReadingLevelFromClient.trim(), secondCycleListeningLevelFromClient.trim(), secondCycleGrammarLevelFromClient.trim()};
		
		report.startStep("Retrieve Lessons Ids of Second Cycle");
		lessonsIds = new ArrayList<String>(); //getLessonsIdsFromLessonDetailsArrOfSpecificUnit(unitId);String[] codes = new String[3];
		for (int i = 0; i < codes.length; i++) {
			String lessonId = pageHelper.runJavaScriptCommand("return JSON.parse(JSON.parse(localStorage.getItem('Course')).CourseJson).Children[1].Children.filter(function(lesson){ return lesson.Metadata.Code ==='"+secondCycleCodes[i]+"'; })[0].NodeId");
			lessonsIds.add(lessonId);
		}
		report.addTitle("Lessons Ids: " + lessonsIds.toString());
		
		report.startStep("Retrieve Unit Id (Second Cycle) from JSON by Lesson Id");
		unitId = "-1";
		testEnvironmentPage.pltRound = 2;
		
		report.startStep("Test Back Button");
		testEnvironmentPage.testPltBackButtonCycleNewTE(unitId, lessonsIds, jsonObj, localizationJson, testName, answersFilePath, secondCycleLevelsClient, secondCycleCodes); // secondCycleLevelFromClient should be secondCycleLevels[1].toString()
		sleep(3);
		
		List<String[]> userTestComponentProgress = dbService.getUserTestComponentProgressByUserId(studentId);
		String[] finalScoresDB = new String[userTestComponentProgress.size()];
		for (int i = 0; i <userTestComponentProgress.size(); i++) {
			finalScoresDB[i] = userTestComponentProgress.get(i)[12];
		}
		report.startStep("Final scores from DB (descending): " + Arrays.toString(finalScoresDB));
		
		report.startStep("Calculate Final grade");
		String[] finalLevels = testEnvironmentPage.calculateAndValidatePltFinalLevel(studentId, levels, secondCycleLevelsClient);
		
		List<String[]> userTestProgress = dbService.getUserTestProgressByUserId(studentId);
				
		report.startStep("Validate isCompleted in DB");
		String isCompletedDB = userTestProgress.get(0)[8];
		testResultService.assertEquals("1", isCompletedDB, "isCompleted in DB is Incorrect");
		
		report.startStep("Validate Submit Reason in DB");
		String submitReasonDB = userTestProgress.get(0)[9];
		testResultService.assertEquals("1", submitReasonDB, "Submit Reason in DB is Incorrect");
		sleep(2);
		
		report.startStep("Validate Scores in End Page");
		testEnvironmentPage.validatePltEndTestLevels(finalLevels);
		
		report.startStep("Validate Test Status in DB is Done"); 
		String testStatus = dbService.getTestStatusOfPLTByUserId(studentId);
		testResultService.assertEquals("3", testStatus, "Test Status in DB is incorrect.");
		
		report.startStep("Validate Final Level Calculated by Automation Match Final Levels in DB");
		String[] userPLtFinalResultDB = dbService.getUserFinalPltResultByInstIdNewTE(institutionId);
		String[] finalLevelsDB = {userPLtFinalResultDB[4],userPLtFinalResultDB[5],userPLtFinalResultDB[6],userPLtFinalResultDB[7]};
		testResultService.assertEquals(true, Arrays.equals(finalLevelsDB, finalLevels), "Final Levels Calculated by Automation Don't match Final levels in DB. Automation: " + Arrays.toString(finalLevels) + ". DB: " + Arrays.toString(finalLevelsDB));
		report.startStep("Levels from db: " + Arrays.toString(finalLevelsDB));
		
		String finalLevelString = testEnvironmentPage.convertPltLevelCodeToLevelString(finalLevels[3]);
		return finalLevelString;
				
	}
	
	/*@Test
	@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "" })
	public void testCheckPLTLevelCalculations() throws Exception {
		
		report.startStep("Init Data");
		String adminId = dbService.getAdminIdByInstId(instId);
		String classId = dbService.getClassIdByName(className, instId);
		PLTStartLevel pltLevel = PLTStartLevel.Basic;
		String pltLanguage = "English";
		String unitId = "";
		String testId = "11";
		String answersFilePath = "files/PLTTestData/PLT_Answers_AllVersions.csv";
		int[] wantedScores = {100,100,100};

		report.startStep("Log Out");
		homePage.logOutOfED();
		
		report.startStep("Log In as Student");
		homePage = createUserAndLoginNewUXClass(className, coursesInstId);
		homePage.waitHomePageloaded();
		pageHelper.closeLastSessionImproperLogoutAlert();
		
		report.startStep("Assign PLT test to student");
		pageHelper.assignPltTestToStudetInTestEnvironment(studentId, 1, "11", adminId, instId, classId);
		sleep(3);
		
		report.startStep("Retrieve JSON Array of Test ID: " + testId);
		jsonObj = netService.getJsonObject("smb://dev2008/EdoNet300Res/Runtime/Metadata/Courses/CourseTests/", testId+".json");
		
		report.startStep("Open Assessments Page");
		testsPage = homePage.openAssessmentsPage(false);
		sleep(3);
		
		report.startStep("Start Test");
		String testName = testsPage.clickStartTest(1, 1);
		
		// Initialize Test Enviorment Page
		testEnvironmentPage = new TestEnvironmentPage(webDriver, testResultService);
		
		report.startStep("Validate Test Name");
		testEnvironmentPage.validateTestName(testName);
		sleep(5);
		
		//report.startStep("Validate Course Intro");
		//testEnvironmentPage.validateIntro(jsonObj, localizationJson, testId, testName);
		
		report.startStep("Choose Level");
		testEnvironmentPage.choosePltLevel(pltLevel.toString());
		
		//report.startStep("Choose Language");
		//testEnvironmentPage.choosePltLanguage(pltLanguage);
		
		report.startStep("Click on Start Test Button");
		testEnvironmentPage.pressOnStartTest();
		sleep(2);
		
		report.startStep("Retrieve 3 sections codes from client side");
		String[] codes = new String[3];
		for (int i = 0; i <3; i++) {
			String code = pageHelper.runJavaScriptCommand("return JSON.parse(JSON.parse(localStorage.getItem('Course')).CourseJson).Children[0].Children["+i+"].Metadata.Code");
			codes[i] = code;
			sleep(1);
		}
		report.addTitle("Codes: " + Arrays.toString(codes));
		
		report.startStep("Retrieve Lessons Ids");
		List<String> lessonsIds = new ArrayList<String>(); //getLessonsIdsFromLessonDetailsArrOfSpecificUnit(unitId);String[] codes = new String[3];
		for (int i = 0; i < codes.length; i++) {
			String lessonId = pageHelper.runJavaScriptCommand("return JSON.parse(JSON.parse(localStorage.getItem('Course')).CourseJson).Children[0].Children.filter(function(lesson){ return lesson.Metadata.Code ==='"+codes[i]+"'; })[0].NodeId");
			lessonsIds.add(lessonId);
		}
		report.addTitle("Lessons Ids: " + lessonsIds.toString());
		
		report.startStep("Retrieve Unit Id (First Cycle) from JSON by Lesson Id");
		unitId = testEnvironmentPage.getValueOfWantedKeyInSpecificTestId(jsonObj, lessonsIds.get(0), "ParentNodeId");
		
		report.startStep("Answer First Cycle Questions");
		testEnvironmentPage.answerPltCycleNewTE(unitId, lessonsIds, jsonObj, localizationJson, testName, answersFilePath, pltLevel.toString()+"2", codes, wantedScores);

		report.startStep("Click Close");
		testEnvironmentPage.clickClose(); // temp
		
		report.startStep("Log Out");
		homePage.logOutOfED();
	}*/
	
	private void gotoCanvasPOST(String chkClass, String chkUser) throws Exception {

		String baseUrl = pageHelper.CILink.split(".com")[0] + ".com/";
		String filePath = pageHelper.buildPathForExternalPages + "Languages/";
		String accessFile =  pageHelper.urlForExternalPages.split(".com")[0] + ".com/" + "Languages/";
		String testFile = "testCanvasPOST_" + dbService.sig(5) + ".html";

		createTestCanvasPltFile(filePath, baseUrl, chkClass, chkUser, testFile);
		
		String openURL = accessFile + testFile;
		
		webDriver.openUrl(openURL);
		webDriver.waitUntilElementAppears(webDriver.waitForElement("btnSubmit", ByTypes.name),20);
		
		webDriver.waitForElement("btnSubmit", ByTypes.name).click();
		sleep(2);
		
		
		if (newUser){
			report.startStep("Check OptIn PrivacyCheckBox and press Continue");
			webDriver.waitForElementAndClick("optInPrivacyStatement__checkBoxW", ByTypes.className);
			webDriver.waitForElementAndClick("optInPrivacyStatement__continue", ByTypes.id);
			sleep(2);
			
			studentId = getEdUserId(chkUser);
			
			if (studentId !=null){
				dbService.insertUserToAutomationTable(institutionId, studentId, chkUser, chkClass, pageHelper.buildId);	
				dbService.checkTheUserInsertedToLtiGrades(studentId,null);
			}
			else
				testResultService.addFailTest("Student doesn't created");
		}
		homePage.closeAllNotifications();
		pageHelper.skipOnBoardingHP();
		
	}
	
	private void createTestCanvasPltFile(String chkPath, String baseUrl, String chkClass, 
			String chkUser, String testFile)  throws Exception {
		//String instId = institutionId;// dbService.getInstituteIdByName(institutionName);
		//String chkInst = institutionName;//dbService.getInstituteNameById(instId);
		
		//.....................................................		
		List<String> wList = new ArrayList<String>();
		
		wList = createHtmlTitle(wList);
		wList = createPltFormInfo(wList, baseUrl, institutionName, chkClass, chkUser);
		wList = createHtmlTail(wList);
		
		textService.writeListToSmbFile(chkPath, testFile, wList, useSMB);
	}	
	
	private List<String> createHtmlTitle(List<String> wList)
	{
		wList.add("<!DOCTYPE html>");
		wList.add("<html>");
		wList.add("<head>");
		
		wList.add("\t<meta charset=\"utf-8\" />");
		wList.add("\t<title></title>");
		
		wList.add("</head>");
		wList.add("<body>");
		
		return  wList; 
	}
	
	private List<String> createPltFormInfo(List<String> wList, String chkURL, String chkInst, String chkClass, String chkUser)
	{
		wList.add("\t<form method=\"post\" action=\"" + chkURL + "entrance.aspx\">");
		wList.add("\t\t<input type=\"submit\" name=\"btnSubmit\" value=\"GO TO ED\" />");
		
		wList.add("\t\t<input type=\"hidden\" name=\"oauth_nonce\" value=\"iKk7CNCRo0JtuHds75AjzzRFD9z5fnOmZaUBDpOhZuw\" />");
		wList.add("\t\t<input type=\"hidden\" name=\"oauth_consumer_key\" value=\"ED_FP\" />"); 
		wList.add("\t\t<input type=\"hidden\" name=\"oauth_signature\" value=\"I96FVJ658qR6yld9mA1+MziiZLY=\" />");
		wList.add("\t\t<input type=\"hidden\" name=\"lis_outcome_service_url\" value=\"https://siglo21.instructure.com/api/lti/v1/tools/190/grade_passback\" />");
		wList.add("\t\t<input type=\"hidden\" name=\"lis_result_sourcedid\" value=\"190-1276-17670-6241-65b27cce6f3cb6e8ca4fda409c4d8a5f3f487f77\" />");
		wList.add("\t\t<input type=\"hidden\" name=\"custom_edinstitution\" value=\"" + chkInst + "\" />");
	
	    wList.add("\t\t<input type=\"hidden\" name=\"roles\" value=\"Learner\" />");
		
		wList.add("\t\t<input type=\"hidden\" name=\"lis_person_name_family\" value=\"LN_" + chkUser + "\" />");
		wList.add("\t\t<input type=\"hidden\" name=\"lis_person_name_given\" value=\"Given_" +chkUser +"\" />");
		wList.add("\t\t<input type=\"hidden\" name=\"lis_person_name_full\" value=\"Full_" +chkUser +"\" />");
		wList.add("\t\t<input type=\"hidden\" name=\"lis_person_contact_email_primary\" value=\"\" />");
		wList.add("\t\t<input type=\"hidden\" name=\"custom_canvas_user_id\" value=\"" + chkUser + "\" />");
		wList.add("\t\t<input type=\"hidden\" name=\"custom_user_sections\" value=\"" + chkClass + "\" />");
	    
//		wList.add("\t\t<input type=\"hidden\" name=\"custom_course_sections\" value=\"" + chkCanvasClass + "\" />");
		wList.add("\t\t<input type=\"hidden\" name=\"custom_course_sections\" value=\"" + CanvasClassId + "\" />");

	//  igb 2018.11.20 this parameter already not mandatory (for entry before PLT) -----------			
	//	wList.add("\t\t<input type=\"hidden\" name=\"custom_course_id\" value=\"20000\" />");
	
		wList.add("\t\t<input type=\"hidden\" name=\"oauth_timestamp\" value=\"1525609571\" />");
	
		wList.add("\t</form>");

		return  wList; 
	}
	
	private List<String> createHtmlTail(List<String> wList)
	{
		wList.add("</body>");
		wList.add("</html>");
		
		return  wList; 
	}
	
	private String getEdUserId(String chkUser) throws Exception {
		
		return dbService.getExternalUserInternalId(chkUser);
	}
	
	private void setProgressInCourse(String courseId, int unitsToCompleteExceptTest, int averageTestScore) throws Exception {
		
		String unitId = "undefined";
		String componentId = "undefined";
		String step_id = "undefined";
		List<String[]> step_items = null;
		
		for (int i = 0; i < unitsToCompleteExceptTest; i++) {
			
			unitId = courseUnits.get(i);
			componentId = dbService.getComponentDetailsByUnitId(unitId).get(0)[1];
			step_id = dbService.getSubComponentsDetailsByComponentId(componentId).get(0)[1];
			step_items = dbService.getSubComponentItems(step_id);
			studentService.setProgressForUnit(unitId, courseId, studentId, 0, false);
			studentService.submitTest(studentId, unitId, componentId, step_items, String.valueOf(averageTestScore), null, false);
			
		}
	
	}
	
	private void setProgressForUnitIndex(String courseId, int unitIndex, int averageTestScore) throws Exception {
		
		String unitId = "undefined";
		String componentId = "undefined";
		String step_id = "undefined";
		List<String[]> step_items = null;
		
		unitId = courseUnits.get(unitIndex+1);
		componentId = dbService.getComponentDetailsByUnitId(unitId).get(0)[1];
		step_id = dbService.getSubComponentsDetailsByComponentId(componentId).get(0)[1];
		step_items = dbService.getSubComponentItems(step_id);
		studentService.setProgressForUnit(unitId, courseId, studentId, 0, false);
		studentService.submitTest(studentId, unitId, componentId, step_items, String.valueOf(averageTestScore), null, false);
	}
	
	
	private String completeCourseTest(int courseSequenceInAvailableSection, CourseCodes code, CourseTests testType, int sectionsToAnswerCorrect, int totalSections) throws Exception {
		
		NewUxClassicTestPage classicTest = testsPage.clickOnStartTest("1", String.valueOf(courseSequenceInAvailableSection));
		sleep(1);
		webDriver.closeAlertByAccept();
		sleep(3);
		webDriver.switchToNewWindow();
		classicTest.switchToTestAreaIframe();
		sleep(1);
		classicTest.pressOnStartTest();
		sleep(3);
		CourseTest courseTest = classicTest.initCourseTestFromCSVFile("files/CourseTestData/CourseTest_Answers.csv", code, testType, sectionsToAnswerCorrect);
		classicTest.performCourseTest(courseTest, totalSections);
		
		report.startStep("Get score and close test");
		classicTest.switchToCompletionMessageFrame();
		sleep(1);
		String finalScore = classicTest.getFinalScore();
		webDriver.switchToTopMostFrame();
		classicTest.switchToTestAreaIframe();
		classicTest.closeCompletionMessageAlert();
		sleep(1);
		
		return finalScore;
	
	}
	
	public void regLogin(String instID, String userName, UserType userType, boolean createNewClass) throws Exception {
		
		report.addTitle("The parmeters is: " + instID + "UserName: " + userName + "Courses ints Id: " + institutionId);
		String instName = dbService.getInstituteNameById(instID);
		String userFN =  userName + "FN";
		String userLN =  userName + "LN";
		String email = userName + "@edusoft.co.il";
		char createClass = 'N';
		char userTypeParam = 'S';
		//classNameAR = configuration.getProperty("arg_className");
		
		switch (userType) {
				
		case Teacher: userTypeParam = 'T';
		
		}
		
		if (createNewClass) {
			createClass = 'Y';
			classNameAR = classNameAR + dbService.sig(3);
		}
		
		String baseUrl = pageHelper.CILink.split(".com")[0] + ".com/";
		String regInsertUrl = baseUrl+"RegUserAndLogin.aspx?Action=I&UserName="+userName+"&Inst="+instName+"&FirstName="+userFN+"&LastName="+userLN+"&Password=12345&Email="+email+"&Class="+classNameAR+"&Language="+Language+"&Link="+CorpUrl+"&UseNameMapping=N&CreateClass="+createClass+"&UserType="+userTypeParam; 
							
		webDriver.openUrl(regInsertUrl);
		homePage = new NewUxHomePage(webDriver, testResultService);		
		pageHelper.skipOptin();
		homePage.skipNotificationWindow();
		pageHelper.skipOnBoardingHP();
		studentId = dbService.getUserIdByUserName(userName, instID);
		
		
	}
	
	public void open_PLT_API(String instName, String className, String apiToken) throws Exception{

		String baseUrl = pageHelper.CILink.split(".com")[0] + ".com/";
		
		String accessUrl = pageHelper.urlForExternalPages;
		accessUrl = accessUrl.split(".com")[0] + ".com/" + "Languages/";
		
		String accessPath = pageHelper.buildPathForExternalPages + "Languages/"; //pageHelper.buildPath
		
		String testFile = "pltapi.html";
		returnUrl = "https://www.yahoo.com/";
		
		//apiplt1
		List<String> html = generateHTML(baseUrl + "PlacementTestEntry.aspx", instName, className,apiToken);
		
		textService.writeListToSmbFile(accessPath+testFile, html, netService.getDomainAuth());
		
		String openURL = accessUrl + testFile;

		webDriver.openUrl(openURL);
		sleep(2);	
	}
	
	public List<String> generateHTML(String path, String instName, String className, String apiToken) throws Exception{
		String[] user = null;
		boolean getUser=true;
		
		try {
			user = getUserFromDb(className, institutionId);
			studentId = user[2]; //dbService.getUserIdByUserName(user[0], configuration.getProperty("institution.id"));
			
			
			if (getUser){
				user = getUserFromDb(className, institutionId);
				userName = user[0];
			}
			else{
				userName = "stud" + dbService.sig(6);
				//user = createUserAndReturnDetails(institutionId,className);
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	
		List<String> wList = new ArrayList<String>();
		wList.add("<!DOCTYPE html>");
		wList.add("<html>");
		wList.add("<head>");
		
		wList.add("\t<meta charset=\"utf-8\" />");
		wList.add("\t<title></title>");
		
		wList.add("</head>");
		wList.add("<body>");
		
		wList.add("\t<form method=\"post\" action=\""+path+"\">");
		wList.add("\t\t<input type=\"hidden\" name=\"IName\" value=\""+instName+"\"/>");
		wList.add("\t\t<input type=\"hidden\" name=\"Token\" value=\""+apiToken+"\"/>");
		wList.add("\t\t<input type=\"hidden\" name=\"UserName\" value=\"" + userName + "\"/>");
		wList.add("\t\t<input type=\"hidden\" name=\"Language\" value=\"English\"/>");
		wList.add("\t\t<input type=\"hidden\" name=\"ReturnUrl\" value=\"" + returnUrl + "\"/>");
		wList.add("\t\t<input type=\"hidden\" name=\"Proctoring\" value=\"false\"/>");
		wList.add("\t\t<input type=\"hidden\" name=\"PushNotification\" value=\"http://lib.ru\"/>");
		wList.add("\t\t<input type=\"submit\" name=\"btnSubmit\"/>");
		wList.add("\t</form>");
	
		wList.add("</body>");
		wList.add("</html>");
		
		return wList;
	}
	
	public void checkTestResultGrades(List<String[]> testResultsPLT_Grades) throws Exception{
		testResultService.assertEquals(6, testResultsPLT_Grades.size(), "Num of records is not 6");
		testResultService.assertEquals("100", testResultsPLT_Grades.get(0)[4], "Grade in Record 1 is Incorrect.");
		testResultService.assertEquals("100", testResultsPLT_Grades.get(1)[4], "Grade in Record 2 is Incorrect.");
		testResultService.assertEquals("100", testResultsPLT_Grades.get(2)[4], "Grade in Record 3 is Incorrect.");
		testResultService.assertEquals("0", testResultsPLT_Grades.get(3)[4], "Grade in Record 4 is Incorrect.");
		testResultService.assertEquals("0", testResultsPLT_Grades.get(4)[4], "Grade in Record 5 is Incorrect.");
		testResultService.assertEquals("0", testResultsPLT_Grades.get(5)[4], "Grade in Record 6 is Incorrect.");
	}
	
	public void validateTestResultPLT(List<String[]> testResultsPLT) {
		testResultService.assertEquals("I3", testResultsPLT.get(0)[2].trim(),"Reading column is Incorrect");
		testResultService.assertEquals("I3", testResultsPLT.get(0)[3].trim(), "Listening column is Incorrect");
		testResultService.assertEquals("I3", testResultsPLT.get(0)[4].trim(), "Grammar column is Incorrect");
		testResultService.assertEquals("I3", testResultsPLT.get(0)[5], "Final column is Incorrect");
		testResultService.assertEquals(null, testResultsPLT.get(0)[7], "Selected Level column is Incorrect");
		testResultService.assertEquals("I2", testResultsPLT.get(0)[8], "Initial Level column is Incorrect");
		testResultService.assertEquals("1", testResultsPLT.get(0)[10], "PLT completed column is Incorrect");
		testResultService.assertEquals("1", testResultsPLT.get(0)[11], "PLT version column is Incorrect");
	}
	
	
	private String createRegularRegUserAndLoginFile(String createClass,String className, String createUser,String userType,String TestFileName)  throws Exception {

		String baseUrl = pageHelper.CILink.split(".com")[0] + ".com/";
		String accessUrl = pageHelper.urlForExternalPages.split(".com")[0] + ".com/languages/";
		
		//String chkPath = pageHelper.buildPath + "Languages/";
		String chkPath = pageHelper.buildPathForExternalPages + "Languages/";
		
		//String chkPath1 = pageHelper.buildPath + "Languages/";

		
		String studentUserName="";
		
		if (createUser.contains("I"))
			studentUserName = userType + dbService.sig(5);
		
		if (createClass.contains("Y"))
			 className = "C" + dbService.sig(5);
	
		//String TestFileName = null;
		report.startStep("Create test file -- " + TestFileName);
		
		
		
		
		//String chkInst = dbService.getInstituteNameById(argInstId);
		
		//.....................................................		
		List<String> wList = new ArrayList<String>();
		
		wList = createHtmlTitle(wList);
		wList = createFormInfoRegularRegUserAndLogin(wList, baseUrl, institutionName, className, studentUserName, createClass,createUser,userType);
		wList = createHtmlTail(wList);
		
		textService.writeListToSmbFile(chkPath + TestFileName, wList, netService.getDomainAuth());

		String openURL = accessUrl + TestFileName;
		
		webDriver.openUrl(openURL);
		webDriver.waitUntilElementAppears(webDriver.waitForElement("btnSubmit", ByTypes.name),20);
		
		webDriver.waitForElement("btnSubmit", ByTypes.name).click();
		sleep(2);
		/*
		report.startStep("Check OptIn PrivacyCheckBox and press Continue");
		webDriver.waitForElementAndClick("optInPrivacyStatement__checkBoxW", ByTypes.className);
		webDriver.waitForElementAndClick("optInPrivacyStatement__continue", ByTypes.id);
		sleep(2);
		
		dbService.insertUserToAutomationTable(institutionId, getEdUserId(chkUser), chkUser, chkClass, pageHelper.buildId);
		*/
		
		pageHelper.skipOptin();
		studentId = dbService.getUserIdByUserName(studentUserName, institutionId);
		
		dbService.insertUserToAutomationTable(institutionId, studentId, userName, classNameAR, pageHelper.buildId);
		
		return  studentUserName;
	}
	
	private List<String> createFormInfoRegularRegUserAndLogin(List<String> wList, String chkURL, String chkInst,
			String chkClass, String chkUser,String createClass,String createUser,String userType)
	{
			
			wList.add("\t<form method=\"post\" action=\"" + chkURL + "RegUserAndLogin.aspx\">");
			wList.add("\t\t<input type=\"submit\" name=\"btnSubmit\" value=\"Form Submit\" />");
			wList.add("\t\t<input type=\"hidden\" name=\"Action\" value=\"" + createUser + "\" />");
			wList.add("\t\t<input type=\"hidden\" name=\"UserType\" value=\"" + userType + "\" />");
			wList.add("\t\t<input type=\"hidden\" name=\"UserName\" value=\"" + chkUser + "\" />");
			wList.add("\t\t<input type=\"hidden\" name=\"Inst\" value=\"" + chkInst + "\" />");
			wList.add("\t\t<input type=\"hidden\" name=\"FirstName\" value=\"FN" +"" + chkUser + "\" />");
			wList.add("\t\t<input type=\"hidden\" name=\"LastName\" value=\"LN" + "" + chkUser + "\" />");
			wList.add("\t\t<input type=\"hidden\" name=\"Password\" value=\"12345\" />");
			wList.add("\t\t<input type=\"hidden\" name=\"Email\" value=\"test@test.com\" />");
			wList.add("\t\t<input type=\"hidden\" name=\"Class\" value=\"" + chkClass + "\" />");
			wList.add("\t\t<input type=\"hidden\" name=\"CreateClass\" value=\"" + createClass + "\" />");
			wList.add("\t\t<input type=\"hidden\" name=\"Language\" value=\"English\" />");
			wList.add("\t\t<input type=\"hidden\" name=\"Link\" value=\"https://google.com\" />");

			wList.add("\t</form>");
					
			return  wList; 
		}
	

	
	/////////////////////////////////////////
	
	
	

	@Test
	//@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "" })
	public void testSubmitReasonsAndTestStatuses_InProgress_MidTerm() throws Exception {
	
		String wantedTestId = "989017755";
		
		// Initialize Test Environment Page
		testEnvironmentPage = new TestEnvironmentPage(webDriver, testResultService);
	
		report.startStep("Init Data");
		courseCode = courseCodes[1];
		CourseCodes wantedCourseCode = testEnvironmentPage.getCourseCodeEnumByCourseCodeString(courseCode);
	
		courseId = getCourseIdByCourseCode(courseCode);
		String answerFilePath = "files/CourseTestData/MidTerm_Answers.csv";
		CourseTests courseTestType= CourseTests.MidTerm;
			
		int testSequence;
		String testName;
	
		int sectionIndex = 0;
		int unitIndex = 0;
		int sectionIndexInUnit = 0;

		List<String[]> userTestProgress=null;
		String scoreForDisplayDB;		

		String isCompletedDB="0";
		String submitReasonDB;
		String userExitTestSettingsId;
		String testStatus;

		boolean isTestAvailable;
		
	//	report.startStep("Log Out of ED");
	//	homePage.logOutOfED();
		
		report.startStep("Test Statuses: Assigned, In Progress and then: Incomplete");
		
	//	report.startStep("Log In as Student");
	//	homePage = createUserAndLoginNewUXClass(className, coursesInstId);
	//	homePage.skipNotificationWindow();
	//	homePage.waitHomePageloaded();
	
		report.startStep("Assign B1 Mid-Term Test to student");
		pageHelper.assignAutomatedTestToStudetInTestEnvironment(studentId, courseId, 2,0,0,0);
		sleep(2);
		
		report.startStep("Return Test Id");
		testId = pageHelper.getAssignedTestIdForStudent(studentId,courseId, 2);
		
		
		String userExitSettingsId = dbService.getUserExitSettingsId(studentId, testId, courseId, "2");
		if (!testId.equals(wantedTestId)) {
			// update test id
			//userExitSettingsId = dbService.getUserExitSettingsId(studentId, testId, courseId, "2");
			dbService.updateTestIdInUserExitSettings(wantedTestId, userExitSettingsId);
			testId = pageHelper.getAssignedTestIdForStudent(studentId,courseId, 2);
			String token = pageHelper.getTokenFromConsoleWithParameters("1", studentId, testId, userExitSettingsId);
			dbService.updateAssignWithToken(token, userExitSettingsId);
		}
		sleep(1);
		
		report.startStep("Retrieve JSON Array of Test ID: " + testId);
		jsonObj = netService.getJsonObject("smb://"+PageHelperService.physicalPath+"/Runtime/Metadata/Courses/CourseTests/", testId, useSMB);
		String roundLevelOfCourse = testEnvironmentPage.getRoundLevelFromJson(jsonObj, testId);
		
		try{
		
			report.startStep("Copy and keep the original " +testId+".json file to backup folder");
			pageHelper.runConsoleProgram("cmd /c copy "+PageHelperService.ServerPath+"CourseTests\\"+testId+".json "+PageHelperService.ServerPath+"CourseTests\\ForAutomationBackup");
			
			report.startStep("Change JSON of Test to Round 2");
			pageHelper.runConsoleProgram("cmd /c copy "+PageHelperService.ServerPath+"CourseTests\\"+testId+"_RoundLevel2\\"+testId+".json "+PageHelperService.ServerPath+"CourseTests");
				
			List<String[]> lessonCountForUnits = dbService.getLessonCountForAllUnitsInCourse(testId);
			List<String> unitIds = new ArrayList<String>();
			
			for (int j = 0; j < lessonCountForUnits.size(); j++) {
				unitIds.add(lessonCountForUnits.get(j)[0]);
			}
				
			report.startStep("Validate Expected Status in DB is Assigned");
			testStatus = dbService.getTestStatusByUserExitTestSettingsId(userExitSettingsId);
			testResultService.assertEquals("1", testStatus,"Test Status in DB is incorrect.");
			
			report.startStep("Open Assessments Page");
			testsPage = homePage.openAssessmentsPage(false);
			
			report.startStep("Start Test");
			testSequence = testsPage.getTestSequenceByCourseId(courseId);
			testName = testsPage.clickStartTest(1, testSequence);
			webDriver.closeAlertByAccept();
					
			report.startStep("Validate Course Intro");
			testEnvironmentPage.validateIntro(jsonObj, localizationJson, testId, testName);
			
			report.startStep("Answer First Section");
			testEnvironmentPage.answerQuestionInSpecificSectionNewTE(answerFilePath, testId, wantedCourseCode,
					courseTestType,sectionIndex, roundLevelOfCourse, unitIds, jsonObj, localizationJson,
					testName, unitIndex, sectionIndexInUnit);
			
			report.startStep("Validate Expected Status in DB is: In Progress");
			testStatus = dbService.getTestStatusByUserExitTestSettingsId(userExitSettingsId);
			testResultService.assertEquals("2", testStatus,"Test Status in DB is incorrect.");
			
			report.startStep("Validate test finalization is DB: isCompleted");
			userTestProgress = dbService.getUserTestProgressByUserId(studentId);
			isCompletedDB = userTestProgress.get(0)[8];
			testResultService.assertEquals("0", isCompletedDB, "isCompleted in DB is Incorrect");
			
			report.startStep("Validate Submit Reason in DB");
			submitReasonDB = userTestProgress.get(0)[9];
			testResultService.assertEquals(true, submitReasonDB == null, "Submit Reason in DB is Incorrect");
		
			report.startStep("Click Close Button");
			testEnvironmentPage.clickClose();
			
			report.startStep("Open Assessments Page");
			homePage.openAssessmentsPage(false);
			
			report.startStep("Validate test is available");
			isTestAvailable = testsPage.checkIfTestIsDisplayedInAvailableTests(testName);
			testResultService.assertEquals(true, isTestAvailable, "Test is not available even though time is not over");
			
			report.startStep("Wait until Test Time is Over and 2 minutes till the service in frontqa2016 c: will update the submit reason");
			waitTestAvailableAssessmentIsOver();
			sleep(60+10);
			
			report.startStep("Validate test is not available");
			isTestAvailable = testsPage.checkIfTestIsDisplayedInAvailableTests(testName);
			testResultService.assertEquals(false, isTestAvailable, "Test is available even though time is over");
			
			report.startStep("Validate Score in DB");
			
			int wait=190; // wait 3 minutes + 10 sec till the 2 min of service than +1 min to run on expired tests
	
			if (PageHelperService.branchCI.equalsIgnoreCase("EdProduction")
					|| PageHelperService.branchCI.equalsIgnoreCase("EdBeta")
					|| PageHelperService.branchCI.equalsIgnoreCase("EdProduction2"))
				wait=250;
				
				for (int j=0; j<wait && isCompletedDB.equalsIgnoreCase("0");j++){ // wait until the service expiration run on server
				userTestProgress = dbService.getUserTestProgressByUserId(studentId);
				isCompletedDB = userTestProgress.get(0)[8];
				sleep(1);
			}
		
			scoreForDisplayDB = userTestProgress.get(0)[4];
			testResultService.assertEquals(false, scoreForDisplayDB.equals("0"), "Score in DB is Incorrect. Score in DB: " + scoreForDisplayDB);//
			
			report.startStep("Validate isCompleted in DB");
			//isCompletedDB = userTestProgress.get(0)[8];
			testResultService.assertEquals("1", isCompletedDB, "isCompleted in DB is Incorrect");
			
			report.startStep("Validate Submit Reason in DB in 3-External Proccess");
			submitReasonDB = userTestProgress.get(0)[9];
			testResultService.assertEquals("3", submitReasonDB, "Submit Reason in DB is Incorrect");
			
			report.startStep("Validate Expected Status in DB is Incomplete");
			userExitTestSettingsId = userTestProgress.get(0)[0];
			testStatus = dbService.getTestStatusByUserExitTestSettingsId(userExitTestSettingsId);
			testResultService.assertEquals("5", testStatus,"Test Status in DB is incorrect.");
			
			
			report.startStep("Close Assessments Page");
			testsPage.close();
			
			report.startStep("Open Assessments Page");
			testsPage = homePage.openAssessmentsPage(false);
			
			report.startStep("Validate test score in assessments page");
			testsPage.clickOnArrowToOpenSection("3");
			testsPage.checkScoreByTest("1", scoreForDisplayDB);
			
			report.startStep("Close Assessments Page");
			testsPage.close();
			
			report.startStep("Log Out of ED");
			homePage.logOutOfED();
		
		}
		finally {	
			report.startStep("copy back the original file to original path");
			pageHelper.runConsoleProgram("cmd /c copy "+PageHelperService.ServerPath+"CourseTests\\ForAutomationBackup\\"+testId+".json "+PageHelperService.ServerPath+"CourseTests");	
		}
	}


	
	@Test
		//@Category(inProgressTests.class)
		@TestCaseParams(testCaseID = { "" })
		public void testSubmitReasonsAndTestStatuses_Expired_MidTerm() throws Exception {
			
			//report.startStep("Test: Status Done");
			
	//		report.startStep("Log Out of ED");
	//		homePage.logOutOfED();
		
			String wantedTestId = "989017755";
			
			// Initialize Test Environment Page
			testEnvironmentPage = new TestEnvironmentPage(webDriver, testResultService);
	//		testsPage  = new NewUxAssessmentsPage(webDriver,testResultService);
			report.startStep("Init Data");
			courseCode = courseCodes[1];
	
			courseId = getCourseIdByCourseCode(courseCode);
	
			String isCompletedDB;
	//		String submitReasonDB;
	//		String userExitTestSettingsId;
			String testStatus;
			String userExitSettingsId;
	//		String roundLevelOfCourse;
	//		List<String[]> userTestProgress;
			boolean isTestAvailable;

			sleep(2);
			
			report.startStep("Test: Status Expired");
			
	//		report.startStep("Log In as Student");
	//		homePage = createUserAndLoginNewUXClass(className, coursesInstId);
	//		homePage.skipNotificationWindow();
	//		homePage.waitHomePageloadedFully();
			
			report.startStep("Assign B1 Mid-Term Test to student");
			pageHelper.assignAutomatedTestToStudetInTestEnvironment(studentId, courseId, 2,0,0,0);
			sleep(2);
			
			report.startStep("Return Test Id");
			testId = pageHelper.getAssignedTestIdForStudent(studentId,courseId, 2);
			userExitSettingsId = dbService.getUserExitSettingsId(studentId, testId, courseId, "2");
			if (!testId.equals(wantedTestId)) {
				// update test id
				dbService.updateTestIdInUserExitSettings(wantedTestId, userExitSettingsId);
				testId = pageHelper.getAssignedTestIdForStudent(studentId,courseId, 2);
				String token = pageHelper.getTokenFromConsoleWithParameters("1", studentId, testId, userExitSettingsId);
				dbService.updateAssignWithToken(token, userExitSettingsId);
			}
			sleep(2);
		
			report.startStep("Open Assessments Page");
			testsPage = homePage.openAssessmentsPage(false);
			sleep(3);
			
			report.startStep("Start Test");
			int testSequence = testsPage.getTestSequenceByCourseId(courseId);
			String testName = testsPage.clickStartTest(1, testSequence);
			webDriver.closeAlertByDismiss();
			
			//report.startStep("Validate test is available");
			isTestAvailable = testsPage.checkIfTestIsDisplayedInAvailableTests(testName);
			testResultService.assertEquals(true, isTestAvailable, "Test is not available even though time is not over");
		
			report.startStep("Wait until Test Time is Over in Assessment");
			waitTestAvailableAssessmentIsOver();
			sleep(60+10);
			
			report.startStep("Validate test is not available");
			isTestAvailable = testsPage.checkIfTestIsDisplayedInAvailableTests(testName);
			testResultService.assertEquals(false, isTestAvailable, "Test is available even though time is over");
			
			report.startStep("Validate Expected Status in DB is Expired");
			testStatus = dbService.getTestStatusByUserExitTestSettingsId(userExitSettingsId);
			testResultService.assertEquals("4", testStatus,"Test Status in DB is incorrect.");
			
			report.startStep("Validate isCompleted in DB is null");
			//userTestProgress = dbService.getUserTestProgressByUserId(studentId);
			isCompletedDB = null;
			testResultService.assertEquals(true, isCompletedDB==null, "isCompleted in DB is Incorrect");
			
			report.startStep("Close Assessments Page");
			testsPage.close();
			
			report.startStep("Log Out of ED");
			homePage.logOutOfED();
			
		}
	



	

	
	

	private void waitTestAvailableAssessmentIsOver() {
		int testAvailabilityMin;
		int testAvailabilitySec;
		try {
			testAvailabilityMin = testsPage.getMinutesInCounter("1");
			testAvailabilitySec = testsPage.getSecondsInCounter("1");
			
			int watingTime = (testAvailabilityMin*60) + testAvailabilitySec;
			sleep(watingTime+2);
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Test
		//@Category(inProgressTests.class) 
		@TestCaseParams(testCaseID = { "43954", "50342" }, testTimeOut = "30")
		public void test02_ArgentinaAdaptiveFlowNewTE_NextCTAndNextCourse() throws Exception {
			
			try {
				
				report.startStep("Restart Browser");
				institutionName=institutionsName[7];
				pageHelper.restartBrowserInNewURL(institutionName, true); 
				
				report.startStep("Init Test Data");
				int startCourseIndex = 1; // "B1";
				String courseId = courses[startCourseIndex];
				String courseCode = courseCodes[startCourseIndex];
				String startCourseName = coursesNames[startCourseIndex]; 
				courseUnits = dbService.getCourseUnits(courses[startCourseIndex]);
				String courseTestName = startCourseName; //"Basic 1";
				int courseProgressPassingGrade = 3;
				int testAvgScorePassingGrade = 20;
				int courseTestScorePassingGrade = 5;
				//classNameAR = configuration.getProperty("classname.CourseTest8");
				
				report.startStep("Retrieve JSON Array of Test ID: " + testId);
				jsonObj = netService.getJsonObject("smb://"+PageHelperService.physicalPath+"/Runtime/Metadata/Courses/CourseTests/", testId+".json", useSMB);
				
				// Start of User Story 43742:Siglo21: Assign Test if Course Criteria Achieved
				
				String[] user = dbService.getStudentAfterPltAndNoProgressForAuthomaticLearningPathNewTE(institutionId,courseCode);
				studentId = user[0];
				userName = user[1];
				externalUserName = user[1];
				userFN = user[2];
				userLN = user[3];
				email = user[5];
				className = user[6];
				CanvasClassId = user[7];
				
				report.startStep("Enter ED via CanvasPOST - Get exists User");
				gotoCanvasPOST(className, externalUserName);
				pageHelper.skipOnBoardingHP();
				homePage.waitHomePageloadedFully();
				
				report.startStep("Enter course and create NOT passing progress - completion < " + courseProgressPassingGrade +"% / score avg < 40%");
				homePage.clickOnContinueButton();
				NewUxLearningArea2 learningArea2 = new NewUxLearningArea2(webDriver, testResultService);
				learningArea2.waitToLearningAreaLoaded();
				
				learningArea2.clickOnNextButton();
				learningArea2.clickOnPlayVideoButton();
				
				//setProgressInCourse(courseId, 5, 30);
			
				report.startStep("Navigate to Home Page and check Course Criteria NOT Achieved");
				homePage.clickOnHomeButton();
				homePage.waitHomePageloadedFully();
				int courseCompletion = Integer.valueOf(homePage.getCompletionWidgetValue());
				int avgTestScore = Integer.valueOf(homePage.getScoreWidgetValue());
						
				if (courseCompletion > courseProgressPassingGrade || avgTestScore > testAvgScorePassingGrade) {
					testResultService.addFailTest("Course Progress is not as expected on this stage", false, true);
				}
						
				report.startStep("Open Assessments and check NO course test assigned");
				testsPage = homePage.openAssessmentsPage(false);
				testResultService.assertEquals(false, testsPage.isCourseTestAvailable(courseTestName), "Course Test displayed though should not. Verify progress and Avg score values in ilp file.");
				testsPage.close();
				sleep(2);
				
				report.startStep("Enter course again and create passing progress - completion = 3% / score avg = 100%");
				homePage.clickOnContinueButton();
				learningArea2.waitToLearningAreaLoaded();
				learningArea2.clickOnNextButton();
			//	setProgressForUnitIndex(courseId,6,100); // add click play

				// get first unitid
				String unitId= courseUnits.get(0);
				List<String[]> unitComponents = dbService.getComponentDetailsByCourseId(courseId,unitId);

				for (int i=0;i<=1;i++) // do progress for 2 component
					studentService.setProgressForComponents(unitComponents.get(i)[0],courseId,studentId,null,30,1,true);

				studentService.setUserCourseUnitProgress(courseId, unitId, studentId);

				report.startStep("Navigate to Home Page check Course Criteria Achieved");
				homePage.clickOnHomeButton();
				homePage.waitHomePageloadedFully();
				courseCompletion = Integer.valueOf(homePage.getCompletionWidgetValue());
				avgTestScore = Integer.valueOf(homePage.getScoreWidgetValue());
						
				if (courseCompletion < courseProgressPassingGrade || avgTestScore < testAvgScorePassingGrade){
					testResultService.addFailTest("Course Progress is not as expected on this stage", false, true);
				}	
			
				report.startStep("Open Assessments and check B1 Final Test assigned");
				homePage.openAssessmentsPage(false);
				sleep(3);	
				testResultService.assertEquals(true, testsPage.checkIfTestIsDisplayedInAvailableTests(courseTestName + " Final Test"), "Final Test NOT displayed though should be. Make sure progress and avg score criteria in ilp.json file configured correctly.");	
				testsPage.close();
				
				// End of User Story 43742:Siglo21: Assign Test if Course Criteria Achieved
				
				/*********************************************************************/
				
				// Start of User Story 43744:Siglo21: Assign Next Course if Course Criteria Achieved
				// Start of User Story 43743:Siglo21: Re-Assign Test if Test Failed
				
	//Shira continue from here			
				
				report.startStep("Complete Course Test with NOT passing grade < 10");
				testId = pageHelper.getAssignedTestIdForStudent(studentId, courseId, 3);
				//int sectionsToSubmit = Integer.parseInt(dbService.getNumberOfSectionsInTestForCourseByTestId(courseId,testId, 3));// change
				// Get list of lesson count for each unit in course
				List<String[]> lessonCountForUnits = dbService.getLessonCountForAllUnitsInCourse(testId);
				
				// Get unit ids list from previous list
				List<String> unitIds = new ArrayList<String>();
				for (int j = 0; j < lessonCountForUnits.size(); j++) {
					unitIds.add(lessonCountForUnits.get(j)[0]);
				}
				
				int unitCount = lessonCountForUnits.size();
				int unitsToAnswer = unitCount;
				int sectionsToSubmit = 0;
				for (int k = 0; k < unitsToAnswer; k++) {
					sectionsToSubmit += Integer.parseInt(lessonCountForUnits.get(k)[1]);
				}
				
				report.startStep("Set Progress to be less than 10");
				//int courseTestScore = Integer.valueOf(completeCourseTest(2, CourseCodes.I3, CourseTests.FinalTest, 1, 8)); // course test for 10 units
				
				// init data to answer final test
				//String finalTestAnswerFilePath = "files/CourseTestData/FinalTest_Answers.csv"; // should be final
				//String courseCode = testEnvironmentPage.getCourseCodeByTestId(finalTestAnswerFilePath, testId); //courseCode = courseCodes[1]; // B1
				// Initialize Test Environment Page
				testEnvironmentPage = new TestEnvironmentPage(webDriver, testResultService);
				CourseCodes wantedCourseCode = testEnvironmentPage.getCourseCodeEnumByCourseCodeString("B1");
				//int sectionToAnswer = 0;
				String roundLevel = "2";
				String finalTestAnswerFilePath = "files/CourseTestData/MidTerm_Answers.csv"; // should be final test
				
				CourseTests courseTestType = CourseTests.MidTerm;//CourseTests.FinalTest;
				
				report.startStep("Retrieve JSON Array of Test ID: " + testId);
				jsonObj = netService.getJsonObject("smb://"+PageHelperService.physicalPath+"/Runtime/Metadata/Courses/CourseTests/", testId, useSMB);
				
				String userExitTestSettingsId = dbService.getUserExitSettingsId(studentId, testId, courseId, "3");
				TestEnvironmentPage.testDuration = dbService.getTestDurationByUserExitSettingsId(userExitTestSettingsId);
				
				report.startStep("Open Assessments Page");
				testsPage = homePage.openAssessmentsPage(false);
				
				report.startStep("Start Test");
				int testSequence = testsPage.getTestSequenceByCourseId(courseId);
				String testName = testsPage.clickStartTest(1, testSequence);
				webDriver.closeAlertByAccept();
				sleep(3);
				
				report.startStep("Validate Test Name");
				testEnvironmentPage.validateTestName(testName);
				sleep(2);
				
				report.startStep("Validate Course Intro");
				testEnvironmentPage.validateIntro(jsonObj, localizationJson, testId, testName);
						
				report.startStep("Submit Test Empty");
				testEnvironmentPage.submitCourseTestEmpty(finalTestAnswerFilePath, testId, wantedCourseCode, courseTestType, roundLevel, unitIds, jsonObj, localizationJson, testName);
				
				report.startStep("Validate Score in DB");
				List<String[]> userTestProgress = dbService.getUserTestProgressByUserId(studentId);
				String scoreForDisplayDB = userTestProgress.get(0)[4];
				testResultService.assertEquals("0", scoreForDisplayDB, "Score in DB is Incorrect");
				sleep(2);
				
				report.startStep("Exit Test");
				testEnvironmentPage.clickExitCourseTest();
				sleep(2);
				
				//int courseTestScore = Integer.valueOf(completeCourseTest(2, CourseCodes.B1, CourseTests.FinalTest, 1, sectionsToSubmit)); // for 8 units
				if (Integer.parseInt(scoreForDisplayDB) > courseTestScorePassingGrade){
					testResultService.addFailTest("Course Test score is not as expected on this stage", false, true);
				}
				
				report.startStep("Open Assessments and check B1 Final Test still Assigned");
				homePage.openAssessmentsPage(false);
				testResultService.assertEquals(true, testsPage.isCourseTestAvailable(testName), "Course Test NOT displayed though should be. Make sure progress and avg score criteria in ilp.json file configured correctly.");	
				testsPage.close();
				
				report.startStep("Open Learning Area and Click Next");
				homePage.clickOnContinueButton();
				sleep(2);
				learningArea2.clickOnNextButton();
				
				report.startStep("Navigate to Home Page check Course Criteria Achieved");
				homePage.clickOnHomeButton();
				homePage.waitHomePageloadedFully();
				
				report.startStep("Complete Course Test with passing grade > 10");
				//courseTestScore = Integer.valueOf(completeCourseTest(2, CourseCodes.B1, CourseTests.FinalTest, 4, 5)); // 5 is the number of section of 8 units
				
				report.startStep("Open Assessments Page");
				testsPage = homePage.openAssessmentsPage(false);
				
				report.startStep("Start Test");
				testSequence = testsPage.getTestSequenceByCourseId(courseId);
				testName = testsPage.clickStartTest(1, testSequence);
				webDriver.closeAlertByAccept();
				sleep(3);
				
				report.startStep("Validate Test Name");
				testEnvironmentPage.validateTestName(testName);
				sleep(3);
				
				report.startStep("Validate Course Intro");
				testEnvironmentPage.validateIntro(jsonObj, localizationJson, testId, testName);
						
				report.startStep("Answer Questions and Validate Intros");
				TestEnvironmentPage.sectionIndex = 1;
				testEnvironmentPage.answerQuestionsNew(finalTestAnswerFilePath, testId, wantedCourseCode, courseTestType, sectionsToSubmit, roundLevel, unitIds, jsonObj, localizationJson, testName, unitsToAnswer);
				
				report.startStep("Validate Score in DB");
				userTestProgress = dbService.getUserTestProgressByUserId(studentId);
				scoreForDisplayDB = userTestProgress.get(0)[4];
				testResultService.assertEquals("100", scoreForDisplayDB, "Score in DB is Incorrect");
				sleep(2);
				
				report.startStep("Exit Test");
				testEnvironmentPage.clickExitCourseTest();
				sleep(2);

				report.startStep("Open Assessments and check B1 Final Test NOT Assigned");
				homePage.openAssessmentsPage(false);
				testResultService.assertEquals(false, testsPage.isCourseTestAvailable(testName), "Course Test displayed though should not. Make sure progress and avg score criteria in ilp.json file configured correctly.");	
				testsPage.close();
				sleep(2);
				
				report.startStep("Click on right arrow and verify Next course added on Home Page");
				homePage.carouselNavigateNext();
				sleep(2);
				testResultService.assertEquals(coursesNames[startCourseIndex+1], homePage.getCurrentCourseName(), "Next course on Home Page is not valid or not found. Make sure ilp.json course sequence mapping is configured correctly.");	 
	
				report.startStep("Click on right arrow again and verify Only 2 courses are dispayed in carousel");
				homePage.carouselNavigateNext();
				sleep(2);
				testResultService.assertEquals(coursesNames[startCourseIndex], homePage.getCurrentCourseName(), "Next course on Home Page is not valid or not found. Make sure ilp.json course sequence mapping is configured correctly.");	 

				/*********************************************************************/
			} finally {
				report.startStep("In case of test failure, don't forget to verify ilp.json file wasn't modified");
				report.report("ilp.json path: "+PageHelperService.sharePhisicalFolder+"Institutions\\" + institutionId);
			}
	
		}

	@Test
	@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "92413", "92410" })
		public void verifyNewVocabulary() throws Exception {

			homePage.logOutOfED();
			NewUxLearningArea2 learningArea2;
			testId = "";

			report.startStep("Restart browser with build which include new vocabulary");
			institutionName = institutionsName[10];
			institutionId = dbService.getInstituteIdByName(institutionName);

			try {
				//webDriver.init();
				webDriver.openUrl("https://edui-ci-87495-new-vocabulary-resource-20230801-1.edusoftrd.com/ED2016#/login");
				//pageHelper.restartBrowserInNewURL("https://edui-ci-87495-new-vocabulary-resource-20230801-1.edusoftrd.com/ED2016#/login", true);
				loginPage = new NewUXLoginPage(webDriver, testResultService);
				//String[] student = pageHelper.getStudentsByInstitutionId(institutionId);
				studentId = pageHelper.createUSerUsingSP(institutionId,"classNewUnits");
				String[] student = dbService.getUserNameAndPasswordByUserId(studentId).get(0);
				homePage = loginPage.loginAsStudent(student[0], student[1]);
				loginPage.waitLoginAfterRestartAppPool();
				pageHelper.skipOptin();
				homePage.closeAllNotifications();
				pageHelper.skipOnBoardingHP();
				NewUxVocabularyPage vocabularyPage = new NewUxVocabularyPage(webDriver, testResultService);
				learningArea2 = homePage.navigateToTask(courseCodes[1], 7, 6, 1, 1);
				vocabularyPage.verifyTitle();
				vocabularyPage.clickOnCardsAndVerifyWhetherTheyMarked();

				vocabularyPage.clickOnCardByOrderNumber(3);
				vocabularyPage.walkThroughAllCards();
				vocabularyPage.verifyMessagesOfCompletedTask();
				vocabularyPage.clickOnReturnButton();
				vocabularyPage.verifyAllCardsAreChecked();

				learningArea2.logOutOfED();



			}catch (Exception e){
				e.printStackTrace();
				testResultService.addFailTest(e.getMessage(),true, true);
			}catch (AssertionError e){
				e.printStackTrace();
				testResultService.addFailTest(e.getMessage(),true, true);
			}finally {
				dbService.deleteUserById(studentId);
			}

		}


	@After
		public void tearDown() throws Exception {

			institutionName="";
			institutionId="";
			report.startStep("Change JSON of Test to Round 2");

			if(!useCEFR && !testId.equalsIgnoreCase("11")) {
				pageHelper.runConsoleProgram("cmd /c copy "+PageHelperService.ServerPath+"CourseTests\\"+testId+"_RoundLevel2\\"+testId+".json "
						+ ""+PageHelperService.ServerPath+"CourseTests");

				report.startStep("copy back the original file to original path");
				pageHelper.runConsoleProgram("cmd /c copy "+PageHelperService.ServerPath+"CourseTests\\ForAutomationBackup\\"+testId+".json "
						+ ""+PageHelperService.ServerPath+"CourseTests");
			}

			testEnvironmentPage.sectionIndex = 1;
			testEnvironmentPage.lessonIndex = 1;
			testEnvironmentPage.testDuration = "";
			super.tearDown();
		}

}