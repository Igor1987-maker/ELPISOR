package pageObjects;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import Enums.ByTypes;
import Objects.GenericTestObject;
import drivers.GenericWebDriver;
import pageObjects.edo.NewUXLoginPage;
import pagesObjects.oms.PageUtils;
import services.TestResultService;

public class FirefoxLoginPage extends GenericTestObject{
	
	FirefoxDriver wd;

	public FirefoxLoginPage(String linkED) throws Exception {
		FirefoxOptions options = new FirefoxOptions();
		wd = new FirefoxDriver(options);
		wd.manage().window().maximize();
		wd.get(linkED);
		//wd = webDriver;
		PageFactory.initElements(wd, this);
	}
	
	
	public void initFirefoxBrowser(String linkED) {
		//System.setProperty("webdriver.gecko.driver",configuration.getPathToFirefoxDriver());
		FirefoxOptions options = new FirefoxOptions();
		wd = new FirefoxDriver(options);
		wd.manage().window().maximize();
		wd.get(linkED);
	}
	

	public void loginToEDwithFirefox(String userName, String password) throws InterruptedException {
		
		WebElement name = wait(By.xpath("//input[@name='userName']"));
		PageUtils.type(userName, name);
		WebElement inputPassword = wait(By.xpath("//input[@name='password']"));
		PageUtils.type(password, inputPassword);
		wd.findElement(By.id("submit1")).click();
		Thread.sleep(5000);
			
}

	private WebElement wait(By locator) {
		WebElement element = null;
		try {
			for(int i = 0;i<=3&&element==null;i++) {
				element = new WebDriverWait(wd, 10).until(ExpectedConditions.visibilityOfElementLocated(locator));
			}
		}catch (Exception e) {
		
		}			
		return element;
	}

	public void closeNotificationsInFirefox() {
	
	try {
		WebElement notifications = wait(By.className("notificationsCenter_hideSlide"));
		while(notifications!=null) {
			notifications.click();
			notifications = wait(By.className("notificationsCenter_hideSlide"));
			}
	}catch(Exception e) {
		}
}

	public void verifyUserLogedInFirefox(String userName) {
		WebElement userNameOnHomePage = wait(By.xpath("//span[contains(@class,'home__userName')]"));
	   	String name = userNameOnHomePage.getText().trim();
		testResultService.assertEquals("Hello " + userName, name, "First Name in Header of Home Page not valid");
		
	}

	public void checkUserLogedOutInFireFox() throws InterruptedException {
		Thread.sleep(500);
		String url = wd.getCurrentUrl();
		for(int i=0;i<30&&!url.contains("SessionTimeout");i++) {
			Thread.sleep(2000);
			url = wd.getCurrentUrl();
		}
    	textService.assertEquals("User doesn't logedOut from first browser",url.contains("SessionTimeout"), true);
       	WebElement oKbutton = wait(By.className("layout__btnAction"));//, ByTypes.className, "Button not found").click();
    	oKbutton.click();
       	WebElement submit = wait(By.id("submit1"));//, ByTypes.id, true, 10);
		textService.assertEquals("Logout incorrect", true, submit.isDisplayed());
		
	}


	public void closeSession() {
		wd.quit();
		
	}


	public void logOutFromEdWithSecondBrowser() throws InterruptedException {
		WebElement element = null;
		wait(By.xpath("//li[contains(@class,'settingsMenu__personal')]")).click();
		wait(By.xpath("//li[contains(@class,'settingsMenu__listItem_logout')]")).click();
		wait(By.xpath("//div[@id='EdoFrameBoxContent']/iframe"));
		wd.switchTo().frame(wd.findElementByXPath("//div[@id='EdoFrameBoxContent']/iframe"));
		Thread.sleep(1);
		wd.findElementByName("btnOk").click();
		Thread.sleep(1);
		
	}


	public void verifyAndConfirmAlertMessage() {
				
		try {
			WebElement alertText = wd.findElementByXPath("//div[contains(@class,'TinyScrollbarW')]");
			String text = alertText.getText().trim();
			testResultService.assertEquals(text.contains("You are already logged in to English Discoveries"),true,"Wrong message or alert not apeared!");
			wd.findElement(By.id("btnOk")).click();
			Thread.sleep(2000);
			WebElement notifications = wait(By.className("notificationsCenter_hideSlide"));
			while(notifications!=null) {
				notifications.click();
				notifications = wait(By.className("notificationsCenter_hideSlide"));
				}
		}catch(Exception e) {
			}
	}


	public void verifyAlertUserLoggedInTOEIC() throws Exception {
		
		Alert alert = wd.switchTo().alert();
		String textFromAlert = alert.getText();
		testResultService.assertEquals(textFromAlert.contains("This user is already logged"), true, "Wrong alert message or alert not apears");
		alert.accept();
		
	}

	
	
	
}
