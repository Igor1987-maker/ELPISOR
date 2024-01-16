package pageObjects.edo;

import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import drivers.GenericWebDriver;
import pageObjects.GenericPage;
import pageObjects.toeic.toeicResultsPage;
import services.TestResultService;

public class AssessmentsFullReportPage extends GenericPage {
	
	@FindBy(id = "ReadingGraphScore")
	public WebElement actualReadingScore_txt;
	
	@FindBy(id = "ListeningGraphScore")
	public WebElement actualListeningScore_txt;
	
	@FindBy(id = "cxtFinalTestReport")
	public WebElement actualName_txt;
	
	@FindBy(id = "dateOfTest")
	public WebElement actualDate_txt;
	
	@FindBy(id = "testReviewLink")
	public WebElement viewResultsButton;
	
	public AssessmentsFullReportPage(GenericWebDriver webDriver, TestResultService testResultService) throws Exception {
		super(webDriver, testResultService);
		// TODO Auto-generated constructor stub
		PageFactory.initElements(webDriver.getWebDriver(), this);
		waitForPageToLoad();
	}

	@Override
	public GenericPage waitForPageToLoad() throws Exception {
		// TODO Auto-generated method stub
		webDriver.waitUntilElementAppears(actualName_txt, 30);
		return null;
	}

	@Override
	public GenericPage OpenPage(String url) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void checkReadingAndListeningScoresInFullReport(String readingScore, String listeningScore) throws Exception {
		String actualReadingScore = actualReadingScore_txt.getText();
		String actualListeningScore = actualListeningScore_txt.getText();
		testResultService.assertEquals(readingScore, actualReadingScore,"Reading score wasn't as expected");
		testResultService.assertEquals(listeningScore, actualListeningScore,"Listening score wasn't as expected");
	}
	
	public void closeModulWindow() {
		//WebElement close = webDriver.getWebDriver().findElement(By.className("modal-close"));
		WebElement close = webDriver.getWebDriver().findElement(By.xpath("//*[@id='mainCtrl']/div[3]/div/div/section/div[1]/a"));
		close.click();
	}
	
	public void validateNameInFullReport(String testName) throws Exception {
		webDriver.waitForElementByWebElement(actualName_txt, "actualName_txt", true, 5);
		String actualName = actualName_txt.getText();
		String[] splittedName = testName.split(" ");
		int nameSize = splittedName.length;
		String expectedName = splittedName[0] + " " + splittedName[1] + " " + splittedName[2] + " -"; //+ splittedName[3] + " " + splittedName[4];
		for (int i = 3; i < nameSize; i++) {
			expectedName = expectedName + " " + splittedName[i];
		}
		expectedName = expectedName +" Report"; 
				
		testResultService.assertEquals(expectedName, actualName,"Test Name wasn't as expected in 'Full Report' Page");
	}
	
	public void validateDateInFullReport(String testDate) throws Exception {
		String actualDate = actualDate_txt.getText(); 
		testResultService.assertEquals(testDate, actualDate,"Test Date wasn't as expected in 'Full Report' Page");
	}
	
	public void clickViewResultsFromFullReport() throws Exception {
		webDriver.ClickElement(viewResultsButton);
	}
	
	public void checkAnswersInViewFullReportPopUp(String clickedTestName, String currentDate,
			String readingScore, String listeningScore, ArrayList<String> finalAnswersArray) throws Exception {
		
		// Change frame to full report frame
		webDriver.switchToFrame("bsModal__iframe");
		//Thread.sleep(1000);
	
		report.startStep("Validate Test Name in 'Full Report' Page");
			validateNameInFullReport(clickedTestName);
		//	Thread.sleep(3000);
			
		report.startStep("Validate Test Date in 'Full Report' Page");
			validateDateInFullReport(currentDate);
	
		report.startStep("Validate Reading and Listening Scores in 'Full Report' Page");
			checkReadingAndListeningScoresInFullReport(readingScore, listeningScore);
			
		report.startStep("Click View Results Button in 'Full Report' Page");
			clickViewResultsFromFullReport();
			
		// Initialize toeic results page and check answers
			toeicResultsPage toeicResultspage = new toeicResultsPage(webDriver,testResultService);
			//Thread.sleep(2000);
			toeicResultspage.checkAnswersInViewResultsPage(finalAnswersArray);
			
		//checkTestCookieFalse();
			//webDriver.getCookieByDomain("https://toeicolpc1.edusoftrd.com", "Personal");
	}

	public void validateNameInFullReport2(String clickedTestName) throws Exception {
		try {
			String actualName = actualName_txt.getText();
			textService.assertTrue("Incorrect tets name", actualName.contains(clickedTestName));
		}catch (Exception e) {
			testResultService.addFailTest(e.getMessage(), false, true);
		}catch (AssertionError e) {
			testResultService.addFailTest(e.getMessage(), false, true);
		}
		
	}
}
