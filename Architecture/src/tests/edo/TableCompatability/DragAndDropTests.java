package tests.edo.TableCompatability;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import pageObjects.DragAndDropSection;
import pageObjects.EdoHomePage;
import Enums.SubComponentName;
import Interfaces.TestCaseParams;
import Objects.Course;
import tests.misc.EdusoftWebTest;

public class DragAndDropTests extends EdusoftWebTest {

	@Before
	public void setup() throws Exception {
		super.setup();
	}

	@Test
	@TestCaseParams(testCaseID = { "16581" })
	public void testDragAndDropClassification() throws Exception {
		// 6 Intermediate 2 Instructions 66 Listening 234 Forecast Practice
		// Classification 3 i2lrwep03 57117
		String[] words = new String[] { "warm", "clean", "heat", "cozy",
				"broken parts", "inspect", "water marks", "ceiling", "check",
				"leaks", "snow", "rain" };

		Course course = pageHelper.getCourses().get(18);

		report.report("Login and navigate to unit component");
		EdoHomePage edoHomePage = pageHelper.loginAsStudent();
	
		edoHomePage.navigateToCourseUnitComponent(course,
				SubComponentName.Practice);
		// Wait until everything is loaded
		edoHomePage.ClickComponentStage("2");
		sleep(3);
		DragAndDropSection dragAndDropSection = new DragAndDropSection(
				webDriver, testResultService);
		report.report("Start to drag and drop - fill all correct answers");
		dragAndDropSection.dragClassificationAnswerByTextFromBank("warm", 1, 1);
		dragAndDropSection
				.dragClassificationAnswerByTextFromBank("clean", 2, 1);
		dragAndDropSection.dragClassificationAnswerByTextFromBank("heat", 3, 1);
		dragAndDropSection.dragClassificationAnswerByTextFromBank("cozy", 4, 1);
		dragAndDropSection.dragClassificationAnswerByTextFromBank(
				"broken parts", 5, 1);
		dragAndDropSection.dragClassificationAnswerByTextFromBank("inspect", 6,
				1);

		dragAndDropSection.dragClassificationAnswerByTextFromBank(
				"water marks", 1, 2);
		dragAndDropSection.dragClassificationAnswerByTextFromBank("ceiling", 2,
				2);
		dragAndDropSection
				.dragClassificationAnswerByTextFromBank("check", 3, 2);
		dragAndDropSection
				.dragClassificationAnswerByTextFromBank("leaks", 4, 2);
		dragAndDropSection.dragClassificationAnswerByTextFromBank("snow", 5, 2);
		dragAndDropSection.dragClassificationAnswerByTextFromBank("rain", 6, 2);

		edoHomePage.clickOnCheckAnswers();

		for (int i = 0; i < words.length; i++) {
			dragAndDropSection.checkDragAndDropCorrectAnswer(words[i]);
		}

		edoHomePage.clickOnClearAnswer();

		report.report("Drop and replace from bank");
		dragAndDropSection.dragClassificationAnswerByTextFromBank("rain", 6, 2);
		dragAndDropSection
				.dragClassificationAnswerByTextFromBank("leaks", 6, 2);
		dragAndDropSection.checkDragElementLocation("leaks", "34", 6, 2);
		dragAndDropSection.checkDragElementIsBackToBank("rain");

		report.report("Drop and replace between used elements");
		dragAndDropSection.dragClassificationAnswerByTextFromBank("rain", 5, 2);

		dragAndDropSection.dragClassificationAnswerByTextFromExistingAnswer(
				"rain", 6, 2, 6, 2);
		dragAndDropSection.checkDragElementLocation("rain", "41", 6, 2);
		dragAndDropSection.checkDragElementIsBackToBank("leaks");
		report.report("click on see answers");
		edoHomePage.clickOnSeeAnswer();
		for (int i = 0; i < words.length; i++) {
			dragAndDropSection.checkDragElementIsPlaced(words[i]);
		}

		// TODO drag element to bank
		edoHomePage.clickOnClearAnswer();
		dragAndDropSection
				.dragClassificationAnswerByTextFromBank("check", 3, 2);
		dragAndDropSection.dragClassificationAnswerToBank("check");
		dragAndDropSection.checkDragElementIsBackToBank("check");

		edoHomePage.clickOnCourseByName(course.getName());
		sleep(5);

		pageHelper.logOut();
		checkProgressInDb("57117");
		report.report("End of test");
	}

