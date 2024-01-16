package tests.misc;

import org.junit.After;
import org.junit.Assert;
import services.AudioService;
import services.PageHelperService;
import Enums.AutoParams;
import Enums.Browsers;

public class EdusoftWebTest extends EdusoftBasicTest {

	// protected GenericWebDriver webDriver; //kill
	public PageHelperService pageHelper;
	public AudioService audioService;
	
	protected boolean enableLoggin = false;
	protected boolean enableProxy=false;

	//In seconds
	String webDriverDefaultTimeOUt = "10";

	public String[] testedBrowser = new String[] { "chrome", "firefox", "IE" };

	// String browser = null;

	@Override
	public void setup() throws Exception {
		super.setup();
		enableLoggin = true;

		selectBrowser();

		if (webDriver == null) {
			testResultService
					.addFailTest("No webdriver found. Please check properties file or pom for webdriver name");
		}

		if (enableLoggin == true) {
			webDriver.setEnableConsoleLog(true);
		}
		webDriver.init(enableProxy);
		String timeout = configuration.getAutomationParam(
				AutoParams.timeout.toString(), "timeOutCMD");
		if (timeout.equals("")) {
			timeout = webDriverDefaultTimeOUt;
		}
		if (System.getProperty("testCaseId") != null) {

			String[] testCaseIds = getTestCaseIds();

//			for (int i = 0; i < testCaseIds.length; i++) {
//				report.addTitle("Test case title: "
//						+ tfsService.getTestCaseTitle(Integer
//								.parseInt(testCaseIds[i])));
//			}
		}
		report.report("Default timeout was set to: " + timeout);
		webDriver.setTimeout(Integer.valueOf(timeout));
		webDriver.maximize();
		try {

		} catch (Exception e) {
			System.out.println(e.toString());
			Assert.fail("openening Webdriver failed. Check that selenium node/grid are running and also check configurations");
		}

		pageHelper = (PageHelperService) ctx.getBean("PageHelperService");
		pageHelper.init(webDriver, autoInstitution, testResultService);
		audioService = (AudioService) ctx.getBean("AudioService");

//		setEnableLoggin();
		testResultService.setWebDriver(webDriver);

		// System.out.println("Browser version is: "+webDriver.getBrowserVersion());

	}

	

	public void enableConsoleLoggin() {
		this.enableLoggin=true;
		
	}
	
	public void enableProxy(){
		this.enableProxy=true;
	}
	
	public void disableProxy(){
		this.enableProxy=false;
	}




	@After
	public void tearDown() throws Exception {

		// System.out.println("Start of EdusoftWebTest teardown");
		try {

			// webDriver.stopProxyListen();

//			if (browser!=null) {
//				// print console log if browser was chrome
//				if (browser.equals(Browsers.chrome.toString())) {
//					webDriver.printConsoleLogs("", false);
//				}
//			}

			// if (testResultService.hasFailedResults()) {
			//
			// webDriver.printScreen("FailCause_", null);
			// }

		} catch (Exception e) {
			System.out.println("Exception in WebTest teardown");
			e.printStackTrace();
		} finally {
			webDriver.quitBrowser();
		}
		// System.out.println("end of EdusoftWebTest teardown");
		super.tearDown();
	}

	public String getSutAndSubDomain() {
		return configuration.getAutomationParam(AutoParams.sutUrl.toString(),
				AutoParams.sutUrl.toString() + "CMD")
				+ "//"
				+ configuration.getProperty("institutaion.subdomain");

	}

	public String getBrowser() {
		return browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}

	public boolean isEnableLoggin() {
		return enableLoggin;
	}

	public String getLogFilter() {
		return logFilter;
	}

}
