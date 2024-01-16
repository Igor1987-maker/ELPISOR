package tests.UnitTests;

import java.io.File;

import jcifs.smb.SmbFile;

import org.hamcrest.core.IsInstanceOf;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import drivers.FirefoxWebDriver;
import drivers.IEWebDriver;
import Enums.Browsers;
import Enums.TestRunnerType;
import Interfaces.TestCaseParams;
import tests.misc.EdusoftWebTest;

public class WebDriverUnitTests extends EdusoftWebTest {

	@Before
	public void setup() throws Exception {
		super.setup();
	}

	@Test
	@TestCaseParams(testCaseID = { "1" }, testedBrowser = Browsers.IE)
	public void testOpenIE() throws Exception {

		Assert.assertTrue(webDriver instanceof IEWebDriver);

	}
	
	@Test
	@TestCaseParams(testCaseID = { "20606" }, testedBrowser = Browsers.firefox)
	public void testOpenFirefox() throws Exception {

		Assert.assertTrue(webDriver instanceof FirefoxWebDriver);

	}

	@Test
	public void takeScreenshot() throws Exception {
		String fileName = "unitTestPrintScreen" + dbService.sig();
		String path = webDriver.printScreen(fileName);
		TestRunnerType runner =getTestRunner();
		System.out.println("Check if file was created");
		if (runner == TestRunnerType.CI) {
			path=path.replace("http", "smb");
			SmbFile file = new SmbFile(path, netService.getAuth());
			
		} else {
			File file = new File(path);
			testResultService.assertEquals(true, file.exists());
		}

		
	}

	@Test
	public void testTakeScreenShot() {

	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

}
