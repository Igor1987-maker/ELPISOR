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
import pageObjects.edo.NewUxLearningArea2;
import testCategories.inProgressTests;
import testCategories.unstableTests;

public class GoOverCourseGetErrorsNew2 extends BasicNewUxTest {

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
	NewUxLearningArea2 learningArea2;
	
	private static final String REPORTS_FOLDER = "courseErrorsReports";
	
	@Before
	public void setup() throws Exception {

		super.setup();
				
	}
	
	@Test
	@Category(unstableTests.class)
	@TestCaseParams(testCaseID = { "" }, ignoreTestTimeout = true, allowMedia = true, institutionId="5233219")
	public void testCoursesNewContent() throws Exception {
				
		//className = "Caba Class"; //configuration.getAutomationParam("Caba Class", "classname");
		//institutionId = configuration.getProperty("Caba_InstitutionId");
		//studentId = "52332170001255"; //pageHelper.createUSerUsingSP(institutionId, className);
		
		/* the following section is to close ED page and open static site instead
		webDriver.closeBrowser();
		webDriver.init();
		webDriver.maximize();
		webDriver.openUrl("https://edtoeic1.qa.com");
		*/
		//className = configuration.getAutomationParam("denis_className", "classname");
		//institutionId = configuration.getProperty("denis_institutionId");
		
		//studentId = pageHelper.createUSerUsingSP(institutionId, className);
		//studentId = "52338370000009"; // This is a hardcoded user for TTED static site
		
		NewUXLoginPage loginPage = new NewUXLoginPage(webDriver,testResultService);
		
	//	studentId = "52322820358485";
		institutionId = BasicNewUxTest.institutionId;
	//	String useName = dbService.getUserNameById(studentId, institutionId);
	
		String []studentd  = pageHelper.getStudentsByInstitutionId(institutionId);
		String useName = studentd[0];
		String studentId = studentd[6];
		String password = studentd[3];
		homePage = loginPage.loginAsStudent(useName, password);
		//homePage = createUserAndLoginNewUXClass(configuration.getClassName(), institutionId);
		//homePage = loginPage.loginAsStudent("600000000009", "f90a46"); // This is a hardcoded user for TTED static site
		
		String [] userCourses = pageHelper.getUserAssignedCourses(studentId);
		
				
		for (int i = 0; i < userCourses.length; i++) {
		//for (int i = 0; i < 1; i++) {	
			courseId = userCourses[i];
			
			String courseName = dbService.getCourseNameById(courseId);
			courseName = courseName.toString().replace(" ", "").trim();
			String actualCourse = homePage.getCurrentCourseName();
			//courseId= "136413";
			report.startStep("Navigating to Course: "+ courseName);
			int count = 1;
			while (count < 20 && !(actualCourse.toString().replace(" ", "").trim().equals(courseName))) {
				homePage.carouselNavigateNext();
				sleep(1);
				actualCourse = homePage.getCurrentCourseName();
				count++;
			} 
						
			checkCourseForErrors(courseName, courseId, false, true, true, true);
			
			report.startStep("Save report for Course: " + courseName);
			textService.writeArrayListToCSVFile("smb://" + configuration.getLogerver() + "/"+REPORTS_FOLDER+"/netLog" + "_" + courseName.trim() +".csv", errorList, false);
			errorList.clear();
		}
		
		sleep(1);
	}
	
