package tests.misc;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.rules.Timeout;
import org.junit.runner.RunWith;

import services.DictionaryService;
import services.EraterService;
import services.ExtendedRunner;
import services.InstitutionService;
import services.MailService;
import services.NetService;
import services.StudentService;
//import services.TfsService;
import Enums.Browsers;
import Enums.BrowsersConfigs;
import Objects.AutoInstitution;
import Objects.GenericTestObject;
import Objects.testResult;
import drivers.AndroidAppiumWebDriver;
//import drivers.AndroidAppiumWebDriver;
import drivers.AndroidWebDriver;
import drivers.ChromeWebDriver;
import drivers.FirefoxWebDriver;
import drivers.GenericWebDriver;
import drivers.HeadlessBrowser;
import drivers.IEWebDriver;
import drivers.IpadDriver;
import drivers.SafariWebDriver;

/**
 * Used to init every test in the system
 * 
 * @author omers
 *
 */
@RunWith(ExtendedRunner.class)
public class EdusoftBasicTest extends GenericTestObject {

	protected GenericWebDriver webDriver;
	protected HeadlessBrowser headlessBrowser;
	protected GenericWebDriver firefoxDriver;
	protected EraterService eraterService;
	protected InstitutionService institutionService;

	protected DictionaryService dictionaryService;
	protected MailService mailService;
	
	

	protected String logFilter = null;
	private int testTimeoutInMinutes = Integer.parseInt(System
			.getProperty("testTimeOut"));

	protected boolean inStep = false;
	protected String testCaseId = null;
	public String testStatus = null;
	boolean testHasFailedResult;

	protected AutoInstitution autoInstitution;

	private boolean printResults = true;

	private boolean hasFailures;
	private String testCaseId1;

	String IE9ip = "http://10.1.0.122:5555";
	String IE10ip = "http://10.1.0.48:5555";
	String safariMacIp = "http://10.1.0.51:5555";

	String browser = null;
	public static int failure_count=0;
	
	/**
	 * for setting default timeout for all tests
	 */
	@Rule
	public final TestRule testTimeOut = Timeout.builder()

	.withTimeout(testTimeoutInMinutes, TimeUnit.MINUTES)
			.withLookingForStuckThread(false).build();

	@Before
	public void setup() throws Exception {
		super.setup();

		String[] testCaseIds = getTestCaseIds();

		if (testCaseIds != null) {
			for (int i = 0; i < testCaseIds.length; i++) {
				report.addTitle("Log for test case: " + testCaseIds[i]);
			}
		}
		String testClass = this.getClass().toString();

		// TODO - add also link to test code
		report.addTitle("Test class and method: " + testClass + "."
				+ testName.getMethodName());

		eraterService = (EraterService) ctx.getBean("EraterService");
		institutionService = (InstitutionService) ctx
				.getBean("InstitutionService");
		studentService = (StudentService) ctx.getBean("StudentService");

		// report = (services.Reporter) ctx.getBean("Reporter");
		mailService = (MailService) ctx.getBean("MailService");
		// report.init();
		netService = (NetService) ctx.getBean("NetService");

		headlessBrowser = (HeadlessBrowser) ctx.getBean("HeadlessBrowser");
		dictionaryService = (DictionaryService) ctx
				.getBean("DictionaryService");

		if (testCaseIds != null) {
			// tfsService = (TfsService) ctx.getBean("TfsService");
			// tfsService.connectToTFS();
			// // audioService = (AudioService) ctx.getBean("AudioService");
			// // report.writelogger("Test name:" + testCaseName);
			//
			if (testCaseIds.length > 0) {

				for (int i = 0; i < testCaseIds.length; i++) {
					String title = tfsService.getWorkItemTitle(testCaseIds[i]);
					if (title != null) {
						report.addTitle("Test case title: " + title);
					}

				}
			}
		}
		int institutionId;
		if (System.getProperty("institutionId") == null) {
			institutionId = 1;
			// autoInstitution =
			// institutionService.getAutoInstitutions().get(0);
		} else {
			// autoInstitution = institutionService.getAutoInstitutions().get(
			// Integer.parseInt(System.getProperty("institutionId")));
			institutionId = Integer.parseInt(System
					.getProperty("institutionId"));
		}

		institutionService.init();

		autoInstitution = institutionService.getInstitution();
		//String planId = configuration.getProperty("planId");
		String planId = configuration.getAutomationParam("planId", "planId");
		if (planId != null) {
			System.setProperty("planId", planId);
		}

//		if (testCaseIds!=null) {
//			for (int i = 0; i < testCaseIds.length; i++) {
//				//			String currentAutomationStatus = tfsService
//				//					.getAutomationStatus(Integer.valueOf(testCaseIds[i]));
//				String desiredAutoStatus = System
//						.getProperty("desiredAutoStatus");
//
//				if (desiredAutoStatus != null) {
//					//				if (!currentAutomationStatus.equals(desiredAutoStatus)) {
//					tfsService.updateTestCaseAutomationState(
//							Integer.valueOf(testCaseIds[i]), desiredAutoStatus);
//					//				}
//				}
//			}
//		}

	}

