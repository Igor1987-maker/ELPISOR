package tests.tms;


import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.server.handler.FindElement;

import Enums.ByTypes;
import Enums.PLTStartLevel;
import Enums.StepProgressBox;
import Interfaces.TestCaseParams;
import testCategories.AngularLearningArea;
import testCategories.NonAngularLearningArea;
import testCategories.inProgressTests;
import Objects.CourseNew;
import Objects.CourseUnitNew;
import Objects.CourseUnitComponentNew;
import Objects.Recording;
import Objects.UnitComponent;
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
//import pageObjects.edo.NewUxMyProfile;
import pageObjects.edo.NewUxCommunityPage;
import pageObjects.edo.NewUxSelfRegistrationPage;
import pageObjects.tms.CurriculumAssignCoursesPage;
import pageObjects.tms.DashboardPage;
import pageObjects.tms.StudentRecordingsPage;
import pageObjects.tms.TmsCreateNewCoursePage;
import pageObjects.tms.TmsHomePage;
import tests.edo.newux.SpeechRecognitionBasicTestNewUX;
import tests.edo.newux.testDataValidation;

public class NewUxCourseBuilderTests extends SpeechRecognitionBasicTestNewUX {

		
	//private static final String curriculumFolderJS = "smb://frontqa3/TMSUX/JS/Curriculum/";
	private static final String curriculumFolderJS = "JS/Curriculum/";
	
	NewUXLoginPage loginPage;
	TmsHomePage tmsHomePage;
	TmsCreateNewCoursePage tmsNewCourse;
	testDataValidation testData;
	NewUxLearningArea learningArea;
	NewUxLearningArea2 learningArea2;
		
	CourseNew expCourse;
	
