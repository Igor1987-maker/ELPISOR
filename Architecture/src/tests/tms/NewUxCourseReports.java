package tests.tms;


import Enums.ByTypes;
import Enums.CourseCodes;
import Enums.CourseTests;
import Enums.TaskTypes;
import Interfaces.TestCaseParams;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import pageObjects.edo.*;
import pageObjects.tms.*;
import services.PageHelperService;
import testCategories.AngularLearningArea;
import testCategories.edoNewUX.SanityTests;
import testCategories.inProgressTests;
import tests.edo.newux.AssessmentsTests;
import tests.edo.newux.BasicNewUxTest;
import tests.edo.newux.SpeechRecognitionBasicTestNewUX;

import java.util.ArrayList;
import java.util.List;

public class NewUxCourseReports extends SpeechRecognitionBasicTestNewUX {

	NewUXLoginPage loginPage;
	DashboardPage dashboardPage;
	TmsHomePage tmsHomePage;
	NewUxLearningArea learningArea;
	NewUxLearningArea2 learningArea2;
	NewUxAssessmentsPage testsPage;
	NewUxInstitutionPage ipage;
	NewUxMyProfile myProfile;
	NewUxCommunityPage communityPage;
	NewUxClassicTestPage classicTest;
	TmsCourseTestReport tmsCTR;
	NewUxMyProgressPage myProgress;
	NewUxDragAndDropSection dragAndDrop;
	NewUxDragAndDropSection2 dragAndDrop2;
	NewUxMessagesPage inboxPage;
	AssessmentsTests assessmentTest;
	NewUxMessagesPage messagesPage; 
	
	//String instID = "";
	Boolean matrixDBCleanup=false;
	Boolean matrixAdminCleanup = false;
	Boolean deleteUser = false;
	String classId= "";
	
	int nextSectionCounter = 0;
	int nextPageCounter = 0;
	List<WebElement> matrixPages;
	List<WebElement> matrixStudents;
	
