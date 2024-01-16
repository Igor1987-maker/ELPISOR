package tests.edo.newux;

import Enums.ByTypes;
import Enums.TaskTypes;
import Interfaces.TestCaseParams;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.WebElement;
import pageObjects.edo.NewUXLoginPage;
import pageObjects.edo.NewUxDragAndDropSection2;
import pageObjects.edo.NewUxLearningArea2;
import testCategories.edoNewUX.HomePage;
import testCategories.edoNewUX.SanityTests;

import java.util.List;

//@Category(AngularLearningArea.class)
public class CourseUnitContinueBtnTests2 extends BasicNewUxTest {

	NewUxLearningArea2 learningArea2;
	
	@Before
	public void setup() throws Exception {
		super.setup();
		report.startStep("Create new student and login");
		homePage = createUserAndLoginNewUXClass();
		homePage.closeAllNotifications();
		homePage.waitHomePageloadedFully();
		
	}
	
	@Test
	@Category(HomePage.class)
	@TestCaseParams(testCaseID = { "21849" }, testMultiple = true)
	public void testCourseStartBtn() throws Exception {


		report.startStep("Get Unit / Lesson Titles in Course Area");
		String LessonTitleOnHomePage = homePage.getLessonTitleInCourseArea();

		report.startStep("Check and Press Start btn");
		learningArea2 = homePage.checkTextandClickOnCourseBtn("Start");
		learningArea2.waitToLearningAreaLoaded();
		
		report.startStep("Get Unit / Lesson Titles in Learning Area");
		String LessonTitleOnLearningArea = learningArea2.getLessonNameFromHeader();

		report.startStep("Verify Navigation To Correct Lesson");
		testResultService.assertEquals(LessonTitleOnHomePage, LessonTitleOnLearningArea);

		report.startStep("Verify 1st Step Open");
		testResultService.assertEquals("1",learningArea2.getStepNumberFromHeader(),"Landed not in 1st Step of lesson");

	}

	@Test
	@Category(HomePage.class)
	@TestCaseParams(testCaseID = { "19984" }, testMultiple = true)
	public void testCourseContinueBtnFD() throws Exception {
		
		homePage.logOutOfED();
		testCourseContinueButtonByCourseId(courses[0],
				institutionId, configuration.getClassName());

	}

	@Test
	@Category(HomePage.class)
	@TestCaseParams(testCaseID = { "19984" }, testMultiple = true)
	public void testCourseContinueBtnB1() throws Exception {
		
		homePage.logOutOfED();
		testCourseContinueButtonByCourseId(courses[1],
				configuration.getInstitutionId(), configuration.getClassName());

	}
	
	@Test
	@Category(HomePage.class)
	@TestCaseParams(testCaseID = { "19984" }, testMultiple = true)
	public void testCourseContinueBtnESP() throws Exception {
		
		institutionName=institutionsName[1];
		pageHelper.initializeData();
		   
        String url = webDriver.getUrl();
        url = url.split(".com")[0] + ".com/" + BasicNewUxTest.institutionName;
       
        webDriver.quitBrowser();
        webDriver.init();
        webDriver.maximize();
        webDriver.openUrl(url);
		
		testCourseContinueButtonByCourseId(specialCoursesData.get(4)[1],
				institutionId, specialCoursesData.get(4)[0]);

	}

	@Test
	@Category(HomePage.class)
	@TestCaseParams(testCaseID = { "19984" }, testMultiple = true)
	public void testCourseContinueBtnCourseBuilder() throws Exception {

		institutionName=institutionsName[1];
		pageHelper.initializeData();
		   
        String url = webDriver.getUrl();
        url = url.split(".com")[0] + ".com/" + BasicNewUxTest.institutionName;
       
        webDriver.quitBrowser();
        webDriver.init();
        webDriver.maximize();
        webDriver.openUrl(url);
        
		testCourseContinueButtonByCourseId(specialCoursesIds[3], institutionId, configuration.getProperty("classname.builder"));

	}

