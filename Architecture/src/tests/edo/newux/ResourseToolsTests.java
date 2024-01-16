package tests.edo.newux;

import java.nio.charset.Charset;
import java.util.List;

import net.lightbody.bmp.core.har.Har;

import org.apache.tools.ant.types.resources.First;
import org.apache.xalan.transformer.Counter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.grid.web.servlet.TestSessionStatusServlet;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import Enums.ByTypes;
import Enums.Skill;
import pageObjects.edo.NewUxLearningArea;
import testCategories.NonAngularLearningArea;
import testCategories.inProgressTests;
import testCategories.reg1;
import testCategories.reg2;
import Interfaces.TestCaseParams;
import tests.edo.newux.testDataValidation;

@Category(NonAngularLearningArea.class)
public class ResourseToolsTests extends BasicNewUxTest {

	private static final String readingTabTitle_1 = "Schedule";
	private static final String readingTabTitle_2 = "E-mail";
	private static final String listeningVideoTabTitle = "Show it!";
	private static final String listeningAudioTabTitle = "Listening";
	private static final String imageOnlyTooltip = "Look at the image!";
	private static final String imageOnlyImageName = "b1roab02.jpg";
	String [] [] typesInOrder = new String [3] [2]; 
	
	NewUxLearningArea learningArea;
	testDataValidation testData = new testDataValidation(); 
	Har har;
	
	@Before
	public void setup() throws Exception {
				
		super.setup();
		learningArea = new NewUxLearningArea(webDriver, testResultService);
	}

	@Test
	@TestCaseParams(testCaseID = { "23348" })
	public void resourseToolsGrammarDefaultDisplayPerStep() throws Exception {
		report.startStep("Get user and login");
		getUserAndLoginNewUXClass();
		report.startStep("Navigate");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "I2", 5, 4);

		report.startStep("Check that See explanation apear in Explore");
		learningArea.checkThatSeeExplanationIsDisplayed();
		learningArea.checkThatPrintIsDisplayed();

		report.startStep("Click on Practice");
		learningArea.clickOnStep(2);
		learningArea.checkThatSeeExplanationIsDisplayed();
		learningArea.checkThatPrintIsDisplayed();

