package drivers;

import java.net.URL;

import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.stereotype.Service;

import services.DbService;

@Service
public class HeadlessBrowser extends GenericWebDriver {

	@Override
	public void init(String remoteUrl, boolean startProxy) throws Exception {
		// TODO Auto-generated method stub
		setTimeout(30);
		setBrowserName("Headless browser");
		setInitialized(true);
//		dbService = new DbService();
//		reporter.report("Remote url from pom file is: " + remoteUrl);
//		logsFolder = folderName;
		try {

			//DesiredCapabilities capabilities = DesiredCapabilities.htmlUnitWithJs();
			DesiredCapabilities capabilities = DesiredCapabilities.htmlUnit();
			
			
//			System.out.println("remote machine for headless browser is: "+remoteUrl);
			webDriver = new RemoteWebDriver(new URL(remoteUrl + "/wd/hub"),
					capabilities);
			deleteCookiesAndCache();
//			setPageLoadTimeOut();
//			setScriptLoadTimeOut();
//			reporter.stopLevel();
		} catch (Exception e) {
			logger.error("Cannot register node or start the remote driver! ", e);
		}
	}

}
