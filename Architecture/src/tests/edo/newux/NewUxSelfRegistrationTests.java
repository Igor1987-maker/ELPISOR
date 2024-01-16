package tests.edo.newux;

import Enums.ByTypes;
import Enums.PLTStartLevel;
import Interfaces.TestCaseParams;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import pageObjects.edo.*;
import pageObjects.tms.DashboardPage;
import pageObjects.tms.SelfRegistrationPage;
import testCategories.edoNewUX.SanityTests;

import java.util.ArrayList;
import java.util.List;

public class NewUxSelfRegistrationTests extends SpeechRecognitionBasicTestNewUX {

	boolean LaureatePLT = false;  // igb 2018.06.06
	
	NewUXLoginPage loginPage;
	DashboardPage dashboardPage;
	NewUxLearningArea learningArea;
	NewUxAssessmentsPage testsPage;
	NewUxInstitutionPage ipage;
	NewUxMyProfile myProfile;
	NewUxCommunityPage communityPage;
    NewUxSelfRegistrationPage register;
    NewUxClassicTestPage pltTest;
    SelfRegistrationPage selfRegistrationPage;
	
	//String instID = "";
	String userName ="";
	String folderPath = "Languages/";
	
	@Before
	public void setup() throws Exception {
		super.setup();
	}
	
	@Test
	@TestCaseParams(testCaseID = { "33692" })
	public void testSelfRegistrationEntryCriteria_NoClass_NoPlt_OldTE() throws Exception {

		report.startStep("Restart Browser in New URL");
		institutionName=institutionsName[3];
		pageHelper.initializeData();

		/*pageHelper.initializeData();
		String url = webDriver.getUrl();
		url = url.split(".com")[0] + ".com/" + BasicNewUxTest.institutionName;
		webDriver.quitBrowser();
		webDriver.init();
		webDriver.maximize();
		webDriver.openUrl(url);*/
		
		report.startStep("Prepare test data for Self-Registration");
		userName = "student" + dbService.sig(6);
		//instID = localInstId; //configuration.getLocalInstitutionId();
				
		report.startStep("Check no Register link displayed");
		loginPage = new NewUXLoginPage(webDriver, testResultService);

		pageHelper.restartBrowserInNewURL(institutionName, true);

		report.startStep("Set Option to Not Add User to Class after Registration, And to Not Open PLT");
		pageHelper.enableSelfRegistrationNoAddToClassNoPLT(institutionId);
		pageHelper.clearLocalStorage();
		webDriver.deleteCookiesAndRefresh();
		sleep(1);
		//loginPage.openSelfRegistrationPage();

		report.startStep("Register and login");
		registerAndLogin(userName);
		studentId = dbService.getUserIdByUserName(userName, institutionId);

		report.startStep("Check user not assigned to class");
		studentId = dbService.getUserIdByUserName(userName, institutionId);
		pageHelper.checkUserNotAssignedToClasses(studentId);

		if (studentId != null){
			String baseUrl = pageHelper.CILink.split(".com")[0] + ".com/";
			//dbService.insertUserToAutomationTable(institutionId, studentId, userName, CanvasClassName, baseUrl);
			dbService.insertUserToAutomationTable(institutionId,studentId,userName,"",baseUrl);
		}

		report.startStep("Validate PLT Is Not Opened");
		testResultService.assertEquals(false, checkIfPltOpened(), "PLT Opened Though it Was Not Configured");

	}
	
