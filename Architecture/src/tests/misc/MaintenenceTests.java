package tests.misc;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.thoughtworks.xstream.mapper.PackageAliasingMapper;

import services.PageHelperService;
import testCategories.Maintenence;

public class MaintenenceTests extends EdusoftBasicTest {
	
	int retainPeriodInDays = 14;
	
	PageHelperService pageHelper = new PageHelperService();
	
	@Test
	@Category(Maintenence.class)
	public void deleteLogs()throws Exception{
		setPeriod();
		netService.clearLogFiles(retainPeriodInDays);
	}
	
	@Test
	@Category(Maintenence.class)
	public void clearWebDriverTemp()throws Exception{
		setPeriod();
		netService.clearWebDriverTempFolder(retainPeriodInDays);
	}
	
	private int setPeriod() {
		
		String period = configuration.getAutomationParam("retainPeriodInDays", "retainPeriodInDays");
		if (period!=null && !period.isEmpty()) retainPeriodInDays = Integer.valueOf(period);
				
		return retainPeriodInDays;
		
	}

}
