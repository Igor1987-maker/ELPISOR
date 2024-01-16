package tests.edo.newux;

import java.math.BigDecimal;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import Enums.TaskTypes;
import pageObjects.edo.NewUxDragAndDropSection;
import pageObjects.edo.NewUxDragAndDropSection2;
import pageObjects.edo.NewUxLearningArea;
import pageObjects.edo.NewUxLearningArea2;
import pageObjects.edo.NewUxMyProgressPage;
import testCategories.AngularLearningArea;
import testCategories.NonAngularLearningArea;
import testCategories.inProgressTests;
import testCategories.reg2;
import testCategories.unstableTests;
import testCategories.edoNewUX.HomePage;
import Interfaces.TestCaseParams;


@Category(reg2.class)
public class CourseStatisticsWidgets extends BasicNewUxTest {

	NewUxMyProgressPage myProgressPage; 
	NewUxLearningArea learningArea; 
	NewUxLearningArea2 learningArea2;
	NewUxDragAndDropSection dragAndDrop; 
	NewUxDragAndDropSection2 dragAndDrop2;
			
	int expectedProgress = 34;
	private final static String myProgressPageTitle = "My Progress"; 
	
	
	@Before
	public void setup() throws Exception {
	
		super.setup();
	}
		
		
	@Test
	@Category(HomePage.class)
	@TestCaseParams(testCaseID = { "19856","40220" }, testMultiple = true)
	public void WidgetsElements() throws Exception {

		report.startStep("Create new student and login");
		homePage = createUserAndLoginNewUXClass();
		homePage.closeAllNotifications();
		homePage.waitHomePageloadedFully();
		
		String CompletionLabel = "Course Completion";
		String ScoreLabel = "Average Test Score";
		String TimeLabel = "Time on Task";
		String pageUnderTest = "Home Page";
		int iteration = 1;

		while (iteration<=2) {
					
			// Verify Course Completion Widget
		report.startStep("Verify Course Completion Widget on: " + pageUnderTest );
			testResultService.assertEquals(true,homePage.isCourseCompletionWidgetExist());
			testResultService.assertEquals("%",	homePage.getCourseCompletionWidgetUnitItem());
			testResultService.assertEquals(CompletionLabel, homePage.getCourseCompletionWidgetLabel(CompletionLabel));
	
			// Verify Score Widget Widget
		report.startStep("Verify Score Widget Widget on: " + pageUnderTest );
			testResultService.assertEquals(true, homePage.isScoreWidgetExist());
			testResultService.assertEquals("%", homePage.getScoreWidgetUnitItem());
			testResultService.assertEquals(ScoreLabel, homePage.getScoreWidgetLabel(ScoreLabel));
	
			// Verify Time Widget
		report.startStep("Verify Time Widget on: " + pageUnderTest );
			testResultService.assertEquals(true, homePage.isTimeWidgetExist());
			testResultService.assertEquals(":", homePage.getTimeWidgetUnitsDelimiter());
			testResultService.assertEquals("Hrs.", homePage.getTimeWidgetHoursLabel());
			testResultService.assertEquals("Mins.", homePage.getTimeWidgetMinLabel());
			testResultService.assertEquals(TimeLabel, homePage.getTimeWidgetLabel(TimeLabel));

			if (iteration==1) homePage.clickOnMyProgress();
			pageUnderTest = "My Progress Page";
			iteration++;	
		}
		
	}

	@Test
	@Category(HomePage.class)
	@TestCaseParams(testCaseID = { "21743", "40220" }, testMultiple = true)
	public void WidgetsDataNoProgress() throws Exception {

		report.startStep("Create new student and login");
		homePage = createUserAndLoginNewUXClass();
		homePage.closeAllNotifications();
		homePage.waitHomePageloadedFully();
		
		int coursesCount = 2;
		
		report.startStep("Verify Widgets Display No Progress");
		for (int i = 0; i < coursesCount; i++) {
			int iteration = 1;
			while (iteration<=2) {
				String completionNoProgress = homePage.getCompletionWidgetValue();
				String timeNoProgress = homePage.getTimeOnTaskWidgetValue();
				testResultService.assertEquals("0", completionNoProgress, "Testing Course ID:" + courses[i]);
				testResultService.assertEquals("00:00", timeNoProgress, "Testing Course ID:" + courses[i]);
				if (iteration==1) homePage.clickOnMyProgress();
				iteration++;
			}
			homePage.clickToOpenNavigationBar();
			homePage.clickOnHomeButton();
			homePage.waitHomePageloadedFully();
			homePage.carouselNavigateNext();
			sleep(1);
			
		}
		
	}