	@Test
	@TestCaseParams(testCaseID = { "33653" })
	public void testSelfRegistration_AddToClass_NoPlt_OldTE() throws Exception {
		
		report.startStep("Restart Browser in New URL");
		institutionName=institutionsName[3];
		pageHelper.restartBrowserInNewURL(institutionName, true); 

		/*pageHelper.initializeData();
		String url = webDriver.getUrl();
		url = url.split(".com")[0] + ".com/" + BasicNewUxTest.institutionName;
		webDriver.quitBrowser();
		webDriver.init();
		webDriver.maximize();
		webDriver.openUrl(url);*/
		
		report.startStep("Prepare test data for Self-Registration");
		userName = "student" + dbService.sig(6);
		//instID = localInstId;//configuration.getLocalInstitutionId();
		String classId =  dbService.getClassIdByName(configuration.getLocalclassName(), institutionId);
		String className = dbService.getClassNameByClassId(classId);

		report.startStep("Set Option to Automattically Add User to Class after Registration, and Open PLT");
		pageHelper.enableSelfRegistrationWithAddToClassNoPLT(institutionId, classId);
		pageHelper.clearLocalStorage();
		webDriver.deleteCookiesAndRefresh();
						
		report.startStep("Register and login");
		loginPage = new NewUXLoginPage(webDriver, testResultService);
		registerAndLogin(userName);
		studentId = dbService.getUserIdByUserName(userName, institutionId);

		if (studentId != null){
			String baseUrl = pageHelper.CILink.split(".com")[0] + ".com/";
			//dbService.insertUserToAutomationTable(institutionId, studentId, userName, CanvasClassName, baseUrl);
			dbService.insertUserToAutomationTable(institutionId,studentId,userName,className,baseUrl);
		}

		report.startStep("Check User Assigned to Class");
		//studentId = dbService.getUserIdByUserName(userName, instID);
		testResultService.assertEquals(true, pageHelper.isUserAssignedToClass(studentId, classId));
			
		report.startStep("Validate PLT Is Not Opened");
		testResultService.assertEquals(false, checkIfPltOpened(), "PLT Opened Though it Was Not Configured");
		
	}
	
