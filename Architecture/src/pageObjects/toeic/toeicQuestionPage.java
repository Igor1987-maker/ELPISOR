package pageObjects.toeic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Enums.ByTypes;
import Objects.TestQuestion;
import drivers.GenericWebDriver;
import pageObjects.GenericPage;
import pageObjects.TestQuestionPage;
import pageObjects.edo.NewUxClassicTestPage;
import services.Reporter;
import services.TestResultService;

public class toeicQuestionPage extends TestQuestionPage {
	
	@FindBy(xpath = "//div[@class='answersWrapper']")
	public WebElement answersSection;
	
	@FindBy(xpath = "//div[@class='nextBT']/a")
	public WebElement nextButton;
	
	@FindBy(xpath = "//div[@class='qText']")
	public WebElement questionNumber;
	
	@FindBy(xpath = "//input[contains(@class,'lessonMultipleCheck')]")
	public List<WebElement> answersButtons;
	
	@FindBy(xpath = "//div[contains(@class,'radioTextWrapper')]")
	public List<WebElement> answersText;
	
	@FindBy(xpath = "//div[@class='submitBT']")
	public WebElement submitTest;
	
	@FindBy(id = "submitTestBtn")
	public WebElement submitYesButton;
	
	@FindBy(id = "cancelSubmitBtn")
	public WebElement submitNoButton;
	
	@FindBy(xpath = "//div[@class='unitTitle']")
	public WebElement sectionTitle;
	
	@FindBy(xpath = "//div[@class='StartBT']/a")
	public WebElement closeButton;
	
	public toeicQuestionPage(GenericWebDriver webDriver,
			TestResultService testResultService) throws Exception {
		super(webDriver, testResultService);
		// TODO Auto-generated constructor stub
		PageFactory.initElements(webDriver.getWebDriver(), this);
	}

