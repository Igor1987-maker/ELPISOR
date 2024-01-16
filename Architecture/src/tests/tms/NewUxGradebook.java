package tests.tms;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import Enums.ByTypes;
import Interfaces.TestCaseParams;
import testCategories.AngularLearningArea;
import pageObjects.edo.NewUXLoginPage;
import pageObjects.edo.NewUxLearningArea;
import pageObjects.edo.NewUxLearningArea2;
import pageObjects.edo.NewUxMyProfile;
import pageObjects.edo.NewUxMyProgressPage;
import pageObjects.edo.NewUxCommunityPage;
import pageObjects.tms.DashboardPage;
import pageObjects.tms.TmsHomePage;
import services.TextService;
import tests.edo.newux.SpeechRecognitionBasicTestNewUX;

public class NewUxGradebook extends SpeechRecognitionBasicTestNewUX {

	protected NewUxCommunityPage communityPage;
	NewUxMyProgressPage myProgressPage;
	NewUXLoginPage loginPage;
	DashboardPage dashboardPage;
	TmsHomePage tmsHomePage;
	NewUxLearningArea learningArea;
	NewUxLearningArea2 learningArea2;
	NewUxMyProfile myProfile;

	Boolean removeScore = false;
	Boolean removeColumns = false;
	
	@Before
	public void setup() throws Exception {
		super.setup();
	}
	
	@Test
	@Category(AngularLearningArea.class)
	@TestCaseParams(testCaseID = { "" })//, institutionId = laureateInstId)
	public void testAcuityZoomCancelAndEdit() throws Exception{
		
		report.startStep("Change to Laurate Instituion");
		institutionName=institutionsName[6];
		pageHelper.restartBrowserInNewURL(institutionName, true); 
		institutionId = dbService.getInstituteIdByName(institutionName);
		sleep(2);
	//	laureateInstId = institutionId;
	
		report.startStep("Init test Data");
			String userName = configuration.getStudentUserName();
			String password = configuration.getStudentPassword();
			String currentUrl = "";
	
		report.startStep("Login as student");
			pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(userName, institutionId));
			NewUXLoginPage loginPage = new NewUXLoginPage(webDriver,testResultService);
			homePage = loginPage.loginAsStudent(userName, password);
			homePage.closeModalPopUp();
		
		report.startStep("Remove Previous meeting if exists");
			verifyAndRemovePreviousMeetings();
			sleep(3);
				
		report.startStep("Open the Online Lessons");
			clickToOnlineLessons();
			sleep(3);
		
		report.startStep("Check student acuity link");
			webDriver.switchToNextTab();
			currentUrl = webDriver.waitForSpecificCurrentUrl(currentUrl, "www");
		
		report.startStep("Student meeting to tomorrow");
			studentmeetingED();
			webDriver.closeNewTab(1);
			sleep(1);
		
		report.startStep("Check student meeting on ED");
			checkstudentmeetingED();
			webDriver.switchToNextTab();
		
		report.startStep("Reschedule the meeting");
			rescheduleMeeting();
		
		report.startStep("Check student meeting on ED");
			checkstudentmeetingED();
			webDriver.switchToNextTab();
		
		report.startStep("Cancel meeting");
			cancelMeeting(); 
		
		report.startStep("checkMeetingNotExist");
			checkMeetingNotExist();
		
