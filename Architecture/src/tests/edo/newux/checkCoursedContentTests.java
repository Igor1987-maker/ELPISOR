package tests.edo.newux;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.WebElement;

import Interfaces.TestCaseParams;
import pageObjects.edo.NewUxLearningArea;
import testCategories.reg1;
import testCategories.reg2;

//19856
@Category(reg2.class)
public class checkCoursedContentTests extends BasicNewUxTest {

	// @Test
	// @TestCaseParams(testCaseID = { "11111" }, testTimeOut = "30")
	// public void testBasicFDA3Courses() throws Exception {
	//
	// report.startStep("Create user");
	// createUserAndLoginNewUXClass();
	// String classId = dbService.getUserClassId(studentId);
	//
	// List<String> courses = dbService.getClassCoursesByClassId(classId);
	//
	// checkCoursesContent(courses);
	// }

	@Before
	public void setup() throws Exception {
		setTestTimeoutInSeconds(30);
		super.setup();
	}

	@Test
	@TestCaseParams(testCaseID = { "22396" }, institutionId = "5232282",ignoreTestTimeout = true)
	public void testFDA3Class() throws Exception {
		checkSpecialCourseByClassName(configuration.getClassName(),configuration.getInstitutionId());
	}

	@Test
	@TestCaseParams(testCaseID = { "22398" }, institutionId = "5232406", ignoreTestTimeout = true)
	public void testESPCourse() throws Exception {

		checkSpecialCourseByClassName(configuration.getProperty("classname.esp"));

	}

	@Test
	@TestCaseParams(testCaseID = { "22400" }, institutionId = "5232406", ignoreTestTimeout = true)
	public void testAviationCourse() throws Exception {
		String className = "classAviation";
		checkSpecialCourseByClassName(className);

	}

	@Test
	@TestCaseParams(testCaseID = { "22401" }, institutionId = "5232406", ignoreTestTimeout = true)
	public void testTourismCourse() throws Exception {
		String className = "classTourism";
		checkSpecialCourseByClassName(className);

	}

	@Test
	@TestCaseParams(testCaseID = { "22402" }, institutionId = "5232406", ignoreTestTimeout = true)
	public void testMedicalCourse() throws Exception {
		String className = "classMedical";
		checkSpecialCourseByClassName(className);

	}

	@Test
	@TestCaseParams(testCaseID = { "22403" }, institutionId = "5232406", ignoreTestTimeout = true)
	public void testAEONCourse() throws Exception {
		String className = "classAEON";
		checkSpecialCourseByClassName(className);

	}

	@Test
	@TestCaseParams(testCaseID = { "22404" }, institutionId = "5232406", ignoreTestTimeout = true)
	public void testBEcomeCourse() throws Exception {
		String className = "classBEcome";
		checkSpecialCourseByClassName(className);

	}

	@Test
	@TestCaseParams(testCaseID = { "22405" }, institutionId = "5232406", ignoreTestTimeout = true)
	public void testBEyondCourse() throws Exception {
		String className = "classBEyond";
		checkSpecialCourseByClassName(className);

	}

	@Test
	@TestCaseParams(testCaseID = { "22408" }, institutionId = "5232406", ignoreTestTimeout = true)
	public void testAtWorkCourse() throws Exception {
		String className = "classAtWork";
		checkSpecialCourseByClassName(className);

	}

	@Test
	@TestCaseParams(testCaseID = { "22409" }, institutionId = "5232406", ignoreTestTimeout = true)
	public void testTeacherTrainingCourse() throws Exception {
		String className = "classTraining";
		checkSpecialCourseByClassName(className);

	}

	@Test
	@TestCaseParams(testCaseID = { "22411" }, institutionId = "5232406", ignoreTestTimeout = true)
	public void testTSkyrocket6Course() throws Exception {
		String className = "classSky";
		checkSpecialCourseByClassName(className);

	}

	@Test
	@TestCaseParams(testCaseID = { "22414" }, institutionId = "5232406", ignoreTestTimeout = true)
	public void testCourseBuilder1UnitCourse() throws Exception {
		String className = "classBuilder1";
		checkSpecialCourseByClassName(className);

	}

	@Test
	@TestCaseParams(testCaseID = { "22416" }, institutionId = "5232406", ignoreTestTimeout = true)
	public void testCourseBuilder15UnitsCourse() throws Exception {
		String className = configuration.getProperty("classname.builder");
		checkSpecialCourseByClassName(className);

	}

	@Test
	@TestCaseParams(testCaseID = { "22417" }, institutionId = "5232406", ignoreTestTimeout = true)
	public void testTMSAuthuringToolCourse() throws Exception {
		String className = "classAT";
		checkSpecialCourseByClassName(className);

	}

	private void checkSpecialCourseByClassName(String className)
			throws Exception {

		checkSpecialCourseByClassName(className, "5232406");
	}

	private void checkSpecialCourseByClassName(String className,
			String institutioNid) throws Exception {
		report.startStep("Create user and login");

		// String className = "classESP";
		String institutionId = institutioNid;
		String classId = dbService.getClassIdByName(className, institutionId);

		studentId = pageHelper.createUSerUsingSP(institutionId, className);

		loginAsStudent(studentId, institutionId);

		report.startStep("Validate courses content");

		List<String> courses = dbService.getClassCoursesByClassId(classId);
		//List<String> courses = dbService.getStudentAssignedCourses(studentId);
		//List<String> courses = dbService.getStudentAssignedCourses(studentId);
		
		checkCoursesContent(courses);
	}

