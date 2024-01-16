package pageObjects.tms;

import Enums.LessMoreEquals;
import drivers.GenericWebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import pageObjects.GenericPage;
import services.TestResultService;

import java.util.List;

public class RegistrationInstitutionsPage extends GenericPage {
	
	@FindBy(xpath="//input[contains(@value,'Add New School')]")
	public WebElement addNewSchoolButton;
	
	@FindBy(xpath="//*[@id='con']/tr/td[3]")
	public List<WebElement> schoolNames;
	
	@FindBy(xpath="//*[@id='con']/tr/td/a/img")
	public List<WebElement> schoolInfoSigns;
	
	@FindBy(xpath="//*[@id='con']/tr/td[2]")
	public List<WebElement> schoolCheckBoxes;
	
	@FindBy(xpath="//a[contains(text(),'Institution Packages')]")
	public WebElement institutionsPackagesTab;
	
	@FindBy(xpath="//input[contains(@value,'  GO  ')]")
	public WebElement institutionsPackagesGoButton;
	
	@FindBy(xpath="//input[contains(@value,'Add Packages')]")
	public WebElement addPackageButton;
	
	@FindBy(xpath="//*[@id='con']/tr/td[3]")
	public List<WebElement> packagesNames;
	
	@FindBy(xpath="//*[@id='con']/tr/td[2]/input")
	public List<WebElement> packagesCheckBoxes;

	@FindBy(xpath="//*[@id='con']/tr/td[7]")
	public List<WebElement> packagesUsedLicense;

	@FindBy(id = "ToeicLrPLC")
	private WebElement toeicLrPLC;

	@FindBy(id = "ToeicBridgeLrPLC")
	private WebElement toeicBridgeLrPLC;

	@FindBy(id = "EdPlacementPLC")
	private WebElement edPlacementPLC;

	public String[] expectedTests = {"PlacementTest", "TOEICBridgePlacementTest", "TOEICPlacementTest"};

	TmsHomePage tmsHomePage = new TmsHomePage(webDriver, testResultService);
	TmsCreateInstitutionPage tmsCreateInstitutionPage = new TmsCreateInstitutionPage(webDriver, testResultService);

	public RegistrationInstitutionsPage(GenericWebDriver webDriver, TestResultService testResultService)
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
	
	public void goToInstitutions() throws Exception {
		tmsHomePage.switchToMainFrame();
		tmsHomePage.clickOnRegistration();
		tmsHomePage.clickOnInstitutions();
		Thread.sleep(2000);
	}

	public void addNewInstitution(String institutionName) throws Exception {
		report.startStep("Open new Institution Window");
		webDriver.ClickElement(addNewSchoolButton);
		String adminName = "admin" + dbService.sig(3);
		tmsCreateInstitutionPage.createNewInstitution(institutionName, "0123456789", "1000", adminName, "12345", "a@a.com");
	}
	
	public boolean verifyInstitutionIsDisplayed(String institutionName) throws Exception {
		boolean isDisplayed = false;
		for (int i = 0; i < schoolNames.size(); i++) {
			if (schoolNames.get(i).getText().equals(institutionName)) {
				isDisplayed = true;
				break;
			}
		}
		testResultService.assertEquals(true, isDisplayed, "The Institution: '"+institutionName+"' is Not Displayed in Institutions List.");
		return isDisplayed;
	}
	
	public boolean verifyInstitutionIsNotDisplayed(String institutionName) throws Exception {
		boolean isDisplayed = false;
		//for (int i = 0; i < schoolNames.size(); i++) {
		for (int i = 0; i < 10; i++) {
			if (schoolNames.get(i).getText().equals(institutionName)) {
				isDisplayed = true;
				break;
			}
		}
		testResultService.assertEquals(false, isDisplayed, "The Institution: '"+institutionName+"' is Not Displayed in Institutions List.");
		return !isDisplayed;
	}
	
	public String editInstitution(String institutionName) throws Exception {
		int instIndex = getInstitutionIndexByName(institutionName);
		webDriver.ClickElement(schoolInfoSigns.get(instIndex));
		return tmsCreateInstitutionPage.editSchoolName(institutionName);
	}

	public void clickInfoInstitution(String institutionName) throws Exception {
		int instIndex = findInstitutionNyName(institutionName);
		webDriver.ClickElement(schoolInfoSigns.get(instIndex));

	}



	public Integer checkIfEquals(int minIndex, int maxIndex, String InstituteName) {
		while (minIndex <= maxIndex) {
			int middleIndex = (minIndex + maxIndex) / 2;
			LessMoreEquals check = isMoreLessOrEquals(InstituteName, schoolNames.get(middleIndex).getText());
			switch (check) {
				case EQUALS:
					return middleIndex;
				case LESS:
					return checkIfEquals(minIndex, middleIndex - 1, InstituteName);
				case MORE:
					return checkIfEquals(middleIndex + 1, maxIndex, InstituteName);
			}

		}
		return null;
	}


	public LessMoreEquals isMoreLessOrEquals(String expectedName, String actualName) {
		int result = expectedName.compareToIgnoreCase(actualName);

		if (result < 0) {
			return LessMoreEquals.LESS;
		} else if (result > 0) {
			return LessMoreEquals.MORE;
		} else {
			return LessMoreEquals.EQUALS;
		}

	}

