package tests.tms;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.support.ui.Select;

import pageObjects.edo.NewUXLoginPage;
import pageObjects.edo.NewUxDragAndDropSection;
import pageObjects.edo.NewUxDragAndDropSection2;
import pageObjects.edo.NewUxLearningArea;
import pageObjects.edo.NewUxLearningArea2;
import pageObjects.tms.CurriculumAssignCoursesPage;
import pageObjects.tms.CurriculumAssignPackagesPage;
import pageObjects.tms.CurriculumAuthoringToolPage;
import pageObjects.tms.CurriculumCourseSequencePage;
import pageObjects.tms.CurriculumViewAllCoursesPage;
import pageObjects.tms.TmsHomePage;
import testCategories.AngularLearningArea;
import testCategories.NonAngularLearningArea;
import Enums.ByTypes;
import Interfaces.TestCaseParams;
import tests.edo.newux.BasicNewUxTest;

public class AssignmentsTests extends BasicNewUxTest  {

	NewUXLoginPage loginPage;
	TmsHomePage tmsHomePage;
	NewUxLearningArea learningArea;
	NewUxLearningArea2 learningArea2;
	NewUxDragAndDropSection dragAndDrop;
	NewUxDragAndDropSection2 dragAndDrop2;
	
	private boolean resetCourseOrder;
	private boolean deleteATComponent;
	String compId;
	private String courseOriginalOrder;
	
	WebElement chkPkg;
	
	String pkgID = "";
	//String instID = "";
	
	String chkClassID = "";
	String chkClassName = "";
	
	String chkUserId = "";  // igb 2018.07.16
	
	
	@Before
	public void setup() throws Exception {
		super.setup();
	}

	@Test
	@Category(NonAngularLearningArea.class)
	@TestCaseParams(testCaseID = { "29615","44358" })
	public void testViewAllCourse() throws Exception {

		report.startStep("Open New UX Login Page");
			loginPage = new NewUXLoginPage(webDriver, testResultService);
		
		report.startStep("Login as Admin");
			tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
			homePage.closeAllNotifications();
		
		report.startStep("Open Curriculum");
			tmsHomePage.switchToMainFrame();	
			tmsHomePage.clickOnCurriculum();		
			sleep(2);
		
		report.startStep("Open View All Coures");
			tmsHomePage.clickOnViewAllCourses();
			sleep(4);  // Increased sleep due to elements loading later than expected.
		
		report.startStep("Open lesson and close with minimize button");
			
			expandListItemAndClick("//*[@id='Package_22983']/td[3]/img");	//Expanding package content
			expandListItemAndClick("//*[@id='Course_43396']/td[4]/img");	//Expanding course content
			expandListItemAndClick("//*[@id='Unit_43397']/td[5]/img");		//Expanding unit content
			sleep(8);   // Bypassing TMS bug we don't want to fix.
			webDriver.waitForElement("//*[@id='Package_22983']/td[3]/img", ByTypes.xpath,"Course list not closed").click();
			webDriver.checkElementNotExist("//*[@id='Com_60']/td[5]/a/img", "Preview icon still here");
			
		report.startStep("Navigate to B1->U1->L1 with window inspection");	
			expandListItemAndClick("//*[@id='Package_22983']/td[3]/img");
			expandListItemAndClick("//*[@id='Course_43396']/td[4]/img");
			expandListItemAndClick("//*[@id='Unit_43397']/td[5]/img");
			webDriver.waitForElement("//*[@id='Com_60']/td[5]/a/img", ByTypes.xpath,"Preview icon doesn't exist").click();
			webDriver.switchToNextTab();
		
		// TODO Add course \ unit info window	
			
		report.startStep("Check landed on expected Unit - Lesson - Step");
			learningArea = new NewUxLearningArea(webDriver, testResultService);
			learningArea.checkUnitLessonStepNameOnLanding("Unit 1: Meet A Rock Star", "Art", "Explore");
			
		report.startStep("Press on Hear btn");
			sleep(2);
			learningArea.clickOnPlayVideoButton();
			sleep(2);
		/*	
		report.startStep("Navigate to S2->T2");
			learningArea.clickOnStep(2);
			learningArea.clickOnTask(1);
			sleep(1);
		
		report.startStep("Select 2 correct 1 wrong answer");
			learningArea.selectAnswerRadioByTextNum(1, 1);
			sleep(1);
			learningArea.selectAnswerRadioByTextNum(2, 5);
			sleep(1);
			learningArea.selectAnswerRadioByTextNum(3, 8);
			sleep(1);
			
		report.startStep("Click on Check Answer and check V/X signs placed correctly");
			learningArea.clickOnCheckAnswer();
			sleep(1);
			learningArea.verifyAnswerRadioSelectedCorrect(1, 1);
			learningArea.verifyAnswerRadioSelectedCorrect(3, 8);
			learningArea.verifyAnswerRadioSelectedWrong(2, 5);
			report.report("Verifying Practice Tools State");
			learningArea.verifyPracticeToolsStateOnCheckAnswer();
		
		report.startStep("Submit Test and check test results displayed");
			learningArea.clickOnStep(3);
			sleep(1);
			learningArea.clickTheStartTestButon();
			sleep(1);
			learningArea.clickOnNextButton(2);
			sleep(1);
			learningArea.selectMultipleAnswer(1);
			sleep(1);
			learningArea.clickOnNextButton(2);
			sleep(1);
			learningArea.selectMultipleAnswer(3);
			sleep(1);
			learningArea.submitTest(true);
			report.report("Returning to Test tab"); // Submit changes tab in used function.
			webDriver.switchToNextTab();
			webDriver.switchToTopMostFrame();
			learningArea.checkThatTestWasSubmitted();
			learningArea.checkThatNextButtonIsNotDisplayedInLA();
			
		report.startStep("Select task and verify Correct/Your Answer tabs displayed");
			learningArea.clickOnTask(2);
			sleep(1);
			learningArea.clickOnCorrectAnswerTab();
			sleep(1);
			learningArea.clickOnYourAnswerTab();
			learningArea.checkThatNextButtonIsNotDisplayedInLA();
			
		report.startStep("Open Lesson List and check no progress indication");
			learningArea.openLessonsList();
			learningArea.checkThatLessonListHasNoProgressBars();
			
		report.startStep("Select Lesson 3 and check check landed on expected Unit - Lesson - Step");
			learningArea.clickOnLessoneByIndex(4);
			sleep(3);
			learningArea.checkUnitLessonStepNameOnLanding("Unit 1: Meet A Rock Star", "Be: Affirmative", "Explore");
		*/
		report.startStep("Logout from TMS and check ED tab closed");
			logoutFromTmsAndCheckEdTabsClosed();
	}
	
