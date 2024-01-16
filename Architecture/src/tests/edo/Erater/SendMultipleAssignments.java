package tests.edo.Erater;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pageObjects.EdoHomePage;
import Objects.Course;
import tests.misc.EdusoftWebTest;

public class SendMultipleAssignments extends EdusoftWebTest {

	EdoHomePage edoHomePage;
	private String textFile;

	@Before
	public void setup() throws Exception {
		super.setup();
	}

	
	public void sendMultiAssignments() throws Exception {
		report.startStep("Load courses");
		textFile = "files/assayFiles/text24.txt";

		List<Course> courses = pageHelper.getCourses();
		report.finishStep();

		report.startStep("Login as student");
		edoHomePage = pageHelper.loginAsStudent();
		report.finishStep();

		report.startStep("Select course and submit assignment");

		for (int i = 0; i < courses.size(); i++) {
			sendAssignmentAsStudent(courses.get(i));
		}

		report.finishStep();

	}

	public void sendAssignmentAsStudent(Course course) throws Exception {
		report.startStep("Send writing assignment to E-Rater");
		System.out.println("Unit componenets is: "
				+ course.getName()
				+ " "

				+ course.getCourseUnits().get(0).getName()
				+ " "
				+ course.getCourseUnits().get(0).getUnitComponent().get(0)
						.getName());

		edoHomePage.clickOnCourses();
		sleep(2);
		edoHomePage.clickOnCourseByName(course.getName());
		edoHomePage.waitForCourseDetailsToBeDisplayed(course.getName());
		String courseUnit = course.getCourseUnits().get(0).getName();
		edoHomePage.clickOnCourseUnit(courseUnit);
		String unitComponent = course.getCourseUnits().get(0)
				.getUnitComponent().get(0).getName();
		edoHomePage.clickOntUnitComponent(unitComponent, "Practice");
		Thread.sleep(5000);
		int unitStage = Integer.valueOf(course.getCourseUnits().get(0)
				.getUnitComponent().get(0).getStageNumber());
		edoHomePage.ClickOnComponentsStage(unitStage);
		sleep(3);
		String timeStampe = dbService.sig(10);
		edoHomePage.submitWritingAssignment(textFile, textService, timeStampe);

		report.finishStep();

		report.startStep("start checking the xml and json");
		String userId = dbService.getUserIdByUserName(pageHelper.getStudent()
				.getUserName(), autoInstitution.getInstitutionId());
		// String textStart = textService.getFirstCharsFromCsv(10, textFile);
		String writingId = eraterService.getWritingIdByUserIdAndTextStart(
				userId, timeStampe);
		// writingIdForDelete.add(writingId);
		report.report("writing id is: " + writingId);
		eraterService.compareJsonAndXmlByWritingId(writingId);
		report.finishStep();

		report.startStep("Wait for E-Rater feedback and send to teacher");
		edoHomePage.clickOnMyAssignments();
		edoHomePage.switchToAssignmentsFrame();
		edoHomePage.clickOnWritingAssignmentsTab(course.getName());
		// edoHomePage.clickToViewAssignment(courseName);
		edoHomePage.clickOnSeeFeedbackAndTryAgsin();
		edoHomePage.switchToAssignmentsFrame();
		edoHomePage.clickOnFeedbackMoreDetails();
		String newText = "this is new text";
		edoHomePage.editFeedbackAssignmentText(newText);

		edoHomePage.clickOnFeedbackSubmitBtn();
		report.finishStep();

	}

	@After
	public void tearDowb() throws Exception {
		super.tearDown();
	}

}
