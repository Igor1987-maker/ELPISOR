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
import testCategories.AngularLearningArea;
import testCategories.NonAngularLearningArea;
import testCategories.inProgressTests;
import testCategories.reg2;
import testCategories.reg3;
import testCategories.unstableTests;

@Category(AngularLearningArea.class)
public class VocabularyTests2 extends BasicNewUxTest {

	NewUxLearningArea2 learningArea2;
	NewUXLoginPage loginPage;

	@Before
	public void setup() throws Exception {
		super.setup();
		report.startStep("Get user and Login");
		getUserAndLoginNewUXClass();
	//	homePage.skipNotificationWindow();
	}

	@Test
	@TestCaseParams(testCaseID = { "24430" })
	public void testOldVocabularyBasicFunction() throws Exception {
		
		String[] letters = new String[] { "A a", "B b", "C c", "D d",
				"E e", "F f"};
		
		String[] words = new String[] { "apple", "bed", "cat", "dog",
				"egg", "frog"};
		
		report.startStep("Navigate to FD -> Unit 1 -> Lesson 1 -> Explore");

		learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "FD", 1, 1);
		sleep(1);
		
		learningArea2.clickOnStep(2);

		sleep(1);

		report.startStep("Select Words and Check Right Side Updated");
		
		for (int i = 0; i < letters.length; i++) {
			learningArea2.checkVocabularySelectionSync(i+1, letters[i], words[i]);
		}
		
		report.startStep("Navigate to Practice Step");
		learningArea2.clickOnStep(3);
		
		report.startStep("Select Words and Check Right Side Updated");
		
		for (int i = 0; i < letters.length; i++) {
			learningArea2.checkVocabularySelectionSync(i+1, letters[i], words[i]);
		}				

	}
	
	@Test
	//@Category(unstableTests.class)
	@TestCaseParams(testCaseID = { "24431" })
	public void testOldVocabularyToolsBehavior() throws Exception {
	
		report.startStep("Navigate to FD -> Unit 1 -> Lesson 1 -> Explore");
		learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "FD", 1, 1);
		learningArea2.waitUntilLearningAreaLoaded();
		learningArea2.clickOnStep(2);
		sleep(1);
		
		report.startStep("Check Only Record Yourself Tool Displayed in Explore");
		learningArea2.checkThatRecordYourselfIsDisplayedInOldVocabulary();
		learningArea2.checkThatSeeTransToolIsNotDisplayedInOldVocabulary();
		
		report.startStep("Go To Practice Step");
		learningArea2.clickOnStep(3);
		
		report.startStep("Check Only Record Yourself Tool Displayed in Practice");
		learningArea2.checkThatRecordYourselfIsDisplayedInOldVocabulary();
		learningArea2.checkThatSeeTransToolIsNotDisplayedInOldVocabulary();
		
		report.startStep("Logout, Set Low Level Localization Support in DB");
		learningArea2.clickOnLogoutLearningArea();
		loginPage = new NewUXLoginPage (webDriver, testResultService);
		testResultService.assertEquals(false, loginPage.isSubmitButtonEnabled(), "Checking that ED Login Page displayed");
		pageHelper.setUserLangSupport(studentId, 1, 3);
		
		report.startStep("Login");
		String userName = dbService.getUserFirstNameByUserId(studentId);
		loginPage.loginAsStudent(userName, "12345");
		sleep(1);
		homePage.closeAllNotifications();
		homePage.waitHomePageloadedFully();
		report.startStep("Navigate to FD -> Unit 1 -> Lesson 1 -> Explore");
		learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "FD", 1, 1);
		learningArea2.waitForPageToLoad();
		learningArea2.clickOnStep(2);
		sleep(1);
		
		report.startStep("Check Record Yourself & See Translation Tools Displayed in Explore");
		learningArea2.checkThatRecordYourselfIsDisplayedInOldVocabulary();
		learningArea2.checkThatSeeTransToolIsDisplayedInOldVocabulary();
		
		report.startStep("Select segment 2 and press on See Translation tool");
		learningArea2.clickOnSeeTransToolInOldVocabulary();

		//report.startStep("Check Translation is Correct"); TODO doesn't work in QA environment due to certificate network error
		
		report.startStep("Close See Translation bubble by X and check it is closed");
		
		learningArea2.closeSeeTranslationTool();
		testResultService.assertEquals(false, learningArea2.checkIfSeeTranslationBubbleDisplayed());
				
		report.startStep("Go To Practice Step");
		learningArea2.clickOnStep(3);
		sleep(1);
		
		report.startStep("Check Record Yourself & See Translation Tools Displayed in Practice");
		learningArea2.checkThatRecordYourselfIsDisplayedInOldVocabulary();
		learningArea2.checkThatSeeTransToolIsDisplayedInOldVocabulary();
	}
	
	
	@Test
	@TestCaseParams(testCaseID = { "24427","24428","24429" })
	public void testNewVocabularyBasicFunction() throws Exception {
			
		String[] wordsList = new String[] { "actor", "classical music", "concert", "movie",
				"newspaper", "radio","rock music", "rock star", "television", "theater" };
		
				
		report.startStep("Navigate to B1 -> Unit 1 -> Lesson 7 -> Explore");
		learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "B1", 1, 7);
		sleep(2);
		learningArea2.clickOnNextButton();

		report.startStep("Select Words and Check Right Side Updated");
		
		for (int i = 0; i < wordsList.length; i++) {
			learningArea2.checkNewVocabularySelectionSync(i+1, wordsList[i]);
		}
	
		report.startStep("Verify Tools Btns Displayed in Explore");
	    learningArea2.checkThatRecordYourselfIsDisplayedInNewVocabulary();
		learningArea2.checkThatHearIsDisplayedInNewVocabulary();
		
		
     	report.startStep("Navigate to Practice Step");
		learningArea2.clickOnStep(2);
		learningArea2.clickOnNextButton();
		
		report.startStep("Select Words and Check Right Side Updated");
		for (int i = 0; i < wordsList.length; i++) {
			learningArea2.checkNewVocabularySelectionSyncInPractice(i, wordsList[i]);
			
		}	
		
		report.startStep("Verify Tools Btns Displayed in Practice");
		learningArea2.checkThatRecordYourselfIsDisplayedInNewVocabulary();
		learningArea2.checkThatHearIsDisplayedInNewVocabulary();

		report.startStep("Navigate to Test Step");
		learningArea2.clickOnStep(3, false);
		learningArea2.clickOnStartTest();
		
		report.startStep("Verify Default Image Displayed in Test");
		learningArea2.checkThatDefaultImageIsDisplayedInNewVocabularyTest();
		
		
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
	
