package tests.edoRegression;

import java.io.File;
import java.net.URL;
import java.nio.charset.Charset;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pageObjects.tms.TmsHomePage;
import tests.misc.EdusoftWebTest;

public class ExportTests extends EdusoftWebTest {

	@Before
	public void setup() throws Exception {
		super.setup();
	}

	// Test case id 7760
	@Test
	public void testExportClasses() throws Exception {
		startStep("Init test data");
		String[] classesForImport = pageHelper
				.getClassesForImport(autoInstitution.getInstitutionId());
		classesForImport = pageHelper.getClassesForImport(autoInstitution
				.getInstitutionId());
		startStep("Login to TMS as Admin");
		TmsHomePage tmsHomePage = pageHelper.loginToTmsAsAdmin();

		startStep("Open Classes");
		tmsHomePage.clickOnClasses();
		tmsHomePage.selectInstitute(autoInstitution.getInstitutionName(),
				autoInstitution.getInstitutionId(), true);

		startStep("Select classes for export");
		tmsHomePage.switchToMainFrame();
		for (int i = 0; i < classesForImport.length; i++) {
			String classId = dbService.getClassIdByName(classesForImport[i],
					autoInstitution.getInstitutionId());
			tmsHomePage.markClassForExport(classId);
		}
		tmsHomePage.clickOnExport();
		webDriver.switchToNewWindow(1);
		tmsHomePage.selectExportFormat("txt");
		tmsHomePage.clickOnExportButtonInPopup();

		startStep("Check the exported file");
		String fileName = tmsHomePage.getExportFileName();
		URL url = new URL(configuration.getProperty("tms.exort.url")
				+ fileName);
//		String fileText = textService.getTextFromFile(fileName,
//				Charset.defaultCharset());
		startStep("Analyze file");
		for (int i = 0; i < classesForImport.length; i++) {
			Assert.assertTrue(textService.searchForTextInFile(
					classesForImport[i], url));
		}
	}
	//Test case 7762
		//Export Students
	@Test
	public void testExportStudents()throws Exception{
		startStep("Init test data");
		String []students=null;
		startStep("Login to TMS as Admin and go to Students sectin");
		TmsHomePage tmsHomePage=pageHelper.loginToTmsAsAdmin();
		tmsHomePage.clickOnStudents();
		
		startStep("Select institue and select all student in page");
		tmsHomePage.selectInstitute(autoInstitution.getInstitutionName(), autoInstitution.getInstitutionId());
		tmsHomePage.switchToMainFrame();
		tmsHomePage.selectAllStudents();
		tmsHomePage.switchToTableFrame();
		students=tmsHomePage.getStudentsForExport(10);
		students=pageHelper.convertStudentIdsToNames(students);
		
		startStep("Click on Export and check file");
		tmsHomePage.clickOnExport();
		webDriver.switchToNewWindow(1);
		tmsHomePage.selectExportFormat("txt");
		tmsHomePage.clickOnExportButtonInPopup();

		startStep("Check the exported file");
		webDriver.printScreen();
		String fileName = tmsHomePage.getExportFileName();
		URL url = new URL(configuration.getProperty("tms.exort.url")
				+ fileName);
		startStep("Analyze file");
		for (int i = 0; i < students.length; i++) {
			Assert.assertTrue(textService.searchForTextInFile(
					students[i], url));
		}
	}
	
	
	

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

}
