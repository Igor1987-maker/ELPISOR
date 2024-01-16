package tests.edo.newux;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import pageObjects.edo.NewUXLoginPage;
import pageObjects.edo.NewUxLearningArea;
import pageObjects.edo.NewUxLearningArea2;
import testCategories.AngularLearningArea;
import testCategories.NonAngularLearningArea;
import testCategories.inProgressTests;
import testCategories.reg1;
import testCategories.reg2;
import Interfaces.TestCaseParams;


@Category(AngularLearningArea.class)
public class SegmentToolsTests2 extends BasicNewUxTest {

	private static final String ES_DICT_FILE = "files/dictFiles/ES/newUx/newUxReadingContent_ES.properties";
	
	NewUxLearningArea2 learningArea2;

	@Before
	public void setup() throws Exception {
		super.setup();
		//learningArea2 = new NewUxLearningArea2(webDriver, testResultService);
	}

	@Test
	@TestCaseParams(testCaseID = { "23602", "23604" }, testMultiple = true)
	public void SeeTranslationTest() throws Exception {

		report.startStep("Get Local user for the test");
		String[] userDetails = getGeneralUser(); //getLocalUser();
		
		String userName = userDetails[0];
		String password = userDetails[1];
		studentId = userDetails[2];

				
		report.startStep("Change Language To Spanish Low Support");
		pageHelper.setUserLangSupport(studentId, 1, 3);

		report.startStep("Login as Local student");
		NewUXLoginPage loginPage = new NewUXLoginPage(webDriver,
				testResultService);
		loginPage.loginAsStudent(userName,password);
		homePage.closeAllNotifications();
		pageHelper.skipOnBoardingHP();
		homePage.waitHomePageloadedFully();
		
		report.startStep("Navigate to Reading Explore");
		learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "A3", 5, 2);
		learningArea2.waitUntilLearningAreaLoaded();
		learningArea2.clickOnNextButton();

		report.startStep("Select segment 2 and press on See Translation tool");
		int segmentNum = 2;
		learningArea2.selectSegmentInReadingByNumber(1,segmentNum, false);
		learningArea2.clickOnSeeTranslationTool();

		report.startStep("Check Segment 2 Translation is Correct");
		dictionaryService.loadDictionaryFile(ES_DICT_FILE);
		String segmentTrans2_es = dictionaryService.getProperty("segment_"
				+ segmentNum);
		String segmentBubble2_es = learningArea2.getSeeTranslationText();
		testResultService.assertEquals(segmentTrans2_es, segmentBubble2_es);

		report.startStep("Select next segment 3");
		learningArea2.selectSegmentInReadingByNumber(1,segmentNum + 1, false);
		learningArea2.clickOnSeeTranslationTool();

		report.startStep("Check Segment 3 Translation is Correct");
		String segmentTrans3_es = dictionaryService.getProperty("segment_"
				+ (segmentNum + 1));
		String segmentBubble3_es = learningArea2.getSeeTranslationText();
		testResultService.assertEquals(segmentTrans3_es, segmentBubble3_es);

		report.startStep("Close See Translation bubble by X and check it is closed");
		