	public int findInstitutionNyName(String instName){
		return checkIfEquals(0,schoolNames.size(),instName);
	}

	
	public int getInstitutionIndexByName(String instName) {
		for (int i = 0; i < schoolNames.size(); i++) {
			if (schoolNames.get(i).getText().equals(instName)) {
				return i;

			}
		}
		return 0;


}

	public boolean areToeikCheckboxesPresent() throws Exception {
		tmsCreateInstitutionPage.switchToInfoPage();

		return toeicLrPLC.isDisplayed() && toeicBridgeLrPLC.isDisplayed() && edPlacementPLC.isDisplayed();
	}

	public void selectAllCheckboxesIfTheyAreNotSelected() throws Exception {
		if (!toeicLrPLC.isSelected()) {
			toeicLrPLC.click();
		}
		if (!toeicBridgeLrPLC.isSelected()) {
			toeicBridgeLrPLC.click();
		}
		if (!edPlacementPLC.isSelected()) {
			edPlacementPLC.click();
		}
		tmsCreateInstitutionPage.submitAndSwitchToMainPage();
	}

	public void unSelectToeicLrPLCCheckbox() throws Exception {
		if (toeicLrPLC.isSelected()) {
			toeicLrPLC.click();
		}

		tmsCreateInstitutionPage.submitAndSwitchToMainPage();
	}

	public boolean areAllCheckboxesSelected() throws Exception {
		tmsCreateInstitutionPage.switchToInfoPage();
		return toeicLrPLC.isSelected() && toeicBridgeLrPLC.isSelected() && edPlacementPLC.isSelected();
	}

	public boolean isLrPLCCheckboxUnSelected() throws Exception {
		tmsCreateInstitutionPage.switchToInfoPage();
		return !toeicLrPLC.isSelected() && toeicBridgeLrPLC.isSelected() && edPlacementPLC.isSelected();
	}

	
	public void deleteIntitution(String instName) throws Exception {
		int instIndex = getInstitutionIndexByName(instName);
		webDriver.ClickElement(schoolCheckBoxes.get(instIndex));
		tmsHomePage.clickOnPromotionAreaMenuButton("Delete");
		webDriver.closeAlertByAccept();
	}
	
	public void clickOnPackages() {
		webDriver.ClickElement(institutionsPackagesTab);
	}
	
	public void clickOnPackagesGoButton() {
		webDriver.ClickElement(institutionsPackagesGoButton);
	}
	
	public void addPackage(String packageName) throws Exception {
		webDriver.ClickElement(addPackageButton);
		webDriver.switchToNewWindow();
		Thread.sleep(1000);
		TmsCreatePackageIntitutionPage tmsCreatePackageIntitutionPage = new TmsCreatePackageIntitutionPage(webDriver, testResultService);
		tmsCreatePackageIntitutionPage.addNewPackage(packageName);
		tmsHomePage.switchToMainFrame();
	}
	
	public boolean verifyPackageIsDisplayed(String expectedName) throws Exception {
		boolean isDisplayed = false;
		for (int i = 0; i < packagesNames.size(); i++) {
			if (packagesNames.get(i).getText().equals(expectedName)) {
				isDisplayed = true;
				break;
			}
		}
		testResultService.assertEquals(true, isDisplayed, "Package '"+expectedName+"' is not displayed in Packages List");
		return isDisplayed;
	}
	
	public boolean verifyPackageIsNotDisplayed(String expectedName) throws Exception {
		boolean isDisplayed = false;
		for (int i = 0; i < packagesNames.size(); i++) {
			if (packagesNames.get(i).getText().equals(expectedName)) {
				isDisplayed = true;
				break;
			}
		}
		testResultService.assertEquals(false, isDisplayed, "Package '"+expectedName+"' is displayed in Packages List");
		return !isDisplayed;
	}
	
	public int getPackageIndexByName(String packageName) {
		int index = 0;
		for (int i = 0; i < packagesNames.size(); i++) {
			if (packagesNames.get(i).getText().equals(packageName)) {
				index = i;
				break;
			}
		}
		return index;
	}
	
	public void deletePackage(String packageName) throws Exception {
		int index = getPackageIndexByName(packageName);
		webDriver.ClickElement(packagesCheckBoxes.get(index));
		tmsHomePage.clickOnPromotionAreaMenuButton("Delete");
		webDriver.closeAlertByAccept();
	}
	
	public void selectLocalInstitution(String instName) throws Exception {
		tmsHomePage.switchToFormFrame();
		webDriver.selectValueFromComboBox("SelectSchool", instName);
		Thread.sleep(1000);
		clickOnPackagesGoButton();
		Thread.sleep(1000);
		webDriver.switchToTopMostFrame();
		tmsHomePage.switchToMainFrame();
	}

	public void validateUsedLicenseOfPackage(String[] packageDetails) throws Exception {

		for (int i = 0;i<packagesNames.size();i++){
			if(packagesNames.get(i).getText().equalsIgnoreCase(packageDetails[1])){
				testResultService.assertEquals(true,
						packagesUsedLicense.get(i).getText().equalsIgnoreCase(packageDetails[0]));
				break;
			}
		}


		//packageDetails[1];  -> name


	}
}
