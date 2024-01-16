package tests.tms;


import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.tools.ant.property.GetProperty;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import Enums.ByTypes;
import Enums.CourseCodes;
import Enums.CourseTests;
import Interfaces.TestCaseParams;
import net.lightbody.bmp.proxy.jetty.html.Break;
import pageObjects.EdoHomePage;
import pageObjects.edo.NewUXLoginPage;
import pageObjects.edo.NewUxAssessmentsPage;
import pageObjects.edo.NewUxHomePage;
import pageObjects.edo.NewUxLearningArea2;
import pageObjects.edo.NewUxMyProfile;
import pageObjects.edo.TestEnvironmentPage;
import pageObjects.tms.DashboardPage;
import pageObjects.tms.TeacherDashboard;
import pageObjects.tms.TmsHomePage;
import services.PageHelperService;
import testCategories.inProgressTests;
import testCategories.unstableTests;
import tests.edo.newux.BasicNewUxTest;
import tests.edo.newux.SpeechRecognitionBasicTestNewUX;
import tests.misc.EdusoftBasicTest;
import tests.tms.dashboard.DashboardBasicTest;

public class NewUxTmsTeacherDashboard extends DashboardBasicTest {
	
	NewUXLoginPage loginPage;
	NewUxLearningArea2 learningArea2;
	DashboardPage dashboardPage;
	TmsHomePage tmsPages;
	TeacherDashboard teacherDashboard;
	//NewUxHomePage homePage;
	
	String url="https://teacher-dashboard-ui-prod.azurewebsites.net/Dashboards/TeacherDashboard/";
	
	// Defines the columns retrieved from SP
	String[] fields = {"ClassDimId","TermWeekNum","AverageComletion","Average_Lesson_Test_Score","AEI"
		,"Lagging_Behind","On_target","Working_Ahead","Total_Student","MidTerm","FinalTerm","Expected_Progress"};
	
