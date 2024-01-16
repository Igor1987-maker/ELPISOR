package tests.tms;


import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.tools.ant.property.GetProperty;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import Enums.ByTypes;
import Interfaces.TestCaseParams;
import net.lightbody.bmp.proxy.jetty.html.Break;
import pageObjects.EdoHomePage;
import pageObjects.edo.NewUXLoginPage;
import pageObjects.edo.NewUxLearningArea2;
import pageObjects.tms.DashboardPage;
import pageObjects.tms.TmsHomePage;
import testCategories.inProgressTests;
import testCategories.unstableTests;
import tests.tms.dashboard.DashboardBasicTest;

public class NewUXDashboardWidgetsTests extends DashboardBasicTest {
	
	NewUXLoginPage loginPage;
	NewUxLearningArea2 learningArea2;
	DashboardPage dashboardPage;
	TmsHomePage tmsPages;
	//String iID;
	protected String studentId;
	String originalKeyValue=null;
	String currentTMSBuildPath=null;
	
	@Before
	public void setup() throws Exception {
		super.setup();
	}
	
	@Test
	@TestCaseParams(testCaseID = { "17947", "17318", "18177", "38338" })
	public void testClassCompletionWidgetAndReport() throws Exception {

		report.startStep("Init test data");
			String[] stages = new String[] { "0-20", "21-40", "41-60", "61-80","81-100" };
			String className = configuration.getProperty("classname.progress");
			//iID = institutionId; //autoInstitution.getInstitutionId();
			String classId = dbService.getClassIdByName(className, institutionId);
			String courseId = courses[1];
			String courseName = dbService.getCourseNameById(courseId);
			//String autoStudentId = dbService.getUserIdByUserName(configuration.getStudentUserName(), configuration.getInstitutionId());
			List<String[]> expUnitsProgress = textService.getStr2dimArrFromCsv("files/csvFiles/autoUnitsProgress.csv");
			List<String> classStudents = dbService.getClassStudentsID(classId);
		/*			
		report.startStep("Make progress for one of students");
			studentService.createSingleProgressRecored(autoStudentId, courses[1], "22309", 10, false, 1);
			sleep(10);
		*/
		report.startStep("Open New UX Login Page");
			loginPage = new NewUXLoginPage(webDriver, testResultService);
			pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(configuration.getProperty("teacher.username"),institutionId));
			sleep(1);
			
		report.startStep("Login as Teacher");
			tmsPages = loginPage.enterTeacherUserAndPassword();
			dashboardPage = new DashboardPage(webDriver,testResultService);
			tmsPages.waitForPageToLoad();
			//webDriver.waitUntilElementAppears("CompletionRateGraph", ByTypes.id,30);
			
		report.startStep("Check that the dashbaord is displayed");
			webDriver.sleep(3);
			dashboardPage.switchToMainFrame();
			testResultService.assertEquals(true,dashboardPage.getDashboardNavBarDisplayStatus(),"Dashboard is not displayed");
			dashboardPage.waitToSelectionBar(false);
			
		report.startStep("Select class and course and press GO");
			//webDriver.waitForJqueryToFinish();
			dashboardPage.hideSelectionBar();
			sleep(2);
			dashboardPage.selectClassInDashBoard(className);
			sleep(1);
			dashboardPage.selectCourseInDashboard(courseName);
			sleep(1);
/*
			WebElement element = webDriver.waitForElement("SelectClass", ByTypes.id);
			element.click();
			Select selectClass = new Select(element);
			selectClass.selectByVisibleText(className);
			
			element = webDriver.waitForElement("SelectCourse", ByTypes.id);
			element.click();
			Select selectCourse = new Select(element);
			selectCourse.selectByVisibleText(courseName);
*/		
			dashboardPage.clickOnDashboardGoButton();
			sleep(2);
		report.startStep("Check widget title");
			String title = dashboardPage.getWidgetTitle(1, 1);
			testResultService.assertEquals("Course Completion", title,"title not found / wrong");
			
		report.startStep("Check that chart is displayed");
			dashboardPage.checkIfWidgetHasData(1, 1);
	
		report.startStep("Check total number of students");
			
			List<String[]> completionList = getClassCompletion(className,institutionName, courseId);
					