	@Test
	@Category(AngularLearningArea.class)
	@TestCaseParams(testCaseID = { "29615","44358" })
	public void testViewAllCourse2() throws Exception {
		
		report.startStep("Open New UX Login Page");
			loginPage = new NewUXLoginPage(webDriver, testResultService);
			String institutionId = configuration.getProperty("institution.id");
		 String packageid = dbService.getInstitutionPackageIDByPackageId(institutionId, "3188"); 
		
		report.startStep("Login as Admin");
			tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
			sleep(2);
		
		// Initialize curriculum view all courses page
			CurriculumViewAllCoursesPage curriculumViewAllCoursesPage = new CurriculumViewAllCoursesPage(webDriver, testResultService);
		
		report.startStep("Click on Curriculum -> View All Courses");
			curriculumViewAllCoursesPage.goToCurriculumViewAllCourses();
			
		report.startStep("Open Package");
			curriculumViewAllCoursesPage.expandListItemAndClick("@name",packageid);	//Expanding package content
			
		report.startStep("Open Course Info and Check Name and Description");
			curriculumViewAllCoursesPage.openCourseInfoAndCheckDescription("Basic 1");
			
		report.startStep("Check Eye Sign Is Not Displayed");
			curriculumViewAllCoursesPage.checkEyeSignIsNotDisplayed();
			
		report.startStep("Open Course");
			curriculumViewAllCoursesPage.expandListItemAndClick("@name","43396");	//Expanding course content
			
		report.startStep("Open Unit Info and Check Name and Description");
			curriculumViewAllCoursesPage.openUnitInfoAndCheckDescription("Meet A Rock Star");
			
		report.startStep("Check Eye Sign Is Not Diplayed");
			curriculumViewAllCoursesPage.checkEyeSignIsNotDisplayed();
			
		report.startStep("Open Unit");
			curriculumViewAllCoursesPage.expandListItemAndClick("@id","43397");		//Expanding unit content
			sleep(2);
			
		report.startStep("Open Lesson Info and Check Name and Description");
			curriculumViewAllCoursesPage.openLessonInfoAndCheckDescription("Art"); //Listening - Art
			sleep(6);
			
		report.startStep("Check Eye Sign Is Diplayed and Click it");
			curriculumViewAllCoursesPage.checkEyeSignIsDisplayed();
			curriculumViewAllCoursesPage.clickEyeSign();
			webDriver.switchToNextTab();
			sleep(6);
		
		report.startStep("Check landed on expected Unit - Lesson - Step");
			learningArea2 = new NewUxLearningArea2(webDriver, testResultService);
			learningArea2.checkUnitLessonStepNameOnLanding("Unit 1: Meet A Rock Star", "Art", "Explore");
			
		report.startStep("Press on Hear btn");
			sleep(2);
			learningArea2.clickOnNextButton();
			learningArea2.clickOnPlayVideoButton();
			sleep(2);	
			
		report.startStep("Logout from TMS and check ED tab closed");
			logoutFromTmsAndCheckEdTabsClosed();	
	}

	@Test
	@Category(AngularLearningArea.class)
	@TestCaseParams(testCaseID = { "49859" })
	public void testSetPackage2() throws Exception {
		
		report.startStep("Init test data");
			String packageId = "3188";
			String institutionId = configuration.getProperty("institution.id");
			String remainingLicenses = dbService.getRemainingLicensesByPackageId(institutionId, packageId);
			String institutionPackageId = dbService.getInstitutionPackageIDByPackageId(institutionId, packageId);
			int remainingLicensesInt = Integer.parseInt(remainingLicenses);
			String newRemainingLicenses;
			String tmsRemainingValue;
			String pressedAnswer = "question-1_answer-1";
			
		report.startStep("Navigate I1-U10-L4-S5-T4 as new student");
			loginPage = new NewUXLoginPage(webDriver, testResultService);
			createUserAndLoginNewUXClass();
			homePage.closeAllNotifications();
			learningArea2 = homePage.navigateToTask("I1", 10, 4, 5, 4);
			
		report.startStep("Perform progress inducing action");
			learningArea2.SelectRadioBtn(pressedAnswer);
		    sleep(2);
		    learningArea2.clickOnNextButton();
		
		 report.startStep("logout of ED");   
		   learningArea2.clickOnLogoutLearningArea();
		   testResultService.assertEquals(false, loginPage.isSubmitButtonEnabled(), "Checking that ED Login Page displayed");
		
		 report.startStep("Verify new remaining lessons");  
		 	newRemainingLicenses = dbService.getRemainingLicensesByPackageId(institutionId, packageId);
		 	int newRemainingLicensesInt = Integer.parseInt(newRemainingLicenses);
		 	testResultService.assertEquals(newRemainingLicensesInt, remainingLicensesInt - 1, "License wasn't used");
		 
		 report.startStep("Login to TMS as Admin");
		 	tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
			sleep(2);
			
		 report.startStep("Navigate to Assign Package");
		 	tmsHomePage.switchToMainFrame();	
			tmsHomePage.clickOnCurriculum();		
			sleep(2);
			tmsHomePage.clickOnAssignPackages();
			//tmsHomePage.clickOnRegistration();
			//tmsHomePage.clickOnClasses();
			sleep(2);
			
		 report.startStep("Verify used license in package");
		 	tmsRemainingValue = webDriver.waitForElement("remainingPackagesTitle" + institutionPackageId, ByTypes.id).getAttribute("value");
		 	testResultService.assertEquals(tmsRemainingValue, newRemainingLicenses, "tms and DB aren't equal");
		}

