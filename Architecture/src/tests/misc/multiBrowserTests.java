package tests.misc;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import drivers.ChromeWebDriver;
import drivers.FirefoxWebDriver;
import drivers.GenericWebDriver;


@RunWith(Parameterized.class)
public class multiBrowserTests  {

	GenericWebDriver driver;
	private GenericWebDriver browser;

	public multiBrowserTests(GenericWebDriver browser) {
		this.browser = browser;
	}

	@Parameters
	public static Collection<Object[]> data() {
		Object[][] data = new Object[][] { { new ChromeWebDriver() },
				{ new FirefoxWebDriver() } };

		return Arrays.asList(data);
	}
	
	 @Before
	    public void setUp(){
	        driver = browser;
	    }
	
	@Test
	public void testMultu()throws Exception{
		driver.openUrl("www.google.co.il");
	}

}
