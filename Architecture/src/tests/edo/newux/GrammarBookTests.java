package tests.edo.newux;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.WebElement;

import Enums.SubComponentName;
import Interfaces.TestCaseParams;
import Objects.Course;
import pageObjects.DragAndDropSection;
import pageObjects.EdoHomePage;
import pageObjects.edo.NewUXLoginPage;
import pageObjects.edo.NewUxDragAndDropSection;
import pageObjects.edo.NewUxGrammarBook;
import pageObjects.edo.NewUxLearningArea;
import pageObjects.edo.NewUxMyProfile;
import testCategories.inProgressTests;
import testCategories.reg2;
import testCategories.reg3;

@Category(reg2.class)
public class GrammarBookTests extends BasicNewUxTest {

	NewUxLearningArea learningArea;
	

	@Before
	public void setup() throws Exception {
		super.setup();
		
		report.startStep("Get user and login");
		//createUserAndLoginNewUXClass();
		getUserAndLoginNewUXClass();

		learningArea = new NewUxLearningArea(webDriver, testResultService);

	}

	@Test
	@TestCaseParams(testCaseID = { "25160" })
	//@Category(inProgressTests.class)
	public void testGrammarBookBasicFunction() throws Exception {
			
		NewUxGrammarBook grammarBook = new NewUxGrammarBook(webDriver,
				testResultService);
		int iteration = 1;
		String topic = "Tenses";
		String subTopic = "Present Simple";
		String lesson = "Yes/No Questions";		
		String subTopicAssertion = "PRESENT SIMPLE";	
		
		report.startStep("Check that navigation bar opened and Grammar Book btn enabled");
		homePage.isNavBarOpen();
		homePage.isNavBarItemEnabled("sitemenu__itemGrammarBook");
				
		while (iteration<=2) {
			
			report.startStep("Open Grammar Book");
			homePage.openGrammarBookPage(false);
			
			report.startStep("Click Start Learning");
			grammarBook.clickOnStartLearning();
			
			report.startStep("Click Start Learning and verify Grammar Book header");
			grammarBook.verifyGrammarBookHeader();
			
			report.startStep("Navigate to the 1st lesson");
			grammarBook.navigateToLesson(topic, subTopic,
					lesson);
						
			report.startStep("Verify lesson elements displayed");
			grammarBook.switchToContentInGrammarBook();
			learningArea.checkThatSeeExplanationIsDisplayed();
			learningArea.checkThatPrintIsDisplayed();
			grammarBook.checkThatTextIsDisplayed();
			learningArea.checkThatPlayButtonIsDisplayed();
			
			report.startStep("Open See explanation and verify Header & SubTopic displayed");
			learningArea.clickOnSeeExplanation();
			sleep(5);
			grammarBook.switchToSeeExplanationPopUp();
			grammarBook.verifySeeExplanationHeader();
			grammarBook.verifySeeExplanationSubTopic(subTopicAssertion);
						
			report.startStep("Close See explanation");
			sleep(3);
			webDriver.closeNewTab(2);
			
			report.startStep("Return to Grammar Book menu page");
			webDriver.switchToTopMostFrame();
			homePage.switchToGrammarBook();
			grammarBook.navigateBackToGrammarBookMenu();
			grammarBook.verifyGrammarBookHeader();
			
			report.startStep("Close Grammar Book");
			grammarBook.close();
			
			report.startStep("Check that navigation bar opened and Grammar Book btn enabled");
			homePage.isNavBarOpen();
			homePage.isNavBarItemEnabled("sitemenu__itemGrammarBook");
			
			if (iteration == 1){
			report.startStep("Navigate to Learning Area");
			homePage.clickOnContinueButton();
			sleep(1);
			learningArea.clickToOpenNavigationBar();
			}
			
			iteration++;	
		}
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}
}