package pageObjects.tms;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import drivers.GenericWebDriver;
import pageObjects.GenericPage;
import services.TestResultService;

public class CurriculumAssignPackagesPage extends GenericPage{
	
	
	@FindBy(xpath = "//*[@id='TblObj']/tr[3]/td[3]/a") 
	public WebElement firstTopPackageName;
	
	@FindBy(xpath = "//tr[contains(@id,'Course')]//td[5]//a") 
	public List<WebElement> coursesNames;
	
	@FindBy(xpath = "//*[@id='TblObj']/tr[3]/td[5]") 
	public WebElement startDateOfFirstTopPackage;
	
	
	

	public CurriculumAssignPackagesPage(GenericWebDriver webDriver, TestResultService testResultService)
			throws Exception {
		super(webDriver, testResultService);
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
	
	public String getNameOfFirstPackage() throws Exception {
		webDriver.waitForElementByWebElement(firstTopPackageName, "packageName", true, 5);
		return firstTopPackageName.getText();
	}
	
	public List<String[]> prepareListForCSVfile(String newClass, String packageName, String startPackageDate, String institutionId, boolean postEntrance) {
		
		List<String[]> list = new ArrayList<>();
		if(postEntrance) {
			list.add(new String[] {"InstitutionId","ClassName","PackageName","StartDate"});
			list.add(new String[] {institutionId,newClass,packageName,startPackageDate});
		}else {
			list.add(new String[] {"ClassName","PackageName","StartDate"});
			list.add(new String[] {newClass,packageName,startPackageDate});
		}
		return list;
	}
	
	public String getStartDateOfFirstTopPackage() throws Exception {
		webDriver.waitForElementByWebElement(startDateOfFirstTopPackage, "startDateOfFirstTopPackage", true, 5);
			return startDateOfFirstTopPackage.getText();
		
	}

	
}
