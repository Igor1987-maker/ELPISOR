package tests.edoRegression;

import org.junit.Test;

import pageObjects.EdoHomePage;
import Objects.Course;
import tests.misc.EdusoftWebTest;

public class ListeningVideoTests extends EdusoftWebTest {
	//Test case 7309
	//
	@Test
	public void testVideoB2ExploreTab() throws Exception {
		startStep("Init test data");
		int courseId = 6;
		String scriptText="Can you give me directions?";
		Course course = pageHelper.initCouse(courseId);
		
		startStep("Login to Edo as student");
		EdoHomePage edoHomePage = pageHelper.loginAsStudent();
		
		startStep("Navigate to Basic2>Getting Help>Help");
		edoHomePage.clickOnCourses();
		edoHomePage.clickOnCourseByName(course.getName());
		edoHomePage.clickOnCourseUnit(course.getCourseUnit());
		edoHomePage.clickOntUnitComponent(course.getUnitComponent(), "Explore");
		
		startStep("Click on See Script and check the script");
		edoHomePage.clickOnSeeScript();
		edoHomePage.checkScript(scriptText);
	
	}
	//Test case id 7310
	@Test
	public void testVideoB2PracticeTab()throws Exception{
		startStep("Init test data");
		int courseId = 6;
		String scriptText="Can you give me directions?";
		Course course = pageHelper.initCouse(courseId);
		
		startStep("Login to Edo as student");
		EdoHomePage edoHomePage = pageHelper.loginAsStudent();
		
		startStep("Navigate to Basic2>Getting Help>Help");
		edoHomePage.clickOnCourses();
		edoHomePage.clickOnCourseByName(course.getName());
		edoHomePage.clickOnCourseUnit(course.getCourseUnit());
		edoHomePage.clickOntUnitComponent(course.getUnitComponent(), "Practice");
		
		startStep("Fill correct answer");
		edoHomePage.selectAnswerFromDropDown("1_1","directions");
		edoHomePage.selectAnswerFromDropDown("1_2","in front of");
		edoHomePage.selectAnswerFromDropDown("1_3","video shop");
		edoHomePage.selectAnswerFromDropDown("2_1","right");
		edoHomePage.selectAnswerFromDropDown("3_1","left");
		edoHomePage.selectAnswerFromDropDown("3_2","next to");
		
		edoHomePage.clickOnCheckAnswers();
		edoHomePage.checkDropDownAnswer("1_1", true);
		edoHomePage.checkDropDownAnswer("1_2", true);
		edoHomePage.checkDropDownAnswer("1_3", true);
		edoHomePage.checkDropDownAnswer("2_1", true);
		edoHomePage.checkDropDownAnswer("3_1", true);
		edoHomePage.checkDropDownAnswer("3_1", true);
		
		startStep("Fill wrong answer");
		edoHomePage.clickOnClearAnswer();
		edoHomePage.selectAnswerFromDropDown("1_1","a ride");
		edoHomePage.clickOnCheckAnswers();
		edoHomePage.checkDropDownAnswer("1_1", false);
		
		startStep("Navigate between practice tabs");
	}

}
