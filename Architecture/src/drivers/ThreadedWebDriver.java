package drivers;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ThreadGuard;

public class ThreadedWebDriver implements Runnable {

	private GenericWebDriver webDriver;

	public ThreadedWebDriver(GenericWebDriver webDriver) {
		super();
		this.webDriver = webDriver;
	}

	@Override
	public void run() throws TimeoutException {
		try {
			doAction();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void doAction() throws TimeoutException, Exception {
		ThreadGuard threadGuard = new ThreadGuard();
		threadGuard.protect(webDriver.getWebDriver());
		webDriver.openUrl("http://edosr.qa.com/omers.aspx");
		System.out.println("Open browser:"+System.currentTimeMillis());
		webDriver.closeBrowser();
	}

}
