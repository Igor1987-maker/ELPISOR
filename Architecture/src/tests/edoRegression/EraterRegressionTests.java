package tests.edoRegression;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pageObjects.EdoHomePage;
import pageObjects.tms.TmsHomePage;
import tests.misc.EdusoftWebTest;
import Interfaces.TestCaseParams;
import Objects.Course;
import Objects.Student;

public class EraterRegressionTests extends EdusoftWebTest {

	List<Course> courses = null;
	List<String> writingIdForDelete = new ArrayList<String>();

	TmsHomePage tmsHomePage;
	String writingId = null;
	String newWritingId = null;

	@Before
	public void setup() throws Exception {
		super.setup();
		courses = pageHelper.getCourses();
		report.report("delete all student assignments");
		String userId = dbService.getUserIdByUserName(
				configuration.getStudentUserName(),
				autoInstitution.getInstitutionId());
		// eraterService.deleteStudentAssignments(userId);

		report.report("Set institution to 'Teacher first' settings");
		eraterService.setEraterTeacherLast();
		report.stopLevel();
	}

	// @Test
	// public void
	// testSubmitTextAsStudentAndCheckFeedbackAsTeacherAndSendAgainChrome()
	// throws Exception {
	// testSubmitTextAsStudentAndCheckFeedbackAsTeacherAndSendAgain(webDriver);
	// }
	//
	// @Test
	// public void
	// testSubmitTextAsStudentAndCheckFeedbackAsTeacherAndSendAgainFirefox()
	// throws Exception {
	// testSubmitTextAsStudentAndCheckFeedbackAsTeacherAndSendAgain(webDriver);
	// }
	// @Test
	// public void
	// testSubmitTextAsStudentAndCheckFeedbackAsTeacherAndSendAgainIE()
	// throws Exception {
	// testSubmitTextAsStudentAndCheckFeedbackAsTeacherAndSendAgain(webDriver);
	// }

