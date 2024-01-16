package tests.edo.newux;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import Enums.ByTypes;
import Enums.StepProgressBox;
import Enums.TaskTypes;
import Interfaces.TestCaseParams;
import pageObjects.edo.NewUXLoginPage;
import pageObjects.edo.NewUxDragAndDropSection;
import pageObjects.edo.NewUxDragAndDropSection2;
import pageObjects.edo.NewUxHomePage;
import pageObjects.edo.NewUxLearningArea2;
import testCategories.AngularLearningArea;
import testCategories.inProgressTests;

@Category(AngularLearningArea.class)
public class setProgressTests2 extends BasicNewUxTest {

	NewUxLearningArea2 learningArea2;
	NewUxDragAndDropSection2 dragAndDrop2;
	NewUxDragAndDropSection dragAndDrop;
	NewUXLoginPage loginPage;

	private static final String htmlEditorXpath = "//body[@id='tinymce']";
	private static final String freeText = "Some text for test some text for test some text for test some text for test.";
	private static final String htmlEditorFrame = "elm1_ifr";
	
	private String courseCode = "undefined";
	private String courseName = "undefined";
	private String courseId = "undefined";
	private String itemId = "undefined";
	
	private int unitNum = 0;
	private int lessonNum = 0;
	private int stepNum = 0;
	private int taskNum = 0;
	
	@Before
	public void setup() throws Exception {
		super.setup();
	
		report.startStep("Create user and login");
		homePage =  createUserAndLoginNewUXClass();
		
	//	BasicNewUxTest.institutionName= institutionsName[9];
	//	pageHelper.restartBrowserInNewURL(institutionName, true); //initializeData();
		
	//	report.startStep("Get user and login");
	//	String className = configuration.getProperty("classname.tBoard");
	//	createUserAndLoginNewUXClass(className, institutionId);
	
		dragAndDrop2 = new NewUxDragAndDropSection2(webDriver,testResultService);
		dragAndDrop = new NewUxDragAndDropSection(webDriver,testResultService);

		homePage.closeAllNotifications();
		homePage.waitHomePageloadedFully();
	}
	
	@Test
	@TestCaseParams(testCaseID = { "22884" })
	public void testSetProgressExploreSpeaking() throws Exception {
		
		report.startStep("Init Test Data");
		setTaskNavPathAndItemId("B3", 4, 3, 1, 1);
	
		report.startStep("Navigate B3-U4-L3-P1-T1");
		learningArea2 = homePage.navigateToTask("B3", 4, 3, 1, 1);
		learningArea2.waitToLearningAreaLoaded();
		
		report.startStep("Click on Play");
		learningArea2.clickOnPlayVideoButton();
		sleep(3);
		
		report.startStep("Verify Progress Set in DB / Set in UI of LA / Get in UI of HP");
		verifyProgressSetGetPositive(true);

	}

	@Test
	@TestCaseParams(testCaseID = { "22886" })
	public void testSetProgressPrepareListening() throws Exception {
	
		report.startStep("Init Test Data");
		setTaskNavPathAndItemId("FD", 2, 1, 1, 1);
		
		report.startStep("Navigate FD-U2-L1-P1-T1");
		learningArea2 = homePage.navigateToTask("FD", 2, 1, 1, 1);
		
		report.startStep("Click on Play button");
		learningArea2.clickOnPlayVideoButtonPrepare();
		sleep(1);
		
		report.startStep("Verify Progress Set in DB / Set in UI of LA / Get in UI of HP");
		verifyProgressSetGetPositive(true);
		
	}
	
	
	@Test
	@TestCaseParams(testCaseID = { "29536" })
	public void testSetProgressCCPrepare() throws Exception {
		
		navigateToCCcourse();
		
		learningArea2 = homePage.checkTextandClickOnCourseBtn("Start");
		learningArea2.waitToLearningAreaLoaded();

		report.startStep("Verify progress in CC prepare with submit");
			webDriver.refresh();
			learningArea2.waitToLearningAreaLoaded();
			
			learningArea2.checkIfTaskHasDoneState(1,false);
			webDriver.waitForElement("inputArea", ByTypes.className).sendKeys("Text to input");
			webDriver.refresh();
			learningArea2.waitToLearningAreaLoaded();
			learningArea2.checkIfTaskHasDoneState(1,false);
			webDriver.waitForElement("submitBtn",ByTypes.id).click();
			webDriver.waitForElement("btnOk", ByTypes.id).click();
			learningArea2.checkIfTaskHasDoneState(1,true);
		
		report.startStep("Switch to next lesson");	
			learningArea2.openLessonsList();
			learningArea2.clickOnLessonByNumber(2);
			
		report.startStep("Verify progress in CC prepare without submit");
			webDriver.refresh();
			learningArea2.waitToLearningAreaLoaded();
			learningArea2.checkIfTaskHasDoneState(1,true);
		
	}

	
	@Test
	@TestCaseParams(testCaseID = { "29547" })
	public void testSetProgressCCExplore() throws Exception {
		
		navigateToCCcourse();
		
		learningArea2 = homePage.checkTextandClickOnCourseBtn("Start");
		learningArea2.waitToLearningAreaLoaded();
			
		report.startStep("Navigate to Explore step");
			learningArea2.openStepsList();
			learningArea2.clickOnStep(2, true); // visit is enough for progress
			sleep(1);
		
		report.startStep("Verify progress in CC Explore");
			webDriver.refresh();
			learningArea2.waitToLearningAreaLoaded();
			learningArea2.openStepsList();
			learningArea2.clickOnStep(2, true);
			learningArea2.checkIfTaskHasDoneState(1,true);
			
	}
	

	@Test
	@TestCaseParams(testCaseID = { "29564" })
	public void testSetProgressCCPractice() throws Exception {
		
		String places = "prFITB__DDLOptionsW_Q1_L1";
		String answers = "DDLOptions__listItem_aid_52";
		
		navigateToCCcourse();
		
		learningArea2 = homePage.checkTextandClickOnCourseBtn("Start");
		learningArea2.waitToLearningAreaLoaded();
			
		report.startStep("Navigate to Practice step");
			learningArea2.openStepsList();
			learningArea2.clickOnStep(3, true); // visit is enough for progress
			sleep(1);
		
		report.startStep("Make sure visiting doesn't cause progress");
			learningArea2.clickOnNextButton(3);
			webDriver.refresh();
			learningArea2.waitToLearningAreaLoaded();
			learningArea2.openStepsList();
			learningArea2.clickOnStep(3, true);
			learningArea2.checkIfTaskHasDoneState(1,false);
			learningArea2.checkIfTaskHasDoneState(2,false);
			learningArea2.checkIfTaskHasDoneState(3,false);
			learningArea2.checkIfTaskHasDoneState(4,false);
			
		report.startStep("Perform progress inducing action in CC Practice");
			//RM code: Added next line in order to fix the problem
			learningArea2.clickOnBackButton(3);
			learningArea2.SelectRadioBtn("question-1_answer-1");
			learningArea2.clickOnNextButton();
			webDriver.waitForElement("inputArea", ByTypes.className).sendKeys("Text to input");
			webDriver.waitForElement("//div[@class='buttonWrapper sendTT']/a",ByTypes.xpath).click();
			webDriver.waitForElement("btnOk", ByTypes.id).click();
			learningArea2.clickOnNextButton();
			dragAndDrop2 = new NewUxDragAndDropSection2(webDriver,testResultService);
			dragAndDrop2.dragAndDropFirstAnswer(TaskTypes.Matching);
			learningArea2.clickOnNextButton();
			learningArea2.selectAnswerFromDropDown2(places,answers);
			webDriver.refresh();
			
		report.startStep("Make sure progress listed for all practice types.");
			sleep(2);
			learningArea2.openStepsList();
			learningArea2.clickOnStep(3, true);
			learningArea2.checkIfTaskHasDoneState(1,true);
			learningArea2.checkIfTaskHasDoneState(2,true);
			learningArea2.checkIfTaskHasDoneState(3,true);
			//learningArea2.checkIfTaskHasDoneState(4,true);
	}
	
	private void navigateToCCcourse() throws Exception {
		
		String actualCourse="";
		
		report.startStep("Navigate to CC progress course and press start");
		for (int i = 0 ; i < coursesNames.length + 1 ; i++) {
			actualCourse = homePage.getCurrentCourseName();
			if (actualCourse.equals("CC progress course"))
				break;
			else {
				homePage.carouselNavigateBack();
				sleep(1);
				homePage.waitHomePageloadedFully();
			}
		}
		if (!actualCourse.equals("CC progress course")) {
			testResultService.addFailTest("Course doesn't exist", true, true);
		}
		
	}

