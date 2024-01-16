package tests.edo.newux;

import java.util.ArrayList;
import java.util.List;

import org.apache.bcel.generic.Instruction;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.WebElement;

import pageObjects.edo.NewUxLearningArea;
import testCategories.NonAngularLearningArea;
import testCategories.inProgressTests;
import testCategories.reg2;
import testCategories.edoNewUX.CourseArea;
import Enums.ByTypes;
import Interfaces.TestCaseParams;

@Category(NonAngularLearningArea.class)
public class LearningAreaStepsTests extends BasicNewUxTest {

	NewUxLearningArea learningArea;
		
	@Test
	@Category(CourseArea.class)
	@TestCaseParams(testCaseID = { "21303" }, skippedBrowsers = {"firefox"}) // Actions API not supported by geckodriver)
	public void testStepsSubComponentsDataVerification() throws Exception {

		String[] expectedSteps = new String[] { "Prepare", "Explore", "Practice", "Interact 1", "Interact 2" };

		int[] testSet1 = new int[] { 1, 4, 1 }; // course index: B1, unit number: 4, lesson number: 1 (Business) - 3 steps
				
		int[] testSet2 = new int[] { 1, 2, 2 }; // course index: B1, unit number: 2, lesson number: 2 (Follow That Man) - 3 steps
		
		int[] testSet3 = new int[] { 2, 2, 3 }; // course index: B2, unit number: 2, lesson number: 3 (Piece of Cake) - 3 steps

		int[] testSet4 = new int[] { 3, 3, 4 }; // course index: B3, unit number: 3, lesson number: 4 (Modals) - 3 steps
		
		int[] testSet5 = new int[] { 3, 1, 7 }; // course index: B3, unit number: 1, lesson number: 7 (Education) - 3 steps
		
		List<int[]> testDataList = new ArrayList<int[]>();
		testDataList.add(testSet1);
		testDataList.add(testSet2);
		testDataList.add(testSet3);
		testDataList.add(testSet4);
		testDataList.add(testSet5);

		report.startStep("Login to ED and navigate to a selected course");
		homePage = createUserAndLoginNewUXClass();

		for (int j = 0; j < testDataList.size(); j++) {

			homePage.navigateToRequiredCourseOnHomePage(coursesNames[testDataList.get(j)[0]]);
			
			// units
			List<String[]> unitsList = dbService.getCourseUnitDetils(courses[testDataList.get(j)[0]]);
			int unitIndex = testDataList.get(j)[1];
			// lessons
			List<String[]> componentsList = dbService.getComponentDetailsByUnitId(unitsList.get(unitIndex - 1)[0]);
			int skill_ID = Integer.parseInt(componentsList.get(testDataList.get(j)[2]-1)[2]);
			// steps
			List<String[]> subComponetnsList = dbService.getSubComponentsDetailsByComponentId(componentsList.get(testDataList.get(j)[2] - 1)[1]);

			report.startStep("Click on lesson");
			homePage.clickOnUnitLessons(testDataList.get(j)[1]);
			sleep(1);
			homePage.clickOnLesson(testDataList.get(j)[1] - 1, testDataList.get(j)[2]);
			report.startStep("Verify lesson name and number");
			NewUxLearningArea learningArea = new NewUxLearningArea(webDriver, testResultService);
			

			report.finishStep();

			report.startStep("Verify steps of Test Set : " + (j+1));
			for (int i = 0; i < subComponetnsList.size(); i++) {
				if (i > 0 && i < subComponetnsList.size()) {
					// clickOnStep(i + 1);

					learningArea.clickOnStep(i + 1);
				}
				webDriver.closeAlertByAccept(2);
				verifyStep(subComponetnsList.get(i)[0], skill_ID, i);

			}
			report.startStep("Navigate back to home page");
			//homePage.clickToOpenNavigationBar();
			homePage.clickOnHomeButton();
		}
		
	}
		
	private void verifyStep(String stepName, int skill_ID,  int stepIndex) throws Exception {
		int index = stepIndex + 1;
		WebElement webElement = webDriver.waitForElement(
				"//div[@class='learning__tasks']//div[" + index
						+ "]//div//div[2]//div", ByTypes.xpath);
	
		if (stepName.equals("Practice 1") || stepName.equals("Practice 2") ) stepName = stepName.replace("Practice", "Interact");
			
		testResultService.assertEquals(". " + stepName, webElement.getText());

		WebElement webElementStepNum = webDriver.waitForElement(
				"//div[@class='learning__tasks']//div[" + index
						+ "]//div//div[1]", ByTypes.xpath);
		testResultService.assertEquals(String.valueOf(index),
				webElementStepNum.getText());

		
		WebElement webElementStepInst = webDriver.waitForElement("learning__stepInstructions", ByTypes.className, 5, false, "Step Instruction not found");
		
		String actualStepInst = "default";
		if (webElementStepInst != null) actualStepInst = webElementStepInst.getText();
		
		String expectedStepInst = "Default";
		
		boolean stepInstExist = true;
		
		switch (skill_ID) {
			
			case 3: // Listening 
			
				switch (stepName) {
			
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
						expectedStepInst = "Take part in the conversation. Select the speaker you would like to practice.";
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
				
				switch (stepName) {
			
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
						expectedStepInst = "Take part in the conversation. Select the speaker you would like to practice.";
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
				
				switch (stepName) {
			
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
						expectedStepInst = "Take part in the conversation. Select the speaker you would like to practice.";
					break;
					
					case "Interact 2":
						expectedStepInst = "Take part in a branching conversation. Choose your response and see how the conversation develops.";
					break;
					
					case "Test":
						expectedStepInst = "Take the quiz to test your understanding.";
					break;
				}
			
			break;
			
			case 6: // Grammar 
				
				switch (stepName) {
			
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
						expectedStepInst = "Take part in the conversation. Select the speaker you would like to practice.";
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
				
				switch (stepName) {
			
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
						expectedStepInst = "Take part in the conversation. Select the speaker you would like to practice.";
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
		
		
		
	}

}
