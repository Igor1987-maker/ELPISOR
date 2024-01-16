package tests.edo.newux;

import java.util.List;

import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.WebElement;

import Enums.ByTypes;
import Interfaces.TestCaseParams;
import pageObjects.edo.NewUxAssessmentsPage;
import pageObjects.edo.NewUxAssignmentsPage;
import pageObjects.edo.NewUxCommunityPage;
import pageObjects.edo.NewUxGrammarBook;
import pageObjects.edo.NewUxInstitutionPage;
import pageObjects.edo.NewUxLearningArea;
import pageObjects.edo.NewUxLearningArea2;
import pageObjects.edo.NewUxMagazinePage;
import pageObjects.edo.NewUxMessagesPage;
import pageObjects.edo.NewUxMyProgressPage;
import testCategories.AngularLearningArea;
import testCategories.NonAngularLearningArea;
import testCategories.reg2;
import testCategories.edoNewUX.CourseArea;
import testCategories.edoNewUX.UserData;


@Category(reg2.class)
public class CarouselAndNavBarTests extends BasicNewUxTest {

	NewUxInstitutionPage ipage;
	NewUxCommunityPage communityPage;
	NewUxAssignmentsPage assignmentsPage;
	NewUxAssessmentsPage testPage;
	NewUxMessagesPage inboxPage;
	NewUxGrammarBook grammarBook;
	NewUxLearningArea2 learningArea2;
	NewUxLearningArea learningArea;
	NewUxMagazinePage magazinePage;
	testDataValidation dataFactory;
	NewUxMyProgressPage myProgress;
	
	
	//private static final String CI_Folder = "smb://CI-SRV//ApplicationEnvironments//";
	//private static final String CI_Folder = "smb://CI-SRV//BuildsArtifacts//";
	private static final String CI_Folder = "smb://CI//BuildsArtifacts//";
	
	@Before
	public void setup() throws Exception {
		super.setup();
	}

	@Test
	@Category(CourseArea.class)
	@TestCaseParams(testCaseID = { "19135" }, testMultiple = true)
	public void CaruselNavigation() throws Exception {
		
		report.startStep("Create user and login");
		createUserAndLoginNewUXClass();
		homePage.closeAllNotifications();
		
		report.startStep("Navigate next and check course name");
		homePage.carouselNavigateNext();
		sleep(1);

		boolean courseNameMatch = homePage.waitForExpectedCourseName("Basic 1",
				2);
		testResultService.assertEquals(true, courseNameMatch,
				"Course name do not match");

		report.startStep("Check if there are broken images");
		webDriver.checkForBrokenImages();
		report.startStep("Navigate back and check course name");
		homePage.carouselNavigateBack();


		courseNameMatch = homePage.waitForExpectedCourseName(
				"First Discoveries", 1);
		testResultService.assertEquals(true, courseNameMatch,
				"Course name do not match");
/*
		report.startStep("Set progress to first FD course item");
		studentService.createSingleProgressRecored(studentId,"20000", "31515", 10, false, 1);
*/		
	}

	@Test
	@Category(CourseArea.class)
	@TestCaseParams(testCaseID = { "20035", "21273" }, testMultiple = true)
	public void testNavigationBar() throws Exception {

		report.startStep("Get user and login");
		getUserAndLoginNewUXClass();
		
		report.startStep("Check that the nav bar is open");
		IsNavBarOpen();

		// homePage.clickToOpenNavigationBar();
		// homePage.getNavigationBarStatus();

		/*Hayk - Was removed temporarily from the release - Should be returned later when the feature is reactivated
		String notif = homePage.getNavBarItemsNotification("5");
		testResultService.assertEquals("56", notif,
				"Number of notifications not found");*/
		
		
		report.startStep("Check all menus enabled");
		int numOfMenus = 7;
		for (int i = 1; i <= numOfMenus; i++) {
			checkffNavBarItemEnabled(i);
		}
		report.startStep("close the nav bar");
		homePage.clickToOpenNavigationBar();
		IsNavBarClosed();

	}
	