		report.startStep("Logout as student");
			homePage.clickOnLogOut();
		
	}

	@Test
	@Category(AngularLearningArea.class)
	@TestCaseParams(testCaseID = { "" })//, institutionId = laureateInstId)
	public void testAcuityZoomIntegration () throws Exception{
		
		report.startStep("Init test Data");
		String userName = configuration.getStudentUserName();
		String password = configuration.getStudentPassword();
		studentId = dbService.getUserIdByUserName(userName, institutionId);
		String teacherName = configuration.getProperty("teacher.username");
		String teacherId = dbService.getUserIdByUserName(teacherName, institutionId);
		
		
		report.startStep("Login as student");
		pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(userName, institutionId));
		NewUXLoginPage loginPage = new NewUXLoginPage(webDriver,testResultService);
		homePage = loginPage.loginAsStudent(userName, password);
		homePage.closeModalPopUp();
		
		report.startStep("Open the Online Lessons");
		clickToOnlineLessons();
		sleep(6);
		
		report.startStep("Check student acuity link");
		webDriver.switchToNextTab();
		String currentUrl = "";
		currentUrl = webDriver.waitForSpecificCurrentUrl(currentUrl, "www");
		//testResultService.assertEquals(AcuityStudentUrl, currentUrl, "about site is not displayed");
		
		report.startStep("Student meeting to tommorow");
		studentmeetingED ();
		webDriver.closeNewTab(1);
		
		report.startStep("Check student meeting on ED");
		checkstudentmeetingED();
		webDriver.switchToNextTab();
		
		webDriver.closeNewTab(1);
		
		report.startStep("Logout as student");
		homePage.clickOnLogOut();
		
		report.startStep("Login as teacher");
		pageHelper.setUserLoginToNull(teacherId);
		tmsHomePage = loginPage.loginAsTmsUser(teacherName, "12345");
		sleep(2);	
		report.startStep("Click on Schedule Lessons and check that teacher receive the meeting");
		tmsHomePage.switchToMainFrame();
		tmsHomePage.clickOnCommunication();
		tmsHomePage.clickOnScheduleLessons();
		sleep(3);
		webDriver.waitForElement("//*[@id='teacherCredPH']/div/div[3]/input",ByTypes.xpath).click();
		webDriver.switchToNextTab();
		
		report.startStep("Check teaher acuity link");
		currentUrl = webDriver.waitForSpecificCurrentUrl(currentUrl, "www");
	
		testResultService.assertEquals(AcuityTeacherUrl, currentUrl, "about site is not displayed");
		
		webDriver.closeNewTab(1);
		tmsHomePage.switchToMainFrame();
		webDriver.waitForElement("//*[@id='teacherUpcomingAppointmentsPH']/div/div[2]/div[1]",ByTypes.xpath).click();
		
		report.startStep("Logout from TMS");
		tmsHomePage.clickOnExit();
		sleep(3);
		
	report.startStep("Login as student");
		homePage = loginPage.loginAsStudent(userName, password);
		checkstudentmeetingED();
		webDriver.switchToNextTab();
		sleep(3);
		report.startStep("Cancel meeting");
		cancelMeeting(); 
		webDriver.waitForElementAndClick("//*[@id='mainCtrl']/div[3]/div/div/div[1]/a", ByTypes.xpath);
		sleep(5);
		report.startStep("Logout as student");
		homePage.clickOnLogOut();
		
	}
	
	
	
	@Test
	@Category(AngularLearningArea.class)
	@TestCaseParams(testCaseID = { "55131" })//, institutionId = laureateInstId)
	public void testPopulateGradebook() throws Exception {

	report.startStep("Change to Laurate Instituion");
		institutionName=institutionsName[6];
		pageHelper.restartBrowserInNewURL(institutionName, true); 
		institutionId = dbService.getInstituteIdByName(institutionName);
		sleep(2);
		//laureateInstId = institutionId;
	
	report.startStep("Init test Data");
		String className = configuration.getProperty("lau_className");
		String classID = dbService.getClassIdByName(className, institutionId);
		String courseName = "Basic 1";
		String courseId = "43396";
		String studentName = "auto auto";
		studentId = dbService.getUserIdByUserName("auto", institutionId);
		String zoomColumnTypeID = dbService.getGradebookCoulmnTypeIdByInstIdAndName(institutionId,"Zoom");
		String speakingColumnTypeID = dbService.getGradebookCoulmnTypeIdByInstIdAndName(institutionId, "Speaking");
		String writingColumnTypeID = dbService.getGradebookCoulmnTypeIdByInstIdAndName(institutionId, "Writing");
		String zoomColumnId = dbService.getGradebookFirstCoulmnIdByClassCourseAndType(classID, courseId, zoomColumnTypeID);
		String speakingColumnId = dbService.getGradebookFirstCoulmnIdByClassCourseAndType(classID, courseId, speakingColumnTypeID);
		String writingColumnId = dbService.getGradebookFirstCoulmnIdByClassCourseAndType(classID, courseId, writingColumnTypeID);
		String adminId = dbService.getAdminIdByInstId(configuration.getProperty("lau_institutionId"));
		String adminName = dbService.getUserNameById(adminId, institutionId);
		WebElement gradebookElement;
		
	report.startStep("Open New UX Login Page");
		loginPage = new NewUXLoginPage(webDriver, testResultService);

	report.startStep("Login as Admin");
		pageHelper.setUserLoginToNull(adminId);
		NewUXLoginPage loginPage = new NewUXLoginPage(webDriver, testResultService);
		tmsHomePage = loginPage.loginAsTmsUser(adminName, "12345");
		sleep(2);		
	
	report.startStep("Navigate to Gradebook");
		tmsHomePage.switchToMainFrame();
		tmsHomePage.clickOnReports();
		sleep(1);
		tmsHomePage.clickOnGradebook();
		sleep(2);
	
		for (int i=0 ; i< 2 ; i++) {
			report.startStep("Select relevant Course and Class");
				tmsHomePage.switchToFormFrame();
				webDriver.selectElementFromComboBox("SelectClass", className);
				webDriver.selectElementFromComboBox("SelectCourse", courseName);
				tmsHomePage.clickOnGo();
				sleep(2);
				webDriver.switchToTopMostFrame();
				tmsHomePage.switchToMainFrame();
			
			if (i == 0) {
				report.startStep("Enter in-range value for student for each column type");
					webDriver.waitForElement("//*[@id='tblAssesmentGrid_frozen']/tbody/tr/td[contains(@title,'" + studentName + "')]",ByTypes.xpath).click();
					gradebookElement = webDriver.waitForElement("//*[@id='tblAssesmentGrid']/tbody/tr/td[contains(@title,'" + studentName + "')]",ByTypes.xpath);
					gradebookElement.findElement(By.xpath(".//following-sibling::td/input[contains(@name,'" + speakingColumnId + "')]")).sendKeys("20");
					gradebookElement.findElement(By.xpath(".//following-sibling::td/input[contains(@name,'" + zoomColumnId + "')]")).click();
					gradebookElement.findElement(By.xpath(".//following-sibling::td/input[contains(@name,'" + writingColumnId + "')]")).sendKeys("4");

				report.startStep("Navigate out of Gradebook");
					webDriver.switchToTopMostFrame();
					tmsHomePage.switchToMainFrame();
					tmsHomePage.clickOnCourseReports();
					removeScore = true;
					sleep(2);
					
				report.startStep("Navigate back to Gradebook");
					tmsHomePage.clickOnGradebook();
					sleep(2);
			}
			
			else	{
				report.startStep("Verify inserted data was saved");
					gradebookElement = webDriver.waitForElement("//*[@id='tblAssesmentGrid']/tbody/tr/td[contains(@title,'" + studentName + "')]",ByTypes.xpath);
					testResultService.assertEquals("20", gradebookElement.findElement(By.xpath(".//following-sibling::td[contains(@aria-describedby,'tblAssesmentGrid_g" + speakingColumnId + "')]")).getText(), "Speaking grade not as expected");
					testResultService.assertEquals("1",gradebookElement.findElement(By.xpath(".//following-sibling::td[contains(@aria-describedby,'tblAssesmentGrid_g" + zoomColumnId + "')]")).getText(), "Zoom grade not as expected"); 
					testResultService.assertEquals("4", gradebookElement.findElement(By.xpath(".//following-sibling::td[contains(@aria-describedby,'tblAssesmentGrid_g" + writingColumnId + "')]")).getText(), "Writing grade not as expected");
			}
		}
		
	report.startStep("DB cleanup");	
		dbService.deleteStudentgradebookGrades(studentId);
		removeScore = false;
	}

	@Test
	@Category(AngularLearningArea.class)
	@TestCaseParams(testCaseID = { "55354" })//, institutionId = laureateInstId)
	public void testGradebookReport() throws Exception {

	report.startStep("Change to Laurate Instituion");
		institutionName=institutionsName[6];
		pageHelper.restartBrowserInNewURL(institutionName, true); 
		institutionId = dbService.getInstituteIdByName(institutionName);
		sleep(2);
	//	laureateInstId = institutionId;
		
	report.startStep("Init test Data");
		String className = configuration.getProperty("lau_className");
		//String classID = dbService.getClassIdByName(className, laureateInstId);
		String courseId = courses[1]; //"43396"; //Basic 1
		String studentName = "auto2 auto2";
		double [] expectedGrades = {100,75,100,100,100,92,93}; 
		expectedGrades[6] = Math.round(0.2 * expectedGrades[0] + 0.2 * expectedGrades[1] + 0.1 * expectedGrades[2] + 0.15 * expectedGrades[3] + 0.15 * expectedGrades[4] + 0.2 * expectedGrades[5]);     
		String teacherName = configuration.getProperty("teacher.username");
		String teacherId = dbService.getUserIdByUserName(teacherName, institutionId);
		WebElement reportElement;
		
	report.startStep("Open New UX Login Page");
		loginPage = new NewUXLoginPage(webDriver, testResultService);

		report.startStep("Login as teacher");
		pageHelper.setUserLoginToNull(teacherId);
		NewUXLoginPage loginPage = new NewUXLoginPage(webDriver, testResultService);
		tmsHomePage = loginPage.loginAsTmsUser(teacherName, "12345");
		sleep(2);			
	
	report.startStep("Navigate to Gradebook report");
		tmsHomePage.switchToMainFrame();
		tmsHomePage.clickOnReports();
		sleep(1);
		tmsHomePage.clickOnCourseReports();
		sleep(2);
		tmsHomePage.switchToFormFrame();
		tmsHomePage.selectReport("16");
		webDriver.selectElementFromComboBox("SelectClass", className);
		//tmsHomePage.selectClass(className, false, true);
		sleep(1);
		tmsHomePage.clickOnGo();

	report.startStep("Select relevant Course");
		webDriver.switchToTopMostFrame();
		tmsHomePage.switchToMainFrame();
		tmsHomePage.selectCourse(courseId);
			
	report.startStep("Review scores and calculate expected final grade");
		webDriver.switchToFrame(webDriver.waitForElement("GradeBookReport", ByTypes.id));
		
		//reportElement = webDriver.waitForElement("//*[@id='tblTestsReportGrid']/tbody/tr/td[contains(@title,'" + studentName + "')]",ByTypes.xpath,false,3);
		reportElement = webDriver.waitUntilElementAppearsAndReturnElement("//*[@id='9']/td[1]",3);
		
		report.report("Comparing final grade componenets");
		testResultService.assertEquals(expectedGrades[0], Double.parseDouble(reportElement.findElement(By.xpath(".//following-sibling::td[contains(@aria-describedby,'tblTestsReportGrid_Writing')]")).getText()), "Writing grade not as expected");
		testResultService.assertEquals(expectedGrades[1], Double.parseDouble(reportElement.findElement(By.xpath(".//following-sibling::td[contains(@aria-describedby,'tblTestsReportGrid_Speaking')]")).getText()), "Speaking grade not as expected");
		testResultService.assertEquals(expectedGrades[2], Double.parseDouble(reportElement.findElement(By.xpath(".//following-sibling::td[contains(@aria-describedby,'tblTestsReportGrid_Zoom')]")).getText()), "Zoom grade not as expected");
		testResultService.assertEquals(expectedGrades[3], Double.parseDouble(reportElement.findElement(By.xpath(".//following-sibling::td[contains(@aria-describedby,'tblTestsReportGrid_TestAvg')]")).getText()), "Avg test score not as expected");
		testResultService.assertEquals(expectedGrades[4], Double.parseDouble(reportElement.findElement(By.xpath(".//following-sibling::td[contains(@aria-describedby,'tblTestsReportGrid_MidTermTest')]")).getText()), "Mid-term grade not as expected");
		testResultService.assertEquals(expectedGrades[5], Double.parseDouble(reportElement.findElement(By.xpath(".//following-sibling::td[contains(@aria-describedby,'tblTestsReportGrid_CourseTest')]")).getText()), "Course test grade not as expected");
		report.report("Comparing Final grade");
		testResultService.assertEquals(expectedGrades[6], Double.parseDouble(reportElement.findElement(By.xpath(".//following-sibling::td[contains(@aria-describedby,'tblTestsReportGrid_FinalGrade')]")).getText()), "Final grade not as expected");
	}
	
	
	@Test
	@Category(AngularLearningArea.class)
	@TestCaseParams(testCaseID = { "54967","54968","54969" })//, institutionId = laureateInstId)
	public void testEditGradebookColumns() throws Exception {
		
		report.startStep("Change to Laurate Instituion");
		institutionName=institutionsName[6];
		pageHelper.restartBrowserInNewURL(institutionName, true); 
		institutionId = dbService.getInstituteIdByName(institutionName);
		sleep(2);
	//	laureateInstId = institutionId;
		
		report.startStep("Init test Data");
			String className = configuration.getProperty("lau_className");
			String classID = dbService.getClassIdByName(className, institutionId);
			String courseName = "Intermediate 1";
			String courseId = "44845";
			//String teacherName = configuration.getProperty("teacher.username");
			//String teacherId = dbService.getUserIdByUserName(teacherName, laureateInstId);
			String [] columns = {"Writing","Speaking","Zoom"};
			String zoomColumnTypeID = dbService.getGradebookCoulmnTypeIdByInstIdAndName(institutionId, "Zoom");
			String speakingColumnTypeID = dbService.getGradebookCoulmnTypeIdByInstIdAndName(institutionId, "Speaking");
			String writingColumnTypeID = dbService.getGradebookCoulmnTypeIdByInstIdAndName(institutionId, "Writing");
		
		report.startStep("Open New UX Login Page");
			loginPage = new NewUXLoginPage(webDriver, testResultService);

		report.startStep("Login as admin");
			//pageHelper.setUserLoginToNull(teacherId);
			NewUXLoginPage loginPage = new NewUXLoginPage(webDriver, testResultService);
			tmsHomePage = loginPage.loginAsTmsUser("admin", "12345");
			sleep(2);		
	
		report.startStep("Navigate to Gradebook");
			tmsHomePage.switchToMainFrame();
			tmsHomePage.clickOnReports();
			sleep(1);
			//tmsHomePage.clickOnGradebook();
			tmsHomePage.clickOnGradebookSettings();
			sleep(2);
			
		report.startStep("Select relevant Course and Class");
			tmsHomePage.switchToFormFrame();
			//webDriver.selectElementFromComboBox("SelectClass", className);
			webDriver.selectElementFromComboBox("SelectCourse", courseName);
			tmsHomePage.clickOnGo();
			sleep(2);
			webDriver.switchToTopMostFrame();
			tmsHomePage.switchToMainFrame();	
			
		report.startStep("Add 1 column from each type");
			removeColumns = true;
			for (int i = 0 ; i < 3 ; i++) {
				report.report("Adding column " + (i + 1) + " to the gradebook");
				tmsHomePage.clickOnPromotionAreaMenuButton("Add");
				sleep(3); 
				webDriver.switchToPopup();
				webDriver.selectElementFromComboBox("gradebookCol__selectCol", columns[i]);
				webDriver.waitForElement("gradebookCol__selectName", ByTypes.id).sendKeys(columns[i]);
				sleep(1);
				webDriver.waitForElement("gradebookCol__save", ByTypes.id).click();
				sleep(1);
				try {
					if (webDriver.getAlertText(3).equals("Column with this caption already exists for the column type!"));
						testResultService.addFailTest("Column " + columns[i] + " already exists", true, true);
				}
				catch (Exception e) {
					report.report("Column inserted successfully");
				}
				webDriver.switchToMainWindow();
				webDriver.switchToTopMostFrame();
				tmsHomePage.switchToMainFrame();
				webDriver.printScreen();
			}
		
		report.startStep("Change name of all 3 columns");
			String [] columnId = {dbService.getGradebookFirstCoulmnIdByClassCourseAndType(classID, courseId, writingColumnTypeID),
					dbService.getGradebookFirstCoulmnIdByClassCourseAndType(classID, courseId, speakingColumnTypeID),
					dbService.getGradebookFirstCoulmnIdByClassCourseAndType(classID, courseId, zoomColumnTypeID)};
			for (int i = 0 ; i < 3 ; i++) {
				tmsHomePage.clickOnPromotionAreaMenuButton("Edit");
				sleep(1);
				webDriver.switchToPopup();
				webDriver.selectElementFromComboBox("gradebookCol__selectCol", columns[2-i]);
				webDriver.waitForElement("gradebookCol__selectName", ByTypes.id).clear();
				columns[2-i] = columns[2-i] + dbService.sig(3);
				webDriver.waitForElement("gradebookCol__selectName", ByTypes.id).sendKeys(columns[2-i]);
				webDriver.waitForElement("gradebookCol__save", ByTypes.id).click();
				sleep(2);
				webDriver.switchToMainWindow();
				webDriver.switchToTopMostFrame();
				tmsHomePage.switchToMainFrame();
			}
		
		report.startStep("Verify new names existance");
			for (int i = 0 ; i < 3 ; i++) {
				testResultService.assertEquals(columns[i], webDriver.waitForElement("tblAssesmentGrid_" + columnId[i], ByTypes.id).getAttribute("title"), "Column don't match expected");
			}
		
		
		report.startStep("Delete all 3 columns");
			for (int i = 0 ; i < 3 ; i++) {
				tmsHomePage.clickOnPromotionAreaMenuButton("Delete");
				sleep(1);
				webDriver.switchToPopup();
				webDriver.selectElementFromComboBox("gradebookCol__selectCol", columns[i]);
				webDriver.waitForElement("gradebookCol__delete", ByTypes.id).click();
				webDriver.closeAlertByAccept();
				webDriver.switchToMainWindow();
				webDriver.switchToTopMostFrame();
				tmsHomePage.switchToMainFrame();
			}
		
		report.startStep("Verify columns deletetion");	
			for (int i = 0 ; i < 3 ; i++) {
				webDriver.checkElementNotExist("//*[@id='tblAssesmentGrid_" + columnId[i] + "']", "Column " + columns[i] + " wasn't deleted");
			}
			removeColumns = false;
	}

	@Test
	@Category(AngularLearningArea.class)
	@TestCaseParams(testCaseID = { "55216","55005" })//, institutionId = laureateInstId)
	public void testGradebookFinalScore() throws Exception {

		report.startStep("Change to Laurate Instituion");
			institutionName=institutionsName[6];
			pageHelper.restartBrowserInNewURL(institutionName, true); 
			institutionId = dbService.getInstituteIdByName(institutionName);
			sleep(2);

		report.startStep("Init test Data");
			String className = configuration.getProperty("lau_className");
			String classID = dbService.getClassIdByName(className, institutionId);
			String courseName = "Basic 2";
			String courseId = "43870";
			String studentName = "auto auto";
			studentId = dbService.getUserIdByUserName("auto", institutionId);
			String zoomColumnTypeID = dbService.getGradebookCoulmnTypeIdByInstIdAndName(institutionId, "Zoom");
			String speakingColumnTypeID = dbService.getGradebookCoulmnTypeIdByInstIdAndName(institutionId, "Speaking");
			String writingColumnTypeID = dbService.getGradebookCoulmnTypeIdByInstIdAndName(institutionId, "Writing");
			String [] columnId = {dbService.getGradebookFirstCoulmnIdByClassCourseAndType(classID, courseId, writingColumnTypeID),
					dbService.getGradebookFirstCoulmnIdByClassCourseAndType(classID, courseId, zoomColumnTypeID),
					dbService.getGradebookFirstCoulmnIdByClassCourseAndType(classID, courseId, speakingColumnTypeID),
					dbService.getGradebookLastCoulmnIdByClassCourseAndType(classID, courseId, writingColumnTypeID),
					dbService.getGradebookLastCoulmnIdByClassCourseAndType(classID, courseId, zoomColumnTypeID),
					dbService.getGradebookLastCoulmnIdByClassCourseAndType(classID, courseId, speakingColumnTypeID)};
			String adminId = dbService.getAdminIdByInstId(institutionId);
			String adminName = dbService.getUserNameById(adminId, institutionId);
			WebElement gradebookElement;
		
		report.startStep("Open New UX Login Page");
			loginPage = new NewUXLoginPage(webDriver, testResultService);
	
		report.startStep("Login as Admin");
			pageHelper.setUserLoginToNull(adminId);
			NewUXLoginPage loginPage = new NewUXLoginPage(webDriver, testResultService);
			tmsHomePage = loginPage.loginAsTmsUser(adminName, "12345");
			sleep(2);		
	
		report.startStep("Navigate to Gradebook");
			tmsHomePage.switchToMainFrame();
			tmsHomePage.clickOnReports();
			sleep(1);
			tmsHomePage.clickOnGradebook();
			sleep(2);
		
		report.startStep("Select relevant Course and Class");
			tmsHomePage.switchToFormFrame();
			webDriver.selectElementFromComboBox("SelectClass", className);
			webDriver.selectElementFromComboBox("SelectCourse", courseName);
			tmsHomePage.clickOnGo();
			sleep(2);
			webDriver.switchToTopMostFrame();
			tmsHomePage.switchToMainFrame();
			

		report.startStep("Enter in-range value for student for each column type");
			webDriver.waitForElement("//*[@id='tblAssesmentGrid_frozen']/tbody/tr/td[contains(@title,'" + studentName + "')]",ByTypes.xpath).click();
			gradebookElement = webDriver.waitForElement("//*[@id='tblAssesmentGrid']/tbody/tr/td[contains(@title,'" + studentName + "')]",ByTypes.xpath);
			gradebookElement.findElement(By.xpath(".//following-sibling::td/input[contains(@name,'" + columnId[0] + "')]")).clear();
			gradebookElement.findElement(By.xpath(".//following-sibling::td/input[contains(@name,'" + columnId[0] + "')]")).sendKeys("3"); // 4
			if (gradebookElement.findElement(By.xpath(".//following-sibling::td/input[contains(@name,'" + columnId[1] + "')]")).getAttribute("checked") == null) {
				gradebookElement.findElement(By.xpath(".//following-sibling::td/input[contains(@name,'" + columnId[1] + "')]")).click();
			}
			
			gradebookElement.findElement(By.xpath(".//following-sibling::td/input[contains(@name,'" + columnId[2] + "')]")).clear();
			gradebookElement.findElement(By.xpath(".//following-sibling::td/input[contains(@name,'" + columnId[2] + "')]")).sendKeys("13"); // 13
			//gradebookElement.findElement(By.xpath(".//following-sibling::td/input[contains(@name,'" + columnId[3] + "')]")).sendKeys("3");
			//gradebookElement.findElement(By.xpath(".//following-sibling::td/input[contains(@name,'" + columnId[4] + "')]")).click();
			//gradebookElement.findElement(By.xpath(".//following-sibling::td/input[contains(@name,'" + columnId[5] + "')]")).sendKeys("20");
			removeScore = true;

		report.startStep("Verify Final score column");
			webDriver.getElement(By.className("resultRoot")).click();
			sleep(2); // Added 1 sec sleep to verify calculation were made.
			webDriver.printScreen("After grades insertion");
			gradebookElement = webDriver.waitForElement("//*[@id='tblAssesmentGrid']/tbody/tr/td[contains(@title,'" + studentName + "')]",ByTypes.xpath);
			testResultService.assertEquals("75", gradebookElement.findElement(By.xpath(".//following-sibling::td[contains(@aria-describedby,'tblAssesmentGrid_t" + writingColumnTypeID + "')]")).getText(), "Writing Final Score not as expected");
			testResultService.assertEquals("100",gradebookElement.findElement(By.xpath(".//following-sibling::td[contains(@aria-describedby,'tblAssesmentGrid_t" + zoomColumnTypeID + "')]")).getText(), "Zoom Final Score not as expected"); 
			testResultService.assertEquals("65", gradebookElement.findElement(By.xpath(".//following-sibling::td[contains(@aria-describedby,'tblAssesmentGrid_t" + speakingColumnTypeID + "')]")).getText(), "Speaking Final Score not as expected");
		
		report.startStep("Logout from TMS");
			tmsHomePage.clickOnExit();
			//sleep(5);
			testResultService.assertEquals(false, loginPage.isSubmitButtonEnabled(), "Checking that Login Page displayed");
			webDriver.printScreen("After logout from TMS");
			
		report.startStep("Login to ED and enter My Progress");
			pageHelper.setUserLoginToNull(studentId);
			loginPage.loginAsStudent(dbService.getUserNameById(studentId, institutionId),"12345");
			homePage.waitHomePageloadedFully();
			homePage.closeModalPopUp();
			//webDriver.printScreen("After login to ED");
			homePage.navigateToRequiredCourseOnHomePage(courseName);
			myProgressPage = homePage.clickOnMyProgress();
			myProgressPage.waitUntilPageIsLoaded();
		
		report.startStep("Examine grades on My progress");
		try {
			testResultService.assertEquals("75", myProgressPage.getGradebookWritingScore(), "Writing Final Score not as expected");
			testResultService.assertEquals("100", myProgressPage.getGradebookZoomScore(), "Zoom Final Score not as expected");
			testResultService.assertEquals("65", myProgressPage.getGradebookSpeakingScore(), "Speaking Final Score not as expected");
		} catch (Exception e) {
			testResultService.addFailTest("Graebook score was not found in my progress", false, true);
		}
		
		report.startStep("DB cleanup");
			dbService.deleteStudentgradebookGrades(studentId);
			removeScore = false;
	}

	public void clickToOnlineLessons () throws Exception{
		
		webDriver.waitForElement("//li[contains(@class,'settingsMenu__personal')]", ByTypes.xpath).click();
		sleep(1);
		webDriver.waitForElement("//li[contains(@class,'settingsMenu__listItem settingsMenu__listItem_onlineLessons ng-scope')]", ByTypes.xpath).click();
		sleep(1);
		webDriver.waitForElement("//div[contains(@class,'acuityZoomSchedule__schduleInfoBtnW')]", ByTypes.xpath).click();
	}
	
	public void verifyAndRemovePreviousMeetings() throws Exception{
		webDriver.waitForElement("//li[contains(@class,'settingsMenu__personal')]", ByTypes.xpath).click();
		sleep(1);
		webDriver.waitForElement("//li[contains(@class,'settingsMenu__listItem settingsMenu__listItem_onlineLessons ng-scope')]", ByTypes.xpath).click();
		sleep(1);
		WebElement previous = webDriver.waitForElement("//*[@id='mainCtrl']/div[3]/div/div/div[2]/div[2]/div/div/div/div[2]/div/div",ByTypes.xpath, 20 , false);
		if (previous != null) {
			webDriver.waitForElement("//*[@id='mainCtrl']/div[3]/div/div/div[2]/div[2]/div/div/div/div[2]/div/div/div[4]",ByTypes.xpath).click();
			sleep(3);
			webDriver.switchToNextTab();
			cancelMeeting();
		}
		webDriver.waitForElementAndClick("//*[@id='mainCtrl']/div[3]/div/div/div[1]/a", ByTypes.xpath);
	}
	
	public void studentmeetingED () throws Exception{
	
	webDriver.waitForElement("//div[contains(@id,'step-pick-appointment')]", ByTypes.xpath).click();
	sleep(1);
	
	//WebElement rightArrowMonth = webDriver.waitForElement("//th[@align='right']//a", ByTypes.xpath);
	//webDriver.ClickElement(rightArrowMonth);
	
	TextService textService = new TextService();
	String currentDate = pageHelper.getCurrentDateByFormat("yyyy-MM-dd");
	String tommorow = textService.updateTime(currentDate,"add", "day",1);
	
	WebElement tommorowCalendar = webDriver.waitForElement("//td[@day='"+tommorow+"']", ByTypes.xpath);
	
	tommorowCalendar.click();
	
	WebElement firstHourAppt = webDriver.waitForElement("//label[contains(@id,'lbl_appt')][1]", ByTypes.xpath);
	
	firstHourAppt.click();
	
	WebElement continuteButton = webDriver.waitForElement("//div[@class='choose-time-actions']//li[1]", ByTypes.xpath);
	
	continuteButton.click();
	
	webDriver.waitForElement("//*[@id='email']",ByTypes.xpath).sendKeys("igorp@test.com");
	
	webDriver.waitForElement("//*[@id='custom-forms']/div[2]/div/input",ByTypes.xpath).click();
	
	/*webDriver.waitForElement("//*[contains(@class,'btn btn-primary margin-top-small')]", ByTypes.xpath).click();
	sleep(1);
	webDriver.waitForElementAndClick("//*[@id='step-pick-appointment']/div[3]/div[3]/div[1]/table[2]/tbody/tr[7]/td[5]", ByTypes.xpath);
	sleep(1);
	webDriver.waitForElement("//*[@id='step-pick-appointment']/div[3]/div[3]/div[2]",ByTypes.xpath).click();
	sleep(1);
	webDriver.waitForElement("//*[@id='step-pick-appointment']/div[3]/div[3]/div[2]/div[2]/a[1]",ByTypes.xpath).click();
	sleep(1);
	webDriver.waitForElement("//*[@id='email']",ByTypes.xpath).sendKeys("igorp@test.com");
	sleep(2);
	webDriver.waitForElement("//*[@id='custom-forms']/div[2]/div/input",ByTypes.xpath).click();
	sleep(3);*/
	}
	
	public void checkstudentmeetingED () throws Exception{

	webDriver.waitForElement("//li[contains(@class,'settingsMenu__personal')]", ByTypes.xpath).click();
	sleep(1);
	webDriver.waitForElement("//li[contains(@class,'settingsMenu__listItem settingsMenu__listItem_onlineLessons ng-scope')]", ByTypes.xpath).click();
	sleep(5);
	webDriver.waitForElement("//*[@id='mainCtrl']/div[3]/div/div/div[2]/div[2]/div/div/div/div[2]/div/div",ByTypes.xpath);
	sleep(1);
	webDriver.waitForElement("//*[@id='mainCtrl']/div[3]/div/div/div[2]/div[2]/div/div/div/div[2]/div/div/div[4]",ByTypes.xpath).click();
	sleep(3);
	}
	
	public void rescheduleMeeting() throws Exception{
		webDriver.waitForElement("//*[@id='appt-reschedule']",ByTypes.xpath).click();
		sleep(2);
		webDriver.waitForElementAndClick("//*[@id='popup-content']", ByTypes.xpath);
		sleep(1);
		webDriver.waitForElement("//*[@id='reschedule-form']/div[1]/div[1]/table[2]/tbody/tr[7]/td[5]",ByTypes.xpath).click();
		sleep(1);
		webDriver.waitForElementAndClick("//*[@id='lbl_appt1564160400']", ByTypes.xpath);
		sleep(1);
		webDriver.closeNewTab(1);
		
	}
	
	public void cancelMeeting() throws Exception{
	webDriver.waitForElement("//*[@id='appt-cancel']",ByTypes.xpath).click();
	sleep(3);
	webDriver.waitForElement("//*[@id='popup-content']/a[1]",ByTypes.xpath).click();
	webDriver.closeNewTab(1);
	sleep(1);
	
	webDriver.waitForElement("//li[contains(@class,'settingsMenu__personal')]", ByTypes.xpath).click();
	sleep(1);
	webDriver.waitForElement("//li[contains(@class,'settingsMenu__listItem settingsMenu__listItem_onlineLessons ng-scope')]", ByTypes.xpath).click();
	
	}
	
	public void checkMeetingNotExist () throws Exception {
		
		webDriver.checkElementNotExist("//*[@id='mainCtrl']/div[3]/div/div/div[2]/div[2]/div/div/div/div[2]/div/div", "exist");
		webDriver.waitForElementAndClick("//*[@id='mainCtrl']/div[3]/div/div/div[1]/a", ByTypes.xpath);
		sleep(5);
	}
	
	@After
	public void tearDown() throws Exception {
		
		if (removeScore) {
			dbService.deleteStudentgradebookGrades(dbService.getUserIdByUserName("auto", institutionId));
		}
		
		if (removeColumns) {
			String courseId = "44845"; // Intermediate 1
			String classID = dbService.getClassIdByName(configuration.getProperty("lau_className"), institutionId);
			dbService.deletegradebookColumns(courseId, classID);
		}
		
		super.tearDown();
	}
		
}
