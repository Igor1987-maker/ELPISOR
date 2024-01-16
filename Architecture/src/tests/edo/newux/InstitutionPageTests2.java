package tests.edo.newux;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.WebElement;

import Enums.ByTypes;
import Enums.NavBarItems;
import Enums.SubComponentName;
import Interfaces.TestCaseParams;
import Objects.Course;
import pageObjects.DragAndDropSection;
import pageObjects.EdoHomePage;
import pageObjects.edo.NewUXLoginPage;
import pageObjects.edo.NewUxDragAndDropSection;
import pageObjects.edo.NewUxGrammarBook;
import pageObjects.edo.NewUxInstitutionPage;
import pageObjects.edo.NewUxLearningArea;
import pageObjects.edo.NewUxLearningArea2;
import pageObjects.edo.NewUxMyProfile;
import testCategories.AngularLearningArea;
import testCategories.NonAngularLearningArea;
import testCategories.inProgressTests;
import testCategories.reg2;
import testCategories.reg3;

@Category(AngularLearningArea.class)
public class InstitutionPageTests2 extends BasicNewUxTest {
	
	NewUxInstitutionPage ipage;

	@Before
	public void setup() throws Exception {
		super.setup();
		
		report.startStep("Get user and login");
		getUserAndLoginNewUXClass();
		
//		ipage = new NewUxInstitutionPage(webDriver, testResultService);
	}

	@Test
	@TestCaseParams(testCaseID = { "28648" })
	public void testInstitutionPage() throws Exception {
								
		report.startStep("Check that navigation bar opened and Institution Page btn enabled");
		homePage.waitHomePageloaded();
		testResultService.assertEquals(true, homePage.isNavBarOpen());
		testResultService.assertEquals(true, homePage.isNavBarItemEnabled("sitemenu__itemInstPage"));
							
		report.startStep("Open Institution Page");
		ipage = homePage.openInstitutionPage(false);
		sleep(2);
													
		report.startStep("Verify elements displayed on left side");
		ipage.verifyLeftBannerInnerText();
		sleep(2);
				
		report.startStep("Verify elements displayed on right side");
		ipage.verifyRightBannerInnerText();
		sleep(2);
						
		report.startStep("Close Institution Page");
		ipage.close();
		sleep(2);
				
		report.startStep("Navigate to Learning Area and check Inst Page not displayed");
		NewUxLearningArea2 newUxLearningArea2 = homePage.clickOnContinueButton2();
		newUxLearningArea2.waitToLearningAreaLoaded();
		
		homePage.clickToOpenNavigationBar();
		sleep(3);
		
		homePage.checkIfNavBarItemDisplayed(false, NavBarItems.sitemenu__itemInstPage);		
		sleep(1);
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}
}