	@Test
	@TestCaseParams(testCaseID = { "45593" }, testMultiple = true)
	public void testCourseLabelCEFR() throws Exception {

		//String instID = institutionId; //autoInstitution.getInstitutionId();
		String currentLevel = "undefined";
		String currentCourse = "undefined";
		
		report.startStep("Get user and login");
		homePage = getUserAndLoginNewUXClass();
	//	homePage.closeAllNotifications();
	//	homePage.waitHomePageloadedFully();
						
		report.startStep("Check NO CEFR indication");
		currentLevel = homePage.getCurrentCourseLabelCEFR();
		testResultService.assertEquals("empty", currentLevel, "CEFR LEVEL displayed though should not");
		
		report.startStep("Logout");
		homePage.clickOnLogOut();
		
		report.startStep("Set show CEFR, refresh and login");
		try {
			dbService.checkAndturnOnPropertyIdFlag(institutionId,"14","1");
			pageHelper.setInstitutionSupportCEFR(institutionId, true);
			webDriver.deleteCookiesAndRefresh();
			pageHelper.restartBrowserSameUrl();
			
			homePage.waitToLoginArea();
			loginAsStudent(studentId);
			homePage.closeAllNotifications();
			homePage.waitHomePageloadedFully();
			
			report.startStep("Check Courses have corresponding CEFR labels");
			dataFactory = new testDataValidation();
			
			JSONObject map = dataFactory.getCefrMapAsJson();
							
			for (int i = 0; i < coursesNames.length; i++) {
							
				currentCourse = coursesNames[i];
				
				homePage.navigateToRequiredCourseOnHomePage(currentCourse);
				
				currentLevel = homePage.getCurrentCourseLabelCEFR();
				
				testResultService.assertEquals(true, currentLevel.equals(map.getString(currentCourse)), "Courses Carousel --- CEFR INDICATION is NOT CORRECT for COURSE: " + currentCourse, true);
			}
			
		} catch (Exception e) {
			
			testResultService.addFailTest(e.toString(), false, true);
			
		} finally {
			
			report.startStep("Set DO NOT SHOW CEFR");
			pageHelper.setInstitutionSupportCEFR(institutionId, false);
			dbService.deletePropertyFlagforInstitution(institutionId,"14");
		}

	}
	
	@Test
	@Category(UserData.class)
	@TestCaseParams(testCaseID = { "19966" }, testMultiple = true, skippedBrowsers = { "safariMac" })
	
	public void testUserData() throws Exception {
		report.startStep("Get user and login");
		homePage = getUserAndLoginNewUXClass();
		homePage.waitHomePageloaded();
		sleep(3);
		
		report.startStep("Check student name");
		testResultService.assertEquals(
				"Hello " + dbService.getUserFirstNameByUserId(studentId),
				homePage.getUserDataText(), "User data not found");
	}
	
	@Test
	@TestCaseParams(testCaseID = { "36869" })
	public void testCustomPromotionSlideED() throws Exception {
		
		report.startStep("Init test data");
		String expectedDate = "08/02/2023";
		String expectedTypeTitle = "Automation Title";
		String expectedTitle = "subtitle here";
		String expectedText = "This is article for automation";
		String expectedBtnText = "Read";
		
		report.startStep("Get user and login");
		getUserAndLoginNewUXClass();
	//	homePage.closeAllNotifications();
	//	homePage.waitHomePageloaded();
		webDriver.scrollToBottomOfPage();
		sleep(1);
		
		//for (int i=1;i<=2;i++){
			
			report.startStep("Verify magazine custom slide and select magazine custom slide");
			int requiredSlide = homePage.verifyMagazineSlideTemplate(expectedDate, expectedTypeTitle, expectedTitle, expectedText, expectedBtnText);
			
			report.startStep("Verify magazine button link");
			homePage.selectPromoSlideByNumber(requiredSlide);
			homePage.clickCurrentPromoSlideButton();
			
			//homePage.getCurrentPromoSlideButton().click();
			homePage.verifyEdusoftSiteOpenNewTab();
		//}
	}
	
