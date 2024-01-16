package tests.edo.newux;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import pageObjects.edo.NewUXLoginPage;
import pageObjects.edo.NewUxLearningArea;
import pageObjects.edo.NewUxMyProgressPage;
import testCategories.NonAngularLearningArea;
import testCategories.fullProgressInClient;
import testCategories.inProgressTests;
import testCategories.unstableTests;
import drivers.FirefoxWebDriver;
import Interfaces.TestCaseParams;

@Category(NonAngularLearningArea.class)
public class CheckProgressTests extends BasicNewUxTest {
	
	NewUxMyProgressPage myProgress;
	NewUxLearningArea learningArea;
	
	
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
		createUserAndLoginNewUXClass();
				
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
						
		report.startStep("Create new user");
		String studentId = pageHelper.createUSerUsingSP();
		
		report.startStep("Create progress in FD Unit 1");
		String unitId = dbService.getCourseUnits(courses[courseIndex]).get(unitNumber-1);
		studentService.setProgressForUnit(unitId, courses[courseIndex], studentId, 7);
		
		report.startStep("Login and check the progress of the unit");
		loginAsStudent(studentId);
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
		sleep(3);
		webDriver.switchToTopMostFrame();
		
		report.startStep("Login and check the progress of the unit");		
		loginAsStudent(studentId);
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
	@Category(unstableTests.class)
	@TestCaseParams(testCaseID = { "21984" })
	public void testLessonWithNoCompletion() throws Exception {
		report.startStep("Create user and login");
		createUserAndLoginNewUXClass();

		report.startLevel("Check lesson 1, unit 1,in First discoveries");
		homePage.clickOnUnitLessons(1);
		// newUxHomePage.scrollToUnitElement(1);
		
	
		sleep(5);
		// System.out.println(newUxHomePage.getLessonProgressStatus(0, 1));
		String progress = homePage.getLessonProgress(0, 1);
		testResultService.assertEquals("\"empty\"", progress,
				"Lesson progress class is not correct");
		// webDriver.getChildElementByCss(element, "svg");
		// System.out.println(webDriver.getElementHTML(element));

		report.report("");

	}

	@Test
	@Category(unstableTests.class)
	@TestCaseParams(testCaseID = { "21985" })
	public void testLessonWithSomeProgress() throws Exception {
		report.startStep("Create user");

		String studentId = pageHelper.createUSerUsingSP();

		report.startStep("Create progress for some of the lessons in FD, unit 1, lesson 1");

		String courseId = courses[0];
		String unitId = dbService.getCourseUnits(courseId).get(0);
		String componentId = dbService.getComponentDetailsByUnitId(unitId).get(
				0)[1];
		studentService.setProgressForComponents(componentId, courseId,
				studentId, null, 60, 2, true);

		report.startStep("Login");
		loginAsStudent(studentId);

		report.startStep("Open unit 1 lesson and check lesson 1");
		homePage.clickOnUnitLessons(1);
		sleep(3);
		String progress = homePage.getLessonProgress(0, 1);
		testResultService.assertEquals("\"medium\"", progress,
				"Lesson progress class is not correct");
	}

	@Test
	@Category(unstableTests.class)
	@TestCaseParams(testCaseID = { "21986" })
	public void testLessonWithFullCompletion() throws Exception {
		report.startStep("Create user");

		String studentId = pageHelper.createUSerUsingSP();

		report.startStep("Create progress for some of the lessons in FD, unit 1, lesson 1");

		String courseId = courses[0];
		String unitId = dbService.getCourseUnits(courseId).get(0);
		String componentId = dbService.getComponentDetailsByUnitId(unitId).get(
				0)[1];
		studentService.setProgressForComponents(componentId, courseId,
				studentId, null, 60, 0, true);

		report.startStep("Login");
		loginAsStudent(studentId);
		sleep(5);
		report.startStep("Open unit 1 lesson and check lesson 1");
		homePage.clickOnUnitLessons(1);
		
		String progress = homePage.getLessonProgress(0, 1);
		
		testResultService.assertEquals("\"full\"", progress,
				"Lesson progress class is not correct");

	}
	