	String instId;
	String courseId;
	String teacherId;
	String newCourseName;
	
	
	@Before
	public void setup() throws Exception {
		super.setup();
		
	}
	
	
	@Test
	@Category(NonAngularLearningArea.class)
	@TestCaseParams(testCaseID = {"39721"}, testTimeOut="15")
	public void testNewCourseBuilderNotBasedOn() throws Exception {
			
		loginPage = new NewUXLoginPage(webDriver, testResultService);
		testData = new testDataValidation();
		instId = autoInstitution.getInstitutionId();
						
		report.startStep("Init test data");
		String courseToBaseName = coursesNames[1]; // Basic 1
		String oldVersion = "3.73";
		String newVersion = "4.33";
		String instCurriculumPath = pageHelper.buildPathTMS + curriculumFolderJS + instId + ".js";
		
		int iteration = 1;
						
		report.startStep("Login as Teacher");
		teacherId = dbService.getUserIdByUserName(autoInstitution.getTeacherUserName(),instId);
		pageHelper.setUserLoginToNull(teacherId);
		sleep(2);
		tmsHomePage = loginPage.enterTeacherUserAndPassword();
		sleep(2);
		tmsHomePage.switchToMainFrame();
		
		report.startStep("Open Course Builder and press New");
		tmsHomePage.clickOnCurriculum();
		sleep(2);
		tmsHomePage.clickOnCourseBuilder();
		sleep(2);
			
		while (iteration<=2) {
		
		String courseSuffix = dbService.sig(3);
			
		tmsNewCourse = tmsHomePage.clickOnNewInCourseBuilder();
		sleep(2);
		webDriver.switchToNextTab();
		
		report.startStep("Select Version");
		if (iteration==1) tmsNewCourse.selectVersion(oldVersion);
		else tmsNewCourse.selectVersion(newVersion);
		
		report.startStep("Enter course name");
		newCourseName = "New Course " + courseSuffix;
		tmsNewCourse.enterNewCourseName(newCourseName);
				
		report.startStep("Press OK");
		tmsNewCourse.clickOnOK();
		webDriver.switchToMainWindow();
		tmsHomePage.switchToMainFrame();
		sleep(2);
				
		report.startStep("Verify Course Name and Version");
		tmsHomePage.verifyCourseNameInCourseBuilder(newCourseName);
		if (iteration==1) {
			tmsHomePage.verifyCourseVersionInCourseBuilder(oldVersion);
		} else {
			tmsHomePage.verifyCourseVersionInCourseBuilder(newVersion);
		}
		
		report.startStep("Check default structure of new course");
										
		String originJS = textService.getSmbFileContent(instCurriculumPath);
		JSONArray curriculum = testData.getJsonArrayFromCurriculumJS(originJS);
		verifyDefaultViewNewCourseNotBasedOn(courseToBaseName, curriculum, false);
					
		report.startStep("Save new course");
		tmsHomePage.clickOnSaveInCourseBuilder();
		courseId = dbService.getCourseIdByName(newCourseName);
		
		report.startStep("Verify Lesson Preview");
		verifyLessonPreview(1, 1);
					
			try {
				report.startStep("Open created course");
				tmsNewCourse = tmsHomePage.clickOnOpenInCourseBuilder();
				sleep(2);
				webDriver.switchToNextTab();
				
				report.startStep("Select Version");
				if (iteration==1) tmsNewCourse.selectVersion(oldVersion);
				else tmsNewCourse.selectVersion(newVersion);
				
				report.startStep("Select created course");
				tmsNewCourse.selectCourseToBase(newCourseName);
				
				report.startStep("Press OK");
				tmsNewCourse.clickOnOK();
				webDriver.switchToMainWindow();
				
				tmsHomePage.switchToMainFrame();
				
				report.startStep("Verify Course Name");
				tmsHomePage.verifyCourseNameInCourseBuilder(newCourseName);
								
				report.startStep("Check default structure of created course");
				originJS = textService.getSmbFileContent(instCurriculumPath);
				curriculum = testData.getJsonArrayFromCurriculumJS(originJS);
				
				verifyDefaultViewNewCourseNotBasedOn(newCourseName, curriculum, true);
				
				report.startStep("Select course and press on Delete");
				tmsHomePage.checkCourseAndDeleteInCourseBuilder();
				
				report.startStep("Click on Open again");
				tmsNewCourse = tmsHomePage.clickOnOpenInCourseBuilder();
				sleep(2);
				webDriver.switchToNextTab();
				
				report.startStep("Select Version");
				if (iteration==1) tmsNewCourse.selectVersion(oldVersion);
				else tmsNewCourse.selectVersion(newVersion);
				
				report.startStep("Check course not displayed");
				tmsNewCourse.verifyCourseDoesNotExistInDropDown(newCourseName);
				
				report.startStep("Press Cancel - close popup");
				tmsNewCourse.clickOnCancel();
				webDriver.switchToMainWindow();
				
				tmsHomePage.switchToMainFrame();
				sleep(1);
				
			} catch (Exception e) {
				
				pageHelper.deleteCourseByCourseOwnerId(courseId, teacherId);
			
			}
					
		iteration++;	
		
		}
	}

	
	@Test
	@Category(AngularLearningArea.class)
	@TestCaseParams(testCaseID = {"39721"}, testTimeOut="15")
	public void testNewCourseBuilderNotBasedOn2() throws Exception {
			
		loginPage = new NewUXLoginPage(webDriver, testResultService);
		testData = new testDataValidation();
		instId = autoInstitution.getInstitutionId();
							
			report.startStep("Init test data");
				String courseToBaseName = coursesNames[1]; // Basic 1
				String oldVersion = "3.73";
				String newVersion = "4.33";
				String instCurriculumPath = pageHelper.buildPathTMS + curriculumFolderJS + instId + ".js";
				
				int iteration = 1;
								
			report.startStep("Login as Teacher");
				teacherId = dbService.getUserIdByUserName(autoInstitution.getTeacherUserName(),instId);
				pageHelper.setUserLoginToNull(teacherId);
				sleep(2);
				tmsHomePage = loginPage.enterTeacherUserAndPassword();
				sleep(2);
				
				webDriver.waitForJqueryToFinish();
				
				tmsHomePage.switchToMainFrame();
				
			report.startStep("Open Course Builder and press New");
				tmsHomePage.clickOnCurriculum();
				sleep(2);
				tmsHomePage.clickOnCourseBuilder();
				sleep(2);
					
				while (iteration<=2) {
				
				String courseSuffix = dbService.sig(3);
					
				tmsNewCourse = tmsHomePage.clickOnNewInCourseBuilder();
				sleep(2);
				webDriver.switchToNextTab();
				
			report.startStep("Select Version");
				if (iteration==1) tmsNewCourse.selectVersion(oldVersion);
				else tmsNewCourse.selectVersion(newVersion);
				
			report.startStep("Enter course name");
				newCourseName = "New Course " + courseSuffix;
				tmsNewCourse.enterNewCourseName(newCourseName);
						
			report.startStep("Press OK");
				tmsNewCourse.clickOnOK();
				webDriver.switchToMainWindow();
				tmsHomePage.switchToMainFrame();
				sleep(2);
						
			report.startStep("Verify Course Name and Version");
				tmsHomePage.verifyCourseNameInCourseBuilder(newCourseName);
				if (iteration==1) {
					tmsHomePage.verifyCourseVersionInCourseBuilder(oldVersion);
				} else {
					tmsHomePage.verifyCourseVersionInCourseBuilder(newVersion);
				}
				
			report.startStep("Check default structure of new course");
	
				String originJS = textService.getSmbFileContent(instCurriculumPath);
				JSONArray curriculum = testData.getJsonArrayFromCurriculumJS(originJS);
				verifyDefaultViewNewCourseNotBasedOn(courseToBaseName, curriculum, false);
					
			report.startStep("Save new course");
				tmsHomePage.clickOnSaveInCourseBuilder();
				courseId = dbService.getCourseIdByName(newCourseName);
		
			report.startStep("Verify Lesson Preview");
				verifyLessonPreview2(1, 1);
					
		try {
			report.startStep("Open created course");
				tmsNewCourse = tmsHomePage.clickOnOpenInCourseBuilder();
				sleep(2);
				webDriver.switchToNextTab();
				
			report.startStep("Select Version");
				if (iteration==1) tmsNewCourse.selectVersion(oldVersion);
				else tmsNewCourse.selectVersion(newVersion);
				
			report.startStep("Select created course");
				tmsNewCourse.selectCourseToBase(newCourseName);
				
				report.startStep("Press OK");
				tmsNewCourse.clickOnOK();
				webDriver.switchToMainWindow();
				
				tmsHomePage.switchToMainFrame();
				
			report.startStep("Verify Course Name");
				tmsHomePage.verifyCourseNameInCourseBuilder(newCourseName);
								
			report.startStep("Check default structure of created course");
				originJS = textService.getSmbFileContent(instCurriculumPath);
				curriculum = testData.getJsonArrayFromCurriculumJS(originJS);
				
				verifyDefaultViewNewCourseNotBasedOn(newCourseName, curriculum, true);
				
			report.startStep("Select course and press on Delete");
				tmsHomePage.checkCourseAndDeleteInCourseBuilder();
				
			report.startStep("Click on Open again");
				tmsNewCourse = tmsHomePage.clickOnOpenInCourseBuilder();
				sleep(2);
				webDriver.switchToNextTab();
				
				report.startStep("Select Version");
				if (iteration==1) 
					tmsNewCourse.selectVersion(oldVersion);
				else 
					tmsNewCourse.selectVersion(newVersion);
				
				report.startStep("Check course not displayed");
					tmsNewCourse.verifyCourseDoesNotExistInDropDown(newCourseName);
				
				report.startStep("Press Cancel - close popup");
					tmsNewCourse.clickOnCancel();
					webDriver.switchToMainWindow();
				
					tmsHomePage.switchToMainFrame();
					sleep(1);
				
			} catch (Exception e) {
				
				pageHelper.deleteCourseByCourseOwnerId(courseId, teacherId);
			
			}
					
		iteration++;	
		
		}
	}

	
	@Test
	@Category(NonAngularLearningArea.class)
	@TestCaseParams(testCaseID = {"39722"}, testTimeOut="30")
	public void testNewCourseBuilderBasedOn() throws Exception {
			
		loginPage = new NewUXLoginPage(webDriver, testResultService);
		testData = new testDataValidation();
		instId = autoInstitution.getInstitutionId();
						
		report.startStep("Init test data");
		String courseToBaseName = coursesNames[4]; // Intermediate 1
		String courseToBaseID = "undefined"; // Intermediate 1
		String oldVersion = "3.73";
		String newVersion = "4.33";
		int iteration = 1;
		
								
		report.startStep("Login as Teacher");
		
		teacherId = dbService.getUserIdByUserName(autoInstitution.getTeacherUserName(), instId);
		pageHelper.setUserLoginToNull(teacherId);
		sleep(2);
		tmsHomePage = loginPage.enterTeacherUserAndPassword();
		sleep(2);
		tmsHomePage.switchToMainFrame();
		
		report.startStep("Open Course Builder and press New");
		tmsHomePage.clickOnCurriculum();
		sleep(2);
		tmsHomePage.clickOnCourseBuilder();
		sleep(2);
			
		while (iteration<=2) {
		
		String courseSuffix = dbService.sig(3);
			
		tmsNewCourse = tmsHomePage.clickOnNewInCourseBuilder();
		sleep(5);
		webDriver.switchToNextTab();
		
		report.startStep("Select Version");
		if (iteration==1) {
			tmsNewCourse.selectVersion(oldVersion);
			courseToBaseID = coursesOld[4];
			expCourse = pageHelper.setCourseUnitsByCourseId(courseToBaseID);
		}
		else {
			tmsNewCourse.selectVersion(newVersion);
			courseToBaseID = courses[4];
			expCourse = pageHelper.setCourseUnitsByCourseId(courseToBaseID);
		}
		
		report.startStep("Enter course name");
		newCourseName = "New Course " + courseSuffix;
		tmsNewCourse.enterNewCourseName(newCourseName);
		
		report.startStep("Select base on option");
		tmsNewCourse.selectBasedOnOption();
		sleep(2);
		
		report.startStep("Select course to base on");
		tmsNewCourse.selectCourseToBase(courseToBaseName);
		sleep(2);
		
		report.startStep("Press OK");
		tmsNewCourse.clickOnOK();
		sleep(2);
		webDriver.switchToMainWindow();
		sleep(1);
		tmsHomePage.switchToMainFrame();
		sleep(1);
		
		report.startStep("Verify Lesson Preview");
		verifyLessonPreview(2, 2);
		
		report.startStep("Verify Course Name and Version");
		tmsHomePage.verifyCourseNameInCourseBuilder(newCourseName);
		if (iteration==1) {
			tmsHomePage.verifyCourseVersionInCourseBuilder(oldVersion);
		} else {
			tmsHomePage.verifyCourseVersionInCourseBuilder(newVersion);
		}
		
		report.startStep("Check default structure of new course");
		verifyDefaultViewNewCourseBasedOn(expCourse, false);
										
		report.startStep("Save new course");
		tmsHomePage.clickOnSaveInCourseBuilder();
		courseId = dbService.getCourseIdByName(newCourseName);
					
			try {
				report.startStep("Open created course");
				tmsNewCourse = tmsHomePage.clickOnOpenInCourseBuilder();
				sleep(2);
				webDriver.switchToNextTab();
				
				report.startStep("Select Version");
				if (iteration==1) tmsNewCourse.selectVersion(oldVersion);
				else tmsNewCourse.selectVersion(newVersion);
				
				report.startStep("Select created course");
				tmsNewCourse.selectCourseToBase(newCourseName);
				
				report.startStep("Press OK");
				tmsNewCourse.clickOnOK();
				webDriver.switchToMainWindow();
				
				tmsHomePage.switchToMainFrame();
				
				report.startStep("Verify Course Name");
				tmsHomePage.verifyCourseNameInCourseBuilder(newCourseName);
								
				report.startStep("Check default structure of created course");
				verifyDefaultViewNewCourseBasedOn(expCourse, true);
								
				report.startStep("Select course and press on Delete");
				tmsHomePage.checkCourseAndDeleteInCourseBuilder();
				
				report.startStep("Click on Open again");
				tmsNewCourse = tmsHomePage.clickOnOpenInCourseBuilder();
				sleep(2);
				webDriver.switchToNextTab();
				
				report.startStep("Select Version");
				if (iteration==1) tmsNewCourse.selectVersion(oldVersion);
				else tmsNewCourse.selectVersion(newVersion);
				
				report.startStep("Check course not displayed");
				tmsNewCourse.verifyCourseDoesNotExistInDropDown(newCourseName);
				
				report.startStep("Press Cancel - close popup");
				tmsNewCourse.clickOnCancel();
				webDriver.switchToMainWindow();
				
				tmsHomePage.switchToMainFrame();
				sleep(1);
				
			} catch (Exception e) {
				
				pageHelper.deleteCourseByCourseOwnerId(courseId, teacherId);
			
			}
			
				
		iteration++;	
		
		}
		
		
	}
	