	@Test
	@TestCaseParams(testCaseID = { "21438" })
	public void testPromoNavigation() throws Exception {
		
				
		report.startStep("Get user and login");
		int articlesNum = 3;
		getUserAndLoginNewUXClass();
		homePage.closeAllNotifications();
		webDriver.scrollToBottomOfPage();
	//	sleep(3);
		
		report.startStep("Navigate between promo slides and check articles titles");
		List<String[]> articlesSet = pageHelper.getLatestMagazineArticlesFromStaticDB("Promotion");
		
		for (int i = 0; i < articlesNum; i++) {
			
			int aNumber = i+1;
			
			homePage.selectPromoSlideByNumber(aNumber);
			report.addTitle("Clicked on Promotion number: " + aNumber);
			sleep(2);
			String actualTitle = homePage.getCurrentMagazineArticleTitle();
			String expectedTitle = "";
			for (int j =0; j < 3; j++) {
				expectedTitle = articlesSet.get(j)[0].trim();
				if (expectedTitle.equals(actualTitle)) {
					break;
				}
			}
		//	testResultService.assertEquals(expectedTitle, actualTitle, "Magazine title is not valid in article:" + aNumber);
		}
		
		
	}
	
	@Test
	@TestCaseParams(testCaseID = { "21277" })
	public void testNavBarHomeButtonFunctionality() throws Exception {
		
		report.startStep("Get user and login");
			getUserAndLoginNewUXClass();
		
		report.startStep("Check that nav bar is open");
			IsNavBarOpen();
			homePage.navigateToRequiredCourseOnHomePage("First Discoveries");
		
		report.startStep("Click on lesson");
			homePage.clickOnUnitLessons(1);
			sleep(1);
			learningArea2 = homePage.clickOnLesson2(0, 1);
			sleep(1);
			learningArea2.waitToLearningAreaLoaded();

		report.startStep("Check that nav bar is closed");
			IsNavBarClosed2();

		report.startStep("Click the Home button");
		
			NewUxLearningArea learningArea = new NewUxLearningArea(webDriver, testResultService);
		//	learningArea.clickToOpenNavigationBar();
			learningArea.clickOnHomeButton();
		
	}
	
