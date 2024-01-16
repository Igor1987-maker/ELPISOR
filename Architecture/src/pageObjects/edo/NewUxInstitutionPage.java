package pageObjects.edo;

import org.apache.tools.ant.taskdefs.Sleep;
import org.openqa.selenium.WebElement;

import Enums.ByTypes;
import drivers.GenericWebDriver;
import pageObjects.GenericPage;
import services.TestResultService;
import testCategories.edoNewUX.HomePage;

public class NewUxInstitutionPage extends GenericPage {
		
	private static final String LEFT_SIDE_XPATH = "//td[@class='instTdLeft']";
	private static final String RIGHT_SIDE_XPATH = "//td[@class='instTdRight']";
	
	private static final String LEFT_XPATH_URL = "//td[@class='MarketingText']";
	private static final String LEFT_TEXT_URL = "This is the message area. It can contain any form of HTML, text , image etc. if the content is bigger than the assigned iframe area a scroll will appear";
	
	private static final String LEFT_XPATH_MESSAGE = "//div[1]";
	private static final String LEFT_TEXT_MESSAGE = "Text Message";
	
	private static final String LEFT_XPATH_DOCUMENT_HEADER = "//div[2]//h2";
	private static final String LEFT_TEXT_DOCUMENT_HEADER = "Document Manager";
	
	private static final String LEFT_XPATH_DOCUMENT_FILE = "//div[2]//a";
	private static final String LEFT_TEXT_DOCUMENT_FILE = "HELLO%20TEST.docx";
		
	//private static final String RIGHT_XPATH_URL = "//ul[contains(@class,'footerLinks')]/li[3]";
	private static final String RIGHT_XPATH_URL = "//*/a[contains(@class,'header-top-wrapper')]";
	
	private static final String RIGHT_TEXT_URL = "About Edusoft";
	
	private static final String RIGHT_XPATH_LINK = "//div[2]//a";
	private static final String RIGHT_TEXT_LINK = "Hyperlink";
	
	private static final String CorpUrl = "http://www.edusoftlearning.com/";
	
	
	public NewUxInstitutionPage(GenericWebDriver webDriver,
			TestResultService testResultService) throws Exception {
		super(webDriver, testResultService);
		// TODO Auto-generated constructor stub
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
	
	public void close() throws Exception{
		webDriver.closeNewTab(1);
		webDriver.switchToMainWindow();
		
	}
	
	public void verifyLeftBannerInnerText() {
		try {

			WebElement element = webDriver.findElementByXpath("/html/body/div/div/div[2]", ByTypes.xpath);
			if (!element.getText().contains("Your administrator has not yet assigned resources for this page"))
			
			{
			
				String actualText = webDriver.waitForElement(LEFT_SIDE_XPATH + LEFT_XPATH_MESSAGE, ByTypes.xpath).getText();
				testResultService.assertEquals(LEFT_TEXT_MESSAGE, actualText, "Verify Left Banner Text-based");
				
				actualText = webDriver.waitForElement(LEFT_SIDE_XPATH + LEFT_XPATH_DOCUMENT_HEADER, ByTypes.xpath).getText();
				testResultService.assertEquals(LEFT_TEXT_DOCUMENT_HEADER, actualText, "Verify Left Banner Document Manager header");
				
				actualText = webDriver.waitForElement(LEFT_SIDE_XPATH + LEFT_XPATH_DOCUMENT_FILE, ByTypes.xpath, "File link not found").getAttribute("href");
				testResultService.assertEquals(true, actualText.toLowerCase().contains(LEFT_TEXT_DOCUMENT_FILE.toLowerCase()), "Verify Left Banner Document Manager file");
								
				/*webDriver.switchToFrame(webDriver.waitForElement(LEFT_SIDE_XPATH + "/div/iframe",ByTypes.xpath));
				actualText = webDriver.waitForElement(LEFT_XPATH_URL, ByTypes.xpath).getText();
				testResultService.assertEquals(LEFT_TEXT_URL, actualText, "Verify Left Banner URL-based");*/
			}																									
			
			webDriver.switchToNewWindow();
		} catch (Exception e) {
				// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
	}
	
	public void verifyRightBannerInnerText() throws Exception{
		
		WebElement element = webDriver.findElementByXpath("/html/body/div/div/div[2]", ByTypes.xpath);
		if (!element.getText().contains("Your administrator has not yet assigned resources for this page"))
			
		{
			
			WebElement hyperLink = webDriver.waitForElement(RIGHT_SIDE_XPATH + RIGHT_XPATH_LINK, ByTypes.xpath,false,webDriver.getTimeout());
			
			if (hyperLink == null){
				for (int i=0;hyperLink== null && i<3;i++){
					hyperLink = webDriver.waitForElement(RIGHT_SIDE_XPATH + RIGHT_XPATH_LINK, ByTypes.xpath,false,webDriver.getTimeout());
				}
			}
			
			String actualText = hyperLink.getText();
			String actualHref = hyperLink.getAttribute("href");
			testResultService.assertEquals(RIGHT_TEXT_LINK, actualText, "Verify Right Banner Link-based");
			testResultService.assertEquals(CorpUrl, actualHref, "Verify Right Banner Link-based URL");
					
			webDriver.switchToFrame(webDriver.waitForElement(RIGHT_SIDE_XPATH + "/div/iframe",ByTypes.xpath));
			
		/*	WebElement element =  webDriver.waitForElement(RIGHT_XPATH_URL, ByTypes.xpath,false,webDriver.getTimeout());
			
			if (element == null){
				for (int i=0;element== null && i<3;i++){
					element =  webDriver.waitForElement(RIGHT_XPATH_URL, ByTypes.xpath,false,webDriver.getTimeout());
				}
			}
			actualText = element.getText();
			testResultService.assertEquals(RIGHT_TEXT_URL, actualText, "Verify Right Banner Text");
			*/
		}
			webDriver.switchToNewWindow();
		
	}
		
	
}