	@Test
	@TestCaseParams(testCaseID = { "22987","40255" })
	public void testGetProgressInLessonList() throws Exception {
		
		report.startStep("Init test data");
		int courseIndex = 1; // Basic 1
		int unitNumber = 3;
		int lesson1 = 1;
		int lesson2 = 2;
		int lesson3 = 3;
		int lesson3_NotStarted = 0;
				
		report.startStep("Create user and log in");
		createUserAndLoginNewUXClass();
		
		report.startStep("Get Unit details for crerating progress in B1-U1");
		String courseId = courses[courseIndex];
		String unitId = dbService.getCourseUnits(courseId).get(unitNumber-1);
	
		report.startStep("Create 100% progress for lesson 1 in B1-U3");
		String componentId = dbService.getComponentDetailsByUnitId(unitId).get(lesson1-1)[1];
		studentService.setProgressForComponents(componentId, courseId, studentId, null, 60, 0, true);
		double lesson1_exp = studentService.calcComponentExpectedProgress(componentId, studentId);
		int lesson1_Completed = (int) Math.round(lesson1_exp*100);

		report.startStep("Create 30% progress for lesson 2 in B1-U3");
		componentId = dbService.getComponentDetailsByUnitId(unitId).get(lesson2-1)[1];
		String subCompId = dbService.getSubComponentsDetailsByComponentId(componentId).get(0)[1];
		studentService.setProgressForSubComponent(subCompId, studentId, courseId, 60, dbService.getSubComponentItems(subCompId), null);
		double lesson2_exp = studentService.calcComponentExpectedProgress(componentId, studentId);
		int lesson2_InProgress = (int) Math.round(lesson2_exp*100);
		
		webDriver.refresh();
		
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
		sleep(1);
		
		report.startStep("Navigate to LA of B1-U1");
		learningArea = homePage.clickOnContinueButton();
        sleep(2);
		
        report.startStep("Open lesson list and check progress indication");
        learningArea.openLessonsList();
        sleep(1);
        learningArea.checkProgressIndicationInLessonList(lesson1, lesson1_Completed);
        learningArea.checkProgressIndicationInLessonList(lesson2, lesson2_InProgress);
        learningArea.checkProgressIndicationInLessonList(lesson3, lesson3_NotStarted);
        
        
        
	}
	
	@Test
	@TestCaseParams(testCaseID = { "21737" })
	public void testDefaultViewInDiffCourses() throws Exception {
		
		report.startStep("Create user");
		studentId = pageHelper.createUSerUsingSP(configuration.getInstitutionId(), configuration.getClassName());
		
		report.startStep("Init test data");
		int indexFD = 0;
		int indexB3 = 3;
		int indexA2 = 8;
				
		report.startStep("Create progress in FD-U8-L6");
		String courseId = courses[indexFD];
		String unitId = dbService.getCourseUnits(courseId).get(7);
		String componentId = dbService.getComponentDetailsByUnitId(unitId).get(5)[1];
		studentService.setProgressForComponents(componentId, courseId, studentId, null, 60, 6, true);
		
		report.startStep("Create progress in B3-U2-L3");
		courseId = courses[indexB3];
		unitId = dbService.getCourseUnits(courseId).get(1);
		componentId = dbService.getComponentDetailsByUnitId(unitId).get(2)[1];
		studentService.setProgressForComponents(componentId, courseId, studentId, null, 60, 6, true);

		report.startStep("Create progress in A2-U1-L5");
		courseId = courses[indexA2];
		unitId = dbService.getCourseUnits(courseId).get(0);
		componentId = dbService.getComponentDetailsByUnitId(unitId).get(4)[1];
		studentService.setProgressForComponents(componentId, courseId, studentId, null, 60, 6, true);
		
		report.startStep("Login with user");
		NewUXLoginPage loginPage = new NewUXLoginPage(webDriver,testResultService);
		homePage = loginPage.loginAsStudent(dbService.getUserNameById(studentId, configuration.getInstitutionId()), "12345");
				
		report.startStep("Navigate to FD and check titles in Course Area");
        homePage.navigateToRequiredCourseOnHomePage(coursesNames[indexFD]);
        sleep(1);
        homePage.checkUnitLessonNameInCourseArea("Unit 8: Going Out", "Places");
        
        report.startStep("Navigate to B3 and check titles in Course Area");
        homePage.navigateToRequiredCourseOnHomePage(coursesNames[indexB3]);
        sleep(1);
        homePage.checkUnitLessonNameInCourseArea("Unit 2: Business Matters", "Fax It");
        
        report.startStep("Navigate to A2 and check titles in Course Area");
        homePage.navigateToRequiredCourseOnHomePage(coursesNames[indexA2]);
        sleep(1);
        homePage.checkUnitLessonNameInCourseArea("Unit 1: Telling The Truth", "Logical Connectors: Contrast");
                
        
	}