	@Test
	@Category(NonAngularLearningArea.class)
	@TestCaseParams(testCaseID = { "33607", "37953" }, skippedBrowsers = {"firefox"}) // Actions API not supported by geckodriver
	public void testNavBarNavigationByTooltip() throws Exception {
		
		int iteration = 0;
		int iterationCount = 6;
		
		report.startStep("Get user and login");
		getUserAndLoginNewUXClass();
		
		report.startStep("Check that nav bar is open");
		IsNavBarOpen();
		
		while (iteration<=iterationCount) {
		
		report.startStep("Mouse hover on Home and check tooltip text");
		if (iteration == 0) checkIfNavBarItemCurrent("1");
		homePage.hoverOnNavBarItemAndVerivyTooltip("sitemenu__itemHome");
						
		report.startStep("Click on Institution Page tooltip and check action done");
			if (iteration == 6) learningArea.clickToOpenNavigationBar();
			ipage = homePage.openInstitutionPage(true);
			sleep(2);
			ipage.close();
		
		report.startStep("Click on Community Page tooltip and check action done");
		if (iteration != 3 && iteration != 4 && iteration != 5) {
			communityPage = homePage.openNewCommunityPage(true);
			communityPage.VerifyCommunityPagesTitle();
			/*sleep(1);
			webDriver.getWebDriver().navigate().back();*/
		}
		
		if (iteration == 0) {
			
			homePage.clickOnHomeButton();
		}
		
		if (iteration == 1) {
			
			homePage.clickOnHomeButton();
			homePage.waitHomePageloaded();
			homePage.clickOnMyProgress();
			homePage.clickToOpenNavigationBar();
		}
		
		if (iteration == 6) {
			
			homePage.clickOnHomeButton();
			homePage.waitHomePageloaded();
			homePage.clickOnContinueButton();
			sleep(2);
			homePage.clickToOpenNavigationBar();
		}
		sleep(1);
						
		report.startStep("Click on Assignments Page tooltip and check action done");
		assignmentsPage = homePage.openAssignmentsPage(true);
		sleep(2);
		assignmentsPage.close();
		
		report.startStep("Click on Assessments Page tooltip and check action done");
		testPage = homePage.openAssessmentsPage(true);
		testPage.verifyAssessmentsPageHeader();
		//sleep(2);
		testPage.close();
		
		report.startStep("Click on Inbox Page tooltip and check action done");
		inboxPage = homePage.openInboxPage(true);
		sleep(2);
		inboxPage.switchToInboxFrame();
		inboxPage.verifyInboxPageTitle();
		inboxPage.close();
		
		report.startStep("Click on Grammar Book Page tooltip and check action done");
		grammarBook = homePage.openGrammarBookPage(true);
		grammarBook.clickOnStartLearning();
		grammarBook.verifyGrammarBookHeader();
		sleep(2);
		grammarBook.close();
				
		sleep(1);
		
		
			if (iteration == 0) {
			
				report.startStep("Click on My Progress Page and check action done");
				homePage.clickOnMyProgress();	
				homePage.clickToOpenNavigationBar();
			}
		
			if (iteration == 1) {
				
				report.startStep("Click on Community Page and check action done");
				homePage.hoverOnNavBarItemAndVerivyTooltip("sitemenu__itemCommunity");
				homePage.clickButtonTooltipOnNavBar("sitemenu__itemCommunity");	
				
				}
			
			if (iteration == 2) {
				
				report.startStep("Click on Magazine and check action done");
				magazinePage = communityPage.openMagazinePage();	
				
				}

			if (iteration == 3) {
	
				report.startStep("Click on Talking Idioms and check action done");
				magazinePage.breadCrumbs_CommunityLink.click();
				communityPage.openIdiomsPage();	
	
			}
			
			if (iteration == 4) {
				
				report.startStep("Click on Games and check action done");
				magazinePage.breadCrumbs_CommunityLink.click();
				communityPage.openGamesPage();	
	
			}
			
			if (iteration == 5) {
				
				report.startStep("Navigate to Learning Area");
				
				homePage.clickOnHomeButton();
				homePage.waitHomePageloaded();
				learningArea = homePage.clickOnContinueButton();
				sleep(2);
				//learningArea.clickToOpenNavigationBar();
				
			}
			
			if (iteration == 6) {
				report.startStep("Click on Home Page tooltip and check action done");
				learningArea.clickToOpenNavigationBar();
				homePage.hoverOnNavBarItemAndVerivyTooltip("sitemenu__itemHome");
				homePage.clickButtonTooltipOnNavBar("sitemenu__itemHome");	
				homePage.getUserDataText();
			}
						
			iteration++;	
		}
		
	}
	
	
	@Test
	@Category(AngularLearningArea.class)
	@TestCaseParams(testCaseID = { "33607", "37953" }, skippedBrowsers = {"firefox"}) // Actions API not supported by geckodriver
	public void testNavBarNavigationByTooltip2() throws Exception {
		
		//int iteration = 0;
		//int iterationCount = 6;
		
		report.startStep("Get user and login");
			getUserAndLoginNewUXClass();
		
			//homePage.waitUntilLoadingMessageIsOver();
			
		report.startStep("Check Navigation bar from Home Page");
			IsNavBarOpen();
			checkIfNavBarItemCurrent("1");
			checkNavigationBar();
		
		report.startStep("Check Navigation bar from Community Page");
			
		report.startStep("Click on Community Page tooltip and check action done");
			communityPage = homePage.openNewCommunityPage(false);
			checkNavigationBar();
			homePage.clickOnHomeButton();
			homePage.waitHomePageloadedFully();	
				
		report.startStep("Check Navigation bar from My Progress Page");
			myProgress = homePage.clickOnMyProgress();	
			myProgress.waitUntilPageIsLoaded();
		
			//homePage.clickToOpenNavigationBar();
			myProgress.clickToOpenNavigationBar();
			checkNavigationBar();
			homePage.clickOnHomeButton();
			homePage.waitHomePageloadedFully();
			
		report.startStep("Enter to Learning Area and Check Navigation bar");
			learningArea2 = homePage.clickOnContinueButton2();
			learningArea2.waitToLearningAreaLoaded();
			//learningArea2.IsNavBarClosed();
			homePage.clickToOpenNavigationBar();
					
		report.startStep("Click on Assignments Page tooltip and check action done");
			assignmentsPage = homePage.openAssignmentsPage(true);
			assignmentsPage.close();
			
		report.startStep("Click on Inbox Page tooltip and check action done");
			inboxPage = homePage.openInboxPage(false);
			inboxPage.waitInboxPageLoaded();
			inboxPage.verifyInboxPageTitle();
			inboxPage.close();
		
		report.startStep("Click on Grammar Book Page tooltip and check action done");
			grammarBook = homePage.openGrammarBookPage(false);
			grammarBook.clickOnStartLearning();
			grammarBook.verifyGrammarBookHeader();
			grammarBook.close();
	
	}
	