	@Test
	@TestCaseParams(testCaseID = { "29554" })
	public void testSetProgressCCTest() throws Exception {
		
		navigateToCCcourse();
		
		learningArea2 = homePage.checkTextandClickOnCourseBtn("Start");
		learningArea2.waitToLearningAreaLoaded();
			
		report.startStep("Navigate to Test step");
			learningArea2.openStepsList();
			learningArea2.clickOnStep(4, false); // visit is enough for progress
			sleep(1);
		
		report.startStep("Start the Test");
			learningArea2.clickOnStartTest();
			
		report.startStep("Answer test");
			learningArea2.SelectRadioBtn("question-1_answer-3");
			learningArea2.clickOnNextButton();
			dragAndDrop2 = new NewUxDragAndDropSection2(webDriver,testResultService);
			dragAndDrop2.dragAndDropFirstAnswer(TaskTypes.Matching);
			learningArea2.clickOnNextButton();
			learningArea2.submitTest(true);
			sleep(2);
			
		report.startStep("Make sure progress listed for all test tasks");
			learningArea2.clickOnReviewTestResults();
			learningArea2.openTaskBar();
			for (int i = 0; i < 3; i++) {
				learningArea2.checkIfTaskHasDoneState(i+1, true);
			}
			learningArea2.closeTaskBar();
			
		report.startStep("Go to Home Page and check course completion value in widget");
			verifyCourseCompletionOnHomeWidgetIsNotZero();
	}
	
	@Test
	//@Category(SanityTests.class)
	@TestCaseParams(testCaseID = { "22888" })
	public void testSetProgressExploreReading() throws Exception {
		
		report.startStep("Init Test Data");
		setTaskNavPathAndItemId("A3", 5, 2, 1, 1);
				
		report.startStep("Navigate to "+ courseName +" - Unit "+ unitNum +" - Lesson "+ lessonNum +" - Step "+ stepNum +" - Task "+ taskNum);
		learningArea2 = homePage.navigateToTask(courseCode, unitNum, lessonNum, stepNum, taskNum);
				
		report.startStep("Verify Progress Set in DB / Set in UI of LA / Get in UI of HP");
		verifyProgressSetGetPositive(true);
		
	}
	
	@Test
	@TestCaseParams(testCaseID = { "56618" })
	public void testSetProgressExploreGettingToKnowTOEIC() throws Exception {
		
		report.startStep("Logout and login as TOEIC class user");
			homePage.clickOnLogOut();
		//	sleep(2);
			String className = "EDTOEIC";
			studentId = pageHelper.createUSerUsingSP(
				institutionId, className);
			String userName = dbService.getUserNameById(studentId,institutionId);
			NewUXLoginPage loginPage = new NewUXLoginPage(webDriver,testResultService);
			NewUxHomePage homePage = loginPage.loginAsStudent(userName, "12345");
		
		report.startStep("Init Test Data");
			setTOEICTaskNavPathAndItemId("M2", 2, 4, 1, 1);
				homePage.waitHomePageloaded();
				
		report.startStep("Navigate to "+ courseName +" - Unit "+ unitNum +" - Lesson "+ lessonNum +" - Step "+ stepNum +" - Task "+ taskNum);
			learningArea2 = homePage.navigateToTOEICTask(courseCode, unitNum, lessonNum, stepNum, taskNum);
			sleep(2);
				
		report.startStep("Verify Progress Set in DB / Set in UI of LA / Get in UI of HP");
			verifyProgressSetGetPositive(true);
		
	}
	
	@Test
	@TestCaseParams(testCaseID = { "22890" })
	public void testSetProgressExploreVocabulary() throws Exception {
	
		report.startStep("Init Test Data");
		setTaskNavPathAndItemId("B1", 2, 6, 1, 1);
				
		report.startStep("Navigate to "+ courseName +" - Unit "+ unitNum +" - Lesson "+ lessonNum +" - Step "+ stepNum +" - Task "+ taskNum);
		learningArea2 = homePage.navigateToTask(courseCode, unitNum, lessonNum, stepNum, taskNum);
		sleep(2);
		
		report.startStep("Verify Progress Set in DB / Set in UI of LA / Get in UI of HP");
		verifyProgressSetGetPositive(true);
				
	}
	
	// needs to change test according to new template
	
	@Test
	@TestCaseParams(testCaseID = { "22892" })
	public void testSetProgressExploreAlphabet() throws Exception {
		
		report.startStep("Init Test Data");
		setTaskNavPathAndItemId("FD", 1, 1, 2, 1);
				
		report.startStep("Navigate to "+ courseName +" - Unit "+ unitNum +" - Lesson "+ lessonNum +" - Step "+ stepNum +" - Task "+ taskNum);
		learningArea2 = homePage.navigateToTask(courseCode, unitNum, lessonNum, stepNum, taskNum);
		
		report.startStep("Verify Progress Set in DB / Set in UI of LA / Get in UI of HP");
		verifyProgressSetGetPositive(true);
	}
	

	@Test
	@TestCaseParams(testCaseID = { "22894" })
	public void testSetProgressPracticeAlphabetMatchingGame() throws Exception {
	
		report.startStep("Init Test Data");
		setTaskNavPathAndItemId("FD", 1, 1, 3, 3);
				
		report.startStep("Navigate to "+ courseName +" - Unit "+ unitNum +" - Lesson "+ lessonNum +" - Step "+ stepNum +" - Task "+ taskNum);
		learningArea2 = homePage.navigateToTask(courseCode, unitNum, lessonNum, stepNum, taskNum);
		
		report.startStep("Make progress action");
		learningArea2.Card1.click();
	
		report.startStep("Verify Progress Set in DB / Set in UI of LA / Get in UI of HP");
		verifyProgressSetGetPositive(false);
		
	}
	
	@Test
	//@Category(SanityTests.class)
	@TestCaseParams(testCaseID = { "22896","25807" }, skippedBrowsers = {"firefox"}) // Actions API not supported by geckodriver)
	public void testSetProgressPracticeClassification2() throws Exception {
		
		report.startStep("Init Test Data");
		setTaskNavPathAndItemId("B1", 2, 6, 2, 2);
				
		report.startStep("Navigate to "+ courseName +" - Unit "+ unitNum +" - Lesson "+ lessonNum +" - Step "+ stepNum +" - Task "+ taskNum);
		learningArea2 = homePage.navigateToTask(courseCode, unitNum, lessonNum, stepNum, taskNum);
		
		report.startStep("Make progress action");
		dragAndDrop2.dragAndDropFirstAnswer(TaskTypes.Classification);
		sleep(2);
		
		report.startStep("Verify Progress Set in DB / Set in UI of LA / Get in UI of HP");
		verifyProgressSetGetPositive(false);
		
	}
	
	//changes  test according new template
	
	@Test
	@TestCaseParams(testCaseID = { "22896","25807" }, skippedBrowsers = {"firefox"}) // Actions API not supported by geckodriver)
	public void testSetProgressPracticeListeningCloseDragAndDrop() throws Exception {
		
	report.startStep("Init Test Data");
		setTaskNavPathAndItemId("A3", 6, 1, 2, 2);
				
	report.startStep("Navigate to "+ courseName +" - Unit "+ unitNum +" - Lesson "+ lessonNum +" - Step "+ stepNum +" - Task "+ taskNum);
		learningArea2 = homePage.navigateToTask(courseCode, unitNum, lessonNum, stepNum, taskNum);
		
	report.startStep("Make progress action");
		dragAndDrop2.dragAndDropFirstAnswer(TaskTypes.Close);
		sleep(2);
		
	report.startStep("Verify Progress Set in DB / Set in UI of LA / Get in UI of HP");
		verifyProgressSetGetPositive(false);
	}
	
	//SetProgress for New close template
	
	@Test
	@TestCaseParams(testCaseID = { "22896","25807" }, skippedBrowsers = {"firefox"}) // Actions API not supported by geckodriver)
	public void testSetProgressPracticeCloseDragAndDrop2() throws Exception {
		
		report.startStep("Init Test Data");
		setTaskNavPathAndItemId("A1", 2, 5, 2, 7);
				
		report.startStep("Navigate to "+ courseName +" - Unit "+ unitNum +" - Lesson "+ lessonNum +" - Step "+ stepNum +" - Task "+ taskNum);
		learningArea2 = homePage.navigateToTask(courseCode, unitNum, lessonNum, stepNum, taskNum);
		
		report.startStep("Make progress action");
	    dragAndDrop2.dragAndDropFirstAnswer(TaskTypes.Close);
		sleep(2);
		
		report.startStep("Verify Progress Set in DB / Set in UI of LA / Get in UI of HP");
		verifyProgressSetGetPositive(false);
		

	}
	
