package tests.toeic.oms;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;

import pageObjects.edo.NewUXLoginPage;
import services.PageHelperService;
import tests.misc.EdusoftWebTest;

public class OMSbase extends EdusoftWebTest {
	//LoginPage loginPage = new LoginPage(webDriver, testResultService);
	@Before
	public void setup() throws Exception {
		super.setup();
		report.startStep("Open Toiec");
		webDriver.deleteCookiesAndCache();
		webDriver.quitBrowser();
		
		webDriver.init();
		webDriver.maximize();
		webDriver.openUrl(PageHelperService.toeicOMSUrl);
		webDriver.getWebDriver().manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		//homePage = getToeicUserAndLogin(PageHelperService.userFilePath);
		
	}
	
	
	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}
}