	@Test
	@TestCaseParams(testCaseID = { "21747","40220" }, testMultiple = true, testTimeOut="20")
	public void WidgetsDataLastProgressFD() throws Exception {

		int expectedProgress=11;
		
		testWidgetDataLastProgress2(configuration.getClassName(),
				institutionId, true, new String[] { courses[0],
						courses[1] }, expectedProgress);
	}

	@Test
	@Category(unstableTests.class)
	@TestCaseParams(testCaseID = { "26138","40220" }, testMultiple = true)
	public void WidgetsDataLastProgressESP() throws Exception {
		
		testWidgetDataLastProgress(specialCoursesData.get(4)[0],
				institutionId, true, new String[] { "20104", "20111" },
				expectedProgress);
	}

	@Test
	@Category(unstableTests.class)
	@TestCaseParams(testCaseID = { "26140","40220" }, testMultiple = true)
	public void WidgetsDataLastProgressCourseBuilder() throws Exception {

		testWidgetDataLastProgress(specialCoursesData.get(3)[0],
				institutionId, false, new String[] { "6304" }, expectedProgress);
	}

	@Test
	@Category(unstableTests.class)
	@TestCaseParams(testCaseID = { "26139","40220" }, testMultiple = true)
	public void WidgetsDataLastProgressEnriched() throws Exception {


		testWidgetDataLastProgress(specialCoursesData.get(2)[0],
				institutionId, true, new String[] { "20000", "22" }, 42);
	}
	
	@Test
	@Category(NonAngularLearningArea.class)
	@TestCaseParams(testCaseID = { "21747" }) // reported Bug 38481:Submit Test / TMS Report Not Updated If Test Made Last
	public void WidgetsDataTmsConsistent() throws Exception {

		report.startStep("Create new student and login");
		homePage = createUserAndLoginNewUXClass();
		learningArea = new NewUxLearningArea(webDriver, testResultService);
		dragAndDrop = new NewUxDragAndDropSection(webDriver, testResultService);
		sleep(3);
				
		report.startStep("Navigate to B1-U1-L1");
		String courseID = courses[1];
		homePage.navigateToCourseUnitAndLesson(courseCodes, "B1", 1, 1);
		sleep(2);
		
		report.startStep("Press on Play btn");
		learningArea.clickOnPlayVideoButton();
		sleep(1);
		
		report.startStep("Submit Test without correct answers - 0%");
		learningArea.clickOnStep(3);
		sleep(1);
		learningArea.clickOnStartTest();
		sleep(1);
		learningArea.submitTest(true);
		
		report.startStep("Go Back To Home Page");
		//homePage.clickToOpenNavigationBar();
		homePage.clickOnHomeButton();
		homePage.waitHomePageloaded();
		
		report.startStep("Verify progress widgets data TMS consistent");
		String expCompletion = studentService.getStudentCompletionCalculated(studentId, courseID);
		String expTestScore = "undefined";
		String expTimeOnTask = "00:01";
				
		verifyStatisticsWidgets(expCompletion, expTimeOnTask, null, false);
				
		report.startStep("Submit Test with correct answers - 40%");
		homePage.clickOnContinueButton();
		learningArea.clickOnStep(3);
		learningArea.clickTheStartTestButon();
		dragAndDrop.dragAndDropCloseAnswerByTextToId("New York", "1_1");
		learningArea.clickOnNextButton();
		dragAndDrop.dragAndDropCloseAnswerByTextToId("reporter", "1_1");
		learningArea.clickOnNextButton();
		learningArea.selectMultipleAnswer(2);
		learningArea.submitTest(true);
		
		report.startStep("Go Back To Home Page");
		//homePage.clickToOpenNavigationBar();
		sleep(1);	
		homePage.clickOnHomeButton();
		homePage.waitHomePageloaded();	
		
		report.startStep("Verify progress widgets data TMS consistent");
		expCompletion = studentService.getStudentCompletionCalculated(studentId, courseID);
		expTestScore = studentService.getStudentTestAvgCalculated(studentId, courseID);
					
		verifyStatisticsWidgets(expCompletion, expTimeOnTask, expTestScore, true);
		
		
	}
	
