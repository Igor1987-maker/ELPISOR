package pagesObjects.oms;

import java.util.ArrayList;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptExecutor;

import Enums.ByTypes;
import drivers.GenericWebDriver;
import pageObjects.GenericPage;
import services.TestResultService;

public class FinalTestScoreReportPage extends GenericPage {
	
	@FindBy(id = "ReadingGraphScore")
	public WebElement readingScore;
	
	@FindBy(id = "ListeningGraphScore")
	public WebElement listeningScore;

	@FindBy(xpath = "//h2[@id='cxtFinalTestReport']")
	public WebElement titlefinalTestReport;
	
	public FinalTestScoreReportPage(GenericWebDriver webDriver, TestResultService testResultService) throws Exception {
		super(webDriver, testResultService);
		PageFactory.initElements(webDriver.getWebDriver(), this);
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
	
	public FinalTestScoreReportPage checkFinalTestReportPageOpen() throws Exception {
	
	report.startStep("Verify if Final test progress report page are open");
		webDriver.switchToPopup();
		webDriver.waitForElementByWebElement(titlefinalTestReport, "Title", true, 20000);
		String title = titlefinalTestReport.getText().toString().trim();
		Thread.sleep(2000);
		textService.assertEquals("Title is wrong", title, "- Final Test Report");
		
		return this;
	}
	
	public ProgressReportPage checkReadingAndListeningScores(String expectedReading, String expectedListening) throws Exception{
		
	report.startStep("Verify Final test scores");
		String reading = readingScore.getText().toString().trim();
		String listening = listeningScore.getText().toString().trim();
		textService.assertEquals("Reading scores not match !!!", expectedReading, reading);
		textService.assertEquals("Listening scores not match !!!", expectedListening, listening);
		
		webDriver.getWebDriver().close();
	    webDriver.switchToMainWindow();
		return new ProgressReportPage(webDriver, testResultService);
	}
	

}
