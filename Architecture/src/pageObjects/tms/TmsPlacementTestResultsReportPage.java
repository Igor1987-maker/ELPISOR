package pageObjects.tms;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import drivers.GenericWebDriver;
import pageObjects.GenericPage;
import services.TestResultService;

public class TmsPlacementTestResultsReportPage extends GenericPage{
	
	@FindBy(xpath = "//tbody[@name='tblBody']//tr//td[1]")
	public List<WebElement> studentsNamesTable;
	
	@FindBy(xpath = "//tbody[@name='tblBody']//tr//td[3]")
	public List<WebElement> readingLevelsTable;
	
	@FindBy(xpath = "//tbody[@name='tblBody']//tr//td[4]")
	public List<WebElement> listeningLevelsTable;
	
	@FindBy(xpath = "//tbody[@name='tblBody']//tr//td[5]")
	public List<WebElement> grammarLevelsTable;
	
	@FindBy(xpath = "//tbody[@name='tblBody']//tr//td[6]")
	public List<WebElement> finalLevelsTable;
	
	TmsHomePage tmsHomePage = new TmsHomePage(webDriver, testResultService);
	TmsAssessmentsTestsAssignmentPage tmsAssessmentsTestsAssignmentPage = new TmsAssessmentsTestsAssignmentPage(webDriver, testResultService);

	public TmsPlacementTestResultsReportPage(GenericWebDriver webDriver, TestResultService testResultService)
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
	
	public int getIndexOfStudent(String studentName) throws Exception {
		int index = 0;
		int totalPageNum = tmsAssessmentsTestsAssignmentPage.getNumberOfPagesInTable();
	
		boolean userFound = false;
		
		for (int j = 0; j < totalPageNum; j++) {
			
			webDriver.switchToFrame("tableFrame");
			
			if (studentsNamesTable.size() == 0) {
				testResultService.addFailTest("Students Table is not Displayed.", true, true);
			}
	
			for (int i = 0; i < studentsNamesTable.size(); i++) {
				if (studentsNamesTable.get(i).getAttribute("title").contains(studentName)) {
					index = i;
					userFound = true;
					break;
				}
			}
			
			if (userFound) {
				webDriver.switchToTopMostFrame();
				tmsHomePage.switchToMainFrame();
				break;
			} 
			
			webDriver.switchToTopMostFrame();
			tmsHomePage.switchToMainFrame();
			webDriver.ClickElement(tmsAssessmentsTestsAssignmentPage.nextPageInTableButton);
		}
		
		return index;
	}
	
	public void validateLevelsAreCorrect(String[] expectedLevels, int studentIndex) throws Exception {
		webDriver.switchToFrame("tableFrame");
		testResultService.assertEquals(expectedLevels[0], readingLevelsTable.get(studentIndex).getText().replace(" ",""),"Reading level in TMS is not compatible with level in DB");
		testResultService.assertEquals(expectedLevels[1], listeningLevelsTable.get(studentIndex).getText().replace(" ",""),"Listening level in TMS is not compatible with level in DB");
		testResultService.assertEquals(expectedLevels[2], grammarLevelsTable.get(studentIndex).getText().replace(" ",""),"Grammar level in TMS is not compatible with level in DB");
		testResultService.assertEquals(expectedLevels[3], finalLevelsTable.get(studentIndex).getText().replace(" ",""),"Final level in TMS is not compatible with level in DB");
		webDriver.switchToTopMostFrame();
		tmsHomePage.switchToMainFrame();
	}

}