	@Test
	@TestCaseParams(testCaseID = { "33654" })
	public void testSelfRegistration_NoClass_WithPLT_OldTE() throws Exception {

		report.startStep("Restart Browser with New URL");
		institutionName=institutionsName[3];
		pageHelper.restartBrowserInNewURL(institutionName, true); 
		
		/*pageHelper.initializeData();
		String url = webDriver.getUrl();
		url = url.split(".com")[0] + ".com/" + BasicNewUxTest.institutionName;
		
		webDriver.quitBrowser();
		webDriver.init();
		webDriver.maximize();
		webDriver.openUrl(url);*/
		
		report.startStep("Prepare test data for Self-Registration");
		userName = "student" + dbService.sig(5);
		//instID = localInstId; //configuration.getLocalInstitutionId();
						
		report.startStep("Set Option to Not Add User to Class After Registration, and Open PLT");
		pageHelper.enableSelfRegistrationNoAddToClassWithPLT(institutionId);
		pageHelper.clearLocalStorage();
		webDriver.deleteCookiesAndRefresh();
		
		report.startStep("Register and login");
		loginPage = new NewUXLoginPage(webDriver, testResultService);
		registerAndLogin(userName);
		studentId = dbService.getUserIdByUserName(userName, institutionId);

		if (studentId != null){
			String baseUrl = pageHelper.CILink.split(".com")[0] + ".com/";
			//dbService.insertUserToAutomationTable(institutionId, studentId, userName, CanvasClassName, baseUrl);
			dbService.insertUserToAutomationTable(institutionId,studentId,userName,"",baseUrl);
		}

		report.startStep("Check PLT Opens and Start Test -> and Then Exit PLT");
		sleep(1);
		webDriver.switchToNewWindow();
		pltTest = new NewUxClassicTestPage(webDriver, testResultService);
		pltTest.verifyTitlePLT();
		pltTest.selectLevelOnStartPLT(PLTStartLevel.Basic);
		pltTest.pressOnStartPLT();
		sleep(2);
		pltTest.exitDuringPLT(false);
					
		report.startStep("Open Assessments");
		testsPage = homePage.openAssessmentsPage(false);
					
		report.startStep("Open Tests Results section");
		testsPage.clickOnArrowToOpenSection("3");
							
		report.startStep("Check Score in Test Results");
		testsPage.checkScoreLevelPLT("1", "A1"); // CEFR inst = First Discoveries
		
		report.startStep("Check User Not Assigned to Class");
		//studentId = dbService.getUserIdByUserName(userName, instID);
		pageHelper.checkUserNotAssignedToClasses(studentId);
	}
	
	
	@Test
	//@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "" })
	public void testSelfRegistration_AddToClass_WithPLT_OldTE() throws Exception {
		report.startStep("Restart Browser with New URL");
		institutionName=institutionsName[3];
		pageHelper.initializeData();
		pageHelper.restartBrowserInNewURL(institutionName, true);
		
		/*pageHelper.initializeData();
		String url = webDriver.getUrl();
		url = url.split(".com")[0] + ".com/" + BasicNewUxTest.institutionName;
		
		webDriver.quitBrowser();
		webDriver.init();
		webDriver.maximize();
		webDriver.openUrl(url);*/
		
		report.startStep("Prepare test data for Self-Registration");
		userName = "student" + dbService.sig(5);
		String className = configuration.getLocalclassName();
		String classId =  dbService.getClassIdByName(className, institutionId);
		
		report.startStep("Set Option to Automatically Add User to Class After Registration, and Open PLT");
		pageHelper.enableSelfRegistrationAddToClassWithPLT(institutionId, classId);
		pageHelper.clearLocalStorage();
		webDriver.deleteCookiesAndRefresh();
	//	pageHelper.restartBrowserInNewURL(institutionName, true);

		report.startStep("Get institution Remaining License for packages assigned to class of SelfReg");
		//List<String[]> getValidInstitutionPackages = dbService.getValidInstitutionPackages(institutionId);
		List<String[]> classPackages = dbService.getClassAssignPackagesNew(className, institutionId);
		List<String[]> remainingLicensesBeforeAddingUser = new ArrayList<>();
			if (classPackages!=null) {
				remainingLicensesBeforeAddingUser = dbService.getRemainingLicensesOfCertainPackage(classPackages, institutionId);
			}else {
				testResultService.addFailTest("ClassHasNoPackages",false,false);
			}
		report.startStep("Register and login");
		loginPage = new NewUXLoginPage(webDriver, testResultService);
		registerAndLogin(userName);
		studentId = dbService.getUserIdByUserName(userName, institutionId);
		
		report.startStep("Check PLT Opens and Start Test -> and Then Exit PLT");
		webDriver.switchToNewWindow();
		pltTest = new NewUxClassicTestPage(webDriver, testResultService);
		pltTest.verifyTitlePLT();
		pltTest.selectLevelOnStartPLT(PLTStartLevel.Basic);
		pltTest.pressOnStartPLT();
		sleep(2);
		pltTest.exitDuringPLT(false);
		pageHelper.skipOnBoardingTour();
					
		report.startStep("Open Assessments");
		testsPage = homePage.openAssessmentsPage(false);
					
		report.startStep("Open Tests Results section");
		testsPage.clickOnArrowToOpenSection("3");
							
		report.startStep("Check Score in Test Results");
		testsPage.checkScoreLevelPLT("1", "A1"); // this inst define as CFER, A1= "First Discoveries"
		
		report.startStep("Check User Assigned to Class");
		testResultService.assertEquals(true, pageHelper.isUserAssignedToClass(studentId, classId));

		report.startStep("Check institution Remaining License reduced after login");
		if (remainingLicensesBeforeAddingUser!=null) {
			//pageHelper.checkRemainingLicenseBurnForallValidPackages(getValidInstitutionPackages,institutionId,false);
			report.startStep("Get institution Remaining License after adding new student");
			List<String[]> remainingLicensesAfterAddingUser = dbService.getRemainingLicensesOfCertainPackage(classPackages, institutionId);
			pageHelper.validateLicensesReducesAfterNewStudentAdded(remainingLicensesBeforeAddingUser, remainingLicensesAfterAddingUser);
		}
		report.startStep("LogOut of ED");
		testsPage.close();
		homePage.waitHomePageloaded();
		homePage.logOutOfED();
	}
	
