package pageObjects.tms;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import drivers.GenericWebDriver;
import pageObjects.GenericPage;
import services.TestResultService;

public class CurriculumCourseSequencePage extends GenericPage {
	
	@FindBy(xpath = "//tr[contains(@id,'Course')]//td[4]") 
	public List<WebElement> coursesNames;
	
	@FindBy(xpath = "//tr[contains(@id,'Course')]//td[3]//input") 
	public List<WebElement> coursesCheckBoxes;
	
	TmsHomePage tmsHomePage = new TmsHomePage(webDriver, testResultService);

	public CurriculumCourseSequencePage(GenericWebDriver webDriver, TestResultService testResultService)
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

	public void goToCurriculumCourseSequence() throws Exception {
		tmsHomePage.switchToMainFrame();
		tmsHomePage.clickOnCurriculum();
		Thread.sleep(2000);
		tmsHomePage.clickOnCourseSequence();
		Thread.sleep(2000);
	}
	
	public void changeSequenceOfSpecificCourse(String courseToMoveName, String courseToBeAfterName) throws Exception {
		webDriver.switchToTopMostFrame();
	 	tmsHomePage.switchToMainFrame();
		
		// get the index of course to be after
		int courseToBeAfterIndex = getCourseIndexByName(courseToBeAfterName);
		
		// get the index of course to move
		int courseToMoveIndex = getCourseIndexByName(courseToMoveName);
		
		// click course to move
		selectCourseByIndex(courseToMoveIndex);
		
		// retrieve difference and click down this num of times
		String direction = "";
		int adjustment = 0;
		if (courseToBeAfterIndex > courseToMoveIndex) {
			direction = "Down";
		} else {
			direction = "Up";
			adjustment = -1;
		}
		int difference = Math.abs(courseToBeAfterIndex - courseToMoveIndex);
		for (int i = 0; i < difference+adjustment; i++) {
			tmsHomePage.clickOnPromotionAreaMenuButton(direction);
		}
		
		// save
		tmsHomePage.clickOnPromotionAreaMenuButton("Save");
	}
	
	public int getCourseIndexByName(String courseName) {
		int index = 0;
		for (int i = 0; i < coursesNames.size(); i++) {
			if (coursesNames.get(i).getText().equals(courseName)) {
				index = i;
				break;
			}
		}
		return index;
	}
	
	public void selectCourseByIndex(int index) {
		webDriver.ClickElement(coursesCheckBoxes.get(index));
	}
}
