package tests.edo.speechRecognition;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ThreadGuard;

import pageObjects.EdoHomePage;
import pageObjects.RecordPanel;
import services.AudioService;
//import tests.speechRecognition.SpeechRecognitionBasicTest;
import Enums.AutoParams;
import Enums.ByTypes;
import Interfaces.TestCaseParams;
import Objects.Recording;
import drivers.ChromeWebDriver;
import drivers.ThreadedWebDriver;

public class SRLoadTest extends tests.edo.speechRecognition.SpeechRecognitionBasicTest {

	int numberOfInstances = 1;
	int timeOut=120;
	String env = "internal";// internal,external
	List<ChromeWebDriver> webDriverList = new ArrayList<ChromeWebDriver>();
	List<RecordPanel> recordPanels = new ArrayList<RecordPanel>();

	ThreadGuard threadGuard = new ThreadGuard();
	String slaveName = null;

	@Before
	public void setup() throws Exception {
		super.setup();
		
		// setEnableLoggin(true);
		// setLogFilter("Incomming message");
		slaveName = configuration.getAutomationParam(null, "slaveNameCMD");
	}

	@Test
	@TestCaseParams(testCaseID = { "2222" })
	public void testMultipleBrowserInstances() throws Exception {

		// String slaveName = configuration.getAutomationParam(null,
		// "slaveNameCMD");
		System.out.println("Slave name is:" + slaveName);
		if (slaveName == null) {
			slaveName = "devMachine";
		}

		netService.updateSlaveStatus(slaveName, "not ready");
		AudioService audioService = new AudioService();

		for (int i = 0; i < numberOfInstances; i++) {
			ChromeWebDriver driver = new ChromeWebDriver();

//			driver.setReporter(report);
//			driver.setTestResultService(testResultService);
			driver.setEnableConsoleLog(true);
			driver.init(
					configuration.getAutomationParam(
							AutoParams.remoteMachine.toString(), "machine"),
					false);
			driver.setTimeout(timeOut);
			webDriverList.add(driver);
		}

		for (int i = 0; i < numberOfInstances; i++) {

			// webDriverList.get(i).openUrl(
			// "http://edonov14.prod.com/automation.aspx");
			// webDriverList
			// .get(i)
			// .addValuesToCookie(
			// "Student",
			// "^StudentID*3000025000321^Language*spa^LangSupLevel*3^Courses*0^FName*TMS^LName*Domain^SID*53638^CMode*L^UserType*da^Type*2^LCE*");
			//
			// webDriverList
			// .get(i)
			// .openUrl(
			// "http://edonov14.prod.com/Runtime/ViewComponents.aspx?id=3000025000321&courseId=8&unitId=78&componentId=284&skill=Speaking&componentName=How%20Awful!&componentType=1&level=component");

			if (env.equals("internal")) {

				webDriverList.get(i).openUrl(
						"http://edonov14.prod.com/automation.aspx");
				webDriverList
						.get(i)
						.addValuesToCookie(
								"Student",
								"^StudentID*3000025000321^Language*spa^LangSupLevel*3^Courses*0^FName*TMS^LName*Domain^SID*37537^CMode*L^UserType*da^Type*2^LCE*");

				webDriverList
						.get(i)
						.openUrl(
								"http://edonov14.prod.com/Runtime/ViewComponents.aspx?id=3000025000321&courseId=8&unitId=78&componentId=284&skill=Speaking&componentName=How%20Awful!&componentType=1&level=component");

			} else if (env.equals("external")) {
				webDriverList.get(i).openUrl(
						"http://edobeta.engdis.com/qa.aspx");
				webDriverList
						.get(i)
						.addValuesToCookie(
								"Student",
								"^StudentID*3000025000321^Language*eng^LangSupLevel*3^Courses*0^FName*TMS^LName*Domain^SID*58846^CMode*L^UserType*da^Type*1^LCE*");

				webDriverList
						.get(i)
						.openUrl(
								"http://edobeta.engdis.com/Runtime/ViewComponents.aspx?id=3000025000321&courseId=20191&unitId=22213&componentId=292&skill=Speaking&componentName=Expensive%20Boutique&componentType=1&level=component");

			}

			// /*****beta site
			EdoHomePage edoHomePage = new EdoHomePage(webDriverList.get(i),
					testResultService);

			edoHomePage.clickOnSeeScript();
			sleep(2);
			edoHomePage.selectTextFromContainer(3);

			RecordPanel recordPanel = edoHomePage.clickOnRecordYourself();
			recordPanel.setWebDriver(webDriverList.get(i));
			recordPanels.add(recordPanel);
			sleep(4);
			edoHomePage.switchToFrameByClassName("cboxIframe");
			// Wait for speak

		}

		// wait for all webDrivers to be ready
		for (int i = 0; i < numberOfInstances; i++) {
			String message = recordPanels.get(i).getRecordPanelMessage();
			checkIfErrorAppear(message);
			recordPanels.get(i).clickTheRecordButtonAndClickRecordAndStop();
			// recordPanels.get(i).checkRecordButtonIsEnabled();
			// webDriverList.get(i).printScreen(
			// "Before clicking the record button");
			// recordPanels.get(i).clickOnRecordButton();
			// sleep(4);
			// String status = recordPanels.get(i).getRecordPanelStatus();
			// testResultService.assertEquals("SPEAK", status,
			// "Waiting for SPEAK status");

		}
		netService.updateSlaveStatus(slaveName, "ready");
		netService.checkAllSlaveStatus();
		// click the record button
		for (int i = 0; i < numberOfInstances; i++) {
			recordPanels.get(i).clickOnRecordButton();
		}

		// play the file
		audioService.sendSoundToVirtualMic(new File(
				//"files/audioFiles/A1SHHA_3.wav"), 16000);
				"//"+configuration.getGlobalProperties("logserverName")+"/shared/files/A1SHHA_3.wav"), 16000);
				

		for (int i = 0; i < numberOfInstances; i++) {
			recordPanels.get(i).checkErrorMessageDoesNotExist();

			String actualSentenceFeedbackText = webDriverList
					.get(i)
					.waitForElement("//div[@class='scoreExpWrapper']",
							ByTypes.xpath).getText();
			System.out.println("Feedback: " + actualSentenceFeedbackText);
			if (actualSentenceFeedbackText.equals("Try Again")) {
				testResultService.addFailTest("Bad feedback");
			} else if (actualSentenceFeedbackText.equals(null)) {
				testResultService.addFailTest("no feedback");
			}
		}
		System.out.println("Playing ended");

		// check feedback

		netService.updateSlaveStatus(slaveName, "not ready");
	}

	public void webdriverThreadGuard() throws Exception {
		// ChromeWebDriver webDriver=new ChromeWebDriver();
		// webDriver.init(configuration.getProperty("remote.machine"), null);
		//
		// ThreadGuard threadGuard=new ThreadGuard();
		//
		// threadGuard.protect(webDriver.getWebDriver());
		for (int i = 0; i < numberOfInstances; i++) {
			ChromeWebDriver driver = new ChromeWebDriver();
			driver.init(configuration.getProperty("remote.machine"), false);
			webDriverList.add(driver);
		}

		// TableThread tableThread = new TableThread(driver, "Thread " + i);
		for (int i = 0; i < numberOfInstances; i++) {
			ExecutorService threadExecutor = Executors
					.newSingleThreadExecutor();
			ThreadedWebDriver threadedWebDriver = new ThreadedWebDriver(
					webDriverList.get(i));
			threadExecutor.execute(threadedWebDriver);
		}

	}

	@After
	public void tearDown() throws Exception {
		for (int i = 0; i < numberOfInstances; i++) {
			webDriverList.get(i).printConsoleLogs("", false);
			webDriverList.get(i).quitBrowser();
		}
		netService.updateSlaveStatus(slaveName, "not ready");
		super.tearDown();
	}

}