	@Test
	@Category(SanityTests.class)
	//@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "" })
	public void testSelfRegistration_AddToClass_WithPLT_NewTE() throws Exception {
		report.startStep("Restart Browser with New URL");
		institutionName=institutionsName[1];
		pageHelper.initializeData();
		
		report.startStep("Prepare test data for Self-Registration");
		userName = "student" + dbService.sig(5);
		String className = configuration.getProperty("classname.CourseTest");
		String classId =  dbService.getClassIdByName(className,institutionId);

		report.startStep("Set Option to Add User to Class After Registration, and Open New PLT");
		pageHelper.enableSelfRegistrationAddToClassWithPLT(institutionId, classId);
		pageHelper.restartBrowserInNewURL(institutionName, true);
		pageHelper.clearLocalStorage();
		webDriver.deleteCookiesAndRefresh();

		report.startStep("Get institution Remaining License for all Valid Packages");
			List<String[]> getValidInstitutionPackages = dbService.getValidInstitutionPackages(institutionId);
			/*List<String[]> classPackages = dbService.getClassAssignPackagesNew(className, institutionId);
			List<String[]> remainingLicensesBeforeAddingUser = new ArrayList<>();
			if (classPackages!=null) {
				remainingLicensesBeforeAddingUser = dbService.getRemainingLicensesOfCertainPackage(classPackages, institutionId);
			}else {
				testResultService.addFailTest("ClassHasNoPackages",false,false);
			}*/

		report.startStep("Register and login");
		loginPage = new NewUXLoginPage(webDriver, testResultService);
		registerAndLoginNewTE(userName);
		studentId = dbService.getUserIdByUserName(userName, institutionId);

		if (studentId != null){
			String baseUrl = pageHelper.CILink.split(".com")[0] + ".com/";
			//dbService.insertUserToAutomationTable(institutionId, studentId, userName, CanvasClassName, baseUrl);
			dbService.insertUserToAutomationTable(institutionId,studentId,userName,"",baseUrl);
		}
			/*List<String[]> classAssignedPackages = dbService.getClassAssignPackagesNew(className,institutionId);
			if (getValidInstitutionPackages!=null && classAssignedPackages.size()>0)
				pageHelper.checkRemainingLicenseBurnForallValidPackages(getValidInstitutionPackages,institutionId,false);
			else
				pageHelper.checkRemainingLicenseBurnForallValidPackages(getValidInstitutionPackages,institutionId,true);*/

		TestEnvironmentPage testEnvironmentPage = new TestEnvironmentPage(webDriver, testResultService);
		sleep(1);

		report.startStep("Check PLT Opens and Start Test -> and Then Exit PLT");
		testEnvironmentPage.validateTestName(testTypes[0]);
		//sleep(3);
		
		report.startStep("Verify Level List & Languages & buttons");
		testEnvironmentPage.verifyLevelListExist();
		testEnvironmentPage.verifyLanguagesExists();
		testEnvironmentPage.verifyStartTestButtonsExists();
		
		report.startStep("Choose Level");
		String statusSymbol = testEnvironmentPage.choosePlacementStatusNewTe("Basic");
		
		report.startStep("Click on Start Test Button");
		testEnvironmentPage.pressOnStartTest();
		sleep(2);
		testEnvironmentPage.clickNext();
		testEnvironmentPage.waitForTestPageLoaded();
		report.startStep("Click Close PLT");
		testEnvironmentPage.clickClose();
		
		/*webDriver.switchToNewWindow();
		pltTest = new NewUxClassicTestPage(webDriver, testResultService);
		pltTest.verifyTitlePLT();
		pltTest.selectLevelOnStartPLT(PLTStartLevel.Basic);
		pltTest.pressOnStartPLT();
		sleep(2);
		pltTest.exitDuringPLT(false);
					
		report.startStep("Open Assessments");
		testsPage = homePage.openAssessmentsPage(false);
					
		report.startStep("Open Tests Results section");
		testsPage.clickOnArrowToOpenSection("3");
							
		report.startStep("Check Score in Test Results");
		testsPage.checkScoreLevelPLT("1", "First Discoveries");	*/
		report.startStep("Skip onboard guidelines");
			pageHelper.skipOnBoardingHP();

		report.startStep("Check institution Remaining License reduced after login");
		if (getValidInstitutionPackages!=null) {
			//List<String[]> getValidInstitutionPackages = dbService.getValidInstitutionPackages(institutionId);
			pageHelper.checkRemainingLicenseBurnForallValidPackages(getValidInstitutionPackages, institutionId, className,true);

			//List<String[]> remainingLicensesAfterAddingUser = dbService.getRemainingLicensesOfCertainPackage(classPackages, institutionId);
			//pageHelper.validateLicensesReducesAfterNewStudentAdded(remainingLicensesBeforeAddingUser, remainingLicensesAfterAddingUser);
		}
		report.startStep("Check User Assigned to Class");
			testResultService.assertEquals(true, pageHelper.isUserAssignedToClass(studentId, classId), "User:" +studentId + " not assigned to class:" + classId);

		report.startStep("Logout from ED");
			homePage = new NewUxHomePage(webDriver,testResultService);
			homePage.logOutOfED();
	}
	
