package tests.tms;


import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Enums.ByTypes;
import Enums.PLTStartLevel;
import Interfaces.TestCaseParams;
import pageObjects.edo.NewUXLoginPage;
import pageObjects.edo.NewUxAssessmentsPage;
import pageObjects.edo.NewUxClassicTestPage;
import pageObjects.edo.NewUxInstitutionPage;
import pageObjects.edo.NewUxLearningArea;
import pageObjects.edo.NewUxMyProfile;
import pageObjects.edo.NewUxCommunityPage;
import pageObjects.edo.NewUxSelfRegistrationPage;
import pageObjects.tms.DashboardPage;
import pageObjects.tms.SelfRegistrationPage;
//import pageObjects.tms.TmsHomePage;
import tests.edo.newux.BasicNewUxTest;
import tests.edo.newux.SpeechRecognitionBasicTestNewUX;


public class NewUxSelfRegistrationTests extends SpeechRecognitionBasicTestNewUX {

	boolean LaureatePLT = false;  // igb 2018.06.06
	
	NewUXLoginPage loginPage;
	DashboardPage dashboardPage;
	//TmsHomePage tmsHomePage;
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
	
//	@Before
	public void setup() throws Exception {
		super.setup();
	}
	
//	@Test
//	@TestCaseParams(testCaseID = { "33692" })
	public void testSelfRegistrationEntryCriteria_NoClass_NoPlt() throws Exception {

		institutionName=institutionsName[3];
		pageHelper.initializeData();
		
		String url = webDriver.getUrl();
		url = url.split(".com")[0] + ".com/" + BasicNewUxTest.institutionName;
		
		webDriver.quitBrowser();
		webDriver.init();
		webDriver.maximize();
		webDriver.openUrl(url);
		
		report.startStep("Prepare test data for Self-Registration");
		userName = "student" + dbService.sig(6);
		//instID = localInstId; //configuration.getLocalInstitutionId();
				
		report.startStep("Check no Register link displayed");
		loginPage = new NewUXLoginPage(webDriver, testResultService);
		loginPage.checkRegisterLinkNotDisplayed();
		
		report.startStep("Set option to automattically add user to class after registration");
		pageHelper.enableSelfRegistrationNoAddToClassNoPLT(institutionId);
		webDriver.deleteCookiesAndRefresh();
		
		report.startStep("Register and login");
		registerAndLogin(userName);
		studentId = dbService.getUserIdByUserName(userName, institutionId);
								
		report.startStep("Check user not assigned to class");
		studentId = dbService.getUserIdByUserName(userName, institutionId);
		pageHelper.checkUserNotAssignedToClasses(studentId);
		
		// validate plt doesnt open (try-catch- switch to popup)
		//
		
	}
	
//	@Test
//	@TestCaseParams(testCaseID = { "33653" })
	public void testSelfRegistrationAddUserToClass_NoPlt() throws Exception {
		
		institutionName=institutionsName[3];
		pageHelper.initializeData();
		
		String url = webDriver.getUrl();
		url = url.split(".com")[0] + ".com/" + BasicNewUxTest.institutionName;
		
		webDriver.quitBrowser();
		webDriver.init();
		webDriver.maximize();
		webDriver.openUrl(url);
		
		report.startStep("Prepare test data for Self-Registration");
		userName = "student" + dbService.sig(6);
		//instID = localInstId;//configuration.getLocalInstitutionId();
		
		String classId =  dbService.getClassIdByName(configuration.getLocalclassName(), institutionId);
				
		report.startStep("Set option to automattically add user to class after registration");
		pageHelper.enableSelfRegistrationWithAddToClassNoPLT(institutionId, classId);
		webDriver.deleteCookiesAndRefresh();
						
		report.startStep("Register and login");
		loginPage = new NewUXLoginPage(webDriver, testResultService);
		registerAndLogin(userName);
		studentId = dbService.getUserIdByUserName(userName, institutionId);
		
		report.startStep("Check user assigned to class");
		//studentId = dbService.getUserIdByUserName(userName, instID);
		testResultService.assertEquals(true, pageHelper.isUserAssignedToClass(studentId, classId));
		
		// validate plt does not open
		//
		
	}
	
//	@Test
//	@TestCaseParams(testCaseID = { "33654" })
	public void testSelfRegistration_NoClass_WithPLT() throws Exception {

		institutionName=institutionsName[3];
		pageHelper.initializeData();
		
		String url = webDriver.getUrl();
		url = url.split(".com")[0] + ".com/" + BasicNewUxTest.institutionName;
		
		webDriver.quitBrowser();
		webDriver.init();
		webDriver.maximize();
		webDriver.openUrl(url);
		
		report.startStep("Prepare test data for Self-Registration");
		userName = "student" + dbService.sig(6);
		//instID = localInstId; //configuration.getLocalInstitutionId();
						
		report.startStep("Set option to automattically add user to class after registration");
		pageHelper.enableSelfRegistrationNoAddToClassWithPLT(institutionId);
		webDriver.deleteCookiesAndRefresh();
		
		report.startStep("Register and login");
		loginPage = new NewUXLoginPage(webDriver, testResultService);
		registerAndLogin(userName);
		studentId = dbService.getUserIdByUserName(userName, institutionId);
		
		report.startStep("Check PLT opens and Start Test");
		webDriver.switchToNewWindow();
		pltTest = new NewUxClassicTestPage(webDriver, testResultService);
		pltTest.verifyTitlePLT();
		pltTest.selectLevelOnStartPLT(PLTStartLevel.Basic);
		pltTest.pressOnStartPLT();
		sleep(2);
		pltTest.exitDuringPLT(false);
					
		report.startStep("Open Assessments");
		testsPage = homePage.openAssessmentsPage(false);
		//sleep(3);
					
		report.startStep("Open Tests Results section");
		testsPage.clickOnArrowToOpenSection("3");
							
		report.startStep("Check Score in Test Results");
		testsPage.checkScoreLevelPLT("1", "First Discoveries");	
		
		// validate user not added to class
		// debug
		report.startStep("Check user assigned to class");
		studentId = dbService.getUserIdByUserName(userName, institutionId);
		pageHelper.checkUserNotAssignedToClasses(studentId);
	}
	
