package tests.edo.newux;

// import java.util.ArrayList;
// import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
// import org.openqa.selenium.safari.SafariDriver;

// import drivers.SafariWebDriver;
import pageObjects.edo.NewUXLoginPage;
import pageObjects.edo.NewUxCommunityPage;
import pageObjects.edo.NewUxHomePage;
import pageObjects.edo.NewUxLearningArea;
import pageObjects.edo.NewUxLearningArea2;
import pageObjects.edo.NewUxMyProgressPage;
// import pageObjects.edo.NewUxMessagesPage;
import testCategories.AngularLearningArea;
import testCategories.inProgressTests;
// import testCategories.reg1;
// import testCategories.reg2;
// import testCategories.edoNewUX.CourseArea;
import testCategories.edoNewUX.GeneralFeatures;
import Enums.ByTypes;
import Interfaces.TestCaseParams;
import net.sourceforge.htmlunit.corejs.javascript.regexp.SubString;

@Category(AngularLearningArea.class)
public class FooterTests2 extends BasicNewUxTest {

	NewUxLearningArea2 learningArea2;
	NewUxCommunityPage communityPage;
	NewUxMyProgressPage myProgress;
	
	private static final String companyName = "Edusoft";
	private static final String mailtoDefault = "info@edusoftlearning.com";
	private static final String imageFileName = "edusoftNew.svg";

	//Custom footer
	private static final String customCompanyNameShort = "QA";
	private static final String customMailto = "customus@123.com";
	private static final String customImageFileName = "logoFooter.png";
	private static final String customlogoURL = "http://microsoft.com";
	private static final String customAboutLink = "http://www.custom.org";
	
	@Before
	public void setup() throws Exception {
		super.setup();

	}

	
	@Test
	@Category(GeneralFeatures.class)
	@TestCaseParams(testCaseID = { "18451", "26832" }, testMultiple = true)
	public void testCustomFooterEdusoft() throws Exception {
		
		report.startStep("Close and reopen with custom Institution, local");
		
		institutionName = institutionsName[6];
		pageHelper.initializeData();
			
		closeBrowserAndOpenAgain(institutionName);
		homePage.waitToFooterLoaded();
		report.startStep("Verify Custom footer before login");
	
		report.startStep("Verify custom 'Contact Us' mailto");
			homePage.checkCustomContactUsLink(customMailto);
	
		report.startStep("Verify 'About Edusoft' link & label in Footer");
			homePage.checkCustomAboutLink(customAboutLink, customCompanyNameShort);
						
		report.startStep("Verify Custom footer Home page");
		report.startStep("Login as an exists student");
			homePage = getUserAndLoginLaurateClass(institutionId);
			sleep(1);
			homePage.closeAllNotifications();
			homePage.waitHomePageloadedFully();
			
			homePage.checkCustomContactUsLink(customMailto);
			homePage.checkCustomAboutLink(customAboutLink, customCompanyNameShort);
	}

	@Test
	@Category(GeneralFeatures.class)
	@TestCaseParams(testCaseID = { "26134", "18447", "18443", "18448", "18442" }, testMultiple = true)
	public void testFooterComunityPages() throws Exception {

		report.startStep("Login with a new student");
			homePage = getUserAndLoginNewUXClass();
		//	homePage.waitHomePageloaded();
		
		report.startStep("Navigate To Community Page and verify Footer");
			communityPage = homePage.openNewCommunityPage(false);
			sleep(1);
			runFourTestSteps();
			
		report.startStep("Navigate To Magazine Page and verify Footer");
			communityPage.openMagazinePage();
			runFourTestSteps();
	
			report.startStep("Navigate To Idioms Page and verify Footer");
			homePage.openNewCommunityPage(false);
			communityPage.openIdiomsPage();
			runFourTestSteps();
	
			report.startStep("Navigate To Games Page and verify Footer");
			homePage.openNewCommunityPage(false);
			communityPage.openGamesPage();
			runFourTestSteps();
	}

	private void runFourTestSteps() throws Exception {
		testFooterAboutLink();
		testFooterPrivacyStatement();
		testFooterLegalNoticePopup();
		testFooterLogo();
	}
	