	// SetProgress for New MTTP
	@Test
	@TestCaseParams(testCaseID = { "46957" }, skippedBrowsers = {"firefox"}) // Actions API not supported by geckodriver)
	public void testSetProgressPracticeMatchTextToPictureNew2() throws Exception {
		
	report.startStep("Init Test Data");
		setTaskNavPathAndItemId("FD", 1, 1, 3, 4);
				
	report.startStep("Navigate to "+ courseName +" - Unit "+ unitNum +" - Lesson "+ lessonNum +" - Step "+ stepNum +" - Task "+ taskNum);
		learningArea2 = homePage.navigateToTask(courseCode, unitNum, lessonNum, stepNum, taskNum);
		
	report.startStep("Make progress action");
		dragAndDrop2.dragAndDropFirstAnswer(TaskTypes.MTTP);
		sleep(2);
		
	report.startStep("Verify Progress Set in DB / Set in UI of LA / Get in UI of HP");
		verifyProgressSetGetPositive(false);
	}
	
	
	@Test
	@TestCaseParams(testCaseID = { "46957" }, skippedBrowsers = {"firefox"}) // Actions API not supported by geckodriver)
	public void testSetProgressPracticeMatchTextToPictureNewNegative2() throws Exception {
		
	report.startStep("Init Test Data");
		setTaskNavPathAndItemId("FD", 1, 1, 3, 4);
				
	report.startStep("Navigate to "+ courseName +" - Unit "+ unitNum +" - Lesson "+ lessonNum +" - Step "+ stepNum +" - Task "+ taskNum);
		learningArea2 = homePage.navigateToTask(courseCode, unitNum, lessonNum, stepNum, taskNum);
		
	report.startStep("Make actions which do not cause progress done");
		
		learningArea2.clickOnSeeAnswer();
		sleep(1);
		
	report.startStep("Verify Progress NOT Set in DB / Set in UI of LA / Get in UI of HP");
		verifyProgressSetGetNegative(false);
	}
	
	//SetProgress for New Matching template
	
		@Test
		@TestCaseParams(testCaseID = { "22896","25807" }, skippedBrowsers = {"firefox"}) // Actions API not supported by geckodriver)
		public void testSetProgressPracticeMatchingDragAndDrop() throws Exception {
			
			report.startStep("Init Test Data");
			setTaskNavPathAndItemId("A3", 6, 1, 2, 5);
					
			report.startStep("Navigate to "+ courseName +" - Unit "+ unitNum +" - Lesson "+ lessonNum +" - Step "+ stepNum +" - Task "+ taskNum);
			learningArea2 = homePage.navigateToTask(courseCode, unitNum, lessonNum, stepNum, taskNum);
			
			report.startStep("Make progress action");
		    dragAndDrop2.dragAndDropFirstAnswer(TaskTypes.Matching);
			sleep(2);
			
			report.startStep("Verify Progress Set in DB / Set in UI of LA / Get in UI of HP");
			verifyProgressSetGetPositive(false);
		}
	
		//SetProgress for New Matching (Negative) template
		
			@Test
			@TestCaseParams(testCaseID = { "22896","25807" }, skippedBrowsers = {"firefox"}) // Actions API not supported by geckodriver)
			public void testSetProgressPracticeMatchingDragAndDropNegative() throws Exception {
				
				report.startStep("Init Test Data");
				setTaskNavPathAndItemId("A3", 6, 1, 2, 5);
						
				report.startStep("Navigate to "+ courseName +" - Unit "+ unitNum +" - Lesson "+ lessonNum +" - Step "+ stepNum +" - Task "+ taskNum);
				learningArea2 = homePage.navigateToTask(courseCode, unitNum, lessonNum, stepNum, taskNum);
				
				report.startStep("Make actions which do not cause progress done");
				learningArea2.clickOnClearAnswer();
				
				report.startStep("Verify Progress NOT Set in DB / Set in UI of LA / Get in UI of HP");
				verifyProgressSetGetNegative(false);
			}
			
			
			// Set Progress for New Sequence image
		
			@Test
			@TestCaseParams(testCaseID = { "24634" }, skippedBrowsers = {"firefox"}) // Actions API not supported by geckodriver)
			public void testSetProgressPracticeNewSequenceImagePositive() throws Exception {
				
				report.startStep("Init Test Data");
				setTaskNavPathAndItemId("A3", 5, 2, 2, 2);
				String correctSeq = "prSeqImg__container_4";
				String wrongImage = "A3RSMGPGP4";
				String correctImage = "A3RSMGPGP3";
						
				report.startStep("Navigate to "+ courseName +" - Unit "+ unitNum +" - Lesson "+ lessonNum +" - Step "+ stepNum +" - Task "+ taskNum);
				learningArea2 = homePage.navigateToTask(courseCode, unitNum, lessonNum, stepNum, taskNum);
				
				report.startStep("Make progress action");
				dragAndDrop.dragAndDropSequenceAnswerByImageNameToPlaceInOrderNewSeq(wrongImage, correctSeq);
				dragAndDrop.dragAndDropSequenceAnswerByImageNameToPlaceInOrderNewSeq(correctImage, correctSeq);
			
				report.startStep("Verify Progress Set in DB / Set in UI of LA / Get in UI of HP");
				verifyProgressSetGetPositive(false);
			}
	
			
			@Test
			@TestCaseParams(testCaseID = { "24634" }, skippedBrowsers = {"firefox"}) // Actions API not supported by geckodriver)
			public void testSetProgressPracticeNewSequenceImageNegative() throws Exception {
				
				report.startStep("Init Test Data");
				setTaskNavPathAndItemId("A3", 5, 2, 2, 2);
						
				report.startStep("Navigate to "+ courseName +" - Unit "+ unitNum +" - Lesson "+ lessonNum +" - Step "+ stepNum +" - Task "+ taskNum);
				learningArea2 = homePage.navigateToTask(courseCode, unitNum, lessonNum, stepNum, taskNum);
				
				report.startStep("Make progress action");
				learningArea2.clickOnClearAnswer();

				
				report.startStep("Verify Progress Set in DB / Set in UI of LA / Get in UI of HP");
				verifyProgressSetGetNegative(false);
			}
			
	@Test
	@TestCaseParams(testCaseID = { "22901" })
	public void testSetProgressPracticeListeningMarkTheTrue() throws Exception {
		
		report.startStep("Init Test Data");
		setTaskNavPathAndItemId("B3", 3, 2, 2, 4);
				
		report.startStep("Navigate to "+ courseName +" - Unit "+ unitNum +" - Lesson "+ lessonNum +" - Step "+ stepNum +" - Task "+ taskNum);
		learningArea2 = homePage.navigateToTask(courseCode, unitNum, lessonNum, stepNum, taskNum);
		
		report.startStep("Make progress action");
		learningArea2.checkCheckBox(3);
		sleep(2);
		
		report.startStep("Verify Progress Set in DB / Set in UI of LA / Get in UI of HP");
		verifyProgressSetGetPositive(false);
		

	}
	
	@Test
	@TestCaseParams(testCaseID = { "22939" })
	public void testSetProgressPracticeListeningFillTheBlanks() throws Exception {
		
	report.startStep("Init Test Data");
		String  places = "prFITB__DDLOptionsW_Q1_L1";
		String  answers = "DDLOptions__listItem_aid_79";
		setTaskNavPathAndItemId("A3", 6, 1, 2, 3);
			
	report.startStep("Navigate to "+ courseName +" - Unit "+ unitNum +" - Lesson "+ lessonNum +" - Step "+ stepNum +" - Task "+ taskNum);
		learningArea2 = homePage.navigateToTask(courseCode, unitNum, lessonNum, stepNum, taskNum);
		
	report.startStep("Make progress action");
		learningArea2.selectAnswerFromDropDown2(places,answers);
		
	report.startStep("Verify Progress Set in DB / Set in UI of LA / Get in UI of HP");
		verifyProgressSetGetPositive(false);

	}
	
	@Test
	@TestCaseParams(testCaseID = { "48464" })
	public void testSetProgressPracticeListeningMCQ() throws Exception {
		
		report.startStep("Init Test Data");
		setTaskNavPathAndItemId("FD", 5, 1, 3, 3);
				
		report.startStep("Navigate to "+ courseName +" - Unit "+ unitNum +" - Lesson "+ lessonNum +" - Step "+ stepNum +" - Task "+ taskNum);
		learningArea2 = homePage.navigateToTask(courseCode, unitNum, lessonNum, stepNum, taskNum);
		
		report.startStep("Make progress action");
		learningArea2.SelectRadioBtn("question-1_answer-1");
		
		report.startStep("Verify Progress Set in DB / Set in UI of LA / Get in UI of HP");
		verifyProgressSetGetPositive(false);

	}
	
