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
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;

import Enums.ByTypes;
import Enums.Skill;
import pageObjects.edo.NewUXLoginPage;
import pageObjects.edo.NewUxHomePage;
import pageObjects.edo.NewUxLearningArea;
import pageObjects.edo.NewUxLearningArea2;
import testCategories.AngularLearningArea;
import testCategories.NonAngularLearningArea;
import testCategories.inProgressTests;
import testCategories.reg1;
import testCategories.reg2;
import Interfaces.TestCaseParams;
import tests.edo.newux.testDataValidation;

@Category(AngularLearningArea.class)
public class ResourseToolsTests2 extends BasicNewUxTest {

	private static final String readingTabTitle_1 = "Schedule";
	private static final String readingTabTitle_2 = "E-mail";
	private static final String listeningVideoTabTitle = "Show it!";
	private static final String listeningAudioTabTitle = "Listening";
	private static final String imageOnlyTooltip = "Look at the image!";
	private static final String imageOnlyImageName = "b1roab02.jpg";
	private static final String audioimageImageName = "b3teaap03.jpg";
	private static boolean TOEIC = false;
	
	String [] [] typesInOrder = new String [3] [2]; 
	
	NewUxLearningArea2 learningArea2;
	
	testDataValidation testData = new testDataValidation(); 
	Har har;
	
	@Before
	public void setup() throws Exception {
				
		super.setup();
		learningArea2 = new NewUxLearningArea2(webDriver, testResultService);
	}

	@Test
	@TestCaseParams(testCaseID = { "23348" })
	public void resourseToolsGrammarDefaultDisplayPerStep() throws Exception {
		
		report.startStep("Get user and login");
		getUserAndLoginNewUXClass();
		
		report.startStep("Navigate to Grammar Explore Step");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "I2", 5, 4);
		sleep(1);
		learningArea2.clickOnNextButton();

		report.startStep("Check Resource Tools in Explore");
		learningArea2.checkThatSeeExplanationIsDisplayed();
		learningArea2.checkThatPrintIsDisplayed();

		report.startStep("Click on Practice and check Resource Tools");
		learningArea2.clickOnStep(2);
		learningArea2.checkThatSeeExplanationIsDisplayed();
		learningArea2.checkThatPrintIsDisplayed();

