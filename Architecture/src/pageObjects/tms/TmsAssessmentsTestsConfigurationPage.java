package pageObjects.tms;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Enums.ByTypes;
import drivers.GenericWebDriver;
import pageObjects.GenericPage;
import services.PageHelperService;
import services.TestResultService;

public class TmsAssessmentsTestsConfigurationPage extends GenericPage {
	
	PageHelperService pageHelper = new PageHelperService();
	TmsHomePage tmsHomePage = new TmsHomePage(webDriver, testResultService);
	
	@FindBy(xpath="//input[@id='OnlyOncePT']")
	public WebElement takeOnceOption;
	
	@FindBy(xpath="//input[@name='MidTermTLength']")
	public WebElement midTermDuration;
	
	@FindBy(xpath="//input[@name='Days']")
	public WebElement assignmentTimeDays;
	
	public TmsAssessmentsTestsConfigurationPage(GenericWebDriver webDriver, TestResultService testResultService)
			throws Exception {
		super(webDriver, testResultService);
		// TODO Auto-generated constructor stub
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
	
	public void goToAssessmentTestsConfiguration() throws Exception{
		report.startStep("Click on Assessment");
		webDriver.switchToTopMostFrame();
		tmsHomePage.switchToMainFrame();
		tmsHomePage.clickOnAssessment();
		Thread.sleep(3000);
		
		webDriver.closeAlertByAccept(2);
		
		report.startStep("Click on Tests Configuration");
		tmsHomePage.clickOnTestsConfiguration();
		Thread.sleep(2000);
	}
	
	public void selectPltAndGo() throws Exception {
	
		webDriver.switchToTopMostFrame();
		tmsHomePage.switchToMainFrame();
		tmsHomePage.switchToFormFrame();
		
		report.startStep("Select Feature");
		tmsHomePage.selectFeature("PT");
		
		report.startStep("Click on Go");
		tmsHomePage.clickOnGo();
		Thread.sleep(2000);
	}
	
	public void clickTakeOnce() throws Exception{
		webDriver.switchToTopMostFrame();
		tmsHomePage.switchToMainFrame();
		if (takeOnceOption.getAttribute("checked")==null) {
			webDriver.ClickElement(takeOnceOption);
		}
	}
	
	public void uncheckTakeOnce() throws Exception{
		webDriver.switchToTopMostFrame();
		tmsHomePage.switchToMainFrame();
		if (takeOnceOption.getAttribute("checked")==null){
			// already unchecked
		} else if (takeOnceOption.getAttribute("checked").equals("true")) {
			webDriver.ClickElement(takeOnceOption);
		}
	}
	
	public void selectMidTermAndFinalAndGo() throws Exception {
		
		webDriver.switchToTopMostFrame();
		tmsHomePage.switchToMainFrame();
		tmsHomePage.switchToFormFrame();
		
		report.startStep("Select Feature");
		tmsHomePage.selectFeature("ET");
		
		report.startStep("Click on Go");
		tmsHomePage.clickOnGo();
		Thread.sleep(2000);
	}
	
	public void updateMidtermTestDuration(String newDuration) throws Exception {
		
		webDriver.switchToTopMostFrame();
		tmsHomePage.switchToMainFrame();
		//tmsHomePage.switchToFormFrame();
		
		midTermDuration.clear();
		webDriver.SendKeys(midTermDuration, newDuration);
	}
	
	public void validateDays(String expectedDays) throws Exception {
		//webDriver.switchToTopMostFrame();
		//tmsHomePage.switchToMainFrame();
		
		testResultService.assertEquals(expectedDays, assignmentTimeDays.getAttribute("value"), "Assignment Time (Days) does not match data in DB");
	}
	
	public void updateDays(String newDays) throws Exception {
		//webDriver.switchToTopMostFrame();
		//tmsHomePage.switchToMainFrame();
		
		assignmentTimeDays.clear();
		webDriver.SendKeys(assignmentTimeDays, newDays);
	}

}