	@Test
	@Category(AngularLearningArea.class)
	@TestCaseParams(testCaseID = {"39722"}, testTimeOut="30")
	public void testCourseBuilderVerifyUnitAndSkillsDropboxes() throws Exception {
		loginPage = new NewUXLoginPage(webDriver, testResultService);
		testData = new testDataValidation();
		instId = autoInstitution.getInstitutionId();
						
		report.startStep("Init test data");
			String courseToBaseName = coursesNames[4]; // Intermediate 1
			String courseToBaseID = "undefined"; // Intermediate 1
			String oldVersion = "3.73";
			String newVersion = "4.33";
			int iteration = 1;
			String className = "assessmentClass";
			String classId = pageHelper.getInternalClassId(className, instId);
			String [] studentd = pageHelper.getStudentsByInstitutionId(instId, classId);
			String studName = studentd[0];
			
								
		report.startStep("Login as Teacher");
		
			teacherId = dbService.getUserIdByUserName(autoInstitution.getTeacherUserName(), instId);
			pageHelper.setUserLoginToNull(teacherId);
			sleep(2);
			tmsHomePage = loginPage.enterTeacherUserAndPassword();
			sleep(2);
			homePage.closeAllNotifications();
			tmsHomePage.switchToMainFrame();
		
		report.startStep("Open Course Builder and press New");
			tmsHomePage.clickOnCurriculum();
			sleep(2);
			tmsHomePage.clickOnCourseBuilder();
			sleep(1);
			
			String courseSuffix = dbService.sig(4);
			tmsNewCourse = tmsHomePage.clickOnNewInCourseBuilder();
			sleep(3);
		//	tmsNewCourse = tmsHomePage.clickOnNewInCourseBuilder();
			webDriver.switchToNextTab();	
		
		report.startStep("Enter course name");
		newCourseName = "New Course " + courseSuffix;
			tmsNewCourse.enterNewCourseName(newCourseName);
			//tmsHomePage.clickOnCourseBasedOn();
			tmsNewCourse.selectVersion(newVersion);
			report.startStep("Select base on option");
			tmsNewCourse.selectBasedOnOption();
			sleep(2);
		
		report.startStep("Select course to base on");
			tmsNewCourse.selectCourseToBase(courseToBaseName);
			sleep(4);
		
		report.startStep("Press OK");
			tmsNewCourse.clickOnOK();
			sleep(2);
			webDriver.switchToMainWindow();
			sleep(1);
			tmsHomePage.switchToMainFrame();
			sleep(1);
			tmsNewCourse.slectLevel();
			tmsNewCourse.selectUnit();
			List<String> componentsByUnit = tmsNewCourse.retrieveAllComponents();
			tmsNewCourse.selectSkill();
			List<String> componentsBySkill = tmsNewCourse.retrieveAllComponents();
			textService.assertTrue("Select by unit must narrow search", componentsByUnit.size()<componentsBySkill.size());
			String unit = tmsNewCourse.nameOfDisplayedUnit();
			textService.assertTrue("Unit name is not default ", unit.equals("Unit"));
			tmsNewCourse.selectUnit();
			String skill = tmsNewCourse.nameOfDisplayedSkill();
			textService.assertTrue("Skill name is not default ", skill.equals("Skill"));
			boolean listMatches = tmsNewCourse.verifyListOfComponentsByUnitDisplayedCorrect(componentsByUnit);
			textService.assertTrue("Unit component not correct", listMatches);
			tmsNewCourse.selectComponent();
			tmsHomePage.clickOnSaveInCourseBuilder();
			
		
		report.startStep("Verify Lesson Preview");
			verifyLessonPreview2(1, 1);
		
		report.startStep("Verify Course Name and Version");
			tmsHomePage.verifyCourseNameInCourseBuilder(newCourseName);
		//	tmsHomePage.verifyCourseVersionInCourseBuilder(newVersion);
			//tmsHomePage.verifyCourseVersionInCourseBuilder(newVersion);
			
	//	report.startStep("Check default structure of new course");
	//		verifyDefaultViewNewCourseBasedOn(expCourse, false);
										
		//report.startStep("Save new course");
		//	tmsHomePage.clickOnSaveInCourseBuilder();
			courseId = dbService.getCourseIdByName(newCourseName);
		
		try {
			report.startStep("Open created course");
			tmsNewCourse = tmsHomePage.clickOnOpenInCourseBuilder();
			sleep(2);
			webDriver.switchToNextTab();
			
			report.startStep("Select Version");
		//	if (iteration==1) tmsNewCourse.selectVersion(oldVersion);
			//else 
			tmsNewCourse.selectVersion(newVersion);
			
			report.startStep("Select created course");
			tmsNewCourse.selectCourseToBase(newCourseName);
			
			report.startStep("Press OK");
			tmsNewCourse.clickOnOK();
			webDriver.switchToMainWindow();
			sleep(4);
			tmsHomePage.switchToMainFrame();
			
			report.startStep("Verify Course Name");
			sleep(2);
			tmsHomePage.verifyCourseNameInCourseBuilder(newCourseName);
		}catch (Exception e) {
				pageHelper.deleteCourseByCourseOwnerId(courseId, teacherId);
			}
		tmsHomePage.clickOnAssignCourses();
		tmsHomePage.selectClass(className,true,false);
		tmsHomePage.selecStudent(studName);
		tmsHomePage.clickOnGo();
		//tmsHomePage.switchToFormFrame();
		webDriver.switchToTopMostFrame();
		tmsHomePage.switchToMainFrame();
		Thread.sleep(1000);
		
		CurriculumAssignCoursesPage curriculumAssignCoursesPage = new CurriculumAssignCoursesPage(webDriver, testResultService);
		curriculumAssignCoursesPage.unmarkAllCourses();
		curriculumAssignCoursesPage.markCheckBoxOfSpecificCourse(newCourseName);
		tmsHomePage.clickOnSave();
		sleep(2);
		tmsHomePage.clickOnExit();
		/*
		report.startStep("Assign Course Test to student");
		String wantedTestId = "989013148";
		pageHelper.assignAutomatedTestToStudetInTestEnvironment(studentId, courseId, testTypeId,0,0,1);
		sleep(2);
		String testId = pageHelper.getAssignedTestIdForStudent(studentId,courseId, testTypeId);
		if (!testId.equals(wantedTestId)) {
			// update test id
			String userExitSettingsId = dbService.getUserExitSettingsId(studentId, testId, courseId, impType);
			dbService.updateTestIdInUserExitSettings(wantedTestId, userExitSettingsId);
		}
		*/
		sleep(2);
				
		report.startStep("Log In as Student");
	//	String userName = dbService.getUserNameById(studentId, instId);
		NewUXLoginPage loginPage = new NewUXLoginPage(webDriver, testResultService);
		homePage = loginPage.loginAsStudent(studName, "12345");
		pageHelper.skipOptin();
		homePage.closeAllNotifications();
		pageHelper.skipOnBoardingHP();
		homePage.waitHomePageloaded();
		String actualCourse = homePage.getCurrentCourseName();
		String actualUnitName = homePage.getUnitName();
		//actualUnitName = getPartOfUnitName(actualUnitName);
		textService.assertEquals("Select by unit must narrow search", newCourseName, actualCourse);
		textService.assertEquals("Select by unit must narrow search", "Unit 1: Education", actualUnitName);
		
		homePage.logOutOfED();
		pageHelper.deleteCourseByCourseOwnerId(courseId, teacherId);
		
	}	

