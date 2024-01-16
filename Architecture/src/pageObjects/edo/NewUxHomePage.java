package pageObjects.edo;

import Enums.ByTypes;
import Enums.NavBarItems;
import drivers.AndroidWebDriver;
import drivers.GenericWebDriver;
import drivers.IEWebDriver;
import drivers.SafariWebDriver;
import org.apache.commons.lang3.ArrayUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.springframework.beans.factory.annotation.Autowired;
import pageObjects.GenericPage;
import services.PageHelperService;
import services.Reporter;
import services.StudentService;
import services.TestResultService;
import tests.edo.newux.BasicNewUxTest;

import java.util.ArrayList;
import java.util.List;


public class NewUxHomePage extends GenericPage {

	private static final String DIV_CONTAINS_CLASS_HOME_NOT_ASSIGNED_TXT = "//div[contains(@class,'home__notAssignedTxt')]";
	private static final String Take_PLT_Message = "Take a placement test to start learning.";
	//private static final String Click_On_Assessments_To_Take_PLT_Message = "Click on the Assessment tab in the navigation bar to take the placement test. Once you complete the placement test, you will be assigned a course.";
	private static final String Click_On_Assessments_To_Take_PLT_Message = "If you are here to take a Placement Test or any other assessment, click on the";
	
	private static final String myProfileFrameName = "bsModal__iframe";
	private static final String modalFrameName = "bsModal__iframe";
	protected static final String USER_MENU = "//div[@id='learning__settingsMenu']";
	protected static final String UL_ID_SITEMENU_ITEM_HOME = "//ul[contains(@class,'sitemenu__menu')]";
	private static final String MENU_BTN = "sitemenu__openMenuBtn";
	private static final String LOGOUT_XPATH = "//a[@data-ng-click='logout()']";
	private static final String userMenuClass = "'home__userDataMenu'";
	//private static final String allUnitsBar = "//div[@class='home__allUnitsProgressIW']";
	private static final String allUnitsBar = "//div[@class='progressBar__wrapper']";
	private static final String UnAssign_Xpath = "//*[@id='mainContent']/div/div/section[3]/div[1]/p[1]";
	
	// Home page Dog messages
	private static final String Top_dog_message = "You have not been assigned a course.";
	private static final String Middle_Dog_message = "Please check in the Assessments Section to see if you need to take a Placement Test.";
	private static final String Last_Dog_message = "Contact your teacher or administrator for help.";
	private static final String Welcome_Message = "Welcome to English Discoveries!";
	private static final String Middle_Welcome_message = "If you are here to take a Placement Test or any other assessment, click on the \"Assessment Center\" section in the Navigation Bar on the left-hand side of the screen.";
	private static final String Last_Welcome_message = "You don't have any courses assigned at the moment. If you would like to start studying, contact your teacher or administrator for help.";
	
	protected int smallTimeOut = 3;
	public String[] courseCodes = BasicNewUxTest.courseCodes;
	public String[] courseCodesTOEIC = BasicNewUxTest.courseCodesTOEIC;
	public String[] courses = BasicNewUxTest.courses;
	public String[] coursesNames = BasicNewUxTest.coursesNames;
	public String[] coursesNamesCABA = BasicNewUxTest.coursesNamesCABA;
	public String[] coursesNamesTOEIC = BasicNewUxTest.coursesNamesTOEIC;
	public String[] coursesNamesUMM = BasicNewUxTest.coursesNamesUMM;
	public static  final String CorpUrl = "https://edusoftlearning.com/";
	
	Reporter report;
	
	@Autowired
	StudentService studentService;
	
	public NewUxHomePage(GenericWebDriver webDriver, TestResultService testResultService) throws Exception {
		super(webDriver, testResultService);
		// studentService=new StudentService();
		// TODO Auto-generated cbbbbonstructor stub
	}

	public void clickOnAboutEdusoft() throws Exception {
		webDriver.waitForElement("About Edusoft", ByTypes.linkText).click();
	}
	
	public String getTokenFromLocalStorage() {
		JavascriptExecutor jsExecutor = (JavascriptExecutor)webDriver.getWebDriver();
		String token = (String) jsExecutor.executeScript("return localStorage.getItem(\"EDAPPToken\")").toString();
		
		return token;
	}

	public void clickOnLegalNotice() throws Exception {
		webDriver.waitForElement("Legal Notices", ByTypes.linkText).click();
	}

	public void clickOnPrivacyStatement() throws Exception {
		webDriver.waitForElement("Privacy Statement", ByTypes.linkText).click();
	}

	public String getOcModalTitleTextInFooter() throws Exception {
		String text = webDriver.waitForElement("//div[contains(@class,'modal-header')]//h2",ByTypes.xpath).getText();

		return text;
	}
	
	public String getOcModalTitleTextInFooter2() throws Exception {
		String text = webDriver
				.waitForElement("//div[contains(@class,'footerModal')]//h2",

		ByTypes.xpath).getText();

		return text;
	}

	public String getContactUsLinkText() throws Exception {

		String text = webDriver
				.waitForElement("//ul[contains(@class,'siteTmpl__footerLinksInnerW')]/li/a", ByTypes.xpath).getText();
		return text;

	}

	public String getCustomCourseName() {
		String courseName = "EDExcellence - Flexibility";
		List<WebElement> elements = webDriver.getWebDriver().findElements(By.cssSelector("a[title='" + courseName + "']"));
		if (elements.size()>0){
			return courseName;
		} else return "Flexibility";
	}

	public void checkCustomAboutLink(String url, String label) throws Exception {

		String actualUrl = webDriver.waitForElement("//ul//li[2]/a[@href]", ByTypes.xpath,false,smallTimeOut).getAttribute("ng-href");
		String actualLabel = webDriver.waitForElement("//ul//li[2]/a[@href]", ByTypes.xpath,false,smallTimeOut).getText();
		
		testResultService.assertEquals(url.toLowerCase(), actualUrl.toLowerCase());
		//testResultService.assertEquals(label.toLowerCase(), actualLabel.toLowerCase()); // the custom Lable not developed

	}

	public void checkCustomContactUsLink(String mailto) throws Exception {
		webDriver.waitUntilElementAppears("//a[contains(@href, 'mailto:" + mailto + "')]", 10);

		//webDriver.waitForElement("//a[contains(@href, 'mailto:" + mailto + "')]", ByTypes.xpath,false,smallTimeOut);
		//webDriver.waitForElement("//a[contains(@href, 'mailto:" + mailto + "')]", ByTypes.xpath);
		
	}

	public void checkCustomLogo(String logoLink, String imageFileName) throws Exception {

		webDriver.waitForElement("//a/img[contains(@src,'" + imageFileName + "')]", ByTypes.xpath);
		webDriver.waitForElement("//a/img[contains(@src,'" + logoLink + "')]", ByTypes.xpath);

	}

	public void checkCustomPrivacyLegal(String whiteLabel) throws Exception {
		
		clickOnPrivacyStatement();
		testResultService.assertEquals(whiteLabel.toLowerCase(),(webDriver.waitForElement("//span[@class='disclamer" + whiteLabel + "']", ByTypes.xpath).getText().toLowerCase()));
		List<WebElement> WLList = webDriver.getElementsByXpath("//span[@class='disclamer" + whiteLabel + "']");
		
		for (int i = 0; i < 10; i++) {
			String text = WLList.get(i).getText().replace(".", "");
			testResultService.assertEquals(whiteLabel.toLowerCase(), text.toLowerCase());

			}

		closeFooterLinksModalPopup();
		webDriver.sleep(1);
		
		WebElement fotterArrowButton = webDriver.waitForElement("//div[contains(@class,'footerHandle')]", ByTypes.xpath, false, smallTimeOut);
		if (fotterArrowButton != null) {
			fotterArrowButton.click();
		}
		
		clickOnLegalNotice();
		String text = webDriver.waitForElement("//i", ByTypes.xpath).getText();
		testResultService.assertEquals(whiteLabel.toLowerCase(), text.toLowerCase());
		closeFooterLinksModalPopup();
		
	}
	
	public void checkCustomPrivacyLegal2(String whiteLabel) throws Exception {
		
		clickOnPrivacyStatement();
		testResultService.assertEquals(whiteLabel.toLowerCase(),(webDriver.waitForElement("//span[@class='disclamer" + whiteLabel + "']", ByTypes.xpath).getText().toLowerCase()));
		List<WebElement> WLList = webDriver.getElementsByXpath("//span[@class='disclamer" + whiteLabel + "']");
		
		for (int i = 0; i < 10; i++) {
			String text = WLList.get(i).getText().replace(".", "");
			testResultService.assertEquals(whiteLabel.toLowerCase(), text.toLowerCase());

			}

//		closeFooterLinksModalPopup2();
//		webDriver.sleep(1);
		closeModalWnd();
		
		WebElement fotterArrowButton = webDriver.waitForElement("//div[contains(@class,'footerHandle')]", ByTypes.xpath, false, smallTimeOut);
		if (fotterArrowButton != null) {
			fotterArrowButton.click();
		}
		
		clickOnLegalNotice();
		String text = webDriver.waitForElement("//i", ByTypes.xpath).getText();
		testResultService.assertEquals(whiteLabel.toLowerCase(), text.toLowerCase());
//		closeFooterLinksModalPopup2();
		closeModalWnd();
	}
	
	private void closeModalWnd() throws Exception {
		WebElement closeBtn = webDriver.waitForElement("//div[contains(@class,'footerModal')]", ByTypes.xpath).findElement(By.className("modal-close"));
		webDriver.sleep(1);
		
 		webDriver.clickOnElementByJavaScript(closeBtn);
	}
	
	public void closeFooterLinksModalPopup() throws Exception {
		
		closeModalPopUp();
	}
	
