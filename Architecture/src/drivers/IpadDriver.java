package drivers;

import java.net.URL;

//import java.util.function.Function;
//import com.google.common.base.Function;

//import org.apache.maven.model.superpom.SuperPomProvider;
import org.openqa.selenium.Alert;
import org.openqa.selenium.Platform;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import services.DbService;
@Service
public class IpadDriver extends GenericWebDriver{
	
	@Override
	public void init(String remoteUrl, boolean useProxy) throws Exception {
		setBrowserName("safariIpad");
		setInitialized(true);
		dbService = new DbService();
		reporter.report("Remote url from pom file is: " + remoteUrl);
		// logsFolder = folderName;
		try {
			if (remoteUrl == null) {
				// remoteUrl = configuration.getProperty("remote.machine");
			}
			// report.report("Initializing SafariWebDriver",
			// Reporter.EnumReportLevel.CurrentPlace);

			// DesiredCapabilities capabilities = new
			// DesiredCapabilities("firefox", "29.0.1", Platform.WINDOWS);

		
	
			


	
			
			// webDriver = new RemoteWebDriver( capabilities);
			// setPageLoadTimeOut();
			
			setPageLoadTimeOut();
			setScriptLoadTimeOut();
			reporter.stopLevel();
		} catch (Exception e) {
			logger.error("Cannot register node or start the remote driver! ", e);
		}
	}

	@Override
	public void hoverOnElement(WebElement element) throws Exception {
		// TODO Auto-generated method stub
		String mouseOverScript = "if(document.createEvent){var evObj = document.createEvent('MouseEvents');evObj.initEvent('mouseover',true, false); arguments[0].dispatchEvent(evObj);} else if(document.createEventObject) { arguments[0].fireEvent('onmouseover');}";
		executeJsScript(mouseOverScript, element);
	}

	@Override
	public void scrollToElement(WebElement element) throws Exception {
		// TODO Auto-generated method stub
		executeJsScript("arguments[0].scrollIntoView(true);", element);
	}

	@Override
	public void scrollToElement(WebElement element, int yOffset)
			throws Exception {
		// TODO Auto-generated method stub
		executeJsScript("arguments[0].scrollIntoView(true);", element);
	}

	@Override
	public String getPopUpText() throws Exception {
		Alert alert = null;
		try {
			WebDriverWait wait = new WebDriverWait(webDriver, getTimeout());
			
			wait.until(ExpectedConditions.alertIsPresent());
			
			alert = webDriver.switchTo().alert();

		} catch (UnhandledAlertException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (alert != null) {
			return alert.getText();
		} else
			return null;

	}


}
