package tests.edoRegression;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pageObjects.EdoHomePage;
import Enums.ByTypes;
import Objects.Course;
import tests.misc.EdusoftWebTest;

public class SkillsTests extends EdusoftWebTest {

	@Before
	public void setup() throws Exception {
		super.setup();
	}

	@Test
	public void testSkillGrammer() throws Exception {
		startStep("Login as student");
		EdoHomePage edoHomePage = pageHelper.loginAsStudent();
		startStep("open course");
		int courseId = 4;// Basic 1,About People,Spelling Changes,6
		String answerId = "66";
		Course course = pageHelper.initCouse(courseId);
		startStep("click on explore");
		edoHomePage.clickOnCourses();
		edoHomePage.clickOnCourseByName(course.getName());
		edoHomePage.clickOnCourseUnit(course.getCourseUnit());
		edoHomePage.clickOntUnitComponent(course.getUnitComponent(), "Explore");
		edoHomePage.clickOnPlayButton();
		edoHomePage.clickOnSeeExplanation();
		edoHomePage.checkExplanationPopupOpens();
		edoHomePage.clickOnSeeText();
		edoHomePage.checkSeeTextPopupOpens();
		startStep("Start practice");
		edoHomePage.clickOnPracticeTab();
		// edoService.getCorrectAnswerId()
		edoHomePage.dragAnswer(answerId);

		edoHomePage.clickOnClearAnswer();
		edoHomePage.checkIfAnswerIsCleared(answerId);
		startStep("Start test");
		edoHomePage.clickOnTestTab();
		edoHomePage.clickOnStartTest();
		edoHomePage.dragAnswer("38");
		edoHomePage.clickOnNextComponent(1);
		edoHomePage.dragAnswer("55");
		edoHomePage.clickOnNextComponent(1);
		edoHomePage.dragAnswer("80");
		edoHomePage.clickOnNextComponent(1);
		edoHomePage.dragAnswer("80");
		edoHomePage.clickOnNextComponent(1);
		edoHomePage.dragAnswer("36");
		edoHomePage.clickOnSubmitTest();
		edoHomePage.checkStudentScore("100%");

	}

	@Test
	public void TestSkillVocabulary() throws Exception {
		startStep("Init test data");
		
		int courseId=5;
		Course course = pageHelper.initCouse(courseId);
		
		
		startStep("Open Edo and login as a student");
		EdoHomePage edoHomePage = pageHelper.loginAsStudent();
		
		startStep("Open vocabalary course unit");
		edoHomePage.clickOnCourses();
		edoHomePage.clickOnCourseByName(course.getName());
		edoHomePage.clickOnCourseUnit(course.getCourseUnit());
		
		startStep("Prepare - play the video");
		edoHomePage.clickOntUnitComponent(course.getUnitComponent(), "Prepare");
		edoHomePage.checkUnitHeaderText(course.getUnitComponent());
		
		startStep("Explore - check the words");
		edoHomePage.clickOnExploreTab();
		edoHomePage.checkUnitHeaderText(course.getUnitComponent());
		
		
		startStep("Practice - answer questions");
		edoHomePage.clickOnPracticeTab();
		edoHomePage.checkUnitHeaderText(course.getUnitComponent());
		edoHomePage.markVocabRadioBtnAnswer("1","Good morning!");
		edoHomePage.markVocabRadioBtnAnswer("2","Hello!");
		edoHomePage.markVocabRadioBtnAnswer("3","Good night!");
		edoHomePage.clickOnCheckAnswers();
		edoHomePage.checkVocabRadioBtnAnswer("1",true,"Good morning!");
		edoHomePage.checkVocabRadioBtnAnswer("2",true,"Hello!");
		edoHomePage.checkVocabRadioBtnAnswer("3",true,"Good night!");
		edoHomePage.clickOnNextComponent(1);
		
		edoHomePage.dragAnswerToElement("86", webDriver.waitForElement("//div[@id=2]", ByTypes.xpath));
		edoHomePage.dragAnswerToElement("45", webDriver.waitForElement("//div[@id=3]", ByTypes.xpath));
		edoHomePage.dragAnswerToElement("89", webDriver.waitForElement("//div[@id=4]", ByTypes.xpath));
		edoHomePage.clickOnCheckAnswers();
		
		
		
		
		
		
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

}
