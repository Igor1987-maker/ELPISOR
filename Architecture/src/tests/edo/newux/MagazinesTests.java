package tests.edo.newux;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Interfaces.TestCaseParams;
import pageObjects.edo.NewUXLoginPage;
import pageObjects.edo.NewUxCommunityPage;
import pageObjects.edo.NewUxHomePage;
import pageObjects.edo.NewUxMagazinePage;
import pageObjects.edo.NewUxMyProfile;

public class MagazinesTests extends BasicNewUxTest{
	
	NewUXLoginPage loginPage;
	NewUxCommunityPage communityPage;
	
	@Before
	public void setup() throws Exception {
		//BasicNewUxTest.institutionName = BasicNewUxTest.institutionsName[10];
		super.setup();
		
		// Initialize Login Page
		loginPage = new NewUXLoginPage(webDriver, testResultService);
		
		report.startStep("Login to ED");
		homePage = getUserAndLoginNewUXClass();
		//homePage.waitHomePageloadedFully();
		
		// Close Modal pop up if it appears
		//homePage.closeModalPopUp();
	}

	@Test	
	@TestCaseParams(testCaseID = { "74903" })
	public void testMagazineArticleOnHomePage() throws Exception {
				
		report.startStep("Open My profile");
		homePage.clickOnMyProfile();
		homePage.switchToMyProfile();
		sleep(2);
		
		NewUxMyProfile myProfile = new NewUxMyProfile(webDriver, testResultService);
		
		report.startStep("Change Community Level Randomly");
		String newLevel = myProfile.changeCommunitySiteLevelRandomly();
		webDriver.switchToMainWindow();
		
		report.startStep("Go to Course " + coursesNames[1]);
		String courseName = coursesNames[1];
		homePage.navigateToRequiredCourseOnHomePage(courseName);
		
		report.startStep("Click on Unit Lessons 2");
		homePage.clickOnUnitLessons(2);
		homePage.scrollToBottomOfLessonsElement(1);
		sleep(1);
		
		report.startStep("Click on Magazine Enrichment Button (open)");
		homePage.clickMagazineEnrichmentButton();
		
		report.startStep("Check Magazine List is Opened");
		homePage.checkMagazinesListIsOpened();
		
		report.startStep("Click on Magazine Enrichment Button (close)");
		homePage.clickMagazineEnrichmentButton();
		
		report.startStep("Check Magazine List is Closed");
		homePage.checkMagazinesListIsClosed();
	
		report.startStep("Click on Magazine Enrichment Button (open)");
		homePage.clickMagazineEnrichmentButton();
		sleep(1);
		
		report.startStep("Click on a Random Magazine");
		String magazineName = homePage.clickRandomMagazine();
		sleep(1);
		
		// Initialize community page
		communityPage = new NewUxCommunityPage(webDriver, testResultService);
		
		// validate the name of the magazine appears in the title of the new opened page
		communityPage.validateMagazineNameAndLevelAreCorrect(magazineName, newLevel);
	}
	
	
	@Test
	@TestCaseParams(testCaseID = {"84993"})
	public void testSearchMagazineArticles() throws Exception{
	//institutionName = institutionsName[10];
	//className = "classNewUnits";
		
	report.startStep("Move to magazine");	
		NewUxCommunityPage communityPage = homePage.clickOnCommunityButton();
		NewUxMagazinePage magazinePage = communityPage.openMagazinePage();
	report.startStep("Verifying search feature");	
		magazinePage.verifyCorrectSearchBarResult();
				
	report.startStep("Log Out");
		homePage.clickOnLogOut();
		
	report.startStep("Verify that user loged out");
		loginPage.verifyDefaultBannerExist();
	}
	
	
	@After
	public void tearDown() throws Exception {
			super.tearDown();
	}

}