	@Test
	@TestCaseParams (testCaseID ={"12345"})
	public void testSetProgressPracticeOpenEnded () throws Exception {
		
	report.startStep("Init Test Data");
		setTaskNavPathAndItemId ("FD", 3, 4, 3, 2);
		String correct1 = "Dear";
		String correct2 = "eggs";
		String correct3 = "cheese";
		String correct4 = "Thanks";
		String [] correctAnswers = {"Dear","eggs","cheese","Thanks"};
		String [] textarea ={"qId_1_alId_1","qId_1_alId_2","qId_1_alId_3","qId_1_alId_4"};
		
	report.startStep("Navigate to "+ courseName +" - Unit "+ unitNum +" - Lesson "+ lessonNum +" - Step "+ stepNum +" - Task "+ taskNum);
		learningArea2 = homePage.navigateToTask(courseCode, unitNum, lessonNum, stepNum, taskNum);
		
	report.startStep("Make progress action");
		for (int i = 0; i < correctAnswers.length; i++) {
		learningArea2.enterOpenSmallSegmentAnswerTextByIndex2(textarea[i], correctAnswers[i]);
		sleep(1);
		}
		
	report.startStep("Verify Progress Set in DB / Set in UI of LA / Get in UI of HP");
		verifyProgressSetGetPositive(false);
		
	}
	
	
	@Test
	@TestCaseParams(testCaseID = { "22963" })
	public void testSetProgressPracticeReadingFreeWriteNegative() throws Exception {
		
		sleep(2);
		report.startStep("Init Test Data");
		setTaskNavPathAndItemId("A3", 5, 2, 2, 6);
				
		report.startStep("Navigate to "+ courseName +" - Unit "+ unitNum +" - Lesson "+ lessonNum +" - Step "+ stepNum +" - Task "+ taskNum);
		learningArea2 = homePage.navigateToTask(courseCode, unitNum, lessonNum, stepNum, taskNum);
		
		report.startStep("Make actions which do not cause progress done");
		learningArea2.clickOnClearAnswer();
		learningArea2.clickOnBackButton();
		webDriver.closeAlertByAccept();
		
		report.startStep("Verify Progress NOT Set in DB / Set in UI of LA / Get in UI of HP");
		verifyProgressSetGetNegative(false);
	}
	
	/**
	 * @throws Exception
	 */
	@Test
	@TestCaseParams(testCaseID = { "47809" })
	public void testSetProgressPracticeNewMCQ() throws Exception {
		
		
	report.startStep("Init Test Data");
		setTaskNavPathAndItemId("B3", 9, 2, 2, 2);
				
	report.startStep("Navigate to "+ courseName +" - Unit "+ unitNum +" - Lesson "+ lessonNum +" - Step "+ stepNum +" - Task "+ taskNum);
		learningArea2 = homePage.navigateToTask(courseCode, unitNum, lessonNum, stepNum, taskNum);
		
	report.startStep("Make progress action");
		learningArea2.SelectRadioBtn("question-1_answer-1");
		
	report.startStep("Verify Progress Set in DB / Set in UI of LA / Get in UI of HP");
		verifyProgressSetGetPositive(false);
	}
	
	@Test
	@TestCaseParams(testCaseID = { "48501" })
	public void testSetProgressPracticeNewMultipleCorrectAnswer() throws Exception {
		
		report.startStep("Init Test Data");
			setTaskNavPathAndItemId("I1", 10, 5, 3, 2);
					
		report.startStep("Navigate to "+ courseName +" - Unit "+ unitNum +" - Lesson "+ lessonNum +" - Step "+ stepNum +" - Task "+ taskNum);
			learningArea2 = homePage.navigateToTask(courseCode, unitNum, lessonNum, stepNum, taskNum);
			
		report.startStep("Make progress action");
			learningArea2.SelectRadioBtn("question-1_answer-1");
			
		report.startStep("Verify Progress Set in DB / Set in UI of LA / Get in UI of HP");
			verifyProgressSetGetPositive(false);
	}
	
	@Test
	@TestCaseParams(testCaseID = { "48279" })
	public void testSetProgressPracticeNewMCQImage() throws Exception {
		
		report.startStep("Init Test Data");
			setTaskNavPathAndItemId("B2", 10, 4, 3, 2);
					
		report.startStep("Navigate to "+ courseName +" - Unit "+ unitNum +" - Lesson "+ lessonNum +" - Step "+ stepNum +" - Task "+ taskNum);
			learningArea2 = homePage.navigateToTask(courseCode, unitNum, lessonNum, stepNum, taskNum);
			
		report.startStep("Make progress action");
			learningArea2.SelectRadioBtn("question-1_answer-1");
			
		report.startStep("Verify Progress Set in DB / Set in UI of LA / Get in UI of HP");
			verifyProgressSetGetPositive(false);

	}
	
	@Test
	@TestCaseParams(testCaseID = { "48728" })
	public void testSetProgressPracticeNewMCQImagesSegmented() throws Exception {
		
		report.startStep("Init Test Data");
			setTaskNavPathAndItemId("FD", 1, 5, 3, 3);
					
		report.startStep("Navigate to "+ courseName +" - Unit "+ unitNum +" - Lesson "+ lessonNum +" - Step "+ stepNum +" - Task "+ taskNum);
			learningArea2 = homePage.navigateToTask(courseCode, unitNum, lessonNum, stepNum, taskNum);
			
		report.startStep("Make progress action");
			learningArea2.SelectRadioBtn("question-1_answer-1");
			
		report.startStep("Verify Progress Set in DB / Set in UI of LA / Get in UI of HP");
			verifyProgressSetGetPositive(false);

	}
	
	@Test
	@TestCaseParams(testCaseID = { "22960" })
	public void testSetProgressPracticeMCQNegative() throws Exception {
		
		report.startStep("Init Test Data");
		setTaskNavPathAndItemId("B3", 9, 2, 2, 2);
				
		report.startStep("Navigate to "+ courseName +" - Unit "+ unitNum +" - Lesson "+ lessonNum +" - Step "+ stepNum +" - Task "+ taskNum);
		learningArea2 = homePage.navigateToTask(courseCode, unitNum, lessonNum, stepNum, taskNum);
		
		report.startStep("Make actions which do not cause progress done");
		learningArea2.clickOnCheckAnswer();
	

		report.startStep("Verify Progress NOT Set in DB / Set in UI of LA / Get in UI of HP");
		verifyProgressSetGetNegative(false);
	}
	
	@Test
	@TestCaseParams(testCaseID = { "22964" })
	public void testSetProgressPracticeReadingFreeWritesERaterPositive() throws Exception {
		
		report.startStep("Init Test Data");
		setTaskNavPathAndItemId("A3", 5, 2, 2, 6);
				
		report.startStep("Navigate to "+ courseName +" - Unit "+ unitNum +" - Lesson "+ lessonNum +" - Step "+ stepNum +" - Task "+ taskNum);
		learningArea2 = homePage.navigateToTask(courseCode, unitNum, lessonNum, stepNum, taskNum);
		
		report.startStep("Make progress action");
		webDriver.swithcToFrameAndSendKeys(htmlEditorXpath, freeText, htmlEditorFrame);
		webDriver.switchToTopMostFrame();
		learningArea2.clickOnSubmitText();

		report.startStep("Check progress set in the DB");
		studentService.checkStudentProgress(studentId, courseId, itemId);
				
		report.startStep("Check progress indications updated in LA");
		verifyProgressIndicationInTaskStepLesson(true, StepProgressBox.half, 1);
		
		report.startStep("Click on Next & Back btn and check progress indications");
		learningArea2.clickOnStep(stepNum-1);
		learningArea2.clickOnStep(stepNum);
		verifyProgressIndicationInTaskStepLesson(true, StepProgressBox.half, 1);
				
		report.startStep("Refresh and check progress indications");
		webDriver.refresh();
		learningArea2.clickOnStep(stepNum);
		verifyProgressIndicationInTaskStepLesson(true, StepProgressBox.half, 1);		

		report.startStep("Go to Home Page and check course completion value in widget");
		verifyCourseCompletionOnHomeWidgetIsNotZero();
	}

