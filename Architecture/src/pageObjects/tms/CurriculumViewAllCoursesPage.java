package pageObjects.tms;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Enums.ByTypes;
import drivers.GenericWebDriver;
import pageObjects.GenericPage;
import services.TestResultService;

public class CurriculumViewAllCoursesPage extends GenericPage {
	
	@FindBy(xpath = "//tr/td[5]/a/img") 
	public WebElement eyeSign;
	
	@FindBy(xpath = "//tr[contains(@id,'Course')]/td[3]/a/img") 
	public List<WebElement> coursesInfoSign;
	
	@FindBy(xpath = "//tr[contains(@id,'Course')]/td[5]") 
	public List<WebElement> coursesNames;
	
	@FindBy(xpath = "//tr[contains(@id,'Unit')]/td[4]/a/img") 
	public List<WebElement> unitsInfoSign;
	
	@FindBy(xpath = "//tr[contains(@id,'Unit')]/td[6]") 
	public List<WebElement> unitsNames;
	
	@FindBy(xpath = "//tr[contains(@id,'Com')]/td[6]/a/img") 
	public List<WebElement> lessonsInfoSign;
	
	@FindBy(xpath = "//tr[contains(@id,'Com')]/td[7]") 
	public List<WebElement> lessonsNames;
	
	@FindBy(xpath = "/html/body/table[1]/tbody/tr/td[1]/table/tbody/tr[1]/td[3]/input") // //td//input[@class='stextarea']
	public WebElement nameInPopup; 
	
	@FindBy(xpath = "//textarea") 
	public WebElement descriptionInPopup;
	
	@FindBy(className = "okButton") 
	public WebElement closeButtonInPopup;
	
	TmsHomePage tmsHomePage = new TmsHomePage(webDriver, testResultService);

	public CurriculumViewAllCoursesPage(GenericWebDriver webDriver, TestResultService testResultService)
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

	public void goToCurriculumViewAllCourses() throws Exception {
		tmsHomePage.switchToMainFrame();
		tmsHomePage.clickOnCurriculum();
		Thread.sleep(2000);
		tmsHomePage.clickOnViewAllCourses();
		Thread.sleep(7000);
	}
	
	public void expandListItemAndClick(String type, String nodeId) throws Exception {
		WebElement element = webDriver.waitForElement("//*/img[contains("+ type +"," + nodeId + ")]", ByTypes.xpath, false, 1);
		element.click();
		Thread.sleep(2000); // Increased sleep due to elements loading later than expected.
	}
	
	public void checkEyeSignIsDisplayed() throws Exception {
		//webDriver.checkElementNotExist("//*[@id='Com_60']/td[5]/a/img", "Preview icon still here");
		testResultService.assertEquals(true, webDriver.isDisplayed(eyeSign),"Eye Sign is Not Displayed.");
	}
	
	public void checkEyeSignIsNotDisplayed() throws Exception {
		testResultService.assertEquals(false, webDriver.isDisplayed(eyeSign),"Eye Sign is Not Displayed.");
	}
	
	public void clickEyeSign() {
		webDriver.ClickElement(eyeSign);
	}
	
	public void checkNameAndDescriptionIsNotNull(String name) throws Exception {
		webDriver.switchToPopup();
		webDriver.switchToFrame("FormFrame");
		testResultService.assertEquals(true, descriptionInPopup.getText()!=null, "Description is null");	
		testResultService.assertEquals(true, nameInPopup.getAttribute("value").contains(name), "Name is Incorrect. Expected: "+name+". Actual:"+nameInPopup.getAttribute("value"));
		Thread.sleep(2000);
		webDriver.closeNewTab(2);
		webDriver.switchToMainWindow();
		tmsHomePage.switchToMainFrame();
		Thread.sleep(2000);
	}
	
	public void openCourseInfoAndCheckDescription(String courseName) throws Exception {
		int courseIndex = getCourseIndexByName(courseName);
		clickCourseInfoSignAtIndex(courseIndex);
		checkNameAndDescriptionIsNotNull(courseName);
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
	
	public void clickCourseInfoSignAtIndex(int courseIndex) {
		webDriver.ClickElement(coursesInfoSign.get(courseIndex));
	}
	
	public void openUnitInfoAndCheckDescription(String unitName) throws Exception {
		int unitIndex = getUnitIndexByName(unitName);
		clickUnitInfoSignAtIndex(unitIndex);
		checkNameAndDescriptionIsNotNull(unitName);
	}
	
	public int getUnitIndexByName(String unitName) {
		int index = 0;
		for (int i = 0; i < unitsNames.size(); i++) {
			if (unitsNames.get(i).getText().equals(unitName)) {
				index = i;
				break;
			}
		}
		return index;
	}
	
	public void clickUnitInfoSignAtIndex(int courseIndex) {
		webDriver.ClickElement(unitsInfoSign.get(courseIndex));
	}
	
	public void openLessonInfoAndCheckDescription(String lessonName) throws Exception {
		int unitIndex = getLessonIndexByName(lessonName);
		clickLessonInfoSignAtIndex(unitIndex);
		checkNameAndDescriptionIsNotNull(lessonName);
	}
	
	public int getLessonIndexByName(String lessonName) {
		int index = 0;
		for (int i = 0; i < lessonsNames.size(); i++) {
			if (lessonsNames.get(i).getText().equals(lessonName)) {
				index = i;
				break;
			}
		}
		return index;
	}
	
	public void clickLessonInfoSignAtIndex(int courseIndex) {
		webDriver.ClickElement(lessonsInfoSign.get(courseIndex));
	}
}