	// for test environment
	NewUxAssessmentsPage testsPage;
	TestEnvironmentPage testEnvironmentPage;
	String courseCode;
	String courseId;
	String testId;
	JSONObject jsonObj = new JSONObject();
	JSONObject localizationJson = new JSONObject();
	//boolean newTE = true;
	String className;
	//userName="";
	String returnUrl="";
			
	
	@Before
	public void setup() throws Exception {
		super.setup();
	}
	@Test
	@Category (inProgressTests.class)
	@TestCaseParams(testCaseID = { "","" }, skippedBrowsers = {"firefox"})
	public void testCompare_AllWeeks_ClassProgressByDB() throws Exception {
		
		TeacherDashboard teacherDashboard = new TeacherDashboard(webDriver, testResultService);
		
		//List<String[]> teacherList = teacherDashboard.getTeacherClassesDimId();
		List<String[]> teacherList = teacherDashboard.getRandomTeacherListFromAllInstitutions();
		
		
		// For Debug get specific teacherDimId
		
		// teacherDimId= 16824 and classDimId=105853
		int index = 0;
	/*	
		// Start of Debugs	
		boolean found=false;
		for (int i=0;i<teacherList.size() && found==false; i++){
			if (teacherList.get(i)[0].equalsIgnoreCase("17390")) // for classDimId change the [0] to [1]
				found=true;
				index = i;
		}
		// end of Debugs
	*/
		
		List<String[]> expecTedteacherClassProgress = null;
		List<String[]> actualTeacherClassProgress = null;
		
	
		// loop all teachers and classes
		for (int tIndex = index; tIndex < teacherList.size(); tIndex++){
			
			report.startStep("Test Id: " + (tIndex+1)+ ", teacherDimId= " +teacherList.get(tIndex)[0]+ " and classDimId=" +teacherList.get(tIndex)[1]);
			
			// get from DBO SP the record set, -1 all weeks rows
			expecTedteacherClassProgress = teacherDashboard.getExpectedClassProgressBeforeClaculationFromDB(teacherList.get(tIndex)[0], teacherList.get(tIndex)[1], -1);
			
			// get from PWBI SP the record set, -1 latest weeks rows
			actualTeacherClassProgress = teacherDashboard.getactualClassProgressAfterClaculationFromDB(teacherList.get(tIndex)[0], teacherList.get(tIndex)[1], -1);
			
			// verify the record set not null
			if (expecTedteacherClassProgress != null || actualTeacherClassProgress != null){
		
				// check and compare all data rows and fields that retrieved from DB.
				teacherDashboard.compareRetreivedDataFromDB(expecTedteacherClassProgress,
						actualTeacherClassProgress,teacherList.get(tIndex)[0],
						teacherList.get(tIndex)[1],fields);	
			}
			else
				testResultService.addFailTest("No data retrieved from DB for teacher=" +teacherList.get(tIndex)[0] + " and ClassDimId= " + teacherList.get(tIndex)[1], false, false);	
		}
		report.report("Failures count is:" + EdusoftBasicTest.failure_count);
	}
	
	
	@Test
	@Category (inProgressTests.class)
	@TestCaseParams(testCaseID = { "","" }, skippedBrowsers = {"firefox"})
	public void testCompare_LatestWeek_ClassProgressByDB() throws Exception {
		
		TeacherDashboard teacherDashboard = new TeacherDashboard(webDriver, testResultService);
		
		//List<String[]> teacherList = teacherDashboard.getTeacherClassesDimId();
		List<String[]> teacherList = teacherDashboard.getRandomTeacherListFromAllInstitutions();
		
		// For Debug get specific teacherDimId
		//teacherDimId= 16838 and classDimId=105865
		int index = 0;
/*		
		// Start of Debugs	
		boolean found=false;
		for (int i=0;i<teacherList.size() && found==false; i++){
			if (teacherList.get(i)[0].equalsIgnoreCase("16838")) // for classDimId change the [0] to [1]
				found=true;
				index = i;
		}
		// end of Debugs
*/	
		
		List<String[]> expecTedteacherClassProgress = null;
		List<String[]> actualTeacherClassProgress = null;
		
	
		// loop all teachers and classes
		for (int tIndex = index; tIndex < teacherList.size(); tIndex++){
			
			report.startStep("Test Id: " + (tIndex+1)+ ", teacherDimId= " +teacherList.get(tIndex)[0]+ " and classDimId=" +teacherList.get(tIndex)[1]);
			
			// get from DBO SP the record set, 1 latest week row
			expecTedteacherClassProgress = teacherDashboard.getExpectedClassProgressBeforeClaculationFromDB(teacherList.get(tIndex)[0], teacherList.get(tIndex)[1], 0);
			
			// get from PWBI SP the record set, 1 latest week row
			actualTeacherClassProgress = teacherDashboard.getactualClassProgressAfterClaculationFromDB(teacherList.get(tIndex)[0], teacherList.get(tIndex)[1], 0);
			
			// verify the record set not null
			if (expecTedteacherClassProgress != null || actualTeacherClassProgress != null){
			
				// compare all data retrieved from DB
				teacherDashboard.compareRetreivedDataFromDB(expecTedteacherClassProgress
					, actualTeacherClassProgress, teacherList.get(tIndex)[0]
					, teacherList.get(tIndex)[1], fields);
			}
	
			else
				testResultService.addFailTest("No data retrieved from DB for teacher=" +teacherList.get(tIndex)[0] + " and ClassDimId= " + teacherList.get(tIndex)[1], false, false);	
		}
		report.report("Failures count is:" + EdusoftBasicTest.failure_count);
	}
	
	

