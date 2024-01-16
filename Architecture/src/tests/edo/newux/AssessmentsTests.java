package tests.edo.newux;

import Enums.ByTypes;
import Enums.CourseCodes;
import Enums.CourseTests;
import Enums.PLTStartLevel;
import Interfaces.TestCaseParams;
import Objects.CourseTest;
import Objects.PLTCycle;
import Objects.PLTTest;
import drivers.GenericWebDriver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.WebElement;
import pageObjects.edo.*;
import pageObjects.tms.TmsHomePage;
import services.TestResultService;
import testCategories.AngularLearningArea;
import testCategories.edoNewUX.ReleaseTests;
import testCategories.inProgressTests;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;


//@Category(AssessmentsTests.class)
//@Category(reg2.class)
public class AssessmentsTests extends BasicNewUxTest {

	private static final String[] ResultScore2 = null;
	NewUxLearningArea learningArea;
	NewUxAssessmentsPage testsPage;
	NewUxClassicTestPage classicTest;
	NewUxMyProgressPage myProgress;
	NewUxLearningArea2 learningArea2;
	TmsHomePage tmsHomePage;
	NewUXLoginPage loginPage;

	String userName = "";
	String expectedUrl = "";
	public static boolean useCEFR = false;

	boolean bTestFailed = false;


	public void NewUxClassicTestPage(GenericWebDriver webDriver, TestResultService testResultService) throws Exception {
		// TODO Auto-generated constructor stub
	}

	public String[] getTestRemainingTimeByStudent(String studentId, int testTypeId)
			throws Exception {


		Date date = new Date();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		String endDateFromDB = dbService.getStudentAssignedTestEndDate(studentId, testTypeId);
		endDateFromDB = endDateFromDB.substring(0, endDateFromDB.indexOf("."));
		Date endDate = df.parse(endDateFromDB);

		long duration = endDate.getTime() - date.getTime();

		long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(duration);
		long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
		long diffInHours = TimeUnit.MILLISECONDS.toHours(duration);
		long diffInDays = TimeUnit.MILLISECONDS.toDays(duration);

		String days = String.format("%1d", (duration / (1000 * 60 * 60 * 24)));
		String hh = String.format("%02d", (duration / (1000 * 60 * 60)) % 24);
		String mm = String.format("%02d", (duration / (1000 * 60)) % 60);
		String ss = String.format("%02d", (duration / 1000) % 60);

		String[] counterValues = new String[]{days, hh, mm, ss};

		return counterValues;

	}

	@Before
	public void setup() throws Exception {
		super.setup();
		report.startStep("Create user and login");
		homePage = createUserAndLoginNewUXClass();
		learningArea = new NewUxLearningArea(webDriver, testResultService);
		learningArea2 = new NewUxLearningArea2(webDriver, testResultService);
		homePage.closeAllNotifications();
		homePage.waitHomePageloadedFully();
		pageHelper.assignPltToInstitution(institutionId);
	}

	//Auto submit test plus time cheking

	@Test
	@TestCaseParams(testCaseID = {"50964", "51166", "51176", "51181"})

	public void testResumeTestTimeCheking() throws Exception {

		report.startStep("Init test data");
		String courseCode = "B1";
		String CompletedTest = "0";
		String courseId = getCourseIdByCourseCode(courseCode);


		report.startStep("Assign B1 Mid-Term Test to student");
		courseId = getCourseIdByCourseCode(courseCode);
		pageHelper.assignAutomatedTestToStudetInTestEnvironment(studentId, courseId, 2, 0, 0, 1);

		String testId = pageHelper.getAssignedTestIdForStudent(studentId, courseId, 2);

		report.startStep("Return Test Id");
		//testId = pageHelper.getAssignedTestIdForStudent(studentId,courseId, 2);
		String[] oldTestIds = {"989013148", "989012509"};
		int randomNum = dbService.getRandonNumber(0, oldTestIds.length - 1);

		if (testId.equals("989017755") || testId.equals("989022835")) {
			// update test id
			String userExitSettingsId = dbService.getUserExitSettingsId(studentId, testId, courseId, "2");
			dbService.updateTestIdInUserExitSettings(oldTestIds[randomNum], userExitSettingsId);
			testId = pageHelper.getAssignedTestIdForStudent(studentId, courseId, 2);
			String token = pageHelper.getTokenFromConsoleWithParameters("1", studentId, testId, userExitSettingsId);
			dbService.updateAssignWithToken(token, userExitSettingsId);
		}


		//sleep(2);
		int sectionsToSubmit = Integer.parseInt(dbService.getNumberOfSectionsInTestForCourseByTestId(courseId, testId, 2));

		report.startStep("Start test and submit first section with correct answer and check test button alert");
		testsPage = homePage.openAssessmentsPage(false);
		//sleep(2);

		int testSequence = testsPage.getTestSequenceByCourseId(courseId);
		if (testSequence != 2) {
			testResultService.addFailTest("Tests sequnce is Incorrect. Excpected: 1. Placement test, 2. course test. Actual: 1. course test, 2. Placement test", false, true);
		}
		classicTest = testsPage.clickOnStartTest("1", String.valueOf(testSequence));
		webDriver.closeAlertByAccept();
		sleep(2);
		webDriver.switchToNewWindow();
		classicTest.switchToTestAreaIframe();
		classicTest.pressOnStartTest();
		SubmitFirstSectionWithCorrectAnswers(1, testId);
		String CTCompletedDB = dbService.getCTCompleted(testId, studentId);
		testResultService.assertEquals(CompletedTest, CTCompletedDB, "Wrong CTCompleted state");
		dbService.SetTestTime(studentId, 3);

		report.startStep("Submit test on resume mode and verify remaining time");
		testsPage = homePage.openAssessmentsPage(false);
		//sleep(5);
		//getTestRemainingTimeByStudent();
		getTestRemainingTimeByStudentAndCourseId(courseId);
		testSequence = testsPage.getTestSequenceByCourseId(courseId);

		if (testSequence != 2) {
			testResultService.addFailTest("Tests sequnce is Incorrect. Excpected: 1. Placement test, 2. course test. Actual: 1. course test, 2. Placement test", false, true);
		}

		classicTest = testsPage.clickOnStartTest("1", String.valueOf(testSequence));
		sleep(2);
		webDriver.switchToNewWindow();
		classicTest.switchToTestAreaIframe();
		String[] counterValues = getTestRemainingTimeByStudent(studentId, 2);
		testResultService.equals(counterValues);

		for (int i = 0; i < sectionsToSubmit - 1; i++) {
			classicTest.browseToLastSectionTask();
			classicTest.pressOnSubmitSection(true);
			report.startStep("Submit test section: " + (i + 2));
		}

		CTCompletedDB = dbService.getCTCompleted(testId, studentId);
		testResultService.assertEquals("1", CTCompletedDB, "Wrong CTCompleted state");
	}

	//US 56039