	private String getPartOfUnitName(String name) {
		
		String []arr = name.split(":");
		return arr[1];
	
	}


	@Test
	@Category(AngularLearningArea.class)
	@TestCaseParams(testCaseID = {"39722"}, testTimeOut="30")
	public void testNewCourseBuilderBasedOn2() throws Exception {
			
		loginPage = new NewUXLoginPage(webDriver, testResultService);
		testData = new testDataValidation();
		instId = autoInstitution.getInstitutionId();
						
		report.startStep("Init test data");
		String courseToBaseName = coursesNames[4]; // Intermediate 1
		String courseToBaseID = "undefined"; // Intermediate 1
		String oldVersion = "3.73";
		String newVersion = "4.33";
		int iteration = 1;
		
								
		report.startStep("Login as Teacher");
		
		teacherId = dbService.getUserIdByUserName(autoInstitution.getTeacherUserName(), instId);
		pageHelper.setUserLoginToNull(teacherId);
		sleep(2);
		tmsHomePage = loginPage.enterTeacherUserAndPassword();
		sleep(2);
		tmsHomePage.switchToMainFrame();
		
		report.startStep("Open Course Builder and press New");
		tmsHomePage.clickOnCurriculum();
		sleep(2);
		tmsHomePage.clickOnCourseBuilder();
		sleep(1);
			
		while (iteration<=2) {
		
		String courseSuffix = dbService.sig(5);
			
		tmsNewCourse = tmsHomePage.clickOnNewInCourseBuilder();
		sleep(3);
		webDriver.switchToNextTab();
		
		report.startStep("Select Version");
		
		if (iteration==1) {
			tmsNewCourse.selectVersion(oldVersion);
			courseToBaseID = coursesOld[4];
			expCourse = pageHelper.setCourseUnitsByCourseId(courseToBaseID);
		}
		else {
			tmsNewCourse.selectVersion(newVersion);
			courseToBaseID = courses[4];
			expCourse = pageHelper.setCourseUnitsByCourseId(courseToBaseID);
		}
		
		report.startStep("Enter course name");
		newCourseName = "New Course " + courseSuffix;
		tmsNewCourse.enterNewCourseName(newCourseName);
		
		report.startStep("Select base on option");
		tmsNewCourse.selectBasedOnOption();
		sleep(2);
		
		report.startStep("Select course to base on");
		tmsNewCourse.selectCourseToBase(courseToBaseName);
		sleep(1);
		
		report.startStep("Press OK");
		tmsNewCourse.clickOnOK();
		sleep(2);
		webDriver.switchToMainWindow();
		sleep(1);
		tmsHomePage.switchToMainFrame();
		sleep(1);
		
		report.startStep("Verify Lesson Preview");
		verifyLessonPreview2(2, 2);
		
		report.startStep("Verify Course Name and Version");
		tmsHomePage.verifyCourseNameInCourseBuilder(newCourseName);
		if (iteration==1) {
			tmsHomePage.verifyCourseVersionInCourseBuilder(oldVersion);
		} else {
			tmsHomePage.verifyCourseVersionInCourseBuilder(newVersion);
		}
		
		report.startStep("Check default structure of new course");
		verifyDefaultViewNewCourseBasedOn(expCourse, false);
										
		report.startStep("Save new course");
		tmsHomePage.clickOnSaveInCourseBuilder();
		courseId = dbService.getCourseIdByName(newCourseName);
					
			try {
				report.startStep("Open created course");
				tmsNewCourse = tmsHomePage.clickOnOpenInCourseBuilder();
				sleep(2);
				webDriver.switchToNextTab();
				
				report.startStep("Select Version");
				if (iteration==1) tmsNewCourse.selectVersion(oldVersion);
				else tmsNewCourse.selectVersion(newVersion);
				
				report.startStep("Select created course");
				tmsNewCourse.selectCourseToBase(newCourseName);
				
				report.startStep("Press OK");
				tmsNewCourse.clickOnOK();
				webDriver.switchToMainWindow();
				sleep(4);
				tmsHomePage.switchToMainFrame();
				
				report.startStep("Verify Course Name");
				tmsHomePage.verifyCourseNameInCourseBuilder(newCourseName);
								
				report.startStep("Check default structure of created course");
				verifyDefaultViewNewCourseBasedOn(expCourse, true);
								
				report.startStep("Select course and press on Delete");
				tmsHomePage.checkCourseAndDeleteInCourseBuilder();
				
				report.startStep("Click on Open again");
				tmsNewCourse = tmsHomePage.clickOnOpenInCourseBuilder();
				sleep(2);
				webDriver.switchToNextTab();
				
				report.startStep("Select Version");
				if (iteration==1) tmsNewCourse.selectVersion(oldVersion);
				else tmsNewCourse.selectVersion(newVersion);
				
				report.startStep("Check course not displayed");
				tmsNewCourse.verifyCourseDoesNotExistInDropDown(newCourseName);
				
				report.startStep("Press Cancel - close popup");
				tmsNewCourse.clickOnCancel();
				webDriver.switchToMainWindow();
				
				tmsHomePage.switchToMainFrame();
				sleep(1);
				
			} catch (Exception e) {
				
				pageHelper.deleteCourseByCourseOwnerId(courseId, teacherId);
			
			}
			
				
		iteration++;	
		
		}
		
		
	}
	
