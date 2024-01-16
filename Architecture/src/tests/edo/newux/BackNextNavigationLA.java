package tests.edo.newux;

import java.util.ArrayList;
import java.util.List;

import org.apache.bcel.generic.Instruction;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.WebElement;

import pageObjects.edo.NewUxLearningArea;
import testCategories.NonAngularLearningArea;
import testCategories.inProgressTests;
import testCategories.reg2;
import testCategories.edoNewUX.CourseArea;
import Enums.ByTypes;
import Interfaces.TestCaseParams;

@Category(NonAngularLearningArea.class)
public class BackNextNavigationLA extends BasicNewUxTest {

	NewUxLearningArea learningArea;
	
	private static final String lastTaskAlert = "You have reached the end of this course.\n You can return to the home page or select another lesson in this unit.";
	private static final String firstTaskAlert = "This is the first task of the course.";
	
	@Test
	@TestCaseParams(testCaseID = { "22493" })
	public void testNextButtonMoveToNextUnit() throws Exception {

		report.startStep("Create user and login");
		createUserAndLoginNewUXClass();

		report.startStep("Navigate to FD, Unit 2, Lesson 7");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "FD", 2, 7);

		report.startStep("Click on the Next button for 4 times");

		NewUxLearningArea learningArea = new NewUxLearningArea(webDriver,
				testResultService);
		for (int i = 0; i < 4; i++) {
			sleep(1);
			learningArea.clickOnNextButton();
		}

		report.startStep("Check that tasks 3 of exlore is opened and all tasks are displayed as visited");
		String currentStep = learningArea.getStepName(3);
		String expectedStep = ". Practice";

		testResultService.assertEquals(expectedStep, currentStep,"Wrong step is displayed");
		for (int i = 0; i < 3; i++) {
			learningArea.checkThatTaskIsVisited(i);
		}

		report.startStep("CLick the Next button again");
		learningArea.clickOnNextButton();

		report.startStep("Check that next lesson is displayed");

		String currentLessonName = learningArea.getLessonName();
		String expectedLessonName = "Where's the Milk?";
		testResultService.assertEquals(expectedLessonName, currentLessonName,"Lesson name is not correct");

		report.startStep("Check unit name");

