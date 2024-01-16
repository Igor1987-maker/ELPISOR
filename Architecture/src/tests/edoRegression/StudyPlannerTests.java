package tests.edoRegression;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pageObjects.EdoHomePage;
import pageObjects.tms.TmsHomePage;
import tests.misc.EdusoftWebTest;

public class StudyPlannerTests extends EdusoftWebTest {

	@Before
	public void setup() throws Exception {
		super.setup();
	}

	// Test case 7407

	@Test
	public void testCreateDraftPlanByStartDate() throws Exception {
		startStep("Init test data");

		startStep("Login to edo as student");
		EdoHomePage edoHomePage = pageHelper.loginAsStudent();
		startStep("Check if Edit draft plan appear (if not, edit student home page");
		if (edoHomePage.checkIfCreateNewPlanAppear() == false) {
			AddPStudylannerInTms();
		}

	}

	public void AddPStudylannerInTms() throws Exception {
		TmsHomePage tmsHomePage = pageHelper.loginToTmsAsAdmin();
		tmsHomePage.clickOnSettings();
		tmsHomePage.clickOnHomePage();
		tmsHomePage.selectInstituteInSettings(autoInstitution.getInstitutionName(),
				autoInstitution.getInstitutionId(), false);
//		tmsHomePage.swithchToFormFrame();
		tmsHomePage.switchToMainFrame();
		tmsHomePage.selectHomePageObject("For a Student");
		tmsHomePage.selectScreenArea("Left side elements");
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

}
