package tests.edo.newux;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.WebElement;

import Enums.ByTypes;
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
import pageObjects.edo.NewUxMyProfile;
import testCategories.NonAngularLearningArea;
import testCategories.inProgressTests;
import testCategories.reg2;
import testCategories.reg3;

@Category(NonAngularLearningArea.class)
public class InstitutionPageTests extends BasicNewUxTest {

	
	NewUxLearningArea learningArea;
	NewUxInstitutionPage ipage;

	@Before
	public void setup() throws Exception {
		super.setup();
		
		report.startStep("Get user and login");
		getUserAndLoginNewUXClass();
		learningArea = new NewUxLearningArea(webDriver, testResultService);
		ipage = new NewUxInstitutionPage(webDriver, testResultService);

	}

	@Test
	@TestCaseParams(testCaseID = { "28648" })
	public void testInstitutionPage() throws Exception {
					
		int iteration = 1;
						
		report.startStep("Check that navigation bar opened and Institution Page btn enabled");
		testResultService.assertEquals(true, homePage.isNavBarOpen());
		testResultService.assertEquals(true, homePage.isNavBarItemEnabled("sitemenu__itemInstPage"));
				
		while (iteration<=2) {
			
			report.startStep("Open Institution Page");
			homePage.openInstitutionPage(false);
			sleep(4);
												
			report.startStep("Verify elements displayed on left side");
			ipage.verifyLeftBannerInnerText();
			sleep(3);
			
			report.startStep("Verify elements displayed on right side");
			ipage.verifyRightBannerInnerText();
			sleep(3);
						
			report.startStep("Close Institution Page");
			ipage.close();
			sleep(2);
						
			if (iteration == 1){
			report.startStep("Navigate to Learning Area");
			homePage.clickOnContinueButton();
			sleep(1);
			learningArea.clickToOpenNavigationBar();
			
			report.startStep("Check that navigation bar opened and Institution Page btn enabled");
			testResultService.assertEquals(true, homePage.isNavBarOpen());
			testResultService.assertEquals(true, homePage.isNavBarItemEnabled("sitemenu__itemInstPage"));
			
			}
			
			iteration++;
			
		}
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}
}