	// public void testSelfRegistration_AddToClass_WithPLT() old te
	// move all (new+old) to ed-selfRegistration
	// new te- courses
	// new te- with plt not class, with plt with class
	

//--igb 2018.06.10--------------------------------------	
//	@Test
//	@TestCaseParams(testCaseID = { "49559" })
	public void testLaureatePLT_OldUser() throws Exception {
		report.startStep("Init Test Data");
		String baseUrl = pageHelper.CILink.split(".com")[0] + ".com/";
		
		String accessUrl = pageHelper.urlForExternalPages.split(".com")[0] + ".com/Languages/";
		String phisicalPath = pageHelper.buildPathForExternalPages + "Languages/";
		
		String[] user = getUserNamePassId(institutionId, configuration.getClassName());
		String chkUser = user[0];
		String chkLang = "English";
		String chkToken = dbService.getApiToken(institutionId);
		String testFile = "TestPlt_" + dbService.sig(5) + ".html";
		LaureatePLT = true;

		createViewPLTfile(phisicalPath, "viewPLT.html");
		String chkReURL = baseUrl + "Languages/" + "viewPLT.html?";
		
		report.startStep("Test LaureatePLT not exist user");
		report.startStep("Change to New URL");
		createTestPLTfile(phisicalPath , testFile, baseUrl, institutionName, chkToken, chkUser, chkLang, chkReURL);
		String chkURL = accessUrl + testFile;
		
		webDriver.openUrl(chkURL);
		sleep(2);
		
		report.startStep("Click Submit");
		selfRegistrationPage = new SelfRegistrationPage(webDriver, testResultService);
		selfRegistrationPage.clickSubmit();
		sleep(2);
		
		report.startStep("Check PLT opens (verify PLT title)");
		pltTest = new NewUxClassicTestPage(webDriver, testResultService);
		pltTest.verifyTitlePLT();
		sleep(2);
		
		report.startStep("Close browser");
		webDriver.closeBrowser();
	}

//	@Test
//	@TestCaseParams(testCaseID = { "49549" })
	public void testLaureatePLT_NewUser() throws Exception {
		
		report.startStep("Init Test Data");
		String baseUrl = pageHelper.CILink.split(".com")[0] + ".com/";
		
		String accessUrl = pageHelper.urlForExternalPages.split(".com")[0] + ".com/Languages/";
		String phisicalPath = pageHelper.buildPathForExternalPages + "Languages/";
		
		String chkUser = "stud" + dbService.sig(4);
		String chkLang = "English";
		String chkToken = dbService.getApiToken(institutionId);
		String testFile = "TestPlt_" + dbService.sig(5) + ".html";
		LaureatePLT = true;

		createViewPLTfile(phisicalPath, "viewPLT.html");
		String chkReURL = baseUrl + "Languages/" + "viewPLT.html?";
		
		report.startStep("Test LaureatePLT not exist user");
		report.startStep("Change to New URL");
		createTestPLTfile(phisicalPath , testFile, baseUrl, institutionName, chkToken, chkUser, chkLang, chkReURL);
		String chkURL = accessUrl + testFile;
		
		webDriver.openUrl(chkURL);
		sleep(2);
		
		report.startStep("Click Submit");
		selfRegistrationPage = new SelfRegistrationPage(webDriver, testResultService);
		selfRegistrationPage.clickSubmit();
		sleep(2);
		
		report.startStep("Check PLT opens (verify PLT title)");
		pltTest = new NewUxClassicTestPage(webDriver, testResultService);
		pltTest.verifyTitlePLT();
		sleep(2);
		
		report.startStep("Close browser");
		webDriver.closeBrowser();
	}
	
//	@Test
//	@TestCaseParams(testCaseID = { "49552" })
	public void testLaureatePLTWrongParam() throws Exception {
		String baseUrl = pageHelper.CILink.split(".com")[0] + ".com/";
		String chkPath = pageHelper.buildPath;
		String chkInst = configuration.getProperty("institution.name", "automation");
		String chkUser = "stud" + dbService.sig(8);
		String chkLang = "English";
		String instID = dbService.getInstituteIdByName(chkInst);
		String chkToken = dbService.getApiToken(instID);
		LaureatePLT = true;
		
		createViewPLTfile(chkPath + folderPath, "viewPLT.html");
		String chkReURL = baseUrl + folderPath + "viewPLT.html?";
		
		report.startStep("Test empty Institution param");
		testWrongParamPLT(chkPath, "TestPlt01.html", baseUrl, chkInst, chkToken, chkUser, chkLang, chkReURL, "emptyInst");

		report.startStep("Test not exist Institution param");
		testWrongParamPLT(chkPath, "TestPlt02.html", baseUrl, chkInst, chkToken, chkUser, chkLang, chkReURL, "errInst");

		report.startStep("Test empty Token param");
		testWrongParamPLT(chkPath, "TestPlt03.html", baseUrl, chkInst, chkToken, chkUser, chkLang, chkReURL, "emptyToken");

		report.startStep("Test error Token param");
		testWrongParamPLT(chkPath, "TestPlt04.html", baseUrl, chkInst, chkToken, chkUser, chkLang, chkReURL, "errToken");

		report.startStep("Test empty User param");
		testWrongParamPLT(chkPath, "TestPlt05.html", baseUrl, chkInst, chkToken, chkUser, chkLang, chkReURL, "emptyUser");

		report.startStep("Close browser");
		webDriver.closeBrowser();
	}

