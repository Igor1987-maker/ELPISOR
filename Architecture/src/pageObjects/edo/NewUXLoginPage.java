package pageObjects.edo;

import Enums.ByTypes;
import drivers.GenericWebDriver;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pageObjects.GenericPage;
import pageObjects.tms.TmsHomePage;
import services.TestResultService;

public class NewUXLoginPage extends GenericPage {

	//private static final String FOOTER_LOGO_XPATH = "//*[@id='mainCtrl']/footer/div[2]/a/img";
	private static final String FOOTER_LOGO_XPATH = "//footer//div[contains(@class,'ownerLogo')]/a/img";
	//private static final String LOGOUT_XPATH = "//a[@ng-click='logout()']";
	private static final String FORGOT_PASSWORD = "Forgot your password?";
	private static final String REGISTER = "Register";
	private static final String LOGOUT_XPATH = "//div[@id='learning__settingsMenu']/div/ul/li/div/ul/li[3]/a";
	private static final String SUBMIT = "submit1";
	private static final String ErrorUserName = "siteLogin__messageText";
	private static final String SUBMIT_FORGOT_PASSWORD = "Submit";
	public static final String GENERAL_BANNER_PATH = "Images/General/skins/ed/defaultInstitutionHeader.jpg";
	
	protected int smallTimeOut = 5;
	
	public NewUXLoginPage(GenericWebDriver webDriver,
			TestResultService testResultService) throws Exception {
		super(webDriver, testResultService);
		// TODO Auto-generated constructor stub
	}

	private String userNameTextbox = "userName";
	private String passwordTextbox = "password";
	private String institutionTextbox = "institutions";

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

