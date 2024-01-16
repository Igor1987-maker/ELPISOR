package tests.edo.newux;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.server.handler.FindElement;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import Enums.ByTypes;
import Enums.StepProgressBox;
import Enums.TaskTypes;
import pageObjects.DragAndDropSection;
import pageObjects.edo.NewUXLoginPage;
import pageObjects.edo.NewUxCommunityPage;
import pageObjects.edo.NewUxDragAndDropSection2;
import pageObjects.edo.NewUxLearningArea;
import pageObjects.edo.NewUxLearningArea2;
import pageObjects.edo.NewUxMyProgressPage;
import services.PageHelperService;
import testCategories.AngularLearningArea;
import testCategories.fullProgressInClient;
import testCategories.inProgressTests;
import testCategories.unstableTests;
import drivers.FirefoxWebDriver;
import drivers.GenericWebDriver;
import Interfaces.TestCaseParams;

@Category(AngularLearningArea.class)
public class CheckProgressTests2 extends BasicNewUxTest {
	
	NewUXLoginPage loginPage;
	NewUxMyProgressPage myProgress;
	public static NewUxLearningArea2 learningArea2;
	public static NewUxDragAndDropSection2 dragAndDrop2;
	
	
	@Before
	public void setup() throws Exception {
		
	//	for (int i=0;i<4;i++){
		super.setup();
		
		// Teacher DashBoard
	/*			
		BasicNewUxTest.institutionName= institutionsName[9];
		String className = configuration.getProperty("classname.tBoard");
		pageHelper.restartBrowserInNewURL(institutionName, true); //initializeData();
*/
		
		// general institution
		BasicNewUxTest.institutionName= institutionsName[0];
		String className = configuration.getProperty("classname");
			
		report.startStep("Get user and login");
		createUserAndLoginNewUXClass(className, institutionId);
		homePage.closeAllNotifications();
		homePage.waitHomePageloadedFully();
	}
	
	
	
	@Test
	@TestCaseParams(testCaseID = { "21777", "40221" })
	public void testZeroProgressInUnit() throws Exception {

		report.startStep("Init test data");
		int unitNumber = 1;
		String expUnitTooltip = "Unit Completion: ";
		String expUnitTooltipValue = "0%";
		String expUnitTooltipFull = expUnitTooltip + expUnitTooltipValue;
		double expUnitProgress = 0.0;
		
		report.startStep("Create user and login");
	//	createUserAndLoginNewUXClass();
				
		report.startStep("Navigate to Basic 1");
		homePage.navigateToRequiredCourseOnHomePage(coursesNames[1]);
		homePage.scrollToUnitProgressBar(unitNumber-1);
		
		report.startStep("Get unit data on Home Page");
		String expUnitName = homePage.getUnitNameByIndex(unitNumber);
		double progressValue = homePage.getUnitProgressBarValue(unitNumber-1);
		String unitProgressTooltip = homePage.getUnitProgressToolTip(unitNumber-1);
				
		report.startStep("Check Unit progress indications on Home Page");
		testResultService.assertEquals(expUnitProgress, progressValue, "Progress value on Home Page is not correct");
		testResultService.assertEquals(expUnitTooltipFull, unitProgressTooltip, "Progress value on Home Page and Tooltip are different");
				
		report.startStep("Check Unit progress indications on My Progress page");
		webDriver.scrollToTopOfPage();
		myProgress = homePage.clickOnMyProgress();
		sleep(1);
		
		verifyUnitDataOnMyProgressPage(unitNumber, expUnitProgress, expUnitTooltipFull, expUnitName);
				
	}

	@Test
	@TestCaseParams(testCaseID = { "21779", "40221" }, skippedBrowsers = { "safariMac" })
	public void testProgressCloseToOneHundred() throws Exception {
		
		report.startStep("Init test data");
		int courseIndex = 0;
		int unitNumber = 1;
						
	//	report.startStep("Create new user");
	//	String studentId = pageHelper.createUSerUsingSP();
		
		report.startStep("Create progress in FD Unit 1");
		learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "FD", 1, 1);
		
		String unitId = dbService.getCourseUnits(courses[courseIndex]).get(unitNumber-1);
		studentService.setProgressForUnit(unitId, courses[courseIndex], studentId, 7);
		
	//	report.startStep("Login and check the progress of the unit");
		homePage.clickOnHomeButton();
		homePage.waitHomePageloadedFully();
//		loginAsStudent(studentId);
		homePage.scrollToUnitProgressBar(unitNumber-1);
		
		String expUnitName = homePage.getUnitNameByIndex(unitNumber);
		String expUnitTooltipFull = homePage.getUnitProgressToolTip(unitNumber-1);
		double progressValue = homePage.getUnitProgressBarValue(unitNumber-1);
		double expectedProgress = studentService.calcUnitExpectedProgress(unitId, studentId);
		expectedProgress = roundExpectedProgress(expectedProgress, 2).doubleValue();
		
		testResultService.assertEquals(expectedProgress, progressValue, "Progress value is not correct on Home Page");
		
		report.startStep("Check Unit progress indications on My Progress page");
		webDriver.scrollToTopOfPage();
		myProgress = homePage.clickOnMyProgress();
		sleep(1);
		verifyUnitDataOnMyProgressPage(unitNumber, expectedProgress, expUnitTooltipFull, expUnitName);
		
		report.startStep("Init test data");
		courseIndex = 4;
		unitNumber = 4;		
		
		report.startStep("Create progress to I1 Unit 4");
		unitId = dbService.getCourseUnits(courses[courseIndex]).get(unitNumber-1);
		studentService.setProgressForUnit(unitId, courses[4], studentId, 12);
		homePage.clickOnLogOut();
		homePage.waitToLoginArea();
		webDriver.switchToTopMostFrame();
		
		report.startStep("Login and check the progress of the unit");		
		loginAsStudent(studentId);
		homePage.closeAllNotifications();
		homePage.scrollToUnitProgressBar(courseIndex);

