package pageObjects.edo;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import Enums.ByTypes;
import Objects.GenericTestObject;
import drivers.GenericWebDriver;
import drivers.SafariWebDriver;
import pageObjects.GenericPage;
import services.TestResultService;




public class NewUxMyProfile extends GenericPage {

	private static final String CHANGE_YOUR_PASSWORD = "Change your password";
	private static final String ScrollBarElementXpath = "//*[@id='TinyScrollbarW']/div[1]/div";
	private static final String X_Button_Xpath = "//a[@class='modal-close']";
	private static final WebDriver WebDriver = null;
	private static final String CHANGE_YOUR_DICTIONARY_SETTINGS = "Change your dictionary setting";
	//private static final String START_BUTTON = "div[class='BSslide item text-center ng-isolate-scope active'] a[class='layout__btnAction ng-binding']";
	private static final String START_BUTTON = "div[class='carouselStartBtnW'] a[class='layout__btnAction ng-binding']";
	
	public NewUxMyProfile(GenericWebDriver webDriver,
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
	
	
	public void changeFirstName(String newFirstName) throws Exception {

		WebElement firstNameElement = getFirstNameElement();
		firstNameElement.clear();
		firstNameElement.sendKeys(newFirstName);

	}
	
	public void changeEmail(String newEmail) throws Exception {

		WebElement emailElement = getEmailElement();
		emailElement.clear();
		emailElement.sendKeys(newEmail);

	}

	private WebElement getFirstNameElement() throws Exception {
		WebElement firstNameElement = webDriver.waitForElement("FirstName",
				ByTypes.name);
		return firstNameElement;
	}
	
	private WebElement getEmailElement() throws Exception {
		WebElement emailElement = webDriver.waitForElement("//input[@name='Email']", ByTypes.xpath);
		return emailElement;
	}

	public void changeLastName(String newLastName) throws Exception {

		WebElement lastNameElement = getLastNameElement();
		lastNameElement.clear();
		lastNameElement.sendKeys(newLastName);

	}

	private WebElement getLastNameElement() throws Exception {
		return webDriver.waitForElement("LastName", ByTypes.name);
	}

	public void changeUserName(String newUserName) throws Exception {

		WebElement userNameElement = webDriver.waitForElement("UserName",
				ByTypes.name, "My profile user name field");
		userNameElement.clear();
		userNameElement.sendKeys(newUserName);

	}

	public void close() throws Exception {
		close(false);
		
	}

	public void close(boolean scroll) throws Exception {

		webDriver.switchToMainWindow();
		WebElement Xbuton = webDriver.waitForElement(X_Button_Xpath,
				ByTypes.xpath, "My Profile X button");
		webDriver.sleep(1);
		if (scroll) {
			//webDriver.switchToFrame("bsModal__iframe");
			webDriver.scrollToElement(Xbuton);
						
		}
		//Xbuton.click();
		webDriver.clickOnElementByJavaScript(Xbuton);

	}
	
	public void clickOnXBtn(boolean scroll) throws Exception {

		WebElement Xbuton = webDriver.waitForElement(X_Button_Xpath,
				ByTypes.xpath, "My Profile X button");
		if (scroll) {
			webDriver.scrollToElement(Xbuton);
						
		}
		Xbuton.click();

	}
	

	public String getFirstName() throws Exception {
		String text = getFirstNameElement().getAttribute("value");
		return text;
	}

	public void clickOnUpdate() throws Exception {
		
		if (webDriver.getBrowserName().equals("firefox")) {
			webDriver.waitForElement("LangSupLevel", ByTypes.id).click();
			for (int i = 0; i < 2; i++) {
				webDriver.sendKey(Keys.TAB);
				
			}
		}
		else {
			WebElement scrollElement = webDriver.waitForElement(
					ScrollBarElementXpath, ByTypes.xpath, "Scroll element");
			webDriver.dragScrollElement(scrollElement, 500);
		}		
		
		WebElement updateButton = webDriver.waitForElement("submitMyProfile", ByTypes.id, webDriver.getTimeout(), true, "Update Button");
		webDriver.ClickElement(updateButton);
		//updateButton.submit();
		Thread.sleep(1000);
		if (!(webDriver instanceof SafariWebDriver)) {
			webDriver.closeAlertByAccept();
		}
	}
	
	
	public void clickOnUpdateInTMS() throws Exception {

		if (webDriver.getBrowserName().equals("firefox")) {
			webDriver.waitForElement("LangSupLevel", ByTypes.id).click();
			for (int i = 0; i < 2; i++) {
				webDriver.sendKey(Keys.TAB);
			}
		}
		else {
			WebElement scrollElement = webDriver.waitForElement(
					ScrollBarElementXpath, ByTypes.xpath, "Scroll element");
			webDriver.dragScrollElement(scrollElement, 500);
		}		
		
		WebElement updateButton = webDriver.waitForElement("submitMyProfile", ByTypes.id, webDriver.getTimeout(), true, "Update Button");

		updateButton.click();
	}
	
	
	//Added By David
	public void scrollDownMyProfile() throws Exception{
		if (webDriver.getBrowserName().equals("firefox")) {
			webDriver.waitForElement("LangSupLevel", ByTypes.id).click();
			for (int i = 0; i < 2; i++) {
				webDriver.sendKey(Keys.TAB);
			}
		}
		else {
			WebElement scrollElement = webDriver.waitForElement(
					ScrollBarElementXpath, ByTypes.xpath, "Scroll element");
			webDriver.dragScrollElement(scrollElement, 500);
		}		
	}
	
	public String getUserName() throws Exception {
		String text = getUserNameElement().getAttribute("value");
		return text;
	}

	public WebElement getUserNameElement() throws Exception {
		return webDriver.waitForElement("UserName", ByTypes.name, 15, true, null);//webDriver.waitForElement("UserName", ByTypes.name);
	}

	public String getLastName() throws Exception {
		return getLastNameElement().getAttribute("value");
	}

	public String clickOnChangePassword() throws Exception {
		//webDriver.waitForElement(CHANGE_YOUR_PASSWORD, ByTypes.linkText).click();
		WebElement element = webDriver.waitUntilElementAppears(CHANGE_YOUR_PASSWORD, ByTypes.linkText, 15);
		element.click();
		//webDriver.clickOnElement(webDriver.waitForElement(CHANGE_YOUR_PASSWORD, ByTypes.linkText));
		
		Thread.sleep(2000);
		String mainWin = webDriver.switchToPopup();
		webDriver.switchToFrame("content");

		return mainWin;

	}

	public void enterOldPassword(String text) throws Exception {
		getOldPassElement().clear();
		getOldPassElement().sendKeys(text);
	}

	private WebElement getOldPassElement() throws Exception {
		return webDriver.waitForElement("OldPassword", ByTypes.id);
	}

	public void clickOnCancel() throws Exception {
		webDriver.waitForElement("Cancel", ByTypes.id).click();

	}

	public void clickOnSubmit() throws Exception {
		getSubmitElement().click();
	}

	private WebElement getSubmitElement() throws Exception {
		return webDriver.waitForElement("Submit", ByTypes.name);
	}

	private WebElement getNewPasswordElement() throws Exception {
		return webDriver.waitForElement("NewPassword", ByTypes.id);
	}

	private WebElement getConfirmPasswordElement() throws Exception {
		return webDriver.waitForElement("ConfirmPassword", ByTypes.id);

	}

	public void enterNewtNewPassword(String text) throws Exception {
		getNewPasswordElement().clear();
		getNewPasswordElement().sendKeys(text);
	}

	public void enterConfirmPassword(String text) throws Exception {
		getConfirmPasswordElement().clear();
		getConfirmPasswordElement().sendKeys(text);
	}

	public String getIFrameTitle() throws Exception {
		String text = webDriver.waitForElement("//h2[contains(@class,'layout__titleForIframeContent')]", ByTypes.xpath).getText();
		return text;
	}
	
	public void selectLangSupport(int langIndex, int levelSupportIndex) throws Exception {
		webDriver.selectElementFromComboBoByIndex("Language", ByTypes.name, langIndex);
		webDriver.selectElementFromComboBoByIndex("LangSupLevel", ByTypes.name, levelSupportIndex);
	}
	
	public void selectLangSupportByValue(String langName, int levelSupportIndex) throws Exception {
		webDriver.selectValueFromComboBox("Language", langName, ByTypes.name);
		webDriver.selectElementFromComboBoByIndex("LangSupLevel", ByTypes.name, 2);
	}
	
	public void ChangeCommunitySiteLevel(String level) throws Exception {
		
		Select dropdown = new Select(webDriver.waitForElement("LevelSelect", ByTypes.id, true, webDriver.getTimeout()));
		dropdown.selectByVisibleText(level);
		
		webDriver.waitForElement("LevelSelect", ByTypes.id, true, webDriver.getTimeout()).click();
	}	

	public void openDictionarySetting() throws Exception {
		webDriver.clickOnElement(webDriver.waitForElement(CHANGE_YOUR_DICTIONARY_SETTINGS, ByTypes.linkText));
	}
	
	public void verifyRightClickDictionarySettings(boolean expectedSetting,String path) throws Exception{
		webDriver.waitForElement("//div[@class='popInstructions ng-scope']",ByTypes.xpath,true,webDriver.getTimeout());
		Boolean actualStatus = webDriver.waitForElement(path, ByTypes.xpath).isSelected();
		testResultService.assertEquals(expectedSetting, actualStatus," The Right Click Settings should be: " + expectedSetting + " actualy it was:" + actualStatus);
	}

	public String performRightClickOnWord(String path) throws Exception{
		String word = webDriver.waitForElement(path, ByTypes.xpath).getText();
		WebElement action = webDriver.waitForElement(path, ByTypes.xpath); 
		Actions builder = new Actions(webDriver.getWebDriver());
		builder.contextClick(action).sendKeys(Keys.ARROW_RIGHT).perform();
		return word;
	}
	
	public void switchToDictionaryWindow() throws Exception {
		webDriver.switchToFrame(webDriver.getWebDriver().findElement(By.id("DictionaryIF")));
		webDriver.switchToFrame(webDriver.getWebDriver().findElement(By.id("wd_ResultFrame")));
		
				//waitForElement("DictionaryIF", ByTypes.id));
		//webDriver.switchToFrame(webDriver.waitForElement("wd_ResultFrame", ByTypes.id));
		
		//webDriver.switchToFrame("DictionaryIF");
		//webDriver.switchToFrame("wd_SearchFrame");
	}
	
	public void clickRightClickDictionarySettingsDisable() throws Exception{
		webDriver.findElementByXpath("//*[@id='settings']/table/tbody/tr[2]/td/div[2]/input",ByTypes.xpath).click();
	}
	
	public void clickRightClickDictionarySettingsEnable() throws Exception{
		webDriver.findElementByXpath("//*[@id='settings']/table/tbody/tr[2]/td/div[1]/input",ByTypes.xpath).click();
	}
	
	public void clickRightClickDictionarySettingsSave() throws Exception{
		webDriver.findElementByXpath("//*[@name='btnOk']",ByTypes.name).click();	
	}
	public void verifyDictionarySettingsVisibility(boolean expectedResult) throws Exception{
		boolean actualResult = webDriver.getWebDriver().findElement(By.xpath("/html/body/div/div[@id='DictionaryPopupContainer']")).isDisplayed();
		testResultService.assertEquals(expectedResult, actualResult);
	}
	
	public String getTheSelectedMyProfileLanguages(String path) throws Exception{
		WebElement comboBox = (webDriver.getWebDriver().findElement(By.xpath(path)));
		Select selectedValue = new Select(comboBox);
		String actualLanguage = selectedValue.getFirstSelectedOption().getText();
		return actualLanguage;
	}

	public void verifyLanguages(String expectedLnaguage) throws Exception{
		String path = "//*[@id='selectLanguage']";
		String actualLanguage =  getTheSelectedMyProfileLanguages(path);	
		testResultService.assertEquals(expectedLnaguage, actualLanguage);
	}
	
	public String changeCommunitySiteLevelRandomly() throws Exception {
		// retrieve current level
		String currentLevel = "";
		List<WebElement> options = webDriver.getWebDriver().findElements(By.xpath("//*[@id=\"LevelSelect\"]/option"));
		for (int i = 0; i < options.size(); i++) {
			if (options.get(i).getAttribute("selected").equals("true")) {
				currentLevel = options.get(i).getText();
				break;
			}
		}
		
		// choose random that doesn't include the current level
		String newLevel = "";
		boolean FLAG = false;
		int randomIndex = (int) (Math.random() * options.size());
		while (!FLAG) {
			if (options.get(randomIndex).getText().equals(currentLevel)) {
				randomIndex = (int) (Math.random() * options.size());
			} else {
				FLAG = true;
				newLevel = options.get(randomIndex).getText();
			}
		}
		
		ChangeCommunitySiteLevel(newLevel);
		Thread.sleep(1000);
		
		clickOnUpdate();
		close(true);
		Thread.sleep(2000);
		
		return newLevel;
	}
	
	public String getMail() throws Exception{
		WebElement emailElement = getEmailElement();
		return emailElement.getAttribute("value");
	}
	
	public String getTheSelectedMyProfileLanguages() throws Exception{
		WebElement comboBox = (webDriver.getWebDriver().findElement(By.xpath("//select[@name='Language']")));//"//*[@id='tbl_DHPMainArea']/tbody/tr[8]/td[1]/select")));
		Select selectedValue = new Select(comboBox);
		String actualLanguage = selectedValue.getFirstSelectedOption().getText();
		return actualLanguage;
	}
	
	public boolean performRightClickOnGivenWord(String word) throws Exception{
		webDriver.switchToTopMostFrame();
		
		WebElement wordElement=null;
		WebElement element = null;
		try {
//			wordElement = webDriver.waitForElement("//*[@id='LABody']/app/div/div/section/div/section[1]/div", ByTypes.xpath, false, 3);
			wordElement = webDriver.waitForElement("//*[text()[contains(.,'"+word+"')]]", ByTypes.xpath, false, 3);
			Actions builder = new Actions(webDriver.getWebDriver());
			builder.contextClick(wordElement).sendKeys(Keys.ARROW_RIGHT).perform();
			element = webDriver.waitUntilElementAppears("Mysearchtext", ByTypes.id, 2);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return webDriver.isDisplayed(element);
	}
	
	public void clickUpdateMyProfileWithoutScroll() throws Exception{
		WebElement update = webDriver.waitForElement("submitMyProfile", ByTypes.id, webDriver.getTimeout(), false, "Update Button");
		webDriver.ClickElement(update);
		webDriver.closeAlertByAccept();
	}
	
	public void setEnglishLanguageInMyProfile() throws Exception {

	//WebElement startButton = new WebDriverWait(webDriver.getWebDriver(), 10).until(ExpectedConditions.elementToBeClickable(By.cssSelector(START_BUTTON)));
	
	WebElement startButton = webDriver.waitForElement(START_BUTTON, ByTypes.cssSelector, true, 5);
	String text = startButton.getText();
		if(!text.equalsIgnoreCase("Start") && !text.equalsIgnoreCase("Continue")){
			webDriver.waitForElement("//li[contains(@class,'settingsMenu__personal')]", ByTypes.xpath).click();
			webDriver.waitForElementAndClick("//*[@id='settingsMenu__listItem_myProfile']/a", ByTypes.xpath);
			NewUxHomePage homePage = new NewUxHomePage(webDriver, testResultService);
			homePage.switchToMyProfile();
			Thread.sleep(2000);
			webDriver.selectElementFromComboBoByIndex("LangSupLevel", ByTypes.name, 0);
			clickOnUpdate();
			Thread.sleep(2000);
			close(true);
			Thread.sleep(2000);
			webDriver.switchToMainWindow();
	
		}
		
	}

}