	@Test
	@TestCaseParams(testCaseID = {"56332"})
	public void testLimitTestExpirationDisplay() throws Exception {

		//try {
		report.startStep("Init test data");
		String courseCode = "B1";
		String courseId = getCourseIdByCourseCode(courseCode);

		report.startStep("Assign B1 Mid-Term Test to student ");
		courseId = getCourseIdByCourseCode(courseCode);
		pageHelper.assignAutomatedTestToStudetInTestEnvironment(studentId, courseId, 2, 0, 0, 3);
		sleep(3);

		report.startStep("Check time limit");
		testsPage = homePage.openAssessmentsPage(false);

		report.startStep("Check if the elements display time in days, minutes, and seconds");
		NewUxAssessmentsPage newUxAssessmentsPage = new NewUxAssessmentsPage(webDriver, testResultService);
		testResultService.assertTrue("the timer is not present", newUxAssessmentsPage.checkTimeDisplay());
		testsPage.close();
		sleep(2);
		homePage.logOutOfED();
//		String timer = webDriver.findElementByXpath("//*[@id='mainCtrl']/div[3]/div/div/div[2]/div[2]/div/div/div/div[1]/table/tbody/tr[2]/td[3]",ByTypes.xpath).getAttribute("Style");
//		if (timer.equals("display: none;")){
//			report.report("Timer of tests enable");
//		}
//		else {
//			report.report("Timer of tests disable");
//		}
//		//sleep(8);
//
//		int testSequence = testsPage.getTestSequenceByCourseId(courseId);
//		if (testSequence != 2) {
//			testResultService.addFailTest("Tests sequnce is Incorrect. Excpected: 1. Placement test, 2. course test. Actual: 1. course test, 2. Placement test", false, true);
//		}
//		//String testSequence = "2";
//		WebElement hoursElement = webDriver.getWebDriver().findElement(By.xpath("//div[contains(@class,'assessments__main')]/div[1]//table[contains(@class,'assessments__table')]//tr["+testSequence+"]//div[contains(@class,'timeCellHours')]"));
//		int counter = 0;
//		while (hoursElement.getAttribute("textContent").equals("00") && counter < 100) {
//			sleep(1);
//			counter++;
//		}
//		//webDriver.waitUntilElementAppears(daysElement, 30);
//		getTestRemainingTimeByStudent();
//		testsPage.close();
//		sleep(2);
//		homePage.logOutOfED();
//	}catch (Exception e) {
//		testResultService.addFailTest(e.getMessage(), true, true);
//	}catch (AssertionError e) {
//		testResultService.addFailTest(e.getMessage(), true, true);
		}




	
	/// Resume test with out check time
	@Test
	//@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "50964", "51166", "51176", "51181" })
	public void testResumeMidTermTest () throws Exception {
		
			bTestFailed = false;  
			
		try {
		
			report.startStep("Init test data");
			String courseCode = courseCodes[1]; //"B1";
			String CompletedTest="0";
			String LastVisitedItem = "";
			String courseId = courses[1]; //getCourseIdByCourseCode(courseCode);
			int testTypeId = 2;
				
			report.startStep("Logout and Login with: " + studentId + " UserId.");	
			homePage.logOutOfED();
			//webDriver.deleteCookiesAndCache();
			//webDriver.clearLocalAndSessionStorage();
			//pageHelper.runJavaScriptCommand("localStorage.clear();");
			//pageHelper.runJavaScriptCommand("sessionStorage.clear();");
				
			String userName = configuration.getStudentUserName();
			String password = configuration.getStudentPassword();
			
			studentId = dbService.getUserIdByUserName(userName, institutionId);
	
			//	studentId = "52322820373057";//
		//	userName = "Cer3";
		//	password ="12345";
			//className = "classNewUx";
			//institutionid = institutionId;
			//studentId = dbService.getUserIdByUserName(userName, institutionId);
		
			report.startStep("Assign " + courseCode + " Mid-Term Test to student");
			pageHelper.assignAutomatedTestToStudetInTestEnvironment(studentId, courseId, testTypeId,0,0,1);
			sleep(3);
			String testId = pageHelper.getAssignedTestIdForStudent(studentId,courseId, testTypeId);
			
			report.startStep("Update Test Id");
			String[] oldTestIds = {"989013148", "989012509"};
			int randomNum = dbService.getRandonNumber(0, oldTestIds.length-1);
			String wantedTestId = "989012509";
				
			if (testId.equals("989017755") || testId.equals("989022835")) {
				String userExitSettingsId = dbService.getUserExitSettingsId(studentId, testId, courseId, "2");
				dbService.updateTestIdInUserExitSettings(wantedTestId, userExitSettingsId);
				testId = pageHelper.getAssignedTestIdForStudent(studentId,courseId, 2);
				String token = pageHelper.getTokenFromConsoleWithParameters("1", studentId, testId, userExitSettingsId);
				dbService.updateAssignWithToken(token, userExitSettingsId);
			}
			
			report.startStep("Restart Browser");
			String url = webDriver.getUrl();
			pageHelper.closeBrowserAndOpenInCognito(false);
			webDriver.openUrl(url);
			
			report.startStep("Login with: " + userName );
			NewUXLoginPage loginPage = new NewUXLoginPage(webDriver,testResultService);
			homePage = loginPage.loginAsStudent(userName, password);
			
			if (testId.equalsIgnoreCase("989013148")) 
					LastVisitedItem = "989013163";
			
			else if (testId.equalsIgnoreCase("989012509"))
					LastVisitedItem= "989012603";
			
			int sectionsToSubmit = Integer.parseInt(dbService.getNumberOfSectionsInTestForCourseByTestId(courseId,testId,testTypeId));
	
			homePage.waitHomePageloaded();
			pageHelper.closeLastSessionImproperLogoutAlert();
			sleep(2);
	
//			report.startStep("Verify Test alert count is correct");
//			verifyTestNumberInAlert(2);
						
			report.startStep("Open Assessment Page and verify Tests Count");
			testsPage = homePage.openAssessmentsPage(false);
			getTestRemainingTimeByStudent();
			verifyTestNumberInAlert(2);
			
			report.startStep("Start test and submit first section with correct answer and check test button alert");
			classicTest = testsPage.clickOnStartTest("1", "2");
			webDriver.closeAlertByAccept();
			sleep(2);
			webDriver.switchToNewWindow();
			classicTest.switchToTestAreaIframe();
			classicTest.pressOnStartTest();
			SubmitFirstSectionWithCorrectAnswers(1,testId);
			
			String CTCompletedDB = dbService.getCTCompleted(testId, studentId);
			testResultService.assertEquals(CompletedTest, CTCompletedDB, "Wrong CTCompleted state");
			
			report.startStep("Submit test on resume mode and verify remaining time");
			testsPage = homePage.openAssessmentsPage(false);
			classicTest = testsPage.clickOnStartTest("1", "2");
			sleep(2);
			webDriver.switchToNewWindow();
			classicTest.switchToTestAreaIframe();
			String LastVisitedItemID = dbService.CTLastVisitedItemID(testId,studentId, testTypeId);
			testResultService.assertEquals(LastVisitedItem, LastVisitedItemID, "Wrong LastVisitedItemID state");
	
			for (int i = 0; i < sectionsToSubmit-1 ; i++) {
				classicTest.browseToLastSectionTask();
				classicTest.pressOnSubmitSection(true);
				report.startStep("Submited section: " + (i+2));
			}
		
			report.startStep("Get score and close test");
			classicTest.switchToCompletionMessageFrame();
			sleep(1);
			String finalScore = classicTest.getFinalScore();
			webDriver.switchToTopMostFrame();
			classicTest.switchToTestAreaIframe();
			classicTest.closeCompletionMessageAlert();
			sleep(1);
				
			report.startStep("Open Assessments and verify B1 Mid-Term Test displayed in Tests Results section");
			homePage.openAssessmentsPage(false);
			CTCompletedDB = dbService.getCTCompleted(testId, studentId);
			CompletedTest="1";
			testResultService.assertEquals(CompletedTest, CTCompletedDB, "Wrong CTCompleted state");
			testsPage.clickOnArrowToOpenSection("3");
			String testName = coursesNames[1] + " " + pageHelper.getImplementationTypeName(2,"ED");
			testsPage.checkTestDisplayedInSectionByTestName(testName, "3", "1");
			
			report.startStep("Get Date of Submission");
	     	testsPage.checkSubmissionDateForTests("1");
		
	     	report.startStep("Check Score in Test Results");
			String resultScore = testsPage.checkScoreByTest("1", finalScore);
			testsPage.close();
			
			report.startStep("Get and Check score Assessment page VS. DB ");
			String dbResultScore = dbService.getStudentCourseTestScore(studentId, courseId, testId, testTypeId);
			String DbScore[] = dbResultScore.split("\\.");;
			
			testResultService.assertEquals(resultScore, DbScore[0],"ED Test result score not match to DB test report score");
			
			report.startStep("Logout as student");
			homePage.clickOnLogOut();
		} catch(Exception e) {
			bTestFailed = true;
			report.reportFailure(e.getMessage());
		}
	}
	
	
	private void closeBrowserAndOpenAgain() throws Exception {
			webDriver.quitBrowser();
			//sleep(2);
			webDriver.openIncognitoChromeWindow();
			webDriver.init();
			sleep(2);
			webDriver.maximize();
			sleep(2);
			pageHelper.openCILatestUXLink();
	}

	// Did test PLT 
	@Test
	@TestCaseParams(testCaseID = { "50743" })
	public void testAssessmentsPagePltDidTestAndShowPLT() throws Exception {
		
		//String instID = institutionId; //configuration.getProperty("institution.id"); //"5232282";
		int iteration = 1;
	
		
	//report.startStep("Check Test Button Alert");
		//verifyTestNumberInAlert(1);			
		
	//report.startStep("Verify the Humbureger Alert doesn't display when the Navigation bar is open");
		//homePage.getHumburgerAlert(false);
		
	//report.startStep("Verify the Humbureger Alert display when the Navigation is close");
		//homePage.clickToOpenNavigationBar();
		//homePage.getHumburgerAlert(true);
		//homePage.clickToOpenNavigationBar();
		
		/* the aboves lines tested by another test, so they can be under comment to reduce the running time */
		
	report.startStep("Open Assessments");
		testsPage = homePage.openAssessmentsPage(false);
		//sleep(3);
										
	report.startStep("Press on Start PLT");
		classicTest = testsPage.clickOnStartTest("1", "1");
	
		report.startStep("Check PLT opens");
		webDriver.switchToNewWindow();
		classicTest.switchToTestAreaIframe();
		classicTest.verifyTitlePLT();
	//	sleep(2);
		
	report.startStep("Perform PLT test - Attempt 1 - skip Cycle 2");
		PLTTest pltTest = classicTest.initPLTTestFromCSVFile("files/PLTTestData/PLT_ED_Answers_NEW.csv", true);
		String expectedLevelonExit = "B2";
		classicTest.performTestDidTest(pltTest);
		classicTest.close();
		
	report.startStep("Click on logout and login again without OptIn");
		learningArea2.clickOnLogoutLearningArea();
		sleep(3);
		loginAsStudent(studentId);
		homePage.skipNotificationWindow();
		homePage.waitHomePageloadedFully();
		
	report.startStep("Open Assessments");
		testsPage = homePage.openAssessmentsPage(false);
		//sleep(3); 
		
	report.startStep("Press on Start PLT");
		classicTest = testsPage.clickOnStartTest("1", "1");
		
	report.startStep("Check PLT opens");
		webDriver.switchToNewWindow();
		classicTest.switchToTestAreaIframe();
		classicTest.verifyTitlePLT();
		//sleep(2);
		
	report.startStep("Perform PLT test - Attempt 1 - skip Cycle 2");
	    pltTest = classicTest.initPLTTestFromCSVFile("files/PLTTestData/PLT_ED_Answers_NEW.csv", true);
		expectedLevelonExit = "Intermediate 3";	
		classicTest.performTest(pltTest);
		
	report.startStep("Verify expected placement level on test end");
		classicTest.verifyPlacementLevelOnResultPagePLT(expectedLevelonExit);
		
	report.startStep("Exit PLT");
		classicTest.clickOnExitPLT();
		
	report.startStep("Check Test Button Alert NOT DISPLAYED");
		WebElement testBtnAlert = homePage.getTestButtonAlertElement();
		if (testBtnAlert!=null)
			testResultService.addFailTest("Alert still shows available PLT test", false, true);
		
	report.startStep("Verify the Humbureger Alert not display");
		homePage.clickToOpenNavigationBar();
		homePage.getHumburgerAlert(false);
		homePage.clickToOpenNavigationBar();
		
	report.startStep("Logout");
		learningArea2.clickOnLogoutLearningArea();
		loginPage = new NewUXLoginPage(webDriver, testResultService);
		testResultService.assertEquals(false, loginPage.isSubmitButtonEnabled(), "Checking that ED Login Page displayed");
		
	report.startStep("Login as Admin");
		pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(configuration.getProperty("schoolAdmin.user"),institutionId));
		sleep(1);
		tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
		sleep(3);
		tmsHomePage.switchToMainFrame();
		
	report.startStep("Open PLT settings page");
		tmsHomePage.clickOnAssessment();
		sleep(3);
		//tmsHomePage.clickOnAutomatedTests();
		tmsHomePage.clickOnTestsConfiguration();
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
		webDriver.waitForElement("OnlyOncePT", ByTypes.id).click();
		
		if (iteration == 1 )
			testResultService.assertEquals(true, tmsHomePage.isPltSettingsDisplayed(), "PLT settings hiden");
		else
			testResultService.assertEquals(false, tmsHomePage.isPltSettingsDisplayed(), "PLT settings not hiden");
		
