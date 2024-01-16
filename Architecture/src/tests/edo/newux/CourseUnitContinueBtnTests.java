package tests.edo.newux;

import org.junit.After;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.WebElement;

import Enums.ByTypes;
import Interfaces.TestCaseParams;
import pageObjects.edo.NewUxLearningArea;
import testCategories.NonAngularLearningArea;
import testCategories.inProgressTests;
import testCategories.edoNewUX.HomePage;

@Category(NonAngularLearningArea.class)
public class CourseUnitContinueBtnTests extends BasicNewUxTest {

	
	@Test
	@Category(HomePage.class)
	@TestCaseParams(testCaseID = { "21849" }, testMultiple = true)
	public void CourseStartBtn() throws Exception {

		report.startStep("Create new student and login");
		homePage = createUserAndLoginNewUXClass();
		report.finishStep();
		sleep(3);

		report.startStep("Get Unit / Lesson Titles in Course Area");
		String UnitTitleOnHomePage = homePage.getUnitTitleInCourseArea();
		String LessonTitleOnHomePage = homePage
				.getLessonTitleInCourseArea();

		report.startStep("Check and Press Start btn");
		homePage.checkTextandClickOnCourseBtn("Start");

		report.startStep("Get Unit / Lesson Titles in Learning Area");
		NewUxLearningArea learningArea = new NewUxLearningArea(webDriver,
				testResultService);
		String UnitTitleOnLearningArea = learningArea.getHeaderTitle();
		// String LessonTitleOnLearningArea = learningArea.getLessonName();

		report.startStep("Verify Navigation To Correct Lesson");
		testResultService.assertEquals(UnitTitleOnHomePage,
				UnitTitleOnLearningArea);
		// testResultService.assertEquals(LessonTitleOnHomePage,
		// LessonTitleOnLearningArea);

		report.startStep("Verify 1st Step Open");
		learningArea.checkFirstStepOpened();

	}

	@Test
	@Category(HomePage.class)
	@TestCaseParams(testCaseID = { "19984" }, testMultiple = true)
	public void CourseContinueBtnFD() throws Exception {

		testCourseContinueButtonByCourseId(courses[0],
				configuration.getInstitutionId(), configuration.getClassName());

	}

	@Test
	@Category(HomePage.class)
	@TestCaseParams(testCaseID = { "19984" }, testMultiple = true)
	public void CourseContinueBtnB1() throws Exception {

		testCourseContinueButtonByCourseId(courses[1],
				configuration.getInstitutionId(), configuration.getClassName());

	}

	@Test
	@Category(HomePage.class)
	@TestCaseParams(testCaseID = { "19984" }, testMultiple = true)
	public void CourseContinueBtnESP() throws Exception {

		testCourseContinueButtonByCourseId(specialCoursesData.get(4)[1],
				institutionId, specialCoursesData.get(4)[0]);

	}

	@Test
	@Category(HomePage.class)
	@TestCaseParams(testCaseID = { "19984" }, testMultiple = true)
	public void CourseContinueBtnCourseBuilder() throws Exception {

		testCourseContinueButtonByCourseId(specialCoursesIds[3], institutionId, configuration.getProperty("classname.builder"));

	}

	@Test
	@Category(HomePage.class)
	@TestCaseParams(testCaseID = { "19984" }, testMultiple = true)
	public void CourseContinueBtnCourseEnriched() throws Exception {

		testCourseContinueButtonByCourseId(specialCoursesIds[2], institutionId, configuration.getProperty("classname.enrich"));

	}

	private void testCourseContinueButtonByCourseId(String courseId,
			String institutionId, String className) throws Exception {
		
		report.startStep("Get Course name by CourseId");
		String courseName = dbService.getCourseNameById(courseId).trim();

		report.startStep("Create new student and login");
		homePage = createUserAndLoginNewUXClass(className, institutionId);
		
		report.startStep("Enter Learning Area");
		homePage.checkTextandClickOnCourseBtn("Start");

		report.startStep("Set progress in B1 and return to HP");
		studentService.setProgressInFirstSubcomponentInComponent(0,0,courseId,studentId);
		//homePage.clickToOpenNavigationBar();
		sleep(1);
		homePage.clickOnHomeButton();

		report.startStep("Check Course Area Titles");
		String courseTitle = homePage.getCourseName();
		testResultService.assertEquals(courseName, courseTitle);
		String unitTitle = homePage.getUnitTitleInCourseArea();
		//String lessonTitle = newUxHomePage.getLessonTitleInCourseArea();

		report.startStep("Check and Press Continue btn");
		homePage.checkTextandClickOnCourseBtn("Continue");

		report.startStep("Verify Navigation To Correct Lesson");
		NewUxLearningArea learningArea = new NewUxLearningArea(webDriver,
				testResultService);
		String UnitTitleOnLearningArea = learningArea.getHeaderTitle();

		testResultService.assertEquals(unitTitle, UnitTitleOnLearningArea);


		report.startStep("Verify 1st Step Open");
		learningArea.checkFirstStepOpened();
	}