	@Before
	public void setup() throws Exception {
		super.setup();
		
	}
	
	
	@Test
	@TestCaseParams(testCaseID = { "84903" })
	public void testTeacherSendCertificateToStudentToMyProfile() throws Exception {
	
	report.startStep("Init test data and login to TMS as teacher");
		String studentName = "";
		String password = "";
		String className = "";
		String courseLevel = "Basic 1";
		String reportType = "End of Course Matrix";
		loginPage = new NewUXLoginPage(webDriver, testResultService);
		String[]student;
		for(int i = 0;i < 2;i++) {
			
			if(i==0) {
				report.startStep("First iteration on old TE");
				student = pageHelper.getMatrixStudentsWithProgress(institutionId);
				studentName = student[1];
				password = student[2];
				studentId = student[0];
				className = student[3];
				//String[]student = pageHelper.getStudentWithProgressFinalGrade(pageHelper.institutionMatrixId_oldTE);
				//studentName = student[0];
				//password = student[1];
				//studentId = student[2];
				//className = pageHelper.getClassNameOfParticularUser(studentId); 
				
				institutionName = BasicNewUxTest.institutionsName[0];
			//	pageHelper.openCILatestUXLink();
				tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
				tmsHomePage.waitForPageToLoad();
				}
			if(i==1) {
				report.startStep("Second iteration on new TE");
				institutionName = BasicNewUxTest.institutionsName[1];
				pageHelper.openCILatestUXLink();
				//loginPage.loginAsStudent("admin2016","12345");
				tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();

				tmsHomePage = new TmsHomePage(webDriver, testResultService);
				tmsHomePage.waitForPageToLoad();
				student = pageHelper.getStudentWithProgressFinalGrade(pageHelper.institutionMatrixId_newTE);
				studentName = student[0];
				password = student[1]; 
				studentId = student[2];
				className = pageHelper.getClassNameOfParticularUser(studentId);
				
			//	student = pageHelper.getMatrixStudentsWithProgress(institutionId);
			//	studentName = student[1];
			//	password = student[2];
			//	studentId = student[0];
			//	className = student[3];
			}
			
		sleep(3);
		tmsHomePage.switchToMainFrame();
		
	report.startStep("Open Matrix report of students progress");
		tmsHomePage.clickOnReports();
		sleep(1);
		tmsHomePage.clickOnCourseReports();
		sleep(1);
		//tmsHomePage.switchToFormFrame();
		//sleep(1);
		//tmsHomePage.selectReport("9"); // 9 it's end of course Matrix
		tmsHomePage.selectCourseReport(reportType);
		sleep(1);
		tmsHomePage.selectClass(className, false, true);
		sleep(1);
		//tmsHomePage.clickOnGo();
		webDriver.switchToPopup();
		
		TmsMatrixPage matrixPage = new TmsMatrixPage(webDriver, testResultService);
		
	report.startStep("Send certificate to student");
		matrixPage.choseCourseLevel(courseLevel);
		matrixPage.findStudentAndSentHimCertificate(studentName);
		webDriver.getWebDriver().close();
		webDriver.switchToMainWindow();
		tmsHomePage.clickOnExitTMS();
		loginPage.waitForPageToLoad();
	
	report.startStep("Login as student");
		pageHelper.setUserLoginToNull(studentId);
		sleep(1);
		webDriver.switchToMainWindow();
		homePage = loginPage.loginAsStudent(studentName,password);
	//	pageHelper.skipOptin();
		homePage.skipNotificationWindow();
		homePage.waitHomePageloadedFully();
		//pageHelper.skipOnBoardingHP();
		//homePage.closeModalPopUp();

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

	
	
	
	
	
	
	//Resume TMS side

	@Test
	@TestCaseParams(testCaseID = { "51239","51238", "50964", "51166", "51176", "51181" })
	public void testMidTestResumeModeOldTE() throws Exception {
		
	TestEnvironmentPage testEnvironmentPage = new TestEnvironmentPage(webDriver,testResultService);
		
	report.startStep("Get default auto student details");
		String userName = configuration.getStudentUserName();
		String password = configuration.getStudentPassword();
		//String institutionId = configuration.getInstitutionId();
		String className = configuration.getProperty("classname.progress");
		String courseCode = "B1";
		studentId = dbService.getUserIdByUserName(userName, institutionId);
		pageHelper.setUserLoginToNull(studentId);
		String impType = "2";
		String resultScore = "";
		String reportType = "Course Tests Report";
		String wrongAnswerFilePath = "files/CourseTestData/MidTermResumeWrongAnswers.csv";
		
		CourseTests testType = CourseTests.MidTerm;
		String answersFilePath = "files/CourseTestData/MidTerm_Answers.csv";
		CourseCodes wantedCourseCode = testEnvironmentPage.getCourseCodeEnumByCourseCodeString(courseCode); 
					
	report.startStep("Login as student");
		NewUXLoginPage loginPage = new NewUXLoginPage(webDriver,testResultService);
		homePage = loginPage.loginAsStudent(userName, password);
		
	report.startStep("Assign B1 Mid-Term Test to student");
		String courseId = getCourseIdByCourseCode(courseCode);
		String courseName = getCourseNameByCourseCode(courseCode);
		pageHelper.assignAutomatedTestToStudetInTestEnvironment(studentId, courseId, Integer.parseInt(impType),0,0,1);
		sleep(2);
		
		String testId = pageHelper.getAssignedTestIdForStudent(studentId,courseId, Integer.parseInt(impType));
		String[] oldTestIds = {"989013148","989012509"};
		int randomNum = dbService.getRandonNumber(0, oldTestIds.length-1);
		randomNum=1;
		
		String userExitSettingsId = dbService.getUserExitSettingsId(studentId, testId, courseId, impType);
		if (testId.equals("989017755") || testId.equals("989013148") || testId.equals("989022835")) {
			
			dbService.updateTestIdInUserExitSettings(oldTestIds[randomNum], userExitSettingsId);
			testId = pageHelper.getAssignedTestIdForStudent(studentId,courseId, Integer.parseInt(impType));
			String token = pageHelper.getTokenFromConsoleWithParameters("1", studentId, testId, userExitSettingsId);
			dbService.updateAssignWithToken(token, userExitSettingsId);
		}
				
	report.startStep("Start test and submit first section with correct answers");
		homePage.closeModalPopUp();
		homePage.waitHomePageloaded();
		testsPage = homePage.openAssessmentsPage(false);
		int testSequence = testsPage.getTestSequenceByCourseId(courseId);
		String testName = testsPage.clickStartTest(1, testSequence);
		sleep(1);
		webDriver.closeAlertByAccept();
		sleep(3);
		webDriver.switchToNewWindow();
		NewUxClassicTestPage classicTest = new NewUxClassicTestPage(webDriver, testResultService);
		classicTest.switchToTestAreaIframe();
		sleep(1);
		classicTest.pressOnStartTest();
		sleep(3);
		
		testEnvironmentPage.answerQuestionsBySectionsOldTe(answersFilePath, testId +"#", wantedCourseCode, testType, 1);
		
	report.startStep("Close Test and Logout");
		classicTest.close();
		homePage.clickOnLogOut();
		testResultService.assertEquals(false, loginPage.isSubmitButtonEnabled(), "Checking that ED Login Page displayed");
		
	report.startStep("Login as teacher and select course test report");
		String tName = configuration.getProperty("teacher.username");
		String tID = dbService.getUserIdByUserName(tName, institutionId);
		pageHelper.setUserLoginToNull(tID);
		loginPage.loginAsStudent(tName, "12345");
		
		tmsHomePage = new TmsHomePage(webDriver, testResultService);
		tmsHomePage.waitForPageToLoad();
		
		TmsReportsPage tmsReportsPage = new TmsReportsPage(webDriver, testResultService);
		
	report.startStep("Select Course Test Report");
		tmsReportsPage.goToCourseReports();
		
		tmsReportsPage.selectCourseReportAndClassAndCourse(reportType, className, courseId);
		
	
		String tmsScore = tmsHomePage.MidTermTestScoreResumeMode(userName);
		testResultService.assertEquals(resultScore, tmsScore, "Final Score in TMS does not match to test result in Assessments page");
		sleep(3);
		
	report.startStep("Validate Did Test is 1, And CTcompleted is 0 in DB");
		String[] studentTestDate = dbService.getStudentFinalTestData(studentId,courseId,testId,Integer.valueOf(impType));
		String CTcompleted = studentTestDate[2];
		testResultService.assertEquals("0", CTcompleted, "Test Marked as Completed in DB");
		String didTest = dbService.getDidTestByUserExitTestSettingsId(userExitSettingsId);
		testResultService.assertEquals("1", didTest, "Did test is not 1 even though test was started");
		
	report.startStep("Log Out");
		webDriver.switchToMainWindow();
		tmsHomePage.switchToMainFrame();
		tmsHomePage.clickOnExit();
	
	report.startStep("Login as student");
		homePage = loginPage.loginAsStudent(userName, password);
		homePage.waitHomePageloaded();
	
	report.startStep("Open Assessments and Resume test");
		testsPage = homePage.openAssessmentsPage(false); 
		testSequence = testsPage.getTestSequenceByCourseId(courseId);
		testsPage.clickStartTest(1, testSequence);
		//classicTest = testsPage.clickOnStartTest("1", "2");
		sleep(1);
		webDriver.switchToNewWindow();
		classicTest.switchToTestAreaIframe();
			
	report.startStep("Answer Incorrectly Section 2");
		testEnvironmentPage.answerSpecificSectionQuestionsOldTe(answersFilePath, testId +"#", wantedCourseCode, testType, 1);

	report.startStep("Do not Answer Sections 3,4 (skip)");
		int sectionsToSubmit = 2; // dont answer section 3,4
		for (int i = 0; i < sectionsToSubmit ; i++) {
			classicTest.browseToLastSectionTask();
			classicTest.pressOnSubmitSection(true);
		}
		
	report.startStep("Answer Incorrectly Section 5");
		testEnvironmentPage.answerSpecificSectionQuestionsOldTe(answersFilePath, testId +"#", wantedCourseCode, testType, 4);
		
	report.startStep("Get score and close test");
		classicTest.switchToCompletionMessageFrame();
		sleep(1);
		String finalScore = classicTest.getFinalScore();
		webDriver.switchToTopMostFrame();
		classicTest.switchToTestAreaIframe();
		classicTest.closeCompletionMessageAlert();
		sleep(1);
		
	report.startStep("Validate Did Test is 1, And CTcompleted is 0 in DB");
		studentTestDate = dbService.getStudentFinalTestData(studentId,courseId,testId,Integer.valueOf(impType));
		CTcompleted = studentTestDate[2];
		testResultService.assertEquals("1", CTcompleted, "Test Not Marked as Completed in DB");
		didTest = dbService.getDidTestByUserExitTestSettingsId(userExitSettingsId);
		testResultService.assertEquals("1", didTest, "Did test is not 1 even though test was started");
		
	report.startStep("Open Assessments");
		homePage.openAssessmentsPage(false);
			//			
	report.startStep("Open Tests Results section");
		testsPage.clickOnArrowToOpenSection("3");
		
	report.startStep("Verify B1 Mid-Term Test displayed in Tests Results section");
		testsPage.checkTestDisplayedInSectionByTestName(testName, "3", "1");
							
	report.startStep("Get Date of Submission");
		String testDate = testsPage.checkSubmissionDateForTests("1");
		
	report.startStep("Check Score in Test Results");
		resultScore = testsPage.checkScoreByTest("1", finalScore);
		testsPage.close();
		sleep(1);
		
	report.startStep("Verify My Progress");
		verifyMidTermTestWidgetDisplayed(finalScore, courseName);
		
	report.startStep("Logout as student");
		homePage.clickOnLogOut();
		testResultService.assertEquals(false, loginPage.isSubmitButtonEnabled(), "Checking that ED Login Page displayed");
		
	report.startStep("Login as teacher and select course test report");
		tName = configuration.getProperty("teacher.username");
		tID = dbService.getUserIdByUserName(tName, institutionId);
		pageHelper.setUserLoginToNull(tID);
		loginPage.loginAsStudent(tName, "12345");
		sleep(3);
		
	report.startStep("Select Course Test Report");
		tmsReportsPage.goToCourseReports();
		tmsReportsPage.selectCourseReportAndClassAndCourse(reportType, className, courseId);
		
	report.startStep("Validate Score and Click it");	
	    tmsScore = tmsHomePage.pressOnMidTermTestScore(userName);
		testResultService.assertEquals(resultScore, tmsScore, "Final Score in TMS does not match to test result in Assessments page");
		sleep(3);
		
	report.startStep("Switch to Course Report Page and check section titles");
		webDriver.switchToNewWindow();
		
	report.startStep("Validate Results");
		tmsCTR = new TmsCourseTestReport(webDriver, testResultService);
		tmsCTR.validateResultsOldTe(studentId, testId, userName, wrongAnswerFilePath);
	
	report.startStep("Quit Browser");
		webDriver.quitBrowser();	
	}


		
	/*@Test
	//@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "34426","35816","40220" })
	public void testCourseTestReportOldTE() throws Exception {
		
		report.startStep("Init Data");
		int impType = 2;
		testsPage = new NewUxAssessmentsPage(webDriver, testResultService);
		String userName = configuration.getStudentUserName();
		String institutionId = configuration.getInstitutionId();
		String userId = dbService.getUserIdByUserName(userName, institutionId);
		String className = configuration.getProperty("classname.progress");
		String courseCode = courseCodes[1]; 
		String courseId = getCourseIdByCourseCode(courseCode);
		studentId = dbService.getUserIdByUserName(userName, institutionId);
		String testId = pageHelper.getDidTestIdForStudent(studentId,courseId, impType);
		loginPage = new NewUXLoginPage(webDriver,testResultService);
		String testDate = getStudentTestDate(courseId, testId);
		String reportType = "Course Tests Report";
		String[] studentTestDate = dbService.getStudentFinalTestData(studentId,courseId,testId,impType);
		String[] grade = studentTestDate[1].split("\\.");
		String completed = studentTestDate[2];
		//String LastVisitedItemId = studentTestDate[3];
		classicTest = testsPage.getClassicObject();
		String wrongAnswerFilePath = "files/CourseTestData/MidTermWrongAnswers.csv";
	
		report.startStep("Login as teacher");
		String tName = configuration.getProperty("teacher.username");
		String tID = dbService.getUserIdByUserName(tName, institutionId);
		pageHelper.setUserLoginToNull(tID);
		loginPage.loginAsStudent(tName, "12345");
		sleep(3);
		
		TmsReportsPage tmsReportsPage = new TmsReportsPage(webDriver, testResultService);
		
		report.startStep("Select Course Test Report");
		tmsReportsPage.goToCourseReports();
		
		tmsHomePage = new TmsHomePage(webDriver, testResultService);
		
		tmsReportsPage.selectCourseReportAndClassAndCourse(reportType, className, courseId);
		
		report.startStep("Press on Mid-term course score");
		String tmsScore = tmsHomePage.pressOnMidTermTestScore(userName);
		
		testResultService.assertEquals("1", completed, "Test not marks as Completed in DB");
		testResultService.assertEquals(grade[0], tmsScore, "Final Score in TMS does not match to test result in Assessments page");
		sleep(3);
		 
		try {	
			
			report.startStep("Validate Results");
			tmsCTR = new TmsCourseTestReport(webDriver, testResultService);

			tmsCTR.validateResults(userId, testId, userName, testDate, wrongAnswerFilePath);
			
			/*report.startStep("Switch to Course Report Page and check section titles");
			webDriver.switchToNewWindow();
			sleep(2);
			tmsCTR = new TmsCourseTestReport(webDriver, testResultService);
			//tmsCTR.verifySectionsTitleMidTerm(userName, testDate, true, true, true);
			int numOfSections = tmsCTR.validateSectionsTitles(userId, testId, userName, testDate);
			sleep(3);
			
			// get answers from file
			TestEnvironmentPage testEnvironmentPage = new TestEnvironmentPage(webDriver, testResultService);
			String answerFilePath = "files/CourseTestData/MidTerm_Answers.csv";
			CourseTest courseTest = testEnvironmentPage.initCourseTestFromCSVFileNew(answerFilePath, testId, CourseCodes.B1, CourseTests.MidTerm, numOfSections);
			CourseTestSection round = new CourseTestSection();
			
			for (int i = 1; i <= numOfSections; i++) {
				report.startStep("Check Test Results View in Section " + i);
				tmsCTR.switchToTestResultsFrameBySection(i);
				int taskCount = classicTest.getSectionTaskCountClassic();
				
				round = courseTest.getSections().get(i-1);
				boolean isCorrect = false;
				
				for (int j = 0; j < taskCount; j++) {
					
					TestQuestion question = round.getSectionQuestions().get(j);
					
					TestQuestionType questionType = question.getQuestionType();
								
					String[] answers = new String[question.getCorrectAnswers().length];
					
					for (int k = 0; k < question.getCorrectAnswers().length; k++) {
						answers[k] = question.getCorrectAnswers()[k].replace("~", ",");
					}
					question.setCorrectAnswers(answers);
					
					String wrongAnswer = tmsCTR.getWrongAnswerFromFile(i, String.join(",", answers)); 
					if (wrongAnswer.equals("~")) {
						isCorrect = true;
						wrongAnswer = "";
					}
					
					tmsCTR.checkAnswerOfTask(j, isCorrect, answers, questionType,wrongAnswer);
					//tmsCTR.clickAndVerifyResultViewPerQuestionMCQ(j, true, true, true);
				}
				
				if (i < numOfSections) {
					webDriver.switchToTopMostFrame();
					tmsCTR.clickOnOpenSectionIconBySection(i+1);
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			super.tearDown();
			webDriver.quitBrowser();
		}	
	}*/

	private String getStudentTestDate(String courseId, String testId) throws Exception {

		String testDate = ReturnTestDate(studentId, courseId, testId, 2);
		String[] arr_testDate = testDate.split("-");
		
		testDate =  arr_testDate[2] + "/" + arr_testDate[1] + "/" + arr_testDate[0];
		return testDate;
	}



	@Test
	@Category(AngularLearningArea.class)
	@TestCaseParams(testCaseID = { "40281" })
	public void testPreviewStudentProgressReport() throws Exception {
		
		report.startStep("Init Test variables");
			String UserID = dbService.getUserIdByUserName("stForAssessment", configuration.getProperty("institution.id"));
			String UserFullName = dbService.getUserFirstNameByUserId(UserID) + " " + dbService.getUserLastNameByUserId(UserID);
			String[] places ={"prFITB__DDLOptionsW_Q1_L1","prFITB__DDLOptionsW_Q1_L2","prFITB__DDLOptionsW_Q1_L4"};
			String[] answers ={"DDLOptions__selected_aid_91","DDLOptions__selected_aid_73"};
			String courseCode = "B1";
			String CourseId = courses[homePage.getCourseIndexByCode(courseCodes, courseCode)];
			String unitId = dbService.getUnitIdByNameAndCourse("Health And Fitness",CourseId);
			String classId = dbService.getUserClassId(UserID);
			String className = dbService.getClassNameByClassId(classId);
			String[] matchingWords = new String[] { "344-555-9089", "7743 N. Broadway", "JoltGym.com", "info@joltgym.com"};
			String[] classificationWords = new String[] { "swimming pool", "gym", "sports field", "dancing", "lifting weights", "swimming","park","running" };
			String[] classificationWords1 = new String[] { "dancing", "lifting weights", "swimming","running" };
			String[] classificationWords2 = new String[] { "swimming pool", "gym", "sports field", "park" };
			String[] MTTPWords = new String[] { "pollution", "solar panels", "coal", "wind turbine" };
			String[] multiAnswerCorrectResult = new String[] {"Run around the park three times!","Visit our new gym today!"};
			tmsCTR = new TmsCourseTestReport(webDriver, testResultService);
			learningArea2 = new NewUxLearningArea2(webDriver, testResultService);
			
		report.startStep("Login as Admin");
			loginPage = new NewUXLoginPage(webDriver, testResultService);
			//instID = configuration.getProperty("institution.id");
			pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(configuration.getProperty("schoolAdmin.user"),institutionId));
			tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
			sleep(2);
			
		report.startStep("Navigate to Student Progress report");
			tmsHomePage.switchToMainFrame();
			tmsHomePage.clickOnReports();
			sleep(1);
			tmsHomePage.clickOnCourseReports();
			sleep(3);
			tmsHomePage.switchToFormFrame();
			tmsHomePage.selectReport("1");
			//webDriver.selectElementFromComboBox("SelectClass", className);
			webDriver.selectValueFromComboBox("SelectClass", className);
			//webDriver.selectElementFromComboBox("SelectUser", UserFullName);
			webDriver.selectValueFromComboBox("SelectUser", UserFullName);
			tmsHomePage.clickOnGo();
			
		report.startStep("Open first test, Course: " + courseCode + "Unit 9: UnitId:" + unitId);
			openNextTestAndEnterReview("Health And Fitness", "Introduction: Health and Fitness",  unitId,  CourseId, true);

		report.startStep("Examine classification practice");
		report.startStep("Course: " + courseCode + "Unit 9: UnitId:" + unitId + ". Item: 3");
			learningArea2.clickOnNextButton(2);
			learningArea2.clickOnYourAnswerTab();
			sleep(2);
			dragAndDrop2 = new NewUxDragAndDropSection2(webDriver, testResultService);
			dragAndDrop2.checkDragAndDropAnswerMark("dancing", 1, true, TaskTypes.Classification);
			dragAndDrop2.checkDragAndDropAnswerMark("sports field", 1, false, TaskTypes.Classification);
			learningArea2.clickOnCorrectAnswerTab();
			sleep(2);
			
			for (int i = 0; i < classificationWords1.length; i++) {
				dragAndDrop2.checkTileIsPlaced(classificationWords1[i], TaskTypes.Classification,0);
			}
			
			for (int i = 0; i < classificationWords2.length; i++) {
				dragAndDrop2.checkTileIsPlaced(classificationWords2[i], TaskTypes.Classification,1);
			}
			
			learningArea2.clickOnNextButton();
		
		report.startStep("Examine MCQ multi-answer practice");
			learningArea2.clickOnYourAnswerTab();
			learningArea2.CorrectnessVMCQ();
			learningArea2.clickOnCorrectAnswerTab();
			List<WebElement> correctAnswers  = webDriver.getElementsByXpath("//div[contains(@class,'multiRadio correct')]/label");
			
			if (!((multiAnswerCorrectResult[0].equals(correctAnswers.get(0).getText())) || (multiAnswerCorrectResult[0].equals(correctAnswers.get(1).getText())))) 
				testResultService.addFailTest("The Answer " + multiAnswerCorrectResult[0] + " doesn't appear on correct answers selection", false, true);
			else
				report.report("The Answer " + multiAnswerCorrectResult[0] + " was selected");
			
			if (!((multiAnswerCorrectResult[1].equals(correctAnswers.get(0).getText())) || (multiAnswerCorrectResult[1].equals(correctAnswers.get(1).getText()))))
				testResultService.addFailTest("The Answer " + multiAnswerCorrectResult[1] + " doesn't appear on correct answers selection", false, true);
			else
				report.report("The Answer " + multiAnswerCorrectResult[1] + " was selected");
			webDriver.closeNewTab(0);
			
		report.startStep("Open second test, Unit: Health And Fitness, Lesson: Jolt Gym");
			openNextTestAndEnterReview("Health And Fitness", "Jolt Gym", unitId, CourseId, false);
			
		report.startStep("Examine Matching practice Item 3");
			learningArea2.clickOnNextButton(2);
			learningArea2.clickOnYourAnswerTab();
			dragAndDrop2 = new NewUxDragAndDropSection2(webDriver, testResultService);
			dragAndDrop2.checkDragAndDropAnswerMark("7743 N. Broadway", 2, true, TaskTypes.Matching);
			dragAndDrop2.checkDragAndDropAnswerMark("info@joltgym.com", 4, true, TaskTypes.Matching);	
			learningArea2.clickOnCorrectAnswerTab();
			
			//for (int i = 0; i < matchingWords.length; i++) {
				dragAndDrop2.checkTileIsPlaced(matchingWords, TaskTypes.Matching);
			//}
		
			
		report.startStep("Examine Fill In The Blanks practice");
			learningArea2.clickOnNextButton(2);
			learningArea2.clickOnYourAnswerTab();
			learningArea2.verifyAnswerFillTheBlankSelectedCorrect2(places[0]);
			learningArea2.verifyAnswerFillTheBlankSelectedCorrect2(places[1]);
			learningArea2.verifyAnswerFillTheBlankSelectedWrong2(places[2]);
			learningArea2.clickOnCorrectAnswerTab();
			learningArea2.checkAnswersSelectedFillTheBlanks2 (answers[0]);
			learningArea2.checkAnswersSelectedFillTheBlanks2 (answers[0]);
			
			webDriver.closeNewTab(0);
			
		report.startStep("Open third test");
			openNextTestAndEnterReview("Health And Fitness", "Looking for Sports Equipment ", unitId, CourseId, false);

		report.startStep("Examine sequence sentence practice");
			learningArea2.clickOnNextButton();
			learningArea2.clickOnYourAnswerTab();
			dragAndDrop = new NewUxDragAndDropSection(webDriver, testResultService);
			dragAndDrop.checkDragAndDropAnswerForNewSeqText("1", true);
			dragAndDrop.checkDragAndDropAnswerForNewSeqText("28", true);
			
		
			
		report.startStep("Examine MCQ + Sound practice");
			learningArea2.clickOnNextButton(2);
			learningArea2.clickOnYourAnswerTab();
			learningArea2.sound.click();
			learningArea2.checkAudioFile("AngularMediaPlayer", "b1soabt104.mp3");
			learningArea2.CorrectnessVMCQ();
			learningArea2.clickOnCorrectAnswerTab();
			testResultService.assertEquals(webDriver.waitForElement(("//div[contains(@class,'multiRadio correct')]/label"),ByTypes.xpath).getText(),"What kind of machine do you want?");
			webDriver.closeNewTab(0);	
			courseCode = "B3";
			CourseId = courses[homePage.getCourseIndexByCode(courseCodes, courseCode)];
			unitId = dbService.getUnitIdByNameAndCourse("Saving Energy",CourseId);
			
		report.startStep("Open fourth test");
			openNextTestAndEnterReview("Saving Energy", "Renewable Energy", unitId, CourseId, true);

		report.startStep("Examine MTTP practice");
			learningArea2.clickOnNextButton(2);
			learningArea2.clickOnYourAnswerTab();
			dragAndDrop2.checkDragAndDropAnswerMarkMTTP("solar panels", 2, true, TaskTypes.MTTP);
			dragAndDrop2.checkDragAndDropAnswerMarkMTTP("wind turbines", 1, false, TaskTypes.MTTP);
			learningArea2.clickOnCorrectAnswerTab();
			for (int i = 0; i < MTTPWords.length; i++) {
				dragAndDrop2.checkTileIsPlacedMTTP(MTTPWords[i], TaskTypes.MTTP);
			}
			webDriver.closeNewTab(0);		
	}

	@Test
	@Category(AngularLearningArea.class)
	@TestCaseParams(testCaseID = {"14571"})
	public void testLockMatrixProgress() throws Exception {
		
		report.startStep("Init test data");
		
			List<WebElement> scores;
		//	String tempStudent;
			String actualstudent;
			String scoreId;
		//	boolean foundFlag = false;
		//	boolean pressedNextOnce = false;
		//	instID = configuration.getProperty("institution.id");
		//	int nextSectionCounter = 0;
		//	int nextPageCounter = 0;
		
		report.startStep("Login to ED as new user");
			loginPage = new NewUXLoginPage(webDriver, testResultService);
			createUserAndLoginNewUXClass();
			/*
			String userName =   "100LN" + dbService.sig(7);
			pageHelper.createUSerUsingSP(instID,userName,userName,userName,"12345","a@a.com",configuration.getProperty("classname"));
			setOnBoardingToVisited(userId);
			dbService.setUserOptIn(userId, true);
			loginPage.loginAsStudent(userName, "12345");
			*/
			deleteUser=true; //delete that user in the end of test
			
		report.startStep("Update the UserName in order to make easy to serach in Matrix report");
			updateUserName(); //to make it easy to find in Matrix
			
		
			actualstudent =dbService.getUserNameById(studentId, institutionId);
			
		report.startStep("Navigate to test");
			learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "B1", 1, 1);
			sleep(1);
			learningArea2.clickOnStep(3, false);
			
		report.startStep("Submit test and logout");
			learningArea2.clickOnStartTest();
			learningArea2.submitTest(true);
			sleep(3);
			learningArea2.clickOnLogoutLearningArea();
			sleep(3);
		
		report.startStep("Login to TMS as admin");
			pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(configuration.getProperty("schoolAdmin.user"),institutionId));
			tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
			sleep(3);
			