	private void checkCoursesContent(List<String> courses) throws Exception {

		for (int i = 0; i < courses.size(); i++) {
			String courseId = courses.get(i);

			// newUxHomePage.waitForExpectedCourseName(
			// dbService.getCourseNameById(courseId), i + 1);
			sleep(3);

			report.startStep("Check Course units for course: " + courseId);

			List<WebElement> unitsElements = homePage.getUnitsElements();

			List<String[]> units = dbService.getCourseUnitDetils(courseId);
			testResultService.assertEquals(unitsElements.size(), units.size(),
					"Units elements and DB units are not in the same size");

			// create progress
			studentService.setProgressForUnit(units.get(0)[0], courseId,
					studentId, null, 60, 10, false, false);

			report.startStep("Create progress for the next course");
			// studentService.setProgressForUnit(units.get(0)[0], courseId,
			// studentId);
			// int rowCounter = 0;
			for (int j = 0; j < unitsElements.size(); j++) {

				// rowCounter++;
				// if (rowCounter == 4) {
				// rowCounter = 1;
				// }
				int index = j + 1;

				String unitId = homePage.getUnitIdTextByIndex(index);

				testResultService.assertEquals(String.valueOf(index), unitId,
						"Unit id is not displayed");

				String unitName = homePage.getUnitNameByIndex(index);

				testResultService.assertEquals(
						units.get(j)[1].trim().replace("  ", " "),
						unitName.trim(), "Unit name in course: " + courseId
								+ ", unit " + j + ":");

				String style = homePage.getUnitStyleByIndex(index);

				testResultService.assertEquals(
						true,
						style.contains("Runtime/Graphics/Units/U_"
								+ units.get(j)[0] + ".jpg"));

				WebElement element = homePage.getUnitElementByIndex(index);

				List<String[]> components = dbService
						.getComponentDetailsByUnitId(units.get(j)[0]);

				for (int f = 0; f < components.size(); f++) {

					homePage.clickOnUnitLessons(index);

					String expectedLessonText;
					expectedLessonText = dbService
							.getLessonNameBySkill(components.get(f));

					String lessonName = homePage.getLessonText(j, f, courseId);
					if (lessonName != null) {
						lessonName = lessonName.trim();
					}
					if (!testResultService.assertEquals(
							expectedLessonText.trim(), lessonName,
							"Lesson name in course: " + courseId + " , Unit: "
									+ f + ", lesson: " + f)) {
						webDriver.printScreen();
					}

					report.startStep("Click on lesson " + lessonName);
					int lessonIndex = f + 1;
					homePage.clickOnLesson(index - 1, lessonIndex);
					sleep(3);

					report.startStep("Validate unit name and lesson name");
					NewUxLearningArea learningArea = new NewUxLearningArea(
							webDriver, testResultService);
					String currentUnitName = learningArea.getHeaderTitle();
					if (!testResultService.assertEquals("Unit " + index + ": "
							+ unitName, currentUnitName,
							"Unit name nof found/do not match")) {
						webDriver.printScreen("Unit name in learinf area");
					}
					
					report.startStep("Check lesson name in learing area");
					/*
					String currentlessonSequnce = learningArea
							.getLessonSequence();

					if (!testResultService.assertEquals(
							"Lesson " + lessonIndex, currentlessonSequnce,
							"Lesson sequnce do not match")) {
						webDriver.printScreen();
					}
					 */
					String currentLesosnName = learningArea.getLessonName();

					if (!testResultService.assertEquals(lessonName,
							currentLesosnName, "Lesson name do not match")) {
						webDriver.printScreen();
					}
					 
					report.startStep("Check lesson steps");

					List<String[]> subComp = dbService
							.getSubComponentsDetailsByComponentId(components
									.get(f)[1]);

					int currentNumOfSteps = learningArea.getNumberOfSteps();
					testResultService.assertEquals(subComp.size(),
							currentNumOfSteps, "Number of steps do not match",
							true);

					for (int s = 0; s < subComp.size(); s++) {
						String stepName = subComp.get(s)[0];
						String actualStepName = learningArea.getStepName(s + 1);
						actualStepName = actualStepName.replace(". ", "");

						testResultService.assertEquals(stepName,
								actualStepName, "Step name do not match");
						report.startStep("Check  number of tasks");

						List<String[]> expectdTasks = dbService
								.getSubComponentItems(subComp.get(s)[1]);

						//learningArea.getNumberOfTasks() -- Was changed due to incorrect behaviour
						//Doesn't get all the Courses Tasks - Hayk
						int currentNumOfTasks = learningArea.getNumberOfTasks();

						testResultService.assertEquals(expectdTasks.size(),
								currentNumOfTasks,
								"Number of tasks do not match", true);
						if (s < subComp.size() - 1) {
							learningArea.clickOnStep(s + 2);
						}
						report.finishStep();

					}

					report.finishStep();
					//homePage.clickToOpenNavigationBar();
					homePage.clickOnHomeButton();
					// open lessons again and click on next lesson

				}

				// click again to close unit lessons
				// homePage.clickOutsideUnitLessons(index);

			}

			homePage.carouselNavigateNext();
		}

	}

	@Test
	public void testCourseBuilderCourses() throws Exception {

	}

	@After
	public void tearDown() throws Exception {
			super.tearDown();
	}
}
