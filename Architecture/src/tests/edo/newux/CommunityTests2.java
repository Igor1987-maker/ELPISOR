package tests.edo.newux;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.WebElement;

import Enums.ByTypes;
import Enums.CommunityActivities;
import Enums.Games;
import Enums.Levels;
import Enums.MagazineTopics;
import Enums.NavBarItems;
import Interfaces.TestCaseParams;
import pageObjects.edo.NewUxCommunityPage;
import pageObjects.edo.NewUxGamesPage;
import pageObjects.edo.NewUxLearningArea;
import pageObjects.edo.NewUxMagazineArticlePage;
import pageObjects.edo.NewUxMagazinePage;
import pageObjects.edo.NewUxMyProfile;
import pageObjects.edo.NewUxTalkIdiomsPage;
import services.DbService;
import testCategories.AngularLearningArea;
import testCategories.NonAngularLearningArea;
import testCategories.inProgressTests;
import testCategories.reg2;

@Category(AngularLearningArea.class)
public class CommunityTests2 extends BasicNewUxTest {

	protected NewUxCommunityPage communityPage;
	protected NewUxLearningArea learningArea;
	protected NewUxMagazinePage magazinePage;
	protected NewUxGamesPage gamesPage;
	protected NewUxTalkIdiomsPage idiomsPage;
	protected NewUxMagazineArticlePage articlePage;
	protected NewUxMyProfile myProfile;
	protected List<String[]> errorListOnTopicPreview;
	protected List<String[]> errorListOnArticlePage;
	private static final String firstIdiom_Basic = "a couch potato";
	private static final String explanation_FirstIdiom_Basic = "someone who spends a lot of time watching TV";
	private static final String example_FirstIdiom_Basic = "Paul is such a couch potato. He never does any exercise.";
	private static final String lastIdiom_Basic = "win-win";
	private static final String firstIdiom_Intermediate = "a tall order";
	private static final String lastIdiom_Intermediate = "under the weather";
	private static final String firstIdiom_Advanced = "ask for the moon";
	private static final String lastIdiom_Advanced = "work your fingers to the bone";
	
	private static final String explanation_Piece_Of_Cake = "something very easy to do";
	private static final String example_Piece_Of_Cake = "Learning English is a piece of cake. It\'s so easy!";
	private static final String audio_file_Piece_Of_Cake = "Idioms/sound/ID__AB002.mp3";

	
	@Before
	public void setup() throws Exception {
		super.setup();
		
		/*
		institutionName = institutionsName[20]; // qa31
		pageHelper.initializeData();
		pageHelper.restartBrowserInNewURL(institutionName, true);
		*/
		
		report.startStep("Reset My Profile settings to update");
		//institutionId = dbService.getInstituteIdByName(institutionName);
		pageHelper.updateInstitutionMyProfileSettings(institutionId, "2");
		report.startStep("Get user and login");
		getUserAndLoginNewUXClass();
		communityPage = new NewUxCommunityPage(webDriver, testResultService);
		pageHelper.checkAndSetCurrentstaticContentConnectionString(true);
		report.finishStep();
	}

	

	@Test
	@TestCaseParams(testCaseID = { "36994" }) 
	public void testNewCommunityBtn_BasicFunctionality() throws Exception {

	
		report.startStep("Open New Community page and verify title");
		testResultService.assertEquals(false, homePage.isNavBarItemCurrent("8"), "Community btn not in normal state before open");
		communityPage = homePage.openNewCommunityPage(false);
		communityPage.VerifyCommunityPagesTitle();
		
		report.startStep("Check that Community btn disabled & current state");
		testResultService.assertEquals(true, homePage.isNavBarItemCurrent("3"), "Community btn not in current state");
		sleep(1);
		
		report.startStep("Go to Home and check that Community btn enabled & normal state");
		
		homePage.clickOnHomeButton(true);
		sleep(2);
		testResultService.assertEquals(false, homePage.isNavBarItemCurrent("3"), "Community btn not in normal state");
					
		report.startStep("Navigate to Learning Area and verify Community Btn Not Displayed");
		homePage.clickOnContinueButton();
		sleep(3);
		homePage.clickToOpenNavigationBar();
		homePage.checkIfNavBarItemDisplayed(false, NavBarItems.sitemenu__itemCommunity);
						
	}
	