		expUnitName = homePage.getUnitNameByIndex(unitNumber);
		expUnitTooltipFull = homePage.getUnitProgressToolTip(unitNumber-1);
		progressValue = homePage.getUnitProgressBarValue(unitNumber-1);
		expectedProgress = studentService.calcUnitExpectedProgress(unitId, studentId);
		expectedProgress = roundExpectedProgress(expectedProgress, 2).doubleValue();
		
		testResultService.assertEquals(expectedProgress, progressValue, "Progress value is not correct on Home Page");
		
		report.startStep("Check Unit progress indications on My Progress page");
		webDriver.scrollToTopOfPage();
		myProgress = homePage.clickOnMyProgress();
		sleep(1);
		verifyUnitDataOnMyProgressPage(unitNumber, expectedProgress, expUnitTooltipFull, expUnitName);

	}

	@Test
	@TestCaseParams(testCaseID = { "21984" })
	public void testLessonWithNoCompletionHP() throws Exception {
		
		report.startStep("Create user and login");
	//	createUserAndLoginNewUXClass();

		report.startStep("Check FD - U1 - L1 indicates no progress");
		homePage.clickOnUnitLessons(1);
		sleep(3);
		
		homePage.checkProgressIndicationInLessonList(1, 0);
		
	}

	@Test
	@TestCaseParams(testCaseID = { "21985" })
	public void testLessonWithSomeProgressHP() throws Exception {
		
		report.startStep("Create progress for some of the lessons in B1, unit 1, lesson 1");
		
		learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "B1", 1, 1);
		
		String courseId = courses[1];
		String unitId = dbService.getCourseUnits(courseId).get(0);
		String componentId = dbService.getComponentDetailsByUnitId(unitId).get(0)[1];
		studentService.setProgressForComponents(componentId, courseId,studentId, null, 60, 2, false);
		studentService.setUserCourseUnitProgress(courseId, unitId, componentId);
		
		homePage.clickOnHomeButton();
		homePage.waitHomePageloadedFully();
		
		report.startStep("Check B1 - U1 - L1 indicates half progress");
		homePage.clickOnUnitLessons(1);
		sleep(3);
		homePage.checkProgressIndicationInLessonList(1, 1);
	}

	@Test
	@TestCaseParams(testCaseID = { "21986" })
	public void testLessonWithFullCompletionHP() throws Exception {
		
		learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "B1", 1, 1);
		
		report.startStep("Create progress for some of the lessons in FD, unit 1, lesson 1");
		String courseId = courses[0];
		String unitId = dbService.getCourseUnits(courseId).get(0);
		String componentId = dbService.getComponentDetailsByUnitId(unitId).get(0)[1];
		studentService.setProgressForComponents(componentId, courseId,studentId, null, 60, 0, true);
		studentService.setUserCourseUnitProgress(courseId, unitId, componentId);
		
		homePage.clickOnHomeButton();
		homePage.waitHomePageloadedFully();
		
		report.startStep("Check FD - U1 - L1 indicates full progress");
		homePage.clickOnUnitLessons(1);		
		homePage.checkProgressIndicationInLessonList(1, 100);

	}
	
	@Test
	@TestCaseParams(testCaseID = { "22987","40255" })
	public void testGetProgressInLessonList() throws Exception {
		
		learningArea2 = new NewUxLearningArea2(webDriver, testResultService);
		
		report.startStep("Init test data");
		int courseIndex = 1; // Basic 1
		int unitNumber = 3;
		int lesson1 = 1;
		int lesson2 = 2;
		int lesson3 = 3;
		int lesson3_NotStarted = 0;
				
		learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "B1", 1, 1);
		
		report.startStep("Get Unit details for crerating progress in B1-U1");
		String courseId = courses[courseIndex];
		String unitId = dbService.getCourseUnits(courseId).get(unitNumber-1);
	
		report.startStep("Create 100% progress for lesson 1 in B1-U3");
		String componentId = dbService.getComponentDetailsByUnitId(unitId).get(lesson1-1)[1];
		studentService.setProgressForComponents(componentId, courseId, studentId, null, 60, 0, true);
		studentService.setUserCourseUnitProgress(courseId, unitId, componentId);
		
		double lesson1_exp = studentService.calcComponentExpectedProgress(componentId, studentId);
		int lesson1_Completed = (int) Math.round(lesson1_exp*100);

		report.startStep("Create 30% progress for lesson 2 in B1-U3");
		componentId = dbService.getComponentDetailsByUnitId(unitId).get(lesson2-1)[1];
		String subCompId = dbService.getSubComponentsDetailsByComponentId(componentId).get(0)[1];
		studentService.setProgressForSubComponent(subCompId, studentId, courseId, 60, dbService.getSubComponentItems(subCompId), null);
		studentService.setUserCourseUnitProgress(courseId, unitId, componentId);
		double lesson2_exp = studentService.calcComponentExpectedProgress(componentId, studentId);
		int lesson2_InProgress = (int) Math.round(lesson2_exp*100);
		
		homePage.clickOnHomeButton();
		homePage.waitHomePageloaded();
		
		report.startStep("Navigate to B1");
		homePage.navigateToRequiredCourseOnHomePage(coursesNames[courseIndex]);
		
		report.startStep("Check Unit progress indications on My Progress page");
		myProgress = homePage.clickOnMyProgress();
		sleep(1);
		myProgress.clickToOpenUnitLessonsProgress(unitNumber);
		sleep(1);
		myProgress.checkProgressStatusInLessonList(lesson1, lesson1_Completed);
		myProgress.checkProgressStatusInLessonList(lesson2, lesson2_InProgress);
		myProgress.checkProgressStatusInLessonList(lesson3, lesson3_NotStarted);
		homePage.clickToOpenNavigationBar();
		homePage.clickOnHomeButton();
		homePage.waitHomePageloadedFully();
		
		report.startStep("Navigate to LA of B1-U1");
		homePage.clickOnContinueButton();
		learningArea2.waitForPageToLoad();
		
        report.startStep("Open lesson list and check progress indication");
        learningArea2.openLessonsList();
        sleep(1);
        learningArea2.checkProgressIndicationInLessonList(lesson1, lesson1_Completed);
        learningArea2.checkProgressIndicationInLessonList(lesson2, lesson2_InProgress);
        learningArea2.checkProgressIndicationInLessonList(lesson3, lesson3_NotStarted);
                
        
	}
	
	@Test
	@TestCaseParams(testCaseID = { "41404" })
	public void testGetProgressInStepList() throws Exception {
		
		learningArea2 = new NewUxLearningArea2(webDriver, testResultService);
				
		int courseIndex = 2; // Basic 2
		// Test Set 1
		int unit_test_1 = 1;
		int lesson_test_1 = 1;
		List<String[]> steps_test_1;
		// Test Set 2
		int unit_test_2 = 10;
		int lesson_test_2 = 1;
		List<String[]> steps_test_2;
						
		report.startStep("Create student");
	//	String className = configuration.getProperty("classname");
	//	String institutionId = configuration.getProperty("institution.id");
	//	studentId = pageHelper.createUSerUsingSP(institutionId, className);
				
	//	report.startStep("Open New UX Login Page");
	//	loginPage = new NewUXLoginPage(webDriver, testResultService);
		
		report.startStep("Get Unit details for crerating progress in B1-U1");
		String courseId = courses[courseIndex];
		String unitId = dbService.getCourseUnits(courseId).get(unit_test_1-1);
	
		report.startStep("Create progress in Unit 1 - Lesson 1 - Steps (1-2)");
		learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "B1", 1, 1);
		
		String componentId = dbService.getComponentDetailsByUnitId(unitId).get(lesson_test_1-1)[1];
		steps_test_1 = dbService.getSubComponentsDetailsByComponentId(componentId);
		createProgressInLessonStepsExceptTest(steps_test_1, courseId, 2, true);
		
		report.startStep("Submit Test in Unit 1 - Lesson 1 - Step 3");
		String step_id = steps_test_1.get(steps_test_1.size()-1)[1];
		List<String[]>step_items = dbService.getSubComponentItems(step_id);
		studentService.submitTest(studentId, unitId, componentId, step_items, false);
		
		report.startStep("Create progress in Unit 10 - Lesson 1 - Steps (1-5)");
		unitId = dbService.getCourseUnits(courseId).get(unit_test_2-1);
		componentId = dbService.getComponentDetailsByUnitId(unitId).get(lesson_test_2-1)[1];	
		steps_test_2 = dbService.getSubComponentsDetailsByComponentId(componentId);
		createProgressInLessonStepsExceptTest(steps_test_2, courseId, 1, false);	
				
		report.startStep("Submit Test in Unit 10 - Lesson 1 - Step 6");
		step_id = steps_test_2.get(steps_test_2.size()-1)[1];
		step_items = dbService.getSubComponentItems(step_id);
		studentService.submitTest(studentId, unitId, componentId, step_items, false);
						
	//	report.startStep("Login as Student and open My Progress page");
	//	homePage = loginAsStudent(studentId);
	//	sleep(2);
		
		homePage.clickOnHomeButton();
		homePage.waitHomePageloadedFully();
				
		report.startStep("Navigate to LA of B2-U1-L1");
		homePage.navigateToRequiredCourseOnHomePage(coursesNames[courseIndex]);
		homePage.clickOnUnitLessons(unit_test_1);
		homePage.clickOnLesson(unit_test_1-1, lesson_test_1);
				
        report.startStep("Open step list and check progress indication");
        learningArea2.openStepsList();
        sleep(1);
        verifyStepsProgressExceptTest(steps_test_1, true);
        learningArea2.checkProgressIndicationInStepList(3, StepProgressBox.empty, true, "100%");
        
        learningArea2.closeStepsList();
       		
		report.startStep("Navigate to LA of B2-U10_1");
		//homePage.clickToOpenNavigationBar();
		sleep(1);
		homePage.clickOnHomeButton();
		homePage.waitHomePageloaded();
        homePage.clickOnUnitLessons(unit_test_2);
		homePage.clickOnLesson(unit_test_2-1, lesson_test_2);
		sleep(5);
		
        report.startStep("Open step list and check progress indication");
        learningArea2.openStepsList();
        sleep(1);
        
        report.startStep("Verify step progress indication in Unit 10 - Lesson 1");
		verifyStepsProgressExceptTest(steps_test_2, false);
		learningArea2.checkProgressIndicationInStepList(steps_test_2.size(), StepProgressBox.done, true, "100%");
	}
	
	@Test
	@TestCaseParams(testCaseID = { "21737" })
	public void testDefaultViewInDiffCourses() throws Exception {
		
		report.startStep("Create user");
		//studentId = pageHelper.createUSerUsingSP(configuration.getInstitutionId(), configuration.getClassName());
		
		report.startStep("Init test data");
		int indexFD = 0;
		int indexB3 = 3;
		int indexA2 = 8;
				
		report.startStep("Create progress in FD-U8-L6");
		
		report.startStep("Navigate FD-U1-L1-T1");
		learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "FD", 8, 6);
		
			String courseId = courses[indexFD];
			String unitId = dbService.getCourseUnits(courseId).get(7);
			String componentId = dbService.getComponentDetailsByUnitId(unitId).get(5)[1];
			studentService.setProgressForComponents(componentId, courseId, studentId, null, 60, 6, false);
			studentService.setUserCourseUnitProgress(courseId, unitId, componentId);
			
			homePage.clickOnHomeButton();
			homePage.waitHomePageloadedFully();
			
			
		report.startStep("Create progress in B3-U2-L3");
		learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "B3", 2, 3);
		
			courseId = courses[indexB3];
			unitId = dbService.getCourseUnits(courseId).get(1);
			componentId = dbService.getComponentDetailsByUnitId(unitId).get(2)[1];
			studentService.setProgressForComponents(componentId, courseId, studentId, null, 60, 6, true);
			studentService.setUserCourseUnitProgress(courseId, unitId, componentId);
			
			homePage.clickOnHomeButton();
			homePage.waitHomePageloadedFully();
			
		report.startStep("Create progress in A2-U1-L5");
			
			learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "A2", 1, 5);
			courseId = courses[indexA2];
			unitId = dbService.getCourseUnits(courseId).get(0);
			componentId = dbService.getComponentDetailsByUnitId(unitId).get(4)[1];
			studentService.setProgressForComponents(componentId, courseId, studentId, null, 60, 6, false);
			studentService.setUserCourseUnitProgress(courseId, unitId, componentId);
		
			
	/*	report.startStep("Login with user");
			NewUXLoginPage loginPage = new NewUXLoginPage(webDriver,testResultService);
			homePage = loginPage.loginAsStudent(dbService.getUserNameById(studentId, configuration.getInstitutionId()), "12345");
			homePage.waitHomePageloaded();		
	*/
	//	webDriver.refresh();
			homePage.clickOnHomeButton();
			homePage.waitHomePageloadedFully();
			
		report.startStep("Navigate to FD and check titles in Course Area");
	        homePage.navigateToRequiredCourseOnHomePage(coursesNames[indexFD]);
	        sleep(2);
	        homePage.waitHomePageloadedFully();
	        homePage.checkUnitLessonNameInCourseArea("Unit 8: Going Out", "Have Fun!");
        
        report.startStep("Navigate to B3 and check titles in Course Area");
	        homePage.navigateToRequiredCourseOnHomePage(coursesNames[indexB3]);
	        sleep(2);
	        //homePage.checkUnitLessonNameInCourseArea("Unit 2: Business Matters", "Fax It");
	        homePage.checkUnitLessonNameInCourseArea("Unit 2: Business Matters", "Modals: Must");
        
        report.startStep("Navigate to A2 and check titles in Course Area");
	        homePage.navigateToRequiredCourseOnHomePage(coursesNames[indexA2]);
	        sleep(2);
	        homePage.checkUnitLessonNameInCourseArea("Unit 1: Telling The Truth", "Logical Connectors: Contrast");
                
        
	}

	@Test
	@TestCaseParams(testCaseID = { "21987" })
	public void testLoackedLessonProgress() throws Exception {

		String className = configuration.getProperty("classname.locked");
//		String studentId = pageHelper.createUSerUsingSP(
//				configuration.getInstitutionId(), className);

		String courseId = courses[0];

		try {
			report.startStep("Create progress for some of the lessons in FD, unit 1, lesson 1");

			learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "FD", 1, 1);
			
			String unitId = dbService.getCourseUnits(courseId).get(0);
			String componentId = dbService.getComponentDetailsByUnitId(unitId)
					.get(0)[1];
			studentService.setProgressForComponents(componentId, courseId,studentId, null, 60, 0, true);
			studentService.setUserCourseUnitProgress(courseId, unitId, componentId);
			
			report.startStep("Lock lesson");

			pageHelper.LockLessonToClass(className, courseId, 1, 1);

//			report.startStep("Create user and login");
//			loginAsStudent(studentId);

			homePage.clickOnHomeButton();
			homePage.waitHomePageloadedFully();
			
			report.startStep("Check locked lesson progress");

			homePage.clickOnUnitLessons(1);
			String progress = homePage.getLessonProgress(0, 1);
			testResultService.assertEquals("\"full\"", progress,
					"Lesson progress class is not correct");

			if (!testResultService.assertEquals(true,
					homePage.getLessonLockStatus(0, 1))) {
				webDriver.printScreen("Locked icon not displayed");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block

		} finally {
			pageHelper.UnlockCourseUnitLessonsToClass(className, courseId, 1);
		}

	}

	@Test
	@TestCaseParams(testCaseID = { "22067" }, testTimeOut="15")
	public void testMaxProgressInCourse() throws Exception {
		
		
		report.startStep("Set full progress for all units in course Basic 3");
		learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "B3", 1, 1);
		
		String courseId = courses[3];
		//studentService.setProgressForCourse(courseId, studentId, null, 60,true, true);
		studentService.setProgressForCourse(courseId, studentId, null, 60,false,true);
		//studentService.submitTestsForCourse(studentId, courseId, true);
		sleep(1);
		
		report.startStep("Back to Home Page");
		homePage.clickOnHomeButton();
		homePage.waitHomePageloadedFully();
		
		report.startStep("Navigate back to course that progress done");
		homePage.carouselNavigateBack();
		homePage.waitHomePageloadedFully();

		report.startStep("Check all units progress bar");
		List<String> units = dbService.getCourseUnits(courseId);
		
		report.startStep("Get calculated units progress from DB and verify with unit Progress bar");
		
		List<String[]> unitsProgress = dbService.getUserUnitProgressAndTestAverage(studentId,courseId);
		testResultService.assertEquals(units.size(), unitsProgress.size(), "Progress unit count is not correct");
		double progressFromDB;
		
		sleep(1);
		
		for (int i = 1; i < units.size(); i++) {
			
			double displayedUnitProgress = homePage.getAllUnitsBarUnitProgress(i+1);
			
			report.startStep("Iteration number "+i);
			//double expectedUnitPrgress = studentService.calcUnitExpectedProgress(units.get(i), studentId);
			//double expectedUnitProgressInBar = expectedUnitPrgress / units.size();
			//expectedUnitProgressInBar = textService.round(expectedUnitProgressInBar, 4);
			try{
				progressFromDB = Double.parseDouble(unitsProgress.get(i-1)[0]);
				testResultService.assertEquals(progressFromDB, displayedUnitProgress*10, "Progress of unit is not correct");
				}
			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
	
			if (!(webDriver instanceof FirefoxWebDriver)) {
				String bgcolor = homePage.getAllUnitsUnitColor(i+1);
				pageHelper.checkColor(bgcolor, HexUnitColors[i]);
				
			}
		}
	}

	@Test
	@TestCaseParams(testCaseID = { "22064","21778","21783","40221" }, testTimeOut = "30")
	public void testAllUnitMaxProgressInUnit() throws Exception {
		
		report.startStep("Init test data");
		int unitNumber = 2;
		String expUnitTooltip = "Unit Completion: ";
		String expUnitTooltipValue = "100%";
		String expUnitTooltipFull = expUnitTooltip + expUnitTooltipValue;
		double expUnitProgress = 100.0;
		
		report.startStep("Navigate B1-U1-L1-T1");
		learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "B1", 1, 1);

		report.startStep("Set full progress for unit 2 of Basic 1");
		String courseId = courses[1];
		String unitId = dbService.getCourseUnits(courseId).get(unitNumber-1);
		studentService.setProgressForUnit(unitId, courseId, studentId, null,60, 0, true, true);

		report.startStep("check the All units progress bar");
		//webDriver.refresh();
		homePage.clickOnHomeButton();
		homePage.waitHomePageloadedFully();
