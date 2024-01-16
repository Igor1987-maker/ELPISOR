package tests.edoRegression;

import org.junit.Before;
import org.junit.Test;

import pageObjects.EdoHomePage;
import Objects.Course;
import tests.misc.EdusoftWebTest;

public class ListeningAudioTests extends EdusoftWebTest {

	@Before
	public void setup() throws Exception {
		super.setup();
	}

	//Test case id 11961
	@Test
	public void testPracticeWritingCheckAnswerBasicFunctionality()
			throws Exception {
		startStep("Init test data");
		this.testCaseId = "11961";
		int courseId = 7;
		Course course = pageHelper.initCouse(courseId);
		String[] answerStrings = new String[] { "might", "cold", "terrible",
				"pain", "relax", "get up" };

		startStep("Login to EDO and get to the course");
		EdoHomePage edoHomePage = pageHelper.loginAsStudent();
		edoHomePage.clickOnCourses();
		edoHomePage.clickOnCourseByName(course.getName());
		edoHomePage.clickOnCourseUnit(course.getCourseUnit());
		edoHomePage.clickOntUnitComponent(course.getUnitComponent(), "Practice");
		edoHomePage.ClickComponentStage("4");
//		edoHomePage.clickOnNextComponent(5);
		sleep(3);
		
		startStep("Validate that 6 input fields are present");
		edoHomePage.checkInputFieldIsDisplayed("0");
		edoHomePage.checkInputFieldIsDisplayed("1");
		edoHomePage.checkInputFieldIsDisplayed("2");
		edoHomePage.checkInputFieldIsDisplayed("3");
		edoHomePage.checkInputFieldIsDisplayed("4");
		edoHomePage.checkInputFieldIsDisplayed("5");
		
		startStep("Fill corretc answers");
		edoHomePage.typeAnswerInInputField("0",answerStrings[0]);
		edoHomePage.typeAnswerInInputField("1",answerStrings[1]);
		edoHomePage.typeAnswerInInputField("2",answerStrings[2]);
		edoHomePage.typeAnswerInInputField("3",answerStrings[3]);
		edoHomePage.typeAnswerInInputField("4",answerStrings[4]);
		edoHomePage.typeAnswerInInputField("5",answerStrings[5]);
		
		startStep("Check answers");
		edoHomePage.clickOnCheckAnswers();
		edoHomePage.checkTextInputAnswer("0",true);
		edoHomePage.checkTextInputAnswer("1",true);
		edoHomePage.checkTextInputAnswer("2",true);
		edoHomePage.checkTextInputAnswer("3",true);
		edoHomePage.checkTextInputAnswer("4",true);
		edoHomePage.checkTextInputAnswer("5",true);
	
		startStep("Clear answers");
		edoHomePage.clickOnClearAnswer();
		
		startStep("Enter wrong answer and check the answer");
		edoHomePage.typeAnswerInInputField("0","bla bla");
		edoHomePage.clickOnCheckAnswers();
		edoHomePage.checkTextInputAnswer("0",false);
		

		
		

	}

}
