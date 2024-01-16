package pageObjects.edo;

import org.openqa.selenium.WebElement;

import Enums.ByTypes;
import drivers.GenericWebDriver;
import pageObjects.GenericPage;
import services.TestResultService;

public class NewUxUnitObjectivesPage extends GenericPage {

	private static final String X_Button_Xpath = "//a[@class='modal-close']";
	
	public NewUxUnitObjectivesPage(GenericWebDriver webDriver,
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
	
	public void close() throws Exception {
		
		webDriver.switchToMainWindow();
		webDriver.waitForElement(X_Button_Xpath,ByTypes.xpath, "unit Objective X button").click();

	}
	
	public void switchToUnitOjbjectiveFrame() throws Exception {
		webDriver.switchToFrame("bsModal__iframe");
	}
	
	public String getSelectedUnitNumber() throws Exception {
		return webDriver.waitForElement("//*[@id='unitList']/li[@class='active']/div/a/span",ByTypes.xpath).getText();
	}

	public String getSelectedUnitTitle() throws Exception {
		String [] title = webDriver.waitForElement("//*[@id='unitList']/li[@class='active']/div/a",ByTypes.xpath).getText().split("\n");
		return title[1];
	}
	
	public String getSelectedInfoTitle() throws Exception {
		return webDriver.waitForElement("//div[@class='unitInfoContent']/h2",ByTypes.xpath).getText();
	}
	
	public String selectDifferentUnitInPage(int unit) throws Exception {
		webDriver.waitForElement("//*[@id='unitList']/li[" + unit + "]/div/a",ByTypes.xpath).click();
		return webDriver.waitForElement("//*[@id='unitList']/li[" + unit + "]/div/a/span[2]",ByTypes.xpath).getText();
	}
	
	public String getUnitContentText() throws Exception {
		return webDriver.waitForElement("//div[@class='unitInfoContentText']",ByTypes.xpath).getText();
	}
	
	public void checkUnitNameIsDisplayedCorrectly(int numOfUnitTest, String objectiveName) throws Exception {
		//report.startStep("Verify Selected Unit Name (on the left side bar)");
		testResultService.assertEquals(String.valueOf(numOfUnitTest) + ".", getSelectedUnitNumber());
		testResultService.assertEquals(objectiveName, getSelectedUnitTitle());
		
		//report.startStep("Verify the unitObjective Title Content");
		testResultService.assertEquals("Unit " + numOfUnitTest + " | " + objectiveName, getSelectedInfoTitle());
		//testResultService.assertEquals(unitObjPage.getUnitContentText().indexOf("Listen for descriptions of people in general business situations") >= 0, true);
	
	}
	
	public void checkUnitObjectivesPage(int firstUnitToPress, int secondUnitToPress, String objectiveName) throws Exception {
		
	//report.startStep("Verify Selected Unit Name (on the left side bar) and the Title");
		checkUnitNameIsDisplayedCorrectly(firstUnitToPress, objectiveName);
		
	//report.startStep("Press on a different unit ("+secondUnitToPress+")");	
		objectiveName = selectDifferentUnitInPage(secondUnitToPress);
		Thread.sleep(1000);
		
	//report.startStep("Verify Selected Unit Name (on the left side bar) and the Title");
		checkUnitNameIsDisplayedCorrectly(secondUnitToPress, objectiveName);
	
	}
}