	@Test
	@TestCaseParams(testCaseID = { "36993" })
	public void testNewCommunityFrontPage() throws Exception {
						
		report.startStep("Open New Community page and verify title");
		sleep(1);
		communityPage = homePage.openNewCommunityPage(false);
		communityPage.VerifyCommunityPagesTitle();

		report.startStep("Verify New Community banner");
		communityPage.verifyNewCommunityBanner();
		
		report.startStep("Verify Community blocks elements");
		communityPage.verifyNewCommunityFrontPageBlocks(CommunityActivities.forum);	
		communityPage.verifyNewCommunityFrontPageBlocks(CommunityActivities.magazine);
		communityPage.verifyNewCommunityFrontPageBlocks(CommunityActivities.games);
		communityPage.verifyNewCommunityFrontPageBlocks(CommunityActivities.idioms);	
			
		
	}
	
	@Test
	@TestCaseParams(testCaseID = { "37019" })
	public void testNewMagazineFrontPage() throws Exception {
		
		report.startStep("Open My Profile and select Intermediate level for Community");
		changeCommunitySiteLevel(Levels.Intermediate);
				
		report.startStep("Open New Community page and press on Magazine link");
		sleep(1);
		communityPage = homePage.openNewCommunityPage(false);
		magazinePage = communityPage.openMagazinePage();
		
		report.startStep("Verify level selected as set in My Profile");
		magazinePage.verifyCommunityLevelSelected(Levels.Intermediate);
		
		report.startStep("Verify Magazine page title");
		magazinePage.verifyMagazineMainTitle();
		
		report.startStep("Verify Magazine page issue month");
		magazinePage.verifyMagazineCurrentIssueMonth();
		
		report.startStep("Get Latest Articles from DB");
		List<String[]> articlesSet = pageHelper.getLatestMagazineArticlesFromStaticDB();
						
		report.startStep("Mouse hover on articles and verify highlighed elements");
		magazinePage.verifyArticlesHighlightedOnHover();
		sleep(1);
		
		report.startStep("Verify articles preview of Intermediate level");
		magazinePage.verifyMagazineArticlePreviewByLevel(Levels.Intermediate, articlesSet);
		magazinePage.verifyMainFeatureDisplayedFirst(articlesSet); 
		webDriver.scrollToTopOfPage();
						
		report.startStep("Click on Basic level and verify articles preview");
		magazinePage.clickOnLevel(Levels.Basic);
		sleep(1);
		magazinePage.verifyMagazineArticlePreviewByLevel(Levels.Basic, articlesSet);
		magazinePage.verifyMainFeatureDisplayedFirst(articlesSet); 
		webDriver.scrollToTopOfPage();
		
		report.startStep("Click on Advanced level and verify articles preview");
		magazinePage.clickOnLevel(Levels.Advanced);
		sleep(1);
		magazinePage.verifyMagazineArticlePreviewByLevel(Levels.Advanced, articlesSet);
		magazinePage.verifyMainFeatureDisplayedFirst(articlesSet); 
						
		
	}
	
	@Test
	@TestCaseParams(testCaseID = { "38490" })
	public void testNewGamesFrontPage() throws Exception {
		
		report.startStep("Open My Profile and select Intermediate level for Community");
		changeCommunitySiteLevel(Levels.Intermediate);
				
		report.startStep("Open New Community page and press on Games link");
		sleep(1);
		communityPage = homePage.openNewCommunityPage(false);
		gamesPage = communityPage.openGamesPage();
		
		report.startStep("Verify breadcrumbs links on Games front page");
		testResultService.assertEquals(true, gamesPage.breadCrumbs_CommunityLink.isDisplayed(), "Comminity link not displayed");
		testResultService.assertEquals(gamesPage.gamesMainTitle.getText(), gamesPage.breadCrumbs_Elements.get(1).getText(), "Games current breadcrumb label not valid");
				
		report.startStep("Verify level selected as set in My Profile");
		gamesPage.verifyCommunityLevelSelected(Levels.Intermediate);
		
		report.startStep("Verify Games page title");
		gamesPage.verifyGamesMainTitle();
								
		report.startStep("Mouse hover on Games and verify highlighed elements");
		gamesPage.verifyGamesHighlightedOnHover();
		
		report.startStep("Verify Games elements present");
		gamesPage.verifyGamesElements(Games.crosswords);
		gamesPage.verifyGamesElements(Games.snowman);
		gamesPage.verifyGamesElements(Games.wordSearch);
		
		report.startStep("Click on Game and check it loaded");
		gamesPage.clickOnGameAndCheckItLoaded(Games.crosswords);
		gamesPage.breadCrumbs_Games.click();
		gamesPage.clickOnGameAndCheckItLoaded(Games.snowman);
		gamesPage.breadCrumbs_Games.click();
		gamesPage.clickOnGameAndCheckItLoaded(Games.wordSearch);
		
		
	}
	
