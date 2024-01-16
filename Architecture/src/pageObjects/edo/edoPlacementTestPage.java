package pageObjects.edo;

import java.util.List;

import org.openqa.selenium.WebElement;

import Enums.ByTypes;
import Enums.PLTStartLevel;
import Enums.PLTStartLevel;
import Enums.TestQuestionType;
import Objects.PLTCycle;
import Objects.PLTTest;
import Objects.TestQuestion;
import drivers.GenericWebDriver;
import pageObjects.GenericPage;
import pageObjects.TestQuestionPage;
import services.TestResultService;
import services.TextService;

public class edoPlacementTestPage extends TestQuestionPage {

	TextService textService = new TextService();

	public edoPlacementTestPage(GenericWebDriver webDriver,
			TestResultService testResultService) throws Exception {
		super(webDriver, testResultService);
		// TODO Auto-generated constructor stub
	}

	public void answerMultiCheckBoxQuestion(TestQuestion question)
			throws Exception {
		for (int i = 0; i < question.getCorrectAnswers().length; i++) {
			webDriver.waitForElement(
					"//span[@class='multiTextInline'][contains(text(),"
							+ question.getCorrectAnswers()[i] + ")]",
					ByTypes.xpath).click();
		}

	}

	@Override
	public void clickOnNextButton() throws Exception {
		webDriver.waitForElement("nextQuest", ByTypes.id, "Next button")
				.click();

	}

	public void dragAnswer(String answerId, String questionLocationId)
			throws Exception {
		WebElement from = webDriver.waitForElement(
				"//div[@data-id='" + answerId + "']",
				ByTypes.xpath,
				"element of answer number text "
						+ questionLocationId.toString());
		WebElement to = webDriver.waitForElement("//span[@data-id='"
				+ questionLocationId + "']", ByTypes.xpath);
		webDriver.dragAndDropElement(from, to);

	}

	public void answerDragAndDropQuestion(TestQuestion question)
			throws Exception {
		String[] answerSourceLocations = question.getCorrectAnswers();
		String[] answerDestinationLocations = question.getAnswersDestinations();
		for (int i = 0; i < answerSourceLocations.length; i++) {
			int index = i + 1;
			WebElement from = webDriver.waitForElement(
					"//div[text()=" + question.getCorrectAnswers()[i] + "]",
					ByTypes.xpath,
					"element of answer number text "
							+ answerSourceLocations[i].toString());
			WebElement to = webDriver.waitForElement("//span[@data-id='1_"
					+ index + "']", ByTypes.xpath);
			webDriver.dragAndDropElement(from, to);
		}
	}

	@Override
	public GenericPage waitForPageToLoad() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GenericPage OpenPage(String url) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void playMedia() throws Exception {
		webDriver.waitForElement("CTrackerPlayBtn", ByTypes.id,
				"Play media button").click();

	}

	@Override
	public void answerCheckboxQuestion(TestQuestion question) throws Exception {

		webDriver
				.waitForElement(
						"//span[@class='multiTextInline'][contains(text(),"
								+ question.getCorrectAnswers()[0] + ")]",
						ByTypes.xpath).click();

	}

	public void answerComboBoxQuestion(TestQuestion question) throws Exception {
		int index = 0;
		for (int i = 0; i < question.getCorrectAnswers().length; i++) {
			index = i + 1;
			webDriver.waitForElement(
					"//div[@class='fitb']//span[@id='1_" + index + "']//div[2]",
					ByTypes.xpath).click();
			Thread.sleep(1000);
			webDriver.waitForElement(
					"//div[@class='optionsWrapper']//table//tbody//tr//td[contains(text(),"
							+ question.getCorrectAnswers()[i] + ")]",
					ByTypes.xpath).click();

		}

	}

	public void answerTrueFalseQuestion(TestQuestion question) throws Exception {

	}

	// public void answerQuestion(TestQuestion question) {
	// // Check question type
	// switch (question.getQuestionType()) {
	// case Basic:
	// case Advance:
	// case Intermediate:
	// case IamNotSure:
	//
	// }
	// ;
	// }

	public void selectStartLevel(PLTStartLevel level) throws Exception {
		switch (level) {
		case Basic:
			webDriver.waitForElement("rbBasic", ByTypes.id,
					"Basic level button").click();
			break;
		case Advanced:
			webDriver.waitForElement("rbAdvanced", ByTypes.id,
					"Intermediate level button").click();
			break;
		case Intermediate:
			webDriver.waitForElement("rbIntermediate", ByTypes.id,
					"Advance level button").click();
			break;
		case IamNotSure:
			webDriver.waitForElement("rbImNotSure", ByTypes.id,
					"Im not sure button").click();
			break;

		}
		;
		webDriver.waitForElement("Submit", ByTypes.name).click();

	}

	public void performTest(PLTTest pltTest) throws Exception {
		TextService textService = new TextService();
		
		// Fill the test questions according to the questions arrayList filled
		// from CSV file

		// Go over all 1st cycle questions and answer them according to the
		// questions object in the 1st PLTCyccle object

		// Cycle 1 has 17-20 questions (17 for basic/inter./advances and 20 for
		// "Im not sure"
		PLTCycle firstCycle = pltTest.getCycles().get(0);
		PLTCycle SecondCycle = pltTest.getCycles().get(1);
		
		selectStartLevel(firstCycle.getPltStartLevel());

		try {
			answerCycleQuestions(firstCycle);

			// report.report("End of cycle 1");
			clickOnGoOnButton();

			// Start of cycle 2
			answerCycleQuestions(SecondCycle);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void clickOnGoOnButton() throws Exception {
		webDriver.waitForElement("DoAgain", ByTypes.id).click();

	}

	@Override
	public void dragAnswer(TestQuestion question) throws Exception {
		// TODO Auto-generated method stub

	}

	public void clickOnGoOn() throws Exception {
		webDriver.waitForElement("DoAgain", ByTypes.id).click();
	}

	public void answerCycleQuestions(PLTCycle pltCycle) throws Exception {
		for (int i = 0; i < pltCycle.getNumberOfQuestions(); i++) {
			TestQuestion question = pltCycle.getCycleQuestions().get(i);
			TestQuestionType questionType = question.getQuestionType();

			// convert ~ to ,
			String[] answers = new String[question.getCorrectAnswers().length];
			for (int j = 0; j < question.getCorrectAnswers().length; j++) {
				answers[j] = question.getCorrectAnswers()[j].replace("~", ",");
				answers[j] = textService.resolveAprostophes(answers[j]);
			}
			question.setCorrectAnswers(answers);

			switch (questionType) {
			case DragAndDropMultiple:
				answerDragAndDropQuestion(question);
				break;
			case RadioMultiple:
				answerMultiCheckBoxQuestion(question);
				break;

			case RadioSingle:
				answerCheckboxQuestion(question);
				break;

			case DragAndDropSingle:
				answerDragAndDropQuestion(question);
				break;

			case comboBox:
				answerComboBoxQuestion(question);

			}
			clickOnNextButton();

		}
	}

}
