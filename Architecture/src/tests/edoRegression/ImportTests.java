package tests.edoRegression;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pageObjects.tms.TmsHomePage;
import tests.misc.EdusoftWebTest;

public class ImportTests extends EdusoftWebTest {

	@Before
	public void setup() throws Exception {
		super.setup();
	}

	// Test case 7764
	// Import students
	@Test
	public void testImportStudents() throws Exception {
		startStep("Init test data");
		
		startStep("Generate text file for import");

		startStep("Login to TMS as Admin");
		TmsHomePage tmsHomePage = pageHelper.loginToTmsAsAdmin();
		
		startStep("Open Students registration");
		tmsHomePage.clickOnStudents();
		tmsHomePage.selectInstitute(autoInstitution.getInstitutionName(), autoInstitution.getInstitutionId(), false);
		tmsHomePage.selectClass("class1");
		
		startStep("Click on import");
//		tmsHomePage.swithchToMainFrame();
		tmsHomePage.clickOnImport();
		webDriver.switchToNewWindow(1);
		tmsHomePage.switchToFormFrame();
		tmsHomePage.clickOnPopupChooseFile();
		
		startStep("Enter file name");
	
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

}