	@Test
	//@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "" })
	public void testSelfRegistration_NoClass_WithPLT_NewTE() throws Exception {
		
		report.startStep("Restart Browser with New URL");
		institutionName=institutionsName[1];
		pageHelper.restartBrowserInNewURL(institutionName, true); 
		
		report.startStep("Prepare test data for Self-Registration");
		userName = "student" + dbService.sig(6);
						
		report.startStep("Set Option to Not Add User to Class After Registration, and Open PLT");
		pageHelper.enableSelfRegistrationNoAddToClassWithPLT(institutionId);
		pageHelper.clearLocalStorage();
		webDriver.deleteCookiesAndRefresh();
		
		report.startStep("Register and login");
		loginPage = new NewUXLoginPage(webDriver, testResultService);
		registerAndLoginNewTE(userName);
		studentId = dbService.getUserIdByUserName(userName, institutionId);

		if (studentId != null){
			String baseUrl = pageHelper.CILink.split(".com")[0] + ".com/";
			//dbService.insertUserToAutomationTable(institutionId, studentId, userName, CanvasClassName, baseUrl);
			dbService.insertUserToAutomationTable(institutionId,studentId,userName,"",baseUrl);
		}

		TestEnvironmentPage testEnvironmentPage = new TestEnvironmentPage(webDriver, testResultService);

		report.startStep("Check PLT Opens and Start Test -> and Then Exit PLT");
		testEnvironmentPage.validateTestName(testTypes[0]); 
	//	sleep(1);
		
		report.startStep("Verify Level List & Languages & buttons");
		testEnvironmentPage.verifyLevelListExist();
		testEnvironmentPage.verifyLanguagesExists();
		testEnvironmentPage.verifyStartTestButtonsExists();
		
		report.startStep("Choose Level");
		String statusSymbol = testEnvironmentPage.choosePlacementStatusNewTe("Basic");
		
		report.startStep("Click on Start Test Button");
		testEnvironmentPage.pressOnStartTest();
		sleep(2);
		testEnvironmentPage.clickNext();
				
		report.startStep("Click Close PLT");
		testEnvironmentPage.waitForTestPageLoaded();
		testEnvironmentPage.clickClose();
		pageHelper.skipOnBoardingHP();

		/*webDriver.switchToNewWindow();
		pltTest = new NewUxClassicTestPage(webDriver, testResultService);
		pltTest.verifyTitlePLT();
		pltTest.selectLevelOnStartPLT(PLTStartLevel.Basic);
		pltTest.pressOnStartPLT();
		sleep(2);
		pltTest.exitDuringPLT(false);
					
		report.startStep("Open Assessments");
		testsPage = homePage.openAssessmentsPage(false);
					
		report.startStep("Open Tests Results section");
		testsPage.clickOnArrowToOpenSection("3");
							
		report.startStep("Check Score in Test Results");
		testsPage.checkScoreLevelPLT("1", "First Discoveries");	*/
		
		report.startStep("Check User Not Assigned to Class");
		//studentId = dbService.getUserIdByUserName(userName, instID);
		pageHelper.checkUserNotAssignedToClasses(studentId);
	
		
	}