	report.startStep("Change Assign PLT option and check PLT settings view");
		tmsHomePage.checkUncheckAssignPLT();
		
		if (iteration == 1 )
			testResultService.assertEquals(false, tmsHomePage.isPltSettingsDisplayed(), "PLT settings not hiden");
		else
			testResultService.assertEquals(true, tmsHomePage.isPltSettingsDisplayed(), "PLT settings hiden");
		
		try {
			
			report.startStep("Press Save");
			tmsHomePage.clickOnSave();
			sleep(3);
		
			report.startStep("Exit TMS");
			tmsHomePage.clickOnExit();
			sleep(3);
			
			report.startStep("Verify 'Do Test Again' btn not displayed");
			loginAsStudent(studentId);
			homePage.skipNotificationWindow();
			homePage.waitHomePageloadedFully();
			
			testsPage = homePage.openAssessmentsPage(false);
			classicTest.verifyDoTestAgainNotDisplayedInPLT();
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		
		finally {
			report.startStep("Restore default PLT settings");
			pageHelper.assignPltToInstitution(institutionId);	
		}

	}

	@Test
	@TestCaseParams(testCaseID = { "27216" })
	public void testAssessmentsPageAvailableTests() throws Exception {
							
		report.startStep("Check that navigation bar opened and Assessments btn enabled");
		homePage.isNavBarOpen();
		homePage.isNavBarItemEnabled("sitemenu__itemTests");
		String testNameED= coursesNames[1] + " " + pageHelper.getImplementationTypeName(2,"ED");	
		
		report.startStep("Open Assessments");
		testsPage = homePage.openAssessmentsPage(false);
		//sleep(2);
		
		report.startStep("Check Verify Assessments header");
		testsPage.verifyAssessmentsPageHeader();
			sleep(2);
		report.startStep("Check Counter value in ALL sections");
		testsPage.checkItemsCounterBySection("1", "1");// Available 
		testsPage.checkItemsCounterBySection("2", "0");// Upcoming
		testsPage.checkItemsCounterBySection("3", "0");// Test Results
		
		report.startStep("Verify Placement Test displayed in Available section");
		testsPage.checkTestDisplayedInSectionByTestName("Placement Test", "1", "1");
		
		report.startStep("Verify Start Test btn displayed for PLT in Available section");
		testsPage.checkStartTestBtnDisplayedByTest("1", "1");
				
		report.startStep("Close Assessments");
		testsPage.close();
		
		report.startStep("Assign " + testNameED + " to student");
		String courseId = getCourseIdByCourseCode("B1");
		//pageHelper.assignAutomatedTestToStudent(studentId,courseId,2,0,0,3);
		pageHelper.assignAutomatedTestToStudetInTestEnvironment(studentId,courseId,2,0,0,3);
		sleep(3);
		
		report.startStep("Open Assessments");
		homePage.openAssessmentsPage(false);
		//sleep(2);
		
		report.startStep("Check Counter value in ALL sections");
		testsPage.checkItemsCounterBySection("1", "2");// Available 
		testsPage.checkItemsCounterBySection("2", "0");// Upcoming
		testsPage.checkItemsCounterBySection("3", "0");// Test Results
		
		report.startStep("Verify Placement Test displayed in Available section");
		testsPage.checkTestDisplayedInSectionByTestName("Placement Test", "1", "1");
		
		report.startStep("Verify Start Test btn displayed for PLT in Available section");
		testsPage.checkStartTestBtnDisplayedByTest("1", "1");
		
		report.startStep("Verify " +testNameED+ " displayed in Available section");
		testsPage.checkTestDisplayedInSectionByTestName(testNameED, "1", "2");
		
		report.startStep("Verify Start Test btn displayed for Mid-Term Test in Available section");
		testsPage.checkStartTestBtnDisplayedByTest("1", "2");
		
		report.startStep("Verify counter value for "+testNameED+" in Available section");
		sleep(180); // in order to be sure the days counter reduces to 2 days and 23:59 at hours and minutes
		String [] counterValues = pageHelper.getTestRemainingTimeByStudent(studentId, 2);
		int days = Integer.parseInt(counterValues[0]);
		
		if (days != 0) {		
			testsPage.checkDaysInCounter(counterValues[0], "2");
			//sleep(2);
		}
		testsPage.checkHoursInCounter(counterValues[1], "2");
		testsPage.checkMinutesInCounter(counterValues[2], "2");
		
		report.startStep("Close Assessments");
		testsPage.close();
	}
		
	@Test
	@TestCaseParams(testCaseID = { "27712" })
	public void testAssessmentsPageUpcomingTests() throws Exception {
				
	NewUxAssessmentsPage testsPage = new NewUxAssessmentsPage(webDriver,
					testResultService);
					
	report.startStep("Assign B1 Final Course Test to student for future time");
	String courseId = getCourseIdByCourseCode("B1");
	pageHelper.assignAutomatedTestToStudetInTestEnvironment(studentId, courseId,3,1,0,2);
	sleep(3);
					
	report.startStep("Open Assessments");
	homePage.openAssessmentsPage(false);
	//sleep(2);
			
	report.startStep("Open Upcoming Tests section");
	testsPage.clickOnArrowToOpenSection("2");
	sleep(2);
									
	report.startStep("Check Counter value in ALL sections");
	testsPage.checkItemsCounterBySection("1", "1");// Available 
	testsPage.checkItemsCounterBySection("2", "1");// Upcoming
	testsPage.checkItemsCounterBySection("3", "0");// Test Results
			
	report.startStep("Verify B1 Final Course Test displayed in Upcoming section");
	testsPage.checkTestDisplayedInSectionByTestName("Basic 1 Final Test", "2", "1");
			
	
	report.startStep("Check no Lock icon next to course test");
		testsPage.checkLockedIconAssessment(false);
	
	report.startStep("Verify Test Date displayed for B1 Final Course Test in Upcoming section");
	String expectedTestDate = pageHelper.getStartTestDateByStudent(studentId, 3);
	String actualTestDate = testsPage.getTestDateForUpcomingTests("1");
	testResultService.assertEquals(expectedTestDate, actualTestDate,"Verifying Test Date in Upcoming Section");
					
	report.startStep("Close Assessments");
	testsPage.close();
			
	}

/*	// Old Learning area mode
	@Test
	@Category(NonAngularLearningArea.class)
	@TestCaseParams(testCaseID = { "40441" })
	public void testAssessmentsPageNavButtonAlert() throws Exception {
				
	report.startStep("Assign B1 MidTerm & Final Course Test to student for future time");
	String courseId = getCourseIdByCourseCode("B1");
	int sectionsToSubmit = Integer.parseInt(dbService.getNumberOfSectionsInTestForCourseByCourseId(courseId, 2));
	pageHelper.assignAutomatedTestToStudent(studentId, courseId,3,1,0,2); // Final Test start in 1 day
	pageHelper.assignAutomatedTestToStudent(studentId, courseId,2,0,1,2); // MidTerm Test start in 1 minute
	sleep(5);
		
	report.startStep("Check Test Button Alert");
	verifyTestNumberInAlert(1);
		
	report.startStep("Wait 1 min till upcoming test becomes availabele");
	sleep(60);
		
	report.startStep("Open Assessments");
	testsPage = homePage.openAssessmentsPage(false);
	//sleep(3);
	
	report.startStep("Check Test Button Alert");
	verifyTestNumberInAlert(2);
			
	report.startStep("Check Counter value in ALL sections");
	testsPage.checkItemsCounterBySection("1", "2");// Available 
	testsPage.checkItemsCounterBySection("2", "1");// Upcoming
	testsPage.checkItemsCounterBySection("3", "0");// Test Results
					
	report.startStep("Submit 1 Test");
	classicTest = testsPage.clickOnStartTest("1", "2");
	webDriver.closeAlertByAccept();
	sleep(3);
	webDriver.switchToNewWindow();
	classicTest.switchToTestAreaIframe();
	classicTest.pressOnStartTest();
	
	
	
	for (int i = 0; i < sectionsToSubmit ; i++) {
		
		classicTest.browseToLastSectionTask();
		report.addTitle("Submit section number: " + (i+1));
		classicTest.pressOnSubmitSection(true);
			}
	sleep(3);

	
	webDriver.closeNewTab(1);
	webDriver.switchToMainWindow();
	
	report.startStep("Check Test Button Alert");
	verifyTestNumberInAlert(1);
	
	report.startStep("Check Test Button Alert in Learning Area");
	homePage.carouselNavigateNext();
	sleep(1);
	homePage.clickOnContinueButton();
	sleep(1);
	learningArea.clickToOpenNavigationBar();
	verifyTestNumberInAlert(1);
					
	}
*/
	
