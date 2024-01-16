package pageObjects.tms;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import drivers.GenericWebDriver;
import pageObjects.GenericPage;
import services.TestResultService;
import services.PageHelperService;


import static tests.edo.newux.BasicNewUxTest.institutionsName;

public class TmsCreateInstitutionPage extends GenericPage {

	@FindBy(id = "SchoolName")
	public WebElement schoolNameField;

	@FindBy(xpath = "//input[contains(@name, 'Phone')]")
	public WebElement phoneField;

	@FindBy(id = "Dname")
	public WebElement hostField;

	@FindBy(id = "NumOfCustomComp")
	public WebElement numOfCustomCompField;

	@FindBy(id = "UsersLimitation")
	public WebElement usersLimitationCheckBox;

	@FindBy(xpath = "//a[contains(text(), 'Administrator')]")
	public WebElement administratorTab;

	@FindBy(xpath = "//input[contains(@name,'FirstName')]")
	public WebElement adminFirstName;

	@FindBy(xpath = "//input[contains(@name,'LastName')]")
	public WebElement adminLastName;

	@FindBy(xpath = "//input[contains(@name,'UserName')]")
	public WebElement adminUserName;

	@FindBy(xpath = "//input[contains(@name,'Password')]")
	public WebElement adminPassword;

	@FindBy(xpath = "//input[contains(@name,'Email')]")
	public WebElement adminEmail;

	@FindBy(id = "Submitbutton")
	public WebElement submitButton;

	TmsHomePage tmsHomePage = new TmsHomePage(webDriver, testResultService);

	public TmsCreateInstitutionPage(GenericWebDriver webDriver, TestResultService testResultService) throws Exception {
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

	public void createNewInstitution(String institutionName, String phoneNum, String numOfCustomComp,
									 String adminName, String adminpassword, String adminemail) throws Exception {

		webDriver.switchToNewWindow();
		Thread.sleep(3000);

		report.startStep("Fill New Institution Form");
		webDriver.switchToFrame("FormFrame");
		webDriver.SendKeys(schoolNameField, institutionName);
		webDriver.SendKeys(phoneField, phoneNum);

		String[] list = PageHelperService.linkED.split("/");
		String instName = list[3];
		String cannonicalDomain = PageHelperService.linkED.replace(instName,institutionName);

		webDriver.SendKeys(hostField, cannonicalDomain);
		webDriver.SendKeys(numOfCustomCompField, numOfCustomComp);
		webDriver.ClickElement(usersLimitationCheckBox);
		webDriver.selectElementFromComboBox("impType", "Blended");

		report.startStep("Switch To And Fill Admin tab");
		webDriver.switchToTopMostFrame();
		webDriver.ClickElement(administratorTab);
		webDriver.switchToFrame("FormFrame");
		webDriver.SendKeys(adminFirstName, adminName);
		webDriver.SendKeys(adminLastName, adminName);
		webDriver.SendKeys(adminUserName, adminName);
		webDriver.SendKeys(adminPassword, adminpassword);
		webDriver.SendKeys(adminEmail, adminemail);
		webDriver.switchToTopMostFrame();
		webDriver.ClickElement(submitButton);
		webDriver.switchToMainWindow();
		tmsHomePage.switchToMainFrame();
	}

	public String editSchoolName(String institutionName) throws Exception {
		webDriver.switchToNewWindow();
		Thread.sleep(1000);
		webDriver.switchToFrame("FormFrame");
		schoolNameField.clear();
		String newInstitutionName = institutionName + dbService.sig(2);
		webDriver.SendKeys(schoolNameField, newInstitutionName);
		webDriver.switchToTopMostFrame();
		webDriver.ClickElement(submitButton);
		Thread.sleep(2000);
		webDriver.switchToMainWindow();
		tmsHomePage.switchToMainFrame();
		return newInstitutionName;
	}



	public void switchToInfoPage() throws Exception {
		webDriver.switchToNewWindow();
		Thread.sleep(1000);
		webDriver.switchToFrame("FormFrame");
	}

	public void submitAndSwitchToMainPage() throws Exception {

		webDriver.switchToTopMostFrame();
		webDriver.ClickElement(submitButton);
		Thread.sleep(2000);
		webDriver.switchToMainWindow();
		tmsHomePage.switchToMainFrame();

	}

}
