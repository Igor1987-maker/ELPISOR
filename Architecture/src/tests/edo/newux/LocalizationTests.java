package tests.edo.newux;

import java.security.PublicKey;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import Enums.ByTypes;
import pageObjects.edo.NewUXLoginPage;
import pageObjects.edo.NewUxMyProfile;
import services.PageHelperService;
import testCategories.inProgressTests;
import testCategories.reg3;
import testCategories.edoNewUX.GeneralFeatures;
import Interfaces.TestCaseParams;

@Category(reg3.class)
public class LocalizationTests extends BasicNewUxTest {

	NewUxMyProfile myProfile;
	//String instName = "undefined";
		
	private static final String HE_DICT_FILE = "files/dictFiles/HE/newUx/newUxHomePage_HE.properties";
	private static final String ES_DICT_FILE = "files/dictFiles/ES/newUx/newUxHomePage_ES.properties";
	private static final String FR_DICT_FILE = "files/dictFiles/FR/newUx/newUxHomePage_FR.properties";
	private static final String JP_DICT_FILE = "files/dictFiles/JP/newUx/newUxHomePage_JP.properties";
	private static final String KR_DICT_FILE = "files/dictFiles/KR/newUx/newUxHomePage_KR.properties";
	private static final String TH_DICT_FILE = "files/dictFiles/TH/newUx/newUxHomePage_TH.properties";
	private static final String VN_DICT_FILE = "files/dictFiles/VN/newUx/newUxHomePage_VN.properties";
	
	public String[] dictFiles = new String[] { ES_DICT_FILE, FR_DICT_FILE, JP_DICT_FILE, KR_DICT_FILE, TH_DICT_FILE, VN_DICT_FILE, HE_DICT_FILE };
	public String[] languages = new String[] { "Spanish" , "French", "Japanese", "Korean", "Thai", "Vietnamese", "Hebrew" };
	public int [] langComboIndex = {15,4,9,10,16,18,7}; 

	@Before
	public void setup() throws Exception {
		institutionName=institutionsName[3];
		super.setup();
		report.startStep("Get CI latest link and open: " + institutionName +  "; institition");
		report.finishStep();
	}
	
	@Test
	@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "18450", "18856" }, testMultiple = true)
	public void testFooterLinksLocalization() throws Exception {

		NewUXLoginPage loginPage = new NewUXLoginPage(webDriver, testResultService);
		
		report.startStep("Get Local user for the test");
		String[] userDetails = getLocalUser();
		//institutionName = dbService.getInstituteNameById(localInstId);
		
		userName = userDetails[0];
		String password = userDetails[1];
		studentId = userDetails[2];
		
		String language = "default";
		String dictFile = "default";	
		
		for (int i = 0; i < languages.length; i++) {
			
			report.addTitle("This is round:" + (i+1) + "out of:" + languages.length);
			
			language = languages[i];
			dictFile = dictFiles[i];
			dictionaryService.loadDictionaryFile(dictFile);
			
			if (language.equalsIgnoreCase("Japanese")){
				int a=0;
			}
			report.startStep("Set "+ languages[i] + " Language Full Support");
			pageHelper.SetInstLangFullSupport(institutionId, language);
			pageHelper.updateCommunityVersion(institutionId);
			pageHelper.restartBrowserInNewURL(institutionName, false);
			
			//webDriver.deleteCookiesAndCache();
			//webDriver.refresh();
			webDriver.switchToTopMostFrame();
			
		report.startStep("Verify Footer Links Localization on Login Page as Language: " + language);
			verifyFooterLinksTranslation(language);
			
		report.startStep("Login as Local student");
			loginPage.loginAsStudent(userName, password);
		report.report("UserName is: "+ userName + "UserId is:" + studentId);
			homePage.waitHomePageloaded();
			pageHelper.skipOnBoardingHP();
			pageHelper.closeLastSessionImproperLogoutAlert();
		
			
			report.startStep("Verify Footer Links Localization on Home Page: " + language);
			verifyFooterLinksTranslation(language);
			
			report.startStep("Logout");
			homePage.clickOnLogOut();
			webDriver.switchToTopMostFrame();
			testResultService.assertEquals(false, loginPage.isSubmitButtonEnabled(), "Checking that ED Login Page displayed at: " + language);
			
		}

	}

	@Test
	@Category(GeneralFeatures.class)
	@TestCaseParams(testCaseID = { "22607" }, testMultiple = true)
	public void testMyProfileChangeLanguage() throws Exception {
		
		report.startStep("Re-enable My Profile edit option");
		dbService.setInstitutionMyProfileGroupId(institutionId, "2");

		pageHelper.restartBrowserInNewURL(institutionName, false);
		//webDriver.maximize();
		
		myProfile = new NewUxMyProfile(webDriver, testResultService);
		
		String language = "default";
		String dictFile = "default";	
		
	report.startStep("Login as Student");
		getUserAndLoginLocalClassName();
		homePage.skipNotificationWindow();
		//pageHelper.skipOnBoardingHP();	
		homePage.closeLastOpenLessonsElement();
			
		for (int i = 0; i < languages.length; i++) {
			
			language = languages[i];
			dictFile = dictFiles[i];
			dictionaryService.loadDictionaryFile(dictFile);
						
			report.startStep("Open My Profile and change Language To "+ language +" Full Support");
			homePage.waitHomePageloadedFully();
			homePage.clickOnMyProfileWithoutTextCheck();
			
			//homePage.clickOnMyProfile();
			sleep(2);
			homePage.switchToMyProfile();
		
			myProfile.selectLangSupportByValue(languages[i], 2);
			myProfile.clickOnUpdate();
			sleep(2);
			verifyHomePageLabelsTranslation(language);
		}
		
	}

	private void verifyFooterLinksTranslation (String language) throws Exception {
		
		// Contact US
		String translation = dictionaryService.getProperty("1_contact_us");
		sleep(1);
		String actual = homePage.getContactUsLinkText();
		testResultService.assertEquals(translation, actual, "Contact Us link " +language+ " translation is not valid");
					
		// Privacy Statement
		translation = dictionaryService.getProperty("5_privacy_statement");
		actual = homePage.getPrivacyStatemantLinkText();
		testResultService.assertEquals(translation, actual, "Privacy Statement "+language+" link translation is not valid");
	}
	
	private void verifyHomePageLabelsTranslation (String language) throws Exception {
		
		report.startStep("Check My Profile " + language + " localization");
		String translation = dictionaryService.getProperty("27_my_profile");
		String actual = myProfile.getIFrameTitle();
		testResultService.assertEquals(translation, actual, "My Profile label " +language+ " translation is not valid");
		
		report.startStep("Close My Profile");
		myProfile.close();
		
		report.startStep("Check Widget Label " + language + " localization");
		translation = dictionaryService.getProperty("12_course_completion");
		actual = homePage.getCourseCompletionWidgetLabel(translation);
		testResultService.assertEquals(translation.trim(), actual.trim(), "Course Completion label " +language+ " translation is not valid");
		sleep(2);
	}
		
	@After
	public void tearDown() throws Exception {
		institutionName="";
		// Set the Institution language to English only - Specific for this test
		report.startStep("Set Only English Support");
		pageHelper.SetInstLangOnlyEnglish(dbService.getInstituteNameById(institutionId));
		report.finishStep();
		
		super.tearDown();
	}
}