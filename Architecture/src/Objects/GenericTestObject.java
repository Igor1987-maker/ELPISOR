package Objects;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ErrorCollector;
import org.junit.rules.Stopwatch;
import org.junit.rules.TestName;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import Enums.TestRunnerType;
import services.Configuration;
import services.DbService;
import services.NetService;
import services.PageHelperService;
import services.Reporter;
import services.StudentService;
import services.TestResultService;
import services.TextService;
import services.TfsService;
import junit.framework.TestCase;

public class GenericTestObject extends TestCase {

	public Reporter report;
	private static final String LOG_FOLDER = "automationLogs";
	
	// protected Configuration configuration;
	protected DbService dbService;
	protected NetService netService;
	protected static TestResultService testResultService;
	protected TextService textService;
	protected Configuration configuration;
	protected TfsService tfsService;
	
	public StudentService studentService;

	protected String testCaseId = null;
	public String testStatus = null;
	boolean testHasFailedResult;
	
	String testElapsedTime=null;
	public static String trigger="";

	public String planId;

	protected static String logFileLink;
	protected static String logFileName;
	protected static long testDuration;
	
	public ClassPathXmlApplicationContext ctx;

	public TestRunnerType runnerType;
	
	@Rule
	public Stopwatch stopWatch=new  Stopwatch() {
	};
	
	@Rule
	public ErrorCollector collector=new ErrorCollector();

	@Rule
	public TestWatcher watcher = new TestWatcher() {

		@Override
		//This will run if test has "Passed"
		protected void succeeded(Description description) {
			// TODO Auto-generated method stub
			
//			System.out.println("Test runtime: "+ stopWatch.runtime(TimeUnit.SECONDS));
			
			report.addEntryInIndex(3, "Test runtime: "+ stopWatch.runtime(TimeUnit.SECONDS)+" Seconds");
			//testDuration = stopWatch.runtime(TimeUnit.SECONDS);
			
			testStatus = "passed";
			//logFileName = null;
			testHasFailedResult = testResultService.hasFailedResults();
			if (testHasFailedResult == true) {
				// fail();
				testResultService.printAllFailures();
				logFileLink = printLogs();
				logFileLink = null;
				repotTestResultToTfs(false);
				org.junit.Assert
						.fail("Test has completed but failed due to some error/Assertions");

			}
			repotTestResultToTfs(true);
			logFileLink = printLogs();
			logFileLink = null;
			
			super.succeeded(description);
		}

		private void repotTestResultToTfs(boolean passed) {

			String[] testCaseIds = getTestCaseIds();
			// fileName = fileName.replace(".html", "");

			if (testCaseIds != null) {
				for (int i = 0; i < testCaseIds.length; i++) {
					// String suiteId = System.getProperty("suiteId");
					String configId = System.getProperty("configId");
					// String testCaseId = System.getProperty("testCaseId");
					String planId = System.getProperty("planId");
					String reportToTFS = configuration.getAutomationParam(
							"useTfs", "useTfs");
					tfsService.updateTestResults(planId, testCaseIds[i],
							configId, passed, reportToTFS);
				}
			}

		}

		@Override
		protected void failed(Throwable e, Description description) {
			// TODO Auto-generated method stub
			testStatus = "failed";
			
			report.addEntryInIndex(3, "Test runtime: "+ stopWatch.runtime(TimeUnit.SECONDS)+" Seconds");
			//testDuration = stopWatch.runtime(TimeUnit.SECONDS);
			
			if (testHasFailedResult == true) {

				testResultService.printAllFailures();

			}
			// testResultService.testResults.add(new testResult("test1",
			// "212121",
			// testResultService.getFailedCauses()));
			repotTestResultToTfs(false);
			logFileLink = printLogs();
			logFileLink = null; //rest for next test.
			super.failed(e, description);

		}

	};
	
	

	@Rule
	public TestName testName = new TestName();
	protected static String testCaseName;