	@Test
	@Category(HomePage.class)
	@TestCaseParams(testCaseID = { "19984" }, testMultiple = true)
	public void testCourseContinueBtnCourseEnriched() throws Exception {

		institutionName=institutionsName[1];
		pageHelper.initializeData();
		   
        String url = webDriver.getUrl();
        url = url.split(".com")[0] + ".com/" + BasicNewUxTest.institutionName;
       
        webDriver.quitBrowser();
        webDriver.init();
        webDriver.maximize();
        webDriver.openUrl(url);
        
		testCourseContinueButtonByCourseId(specialCoursesIds[2], institutionId, configuration.getProperty("classname.enrich"));

	}

	@Test
	@Category(HomePage.class)
	@TestCaseParams(testCaseID = { "21850" }, testMultiple = true)
	public void testCourseStartOverBtn() throws Exception {

	/*	report.startStep("Create new student and login");
		homePage = createUserAndLoginNewUXClass();
		homePage.waitHomePageloaded();
	*/
		report.startStep("Enter Learning Area");
		String UnitTitleOnHomePage = homePage.getUnitTitleInCourseArea();
		homePage.checkTextandClickOnCourseBtn("Start");

		report.startStep("Set progress 100% in FD and return to HP");
		studentService.setProgressForCourse(courses[0], studentId, null, 60,false,false);
		//homePage.clickToOpenNavigationBar();
		sleep(1);
		homePage.clickOnHomeButton();
		homePage.waitHomePageloadedFully();
		
		report.startStep("Navigate back to FD");
		homePage.carouselNavigateBack();
		sleep(1);
		homePage.waitHomePageloadedFully();
		report.startStep("Check and Press Start Over btn");
		learningArea2 = homePage.checkTextandClickOnCourseBtn("Start Over");
		learningArea2.waitToLearningAreaLoaded();
		
		report.startStep("Verify Navigation To Correct Lesson");
		learningArea2.openLessonsList();
		String UnitTitleOnLearningArea = learningArea2.getUnitTitleInLessonList();
		testResultService.assertEquals(UnitTitleOnHomePage, UnitTitleOnLearningArea);
		
		report.startStep("Verify 1st Step Open");
		testResultService.assertEquals("1",learningArea2.getStepNumberFromHeader(),"Landed not in 1st Step of lesson");

	}

	@Test
	@Category(HomePage.class)
	@TestCaseParams(testCaseID = { "21895" }, testMultiple = true, skippedBrowsers = {"firefox"}) // Actions API not supported by geckodriver)
	public void testUnitStartButton() throws Exception {
		
		int unitNum = 1;
		
		report.startStep("Get Last Progress Unit Title");
		String unitTitleHP = homePage.getUnitTitleInCourseArea();

		report.startStep("Mouse hover on Unit 1, check Btn Label");
		homePage.scrollToUnitElement(unitNum);

		WebElement startButton = homePage.hoverOnUnitAndGetBtnElement(unitNum,true);
		String btnColor = startButton.getCssValue("background-color");
		testResultService.assertEquals("rgba(39, 174, 97, 1)", btnColor, "Button color is not valid before hover");
		
		String actualLabel = startButton.getText();
		testResultService.assertEquals("Start", actualLabel);
						
		report.startStep("Click on Start btn");
		startButton.click();
		sleep(1);
				
		report.startStep("Verify LA opened on correct Unit");
		NewUxLearningArea2 learningArea2 = new NewUxLearningArea2(webDriver, testResultService);
		learningArea2.openLessonsList();
		sleep(1);
		String unitTitleLA = learningArea2.getUnitTitleInLessonList();
		testResultService.assertEquals(unitTitleHP, unitTitleLA);
		learningArea2.closeLessonsList();

		report.startStep("Click Home Btn");
		//homePage.clickToOpenNavigationBar();
		sleep(1);
		homePage.clickOnHomeButton();
		homePage.waitHomePageloaded();
		
		report.startStep("Mouse hover on unit title in unit block & click on this area");
		WebElement unitTitleInUnitsGrid = homePage.hoverOnUnitTitleInUnitBlockAndGetElement(1);
		unitTitleInUnitsGrid.click();

		report.startStep("Check lesson list opened");
		for (int i = 0; i < 7; i++) {
			int index = i + 1;
			homePage.getLessonElement(unitNum-1, index);
		}
		
		report.startStep("Mouse hover on other Units and check Unit Btn Not Displayed");
		homePage.checkUnitBtnNotDisplayed(unitNum+1, unitNum+2);

	}

