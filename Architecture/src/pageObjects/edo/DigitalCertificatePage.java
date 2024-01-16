package pageObjects.edo;

import Enums.ByTypes;
import drivers.GenericWebDriver;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pageObjects.GenericPage;
import services.TestResultService;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class DigitalCertificatePage extends GenericPage {

	@FindBy(xpath = "//ets-certificate-data-table/div/div[1]/table/tr/td[2]")
	public WebElement testDate;

	@FindBy(css = "div[class='certificate-container'] div ets-student-data div h2")
	public WebElement nationalId;

	@FindBy(xpath = "//span[normalize-space()='View']")
	public WebElement viewButton;

	@FindBy(css = "a[target='_blank']")
	public WebElement QRcode;

	@FindBy(id = "nationalId")
	public WebElement nationalIdInput;

	@FindBy(id = "testDate")
	public WebElement testDateInput;

	@FindBy(id = "certificateId")
	public WebElement certificateIdInput;

	@FindBy(xpath = "//button[@type='submit']")
	public WebElement validateButton;

	@FindBy(css = ".btn.btn-m.btn-default")
	public WebElement scoreReportButton;

	@FindBy(xpath = "//div[@class='container-content container-content--gray-100']/h1")
	public WebElement title;

	@FindBy(css = ".btn-lg.btn-toeic")
	public WebElement validateReportButton;

	@FindBy(css = ".btn.btn-lg")
	public WebElement validateBridgeReportButton;

	@FindBy(css = ".container-score h1")
	public List<WebElement> scores;

	@FindBy(css = "div[class='skill-score--values flex'] h1")
	public List<WebElement> scoresOnScoresPage;

	@FindBy(css = ".display-2.text-primary")
	public WebElement studentName;

	@FindBy(xpath = "//div[@class='container-content container-content--white']/h1")
	public WebElement validationPageTitle;

	@FindBy(xpath = "//div[@class='result-box result-box--pass']//div//button")
	public WebElement returnToScoreReportPageButton;

	@FindBy(tagName = "ets-icon-close")
	public WebElement closeCertificate;

	@FindBy(css = ".loader")
	public WebElement loader;

	@FindBy(css = ".btn-text--clear")
	public WebElement clearForm;

	@FindBy(css = "h3.invalid")
	public WebElement verifyText;

	public String TOEIC_CERTIFICATE_VALIDATION_TITLE = "TOEIC® Certificate Validation"; //"TOEIC CERTIFICATE VALIDATION";

	public String TOEIC_BRIDGE_CERTIFICATE_VALIDATION_TITLE = "TOEIC BRIDGE CERTIFICATE VALIDATION";

	public String TOEIC_TEST_SCORE_REPORT_TITLE = "TOEIC® TEST SCORE REPORT";

	public String TOEIC_BRIDGE_TEST_SCORE_REPORT_TITLE = "TOEIC BRIDGE® TEST SCORE REPORT";

	public String VALIDATION_TEXT_RESULTS ="We could not verify this search";

	public boolean isToeicBridgeValidationPage = false;


	public DigitalCertificatePage(GenericWebDriver webDriver, TestResultService testResultService) throws Exception {
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

	public String getNationalID() {
		return nationalId.getText();
	}

	public String getTestDate() {
		return testDate.getText();
	}

	public void clickOnViewCertificate() {
		viewButton.click();
	}

	public void clickOnQRcode() throws Exception {
		webDriver.waitForElementByWebElement(QRcode, "QR code", true, 10);
		QRcode.click();

	}

	public WebElement getQrElement() throws Exception {
		webDriver.waitForElementByWebElement(QRcode, "QR code", true, 10);
		return QRcode;

	}

	public void verifyCertificate(String nationalId, String testDate, String certificateId) throws Exception {
		webDriver.switchToNewWindow();
		Thread.sleep(100);
		nationalIdInput.sendKeys(nationalId);
		Thread.sleep(300);
		testDateInput.sendKeys(testDate);
		Thread.sleep(300);
		certificateIdInput.sendKeys(certificateId);
		Thread.sleep(300);
	}

	public void clickOnValidate() {
		validateButton.click();
	}

	public void clickOnViewFullScoreReport() throws Exception {
		webDriver.waitForElementByWebElement(scoreReportButton, "scoreReportButton", true, 8);
		scoreReportButton.click();
	}

	public void verifyTitleOfScoreReportPage() throws Exception {

		webDriver.waitForElementByWebElement(title, "title", true, 5);
		try {
			textService.assertEquals("Incorrect title", TOEIC_TEST_SCORE_REPORT_TITLE, title.getText());
		} catch (AssertionError e) {
			testResultService.addFailTest(e.getMessage(), true, true);
		}
	}

	public void verifySeveralTitlesOfScoreReportPage() throws Exception {
		webDriver.waitForElementByWebElement(title, "title", true, 5);
		try {
			boolean correctTitle = title.getText().equalsIgnoreCase(TOEIC_TEST_SCORE_REPORT_TITLE);
			if(!correctTitle){
				isToeicBridgeValidationPage = true;
				correctTitle = title.getText().equalsIgnoreCase(TOEIC_BRIDGE_TEST_SCORE_REPORT_TITLE);
			}
			testResultService.assertTrue("Incorrect title",correctTitle);
			//textService.assertEquals("Incorrect title", TOEIC_TEST_SCORE_REPORT_TITLE, title.getText());
		} catch (AssertionError e) {
			testResultService.addFailTest(e.getMessage(), true, true);
		}
	}

	public void openValidationPage() throws Exception {
		if (isToeicBridgeValidationPage){
			webDriver.scrollToElement(validateBridgeReportButton);
			webDriver.hideFooter(".footer-blur");
			webDriver.hideFooter(".footer");
			Thread.sleep(1);
			validateBridgeReportButton.click();
		}else {
			webDriver.scrollToElement(validateReportButton);
			webDriver.hideFooter(".footer-blur");
			webDriver.hideFooter(".footer");
			Thread.sleep(1);
			validateReportButton.click();
		}

	}

	public ArrayList<String> getAmountOfWindows() {
		return new ArrayList<>(webDriver.getWebDriver().getWindowHandles());

	}

	public void validateStudentScores(String[] certificateData) throws Exception {


		//List<WebElement> scores = webDriver.getWebDriver().findElements(By.cssSelector(".actual-score.roboto-slab.normal.font-weigth--500"));//webDriver.getElementsByXpath("actual-score.roboto-slab.normal.font-weigth--500");
		//List<WebElement> scores = webDriver.getWebDriver().findElements(By.cssSelector(".container-score h1"));
		//String totalScoresUI = scores.get(0).getText();
		webDriver.waitForElementByWebElement(scores.get(0), "scores", true, 5);
		String readingUI = scores.get(0).getText();
		String listeningUI = scores.get(1).getText();
		String read = certificateData[0].split("[.]")[0];
		String listen = certificateData[1].split("[.]")[0];
		textService.assertEquals("Incorrect data", readingUI, read);
		textService.assertEquals("Incorrect data", listeningUI, listen);
	}

	public String[] getScoresFromScoresPage() throws Exception {

		webDriver.waitForElementByWebElement(scoresOnScoresPage.get(0), "scoresOnScoresPage", true, 5);
		String[] arr = new String[scoresOnScoresPage.size()];
		int i = 0;
		while (i < scoresOnScoresPage.size()) {
			arr[i] = scoresOnScoresPage.get(i).getText();
			i++;
		}
		return arr;
	}

	public void verifyInputTextOnValidationPage() {
		String cl1 = nationalIdInput.getAttribute("class");
		nationalIdInput.sendKeys("1");
		String cl2 = nationalIdInput.getAttribute("class");
		textService.assertNotSame("Typing to nationalID broken", cl1, cl2);

		cl1 = testDateInput.getAttribute("class");
		testDateInput.sendKeys("3");
		cl2 = testDateInput.getAttribute("class");
		textService.assertNotSame("Typing to testDate broken", cl1, cl2);

		cl1 = certificateIdInput.getAttribute("class");
		certificateIdInput.sendKeys("t");
		cl2 = certificateIdInput.getAttribute("class");
		textService.assertNotSame("Typing to testDate broken", cl1, cl2);
	}

	public String getStudentName() throws Exception {
		webDriver.waitForElementByWebElement(studentName, "studentName", true, 5);
		return studentName.getText();

	}

	public void verifyUrlDoesntContainStudentDetails(String studentName, String nationalId, String certificateId) throws Exception {
		String url = webDriver.getUrl();
		textService.assertFalse("URL contains sensitive info", url.contains(studentName));
		textService.assertFalse("URL contains sensitive info", url.contains(nationalId));
		textService.assertFalse("URL contains sensitive info", url.contains(certificateId));
	}

	public void verifyValidationPageTitle() throws Exception {
		webDriver.waitForElementByWebElement(validationPageTitle, "validationPageTitle", true, 2000);
		textService.assertEquals("Incorrect title", validationPageTitle.getText(), TOEIC_CERTIFICATE_VALIDATION_TITLE);
	}

	public void verifySeveralValidationPageTitles() throws Exception {
		webDriver.waitForElementByWebElement(validationPageTitle, "validationPageTitle", true, 2000);
		boolean sameTitle = validationPageTitle.getText().equalsIgnoreCase(TOEIC_CERTIFICATE_VALIDATION_TITLE);
		if (!sameTitle){
			sameTitle = validationPageTitle.getText().equalsIgnoreCase(TOEIC_BRIDGE_CERTIFICATE_VALIDATION_TITLE);
		}
		textService.assertTrue("Incorrect title",sameTitle);
		//textService.assertEquals("Incorrect title", validationPageTitle.getText(), TOEIC_CERTIFICATE_VALIDATION_TITLE);

}


	public void verifyTestDate() {
		String testDatePlaceholder = testDateInput.getAttribute("placeholder");
		textService.assertTrue("Placeholder empty", testDatePlaceholder.length() > 0);
	}

	public void returnToScoreReportPage() {
		returnToScoreReportPageButton.click();

	}

	public void zoomOutCertificate() {
		JavascriptExecutor jse = (JavascriptExecutor) webDriver.getWebDriver();
		jse.executeScript("document.body.style.zoom = '70%';");
	}

	public void closeCertificate() {
		WebElement closeCertificate = webDriver.getElement(By.tagName("ets-icon-close"));
		webDriver.clickOnElementByJavaScript(closeCertificate);

	}

	public boolean isLoaderVisible(WebElement element, String expectedText) {
		WebDriverWait wait = new WebDriverWait(webDriver.getWebDriver(), 20);

		try {
			wait.until(ExpectedConditions.visibilityOf(loader));
			return true;
		} catch (NoSuchElementException | StaleElementReferenceException | TimeoutException e) {
			if (element.isDisplayed() && element.getText().equals(expectedText)) {
				return true;
			}
			return false;
		}
	}


	public void clickClearFormButton() throws Exception {
		clearForm.click();
	}

	public void waitCertificateDownloaded() throws Exception {
		WebElement DC_page = webDriver.waitForElement("//div[@class='container-content container-content--gray-100']/h1", ByTypes.xpath, false, 5);
		for (int i = 0; i < 10 && DC_page == null; i++) {
			DC_page = webDriver.waitForElement("//div[@class='container-content container-content--gray-100']/h1", ByTypes.xpath, false, 5);
		}
	}

}