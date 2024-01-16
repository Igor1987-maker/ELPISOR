package tests.igors;

import tests.misc.EdusoftWebTest;

import org.junit.Test;
import org.openqa.selenium.WebElement;

import Enums.ByTypes;
import Interfaces.Sanity;
import Interfaces.TestCaseParams;

public class Training2 extends EdusoftWebTest {
	
	
		@Test
		public void checkEdusoftSite() throws Exception {

			report.startStep("Open Edusoft Marketing Site");
			webDriver.openUrl("http://www.edusoftlearning.com/"); 
					
			report.startStep("Find element");
			WebElement searchField = webDriver.waitForElement("s", ByTypes.name);
			
			report.startStep("Enter 'English Discoveries' into search field");
			searchField.sendKeys("English Discoveries");
			
			report.startStep("Press Search button");
			WebElement SearchButton = webDriver.waitForElement("searchsubmit", ByTypes.id);
			SearchButton.click();
			
			report.startStep("Wait 3 seconds");
			sleep(3);
			
			report.startStep("Retrieve text of search results header");
			WebElement ResultElement = webDriver.waitForElement("splashWide", ByTypes.id);
			String Text = ResultElement.getText();
			
			report.startStep("Verify search results header text is 'Search results'");
			testResultService.assertEquals("Search results", Text);
			
			
			
		}
		
		
}