	@Test
	@Category(HomePage.class)
	@TestCaseParams(testCaseID = { "21897" }, testMultiple = true, skippedBrowsers = {"firefox"}) // Actions API not supported by geckodriver)
	public void testUnitContinueButton() throws Exception {
				
		int courseIndex = 1;
		int unitNumber = 2;
		int lessonNumber = 1;
		
		report.startStep("Enter Learning Area B1 - Unit 2 - Lesson 1");
		homePage.navigateToRequiredCourseOnHomePage(coursesNames[courseIndex]);
		homePage.waitHomePageloadedFully();
		homePage.clickOnUnitLessons(unitNumber);
		learningArea2 = homePage.clickOnLesson2(unitNumber-1, lessonNumber);
		learningArea2.waitToLearningAreaLoaded();
				
		report.startStep("Set some progress in B1 Unit 2 - Lesson 1 and retorn to HP");
		studentService.setProgressInFirstComponentInUnit(unitNumber-1, studentId, courses[courseIndex]);

		homePage.clickOnHomeButton();
		homePage.waitHomePageloadedFully();
		
		report.startStep("Get Last Progress Unit Title");
		String unitTitleHP = homePage.getUnitTitleInCourseArea();

		report.startStep("Mouse hover on Unit 2 , check Btn Label");
		homePage.scrollToUnitElement(unitNumber);

		WebElement unitButton = homePage.hoverOnUnitAndGetBtnElement(unitNumber,true);
		String btnColor = unitButton.getCssValue("background-color");
		testResultService.assertEquals("rgba(39, 174, 97, 1)", btnColor, "Button color is not valid before hover");
		
		String actualLabel = unitButton.getText();
		testResultService.assertEquals("Continue", actualLabel);
		sleep(1);

		report.startStep("Mouse hover on Unit Image & check Unit btn highlighted");
		unitButton = homePage.hoverOnUnitImageAndGetBtnElement(unitNumber,true);
		btnColor = unitButton.getCssValue("background-color");
		testResultService.assertEquals("rgba(57, 207, 96, 1)", btnColor, "Button color is not valid after hover");
				
		report.startStep("Click on Continue btn");
		unitButton.click();
		
		report.startStep("Versify LA opened on correct Unit");
		NewUxLearningArea2 learningArea2 = new NewUxLearningArea2(webDriver, testResultService);
		learningArea2.waitToLearningAreaLoaded();
		learningArea2.openLessonsList();
		sleep(2);
		String unitTitleLA = learningArea2.getUnitTitleInLessonList();
		if (!testResultService.assertEquals(unitTitleHP, unitTitleLA)) {
			webDriver.printScreen("Unit name is not the same");
		}
		learningArea2.closeLessonsList();

		report.startStep("Click Home Btn");
		//homePage.clickToOpenNavigationBar();
		sleep(1);
		homePage.clickOnHomeButton();
		homePage.waitHomePageloaded();
		
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

	//	report.startStep("Create new student and login");
	//	homePage = createUserAndLoginNewUXClass();
		
		report.startStep("Mouse hover on Unit 1");
		homePage.scrollToUnitElement(1);
		

		report.startStep("Click on Unit Lessons btn");
		homePage.clickOnUnitLessons(1);
		
		report.startStep("Check Lesson List");
		checkLessonListInUnit("FD", 1, 7);
		sleep(2);

		report.startStep("Click on empty area and check lesson list closed");
		WebElement nextUnitElement = homePage.hoverOnUnitTitleInUnitBlockAndGetElement(2);
		webDriver.clickOnElementWithOffset(nextUnitElement, -50, 0);
		sleep(3);
		homePage.checkThatUnitLessonListNotDisplayed(1);
		
		report.startStep("Mouse hover on unit title in unit block & click on this area");
		WebElement unitTitleInUnitsGrid = homePage.hoverOnUnitTitleInUnitBlockAndGetElement(1);
		unitTitleInUnitsGrid.click();
		
		report.startStep("Check Lesson List");
		checkLessonListInUnit("FD", 1, 7);
		sleep(2);
		
		report.startStep("Click on empty area and check lesson list closed");
		nextUnitElement = homePage.hoverOnUnitTitleInUnitBlockAndGetElement(2);
		sleep(2);
		webDriver.clickOnElementWithOffset(nextUnitElement, -50, 0);
		webDriver.printScreen("After pressing outside of lesson menu");
		sleep(1);
		homePage.checkThatUnitLessonListNotDisplayed(1);
		
	}
	
