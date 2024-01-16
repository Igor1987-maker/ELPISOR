package drivers;

import java.net.URL;

import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
//import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.ProfilesIni;
//import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
//import org.openqa.selenium.remote.server.FirefoxDriverProvider;
import org.springframework.stereotype.Service;
//import org.openqa.selenium.firefox.MarionetteDriver;

import Enums.ByTypes;
import services.DbService;

@Service
public class FirefoxWebDriver extends GenericWebDriver {

		
	@Override
	public void init(String remoteUrl, boolean useProxy) throws Exception {
		this.timeout = 10;
		setBrowserName("firefox");
		setInitialized(true);
		dbService = new DbService();
		
		reporter.report("Remote url from pom file is: " + remoteUrl);
		
		try {
			
			ProfilesIni profile = new ProfilesIni();
				
			//FirefoxProfile firefoxProfile = new FirefoxProfile(); //---------PROD
			//FirefoxProfile firefoxProfile = profile.getProfile("automation"); // ------- FF47+
			FirefoxOptions options = new FirefoxOptions();
			//options.setProfile(firefoxProfile);
			//options.setCapability(CapabilityType.SUPPORTS_FINDING_BY_CSS, true);
						
		//	DesiredCapabilities capabilities = DesiredCapabilities.firefox();
							
		//	capabilities.setCapability(CapabilityType.SUPPORTS_FINDING_BY_CSS, true); //---------PROD
			
		//	capabilities.setCapability(FirefoxDriver.PROFILE, firefoxProfile); // --------PROD
			//options.setBinary("C:\\Program Files\\Mozilla Firefox\\firefox.exe"); 
			//options.addArguments("--headless");
			webDriver = new RemoteWebDriver(new URL(remoteUrl + "/wd/hub"),options);
			//webDriver = new RemoteWebDriver(capabilities);
			sleep(5);
			setPageLoadTimeOut();
			setScriptLoadTimeOut();
			reporter.stopLevel();
		
		} catch (Exception e) {
			logger.error("Cannot register node or start the remote driver! ", e.toString());
		}
	}

	@Override
	public void waitForElementAndClick(String idValue, ByTypes byType)
			throws Exception {
		try {
			waitForElement(idValue, byType, this.timeout, true).click();
		} catch (InvalidElementStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void clickOnElement(WebElement element) {
		// td.sendKeys(Keys.ENTER);
		JavascriptExecutor executor = (JavascriptExecutor) webDriver;
		executor.executeScript("arguments[0].click();", element);

	}

	@Override
	public void highlightElement(WebElement element) {
		// do nothing if fi refox

	}

	@Override
	public void scrollToElement(WebElement element, int yOffset)
			throws Exception {
		int y = element.getLocation().y;
		scrollToTo(0, y - 400);
	}

	// }

}