	// Test case id:15621
	@Test
	@TestCaseParams(testCaseID = "15621")
	public void testSubmitTextAsStudentAndCheckFeedbackAsTeacherAndSendAgain()
			throws Exception {

		int courseId = 2;
		String textFile = "files/assayFiles/text24.txt";
		Student student = new Student();

		EdoHomePage edoHomePage = null;

		if (configuration.getAutomationParam("dbaccess", "dbaccess").equals(
				"false")) {
			edoHomePage = pageHelper.loginAsStudent();
		} else {
			startStep("Create a student for the test");
			String StudentUserName = "student" + dbService.sig(6);
			StudentUserName = dbService.getUserNameById(
					pageHelper.createUSerUsingSP(),
					configuration.getInstitutionId());
			startStep("Login to Edo");

			report.report("using file: " + textFile);

			student.setUserName(StudentUserName);
			student.setPassword(configuration.getStudentPassword());
			student.setId(dbService.getUserIdByUserName(StudentUserName,
					autoInstitution.getInstitutionId()));
			edoHomePage = pageHelper.loginAsStudent(student);
		}
		startStep("Open home page and start a writing drill");
		String courseName = courses.get(courseId).getName();
		sleep(3);
		edoHomePage.clickOnCourses();
		edoHomePage.clickOnCourseByName(courseName);
		sleep(3);
		edoHomePage.waitForCourseDetailsToBeDisplayed(courseName);
		String courseUnit = courses.get(courseId).getCourseUnits().get(0)
				.getName();
		edoHomePage.clickOnCourseUnit(courseUnit);
		String unitComponent = courses.get(courseId).getCourseUnits().get(0)
				.getUnitComponent().get(0).getName();
		edoHomePage.clickOntUnitComponent(unitComponent, "Practice");
		Thread.sleep(5000);
		int unitStage = Integer.valueOf(courses.get(courseId).getCourseUnits()
				.get(0).getUnitComponent().get(0).getStageNumber());
		edoHomePage.ClickOnComponentsStage(unitStage);
		sleep(3);
		String textStart = dbService.sig(8);
		edoHomePage.submitWritingAssignment(textFile, textService, textStart);

		startStep("start checking the xml and json");

		String userId = dbService.getUserIdByUserName(student.getUserName(),
				autoInstitution.getInstitutionId());

		if (!configuration.getAutomationParam("dbaccess", "dbaccess").equals(
				"false")) {

			writingId = eraterService.getWritingIdByUserIdAndTextStart(userId,
					textStart);
			writingIdForDelete.add(writingId);
			report.report("writing id is: " + writingId);
			eraterService.compareJsonAndXmlByWritingId(writingId);
		}
		edoHomePage.waitForPageToLoad();
		edoHomePage.clickOnCourses();
		sleep(3);

		startStep("Check the Erater feedback as a student and send again");
		edoHomePage.clickOnMyAssignments();
		edoHomePage.switchToAssignmentsFrame();
		edoHomePage.clickOnWritingAssignmentsTab(courseName);
		edoHomePage.clickOnSeeFeedbackAndTryAgsin();
		edoHomePage.switchToAssignmentsFrame();
		edoHomePage.clickOnFeedbackMoreDetails();
		String newText = "this is new text";
		edoHomePage.editFeedbackAssignmentText(newText);

		edoHomePage.clickOnFeedbackSubmitBtn();

		startStep("wait for writing to be processed again");
		eraterService.getWritingIdByUserIdAndTextStart(userId, textStart, true);

		startStep("Login as teacher and send feedback to the student");
		webDriver.deleteCookiesAndRefresh();
		pageHelper.loginAsTeacher();
		webDriver.closeAlertByAccept();
		tmsHomePage = (TmsHomePage) edoHomePage.openTeachersCorner();
		newWritingId = eraterService.getWritingIdByUserIdAndTextStart(
				student.getId(), newText);
		eraterService.checkWritingIsProcessed(newWritingId);
		tmsHomePage.clickOnWritingAssignments();
		sleep(2);
		tmsHomePage.clickOnStudentAssignment(student.getUserName(), courseName);
		sleep(2);
		tmsHomePage.clickOnAssignmentSummary();
		sleep(2);
		int rating = 1;
		sendTeacherFeedback(false);
		// ***********************

		Thread.sleep(2000);

		startStep("Login again as student and check the feedback from the teacher");
		webDriver.quitBrowser();
		webDriver.init();
		webDriver.openUrl(getSutAndSubDomain());
		pageHelper.loginAsStudent(student);
		sleep(2);

		edoHomePage.clickOnMyAssignments();

		edoHomePage.switchToAssignmentsFrame();
		edoHomePage.clickOnWritingAssignmentsTab(courseName);
		sleep(2);
		edoHomePage.clickOnSeeFeedback();
		edoHomePage.switchToAssignmentsFrame();
		sleep(3);
		edoHomePage.checkRatingFromTeacher(rating);

	}

	//
	@Test
	@TestCaseParams(testCaseID = "")
	public void testSaveAssignmentAndSend() throws Exception {

		EdoHomePage edoHomePage = null;
		Student student = new Student();
		startStep("Create a student for the test");
		String StudentUserName = "student" + dbService.sig(6);
//		pageHelper.addStudent(StudentUserName);
		StudentUserName = dbService.getUserNameById(
				pageHelper.createUSerUsingSP(),
				configuration.getInstitutionId());
		
		if (configuration.getAutomationParam("dbaccess", "dbaccess").equals(
				"false")) {
			edoHomePage = pageHelper.loginAsStudent();
			student.setUserName(pageHelper.getStudent().getUserName());
		} else {

			student.setUserName(StudentUserName);
			student.setPassword(configuration.getStudentPassword());
			student.setId(dbService.getUserIdByUserName(StudentUserName,
					autoInstitution.getInstitutionId()));

			startStep("Login as student and enter some text");
			edoHomePage = pageHelper.loginAsStudent(student);
		}
		sleep(2);

		String addedText = textService.getTextFromFile(
				"files/assayFiles/text2.txt", Charset.defaultCharset());

		edoHomePage.addWritingAssignmentWithoutSubmitting(courses.get(3),
				addedText, textService);

		startStep("Save and leave the writing assignment");
		edoHomePage.clickToSaveAssignment();
		sleep(3);
		edoHomePage.clickOnLastComponent(1);
		Thread.sleep(3000);

		startStep("Move back to the writing assignment and check the text");
		edoHomePage.clickOnNextComponent(1);
		Thread.sleep(3000);

		startStep("Send the assignment");
		edoHomePage.clickOnSubmitAssignment();

		startStep("Check the assignment in the DB");
		String userId = dbService.getUserIdByUserName(student.getUserName(),
				autoInstitution.getInstitutionId());
		if (!configuration.getAutomationParam("dbaccess", "dbaccess").equals(
				"false")) {
			Assert.assertNotNull("validate writing id in DB",
					eraterService.getWritingIdByUserId(userId));
		}

	}