	private void testFooterAboutLink() throws Exception {
		// webDriver.switchToNextTab();

		homePage.clickOnAboutEdusoft();
		homePage.verifyEdusoftSiteOpenNewTab();
		
	}

	private void testFooterPrivacyStatement() throws Exception {
		report.startStep("Check privacy statement");
		homePage.clickOnPrivacyStatement();
		String title = homePage.getOcModalTitleTextInFooter2();
		testResultService.assertEquals("Privacy Statement", title);

		closeModalWnd();
	}

	private void testFooterLegalNoticePopup() throws Exception {
		report.startStep("Legal Notice statement");
		homePage.clickOnLegalNotice();
		String title = homePage.getOcModalTitleTextInFooter2();
		testResultService.assertEquals("Legal Notices", title);

		closeModalWnd();
	}

	private void closeModalWnd() throws Exception {
		WebElement closeBtn = webDriver.waitForElement("//div[contains(@class,'footerModal')]", ByTypes.xpath).findElement(By.className("modal-close"));
		sleep(1);
 		webDriver.clickOnElementByJavaScript(closeBtn);
 		sleep(2);
	}
	
	private void testFooterLogo() throws Exception {

		report.startStep("Verifiy that logo is displayed");
		NewUXLoginPage loginPage = new NewUXLoginPage(webDriver, testResultService);
		sleep(2);
		testResultService.assertEquals(true, loginPage.getfFooterImageDisplayed(), "Footer image is not displayed");

		report.startStep("Mouse hover on logo");

		report.startStep("Click on logo");
		loginPage.getFooterLogoElement().click();
		homePage.verifyEdusoftSiteOpenNewTab();

	}

	private void testFooterEdLogo() throws Exception {

		report.startStep("Verifiy that ED logo is displayed");
		NewUxLearningArea2 learningArea2;
		learningArea2 = new NewUxLearningArea2(webDriver, testResultService);
		
		testResultService.assertEquals(true, learningArea2.verifyEdLogoDisplayedInFooter(),
				"Footer ED LOGO image is not displayed");
	}

	private void closeBrowserAndOpenAgain(String institutionName) throws Exception {
		
		String url = webDriver.getUrl();
		url = url.replace("com/automation", "com/"+ institutionName);
		
		webDriver.deleteCookiesAndCache();
		webDriver.quitBrowser();
		sleep(2);
		webDriver.init();
		sleep(2);
		webDriver.maximize();
		webDriver.openUrl(url);
		sleep(2);
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}


	@Test
		@Category(GeneralFeatures.class)
		@TestCaseParams(testCaseID = { "26134", "18447", "18443", "18448", "18442" }, testMultiple = true)
		public void testFooterAllPages() throws Exception {
	
			report.startStep("Verifiy Footer on Login Page");
			sleep(1);
			runFourTestSteps();
	
			report.startStep("Login with a new student");
				homePage = getUserAndLoginNewUXClass();
			//	homePage.waitHomePageloaded();
	
			report.startStep("Verifiy Footer on Home Page");
				runFourTestSteps();
				homePage.waitHomePageloaded();
				webDriver.scrollToTopOfPage();
				
			report.startStep("Verifiy Footer on My Progress Page");
				myProgress = homePage.clickOnMyProgress();
				myProgress.waitForPageToLoad();
				sleep(1);
				runFourTestSteps();
				sleep(2);
				
				myProgress.clickToOpenNavigationBar();
				homePage.clickOnHomeButton();
				myProgress.waitHomePageloadedFully();
				
			report.startStep("Navigate To Learning Area and Verify Footer in LA");	
				learningArea2 = homePage.clickOnContinueButton2();
				learningArea2.waitUntilLearningAreaLoaded();
				learningArea2.clickToOpenNavigationBar();
				sleep(1);
				learningArea2.openAboutUsPage(false);
	
			report.startStep("Verifiy Footer on Learning Area");
			runFourTestSteps();
			
			testFooterEdLogo();
	
			report.startStep("Close Footer and check links not displayed");
			learningArea2.closeAboutUsPage();
			sleep(1);
			
			learningArea2.verifyFooterLinksNotDisplayed();
		}
}
