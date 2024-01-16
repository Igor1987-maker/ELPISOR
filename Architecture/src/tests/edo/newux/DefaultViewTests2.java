package tests.edo.newux;

import java.util.List;

import org.junit.After;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import pageObjects.edo.NewUxHomePage;
import pageObjects.edo.NewUxLearningArea;
import pageObjects.edo.NewUxLearningArea2;
import testCategories.AngularLearningArea;
import testCategories.NonAngularLearningArea;
import testCategories.inProgressTests;
import testCategories.unstableTests;
import Interfaces.TestCaseParams;

@Category(AngularLearningArea.class)
public class DefaultViewTests2 extends BasicNewUxTest {

	
	NewUxLearningArea2 learningArea2;
		
	@Test
	@TestCaseParams(testCaseID = { "19875" })
	public void testCompletionFirstUnitInLessonFD() throws Exception {

		testCompletionFirstLessonInUnitByCourseId(specialCoursesData.get(0)[1],
				specialCoursesData.get(0)[0], configuration.getInstitutionId());

	}

	@Test
	@TestCaseParams(testCaseID = { "19875" })
	public void testCompletionFirstUnitInLessonBasic1() throws Exception {

		testCompletionFirstLessonInUnitByCourseId(specialCoursesData.get(1)[1],
				specialCoursesData.get(1)[0], configuration.getInstitutionId());

	}

	@Test
	@TestCaseParams(testCaseID = { "19875" })
	public void testCompletionFirstUnitInLessonEnrichedCourse()
			throws Exception {

		institutionName=institutionsName[1];
		pageHelper.initializeData();
		   
        String url = webDriver.getUrl();
        url = url.split(".com")[0] + ".com/" + BasicNewUxTest.institutionName;
       
        webDriver.quitBrowser();
        webDriver.init();
        webDriver.maximize();
        webDriver.openUrl(url);
        
		testCompletionFirstLessonInUnitByCourseId(specialCoursesData.get(2)[1],
				specialCoursesData.get(2)[0], institutionId);

	}

	@Test
	@TestCaseParams(testCaseID = { "19875" })
	public void testCompletionFirstUnitInLessonCourseBuilder() throws Exception {

		institutionName=institutionsName[1];
		pageHelper.initializeData();
		   
        String url = webDriver.getUrl();
        url = url.split(".com")[0] + ".com/" + BasicNewUxTest.institutionName;
       
        webDriver.quitBrowser();
        webDriver.init();
        webDriver.maximize();
        webDriver.openUrl(url);
        
		testCompletionFirstLessonInUnitByCourseId(specialCoursesData.get(3)[1],
				specialCoursesData.get(3)[0], institutionId);

	}

	@Test
	@TestCaseParams(testCaseID = { "19875" })
	public void testCompletionFirstUnitInLessonESP() throws Exception {

		institutionName=institutionsName[1];
		pageHelper.initializeData();
		   
        String url = webDriver.getUrl();
        url = url.split(".com")[0] + ".com/" + BasicNewUxTest.institutionName;
       
        webDriver.quitBrowser();
        webDriver.init();
        webDriver.maximize();
        webDriver.openUrl(url);
        
		testCompletionFirstLessonInUnitByCourseId(specialCoursesData.get(4)[1], specialCoursesData.get(4)[0], institutionId);

	}

