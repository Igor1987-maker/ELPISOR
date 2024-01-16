package pagesObjects.oms;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.taskdefs.Sleep;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Enums.ByTypes;
import drivers.GenericWebDriver;
import pageObjects.GenericPage;
import services.TestResultService;

public class ProgressReportPage extends GenericPage {

	public static String oldTab;
	
	@FindBy(id = "cxtUserName") //TIminutes
	public WebElement title;
	
	@FindBy(id = "TIminutes") 
	public WebElement timeOnLesson;
		
	@FindBy(css = "tr[id ^= 'u']")
	public List<WebElement> units;//closeModal('cancel')  
	
	@FindBy(xpath = "//a[normalize-space()='X']")
	public WebElement closeProgressReportWindow;
	
	@FindBy(id = "uTestGraphScoreCircle")
	public WebElement unitAvarageScore;
	            //scoreReport1163086340653dc3c8cc5-5620-41b3-a65f-7e03a1d4b183
	@FindBy(xpath = "//*[@class='scoreReportLink'][text()='Score Report']")
	public WebElement finalTestScoreReport;
	
	@FindBy(css = "tr[id^='details'] td[class='width90']:first-child")
	public WebElement dateOfUnitCompletion;
	
	//tr[id^='details'] td[class='width90']:first-child
	
	public ProgressReportPage(GenericWebDriver webDriver, TestResultService testResultService) throws Exception {
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
	
	public boolean checkTitleOfProgressReportPage() throws Exception {
		report.startStep("Verify title of progress report page");
		webDriver.waitForElementByWebElement(title, "Title of progress window", true, 30000);
		int wait = 0;
		while(wait<3&&title==null) {
			webDriver.waitForElementByWebElement(title, "Title of progress window", true, 30000);
		}
		Thread.sleep(5000);
		String windowTitle = title.getText().trim();
		return windowTitle.contains("Progress Report");
	}
	
	public ProgressReportPage checkTimeSpendedOnTestMatch(String time) throws Exception {
	
	report.startStep("Verify course invested time from Ed are same at OMS");
		
		String timeInOMS = timeOnLesson.getText().toString();
		textService.assertEquals("Time from Ed and OMS are not match",time,timeInOMS);
		
		return this;
	}
	
	public ProgressReportPage checkProgressDisplayedCorrectly(String edUnitName, String actCompletion) throws Exception {
		
	report.startStep("Verify if Progress report page are open");
		
		boolean isTitleOfProgressPresent = checkTitleOfProgressReportPage();
		testResultService.assertEquals(true, isTitleOfProgressPresent
				,"Progress window message incorrect or doesn't open");
		//String[] studentProgress = dbService.getProgressOfStudentFromTOEIC();
		
		WebElement graphBlock = null;
		WebElement graphScore = null;;
		boolean isFreshDate = false;
		int iterationToWait = 0;
	report.startStep("Validate progress and date from ED with OMS");
		if(isTitleOfProgressPresent) {
		report.startStep("Searching wanted unit");
			while(iterationToWait <3 && units.get(0)==null) {
				Thread.sleep(4000);
			}
			for(WebElement el:units) {
				String unitName = el.getText().trim();
				if(unitName.contains(edUnitName)) {
					
					report.startStep("Extracting data of progress from UI"
							+ " and assert it with progress in DB");
					graphBlock = el.findElement(By.className("graphBlock"));
					graphScore = el.findElement(By.className("graphScoreCircle"));
					
					String valueOfGraphBlock = graphBlock.getAttribute("style");
					String valueOfGgraphScore = graphScore.getText();
					String[] per = valueOfGraphBlock.split("[:%]");
					String percents = per[1].trim();
					testResultService.assertEquals(valueOfGgraphScore,percents
							,"Percentage of UI not equals to each other");
					testResultService.assertEquals(actCompletion, percents
							,"Progress of UI and DataBase not equals");
					
					el.click();
					List<WebElement> dateEl = el.findElements(By.xpath("//td/table/tr[2]/td[3]"));
					//List<WebElement> dateEl = el.findElements(By.cssSelector("tr[id^='details'] td[class='width90']"));
					Thread.sleep(1000);
					webDriver.scrollToElement(dateEl.get(0));
					Thread.sleep(1000);
					String date = dateEl.get(0).getText().trim();
					LocalDate currentDate = LocalDate.now();
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
					String curDate = currentDate.format(formatter);
					isFreshDate = date.equals(curDate);
					
					textService.assertEquals("No fresh message", true, isFreshDate);
					break;
				}
			}
		}
		return this;
		
	}
	
	public ProgressReportPage checkUnitAndFinalTestProgress(String expectedUnitProgress) throws Exception {
		
	report.startStep("Verify if progress report page are open");
		Thread.sleep(5000);
		testResultService.assertEquals(true, checkTitleOfProgressReportPage()
				,"Progress window message incorrect or doesn't open");
		
		report.startStep("Verify progress of Unit test");
		String scoreOfUnitTest = unitAvarageScore.getText();
		textService.assertEquals("Score is wrong", expectedUnitProgress, scoreOfUnitTest);
				
		return this;
	}
	
	public FinalTestScoreReportPage clickOnFinalTestReport() throws Exception {

	report.startStep("Open final test report window");
		Thread.sleep(7000);
		testResultService.assertEquals(true, checkTitleOfProgressReportPage()
				,"Progress window message incorrect or doesn't open");
		webDriver.waitForElementByWebElement(finalTestScoreReport, "finalReport", true, 4000);
		finalTestScoreReport.click();
				return new FinalTestScoreReportPage(webDriver, testResultService);
	}
	
	
	public HomePage closeProgressReportWindow() throws Exception {
		report.startStep("Closing report window");
		closeProgressReportWindow.click();
						return new HomePage(webDriver, testResultService);
		}

}