		report.startStep("Click on Test and check Resource Tools");
		learningArea2.clickOnStep(3, false);
		learningArea2.clickOnStartTest();
		learningArea2.checkThatSeeExplanationIsNotDisplayed();
		learningArea2.checkThatPrintIsNotDisplayed();

	}

	@Test
	@TestCaseParams(testCaseID = { "23351" ,"23648","23644","23647","23646"})
	public void testResToolsDefaultTextResources()
			throws Exception {
		
		report.startStep("Create user and login");
		createUserAndLoginNewUXClass();
		homePage.closeAllNotifications();
		homePage.waitHomePageloadedFully();
		
		
		report.startStep("Navigate to A3-U5-L2");
		homePage.navigateToRequiredCourseOnHomePage("First Discoveries");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "A3", 5, 2);
		learningArea2.waitToLearningAreaLoaded();
		
		learningArea2.clickOnNextButton();
		
		report.startStep("Check Resource Tools");
		checkThatTextResourceToolsAreDisplayed("Explore");
		sleep(2);

		report.startStep("Click on Practice");
		learningArea2.clickOnStep(2);
		checkThatTextResourceToolsAreDisplayed("Practice");
		sleep(2);
		
		report.startStep("Click on Test");
		learningArea2.clickOnStep(3, false);
		learningArea2.clickOnStartTest();
		sleep(1);
		checkThatTextResourceToolsNotDisplayed("Test");
		
		report.startStep("Submit test and check resource tools displayed");
		learningArea2.submitTest(true);
		webDriver.switchToTopMostFrame();
		//checkThatTextResourceToolsAreDisplayed("Test review score");
		checkThatTextResourceToolsNotDisplayed("Test review score");
		sleep(1);
		
		report.startStep("Press on Task 1 and check resource tools displayed");
		learningArea2.clickOnReviewTestResults();
		//checkThatTextResourceToolsAreDisplayed("Test review answers");
		checkThatTextResourceToolsNotDisplayed("Test review score");
		
		report.startStep("Go to FD, unit 2, lesson 4");
		//homePage.clickToOpenNavigationBar();
		homePage.clickOnHomeButton();
		homePage.waitHomePageloadedFully();
		
		homePage.carouselNavigateNext();
		homePage.navigateToCourseUnitAndLesson(courseCodes, "FD", 2, 4);
		sleep(3);
		learningArea2.clickOnStep(2, false);
		learningArea2.checkThatHearAllIsDisplayed();
		learningArea2.checkThatPrintIsDisplayed();

		report.startStep("Click on Pracice");
		learningArea2.clickOnStep(3, false);
		learningArea2.checkThatHearAllIsDisplayed();
		learningArea2.checkThatPrintIsDisplayed();
		sleep(2);
		
		report.startStep("Go to FD, unit 3, lesson 1");
		//learningArea2.clickToOpenNavigationBar();
		learningArea2.clickOnHomeButton();
		homePage.waitHomePageloaded();
		
		homePage.navigateToCourseUnitAndLesson(courseCodes, "FD", 3, 1);
		sleep(1);
		learningArea2.clickOnStep(2);
		learningArea2.clickOnSeeText();
		learningArea2.checkThatPrintIsDisplayed();
		sleep(1);

		learningArea2.clickOnStep(3);
		learningArea2.clickOnSeeText();
		learningArea2.checkThatPrintIsDisplayed();

	}

	@Test
	@TestCaseParams(testCaseID = { "23352","34456" })
	public void testResToolsDefaultAudioResources()
			throws Exception {
		
		report.startStep("Get user and login");
		getUserAndLoginNewUXClass();
	//	homePage.closeAllNotifications();
	//	homePage.waitHomePageloadedFully();
		
		report.startStep("Navigate to A3-U6-L1-S1-T1");
		learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "A3", 6, 1);
		learningArea2.waitToLearningAreaLoaded();
		
		learningArea2.clickOnNextButton();
		
		report.startStep("Check See Text tool displayed in Explore");
		learningArea2.checkThatSeeTextIsDisplayed();
		
		report.startStep("Check See Text tool displayed in Practice");
		learningArea2.clickOnStep(2);
		learningArea2.checkThatSeeTextIsDisplayed();

		report.startStep("Check See Text tool NOT displayed in Test");
		learningArea2.clickOnStep(3, false);
		learningArea2.clickOnStartTest();
		learningArea2.checkThatTextDisplayed(false);

		report.startStep("Submit test and check no resource tools displayed");
		learningArea2.submitTest(true);
		learningArea2.checkThatTextDisplayed(false);
		sleep(1);
		
		report.startStep("Press on Task 1 and check resource tools displayed");
		learningArea2.clickOnReviewTestResults();
		learningArea2.checkThatTextDisplayed(true);
		
		report.startStep("Go to Basic 3, Eating out, Japanese restorant, Step 1 - Task 1");
		//homePage.clickToOpenNavigationBar();
		homePage.clickOnHomeButton();
		homePage.waitHomePageloaded();
		
		homePage.navigateToCourseUnitAndLesson(courseCodes, "B3", 4, 3);
		sleep(1);
		learningArea2.clickOnNextButton();
		
		report.startStep("Check See Text tool displayed in Explore");
		learningArea2.checkThatSeeTextIsDisplayed();

	}

	@Test
	@TestCaseParams(testCaseID = { "24157" })
	public void testResToolsFuncSeeExplanation() throws Exception {
		
		report.startStep("Get user and login");
		getUserAndLoginNewUXClass();
	//	homePage.closeAllNotifications();
	//	homePage.waitHomePageloadedFully();
		
		report.startStep("Navigate to LA Grammar Task");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "I2", 5, 4);
		learningArea2.waitToLearningAreaLoaded();
		learningArea2.clickOnNextButton();
		
		homePage.clickToOpenNavigationBar();
		sleep(1);
		homePage.clickToOpenNavigationBar();
		
		report.startStep("Click on see explanation");
		learningArea2.clickOnSeeExplanation();
		sleep(2);
		learningArea2.checkThatSeeExplanationIsOpen();

	}

	@Test
	@TestCaseParams(testCaseID = { "24158"})
	public void testResToolsFuncMainIdea() throws Exception {
		
		report.startStep("Get user and login");
		getUserAndLoginNewUXClass();
	//	homePage.closeAllNotifications();
	//	homePage.waitHomePageloadedFully();
		
		report.startStep("Navigate to LA Reading Task: A3, 5, 2");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "A3", 5, 2);
		learningArea2.waitToLearningAreaLoaded();
		learningArea2.clickOnNextButton();

		report.startStep("Click Main idea");
		learningArea2.clickOnMainIdea();
		learningArea2.checkThatMainIdeaIsHighlighted("\"I'm sorry", "help.\"");

		report.startStep("Click again on Main idea to hide it");

		learningArea2.clickOnMainIdea();

		learningArea2.checkThatMainIdeaIsNotHighlighted("\"I'm sorry", "help.\"");

	}

	@Test
	@TestCaseParams(testCaseID = { "24159"})
	public void testResToolsFuncKeyWords() throws Exception {
		
		String keyWordToTest = "Detective";
		
		report.startStep("Get user and login");
		getUserAndLoginNewUXClass();
	//	homePage.closeAllNotifications();
	//	homePage.waitHomePageloadedFully();
		
		report.startStep("Navigate to LA Reading Task A3, 5, 2");
		learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "A3", 5, 2);
		learningArea2.waitToLearningAreaLoaded();
		
		learningArea2.clickOnNextButton(1);

		report.startStep("Click on keywords and check");
		learningArea2.clickOnKeyWords();
		sleep(1);
		
		learningArea2.checkThatSpecificKeyWordIsHighlighted(keyWordToTest);

		learningArea2.clickOnWordByText(keyWordToTest);

		learningArea2.checkThatKeyWordOpupOpens(keyWordToTest);

		learningArea2.closeKeyWorPopup();
		sleep(1);

		report.startStep("Click the Keywords button again and check that text is not highlighted");
		learningArea2.clickOnKeyWords();
		learningArea2.checkThatSpecificKeyWordIsNotHighlighted(keyWordToTest);
	}

	@Test
	@TestCaseParams(testCaseID = { "24160" })
	public void testResToolsFuncReferenceWords() throws Exception {
		
		report.startStep("Get user and login");
		getUserAndLoginNewUXClass();
	//	homePage.closeAllNotifications();
	//	homePage.waitHomePageloadedFully();
		
		report.startStep("Navigate to LA Reading Task");
		learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "A3", 5, 2);
		learningArea2.waitToLearningAreaLoaded();
		learningArea2.clickOnNextButton();

		report.startStep("Click on refrence words");
		learningArea2.clickOnReferenceWords();
	}

	@Test
	@TestCaseParams(testCaseID = { "23643" })
	public void testResToolsFuncSeeText() throws Exception {
		
		report.startStep("Get user and login");
		getUserAndLoginNewUXClass();
	//	homePage.closeAllNotifications();
	//	homePage.waitHomePageloadedFully();
		
		report.startStep("Navigate To Task with See Text tool A3-U6-L1");
		//homePage.navigateToRequiredCourseOnHomePage("First Discoveries");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "A3", 6, 1);
		learningArea2.waitToLearningAreaLoaded();
		learningArea2.clickOnNextButton();

		report.startStep("Click on see text");
		learningArea2.clickOnSeeText();
		learningArea2.checkThatTextDisplayed(true);

		report.startStep("Click to hide text");
		learningArea2.clickOnSeeText();
		learningArea2.checkThatTextDisplayed(false);

		report.startStep("Move to practice and check See Text");
		learningArea2.clickOnStep(2);
		learningArea2.clickOnSeeText();
		learningArea2.checkThatTextDisplayed(true);

		report.startStep("Click to hide text");
		learningArea2.clickOnSeeText();
		learningArea2.checkThatTextDisplayed(false);
		
		report.startStep("Move to Test and check See Text & Text not displayed");
		learningArea2.clickOnStep(3, false);
		learningArea2.clickOnStartTest();
		sleep(2);
		learningArea2.checkSeeTextDisplayed(false);
	
		report.startStep("Check See Text in A1, unit 1 , Lesosn 1");
		//learningArea2.clickToOpenNavigationBar();
		learningArea2.clickOnHomeButton();
		learningArea2.approveTest();
		homePage.waitHomePageloadedFully();
		
		homePage.navigateToCourseUnitAndLesson(courseCodes, "A1", 1, 1);
		learningArea2.waitToLearningAreaLoaded();
		learningArea2.clickOnNextButton();
		learningArea2.clickOnSeeText();
		learningArea2.checkThatTextDisplayed(true);

		report.startStep("Click to hide text");
		learningArea2.clickOnSeeText();
		learningArea2.checkThatTextDisplayed(false);

		report.startStep("Move to practice and check See Text");
		learningArea2.clickOnStep(2);
		learningArea2.clickOnSeeText();
		learningArea2.checkThatTextDisplayed(true);
	
		report.startStep("Click to hide text");
		learningArea2.clickOnSeeText();
		learningArea2.checkThatTextDisplayed(false);
		
		report.startStep("Move to Test and check See Text & Text not displayed");
		learningArea2.clickOnStep(3, false);
		learningArea2.clickOnStartTest();
		sleep(2);
		learningArea2.checkSeeTextDisplayed(false);
		learningArea2.checkThatTextDisplayed(false);
						
		report.startStep("Navigate to FD, unit 3 , lesson 2");
		//learningArea2.clickToOpenNavigationBar();
		learningArea2.clickOnHomeButton();
		learningArea2.approveTest();
		homePage.waitHomePageloadedFully();
		
		homePage.navigateToCourseUnitAndLesson(courseCodes, "FD", 3, 2);
		learningArea2.waitToLearningAreaLoaded();
		
		report.startStep("Click on see text");
		learningArea2.clickOnStep(2);
		sleep(1);
		learningArea2.clickOnSeeText();
		sleep(1);
		learningArea2.checkThatTextDisplayed(true);

		report.startStep("Click on hide text");
		learningArea2.clickOnSeeText();
		sleep(2);
		learningArea2.checkThatTextDisplayed(false);

		report.startStep("Move to Practice and click on see text");
		learningArea2.clickOnStep(3);
		sleep(1);
		
		report.startStep("Click on see text");
		learningArea2.clickOnSeeText();
		sleep(1);
		learningArea2.checkThatTextDisplayed(true);

		report.startStep("Click on hide text");
		learningArea2.clickOnSeeText();
		sleep(1);
		learningArea2.checkThatTextDisplayed(false);
		
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
		homePage.waitHomePageloadedFully();
		
		report.startStep("Navigate to A1-U9-L5-S1-T1");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "A1", 9, 5);
		learningArea2.waitToLearningAreaLoaded();
		learningArea2.clickOnNextButton();
				
		report.startStep("Verify Multi Resource Set in Step 1");
		verifyMultiRes_3_Sets(itemCode, 1, 1, true);
		
		report.startStep("Verify Multi Resource Set in Step 2");
		learningArea2.clickOnStep(2);
		verifyMultiRes_3_Sets(itemCode, 2, 1, false);
		
		report.startStep("Navigate back and check default state");
		//sleep(1);
		learningArea2.clickOnStep(1);
		verifyMultiResTaskDefaultState(itemCode, 1, 1, typesInOrder);
		
		//report.startStep("Check progress not done");		
		//learningArea2.checkThatTaskIsVisitedAndCurrent(0);
		
		report.startStep("Navigate to Step 4 and Start Test, navigate to Task 2 in Test");
		learningArea2.clickOnStep(4, false);
		learningArea2.clickOnStartTest();
		learningArea2.clickOnTaskByNumber(2);
		
		report.startStep("Verify Multi Resource Set in Test");
		verifyMultiRes_3_Sets(itemCode, 4, 2, true);
				
		report.startStep("Select Tab 1");
		learningArea2.clickOnMultiResTab(1);
		learningArea2.verifyMultiResTabSelected(1);
				
		report.startStep("Submit Test");
		learningArea2.submitTest(true);
		
		int counter = 1;
		
		while (counter<2) {
		
			sleep(1);
			report.startStep("Verify Multi Resource Set after Submit Test");
			verifyMultipleResourcesNotDisplay();
			
			if (counter == 1) {
				report.startStep("Verify Multi Resource in Review");
				learningArea2.clickOnReviewTestResults();
				learningArea2.clickOnTaskByNumber(2);
				verifyMultiRes_3_Sets(itemCode, 4, 2, false);
			}
			counter++;
			
		}
		
		
	}
	
	private void verifyMultipleResourcesNotDisplay() throws Exception {

		WebElement element=null;
		Boolean actual=false;
		
		element = webDriver.waitUntilElementAppears("learning__RAMultiResTabsW", ByTypes.className, 1);
		
		if (element!=null)
			actual=true;
		
		testResultService.assertEquals(false, actual);
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
	//	homePage.closeAllNotifications();
	//	homePage.waitHomePageloadedFully();
		
		report.startStep("Navigate to B1-U9-L6-S2");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "B1", 9, 6);
		learningArea2.waitToLearningAreaLoaded();
		
		learningArea2.clickOnStep(2);
						
		report.startStep("Verify resource tool displayed");
		learningArea2.checkThatSeeTextIsDisplayed();
		
		report.startStep("Check showTools value for Step 1 / Task 1 in lessonJS");
		testResultService.assertEquals(false, testData.isShowToolsEnabledInLessonJS(1, 1, lessonJS), "Show Tools value in lesson.js is not valid");
		
		report.startStep("Navigate to prev step - verify resource tool NOT displayed");
		learningArea2.clickOnBackButton(2);
		sleep(1);
		learningArea2.checkThatSeeTextIsNotDisplayed();
			
		report.startStep("Navigate to next step - verify resource tool displayed");
		learningArea2.clickOnNextButton(2);
		sleep(1);
		learningArea2.checkThatSeeTextIsDisplayed();
		
		report.startStep("Navigate to prev task verify resource tool NOT displayed");
		learningArea2.clickOnBackButton(2);
		sleep(1);
		learningArea2.checkThatSeeTextIsNotDisplayed();
		
		
	}
	
	@Test
	@TestCaseParams(testCaseID = { "35133" })
	public void testImageOnlyResourceFunc() throws Exception {
								
		report.startStep("Create user and login");
		getUserAndLoginNewUXClass();
	//	homePage.closeAllNotifications();
	//	homePage.waitHomePageloadedFully();
		
		report.startStep("Navigate to B1-U9-L5-S1-T1");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "B1", 9, 5);
		learningArea2.waitToLearningAreaLoaded();
		
		learningArea2.clickOnNextButton();
				
		report.startStep("Verify Image Only Resource Task default state");
		learningArea2.verifyImageOnlyResourceDisplayedInTask(imageOnlyImageName);
		//learningArea.verifyImageOnlyTooltip(imageOnlyTooltip); // tooltip removed by design
						
		report.startStep("Verify Image Only Resource has no tools");
		learningArea2.checkThatHearAllIsNotDisplayed();
		learningArea2.checkThatPrintIsNotDisplayed();
		
		report.startStep("Navigate to next step and back and check default state");
		learningArea2.clickOnNextButton();
		sleep(1);
		learningArea2.clickOnBackButton();
		
		report.startStep("Verify Image Only Resource Task default state");
		learningArea2.verifyImageOnlyResourceDisplayedInTask(imageOnlyImageName);
		//learningArea.verifyImageOnlyTooltip(imageOnlyTooltip); // tooltip removed by design
		
		report.startStep("Check progress not done");		
		learningArea2.checkThatTaskIsVisitedAndCurrent(1);
		
	}
	
	@Test
	@TestCaseParams(testCaseID = { "56970" })
	public void testAudioImageResourceFunc() throws Exception {
								
		report.startStep("init Test Data");
			TOEIC = true;
			String courseName = coursesNamesTOEIC[0];
			String courseId = coursesTOEIC[0];
			String courseCode = courseCodesTOEIC[0];
			String itemId = "1547068";
			String className = "EDTOEIC";
			int taskNum = 3; 
			String studentId = pageHelper.createUSerUsingSP(
			configuration.getInstitutionId(), className);
		
		report.startStep("Login with new user");
			String userName = dbService.getUserNameById(studentId,
				configuration.getInstitutionId());
			NewUXLoginPage loginPage = new NewUXLoginPage(webDriver,testResultService);
			NewUxHomePage homePage = loginPage.loginAsStudent(userName, "12345");
			homePage.closeAllNotifications();
			homePage.waitHomePageloadedFully();
			
		report.startStep("Navigate to new Resource type");
			learningArea2 = homePage.navigateToCourseUnitLessonLA2TOEIC(courseCodesTOEIC, courseCode, 1, 4, false, courseName);
			learningArea2.waitUntilLearningAreaLoaded();
			
			learningArea2.clickOnStep(2,false);
			sleep(1);
			learningArea2.clickOnTaskByNumber(taskNum);
				
		report.startStep("Verify Image Only Resource Task default state");
			learningArea2.verifyAudioImageResourceDisplayedInTask(audioimageImageName);
						
		report.startStep("Verify Image Only Resource has no tools");
			learningArea2.checkThatHearAllIsNotDisplayed();
			learningArea2.checkThatPrintIsNotDisplayed();
		
		report.startStep("Verify Audio tracker exists and is in ready state");
			sleep(2);
			WebElement playButton = webDriver.waitForElement("CTrackerPlayBtn", ByTypes.id);
			testResultService.assertEquals(true, playButton.getAttribute("class").contains("mediaPlayPause--play"));
			
		report.startStep("Press play and see button status change");
			boolean eanabled = playButton.isEnabled();
			boolean display = playButton.isDisplayed();
			report.addTitle("Play buttons is Enabled =" + eanabled + "and Display=" + display );
			
			if (eanabled && display){	
				playButton.click();
			}
			else
			{
				sleep(10);
				eanabled = playButton.isEnabled();
				display = playButton.isDisplayed();
				report.addTitle("Play buttons is Enabled =" + eanabled + "and Display=" + display );
				playButton.click();
			}
					
			report.report("Clicked on play button");
			sleep(1);
			playButton = webDriver.waitForElement("CTrackerPlayBtn", ByTypes.id);
			testResultService.assertEquals(true, playButton.getAttribute("class").contains("mediaPlayPause--pause"));
		
		report.startStep("Make sure no progress is logged for the user");
			learningArea2.openTaskBar();
			learningArea2.checkIfTaskHasDoneState(taskNum, false);
			learningArea2.closeTaskBar();
			studentService.checkStudentHasNoProgress(studentId, courseId, itemId);
			
	}
	
	@Test
	@TestCaseParams(testCaseID = { "44927" })
	public void testViewResourceFunc() throws Exception {
				
		report.startStep("Set browser to min supported resolution");
		webDriver.setBrowserResolution(1024, 788);
		
		report.startStep("Get user and login");
		getUserAndLoginNewUXClass();
	//	homePage.closeAllNotifications();
	//	homePage.waitHomePageloadedFully();

		homePage.clickToOpenNavigationBar();
		report.startStep("Navigate to A1-U1-L1-S2-T4");
		homePage.navigateToTask("A1", 1, 1, 2, 4);
		learningArea2.waitToLearningAreaLoaded();
				
		report.startStep("Press View Resource");
		learningArea2.clickOnViewResource();
		
		report.startStep("Check View Resource btn pressed");
		learningArea2.checkViewResourceNotClickable();
		
		report.startStep("Check See Text tool displayed and clickable");
		learningArea2.checkThatSeeTextIsDisplayed();
		learningArea2.clickOnSeeText();
		
		report.startStep("Check any practice tool NOT DISPLAYED");
		learningArea2.checkThatClearToolIsNotDisplayed();
			
		report.startStep("Close Resource Layer");
		learningArea2.closeResourceLayer();
							
		report.startStep("Check any practice tool DISPLAYED");
		learningArea2.checkThatClearToolIsDisplayed();
		
		report.startStep("Maximize browser and check view resource is not clickable");
		webDriver.maximize();
		sleep(3);  // igb 2018.11.19
		
		learningArea2.checkViewResourceNotClickable(); 
		
		report.startStep("Check See Text tool displayed and clickable");
		learningArea2.checkThatSeeTextIsDisplayed();
		learningArea2.clickOnSeeText();
		
		report.startStep("Check any practice tool DISPLAYED");
		learningArea2.checkThatClearToolIsDisplayed();
				
	}
	
	@Test
	@TestCaseParams(testCaseID = { "44927" })
	public void testViewResourceFuncTestMode() throws Exception {
				
		report.startStep("Set browser to min supported resolution");
		webDriver.setBrowserResolution(1024, 788);
		
		report.startStep("Get user and login");
		getUserAndLoginNewUXClass();
	//	homePage.closeAllNotifications();
	//	homePage.waitHomePageloadedFully();
		
		report.startStep("Navigate to A1-U9-L1-S6-T2");
		learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "A1", 9, 1);
		learningArea2.waitToLearningAreaLoaded();
		
		learningArea2.clickOnStep(6, false);
		learningArea2.clickOnStartTest();
		learningArea2.clickOnTaskByNumber(2);
				
		report.startStep("Press View Resource");
		learningArea2.clickOnViewResource();
		sleep(1);		
		
		report.startStep("Close Resource Layer");
		learningArea2.closeResourceLayer();
		sleep(1);
		
		report.startStep("Maximize browser and check view resource is not clickable");
		webDriver.maximize();
		sleep(1);  // igb 2018.11.19
		
		learningArea2.checkViewResourceNotClickable();
		
		report.startStep("Submit Test");
		learningArea2.submitTest(true);
		
		report.startStep("Press on Review and navigate to Task 2");
		learningArea2.clickOnReviewTestResults();
		learningArea2.clickOnTaskByNumber(2);
		
		report.startStep("Set browser to min supported resolution");
		webDriver.setBrowserResolution(1024, 788);	
		sleep(2);
		
		report.startStep("Press View Resource");
		learningArea2.clickOnViewResource();
		sleep(2);		
		
		report.startStep("Close Resource Layer");
		learningArea2.closeResourceLayer();
		
		report.startStep("Maximize browser and check view resource is not clickable");
		webDriver.maximize();
		sleep(1);  // igb 2018.11.19
		
		learningArea2.checkViewResourceNotClickable();
		
	}
		
	private void checkThatTextResourceToolsAreDisplayed(String stepName) throws Exception {
		
		report.startStep("Checking the Resource display in:" + stepName);
		learningArea2.checkThatHearAllIsDisplayed();
		learningArea2.checkThatMainIdeaToolIsDisplayed();
		learningArea2.checkThatKeywordsToolIsDisplayed();
		learningArea2.checkThatReferenceWordsToolIsDisplayed();
		learningArea2.checkThatPrintIsDisplayed();
	}
	
	private void checkThatTextResourceToolsNotDisplayed(String stepName) throws Exception {
		report.startStep("Checking the Resource display in:" + stepName);
		learningArea2.checkThatHearAllIsNotDisplayed();
		learningArea2.checkThatMainIdeaToolIsNotDisplayed();
		learningArea2.checkThatKeywordsToolIsNotDisplayed();
		learningArea2.checkThatReferenceWordsToolIsNotDisplayed();
		learningArea2.checkThatPrintIsNotDisplayed();
	}
	
	private void verifyMultiResTaskDefaultState(String itemCode, int stepNum, int taskNum, String [] [] typesInOrder) throws Exception {
	
		report.startStep("Check thumbnail titles & types");
		for (int i = 0; i < typesInOrder.length; i++) {
			learningArea2.verifytMultiResTabTitle(i+1, typesInOrder[i][1]);
			learningArea2.verifytMultiResTabType(i+1, typesInOrder[i][0]);
		}
		
		report.startStep("Check resourse sets order vs lesson JS");
		String lessonJS = textService.getTextFromFile("files/lessonFiles/" + itemCode + ".js", Charset.defaultCharset());
		testData.verifyMultiResTabOrderFromLessonJS(stepNum, taskNum, typesInOrder, lessonJS);
		
		report.startStep("Verify the left tab is selected");
		learningArea2.verifyMultiResTabSelected(1);
		
	}
	
	private void verifyMultiRes_3_Sets(String itemCode, int stepNum, int taskNum, boolean isTestMode) throws Exception {
		
		report.startStep("Verify Multi Resource Task default state");
		verifyMultiResTaskDefaultState(itemCode, stepNum, taskNum, typesInOrder);
			
		report.startStep("Verify resource set 1 loaded");
		learningArea2.verifyResourceSetLoadedByType(Skill.Reading, itemCode, isTestMode);
		//sleep(1);
		
		report.startStep("Click on resource set 2 and verify it loaded");
		learningArea2.clickOnMultiResTab(2);
		learningArea2.verifyMultiResTabSelected(2);
		learningArea2.verifyResourceSetLoadedByType(Skill.Reading, itemCode, isTestMode);
		//sleep(1);
		
		report.startStep("Click on resource set 3 and verify it loaded");
		learningArea2.clickOnMultiResTab(3);
		learningArea2.verifyMultiResTabSelected(3);
		learningArea2.verifyResourceSetLoadedByType(Skill.Listening, itemCode, isTestMode);
		//sleep(1);
		
		report.startStep("Check hover on Multi Resource header");
		learningArea2.hoverOnMultiResHeaderAndCheckExpand();
		
	}
	
	
	
	@After
	public void tearDown() throws Exception {
		/*report.startStep("Set progress to FD item");
		if (!TOEIC){
			studentService.createSingleProgressRecored(studentId,"20000", "31515", 10, false, 1);
		}
		*/
		super.tearDown();
	}
}