	@Test
	@TestCaseParams(testCaseID = { "21987" })
	public void testLoackedLessonProgress() throws Exception {

		String className = configuration.getProperty("classname.locked");
		String studentId = pageHelper.createUSerUsingSP(
				configuration.getInstitutionId(), className);

		String courseId = courses[0];

		try {
			report.startStep("Create progress for some of the lessons in FD, unit 1, lesson 1");

			String unitId = dbService.getCourseUnits(courseId).get(0);
			String componentId = dbService.getComponentDetailsByUnitId(unitId)
					.get(0)[1];
			studentService.setProgressForComponents(componentId, courseId,
					studentId, null, 60, 0, true);

			report.startStep("Lock lesson");

			pageHelper.LockLessonToClass(className, courseId, 1, 1);

			report.startStep("Create user and login");
			loginAsStudent(studentId);

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
	@Category(unstableTests.class)
	@TestCaseParams(testCaseID = { "22067" })
	public void testMaxProgressInCourse() throws Exception {
		report.startStep("Create user");
		String studentId = pageHelper.createUSerUsingSP();
		sleep(2);
		
		report.startStep("Set full progress for all units in course Basic 2");
		String courseId = courses[3];
		studentService.setProgressForCourse(courseId, studentId, null, 60,true, true);

		sleep(6);
		report.startStep("Login and check the All units progress bar");
		loginAsStudent(studentId);
		sleep(6);

		//back to Course where progress done
		homePage.carouselNavigateBack();

		report.startStep("Check all units progress bar");
		List<String> units = dbService.getCourseUnits(courseId);
		for (int i = 0; i < units.size(); i++) {
			double displayedUnitProgress = homePage
					.getAllUnitsBarUnitProgress(1);

			double expectedUnitPrgress = studentService
					.calcUnitExpectedProgress(units.get(i), studentId);

			double expectedUnitProgressInBar = expectedUnitPrgress
					/ units.size();
			expectedUnitProgressInBar = textService.round(expectedUnitProgressInBar, 4);

			testResultService.assertEquals(expectedUnitProgressInBar,
					displayedUnitProgress, "Progress of unit is not correct");

			if (!(webDriver instanceof FirefoxWebDriver)) {
				String bgcolor = homePage.getAllUnitsUnitColor(i + 1);
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
		
		report.startStep("Create user");
		studentId = pageHelper.createUSerUsingSP();

		report.startStep("Set full progress for unit 2 of Basic 1");
		String courseId = courses[1];
		String unitId = dbService.getCourseUnits(courseId).get(unitNumber-1);
		studentService.setProgressForUnit(unitId, courseId, studentId, null,60, 0, true, true);

		report.startStep("Login and check the All units progress bar");
		loginAsStudent(studentId);
		
		double displayedUnitProgress = homePage.getAllUnitsBarUnitProgress(unitNumber);
		double expectedUnitPrgress = studentService.calcUnitExpectedProgress(unitId, studentId);
		double expectedUnitProgressInBar = expectedUnitPrgress / dbService.getCourseUnits(courseId).size();
		expectedUnitProgressInBar = textService.round(expectedUnitProgressInBar, 4);

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

	@Test
	@TestCaseParams(testCaseID = { "22087" }, allowMedia=true)
	public void testTasksProgressDefaultViewFirstEnter() throws Exception {
		report.startStep("Create new user and logjn");
		createUserAndLoginNewUXClass();

		report.startStep("Navigate to FD, unit 1 lesson 1");

		NewUxLearningArea learningArea = new NewUxLearningArea(webDriver,
				testResultService);

		homePage.navigateToCourseUnitAndLesson(courseCodes, "FD", 1, 1);

		report.startStep("Check that the 1st task has current and visited indication");

		validateLessonTasks(3, new int[] { 1, 1, 6 }, learningArea);
		//homePage.clickToOpenNavigationBar();
		homePage.clickOnHomeButton();

		homePage.navigateToCourseUnitAndLesson(courseCodes, "FD", 3, 2);

		validateLessonTasks(5, new int[] { 1, 1, 3, 1, 1 }, learningArea);
		//homePage.clickToOpenNavigationBar();
		homePage.clickOnHomeButton();

		homePage.navigateToCourseUnitAndLesson(courseCodes, "B2", 2, 3);

		validateLessonTasks(3, new int[] { 1, 1, 1 }, learningArea);
		//homePage.clickToOpenNavigationBar();
		homePage.clickOnHomeButton();

		homePage.navigateToCourseUnitAndLesson(courseCodes, "A3", 8, 6);

		validateLessonTasks(3, new int[] { 1, 10, 15 }, learningArea);

		// System.out.println(learningArea.getNumberOfTasksBySubCompIndex(2));
		// System.out.println(learningArea.getNumberOfTasksBySubCompIndex(3));
		report.startStep("Check number of tasks for each sub component");
	}

	@Test
	@TestCaseParams(testCaseID = { "19880" })
	public void testCourseDisplayIfCompleteed() throws Exception {
		report.startStep("Create user and set full progress for FD course");
		studentId = pageHelper.createUSerUsingSP();
		studentService.setProgressForCourse(courses[0], studentId, null, 60);

		report.startStep("Login and check that next course is Basic 1");
		loginAsStudent(studentId);

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
		report.startStep("Create student in class with no assigned courses");
		createUserAndLoginNewUXClass(configuration.getProperty("classname.nocourses"));
		sleep(3);
		report.startStep("Check an asigned courses message");
		homePage.validateUnAssignedCoursesMessage();
	}

	@Test
	@TestCaseParams(testCaseID = { "22088" })
	public void testTasksProgressSAveVisitedStatus() throws Exception {

		report.startStep("Create user and login");
		createUserAndLoginNewUXClass();

		report.startStep("Navigate to FD, unit 1, lesson 1");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "FD", 1, 1);
		NewUxLearningArea learningArea = new NewUxLearningArea(webDriver,
				testResultService);
		
		//learningArea.clickOnStep(2);
		
		report.startStep("Visit all tasks");
		int numberOfTasks = 6;
		
		learningArea.clickOnStep(3);
		for (int i = 0; i < numberOfTasks - 1; i++) {
			sleep(1);
			int index = i + 1;
			learningArea.clickOnTask(index);

		}
/*		report.startStep("Verify all tasks state");
		for (int i = 0; i < numberOfTasks; i++) {
			int index = i + 1;
			learningArea.clickOnTask(index);
			testResultService.assertEquals(true,
					learningArea.checkThatTaskIsVisitedAndCurrent(i),
					"Task state is not correct", true);

		}

		report.startStep("Click on the first task and check its state");
		learningArea.clickOnTask(1);
		testResultService.assertEquals(true,
				learningArea.checkThatTaskIsVisitedAndCurrent(0),
				"Task state is not correct", true);
*/
		report.startStep("Click on Home button and return to the same lesson");
		//homePage.clickToOpenNavigationBar();
		homePage.clickOnHomeButton();
		homePage.navigateToCourseUnitAndLesson(courseCodes, "FD", 1, 1);
		learningArea.clickOnStep(3);
		report.startStep("Verify the State Visited");
		for (int i = 0; i < numberOfTasks; i++) {
			//int index = i + 1;
			testResultService.assertEquals(true,
					learningArea.checkThatTaskIsVisitedAndCurrent(i),
					"Task state is not correct", true);
				if ( i+1 < numberOfTasks){
					learningArea.clickOnTask(i+1);
				} 
			}
	}
	
	@Test
	@Category(fullProgressInClient.class)
	@TestCaseParams(testCaseID = { "22990" }, ignoreTestTimeout = true, allowMedia = true)
	public void testCompleteFullUnitClientSide() throws Exception {
				
		report.startStep("Create user and login");		
		createUserAndLoginNewUXClass();

		report.startStep("Navigate to B3");
		homePage.navigateToRequiredCourseOnHomePage("Basic 3");
		
		report.startStep("Enter Unit 8, make progress and check progress bars");
		makeProgressInLearningAreaByUnitLessons("B3", 8, 1, 7, false);
		webDriver.refresh();
		
		report.startStep("Enter Unit 8 and check progress done");
		checkProgressInLearningAreaByUnitLessons("B3", 8, 1, 7);
	}
	
	@Test
	@TestCaseParams(testCaseID = { "26144" }, allowMedia = true, skippedBrowsers = {"firefox"}) // Actions API not supported by geckodriver)
	public void testSaveDoneStatusInDiffCourses() throws Exception {
				
		report.startStep("Create user and login");
		createUserAndLoginNewUXClass();

		report.startStep("Navigate to FD");
		homePage.navigateToRequiredCourseOnHomePage("First Discoveries");
		
		report.startStep("Enter Unit 1 - Lesson 1, make progress and check progress bars");
		makeProgressInLearningAreaByUnitLessons("FD", 1, 1, 1, false);
		webDriver.scrollToTopOfPage();
		
		report.startStep("Navigate to A3");
		homePage.navigateToRequiredCourseOnHomePage("Advanced 3");
		
		report.startStep("Enter Unit 1 - Lesson 1, make progress and check progress bars");
		makeProgressInLearningAreaByUnitLessons("A3", 2, 1, 1, false);
		webDriver.scrollToTopOfPage();
		
		report.startStep("Navigate to FD");
		homePage.navigateToRequiredCourseOnHomePage("First Discoveries");
		
		report.startStep("Enter Unit 1 - Lesson 1 and check progress done");
		checkProgressInLearningAreaByUnitLessons("FD", 1, 1, 1);
	}
	
	@Test
	@TestCaseParams(testCaseID = { "22091" })
	public void testTaskProgressDoNotSaveVisitedStatusInTest() throws Exception {
		report.startStep("Create user and navigate to A3>Unit 8>lesson 6");
		createUserAndLoginNewUXClass();
		sleep(1);
		homePage.navigateToCourseUnitAndLesson(courseCodes, "A3", 8, 6);
		sleep(1);
		NewUxLearningArea learningArea = new NewUxLearningArea(webDriver, testResultService);
		learningArea.clickOnStep(3);
		sleep(1);
		learningArea.clickOnStartTest();

		testResultService.assertTrue("Task is not visited and current",
				learningArea.checkThatTaskIsVisitedAndCurrent(0));
		
		learningArea.clickOnTask(1);
		learningArea.clickOnTask(2);

		sleep(1);

		learningArea.checkThatTaskIsVisited(0);
		learningArea.checkThatTaskIsVisited(1);
		learningArea.checkThatTaskIsVisitedAndCurrent(2);

		learningArea.clickOnStep(1);
		learningArea.approveTest();
		sleep(1);
		learningArea.checkThatTaskIsVisitedAndCurrent(0);

		report.startStep("Go to Test step again");
		webDriver.switchToTopMostFrame();
		learningArea.clickOnStep(3);

		learningArea.clickOnStartTest();
		sleep(1);
		learningArea.checkThatTaskIsVisitedAndCurrent(0);

		learningArea.chechThatTaskIsNotVisited(1);
		learningArea.chechThatTaskIsNotVisited(2);

		report.startStep("Go to homepage and back to the lesson area");
		//learningArea.clickToOpenNavigationBar();
		learningArea.clickOnHomeButton();
		sleep(1);
		learningArea.approveTest();
		sleep(1);
		
		homePage.navigateToCourseUnitAndLesson(courseCodes, "A3", 8, 6);
		sleep(1);
		learningArea.clickOnStep(2);
		sleep(1);
		learningArea.checkThatTaskIsVisitedAndCurrent(0);

		learningArea.chechThatTaskIsNotVisited(1);
		learningArea.chechThatTaskIsNotVisited(2);

		learningArea.clickOnStep(3);
		sleep(1);
		learningArea.clickOnStartTest();

		learningArea.checkThatTaskIsVisitedAndCurrent(0);

		learningArea.chechThatTaskIsNotVisited(1);
		learningArea.chechThatTaskIsNotVisited(2);

	}
		
	private void makeProgressInLearningAreaByUnitLessons(String courseCode, int unitNumber, int startFromLesson, int stopAfterLesson, boolean checkTaskDone) throws Exception {
		
		NewUxLearningArea learningArea = new NewUxLearningArea(webDriver, testResultService);
		String courseId = courses[homePage.getCourseIndexByCode(courseCodes, courseCode)];
		List<String[]> units = dbService.getCourseUnitDetils(courseId);

		//for (int i = 0; i < units.size(); i++) { // PRODUCTION
		//for (int i = 6; i < units.size(); i++) {
		for (int i = (unitNumber-1); i < unitNumber; i++) {
			
			report.startStep("Go over unit: " + units.get(i)[1]);
			
			List<String[]> components = dbService.getComponentDetailsByUnitId(units.get(i)[0]);

			int unitIndex = i + 1;
			//int lessonIndex = 1; // PRODUCTION
			int lessonIndex = startFromLesson; 
			int stepIndex = 1;
			int taskIndex = 1;
			
			sleep(1);
			homePage.clickOnUnitLessons(unitIndex);
			//newUxHomePage.clickOnLesson(i, 1); // PRODUCTION
			//newUxHomePage.clickOnLesson(i, 6);
			homePage.clickOnLesson(i, startFromLesson);
			sleep(3);
																	
			//for (int j = 0; j < components.size(); j++) { // PRODUCTION
			//for (int j = 5; j < components.size(); j++) {
			for (int j = (startFromLesson-1); j < stopAfterLesson; j++) {	
			
				report.startStep("Go over lesson: " + components.get(j)[0]);
						
				report.startStep("Go over every step");
				List<String[]> subComp = dbService.getSubComponentsDetailsByComponentId(components.get(j)[1]);

				boolean isTest = false;
				boolean isInteract = false;
				for (int h = 0; h < subComp.size(); h++) {
					if (h > 0) {
						stepIndex = h + 1;
						report.startStep("Click on Step: " + h);
						sleep(1);
						taskIndex = 1;					
						learningArea.clickOnStep(stepIndex);
						
					}

					// if step is Interact
					if (subComp.get(h)[0].contains("Practice 1") || subComp.get(h)[0].contains("Practice 2") || subComp.get(h)[0].contains("Interact 1") || subComp.get(h)[0].contains("Interact 2") ) {
						learningArea.closeAlertModalByAccept();
						//learningArea.clickOnCancel();
						isInteract = true;
					}
										
					// if step is test
					if (subComp.get(h)[0].contains("Test")) {
						learningArea.clickOnStartTest();
						isTest = true;
					}

					report.startStep("Go over every Task");
					List<String[]> expectdTasks = dbService.getSubComponentItems(subComp.get(h)[1]);
					
					int itemTypeId = Integer.parseInt(expectdTasks.get(0)[2]); 
					String itemCode = expectdTasks.get(0)[1];
					
					report.startStep("Making progress in Task 1 / Checking status of Task 1");
					if (checkTaskDone && !isTest) {
						if (itemTypeId==21) //learningArea.closeAlertModalByAccept();
                        learningArea.clickOnCancel();
						learningArea.checkThatTaskIsDone(0);
					}
					else { 
						if (!checkTaskDone) learningArea.makeProgressActionByItemType(itemTypeId, itemCode);
						testResultService.assertEquals(learningArea.checkThatTaskIsVisitedAndCurrent(0), true, "Task not visited or not current");
					}
					
					taskIndex = 1;
					int taskPage = 0;
					report.report("Number of tasks is :"+expectdTasks.size());
					for (int k = 0; k < expectdTasks.size(); k++) {
										
						itemTypeId = Integer.parseInt(expectdTasks.get(k)[2]);
						itemCode = expectdTasks.get(k)[1];
						
						if (k > 0) {
							if (taskIndex % 7  == 0) {
								sleep(1);
								learningArea.clickOnTasksNext();
								taskPage++;
							}
							sleep(2);
							if (expectdTasks.size() > 7) {
								learningArea.clickOnTask(taskIndex, taskPage);
								
								report.startStep("Making progress in Task "+(taskIndex+1)+" / Checking status of Task " + (taskIndex+1));
								if (checkTaskDone && !isTest) {
									if (itemTypeId==21) //learningArea.closeAlertModalByAccept();
									learningArea.clickOnCancel();
									learningArea.checkThatTaskIsDone(taskIndex);
								}
								else { 
									if (!checkTaskDone) learningArea.makeProgressActionByItemType(itemTypeId, itemCode);
									testResultService.assertEquals(learningArea.checkThatTaskIsVisitedAndCurrent(taskIndex), true, "Task not visited or not current");
								}
								
							} else {
								learningArea.clickOnTask(taskIndex);
								
								report.startStep("Making progress in Task "+(taskIndex+1)+" / Checking status of Task " + (taskIndex+1));
								if (checkTaskDone && !isTest) {
									if (itemTypeId==21) //learningArea.closeAlertModalByAccept();
										learningArea.clickOnCancel();
									learningArea.checkThatTaskIsDone(taskIndex);
								}
								else { 
									if (!checkTaskDone) learningArea.makeProgressActionByItemType(itemTypeId, itemCode);
									testResultService.assertEquals(learningArea.checkThatTaskIsVisitedAndCurrent(taskIndex), true, "Task not visited or not current");
								}
							}
						
						webDriver.waitForJqueryToFinish();
						taskIndex++;
						}
					}
						

				}
				
				report.finishStep();
				
				lessonIndex = j + 2;
								
				//if (lessonIndex > components.size()){ // PRODUCTION
				if (lessonIndex > stopAfterLesson){ 
				//if (lessonIndex > 2){ 
					if (isTest && checkTaskDone) learningArea.submitTest(true);
					else if (isTest) learningArea.submitTest(false);
					
					report.startStep("Open Lesson List and check all lessons completed");
					learningArea.openLessonsList();
					learningArea.checkLessonsCompletedInLessonList(startFromLesson, stopAfterLesson);
					learningArea.closeLessonsList();
					sleep(1);
					
					report.startStep("Go back to Home Page");
					//learningArea.clickToOpenNavigationBar();
					learningArea.clickOnHomeButton();
					
					report.startStep("Open unit: "+unitIndex+" and check all lessons completed");
					homePage.clickOnUnitLessons(unitIndex);
					learningArea.checkLessonsCompletedInLessonList(startFromLesson, stopAfterLesson);
					//homePage.clickToOpenNavigationBar();
					//newUxHomePage.clickOnUnitLessons(unitIndex+1);
					
					report.startStep("Check Unit "+unitIndex+" Progress Bar");
					int lessonsCompleted = stopAfterLesson-startFromLesson+1;
					double unitProgress = homePage.getUnitProgressBarValue(unitIndex-1);
					double actualUnitProgress = Math.round(unitProgress); 
					double expectedUnitProgress = Math.round(100.0 / components.size() * lessonsCompleted);
					testResultService.assertEquals(expectedUnitProgress, actualUnitProgress,"Progress in Unit Progress Bar is not correct");
					
					report.startStep("Check All Units Progress Bar");
					unitProgress = homePage.getAllUnitsBarUnitProgress(unitIndex);
					actualUnitProgress = Math.round(unitProgress); // roundExpectedProgress(unitProgress, 0).intValue();
					expectedUnitProgress = Math.round(expectedUnitProgress / units.size()); 
					testResultService.assertEquals(expectedUnitProgress, actualUnitProgress, "Progress of Unit in All Units Bar is not correct");
				
					report.startStep("Check Course Completion widget");
					String actCompletion = homePage.getCompletionWidgetValue();
					double totalLessonsInCourse = getNumberOfLessonsInCourse(units);
					//String expectedCourseProgress = String.valueOf((int)expectedUnitProgress);
					int expCourseProgress = (int) Math.round(lessonsCompleted / totalLessonsInCourse * 100);
					String expectedCourseProgress = String.valueOf(expCourseProgress);
					testResultService.assertEquals(expectedCourseProgress, actCompletion, "Progress of Course in Widget is not correct");
					
					sleep(1);
										
				} else {
					report.startStep("Navigate to next lesson by lesson list");
					if (isTest && checkTaskDone) learningArea.submitTest(true);
					else if (isTest) learningArea.submitTest(false);
					learningArea.openLessonsList();
					sleep(1);
					learningArea.clickOnLessoneByIndex(lessonIndex);
																 
				}

			}

		}

	}
		
	private void checkProgressInLearningAreaByUnitLessons(String courseCode, int unitNumber,int startFromLesson, int stopAfterLesson) throws Exception {
	
		makeProgressInLearningAreaByUnitLessons(courseCode, unitNumber, startFromLesson, stopAfterLesson, true);
		
	}
		
	private void validateLessonTasks(int numOfSteps, int[] arr,
			NewUxLearningArea learningArea) throws Exception {

		for (int i = 0; i < numOfSteps; i++) {
			int index = i + 1;

			webDriver.closeAlertByAccept(2);
			report.startStep("Check Explore tasks");
//			testResultService.assertEquals(arr[i],
//					learningArea.getNumberOfTasks(),
//					"Number of tasks is not correct", true);
			if (!learningArea.getStepName(index).contains("Test")) {
				testResultService.assertEquals(true,
						learningArea.checkThatTaskIsVisitedAndCurrent(0),
						"Task is not marked as visited", true);
			}
			if (i < numOfSteps - 1) {
				learningArea.clickOnStep(i + 2);
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
	
	private double getNumberOfLessonsInCourse(List<String[]> courseUnits) throws Exception {

		int totalLessons = 0;
		
		for (int j = 0; j < courseUnits.size(); j++) {
			totalLessons = totalLessons + dbService.getComponentDetailsByUnitId(courseUnits.get(j)[0]).size();
		}
		return totalLessons;

	}
	
	
}