			int allStudentsWithProgress = 0;
			String[] complStudents = new String[5];
			for (int i = 0; i <= 4; i++) {
				complStudents[i] = dashboardPage.getNumberOfStudentsInCompletiionChart(i);
				allStudentsWithProgress += Integer.valueOf(complStudents[i]);
			}
	
		report.startStep("Check number of students for each stage");
			int listCounter = 0;
			for (int i = 0; i < complStudents.length; i++) {
				if (!complStudents[i].equals("0")) {
					if (completionList.get(listCounter)[3].equals(stages[0])) {
						testResultService.assertEquals(complStudents[i],
								completionList.get(listCounter)[0],
								"number of students for stage:" + stages[0]
										+ " not found");
						listCounter++;
					} else if (completionList.get(listCounter)[3].equals(stages[1])) {
						testResultService.assertEquals(complStudents[i],
								completionList.get(listCounter)[0],
								"number of students for stage:" + stages[1]
										+ " not found");
						listCounter++;
					} else if (completionList.get(listCounter)[3].equals(stages[2])) {
						testResultService.assertEquals(complStudents[i],
								completionList.get(listCounter)[0],
								"number of students for stage:" + stages[2]
										+ " not found");
						listCounter++;
					} else if (completionList.get(listCounter)[3].equals(stages[3])) {
						testResultService.assertEquals(complStudents[i],
								completionList.get(listCounter)[0],
								"number of students for stage:" + stages[3]
										+ " not found");
						listCounter++;
					} else if (completionList.get(listCounter)[3].equals(stages[4])) {
						testResultService.assertEquals(complStudents[i],
								completionList.get(listCounter)[0],
								"number of students for stage:" + stages[4]
										+ " not found");
						listCounter++;
					}
				}
				// check that value is 0
			
			}
	
		report.startStep("Check summary of all students");
	
			testResultService.assertEquals(allStudentsWithProgress, Integer
					.parseInt(dbService.getNumberOfStudentsInClass(className,
							autoInstitution.getInstitutionId())),
					"total number of students with completion do not match");
	
		report.startStep("Click on Class Completion Report btn, check that report opens and check selected course / class");
			//sleep(5);
			dashboardPage.clickOnCompletionWidgetButton();
			sleep(3);
			tmsPages.checkForReportResults();
			sleep(2);
			testResultService.assertEquals(courseName,tmsPages.getSelectedCourseInReport());
			testResultService.assertEquals(className, dashboardPage.getSelectedClassInReport());
		
		report.startStep("Check students units progress display");
			tmsPages.switchToTableFrame();
			//sleep(5);
			verifyClassCompletionReport(classStudents, expUnitsProgress);	
			
		report.startStep("Check Legend with units");
			List<String[]> units = dbService.getCourseUnitDetils(courseId);
			verifyLegendText(units);
			sleep(2);
			
		report.startStep("Navigate to the same report from Home and check students units progress display");
			navigateToReportViaHome("2", className, courseId);
			
		report.startStep("Check students units progress display");
			verifyClassCompletionReport(classStudents, expUnitsProgress);	
			