	@Test
	@Category(HomePage.class)
	@TestCaseParams(testCaseID = { "21894" }, testMultiple = true, skippedBrowsers = {"firefox"}) // Actions API not supported by geckodriver)
	public void testNoUnitBtnIfCourseCompleted() throws Exception {

		homePage.logOutOfED();

		report.startStep("Get user that completed Unit");
		
		String[] student = pageHelper.getStudentCompletedCourseProgress(institutionId);
		userName = student[0];
		String password = student[1];
		String coursesName = student[3];
		
		NewUXLoginPage loginPage = new NewUXLoginPage(webDriver,testResultService);
		
		report.startStep("Login with User Name: "+userName+" ");
		
		loginPage.loginAsStudent(userName, password);
		homePage.closeAllNotifications();
		homePage.navigateToRequiredCourseOnHomePage(coursesName);
		homePage.waitHomePageloadedFully();
		
		int unitCount = dbService.getUnitNamesByCourse(student[4]).size();
		
		report.startStep("Mouse hover on all Units and check if Unit Btn Displayed");
		homePage.checkUnitBtnNotDisplayed(1, unitCount);

	}
	
//	@Test
//	@Category(inProgressTests.class)
//	@TestCaseParams(testCaseID = { "21274" })
	public void testHeaderUnitColorAndSubColor() throws Exception {

		int unitIndex = 1;
		report.startStep("Login to EDO and navigate to a selected course");

	//	homePage = createUserAndLoginNewUXClass();

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

	private void testCourseContinueButtonByCourseId(String courseId,
			String institutionId, String className) throws Exception {
		
		report.startStep("Get Course name by CourseId");
		String courseName = dbService.getCourseNameById(courseId).trim();
		
	//	homePage.logOutOfED();

		report.startStep("Create new student and login");
		homePage = createUserAndLoginNewUXClass(className, institutionId);
		homePage.closeAllNotifications();
		homePage.waitHomePageloadedFully();
		
		report.startStep("Navigate to Course and check course area titles");
		homePage.navigateToRequiredCourseOnHomePage(courseName);
		homePage.getLessonTitleInCourseArea();
		String courseTitle = homePage.getCurrentCourseName();
		String lessonTitleHP = homePage.getLessonTitleInCourseArea();
		testResultService.assertEquals(courseName, courseTitle);
		
		report.startStep("Enter Learning Area");
		learningArea2 =  homePage.checkTextandClickOnCourseBtn("Start");
		learningArea2.waitToLearningAreaLoaded();
		
		report.startStep("Set progress in "+courseName+" and return to HP");
		studentService.setProgressInFirstSubcomponentInComponent(0,0,courseId,studentId);
		//homePage.clickToOpenNavigationBar();
		sleep(2);
		homePage.clickOnHomeButton();
		homePage.waitHomePageloadedFully();

		report.startStep("Check and Press Continue btn");
		learningArea2 = homePage.checkTextandClickOnCourseBtn("Continue");

		report.startStep("Verify Navigation To Correct Lesson");		
		String lessonTitleLA = learningArea2.getLessonNameFromHeader();
		testResultService.assertEquals(lessonTitleHP, lessonTitleLA);

		report.startStep("Verify 1st Step Open");
		testResultService.assertEquals("1",learningArea2.getStepNumberFromHeader(),"Landed not in 1st Step of lesson");
		
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
	
	@Test
	@Category(SanityTests.class)
	@TestCaseParams(testCaseID = { "86962" }, testMultiple = true)
	public void testContinueToLastPracticeItemWithProgress() throws Exception {
		
		homePage.logOutOfED();
		
		report.startStep("Get Course name by CourseId");
			String courseName = dbService.getCourseNameById(courses[1]).trim();
		
		report.startStep("Create new student and login");
			homePage = createUserAndLoginNewUXClass(configuration.getClassName(), institutionId);
			homePage.closeAllNotifications();
			homePage.waitHomePageloadedFully();
					
		report.startStep("Navigate to Course and check course area titles");
		//	homePage.navigateToRequiredCourseOnHomePage(courseName);
			List<String[]> item = pageHelper.getLastVisitedItem(studentId);
			TestCase.assertTrue("New created user heave last visited item id not null", item==null);
			learningArea2 = homePage.navigateToTask(courseCodes[1], 1, 1, 2, 1);
		
		
		report.startStep("Verify visited item saved on DB");
		/*	item = pageHelper.getLastVisitedItem(studentId);
			String itemId = item.get(0)[3];
			String itemName = item.get(0)[11];
			String itemNameFromUI = learningArea2.getTaskName();
			
			textService.assertEquals("Item name from DB and from UI not match", itemName, itemNameFromUI);
		*/	
		report.startStep("Do some steps");
			NewUxDragAndDropSection2 dragAndDrop2 = new NewUxDragAndDropSection2 (webDriver, testResultService);
			dragAndDrop2.dragAndDropFirstAnswer(TaskTypes.MTTP);
			sleep(1);
			learningArea2.clickOnNextButton();
			sleep(1);
			learningArea2.SelectRadioBtn("question-1_answer-1");
			
		report.startStep("Verify id of visited item changed after moving to next task");
	/*		item = pageHelper.getLastVisitedItem(studentId);
			String itemId2 = item.get(0)[3];
			textService.assertTrue("Item id the same, after moving to another task ", itemId!=itemId2);//22311
		*/	
		report.startStep("Store place of last item with progress");
			String lessonTitleLA = learningArea2.getLessonNameFromHeader();
			int currentTaskPage = learningArea2.getNumberOfLastTaskWithProgress();//.getCurrentTasksPage();
			String stepTitleLA = learningArea2.getStepNameFromHeader();
			learningArea2.clickOnNextButton();

		report.startStep("Exit from LA");
			homePage.clickOnHomeButton();
			homePage.waitHomePageloaded();
		
		report.startStep("Check course area titles");
			String courseTitle = homePage.getCurrentCourseName();
			//String lessonTitleHP = homePage.getLessonTitleInCourseArea();
			testResultService.assertEquals(courseName, courseTitle);
		
		report.startStep("Check and Press Continue btn");
			learningArea2 = homePage.checkTextandClickOnCourseBtn("Continue");
		
		report.startStep("Verify that system return me to the last item with progress");
			String lessonTitleLA_2 = learningArea2.getLessonNameFromHeader();
			int currentTaskPage_2 = learningArea2.getNumberOfLastTaskWithProgress();
			String stepTitleLA_2 = learningArea2.getStepNameFromHeader();
			textService.assertEquals("Lesson not match", lessonTitleLA, lessonTitleLA_2);
			textService.assertEquals("Task not match", currentTaskPage, currentTaskPage_2);
			textService.assertEquals("Step not match", stepTitleLA, stepTitleLA_2);
		
		report.startStep("LogOut of ED");
			homePage.logOutOfED();
		
		//1. Enter to specific item in practice
		//homePage.navigateToTask(coursesInstId, failure_count, failure_count, failure_count, failure_count);
		// 2. do an action in this practice
		// 3. back to home
		//4. verify the last unit and esson on home
		//5. click on continue
		//6. verify you landed to above practice item.
		
		// 7. do the same on FD explore and other courses with Test component tasks and Interact 1/2
			
	}
	
	@Test
	@Category(HomePage.class)
	@TestCaseParams(testCaseID = { "86987" }, testMultiple = true)
	public void testCourseContinueToNextlessonOfUnit() throws Exception {
	homePage.logOutOfED();
		
		report.startStep("Get Course name by CourseId");
			String courseName = dbService.getCourseNameById(courses[0]).trim();
		
		report.startStep("Create new student and login");
			homePage = createUserAndLoginNewUXClass(configuration.getClassName(), institutionId);
			sleep(3);
			homePage.closeAllNotifications();
			
		report.startStep("Navigate to Course and check course area titles");
		//	homePage.navigateToRequiredCourseOnHomePage(courseName);
			learningArea2 = homePage.navigateToTask(courseCodes[0], 1, 1, 1, 1);
			String courseId = getCourseIdByCourseCode(courseCodes[0]);
			List<String[]> units = dbService.getCourseUnitDetils(courseId);
			
			List<String[]> components = dbService.getComponentDetailsByUnitId(units.get(0)[0]); //we need only one first unit at this test 
		
			int unitIndex = 1;
			int lessonIndex = 1;
			int stepIndex = 1;
			int taskIndex = 1;
			
	//	for (int j = 0; j < components.size(); j++) { // PRODUCTION
		for (int j = 0; j < 1; j++) {
		
		report.startStep("Go over lesson: " + components.get(j)[0]);
					
			report.startStep("Get Component SubComponents(Steps) by componentId");
			List<String[]> subComp = dbService.getSubComponentsDetailsByComponentId(components.get(j)[1]);

			boolean isTest = false;
			boolean isInteract = false;
			boolean stepHasIntro = false;
			String stepName = "undefined";
			stepIndex = 1;
			taskIndex = 1;
							
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
					
				
				} else if (stepHasIntro) learningArea2.clickOnNextButton();
			
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
			
						report.startStep("Making progress in Task "+taskIndex+" / Checking status of Task " + taskIndex);
						try {
							learningArea2.makeProgressActionByItemType(itemTypeId, itemCode);
						} catch (Exception e) {
							testResultService.addFailTest("Cannot make progress in "+courseName+" : "+"Unit "+unitIndex+" - "+"Lesson "+ lessonIndex +" - "+"Step "+stepIndex+" - "+"Task "+ taskIndex, false, true);
							e.printStackTrace();
						}
					taskIndex++;
				}
			}
		lessonIndex = j + 2;
		}
		
		report.startStep("Exit from LA");
			homePage.clickOnHomeButton();
			homePage.waitHomePageloaded();
	
		report.startStep("Check and Press Continue btn");
			learningArea2 = homePage.checkTextandClickOnCourseBtn("Continue");
			sleep(5);
				
		report.startStep("Check Unit and lesson name in Lesson: " + lessonIndex);
			List<String[]> subComp = dbService.getSubComponentsDetailsByComponentId(components.get(1)[1]);
			String expectedStepName = subComp.get(0)[0];
			String currentStepName = learningArea2.getStepNameFromHeader();
			String expectedLessonName = dbService.getLessonNameBySkill(components.get(1));
			String currentLessonName = learningArea2.getLessonNameFromHeader();
			textService.assertEquals("Lesson name not match", expectedLessonName, currentLessonName);
			textService.assertEquals("Step name not match", expectedStepName, currentStepName);
		
		}
			
	
	@After
	public void tearDown() throws Exception {
		/*report.startStep("Set progress to FD item");
		studentService.createSingleProgressRecored(studentId,"20000", "31515", 10, false, 1);*/
		institutionName="";
		super.tearDown();
	}
	
}
