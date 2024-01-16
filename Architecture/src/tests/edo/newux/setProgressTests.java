package tests.edo.newux;

import java.math.BigDecimal;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import Enums.InteractStatus;
import Interfaces.TestCaseParams;
import pageObjects.InteractSection;
import pageObjects.edo.NewUXLoginPage;
import pageObjects.edo.NewUxLearningArea;
import testCategories.NonAngularLearningArea;
import testCategories.inProgressTests;
import testCategories.reg3;
import testCategories.unstableTests;

@Category(NonAngularLearningArea.class)
public class setProgressTests extends BasicNewUxTest {

	NewUxLearningArea learningArea;
	NewUXLoginPage loginPage;

	private static final String htmlEditorXpath = "//body[@id='tinymce']";
	private static final String freeText = "Some text for test some text for test some text for test some text for test.";
	private static final String htmlEditorFrame = "elm1_ifr";
	
	
	@Before
	public void setup() throws Exception {
		super.setup();
		report.startStep("Create user and login");
		createUserAndLoginNewUXClass();
		learningArea = new NewUxLearningArea(webDriver, testResultService);

	}

	// bug is open
	@Test
	@TestCaseParams(testCaseID = { "22884" })
	public void testSetProgressExploreSpeaking() throws Exception {
		report.startStep("Navigate to course, Unit and lesson");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "B3", 4, 3);
		//
		String courseId = getCourseIdByCourseCode("B3");

		String itemId = getItemIdByCourseUnitLessonStepAndTask(courseId, 4, 3, 1, 1);

		report.startStep("Click on Play button");
		learningArea.clickOnPlayVideoButton();

		sleep(5);
		learningArea.clickOnNextButton();

		learningArea.clickOnStep(1);
		report.startStep("Check that task stutus is done");

		learningArea.checkThatTaskIsDone(0);
		webDriver.refresh();
		learningArea.checkThatTaskIsDone(0);

		// learningArea.checkThatTaskIsDone(0);
		//learningArea.clickToOpenNavigationBar();
		learningArea.clickOnHomeButton();

