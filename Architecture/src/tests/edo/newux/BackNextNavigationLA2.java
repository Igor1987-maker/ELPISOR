package tests.edo.newux;

import java.util.ArrayList;
import java.util.List;

import org.apache.bcel.generic.Instruction;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.WebElement;

import pageObjects.edo.NewUxLearningArea;
import pageObjects.edo.NewUxLearningArea2;
import testCategories.AngularLearningArea;
import testCategories.NonAngularLearningArea;
import testCategories.inProgressTests;
import testCategories.reg2;
import testCategories.edoNewUX.CourseArea;
import Enums.ByTypes;
import Interfaces.TestCaseParams;

@Category(AngularLearningArea.class)
public class BackNextNavigationLA2 extends BasicNewUxTest {

	NewUxLearningArea2 learningArea2;
	
	private static final String lastTaskAlert = "You have reached the end of this course.\nYou can return to the home page or select another lesson in this unit.";
	private static final String firstTaskAlert = "This is the first task of the course.";
	
	@Before
	public void setup() throws Exception {
		super.setup();
		getUserAndLoginNewUXClass();
		//homePage.skipNotificationWindow();
		//pageHelper.skipOnBoardingHP();
		//homePage.waitHomePageloadedFully();

	}
	
	@Test
	@TestCaseParams(testCaseID = { "22493" })
	public void testNextButtonMoveToNextUnit() throws Exception {

		report.startStep("Get user and login");
		//getUserAndLoginNewUXClass();

		report.startStep("Navigate to FD, Unit 2, Lesson 7");
		learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "FD", 2, 7);

		report.startStep("Click on the Next button for 6 times");
		learningArea2.clickOnNextButton(6);
		
		report.startStep("Check that task 3 of exlore is opened and it is current");
		String currentStep = learningArea2.getStepNameFromHeader();
		String expectedStep = "Practice";
		testResultService.assertEquals(expectedStep, currentStep,"Wrong step is displayed");
		int tasksInStep = 3;
		learningArea2.openTaskBar();
		for (int i = 0; i < tasksInStep; i++) {
			if (i == tasksInStep-1) learningArea2.checkIfTaskHasCurrentState(i+1, true);
			else learningArea2.checkIfTaskHasCurrentState(i+1, false);
		}
		learningArea2.closeTaskBar();

		report.startStep("CLick the Next button again");
		learningArea2.clickOnNextButton();

		learningArea2.setUnitReflectionAndContinue(false);
		
		report.startStep("Check that next lesson is displayed");
		String currentLessonName = learningArea2.getLessonNameFromHeader();
		String expectedLessonName = "Where's the Milk?";
		testResultService.assertEquals(expectedLessonName, currentLessonName,"Lesson name is not correct");

