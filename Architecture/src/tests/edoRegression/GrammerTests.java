package tests.edoRegression;

import org.junit.Before;
import org.junit.Test;

import Objects.Course;
import pageObjects.EdoHomePage;
import tests.misc.EdusoftWebTest;

public class GrammerTests extends EdusoftWebTest {

	@Before
	public void setup() throws Exception {
		super.setup();
	}

	@Test
	public void testGrammerB1ExploreTab() throws Exception {

		startStep("Init test data");
		int courseId = 4;
		this.testCaseId="7283";
		Course course = pageHelper.initCouse(courseId);

		startStep("Login to EDO as student");
		EdoHomePage edoHomePage = pageHelper.loginAsStudent();

		startStep("Navigate to Basic1>About people>Spelling changes>Explore tab");
		edoHomePage.clickOnCourses();
		edoHomePage.clickOnCourseByName(course.getName());
		edoHomePage.clickOnCourseUnit(course.getCourseUnit());
		edoHomePage.clickOntUnitComponent(course.getUnitComponent(), "Explore");

		startStep("Click on Play button");
		edoHomePage.clickOnPlayButton();

		startStep("Click on See explanation");
		edoHomePage.clickOnSeeExplanation();
		edoHomePage.checkExplanationPopupOpens();

		startStep("click on See text");
		edoHomePage.clickOnSeeText();
		edoHomePage.checkSeeTextPopupOpens();

	}

	@Test
	public void testGrammerB1PracticeTab() throws Exception {
		startStep("Init test data");
		this.testCaseId="7284";
		int courseId = 4;
		Course course = pageHelper.initCouse(courseId);
		String answerId = "66";

		startStep("Login to EDO as student");
		EdoHomePage edoHomePage = pageHelper.loginAsStudent();

		startStep("Navigate to Basic1>About people>Spelling changes>Practice tab");
		edoHomePage.clickOnCourses();
		edoHomePage.clickOnCourseByName(course.getName());
		edoHomePage.clickOnCourseUnit(course.getCourseUnit());
		edoHomePage
				.clickOntUnitComponent(course.getUnitComponent(), "Practice");
		edoHomePage.dragAnswer(answerId);

		edoHomePage.clickOnClearAnswer();
		edoHomePage.checkIfAnswerIsCleared(answerId);
		
	}
	
	@Test
	public void testGrammerB1TestTab() throws Exception {
		startStep("Init test data");
		this.testCaseId="7285";
		int courseId = 4;
		Course course = pageHelper.initCouse(courseId);
		String answerId = "66";

		startStep("Login to EDO as student");
		EdoHomePage edoHomePage = pageHelper.loginAsStudent();

		startStep("Navigate to Basic1>About people>Spelling changes>Test tab");
		edoHomePage.clickOnCourses();
		edoHomePage.clickOnCourseByName(course.getName());
		edoHomePage.clickOnCourseUnit(course.getCourseUnit());
		edoHomePage
				.clickOntUnitComponent(course.getUnitComponent(), "Test");

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


}
