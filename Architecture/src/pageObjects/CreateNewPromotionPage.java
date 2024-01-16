package pageObjects;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Enums.ByTypes;
import drivers.GenericWebDriver;
import pageObjects.tms.TmsHomePage;
import services.TestResultService;

public class CreateNewPromotionPage extends GenericPage{
	
	@FindBy(xpath = "//span/a[text()='Template 2']")
	public WebElement secondTemplate;
	
	@FindBy(xpath = "//*[@id='home__marketingMagTitle']")
	public WebElement promotionTitle;
	
	@FindBy(xpath = "//*[@id='home__marketingMagTypeTitle']")
	public WebElement promotionSubTitle;
	
	@FindBy(xpath = "//*[@id='home__marketingMagText']")
	public WebElement promotionText;
	
	@FindBy(xpath = "//*[@id='home__marketingMagBtn']")
	public WebElement promotionButton;
	
	@FindBy(xpath = "//*[@id='slideLinkUrl']")
	public WebElement promotionLink;
	
	@FindBy(xpath = "//span/a[text()='Save']")
	public WebElement saveButton;
	
	@FindBy(xpath = "//span/a[text()='Cancel']")
	public WebElement cancelButton;
	
	TmsHomePage tmsHomePage = new TmsHomePage(webDriver, testResultService);

	public CreateNewPromotionPage(GenericWebDriver webDriver, TestResultService testResultService) throws Exception {
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
	
	public void createNewPromotion(String title, String subTitle, String text, String btnText, String link) throws Exception {
		
		report.startStep("Press on New button");
		tmsHomePage.clickOnPromotionAreaMenuButton("New");
		Thread.sleep(1000);
	
		report.startStep("Select and populate slide template");
		webDriver.switchToNewWindow();
		Thread.sleep(4000);
		webDriver.ClickElement(secondTemplate); 
		Thread.sleep(2000);
		populatePromotionSlideValue(promotionTitle, title);
		populatePromotionSlideValue(promotionSubTitle, subTitle);
		populatePromotionSlideValue(promotionText, text);
		populatePromotionSlideValue(promotionButton, btnText);
		populatePromotionSlideValue(promotionLink, link);
		Thread.sleep(1000);
	
		report.startStep("Add custom image to Slide");
		String fileName = "promotionImageForAutomationTC.jpg"; 
		String bodyFile = "\\\\"+configuration.getGlobalProperties("logserverName")+"\\AutoLogs\\ToolsAndResources\\Shared\\" + fileName;
		String elementId = "SelectFile"; 
		tmsHomePage.addCustomImageToSlide(bodyFile, elementId);	// add click ok
		Thread.sleep(1000);
		
		report.startStep("Save and Close Slide creation window");	
		clickSave();
		Thread.sleep(1000);
		clickCancel();
		webDriver.switchToMainWindow();
		tmsHomePage.switchToMainFrame();
		Thread.sleep(1000);
	}
	
	private void populatePromotionSlideValue(WebElement element, String value) throws Exception {
		element.clear();
		webDriver.SendKeys(element, value);
	}
	
	public void clickSave() throws InterruptedException {
		//webDriver.waitForElement("//span/a[text()='Save']", ByTypes.xpath).click();
		webDriver.ClickElement(saveButton);
		webDriver.closeAlertByAccept();
		Thread.sleep(1000);	
	}
	
	public void clickCancel() throws InterruptedException {
		//webDriver.waitForElement("//span/a[text()='Cancel']", ByTypes.xpath).click();
		webDriver.ClickElement(cancelButton);
		Thread.sleep(2000);
	}

}