	@Test
	@TestCaseParams(testCaseID = { "16585" })
	public void testDragAndDropMatching() throws Exception {
		// Intermediate 2 Instructions 66 Reading 235 Flight Information
		// Practice Matching 2 i2rnfip002 24387
		Course course = pageHelper.getCourses().get(19);
		String[] words = new String[] { "claim", "inspection", "items",
				"complications", "card", };

		report.report("Login and navigate to unit component");
		EdoHomePage edoHomePage = pageHelper.loginAsStudent();
		
		edoHomePage.navigateToCourseUnitComponent(course,
				SubComponentName.Practice);

		edoHomePage.ClickComponentStage("1");

		DragAndDropSection dragAndDropSection = new DragAndDropSection(
				webDriver, testResultService);

		report.report("Fill all answers");
		dragAndDropSection.dragClassificationAnswerByTextFromBank(words[0], 1,
				2);
		dragAndDropSection.dragClassificationAnswerByTextFromBank(words[1], 2,
				2);
		dragAndDropSection.dragClassificationAnswerByTextFromBank(words[2], 3,
				2);
		dragAndDropSection.dragClassificationAnswerByTextFromBank(words[3], 4,
				2);
		dragAndDropSection.dragClassificationAnswerByTextFromBank(words[4], 5,
				2);

		report.report("check answers");
		edoHomePage.clickOnCheckAnswers();
		for (int i = 0; i < words.length; i++) {
			dragAndDropSection.checkDragAndDropCorrectAnswer(words[i]);
		}

		report.report("clear answers");
		edoHomePage.clickOnClearAnswer();

		for (int i = 0; i < words.length; i++) {
			dragAndDropSection.checkDragElementIsBackToBank(words[i]);
		}

		report.report("Drop and replace from bank");
		dragAndDropSection.dragClassificationAnswerByTextFromBank(words[0], 1,
				2);
		dragAndDropSection.dragClassificationAnswerByTextFromBank(words[1], 1,
				2);
		dragAndDropSection.checkDragElementIsBackToBank(words[0]);
		report.report("Drop and replace between used elements");
		edoHomePage.clickOnClearAnswer();
		dragAndDropSection.dragClassificationAnswerByTextFromBank(words[0], 1,
				2);
		dragAndDropSection.dragClassificationAnswerByTextFromBank(words[1], 2,
				2);

		dragAndDropSection.dragClassificationAnswerByTextFromExistingAnswer(
				words[0], 6, 2, 2, 2);
		dragAndDropSection.checkDragElementIsBackToBank(words[1]);

		report.report("click on see answers");
		edoHomePage.clickOnSeeAnswer();
		for (int i = 0; i < words.length; i++) {
			dragAndDropSection.checkDragElementIsPlaced(words[i]);
		}

		edoHomePage.clickOnCourseByName(course.getName());
		sleep(5);

		pageHelper.logOut();
		checkProgressInDb("24387");
	}

	@Test
	@TestCaseParams(testCaseID = { "16586" })
	public void testDragAndDropSequenceSentence() throws Exception {
		// Basic 2 Interesting People 46 Reading 132 Do It! Practice Sequence
		// Sentence 2 b2radip002 23043
		Course course = pageHelper.getCourses().get(20);
		String[] words = new String[] {
				"She hired some workers.",// 4
				"She decided that she wanted to tell others what to do.",// 2
				"She started the Clean-House Agency by putting an ad in a newspaper.",// 3
				"She taught the workers how to clean.",// 5
				"Her mother was always telling her what to do.",// 1
				"She decided to write cookbooks as a new business.",// 7
				"She started sending the workers out to homes and offices." };// 6
		report.report("Login and navigate to unit component");
		EdoHomePage edoHomePage = pageHelper.loginAsStudent();

		DragAndDropSection dragAndDropSection = new DragAndDropSection(
				webDriver, testResultService);
		
		edoHomePage.navigateToCourseUnitComponent(course,
				SubComponentName.Practice);

		edoHomePage.ClickComponentStage("1");
		sleep(5);
		webDriver.printScreen("Before dragging");
		report.report("Arrange all answers");
		dragAndDropSection.dragSeqSentence(words[1], 2);
		dragAndDropSection.dragSeqSentence(words[0], 4);
		dragAndDropSection.dragSeqSentence(words[2], 3);
		dragAndDropSection.dragSeqSentence(words[3], 5);
		dragAndDropSection.dragSeqSentence(words[4], 1);
		dragAndDropSection.dragSeqSentence(words[5], 7);
		dragAndDropSection.dragSeqSentence(words[6], 6);

		report.report("Check answer");
		edoHomePage.clickOnCheckAnswers();
		int index = 0;
		sleep(5);
		dragAndDropSection.checkSeqSentenceCorrectAnswer(words[0], 4);
		dragAndDropSection.checkSeqSentenceCorrectAnswer(words[1], 2);
		dragAndDropSection.checkSeqSentenceCorrectAnswer(words[2], 3);
		dragAndDropSection.checkSeqSentenceCorrectAnswer(words[3], 5);
		dragAndDropSection.checkSeqSentenceCorrectAnswer(words[4], 1);
		dragAndDropSection.checkSeqSentenceCorrectAnswer(words[5], 7);
		dragAndDropSection.checkSeqSentenceCorrectAnswer(words[6], 6);

		report.report("Check wrong answer");
		dragAndDropSection.clickOnClearAnswer();
		dragAndDropSection.dragSeqSentence(words[1], 4);
		dragAndDropSection.clickOnCheckAnswers();
		dragAndDropSection.checkSeqSentenceInCorrectAnswer(words[1], 4);

		edoHomePage.clickOnCourseByName(course.getName());
		sleep(5);

		pageHelper.logOut();
		checkProgressInDb("23043");

	}