		String expectedUnitName = "Unit 3: Supermarket Shopping";
		String currentUnitName = learningArea.getHeaderTitle();
		testResultService.assertEquals(expectedUnitName, currentUnitName,"Unit name is not correct");

	}
	
	@Test
	@TestCaseParams(testCaseID = { "34459" })
	public void testBackButtonMoveToNextUnit() throws Exception {

		report.startStep("Create user and login");
		createUserAndLoginNewUXClass();

		report.startStep("Navigate to FD, Unit 2, Lesson 1");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "FD", 2, 1);
		
		report.startStep("Check Prepare step ");
		NewUxLearningArea learningArea = new NewUxLearningArea(webDriver, testResultService);
		learningArea.checkThatStepNameIsDisplayed("Prepare");
		learningArea.verifyTaskCounterValues(1, 1);
				
		report.startStep("CLick the Back button");
		learningArea.clickOnBackButton();

		report.startStep("Check that previous Unit / last Lesson is displayed");

		String currentLessonName = learningArea.getLessonName();
		String expectedLessonName = "Numbers: 11-20";
		testResultService.assertEquals(expectedLessonName, currentLessonName,"Lesson name is not correct");

		String expectedUnitName = "Unit 1: Introduction";
		String currentUnitName = learningArea.getHeaderTitle();
		testResultService.assertEquals(expectedUnitName, currentUnitName,"Unit name is not correct");

		report.startStep("Check task counter");
		learningArea.verifyTaskCounterValues(6, 6);
		
		report.startStep("Click on Back btn to get to Explore step and check Task states");
		for (int i = 5; i >= 0; i--) {
			learningArea.checkThatTaskIsVisitedAndCurrent(i);
			learningArea.checkIfTaskIsActive(i+1);
			learningArea.verifyTaskCounterValues(i+1, 6);
			learningArea.clickOnBackButton();
		}
		
		report.startStep("Check Explore step ");
		learningArea.checkThatStepNameIsDisplayed("Explore");
		learningArea.verifyTaskCounterValues(1, 1);
	}

	@Test
	@TestCaseParams(testCaseID = { "22509" })
	public void testNextButtonBehaviourIfLastTaskInCourse() throws Exception {
		report.startStep("Create user and login");
		createUserAndLoginNewUXClass();

		report.startStep("Navigate to FD, unit 8 lesson 6");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "FD", 8, 6);

		report.startStep("Click on the Next button for 5 times");

		NewUxLearningArea learningArea = new NewUxLearningArea(webDriver,
				testResultService);
		for (int i = 0; i < 5; i++) {
			webDriver.sleep(2);
			learningArea.clickOnNextButton();
		}

		/*for (int i = 0; i < 3; i++) {
			learningArea.checkThatTaskIsVisitedAndCurrent(i);
		}*/
		
		testResultService.assertEquals(true, learningArea.checkThatTaskIsVisitedAndCurrent(3));
		
		report.startStep("Click on the next button again");
		learningArea.clickOnNextButton();
		
		String alertText = webDriver.getAlertText(5);
		sleep(3);
		testResultService.assertEquals(lastTaskAlert, alertText, "Checking End-of-Course Alert Notification");

	}
	
	@Test
	@TestCaseParams(testCaseID = { "34460" })
	public void testBackButtonInFirstTaskInCourse() throws Exception {
		
		report.startStep("Create user and login");
		createUserAndLoginNewUXClass();

		report.startStep("Navigate to B2, Unit 1, Lesson 1");
		learningArea = homePage.navigateToCourseUnitAndLesson(courseCodes, "B2", 1, 1);
		sleep(2);
		String headerBeforeAction = learningArea.getHeaderTitle();

		report.startStep("Click on the Back btn and check alert / no action");
		learningArea.clickOnBackButton();
		String alertText = webDriver.getAlertText(3);
		webDriver.closeAlertByAccept();
		String headerAfterAction = learningArea.getHeaderTitle();
		
		testResultService.assertEquals(firstTaskAlert, alertText, "Checking Start-of-Course Alert Notification");
		testResultService.assertEquals(true, headerBeforeAction.equals(headerAfterAction), "Learning area place changed though user should remain on the same page");

	}
	
	@Test
	@TestCaseParams(testCaseID = { "22487" })
	public void testNextButtonMoveToNextTaskStepLesson() throws Exception {
		report.startStep("Create new user and login");
		createUserAndLoginNewUXClass();

		NewUxLearningArea learningArea = new NewUxLearningArea(webDriver,
				testResultService);

		report.startStep("Navigate to FD, unit 1, lesson 1");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "FD", 1, 1);
		sleep(2);

		report.startStep("Click on Next to get to Explore step");
		sleep(1);
		learningArea.clickOnNextButton();
		sleep(1);
		learningArea.checkThatStepNameIsDisplayed("Explore");

		report.startStep("Click on Next to get to Practice");
		learningArea.clickOnNextButton();
		sleep(1);
		learningArea.checkThatStepNameIsDisplayed("Practice");

		report.startStep("Click the next button for 5 times");
		for (int i = 0; i < 5; i++) {
			learningArea.checkThatTaskIsVisitedAndCurrent(i);
			learningArea.clickOnNextButton();
			sleep(1);
		}
				
		report.startStep("Click on the 3rd task");
		learningArea.clickOnTask(2);
		learningArea.checkIfTaskIsActive(3); 

		report.startStep("Click on Next for 3 times");
		for (int i = 0; i < 3; i++) {
			learningArea.clickOnNextButton();
			sleep(1);
		}

		learningArea.checkIfTaskIsActive(6);

		report.startStep("Click on the Next button again - student should move to next lesson");
		learningArea.clickOnNextButton();
		sleep(1);

		String currentLesson = learningArea.getLessonName();
		testResultService.assertEquals("Letters: M-Z", currentLesson, "Lesson name is not correct", true);

	}

	@Test
	@TestCaseParams(testCaseID = { "34458" })
	public void testBackButtonMoveToNextTaskStepLesson() throws Exception {
		
		report.startStep("Create new user and login");
		createUserAndLoginNewUXClass();

		NewUxLearningArea learningArea = new NewUxLearningArea(webDriver,testResultService);

		report.startStep("Navigate to FD, unit 3, lesson 7, step 3, task 4");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "FD", 3, 7);
		sleep(1);
		learningArea.clickOnStep(3);
		learningArea.clickOnTask(3);
				
		report.startStep("Click on Back btn to get to Explore step and check Task states");
		for (int i = 3; i >= 0; i--) {
			learningArea.checkThatTaskIsVisitedAndCurrent(i);
			learningArea.checkIfTaskIsActive(i+1);
			learningArea.verifyTaskCounterValues(i+1, 4);
			learningArea.clickOnBackButton();
		}
		
		report.startStep("Check Explore step ");
		learningArea.checkThatStepNameIsDisplayed("Explore");
		learningArea.verifyTaskCounterValues(1, 1);
				
		report.startStep("Click on Back to get to Prepare ");
		learningArea.clickOnBackButton();
		learningArea.checkThatStepNameIsDisplayed("Prepare");
		learningArea.verifyTaskCounterValues(1, 1);
				
		report.startStep("Click on the Back button again - student should move to previous lesson");
		learningArea.clickOnBackButton();
		sleep(1);

		String currentLesson = learningArea.getLessonName();
		testResultService.assertEquals("Prepositions", currentLesson, "Lesson name is not correct", true);

	}
	
	@Test
	@TestCaseParams(testCaseID = { "22538" })
	public void testSkipLockedLesson() throws Exception {

		String className = configuration.getProperty("classname.locked");
		NewUxLearningArea learningArea = new NewUxLearningArea(webDriver,
				testResultService);
		try {

			report.startStep("Lock lesson 2 in FD unit 1");
			pageHelper.LockLessonToClass(className, courses[0], 1, 2);

			report.startStep("Create user and login");
			createUserAndLoginNewUXClass(className);

			report.startStep("Navigate to FD Unit 1 lesson 1");
			homePage.navigateToCourseUnitAndLesson(courseCodes, "FD", 1, 1);

			report.startStep("Click on Next button for 7 times to get to the  last task of the lesson");
			for (int i = 0; i < 8; i++) {
				learningArea.clickOnNextButton();
				sleep(1);
			}
			sleep(2);
			
			report.startStep("Click on Next button and check that lesson 3 is displayed");
			String currentLesson = learningArea.getLessonName();
			testResultService.assertEquals("Vowels", currentLesson,"Lesson name is not correct", true);

		} catch (Exception e) {
			// TODO Auto-generated catch block

		} finally {
			pageHelper.UnlockCourseUnitLessonsToClass(className, courses[0], 1);
		}
	}

	@Test
	@TestCaseParams(testCaseID = { "22541" })
	public void testNextButtonSkipLockedUnit() throws Exception {

		String className = configuration.getProperty("classname.locked");
		NewUxLearningArea learningArea = new NewUxLearningArea(webDriver,
				testResultService);

		try {
			report.startStep("Lock unit 2 in FD");
			pageHelper.LockUnitToClass(className, courses[0], 2);

			report.startStep("Create user and login");
			createUserAndLoginNewUXClass(className);

			report.startStep("Navigate to FD, unit 1, Lesson 7");
			homePage
					.navigateToCourseUnitAndLesson(courseCodes, "FD", 1, 7);

			report.startStep("Click on Next button for 7 times to get to the last task");
			for (int i = 0; i < 9; i++) {
				learningArea.clickOnNextButton();
				sleep(1);
			}

			report.startStep("Click on next and check that 1st lesson from unit 3 is displayed");
			learningArea.clickOnNextButton();
			String curretUnit = learningArea.getHeaderTitle();

			testResultService.assertEquals(
					"Unit 3: Supermarket Shopping", curretUnit,
					"Unit name is not correct");

			// String currentLesson = learningArea.getLessonName();

			// testResultService.assertEquals("Where's the Milk?",
			// currentLesson,
			// "Lesson name is not correct");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			pageHelper.UnlockCourseUnitLessonsToClass(className, courses[0], 2);
		}

	}

	@Test
	@TestCaseParams(testCaseID = { "22091" })
	public void testTaskProgressDoNotSaveVisitedStatus() throws Exception {
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
		sleep(1);
		learningArea.clickOnTask(2);
		sleep(1);
		learningArea.checkThatTaskIsVisited(0);
		learningArea.checkThatTaskIsVisited(1);
		learningArea.checkThatTaskIsVisitedAndCurrent(2);

		learningArea.clickOnStep(1);
		sleep(2);
		learningArea.approveTest();
		sleep(2);
		learningArea.checkThatTaskIsVisitedAndCurrent(0);

		report.startStep("Go to Test step again");
		webDriver.switchToTopMostFrame();
		learningArea.clickOnStep(3);
		sleep(1);
		learningArea.clickOnStartTest();
		sleep(1);
		learningArea.checkThatTaskIsVisitedAndCurrent(0);
		learningArea.chechThatTaskIsNotVisited(1);
		learningArea.chechThatTaskIsNotVisited(2);

		report.startStep("Go to homepage and back to the lesson area");
		//learningArea.clickToOpenNavigationBar();
		learningArea.clickOnHomeButton();
		homePage.waitHomePageloaded();
		learningArea.approveTest();
		sleep(2);
		
		homePage.navigateToCourseUnitAndLesson(courseCodes, "A3", 8, 6);
		sleep(1);
		learningArea.clickOnStep(2);
		sleep(2);
		learningArea.checkThatTaskIsVisitedAndCurrent(0);
		learningArea.chechThatTaskIsNotVisited(1);
		learningArea.chechThatTaskIsNotVisited(2);

		learningArea.clickOnStep(3);
		sleep(2);
		learningArea.clickOnStartTest();

		learningArea.checkThatTaskIsVisitedAndCurrent(0);
		learningArea.chechThatTaskIsNotVisited(1);
		learningArea.chechThatTaskIsNotVisited(2);

	}
	

}