	@Test
	@Category(AngularLearningArea.class)
	@TestCaseParams(testCaseID = { "40441" })
	public void testAssessmentsPageNavButtonAlert2() throws Exception {
				
	report.startStep("Assign B1 MidTerm & Final Course Test to student for future time");
	String courseId = getCourseIdByCourseCode("B1");
	
	String wantedTestId = "989012509";
	String newTestIdTE = "989017755";
	
	pageHelper.assignAutomatedTestToStudetInTestEnvironment(studentId, courseId,3,1,0,2); // Final Test start in 1 day
	sleep(3);
	pageHelper.assignAutomatedTestToStudetInTestEnvironment(studentId, courseId,2,0,0,2); // MidTerm Test start in 1 minute
	sleep(3);
	
	report.startStep("Return Test Id");
	String testId = pageHelper.getAssignedTestIdForStudent(studentId,courseId, 2);
	
	if (testId.equals(newTestIdTE) || testId.equals("989022835")) {
		// update test id
		String userExitSettingsId = dbService.getUserExitSettingsId(studentId, testId, courseId, "2");
		
		dbService.updateTestIdInUserExitSettings(wantedTestId, userExitSettingsId);
		testId = pageHelper.getAssignedTestIdForStudent(studentId,courseId, 2);
		String token = pageHelper.getTokenFromConsoleWithParameters("1", studentId, testId, userExitSettingsId);
		dbService.updateAssignWithToken(token, userExitSettingsId);
	}
	
	
	// testId = pageHelper.getAssignedTestIdForStudent(studentId,courseId, 2);
	
	
	
	int sectionsToSubmit = Integer.parseInt(dbService.getNumberOfSectionsInTestForCourseByTestId(courseId, testId, 2));
	
	//report.startStep("Check Test Button Alert");
	//verifyTestNumberInAlert(1);
		
	//report.startStep("Wait 5 sec till upcoming test becomes availabele");
	//sleep(3);
		
	report.startStep("Open Assessments");
	testsPage = homePage.openAssessmentsPage(false);
	//sleep(5);
	
	report.startStep("Check Test Button Alert");
	verifyTestNumberInAlert(2);
			
	report.startStep("Check Counter value in ALL sections");
	testsPage.checkItemsCounterBySection("1", "2");// Available 
	testsPage.checkItemsCounterBySection("2", "1");// Upcoming
	testsPage.checkItemsCounterBySection("3", "0");// Test Results
	
					
	report.startStep("Validate Test sequence");
	int sequence = testsPage.getAndValidateCourseTestSequence(courseId,2);
	String testSequence = Integer.toString(sequence);
	
	report.startStep("Start First available Test");
	classicTest = testsPage.clickOnStartTest("1", testSequence);
	
	
	webDriver.closeAlertByAccept();
	sleep(3);
	webDriver.switchToNewWindow();
	classicTest.switchToTestAreaIframe();
	classicTest.pressOnStartTest();
	//sleep(3);
	classicTest.waitForTestPageLoaded();
	
	for (int i = 0; i < sectionsToSubmit ; i++) {
		
		classicTest.browseToLastSectionTask();
		report.addTitle("Submit section number: " + (i+2));
		classicTest.checkSubmitSectionDisplay(true);
		classicTest.pressOnSubmitSection(true);
		}
	sleep(3);
	
	webDriver.closeNewTab(1);
	webDriver.switchToMainWindow();
	report.startStep("Check Test Button Alert");
	verifyTestNumberInAlert(1);
								
	}
						
	@Test
	@TestCaseParams(testCaseID = { "27123", "40220" })
	public void testAssessmentsPageTestResults() throws Exception {
					
	String expScore = "0";
	String courseCode = "B1";
	String courseId = getCourseIdByCourseCode(courseCode);
	String courseName = getCourseNameByCourseCode(courseCode);
	
	
	report.startStep("Assign B1 Final Test to student");
		pageHelper.assignAutomatedTestToStudetInTestEnvironment(studentId, courseId, 3,0,0,1);
		String testId = pageHelper.getAssignedTestIdForStudent(studentId, courseId, 3);
		int sectionsToSubmit = Integer.parseInt(dbService.getNumberOfSectionsInTestForCourseByTestId(courseId,testId, 3));
		//sleep(3);
	report.addTitle("Test Id is: " + testId + ". Section counts are: " + sectionsToSubmit);
		
	report.startStep("Open My Progress page and check course tests widgets not displayed");
		verifyCourseTestWidgetsNotDisplayed(courseName);
		homePage.waitHomePageloaded();
	
	report.startStep("Open Assessments");
		testsPage = homePage.openAssessmentsPage(false);
		//sleep(2);
			
	report.startStep("Check Counter value in ALL sections");
		testsPage.checkItemsCounterBySection("1", "2");// Available 
		testsPage.checkItemsCounterBySection("2", "0");// Upcoming
		testsPage.checkItemsCounterBySection("3", "0");// Test Results
					
	report.startStep("Start Test");
		int testSequence = testsPage.getTestSequenceByCourseId(courseId);
		if (testSequence != 2) {
			testResultService.addFailTest("Tests sequnce is Incorrect. Excpected: 1. Placement test, 2. course test. Actual: 1. course test, 2. Placement test", false, true);
		}
		classicTest = testsPage.clickOnStartTest("1", String.valueOf(testSequence));
		webDriver.closeAlertByAccept();
		sleep(2);
		webDriver.switchToNewWindow();
		classicTest.switchToTestAreaIframe();
		classicTest.pressOnStartTest();
		classicTest.waitForTestPageLoaded();
		//sleep(3);
		
		// Verified closing of test following the resume functionality addition. 
		for (int i = 0; i < sectionsToSubmit ; i++) {
			classicTest.browseToLastSectionTask();
			classicTest.pressOnSubmitSection(true);
			report.addTitle("Submitted section number: " + (i+1));
		}
		classicTest.close();
		// 19.7.2018 end
		
			
		
	report.startStep("Open Assessments");
		webDriver.switchToMainWindow();
		homePage.openAssessmentsPage(false);
		sleep(3);
					
	report.startStep("Check Counter value in ALL sections");
		testsPage.checkItemsCounterBySection("1", "1");// Available 
		testsPage.checkItemsCounterBySection("2", "0");// Upcoming
		testsPage.checkItemsCounterBySection("3", "1");// Test Results
			
	report.startStep("Open Tests Results section");
		testsPage.clickOnArrowToOpenSection("3");
			
	report.startStep("Verify" + courseName+ " Final Test displayed in Tests Results section");
		testsPage.checkTestDisplayedInSectionByTestName(courseName +" Final Test", "3", "1");
								
	report.startStep("Check Date of Submission");
		testsPage.checkSubmissionDateForTests("1");
			
	report.startStep("Check Score in Test Results");
		testsPage.checkScoreByTest("1", expScore);
	
	report.startStep("Close Assignment Page");
		testsPage.close();
		sleep(1);
	
	report.startStep("Verify My Progress");
		verifyFinalTestWidgetDisplayed(expScore, courseName);
		
	}

	@Category(ReleaseTests.class)
	@Test
	@TestCaseParams(testCaseID = { "27240","40425","40830","40831" })
	public void testAssessmentsPagePLT() throws Exception {
	
	report.startStep("Wait to Home page loaded");
	homePage.waitHomePageloadedFully();
	
	//report.startStep("Check Test Button Alert");
	//verifyTestNumberInAlert(1);			
	
	report.startStep("Verify the Humbureger Alert doesn't display when the Navigation bar is open");
	homePage.getHumburgerAlert(false);
	
	//report.startStep("Verify the Humbureger Alert display when the Navigation is close");
	//homePage.clickToOpenNavigationBar();
	//homePage.getHumburgerAlert(true);
	//homePage.clickToOpenNavigationBar();
	
	report.startStep("Open Assessments");
	testsPage = homePage.openAssessmentsPage(false);
	//sleep(3);
									
	report.startStep("Press on Start PLT");
	classicTest = testsPage.clickOnStartTest("1", "1");
	
	report.startStep("Check PLT opens");
	Thread.sleep(2000);
	webDriver.switchToNewWindow();
	classicTest.switchToTestAreaIframe();
	classicTest.verifyTitlePLT();
	//sleep(2);
		
	report.startStep("Perform PLT test - Attempt 1 - skip Cycle 2");
	PLTTest pltTest = classicTest.initPLTTestFromCSVFile("files/PLTTestData/PLT_ED_Answers_NEW.csv", true);
	String expectedLevelonExit = "Intermediate 3";	
	classicTest.performTest(pltTest);
	
	report.startStep("Verify expected placement level on test end");
	classicTest.verifyPlacementLevelOnResultPagePLT(expectedLevelonExit);
	
	report.startStep("Verify PLT Initial Level is correct");
	//String expectedInitialLevel = firstCycle.getPltStartLevel().toString().substring(0, 2);
	
	
	classicTest.verifyPlacementLevelOnResultPagePLT(expectedLevelonExit);
	
	report.startStep("Verify description link");
	classicTest.verifyDescriptionLinkPLT(coursesNames);
	
	report.startStep("Verify prefer higher level link");
	classicTest.verifyPreferToStudyLinkPLT();
	
	report.startStep("Click on Do Test Again");
	classicTest.clickOnDoTestAgainPLT();
	
	report.startStep("Check PLT opens");
	webDriver.switchToNewWindow();
	classicTest.switchToTestAreaIframe();
	classicTest.verifyTitlePLT();
	
	report.startStep("Perform PLT test - Attempt 2 - do both cycles");
	pltTest = classicTest.initPLTTestFromCSVFile("files/PLTTestData/PLT_ED_Answers_NEW.csv", false);
	//expectedLevelonExit = "Advanced 3";	
	//classicTest.performTest(pltTest);
	expectedLevelonExit = "First Discoveries";
	classicTest.startPltAndExit();
	
	report.startStep("Verify expected placement level on test end");
	classicTest.verifyPlacementLevelOnResultPagePLT(expectedLevelonExit);
	
	//report.startStep("Verify description link");
	//classicTest.verifyDescriptionLinkPLT(coursesNames);
	
	//report.startStep("Verify prefer higher level link link");
	//classicTest.verifyPreferToStudyLinkPLT();	
	
	report.startStep("Exit PLT");
	classicTest.clickOnExitPLT();
	
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
	//sleep(1);
					
	report.startStep("Open Tests Results section");
	testsPage.clickOnArrowToOpenSection("3");
			
	report.startStep("Verify PLT displayed in Test Results");
	testsPage.checkTestDisplayedInSectionByTestName("Placement Test", "3", "1");
								
	report.startStep("Check Date of Submission");
	testsPage.checkSubmissionDateForTests("1");
			
	report.startStep("Check Score in Test Results");
	testsPage.checkScoreLevelPLT("1", expectedLevelonExit);
	
	
}