	@Test
	@Category(AngularLearningArea.class)
	@TestCaseParams(testCaseID = { "29333" })
	public void testChangeCourseSequence() throws Exception {
		
		report.startStep("Init test data");
			String className = configuration.getProperty("classname.assessment");
			String classId = dbService.getClassIdByName(className, configuration.getProperty("institution.id"));
			String[] newCoursesOrder = { "First Discoveries", "Advanced 1","Advanced 2", "Advanced 3", "Basic 1", "Basic 2", "Basic 3", "Intermediate 1", "Intermediate 2", "Intermediate 3"};
			List<String> currentCourseList = dbService.getClassCourseSequence(classId);
			courseOriginalOrder = String.join(", ", currentCourseList);
			
			report.startStep("Get random user");
			String[] users = getUserNamePassId(institutionId, className);
			String studentName = users[0];
			
		
		report.startStep("Login as admin");
			loginPage = new NewUXLoginPage(webDriver, testResultService);

			pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(configuration.getProperty("schoolAdmin.user"),institutionId));
			tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
			homePage.closeAllNotifications();
			webDriver.waitForJqueryToFinish();
			
		// Initialize curriculum course sequence page
			CurriculumCourseSequencePage curriculumCourseSequencePage = new CurriculumCourseSequencePage(webDriver, testResultService);
			
		report.startStep("Click on Curriculum -> Course Sequence");
			curriculumCourseSequencePage.goToCurriculumCourseSequence();
		
		 report.startStep("Select class and press Go");
		 	tmsHomePage.switchToFormFrame();
		 	tmsHomePage.selectClass(className, false, true);
		 	sleep(2);
		 
		 report.startStep("Move FD to after I3 and save");	
		 	curriculumCourseSequencePage.changeSequenceOfSpecificCourse("First Discoveries", "Intermediate 3");
		 	//sleep(2);
		 	resetCourseOrder = true;
		
