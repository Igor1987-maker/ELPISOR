package tests.edo.newux;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import pageObjects.edo.NewUXLoginPage;
import pageObjects.edo.NewUxHomePage;
import pageObjects.edo.NewUxMyProgressPage;
import testCategories.reg2;
import testCategories.edoNewUX.CourseArea;
import testCategories.edoNewUX.GeneralFeatures;
import Enums.ByTypes;
import Interfaces.TestCaseParams;
@Category(reg2.class)
public class LockCourseUnitsLessons extends BasicNewUxTest {

	NewUxMyProgressPage myProgressPage;

	int lockedCourseNum = 1;
	int lockedUnitNum = 1;
	int lockedLessonNum = 1;

	String lockedCourseID = courses[lockedCourseNum - 1];
	int lockedUnitIndex = lockedUnitNum - 1;
	int lockedLessonIndex = lockedLessonNum - 1;

	@Test
	@Category(CourseArea.class)
	@TestCaseParams(testCaseID = { "20834" }, testMultiple = true)
	public void LockCourse() throws Exception {

		report.startStep("Create new student");
		String className = configuration.getProperty("classname.locked");
		String studentId = pageHelper.createUSerUsingSP(
				institutionId, className);

		String userName = dbService.getUserNameById(studentId,
				institutionId);

		report.startStep("Login with new student");
		NewUXLoginPage loginPage = new NewUXLoginPage(webDriver,
				testResultService);
		NewUxHomePage homePage = loginPage.loginAsStudent(userName, "12345");

		report.startStep("Unassign course");
		pageHelper.LockCourseToClass(className, lockedCourseID);
		String[] courseLock = ArrayUtils.removeElement(courses, lockedCourseID);
		homePage.waitHomePageloadedFully();
		
		report.startStep("Check course locked");
		for (int i = 0; i < courseLock.length; i++) {
			String actualCourse = homePage.getCurrentCourseElement(i + 1)
					.getText();
			String expectedCourse = dbService.getCourseNameById(courseLock[i])
					.trim();
			testResultService.assertEquals(expectedCourse, actualCourse);
			homePage.carouselNavigateNext();
			sleep(2);
		}

		report.startStep("Assign all courses");
		pageHelper.UnlockCourseUnitLessonsToClass(className, lockedCourseID, 1);
		webDriver.refresh();

		report.startStep("Check courses assigned");
		for (int i = 0; i < courses.length; i++) {
			String actualCourse = homePage.getCurrentCourseElement(i + 1)
					.getText();
			String expectedCourse = dbService.getCourseNameById(courses[i])
					.trim();
			testResultService.assertEquals(expectedCourse, actualCourse);
			homePage.carouselNavigateNext();
			sleep(3);
		}

		report.startStep("Logout");
		homePage.clickOnLogOut();
	}

	@Test
	@Category(CourseArea.class)
	@TestCaseParams(testCaseID = { "20837", "40221" }, testMultiple = true)
	public void LockUnit() throws Exception {

		report.startStep("Create new student in lockedUnitsClass and login");
		String className = configuration.getProperty("classname.locked");
		homePage = createUserAndLoginNewUXClass(className);
		//report.finishStep();

		report.startStep("Unassign unit");
		pageHelper.LockUnitToClass(className, lockedCourseID, lockedUnitNum);
		sleep(2);
		webDriver.refresh();
		homePage.waitHomePageloaded();
		
		report.startStep("Check unit locked on Home Page");
		for (int i = 1; i < lockedCourseNum; i++) {
			homePage.carouselNavigateNext();
			sleep(3);
		}
		
		String unitLockedLabel = homePage.getUnitLockedElement(lockedUnitNum,true).getAttribute("title");
		testResultService.assertEquals("Locked", unitLockedLabel);
		
		report.startStep("Check unit locked on My Progress Page");
		boolean isLocked = false;
		myProgressPage = homePage.clickOnMyProgress();
		myProgressPage.waitForPageToLoad();
		
		isLocked = myProgressPage.isUnitLocked(lockedUnitNum);
		testResultService.assertEquals(true, isLocked, "Unit not locked though it should be");
		homePage.clickToOpenNavigationBar();
		homePage.clickOnHomeButton();
		
		report.startStep("Assign all units");
		pageHelper.UnlockCourseUnitLessonsToClass(className, lockedCourseID, lockedUnitNum);
		webDriver.refresh();
		homePage.waitHomePageloaded();
		
		report.startStep("Check unit unlocked");
		for (int i = 1; i < lockedCourseNum; i++) {
			homePage.carouselNavigateNext();
			sleep(3);
		}

		WebElement lockedUnitExists = homePage.getUnitLockedElement(lockedUnitNum,false);

		if (lockedUnitExists != null) {
			boolean isLockedUnitExists = true;
			testResultService.assertEquals(false, isLockedUnitExists,
					"Unit still locked after it was unlocked");
		}

		report.startStep("Check unit unlocked on My Progress Page");
		isLocked = true;
		myProgressPage = homePage.clickOnMyProgress();
		myProgressPage.waitForPageToLoad();
		
		isLocked = myProgressPage.isUnitLocked(lockedUnitNum);
		testResultService.assertEquals(false, isLocked, "Unit locked though it should be available");
		
		report.startStep("Logout");
		homePage.clickOnLogOut();

	}