	@Test
	//@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "" })
	public void testSelfRegistration_NotDisplay() throws Exception {
		//report.startStep("Restart Browser with New URL");
		//institutionName = institutionsName[3];
		//pageHelper.initializeData();
		//pageHelper.restartBrowserInNewURL(institutionName, true);

		/*pageHelper.initializeData();
		String url = webDriver.getUrl();
		url = url.split(".com")[0] + ".com/" + BasicNewUxTest.institutionName;

		webDriver.quitBrowser();
		webDriver.init();
		webDriver.maximize();
		webDriver.openUrl(url);*/

		loginPage = new NewUXLoginPage(webDriver, testResultService);
		loginPage.checkRegisterLinkNotDisplayed();
	}

/*
//--igb 2018.06.10--------------------------------------	
	@Test
	@TestCaseParams(testCaseID = { "49559" })
	public void testLaureateOldPLT_ExitsUser() throws Exception {
		report.startStep("Init Test Data");
		
		String baseUrl = pageHelper.CILink.split(".com")[0] + ".com/";
		String filePath = pageHelper.buildPathForExternalPages + "Languages/";
		String accessFile = pageHelper.urlForExternalPages.split(".com")[0] + ".com/" + "Languages/";
		String testFile = "PLT_Post_" + dbService.sig(4) + ".html";
			
		String[] user = getGeneralUser();
		String chkUser =  user[0];
				
		String chkLang = "English";
		String chkToken = dbService.getApiToken(institutionId);
		
		LaureatePLT = true;

		createViewPLTfile(filePath, "viewPLT.html");
		String chkReURL = baseUrl + folderPath + "viewPLT.html?";
		
		report.startStep("Test LaureatePLT exist user");
		createTestPLTfile(filePath, testFile, baseUrl, institutionName, chkToken, chkUser, chkLang, chkReURL);
		
		report.startStep("Change to New URL");
		pageHelper.clearLocalStorage();
		webDriver.deleteCookiesAndRefresh();
		webDriver.openUrl(accessFile + testFile);
		sleep(1);
		
		report.startStep("Click Submit");
		selfRegistrationPage = new SelfRegistrationPage(webDriver, testResultService);
		selfRegistrationPage.clickSubmit();
		sleep(2);
		
		report.startStep("Check PLT opens (verify PLT title)");
		checkOldPltOpensAndClose();

		//pltTest = new NewUxClassicTestPage(webDriver, testResultService);
		//pltTest.verifyTitlePLT();
		sleep(1);
		
		report.startStep("Close browser");
		webDriver.closeBrowser();
	}
*/

/*
	@Test
	@TestCaseParams(testCaseID = { "49549" })
	public void testLaureateOldPLT_NewUser() throws Exception {
		
		report.startStep("Init Test Data");
		String baseUrl = pageHelper.CILink.split(".com")[0] + ".com/";
		String filePath = pageHelper.buildPathForExternalPages + "Languages/";
		String accessFile = pageHelper.urlForExternalPages.split(".com")[0] + ".com/" + "Languages/";
		String testFile = "PLT_Post_" + dbService.sig(4) + ".html";
			
		//String[] user = createUserAndReturnDetails(institutionId, configuration.getClassName());
		String chkUser =  "stud-plt" + dbService.sig(4); //user[0];
				
		String chkLang = "English";
		String chkToken = dbService.getApiToken(institutionId);

		createViewPLTfile(filePath, "viewPLT.html");
		String chkReURL = accessFile + "viewPLT.html?";
		
		report.startStep("Test LaureatePLT exist user");
		createTestPLTfile(filePath, testFile, baseUrl, institutionName, chkToken, chkUser, chkLang, chkReURL);
		
		report.startStep("Change to New URL");
		pageHelper.clearLocalStorage();
		webDriver.deleteCookiesAndRefresh();
		webDriver.openUrl(accessFile + testFile);
		sleep(1);
		
		report.startStep("Click Submit");
		selfRegistrationPage = new SelfRegistrationPage(webDriver, testResultService);
		selfRegistrationPage.clickSubmit();
		sleep(1);

		report.startStep("Check PLT opens (verify PLT title)");
		checkOldPltOpensAndClose();
		sleep(1);

		report.startStep("Close browser");
		webDriver.closeBrowser();
	}
*/
	/*
	private void checkOldPltOpensAndClose() throws Exception {
		//report.startStep("Check PLT opens (verify PLT title)");
		pltTest = new NewUxClassicTestPage(webDriver, testResultService);
		pltTest.verifyTitlePLT();
		pltTest.selectLevelOnStartPLT(PLTStartLevel.Basic);
		pltTest.pressOnStartPLT();
		webDriver.switchToPopup();
		pltTest.waitForTestPageLoaded();
		webDriver.waitForElement("//div[@id='exitCell']//td[3]", ByTypes.xpath).click();
		webDriver.switchToPopup();
		webDriver.waitForElement("submitBtn", ByTypes.className).click();
		webDriver.switchToMainWindow();
		pltTest.clickOnExitPLT();
		//pltTest.exitDuringPLT(false);

		//pltTest.selectLevelOnStartPLT(PLTStartLevel.Basic);
		//pltTest.pressOnStartPLT();
		//pltTest.startPltAndExit();
		//pltTest.waitForTestPageLoaded();
		//pltTest.clickExitPltAndClosePopUp();
		sleep(1);
	}
*/