	private void testCompletionFirstLessonInUnitByCourseId(String courseId,
			String className, String institutionId) throws Exception {
		
		report.startStep("Login as student");
		int unitIndex = 0;
		int lessonIndex = 1;
		
		createUserAndLoginNewUXClass(className, institutionId);
		homePage.closeAllNotifications();
		homePage.waitHomePageloadedFully();
		
		report.startStep("Go to the learning area");
		String courseName = dbService.getCourseNameById(courseId);
		homePage.navigateToRequiredCourseOnHomePage(courseName);
		learningArea2 = homePage.clickOnContinueButton2();
		learningArea2.waitToLearningAreaLoaded();
		
		report.startStep("Simulate progress");
		String unitId = dbService.getCourseUnitDetils(courseId).get(unitIndex)[0];
		String componentId = dbService.getComponentDetailsByUnitId(unitId).get(lessonIndex - 1)[1];
		studentService.setProgressForComponents(componentId, courseId, studentId, null, 60, 0, true, true);

		report.startStep("Go back to the course area and check the unit and lesson name");
		//homePage.clickToOpenNavigationBar();
		sleep(1);
		homePage.clickOnHomeButton();
		homePage.waitHomePageloadedFully();
		
		String currentCourseName = homePage.getCourseName().trim();
		String nextUnitName = null;
		String lessonName = null;
		String expectedUnitName = null;
		String expectedLessonName=null;
		if (dbService.getCourseUnitDetils(courseId).size() > 1) {
			nextUnitName = homePage.getUnitName();
			lessonName = homePage.getNextLesosnName();
			int unitNumber = unitIndex + 1;
			expectedUnitName = "Unit " + unitNumber + ": "
					+ dbService.getUnitNameById(unitId).trim();
			componentId = dbService.getComponentDetailsByUnitId(unitId).get(
					lessonIndex)[1];

			 expectedLessonName = dbService
					.getLessonNameByCourseUnitAndLessonNumber(courseId, unitIndex,
							lessonIndex + 1);
		}

		String expectedCourseName = dbService.getCourseNameById(courseId).trim();

		testResultService.assertEquals(expectedCourseName, currentCourseName,
				"Course name not found");
		if (dbService.getCourseUnitDetils(courseId).size() > 1) {
			testResultService.assertEquals(expectedUnitName, nextUnitName,
					"Unit name not found");
			testResultService.assertEquals(expectedLessonName, lessonName,
					"Lesson name not found");
		}
		
	}

	@Test
	@TestCaseParams(testCaseID = { "19876" })
	public void testCompletionOfLastLessonInUnit() throws Exception {
		report.startStep("Login as student");
		String courseCode = "I2";
		int unitIndex = 5;

		String courseId = getCourseIdByCourseCode(courseCode);
		String unitId = dbService.getCourseUnitDetils(courseId).get(
				unitIndex - 1)[0];
		NewUxHomePage newUxHomePage = createUserAndLoginNewUXClass();
		homePage.closeAllNotifications();
		homePage.waitHomePageloaded();
		report.finishStep();

		report.startStep("Go to the learning area");
		String courseName = dbService.getCourseNameById(courses[newUxHomePage
				.getCourseIndexByCode(courseCodes, courseCode)]);
		NewUxLearningArea learningArea = newUxHomePage
				.navigateToCourseUnitAndLesson(courseCodes, courseCode,
						unitIndex, 5);
		report.finishStep();

		report.startStep("Set progress to all the unit's lessons");
		studentService.setProgressForUnit(unitId, courseId, studentId);
		report.finishStep();
		sleep(2);
		
		report.startStep("Check that the next unit, 1st lessson is displayed in the title");
		//learningArea.clickToOpenNavigationBar();
		newUxHomePage.clickOnHomeButton();
		homePage.waitHomePageloaded();
		
		String currentCourseName = newUxHomePage.getCourseName().trim();
		String nextUnitName = newUxHomePage.getUnitName();
		String lessonName = newUxHomePage.getNextLesosnName();

		String expectedCourseName = dbService.getCourseNameById(courseId)
				.trim();
		// unitIndex = unitIndex + 1;
		unitId = dbService.getCourseUnitDetils(courseId).get(unitIndex)[0];
		unitIndex = unitIndex + 1;
		String expectedUnitName = "Unit " + unitIndex + ": "
				+ dbService.getUnitNameById(unitId);

		String componentId = dbService.getComponentDetailsByUnitId(unitId).get(
				0)[1];

		String expectedLessonName = dbService.getComponentNameById(componentId);

		testResultService.assertEquals(expectedCourseName, currentCourseName,
				"Course name not found");
		testResultService.assertEquals(expectedUnitName, nextUnitName,
				"Unit name not found");
		testResultService.assertEquals(expectedLessonName, lessonName,
				"Lesson name not found");

		report.finishStep();

	}

