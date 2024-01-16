package pageObjects.edo;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import drivers.GenericWebDriver;
import pageObjects.GenericPage;
import services.TestResultService;

public class NewUxDictionaryPage extends GenericPage{
	
	@FindBy(xpath = "//input[@value='1']")
	public WebElement enableButton;
	
	@FindBy(xpath = "//input[@value='0']")
	public WebElement disableButton;
	
	@FindBy(xpath = "//input[@value='Save']")
	public WebElement saveButton;
	
	@FindBy(xpath = "//a[@id='Cancel']")
	public WebElement cancelButton;
	
	public NewUxDictionaryPage(GenericWebDriver webDriver, TestResultService testResultService) throws Exception {
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
	
	public void clickEnableButton(){
		webDriver.ClickElement(enableButton);
	}
	
	public void clickDisableButton(){
		webDriver.ClickElement(disableButton);
	}
	
	public boolean validateEnableChecked(){
		String enableAttr = enableButton.getAttribute("checked");
		if (enableAttr == null) {
			return false;
		}
		testResultService.assertEquals("true", enableAttr, "Enable Button is not Checked");
		return enableAttr.equals("true");
	}
	
	public boolean validateDisableChecked(){
		String disableAttr = disableButton.getAttribute("checked");
		if(disableAttr == null) {
			return false;
		}
		testResultService.assertEquals("true", disableButton.getAttribute("checked"), "Disable Button is not Checked");
		return disableAttr.equals("true");
	}
	
	public void clickSave(){
		webDriver.ClickElement(saveButton);
	}
	
	public void clickCancel(){
		webDriver.ClickElement(cancelButton);
	}

	
}