	public void enterUserName(String userName) throws Exception {
		try {
			enterUserName(userName, true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void enterInstitution(String institution) throws Exception {
		try {
			//clearPassword();
			WebElement element = webDriver.waitForElement(passwordTextbox, ByTypes.name);
			element.clear();
			
			webDriver.waitForElement(institutionTextbox, ByTypes.name).sendKeys(
					institution);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void enterUserName(String userName, boolean clear) throws Exception {
		
		try {
			//WebElement element = webDriver.waitForElement(userNameTextbox, ByTypes.name);
			WebElement element = webDriver.waitUntilElementAppearsAndReturnElementByType(userNameTextbox,
					ByTypes.name, 10);
			if (clear && element!=null) {
				element.clear();
			}
			element.sendKeys(userName);
			//webDriver.sendKey(Keys.TAB);
		} catch (Exception e) {
			// TODO Auto-generated catch block

			testResultService.addFailTest("Problem entering userName: "
					+ e.toString());
		}
	}

	public void enterPassowrd(String password) throws Exception {
		try {
			WebElement element = webDriver.waitForElement(passwordTextbox, ByTypes.name);
			
			if (element!=null)
				element.clear();
		
			element.sendKeys(password);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void clickOnSubmit() throws Exception {
		try {
			webDriver.waitForElement(SUBMIT, ByTypes.id).click();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void clearUserName() throws Exception {
		try {
			webDriver.waitForElement(userNameTextbox, ByTypes.name).clear();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			testResultService.addFailTest("Problem clearing user name");
		}

	}

	public void clearPassword() throws Exception {
		try {
			webDriver.waitForElement(passwordTextbox, ByTypes.name).clear();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean isSubmitButtonEnabled() throws Exception {

		// boolean result = true;
		try {
			WebElement element=null;
			for (int i=0;i<=5 && element==null;i++) {
				element = webDriver.waitForElement(SUBMIT, ByTypes.id,1, false);
			}
			// System.out.println("Disabled:"
			// +element.getAttribute("disabled"));
			if (element.getAttribute("disabled").equals("true")) {
			//if (element!=null) {
				return false;
			} else {
				return true;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			testResultService.addFailTest("Submit button not enabled", false,
					true);
		}
		return false;
	}
	
	public void isSubmitButtonDisabled() throws Exception {

		// boolean result = true;
		

			WebElement element = webDriver.waitForElement(SUBMIT, ByTypes.id,
					webDriver.getTimeout(), false);
	}

	public void PressOnLogin () throws Exception{
		
		webDriver.waitForElement(SUBMIT, ByTypes.id).click();
		
	}
	
	public void ValidateCabaErrorMessages(String ErrorMessage) throws Exception {
		
		webDriver.waitForElement(ErrorMessage, ByTypes.className);
	}

	public boolean isLogOutDisplayed() throws Exception {
		try {
			webDriver.waitForElement("//li[contains(@class, 'settingsMenu__personal')]", ByTypes.xpath).click();
			WebElement webElement =webDriver.waitForElement("//li[contains(@class, 'settingsMenu__listItem_logout')]/a", ByTypes.xpath, true, 15);
			
			//WebElement webElement = webDriver.waitForElement(LOGOUT_XPATH, ByTypes.xpath);
			return webElement.isDisplayed();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;

	}

	public void clickOnLogout() throws Exception {
		webDriver.waitForElement(LOGOUT_XPATH, ByTypes.xpath).click();
	}

	public NewUxHomePage loginAsStudent() throws Exception {
		return loginAsStudent(configuration.getProperty("student"),
				configuration.getProperty("student.user.password"));
	}

	public NewUxHomePage loginAsStudent(String userName, String password)
			throws Exception {
		enterUserName(userName);
		enterPassowrd(password);
		clickOnSubmit();

		return new NewUxHomePage(webDriver, testResultService);
	}

	public TmsHomePage enterSchoolAdminUserAndPassword() throws Exception {

		enterUserName(configuration.getProperty("schoolAdmin.user"));
		Thread.sleep(1500);
		enterPassowrd(configuration.getProperty("schoolAdmin.pass"));
		
		clickOnSubmit();
		
		return new TmsHomePage(webDriver, testResultService);
	}

	public TmsHomePage enterSchoolAdminUserAndPassword(String userName, String userID) throws Exception {

		enterUserName(userName);
		Thread.sleep(1500);
		enterPassowrd(userID);

		clickOnSubmit();

		return new TmsHomePage(webDriver, testResultService);
	}
	
public TmsHomePage enterED2016AdminUserAndPassword() throws Exception {
		
		enterUserName(configuration.getProperty("ED2016Admin.user"));
		Thread.sleep(1500);
		enterPassowrd(configuration.getProperty("ED2016Admin.pass"));
		
		clickOnSubmit();
		
		return new TmsHomePage(webDriver, testResultService);
	}

	public TmsHomePage enterTeacherUserAndPassword() throws Exception {
		
		enterUserName(configuration.getProperty("teacher.username"));
		enterPassowrd(configuration.getProperty("teacher.password"));
		Thread.sleep(500);
		clickOnSubmit();
		
		return new TmsHomePage(webDriver, testResultService);
	}

	public String getPopUpMessageText() throws Exception {
		return webDriver.getPopUpText();

	}

	public void clickOnPassTextBox() throws Exception {
		webDriver.waitForElement(passwordTextbox, ByTypes.name).click();

	}

	public void clickOnUserNameTextBpx() throws Exception {
		webDriver.waitForElement(userNameTextbox, ByTypes.name).click();

	}

	public boolean getfFooterImageDisplayed() throws Exception {
		boolean imageFound;
		WebElement element = getFooterLogoElement();
		if (element.isDisplayed() == false) {
			imageFound = false;
		} else {
			imageFound = webDriver.isImageLoaded(element);
		}
		if (imageFound) {
			return true;
		} else {
			webDriver.printScreen("Image not found");
			return false;
		}

	}

	public WebElement getFooterLogoElement() throws Exception {
		WebElement element = webDriver.waitForElement(FOOTER_LOGO_XPATH,
				ByTypes.xpath);
		return element;
	}

	public void clickOnForgotPassword() throws Exception {
			webDriver.waitForElement(FORGOT_PASSWORD, ByTypes.linkText).click();
	
	}
	
	public NewUxSelfRegistrationPage openSelfRegistrationPage() throws Exception {
		try {
			getRegisterLinkElement().click();
		}catch (Exception e) {
			System.out.println("Registration button not available on login page "+ e.getMessage());
			e.printStackTrace();
			testResultService.addFailTest("Registration button not available on login page "+e.getMessage(), true, true);
		}
		webDriver.switchToPopup();
		return new NewUxSelfRegistrationPage(webDriver, testResultService);
	}
	
	public void checkRegisterLinkNotDisplayed() throws Exception {
		if(getRegisterLinkElement()!=null)
			testResultService.addFailTest("Register link displayed though it should not");
		
	}
	
	public void checkForgotPasswordInstruction(String expectedInstruction) throws Exception {
		String actualText= webDriver.waitForElement("//span[contains(@class,'forgotPasswordInstructions')]", ByTypes.xpath).getText();
		testResultService.assertEquals(expectedInstruction, actualText, "Instruction text is not as expected");
	}
	
	public void enterUserNameInForgotPasswordForm(String text) throws Exception {
		getUserNameElementInForgotPassForm().clear();
		getUserNameElementInForgotPassForm().sendKeys(text);
	}

	private WebElement getUserNameElementInForgotPassForm() throws Exception {
		return webDriver.waitForElement("userName", ByTypes.id);
	}
	
	private WebElement getRegisterLinkElement() throws Exception {
		return webDriver.waitForElement(REGISTER, ByTypes.linkText, false, smallTimeOut);
	}
	
	public void clickOnSubmitInForgotPasswordForm() throws Exception {
		try {
			webDriver.waitForElement(SUBMIT_FORGOT_PASSWORD, ByTypes.id).click();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void waitForLoginProgressBarEnds(int timeoutInSec) throws Exception {
		
		boolean isProgressBarFull = false;
		
		int elapsedTime = 0;
				
		while (isProgressBarFull == false) {
			String progressValue = getLoginProgressBarElement().getAttribute("style").split(" ") [1].substring(0, 2);
			if (progressValue.equals("99")) {
				isProgressBarFull = true;
				break;
			} else {
				elapsedTime++;
				Thread.sleep(1000);
				if (elapsedTime == timeoutInSec) {
				testResultService.addFailTest("Progress Bar was not full after timeout ended");
				break;
				}
			}
		}
				
		
	}
	
	public WebElement getLoginProgressBarElement() {
		
		WebElement element = null;   //waitForLogin__progress //*[@id="loginSection"]/section/div/div/div[1]/div[1]/div/div
		try {
			element = webDriver.waitUntilElementAppearsAndReturnElementByType("waitForLogin__textW", ByTypes.className, 3);
		}
		catch (Exception e) {
			e.printStackTrace();	
		}
		
		return element;
	}
	
	public void VerifyLoginProgressDisplay(boolean expected) {
		
		WebElement element = null;
		boolean actualDisplay=false;
		
		try {
			element = webDriver.waitUntilElementAppearsAndReturnElementByType("waitForLogin__textW", ByTypes.className, 1);
						if (element != null)
				actualDisplay=true;
			
			testResultService.assertEquals(expected, actualDisplay, "Delayed login element display while it doesn't");
		}
		catch (Exception e) {
			e.printStackTrace();	
		}
		
	}

	public void verifyUserLoggedInMessage(String expected, boolean clickOK) throws Exception {
		
		String actual = webDriver.waitForElement("waitForLogin__messageW", ByTypes.className, "Message not found").getText();
		testResultService.assertEquals(expected, actual, "Message is not valid");
		
		if (clickOK) {
			WebElement element = webDriver.waitForElement("//a[contains(@class,'layout__btnAction layout__btnActionSmall')]",ByTypes.xpath,1,false);
			element.click();
		}
			//webDriver.waitForElement("OK", ByTypes.linkText, "OK btn not found").click();
				
	}
	
	public void verifyWaitForLoginMessage(String expected) throws Exception {
		
		String actual = webDriver.waitForElement("waitForLogin__textW", ByTypes.className, "Message not found").getText();
		testResultService.assertEquals(expected, actual, "Message is not valid");
				
	}
	
	public void verifyDefaultBannerExist() throws Exception {
		
		String banner = getBannerUrl();
		testResultService.assertTrue("Default banner not found",banner.contains(GENERAL_BANNER_PATH));
		
				
	}
	
	public void clickOK_onSessionIsNoLongerActive() throws Exception {
		webDriver.waitForElement("layout__btnAction", ByTypes.className, "Button not found").click();
	}
	
	public void verifyDefaultBannerExist(String path) throws Exception {
		
		String banner = getBannerUrl();
		banner = banner.toLowerCase();
		testResultService.assertTrue("Default banner not found",banner.contains(path.toLowerCase()));
	}

	private String getBannerUrl() throws Exception {
		WebElement element = webDriver.waitUntilElementAppears("//header[contains(@class,'siteLogin__header')]", ByTypes.xpath,15);
		return element.getCssValue("background-image"); //   siteLogin__header

	}
	
	public TmsHomePage loginAsTmsUser(String userName, String password)
			throws Exception {
		enterUserName(userName);
		enterPassowrd(password);
		clickOnSubmit();

		return new TmsHomePage(webDriver, testResultService);
	}
	
	public void waitEDToeicLoginLoaded() throws Exception{
		WebElement element=null;
		try {
			for (int i=0;i<=15 && element==null;i++){
				element = webDriver.waitForElement("loginSection", ByTypes.id, false, 1);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			testResultService.addFailTest("Before Login not loaded", true, true);
		}
		
		if (element==null)
			try {
				testResultService.addFailTest("TOEIC Before Login not loaded", true, true);
			
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	public boolean waitAndCheckEDLoginLoaded() throws Exception {
		WebElement element = null;
		boolean loaded = false;
		try {
			for (int i = 0; i <= 15 && element == null; i++) {
				element = webDriver.waitForElement("loginSection", ByTypes.id, false, 1);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			testResultService.addFailTest("Before Login not loaded", true, true);
		}
		if (element != null)
			loaded=true;

		return loaded;
	}

	public void clickLogotAndVerifySantilianaDirectedLink(String studentId) {
		String customerUrl = null;
		try {
			customerUrl = webDriver.getUrl();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			testResultService.assertEquals(true, 
					//customerUrl.contains("identity.santillanaconnect.com/Account/Login"),"Customer url doesn't display");
					customerUrl.contains("Runtime/close.html"),"Correct url doesn't display");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String value = null;
		try {
			value = dbService.getStudentLogOutValue(studentId);
			testResultService.assertEquals(true, value.contains("2000-01-01 00:00:00"),"User Not loggedout from ED");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	public void verifyAndConfirmAlertMessage() {
		try {
			WebElement alertText = webDriver.waitForElement("//div[contains(@class,'TinyScrollbarW')]", ByTypes.xpath, false, 5);
			String text = alertText.getText().trim();
			testResultService.assertEquals(text.contains("You are already logged in"),true,"Wrong message or alert not apeared!");
			webDriver.waitForElement("btnOk", ByTypes.id, false, webDriver.getTimeout()).click();
			Thread.sleep(2000);
		}catch(Exception e) {
			//e.printStackTrace();
		}catch (AssertionError e) {
			// TODO: handle exception
		}
				
		
	}

	public void clearUserNameSantiliana() throws Exception {
		WebElement userNameElement = webDriver.waitForElement("Username", ByTypes.id, false, 5);
		userNameElement.clear();


	}
	
	public NewUxHomePage SantilianaLoginPage(String userName,String password) throws Exception {
		try {
			
			
			WebElement userNameElement = webDriver.waitForElement("Username", ByTypes.id, false, 5);
			WebElement passwordElement = webDriver.waitForElement("Password", ByTypes.id, false, 5);
			
			userNameElement.sendKeys(userName);
			passwordElement.sendKeys(password);
			
			webDriver.waitForElement("submitLogin", ByTypes.id, false, 5).click();
			
			Thread.sleep(2000);
			
					}catch(Exception e) {
						e.printStackTrace();
				}
		return new NewUxHomePage(webDriver,testResultService);
	}

	public void waitLoginAfterRestartAppPool() throws Exception {
		
		WebElement submitBtn = webDriver.waitForElement(SUBMIT, ByTypes.id,1, false);		
			for(int i = 0;i<=50&&submitBtn!=null;i++) {
				Thread.sleep(2000);
				submitBtn = webDriver.waitForElement(SUBMIT, ByTypes.id,1, false);
					if(i==40) {
						submitBtn.click();
					}
			}
		
		//webDriver.waitForElementByWebElement(getFooterLogoElement(), ErrorUserName, getfFooterImageDisplayed(), smallTimeOut);
		
	}

	public void verifyUserGetSessionTimeOut() throws Exception {
		Thread.sleep(3000);
		String url = webDriver.getUrl();
		for(int i=0;i<30&&!url.contains("SessionTimeout");i++) {
			Thread.sleep(2000);
			url = webDriver.getUrl();
		}
    	textService.assertEquals("User doesn't logedOut from first browser",url.contains("SessionTimeout"), true);
    	clickOK_onSessionIsNoLongerActive();
    	WebElement submit = webDriver.waitForElement("submit1", ByTypes.id, true, 10);
		textService.assertEquals("Logout incorrect", true, submit.isDisplayed());
	}

	public void confirmLogoutSantiliana() throws Exception {
		webDriver.getWebDriver().getPageSource();
		WebElement yesButton = webDriver.waitForElement("yes-logout-btn", ByTypes.id);
		yesButton.click();
	}

	@FindBy(id = "btnOk")
	WebElement loginOnThisDevise;

	public void clickLoginOnThisDevise() throws Exception {
		WebElement loginOnThisDevise = webDriver.waitForElement("btnOk", ByTypes.id);
		loginOnThisDevise.click();


	}

}