	@Override
	public void clickOnNextButton() throws Exception {
		webDriver.waitForElement("//div[@class='nextBT']//a", ByTypes.xpath,
				"Test question next button");

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
	public void answerMultiCheckBoxQuestion(TestQuestion question) {
		// TODO Auto-generated method stub

	}

	public void answerCheckboxQuestion(String questionId, String answerId)
			throws Exception {
		webDriver.waitForElement(
				"//div[" + questionId + "]//div[" + questionId + answerId
						+ "]//div//div//div[2]",
				ByTypes.xpath,
				"answer checkbox for question " + questionId + " and answer: "
						+ answerId).click();
		;
	}

	@Override
	public void dragAnswer(TestQuestion question) {
		// TODO Auto-generated method stub

	}

	@Override
	public void playMedia() throws Exception {
		webDriver.waitForElement("div[@id='listenBtn']//a", ByTypes.xpath,
				"Play media button").click();

	}

	@Override
	public void answerCheckboxQuestion(TestQuestion question) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	public void validateAnswersSectionIsNotNull() throws Exception {
		/*
		WebElement element=null;
		
		for (int i=0; i<=20 && element==null;i++){
			element = webDriver.waitForElement("//*[contains(@class,'lessonAnswersWrapper')]", ByTypes.xpath, false, 1);
		}
		*/
		 
		webDriver.waitUntilElementAppears(answersSection, 20);
		String answersText = answersSection.getText();
		testResultService.assertEquals(true, answersText != null, "Answers Text is Null");
	}
	
	
	public void clickNext() {
		webDriver.waitUntilElementAppears(nextButton,5);
		webDriver.ClickElement(nextButton);
	}
	
	public void answerQuestions(ArrayList<String> answers) throws Exception {
		String correctAnswer = "";
		int numOfQustionsInPage = 0;
		int numOfAnswersForQuestion = 0;
		
		// Run Through all answers
		for (int i = 0; i < answers.size(); i++) {
			
			webDriver.waitUntilElementAppears("//div[contains(@class,'radioTextWrapper')]", 30);
			webDriver.waitUntilElementAppears(answersButtons, 10);
			webDriver.waitUntilElementAppears("//div[contains(@class,'lessonAnswersWrapper')]", 5);
//			lessonAnswersWrapper  word
			// Retrieve number of question in page
			numOfQustionsInPage = getNumOfQuestionsInPage();
		//	Thread.sleep(600);
			
			// Run through all questions in a page- when there is one question this loop will run one time
			for (int k = 0; k < numOfQustionsInPage; k++) {
				
				// Get the current correct answer
				correctAnswer = answers.get(i);
				
				// Retrieve the num of answers for each question (sometimes this number is 3 but most times it is 4)
				numOfAnswersForQuestion = retrieveNumOfAnswersForQuestion(numOfQustionsInPage);
					
				// Click the correct answer
				for (int j = 0; j < numOfAnswersForQuestion; j++) {

					if (webDriver.getTextFromElement(answersText.get(j+numOfAnswersForQuestion*k)).contains(correctAnswer))
					{
						if (numOfQustionsInPage>1)
							webDriver.scrollToElement(answersButtons.get(j+numOfAnswersForQuestion*k));
						//Thread.sleep(500);
						webDriver.ClickElement(answersButtons.get(j+numOfAnswersForQuestion*k));
						break;
					}
				}
				
				// if there is more than one question in a page- raise the counter i by one (i represent the question number)
				if (numOfQustionsInPage > 1 && (k+1) < numOfQustionsInPage) {
					i++;
				}
			}
			//Thread.sleep(500);
			clickNext();
			Thread.sleep(500);
		}
	}
	
	public int retrieveNumOfAnswersForQuestion(int numOfQustionsInPage) {
		int numOfAllAnswersInPage = answersText.size();
		return numOfAllAnswersInPage/numOfQustionsInPage;
	}
	
	public int getNumOfQuestionsInPage() {
		ArrayList<WebElement> questions = (ArrayList<WebElement>) webDriver.getWebDriver().findElements(By.xpath("//div[@class='qTextWrapper']"));
		return questions.size();
	}
	
	public void clickSubmitTest() {
		webDriver.ClickElement(submitTest);
	}
	
	public void submit(boolean continueBool) throws Exception {
		clickSubmitTest();
		if (continueBool) {
			webDriver.ClickElement(submitYesButton);	
		} else {
			webDriver.ClickElement(submitNoButton);
			webDriver.validateURL("/Runtime/Test.aspx");
		}
	}
	
	public void checkPartTwoIsDisplayed() throws Exception {
		if (webDriver.isDisplayed(sectionTitle)) {
			testResultService.assertEquals(true, sectionTitle.getText().contains("Part 2"), "The section Title doesn't contain 'Part 2'");
		} else {
			testResultService.addFailTest("Section Title is not Displayed");
		}
	}
	
	public void clickCloseButton() {
		webDriver.ClickElement(closeButton);
	}
	
	public void checkPartNumberIsDisplayed(int wantedNum) throws Exception {
		webDriver.waitUntilElementAppears(sectionTitle,10);
		testResultService.assertEquals(true, sectionTitle.getText().contains("Part " + wantedNum), "The section Title doesn't contain Part " + wantedNum);
		
		/*if (webDriver.isDisplayed(sectionTitle)) {
			testResultService.assertEquals(true, sectionTitle.getText().contains("Part " + wantedNum), "The section Title doesn't contain 'Part 2'");
		//} else {
			//testResultService.addFailTest("Section Title is not Displayed");
		}*/
	}

	public ArrayList<String> answerQuestionsInSeveralSections(int numOfSections, String answersFilePath) throws Exception {
		NewUxClassicTestPage classicTest = new NewUxClassicTestPage(webDriver,testResultService);
				
		ArrayList<String> finalAnswersArray = new ArrayList<String>();
		for (int i = 0; i < numOfSections; i++) {
			
			report.startStep("Validate The Answers Section is not null");
				validateAnswersSectionIsNotNull();
				//Thread.sleep(1000);
			
			// Test name,Test form,Skill,Section, CorectAnswer
			report.startStep("Retrieve answers from File");
				ArrayList<String> answersArray = classicTest.getAnswersArrayBySectionNumber(Integer.toString(i+1), answersFilePath);
				//Thread.sleep(2000);
				
				if (answersArray.size() == 0) {
					testResultService.addFailTest("Trying to Retrieve Answers in Section "+(i+1)+" from File, but they Don't Exist");
					break;	
				}
				
				finalAnswersArray.addAll(answersArray);
				Thread.sleep(300);
				
			report.startStep("Answer the Questions in section: " + (i+1));
				answerQuestions(answersArray);
				
			report.startStep("Check the Section Title is 'Part "+(i+2)+"'");
				checkPartNumberIsDisplayed(i+2);
				//Thread.sleep(1000);
				
			report.startStep("Click Next");
				clickNext();
				//Thread.sleep(1000);
		}
		return finalAnswersArray;
	}
	
	public void validateScoreIsDisplayedCorrectly(String expectedTestResult) throws Exception {
		String actualTestScore = webDriver.waitForElement("//span[contains(@class,'testResult')]",ByTypes.xpath, 5, false).getText();
		testResultService.assertEquals(expectedTestResult,actualTestScore,"Test score isn't as expected");
		
	}
}