		report.startStep("Check Legend with units");
			verifyLegendText(units);
		
	}

	@Test
	@TestCaseParams(testCaseID = { "17097","38339" })
	public void testClassTestScoreWidgetAndReport() throws Exception {

		report.startStep("Init test data & Make progress for one of students");
			//String autoStudentId = dbService.getUserIdByUserName(configuration.getStudentUserName(), configuration.getInstitutionId());
			String className = configuration.getProperty("classname.progress");
			//iID = institutionId; // autoInstitution.getInstitutionId();
			String classId = dbService.getClassIdByName(className, institutionId);
			String courseId = courses[1];
			String courseName = coursesNames[1]; //dbService.getCourseNameById(courseId);
			List<String[]> units = dbService.getCourseUnitDetils(courseId);
			List<String[]> expUnitsProgress = textService.getStr2dimArrFromCsv("files/csvFiles/autoUnitsProgress.csv");
			List<String> classStudents = dbService.getClassStudentsID(classId);
			
			/*
			studentService.createSingleProgressRecored(autoStudentId, courses[0], "31515", 10, false, 1);
			sleep(1);
			*/
		report.startStep("Open New UX Login Page");
			loginPage = new NewUXLoginPage(webDriver, testResultService);
			pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(configuration.getProperty("teacher.username"),configuration.getProperty("institution.id")));
	
		report.startStep("Login as Teacher");
			tmsPages = loginPage.enterTeacherUserAndPassword();
			dashboardPage = new DashboardPage(webDriver,testResultService);
			
		report.startStep("Check that the dashbaord is displayed");
			//webDriver.sleep(3);
			dashboardPage.switchToMainFrame();
			testResultService.assertEquals(true,dashboardPage.getDashboardNavBarDisplayStatus(),"Dashboard is not displayed");
			dashboardPage.waitToSelectionBar(false);
			
		report.startStep("Select class and course and press GO");
			dashboardPage.hideSelectionBar();
			sleep(2);
			dashboardPage.selectClassInDashBoard(className);
			sleep(1);
			dashboardPage.selectCourseInDashboard(courseName);
			sleep(1);
			dashboardPage.clickOnDashboardGoButton();
			
		report.startStep("Check widget title");
			String title = dashboardPage.getWidgetTitle(1, 2);
			testResultService.assertEquals("Average Test Scores", title, "Test Score widget title not found");
			
		report.startStep("Check that chart is displayed");
			dashboardPage.checkIfWidgetHasData(1, 2);
			
		report.startStep("Check widget data");
			
			String expUnitsAvgScore = "100";
			int expNumOfUnits = units.size();
			int actNumOfUnits = dashboardPage.getAvgScoreUnitsElements().size();
			testResultService.assertEquals(expNumOfUnits, actNumOfUnits, "Number of Units in Test Score widget does not match");
					
			String scoreUnit;
			
			for (int i = 0; i < expNumOfUnits; i++) {
				scoreUnit = dashboardPage.getAvgScorePerUnitClassTestScore(0);
				testResultService.assertEquals(expUnitsAvgScore, scoreUnit, "Unit avg score does not match");
			}
			
		report.startStep("Click on Class Score Report btn, check that report opens and check selected course / class");
			//sleep(5);
			dashboardPage.clickOnClassTestScoreReport();
			//sleep(8);  // Replaced 3 second sleep with 8 after repeated manual testing showed that ~5 are needed. 
			tmsPages.checkForReportResults();
			testResultService.assertEquals(courseName,tmsPages.getSelectedCourseInReport());
			testResultService.assertEquals(className, dashboardPage.getSelectedClassInReport());
			
		report.startStep("Check students units score display");
			tmsPages.switchToTableFrame();
			verifyClassScoreReport(classStudents, expUnitsProgress);	
			
		report.startStep("Check Legend with units");
			verifyLegendText(units);
					
		report.startStep("Navigate to the same report from Home and check students units progress display");
			navigateToReportViaHome("3", className, courseId);
			
		report.startStep("Check students units score display");
			verifyClassScoreReport(classStudents, expUnitsProgress);
				
		report.startStep("Check Legend with units");
			verifyLegendText(units);
	}
	
