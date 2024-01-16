package pageObjects.tms;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import drivers.GenericWebDriver;
import pageObjects.GenericPage;
import services.TestResultService;

public class SelfRegistrationPage extends GenericPage {
	
	@FindBy(name = "btnSubmit")
	public WebElement submitButton;
	
	@FindBy(xpath = "//span[@id='err']")
	public WebElement errorMessage;
	
	public SelfRegistrationPage(GenericWebDriver webDriver, TestResultService testResultService) throws Exception {
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

	public void clickSubmit() {
		webDriver.ClickElement(submitButton);
	}
	
	public void checkErrorMessage(String expectedErrMessage,String fileName) throws Exception {
		String actualErrMessage = errorMessage.getText();
		
		if (actualErrMessage.isEmpty() || actualErrMessage=="")
			testResultService.assertEquals(true, webDriver.getUrl().contains(expectedErrMessage), "The Error Message is Incorrect: " + fileName);
		else
			testResultService.assertEquals(expectedErrMessage, actualErrMessage, "The Error Message is Incorrect: " + fileName);
	}
}
