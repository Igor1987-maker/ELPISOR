package tests.tms;


import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import Enums.ByTypes;
import Enums.PLTStartLevel;
import Enums.StepProgressBox;
import Interfaces.TestCaseParams;
import testCategories.inProgressTests;
import Objects.Recording;
import pageObjects.EdoLoginPage;
import pageObjects.RecordPanel;
import pageObjects.edo.NewUXLoginPage;
import pageObjects.edo.NewUxAssessmentsPage;
import pageObjects.edo.NewUxAssignmentsPage;
import pageObjects.edo.NewUxClassicTestPage;
import pageObjects.edo.NewUxHomePage;
import pageObjects.edo.NewUxInstitutionPage;
import pageObjects.edo.NewUxLearningArea;
import pageObjects.edo.NewUxLearningArea2;
import pageObjects.edo.NewUxMyProfile;
import pageObjects.edo.NewUxMyProgressPage;
//import pageObjects.edo.NewUxMyProfile;
import pageObjects.edo.NewUxCommunityPage;
import pageObjects.edo.NewUxSelfRegistrationPage;
import pageObjects.tms.DashboardPage;
import pageObjects.tms.StudentRecordingsPage;
import pageObjects.tms.TmsHomePage;
import tests.edo.newux.SpeechRecognitionBasicTestNewUX;

public class NewUxStudentProgressReport extends SpeechRecognitionBasicTestNewUX {

		
	NewUXLoginPage loginPage;
	NewUxMyProgressPage myProgressPage;
	TmsHomePage tmsHomePage;
	
	
	String classNameSP = null;
	String userNameSP = null;
	
	String courseId;
	List <String> units; 
	String unit_id;
	List <String[]> unit_lessons;
	String lesson_id;
	List <String[]> unit_lesson_steps;
	String step_id;
	List <String[]> step_items;
	
	
	
	@Before
	public void setup() throws Exception {
		super.setup();
		
	}
	
	
	@Test
	@TestCaseParams(testCaseID = {"39320"})
	public void testStudentProgressStepLevelTMS() throws Exception {
	/*	
		int num = 1, max = 20;
		int d=0;
		int m=0;
		float f=0;
		while (num <= max)
		{
		     //if (num%2 == 0)
			d= num/2;
			f = num/2;
			m= num%2;
			System.out.println(num + ":" + d + ";" + f + ";" + m);
		     num++;
		}
		*/
		

		report.startStep("Init test data");
		courseId = courses[2]; // Basic 2
		units = dbService.getCourseUnits(courseId);
				
		report.startStep("Create student in openSpeech class");
		classNameSP = configuration.getProperty("classname.openSpeech");
		//String institutionId = institutionId; //configuration.getProperty("institution.id");
		studentId = pageHelper.createUSerUsingSP(institutionId, classNameSP);
		userNameSP = dbService.getUserNameById(studentId, institutionId);
		
		report.startStep("Open New UX Login Page");
		loginPage = new NewUXLoginPage(webDriver, testResultService);
				
		report.startStep("Create 100% progress in Unit 1 - Lesson 1 - Step 1");
		initItemsForStepProgressSet(1, 1, 1, true);
		studentService.setProgressForSubComponent(step_id, studentId, courseId, 60, step_items, null);
		
		report.startStep("Create 50% progress in Unit 1 - Lesson 1 - Step 1");
		initItemsForStepProgressSet(1, 1, 2, false);
		step_items = studentService.removeItemsFromStepList(step_items, 2);
		studentService.setProgressForSubComponent(step_id, studentId, courseId, 60, step_items, null);
		
		report.startStep("Save current unit / lesson id for validation");
		String unit1_ID = unit_id;
		String unit1_lesson1_ID = lesson_id;
		List <String[]> unit1_lesson1_steps	= unit_lesson_steps;			
		
		
		report.startStep("Create progress in Unit 10 - Lesson 1 - Step 1-5");
		initItemsForStepProgressSet(10, 1, 1, true);
		for (int i = 1; i < 6; i++) {
			
			if (i%2 == 0){
				initItemsForStepProgressSet(10, 1, i, false);
				studentService.setProgressForSubComponent(step_id, studentId, courseId, 60, step_items, null);
			} else {
				initItemsForStepProgressSet(10, 1, i, false);
				step_items = studentService.removeItemsFromStepList(step_items, 1);
				studentService.setProgressForSubComponent(step_id, studentId, courseId, 60, step_items, null);
			}
		}
		
		report.startStep("Submit Test in Unit 10 - Lesson 1 - Step 6");
		initItemsForStepProgressSet(10, 1, 6, false);
		studentService.submitTest(studentId, unit_id, lesson_id, step_items, false);
		
		report.startStep("Save current unit / lesson id for validation");
		String unit10_ID = unit_id;
		String unit10_lesson1_ID = lesson_id;
		List <String[]> unit10_lesson1_steps = unit_lesson_steps;	
		
		report.startStep("Login as Teacher");
		pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(autoInstitution.getTeacherUserName(),institutionId));
		sleep(3);
		tmsHomePage = loginPage.enterTeacherUserAndPassword();
		//loginPage.enterSchoolAdminUserAndPassword();
		
