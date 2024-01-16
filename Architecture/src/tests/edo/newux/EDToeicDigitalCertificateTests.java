package tests.edo.newux;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.WebElement;

import Enums.ByTypes;
import Interfaces.TestCaseParams;
import pageObjects.edo.DigitalCertificatePage;
import pageObjects.edo.NewUxDragAndDropSection2;
import testCategories.inProgressTests;

public class EDToeicDigitalCertificateTests extends BasicNewUxTest{

	NewUxDragAndDropSection2 dragAndDrop2;
	DigitalCertificatePage certificatePage;
	
	
	
	@Before
	public void setup() throws Exception {
		super.setup();
	}
	
	@Test
	@TestCaseParams(testCaseID = { "89417" })
	public void testVerifyToeicDigitalCertificate() throws Exception {
	
		report.startStep("Open Digital Certificate page of certain user");
			String etsURL = "https://etscertificate.edusoftrd.com/";
			String[] certificateData = pageHelper.getStudentScoresAndCertificateURL("9");
			etsURL+=certificateData[4];
			webDriver.openUrl(etsURL); 
			waitTillCertificatePageDownload();
			certificatePage = new DigitalCertificatePage(webDriver, testResultService);
			
		report.startStep("Verify score report page downloaded");
			certificatePage.verifyTitleOfScoreReportPage();
			
		report.startStep("Verify student name displayed");					
			String studentNameFromDB = certificateData[2]+" "+certificateData[3];
			String studentName = certificatePage.getStudentName();
			textService.assertNotNull("Incorrect student name, or it's not found ", studentName);
			//textService.assertEquals(studentNameFromDB, studentNameFromDB, studentName.getText());
			
		report.startStep("Open Validation page of Digital Certificate");
			certificatePage.openValidationPage();
		
		report.startStep("Verify Validation page open");
			certificatePage.verifyValidationPageTitle();
				
		report.startStep("Verify scores of student displayed and compare them with DB");
			certificatePage.validateStudentScores(certificateData);
			certificatePage.verifyTestDate();
					
		report.startStep("Return to Score report page");	
			certificatePage.returnToScoreReportPage();
			certificatePage.verifyTitleOfScoreReportPage();
			
		report.startStep("View and check certificate himself");
			certificatePage.clickOnViewCertificate();
			certificatePage.zoomOutCertificate();
			textService.assertNotNull("QR code missed", certificatePage.getQrElement());
			
		report.startStep("Close certificate");
			certificatePage.closeCertificate();
		
	}
	
	@Test
	@TestCaseParams(testCaseID = { "92358" })
	public void testOpenValidationPageByQR() throws Exception {
	
		report.startStep("Open Digital Certificate page of certain user");
			String etsURL = "https://etscertificate.edusoftrd.com/";
			String[] certificateData = pageHelper.getStudentScoresAndCertificateURL("18");
			etsURL+=certificateData[4];
			webDriver.openUrl(etsURL);
			waitTillCertificatePageDownload();
			certificatePage = new DigitalCertificatePage(webDriver, testResultService);
		
		report.startStep("Obtain  certificateId, nationalID, testDate from UI");	
			String certificateId = certificateData[5];
			String nationalId = certificatePage.getNationalID();
			String testDate = certificatePage.getTestDate();
			
		report.startStep("Click on View certificate");	
			certificatePage.clickOnViewCertificate();
			sleep(1);
			
		report.startStep("Click on QR-code to validate certificate");	
			certificatePage.clickOnQRcode();
			
		report.startStep("Paste all details (certificateId, nationalID, testDate) and click on validate certificate");	
			certificatePage.verifyCertificate(nationalId, testDate, certificateId);  //321321321 ,  	18/01/2023 , 888194305788
			certificatePage.clickOnValidate();
			
		report.startStep("Click on view full score report");	
			certificatePage.clickOnViewFullScoreReport();
			
		report.startStep("Verify that you returned to Score Report page in new tab");	
			certificatePage.verifyTitleOfScoreReportPage();
	}
	
	
	@Test
	@TestCaseParams(testCaseID = { "89210" })
	public void testValidationPageShowCorrectData() throws Exception {
	
		report.startStep("Open Digital Certificate page of certain user");
			String etsURL = "https://etscertificate.edusoftrd.com/";
			String[] certificateData = pageHelper.getStudentScoresAndCertificateURL("18");
			etsURL+=certificateData[4];
			webDriver.openUrl(etsURL); 
			waitTillCertificatePageDownload();
			certificatePage = new DigitalCertificatePage(webDriver, testResultService);
			
		report.startStep("Get student name and national id");
			String studentName = certificatePage.getStudentName();
			String nationalId = certificatePage.getNationalID();
			
		report.startStep("Get students scores and compare with DB scores");
			String[] scores = certificatePage.getScoresFromScoresPage();
			textService.assertEquals("", certificateData[0].split("[.]")[0], scores[0]);
			textService.assertEquals("", certificateData[1].split("[.]")[0], scores[1]);
			
		report.startStep("Open validation scores page");
			certificatePage.openValidationPage();
			sleep(1);
			textService.assertEquals("Validation page opens in new tab", 1, certificatePage.getAmountOfWindows().size());
		
		report.startStep("Validate student scores");
			certificatePage.validateStudentScores(certificateData);
			
		report.startStep("Validate inputs on validation page");
			certificatePage.verifyInputTextOnValidationPage();
			
		report.startStep("Validate url not contain student details");
			certificatePage.verifyUrlDoesntContainStudentDetails(studentName,nationalId,certificateData[5]);
			
	}


	@Test
	@TestCaseParams(testCaseID = {"93364"})
	public void testVerifyLoadersPresent() throws Exception {

		String incorrectNationalId = "0987652";
		String incorrectTestDate = "11/10/2023";
		String incorrectCertificateId = "1234567890";

		report.startStep("Open url");
		String etsURL = "https://etscertificate.edusoftrd.com/";
		String[] certificateData = pageHelper.getStudentScoresAndCertificateURL("18");
		etsURL+=certificateData[4];
		webDriver.openUrl(etsURL);

		report.startStep("Open the certificate and check the page loaded");
		certificatePage = new DigitalCertificatePage(webDriver, testResultService);
		Thread.sleep(300);
		textService.assertTrue(certificatePage.isLoaderVisible(certificatePage.title, certificatePage.TOEIC_TEST_SCORE_REPORT_TITLE));
		certificatePage.clickOnViewCertificate();
		certificatePage.closeCertificate();

		report.startStep("Open validation page");
		certificatePage.openValidationPage();
		Thread.sleep(300);
		report.startStep("Clear form");
		certificatePage.clickClearFormButton();

		report.startStep("Add incorrect data on validation field");
		certificatePage.nationalIdInput.sendKeys(incorrectNationalId);
		certificatePage.testDateInput.sendKeys(incorrectTestDate);
		certificatePage.certificateIdInput.sendKeys(incorrectCertificateId);
		certificatePage.clickOnValidate();

		report.startStep("Check the loader present on validation page");
		textService.assertTrue(certificatePage.isLoaderVisible(certificatePage.verifyText, certificatePage.VALIDATION_TEXT_RESULTS));

	}



	private void waitTillCertificatePageDownload() throws Exception {
		WebElement DC_page = webDriver.waitForElement("//div[@class='container-content container-content--gray-100']/h1", ByTypes.xpath, false, 5);
			for (int i=0; i<10 && DC_page == null;i++){
				DC_page = webDriver.waitForElement("//div[@class='container-content container-content--gray-100']/h1", ByTypes.xpath, false, 5);
		}
	}
	
	
}
