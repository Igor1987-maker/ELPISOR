package pageObjects.tms;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import Enums.ByTypes;
import drivers.GenericWebDriver;
import pageObjects.GenericPage;
import services.TestResultService;

public class TmsCreateNewCoursePage extends GenericPage {

	String mainWin=null;
	
	@FindBy(id="versionSel")
	public WebElement versionDropdown;	
	
	@FindBy(name="courseName")
	public WebElement courseNameInput;
	
	@FindBy(how = How.NAME, using = "basedCourse")
	public List<WebElement> basedOnRadioButtons;
	
	@FindBy(id="coursesSel")
	public WebElement coursesDropdown;
	
	@FindBy(xpath="//input[contains(@value,'OK')]")
	public WebElement buttonOK;
	
	@FindBy(xpath="//input[contains(@value,'Cancel')]")
	public WebElement buttonCANCEL;
	
	@FindBy(id="selectComp1_1")
	public WebElement selectComponent;
	
	@FindBy(id="selectUnit1_1")
	public WebElement selectUnit;
	
	@FindBy(id="selectSkill1_1")
	public WebElement selectSkill; 
	
	@FindBy(id="selectLevel1_1")
	public WebElement selectLevel; 
	
	public TmsCreateNewCoursePage(GenericWebDriver webDriver,TestResultService testResultService) throws Exception {
		super(webDriver ,testResultService);
		PageFactory.initElements(webDriver.getWebDriver(), this);
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
	
	
	
	public void selectVersion (String version) throws Exception{
		
		webDriver.selectElementFromComboBox(versionDropdown.getAttribute("name"), version, true);
		
	}
	
	public void verifyCourseDoesNotExistInDropDown (String courseName) throws Exception{
		
		Select select = new Select(webDriver.waitForElement(coursesDropdown.getAttribute("name"), ByTypes.id));
		List<WebElement> options = select.getOptions();
		
		for (int i = 0; i < options.size(); i++) {
			if (options.get(i).getText().equals(courseName)){
				testResultService.addFailTest("Course still exists in dropdown list", false, true);
			}
		}
		
	}
		
	public void enterNewCourseName (String name) throws Exception{
		
		courseNameInput.sendKeys(name);
		
	}
	
	public void selectNoBasedOption () throws Exception{
		
		basedOnRadioButtons.get(0).click();
		
	}
	
	public void selectBasedOnOption () throws Exception{
		
		basedOnRadioButtons.get(1).click();
		
	}
	
	public void selectCourseToBase (String courseName) throws Exception{
		
		webDriver.selectElementFromComboBox(coursesDropdown.getAttribute("name"), courseName, true);
		
	}
	
	public void clickOnOK() throws Exception{
				
		buttonOK.click();
	}
	
	public void clickOnCancel() throws Exception{
		
		buttonCANCEL.click();
	}

	public List<String> retrieveAllComponents() {
		List<WebElement> componentList = selectComponent.findElements(By.cssSelector("option")); //select[@id='selectComp1_1']/option
		List<String> components = componentList.stream().map(e->e.getText().toString()).collect(Collectors.toList());
		
		return components;
	}

	public void selectUnit() {
		new Select(selectUnit).selectByVisibleText("At The Restaurant");
		
	}

	public void selectSkill() {
		new Select(selectSkill).selectByVisibleText("Grammar");
		
	}

	public String nameOfDisplayedUnit() {
		String unit = selectUnit.findElement(By.cssSelector("option")).getText();
		return unit;
	}

	public String nameOfDisplayedSkill() {
		String skill = selectSkill.findElement(By.cssSelector("option")).getText();
		return skill;
	}

	public boolean verifyListOfComponentsByUnitDisplayedCorrect(List<String> componentsByUnit) throws Exception {
	
		
		//dbService.getUnitIdByNameAndCourse("", mainWin);
		//dbService.getUnitItems(mainWin);
		List<String[]> dBcomponents = dbService.getComponentDetailsByUnitId("43412");
		List<String> dBCom = dBcomponents.stream().map(s->s[0]).collect(Collectors.toList());
		textService.assertEquals("Sizes doesn't matches",componentsByUnit.size()-1, dBCom.size());
		int t = 0;
			if((componentsByUnit.size()-1)!=dBCom.size()){
				return false; 
			}
		
		boolean unitMatch = true;
		boolean foundComp = false;
				
		for(int i = 0;i<dBCom.size();i++) {
			String oneComponent = dBCom.get(i);
			for(int j = 1;j<componentsByUnit.size();j++) {
				foundComp = componentsByUnit.get(j).contains(oneComponent);
				if(foundComp==true)
					break;
			}
			if(foundComp==false) {
				unitMatch = false;
			}
		}
		return unitMatch;
	}

	public void slectLevel() {
		new Select(selectLevel).selectByVisibleText("Basic 1");
	}

	public void selectComponent() {
		new Select(selectComponent).selectByVisibleText("Food");
	}

}