		sleep(3);
		tmsHomePage.switchToMainFrame();
		
		report.startStep("Open Student Progress report");
		tmsHomePage.clickOnReports();
		sleep(2);
		tmsHomePage.clickOnCourseReports();
		sleep(2);
		tmsHomePage.switchToFormFrame();
		sleep(1);
		tmsHomePage.selectReport("1");
		sleep(5);
						
		report.startStep("Find student in search section");
		tmsHomePage.clickOnSearch();
		sleep(2);
		tmsHomePage.findStudentInSearchSection(classNameSP, userNameSP);
		sleep(2);
		
		report.startStep("Select course");
		tmsHomePage.switchToMainFrame();
		tmsHomePage.selectCourse(courseId);
		sleep(2);
		
		report.startStep("Open Unit 1 - Lesson 1");
		tmsHomePage.openStudentStepProgressByUnitLessonId(unit1_ID, unit1_lesson1_ID);
		
		report.startStep("Verify step name in tooltip in Unit 1 - Lesson 1");
		
		for (int i = 0; i < unit1_lesson1_steps.size(); i++) {
			tmsHomePage.verifyStepBoxTooltip(unit1_ID, unit1_lesson1_ID, (i+1), unit1_lesson1_steps.get(i)[0]);
		}
		
		report.startStep("Verify step progress indication in Unit 1 - Lesson 1");
		tmsHomePage.verifyStepProgressIndication(unit1_ID, unit1_lesson1_ID, 1 , StepProgressBox.done);
		tmsHomePage.verifyStepProgressIndication(unit1_ID, unit1_lesson1_ID, 2 , StepProgressBox.half);
		tmsHomePage.verifyStepProgressIndication(unit1_ID, unit1_lesson1_ID, 3 , StepProgressBox.empty);
		
		report.startStep("Open Unit 10 - Lesson 1");
		tmsHomePage.openStudentStepProgressByUnitLessonId(unit10_ID, unit10_lesson1_ID);
		
		report.startStep("Verify step name in tooltip in Unit 10 - Lesson 1");
		
		for (int i = 0; i < unit10_lesson1_steps.size(); i++) {
			tmsHomePage.verifyStepBoxTooltip(unit10_ID, unit10_lesson1_ID, (i+1), unit10_lesson1_steps.get(i)[0]);
		}
		
		
		report.startStep("Verify step progress indication in Unit 10 - Lesson 1");
		for (int j = 1; j <= unit_lesson_steps.size(); j++) {
	
			/*
			switch (j){
			
			case 2: case 2: case 4: case 6:
				tmsHomePage.verifyStepProgressIndication(unit10_ID, unit10_lesson1_ID, j, StepProgressBox.done);
				break;
				
			case 1: case 1: case 3 :case 5:
				tmsHomePage.verifyStepProgressIndication(unit10_ID, unit10_lesson1_ID, j , StepProgressBox.half);
				break;
			}
			*/
			
			if (j%2==0) // Even Number
				tmsHomePage.verifyStepProgressIndication(unit10_ID, unit10_lesson1_ID, j, StepProgressBox.done);
			
			else // UnEven number
				tmsHomePage.verifyStepProgressIndication(unit10_ID, unit10_lesson1_ID, j , StepProgressBox.half);
			
		}
				