		report.startStep("Click on Test");
		learningArea.clickOnStep(3);
		learningArea.clickOnStartTest();
		learningArea.checkThatSeeExplanationIsNotDisplayed();
		learningArea.checkThatPrintIsNotDisplayed();

	}

	@Test
	@TestCaseParams(testCaseID = { "23351" ,"23648","23644","23647","23646"})
	public void testResourceTextResourcesToolsDefaultDisplayPerStep()
			throws Exception {
		
		report.startStep("Create user and login");
		createUserAndLoginNewUXClass();

		report.startStep("Navigate");
		homePage.navigateToRequiredCourseOnHomePage("First Discoveries");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "A3", 5, 2);

		report.startStep("Check that these tools are displayed");
		checkThatTextResourceToolsAreDisplayed();
		sleep(2);

		report.startStep("Click on Practice");
		learningArea.clickOnStep(2);
		checkThatTextResourceToolsAreDisplayed();
		sleep(2);
		
		report.startStep("Click on Test");
		learningArea.clickOnStep(3);
		learningArea.clickOnStartTest();
		checkThatTextResourceToolsNotDisplayed();
		
		report.startStep("Submit test and check no resource tools displayed");
		learningArea.submitTest(true);
		webDriver.switchToTopMostFrame();
		checkThatTextResourceToolsAreDisplayed();
		sleep(1);
		
		report.startStep("Press on Task 1 and check resource tools displayed");
		learningArea.clickOnTask(0);
		checkThatTextResourceToolsAreDisplayed();
		
		report.startStep("Go to FD, unit 2, lesson 4");
		//learningArea.clickToOpenNavigationBar();
		learningArea.clickOnHomeButton();
		sleep(3);
		homePage.carouselNavigateNext();
		homePage.navigateToCourseUnitAndLesson(courseCodes, "FD", 2, 4);
		learningArea.clickOnStep(2);
		learningArea.checkThatHearAllIsDisplayed();
		learningArea.checkThatPrintIsDisplayed();

		report.startStep("Click on Pracice");
		learningArea.clickOnStep(2);
		learningArea.checkThatHearAllIsDisplayed();
		learningArea.checkThatPrintIsDisplayed();
		sleep(2);
		
		report.startStep("Go to FD, unit 3, lesson 1");
		//learningArea.clickToOpenNavigationBar();
		learningArea.clickOnHomeButton();
		homePage.navigateToCourseUnitAndLesson(courseCodes, "FD", 3, 1);
		sleep(1);
		learningArea.clickOnStep(2);
		learningArea.clickOnSeeText();
		learningArea.checkThatPrintIsDisplayed();
		sleep(1);

		learningArea.clickOnStep(3);
		learningArea.clickOnSeeText();
		learningArea.checkThatPrintIsDisplayed();

	}

	@Test
	@TestCaseParams(testCaseID = { "23352","34456" })
	public void testResourseToolsAudioResourcesDefaultDisplayedPErStep()
			throws Exception {
		report.startStep("Get user and login");
		getUserAndLoginNewUXClass();

		report.startStep("Navigate to A3-U6-L1");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "A3", 6, 1);

		report.startStep("Check See Text tool displayed in Explore");
		learningArea.checkThatSeeTextIsDisplayed();
		
		report.startStep("Check See Text tool displayed in Practice");
		learningArea.clickOnStep(2);
		learningArea.checkThatSeeTextIsDisplayed();

		report.startStep("Check See Text tool NOT displayed in Test");
		learningArea.clickOnStep(3);
		learningArea.clickOnStartTest();
		learningArea.checkThatTextDisplayed(false);

		report.startStep("Submit test and check no resource tools displayed");
		learningArea.submitTest(true);
		
		learningArea.checkThatTextDisplayed(false);
		sleep(1);
		
		report.startStep("Press on Task 1 and check resource tools displayed");
		learningArea.clickOnTask(0);
		learningArea.checkThatTextDisplayed(true);
		
		report.startStep("Go to Basic 3, Eating out, Japanese restorant");
		//learningArea.clickToOpenNavigationBar();
		learningArea.clickOnHomeButton();
		sleep(1);
		homePage.navigateToCourseUnitAndLesson(courseCodes, "B3", 4, 3);
		sleep(1);
		report.startStep("Check See Text tool displayed in Explore");
		learningArea.checkThatSeeTextIsDisplayed();

	}

	@Test
	@TestCaseParams(testCaseID = { "24157" })
	public void testResToolsFuncSeeExplanation() throws Exception {
		
		report.startStep("Get user and login");
		getUserAndLoginNewUXClass();
		
		report.startStep("Navigate to LA");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "I2", 5, 4);
		sleep(2);

		report.startStep("Click on see explanation");
		learningArea.clickOnSeeExplanation();
		sleep(2);
		learningArea.checkThatSeeExplanationIsOpen();

	}

	@Test
	@TestCaseParams(testCaseID = { "24158"})
	public void testResToolsFuncMainIdea() throws Exception {
		
		report.startStep("Get user and login");
		getUserAndLoginNewUXClass();
		homePage.navigateToCourseUnitAndLesson(courseCodes, "A3", 5, 2);
		sleep(3);

		report.startStep("Click Main idea");
		learningArea.clickOnMainIdea();
		learningArea.checkThatMainIdeaIsHighlighted("\"I'm sorry", "help.\"");

		report.startStep("Click again on Main idea to hide it");

		learningArea.clickOnMainIdea();

		learningArea
				.checkThatMainIdeaIsNotHighlighted("\"I'm sorry", "help.\"");

	}

	@Test
	@TestCaseParams(testCaseID = { "24159"})
	public void testResToolsFuncKeyWords() throws Exception {
		
		String keyWordToTest = "Detective";
		
		report.startStep("Get user and login");
		getUserAndLoginNewUXClass();
		homePage.navigateToCourseUnitAndLesson(courseCodes, "A3", 5, 2);
		sleep(2);

		report.startStep("Click on keywords and check");
		learningArea.clickOnKeyWords();
		sleep(2);
		
		learningArea.checkThatSpecificKeyWordIsHighlighted(keyWordToTest);

		learningArea.clickOnWordByText(keyWordToTest);

		learningArea.checkThatKeyWordOpupOpens(keyWordToTest);

		learningArea.closeKeyWorPopup();
		sleep(2);

		report.startStep("Click the Keywords button again and check that text is not highlighted");
		learningArea.clickOnKeyWords();
		learningArea.checkThatSpecificKeyWordIsNotHighlighted(keyWordToTest);
	}

	@Test
	@TestCaseParams(testCaseID = { "24160" })
	public void testResToolsFuncReferenceWords() throws Exception {
		report.startStep("Get user and login");
		getUserAndLoginNewUXClass();
		sleep(1);
		homePage.navigateToCourseUnitAndLesson(courseCodes, "A3", 5, 2);

		sleep(2);
		report.startStep("Click on refrence words");
		learningArea.clickOnReferenceWords();
	}

	@Test
	@TestCaseParams(testCaseID = { "23643" })
	public void testResToolsFuncSeeText() throws Exception {
		report.startStep("Get user and login");
		getUserAndLoginNewUXClass();
		homePage.navigateToRequiredCourseOnHomePage("First Discoveries");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "A3", 6, 1);

		report.startStep("Click on see text");
		learningArea.clickOnSeeText();
		learningArea.checkThatTextDisplayed(true);

		report.startStep("Click to hide text");
		learningArea.clickOnSeeText();
		learningArea.checkThatTextDisplayed(false);
		

		report.startStep("Move to practice and check See Text");
		learningArea.clickOnStep(2);
		learningArea.clickOnSeeText();
		learningArea.checkThatTextDisplayed(true);
		sleep(1);

		report.startStep("Click to hide text");
		learningArea.clickOnSeeText();
		learningArea.checkThatTextDisplayed(false);
		
		report.startStep("Move to Test and check See Text & Text not displayed");
		learningArea.clickOnStep(3);
		learningArea.clickOnStartTest();
		sleep(2);
		learningArea.checkSeeTextDisplayed(false);
	
		report.startStep("Check See Text in A1, unit 1 , Lesosn 1");
		//learningArea.clickToOpenNavigationBar();
		learningArea.clickOnHomeButton();
		learningArea.approveTest();
		sleep(3);
		
		homePage.navigateToCourseUnitAndLesson(courseCodes, "A1", 1, 1);
		sleep(2);
		learningArea.clickOnSeeText();
		
		learningArea.checkThatTextDisplayed(true);

		report.startStep("Click to hide text");
		learningArea.clickOnSeeText();
		learningArea.checkThatTextDisplayed(false);

		report.startStep("Move to practice and check See Text");
		learningArea.clickOnStep(2);		
		learningArea.clickOnSeeText();
		learningArea.checkThatTextDisplayed(true);
		sleep(1);
	
		report.startStep("Click to hide text");
		learningArea.clickOnSeeText();
		learningArea.checkThatTextDisplayed(false);
		
		report.startStep("Move to Test and check See Text & Text not displayed");
		
		learningArea.clickOnStep(3);
		learningArea.clickOnStartTest();
		sleep(2);
		learningArea.checkSeeTextDisplayed(false);
		learningArea.checkThatTextDisplayed(false);
						
		report.startStep("Navigate to FD, unit 3 , lesson 2");
		//learningArea.clickToOpenNavigationBar();
		learningArea.clickOnHomeButton();
		learningArea.approveTest();
		
		homePage.navigateToCourseUnitAndLesson(courseCodes, "FD", 3, 2);
		sleep(2);
		
		report.startStep("Click on see text");
		learningArea.clickOnStep(2);
		sleep(1);
		learningArea.clickOnSeeText();
		sleep(1);
		learningArea.checkThatTextDisplayed(true);

		report.startStep("Click on hide text");
		learningArea.clickOnSeeText();
		sleep(1);
		learningArea.checkThatTextDisplayed(false);

		report.startStep("Move to Practice and click on see text");
		learningArea.clickOnStep(3);
		sleep(1);
		
		report.startStep("Click on see text");
		learningArea.clickOnSeeText();
		sleep(1);
		learningArea.checkThatTextDisplayed(true);

		report.startStep("Click on hide text");
		learningArea.clickOnSeeText();
		sleep(1);
		learningArea.checkThatTextDisplayed(false);
		
	}
	
	@Test
	@TestCaseParams(testCaseID = { "35023","35057","35104","35514" }, skippedBrowsers = {"firefox"}) // Actions API not supported by geckodriver)
	public void testMultipleResourcesFunc() throws Exception {
		
		report.startStep("Init test data");
		String itemCode = "a1roaa";
		
		typesInOrder [0] [0] = Skill.Reading.toString();
		typesInOrder [0] [1] = readingTabTitle_1;
		
		typesInOrder [1] [0] = Skill.Reading.toString();
		typesInOrder [1] [1] = readingTabTitle_2;
		
		typesInOrder [2] [0] = Skill.Listening.toString();
		typesInOrder [2] [1] = listeningAudioTabTitle;
								
		report.startStep("Create user and login");
		createUserAndLoginNewUXClass();
		homePage.closeAllNotifications();
		
		report.startStep("Navigate to A1-U9-L5-S1");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "A1", 9, 5);
		sleep(1);
				
		report.startStep("Verify Multi Resource Set in Step 1");
		verifyMultiRes_3_Sets(itemCode, 1, 1, true);
		
		report.startStep("Verify Multi Resource Set in Step 2");
		learningArea.clickOnNextButton();
		verifyMultiRes_3_Sets(itemCode, 2, 1, false);
		
		report.startStep("Navigate back and check default state");
		sleep(1);
		learningArea.clickOnBackButton();
		verifyMultiResTaskDefaultState(itemCode, 1, 1, typesInOrder);
		
		report.startStep("Check progress not done");		
		learningArea.checkThatTaskIsVisitedAndCurrent(0);
		
		report.startStep("Navigate to Step 4 and Start Test, navigate to Task 2 in Test");
		learningArea.clickOnStep(4);
		learningArea.clickOnStartTest();
		learningArea.clickOnTask(1);
		
		report.startStep("Verify Multi Resource Set in Test");
		verifyMultiRes_3_Sets(itemCode, 4, 2, true);
				
		report.startStep("Select Tab 1");
		learningArea.clickOnMultiResTab(1);
		learningArea.verifyMultiResTabSelected(1);
				
		report.startStep("Submit Test");
		learningArea.submitTest(true);
		
		int counter = 1;
		
		while (counter<=2) {
		
			sleep(1);
			report.startStep("Verify Multi Resource Set after Submit Test");
			verifyMultiRes_3_Sets(itemCode, 4, 2, false);
			
			if (counter == 1) learningArea.clickOnTask(1);
			counter++;
			
		}
		
		
	}
	
	@Test
	@TestCaseParams(testCaseID = { "34869" })
	public void testToggleResourceTools() throws Exception {
		
		report.startStep("Init test data");
		//String itemCode = "i3lafa";
		String itemCode = "b1loac";
		String lessonJS = textService.getTextFromFile("files/lessonFiles/" + itemCode + ".js", Charset.defaultCharset());
		
		report.startStep("Check showTools value for Step 2 / Task 1 in lessonJS");
		testResultService.assertEquals(true, testData.isShowToolsEnabledInLessonJS(2, 1, lessonJS), "Show Tools value in lesson.js is not valid");
								
		report.startStep("Get user and login");
		getUserAndLoginNewUXClass();
		
		report.startStep("Navigate to B1-U9-L6-S2");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "B1", 9, 6);
		sleep(1);
		learningArea.clickOnStep(2);
						
		report.startStep("Verify resource tool displayed");
		learningArea.checkThatSeeTextIsDisplayed();
		
		report.startStep("Check showTools value for Step 1 / Task 1 in lessonJS");
		testResultService.assertEquals(false, testData.isShowToolsEnabledInLessonJS(1, 1, lessonJS), "Show Tools value in lesson.js is not valid");
		
		report.startStep("Navigate to prev step - verify resource tool NOT displayed");
		learningArea.clickOnBackButton();
		sleep(1);
		learningArea.checkThatSeeTextIsNotDisplayed();
			
		report.startStep("Navigate to next step - verify resource tool displayed");
		learningArea.clickOnNextButton();
		sleep(1);
		learningArea.checkThatSeeTextIsDisplayed();
		
		report.startStep("Navigate to prev task verify resource tool NOT displayed");
		learningArea.clickOnBackButton();
		sleep(1);
		learningArea.checkThatSeeTextIsNotDisplayed();
		
		
	}
	
	@Test
	//@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "35133" })
	public void testImageOnlyResourceFunc() throws Exception {
								
		report.startStep("Create user and login");
		getUserAndLoginNewUXClass();
		
		report.startStep("Navigate to B1-U9-L5-S1");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "B1", 9, 5);
		sleep(1);
				
		report.startStep("Verify Image Only Resource Task default state");
		learningArea.verifyImageOnlyResourceDisplayedInTask(imageOnlyImageName);
		//learningArea.verifyImageOnlyTooltip(imageOnlyTooltip); // tooltip removed by design
						
		report.startStep("Verify Image Only Resource has no tools");
		learningArea.checkThatHearAllIsNotDisplayed();
		learningArea.checkThatPrintIsNotDisplayed();
		
		report.startStep("Navigate to next step and back and check default state");
		learningArea.clickOnNextButton();
		sleep(1);
		learningArea.clickOnBackButton();
		
		report.startStep("Verify Image Only Resource Task default state");
		learningArea.verifyImageOnlyResourceDisplayedInTask(imageOnlyImageName);
		//learningArea.verifyImageOnlyTooltip(imageOnlyTooltip); // tooltip removed by design
		
		report.startStep("Check progress not done");		
		learningArea.checkThatTaskIsVisitedAndCurrent(1);
		
	}
		
	private void checkThatTextResourceToolsAreDisplayed() throws Exception {
		learningArea.checkThatHearAllIsDisplayed();
		learningArea.checkThatMainIdeaToolIsDisplayed();
		learningArea.checkThatKeywordsToolIsDisplayed();
		learningArea.checkThatReferenceWordsToolIsDisplayed();
		learningArea.checkThatPrintIsDisplayed();
	}
	
	private void checkThatTextResourceToolsNotDisplayed() throws Exception {
		learningArea.checkThatHearAllIsNotDisplayed();
		learningArea.checkThatMainIdeaToolIsNotDisplayed();
		learningArea.checkThatKeywordsToolIsNotDisplayed();
		learningArea.checkThatReferenceWordsToolIsNotDisplayed();
		learningArea.checkThatPrintIsNotDisplayed();
	}
	
	private void verifyMultiResTaskDefaultState(String itemCode, int stepNum, int taskNum, String [] [] typesInOrder) throws Exception {
	
		report.startStep("Check thumbnail titles & types");
		for (int i = 0; i < typesInOrder.length; i++) {
			learningArea.verifytMultiResTabTitle(i+1, typesInOrder[i][1]);
			learningArea.verifytMultiResTabType(i+1, typesInOrder[i][0]);
		}
		
		report.startStep("Check resourse sets order vs lesson JS");
		String lessonJS = textService.getTextFromFile("files/lessonFiles/" + itemCode + ".js", Charset.defaultCharset());
		testData.verifyMultiResTabOrderFromLessonJS(stepNum, taskNum, typesInOrder, lessonJS);
		
		report.startStep("Verify the left tab is selected");
		learningArea.verifyMultiResTabSelected(1);
		
	}
	
	private void verifyMultiRes_3_Sets(String itemCode, int stepNum, int taskNum, boolean isTestMode) throws Exception {
		
		report.startStep("Verify Multi Resource Task default state");
		verifyMultiResTaskDefaultState(itemCode, stepNum, taskNum, typesInOrder);
			
		report.startStep("Verify resource set 1 loaded");
		learningArea.verifyResourceSetLoadedByType(Skill.Reading, itemCode, isTestMode);
		sleep(1);
		
		report.startStep("Click on resource set 2 and verify it loaded");
		learningArea.clickOnMultiResTab(2);
		learningArea.verifyMultiResTabSelected(2);
		learningArea.verifyResourceSetLoadedByType(Skill.Reading, itemCode, isTestMode);
		sleep(1);
		
		report.startStep("Click on resource set 3 and verify it loaded");
		learningArea.clickOnMultiResTab(3);
		learningArea.verifyMultiResTabSelected(3);
		learningArea.verifyResourceSetLoadedByType(Skill.Listening, itemCode, isTestMode);
		sleep(1);
		
		report.startStep("Check hover on Multi Resource header");
		learningArea.hoverOnMultiResHeaderAndCheckExpand();
		
	}
	
	
	
		
	@After
	public void tearDown() throws Exception {
		report.startStep("Set progress to FD item");
		studentService.createSingleProgressRecored(studentId,"20000", "31515", 10, false, 1);
		super.tearDown();
	}
}