//		loginAsStudent(studentId);
		
		int displayedUnitProgress = homePage.getAllUnitsBarUnitProgress(unitNumber);
		//double expectedUnitPrgress = studentService.calcUnitExpectedProgress(unitId, studentId);
		int expectedUnitPrgress = getUserUnitProgress(studentId, unitId,courseId);
		int expectedUnitProgressInBar = expectedUnitPrgress/dbService.getCourseUnits(courseId).size();
		//expectedUnitProgressInBar = expectedUnitProgressInBar, 4);

		report.startStep("Check colors");
		testResultService.assertEquals(expectedUnitProgressInBar, displayedUnitProgress, "Progress of unit is not correct on Home Page");
		String bgcolor = homePage.getAllUnitsUnitColor(unitNumber);
		
		if (!(webDriver instanceof FirefoxWebDriver)) {
			pageHelper.checkColor(bgcolor, HexUnitColors[unitNumber-1]);
		}
		
		report.startStep("Check tooltip of all units bar");
		double courseProgress = studentService.calcCourseExpectedProgress(courseId, studentId);
		String allUnitsProgress = homePage.getAllUnitsBarTooltip();
		int result = (int) Math.round(courseProgress);
		testResultService.assertEquals("Course Completion: " + result + "%", allUnitsProgress, "Course progress tooltip is not correct");
				
		report.startStep("Check the Completed text and Unit progress bar");
		homePage.scrollToUnitProgressBar(unitNumber-1);
		String unitText = homePage.getUnitText(unitNumber-1);
		String expUnitName = homePage.getUnitNameByIndex(unitNumber);
		testResultService.assertEquals("Completed!", unitText, "The text Completed! not found");
		double progressValue = homePage.getUnitProgressBarValue(unitNumber-1);
		testResultService.assertEquals(expectedUnitPrgress, progressValue, "Progress value is not correct");

		report.startStep("Checks the Unit's tooltip");
		String unitProgressTooltip = homePage.getUnitProgressToolTip(unitNumber-1);
		testResultService.assertEquals(expUnitTooltipFull, unitProgressTooltip, "Progress value and Tooltip are different");
		
		report.startStep("Check Unit progress indications on My Progress page");
		webDriver.scrollToTopOfPage();
		myProgress = homePage.clickOnMyProgress();
		sleep(1);
		
		bgcolor = myProgress.getUnitProgressColor(unitNumber);
		
		if (!(webDriver instanceof FirefoxWebDriver)) {
			pageHelper.checkColor(bgcolor, HexUnitColors[unitNumber-1]);
		}
		
		verifyUnitDataOnMyProgressPage(unitNumber, expUnitProgress, expUnitTooltipFull, expUnitName);
	}

	private int getUserUnitProgress(String studentId, String unitId,String courseId) {
		
		int unitIdProgress=0;
		try {
			List<String[]> unitsProgress = dbService.getUserUnitProgressAndTestAverage(studentId,courseId);
			
			for (int i=0; i<unitsProgress.size();i++){
				if (unitsProgress.get(i)[2].equalsIgnoreCase(unitId))
					unitIdProgress = (int) Double.parseDouble(unitsProgress.get(i)[0]);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return unitIdProgress;
	}



	@Test
	@TestCaseParams(testCaseID = { "22087" }, allowMedia=true)
	public void testTasksProgressDefaultViewFirstEnter() throws Exception {
		
		report.startStep("Create new user and logjn");
//		createUserAndLoginNewUXClass();
		sleep(2);
		learningArea2 = new NewUxLearningArea2(webDriver,testResultService);
		
		// new int[] [] { {1,0}, {1,1}, {6,0} : array member meaning {numOfTasksInStep, isFirstTaskDoneOnEnter: 1=Done, 0=NotDone}

		report.startStep("Navigate to FD, Unit 1, Lesson 1 and check tasks state");		
		homePage.navigateToCourseUnitAndLesson(courseCodes, "FD", 1, 1);
		validateLessonTasks(3, new int[] [] { {1,0}, {1,1}, {6,0} }, false);
		//homePage.clickToOpenNavigationBar();
		homePage.clickOnHomeButton(true);
		
		report.startStep("Navigate to FD, Unit 3, Lesson 2 and check tasks state");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "FD", 3, 2);
		validateLessonTasks(5, new int[] [] { {1,0}, {1,0}, {3,0}, {1,0}, {1,0} }, false);
		//homePage.clickToOpenNavigationBar();
		homePage.clickOnHomeButton(true);
		
		report.startStep("Navigate to B2, Unit 2, Lesson 3 and check tasks state");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "B2", 2, 3);
		learningArea2.clickOnNextButton();
		validateLessonTasks(3, new int[] [] { {1,0}, {1,0}, {1,0} }, false);
		//homePage.clickToOpenNavigationBar();
		homePage.clickOnHomeButton(true);
		
		report.startStep("Navigate to B3, Unit 9, Lesson 2 and check tasks state");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "B3", 9, 2);
		learningArea2.clickOnNextButton();
		validateLessonTasks(6, new int[] [] { {1,1}, {2,0}, {5,0}, {2,0}, {1,0} ,{5,0} }, true);
		//homePage.clickToOpenNavigationBar();
		homePage.clickOnHomeButton();
		learningArea2.approveTest();
		
		report.startStep("Navigate to A3, Unit 8, Lesson 6 and check tasks state");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "A3", 8, 6);
		learningArea2.clickOnNextButton();
		validateLessonTasks(3, new int[] [] { {1,1}, {10,0}, {15,0} }, true);
			
	}

	@Test
	@TestCaseParams(testCaseID = { "19880" })
	public void testCourseDisplayIfCompleteed() throws Exception {
	//	report.startStep("Create user and set full progress for FD course");
	//	studentId = pageHelper.createUSerUsingSP();
		
		report.startStep("Navigate FD-U1-L1-T1");
		learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "FD", 1, 1);
		
		studentService.setProgressForCourse(courses[0], studentId, null, 60);

	//	report.startStep("Login and check that next course is Basic 1");
	//	loginAsStudent(studentId);

		homePage.clickOnHomeButton();
		homePage.waitHomePageloadedFully();
		
		testResultService
				.assertEquals("Basic 1", homePage.getCourseName());
		testResultService.assertEquals("Unit 1: Meet A Rock Star",
				homePage.getUnitName());

		report.startStep("Navigate to previous course");

		homePage.carouselNavigateBack();
		sleep(2);

		testResultService.assertEquals("Start Over", homePage
				.getCourseContinueButton().getText());

	}
	
	@Test
	@TestCaseParams(testCaseID = { "19881" })
	public void testNoAssignedCourses()throws Exception{
		homePage.logOutOfED();
		
		report.startStep("Create student in class with no assigned courses");
		String className =  configuration.getProperty("classname.nocourses");
		
		createUserAndLoginNewUXClass(className, institutionId);
		homePage.closeAllNotifications();
		homePage.waitHomePageloaded();
		
		report.startStep("Check an asigned courses message");
		homePage.validateUnAssignedCoursesMessage();
	}
	
	@Test
	@TestCaseParams(testCaseID = { "22091" })
	public void testTaskProgressDoNotSaveDoneStateInTest() throws Exception {
		
		report.startStep("Create user and navigate to B1 > Unit 1 > Lesson 1 > Step 3 (Test)");
	//	createUserAndLoginNewUXClass();
		sleep(1);
		learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "B1", 1, 1);
		sleep(1);
		learningArea2.clickOnStep(3, false);
		sleep(1);
		
		report.startStep("Start Test");
		learningArea2.clickOnStartTest();

		report.startStep("Verify 1st Task has Current state");
		learningArea2.openTaskBar();
		learningArea2.checkIfTaskHasCurrentState(1, true);
		
		report.startStep("Click on 2nd Task and verify it has Current state");
		learningArea2.clickOnTaskByNumber(2);
		learningArea2.openTaskBar();
		learningArea2.checkIfTaskHasCurrentState(2, true);
		learningArea2.checkIfTaskHasCurrentState(1, false);
		learningArea2.closeTaskBar();
		
		report.startStep("Navigate to Step 1");
			
		learningArea2.clickOnStep(1);
		learningArea2.approveTest();
		sleep(1);
		
		report.startStep("Go to Test Step again");
		webDriver.switchToTopMostFrame();
		learningArea2.clickOnStep(3, false);
		sleep(1);
		learningArea2.clickOnStartTest();
		sleep(1);
		
		report.startStep("Verify 1st Task has Current state but Not Done");
		learningArea2.openTaskBar();
		learningArea2.checkIfTaskHasCurrentState(1, true);
		learningArea2.checkIfTaskHasDoneState(1, false);
		learningArea2.closeTaskBar();

		report.startStep("Go to Homepage and navigate to B1 > Unit 1 > Lesson 1 > Step 3 (Test)");
		//homePage.clickToOpenNavigationBar();
		homePage.clickOnHomeButton();
		learningArea2.approveTest();
		homePage.waitHomePageloaded();
		sleep(1);
		
		homePage.navigateToCourseUnitAndLesson(courseCodes, "B1", 1, 1);
		sleep(1);
			
		learningArea2.clickOnStep(3, false);
		sleep(1);
		
		report.startStep("Start Test");
		learningArea2.clickOnStartTest();

		report.startStep("Verify 1st Task has Current state but Not Done");
		learningArea2.openTaskBar();
		learningArea2.checkIfTaskHasCurrentState(1, true);
		learningArea2.checkIfTaskHasDoneState(1, false);

	}
	
	@Test
	@Category(fullProgressInClient.class)
	@TestCaseParams(testCaseID = { "22990" }, ignoreTestTimeout = true, allowMedia = true)
	public void testCompleteFullUnitClientSide() throws Exception {
				
		report.startStep("Create user and login");
	//	createUserAndLoginNewUXClass();
		
		report.startStep("Navigate to FD");
		homePage.navigateToRequiredCourseOnHomePage("Basic 1");//("First Discoveries");
		
		report.startStep("Enter Unit 8, make progress and check progress bars");
		makeProgressInLearningAreaByUnitLessons("B1", 8, 5, 9, false);
		webDriver.refresh();
		
		report.startStep("Enter Unit 8 and check progress done");
		checkProgressInLearningAreaByUnitLessons("FD", 1, 1, 7);
	}
	
	// test changed according to new template

	
	@Test
	@TestCaseParams(testCaseID = { "26144" }, allowMedia = true, skippedBrowsers = {"firefox"}) // Actions API not supported by geckodriver)
	public void testSaveDoneStatusInDiffCourses() throws Exception {
				
	
	report.startStep("Navigate to Basic 3");
		homePage.navigateToRequiredCourseOnHomePage("Basic 3");
		homePage.waitHomePageloadedFully();
		
	report.startStep("Enter Unit 1 - Lesson 1, Do action in order to do progress");
		makeProgressInLearningAreaByUnitLessons("B3", 1, 1, 1, false);
		webDriver.scrollToTopOfPage();
		
		
	report.startStep("Navigate to I2"); 
		homePage.navigateToRequiredCourseOnHomePage("Intermediate 2");
		homePage.waitHomePageloadedFully();

	report.startStep("Navigate to Basic 3");
		webDriver.refresh();
		homePage.waitHomePageloadedFully();
		String actualCourseName = homePage.getCurrentCourseName();
		testResultService.assertEquals("Basic 3", actualCourseName, "Last course Name load loaded");
		
		
	report.startStep("Enter Unit 1 - Lesson 1 and check progress done");
		checkProgressInLearningAreaByUnitLessons("B3", 1, 1, 1);
	}
		
	public void makeProgressInLearningAreaByUnitLessons(String courseCode, int unitNumber, int startFromLesson, int stopAfterLesson, boolean checkTaskDone) throws Exception {
		
		NewUxDragAndDropSection2 dragAndDrop2 = new NewUxDragAndDropSection2(webDriver, testResultService);
		NewUxLearningArea2 learningArea2 = new NewUxLearningArea2(webDriver, testResultService);
		
		int courseIndex = homePage.getCourseIndexByCode(courseCodes, courseCode);
		String courseId = courses[courseIndex];
		String courseName = coursesNames[courseIndex];
		List<String[]> units = dbService.getCourseUnitDetils(courseId);
		
		for (int i = (unitNumber-1); i < unitNumber; i++) {
			
			report.startStep("Go over unit: "+ unitNumber + "." + units.get(i)[1]);
			
			List<String[]> components = dbService.getComponentDetailsByUnitId(units.get(i)[0]);
			
			int lessonNumber = startFromLesson; 
						
			//sleep(2);
			homePage.clickOnUnitLessons(unitNumber);
			homePage.clickOnLesson(i, lessonNumber);
			learningArea2.waitToLearningAreaLoaded();
			
			
			for (int j = (lessonNumber-1); j < stopAfterLesson; j++) {	
				report.startStep("Go over lesson: "+ lessonNumber + "."+ components.get(j)[0]);
						
				List<String[]> subComp = dbService.getSubComponentsDetailsByComponentId(components.get(j)[1]);

				boolean isTest = false;
				boolean isInteract = false;
				boolean stepHasIntro = false;
				int stepNumber = 1;
				
				for (int h = 0; h < subComp.size(); h++) {
						
					report.startStep("Navigate to Unit: "+ unitNumber+",Lesson: "+lessonNumber+" Step: " + (h+1));
					
					// define if Step is Interact
					if (subComp.get(h)[0].contains("Practice 1") 
							|| subComp.get(h)[0].contains("Practice 2")
							|| subComp.get(h)[0].contains("Interact 1")
							|| subComp.get(h)[0].contains("Interact 2") ) {
						isInteract = true;
					}
										
					// define if Step is Test
					if (subComp.get(h)[0].contains("Test") || subComp.get(h)[0].contains("Let's review")) {
						isTest = true;
					}
					
					// define if Step has Intro page
					if (learningArea2.isTaskCounterHasIntroMode()) {
						stepHasIntro = true;
					}
										
					if (h > 0) {
						stepNumber = h + 1;
						//sleep(2);
						
						if (isTest) {
							learningArea2.clickOnStep(stepNumber, false);	
							learningArea2.clickOnStartTest();
						} else 	{
							learningArea2.clickOnStep(stepNumber);
							learningArea2.waitToLearningAreaLoaded();
						}
						
						if (isInteract)
							learningArea2.closeAlertModalByAccept();
						
					} else if (stepHasIntro)
						learningArea2.clickOnNextButton();
					

					//report.startStep("Get tasks details from DB");
					//report.addTitle("Get tasks details from DB");
					List<String[]> expectdTasks = dbService.getSubComponentItems(subComp.get(h)[1]);
					int itemTypeId = 0; 
					String itemCode = "undefined";
					
					//report.startStep("Number of tasks: " + expectdTasks.size());
					int taskNumber = 1;					
										
					for (int k = 0; k < expectdTasks.size(); k++) {
										
						itemTypeId = Integer.parseInt(expectdTasks.get(k)[2]);
						itemCode = expectdTasks.get(k)[1];
						
						if (k > 0) {
							learningArea2.clickOnTaskByNumber(taskNumber);
						}
						report.report("Entered to task number: " + (k+1));
						if (h == 1 && taskNumber ==5 && !checkTaskDone){
							// Fix the issue of custom element scroll in close B3,U1, L1, S2,T5 low resolution.
							dragAndDrop2.scrollCustomElement(TaskTypes.Close,100);
						}

						//report.startStep("Making progress in "+courseName +" "+unitNumber +" ,Unit: "+ lessonNumber+" , Lesson: "+stepNumber +" Task: "+taskNumber+" / Checking status of Task " + taskNumber);
						//report.addTitle("Making progress in "+courseName +" ,Unit: "+unitNumber +" ,Lesson: "+ lessonNumber+" "+stepNumber +" Task: "+taskNumber+" / Checking status of Task " + taskNumber);
						if (checkTaskDone && !isTest) {
							if (itemTypeId==21)
								learningArea2.closeAlertModalByAccept();
							
							learningArea2.openTaskBar();
							learningArea2.checkIfTaskHasDoneState(taskNumber, true);
							//sleep(1);
	
						} else { 
							if (!checkTaskDone)
								learningArea2.makeProgressActionByItemType(itemTypeId, itemCode);
							
							learningArea2.openTaskBar();
							//if (isTest)
								learningArea2.checkIfTaskHasCurrentState(taskNumber, true);
							//else
							//	learningArea2.checkIfTaskHasCurrentState(taskNumber, true);
								
						}
									
						if (taskNumber == expectdTasks.size())
							learningArea2.closeTaskBar();
						taskNumber++;														
					}
						
				}
								
				lessonNumber = j + 2;
								
				if (lessonNumber > stopAfterLesson){ 
				 
					if (isTest && checkTaskDone) {
						report.startStep("Submit Test");
						learningArea2.submitTest(true);
					}
					else if (isTest) {
						report.startStep("Submit Test");
						learningArea2.submitTest(false);
					}
					
					report.startStep("Learning Area : open lesson list and check lessons completed");
					learningArea2.openLessonsList();
					learningArea2.checkLessonsCompletedInLessonList(startFromLesson, stopAfterLesson);
					learningArea2.closeLessonsList();
					//sleep(1);
					
					report.startStep("Go back to Home Page");
					//learningArea2.clickToOpenNavigationBar();
					sleep(1);
					learningArea2.clickOnHomeButton();
					homePage.waitHomePageloadedFully();
					homePage.navigateToRequiredCourseOnHomePage(courseName);
					
					report.startStep("Home Page : open unit "+unitNumber+" and check lessons completed");
					homePage.clickOnUnitLessons(unitNumber);
					homePage.checkLessonsCompletedInLessonList(startFromLesson, stopAfterLesson);
										
					report.startStep("Check Unit "+unitNumber+" Progress Bar");
					double unitProgress = homePage.getUnitProgressBarValue(unitNumber-1);
					double actualUnitProgress = Math.round(unitProgress); 
					double expectedUnitProgress = Math.round(100.0 / components.size() * (stopAfterLesson-startFromLesson+1));
					testResultService.assertEquals(expectedUnitProgress, actualUnitProgress,"Progress in Unit Progress Bar is not correct");
					
					report.startStep("Check All Units Progress Bar");
					unitProgress = homePage.getAllUnitsBarUnitProgress(unitNumber);
					actualUnitProgress = Math.round(unitProgress); // roundExpectedProgress(unitProgress, 0).intValue();
					expectedUnitProgress = Math.round(expectedUnitProgress / units.size()); 
					testResultService.assertEquals(expectedUnitProgress, actualUnitProgress, "Progress of Unit in All Units Bar is not correct");
				
					report.startStep("Check Course Completion widget");
					String courseCompletionProgress = homePage.getCompletionWidgetValue();
					int expectedCourseProgressInt = (int)(expectedUnitProgress);
					String expectedCourseProgress = String.valueOf((int)expectedCourseProgressInt);
					
					testResultService.assertEquals(expectedCourseProgress,courseCompletionProgress, "Progress of Course in Widget is not correct");
					
					//sleep(1);
										
				} else {
					
					report.startStep("Navigate to next lesson by lesson list");
					if (isTest && checkTaskDone) learningArea2.submitTest(true);
					else if (isTest) learningArea2.submitTest(false);
					learningArea2.openLessonsList();
					sleep(1);
					learningArea2.clickOnLessonByNumber(lessonNumber);
																 
				}

			}

		}

	}
		
	private void checkProgressInLearningAreaByUnitLessons(String courseCode, int unitNumber,int startFromLesson, int stopAfterLesson) throws Exception {
	
		makeProgressInLearningAreaByUnitLessons(courseCode, unitNumber, startFromLesson, stopAfterLesson, true);
		
	}
		
	private void validateLessonTasks(int numOfSteps, int[] [] arr, boolean lastStepIsTest) throws Exception {

		for (int i = 0; i < numOfSteps; i++) {
						
			int stepNum = i+1;
			int taskNum = 0;
									
			report.startStep("Check Tasks State in Step " + stepNum);
			learningArea2.openTaskBar();
			sleep(1);					
			
			for (int j = 0; j < arr[i][0]; j++) {
				
				taskNum = j+1;
				
				if (taskNum==1) {
					learningArea2.checkIfTaskHasCurrentState(taskNum, true);
					if (arr[i][1] == 1) learningArea2.checkIfTaskHasDoneState(taskNum, true);
					else learningArea2.checkIfTaskHasDoneState(taskNum, false);
				} else {
					learningArea2.checkIfTaskHasCurrentState(taskNum, false);
					learningArea2.checkIfTaskHasDoneState(taskNum, false);					
				}
			}
			
			learningArea2.closeTaskBar();
			
			if (stepNum < numOfSteps) {
				
				if (lastStepIsTest && stepNum == numOfSteps-1) {
					learningArea2.clickOnStep(stepNum + 1, false);
					learningArea2.clickOnStartTest();
				} else learningArea2.clickOnStep(stepNum + 1);
									
			}

		}

	}
	
	private void verifyUnitDataOnMyProgressPage(int unitNumber, double expUnitProgress, String expUnitTooltipFull, String expUnitName) throws Exception {

		double actProgressValue = Double.valueOf(myProgress.getUnitProgressBarWidth(unitNumber));
		int expUnitCompletion = (int) Math.round(expUnitProgress);
		String actUnitProgressTooltip = myProgress.getUnitProgressTooltip(unitNumber);
		String actUnitCompletion = myProgress.getUnitProgressCompletionValue(unitNumber);
		String actUnitLabel = myProgress.getUnitLabel(unitNumber);
		String actUnitName = myProgress.getUnitName(unitNumber);
		
		testResultService.assertEquals(expUnitProgress, actProgressValue, "Progress value on My Progress is not correct");
		testResultService.assertEquals(expUnitTooltipFull, actUnitProgressTooltip, "Progress value on My Progress and tooltip are different");
		testResultService.assertEquals(String.valueOf(expUnitCompletion)+"%", actUnitCompletion, "Unit completion on My Progress is not correct");
		testResultService.assertEquals("Unit "+ String.valueOf(unitNumber), actUnitLabel, "Unit label on My Progress is not correct");
		testResultService.assertEquals(expUnitName, actUnitName, "Unit name on My Progress is not correct");

	}
	
	private void createProgressInLessonStepsExceptTest(List<String[]> steps, String courseId, int numOfTasksToRemoveForInProgressStep, boolean startFirstStepDone) throws Exception {

		// create progress in steps: one - done, next - in progress, and so on 
		
		String step_id = null;
		List<String[]> step_items = null;
		boolean enter = false;
		
		for (int i = 0; i < steps.size()-1; i++) {
			
				step_id = steps.get(i)[1];
				step_items = dbService.getSubComponentItems(step_id);
				if (startFirstStepDone) enter = (i%2 == 0);
				else enter = (i%2 != 0);
				
				if (enter){
					studentService.setProgressForSubComponent(step_id, studentId, courseId, 60, step_items, null);
				} else {
					step_items = studentService.removeItemsFromStepList(step_items, numOfTasksToRemoveForInProgressStep);
					studentService.setProgressForSubComponent(step_id, studentId, courseId, 60, step_items, null);
				}
						
			}

	}
	
	private void verifyStepsProgressExceptTest(List<String[]> steps, boolean isFirstStepDone) throws Exception {
		
		// check progress in steps: one - done, next - in progress, and so on
		
		boolean enter = false;
		
		for (int j = 0; j < steps.size()-1; j++) {
			
			if (isFirstStepDone) enter = (j%2 == 0);
			else enter = (j%2 != 0);
			
			if (enter)
				learningArea2.checkProgressIndicationInStepList(j+1, StepProgressBox.done, false, "undefined");
			else
				learningArea2.checkProgressIndicationInStepList(j+1, StepProgressBox.half, false, "undefined");
		}
		

	}

	@After
	public void tearDown() throws Exception {
			super.tearDown();
	}
	
}
