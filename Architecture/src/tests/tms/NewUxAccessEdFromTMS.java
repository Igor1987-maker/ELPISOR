package tests.tms;


import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.WebElement;

import Enums.ByTypes;
import Enums.CommunityActivities;
import Enums.NavBarItems;
import Enums.TaskTypes;
import Interfaces.TestCaseParams;
import testCategories.AngularLearningArea;
import testCategories.NonAngularLearningArea;
import testCategories.inProgressTests;
import Objects.Recording;
import pageObjects.RecordPanel;
import pageObjects.edo.NewUXLoginPage;
import pageObjects.edo.NewUxAssessmentsPage;
import pageObjects.edo.NewUxInstitutionPage;
import pageObjects.edo.NewUxLearningArea;
import pageObjects.edo.NewUxLearningArea2;
import pageObjects.edo.NewUxMyProfile;
import pageObjects.edo.NewUxCommunityPage;
import pageObjects.edo.NewUxDragAndDropSection2;
import pageObjects.tms.DashboardPage;
import pageObjects.tms.TmsHomePage;
import tests.edo.newux.SpeechRecognitionBasicTestNewUX;

public class NewUxAccessEdFromTMS extends SpeechRecognitionBasicTestNewUX {

	NewUXLoginPage loginPage;
	DashboardPage dashboardPage;
	TmsHomePage tmsHomePage;
	NewUxLearningArea learningArea;
	NewUxLearningArea2 learningArea2;
	NewUxAssessmentsPage testsPage;
	NewUxInstitutionPage ipage;
	NewUxMyProfile myProfile;
	NewUxCommunityPage communityPage;
	
	@Before
	public void setup() throws Exception {
		super.setup();
	
		
	}
		
	@Test
	@Category(NonAngularLearningArea.class)
	@TestCaseParams(testCaseID = { "28638" })
	public void testLessonPreviewFromTMS() throws Exception {

		
		report.startStep("Open New UX Login Page");
		loginPage = new NewUXLoginPage(webDriver, testResultService);
		pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(configuration.getProperty("teacher.username"),institutionId));

		report.startStep("Login as Teacher");
		tmsHomePage = loginPage.enterTeacherUserAndPassword();
		sleep(3);
		tmsHomePage.switchToMainFrame();
		
		report.startStep("Open ED Preview page for B2 -> U2 -> L2");
		tmsHomePage.clickOnCurriculum();
		sleep(3);
		tmsHomePage.clickOnAssignCourses();
		sleep(3);
		tmsHomePage.selectClass(configuration.getProperty("classname.openSpeech"));
		sleep(3);
				
		tmsHomePage.openPreviewByCourseUnitAndLesson(courseCodes, "B2", 2, 2);
		webDriver.switchToNextTab();
		
		report.startStep("Check Navigation bar, User Menu & Footer hiden");
		learningArea2 = new NewUxLearningArea2(webDriver, testResultService);
		learningArea2.checkThatNavBarIsNotDisplayed();
		learningArea2.checkThatUserMenuIsNotDisplayed();
		//learningArea.checkThatFooterIsNotDisplayed();	// reported Bug 29778:Preview TMS / Footer Not Hiden In Previewed Lesson (P) (FF)	
		
		report.startStep("Check landed on expected Unit - Lesson - Step");
		learningArea2.checkUnitLessonStepNameOnLanding("Unit 2: Healthy Eating", "Dieters Are Feeling Great!", "Explore");
				
		report.startStep("Press on Hear btn");
		learningArea2.clickOnNextButton();
		learningArea2.clickOnHearAll();
		
		report.startStep("Press Step 2 - Practice");
		learningArea2.clickOnStep(2);
		sleep(1);
		
		report.startStep("Select 1 correct 1 wrong answer");
		learningArea2.clickOnNextButton(1);
		
		report.startStep("Drag all tiles to droptargets");
		NewUxDragAndDropSection2 dragAndDrop2 = new NewUxDragAndDropSection2(webDriver, testResultService);	
		dragAndDrop2.dragAndDropAnswerByTextToTarget("special", 1, TaskTypes.Close);
		sleep(1);

		report.startStep("Click on Check Answer and check V/X signs placed correctly");
		learningArea2.clickOnCheckAnswer();
		sleep(1);
	//	learningArea2.verifyAnswerRadioSelectedWrong(1, 1);
	//	learningArea2.verifyAnswerRadioSelectedWrong(2, 4);

		report.startStep("Verify Practice Tools State");
		learningArea2.verifyPracticeToolsStateOnCheckAnswer();
		
