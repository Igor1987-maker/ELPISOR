package tests.edo.newux;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.WebDriver;

import Interfaces.TestCaseParams;
import pageObjects.edo.NewUxLearningArea;
import pageObjects.edo.NewUxMyProgressPage;
import testCategories.unstableTests;
import testCategories.NonAngularLearningArea;
import testCategories.inProgressTests;

@Category(NonAngularLearningArea.class)
public class LessonsListAndUnitLessonNameTests extends BasicNewUxTest {

	NewUxLearningArea learningArea;
	NewUxMyProgressPage myProgress;

	@Before
	public void setup() throws Exception {
		super.setup();
		learningArea = new NewUxLearningArea(webDriver, testResultService);
	}

	@Test
	@TestCaseParams(testCaseID = { "23302" ,"24425"})

	public void testLessonAndUnitNameNavFromLessonsList()
			throws Exception {
		
		report.startStep("Create user and login");
		createUserAndLoginNewUXClass();

		report.startStep("Go to Intermediate 1");
		String courseName = coursesNames[4];
		String courseId = courses[4];
		homePage.navigateToRequiredCourseOnHomePage(courseName);
		sleep(3);

		report.startStep("Navigate to all lessons of new units in Intermediate 1");
		//List<String> units = dbService.getCourseBaseUnits(courseId);
		List<String> units = dbService.getCourseUnits(courseId);
		studentService.setProgress(studentId, courses[4], "23863");
		//check new units instead of full 10 units
		
		for (int i = 8; i < units.size(); i++) {

			int unitIndex = i + 1;
			report.startStep("Open unit " + (i + 1) + " and lesson 1");
			homePage.clickOnUnitLessons(unitIndex);
			homePage.clickOnLesson(i, 1);

			report.startStep("Check lessons");
			
			List<String[]> componentDetails = dbService.getComponentDetailsByUnitId(units.get(i));

			for (int j = 0; j < componentDetails.size(); j++) {

				if (j > 0) {
					int index = j + 1;
					learningArea.openLessonsList();
					sleep(2);
					learningArea.validateLessonListUnitImage(units.get(i));
					learningArea.clickOnLessoneByIndex(index);
				}

				String actualHeader = learningArea.getHeaderTitle();
				String lessonNameInTitle = learningArea.getLessonNameFromHeader();
				System.out.println(actualHeader);
				String expectedUnitName = "Unit " + unitIndex + ": " + dbService.getUnitNameById(units.get(i));

				testResultService.assertEquals(expectedUnitName, actualHeader, "Unit name do not mactch", true);
				String expectedLessonName = dbService.getLessonNameBySkill(componentDetails.get(j)).trim();
				testResultService.assertEquals(expectedLessonName, lessonNameInTitle, "Lesson name do not mactch", true);

			}
			
			//learningArea.clickToOpenNavigationBar();
			learningArea.clickOnHomeButton();
			sleep(1);

		}

	}

	@Test	
	@TestCaseParams(testCaseID = { "23298" }, skippedBrowsers = {"firefox"}) // Actions API not supported by geckodriver)
	public void testLessonAndUnitNameNavFromHomePage()
			throws Exception {
		
		report.startStep("Create user and login");
		createUserAndLoginNewUXClass();

		report.startStep("Go to Basic 1");
		String courseName = coursesNames[1];
		String courseId = courses[1];
		homePage.navigateToRequiredCourseOnHomePage(courseName);
		
		report.startStep("Navigate to old units and lessons in Basic 1");
		List<String> units = dbService.getCourseUnits(courseId);
		//do progress for one item so to back to home on B1 instead of full unit
		studentService.setProgress(studentId, courses[1], "22309");
		//check 1 old unit and 1 new instead of full 10 units
		
		for (int i = 7; i < 9; i++) {
			
			List<String[]> componentDetails = dbService.getComponentDetailsByUnitId(units.get(i));
		
			int unitIndex = i + 1;
			String unitName = dbService.getUnitNameById(units.get(i)).trim();
			sleep(1);

			for (int j = 0; j < componentDetails.size(); j++) {
		
				int lessonIndex = j + 1;
				homePage.clickOnUnitLessons(unitIndex);
				homePage.clickOnLesson(i, lessonIndex);
								
				report.startStep("Check header of Unit:" + unitIndex + " , " + "Lesson:" + lessonIndex);
				String actualHeader = learningArea.getHeaderTitle();

				String lessonNameInTitle = learningArea.getLessonNameFromHeader();
				String expectedUnitName = "Unit " + unitIndex + ": " + unitName;
				testResultService.assertEquals(expectedUnitName, actualHeader, "Unit name do not mactch", true);
											
				String expectedLessonName = dbService.getLessonNameBySkill(componentDetails.get(j)).trim();
				
				if (expectedLessonName.startsWith("Story")) { // exception for new units - remove Story: subject from lesson names
					expectedLessonName = expectedLessonName.replace("Story:", "").trim();
				} else expectedLessonName = expectedLessonName.trim();
				
				testResultService.assertEquals(expectedLessonName, lessonNameInTitle, "Lesson name do not mactch", true);
				
				//learningArea.clickToOpenNavigationBar();
				learningArea.clickOnHomeButton();
				sleep(1);
			}

		}

	}
	
	@Test	
	@TestCaseParams(testCaseID = { "40255" })
	public void testLessonAndUnitNameOnMyProgressPage()
			throws Exception {
		
		report.startStep("Create user and login");
		getUserAndLoginNewUXClass();

		report.startStep("Go to Basic 1 and open My Progress");
		String courseName = coursesNames[1];
		String courseId = courses[1];
		homePage.navigateToRequiredCourseOnHomePage(courseName);
		myProgress = homePage.clickOnMyProgress();	
		sleep(1);
		List<String> units = dbService.getCourseUnits(courseId);
				
		for (int i = 0; i < units.size(); i++) {
			
			List<String[]> componentDetails = dbService.getComponentDetailsByUnitId(units.get(i));
		
			int unitIndex = i + 1;
			String unitName = dbService.getUnitNameById(units.get(i)).trim();
			
			report.startStep("Check lessons names on My Progress page for Unit: " + unitIndex);
			myProgress.clickToOpenUnitLessonsProgress(unitIndex);
			sleep(1);
			
			testResultService.assertEquals("Unit "+ unitIndex, myProgress.getUnitLabel(unitIndex), " Unit label on My progress page is wrong");
			testResultService.assertEquals(unitName, myProgress.getUnitName(unitIndex), " Unit name on My progress page is wrong");
			
			checkLessonsInMyProgressPage(componentDetails);
			
		}

	}
	
	private void checkLessonsInMyProgressPage(List<String[]> components) throws Exception {
		
		for (int i = 0; i < components.size(); i++) {
		
			int lessonNumber = i + 1;

			String actlessonName = myProgress.getLessonName(lessonNumber);
			String expectedLessonName = dbService.getLessonNameBySkill(components.get(i));
			
			if (expectedLessonName.startsWith("Story")) { // exception for new units - remove Story: subject from lesson names
				expectedLessonName = expectedLessonName.replace("Story:", "").trim();
			} else expectedLessonName = expectedLessonName.trim();
			
			
			testResultService.assertEquals(expectedLessonName, actlessonName, "Lesson name do not match", true);
		}

	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

}