		report.startStep("Navigate to Matrix report");
			tmsHomePage.switchToMainFrame();
			tmsHomePage.clickOnReports();
			sleep(2);
			tmsHomePage.clickOnCourseReports();
			sleep(2);
			tmsHomePage.switchToFormFrame();
			tmsHomePage.selectReport("9");
			tmsHomePage.selectClass(configuration.getProperty("classname"), false, true);
			sleep(1);
		
		report.startStep("Select relevant course in Matrix");
			webDriver.switchToNewWindow();
			//webDriver.selectElementFromComboBox("DDLCources", "Basic 1");
			webDriver.selectValueFromComboBox("DDLCources", "Basic 1");
			waitForMatrixLoadingToEnd(3,15,5);
			webDriver.printScreen("Matrix load end");
		
		report.startStep("Find and lock student");
			findUserandLock(actualstudent);
			
			
			webDriver.waitForElement("//input[@value='" + studentId + "']", ByTypes.xpath).click();
			webDriver.waitForElement("btnLock", ByTypes.id).click();
			sleep(2);
			
		report.startStep("Logout of TMS");
			webDriver.closeNewTab(0);
			webDriver.switchToMainWindow();
			tmsHomePage.switchToMainFrame();
			tmsHomePage.clickOnExit();
			
		report.startStep("Login to ED as same student");
			loginPage.loginAsStudent(actualstudent, "12345");
			sleep(5);
		
