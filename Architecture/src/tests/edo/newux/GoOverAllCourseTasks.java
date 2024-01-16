package tests.edo.newux;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import Interfaces.TestCaseParams;
import pageObjects.edo.NewUxHomePage;
import pageObjects.edo.NewUxLearningArea;
import testCategories.inProgressTests;
import testCategories.unstableTests;

public class GoOverAllCourseTasks extends BasicNewUxTest {

	@Before
	public void setup() throws Exception {
//		System.setProperty("useProxy", "true");
	//	enableProxy();
	//	enableConsoleLoggin();
		super.setup();
		
	}
	

	public GoOverAllCourseTasks() {
		super();
		// TODO Auto-generated constructor stub
	}


	@Test
	@Category(unstableTests.class)
	@TestCaseParams(testCaseID = { "" }, ignoreTestTimeout = true)
	public void testCourseFD() throws Exception {
		checkCourseForErrors("FD", 1);
	}

	@Test
	@Category(unstableTests.class)
	@TestCaseParams(testCaseID = { "" }, ignoreTestTimeout = true)
	public void testCourseB1() throws Exception {
		checkCourseForErrors("B1", 2);
	}

	@Test
	@Category(unstableTests.class)
	@TestCaseParams(testCaseID = { "" }, ignoreTestTimeout = true)
	public void testCourseB2() throws Exception {
		checkCourseForErrors("B2", 3);
	}
	
	@Test
	@Category(unstableTests.class)
	@TestCaseParams(testCaseID = { "" }, ignoreTestTimeout = true)
	public void testCourseB3() throws Exception {
		checkCourseForErrors("B3", 4);
	}
	
	@Test
	@Category(unstableTests.class)
	@TestCaseParams(testCaseID = { "" }, ignoreTestTimeout = true)
	public void testCourseI1() throws Exception {
		checkCourseForErrors("I1", 5);
	}
	
	@Test
	@Category(unstableTests.class)
	@TestCaseParams(testCaseID = { "" }, ignoreTestTimeout = true)
	public void testCourseI2() throws Exception {
		checkCourseForErrors("I2", 6);
	}
	@Test
	@Category(unstableTests.class)
	@TestCaseParams(testCaseID = { "" }, ignoreTestTimeout = true)
	public void testCourseI3() throws Exception {
		checkCourseForErrors("I3", 6);
	}
	
	@Test
	@Category(unstableTests.class)
	@TestCaseParams(testCaseID = { "" }, ignoreTestTimeout = true)
	public void testCourseA1() throws Exception {
		checkCourseForErrors("A1", 7);
	}
	
	@Test
	@Category(unstableTests.class)
	@TestCaseParams(testCaseID = { "" }, ignoreTestTimeout = true)
	public void testCourseA2() throws Exception {
		checkCourseForErrors("A2", 8);
	}
	
	@Test
	@Category(unstableTests.class)
	@TestCaseParams(testCaseID = { "" }, ignoreTestTimeout = true)
	public void testCourseA3() throws Exception {
		checkCourseForErrors("A3", 9);
	}
	@After
	public void tearDown() throws Exception {
		
		webDriver.printProxyOutputToFile();
		netService.checkForNetworkErrors(webDriver.getHar());
//		webDriver.checkConsoleLogsForErrors();
		super.tearDown();
	}

	private void checkCourseForErrors(String CourseName, int coursePosition)
			throws Exception {
		createUserAndLoginNewUXClass();
		//NewUxHomePage homePage = new NewUxHomePage(webDriver, testResultService); //
	
		homePage.navigateToCourse(coursePosition);

		NewUxLearningArea learningArea = new NewUxLearningArea(webDriver,
				testResultService);

		String courseId = getCourseIdByCourseCode(CourseName);

		List<String[]> units = dbService.getCourseUnitDetils(courseId);

		// create progress
		studentService.setProgressForUnit(units.get(0)[0], courseId, studentId,
				null, 60, 10, false, false);

		for (int i = 0; i < units.size(); i++) {
			
			report.startStep("Go over unit: " + units.get(i)[1]);

			List<String[]> components = dbService
					.getComponentDetailsByUnitId(units.get(i)[0]);

			for (int j = 0; j < components.size(); j++) {
				int unitIndex = i + 1;
				int lessonIndex = j + 1;
				report.startStep("Go over component: " + components.get(j)[0]);

				homePage.clickOnUnitLessons(unitIndex);
				homePage.clickOnLesson(i, lessonIndex);
				sleep(2);
//				webDriver.waitForJqueryToFinish();
				report.startStep("Go over every step");
				List<String[]> subComp = dbService
						.getSubComponentsDetailsByComponentId(components.get(j)[1]);

				boolean isTest = false;
				for (int h = 0; h < subComp.size(); h++) {
					if (h > 0) {
						int stepIndex = h + 1;
						report.startStep("Click on Step: " + h);
//						sleep(4);
						
						learningArea.clickOnStep(stepIndex);
//						webDriver.waitForJqueryToFinish();
						sleep(2);
					}

					// if step is test
					if (subComp.get(h)[0].contains("Test")) {
						learningArea.clickOnStartTest();
//						webDriver.waitForJqueryToFinish();
						sleep(2);
						isTest = true;
					}

					report.startStep("Go over every Task");
					List<String[]> expectdTasks = dbService
							.getSubComponentItems(subComp.get(h)[1]);
					int counter = 0;
					int taskIndex = 1;
					int taskPage = 0;
					report.report("Number of tasks is :"+expectdTasks.size());
					for (int k = 0; k < expectdTasks.size(); k++) {
						
					

						if (k > 0) {
							if (counter == 6) {
								learningArea.clickOnTasksNext();
								counter = 0;
//								taskIndex = 0;
								taskPage++;
							}
							sleep(2);
							if (expectdTasks.size() > 7) {
								learningArea.clickOnTask(taskIndex, taskPage);
							} else {
								learningArea.clickOnTask(taskIndex);
							}
							webDriver.waitForJqueryToFinish();
							counter++;
							taskIndex++;
						}
					}

				}
				
				report.finishStep();
				report.startStep("Go back to the home page");
				learningArea.clickToOpenNavigationBar();
				learningArea.clickOnHomeButton();
				if (isTest) {
					learningArea.approveTest();
				}

			}

		}
		

	}
	

}