	@Test
	@TestCaseParams(testCaseID = { "51353" })
	public void testMultiTestsForCourseMultiAssignments() throws Exception {
		
		report.startStep("Init Test Data");
			String CourseId = "44205"; //Basic 3
			String TestId = "47859"; // Intermediate 2 CT
			String unitid = "47860";
			String componentid = "27266";
			String lastItemId="61190";
			
			String[] IDs = new String[2];
			String[] testScores = {"0","0"};
			String[] section1_CorrectAns = {"New York","Her first flight was delayed.","True","False","The flight is nonstop."};

			String testName = "Basic 3 Final Test";
			int TestType = 3; //3 means CT ; 2 means mid-term
			int sectionsToSubmit;
			String instID = configuration.getProperty("institution.id");;
		
		try {
			//report.startStep("Add additional test in TestBank For Course");
				//dbService.addTestToCourseByIds(CourseId,TestId,TestType);
				
			//report.startStep("Turn On 'Allow Multi-test' for Institution");
			//dbService.enableMultiTestForInstitution(instID);
			
			report.startStep("Assign CT for the current user");
				pageHelper.assignAutomatedTestToStudetInTestEnvironment(studentId,CourseId,TestType,0,0,3);
				
			for (int i = 0 ; i < 1 ; i++){
					
				report.startStep("Get assigned test Id");
					IDs[i] = dbService.getStudentAssignedTestId(studentId, TestType);		
				
					if (!IDs[i].equalsIgnoreCase(TestId)){
					unitid = "1540921";
					componentid = "1540922";
					
				report.startStep("Submit one section of B3 CT");
					pageHelper.submitCourseTestSection(studentId,unitid,componentid,lastItemId);
					
				report.startStep("Assign CT for the current user");
					pageHelper.assignAutomatedTestToStudetInTestEnvironment(studentId,CourseId,TestType,0,0,3);
					}
				
			}
			
			report.startStep("Compare assigned test IDs");
			if (IDs[0] == IDs[1]) {
				testResultService.addFailTest("Same test was assigned twice");
			}
		}
		finally {
			report.startStep("Undo DB changes");
				//instID = configuration.getProperty("institution.id");
				//dbService.disableMultiTestForInstitution(instID);
				//dbService.removeTestFromCourseByIds(CourseId,TestId,TestType);
				dbService.removeTestResults(studentId,CourseId,TestType);
				dbService.removeUserExitTestSettings(studentId,CourseId,TestType);
		}
	}	

	
	@Test
	@TestCaseParams(testCaseID = { "40830" })
	public void testAssessmentsGetAdvanced3_PLT_CEFR_oldTE() throws Exception {
		
		report.startStep("Restart browser, create user and login");
			//homePage.waitTillCarouselLoad();	
			homePage.logOutOfED();
			String instName = institutionsName[14];
			pageHelper.restartBrowserInNewURL(instName, true);
			
			try{
				useCEFR = true;	
		
				String instId = dbService.getInstituteIdByName(instName);
				List<String[]> classes = pageHelper.getclassesByInstitutionName(instName);
				String[]oneClass = classes.get(new Random().nextInt(classes.size()-1));
				homePage = createUserAndLoginNewUXClass(oneClass[0],instId);
				pageHelper.skipOptin();
				homePage.skipNotificationWindow();
				pageHelper.skipOnBoardingHP();
				homePage.waitHomePageloaded();
					
				pageHelper.assignPltToInstitution(instId);
					sleep(4);
				
				report.startStep("Open Assessments");
					testsPage = homePage.openAssessmentsPage(false);
					//sleep(3);
												
				report.startStep("Press on Start PLT");
				classicTest = testsPage.clickOnStartTest("1", "1");
				
				report.startStep("Check PLT opens");
				Thread.sleep(1000);
					webDriver.switchToNewWindow();
					classicTest.switchToTestAreaIframe();
					classicTest.verifyTitlePLT();
						
				report.startStep("Perform PLT test from Intermediate (3) - Attempt 2 - do both cycles");
					PLTTest pltTest = classicTest.initPLTTestFromCSVFile("files/PLTTestData/PLT_ED_Answers_NEW.csv", false);
					String expectedLevelonExit = "C1";//"Advanced 3";	
					classicTest.performTest(pltTest);
		
				report.startStep("Verify expected placement level and CEFR result");
					classicTest.verifyPlacementLevelOnResultPagePLT(expectedLevelonExit);
					
				report.startStep("Exit PLT");
					classicTest.clickOnExitPLT();
				
				report.startStep("Open Assessments");
					homePage.openAssessmentsPage(false);
					//sleep(1);
								
				report.startStep("Open Tests Results section");
					testsPage.clickOnArrowToOpenSection("3");
						
				report.startStep("Verify PLT displayed in Test Results");
					testsPage.checkTestDisplayedInSectionByTestName("Placement Test", "3", "1");
											
				report.startStep("Check Date of Submission");
					testsPage.checkSubmissionDateForTests("1");
						
				report.startStep("Check Score in Test Results");
					testsPage.checkScoreLevelPLT("1", expectedLevelonExit);
			}
			finally {
				useCEFR = false;	
			}
	}
	
	
	private void verifyCourseTestWidgetsNotDisplayed(String courseName) throws Exception {
		
		homePage.navigateToRequiredCourseOnHomePage(courseName);
		Thread.sleep(1000);
		myProgress = homePage.clickOnMyProgress();
		Thread.sleep(1000);
		testResultService.assertEquals(courseName, myProgress.getCourseName(), "Course Name is wrong");
		testResultService.assertEquals(false, myProgress.isMidTermWidgetDisplayed(), "Midterm widget displayed though it should not");
		testResultService.assertEquals(false, myProgress.isFinalTestWidgetDisplayed(), "Final test widget displayed though it should not");
		homePage.clickToOpenNavigationBar();
		homePage.clickOnHomeButton();
		
	}
	
	private void verifyFinalTestWidgetDisplayed(String score, String courseName) throws Exception {
		
		homePage.navigateToRequiredCourseOnHomePage(courseName);
		webDriver.waitUntilElementAppears("//div[contains(@class,'carouselCaptions')]", ByTypes.xpath, 10);
		//sleep(2);
		myProgress = homePage.clickOnMyProgress();
		myProgress.waitForPageToLoad();
		testResultService.assertEquals(courseName, myProgress.getCourseName(), "Course Name is wrong");
		//sleep(2);
		testResultService.assertEquals(true, myProgress.isFinalTestWidgetDisplayed(), "Final test widget not displayed");
		//sleep(3);
		testResultService.assertEquals(score, myProgress.getFinalTestScore(), "Final test score not valids");
		//sleep(2);
		homePage.clickToOpenNavigationBar();
		sleep(1);
		homePage.clickOnHomeButton();
		
	}
	
	private void verifyTestNumberInAlert(int expectedNumberInAlert) throws Exception {
						
		int actNumInAlert = Integer.valueOf(homePage.getAvailableTestsNumberInAlert());
		testResultService.assertEquals(expectedNumberInAlert, actNumInAlert, "Alert shows wrong num of available tests: "+expectedNumberInAlert+" tests expected", true);
		
	}
	
	public void  SubmitFirstSectionWithCorrectAnswers (int section,String testId) throws Exception{
		
		String filePath = "";
		/*
		if (testId.startsWith("154") || (testId.startsWith("9890"))) {
			filePath = "files/CourseTestData/CourseTest_Answers_2019.csv";
		}
		else{
			filePath = "files/CourseTestData/CourseTest_Answers.csv";
		}
			*/
		CourseTests testType = CourseTests.MidTerm;
		
		if (testId.startsWith("989012509")) {
			filePath = "files/CourseTestData/CourseTest_Answers_2019.csv";
			
		} else if (testId.equals("47845")) {
			filePath = "files/CourseTestData/CourseTest_Answers_2019.csv";
			testType = CourseTests.FinalTest;
			
		} else if (testId.equals("1540731")) {
			filePath = "files/CourseTestData/CourseTest_Answers_B1_1540731.csv";
			testType = CourseTests.FinalTest;

		} else if (testId.equals("989013148")) {
			filePath = "files/CourseTestData/CourseTest_Answers_B1_MT_989013148.csv";
		
		} else {
			filePath = "files/CourseTestData/CourseTest_Answers.csv";
		}
		
		classicTest = new NewUxClassicTestPage(webDriver,testResultService); // new line
		
		CourseTest courseTest = classicTest.initCourseTestFromCSVFile(filePath, CourseCodes.B1, testType, section);
		classicTest.performCourseTest(courseTest, section);
		/*
		
		//String [] section1_CorrectAns = {"at the Mozart Theater.","False","a rock star.","True","False"};
		String [] section1_CorrectAns = {"We don't know","free drink","False.","True","excellent food"};
		
		int questionsInSection = 5;
			for (int i = 0; i < questionsInSection; i++) {
				classicTest.selectAnswerByTextMCQ(section1_CorrectAns[i]);
				sleep(1);
				classicTest.pressOnNextTaskArrow();
			}
			sleep (60);
			classicTest.pressOnSubmitSection(true);
			*/
		//classicTest.pressOnSubmitSection(true);	
		classicTest.close();
	}
public void  SubmitFirstSectionWithCorrectAnswers (int section,String testId,NewUxClassicTestPage classicTest) throws Exception{
		
		String filePath = "";
		
		if (testId.startsWith("154") || (testId.startsWith("9890"))) {
			filePath = "files/CourseTestData/CourseTest_Answers_2019.csv";
		}
		else{
			filePath = "files/CourseTestData/CourseTest_Answers.csv";
		}
			
		CourseTest courseTest = classicTest.initCourseTestFromCSVFile(filePath, CourseCodes.B1, CourseTests.MidTerm, section);
		classicTest.performCourseTest(courseTest, section);
	}
	
	 public void getTestRemainingTimeByStudent () throws Exception {
		 
		
	 try {
		 	String [] counterValues = pageHelper.getTestRemainingTimeByStudent(studentId, 2);
		 	sleep(2);
			testsPage.checkDaysInCounter(counterValues[0], "2");
			sleep(2);
			testsPage.checkHoursInCounter(counterValues[1], "2");
			testsPage.checkMinutesInCounter(counterValues[2], "2");	 
	 }catch (Exception e) {
			testResultService.addFailTest(e.getMessage(), true, true);
	 }catch (AssertionError e) {
			testResultService.addFailTest(e.getMessage(), true, true);
	 }	
 }
	 
	 public void getTestRemainingTimeByStudentAndCourseId (String courseId) throws Exception {
		 int testSequence = testsPage.getTestSequenceByCourseId(courseId);
			if (testSequence != 2) {
				testResultService.addFailTest("Tests sequnce is Incorrect. Excpected: 1. Placement test, 2. course test. Actual: 1. course test, 2. Placement test", false, true);
			}
		 String [] counterValues = pageHelper.getTestRemainingTimeByStudent(studentId, 2);
			//int days = Integer.parseInt(counterValues[0]);
			
			//if (days != 0) {		
		 	sleep(2);
			testsPage.checkDaysInCounter(counterValues[0], String.valueOf(testSequence));
				sleep(2);
			//}
			testsPage.checkHoursInCounter(counterValues[1], String.valueOf(testSequence));
			
			testsPage.checkMinutesInCounter(counterValues[2], String.valueOf(testSequence));	 
	 }
	 
