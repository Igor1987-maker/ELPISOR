package drivers;



import java.net.URL;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.internal.TouchAction;
import org.openqa.selenium.interactions.touch.TouchActions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.stereotype.Service;

import services.DbService;

@Service
public class AndroidWebDriver extends GenericWebDriver {

	@Override
	public void init(String remoteUrl, boolean useProxy) throws Exception {
	

		try {
			try {
				DesiredCapabilities capabilities = DesiredCapabilities.chrome();
				ChromeOptions chromeOptions = new ChromeOptions();
				chromeOptions.setExperimentalOption("androidPackage",
						"com.android.chrome");

				capabilities.setCapability(ChromeOptions.CAPABILITY,
						chromeOptions);
				
				capabilities.setPlatform(Platform.ANDROID);

				webDriver = new ChromeDriver(chromeOptions);
				setBrowserName("android");
				setInitialized(true);
				
				setPageLoadTimeOut();
				setScriptLoadTimeOut();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


			setInitialized(true);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void swipe() {

		TouchActions touchActions = new TouchActions(webDriver);
		// touchActions.m
	}

	@Override
	public void maximize() {
		// TODO Auto-generated method stub

	}

	
	
	@Override
	public String printScreen(String message) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void scrollToElement(WebElement element, int yOffset)
			throws Exception {
		// TODO Auto-generated method stub
		executeJsScript("arguments[0].scrollIntoView(true);", element);
	}
	


}
