package tests.edo.Erater;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import Objects.Course;
import tests.misc.EdusoftWebTest;

public class EraterAddRemoveFeedbackComments extends EdusoftWebTest {

	List<Course> courses = null;
	List<String> writingIdForDelete = new ArrayList<String>();

	@Before
	public void setup() throws Exception {
		super.setup();
		courses = pageHelper.getCourses();
		report.report("delete all student assignments");
		String userId = dbService.getUserIdByUserName(
				configuration.getStudentUserName(),
				autoInstitution.getInstitutionId());
		eraterService.deleteStudentAssignments(userId);
		report.stopLevel();
		report.report("Set institution to 'Teacher first' settings");
		eraterService.setEraterTeacherLast();
		report.stopLevel();
	}

	
	
	

	@After
	public void tearDown() throws Exception {
		report.report("Delete writings");
		for (int i = 0; i < writingIdForDelete.size(); i++) {
			eraterService.deleteWritngFromDb(writingIdForDelete.get(i));
		}
		super.tearDown();
	}

}
