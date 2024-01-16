package tests.tms;


import Enums.ByTypes;
import Interfaces.TestCaseParams;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import pageObjects.CreateNewPromotionPage;
import pageObjects.TMSPromotionPage;
import pageObjects.edo.*;
import pageObjects.tms.DashboardPage;
import pageObjects.tms.TmsHomePage;
import testCategories.AngularLearningArea;
import testCategories.edoNewUX.SanityTests;
import tests.edo.newux.SpeechRecognitionBasicTestNewUX;

import java.io.Closeable;
import java.util.List;

public class NewUxTmsSettings extends SpeechRecognitionBasicTestNewUX {

	protected NewUxCommunityPage communityPage;		
	NewUXLoginPage loginPage;
	DashboardPage dashboardPage;
	TmsHomePage tmsHomePage;
	NewUxLearningArea learningArea;
	NewUxLearningArea2 learningArea2;
	NewUxMyProfile myProfile;
	
 
	private boolean setCommunityToGlobal;
	private boolean returnStudyPlanner;
	private boolean languageSupportLevelReset;
	private boolean resetMyProfileSettings = false;
	String instID = "";
	String userName ="";
	
	@Before
	public void setup() throws Exception {
		super.setup();
		
	}

//--igb 2018.07.12 --> add new test ------------------------------	
	@Test
	@TestCaseParams(testCaseID = { "50886" })
	public void testCheckNextCourseFeature() throws Exception {
		
		report.startStep("Open New UX Login Page");
			loginPage = new NewUXLoginPage(webDriver, testResultService);
	
		report.startStep("Login as Admin");
			loginPage = new NewUXLoginPage(webDriver, testResultService);
			instID = configuration.getProperty("institution.id");
			pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(configuration.getProperty("schoolAdmin.user"),instID));
			tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
			sleep(2);
		
		report.startStep("Navigate to Settings > Features");
			tmsHomePage.switchToMainFrame();
			tmsHomePage.clickOnSettings();
			sleep(1);
			tmsHomePage.clickOnFeatures();
			sleep(2);

		report.startStep("Check Settings > Features > \'Next Course\'");
			
		tmsHomePage.switchToFormFrame();
		
		boolean chkRes = checkComboOption("SelectFeature", "Next Course");
		
		if(chkRes)
			report.report("\'Next Course\' present in List");
		else
			report.report("\'Next Course\' not present in List");

		
		report.startStep("Log out of TMS");
			webDriver.switchToMainWindow();
			tmsHomePage.switchToMainFrame();
			tmsHomePage.clickOnExit();
			sleep(1);
			
		testResultService.assertEquals(false, loginPage.isSubmitButtonEnabled(), "Checking that Login Page displayed");
	}			
