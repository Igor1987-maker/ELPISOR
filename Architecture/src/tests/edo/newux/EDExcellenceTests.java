package tests.edo.newux;

import Enums.ByTypes;
import Enums.CourseCodes;
import Enums.TaskTypes;
import Interfaces.TestCaseParams;
import drivers.FirefoxWebDriver;
import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import pageObjects.FirefoxLoginPage;
import pageObjects.edo.*;
import pageObjects.tms.*;
import pageObjects.toeic.ToeicLearningAreaAndProgressPage;
import pageObjects.toeic.toeicQuestionPage;
import pageObjects.toeic.toeicResultsPage;
import pageObjects.toeic.toeicStartPage;
import testCategories.edoNewUX.EDExcellenceArea;
import testCategories.inProgressTests;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class EDExcellenceTests extends BasicNewUxTest{

	private boolean deleteImportedUser;
	TmsHomePage tmsHomePage;
	NewUXLoginPage loginPage;
	NewUxAssessmentsPage testsPage;
	ToeicLearningAreaAndProgressPage makeProgressInCoursePage;
	NewUxLearningArea2 learningArea2;
	//String[] forms = {"4ATB3","3XTB1"};
	//List<String> forms =  new ArrayList<String>(List.of("3XTB1","4ATB3"));
	List<String> forms = Arrays.asList("3XTB1", "4ATB3");
	FirefoxLoginPage firefoxPage;
	String classNameExcellence = "";//configuration.getEDExcellenceClassName();
	//Map<String,String> map = Map.of("","","","");

	@Before
	public void setup() throws Exception {
		institutionName = institutionsName[16];
		super.setup();
		classNameExcellence = configuration.getEDExcellenceClassName();
	}

	@Test
	@Category(EDExcellenceTests.class)
	@TestCaseParams(testCaseID = { "94595"})
	public void testUpdatingLicensesOnStudentsLogin() throws Exception {

		try {
			report.startStep("Store amount of package license before user login");
				List<String[]> institutionPackagesBefore = dbService.getInstitutionPackages(institutionId);

			report.startStep("Get institution Remaining License for packages assigned to class");
				List<String[]> classPackages = dbService.getClassAssignPackagesNew(classNameExcellence, institutionId);
				/*List<String[]> remainingLicensesBeforeAddingUser = new ArrayList<>();
				if (classPackages!=null) {
					remainingLicensesBeforeAddingUser = dbService.getRemainingLicensesOfCertainPackage(classPackages, institutionId);
				}else {
					testResultService.addFailTest("ClassHasNoPackages",false,false);
				}*/

			report.startStep("Create user and login");
				String studentId = pageHelper.createUSerUsingSP(institutionId, classNameExcellence);
				List<String[]> user = dbService.getUserNameAndPasswordByUserId(studentId);
				userName = user.get(0)[0];
				String password = user.get(0)[1];
				NewUXLoginPage loginPage = new NewUXLoginPage(webDriver, testResultService);
				loginPage.loginAsStudent(userName, password);
				loginPage.waitLoginAfterRestartAppPool();
				pageHelper.skipOptin();
				homePage.closeAllNotifications();
				pageHelper.skipOnBoardingHP();
				homePage.waitHomePageloadedFully();

			report.startStep("Verify on DB whether student added to PackageProgress table");
				List<String[]> packageProgress = dbService.getUserPackagesProgress(studentId);
				String date = pageHelper.getCurrentDateByFormat("yyyy-MM-dd");
				for (String[] pkg:packageProgress) {
					String dayAndTimeOfStartedLicense = pkg[1];
					testResultService.assertEquals(true, dayAndTimeOfStartedLicense.startsWith(date), "Wrong package progress date");
				}

			report.startStep("Verify that amount of packageProgress match amount of packages assigned to class");
				testResultService.assertEquals(packageProgress.size(),classPackages.size(),"Amount of packageProgress should match the packages assigned to class");
				//String[] packageProgress = dbService.getStudentPackageProgress(studentId);

			report.startStep("Verify that licenses was burned after login");
			/*if (remainingLicensesBeforeAddingUser!=null) {
				//pageHelper.checkRemainingLicenseBurnForallValidPackages(getValidInstitutionPackages,institutionId,false);
				report.startStep("Get institution Remaining License after adding new student");
				List<String[]> remainingLicensesAfterAddingUser = dbService.getRemainingLicensesOfCertainPackage(classPackages, institutionId);
				pageHelper.validateLicensesReducesAfterNewStudentAdded(remainingLicensesBeforeAddingUser, remainingLicensesAfterAddingUser);
			}*/



			/*report.startStep("Verify the start date of package progress and the institution package Progress id are identical");
				for (int i=0;i<=packageProgress.size();i++){
					testResultService.assertEquals(true,
							packageProgress.get(i)[1].startsWith(date) && ValidInstitutionPackages.get(i)[0]==packageProgress.get(i)[0]
							, "Wrong package progress date");
				}*/


			report.startStep("Verify used license increase on DB after user had logged in");
				String[] packageDetails = verifyLicenses(institutionPackagesBefore);

			report.startStep("Logout from ED");
				homePage.logOutOfED();
				loginPage.waitForPageToLoad();

			report.startStep("Login as TmsDomain");
				report.startStep("Close ED and Open TMS");
				tmsHomePage = pageHelper.closeEDAndOpenTMS();
				report.startStep("Login as tmsdomain");
				tmsHomePage.loginAsTmsdomain();

			report.startStep("Go to Institutions/Institution Packages");
				RegistrationInstitutionsPage registrationInstitutionsPage = new RegistrationInstitutionsPage(webDriver, testResultService);
				registrationInstitutionsPage.goToInstitutions();
				registrationInstitutionsPage.clickOnPackages();

			report.startStep("Select Institution and click Go");
				registrationInstitutionsPage.selectLocalInstitution(institutionName);

			report.startStep("Verify used license increased");
				registrationInstitutionsPage.validateUsedLicenseOfPackage(packageDetails);

			/*report.startStep("Go to Curriculum/Assign Packages");
				tmsHomePage = new TmsHomePage(webDriver,testResultService);
				tmsHomePage.clickOnCurriculum();
				tmsHomePage.clickOnAssignPackages();

			report.startStep("Click on Package and verify that User Licenses updated");*/

			report.startStep("Close browser and open ED");
				webDriver.quitBrowser();
				webDriver.init();
				Thread.sleep(2000);
				webDriver.maximize();
				webDriver.openUrl(pageHelper.CILink);

			report.startStep("Login as student again");
				loginPage.loginAsStudent(userName, password);
				homePage.closeAllNotifications();
				homePage.waitHomePageloadedFully();

			report.startStep("Navigate to task and make some progress");
				learningArea2 = homePage.navigateToTask(courseCodes[1], 1, 1, 2, 2);
				learningArea2.SelectRadioBtn("question-1_answer-1");
				learningArea2.clickOnNextButton();
				sleep(1);
				NewUxDragAndDropSection2 dragAndDrop2 = new NewUxDragAndDropSection2(webDriver, testResultService);
				dragAndDrop2.dragAndDropFirstAnswer(TaskTypes.MTTP);
				sleep(1);
				learningArea2.clickOnNextButton();

			report.startStep("Verify start day of license not changed");
				sleep(1);

				packageProgress = dbService.getUserPackagesProgress(studentId);

				for (int i=0;i<packageProgress.size();i++){
					testResultService.assertEquals(true, packageProgress.get(i)[1].startsWith(date), "Wrong package progress date");
				}

			report.startStep("Logout from ED");
				learningArea2.logOutOfED();
				loginPage.waitForPageToLoad();

		}catch (Exception | AssertionError e){
			testResultService.addFailTest(e.getMessage(), true,true);
		}finally {
			report.startStep("Cleaning test data. Delete student and class");
				dbService.deleteStudentByName(institutionId, userName);

		}

	}


	@Test
	@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "93374"})
	public void testLinkToTOEICDigitalScoreReport() throws Exception {

		report.startStep("Retrieve student with Certificate test-results");
			studentId = dbService.getUserIdFromETSCertificateDB();
			List<String[]> student = dbService.getUserNameAndPasswordByUserId(studentId);
			institutionId = student.get(0)[5];
			userName = student.get(0)[0];
			password = student.get(0)[1];

		report.startStep("Get institution where student belongs to");
			institutionName = dbService.getInstituteNameById(institutionId);

		report.startStep("Restart browser with new URL");
			String url = webDriver.getUrl().split(".com")[0] + ".com/" + institutionName;
			webDriver.openUrl(url);

		report.startStep("Login with student");
			loginPage = new NewUXLoginPage(webDriver,testResultService);
			homePage = loginPage.loginAsStudent(userName, password);//L1,12345
			pageHelper.skipOptin();
			homePage.closeAllNotifications();
			homePage.waitForPageToLoad();
			sleep(2);
			testResultService.assertTrue("", homePage.getUserDataText().contains("Hello"));
			String actualCourse = homePage.getCurrentCourseName();

		report.startStep("Open assessment");
			testsPage = homePage.openAssessmentsPage(false);
			sleep(2);

		report.startStep("Open section of test results");
			Integer numberOfSection = testsPage.getNumberOfWantedSection("Test Results");
			testsPage.clickOnArrowToOpenSection(numberOfSection.toString());

		report.startStep("Click on View Certificate Result");
			DigitalCertificatePage certificatePage = testsPage.clickOnViewResultsOfCertificationTest();

		report.startStep("Verify Certificate open with correct user name");
			certificatePage.verifySeveralTitlesOfScoreReportPage();
			String userNameFromUI = certificatePage.getStudentName();
			Assert.assertNotNull("UserName wont display",userNameFromUI);
			//testResultService.assertEquals("Ivaan Angulo",userName,"Incorrect username",true);

		report.startStep("Open and verify Validation page");
			certificatePage.openValidationPage();
			sleep(1);
			certificatePage.verifySeveralValidationPageTitles();

		report.startStep("Close certificate and switch back to ED");
			webDriver.closeTab(1);
			webDriver.switchToMainWindow();

		report.startStep("Close assessment and Logout from ED");
			testsPage.close();
			sleep(2);
			homePage.clickOnLogOut();

	}


	

	@Test
	@Category(EDExcellenceArea.class)
	@TestCaseParams(testCaseID = { "90795"})
	public void testVerifyLongCourseName_TOEIC_Bridge() throws Exception{

		report.startStep("Get user and login to EDExcellence institution");
			homePage = getUserAndLogin(classNameExcellence, institutionId, true, true);
			pageHelper.skipOnBoardingHP();
			homePage.waitHomePageloadedFully();
			
			//String[] arr = pageHelper.getUserNamePassworIddByInstitutionIdAndClassName(institutionId, internalClassName);
		report.startStep("Navigate to TOEIC Bridge course and extract font-size of course name");
			homePage.navigateToRequiredCourseOnHomePage(coursesNamesEDExcellence[3]);
			WebElement courseName = webDriver.waitForElement("//*[@id='mainContent']/div/div/section[1]/div/div/div/div[1]/div[contains(@class,'active')]/div/h3",
					ByTypes.xpath, false, webDriver.getTimeout());
			String font1 = courseName.getCssValue("font-size").replace("px", "").trim();//div[contains(@class,'carouselCaptions')]//h3
		
		report.startStep("minimize the window and get font-size once again");
			webDriver.getWebDriver().manage().window().setSize(new Dimension(900, 600));
			sleep(2);
			String font2 = courseName.getCssValue("font-size").replace("px", "").trim();
						
		report.startStep("Check that font-size reduced and flexible after minimizing window");
			textService.assertTrue("Font-size of course name didn't changed after minimizing window", 
																			Double.parseDouble(font1)>Double.parseDouble(font2));
			
		report.startStep("Maximize browser window and check course name in courseList");		
			webDriver.getWebDriver().manage().window().maximize();
			sleep(1);
			homePage.clickOnCourseList();
			List<WebElement> coursesFromCourseList = homePage.getCourseListCourseELements();
			List<String> courseNamesFromList = coursesFromCourseList.stream().map(c->c.getText().toString()).collect(Collectors.toList());
			boolean courseDisplayedCorrectly = courseNamesFromList.contains(coursesNamesEDExcellence[3]);
			textService.assertTrue("Incorrect courseName at course-list dropdown, or it's not fully displayed",
																	courseNamesFromList.contains(coursesNamesEDExcellence[3]));
					
		report.startStep("Go to myProgress page and verify course name displayed correctly"); 	
			NewUxMyProgressPage chkProgressPage = homePage.clickOnMyProgress(); 
		    chkProgressPage.waitForPageToLoad();
		    String myProgressCourseName = chkProgressPage.getCourseName();
		    //textService.assertEquals("Incorrect corse name in myProgress page", coursesNamesTOEIC[3], myProgressCourseName);
		    textService.assertTrue("Incorrect course name in myProgress page", myProgressCourseName.contains(coursesNamesEDExcellence[3]));
		    
		report.startStep("Verify course name fully displayed with minimized window");    
		    webDriver.getWebDriver().manage().window().setSize(new Dimension(900, 600));
		    sleep(1);
		    myProgressCourseName = chkProgressPage.getCourseNameWithMinimizedWindow();
		    //textService.assertEquals("Incorrect corse name in myProgress page with minimized window", coursesNamesTOEIC[3], myProgressCourseName);
		    textService.assertTrue("Incorrect corse name in myProgress page", myProgressCourseName.contains(coursesNamesEDExcellence[3]));
	
		report.startStep("LogOut from ED");    
			chkProgressPage.logOutOfED();
	
	}
	
	
	@Test
	@Category(EDExcellenceArea.class)
	@TestCaseParams(testCaseID = { "90150"})
	public void testAdd_TOEICcourses_To_ED_Excellence() throws Exception{
		
		report.startStep("initilaze data");
			//institutionName = institutionsName[16];
			//pageHelper.initializeData();
			//pageHelper.closeBrowserAndOpenInCognito(false);
		
		report.startStep("Create student and class");
			String baseUrl = pageHelper.CILink.split(".com")[0] + ".com/";
			userName = "stud" + dbService.sig(5);
			String userFN = userName + "-FN";
			String userLN = userName + "-LN";
			String email = userName + new Random().nextInt(1000) + "-@edusof-t.co.il";
			String newClass = "Class-" + dbService.sig(4);
			String regUserUrl = "";
		report.startStep("Login to ED via RegUser");
		  try {
			regUserUrl = baseUrl + "RegUserAndLogin.aspx?Action=I&UserName=" + userName + "" + "&Inst="
					+ institutionName + "&FirstName=" + userFN + "&LastName=" + userLN + "" + "&Password=12345&Email="
					+ email + "&Class=" + newClass + "" + "&Language=English&Link=" + pageHelper.linkED
					+ "&UseNameMapping=N&CreateClass=Y&UserType=S";
			
			webDriver.openUrl(regUserUrl);
			pageHelper.skipOptin();
			homePage.closeAllNotifications();
			pageHelper.skipOnBoardingHP();
			homePage.waitHomePageloadedFully();
			studentId = dbService.getUserIdByUserName(userName, institutionId);

		report.startStep("Insert new user to automation table");
		 if (studentId != null){
			  dbService.insertUserToAutomationTable(institutionId, studentId, userName, classNameExcellence, baseUrl);
			  classId = dbService.getClassIdByClassName(newClass,institutionId);
			  dbService.insertClassToAutomationTable(classId, newClass, null, institutionId, studentId, baseUrl);
		  }
			
		report.startStep("Validate all TOEIC courses available in ED");
			//int count = 0;
			String actualCourse = homePage.getCurrentCourseName();
			String currCourse = actualCourse;
			List<String> allCoursesDisplayed = new ArrayList<>();
			int i=0;
			do{
				if(currCourse.equals(coursesNamesEDExcellence[0])
						||currCourse.equals(coursesNamesEDExcellence[1])
						||currCourse.equals(coursesNamesEDExcellence[2])
						||currCourse.contains(coursesNamesEDExcellence[3])
				)
					{
				  		allCoursesDisplayed.add(currCourse);
					}

				homePage.carouselNavigateNext();
				homePage.waitHomePageloadedFully();
				i++;
				sleep(1);
				currCourse = homePage.getCurrentCourseName();

			}while(!actualCourse.equals(currCourse) && i<25);

			try {
				textService.assertEquals("Wrong course name or course not assigned to EDExcellence", coursesNamesEDExcellence.length, allCoursesDisplayed.size());
			}catch (Exception e) {
				e.printStackTrace();
				testResultService.addFailTest(testName.getMethodName(),true,true);
			}
			
		report.startStep("LogOut from Ed and enter to TMS");
			homePage.clickOnLogOut();
			pageHelper.restartBrowserInNewURL(institutionName, false);
			loginPage = new NewUXLoginPage(webDriver, testResultService);
			pageHelper.setUserLoginToNull(dbService.getUserIdByUserName("admin",institutionId));
			tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
			homePage.closeAllNotifications();
			homePage.waitUntilLoadingMessageIsOver();
		
		report.startStep("Go to CurriculumAssignCoursesPage");
			tmsHomePage.switchToMainFrame();
			tmsHomePage.clickOnCurriculum();
			tmsHomePage.clickOnAssignCourses();
			sleep(1);
			tmsHomePage.selectClass(newClass, true, true);
			CurriculumAssignCoursesPage curriculumAssignCoursesPage = new CurriculumAssignCoursesPage(webDriver, testResultService);
			curriculumAssignCoursesPage.openOrCloseSpecificCourse(coursesNamesEDExcellence[0]);
			sleep(1);
			
		report.startStep("Unassign one of the unit at TOEIC " +coursesNamesEDExcellence[0]);
			curriculumAssignCoursesPage.markCheckBoxOfSpecificUnit("Personnel");
			tmsHomePage.clickOnSave();
			sleep(2);
		
		report.startStep("Exit of TMS and login to ED");
			//tmsHomePage.clickOnExitTMS();
			tmsHomePage.clickOnExit();
			sleep(1);
			webDriver.openUrl(regUserUrl);
		//	pageHelper.skipOptin();
			homePage.closeAllNotifications();
						
		report.startStep("Navigate to course "+coursesNamesEDExcellence[0]);	
			homePage.navigateToRequiredCourseOnHomePage(coursesNamesEDExcellence[0]);
			
		report.startStep("Find locked unit and verify locked-label");		
			WebElement unitLockedSign = homePage.getUnitLockedElement(1,true);
			WebElement unitLocedSignParent = webDriver.waitForElement("home__detailsTitle-1", ByTypes.id);
			webDriver.scrollToElement(unitLocedSignParent, 0);
			String unitLockedLabel = unitLockedSign.getAttribute("title");
			testResultService.assertEquals("Locked", unitLockedLabel);
		
		report.startStep("Logout from ED");		
			homePage.logOutOfED();
		}
		catch(Exception e) {
			e.printStackTrace();
			testResultService.addFailTest(testName.getMethodName(),true,true);
		}finally{
			
		//report.startStep("Cleaning test data. Delete student and class");
			//dbService.deleteStudentByName(institutionId, userName);
			//dbService.deleteClassByName(institutionId, newClass);
		}
		
	}
	
	
	@Test
	@Category(EDExcellenceArea.class)
	@TestCaseParams(testCaseID = { "89340,89905"})
	public void testToeicBridgeAsPLT_byImportNewStudent() throws Exception {


	report.startStep("Generate new student and save him in txt file ");	
		String i = dbService.sig(4);
		userName = "St"+i;
		String fileName = "NewStudent"+i+".txt";
		String filePath = "\\\\"+configuration.getGlobalProperties("logserverName")+"\\AutoLogs\\ToolsAndResources\\Shared\\ToeicStudentsToImport\\"+fileName;
		//String filePath = "smb://"+configuration.getGlobalProperties("logserverName")+"//AutoLogs//ToolsAndResources//Shared//ToeicStudentsToImport";
		generateNewTxtFileForImportSudents(userName,filePath);

	report.startStep("Login as Admin");
		//pageHelper.restartBrowserInNewURL(institutionsName[16], true); 
		loginPage = new NewUXLoginPage(webDriver, testResultService);
		//institutionId = dbService.getInstituteIdByName(institutionsName[16]);
		pageHelper.setUserLoginToNull(dbService.getUserIdByUserName("admin",institutionId));
		tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
		homePage.closeAllNotifications();
		//sleep(5);
		homePage.waitUntilLoadingMessageIsOver();
		
	report.startStep("Click on Registration");
		tmsHomePage.switchToMainFrame();
		tmsHomePage.clickOnRegistration();
		sleep(1);
		
	report.startStep("Click on Students");
		tmsHomePage.clickOnStudents();
		sleep(5);
		
	report.startStep("Click On Import");
		tmsHomePage.clickOnPromotionAreaMenuButton("Import");
		sleep(3);
		webDriver.switchToPopup();
		
	report.startStep("Attach File and close popup");
	try {
		//tmsHomePage.importFileForRegistration("\\\\"+configuration.getGlobalProperties("logserverName")+"\\Shared\\ToeicStudentsToImport\\stud1.txt");
		//tmsHomePage.importFileForRegistration("\\\\"+configuration.getGlobalProperties("logserverName")+"\\AutoLogs\\ToolsAndResources\\Shared\\ToeicStudentsToImport\\"+fileName);
		tmsHomePage.importFileForRegistration(filePath);
		deleteImportedUser = true;
		
	report.startStep("Verify that student assigned to Toeic bridge test");
		String authorizationKey = dbService.verifyStudentAssignedToToeicBridge(userName);
		textService.assertNotNull("Student wasn't assigned to the ToeicBridgePLT test", authorizationKey);
		
	report.startStep("Verify that student was assigned to correct testFormID");
		String studId = dbService.getUserIdByUserName(userName, institutionId);// dbService.getInstituteIdByName(institutionsName[16]));
		String formID = dbService.getTestFormIdFromToeicDB(studId);
		textService.assertEquals("Wrong formID", "4OTB5", formID);
		
	report.startStep("Exit from");	
		//tmsHomePage.clickOnExitTMS();
		tmsHomePage.clickOnExit();
		
/*	report.startStep("Delete txt file");	
		File createdStudent= new File("\\\\"+configuration.getGlobalProperties("logserverName")+"\\AutoLogs\\ToolsAndResources\\Shared\\ToeicStudentsToImport\\"+fileName); 
		createdStudent.delete();
		boolean fileStillExist = createdStudent.exists();
		textService.assertEquals("File not deleted", false, fileStillExist);*/
		
	report.startStep("Login to ED");
	    loginPage.loginAsStudent(userName, "12345");
	    loginPage.waitLoginAfterRestartAppPool();
	    pageHelper.skipOptin();
	    homePage.closeAllNotifications();
	    pageHelper.skipOnBoardingHP();
		homePage.waitHomePageloadedFully();
	
	report.startStep("Go to Assesment and verify TOEIC bridge available");
		testsPage = homePage.openAssessmentsPage(false);
		sleep(4);
		boolean isTestAvailable = testsPage.checkIfTestIsDisplayedInAvailableTests("TOEIC Bridge");//TOEIC Bridge™ test
		textService.assertEquals("TOEIC bridge unavailable at assessments", true, isTestAvailable);


		report.startStep("Open test");
			//String clickedTestName = testsPage.clickStartTestByName(testTypes[5]);
			testsPage.clickStartTest(1, 1);
			sleep(4);
			String mainWindow = webDriver.switchToNewWindow();
			TOEICTestPage toeicTestPage = new TOEICTestPage(webDriver,testResultService);
			toeicTestPage.waitTillToeicTestDownloadedAncClickOnStart();

		report.startStep("Verify whether test running");
			String parentWindowHandle = webDriver.switchToPopup();
			toeicTestPage.completeTOEICTest();
			//toeicTestPage.fulfillTOEICTest();
			//toeicTestPage.verifyTestIsRunning();

		report.startStep("Close test window and switch to main window");
			webDriver.getWebDriver().switchTo().window(parentWindowHandle);
			webDriver.getWebDriver().close();
			webDriver.switchToMainWindow(mainWindow);

		report.startStep("Log out from ED");
			homePage.logOutOfED();
		
	}catch (Exception e) {
		e.printStackTrace();
		testResultService.addFailTest(e.getMessage(), true, true);
	}catch (AssertionError e) {
		System.out.println(e.getMessage()); 
		testResultService.addFailTest("TOEIC bridge unavailable at assessments/n "+e.getMessage());
	}finally {
		
		File createdStudent= new File(filePath); 
		createdStudent.delete();
		boolean fileStillExist = createdStudent.exists();
		textService.assertEquals("File not deleted", false, fileStillExist);
		
		report.startStep("Cleaning test data. Delete student and class");		
			dbService.deleteStudentByName(institutionId, userName);
		}
	}
	
	
	
	@Test
	@Category(EDExcellenceArea.class)
	@TestCaseParams(testCaseID = {"89906"})
	public void testAssignOldFormTOEIC_BridgeByTMS() throws Exception {
		
		report.startStep("Restart browser with new institution and login to TMS");
			institutionName = institutionsName[1];
			pageHelper.restartBrowserInNewURL(institutionName, true);
			
		report.startStep("Login as admin");
			loginPage = new NewUXLoginPage(webDriver, testResultService);
			pageHelper.setUserLoginToNull(dbService.getUserIdByUserName("admin",institutionId));
			tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
			homePage.closeAllNotifications();
			homePage.waitUntilLoadingMessageIsOver();
		
		report.startStep("Create student and get he's credentials");
			String className = "DoNotDeleteThisClass"; //configuration.getProperty("classname.CourseTest");
			String UserID = pageHelper.createUSerUsingSP(institutionId, className);
			List<String[]> user = dbService.getUserNameAndPasswordByUserId(UserID);
			userName = user.get(0)[0];
			String password = user.get(0)[1];
			String userFirsName = user.get(0)[3];
		
		
		report.startStep("Go to Assessments -> Test Assignment");
			TmsAssessmentsTestsAssignmentPage tmsAssessmentsTestsAssignmentPage = new TmsAssessmentsTestsAssignmentPage(webDriver, testResultService);
			tmsAssessmentsTestsAssignmentPage.goToAssessmentTestsAssignment();
						
		report.startStep("Assign Toeic bridge");
			configureAndSelectToeicTest("TT","Toeic Bridge","New Event");
			webDriver.switchToPopup();
			
		report.startStep("Create event for Toeic bridge");	
			try {
				createEventForToeicBridge();
				sleep(1);
				webDriver.switchToMainWindow();
				webDriver.switchToTopMostFrame();
				tmsHomePage.switchToMainFrame();
			
			report.startStep("Select student and add him to new event");
				tmsHomePage.clickOnAddStudets();
				webDriver.switchToPopup();
				sleep(3);
				selectStudentForTOEICtest(className);
				sleep(1);
				webDriver.closeAlertByAccept();
				webDriver.closeNewTab(1);
				webDriver.switchToMainWindow();
				webDriver.switchToTopMostFrame();
				tmsHomePage.switchToMainFrame();
			
			report.startStep("Click on save button");
				tmsHomePage.waitForSaveButtonAndClick();
				sleep(5);
			
			report.startStep("Verify that student retrieved authorizationKey");
				WebElement authorizationKey = tmsHomePage.waitForAuthorizationKeyToBeDisplayed();
				String authKey = authorizationKey.getText();
				textService.assertNotNull("Authorization key not created", authKey);
			
			report.startStep("Verify that student was assigned to correct testFormID");
				String formID = dbService.getTestFormIdFromToeicDB(UserID);//4ATB3
				Integer index = forms.indexOf(formID);
				if(index.toString() == null) {
					testResultService.addFailTest("Wrong form id was assigned");
				}
				//textService.assertEquals("Wrong formID", "3XTB1", formID);
				
			report.startStep("LogOut from TMS");	
				tmsHomePage.clickOnExit();
			}catch(Exception e) {
				e.printStackTrace();
				testResultService.addFailTest(e.getMessage(), true, true);
			}finally {
			report.startStep("Cleaning test data. Delete student and class");		
				dbService.deleteStudentByName(institutionId, userName);
				dbService.deleteLastToeicTestEvent();
			
			}
	}
	
	
	@Test
	@Category(EDExcellenceArea.class)
	@TestCaseParams(testCaseID = { "90161"})
	public void testSessionTimeOutFromLearningArea_ED_Excellence() throws Exception {
		
	report.startStep("Create student and class variables");
		//String [] restartPool = {"StopAppPoolRemotely.bat","StartAppPoolRemotely.bat"}; 
		int unitNumber = 1;
		String expectedTestResult = "20";
		String courseName = coursesNamesEDExcellence[0];
		int numOfSections = 2;
		
	try {	
		
		/*
	report.startStep("Refresh page to play it safe");
		sleep(2);
		webDriver.refresh();
		NewUXLoginPage loginPage = new NewUXLoginPage(webDriver, testResultService);
		loginPage.waitForPageToLoad();
		
	report.startStep("Set expiration time to service appjson settings (minutes)");
		pageHelper.setSessionExpirationTime("2");
						
	report.startStep("restart the application pool");
		pageHelper.restartWebServiceApplicationPool();
		//for(String action: restartPool) {
		//	pageHelper.runConsoleProgram(PageHelperService.batFilesRestartPoolPath + action);
		//	sleep(4);
		//}	
		sleep(2);
		webDriver.refresh();
		loginPage.waitForPageToLoad();
		*/
					
	report.startStep("Create user and Login to ED");
	
		studentId = pageHelper.createUSerUsingOptIn(institutionId, classNameExcellence);
		userName = dbService.getUserNameById(studentId, institutionId);
		loginPage = new NewUXLoginPage(webDriver, testResultService);
		homePage = loginPage.loginAsStudent(userName, "12345");
		sleep(1);
		loginPage.waitLoginAfterRestartAppPool();
	  	pageHelper.skipOptin();
		homePage.waitNotificationsAfterRestartPool();
		homePage.closeAllNotifications();
		pageHelper.skipOnBoardingHP();
		homePage.waitHomePageloadedFully();
		studentId = dbService.getUserIdByUserName(userName, institutionId);
	
	report.startStep("Navigate to some task of TOEIC course in LA");
		//homePage.navigateToTOEICTask(coursesNamesEDExcellence[0], 1, 3, 2, 2);
		homePage.navigateToRequiredCourseOnHomePage(courseName);
		homePage.clickOnUnitLessons(unitNumber);
		homePage.clickOnLesson(unitNumber-1, 4);
		NewUxLearningArea2 learningArea2 = new NewUxLearningArea2(webDriver, testResultService);
		learningArea2.waitUntilLearningAreaLoaded();
		learningArea2.clickOnNextButton();
		learningArea2.clickOnStep(2);
		learningArea2.clickOnTaskByNumber(2);
		
	report.startStep("Change token in local storage to get session time out");
		String token = pageHelper.runJavaScriptCommand("return localStorage.getItem(\"EDAPPToken\")");
		pageHelper.runJavaScriptCommand("localStorage.setItem('EDAPPToken','"+token+"11')");

	report.startStep("Wait till session get expired");
		homePage.waitTillInActivityPageOrTimeExpirationApears(70);
		
	//report.startStep("Wait until Session time out is displayed after 140 seconds");
	//	homePage.waitTillInActivityPageOrTimeExpirationApears(70);
		
	report.startStep("Verify user get the inactivity page and click OK");
		loginPage = new NewUXLoginPage(webDriver, testResultService);
		String message = webDriver.waitForElement("h1", ByTypes.tagName).getText();
		testResultService.assertEquals("This session is no longer active on this device.", message, "Wrong session timeOut message");
		loginPage.clickOK_onSessionIsNoLongerActive();
		sleep(2);
		testResultService.assertEquals(false, loginPage.isSubmitButtonEnabled(),"Checking that ED Login Page displayed");
		 
	report.startStep("Verify the user logged out in DB");
		boolean status = studentService.getUserLoginStatus(studentId);
		testResultService.assertEquals(true, status,"Checking that user logged out from DB as well");
		
	report.startStep("LogIn to ED");
		homePage = loginPage.loginAsStudent(userName, "12345");
		sleep(1);
		loginPage.verifyAndConfirmAlertMessage();
		homePage.closeAllNotifications();
		homePage.waitHomePageloadedFully();
		//homePage.navigateToRequiredCourseOnHomePage(coursesNamesEDExcellence[3]);
		
	report.startStep("Accomplish unit test");
		makeProgressInCoursePage = new ToeicLearningAreaAndProgressPage(webDriver, testResultService);
		ArrayList<String> UnitTestAnswers2 = makeProgressInCoursePage.accomplishUnitTest(unitNumber,coursesNamesEDExcellence[3],expectedTestResult,numOfSections,"files/TOEIC/UnitTestTOEICBridge.csv");
	
	report.startStep("Initialize my progress page");
		NewUxMyProgressPage myProgress = new NewUxMyProgressPage(webDriver, testResultService);
	
	report.startStep("Enter " + coursesNamesEDExcellence[3] + " My Progress Section");	
		homePage.navigateToRequiredCourseOnHomePage(coursesNamesEDExcellence[3]);
		//sleep(2);
		myProgress = homePage.clickOnMyProgress();
		myProgress.waitForPageToLoad();
	
	report.startStep("Verify Unit Test Score is Displayed in My Progress Page");
		myProgress.clickToOpenUnitLessonsProgress(1);
		testResultService.assertEquals(expectedTestResult.toString(), myProgress.checkUnitTestScoreOfSpecificUnit(unitNumber),"Scores aren't identical");
		sleep(2);
		myProgress.clickViewResultsInMyProgressPageForSpecificUnit();
		
	report.startStep("Click 'View Results' Button");	
		toeicResultsPage toeicResultspage = new toeicResultsPage(webDriver,testResultService);
		toeicResultspage.waitForPageToLoad();
		toeicResultspage.verifyCorrectAnswerSignsOnTabs(UnitTestAnswers2.size());
		
	report.startStep("LogOut from ED");
		myProgress.logOutOfED();
	 
	  }catch (Exception e) {
		e.printStackTrace();
		testResultService.addFailTest(e.getMessage(), true, true);
	  }catch(AssertionError e){
		  e.printStackTrace();
		  testResultService.addFailTest(e.getMessage(), true, true);
	  }
		  finally {
	  
			//report.startStep("Return normall value (120 min) to session expiration time and restart the app-pool");
			//	pageHelper.setSessionExpirationTime("120");
			//	pageHelper.restartWebServiceApplicationPool();
				
			report.startStep("Cleaning test data");
				dbService.deleteStudentByName(institutionId, userName);
				//dbService.deleteClassByName(institutionId, "");
		}
	}
	
	@Test
	@Category(EDExcellenceArea.class)
	@TestCaseParams(testCaseID = { "90152"})
	public void testRegUserByGet_CourseProgressED_Excellence() throws Exception {
		
	report.startStep("Refresh page to play it safe");
		//webDriver.refresh();
		NewUXLoginPage loginPage = new NewUXLoginPage(webDriver, testResultService);
		loginPage.waitForPageToLoad();
				
	report.startStep("Create student and class variables");
		String baseUrl = pageHelper.CILink.split(".com")[0] + ".com/";
		userName = "stud" + dbService.sig(5);
		String userFN = userName + "-FN";
		String userLN = userName + "-LN";
		String email = userName + new Random().nextInt(1000) + "-@edusof-t.co.il";
		String newClass = "Class-" + dbService.sig(4);
		String regUserUrl = "";
		int wantedUnit = 1;
	
	report.startStep("Login to ED via RegUser");
	  try {
		  regUserUrl = baseUrl + "RegUserAndLogin.aspx?Action=I&UserName=" + userName + "" + "&Inst="
				  + institutionName + "&FirstName=" + userFN + "&LastName=" + userLN + "" + "&Password=12345&Email="
				  + email + "&Class=" + newClass + "" + "&Language=English&Link=" + pageHelper.linkED
				  + "&UseNameMapping=N&CreateClass=Y&UserType=S";

		  webDriver.openUrl(regUserUrl);
		  pageHelper.skipOptin();
		  homePage.closeAllNotifications();
		  pageHelper.skipOnBoardingHP();
		  homePage.waitHomePageloadedFully();
		  studentId = dbService.getUserIdByUserName(userName, institutionId);

		  report.startStep("Insert new user to automation table");
		  if (studentId != null){
			  dbService.insertUserToAutomationTable(institutionId, studentId, userName, classNameExcellence, baseUrl);
			  classId = dbService.getClassIdByClassName(newClass,institutionId);
			  dbService.insertClassToAutomationTable(classId, newClass, null, institutionId, studentId, baseUrl);
		  }

	report.startStep("Navigate to wanted course");	
		homePage.navigateToRequiredCourseOnHomePage(coursesNamesEDExcellence[0]);
		Thread.sleep(1500);
		homePage.clickOnUnitLessons(wantedUnit);
		homePage.clickOnLesson(0, 1);
		String courseName = dbService.getCourseNameById(coursesTOEIC[0]);
		courseName = courseName.toString().replace(" ", "").trim();
		
	report.startStep("Make some progress in firs unit");	
		makeProgressInCoursePage = new ToeicLearningAreaAndProgressPage(webDriver, testResultService);
		makeProgressInCoursePage.makeProgressInToeicCourse(courseName, coursesTOEIC[0], false, true, true, true);
		
	report.startStep("Check Unit progress in "+makeProgressInCoursePage.unitName+" progress Bar");
		String courseCompletionInPercents="";
		String averageTestScore = "";
		Integer progressOfUnit = 0;
		
		/*
		try {
			String strProgress = homePage.getUnitProgress(wantedUnit);
			progressOfUnit = Integer.parseInt(strProgress);
			List<String[]> progress = dbService.getUserUnitProgressAndTestAverage(studentId, coursesTOEIC[0]);
			textService.assertNotNull("Student heave no unit progress ",progressOfUnit);
			textService.assertEquals("Test average not match", progress.get(0)[1], averageTestScore);
			int unitProgressFromDB = (int)Double.parseDouble(progress.get(0)[0]);
			textService.assertEquals("Progress in unit not coresponding to DB", true, isProgressMatch(unitProgressFromDB, progressOfUnit));
		}catch (Exception e) {
			testResultService.addFailTest("Wrong Progress On DB", false, true);
		}catch (AssertionError e) {
			testResultService.addFailTest("Wrong Progress On DB", false, true);
		}  */
		
	try {
		
		report.startStep("Check Average Test Score and time on task are updated on HomePage");
			List<String[]> courseProgress = dbService.getStudentCourseProgress(studentId);
			sleep(1);
			courseCompletionInPercents = homePage.getCourseCompletionDisplayedOnHomePage();
			sleep(1);
			averageTestScore = homePage.getAverageTestScore();
			sleep(1);
			textService.assertNotNull("Student heave no course progress ",courseCompletionInPercents);
			textService.assertEquals("Test average not match", Integer.parseInt(courseProgress.get(0)[0]), Integer.parseInt(courseCompletionInPercents));
			//int timeOnTaskFromUI = homePage.getTimeOnTaskFromHomePage();
		    //int timeOnTaskFromDB = pageHelper.calculateTimeOnTaskFromDB(Integer.parseInt(courseProgress.get(0)[2]));
		    //textService.assertEquals("Time from UI not match to time from DB", timeOnTaskFromUI, timeOnTaskFromDB);
	}catch (Exception e) {
		testResultService.addFailTest("Wrong Progress On DB", false, true);
	}catch (AssertionError e) {
		testResultService.addFailTest("Wrong Progress On DB", false, true);
	}	
		
	report.startStep("Go to 'My Progress Page'");
		NewUxMyProgressPage chkProgressPage = homePage.clickOnMyProgress(); 
	    chkProgressPage.waitForPageToLoad();
		chkProgressPage.clickToOpenUnitLessonsProgress(1);
		sleep(3);
	    
	report.startStep("Verify average test score, unit-progress, course progress is displayed in 'My Progress Page' ");
		//String unitPrgsFromPrgsPage = chkProgressPage.getCertainProgressFromProgressPageByValue("UnitProgress");
		//chkProgressPage.verifyWhetherCourseOpened(coursesNamesEDExcellence[0]);
		String testScoreFromPrgsPage = chkProgressPage.getCertainProgressFromProgressPageByValue("TestScore",coursesNamesEDExcellence[0]);
		String coursePrgsFromPrgsPage = chkProgressPage.getCertainProgressFromProgressPageByValue("CourseProgress",coursesNamesEDExcellence[0]);
			
		try {
			//textService.assertTrue("Unit progress from UI not match to progress from Progress page", progressOfUnit.toString().equalsIgnoreCase(unitPrgsFromPrgsPage));
			textService.assertEquals("Test score not match", averageTestScore, testScoreFromPrgsPage);
			textService.assertEquals("Course progress not match", courseCompletionInPercents, coursePrgsFromPrgsPage);
		}catch (Exception e) {
			e.printStackTrace();
			testResultService.addFailTest("Some of the progress not stored",true,true);
		}catch (AssertionError e) {
			e.printStackTrace();
			testResultService.addFailTest("Some of the progress not stored",true,true);
		}
	report.startStep("LogOut from ED");
	    homePage.logOutOfED();
	    
	  }catch (Exception e) {
		  e.printStackTrace();
			testResultService.addFailTest(testName.getMethodName(),true,true);
	  }catch (AssertionError e) {
		  e.printStackTrace();
			testResultService.addFailTest(testName.getMethodName(),true,true);
	}
	  
	  finally{
		//report.startStep("Cleaning test data. Delete student and class");
			//dbService.deleteStudentByName(institutionId, userName);
			//dbService.deleteClassByName(institutionId, newClass);
	  }
		
	}
	
	
	@Test
	@Category(EDExcellenceArea.class)
	@TestCaseParams(testCaseID = { "90169"})
	public void testUnitTest_ED_Excellence() throws Exception {  //Module 1: High-beginner/ Unit 1/ UnitTest
		
	report.startStep("Init test data");
		String expectedTestResult = testScoreTOEIC[3];
		String courseName = coursesNamesEDExcellence[0];
		int numOfSections = numOfSectionsAnsweredTOEIC[3];
		int unitID = 1;
		String pathToAnswers = "files/TOEIC/UnitTestAnswers2.csv";		
			if(pageHelper.linkED.contains("ednewb.engdis.com")) {
				pathToAnswers = "files/TOEIC/UnitTestAnswersBeta.csv";
			}
	try {	
					
		report.startStep("Get user and login to EDExcellence institution");
			homePage = createUserAndLoginForOptIn(classNameExcellence, institutionId);
			sleep(8);
			pageHelper.skipOptin();
			homePage.closeAllNotifications();
			//homePage = getUserAndLogin("Combinado", institutionId, true, true);
			pageHelper.skipOnBoardingHP();
			
		report.startStep("Accomplish unit test");
			makeProgressInCoursePage = new ToeicLearningAreaAndProgressPage(webDriver, testResultService);
			ArrayList<String> UnitTestAnswers2 = makeProgressInCoursePage.accomplishUnitTest(unitID,courseName,expectedTestResult,numOfSections, pathToAnswers);
		
		report.startStep("Initialize my progress page");
			NewUxMyProgressPage myProgress = new NewUxMyProgressPage(webDriver, testResultService);
		
		report.startStep("Enter " + courseName + " My Progress Section");	
			homePage.navigateToRequiredCourseOnHomePage(courseName);
			//sleep(2);
			myProgress = homePage.clickOnMyProgress();
			myProgress.waitForPageToLoad();
		
		report.startStep("Verify Unit Test Score is Displayed in My Progress Page");
			myProgress.clickToOpenUnitLessonsProgress(1);
			testResultService.assertEquals(expectedTestResult.toString(), myProgress.checkUnitTestScoreOfSpecificUnit(unitID),"Scores aren't identical");
			sleep(2);
			myProgress.clickViewResultsInMyProgressPageForSpecificUnit();
			
		report.startStep("Click 'View Results' Button");	
			toeicResultsPage toeicResultspage = new toeicResultsPage(webDriver,testResultService);
			//toeicResultspage.checkAnswersInViewResultsPage(UnitTestAnswers2);
			toeicResultspage.verifyCorrectAnswerSignsOnTabs(UnitTestAnswers2.size());
		
		report.startStep("LogOut from ED");
			myProgress.logOutOfED();
		
	}catch (Exception e) {
		e.printStackTrace();
		testResultService.addFailTest(testName.getMethodName(), true, true);
	}catch (AssertionError e) {
		e.printStackTrace();
		testResultService.addFailTest(testName.getMethodName(), true, true);
	}
	finally {
		dbService.deleteStudentByName(institutionId, userName);
	}
		
	}
	
	
	@Test
	@Category(EDExcellenceArea.class)
	@TestCaseParams(testCaseID = { "91344"})
	public void testPractice_Midterm_ResultsInCorrectOrder() throws Exception {
		
	report.startStep("Init test data");	
		String wantedTestId = "989017755";
		String readingScore = "5-15"; 
		String listeningScore = "5-60"; 
		String courseName = coursesNamesEDExcellence[3];
		String testId = "";
		
try {
	report.startStep("Get user and login to EDExcellence institution");
		homePage = createUserAndLoginForOptIn(classNameExcellence, institutionId);
		loginPage = new NewUXLoginPage(webDriver, testResultService);
		loginPage.waitLoginAfterRestartAppPool();
		pageHelper.skipOptin();
		homePage.closeAllNotifications();
		pageHelper.skipOnBoardingHP();
		homePage.waitHomePageloadedFully();
		
	report.startStep("Open Assessments Page");
		testsPage = homePage.openAssessmentsPage(false);
				
	report.startStep("Check amount of Practice test in second section");
		testsPage.checkItemsCounterBySection("2", "8");
		
	report.startStep("Open First Section");
		testsPage.clickOnArrowToOpenSection("2");
		
	report.startStep("Click Start Test on the First Test");
		String clickedTestName = testsPage.clickStartTestByName(practiceTestNames[4]);
		//String clickedTestName = testsPage.clickStartTest(2, 2);
		sleep(2);
		webDriver.switchToPopup();
		toeicStartPage toeicStartpage = new toeicStartPage(webDriver,testResultService);
		toeicStartpage.waitForPageToLoad();
		
	report.startStep("Validate the URL is Correct");
		webDriver.validateURL("/Runtime/Test.aspx"); 
				
	report.startStep("Click Go Button");
		toeicStartpage.clickGoButton();
		
	report.startStep("Validate the Title is the Name of the First Test");
		toeicStartpage.validateTitle(clickedTestName);//clickedTestName
			
	report.startStep("Validate 'Test Your Sound' Button is Clickable");
		toeicStartpage.checkTestSoundButtonIsClickable();
	
	report.startStep("Validate the Welcome Text is Not Null");
		toeicStartpage.validateTheWelcomeTextIsNotNull();
		
	report.startStep("Click Start");
		toeicStartpage.clickStart();
		toeicQuestionPage toeicQuestionpage = new toeicQuestionPage(webDriver,testResultService);
		
	report.startStep("Click Next");
		toeicQuestionpage.clickNext();
		sleep(2);
		
	report.startStep("Answer All Questions in Section 1");
		ArrayList<String> finalAnswersArray = toeicQuestionpage.answerQuestionsInSeveralSections(1, "files/TOEIC/TOEICPracticeTest3.csv"); //TOEICBridgeFullPracticeTestAnswers.csv");
		
	report.startStep("Click Submit");
		toeicQuestionpage.submit(true);
		sleep(2);
	
	report.startStep("Close The Test");
		webDriver.switchToMainWindow();
		sleep(1);
		
	report.startStep("Open Assessments Page");
		testsPage = homePage.openAssessmentsPage(false);
		
	report.startStep("Check amount of Practice test results");
		testsPage.checkItemsCounterBySection("5", "1");
		
	report.startStep("Open third Section");
		testsPage.clickOnArrowToOpenSection("5");
	
	report.startStep("Check 'Full Report' and 'View Results' Buttons Are Available");
		boolean isFullReportButtonDisplayed = testsPage.checkViewFullReportDisplayed("1");
		boolean isResultButtonDisplayed = testsPage.checkViewResultsDisplayed("1");
		if(!isFullReportButtonDisplayed||!isResultButtonDisplayed) {
			testResultService.addFailTest("Full report button or Result button not displayed", true, true);
		}
				
	report.startStep("Click 'View Full Report' Button");
		testsPage.clickViewFullReport(1);
		Thread.sleep(1000);
		webDriver.switchToFrame("bsModal__iframe");
		Thread.sleep(1000);
						
	report.startStep("Validate Test Name in 'Full Report' Page");
		AssessmentsFullReportPage assessmentPage = new AssessmentsFullReportPage(webDriver, testResultService);
		assessmentPage.validateNameInFullReport2(clickedTestName+" Report");
	//	Thread.sleep(3000);
					
	report.startStep("Validate Test Date in 'Full Report' Page");
		String currentDate = webDriver.getCurrentDate();
		assessmentPage.validateDateInFullReport(currentDate);
			
	report.startStep("Validate Reading and Listening Scores in 'Full Report' Page");
		assessmentPage.checkReadingAndListeningScoresInFullReport(readingScore, listeningScore);	
	
	report.startStep("Close 'Full Report' Page");
		webDriver.getWebDriver().switchTo().parentFrame();
		testsPage.close();
		sleep(2);
				
	report.startStep("LogOut from ED");	
		homePage.logOutOfED();
				
	report.startStep("Assign B1 Mid-Term Test to student");
		TestEnvironmentPage testEnvironmentPage = new TestEnvironmentPage(webDriver, testResultService);
		String courseCode = courseCodes[1];
		CourseCodes wantedCourseCode = testEnvironmentPage.getCourseCodeEnumByCourseCodeString(courseCode);

		String courseId = getCourseIdByCourseCode(courseCode);
		pageHelper.assignAutomatedTestToStudetInTestEnvironment(studentId, courseId, 2,0,0,0);
		sleep(2);
		
	report.startStep("LogIn to ED");
			NewUXLoginPage loginPage = new NewUXLoginPage(webDriver, testResultService);
			homePage = loginPage.loginAsStudent(userName, "12345");
			homePage.closeAllNotifications();
			homePage.waitHomePageloaded();
			webDriver.refresh();
			sleep(1);

	report.startStep("Open Assessments Page");
			testsPage = homePage.openAssessmentsPage(false);
			
	report.startStep("Verify amount of Practice test didn't change");
			sleep(3);
			testsPage.checkItemsCounterBySection("2", "8");
			
	report.startStep("Start Test");
			int testSequence = testsPage.getTestSequenceByCourseId(courseId);
			String testName = testsPage.clickStartTest(1, testSequence);
			webDriver.closeAlertByAccept();
			sleep(4);
	
	report.startStep("Acomplish test without answers and submit it");
			testEnvironmentPage.waitForStartTetsButon(60);
			testEnvironmentPage.clickStartTest();
			sleep(2);
			testEnvironmentPage.clickNext();
			sleep(2);
			testEnvironmentPage.submitMidtermTestWithoutAnswers();
			sleep(2);
			
	report.startStep("Open assessment area");
			testsPage = homePage.openAssessmentsPage(false);
			
	report.startStep("Check that amount of 'Practice test results' doesn't rise after acomplishing Midterm test");
			testsPage.checkItemsCounterBySection("5", "1");
			
	report.startStep("Check that amount of 'Test results' after acomplishing Midterm test");	
			testsPage.checkItemsCounterBySection("4", "1");
		
	report.startStep("Open fourth Section");
			testsPage.clickOnArrowToOpenSection("4");
			testsPage.checkScoresInEDExcellenceByTest("0");
						
	report.startStep("Close Assessments Page");
			testsPage.close();

	report.startStep("Log Out of ED");
			sleep(1);
			homePage.logOutOfED();
		
	}catch (Exception e) {
		e.printStackTrace();
		testResultService.addFailTest(e.getMessage(), true, true);
	}catch (AssertionError e) {
		e.printStackTrace();
		testResultService.addFailTest(e.getMessage(), true, true);
	}finally {
		//dbService.deleteStudentByName(institutionId, userName);
		//report.startStep("copy back the original file to original path");
		//pageHelper.runConsoleProgram("cmd /c copy "+PageHelperService.ServerPath+"CourseTests\\ForAutomationBackup\\"+testId+".json "+PageHelperService.ServerPath+"CourseTests");	
	}
}

	@Test
	@Category(EDExcellenceArea.class)
	@TestCaseParams(testCaseID = { "91344"})
	public void testPracticeTestCheckReport() throws Exception {

		report.startStep("Init test data");
		String courseName = "TOEIC® Official Learning and Preparation Course 2";
		String reportType = "Practice Test Report";

		try {
			report.startStep("Get user and login to EDExcellence institution");
			homePage = createUserAndLoginForOptIn(classNameExcellence, institutionId);
			loginPage = new NewUXLoginPage(webDriver, testResultService);
			loginPage.waitLoginAfterRestartAppPool();
			pageHelper.skipOptin();
			homePage.closeAllNotifications();
			pageHelper.skipOnBoardingHP();
			homePage.waitHomePageloadedFully();
			userName = dbService.getUserNameById(studentId, institutionId);

			report.startStep("Open Assessments Page");
			testsPage = homePage.openAssessmentsPage(false);

			report.startStep("Check amount of Practice test in second section");
			testsPage.checkItemsCounterBySection("2", "8");

			report.startStep("Open First Section");
			testsPage.clickOnArrowToOpenSection("2");

			report.startStep("Click Start Test on the First Test");
			String clickedTestName = testsPage.clickStartTestByName(practiceTestNames[4]);

			sleep(2);
			webDriver.switchToPopup();
			toeicStartPage toeicStartpage = new toeicStartPage(webDriver, testResultService);
			toeicStartpage.waitForPageToLoad();

			report.startStep("Validate the URL is Correct");
			webDriver.validateURL("/Runtime/Test.aspx");

			report.startStep("Click Go Button");
			toeicStartpage.clickGoButton();

			report.startStep("Validate the Title is the Name of the First Test");
			toeicStartpage.validateTitle(clickedTestName);

			report.startStep("Validate 'Test Your Sound' Button is Clickable");
			toeicStartpage.checkTestSoundButtonIsClickable();

			report.startStep("Validate the Welcome Text is Not Null");
			toeicStartpage.validateTheWelcomeTextIsNotNull();

			report.startStep("Click Start");
			toeicStartpage.clickStart();
			toeicQuestionPage toeicQuestionpage = new toeicQuestionPage(webDriver, testResultService);

			report.startStep("Click Next");
			toeicQuestionpage.clickNext();
			sleep(2);

			report.startStep("Answer All Questions in Section 1");
			ArrayList<String> finalAnswersArray = toeicQuestionpage.answerQuestionsInSeveralSections(1, "files/TOEIC/TOEICPracticeTest3.csv"); //TOEICBridgeFullPracticeTestAnswers.csv");

			report.startStep("Click Submit");
			toeicQuestionpage.submit(true);
			sleep(2);

			report.startStep("Close The Test");
			webDriver.switchToMainWindow();
			sleep(1);

			report.startStep("LogOut from ED");
			homePage.logOutOfED();

			report.startStep("Login as Admin");
			loginPage = new NewUXLoginPage(webDriver, testResultService);
			String[] adminUser = dbService.getAdminUserByInstitutionId(institutionId);
			loginPage.loginAsStudent(adminUser[0], adminUser[1]);
			sleep(1);
			homePage.waitUntilLoadingMessageIsOver();
			homePage.closeAllNotifications();

			report.startStep("Navigate to course report page");
			TmsReportsPage tmsReportsPage = new TmsReportsPage(webDriver, testResultService);
			tmsReportsPage.goToCourseReports();
			tmsReportsPage.selectCourseReportAndClass(reportType, classNameExcellence);
			tmsReportsPage.selectCourseByCourseName(courseName);
			sleep(15);

			report.startStep("Filter by full practice last score");
			tmsReportsPage.filterByFullPracticeLastScore();
			sleep(4);
			report.startStep("Return student row");
			tmsReportsPage.returnRowStudent(userName);
			sleep(2);
			WebElement row = tmsReportsPage.returnRowStudent(userName);
			report.startStep("The result 1 in " + tmsReportsPage.getAttemptResultFullPractice(row) + "result 3 in" + tmsReportsPage.getLostScoreFullPractice(row));
			testResultService.assertEquals("1", tmsReportsPage.getAttemptResultFullPractice(row));
			testResultService.assertEquals("3", tmsReportsPage.getLostScoreFullPractice(row));

			report.startStep("Quit Browser");
			webDriver.quitBrowser();


		} catch (Exception e) {
			e.printStackTrace();
			testResultService.addFailTest(testName.getMethodName(), true, true);
		} catch (AssertionError e) {
			e.printStackTrace();
			testResultService.addFailTest(testName.getMethodName(), true, true);
		}
	}

	
	
	@Test
	@Category(EDExcellenceArea.class)
	@TestCaseParams(testCaseID = { "91343"})
	public void testPracticePltMidtermFinalCorrectOrder() throws Exception{
		
	try {	
		//int iteration = 1;
		//String unitId = "-1";
		String testId = "11";
		
	report.startStep("Get user and login to EDExcellence institution");
		homePage = createUserAndLoginForOptIn(classNameExcellence, institutionId);

		loginPage = new NewUXLoginPage(webDriver, testResultService);
		loginPage.waitLoginAfterRestartAppPool();
		//sleep(8);
		pageHelper.skipOptin();
		homePage.closeAllNotifications();
		pageHelper.skipOnBoardingHP();
		homePage.waitHomePageloadedFully();
		
	report.startStep("Assign PLT test to student");
		String adminId = dbService.getAdminIdByInstId(institutionId);
		String classId = dbService.getClassIdByName(classNameExcellence, institutionId);
		pageHelper.assignPltTestToStudetInTestEnvironment(studentId, 1, testId, adminId, institutionId, classId);
		sleep(3);
		
	report.startStep("Assign B1 Mid-Term Test to student");
		TestEnvironmentPage testEnvironmentPage = new TestEnvironmentPage(webDriver, testResultService);
		String courseCode = courseCodes[1];
		CourseCodes wantedCourseCode = testEnvironmentPage.getCourseCodeEnumByCourseCodeString(courseCode);

		String courseId = getCourseIdByCourseCode(courseCode);
		pageHelper.assignAutomatedTestToStudetInTestEnvironment(studentId, courseId, 2,0,0,0);
		sleep(2);	
	
	report.startStep("Assign B1 Final Test to student");
		pageHelper.assignAutomatedTestToStudetInTestEnvironment(studentId, courseId, 3,0,0,1);
		sleep(2);
		
	report.startStep("Open Assessments Page");
		testsPage = homePage.openAssessmentsPage(false);
		
	report.startStep("Verify amount of Practice test are 8");
		testsPage.checkItemsCounterBySection("2", "8");
	
	report.startStep("Verify amount of just assigned test are 3");
		testsPage.checkItemsCounterBySection("1", "3");
	
	report.startStep("Verify Placment test-name and position");
		testsPage.checkTestDisplayedInSectionByTestName("Placement Test", "1", "1");
		
	report.startStep("Verify Midterm test-name and position");
		testsPage.checkTestDisplayedInSectionByTestName("Basic 1 Midterm Test", "1", "2");
	
	report.startStep("Verify Final test-name and position");
		testsPage.checkTestDisplayedInSectionByTestName("Basic 1 Final Test", "1", "3");
		
	report.startStep("Close Assessments Page");
		testsPage.close();
		
	report.startStep("Log Out of ED");
		homePage.logOutOfED();
		
	}catch (Exception e) {
		e.printStackTrace();
		testResultService.addFailTest(e.getMessage(), true, true);
	}catch (AssertionError e) {
		e.printStackTrace();
		testResultService.addFailTest(e.getMessage(), true, true);
	}finally {
		//dbService.deleteStudentByName(institutionId, userName);
	}
}
	
	
	@Test
	@Category(EDExcellenceArea.class)
	@TestCaseParams(testCaseID = { "91554"})
	public void testForceLogInOnEDExcellence() throws Exception{
		
	try {	
	
	report.startStep("Create user for login");
		studentId = pageHelper.createUSerUsingSP(institutionId, classNameExcellence); 
		userName = dbService.getUserNameById(studentId, institutionId);
		String expectedTestResult = testScoreTOEIC[3];
		int numOfSections = numOfSectionsAnsweredTOEIC[3];
		
/*	report.startStep("logIn with fireFox");
		System.setProperty("webdriver.gecko.driver",configuration.getPathToFirefoxDriver());
		firefoxPage = new FirefoxLoginPage(pageHelper.linkED);
		firefoxPage.loginToEDwithFirefox(userName, "12345");
		firefoxPage.closeNotificationsInFirefox();
		firefoxPage.verifyUserLogedInFirefox(userName);*/  // useful for local running
		
	report.startStep("Login to Firefox with same user BROWSER No.2");
		firefoxDriver = (FirefoxWebDriver) ctx.getBean("FirefoxWebDriver");
		firefoxDriver.init();
		sleep(1);
		firefoxDriver.maximize();
		firefoxDriver.openUrl(pageHelper.linkED);
		
		NewUXLoginPage loginPage2 = new NewUXLoginPage(firefoxDriver, testResultService);
		loginPage2.waitForPageToLoad();
		NewUxHomePage homePage2 = loginPage2.loginAsStudent(userName, "12345");
		loginPage2.waitLoginAfterRestartAppPool();
		//sleep(3);
		
	report.startStep("Verify and confirm alert of Force login");
		pageHelper.skipOptin();
		homePage2.closeAllNotifications();
		homePage2.waitHomePageloaded();
				
	report.startStep("ForceLogin with Chrome");
		NewUXLoginPage loginPage = new NewUXLoginPage(webDriver, testResultService);
		loginPage.loginAsStudent(userName, "12345");
		loginPage.verifyAndConfirmAlertMessage();
		homePage.closeAllNotifications();
		homePage.waitHomePageloadedFully();
		
	report.startStep("Check user loged out in FireFox");
		loginPage2.verifyUserGetSessionTimeOut();
		//firefoxPage.checkUserLogedOutInFireFox();  useful for local running
		
	report.startStep("Get all courses from course-list");	
		homePage.clickOnCourseList();
		sleep(2);
		List<WebElement> coursesFromCourseList = homePage.getCourseListCourseELements();
		
	report.startStep("Check all TOEIC courses still available after force-login");
		List<String> courseNamesFromList = coursesFromCourseList.stream().map(c->c.getText().toString()).collect(Collectors.toList());
		for(String toeicCourse:coursesNamesEDExcellence) {
			textService.assertTrue("Course '"+toeicCourse+"' are missing after force-login",courseNamesFromList.contains(toeicCourse));
		}
		
	report.startStep("Check all ED courses still available after force-login");	
		for(String edCourse:coursesNames) {
			textService.assertTrue("Course '"+edCourse+"' are missing after force-login",courseNamesFromList.contains(edCourse));
		}
		
	report.startStep("Open Assessments Page");
		testsPage = homePage.openAssessmentsPage(false);
				
	report.startStep("Check amount of Practice test in second section");
		testsPage.checkItemsCounterBySection("2", "8");
		
	report.startStep("Close Assessments Page");
		testsPage.close();	
				
	report.startStep("Navigate to " + coursesNamesEDExcellence[0]+" , and Accomplish unit test");
		String pathToAnswers = "files/TOEIC/UnitTestAnswers2.csv";		
			if(pageHelper.linkED.contains("ednewb.engdis.com")) {
				pathToAnswers = "files/TOEIC/UnitTestAnswersBeta.csv";
			}
		makeProgressInCoursePage = new ToeicLearningAreaAndProgressPage(webDriver, testResultService);
		ArrayList<String> UnitTestAnswers2 = 
				makeProgressInCoursePage.accomplishUnitTest(1,coursesNamesEDExcellence[0],expectedTestResult,numOfSections, pathToAnswers);
		
	report.startStep("Check Unit Objective are present");	
		homePage.clickOnUnitLessons(1);
		homePage.checkUnitObjectivePerEDExcellence();
		
	report.startStep("Check progress in my-progress page");	
		NewUxMyProgressPage myProgress = new NewUxMyProgressPage(webDriver, testResultService);
		myProgress = homePage.clickOnMyProgress();
		myProgress.waitForPageToLoad();
	
	report.startStep("Verify Unit Test Score is Displayed in My Progress Page");
		myProgress.clickToOpenUnitLessonsProgress(1);
		testResultService.assertEquals(expectedTestResult.toString(), myProgress.checkUnitTestScoreOfSpecificUnit(1),"Scores aren't identical");
		
	report.startStep("Log out");	
		homePage.logOutOfED();
		
	}catch (Exception e) {
		testResultService.addFailTest(e.getMessage(), true, true);
	}catch(AssertionError e) {
		testResultService.addFailTest(e.getMessage(), true, true);
	}
	finally {
		dbService.deleteStudentByName(institutionId, userName);
		firefoxDriver.quitBrowser();
		firefoxDriver.getWebDriver().quit();
		firefoxDriver.tearDown();
		//firefoxPage.closeSession(); useful for local running
	
	
	}
}
	
	//@Test
	//@Category(inProgressTests.class)
	//@TestCaseParams(testCaseID = { "91647"})
	public void testSlidingExpirationOnTOEICourse() throws Exception {
		
		report.startStep("Wanted scores on Practice-test");
			String readingScore = "15-15"; 
			String listeningScore = "15-15";
		
		try {	
			
			report.startStep("Refresh page to play it safe");
				sleep(2);
				webDriver.refresh();
				NewUXLoginPage loginPage = new NewUXLoginPage(webDriver, testResultService);
				loginPage.waitForPageToLoad();
			
			report.startStep("Set expiration time to service appjson settings (minutes)");
				pageHelper.setSessionExpirationTime("2");
										
			report.startStep("restart the application pool");
				pageHelper.restartWebServiceApplicationPool();
				sleep(2);
				webDriver.refresh();
				loginPage.waitForPageToLoad();
							
			report.startStep("Create user and Login to ED");
				studentId = pageHelper.createUSerUsingOptIn(institutionId, classNameExcellence);
				userName = dbService.getUserNameById(studentId, institutionId);
				homePage = loginPage.loginAsStudent(userName, "12345");
				sleep(1);
				loginPage.waitLoginAfterRestartAppPool();
			 	pageHelper.skipOptin();
				homePage.waitNotificationsAfterRestartPool();
				homePage.closeAllNotifications();
				pageHelper.skipOnBoardingHP();
				homePage.waitHomePageloadedFully();
				studentId = dbService.getUserIdByUserName(userName, institutionId);
				
			report.startStep("Navigate to task in ED course: "+courseCodes[1]);
				learningArea2 = new NewUxLearningArea2(webDriver, testResultService);
				learningArea2 = homePage.navigateToTask(courseCodes[1], 1, 1, 2, 2);
				
			report.startStep("Retrieve session token and wait till half of the session passed");
				String firstToken = pageHelper.runJavaScriptCommand("return localStorage.getItem(\"EDAPPToken\")");
				sleep(61);
				System.out.println("First token is: "+firstToken);
				
			report.startStep("Make some progress and verify that session token updated");
				learningArea2.SelectRadioBtn("question-1_answer-1");
				learningArea2.clickOnNextButton();
				sleep(1);
				String secondToken = pageHelper.runJavaScriptCommand("return localStorage.getItem(\"EDAPPToken\")");
				System.out.println("Second token is: "+secondToken);
				textService.assertEquals("Token not updated after half of the session has gone", false, firstToken.equals(secondToken));
				
			report.startStep("Return to homePage and wait till half of the session passed");	
				learningArea2.clickOnHomeButton();
				sleep(65);
				
			report.startStep("Navigate to TOEIC course and make sure you still logged in");
				homePage.navigateToRequiredCourseOnHomePage(coursesNamesEDExcellence[3]);
				testResultService.assertEquals("Hello " + userName + "", homePage.getUserDataText(),
						"First Name in Header of Home Page not match");
								
			report.startStep("Open Assessments Page");
				testsPage = homePage.openAssessmentsPage(false);
						
			report.startStep("Check amount of Practice test in second section");
				testsPage.checkItemsCounterBySection("2", "8");
				
			report.startStep("Open First Section");
				testsPage.clickOnArrowToOpenSection("2");
				
			report.startStep("Click Start Test on the First Test");
				String clickedTestName = testsPage.clickStartTestByName(practiceTestNames[1]);
				//String clickedTestName = testsPage.clickStartTest(2, 2);
				sleep(2);
				webDriver.switchToPopup();
				toeicStartPage toeicStartpage = new toeicStartPage(webDriver,testResultService);
				toeicStartpage.waitForPageToLoad();
				
				report.startStep("Click Go Button");
				toeicStartpage.clickGoButton();
				
			report.startStep("Validate the Title is the Name of the First Test");
				toeicStartpage.validateTitle(clickedTestName);
									
			report.startStep("Click Start");
				toeicStartpage.clickStart();
				toeicQuestionPage toeicQuestionpage = new toeicQuestionPage(webDriver,testResultService);
				
			report.startStep("Click Next");
				toeicQuestionpage.clickNext();
				sleep(2);
				
			report.startStep("Answer All Questions in Section 1");
				ArrayList<String> finalAnswersArray = toeicQuestionpage.answerQuestionsInSeveralSections(1,"files/TOEIC/TOEICBridgeFullPracticeTestAnswers.csv");//files/TOEIC/TOEICPracticeTest3.csv"); //
				
			report.startStep("Click Submit");
				toeicQuestionpage.submit(true);
				sleep(2);
			
			report.startStep("Close The Test");
				webDriver.switchToMainWindow();
				sleep(1);
				
				report.startStep("Open Assessments Page");
				testsPage = homePage.openAssessmentsPage(false);
				
			report.startStep("Check amount of Practice test results");
				testsPage.checkItemsCounterBySection("5", "1");
				
			report.startStep("Open third Section");
				testsPage.clickOnArrowToOpenSection("5");
			
			report.startStep("Check 'Full Report' and 'View Results' Buttons Are Available");
				boolean isFullReportButtonDisplayed = testsPage.checkViewFullReportDisplayed("1");
				boolean isResultButtonDisplayed = testsPage.checkViewResultsDisplayed("1");
				if(!isFullReportButtonDisplayed||!isResultButtonDisplayed) {
					testResultService.addFailTest("Full report button or Result button not displayed", true, true);
				}
						
			report.startStep("Click 'View Full Report' Button");
				testsPage.clickViewFullReport(1);
				Thread.sleep(1000);
				webDriver.switchToFrame("bsModal__iframe");
				Thread.sleep(1000);
								
			report.startStep("Validate Test Name in 'Full Report' Page");
				AssessmentsFullReportPage assessmentPage = new AssessmentsFullReportPage(webDriver, testResultService);
				assessmentPage.validateNameInFullReport2(clickedTestName+" Report");
				Thread.sleep(3000);
							
			report.startStep("Validate Test Date in 'Full Report' Page");
				String currentDate = webDriver.getCurrentDate();
				assessmentPage.validateDateInFullReport(currentDate);
					
			report.startStep("Validate Reading and Listening Scores in 'Full Report' Page");
				assessmentPage.checkReadingAndListeningScoresInFullReport(readingScore, listeningScore);	
			
			report.startStep("Close 'Full Report' Page");
				webDriver.getWebDriver().switchTo().parentFrame();
				testsPage.close();
				sleep(2);
							
			report.startStep("LogOut from ED");
				homePage.logOutOfED();
			 
			  }catch (Exception e) {
				e.printStackTrace();
			  }catch(AssertionError e){
				  e.printStackTrace();
			  }
				  finally {
			  
					report.startStep("Return normall value (120 min) to session expiration time and restart the app-pool");
						pageHelper.setSessionExpirationTime("120");
						pageHelper.restartWebServiceApplicationPool();
						
					report.startStep("Cleaning test data");
						dbService.deleteStudentByName(institutionId, userName);						
				}
	}
	
	
	@Test
	@Category(EDExcellenceTests.class)
	@TestCaseParams(testCaseID = { "91647"})
	public void testCourseLicenseExpiration() throws Exception {
		
	try {
		report.startStep("Get user and login to EDExcellence institution");
			homePage = getUserAndLogin(classNameExcellence, institutionId, true, true);
			pageHelper.skipOnBoardingHP();
			homePage.waitHomePageloadedFully();
						
		report.startStep("Verify 'License Expiration' of course: '"+coursesNames[0]+"', are displayed");
			homePage.navigateToRequiredCourseOnHomePage(coursesNames[0]);
			String license1 = homePage.getLicenseExpirationWidgetValue();
			//String licenseHour1 = homePage.getLicenseHourExpiration();
			textService.assertTrue("Something wrong in license expiration data", license1.length()>=10);
			//textService.assertTrue("Something wrong in license expiration hour data", licenseHour1.length()>=3);
			
		report.startStep("Verify 'License Expiration' of course: '"+coursesNamesEDExcellence[0]+"', are displayed");
			homePage.navigateToRequiredCourseOnHomePage(coursesNamesEDExcellence[0]);
			String license2 = homePage.getLicenseExpirationWidgetValue();
			//String licenseHour2 = homePage.getLicenseHourExpiration();
			textService.assertTrue("Something wrong in license expiration data", license2.length()>=10);
			//textService.assertTrue("Something wrong in license expiration hour data", licenseHour2.length()>=3);	
			
			
		report.startStep("Verify 'License Expiration' of course: '"+coursesNamesEDExcellence[3]+"', are displayed");
			homePage.clickOnCourseList();
			sleep(2);
			List<WebElement> coursesFromCourseList = homePage.getCourseListCourseELements();
			WebElement wantedCourse = coursesFromCourseList.stream().filter(e->e.getText().contains(coursesNamesEDExcellence[3])).findAny().orElse(null);
			wantedCourse.click();
			sleep(3);
			String license3 = homePage.getLicenseExpirationWidgetValue();
			//String licenseHour3 = homePage.getLicenseHourExpiration();
			textService.assertTrue("Something wrong in license expiration data", license3.length()>=10);
			//textService.assertTrue("Something wrong in license expiration hour data", licenseHour3.length()>=3);
					
		report.startStep("Open my progress page");
			NewUxMyProgressPage myProgressPage = homePage.clickOnMyProgress();
			myProgressPage.waitForPageToLoad();
			
		report.startStep("Get Licenses of '"+coursesNamesEDExcellence[3]+"'");	
			sleep(2);
			webDriver.scrollToElement(webDriver.waitForElement("home__studentWidgetLicenceExpirationData", ByTypes.id));
			String licensePerTOEICBridge = myProgressPage.getLicenseExpirationWidgetValue();
			
		report.startStep("Get Licenses of '"+coursesNames[0]+"'");		
			List<WebElement> courseNames = webDriver.getElementsByXpath("//*[@class='myProgress__courseTitleText ng-binding']");
			try {
				WebElement el = webDriver.waitForElement("//div[@class='mCustomScrollBox mCS-onLightBackground mCSB_vertical mCSB_inside']/div[@class='mCSB_container']", ByTypes.xpath);
				String elementToSetScroll = el.getAttribute("id");
				webDriver.executeJsScript("document.getElementById('"+elementToSetScroll+"').setAttribute('style', 'position: relative; top: 0px; left: 0px;')");
			}catch (Exception e) {					
			}catch (AssertionError e) {				
			}
			courseNames.get(0).click();
			sleep(1);
			String licensePerEnglishDiscoveries = myProgressPage.getLicenseExpirationWidgetValue();
			
		report.startStep("Compare that values from progress-page are same as on home-page");	
			textService.assertEquals("Licenses values are diffrent between home-page and progress-page", license1.trim(), licensePerEnglishDiscoveries.trim());
			textService.assertEquals("Licenses values are diffrent between home-page and progress-page", license3.trim(), licensePerTOEICBridge.trim());
				
		report.startStep("Log out of ED");
			myProgressPage.logOutOfED();
			
		}catch (Exception e) {	
			testResultService.addFailTest(e.getMessage(), true, true);
		}catch (AssertionError e) {	
			testResultService.addFailTest(e.getMessage(), true, true);
		}	
	}
	
	
	
	@Test
	@Category(EDExcellenceTests.class)
	@TestCaseParams(testCaseID = { "91746"})
	public void testMarkedAnsweredQuestion() throws Exception {
	
	try {
					
		report.startStep("Create user and login");	
			homePage = createUserAndLoginForOptIn(classNameExcellence, institutionId);
			NewUXLoginPage loginPage = new NewUXLoginPage(webDriver, testResultService);
			loginPage.waitLoginAfterRestartAppPool();
			pageHelper.skipOptin();
			homePage.closeAllNotifications();
			pageHelper.skipOnBoardingHP();
			homePage.waitHomePageloadedFully();
		
		report.startStep("Navigate to task in course "+courseCodes[1]);	
			learningArea2 = homePage.navigateToTask(courseCodes[1], 1, 1, 2, 2);
			
		report.startStep("Making progress");	
			learningArea2.SelectRadioBtn("question-1_answer-1");
			learningArea2.clickOnNextButton();
			sleep(1);
			NewUxDragAndDropSection2 dragAndDrop2 = new NewUxDragAndDropSection2 (webDriver, testResultService);
			dragAndDrop2.dragAndDropFirstAnswer(TaskTypes.MTTP);
			sleep(1);
			learningArea2.clickOnNextButton();
					
		report.startStep("Open task-bar and verify answered question marked");
			learningArea2.openTaskBar();
			learningArea2.verifyAnswersMarked();
			learningArea2.closeTaskBar();
			
		report.startStep("Store last visited lesson-name before landing to home-page");
			String lessonTitleLA = learningArea2.getLessonNameFromHeader();
			learningArea2.clickOnHomeButton();
			homePage.waitHomePageloaded();
			
		report.startStep("Click on continue and verify that you returned to same lesson");	
			learningArea2 = homePage.clickOnContinueButton2();
			learningArea2.waitForPageToLoad();
			String lessonTitleLA2 = learningArea2.getLessonNameFromHeader();
			textService.assertEquals("You landed to not same lesson", lessonTitleLA, lessonTitleLA2);
			
		report.startStep("Open task-bar and verify answered question remain be marked");
			learningArea2.openTaskBar();
			learningArea2.verifyAnswersMarked();
			learningArea2.closeTaskBar();
			
		report.startStep("LogOut of ED");
			learningArea2.logOutOfED();
			
		}catch (Exception e) {
			testResultService.addFailTest(e.getMessage(), true, true);
		}catch (AssertionError e) {
			testResultService.addFailTest(e.getMessage(), true, true);
		}finally {
			report.startStep("Cleaning test data. Delete student and class");		
			dbService.deleteStudentByName(institutionId, userName);
		}
		
	}
	
	/*@Test
	public void testKerberos() throws Exception  {
		
		System.setProperty("java.security.auth.login.config", "files/Kerberos/kerberos.conf");
		
	 	String user = configuration.getGlobalProperties("domain.user");
		String pass = configuration.getGlobalProperties("domain.pass");
	    KerberosDemo kd = new KerberosDemo();
	    Subject serviceSubject = kd.doInitialLogin(user,pass);
	}*/
	
	
	
	private boolean isProgressMatch(int progressFromDB, int progressOfUnit) {
		boolean progressMatch = false;
		int res = progressOfUnit - progressFromDB;
		if(res==0||res==1) {
			progressMatch = true;
		}
		return progressMatch;
	}

	private void createEventForToeicBridge() throws Exception {
		WebElement dateInput = webDriver.waitForElement("toeic_date_pick", ByTypes.id);
		dateInput.click();
		sleep(2);
		List<WebElement> days = webDriver.getElementsByXpath("//a[contains(@class,'ui-state-default')]");
		LocalDate date = LocalDate.now();
		Integer day = date.getDayOfMonth();
		WebElement dayToClick = days.stream().filter(d->d.getText().equalsIgnoreCase(day.toString())).findAny().orElse(null);
		sleep(1);
		dayToClick.click();
		WebElement timeInput = webDriver.waitForElement("toeic_time_pick", ByTypes.id);
		timeInput.click();
		sleep(1);
		List<WebElement> times = webDriver.getElementsByXpath("//div[@class='xdsoft_time_variant']/div[not(contains(@class,'xdsoft_disabled'))]"); 
		sleep(1);
		times.get(0).click();
		WebElement okBtn = webDriver.waitForElement("btn_ok", ByTypes.id);
		okBtn.click();
		
	}

	private void selectStudentForTOEICtest(String className) throws Exception {
		//new Select(webDriver.waitForElement("selectClass",ByTypes.id)).selectByVisibleText(className);
		WebElement el = webDriver.waitForElement("selectClass", ByTypes.id);
		for(int i = 0;i<5&&el==null;i++) {
			el = webDriver.waitForElement("selectClass", ByTypes.id);
		}
		
		webDriver.selectElementFromComboBox("selectClass",className);
		sleep(3);
		List<WebElement> checkBoxes = webDriver.getElementsByXpath("//input[@type='checkbox']");
		int iterations = checkBoxes.size();
		for(int i = 0;i<iterations;i++) {
			checkBoxes.get(i).click();
			Thread.sleep(500);
			checkBoxes = webDriver.getElementsByXpath("//input[@type='checkbox']");
		}
		sleep(2);
		List<WebElement> studentNames = webDriver.getElementsByXpath("//tr[contains(@class,'ui-widget-content')]/td[4]");
		sleep(1);
		for(int i = 0;i<studentNames.size();i++) {
			if(studentNames.get(i).getText().equalsIgnoreCase(userName)) {
				checkBoxes.get(i).click();
				Thread.sleep(500);
			}
		}
		webDriver.waitForElement("btn_ok", ByTypes.id).click();
	}

	private void configureAndSelectToeicTest(String feature, String testType, String event) throws Exception {
		
		webDriver.switchToTopMostFrame();
		tmsHomePage.switchToMainFrame();
		tmsHomePage.switchToFormFrame();
		try {
			tmsHomePage.selectFeature(feature);
			sleep(1);
			webDriver.selectElementFromComboBox("SelectToeicTestType", testType);
			sleep(1);
			webDriver.selectElementFromComboBox("SelectToeicTestEvent", event);
			sleep(2);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void generateNewTxtFileForImportSudents(String userName, String filePath) throws Exception {
		List<String> fileListContains = new ArrayList<>();
		fileListContains.add("FirstName	LastName	UserName	Password	Gender	Email	Class");
		fileListContains.add(userName+"	"+userName+"	"+userName+"	12345	u	"+userName+"@mail.com	class200");
		//textService.writeListToSmbFile(filePath, fileName, fileListContains, false);
		textService.writeTextToFileWithSeveralLines(filePath, fileListContains);
		//textService.writeListToSmbFile(filePath+fileName, fileListContains, netService.getDomainAuth());
	}

	private String[] verifyLicenses(List<String[]> institutionPackagesBefore) throws Exception {

		String[] packageDetails = new String[2];
		List<String[]> institutionPackagesAfter = dbService.getInstitutionPackages(institutionId);
		for (int i = 0; i< institutionPackagesBefore.size(); i++){
			boolean equals = institutionPackagesBefore.get(i)[3].equals(institutionPackagesAfter.get(i)[3]);
			if (!equals){
				packageDetails[0] = institutionPackagesAfter.get(i)[3];
				packageDetails[1] = institutionPackagesAfter.get(i)[1];
				break;
			}
		}
		testResultService.assertTrue("Used license not increased after student's login",packageDetails[1].length()>0);
	return packageDetails;
	}
	
	
}