	private void testWrongParamPLT(String chkPath, String testFile, String baseUrl,
						String chkInst, String chkToken, String chkUser, String chkLang,
			            String chkReURL, String errType,String accessFile) throws Exception {
		
		String  chkErrState = "";
		switch(errType) {
			case "emptyInst":
				chkInst = ""; 
				chkErrState = "err=1";
				break;
			case "errInst":
				chkInst = "xxx1"; 
				chkErrState = "err=1";
				break;
			case "emptyToken":
				chkToken = ""; 
				chkErrState = "err=2";
				break;
			case "errToken":
				chkToken += "A"; 
				chkErrState = "err=2";
				break;
			case "emptyUser":
				chkUser = ""; 
				chkErrState = "err=3";
				break;
		}
		
		createTestPLTfile(chkPath, testFile, baseUrl, chkInst, chkToken, chkUser, chkLang, chkReURL);
	
		
		webDriver.closeBrowser();
		//webDriver.openIncognitoChromeWindow();
		webDriver.init();
		webDriver.openUrl(accessFile + testFile);
		webDriver.waitUntilElementAppearsAndReturnElementByType("btnSubmit", ByTypes.name , 10);
			
		report.startStep("Click Submit");
		selfRegistrationPage = new SelfRegistrationPage(webDriver, testResultService);
		selfRegistrationPage.clickSubmit();
		sleep(2);
		
		selfRegistrationPage.checkErrorMessage(chkErrState,testFile);	
	}
	
	private void createViewPLTfile(String chkPath, String viewFile) throws Exception
	{
		List<String> wList = new ArrayList<String>();
		
		wList = createHtmlTitle(wList, true);
		wList = createHtmlTail(wList);
	
		textService.writeListToSmbFile(chkPath + viewFile, wList, netService.getDomainAuth());
	}
	
	private void createTestPLTfile(String chkPath, String testFile, String baseUrl,
		            String chkInst, String chkToken, String chkUser, String chkLang,
		            String chkReURL) throws Exception
	{
		List<String> wList = new ArrayList<String>();
	
		wList = createHtmlTitle(wList, false);
		wList = createFormInfo(wList, baseUrl, chkInst, chkToken, chkUser, chkLang, chkReURL);
		wList = createHtmlTail(wList);
		
		textService.writeListToSmbFile(chkPath+testFile, wList, netService.getDomainAuth());
	}
	
	private List<String> createHtmlTitle(List<String> wList, boolean bJsScript)
	{
		wList.add("<!DOCTYPE html>");
		wList.add("<html>");
		wList.add("<head>");
		wList.add("\t<meta charset=\"utf-8\" />");
		wList.add("\t<title></title>");
		
		if(bJsScript) { wList = createJsScript(wList); }
			
		wList.add("</head>");
		wList.add("<body>");
		
		if(bJsScript) { 
		   wList.add("<span id=\"user\"></span>");
		   wList.add("<br/><br/>");
		   wList.add("<span id=\"err\"></span>");
		}
		
		return  wList; 
	}
	
	private List<String> createJsScript(List<String> wList)
	{
		wList.add("<script type=\"text/javascript\" src=\"/Runtime/JavaScript/jquery-1.8.2.min.js\"></script>");
		wList.add("<script type=\"text/javascript\">");
 		wList.add("\t$(document).ready(function () \n\t { $('#user').text(\"user=\" + qs('Username')); \n\t  $('#err').text(\"err=\" + qs('error')); \n\t });");

 		wList.add("\n\t function qs(key) {");
 		wList.add("\n\t key = key.replace(/[*+?^$.\\[\\]{}()|\\\\/]/g, \"\\$&\");"); // escape RegEx meta chars
 		wList.add("\n\t var match = location.search.match(new RegExp(\"[?&]\" + key + \"=([^&]+)(&|$)\"));");
 		wList.add("\n\t return match && decodeURIComponent(match[1].replace(/\\+/g, \" \"));");
 		wList.add("\n\t }");
 		
		wList.add("</script>");

		return  wList; 
	}
	
