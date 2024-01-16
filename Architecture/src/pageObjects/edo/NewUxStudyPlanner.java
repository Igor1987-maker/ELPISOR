package pageObjects.edo;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import Enums.ByTypes;
import drivers.GenericWebDriver;
import pageObjects.GenericPage;
import services.TestResultService;

public class NewUxStudyPlanner extends GenericPage {

	private static final String X_Button_Xpath = "//a[@class='modal-close']";
	
	public NewUxStudyPlanner(GenericWebDriver webDriver,
			TestResultService testResultService) throws Exception {
		super(webDriver, testResultService);
		// TODO Auto-generated constructor stub
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
	
	
	
	public void verifyStudyPlannerHeader() throws Exception{
		WebElement element = webDriver.waitUntilElementAppears("//div[contains(@class, 'homeBarHeader')]", ByTypes.xpath, 20);
		//String header = webDriver.waitForElement("//div[contains(@class, 'homeBarHeader')]", ByTypes.xpath).getText();
		if (element!=null){
			String header = element.getText();
			testResultService.assertEquals("Study Planner", header, "Verifying Study Planner Header");
		}
		else
			testResultService.addFailTest("Study Planner window doesn't display",false,true);
	}
	
	
	public void close() throws Exception {
		webDriver.switchToMainWindow();
		WebElement Xbuton = webDriver.waitForElement(X_Button_Xpath,ByTypes.xpath, "Grammar Book X button");
		Xbuton.click();
	}

	public void createNewPlan() throws Exception {
		WebElement cPlan = webDriver.waitForElement("Create New Plan",ByTypes.linkText);
		cPlan.click();
	}
	
	public void switchToStudyPlannerWizard() throws Exception {
		webDriver.switchToNewWindow();
		webDriver.switchToFrame("baseIFRAr");
	}
	
	public void selectBasic1Level() throws Exception {
		WebElement element = webDriver.waitUntilElementAppearsAndReturnElement("/html/body/div[1]/span"
				, 2);
		
		String title = element.getText();
		//testResultService.assertEquals(title, "English Discoveries Online");
		testResultService.assertEquals(title, "Starting Level");
		
		List<WebElement> levelList = webDriver.waitForElement("StudyPlanBody",ByTypes.id).findElements(By.xpath("//tr/td/input"));
		levelList.get(1).click();
	}
	public void clickOnNext() throws Exception {		
		webDriver.switchToTopMostFrame();
		webDriver.getWebDriver().findElement(By.id("navBtn")).click();
	}
	
}