		 try {
			 report.startStep("Logout of TMS");
			 	tmsHomePage.clickOnExit();
				testResultService.assertEquals(false, loginPage.isSubmitButtonEnabled(), "Checking that Login Page displayed");
				//sleep(4);
			
			report.startStep("Get into while loop");
			int iteration = 0;
			while (iteration < 2) {
				 report.startStep("Login to ED");
				 	pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(studentName,institutionId));
				 	loginPage.loginAsStudent(studentName,"12345");
				 
				 	homePage.closeAllNotifications();
				 	
					
				 report.startStep("Verify Course sequence in carousel");
				 	verifyCourseSequense(iteration,newCoursesOrder);
				 	
				 	
				report.startStep("Logout of ED");
					homePage.clickOnLogOut();
				
				if (iteration == 0) {
					report.startStep("Run SQL to set the original course order");
						dbService.setClassCourseSequence(classId, courseOriginalOrder);
						sleep(2);
				} else {
					resetCourseOrder = false;
				}
				iteration++;
			}
		 } finally {
			 report.startStep("Run SQL to set the original course order");
			 	dbService.setClassCourseSequence(classId, courseOriginalOrder);
		}
	}

	private void verifyCourseSequense(int iteration, String[] newCoursesOrder) throws Exception {
		
		homePage.navigateToRequiredCourseOnHomePage("First Discoveries");
		for (int i = 0 ; i < coursesNames.length ; i++) {
			sleep(1);
			if (iteration == 0) {
				testResultService.assertEquals(homePage.getCurrentCourseName(), newCoursesOrder[i], "Sequence don't match");
			} else {
				testResultService.assertEquals(homePage.getCurrentCourseName(), coursesNames[i], "Sequence wasn't modified");
			}
		 	homePage.carouselNavigateNext();
		 	homePage.waitTillCarouselLoad();
		 }
		
	}
	
	
	@Test
	@TestCaseParams(testCaseID = { "" })
	public void assignPackagesViaImportCSV() throws Exception {
		
	report.startStep("Change to Courses Instituion");
		institutionName=institutionsName[2];
		pageHelper.restartBrowserInNewURL(institutionName, true); 
		sleep(2);
		
		
	report.startStep("Specify test data");
		List<String[]> list = new ArrayList<>();
		String tailNumber = dbService.sig(4);
		String newClass = "Class-" + tailNumber;
		String fileName = "AddPackageToNewClass"+tailNumber+".csv";
		//String filePath = "\\\\"+configuration.getGlobalProperties("logserverName")+"\\AutoLogs\\ToolsAndResources\\Shared\\ToeicStudentsToImport\\"+fileName;	
		String filePath = "\\"+String.join("\\",pageHelper.buildPathForExternalPages.substring(4).split("//"))+"\\Languages\\"+fileName;
		
	try {
		
	report.startStep("Login as Admin");
		loginPage = new NewUXLoginPage(webDriver, testResultService);
		pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(configuration.getProperty("schoolAdmin.user"),configuration.getProperty("institution.id")));
		sleep(1);
		tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
		homePage.closeAllNotifications();
		
		
		
	report.startStep("Open Curriculum");
		tmsHomePage.switchToMainFrame();
		tmsHomePage.clickOnCurriculum();		
		sleep(1);		
		
	report.startStep("Open Assign Packages");
		tmsHomePage.clickOnAssignPackages();
		sleep(2);
			
	report.startStep("Prepare data for CSV file to assign package");
		CurriculumAssignPackagesPage cap = new CurriculumAssignPackagesPage(webDriver, testResultService);
		String packageName = cap.getNameOfFirstPackage();
		String startPackageDateFromUI = cap.getStartDateOfFirstTopPackage();
		List<String[]> instPackage = dbService.getStartDateOfInstitutionPackage(institutionId,packageName);
		String startPackageDate = instPackage.get(0)[0].split(" ")[0];
		LocalDate current = LocalDate.parse(startPackageDate);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
		startPackageDate = current.format(formatter);
		
	report.startStep("Create CSV file with package-name and store here: "+filePath);
		list = cap.prepareListForCSVfile(newClass, packageName, startPackageDate, institutionId, false);
		textService.writeArrayistToCSVFile(filePath, list);
		
				
	report.startStep("Click On Import");
		tmsHomePage.clickOnPromotionAreaMenuButton("Import");
		sleep(3);
		webDriver.switchToPopup();
	
	report.startStep("Upload CSV into input area and click 'Upload'");
		try {
			webDriver.executeJsScript("document.getElementById('file-upload').style.display='block'");
			WebElement input = webDriver.waitForElement("file-upload", ByTypes.id);
			input.sendKeys(filePath);
			WebElement upload = webDriver.waitForElement("//span[text()='Upload']", ByTypes.xpath);
			upload.click();
		}catch (Exception e) {
			testResultService.addFailTest("Something wrong with input element", true, true);
		}	
		
	report.startStep("Verify message after upload and close the pop-up");
		WebElement messageAfterUpload = webDriver.waitForElement("//div[@class='selected-file__upload--error ng-star-inserted']", ByTypes.xpath);
		textService.assertEquals("Upload unsuccessfull", "Upload successful", messageAfterUpload.getText().trim());
		webDriver.executeJsScript("document.getElementById('file-upload').style.display='none'");
		webDriver.closeNewTab(2);
		webDriver.switchToMainWindow();
		webDriver.switchToTopMostFrame();
		tmsHomePage.switchToMainFrame();
		sleep(2);
		
	report.startStep("Verify whether package has new updated start-date");
		WebElement assignmentDate = webDriver.waitForElement("//*[@id='TblObj']/tr[3]/td[5]", ByTypes.xpath);
		//startPackageDate = pageHelper.getCurrentDateByFormat("d/MM/yyyy");
		textService.assertEquals("Incorrect package-assignment date", startPackageDateFromUI, assignmentDate.getText().trim());
		
	report.startStep("Verify whether class was assigned to package");
		List<String[]> instPackages = dbService.getUnExpiredInstitutionPackages(institutionId);
		String[] instPkg = instPackages.stream().filter(p->p[1].equals(packageName)).findAny().orElse(null);
		List<String> classAssignPackages = dbService.getClassAssignPackages(newClass,institutionId);
		textService.assertTrue("Start-date of package not updated on DB", classAssignPackages.contains(instPkg[0]));
		
	report.startStep("LogOut from TMS");
		tmsHomePage.clickOnExitTMS();
			
	}catch (Exception e) {
		testResultService.addFailTest(e.getMessage(), true, true);
		e.printStackTrace();
	}catch(AssertionError e) {
		testResultService.addFailTest(e.getMessage(), true, true);
		e.printStackTrace();
	}
	finally{
		//File createdStudent= new File("\\\\"+configuration.getGlobalProperties("logserverName")+"\\AutoLogs\\ToolsAndResources\\Shared\\ToeicStudentsToImport\\"+fileName);
		File uploadedPackage= new File(filePath); 
		uploadedPackage.delete();
	}
		
	}
	


	@Test
	@TestCaseParams(testCaseID = { "29624" })
	public void AssignPackagesTest() throws Exception {
		
	
		report.startStep("Open New UX Login Page");
		loginPage = new NewUXLoginPage(webDriver, testResultService);
		
		report.startStep("Login as Admin");
		pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(configuration.getProperty("schoolAdmin.user"),institutionId));
		sleep(1);
		tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
		homePage.closeAllNotifications();
		tmsHomePage.switchToMainFrame();
		
		report.startStep("Open Curriculum");
		tmsHomePage.clickOnCurriculum();		
		//sleep(2);
		
		report.startStep("Open Assign Packages");
		tmsHomePage.clickOnAssignPackages();
		sleep(2);

		
		try { //--open 1-st Package ----------
			chkPkg = tmsHomePage.getFirstExpandNode(false);
			
			pkgID = chkPkg.getAttribute("name").substring("imgPackageShowHide".length());
			
			chkPkg.click();
			
			sleep(2);
		}
		catch (Exception e) {
			report.reportFailure(e.getMessage());
			  tmsExit();
			return;
		}
	
		try {
			report.startStep("Get using class property");
			getClassNamAndClassIdFromAssignPackagePage();

		}
		catch (Exception e) {
			report.reportFailure(e.getMessage());
			  tmsExit();
			return;
		}
	
		
		try { //--select Package for Class -------
			report.startStep("Select Package for Class");
			
			WebElement chkEl01 = webDriver.waitForElement("//*[@id='" + chkClassID + "']/td[3]/input", ByTypes.xpath);
			
		//	boolean bState1 = chkEl01.isSelected();  -- optionally 
			chkEl01.click();
			sleep(2);
	
		//	boolean bState2 = chkEl01.isSelected();  -- optionally
			report.startStep("Save select Package result");
			tmsHomePage.clickOnSave();
			sleep(2);
		}
		catch (Exception e) {
			report.reportFailure(e.getMessage());
			  tmsExit();
			return;
		}
	
		
		try { //-- re-Open 1-st Package ---------
			report.startStep("Navigate to Assign Courses and back");

			chkPkg.click();
			sleep(2);
			
	    //--re-Open Assign Packages ------------
			tmsHomePage.clickOnAssignCourses();
			sleep(1);
			
			tmsHomePage.clickOnAssignPackages();
			sleep(1);
	    //--re-Open Assign Packages ---------
	
			chkPkg = tmsHomePage.getFirstExpandNode(false);
		 	
			chkPkg.click();
			sleep(2);
		}
		catch (Exception e) {
			report.reportFailure(e.getMessage());
			  tmsExit();
			return;
		}
		
		try { //-- unSelect Package for Class ----------
			report.startStep("unSelect Package for Class");
			
			WebElement chkEl02 = webDriver.waitForElement("//*[@id='" + chkClassID + "']/td[3]/input", ByTypes.xpath);
			
		//	boolean bState3 = chkEl02.isSelected();  -- optionally 
			chkEl02.click();
			sleep(2);
	
		//	boolean bState4 = chkEl02.isSelected();  -- optionally
			report.startStep("Save re-Select Package result");
			tmsHomePage.clickOnSave();
			sleep(2);
		}
		catch (Exception e) {
			report.reportFailure(e.getMessage());
			  tmsExit();
			return;
		}
		
		tmsExit();
	}
	
	private void getClassNamAndClassIdFromAssignPackagePage() {
		
		chkClassName = configuration.getProperty("classname.nocourses");
		chkClassName = configuration.getProperty("classname.nostudents");
		WebElement chkRes;
		try {
			chkRes = webDriver.waitForElement("//tbody//tr//td[translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')='" + chkClassName.toLowerCase() + "']/parent::*", ByTypes.xpath,false,1);
			chkClassID = chkRes.getAttribute("id");
			
			report.startStep("ClassName='" + chkClassName + "'");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	@Test
	@Category(NonAngularLearningArea.class)
	@TestCaseParams(testCaseID = { "50867","50876" })
	public void testModifyByCourseToClass() throws Exception {
		
		initModifiedTest();

 		tmsHomePage.clickOnGo();
 		sleep(1);
		
		
		for(int i=0; i<2; ++i)
		{
			String startModified = getModifiedValue();
			
			report.startStep("Select FD course");
		 		webDriver.switchToTopMostFrame();
		 		tmsHomePage.switchToMainFrame();
		 	
		 		webDriver.waitForElement("//*[@id='Course_20000']/td[3]/input", ByTypes.xpath).click();
		 	
		 		tmsHomePage.clickOnPromotionAreaMenuButton("Save");
	 		
	 		String chkModified = getModifiedValue();
	 	
			if(chkModified.equals(startModified))
	 			report.reportFailure("[Modified] state not changed for CourseToClass assigment");
			
			resetModifiedValue();
			sleep(2);
		}	
		 	
		tmsExit();
	}	

	@Test
	@Category(AngularLearningArea.class)
	@TestCaseParams(testCaseID = { "7037","29463" })
	public void testExternalATNewComponent() throws Exception {
		
		report.startStep("Init Test Data");
		String expectedInstructionText = "Select the template you want to use for your customized component. Then, complete the component details. Click \"Next\" to go on to the next stage.";
		String[] expectedtemplateTitles = {"Article","Story","Notice","E-mail","Formal Letter","External Content","Blank"};
		String[] expectedSubComponents = {"Prepare","Explore","Practice","Test"};
		String componentName = "Component" + dbService.sig(3);
		//instID = configuration.getProperty("institution.id");
		String adminId = dbService.getUserIdByUserName(configuration.getProperty("schoolAdmin.user"),institutionId);
		
		report.startStep("Login to TMS as Admin");
		loginPage = new NewUXLoginPage(webDriver, testResultService);
		pageHelper.setUserLoginToNull(adminId);
		tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
		homePage.closeAllNotifications();
		tmsHomePage.waitForPageToLoad();
			
		// Initialize curriculum authoring tool page
		CurriculumAuthoringToolPage curriculumAuthoringToolPage = new CurriculumAuthoringToolPage(webDriver, testResultService);
			
		report.startStep("Navigate to External AT");
		curriculumAuthoringToolPage.goToAuthoringTool();
		
		report.startStep("Create New Component");
		curriculumAuthoringToolPage.createNewComponent(expectedInstructionText, expectedtemplateTitles, componentName, expectedSubComponents);
		deleteATComponent = true;
		sleep(2);
		
		try {
		
			report.startStep("Verify Component Creation");
			compId = dbService.getInstCustomComponentByOwner(institutionId,adminId,componentName);
			//testResultService.assertEquals(componentName,webDriver.waitForElement("btnComp" + compId, ByTypes.id).getText());
			//sleep(3);
			curriculumAuthoringToolPage.validateComponentAddedSuccessfully(componentName);
			sleep(3);
			
			report.startStep("Delete Component");
			curriculumAuthoringToolPage.deleteComponent(componentName);
			sleep(3);
			//webDriver.checkElementNotExist("//*[@id='btnComp" + compId + "']");
			boolean isNotDisplayed = curriculumAuthoringToolPage.checkComponentIsNotDisplayed(componentName);
			if (isNotDisplayed) {
				deleteATComponent = false;
			}
		} catch (Exception e) {
			report.startStep("on error: Delete Created Component");
			String sql = "DeleteInstitutionComponents " + compId;
			dbService.runDeleteUpdateSql(sql);
		}
	}	
	
	@Test
	@Category(AngularLearningArea.class)
	@TestCaseParams(testCaseID = { "50867","50876" })
	public void testModifyByCourseToClass2() throws Exception {
		
		report.startStep("Init Modified Test");
			String classId = initModifiedTest();
	 		tmsHomePage.clickOnGo();
	 		sleep(1);
			
	 	// Initialize curriculum assign courses page
	 		CurriculumAssignCoursesPage curriculumAssignCoursesPage = new CurriculumAssignCoursesPage(webDriver, testResultService);
		
	 	report.startStep("Retrieve Start Modified Value");
			String startModified = getModifiedValue();
			webDriver.switchToTopMostFrame();
	 		tmsHomePage.switchToMainFrame();
	 		
	 	report.startStep("Validate FD is Unmarked before the Test");
			dbService.unauthorizeCourseToClass(classId, "20000;37322;37312;37319;37311;37310;37316;37317;37320;37323;7610;523125618;523127370;523127423;523127630;523127683;523132264;523132268;523132448;523132992;523133157;523133489;");
			tmsHomePage.clickOnPromotionAreaMenuButton("Reset");
			sleep(2);
			
		report.startStep("Mark and Expand FD course node");
			curriculumAssignCoursesPage.markCheckBoxOfSpecificCourse("First Discoveries");
			sleep(2);
			
		report.startStep("Click on Save");
		 	tmsHomePage.clickOnPromotionAreaMenuButton("Save");
		 	sleep(4);
	 		
		 report.startStep("Check Course is Marked");
		 	curriculumAssignCoursesPage.checkCourseIsMarked("First Discoveries");
		 	sleep(1);
		 	
		report.startStep("Retrieve Modified Value and Validate it is different");
	 		String chkModified = getModifiedValue();
			if(chkModified.equals(startModified))
	 			report.reportFailure("[Modified] state not changed for CourseToClass assigment");
			
			//resetModifiedValue();
		report.startStep("Reset Nodes (Unmark FD and Save)");
			curriculumAssignCoursesPage.markCheckBoxOfSpecificCourse("First Discoveries");
			tmsHomePage.clickOnPromotionAreaMenuButton("Save");
			sleep(3);
			
		 report.startStep("Check Course is Unmarked");
		 	curriculumAssignCoursesPage.checkCourseIsNotMarked("First Discoveries");
			
		report.startStep("Log Out");	
			tmsExit();
	}	
	
	@Test
	@Category(NonAngularLearningArea.class)
	@TestCaseParams(testCaseID = { "50868","50877" })
	public void testModifyByUnitToClass() throws Exception {
		
		initModifiedTest();
		
 		tmsHomePage.clickOnGo();
 		sleep(1);

		for(int i=0; i<2; ++i)
		{
			String startModified = getModifiedValue();

			report.startStep("Expand FD course node");
				webDriver.switchToTopMostFrame();
				tmsHomePage.switchToMainFrame();
			
				expandListItemAndClick("//*[@id='Course_20000']/td[4]/img");
				sleep(1);
			
			report.startStep("Select 1-st Unit in FD course");
				webDriver.waitForElement("//*[@id='Unit_20023']/td[5]/input", ByTypes.xpath).click();
				sleep(1);
				
		 		tmsHomePage.clickOnPromotionAreaMenuButton("Save");
		 		
	 		String chkModified = getModifiedValue();
	 	
			if(chkModified.equals(startModified))
	 			report.reportFailure("[Modified] state not changed for UnitToClass assigment");
			
			resetModifiedValue();
			sleep(2);
		}
		
		tmsExit();
	}	

	@Test
	@Category(AngularLearningArea.class)
	@TestCaseParams(testCaseID = { "50868","50877" })
	public void testModifyByUnitToClass2() throws Exception {
			
		report.startStep("Init Modified Test");
			String classId = initModifiedTest();
	 		tmsHomePage.clickOnGo();
	 		sleep(1);
	 		
 		// Initialize curriculum assign courses page
	 		CurriculumAssignCoursesPage curriculumAssignCoursesPage = new CurriculumAssignCoursesPage(webDriver, testResultService);
		
	 	report.startStep("Retrieve Start Modified Value");
			String startModified = getModifiedValue();
			webDriver.switchToTopMostFrame();
			tmsHomePage.switchToMainFrame();
			sleep(1);
			
		report.startStep("Validate FD is Unmarked before the Test");
			dbService.unauthorizeCourseToClass(classId, "20000;37322;37312;37319;37311;37310;37316;37317;37320;37323;7610;523125618;523127370;523127423;523127630;523127683;523132264;523132268;523132448;523132992;523133157;523133489;");
			tmsHomePage.clickOnPromotionAreaMenuButton("Reset");
			sleep(2);
			
		report.startStep("Mark and Expand FD course node");
			curriculumAssignCoursesPage.markCheckBoxOfSpecificCourse("First Discoveries");
			curriculumAssignCoursesPage.openOrCloseSpecificCourse("First Discoveries");
			sleep(3);
			
		report.startStep("Unmark 1-st Unit in FD course");
			curriculumAssignCoursesPage.markCheckBoxOfSpecificUnit("Introduction");
			sleep(1);
			
		report.startStep("Click on Save");
		 	tmsHomePage.clickOnPromotionAreaMenuButton("Save");
		 	sleep(3);
		 	
		report.startStep("Check Course is Marked and Unit is Unmarked");
		 	curriculumAssignCoursesPage.checkCourseIsMarked("First Discoveries");
		 	curriculumAssignCoursesPage.openOrCloseSpecificCourse("First Discoveries");
		 	curriculumAssignCoursesPage.checkUnitIsNotMarked("Introduction");
		 	curriculumAssignCoursesPage.openOrCloseSpecificCourse("First Discoveries");
		 	sleep(2);
		 	
		report.startStep("Retrieve Modified Value and Validate it is different");
	 		String chkModified = getModifiedValue();
	 		sleep(2);
			if (chkModified.equals(startModified))
	 			report.reportFailure("[Modified] state not changed for UnitToClass assigment");
			sleep(2);
			
			//resetModifiedValue();
		report.startStep("Reset Nodes (Open FD, Mark first unit, Unmark FD and Save)");
			curriculumAssignCoursesPage.openOrCloseSpecificCourse("First Discoveries");
			curriculumAssignCoursesPage.markCheckBoxOfSpecificUnit("Introduction");
			curriculumAssignCoursesPage.markCheckBoxOfSpecificCourse("First Discoveries");
			tmsHomePage.clickOnPromotionAreaMenuButton("Save");
			sleep(2);	
			
		report.startStep("Check Course is Unmarked");
		 	curriculumAssignCoursesPage.checkCourseIsNotMarked("First Discoveries");
		
		report.startStep("Log Out");
			tmsExit();
	}	
	
	@Test
	@Category(NonAngularLearningArea.class)
	@TestCaseParams(testCaseID = { "50869","50879" })
	public void testModifyByLessonToClass() throws Exception {
		
		initModifiedTest();

 		tmsHomePage.clickOnGo();
 		sleep(1);
		
		for(int i=0; i<2; ++i)
		{
			String startModified = getModifiedValue();

			report.startStep("Expand FD course node");
				webDriver.switchToTopMostFrame();
				tmsHomePage.switchToMainFrame();
			
				expandListItemAndClick("@name","20000");
				sleep(1);
			
			report.startStep("Select 1-st Unit in FD course");
				expandListItemAndClick("@id","20023");
				sleep(1);
			
			report.startStep("Select 1-st Lesson in 1-st Unit in FD course");
				webDriver.waitForElement("//*[@id='Com_20447']/td[7]/input", ByTypes.xpath).click();
				sleep(1);
				
		 	tmsHomePage.clickOnPromotionAreaMenuButton("Save");
		 		
	 		String chkModified = getModifiedValue();
	 	
			if(chkModified.equals(startModified))
	 			report.reportFailure("[Modified] state not changed for LessonToClass assigment");
			
			resetModifiedValue();
			sleep(2);
		}
		
		tmsExit();
	}	

	@Test
	@Category(AngularLearningArea.class)
	@TestCaseParams(testCaseID = { "50869","50879" })
	public void testModifyByLessonToClass2() throws Exception {
		
		report.startStep("Init Modified Test");
			String classId = initModifiedTest();
			tmsHomePage.clickOnGo();
			sleep(1);
			
		// Initialize curriculum assign courses page
	 		CurriculumAssignCoursesPage curriculumAssignCoursesPage = new CurriculumAssignCoursesPage(webDriver, testResultService);
		
		report.startStep("Retrieve Start Modified Value");
			String startModified = getModifiedValue();
			webDriver.switchToTopMostFrame();
			tmsHomePage.switchToMainFrame();
			
		report.startStep("Validate FD is Unmarked before the Test");
			dbService.unauthorizeCourseToClass(classId, "20000;37322;37312;37319;37311;37310;37316;37317;37320;37323;7610;523125618;523127370;523127423;523127630;523127683;523132264;523132268;523132448;523132992;523133157;523133489;");
			tmsHomePage.clickOnPromotionAreaMenuButton("Reset");
			sleep(2);
		
		report.startStep("Mark and Expand FD course node");
			curriculumAssignCoursesPage.markCheckBoxOfSpecificCourse("First Discoveries");
			curriculumAssignCoursesPage.openOrCloseSpecificCourse("First Discoveries");	
			
		report.startStep("Select 1-st Unit in FD course");
			curriculumAssignCoursesPage.openOrCloseSpecificUnit("Introduction");
			sleep(1);
		
		report.startStep("Select 1-st Lesson in 1-st Unit in FD course");
			curriculumAssignCoursesPage.markCheckBoxOfSpecificLesson("Alphabet - Letters: A-L");
			sleep(1);
			
		report.startStep("Click on Save");
	 		tmsHomePage.clickOnPromotionAreaMenuButton("Save");
	 		sleep(3);
	 		
	 	report.startStep("Check Course is Marked, Unit is Marked and Lesson is Unmarked");
		 	curriculumAssignCoursesPage.checkCourseIsMarked("First Discoveries");
		 	curriculumAssignCoursesPage.openOrCloseSpecificCourse("First Discoveries");
		 	curriculumAssignCoursesPage.checkUnitIsMarked("Introduction");
		 	curriculumAssignCoursesPage.openOrCloseSpecificUnit("Introduction");
		 	curriculumAssignCoursesPage.checkLessonIsNotMarked("Alphabet - Letters: A-L");
		 	curriculumAssignCoursesPage.openOrCloseSpecificCourse("First Discoveries");
	 		
		report.startStep("Retrieve Modified Value and Validate it is different");
	 		String chkModified = getModifiedValue();
			if(chkModified.equals(startModified))
	 			report.reportFailure("[Modified] state not changed for LessonToClass assigment");
			
		//resetModifiedValue();	
		report.startStep("Reset Nodes (Open FD, Open first unit, Mark First Lesson, Unmark FD and Save)");
			curriculumAssignCoursesPage.openOrCloseSpecificCourse("First Discoveries");
		 	curriculumAssignCoursesPage.openOrCloseSpecificUnit("Introduction");
			curriculumAssignCoursesPage.markCheckBoxOfSpecificLesson("Alphabet - Letters: A-L");
		 	curriculumAssignCoursesPage.markCheckBoxOfSpecificCourse("First Discoveries");
			tmsHomePage.clickOnPromotionAreaMenuButton("Save");
			sleep(2);
			
		report.startStep("Check Course is Unmarked");
		 	curriculumAssignCoursesPage.checkCourseIsNotMarked("First Discoveries");
		
		report.startStep("Log Out");
			tmsExit();
	}	

	@Test
	@Category(NonAngularLearningArea.class)
	@TestCaseParams(testCaseID = { "50873","50891" })
	public void testModifyByCourseToStudent() throws Exception {
		
		initModifiedTest();

		selectOptionByValue("SelectUser", chkUserId);
		sleep(1);
		
 		tmsHomePage.clickOnGo();
 		sleep(1);
		
		
		for(int i=0; i<2; ++i)
		{
			String startModified = getModifiedValue();
			
			report.startStep("Select FD course");
		 		webDriver.switchToTopMostFrame();
		 		tmsHomePage.switchToMainFrame();
		 	
		 		webDriver.waitForElement("//*[@id='Course_20000']/td[3]/input", ByTypes.xpath).click();
		 	
		 		tmsHomePage.clickOnPromotionAreaMenuButton("Save");
	 		
	 		String chkModified = getModifiedValue();
	 	
			if(chkModified.equals(startModified))
	 			report.reportFailure("[Modified] state not changed for CourseToClass assigment");
			
			resetModifiedValue();
			sleep(2);
		}	
		 	
		tmsExit();
	}	

	@Test
	@Category(AngularLearningArea.class)
	@TestCaseParams(testCaseID = { "50873","50891" })
	public void testModifyByCourseToStudent2() throws Exception {
		
		report.startStep("Init Modified Test");
			String classId = initModifiedTest();
			selectOptionByValue("SelectUser", chkUserId);
			sleep(1);
	 		tmsHomePage.clickOnGo();
	 		sleep(1);
		
	 	report.startStep("Retrieve Start Modified Value");
	 		String startModified = getModifiedValue();
	 		webDriver.switchToTopMostFrame();
	 		tmsHomePage.switchToMainFrame();
	 		
	 	// Initialize curriculum assign courses page
	 		CurriculumAssignCoursesPage curriculumAssignCoursesPage = new CurriculumAssignCoursesPage(webDriver, testResultService);
	 		
	 	report.startStep("Validate FD is Unmarked before the Test");
			dbService.unauthorizeCourseToClass(classId, "20000;37322;37312;37319;37311;37310;37316;37317;37320;37323;7610;523125618;523127370;523127423;523127630;523127683;523132264;523132268;523132448;523132992;523133157;523133489;");
			tmsHomePage.clickOnPromotionAreaMenuButton("Reset");
			sleep(2);
	 		
		report.startStep("Select FD course");
			curriculumAssignCoursesPage.markCheckBoxOfSpecificCourse("First Discoveries");

	 	report.startStep("Click on Save");
	 		tmsHomePage.clickOnPromotionAreaMenuButton("Save");
	 		sleep(2);
	 		
		report.startStep("Check Course is Marked");
		 	curriculumAssignCoursesPage.checkCourseIsMarked("First Discoveries");
			
		report.startStep("Retrieve Modified Value and Validate it is different");
	 		String chkModified = getModifiedValue();
			if(chkModified.equals(startModified))
	 			report.reportFailure("[Modified] state not changed for CourseToClass assigment");
			
		//resetModifiedValue();
		report.startStep("Reset (Unmark FD and Save)");
			curriculumAssignCoursesPage.markCheckBoxOfSpecificCourse("First Discoveries");
			sleep(2);
			
		report.startStep("Check Course is Unmarked");
		 	curriculumAssignCoursesPage.checkCourseIsNotMarked("First Discoveries");	
			
		report.startStep("Log Out");	
			tmsExit();
	}	
	
	private void selectOptionByValue(String comboName, String optionValue) throws Exception {

		Select select = new Select(webDriver.waitForElement(comboName, ByTypes.id));
		
		List<WebElement> options = select.getOptions();
		
		for (int i = 0; i < options.size(); i++) {
			if(options.get(i).getAttribute("value").contains(optionValue))
			{
				select.selectByIndex(i);
				return;
			}
		}
	}
	
	private String getModifiedValue() throws Exception {
		return dbService.getUserModifiedValue(chkUserId);

	}

	private void resetModifiedValue() throws Exception {
		dbService.resetUserModifiedValue(chkUserId);
	}
	
	private String initModifiedTest() throws Exception {
		
		pkgID = "";
		
		//instID = configuration.getProperty("institution.id");
		
		chkClassName = configuration.getProperty("classname.progress");
		
		chkClassID = dbService.getClassIdByName(chkClassName, institutionId);
		
		chkUserId = dbService.getLatestUserIdByInstandClass(institutionId,chkClassID);
 		
		report.startStep("Open New UX Login Page");
			loginPage = new NewUXLoginPage(webDriver, testResultService);
		
		report.startStep("Login as Admin");
			tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
			sleep(2);
		webDriver.waitForJqueryToFinish();
		report.startStep("Open Curriculum");
			tmsHomePage.switchToMainFrame();	
			tmsHomePage.clickOnCurriculum();		
			sleep(2);

		report.startStep("Open Assign Courses");
			tmsHomePage.clickOnAssignCourses();
			sleep(2);

//--igb 2018.07.17 -------- common part of code ---------------------- 			
		report.startStep("Select class -- \'" + chkClassName + "\'");
			tmsHomePage.switchToFormFrame();
			webDriver.selectElementFromComboBox("SelectClass", chkClassName);
			sleep(1);
			
//			tmsHomePage.clickOnGo();
//			sleep(1);
			return chkClassID;
	}
	
	private void logoutFromTmsAndCheckEdTabsClosed() throws Exception {
		webDriver.switchToMainWindow();
		
		tmsHomePage.switchToMainFrame();
		tmsHomePage.clickOnExit();
		sleep(4);
		webDriver.switchToTopMostFrame();
		testResultService.assertEquals(false, loginPage.isSubmitButtonEnabled(), "Checking that Login Page displayed");
		webDriver.checkNumberOfTabsOpened(1);
	}
	
	private void expandListItemAndClick(String nodeId) throws Exception {
		webDriver.waitForElement(nodeId, ByTypes.xpath).click();
		sleep(2);  // Increased sleep due to elements loading later than expected.
	}
	
	private void expandListItemAndClick(String type, String nodeId) throws Exception {
		WebElement element = webDriver.waitForElement("//*/img[contains("+ type +"," + nodeId + ")]", ByTypes.xpath, false, 1);
		element.click();
		sleep(2);  // Increased sleep due to elements loading later than expected.
	}
	
	@After
	public void tearDown() throws Exception {
		studentId = dbService.getUserIdByUserName(configuration.getProperty("schoolAdmin.user"),configuration.getProperty("institution.id"));
		//super.tearDown();
		if (resetCourseOrder) {
			String className = configuration.getProperty("classname.assessment");
			String classId = dbService.getClassIdByName(className, configuration.getProperty("institution.id"));
			dbService.setClassCourseSequence(classId, courseOriginalOrder);
		}
		
		if (deleteATComponent) {
			dbService.deleteCustomComponentById(compId);
		}
		super.tearDown();
	}
	
	@SuppressWarnings("finally")
	public void tmsExit() throws Exception {
		
		if((institutionId != "") && (pkgID != "") && (chkClassID) != "") {
			try {
				String sp = "exec dbo.SetClassAssignedPackages " + institutionId + ", '" + chkClassID.substring("classId".length()) + "," + pkgID + ",0;'";

				dbService.runStorePrecedure(sp);				
			}
			catch (Exception e) {
//				String chkMsg = e.getMessage();
			}
			finally {
				report.startStep("Exit TMS");
				tmsHomePage.clickOnExit();
				sleep(2);
				return;
			}
		}
		
		report.startStep("Exit TMS");
		tmsHomePage.clickOnExit();
		sleep(2);
	}
}