//	@Test // time on task calculation not in use anymore
	@TestCaseParams(testCaseID = { "38340","49123","49120" }, skippedBrowsers = {"firefox"}) // Actions API not supported by geckodriver)
	public void testClassTimeOnTaskWidgetAndReport() throws Exception {
		
			report.startStep("Init dtest data & Make progress for one of students");
				studentId = dbService.getUserIdByUserName(configuration.getStudentUserName(), configuration.getInstitutionId());
				String className = configuration.getProperty("classname.progress");
				//iID = autoInstitution.getInstitutionId();
				String classId = dbService.getClassIdByName(className, institutionId);
		//		String courseId = courses[1]; -- igb 2018.05.13
				String courseId = courses[0]; 
				String courseName = dbService.getCourseNameById(courseId);
				List<String[]> units = dbService.getCourseUnitDetils(courseId);
				List<String> classStudents = dbService.getClassStudentsID(classId);
				//int timeonLessonInseconds = 20;
				String dbAvgTimeOnUnit=null;
				//String widghetViewMode="Unit";
	/*		
			//report.startStep("Set Time on Lesson and Task in DB for Student: " + studentId);
				startStep("Set Time on Task in DB for Student: " + studentId);
				studentService.createSingleProgressRecored(studentId, courses[0], "31515", timeonLessonInseconds, false, 1);
				sleep(1);
				
			//startStep("Set Time on Lesson in DB for Student: " + studentId);
			//	studentService.setTimeOnLessonInSeconds(studentId,courses[0],"20447",timeonLessonInseconds);
			//	sleep(1);
		*/
			String reportMode = "task"; //Lesson
		//	report.startStep("Set the TMS to show Time on "+ reportMode +" data in reports");
		//	setTMSreportMode(reportMode);

			try{
					
				
			report.startStep("Open New UX Login Page");
				loginPage = new NewUXLoginPage(webDriver, testResultService);
				pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(configuration.getProperty("teacher.username"),configuration.getProperty("institution.id")));
		
			report.startStep("Login as Teacher");
				tmsPages = loginPage.enterTeacherUserAndPassword();
				dashboardPage = new DashboardPage(webDriver,testResultService);

			report.startStep("Check that the dashbaord is displayed");
				webDriver.sleep(3);
				dashboardPage.switchToMainFrame();
				testResultService.assertEquals(true,dashboardPage.getDashboardNavBarDisplayStatus(),"Dashboard is not displayed");
				dashboardPage.waitToSelectionBar(false);
					
				report.startStep("Select class and course and press GO");
					dashboardPage.hideSelectionBar();
					dashboardPage.selectClassInDashBoard(className);
					sleep(2);
					dashboardPage.selectCourseInDashboard(courseName);
					sleep(2);
					dashboardPage.clickOnDashboardGoButton();

			report.startStep("Check widget Unit's title and AVG time");
			
				String title = dashboardPage.getTpsWidgetTitle();
				testResultService.assertEquals("Units", title, "Units widget title not found");
		
			report.startStep("Check widget shows all units");
					

				for (int i = 0; i < units.size(); i++) {
					startStep("Check widget shows unit: " + (i+1));
				
					String unitName = dashboardPage.getTPSUnitNameTooltip(i + 1);
					testResultService.assertEquals("Unit Name: " + units.get(i)[1],
							unitName, "Unit number: " + i + " tooltip not found");
					
					String actUiAvgTimeOnUnit = dashboardPage.getTPSUnitAvgTime(i+1);
					dbAvgTimeOnUnit = getWidgetTimeClassByUnit(className, courseId,(i + 1),reportMode);
					
					if (!dbAvgTimeOnUnit.equals("null")) {
						dbAvgTimeOnUnit = dbAvgTimeOnUnit.substring(0,5);
					}
					testResultService.assertEquals(dbAvgTimeOnUnit,actUiAvgTimeOnUnit, "Avrage time isn't the same");

					if ((i+1)%4 == 0 && (i+1)!=units.size()) {
						dashboardPage.clickTPSNextButton();
						sleep(1);
					}
				}

			report.startStep("Click on View By Skill");
				dashboardPage.clickOnUnitOverViewByskill();
				
				//String[] skillsOrder = {"Listening", "Reading", "Speaking", "Grammar","Vocabulary"}; // for other course
				String[] skillsOrder = {"Listening", "Reading", "Speaking", "Alphabet","Writing","Vocabulary"}; // for FD course
				String actulSkillAvgTime=null;
				
				for (int i=0; i< skillsOrder.length;i++)
				{
					startStep("Check AVG time in Skills: " + skillsOrder[i]);
					
					//Get actual skill order from widget
					String skillName =  dashboardPage.getSkillOnPage(i+1);
					testResultService.assertEquals(skillName,skillsOrder[i], "The skill Name order not match");
					
					actulSkillAvgTime = dashboardPage.getTPSSkillAvgTime(i+1);
					dbAvgTimeOnUnit = getWidgetTimeClassBySkill(className, courseId, skillName,reportMode);
					
						if (!dbAvgTimeOnUnit.equals("null")) {
							dbAvgTimeOnUnit = dbAvgTimeOnUnit.substring(0,5);
						}
						testResultService.assertEquals(dbAvgTimeOnUnit,actulSkillAvgTime, "Skill Average time doesn't match to DB, Skill: " + skillName);
					
					if ((i+1)%4 == 0 && (i+1)!=skillsOrder.length) {
						dashboardPage.clickTPSNextButton();
						sleep(1);
					}
				}
				
				
			startStep("Click on Time on Task, check that report opens and check selected course / class");
				dashboardPage.clickOnTimeOnTaskReport();
				sleep(3);
				dashboardPage.checkForReportResults();
				testResultService.assertEquals(courseName,tmsPages.getSelectedCourseInReport());
				testResultService.assertEquals(className, dashboardPage.getSelectedClassInReport());
				
			report.startStep("Check students total time on task display");
				tmsPages.switchToTableFrame();
				verifyTimeOnTaskReport(classStudents, courseId);
						
			report.startStep("Check Legend with units");
				verifyLegendText(units);
				
			report.startStep("Navigate to the same report from Home and check students total time on task display");
				navigateToReportViaHome("6", className, courseId);
							
			report.startStep("Check students total time on task display");
				verifyTimeOnTaskReport(classStudents, courseId);
				
			report.startStep("Check Legend with units");
				verifyLegendText(units);
			
		
			} finally {
			
			//pageHelper.reWriteWebConfigKeyByPath(currentTMSBuildPath, "TimeOnLessonFlag", originalKeyValue);
			}
	}
	

	private void setTMSreportMode(String reportMode) throws Exception {
		// TODO Auto-generated method stub
		
		String tmsurl = pageHelper.getWebConfigKey("ed", "TmsHost");
		currentTMSBuildPath = pageHelper.setAndReturnBuildPathCI(tmsurl);
		originalKeyValue = textService.getWebConfigAppSettingsValuByKey(currentTMSBuildPath, "TimeOnLessonFlag");
		pageHelper.reWriteWebConfigKeyByPath(currentTMSBuildPath, "TimeOnLessonFlag", reportMode);
		
	}

	@Test
	@TestCaseParams(testCaseID = { "49533","49534" }, skippedBrowsers = {"firefox"}) // Actions API not supported by geckodriver)
	public void testTimeOnLessonReport() throws Exception {
		
		report.startStep("Init dtest data & Make progress for one of students");
			//iID = institutionId; //autoInstitution.getInstitutionId();
			String autoStudentName = configuration.getStudentUserName();
			String autoTeacherName = configuration.getProperty("teacher.username");
			String className = configuration.getProperty("classname.progress");
			studentId = dbService.getUserIdByUserName(autoStudentName, institutionId);
			int courseIndex = 0; // B2
			String courseId = courses[courseIndex];
			//String lessonId = "105"; // B2-U1-L1
			String timeOnLessonReportId = "6"; //6= Time on task   //"15"; // TimeOnTask2
			String reportMode = "lesson"; //task
			String courseName = dbService.getCourseNameById(courseId);
			List<String[]> units = dbService.getCourseUnitDetils(courseId);
			//List<String> skills = dbService.getSkillInCourse(courseId);
			String classId = dbService.getClassIdByName(className, institutionId);
			List<String> classStudents = dbService.getClassStudentsID(classId);
			
			
		//startStep("Set the TMS to show Time on "+ reportMode +" data in reports");
			//setTMSreportMode(reportMode);
		
		try {

			report.startStep("Login as Teacher");
				loginPage = new NewUXLoginPage(webDriver, testResultService);
				pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(autoTeacherName, institutionId));
				tmsPages = loginPage.enterTeacherUserAndPassword();
			
				
			report.startStep("Check that the dashbaord is displayed");
				tmsPages.waitForPageToLoad();
				dashboardPage = new DashboardPage(webDriver,testResultService);
				dashboardPage.switchToMainFrame();
				sleep(5);
				testResultService.assertEquals(true,dashboardPage.getDashboardNavBarDisplayStatus(),"Dashboard is not displayed");
				dashboardPage.waitToSelectionBar(false);
				
			//	String selectedClass=null,selectedCourse=null;			
			//	selectedClass = dashboardPage.getClassLabelText();
			//	selectedCourse = dashboardPage.getCourseLabelText();

				// Select class and course only if needed
			//	if (!(selectedClass.equals(className) && !selectedCourse.equals(courseName))){
				report.startStep("Select class and course and press GO");
					dashboardPage.hideSelectionBar();
					dashboardPage.selectClassInDashBoard(className);
					dashboardPage.selectCourseInDashboard(courseName);
					sleep(1);
					dashboardPage.clickOnDashboardGoButton();	
			//	}
			
				/*
			report.startStep("Check widget title");
				String title = dashboardPage.getTpsWidgetTitle();
				testResultService.assertEquals("Units", title, "Units widget title not found");
				 */
				
			report.startStep("Check widget shows all units");
					
				//webDriver.waitForJqueryToFinish();
				String dbAvgTimeOnUnit=null;
				String actUiAvgTimeOnUnit=null;
				
				for (int i = 0; i < units.size(); i++) {
					
					startStep("Check AVG time in Unit :"+ (i+1));
					
					String unitName = dashboardPage.getTPSUnitNameTooltip(i + 1);
					testResultService.assertEquals("Unit Name: " + units.get(i)[1],
							unitName, "Unit number: " + i + " tooltip not found");
					
					actUiAvgTimeOnUnit = dashboardPage.getTPSUnitAvgTime(i+1);				
					dbAvgTimeOnUnit = getWidgetTimeClassByUnit(className, courseId,(i + 1),reportMode);
					
					/* the above function already retuen the correct data format or null so this statement seen not relevant
						if (!dbAvgTimeOnUnit.equals("null")) {
							dbAvgTimeOnUnit = dbAvgTimeOnUnit.substring(0,5);
						}
					*/
						testResultService.assertEquals(dbAvgTimeOnUnit,actUiAvgTimeOnUnit, "Actual Unit Average time doesn't match to DB, Unit: " + (i + 1));
					
					if ((i+1)%4 == 0 && (i+1)!=units.size()) {
						dashboardPage.clickTPSNextButton();
						sleep(1);
					}
				}

				
			report.startStep("Click on View By Skill");
				dashboardPage.clickOnUnitOverViewByskill();
				
				//String[] skillsOrder = {"Listening", "Reading", "Speaking", "Grammar","Vocabulary"}; // for other course
				String[] skillsOrder = {"Listening", "Reading", "Speaking", "Alphabet","Writing","Vocabulary"}; // for FD course
				String actulSkillAvgTime=null;
				
				for (int i=0; i< skillsOrder.length;i++)
				{
					startStep("Check AVG time in Skills: " + skillsOrder[i]);
					
					//Get actual skill order from widget
					String skillName =  dashboardPage.getSkillOnPage(i+1);
					testResultService.assertEquals(skillName,skillsOrder[i], "The skill Name order not match");
					
					actulSkillAvgTime = dashboardPage.getTPSSkillAvgTime(i+1);
					dbAvgTimeOnUnit = getWidgetTimeClassBySkill(className, courseId, skillName,reportMode);
					
					/* the above function already retuen the correct data format or null so this statement seen not relevant
						if (!dbAvgTimeOnUnit.equals("null")) {
							dbAvgTimeOnUnit = dbAvgTimeOnUnit.substring(0,5);
						}
					*/
						testResultService.assertEquals(dbAvgTimeOnUnit,actulSkillAvgTime, "Skill Average time doesn't match to DB, Skill: " + skillName);
					
					if ((i+1)%4 == 0 && (i+1)!=skillsOrder.length) {
						dashboardPage.clickTPSNextButton();
						sleep(1);
					}
				}
				
				
			report.startStep("Click on Time on Task, check that report opens and check selected course / class");
				dashboardPage.clickOnTimeOnTaskReport();
				sleep(3);
				dashboardPage.checkForReportResults();
				testResultService.assertEquals(courseName,tmsPages.getSelectedCourseInReport());
				testResultService.assertEquals(className, dashboardPage.getSelectedClassInReport());
				tmsPages.switchToTableFrame();
				verifyTimeOnLessonReportInCourse(classStudents, courseId);
				
			report.startStep("Navigate to Time On Lesson report");
				navigateToReportViaHome(timeOnLessonReportId, className, courseId);
				sleep(1);
				
			startStep("Check students total time on task display");
				verifyTimeOnLessonReportInCourse(classStudents, courseId);
			
		} catch (Exception e) {
			
			testResultService.addFailTest(e.toString(), false, true);
			
		} finally {
			
			//pageHelper.reWriteWebConfigKey("TMS", "TimeOnLessonFlag", originalKeyValue);

		}
				
		
	}

	
		
	private void verifyClassCompletionReport (List<String> classStudents, List<String[]> expUnitsProgress) throws Exception {
		
			for (int j = 0; j < classStudents.size(); j++) {
			
			String userName = dbService.getUserNameById(classStudents.get(j), institutionId);
			List<String[]> actUnitsProgress = tmsPages.getStudentUnitsCompletionInTableByName(userName);
			
			for (int k = 0; k < actUnitsProgress.size(); k++) {
				
				String expUP = "undefined";
				for (int l = 0; l < expUnitsProgress.size(); l++) {
					if (expUnitsProgress.get(l)[0].equals(userName))
						expUP = expUnitsProgress.get(l)[k+1];
				}
				String actUP = actUnitsProgress.get(k)[1];
				testResultService.assertEquals(expUP, actUP, "Expected Unit Progress does not match graphical display in TMS report. Unit Number: " + k + "and Username: " + userName);
			}
			
		}
		
		
		
	}
	
	private void verifyClassScoreReport (List<String> classStudents, List<String[]> expUnitsProgress) throws Exception {
		
		for (int j = 0; j < classStudents.size(); j++) {
		
		String userName = dbService.getUserNameById(classStudents.get(j), institutionId);
		List<String[]> actUnitsProgress = tmsPages.getStudentUnitsScoreInTableByName(userName);
					
		
		for (int k = 0; k < actUnitsProgress.size(); k++) {
			
			String expUP = "undefined";
			for (int l = 0; l < expUnitsProgress.size(); l++) {
				if (expUnitsProgress.get(l)[0].equals(userName))
					expUP = expUnitsProgress.get(l)[k+1];
			}
			
			if (expUP.equals("0")) expUP = "";
			String actUP = actUnitsProgress.get(k)[1];
			testResultService.assertEquals(expUP, actUP, "Expected Unit Avg Score that exists in: files/csvFiles/autoUnitsProgress.csv does not match graphical display in TMS report. UserName=" + userName + " + Unit: " + (k+1));
			}
		
		}
	}
		
	private void verifyTimeOnTaskReport (List<String> classStudents, String courseId) throws Exception {
		
		for (int j = 0; j < classStudents.size(); j++) {
		
		String studentID =  classStudents.get(j);
		String expTimeOnTask = dbService.getStudentTimeOnTask(courseId, studentID);
		String userName = dbService.getUserNameById(studentID, institutionId);
		String actTimeOnTask = tmsPages.getStudentTotalTimeOnTask(userName);
							
		testResultService.assertEquals(expTimeOnTask, actTimeOnTask, "Expected total Time on Task does not match graphical display in TMS report");
				
		}
	}	
	
	
	private void verifyTimeOnLessonReportInCourse (List<String> classStudents, String courseId) throws Exception {
		
		for (int j = 0; j < classStudents.size(); j++) {
		
		String studentID =  classStudents.get(j);
		String expTimeOnTask = dbService.getStudentTimeOnLessonInSecondsInCourse(studentID,courseId);
		
		String userName = dbService.getUserNameById(studentID, institutionId);
		String actTimeOnTask = tmsPages.getStudentTotalTimeOnLesson(userName);
							
		testResultService.assertEquals(actTimeOnTask, expTimeOnTask, "Expected total Time on Task does not match graphical display in TMS report for User: " + userName);
				
		}
	}	

	/*
	private String getAvgTimeOnTask (List<String> classStudents, String courseId,String i) throws Exception {
		
		int k = 0;
		int totalTimeInMin = 0;
		int avg;		
		for (int j = 0; j < classStudents.size(); j++) {
			String studentID =  classStudents.get(j);
			String expTimeOnTask = dbService.getStudentTimeOnTaskInUnit(courseId, studentID, i);
			if (!expTimeOnTask.equals("00:00")){
				k++;
				totalTimeInMin += Integer.valueOf(expTimeOnTask.substring(0,2))*60 + Integer.valueOf(expTimeOnTask.substring(3)); 
			}
		}
		avg = totalTimeInMin/k;
		if (avg/60 < 10)
			return "0" + avg/60 + ":0" + avg % 60;
		else
			return avg/60 + ":" + avg % 60;
	}	
	*/
	
	/*
	private String getAvgTimeInCourse (List<String> classStudents, String courseId,String i,String reportMode) throws Exception {
		
		double k = 0;
		int totalTimeInMin = 0;
		int avg;
		String expTimeOnTask = null;
		
		for (int j = 0; j < classStudents.size(); j++) {
			String studentID =  classStudents.get(j);
			
			
			if (reportMode.equals("lesson")){
				expTimeOnTask= dbService.getStudentTimeOnLessonInSecondsInUnit(studentID,courseId, i);
			}
			else{
				expTimeOnTask = dbService.getStudentTimeOnTaskInUnit(courseId, studentID, i);
			}
			
			if (!expTimeOnTask.equals("null")){
				k++;
				expTimeOnTask = expTimeOnTask.substring(0,5);
				totalTimeInMin += Integer.valueOf(expTimeOnTask.substring(0,2))*60 + Integer.valueOf(expTimeOnTask.substring(3)); 
			}
		}
		avg = (int) Math.round(totalTimeInMin/k);
		if (avg/60 < 10)
			return "0" + avg/60 + ":0" + avg % 60;
		else
			return avg/60 + ":" + avg % 60;
	}	
	
	*/