	@Test
	@TestCaseParams(testCaseID = { "37433" })
	public void testNewCommunityForumPage() throws Exception {
				
		report.startStep("Open New Community page and press on Magazine link");
		sleep(1);
		communityPage = homePage.openNewCommunityPage(false);
		String mainWin = communityPage.openForumPage();
		
		report.startStep("Verify forum page elements");
		communityPage.ForumPageContentVerification();
		
		report.startStep("Verify no breadcrumb on forum page");
		communityPage.verifyOldBreadcrumbNavigationNotDisplayed();
				
		/*report.startStep("Go to Community Front page and press Forum again");
		//webDriver.switchToMainWindow(mainWin);
		//webDriver.switchToTopMostFrame();
		//webDriver.getWebDriver().findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL, Keys.PAGE_UP);
		webDriver.switchToMainWindow(mainWin);
		communityPage.openForumPage();
		webDriver.checkNumberOfTabsOpened(2);
		communityPage.ForumPageContentVerification();*/ //ToDo stopped working - cannot switch to previous tab   
		
		report.startStep("Click on any discussion link");
		communityPage.clickOnForumCommunityDiscussionByPartialLink("Business");
		sleep(2);
		
		report.startStep("Verify Add Message btn present and click Forum Home");
		testResultService.assertEquals(true, communityPage.getAddNewMessageBtn().isDisplayed(), "Add Message btn not found");
		communityPage.clickOnForumHomeLink();
		
		report.startStep("Verify forum page elements");
		communityPage.ForumPageContentVerification();
		
		
	}
	
	@Test
	@TestCaseParams(testCaseID = { "37272" })
	public void testNewMagazineArchivePage() throws Exception {
			
		report.startStep("Open My Profile and select Basic level for Community");
		changeCommunitySiteLevel(Levels.Basic);
		
		report.startStep("Open New Community page and press on Magazine link");
		sleep(1);
		communityPage = homePage.openNewCommunityPage(false);
		magazinePage = communityPage.openMagazinePage();
		
		report.startStep("Click on Archive button");
		magazinePage.clickOnArchive();
		
		report.startStep("Verify current month/year selected");
		magazinePage.verifyCurrentMonthYearSelectedOnArchiveOpen();
		
		report.startStep("Verify next year arrow btn disabled");
		magazinePage.verifyRightArrowCalPickerDisabled();
		
		report.startStep("Verify future months disabled");
		magazinePage.verifyFutureMonthsInCalPickerDisabled();
		
		report.startStep("Close CalPicker by OK and open again");
		magazinePage.clickOkInArchive();
		sleep(1);
		magazinePage.clickOnArchive();
		
		report.startStep("Init test data");
		String monthToTest = "March";
		String yearToTest = "2011";
		List<String[]> articlesSet = pageHelper.getMagazineArticlesByMonthAndYearFromStaticDB(monthToTest, yearToTest);
		
		report.startStep("Select year & month and press OK");
		magazinePage.selectMonthAndYearInArchive(monthToTest, yearToTest, true);
		sleep(2);
						
		report.startStep("Verify level selected as set in My Profile");
		magazinePage.verifyCommunityLevelSelected(Levels.Basic);
		
		report.startStep("Verify Magazine page issue month");
		magazinePage.verifyArchiveMagazineIssueMonth(monthToTest, yearToTest);
		
		report.startStep("Verify articles preview of Basic level");
		magazinePage.verifyMagazineArticlePreviewByLevel(Levels.Basic, articlesSet);
		
		report.startStep("Select another issue in CalPicker");
		magazinePage.clickOnArchive();
		
		report.startStep("Update test data");
		monthToTest = "August";
		yearToTest = "2003";
		articlesSet = pageHelper.getMagazineArticlesByMonthAndYearFromStaticDB(monthToTest, yearToTest);
		
		report.startStep("Select year & month and press OK");
		magazinePage.selectMonthAndYearInArchive(monthToTest, yearToTest, true);
		sleep(2);
				
		report.startStep("Verify level selected as set in My Profile");
		magazinePage.verifyCommunityLevelSelected(Levels.Basic);
		
		report.startStep("Verify Magazine page issue month");
		magazinePage.verifyArchiveMagazineIssueMonth(monthToTest, yearToTest);
		
		report.startStep("Click on Advanced level");
		magazinePage.clickOnLevel(Levels.Advanced);
		sleep(1);
		
		report.startStep("Verify articles preview of Basic level");
		magazinePage.verifyMagazineArticlePreviewByLevel(Levels.Advanced, articlesSet);
				
		
	}
	