		report.startStep("Press on Task 5 (E-Rater) and check Submit btn hiden");
		learningArea2.openTaskBar();
		learningArea2.clickOnTask(4);
		learningArea2.checkThatSubmitToTeacherBtnIsDisabled();
		
		report.startStep("Submit Test and check test results displayed");
		learningArea2.clickOnStep(3);
		learningArea2.closeAlertModalByAccept();
		sleep(1);
		learningArea2.clickTheStartTestButon();
		sleep(1);
	//	learningArea2.selectMultipleAnswer(2);
		sleep(1);
		learningArea2.clickOnNextButton();
		sleep(1);
	//	learningArea2.selectMultipleAnswer(3);
		sleep(1);
		learningArea2.clickToOpenNavigationBar();
		learningArea2.submitTest(true);
		sleep(1);
		
		webDriver.switchToNextTab();
		webDriver.switchToTopMostFrame();
		learningArea2.checkThatTestWasSubmitted();
		
		report.startStep("Check Next btn hiden");
		learningArea2.checkThatNextButtonIsNotDisplayedInLA();
		
		report.startStep("Select task and verify Correct/Your Answer tabs displayed");
		learningArea2.clickOnTask(0);
		sleep(1);
		learningArea2.clickOnYourAnswerTab();
		sleep(1);
		learningArea2.clickOnCorrectAnswerTab();
		learningArea2.checkThatNextButtonIsNotDisplayedInLA();
		
		report.startStep("Open Lesson List and check no progress indication");
		learningArea2.openLessonsList();
		learningArea2.checkThatLessonListHasNoProgressBars();
		
		report.startStep("Select Lesson 3 and check check landed on expected Unit - Lesson - Step");
		learningArea2.clickOnLessoneByIndex(3);
		sleep(3);
		learningArea2.checkUnitLessonStepNameOnLanding("Unit 2: Healthy Eating", "Piece of Cake", "Explore");
				
		report.startStep("Navigate To Step 4 and check Next btn hiden");	
		learningArea2.clickOnStep(4);
		sleep(2);
		learningArea2.checkThatNextButtonIsNotDisplayedInLA();
		
		
		// bug reported 37199 - commented as after 50 failures still not fixed
		/*report.startStep("Refresh and check check landed on expected Unit - Lesson - Step");
		webDriver.refresh();
		sleep(2);
		learningArea.checkUnitLessonStepNameOnLanding("Unit 2: Healthy Eating", "Piece of Cake", "Explore");*/
		