	@Test
	@TestCaseParams(testCaseID = {"40811"}, testTimeOut="15")
	public void testSearchCustomComponent() throws Exception {
		
		report.startStep("Login as Teacher");		
			loginPage = new NewUXLoginPage(webDriver, testResultService);	
			instId = autoInstitution.getInstitutionId();
			teacherId = dbService.getUserIdByUserName(autoInstitution.getTeacherUserName(),instId);
			pageHelper.setUserLoginToNull(teacherId);
			sleep(2);
			tmsHomePage = loginPage.enterTeacherUserAndPassword();
			sleep(2);
			tmsHomePage.switchToMainFrame();
		
		report.startStep("Open Course Builder");
			tmsHomePage.clickOnCurriculum();
			sleep(2);
			tmsHomePage.clickOnCourseBuilder();
			sleep(2);
			
		report.startStep("Click on Search tool");
			tmsHomePage.clickOnSearch();
			sleep(2);
			webDriver.switchToTopMostFrame();
			tmsHomePage.switchToSearchFrame();
			tmsHomePage.switchToOptionsFrame();
		
		report.startStep("Find created Custom Component");
			String CustomComponent = "B1 Automation Report";
			tmsHomePage.findCustomComponent(CustomComponent,"Project");
		
		report.startStep("Verify Custom Component in the result list");
			verifyCustomComponentInResult(CustomComponent);
	}
	