	private void checkCourseForErrors(String courseName, String courseId, boolean checkConsoleErrors, boolean checkProgress, boolean checkUnitLessonNames, boolean checkStepNames)
			throws Exception {
		
		report.startStep("Get Course Units by courseId");
		learningArea2 = new NewUxLearningArea2(webDriver, testResultService);
		errorList = new ArrayList<>(); 
		
		units = dbService.getCourseUnitDetils(courseId);

		if (checkUnitLessonNames) {
			report.startStep("Check Units scope & sequence on home page");
			validateUnitsOnHomePage();	
		}
		
		for (int i = 0; i < units.size(); i++) { // PRODUCTION
		//for (int i = 9; i < units.size(); i++) {
		//for (int i = 0; i < 1; i++) {
			
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
				validateUnitLessonsOnHomePage(i); // does not work with more then 14 lessons
			}
						
			homePage.clickOnLesson(i, lessonIndex); // PRODUCTION
			//homePage.clickOnLesson(i, 14);
			sleep(2);
						
			for (int j = 0; j < components.size(); j++) { // PRODUCTION
			//for (int j = 0; j < 1; j++) {
			//for (int j = 13; j < components.size(); j++) {	
				
				
				report.startStep("Go over lesson: " + components.get(j)[0]);
						
				report.startStep("Get Component SubComponents(Steps) by componentId");
				List<String[]> subComp = dbService.getSubComponentsDetailsByComponentId(components.get(j)[1]);

				boolean isTest = false;
				boolean isInteract = false;
				boolean stepHasIntro = false;
				String stepName = "undefined";
				stepIndex = 1;
				taskIndex = 1;
				
				if (checkUnitLessonNames) {
					report.startStep("Check Unit and lesson name in Lesson: " + lessonIndex);
					validateUnitLessonsInLearningArea(unitIndex, j);
					sleep(1);
				}
				
				if (checkStepNames) {
					report.startStep("Check Step Name in Step: " + stepIndex);
					validateStepNameAndNumber(subComp.get(stepIndex-1)[0], stepIndex);
				}
								
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
						
						if (checkStepNames) {
							report.startStep("Check Step Name in Step: " + stepIndex);
							validateStepNameAndNumber(subComp.get(stepIndex-1)[0], stepIndex);
						}
						
					} else if (stepHasIntro) learningArea2.clickOnNextButton();
													
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
							//learningArea2.clickOnTaskByNumber(taskIndex);
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
								
				lessonIndex = j + 2;
								
				if (lessonIndex > components.size()){ 
																
					
					if (isTest && checkProgress) learningArea2.submitTest(false);
					
					if (checkProgress) {
						report.startStep("Open Lesson List and check all lessons completed");
						learningArea2.openLessonsList();
						learningArea2.checkLessonsCompletedInLessonList(1, components.size());
						learningArea2.closeLessonsList();
						sleep(1);
					}
					
					report.startStep("Go back to Home Page");
					//learningArea2.clickToOpenNavigationBar();
					//sleep(1);
					learningArea2.clickOnHomeButton();
					if (!checkProgress && isTest) learningArea2.approveTest();
					homePage.waitHomePageloaded();
							
					if (checkProgress) {
					
						report.startStep("Open unit: "+unitIndex+" and check all lessons completed");
						homePage.clickOnUnitLessons(unitIndex);
						homePage.checkLessonsCompletedInLessonList(1, components.size());
						homePage.clickToOpenNavigationBar();
										
						report.startStep("Check Unit "+unitIndex+" Progress Bar");
						double unitProgress = homePage.getUnitProgressBarValue(unitIndex-1);
						double actualUnitProgress = Math.round(unitProgress); 
						//double expectedUnitProgress = Math.round(100.0 / components.size() * (components.size()-2));
						double expectedUnitProgress = 100.0;
						testResultService.assertEquals(expectedUnitProgress, actualUnitProgress,"Progress in Unit Progress Bar is not correct: "+courseName+" : "+"Unit "+unitIndex);
					/*
						report.startStep("Check All Units Progress Bar");
						unitProgress = homePage.getAllUnitsBarUnitProgress(unitIndex);
						actualUnitProgress = Math.round(unitProgress); // roundExpectedProgress(unitProgress, 0).intValue();
						expectedUnitProgress = Math.round(expectedUnitProgress / units.size()); 
						testResultService.assertEquals(expectedUnitProgress, actualUnitProgress, "Progress of Unit in All Units Bar is not correct: "+courseName+" : "+"Unit "+unitIndex);
				*/
						report.startStep("Check Course Completion widget");
						String actCompletion = homePage.getCompletionWidgetValue();
						double totalLessonsInCourse = getNumberOfLessonsInCourse(units);
						totalLessonsCompleted += components.size();
						int expCourseProgress = (int) Math.round(totalLessonsCompleted / totalLessonsInCourse * 100);
						String expectedCourseProgress = String.valueOf(expCourseProgress);
						testResultService.assertEquals(expectedCourseProgress, actCompletion, "Progress of Course in Widget is not correct: " + courseName);
					
					sleep(1);
					}
					
					
				} else {
										
					report.startStep("Navigate to next lesson by lesson list");
					
					if (isTest && checkProgress) {
						learningArea2.submitTest(false);
						learningArea2.openLessonsList();
						sleep(1);
						learningArea2.clickOnLessonByNumber(lessonIndex);
						sleep(2); 
						
					} else if (isTest && !checkProgress)  {
						learningArea2.openLessonsList();
						sleep(1);
						learningArea2.clickOnLessonByNumber(lessonIndex);
						sleep(2);
						learningArea2.approveTest();
					} else { 
						learningArea2.openLessonsList();
						sleep(1);
						learningArea2.clickOnLessonByNumber(lessonIndex);
						sleep(2);				
					}
					
				}

			}
			
		}

	}

	public void validateUnitsOnHomePage() throws Exception { 
		
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
	
	public void validateUnitLessonsOnHomePage(int unitIndex) throws Exception { 
			
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
				
	public void validateUnitLessonsInLearningArea(int unitNumber, int lessonIndex) throws Exception { 
			
		String currentUnitName = learningArea2.openLessonsList().getUnitTitleInLessonList();
		learningArea2.closeLessonsList();
		
		if (!testResultService.assertEquals("Unit " + unitNumber + ": " + unitName, currentUnitName, "Unit name not found/does not match")) {
			webDriver.printScreen("Unit name in learinf area");
		}
				
		for (int f = 0; f < components.size(); f++) {
			expectedLessonName [f] = dbService.getLessonNameBySkill(components.get(f));
		}
				
		String currentLessonName = learningArea2.getLessonNameFromHeader();

		if (expectedLessonName [lessonIndex].startsWith("Story")) { // exception for new units - remove Story: subject from lesson names
			expectedLessonName [lessonIndex] = expectedLessonName [lessonIndex].replace("Story:", "").trim();
		} else expectedLessonName [lessonIndex] = expectedLessonName [lessonIndex].trim();
		
		if (!testResultService.assertEquals(expectedLessonName[lessonIndex].trim(), currentLessonName, "Lesson name does not match")) {
			webDriver.printScreen();
		}
		
	}
	
	public void validateStepNameAndNumber(String expStepName, int stepNumber) throws Exception { 
			
		
		if (expStepName.equals("Practice 1") || expStepName.equals("Practice 2") ){
			expStepName = expStepName.replace("Practice", "Interact");
		}
		
		String actualStepName = learningArea2.getStepNameFromHeader();
		String actualStepNumber = learningArea2.getStepNumberFromHeader();
		actualStepName = actualStepName.replace(". ", "");
		testResultService.assertEquals(expStepName.trim(), actualStepName.trim(), "Step Name does not match");
		testResultService.assertEquals(String.valueOf(stepNumber), actualStepNumber, "Step Number does not match");
	}
		
	public double getNumberOfLessonsInCourse(List<String[]> courseUnits) throws Exception {

		int totalLessons = 0;
		
		for (int j = 0; j < courseUnits.size(); j++) {
			totalLessons = totalLessons + dbService.getComponentDetailsByUnitId(courseUnits.get(j)[0]).size();
		}
		return totalLessons;

	}
		
	@After
	public void tearDown() throws Exception {
		
		super.tearDown();
	
		
	}
	
	
}
