package tests.edo.newux;

import org.junit.After;
import org.junit.Before;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import Enums.ByTypes;
import Interfaces.TestCaseParams;
import pageObjects.edo.NewUXLoginPage;
import pageObjects.edo.NewUxHomePage;
import pageObjects.edo.NewUxLearningArea2;
import services.PageHelperService;
import testCategories.AngularLearningArea;
import testCategories.inProgressTests;


@Category(AngularLearningArea.class)
public class CabaProject extends BasicNewUxTest {
	
	NewUXLoginPage loginPage;
	String banner;
	NewUxLearningArea2 learningArea2;

	private static final String ErrorUserName = "siteLogin__messageText";
	private static final String NewMcq = "Choose two correct answers.";
	private static final String NewMcqTest = "Choose the correct answer.";
	private static final String NewMcqMediaTest = "Listen and choose the correct answer.";
	protected String studentId;
	
	
	@Before
	public void setup() throws Exception {
		super.setup();
	
	}
	
private void closeBrowserAndOpenAgain() throws Exception {
		
		webDriver.quitBrowser();
		sleep(2);
		webDriver.init();
		sleep(2);
		webDriver.maximize();
		sleep(2);	
	}
	

// test for Caba Multiple image answer

@Test
@TestCaseParams(testCaseID = { "48501" })
public void CabaMcqWithImage() throws Exception {
	
	String correctAnswer = "question-1_answer-3";
	String wrongAnswer = "question-1_answer-1";
	
    report.startStep("Create new student");
	String className = configuration.getProperty("classname.caba");
	studentId = pageHelper.createUSerUsingSP(institutionId, className);
	userName = dbService.getUserNameById(studentId,institutionId);

	
	report.startStep("Login with new student");
	NewUXLoginPage loginPage = new NewUXLoginPage(webDriver,
			testResultService);
	NewUxHomePage homePage = loginPage.loginAsStudent(userName, "12345");
	homePage.waitHomePageloadedFully();
	
	
	report.startStep("Navigate FDP-U2-L6-S2-T1");
	learningArea2 = homePage.navigateToCABATask("EDP", 2, 6, 2, 1);
	learningArea2.waitUntilLearningAreaLoaded();
	
	report.startStep("Verify instruction text");
	learningArea2.VerificationOfQuestionInstruction(NewMcq);

report.startStep("Select answers. Click on Clear and check  answers selected");
    learningArea2.SelectRadioBtn(correctAnswer);
    sleep(2);
	learningArea2.clickOnClearAnswer();
	learningArea2.clickOnSeeAnswer();
	sleep(1);
	learningArea2.clickOnSeeAnswer();
	
report.startStep("Select 1 correct click on Check Answer and check V/X signs placed correctly");
    learningArea2.SelectRadioBtn(correctAnswer);
    sleep(2);
	learningArea2.clickOnCheckAnswer();
	sleep(2);
	learningArea2.CorrectnessVMCQ();
	learningArea2.clickOnClearAnswer();
	
report.startStep("Select 1 wromg click on Check Answer and check V/X signs placed correctly");
    learningArea2.SelectRadioBtn(wrongAnswer);
	learningArea2.clickOnCheckAnswer();
	learningArea2.CorrectnessXMCQ();
	sleep(1);

report.report("Verify Practice Tools State");
	learningArea2.verifyPracticeToolsStateOnCheckAnswer();
	learningArea2.clickOnCheckAnswer();
	
report.startStep("Check next selection unselect current");
   learningArea2.SelectRadioBtn(correctAnswer);
   sleep(1);
   learningArea2.SelectRadioBtn(wrongAnswer);

}

@Test
@TestCaseParams(testCaseID = { "48501" })
public void CabaMcqWithImage_TestMode() throws Exception {
	
	String expectedScore = "20%";
	String correctAnswer = "question-1_answer-3";
	int taskNum=2;
	
	
    report.startStep("Create new student");
	String className = configuration.getProperty("classname.caba");
	studentId = pageHelper.createUSerUsingSP(institutionId, className);

	userName = dbService.getUserNameById(studentId,institutionId);

	report.startStep("Login with new student");
	NewUXLoginPage loginPage = new NewUXLoginPage(webDriver,
			testResultService);
	NewUxHomePage homePage = loginPage.loginAsStudent(userName, "12345");
	homePage.waitHomePageloadedFully();
	
	report.startStep("Navigate FDP-U8-L1-T4");
	learningArea2 = homePage.navigateToCourseUnitLessonLA2CABA(courseCodesCABA, "EDP", 8, 1);
	learningArea2.waitToLearningAreaLoaded();
	
	learningArea2.clickOnStep(4, false);
	learningArea2.clickOnStartTest();
	learningArea2.clickOnTaskByNumber(taskNum);
	
	report.startStep("Verify instruction text");
	learningArea2.VerificationOfQuestionInstruction(NewMcqTest);
	
report.startStep("Select answers, navigate to other task and back and check correct answer is selected");
    learningArea2.checkAllAnswersUnselectedMCQ();
    learningArea2.SelectRadioBtn(correctAnswer);	
	sleep (1);
	learningArea2.clickOnBackButton();
	learningArea2.clickOnNextButton();
	
	
report.startStep("Submit test and check score");
	learningArea2.submitTest(true);
	String score = learningArea2.getTestScore();
	testResultService.assertEquals(expectedScore, score, "Checking Test Score validity");
	
report.startStep("Press on Review and open task bar");
	learningArea2.clickOnReviewTestResults();
	learningArea2.openTaskBar();
	
report.startStep("Check MCQ task indication is correct");
learningArea2.checkTestTaskMark(taskNum, true);
	learningArea2.closeTaskBar();
	learningArea2.clickOnTaskByNumber(taskNum);
		
report.startStep("Check Your Answer Tab");
	learningArea2.clickOnYourAnswerTab();
	learningArea2.CorrectnessVMCQ();

report.startStep("Check Correct Answer Tab");
	learningArea2.clickOnCorrectAnswerTab();


}

@Test
@TestCaseParams(testCaseID = { "48501" })
public void CabaMcqMedia_TestMode() throws Exception {
	
	String expectedScore = "20%";
	String correctAnswer = "question-1_answer-1";
	int taskNum=3;
	
	
    report.startStep("Create new student");
	String className = configuration.getProperty("classname.caba");
	studentId = pageHelper.createUSerUsingSP(institutionId, className);

	String userName = dbService.getUserNameById(studentId,institutionId);

	report.startStep("Login with new student");
	NewUXLoginPage loginPage = new NewUXLoginPage(webDriver,
			testResultService);
	NewUxHomePage homePage = loginPage.loginAsStudent(userName, "12345");
	homePage.waitHomePageloadedFully();
	
	
	report.startStep("Navigate FDP-U8-L1-T4");
	learningArea2 = homePage.navigateToCourseUnitLessonLA2CABA(courseCodes, "EDP", 8, 1);
	learningArea2.waitUntilLearningAreaLoaded();
	
	learningArea2.clickOnStep(4, false);
	learningArea2.clickOnStartTest();
	learningArea2.clickOnTaskByNumber(taskNum);
	sleep(2); //wait the correct instruction will display
	
	report.startStep("Verify instruction text");
	learningArea2.VerificationOfQuestionInstruction(NewMcqMediaTest);
	
	report.startStep("Select answers, navigate to other task and back and check correct answer is selected");
    learningArea2.checkAllAnswersUnselectedMCQ();
    learningArea2.SelectRadioBtn(correctAnswer);	
	sleep (1);
	learningArea2.sound.click();
	learningArea2.checkAudioFile("AngularMediaPlayer", "fdvoaht103.mp3");
	learningArea2.clickOnBackButton();
	learningArea2.clickOnNextButton();
	
	
	report.startStep("Submit test and check score");
	learningArea2.submitTest(true);
	String score = learningArea2.getTestScore();
	testResultService.assertEquals(expectedScore, score, "Checking Test Score validity");
	
	report.startStep("Press on Review and open task bar");
	learningArea2.clickOnReviewTestResults();
	learningArea2.openTaskBar();
	
	report.startStep("Check MCQ task indication is correct");
	learningArea2.checkTestTaskMark(taskNum, true);
	learningArea2.closeTaskBar();
	learningArea2.clickOnTaskByNumber(taskNum);
		
	report.startStep("Check Your Answer Tab");
	learningArea2.clickOnYourAnswerTab();
	learningArea2.CorrectnessVMCQ();

	report.startStep("Check Correct Answer Tab");
	learningArea2.clickOnCorrectAnswerTab();


	}
	
