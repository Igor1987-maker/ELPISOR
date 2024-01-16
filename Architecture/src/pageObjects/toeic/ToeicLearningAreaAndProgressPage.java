package pageObjects.toeic;

import drivers.GenericWebDriver;
import org.json.JSONObject;
import org.openqa.selenium.support.PageFactory;
import pageObjects.GenericPage;
import pageObjects.edo.*;
import services.PageHelperService;
import services.TestResultService;
import tests.edo.newux.BasicNewUxTest;

import java.util.ArrayList;
import java.util.List;

public class ToeicLearningAreaAndProgressPage extends GenericPage{

	NewUXLoginPage loginPage;
	NewUxClassicTestPage classicTest;
	NewUxAssessmentsPage testsPage;
	NewUxLearningArea2 learningArea2;
	NewUxUnitObjectivesPage unitObjPage;
	PageHelperService pageHelper = new PageHelperService();
	NewUxHomePage homePage;
	private static final String REPORTS_FOLDER = "courseErrorsReports";
	public static String unitName;
	List<String[]> errorList;
	List<String[]> units;
	List<String[]> components;
	String [] expectedLessonName;
	int totalLessonsCompleted = 0;
	String actCompletion = "";
	String progressOfUnit;
	JSONObject jsonObj = new JSONObject();
	
	
	
	public ToeicLearningAreaAndProgressPage(GenericWebDriver webDriver, TestResultService testResultService)
			throws Exception {
		super(webDriver, testResultService);
		PageFactory.initElements(webDriver.getWebDriver(), this);
	}

	@Override
	public GenericPage waitForPageToLoad() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GenericPage OpenPage(String url) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	
	public void makeProgressInToeicCourse(String courseName, String courseId, boolean checkConsoleErrors, boolean checkProgress, boolean checkUnitLessonNames, boolean checkStepNames) throws Exception {
		
	report.startStep("Get Course Units by courseId");
		learningArea2 = new NewUxLearningArea2(webDriver, testResultService);
		learningArea2.waitUntilLearningAreaLoaded();
		units = dbService.getCourseUnitDetils(courseId);
				
		for (int i = 0; i < 1; i++) {
			
			unitName = units.get(i)[1].trim().replace("  ", " ");
			report.startStep("Get Unit Components by unitId");
			components = dbService.getComponentDetailsByUnitId(units.get(i)[0]);

	report.startStep("Go over unit: " + units.get(i)[1]);
			
			int unitIndex = i + 1;
			int lessonIndex = 1;
			int stepIndex = 1;
			int taskIndex = 1;
			sleep(1);
			homePage = new NewUxHomePage(webDriver, testResultService);
			//homePage.clickOnUnitLessons(unitIndex);
			//sleep(2);
			expectedLessonName = new String [components.size()];		
			//homePage.clickOnLesson(i, lessonIndex); 
			//learningArea2.waitUntilLearningAreaLoaded();
     		//for (int j = 0; j < components.size(); j++) { // PRODUCTION
	
	report.startStep("Go over lesson");		
		for (int j = 0; j < 1; j++) {
			//for (int j = 13; j < components.size(); j++) {	
			report.startStep("Get Component SubComponents(Steps) by componentId");
				List<String[]> subComp = dbService.getSubComponentsDetailsByComponentId(components.get(j)[1]);
				boolean isTest = false;
				boolean isInteract = false;
				boolean stepHasIntro = false;
				String stepName = "undefined";
				stepIndex = 1;
				taskIndex = 1;
						
				for (int h = 0; h < subComp.size(); h++) {
					taskIndex = 1;
					stepName = subComp.get(h)[0];
					// define if Step is Interact
					if (stepName.contains("Practice 1") || stepName.contains("Practice 2") || stepName.contains("Interact 1") || stepName.contains("Interact 2") ) {
						isInteract = true;
					}
					// define if Step is Test
					if (stepName.contains("Test") || stepName.contains("Let's review")) {
						isTest = true;
					}
					// define if Step has Intro page
					if (learningArea2.isTaskCounterHasIntroMode()) {
						stepHasIntro = true;
					}
					
					if (h > 0) {
					
						stepIndex = h + 1;
						report.startStep("Click on Step: " + stepIndex);
												
						if (isTest) {
							learningArea2.clickOnStep(stepIndex, false);	
							learningArea2.clickOnStartTest();
						} else learningArea2.clickOnStep(stepIndex);	
						sleep(1);
												
						if (isInteract) learningArea2.closeAlertModalByAccept();
					
				} else 
						if (stepHasIntro) learningArea2.clickOnNextButton();
													
					if (checkConsoleErrors) {
						report.startStep("Enable console watcher");
						errorList = pageHelper.checkTaskForConsoleErrorsAddToList(errorList, courseName, unitIndex, lessonIndex, stepIndex, taskIndex);
					}
					
					report.startStep("Get SubComponets Items (Tasks) by subComponentId");
										
					List<String[]> expectdTasks = dbService.getSubComponentItems(subComp.get(h)[1]);
					
					report.startStep("Go over every Task / Number of Tasks is :"+expectdTasks.size());
					
					taskIndex = 1;	
					
					int itemTypeId = Integer.parseInt(expectdTasks.get(0)[2]); 
					String itemCode = expectdTasks.get(0)[1];
										
					for (int k = 0; k < expectdTasks.size(); k++) {
						
						itemTypeId = Integer.parseInt(expectdTasks.get(k)[2]);
						itemCode = expectdTasks.get(k)[1];
						
						if (k > 0) {
							learningArea2.clickOnNextButton();
						}
												
						if (checkConsoleErrors) {
							report.startStep("Enable console watcher");
							errorList = pageHelper.checkTaskForConsoleErrorsAddToList(errorList, courseName, unitIndex, lessonIndex, stepIndex, taskIndex);
						}
						
						if (checkProgress) {
							report.startStep("Making progress in Task "+taskIndex+" / Checking status of Task " + taskIndex);
							try {
								learningArea2.makeProgressActionByItemType(itemTypeId, itemCode);
							} catch (Exception e) {
								testResultService.addFailTest("Cannot make progress in "+courseName+" : "+"Unit "+unitIndex+" - "+"Lesson "+ lessonIndex +" - "+"Step "+stepIndex+" - "+"Task "+ taskIndex, false, true);
								e.printStackTrace();
							}
							
						}
						
						taskIndex++;
																				
					}
						
				}
		report.startStep("Submit lesson");			
			if (isTest && checkProgress) {
						learningArea2.submitTest(false);
						sleep(2);
						learningArea2.clickOnNextButton();
			}
		report.startStep("Go back to Home Page");
					sleep(2);
					learningArea2.clickOnHomeButton();
					if (!checkProgress && isTest) learningArea2.approveTest();
					homePage.waitHomePageloaded();
							
					if (checkProgress) {
		//report.startStep("Check Unit "+unitIndex+" Progress Bar");
						//progressOfUnit = homePage.getUnitProgress(unitName);
						
		report.startStep("Waiting until progress stores in DB");
					sleep(30);
						
				}
			}
		}
		
		
	}
	
	
	
	
	
	
	
	
	public void sleep(int seconds) throws Exception {
		report.report("Sleeping for " + seconds + "seconds");
		Thread.sleep(seconds * 1000);
	}