	@Test
	@TestCaseParams(testCaseID = { "37349" })
	public void testNewMagazineTopicsPage() throws Exception {
			
		report.startStep("Open My Profile and select Basic level for Community");
		changeCommunitySiteLevel(Levels.Basic);
		
		report.startStep("Open New Community page and press on Magazine link");
		sleep(1);
		communityPage = homePage.openNewCommunityPage(false);
		magazinePage = communityPage.openMagazinePage();
		sleep(1);
		
		report.startStep("Click on Topic button and verify 10 topics exist");
		List<WebElement> allTopics = magazinePage.clickOnTopics();
		sleep(2);
		testResultService.assertEquals(10, allTopics.size(), "The number of Topics is not 10");
		
		report.startStep("Click on any topic and verify title on topic page");
		String expectedTopic = magazinePage.clickOnSpecificTopic(MagazineTopics.Business);
		sleep(3);
		magazinePage.verifyTopicTitle(expectedTopic);
		
		report.startStep("Verify level selected as set in My Profile");
		magazinePage.verifyCommunityLevelSelected(Levels.Basic);
		
		report.startStep("Verify all displayed articles of topic have default elements");
		List<WebElement> articlesOnPage = magazinePage.getArticlesOnPage();
		
		for (int i = 0; i < articlesOnPage.size(); i++) {
			magazinePage.verifyArticleElementsDisplayed(articlesOnPage.get(i));
		}
		
		report.startStep("Verify articles sorted by issue date");
		magazinePage.verifyIssuesSortedByDateInTopics(articlesOnPage);
		
		report.startStep("Verify only 15 loaded");
		testResultService.assertEquals(15, articlesOnPage.size(), "Less or more then 15 articles displayed on page");
		
		report.startStep("Click on Show More and verify 15 issues added");
		
		int multiplier = 15;
		int totalArticles = magazinePage.getNumberOfIssuesInTopic();
		int timesToClickToEnd = totalArticles / multiplier;
		int expectedNumber = 0;
		int actualNumber = 0;
		
		for (int i = 0; i < timesToClickToEnd-1; i++) {
			magazinePage.clickOnReadMoreLinkInTopic();
			sleep(3);
			articlesOnPage = magazinePage.getArticlesOnPage();
			actualNumber = articlesOnPage.size();
			
			if (actualNumber < totalArticles) {
				expectedNumber = (i+2) * multiplier;
			} else if (actualNumber == totalArticles) {
				expectedNumber = totalArticles;
				}
			
			testResultService.assertEquals(expectedNumber, actualNumber, "Number of articles on page is wrong");
			sleep(1);
		}
		
		
		
		report.startStep("Click on another Level and verify preview text changed");
		
		String [] before = new String [articlesOnPage.size()];
		String [] after = new String [articlesOnPage.size()];
		
		for (int i = 0; i < articlesOnPage.size(); i++) {
			before [i] = magazinePage.getArticlePreviewText(articlesOnPage.get(i)).getText();
		}
		
		webDriver.scrollToTopOfPage();
		magazinePage.clickOnLevel(Levels.Advanced);
		sleep(4);
		articlesOnPage = magazinePage.getArticlesOnPage();
		
		for (int i = 0; i < articlesOnPage.size(); i++) {
			after [i] = magazinePage.getArticlePreviewText(articlesOnPage.get(i)).getText();
			if (before[i].equals(after[i])) testResultService.addFailTest("Preview text not changed", false, false);
		}
		
		report.startStep("Select another topic and verify displayed in current level");
		magazinePage.clickOnTopics();
		expectedTopic = magazinePage.clickOnSpecificTopic(MagazineTopics.Health);
		sleep(1);
		magazinePage.verifyTopicTitle(expectedTopic);
		magazinePage.verifyCommunityLevelSelected(Levels.Advanced);
				
		
	}
	