//--igb 2018.07.12 --> add new test -------------	
	
	@Test
	@TestCaseParams(testCaseID = { "44310" })
	public void testUpdateMyProfileSettingInTms() throws Exception {
		
		
		report.startStep("Change to Local institution");
		institutionName=institutionsName[3];
		pageHelper.restartBrowserInNewURL(institutionName, true);
		sleep(2);

		report.startStep("Open New UX Login Page");
			loginPage = new NewUXLoginPage(webDriver, testResultService);
	
		report.startStep("Login as Admin");
			String userName = dbService.getUserNameById(dbService.getAdminIdByInstId(institutionId), institutionId);
			NewUXLoginPage loginPage = new NewUXLoginPage(webDriver, testResultService);
			tmsHomePage = loginPage.loginAsTmsUser(userName, "12345");
			sleep(2);
		
		report.startStep("Navigate to Settings > Features");
			tmsHomePage.switchToMainFrame();
			tmsHomePage.clickOnSettings();
			sleep(1);
			tmsHomePage.clickOnFeatures();
			sleep(2);
		
		report.startStep("Select My Profile and press GO");
			tmsHomePage.switchToFormFrame();
			tmsHomePage.selectFeature("MP");
			sleep(1);
			tmsHomePage.ClickToGo();
			sleep(1);
		
		report.startStep("Change Radio button, press go and verify change");	
			webDriver.switchToTopMostFrame();
			tmsHomePage.switchToMainFrame();
			List<WebElement> chkRbtns  = webDriver.getElementsByXpath("//*[@id='MPGroup']");
			for (int i = 0; i < chkRbtns.size(); i++) {
				if(chkRbtns.get(i).getAttribute("value").equals("FieldUsers cannot update My Profile information.")) {
					chkRbtns.get(i).click();
					break;
				}
			}
			try {
					tmsHomePage.clickOnSaveFeature();
					tmsHomePage.switchToFormFrame();
					tmsHomePage.ClickToGo();
					sleep(3);
					if(!dbService.getInstituteMyProfileGroupIdByName(configuration.getProperty("local_institutionId")).equals("3"))
					{
						testResultService.addFailTest("Radio button change wasn't saved");
						org.junit.Assert.fail("Radio button change wasn't saved");
					}
					else
						resetMyProfileSettings = true;
					
				report.startStep("Logout as admin");
					webDriver.switchToMainWindow();
					tmsHomePage.switchToMainFrame();
					sleep(3);
					tmsHomePage.clickOnExit();
					testResultService.assertEquals(false, loginPage.isSubmitButtonEnabled(), "Checking that ED Login Page displayed");	
				
				report.startStep("Login as student and Open My Profile");
					getUserAndLoginLocalClassName();
					sleep(3);
					homePage.clickOnMyProfileWithoutTextCheck();
					sleep(3);
					homePage.switchToMyProfile();
					
				report.startStep("Verify edit status of My Profile and logout.");
					NewUxMyProfile myProfile = new NewUxMyProfile(webDriver,testResultService);
					sleep(1);
					if (webDriver.waitForElement("FirstName",ByTypes.name).isEnabled() == true){
						testResultService.addFailTest("My Profile values can be modified");
						org.junit.Assert.fail("My Profile values can be modified");
					}
					myProfile.close(true);
					report.report("Closed My Profile window");
					sleep(3);
					homePage.clickOnLogOut();
					
			} finally {
			
				report.startStep("Re-enable My Profile edit option");
				dbService.setInstitutionMyProfileGroupId(institutionId, "2");
				resetMyProfileSettings = false;
		}
	}

	@Test
	@TestCaseParams(testCaseID = { "30749" })
	public void testUpdateStudyPlannerSettingInTms() throws Exception {
		
		report.startStep("Init test data");
			int iteration = 1;
			String dbValue;
			String studentName = configuration.getProperty("student.user.name");
			studentId = dbService.getUserIdByUserName(studentName, institutionId);
			String UserFullName = dbService.getUserFirstNameByUserId(studentId) + " " + dbService.getUserLastNameByUserId(studentId);
			String className = configuration.getProperty("classname.progress");
					
		report.startStep("Login as Admin");
			loginPage = new NewUXLoginPage(webDriver, testResultService);
			//instID = configuration.getProperty("institution.id");
			pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(configuration.getProperty("schoolAdmin.user"),institutionId));
			tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
			sleep(2);
		
		report.startStep("Click on Settings");
			tmsHomePage.switchToMainFrame();
			tmsHomePage.clickOnSettings();
			sleep(2);

		report.startStep("Click on Features");
			tmsHomePage.clickOnFeatures();
			sleep(4);
	
		report.startStep("Select Study Planner, Class, Student and press GO");
			tmsHomePage.switchToFormFrame();
			tmsHomePage.selectFeature("SP");
			sleep(1);
			//webDriver.selectElementFromComboBox("SelectClass", className);
			//webDriver.selectElementFromComboBox("SelectUser", UserFullName);
			
			webDriver.selectValueFromComboBox("SelectClass", className);
			webDriver.selectValueFromComboBox("SelectUser", UserFullName);
			tmsHomePage.clickOnGo();
			sleep(2);
			returnStudyPlanner = true;
		
		while (iteration < 4) {
			report.startStep("Change Radio button, press go and verify change");
				webDriver.switchToTopMostFrame();
				tmsHomePage.switchToMainFrame();
				webDriver.waitForElement("//input[@type='radio'][@value='"+ iteration + "']", ByTypes.xpath).click();
				tmsHomePage.clickOnSaveFeature();
				tmsHomePage.switchToFormFrame();
				tmsHomePage.clickOnGo();
				sleep(2);
				dbValue = dbService.getUserStudyPlanSettingsByUserId(studentId);
				if(!dbValue.equals(iteration + ""))
				{
					testResultService.addFailTest("Radio button change wasn't saved");
					org.junit.Assert.fail("Radio button change wasn't saved");
				}
				iteration++;
		}
		
		report.startStep("Log out of TMS");
			returnStudyPlanner = false;
			webDriver.switchToTopMostFrame();
			tmsHomePage.switchToMainFrame();
			tmsHomePage.clickOnExit();
			sleep(1);
			testResultService.assertEquals(false, loginPage.isSubmitButtonEnabled(), "Checking that Login Page displayed");
	}

	@Test
	@Category(AngularLearningArea.class)
	@TestCaseParams(testCaseID = { "55539" })
	public void testChangeWalkthroughSettings() throws Exception {
	
		for (int i =0 ; i<2 ; i++) {
			report.startStep("Login as Admin");
				loginPage = new NewUXLoginPage(webDriver, testResultService);
				instID = configuration.getProperty("institution.id");
				pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(configuration.getProperty("schoolAdmin.user"),instID));
				tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
				
				homePage.waitUntilLoadingMessageIsOver();
			
			report.startStep("Click on Settings");
				tmsHomePage.switchToMainFrame();
				tmsHomePage.clickOnSettings();
				sleep(2);
	
			report.startStep("Click on Features");
				tmsHomePage.clickOnFeatures();
				sleep(4);
		
			report.startStep("Select Walkthrough settings and press GO");
				tmsHomePage.switchToFormFrame();
				tmsHomePage.selectFeature("WT");
				sleep(2);
				tmsHomePage.clickOnGo();
				sleep(2);
			
			report.startStep("Get status of Walkthrough");
				webDriver.switchToTopMostFrame();
				tmsHomePage.switchToMainFrame();
				Boolean checkboxStatus = webDriver.waitForElement("walkthrough", ByTypes.id).isSelected();
				report.report("Value of checkbox is " + String.valueOf(checkboxStatus));
			
			report.startStep("Change status of Walkthrough");	
				if (checkboxStatus)
					webDriver.setCheckBoxState(false, "walkthrough");
				else
					webDriver.setCheckBoxState(true, "walkthrough");
				tmsHomePage.clickOnPromotionAreaMenuButton("Save");
				sleep(2);
			
			report.startStep("Verify change appear on DB");
				String dbValue = dbService.getInstPropertyValueByProperyId(instID, 8);
				if (checkboxStatus)
					testResultService.assertEquals("0", dbValue, "checkbox change didn't work");
				else
					testResultService.assertEquals("1", dbValue, "checkbox change didn't work");
			
			report.startStep("Log out of TMS");
				tmsHomePage.clickOnExit();
				sleep(1);
				testResultService.assertEquals(false, loginPage.isSubmitButtonEnabled(), "Checking that Login Page displayed");
				
			report.startStep("Login to ED with user who is yet to see Walkthrough");
				String[] userDetails = pageHelper.getUserFromDbByInstitutionIdAndClassName(instID, configuration.getClassName());
				dbService.updateOnboardingStateForUser(userDetails[2],1,false);
				pageHelper.setUserLoginToNull(userDetails[2]);
				homePage = loginPage.loginAsStudent(userDetails[0], userDetails[1]);
				sleep(3);
				homePage.closeModalPopUp();
				
			report.startStep("Examine on-boarding status as student");
				if (checkboxStatus) { // We expect walkthrough to appear
					pageHelper.skipOnBoardingHP();
					/*
					String continueButtonText;
					do {
						webDriver.waitForElement("//a[contains(@class, 'onBoarding__next')]", ByTypes.xpath).click();
						sleep(1);
						continueButtonText = webDriver.waitForElement("//a[contains(@class, 'onBoarding__next')]", ByTypes.xpath).getText();
					} while(!continueButtonText.equals("Close"));
					webDriver.waitForElement("//a[contains(@class, 'onBoarding__next')]", ByTypes.xpath).click();
				*/
				}
				else { // We don't expect walkthrough to appear
					webDriver.checkElementNotExist("//a[contains(@class, 'onBoarding__next')]");
					dbService.updateOnboardingStateForUser(userDetails[2],1,true);
				}
				
			report.startStep("Logout Student");
				homePage.clickOnLogOut();
				sleep(2);
		}
	}
	
	@Test
	@TestCaseParams(testCaseID = { "7141" })
	public void testToggleCommunitySettingsWorkingMode() throws Exception {
		
		report.startStep("Init test data");
			int iteration = 0;
			List<WebElement> chkRbtns;
			List<WebElement> forumContainers;
			String forumMessage;
			
		while (iteration<2) {
		report.startStep("Login as Admin");
			loginPage = new NewUXLoginPage(webDriver, testResultService);
			instID = configuration.getProperty("institution.id");
			pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(configuration.getProperty("schoolAdmin.user"),instID));
			tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
		
		report.startStep("Click on Settings");
			tmsHomePage.switchToMainFrame();
			tmsHomePage.clickOnSettings();
			sleep(2);
			
		report.startStep("Click on Community");
			tmsHomePage.clickOnCommunity();
			sleep(3);
		
		report.startStep("Change Community Settings Working Mode");
			chkRbtns  = webDriver.getElementsByXpath("//td/input");
			chkRbtns.get(iteration).click();
			tmsHomePage.clickOnPromotionAreaMenuButton("Save");
			if (iteration == 0) {
				setCommunityToGlobal = true;
			}
			else {
				setCommunityToGlobal = false;
			}
			sleep(2);
			
		report.startStep("Logout of TMS");
			tmsHomePage.clickOnExit();
			testResultService.assertEquals(false, loginPage.isSubmitButtonEnabled(), "Checking that Login Page displayed");
			
		report.startStep("Login to ED");
			pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(configuration.getProperty("student.user.name"),instID));
			loginPage.loginAsStudent(configuration.getProperty("student.user.name"),
					configuration.getProperty("student.user.password"));
			sleep(1);
			homePage.closeModalPopUp();
			
		report.startStep("Navigate To Forum Page");
			communityPage = homePage.openNewCommunityPage(false);
			communityPage.openForumPage();
			webDriver.printScreen("After opening forum page");
			
		report.startStep("Verify Global Forum Status");
			if (iteration == 0 ) {
				forumContainers = webDriver.getElementsByXpath("//*[@id='topicsCont']/div");
				forumMessage = forumContainers.get(iteration).getText().trim();
				testResultService.assertEquals("#This feature is not available because you are not connected to Edusoft's Global Community.",forumMessage);
			}
			else {
				forumContainers = webDriver.getElementsByXpath("//*[@id='topicsCont']/div/ol/li/a");
				forumMessage = forumContainers.get(iteration - 1).getText();
				forumMessage = forumMessage.substring(0, forumMessage.length() - 8);
				testResultService.assertEquals("Getting To Know You",forumMessage);
			}
		
		report.startStep("Logout of ED");
			webDriver.closeNewTab(1);
			homePage.clickOnLogOut();
			testResultService.assertEquals(false, loginPage.isSubmitButtonEnabled(), "Checking that Login Page displayed");
		iteration++;	
		}

	}

	@Test
	@Category(AngularLearningArea.class)
	@TestCaseParams(testCaseID = {"44330","44329","44328","44327","44322","44321","44320","44319","44318","44317" })
	public void testCheckPDFExistance() throws Exception {
			
		report.startStep("Login as Admin");
			loginPage = new NewUXLoginPage(webDriver, testResultService);
			instID = configuration.getProperty("institution.id");
			pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(configuration.getProperty("schoolAdmin.user"),instID));
			tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
		
		report.startStep("Navigate to Resources");
			tmsHomePage.switchToMainFrame();
			tmsHomePage.clickOnResources();
			sleep(2);
		
		report.startStep("Checking status of Guidelines PDFs");
			tmsHomePage.clickOnGuidelines();
			//checkPDFLinksInArea("Guidelines");
			checkPdfLinkOpened("Guidelines");
		
		report.startStep("Checking status of Lesson Plans PDFs");
			tmsHomePage.clickOnLessonPlans();
			//checkPDFLinksInArea("LessonPlans");
			checkPdfLinkOpened("LessonPlans");

		report.startStep("Checking status of Worksheets PDFs");
			tmsHomePage.clickOnWorksheets();
			//checkPDFLinksInArea("Worksheets");
			checkPdfLinkOpened("Worksheets");
			
		report.startStep("Checking status of Explore Texts PDFs");
			tmsHomePage.clickOnExploreTexts();
			//checkPDFLinksInArea("ExploreTexts");
			checkPdfLinkOpened("ExploreTexts");
			
		report.startStep("Checking status of Words Lists PDFs");
			tmsHomePage.clickOnWordLists();
			//checkPDFLinksInArea("WordZoneIdiomsLists");
			checkPdfLinkOpened("WordZoneIdiomsLists");

		report.startStep("Checking status of Scope and Sequence PDFs");
			tmsHomePage.clickOnScopeAndSequence();
			//checkPDFLinksInArea("ScopeAndSequence");
			checkPdfLinkOpened("ScopeAndSequence");
			
		report.startStep("Navigate to Assessment");
			tmsHomePage.clickOnAssessment();
			sleep(2);
			
		report.startStep("Checking status of Guidelines PDFs");
			tmsHomePage.clickOnGuidelines();
			//checkPDFLinksInArea("Guidelines");
			checkPdfLinkOpened("Guidelines");
		
		report.startStep("Checking status of Projects PDFs");
			tmsHomePage.clickOnProjects();
			//checkPDFLinksInArea("Projects");
			checkPdfLinkOpened("Projects");
			
		report.startStep("Checking status of Role-Plays PDFs");
			tmsHomePage.clickOnRolePlays();
			//checkPDFLinksInArea("RolePlays");
			checkPdfLinkOpened("RolePlays");
			
		report.startStep("Checking status of Rubics PDFs");
			tmsHomePage.clickOnRubrics();
			//checkPDFLinksInArea("Rubrics");	
			checkPdfLinkOpened("Rubrics");
			
		report.startStep("Checking status of Exit Tests PDFs");
			tmsHomePage.clickOnExitTests();
			//checkPDFLinksInArea("ExitTest");
			checkPdfLinkOpened("ExitTest");
	}
	
	private void checkPdfLinkOpened(String string) throws Exception {
		WebElement element=null;
		
		for (int i=0; i<2 && element == null; i++){
			element = webDriver.waitForElement("resultRoot", ByTypes.className, false, webDriver.getTimeout());
		}
		
		String ExpectedLinkTitle;
		//String actualLinkTitle;
		//WebElement tempElement;
		List <WebElement> links = webDriver.getElementsByXpath("//a[contains(@class,'FAQtitle')]");
		
		OkHttpClient client = new OkHttpClient();
		Closeable responses = null;
		
		for (int i = 0 ; i < links.size(); i++) {
			ExpectedLinkTitle = links.get(i).getAttribute("href");
					//.replace(" - "," ").replace("\"","").replace("Role-Plays","Roleplays");
			links.get(i).click();
			sleep(1);
			webDriver.switchToPopup();
			String url = webDriver.getUrl();
			
			Request request = new Request.Builder().url(url).build();
			
			responses = client.newCall(request).execute();
			okhttp3.Response response = client.newCall(request).execute();
			int code = response.code();
			
			testResultService.assertEquals(true,code==200,"The link + '" + url + "' + is broken");
			//testResultService.assertEquals(true,(url.contains(string + ".pdf")),"The link + '" + string + "' + is broken");
			
			//testResultService.assertEquals("application/pdf", tempElement.getAttribute("type"),"PDF for " + ExpectedLinkTitle + " wasn't found");
			//actualLinkTitle = tempElement.getAttribute("src");
			//actualLinkTitle = actualLinkTitle.substring(actualLinkTitle.indexOf(tempArea + "/") + tempArea.length() + 1,actualLinkTitle.length()-4).replace("%20"," ").replace("_"," ").replace("Exit Testanswer k","Answer K");
			//testResultService.assertEquals(ExpectedLinkTitle, actualLinkTitle,"- Opened file is not " + ExpectedLinkTitle);
			webDriver.closeNewTab(0);
			webDriver.switchToMainWindow();
			tmsHomePage.switchToMainFrame();
		}
	}

	private void checkPDFLinksInArea(String tempArea) throws Exception {
		WebElement element=null;
		
		for (int i=0; i<2 && element == null; i++){
			element = webDriver.waitForElement("resultRoot", ByTypes.className, false, webDriver.getTimeout());
		}
		
		String ExpectedLinkTitle;
		String actualLinkTitle;
		WebElement tempElement;
		List <WebElement> links = webDriver.getElementsByXpath("//a[contains(@class,'FAQtitle')]");
		for (int i = 0 ; i < links.size(); i++) {
			ExpectedLinkTitle = links.get(i).getText().replace(" - "," ").replace("\"","").replace("Role-Plays","Roleplays");
			links.get(i).click();
			sleep(2);
			webDriver.switchToPopup();
			tempElement = webDriver.waitForElement("plugin",ByTypes.id, 10, true, ExpectedLinkTitle + " is a broken link");
			testResultService.assertEquals("application/pdf", tempElement.getAttribute("type"),"PDF for " + ExpectedLinkTitle + " wasn't found");
			actualLinkTitle = tempElement.getAttribute("src");
			actualLinkTitle = actualLinkTitle.substring(actualLinkTitle.indexOf(tempArea + "/") + tempArea.length() + 1,actualLinkTitle.length()-4).replace("%20"," ").replace("_"," ").replace("Exit Testanswer k","Answer K");
			testResultService.assertEquals(ExpectedLinkTitle, actualLinkTitle,"- Opened file is not " + ExpectedLinkTitle);
			webDriver.closeNewTab(0);
			webDriver.switchToMainWindow();
			tmsHomePage.switchToMainFrame();
		}
	}
	
	@Test
	@Category(AngularLearningArea.class)
	@TestCaseParams(testCaseID = { "41913" })
	public void testLangSupportBySeeTranslation2() throws Exception {
		
		report.startStep("Init Test data");
			int iteration = 1;
			//instID = configuration.getProperty("institution.id");
			String classForTest = configuration.getProperty("classname.progress");
			String classId = dbService.getClassIdByName(classForTest, institutionId);
			List<WebElement> supportLevel;
		
		while (iteration >= 0)
		{
			report.startStep("Login as Admin");
				loginPage = new NewUXLoginPage(webDriver, testResultService);
				//instID = configuration.getProperty("institution.id");
				pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(configuration.getProperty("schoolAdmin.user"),institutionId));
				tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
				
			report.startStep("Navigate to Settings > Languages");
				tmsHomePage.switchToMainFrame();
				tmsHomePage.clickOnSettings();
				sleep(2);
				tmsHomePage.clickOnLanguage();
				sleep(5);
				tmsHomePage.selectClass(classForTest, true, true);
			
			report.startStep("Change Language Degree of Support");
				supportLevel  = webDriver.getElementsByXpath("//input[contains(@name,'Support')]");
				supportLevel.get(iteration).click();
				tmsHomePage.clickOnPromotionAreaMenuButton("Save");
				if (iteration == 1)
					languageSupportLevelReset = true;
				else
					languageSupportLevelReset = false;
				sleep(1);
			
			report.startStep("Verify Change in DB");
				testResultService.assertEquals(iteration + "", dbService.getClassLanguageSupportLevel(classId), "Lang support wasn't saved in DB");
			
			report.startStep("Logout of TMS");
				tmsHomePage.clickOnExit();
				testResultService.assertEquals(false, loginPage.isSubmitButtonEnabled(), "Checking that Login Page displayed");
			
			report.startStep("Login to ED");
				pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(configuration.getProperty("student.user.name"),institutionId));
				loginPage.loginAsStudent(configuration.getProperty("student.user.name"),
						configuration.getProperty("student.user.password"));
				sleep(3);
				homePage.closeModalPopUp();
				
			report.startStep("Navigate to FD->U1->L1->S2->T1");
				learningArea2 = homePage.navigateToTask("FD", 1, 1, 2, 1);
				sleep(1);				

			report.startStep("Interact with See Translation resource");
				if (iteration == 1)
				{
					learningArea2.clickOnVocabTransTool();
					String transtext = webDriver.waitForElement("keywordExampleText",ByTypes.className).getText();
					testResultService.assertEquals(transtext, "apple - apple (mela)", "Translation didn't appear");
				}
				else
				{
					webDriver.checkElementNotExist("//*[@id='getTransIcon']");
				}
			
			report.startStep("Logout of ED");
				learningArea2.clickOnLogoutLearningArea();
				testResultService.assertEquals(false, loginPage.isSubmitButtonEnabled(), "Checking that ED Login Page displayed");
				iteration--;
		}
	}

	@Test
	@Category(SanityTests.class)
	@TestCaseParams(testCaseID = { "37864" })
	public void testCreateNewPromotionSlide() throws Exception {
		
		boolean isDeleted = false;
		TMSPromotionPage tmsPromotionPage;
		report.startStep("Init test data");
		String promotionTypeTitle = "Title" + dbService.sig(3);
		String promotionSubTitle = "Subtitle" + dbService.sig(3);
		String promotionText = "Text" + dbService.sig(3);
		String promotionBtnText = "CNN";
		String promotionBtnLink = "https://edition.cnn.com/";
		//String fileName = "promotionImageForAutomationTC.jpg"; 
		//String bodyFile = "\\\\NEWJENKINS\\Shared\\" + fileName;
		// "+configuration.getGlobalProperties("logserverName")+" // new server
		//String elementId = "SelectFile"; 
		
		try {
			
			report.startStep("Login as Admin");
			loginPage = new NewUXLoginPage(webDriver, testResultService);
			instID = institutionId;// configuration.getProperty("institution.id");
			pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(configuration.getProperty("schoolAdmin.user"),instID));
			tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
			homePage = new NewUxHomePage(webDriver,testResultService);
			homePage.closeAllNotifications();
			tmsHomePage.waitForPageToLoad();
			// Initialize tms promotion page
			tmsPromotionPage = new TMSPromotionPage(webDriver, testResultService);
			
			report.startStep("Navigate to Settings > Promotion Area");
			/*tmsHomePage.switchToMainFrame();
			tmsHomePage.clickOnSettings();
			sleep(1);
			tmsHomePage.clickOnPromotionArea();
			sleep(2);*/
			tmsPromotionPage.goToPromotionArea();
			
			// Initialize create new promotion page
			CreateNewPromotionPage createNewPromotionPage = new CreateNewPromotionPage(webDriver, testResultService);

			report.startStep("Create New Promotion");
			/*report.startStep("Press on New button");
			tmsHomePage.clickOnPromotionAreaMenuButton("New");
			sleep(1);
		
			report.startStep("Select and populate slide template");
			webDriver.switchToNewWindow();
			webDriver.waitForElement("//span/a[text()='Template 2']", ByTypes.xpath).click();
			populatePromotionSlideValue("home__marketingMagTitle",promotionTypeTitle);
			populatePromotionSlideValue("home__marketingMagTypeTitle",promotionSubTitle);
			populatePromotionSlideValue("home__marketingMagText",promotionText);
			populatePromotionSlideValue("home__marketingMagBtn",promotionBtnText);
			populatePromotionSlideValue("slideLinkUrl",promotionBtnLink);
		
			report.startStep("Add custom image to Slide");
			tmsHomePage.addCustomImageToSlide(bodyFile, elementId);	
			
			report.startStep("Save and Close Slide creation window");	
			webDriver.waitForElement("//span/a[text()='Save']", ByTypes.xpath).click();
			webDriver.closeAlertByAccept();
			sleep(1);
			webDriver.waitForElement("//span/a[text()='Cancel']", ByTypes.xpath).click();
			sleep(2);*/
			createNewPromotionPage.createNewPromotion(promotionTypeTitle, promotionSubTitle, promotionText, promotionBtnText, promotionBtnLink);
			
			/*report.startStep("Refresh");
			webDriver.refresh();
			
			report.startStep("Navigate to Settings > Promotion Area");
			tmsPromotionPage.goToPromotionArea();*/
			
			report.startStep("Publish new slide");
			//webDriver.switchToMainWindow();
			//tmsHomePage.switchToMainFrame();
			//sleep(1);
			/*webDriver.waitForElement("//td//div[contains(text(),'" + promotionTypeTitle + "')]",ByTypes.xpath).click();
			sleep(1);
			tmsHomePage.clickOnPromotionAreaMenuButton("Publish");*/
			sleep(3);
			tmsPromotionPage.publishPromotion(promotionTypeTitle);
		
			report.startStep("Logout of TMS");	
			sleep(1);
			tmsHomePage.clickOnExit();
			testResultService.assertEquals(false, loginPage.isSubmitButtonEnabled(), "Checking that Login Page displayed");

			report.startStep("Restart browser and wait 60 sec till promotion will uploaded");
				pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(configuration.getProperty("student.user.name"),instID));
				pageHelper.restartBrowserInNewURL(institutionName,false);


			report.startStep("Login to ED and select new slide");
				loginPage.loginAsStudent(configuration.getProperty("student.user.name"),configuration.getProperty("student.user.password"));
				homePage.closeAllNotifications();
				homePage.waitHomePageloaded();
				homePage.closeModalPopUp();
				//homePage.selectPromoSlideByNumber(-1); //the latest item, the max number


			report.startStep("Inspect new slide exists with right elements");
				webDriver.scrollToBottomOfPage();
				boolean foundedPromotion = homePage.findWantedPromotion(promotionTypeTitle);
				//sleep(4);
				if(foundedPromotion) {
					homePage.verifyPromotionSlideTemplate(promotionTypeTitle, promotionSubTitle, promotionText, promotionBtnText);
				}else {
					testResultService.addFailTest("Promotion not displayed on ED",true,true);
				}
				homePage.clickOnLogOut();
			
			report.startStep("Log in to TMS");
			pageHelper.restartBrowserInNewURL(institutionName,false);
			//webDriver.getWebDriver().getPageSource();
			tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
			homePage.closeAllNotifications();
			tmsHomePage.waitForPageToLoad();

			report.startStep("Navigate to Settings > Promotion Area");
			tmsPromotionPage.goToPromotionArea();
		
			report.startStep("Delete New Slide");
			tmsPromotionPage.deletePromotion(promotionTypeTitle);
			
			report.startStep("Validate New Slide is deleted");
			isDeleted = tmsPromotionPage.verifyPromotionIsNotDisplayedInTheTable(promotionTypeTitle);
			
			report.startStep("Logout of TMS");
			tmsHomePage.clickOnExit();
			
		} catch (Exception e) {
			
		} finally {
			
			if (!isDeleted) {
				
				report.startStep("Close browser and open again");
				webDriver.quitBrowser();
				sleep(2);
				webDriver.init();
				sleep(2);
				webDriver.maximize();
				//sleep(2);
			
				report.startStep("Login again with the same user");
				webDriver.openUrl(pageHelper.CILink);
				
				report.startStep("Login as Admin");
				tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
				homePage.closeAllNotifications();
				tmsHomePage.waitForPageToLoad();
				tmsPromotionPage = new TMSPromotionPage(webDriver, testResultService);
				
				// Go to Promotion Area
				tmsPromotionPage.goToPromotionArea();
				
				report.startStep("Delete the Promotion");
				sleep(2);
				tmsPromotionPage.deletePromotion(promotionTypeTitle);
				sleep(1);
				
				report.startStep("Verify the Promotion is Deleted");
				tmsPromotionPage.verifyPromotionIsNotDisplayedInTheTable(promotionTypeTitle);
				
				report.startStep("Logout of TMS");
				tmsHomePage.clickOnExitTMS();
				sleep(2);
			}	
			
		}
	}
	
	/*private void populatePromotionSlideValue(String field, String Text) throws Exception {

		webDriver.waitForElement("//*[@id='" + field + "']", ByTypes.xpath).clear();
		webDriver.waitForElement("//*[@id='" + field + "']", ByTypes.xpath).sendKeys(Text);
	}*/

