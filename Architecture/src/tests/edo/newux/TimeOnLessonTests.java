package tests.edo.newux;

// import org.json.JSONObject;
// import java.sql.Timestamp;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import pageObjects.edo.NewUxMyProgressPage;
import testCategories.NonAngularLearningArea;
// import pageObjects.edo.NewUxLearningArea;
import Enums.FeaturesList;
import Interfaces.TestCaseParams;
@Category(NonAngularLearningArea.class)
public class TimeOnLessonTests extends BasicNewUxTest {
	static final int secInToL_10 = 10;
	static final int secInToL_20 = 20;
	
	boolean bActiveToL = false; 
/*	
	@Before
	public void setUp() throws Exception {
		super.setup();
	}
*/
	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

	@Test
	@TestCaseParams(testCaseID = { "45233", "45234" })   // for Old Learning Area
	public void TimeOnLessonTest() throws Exception {
		String expendToL = "00:00";
		
		String edToLState = getToLEdState();
		
		for(int i=0; i<2; i++)
		{
			report.startStep("Cycle ED ToL in web.config=\'" + edToLState + "\'");
		
			bActiveToL = pageHelper.getFeatureStatePerInstallation(FeaturesList.timeOnLesson);

			report.startStep("Login to ED");
		
			homePage = createUserAndLoginNewUXClass();
		
			ToLBaseRun(secInToL_20);
			sleep(1);

			ToLBaseRun(secInToL_20 + secInToL_10);
			sleep(1);
		
			ToLBaseRun(secInToL_10);
			sleep(1);

			homePage.navigateToCourse(2);
			
			String homeToLWidgetValue = homePage.getTimeOnTaskWidgetValue();
			
 			report.startStep("HomePage ToL Widget: " + homeToLWidgetValue);

 			if(edToLState.equalsIgnoreCase("lesson") && !bActiveToL)
			{
				if(!homeToLWidgetValue.equals("00:00"))
					report.reportFailure("ToL HomePage value not correct");
			}
			else
			{
				if(homeToLWidgetValue.equals("00:00"))
					report.reportFailure("ToL HomePage value not correct");
			}
			
			NewUxMyProgressPage chkProgressPage = homePage.clickOnMyProgress();
			sleep(1);
			
			String chkHH = chkProgressPage.getTimeOnTaskHours();
			String chkMM = chkProgressPage.getTimeOnTaskMinutes();

			String myProgressToL = chkHH + ":" + chkMM;

 			report.startStep("MyProgress ToL Widget: " + myProgressToL);

 			
			if(edToLState.equalsIgnoreCase("lesson") && !bActiveToL)
			{
				if(!myProgressToL.equals("00:00"))
					report.reportFailure("ToL MyProgress value not correct");
			}
			else
			{
				if(myProgressToL.equals("00:00"))
				   report.reportFailure("ToL MyProgress value not correct");
			}
			
			doLogOut();

//..........			
			report.startStep("Change ToL feature mode");
		
			if(bActiveToL)
     			pageHelper.setFeaturesListPerInstallation(FeaturesList.timeOnLesson, "false");
			else
     			pageHelper.setFeaturesListPerInstallation(FeaturesList.timeOnLesson, "true");
			
			bActiveToL = pageHelper.getFeatureStatePerInstallation(FeaturesList.timeOnLesson);

			
			report.startStep("Login to ED");
		
			homePage = createUserAndLoginNewUXClass();
		
			ToLBaseRun(secInToL_20);
			sleep(1);

			ToLBaseRun(secInToL_20 + secInToL_10);
			sleep(1);
		
			ToLBaseRun(secInToL_10);
			sleep(1);

			homePage.navigateToCourse(2);
			
			homeToLWidgetValue = homePage.getTimeOnTaskWidgetValue();
			
 			report.startStep("HomePage ToL Widget: " + homeToLWidgetValue);

 			if(edToLState.equalsIgnoreCase("lesson") && !bActiveToL)
			{
				if(!homeToLWidgetValue.equals("00:00"))
					report.reportFailure("ToL HomePage value not correct");
			}
			else
			{
				if(homeToLWidgetValue.equals("00:00"))
					report.reportFailure("ToL HomePage value not correct");
			}
 			
			chkProgressPage = homePage.clickOnMyProgress();
			sleep(2);
			
			chkHH = chkProgressPage.getTimeOnTaskHours();
			chkMM = chkProgressPage.getTimeOnTaskMinutes();
			
			myProgressToL = chkHH + ":" + chkMM;

 			report.startStep("MyProgress ToL Widget: " + myProgressToL);

 			
			if(edToLState.equalsIgnoreCase("lesson") && !bActiveToL)
			{
				if(!myProgressToL.equals("00:00"))
					report.reportFailure("ToL MyProgress value not correct");
			}
			else
			{
				if(myProgressToL.equals("00:00"))
				   report.reportFailure("ToL MyProgress value not correct");
			}
			
			doLogOut();

		
			if(edToLState.equalsIgnoreCase("task"))
				edToLState = "lesson";
			else
				edToLState = "task";
		
			pageHelper.reWriteWebConfigKey("ed", "TimeOnLessonFlag", edToLState);
		}
	}
	
	private void ToLBaseRun(int chkTime) throws Exception {
		String ToL_Mode = "true"; 
		
		if(!bActiveToL)
			ToL_Mode = "false";
		
//		report.startStep("Start ToL BaseTest -- ToL feature=\'" + ToL_Mode + "\'");
		report.startStep("Start ToL BaseTest (" + Integer.toString(chkTime) + " sec) -- ToL feature=\'" + ToL_Mode + "\'");
	
		report.startStep("Navigate to B1-U1-L1");
		homePage.navigateToCourseUnitAndLesson(courseCodes, "B1", 1, 1);
	
		sleep(chkTime);
		
//		long t1 = System.currentTimeMillis();
		
		report.startStep("Return to Home Page");

	//	homePage.clickToOpenNavigationBar();
		homePage.clickToOpenNavigationBarHP();

//		long t2 = System.currentTimeMillis();
		
//		String tLogOut = Long.toString((t2 - t1) / 1000);
	}

	private String getToLinDb(String edToLState) throws Exception {
		
		return dbService.getStudentTimeInCourse(studentId, edToLState, courses[1]);
	}

	private String getToLEdState() throws Exception {

		String chkKeyValue = null;
		
		try
		{
			chkKeyValue = textService.getWebConfigAppSettingsValuByKey(pageHelper.buildPath, "TimeOnLessonFlag");
		}
		catch(Exception ex)
		{
		}		
		
		return chkKeyValue;
	}
	
	private void doLogOut() throws Exception {
		report.startStep("LogOut");
		homePage.clickOnLogOut();
	}
}