private String getWidgetTimeClassByUnit(String className,String courseId,int unitSequence, String reportMode) throws Exception {
			
		List<String[]> time = null;
		String timeSummary=null;

		if (reportMode.equals("lesson")){
			time= dbService.getClassTimeOnLessonInSecondsByUnit(className,courseId, unitSequence);
		}
		//else{ // By Task
		//	time = dbService.getClassTimeOnTaskInSecondsByUnit(className, courseId, unitSequence);
		//}

		if (time== null || time.isEmpty())
			timeSummary=null;
		else
			timeSummary = convertToCorrectTimeformat(time);

		return timeSummary;
	}

	public String getWidgetTimeClassBySkill(String className, String courseId,String skillName,String reportMode) throws Exception{
		
		List<String[]> time;
		
		if (reportMode.equals("lesson")){
			time = dbService.getClassTimeOnLessonInSecondsBySkill(className, courseId,skillName); 	
		}
		else{ // task
			time = dbService.getClassTimeOnTaskInSecondsBySkill(className, courseId,skillName);
		}
		
		String timeSummary = convertToCorrectTimeformat(time);
		
		return timeSummary;
	}
	
	
	private String convertToCorrectTimeformat(List<String[]> data) throws Exception{
		
		String time = null;
		
		String[] dbTime = data.get(0);
		
		if (dbTime[0]== null || dbTime[1]==null)
			return "null";
		else
		{
		if (dbTime[0].length() == 1)
			time =  "0" + dbTime[0] + ":";
		else
			time = dbTime[0] + ":";
		
		
		if (dbTime[1].length() == 1)
			time =  time + "0" + dbTime[1];
		else
			time = time + dbTime[1];
		}
		return time;
	}
	
	/*
	private void verifyTimeOnLessonReport (String studentName, int expTimeOnLessonInSeconds) throws Exception {
		
		String actTOL = tmsPages.getStudentTotalTimeOnLesson(studentName);

	    String expTOL = String.format("%02d:%02d:%02d", expTimeOnLessonInSeconds / 3600, (expTimeOnLessonInSeconds % 3600) / 60, (expTimeOnLessonInSeconds % 60));
	    	    
		testResultService.assertEquals(expTOL, actTOL, "Expected total Time on Lesson does not match graphical display in TMS report");
				
		
	}	
		*/
	private void verifyLegendText (List<String[]> units) throws Exception {
	
		webDriver.switchToTopMostFrame();
		tmsPages.switchToMainFrame();
		String legendText = tmsPages.getLegendText();
		
		String expUnitName = "undefined";
		int number = 0;
		
		for (int j = 0; j < units.size(); j++) {
			number = j + 1;
			expUnitName = "Unit "+ number + " - " + units.get(j)[1];
			testResultService.assertEquals(true, legendText.contains(expUnitName), "Unit name is not in legend text");
		}

	}
	
	private void navigateToReportViaHome (String reportValueId, String className, String courseId) throws Exception {
			
		webDriver.switchToTopMostFrame();
		tmsPages.switchToMainFrame();
		tmsPages.ClickTheHomeButton();
		sleep(1);
		tmsPages.clickOnReports();
		sleep(1);
		tmsPages.clickOnCourseReports();
		sleep(1);
		tmsPages.switchToFormFrame();
		sleep(1);
		tmsPages.selectReport(reportValueId);
		sleep(1);
		tmsPages.selectClass(className, false, true);
		sleep(1);
		webDriver.switchToTopMostFrame();
		tmsPages.switchToMainFrame();
		sleep(1);
		tmsPages.selectCourse(courseId);
		sleep(1);				
		tmsPages.switchToTableFrame();
		
	}
		
	
	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}
	
}
	
	

