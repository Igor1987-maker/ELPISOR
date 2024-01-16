package pageObjects.edo;

import org.openqa.selenium.WebElement;

import Enums.ByTypes;
import drivers.GenericWebDriver;
import pageObjects.GenericPage;
import services.TestResultService;

public class NewUxGrammarBook extends GenericPage {

	private static final String X_Button_Xpath = "//a[@class='modal-close']";
	
	public NewUxGrammarBook(GenericWebDriver webDriver,
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
	
	public void clickOnStartLearning() throws Exception{
		webDriver.clickOnElement(webDriver.waitForElement("//div[@class='grammarBookEnterBT']/a", ByTypes.xpath,3,false));
		
	}
	
	public void verifyGrammarBookHeader() throws Exception{
		webDriver.waitUntilElementAppears("//h2", 5);
		String header = webDriver.waitForElement("//h2", ByTypes.xpath).getText();
		testResultService.assertEquals("Grammar Book Menu", header, "Verify Grammar Book Header");
	}
	
	public void navigateToFirstLesson() throws Exception{
		webDriver.waitForElement("//img[@id='icon56']", ByTypes.xpath).click();
	}

	public void navigateToLesson(String topic, String subTopic, String lesson) throws Exception{
		webDriver.clickOnElement(webDriver.waitForElement("//td[@class='menuItem'][text()='"+topic+"']", ByTypes.xpath));
		webDriver.clickOnElement(webDriver.waitForElement("//td[@class='menuChildItem'][text()='"+subTopic+"']", ByTypes.xpath));
		webDriver.clickOnElement(webDriver.waitForElement("//td[@class='componentsItem'][text()='"+lesson+"']", ByTypes.xpath));
		
	}
	
	public void switchToContentInGrammarBook() throws Exception {
		webDriver.switchToFrame("contFrame");

	}
	
	public void navigateBackToGrammarBookMenu() throws Exception{
		webDriver.clickOnElement(webDriver.waitForElement("//div[@onclick='flipToMenu()']", ByTypes.xpath));
		
	}
	
	public void close() throws Exception {
		webDriver.switchToMainWindow();
		
		WebElement Xbuton = webDriver.waitForElement(X_Button_Xpath,ByTypes.xpath, "Grammar Book X button");
		Xbuton.click();

	}
	
	public void switchToSeeExplanationPopUp() throws Exception {
		webDriver.switchToNewWindow();

	}
	
	public void verifySeeExplanationHeader() throws Exception{
		String header = webDriver.waitForElement("//div[@class='PopupTitle']", ByTypes.xpath, false, webDriver.getTimeout()).getText();
		testResultService.assertEquals("See Explanation", header, "Verify See Explanation Header");
	}
	
	public void verifySeeExplanationSubTopic(String subTopic) throws Exception{
		WebElement element = webDriver.waitForElement("//span[@class='num0'][text()[contains(.,'" + subTopic + "')]]", ByTypes.xpath, false, webDriver.getTimeout());
		if (element == null) {
			testResultService.addFailTest("Text is not displayed in See Explanation", false, true);
		}
	}
	
	public void checkThatTextIsDisplayed() throws Exception {
		WebElement element = webDriver.waitForElement("//div[@class='seeTextContainer']",ByTypes.xpath, false, webDriver.getTimeout());
		if (element == null) {
			testResultService.addFailTest("Text is not displayed", false, true);
		}
		
	}
	
	
}