	public void  SubmitSecondSectionWithWrongAnswers () throws Exception{
		int questionsInSection = 5;
		//String [] section1_WrongAns = {"False","free drink.","True","False","excellent food."};
		for (int i = 0; i < questionsInSection; i++) {
		//classicTest.selectAnswerByTextMCQ(section1_WrongAns[i]);
		sleep(1);
		classicTest.pressOnNextTaskArrow();
		}
	}

	/*public PLTTest initPLTTestFromCSVFile(String path, Boolean skipCycle2) throws Exception {
		
		PLTCycle cycle1 = new PLTCycle();
		PLTCycle cycle2 = new PLTCycle();
	
		cycle1 = initCycle("1", path, PLTStartLevel.Intermediate, false);
		cycle2 = initCycle("2", path, PLTStartLevel.Advance, skipCycle2);
	
		List<PLTCycle> testCycles = new ArrayList<PLTCycle>();
		testCycles.add(cycle1);
		testCycles.add(cycle2);
	
		PLTTest pltTest = new PLTTest(testCycles);
		
		return pltTest;
	}
		
	private PLTCycle initCycle(String cycleNumber, String path,
			PLTStartLevel pltStartLevel, boolean skipCycle) throws Exception {
		
		List<String[]> testData = textService.getStr2dimArrFromCsv(path);
	
		List<TestQuestion> cycleQuestions = new ArrayList<TestQuestion>();
		
		for (int i = 0; i < testData.size(); i++) {
			
			// if cycle number match - create TestQuestion object
			if (testData.get(i)[0].equals(cycleNumber)) {
				
				try {
					report.report("Reading line: "+i+" from csv file");
					
					String[] answers = textService.splitStringToArray(testData.get(i)[1],"\\|");
					
					String[] answerDestinations = textService.splitStringToArray(testData.get(i)[2],"\\|");
					
					String[] wrongAnswers = textService.splitStringToArray(testData.get(i)[3],"\\|");
					
					if (skipCycle) wrongAnswers[0] = "DoNotAnswer";
						
	//			int[] blankAnswers = textService.splitStringToIntArray(
	//					testData.get(i)[5], "?!^");
	
	//			boolean booleanAnswer = Boolean.getBoolean(testData.get(i)[5]);
					// String questionType = testData.get(i)[6];
					report.report("Question type from csv is: "+testData.get(i)[5]);
					TestQuestionType questionType = TestQuestionType
							.valueOf(testData.get(i)[5]);
	
					TestQuestion question = new TestQuestion(answers,
							answerDestinations, wrongAnswers, new int[]{},
							 questionType);
					cycleQuestions.add(question);
					report.report("Finished adding question data for line: "+i);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		PLTCycle pltCycle = new PLTCycle(pltStartLevel,
				Integer.valueOf(cycleNumber), null, cycleQuestions, null);
		return pltCycle;
	
	}*/
	
	
	@Test
	@TestCaseParams(testCaseID = { "74558" })
	public void testPlacementVersion1Institution() throws Exception {
		report.startStep("Init Data");
		String[] expectedTitles = {"Life On My Street", "Save The Earth", "Company Dumps Toxic Waste","Businesswoman Nicole Hunt Tells All"};
		String[] actualTitles = new String[expectedTitles.length];
		classicTest = new NewUxClassicTestPage(webDriver,testResultService);
		sleep(2);
		
		report.startStep("Start PLT Test");
		classicTest.startPltTest();
		
		report.startStep("Retrieve the Title of Placement Test In Level Basic");
		String currentTitle = classicTest.getTitleOfPlacementTestInSpecificLevel(PLTStartLevel.Basic, studentId);
		actualTitles[0] = currentTitle;
		
		report.startStep("Validate Level 'B2' is Stored Correctly in the DB");
		checkLevelInDb("B2");
		
		report.startStep("Retrieve the Title of Placement Test In Level Intermidate");
		currentTitle = classicTest.getTitleOfPlacementTestInSpecificLevel(PLTStartLevel.Intermediate, studentId);
		actualTitles[1] = currentTitle;
		
		report.startStep("Validate Level 'I2' is Stored Correctly in the DB");
		checkLevelInDb("I2");
		
		report.startStep("Retrieve the Title of Placement Test In Level Advance");
		currentTitle = classicTest.getTitleOfPlacementTestInSpecificLevel(PLTStartLevel.Advanced, studentId);
		actualTitles[2] = currentTitle;
		
		report.startStep("Validate Level 'A2' is Stored Correctly in the DB");
		checkLevelInDb("A2");
		
		report.startStep("Retrieve the Title of Placement Test In Level I am not sure");
		currentTitle = classicTest.getTitleOfPlacementTestInSpecificLevel(PLTStartLevel.IamNotSure, studentId);
		actualTitles[3] = currentTitle;
		
		report.startStep("Validate Level 'B3' is Stored Correctly in the DB");
		checkLevelInDb("B3");
		
		report.startStep("Close the Test Page");
		classicTest.clickExitPltButton();
		
		report.startStep("Validate the Retrieved Titles are Correct");
		classicTest.validateTitlesAreCorrect(expectedTitles,actualTitles);
		
		report.startStep("Log out");
		homePage.logOutOfED();
	}
	
	@Test
	@TestCaseParams(testCaseID = { "74559" })
	public void testPlacementVersion2Institution() throws Exception {
	
		report.startStep("Log out");
		homePage.logOutOfED();
		
		institutionName = institutionsName[3];
		pageHelper.initializeData();
		
		
		report.startStep("Retrieve the URL");
		String url = webDriver.getUrl();
		
		report.startStep("Replace 'Automation' to 'Local'");
		url = url.replace("automation", "local");
		url =  url.replace("/home", "/login");
		
		report.startStep("Close browser and open again");
		
		pageHelper.restartBrowser();
		
		report.startStep("Login again with the same user");
		webDriver.openUrl(url);
		sleep(2);
		
		report.startStep("Get Local user and Log in");
		String[] userDetails = getLocalUser();
		String userName = userDetails[0];
		String password = userDetails[1];
		NewUXLoginPage loginPage = new NewUXLoginPage(webDriver, testResultService);
		loginPage.loginAsStudent(userName, password);
		institutionId = dbService.getInstituteIdByName("Local");
		studentId = dbService.getUserIdByUserName(userName, institutionId);
		
		pageHelper.skipOptin();
		homePage.skipNotificationWindow();
		homePage.waitHomePageloadedFully();
		
		report.startStep("Skip the Walkthrough");
		pageHelper.skipOnBoardingHP();
		
		report.startStep("Init Data");
		String[] expectedTitles = {"From:", "Amanda's Vacation", "Dorothy Green","Artemis Theater Presents..."};
		String[] actualTitles = new String[expectedTitles.length];
		classicTest = new NewUxClassicTestPage(webDriver,testResultService);
		
		report.startStep("Start PLT Test");
		classicTest.startPltTest();
		
		report.startStep("Retrieve the Title of Placement Test In Level Basic");
		String currentTitle = classicTest.getTitleOfPlacementTestInSpecificLevel(PLTStartLevel.Basic, studentId);
		actualTitles[0] = currentTitle;
		
		report.startStep("Validate Level 'B2' is Stored Correctly in the DB");
		checkLevelInDb("B2");
		
		report.startStep("Retrieve the Title of Placement Test In Level Intermidate");
		currentTitle = classicTest.getTitleOfPlacementTestInSpecificLevel(PLTStartLevel.Intermediate, studentId);
		actualTitles[1] = currentTitle;
		
		report.startStep("Validate Level 'I2' is Stored Correctly in the DB");
		checkLevelInDb("I2");
		
		report.startStep("Retrieve the Title of Placement Test In Level Advance");
		currentTitle = classicTest.getTitleOfPlacementTestInSpecificLevel(PLTStartLevel.Advanced, studentId);
		actualTitles[2] = currentTitle;
		
		report.startStep("Validate Level 'A2' is Stored Correctly in the DB");
		checkLevelInDb("A2");
		
		report.startStep("Retrieve the Title of Placement Test In Level I am not sure");
		currentTitle = classicTest.getTitleOfPlacementTestInSpecificLevel(PLTStartLevel.IamNotSure, studentId);
		actualTitles[3] = currentTitle;
		
		report.startStep("Validate Level 'B3' is Stored Correctly in the DB");
		checkLevelInDb("B3");
	
		report.startStep("Close the Test Page");
		classicTest.clickExitPltButton();
		
		report.startStep("Validate the Retrieved Reading Titles are Correct");
		classicTest.validateTitlesAreCorrect(expectedTitles,actualTitles);
		
		report.startStep("Log out");
		homePage.logOutOfED();
	}
	
	@Test
	@TestCaseParams(testCaseID = { "" })
	public void testPlacementVersion3Institution() throws Exception {
		
		report.startStep("Retrieve the URL");
		institutionName = institutionsName[6]; //dbService.getInstituteNameById(laureateInstId);

		report.startStep("Log out");
		homePage.logOutOfED();
		
		report.startStep("Close browser and open again");
		report.startStep("Replace 'Automation' to: " + institutionName);
		pageHelper.restartBrowserInNewURL(institutionName,true);
		sleep(2);
		
		report.startStep("Get User and Log in");
		createUserAndLoginNewUXClass(configuration.getProperty("lau_className"), institutionId);
		
		report.startStep("Wait until loading message dissappers (if its displayed)");
		homePage.waitUntilLoadingMessageIsOver();
			
		report.startStep("Close All Notifications");	
		homePage.closeAllNotifications();
		homePage.waitHomePageloaded();
		
		report.startStep("Close modal pop up (if it appears)");
		homePage.closeModalPopUp();
		
		report.startStep("Init Data");
		String[] expectedTitles = {"Mad Mo's Department Store", "Computer Expo", "You've Hit the Jackpot!","Police Arrest Bicycle Thief"};
		String[] actualTitles = new String[expectedTitles.length];
		classicTest = new NewUxClassicTestPage(webDriver,testResultService);
		
		report.startStep("Start PLT Test");
		classicTest.startPltTest();
		sleep(2);
		
		report.startStep("Retrieve the Title of Placement Test In Level Basic");
		String currentTitle = classicTest.getTitleOfPlacementTestInSpecificLevel(PLTStartLevel.Basic, studentId);
		actualTitles[0] = currentTitle;
		
		report.startStep("Validate Level 'B2' is Stored Correctly in the DB");
		checkLevelInDb("B2");
		
		report.startStep("Retrieve the Title of Placement Test In Level Intermidate");
		currentTitle = classicTest.getTitleOfPlacementTestInSpecificLevel(PLTStartLevel.Intermediate, studentId);
		actualTitles[1] = currentTitle;
		
		report.startStep("Validate Level 'I2' is Stored Correctly in the DB");
		checkLevelInDb("I2");
		
		report.startStep("Retrieve the Title of Placement Test In Level Advance");
		currentTitle = classicTest.getTitleOfPlacementTestInSpecificLevel(PLTStartLevel.Advanced, studentId);
		actualTitles[2] = currentTitle;
		
		report.startStep("Validate Level 'A2' is Stored Correctly in the DB");
		checkLevelInDb("A2");
		
		report.startStep("Retrieve the Title of Placement Test In Level I am not sure");
		currentTitle = classicTest.getTitleOfPlacementTestInSpecificLevel(PLTStartLevel.IamNotSure, studentId);
		actualTitles[3] = currentTitle;
		
		report.startStep("Validate Level 'B3' is Stored Correctly in the DB");
		checkLevelInDb("B3");
		
		report.startStep("Close the Test Page");
		classicTest.clickExitPltButton();
		
		report.startStep("Validate the Retrieved Reading Titles are Correct");
		classicTest.validateTitlesAreCorrect(expectedTitles,actualTitles);
		
		report.startStep("Log out");
		homePage.logOutOfED();
	}
	