	// Test case: 13569,13572,13573
	@Test
	@TestCaseParams(testCaseID = { "13569", "13572", "13573" })
	public void testAddTeacherCommentToStudentAssignment() throws Exception {
		Student student = new Student();

		startStep("Create a student for the test");
		String studentUserName = "student" + dbService.sig(6);
		EdoHomePage edoHomePage = new EdoHomePage(webDriver, testResultService);

		if (configuration.getAutomationParam("dbaccess", "dbaccess").equals(
				"false")) {
			edoHomePage = pageHelper.loginAsStudent();
			student.setUserName(pageHelper.getStudent().getUserName());
		} else {
			// pageHelper.addStudent(studentUserName);
			studentUserName = dbService.getUserNameById(
					pageHelper.createUSerUsingSP(),
					configuration.getInstitutionId());

			student.setUserName(studentUserName);
			student.setPassword(configuration.getStudentPassword());
			student.setId(dbService.getUserIdByUserName(studentUserName,
					autoInstitution.getInstitutionId()));
			edoHomePage = pageHelper.loginAsStudent(student);
		}

		startStep("Login to Edo");
		int courseId = 3;
		String textFile = "files/assayFiles/text24.txt";
		report.report("using file: " + textFile);

		startStep("Open home page and start a writing drill");
		String courseName = courses.get(courseId).getName();
		sleep(2);
		edoHomePage.clickOnCourses();
		sleep(5);
		edoHomePage.clickOnCourseByName(courseName);
		edoHomePage.waitForCourseDetailsToBeDisplayed(courseName);
		String courseUnit = courses.get(courseId).getCourseUnits().get(0)
				.getName();
		edoHomePage.clickOnCourseUnit(courseUnit);
		sleep(2);
		String unitComponent = courses.get(courseId).getCourseUnits().get(0)
				.getUnitComponent().get(0).getName();
		edoHomePage.clickOntUnitComponent(unitComponent, "Practice");
		Thread.sleep(5000);
		int unitStage = Integer.valueOf(courses.get(courseId).getCourseUnits()
				.get(0).getUnitComponent().get(0).getStageNumber());
		edoHomePage.ClickOnComponentsStage(unitStage);
		sleep(3);
		String textStart = dbService.sig(8);
		edoHomePage.submitWritingAssignment(textFile, textService, textStart);

		startStep("start checking the xml and json");
		String userId = dbService.getUserIdByUserName(student.getUserName(),
				autoInstitution.getInstitutionId());
		writingId = eraterService.getWritingIdByUserIdAndTextStart(userId,
				textStart);
		writingIdForDelete.add(writingId);
		report.report("writing id is: " + writingId);
		eraterService.compareJsonAndXmlByWritingId(writingId);
		edoHomePage.waitForPageToLoad();
		edoHomePage.clickOnCourses();

		startStep("Check the Erater feedback as a student and send again");
		edoHomePage.clickOnMyAssignments();
		edoHomePage.switchToAssignmentsFrame();
		edoHomePage.clickOnWritingAssignmentsTab(courseName);
		edoHomePage.clickOnSeeFeedbackAndTryAgsin();
		edoHomePage.switchToAssignmentsFrame();
		edoHomePage.clickOnFeedbackMoreDetails();
		String newText = "this is new text";
		edoHomePage.editFeedbackAssignmentText(newText);
		edoHomePage.clickOnFeedbackSubmitBtn();

		startStep("Login as teacher and send feedback to the student");
		webDriver.deleteCookiesAndRefresh();
		sleep(2);
		edoHomePage = pageHelper.loginAsTeacher();
		edoHomePage.waitForPageToLoad();
		tmsHomePage = (TmsHomePage) edoHomePage.openTeachersCorner();
		newWritingId = eraterService.getWritingIdByUserIdAndTextStart(
				student.getId(), textStart);
		eraterService.checkWritingIsProcessed(newWritingId);
		tmsHomePage.clickOnWritingAssignments();
//		tmsHomePage.sortWritingAssignmentsByDateDesc();
		tmsHomePage.clickOnStudentAssignment(student.getUserName(), courseName);
		// ********************

		startStep("Check that the Add button is disabled when no comment is selected");
		sleep(3);
		tmsHomePage.clickOnXFeedback();
		tmsHomePage.checkAddCommentButtonStatus(true);

		startStep("Click the Add buttom, select a plave and add a comment");
		tmsHomePage.clickAddCommentButton();
		tmsHomePage.clickOnTextArea(20, 20);
		String commentText = "comment" + dbService.sig(5);
		tmsHomePage.enterTeacherCommentText(commentText);
		tmsHomePage.clickAddCommentDoneButton();
		sendTeacherFeedback(true);

		startStep("Login as student and check that the commnet is not displayed");
		webDriver.quitBrowser();
		webDriver.init();
		pageHelper.loginAsStudent();
		sleep(5);
		webDriver.printScreen("after student login");
		edoHomePage.clickOnMyAssignments();
		sleep(3);
		edoHomePage.switchToAssignmentsFrame();
		edoHomePage.clickOnWritingAssignmentsTab(courseName);
		// edoHomePage.clickToViewAssignment(courseName);
		edoHomePage.clickOnSeeFeedback();
		edoHomePage.switchToAssignmentsFrame();
		Thread.sleep(3000);
		edoHomePage.clickOnFeedbackMoreDetails();
		sleep(2);
		edoHomePage.checkForTeacherComment("t0_1_0", commentText);

	}

