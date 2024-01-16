package tests.edo.newux;

import Enums.TaskTypes;
import Interfaces.TestCaseParams;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import pageObjects.edo.NewUxDragAndDropSection2;
import pageObjects.edo.NewUxLearningArea2;
import pageObjects.edo.NewUxMyProgressPage;
import testCategories.edoNewUX.CourseArea;
import testCategories.edoNewUX.SanityTests;

import java.util.ArrayList;
import java.util.List;

//@Category(AngularLearningArea.class)
public class LearningAreaStepsTests2 extends BasicNewUxTest {

	NewUxLearningArea2 learningArea2;
	NewUxMyProgressPage myProgress;
	
	static final String interact1Instruction = "Take part in the conversation. Select the speaker you would like to practice.";
	
	
	@Test
	@Category(CourseArea.class)
	@TestCaseParams(testCaseID = { "41126", "41130", "41014" }, skippedBrowsers = {"firefox"}) // Actions API not supported by geckodriver)
	public void testStepListDataLabels() throws Exception {

		//String[] expectedSteps = new String[] { "Prepare", "Explore", "Practice", "Interact 1", "Interact 2" };

		int[] testSet1 = new int[] { 1, 4, 1 }; // course index: B1, unit number: 4, lesson number: 1 (Business) - 3 steps
				
		int[] testSet2 = new int[] { 1, 2, 2 }; // course index: B1, unit number: 2, lesson number: 2 (Follow That Man) - 3 steps
		
		int[] testSet3 = new int[] { 2, 2, 3 }; // course index: B2, unit number: 2, lesson number: 3 (Piece of Cake) - 3 steps

		int[] testSet4 = new int[] { 3, 3, 4 }; // course index: B3, unit number: 3, lesson number: 4 (Modals) - 3 steps
		
		int[] testSet5 = new int[] { 3, 1, 7 }; // course index: B3, unit number: 1, lesson number: 7 (Education) - 3 steps
		
		int[] testSet6 = new int[] { 3, 9, 4 }; // course index: B3, unit number: 9, lesson number: 4 (Looking for a Washing Machine) - 7 steps
		
		List<int[]> testDataList = new ArrayList<int[]>();
		testDataList.add(testSet1);
		testDataList.add(testSet2);
		testDataList.add(testSet3);
		testDataList.add(testSet4);
		testDataList.add(testSet5);
		testDataList.add(testSet6);

		report.startStep("Login to ED as Student");
		homePage = getUserAndLoginNewUXClass();

		for (int j = 0; j < testDataList.size(); j++) {

			report.startStep("Navigate to a course: " + coursesNames[testDataList.get(j)[0]]);
			homePage.navigateToRequiredCourseOnHomePage(coursesNames[testDataList.get(j)[0]]);
			sleep(1);
			
			// units
			report.startStep("Navigate to a Unit: " + courses[testDataList.get(j)[0]]);
			List<String[]> unitsList = dbService.getCourseUnitDetils(courses[testDataList.get(j)[0]]);
			int unitIndex = testDataList.get(j)[1];
			// lessons
			report.startStep("Navigate to a Lesson: " + unitsList.get(unitIndex - 1)[0]);
			List<String[]> componentsList = dbService.getComponentDetailsByUnitId(unitsList.get(unitIndex - 1)[0]);
			int skill_ID = Integer.parseInt(componentsList.get(testDataList.get(j)[2]-1)[2]);
			// steps
			report.startStep("Navigate to a Step: " + componentsList.get(testDataList.get(j)[2] - 1)[1]);
			List<String[]> subComponetnsList = dbService.getSubComponentsDetailsByComponentId(componentsList.get(testDataList.get(j)[2] - 1)[1]);

			report.startStep("Click on lesson" + testDataList.get(j)[1]);
			homePage.clickOnUnitLessons(testDataList.get(j)[1]);
			sleep(1);
			homePage.clickOnLesson(testDataList.get(j)[1] - 1, testDataList.get(j)[2]);			
			sleep(1);
			learningArea2 = new NewUxLearningArea2(webDriver, testResultService);
					
			report.startStep("Verify steps of Test Set : " + (j+1));
			
			for (int i = 0; i < subComponetnsList.size(); i++) {
			
				String expStepName = subComponetnsList.get(i)[0].trim();
				
				boolean isTest = false;
				if (expStepName.equalsIgnoreCase("Test") || expStepName.equalsIgnoreCase("Let's Review")) isTest = true;
				
				if (expStepName.equals("Practice 1") || expStepName.equals("Practice 2") ) expStepName = expStepName.replace("Practice", "Interact");
				int stepNumber = i+1;
								
				report.startStep("Verify Step Name in Header: " + expStepName);
				String actStepName = learningArea2.getStepNameFromHeader();
				testResultService.assertEquals(expStepName, actStepName);

				report.startStep("Verify Step Number in Header");
				String actStepNumber = learningArea2.getStepNumberFromHeader();
				testResultService.assertEquals(String.valueOf(stepNumber), actStepNumber);
				
				if (!isTest) {
					report.startStep("Verify Step Name in Intro:" + expStepName);
					actStepName = learningArea2.getStepNameFromIntro();
					testResultService.assertEquals(expStepName, actStepName);
	
					report.startStep("Verify Step Number in Intro");
					actStepNumber = learningArea2.getStepNumberFromIntro();
					testResultService.assertEquals(String.valueOf(stepNumber), actStepNumber);
					
					report.startStep("Verify Step Instruction");
					verifyStepInstruction(expStepName, skill_ID, stepNumber);
				
				}
				
				report.startStep("Open Step List");
				learningArea2.openStepsList();
				sleep(1);
				
				report.startStep("Check step current indication");
				learningArea2.verifyStepIsCurrentByNumber(stepNumber);
					
				report.startStep("Verify Step Name in List");
				actStepName = learningArea2.getStepName(stepNumber);
				testResultService.assertEquals(expStepName, actStepName);

				report.startStep("Verify Step Number in List");
				actStepNumber = learningArea2.getStepNumber(stepNumber);
				testResultService.assertEquals(String.valueOf(stepNumber), actStepNumber);
								
				report.startStep("Click on next step");
				if (stepNumber < subComponetnsList.size()) learningArea2.clickOnStep(stepNumber+1, false);
				else learningArea2.closeStepsList();
					

			}
			
			report.startStep("Navigate back to home page");
			sleep(1);
			//homePage.clickToOpenNavigationBar();
			homePage.clickOnHomeButton();
		}
		
	}
	
