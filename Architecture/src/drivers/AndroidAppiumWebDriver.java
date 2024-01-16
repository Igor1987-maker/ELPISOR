package drivers;

import java.net.URL;

//import org.apache.bcel.generic.CPInstruction;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.touch.TouchActions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;

public class AndroidAppiumWebDriver extends GenericWebDriver {

	@Override
	public void init(String remoteUrl, boolean useProxy) throws Exception {

		try {
			try {
				//DesiredCapabilities capabilities = DesiredCapabilities.android();
				/*ChromeOptions chromeOptions = new ChromeOptions();
				chromeOptions.setExperimentalOption("androidPackage", "com.android.chrome");

				capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);

				capabilities.setPlatform(Platform.ANDROID);

				webDriver = new ChromeDriver(chromeOptions);*/
				
				DesiredCapabilities capabilities = new DesiredCapabilities();
	            
	            capabilities.setCapability("deviceName", "Android");
	            capabilities.setCapability(CapabilityType.PLATFORM, "Android");
	            capabilities.setCapability(CapabilityType.BROWSER_NAME, "Chrome");
	            capabilities.setCapability("platformName", "Android");
	            capabilities.setCapability("platformVersion", "4.4.2");
	            
	            capabilities.setCapability("browserName", "Chrome");
	            capabilities.setCapability("appPackage", "com.android.chrome");
				
	            webDriver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
	            
	            //webDriver = new RemoteWebDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
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
	public void scrollToElement(WebElement element, int yOffset) throws Exception {
		// TODO Auto-generated method stub
		executeJsScript("arguments[0].scrollIntoView(true);", element);
	}

}