	@Before
	public void setup() throws Exception {
		try {

			System.out
					.println("******    Test details and used configuration  **********");

			ctx = new ClassPathXmlApplicationContext("beans.xml");
			report = (services.Reporter) ctx.getBean("Reporter");
			configuration = (Configuration) ctx.getBean("configuration");

			// configuration=(Configuration)ctx.getBean("configuration");
			textService = (TextService) ctx.getBean("TextSerivce");
			dbService = (DbService) ctx.getBean("DbService");
			testResultService = (TestResultService) ctx
					.getBean("testResultService");
			tfsService = (TfsService) ctx.getBean("TfsService");
//			tfsService.connectToTFS();
			testCaseName = testName.getMethodName();
			// planId = configuration.getProperty("planId");
			// System.out.println("Plan id is: "+planId);
			// System.setProperty("planId", planId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Failed during GenericTestObject setup: "
					+ e.getMessage());
			// testResultService.addFailTest(e.getStackTrace().toString());
		}

	}

	/**
	 * @return the Runner type
	 */
	public TestRunnerType getTestRunner() {
		// if test is run in CI/development

		if (System.getProperty("remote.machine") != null) {
			runnerType = TestRunnerType.CI;
		} else if (System.getProperty("remote.machine") == null) {
			runnerType = TestRunnerType.local;
		}
		return runnerType;
	}

	/**
	 * @return the file name of the log
	 */
	public String printLogs() {
		// return
		
		if (logFileLink == null){
			//logFileLink = null;
			//logFileName = null;
			try {
	
				String testCaseName = System.getProperty("testCaseName");
				String browserName = System.getProperty("browserName");
				if (browserName != null) {
					browserName = browserName.replace(" ", "_");
				} else {
					browserName = "noBrowserSet";
				}
			
				String currentDate = getCurrentDate();
				   
				logFileName = testCaseName + "_" + browserName + currentDate + ".html";
				
				// if running in CI
				// TestRunnerType runner = configuration.getTestRunner();
				TestRunnerType runner = TestRunnerType.CI;
				if (runner == TestRunnerType.CI) {
	
					//String path = "smb://"+configuration.getGlobalProperties("logserverName")+"/AutoLogs/"+LOG_FOLDER+"/" + logFileName;
					String path = "\\\\"+configuration.getGlobalProperties("logserverName")+"\\AutoLogs\\"+LOG_FOLDER+"\\"+logFileName; //smb://10.1.0.213/automationLogs/testlogtestCanvasNewStudentNewClass_chrome_2023_02_02_12.html
					
					textService.writeListToHtmlFile(report.getReportLogs(), false, path);
					logFileLink = "https://"+configuration.getGlobalProperties("urlLogFiles")+"/"+LOG_FOLDER+"/" + logFileName;
					
					//logFileLink = "\\\\"+configuration.getGlobalProperties("logserverName")+"\\"+LOG_FOLDER+"\\" + fileName;
				} else {
					String path = System.getProperty("user.dir") + "/log//current/"+ logFileName;
					logFileLink = path;
					System.out.println("test runner not CI line 236 GenericTestObject");
					textService.writeListToHtmlFile(report.getReportLogs(), false, path);
				}
				System.out.println("Log file can be found here: " + logFileLink);
	
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
			//boolean testHasFailedResult = testResultService.hasFailedResults();
		}
		return logFileLink;
		// testResultService.printAllFailures();
	}

	public String getCurrentDate() {
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyMMdd_HHmmss");
		  LocalDateTime now = LocalDateTime.now();
		String currentDate = dtf.format(now).toString();
		    
		return currentDate;
	}

	public String getShortCurrentDate() {
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyMMddHH");
		  LocalDateTime now = LocalDateTime.now();
		String currentDate = dtf.format(now).toString();
		    
		return currentDate;
	}

	@After
	public void tearDown() throws Exception {
		// printLogs();
//		System.out.println("Generic test object teardown");

		if (testHasFailedResult) {
			// testResultService.testResults.add(new
			// testResult(dbService.sig(4),
			// dbService.sig(4), null));
			// report.report("Test failed due to some errors");
		}
	}

	/**
	 * @return the test cases id's
	 */
	public String[] getTestCaseIds() {
		String[] testCaseIds = null;
		if (System.getProperty("testCaseId") != null) {
			testCaseIds = System.getProperty("testCaseId").split(",");
		}

		return testCaseIds;
	}

	public String getLogFilePath() throws Exception {
		
		String browserName = System.getProperty("browserName");
		if (browserName != null) {
			browserName = browserName.replace(" ", "_");
		} else {
			browserName = "noBrowserSet";
		}
			
		String currentDate = getCurrentDate();
		  
		String fileName = "log_" + testCaseName + "_" + browserName + "_" + currentDate + ".html";
	
		// if running in CI
		// TestRunnerType runner = configuration.getTestRunner();
		TestRunnerType runner = TestRunnerType.CI;
		if (runner == TestRunnerType.CI) {
	
		//	String path = "smb://"+configuration.getGlobalProperties("logserver")+"/"+LOG_FOLDER+"/" + fileName;
		//	textService.writeListToHtmlFile(report.getReportLogs(), true, path);
			logFileLink = "https://"+configuration.getGlobalProperties("urlLogFiles")+"/"+LOG_FOLDER+"/" + logFileName;

			//logFileLink = "\\\\"+configuration.getGlobalProperties("logserverName")+"\\"+LOG_FOLDER+"\\" + fileName;
		} else {
			String path = System.getProperty("user.dir") + "/log//current/"+ fileName;
			logFileLink = path;
		//	textService.writeListToHtmlFile(report.getReportLogs(), false,path);
			}
		
		return logFileLink;
	}
	
	public void insertTestSatusToDB() {
		
		String logTestFileLink;
		
		try {
			String testStatus = getTestStatus();
			String testClass = getClass().toString();
			logTestFileLink = printLogs(); //getLogFilePath();
			String sourceBranch_CI = PageHelperService.branchCI;
			String trigger = GenericTestObject.trigger;
			testDuration = stopWatch.runtime(TimeUnit.SECONDS);
			dbService.insertDBTestStatus(testClass,testCaseName,testStatus,PageHelperService.linkED,logTestFileLink,sourceBranch_CI,trigger,testDuration);	
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public String getTestStatus() throws Exception {

		String testStatus=null;
		
		boolean testHasFailedResult = testResultService.hasFailedResults();

		if (testHasFailedResult)
			testStatus="Failed";
		else
			testStatus="Passed";
		
		return testStatus;
	}
}