		learningArea2.closeSeeTranslationTool();
		testResultService.assertEquals(false,
		learningArea2.checkIfSeeTranslationBubbleDisplayed());
		
	}

	@Test
	@TestCaseParams(testCaseID = { "23377" })
	public void testSegmentToolsDisplayForTextResorces() throws Exception {
		
		report.startStep("Get user for the test");
			String[] userDetails = getGeneralUser();
			
			String userName = userDetails[0];
			String password = userDetails[1];
			studentId = userDetails[2];

		report.startStep("set Language Spanish Full Support");		
			pageHelper.setUserLangSupport(studentId,3,3);
			sleep(2);
		
		report.startStep("Login as student");
			NewUXLoginPage loginPage = new NewUXLoginPage(webDriver, testResultService);
			loginPage.loginAsStudent(userName,password);
			sleep(1);
			
		
		report.startStep("Navigate to A3, Unit 5, Lesson 2");
			learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "A3", 5, 2);
			sleep(2);
			learningArea2.clickOnNextButton();
			
		report.startStep("Check Explore and Hover on any segment and check the tools");
			learningArea2.selectSegmentInReadingByNumber(1,2, false);
			learningArea2.checkThatSeeTranslationDisplayed();
			learningArea2.checkThatHearPartIsDisplayed();
			sleep(1);
	
		report.startStep("Check Practice");
			learningArea2.clickOnStep(2);
	
			learningArea2.selectSegmentInReadingByNumber(1,3, false);
			learningArea2.checkThatSeeTranslationDisplayed();
			learningArea2.checkThatHearPartIsDisplayed();

		report.startStep("Check Test - Segment Tools Not Displayed");
			learningArea2.clickOnStep(3,false);
	
			learningArea2.clickOnStartTest();
	
			learningArea2.selectSegmentInReadingByNumber(1,4, false);
			learningArea2.checkThatSeeTranslationNotDisplayed();
			learningArea2.checkThatHearPartIsNotDisplayed();
	
		report.startStep("Submit test and check segment tools displayed");
			learningArea2.submitTest(true);
			sleep(1);
			
			learningArea2.selectSegmentInReadingByNumber(1,2, false);
			learningArea2.checkThatSeeTranslationDisplayed();
			learningArea2.checkThatHearPartIsDisplayed();
			
		report.startStep("Press on Task 1 and check segment tools displayed");
			learningArea2.clickOnReviewTestResults();
			sleep(1);
			learningArea2.selectSegmentInReadingByNumber(1,3, false);
			learningArea2.checkThatSeeTranslationDisplayed();
			learningArea2.checkThatHearPartIsDisplayed();
		
		
		report.startStep("Go back to the home page and then to FD, unit 2, lesson 4");
			homePage.clickOnHomeButton();
			homePage.waitHomePageloaded();
			
			homePage.navigateToRequiredCourseOnHomePage("First Discoveries");
			homePage.clickOnUnitLessons(2);
			homePage.clickOnLesson(2, 4);
			sleep(1);
			learningArea2.clickOnStep(3);
			learningArea2.selectSegmentInReadingExploreByNumber(3);
	
			learningArea2.checkThatSeeTranslationDisplayed();
			learningArea2.checkThatHearPartIsDisplayed();
	
			learningArea2.clickOnStep(2);
	
			learningArea2.selectSegmentInReadingExploreByNumber(2);
	
			learningArea2.checkThatSeeTranslationDisplayed();
			learningArea2.checkThatHearPartIsDisplayed();

		report.startStep("Go back to the home page and then to A1, unit 4, lesson 1");
			homePage.clickOnHomeButton();
			homePage.waitHomePageloaded();
			
			homePage.navigateToCourseUnitAndLesson(courseCodes, "A1", 4, 1);
			sleep(5);
			learningArea2.clickOnNextButton();

		report.startStep("Check Explore");
			learningArea2.clickOnSeeText();
			learningArea2.selectTextForRecord(2);
	
			learningArea2.checkThatSeeTranslationDisplayed();
			learningArea2.checkThatHearPartIsDisplayed();
			learningArea2.checkThatRecordYourselfIsDisplayed();
			sleep(2);

		report.startStep("Check Practice");
			learningArea2.clickOnStep(2);
	
			learningArea2.clickOnSeeText();
			sleep(1);
			learningArea2.selectTextForRecord(2);
	
			learningArea2.checkThatSeeTranslationDisplayed();
			learningArea2.checkThatHearPartIsDisplayed();
			learningArea2.checkThatRecordYourselfIsDisplayed();

		report.startStep("Check Test - Segment Tools Not Displayed");
			learningArea2.clickOnStep(3, false);
			learningArea2.clickOnStartTest();
			sleep(2);
			learningArea2.checkThatSeeTextIsNotDisplayed();
	
	}
	
	@After
	public void tearDown() throws Exception {
		report.startStep("Set progress to FD item");
		studentService.createSingleProgressRecored(studentId,"20000", "31515", 10, false, 1);
		
		report.startStep("Set No Support Language");
		pageHelper.setUserLangSupportLevel(studentId,0);
		
		super.tearDown();
	}
}