	@Test
	@TestCaseParams(testCaseID = { "37427" })
	public void testNewMagazineBreadcrumbLinks() throws Exception {
			
		report.startStep("INit test data");
		String monthToTest = "January";
		String yearToTest = "2016";
		int bIndexAfterDM = 2;
		
		report.startStep("Open New Community page and press on Magazine link");
		sleep(1);
		communityPage = homePage.openNewCommunityPage(false);
		magazinePage = communityPage.openMagazinePage();
		sleep(1);
		
		report.startStep("Verify breadcrumbs links on Magazine home page");
		testResultService.assertEquals(true, magazinePage.breadCrumbs_CommunityLink.isDisplayed(), "Comminity link not displayed");
		testResultService.assertEquals(magazinePage.getMagazineMainTitle(), magazinePage.breadCrumbs_Elements.get(1).getText(), "Magazine current breadcrumb label not valid");
		
		report.startStep("Click on Article and verify breadcrumbs link");
		clickOnArticleAndVerifyBreadcrumbLink(0, 2);
		sleep(1);
		
		report.startStep("Navigate back to community home and verify breadcrumbs links");
		navigateBackToCommunityByBreadcrumbInMagazine();
		sleep(1);
		
		report.startStep("Navigate to Archive and select any issue");
		magazinePage = communityPage.openMagazinePage();
		sleep(1);
		magazinePage.clickOnArchive();
		sleep(1);
		magazinePage.selectMonthAndYearInArchive(monthToTest, yearToTest, true);
		sleep(1);
		
		report.startStep("Verify breadcrumbs links in Archive issue");
		String archiveIssueDate = magazinePage.issueMonth.getText();
		testResultService.assertEquals(archiveIssueDate, magazinePage.breadCrumbs_Elements.get(bIndexAfterDM).getText(), "Magazine current breadcrumb label not valid");
		
		report.startStep("Click on article and verify breadcrumbs link");
		clickOnArticleAndVerifyBreadcrumbLink(1,3);
		sleep(1);

		webDriver.scrollToTopOfPage();
		
		report.startStep("Click on archive issue date and verify breadcrumbs links");
		magazinePage.breadCrumbs_Elements.get(2).click();
		sleep(1);
		
		archiveIssueDate = magazinePage.issueMonth.getText();
		testResultService.assertEquals(archiveIssueDate, magazinePage.breadCrumbs_Elements.get(bIndexAfterDM).getText(), "Magazine current breadcrumb label not valid");
				
		report.startStep("Navigate back to community home and verify breadcrumbs links");
		navigateBackToCommunityByBreadcrumbInMagazine();
		sleep(1);
				
		report.startStep("Navigate to Topic and select any topic");
		magazinePage = communityPage.openMagazinePage();
		sleep(2);
		magazinePage.clickOnTopics();
		sleep(2);
		String expectedTopic = magazinePage.clickOnSpecificTopic(MagazineTopics.Lifestyle);
		sleep(3);
		magazinePage.verifyTopicTitle(expectedTopic);
		
		report.startStep("Verify breadcrumbs links in Topic page");
		testResultService.assertEquals(expectedTopic, magazinePage.breadCrumbs_Elements.get(bIndexAfterDM).getText(), "Magazine current breadcrumb label not valid");
		
		report.startStep("Click on article and verify breadcrumbs link");
		clickOnArticleAndVerifyBreadcrumbLink(2,3);		
		sleep(1);
		
		webDriver.scrollToTopOfPage();
		
		report.startStep("Click on Topic in breadcrumb and verify breadcrumbs links");
		magazinePage.breadCrumbs_Elements.get(bIndexAfterDM).click();
		sleep(1);
		magazinePage.verifyTopicTitle(expectedTopic);
		testResultService.assertEquals(expectedTopic, magazinePage.breadCrumbs_Elements.get(bIndexAfterDM).getText(), "Magazine current breadcrumb label not valid");
		
		report.startStep("Navigate back to community home and verify breadcrumbs links");
		navigateBackToCommunityByBreadcrumbInMagazine();
		sleep(1);
		
		report.startStep("Navigate to Home Page");
		
		homePage.clickOnHomeButton();
		homePage.waitHomePageloaded();
					
		report.startStep("Click on Read in Promo area and verify magazine article breadcrumb");
		String articleToEnter = homePage.getCurrentMagazineArticleTitle();
		articlePage = homePage.StartReadingMagazinePromo();
		sleep(1);
		//testResultService.assertEquals(articleToEnter, articlePage.breadCrumbs_Elements.get(bIndexAfterDM).getText(), "Article current breadcrumb label not valid");
	
		report.startStep("Navigate back to community home and verify breadcrumbs links");
		webDriver.refresh();
		navigateBackToCommunityByBreadcrumbInMagazine();
		
	}
	
