package tests.igors;

import tests.misc.EdusoftWebTest;

import org.junit.Test;
import org.openqa.selenium.WebElement;

import Enums.ByTypes;
import Interfaces.Sanity;
import Interfaces.TestCaseParams;


public class Training1 extends EdusoftWebTest {
	/* Israel Test */
	
	
	
		@Test
		public void checkEdusoftSite() throws Exception {

			
			report.startStep("Open Edusoft Marketing Site");
			webDriver.openUrl("http://www.edusoftlearning.com/");		
			
			report.startStep("Enter 'English D' into search field");
			WebElement searchField = webDriver.waitForElement("s", ByTypes.name);
			searchField.sendKeys("English D");
			
			report.startStep("Press Search button");
			WebElement SearchButton = webDriver.waitForElement("searchsubmit", ByTypes.id);
			clickonElement(SearchButton);
			
			report.startStep("Wait 3 seconds");
			sleep(3);
			
			report.startStep("Retrieve text of search results header");
			WebElement ReaultsHeader = webDriver.waitForElement("splashWide", ByTypes.id);
			String HeaderText = ReaultsHeader.getText();
			
			report.startStep("Verify search results header text is 'Search results'");
			testResultService.assertEquals("Search results", HeaderText);
			
			
			report.startStep("Verify second title text");
			WebElement secondLinkTitle = webDriver.waitForElement("//*[@id='inner_content']/div/p[4]/a", ByTypes.xpath);
			String expectedTitle = "Edusoft Participates in LATAM Conferences";
			verifyTextinElement(secondLinkTitle,expectedTitle);
			
	
			report.startStep("Verify second title text");
			secondLinkTitle = webDriver.waitForElement("//*[@id='inner_content']/div/p[7]/a", ByTypes.xpath);
			expectedTitle = "Professional Development Expanded in Kazakhstan";
			verifyTextinElement(secondLinkTitle,expectedTitle);
			
			
		}

		private void verifyTextinElement(WebElement secondLinkTitle,String expectedTitle) {
			String actualTitle = secondLinkTitle.getText();
			testResultService.assertEquals(expectedTitle, actualTitle, "The titles not match");
			
		}

		private void clickonElement(WebElement SearchButton) {
			// TODO Auto-generated method stub
			SearchButton.click();
		}
		
		
}