	@Test
	@TestCaseParams(testCaseID = { "22968" })
	public void testSetProgressPracticeListeningOpenEnded() throws Exception {
	
		report.startStep("Init Test Data");
		setTaskNavPathAndItemId("A3", 6, 1, 2, 6);
		
		String [] correctAnswers = {"Dear","eggs"};
		String [] textarea ={"qId_1_alId_1","qId_1_alId_2"};
				
		report.startStep("Navigate to "+ courseName +" - Unit "+ unitNum +" - Lesson "+ lessonNum +" - Step "+ stepNum +" - Task "+ taskNum);
		learningArea2 = homePage.navigateToTask(courseCode, unitNum, lessonNum, stepNum, taskNum);
		
		report.startStep("Make progress action");
		learningArea2.enterOpenSmallSegmentAnswerTextByIndex2(textarea[0], correctAnswers[0]);
		learningArea2.enterOpenSmallSegmentAnswerTextByIndex2(textarea[1], correctAnswers[1]);
		sleep(2);
		
		report.startStep("Verify Progress Set in DB / Set in UI of LA / Get in UI of HP");
		verifyProgressSetGetPositive(false);

	}
	
	@Test
	@TestCaseParams(testCaseID = { "22864" })
	public void testSetProgressPrepareListeningMedia2Pages() throws Exception {
		
		report.startStep("Init Test Data");
		setTaskNavPathAndItemId("FD", 2, 1, 1, 1);
				
		report.startStep("Navigate to "+ courseName +" - Unit "+ unitNum +" - Lesson "+ lessonNum +" - Step "+ stepNum +" - Task "+ taskNum);
		learningArea2 = homePage.navigateToTask(courseCode, unitNum, lessonNum, stepNum, taskNum);
		
		report.startStep("Make progress action");
		learningArea2.clickOnPage2();
		sleep(2);
		learningArea2.clickOnPlayVideoButtonPrepare();

		report.startStep("Verify Progress Set in DB / Set in UI of LA / Get in UI of HP");
		verifyProgressSetGetPositive(true);

	}
	
	@Test
	@TestCaseParams(testCaseID = { "22881" })
	public void testSetProgressExploreSpeakingNegative() throws Exception {
		
		report.startStep("Init Test Data");
		setTaskNavPathAndItemId("B3", 4, 3, 1, 1);
				
		report.startStep("Navigate to "+ courseName +" - Unit "+ unitNum +" - Lesson "+ lessonNum +" - Step "+ stepNum +" - Task "+ taskNum);
		learningArea2 = homePage.navigateToTask(courseCode, unitNum, lessonNum, stepNum, taskNum);
		
		report.startStep("Click on See Text");
		learningArea2.clickOnSeeText();
		
		report.startStep("Verify Progress NOT Set in DB / Set in UI of LA / Get in UI of HP");
		verifyProgressSetGetNegative(true);

	}
	
	@Test
	@TestCaseParams(testCaseID = { "22975" })
	public void testSetProgressTestGrammar() throws Exception {
		
		report.startStep("Init Test Data");
		setTaskNavPathAndItemId("I2", 5, 4, 3, 4);
				
		report.startStep("Navigate to "+ courseName +" - Unit "+ unitNum +" - Lesson "+ lessonNum +" - Step "+ stepNum +" - Task "+ taskNum);
		learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, courseCode, unitNum, lessonNum);
		learningArea2.clickOnStep(stepNum, false);
		sleep(2);

		report.startStep("Start the Test");
		learningArea2.clickOnStartTest();
		learningArea2.clickOnTaskByNumber(taskNum);

		report.startStep("Submit the test, check that all tasks are done");
		learningArea2.submitTest(true);
		sleep(2);
		learningArea2.clickOnReviewTestResults();
		learningArea2.openTaskBar();
		for (int i = 0; i < 4; i++) {
			learningArea2.checkIfTaskHasDoneState(i+1, true);
		}
		learningArea2.closeTaskBar();
		
		String compSubComp = getCompoSubCompByCourseUnitAndStep(courseId, unitNum, lessonNum, stepNum);
		studentService.checkStudentTestResult(studentId, compSubComp);
		