	private List<String> createHtmlTail(List<String> wList)
	{
		wList.add("</body>");
		wList.add("</html>");
		
		return  wList; 
	}
	
	private List<String> createFormInfo(List<String> wList, String chkURL, String chkInst,
						 String chkToken, String chkUser, String chkLang, String chkReURL)
	{
		wList.add("\t<form method=\"post\" action=\"" + chkURL + "PlacementTestEntry.aspx\">");
		wList.add("\t\t<input type=\"hidden\" name=\"IName\" value=\"" + chkInst + "\" />");
		wList.add("\t\t<input type=\"hidden\" name=\"Token\" value=\"" + chkToken + "\" />");
		wList.add("\t\t<input type=\"hidden\" name=\"UserName\" value=\"" + chkUser + "\" />");
		wList.add("\t\t<input type=\"hidden\" name=\"Language\" value=\"" + chkLang + "\" />");
		wList.add("\t\t<input type=\"hidden\" name=\"ReturnUrl\" value=\"" + chkReURL + "\" />");
		wList.add("\t\t<input type=\"submit\" name=\"btnSubmit\" />");
		wList.add("\t</form>");
		
		return  wList; 
	}
//--igb 2018.06.10----------------	
	
	private void registerAndLogin(String userName) throws Exception {
		
		report.startStep("Open Self-Registration page");
		register = loginPage.openSelfRegistrationPage();
		
		report.startStep("Verify Title and Labels");
		register.verifyFormTitle();
		register.verifyFormFieldsLabels();
		
		report.startStep("Enter valid values into all required fields");
		String userDetails [] = register.fillRegisterForm(userName);
		
		report.startStep("Click Register");
		register.clickOnRegister();
		
		report.startStep("Login and check First Name");
		webDriver.switchToMainWindow();
		webDriver.waitForElementAndClick("optInPrivacyStatement__checkBoxW", ByTypes.className);
		webDriver.waitForElementAndClick("optInPrivacyStatement__continue", ByTypes.id);
		webDriver.switchToMainWindow();
		testResultService.assertEquals("Hello " + userDetails[1], homePage.getUserDataText(), "Verify First Name in Header of Home Page");
	
		
	}
	
	public boolean checkIfPltOpened(){
		boolean pltOpened = false;
		try {
			webDriver.switchToNewWindow();
			pltOpened = true;
		} catch (Exception e) { 
			pltOpened = false;
		}
		return pltOpened;
	}
	
	private void registerAndLoginNewTE(String userName) throws Exception {
		
		report.startStep("Open Self-Registration page");
		register = loginPage.openSelfRegistrationPage();
		
		report.startStep("Verify Title and Labels");
		register.verifyFormTitle();
		register.verifyFormFieldsLabels();
		
		report.startStep("Enter valid values into all required fields");
		String userDetails [] = register.fillRegisterForm(userName);
		
		report.startStep("Click Register");
		register.clickOnRegister();
		
		report.startStep("Login and check First Name");
		webDriver.switchToMainWindow();
		webDriver.waitForElementAndClick("optInPrivacyStatement__checkBoxW", ByTypes.className);
		webDriver.waitForElementAndClick("optInPrivacyStatement__continue", ByTypes.id);
		//testResultService.assertEquals("Hello " + userDetails[1], homePage.getUserDataText(), "Verify First Name in Header of Home Page");

		homePage.closeAllNotifications();
	}
				
	@After
	public void tearDown() throws Exception {
		
		institutionName="";
		institutionId="";
		/*
		if(!LaureatePLT)
		{
			report.startStep("Restore default Self-registration settings");
			pageHelper.disableSelfRegistration(institutionId);
			studentService.deleteStudent(userName, institutionId);
			
			super.tearDown();
		}
		*/
		super.tearDown();
	}
}