	@Test
	@Category(HomePage.class)
	@TestCaseParams(testCaseID = { "21850" }, testMultiple = true)
	public void CourseStartOverBtn() throws Exception {

		report.startStep("Create new student and login");
		homePage = createUserAndLoginNewUXClass();
		report.finishStep();

		report.startStep("Enter Learning Area");
		homePage.checkTextandClickOnCourseBtn("Start");

		report.startStep("Set progress 100% in FD and return to HP");
		studentService.setProgressForCourse(courses[0], studentId, null, 60);
		//homePage.clickToOpenNavigationBar();
		homePage.clickOnHomeButton();

		report.startStep("Navigate back to FD");
		homePage.carouselNavigateBack();
		sleep(3);

		report.startStep("Check and Press Start Over btn");
		homePage.checkTextandClickOnCourseBtn("Start Over");

		report.startStep("Verify Navigation To Correct Lesson");
		NewUxLearningArea learningArea = new NewUxLearningArea(webDriver,
				testResultService);
		String UnitTitleOnLearningArea = learningArea.getHeaderTitle();
		// String LessonTitleOnLearningArea = learningArea.getLessonName();

		testResultService.assertEquals("Unit 1: Introduction",
				UnitTitleOnLearningArea);
		// testResultService.assertEquals("Letters: A-L",
		// LessonTitleOnLearningArea);

		report.startStep("Verify 1st Step Open");
		learningArea.checkFirstStepOpened();

	}

	@Test
	@Category(HomePage.class)
	@TestCaseParams(testCaseID = { "21895" }, testMultiple = true, skippedBrowsers = {"firefox"}) // Actions API not supported by geckodriver)
	public void testUnitStartButton() throws Exception {
		webDriver.maximize();
		NewUxLearningArea learningArea = new NewUxLearningArea(webDriver,
				testResultService);

		report.startStep("Create new student and login");
		homePage = createUserAndLoginNewUXClass();
		
		report.startStep("Get Last Progress Unit Title");
		String unitTitleHP = homePage.getUnitTitleInCourseArea();

		report.startStep("Mouse hover on Unit 1, check Btn Label");
		homePage.scrollToUnitElement(1);

		WebElement startButton = homePage.hoverOnUnitAndGetBtnElement(1,true);
		String btnColor = startButton.getCssValue("background-color");
		testResultService.assertEquals("rgba(39, 174, 97, 1)", btnColor, "Button color is not valid before hover");
		
		String actualLabel = startButton.getText();
		testResultService.assertEquals("Start", actualLabel);
		
		/*report.startStep("Mouse hover on Unit Image & check Unit btn highlighted");
		startButton = newUxHomePage.hoverOnUnitImageAndGetBtnElement(1,true);
		btnColor = startButton.getCssValue("background-color");
		testResultService.assertEquals("rgba(57, 207, 96, 1)", btnColor, "Button color is not valid after hover");*/
				
		report.startStep("Click on Start btn");
		startButton.click();
		sleep(1);
		
		
		report.startStep("Versify LA opened on correct Unit");
		String unitTitleLA = learningArea.getHeaderTitle();
		testResultService.assertEquals(unitTitleHP, unitTitleLA);

		report.startStep("Click Home Btn");
		//learningArea.clickToOpenNavigationBar();
		learningArea.clickOnHomeButton();

		report.startStep("Mouse hover on unit title in unit block & click on this area");
		WebElement unitTitleInUnitsGrid = homePage.hoverOnUnitTitleInUnitBlockAndGetElement(1);
		unitTitleInUnitsGrid.click();

		report.startStep("Check lesson list opened");
		for (int i = 0; i < 7; i++) {
			int index = i + 1;
			homePage.getLessonElement(0, index);
		}
		
		report.startStep("Mouse hover on other Units and check Unit Btn Not Displayed");
		homePage.checkUnitBtnNotDisplayed(2, 3);

	}