		report.startStep("Check the progress in the DB");
		studentService.checkStudentProgress(studentId, courseId, itemId);

	}

	@Test
	@TestCaseParams(testCaseID = { "22886" })
	public void testSetProgressExploreListening() throws Exception {
		report.startStep("Navigate");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "FD", 2, 1);
		String courseId = getCourseIdByCourseCode("FD");

		String itemId = getItemIdByCourseUnitLessonStepAndTask(courseId, 2, 1, 1, 1);
		sleep(2);
		report.startStep("Click on Play button");
		learningArea.clickOnPlayVideoButtonPrepare();

		sleep(3);
		learningArea.clickOnNextButton();

		learningArea.clickOnStep(1);
		report.startStep("Check that task stutus is done");

		learningArea.checkThatTaskIsDone(0);
		webDriver.refresh();
		learningArea.checkThatTaskIsDone(0);
		//learningArea.clickToOpenNavigationBar();
		learningArea.clickOnHomeButton();

		report.startStep("Check the progress in the DB");
		studentService.checkStudentProgress(studentId, courseId, itemId);
	}

	@Test
	@TestCaseParams(testCaseID = { "22888" })
	public void testSetProgressExploreReading() throws Exception {
		report.startStep("Navigate");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "A3", 5, 2);

		String courseId = getCourseIdByCourseCode("A3");

		String itemId = getItemIdByCourseUnitLessonStepAndTask(courseId, 5, 2, 1, 1);
		sleep(2);
		learningArea.clickOnNextButton();
		sleep(2);

		learningArea.clickOnStep(1);

		learningArea.checkThatTaskIsDone(0);
		webDriver.refresh();
		learningArea.checkThatTaskIsDone(0);
		//learningArea.clickToOpenNavigationBar();
		learningArea.clickOnHomeButton();

		report.startStep("Check the progress in the DB");
		studentService.checkStudentProgress(studentId, courseId, itemId);

	}

	@Test
	@TestCaseParams(testCaseID = { "22890" })
	public void testSetProgressExploreVocabulary() throws Exception {
		report.startStep("Navigate");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "B1", 2, 6);

		String courseId = getCourseIdByCourseCode("B1");

		String itemId = getItemIdByCourseUnitLessonStepAndTask(courseId, 2, 6, 1, 1);
		sleep(2);
		learningArea.clickOnNextButton();
		sleep(2);
		learningArea.clickOnStep(1);

		learningArea.checkThatTaskIsDone(0);
		webDriver.refresh();
		learningArea.checkThatTaskIsDone(0);
		//learningArea.clickToOpenNavigationBar();
		learningArea.clickOnHomeButton();

		report.startStep("Check the progress in the DB");
		studentService.checkStudentProgress(studentId, courseId, itemId);
	}

	@Test
	@TestCaseParams(testCaseID = { "22892" })
	public void testSetProgressExploreAlphabet() throws Exception {
		report.startStep("Navigate");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "FD", 1, 1);

		String courseId = getCourseIdByCourseCode("FD");

		// String itemId = getItemIdByCourseUnitLessonStepAndTask(courseId, 1,
		// 1, 1, 1);

		String itemId = getItemIdByCourseUnitLessonStepAndTask(courseId, 1, 1, 2, 1);

		learningArea.clickOnStep(2);
		sleep(1);  //Added one second sleep to increase stability. 
		learningArea.clickOnNextButton();

		learningArea.clickOnStep(2);

		learningArea.checkThatTaskIsDone(0);
		webDriver.refresh();
		learningArea.clickOnStep(2);
		learningArea.checkThatTaskIsDone(0);
		//learningArea.clickToOpenNavigationBar();
		learningArea.clickOnHomeButton();

		report.startStep("Check the progress in the DB");
		studentService.checkStudentProgress(studentId, courseId, itemId);
	}

	@Test
	@TestCaseParams(testCaseID = { "22894" })
	public void testSetProgressPracticeAlphabetMatchingGame() throws Exception {
		report.startStep("Navigate");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "FD", 1, 1);

		learningArea.clickOnStep(3);
		learningArea.clickOnTask(2);

		learningArea.openCard(1);
		sleep(3);
		learningArea.openCard(3);
		sleep(3);
		learningArea.clickOnNextButton();
		learningArea.clickOnTask(2);

		learningArea.checkThatTaskIsDone(2);
		webDriver.refresh();
		learningArea.clickOnStep(3);
		learningArea.checkThatTaskIsDone(2);
		//learningArea.clickToOpenNavigationBar();
		learningArea.clickOnHomeButton();

		String courseId = getCourseIdByCourseCode("FD");

		String itemId = getItemIdByCourseUnitLessonStepAndTask(courseId, 1, 1, 3, 3);

		report.startStep("Check the progress in the DB");
		studentService.checkStudentProgress(studentId, courseId, itemId);
	}

	@Test
	@TestCaseParams(testCaseID = { "22896","25807" }, skippedBrowsers = {"firefox"}) // Actions API not supported by geckodriver)
	public void testSetProgressPracticeListeningCloseDragAndDrop() throws Exception {
		report.startStep("Navigate");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "A3", 6, 1);
		learningArea.clickOnStep(2);
		learningArea.clickOnTask(1);

		report.startStep("Perform drag and drop");
		learningArea.dragAndDropAnswerById("58");
		sleep(1);
		learningArea.clickOnCheckAnswer();
		sleep(1);

		report.startStep("Click on Next and go to the home page");
		learningArea.clickOnNextButton();
		learningArea.clickOnTask(1);

		learningArea.checkThatTaskIsDone(1);
		webDriver.refresh();
		learningArea.clickOnStep(2);
		learningArea.checkThatTaskIsDone(1);
		//learningArea.clickToOpenNavigationBar();
		learningArea.clickOnHomeButton();

		report.startStep("Check the progress in the DB");
		String courseId = getCourseIdByCourseCode("A3");
		String itemId = getItemIdByCourseUnitLessonStepAndTask(courseId, 6, 1, 2, 2);

		studentService.checkStudentProgress(studentId, courseId, itemId);
	}

	@Test
	@TestCaseParams(testCaseID = { "22901" })
	public void testSetProgressPracticeListeningMarkTheTrue() throws Exception {
		report.startStep("Navigate");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "B3", 3, 2);
		learningArea.clickOnStep(2);
		learningArea.clickOnTask(1);

		report.startStep("select  answer and click on check answer");
		learningArea.checkCheckBox(3);
		sleep(1);
		learningArea.clickOnCheckAnswer();
		sleep(1);
		learningArea.clickOnNextButton();
		sleep(1);
		learningArea.clickOnTask(1);

		learningArea.checkThatTaskIsDone(1);
		webDriver.refresh();
		learningArea.clickOnStep(2);
		sleep(1);
		learningArea.clickOnTask(1);
		learningArea.checkThatTaskIsDone(1);
		//learningArea.clickToOpenNavigationBar();
		learningArea.clickOnHomeButton();
		report.startStep("Check the progress in the DB");
		String courseId = getCourseIdByCourseCode("B3");
		String itemId = getItemIdByCourseUnitLessonStepAndTask(courseId, 3, 2, 2, 2);

		studentService.checkStudentProgress(studentId, courseId, itemId);

	}

	@Test
	@TestCaseParams(testCaseID = { "22939" })
	public void testSetProgressPracticeListeningFillTheBlanks() throws Exception {
		report.startStep("Navigate");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "A3", 6, 1);
		learningArea.clickOnStep(2);

		learningArea.clickOnTask(2);

		report.startStep("Select answer fro  drop down");
		learningArea.selectAnswerFromDropDown("1_1", "angry reaction");
		learningArea.clickOnCheckAnswer();

		report.startStep("Check that task is marked as done");
		learningArea.clickOnNextButton();
		learningArea.clickOnTask(2);
		learningArea.checkThatTaskIsDone(2);

		webDriver.refresh();
		learningArea.clickOnStep(2);
		learningArea.checkThatTaskIsDone(2);
		//learningArea.clickToOpenNavigationBar();
		learningArea.clickOnHomeButton();
		report.startStep("Check the progress in the DB");
		String courseId = getCourseIdByCourseCode("A3");
		String itemId = getItemIdByCourseUnitLessonStepAndTask(courseId, 6, 1, 2, 3);

		studentService.checkStudentProgress(studentId, courseId, itemId);

	}

	@Test
	@TestCaseParams(testCaseID = { "22961" })
	public void testSetProgressPracticeListeningMCQ() throws Exception {
		report.startStep("Navigate");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "FD", 5, 1);
		learningArea.clickOnStep(3);

		learningArea.clickOnTask(2);

		report.startStep("Select an answer");
		learningArea.selectAnswerRadio(1, 3);
		learningArea.clickOnCheckAnswer();

		report.startStep("Check that task is marked as done");
		// learningArea.clickOnNextButton();
		learningArea.clickOnTask(1);

		// learningArea.clickOnTask(2);

		learningArea.checkThatTaskIsDone(2);
		webDriver.refresh();
		learningArea.clickOnStep(3);
		learningArea.checkThatTaskIsDone(2);
		//learningArea.clickToOpenNavigationBar();
		learningArea.clickOnHomeButton();
		report.startStep("Check the progress in the DB");
		String courseId = getCourseIdByCourseCode("FD");
		String itemId = getItemIdByCourseUnitLessonStepAndTask(courseId, 5, 1, 3, 3);

		studentService.checkStudentProgress(studentId, courseId, itemId);

	}

	@Test
	@TestCaseParams(testCaseID = { "22963" })
	public void testSetProgressPracticeReadingFreeWriteNegative() throws Exception {
		report.startStep("Navigate");

		// TODO Continue From This Test
		homePage.navigateToCourseUnitAndLesson(courseCodes, "A3", 5, 2);
		learningArea.clickOnStep(2);
		sleep(1);
		learningArea.clickOnTask(5);
		sleep(1);
		learningArea.clickOnTask(4);
		sleep(2);
		learningArea.clickOnTask(5);
		sleep(3);
		
		report.startStep("Click on clear text");
		learningArea.clickOnClearAnswer();

		report.startStep("Click on other step and go back");
		learningArea.clickOnStep(3);
		learningArea.clickOnStep(2);

		report.startStep("Check that task is mark as visited");
		testResultService.assertEquals(true, learningArea.checkThatTaskIsVisited(5),
				"Task state is not correct", true);

		webDriver.refresh();
		// webDriver.navigate(webDriver.getUrl());

		learningArea.clickOnStep(2);
		testResultService.assertEquals(true, learningArea.checkThatTaskIsVisited(5), "Task state is not correct", true);

		report.startStep("Go to home page and check that progress was not set");
		//learningArea.clickToOpenNavigationBar();
		learningArea.clickOnHomeButton();

		String courseId = getCourseIdByCourseCode("A3");
		String itemId = getItemIdByCourseUnitLessonStepAndTask(courseId, 5, 2, 2, 6);

		studentService.checkStudentHasNoProgress(studentId, courseId, itemId);
	}

	
	@Test
	@TestCaseParams(testCaseID = { "22964" })
	public void testSetProgressPracticeReadingFreeWritesERaterPositive() throws Exception {
		report.startStep("Navigate");

		homePage.navigateToCourseUnitAndLesson(courseCodes, "A3", 5, 2);
		learningArea.clickOnStep(2);
		learningArea.clickOnTask(5);
		sleep(3);

		report.startStep("Enter some text");
		webDriver.swithcToFrameAndSendKeys(htmlEditorXpath,
				freeText, htmlEditorFrame);

		report.startStep("Click on submit text");
		webDriver.switchToTopMostFrame();
		learningArea.clickOnSubmitText();

		report.startStep("Click on other step and go back");
		learningArea.clickOnStep(1);
		learningArea.clickOnStep(2);

		report.startStep("Check that task is mark as visited");
		learningArea.checkThatTaskIsDone(5);
		webDriver.refresh();
		learningArea.clickOnStep(2);
		learningArea.checkThatTaskIsDone(5);

		report.startStep("Go to home page and check that progress set");
		//learningArea.clickToOpenNavigationBar();
		learningArea.clickOnHomeButton();

		String courseId = getCourseIdByCourseCode("A3");
		String itemId = getItemIdByCourseUnitLessonStepAndTask(courseId, 5, 2, 2, 6);

		studentService.checkStudentProgress(studentId, courseId, itemId);
	}

	
	@Test
	@TestCaseParams(testCaseID = { "22968" })
	public void testSetProgressPracticeListeningOpenEnded() throws Exception {
		report.startStep("Navifate");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "A3", 6, 1);
		learningArea.clickOnStep(2);
		sleep(1);
		learningArea.clickOnTask(5);

		report.startStep("Click on check answer");

		learningArea.enterAnswerTextByIndex("0", "bla bla");
		learningArea.clickOnCheckAnswer();
		learningArea.clickOnTask(4);
		sleep(1);
		learningArea.clickOnTask(5);

		report.startStep("Check that task is marked as done");
		learningArea.checkThatTaskIsDone(5);
		webDriver.refresh();
		sleep(1);
		learningArea.clickOnStep(2);
		learningArea.checkThatTaskIsDone(5);
		//learningArea.clickToOpenNavigationBar();
		learningArea.clickOnHomeButton();
		report.startStep("Check the progress in the DB");
		String courseId = getCourseIdByCourseCode("A3");
		String itemId = getItemIdByCourseUnitLessonStepAndTask(courseId, 6, 1, 2, 6);

		studentService.checkStudentProgress(studentId, courseId, itemId);

	}

	// bug 23765
	@Test
	@Category(unstableTests.class)
	@TestCaseParams(testCaseID = { "22862" })
	public void testSetProgressPrepareListeningMedia2PagesNegative() throws Exception {
		report.startStep("Navigate");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "FD", 2, 1);

		report.startStep("Click on page 2 below image");
		learningArea.clickOnPage2();

		learningArea.clickOnNextButton();

		learningArea.clickOnStep(1);

		report.startStep("Check that task is marked as visited");
		learningArea.checkThatTaskIsVisitedAndCurrent(0);

		webDriver.refresh();
		testResultService.assertEquals(true, learningArea.checkThatTaskIsVisited(0), "Task state is not correct", true);

		report.startStep("Go To home page and check that progress is not created");
		//learningArea.clickToOpenNavigationBar();
		learningArea.clickOnHomeButton();

		report.startStep("Check the progress in the DB");
		String courseId = getCourseIdByCourseCode("FD");
		String itemId = getItemIdByCourseUnitLessonStepAndTask(courseId, 2, 1, 1, 1);

		studentService.checkStudentHasNoProgress(studentId, courseId, itemId);

	}

	@Test
	@TestCaseParams(testCaseID = { "22864" })
	public void testSetProgressPrepareListeningMedia2Pages() throws Exception {
		report.startStep("Navigate");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "FD", 2, 1);

		report.startStep("Click on page 2 below image");
		learningArea.clickOnPage2();
		sleep(2);

		learningArea.clickOnPlayVideoButtonPrepare();

		learningArea.clickOnNextButton();

		learningArea.clickOnStep(1);

		report.startStep("Check that task is marked as visited");
		learningArea.checkThatTaskIsDone(0);
		webDriver.refresh();
		learningArea.checkThatTaskIsDone(0);

		report.startStep("Go To home page and check that progress is not created");
		//learningArea.clickToOpenNavigationBar();
		learningArea.clickOnHomeButton();

		report.startStep("Check the progress in the DB");
		String courseId = getCourseIdByCourseCode("FD");
		String itemId = getItemIdByCourseUnitLessonStepAndTask(courseId, 2, 1, 1, 1);

		studentService.checkStudentProgress(studentId, courseId, itemId);

	}

	@Test
	@TestCaseParams(testCaseID = { "22881" })
	public void testSetProgressExploreSpeakingNegative() throws Exception {
		report.startStep("Navigate");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "B3", 4, 3);

		report.startStep("Click in see script and hear part");
		learningArea.clickOSeeScript();
		// learningArea.clickOnHearPart();

		report.startStep("Click on next and go back - check that task is marked as visited");
		learningArea.clickOnNextButton();

		learningArea.clickOnStep(1);

		learningArea.checkThatTaskIsVisitedAndCurrent(0);
		webDriver.refresh();
		testResultService.assertEquals(true, learningArea.checkThatTaskIsVisited(0), "Task state is not correct", true);

		report.startStep("Go back to Home page and check that progress was not set");
		//learningArea.clickToOpenNavigationBar();
		learningArea.clickOnHomeButton();

		String courseId = getCourseIdByCourseCode("B3");
		String itemId = getItemIdByCourseUnitLessonStepAndTask(courseId, 4, 3, 1, 1);

		studentService.checkStudentHasNoProgress(studentId, courseId, itemId);

	}

	@Test
	@TestCaseParams(testCaseID = { "22975" })
	public void testSetProgressTestGrammar() throws Exception {
		report.startStep("Navigate");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "I2", 5, 4);

		learningArea.clickOnStep(3);

		sleep(2);

		report.startStep("Start the Test ");
		learningArea.clickOnStartTest();
		learningArea.clickOnTask(3);

		report.startStep("Submit the test, check that all taskss are done");
		learningArea.submitTest(true);
		sleep(2);
		

		for (int i = 0; i < 4; i++) {
			learningArea.checkThatTaskIsDone(i);
		}

		report.startStep("Go to the home page and check the progress");
		//learningArea.clickToOpenNavigationBar();
		learningArea.clickOnHomeButton();

		String courseId = getCourseIdByCourseCode("I2");
		// String itemId = getItemIdByCourseUnitLessonStepAndTask(courseId, 5,
		// 4,
		// 3, 4);

		String compSubComp = getCompoSubCompByCourseUnitAndStep(courseId, 5, 4, 3);
		studentService.checkStudentTestResult(studentId, compSubComp);

	}

	private String getCompoSubCompByCourseUnitAndStep(String courseId, int unitIndex, int compIndex, int subCompIndex)
			throws Exception {
		String unitId = dbService.getCourseUnits(courseId).get(unitIndex - 1);

		// lesson id
		String compId = dbService.getComponentDetailsByUnitId(unitId).get(compIndex - 1)[1];
		// step id
		String SubComp = dbService.getSubComponentsDetailsByComponentId(compId).get(subCompIndex - 1)[1];

		return SubComp;
	}

	@Test
	@TestCaseParams(testCaseID = { "22885" })
	public void testSetProgressExploreListeningTVNegative() throws Exception {
		homePage.navigateToCourseUnitAndLesson(courseCodes, "FD", 2, 1);

		report.startStep("Click in see script and hear part");

		learningArea.clickOnStep(2);

		sleep(2);
		learningArea.clickOSeeScript();
		// learningArea.clickOnHearPart();
		sleep(2);
		learningArea.clickOnNextButton();

		learningArea.clickOnStep(2);

		report.startStep("Check that task is marked as visited");
		learningArea.checkThatTaskIsVisitedAndCurrent(0);

		webDriver.refresh();
		learningArea.clickOnStep(2);
		testResultService.assertEquals(true, learningArea.checkThatTaskIsVisited(0), "Task state is not correct", true);
		report.startStep("return to home page and check that progress is not set");
		//learningArea.clickToOpenNavigationBar();
		learningArea.clickOnHomeButton();

		String courseId = getCourseIdByCourseCode("FD");
		String itemId = getItemIdByCourseUnitLessonStepAndTask(courseId, 2, 1, 1, 1);

		studentService.checkStudentHasNoProgress(studentId, courseId, itemId);

	}

	@Test
	@TestCaseParams(testCaseID = { "22887" })
	public void testSetProgressExploreReadinPositive1() throws Exception {
		
		report.startStep("Navigate");
		sleep(2);
		homePage.navigateToCourseUnitAndLesson(courseCodes, "A3", 5, 2);

		report.startStep("Click on hear all, hear part, key words");
		sleep(2);
		learningArea.clickOnHearAll();
		sleep(5);
		// learningArea.clickOnHearPart();
		// sleep(1);
		learningArea.clickOnKeyWords();

		report.startStep("click on Next and then back to step 1");
		learningArea.clickOnStep(2);
		learningArea.clickOnStep(1);

		learningArea.checkThatTaskIsDone(0);
		webDriver.refresh();
		learningArea.checkThatTaskIsDone(0);

		/*
		 * learningArea.checkThatTaskIsVisitedAndCurrent(0);
		 * webDriver.refresh(); testResultService.assertEquals(true,
		 * learningArea.checkThatTaskIsVisited(0), "Task state is not correct",
		 * true);
		 */

		report.startStep("return to home page and check that progress is not set");
		//learningArea.clickToOpenNavigationBar();
		learningArea.clickOnHomeButton();

		String courseId = getCourseIdByCourseCode("A3");
		String itemId = getItemIdByCourseUnitLessonStepAndTask(courseId, 5, 2, 1, 1);

		studentService.checkStudentProgress(studentId, courseId, itemId);

	}

	@Test
	@TestCaseParams(testCaseID = { "22889" })
	public void testSetProgressExporeVocabularyNewPositive2() throws Exception {
		report.startStep("Navigate");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "B1", 2, 6);

		report.startStep("Presss on any word and open the tool and press hear");

		learningArea.clickOnWordByIndex(4);

		learningArea.clickOnToolHear();
		sleep(2);

		learningArea.clickOnStep(2);
		sleep(2);
		
		report.startStep("Return to explore step");

		learningArea.clickOnStep(1);
		sleep(2);
		
		learningArea.checkThatTaskIsDone(0);
		webDriver.refresh();
		learningArea.checkThatTaskIsDone(0);

		report.startStep("Return to the home page and check that no progress was done");
		//learningArea.clickToOpenNavigationBar();
		learningArea.clickOnHomeButton();

		String courseId = getCourseIdByCourseCode("B1");
		String itemId = getItemIdByCourseUnitLessonStepAndTask(courseId, 2, 6, 1, 1);

		studentService.checkStudentProgress(studentId, courseId, itemId);
	}

	@Test
	@TestCaseParams(testCaseID = { "22891" })
	public void testSetProgressExploreAlphabetPositive() throws Exception {
		report.startStep("Navigate");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "FD", 1, 1);
		learningArea.clickOnStep(2);

		learningArea.clickOnVocabWordByIndex(2);
		learningArea.clickOnStep(3);
		sleep(1);

		learningArea.clickOnStep(2);
		learningArea.checkThatTaskIsDone(0);

		report.startStep("Refresh and do to Explore step 2");
		webDriver.refresh();
		learningArea.clickOnStep(2);
		learningArea.checkThatTaskIsDone(0);

		report.startStep("Go back to the home and check that no progress was created");
		//learningArea.clickToOpenNavigationBar();
		learningArea.clickOnHomeButton();
		String courseId = getCourseIdByCourseCode("FD");
		String itemId = getItemIdByCourseUnitLessonStepAndTask(courseId, 1, 1, 2, 1);
		studentService.checkStudentProgress(studentId, courseId, itemId);

	}

	@Test
	@TestCaseParams(testCaseID = { "22893" })
	public void testSetProgressPracticeAlphabetMatchingGamePositive1() throws Exception {
		report.startStep("Create user and navigate");

		homePage.navigateToCourseUnitAndLesson(courseCodes, "FD", 1, 1);

		learningArea.clickOnStep(3);
		learningArea.clickOnTask(2);
		sleep(2);
		learningArea.openCard(2);
		sleep(3);
		learningArea.openCard(3);

		learningArea.clickOnClearAnswer();

		learningArea.clickOnStep(2);

		sleep(1);

		learningArea.clickOnStep(3);

		learningArea.checkThatTaskIsDone(2);
		webDriver.refresh();
		learningArea.clickOnStep(3);
		learningArea.checkThatTaskIsDone(2);
		//learningArea.clickToOpenNavigationBar();
		learningArea.clickOnHomeButton();
		report.startStep("Check the progress");

		String courseId = getCourseIdByCourseCode("FD");
		String itemId = getItemIdByCourseUnitLessonStepAndTask(courseId, 1, 1, 3, 3);

		studentService.checkStudentProgress(studentId, courseId, itemId);

	}

	@Test
	@TestCaseParams(testCaseID = { "22895" }, skippedBrowsers = {"firefox"}) // Actions API not supported by geckodriver)
	public void testSetProgressPracticeListeningCloseDragAndDropNegative() throws Exception {
		webDriver.maximize();
		homePage.navigateToCourseUnitAndLesson(courseCodes, "A3", 6, 1);

		learningArea.clickOnStep(2);
		sleep(1);
		learningArea.clickOnTask(1);
		sleep(1);

		report.startStep("Click on paly media");
		learningArea.clickOnPlaMediaByIndex("2");

		sleep(2);

		report.startStep("Drag and drop answer");

		learningArea.dragAndDropAnswerFromPracticeBackToAnswerBank("58");
		sleep(1);
		report.startStep("Click on See Answer");
		learningArea.clickOnSeeAnswer();
		sleep(1);
		learningArea.clickOnClearAnswer();

		learningArea.clickOnStep(1);
		sleep(1);
		learningArea.clickOnStep(2);

		learningArea.checkThatTaskIsVisited(1);
		webDriver.refresh();
		learningArea.clickOnStep(2);
		learningArea.checkThatTaskIsVisited(1);
		
		report.startStep("Check the progress in the DB");
		String courseId = getCourseIdByCourseCode("A3");
		String itemId = getItemIdByCourseUnitLessonStepAndTask(courseId, 6, 1, 2, 2);

		studentService.checkStudentHasNoProgress(studentId, courseId, itemId);
	}

	@Test
	@TestCaseParams(testCaseID = { "22900" })
	public void testSetProgressPracticeListeningMarkTheTrueNegative() throws Exception {
		report.startStep("Navigate");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "B3", 3, 2);
		learningArea.clickOnStep(2);
		sleep(1);
		learningArea.clickOnTask(1);
		sleep(1);
		learningArea.clickOnTask(4);
		sleep(1);
		learningArea.clickOnTask(1);
		sleep(1);
		report.startStep("select  answer and click on check answer");
		// learningArea.checkCheckBox(3);

		learningArea.clickOnCheckAnswer();

		// learningArea.clickOnNextButton();
		learningArea.clickOnTask(1);

		learningArea.checkThatTaskIsVisitedAndCurrent(1);

		webDriver.refresh();
		sleep(2);
		learningArea.clickOnStep(2);
		sleep(2);
		testResultService.assertEquals(true, learningArea.checkThatTaskIsVisited(1), "Task state is not correct", true);
		//learningArea.clickToOpenNavigationBar();
		learningArea.clickOnHomeButton();
		sleep(1);
		report.startStep("Check the progress in the DB");
		String courseId = getCourseIdByCourseCode("B3");
		String itemId = getItemIdByCourseUnitLessonStepAndTask(courseId, 3, 2, 2, 2);

		studentService.checkStudentHasNoProgress(studentId, courseId, itemId);
	}

	@Test
	@TestCaseParams(testCaseID = { "22938" })
	public void testSetProgressPracticeListeningFillTheBlanksNegative() throws Exception {
		report.startStep("Navigate");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "A3", 6, 1);
		sleep(2);
		learningArea.clickOnStep(2);
		sleep(2);
		learningArea.clickOnTask(2);
		sleep(1);
		learningArea.clickOnTask(3);
		sleep(1);
		learningArea.clickOnTask(2);

		report.startStep("Select answer fro  drop down");
		learningArea.openDropDown(2);
		sleep(2);
		learningArea.clickOnSeeAnswer();
		sleep(2);
		
		report.startStep("Click on Next and check that task is marked as visited");
		learningArea.clickOnNextButton();
		sleep(1);
		testResultService.assertEquals(true, learningArea.checkThatTaskIsVisited(2), "Task state is not correct", true);
		
		webDriver.refresh();
		sleep(1);
		learningArea.clickOnStep(2);
		testResultService.assertEquals(true, learningArea.checkThatTaskIsVisited(2), "Task state is not correct", true);
		//learningArea.clickToOpenNavigationBar();
		learningArea.clickOnHomeButton();
		report.startStep("Check the progress in the DB");
		String courseId = getCourseIdByCourseCode("A3");
		String itemId = getItemIdByCourseUnitLessonStepAndTask(courseId, 6, 1, 2, 3);

		studentService.checkStudentHasNoProgress(studentId, courseId, itemId);
	}

	@Test
	@TestCaseParams(testCaseID = { "22960" })
	public void testSetProgressPracticeListeningMCQNegative() throws Exception {
		report.startStep("Navigate");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "FD", 5, 1);
		learningArea.clickOnStep(3);
		learningArea.clickOnTask(2);

		report.startStep("Select an answer");
		learningArea.clickOnPlaMediaByIndex("1_3");
		sleep(2);
		learningArea.clickOnCheckAnswer();
		learningArea.clickOnSeeAnswer();

		report.startStep("Check that task is marked as done");
		// learningArea.clickOnNextButton();
		learningArea.clickOnTask(1);

		// learningArea.clickOnTask(2);

		learningArea.checkThatTaskIsVisited(2);
		
		webDriver.refresh();
		learningArea.clickOnStep(3);
		testResultService.assertEquals(true, learningArea.checkThatTaskIsVisited(2), "Task state is not correct", true);
		//learningArea.clickToOpenNavigationBar();
		learningArea.clickOnHomeButton();
		report.startStep("Check the progress in the DB");
		String courseId = getCourseIdByCourseCode("FD");
		String itemId = getItemIdByCourseUnitLessonStepAndTask(courseId, 5, 1, 3, 3);

		studentService.checkStudentHasNoProgress(studentId, courseId, itemId);
	}

	@Test
	@TestCaseParams(testCaseID = { "22974" })
	public void testSetProgressTestGrammarNegative() throws Exception {
		homePage.navigateToCourseUnitAndLesson(courseCodes, "I2", 5, 4);

		sleep(2);
		learningArea.clickOnStep(3);
		sleep(2);
		learningArea.clickOnStartTest();
		sleep(2);
		learningArea.clickOnTask(3);
		sleep(5);
		learningArea.clickOnPlayVideoButton();

		learningArea.clickOnStep(2);

		learningArea.approveTest();
		
		//learningArea.clickToOpenNavigationBar();
		learningArea.clickOnHomeButton();

		report.startStep("Check the progress in the DB");
		String courseId = getCourseIdByCourseCode("I2");
		String itemId = getItemIdByCourseUnitLessonStepAndTask(courseId, 5, 4, 3, 4);

		studentService.checkStudentHasNoProgress(studentId, courseId, itemId);

	}

	

	@Test
	@TestCaseParams(testCaseID = { "22967" })
	public void testSetProgressPracticeListeningOpenEndedPositive() throws Exception {
		
		report.startStep("Navifate");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "A3", 6, 1);
		sleep(1);
		learningArea.clickOnStep(2);
		learningArea.clickOnTask(5);

		report.startStep("Click on check answer");
		learningArea.enterAnswerTextByIndex("0", "bla bla");
		learningArea.clickOnCheckAnswer();
		learningArea.clickOnTask(4);
		learningArea.clickOnTask(5);

		report.startStep("Check that task is marked as done");
		learningArea.checkThatTaskIsDone(5);
		webDriver.refresh();
		sleep(1);
		learningArea.clickOnStep(2);
		learningArea.checkThatTaskIsDone(5);
		//learningArea.clickToOpenNavigationBar();
		learningArea.clickOnHomeButton();
		
		report.startStep("Check the progress in the DB");
		String courseId = getCourseIdByCourseCode("A3");
		String itemId = getItemIdByCourseUnitLessonStepAndTask(courseId, 6, 1, 2, 6);
		studentService.checkStudentProgress(studentId, courseId, itemId);
	}

	@Test
	@TestCaseParams(testCaseID = { "33778" })
	public void testSetProgressPracticeInsertText() throws Exception {
		
		report.startStep("Init test data");
		int taskIndex = 5;
		int stepNum = 3; 
							
		
		report.startStep("Navigate B1-U9-L5-S3");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "B1", 9, 5);				
		sleep(1);
		learningArea.clickOnStep(stepNum);
		sleep(1);
		learningArea.clickOnTask(taskIndex);
		sleep(1);

		report.startStep("Check task status visited & current");
		learningArea.checkThatTaskIsVisitedAndCurrent(taskIndex);
		
		report.startStep("Make action go to prev task and back and check task status visited & current");
		learningArea.clickOnSeeAnswer();
		learningArea.clickOnBackButton();
		sleep(1);
		learningArea.clickOnTask(taskIndex);
		learningArea.checkThatTaskIsVisitedAndCurrent(taskIndex);

		report.startStep("Go to next step and back and check task status visited & current");
		learningArea.clickOnStep(stepNum+1);
		sleep(2);
		learningArea.clickOnStep(stepNum);
		sleep(2);
		learningArea.clickOnTask(taskIndex);
		sleep(1);
		learningArea.checkThatTaskIsVisitedAndCurrent(taskIndex);
		
		report.startStep("Refresh and back and check task status visited & current");
		webDriver.refresh();
		sleep(1);
		learningArea.clickOnStep(stepNum);
		sleep(2);
		learningArea.clickOnTask(taskIndex);
		sleep(2);
		learningArea.checkThatTaskIsVisitedAndCurrent(taskIndex);
		
		report.startStep("Check the progress not saved in the DB");
		String courseId = getCourseIdByCourseCode("B1");
		String itemId = getItemIdByCourseUnitLessonStepAndTask(courseId, 9, 5, stepNum, taskIndex+1);
		studentService.checkStudentHasNoProgress(studentId, courseId, itemId);
		
		report.startStep("Make progress trigger action, go to prev task and back and check task status done");
		learningArea.clickOnMarkerAndVerifySentenceInsert(1, learningArea.getInsertSentenceAnswerElement().getText());
		learningArea.clickOnBackButton();
		sleep(2);
		learningArea.clickOnTask(taskIndex);
		learningArea.checkThatTaskIsDone(taskIndex);
		
		report.startStep("Go to next step and back and check task status task status done");
		learningArea.clickOnStep(stepNum+1);
		sleep(2);
		learningArea.clickOnStep(stepNum);
		sleep(1);
		learningArea.clickOnTask(taskIndex);
		sleep(1);
		learningArea.checkThatTaskIsDone(taskIndex);
		
		report.startStep("Refresh and back and check task status status done");
		webDriver.refresh();
		sleep(1);
		learningArea.clickOnStep(stepNum);
		sleep(1);
		learningArea.clickOnTask(taskIndex);
		sleep(1);
		learningArea.checkThatTaskIsDone(taskIndex);
		//learningArea.clickToOpenNavigationBar();
		sleep(1);
		learningArea.clickOnHomeButton();
		report.startStep("Check the progress saved in the DB");
		studentService.checkStudentProgress(studentId, courseId, itemId);
		
		report.startStep("Verify comletion value updated in student statistics");
		String completionProgressFD = homePage.getCompletionWidgetValue();
		testResultService.assertTrue("No progress displayed in student statistics",!completionProgressFD.equalsIgnoreCase("0"));		

	}
	
	
	@Test
	@TestCaseParams(testCaseID = { "33628" })
	public void testSetProgressPracticeEditText() throws Exception {
		
		report.startStep("Navigate B3-U9-L5-S4");
		int taskIndex = 0;
		int stepNum = 4;
		homePage.navigateToCourseUnitAndLesson(courseCodes, "B3", 9, 5);
		sleep(1);
		learningArea.clickOnStep(stepNum);
		sleep(1);
		learningArea.clickOnTask(taskIndex);
		sleep(1);

		report.startStep("Check task status visited & current");
		learningArea.checkThatTaskIsVisitedAndCurrent(taskIndex);
		
		report.startStep("Make action go to next task and back and check task status visited & current");
		learningArea.clickOnSeeAnswer();
		learningArea.clickOnNextButton();
		sleep(1);
		learningArea.clickOnTask(taskIndex);
		learningArea.checkThatTaskIsVisitedAndCurrent(taskIndex);

		report.startStep("Go to next step and back and check task status visited & current");
		learningArea.clickOnStep(stepNum+1);
		learningArea.clickOnStep(stepNum);
		learningArea.clickOnTask(taskIndex);
		learningArea.checkThatTaskIsVisitedAndCurrent(taskIndex);
		
		report.startStep("Refresh and back and check task status visited & current");
		webDriver.refresh();
		sleep(1);
		learningArea.clickOnStep(stepNum);
		sleep(1);
		learningArea.clickOnTask(taskIndex);
		sleep(1);
		learningArea.checkThatTaskIsVisitedAndCurrent(taskIndex);
		
		report.startStep("Check the progress not saved in the DB");
		String courseId = getCourseIdByCourseCode("B3");
		String itemId = getItemIdByCourseUnitLessonStepAndTask(courseId, 9, 5, stepNum, taskIndex+1);
		studentService.checkStudentHasNoProgress(studentId, courseId, itemId);
		
		report.startStep("Make progress trigger action, go to next task and back and check task status done");
		learningArea.getEditTextInputElements().get(1).sendKeys("sample");
		learningArea.clickOnNextButton();
		sleep(1);
		learningArea.clickOnTask(taskIndex);
		learningArea.checkThatTaskIsDone(taskIndex);
		
		report.startStep("Go to next step and back and check task status task status done");
		learningArea.clickOnStep(stepNum+1);
		learningArea.clickOnStep(stepNum);
		learningArea.clickOnTask(taskIndex);
		learningArea.checkThatTaskIsDone(taskIndex);
		
		report.startStep("Refresh and back and check task status status done");
		webDriver.refresh();
		sleep(1);
		learningArea.clickOnStep(stepNum);
		sleep(1);
		learningArea.clickOnTask(taskIndex);
		sleep(1);
		learningArea.checkThatTaskIsDone(taskIndex);
		//learningArea.clickToOpenNavigationBar();
		learningArea.clickOnHomeButton();
		report.startStep("Check the progress saved in the DB");
		studentService.checkStudentProgress(studentId, courseId, itemId);
		
		report.startStep("Verify comletion value updated in student statistics");
		String completionProgressFD = homePage.getCompletionWidgetValue();
		testResultService.assertTrue("No progress displayed in student statistics",!completionProgressFD.equalsIgnoreCase("0"));		

	}
	
	@Test
	@TestCaseParams(testCaseID = { "33907" })
	public void testSetProgressPracticeHighlightText() throws Exception {
		
		int taskIndex = 1;
		int stepNum = 3;
		
				
		report.startStep("Navigate I1-U10-L2-S3");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "I1", 10, 2);
		learningArea.clickOnStep(stepNum);
		sleep(1);
		learningArea.clickOnTask(taskIndex);
		sleep(1);

		report.startStep("Check task status visited & current");
		learningArea.checkThatTaskIsVisitedAndCurrent(taskIndex);
		
		report.startStep("Make action go to next task and back and check task status visited & current");
		learningArea.clickOnSeeAnswer();
		learningArea.clickOnNextButton();
		learningArea.clickOnTask(taskIndex);
		learningArea.checkThatTaskIsVisitedAndCurrent(taskIndex);

		report.startStep("Go to next step and back and check task status visited & current");
		learningArea.clickOnStep(stepNum+1);
		learningArea.clickOnStep(stepNum);
		learningArea.clickOnTask(taskIndex);
		learningArea.checkThatTaskIsVisitedAndCurrent(taskIndex);
		
		report.startStep("Refresh and back and check task status visited & current");
		webDriver.refresh();
		sleep(1);
		learningArea.clickOnStep(stepNum);
		sleep(1);
		learningArea.clickOnTask(taskIndex);
		sleep(1);
		learningArea.checkThatTaskIsVisitedAndCurrent(taskIndex);
		
		report.startStep("Check the progress not saved in the DB");
		String courseId = getCourseIdByCourseCode("I1");
		String itemId = getItemIdByCourseUnitLessonStepAndTask(courseId, 10, 2, stepNum, taskIndex+1);
		studentService.checkStudentHasNoProgress(studentId, courseId, itemId);
		
		report.startStep("Make progress trigger action, go to next task and back and check task status done");
		learningArea.selectAnswerRadioByTextNum(1, 2);
		learningArea.clickOnNextButton();
		learningArea.clickOnTask(taskIndex);
		learningArea.checkThatTaskIsDone(taskIndex);
		
		report.startStep("Go to next step and back and check task status task status done");
		learningArea.clickOnStep(stepNum+1);
		learningArea.clickOnStep(stepNum);
		learningArea.clickOnTask(taskIndex);
		learningArea.checkThatTaskIsDone(taskIndex);
		
		report.startStep("Refresh and back and check task status status done");
		webDriver.refresh();
		sleep(1);
		learningArea.clickOnStep(stepNum);
		sleep(1);
		learningArea.clickOnTask(taskIndex);
		sleep(1);
		learningArea.checkThatTaskIsDone(taskIndex);
		//learningArea.clickToOpenNavigationBar();
		learningArea.clickOnHomeButton();
		report.startStep("Check the progress saved in the DB");
		studentService.checkStudentProgress(studentId, courseId, itemId);
		
		report.startStep("Verify comletion value updated in student statistics");
		String completionProgressFD = homePage.getCompletionWidgetValue();
		testResultService.assertTrue("No progress displayed in student statistics",!completionProgressFD.equalsIgnoreCase("0"));		

	}
			
	
	@Test
	@TestCaseParams(testCaseID = { "34168" })
	public void testSetProgressPracticeOpenWriting() throws Exception {
		
				
		report.startStep("Navigate to B1-U9-L3-S4");
		int unitNum = 9;
		int lessonNum = 3;
		int taskIndex = 2;
		int stepNum = 4;
		homePage.navigateToCourseUnitAndLesson(courseCodes, "B1", unitNum, lessonNum);
		learningArea.clickOnStep(stepNum);
		sleep(2);
		learningArea.clickOnTask(taskIndex);
		sleep(2);

		report.startStep("Check task status visited & current");
		learningArea.checkThatTaskIsVisitedAndCurrent(taskIndex);
		
		report.startStep("Make action go to previous task and back and check task status visited & current");
		webDriver.swithcToFrameAndSendKeys(htmlEditorXpath,freeText, htmlEditorFrame);
		webDriver.switchToTopMostFrame();
		learningArea.clickOnBackButton();
		webDriver.closeAlertByAccept(2);
		learningArea.clickOnTask(taskIndex);
		learningArea.checkThatTaskIsVisitedAndCurrent(taskIndex);

		report.startStep("Go to previous step and back and check task status visited & current");
		learningArea.clickOnStep(stepNum-1);
		learningArea.clickOnStep(stepNum);
		learningArea.clickOnTask(taskIndex);
		learningArea.checkThatTaskIsVisitedAndCurrent(taskIndex);
		
		report.startStep("Refresh and back and check task status visited & current");
		webDriver.refresh();
		sleep(1);
		learningArea.clickOnStep(stepNum);
		sleep(1);
		learningArea.clickOnTask(taskIndex);
		sleep(1);
		learningArea.checkThatTaskIsVisitedAndCurrent(taskIndex);
		
		report.startStep("Check the progress not saved in the DB");
		String courseId = getCourseIdByCourseCode("B1");
		String itemId = getItemIdByCourseUnitLessonStepAndTask(courseId, unitNum, lessonNum, stepNum, taskIndex+1);
		studentService.checkStudentHasNoProgress(studentId, courseId, itemId);
		
		report.startStep("Make progress trigger action (press Send To Teacher) , go to previous task and back and check task status done");
		webDriver.swithcToFrameAndSendKeys(htmlEditorXpath,freeText, htmlEditorFrame);
		webDriver.switchToTopMostFrame();
		learningArea.clickOnSubmitToTeacherOW();
		learningArea.closeAlertModalByAccept();
		learningArea.clickOnBackButton();
		learningArea.closeAlertModalByAccept();
		learningArea.clickOnTask(taskIndex);
		learningArea.checkThatTaskIsDone(taskIndex);
		
		report.startStep("Go to next step and back and check task status task status done");
		learningArea.clickOnStep(stepNum-1);
		learningArea.clickOnStep(stepNum);
		learningArea.clickOnTask(taskIndex);
		learningArea.checkThatTaskIsDone(taskIndex);
		
		report.startStep("Refresh and back and check task status status done");
		webDriver.refresh();
		sleep(1);
		learningArea.clickOnStep(stepNum);
		sleep(1);
		learningArea.clickOnTask(taskIndex);
		sleep(1);
		learningArea.checkThatTaskIsDone(taskIndex);
		//learningArea.clickToOpenNavigationBar();
		learningArea.clickOnHomeButton();
		report.startStep("Check the progress saved in the DB");
		studentService.checkStudentProgress(studentId, courseId, itemId);
		
		report.startStep("Verify comletion value updated in student statistics");
		String completionProgressFD = homePage.getCompletionWidgetValue();
		testResultService.assertTrue("No progress displayed in student statistics",!completionProgressFD.equalsIgnoreCase("0"));		

	}
	
	@Test
	@TestCaseParams(testCaseID = { "40717" })
	public void testSetProgressPracticeOpenWritingNoTeacher() throws Exception {
		
		report.startStep("Create and login to ED as student and navigate to Free Writing task");
		homePage.clickOnLogOut();
		webDriver.switchToTopMostFrame();
		String className = configuration.getProperty("classname.locked");
		String institutionId = configuration.getInstitutionId();
		studentId = pageHelper.createUSerUsingSP(institutionId, className);
		String studentUserName = dbService.getUserNameById(studentId, institutionId);
		loginPage = new NewUXLoginPage(webDriver,testResultService);
		homePage = loginPage.loginAsStudent(studentUserName, "12345");			
		
		report.startStep("Navigate to B1-U9-L3-S4");
		int unitNum = 9;
		int lessonNum = 3;
		int taskIndex = 2;
		int stepNum = 4;
		homePage.navigateToCourseUnitAndLesson(courseCodes, "B1", unitNum, lessonNum);
		sleep(1);
		learningArea.clickOnStep(stepNum);
		sleep(1);
		learningArea.clickOnTask(taskIndex);
		sleep(1);
		
		report.startStep("Check task status visited & current");
		learningArea.checkThatTaskIsVisitedAndCurrent(taskIndex);
		
		report.startStep("Make action go to previous task and back and check task status visited & current");
		learningArea.clickOnClearAnswer();
		learningArea.clickOnBackButton();
		webDriver.closeAlertByAccept(2);
		learningArea.clickOnTask(taskIndex);
		learningArea.checkThatTaskIsVisitedAndCurrent(taskIndex);

		report.startStep("Go to next step and back and check task status visited & current");
		learningArea.clickOnStep(3);
		sleep(1);
		learningArea.clickOnStep(stepNum);
		learningArea.clickOnTask(taskIndex);
		learningArea.checkThatTaskIsVisitedAndCurrent(taskIndex);
		
		report.startStep("Refresh and back and check task status visited & current");
		webDriver.refresh();
		sleep(1);
		learningArea.clickOnStep(stepNum);
		sleep(1);
		learningArea.clickOnTask(taskIndex);
		sleep(1);
		learningArea.checkThatTaskIsVisitedAndCurrent(taskIndex);
		
		report.startStep("Check the progress not saved in the DB");
		String courseId = getCourseIdByCourseCode("B1");
		String itemId = getItemIdByCourseUnitLessonStepAndTask(courseId, unitNum, lessonNum, stepNum, taskIndex+1);
		studentService.checkStudentHasNoProgress(studentId, courseId, itemId);
		
		report.startStep("Make progress trigger action (just enter text), go to previous task and back and check task status done");
		webDriver.swithcToFrameAndSendKeys(htmlEditorXpath, freeText, htmlEditorFrame);
		webDriver.switchToTopMostFrame();
		learningArea.clickOnBackButton();
		webDriver.closeAlertByAccept(2);
		learningArea.clickOnTask(taskIndex);
		learningArea.checkThatTaskIsDone(taskIndex);
		
		report.startStep("Go to next step and back and check task status task status done");
		learningArea.clickOnStep(3);
		sleep(1);
		learningArea.clickOnStep(stepNum);
		learningArea.clickOnTask(taskIndex);
		learningArea.checkThatTaskIsDone(taskIndex);
		
		report.startStep("Refresh and back and check task status status done");
		webDriver.refresh();
		sleep(1);
		learningArea.clickOnStep(stepNum);
		sleep(1);
		learningArea.clickOnTask(taskIndex);
		sleep(1);
		learningArea.checkThatTaskIsDone(taskIndex);
		//learningArea.clickToOpenNavigationBar();
		learningArea.clickOnHomeButton();
		report.startStep("Check the progress saved in the DB");
		studentService.checkStudentProgress(studentId, courseId, itemId);
		
		report.startStep("Verify comletion value updated in student statistics");
		String completionProgressFD = homePage.getCompletionWidgetValue();
		testResultService.assertTrue("No progress displayed in student statistics",!completionProgressFD.equalsIgnoreCase("0"));		

	}
	
	@Test
	@TestCaseParams(testCaseID = { "22966" })
	public void testSetProgressPracticeFreeWriteNonErater() throws Exception {
		
		report.startStep("Navigate to FD-U2-L4-S3-T3");
		int unitNum = 2;
		int lessonNum = 4;
		int taskIndex = 2;
		int stepNum = 3;
		String texInput = "Hello";
		String sectionNum = "1";
		homePage.navigateToCourseUnitAndLesson(courseCodes, "FD", unitNum, lessonNum);
		learningArea.clickOnStep(stepNum);
		learningArea.clickOnTask(taskIndex);

		report.startStep("Check task status visited & current");
		learningArea.checkThatTaskIsVisitedAndCurrent(taskIndex);
		
		report.startStep("Make action go to previous task and back and check task status visited & current");
		learningArea.enterFreeWritingTextBySection(sectionNum, texInput);
		learningArea.clickOnBackButton();
		learningArea.clickOnTask(taskIndex);
		learningArea.checkThatTaskIsVisitedAndCurrent(taskIndex);

		report.startStep("Go to previous step and back and check task status visited & current");
		learningArea.clickOnStep(stepNum-1);
		learningArea.clickOnStep(stepNum);
		learningArea.clickOnTask(taskIndex);
		learningArea.checkThatTaskIsVisitedAndCurrent(taskIndex);
		
		report.startStep("Refresh and back and check task status visited & current");
		webDriver.refresh();
		sleep(1);
		learningArea.clickOnStep(stepNum);
		sleep(1);
		learningArea.clickOnTask(taskIndex);
		sleep(1);
		learningArea.checkThatTaskIsVisitedAndCurrent(taskIndex);
		
		report.startStep("Check the progress not saved in the DB");
		String courseId = getCourseIdByCourseCode("FD");
		String itemId = getItemIdByCourseUnitLessonStepAndTask(courseId, unitNum, lessonNum, stepNum, taskIndex+1);
		studentService.checkStudentHasNoProgress(studentId, courseId, itemId);
		
		report.startStep("Make progress trigger action (enter text and press Send To Teacher), go to previous task and back and check task status done");
		learningArea.enterFreeWritingTextBySection(sectionNum, texInput);
		learningArea.clickOnSendToTeacher();
		learningArea.closeAlertModalByAccept();
		sleep(1);
		learningArea.clickOnBackButton();
		learningArea.clickOnTask(taskIndex);
		learningArea.checkThatTaskIsDone(taskIndex);
		
		report.startStep("Go to previous step and back and check task status task status done");
		learningArea.clickOnStep(stepNum-1);
		learningArea.clickOnStep(stepNum);
		learningArea.clickOnTask(taskIndex);
		learningArea.checkThatTaskIsDone(taskIndex);
		
		report.startStep("Refresh and back and check task status status done");
		webDriver.refresh();
		sleep(1);
		learningArea.clickOnStep(stepNum);
		sleep(1);
		learningArea.clickOnTask(taskIndex);
		sleep(1);
		learningArea.checkThatTaskIsDone(taskIndex);
		//learningArea.clickToOpenNavigationBar();
		learningArea.clickOnHomeButton();
		report.startStep("Check the progress saved in the DB");
		studentService.checkStudentProgress(studentId, courseId, itemId);
		
		report.startStep("Verify comletion value updated in student statistics");
		String completionProgressFD = homePage.getCompletionWidgetValue();
		testResultService.assertTrue("No progress displayed in student statistics",!completionProgressFD.equalsIgnoreCase("0"));		

	}
	
	@Test
	@TestCaseParams(testCaseID = { "40716" })
	public void testSetProgressPracticeFreeWriteNoEraterNoTeacher() throws Exception {
				
		report.startStep("Create and login to ED as student and navigate to Free Writing task");
		homePage.clickOnLogOut();
		webDriver.switchToTopMostFrame();
		String className = configuration.getProperty("classname.locked");
		String institutionId = configuration.getInstitutionId();
		studentId = pageHelper.createUSerUsingSP(institutionId, className);
		String studentUserName = dbService.getUserNameById(studentId, institutionId);
		loginPage = new NewUXLoginPage(webDriver,testResultService);
		homePage = loginPage.loginAsStudent(studentUserName, "12345");				
				
		report.startStep("Navigate to FD-U2-L4-S3-T3");
		int unitNum = 2;
		int lessonNum = 4;
		int taskIndex = 2;
		int stepNum = 3;
		String texInput = "Hello";
		String sectionNum = "1";
		homePage.navigateToCourseUnitAndLesson(courseCodes, "FD", unitNum, lessonNum);
		learningArea.clickOnStep(stepNum);
		learningArea.clickOnTask(taskIndex);

		report.startStep("Check task status visited & current");
		learningArea.checkThatTaskIsVisitedAndCurrent(taskIndex);
		
		report.startStep("Make action go to previous task and back and check task status visited & current");
		learningArea.clickOnClearAnswer();
		learningArea.clickOnBackButton();
		learningArea.clickOnTask(taskIndex);
		learningArea.checkThatTaskIsVisitedAndCurrent(taskIndex);

		report.startStep("Go to previous step and back and check task status visited & current");
		learningArea.clickOnStep(stepNum-1);
		learningArea.clickOnStep(stepNum);
		learningArea.clickOnTask(taskIndex);
		learningArea.checkThatTaskIsVisitedAndCurrent(taskIndex);
		
		report.startStep("Refresh and back and check task status visited & current");
		webDriver.refresh();
		sleep(1);
		learningArea.clickOnStep(stepNum);
		sleep(1);
		learningArea.clickOnTask(taskIndex);
		sleep(1);
		learningArea.checkThatTaskIsVisitedAndCurrent(taskIndex);
		
		report.startStep("Check the progress not saved in the DB");
		String courseId = getCourseIdByCourseCode("FD");
		String itemId = getItemIdByCourseUnitLessonStepAndTask(courseId, unitNum, lessonNum, stepNum, taskIndex+1);
		studentService.checkStudentHasNoProgress(studentId, courseId, itemId);
		
		report.startStep("Make progress trigger action (just enter text), go to previous task and back and check task status done");
		learningArea.enterFreeWritingTextBySection(sectionNum, texInput);
		sleep(1);
		learningArea.clickOnBackButton();
		learningArea.clickOnTask(taskIndex);
		learningArea.checkThatTaskIsDone(taskIndex);
		
		report.startStep("Go to previous step and back and check task status task status done");
		learningArea.clickOnStep(stepNum-1);
		learningArea.clickOnStep(stepNum);
		learningArea.clickOnTask(taskIndex);
		learningArea.checkThatTaskIsDone(taskIndex);
		
		report.startStep("Refresh and back and check task status status done");
		webDriver.refresh();
		sleep(1);
		learningArea.clickOnStep(stepNum);
		sleep(1);
		learningArea.clickOnTask(taskIndex);
		sleep(1);
		learningArea.checkThatTaskIsDone(taskIndex);
		//learningArea.clickToOpenNavigationBar();
		learningArea.clickOnHomeButton();
		report.startStep("Check the progress saved in the DB");
		studentService.checkStudentProgress(studentId, courseId, itemId);
		
		report.startStep("Verify comletion value updated in student statistics");
		String completionProgressFD = homePage.getCompletionWidgetValue();
		testResultService.assertTrue("No progress displayed in student statistics",!completionProgressFD.equalsIgnoreCase("0"));		

	}
	
	
	
	@Test
	@TestCaseParams(testCaseID = { "33851" })
	public void testSetProgressPracticeSelectText() throws Exception {
		
		report.startStep("Init test data");
		int taskIndex = 1;
		int stepNum = 5;
						
		
		report.startStep("Navigate B1-U9-L1-S5");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "B1", 9, 1);
		sleep(1);
					
		learningArea.clickOnStep(stepNum);
		sleep(1);
						
		learningArea.clickOnTask(taskIndex);
		sleep(1);

		report.startStep("Check task status visited & current");
		learningArea.checkThatTaskIsVisitedAndCurrent(taskIndex);
		
		report.startStep("Make action go to next task and back and check task status visited & current");
		learningArea.clickOnSeeAnswer();
		learningArea.clickOnNextButton();
		learningArea.clickOnTask(taskIndex);
		learningArea.checkThatTaskIsVisitedAndCurrent(taskIndex);

		report.startStep("Go to next step and back and check task status visited & current");
		learningArea.clickOnStep(stepNum+1);
		learningArea.clickOnStep(stepNum);
		learningArea.clickOnTask(taskIndex);
		learningArea.checkThatTaskIsVisitedAndCurrent(taskIndex);
		
		report.startStep("Refresh and back and check task status visited & current");
		webDriver.refresh();
		sleep(1);
		learningArea.clickOnStep(stepNum);
		sleep(1);
		learningArea.clickOnTask(taskIndex);
		sleep(1);
		learningArea.checkThatTaskIsVisitedAndCurrent(taskIndex);
		
		report.startStep("Check the progress not saved in the DB");
		String courseId = getCourseIdByCourseCode("B1");
		String itemId = getItemIdByCourseUnitLessonStepAndTask(courseId, 9, 1, stepNum, taskIndex+1);
		studentService.checkStudentHasNoProgress(studentId, courseId, itemId);
		
		report.startStep("Make progress trigger action, go to next task and back and check task status done");
		learningArea.clickOnMarkerAndVerifySentenceSelect(1);
		learningArea.clickOnNextButton();
		learningArea.clickOnTask(taskIndex);
		learningArea.checkThatTaskIsDone(taskIndex);
		
		report.startStep("Go to next step and back and check task status task status done");
		learningArea.clickOnStep(stepNum+1);
		learningArea.clickOnStep(stepNum);
		learningArea.clickOnTask(taskIndex);
		learningArea.checkThatTaskIsDone(taskIndex);
		
		report.startStep("Refresh and back and check task status status done");
		webDriver.refresh();
		sleep(1);
		learningArea.clickOnStep(stepNum);
		sleep(1);
		learningArea.clickOnTask(taskIndex);
		sleep(1);
		learningArea.checkThatTaskIsDone(taskIndex);
		//learningArea.clickToOpenNavigationBar();
		learningArea.clickOnHomeButton();
		report.startStep("Check the progress saved in the DB");
		studentService.checkStudentProgress(studentId, courseId, itemId);
		
		report.startStep("Verify comletion value updated in student statistics");
		String completionProgressFD = homePage.getCompletionWidgetValue();
		testResultService.assertTrue("No progress displayed in student statistics",!completionProgressFD.equalsIgnoreCase("0"));
		
	}
	
	
	@Ignore // cancel when actual content with such task appears available 
	@Test
	@TestCaseParams(testCaseID = { "35146" })
	public void testSetProgressListeningEmptyTask() throws Exception {
		
		int taskIndex = 5;
		
		report.startStep("Navigate A2-U2-L2-P1");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "I3", 7, 1);
		sleep(1);
		learningArea.clickOnStep(2);
		sleep(1);
		learningArea.clickOnTask(taskIndex);
		sleep(1);

		report.startStep("Check task status visited & current");
		learningArea.checkThatTaskIsVisitedAndCurrent(taskIndex);
		
		report.startStep("Go to next task and back and check task status visited & current");
		learningArea.clickOnNextButton();
		learningArea.clickOnTask(taskIndex);
		learningArea.checkThatTaskIsVisitedAndCurrent(taskIndex);

		report.startStep("Go to next step and back and check task status visited & current");
		learningArea.clickOnStep(3);
		learningArea.clickOnStep(2);
		learningArea.clickOnTask(taskIndex);
		learningArea.checkThatTaskIsVisitedAndCurrent(taskIndex);
		
		report.startStep("Refresh and back and check task status visited & current");
		webDriver.refresh();
		sleep(1);
		learningArea.clickOnStep(2);
		sleep(1);
		learningArea.clickOnTask(taskIndex);
		sleep(1);
		learningArea.checkThatTaskIsVisitedAndCurrent(taskIndex);
		
		report.startStep("Check the progress not saved in the DB");
		String courseId = getCourseIdByCourseCode("I3");
		String itemId = getItemIdByCourseUnitLessonStepAndTask(courseId, 7, 1, 2, (taskIndex+1));
		studentService.checkStudentHasNoProgress(studentId, courseId, itemId);
		
		report.startStep("Make progress trigger action, go to next task and back and check task status done");
		learningArea.clickOnPlayNewVideoButton();
		learningArea.clickOnNextButton();
		learningArea.clickOnTask(taskIndex);
		learningArea.checkThatTaskIsDone(taskIndex);
		
		report.startStep("Go to next step and back and check task status task status done");
		learningArea.clickOnStep(3);
		learningArea.clickOnStep(2);
		learningArea.clickOnTask(taskIndex);
		learningArea.checkThatTaskIsDone(taskIndex);
		
		report.startStep("Refresh and back and check task status status done");
		webDriver.refresh();
		sleep(1);
		learningArea.clickOnStep(2);
		sleep(1);
		learningArea.clickOnTask(taskIndex);
		sleep(1);
		learningArea.checkThatTaskIsDone(taskIndex);
		//learningArea.clickToOpenNavigationBar();
		learningArea.clickOnHomeButton();
		report.startStep("Check the progress saved in the DB");
		studentService.checkStudentProgress(studentId, courseId, itemId);
		
		report.startStep("Verify comletion value updated in student statistics");
		String completionProgressFD = homePage.getCompletionWidgetValue();
		testResultService.assertTrue("No progress displayed in student statistics",!completionProgressFD.equalsIgnoreCase("0"));
		
	}
	
	
	@Test
	@TestCaseParams(testCaseID = { "35146" })
	public void testSetProgressReadingEmptyTask() throws Exception {
		
		int taskIndex = 0;
		int stepNum = 1;
		
		report.startStep("Navigate B1-U9-L2-S1");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "B1", 9, 2);
		sleep(1);
		learningArea.clickOnTask(taskIndex);
		sleep(1);

		report.startStep("Check task status visited & current");
		learningArea.checkThatTaskIsVisitedAndCurrent(taskIndex);
				
		
		report.startStep("Check the progress not saved in the DB");
		String courseId = getCourseIdByCourseCode("B1");
		String itemId = getItemIdByCourseUnitLessonStepAndTask(courseId, 9, 2, stepNum, (taskIndex+1));
		studentService.checkStudentHasNoProgress(studentId, courseId, itemId);
				
		report.startStep("Go to next step and back and check task status task status done");
		learningArea.clickOnStep(stepNum+1);
		learningArea.clickOnStep(stepNum);
		learningArea.clickOnTask(taskIndex);
		learningArea.checkThatTaskIsDone(taskIndex);
		
		report.startStep("Refresh and back and check task status status done");
		webDriver.refresh();
		sleep(1);
		learningArea.clickOnStep(stepNum);
		sleep(1);
		learningArea.clickOnTask(taskIndex);
		sleep(1);
		learningArea.checkThatTaskIsDone(taskIndex);
		
		//learningArea.clickToOpenNavigationBar();
		learningArea.clickOnHomeButton();
		
		report.startStep("Check the progress saved in the DB");
		studentService.checkStudentProgress(studentId, courseId, itemId);
		
		report.startStep("Verify comletion value updated in student statistics");
		String completionProgressFD = homePage.getCompletionWidgetValue();
		testResultService.assertTrue("No progress displayed in student statistics",!completionProgressFD.equalsIgnoreCase("0"));
		
	}
	
	private String getItemIdByCourseUnitLessonStepAndTask(String courseId, int unitIndex, int lessonIndex,
			int StepIndex, int taskIndex) throws Exception {
		
		String itemId = pageHelper.getItemIdByCourseUnitLessonStepAndTask(courseId, unitIndex, lessonIndex, StepIndex, taskIndex);
				
		return itemId;
	}
	
	@After
	public void tearDown() throws Exception {
		
		report.startStep("Set No Support Language");
		pageHelper.setUserLangSupportLevel(studentId,0);
		
		super.tearDown();
	}

}