	@Test
	@Category(CourseArea.class)
	@TestCaseParams(testCaseID = { "20874","40225" }, testMultiple = true)
	public void LockLesson() throws Exception {

		String className = configuration.getProperty("classname.locked");
		report.startStep("Unassign Lesson");
		pageHelper.LockLessonToClass(className, lockedCourseID, lockedUnitNum, lockedLessonNum);

		report.startStep("Create new student in lockedUnitsClass and login");
		homePage = createUserAndLoginNewUXClass(className);
		
		report.startStep("Check Lesson locked");
		for (int i = 1; i < lockedCourseNum; i++) {
			homePage.carouselNavigateNext();
			sleep(3);
		}

		homePage.clickOnUnitLessons(lockedUnitNum);
		testResultService.assertEquals(true, homePage.getLessonLockStatus(lockedUnitIndex, lockedLessonNum), "Lesson was not locked");
		
		report.startStep("Check lesson locked on My Progress Page");
		boolean isLocked = false;
		sleep(2);
		myProgressPage = homePage.clickOnMyProgress();
		webDriver.refresh();
		sleep(5);
		myProgressPage.clickToOpenUnitLessonsProgress(lockedUnitNum);
		sleep(5);
		isLocked = myProgressPage.isLessonLocked(lockedLessonNum);
		testResultService.assertEquals(true, isLocked, "Lesson not locked though it should be");
		sleep(5);
		homePage.clickToOpenNavigationBar();
		homePage.clickOnHomeButton();
		homePage.waitHomePageloaded();
		
		report.startStep("Assign all lessons");
		pageHelper.UnlockCourseUnitLessonsToClass(className, lockedCourseID, lockedUnitNum);
		webDriver.refresh();
		homePage.waitHomePageloaded();
		
		report.startStep("Check Lesson unlocked");
		homePage.clickOnUnitLessons(lockedUnitNum);
		testResultService.assertEquals(false, homePage.getLessonLockStatus(lockedUnitIndex, lockedLessonNum), "Lesson was locked");

		report.startStep("Check lesson unlocked on My Progress Page");
		isLocked = true;
		myProgressPage = homePage.clickOnMyProgress();
		webDriver.refresh();
		sleep(1);
		myProgressPage.clickToOpenUnitLessonsProgress(lockedUnitNum);
		sleep(1);
		isLocked = myProgressPage.isLessonLocked(lockedLessonNum);
		testResultService.assertEquals(false, isLocked, "Lesson locked though it should be available");
				
		report.startStep("Logout");
		homePage.clickOnLogOut();

	}
	
	@Test
	@TestCaseParams(testCaseID = { "20880" })
	public void testLessonsDisabledButDisplayedIfUnitIsLocked()
			throws Exception {

		report.startStep("Lock unit 1 in First Discoveries course");

		String className = configuration.getProperty("classname.locked");

		pageHelper.LockUnitToClass(className, courses[0], 1);

		try {
			report.startStep("Create new user and Login to EDO");

			createUserAndLoginNewUXClass(className);

			report.startStep("Show the lessons of the locked unit");
				WebElement element = homePage.getUnitLockedElement(1,true);
				webDriver.scrollToElement(element, 0);
				element.click();
			
			report.startStep("Check that lessons are not clickable");

			for (int i = 0; i < 7; i++) {
				int index = i + 1;
				homePage.checkThatLessonIsNotClickable(0, index);
				report.addTitle("Clicked on Lesson: " + index);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			pageHelper.UnlockCourseUnitLessonsToClass(className, courses[0], 1);
		}

	}

	@After
	public void tearDown() throws Exception {

		super.tearDown();
	}

}