	@Test
	@Category(HomePage.class)
	@TestCaseParams(testCaseID = { "21897" }, testMultiple = true, skippedBrowsers = {"firefox"}) // Actions API not supported by geckodriver)
	public void testUnitContinueButton() throws Exception {
		webDriver.maximize();
		NewUxLearningArea learningArea = new NewUxLearningArea(webDriver,
				testResultService);

		report.startStep("Create new student and login");
		homePage = getUserAndLoginNewUXClass();
		sleep(3);

		report.startStep("Enter Learning Area");
		homePage.clickOnContinueButton();
		sleep(2);

		report.startStep("Set some progress in B1 Unit 2 - Lesson 1 and retorn to HP");
		studentService.createSingleProgressRecored(studentId,courses[1], "36181", 10, false, 1);
		//homePage.clickToOpenNavigationBar();
		homePage.clickOnHomeButton();
		sleep(3);
		
		report.startStep("Get Last Progress Unit Title");
		String unitTitleHP = homePage.getUnitTitleInCourseArea();

		report.startStep("Mouse hover on Unit 2 , check Btn Label");
		homePage.scrollToUnitElement(2);

		WebElement unitButton = homePage.hoverOnUnitAndGetBtnElement(2,true);
		String btnColor = unitButton.getCssValue("background-color");
		testResultService.assertEquals("rgba(39, 174, 97, 1)", btnColor, "Button color is not valid before hover");
		
		String actualLabel = unitButton.getText();
		testResultService.assertEquals("Continue", actualLabel);
		sleep(1);

		report.startStep("Mouse hover on Unit Image & check Unit btn highlighted");
		unitButton = homePage.hoverOnUnitImageAndGetBtnElement(2,true);
		btnColor = unitButton.getCssValue("background-color");
		testResultService.assertEquals("rgba(57, 207, 96, 1)", btnColor, "Button color is not valid after hover");
				
		report.startStep("Click on Continue btn");
		unitButton.click();
		sleep(1);
		
		report.startStep("Versify LA opened on correct Unit");
		String unitTitleLA = learningArea.getHeaderTitle();
		if (!testResultService.assertEquals(unitTitleHP, unitTitleLA)) {
			webDriver.printScreen("Unit name is not the same");
		}

		report.startStep("Click Home Btn");
		//learningArea.clickToOpenNavigationBar();
		learningArea.clickOnHomeButton();
		sleep(2);
		
		report.startStep("Mouse hover on unit title in unit block & click on this area");
		WebElement unitTitleInUnitsGrid = homePage.hoverOnUnitTitleInUnitBlockAndGetElement(2);
		unitTitleInUnitsGrid.click();

		report.startStep("Check lesson list opened");
		for (int i = 0; i < 7; i++) {
			int index = i + 1;
			homePage.getLessonElement(1, index);
		}
		report.startStep("Mouse hover on other Units and check Unit Btn Not Displayed");
		homePage.checkUnitBtnNotDisplayed(3, 4);

	}

	@Test
	@TestCaseParams(testCaseID = {"21150"})
	public void testUnitLessonsBtnFunction() throws Exception {

		report.startStep("Create new student and login");
		homePage = createUserAndLoginNewUXClass();
		
		report.startStep("Mouse hover on Unit 1");
		homePage.scrollToUnitElement(1);
		

		report.startStep("Click on Unit Lessons btn");
		homePage.clickOnUnitLessons(1);
		
		report.startStep("Check Lesson List");
		checkLessonListInUnit("FD", 1, 7);

		report.startStep("Click on empty area and check lesson list closed");
		WebElement nextUnitElement = homePage.hoverOnUnitTitleInUnitBlockAndGetElement(2);
		webDriver.clickOnElementWithOffset(nextUnitElement, -50, 0);
		homePage.checkThatUnitLessonListNotDisplayed(1);
		
		report.startStep("Mouse hover on unit title in unit block & click on this area");
		WebElement unitTitleInUnitsGrid = homePage.hoverOnUnitTitleInUnitBlockAndGetElement(1);
		unitTitleInUnitsGrid.click();
		
		report.startStep("Check Lesson List");
		checkLessonListInUnit("FD", 1, 7);
		
		report.startStep("Click on empty area and check lesson list closed");
		nextUnitElement = homePage.hoverOnUnitTitleInUnitBlockAndGetElement(2);
		webDriver.clickOnElementWithOffset(nextUnitElement, -50, 0);
		homePage.checkThatUnitLessonListNotDisplayed(1);
		
	}
	