		report.startStep("Start test again");
			learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "B1", 1, 1);
			sleep(3);
			learningArea2.clickOnStep(3, false);
			learningArea2.clickOnStartTest();
			
		report.startStep("Retake test and achieve none-zero score");
			dragAndDrop2 = new NewUxDragAndDropSection2(webDriver, testResultService);
			dragAndDrop2.dragAndDropAnswerByTextToTarget("New York", 1,TaskTypes.Close);
			learningArea2.clickOnNextButton();
			sleep(2);
			dragAndDrop2.dragAndDropAnswerByTextToTarget("reporter", 1,TaskTypes.Close);
			learningArea2.clickOnNextButton();
			sleep(2);
			learningArea2.SelectRadioBtn("question-1_answer-1");
			learningArea2.clickOnNextButton();
			sleep(2);
			dragAndDrop2.dragAndDropAnswerByTextToTarget("rock music", 1,TaskTypes.Close);
			learningArea2.clickOnNextButton();
			sleep(2);
			learningArea2.SelectRadioBtn("question-1_answer-3");
			learningArea2.submitTest(false);
			sleep(3);
		
		report.startStep("Logout and Login to TMS");
			learningArea2.clickOnLogoutLearningArea();
			sleep(5);
			pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(configuration.getProperty("schoolAdmin.user"),institutionId));
			tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
			sleep(3);
			
		report.startStep("Navigate back to Matrix report");
			tmsHomePage.switchToMainFrame();
			tmsHomePage.clickOnReports();
			sleep(2);
			tmsHomePage.clickOnCourseReports();
			sleep(2);
			tmsHomePage.switchToFormFrame();
			tmsHomePage.selectReport("9");
			tmsHomePage.selectClass(configuration.getProperty("classname"), false, true);
			sleep(3);
		
		report.startStep("Select relevant course in Matrix");
			webDriver.switchToNewWindow();
			webDriver.selectElementFromComboBox("DDLCources", "Basic 1");
			waitForMatrixLoadingToEnd(3,15,5);
			//webDriver.printScreen("Matrix load end");
		
		report.startStep("Find locked student");
			findLockedUser(); 
		
			
		report.startStep("Examine grade value of student");
			scores = webDriver.getElementsByXpath("//div[contains(@class,'scoreDis')]");
			testResultService.assertEquals("0",scores.get(1).getText());
			scoreId = scores.get(1).getAttribute("id");
		
		report.startStep("Unlock and Examine grade value of student");
			webDriver.waitForElement("//input[@value='" + studentId + "']", ByTypes.xpath).click();
			webDriver.waitForElement("btnUnLock", ByTypes.id).click();
			waitForMatrixLoadingToEnd(5,15,5);
			testResultService.assertEquals("2",webDriver.waitForElement("//*[@id='" + scoreId + "']",ByTypes.xpath).getText());
		
	}

	private void updateUserName() throws Exception {
		NewUxMyProfile myProfile = new NewUxMyProfile(webDriver,
				testResultService);
		
			String newLastName =   "100LN" + dbService.sig(7);
			homePage.clickOnMyProfile();
			homePage.switchToMyProfile();
			myProfile.changeLastName(newLastName);
			myProfile.changeUserName(newLastName);
			myProfile.clickOnUpdate();
			myProfile.close(true);
	}



	private void findLockedUser() throws Exception {
		boolean pressedNextOnce = false;
		 for (int i = 0 ; i < nextSectionCounter ; i++) {
			 if (pressedNextOnce)
					webDriver.waitForElement("//*[@id='DataPager1']/a[6]", ByTypes.xpath).click();
				else {
					webDriver.waitForElement("//*[@id='DataPager1']/a[5]", ByTypes.xpath).click();
					pressedNextOnce = true;
				}
				sleep(2);
		 }
		 for (int i = 0 ; i < nextPageCounter ; i++) {
			 matrixPages = webDriver.getElementsByXpath("//*[@id='DataPager1']/a[contains(@class,'pagerUnselect')]");
			 matrixPages.get(i).click();
			sleep(2);
		 }
		
	}



	private void findUserandLock(String actualstudent) throws Exception {
		
		report.report("Testing pages " + (nextSectionCounter * 5 + 1) + " until " + (nextSectionCounter * 5 + 5)); //there are 5 page that display at given time
		while (true) {
			boolean foundFlag = false;
			
			for (int i = 0 ; i < 5 ; i ++) {
				matrixPages = webDriver.getElementsByXpath("//*[@id='DataPager1']/span[contains(@class,'pagerCurrent')]");
				matrixStudents = webDriver.getElementsByXpath("//span[contains(@class,'studentName')]");
				for (int j = 0 ; j < matrixStudents.size(); j ++) {
					String tempStudent = matrixStudents.get(j).getText();
					tempStudent = tempStudent.substring(0, tempStudent.length() - 5);
					if (tempStudent.equals(actualstudent)){
						report.report("Student found on page " + (nextSectionCounter * 5 + 1 + i));
						foundFlag = true;
						break;
					}
				}
				if (foundFlag) 
					break;
				else {
					if (i != 4) {
						matrixPages.get(i).click();
						nextPageCounter ++;
						sleep(2);
					}
				}
			}
			if (foundFlag) 
				break;
			else {
				boolean pressedNextOnce = false;
				if (pressedNextOnce)
					webDriver.waitForElement("//*[@id='DataPager1']/a[6]", ByTypes.xpath,"Couldn't press the next session button").click();
				else {
					webDriver.waitForElement("//*[@id='DataPager1']/a[5]", ByTypes.xpath,"Couldn't press the next session button").click();
					pressedNextOnce = true;
				}
				sleep(2);
				nextSectionCounter ++;
				report.report("Testing pages " + (nextSectionCounter * 5 + 1) + " until " + (nextSectionCounter * 5 + 5));
				nextPageCounter = 0;
			}
		}
		
	}



	@Test
	@Category(AngularLearningArea.class)
	@TestCaseParams(testCaseID = {"55310"})
	public void testMatrixPrintCertificate() throws Exception {
		String studentName = "";
		String password = "";
		String className = "";
		String course = coursesNames[1];//"Basic 1";
		String noCertificateString = "The selected student(s) are not eligible for an end of course certificate.";
		loginPage = new NewUXLoginPage(webDriver, testResultService);
	
		
				String[]student = pageHelper.getStudentWithProgressFinalGrade(pageHelper.institutionMatrixId_oldTE);
				studentName = student[0];
				password = student[1];
				studentId = student[2];
				className = pageHelper.getClassNameOfParticularUser(studentId); 
			//	String s = BasicNewUxTest.institutionId;
				
				classId = pageHelper.getInternalClassId(className, institutionId);
				institutionName = BasicNewUxTest.institutionsName[0];
			//	pageHelper.openCILatestUXLink();
				tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
				tmsHomePage.waitForPageToLoad();
		
		/*		
		report.startStep("Init test data");
			instID = configuration.getProperty("institution.id");
			String course = "Intermediate 2";
			String userName = "stForAssessment";
			String studentId = dbService.getUserIdByUserName(userName, instID);
			matrixAdminCleanup = true;
			String noCertificateString = "The selected student(s) are not eligible for an end of course certificate.";
				
		report.startStep("Login to TMS as admin");
			loginPage = new NewUXLoginPage(webDriver, testResultService);
			pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(configuration.getProperty("schoolAdmin.user"),instID));
			tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
			sleep(6);
		*/	
		report.startStep("Navigate to Matrix report");
			tmsHomePage.switchToMainFrame();
			tmsHomePage.clickOnReports();
			sleep(2);
			tmsHomePage.clickOnCourseReports();
			sleep(2);
			tmsHomePage.switchToFormFrame();
			tmsHomePage.selectReport("9");
			//tmsHomePage.selectClass(configuration.getProperty("classname.assessment"), false, true);
			tmsHomePage.selectClass(className, false, true);
			
		report.startStep("Select relevant course in Matrix");
			webDriver.switchToNewWindow();
			//webDriver.selectElementFromComboBox("DDLCources", course);
			webDriver.selectValueFromComboBox("DDLCources", course);
			waitForMatrixLoadingToEnd(3,15,5);
			webDriver.printScreen("Matrix load end");
			
		report.startStep("Verify Student not eligible for certificate");
			
			TmsMatrixPage matrixPage = new TmsMatrixPage(webDriver, testResultService);
			int index = matrixPage.findStudentAndPrintCertificate(studentName);
			//ClickOnstudentCheckBox(index);
			
	//		webDriver.waitForElement("btnPrint", ByTypes.id).click();
	//		sleep(2);
	//		webDriver.switchToPopup();
	//		testResultService.assertEquals(noCertificateString, webDriver.waitForElement("/html/body", ByTypes.xpath).getText());
	//		webDriver.closeNewTab(1);
			
		
		report.startStep("Verify Certificate no available with open locker");	
			
			boolean isOpen = matrixPage.checkLockerOpen(index);
			if(!isOpen) {
				clickOnLockButton(index);
			}
			sleep(1);
			ClickOnstudentCheckBox(index);
			sleep(1);
			webDriver.waitForElement("btnPrint", ByTypes.id).click();
			sleep(3);
			webDriver.switchToPopup();
			testResultService.assertEquals(noCertificateString, webDriver.waitForElement("/html/body", ByTypes.xpath).getText());
			webDriver.closeNewTab(1);
			webDriver.switchToNewWindow();
			
		report.startStep("Verify Certificate available with closed locker");	
			//ClickOnstudentCheckBox(index);
			isOpen = matrixPage.checkLockerOpen(index);
			if(isOpen) {
				clickOnLockButton(index);
			}
			
			sleep(3);
			clickOnPrintButtonOfParticularUser(index);
			//webDriver.waitForElement("btnPrint", ByTypes.id).click();
			sleep(3);
			webDriver.switchToPopup();
			String url = webDriver.getUrl();
			textService.assertTrue("Procedure not match", url.contains("GetUserCertificate"));//  GetClassCertificates
			textService.assertTrue("Class id is not match", url.contains(studentId)); //classId
			webDriver.closeNewTab(1);
			webDriver.switchToNewWindow();
			
		report.startStep("Verify final grade");	
			ClickOnstudentCheckBox(index);
			String finalGrade = getFinalGradeOfParticularUser(index);
			if (Integer.parseInt(finalGrade) < 48) {
				testResultService.addFailTest("Final grade isn't sufficient for certificate");
			}
		report.startStep("Edit manual grade values");	
			//webDriver.switchToNewWindow();
			clickOnEditIcon(index);
			sleep(1);
			typeIntoGrades("100",index);
			matrixDBCleanup = true;
			sleep(1);
			clickOnSave(index);
			sleep(1);
						
			webDriver.closeNewTab(1);
			webDriver.switchToMainWindow();
	//	report.startStep("Remove user lock");
			//webDriver.closeNewTab(1);
			//webDriver.getWebDriver().close();
		//	webDriver.switchToNewWindow();
		//	ClickOnstudentCheckBox(index);
		//	webDriver.waitForElement("btnUnLock", ByTypes.id).click();
		//	sleep(2);
		//	webDriver.closeNewTab(0);
	
		report.startStep("Exit from tms");	
			tmsHomePage.clickOnExitTMS();
		//report.startStep("Clean DB");	
		//	dbService.removeMatrixManualGradesForStudent(studentId);
		//	matrixDBCleanup = false;
	}

	


	private String getFinalGradeOfParticularUser(int index) throws Exception {
		
		return webDriver.waitForElement("baseList_ctrl"+index+"_col6", ByTypes.id).getText();
	}


	private void clickOnPrintButtonOfParticularUser(int index) throws Exception {
		webDriver.waitForElement("baseList_ctrl"+index+"_btnItemPrint", ByTypes.id).click();
		
	}


	private void clickOnLockButton(int index) throws Exception {
		webDriver.waitForElement("baseList_ctrl"+index+"_btnItemLock", ByTypes.id).click();
		
	}


	private void typeIntoGrades(String string,int index) throws Exception {
		//webDriver.waitForElement("baseList_ctrl0_divValueCol4", ByTypes.id).sendKeys("100");
		//webDriver.waitForElement("baseList_ctrl0_divValueCol5", ByTypes.id).sendKeys("100");
		WebElement typeBox = webDriver.waitForElement("baseList_ctrl"+index+"_divComment", ByTypes.id); ///here failures
		typeBox.click();
		typeBox.clear();
		typeBox.sendKeys(string);
		
	}


	private void ClickOnstudentCheckBox(int index) throws Exception {
		webDriver.waitForElement("baseList_ctrl"+index+"_chbStudentSelect", ByTypes.id).click();
	}



	@Test
	@Category(AngularLearningArea.class)
	@TestCaseParams(testCaseID = {"55132"})
	public void testMatrixSendResults() throws Exception {
		
		report.startStep("Init test data");
			//instID = configuration.getProperty("institution.id");
			String course = "Intermediate 2";
			matrixAdminCleanup = false;
			String userName = "stForAssessment";
			String studentId = dbService.getUserIdByUserName(userName, institutionId);
			String teacherMessageRow;
			List<WebElement> messageContent;
			String dbText;
				
		report.startStep("Login to TMS as teacher");
			loginPage = new NewUXLoginPage(webDriver, testResultService);
			pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(configuration.getProperty("teacher.username"),institutionId));
			tmsHomePage = loginPage.enterTeacherUserAndPassword();
			sleep(3);
			
		report.startStep("Navigate to Matrix report");
			tmsHomePage.switchToMainFrame();
			tmsHomePage.clickOnReports();
			sleep(2);
			tmsHomePage.clickOnCourseReports();
			sleep(2);
			tmsHomePage.switchToFormFrame();
			tmsHomePage.selectReport("9");
			tmsHomePage.selectClass(configuration.getProperty("classname.assessment"), false, true);
		
		report.startStep("Select relevant course in Matrix");
			webDriver.switchToNewWindow();
			webDriver.selectElementFromComboBox("DDLCources", course);
			waitForMatrixLoadingToEnd(3,15,5);
			webDriver.printScreen("Matrix load end");
			
		report.startStep("Edit manual grade values");	
			clickOnEditIcon(0);
			sleep(1);
			//webDriver.waitForElement("baseList_ctrl0_divValueCol4", ByTypes.id).sendKeys("100");
			WebElement typeBox = webDriver.waitForElement("baseList_ctrl1_divComment", ByTypes.id); ///here failures
			typeBox.click();
			typeBox.sendKeys("100");//*[@id="baseList_ctrl0_divComment"] 
			matrixDBCleanup = true;
			sleep(1);
			clickOnSave(0);
			
			sleep(1);
		
		report.startStep("Send results to student");	
			ClickOnstudentCheckBox(0);
			sleep(3);
			webDriver.waitForElement("btnLock", ByTypes.id).click();
			sleep(3);
			webDriver.printScreen("AfterLockingBeforeSend");
			ClickOnstudentCheckBox(0);
			sleep(2);
			webDriver.waitForElement("btnSend", ByTypes.id).click();
		
		report.startStep("Remove user lock");
			ClickOnstudentCheckBox(0);
			webDriver.waitForElement("btnUnLock", ByTypes.id).click();
			sleep(2);
			webDriver.closeNewTab(0);
			sleep(1);
			
		report.startStep("Clean DB");	
			dbService.removeMatrixManualGradesForStudent(studentId);
			matrixDBCleanup = false;
			
		report.startStep("Navigate to Teacher sent items");
			webDriver.switchToMainWindow();
			tmsHomePage.switchToMainFrame();
			tmsHomePage.clickOnCommunication();
			tmsHomePage.clickOnSentItems();
			
		report.startStep("Verify sent email content");
			webDriver.switchToFrame("tableFrame");
			teacherMessageRow = dbService.getValueFromOutBoxBySubject("MessageId", "Course Results");
			webDriver.waitForElement("view" + teacherMessageRow, ByTypes.id).click();
			webDriver.switchToPopup();
			sleep(2);
			messageContent = webDriver.getElementsByXpath("//div[contains(@name,'stext')]/table/tbody");
			dbText = dbService.getValueFromOutBoxBySubject("Message", "Course Results").replace("</span>&nbsp;", " ").replace("&nbsp;", "  \n ").replace("/", "").replace("<span>", "").replace("<td>", "").replace("<td align='left'>", "").replace("<td align='left' >", "").replace("<tr>", "").replace("<table>", "").replace("<b>", "").replace("<i>", "").replace("  \n   \n ", "  \n ").replace("Course Matrix Report(Intermediate 2)  ", "Course Matrix Report (Intermediate 2)").replace("Final Grade: 69  \n ", "Final Grade: 69  ");
			testResultService.assertEquals(messageContent.get(0).getText(), dbText);
			
		report.startStep("Logout of TMS");
			webDriver.closeNewTab(0);
			webDriver.switchToMainWindow();
			webDriver.switchToTopMostFrame();
			tmsHomePage.switchToMainFrame();
			tmsHomePage.clickOnExit();	
			
		report.startStep("Login to ED as student and enter Inbox");
			pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(userName,institutionId));
			homePage = loginPage.loginAsStudent(userName, "12345");
			sleep(3);
			homePage.closeModalPopUp();
			inboxPage = homePage.openInboxPage(false);
			sleep(5);
			inboxPage.switchToInboxFrame();
		
		report.startStep("Verify received email content");	
			webDriver.waitForElement("msg" + teacherMessageRow, ByTypes.id).click();
			sleep(2);
			webDriver.switchToPopup();
			webDriver.switchToFrame("ReadWrite");
			messageContent = webDriver.getElementsByXpath("//*[@id='WebPalsTextAreaId']/table/tbody");
			dbText = dbText.replace("  \n ", "\n").replace("\n ", "\n").replace("  ", "");
			testResultService.assertEquals(messageContent.get(0).getText(), dbText);
			webDriver.closeNewTab(1);
			webDriver.closeNewTab(0);
			sleep(1);
		
		report.startStep("DB message cleanup and logout");
			dbService.deleteOutboxByMessageId(teacherMessageRow);
			homePage.clickOnLogOut();
		
	}
	
	@Test
	//@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "51239","51238","81926" })
	public void testMidTestResumeModeNewTERoundLevel3() throws Exception {
		
		TestEnvironmentPage testEnvironmentPage = new TestEnvironmentPage(webDriver,testResultService);
			
		report.startStep("Get default auto student details");
			String instName = "Courses";
			String institutionId = dbService.getInstituteIdByName(instName);//configuration.getInstitutionId();
			String className = configuration.getProperty("classname.CourseTest");
			String courseCode = "B1";
			String impType = "2";
			String resultScore = "";
			String reportType = "Course Tests Report";
			String wrongAnswerFilePath = "files/CourseTestData/MidTermResumeWrongAnswers.csv";
			String wantedTestId = "989017755";
		//	String ServerPath=""+PageHelperService.sharePhisicalFolder+"Runtime\\Metadata\\Courses\\";
			JSONObject jsonObj = new JSONObject();
			report.startStep("Retrieve Localization Json US");
			JSONObject localizationJson = netService.getJsonObject("smb://"+PageHelperService.physicalPath+"/Runtime/Localization/TestInstructions/", "en_US", false);
			NewUXLoginPage loginPage = new NewUXLoginPage(webDriver,testResultService);
			
			CourseTests testType = CourseTests.MidTerm;
			String answersFilePath = "files/CourseTestData/MidTerm_Answers.csv";
			CourseCodes wantedCourseCode = testEnvironmentPage.getCourseCodeEnumByCourseCodeString(courseCode);	
		
		report.startStep("Retrieve the URL");
			String url = webDriver.getUrl();
			
		report.startStep("Replace 'Automation' to 'Courses'");
			url = url.replace("automation", "courses");
			
		report.startStep("Change to Correct URL");
			webDriver.openUrl(url);
			
		report.startStep("Login as student");
			homePage = createUserAndLoginNewUXClass(className, institutionId);
			homePage.waitHomePageloaded();
			
		report.startStep("Assign B1 Mid-Term Test to student");
			String courseId = getCourseIdByCourseCode(courseCode);
			String courseName = getCourseNameByCourseCode(courseCode);
			pageHelper.assignAutomatedTestToStudetInTestEnvironment(studentId, courseId, Integer.parseInt(impType),0,0,1);
			sleep(5);
			
		report.startStep("Update Test Id");
			String testId = pageHelper.getAssignedTestIdForStudent(studentId,courseId, Integer.parseInt(impType));
			String userExitSettingsId = dbService.getUserExitSettingsId(studentId, testId, courseId, impType);
			if (!testId.equals(wantedTestId)) {
				dbService.updateTestIdInUserExitSettings(wantedTestId, userExitSettingsId);
				testId = pageHelper.getAssignedTestIdForStudent(studentId,courseId, Integer.parseInt(impType));
				String token = pageHelper.getTokenFromConsoleWithParameters("1", studentId, testId, userExitSettingsId);
				dbService.updateAssignWithToken(token, userExitSettingsId);
			}
			
		report.startStep("Change JSON of Test to Round 3");
			pageHelper.runConsoleProgram("cmd /c copy "+PageHelperService.ServerPath+"CourseTests\\"+testId+"_RoundLevel3\\"+testId+".json "+PageHelperService.ServerPath+"CourseTests");
		
		report.startStep("Retrieve JSON Array of Test ID: " + testId);
			jsonObj = netService.getJsonObject("smb://"+PageHelperService.physicalPath+"/Runtime/Metadata/Courses/CourseTests/", testId, false);
			
		report.startStep("Open Assessments Page");
			testsPage = homePage.openAssessmentsPage(false);
			
		report.startStep("Start Test");
			int testSequence = testsPage.getTestSequenceByCourseId(courseId);
			String testName = testsPage.clickStartTest(1, testSequence);
			webDriver.closeAlertByAccept();
			sleep(3);	
			testEnvironmentPage.sectionIndex = 1;
			
		report.startStep("Get Round Level of Course");
			String roundLevelOfCourse = testEnvironmentPage.getRoundLevelFromJson(jsonObj, testId);
			
			// Get list of lesson count for each unit in course
			List<String[]> lessonCountForUnits = dbService.getLessonCountForAllUnitsInCourse(testId);
			
			// Get unit ids list from previous list
			List<String> unitIds = new ArrayList<String>();
			for (int j = 0; j < lessonCountForUnits.size(); j++) {
				unitIds.add(lessonCountForUnits.get(j)[0]);
			}
			
		report.startStep("Validate Course Intro");
			testEnvironmentPage.validateIntro(jsonObj, localizationJson, testId, testName);
			
		report.startStep("Start test and submit first section with correct answers");
			int sectionIndex = 0;
			int unitIndex = 0;
			int sectionIndexInUnit = 0;
			testEnvironmentPage.answerQuestionInSpecificSectionNewTE(answersFilePath, testId, wantedCourseCode, testType, sectionIndex, roundLevelOfCourse, unitIds, jsonObj, localizationJson, testName, unitIndex, sectionIndexInUnit);
			sleep(3);
			
		report.startStep("Close Test and Logout");
			testEnvironmentPage.clickClose();
			homePage.clickOnLogOut();
			testResultService.assertEquals(false, loginPage.isSubmitButtonEnabled(), "Checking that ED Login Page displayed");
			
		report.startStep("Login as Admin and select course test report");
			String tID = dbService.getUserIdByUserName("admin", institutionId);
			pageHelper.setUserLoginToNull(tID);
			loginPage.loginAsStudent("admin", "12345");
			sleep(3);
			
			TmsReportsPage tmsReportsPage = new TmsReportsPage(webDriver, testResultService);
			
		report.startStep("Select Course Test Report");
			tmsReportsPage.goToCourseReports();
			tmsReportsPage.selectCourseReportAndClassAndCourse(reportType, className, courseId);
			
		report.startStep("Validate no Score is Displayed for Student");
			String userName = dbService.getUserNameById(studentId, institutionId);
			tmsHomePage = new TmsHomePage(webDriver, testResultService);
			String tmsScore = tmsHomePage.MidTermTestScoreResumeMode(userName);
			testResultService.assertEquals(resultScore, tmsScore, "Final Score in TMS does not match to test result in Assessments page");
			sleep(3);
			
		report.startStep("Validate CTcompleted is 0 in DB");
			List<String[]> studentTestProgress = dbService.getUserTestProgressByUserId(studentId);
			String iscompleted = studentTestProgress.get(0)[8];
			testResultService.assertEquals("0", iscompleted, "Test Marked as Completed in DB");
			
		report.startStep("Log Out");
			webDriver.switchToMainWindow();
			tmsHomePage.switchToMainFrame();
			tmsHomePage.clickOnExit();
		
		report.startStep("Login as student");
			homePage = loginPage.loginAsStudent(userName, "12345");
			
		report.startStep("Open Assessments and Resume test");
			testsPage = homePage.openAssessmentsPage(false); 
			testsPage.clickOnStartTest("1", "1");
			sleep(1);
				
		report.startStep("Answer Incorrectly Section 2");
			sectionIndex = 1;
			unitIndex = 0;
			sectionIndexInUnit = 1;
			testEnvironmentPage.answerQuestionInSpecificSectionNewTE(answersFilePath, testId + "#", wantedCourseCode, testType, sectionIndex, roundLevelOfCourse, unitIds, jsonObj, localizationJson, testName, unitIndex, sectionIndexInUnit);
			sleep(2);
			
			report.startStep("Do not Answer Sections 3,4 (skip)");
			testEnvironmentPage.sectionIndex = 2;
			//testEnvironmentPage.lessonIndex = 1;
			int sectionsToSubmit = 2; // dont answer section 3,4
			for (int i = 0; i < sectionsToSubmit ; i++) {
				sectionIndex = i+2;
				unitIndex = 1;
				sectionIndexInUnit = i;
				testEnvironmentPage.lessonIndex = sectionIndexInUnit+1;
				testEnvironmentPage.submitSectionEmptyNewTE(answersFilePath, testId + "#", wantedCourseCode, testType, sectionIndex, roundLevelOfCourse, unitIds, jsonObj, localizationJson, testName, unitIndex, sectionIndexInUnit);
				sleep(2);
			}
			
		report.startStep("Answer Incorrectly Sections 5-10");
		testEnvironmentPage.sectionIndex = 3;
		//testEnvironmentPage.lessonIndex = 1;
			sectionsToSubmit = 5;
			for (int i = 0; i <= sectionsToSubmit; i++) {
				sectionIndex = i+4;
				unitIndex = 2;
				sectionIndexInUnit = i;
				testEnvironmentPage.lessonIndex = sectionIndexInUnit + 1;
				testEnvironmentPage.answerQuestionInSpecificSectionNewTE(answersFilePath, testId + "#", wantedCourseCode, testType, sectionIndex, roundLevelOfCourse, unitIds, jsonObj, localizationJson, testName, unitIndex, sectionIndexInUnit);
			}
			sleep(2);
		
		report.startStep("Get score and close test");
		// get score from screen- need to add once developed
		report.startStep("Validate Score in DB");
			String scoreForDisplayDB = dbService.getUserTestProgressByUserId(studentId).get(0)[4];
		
		report.startStep("Click Exit Button");
			testEnvironmentPage.clickExitCourseTest();
			
		report.startStep("Validate CTcompleted is 1 in DB");
			studentTestProgress = dbService.getUserTestProgressByUserId(studentId);
			iscompleted = studentTestProgress.get(0)[8];
			testResultService.assertEquals("1", iscompleted, "Test Not Marked as Completed in DB");
				
		report.startStep("Open Assessments");
			homePage.openAssessmentsPage(false);
							
		report.startStep("Open Tests Results section");
			testsPage.clickOnArrowToOpenSection("3");
			
		report.startStep("Verify B1 Mid-Term Test displayed in Tests Results section");
			testsPage.checkTestDisplayedInSectionByTestName(testName, "3", "1");
								
		report.startStep("Get Date of Submission");
			String testDate = testsPage.checkSubmissionDateForTests("1");
			
		report.startStep("Check Score in Test Results");
			resultScore = testsPage.checkScoreByTest("1", scoreForDisplayDB);
			testsPage.close();
			sleep(1);
			
		report.startStep("Verify My Progress");
			verifyMidTermTestWidgetDisplayed(scoreForDisplayDB, courseName);
			
		report.startStep("Logout as student");
			homePage.clickOnLogOut();
			testResultService.assertEquals(false, loginPage.isSubmitButtonEnabled(), "Checking that ED Login Page displayed");
			
		report.startStep("Login as teacher and select course test report");
			tID = dbService.getUserIdByUserName("admin", institutionId);
			pageHelper.setUserLoginToNull(tID);
			loginPage.loginAsStudent("admin", "12345");
			sleep(3);
			
		report.startStep("Select Course Test Report");
			tmsReportsPage.goToCourseReports();
			tmsReportsPage.selectCourseReportAndClassAndCourse(reportType, className, courseId);
			
		report.startStep("Validate Score and Click it");	
		    tmsScore = tmsHomePage.pressOnMidTermTestScore(userName);
			testResultService.assertEquals(resultScore, tmsScore, "Final Score in TMS does not match to test result in Assessments page");
			sleep(3);
			
			if (!tmsScore.equals(" ")) {
				
				report.startStep("Switch to Course Report Page and check section titles");
					webDriver.switchToNewWindow();
				
				report.startStep("Validate Results");
					tmsCTR = new TmsCourseTestReport(webDriver, testResultService);
					tmsCTR.validateResultsNewTeNew(studentId, testId, userName, wrongAnswerFilePath);
			}
		report.startStep("Quit Browser");
			webDriver.quitBrowser();	
	}
	
	@Test
	//@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "51239","51238", "81926" })
	public void testMidTestResumeModeNewTERoundLevel2() throws Exception {

		TestEnvironmentPage testEnvironmentPage = new TestEnvironmentPage(webDriver,testResultService);
			
		report.startStep("Get default auto student details");
			String instName = "Courses";
			String institutionId = dbService.getInstituteIdByName(instName);//configuration.getInstitutionId();
			String className = configuration.getProperty("classname.CourseTest");
			String courseCode = "B1";
			String impType = "2";
			String resultScore = "";
			String reportType = "Course Tests Report";
			String wrongAnswerFilePath = "files/CourseTestData/MidTermResumeWrongAnswers.csv";
			String wantedTestId = "989017755";
			String ServerPath=""+PageHelperService.sharePhisicalFolder+"Runtime\\Metadata\\Courses\\";
			JSONObject jsonObj = new JSONObject();
			report.startStep("Retrieve Localization Json US");
			JSONObject localizationJson = netService.getJsonObject("smb://"+PageHelperService.physicalPath+"/Runtime/Localization/TestInstructions/", "en_US", false);
			NewUXLoginPage loginPage = new NewUXLoginPage(webDriver,testResultService);
			
			CourseTests testType = CourseTests.MidTerm;
			String answersFilePath = "files/CourseTestData/MidTerm_Answers.csv";
			CourseCodes wantedCourseCode = testEnvironmentPage.getCourseCodeEnumByCourseCodeString(courseCode);	
		
		report.startStep("Retrieve the URL");
			String url = webDriver.getUrl();
			
		report.startStep("Replace 'Automation' to 'Courses'");
			url = url.replace("automation", "courses");
			
		report.startStep("Change to Correct URL");
			webDriver.openUrl(url);
		
		////
		
		report.startStep("Login as student");
			homePage = createUserAndLoginNewUXClass(className, institutionId);
			homePage.waitHomePageloaded();
			
		report.startStep("Assign B1 Mid-Term Test to student");
			String courseId = getCourseIdByCourseCode(courseCode);
			String courseName = getCourseNameByCourseCode(courseCode);
			pageHelper.assignAutomatedTestToStudetInTestEnvironment(studentId, courseId, Integer.parseInt(impType),0,0,1);
			sleep(5);
			
		report.startStep("Update Test Id");
			String testId = pageHelper.getAssignedTestIdForStudent(studentId,courseId, Integer.parseInt(impType));
			String userExitSettingsId = dbService.getUserExitSettingsId(studentId, testId, courseId, impType);
			if (!testId.equals(wantedTestId)) {
				dbService.updateTestIdInUserExitSettings(wantedTestId, userExitSettingsId);
				testId = pageHelper.getAssignedTestIdForStudent(studentId,courseId, Integer.parseInt(impType));
				String token = pageHelper.getTokenFromConsoleWithParameters("1", studentId, testId, userExitSettingsId);
				dbService.updateAssignWithToken(token, userExitSettingsId);
			}
			
		report.startStep("Change JSON of Test to Round 2");
			pageHelper.runConsoleProgram("cmd /c copy "+ServerPath+"CourseTests\\"+testId+"_RoundLevel2\\"+testId+".json "+ServerPath+"CourseTests");
		
		report.startStep("Retrieve JSON Array of Test ID: " + testId);
			jsonObj = netService.getJsonObject("smb://"+PageHelperService.physicalPath+"/Runtime/Metadata/Courses/CourseTests/", testId, false);
			
		report.startStep("Open Assessments Page");
			testsPage = homePage.openAssessmentsPage(false);
			
		report.startStep("Start Test");
			int testSequence = testsPage.getTestSequenceByCourseId(courseId);
			String testName = testsPage.clickStartTest(1, testSequence);
			webDriver.closeAlertByAccept();
			sleep(3);
			
		report.startStep("Get Round Level of Course");
			String roundLevelOfCourse = testEnvironmentPage.getRoundLevelFromJson(jsonObj, testId);
			
		// Get list of lesson count for each unit in course
			List<String[]> lessonCountForUnits = dbService.getLessonCountForAllUnitsInCourse(testId);
			
		// Get unit ids list from previous list
			List<String> unitIds = new ArrayList<String>();
			for (int j = 0; j < lessonCountForUnits.size(); j++) {
				unitIds.add(lessonCountForUnits.get(j)[0]);
			}
			
		report.startStep("Validate Course Intro");
			testEnvironmentPage.validateIntro(jsonObj, localizationJson, testId, testName);
			
		report.startStep("Start test and submit first section with correct answers");
			int sectionIndex = 0;
			int unitIndex = 0;
			int sectionIndexInUnit = 0;
			testEnvironmentPage.answerQuestionInSpecificSectionNewTE(answersFilePath, testId, wantedCourseCode, testType, sectionIndex, roundLevelOfCourse, unitIds, jsonObj, localizationJson, testName, unitIndex, sectionIndexInUnit);
			sleep(3);
			
		report.startStep("Close Test and Logout");
			testEnvironmentPage.clickClose();
			homePage.clickOnLogOut();
			testResultService.assertEquals(false, loginPage.isSubmitButtonEnabled(), "Checking that ED Login Page displayed");
			
		report.startStep("Login as Admin and select course test report");
			String tID = dbService.getUserIdByUserName("admin", institutionId);
			pageHelper.setUserLoginToNull(tID);
			loginPage.loginAsStudent("admin", "12345");
			sleep(6);
			
			TmsReportsPage tmsReportsPage = new TmsReportsPage(webDriver, testResultService);
			
		report.startStep("Select Course Test Report");
			tmsReportsPage.goToCourseReports();
			tmsReportsPage.selectCourseReportAndClassAndCourse(reportType, className, courseId);
			
		report.startStep("Validate no Score is Displayed for Student");
			String userName = dbService.getUserNameById(studentId, institutionId);
			tmsHomePage = new TmsHomePage(webDriver, testResultService);
			String tmsScore = tmsHomePage.MidTermTestScoreResumeMode(userName);
			testResultService.assertEquals(resultScore, tmsScore, "Final Score in TMS does not match to test result in Assessments page");
			sleep(3);
			
		report.startStep("Validate CTcompleted is 0 in DB");
			List<String[]> studentTestProgress = dbService.getUserTestProgressByUserId(studentId);
			String iscompleted = studentTestProgress.get(0)[8];
			testResultService.assertEquals("0", iscompleted, "Test Marked as Completed in DB");
			
		report.startStep("Log Out");
			webDriver.switchToMainWindow();
			tmsHomePage.switchToMainFrame();
			tmsHomePage.clickOnExit();
		
		report.startStep("Login as student");
			homePage = loginPage.loginAsStudent(userName, "12345");
			
		report.startStep("Open Assessments and Resume test");
			testsPage = homePage.openAssessmentsPage(false); 
			testSequence = testsPage.getTestSequenceByCourseId(courseId);
			testsPage.clickStartTest(1, testSequence);
			webDriver.closeAlertByAccept();
			sleep(3);
				
		report.startStep("Answer Incorrectly Section 2");
			testEnvironmentPage.sectionIndex = 1;
			testEnvironmentPage.lessonIndex = 2;
			sectionIndex = 1;
			unitIndex = 0;
			sectionIndexInUnit = 1;
			testEnvironmentPage.answerQuestionInSpecificSectionNewTE(answersFilePath, testId + "#", wantedCourseCode, testType, sectionIndex, roundLevelOfCourse, unitIds, jsonObj, localizationJson, testName, unitIndex, sectionIndexInUnit);
			sleep(2);
			
		report.startStep("Validate Resume Records is Saved in DB");
			List<String[]> userTestComponentProgress = dbService.getUserTestComponentProgressByUserId(studentId);
			String userTestComponentProgressId = userTestComponentProgress.get(0)[0];
			testEnvironmentPage.validateResumeRecordExist(userTestComponentProgressId);
			
		report.startStep("Do not Answer Sections 3,4 (skip)");
		testEnvironmentPage.sectionIndex = 2;
		//testEnvironmentPage.lessonIndex = 1;
			int sectionsToSubmit = 2; // dont answer section 3,4
			for (int i = 0; i < sectionsToSubmit ; i++) {
				sectionIndex = i+2;
				unitIndex = 1;
				sectionIndexInUnit = i;
				testEnvironmentPage.lessonIndex = sectionIndexInUnit+1;
				testEnvironmentPage.submitSectionEmptyNewTE(answersFilePath, testId + "#", wantedCourseCode, testType, sectionIndex, roundLevelOfCourse, unitIds, jsonObj, localizationJson, testName, unitIndex, sectionIndexInUnit);
				sleep(2);
			}
			
		report.startStep("Answer Incorrectly Sections 5-10");
		testEnvironmentPage.sectionIndex = 3;
		//testEnvironmentPage.lessonIndex = 1;
			sectionsToSubmit = 5;
			for (int i = 0; i <= sectionsToSubmit; i++) {
				sectionIndex = i+4;
				//testEnvironmentPage.sectionIndex = sectionIndex+1;
				unitIndex = 2;
				sectionIndexInUnit = i;
				testEnvironmentPage.lessonIndex = sectionIndexInUnit+1;
				testEnvironmentPage.answerQuestionInSpecificSectionNewTE(answersFilePath, testId + "#", wantedCourseCode, testType, sectionIndex, roundLevelOfCourse, unitIds, jsonObj, localizationJson, testName, unitIndex, sectionIndexInUnit);
			}
			sleep(2);
		
		report.startStep("Get score and close test");
		report.startStep("Validate Score in DB");
			String scoreForDisplayDB = dbService.getUserTestProgressByUserId(studentId).get(0)[4];
			
		report.startStep("Validate Score");
			testEnvironmentPage.validateScoreEndOfTest(scoreForDisplayDB);
		
		report.startStep("Click Exit Button");
			testEnvironmentPage.clickExitCourseTest();
			
		report.startStep("Validate CTcompleted is 1 in DB");
			studentTestProgress = dbService.getUserTestProgressByUserId(studentId);
			iscompleted = studentTestProgress.get(0)[8];
			testResultService.assertEquals("1", iscompleted, "Test Not Marked as Completed in DB");
				
		report.startStep("Open Assessments");
			homePage.openAssessmentsPage(false);
							
		report.startStep("Open Tests Results section");
			testsPage.clickOnArrowToOpenSection("3");
			
		report.startStep("Verify B1 Mid-Term Test displayed in Tests Results section");
			testsPage.checkTestDisplayedInSectionByTestName(testName, "3", "1");
								
		report.startStep("Get Date of Submission");
			String testDate = testsPage.checkSubmissionDateForTests("1");
			
		report.startStep("Check Score in Test Results");
			resultScore = testsPage.checkScoreByTest("1", scoreForDisplayDB);
			testsPage.close();
			sleep(1);
			
		report.startStep("Verify My Progress");
			verifyMidTermTestWidgetDisplayed(scoreForDisplayDB, courseName);
			
		report.startStep("Logout as student");
			homePage.clickOnLogOut();
			testResultService.assertEquals(false, loginPage.isSubmitButtonEnabled(), "Checking that ED Login Page displayed");
		
		////
			
			/*TmsReportsPage tmsReportsPage = new TmsReportsPage(webDriver, testResultService);
			tmsHomePage = new TmsHomePage(webDriver, testResultService);
			String courseId = getCourseIdByCourseCode(courseCode);
			String tID;
			String tmsScore;
			String testId = wantedTestId;
			String userName = "stud12220673";
			studentId = dbService.getUserIdByUserName(userName, institutionId);
			resultScore = "42";*/
			
		report.startStep("Login as admin and select course test report");
			tID = dbService.getUserIdByUserName("admin", institutionId);
			pageHelper.setUserLoginToNull(tID);
			loginPage.loginAsStudent("admin", "12345");
			sleep(3);
			
		report.startStep("Select Course Test Report");
			tmsReportsPage.goToCourseReports();
			tmsReportsPage.selectCourseReportAndClassAndCourse(reportType, className, courseId);
			sleep(3);
			
		report.startStep("Validate Score and Click it");	
		    tmsScore = tmsHomePage.pressOnMidTermTestScore(userName);
			testResultService.assertEquals(resultScore, tmsScore, "Final Score in TMS does not match to test result in Assessments page");
			sleep(3);
			
			if (!tmsScore.equals(" ")) {
				
				report.startStep("Switch to Course Report Page and check section titles");
					webDriver.switchToNewWindow();
				
				report.startStep("Validate Results");
					tmsCTR = new TmsCourseTestReport(webDriver, testResultService);
					tmsCTR.validateResultsNewTeNew(studentId, testId, userName, wrongAnswerFilePath);
			}
		report.startStep("Quit Browser");
			webDriver.quitBrowser();	
	}
	
	@Test
	//@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "" })
	public void testMatrixNewTE() throws Exception {
		
		report.startStep("Get default auto student details");
		String instName = "Courses";
		String institutionId = dbService.getInstituteIdByName(instName);//configuration.getInstitutionId();
		String className = configuration.getProperty("classname.CourseTest");
		String courseCode = "B1";
		String courseId = getCourseIdByCourseCode(courseCode);
		String courseName = dbService.getCourseNameById(courseId);
		String reportType = "End of Course Matrix";
		String wrongAnswerFilePath = "files/CourseTestData/MidTermResumeWrongAnswers.csv";
		String testId = "989017755";
		NewUXLoginPage loginPage = new NewUXLoginPage(webDriver,testResultService);
		
		report.startStep("Retrieve the URL");
		String url = webDriver.getUrl();
		
		report.startStep("Replace 'Automation' to 'Courses'");
		url = url.replace("automation", "courses");
		
		report.startStep("Change to Correct URL");
		webDriver.openUrl(url);
		
		String tID = dbService.getUserIdByUserName("admin", institutionId);
		pageHelper.setUserLoginToNull(tID);
		loginPage.loginAsStudent("admin", "12345");
		sleep(3);
		
		TmsReportsPage tmsReportsPage = new TmsReportsPage(webDriver, testResultService);
		
		List<String[]> userDetails = dbService.getUserDetailsByScoreAndCourseIdAndInstIdnewTE("42", courseId, institutionId);
		String userName = userDetails.get(0)[3];
		studentId = dbService.getUserIdByUserName(userName, institutionId);
		String userLastName = userDetails.get(0)[1];
		report.startStep("Change Last Name of User:" + userName + " to " + userLastName);//
		String newTempLastName = "0last";
		dbService.updateUserLastName(userLastName, newTempLastName);
		
		try {
		
			report.startStep("Select Course Test Report");
			tmsReportsPage.goToCourseReports();
			tmsReportsPage.selectMatricReportAndClickGo(reportType, className);
			
			TmsMatrixPage tmsMatrixPage = new TmsMatrixPage(webDriver, testResultService);
			tmsMatrixPage.chooseCourse(courseName);
			sleep(5);
			
			tmsMatrixPage.findStudentAndClickOnScore(userName);
			
			webDriver.switchToMainWindow();
			webDriver.switchToPopUp(2);
			
			TmsCourseTestReport tmsCTR = new TmsCourseTestReport(webDriver, testResultService);
			tmsCTR.validateResultsNewTeNew(studentId, testId, userName, wrongAnswerFilePath);
		} finally {
			report.startStep("Change Last Name of User:" + userName + " to " + userLastName);
			dbService.updateUserLastName(newTempLastName, userLastName);
		}
		
		webDriver.quitBrowser();

	}
	
	@Test
	//@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "" })
	public void testMatrixOldTE() throws Exception {
		
		report.startStep("Get default auto student details");
		//String instName = "Automation";
		//institutionId = dbService.getInstituteIdByName(instName);//configuration.getInstitutionId();
		String className = configuration.getProperty("classname.progress");
		String courseCode = "B1";
		String courseId = getCourseIdByCourseCode(courseCode);
		String courseName = dbService.getCourseNameById(courseId);
		String reportType = "End of Course Matrix";
		String wrongAnswerFilePath = "files/CourseTestData/MidTermResumeWrongAnswers.csv";
		String testId = "989012509";//"989013148";
		NewUXLoginPage loginPage = new NewUXLoginPage(webDriver,testResultService);
		
		/*report.startStep("Retrieve the URL");
		String url = webDriver.getUrl();
		
		report.startStep("Replace 'Automation' to 'Courses'");
		url = url.replace("automation", "courses");
		
		report.startStep("Change to Correct URL");
		webDriver.openUrl(url);*/
		
		String tID = dbService.getUserIdByUserName("admin", institutionId);
		pageHelper.setUserLoginToNull(tID);
		loginPage.loginAsStudent("admin", "12345");
		sleep(3);
		
		TmsReportsPage tmsReportsPage = new TmsReportsPage(webDriver, testResultService);
		
		List<String[]> userDetails = dbService.getUserDetailsByScoreAndCourseIdAndInstIdOldTE("20", courseId, institutionId);
		if (userDetails == null) {
			testResultService.addFailTest("No Records of Mid Term Tests with score 35 were found", true, false);
		}
		
		String userName = userDetails.get(0)[3];
		studentId = dbService.getUserIdByUserName(userName, institutionId);
		//String userLastName = userDetails.get(0)[1];
		//report.startStep("Change Last Name of User:" + userName + " to " + userLastName);//
		//String newTempLastName = "0last";
		//dbService.updateUserLastName(userLastName, newTempLastName);
		
		try {
		
			report.startStep("Select Matrix Report");
			tmsReportsPage.goToCourseReports();
			tmsReportsPage.selectMatricReportAndClickGo(reportType, className);
			
			report.startStep("Select Course");
			TmsMatrixPage tmsMatrixPage = new TmsMatrixPage(webDriver, testResultService);
			tmsMatrixPage.chooseCourse(courseName);
			sleep(5);
			
			report.startStep("Click On Score Of Student");
			tmsMatrixPage.findStudentAndClickOnScore(userName);
			
			report.startStep("Validate Report");
			webDriver.switchToMainWindow();
			webDriver.switchToPopUp(2);
			
			TmsCourseTestReport tmsCTR = new TmsCourseTestReport(webDriver, testResultService);
			tmsCTR.validateResultsOldTe(studentId, testId, userName, wrongAnswerFilePath);
		} finally {
			//report.startStep("Change Last Name of User:" + userName + " to " + userLastName);
			//dbService.updateUserLastName(newTempLastName, userLastName);
		}
		
		webDriver.quitBrowser();

	}
	
	@Test
	//@Category(SanityTests.class)
	@TestCaseParams(testCaseID = { "" })
	public void testPltTestReportNewTE() throws Exception {
		
		//String className = configuration.getProperty("classname.CourseTest");
		
		institutionName = institutionsName[1];
		pageHelper.restartBrowserInNewURL(institutionName, true);
		
		loginPage = new NewUXLoginPage(webDriver, testResultService);
		
		report.startStep("Login as Admin and select course test report");
		String tID = dbService.getUserIdByUserName("admin", institutionId);
		pageHelper.setUserLoginToNull(tID);
		loginPage.loginAsStudent("admin", "12345");
		sleep(2);
		//webDriver.refresh();
		//sleep(1);
		//tmsHomePage.waitForPageToLoad();
		TmsReportsPage tmsReportsPage = new TmsReportsPage(webDriver, testResultService);
		
		report.startStep("Select Placment Test Report");
		tmsReportsPage.goToPlacementTestReports();
		
		report.startStep("Retrieve Details of User that Completed PLT Test");
		String[] userPLtFinalResult = dbService.getUserFinalPltResultByInstIdNewTE(institutionId);
		String className = userPLtFinalResult[8];
		String studentNameDB = userPLtFinalResult[2] + " " + userPLtFinalResult[3];
		String[] levelsDB = {userPLtFinalResult[4],userPLtFinalResult[5],userPLtFinalResult[6], userPLtFinalResult[7]};
		
		report.startStep("Choose Placement Test Results Report, Class, and Click Go");
		tmsReportsPage.choosePltReportTypeAndClassAndClickGo("Placement Test Results", className);
		
		TmsPlacementTestResultsReportPage tmsPlacementTestResultsReportPage = new TmsPlacementTestResultsReportPage(webDriver, testResultService);
		
		report.startStep("Get Index of Student in Table");
		int studentIndex = tmsPlacementTestResultsReportPage.getIndexOfStudent(studentNameDB);
		
		report.startStep("Convert Levels Codes from DB to Levels Names");
		TestEnvironmentPage testEnvironmentPage = new TestEnvironmentPage(webDriver, testResultService);
		String[] convertedLevelsDB = new String[levelsDB.length];
		for (int i = 0; i < convertedLevelsDB.length; i++) {
			convertedLevelsDB[i] = testEnvironmentPage.convertPltLevelCodeToLevelString(levelsDB[i]);
		}
		
		report.startStep("Validate Levels are Correct in TMS");
		tmsPlacementTestResultsReportPage.validateLevelsAreCorrect(convertedLevelsDB, studentIndex);
		
		report.startStep("Choose Placement Test Summary Report, Class, and Click Go");
		tmsReportsPage.choosePltReportTypeAndClassAndTimeFrameAndClickGo("Placement Test Summary", className, "1 Month");
		
		TmsPlacementTestSummaryReportPage tmsPlacementTestSummaryReportPage = new TmsPlacementTestSummaryReportPage(webDriver, testResultService);
		
		report.startStep("Validate sum is correct for level: " + convertedLevelsDB[3]);
		int levelIndex = tmsPlacementTestSummaryReportPage.getLevelIndexInTable(convertedLevelsDB[3]);
		tmsPlacementTestSummaryReportPage.validateFinalReaultSumByLevel(levelIndex, 1); // currently hardcoded- need to change
		
		
		
		// find student and click on its report
		
		// report opened- validate it is correct
		
		// click user selection
		
		// validate export
		
		
		
		
		
	}

	@Test
	//@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "" })
	public void testPltTestReportOldTE() throws Exception {
		
		report.startStep("Login as Admin");
		pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(configuration.getProperty("schoolAdmin.user"),institutionId));
		NewUXLoginPage loginPage = new NewUXLoginPage(webDriver,testResultService);
		loginPage.loginAsStudent(configuration.getProperty("schoolAdmin.user"), "12345");
		sleep(3);
		
		TmsReportsPage tmsReportsPage = new TmsReportsPage(webDriver, testResultService);
		
		report.startStep("Select Placement Test Report");
		tmsReportsPage.goToPlacementTestReports();
		
		report.startStep("Retrieve Details of User that Completed PLT Test");
		String[] userPLtFinalResult = dbService.getUserFinalPltResultByInstId(institutionId);
		String className = userPLtFinalResult[8];
		String studentNameDB = userPLtFinalResult[2] + " " + userPLtFinalResult[3];
		String[] levelsDB = {userPLtFinalResult[4],userPLtFinalResult[5],userPLtFinalResult[6], userPLtFinalResult[7]};
		
		report.startStep("Choose Placement Test Results Report, Class, and Click Go");
		tmsReportsPage.choosePltReportTypeAndClassAndClickGo("Placement Test Results", className);
		
		TmsPlacementTestResultsReportPage tmsPlacementTestResultsReportPage = new TmsPlacementTestResultsReportPage(webDriver, testResultService);
		
		report.startStep("Get Index of Student in Table");
		int studentIndex = tmsPlacementTestResultsReportPage.getIndexOfStudent(studentNameDB);
		
		report.startStep("Convert Levels Codes from DB to Levels Names");
		TestEnvironmentPage testEnvironmentPage = new TestEnvironmentPage(webDriver, testResultService);
		String[] convertedLevelsDB = new String[levelsDB.length];
		for (int i = 0; i < convertedLevelsDB.length; i++) {
			convertedLevelsDB[i] = testEnvironmentPage.convertPltLevelCodeToLevelString(levelsDB[i]);
		}
		
		report.startStep("Validate Levels are Correct in TMS");
		tmsPlacementTestResultsReportPage.validateLevelsAreCorrect(convertedLevelsDB, studentIndex);
		
		report.startStep("Choose Placement Test Summary Report, Class, and Click Go");
		tmsReportsPage.choosePltReportTypeAndClassAndTimeFrameAndClickGo("Placement Test Summary", className, "2 Months");
		
		TmsPlacementTestSummaryReportPage tmsPlacementTestSummaryReportPage = new TmsPlacementTestSummaryReportPage(webDriver, testResultService);
		
		report.startStep("Validate sum is correct for level: " + convertedLevelsDB[3]);
		int levelIndex = tmsPlacementTestSummaryReportPage.getLevelIndexInTable(convertedLevelsDB[3]);
		tmsPlacementTestSummaryReportPage.validateFinalReaultSumByLevel(levelIndex, 1); // currently hardcoded- need to change
		
		// validate export
		//
		
		
	}
	
	@Test
	//@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "81918", "81919" })
	public void testPltStatusReport() throws Exception {
		
		institutionName=institutionsName[1];
		
		report.startStep("Change to Courses Instituion");
		pageHelper.restartBrowserInNewURL(institutionName, true); 
		
		////// TC- 81918 //////
		report.startStep("Init Data");
		int testTypeId = 1;
		String currentDate;
		String className = configuration.getProperty("classname.CourseTest"); //dbService.getClassNameByClassId(classId);
		String reportType = "Placement Test Status";
		String adminId = dbService.getAdminIdByInstId(institutionId);
		String classId = dbService.getClassIdByName(className, institutionId);
		String testId = "11";
		
		report.startStep("Create User in Status Unassigned");
		String userIdOfUnassigned = pageHelper.createUSerUsingSP(institutionId, className);
		
		report.startStep("Create User in Status Assigned");
		String userIdOfAssigned = pageHelper.createUSerUsingSP(institutionId, className);
		pageHelper.assignPltTestToStudetInTestEnvironment(userIdOfAssigned, 1, testId, adminId, institutionId, classId);

		report.startStep("Create User in Status Incomplete");
		String userIdOfIncomplete = pageHelper.createUSerUsingSP(institutionId, className);
		pageHelper.assignPltTestToStudetInTestEnvironment(userIdOfIncomplete, 1, testId, adminId, institutionId, classId);
		String userTestAdministrationId = dbService.getUserTestAdministrationByStudentId(userIdOfIncomplete);
		pageHelper.insertRecordToUserTestProgressPLT(userIdOfIncomplete, userTestAdministrationId, "B2");
		dbService.updateCompletedInUserTestProgress(userTestAdministrationId, "1");
		dbService.updateSubmitReasonInUserTestProgress(userTestAdministrationId, "1");
		currentDate = pageHelper.getCurrentDateByFormat("yyyy-MM-dd HH:mm:ss.sss");
		String threeDaysAgo = textService.updateTime(currentDate,"reduce", "day",3);
		dbService.updateStartAndEndDateInUserTestAdministration(userIdOfIncomplete, testId, String.valueOf(testTypeId), threeDaysAgo, threeDaysAgo);
		dbService.updateTestStatusIdByUserTestAdministrationId(userTestAdministrationId, "5");
		
		report.startStep("Create User in Status Expired");
		String userIdOfExpired = pageHelper.createUSerUsingSP(institutionId, className);
		pageHelper.assignPltTestToStudetInTestEnvironment(userIdOfExpired, 1, testId, adminId, institutionId, classId);
		userTestAdministrationId = dbService.getUserTestAdministrationByStudentId(userIdOfExpired);
		//currentDate = pageHelper.getCurrentDateByFormat("yyyy-MM-dd HH:mm:ss.sss");
		String twoDaysAgo = textService.updateTime(currentDate,"reduce", "day",2);
		String yesterday = textService.updateTime(currentDate,"reduce", "day",1);
		dbService.updateStartAndEndDateInUserTestAdministration(userIdOfExpired, testId, String.valueOf(testTypeId), twoDaysAgo, yesterday);
		dbService.updateTestStatusIdByUserTestAdministrationId(userTestAdministrationId, "4");
		
		report.startStep("Create User in Status Done");
		String userIdOfDone = pageHelper.createUSerUsingSP(institutionId, className);
		pageHelper.assignPltTestToStudetInTestEnvironment(userIdOfDone, 1, testId, adminId, institutionId, classId);
		userTestAdministrationId = dbService.getUserTestAdministrationByStudentId(userIdOfDone);
		dbService.updateCompletedInUserTestProgress(userTestAdministrationId, "1");
		dbService.updateSubmitReasonInUserTestProgress(userTestAdministrationId, "1");
		dbService.updateTestStatusIdByUserTestAdministrationId(userTestAdministrationId, "3");
		
		loginPage = new NewUXLoginPage(webDriver, testResultService);
		
		report.startStep("Login as Admin and select course test report");
		String tID = dbService.getUserIdByUserName("admin", institutionId);
		pageHelper.setUserLoginToNull(tID);
		loginPage.loginAsStudent("admin", "12345");
		sleep(3);
		
		TmsReportsPage tmsReportsPage = new TmsReportsPage(webDriver, testResultService);
		
		report.startStep("Select Placement Test Report");
		tmsReportsPage.goToPlacementTestReports();
		
		report.startStep("Select Report Type, Class and Click Go");
		tmsReportsPage.choosePltReportTypeAndClassAndClickGo(reportType, className); 

		report.startStep("Validate Students Appear in Table with Correct Statuses");
		
		report.startStep("Validate Student:"+userIdOfUnassigned+" Appear in Table with Status: Unassigned");
		tmsReportsPage.validateStatusReport(userIdOfUnassigned, "Unassigned", institutionId);
		
		report.startStep("Validate Student:"+userIdOfAssigned+" Appear in Table with Status: Assigned");
		tmsReportsPage.validateStatusReport(userIdOfAssigned, "Assigned", institutionId);
		
		report.startStep("Validate Student:"+userIdOfIncomplete+" Appear in Table with Status: Incomplete");
		tmsReportsPage.validateStatusReport(userIdOfIncomplete, "Incomplete", institutionId);
		
		report.startStep("Validate Student:"+userIdOfExpired+" Appear in Table with Status: Expired");
		tmsReportsPage.validateStatusReport(userIdOfExpired, "Expired", institutionId);
		
		report.startStep("Validate Student:"+userIdOfDone+" Appear in Table with Status: Done");
		tmsReportsPage.validateStatusReport(userIdOfDone, "Done", institutionId);

		// verify start and end date are correct
		//
		
		report.startStep("Logout as Admin");
		tmsHomePage = new TmsHomePage(webDriver, testResultService);
		tmsHomePage.clickOnExit();
		
		////// TC- 81919 //////
		
		report.startStep("Restart Browser");
		institutionName = institutionsName[0];
		pageHelper.restartBrowserInNewURL(institutionName, true);
		
		loginPage = new NewUXLoginPage(webDriver, testResultService);
		
		report.startStep("Login as Admin and select course test report");
		tID = dbService.getUserIdByUserName("admin", institutionId);
		pageHelper.setUserLoginToNull(tID);
		loginPage.loginAsStudent("admin", "12345");
		sleep(3);
				
		report.startStep("Select Placement Test Report");
		tmsReportsPage.goToPlacementTestReports();
		
		tmsReportsPage = new TmsReportsPage(webDriver, testResultService);
		
		report.startStep("Validate '"+reportType+"' is not an option in the Reports List (Old TE)");
		tmsReportsPage.validateReportTypeIsNotInReportList(reportType); // debug
		
		
	}
	
	@Test
	@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "81922" })
	public void testPltStatusReportPrintFunctionality() throws Exception {
		
		report.startStep("Init Data");
		String className = configuration.getProperty("classname.CourseTest"); //dbService.getClassNameByClassId(classId);
		String reportType = "Placement Test Status";
		
		institutionName=institutionsName[1];
		
		report.startStep("Change to Courses Instituion");
		pageHelper.restartBrowserInNewURL(institutionName, true); 
		
		loginPage = new NewUXLoginPage(webDriver, testResultService);
		
		report.startStep("Login as Admin and select course test report");
		String tID = dbService.getUserIdByUserName("admin", institutionId);
		pageHelper.setUserLoginToNull(tID);
		loginPage.loginAsStudent("admin", "12345");
		sleep(3);
		
		TmsReportsPage tmsReportsPage = new TmsReportsPage(webDriver, testResultService);
		
		report.startStep("Select Placement Test Report");
		tmsReportsPage.goToPlacementTestReports();
		
		report.startStep("Select Report Type, Class and Click Go");
		tmsReportsPage.choosePltReportTypeAndClassAndClickGo(reportType, className); 
		
		report.startStep("Click Print");
		TmsHomePage tmsHomePage = new TmsHomePage(webDriver, testResultService);
		tmsHomePage.clickOnPrint();
		
		// validate pop opened
		Alert printDialog = webDriver.getWebDriver().switchTo().alert();
		printDialog.dismiss();

	
	}
	
	// This test performs course test in Old TE, validates course test reports are displayed in Old TE and then changes TE FLAG to New TE
	// and validates course test reports are still displayed in Old TE
	@Test
	//@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "81928" })
	public void testCourseTestReportWithChangeOfTestEnvironment() throws Exception {
		
		try {
			
			String instName = institutionsName[0];
			institutionId = dbService.getInstituteIdByName(instName);
			report.startStep("Turn Off Test Environment FLAG");
			dbService.checkAndturnOffTestEnviormentFlag(institutionId);
			
			TestEnvironmentPage testEnvironmentPage = new TestEnvironmentPage(webDriver,testResultService);
			
			report.startStep("Get default auto student details");
			String className = configuration.getProperty("classname");
			String courseCode = "B1";
			String impType = "2";
			String reportType = "Course Tests Report";
			String wrongAnswerFilePath = "files/CourseTestData/MidTermResumeWrongAnswers.csv";
		//	String ServerPath= PageHelperService.sharePhisicalFolder+"Runtime\\Metadata\\Courses\\";
			JSONObject jsonObj = new JSONObject();
			
			report.startStep("Retrieve Localization Json US");
			JSONObject localizationJson = netService.getJsonObject("smb://"+PageHelperService.physicalPath+"/Runtime/Localization/TestInstructions/", "en_US", false);
			NewUXLoginPage loginPage = new NewUXLoginPage(webDriver,testResultService);
			
			CourseTests testType = CourseTests.MidTerm;
			String answersFilePath = "files/CourseTestData/MidTerm_Answers.csv";
			CourseCodes wantedCourseCode = testEnvironmentPage.getCourseCodeEnumByCourseCodeString(courseCode);	
				
			report.startStep("Login as student");
			homePage = createUserAndLoginNewUXClass(className, institutionId);
			homePage.waitHomePageloaded();
				
			report.startStep("Assign B1 Mid-Term Test to student");
			String courseId = getCourseIdByCourseCode(courseCode);
			String courseName = getCourseNameByCourseCode(courseCode);
			pageHelper.assignAutomatedTestToStudetInTestEnvironment(studentId, courseId, Integer.parseInt(impType),0,0,1);
			sleep(5);
			
			String testId = pageHelper.getAssignedTestIdForStudent(studentId,courseId, Integer.parseInt(impType));
				
			report.startStep("Retrieve JSON Array of Test ID: " + testId);
			jsonObj = netService.getJsonObject("smb://"+PageHelperService.physicalPath+"/Runtime/Metadata/Courses/CourseTests/", testId, false);
				
			report.startStep("Open Assessments Page");
			testsPage = homePage.openAssessmentsPage(false);
				
			report.startStep("Start Test");
			int testSequence = testsPage.getTestSequenceByCourseId(courseId);
			String testName = testsPage.clickStartTest(1, testSequence);
			webDriver.closeAlertByAccept();
			sleep(3);
			
			webDriver.switchToNewWindow();
			NewUxClassicTestPage classicTest = new NewUxClassicTestPage(webDriver, testResultService);
			classicTest.switchToTestAreaIframe();
			sleep(1);
			classicTest.pressOnStartTest();
			sleep(3);
			
			testEnvironmentPage.answerQuestionsBySectionsOldTe(answersFilePath, testId +"#", wantedCourseCode, testType, 1);
			
			report.startStep("Answer Incorrectly Section 2");
			testEnvironmentPage.answerSpecificSectionQuestionsOldTe(answersFilePath, testId +"#", wantedCourseCode, testType, 1);

			report.startStep("Do not Answer Sections 3,4 (skip)");
			int sectionsToSubmit = 2; // dont answer section 3,4
			for (int i = 0; i < sectionsToSubmit ; i++) {
				classicTest.browseToLastSectionTask();
				classicTest.pressOnSubmitSection(true);
			}
			
		report.startStep("Answer Incorrectly Section 5");
			testEnvironmentPage.answerSpecificSectionQuestionsOldTe(answersFilePath, testId +"#", wantedCourseCode, testType, 4);
			
		report.startStep("Get score and close test");
			classicTest.switchToCompletionMessageFrame();
			sleep(1);
			String finalScore = classicTest.getFinalScore();
			webDriver.switchToTopMostFrame();
			classicTest.switchToTestAreaIframe();
			classicTest.closeCompletionMessageAlert();
			sleep(1);
					
			report.startStep("Logout");
			homePage.clickOnLogOut();
			testResultService.assertEquals(false, loginPage.isSubmitButtonEnabled(), "Checking that ED Login Page displayed");
			
			report.startStep("Turn On Test Environment FLAG");
			dbService.checkAndturnOnTestEnviormentFlag(institutionId);
			
			TmsReportsPage tmsReportsPage = new TmsReportsPage(webDriver, testResultService);
		
			report.startStep("Login as teacher and select course test report");
			String tID = dbService.getUserIdByUserName("admin", institutionId);
			pageHelper.setUserLoginToNull(tID);
			loginPage.loginAsStudent("admin", "12345");
			sleep(3);
				
			report.startStep("Select Course Test Report");
			tmsReportsPage.goToCourseReports();
			tmsReportsPage.selectCourseReportAndClassAndCourse(reportType, className, courseId);
			sleep(3);
			
			report.startStep("Validate Score and Click it");	
			userName = dbService.getUserNameById(studentId, institutionId);
			TmsHomePage tmsHomePage = new TmsHomePage(webDriver, testResultService);
		    String tmsScore = tmsHomePage.pressOnMidTermTestScore(userName);
			testResultService.assertEquals(finalScore, tmsScore, "Final Score in TMS does not match to test result in Assessments page");
			sleep(3);
				
			if (!tmsScore.equals(" ")) {
				
				report.startStep("Switch to Course Report Page and check section titles");
					webDriver.switchToNewWindow();
				
				report.startStep("Validate Results");
					tmsCTR = new TmsCourseTestReport(webDriver, testResultService);
					tmsCTR.validateResultsOldTe(studentId, testId, userName, wrongAnswerFilePath);
			}
			
			report.startStep("Quit Browser");
			webDriver.quitBrowser(); 
		} catch (Exception e) {
			
		} finally {
			
			report.startStep("Turn Off Test Environment FLAG for InstitutionId: " + institutionId);
			dbService.checkAndturnOffTestEnviormentFlag(institutionId);
		}
	}
	
	@Test
	@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "81923" })
	public void testPltStatusReportExport() throws Exception {
		
		institutionName=institutionsName[1];
		
		report.startStep("Change to Courses Instituion");
		pageHelper.restartBrowserInNewURL(institutionName, true); 
		
		////// TC- 81918 //////
		report.startStep("Init Data");
		//int testTypeId = 1;
		//String currentDate;
		String className = configuration.getProperty("classname.CourseTest"); //dbService.getClassNameByClassId(classId);
		String reportType = "Placement Test Status";
		//String adminId = dbService.getAdminIdByInstId(institutionId);
		//String classId = dbService.getClassIdByName(className, institutionId);
		//String testId = "11";
		String windowsUser = pageHelper.getWindowsUserName();
		
		pageHelper.runConsoleProgram("cmd /c del C:\\Users\\"+windowsUser+"\\Downloads\\1033*.*");
		
		String exportedDataFilePath = "C:\\\\Users\\"+windowsUser+"\\Downloads\\1033.csv";
		
		loginPage = new NewUXLoginPage(webDriver, testResultService);
		
		report.startStep("Login as Admin and select course test report");
		String tID = dbService.getUserIdByUserName("admin", institutionId);
		pageHelper.setUserLoginToNull(tID);
		loginPage.loginAsStudent("admin", "12345");
		sleep(3);
		
		TmsReportsPage tmsReportsPage = new TmsReportsPage(webDriver, testResultService);
		
		report.startStep("Select Placement Test Report");
		tmsReportsPage.goToPlacementTestReports();
		
		report.startStep("Select Report Type, Class and Click Go");
		tmsReportsPage.choosePltReportTypeAndClassAndClickGo(reportType, className); 

		report.startStep("Click Export");
		TmsHomePage tmsHomePage = new TmsHomePage(webDriver, testResultService);
		tmsHomePage.clickOnExport();
		
		pageHelper.runConsoleProgram("cmd /c cd / && powershell -command \"Get-Content C:\\Users\\"+windowsUser+"\\Downloads\\1033.csv\" > C:Users\\"+windowsUser+"\\Downloads\\1033-utf8.csv");
		
		report.startStep("Read Data from Exported Excel");
		List<String[]> exportedData = textService.getStr2dimArrFromCsv(exportedDataFilePath.split(".csv")[0] + "-utf8.csv");
		
		report.startStep("Validate First Names are Exported Correctly");
		List<String> studentsFirstNames = tmsReportsPage.getColumnListFromExcel(exportedData, "First Name", reportType);
		
		TmsAssessmentsTestsAssignmentPage tmsAssessmentsAutomatedTestsPage = new TmsAssessmentsTestsAssignmentPage(webDriver, testResultService);

		//webDriver.switchToFrame("PLTStatusReport");
		
		List<String> firstNamesWeb = tmsAssessmentsAutomatedTestsPage.getListFromPltStatusReportTableByCoulmn("First Name");
		
		testResultService.assertEquals(true, studentsFirstNames.equals(firstNamesWeb), "First Names in excel file don't match first names in web. From Excel: " + studentsFirstNames.toString() +".\nFrom Web: " + firstNamesWeb.toString());
		
		report.startStep("Validate Last Names are Exported Correctly");
		List<String> studentsLastNames = tmsReportsPage.getColumnListFromExcel(exportedData, "Last Name", reportType);
		List<String> lastNamesWeb = tmsAssessmentsAutomatedTestsPage.getListFromPltStatusReportTableByCoulmn("Last Name");
		testResultService.assertEquals(true, studentsLastNames.equals(lastNamesWeb), "Last Names in excel file don't match last names in web. From Excel: " + studentsLastNames.toString() +".\nFrom Web: " + lastNamesWeb.toString());

		report.startStep("Validate User Names are Exported Correctly");
		List<String> userNames = tmsReportsPage.getColumnListFromExcel(exportedData, "User Name", reportType);
		List<String> userNamesWeb = tmsAssessmentsAutomatedTestsPage.getListFromPltStatusReportTableByCoulmn("User Name");
		testResultService.assertEquals(true, userNames.equals(userNamesWeb), "Users Names in excel file don't match users names in web. From Excel: " + userNames.toString() +".\nFrom Web: " + userNamesWeb.toString());

		/*report.startStep("Validate Start Dates are Exported Correctly");
		List<String> startDates = tmsReportsPage.getStudentsFirstNamesFromExcel(exportedData, "Start Date",reportType);
		List<String> startDatesWeb = tmsAssessmentsAutomatedTestsPage.getListFromTableByCoulmn("Start Date");
		testResultService.assertEquals(true, startDates.equals(startDatesWeb), "Start Dates in excel file don't match start dates in web. From Excel: " + startDates.toString() +".\nFrom Web: " + startDatesWeb.toString());

		report.startStep("Validate End Dates are Exported Correctly");
		List<String> endDates = tmsReportsPage.getStudentsFirstNamesFromExcel(exportedData, "End Date", reportType);
		List<String> endDatesWeb = tmsAssessmentsAutomatedTestsPage.getListFromTableByCoulmn("End Date");
		testResultService.assertEquals(true, endDates.equals(endDatesWeb), "End Dates in excel file don't match start dates in web. From Excel: " + endDates.toString() +".\nFrom Web: " + endDatesWeb.toString());
		 */
		
		report.startStep("Validate Test Statuses are Exported Correctly");
		List<String> testStatuses = tmsReportsPage.getColumnListFromExcel(exportedData, "Test Status", reportType);
		List<String> testStatusesWeb = tmsAssessmentsAutomatedTestsPage.getListFromPltStatusReportTableByCoulmn("Status");
		testResultService.assertEquals(true, testStatuses.equals(testStatusesWeb), "Test statuses in excel file don't match test statuses in web. From Excel: " + testStatuses.toString() +".\nFrom Web: " + testStatusesWeb.toString());

	}
	
	@Test
	@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "" })
	public void testExportCourseTestReport() throws Exception {
		
		institutionName=institutionsName[1];
		
		report.startStep("Change to Courses Instituion");
		pageHelper.restartBrowserInNewURL(institutionName, true); 
		
		String windowsUser ="shiraqt";// pageHelper.getWindowsUserName();
		String reportType = "Course Tests Report";
		String className = configuration.getProperty("classname.CourseTest");
		String courseCode = "B1";
		String courseId = getCourseIdByCourseCode(courseCode);
		
		pageHelper.runConsoleProgram("cmd /c del C:\\Users\\"+windowsUser+"\\Downloads\\1033*.*");
		
		String exportedDataFilePath = "C:\\\\Users\\"+windowsUser+"\\Downloads\\1033.csv";
		report.startStep("path:" + exportedDataFilePath +" username: " + windowsUser);
		
		loginPage = new NewUXLoginPage(webDriver, testResultService);
		
		report.startStep("Login as Admin and select course test report");
		String tID = dbService.getUserIdByUserName("admin", institutionId);
		pageHelper.setUserLoginToNull(tID);
		loginPage.loginAsStudent("admin", "12345");
		sleep(3);
		
		TmsReportsPage tmsReportsPage = new TmsReportsPage(webDriver, testResultService);
		
		report.startStep("Select Course Test Report");
		tmsReportsPage.goToCourseReports();
		tmsReportsPage.selectCourseReportAndClassAndCourse(reportType, className, courseId);
		
		report.startStep("Click Export");
		TmsHomePage tmsHomePage = new TmsHomePage(webDriver, testResultService);
		tmsHomePage.clickOnExport();
		
		pageHelper.runConsoleProgram("cmd /c cd / && powershell -command \"Get-Content C:\\Users\\"+windowsUser+"\\Downloads\\1033.csv\" > C:Users\\"+windowsUser+"\\Downloads\\1033-utf8.csv");

		report.startStep("Read Data from Exported Excel");
		List<String[]> exportedData = textService.getStr2dimArrFromCsv(exportedDataFilePath.split(".csv")[0] + "-utf8.csv");
		
		report.startStep("Validate First Names are Exported Correctly");
		List<String> studentsFirstNames = tmsReportsPage.getColumnListFromExcel(exportedData, "First Name", reportType);
		
		TmsAssessmentsTestsAssignmentPage tmsAssessmentsAutomatedTestsPage = new TmsAssessmentsTestsAssignmentPage(webDriver, testResultService);
		
		tmsCTR = new TmsCourseTestReport(webDriver, testResultService);
		
		List<String> studentsNames = tmsCTR.getStudentsNamesFromCourseTestReportTable(); //tmsAssessmentsAutomatedTestsPage.getListFromTableByCoulmn("First Name");
		List<String> firstNamesWeb = tmsCTR.getStudentsFirstNamesFromStudentsNamesList(studentsNames);
		List<String> lastNamesWeb = tmsCTR.getStudentsLastNamesFromStudentsNamesList(studentsNames);
		
		testResultService.assertEquals(true, studentsFirstNames.equals(firstNamesWeb), "First Names in excel file don't match first names in web. From Excel: " + studentsFirstNames.toString() +".\nFrom Web: " + firstNamesWeb.toString());
		
		report.startStep("Validate Last Names are Exported Correctly");
		List<String> studentsLastNames = tmsReportsPage.getColumnListFromExcel(exportedData, "Last Name", reportType);
		testResultService.assertEquals(true, studentsLastNames.equals(lastNamesWeb), "Last Names in excel file don't match Last names in web. From Excel: " + studentsLastNames.toString() +".\nFrom Web: " + lastNamesWeb.toString());

		report.startStep("Validate Mid term Test final grade are Exported Correctly");
		List<String> midTermFinalGrade = tmsReportsPage.getColumnListFromExcel(exportedData, "Midterm Test Final Grade", reportType);
		List<String> midTermFinalGradesWeb = tmsCTR.getMidTermFinalGradesFromCourseTestReportTable();
		testResultService.assertEquals(true, midTermFinalGrade.equals(midTermFinalGradesWeb), "Mid Term Final Grades in excel file don't match Mid Term Final Grades in web. From Excel: " + midTermFinalGrade.toString() +".\nFrom Web: " + midTermFinalGradesWeb.toString());
	
		report.startStep("Validate Course Test final grade are Exported Correctly");
		List<String> courseTestFinalGrade = tmsReportsPage.getColumnListFromExcel(exportedData, "Course Test Final Grade", reportType);
		List<String> courseTestFinalGradesWeb = tmsCTR.getCourseTestFinalGradesFromCourseTestReportTable();
		testResultService.assertEquals(true, courseTestFinalGrade.equals(courseTestFinalGradesWeb), "Course Test Final Grades in excel file don't match Course Test Final Grades in web. From Excel: " + courseTestFinalGrade.toString() +".\nFrom Web: " + courseTestFinalGradesWeb.toString());

	
	
	}
	
	
	@Test
	@Category(SanityTests.class)
	@TestCaseParams(testCaseID = { "" })
	public void testReachingReportsInTMS() throws Exception{
		
		report.startStep("Prepare test-data");
			String courseCode = "B1";
			String className = configuration.getProperty("classname.progress");
			String reportType = "Course Tests Report";
			String courseId = getCourseIdByCourseCode(courseCode);
		
		report.startStep("Login as admin");
			loginPage = new NewUXLoginPage(webDriver, testResultService);
			pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(configuration.getProperty("schoolAdmin.user"),institutionId));
			tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
			homePage.closeAllNotifications();
			tmsHomePage.waitForPageToLoad();
		
		report.startStep("Navigate to course report page");
			TmsReportsPage tmsReportsPage = new TmsReportsPage(webDriver, testResultService);
			tmsReportsPage.goToCourseReports();
			sleep(3);
		
		report.startStep("Select report and class");
			tmsReportsPage.selectCourseReportAndClassAndCourse(reportType, className, courseId);
			sleep(2);
		
		report.startStep("Verify if there a students with course report");
			List<WebElement> studentsNames = null;
			for(int i = 0;i<5&&studentsNames==null;i++) {
				studentsNames = webDriver.getWebDriver().findElements(By.xpath("//td[@class='tdt']//div"));
			}
			textService.assertNotNull("Frame not downloaded or no students in the class", studentsNames);
		
		
		report.startStep("");
			tmsHomePage.clickOnExit();
		//sleep(1);
		//tmsHomePage.selectReport("9"); // 9 it's end of course Matrix
		//sleep(5);
		

	}
	
	
	private void clickOnSave(int index) throws Exception {
		webDriver.waitForElement("baseList_ctrl"+index+"_btnUpdateItemEdit", ByTypes.id).click();
	}



	private void clickOnEditIcon(int index) throws Exception {
		//webDriver.waitForElement("baseList_ctrl0_btnItemEdit", ByTypes.id).click();
		   
		webDriver.waitForElement("baseList_ctrl"+index+"_btnItemEdit", ByTypes.id).click();
	}



	private void logoutFromTmsAndCheckEdTabsClosed() throws Exception {
		webDriver.switchToMainWindow();
		tmsHomePage.switchToMainFrame();
		tmsHomePage.clickOnExit();
		sleep(2);
		testResultService.assertEquals(false, loginPage.isSubmitButtonEnabled(), "Checking that Login Page displayed");
		webDriver.checkNumberOfTabsOpened(1);
	}
	
	private void verifyMidTermTestWidgetDisplayed(String score, String courseName) throws Exception {
		sleep(3);
		homePage.navigateToRequiredCourseOnHomePage(courseName);
		sleep(1);
		myProgress = homePage.clickOnMyProgress();
		sleep(1);
		testResultService.assertEquals(courseName, myProgress.getCourseName(), "Course Name is wrong");
		boolean isMidTermWidgetDisplayed = myProgress.isMidTermWidgetDisplayed();
		testResultService.assertEquals(true, isMidTermWidgetDisplayed, "Midterm test widget not displayed", true);

		if (isMidTermWidgetDisplayed) {
			testResultService.assertEquals(score, myProgress.getMidTermScore(), "Midterm test score not valid");
		}
		homePage.clickToOpenNavigationBar();
		homePage.clickOnHomeButton();
		
	}
	
	private void openNextTestAndEnterReview(String unitName, String lessonName, String unitId, String CourseId, Boolean changeCourse) throws Exception {
		webDriver.switchToMainWindow();
		sleep(1);
		webDriver.switchToTopMostFrame();
		tmsHomePage.switchToMainFrame();
		
		if (changeCourse)			
			tmsHomePage.selectCourse(CourseId);
		else
			webDriver.waitForElement(unitId + "check", ByTypes.id).click();
		
		report.report("CourseId: " + CourseId + " Unit Name:" + unitName + "Lesson Name:" + lessonName);
		tmsHomePage.openUnitReportAndPressOnTestScore(unitName, lessonName);
		webDriver.switchToNewWindow();
		tmsCTR.switchToFirstComponentTestResultFrame();
		learningArea2.clickOnReviewTestResults();
	}
	
	private void waitForMatrixLoadingToEnd (int initSleepInterval , int sleepBetweenCycles, int cycleNumber) throws Exception {
		sleep(initSleepInterval);
		for (int i = 0 ; i < cycleNumber ; i++) {
			String loadingState = webDriver.findElementByXpath("//*[@id='updateProgress']",ByTypes.xpath).getAttribute("Style");
			if (loadingState.equals("display: none;")) {
				report.report("Loading took less than " + (initSleepInterval + sleepBetweenCycles*i) + " seconds");
				break;
			}
			report.report("Loading took longer than " + (initSleepInterval + sleepBetweenCycles*i) + " seconds");
			sleep(sleepBetweenCycles);
	}
	}
	
	public void openAssessmentsAndStartTest () throws Exception {
	testsPage = homePage.openAssessmentsPage(false);
	//sleep(2);
	classicTest = testsPage.clickOnStartTest("1", "2");
	sleep(1);
	webDriver.closeAlertByAccept();
	sleep(3);
	webDriver.switchToNewWindow();
	classicTest.switchToTestAreaIframe();
	sleep(1);
	classicTest.pressOnStartTest();
	sleep(3);
	}
	
	public void  SubmitFirstSectionWithCorrectAnswers () throws Exception{
		String [] section1_CorrectAns = {"at the Mozart Theater.","False","a rock star.","True","False"};
		int questionsInSection = 5;
			for (int i = 0; i < questionsInSection; i++) {
				classicTest.selectAnswerByTextMCQ(section1_CorrectAns[i]);
				sleep(1);
				classicTest.pressOnNextTaskArrow();
			}
			classicTest.pressOnSubmitSection(true);
			classicTest.close();
	}
	
	public void selectCourseTestReport () throws Exception{
		tmsHomePage = new TmsHomePage(webDriver, testResultService);
		tmsHomePage.switchToMainFrame();
		tmsHomePage.clickOnReports();
		sleep(2);
		tmsHomePage.clickOnCourseReports();
		sleep(4);
		tmsHomePage.switchToFormFrame();
		tmsHomePage.selectReport("12");
		sleep(1);
	}
	
	public String ReturnTestDate (String studentId, String courseid,String testId, int testTypeId) throws Exception{

		String info[] = dbService.getStudentFinalTestData(studentId,courseid,testId,testTypeId);
		String testDate = info[0];
		testDate = testDate.substring(0, testDate.indexOf(" "));
	
		return testDate;
		
	}
	
	@After
	public void tearDown() throws Exception {
		if (matrixDBCleanup) {
			//instID = configuration.getProperty("institution.id");
			studentId = dbService.getUserIdByUserName("stForAssessment", institutionId);
			dbService.removeMatrixManualGradesForStudent(studentId);
			if (matrixAdminCleanup)
				dbService.setMatrixUserLockState(studentId, dbService.getUserIdByUserName(configuration.getProperty("schoolAdmin.user"),institutionId), "0", courses[5]);
			else
				dbService.setMatrixUserLockState(studentId, dbService.getUserIdByUserName(configuration.getProperty("teacher.username"),institutionId), "0", courses[5]);
		}
		
		if (deleteUser){
			classId = dbService.getClassIdByName(configuration.getProperty("classname"),institutionId);
			dbService.deleteUserIdByClassId(classId,studentId);
		}
		super.tearDown();
		
	}
	
	
}
