package pageObjects;

import Enums.ByTypes;
import drivers.GenericWebDriver;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import services.PageHelperService;
import services.TestResultService;

import java.util.List;

public class ToeicUploadResultPage extends GenericPage {


    ToeicStudentsAdministrationPage studentsAdministrationPage;

    @FindBy(css = "[testid='reload-csv']")
    private WebElement reloadCsvButton;

    @FindBy(css = "[testid='fail-reason']")
    private WebElement failReasonElement;

    @FindBy(css = "[testid='user-message']")
    private WebElement userMessageElement;

    @FindBy(css = "[testid='counter']")
    private WebElement counterElement;

    @FindBy(css = "[testid='students']")
    private WebElement studentsElement;

    @FindBy(css = "[testid='errors']")
    private WebElement errorsElement;

    @FindBy (css = ".text-primary-600.mb-7")//(css = ".text-gray-600.text-sm.font-semibold")
    private WebElement administratioName;

    @FindBy(xpath = "//div[text()=' Create Administration']")
    private WebElement createAdministration;

    @FindBy(css = "h1")
    private WebElement alertErrorAfterRegistration;

    @FindBy(css = ".text-2xl.uppercase.font-bold.mb-10")
    private WebElement messageAfterSessionCreated;

    // Constructor and existing methods

    public void reloadCSV() {
        reloadCsvButton.click();
    }

    public boolean isErrorPresent() {
        return failReasonElement.isDisplayed() && userMessageElement.isDisplayed();
    }

    public boolean noErrorsExist() {
        return !isErrorPresent();
    }

    public String getFailReason() {
        return failReasonElement.getText();
    }

    public String getUserMessage() {
        return userMessageElement.getText();
    }

    public int getCounterValue() {
        return Integer.parseInt(counterElement.getText());
    }

    public int getNumberOfStudents() {
        return Integer.parseInt(studentsElement.getText());
    }

    public int getNumberOfErrors() {
        return Integer.parseInt(errorsElement.getText());
    }

    @FindBy(css = "[test-id='edu-logo']")
    private WebElement edusoftLogo;

    @FindBy(css = "[test-id='pagename']")
    private WebElement pageName;

    @FindBy(css = "[test-id='edx-logo']")
    private WebElement edxLogo;

    @FindBy(css = "[test-id='steps']")
    private WebElement stepsPanel;

    @FindBy(css = "[test-id='first-step']")
    private WebElement firstStepDiv;

    @FindBy(css = ".circles")
    private WebElement circleInsideFirstStepDiv;

    @FindBy(css = "[test-id='upload-csv']")
    private WebElement uploadCsvButton;

    @FindBy(css = "[test-id='download-csv']")
    private WebElement downloadCsvButton;

    @FindBy(css = "[test-id='csv-buttons-descr']")
    private WebElement csvButtonsDescriptionLabel;

    @FindBy(css = "[test-id='back']")
    private WebElement backButton;

    @FindBy (xpath = "//div[text()=' Next']") //(css = "[test-id='next']")
    private WebElement nextButton;




    private String registrationUrl = String.format("%s/registration", PageHelperService.edxUrl);

    public ToeicUploadResultPage(GenericWebDriver driver, TestResultService testResultService,
                                 ToeicStudentsAdministrationPage studentsAdministrationPage) throws Exception {
        super(driver, testResultService);
    	this.testResultService = testResultService;
        this.studentsAdministrationPage = studentsAdministrationPage;
        PageFactory.initElements(driver.getWebDriver(), this);
    }

    public void checkPageName() {
        Assert.assertEquals(pageName.getText(), "Toeic Test Student Registration");
    }

    public void checkEdusoftLogo() {
        Assert.assertTrue(edusoftLogo.isDisplayed());
    }

    public void checkEDXLogo() {
        Assert.assertTrue(edxLogo.isDisplayed());
    }

    public void checkFirstStepAppears() {
        String expectedCircleColor = "rgba(0, 128, 0, 1)"; // Replace with the expected green color in rgba format
        Assert.assertEquals(circleInsideFirstStepDiv.getCssValue("background-color"), expectedCircleColor);
    }