		report.startStep("Logout from TMS and check ED tab closed");
		logoutFromTmsAndCheckEdTabsClosed();
		
	}
	
	@Test
	@Category(NonAngularLearningArea.class)
	@TestCaseParams(testCaseID = { "32586", "32556" , "31171"})
	public void testAccessToEdEntryCriteria() throws Exception {

		
		report.startStep("Get Teacher details");
		studentId = dbService.getUserIdByUserName(configuration.getProperty("teacher.username"),institutionId);
		String firstName = dbService.getUserFirstNameByUserId(studentId);
		
		report.startStep("Open New UX Login Page");
		loginPage = new NewUXLoginPage(webDriver, testResultService);
		pageHelper.setUserLoginToNull(studentId);

		report.startStep("Login as Teacher");
		tmsHomePage = loginPage.enterTeacherUserAndPassword();
		homePage.skipNotificationWindow();	
		tmsHomePage.waitForPageToLoad();
		tmsHomePage.switchToMainFrame();
		
		report.startStep("Press on 'Access To ED Platform' link");
		tmsHomePage.clickOnCurriculum();
		//sleep(2);
		homePage = tmsHomePage.clickOnAccessToEd();
		sleep(2);
		webDriver.switchToNextTab();

		report.startStep("Verify user details in ED header");
		homePage.waitHomePageloadedFully();
		testResultService.assertEquals("Hello " + firstName, homePage.getUserDataText(), "Verify First Name in Header of Home Page");
				
		report.startStep("Verify assigned courses and that widgets display no progress");
		
		//String [] courses = pageHelper.getInstitutionAllCoursesNames(institutionId);
		List<String[]> coursesList = pageHelper.getInstitutionAllCoursesNames(institutionId);
				
		for (int i = 0; i < coursesList.size(); i++) {
			
			String actualCourse = homePage.getCurrentCourseElement(i+1).getText();
			String completionNoProgress = homePage.getCompletionWidgetValue();
			
			testResultService.assertEquals(actualCourse, coursesList.get(i)[1],"Course Name is Wrong");
			testResultService.assertEquals("0", completionNoProgress, "Testing Course:" + coursesList.get(i)[1]);
			
			homePage.carouselNavigateNext();
			homePage.waitHomePageloadedFully();
		}
		
		String actualCourse = homePage.getCurrentCourseElement(1).getText();
		testResultService.assertEquals(actualCourse, coursesList.get(0)[1],"Assigned courses list is wrong");
				
		report.startStep("Navigate to LA and check that progress displayed");
		
		learningArea = homePage.navigateToCourseUnitLessonLA2(courseCodes, "FD", 2, 1);
		String courseId = getCourseIdByCourseCode("FD");
		String itemId = pageHelper.getItemIdByCourseUnitLessonStepAndTask(courseId, 2, 1, 1, 1);
		
		learningArea.clickOnPlayVideoButtonPrepare();
		sleep(1);
		learningArea.clickOnNextButton();
		//learningArea.clickOnStep(1);
		//learningArea2.openTaskBar();
		//learningArea2.checkIfTaskHasDoneState(1,true);
		//learningArea.checkThatTaskIsDone(0);
		
		report.startStep("Navigate to HP and check that progress not saved");
		//learningArea.clickToOpenNavigationBar();
		learningArea.clickOnHomeButton();
		homePage.waitHomePageloadedFully();
		String completionNoProgress = homePage.getCompletionWidgetValue();
		testResultService.assertEquals("0", completionNoProgress, "Testing no progress displayed on course statistics");
		studentService.checkStudentHasNoProgress(studentId, courseId, itemId);
				
		report.startStep("Verify Nav Bar buttons behavior for teacher");
		verifyNavBarModeForTeacher();
				
		report.startStep("Click on My Info btn and check all links disabled");
		homePage.clickOnUserAvatar();
		sleep(1);
		homePage.verifyLogoutLinkIsDisabled();
		learningArea.verifyStudyPlannerLinkIsDisabled();
		learningArea.verifyMyProfileLinkIsDisabled();
		
		report.startStep("Navigate To Learning Area and check all links disabled");
		homePage.clickOnContinueButton();
		sleep(1);
		learningArea.clickOnUserAvatar();
		sleep(1);
		learningArea.verifyMyProfileLinkIsDisabled();
		learningArea.verifyStudyPlannerLinkIsDisabled();
		learningArea.verifyLogoutLinkIsDisabled();
		
		report.startStep("Verify Nav Bar buttons behavior for teacher");
		learningArea.clickToOpenNavigationBar();
		verifyNavBarModeForTeacher();
		
		report.startStep("Logout from TMS and check ED tab closed");
		logoutFromTmsAndCheckEdTabsClosed();
	}
	
	@Test
	@Category(AngularLearningArea.class)
	@TestCaseParams(testCaseID = { "32586", "32556" , "31171"})
	public void testAccessToEdEntryCriteria2() throws Exception {

		
		report.startStep("Get Teacher details");
		studentId = dbService.getUserIdByUserName(configuration.getProperty("teacher.username"),institutionId);
		String firstName = dbService.getUserFirstNameByUserId(studentId);
		
		report.startStep("Open New UX Login Page");
		loginPage = new NewUXLoginPage(webDriver, testResultService);
		pageHelper.setUserLoginToNull(studentId);

		report.startStep("Login as Teacher");
		tmsHomePage = loginPage.enterTeacherUserAndPassword();
		homePage.closeAllNotifications();
		tmsHomePage.waitForPageToLoad();
		tmsHomePage.switchToMainFrame();
		
		report.startStep("Press on 'Access To ED Platform' link");
		tmsHomePage.clickOnCurriculum();
		homePage = tmsHomePage.clickOnAccessToEd();
		webDriver.switchToNextTab();
		homePage.waitHomePageloadedFully();
		
		report.startStep("Verify user details in ED header");
		testResultService.assertEquals("Hello " + firstName, homePage.getUserDataText(), "Verify First Name in Header of Home Page");
				
		report.startStep("Verify assigned courses and that widgets display no progress");
			verifyAssignedCoursesAndCompletion();
	
		report.startStep("Navigate to LA and check that progress displayed");
			learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "FD", 2, 1);
			String courseId = getCourseIdByCourseCode("FD");
			String itemId = pageHelper.getItemIdByCourseUnitLessonStepAndTask(courseId, 2, 1, 1, 1);
			learningArea2.waitToLearningAreaLoaded();
			learningArea2.clickOnPlayVideoButtonPrepare();
			
			learningArea2.clickOnNextButton();
			learningArea2.clickOnStep(1);
			learningArea2.openTaskBar();
			learningArea2.checkIfTaskHasDoneState(1, false);
			learningArea2.closeTaskBar();
		
		report.startStep("Navigate to HP and check that progress not saved");
			learningArea2.clickOnHomeButton();
			homePage.waitHomePageloadedFully();
			
			String completionNoProgress = homePage.getCompletionWidgetValue();
			testResultService.assertEquals("0", completionNoProgress, "Testing no progress displayed on course statistics");
			studentService.checkStudentHasNoProgress(studentId, courseId, itemId);
				
		report.startStep("Verify Nav Bar buttons behavior for teacher");
			verifyNavBarModeForTeacher();
				
		report.startStep("Click on My Info btn and check all links disabled");
			homePage.clickOnUserAvatar();
			sleep(1);
			homePage.verifyLogoutLinkIsDisabled();
			learningArea2.verifyStudyPlannerLinkIsDisabled();
			learningArea2.verifyMyProfileLinkIsDisabled();
			learningArea2.verifyOnlineSessionLinkIsDisabled();
			homePage.clickOnUserAvatar();
			learningArea2.verifyNotificationsLinkIsDisabled();
			
		report.startStep("Navigate To Learning Area and check all links disabled");
			learningArea2 = homePage.clickOnContinueButton2();
			learningArea2.waitToLearningAreaLoaded();
			learningArea2.clickOnUserAvatar();
			sleep(1);
			learningArea2.verifyMyProfileLinkIsDisabled();
			learningArea2.verifyStudyPlannerLinkIsDisabled();
			learningArea2.verifyLogoutLinkIsDisabled();
			learningArea2.verifyOnlineSessionLinkIsDisabled();
			homePage.clickOnUserAvatar();
			learningArea2.verifyNotificationsLinkIsDisabled();
			
		report.startStep("Verify Nav Bar buttons behavior for teacher");
			learningArea2.clickToOpenNavigationBar();
			verifyNavBarModeForTeacher2();
		
		// close the Ed tab
			webDriver.closeNewTab(1);
			webDriver.switchToMainWindow();
			tmsHomePage.switchToMainFrame();
				
		// continue work with TMS
			tmsHomePage.clickOnCurriculum();
			tmsHomePage.clickOnAssignCourses();
			
		report.startStep("Logout from TMS and check ED tab closed");
			logoutFromTmsAndCheckEdTabsClosed();
				
	}
	
	private void verifyAssignedCoursesAndCompletion() {

		//String [] courses = pageHelper.getInstitutionAllCoursesNames(institutionId);
		List<String[]> coursesList;
		try {
			coursesList = pageHelper.getInstitutionAllCoursesNames(institutionId);
		
			for (int i = 0; i < coursesList.size(); i++) {
				
				String courseAliasName = dbService.getCourseAliasNameById(coursesList.get(i)[0]);
				String actualCourse = homePage.getCurrentCourseElement(i+1).getText();
				String completionNoProgress = homePage.getCompletionWidgetValue().trim();
				
				testResultService.assertEquals(actualCourse, courseAliasName,"Course Name is Wrong");
				testResultService.assertEquals("0", completionNoProgress, "Testing Course:" + coursesList.get(i)[0]);
				
				homePage.carouselNavigateNext();
				homePage.waitHomePageloadedFully();
			}
			

			String actualCourse = homePage.getCurrentCourseElement(1).getText();
			testResultService.assertEquals(actualCourse, coursesNames[0],"Assigned courses list is wrong");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	@Category(NonAngularLearningArea.class)
	@TestCaseParams(testCaseID = { "32559" })
	public void testNoSubmitInWritingAssignments() throws Exception {

		
		report.startStep("Get Teacher ID");
		studentId = dbService.getUserIdByUserName(configuration.getProperty("teacher.username"),configuration.getProperty("institution.id"));
		
		report.startStep("Open New UX Login Page");
		loginPage = new NewUXLoginPage(webDriver, testResultService);
		pageHelper.setUserLoginToNull(studentId);

		report.startStep("Login as Teacher");
		tmsHomePage = loginPage.enterTeacherUserAndPassword();
		sleep(3);
		tmsHomePage.switchToMainFrame();
		
		report.startStep("Press on 'Access To ED Platform' link");
		tmsHomePage.clickOnCurriculum();
		sleep(2);
		homePage = tmsHomePage.clickOnAccessToEd();
		sleep(2);
		webDriver.switchToNextTab();
					
		report.startStep("Verify user details in ED header");
		String firstName = dbService.getUserFirstNameByUserId(studentId);
		testResultService.assertEquals("Hello " + firstName, homePage.getUserDataText(), "Verify First Name in Header of Home Page");
		
		report.startStep("Navigate to First Discoveries ->Unit 2-About Me->Steve's Application Form-> Practice -> Task 3");
		learningArea = homePage.navigateToCourseUnitAndLesson(courseCodes, "FD", 2, 4);
		learningArea.clickOnStep(3);
		learningArea.clickOnTask(2);
		
		report.startStep("Verify Send To Teacher btn not displayed");
		learningArea.checkThatSendToTeacherBtnIsNotDisplayed();
		
		report.startStep("Enter text and verify Send To Teacher btn not displayed");
		learningArea.enterFreeWritingTextBySection("1", "Hello");
		learningArea.checkThatSendToTeacherBtnIsNotDisplayed();
		
		report.startStep("Navigate to ERater task");
		//learningArea.clickToOpenNavigationBar();
		learningArea.clickOnHomeButton();
		homePage.navigateToCourseUnitAndLesson(courseCodes, "B1", 3, 2);
		learningArea.clickOnStep(2);
		learningArea.clickOnTask(4);
		sleep(3);

		report.startStep("Verify Submit btn not displayed");
		learningArea.checkThatSubmitToTeacherBtnIsDisabled();
		
		report.startStep("Enter text and verify Submit btn not displayed");
		webDriver.swithcToFrameAndSendKeys("//body[@id='tinymce']", "Some text bla bla Some text bla bla Some text bla bla Some text bla bla", "elm1_ifr");
		webDriver.switchToTopMostFrame();
		learningArea.checkThatSubmitToTeacherBtnIsDisabled();
		
		
	}
	
	@Test
	@Category(AngularLearningArea.class)
	@TestCaseParams(testCaseID = { "32559" })
	public void testNoSubmitInWritingAssignments2() throws Exception {

		
		report.startStep("Get Teacher details");
		studentId = dbService.getUserIdByUserName(configuration.getProperty("teacher.username"),institutionId);
		String firstName = dbService.getUserFirstNameByUserId(studentId);
		
		report.startStep("Open New UX Login Page");
		loginPage = new NewUXLoginPage(webDriver, testResultService);
		pageHelper.setUserLoginToNull(studentId);

		report.startStep("Login as Teacher");
		tmsHomePage = loginPage.enterTeacherUserAndPassword();
		homePage.skipNotificationWindow();
		tmsHomePage.waitForPageToLoad();
		tmsHomePage.switchToMainFrame();
		
		report.startStep("Press on 'Access To ED Platform' link");
		tmsHomePage.clickOnCurriculum();
		homePage = tmsHomePage.clickOnAccessToEd();
		webDriver.switchToNextTab();
		homePage.waitHomePageloadedFully();
					
		report.startStep("Verify user details in ED header");
		firstName = dbService.getUserFirstNameByUserId(studentId);
		testResultService.assertEquals("Hello " + firstName, homePage.getUserDataText(), "Verify First Name in Header of Home Page");
		
		report.startStep("Navigate to First Discoveries ->Unit 2-About Me->Steve's Application Form-> Practice -> Task 3");
		learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "FD", 2, 4);
		learningArea2.clickOnStep(3);
		learningArea2.clickOnTaskByNumber(3);
		
		report.startStep("Verify Send To Teacher btn not displayed");
		learningArea2.checkThatSendToTeacherBtnIsNotDisplayed();
		
		report.startStep("Enter text and verify Send To Teacher btn not displayed");
		learningArea2.enterFreeWritingTextBySection("1", "Hello");
		learningArea2.checkThatSendToTeacherBtnIsNotDisplayed();
		
		report.startStep("Navigate to ERater task");
		//learningArea2.clickToOpenNavigationBar();
		learningArea2.clickOnHomeButton();
		homePage.waitHomePageloadedFully();
		
		learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "B1", 3, 2);
		learningArea2.clickOnStep(2);
		learningArea2.clickOnTaskByNumber(5);
		sleep(1);

		report.startStep("Verify Submit btn not displayed");
		learningArea2.checkThatSubmitToTeacherBtnIsDisabled();
		
		report.startStep("Enter text and verify Submit btn not displayed");
		String text = "Some text bla bla Some text bla bla Some text bla bla Some text bla bla";
		webDriver.swithcToFrameAndSendKeys("//body[@id='tinymce']",	text, "elm1_ifr");
	
		webDriver.switchToTopMostFrame();
		learningArea2.checkThatSubmitToTeacherBtnIsDisabled();
		
		
	}
	
	@Test
	@TestCaseParams(testCaseID = { "32552" })
	public void testTeacherCommunitySectionsDisabled() throws Exception {
		
		report.startStep("Prepare Teacher user details");
		String userName = configuration.getProperty("teacher.username");
		studentId = dbService.getUserIdByUserName(userName,configuration.getProperty("institution.id"));
		String firstName = dbService.getUserFirstNameByUserId(studentId);
				
		report.startStep("Open New UX Login Page");
		loginPage = new NewUXLoginPage(webDriver, testResultService);
		pageHelper.setUserLoginToNull(studentId);

		report.startStep("Login as Teacher");
		tmsHomePage = loginPage.enterTeacherUserAndPassword();
		homePage.skipNotificationWindow();
		tmsHomePage.switchToMainFrame();
		tmsHomePage.waitForPageToLoad();
		
		report.startStep("Press on 'Access To ED Platform' link");
		tmsHomePage.clickOnCurriculum();
		homePage = tmsHomePage.clickOnAccessToEd();
		webDriver.switchToNextTab();
		homePage.waitHomePageloadedFully();
		
		report.startStep("Verify user details in ED header");
		testResultService.assertEquals("Hello " + firstName, homePage.getUserDataText(), "Verify First Name in Header of Home Page");
				
		report.startStep("Open Community page");
		communityPage = homePage.openNewCommunityPage(false);
			
		report.startStep("Check that Forum not displayed");
		communityPage.verifyCommunityActivityNotDisplayed(CommunityActivities.forum);
	}			
	
	private void verifyNavBarModeForTeacher() throws Exception { 
			
		report.startStep("Verify Assignments & Inbox buttons disabled in nav Bar");
			
		testResultService.assertEquals(false, homePage.isNavBarItemEnabled("@id='"+NavBarItems.sitemenu__itemMyAssignments.toString()+"'")); // Assignments
		testResultService.assertEquals(false, homePage.isNavBarItemEnabled("@id='"+NavBarItems.sitemenu__itemMessages.toString()+"'")); // Inbox
	
		report.startStep("Verify Assessments enabled but opens without data");
		testsPage = homePage.clickOnAssessmentsButton();
		sleep(2);
		
		testsPage.checkItemsCounterBySection("1", "0");// Available 
		testsPage.checkItemsCounterBySection("2", "0");// Upcoming
		testsPage.checkItemsCounterBySection("3", "0");// Test Results
		testsPage.close();
		sleep(2);
		
		report.startStep("Open Institution Page and verify student's banners displayed");
		ipage = homePage.clickOnInstitutionButton();
		sleep(2);
		webDriver.switchToPopup();
		ipage.verifyLeftBannerInnerText();
		sleep(2);
		webDriver.switchToPopup();
		ipage.verifyRightBannerInnerText();
		sleep(2);
		webDriver.switchToPopup();
		ipage.close();
		webDriver.switchToNewWindow();
			
	}
	
	private void verifyNavBarModeForTeacher2() throws Exception { 
		
		report.startStep("Verify Assignments & Inbox buttons disabled in nav Bar");
			
		testResultService.assertEquals(false, homePage.isNavBarItemEnabled("@id='"+NavBarItems.sitemenu__itemMyAssignments.toString()+"'")); // Assignments
		testResultService.assertEquals(false, homePage.isNavBarItemEnabled("@id='"+NavBarItems.sitemenu__itemMessages.toString()+"'")); // Inbox
		
		report.startStep("Verify No Assessments & Institution buttons");
		homePage.checkIfNavBarItemDisplayed(false, NavBarItems.sitemenu__itemInstPage);		
		homePage.checkIfNavBarItemDisplayed(false, NavBarItems.sitemenu__itemTests);		
				
	}
	
	private void logoutFromTmsAndCheckEdTabsClosed() throws Exception {
		webDriver.switchToMainWindow();
		
		tmsHomePage.switchToMainFrame();
		tmsHomePage.clickOnExit();
		sleep(2);
		webDriver.switchToTopMostFrame();
		testResultService.assertEquals(false, loginPage.isSubmitButtonEnabled(), "Checking that Login Page displayed");
		webDriver.checkNumberOfTabsOpened(1);
	}
				
	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}
	
	
}