	@Test
	@TestCaseParams(testCaseID = { "16587" })
	public void testDragAndDropSequenceimagetemplates() throws Exception {
		// 4 Basic 2 Buying And Selling 41 Reading 106 Wrong Color Practice
		// Sequence Image 4 b2rswcp004 22777
		String[] ids = new String[] { "44", "75", "49", "65", "67", "98" };
		Course course = pageHelper.getCourses().get(24);

		report.report("Login and navigate to unit component");
		EdoHomePage edoHomePage = pageHelper.loginAsStudent();
	
		edoHomePage.navigateToCourseUnitComponent(course,
				SubComponentName.Practice);

		edoHomePage.ClickComponentStage("3");
		sleep(3);
		DragAndDropSection dragAndDropSection = new DragAndDropSection(
				webDriver, testResultService);

		dragAndDropSection.dragImageToPlace(ids[0], 1, 1);
		dragAndDropSection.dragImageToPlace(ids[1], 1, 2);
		dragAndDropSection.dragImageToPlace(ids[2], 1, 3);
		dragAndDropSection.dragImageToPlace(ids[3], 2, 1);
		dragAndDropSection.dragImageToPlace(ids[4], 2, 2);
		dragAndDropSection.dragImageToPlace(ids[5], 2, 3);

		report.report("Check answer");
		edoHomePage.clickOnCheckAnswers();

		dragAndDropSection.checkDraggedImageCorrectAnswer(ids[0], 1, 1);
		dragAndDropSection.checkDraggedImageCorrectAnswer(ids[1], 1, 2);
		dragAndDropSection.checkDraggedImageCorrectAnswer(ids[2], 1, 3);
		dragAndDropSection.checkDraggedImageCorrectAnswer(ids[3], 2, 1);
		dragAndDropSection.checkDraggedImageCorrectAnswer(ids[4], 2, 2);
		dragAndDropSection.checkDraggedImageCorrectAnswer(ids[5], 2, 3);
		
		edoHomePage.clickOnCourseByName(course.getName());
		sleep(5);

		pageHelper.logOut();
		checkProgressInDb("22777");

	}