	@Test
	@Category(CourseArea.class)
	@TestCaseParams(testCaseID = { "41126", "41130", "41015"  }, skippedBrowsers = {"firefox"}) // Actions API not supported by geckodriver)
	public void testStepIntroHasNoPracticeTools() throws Exception {

		report.startStep("Login to ED and navigate to Intro of Practice Step");
		homePage = getUserAndLoginNewUXClass();
		learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "B1", 1, 1);
		sleep(1);
		learningArea2.clickOnStep(2, false);
		
		report.startStep("Verify No Practice Tools on Intro Page");
		learningArea2.checkThatCheckAnswerToolIsNotDisplayed();
		learningArea2.checkThatSeeAnswerToolIsNotDisplayed();
		learningArea2.checkThatClearToolIsNotDisplayed();
		
		report.startStep("Navigate Next and Back to Intro Page");
		learningArea2.clickOnNextButton();
		learningArea2.openTaskBar();
		learningArea2.clickOnIntro();
		sleep(1);
		
		report.startStep("Verify No Practice Tools on Intro Page");
		learningArea2.checkThatCheckAnswerToolIsNotDisplayed();
		learningArea2.checkThatSeeAnswerToolIsNotDisplayed();
		learningArea2.checkThatClearToolIsNotDisplayed();
		
		
	}
	
	@Test
	@TestCaseParams(testCaseID = { "56682" })
	public void testEnterLAFromMyProgress() throws Exception {
		
		report.startStep("Login and Init Test Data");
			homePage = getUserAndLoginNewUXClass();
			homePage.closeAllNotifications();
			
			int lessons[] = {4,2};
			String courseName = "Intermediate 1";
		
		for (int i = 0 ; i < 2 ; i++) {
			report.startStep("Login to ED enter Intermediate 1 My Progress Page");
				homePage.navigateToRequiredCourseOnHomePage(courseName);
				myProgress = homePage.clickOnMyProgress();
				sleep(2);
				
			report.startStep("Enter the none-first step in a lesson");
				myProgress.clickToOpenUnitLessonsProgress(4);
				sleep(3);
				String expLessonName = myProgress.getLessonName(lessons[i]);
				learningArea2 = myProgress.clickonStepInLesson(lessons[i],2);
				learningArea2.waitUntilLearningAreaLoaded();
				
			report.startStep("Verify expected step-lesson landing");
				testResultService.assertEquals(expLessonName,learningArea2.getLessonNameFromHeader(),"Wrong lesson navigated to");
				testResultService.assertEquals("Explore",learningArea2.getStepNameFromHeader(),"Wrong lesson navigated to");
				
				if (i == 1) 
					learningArea2.checkThatKeywordsToolIsDisplayed();
				else 
					learningArea2.checkThatSeeExplanationIsNotDisplayed();
				
			report.startStep("Return to HomePage");
				learningArea2.clickOnHomeButton();
				homePage.waitHomePageloaded();
		}
	}
		
	private void verifyStepInstruction(String expStepName, int skill_ID,  int stepNumber) throws Exception {
			
		String actualStepInst = "undefined";
		String expectedStepInst = "undefined";
		String stepInstructionInIntro = learningArea2.stepIntroInstruction.getText();
		
		learningArea2.openTaskBar();
		sleep(1);
		actualStepInst = learningArea2.stepInstruction.getText();
		testResultService.assertEquals(stepInstructionInIntro, actualStepInst,"Step Instruction on Intro Page different from Step Instruction in Task Dropdown");
					
		boolean stepInstExist = true;
		
		switch (skill_ID) {
			
			case 3: // Listening 
			
				switch (expStepName) {
			
					case "Prepare":
						stepInstExist = false; 
					break;
					
					case "Explore":
						expectedStepInst = "Listen to the recording. If you need help, use the student tools.";
 					break;
					
					case "Practice":
						expectedStepInst = "Listen to the recording again and do the tasks that follow. If you need help, use the student tools.";
					break;
					
					case "Interact 1":
						//expectedStepInst = "Take part in the conversation. Choose the speaker you would like to be.";
						//expectedStepInst = "Take part in the conversation. Select the speaker you would like to practice.";
						expectedStepInst = interact1Instruction;
					break;
					
					case "Interact 2":
						expectedStepInst = "Take part in a branching conversation. Choose your response and see how the conversation develops.";
					break;
					
					case "Test":
						expectedStepInst = "Take the quiz to test your understanding.";
					break;
				}
			
			break;
			
			case 2: // Reading 
				
				switch (expStepName) {
			
					case "Prepare":
						stepInstExist = false; 
					break;
					
					case "Explore":
						expectedStepInst = "Read the story. If you need help, use the student tools.";
 					break;
					
					case "Practice":
						expectedStepInst = "Read the story again and do the tasks that follow. If you need help, use the student tools.";
					break;
					
					case "Interact 1":
						//expectedStepInst = "Take part in the conversation. Choose the speaker you would like to be.";
						//expectedStepInst = "Take part in the conversation. Select the speaker you would like to practice.";
						expectedStepInst = interact1Instruction;
					break;
					
					case "Interact 2":
						expectedStepInst = "Take part in a branching conversation. Choose your response and see how the conversation develops.";
					break;
					
					case "Test":
						expectedStepInst = "Take the quiz to test your understanding.";
					break;
				}
			
			break;

			case 4: // Speaking 
				
				switch (expStepName) {
			
					case "Prepare":
						stepInstExist = false; 
					break;
					
					case "Explore":
						expectedStepInst = "Listen to the conversation. If you need help, use the student tools.";
 					break;
					
					case "Practice":
						expectedStepInst = "Listen to the conversation again and do the tasks that follow. If you need help, use the student tools.";
					break;
					
					case "Interact 1":
						//expectedStepInst = "Take part in the conversation. Choose the speaker you would like to be.";
						//expectedStepInst = "Take part in the conversation. Select the speaker you would like to practice.";
						expectedStepInst = interact1Instruction;
					break;
					
					case "Interact 2":
						expectedStepInst = "Take part in a branching conversation. Choose your response and see how the conversation develops.";
					break;
					
					case "Test":
						expectedStepInst = "Take the quiz to test your understanding.";
					break;
					
					case "Getting ready to watch a video":
						expectedStepInst = "You are going to watch a conversation between a salesclerk and a customer. Look at the picture from the video. Where does the conversation take place?";
					break;
					
					case "While you watch":
						expectedStepInst = "Now let's watch the video. What does the customer want to buy?";
					break;
					
					case "Watch again":
						expectedStepInst = "Watch the video again and complete the following tasks. Use the student tools to help you.";
					break;
					
					case "Interactive speaking practice":
						//expectedStepInst = "Take part in the conversation. Click on the speaker you would like to be.";
						//expectedStepInst = "Take part in the conversation. Select the speaker you would like to practice.";
						expectedStepInst = interact1Instruction;
					break;
					
					case "Preparing for branching speaking":
						expectedStepInst = "You are going to take part in a branching conversation. To prepare, complete the following tasks.";
					break;
					
					case "Branching speaking practice":
						expectedStepInst = "Take part in a branching conversation. Choose your response and see how the conversation develops.";
					break;
					
					case "Let's review":
						expectedStepInst = "Let's review some of the main points of this lesson.";
					break;
					
					
				}
			
			break;
			
			case 6: // Grammar 
				
				switch (expStepName) {
			
					case "Prepare":
						stepInstExist = false; 
					break;
					
					case "Explore":
						expectedStepInst = "Listen to the examples of the grammar point. If you need help, click on See Explanation.";
 					break;
					
					case "Practice":
						expectedStepInst = "Review the grammar point and do the tasks that follow. If you need help, use the student tools.";
					break;
					
					case "Interact 1":
						//expectedStepInst = "Take part in the conversation. Choose the speaker you would like to be.";
						//expectedStepInst = "Take part in the conversation. Select the speaker you would like to practice.";
						expectedStepInst = interact1Instruction;
					break;
					
					case "Interact 2":
						expectedStepInst = "Take part in a branching conversation. Choose your response and see how the conversation develops.";
					break;
					
					case "Test":
						expectedStepInst = "Take the quiz to test your understanding.";
					break;
				}
			
			break;
			
			case 10: // Vocabulary 
				
				switch (expStepName) {
			
					case "Prepare":
						stepInstExist = false; 
					break;
					
					case "Explore":
						expectedStepInst = "Read and listen to each item in the vocabulary list. Click each item for more information.";
 					break;
					
					case "Practice":
						expectedStepInst = "Review the vocabulary list and do the tasks that follow. If you need help, use the student tools.";
					break;
					
					case "Interact 1":
						//expectedStepInst = "Take part in the conversation. Choose the speaker you would like to be.";
						//expectedStepInst = "Take part in the conversation. Select the speaker you would like to practice.";
						expectedStepInst = interact1Instruction;
					break;
					
					case "Interact 2":
						expectedStepInst = "Take part in a branching conversation. Choose your response and see how the conversation develops.";
					break;
					
					case "Test":
						expectedStepInst = "Take the quiz to test your understanding.";
					break;
				}
			
			break;
			
			default: testResultService.addFailTest("Skill Id or Step Name not defined / or invalid");
			
		}
		
		if (stepInstExist) testResultService.assertEquals(expectedStepInst, actualStepInst,"Step Instruction not found or does not match to expected for this step");
		
		learningArea2.closeTaskBar();
		sleep(1);
		
	}
	
	
	@Test
	@Category(CourseArea.class)
	@TestCaseParams(testCaseID = { "87012" }, skippedBrowsers = {"firefox"}) 
	public void testVerifyAllKindsOfAnswersStoredAfterNext_Back() throws Exception {
		
		institutionName=institutionsName[0];
		pageHelper.restartBrowserInNewURL(institutionName, true);
		
	report.startStep("Get Course name by CourseId");
		//String courseName = dbService.getCourseNameById(courses[2]).trim();
	
	report.startStep("Create new student and login");
		homePage = createUserAndLoginNewUXClass(configuration.getClassName(), institutionId);
		pageHelper.skipOptin();
		homePage.skipNotificationWindow();
		pageHelper.skipOnBoardingHP();
		homePage.waitHomePageloadedFully();
		
	report.startStep("Navigate to specific course, do some progress, and verify answer stored");
		//learningArea2 = homePage.navigateToTask(courseCodes[2], 1, 2, 2, 1);
														//unit,lesson,step,task
		learningArea2 = homePage.navigateToTask(courseCodes[1], 1, 4, 2, 2); //drag and drop
		//learningArea2.waitUntilLearningAreaLoaded();
		NewUxDragAndDropSection2 dragAndDrop2 = new NewUxDragAndDropSection2(webDriver, testResultService);
		dragAndDrop2.dragAndDropFirstAnswer(TaskTypes.Droppable);
		sleep(1);
		learningArea2.clickOnNextButton();
		sleep(1);
		learningArea2.clickOnBackButton();
		boolean stored = dragAndDrop2.verifyDragAndDropAnswerStored(TaskTypes.Droppable);
		TestCase.assertEquals("Answer doesn't stored",false, stored);
		learningArea2.clickOnHomeButton();
		
	report.startStep("Navigate to specific course, do some progress, and verify answer stored");
		learningArea2 = homePage.navigateToTask(courseCodes[1], 1, 2, 2, 2); // multiple choices checkboxes
		//learningArea2.waitUntilLearningAreaLoaded();
		learningArea2.selectAnswerRadioByTextNum(1, 1);
		stored = learningArea2.verifyAnswerStored("checkBoxes");
		learningArea2.clickOnNextButton();
		sleep(1);
		learningArea2.clickOnBackButton();
		stored = learningArea2.verifyCheckBoxAnswerStored();
		TestCase.assertEquals("Answer doesn't stored",false, stored);
		learningArea2.clickOnHomeButton();
		sleep(1);
		
	report.startStep("Navigate to specific course, do some progress, and verify answer stored");
		learningArea2 = homePage.navigateToTask(courseCodes[1], 8, 5, 2, 1); // drag&drop with picture
		//learningArea2.waitUntilLearningAreaLoaded();
		dragAndDrop2.dragAndDropFirstAnswer(TaskTypes.Droppable);
		learningArea2.clickOnNextButton();
		sleep(1);
		learningArea2.clickOnBackButton();
		sleep(1);
		stored = dragAndDrop2.verifyDragAndDropAnswerStored(TaskTypes.Droppable);
		TestCase.assertEquals("Answer doesn't stored",false, stored);
		learningArea2.clickOnHomeButton();
		sleep(1);
	
	report.startStep("Navigate to specific course, do some progress, and verify answer stored");
		learningArea2 = homePage.navigateToTask(courseCodes[1], 1, 7, 2, 3); // drag&drop with picture	
		learningArea2.waitUntilLearningAreaLoaded();
		String text = "some text";
		learningArea2.typeIntoTextArea(text);
		stored = learningArea2.verifyValueStoredAtTextArea(text);
		learningArea2.clickOnNextButton();
		sleep(1);
		learningArea2.clickOnBackButton();
		sleep(1);
		stored = learningArea2.verifyValueStoredAtTextArea(text);
		TestCase.assertEquals("Answer doesn't stored",false, stored);
		learningArea2.clickOnHomeButton();
		sleep(1);
	
	report.startStep("Log out of ED");	
		homePage.clickOnLogOut();
	}
	

	@Test
	@Category(SanityTests.class)
	@TestCaseParams(testCaseID = { "87012" }, skippedBrowsers = {"firefox"}) 
	public void testKeepStudentAnswers() throws Exception {
		
		institutionName=institutionsName[0];
		pageHelper.restartBrowserInNewURL(institutionName, true);
		
	report.startStep("Get Course name by CourseId");
		String courseName = dbService.getCourseNameById(courses[2]).trim();
	
	report.startStep("Create new student and login");
		homePage = createUserAndLoginNewUXClass(configuration.getClassName(), institutionId);
		//sleep(3);
		pageHelper.skipOptin();
		homePage.skipNotificationWindow();
		pageHelper.skipOnBoardingHP();
		homePage.waitHomePageloadedFully();
		
	report.startStep("Navigate to Course and check course area titles");
		learningArea2 = homePage.navigateToTask(courseCodes[2], 1, 2, 2, 1);
		//learningArea2.waitUntilLearningAreaLoaded();
		String courseId = getCourseIdByCourseCode(courseCodes[2]);
		List<String[]> units = dbService.getCourseUnitDetils(courseId);
		List<String[]> components = dbService.getComponentDetailsByUnitId(units.get(0)[0]); //we need only one first unit at this test 
	
	report.startStep("Do some progress");
		learningArea2.SelectRadioBtn("question-1_answer-1");
		sleep(1);
		learningArea2.clickOnNextButton();
		sleep(1);
		learningArea2.SelectRadioBtn("question-1_answer-1");	
		
		
	report.startStep("Move forward trough tasks, and return back");	
		boolean stored = false;
		learningArea2.clickOnNextButton();
		sleep(1);
		learningArea2.clickOnBackButton();
		sleep(1);
		stored = learningArea2.verifyAnswerStored("radioButton");
		TestCase.assertEquals("Answer doesn't stored",true, stored);
		learningArea2.clickOnBackButton();
		sleep(1);
		stored = learningArea2.verifyAnswerStored("radioButton");
		TestCase.assertEquals("Answer doesn't stored",true, stored);
	
	report.startStep("Move between tasks by task bar, and return back");	
		learningArea2.clickOnTaskByNumber(4);
		learningArea2.clickOnTaskByNumber(1);
		sleep(1);
		stored = learningArea2.verifyAnswerStored("radioButton");
		TestCase.assertEquals("Answer doesn't stored",true, stored);
		learningArea2.clickOnTaskByNumber(2);
		sleep(1);
		stored = learningArea2.verifyAnswerStored("radioButton");
		TestCase.assertEquals("Answer doesn't stored",true, stored);
		
	report.startStep("Init varibles for component test");
		int unitIndex = 1;
		int lessonIndex = 1;
		int stepIndex = 1;
		int taskIndex = 1;
		boolean isTest = false;
		boolean isInteract = false;

	report.startStep("Go over component test of nex lesson: " + components.get(1)[1]);
						
		report.startStep("Get Component SubComponents(Steps) by componentId");
				List<String[]> subComp = dbService.getSubComponentsDetailsByComponentId(components.get(1)[1]); //1 it's second lesson of the uni			
			//	boolean stepHasIntro = false;
			//	String stepName = "undefined";						
		String stepName = subComp.get(2)[0];		
		if (stepName.contains("Practice 1") || stepName.contains("Practice 2") || stepName.contains("Interact 1") || stepName.contains("Interact 2") ) {
				isInteract = true;
			}
		if (stepName.contains("Test") || stepName.contains("Let's review")) {
				isTest = true;
			}
		stepIndex = 3; // h + 1;
											
	report.startStep("Click on Step: " + stepIndex);
												
		if (isTest) {
			learningArea2.clickOnStep(stepIndex, false);	
			learningArea2.clickOnStartTest();
		} else 
			learningArea2.clickOnStep(stepIndex);	
				sleep(1);
												
					//	if (isInteract) learningArea2.closeAlertModalByAccept();
						
					
					//} else if (stepHasIntro) learningArea2.clickOnNextButton();
				
	report.startStep("Get SubComponets Items (Tasks) by subComponentId");
										
		List<String[]> expectdTasks = dbService.getSubComponentItems(subComp.get(2)[1]); // 2 it's component test
					
	report.startStep("Go over every Task in component test");
					
		taskIndex = 1;	
		int itemTypeId = Integer.parseInt(expectdTasks.get(0)[2]); 
		String itemCode = expectdTasks.get(0)[1];
		
		for (int k = 0; k < expectdTasks.size(); k++) {
						
			itemTypeId = Integer.parseInt(expectdTasks.get(k)[2]);
			itemCode = expectdTasks.get(k)[1];
						
			if (k > 0) {
				//learningArea2.clickOnTaskByNumber(taskIndex);
				learningArea2.clickOnNextButton();
			}
				
		report.startStep("Making progress in Task "+taskIndex+" / Checking status of Task " + taskIndex);
			try {
				learningArea2.makeProgressActionByItemType(itemTypeId, itemCode);
				} catch (Exception e) {
					testResultService.addFailTest("Cannot make progress in "+courseName+" : "+"Unit "+unitIndex+" - "+"Lesson "+ lessonIndex +" - "+"Step "+stepIndex+" - "+"Task "+ taskIndex, false, true);
					e.printStackTrace();
				}
			taskIndex++;
		}
		report.startStep("Submit test");		
			if (isTest) {
				learningArea2.submitTest(false);
				}
			
		report.startStep("Return to step 2 of current lesson, to verify answers are still stored");
			learningArea2.clickOnStep(2,false);
			learningArea2.clickOnNextButton();
			sleep(1);
			stored = learningArea2.verifyAnswerStored("radioButton");
			TestCase.assertEquals("Answer doesn't stored",true, stored);
			learningArea2.clickOnNextButton();
			sleep(1);
			stored = learningArea2.verifyAnswerStored("radioButton");
			TestCase.assertEquals("Answer doesn't stored",true, stored);
			
		report.startStep("Change lesson, and return to previous one, to verify are answers erased");	
			learningArea2.openLessonsList();
			learningArea2.clickOnLessonByNumber(3);
			learningArea2.openLessonsList();
			learningArea2.clickOnLessonByNumber(2);
			learningArea2.clickOnStep(2,false);
			learningArea2.clickOnNextButton();
			stored = learningArea2.verifyAnswerStored("radioButton");
			TestCase.assertEquals("Answer doesn't stored",false, stored);
			learningArea2.clickOnNextButton();
			stored = learningArea2.verifyAnswerStored("radioButton");
			TestCase.assertEquals("Answer doesn't stored",false, stored);
			
		report.startStep("Logout of ED");
			homePage.clickOnLogOut();
			
		
	}
	
	
	
	
	@After
	public void tearDown() throws Exception {
			super.tearDown();
	}

}