	@Test
	@TestCaseParams(testCaseID = {"29331"}, testTimeOut="15")
	public void testSaveCustomComponent() throws Exception {
		
		report.startStep("Login as Teacher");		
			loginPage = new NewUXLoginPage(webDriver, testResultService);	
			instId = autoInstitution.getInstitutionId();
			teacherId = dbService.getUserIdByUserName(autoInstitution.getTeacherUserName(),instId);
			pageHelper.setUserLoginToNull(teacherId);
			String instCompId = dbService.getInstCustomComponentByOwner(instId, teacherId, "Notice");
			sleep(1);
			tmsHomePage = loginPage.enterTeacherUserAndPassword();
			sleep(2);
			tmsHomePage.switchToMainFrame();
		
		report.startStep("Click on Authoring Tool");
			tmsHomePage.clickOnCurriculum();
			sleep(5);
			tmsHomePage.clickOnAuthoringTool();
			sleep(5);
			tmsHomePage.openAuthoringToolInfocard("comp",instCompId);
		
		report.startStep("Verify the Custom Component Title");
			webDriver.switchToNewWindow();
			webDriver.switchToFrame("cmp_details");
			String compTitle = webDriver.waitForElement("cmpTitle", ByTypes.id).getAttribute("value");
			testResultService.assertEquals("Notice", compTitle, "The Component title is wrong");	
			webDriver.switchToTopMostFrame();
			
		report.startStep("Click on Finish");
			webDriver.waitForElement("button1", ByTypes.id).click();
	}
	
