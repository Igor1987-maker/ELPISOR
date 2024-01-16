package pageObjects;

import Objects.TestQuestion;
import services.TestResultService;
import drivers.GenericWebDriver;

public abstract class TestQuestionPage extends GenericPage  {

	public TestQuestionPage(GenericWebDriver webDriver,
			TestResultService testResultService) throws Exception {
		super(webDriver, testResultService);
		// TODO Auto-generated constructor stub
	}

	public abstract void answerMultiCheckBoxQuestion(TestQuestion question) throws Exception;
	
	public abstract void clickOnNextButton() throws Exception;
	
	public abstract void dragAnswer(TestQuestion question)throws Exception;
	
	public abstract void playMedia()throws Exception;
	
	public abstract void answerCheckboxQuestion(TestQuestion question)throws Exception;
}