	public ArrayList<String> accomplishUnitTest(int unitID, String courseName, String expectedTestResult, int numOfSections, String answersFilePath) throws Exception {
		
	report.startStep("Open Unit Test of Course " + courseName + ", Unit 1");
	
	homePage = new NewUxHomePage(webDriver, testResultService);
	homePage.openUnitTestForCourse(unitID,courseName); // First param is unit.
	String testName ="";
		if(BasicNewUxTest.institutionName.equalsIgnoreCase("QaJobRediness")) {
			testName = courseName + " - Unit "+unitID+" Test";
		}else {
			testName = courseName + " Unit Test - Unit " + unitID;
		}
		//sleep(3);
		
	report.startStep("Initialize toeic start page");
		toeicStartPage toeicStartpage = new toeicStartPage(webDriver,testResultService);
		toeicStartpage.waitForPageToLoad();
	
	report.startStep("Validate the URL is Correct");
		webDriver.validateURL("/Runtime/Test.aspx"); 
		//sleep(1);

	report.startStep("Press Start New Test if 'Resume Test' Option is Present");
		toeicStartpage.pressStartNewTestInResumePopUp();
	
	report.startStep("Validate the Title is the Name of the First Test");
		toeicStartpage.validateTitle(testName);
		
	report.startStep("Validate 'Test Your Sound' Button is Clickable");
		toeicStartpage.checkTestSoundButtonIsClickable();
	
	report.startStep("Validate the Welcome Text is Not Null");
		toeicStartpage.validateTheWelcomeTextIsNotNull();
		
	report.startStep("Click Start");
		toeicStartpage.clickStart();
		sleep(1);
		
	report.startStep("Initialize toeic questions page");
		toeicQuestionPage toeicQuestionpage = new toeicQuestionPage(webDriver,testResultService);
		
	report.startStep("Click Next");
		toeicQuestionpage.clickNext();
		
	report.startStep("Answer Questions");
		ArrayList<String> UnitTestAnswers2 = toeicQuestionpage.answerQuestionsInSeveralSections(numOfSections,answersFilePath);// "files/TOEIC/UnitTestAnswers2.csv");

	report.startStep("Click Submit");
		sleep(2);
		toeicQuestionpage.submit(true);
		sleep(1);
		
	report.startStep("Validate the Score is Displayed Correctly");
		toeicQuestionpage.validateScoreIsDisplayedCorrectly(expectedTestResult);
		
	report.startStep("Close The Test");
		toeicQuestionpage.clickCloseButton();
		webDriver.switchToMainWindow();
		
		return UnitTestAnswers2;
	}
	
	
	
}