	private void verifyDefaultViewNewCourseBasedOn(CourseNew expCourse, boolean isAfterOpen) throws Exception {
		
		String expCourseName = expCourse.getName();
		int unitsCount = expCourse.getCourseUnits().size();
		sleep(3);
		List<String> units = tmsHomePage.getDefaultUnitsInNewCourseOfBuilder();
		testResultService.assertEquals(unitsCount, units.size(), "Number of units in base course is different from number of units displayed");
		
		for (int i = 0; i < units.size(); i++) {
			
			if (isAfterOpen) expCourseName = newCourseName; 
						
			int unitNumber = i+1;
			
			CourseUnitNew unitToTest = expCourse.getCourseUnits().get(i);
			
			testResultService.assertEquals(unitToTest.getName() , units.get(i),"Unit Name for unit: "+unitNumber+" is wrong");
			
			int lessonsCount = unitToTest.getUnitComponents().size();
			
			List<CourseUnitComponentNew> componentsToTest = unitToTest.getUnitComponents();
			List<String[]> components = tmsHomePage.getDefaultComponentsInNewCourseOfBuilder(unitNumber);
		
			report.report("The unit number is: "+ unitNumber);		
			for (int j = 0; j < lessonsCount; j++) {
					
				int lessonNumber = j+1;
				report.report("The Lesson number is: "+ lessonNumber);
	 
				testResultService.assertEquals(expCourseName, components.get(j)[0],"Course Name for unit: "+ unitNumber +" / Lesson: "+lessonNumber+" is wrong");
				testResultService.assertEquals(componentsToTest.get(j).getName().trim(), components.get(j)[2].trim(),"Lesson Name for unit: "+ unitNumber +" / Lesson: "+lessonNumber+" is wrong");
				testResultService.assertEquals(componentsToTest.get(j).getId().trim(), components.get(j)[3].trim(),"Lesson ID for unit: "+ unitNumber +" / Lesson: "+lessonNumber+" is wrong");
				
			}
		}
		
		
		
	}
	