	@Test
	@TestCaseParams(testCaseID = { "16589" })
	public void testDragAndDropCloze() throws Exception {
		// Advanced 1 Complaints 80 Reading 291 Complaint Practice Cloze 3
		// a1rlcop03 57206
		String[] words = new String[] { "Buys washing machine.",
				"Technician explains how machine works.",
				"Machine delivered and connected.",
				"Studies the instruction book thoroughly.",
				"Calls the department store.",
				"Technician comes to inspect machine.",
				"Threatens to take action with a lawyer." };
		Course course = pageHelper.getCourses().get(22);
		report.report("Login and navigate to unit component");
		EdoHomePage edoHomePage = pageHelper.loginAsStudent();
		
		edoHomePage.navigateToCourseUnitComponent(course,
				SubComponentName.Practice);

		edoHomePage.ClickComponentStage("2");

		DragAndDropSection dragAndDropSection = new DragAndDropSection(
				webDriver, testResultService);

		report.report("Arrange answers in the correct order");
		String xpath = "//div[@class='TextDiv']//span[%s]";

		dragAndDropSection.dragAnserToElementByXpath(words[0], xpath, "1");
		dragAndDropSection.dragAnserToElementByXpath(words[2], xpath, "2");
		dragAndDropSection.dragAnserToElementByXpath(words[3], xpath, "3");
		dragAndDropSection.dragAnserToElementByXpath(words[1], xpath, "4");
		dragAndDropSection.dragAnserToElementByXpath(words[5], xpath, "5");
		dragAndDropSection.dragAnserToElementByXpath(words[4], xpath, "6");
		dragAndDropSection.dragAnserToElementByXpath(words[6], xpath, "7");

		report.report("Check answer");
		edoHomePage.clickOnCheckAnswers();
		for (int i = 0; i < words.length; i++) {
			dragAndDropSection.checkDragAndDropCorrectAnswerCloze(words[i]);
		}

		report.report("Clear answers");
		edoHomePage.clickOnClearAnswer();

		report.report("Check that all element are back in the bank");
		for (int i = 0; i < words.length; i++) {
			dragAndDropSection.checkDragElementIsBackToBankCloze(words[i]);
		}

		report.report("Drop and replace from bank");

		dragAndDropSection.dragAnserToElementByXpath(words[4], xpath, "6");
		dragAndDropSection.dragAnserToElementByXpath(words[6], xpath, "6");
		dragAndDropSection.checkDragElementLocationCloze("6", "70");

		report.report("click on see answers and check that all correct answers are displayed");
		edoHomePage.clickOnSeeAnswer();
		dragAndDropSection.checkDragElementLocationCloze("1", "62");
		dragAndDropSection.checkDragElementLocationCloze("2", "89");
		dragAndDropSection.checkDragElementLocationCloze("3", "79");
		dragAndDropSection.checkDragElementLocationCloze("4", "66");
		dragAndDropSection.checkDragElementLocationCloze("5", "81");
		dragAndDropSection.checkDragElementLocationCloze("6", "54");
		dragAndDropSection.checkDragElementLocationCloze("7", "70");

		report.report("Drag element to the bank");
		edoHomePage.clickOnClearAnswer();
		dragAndDropSection.dragAnserToElementByXpath(words[4], xpath, "6");
		dragAndDropSection.dragClassificationAnswerToBankCloze(words[4]);
		dragAndDropSection.checkDragElementIsBackToBankCloze(words[4]);

		edoHomePage.clickOnCourseByName(course.getName());
		sleep(5);

		pageHelper.logOut();
		checkProgressInDb("57206");
	}

	@Test
	@TestCaseParams(testCaseID = { "16592" })
	public void testDragAndDropMatchTexttoPicture() throws Exception {
		// Basic 2 Healthy Eating 42 Listening 110 Food Practice Match Text To
		// Picture 1 b2lrfop01 57356
		Course course = pageHelper.getCourses().get(23);
		String[] words = new String[] { "a cucumber", "lettuce", "a plate",
				"meat", "dessert" };
		report.report("Login and navigate to unit component");
		EdoHomePage edoHomePage = pageHelper.loginAsStudent();
	
		edoHomePage.navigateToCourseUnitComponent(course,
				SubComponentName.Practice);

		DragAndDropSection dragAndDropSection = new DragAndDropSection(
				webDriver, testResultService);

		report.report("Drag all ansers");
		String xpath = "//div[@id='ListPlaceHolder']//div[%s]//div";
		dragAndDropSection.dragAnserToElementByXpath(words[0], xpath, "2");
		dragAndDropSection.dragAnserToElementByXpath(words[1], xpath, "1");
		dragAndDropSection.dragAnserToElementByXpath(words[2], xpath, "3");
		dragAndDropSection.dragAnserToElementByXpath(words[3], xpath, "5");
		dragAndDropSection.dragAnserToElementByXpath(words[4], xpath, "4");

		report.report("Check answer");
		edoHomePage.clickOnCheckAnswers();

		for (int i = 0; i < words.length; i++) {
			dragAndDropSection.checkDragAndDropCorrectAnswerCloze(words[i]);
		}

		report.report("Clear answers");
		edoHomePage.clickOnClearAnswer();

		report.report("Check that all element are back in the bank");
		for (int i = 0; i < words.length; i++) {
			dragAndDropSection.checkDragElementIsBackToBankCloze(words[i]);
		}

		report.report("Drop and replace from bank");
		edoHomePage.clickOnClearAnswer();
		dragAndDropSection.dragAnserToElementByXpath(words[4], xpath, "2");
		sleep(3);
		// edoHomePage.checkDragElementLocationPicture("2", words[4]);
		dragAndDropSection.dragAnserToElementByXpath(words[3], xpath, "2");
		dragAndDropSection.checkDragElementLocationPicture("2", "70");

		report.report("click on see answers and check that all correct answers are displayed");
		edoHomePage.clickOnSeeAnswer();
		dragAndDropSection.checkDragElementLocationPicture("1", "91");
		dragAndDropSection.checkDragElementLocationPicture("2", "83");
		dragAndDropSection.checkDragElementLocationPicture("3", "68");
		dragAndDropSection.checkDragElementLocationPicture("4", "43");
		dragAndDropSection.checkDragElementLocationPicture("5", "70");

		report.report("Drag element to the bank");
		edoHomePage.clickOnClearAnswer();
		dragAndDropSection.dragAnserToElementByXpath(words[4], xpath, "5");
		dragAndDropSection.dragClassificationAnswerToBankCloze(words[4]);
		dragAndDropSection.checkDragElementIsBackToBankCloze(words[4]);
		
		edoHomePage.clickOnCourseByName(course.getName());
		sleep(5);

		pageHelper.logOut();
		checkProgressInDb("57356");

	}