	public void sleep(int seconds) throws Exception {
		report.report("Sleeping for " + seconds + "seconds");
		Thread.sleep(seconds * 1000);
	}

	/**
	 * This method will run after each test
	 * 
	 * 1. Finalize the html log
	 */
	@After
	public void tearDown() throws Exception {

		try {
			report.endLog();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		boolean testHasFailedResult = testResultService.hasFailedResults();
		// if (printResults == true && testHasFailedResult) {
		// testResultService.printAllFailures();
		// }
		if (testHasFailedResult) {
			// testResultService.testResults.add(new
			// testResult(dbService.sig(4),
			// dbService.sig(4), null));
			report.report("Test failed due to some errors");
		}

	}


	public void startStep(String stepName) throws Exception {
		if (inStep == true) {
			report.stopLevel();
		}
		// report.step(stepName);
		report.report(stepName);
		// report.report("Step: " + stepName);
		inStep = true;
	}

	public void printMessage(String message) {

		report.report(message);
	}

	public void endStep() throws Exception {
		report.stopLevel();
	}

	public boolean isPrintResults() {
		return printResults;
	}

	public void setPrintResults(boolean printResults) {
		this.printResults = printResults;
	}

	public String getTestCaseId() {
		return testCaseId;
	}

	public void setTestCaseId(String testCaseId) {
		this.testCaseId = testCaseId;
	}

//	public void setEnableLoggin(boolean enableLoggin) {
//		this.enableLoggin = enableLoggin;
//	}

	public void setLogFilter(String logFilter) {
		this.logFilter = logFilter;
	}

	public void setTestTimeoutInSeconds(int testTimeoutInMinutes) {
		this.testTimeoutInMinutes = testTimeoutInMinutes;
	}

	public int getTfsConfigId(String browser) {
		int configId = 0;
		// 110 - win 7 / firefox 43
		// 105 - win 7 / chrome 47
		// 24 - win 7 / IE11
		// 7 - win 7 / IE9
		// 17 - MAC / safari
		// 42 - win 7 / IE10
		// 27 - win 7 / chrome android
		
		// 107 - win 10 / firefox 43
		// 106 - win 10 / chrome 47
		// 108 - win 10 / IE11
		// 111 - Windows 7,Chrome
		// 112 - Windows 7,FireFox

		String OSAgent = configuration.getUserAgentOS();
		
				
		if (browser.equals(BrowsersConfigs.IE9.toString()) && OSAgent.equalsIgnoreCase("windows7"))  {
			configId = 7;
		} else if (browser.equals(BrowsersConfigs.IE10.toString()) && OSAgent.equalsIgnoreCase("windows7")) {
			configId = 42;
		} else if (browser.equals(BrowsersConfigs.firefox.toString())&& OSAgent.equalsIgnoreCase("windows7"))  {
			configId = 112;
		} else if (browser.equals(BrowsersConfigs.chrome.toString())&& OSAgent.equalsIgnoreCase("windows7"))  {
			configId = 111;
		} else if (browser.equals(BrowsersConfigs.IE11.toString())&& OSAgent.equalsIgnoreCase("windows7"))  {
			configId = 24;
		} else if (browser.equals(BrowsersConfigs.firefox.toString()) && OSAgent.equalsIgnoreCase("windows10"))  {
			configId = 107;
		} else if (browser.equals(BrowsersConfigs.chrome.toString()) && OSAgent.equalsIgnoreCase("windows10"))  {
			configId = 106;
		} else if (browser.equals(BrowsersConfigs.IE11.toString()) && OSAgent.equalsIgnoreCase("windows10"))  {
			configId = 108;
					
		} else if (browser.equals(BrowsersConfigs.safariMac.toString())) {
			configId = 17;
		} else if (browser.equals(BrowsersConfigs.chromAndroid.toString())) {
			configId = 49;
		} else if (browser.equals(BrowsersConfigs.ipad.toString())) {
			configId = 1;
		}

		System.setProperty("configId", String.valueOf(configId));
		
		return configId;
	}

	/**
	 * get the browser parameter from the maven CMD/properties file Calls the
	 * init method for the specific webDriver Set a specific remote machine to
	 * run the tests if browser is IE9/IE10/MAC etc
	 */
	public void selectBrowser() {

		if (System.getProperty("brw") != null) {
			browser = System.getProperty("brw");
//			System.out.println("Got browser from runner: " + browser);

			String remoteMachine = null;

			if (browser == BrowsersConfigs.IE9.toString()) {
				remoteMachine = IE9ip;
			} else if (browser == BrowsersConfigs.IE10.toString()) {
				remoteMachine = IE10ip;
			} else if (browser == BrowsersConfigs.safariMac.toString()) {
				remoteMachine = safariMacIp;
			}

			if (remoteMachine != null) {
				System.setProperty("remoteMachine", remoteMachine);
			}

		}

		else if (System.getProperty("browserParam") == null
				|| System.getProperty("browserParam").equals(
						Browsers.empty.toString())) {

			browser = configuration.getAutomationParam("browser", "browserCMD");
		}

		// else if (System.getProperty("brw") != null) {
		// browser = System.getProperty("brw");
		// System.out.println("Got browser from runner");
		// }

		else {
			browser = System.getProperty("browserParam");
		}

		report.addTitle("Using " + browser + " Browser");
		browser = browser.trim();
		if (browser.equals(BrowsersConfigs.chrome.toString())) {
				webDriver = (ChromeWebDriver) ctx.getBean("ChromeWebDriver");
				
		} else if (browser.equals(Browsers.safari.toString())
				|| browser.equals("safariMac")
				|| browser.equals(BrowsersConfigs.safariMac)) {
			webDriver = (SafariWebDriver) ctx.getBean("SafariWebDriver");
		} else if (browser.equals(Browsers.IE.toString())
				|| browser.equals(BrowsersConfigs.IE9.toString())
				|| browser.equals(BrowsersConfigs.IE10.toString())
				|| browser.equals(BrowsersConfigs.IE11.toString())) {
			webDriver = (IEWebDriver) ctx.getBean("IEWebDriver");
		} else if (browser.equals(Browsers.firefox.toString())) {
			webDriver = (FirefoxWebDriver) ctx.getBean("FirefoxWebDriver");
		} else if (browser.equals(Browsers.android.toString())
				|| browser.equals(BrowsersConfigs.chromAndroid.toString())) {
			webDriver = (AndroidAppiumWebDriver) ctx.getBean("AndroidAppiumWebDriver");
		} else if (browser.equals(BrowsersConfigs.ipad)) {
			webDriver = (IpadDriver) ctx.getBean("IpadDriver");
		}

		if (webDriver == null) {
			webDriver = (ChromeWebDriver) ctx.getBean("ChromeWebDriver");
		}

		webDriver.setBrowserName(browser);
		System.setProperty("browserName", browser);
		getTfsConfigId(browser);
	}
		

}
