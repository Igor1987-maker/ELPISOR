package tests.edo;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Enums.PLTCycleType;
import Enums.PLTStartLevel;
import Enums.TestQuestionType;
import Objects.PLTCycle;
import Objects.PLTTest;
import Objects.TestQuestion;
import pageObjects.EdoHomePage;
import pageObjects.edo.edoPlacementTestPage;
import tests.misc.EdusoftWebTest;

public class EdoPLTTests extends EdusoftWebTest {

	@Before
	public void setup() throws Exception {
		super.setup();
		enableConsoleLoggin();
	}

	@Test
	public void basePLTTest() throws Exception {
		
		PLTTest pltTest = initPLTTestFromCSVFile("files/PLTTestData/PLTEDOAnswers.csv");
		
		EdoHomePage edoHomePage = pageHelper.loginAsStudent();
		edoPlacementTestPage edoPlacementTestPage = edoHomePage
				.clickOnPLTTests();
		edoPlacementTestPage.selectStartLevel(PLTStartLevel.Basic);

		// TestQuestion question = new TestQuestion(new String[] {
		// "New Town has joined an ecology program.",
		// "in cities and towns throughout the U.S." }, null, null,
		// new int[] { 2 }, false, TestQuestionType.RadioMultiple);
		//
		// TestQuestion question2 = new TestQuestion(new String[] { "Citizens",
		// "garbage" }, new String[] { "1_1", "1_2" }, new String[] {},
		// new int[] {}, true, TestQuestionType.DragAndDropMultiple);
		// List<TestQuestion> cycle1questions = new ArrayList<TestQuestion>();
		// cycle1questions.add(question);
		// cycle1questions.add(question2);

		// PLTCycle cycleOne = new PLTCycle(PLTStartLevel.Intermediate, 1, null,
		// cycle1questions, PLTCycleType.Reading);
		// cycleOne.setQuestions(cycle1questions);

		

		edoPlacementTestPage.performTest(pltTest);
		System.out.println("Test ended");

	}

	public PLTTest initPLTTestFromCSVFile(String path) throws Exception {
		PLTCycle cycle1 = new PLTCycle();
		PLTCycle cycle2 = new PLTCycle();

		cycle1 = initCycle("1", path, PLTStartLevel.Basic);
		cycle2 = initCycle("2", path, PLTStartLevel.Intermediate);

		List<PLTCycle> testCycles = new ArrayList<PLTCycle>();
		testCycles.add(cycle1);
		testCycles.add(cycle2);

		PLTTest pltTest = new PLTTest(testCycles);
		return pltTest;
	}

	private PLTCycle initCycle(String cycleNumber, String path,
			PLTStartLevel pltStartLevel) throws Exception {
		List<String[]> testData = textService.getStr2dimArrFromCsv(path);

		List<TestQuestion> cycleQuestions = new ArrayList<TestQuestion>();
		for (int i = 0; i < testData.size(); i++) {
			// if cycle number match - create TestQuestion object
			if (testData.get(i)[0].equals(cycleNumber)) {
				try {
					report.report("Reading line: "+i+" from csv file");
					String[] answers = textService.splitStringToArray(testData
							.get(i)[1],"\\|");
					
					String[] answerDestinations = textService
							.splitStringToArray(testData.get(i)[2],"\\|");
					String[] wrongAnswers = textService.splitStringToArray(testData
							.get(i)[3]);
//				int[] blankAnswers = textService.splitStringToIntArray(
//						testData.get(i)[5], "?!^");

//				boolean booleanAnswer = Boolean.getBoolean(testData.get(i)[5]);
					// String questionType = testData.get(i)[6];
					report.report("Question type from csv is: "+testData.get(i)[5]);
					TestQuestionType questionType = TestQuestionType
							.valueOf(testData.get(i)[5]);

					TestQuestion question = new TestQuestion(answers,
							answerDestinations, wrongAnswers, new int[]{},
							 questionType);
					cycleQuestions.add(question);
					report.report("Finished adding question data for line: "+i);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		PLTCycle pltCycle = new PLTCycle(pltStartLevel,
				Integer.valueOf(cycleNumber), null, cycleQuestions, null);
		return pltCycle;

	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

}