		sleep(3);
		
	}
	
	@Test
	@TestCaseParams(testCaseID = {"40255"}, skippedBrowsers = {"firefox"}) // Actions API not supported by geckodriver)
	public void testStudentMyProgressStepLevelED() throws Exception {
		

		report.startStep("Init test data");
		courseId = courses[2]; // Basic 2
		units = dbService.getCourseUnits(courseId);
		int unit1 = 1;
		int unit10 = 10;
		int lessonNum = 1;
		
				
		report.startStep("Create student in openSpeech class");
		classNameSP = configuration.getProperty("classname.openSpeech");
		institutionId = configuration.getProperty("institution.id");
		studentId = pageHelper.createUSerUsingSP(institutionId, classNameSP);
		userNameSP = dbService.getUserNameById(studentId, institutionId);
		
		report.startStep("Open New UX Login Page");
		loginPage = new NewUXLoginPage(webDriver, testResultService);
				
		report.startStep("Create 100% progress in Unit 1 - Lesson 1 - Step 1");
		initItemsForStepProgressSet(unit1, lessonNum, 1, true);
		List <String[]> unit1_lesson1_steps = unit_lesson_steps;
		studentService.setProgressForSubComponent(step_id, studentId, courseId, 60, step_items, null);
		
		report.startStep("Create 50% progress in Unit 1 - Lesson 1 - Step 2");
		initItemsForStepProgressSet(unit1, lessonNum, 2, false);
		step_items = studentService.removeItemsFromStepList(step_items, 2);
		studentService.setProgressForSubComponent(step_id, studentId, courseId, 60, step_items, null);
					
				
		report.startStep("Create progress in Unit 10 - Lesson 1 - Step 1-5");
		initItemsForStepProgressSet(unit10, lessonNum, 1, true);
		List <String[]> unit10_lesson1_steps = unit_lesson_steps;
		for (int i = 1; i < 6; i++) {
			
			if (i%2 == 0){
				initItemsForStepProgressSet(unit10, lessonNum, i, false);
				studentService.setProgressForSubComponent(step_id, studentId, courseId, 60, step_items, null);
			} else {
				initItemsForStepProgressSet(unit10, lessonNum, i, false);
				step_items = studentService.removeItemsFromStepList(step_items, 1);
				studentService.setProgressForSubComponent(step_id, studentId, courseId, 60, step_items, null);
			}
		}
		
		report.startStep("Submit Test in Unit 10 - Lesson 1 - Step 6");
		initItemsForStepProgressSet(unit10, lessonNum, 6, false);
		studentService.submitTest(studentId, unit_id, lesson_id, step_items, false);
				
		report.startStep("Login as Student and open My Progress page");
		homePage = loginAsStudent(studentId);
		sleep(3);
		myProgressPage = homePage.clickOnMyProgress();
		sleep(3);
		report.startStep("Open Unit 1 - Lesson 1");
		myProgressPage.clickToOpenUnitLessonsProgress(unit1);
		sleep(3);
		
		report.startStep("Verify step name in tooltip in Unit 1 - Lesson 1"); 
		for (int i = 0; i < unit1_lesson1_steps.size(); i++) {
			myProgressPage.verifyStepNameInTooltip(lessonNum, (i+1), unit1_lesson1_steps.get(i)[0]);
		}
		
		report.startStep("Verify step progress indication in Unit 1 - Lesson 1");
		myProgressPage.verifyStepProgressIndication(lessonNum, 1, StepProgressBox.done);
		myProgressPage.verifyStepProgressIndication(lessonNum, 2, StepProgressBox.half);
		myProgressPage.verifyStepProgressIndication(lessonNum, 3, StepProgressBox.empty);
		
		report.startStep("Open Unit 10 - Lesson 1");
		myProgressPage.clickToOpenUnitLessonsProgress(unit10);
		sleep(1);
		
		report.startStep("Verify step name in tooltip in Unit 10 - Lesson 1"); 
		for (int i = 0; i < unit10_lesson1_steps.size(); i++) {
			myProgressPage.verifyStepNameInTooltip(lessonNum, (i+1),unit10_lesson1_steps.get(i)[0]);
		}
				
		report.startStep("Verify step progress indication in Unit 10 - Lesson 1");
		for (int j = 1; j <= unit_lesson_steps.size(); j++) {
			
			if (j%2==0) myProgressPage.verifyStepProgressIndication(lessonNum, j, StepProgressBox.done);
			else myProgressPage.verifyStepProgressIndication(lessonNum, j, StepProgressBox.half);
		}
				
		
	}
	
	private void initItemsForStepProgressSet(int unitNum, int lessonNumber, int stepNumber, boolean resetUnitLesson) throws Exception {
		
		
		int unitIndex = unitNum - 1;
		int lessonIndex = lessonNumber - 1;
		int stepIndex = stepNumber - 1;
		
			if (resetUnitLesson) {
		
				unit_id = units.get(unitIndex);
				unit_lessons = dbService.getComponentDetailsByUnitId(unit_id);
				
				lesson_id = unit_lessons.get(lessonIndex)[1];
				unit_lesson_steps = dbService.getSubComponentsDetailsByComponentId(lesson_id);
			}
				
				step_id = unit_lesson_steps.get(stepIndex)[1];
				step_items = dbService.getSubComponentItems(step_id);
		
		
	}
					
	@After
	public void tearDown() throws Exception {
				
		super.tearDown();
		
	}
	
	
}
