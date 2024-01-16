package drivers;

import java.net.URL;
import java.util.logging.Level;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import services.DbService;

public class ChromeWebDriver extends GenericWebDriver {

	

	@Override
	public void init(String remoteUrl, boolean userProxy) throws Exception {

		reporter.report("remote url in chrome webdriver: " + remoteUrl);
		setBrowserName("chrome");
		setInitialized(true);
		dbService = new DbService();
		ChromeOptions options = new ChromeOptions();
		
		try {
			if (userProxy) {
				startProxyServer();
				options.setCapability(CapabilityType.PROXY, proxy);

			}

			options.addArguments("--disable-extensions");
		//	if (getTestRunner().toString().equalsIgnoreCase("CI"))
		//		options.addArguments("--headless"); // HIDE THE CHROME AND RUN IN BACKGROUND

		//	options.addArguments("--ignore-certificate-errors");
			options.addArguments("--start-maximized");
			
			options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"}); // Disable Chrome is been controlled by another software
			options.setExperimentalOption("useAutomationExtension", false);
		//	options.setAcceptInsecureCerts(true);
			
			if (openIncognitoChrome)
				options.addArguments("incognito");	
			
			// only for SR tests
			try {
				if (System.getProperty("chromeMedia").equals("true")) {
					options.addArguments("--use-fake-ui-for-media-stream");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}
			options.setCapability(ChromeOptions.CAPABILITY, options);
			
			webDriver = new RemoteWebDriver(new URL(remoteUrl + "/wd/hub"), options);
					
			setPageLoadTimeOut();
		
			setScriptLoadTimeOut();

		}

		catch (Exception e) {
			System.out.println(e.getMessage());
			logger.error("Cannot register node or start the remote driver! ", e);
		} finally {
			
		}

	}

	

}
