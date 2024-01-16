package tests.edo.newux;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import pageObjects.edo.NewUXLoginPage;
import pageObjects.edo.NewUxLearningArea;
import testCategories.NonAngularLearningArea;
import testCategories.inProgressTests;
import testCategories.reg1;
import testCategories.reg2;
import Interfaces.TestCaseParams;


@Category(NonAngularLearningArea.class)
public class SegmentToolsTests extends BasicNewUxTest {

	private static final String ES_DICT_FILE = "files/dictFiles/ES/newUx/newUxReadingContent_ES.properties";
	
	NewUxLearningArea learningArea;

	@Before
	public void setup() throws Exception {
		super.setup();
		learningArea = new NewUxLearningArea(webDriver, testResultService);
	}

	@Test
	@TestCaseParams(testCaseID = { "23602", "23604" }, testMultiple = true)
	public void SeeTranslationTest() throws Exception {

		report.startStep("Get Local user for the test");
		
	//--igb 2018.07.23 --- use 'st1' user all time-------		
	//  String[] userDetails = getLocalUser();
	//	
	//	String userName = userDetails[0];
	//	String password = userDetails[1];
	//	studentId = userDetails[2];
	//--igb 2018.07.23 --- use 'st1' user all time-------
		String institutionId = dbService.getInstitutionIdByUserId(studentId);
		String userName = dbService.getUserNameById(studentId, institutionId);// "st1";
		String password = "12345";
		
		//studentId = "52332180000002";
	//--igb 2018.07.23 --- use 'st1' user all time-------		
				
		report.startStep("Change Language To Spanish Low Support");
		pageHelper.setUserLangSupport(studentId, 1, 3);

		report.startStep("Login as Local student");
		NewUXLoginPage loginPage = new NewUXLoginPage(webDriver,
				testResultService);
		loginPage.loginAsStudent(userName,password);
		
		
		report.startStep("Navigate to Reading Explore");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "A3", 5, 2);

		report.startStep("Select segment 2 and press on See Translation tool");
		int segmentNum = 2;
		learningArea.selectSegmentInReadingByNumber(1,segmentNum, false);
		learningArea.clickOnSeeTranslationTool();

		report.startStep("Check Segment 2 Translation is Correct");
		dictionaryService.loadDictionaryFile(ES_DICT_FILE);
		String segmentTrans2_es = dictionaryService.getProperty("segment_"
				+ segmentNum);
		String segmentBubble2_es = learningArea.getSeeTranslationText();
		testResultService.assertEquals(segmentTrans2_es, segmentBubble2_es);

		report.startStep("Select next segment 3");
		learningArea.selectSegmentInReadingByNumber(1,segmentNum + 1, false);
		learningArea.clickOnSeeTranslationTool();

		report.startStep("Check Segment 3 Translation is Correct");
		String segmentTrans3_es = dictionaryService.getProperty("segment_"
				+ (segmentNum + 1));
		String segmentBubble3_es = learningArea.getSeeTranslationText();
		testResultService.assertEquals(segmentTrans3_es, segmentBubble3_es);

		report.startStep("Close See Translation bubble by X and check it is closed");
		
		learningArea.closeSeeTranslationTool();
		testResultService.assertEquals(false,
		learningArea.checkIfSeeTranslationBubbleDisplayed());
		
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
	
		report.startStep("Login as student");
		NewUXLoginPage loginPage = new NewUXLoginPage(webDriver,
				testResultService);
		loginPage.loginAsStudent(userName,password);
		sleep(1);
			
		
		report.startStep("Navigate to A3, Unit 5, Lesson 2");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "A3", 5, 2);

		report.startStep("Check Explore and Hover on any segment and check the tools");
		sleep(2);
		learningArea.selectSegmentInReadingByNumber(1,2, false);
		learningArea.checkThatSeeTranslationDisplayed();
		learningArea.checkThatHearPartIsDisplayed();
		sleep(1);

		report.startStep("Check Practice");
		learningArea.clickOnStep(2);

		learningArea.selectSegmentInReadingByNumber(1,3, false);
		learningArea.checkThatSeeTranslationDisplayed();
		learningArea.checkThatHearPartIsDisplayed();

		report.startStep("Check Test - Segment Tools Not Displayed");
		learningArea.clickOnStep(3);

		learningArea.clickOnStartTest();

		learningArea.selectSegmentInReadingByNumber(1,4, false);
		learningArea.checkThatSeeTranslationNotDisplayed();
		learningArea.checkThatHearPartIsNotDisplayed();

		report.startStep("Submit test and check segment tools displayed");
		learningArea.submitTest(true);
		
		sleep(1);
		learningArea.selectSegmentInReadingByNumber(1,2, false);
		learningArea.checkThatSeeTranslationDisplayed();
		learningArea.checkThatHearPartIsDisplayed();
		
		report.startStep("Press on Task 1 and check segment tools displayed");
		learningArea.clickOnTask(0);
		sleep(1);
		learningArea.selectSegmentInReadingByNumber(1,3, false);
		learningArea.checkThatSeeTranslationDisplayed();
		learningArea.checkThatHearPartIsDisplayed();
		
		
		report.startStep("Go back to the home page and then to FD, unit 2, lesson 4");
		//learningArea.clickToOpenNavigationBar();
		learningArea.clickOnHomeButton();
		homePage.carouselNavigateNext();
		homePage.navigateToCourseUnitAndLesson(courseCodes, "FD", 2, 4);
		
		learningArea.clickOnStep(3);
		learningArea.selectSegmentInReadingExploreByNumber(3);

		learningArea.checkThatSeeTranslationDisplayed();
		learningArea.checkThatHearPartIsDisplayed();

		learningArea.clickOnStep(2);

		learningArea.selectSegmentInReadingExploreByNumber(2);

		learningArea.checkThatSeeTranslationDisplayed();
		learningArea.checkThatHearPartIsDisplayed();

		report.startStep("Go back to the home page and then to A1, unit 4, lesson 1");
		//learningArea.clickToOpenNavigationBar();
		learningArea.clickOnHomeButton();
		homePage.navigateToCourseUnitAndLesson(courseCodes, "A1", 4, 1);
		sleep(2);

		report.startStep("Check Explore");
		learningArea.clickOnSeeText();
		learningArea.selectTextForRecord(2);

		learningArea.checkThatSeeTranslationDisplayed();
		learningArea.checkThatHearPartIsDisplayed();
		learningArea.checkThatRecordYourselfIsDisplayed();
		sleep(2);

		report.startStep("Check Practice");
		learningArea.clickOnStep(2);

		learningArea.clickOnSeeText();
		sleep(1);
		learningArea.selectTextForRecord(2);

		learningArea.checkThatSeeTranslationDisplayed();
		learningArea.checkThatHearPartIsDisplayed();
		learningArea.checkThatRecordYourselfIsDisplayed();

		report.startStep("Check Test - Segment Tools Not Displayed");
		learningArea.clickOnStep(3);
		learningArea.clickOnStartTest();
		sleep(2);
		learningArea.checkThatSeeTextIsNotDisplayed();
	
		
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