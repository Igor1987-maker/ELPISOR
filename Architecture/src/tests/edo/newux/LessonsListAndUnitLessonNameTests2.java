package tests.edo.newux;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.WebDriver;

import Enums.ByTypes;
import Interfaces.TestCaseParams;
import pageObjects.edo.NewUXLoginPage;
import pageObjects.edo.NewUxHomePage;
import pageObjects.edo.NewUxLearningArea;
import pageObjects.edo.NewUxLearningArea2;
import pageObjects.edo.NewUxMyProgressPage;
import pageObjects.edo.NewUxUnitObjectivesPage;
import testCategories.reg2;
import testCategories.unstableTests;
import testCategories.AngularLearningArea;
import testCategories.inProgressTests;

@Category(AngularLearningArea.class)
public class LessonsListAndUnitLessonNameTests2 extends BasicNewUxTest {

	NewUxLearningArea2 learningArea2;
	NewUxMyProgressPage myProgress;
	NewUxUnitObjectivesPage unitObjPage;

	@Before
	public void setup() throws Exception {
		super.setup();
		
	}

	
	@Test	
	@TestCaseParams(testCaseID = { "23298" }, skippedBrowsers = {"firefox"}) // Actions API not supported by geckodriver)
	public void testLessonAndUnitNameNavFromHomePage()
			throws Exception {
		
		report.startStep("Go to Basic 1");
		getUserAndLoginNewUXClass();
		learningArea2 = new NewUxLearningArea2(webDriver, testResultService);
		String courseName = coursesNames[1];
		String courseId = courses[1];
		homePage.navigateToRequiredCourseOnHomePage(courseName);
		sleep(2);
		
		List<String> units = dbService.getCourseUnits(courseId);
		
		//do progress for one item so to back to home on B1 instead of full unit
		studentService.setProgress(studentId, courses[1], "22309");
				
		for (int i = 7; i < 9; i++) { //check 1 old unit and 1 new instead of full 10 units
			
			List<String[]> componentDetails = dbService.getComponentDetailsByUnitId(units.get(i));
		
			int unitNum = i + 1;
			
			for (int j = 0; j < componentDetails.size(); j++) {
		
				int lessonNum = j + 1;
				
				String expectedLessonName = dbService.getLessonNameBySkill(componentDetails.get(j)).trim();
				expectedLessonName = removeStoryFromLessonName(expectedLessonName);
				
				report.startStep("Enter unit: "+unitNum+" / lesson "+lessonNum+" in Basic 1");
				homePage.clickOnUnitLessons(unitNum);
				homePage.clickOnLesson(i, lessonNum);
				sleep(5);		
				
				String actlessonName = learningArea2.getLessonNameFromHeader();
				String actlessonNum = learningArea2.getLessonNumberFromHeader();
										
				report.startStep("Verify lesson name in header");
				testResultService.assertEquals(expectedLessonName, actlessonName, "Lesson name is not valid", true);
				testResultService.assertEquals(lessonNum, Integer.valueOf(actlessonNum), "Lesson number is not valid", true);
				
				//learningArea2.clickToOpenNavigationBar();
				sleep(2);
				learningArea2.clickOnHomeButton();
				homePage.waitHomePageloaded();
			}

		}

	}
	
	@Test
	@TestCaseParams(testCaseID = { "23302" ,"24425"})