	@Test
	@Category(AngularLearningArea.class)
	@TestCaseParams(testCaseID = { "21747" }) // reported Bug 38481:Submit Test / TMS Report Not Updated If Test Made Last
	public void WidgetsDataTmsConsistent2() throws Exception {

		report.startStep("Create new student and login");
		homePage = createUserAndLoginNewUXClass();
		homePage.closeAllNotifications();
		homePage.waitHomePageloadedFully();
		
		dragAndDrop = new NewUxDragAndDropSection(webDriver, testResultService);
		dragAndDrop2 = new NewUxDragAndDropSection2(webDriver, testResultService);
		//sleep(3);
				
		report.startStep("Navigate to B1-U1-L1");
		String courseID = courses[1];
		learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "B1", 1, 1);
		//sleep(2);
		learningArea2.clickOnNextButton();
		
		report.startStep("Press on Play btn");
		learningArea2.clickOnPlayVideoButton();
		sleep(1);
		
		report.startStep("Submit Test without correct answers - 0%");
		learningArea2.clickOnStep(3, false);
		sleep(1);
		learningArea2.clickOnStartTest();
		sleep(1);
		learningArea2.submitTest(true);
		
		report.startStep("Go Back To Home Page");
		//homePage.clickToOpenNavigationBar();
		sleep(1);
		homePage.clickOnHomeButton();
		homePage.waitHomePageloadedFully();
		
		report.startStep("Verify progress widgets data TMS consistent");
		String expCompletion = studentService.getStudentCompletionCalculated(studentId, courseID);
		String expTestScore = "undefined";
		String expTimeOnTask = "00:01";
				
		verifyStatisticsWidgets(expCompletion, expTimeOnTask, null, false);
				
		report.startStep("Submit Test with correct answers - 40%");
		learningArea2 = homePage.clickOnContinueButton2();
		learningArea2.waitUntilLearningAreaLoaded();
		learningArea2.clickOnStep(3, false);
		learningArea2.clickTheStartTestButon();
		//dragAndDrop.dragAndDropCloseAnswerByTextToId("New York", "1_1");
		dragAndDrop2.dragAndDropAnswerByTextToTarget("New York", 1, TaskTypes.Close);
		learningArea2.clickOnNextButton();
	   // dragAndDrop.dragAndDropCloseAnswerByTextToId("reporter", "1_1");
		dragAndDrop2.dragAndDropAnswerByTextToTarget("reporter", 1, TaskTypes.Close);
		learningArea2.clickOnNextButton();
	   //learningArea2.selectMultipleAnswer(2);
		learningArea2.SelectRadioBtn("question-1_answer-1");
		learningArea2.submitTest(true);
		
		report.startStep("Go Back To Home Page");
		//homePage.clickToOpenNavigationBar();
		sleep(1);	
		homePage.clickOnHomeButton();
		homePage.waitHomePageloadedFully();	
		
		report.startStep("Verify progress widgets data TMS consistent");
		expCompletion = studentService.getStudentCompletionCalculated(studentId, courseID);
		expTestScore = studentService.getStudentTestAvgCalculated(studentId, courseID);
					