	// public void testRemoveComment() throws Exception {
	// startStep("Create a student for the test");
	// String StudentUserName = "student" + dbService.sig(6);
	// pageHelper.addStudent(StudentUserName);
	//
	// startStep("Login to Edo");
	// int courseId = 3;
	// String textFile = "files/assayFiles/text24.txt";
	// String commentID = "0_0";
	// report.report("using file: " + textFile);
	// // EdoLoginPage edoLoginPage = new EdoLoginPage(webDriver);
	// // edoLoginPage.OpenPage(getSutAndSubDomain());
	// //
	// // Student student = new Student();
	// // student.setUserName(configuration.getStudentUserName());
	// // student.setPassword(configuration.getStudentPassword());
	// // student.setId(dbService.getUserIdByUserName(student.getUserName(),
	// // autoInstitution.getInstitutionId()));
	// // EdoHomePage edoHomePage = edoLoginPage.login(student);
	// Student student = new Student();
	// student.setUserName(StudentUserName);
	// student.setPassword(configuration.getStudentPassword());
	// student.setId(dbService.getUserIdByUserName(StudentUserName,
	// autoInstitution.getInstitutionId()));
	// EdoHomePage edoHomePage = pageHelper.loginAsStudent(student);
	//
	// startStep("Open home page and start a writing drill");
	// String courseName = courses.get(courseId).getName();
	// sleep(2);
	// edoHomePage.clickOnCourses();
	// sleep(5);
	// edoHomePage.clickOnCourseByName(courseName);
	// edoHomePage.waitForCourseDetailsToBeDisplayed(courseName);
	// String courseUnit = courses.get(courseId).getCourseUnits().get(0)
	// .getName();
	// edoHomePage.clickOnCourseUnit(courseUnit);
	// String unitComponent = courses.get(courseId).getCourseUnits().get(0)
	// .getUnitComponent().get(0).getName();
	// edoHomePage.clickOntUnitComponent(unitComponent, "Practice");
	// Thread.sleep(5000);
	// int unitStage = Integer.valueOf(courses.get(courseId).getCourseUnits()
	// .get(0).getUnitComponent().get(0).getStageNumber());
	// edoHomePage.ClickOnComponentsStage(unitStage);
	// sleep(3);
	// String textStart = dbService.sig(8);
	// edoHomePage.submitWritingAssignment(textFile, textService, textStart);
	// // System.out.println("sleeping for 60 seconds");
	// // Thread.sleep(60000);
	//
	// startStep("start checking the xml and json");
	// String userId = dbService.getUserIdByUserName(student.getUserName(),
	// autoInstitution.getInstitutionId());
	// // String textStart = textService.getFirstCharsFromCsv(10, textFile);
	// writingId = eraterService.getWritingIdByUserIdAndTextStart(userId,
	// textStart);
	// writingIdForDelete.add(writingId);
	// report.report("writing id is: " + writingId);
	// eraterService.compareJsonAndXmlByWritingId(writingId);
	// edoHomePage.waitForPageToLoad();
	// edoHomePage.clickOnCourses();
	//
	// startStep("Check the Erater feedback as a student and send again");
	// edoHomePage.clickOnMyAssignments();
	// edoHomePage.switchToAssignmentsFrame();
	// edoHomePage.clickOnWritingAssignmentsTab(courseName);
	// // edoHomePage.clickToViewAssignment(courseName);
	// edoHomePage.clickOnSeeFeedbackAndTryAgsin();
	// edoHomePage.switchToAssignmentsFrame();
	// edoHomePage.clickOnFeedbackMoreDetails();
	// String newText = "this is new text";
	// edoHomePage.editFeedbackAssignmentText(newText);
	//
	// edoHomePage.clickOnFeedbackSubmitBtn();
	//
	// startStep("Login as teacher and send feedback to the student");
	// webDriver.deleteCookiesAndRefresh();
	// // edoLoginPage.OpenPage(getSutAndSubDomain());
	// // edoHomePage = edoLoginPage.login(teacher);
	// edoHomePage = pageHelper.loginAsTeacher();
	// edoHomePage.waitForPageToLoad();
	// sleep(3);
	// tmsHomePage = edoHomePage.openTeachersCorner();
	// newWritingId = eraterService.getWritingIdByUserIdAndTextStart(
	// student.getId(), textStart);
	// eraterService.checkWritingIsProcessed(newWritingId);
	// tmsHomePage.clickOnWritingAssignments();
	//
	// tmsHomePage.clickOnStudentAssignment(student.getUserName(), courseName);
	// // ********************
	// startStep("Check that the Add button is disabled when no comment is selected");
	// sleep(3);
	// tmsHomePage.clickOnXFeedback();
	// // webDriver.switchToFrame("cboxIframe");
	//
	// tmsHomePage.swithchToMainFrame();
	// tmsHomePage.swithchToCboxFrame();
	// tmsHomePage.clickOnTextArea(20, 20);
	// sleep(2);
	// tmsHomePage.checkRemoveCommentButtonStatus(true, false);
	// // tmsHomePage.clickOnRemoveCommentButton();
	//
	//
	// // ****************************
	// // startStep("Send the feedback to the student");
	// // tmsHomePage.clickOnTeacherFeedbackContinueButton();
	// // tmsHomePage.clickOnRateAssignmentButton();
	// // int rating = 1;
	// // tmsHomePage.rateAssignment(rating);
	// // Thread.sleep(2000);
	// // tmsHomePage.clickOnApproveAssignmentButton();
	// // sleep(4);
	// // tmsHomePage.sendFeedback();
	// // sleep(4);
	// sendTeacherFeedback(true);
	//
	// startStep("Login as student and check that the commnet is not displayed");
	// webDriver.quitBrowser();
	// webDriver.init(testResultService);
	// pageHelper.loginAsStudent();
	// sleep(5);
	// webDriver.printScreen("after student login");
	// edoHomePage.clickOnMyAssignments();
	// sleep(3);
	// edoHomePage.switchToAssignmentsFrame();
	// edoHomePage.clickOnWritingAssignmentsTab(courseName);
	// // edoHomePage.clickToViewAssignment(courseName);
	// edoHomePage.clickOnSeeFeedback();
	// edoHomePage.switchToAssignmentsFrame();
	// Thread.sleep(3000);
	// edoHomePage.clickOnFeedbackMoreDetails();
	// sleep(2);
	// // edoHomePage.clickOnWritingAssignmentsTab(courseName);
	// // webDriver.switchToFrame(webDriver.waitForElement(
	// // "//iframe[@class='cboxIframe']", ByTypes.xpath));
	//
	// edoHomePage.clickOnTextAreaAndCheckThatCommentIsNotDislayed();
	//
	// }