	public void testLessonAndUnitNameNavFromLessonsList()
			throws Exception {
		
		
		report.startStep("Go to Intermediate 1");
		getUserAndLoginNewUXClass();
		learningArea2 = new NewUxLearningArea2(webDriver, testResultService);
		String courseName = coursesNames[4];
		String courseId = courses[4];
		homePage.navigateToRequiredCourseOnHomePage(courseName);
		sleep(5);
			
		List<String> units = dbService.getCourseUnits(courseId);
		
		//do progress for one item so to back to home on I1 instead of full unit
		studentService.setProgress(studentId, courses[4], "23863");
		
		
		for (int i = 8; i < units.size(); i++) { //check new units instead of full 10 units

			int unitNumber = i + 1;
			
			report.startStep("Enter unit " + unitNumber + " / Lesson 1");
			homePage.clickOnUnitLessons(unitNumber);
			homePage.clickOnLesson(i, 1);
			sleep(5);

			report.startStep("Check all lessons in unit " + unitNumber);
			
			List<String[]> componentDetails = dbService.getComponentDetailsByUnitId(units.get(i));

			for (int j = 0; j < componentDetails.size(); j++) {
					
				int lessonNumber = j + 1;

				report.startStep("Check lesson " + lessonNumber);
				
				report.startStep("Open lesson list");
				learningArea2.openLessonsList();
				sleep(2);
					
				report.startStep("Check course name in lesson list");
				String actualCourseName = learningArea2.getCourseNameInLessonList();
				testResultService.assertEquals(courseName, actualCourseName, "Course name is not valid", true);
				
				report.startStep("Check unit title in lesson list");
				String expectedUnitName = "Unit " + unitNumber + ": " + dbService.getUnitNameById(units.get(i));
				String actualUnitName = learningArea2.getUnitTitleInLessonList();
				testResultService.assertEquals(expectedUnitName, actualUnitName, "Unit name is not valid", true);
				
				report.startStep("Check unit image in lesson list");
				learningArea2.validateLessonListUnitImage(units.get(i));
				
				report.startStep("Check lesson name in header");
				String expectedLessonName = dbService.getLessonNameBySkill(componentDetails.get(j)).trim();
				String lessonNameInTitle = learningArea2.getLessonNameFromHeader();
				testResultService.assertEquals(expectedLessonName, lessonNameInTitle, "Lesson name in header is not valid", true);
				
				report.startStep("Check lesson name in list");
				String lessonNameInList = learningArea2.getLessonNameFromListByNumber(lessonNumber);
				testResultService.assertEquals(expectedLessonName, lessonNameInList, "Lesson name in lesson list is not valid", true);
				
				report.startStep("Check lesson number in header and in list");
				String actlessonNumHeader = learningArea2.getLessonNumberFromHeader();
				String actlessonNumList = learningArea2.getLessonNumberFromList(lessonNumber);
				testResultService.assertEquals(actlessonNumHeader, actlessonNumList, "Lesson number in lesson list does not match to header", true);
				
				report.startStep("Check lesson number in header and in list");
				learningArea2.verifyLessonIsCurrentByNumber(lessonNumber);
				
				if (lessonNumber < componentDetails.size()) {
					report.startStep("Click on next lesson " + lessonNumber);
					learningArea2.clickOnLessonByNumber(lessonNumber+1);
					sleep(2);
				}
				
			}
			
			report.startStep("Go to Home Page");
			learningArea2.closeLessonsList();
			sleep(1);
			//homePage.clickToOpenNavigationBar();
			homePage.clickOnHomeButton();
			homePage.waitHomePageloaded();

		}

	}

	@Test	
	@TestCaseParams(testCaseID = { "40255" })
	public void testLessonAndUnitNameOnMyProgressPage() throws Exception {
		
		report.startStep("Go to "+ coursesNames[1] +" and open My Progress");
		getUserAndLoginNewUXClass();
		learningArea2 = new NewUxLearningArea2(webDriver, testResultService);
		String courseName = coursesNames[1];
		String courseId = courses[1];
		
		homePage.navigateToRequiredCourseOnHomePage(courseName);
		myProgress = homePage.clickOnMyProgress();	
		sleep(1);
		
		webDriver.scrollToTopOfPage();
		
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
			String expectedLessonName = dbService.getLessonNameBySkill(components.get(i)).trim();
			expectedLessonName = removeStoryFromLessonName(expectedLessonName);
					
			testResultService.assertEquals(expectedLessonName, actlessonName, "Lesson name do not match", true);
		}

	}

	private String removeStoryFromLessonName(String expectedLessonName) throws Exception {
						
		if (expectedLessonName.startsWith("Story")) { // exception for new units - remove Story: subject from lesson names
			expectedLessonName = expectedLessonName.replace("Story:", "").trim();
		}
		
		return expectedLessonName;
	}


	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

}