	private void checkNavigationBar() {
		// TODO Auto-generated method stub
		
		try {
			report.startStep("Check that nav bar is open");
				//sleep(8); //wait the nav bar will open
				
			report.startStep("Mouse hover on Home and check tooltip text");			
				homePage.hoverOnNavBarItemAndVerivyTooltip("sitemenu__itemHome");
			
				WebElement element = webDriver.findElementByXpath("//button[contains(@class,'openMenuBtn')]", ByTypes.xpath);
				webDriver.hoverOnElement(element);	
			
			report.startStep("Click on Institution Page tooltip and check action done");
				ipage = homePage.openInstitutionPage(false);
				sleep(1);
				ipage.close();
				webDriver.hoverOnElement(element);		
			
			report.startStep("Verify Community Tool Tip");
				communityPage = new NewUxCommunityPage(webDriver,testResultService);
				homePage.hoverOnNavBarItemAndVerivyTooltip("sitemenu__itemCommunity");
				webDriver.hoverOnElement(element);
				
			report.startStep("Click on Assignments Page tooltip and check action done");
				assignmentsPage = homePage.openAssignmentsPage(true);
				sleep(1);
				assignmentsPage.close();
		
			report.startStep("Click on Assessments Page tooltip and check action done");
				testPage = homePage.openAssessmentsPage(true);
				sleep(1);
				testPage.verifyAssessmentsPageHeader();
				testPage.close();
				
			report.startStep("Click on Inbox Page tooltip and check action done");
				inboxPage = homePage.openInboxPage(false);
				inboxPage.waitInboxPageLoaded();
				inboxPage.verifyInboxPageTitle();
				inboxPage.close();
			
			report.startStep("Click on Grammar Book Page tooltip and check action done");
				grammarBook = homePage.openGrammarBookPage(false);
				grammarBook.clickOnStartLearning();
				grammarBook.verifyGrammarBookHeader();
				grammarBook.close();
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	private void verifyEdusoftSiteOpenNewTab() throws Exception {
		sleep(1);
		webDriver.switchToNextTab(); 
		webDriver.waitForElement("/html/body", ByTypes.xpath, false, webDriver.getWindowHeight()); 
		
		String currentUrl = webDriver.waitForSpecificCurrentUrl("", "edusoft");
		
		String fullUrl[] = currentUrl.split("/");
		String Corpurl[] = CorpUrl.split("/"); 
		webDriver.closeNewTab(2);
		webDriver.switchToMainWindow();
		testResultService.assertEquals(Corpurl[2], fullUrl[2],	"Edusoft site is not displayed");
		
	}
	*/
	private void IsNavBarOpen() throws Exception {
		boolean isNavBarOpen = homePage.isNavBarOpen();
		testResultService.assertEquals(true, isNavBarOpen,"Nav bar is not dislayed");
	}

	private void IsNavBarClosed() throws Exception {
		boolean isNavBarClosed = homePage.isNavBarClosed();
		testResultService.assertEquals(true, isNavBarClosed,"Nav bar is not closed");
	}
	
	private void IsNavBarClosed2() throws Exception {
		boolean isNavBarClosed = homePage.isNavBarClosed();
		testResultService.assertEquals(false, isNavBarClosed,
				"Nav bar is not closed");
	}

	private void checkIfNavBarItemAlerted(String id) throws Exception {
		boolean isEnabled = homePage.isNavBarItemAlerted(id);
		testResultService.assertEquals(true, isEnabled,
				"Nav bar item was not alerted");

	}
	
	private void checkIfNavBarItemCurrent(String id) throws Exception {
		boolean isCurrent = homePage.isNavBarItemCurrent(id);
		testResultService.assertEquals(true, isCurrent,	"Nav bar item was not in current state");

	}

	private void checkffNavBarItemEnabled(int id) throws Exception {
		String num = String.valueOf(id);
		boolean isEnabled = homePage.isNavBarItemEnabled(num);
		testResultService.assertEquals(true, isEnabled,
				"Nav bar item was not disabled");
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
		report.startStep("Set DO NOT SHOW CEFR");
		pageHelper.setInstitutionSupportCEFR(institutionId, false);
		dbService.deletePropertyFlagforInstitution(institutionId,"14");
	}

}



