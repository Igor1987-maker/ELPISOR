package pageObjects.edo;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import drivers.GenericWebDriver;
import pageObjects.GenericPage;
import services.PageHelperService;
import services.TestResultService;

public class NewUxDictionaryPopUpPage extends GenericPage{
	
	@FindBy(xpath = "//input[@type='text']")
	public WebElement wordElement;
	
	@FindBy(xpath = "//select[@id='selectLanguage']//option[@selected]")
	public WebElement languageElement;
	
	@FindBy(xpath = "//a[@title='Close panel']")
	public WebElement closeButton;

	public NewUxDictionaryPopUpPage(GenericWebDriver webDriver, TestResultService testResultService) throws Exception {
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
	
	public void validateDictionaryPopUp(String word, String language) throws Exception {
		//webDriver.switchToFrame("DictionaryIF");
		//webDriver.switchToFrame("wd_SearchFrame");
		testResultService.assertEquals(word, wordElement.getAttribute("value"),"Dictionary Word is Inccorect");
		testResultService.assertEquals(language, languageElement.getText(),"Dictionary Language is Inccorect");
		//webDriver.switchToMainWindow();
	}
	
	public void closeDictionaryPopup(){
		webDriver.ClickElement(closeButton);
	}
	
	

}
