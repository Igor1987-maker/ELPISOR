package tests.tms.offlineSync;

import org.junit.Test;

import pageObjects.EdoHomePage;
import pageObjects.EdoLoginPage;
import pageObjects.tms.TmsHomePage;
import tests.misc.EdusoftWebTest;

public class testSyncFiles extends EdusoftWebTest {
	// run only with edoprtu.properties

	@Test
	public void testGettingSyncFileFronOnlineForTheFirstTime() throws Exception {
		String institutionId = "6550216";

		report.report("set instititution lasyOfflineSyncToNull");
		dbService.setInstitutionLastOfflineSyncToNull(institutionId);

		report.report("Login as institution Admin");

		EdoHomePage edoHomePage = LoginAsSchoolAdmin(institutionId, "Admin",
				"Admin");

		report.report("Click on teacher's corner");
		TmsHomePage tmsHomePage = (TmsHomePage) edoHomePage
				.clickOnTeachersCorner(false);
		report.report("Click on the start button");
		// tmsHomePage.swithchToMainFrame();
		tmsHomePage.clickOnSyncStartButton();

		tmsHomePage.clickOnSyncSyncronizeButton();

		report.report("Check for sync file");
		boolean syncFileFound = tmsHomePage.checkForSyncFile(institutionId,
				configuration.getProperty("tms.url"));

		testResultService.assertEquals(true, syncFileFound,
				"Sync file not found");

	}

	private EdoHomePage LoginAsSchoolAdmin(String institutionId,
			String userName, String password) throws Exception {
		pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(userName,
				institutionId));
		String schoolName = dbService.getInstituteNameById(institutionId);
		String url = configuration.getProperty("sut.url");
		url += "/" + schoolName + ".aspx";
		webDriver.openUrl(url);
		EdoLoginPage edoLoginPage = new EdoLoginPage(webDriver,
				testResultService);
		edoLoginPage.typeUserNameAndPass(userName, password);
		EdoHomePage edoHomePage = edoLoginPage.submitLogin();
		webDriver.closeAlertByAccept();
		return edoHomePage;
	}

}