	@Test
	@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "" }, ignoreTestTimeout = true)
	public void testNewMagazineAllArticlesByTopics() throws Exception {
			
		report.startStep("INIT LEVEL TO CHECK");
		Levels level = Levels.Advanced;
		
		report.startStep("Set community site level to: " + level.toString());
		sleep(1);
		changeCommunitySiteLevel(level);
		sleep(1);
				
		report.startStep("Open New Community page and press on Magazine link");
		sleep(1);
		communityPage = homePage.openNewCommunityPage(false);
		magazinePage = communityPage.openMagazinePage();
		
		report.startStep("Click on Topics button");
		List<WebElement> topics = magazinePage.clickOnTopics();
		sleep(1);
		
		String topicName = "undefined";
		errorListOnTopicPreview = new ArrayList<>();
		errorListOnArticlePage = new ArrayList<>();	
		
		//for (int i = 0; i < topics.size(); i++) {
		
		//for (int i = 0; i < 3; i++) { // from World to Science
		//for (int i = 3; i < 6; i++) { // from Environment to Arts
		//for (int i = 6; i < 9; i++) { // from Sport to General Interest
		for (int i = 6; i <= 7; i++) { // from Sport to General Interest
		//for (int i = 9; i < 10; i++) { // Education
							
			report.startStep("Click on topic: " + (i+1));
			topicName = topics.get(i).getText();
			topics.get(i).click();
			sleep(3);
			
			checkArticlesByLevel(level, topicName, false, true);
						
			/*for (int j = 0; j < 1; j++) {
				
				if (j==0) checkArticlesByLevel(Levels.Basic, true, true);
				else if (j==1) checkArticlesByLevel(Levels.Intermediate, true, true);
				else if (j==2) checkArticlesByLevel(Levels.Advanced, true, true);
				
				webDriver.refresh();
				sleep(2);
							
			}*/
			
			
			sleep(1);
			int topicSizeToTest=7;
			//if ((i+1) < topics.size()){
			if ((i+1) <= topicSizeToTest){
				report.startStep("Open Topics list");
				topics = magazinePage.clickOnTopics();
				sleep(1); 
			}
			
		}
		
		report.startStep("Save reports with errors");
		
		if (!errorListOnTopicPreview.isEmpty()) {
			textService.writeArrayListToCSVFile("smb://" + configuration.getLogerver() + "/UXMagazineArticlesErrors/Log_Previews" + dbService.sig() + ".csv", errorListOnTopicPreview, false);
		}
		
		if (!errorListOnArticlePage.isEmpty()) {
			textService.writeArrayListToCSVFile("smb://" + configuration.getLogerver() + "/UXMagazineArticlesErrors/Log_Articles" + dbService.sig() + ".csv", errorListOnArticlePage, false);
		}
		
		errorListOnTopicPreview.clear();
		errorListOnArticlePage.clear();
		
		report.startStep("Total articles checked: " + magazinePage.articlesCount);
				
	}
	
	@Test
	@TestCaseParams(testCaseID = { "37287","37419" })
	public void testNewMagazineArticlePage() throws Exception {
				
		report.startStep("Init Test Data");
		int articleToTest = 0;
		
		report.startStep("Open My Profile and select Intermediate level for Community");
		changeCommunitySiteLevel(Levels.Intermediate);
						
		report.startStep("Open New Community page and press on Magazine link");
		homePage.waitHomePageloadedFully();
		communityPage = homePage.openNewCommunityPage(false);
		magazinePage = communityPage.openMagazinePage();
				
		report.startStep("Get Latest Articles from DB");
		List<String[]> articlesSet = pageHelper.getLatestMagazineArticlesFromStaticDB();
			
		report.startStep("Click on Read More for Main article - first from left ");
		articlePage = magazinePage.clickOnReadMoreByIndex(articleToTest);
		magazinePage.waitMagazineArticleLoaded();
		
		report.startStep("Verify article page main title");
		magazinePage.verifyMagazineMainTitle();
		
		report.startStep("Verify article page issue month");
		magazinePage.verifyMagazineCurrentIssueMonth();
				
		report.startStep("Verify article of Intermediate level");
		articlePage.verifyMainArticleByLevel(Levels.Intermediate, articlesSet);
						
		report.startStep("Click on Basic level and verify article");
		magazinePage.clickOnLevel(Levels.Basic);
		sleep(1);
		articlePage.verifyMainArticleByLevel(Levels.Basic, articlesSet);
				
		report.startStep("Click on Advanced level and verify article");
		magazinePage.clickOnLevel(Levels.Advanced);
		sleep(1);
		articlePage.verifyMainArticleByLevel(Levels.Advanced, articlesSet);
			
		report.startStep("Click on Large font and check font changed");
		articlePage.bigFontTool.click();
		sleep(1);
		articlePage.verifyLargeFontEnabled();
		articlePage.clickOnLevel(Levels.Basic);
		sleep(1);
		articlePage.verifyLargeFontEnabled();
		
		report.startStep("Check Key Word tool");
		articlePage.keyWordTool.click();
		testResultService.assertEquals(true, articlePage.isKeyWordHighlighted(), "Key words not highlighted");
		articlePage.clickOnKeyWordAndVerifyGlossary(0);
		articlePage.keyWordTool.click();
		webDriver.moveToElementAndClick(articlePage.bigFontTool);
		testResultService.assertEquals(false, articlePage.isKeyWordHighlighted(), "Key words highlighted");
		
		report.startStep("Check question functionality");
		checkPracticeInMagazine();
		sleep(2);

		report.startStep("Press Next and question functionality");
		articlePage.nextTaskArrow.click();
		sleep(2);
		checkPracticeInMagazine();	
		
		sleep(1);
		
	}
	
	@Test
	@TestCaseParams(testCaseID = { "37584","37587" })
	public void testNewTalkingIdiomsListByLevel() throws Exception {
		
		report.startStep("Open My Profile and select Intermediate level for Community");
		changeCommunitySiteLevel(Levels.Intermediate);
				
		report.startStep("Open New Community page and press on Talking Idioms link");
		sleep(1);
		
		communityPage = homePage.openNewCommunityPage(false);
		idiomsPage = communityPage.openIdiomsPage();
		sleep(1);
		webDriver.scrollToTopOfPage();
		
		report.startStep("Verify header");
		idiomsPage.verifyIdiomsHeader();
		
		report.startStep("Verify level selected as set in My Profile");
		idiomsPage.verifyCommunityLevelSelected(Levels.Intermediate);
		sleep(2);
		
		report.startStep("Verify scope of idioms for Intermediate level");
		checkIdiomListByFirstAndLast(firstIdiom_Intermediate, lastIdiom_Intermediate);
		
		report.startStep("Click on Basic level and verify idioms");
		idiomsPage.clickOnLevel(Levels.Basic);
		sleep(2);
		checkIdiomListByFirstAndLast(firstIdiom_Basic, lastIdiom_Basic);
		
		report.startStep("Click on Advanced level and verify idioms");
		idiomsPage.clickOnLevel(Levels.Advanced);
		sleep(2);
		checkIdiomListByFirstAndLast(firstIdiom_Advanced, lastIdiom_Advanced);
				
	}
	
	@Test
	@TestCaseParams(testCaseID = { "37882" })
	public void testNewTalkingIdiomsSelected() throws Exception {
		
		report.startStep("Open My Profile and select Intermediate level for Community");
		changeCommunitySiteLevel(Levels.Basic);
				
		report.startStep("Open New Community page and press on Talking Idioms link");
		sleep(1);
		communityPage = homePage.openNewCommunityPage(false);
		idiomsPage = communityPage.openIdiomsPage();
		sleep(1);
				
		report.startStep("Verify first idiom selected");
		idiomsPage.verifyIdiomSelectedByIndex(0);
		idiomsPage.verifySelectedIdiomItems(firstIdiom_Basic, explanation_FirstIdiom_Basic, example_FirstIdiom_Basic);
		
		report.startStep("Select idiom from the list and verify details in right side");
		String selectedIdiom = idiomsPage.selectIdiomByIndex(1);
		sleep(1);
		idiomsPage.verifySelectedIdiomItems(selectedIdiom, explanation_Piece_Of_Cake, example_Piece_Of_Cake);
		
		report.startStep("Verify Hear & Record btns displayed");
		testResultService.assertEquals(true, idiomsPage.hearBtn.isDisplayed(), "Hear btn not displayed");
		testResultService.assertEquals(true, idiomsPage.recordBtn.isDisplayed(), "Record btn not displayed");
		
		report.startStep("Click Hear and verify media played");
		idiomsPage.hearBtn.click();
		learningArea = new NewUxLearningArea(webDriver, testResultService);
		learningArea.checkAudioFile("IdiomsMediaPlayer", audio_file_Piece_Of_Cake);
		sleep(1);
				
		
	}
	

	private void changeCommunitySiteLevel(Levels level) throws Exception {
		
		myProfile = homePage.clickOnMyProfile();
		homePage.switchToMyProfile();
				
		sleep(1);
		myProfile.ChangeCommunitySiteLevel(level.toString());
		myProfile.clickOnUpdate();
		myProfile.close();
	}
	
	private void checkPracticeInMagazine() throws Exception {
		
	 	report.startStep("Click on See answers and check answer selected");
		articlePage.clickOnSeeAnswer();
		int correctIndex = articlePage.verifyAnswerRadioSelectedCorrect();
		
		report.startStep("Click on Clear and check all answers unselected");
		articlePage.clickOnClearAnswer();
		articlePage.verifyAnswerRadioUnselected();
		
		report.startStep("Select correct answer");
		articlePage.selectPracticeAnswerByIndex(correctIndex);
		
		report.startStep("Click on Check Answer and check V/X signs placed correctly");
		articlePage.clickOnCheckAnswer();
		articlePage.verifyAnswerRadioMark(correctIndex, true);	
		
		report.startStep("Click on Clear and check all answers unselected");
		articlePage.clickOnClearAnswer();
		articlePage.verifyAnswerRadioUnselected();
		
		report.startStep("Select wrong answer");
		int size = articlePage.practiceAnswers.size();
		int wrongIndex = 0;
		int random = 0;
		
		for (int i = 0; i < size; i++) {
			random = new Random().nextInt(size);
			if (random != correctIndex) wrongIndex = random;
			
		}
		articlePage.selectPracticeAnswerByIndex(wrongIndex);
		
		report.startStep("Click on Check Answer and check V/X signs placed correctly");
		articlePage.clickOnCheckAnswer();
		articlePage.verifyAnswerRadioMark(wrongIndex, false);	
		
		report.startStep("Click on Clear and check all answers unselected");
		articlePage.clickOnClearAnswer();
		articlePage.verifyAnswerRadioUnselected();	
	}
	
	private void checkArticlesByLevel(Levels level, String topicName, Boolean checkArticlesPreview, Boolean enterEachArticle) throws Exception {
		
		/*report.startStep("Click on "+level+" level");
		magazinePage.clickOnLevel(level); 
		webDriver.sleep(1);
		*/
				
		
		report.startStep("Click on Read More to open all articles");
		magazinePage.clickOnReadMoreLinkInTopic();
		Thread.sleep(1000);
		
		webDriver.scrollToTopOfPage();
		Thread.sleep(1000);
		
		report.startStep("Check articles of topic: " + topicName);
		
		if (checkArticlesPreview)
			errorListOnTopicPreview = magazinePage.verifyArticlesPreviewInTopicByLevel(level, topicName, errorListOnTopicPreview);
		if (enterEachArticle)
			errorListOnArticlePage = magazinePage.verifyEveryArticlePageInTopicByLevel(level, topicName, errorListOnArticlePage);
		
		
	}

	private void navigateBackToCommunityByBreadcrumbInMagazine() throws Exception {
	
	report.startStep("Click on Discovery Magazine");
	webDriver.scrollToTopOfPage();
	webDriver.waitUntilElementAppears("Discoveries Magazine",ByTypes.linkText, 10);
	magazinePage.breadCrumbs_DiscMagazineLink.click();
	sleep(1);
	testResultService.assertEquals(true, magazinePage.breadCrumbs_CommunityLink.isDisplayed(), "Community breadcrumb link not displayed");
	testResultService.assertEquals(magazinePage.getMagazineMainTitle(), magazinePage.breadCrumbs_Elements.get(1).getText(), "Discoberies Magazine breadcrumb label not valid");
	
	report.startStep("Click on Community in breadcrumb and verify back to community");
	magazinePage.breadCrumbs_CommunityLink.click();
	sleep(1);
	communityPage.VerifyCommunityPagesTitle();
	
	}
	
	private void clickOnArticleAndVerifyBreadcrumbLink(int articleIndex, int breadcrumbIndex) throws Exception {
	
	webDriver.waitUntilElementAppears(magazinePage.magazineArticles, 10);
	String articleToEnter = magazinePage.getArticleTitle(magazinePage.magazineArticles.get(articleIndex)).getText();
	articlePage = magazinePage.clickOnReadMoreByIndex(articleIndex);
	sleep(1);
	webDriver.waitUntilElementAppears(magazinePage.breadCrumbs_Elements, 10);
	testResultService.assertEquals(articleToEnter, magazinePage.breadCrumbs_Elements.get(breadcrumbIndex).getText(), "Article current breadcrumb label not valid");
	
	}
	
	private void checkIdiomListByFirstAndLast(String expectedFirst,String expectedLast) throws Exception {
		
		String actualFirst = idiomsPage.getIdiomTextByIndex(0);
		String actualLast = idiomsPage.getIdiomTextByIndex(idiomsPage.idioms.size()-1);
		testResultService.assertEquals(expectedFirst, actualFirst, "First idiom does not match to level");		
		testResultService.assertEquals(expectedLast, actualLast, "Last idiom does not match to level");
		
	}
	

	
	@After
	public void tearDown() throws Exception {
		
		report.startStep("Reset the User Community Level to default");
		pageHelper.checkAndSetCurrentstaticContentConnectionString(false);
		pageHelper.changeUserCommunityLevel("Basic", studentId);
	
		super.tearDown();
		
	}
}