	@Test
	@Category(unstableTests.class)
	@TestCaseParams(testCaseID = { "21696" })
	public void testCourseCarouselLastProgressInDifferentUnits()
			throws Exception {
		String courseCode = "I2";
		report.startStep("Create user and login");

		createUserAndLoginNewUXClass();
		homePage.closeAllNotifications();
		homePage.waitHomePageloadedFully();
	
		report.startStep("Navigate to course I1, unit 2");
		//String courseName = dbService.getCourseNameById(courses[homePage
		//		.getCourseIndexByCode(courseCodes, courseCode)]);
		learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, courseCode, 2,1);
		learningArea2.waitToLearningAreaLoaded();
		
		report.startStep("Create progress for 50% of unit 2");
		String courseId = getCourseIdByCourseCode(courseCode);
		String courseUnit = dbService.getCourseUnits(courseId).get(1);
		studentService.setProgressForUnit(courseUnit, courseId, studentId,
				null, 60, 8, false, false);

		report.startStep("Navigate back to the home page");
		//homePage.clickToOpenNavigationBar();
		homePage.clickOnHomeButton();
		homePage.waitHomePageloadedFully();
		
		report.startStep("Check that unit 2 is displayed");
		String currentUnitName = homePage.getUnitName().trim();
		String expectedUnitName = dbService.getUnitNameById(courseUnit).trim();
		testResultService.assertEquals("Unit 2: " + expectedUnitName,
				currentUnitName, "Unit name is not as expected");

		report.startStep("Navigate to course I1, unit 4");
		homePage.clickOnUnitLessons(4);
		homePage.clickOnLesson(3, 2);

		report.startStep("Create progress for 50% of unit 4");
		courseUnit = dbService.getCourseUnits(courseId).get(3);
		studentService.setProgressForUnit(courseUnit, courseId, studentId,
				null, 60, 8, false, false);

		report.startStep("Navigate back to the home page");
		//homePage.clickToOpenNavigationBar();
		homePage.clickOnHomeButton();
		homePage.waitHomePageloaded();
		
		currentUnitName = homePage.getUnitName().trim();
		expectedUnitName = dbService.getUnitNameById(courseUnit).trim();

		report.startStep("Check that unit 4 is displayed");
		testResultService.assertEquals("Unit 4: " + expectedUnitName,
				currentUnitName, "Unit name is not as expected");

		report.startStep("Navigate to course I1, unit 8");
		homePage.clickOnUnitLessons(6);
		homePage.clickOnLesson(5, 2);

		report.startStep("Create progress for 50% of unit 8");

		courseUnit = dbService.getCourseUnits(courseId).get(7);
		studentService.setProgressForUnit(courseUnit, courseId, studentId,
				null, 60, 8, false, false);

		report.startStep("Navigate back to the home page");
		//homePage.clickToOpenNavigationBar();
		homePage.clickOnHomeButton();
		homePage.waitHomePageloaded();
		
		report.startStep("Check that unit 8 is displayed");

		currentUnitName = homePage.getUnitName().trim();
		expectedUnitName = dbService.getUnitNameById(courseUnit).trim();