	@Test
	@Category(HomePage.class)
	@TestCaseParams(testCaseID = { "21894" }, testMultiple = true, skippedBrowsers = {"firefox"}) // Actions API not supported by geckodriver)
	public void NoUnitBtnIfCourseCompleted() throws Exception {

		report.startStep("Create new student and login");
		homePage = createUserAndLoginNewUXClass();

		report.startStep("Set full progress in FD course");
		studentService.setProgressForCourse(courses[0], studentId, null, 60);
		webDriver.refresh();

		report.startStep("Navigate back to FD");
		homePage.carouselNavigateBack();
		sleep(2);

		report.startStep("Mouse hover on all Units and check if Unit Btn Displayed");
		homePage.checkUnitBtnNotDisplayed(1, 8);

	}
	
	@Test
	@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "21274" })
	public void testHeaderUnitColorAndSubColor() throws Exception {

		int unitIndex = 1;
		report.startStep("Login to EDO and navigate to a selected course");

		homePage = createUserAndLoginNewUXClass();

		for (int i = 0; i < 8; i++) {
			report.startStep("Click on lesson");
			homePage.clickOnUnitLessons(unitIndex);
			sleep(1);
			homePage.clickOnLesson(unitIndex - 1, 3);
			report.startStep("Check background color");
			sleep(2);
			String hexColor = HexUnitColors[unitIndex - 1];
			String hexHeaderBackColor = headerColors[unitIndex - 1];
			WebElement element = webDriver.waitForElement(
					"//section[contains(@class,'learning__userBar utils__unitColorSubBG-" + unitIndex
							+ "')]", ByTypes.xpath);
			// HEX if IE
			String borderColor = element.getCssValue("border-color");
			String backgroundColor = element.getCssValue("background-color");
			backgroundColor = backgroundColor.replace(", 1)", ")");
			WebElement headerElement = webDriver.waitForElement(
					"//section[contains(@class,'utils__unitColorSubBG-"
							+ unitIndex + "')]", ByTypes.xpath);
			String headerBackground = headerElement
					.getCssValue("background-color");
			// HEX if IE
			// String headerBorder = element.getCssValue("border-color");
			// String headerExpectedColor = headerColors[unitIndex - 1];
			if (webDriver.getBrowserName() != "firefox") {
				pageHelper.checkColor(borderColor, hexColor);
				pageHelper.checkColor(backgroundColor, hexColor);

				pageHelper.checkColor(headerBackground, hexHeaderBackColor);
				// pageHelper.checkColor(headerBorder, hexHeaderBackColor);

			}
			homePage.clickToOpenNavigationBar();
			homePage.clickOnHomeButton();
			unitIndex = unitIndex + 1;
		}

	}

	private void checkLessonListInUnit(String courseCode, int unitNumber, int numOfLessons) throws Exception {
	
		String expectedLessonName;
		String actualLessonName;
		int actualLessonNumber;
		for (int i = 0; i < numOfLessons; i++) {
			int index = i + 1;
			expectedLessonName = dbService.getLessonNameByCourseUnitAndLessonNumber(getCourseIdByCourseCode(courseCode), unitNumber-1, index);
			actualLessonName = homePage.getLessonElement(unitNumber-1, index).getText();
			actualLessonNumber = Integer.parseInt(homePage.getLessonNumberElement(unitNumber-1, index).getText().replace(".",""));
			testResultService.assertEquals(expectedLessonName, actualLessonName, "Lesson name is not valid");
			testResultService.assertEquals(index, actualLessonNumber, "Lesson number is not valid");
		}
	}
	
	@After
	public void tearDown() throws Exception {
		report.startStep("Set progress to FD item");
		studentService.createSingleProgressRecored(studentId,"20000", "31515", 10, false, 1);
	
		super.tearDown();
	}
	
}