		verifyStatisticsWidgets(expCompletion, expTimeOnTask, expTestScore, true);
		
		
	}
	
	private void testWidgetDataLastProgress(String className,
			String institutionId, boolean hasMoreThenOneCourse,
			String[] courses, int progress) throws Exception {
		
		report.startStep("Create new student and login");
		homePage = createUserAndLoginNewUXClass(className, institutionId);
		sleep(3);
				
		report.startStep("Enter Learning Area");
		homePage.clickOnContinueButton();

		report.startStep("Set progress Completion & Time");
		studentService.setProgressInFirstUnitInCourse(courses[0], studentId, 5);

		report.startStep("Go Back To Home Page");
		//homePage.clickToOpenNavigationBar();
		homePage.clickOnHomeButton();
		
		report.startStep("Generate test data");
		BigDecimal completion = roundExpectedProgress(studentService.calcCourseExpectedProgress(courses[0], studentId));
		int completionInt = completion.intValue();
		String expCompletion = String.valueOf(completionInt);
		String expTimeOnTask = dbService.getStudentTimeOnTask(courses[0], studentId);
		expTimeOnTask = expTimeOnTask.substring(0, 5);
		String expTestScore = "100"; 
		
		report.startStep("Verify Comletion Widgets on Home Page");
		verifyStatisticsWidgets(expCompletion, expTimeOnTask, null, false);
		
		report.startStep("Verify My Progress Page title & course name");
		String expCourseName = homePage.getCurrentCourseName();
		myProgressPage = homePage.clickOnMyProgress();
		sleep(1);
		testResultService.assertEquals(myProgressPageTitle, myProgressPage.getPageTitle(), "My Progress page title is not valid");
		testResultService.assertEquals(expCourseName, myProgressPage.getCourseName(), "Course name is not valid");
		testResultService.assertEquals(true, myProgressPage.printBtn.isDisplayed(), "Print btn not found");
		
		report.startStep("Verify Comletion Widgets on My Progress Page");
		verifyStatisticsWidgets(expCompletion, expTimeOnTask, null, false);
		homePage.clickToOpenNavigationBar();
		homePage.clickOnHomeButton();
		homePage.waitHomePageloaded();
		
		if (hasMoreThenOneCourse) {
			
			report.startStep("Navigate to B1 and Enter Learning Area");
			sleep(2);
			homePage.carouselNavigateNext();
			sleep(3);
			homePage.checkTextandClickOnCourseBtn("Start");

			report.startStep("Set progress Completion & Score & Time");
			studentService.setProgressInFirstUnitInCourse(courses[1],studentId, 0);
			studentService.submitTestsForCourse(studentId, courses[1], false);

			report.startStep("Go Back To Home Page");
			sleep(2);
			//homePage.clickToOpenNavigationBar();
			homePage.clickOnHomeButton();
			homePage.waitHomePageloaded();
						
			report.startStep("Generate test data");
			completion = roundExpectedProgress(studentService.calcCourseExpectedProgress(courses[1], studentId));
			completionInt = progress;
			expCompletion = String.valueOf(completionInt);
			expTimeOnTask = dbService.getStudentTimeOnTask(courses[1], studentId);
			
			report.startStep("Verify Comletion Widgets on Home Page for second course");
			verifyStatisticsWidgets(expCompletion, expTimeOnTask, expTestScore, true);
				
			report.startStep("Verify My Progress Page title & course name");
			expCourseName = homePage.getCurrentCourseName();
			myProgressPage = homePage.clickOnMyProgress();
			sleep(1);
			testResultService.assertEquals(myProgressPageTitle, myProgressPage.getPageTitle(), "My Progress page title is not valid");
			testResultService.assertEquals(expCourseName, myProgressPage.getCourseName(), "Course name is not valid");
			testResultService.assertEquals(true, myProgressPage.printBtn.isDisplayed(), "Print btn not found");
			
			report.startStep("Verify Comletion Widgets on My Progress Page for second course");
			verifyStatisticsWidgets(expCompletion, expTimeOnTask, expTestScore, true);
						
		}
		
	}
	
	
	///New
	
	private void testWidgetDataLastProgress2(String className,
			String institutionId, boolean hasMoreThenOneCourse,
			String[] courses, int progress) throws Exception {
		
		report.startStep("Create new student and login");
		homePage = createUserAndLoginNewUXClass(className, institutionId);
		pageHelper.skipOptin();
		homePage.skipNotificationWindow();
		homePage.waitHomePageloadedFully();
				
		report.startStep("Enter Learning Area");
		homePage.navigateToRequiredCourseByList(courses[0]);
		homePage.waitHomePageloadedFully();
		homePage.clickOnContinueButton();

		report.startStep("Set progress Completion & Time");
		studentService.setProgressInFirstUnitInCourse(courses[0], studentId, 5);

		report.startStep("Go Back To Home Page");
		//homePage.clickToOpenNavigationBar();
		homePage.clickOnHomeButton();
		homePage.waitHomePageloadedFully();
		
		report.startStep("Generate test data");
		BigDecimal completion = roundExpectedProgress(studentService.calcCourseExpectedProgress(courses[0], studentId));
		int completionInt = completion.intValue();
		String expCompletion = String.valueOf(completionInt);
		String expTestScore = "100"; 
		
		report.startStep("Verify Comletion Widgets on Home Page");
		verifyStatisticsWidgets(expCompletion, "expTimeOnTask", null, false);
		
		report.startStep("Verify My Progress Page title & course name");
		String expCourseName = homePage.getCurrentCourseName();
		myProgressPage = homePage.clickOnMyProgress();
		sleep(1);
		
		testResultService.assertEquals(myProgressPageTitle, myProgressPage.getPageTitle(), "My Progress page title is not valid");
		testResultService.assertEquals(expCourseName, myProgressPage.getCourseName(), "Course name is not valid");
		testResultService.assertEquals(true, myProgressPage.printBtn.isDisplayed(), "Print btn not found");
		
		report.startStep("Verify Comletion Widgets on My Progress Page");
		verifyStatisticsWidgets(expCompletion, "expTimeOnTask", null, false);
		homePage.clickToOpenNavigationBar();
		homePage.clickOnHomeButton();
		homePage.waitHomePageloaded();
		
		if (hasMoreThenOneCourse) {
			
			report.startStep("Navigate to B1 and Enter Learning Area");
			//sleep(2);
			homePage.carouselNavigateNext();
			homePage.waitHomePageloadedFully();
			sleep(1);
			homePage.checkTextandClickOnCourseBtn("Start");

			report.startStep("Set progress Completion & Score & Time");
			studentService.setProgressInFirstUnitInCourse(courses[1],studentId, 0);
			//studentService.submitTestsForCourse(studentId, courses[1], true);

			report.startStep("Go Back To Home Page");
			sleep(2);
			//homePage.clickToOpenNavigationBar();
			homePage.clickOnHomeButton();
			homePage.waitHomePageloadedFully();
						
			report.startStep("Generate test data");
			completion = roundExpectedProgress(studentService.calcCourseExpectedProgress(courses[1], studentId));
			completionInt = progress;
			expCompletion = String.valueOf(completionInt);
			//expTimeOnTask = dbService.getStudentTimeOnTask(courses[1], studentId);
			
			report.startStep("Verify Comletion Widgets on Home Page for second course");
			verifyStatisticsWidgets(expCompletion, "expTimeOnTask", expTestScore, true);
				
			report.startStep("Verify My Progress Page title & course name");
			expCourseName = homePage.getCurrentCourseName();
			myProgressPage = homePage.clickOnMyProgress();
			sleep(1);
			testResultService.assertEquals(myProgressPageTitle, myProgressPage.getPageTitle(), "My Progress page title is not valid");
			testResultService.assertEquals(expCourseName, myProgressPage.getCourseName(), "Course name is not valid");
			testResultService.assertEquals(true, myProgressPage.printBtn.isDisplayed(), "Print btn not found");
			
			report.startStep("Verify Comletion Widgets on My Progress Page for second course");
			verifyStatisticsWidgets(expCompletion, "expTimeOnTask", expTestScore, true);
						
		}
		
	}
		
	private void verifyStatisticsWidgets(String expCompletion, String expTimeOnTask, String expTestScore, boolean testScoreExists) throws Exception {
						
		report.startStep("Verify Comletion Progress");
		String actCompletionProgress = homePage.getCompletionWidgetValue();
		testResultService.assertEquals(expCompletion, actCompletionProgress);
/*
		report.startStep("Verify Time Progress");
		String actTimeProgress = homePage.getTimeOnTaskWidgetValue();
		expTimeOnTask = expTimeOnTask.substring(0, 5);
		testResultService.assertEquals(expTimeOnTask, actTimeProgress);
	*/	
		
		if (testScoreExists) {
			report.startStep("Verify Score Progress");
			String actTestGrade = homePage.getScoreWidgetValue();
			testResultService.assertEquals(expTestScore, actTestGrade);
		}
		
	}
	@After
	public void tearDown() throws Exception {
			super.tearDown();
	}
	

}
