package pageObjects.toeic;

import java.util.ArrayList;
import java.util.List;

import org.junit.experimental.theories.Theories.TheoryAnchor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Enums.ByTypes;
import drivers.GenericWebDriver;
import pageObjects.GenericPage;
import pageObjects.edo.NewUxAssessmentsPage;
import services.TestResultService;

public class toeicResultsPage extends GenericPage {
	NewUxAssessmentsPage newUxAssessmentsPage = new NewUxAssessmentsPage(webDriver,testResultService);
	
	@FindBy(xpath = "//ul[@id='itemsReviewList']/li")
	public List<WebElement> answersTabs;
					 
	@FindBy(xpath = "//div[contains(@class,'rightWrong')]")
	public List<WebElement> answersSigns;
	
	@FindBy(xpath = "//div[contains(@class,'lessonMultipleAnswer')]")
	public List<WebElement> answersOptions;
	
	@FindBy(xpath = "//div[contains(@class,'radioTextWrapper')]")
	public List<WebElement> answersText;
	
	@FindBy(className = "closingTest")
	public WebElement closeButton;
	
	@FindBy(xpath = "//div[contains(@class,'lessonMultipleAnswer')]//input")
	public List<WebElement> answersCheckBoxes;
		
	public toeicResultsPage(GenericWebDriver webDriver, TestResultService testResultService) throws Exception {
		super(webDriver, testResultService);
		// TODO Auto-generated constructor stub
		PageFactory.initElements(webDriver.getWebDriver(), this);
	}

	@Override
	public GenericPage waitForPageToLoad() throws Exception {
		// TODO Auto-generated method stub
		webDriver.waitUntilElementAppears(answersTabs, 120);
		return null;
	}