	private void verifyDefaultViewNewCourseNotBasedOn(String expCourseName, JSONArray curriculum, boolean isAfterOpen) throws Exception {
		
		int courseIndex = testData.getJsonObjIndexByKeyValue(curriculum, "CourseName", expCourseName);
		
		JSONObject courseCurriculum = curriculum.getJSONObject(courseIndex);
		JSONArray versionsArr = courseCurriculum.getJSONArray("Versions");
		JSONObject courseVersion = versionsArr.getJSONObject(0);
		JSONArray skillsArr = courseVersion.getJSONArray("Skills");
		
		
		if (isAfterOpen) {
			tmsHomePage.selectComponentCourseInCourseBuilder(1, 1, newCourseName);
		} else {
			tmsHomePage.clickOnNewUnitInCourseBuilder();
			tmsHomePage.enterUnitNameInCourseBuilder(1, "New Unit");
			tmsHomePage.clickOnNewComponentInCourseBuilder(1);
			tmsHomePage.selectComponentCourseInCourseBuilder(1, 1, expCourseName);
		}	
				
		for (int j = 0; j < skillsArr.length(); j++) {
			
			JSONObject skillCurriculum = skillsArr.getJSONObject(j);
			String skillName = skillCurriculum.getString("SkillName");
						
			tmsHomePage.selectComponentSkillInCourseBuilder(1, 1, skillName);
			sleep(1);
			
			JSONArray componentsArr = skillCurriculum.getJSONArray("Components");
			
			for (int i = 0; i < componentsArr.length(); i++) {
				JSONObject componentObj = componentsArr.getJSONObject(i);
				String componentName = componentObj.getString("Name").trim();
				String componentID = String.valueOf(componentObj.getInt("ComponentId"));
				componentName = textService.escapeApostrophes(componentName);
				
				tmsHomePage.selectComponentNameInCourseBuilder(1, 1, componentName);
				String actCompId = webDriver.getSelectedAttributeValueFromComboBox("selectComp1_1");
				testResultService.assertEquals(componentID, actCompId,"Lesson ID for Lesson: "+componentName+" is wrong");
				
			}
			
			
		}
		
	}
		
	private void verifyLessonPreview (int unitNum, int lessonNum) throws Exception {
		
		learningArea = new NewUxLearningArea(webDriver, testResultService);
		tmsHomePage.verifyLessonPreviewInCourseBuilder(unitNum, lessonNum, learningArea);
		tmsHomePage.switchToMainFrame();			
	}
	
	private void verifyLessonPreview2 (int unitNum, int lessonNum) throws Exception {
		
		learningArea2 = new NewUxLearningArea2(webDriver, testResultService);
		tmsHomePage.verifyLessonPreviewInCourseBuilder(unitNum, lessonNum, learningArea2);
		tmsHomePage.switchToMainFrame();			
	}

	private void verifyCustomComponentInResult(String expect_customComponent) throws Exception{
		webDriver.switchToTopMostFrame();
		tmsHomePage.switchToSearchFrame();
		webDriver.switchToFrame("FormFrame");
		
		String actual_customComponent = webDriver.waitForElement("/html/body/table/tbody/tr[1]/td[7]", ByTypes.xpath).getText();
		testResultService.assertEquals(expect_customComponent, actual_customComponent,"Custom Component not match");
	}
	
	
	@After
	public void tearDown() throws Exception {
						
		super.tearDown();
		
	}
	
	
}
