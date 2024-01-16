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
import pageObjects.edo.NewUxLearningArea;
import pageObjects.edo.NewUxLearningArea2;
import testCategories.NonAngularLearningArea;
import testCategories.inProgressTests;
import testCategories.reg2;
import testCategories.reg3;
import testCategories.unstableTests;

@Category(NonAngularLearningArea.class)
public class VocabularyTests extends BasicNewUxTest {

	NewUxLearningArea learningArea;
	NewUXLoginPage loginPage;
	NewUxLearningArea2 learningArea2;

	@Before
	public void setup() throws Exception {
		super.setup();
		report.startStep("Get user and Login");
		getUserAndLoginNewUXClass();
		homePage.closeAllNotifications();
		learningArea = new NewUxLearningArea(webDriver, testResultService);
		learningArea2 = new NewUxLearningArea2(webDriver, testResultService);

	}

	@Test
	@TestCaseParams(testCaseID = { "24430" })
	public void testOldVocabularyBasicFunction() throws Exception {
		
		String[] letters = new String[] { "A a", "B b", "C c", "D d",
				"E e", "F f"};
		
		String[] words = new String[] { "apple", "bed", "cat", "dog",
				"egg", "frog"};
		
		report.startStep("Navigate to FD -> Unit 1 -> Lesson 1 -> Explore");

		homePage.navigateToCourseUnitAndLesson(courseCodes, "FD", 1, 1);

		learningArea.clickOnStep(2);
		learningArea.clickOnNextButton();
		sleep(2);

		report.startStep("Select Words and Check Right Side Updated");
		
		for (int i = 0; i < letters.length; i++) {
			learningArea.checkVocabularySelectionSync(i+1, letters[i], words[i]);
		}
		
		report.startStep("Navigate to Practice Step");
		learningArea.clickOnStep(3);
		learningArea.clickOnNextButton();
		report.startStep("Select Words and Check Right Side Updated");
		
		for (int i = 0; i < letters.length; i++) {
			learningArea.checkVocabularySelectionSync(i+1, letters[i], words[i]);
		}				

	}
	
	@Test
	//@Category(unstableTests.class)
	@TestCaseParams(testCaseID = { "24431" })
	public void testOldVocabularyToolsBehavior() throws Exception {
	
		report.startStep("Navigate to FD -> Unit 1 -> Lesson 1 -> Explore");
		homePage.navigateToRequiredCourseOnHomePage("First Discoveries");
		homePage.waitHomePageloadedFully();
		homePage.navigateToCourseUnitAndLesson(courseCodes, "FD", 1, 1);
		learningArea2.waitUntilLearningAreaLoaded();
		learningArea.clickOnStep(2);
		
		//sleep(2);
		
		report.startStep("Check Only Record Yourself Tool Displayed in Explore");
		//learningArea.clickOnNextButton();
		learningArea.checkThatRecordYourselfIsDisplayedInOldVocabulary();
		learningArea.checkThatSeeTransToolIsNotDisplayedInOldVocabulary();
		
		report.startStep("Go To Practice Step");
		learningArea.clickOnStep(3);
		sleep(2);
		
		report.startStep("Check Only Record Yourself Tool Displayed in Practice");
		learningArea.clickOnNextButton();
		learningArea.checkThatRecordYourselfIsDisplayedInOldVocabulary();
		learningArea.checkThatSeeTransToolIsNotDisplayedInOldVocabulary();
		
		report.startStep("Logout, Set Low Level Localization Support in DB");
		learningArea.clickOnLogoutLearningArea();
		loginPage = new NewUXLoginPage (webDriver, testResultService);
		testResultService.assertEquals(false, loginPage.isSubmitButtonEnabled(), "Checking that ED Login Page displayed");
		pageHelper.setUserLangSupport(studentId, 1, 3);
		
		report.startStep("Login");
		String userName = dbService.getUserFirstNameByUserId(studentId);
		loginPage.loginAsStudent(userName, "12345");
		sleep(1);
		homePage.closeAllNotifications();
		report.startStep("Navigate to FD -> Unit 1 -> Lesson 1 -> Explore");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "FD", 1, 1);
		learningArea2.waitUntilLearningAreaLoaded();
		learningArea.clickOnStep(2);
		sleep(1);
		
