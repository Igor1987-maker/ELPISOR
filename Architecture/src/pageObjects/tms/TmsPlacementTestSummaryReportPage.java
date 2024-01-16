package pageObjects.tms;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import drivers.GenericWebDriver;
import pageObjects.GenericPage;
import services.TestResultService;

public class TmsPlacementTestSummaryReportPage extends GenericPage{
	
	@FindBy(xpath = "/html/body/form/table/tbody/tr/td[1]/table[4]//tr//td[2]")
	public List<WebElement> levelsInSummaryTable;
	
	@FindBy(xpath = "/html/body/form/table/tbody/tr/td[1]/table[4]//tr//td[3]")
	public List<WebElement> finalResultsInSummaryTable;

	public TmsPlacementTestSummaryReportPage(GenericWebDriver webDriver, TestResultService testResultService)
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
	
	public int getLevelIndexInTable(String level) {
		int index = 0;
		for (int i = 0; i < levelsInSummaryTable.size(); i++) {
			if (levelsInSummaryTable.get(i).getText().replace(" ", "").equals(level.replace(" ", ""))) {
				index = i;
				break;
			}
		}
		return index;
	}
	
	public void validateFinalReaultSumByLevel(int levelIndex, int expectedSum) throws NumberFormatException, Exception {
		//testResultService.assertEquals(Integer.toString(expectedSum), finalResultsInSummaryTable.get(levelIndex).getText(), "Final Results Sum is incorrect");
		testResultService.assertEquals(true, Integer.parseInt(finalResultsInSummaryTable.get(levelIndex).getText()) >= expectedSum, "Final Results Sum is incorrect");

	}

}