//--igb 2018.07.12 --> add new test ------------------------------
	private boolean checkComboOption(String comboName, String optName) throws Exception {
		
		List<WebElement> comboOptions = webDriver.waitForElement("//*[@id='" + comboName + "']", ByTypes.xpath).findElements(By.tagName( "option"));

		for (int i = 0; i < comboOptions.size(); i++) {
			if(comboOptions.get(i).getText().equalsIgnoreCase(optName))
				return true;
		}

		return false;
	}
//--igb 2018.07.12 --> add new test -----------	
	
	@After
	public void tearDown() throws Exception {
		studentId = dbService.getAdminIdByInstId(configuration.getProperty("local_institutionId"));
		
		if (setCommunityToGlobal){
			dbService.setCommunicationModeForInstitution(configuration.getProperty("institution.id"), "2");
		}
		
		if (returnStudyPlanner) {
			String userID = dbService.getUserIdByUserName(configuration.getProperty("student.user.name"), configuration.getProperty("institution.id"));
			dbService.setUserStudyPlanSettingsForUser(userID, "3");
		}
		
		if (languageSupportLevelReset)
		{
			String classId = dbService.getClassIdByName(configuration.getProperty("classname.progress"), institutionId);
			dbService.updateClassLanguageSupportLevel(classId,"0");
		}
		
		if (resetMyProfileSettings)
		{
			dbService.setInstitutionMyProfileGroupId(institutionId, "2");
		}
		
		super.tearDown();
	}	
}
