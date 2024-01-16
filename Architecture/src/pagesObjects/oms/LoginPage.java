package pagesObjects.oms;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import drivers.GenericWebDriver;
import pageObjects.GenericPage;
import pageObjects.toeic.toeicStartPage;
import services.TestResultService;


public class LoginPage extends GenericPage {

	@FindBy(id = "userNameLogin")
	public WebElement userName;
	
	@FindBy(id = "passwordLogin")
	public WebElement password;
	
	@FindBy(css = "button[type='submit']")
	public WebElement loginButton;
	
	@FindBy(xpath = "//p[@class='ng-scope']")
	public WebElement welcomeMessage;
	
	public LoginPage(GenericWebDriver webDriver,
			TestResultService testResultService) throws Exception {
		super(webDriver, testResultService);
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
	
	public HomePage enterCredentials(String name,String userPassword) throws Exception {
		report.startStep("Filling teacher credentials");
	webDriver.waitForElementByWebElement(userName, "name", true, 10000);
		PageUtils.type(name,userName);
		PageUtils.type(userPassword,password);
		loginButton.click();
		Thread.sleep(7000);
		return new HomePage(webDriver, testResultService);
		
	}

	public void verifyUserIsLoggedOut() throws Exception {
		report.startStep("Verify message on login page after user loged out");
		Thread.sleep(2000);
		webDriver.waitForElementByWebElement(welcomeMessage, "User not logged out", true, 10000);
		Thread.sleep(1000);
		String invitationToLogin = welcomeMessage.getText().trim();
		//textService.assertEquals(invitationToLogin,"Please Login To The System");
		textService.assertEquals("User loged out incorrectly", "Please Login To The System", invitationToLogin);
	}


}