		report.startStep("Check that unit 8 is displayed");
		testResultService.assertEquals("Unit 8: " + expectedUnitName,
				currentUnitName, "Unit name is not as expected");
	}

	@Test
	@TestCaseParams(testCaseID = { "21690" })
	public void testLockedUnitFirstUnitInCourse() throws Exception {
		String className = configuration.getProperty("classname.locked");
		String courseId = courses[0];
		try {
			report.startStep("Create new student");

			String studentId = pageHelper.createUSerUsingSP(
					configuration.getInstitutionId(), className);

			report.startStep("Lock 1st unit of 1st course");
			pageHelper.LockUnitToClass(className, courseId, 1);

			report.startStep("Login");
			loginAsStudent(studentId);
			homePage.closeAllNotifications();
			
			String expectedCourseName = dbService.getCourseNameById(courseId);

			String unitId = dbService.getCourseUnits(courseId).get(1);
			String expectedUnitName = dbService.getUnitNameById(unitId).trim();
			expectedUnitName = expectedUnitName.replace("  ", " ");

			String componentId = dbService.getComponentDetailsByUnitId(unitId)
					.get(0)[1];

			String expectedLessonName = dbService
					.getComponentNameById(componentId);

			String currentCourseName = homePage.getCourseName().trim();
			String nextUnitName = homePage.getUnitName().trim();
			String lessonName = homePage.getNextLesosnName();

			testResultService.assertEquals(expectedCourseName,
					currentCourseName, "Course name not found");
			testResultService.assertEquals("Unit 2: " + expectedUnitName,
					nextUnitName, "Unit name not found");
			testResultService.assertEquals(expectedLessonName, lessonName,
					"Lesson name not found");

			report.startStep("Click on continue button");
			homePage.clickOnContinueButton();
			sleep(1);
					
			report.startStep("Go back to the home page");
			//homePage.clickToOpenNavigationBar();
			homePage.clickOnHomeButton();
			homePage.waitHomePageloadedFully();
			
			currentCourseName = homePage.getCourseName().trim();
			nextUnitName = homePage.getUnitName().trim();
			lessonName = homePage.getNextLesosnName();
			report.startStep("Go back to the home page");
			testResultService.assertEquals(expectedCourseName,
					currentCourseName, "Course name not found");
			testResultService.assertEquals("Unit 2: " + expectedUnitName,
					nextUnitName, "Unit name not found");
			testResultService.assertEquals(expectedLessonName, lessonName,
					"Lesson name not found");

		} catch (Exception e) {
			// TODO Auto-generated catch block

		} finally {
			pageHelper.UnlockCourseUnitLessonsToClass(className, courseId, 1);
		}
	}

	@Test
	@TestCaseParams(testCaseID = { "21692" })
	public void testLockedCourseFirstCourse() throws Exception {
		String className = configuration.getProperty("classname.locked");
		String lockedCourseId = courses[0];

		String firstCourseId = courses[1];

		try {
			report.startStep("Lock the First discoveries course");

			pageHelper.LockCourseToClass(className, lockedCourseId);

			report.startStep("Create user and login");

			createUserAndLoginNewUXClass(className);

			report.startStep("Check the default course, unit and lesson");

			String currentCourseName = homePage.getCourseName().trim();
			String nextUnitName = homePage.getUnitName().trim();
			String lessonName = homePage.getNextLesosnName();

			String expectedCourseName = dbService
					.getCourseNameById(firstCourseId);

			String unitId = dbService.getCourseUnits(firstCourseId).get(0);
			String expectedUnitName = dbService.getUnitNameById(unitId).trim();
			expectedUnitName = expectedUnitName.replace("  ", " ");

			String componentId = dbService.getComponentDetailsByUnitId(unitId)
					.get(0)[1];

			String expectedLessonName = dbService
					.getComponentNameById(componentId);

			testResultService.assertEquals(expectedCourseName.trim(),
					currentCourseName.trim(), "Course name not found");
			testResultService.assertEquals("Unit 1: " + expectedUnitName,
					nextUnitName, "Unit name not found");
			testResultService.assertEquals(expectedLessonName, lessonName,
					"Lesson name not found");

			report.startStep("Click the continue button");
			homePage.clickOnContinueButton();
			sleep(1);

			report.startStep("Check the Learning area course name");
			//NewUxLearningArea learningArea = new NewUxLearningArea(webDriver,testResultService);


			report.startStep("Go back to the learning area");
			//homePage.clickToOpenNavigationBar();
			homePage.clickOnHomeButton();
			homePage.waitHomePageloaded();
			
			report.startStep("Check again the course, unit and lesson names");

			currentCourseName=null;
			for (int i=0;i<2 && currentCourseName==null;i++){
				currentCourseName = homePage.getCourseName().trim();	
			}
			nextUnitName = homePage.getUnitName().trim();
			lessonName = homePage.getNextLesosnName();

			testResultService.assertEquals(expectedCourseName.trim(),
					currentCourseName.trim(), "Course name not found");
			testResultService.assertEquals("Unit 1: " + expectedUnitName,
					nextUnitName, "Unit name not found");
			testResultService.assertEquals(expectedLessonName, lessonName,
					"Lesson name not found");

		} catch (Exception e) {
			// TODO Auto-generated catch block

		} finally {
			pageHelper.UnlockCourseUnitLessonsToClass(className,
					lockedCourseId, 1);
		}

	}

	@Test
	@TestCaseParams(testCaseID = { "21694" })
	public void testLastProgressSimpleFlowFD() throws Exception {
		testLastProgressSimpleFlowByCourseId(specialCoursesData.get(0)[1],
				specialCoursesData.get(0)[0], configuration.getInstitutionId(),
				1, 1);

	}

	@Test
	@TestCaseParams(testCaseID = { "21694" })
	public void testLastProgressSimpleFlowB1() throws Exception {
		testLastProgressSimpleFlowByCourseId(specialCoursesData.get(1)[1],
				specialCoursesData.get(1)[0], institutionId,
				1, 1);

	}

	@Test
	@TestCaseParams(testCaseID = { "21694" })//, institutionId = coursesInstId)
	public void testLastProgressSimpleFlowCourseEnriched() throws Exception {
		
		institutionName=institutionsName[1];
		pageHelper.initializeData();
		   
        String url = webDriver.getUrl();
        url = url.split(".com")[0] + ".com/" + BasicNewUxTest.institutionName;
       
        webDriver.quitBrowser();
        webDriver.init();
        webDriver.maximize();
        webDriver.openUrl(url);
        
		testLastProgressSimpleFlowByCourseId(specialCoursesData.get(2)[1],specialCoursesData.get(2)[0], institutionId, 0, 0);

	}

	@Test
	@TestCaseParams(testCaseID = { "21694" } )//, institutionId = coursesInstId)
	public void testLastProgressSimpleFlowCourseBuilder() throws Exception {
		
		institutionName=institutionsName[1];
		pageHelper.initializeData();
		   
        String url = webDriver.getUrl();
        url = url.split(".com")[0] + ".com/" + BasicNewUxTest.institutionName;
       
        webDriver.quitBrowser();
        webDriver.init();
        webDriver.maximize();
        webDriver.openUrl(url);
        
		testLastProgressSimpleFlowByCourseId(specialCoursesData.get(3)[1],
				specialCoursesData.get(3)[0], institutionId, 0, 0);

	}

	@Test
	@TestCaseParams(testCaseID = { "21694" })//, institutionId = coursesInstId)
	public void testLastProgressSimpleFlowESP() throws Exception {
	
		institutionName=institutionsName[1];
		pageHelper.initializeData();
	
		pageHelper.restartBrowserInNewURL(institutionName, true);
        /*
		String url = webDriver.getUrl();
        url = url.split(".com")[0] + ".com/" + BasicNewUxTest.institutionName;
       
        webDriver.quitBrowser();
        webDriver.init();
        webDriver.maximize();
        webDriver.openUrl(url);
        */
		testLastProgressSimpleFlowByCourseId(specialCoursesData.get(4)[1],specialCoursesData.get(4)[0], institutionId, 0, 1);

	}

	private void testLastProgressSimpleFlowByCourseId(String courseId,
			String className, String institutionId, int unitIndex,
			int lessonIndex) throws Exception {

		String unitId = dbService.getCourseUnits(courseId).get(unitIndex);
		String componentId = dbService.getComponentDetailsByUnitId(unitId).get(
				lessonIndex)[1];
		String subComponentid = dbService.getSubComponentsDetailsByComponentId(
				componentId).get(0)[1];
		String itemId = dbService.getSubComponentItems(subComponentid).get(0)[0];

		report.startStep("create user");


		String studentId = pageHelper.createUSerUsingSP(institutionId,
				className);

		report.startStep("set progress ");
		studentService.createSingleProgressRecored(studentId, courseId, itemId,
				false, 1);

		report.startStep("Login with user created");

		homePage = loginAsStudent(studentId, institutionId);
		homePage.closeAllNotifications();
		homePage.waitHomePageloadedFully();
		
		report.startStep("CourseArea prompt the 2nd lesson of the Unit 2 of the B1 course");

		String expectedCourseName = dbService.getCourseNameById(courseId);
		String expectedUnitName = dbService.getUnitNameById(unitId)
				.replace("  ", " ").trim();
		String expectedLessonName = dbService.getComponentNameById(componentId);

		String currentCourseName = homePage.getCourseName().trim();
		String nextUnitName = homePage.getUnitName().trim();
		String lessonName = homePage.getNextLesosnName();
		
		
		testResultService.assertEquals(expectedCourseName.trim(),currentCourseName.trim(), "Course name: " + expectedCourseName.trim() + "not found");
		int unitNumber = unitIndex + 1;
		
		testResultService.assertEquals("Unit " + unitNumber + ": "
				+ expectedUnitName, nextUnitName, "Unit name: " + expectedUnitName + "not found");
		
		testResultService.assertEquals(expectedLessonName, lessonName,
				"Lesson name: " + expectedLessonName + "not found");
	}

	@Test
	@TestCaseParams(testCaseID = { "21688" })
	public void testDefaultViewLockedLessonFirstLessonInUnit() throws Exception {

		report.startStep("Creat new student & lock the 1st lesson of the 1st unit of the 1st course ");

		String className = configuration.getProperty("classname.locked");
		String studentId = pageHelper.createUSerUsingSP(configuration.getInstitutionId(), className);
		String courseId = courses[0];

		String unitId = dbService.getCourseUnits(courseId).get(0);
		String componentId = dbService.getComponentDetailsByUnitId(unitId).get(1)[1];

		try {
			pageHelper.LockLessonToClass(className, courseId, 1, 1);
			
			report.startStep("Login");
			homePage = loginAsStudent(studentId);

			report.startStep("Verify course area titles");
			String currentCourseName = homePage.getCourseName().trim();
			String currentUnitName = homePage.getUnitName().trim();
			String currentLessonName = homePage.getNextLesosnName();

			String expectedCourseName = dbService.getCourseNameById(courseId);
			String expectedUnitName = dbService.getUnitNameById(unitId);
			String expectedLessonName = dbService.getComponentNameById(componentId);

			testResultService.assertEquals(expectedCourseName.trim(),currentCourseName.trim(), "Course name not found");
			testResultService.assertEquals("Unit 1: " + expectedUnitName.trim(), currentUnitName,"Unit name not found");
			testResultService.assertEquals(expectedLessonName, currentLessonName,"Lesson name not found");

			
			report.startStep("Click Continue and verify landing on correct Unit - Lesson in Learning Area");
			learningArea2 = homePage.clickOnContinueButton2();
			String actualLessonName = learningArea2.getLessonNameFromHeader();
			learningArea2.openLessonsList();
			String actualUnitName = learningArea2.getUnitTitleInLessonList();
			
			testResultService.assertEquals(currentUnitName, actualUnitName, "Unit name not found");
			testResultService.assertEquals(currentLessonName, actualLessonName, "Lesson name not found");
		
		} catch (Exception e) {
			// TODO Auto-generated catch block

		} finally {
			pageHelper.UnlockCourseUnitLessonsToClass(className, courseId, 1);
		}

	}

	@Test
	@TestCaseParams(testCaseID = { "21697" })
	public void testDefaultViewLastProgressDifferentCourses() throws Exception {
		String courseId = courses[3];
		String unitId = dbService.getCourseUnits(courseId).get(2);

		report.startStep("Create student and login");
		createUserAndLoginNewUXClass();
		homePage.closeAllNotifications();
		homePage.waitHomePageloadedFully();
		
		report.startStep("Navigate to course B3,unit 2, lesson 3");
		learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "B3", 2, 3);
		learningArea2.waitToLearningAreaLoaded();
		
		report.startStep("Set progress in Course B3");
		studentService.setProgressForUnit(unitId, courseId, studentId);
		//homePage.clickToOpenNavigationBar();
		homePage.clickOnHomeButton();
		homePage.waitHomePageloadedFully();
		
		String currentCourseName = homePage.getCourseName().trim();

		String expectedCourseName = dbService.getCourseNameById(courseId)
				.trim();

		if (!testResultService.assertEquals(expectedCourseName,
				currentCourseName, "Correct course name not found")) {
			webDriver.printScreen("Course name not found");
		}

	}
	
	@After
	public void tearDown() throws Exception {
		institutionName="";
		super.tearDown();
	}
}