	public void checkLevelInDb(String expectedLevel) {
		String initialPltLevel = dbService.getInitialPLTLevel(studentId);
		testResultService.assertEquals(expectedLevel, initialPltLevel, "Plt level is not stored correctly in DB");
	}

	@Category(ReleaseTests.class)
	@Test
	@TestCaseParams(testCaseID = { "" })
	public void testPLT_API_GetUser() throws Exception {
		
		report.startStep("Log out");
		homePage.logOutOfED();
				
		report.startStep("Open PLT API");
		
		//String instName= institutionName; //here you can change the InstitutionName; //"courses";
		String apiToken = dbService.getApiToken(institutionId);
		String className = "IntegrationPLT";
		//String className = "classNewUx";
		boolean newUser = false;

		//open_PLT_API(instName);
		pageHelper.open_PLT_API(institutionName, className, apiToken,newUser);
		
		// Initialize classic test page
		classicTest = new NewUxClassicTestPage(webDriver,testResultService);

		report.startStep("Click Submit");
		classicTest.clickSubmitOnPLT_API();
		studentId = dbService.getUserIdByUserName(BasicNewUxTest.userName, institutionId); // get here the studentId

		report.startStep("Verify Title");
		classicTest.verifyTitlePLT();
		
		report.startStep("Verify Level List & Languages & buttons");
		classicTest.verifyLevelListExist();
		classicTest.verifyLanguagesExists();
		classicTest.verifyStartTestButtonsExists();
		
		report.startStep("Init PLT Test From CSV File");
		PLTTest pltTest = classicTest.initPLTTestFromCSVFile("files/PLTTestData/PLT_ED_Answers_NEW.csv", true);
				
		PLTCycle firstCycle = pltTest.getCycles().get(0);
		PLTCycle SecondCycle = pltTest.getCycles().get(1);
		
		report.startStep("Select Level");
		classicTest.selectLevelOnStartPLT(firstCycle.getPltStartLevel());
		
		report.startStep("Press Start PLT");
		classicTest.pressOnStartPLT();
		
		report.startStep("Switch to New Window");
		webDriver.switchToNewWindow();

		webDriver.waitForElement("//*[@id='mainAreaTD']/div[1]", ByTypes.xpath, true, webDriver.getTimeout()); // Right Resource
		webDriver.waitForElement("//*[@id='mainAreaTD']/div[2]", ByTypes.xpath, false, webDriver.getTimeout()); // Left Resource
		
		String initialLevel = firstCycle.getPltStartLevel().toString().substring(0, 2);
		
		report.startStep("Answer First Cycle");
		classicTest.answerCycleQuestions(firstCycle);

		report.startStep("Click Go On Button");
		classicTest.clickOnGoOnButton();

		report.startStep("Answer Second Cycle");
		classicTest.answerCycleQuestions(SecondCycle);
		
		report.startStep("Switch to Main Window");
		webDriver.switchToMainWindow();
		
		report.startStep("Verify Title");
		classicTest.verifyTitlePLT();
		
		report.startStep("Verify Placement Level On Result Page");
		classicTest.verifyPlacementLevelOnResultPagePLT("Intermediate 3");
		
		report.startStep("Check Results in DB");
		//studentId = "52322820301075"; // change to dynamic 
		
		// check results in db
		List<String[]> testResultsPLT = dbService.getTestResultOfPLT_API_test(studentId);
		validateTestResultPLT(testResultsPLT);
		
		// check results in db- grades
		List<String[]> testResultsPLT_Grades = dbService.getTestResultOfPLT_API_test_Grades(studentId);
		checkTestResultGrades(testResultsPLT_Grades);
		
		classicTest.clickOnExitPLT();
		
		report.startStep("Verify the return URL and result");
		String actualreturnUrl = webDriver.getUrl();
		String expectedUrlAndResult = pageHelper.staticUrlForExternalPages + "Languages/viewPLT.html" + "?UserName="+ BasicNewUxTest.userName 
				+"&Final="+testResultsPLT.get(0)[5]
				+"&Reading="+testResultsPLT.get(0)[2].trim()
				+"&Listening="+testResultsPLT.get(0)[3].trim()
				+"&Grammar="+testResultsPLT.get(0)[4].trim();
		
		checkTheReturnUrlAndReturnFinalResultPLT(actualreturnUrl,expectedUrlAndResult);
		// 	https://www.yahoo.com/?UserName=stud263337&Final=I3&Reading=I3&Listening=I3&Grammar=I3
	}

	@Category(ReleaseTests.class)
	@Test
	@TestCaseParams(testCaseID = { "" })
	public void testPLT_API_NewUser() throws Exception {

		report.startStep("Log out");
		homePage.logOutOfED();


		report.startStep("Initialize needed variables");
		//String instName= institutionName; //here you can change the InstitutionName; //"courses";
		String apiToken = dbService.getApiToken(institutionId);
		String className = "IntegrationPLT";
		//String className = "classNewUx";
		boolean newUser = true;

		report.startStep("Open PLT API");
			pageHelper.open_PLT_API(institutionName, className, apiToken,newUser);
			classicTest = new NewUxClassicTestPage(webDriver,testResultService);

		report.startStep("Get institution Remaining License for all Valid Packages");
		List<String[]> getValidInstitutionPackages = dbService.getValidInstitutionPackages(institutionId);

		report.startStep("Click Submit");
		classicTest.clickSubmitOnPLT_API();
		studentId = dbService.getUserIdByUserName(BasicNewUxTest.userName, institutionId); // get here the studentId

		report.startStep("Verify Title");
		classicTest.verifyTitlePLT();

		report.startStep("Verify Level List & Languages & buttons");
		classicTest.verifyLevelListExist();
		classicTest.verifyLanguagesExists();
		classicTest.verifyStartTestButtonsExists();

		report.startStep("Init PLT Test From CSV File");
		PLTTest pltTest = classicTest.initPLTTestFromCSVFile("files/PLTTestData/PLT_ED_Answers_NEW.csv", true);

		PLTCycle firstCycle = pltTest.getCycles().get(0);
		PLTCycle SecondCycle = pltTest.getCycles().get(1);

		report.startStep("Select Level");
		classicTest.selectLevelOnStartPLT(firstCycle.getPltStartLevel());

		report.startStep("Press Start PLT");
		classicTest.pressOnStartPLT();

		report.startStep("Switch to New Window");
		webDriver.switchToNewWindow();

		webDriver.waitForElement("//*[@id='mainAreaTD']/div[1]", ByTypes.xpath, true, webDriver.getTimeout()); // Right Resource
		webDriver.waitForElement("//*[@id='mainAreaTD']/div[2]", ByTypes.xpath, false, webDriver.getTimeout()); // Left Resource

		String initialLevel = firstCycle.getPltStartLevel().toString().substring(0, 2);

		report.startStep("Answer First Cycle");
		classicTest.answerCycleQuestions(firstCycle);

		report.startStep("Click Go On Button");
		classicTest.clickOnGoOnButton();

		report.startStep("Answer Second Cycle");
		classicTest.answerCycleQuestions(SecondCycle);

		report.startStep("Switch to Main Window");
		webDriver.switchToMainWindow();

		report.startStep("Verify Title");
		classicTest.verifyTitlePLT();

		report.startStep("Verify Placement Level On Result Page");
		classicTest.verifyPlacementLevelOnResultPagePLT("Intermediate 3");

		report.startStep("Check Results in DB");
		//studentId = "52322820301075"; // change to dynamic

		// check results in db
		List<String[]> testResultsPLT = dbService.getTestResultOfPLT_API_test(studentId);
		validateTestResultPLT(testResultsPLT);

		// check results in db- grades
		List<String[]> testResultsPLT_Grades = dbService.getTestResultOfPLT_API_test_Grades(studentId);
		checkTestResultGrades(testResultsPLT_Grades);

		classicTest.clickOnExitPLT();

		report.startStep("Verify the return URL and result");
		String actualreturnUrl = webDriver.getUrl();
		String expectedUrlAndResult = pageHelper.staticUrlForExternalPages + "Languages/viewPLT.html" + "?UserName="+ BasicNewUxTest.userName
				+"&Final="+testResultsPLT.get(0)[5]
				+"&Reading="+testResultsPLT.get(0)[2].trim()
				+"&Listening="+testResultsPLT.get(0)[3].trim()
				+"&Grammar="+testResultsPLT.get(0)[4].trim();

		checkTheReturnUrlAndReturnFinalResultPLT(actualreturnUrl,expectedUrlAndResult);
		// 	https://www.yahoo.com/?UserName=stud263337&Final=I3&Reading=I3&Listening=I3&Grammar=I3
	}