	public void closeFooterLinksModalPopup2() throws Exception {
		
		WebElement closeBtn = webDriver.waitForElement("//div[contains(@class,'footerModal')]", ByTypes.xpath).findElement(By.className("modal-close"));
		closeBtn.click();
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

	public void carouselNavigateNext() throws Exception {
		WebElement element = webDriver.waitUntilElementAppearsAndReturnElement("//a[@ng-click='next()']", 10);

		if(element==null) {
			testResultService.addFailTest("Carousel not displayed on home page", true, true);
		}
		// if (!(webDriver instanceof AndroidWebDriver)) {
		element.click();
	}
	
	public void checkCourseCarouselArrowsNotDisplayed() throws Exception {
		WebElement rightArrow = webDriver.waitForElement("//a[@ng-click='next()']", ByTypes.xpath, 1, false);
		WebElement leftArrow = webDriver.waitForElement("//a[@ng-click='prev()']", ByTypes.xpath, 1,  false);
		if (rightArrow != null && leftArrow != null) testResultService.addFailTest("Course Carousel arrows displayed", false, true);
		
	}

	public void carouselNavigateNext(int index) throws Exception {
		String currentCourseName = getCurrentCourseElement(index).getText();

		WebElement element = webDriver.waitForElement("//a[@ng-click='next()']", ByTypes.xpath);

		// if (!(webDriver instanceof AndroidWebDriver)) {
		element.click();

		String newCourseName = getCurrentCourseElement(index + 1).getText();

		int timeOut = 5;
		int elpsedTime = 0;
		while (elpsedTime <= timeOut) {
			if (newCourseName.equals(currentCourseName)) {
				Thread.sleep(1000);
				elpsedTime++;
			} else {
				break;
			}

		}

		// }
		// else{
		// webDriver.swipeElementLeft(element, -200);
		// }

	}

	public void carouselNavigateBack() throws Exception {
		WebElement element = webDriver.waitUntilElementAppearsAndReturnElement("//a[@ng-click='prev()']",10);
		element.click();
	}

	public String getUnitName() throws Exception {
		WebElement element = webDriver.waitForElement("//div[contains(@class,'carouselCaptions')]//ul//li[1]",
				ByTypes.xpath,false,webDriver.getTimeout());

		return webDriver.getElementText(element);

	}

	public void clickOnLogOut() throws Exception {
		// TODO Auto-generated method stub

		webDriver.waitForElement("//li[contains(@class,'settingsMenu__personal')]", ByTypes.xpath).click();
		
		//webDriver.waitForElement(LOGOUT_XPATH, ByTypes.xpath, webDriver.getTimeout(), false).click();
		
		webDriver.waitForElement("//li[contains(@class,'settingsMenu__listItem_logout')]", ByTypes.xpath).click();
		
		webDriver.switchToFrame(webDriver.waitForElement("//div[@id='EdoFrameBoxContent']/iframe", ByTypes.xpath));

		webDriver.waitForElement("btnOk", ByTypes.name).click();
		
	//	webDriver.switchToMainWindow();
	}
	
	public void waitToLoginArea() throws Exception {
		webDriver.waitUntilElementAppears("loginSection", ByTypes.id,10);
	}
	
	public void waitToBannerLoaded() throws Exception {
		webDriver.waitUntilElementAppears("//a[contains(@class,'siteLogin__headerLink')]", ByTypes.xpath,10);
	}
	
	// new
	public void logOutOfED() throws Exception {

		webDriver.waitForElement("//li[contains(@class,'settingsMenu__personal')]", ByTypes.xpath).click();
		
		//webDriver.waitForElement(LOGOUT_XPATH, ByTypes.xpath, webDriver.getTimeout(), false).click();
		
		webDriver.waitForElement("//li[contains(@class,'settingsMenu__listItem_logout')]", ByTypes.xpath).click();
		
		webDriver.switchToFrame(webDriver.waitForElement("//div[@id='EdoFrameBoxContent']/iframe", ByTypes.xpath));

		webDriver.waitForElement("btnOk", ByTypes.name).click();
		
		webDriver.switchToMainWindow();
		
		WebElement submit = webDriver.waitForElement("submit1", ByTypes.id, true, 10);
		textService.assertEquals("Logout incorrect", true, submit.isDisplayed());
		//Thread.sleep(2);
	//	NewUXLoginPage loginPage = new NewUXLoginPage(webDriver, testResultService);
	//	testResultService.assertEquals(false, loginPage.isSubmitButtonEnabled(), "Checking that Login Page displayed");
	}
	

	public void clickToOpenNavigationBar() throws Exception {
		
		WebElement element = webDriver.waitUntilElementAppears("sitemenu__hamburger", ByTypes.className,smallTimeOut);
		
	
		if (element != null) {
			webDriver.hoverOnElement(element);
			Thread.sleep(1000);
			element.click();
			Thread.sleep(1000);
		} else {
			testResultService.addFailTest("Menu was not displayed", false, true);
		}

	}

	public void clickToOpenNavigationBarHP() throws Exception {
		WebElement element = webDriver.waitForElement("//*[@id='sitemenu__itemHome']/a", ByTypes.xpath,
				webDriver.getTimeout(), false);

		if (element != null) {
			element.click();
		} else {
			testResultService.addFailTest("Home button was not displayed", false, true);
		}

	}

	public void getNavigationBarStatus() throws Exception {
		webDriver.waitForElement("//nav[contains(@class, 'layout__siteNav')]", ByTypes.xpath, webDriver.getTimeout(),
				false);

	}

	public String getNavBarItemsNotification(String id) throws Exception {
		WebElement element = webDriver.waitForElement(UL_ID_SITEMENU_ITEM_HOME + "//li[" + id + "]//a//div//span",
				ByTypes.xpath, webDriver.getTimeout(), false);
		return element.getText();

	}

	public boolean isNavBarItemEnabled(String id) throws Exception {
		WebElement element = webDriver.waitForElement(
				UL_ID_SITEMENU_ITEM_HOME + "//li[" + id + "][contains(@class,'disabled')]", ByTypes.xpath,
				smallTimeOut, false);
		if (element == null) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isNavBarItemCurrent (String btnPositionNum) throws Exception {
		WebElement element = webDriver.waitForElement(
				UL_ID_SITEMENU_ITEM_HOME + "//li[" + btnPositionNum + "][contains(@class,'current')]", ByTypes.xpath,
				smallTimeOut, false);
		if (element == null) {
			return false;
		} else {
			return true;
		}
	}

	public boolean isNavBarItemAlerted(String id) throws Exception {
		WebElement element = webDriver.waitForElement(
				UL_ID_SITEMENU_ITEM_HOME + "//li[" + id + "][contains(@class,'alerted')]",
				ByTypes.xpath, webDriver.getTimeout(), false);
		if (element != null) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isNavBarOpen() throws Exception {
		WebElement element = webDriver.waitForElement(UL_ID_SITEMENU_ITEM_HOME + "//li[1]", ByTypes.xpath,
				webDriver.getTimeout(), false);
		try {
			if (element.isDisplayed()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return false;
		}
	}

	public boolean isNavBarClosed() throws Exception {
		WebElement element = webDriver.waitForElement(UL_ID_SITEMENU_ITEM_HOME + "//li[1]", ByTypes.xpath,
				webDriver.getTimeout(), false);
		if (element == null) {
			return true;
		} else {
			return false;
		}
	}

	public void checkIfNavBarItemDisplayed(boolean expectedResult, NavBarItems itemId) throws Exception {
			boolean actualResult = webDriver.getWebDriver().findElements(By.id(itemId.toString())).size() > 0;	
		testResultService.assertEquals(expectedResult, actualResult,"Item under test is : "+itemId+" display expected: "+expectedResult+" / display actual: "+actualResult);
		
	}
	
	
	public String getUserDataText() throws Exception {
		WebElement element = webDriver.waitUntilElementAppearsAndReturnElement("//span[contains(@class,'home__userName')]", 15);
		
		if (element == null) {
			testResultService.addFailTest("User data element is not found");
			return null;
		}
		return element.getText();
	}

	public boolean isCourseCompletionWidgetExist() throws Exception {

		WebElement element = webDriver.waitForElement("home__studentWidgetCompletion", ByTypes.className);

		return element.isDisplayed();

	}

	public String getCourseCompletionWidgetUnitItem() throws Exception {

		WebElement element = webDriver.waitForElement("//div[@class = 'layout__pull itemUnit']", ByTypes.xpath);

		return element.getText();

	}
	
	public String getCourseCompletionDisplayedOnHomePage() throws Exception {

		WebElement element = webDriver.waitForElement("home__studentWidgetCompletionData", ByTypes.id);

		return element.getText();

	}

	public String getCourseCompletionWidgetLabel(String CompletionLabel) throws Exception {

		WebElement element = webDriver.waitForElement("//span[@title = '" + CompletionLabel + "']", ByTypes.xpath);

		return element.getText();

	}

	public boolean isScoreWidgetExist() throws Exception {

		WebElement element = webDriver.waitForElement("home__studentWidgetTestScores", ByTypes.className);

		return element.isDisplayed();

	}

	public String getScoreWidgetUnitItem() throws Exception {

		WebElement element = webDriver.waitForElement("//div[@class = 'layout__pull itemUnit']", ByTypes.xpath);

		return element.getText();

	}

	public String getScoreWidgetLabel(String ScoreLabel) throws Exception {

		WebElement element = webDriver.waitForElement("//span[@title = '" + ScoreLabel + "']", ByTypes.xpath);

		return element.getText();

	}

	public boolean isTimeWidgetExist() throws Exception {

		WebElement element = webDriver.waitForElement("home__studentWidgetTimeOnTask", ByTypes.className,false,smallTimeOut);

		return element.isDisplayed();

	}

	public String getTimeWidgetUnitsDelimiter() throws Exception {

		WebElement element = webDriver.waitForElement("//span[@class = 'home__studentWidgetHrMinDots']", ByTypes.xpath);

		return element.getText();

	}

	public String getTimeWidgetHoursLabel() throws Exception {

		//WebElement element = webDriver.waitForElement("//span[1][@class = 'home__studentWidgetHrMinHrTxt']",
		WebElement element = webDriver.waitForElement("//*/aside[3]/div[1]/div/div[2]/div/span[1]",
				ByTypes.xpath,false,2);

		return element.getText();

	}

	public String getTimeWidgetMinLabel() throws Exception {

		//WebElement element = webDriver.waitForElement("//span[3][@class = 'home__studentWidgetHrMinHrTxt']",
		WebElement element = webDriver.waitForElement("//*/aside[3]/div[1]/div/div[2]/div/span[3]",
			ByTypes.xpath,false,2);

		return element.getText();

	}

	public String getTimeWidgetLabel(String TimeLabel) throws Exception {

		WebElement element = webDriver.waitForElement("//span[@title = '" + TimeLabel + "']", ByTypes.xpath);

		return element.getText();

	}

	// need to be fixed - returns wrong number
	public List<WebElement> getUnitsElements() throws Exception {
		WebElement allUnitsElement = webDriver.waitForElement("//div[contains(@class,'home__allUnitsW')]",
				ByTypes.xpath);

		List<WebElement> unitElements = webDriver.getChildElementsByXpath(allUnitsElement,
				"//div//div[@class='home__unitIW']");

		// System.out.println("Units count:" + unitElements.size());

		// List<WebElement> unitElements = new ArrayList<WebElement>();
		// boolean hasMoreChileElemts = true;
		// int index = 1;
		// WebElement unitElement;
		// while (hasMoreChileElemts) {
		// try {
		// unitElement = null;
		// unitElement = webDriver.waitForElement(
		// "//div[contains(@class,'home__allUnitsW')]//div["
		// + index + "]", ByTypes.xpath, false,
		// webDriver.getTimeout());
		// if (unitElement != null) {
		// unitElements.add(unitElement);
		// index++;
		// } else {
		// hasMoreChileElemts = false;
		// break;
		// }
		// } catch (Exception e) {
		// hasMoreChileElemts = false;
		// break;
		// }
		// }
		return unitElements;
	}

	public String getUnitNameByUnitElement(WebElement unitElement) throws Exception {
		WebElement child = webDriver.getChildElementByXpath(unitElement,
				"//div[@class='home__details']//h3[contains(@class,'home__detailsTitle')]");
		return child.getText();

	}

	public WebElement getCurrentCourseElement(int courseIndex) throws Exception {
		// TODO Auto-generated method stub
		// return webDriver.waitForElement(
		// "//div[contains(@class,'carouselCaptions')]//h3[text()='"
		// + courseName + "']", ByTypes.xpath);

		WebElement element = null;
		
		for (int i=0;i<=20 && element==null;i++){
			element = webDriver.waitForElement(
					"//*[@id='mainContent']/div/div/section[1]/div/div/div/div[1]/div[" + courseIndex + "]/div/h3",
					ByTypes.xpath,1,false, "Current course name");
		}
		
		return element;
	}
	
	public String getCurrentCourseName() throws Exception {
		
		WebElement element =null;
		String currentCourseName = ""; //element.getText();
		
		for (int i=0;i<=30 && element==null;i++){
			element = webDriver.waitForElement(
					"//*[@id='mainContent']/div/div/section[1]/div/div/div/div[1]/div[contains(@class,'active')]/div/h3",
					ByTypes.xpath,1,false, "Current course name not found");
		}
		
		int i=0;
		if (element!=null)
		{
			while((currentCourseName == null || currentCourseName == "") && i < 30) {
				currentCourseName = element.getText();
				Thread.sleep(1000);
				i++;
			}
		}		
		
		return currentCourseName;
		
	}
	
	public String getCurrentCourseLabelCEFR() throws Exception {
		
		String level = "empty";
		WebElement cefr = null;
		String xpath = ".//div//img[@class='carouselCefrBadgeImg'][contains(@src,'.png')]";
		
		//WebElement currentCourse = webDriver.waitForElement(
		//		"//*[@id='mainContent']/div/div/section[1]/div/div/div/div[1]/div[contains(@class,'active')]",
		//		ByTypes.xpath,1,false, "Course element not found");

		try {
			cefr = webDriver.waitUntilElementAppearsAndReturnElement(xpath, 1);
					//currentCourse.findElement(By.xpath(".//div//img[@class='carouselCefrBadgeImg'][contains(@src,'.png')]"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		if (cefr != null)
			return cefr.getAttribute("src").split("CEFR-")[1].substring(0,2);
		
		else return level;
	}

	public NewUxLearningArea clickOnContinueButton() throws Exception {
		
		WebElement ContinueButton = getCourseContinueButton();
		if (ContinueButton!=null)
			ContinueButton.click();
		else

		waitForPageToLoad();
		
		return new NewUxLearningArea(webDriver, testResultService);
	}
	
	public NewUxLearningArea2 clickOnContinueButton2() throws Exception {
		
		getCourseContinueButton().click();
		
		return new NewUxLearningArea2(webDriver, testResultService);
	}
	
	public WebElement getCourseContinueButton()  {
		
		WebElement caroElement = null;
		try {
			caroElement = webDriver.waitUntilElementAppears("//*[@id='mainContent']/div/div/section[1]/div/div/div/div[1]/div[1]/div/div/a", ByTypes.xpath, 5);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return caroElement;
	}

	public String getBgImageFileName() throws Exception {
		// TODO Auto-generated method stub
		return webDriver.waitForElement("//div[contains(@class,'BSslide')]", ByTypes.xpath).getAttribute("style");
	}

	public WebElement getUserAvaterElement() throws Exception {
		WebElement element = null;
		try {
			
			//element = webDriver.waitForElement("//div[@id='learning__settingsMenu']/div/ul/li", ByTypes.xpath, true, 15);
			
			element = webDriver.waitForElement("//li[contains(@class, 'settingsMenu__personal')]", ByTypes.xpath, true, 15);
			
			//element = webDriver.waitForElement("//a//img[@id='home__userProfileImg']", ByTypes.xpath);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			testResultService.addFailTest("Cannot Find Avatar Element", false, true);

		}
		if (element == null) {
			testResultService.addFailTest("Avatar Element Null", true, true);
		}
		return element;

	}

	public boolean getUserAvataerIsDisplayed() throws Exception {
		return getUserAvaterElement().isDisplayed();

	}

	public void clickOnUserAvatar() throws Exception {
		getUserAvaterElement().click();

	}

	public void checkTextAndclickOnMyProfile(int sequenceNum) throws Exception {
		WebElement element = webDriver.waitForElement("//ul[contains(@class,'settingsMenu__listItemW_personal')]//li["+sequenceNum+"]/a", ByTypes.xpath);
		testResultService.assertEquals("My Profile", element.getText(), "Checking My Profile link text");
		element.click();

	}

	public void checkTextAndclickOnStudyPlanner() throws Exception {
		//WebElement element = webDriver.waitForElement("//ul[contains(@class,'settingsMenu__listItemW_personal')]//li["+sequenceNum+"]/a", ByTypes.xpath);
		WebElement element = webDriver.waitForElement("settingsMenu__listItem_studyPlanner", ByTypes.id);
		testResultService.assertEquals("Study Planner", element.getText(), "Checking Study Planner link text");
		element.click();
	}
	
	public void checkTextandClickOnLogoutLink() throws Exception {
		//WebElement element = webDriver.waitForElement("//ul[contains(@class,'settingsMenu__listItemW_personal')]//li["+sequenceNum+"]/a", ByTypes.xpath);
		WebElement element = webDriver.waitForElement("settingsMenu__listItem_logout", ByTypes.id);
		testResultService.assertEquals("Logout", element.getText(), "Checking Logout link text");
		element.click();
		
	}
	
	public void checkTextAndclickOnNotifications() throws Exception {
		//WebElement element = webDriver.waitForElement("//ul[contains(@class,'settingsMenu__listItemW_personal')]//li["+sequenceNum+"]/a", ByTypes.xpath);
		WebElement element = webDriver.waitForElement("settingsMenu__listItem_NotificationCenter", ByTypes.id);
		testResultService.assertEquals("Notifications", element.getText(), "Checking Notifications link text");
		element.click();
		
		WebElement notifications = webDriver.waitForElement("home__marketingSlideOW_0", ByTypes.id, false, 2);
		if (notifications != null){
			webDriver.waitForElement("notificationsCenter_hideSlide", ByTypes.className, false, 2).click();
		} else{
			testResultService.addFailTest("Notification not opened", false, false);
		}
			
	}
	
	public void skipNotificationWindow() throws Exception {
				
		WebElement notifications = webDriver.waitForElement("home__marketingSlideOW_0", ByTypes.id, false, 10);
		Thread.sleep(500);
		
		for (int i=0; i<10 && notifications != null;i++){
			WebElement element = webDriver.waitForElement("notificationsCenter_hideSlide", ByTypes.className, false, 1);
			element.click();
			notifications = webDriver.waitForElement("home__marketingSlideOW_0", ByTypes.id, false, 2);
		}		
	}
	
	public void checkTextAndclickOnLineSession() throws Exception {
		WebElement element = webDriver.waitForElement("settingsMenu__listItem_onlineSessions", ByTypes.id,false,2);
		testResultService.assertEquals("Online Sessions", element.getText(), "Checking Online Sessions link text");
		element.click();
		
		WebElement session = webDriver.waitForElement("//*[@id='mainCtrl']/div[3]/div/div/div[1]/h2", ByTypes.xpath, false, 2);
		testResultService.assertEquals("Online Sessions", session.getText(), "Checking Online Sessions title in window");
		webDriver.waitForElement("//*[@id='mainCtrl']/div[3]/div/div/div[1]/a", ByTypes.xpath, false, 2).click(); 
	}
	
	public void checkTextandClickOnWalkthroughLink(int sequenceNum, boolean click) throws Exception {
		//WebElement element = webDriver.waitForElement("//ul[contains(@class,'settingsMenu__listItemW_help')]//li["+sequenceNum+"]/a", ByTypes.xpath);
		WebElement element = webDriver.waitForElement("settingsMenu__listItem_OnBoarding", ByTypes.id);
		Thread.sleep(3000);
		testResultService.assertEquals("Walkthrough", element.getText(), "Checking Walkthrough link text");
		if (click)
			element.click();
		
	}
	
	public void closeWalkthrough() throws Exception {
		WebElement element = webDriver.waitForElement("onBoarding__close", ByTypes.className);
		element.click();
		
	}
	
	
	public void checkTextandClickOnSysCheck(int sequenceNum, boolean click) throws Exception {
		//WebElement element = webDriver.waitForElement("//ul[contains(@class,'settingsMenu__listItemW_help')]//li["+sequenceNum+"]/a", ByTypes.xpath);
		WebElement element = webDriver.waitForElement("settingsMenu__listItem_SystemCheck", ByTypes.id);
		testResultService.assertEquals("System Check", element.getText(), "Checking Sys Check link text");
		if (click) 
			element.click();
		
	}

	public void closeTheProfileMenu() throws Exception {
		WebElement element = webDriver.waitForElement("//div[contains(@class,'home__userAvatar')]", ByTypes.xpath);
		webDriver.clickOnElementWithOffset(element, -50, 0);

		webDriver.checkElementNotExist("//ul[@class=" + userMenuClass + "]//li[1][@class='home__userDataMenuItem']");
		// testResultService.assertEquals(false, myProfile.isDisplayed(),
		// "Checking that My Profile is not displayed");

	}

	public String getUnitNumber(WebElement unitElement) throws Exception {
		return webDriver.getChildElementByXpath(unitElement, "//div[@class='home__details']//div[1]").getText();

	}

	public String getUnitName(WebElement element) throws Exception {
		String str = webDriver.getChildElementByXpath(element, "//h3[contains(@class,'detailsTitle')]").getText();

		return str;
	}

	public String getPrivacyStatemantLinkText() throws Exception {

		String text = webDriver
				.waitForElement("//ul[contains(@class,'siteTmpl__footerLinksInnerW')]/li[5]/a", ByTypes.xpath)
				.getText();
		return text;

	}

	public String getCourseName() throws Exception {
		String courseName = null;
		try {
			// courseName = webDriver.waitForElement(
			// "//div[contains(@class,'carouselCaptions')]//h3",
			// ByTypes.xpath, false, webDriver.getTimeout() + 10)
			// .getText();

			WebElement element = webDriver.waitForElement("//div[contains(@class,'carouselCaptions')]//h3",	ByTypes.xpath, false, webDriver.getTimeout());
						

			courseName = webDriver.getElementText(element);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			webDriver.printScreen("Problem getting course name");
			testResultService.addFailTest("Problem getting course name");
		}
		return courseName;

	}

	// public void clickOnUnitLessons(int unitNumber) throws Exception {
	// int rowCounter = unitNumber / 3;
	// clickOnUnitLessons(unitNumber, rowCounter);
	// }

	public String clickOnUnitLessons(int unitNumber) throws Exception {

		return clickOnUnitLessons(unitNumber, 0);
	}

	public String clickOnUnitLessons(int unitNumber, int xOffset) throws Exception {
		try {
			
			// retrieve the unit name
			WebElement unitNameElement = webDriver.waitForElement("//h3[@id='home__detailsTitle-"+unitNumber+"']/span[@class='textFitted']",ByTypes.xpath, false, webDriver.getTimeout());
			String name = unitNameElement.getText();
			
			int rowCounter = unitNumber / 3;
			
			WebElement element = webDriver.waitForElement(
					"//div[contains(@class,'home__allUnitsW')]//div[" + unitNumber + "]//div[1]//div[1]//div[2]//div[1]//div[1]",
					ByTypes.xpath, false, webDriver.getTimeout());
			
			if (element != null) {
				webDriver.scrollToElement(element, 0);		
				
				element = webDriver.waitForElement("//div[contains(@class,'home__allUnits')]//div[" + unitNumber + "]//div[1]//div[1]//div[2]//h3[contains(@class,'home__detailsTitle')]",
						ByTypes.xpath, "Unit Title element");
				
				element.click();
				
			
			} else {
				testResultService.addFailTest("Could not get lesson element", false, true);
			}
			// Thread.sleep(2000);

			// }
			return name;
		} catch (Exception e) {
			// TODO Auto-generated catch block

			String message = "Failed during click on unit lessons";
			// webDriver.printScreen(message);
			testResultService.addFailTest(message, true, true);
			return null;
		}
	}

	public void scrollToUnitElement(int unitNumber) throws Exception {

		scrollToUnitElement(unitNumber,0);
		// scrollToUnitElement(unitNumber, -200);
	}

	public void scrollToUnitElement(int unitNumber, int Yoffset) throws Exception {
		webDriver.scrollToElement(webDriver.waitForElement(
				"//div[contains(@class,'home__allUnits')]//div[" + unitNumber + "]//div[1]", ByTypes.xpath,false,webDriver.getTimeout()), Yoffset);
	}

	public NewUxLearningArea clickOnLesson(int unitIndex, int lessonIndex) throws Exception {

		try {
			if (!(webDriver instanceof AndroidWebDriver)) {
				if (lessonIndex > 5) {
					scrollToBottomOfLessonsElement(unitIndex);
					Thread.sleep(1000);
				}
				WebElement element = getLessonElement(unitIndex, lessonIndex);

				element.click();
			} else {
				if (lessonIndex > 5) {
					scrollToBottomOfLessonsElement(unitIndex);
				}
				WebElement element = getLessonElementAndroid(unitIndex, lessonIndex);

				element.click();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// webDriver.printScreen("Failed while trying to click on lessons");
			// System.out.println(e.toString());
			testResultService.addFailTest("Failed while trying to click on lessons", false, true);
		}
		
		return new NewUxLearningArea(webDriver, testResultService);
	}
	
	public NewUxLearningArea2 clickOnLesson2(int unitIndex, int lessonIndex) throws Exception {

		try {
			if (!(webDriver instanceof AndroidWebDriver)) {
				if (lessonIndex > 5) {
					scrollToBottomOfLessonsElement(unitIndex);
					Thread.sleep(1000);
				}
				WebElement element = getLessonElement(unitIndex, lessonIndex);

				element.click();
			} else {
				if (lessonIndex > 5) {
					scrollToBottomOfLessonsElement(unitIndex);
				}
				WebElement element = getLessonElementAndroid(unitIndex, lessonIndex);

				element.click();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// webDriver.printScreen("Failed while trying to click on lessons");
			// System.out.println(e.toString());
			testResultService.addFailTest("Failed while trying to click on lessons", false, true);
		}
		
		return new NewUxLearningArea2(webDriver, testResultService);
	}

	public String getLessonText(int unitIndex, int lessonIndex, String courseId) throws Exception {

		int index = lessonIndex + 1;
		if (index > 6) {
			scrollToBottomOfLessonsElement(unitIndex);
		}
		String lessonText = null;
		// return webDriver.waitForElement(
		// "//div[contains(@class,'home__allUnits')]//div[" + unitIndex
		// + "]//div[1]//div[3]//div//div[2]//div//ul/li["
		// + index + "]//span[2]//a", ByTypes.xpath).getText();

		try {

			if (webDriver instanceof AndroidWebDriver) {

				WebElement element = getLessonElementAndroid(unitIndex, index);
				lessonText = element.getText();
			} else {
				WebElement element = getLessonElement(unitIndex, index);

				lessonText = element.getText();
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			testResultService.addFailTest(
					"Text not found for unit: " + unitIndex + " ,lesson: " + lessonIndex + " course id: " + courseId);
			webDriver.printScreen();
		}
		
		if (index > 6) {
			scrollToTopOfLessonsElement(unitIndex);
		}
		
		return lessonText;
	}

	public WebElement getLessonElement(int unitIndex, int index) throws Exception {
		try { //new method following new scroll-bar
			return webDriver.getElement(By.xpath("//*[@id='home__courseListItem_id_" + index + "']/a/div[1]/span[contains(@class,'home__courseListItemName')]"));
		}
		catch (Exception e) { // old method following old scroll-bar //TODO delete once scroll-bar fix is released.
			return webDriver.waitForElement("//*[@id='home__unitIW_" + unitIndex + "']/div[1]/div[3]/div/div[2]/div/ul/li["
					+ index + "]/a/div[1]/span[2]", ByTypes.xpath, false, webDriver.getTimeout());
		}
	}
	
	public WebElement getLessonNumberElement(int unitIndex, int index) throws Exception {
		try { //new method following new scroll-bar
			return webDriver.getElement(By.xpath("//*[@id='home__courseListItem_id_" + index + "']/a/div[1]/span[contains(@class,'home__courseListItemNumber')]"));
		}
		catch (Exception e) { // old method following old scroll-bar //TODO delete once scroll-bar fix is released.
			return webDriver.waitForElement("//*[@id='home__unitIW_" + unitIndex + "']/div[1]/div[3]/div/div[2]/div/ul/li["
					+ index + "]/a/div[1]/span[1]", ByTypes.xpath, false, webDriver.getTimeout());
		}
	}

	public String getLessonProgress(int unitIndex, int index) throws Exception {
		WebElement element = null;
		if (!(webDriver instanceof AndroidWebDriver)) {
			element = getLessonProgressElement(unitIndex, index);
		} else {
			element = getLessonProgressElementAndroid(unitIndex, index);
		}

		WebElement svgElement = webDriver.getChildElementByCss(element, "svg");

		String content = null;
		if (webDriver instanceof IEWebDriver || webDriver instanceof SafariWebDriver) {
			content = webDriver.getElementHTML(element);
		} else {
			content = webDriver.getElementHTML(svgElement);
		}

		content = content.substring(content.indexOf("<g"), content.indexOf(">", content.indexOf("<g")));
		content = content.replace("<g class=", "");
		return content;
	}

	public boolean getLessonLockStatus(int unitIndex, int index) throws Exception {
		WebElement element = null;
		if (!(webDriver instanceof AndroidWebDriver)) {
			element = getLessonProgressElement(unitIndex, index);
		} else {
			element = getLessonProgressElementAndroid(unitIndex, index);
		}

		// WebElement svgElement = webDriver.getChildElementByCss(element,
		// "svg");

		// WebElement svgElement=webDriver.getElementsByTagName("svg").get(7);
		// webDriver.highlightElement(svgElement);
		//
		// WebElement webElement=webDriver.getChildElementByXpath(svgElement,
		// "//g[@class='lock']");
		// if(webElement!=null){
		// return true;
		// }
		// else{
		// return false;
		// }
		WebElement webElement;
		try { // new method for new scroll bar
			webElement = webDriver.getElement(By.xpath("//*[@id='home__courseListItem_id_" + index + "']/a/div[2]/span[contains(@class,'home__courseListItemProgress')]"));
			//webElement = webDriver.waitForElement("//*[@id='home__courseListItem_id_" + index + "']/a/div[2]/span[contains(@class,'home__courseListItemProgress')]", ByTypes.xpath, false, webDriver.getTimeout());
		}
		catch (Exception e) { // old method for old scroll-bar // TODO Remove this once new scroll-bar is released.
			webElement = webDriver.waitForElement("//*[@id='home__unitIW_" + unitIndex
					+ "']/div[1]/div[3]/div/div[2]/div/ul/li[" + index + "]//a//div[2]//span", ByTypes.xpath);
		}
		String state = webElement.getAttribute("title");
		if (state.contains("Locked")) {
			return true;
		} else {
			return false;
		}

		// String content = webDriver.getElementHTML(svgElement);
		// if (content.contains("class=\"lock")) {
		// return true;
		// } else {
		// return false;
		// }

	}

	private WebElement getLessonProgressElementAndroid(int unitIndex, int index) throws Exception {
		WebElement element;
		element = webDriver.waitForElement(
				"//*[@id='home__unitIW_" + unitIndex + "']/div[1]/div[3]/div/ul/li[" + index + "]/a/div[2]/span",
				ByTypes.xpath, false, webDriver.getTimeout());
		return element;
	}

	private WebElement getLessonProgressElement(int unitIndex, int index) throws Exception {
		WebElement element;
		try { // new method for new scroll bar
			element = webDriver.getElement(By.xpath("//*[@id='home__courseListItem_id_" + index + "']/a/div[2]/span[contains(@class,'home__courseListItemProgress')]"));
			//element = webDriver.waitForElement("//*[@id='home__courseListItem_id_" + index + "']/a/div[2]/span[contains(@class,'home__courseListItemProgress')]", ByTypes.xpath, false, webDriver.getTimeout());
		}
		catch (Exception e) { // old method for old scroll-bar // TODO Remove this once new scroll-bar is released.
			element = webDriver.waitForElement("//*[@id='home__unitIW_" + unitIndex
					+ "']/div[1]/div[3]/div/div[2]/div/ul/li[" + index + "]/a/div[2]/span", ByTypes.xpath, false,
					webDriver.getTimeout());
		}
		return element;
	}

	private WebElement getLessonElementAndroid(int unitIndex, int index) throws Exception {
		return webDriver.waitForElement(
				"//*[@id='home__unitIW_" + unitIndex + "']/div[1]/div[3]/div/ul/li[" + index + "]/a/div[1]/span[2]",
				ByTypes.xpath, false, webDriver.getTimeout());
	}

	public void scrollToBottomOfLessonsElement(int unitsIndex) throws Exception {
		try {

			// first try - did not work
			// WebElement lessonsElement = webDriver.waitForElement(
			// "//*[@id='home__unitIW_" + unitsIndex
			// + "']/div[1]/div[3]/div/div[1]/div", ByTypes.xpath);
			//
			// webDriver.scollInElement(lessonsElement);

			// second try - changing the style - did not work
			// WebElement element = webDriver.waitForElement(
			// "//*[@id='home__unitIW_" + unitsIndex
			// + "']/div[1]/div[3]/div/div[1]/div", ByTypes.xpath);
			// webDriver.changeElementStyle(element,
			// "height: 224px; top: 56px;");

			// 3rd try - dragging the scroll

			WebElement element = null;

			if (!(webDriver instanceof AndroidWebDriver)) {
				try { // new method for new scroll bar
					element = webDriver.getElement(By.xpath("//*[@id='home__unitIW_" + unitsIndex + "']//div[contains(@class,'mCSB_dragger_bar')]"));
					//element = webDriver.waitForElement("//*[@id='home__unitIW_" + unitsIndex + "']//div[contains(@class,'mCSB_dragger_bar')]", ByTypes.xpath,2, false, "Lessons scroll element");
				}
				catch (Exception e) { // old method for old scroll-bar // TODO Remove this once new scroll-bar is released.
					element = webDriver.waitForElement(
							"//*[@id='home__unitIW_" + unitsIndex + "']//div[1]//div[3]//div//div[1]//div", ByTypes.xpath,
							2, false, "Lessons scroll element");
				}
			} 
			else {
				element = webDriver.waitForElement("//*[@id='home__unitIW_" + unitsIndex + "']//div[1]//div[3]//div",
						ByTypes.xpath);
			}
			if (element != null) {
				WebElement scrollContainer;
			
				try{
					scrollContainer = webDriver.getElement(By.xpath("//*[@id='home__unitIW_" + unitsIndex + "']//div[contains(@class,'mCSB_draggerContainer')]"));
				
					WebElement lessonListScrollContainer = webDriver.getElement(By.xpath("//*[@id='home__unitIW_" + unitsIndex + "']//div[contains(@class,'mCustomScrollbar')]")); //find the Element of the new scroll bar 
					if (!lessonListScrollContainer.getAttribute("class").contains("mCS_no_scrollbar"))
					{
						int scrollContainerHeight = scrollContainer.getSize().height;
						int y = element.getLocation().y;
						int height = element.getSize().height;
						webDriver.dragScrollElement(element, scrollContainerHeight - height);
					}
				}
				
				// we can remove all the catch block when merge the code and new scroll to prod2 and production site.
				catch (Exception e) { // old method for old scroll-bar // TODO Remove this once new scroll-bar is released.
					scrollContainer = webDriver.waitForElement("scroll-bar", ByTypes.className);
					
					if (scrollContainer != null)
					{
						int scrollContainerHeight = scrollContainer.getSize().height;
						int y = element.getLocation().y;
						int height = element.getSize().height;
						webDriver.dragScrollElement(element, scrollContainerHeight - height);
					}
				}
				
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			testResultService.addFailTest("failed during scrollToBottomOfLessonsElement", false, true);
		}
	}
	
	public void scrollToTopOfLessonsElement(int unitsIndex) throws Exception {
		try {

			// first try - did not work
			// WebElement lessonsElement = webDriver.waitForElement(
			// "//*[@id='home__unitIW_" + unitsIndex
			// + "']/div[1]/div[3]/div/div[1]/div", ByTypes.xpath);
			//
			// webDriver.scollInElement(lessonsElement);

			// second try - changing the style - did not work
			// WebElement element = webDriver.waitForElement(
			// "//*[@id='home__unitIW_" + unitsIndex
			// + "']/div[1]/div[3]/div/div[1]/div", ByTypes.xpath);
			// webDriver.changeElementStyle(element,
			// "height: 224px; top: 56px;");

			// 3rd try - dragging the scroll

			WebElement element = null;

			if (!(webDriver instanceof AndroidWebDriver)) {
				try { // new method for new scroll bar
					element = webDriver.getElement(By.xpath("//*[@id='home__unitIW_" + unitsIndex + "']//div[contains(@class,'mCSB_dragger_bar')]"));
					//element = webDriver.waitForElement("//*[@id='home__unitIW_" + unitsIndex + "']//div[contains(@class,'mCSB_dragger_bar')]", ByTypes.xpath,2, false, "Lessons scroll element");
				}
				catch (Exception e) { // old method for old scroll-bar // TODO Remove this once new scroll-bar is released.
					element = webDriver.waitForElement(
							"//*[@id='home__unitIW_" + unitsIndex + "']//div[1]//div[3]//div//div[1]//div", ByTypes.xpath,
							2, false, "Lessons scroll element");
				}
			} 
			else {
				element = webDriver.waitForElement("//*[@id='home__unitIW_" + unitsIndex + "']//div[1]//div[3]//div",
						ByTypes.xpath);
			}
			if (element != null) {
				WebElement scrollContainer;
				try { // new method for new scroll bar
					scrollContainer = webDriver.waitForElement("//*[@id='home__unitIW_" + unitsIndex + "']//div[contains(@class,'mCSB_draggerContainer')]", ByTypes.xpath);
				}
				catch (Exception e) { // old method for old scroll-bar // TODO Remove this once new scroll-bar is released.
					scrollContainer = webDriver.waitForElement("scroll-bar", ByTypes.className);
				}
				int scrollContainerHeight = scrollContainer.getSize().height;
				int y = element.getLocation().y;
				int height = element.getSize().height;
				webDriver.dragScrollElement(element, height - scrollContainerHeight);
				//
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			testResultService.addFailTest("failed during scrollToTopOfLessonsElement", false, true);
		}
	}
	

	public void closeLastOpenLessonsElement() throws Exception {
		
		WebElement hoverElement = webDriver.waitForElement(
				"//div[contains(@class,'home__allUnits')]//div[" + 1 + "]//div[1]", ByTypes.xpath,false,3);
		
		if (hoverElement !=null){
			webDriver.clickOnElementWithOffset(hoverElement, -20, 0);
			// hoverElement.click();
		}
		
	}

	public void scrollUp() throws Exception {
		webDriver.scrollToTopOfPage();

	}

	public String getUnitIdTextByIndex(int number) throws Exception {
		WebElement element = webDriver.waitForElement(
				"//div[contains(@class,'home__allUnits')]//div[" + number + "]//div[1]//div[1]//div[2]//div[1]",
				ByTypes.xpath, "Unit text");
		return webDriver.getElementText(element);
	}

	public String getUnitNameByIndex(int number) throws Exception {
		return webDriver.waitForElement(
				"//div[contains(@class,'home__allUnits')]//div[" + number + "]//div[1]//div[1]//div[2]//h3",
				ByTypes.xpath, "Unit name").getText().trim();

	}

	public String getUnitStyleByIndex(int index) throws Exception {
		return webDriver
				.waitForElement("//div[contains(@class,'home__allUnits')]//div[" + index + "]//div[1]//div[1]//div[1]",
						ByTypes.xpath, "Unit style")
				.getAttribute("style");
	}

	public WebElement getUnitElementByIndex(int index) throws Exception {
		return webDriver.waitForElement(
				"//div[contains(@class,'home__allUnits')]//div[" + index + "]//div[1]//div[1]//div[1]", ByTypes.xpath,
				"Unit element");
	}

	

	
	public void clickOnHomeButton() throws Exception {
	
		try {
			WebElement homeElement= webDriver.waitForElement("sitemenu__itemHome",ByTypes.id, false, smallTimeOut);
			webDriver.hoverOnElement(homeElement);
			Thread.sleep(1000);
			WebElement element= webDriver.waitForElement("roof",ByTypes.id, false, smallTimeOut);
				element.click();
								
		} catch (Exception e) {
			testResultService.addFailTest("Home button element not found",false,true);
		}

	}

	public void clickOnHomeButton(Boolean continueToHome) throws Exception {
		
		try {
			WebElement homeElement= webDriver.waitForElement("sitemenu__itemHome",ByTypes.id, false, smallTimeOut);
			webDriver.hoverOnElement(homeElement);
			
			WebElement element= webDriver.waitForElement("roof",ByTypes.id, false, smallTimeOut);
				element.click();
								
				if (continueToHome){
					element= webDriver.waitForElement("carousel-inner",ByTypes.className , false, webDriver.getTimeout());
					webDriver.hoverOnElement(element);
					waitForPageToLoad();	
				}
				
				
		} catch (Exception e) {
			testResultService.addFailTest("Home button element not found",false,true);
		}

	}
	
		
	
	public void clickOnContinueButton(int index) throws Exception {

			try{
				webDriver.waitForElement("//*[@id='home__unitIW_" + index + "']/div[1]/div[1]/div/a/span", ByTypes.xpath,true,webDriver.getTimeout())
				.click();
		
			} catch (Exception e) {
				// TODO Auto-generated catch block
				testResultService.addFailTest("Continue button element not found");
			}
	}

	public void checkThatLessonIsNotClickable(int unitIndex, int index) throws Exception {
		WebElement element;

		if (webDriver instanceof AndroidWebDriver) {

			element = getLessonElementAndroid(unitIndex, index);

		} else {
			element = getLessonElement(unitIndex, index);
			String currentUrl = webDriver.getUrl();
			element.click();
			testResultService.assertEquals(currentUrl, webDriver.getUrl(), "Lesson was clicked when it should not");

		}

	}

	private boolean checkIfLessonElementClickable(int unitIndex, int index) throws Exception {
		return webDriver.checkElementEnabledAndClickable("//*[@id='home__unitIW_" + unitIndex
				+ "']/div[1]/div[3]/div/div[2]/div/ul/li[" + index + "]/a/div[1]/span[2]");

	}

	public void clickOutsideUnitLessons(int index) throws Exception {
		WebElement element = webDriver.waitForElement(
				"//div[contains(@class,'home__allUnits')]//div[" + index + "]//div//div//div[3]", ByTypes.xpath,
				"Outsode unit lessons");
		// Actions builder = new Actions(webDriver);
		// builder.moveToElement(element, -30, 0);

		webDriver.clickOnElementWithOffset(element, -50, 0);

	}

	public double getUnitProgressBarValue(int index) throws Exception {

		// scrollToBottomOfLessonsElement(index);
		WebElement element = webDriver.waitForElement("//*[@id='home__unitIW_" + index + "']/div[1]/div[2]/div[2]/div",
				ByTypes.xpath);
		String childHtml = webDriver.getElementHTML(element);

		// String value = childHtml.substring(childHtml.indexOf("width:"),
		// childHtml.indexOf("><"));

		String value = childHtml.substring(childHtml.indexOf("style=\"width"),
				childHtml.indexOf("%;", childHtml.indexOf("style=\"width")));

		// webDriver.highlightElement(element);
		// WebElement child = webDriver.getChildElementByXpath(element,
		// "/div[contains(@class,'home__detailsProgressItem')]");

		value = value.replace("style=\"width: ", "");
		// value = value.replace("%;", "");
		// value = value.replace("\"", "");

		// if (value.substring(value.indexOf("."), value.length()).length() > 4)
		// {
		// value = value.substring(0, value.indexOf(".") + 5);
		// }
		double progressValue = Double.valueOf(value);

		progressValue = textService.round(progressValue, 4);

		return progressValue;

	}

	public String getUnitProgressToolTip(int index) throws Exception {
		WebElement element = webDriver.waitForElement("//*[@id='home__unitIW_" + index + "']/div[1]/div[2]/div[2]/div",ByTypes.xpath);
		String progress = element.getAttribute("title");
	
		return progress;
	}
	
	public NewUxLearningArea navigateToCourseUnitAndLesson(String[] courseCodes, String courseCode, int unitNumber,
			int lessonNumber) throws Exception {

		return navigateToCourseUnitAndLesson(courseCodes, courseCode, unitNumber, lessonNumber, false, null);
	}
	
	public NewUxLearningArea2 navigateToCourseUnitLessonLA2(String[] courseCodes, String courseCode, int unitNumber,
			int lessonNumber) throws Exception {

		return navigateToCourseUnitLessonLA2(courseCodes, courseCode, unitNumber, lessonNumber, false, null);
	}
	

	// navigate for caba
	
	public NewUxLearningArea2 navigateToCourseUnitLessonLA2CABA(String[] courseCodesCABA , String courseCode, int unitNumber,
			int lessonNumber) throws Exception {

		return navigateToCourseUnitLessonLA2CABA(courseCodesCABA, courseCode, unitNumber, lessonNumber, false, null);
	}
	
	// navigate for TOEIC
	
		public NewUxLearningArea2 navigateToCourseUnitLessonLA2TOEIC(String[] courseCodesTOEIC , String courseCode, int unitNumber,
				int lessonNumber) throws Exception {

			return navigateToCourseUnitLessonLA2TOEIC(courseCodesTOEIC, courseCode, unitNumber, lessonNumber, false, null);
		}
	
	
	public NewUxLearningArea navigateToCourseUnitAndLesson(String[] courseCodes, String courseCode, int unitNumber,
			int lessonNumber, boolean setProgress, String studentId) throws Exception {

		int courseIndex = getCourseIndexByCode(courseCodes, courseCode);

		if (setProgress == false) {
			//navigateToCourse(courseIndex + 1);
			navigateToRequiredCourseOnHomePage(coursesNames[courseIndex]);
		} else {
			String courseId = courses[getCourseIndexByCode(courseCodes, courseCode)];
			// String unitId = studentService.getCourseUnits(courseId).get(0);
			studentService.setProgressInFirstComponentInUnit(0, studentId, courseId);

			webDriver.refresh();
		}
		// String courseName =
		// dbService.getCourseNameById(courses[courseIndex]);
		// checkThatCourseDataIsLoaded(courseName);
		Thread.sleep(1500);
		clickOnUnitLessons(unitNumber);
		clickOnLesson(unitNumber - 1, lessonNumber);
		return new NewUxLearningArea(webDriver, testResultService);

	}
	
	public NewUxLearningArea2 navigateToCourseUnitLessonLA2(String[] courseCodes, String courseCode, int unitNumber,
			int lessonNumber, boolean setProgress, String studentId) throws Exception {

		int courseIndex = getCourseIndexByCode(courseCodes, courseCode);

		if (setProgress == false) {
			//navigateToCourse(courseIndex + 1);
			navigateToRequiredCourseOnHomePage(coursesNames[courseIndex]);
		} else {
			String courseId = courses[getCourseIndexByCode(courseCodes, courseCode)];
			// String unitId = studentService.getCourseUnits(courseId).get(0);
			studentService.setProgressInFirstComponentInUnit(0, studentId, courseId);

			webDriver.refresh();
			checkThatCourseDataIsLoaded(coursesNames[courseIndex]);
		}
		// String courseName =
		// dbService.getCourseNameById(courses[courseIndex]);
		checkThatCourseDataIsLoaded(coursesNames[courseIndex]);
		//Thread.sleep(1500);
		clickOnUnitLessons(unitNumber);
		clickOnLesson(unitNumber - 1, lessonNumber);
		NewUxLearningArea2 learningArea2 = new NewUxLearningArea2(webDriver, testResultService);
		//learningArea2.waitForPageToLoad();
		learningArea2.waitUntilLearningAreaLoaded();
		return learningArea2;
		//return new NewUxLearningArea2(webDriver, testResultService);

	}

	//navigate for Caba
	
	public NewUxLearningArea2 navigateToCourseUnitLessonLA2CABA(String[] courseCodesCABA, String courseCode, int unitNumber,
			int lessonNumber, boolean setProgress, String studentId) throws Exception {

		int courseIndex = getCourseIndexByCode(courseCodesCABA, courseCode);

		if (setProgress == false) {
			//navigateToCourse(courseIndex + 1);
			navigateToRequiredCourseOnHomePage(coursesNamesCABA[courseIndex]);
		} else {
			String courseId = courses[getCourseIndexByCode(courseCodesCABA, courseCode)];
			// String unitId = studentService.getCourseUnits(courseId).get(0);
			studentService.setProgressInFirstComponentInUnit(0, studentId, courseId);

			webDriver.refresh();
		}
		Thread.sleep(1500);
		clickOnUnitLessons(unitNumber);
		clickOnLesson(unitNumber - 1, lessonNumber);
		return new NewUxLearningArea2(webDriver, testResultService);

	}
	
	public NewUxLearningArea2 navigateToCourseUnitLessonLA2TOEIC(String[] courseCodesTOEIC, String courseCode, int unitNumber,
			int lessonNumber, boolean setProgress, String studentId) throws Exception {

		int courseIndex = getCourseIndexByCode(courseCodesTOEIC, courseCode);

		if (setProgress == false) {
			//navigateToCourse(courseIndex + 1);
			navigateToRequiredCourseOnHomePage(coursesNamesTOEIC[courseIndex]);
		} else {
			String courseId = courses[getCourseIndexByCode(courseCodesTOEIC, courseCode)];
			// String unitId = studentService.getCourseUnits(courseId).get(0);
			studentService.setProgressInFirstComponentInUnit(0, studentId, courseId);

			webDriver.refresh();
		}
		Thread.sleep(1500);
		clickOnUnitLessons(unitNumber);
		clickOnLesson(unitNumber - 1, lessonNumber);
		return new NewUxLearningArea2(webDriver, testResultService);

	}
	
	public void checkThatCourseDataIsLoaded(String courseName) throws Exception {

		boolean found = false;
		int elapsesTime = 0;
		while (found == false) {
			String currentCourseName = getCurrentCourseName(); // getCourseName(); //getCurrentCourseName()
			if (currentCourseName.equals(courseName)) {
				found = true;
			}
			Thread.sleep(1000);
			elapsesTime++;
			if (elapsesTime == 10) {
				testResultService.addFailTest("Course name: " + courseName + " not found while navigating", true, true);
				found = true;
			}
		}

	}

	public int getCourseIndexByCode(String[] courseCodes, String courseCode) {
		int index = 0;
		for (int i = 0; i < courseCodes.length; i++) {
			if (courseCode.equals(courseCodes[i])) {
				index = i;
				break;
			}
		}
		return index;
	}

	public void navigateToCourse(int courseNumber) throws Exception {
		for (int j = 0; j < courseNumber - 1; j++) {
			//carouselNavigateNext(j + 1);
			carouselNavigateNext();
			Thread.sleep(1000);
		}
	}

	public void scrollToUnitProgressBar(int index) throws Exception {
		WebElement element = webDriver.waitForElement("//*[@id='home__unitIW_" + index + "']/div[1]/div[2]/div[2]/div",
				ByTypes.xpath);
		webDriver.scrollToElement(element, 0);

	}

	public String getUnitText(int unitIndex) throws Exception {
		return webDriver
				.waitForElement("//*[@id='home__unitIW_" + unitIndex + "']/div[1]/div[1]/div/div", ByTypes.xpath)
				.getText();
	}

	public String getNextLesosnName() throws Exception {
		return webDriver.waitForElement("//div[contains(@class,'carouselCaptions')]//ul//li[2]", ByTypes.xpath,false,webDriver.getTimeout())
				.getText();
	}

	public void checkUnitLessonNameInCourseArea(String expectedUnitName, String expectedLessonName) throws Exception {
		
		String currentUnitName = getUnitTitleInCourseArea();
		testResultService.assertEquals(expectedUnitName, currentUnitName, "Unit name not found/do not match");
		
		String currentLessonName = getLessonTitleInCourseArea();
		testResultService.assertEquals(expectedLessonName, currentLessonName, "Lesson name not found/do not match");
				
	}
	
	
	public String getUnitTitleInCourseArea() throws Exception {
						
		WebElement element = getCurrentSlideInCourseArea();
		webDriver.waitUntilElementAppears("carouselPathUnitOrdinal", ByTypes.className, 20);
		
		 String text="";
			Thread.sleep(1000);
			text = element.findElement(By.className("carouselPathUnitOrdinal")).getText();	
			
			for (int i=0;i<20 && text.equalsIgnoreCase("");i++){
				text = element.findElement(By.className("carouselPathUnitOrdinal")).getText();
				Thread.sleep(1000);
			}
		
		if (!(text.isEmpty() || text.equalsIgnoreCase("") || text.equalsIgnoreCase(null))) {
			return text;
		} else
			webDriver.printScreen("Unit Title Not Found");
		return text;
	}

	public String getLessonTitleInCourseArea() throws Exception {
	
		WebElement element = webDriver.waitUntilElementAppears("carouselPathLessonNAme", ByTypes.className, 10);
		
		if (element != null) {
			return element.getText();
		} else
			webDriver.printScreen("Lesson Title Not Found");
		return null;
	}
	
	private WebElement getCurrentSlideInCourseArea() throws Exception {
		return webDriver.waitUntilElementAppearsAndReturnElement("//div[contains(@class,'BSslide') and contains(@class,'active')]",5);
		
		//return webDriver.waitForElement("//div[contains(@class,'BSslide') and contains(@class,'active')]", ByTypes.xpath, false, webDriver.getTimeout());
	}

	public NewUxLearningArea2 checkTextandClickOnCourseBtn(String label) throws Exception {

		waitUntilCourseAreaLoaded(10);			
		
		WebElement element = webDriver.waitForElement(
				"//div[contains(@class,'BSslide')][contains(@class,'active')]//div[contains(@class,'carouselStartBtnW')]//a",
				ByTypes.xpath,false,webDriver.getTimeout());
		
		String actualBtnLabel = element.getText();
		
		testResultService.assertEquals(label, actualBtnLabel);
		
		WebElement button = webDriver.waitForElement(
				"//div[contains(@class,'BSslide')][contains(@class,'active')]//div[contains(@class,'carouselStartBtnW')]//a",
				ByTypes.xpath,true,webDriver.getTimeout());
		button.click();
		
		return new NewUxLearningArea2(webDriver, testResultService);		
	}

	public void waitUntilCourseAreaLoaded(int wait) {
		webDriver.waitUntilElementAppears(
				"//div[contains(@class,'BSslide')][contains(@class,'active')]//div[contains(@class,'carouselStartBtnW')]//a",
				ByTypes.xpath,wait);
	}

	public String getCompletionWidgetValue() throws Exception {

		WebElement element = null;
		boolean textFound = false;
		int timeout = 10;
		while (textFound == false) {
			int elapasedTime = 0;
			webDriver.waitUntilElementAppears("home__studentWidgetCompletionData", ByTypes.id,10);
			element = webDriver.waitForElement("home__studentWidgetCompletionData", ByTypes.id,1,false);
			if (element.getText().length() > 0) {
				textFound = true;
			} else {
				Thread.sleep(1000);
				elapasedTime++;
			}
			if (elapasedTime == timeout) {
				break;
			}
		}

		return element.getText();

	}

	public String getScoreWidgetValue() throws Exception  {
		webDriver.waitUntilElementAppears("home__studentWidgetTestScoresData", ByTypes.id,10);
		WebElement element;
		String score="0";
		
		try {
			element = webDriver.waitForElement("home__studentWidgetTestScoresData", ByTypes.id,1,false);
			score = element.getText();
			
		} catch (Exception e) {
			score="0";
			return score;
		}
		
		if (score.isEmpty())
			score="0";
		
		return score;
	}

	public String getTimeOnTaskWidgetValue() throws Exception {

		String timeWidgetHr = null;

		String timeWidgetMin = null;
		boolean textFound = false;
		int elapsedTime = 0;
		while (textFound == false) {
			timeWidgetHr = webDriver.waitForElement("//div[@class='home__studentWidgetHrMin']/span[1]", ByTypes.xpath)
					.getText();
			if (timeWidgetHr.length() > 0) {
				textFound = true;
			} else {
				elapsedTime++;
				Thread.sleep(1000);
				if (elapsedTime == webDriver.getTimeout()) {
					break;
				}
			}
			timeWidgetMin = webDriver.waitForElement("//div[@class='home__studentWidgetHrMin']/span[3]", ByTypes.xpath)
					.getText();
		}

		String timeHrMin = timeWidgetHr + ":" + timeWidgetMin;

		return timeHrMin;

	}

	public String getLessonProgressStatus(int unitIndex, int lessonNumber) throws Exception {
		WebElement element = webDriver.waitForElement(
				"//[@id='home__unitIW_" + unitIndex + "']/div[1]/div[3]/div/div[2]/div/ul/li[" + lessonNumber + "]/a",
				ByTypes.xpath);
		return element.getAttribute("class");

	}

	public void checkUnitBtnNotDisplayed(int unitStartCheck, int unitEndCheck) throws Exception {

		for (int i = unitStartCheck; i < (unitEndCheck + 1); i++) {
			WebElement isBtnExist = hoverOnUnitAndGetBtnElement(i, false);
			if (!(isBtnExist == null)) {
				Boolean Exists = true;
				testResultService.assertEquals(false, Exists, "Unit Btn Displayed");
			}
		}

	}

	public WebElement hoverOnUnitAndGetBtnElement(int unitNum, boolean isButonMandatory) throws Exception {

		
		scrollToUnitElement(unitNum, 0);
		Thread.sleep(500);
		WebElement element = webDriver.waitForElement(
				"//div[contains(@class,'home__allUnits')]//div[" + unitNum + "]//div[1]//div[1]//div[2]//div[1]",
				ByTypes.xpath, "Unit element");

		webDriver.hoverOnElement(element);
		WebElement button = webDriver.waitForElement(
				"//div[contains(@class,'home__allUnits')]//div[" + unitNum
						+ "]//a[contains(@class,'home__units_lessonsContinue')]",
				ByTypes.xpath, false, smallTimeOut);
		
		if (button == null && isButonMandatory == true) {
			testResultService.addFailTest("FAILED DURING hoverOnUnitAndGetBtnElement", true, true);

		}

		return button;
	}
	
	public WebElement hoverOnUnitImageAndGetBtnElement(int unitNum, boolean isButonMandatory) throws Exception {

		
		scrollToUnitElement(unitNum, 0);
		Thread.sleep(500);
		WebElement element = webDriver.waitForElement(
				"//div[contains(@class,'home__allUnits')]//div[" + unitNum + "]//div[1]//div[1]//div[2]//div[1]",
				ByTypes.xpath,webDriver.getTimeout(),false, "Unit element");

		webDriver.hoverOnElement(element);
		
		WebElement buttonArea = webDriver.waitForElement(
				"//div[contains(@class,'home__allUnits')]//div[" + unitNum + "]//div[1]//div[1]//div[1]",
				ByTypes.xpath, "Unit element");
		
		
		webDriver.hoverOnElement(buttonArea);
				
						
		WebElement button = webDriver.waitForElement(
				"//div[contains(@class,'home__allUnits')]//div[" + unitNum
						+ "]//a[contains(@class,'home__units_lessonsContinue')]",
				ByTypes.xpath, false, webDriver.getTimeout());
		
		if (button == null && isButonMandatory == true) {
			testResultService.addFailTest("FAILED DURING hoverOnUnitImageAndCheckBtnHighlighted", true, true);

		}

		return button;
	}
	
	public WebElement hoverOnUnitTitleInUnitBlockAndGetElement(int unitNum) throws Exception {

		
		scrollToUnitElement(unitNum, 0);
		Thread.sleep(500);
		WebElement element = webDriver.waitForElement(
				"//div[contains(@class,'home__allUnits')]//div[" + unitNum + "]//div[1]//div[1]//div[2]//div[1]",
				ByTypes.xpath, "Unit element");

		webDriver.hoverOnElement(element);
		
		element = webDriver.waitForElement(
				"//div[contains(@class,'home__allUnits')]//div[" + unitNum + "]//div[1]//div[1]//div[2]//h3[contains(@class,'home__detailsTitle')]",
				ByTypes.xpath, "Unit Title element");

		webDriver.hoverOnElement(element);
				
		return element;
	}
	
	public void checkThatUnitLessonListNotDisplayed(int unitNum) throws Exception {
	
		//scrollToUnitElement(unitNum, 200);
		Thread.sleep(1000);
		WebElement element = webDriver.waitForElement("//div[contains(@class,'home__allUnits')]//div[" + unitNum + "]//div[1]//div[1]/div[contains(@class,'home__courseList')]", ByTypes.xpath, smallTimeOut, false, "Lesson List element");
		if (element != null) testResultService.addFailTest("Lesson List still opened");
		
	}
	

	public int getAllUnitsBarUnitProgress(int i) throws Exception {
		
		WebElement unitProgress;
		
		unitProgress = getAllUnitsBarUnitElement(i);
		String value = unitProgress.getAttribute("style").toString();
		value = value.replace("width: ", "");
		value = value.replace("%;", "");
		int result = (int) Double.parseDouble(value);
				
		return result;

	}

	private WebElement getAllUnitsBarUnitElement(int index) throws Exception {
		WebElement unitProgress = webDriver.waitUntilElementAppears(allUnitsBar + "/div[" + index + "]", ByTypes.xpath,10);

		return unitProgress;
	}

	public String getAllUnitsUnitColor(int i) throws Exception {
		//int index = i + 1;
		WebElement unitProgress = getAllUnitsBarUnitElement(i);
		Thread.sleep(1000);
		unitProgress = unitProgress.findElement(By.tagName("rect"));
		String color = unitProgress.getCssValue("fill");
		return color;
	}

	public String getAllUnitsBarTooltip() throws Exception {
		WebElement element = webDriver.waitForElement(allUnitsBar, ByTypes.xpath);
		return element.getAttribute("title");
	}

	public WebElement getUnitLockedElement(int unitSequence, boolean expectedState) throws Exception {
		int unitNum = unitSequence-1;
		
		//webDriver.waitUntilElementAppears("//*[@id='home__unitIW_"+ unitNum +"']/div[1]/div[1]/div/div", 10);
		
		WebElement element = webDriver.waitForElement(
				"//*[@id='home__unitIW_"+ unitNum +"']/div[1]/div[1]/div/div/ed-lock-icon",
				ByTypes.xpath, 1, false, "Unit lock");

		if (element == null && expectedState == true) {
			webDriver.printScreen("Unit locked element not found");
		} 
		
		if (element != null && expectedState == false) {
			webDriver.printScreen("Unit locked though it should not");
		} 
		
		
		return element;
	}

	public NewUxMyProfile clickOnMyProfile() throws Exception {
		//clickOnUserAvatar();
		//checkTextAndclickOnMyProfile();
		
		webDriver.waitForElement("//li[contains(@class,'settingsMenu__personal')]", ByTypes.xpath).click();
		WebElement element=null;
		
		//element = webDriver.waitUntilElementAppears("My Profile", ByTypes.linkText,3);
		element = webDriver.waitUntilElementAppears("//li[contains(@class,'listItem_myProfile')]", ByTypes.xpath,3);

		if (element == null) {
			element = webDriver.waitUntilElementAppears("Meu Perfil", ByTypes.linkText, 1);
			if (element == null) {
				element = webDriver.waitUntilElementAppears("Mi perfil", ByTypes.linkText, 1);
			}
		}
		element.click();
		
		webDriver.sleep(2);  // igb 2018.11.11
		
		return new NewUxMyProfile (webDriver, testResultService); 
	}
	
	public void clickWithJS() {
		WebElement element = webDriver.findElementByXpath("//li[contains(@class,'settingsMenu__personal')]",ByTypes.xpath);
		JavascriptExecutor executor = (JavascriptExecutor)webDriver.getWebDriver();
		executor.executeScript("arguments[0].click();", element);
	}




	public void clickOnMyProfileWithoutTextCheck() throws Exception {
		clickOnUserAvatar();
		Thread.sleep(3);
		WebElement element = webDriver.waitForElement("//li[contains(@class, 'settingsMenu__listItem_myProfile')]/a", ByTypes.xpath);
		element.click();
	}

	public void switchToMyProfile() throws Exception {
		webDriver.switchToFrame(myProfileFrameName);
	}

	public boolean waitForExpectedCourseName(String expectedCourseName, int courseIndex) throws Exception {
		// TODO Auto-generated method stub
		boolean match = false;
		int elapsedTime = 0;
		while (match == false) {
			String currentCourseName = getCurrentCourseElement(courseIndex).getText();
			if (expectedCourseName.equals(currentCourseName)) {
				match = true;
			} else {
				elapsedTime++;
				Thread.sleep(1000);
				if (elapsedTime == webDriver.getTimeout()) {
					break;
				}
			}
		}
		return match;
	}

	public void validateUnAssignedCoursesMessage() throws Exception {
		
		WebElement topElement = null;
		
		for (int i =0; topElement==null && i<5; i++){
			topElement = webDriver.waitForElement("//*[@id='mainContent']/div/div/section[3]/div", ByTypes.xpath, smallTimeOut, false);
			Thread.sleep(1000);
		}
			//Top text
			WebElement element = webDriver.waitForElement(DIV_CONTAINS_CLASS_HOME_NOT_ASSIGNED_TXT + "//h3", ByTypes.xpath,false,webDriver.getTimeout());
			String text = element.getText();
			testResultService.assertEquals(Welcome_Message, text);

			//Middle text
			element = webDriver.waitForElement(DIV_CONTAINS_CLASS_HOME_NOT_ASSIGNED_TXT + "//p[1]", ByTypes.xpath,false,webDriver.getTimeout());
			text = element.getText();
			testResultService.assertEquals(Middle_Welcome_message, text);
		
			//Last text
			element = webDriver.waitForElement(DIV_CONTAINS_CLASS_HOME_NOT_ASSIGNED_TXT + "//p[2]", ByTypes.xpath,false,webDriver.getTimeout());
			text = element.getText();
			testResultService.assertEquals(Last_Welcome_message, text);
		
			
		webDriver.sleep(2);
	}
	
	public void verifyHomePageMessageStartCourseByPLT() throws Exception {
		
		String text = "undefined";
		
		WebElement element = webDriver.waitUntilElementAppearsAndReturnElement(DIV_CONTAINS_CLASS_HOME_NOT_ASSIGNED_TXT + "//h3", 10);
		if (element != null) 
			text = element.getText();
		testResultService.assertEquals(Welcome_Message, text, "Message on Home Page is not valid or not found");
				
		element = webDriver.waitForElement(DIV_CONTAINS_CLASS_HOME_NOT_ASSIGNED_TXT + "//p", ByTypes.xpath, false, webDriver.getTimeout());
		if (element != null)
			text = element.getText();
		//testResultService.assertEquals(Click_On_Assessments_To_Take_PLT_Message, text, "Message on Home Page is not valid or not found");
		
		if (!text.contains(Click_On_Assessments_To_Take_PLT_Message))
			testResultService.addFailTest("Text on Home page not match", false, true);
			
				
	}

	public void UpdateMyProfile() throws Exception {
		WebElement firstNameElement = webDriver.waitForElement("FirstName", ByTypes.name);
		firstNameElement.clear();
		firstNameElement.sendKeys("New Name");

		for (int i = 0; i < 9; i++) {
			webDriver.sendKey(Keys.TAB);
		}

		webDriver.waitForElement("submitMyProfile", ByTypes.id).click();
	}
	
	public void OpenDictionary() throws Exception {
		WebElement element = webDriver.waitForElement("//h2[text() = 'All Units']", ByTypes.xpath);
				
		Actions action = new Actions((WebDriver) webDriver);
		action.moveToElement(element).contextClick(element).perform();
		
	}
	
	public void clickButtonOnNavBar(String itemId) throws Exception {
		webDriver.waitUntilElementAppears(UL_ID_SITEMENU_ITEM_HOME + "//li[@id='" + itemId + "']/a", ByTypes.xpath, 10);
		WebElement element = webDriver.waitForElement(UL_ID_SITEMENU_ITEM_HOME + "//li[@id='" + itemId + "']/a", ByTypes.xpath);
		element.click();
		
	}
	
	public void clickButtonTooltipOnNavBar(String itemId) throws Exception {
		
		webDriver.waitUntilElementAppears(UL_ID_SITEMENU_ITEM_HOME + "//li[@id='" + itemId + "']/a//div[contains(@class,'sitemenu__itemTooltip')]", 5);
		WebElement element = webDriver.waitForElement(UL_ID_SITEMENU_ITEM_HOME + "//li[@id='" + itemId + "']/a//div[contains(@class,'sitemenu__itemTooltip')]", ByTypes.xpath);
		element.click();
			
	}
	
	public NewUxInstitutionPage openInstitutionPage(boolean clickOnTooltip) throws Exception {
		
		if (clickOnTooltip) {
			hoverOnNavBarItemAndVerivyTooltip("sitemenu__itemInstPage");
			clickButtonTooltipOnNavBar ("sitemenu__itemInstPage"); 
		}
		else clickButtonOnNavBar("sitemenu__itemInstPage");
		
		webDriver.sleep(1);  // igb 2018.11.13
		
		switchToInstPage();
		
		return new NewUxInstitutionPage(webDriver,testResultService);
	}
	
	public NewUxCommunityPage openCommunityPage(boolean clickOnTooltip) throws Exception {
		
		if (clickOnTooltip) {
			hoverOnNavBarItemAndVerivyTooltip("sitemenu__itemCommunity");
			clickButtonTooltipOnNavBar ("sitemenu__itemCommunity");			
		}
		else clickButtonOnNavBar("sitemenu__itemCommunity");
		
		webDriver.sleep(2);  // igb 2018.11.13
		
		webDriver.switchToNextTab();
		
		return new NewUxCommunityPage(webDriver,testResultService);
	}
	
	public NewUxCommunityPage openNewCommunityPage(boolean clickOnTooltip) throws Exception {
		
		if (clickOnTooltip) {
			hoverOnNavBarItemAndVerivyTooltip("sitemenu__itemCommunity");
			clickButtonTooltipOnNavBar ("sitemenu__itemCommunity");			
		}
		else {clickButtonOnNavBar("sitemenu__itemCommunity"); 
			
		}
		
		webDriver.sleep(2);  // igb 2018.11.13
		
		return new NewUxCommunityPage(webDriver,testResultService);
	}
	
	public NewUxAssignmentsPage openAssignmentsPage(boolean clickOnTooltip) throws Exception {
		
		if (clickOnTooltip) {
			hoverOnNavBarItemAndVerivyTooltip("sitemenu__itemMyAssignments");
			clickButtonTooltipOnNavBar("sitemenu__itemMyAssignments");			
		}
		else clickButtonOnNavBar("sitemenu__itemMyAssignments");

		webDriver.sleep(2);  // igb 2018.11.13
		
		switchToFrameInModal();
		
		return new NewUxAssignmentsPage(webDriver,testResultService);
	}
	
	public void switchToFrameInModal() throws Exception {
		String curWindow = "";
			
		for (int i=0;i<=10 && curWindow.length()==0 ;i++){
					curWindow = webDriver.switchToFrame("bsModal__iframe",false);
					Thread.sleep(500);
		}
	}
	
	public NewUxAssessmentsPage openAssessmentsPage(boolean clickOnTooltip) throws Exception {
		
		if (clickOnTooltip) {
			hoverOnNavBarItemAndVerivyTooltip("sitemenu__itemTests");
			clickButtonTooltipOnNavBar("sitemenu__itemTests");			
		}
		else clickButtonOnNavBar("sitemenu__itemTests");

		webDriver.waitUntilElementAppears("//div[contains(@class,'assessments__main')]", 20);
		//webDriver.sleep(4);  // igb 2018.11.13
		
		return new NewUxAssessmentsPage(webDriver,testResultService);
	}
	
	public NewUxMessagesPage openInboxPage(boolean clickOnTooltip) throws Exception {
		
		if (clickOnTooltip) {
			hoverOnNavBarItemAndVerivyTooltip("sitemenu__itemMessages");
			clickButtonTooltipOnNavBar("sitemenu__itemMessages");			
		}
		else clickButtonOnNavBar("sitemenu__itemMessages");

		webDriver.sleep(2);  // igb 2018.11.13
		
		webDriver.switchToNextTab();
				
		return new NewUxMessagesPage(webDriver,testResultService);
	}
	
	public NewUxGrammarBook openGrammarBookPage(boolean clickOnTooltip) throws Exception {
		
		if (clickOnTooltip) {
			hoverOnNavBarItemAndVerivyTooltip("sitemenu__itemGrammarBook");
			clickButtonTooltipOnNavBar("sitemenu__itemGrammarBook");			
		}
		else clickButtonOnNavBar("sitemenu__itemGrammarBook");

		webDriver.sleep(2);  // igb 2018.11.13
		
		switchToGrammarBook();
				
		return new NewUxGrammarBook(webDriver,testResultService);
	}
	
	public void switchToGrammarBook() throws Exception {
		switchToFrameInModal();
	}
	
	public void switchToInstPage() throws Exception {
		webDriver.switchToNewWindow();
	}
	
	public void switchToStudyPlanner() throws Exception {
		switchToFrameInModal();
	}
	
	public void switchToDictionaryDialog() throws Exception {
		
		if (PageHelperService.branchCI.contains("app")){
			
			webDriver.switchToFrame(webDriver.waitForElement("DictionaryIF", ByTypes.id));
			webDriver.switchToFrame(webDriver.waitForElement("wd_ResultFrame", ByTypes.id));
		//webDriver.switchToFrame("wd_ResultFrame");
		}
	}
	
	public void closeDictionaryDialog() throws Exception {
		//webDriver.waitForElement("//div[contains(@class,'DictionaryClose')]/a", ByTypes.xpath).click();
		webDriver.waitForElement("//div[@id='DictionaryPanelContainer']/div/div[2]/a", ByTypes.xpath).click();

	}
	
	public void cancelLogOut() throws Exception {
				
		webDriver.switchToFrame(webDriver.waitForElement("//div[@id='EdoFrameBoxContent']/iframe", ByTypes.xpath));
		webDriver.waitForElement("btnCancel", ByTypes.id).click();
		webDriver.switchToMainWindow();
		
	}
	
	public void clickOnWalkthrough() throws Exception {
		clickOnHelp();
		webDriver.waitForElement("Walkthrough", ByTypes.linkText).click();
	}
	
	public void clickOnVideoTutorials() throws Exception {
		clickOnHelp();
		webDriver.waitForElement("settingsMenu__listItem_videoTutorials", ByTypes.id).click();

		
	}
	
	
	public void clickOnHelp() throws Exception {
		getHelpBtnElement().click();

	}
	
	public WebElement getHelpBtnElement() throws Exception {
		WebElement element = webDriver.waitForElement("//li[contains(@class, 'settingsMenu__help')]", ByTypes.xpath, webDriver.getTimeout(), true, "Verify Help Btn");
		return element;

	}
	
	public void clickOnCourseList() throws Exception {
		if(getCourseListBtnElement()!= null);
			getCourseListBtnElement().click();

	}
	
	public WebElement getCourseListBtnElement() throws Exception {
		//WebElement element = webDriver.waitForElement("//li[contains(@class, 'settingsMenu__courseList')]", ByTypes.xpath, webDriver.getTimeout(), true, "Verify Course List Btn");
		WebElement element = webDriver.waitForElement("settingsMenu__courseList", ByTypes.id, smallTimeOut, false, "Verify Course List Btn");
		return element;

	}
	
	public boolean isCourseListBtnEnabled() throws Exception {
		WebElement element = webDriver.waitForElement("//li[contains(@class, 'settingsMenu__courseList')]", ByTypes.xpath, 5, false);
		if (element == null)
			return false;
		else
			return true;
	}
	
	public List<WebElement> getCourseListCourseELements()  {
		
		WebElement element = null;
		
		for (int i=0;i<=10 && element==null;i++){
			try {
				element = webDriver.waitForElement("//*[@id='mCSB_2_container']/ul/li[1]", ByTypes.xpath, false, 1);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		List<WebElement> courseList = null;
		try {
			courseList = webDriver.getElementsByXpath("//div[contains(@class, 'mCSB_container')]/ul/li/div/a");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return courseList;

	}
	
	public String getCourseListSelectedCourse() throws Exception {
		return webDriver.waitForElement("//*[@id='mCSB_2_container']/ul/li/div/a[contains(@class, 'lightMenuTop_link--selected')]",ByTypes.xpath).getText();
	}
	
	public void clickOnDictionary() throws Exception {
		getDictionaryBtnElement().click();

	}
	
	public WebElement getDictionaryBtnElement() throws Exception {
		WebElement element = webDriver.waitForElement("//li[contains(@class, 'settingsMenu__dictionary')]", ByTypes.xpath, webDriver.getTimeout(), true, "Verify Dictionary Btn");
		return element;

	}
	public WebElement getDictionaryBtnElementWhenDisabled() throws Exception {
		WebElement element = webDriver.waitForElement("//li[contains(@class, 'settingsMenu__dictionary--disabled')]", ByTypes.xpath, webDriver.getTimeout(), true, "Verify Dictionary Btn Disabled");
		return element;

	}
	
	public void inputDictionaryTextandPressSearch(String input) throws Exception {
		webDriver.waitForElement("DictionaryInput", ByTypes.id).sendKeys(input);
		webDriver.waitForElement("//span[contains(@class,'dictionarySearch')]", ByTypes.xpath).click();
	
	}
	
	public void verifyDictionaryResultByInputText(String input) throws Exception {
		String actualResult = webDriver.waitForElement("//a[contains(text(),'"+input+"')]", ByTypes.xpath).getText();
		testResultService.assertEquals(input, actualResult);
	
	}
			
	public NewUxCommunityPage clickOnCommunityButton() throws Exception {
		try {
			WebElement communityElement = webDriver.waitForElement("//li[@id = 'sitemenu__itemCommunity']/a", ByTypes.xpath, true, webDriver.getTimeout());
			communityElement.click();
			
		} catch (Exception e) {
			testResultService.addFailTest("Community button element not found");
		}
		
		return new NewUxCommunityPage (webDriver, testResultService);
	}
	
	public void VerifyCommunityButtonIsEnabled(Boolean status) throws Exception {
		testResultService.assertEquals(status, webDriver.waitForElement("sitemenu__itemCommunity", ByTypes.id, false, webDriver.getTimeout()).isEnabled(), 
				"The Community button was " + status + ", while it should be otherwise.");
	}

	public NewUxMagazineArticlePage StartReadingMagazinePromo() throws Exception {
		if (!webDriver.getBrowserName().equals("chrome")) {
			webDriver.scrollToBottomOfPage();
		}
		
		webDriver.waitForElement("//div[contains(@class,'home__marketingMagBtn')]/a", ByTypes.xpath, webDriver.getTimeout(), true, "The 'Read' button of the Magazine Promotion was found").click();
		
		return new NewUxMagazineArticlePage(webDriver, testResultService);
	
	}
	
	
	public String getCurrentMagazineArticleTitle() throws Exception {
		WebElement element = webDriver.waitForElement("//section[contains(@class,'home__marketingW')]//div[contains(@class,'active')]//div[contains(@class,'home__marketingMagTitle')]", ByTypes.xpath, webDriver.getTimeout(), true, "The title of magazine on Home was not found");
		String title = element.getText();
		
		return title;
	}
	
	public void selectPromoSlideByNumber(int number) throws Exception {
		
		if (number == -1){
			WebElement element = webDriver.waitForElement("//*[@id='marketingArea']/section/div/div/ol", ByTypes.xpath,false,2);
			
			List<WebElement> elements = element.findElements(By.tagName("li"));
			number = elements.size();	
		}
		webDriver.waitForElement("//section[@id='marketingArea']//li["+number+"]", ByTypes.xpath, smallTimeOut, true, "Click on promo slide circle").click();
	}
	
	public int getPromoSlideCount() throws Exception {
		
		WebElement element = webDriver.waitForElement("//*[@id='marketingArea']/section/div/div/ol", ByTypes.xpath,false,2);
		List<WebElement> elements = element.findElements(By.tagName("li"));
		int count = elements.size();
		
		return count;	
	}

	
	public int verifyMagazineSlideTemplate(String expectedDate, String expectedTypeTitle, String expectedTitle, String expectedText, String expectedBtnText) throws Exception {
		
		String actualTypeTitle;
		int i;
		int count = getPromoSlideCount();
		
		String promoAreaXpath = "//section[@id='marketingArea']";
		String activeSlideXpath = "//div[contains(@class,'BSslide item')][contains(@class,'active')]";
		
		for (i=1;i<=count;i++){
			selectPromoSlideByNumber(i);
			Thread.sleep(500);
			actualTypeTitle = webDriver.waitForElement(promoAreaXpath + activeSlideXpath + "//div[contains(@class,'home__marketingMagTypeTitle')]", ByTypes.xpath).getAttribute("innerText");
			
			if (actualTypeTitle.equalsIgnoreCase(expectedTypeTitle))
				break;
		}
		
		String actualDate = webDriver.waitForElement(promoAreaXpath + activeSlideXpath + "//div[contains(@class,'home__marketingMagMonth')]", ByTypes.xpath).getAttribute("innerText");
		actualTypeTitle = webDriver.waitForElement(promoAreaXpath + activeSlideXpath + "//div[contains(@class,'home__marketingMagTypeTitle')]", ByTypes.xpath).getAttribute("innerText");
		String actualTitle = webDriver.waitForElement(promoAreaXpath + activeSlideXpath + "//div[contains(@class,'home__marketingMagTitle')]", ByTypes.xpath).getAttribute("innerText");
		String actualText = webDriver.waitForElement(promoAreaXpath + activeSlideXpath + "//div[contains(@class,'home__marketingMagText')]",  ByTypes.xpath).getAttribute("innerText");
		String actualButton = getCurrentPromoSlideButton().getAttribute("innerText");
		
		
		testResultService.assertEquals(expectedDate, actualDate, "Magazine slide date is not valid");
		testResultService.assertEquals(expectedTypeTitle, actualTypeTitle, "Magazine slide subtitle is not valid");
		testResultService.assertEquals(expectedTitle, actualTitle, "Magazine slide title is not valid");
		testResultService.assertEquals(expectedText, actualText, "Magazine slide text is not valid");
		testResultService.assertEquals(expectedBtnText, actualButton, "Magazine slide btn text is not valid");
		
		return i;
	}
	
	public WebElement getCurrentPromoSlideButton() throws Exception {
		
		String promoAreaXpath = "//section[@id='marketingArea']";
		String activeSlideXpath = "//div[contains(@class,'BSslide item')][contains(@class,'active')]";
		String btnXpath = "//div[contains(@class,'home__marketingMagBtn')]/a";
		
		return webDriver.waitForElement(promoAreaXpath + activeSlideXpath + btnXpath , ByTypes.xpath);
			
				
	}
	
	public void clickCurrentPromoSlideButton() throws Exception {
		
		String promoAreaXpath = "//section[@id='marketingArea']";
		String activeSlideXpath = "//div[contains(@class,'BSslide item')][contains(@class,'active')]";
		String btnXpath = "//div[contains(@class,'home__marketingMagBtn')]/a";
	
		webDriver.waitForElement(promoAreaXpath + activeSlideXpath + btnXpath , ByTypes.xpath).click();
	}

	public void verifyPromotionSlideTemplate(String expectedTitle, String expectedTypeTitle, String expectedText, String expectedBtnText) throws Exception {
		
		String promoAreaXpath = "//section[@id='marketingArea']";
		String activeSlideXpath = "//div[contains(@class,'BSslide item')][contains(@class,'active')]";
		
		String actualTitle = webDriver.waitForElement(promoAreaXpath + activeSlideXpath + "//div[contains(@class,'home__marketingMagTitle')]", ByTypes.xpath).getAttribute("innerText");
		String actualTypeTitle = webDriver.waitForElement(promoAreaXpath + activeSlideXpath + "//div[contains(@class,'home__marketingMagTypeTitle')]", ByTypes.xpath).getAttribute("innerText");
		String actualText = webDriver.waitForElement(promoAreaXpath + activeSlideXpath + "//div[contains(@class,'home__marketingMagText')]",  ByTypes.xpath).getAttribute("innerText");
		String actualButton = getCurrentPromoSlideButton().getAttribute("innerText");		
		
		selectPromoSlideByNumber(-1);
		
		testResultService.assertEquals(expectedTitle, actualTitle, "Promo slide title is not valid");
		testResultService.assertEquals(expectedTypeTitle, actualTypeTitle, "Promo slide subtitle is not valid");
		testResultService.assertEquals(expectedText, actualText, "Promo slide text is not valid");
		testResultService.assertEquals(expectedBtnText, actualButton, "Promo slide btn text is not valid");
	}
	
	public NewUxAssessmentsPage clickOnAssessmentsButton() throws Exception {
		try {
			WebElement element = webDriver.waitForElement("//li[@id = 'sitemenu__itemTests']/a", ByTypes.xpath, true, webDriver.getTimeout());
			element.click();
			
		} catch (Exception e) {
			testResultService.addFailTest("Home button element not found");
		}
		
		return new NewUxAssessmentsPage (webDriver, testResultService);
		
	}
	
	public NewUxInstitutionPage clickOnInstitutionButton() throws Exception {
		try {
			WebElement element = webDriver.waitForElement("//li[@id = 'sitemenu__itemInstPage']/a", ByTypes.xpath, true, webDriver.getTimeout());
			element.click();
			switchToInstPage();
			
		} catch (Exception e) {
			testResultService.addFailTest("Institution Page button element not found");
		}
		
		return new NewUxInstitutionPage (webDriver, testResultService);
		
	}
	
	public void verifyLogoutLinkIsDisabled () throws Exception {
		//WebElement element = webDriver.waitForElement("//ul[contains(@class,'settingsMenu__listItemW_personal')]//li[3][contains(@class,'disabled')]/a", ByTypes.xpath, webDriver.getTimeout(),false);
		WebElement element = webDriver.waitForElement("settingsMenu__listItem_logout", ByTypes.id, smallTimeOut,false);
		if (!element.getAttribute("class").contains("disabled")) {
			testResultService.addFailTest("Logout link enabled when it should not", false, true);
		}	
	}
	
	public void navigateToRequiredCourseByList(String courseName)  {
		//int indexTarget = ArrayUtils.indexOf(coursesNamesUMM, courseName);
		int indexTarget = ArrayUtils.indexOf(courses, courseName);
		try {
			clickOnCourseList();
			Thread.sleep(2000);	
			
			List<WebElement> courseList = getCourseListCourseELements();
			courseList.get(indexTarget).click();
			Thread.sleep(2000);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	public void navigateToRequiredCourseOnHomePage(String courseName) throws Exception {
			
		
			String actualCourse = getCurrentCourseName();
			int indexTarget = ArrayUtils.indexOf(coursesNames, courseName);
			
			if (indexTarget == -1) {
				indexTarget = ArrayUtils.indexOf(coursesNamesTOEIC, courseName);
			}
			
			if (isCourseListBtnEnabled() && indexTarget >=0 && !actualCourse.equals(courseName)) {
				clickOnCourseList();
				Thread.sleep(2000);
				//sometimes the list is closed - should be checked
				checkCourseListandOpenifClose();
				
				List<WebElement> courseList = getCourseListCourseELements();
				courseList.get(indexTarget).click();
				checkCourseListandCloseifOpen();
				
				actualCourse = getCurrentCourseName();
				if (!actualCourse.equals(courseName)) { // For cases when we change the course order, such as course sequence test
					clickOnCourseList();
					courseList = getCourseListCourseELements();
					for (int i = 0 ; i < 30 ; i++) {
						if(courseList.get(i).getAttribute("title").equals(courseName)) {
							courseList.get(i).click();
							getCurrentCourseName();
							break;
						}
					}
				}
			}
			else {
				
				
				// decide in which direction to navigate in course carousel
				
				int indexCurrent = ArrayUtils.indexOf(coursesNames, actualCourse);
				int size = coursesNames.length;
				
				boolean goNext = true;
				if (indexCurrent > indexTarget && Math.abs(indexCurrent - indexTarget) < size/2) goNext = false;	
				else if (indexCurrent < indexTarget && Math.abs(indexCurrent - indexTarget) > size/2) goNext = false;
				
				// find & navigate to required course
				courseName = courseName.toString().replace(" ", "").trim();
				
				int count = 0;
				while (count < 50 && !(actualCourse.toString().replace(" ", "").trim().equals(courseName))) {
				
					if (goNext)
						carouselNavigateNext();
					else
						carouselNavigateBack();
					
					Thread.sleep(500);
					actualCourse = getCurrentCourseName();
					
					count++;
				}
			}
	}



	public void scrollListLessons(String courseName){
		Actions action = new Actions(webDriver.getWebDriver());
		WebElement list = webDriver.getWebDriver().findElement(By.cssSelector("a[title='"+courseName+"']"));
		action.moveToElement(list).perform();
	}

	public void clickCourseNameFromList(String courseName){
		webDriver.getWebDriver().findElement(By.cssSelector("a[title='"+courseName+"']")).click();

	}


//	public void navigateToCustomCourse(String courseName) throws Exception {
//
//		//String actualCourse = getCurrentCourseName();
//		int indexTarget = ArrayUtils.indexOf(BasicNewUxTest.customCourseNames, courseName);
//		List<WebElement> courseList = getCourseListCourseELements();
//		courseList.get(indexTarget).click();
//
//	}



//		WebElement courseDropdown = webDriver.waitForElement("settingsMenu__courseList", ByTypes.id, smallTimeOut, true, "Verify Course Dropdown");
//
//		WebElement targetCourse = null;
//		for (WebElement course : courseDropdown.findElements(By.tagName("li"))) {
//			if (course.getText().equals(courseName)) {
//				targetCourse = course;
//				break;












//		if (isCourseListBtnEnabled() && indexTarget >=0 && !actualCourse.equals(courseName)) {
//			clickOnCourseList();
//			Thread.sleep(2000);
//			//sometimes the list is closed - should be checked
//			checkCourseListandOpenifClose();
//
//			List<WebElement> courseList = getCourseListCourseELements();
//			courseList.get(indexTarget).click();
//			checkCourseListandCloseifOpen();
//
//			actualCourse = getCurrentCourseName();
//			if (!actualCourse.equals(courseName)) { // For cases when we change the course order, such as course sequence test
//				clickOnCourseList();
//				courseList = getCourseListCourseELements();
//				for (int i = 0 ; i < 30 ; i++) {
//					if(courseList.get(i).getAttribute("title").equals(courseName)) {
//						courseList.get(i).click();
//						getCurrentCourseName();
//						break;
//					}
//				}
//			}
//		}

//		WebElement courseListButton = getCourseListBtnElement();
//		courseListButton.click();
//
//		//clickOnCourseList();
//		checkCourseListandOpenifClose();
//
//
//
//		WebElement courseDropdown = webDriver.waitForElement("settingsMenu__courseList", ByTypes.id, smallTimeOut, true, "Verify Course Dropdown");
//
//		WebElement targetCourse = null;
//		for (WebElement course : courseDropdown.findElements(By.tagName("li"))) {
//			if (course.getText().equals(courseName)) {
//				targetCourse = course;
//				break;
//			}
//		}
//
//		if (targetCourse != null) {
//			targetCourse.click();
//		} else {
//			throw new Exception("Course not found in the dropdown.");
//		}

//		if (isCourseListBtnEnabled() && indexTarget >=0 && !actualCourse.equals(courseName)) {
//			clickOnCourseList();
//			Thread.sleep(2000);
//			//sometimes the list is closed - should be checked
//			checkCourseListandOpenifClose();

//			List<WebElement> courseList = getCourseListCourseELements();
//			courseList.get(indexTarget).click();
//			checkCourseListandCloseifOpen();

//			actualCourse = getCurrentCourseName();
//			if (!actualCourse.equals(courseName)) { // For cases when we change the course order, such as course sequence test
//				clickOnCourseList();
//				courseList = getCourseListCourseELements();
//				for (int i = 0 ; i < 30 ; i++) {
//					if(courseList.get(i).getAttribute("title").equals(courseName)) {
//						courseList.get(i).click();
//						getCurrentCourseName();
//						break;
//					}
//				}
//			}





		//checkCourseListandCloseifOpen();
	//}
			
	public void checkCourseListandCloseifOpen() throws Exception {
		
		WebElement element = webDriver.waitForElement("//*[@id='mCSB_2_container']/ul/li[1]", ByTypes.xpath, false, 2);
		if (element!=null)
			clickOnCourseList();
	}
	
	
private void checkCourseListandOpenifClose() throws Exception {
		
		WebElement element = webDriver.waitForElement("//*[@id='mCSB_2_container']/ul/li[1]", ByTypes.xpath, false, 2);
		if (element==null)
			clickOnCourseList();
	}

	public void hoverOnNavBarItemAndVerivyTooltip (String itemId) throws Exception {
		
		String baseItemXpath = UL_ID_SITEMENU_ITEM_HOME + "//li[@id='" + itemId + "']" ;
		//WebElement element = webDriver.waitForElement(baseItemXpath, ByTypes.xpath, smallTimeOut, false);
		WebElement element = webDriver.waitUntilElementAppearsAndReturnElement(baseItemXpath, 10);
		webDriver.waitForElement(baseItemXpath+"//div[2][not contains(@class,'visible')]", ByTypes.xpath, smallTimeOut, false);
		webDriver.hoverOnElement(element);
		webDriver.waitForElement(baseItemXpath+"//div[2][contains(@class,'visible')]", ByTypes.xpath, smallTimeOut, false);
		
		switch (itemId)  {
		
		case "sitemenu__itemHome":
			testResultService.assertEquals("Home", element.getText(), "Checking Nav Bar item tooltip text");
			break;
		case "sitemenu__itemInstPage":
			testResultService.assertEquals("Institution Page", element.getText(), "Checking Nav Bar item tooltip text");
			break;
		case "sitemenu__itemCommunity":
			testResultService.assertEquals("Community", element.getText(), "Checking Nav Bar item tooltip text");
			break;
		case "sitemenu__itemMyAssignments":
			testResultService.assertEquals("Assignments", element.getText(), "Checking Nav Bar item tooltip text");
			break;
		case "sitemenu__itemTests":
			element = element.findElement(By.className("sitemenu__itemTooltip"));
			testResultService.assertEquals("Assessment Center", element.getText(), "Checking Nav Bar item tooltip text");
			break;
		case "sitemenu__itemMessages":
			testResultService.assertEquals("Inbox", element.getText(), "Checking Nav Bar item tooltip text");
			break;
		case "sitemenu__itemGrammarBook":
			testResultService.assertEquals("Grammar Book", element.getText(), "Checking Nav Bar item tooltip text");
			break;
					
		}
		
	}
	
	public void closeModalPopUp() throws Exception {
		
		WebElement modal = webDriver.waitForElement("modal-close", ByTypes.className, smallTimeOut, false);
		if (modal != null)
			modal.click();
				
	}
	
	public String openUnitObjectiveById(int Id) throws Exception {
		
		String name = clickOnUnitLessons(Id); // should return the name of the unit- then return this name at the end of the function
		WebElement unitObjectiveIcon = webDriver.getElement(By.xpath("//div[contains(@class,'unitInfo')]"));
		webDriver.hoverOnElement(unitObjectiveIcon);
		Thread.sleep(1500);
		WebElement unitObjectiveText = webDriver.getElement(By.xpath("//div[contains(@class,'unitInfoText')]"));
		testResultService.assertEquals("Unit Objectives",unitObjectiveText.getText());
		unitObjectiveIcon.click();
		return name;
		//return new NewUxUnitObjectivesPage(webDriver, testResultService);
	}
	
	public void openUnitTestForCourse(int Id, String courseName) throws Exception {
		
		navigateToRequiredCourseOnHomePage(courseName);
		clickOnUnitLessons(Id);
		Thread.sleep(1000);
		scrollToBottomOfLessonsElement(Id-1);
		webDriver.waitForElement("//a[contains(@class,'home__courseListUnitTest_openTest')]",ByTypes.xpath).click();
		Thread.sleep(2000);
		webDriver.switchToNewWindow();
	}

	public void startUnitTestForCourse(int Id, String courseName) throws Exception {

		//navigateToRequiredCourseOnHomePage(courseName);
		clickOnUnitLessons(Id);
		Thread.sleep(3000);
		scrollToBottomOfLessonsElement(Id-1);
		Thread.sleep(3000);
		webDriver.waitForElement("//a[contains(@class,'home__courseListUnitTest_openTest')]",ByTypes.xpath).click();
		Thread.sleep(3000);
		webDriver.switchToNewWindow();
	}
	
	public NewUxMyProgressPage clickOnMyProgress() throws Exception {
		
		WebElement element = null;
			for (int i=0;i<=20 && element==null;i++){
			element = webDriver.waitForElement("My Progress", ByTypes.linkText, 1,false, "My Progress button not found");	
		}
		element.click();
		
		return new NewUxMyProgressPage(webDriver, testResultService);
	}
	
	public WebElement getTestButtonAlertElement() throws Exception {		
		
		WebElement element = webDriver.waitForElement("//*[@id='sitemenu__itemTests']/a/div[1]/span",ByTypes.xpath,false,1);	
		return element;
	}
	
	public String getAvailableTestsNumberInAlert() throws Exception {
		//WebElement element = webDriver.waitForElement("//*[@id='sitemenu__itemTests']/a/div[1]/span",ByTypes.xpath,webDriver.getTimeout(),false);
		//Thread.sleep(4000);
		WebElement testAlert=null;
		String testCount="0";

		for (int i=0;i<=5 && testCount.equalsIgnoreCase("0") ;i++){
			testAlert= getTestButtonAlertElement();
			
			try{
				testCount = testAlert.getText();
			}
			catch (Exception e) {
				//testCount="0";
			}
		}
	
		if (testCount.equalsIgnoreCase("0")){
			//testResultService.addFailTest("Alert not displayed though it should be", false, true);
		}else{
			testCount = testAlert.getText();
		}
		return testCount;
	}
	
	
	public void getHumburgerAlert(boolean expected) throws Exception {
		boolean actual=false;
		
		WebElement humburgerAlert = webDriver.waitForElement("G11", ByTypes.id,1,false);
		if (humburgerAlert!=null)
			actual = humburgerAlert.isDisplayed();		
		
		testResultService.assertEquals(expected, actual, "The Humburager alerts hould be: " + expected + " but Actual is: " + actual);
	}
	
	public void checkLessonsCompletedInLessonList(int startOnLesson, int stopOnLesson) throws Exception {
		boolean completed = false;
		for (int i = (startOnLesson-1); i < stopOnLesson; i++) {
			completed = getLessonProgressElements().get(i).getAttribute("title").equalsIgnoreCase("Completed");
			testResultService.assertEquals(true, completed, "Lesson "+ (i+1) + " indication is not COMPLETED");
		}
	}
	
	public void checkProgressIndicationInLessonList(int lessonNumber, int progressPercent) throws Exception {
		
		List<WebElement> indications = getLessonProgressElements();
		WebElement element = null; 
		//boolean isStateCorrect = false;
		
		if (progressPercent == 0) {
			//isStateCorrect = indications.get(lessonNumber-1).getAttribute("className").contains("notStarted");
			element = indications.get(lessonNumber-1).findElement(By.className("empty"));
		} else if (1 <= progressPercent && progressPercent < 100) {
			//isStateCorrect = indications.get(lessonNumber-1).getAttribute("className").contains("inProgress");
			element=indications.get(lessonNumber-1).findElement(By.className("medium"));	
		} else if (progressPercent == 100) {
			//isStateCorrect = indications.get(lessonNumber-1).getAttribute("className").contains("done");
			element=indications.get(lessonNumber-1).findElement(By.className("full"));
		}
		
		testResultService.assertEquals(true, element.isDisplayed(), "Lesson Progress indication not valid");
		//testResultService.assertEquals(true, isStateCorrect, "Lesson Progress indication not valid");

	}
	
	public void checkThatLessonListHasNoProgressBars() throws Exception {
		if (getLessonProgressElements().size() > 0) {
			testResultService.addFailTest("Lesson list has progress bars though it should have not", false, true);
		}

	}
		
	private List<WebElement> getLessonProgressElements() throws Exception {
		//List<WebElement> progress = webDriver.getWebDriver().findElements(By.xpath("//*[contains(@class,'progressBar__progress')]"));
		//return progress;
		return webDriver.waitForElement("//ul[contains(@class,'courseListItemsW')]", ByTypes.xpath).findElements(By.xpath("//span[contains(@class,'courseListItemProgress')]"));
	}
	
	public NewUxLearningArea2 navigateToTask(String courseCode, int unitNum, int lessonNum, int stepNum, int taskNum) throws Exception { 
		
		NewUxLearningArea2 learningArea2;
		learningArea2 = navigateToCourseUnitLessonLA2(courseCodes, courseCode, unitNum, lessonNum);
		learningArea2.waitToLearningAreaLoaded();
		
		if (stepNum > 1) {
			learningArea2.clickOnStep(stepNum);
		} else if (learningArea2.isTaskCounterHasIntroMode())learningArea2.clickOnNextButton();
		

		if (taskNum > 1) {
			learningArea2.clickOnTaskByNumber(taskNum);
		}
			
		return learningArea2;	
	}
	
	//for CABA
	
public NewUxLearningArea2 navigateToCABATask(String courseCode, int unitNum, int lessonNum, int stepNum, int taskNum) throws Exception { 
		
		NewUxLearningArea2 learningArea2;
		learningArea2 = navigateToCourseUnitLessonLA2CABA(courseCodes, courseCode, unitNum, lessonNum);
		
		Thread.sleep(1000);
		
		if (stepNum > 1) {
			learningArea2.clickOnStep(stepNum);
		} else if (learningArea2.isTaskCounterHasIntroMode())learningArea2.clickOnNextButton();
		
		Thread.sleep(1000);
		
		if (taskNum > 1) {
			learningArea2.clickOnTaskByNumber(taskNum);
		}
		
		Thread.sleep(1000);
		
		return learningArea2;
		
	}

//for TOEIC

	public NewUxLearningArea2 navigateToTOEICTask(String courseCode, int unitNum, int lessonNum, int stepNum, int taskNum) throws Exception { 
		
		NewUxLearningArea2 learningArea2;
		learningArea2 = navigateToCourseUnitLessonLA2TOEIC(courseCodesTOEIC, courseCode, unitNum, lessonNum);
		
		Thread.sleep(1000);
		
		if (stepNum > 1) {
			learningArea2.clickOnStep(stepNum);
		} else if (learningArea2.isTaskCounterHasIntroMode())learningArea2.clickOnNextButton();
		
		Thread.sleep(1000);
		
		if (taskNum > 1) {
			learningArea2.clickOnTaskByNumber(taskNum);
		}
		
		Thread.sleep(1000);
		
		return learningArea2;
	
}

	public void verifyEdusoftSiteOpenNewTab() throws Exception {
		
		webDriver.switchToNextTab(); 
		webDriver.waitForElement("/html/body", ByTypes.xpath, false, webDriver.getTimeout()); 
		
		String currentUrl = webDriver.waitForSpecificCurrentUrl("", "edusoft");
		
		String fullUrl[] = currentUrl.split("/");
		String Corpurl[] = CorpUrl.split("/"); 
		webDriver.closeNewTab(2);
		webDriver.switchToMainWindow();
		testResultService.assertEquals(Corpurl[2], fullUrl[2],	"Edusoft site is not displayed");
		
	}

	public void clickMagazineEnrichmentButton() {
		try {
			WebElement magazineEnrichmentButton = webDriver.waitForElement("//*[contains(@id,\"home__courseListAdditionalContent\")]/div", ByTypes.xpath, true, smallTimeOut);
			
			if (webDriver.isDisplayed(magazineEnrichmentButton)) {
				webDriver.ClickElement(magazineEnrichmentButton);
			} else {
				testResultService.addFailTest("Magazine Enrichment button is not displayed");
			}
		} catch (Exception e) {
			testResultService.addFailTest("FAIL: Click on Magazine Enrichment button");
		}
	}
	
	public ArrayList<WebElement> getMagazinesList() {
		ArrayList<WebElement> magazinesList = (ArrayList<WebElement>) webDriver.getWebDriver().findElements(By.xpath("//*[contains(@id,\"home__additionlContent\")]/a"));
		return magazinesList;	
	}
	
	public String clickRandomMagazine() {
		ArrayList<WebElement> magazinesNames = getMagazinesList();
		int randomIndex = (int) (Math.random() * magazinesNames.size());
		String magazinName = magazinesNames.get(randomIndex).getText();
		magazinesNames.get(randomIndex).click();
		return magazinName;
	}
	
	public void checkMagazinesListIsClosed() throws Exception {
		WebElement element = webDriver.waitForElement("//*[contains(@id,'home__courseListAdditionalContent')]",ByTypes.xpath);
		boolean isClosed = element.getAttribute("className").contains("closed");
		testResultService.assertEquals(true, isClosed,"Magazine list is not closed");	
	}
	
	public void checkMagazinesListIsOpened() throws Exception {
		WebElement element = webDriver.waitForElement("//*[contains(@id,'home__courseListAdditionalContent')]",ByTypes.xpath);
		boolean isOpened = element.getAttribute("className").contains("opened");
		testResultService.assertEquals(true, isOpened,"Magazine list is not opened");	
	}
	
	public void closeAllNotifications () throws Exception {
		
		WebElement mainElement= null;

		try {
			for(int i = 0;i<5&&mainElement==null;i++) {
			  mainElement = webDriver.waitForElement("home__marketingMagMain", ByTypes.className, false, 1);

			  if(mainElement!=null) {
				  WebElement closeNotification = webDriver.waitForElement("notificationsCenter_hideSlide", ByTypes.className, false, 5);
				  
				  while (closeNotification != null && mainElement!=null){
					closeNotification.click();
					closeNotification = webDriver.waitForElement("notificationsCenter_hideSlide", ByTypes.className, false, 1);
				  }
			  	}
			}
		}catch (Exception e) {
			//e.printStackTrace();
		}
	}

	public void waitUntilLoadingMessageIsOver() throws Exception {
		WebElement message = webDriver.waitForElement("//div[@class='waitForLogin__textW ng-binding']", ByTypes.xpath, smallTimeOut, false);

		boolean isDisplayed = true;
		while (isDisplayed) {
			if (message != null ) {
				isDisplayed = true;
			} else {
				isDisplayed = false;
			}
			message = webDriver.waitForElement("//div[@class='waitForLogin__textW ng-binding']", ByTypes.xpath, smallTimeOut, false);
		}	
	}
	
	public void validateInboxCounter(int expectedCount) throws Exception {
		//Thread.sleep(7000);
		//WebElement counter = webDriver.waitForElement("//li[contains(@class,'itemMessages')]//div[@class='sitemenu__notification ng-scope']", ByTypes.xpath, smallTimeOut, false);
		
		WebElement counter= webDriver.waitUntilElementAppearsAndReturnElement("//li[contains(@class,'itemMessages')]//div[@class='sitemenu__notification ng-scope']", 5);

		if (expectedCount == 0 && counter!=null){
			for (int i=0; counter!=null && i<=20;i++){
				//Thread.sleep(1);
				counter = webDriver.waitForElement("//li[contains(@class,'itemMessages')]//div[@class='sitemenu__notification ng-scope']", ByTypes.xpath, 1, false);
			}
		}

		if (expectedCount == 0) {
			testResultService.assertEquals(true, counter == null,"Counter is shown when it sould be set to 0.", true);
		} else {
			if (counter == null) {
				testResultService.assertEquals(expectedCount, 0, "Count is Not Displayed Correctly.", true);
			} else {
				testResultService.assertEquals(Integer.toString(expectedCount), counter.getText(), "Count is Not Displayed Correctly.", true);
			}
		}
	}
	
	public void openUnitAndScrollDown(String courseName, int unitID) throws Exception {
		navigateToRequiredCourseOnHomePage(courseName);
		clickOnUnitLessons(unitID);
		Thread.sleep(1000);
		scrollToBottomOfLessonsElement(unitID-1);
	}
	
	public boolean clickViewResultButtonInsideUnit() throws Exception {
		boolean clicked = false;
		WebElement viewResultsButton = webDriver.waitForElement("//a[contains(@class,'home__courseListUnitTest_viewResults')]", ByTypes.xpath, false, 3);
		if (viewResultsButton != null) {
			viewResultsButton.click();
			clicked = true;
		}
		return clicked;
	}
	
	public void checkTestNameInMyProgressPage(String expectedName) throws Exception {
		webDriver.waitUntilElementAppears("//div[contains(@class,'myProgress__statsToeicTestName')]", 15);
		WebElement testName = webDriver.waitForElement("//div[contains(@class,'myProgress__statsToeicTestName')]", ByTypes.xpath, true, 3);
		testResultService.assertEquals(testName.getText(), expectedName,"Test Name is Not Displayed Correctly in My Progress Page");
	}
	
	public boolean checkViewFullReportDisplayedInMyProgressPage() throws Exception {
		try {
			WebElement viewFullReportButton = webDriver.waitForElement("//div[contains(@class,'assessments__viewFullReport')]/a", ByTypes.xpath, false, 3);
			boolean isDisplayed = webDriver.isDisplayed(viewFullReportButton);
			testResultService.assertEquals(true, isDisplayed);
			return isDisplayed;
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean checkViewResultsDisplayedInMyProgressPage() throws Exception {
		try {
			WebElement viewResultsButtons = webDriver.waitForElement("//div[contains(@class,'assessments__viewResults')]/a", ByTypes.xpath, false, 3);
			boolean isDisplayed = webDriver.isDisplayed(viewResultsButtons);
			testResultService.assertEquals(true, isDisplayed);
			return isDisplayed;
		} catch (Exception e) {
			return false;
		}
	}
	
	public void clickViewFullReportInMyProgressPage() throws Exception {
		WebElement viewFullReportButton = webDriver.waitForElement("//div[contains(@class,'assessments__viewFullReport')]/a", ByTypes.xpath, false, 3);
		webDriver.ClickElement(viewFullReportButton);
	}
	
	// This function clicks the button from the left side of my progress page
	public void clickViewResultsInMyProgressPage() throws Exception {
		WebElement viewResultsButton = webDriver.waitForElement("//div[contains(@class,'assessments__viewResults')]/a", ByTypes.xpath, false, 3);
		webDriver.ClickElement(viewResultsButton);
	}
	
	// This function clicks the button inside a specific unit- once the unit is open
	public void clickViewResultsInMyProgressPageForSpecificUnit() throws Exception {
		webDriver.waitUntilElementAppears("//a[contains(@class,'home__courseListUnitTest_viewResults')]", 10);
		WebElement viewResultsButton = webDriver.waitForElement("//a[contains(@class,'home__courseListUnitTest_viewResults')]", ByTypes.xpath);
		webDriver.ClickElement(viewResultsButton);
	}
	
	public void clickTakeTestAgainInMyProgressPageForSpecificUnit() throws Exception {
		WebElement takeTestAgainButton = webDriver.waitForElement("//a[@class='home__courseListUnitTest_openTestAgain'][@title='Take again']", ByTypes.xpath, false, 5);
		webDriver.ClickElement(takeTestAgainButton);//[@title='Take again']
		Thread.sleep(2000);
	}
	
	public void clickStartTestInMyProgressPageForSpecificUnit() throws Exception {
		WebElement takeTestAgainButton = webDriver.waitForElement("//a[contains(@class,'home__courseListUnitTest_openTest ')]", ByTypes.xpath, false, 3);
		webDriver.ClickElement(takeTestAgainButton);
	}
	
	public int retrieveMessagesCount() throws Exception {
		WebElement counter = webDriver.waitForElement("//li[contains(@class,'itemMessages')]//div[@class='sitemenu__notification ng-scope']", ByTypes.xpath, smallTimeOut, false);
		if (counter == null) {
			return 0;
		} else {
			return Integer.parseInt(counter.getText());
		}
	}

	public void waitTillCarouselLoad() throws Exception {
		WebElement element = null;
		
		for (int i=0; i<=20 && element==null ;i++){
			element = webDriver.waitForElement("//div[contains(@class, 'carousel-control')]", ByTypes.xpath,
					1, false, "carousel home page not loaded");	
		}
	}
	
	public Integer getUnitCompletionFromProgressBar(int unitSequence) throws Exception {
		WebElement progressBarOfUnit = webDriver.waitForElement("//*[contains(@id,'home__unitIW_"+(unitSequence-1)+"')]//div[@class='progressBar__wrapper']", ByTypes.xpath, true, 5);
		String progressPercentage = progressBarOfUnit.getAttribute("title").split(": ")[1];
		progressPercentage = progressPercentage.replace(progressPercentage.substring(progressPercentage.length()-1), "");
		return Integer.parseInt(progressPercentage);
	}
	
	public void validateCourseIsLocked(String courseName) throws Exception {
	
		// Open the Courses List
		boolean courseListButton = isCourseListBtnEnabled();
		int numOfCoursesInLIst=0;
		
		if (courseListButton){
			clickOnCourseList();	
			Thread.sleep(1000);
			// Get the Number of Courses
			List<WebElement> courseList = getCourseListCourseELements();
			numOfCoursesInLIst = courseList.size();
			
			// Validate the Course has Lock Sign in Courses List
			boolean hasLockedSign = false;
			for (int i = 0; i < numOfCoursesInLIst; i++) {
				if (courseList.get(i).getText().equals(courseName)) {
					hasLockedSign = courseList.get(i).getAttribute("class").contains("--courseLocked");
					testResultService.assertEquals(true, hasLockedSign,"Course " + courseName +" Doesn't have Locked Sign in Courses List (Should Be Locked).");
					break;
				}
			}
			testResultService.assertEquals(true, hasLockedSign, "Course " +courseName+ " Doesn't Appear on Courses List (Should Appear Locked)");
	
			// Close Courses List
			clickOnCourseList(); 
			Thread.sleep(1000);
		}
		
		// Validate Course Doesn't Appear on Home Page
		boolean appearsOnHomePage = false;
		String actualCourse = getCurrentCourseName();
		courseName = courseName.toString().replace(" ", "").trim();
		int count = 0;
		while (count < numOfCoursesInLIst) {
			if (actualCourse.toString().replace(" ", "").trim().equals(courseName)) {
				appearsOnHomePage = true;
				break;
			} else {
				carouselNavigateNext();
				Thread.sleep(1500);
				actualCourse = getCurrentCourseName();
				count++;	
			}
			Thread.sleep(1000);
		}	
		testResultService.assertEquals(false, appearsOnHomePage, "Course " +courseName+ " Appears on Home Page Even Though it Should be Locked.");
	}
	
	public void validateCourseIsNotLocked(String courseName) throws Exception {
		
		//report.startStep("Open the Courses List");
		clickOnCourseList();
		 
		//report.startStep("Get the Number of Courses");
		List<WebElement> courseList = getCourseListCourseELements();
		int numOfCoursesInLIst = courseList.size();
		
		//report.startStep("Validate the Course Doesn't Have Lock Sign");
		boolean hasLockedSign = true;
		for (int i = 0; i < numOfCoursesInLIst; i++) {
			if (courseList.get(i).getText().equals(courseName)) {
				hasLockedSign = courseList.get(i).getAttribute("class").contains("--courseLocked");
				testResultService.assertEquals(false, hasLockedSign,"Course " + courseName +" has Locked Sign in Courses List (Should not Be Locked).");
				break;
			}
		}
		testResultService.assertEquals(false, hasLockedSign, "Course " +courseName+ " Doesn't Appear on Courses List (Should Appear not Locked)");

		//report.startStep("Close Courses List");
		clickOnCourseList(); 
		
		//report.startStep("Validate Course Appears on Home Page");
		boolean appearsOnHomePage = false;
		String actualCourse = getCurrentCourseName();
		courseName = courseName.toString().replace(" ", "").trim();
		int count = 0;
		while (count < numOfCoursesInLIst) {
			if (actualCourse.toString().replace(" ", "").trim().equals(courseName)) {
				appearsOnHomePage = true;
				break;
			} else {
				carouselNavigateNext();
				Thread.sleep(1500);
				actualCourse = getCurrentCourseName();
				count++;	
			}
		}	
		testResultService.assertEquals(true, appearsOnHomePage, "Course " +courseName+ " Doesn't Appear on Home Page Even Though it Should not Be Locked.");
	}
	
	public void checkTextAndclickOnNotificationsWithoutClosing() throws Exception {
		WebElement element = webDriver.waitForElement("settingsMenu__listItem_NotificationCenter", ByTypes.id,webDriver.getTimeout(),false);
		testResultService.assertEquals("Notifications", element.getText(), "Checking Notifications link text");
		element.click();
	}
	
	public void waitHomePageloaded() throws Exception {
		//webDriver.waitUntilElementAppears("//*[contains(@class,'home__userBar')]", 100);
		WebElement element=null;
		element = webDriver.waitUntilElementAppears("//*[contains(@class,'home__userBar')]", ByTypes.xpath, 60);
		/*	
		if (element==null){
			webDriver.refresh();
			Thread.sleep(4000);
			element = webDriver.waitUntilElementAppears("//*[contains(@class,'home__userBar')]", ByTypes.xpath, 10);
	*/	
			
			if (element==null){
				testResultService.addFailTest("Home page not loaded", false, true);
			}
	//	}
			
		/*WebElement element=null;
		try {
			for (int i=0;i<=60 && element==null;i++){
				element = webDriver.waitForElement("//*[contains(@class,'home__userBar')]", ByTypes.xpath, 1, false, "Home Page not loaded after: " + i + "seconds");
		}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	
	
	public void waitHomePageloadedFully() throws Exception {
		boolean homeLoaded = webDriver.waitUntilElementAppears("//*[contains(@class,'home__userBar')]", 10);
		waitUntilCourseAreaLoaded(7);
		WebElement element = getCurrentSlideInCourseArea();
		
		if (element ==null || !homeLoaded)
			try {
				testResultService.addFailTest("Home page or Course Area not loaded", true, true);
			
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	public void goToHomepageThroughMenu() throws Exception {
		webDriver.waitForElement("//nav[@id='navigation']//button", ByTypes.xpath,true, webDriver.getTimeout()).click();
		webDriver.waitForElement("//li[@id='sitemenu__itemHome']//a", ByTypes.xpath,true, webDriver.getTimeout()).click();
	}
	
	public void checkNoNotificationsExistForStudent() throws Exception {
		WebElement notificationPopUp = webDriver.waitForElement("home__marketingSlideOW_0", ByTypes.id, false, webDriver.getTimeout());
		if (notificationPopUp != null) {
			String text = notificationPopUp.getAttribute("textContent");
			testResultService.assertEquals(text, "There are no messages", "Some notification Are displayed.");
		}
	}
	
	public void checkTextAndclickOnOnlineSessionsWithoutClosing() throws Exception {
		WebElement element = webDriver.waitForElement("settingsMenu__listItem_onlineSessions", ByTypes.id,webDriver.getTimeout(),false);
		testResultService.assertEquals("Online Sessions", element.getText(), "Checking Online Sessions link text");
		element.click();
	}
	
	public void validateTaskTitleIsCorrect(String title) throws Exception {
		String actualTitle = webDriver.waitForElement("//div[@id='textContainer']//h1", ByTypes.xpath, false, webDriver.getTimeout()).getText();
		testResultService.assertEquals(title, actualTitle,"Title is not correct.");
	}
	
	public void validateTaskTitleContainsCorrect(String title) throws Exception {
		String actualTitle = webDriver.waitForElement("//div[@id='textContainer']//h1", ByTypes.xpath, false, webDriver.getTimeout()).getText();
		testResultService.assertEquals(true, actualTitle.contains(title),"Title is not correct.");
	}
	
	public void validateBodyIsNotNull() throws Exception {
		String actualBody = webDriver.waitForElement("//div[@id='textContainer']", ByTypes.xpath, false, webDriver.getTimeout()).getText();
		testResultService.assertEquals(true, actualBody!=null,"Body is null.");
	}
	
	public void waitHomePageloadedAndCloseModalPopup() {
		WebElement element=null;
		WebElement modal=null;
		try {
			for (int i=0;i<=20 && modal==null;i++){
				modal = webDriver.waitForElement("modal-close", ByTypes.className, smallTimeOut, false);
				if (modal != null) {
					modal.click();
					for (int j=0;j<=60 && element==null;j++){
						element = webDriver.waitForElement("//*[contains(@class,'home__userBar')]", ByTypes.xpath, 1, false, "Home Page not loaded after: " + i + "seconds");
						if (element != null) {
							break;
						}
					}
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean checkCourseListIsClosed() throws Exception {
		String property = getCourseListBtnElement().getAttribute("className");
		if (property.contains("closed")) {
			return true;
		} else {
			return false;
		}
	}
	
	public void waitForCourseListToClose() throws Exception {
		boolean isClosed = checkCourseListIsClosed();
		for (int i = 0; i < 15 && !isClosed; i++) {
			isClosed = checkCourseListIsClosed();
		}	
	}
	
	public void scrollDownToReportButtons() throws Exception{
		WebElement scroller = webDriver.waitForElement("mCSB_4_dragger_vertical", ByTypes.id);
		webDriver.dragScrollElement(scroller, scroller.getSize().height - scroller.getLocation().y);
	}
	
	public void scrollToTopOfMyProgressPage() throws Exception{
		WebElement firstUnitArrow = webDriver.getWebDriver().findElement(By.className("myProgress__unitArrow"));
		webDriver.scrollToElement(firstUnitArrow);
	}
	
	public String getRegUserAndLoginError() throws Exception{
		String errorString="";
		
		WebElement element = webDriver.waitUntilElementAppearsAndReturnElement("/html/body/pre", 5);
		
		if (element==null)
			element = webDriver.waitUntilElementAppearsAndReturnElement("/html/body/table/tbody/tr[1]/td/div", 5);
		
		errorString= element.getText();
		return errorString;
	}

	public boolean myProfileIsClickable() throws Exception {
		boolean res = webDriver.checkElementEnabled("//li[contains(@class,'settingsMenu__personal')]");
		
		//webDriver.waitForElement("//li[contains(@class,'settingsMenu__personal')]", ByTypes.xpath);
		return res;
	}

	public void clickOnRightArrow() throws Exception {
		
		webDriver.sendKey(Keys.ARROW_RIGHT);
	}

	public void refreshHomePage() {
		webDriver.getWebDriver().findElement(By.className("home__logoImage")).click();;
		
	}

	public String getUnitProgress(int unitSequence) throws Exception {
	/*	
		List<WebElement> units = webDriver.getElementsByXpath("//*[@class='textFitted']");

		WebElement unit = units.stream().filter(e->e.getText().trim().equalsIgnoreCase(unitName)).findAny().orElse(null);;
		WebElement progress = unit.findElement(By.xpath("//div[@class='progressBar__wrapper']"));
		String percents = progress.getAttribute("title");
		String [] arr = percents.split("[:%]");
		percents = arr[1].trim();
		*/
		Thread.sleep(1000);
		WebElement progress = null;
		String percents = "";
		List<WebElement> units = webDriver.getElementsByLocator(By.xpath("//div[starts-with(@title,'Unit Completion')]"));
		Thread.sleep(1000);
		webDriver.scrollToElement(units.get(0), 0);	
		Thread.sleep(1000);
		percents = units.get(0).getAttribute("title");
		String [] arr = percents.split("[:%]");
		percents = arr[1].trim();
		Thread.sleep(1000);
		
		/*webDriver.getElementsByXpath("//*[@class='home__unitIW']");		
		for(int i = 0;i<units.size();i++) {
			webDriver.scrollToElement(units.get(i), 0);
			String text = units.get(i).findElement(By.className("textFitted")).getText();
			if(text.equalsIgnoreCase(unitName)) {
				progress = units.get(i).findElement(By.xpath("//div[starts-with(@title,'Unit Completion')]"));               //div[@class='progressBar__wrapper']"));
				percents = progress.getAttribute("title");
				String [] arr = percents.split("[:%]");
				percents = arr[1].trim();
			break;
			}
		}*/
		
		
		return percents;
	}
	
	public void verifyAllCoursesAssignedAndOpenOnHomePage() {
		
		try {
			
			for (int i=0;i<coursesNames.length;i++){
				String currentCourseName = getCurrentCourseName();
				testResultService.assertEquals(currentCourseName, coursesNames[i],
						"Course on Home Page is not valid or not found.");
				
				carouselNavigateNext();
				Thread.sleep(1000);
			}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	public String getAverageTestScore() throws Exception {
		WebElement element = webDriver.waitForElement("home__studentWidgetTestScoresData", ByTypes.id);

		return element.getText();
		
	}

	public int getTimeOnTaskFromHomePage() {
		List<WebElement> time = webDriver.getWebDriver()
				.findElements(By.cssSelector(".home__studentWidgetHrMinHrTxt.ng-binding"));
		String minutes = time.get(1).getText().toString();
		Character min;
		if(minutes.startsWith("0")) {
			min = minutes.charAt(1);
			minutes = min.toString();
		}
		return Integer.parseInt(minutes);
	}

	public void waitTillInActivityPageOrTimeExpirationApears(int time) throws Exception {
		int steps = time/10;
		boolean islogOut = false;
		for(int i = 0; i<steps; i++) {
			String url = webDriver.getUrl();
			System.out.println(url);
			islogOut = url.endsWith("SessionTimeout");
			if(islogOut) {
				break;
			}
			Thread.sleep(10000);
		}
		if(!islogOut) {
			testResultService.addFailTest("Sliding expiration is broken!!!");
		}
	}	
	
	public void waitToFooterLoaded() throws Exception {
		
		WebElement element = webDriver.waitForElement("Contact Us", ByTypes.linkText, smallTimeOut, true, "Check Footer Links Not Displayed");
		
	}

	public void checkUnitObjectivePerEDExcellence() throws Exception {
		WebElement unitObjectiveIcon = webDriver.getElement(By.xpath("//div[contains(@class,'unitInfo')]"));
		webDriver.hoverOnElement(unitObjectiveIcon);
		Thread.sleep(1500);
		WebElement unitObjectiveText = webDriver.getElement(By.xpath("//div[contains(@class,'unitInfoText')]"));
		testResultService.assertEquals("Unit Objectives",unitObjectiveText.getText());
	}

	public void waitNotificationsAfterRestartPool() {
		WebElement mainElement = null;
		try {
			for(int i = 0;i<50&&mainElement==null;i++) {
			  mainElement = webDriver.waitForElement("home__marketingMagMain", ByTypes.className, false, 2);
			}
		}catch (Exception e) {
			
		}
	}

	public String getLicenseExpirationWidgetValue() throws Exception {
		
		WebElement license = webDriver.waitForElement("home__studentWidgetLicenceExpirationData", ByTypes.id);
		if(license!=null) {
			return license.getText();
		}else {
			testResultService.addFailTest("License Expiration widget won't apears", true, true);
		}
		return "";
		
	}

	public String getLicenseHourExpiration() throws Exception {
		
		WebElement licenseHour = 
				webDriver.waitForElement("//span[@class='home__studentWidgetDataLicExprHour ng-binding']", ByTypes.xpath);
		if(licenseHour!=null) {
			return licenseHour.getText();
		}else {
			testResultService.addFailTest("License Expiration widget won't apears", true, true);
		}
		return "";
		
	}

	public WebElement getStartCourseElement() throws  Exception{

		WebElement startButton = webDriver.waitUntilElementAppears(
				"//div[contains(@class,'BSslide')][contains(@class,'active')]//div[contains(@class,'carouselStartBtnW')]//a",
				ByTypes.xpath,10);

		return startButton;
	}

	public void enterToLAByMainButton() throws Exception {
		WebElement button = null;

		button = getCourseContinueButton();
		if (button != null)
			button.click();
		else {
			button = getStartCourseElement();

			if (button != null)
				button.click();
		}
	}

	public boolean findWantedPromotion(String expectedTitle) throws Exception {
		boolean found = false;
		found = doesPromotionDisplayed(expectedTitle);
		if (!found){
			Thread.sleep(60000);
			webDriver.refresh();
			waitHomePageloaded();
			webDriver.scrollToBottomOfPage();
			found = doesPromotionDisplayed(expectedTitle);
		}

		return found;
	}

	private boolean doesPromotionDisplayed(String expectedTitle) throws Exception {
		boolean found = false;
		String promoAreaXpath = "//section[@id='marketingArea']";
		String activeSlideXpath = "//div[contains(@class,'BSslide item')][contains(@class,'active')]";


		WebElement element = webDriver.waitForElement("//*[@id='marketingArea']/section/div/div/ol", ByTypes.xpath,false,2);
		List<WebElement> elements = element.findElements(By.tagName("li"));

		for (int i = elements.size()-1;i>=0;i--) {

			String actualTitle = webDriver.waitForElement(promoAreaXpath + activeSlideXpath + "//div[contains(@class,'home__marketingMagTitle')]", ByTypes.xpath).getAttribute("innerText");
			if (expectedTitle.equalsIgnoreCase(actualTitle)){
				found=true;
				//webDriver.waitForElement("//section[@id='marketingArea']//li["+i+"]", ByTypes.xpath, smallTimeOut, true, "Click on promo slide circle").click();
				return found;
			}
			elements.get(i).click();
			Thread.sleep(1000);
		}
		return found;
	}
}