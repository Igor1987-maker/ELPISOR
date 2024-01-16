package pageObjects.tms;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Enums.ByTypes;
import drivers.GenericWebDriver;
import pageObjects.GenericPage;
import services.TestResultService;

public class RegistrationClassesPage extends GenericPage {

	@FindBy(xpath = "//*[contains(@id,'ClassName')]")
	public List<WebElement> classListName;

	@FindBy(xpath = "//*[@id='form1']/table/tbody/tr[4]/td[1]/input[1]") 
	public WebElement submitButtonInEditGroupPopup;
	
	@FindBy(xpath = "//input[@value='  Move  ']") 
	public WebElement moveButtonInMoveGroupPopup;
	
	TmsHomePage tmsHomePage = new TmsHomePage(webDriver, testResultService);

	public RegistrationClassesPage(GenericWebDriver webDriver, TestResultService testResultService) throws Exception {
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

	public void goToRegistrationClasses() throws Exception {
		report.startStep("Click on Registration");
		tmsHomePage.switchToMainFrame();
		tmsHomePage.clickOnRegistration();
		Thread.sleep(1000);
		
		report.startStep("Click on Classes");
		tmsHomePage.clickOnClasses();
		Thread.sleep(3000);
	}
	
	public void addNewGroupToSpecificClass(String className, String groupName) throws Exception {
		webDriver.waitForElement("//*[@id='ClassName" + className + "']", ByTypes.xpath).click();
		webDriver.waitForElement("//*[@id='Group" + className + "']", ByTypes.xpath).sendKeys(groupName);
		webDriver.waitForElement("//*[@id='TrBut" + className + "']//td//input[@type='button'][contains(@id,'but')]", ByTypes.xpath).click();
		Thread.sleep(2000);	
	}
	
	public String editGroup(String className, String newGroupName) throws Exception {
		List<WebElement> editGroupButtons = webDriver.getWebDriver().findElements(By.xpath("//*[@id='tbd" + className + "']//tr//td//a//img"));
		editGroupButtons.get(editGroupButtons.size()-1).click();
		webDriver.switchToPopup();
		tmsHomePage.switchToFormFrame();
		webDriver.waitForElement("Name", ByTypes.id).sendKeys("123");
		newGroupName = newGroupName + "123";
		webDriver.switchToTopMostFrame();
		webDriver.ClickElement(submitButtonInEditGroupPopup);
		webDriver.switchToMainWindow();
		tmsHomePage.switchToMainFrame();
		return newGroupName;
	}
	
	public void moveGroupToDifferentClass(String originalClass, String newClass) throws Exception {
		List<WebElement> groupCheckboxes = webDriver.getWebDriver().findElements(By.xpath("//*[@id='tbd" + originalClass + "']//tr//td//input[@type='checkbox']"));
		groupCheckboxes.get(groupCheckboxes.size()-1).click();
		tmsHomePage.clickOnPromotionAreaMenuButton("Move");
		webDriver.switchToNewWindow();
		webDriver.switchToFrame("DataFrame");
		tmsHomePage.selectClassToMoveGroup(newClass);
		Thread.sleep(1000);
		webDriver.switchToMainWindow();
		webDriver.switchToNewWindow();
		webDriver.ClickElement(moveButtonInMoveGroupPopup);

		if (webDriver.getAlertText(1) != null);
			webDriver.closeAlertByAccept();

		webDriver.switchToMainWindow();
		tmsHomePage.switchToMainFrame();
		Thread.sleep(2000);
	}
	
	public boolean validateGroupIsDisplayedInClass(String className, String groupName) throws Exception {
		List<WebElement> groupNamesOfSpecificClass = webDriver.getWebDriver().findElements(By.xpath("//*[@id='tbd" + className + "']//tr//td[contains(@id,'GroupName')]"));
		boolean isDisplayed = false;
		for (int i = 0; i < groupNamesOfSpecificClass.size(); i++) {
			if (groupNamesOfSpecificClass.get(i).getText().equals(groupName)) {
				isDisplayed = true;
				break;
			}
		}
		
		testResultService.assertEquals(true,  isDisplayed, "Group '"+groupName+"' is not displayed in Class '"+className+"'");
		return isDisplayed;
	}
	
	public boolean validateGroupIsNotDisplayedInClass(String className, String groupName) throws Exception {
		List<WebElement> groupNamesOfSpecificClass = webDriver.getWebDriver().findElements(By.xpath("//*[@id='tbd" + className + "']//tr//td[contains(@id,'GroupName')]"));
		boolean isDisplayed = false;
		for (int i = 0; i < groupNamesOfSpecificClass.size(); i++) {
			if (groupNamesOfSpecificClass.get(i).getText().equals(groupName)) {
				isDisplayed = true;
				break;
			}
		}
		
		testResultService.assertEquals(false,  isDisplayed, "Group '"+groupName+"' is not displayed in Class '"+className+"'");
		return !isDisplayed;
	}
	
	public void deleteGroup(String className, String groupName) throws Exception {
		int groupIndex = getGroupIndexByNameInSpecificClass(className, groupName);
		List<WebElement> groupCheckboxes = webDriver.getWebDriver().findElements(By.xpath("//*[@id='tbd" + className + "']//tr//td//input[@type='checkbox']"));
		groupCheckboxes.get(groupIndex).click();
		tmsHomePage.clickOnPromotionAreaMenuButton("Delete");
		webDriver.closeAlertByAccept();
	}
	
	public int getGroupIndexByNameInSpecificClass(String className, String groupName) {
		List<WebElement> groupNamesOfSpecificClass = webDriver.getWebDriver().findElements(By.xpath("//*[@id='tbd" + className + "']//tr//td[contains(@id,'GroupName')]"));
		int index = 0;
		for (int i = 0; i <groupNamesOfSpecificClass.size(); i++) {
			if (groupNamesOfSpecificClass.get(i).getText().equals(groupName)) {
				index = i;
				break;
			}
		}
		return index;
	}
	
	public void addClass(String className) throws Exception {
		tmsHomePage.enterClassName(className);
		tmsHomePage.clickOnAddClass();	
	}
	
	public String validateClassDetailsAndEdit(String className) throws Exception {
		report.startStep("Open Class information card");	
		WebElement element = tmsHomePage.getLastInfoNode();
		element.click();
		Thread.sleep(2000);
		
	report.startStep("Verify class name match");
		webDriver.switchToPopup();
		Thread.sleep(3000);
		webDriver.switchToFrame("FormFrame");
		//Thread.sleep(3000);
		tmsHomePage.verifyInfoCardDetails("Name",className);
	//	Thread.sleep(1000);
		
	report.startStep("Edit Class Name");
		webDriver.waitForElement("Name", ByTypes.name).sendKeys("77");
		String newClassName = className + "77";
		
	report.startStep("Close Class info card");	
		webDriver.switchToMainWindow();
		webDriver.switchToPopup();
		webDriver.waitForElement("Submit2", ByTypes.name).click();
		webDriver.switchToMainWindow();	
		tmsHomePage.switchToMainFrame();
		return newClassName;
	}
	
	public void deleteClass(String classId) throws Exception {
		tmsHomePage.markClassForDelete(classId);
		tmsHomePage.clickOnDelete();
		webDriver.closeAlertByAccept();
	}
}