	@Test
	@Category (inProgressTests.class)
	@TestCaseParams(testCaseID = { "","" }, skippedBrowsers = {"firefox"})
	public void testVerifyTeacherClassCount() throws Exception {
		
		TeacherDashboard teacherDashboard = new TeacherDashboard(webDriver, testResultService);
		
		List<String[]> teacherList = teacherDashboard.getTeachersEligableListToSeeDashboard();
		List<WebElement> weeksElements = null;
			
		String classList =null;
		String currentUrl=null;
		String weeksCount=null;
		boolean mostEquel=false;
		String teacherUserId="";
		
		boolean found=false;
		String[] testScore = new String[2];
		
		for (int teacherIndex=0 ; teacherIndex<= teacherList.size(); teacherIndex++){
			
			// get institutionId by Teacher Id
			BasicNewUxTest.institutionId = dbService.getInstitutionIdByUserId(teacherList.get(teacherIndex)[0]);
	
			// open direct URL
			currentUrl = url + teacherList.get(teacherIndex)[0];
			
/*		
			// for debug
			for (int i=0; i< teacherList.size() && found==false ;i++){
		
				if(currentUrl.contains("52340850032466")){
					found=true;
					BasicNewUxTest.institutionId = dbService.getInstitutionIdByUserId(teacherList.get(teacherIndex)[0]);
				}
				else{
					teacherIndex = teacherIndex +1;
					currentUrl = url + teacherList.get(teacherIndex)[0];
				}
			}
			// end for debug
*/
			
			// open direct link
			webDriver.openUrl(currentUrl);
			
			//String totalStudentInClass1 = teacherDashboard.getTotalStudent_TeacherDashboard();
			
			
			// wait for class / left side elements
			webDriver.waitUntilElementAppears(teacherDashboard.classList, 5);
			
			report.startStep("The tested url ID: "+(teacherIndex+1)+" is: " + currentUrl);
			
			report.report("Get Total weeks count from the UI");
			List<WebElement> totalWeeksCount = teacherDashboard.getWeeksChildCount();
		
			// get how many weeks node this teacher has, how many Terms plan.
				for (int weekIndex=0; weekIndex< totalWeeksCount.size();weekIndex++){
					totalWeeksCount.get(weekIndex).click();
					sleep(1);
					
		//			report.report("Get from UI Classes count by Week node" + (weekIndex+1));
		//			List<WebElement> elementsClasses = webDriver.getChildElementsByXpath(teacherDashboard.classList, "div");

		// comment can be remove after bug fix		
		//			report.report("Compare Class list count DB Vs.UI");		
		//			classList = teacherList.get(teacherIndex+(weekIndex))[1];
		//			testResultService.assertEquals(Integer.parseInt(classList),elementsClasses.size(), "Class list count not match");
					
		//			report.report("Get from UI Weeks square count for week node: " +(weekIndex+1) );
		//			weeksCount = totalWeeksCount.get(weekIndex).getText();
		//			weeksElements = webDriver.getChildElementsByXpath(teacherDashboard.weeksAreaSquares, "div");
					
		//			report.report("Compare Week count Squares count Vs. Total Count");
		//			testResultService.assertEquals(Integer.parseInt(weeksCount),weeksElements.size(), "Week Count not match");
					
					// check Class Widget
					String[] className =  teacherDashboard.getClassesNameFromList();
					int classesCount = className.length;
					
					// loop all class list
					for (int classIndex=0; classIndex < classesCount; classIndex++){
						
						// click on class index
						clickOnClassCheckBox(classIndex);
						
						// team need to fix the class name, here we tried to take from the top of widget but it's not recognized.
						//String currentClassNameFromTopWidget = teacherDashboard.getCurrentClassNameFromTopWidget();
						
						// get classDimId
						String classDimId = teacherDashboard.getClassDimIdByClassName(BasicNewUxTest.institutionId,className[classIndex]);
						
						// get Class Progress from DB
						List<String[]> classProgressFromDB = teacherDashboard.getExpectedClassProgressBeforeClaculationFromDB(teacherList.get(teacherIndex)[2],classDimId,0);
						// get ClassEIC
						
						// wait for loaded new teacher and classes progress
						if (teacherUserId.equalsIgnoreCase(teacherList.get(teacherIndex)[2]))
							sleep(4);
						
						String classAEIUI = teacherDashboard.getClassEIC_TeacherDashboard();

						//report.report("Get class EIC from DB and compare with UI and DB");
						String classEICFromDB = classProgressFromDB.get(0)[4];
					mostEquel = teacherDashboard.checkResultNoGapOf2(classAEIUI,classEICFromDB);
						testResultService.assertEquals(true,mostEquel
								,"Class AEI it's not correct. The db value is: " +classEICFromDB+ " and the UI value is:" +classAEIUI+ " ,Class Name: " +className[classIndex]);
						
						//report.report("Get Class Completion from UI and DB");
						String classCompletion =  teacherDashboard.getClassAverageCompletion_TeacherDashboard();
						// get Class Completion from DB and compare.
						String classCompletionFromDB = classProgressFromDB.get(0)[2];
					mostEquel = teacherDashboard.checkResultNoGapOf2(classCompletion,classCompletionFromDB);
						testResultService.assertEquals(true,mostEquel
								,"Class Completion it's not correct. The DB value is: " +classCompletionFromDB+ " and the UI value is:" +classCompletion + " ,Class Name: "+className[classIndex]);
						
						//report.report("Get Class Test Average from UI and DB");
						String classTestAverage = teacherDashboard.getClassAverageLessonScore_TeacherDashboard();
						// get Class Test Average from DB and compare
						String classTestAverageFromDB = classProgressFromDB.get(0)[3];
					mostEquel = teacherDashboard.checkResultNoGapOf2(classTestAverage,classTestAverageFromDB);
						testResultService.assertEquals(true,mostEquel
							,"Class Test Average it's not correct. The DB value is: " +classTestAverageFromDB+ " and the UI value is:" +classTestAverage+" ,Class Name: " +className[classIndex]);
						
						// get total student from UI and DB and compare
						//String totalStudentInClass = teacherDashboard.getTotalStudent_TeacherDashboard(); // need to fix
						//String totalStudentInClassFromDB = classProgressFromDB.get(0)[8]; // correct
						//testResultService.assertEquals(totalStudentInClassFromDB, totalStudentInClass,"Total student in Class not correct");
						
						//get MidTerm and Final Test score
						testScore = teacherDashboard.getMidTermAndFinalTestSCoreFromUI();
						// compare with DB
						
						teacherUserId = teacherList.get(teacherIndex)[2];	
						swithToTopMainWindow();
					}
					
					// to compare if it's the same teacher in order to add sleep when the page loaded in first time.
					
					
					// get new teacher
					teacherIndex = (teacherIndex + weekIndex);
				}		
		}
	
	}
	
	
	private void swithToTopMainWindow() {
		try {
			//webDriver.switchToMainWindow();
			webDriver.switchToTopMostFrame();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@Test
	@Category (inProgressTests.class)
	@TestCaseParams(testCaseID = { "","" }, skippedBrowsers = {"firefox"})
	public void testVerifyTeacherDashboardSanity() throws Exception {
		
		TeacherDashboard teacherDashboard = new TeacherDashboard(webDriver, testResultService);
		NewUXLoginPage loginPage = new NewUXLoginPage(webDriver, testResultService);
		TmsHomePage tmsHomePage = new TmsHomePage(webDriver, testResultService);
			
		List<String[]> teacherList = teacherDashboard.getTeacherCredentialToLoingFromDB();
		
		List<String[]> ClassAndWeeksList = teacherDashboard.getTeachersEligableListToSeeDashboard();
		List<WebElement> weeksElements = null;
		List<WebElement> currentClass=null;
		WebElement classNameElement=null;
		
		String currentUrl=null;
		String weeksCount=null;
		String password=null;
		String classListDB = null;
	//	String totalStudentInClassFromDB=null;
	//	String totalStudentInClassFromUI=null;
		int week = 0;
		
		boolean found= false;
		
		for (int i=0 ; i<= teacherList.size(); i++){
			currentUrl = teacherList.get(i)[0];
			userName = teacherList.get(i)[1];
			password = teacherList.get(i)[2];
			studentId = teacherList.get(i)[3];
			BasicNewUxTest.institutionId = teacherList.get(i)[4];
			
			// find the teacher classes and week
			for (int t=0; t <ClassAndWeeksList.size() && !found;t++){
				if (ClassAndWeeksList.get(t)[0].equalsIgnoreCase(studentId)){
					classListDB = ClassAndWeeksList.get(t)[1];
					found=true;
				}
			}
			
			
			if (!currentUrl.equalsIgnoreCase(teacherList.get(i)[0]) || i==0){
				report.startStep("Tested url is: " + currentUrl);
				webDriver.openUrl("https://" + currentUrl);
			}
				
			report.report("UserName is : "+ userName + " ,and Password is: "+ password + " ,UserId is :" + teacherList.get(i)[3]);
			homePage = loginPage.loginAsStudent(userName, password);
			//sleep(2);
			homePage.skipNotificationWindow();
			sleep(2);
			tmsHomePage.switchToMainFrame();
			teacherDashboard.switchToTeacherDashboardFrame();
			
			webDriver.waitUntilElementAppears(teacherDashboard.classList, 10);
			
			report.report("Get Total weeks count from the UI");
			List<WebElement> totalWeeksCount = teacherDashboard.getWeeksChildCount();
		/*	
			if(currentUrl.contains("52341270000014")){
				String a = "1";
				a = "aa";
			}
		*/		
				for (int w=0; w < totalWeeksCount.size();w++){
					totalWeeksCount.get(w).click();
					sleep(1);
					
					report.report("Get from UI Classes count by Week node" + (w+1));
					List<WebElement> elementsClasses = webDriver.getChildElementsByXpath(teacherDashboard.classList, "div");
					
					report.report("Compare Class list count DB Vs.UI");
					testResultService.assertEquals(Integer.parseInt(classListDB),elementsClasses.size(), "Class list count not match");
					
					
					// click on class elements
					for (int classIndex=0; classIndex < elementsClasses.size();classIndex++){
						currentClass = webDriver.getChildElementsByXpath(teacherDashboard.classElemnt,"span");
						//currentClass.get(c).click();
						clickOnClassCheckBox(classIndex);
					
						String className = currentClass.get(classIndex).getText();
						String classDimId = teacherDashboard.getClassDimIdByClassName(BasicNewUxTest.institutionId,className);
						
						//report.report("Compare total student in class");
						//teacherDashboard.compareTotalStudentInClass(classDimId,week); //need to fix
						
						report.report("Get from UI Weeks square count for week node: " +(w+1) );
						weeksCount = totalWeeksCount.get(w).getText();
						weeksElements = webDriver.getChildElementsByXpath(teacherDashboard.weeksAreaSquares, "div");
						
						report.report("Compare Week count Squares count Vs. Total Count");
						testResultService.assertEquals(Integer.parseInt(weeksCount),weeksElements.size(), "Week Count not match");
						
						//get class EIC from widget
						String classEICFropmUI = teacherDashboard.getClassEIC_TeacherDashboard();
						//String ClassEICFromDB = teacherDashboard.getclassFromDB(classDimId,week);
						
						// in case there is teacher has more than one class assgined
						i = (i + w);
					}
				}
				found=false;
				webDriver.switchToMainWindow();
				//tmsHomePage.switchToMainFrame();
				tmsHomePage.clickOnExit();
			}
		}
	
	
	
	private void clickOnClassCheckBox(int classIndex) {
		try {
		
		webDriver.switchToMainWindow();
		
		List<WebElement> classlist = webDriver.getElementsByXpath("//div[contains(@class,'bg-white shadow w-4 h-4 rounded')]");
		classlist.get(classIndex).click();
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
		
	}


	@Test
	@TestCaseParams(testCaseID = { "78729" , "78604" , "78685" , "78721" , "78859", "79554", "78975" })
	public void testCompleteMidTestAndGetResultsRoundLevel2() throws Exception {
		
		
		institutionName=institutionsName[12];
		webDriver.closeBrowser();
		super.setup();
		
		
		JSONObject localizationJson = new JSONObject();
		
		report.startStep("Retrieve Localization Json US");
		localizationJson = netService.getJsonObject("smb://"+PageHelperService.physicalPath+"/Runtime/Localization/TestInstructions/", "en_US.json", false);
		
		className = "classTboard2"; //configuration.getProperty("classname.CourseTest");
		
		//homePage = createUserAndLoginNewUXClass(className, institutionId);
		
		// /*
		// get random users
		String[] user = getUserNamePassId(institutionId, className); //(className, institutionId);
		userName = user[0];
		String password = user[1];
		studentId = user[2];
		// */
		
		/*
		// get specific user
		userName = ""; // define the wanted username
		studentId = dbService.getUserIdByUserName(userName, institutionId);
		String password = "12345";
		*/
		
		String wantedTestId = "989017755";
		//String wantedTestId = "989022790";
		
		// Initialize Test Environment Page
		testEnvironmentPage = new TestEnvironmentPage(webDriver, testResultService);
		NewUXLoginPage loginPage = new NewUXLoginPage(webDriver, testResultService);
		
		report.startStep("Init Data");
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
		
		
		homePage = loginPage.loginAsStudent(userName, password);
		homePage.waitHomePageloaded();
		
		
		report.startStep("Change JSON of Test to Round 2");
		pageHelper.runConsoleProgram("cmd /c copy "+PageHelperService.ServerPath+"CourseTests\\"+testId+"_RoundLevel2\\"+testId+".json "+PageHelperService.ServerPath+"CourseTests");
	
		report.startStep("Retrieve JSON Array of Test ID: " + testId);
		jsonObj = netService.getJsonObject("smb://"+PageHelperService.physicalPath+"/Runtime/Metadata/Courses/CourseTests/", testId + ".json", false);
		
		report.startStep("Open Assessments Page");
		testsPage = homePage.openAssessmentsPage(false);
		
		report.startStep("Start Test");
		int testSequence = testsPage.getTestSequenceByCourseId(courseId);
		String testName = testsPage.clickStartTest(1, testSequence);
		webDriver.closeAlertByAccept();
		sleep(3);
		
		report.startStep("Validate Test Name");
		testEnvironmentPage.validateTestName(testName);
		sleep(3);
		
		report.startStep("Validate Course Intro");
		testEnvironmentPage.validateIntro(jsonObj, localizationJson, testId, testName);
				
		report.startStep("Get Round Level of Course");
		String roundLevelOfCourse = testEnvironmentPage.getRoundLevelFromJson(jsonObj, testId);
		//String roundLevelOfCourse = testEnvironmentPage.getValueOfWantedKeyInSpecificTestId(jsonObj, testId, "RoundLevel");
		
		// Get Number of Sections to Submit
		int unitCount = 1;
		
		// Get list of lesson count for each unit in course
		List<String[]> lessonCountForUnits = dbService.getLessonCountForAllUnitsInCourse(testId);
		
		// Get unit ids list from previous list
		List<String> unitIds = new ArrayList<String>();
		for (int j = 0; j < lessonCountForUnits.size(); j++) {
			unitIds.add(lessonCountForUnits.get(j)[0]);
		}
		
		unitCount = lessonCountForUnits.size();
		// Change unit count below to as many units you want to answer (In this case all units are being answered)
		int unitsToAnswer = 2; //unitCount
		int sectionsToAnswer = 0;
		for (int k = 0; k < unitsToAnswer; k++) {
			sectionsToAnswer += Integer.parseInt(lessonCountForUnits.get(k)[1]);
		}
				
		report.startStep("Answer Questions and Validate Intros");
		testEnvironmentPage.answerQuestionsNew(answerFilePath, testId, wantedCourseCode, courseTestType, sectionsToAnswer, roundLevelOfCourse, unitIds, jsonObj, localizationJson, testName, unitsToAnswer);
				
		report.startStep("Validate Score in DB");
		List<String[]> userTestProgress = dbService.getUserTestProgressByUserId(studentId);
		String scoreForDisplayDB = userTestProgress.get(0)[4];
//		testResultService.assertEquals("100", scoreForDisplayDB, "Score in DB is Incorrect");
		
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
		
	}
	
	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}
	
}
	
	