	private void testWrongParamPLT(String chkPath, String testFile, String baseUrl,
						String chkInst, String chkToken, String chkUser, String chkLang,
			            String chkReURL, String errType) throws Exception {
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
		
		createTestPLTfile(chkPath + folderPath, testFile, baseUrl, chkInst, chkToken, chkUser, chkLang, chkReURL);
		
		String chkURL = baseUrl + folderPath + testFile;
		
		webDriver.openUrl(chkURL);
		sleep(5);
			
		report.startStep("Click Submit and Verify the error code result of test file: " +testFile);
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
		
		textService.writeListToSmbFile(chkPath + testFile, wList, netService.getDomainAuth());
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
		
		// add phone num
		//
		
		report.startStep("Click Register");
		register.clickOnRegister();
		
		report.startStep("Login and check First Name");
		webDriver.switchToMainWindow();
		webDriver.waitForElementAndClick("optInPrivacyStatement__checkBoxW", ByTypes.className);
		webDriver.waitForElementAndClick("optInPrivacyStatement__continue", ByTypes.id);
		testResultService.assertEquals("Hello " + userDetails[1], homePage.getUserDataText(), "Verify First Name in Header of Home Page");
	
		
	}
				
//	@After
	public void tearDown() throws Exception {
		
		institutionName="";
		institutionId= "";
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