	@Test
	@TestCaseParams(testCaseID = { "17124" })
	public void testDragAndDrop3Columns() throws Exception {
		// Basic 3>>Eating Out>>Pearl's Party Service - 1
		Course course = pageHelper.getCourses().get(25);
		String[] words = new String[] { "fresh rolls", "grilled fish",
				"chicken", "strawberries and whipped cream", "salad",
				"chocolate nut cake" };
		report.report("Login and navigate to unit component");
		EdoHomePage edoHomePage = pageHelper.loginAsStudent();
	
		edoHomePage.navigateToCourseUnitComponent(course,
				SubComponentName.Practice);

		DragAndDropSection dragAndDropSection = new DragAndDropSection(
				webDriver, testResultService);

		dragAndDropSection.dragClassificationAnswerByTextFromBank(words[0], 1,
				1);
		dragAndDropSection.dragClassificationAnswerByTextFromBank(words[1], 1,
				2);
		dragAndDropSection.dragClassificationAnswerByTextFromBank(words[2], 2,
				2);
		dragAndDropSection.dragClassificationAnswerByTextFromBank(words[3], 1,
				3);
		dragAndDropSection.dragClassificationAnswerByTextFromBank(words[4], 2,
				1);
		dragAndDropSection.dragClassificationAnswerByTextFromBank(words[5], 2,
				3);

		report.report("Check answer");
		edoHomePage.clickOnCheckAnswers();

		for (int i = 0; i < words.length; i++) {
			dragAndDropSection.checkDragAndDropCorrectAnswer(words[i]);
		}
		edoHomePage.clickOnClearAnswer();
		for (int i = 0; i < words.length; i++) {
			dragAndDropSection.checkDragElementIsBackToBank(words[i]);
		}

		edoHomePage.clickOnClearAnswer();

		report.report("Drop and replace from bank");
		dragAndDropSection.dragClassificationAnswerByTextFromBank(words[0], 1,
				1);
		dragAndDropSection.dragClassificationAnswerByTextFromBank(words[1], 1,
				1);

		dragAndDropSection.checkDragElementLocationInTable("61", "1", "1");

		report.report("Drop and replace between used elements");
		edoHomePage.clickOnClearAnswer();
		dragAndDropSection.dragClassificationAnswerByTextFromBank(words[0], 1,
				1);
		dragAndDropSection.dragClassificationAnswerByTextFromBank(words[1], 1,
				2);

		dragAndDropSection.dragClassificationAnswerByTextFromBank(words[0], 1,
				1);
		dragAndDropSection.checkDragElementLocationInTable("58", "1", "1");
		dragAndDropSection.checkDragElementIsBackToBank(words[1]);
		report.report("click on see answers");
		edoHomePage.clickOnClearAnswer();
		edoHomePage.clickOnSeeAnswer();

		for (int i = 0; i < words.length; i++) {
			dragAndDropSection.checkDragElementIsPlaced(words[i]);
		}
		
		edoHomePage.clickOnCourseByName(course.getName());
		sleep(5);

		pageHelper.logOut();
		checkProgressInDb("23406");

	}

	public void checkProgressInDb(String itemId) throws Exception {
		String sql = "  select * from progress where UserId="
				+ dbService.getUserIdByUserName(
						autoInstitution.getStudentUserName(),
						autoInstitution.getInstitutionId()) + " and itemId="
				+ itemId
				+ "  and LastUpdate>DATEADD(second, -40 ,CURRENT_TIMESTAMP)";
		String result = dbService.getStringFromQuery(sql);
		report.report("Sql result is: " + result);
		testResultService.assertTrue("No progress found in DB", result != null);
	}

	@After
	public void tearDown() throws Exception {

		super.tearDown();
	}

}
