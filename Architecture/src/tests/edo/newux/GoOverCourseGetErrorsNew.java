package tests.edo.newux;

import java.util.ArrayList;
import java.util.List;

import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.proxy.selenium.SeleniumProxyHandler;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;

//import com.thoughtworks.selenium.Selenium;







import org.openqa.selenium.WebElement;

import Interfaces.TestCaseParams;
import pageObjects.edo.NewUXLoginPage;
import pageObjects.edo.NewUxLearningArea;
import testCategories.inProgressTests;
import testCategories.unstableTests;

public class GoOverCourseGetErrorsNew extends BasicNewUxTest {

	Har har;
	List<String[]> errorList;
	NewUXLoginPage loginPage;
	String className;
	String institutionId;
	String courseId;
	List<String[]> units;
	String unitName;
	List<String[]> components;
	int totalLessonsCompleted = 0;
	String [] expectedLessonName;
	NewUxLearningArea learningArea;
	
	@Before
	public void setup() throws Exception {
//		System.setProperty("useProxy", "true");
		//enableProxy();
		//enableConsoleLoggin();
		
		super.setup();
				
	}

	@Test
	@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "" }, ignoreTestTimeout = true, institutionId = "5232402", allowMedia = true)
	public void testCourseFD() throws Exception {
		
		
		className = configuration.getAutomationParam("denis_className", "classname");
		institutionId = configuration.getProperty("denis_institutionId");
		
		loginToEnvAT();
		
		
		/*studentId = pageHelper.createUSerUsingSP(institutionId, className);
		NewUXLoginPage loginPage = new NewUXLoginPage(webDriver,testResultService);
		homePage = loginPage.loginAsStudent(dbService.getUserNameById(studentId, institutionId), "12345");*/
		
		//String classId = dbService.getClassIdByName(className, institutionId);
		
		String [] userCourses = pageHelper.getUserAssignedCourses(studentId);
		
				
		for (int i = 0; i < userCourses.length; i++) {
			String courseName = dbService.getCourseNameById(userCourses[i]);
			courseName = courseName.toString().replace(" ", "").trim();
			String actualCourse = homePage.getCurrentCourseName();
			
			report.startStep("Navigating to Course: "+ courseName);
			int count = 1;
			while (count < 10 && !(actualCourse.toString().replace(" ", "").trim().equals(courseName))) {
				homePage.carouselNavigateNext();
				sleep(1);
				actualCourse = homePage.getCurrentCourseName();
				count++;
			} 
			
			try {
				checkCourseForNetworkErrors(courseName, userCourses[i]);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			report.startStep("Save report for Course: " + courseName);
			textService.writeArrayListToCSVFile("smb://" + configuration.getLogerver() + "/UXNetErrorsReports/netLog" + "_" + courseName.trim() +".csv", errorList, false);
			errorList.clear();
		}
		
		sleep(1);
	}
	
	@Test
	@Category(unstableTests.class)
	@TestCaseParams(testCaseID = { "" }, ignoreTestTimeout = true, allowMedia = true, institutionId="5233456")
	public void testCoursesNewContent() throws Exception {
		
		
		className = configuration.getAutomationParam("denis_className", "classname");
		institutionId = configuration.getProperty("denis_institutionId");
		//institutionId = autoInstitution.getInstitutionId();
		
		//loginToEnvAT();
				
		studentId = pageHelper.createUSerUsingSP(institutionId, className);
		NewUXLoginPage loginPage = new NewUXLoginPage(webDriver,testResultService);
		homePage = loginPage.loginAsStudent(dbService.getUserNameById(studentId, institutionId), "12345");
		
		//String classId = dbService.getClassIdByName(className, institutionId);
		
		String [] userCourses = pageHelper.getUserAssignedCourses(studentId);
		
				
		for (int i = 0; i < userCourses.length; i++) {
			
			courseId = userCourses[i];
			String courseName = dbService.getCourseNameById(courseId);
			courseName = courseName.toString().replace(" ", "").trim();
			String actualCourse = homePage.getCurrentCourseName();
			
			report.startStep("Navigating to Course: "+ courseName);
			int count = 1;
			while (count < 20 && !(actualCourse.toString().replace(" ", "").trim().equals(courseName))) {
				homePage.carouselNavigateNext();
				sleep(1);
				actualCourse = homePage.getCurrentCourseName();
				count++;
			} 
					
			
			checkCourseForErrors(courseName, courseId, true, true, true, true);
			
			report.startStep("Save report for Course: " + courseName);
			textService.writeArrayListToCSVFile("smb://" + configuration.getLogerver() + "/UXNetErrorsReports/netLog" + "_" + courseName.trim() +".csv", errorList, false);
			errorList.clear();
		}
		
		sleep(1);
	}

	private void checkCourseForNetworkErrors(String courseName, String courseId)
			throws Exception {
		
		report.startStep("Get Course Name by ID");
						
		NewUxLearningArea learningArea = new NewUxLearningArea(webDriver, testResultService);
		
		errorList = new ArrayList<>(); 
		
		List<String[]> units = dbService.getCourseUnitDetils(courseId);

		for (int i = 0; i < units.size(); i++) { // PRODUCTION
		//for (int i = 5; i < units.size(); i++) {
		//for (int i = 9; i < 10; i++) {
			
			report.startStep("Go over unit: " + units.get(i)[1]);
			
			List<String[]> components = dbService.getComponentDetailsByUnitId(units.get(i)[0]);

			int unitIndex = i + 1;
			int lessonIndex = 1;
			int stepIndex = 1;
			int taskIndex = 1;
			
			sleep(1);
			homePage.clickOnUnitLessons(unitIndex);
			//newUxHomePage.clickOnLesson(i, 1); // PRODUCTION
			homePage.clickOnLesson(i, 3);
			
			report.startStep("Enable network watcher - init new har object");
			errorList = pageHelper.checkTaskForNetErrorsAddToList(errorList, 5, courseName, unitIndex, lessonIndex, stepIndex, taskIndex);
								
			//for (int j = 0; j < components.size(); j++) { // PRODUCTION
			//for (int j = 2; j < 2; j++) {
			for (int j = 2; j < components.size(); j++) {	
			
				report.startStep("Go over lesson: " + components.get(j)[0]);
						
				report.startStep("Go over every step");
				List<String[]> subComp = dbService.getSubComponentsDetailsByComponentId(components.get(j)[1]);

				boolean isTest = false;
				boolean isInteract = false;
				for (int h = 0; h < subComp.size(); h++) {
					if (h > 0) {
						stepIndex = h + 1;
						report.startStep("Click on Step: " + h);
//						sleep(4);
						
						taskIndex = 1;					
						learningArea.clickOnStep(stepIndex);
						report.startStep("Enable network watcher - init new har object");
						errorList = pageHelper.checkTaskForNetErrorsAddToList(errorList, 5, courseName, unitIndex, lessonIndex, stepIndex, taskIndex);

					}

					// if step is Interact
					if (subComp.get(h)[0].contains("Practice 1") || subComp.get(h)[0].contains("Practice 2") || subComp.get(h)[0].contains("Interact 1") || subComp.get(h)[0].contains("Interact 2") ) {
						learningArea.closeAlertModalByAccept();
						isInteract = true;
					}
					
					
					// if step is test
					if (subComp.get(h)[0].contains("Test")) {
						learningArea.clickOnStartTest();
						report.startStep("Enable network watcher - init new har object");
						errorList = pageHelper.checkTaskForNetErrorsAddToList(errorList, 2, courseName, unitIndex, lessonIndex, stepIndex, taskIndex);
						isTest = true;
					}

					report.startStep("Go over every Task");
					List<String[]> expectdTasks = dbService.getSubComponentItems(subComp.get(h)[1]);
					taskIndex = 1;
					int taskPage = 0;
					report.report("Number of tasks is :"+expectdTasks.size());
					for (int k = 0; k < expectdTasks.size(); k++) {
										
											
						if (k > 0) {
							if (taskIndex % 7  == 0) {
								learningArea.clickOnTasksNext();
								taskPage++;
							}
							sleep(2);
							if (expectdTasks.size() > 7) {
								learningArea.clickOnTask(taskIndex, taskPage);
								report.startStep("Enable network watcher - init new har object");
								errorList = pageHelper.checkTaskForNetErrorsAddToList(errorList, 1, courseName, unitIndex, lessonIndex, stepIndex, taskIndex+1);
							} else {
								learningArea.clickOnTask(taskIndex);
								report.startStep("Enable network watcher - init new har object");
								errorList = pageHelper.checkTaskForNetErrorsAddToList(errorList, 1, courseName, unitIndex, lessonIndex, stepIndex, taskIndex+1);
							}
						webDriver.waitForJqueryToFinish();
						taskIndex++;
						}
					}
						

				}
				
				report.finishStep();
				
				lessonIndex = j + 2;
				
				
				if (lessonIndex > components.size()){ 
					report.startStep("Go back to Home Page");
					learningArea.clickToOpenNavigationBar();
					learningArea.clickOnHomeButton();
					sleep(1);
					if (isTest) learningArea.approveTest();
					
				} else {
					report.startStep("Navigate to next lesson by lesson list");
					learningArea.openLessonsList();
					sleep(1);
					learningArea.clickOnLessoneByIndex(lessonIndex);
					if (isTest) learningArea.approveTest();
					report.startStep("Enable network watcher - init new har object");
					errorList = pageHelper.checkTaskForNetErrorsAddToList(errorList, 5, courseName, unitIndex, lessonIndex, 1, 1);
						 
				}

			}

		}

	}
	
	private void checkCourseForErrors(String courseName, String courseId, boolean checkConsoleErrors, boolean checkProgress, boolean checkUnitLessonNames, boolean checkStepNames)
			throws Exception {
		
		report.startStep("Get Course Units by courseId");
		learningArea = new NewUxLearningArea(webDriver, testResultService);
		errorList = new ArrayList<>(); 
		
		units = dbService.getCourseUnitDetils(courseId);

		if (checkUnitLessonNames) {
		report.startStep("Check Units scope & sequence on home page");
		validateUnitsOnHomePage();	
		}
		
		for (int i = 0; i < units.size(); i++) { // PRODUCTION
		//for (int i = 2; i < units.size(); i++) {
		//for (int i = 5; i < 6; i++) {
			
			unitName = units.get(i)[1].trim().replace("  ", " ");
			
			report.startStep("Get Unit Components by unitId");
			
			components = dbService.getComponentDetailsByUnitId(units.get(i)[0]);

			report.startStep("Go over unit: " + units.get(i)[1]);
			
			int unitIndex = i + 1;
			int lessonIndex = 1;
			int stepIndex = 1;
			int taskIndex = 1;
			
			sleep(1);
			homePage.clickOnUnitLessons(unitIndex);
			sleep(2);
			expectedLessonName = new String [components.size()];		
						
			if (checkUnitLessonNames) {
			report.startStep("Check Lessons scope & sequence on home page in Unit: " + unitIndex);
			//validateUnitLessonsOnHomePage(i); // does not work with more then 14 lessons
			}
						
			homePage.clickOnLesson(i, lessonIndex); // PRODUCTION
			//homePage.clickOnLesson(i, 14);
						
			if (checkConsoleErrors) {
			report.startStep("Enable console watcher");
			errorList = pageHelper.checkTaskForConsoleErrorsAddToList(errorList, courseName, unitIndex, lessonIndex, stepIndex, taskIndex);
			}
			
			
			for (int j = 0; j < components.size(); j++) { // PRODUCTION
			//for (int j = 0; j < 1; j++) {
			//for (int j = 13; j < components.size(); j++) {	
				
				
				report.startStep("Go over lesson: " + components.get(j)[0]);
						
				report.startStep("Get Component SubComponents(Steps) by componentId");
				List<String[]> subComp = dbService.getSubComponentsDetailsByComponentId(components.get(j)[1]);

				boolean isTest = false;
				boolean isInteract = false;
				stepIndex = 1;
				taskIndex = 1;
				
				if (checkUnitLessonNames) {
				report.startStep("Check Unit and lesson name in Lesson: " + lessonIndex);
				validateUnitLessonsInLearningArea(unitIndex, j);
				}
				
				if (checkStepNames) {
				report.startStep("Check Step Name in Step: " + stepIndex);
				validateStepNameAndNumber(subComp.get(stepIndex-1)[0], stepIndex);
				}
								
				
				if (checkConsoleErrors) {
				report.startStep("Enable console watcher");
				errorList = pageHelper.checkTaskForConsoleErrorsAddToList(errorList, courseName, unitIndex, lessonIndex, stepIndex, taskIndex);
				}				
				
				for (int h = 0; h < subComp.size(); h++) {
					if (h > 0) {
						stepIndex = h + 1;
						taskIndex = 1;
						
						report.startStep("Click on Step: " + stepIndex);
						sleep(2);
						
						learningArea.clickOnStep(stepIndex);
						
						if (checkStepNames) {
						report.startStep("Check Step Name in Step: " + stepIndex);
						validateStepNameAndNumber(subComp.get(stepIndex-1)[0], stepIndex);
						}
						
						if (checkConsoleErrors) {
						report.startStep("Enable console watcher");
						errorList = pageHelper.checkTaskForConsoleErrorsAddToList(errorList, courseName, unitIndex, lessonIndex, stepIndex, taskIndex);
						}
					}

					// if step is Interact
					if (subComp.get(h)[0].contains("Practice 1") || subComp.get(h)[0].contains("Practice 2") || subComp.get(h)[0].contains("Interact 1") || subComp.get(h)[0].contains("Interact 2") || subComp.get(h)[0].contains("speaking practice") ) {
						learningArea.closeAlertModalByAccept();
						isInteract = true;
					}
										
					// if step is test
					if (subComp.get(h)[0].contains("Test") || subComp.get(h)[0].contains("Let's review") ) {
						learningArea.clickOnStartTest();
						
						if (checkConsoleErrors) {
						report.startStep("Enable console watcher");
						errorList = pageHelper.checkTaskForConsoleErrorsAddToList(errorList, courseName, unitIndex, lessonIndex, stepIndex, taskIndex);
						}
						
						isTest = true;
					}

					report.startStep("Get SubComponets Items (Tasks) by subComponentId");
										
					List<String[]> expectdTasks = dbService.getSubComponentItems(subComp.get(h)[1]);
					
					report.startStep("Go over every Task / Number of Tasks is :"+expectdTasks.size());
					
					taskIndex = 1;
					int taskPage = 0;
					
					int itemTypeId = Integer.parseInt(expectdTasks.get(0)[2]); 
					String itemCode = expectdTasks.get(0)[1];
					String itemId = expectdTasks.get(0)[0];
					String actItemId = "undefined";
					
					actItemId = learningArea.clickOnTask(0);
					
					report.startStep("Check ItemId matches DB");
					if (!itemId.equals(actItemId)) {					
					testResultService.addFailTest("ItemID does not match DB: "+courseName+" : "+"Unit "+unitIndex+" - "+"Lesson "+ lessonIndex +" - "+"Step "+stepIndex+" - "+"Task "+ taskIndex, false, false);
					}
					
					if (checkProgress) {
						report.startStep("Make progress in Task 1 and / Checking status of Task 1");
						try {
							learningArea.makeProgressActionByItemType(itemTypeId, itemCode);
						} catch (Exception e) {
							testResultService.addFailTest("Cannot make progress in this task", false, true);
							e.printStackTrace();
						}
						testResultService.assertEquals(learningArea.checkThatTaskIsVisitedAndCurrent(0), true, "Task not visited or not current");
					}								
					
					for (int k = 0; k < expectdTasks.size(); k++) {
																
						itemTypeId = Integer.parseInt(expectdTasks.get(k)[2]);
						itemCode = expectdTasks.get(k)[1];
						itemId = expectdTasks.get(k)[0];
						
						if (k > 0) {
							if (taskIndex % 7  == 0) {
								sleep(1);
								learningArea.clickOnTasksNext();
								taskPage++;
							}
							sleep(1);
													
							actItemId = learningArea.clickOnTask(taskIndex, taskPage);
							
							report.startStep("Check ItemId matches DB");
							if (!itemId.equals(actItemId)) {					
								testResultService.addFailTest("ItemID does not match DB: "+courseName+" : "+"Unit "+unitIndex+" - "+"Lesson "+ lessonIndex +" - "+"Step "+stepIndex+" - "+"Task "+ (taskIndex+1), false, false);
								}
							if (checkConsoleErrors) {
							report.startStep("Enable console watcher");
							errorList = pageHelper.checkTaskForConsoleErrorsAddToList(errorList, courseName, unitIndex, lessonIndex, stepIndex, taskIndex+1);
							}
							
							if (checkProgress) {
								report.startStep("Making progress in Task "+(taskIndex+1)+" / Checking status of Task " + (taskIndex+1));
								try {
									learningArea.makeProgressActionByItemType(itemTypeId, itemCode);
								} catch (Exception e) {
									testResultService.addFailTest("Cannot make progress in this task", false, false);
									e.printStackTrace();
								}
								testResultService.assertEquals(learningArea.checkThatTaskIsVisitedAndCurrent(taskIndex), true, "Task not visited or not current");
							}
							
							
						taskIndex++;
						}
					}
						

				}
				
								
				lessonIndex = j + 2;
								
				if (lessonIndex > components.size()){ 
																
					
					if (isTest && checkProgress) learningArea.submitTest(false);
					
					if (checkProgress) {
						report.startStep("Open Lesson List and check all lessons completed");
						learningArea.openLessonsList();
						learningArea.checkLessonsCompletedInLessonList(1, components.size());
						learningArea.closeLessonsList();
						sleep(1);
					}
					
					report.startStep("Go back to Home Page");
					learningArea.clickToOpenNavigationBar();
					learningArea.clickOnHomeButton();
					if (!checkProgress && isTest) learningArea.approveTest();
					sleep(1);
							
					if (checkProgress) {
					
						report.startStep("Open unit: "+unitIndex+" and check all lessons completed");
						homePage.clickOnUnitLessons(unitIndex);
						learningArea.checkLessonsCompletedInLessonList(1, components.size());
						homePage.clickToOpenNavigationBar();
										
						report.startStep("Check Unit "+unitIndex+" Progress Bar");
						double unitProgress = homePage.getUnitProgressBarValue(unitIndex-1);
						double actualUnitProgress = Math.round(unitProgress); 
						//double expectedUnitProgress = Math.round(100.0 / components.size() * (components.size()-2));
						double expectedUnitProgress = 100.0;
						testResultService.assertEquals(expectedUnitProgress, actualUnitProgress,"Progress in Unit Progress Bar is not correct");
					
						report.startStep("Check All Units Progress Bar");
						unitProgress = homePage.getAllUnitsBarUnitProgress(unitIndex);
						actualUnitProgress = Math.round(unitProgress); // roundExpectedProgress(unitProgress, 0).intValue();
						expectedUnitProgress = Math.round(expectedUnitProgress / units.size()); 
						testResultService.assertEquals(expectedUnitProgress, actualUnitProgress, "Progress of Unit in All Units Bar is not correct");
				
						report.startStep("Check Course Completion widget");
						String actCompletion = homePage.getCompletionWidgetValue();
						double totalLessonsInCourse = getNumberOfLessonsInCourse(units);
						totalLessonsCompleted += components.size();
						int expCourseProgress = (int) Math.round(totalLessonsCompleted / totalLessonsInCourse * 100);
						String expectedCourseProgress = String.valueOf(expCourseProgress);
						testResultService.assertEquals(expectedCourseProgress, actCompletion, "Progress of Course in Widget is not correct");
					
					sleep(1);
					}
					
					
				} else {
					
					report.startStep("Navigate to next lesson by lesson list");
					
					if (isTest && checkProgress) {
						learningArea.submitTest(false);
						learningArea.openLessonsList();
						sleep(1);
						learningArea.clickOnLessoneByIndex(lessonIndex);
					} else if (isTest && !checkProgress)  {
						learningArea.openLessonsList();
						sleep(1);
						learningArea.clickOnLessoneByIndex(lessonIndex);
						learningArea.approveTest();
					} else { 
						learningArea.openLessonsList();
						sleep(1);
						learningArea.clickOnLessoneByIndex(lessonIndex);
												
					}
				}

			}

		}

	}

	private void validateUnitsOnHomePage() throws Exception { 
		
		List<WebElement> unitsElements = homePage.getUnitsElements();
		testResultService.assertEquals(unitsElements.size(), units.size(), "Number of units on page and in DB are not the same");
		String unitName;
		
		for (int j = 0; j < unitsElements.size(); j++) {
			int index = j + 1;
			String unitId = homePage.getUnitIdTextByIndex(index);
			testResultService.assertEquals(String.valueOf(index), unitId,"Unit number is not displayed");
			unitName = homePage.getUnitNameByIndex(index);
			testResultService.assertEquals(units.get(j)[1].trim().replace("  ", " "), unitName.trim(), "Unit name in course: " + courseId + ", unit: " + index + " does not match to DB value");
		}		
		
	}
	
	private void validateUnitLessonsOnHomePage(int unitIndex) throws Exception { 
			
		for (int f = 0; f < components.size(); f++) {
	
		expectedLessonName [f] = dbService.getLessonNameBySkill(components.get(f));
		
		String actualName = homePage.getLessonText(unitIndex, f, courseId);
			if (actualName != null) {
			actualName = actualName.trim();
			}
			
			if (expectedLessonName [f].startsWith("Story")) { // exception for new units - remove Story: subject from lesson names
				expectedLessonName [f] = expectedLessonName [f].replace("Story:", "").trim();
			} else expectedLessonName [f] = expectedLessonName [f].trim();
			
			if (!testResultService.assertEquals(expectedLessonName[f].trim(), actualName,"Lesson name in course: " + courseId + " , Unit: " + (unitIndex+1) + ", Lesson: " + f + "does not match DB")) {
			webDriver.printScreen();
			}

		}
	}
				
	private void validateUnitLessonsInLearningArea(int unitNumber, int lessonIndex) throws Exception { 
	
		String currentUnitName = learningArea.getHeaderTitle();
		if (!testResultService.assertEquals("Unit " + unitNumber + ": " + unitName, currentUnitName, "Unit name not found/does not match")) {
		webDriver.printScreen("Unit name in learinf area");
		}
			
		
		for (int f = 0; f < components.size(); f++) {
			
			expectedLessonName [f] = dbService.getLessonNameBySkill(components.get(f));
			
		}
				
		String currentLessonName = learningArea.getLessonName();

		if (expectedLessonName [lessonIndex].startsWith("Story")) { // exception for new units - remove Story: subject from lesson names
			expectedLessonName [lessonIndex] = expectedLessonName [lessonIndex].replace("Story:", "").trim();
		} else expectedLessonName [lessonIndex] = expectedLessonName [lessonIndex].trim();
		
		if (!testResultService.assertEquals(expectedLessonName[lessonIndex].trim(), currentLessonName, "Lesson name does not match")) {
		webDriver.printScreen();
		}
		
	}
	
	private void validateStepNameAndNumber(String expStepName, int stepNumber) throws Exception { 
			
		
		if (expStepName.equals("Practice 1") || expStepName.equals("Practice 2") ){
			expStepName = expStepName.replace("Practice", "Interact");
		}
		
		String actualStepName = learningArea.getStepName(stepNumber);
		String actualStepNumber = learningArea.getStepNumber(stepNumber);
		actualStepName = actualStepName.replace(". ", "");
		testResultService.assertEquals(expStepName.trim(), actualStepName.trim(), "Step Name does not match");
		testResultService.assertEquals(String.valueOf(stepNumber), actualStepNumber, "Step Number does not match");
	}
	 	
	private void loginToEnvAT() throws Exception { 
		
		report.startStep("Login to ED as student - AT env");
		sleep(2);
		webDriver.openUrl(configuration.getSutUrl());
		sleep(2);
		studentId = pageHelper.createUSerUsingSP(institutionId, className);
		
		loginPage = new NewUXLoginPage(webDriver, testResultService);
		homePage = loginPage.loginAsStudent(dbService.getUserNameById(studentId, institutionId), "12345");
		report.startStep("Skip Walkthrough tour");        
		for (int n = 0; n < 12; n++) {
                sleep(1);
                webDriver.sendKey(Keys.ARROW_RIGHT);                
          }
		}
	
	private double getNumberOfLessonsInCourse(List<String[]> courseUnits) throws Exception {

		int totalLessons = 0;
		
		for (int j = 0; j < courseUnits.size(); j++) {
			totalLessons = totalLessons + dbService.getComponentDetailsByUnitId(courseUnits.get(j)[0]).size();
		}
		return totalLessons;

	}
		
	@After
	public void tearDown() throws Exception {
		
		super.tearDown();
				
		/*report.startStep("List All 4** / 5** Network Errors");
		netService.checkForNetworkErrors(webDriver.getHar());
		webDriver.checkConsoleLogsForErrors();*/
		
	}
	
	
}