		report.startStep("Check Record Yourself & See Translation Tools Displayed in Explore");
		learningArea.checkThatRecordYourselfIsDisplayedInOldVocabulary();
		learningArea.checkThatSeeTransToolIsDisplayedInOldVocabulary();
		
		report.startStep("Select segment 2 and press on See Translation tool");
		learningArea.clickOnSeeTransToolInOldVocabulary();

		//report.startStep("Check Translation is Correct"); TODO doesn't work in QA environment due to certificate network error
		
		report.startStep("Close See Translation bubble by X and check it is closed");
		
		learningArea.closeSeeTranslationTool();
		testResultService.assertEquals(false, learningArea.checkIfSeeTranslationBubbleDisplayed());
				
		report.startStep("Go To Practice Step");
		learningArea.clickOnStep(3);
		sleep(1);
		
		report.startStep("Check Record Yourself & See Translation Tools Displayed in Practice");
		learningArea.clickOnNextButton();
		learningArea.checkThatRecordYourselfIsDisplayedInOldVocabulary();
		learningArea.checkThatSeeTransToolIsDisplayedInOldVocabulary();
	}
	
	@Test
	@TestCaseParams(testCaseID = { "24427","24428","24429" })
	public void testNewVocabularyBasicFunction() throws Exception {
			
		String[] wordsList = new String[] { "actor", "classical music", "concert", "movie",
				"newspaper", "radio","rock music", "rock star", "television", "theater" };
		
				
		report.startStep("Navigate to B1 -> Unit 1 -> Lesson 7 -> Explore");

		homePage.navigateToCourseUnitAndLesson(courseCodes, "B1", 1, 7);
		sleep(2);
		
		report.startStep("Click Next Button");
		learningArea.clickOnNextButton();

		report.startStep("Select Words and Check Right Side Updated");
		
		for (int i = 0; i < wordsList.length; i++) {
			learningArea.checkNewVocabularySelectionSync(i+1, wordsList[i]);
		}
		
		report.startStep("Verify Tools Btns Displayed in Explore");
		
		learningArea.checkThatRecordYourselfIsDisplayedInNewVocabulary();
		learningArea.checkThatHearIsDisplayedInNewVocabulary();
		
		
		report.startStep("Navigate to Practice Step");
		//learningArea.clickOnStep(2);
		learningArea.clickOnStepNew(1);
		
		report.startStep("Click Next Button twice");
		learningArea.clickOnNextButton(); 
		learningArea.clickOnNextButton();
		
		report.startStep("Select Words and Check Right Side Updated");
		for (int i = 0; i < wordsList.length; i++) {
			learningArea.checkNewVocabularySelectionSyncInPractice(i, wordsList[i]);
			
		}	
		
		report.startStep("Verify Tools Btns Displayed in Practice");
		
		learningArea.checkThatRecordYourselfIsDisplayedInNewVocabulary();
		learningArea.checkThatHearIsDisplayedInNewVocabulary();

		report.startStep("Navigate to Test Step");
		//learningArea.clickOnStep(3);
		learningArea.clickOnStepNew(2);
		learningArea.clickOnStartTest();
		
		report.startStep("Verify Default Image Displayed in Test");
		learningArea.checkThatDefaultImageIsDisplayedInNewVocabularyTest();
		
		
	}
	
	@After
	public void tearDown() throws Exception {
		report.startStep("Reset language settings");
		pageHelper.setUserLangSupport(studentId, 0, 10);
		
		report.startStep("Set progress to first FD course item");
		studentService.createSingleProgressRecored(studentId,"20000", "31515", 10, false, 1);
		
		super.tearDown();
	}	
	
}
	
