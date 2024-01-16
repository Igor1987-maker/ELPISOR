package pageObjects.tms;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Enums.ByTypes;
import drivers.GenericWebDriver;
import pageObjects.GenericPage;
import services.TestResultService;

public class TmsCreatePackageIntitutionPage extends GenericPage {
	
	@FindBy(xpath="//button[contains(@name,'go')]")
	public WebElement goButton;
	
	TmsHomePage tmsHomePage = new TmsHomePage(webDriver, testResultService);

	public TmsCreatePackageIntitutionPage(GenericWebDriver webDriver, TestResultService testResultService)
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

	public void addNewPackage(String packageName) throws Exception {
		report.startStep("Select custom componnent package and submit");
		webDriver.selectElementFromComboBox("selectPeriod", ByTypes.name, "12 months");	
		webDriver.selectElementFromComboBox("selectLevel", ByTypes.name, "Custom Components");
		webDriver.ClickElement(goButton);
		Thread.sleep(1000);
		assignPackageToInstitution(packageName);
	}

	private void assignPackageToInstitution(String targetPackage) throws Exception{
		tmsHomePage.switchToFormFrame();
		List<WebElement> chkBtns  = webDriver.getElementsByXpath("//*[@id='TblObj']/tr/td[3]/a");
		for (int i = 0 ; i < chkBtns.size() ; i++)
		{
			String packageName = chkBtns.get(i).getText(); 
			if (packageName.equals(targetPackage))
			{
				i+=2;
				webDriver.waitForElement("//*[@id='TblObj']/tr[" + i + "]/td[1]", ByTypes.xpath).click();
				webDriver.waitForElement("//*[@id='TblObj']/tr[" + i + "]/td[11]", ByTypes.xpath).click();
				webDriver.switchToPopup();
				webDriver.waitForElement("//a[@class='todaylink']", ByTypes.xpath).click();
				webDriver.switchToMainWindow();
				webDriver.switchToNewWindow();
				tmsHomePage.switchToFormFrame();
				webDriver.waitForElement("//*[@id='TblObj']/tr[" + i + "]/td[13]/input", ByTypes.xpath).sendKeys("1000");
				break;
			}
		}
		webDriver.switchToTopMostFrame();
		webDriver.waitForElementAndClick("//input[contains(@value,'  Submit  ')]", ByTypes.xpath);
		webDriver.switchToMainWindow();
	}
}
