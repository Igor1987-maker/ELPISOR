package pageObjects;

import Enums.ByTypes;
import drivers.GenericWebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import pageObjects.tms.TmsHomePage;
import services.TestResultService;

import java.util.List;

public class TMSPromotionPage extends GenericPage{
	
	@FindBy(xpath = "//tr[contains(@class,'ui-widget-content jqgrow ui-row-ltr')]//td[2]")
	public List<WebElement> promotionNameTable;
	
	@FindBy(xpath = "//tr[contains(@class,'ui-widget-content jqgrow ui-row-ltr')]//td[3]")
	public List<WebElement> promotionStatusTable;
	
	TmsHomePage tmsHomePage = new TmsHomePage(webDriver, testResultService);

	public TMSPromotionPage(GenericWebDriver webDriver, TestResultService testResultService) throws Exception {
		super(webDriver, testResultService);
		// TODO Auto-generated constructor stub
		PageFactory.initElements(webDriver.getWebDriver(), this);	
	}

	@Override
	public GenericPage waitForPageToLoad() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GenericPage OpenPage(String url) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void goToPromotionArea() throws Exception {
		tmsHomePage.switchToMainFrame();
		tmsHomePage.clickOnSettings();
		Thread.sleep(1000);
		tmsHomePage.clickOnPromotionArea();
		Thread.sleep(2000);
	}
	
	public void publishPromotion(String promotionName) throws Exception {
		List<WebElement> promotionNameTable = webDriver.getElementsByXpath("//tr[contains(@class,'ui-widget-content jqgrow ui-row-ltr')]//td[2]");
		Thread.sleep(1000);
		List<WebElement> promotionStatusTable = webDriver.getElementsByXpath("//tr[contains(@class,'ui-widget-content jqgrow ui-row-ltr')]//td[3]");
		try {
			boolean isPromotionFound = false;
			for (int i = 0; i <promotionNameTable.size(); i++) {
				if (promotionNameTable.get(i).getText().equals(promotionName)) {
					webDriver.ClickElement(promotionStatusTable.get(i));
					webDriver.ClickElement(promotionStatusTable.get(i));
					Thread.sleep(1000);
					tmsHomePage.clickOnPromotionAreaMenuButton("Publish");
					Thread.sleep(1000);
					isPromotionFound = true;
					break;
				} 
			}
			if (!isPromotionFound) {
				testResultService.addFailTest("The Promotion: " +promotionName+ " is not found in the Promotions list");
			}
		} catch (Exception e) {
			testResultService.addFailTest("FAIL: Publish Promotion. Error: " + e);
		}
	}

	public void deletePromotion(String promotionName)throws Exception {


		try {
			List<WebElement> promotionNameTable = webDriver.getElementsByXpath("//tr[contains(@class,'ui-widget-content jqgrow ui-row-ltr')]//td[2]");
			boolean isPromotionFound = false;
			for (int i = 0; i < promotionNameTable.size(); i++) {
				if (promotionNameTable.get(i).getText().equals(promotionName)) {
					webDriver.ClickElement(promotionNameTable.get(i));
					webDriver.ClickElement(promotionNameTable.get(i));
					tmsHomePage.clickOnPromotionAreaMenuButton("Hide");
					Thread.sleep(2000);
					String status = webDriver.waitUntilElementAppears("//span[text()='Delete']", ByTypes.xpath, 8).getAttribute("disabled");
					if (status!=null&&status.equalsIgnoreCase("disabled")) {
						webDriver.ClickElement(promotionNameTable.get(i));
						tmsHomePage.clickOnPromotionAreaMenuButton("Hide");
						Thread.sleep(2000);
						tmsHomePage.clickOnPromotionAreaMenuButton("Delete");
					}else {
						tmsHomePage.clickOnPromotionAreaMenuButton("Delete");
					}
					Thread.sleep(2000);
					isPromotionFound = true;
					break;
				}
			}
			
			if (!isPromotionFound) {
				testResultService.addFailTest("The Promotion: " +promotionName+ " was not found in the promotions list");
			}
		} catch (Exception e) {
			testResultService.addFailTest("FAIL: Delete Promotion. Error: " + e);
		}	
	}
	
	public boolean verifyPromotionIsNotDisplayedInTheTable(String promotionName) throws Exception {
		try {
			List<WebElement> promotionNameTable = webDriver.getElementsByXpath("//tr[contains(@class,'ui-widget-content jqgrow ui-row-ltr')]//td[2]");
			boolean promotionDisplayed = false;
			for (int i = 0; i <promotionNameTable.size(); i++) {
				if (promotionNameTable.get(i).getText().equals(promotionName)) {
					promotionDisplayed = true;
					break;
				}
			}
	
			testResultService.assertEquals(false, promotionDisplayed, "Check the Promotion: " +promotionName+ " is not displayed in the Promotions List.");
			if (promotionDisplayed) {
				return false;
			} else {
				return true;
			}
		} catch (Exception e) {
			testResultService.addFailTest("FAIL: Check Promotion: " +promotionName+ " is not displayed in the Promotions List. Error: " + e);
			return false;
		}
	}
}
