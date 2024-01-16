package tests.edo.newux;

// import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import pageObjects.edo.NewUxLearningArea2;
import pageObjects.edo.NewUxMyProgressPage;
import testCategories.AngularLearningArea;
import testCategories.NonAngularLearningArea;
import Enums.FeaturesList;
import Interfaces.TestCaseParams;

@Category(AngularLearningArea.class)
public class TimeOnLessonTests2 extends BasicNewUxTest {
	NewUxLearningArea2 learningArea2;
	
	static final int secInToL_10 = 10;
	static final int secInToL_20 = 20;
	
	boolean bActiveToL = false; 
	

	@Before
	public void setUp() throws Exception {
		super.setup();
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

	@Test
	@TestCaseParams(testCaseID = { "45233", "45234" })   // for Old Learning Area
	public void TimeOnLessonTest() throws Exception {
		//String expendToL = "00:00";
		
		//String edToLState = getToLEdState();
		
		//for(int i=0; i<2; i++)
		//{
			//report.startStep("Cycle ED ToL in web.config=\'" + edToLState + "\'");
		
			//bActiveToL = pageHelper.getFeatureStatePerInstallation(FeaturesList.timeOnLesson);

			report.startStep("Create user and Login to ED");
		
			homePage = createUserAndLoginNewUXClass();
			homePage.closeAllNotifications();
			homePage.waitHomePageloadedFully();
			
			report.startStep("Navigate to B1-U1-L1");
			learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "B1", 1, 1);
			learningArea2.clickOnNextButton();
		
			sleep(secInToL_20);
			learningArea2.clickOnSeeText();
			learningArea2.clickOnPlayVideoButton();
			
//			long t1 = System.currentTimeMillis();
			
			report.startStep("Return to Home Page");
			
				learningArea2.clickOnHomeButton();
				homePage.waitHomePageloadedFully();

			report.startStep("Check only one record of Tol in DB and the Value is correct in Home Page");
				checkTimeOnLessonRecordinDB("1",studentId);
			
				String homeToLWidgetValue = homePage.getTimeOnTaskWidgetValue();
				
				if(homeToLWidgetValue.equals("00:00"))
					   report.reportFailure("ToL HomePage value not correct");	
				
			report.startStep("Click on My progress");	
				NewUxMyProgressPage chkProgressPage = homePage.clickOnMyProgress();
			
				boolean widgetExisttDisplay = homePage.isTimeWidgetExist();
			
				String chkHH = chkProgressPage.getTimeOnTaskHours();
				String chkMM = chkProgressPage.getTimeOnTaskMinutes();
			
				String myProgressToL = chkHH + ":" + chkMM;
			
				if (widgetExisttDisplay){
					if(myProgressToL.equals("00:00"))
					   report.reportFailure("ToL MyProgress value not correct");	
				}
			
			report.startStep("Return to Home Page");
				homePage.clickToOpenNavigationBar();
				homePage.clickOnHomeButton();
				homePage.waitHomePageloadedFully();
			
			//homePage.navigateToCourse(2); //courses[1]
			//homePage.navigateToRequiredCourseOnHomePage(coursesNames[1]);
			
			report.startStep("Enter to Learning Area");
				learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "B1", 1, 1);
				learningArea2.openStepsList();
				learningArea2.clickOnStep(3,false);
				sleep(1);
				learningArea2.clickOnStartTest();
				learningArea2.submitTest(true);
				learningArea2.clickOnNextButton();
				sleep(30);
				homePage.clickOnHomeButton();
				homePage.waitHomePageloadedFully();
				
				checkTimeOnLessonRecordinDB("3",studentId);
			
			report.startStep("Enter to My Progeress");	
				homePage.clickOnMyProgress();
			
				chkHH = chkProgressPage.getTimeOnTaskHours();
				chkMM = chkProgressPage.getTimeOnTaskMinutes();
			
				myProgressToL = chkHH + ":" + chkMM;
			
				testResultService.assertEquals("00:02", myProgressToL,"Time on Lesson in My Progress not coerrect");
				
				doLogOut();
		/*	
			ToLBaseRun(secInToL_20 + secInToL_10);
			sleep(2);
		
			ToLBaseRun(secInToL_10);
			sleep(1);
		
			homePage.navigateToCourse(2);
			
			String homeToLWidgetValue = homePage.getTimeOnTaskWidgetValue();
			
 			report.startStep("HomePage ToL Widget: " + homeToLWidgetValue);
 			
 			//if(edToLState.equalsIgnoreCase("lesson") && !bActiveToL)
			//{
				if(homeToLWidgetValue.equals("00:00"))
					report.reportFailure("ToL HomePage value not correct");
			//}
			//else
			//{
			//	if(homeToLWidgetValue.equals("00:00"))
			//		report.reportFailure("ToL HomePage value not correct");
			//}
			
			NewUxMyProgressPage chkProgressPage = homePage.clickOnMyProgress();
			sleep(1);
			
			String chkHH = chkProgressPage.getTimeOnTaskHours();
			String chkMM = chkProgressPage.getTimeOnTaskMinutes();

			String myProgressToL = chkHH + ":" + chkMM;

			report.startStep("MyProgress ToL Widget: " + myProgressToL);
	*/		
 			
			/*if(edToLState.equalsIgnoreCase("lesson") && !bActiveToL)
			{
				if(!myProgressToL.equals("00:00"))
					report.reportFailure("ToL MyProgress value not correct");
			}
			else
			{*/
			

			//}

		/*			
		 *
			report.startStep("Change ToL feature mode");
		
			if(bActiveToL)
     			pageHelper.setFeaturesListPerInstallation(FeaturesList.timeOnLesson, "false");
			else
     			pageHelper.setFeaturesListPerInstallation(FeaturesList.timeOnLesson, "true");
			
			bActiveToL = pageHelper.getFeatureStatePerInstallation(FeaturesList.timeOnLesson);


			report.startStep("Login to ED");
		
			homePage = createUserAndLoginNewUXClass();
		
			ToLBaseRun(secInToL_20);
			sleep(2);

			ToLBaseRun(secInToL_20 + secInToL_10);
			sleep(2);
		
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
			*/
		//}
				
	}
	
	private void checkTimeOnLessonRecordinDB(String expectedRowCount, String studentId) {
		// TODO Auto-generated method stub
		  
		String actualRowcount = dbService.getTimeOnLessonRecords(studentId);
		testResultService.assertEquals(expectedRowCount, actualRowcount, "Time On Lesson records in DB not match to expected record");
		
	}

	private void ToLBaseRun(int chkTime) throws Exception {
		String ToL_Mode = "true"; 
		
		if(!bActiveToL)
			ToL_Mode = "false";
		
//		report.startStep("Start ToL BaseTest -- ToL " + ToL_Mode + " mode");
		report.startStep("Start ToL BaseTest (" + Integer.toString(chkTime) + " sec) -- ToL feature=\'" + ToL_Mode + "\'");
		
		report.startStep("Navigate to B1-U1-L1");
		learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "B1", 1, 1);
		learningArea2.clickOnNextButton();
	
		sleep(chkTime);
		
//		long t1 = System.currentTimeMillis();
		
		report.startStep("Return to Home Page");

		//homePage.clickToOpenNavigationBar();
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
