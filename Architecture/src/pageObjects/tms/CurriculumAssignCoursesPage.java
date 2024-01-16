package pageObjects.tms;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import drivers.GenericWebDriver;
import pageObjects.GenericPage;
import services.TestResultService;

public class CurriculumAssignCoursesPage extends GenericPage {
	
	@FindBy(xpath = "//tr[contains(@id,'Course')]//td[3]//input") 
	public List<WebElement> coursesCheckboxes;
	
	@FindBy(xpath = "//tr[contains(@id,'Course')]//td[5]//a") 
	public List<WebElement> coursesNames;
	
	@FindBy(xpath = "//tr[contains(@id,'Course')]//td[4]//img") 
	public List<WebElement> coursesOpenCloseButtons;
	
	@FindBy(xpath = "//tr[contains(@id,'Unit')]//td[5]//input") 
	public List<WebElement> unitsCheckboxes;
	
	@FindBy(xpath = "//tr[contains(@id,'Unit')]//td[7]//a") 
	public List<WebElement> unitsNames;
	
	@FindBy(xpath = "//tr[contains(@id,'Unit')]//td[6]//img") 
	public List<WebElement> unitsOpenCloseButtons;
	
	@FindBy(xpath = "//tr[contains(@id,'Com')]//td[8]//a") 
	public List<WebElement> lessonsNames;
	
	@FindBy(xpath = "//tr[contains(@id,'Com')]//td[7]//input") 
	public List<WebElement> lessonsCheckboxes;
	
	TmsHomePage tmsHomePage = new TmsHomePage(webDriver, testResultService);

	public CurriculumAssignCoursesPage(GenericWebDriver webDriver, TestResultService testResultService)
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

	public void markCheckBoxOfSpecificCourse(String courseName) throws InterruptedException {
		int index = getCourseIndexByName(courseName);
		webDriver.ClickElement(coursesCheckboxes.get(index));
		Thread.sleep(1000);
	}
	
	public int getCourseIndexByName(String courseName) {
		int index = 0;
		for (int i = 0; i < coursesNames.size(); i++) {
			if (coursesNames.get(i).getText().contains(courseName)) {
				index = i;
				break;
			}
		}
		return index;
	}
	
	public void openOrCloseSpecificCourse(String courseName) throws InterruptedException {
		int index = getCourseIndexByName(courseName);
		webDriver.ClickElement(coursesOpenCloseButtons.get(index));
		Thread.sleep(1000);
	}
	
	public void markCheckBoxOfSpecificUnit(String unitName) throws InterruptedException {
		int index = getUnitIndexByName(unitName);
		webDriver.ClickElement(unitsCheckboxes.get(index));
		Thread.sleep(1000);
	}
	
	public int getUnitIndexByName(String unitName) {
		int index = 0;
		for (int i = 0; i < unitsNames.size(); i++) {
			if (unitsNames.get(i).getText().contains(unitName)) {
				index = i;
				break;
			}
		}
		return index;
	}
	
	public void checkCourseIsMarked(String courseName) throws InterruptedException {
		int index = getCourseIndexByName(courseName);
		testResultService.assertEquals("true", coursesCheckboxes.get(index).getAttribute("checked"), "Checkbox of course '"+courseName+"' is not marked.");
		Thread.sleep(1000);
	}
	
	public void unmarkAllCourses() throws InterruptedException {
		for(WebElement el:coursesCheckboxes) {
			String statusCheckBox = el.getAttribute("checked");
			if(statusCheckBox!=null) {
				if(statusCheckBox.equalsIgnoreCase("true")) {
					el.click();
					Thread.sleep(1000);
			}
			}
		}
	}
	
	public boolean checkCourseIsNotMarked(String courseName) throws Exception {
		int index = getCourseIndexByName(courseName);
		testResultService.assertEquals(true, coursesCheckboxes.get(index).getAttribute("checked") == null, "Checkbox of course '"+courseName+"' is marked.");
		return "0".equals(coursesCheckboxes.get(index).getAttribute("value"));
	}
	
	public void checkUnitIsMarked(String unitName) throws InterruptedException {
		int index = getCourseIndexByName(unitName);
		testResultService.assertEquals("true", unitsCheckboxes.get(index).getAttribute("checked"), "Checkbox of unit '"+unitName+"' is not marked.");
		Thread.sleep(1000);
	}
	
	public void checkUnitIsNotMarked(String unitName) throws Exception {
		int index = getUnitIndexByName(unitName);
		testResultService.assertEquals(true, unitsCheckboxes.get(index).getAttribute("checked") == null, "Checkbox of unit '"+unitName+"' is not marked.");
	}
	
	public void openOrCloseSpecificUnit(String unitName) throws InterruptedException {
		int index = getUnitIndexByName(unitName);
		webDriver.ClickElement(unitsOpenCloseButtons.get(index));
		Thread.sleep(1000);
	}
	
	public void markCheckBoxOfSpecificLesson(String lessonName) throws InterruptedException {
		int index = getLessonIndexByName(lessonName);
		webDriver.ClickElement(lessonsCheckboxes.get(index));
		Thread.sleep(1000);
	}
	
	public int getLessonIndexByName(String lessonName) {
		int index = 0;
		for (int i = 0; i < lessonsNames.size(); i++) {
			if (lessonsNames.get(i).getText().contains(lessonName)) {
				index = i;
				break;
			}
		}
		return index;
	}
	
	public void checkLessonIsMarked(String lessonName) {
		int index = getLessonIndexByName(lessonName);
		testResultService.assertEquals("true", lessonsCheckboxes.get(index).getAttribute("checked"), "Checkbox of lesson '"+lessonName+"' is not marked.");
	}
	
	public void checkLessonIsNotMarked(String lessonName) throws Exception {
		int index = getLessonIndexByName(lessonName);
		testResultService.assertEquals(true, lessonsCheckboxes.get(index).getAttribute("checked") == null, "Checkbox of lesson '"+lessonName+"' is marked.");
	}
}