	// Test case id:13268,13265
	@Test
	@TestCaseParams(testCaseID = { "13268", "13265" })
	public void testRemoveCommentAsTeacher() throws Exception {
		startStep("Create a student for the test");
		String StudentUserName = "student" + dbService.sig(6);
		StudentUserName = dbService.getUserNameById(
				pageHelper.createUSerUsingSP(),
				configuration.getInstitutionId());

		startStep("Login to Edo");
		int courseId = 3;
		String textFile = "files/assayFiles/text24.txt";
		String commentID = "0_0";
		report.report("using file: " + textFile);
		Student student = new Student();
		student.setUserName(StudentUserName);
		student.setPassword(configuration.getStudentPassword());
		student.setId(dbService.getUserIdByUserName(StudentUserName,
				autoInstitution.getInstitutionId()));
		EdoHomePage edoHomePage = pageHelper.loginAsStudent(student);

		startStep("Open home page and start a writing drill");
		String courseName = courses.get(courseId).getName();
		edoHomePage.clickOnCourses();
		edoHomePage.clickOnCourseByName(courseName);
		edoHomePage.waitForCourseDetailsToBeDisplayed(courseName);
		String courseUnit = courses.get(courseId).getCourseUnits().get(0)
				.getName();
		edoHomePage.clickOnCourseUnit(courseUnit);
		String unitComponent = courses.get(courseId).getCourseUnits().get(0)
				.getUnitComponent().get(0).getName();
		edoHomePage.clickOntUnitComponent(unitComponent, "Practice");
		Thread.sleep(5000);
		int unitStage = Integer.valueOf(courses.get(courseId).getCourseUnits()
				.get(0).getUnitComponent().get(0).getStageNumber());
		edoHomePage.ClickOnComponentsStage(unitStage);
		sleep(3);
		edoHomePage.submitWritingAssignment(textFile, textService);
		// System.out.println("sleeping for 60 seconds");
		// Thread.sleep(60000);

		startStep("start checking the xml and json");
		String userId = dbService.getUserIdByUserName(student.getUserName(),
				autoInstitution.getInstitutionId());
		String textStart = textService.getFirstCharsFromCsv(10, textFile);
		String writingId = eraterService.getWritingIdByUserIdAndTextStart(
				userId, textStart);
		writingIdForDelete.add(writingId);
		report.report("writing id is: " + writingId);
		eraterService.compareJsonAndXmlByWritingId(writingId);
		edoHomePage.waitForPageToLoad();
		edoHomePage.clickOnCourses();

		startStep("Check the Erater feedback as a student and send again");
		edoHomePage.clickOnMyAssignments();
		edoHomePage.switchToAssignmentsFrame();
		edoHomePage.clickOnWritingAssignmentsTab(courseName);
		// edoHomePage.clickToViewAssignment(courseName);
		edoHomePage.clickOnSeeFeedbackAndTryAgsin();
		edoHomePage.switchToAssignmentsFrame();
		edoHomePage.clickOnFeedbackMoreDetails();
		String newText = student.getId()+" this is new text";
		edoHomePage.editFeedbackAssignmentText(newText);

		edoHomePage.clickOnFeedbackSubmitBtn();

		startStep("Login as teacher and send feedback to the student");
		webDriver.deleteCookiesAndRefresh();
		// edoLoginPage.OpenPage(getSutAndSubDomain());
		// edoHomePage = edoLoginPage.login(teacher);
		edoHomePage = pageHelper.loginAsTeacher();
		edoHomePage.waitForPageToLoad();
		TmsHomePage tmsHomePage = (TmsHomePage) edoHomePage
				.openTeachersCorner();
		String newWritingId = eraterService.getWritingIdByUserIdAndTextStart(
				student.getId(), newText);
		eraterService.checkWritingIsProcessed(newWritingId);
		tmsHomePage.clickOnWritingAssignments();

		tmsHomePage.clickOnStudentAssignment(student.getUserName(), courseName);

		startStep("Check that the Remove button is disabled when no comment is selected");
		tmsHomePage.clickOnXFeedback();
		tmsHomePage.checkRemoveCommentButtonStatus(false, true);

		startStep("Select a comment and check the remove button");
		String commentText = tmsHomePage.selectFeedbackComment(commentID);
		tmsHomePage.checkRemoveCommentButtonStatus(true, false);

		startStep("click the remove button and check the texts");
		tmsHomePage.clickTheRemoveCommentButton();
		tmsHomePage.checkIfCommentedTextIsInderlined(commentID, false);
		tmsHomePage.checkCommentTitle(false);

		startStep("Send the feedback to the student");
		tmsHomePage.clickOnTeacherFeedbackContinueButton();
		tmsHomePage.clickOnRateAssignmentButton();
		int rating = 1;
		tmsHomePage.rateAssignment(rating);
		Thread.sleep(2000);
		tmsHomePage.clickOnApproveAssignmentButton();
		Thread.sleep(2000);
		tmsHomePage.sendFeedback();
		Thread.sleep(2000);

		startStep("Login as student and check that the commnet is not displayed");
		webDriver.quitBrowser();
		webDriver.init();
		pageHelper.loginAsStudent();
		sleep(5);
		webDriver.printScreen("after student login");
		edoHomePage.clickOnMyAssignments();
		sleep(3);
		edoHomePage.switchToAssignmentsFrame();
		edoHomePage.clickOnWritingAssignmentsTab(courseName);
		// edoHomePage.clickToViewAssignment(courseName);
		edoHomePage.clickOnSeeFeedback();
		edoHomePage.switchToAssignmentsFrame();
		Thread.sleep(3000);
		edoHomePage.clickOnFeedbackMoreDetails();
		sleep(2);

		edoHomePage
				.checkAsStudentFeedbackComment(commentID, false, commentText);

	}

	@After
	public void tearDown() throws Exception {
		// report.report("Delete writings");
		// for (int i = 0; i < writingIdForDelete.size(); i++) {
		// eraterService.deleteWritngFromDb(writingIdForDelete.get(i));
		// }
		super.tearDown();

	}

	public void sendTeacherFeedback(boolean clickOnContinue) throws Exception {
		startStep("Send the feedback to the student");
		if (clickOnContinue) {
			tmsHomePage.clickOnTeacherFeedbackContinueButton();
		}
		sleep(2);
		tmsHomePage.clickOnRateAssignmentButton();
		int rating = 1;
		tmsHomePage.rateAssignment(rating);
		Thread.sleep(2000);
		tmsHomePage.clickOnApproveAssignmentButton();
		sleep(4);
		tmsHomePage.sendFeedback();
		sleep(1);
		tmsHomePage.sendFeedback();
		sleep(6);
		webDriver.printScreen("After clicking send to all");
		eraterService.checkWritingIsReviewed(newWritingId);

	}

}
