package tests.edo.newux;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.safari.SafariDriver;

import drivers.SafariWebDriver;
import pageObjects.edo.NewUXLoginPage;
import pageObjects.edo.NewUxCommunityPage;
import pageObjects.edo.NewUxLearningArea;
import testCategories.NonAngularLearningArea;
import testCategories.reg1;
import testCategories.reg2;
import testCategories.edoNewUX.CourseArea;
import testCategories.edoNewUX.GeneralFeatures;
import Enums.ByTypes;
import Interfaces.TestCaseParams;

@Ignore  // igb 2018.11.08 --> deactivate FooterTests in old-LA
@Category(NonAngularLearningArea.class)
public class FooterTests extends BasicNewUxTest {

	
	NewUxCommunityPage communityPage;
	
	private static final String mailtoDefault = "info@edusoftlearning.com";

	@Before
	public void setup() throws Exception {
		super.setup();
		
	}
	
	@Test
	@Category(GeneralFeatures.class)
	@TestCaseParams(testCaseID = { "18451", "26832" },testMultiple=true)
	public void CustomFooterEdusoft() throws Exception {

		String companyName = "Edusoft";
		String companyHost = CorpUrl;
		String mailtoCustom = "custom@edusoftlearning.com";
		String imageFileName = "edusoftNew.svg";
				
		report.startStep("Verify Default 'Contact Us' mailto");
		homePage.checkCustomContactUsLink(mailtoDefault);
		
		report.startStep("Set custom 'Contact Us' mailto & refresh");
		dbService.setInstitutionContactUsEMail(autoInstitution.getInstitutionId(), mailtoCustom);
		sleep(1);
		webDriver.deleteCookiesAndCache();
		webDriver.refresh();
		sleep(2);
		
		try {
			for (int i = 0; i <= 3; i++) {
				sleep(3);		
				// Verify 'About' link & label
				report.startStep("Verify 'About Edusoft' link & label in Footer");
				homePage.checkCustomAboutLink(companyHost, companyName);
				report.finishStep();

				// Verify 'Contact Us' mailto
				report.startStep("Verify 'Contact Us' mailto");
				homePage.checkCustomContactUsLink(mailtoCustom);
				report.finishStep();

				// Verify LOGO image & link
				report.startStep("Verify LOGO image & link");
				homePage.checkCustomLogo(companyHost, imageFileName);
				report.finishStep();

				// Verify Privacy Statement & Legal Notices white label values
				report.startStep(" Verify Privacy Statement & Legal Notices white label values");
				homePage.checkCustomPrivacyLegal(companyName);
				sleep(2);
				report.finishStep();
			
				if (i==0){
					// Login
					report.startStep("Login as an exists student");
					//newUxHomePage = createUserAndLoginNewUXClass();
					homePage = getUserAndLoginNewUXClass();
					sleep(1);
				} else if (i==1)
				{
					report.startStep("Navigate to Learning area and open Footer");
					NewUxLearningArea learningArea;
					learningArea = new NewUxLearningArea(webDriver, testResultService);
					webDriver.scrollToTopOfPage();
					homePage.clickOnContinueButton();
					sleep(1);
					learningArea.clickOnFooterArrowBtn();
					sleep(1);
				} else if (i == 2) {
					report.startStep("Navigate to Community page");
					homePage.clickToOpenNavigationBar();
					homePage.openNewCommunityPage(false);
					sleep(2);
					
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			testResultService.addFailTest(e.toString(), false, true);
		} finally {
			report.startStep("Set default 'Contact Us' mailto & refresh");
			dbService.setInstitutionContactUsEMail(autoInstitution.getInstitutionId(), "NULL");
		}

	}
	
	@Test
	@Category(GeneralFeatures.class)
	@TestCaseParams(testCaseID = { "26134","18447", "18443", "18448", "18442" }, testMultiple = true)
	public void testFooterAllPages() throws Exception {
		
		report.startStep("Verifiy Footer on Login Page");
		sleep(1);
		testFooterAboutLink();
		testFooterPrivacyStatement();
		testFooterLegalNoticePopup();
		testFooterLogo();
				
		report.startStep("Login with a new student");
		homePage = getUserAndLoginNewUXClass();
		sleep(1);
		
		report.startStep("Verifiy Footer on Home Page");
		testFooterAboutLink();
		testFooterPrivacyStatement();
		testFooterLegalNoticePopup();
		testFooterLogo();
		sleep(2);
				
		report.startStep("Navigate To Community Page and verify Footer");
		communityPage = homePage.openNewCommunityPage(false);
		testFooterAboutLink();
		testFooterPrivacyStatement();
		testFooterLegalNoticePopup();
		testFooterLogo();
		
		report.startStep("Navigate To Magazine Page and verify Footer");
		communityPage.openMagazinePage();
		testFooterAboutLink();
		testFooterPrivacyStatement();
		testFooterLegalNoticePopup();
		testFooterLogo();
		
		report.startStep("Navigate To Idioms Page and verify Footer");
		homePage.openNewCommunityPage(false);
		communityPage.openIdiomsPage();
		testFooterAboutLink();
		testFooterPrivacyStatement();
		testFooterLegalNoticePopup();
		testFooterLogo();
		
		report.startStep("Navigate To Games Page and verify Footer");
		homePage.openNewCommunityPage(false);
		communityPage.openGamesPage();
		testFooterAboutLink();
		testFooterPrivacyStatement();
		testFooterLegalNoticePopup();
		testFooterLogo();
				
		report.startStep("Verifiy Footer on My Progress Page");
		homePage.clickOnHomeButton();
		homePage.clickOnMyProgress();
		testFooterAboutLink();
		testFooterPrivacyStatement();
		testFooterLegalNoticePopup();
		testFooterLogo();
		sleep(2);
		
		report.startStep("Navigate To Learning Area and Verify Footer in LA");
		homePage.clickToOpenNavigationBar();
		homePage.clickOnHomeButton();
		sleep(2);
		NewUxLearningArea learningArea = new NewUxLearningArea(webDriver, testResultService);
		webDriver.scrollToTopOfPage();
		sleep(2);
		homePage.clickOnContinueButton();
		sleep(2);
		learningArea.clickOnFooterArrowBtn();
		sleep(2);
		
		report.startStep("Verifiy Footer on Learning Area");
		testFooterAboutLink();
		testFooterPrivacyStatement();
		learningArea.clickOnFooterArrowBtn();
		testFooterLegalNoticePopup();
		learningArea.clickOnFooterArrowBtn();
		testFooterLogo();
		testFooterEdLogo();
				
		report.startStep("Close Footer and check links not displayed");
		learningArea.clickOnFooterArrowBtn();
		learningArea.verifyFooterLinksNotDisplayed();
								
	}
		
	public void testFooterAboutLink() throws Exception {
		// webDriver.switchToNextTab();

		homePage.clickOnAboutEdusoft();
		sleep(8);

		webDriver.switchToNextTab();

		String currentUrl = "";

		currentUrl = webDriver.waitForSpecificCurrentUrl(currentUrl, "www");
		// currentUrl = webDriver.getUrl();
		
		webDriver.closeNewTab(2);
		
		testResultService.assertEquals(CorpUrl, currentUrl,
				"about site is not displayed");
	}

	public void testFooterPrivacyStatement() throws Exception {
		report.startStep("Check privacy statement");
		homePage.clickOnPrivacyStatement();
		String title = homePage.getOcModalTitleTextInFooter();
		testResultService.assertEquals("Privacy Statement", title);
		webDriver.waitForElement("//a[@title='Close window']", ByTypes.xpath).click();
		sleep(2);

	}

	public void testFooterLegalNoticePopup() throws Exception {
		homePage.clickOnLegalNotice();
		String title = homePage.getOcModalTitleTextInFooter();
		testResultService.assertEquals("Legal Notices", title);
		webDriver.waitForElement("//a[@title='Close window']", ByTypes.xpath).click();
		sleep(2);
		
	}

	public void testFooterLogo() throws Exception {

		report.startStep("Verifiy that logo is displayed");
		NewUXLoginPage loginPage = new NewUXLoginPage(webDriver,
				testResultService);
		sleep(2);
		testResultService.assertEquals(true,
				loginPage.getfFooterImageDisplayed(),
				"Footer image is not displayed");

		report.startStep("Mouse hover on logo");

		report.startStep("Click on logo");
		loginPage.getFooterLogoElement().click();
		sleep(2);
		webDriver.switchToNextTab();
		sleep(8);
		String currentUrl = webDriver.getUrl();
		testResultService.assertEquals(CorpUrl, currentUrl);

		report.startStep("Close company site tab");
		webDriver.closeNewTab(2);
	}
	
	public void testFooterEdLogo() throws Exception {
		
		report.startStep("Verifiy that ED logo is displayed");
		NewUxLearningArea learningArea;
		learningArea = new NewUxLearningArea(webDriver, testResultService);
		testResultService.assertEquals(true,
				learningArea.verifyEdLogoDisplayedInFooter(),
				"Footer ED LOGO image is not displayed");		
	}
	
	@After
	public void tearDown() throws Exception {
		
		super.tearDown();
	}
}