	@Override
	public GenericPage OpenPage(String url) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public void validateAnswers(ArrayList<String> answers) throws Exception {
		try {
			
			String actualNumber = "";
			String actualAnswerSign = "";
			String correctAnswer = "";
			boolean isVcheckDisplayed = false;
			boolean isCorrectAnswerChecked = false;
			
			webDriver.waitUntilElementAppears(answersTabs, 60);
			
			// run through tabs
			for (int i = 0; i < answers.size(); i++) {
				int answerNum = i + 1;
				
				// click the tab
				webDriver.ClickElement(answersTabs.get(i));
				webDriver.waitUntilElementAppears(answersOptions, 30);
				Thread.sleep(600);
				
				// retrieve answer num and validate its equal to i + 1
				actualNumber = answersTabs.get(i).getText();
				testResultService.assertEquals(actualNumber, Integer.toString(answerNum), "Answer Number is not Displayed Correctly.");
			
				// retrieve current answer's sign
				actualAnswerSign = answersSigns.get(i).getAttribute("className");
				testResultService.assertEquals(true, actualAnswerSign.contains(" v "), "Answer's Sign is Not Correct. (Answer Number: "+answerNum+")");
			
				correctAnswer = answers.get(i);
				
				// wait for questions to load
				webDriver.waitUntilElementAppears("//div[@class='qTextWrapper']", 10);
				
				// retrieve the number of questions in the current tab and run trough each question until we find the question of the current tab
				ArrayList<WebElement> questionsBlocks = (ArrayList<WebElement>) webDriver.getWebDriver().findElements(By.xpath("//div[@class='qTextWrapper']"));
				for (int k = 0; k < questionsBlocks.size(); k++) {
					if (questionsBlocks.get(k).getText().contains(actualNumber)) {
						
						// retrieve the number of answers for each question
						int numOfAnswersForQuestion = retrieveNumOfAnswersForQuestionInResultsPage(questionsBlocks.size());
						
						// validate v is displayed near the correct answers
						isVcheckDisplayed = false;
						isCorrectAnswerChecked = false;
						for (int j = 0; j < numOfAnswersForQuestion; j++) {
							if (answersText.get(j+numOfAnswersForQuestion*k).getText().contains(correctAnswer)) {
								isVcheckDisplayed = answersOptions.get(j+numOfAnswersForQuestion*k).getAttribute("className").contains("vCheck");
								isCorrectAnswerChecked = answersCheckBoxes.get(j+numOfAnswersForQuestion*k).getAttribute("checked").equals("true");
								break;
							}
						}
						testResultService.assertEquals(true,isVcheckDisplayed,"V Sign is Not Displayed Near the Correct Answer");	
						testResultService.assertEquals(true,isCorrectAnswerChecked,"The Checkbox of the Correct Answer Is Not Checked");
						break;
					}
				}
				Thread.sleep(500);
			}
			
			// for the next answer (the wrong one)
			webDriver.ClickElement(answersTabs.get(answers.size()));
			Thread.sleep(500);
			actualNumber = answersTabs.get(answers.size()).getText();
			testResultService.assertEquals(actualNumber, Integer.toString(answers.size()+1), "Answer Number is not Displayed Correctly.");
	
			actualAnswerSign = answersSigns.get(answers.size()).getAttribute("className");
			testResultService.assertEquals(true, actualAnswerSign.contains("x"), "Answer's Sign is Not Correct. (Answer Number: "+Integer.toString(answers.size()+1)+")");
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public int retrieveNumOfAnswersForQuestionInResultsPage(int numOfQuestionsInTab) {
		int numOfAllAnswersInPage = answersText.size();
		return numOfAllAnswersInPage/numOfQuestionsInTab;
	}
	
	public void checkAnswersInViewResultsPage(ArrayList<String> finalAnswersArray) throws Exception {
		// change focus to the new opened pop up
		Thread.sleep(2000);
		webDriver.switchToPopup();
		//Thread.sleep(1000);
	//	boolean isErrorDisplayed = newUxAssessmentsPage.closeErrorPopUpIfItsDisplayed();
//		if (!isErrorDisplayed) {
			
			waitForPageToLoad();

			report.startStep("Validate the URL is Correct");
			webDriver.validateURL("/Runtime/testResultsReview.aspx");// https://toeicolpc1.edusoftrd.com/Runtime/testResultsReview.aspx
		
			report.startStep("Validate the Answers are Displayed Correctly in the Toeic Results Page");
			validateAnswers(finalAnswersArray);
			
			// Initialize toeic results page
			toeicResultsPage toeicResultspage = new toeicResultsPage(webDriver,testResultService);
					
			report.startStep("Close the Toeic Results Page");
			webDriver.ClickElement(toeicResultspage.closeButton);
			webDriver.switchToPreviousTab();
			Thread.sleep(1000);
	//	} else {
			testResultService.addFailTest("'View Results' Button was Clicked and an Error pop up Appeared (skipping test)");
		//}
	}

	public void verifyCorrectAnswerSignsOnTabs(int size) throws Exception {
		
		report.startStep("Switch to result pop-up page");
			Thread.sleep(2000);
			webDriver.switchToPopup();
			waitForPageToLoad();
			
		report.startStep("Validate the URL is Correct");
			webDriver.validateURL("/Runtime/testResultsReview.aspx");
		
			for(int i = 0; i<answersSigns.size(); i++) {
				String actualAnswerSign = answersSigns.get(i).getAttribute("className");
				if(i<size) {
					//boolean b = actualAnswerSign.contains(" v");
					testResultService.assertEquals(true, actualAnswerSign.contains(" v"), "Answer's Sign is Not Correct. (Answer Number: "+(i+1)+")");
				}else { 
					testResultService.assertEquals(true, actualAnswerSign.contains(" x"), "Answer's Sign is Not Correct. (Answer Number: "+(i+1)+")");
				}
			}
			
		report.startStep("Close the Toeic Results Page");
			webDriver.ClickElement(closeButton);
			webDriver.switchToPreviousTab();
			Thread.sleep(1000);
	}
}