		report.startStep("Check unit name");
		String expectedUnitName = "Unit 3: Supermarket Shopping";
		learningArea2.openLessonsList();
		String currentUnitName = learningArea2.getUnitTitleInLessonList();
		testResultService.assertEquals(expectedUnitName, currentUnitName,"Unit name is not correct");

	}
	
	@Test
	@TestCaseParams(testCaseID = { "34459" })
	public void testBackButtonMoveToNextUnit() throws Exception {

		report.startStep("Get user and login");
		//getUserAndLoginNewUXClass();

		report.startStep("Navigate to FD, Unit 2, Lesson 1");
		learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "FD", 2, 1);
		
		report.startStep("Check Prepare step ");
		testResultService.assertEquals(learningArea2.getStepNameFromHeader(), "Prepare", "Step Name is not correct");
		learningArea2.verifyTaskCounterValues(1, 1);
				
		report.startStep("Click the Back button");
		learningArea2.clickOnBackButton();
		learningArea2.checkAndBackFromUnitReflection();
	
		
		report.startStep("Check that previous Unit / last Lesson is displayed");
		String currentLessonName = learningArea2.getLessonNameFromHeader();
		String expectedLessonName = "Numbers: 11-20";
		testResultService.assertEquals(expectedLessonName, currentLessonName,"Lesson name is not correct");

		String expectedUnitName = "Unit 1: Introduction";
		learningArea2.openLessonsList();
		String currentUnitName = learningArea2.getUnitTitleInLessonList();
		learningArea2.closeLessonsList();
		testResultService.assertEquals(expectedUnitName, currentUnitName,"Unit name is not correct");

		report.startStep("Check task counter");
		learningArea2.verifyTaskCounterValues(6, 6);
		
		
		report.startStep("Click on Back btn to get to Explore step and check Task states");
		for (int i = 5; i >= 0; i--) {
			learningArea2.verifyTaskCounterValues(i+1, 6);
			learningArea2.clickOnBackButton();
		}
		
		
		testResultService.assertEquals(true, learningArea2.isTaskCounterHasIntroMode(), "Task Counter does not show Intro text");
		learningArea2.clickOnBackButton();
		
		report.startStep("Check Explore step");
		testResultService.assertEquals(learningArea2.getStepNameFromHeader(), "Explore", "Step Name is not correct");
		learningArea2.verifyTaskCounterValues(1, 1);
	}

	@Test
	@TestCaseParams(testCaseID = { "22509" })
	public void testNextButtonBehaviourIfLastTaskInCourse() throws Exception {
	
		report.startStep("Get user and login");
		//getUserAndLoginNewUXClass();

		report.startStep("Navigate to FD, unit 8 lesson 6");
		learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "FD", 8, 6);

		report.startStep("Click on the Next button for 7 times");
	
		for (int i = 0; i < 7; i++) {
			learningArea2.clickOnNextButton();
			Thread.sleep(500);
		}
		
		learningArea2.openTaskBar();
		learningArea2.checkIfTaskHasCurrentState(4, true);
		learningArea2.closeTaskBar();
		
					
		report.startStep("Click on Next btn and check alert / no action");
		String headerBeforeAction = learningArea2.getLessonNameFromHeader();
		learningArea2.clickOnNextButton();
		learningArea2.setUnitReflectionAndContinue(false);
		learningArea2.validateAlertModalByMessage(lastTaskAlert, true);
		String headerAfterAction = learningArea2.getLessonNameFromHeader();
		sleep(1);
		testResultService.assertEquals(true, headerBeforeAction.equals(headerAfterAction), "Learning area place changed though user should remain on the same page");

	}
	
	@Test
	@TestCaseParams(testCaseID = { "34460" })
	public void testBackButtonInFirstTaskInCourse() throws Exception {
		
		report.startStep("Get user and login");
		//getUserAndLoginNewUXClass();

		report.startStep("Navigate to B2, Unit 1, Lesson 1");
		learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "B2", 1, 1);
		String headerBeforeAction = learningArea2.getLessonNameFromHeader();

		report.startStep("Click on the Back btn and check alert / no action");
		learningArea2.clickOnBackButton();
		learningArea2.validateAlertModalByMessage(firstTaskAlert, true);
		String headerAfterAction = learningArea2.getLessonNameFromHeader();
		sleep(1);
		testResultService.assertEquals(true, headerBeforeAction.equals(headerAfterAction), "Learning area place changed though user should remain on the same page");

	}
	
	@Test
	@TestCaseParams(testCaseID = { "22487" })
	public void testNextButtonMoveToNextTaskStepLesson() throws Exception {
		
		report.startStep("Get new user and login");
			homePage.logOutOfED();
			createUserAndLoginNewUXClass();
			homePage.skipNotificationWindow();
			pageHelper.skipOnBoardingHP();
			homePage.waitHomePageloadedFully();
			
		report.startStep("Navigate to FD, unit 1, lesson 1");
			learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "FD", 1, 1);
			
		report.startStep("Click on Next to get to Explore Intro Page");
			learningArea2.clickOnNextButton(1);
			sleep(1);
			String expStep = "Explore";
			testResultService.assertEquals(expStep,learningArea2.getStepNameFromHeader(), "Step Name is not correct in Header");
			testResultService.assertEquals(expStep,learningArea2.getStepNameFromIntro(), "Step Name is not correct in Intro");
			
		report.startStep("Click on Next to get to Explore Task 1");
			sleep(1);
			learningArea2.clickOnNextButton(1);
			sleep(1);
			testResultService.assertEquals(learningArea2.getStepNameFromHeader(), expStep, "Step Name is not correct");

		report.startStep("Click on Next to get to Practice Intro Page");
			learningArea2.clickOnNextButton(1);
			sleep(1);
			expStep = "Practice";
			testResultService.assertEquals(learningArea2.getStepNameFromHeader(), expStep, "Step Name is not correct in Header");
			testResultService.assertEquals(learningArea2.getStepNameFromIntro(), expStep, "Step Name is not correct in Intro");
			
		report.startStep("Click on Next to get to Practice Task 1");
			learningArea2.clickOnNextButton(1);
			sleep(1);
			testResultService.assertEquals(learningArea2.getStepNameFromHeader(), expStep, "Step Name is not correct");

			sleep(3);
		report.startStep("Click the next button for 5 times");
			learningArea2.clickOnNextButton(5);
		
		report.startStep("Click on the 3rd task");
			learningArea2.clickOnTaskByNumber(3);
		
		report.startStep("Click on Next for 3 times");
			learningArea2.clickOnNextButton(3);
	
			learningArea2.openTaskBar();
			learningArea2.checkIfTaskHasCurrentState(6, true);
			learningArea2.closeTaskBar();

		report.startStep("Click on the Next button 2 times again - student should move to next lesson");
			learningArea2.clickOnNextButton(2);
			sleep(1);

			String currentLesson = learningArea2.getLessonNameFromHeader();
			testResultService.assertEquals("Letters: M-Z", currentLesson, "Lesson name is not correct", true);

	}

	@Test
	@TestCaseParams(testCaseID = { "34458" })
	public void testBackButtonMoveToNextTaskStepLesson() throws Exception {
		
		report.startStep("Get new user and login");
		//getUserAndLoginNewUXClass();
		
		report.startStep("Navigate to FD, unit 3, lesson 7, step 3, task 4");
		learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "FD", 3, 7);
		learningArea2.clickOnStep(3);
		learningArea2.clickOnTaskByNumber(4);
				
		report.startStep("Click on Back btn to get to Explore step and check Task states");
		for (int i = 3; i >= 0; i--) {
			learningArea2.verifyTaskCounterValues(i+1, 4);
			learningArea2.clickOnBackButton();
		}
		testResultService.assertEquals(true, learningArea2.isTaskCounterHasIntroMode(), "Task Counter does not show Intro text");
		learningArea2.clickOnBackButton();
		
		
		report.startStep("Check Explore Step Task 1");
		String expStep = "Explore";
		Thread.sleep(1000);
		testResultService.assertEquals(learningArea2.getStepNameFromHeader(), expStep, "Step Name is not correct");
		learningArea2.verifyTaskCounterValues(1, 1);
		
		report.startStep("Check Explore Step Intro");
		learningArea2.clickOnBackButton();
		testResultService.assertEquals(learningArea2.getStepNameFromHeader(), expStep, "Step Name is not correct in Header");
		testResultService.assertEquals(learningArea2.getStepNameFromIntro(), expStep, "Step Name is not correct in Intro");
		testResultService.assertEquals(true, learningArea2.isTaskCounterHasIntroMode(), "Task Counter does not show Intro text");
						
		report.startStep("Click on Back to get to Prepare Task 1");
		learningArea2.clickOnBackButton();
		expStep = "Prepare";
		testResultService.assertEquals(learningArea2.getStepNameFromHeader(), expStep, "Step Name is not correct");
		learningArea2.verifyTaskCounterValues(1, 1);
						
		report.startStep("Click on the Back button again - student should move to previous lesson");
		learningArea2.clickOnBackButton();
		sleep(1);

		String currentLesson = learningArea2.getLessonNameFromHeader();
		testResultService.assertEquals("Prepositions", currentLesson, "Lesson name is not correct", true);

	}
	
	@Test
	@TestCaseParams(testCaseID = { "22538" })
	public void testSkipLockedLesson() throws Exception {

		String className = configuration.getProperty("classname.locked");
		
		try {

			report.startStep("Lock lesson 2 in FD unit 1");
			pageHelper.LockLessonToClass(className, courses[0], 1, 2);

			report.startStep("Create user and login");
			//getUserAndLoginNewUXClass(className);
			//sleep(2);

			report.startStep("Navigate to FD Unit 1 lesson 1");
			learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "FD", 1, 1);
			//sleep(3);

			report.startStep("Click on Next button for 8 times to get to the  last task of the lesson");
			for (int i = 0; i < 9; i++) {
				learningArea2.clickOnNextButton();
				sleep(1);
			}
			
			report.startStep("Click on Next button and check that lesson 3 is displayed");
			learningArea2.clickOnNextButton();
			sleep(1);
			String currentLesson = learningArea2.getLessonNameFromHeader();
			testResultService.assertEquals("Letters: M-Z", currentLesson,"Lesson name is not correct", true);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			testResultService.addFailTest(e.toString(), false, true);
		} finally {
			pageHelper.UnlockCourseUnitLessonsToClass(className, courses[0], 1);
		}
	}

	@Test
	@TestCaseParams(testCaseID = { "22541" })
	public void testNextButtonSkipLockedUnit() throws Exception {

		String className = configuration.getProperty("classname.locked");
		
		try {
			report.startStep("Lock unit 2 in FD");
			pageHelper.LockUnitToClass(className, courses[0], 2);

			report.startStep("Create user and login");
			report.startStep("Navigate to FD, unit 1, Lesson 7");
			learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "FD", 1, 7);
						
			report.startStep("Click on Next button for 7 times to get to the last task");
			for (int i = 0; i < 10; i++) {
				learningArea2.clickOnNextButton();
			}

			report.startStep("Click on next and check that 1st lesson from unit 3 is displayed");
			learningArea2.clickOnNextButton();
			sleep(2);
			learningArea2.openLessonsList();
			String curretUnit = learningArea2.getUnitTitleInLessonList();
			testResultService.assertEquals("Unit 2: About Me", curretUnit, "Unit name is not correct");
			String currentLesson = learningArea2.getLessonNameFromHeader();
			testResultService.assertEquals("We're Neighbors!", currentLesson, "Lesson name is not correct");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			testResultService.addFailTest(e.toString(), false, true);
		} finally {
			pageHelper.UnlockCourseUnitLessonsToClass(className, courses[0], 2);
		}

	}
	
	@After
	public void tearDown() throws Exception {
			super.tearDown();
	}

}