		report.startStep("Go to Home Page and check course completion value in widget");
		verifyCourseCompletionOnHomeWidgetIsNotZero();

	}
	
	@Test
	@TestCaseParams(testCaseID = { "22885" })
	public void testSetProgressExploreListeningTVNegative() throws Exception {
	
		report.startStep("Init Test Data");
		setTaskNavPathAndItemId("FD", 2, 1, 2, 1);
				
		report.startStep("Navigate to "+ courseName +" - Unit "+ unitNum +" - Lesson "+ lessonNum +" - Step "+ stepNum +" - Task "+ taskNum);
		learningArea2 = homePage.navigateToTask(courseCode, unitNum, lessonNum, stepNum, taskNum);
		
		report.startStep("Make actions which do not cause progress done");
		learningArea2.clickOnSeeText();
		
		report.startStep("Verify Progress NOT Set in DB / Set in UI of LA / Get in UI of HP");
		verifyProgressSetGetNegative(false);

	}
	
	
	
	@Test
	@TestCaseParams(testCaseID = { "22887" })
	public void testSetProgressExploreReadingPositive1() throws Exception {
		
		report.startStep("Init Test Data");
		setTaskNavPathAndItemId("A3", 5, 2, 1, 1);
				
		report.startStep("Navigate to "+ courseName +" - Unit "+ unitNum +" - Lesson "+ lessonNum +" - Step "+ stepNum +" - Task "+ taskNum);
		learningArea2 = homePage.navigateToTask(courseCode, unitNum, lessonNum, stepNum, taskNum);
		
		report.startStep("Make progress action");
		learningArea2.clickOnHearAll();
		sleep(2);
		learningArea2.clickOnKeyWords();
		
		report.startStep("Verify Progress Set in DB / Set in UI of LA / Get in UI of HP");
		verifyProgressSetGetPositive(true);
				

	}
	
	@Test
	@TestCaseParams(testCaseID = { "22889" })
	public void testSetProgressExporeVocabularyNewPositive2() throws Exception {
		
		report.startStep("Init Test Data");
		setTaskNavPathAndItemId("B1", 2, 6, 1, 1);
				
		report.startStep("Navigate to "+ courseName +" - Unit "+ unitNum +" - Lesson "+ lessonNum +" - Step "+ stepNum +" - Task "+ taskNum);
		learningArea2 = homePage.navigateToTask(courseCode, unitNum, lessonNum, stepNum, taskNum);
		
		report.startStep("Make progress action");
		learningArea2.clickOnWordByIndex(4);
		sleep(1);
		learningArea2.clickOnToolHear();
		sleep(1);
		
		report.startStep("Verify Progress Set in DB / Set in UI of LA / Get in UI of HP");
		verifyProgressSetGetPositive(true);
		
	}
	
	@Test
	@TestCaseParams(testCaseID = { "22891" })
	public void testSetProgressExploreAlphabetPositive() throws Exception {
		
		report.startStep("Init Test Data");
		setTaskNavPathAndItemId("FD", 1, 1, 2, 1);
				
		report.startStep("Navigate to "+ courseName +" - Unit "+ unitNum +" - Lesson "+ lessonNum +" - Step "+ stepNum +" - Task "+ taskNum);
		learningArea2 = homePage.navigateToTask(courseCode, unitNum, lessonNum, stepNum, taskNum);
		
		report.startStep("Make progress action");
		learningArea2.clickOnVocabWordByIndex(2);
		
		report.startStep("Verify Progress Set in DB / Set in UI of LA / Get in UI of HP");
		verifyProgressSetGetPositive(true);

	}
	
	
		
	@Test
	@TestCaseParams(testCaseID = { "22895" }, skippedBrowsers = {"firefox"}) // Actions API not supported by geckodriver)
	public void testSetProgressPracticeListeningCloseDragAndDropNegative() throws Exception {
		
report.startStep("Init Test Data");
		setTaskNavPathAndItemId("A3", 6, 1, 2, 2);
				
report.startStep("Navigate to "+ courseName +" - Unit "+ unitNum +" - Lesson "+ lessonNum +" - Step "+ stepNum +" - Task "+ taskNum);
		learningArea2 = homePage.navigateToTask(courseCode, unitNum, lessonNum, stepNum, taskNum);
		
report.startStep("Make actions which do not cause progress done");
			learningArea2.clickOnClearAnswer();
		
report.startStep("Verify Progress NOT Set in DB / Set in UI of LA / Get in UI of HP");
		verifyProgressSetGetNegative(false);
	
	}
	
	//Test SetProgress Negative for Close New Template
	@Test
	@TestCaseParams(testCaseID = { "22895" }, skippedBrowsers = {"firefox"}) // Actions API not supported by geckodriver)
	public void testSetProgressPracticeCloseDragAndDropNegative2() throws Exception {
		
		report.startStep("Init Test Data");
		setTaskNavPathAndItemId("A1", 2, 5, 2, 7);
				
		report.startStep("Navigate to "+ courseName +" - Unit "+ unitNum +" - Lesson "+ lessonNum +" - Step "+ stepNum +" - Task "+ taskNum);
		learningArea2 = homePage.navigateToTask(courseCode, unitNum, lessonNum, stepNum, taskNum);
		
		report.startStep("Make actions which do not cause progress done");
		learningArea2.clickOnClearAnswer();
		
		report.startStep("Verify Progress NOT Set in DB / Set in UI of LA / Get in UI of HP");
		verifyProgressSetGetNegative(false);
	}
	
	@Test
	@TestCaseParams(testCaseID = { "22900" })
	public void testSetProgressPracticeListeningMarkTheTrueNegative() throws Exception {
	
		report.startStep("Init Test Data");
		setTaskNavPathAndItemId("B3", 3, 2, 2, 2);
				
		report.startStep("Navigate to "+ courseName +" - Unit "+ unitNum +" - Lesson "+ lessonNum +" - Step "+ stepNum +" - Task "+ taskNum);
		learningArea2 = homePage.navigateToTask(courseCode, unitNum, lessonNum, stepNum, taskNum);
		
		report.startStep("Make actions which do not cause progress done");
		learningArea2.clickOnCheckAnswer();

		report.startStep("Verify Progress NOT Set in DB / Set in UI of LA / Get in UI of HP");
		verifyProgressSetGetNegative(false);
	}
	
	@Test
	@TestCaseParams(testCaseID = { "22938" })
	public void testSetProgressPracticeListeningFillTheBlanksNegative() throws Exception {
		
	report.startStep("Init Test Data");
		setTaskNavPathAndItemId("A3", 6, 1, 2, 3);
				
	report.startStep("Navigate to "+ courseName +" - Unit "+ unitNum +" - Lesson "+ lessonNum +" - Step "+ stepNum +" - Task "+ taskNum);
		learningArea2 = homePage.navigateToTask(courseCode, unitNum, lessonNum, stepNum, taskNum);
		
	report.startStep("Make actions which do not cause progress done");
		learningArea2.clickOnSeeAnswer();
		sleep(2);

	report.startStep("Verify Progress NOT Set in DB / Set in UI of LA / Get in UI of HP");
		verifyProgressSetGetNegative(false);
	
	
	}
	
	@Test
	@TestCaseParams(testCaseID = { "48464" })
	public void testSetProgressPracticeListeningMCQNegative() throws Exception {
		
		report.startStep("Init Test Data");
		setTaskNavPathAndItemId("FD", 5, 1, 3, 3);
				
		report.startStep("Navigate to "+ courseName +" - Unit "+ unitNum +" - Lesson "+ lessonNum +" - Step "+ stepNum +" - Task "+ taskNum);
		learningArea2 = homePage.navigateToTask(courseCode, unitNum, lessonNum, stepNum, taskNum);
		
		report.startStep("Make actions which do not cause progress done");
		learningArea2.clickOnCheckAnswer();
		sleep(1);

		report.startStep("Verify Progress NOT Set in DB / Set in UI of LA / Get in UI of HP");
		verifyProgressSetGetNegative(false);
	}
	
	@Test
	@TestCaseParams(testCaseID = { "22974" })
	public void testSetProgressTestGrammarNegative() throws Exception {
		
		report.startStep("Init Test Data");
		setTaskNavPathAndItemId("I2", 5, 4, 3, 4);
				
		report.startStep("Navigate to "+ courseName +" - Unit "+ unitNum +" - Lesson "+ lessonNum +" - Step "+ stepNum +" - Task "+ taskNum);
		learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, courseCode, unitNum, lessonNum);
		learningArea2.clickOnStep(stepNum, false);
		sleep(2);
		
		report.startStep("Start the Test");
		learningArea2.clickOnStartTest();
		sleep(2);
		learningArea2.clickOnTaskByNumber(taskNum);
		sleep(2);
		learningArea2.clickOnPlayVideoButton();
		sleep(2);
		learningArea2.clickOnStep(2, false);
		
		report.startStep("Exit Test without Submit");
		learningArea2.approveTest();
		
		String compSubComp = getCompoSubCompByCourseUnitAndStep(courseId, unitNum, lessonNum, stepNum);
		studentService.checkStudentHasNoTestProgress(studentId, compSubComp);
		
		report.startStep("Check Test Step Not Started");
		learningArea2.openStepsList();
		learningArea2.checkProgressIndicationInStepList(stepNum, StepProgressBox.empty, true, "");
		learningArea2.closeStepsList();
				
		report.startStep("Refresh and check progress indications");
		webDriver.refresh();
		sleep(2);
				
		report.startStep("Check Test Step Not Started after Refresh");
		learningArea2.openStepsList();
		learningArea2.checkProgressIndicationInStepList(stepNum, StepProgressBox.empty, true, "");
		learningArea2.closeStepsList();
	}

	@Test
	@TestCaseParams(testCaseID = { "33778" })
	public void testSetProgressPracticeInsertText() throws Exception {
		
		report.startStep("Init Test Data");
		setTaskNavPathAndItemId("B1", 9, 5, 3, 6);
				
		report.startStep("Navigate to "+ courseName +" - Unit "+ unitNum +" - Lesson "+ lessonNum +" - Step "+ stepNum +" - Task "+ taskNum);
		learningArea2 = homePage.navigateToTask(courseCode, unitNum, lessonNum, stepNum, taskNum);
		
		report.startStep("Make actions which do not cause progress done");
		learningArea2.clickOnSeeAnswer();
		
		report.startStep("Verify Progress NOT Set in DB / Set in UI of LA / Get in UI of HP");
		verifyProgressSetGetNegative(false);
		
		report.startStep("Navigate to "+ courseName +" - Unit "+ unitNum +" - Lesson "+ lessonNum +" - Step "+ stepNum +" - Task "+ taskNum);
		learningArea2.clickOnTaskByNumber(taskNum);
		//RM:
		learningArea2.closeTaskBar();
				
		report.startStep("Make progress action");
		learningArea2.clickOnMarkerAndVerifySentenceInsert(1, learningArea2.getInsertSentenceAnswerElement().getText());
				
		report.startStep("Verify Progress Set in DB / Set in UI of LA / Get in UI of HP");
		verifyProgressSetGetPositive(false);	

	}
	
	@Test
	@TestCaseParams(testCaseID = { "33628" })
	public void testSetProgressPracticeEditText() throws Exception {
		
		report.startStep("Init Test Data");
		setTaskNavPathAndItemId("B3", 9, 5, 4, 1);
				
		report.startStep("Navigate to "+ courseName +" - Unit "+ unitNum +" - Lesson "+ lessonNum +" - Step "+ stepNum +" - Task "+ taskNum);
		learningArea2 = homePage.navigateToTask(courseCode, unitNum, lessonNum, stepNum, taskNum);
		
		report.startStep("Make actions which do not cause progress done");
		learningArea2.clickOnSeeAnswer();
		
		report.startStep("Verify Progress NOT Set in DB / Set in UI of LA / Get in UI of HP");
		verifyProgressSetGetNegative(false);
				
		report.startStep("Make progress action");
		learningArea2.getEditTextInputElements().get(1).sendKeys("sample");
		sleep(2);
		
		report.startStep("Verify Progress Set in DB / Set in UI of LA / Get in UI of HP");
		verifyProgressSetGetPositive(false);				

	}
	
	@Test
	@TestCaseParams(testCaseID = { "33907" })
	public void testSetProgressPracticeHighlightText() throws Exception {
		
		report.startStep("Init Test Data");
		setTaskNavPathAndItemId("I1", 10, 2, 3, 2);
				
		report.startStep("Navigate to "+ courseName +" - Unit "+ unitNum +" - Lesson "+ lessonNum +" - Step "+ stepNum +" - Task "+ taskNum);
		learningArea2 = homePage.navigateToTask(courseCode, unitNum, lessonNum, stepNum, taskNum);
		
		report.startStep("Make actions which do not cause progress done");
		learningArea2.clickOnSeeAnswer();
		
		report.startStep("Verify Progress NOT Set in DB / Set in UI of LA / Get in UI of HP");
		verifyProgressSetGetNegative(false);
				
		report.startStep("Navigate to "+ courseName +" - Unit "+ unitNum +" - Lesson "+ lessonNum +" - Step "+ stepNum +" - Task "+ taskNum);
		learningArea2.clickOnTaskByNumber(taskNum);
		learningArea2.closeTaskBar();
//		Thread.sleep(2000);
		
		report.startStep("Make progress action");
		learningArea2.SelectRadioBtn("question-1_answer-1");
		
		report.startStep("Verify Progress Set in DB / Set in UI of LA / Get in UI of HP");
		verifyProgressSetGetPositive(false);		
	}
			
	@Test
	@TestCaseParams(testCaseID = { "34168" })
	public void testSetProgressPracticeOpenWriting() throws Exception {
		
		report.startStep("Init Test Data");
		setTaskNavPathAndItemId("B1", 9, 3, 4, 3);
				
		report.startStep("Navigate to "+ courseName +" - Unit "+ unitNum +" - Lesson "+ lessonNum +" - Step "+ stepNum +" - Task "+ taskNum);
		learningArea2 = homePage.navigateToTask(courseCode, unitNum, lessonNum, stepNum, taskNum);
		sleep(3);
		report.startStep("Make actions which do not cause progress done");
		webDriver.swithcToFrameAndSendKeys(htmlEditorXpath,freeText, htmlEditorFrame);
		webDriver.switchToTopMostFrame();
		learningArea2.clickOnBackButton();
		webDriver.closeAlertByAccept(2);
		
		report.startStep("Verify Progress NOT Set in DB / Set in UI of LA / Get in UI of HP");
		verifyProgressSetGetNegative(false);
				
		report.startStep("Navigate to "+ courseName +" - Unit "+ unitNum +" - Lesson "+ lessonNum +" - Step "+ stepNum +" - Task "+ taskNum);
		sleep(1);
		learningArea2.clickOnTaskByNumber(taskNum);
		sleep(1);
		
		report.startStep("Make progress action");
		webDriver.swithcToFrameAndSendKeys(htmlEditorXpath,freeText, htmlEditorFrame);
		webDriver.switchToTopMostFrame();
		learningArea2.clickOnSubmitToTeacherOW();
		learningArea2.closeAlertModalByAccept();
		learningArea2.clickOnBackButton();
		learningArea2.closeAlertModalByAccept();
		
		report.startStep("Verify Progress Set in DB / Set in UI of LA / Get in UI of HP");
		verifyProgressSetGetPositive(false);		
		
	}
	
	@Test
	@TestCaseParams(testCaseID = { "40717" })
	public void testSetProgressPracticeOpenWritingNoTeacher() throws Exception {
		
		report.startStep("Create and login to ED as student and navigate to Free Writing task");
		homePage.clickOnLogOut();
		webDriver.switchToTopMostFrame();
		
		String className = configuration.getProperty("classname.classNoTeacher");
		
		homePage =  createUserAndLoginNewUXClass(className, institutionId);
		
		//studentId = pageHelper.createUSerUsingSP(institutionId, className);
		//String studentUserName = dbService.getUserNameById(studentId, institutionId);
		//loginPage = new NewUXLoginPage(webDriver,testResultService);
		//homePage = loginPage.loginAsStudent(studentUserName, "12345");
		
		homePage.skipNotificationWindow();
		homePage.waitHomePageloadedFully();
		
		report.startStep("Init Test Data");
		setTaskNavPathAndItemId("B1", 9, 3, 4, 3);
				
		report.startStep("Navigate to "+ courseName +" - Unit "+ unitNum +" - Lesson "+ lessonNum +" - Step "+ stepNum +" - Task "+ taskNum);
		learningArea2 = homePage.navigateToTask(courseCode, unitNum, lessonNum, stepNum, taskNum);
		learningArea2.waitForPageToLoad();
		
		report.startStep("Make actions which do not cause progress done");
		learningArea2.clickOnClearAnswer();
		learningArea2.clickOnBackButton();
		webDriver.closeAlertByAccept(2);
		
		report.startStep("Verify Progress NOT Set in DB / Set in UI of LA / Get in UI of HP");
		verifyProgressSetGetNegative(false);
				
		report.startStep("Navigate to "+ courseName +" - Unit "+ unitNum +" - Lesson "+ lessonNum +" - Step "+ stepNum +" - Task "+ taskNum);
		learningArea2.clickOnTaskByNumber(taskNum);
		
		report.startStep("Make progress action");
		webDriver.swithcToFrameAndSendKeys(htmlEditorXpath, freeText, htmlEditorFrame);
		webDriver.switchToTopMostFrame();
		learningArea2.clickOnBackButton();
		webDriver.closeAlertByAccept(2);
	
		report.startStep("Verify Progress Set in DB / Set in UI of LA / Get in UI of HP");
		verifyProgressSetGetPositive(false);			

	}
	
	@Test
	@TestCaseParams(testCaseID = { "22966" })
	public void testSetProgressPracticeFreeWriteNonErater() throws Exception {
		
		report.startStep("Init Test Data");
		setTaskNavPathAndItemId("FD", 2, 4, 3, 3);
		String texInput = "Hello";
		String sectionNum = "1";
				
		report.startStep("Navigate to "+ courseName +" - Unit "+ unitNum +" - Lesson "+ lessonNum +" - Step "+ stepNum +" - Task "+ taskNum);
		learningArea2 = homePage.navigateToTask(courseCode, unitNum, lessonNum, stepNum, taskNum);
		
		report.startStep("Make actions which do not cause progress done");
		learningArea2.enterFreeWritingTextBySection(sectionNum, texInput);
		learningArea2.clickOnBackButton();
		
		report.startStep("Verify Progress NOT Set in DB / Set in UI of LA / Get in UI of HP");
		verifyProgressSetGetNegative(false);
				
		report.startStep("Navigate to "+ courseName +" - Unit "+ unitNum +" - Lesson "+ lessonNum +" - Step "+ stepNum +" - Task "+ taskNum);
		learningArea2.clickOnTaskByNumber(taskNum);
		
		report.startStep("Make progress action");
		learningArea2.enterFreeWritingTextBySection(sectionNum, texInput);
		learningArea2.clickOnSendToTeacher();
		learningArea2.closeAlertModalByAccept();
		sleep(1);
				
		report.startStep("Verify Progress Set in DB / Set in UI of LA / Get in UI of HP");
		verifyProgressSetGetPositive(false);				

	}
	
	@Test
	@TestCaseParams(testCaseID = { "40716" })
	public void testSetProgressPracticeFreeWriteNoEraterNoTeacher() throws Exception {
				
		report.startStep("Create and login to ED as student and navigate to Free Writing task");
		homePage.clickOnLogOut();
		webDriver.switchToTopMostFrame();
		
		String className = configuration.getProperty("classname.classNoTeacher");
		homePage =  createUserAndLoginNewUXClass(className, institutionId);
		
		homePage.skipNotificationWindow();
		homePage.waitHomePageloadedFully();
			
		report.startStep("Init Test Data");
		setTaskNavPathAndItemId("FD", 2, 4, 3, 3);
		String texInput = "Hello";
		String sectionNum = "1";
				
		report.startStep("Navigate to "+ courseName +" - Unit "+ unitNum +" - Lesson "+ lessonNum +" - Step "+ stepNum +" - Task "+ taskNum);
		learningArea2 = homePage.navigateToTask(courseCode, unitNum, lessonNum, stepNum, taskNum);
		learningArea2.waitToLearningAreaLoaded();
		
		report.startStep("Make actions which do not cause progress done");
		learningArea2.clickOnClearAnswer();
		learningArea2.clickOnBackButton();
		
		report.startStep("Verify Progress NOT Set in DB / Set in UI of LA / Get in UI of HP");
		verifyProgressSetGetNegative(false);
				
		report.startStep("Navigate to "+ courseName +" - Unit "+ unitNum +" - Lesson "+ lessonNum +" - Step "+ stepNum +" - Task "+ taskNum);
		learningArea2.clickOnTaskByNumber(taskNum);
		
		report.startStep("Make progress action");
		learningArea2.enterFreeWritingTextBySection(sectionNum, texInput);
		sleep(2);
		
		report.startStep("Verify Progress Set in DB / Set in UI of LA / Get in UI of HP");
		verifyProgressSetGetPositive(false);	

	}
	
	@Test
	@TestCaseParams(testCaseID = { "33851" })
	public void testSetProgressPracticeSelectText() throws Exception {
		
		report.startStep("Init Test Data");
		setTaskNavPathAndItemId("B1", 9, 1, 5, 2);
				
		report.startStep("Navigate to "+ courseName +" - Unit "+ unitNum +" - Lesson "+ lessonNum +" - Step "+ stepNum +" - Task "+ taskNum);
		learningArea2 = homePage.navigateToTask(courseCode, unitNum, lessonNum, stepNum, taskNum);
		
		report.startStep("Make actions which do not cause progress done");
		learningArea2.clickOnSeeAnswer();
		sleep(1);
		
		report.startStep("Verify Progress NOT Set in DB / Set in UI of LA / Get in UI of HP");
		verifyProgressSetGetNegative(false);
				
		report.startStep("Navigate to "+ courseName +" - Unit "+ unitNum +" - Lesson "+ lessonNum +" - Step "+ stepNum +" - Task "+ taskNum);
		learningArea2.clickOnTaskByNumber(taskNum);
		//RM:
		learningArea2.closeTaskBar();
		
		report.startStep("Make progress action");
		learningArea2.clickOnMarkerAndVerifySentenceSelect(1);
		sleep(1);
		
		report.startStep("Verify Progress Set in DB / Set in UI of LA / Get in UI of HP");
		verifyProgressSetGetPositive(false);		
		
	}

	@Test
	@TestCaseParams(testCaseID = { "35146" })
	public void testSetProgressReadingEmptyTask() throws Exception {
		
		report.startStep("Init Test Data");
		setTaskNavPathAndItemId("B1", 9, 2, 1, 1);
				
		report.startStep("Navigate to "+ courseName +" - Unit "+ unitNum +" - Lesson "+ lessonNum +" - Step "+ stepNum +" - Task "+ taskNum);
		learningArea2 = homePage.navigateToTask(courseCode, unitNum, lessonNum, stepNum, taskNum);
		sleep(2);
		
		report.startStep("Verify Progress Set in DB / Set in UI of LA / Get in UI of HP");
		verifyProgressSetGetPositive(true);		
		
		
	}
		
	private String getItemIdByCourseUnitLessonStepAndTask(String courseId, int unitIndex, int lessonIndex,
			int StepIndex, int taskIndex) throws Exception {
		
		String itemId = pageHelper.getItemIdByCourseUnitLessonStepAndTask(courseId, unitIndex, lessonIndex, StepIndex, taskIndex);
				
		return itemId;
	}
	
	private void verifyCourseCompletionOnHomeWidgetIsNotZero() throws Exception {
		
		//homePage.clickToOpenNavigationBar();
		sleep(1);
		homePage.clickOnHomeButton();
		homePage.waitHomePageloadedFully();
		
		String completionProgressFD = homePage.getCompletionWidgetValue();
		testResultService.assertTrue("No progress displayed in student statistics",!completionProgressFD.equalsIgnoreCase("0"));
	}
	
	private void setTaskNavPathAndItemId(String courseCode, int unitNum, int lessonNum, int stepNum, int taskNum) throws Exception {
		
		this.courseCode = courseCode;
		this.unitNum = unitNum;
		this.lessonNum = lessonNum;
		this.stepNum = stepNum;
		this.taskNum = taskNum;
		
		this.courseName = getCourseNameByCourseCode(courseCode);
		this.courseId = getCourseIdByCourseCode(courseCode);
		this.itemId = getItemIdByCourseUnitLessonStepAndTask(courseId, unitNum, lessonNum, stepNum, taskNum);
		
	}
	
	public void setTOEICTaskNavPathAndItemId(String courseCode, int unitNum, int lessonNum, int stepNum, int taskNum) throws Exception {
		
		this.courseCode = courseCode;
		this.unitNum = unitNum;
		this.lessonNum = lessonNum;
		this.stepNum = stepNum;
		this.taskNum = taskNum;
		
		this.courseName = getTOEICCourseNameByCourseCode(courseCode);
		this.courseId = getTOEICCourseIdByCourseCode(courseCode);
		this.itemId = getItemIdByCourseUnitLessonStepAndTask(courseId, unitNum, lessonNum, stepNum, taskNum);
		
	}
	
	
	private void verifyProgressIndicationInTaskStepLesson(boolean isTaskDone, StepProgressBox stepProgressBar, int lessonProgressInPercent) throws Exception {
		
		report.startStep("Check task status Done in Task Bar Task: " + taskNum);
		learningArea2.openTaskBar();
		sleep(1);
		learningArea2.checkIfTaskHasDoneState(taskNum, isTaskDone);
		learningArea2.closeTaskBar();
		
		report.startStep("Check Step In Progress, Step:" + stepNum);
		learningArea2.openStepsList();
		sleep(1);
		learningArea2.checkProgressIndicationInStepList(stepNum, stepProgressBar, false, "undefined");
		learningArea2.closeStepsList();
		
		report.startStep("Check Lesson In Progress , Lesson: " + lessonNum);
		learningArea2.openLessonsList();
		sleep(1);
		learningArea2.checkProgressIndicationInLessonList(lessonNum, lessonProgressInPercent);
		learningArea2.closeLessonsList();
		
	}
	
	public void verifyProgressSetGetPositive(boolean isSingleTaskInStep) throws Exception {
		
		report.startStep("Check progress set in the DB");
		studentService.checkStudentProgress(studentId, courseId, itemId);
		//sleep(1);
				
		report.startStep("Check progress indications updated in LA");
		StepProgressBox stepProgress;
		if (isSingleTaskInStep) stepProgress = StepProgressBox.done;
		else stepProgress = StepProgressBox.half;
		verifyProgressIndicationInTaskStepLesson(true, stepProgress, 1);
		
		report.startStep("Click on Next & Back btn and check progress indications");
		learningArea2.clickOnNextButton();
		learningArea2.clickOnBackButton();
		verifyProgressIndicationInTaskStepLesson(true, stepProgress, 1);
				
		report.startStep("Refresh and check progress indications");
		webDriver.refresh();
		learningArea2.waitToLearningAreaLoaded();
		
		if(stepNum > 1)
			learningArea2.clickOnStep(stepNum);
		
		verifyProgressIndicationInTaskStepLesson(true, stepProgress, 1);

		report.startStep("Go to Home Page and check course completion value in widget");
		verifyCourseCompletionOnHomeWidgetIsNotZero();

	/*	 Comment needs to remove when it will fix 
		report.startStep("Check UserCourseProgress table that updated");
		String progressDone = studentService.getStudentCompletionCalculated(studentId, courseId);
		testResultService.assertEquals("1", progressDone, "Wrong progress percentage saved in UserCourseProgress");
	*/
	}
	
	private void verifyProgressSetGetNegative(boolean isFirstStepInLesson) throws Exception {
		
		report.startStep("Check progress NOT set in the DB");
		studentService.checkStudentHasNoProgress(studentId, courseId, itemId);
		
		report.startStep("Check task status Not Started in Task Bar");
		learningArea2.openTaskBar();
		learningArea2.checkIfTaskHasDoneState(taskNum, false);
		learningArea2.closeTaskBar();
		
		report.startStep("Check Step Not Started");
		learningArea2.openStepsList();
		learningArea2.checkProgressIndicationInStepList(stepNum, StepProgressBox.empty, false, "undefined");
		learningArea2.closeStepsList();
				
		report.startStep("Refresh and check progress indications");
		webDriver.refresh();
		learningArea2.waitToLearningAreaLoaded();
		
		if(!isFirstStepInLesson)
			learningArea2.clickOnStep(stepNum);
		
		report.startStep("Check task status Not Started in Task Bar after Refresh");
		learningArea2.openTaskBar();
		learningArea2.checkIfTaskHasDoneState(taskNum, false);
		learningArea2.closeTaskBar();
		
		report.startStep("Check Step Not Started after Refresh");
		learningArea2.openStepsList();
		learningArea2.checkProgressIndicationInStepList(stepNum, StepProgressBox.empty, false, "undefined");
		learningArea2.closeStepsList();
		
	}
	
	private String getCompoSubCompByCourseUnitAndStep(String courseId, int unitIndex, int compIndex, int subCompIndex)
			throws Exception {
		String unitId = dbService.getCourseUnits(courseId).get(unitIndex - 1);

		// lesson id
		String compId = dbService.getComponentDetailsByUnitId(unitId).get(compIndex - 1)[1];
		// step id
		String SubComp = dbService.getSubComponentsDetailsByComponentId(compId).get(subCompIndex - 1)[1];

		return SubComp;
	}
	
	
	@After
	public void tearDown() throws Exception {
		
		//report.startStep("Set No Support Language");
		//pageHelper.setUserLangSupportLevel(studentId, 0);
		
		super.tearDown();
	}

}