    public void verifyNextButtonDisabled() {
        Assert.assertFalse(backButton.isEnabled());
    }

    public void verifyBackButtonEnabled() {
        Assert.assertTrue(backButton.isEnabled());
    }

    public void verifyURL() {
        WebDriverWait wait = new WebDriverWait(webDriver.getWebDriver(), 10);
        wait.until(ExpectedConditions.urlToBe(registrationUrl));
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

    public void clickOnNextButton() throws Exception {
//        waitNextButtonBeEnabled();
//        waitTillNextButtonBeEnabled();
//        webDriver.waitForElementByWebElement(nextButton,"Next",true,20);
//
//        nextButton.click();
//        webDriver.waitForElementByWebElement(nextButton,"Next",true,20);
//        WebElement nextButton = webDriver.getWebDriver().findElement(By.xpath("//div[text()=' Next']"));
//        JavascriptExecutor executor = (JavascriptExecutor)webDriver.getWebDriver();
//        executor.executeScript("arguments[0].click();", nextButton);


        JavascriptExecutor executor = (JavascriptExecutor) webDriver.getWebDriver();

        new WebDriverWait(webDriver.getWebDriver(), 20).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[text()=' Next']")));

        WebElement nextButton = webDriver.getWebDriver().findElement(By.xpath("//div[text()=' Next']"));

        executor.executeScript("arguments[0].click();", nextButton);

    }
    public void waitTillNextButtonBeEnabled() throws Exception {
        WebElement nextBTN = webDriver.waitForElement("//div/app-button[2]",ByTypes.xpath);
        String statusOfNextButton = nextBTN.getAttribute("ng-reflect-disabled");
        int i = 0;
        while (i<5 && statusOfNextButton.equalsIgnoreCase("true")){
            nextBTN = webDriver.waitForElement("//div/app-button[2]",ByTypes.xpath);
            statusOfNextButton = nextBTN.getAttribute("ng-reflect-disabled");
            Thread.sleep(2000);
        }
    }



    public void waitNextButtonBeEnabled() throws Exception {
        boolean isEnable = false;
        int i = 0;
        while (isEnable == false && i < 5) {
            isEnable = nextButton.isEnabled();
            Thread.sleep(2000);
            i++;
        }
        if (isEnable==false){
            testResultService.addFailTest("File not uploaded",true, true);
        }
    }

    public String retrieveCreatedAdministrationName() throws Exception {
        webDriver.waitForElementByWebElement(administratioName,"administratioName",true,10);
        String admName =  administratioName.getText();
        return admName;
    }

    public void clickOnCreateAdministration() throws Exception {
        webDriver.waitForElementByWebElement(createAdministration," Create Administration",true,10);
        createAdministration.click();
    }

    public void verifyWhetherSessionWasCreated() throws Exception {
        WebElement title = null;
        int i = 0;
        while (title == null && i < 3) {
            try{
                i++;
                title = webDriver.waitForElement(".text-2xl.uppercase.font-bold.mb-10", ByTypes.cssSelector, 5, true, null);
            }catch (Exception e) {
               // testResultService.addFailTest("Session wasn't created", true, true);
            }catch (AssertionError e){
               //testResultService.addFailTest("Session wasn't created", true, true);
            }
        }

        if (title != null) {
            String message = title.getText();
            textService.assertEquals("Session wasn't created", "THE SESSIONS WERE CREATED SUCCESSFULLY", message);
        } else {
            testResultService.addFailTest("Session wasn't created", true, true);
        }

    }

    @FindBy(css = "div.p-2.bg-white")
    public List<WebElement> dropdown;

    @FindBy(css = ".h-10")
    public List<WebElement> dropdownMethod;

    public void clickMethodsDropdown(){
        webDriver.ClickElement(dropdown.get(1));
    }

    public void selectByopMethod(){
        webDriver.ClickElement(dropdownMethod.get(1));
    }


}
///html/body/app-root/main/div[2]/div/app-publish-session/div/div/div[2]/app-administration-result-card/div/div[1]/div[2]/p