	@Test
	@TestCaseParams(testCaseID = {""})
	public void testResourceCABAPage () throws Exception {
		
		learningArea2 = new NewUxLearningArea2(webDriver, testResultService);
		
	report.startStep("Init Test Data");
		String tokenUrl = "runtime/customIncludes/caba/edResourceLinks.html";
		String baseUrl = pageHelper.CILink.split(".com")[0] + ".com/";
		String link = "customResourceLinks__resourceLink";
		String link2 = "customResourceLinks__resourceLink";
		
		tokenUrl = baseUrl+tokenUrl;
		
	report.startStep("Open CABA Resource Page");
		webDriver.deleteCookiesAndCache();	
		webDriver.closeBrowser();
		closeBrowserAndOpenAgain();		
		webDriver.openUrl(tokenUrl);
		
	report.startStep("Press on any link");
	    webDriver.waitForElement(link, ByTypes.className).click();	   
		webDriver.waitForElement(link2, ByTypes.className).click();	   
	}
		
	@Test
	@TestCaseParams(testCaseID = {"49319"})
	public void testCabaLoginPage () throws Exception {
		
		String institutionName ="ca";
		String institutionId = configuration.getProperty("caba.InstitutionId");
		String clsasName = configuration.getProperty("caba.class");
		String url = pageHelper.getCustomUrl("caba");
		
	report.startStep("Init Test Data");
		learningArea2 = new NewUxLearningArea2(webDriver, testResultService);
		loginPage = new NewUXLoginPage (webDriver, testResultService);
		
	report.startStep("Open CABA Resource Page");
		webDriver.deleteCookiesAndCache();	
		closeBrowserAndOpenAgain();
		webDriver.openUrl("https://" + url);
		sleep(3);
		
		
	report.startStep("Submit disabled");
		loginPage.isSubmitButtonDisabled();
			
	report.startStep("Test Blank user name");
		loginPage.enterPassowrd("12345");
		loginPage.clickOnUserNameTextBpx();
		loginPage.PressOnLogin();
		loginPage.clickOnPassTextBox();
		loginPage.ValidateCabaErrorMessages(ErrorUserName);
		
	report.startStep("Special char in user name");
		loginPage.enterUserName("aaaaaaaa#");
		loginPage.PressOnLogin();
		loginPage.ValidateCabaErrorMessages(ErrorUserName);
		loginPage.isSubmitButtonDisabled();
		
	report.startStep("No english chars");
		loginPage.enterUserName("aaaa�zzz");
		loginPage.PressOnLogin();
		loginPage.ValidateCabaErrorMessages(ErrorUserName);
		loginPage.isSubmitButtonDisabled();
		
	report.startStep("Password with special chars");
		loginPage.enterPassowrd("123$4444");
	    loginPage.PressOnLogin();
	    loginPage.ValidateCabaErrorMessages(ErrorUserName);
	    loginPage.isSubmitButtonDisabled();
	    
	report.startStep("Login to CABA");
		
	String[] userDetails= getUserNamePassId(institutionId, clsasName);
	
	//String[] userDetails = pageHelper.getUserNamePassworIddByInstitutionIdAndClassName(institutionId, clsasName);
	
	    loginPage.enterUserName(userDetails[0]);
	    loginPage.enterInstitution(institutionName);
	    sleep(2);
	    loginPage.enterPassowrd(userDetails[1]);
	    loginPage.PressOnLogin();
	    sleep(2);
	    
	report.startStep("Click on logout");
		homePage.closeModalPopUp();
		learningArea2.clickOnLogoutLearningArea();
	
	}
	
	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}	
}