	@Test
	@TestCaseParams(testCaseID = { "49552" })
	public void testPLT_API_WrongParam() throws Exception {

		String baseUrl = pageHelper.CILink.split(".com")[0] + ".com/";
		String filePath = pageHelper.buildPathForExternalPages + "Languages/";
		String accessFile = pageHelper.urlForExternalPages.split(".com")[0] + ".com/" + "Languages/";
		String testFile = "PLT_Post_" + dbService.sig(4) + ".html";

		String[] user = getGeneralUser();
		String chkUser =  user[0];

		String chkLang = "English";
		String chkToken = dbService.getApiToken(institutionId);

		createViewPLTfile(filePath, "viewPLT.html");
		String chkReURL = accessFile + "viewPLT.html?";

		report.startStep("Test empty Institution param");
		testWrongParamPLT(filePath, testFile, baseUrl, institutionName, chkToken, chkUser, chkLang, chkReURL, "emptyInst",accessFile);

		report.startStep("Test not exist Institution param");
		testWrongParamPLT(filePath, testFile, baseUrl, institutionName, chkToken, chkUser, chkLang, chkReURL, "errInst",accessFile);

		report.startStep("Test empty Token param");
		testWrongParamPLT(filePath, testFile, baseUrl, institutionName, chkToken, chkUser, chkLang, chkReURL, "emptyToken",accessFile);

		report.startStep("Test error Token param");
		testWrongParamPLT(filePath, testFile, baseUrl, institutionName, chkToken, chkUser, chkLang, chkReURL, "errToken",accessFile);

		report.startStep("Test empty User param");
		testWrongParamPLT(filePath, testFile, baseUrl, institutionName, chkToken, chkUser, chkLang, chkReURL, "emptyUser",accessFile);

		report.startStep("Close browser");
		webDriver.closeBrowser();

	}
	

	// This test answer one course test (midterm or final) by test id
	// The only variables needed to be initialized are : wantedTestId
	@Test
	@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "" })
	public void testPerformCourseTestOldTE1() throws Exception {
		//newTE=false;
		
		// Initialize Test Environment Page
		TestEnvironmentPage testEnvironmentPage = new TestEnvironmentPage(webDriver, testResultService);	
		
		report.startStep("Init Data");
		String wantedTestId = "989013148";
		//String instId = dbService.getInstituteIdByName("automation");
		
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
		String courseId = getCourseIdByCourseCode(courseCode);
		
		//report.startStep("LogOut");
		//homePage.logOutOfED();
		
		//report.startStep("Turn Off Test Enviorment FLAG");
		//dbService.turnOffTestEnviormentFlag(instId);
		
		report.startStep("Assign Course Test to student");
		pageHelper.assignAutomatedTestToStudetInTestEnvironment(studentId, courseId, testTypeId,0,0,1);
		sleep(2);
		String testId = pageHelper.getAssignedTestIdForStudent(studentId,courseId, testTypeId);
		if (!testId.equals(wantedTestId)) {
			// update test id
			String userExitSettingsId = dbService.getUserExitSettingsId(studentId, testId, courseId, impType);
			dbService.updateTestIdInUserExitSettings(wantedTestId, userExitSettingsId);
		}
		sleep(2);
		
		/*report.startStep("Log In as Student");
		String userName = dbService.getUserNameById(studentId, instId);
		NewUXLoginPage loginPage = new NewUXLoginPage(webDriver, testResultService);
		homePage = loginPage.loginAsStudent(userName, "12345");
		homePage.waitHomePageloaded();
		pageHelper.closeLastSessionImproperLogoutAlert();*/
		
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
			String[] dbResultScore = dbService.getStudentFinalTestData(studentId, courseId, wantedTestId, testTypeId);
			String DbScore[] = dbResultScore[1].split("\\.");
			
			testResultService.assertEquals(score, DbScore[0],"ED Test result score not match to DB test report score");
			testResultService.assertEquals("1", dbResultScore[2],"CT Completed is not 1");
		
		webDriver.switchToTopMostFrame();
		sleep(2);
		webDriver.switchToFrame(webDriver.waitForElement("oed__iframe", ByTypes.className,false,webDriver.getTimeout()));
		
		webDriver.waitForElement("cboxClose", ByTypes.id).click();
		webDriver.switchToMainWindow();
		sleep(1);
		
		report.startStep("Log Out");
		homePage.logOutOfED();
	
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
	
	public void checkTestResultGrades(List<String[]> testResultsPLT_Grades) throws Exception{
		testResultService.assertEquals(6, testResultsPLT_Grades.size(), "Num of records is not 6");
		testResultService.assertEquals("100", testResultsPLT_Grades.get(0)[4], "Grade in Record 1 is Incorrect.");
		testResultService.assertEquals("100", testResultsPLT_Grades.get(1)[4], "Grade in Record 2 is Incorrect.");
		testResultService.assertEquals("100", testResultsPLT_Grades.get(2)[4], "Grade in Record 3 is Incorrect.");
		testResultService.assertEquals("0", testResultsPLT_Grades.get(3)[4], "Grade in Record 4 is Incorrect.");
		testResultService.assertEquals("0", testResultsPLT_Grades.get(4)[4], "Grade in Record 5 is Incorrect.");
		testResultService.assertEquals("0", testResultsPLT_Grades.get(5)[4], "Grade in Record 6 is Incorrect.");
	}
	
	public void open_PLT_API(String institutionName) throws Exception{

		String baseUrl = pageHelper.CILink.split(".com")[0] + ".com/";
		String accessToSite = pageHelper.urlForExternalPages.split(".com")[0] + ".com/" + "Languages/";
		String filePath = pageHelper.buildPathForExternalPages + "Languages/";
				
		//accessToSite = pageHelper.urlForExternalPages + "Languages/";
		
		String instId = dbService.getInstituteIdByName(institutionName);
		String apiToken = dbService.getApiToken(instId);
		
		String testFile = "pltapi.html";
		expectedUrl = "https://www.yahoo.com/";
		
		//apiplt1
		List<String> html = generateHTML(baseUrl + "PlacementTestEntry.aspx",institutionName,apiToken);
		
		textService.writeListToSmbFile(filePath+testFile, html, netService.getDomainAuth());
		
		String openUrl = accessToSite + testFile;

		webDriver.openUrl(openUrl);
		sleep(2);	
	}
	
	public List<String> generateHTML(String path,String instName,String apiToken) throws Exception{
		String[] user = null;
		boolean getUser=true;
		
		
		String className = configuration.getProperty("classname");
		try {
			
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
		wList.add("\t\t<input type=\"hidden\" name=\"IName\" value=\""+instName+"\">");
		wList.add("\t\t<input type=\"hidden\" name=\"Token\" value=\""+apiToken+"\">");
		wList.add("\t\t<input type=\"hidden\" name=\"UserName\" value=\"" + userName + "\"/>");
		wList.add("\t\t<input type=\"hidden\" name=\"Language\" value=\"English\"/>");
		wList.add("\t\t<input type=\"hidden\" name=\"ReturnUrl\"  value=\"" +expectedUrl+ "\">");
		wList.add("\t\t<input type=\"hidden\" name=\"Proctoring\" value=\"false\"/>");
		wList.add("\t\t<input type=\"hidden\" name=\"PushNotification\" value=\"http://lib.ru\"/>");
		wList.add("\t\t<input type=\"submit\" name=\"btnSubmit\"/>");
		wList.add("\t</form>");
	
		wList.add("</body>");
		wList.add("</html>");
		
		return wList;
	}

	private void testWrongParamPLT(String chkPath, String testFile, String baseUrl,
								   String chkInst, String chkToken, String chkUser, String chkLang,
								   String chkReURL, String errType,String accessFile) throws Exception {

		String chkErrState = "";
		switch (errType) {
			case "emptyInst":
				chkInst = "";
				chkErrState = "err=1";
				break;
			case "errInst":
				chkInst = "xxx1";
				chkErrState = "err=1";
				break;
			case "emptyToken":
				chkToken = "";
				chkErrState = "err=2";
				break;
			case "errToken":
				chkToken += "A";
				chkErrState = "err=2";
				break;
			case "emptyUser":
				chkUser = "";
				chkErrState = "err=3";
				break;
		}
	}

	private void createViewPLTfile(String chkPath, String viewFile) throws Exception
	{
		List<String> wList = new ArrayList<String>();

		wList = createHtmlTitle(wList, true);
		wList = createHtmlTail(wList);

		textService.writeListToSmbFile(chkPath + viewFile, wList, netService.getDomainAuth());
	}

	private List<String> createHtmlTitle(List<String> wList, boolean bJsScript)
	{
		wList.add("<!DOCTYPE html>");
		wList.add("<html>");
		wList.add("<head>");
		wList.add("\t<meta charset=\"utf-8\" />");
		wList.add("\t<title></title>");

		if(bJsScript) { wList = createJsScript(wList); }

		wList.add("</head>");
		wList.add("<body>");

		if(bJsScript) {
			wList.add("<span id=\"user\"></span>");
			wList.add("<br/><br/>");
			wList.add("<span id=\"err\"></span>");
		}

		return  wList;
	}

	private List<String> createHtmlTail(List<String> wList)
	{
		wList.add("</body>");
		wList.add("</html>");

		return  wList;
	}

	private List<String> createJsScript(List<String> wList)
	{
		wList.add("<script type=\"text/javascript\" src=\"/Runtime/JavaScript/jquery-1.8.2.min.js\"></script>");
		wList.add("<script type=\"text/javascript\">");
		wList.add("\t$(document).ready(function () \n\t { $('#user').text(\"user=\" + qs('Username')); \n\t  $('#err').text(\"err=\" + qs('error')); \n\t });");

		wList.add("\n\t function qs(key) {");
		wList.add("\n\t key = key.replace(/[*+?^$.\\[\\]{}()|\\\\/]/g, \"\\$&\");"); // escape RegEx meta chars
		wList.add("\n\t var match = location.search.match(new RegExp(\"[?&]\" + key + \"=([^&]+)(&|$)\"));");
		wList.add("\n\t return match && decodeURIComponent(match[1].replace(/\\+/g, \" \"));");
		wList.add("\n\t }");

		wList.add("</script>");

		return  wList;
	}

	public void checkTheReturnUrlAndReturnFinalResultPLT(String actualreturnUrl,String expectedUrlAndResult) {
		
		testResultService.assertEquals(actualreturnUrl,expectedUrlAndResult, "Return URL or Result is incorrect");
	}


	@After
	public void tearDown() throws Exception {
			institutionName="";
			useCEFR = false;
			super.tearDown();
			//webDriver.closeBrowser();	
	}
}
