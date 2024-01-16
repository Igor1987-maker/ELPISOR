package tests.edo.newux;

import Enums.ByTypes;
import Enums.UserType;
import Interfaces.TestCaseParams;
import org.imsglobal.lti.launch.LtiOauthSigner;
import org.imsglobal.lti.launch.LtiSigningException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.WebElement;
import pageObjects.edo.*;
import pageObjects.tms.CurriculumAssignPackagesPage;
import pageObjects.tms.TmsHomePage;
import services.PageHelperService;
import testCategories.edoNewUX.LoginPage;
import testCategories.edoNewUX.SanityTests;
import testCategories.inProgressTests;

import java.io.File;
import java.util.*;

//@Category(AngularLearningArea.class)
public class ExternalLoginAndIntegration extends BasicNewUxTest {

	NewUXLoginPage loginPage;
	NewUxLearningArea learningArea;
	NewUxLearningArea2 learningArea2;
	NewUxAssessmentsPage testsPage;
	NewUxClassicTestPage classicTest;
	NewUxMyProgressPage myProgress;
	NewUxMyProfile myProfile;
	TmsHomePage tmsHome;

	private static final String Cannot_Take_PLT_Alert = "You cannot take the placement test at this time. If you would like to be assigned a placement test, please contact your program coordinator.";
	private static final String Language = "English";
	// private static final String Language = "Portuguese";
	
	private boolean assignCoursesToClassWhenTestEnds = true;
	
	private static String CanvasClassName;// = "DefaultCanvasClass";
	private static String CanvasClassId;// = "DefaultCanvasClass_Id";
	
	private static final String GENERAL_BANNER_PATH = "Images/General/skins/ed/defaultInstitutionHeader.jpg";

	private static int amountClasses = 0;

	private final String extLoginIncorrectUserName = "Your user name or password is incorrect. Please try again.";

	private List<String[]> baseLineLicense = new ArrayList<>();
	private List<String[]> currentLicense = new ArrayList<>();
	private List<String[]> classPackages = new ArrayList<>();

	String classNameAR = "undefined";
	String userFN = "FN";
	String userLN = "LN";
	String studentUserName = "";
	String role = "Learner";
	String password = "";
	boolean useNameUserWithMinus = false;
	boolean multipleClasses = false;
	int classAssignmnetAmount = 1;
	public static boolean useMapTable = false;
	public static boolean useSecurityCheck = false;
	public static boolean assignPackages = false;
//	public static boolean useUserMapTable = false;
	boolean useAssignedClassToUser = false;
	public static boolean canvasTest = false;
	public String randomUserAddress = "";
	List<String> courseUnits = null;
	boolean useNewClass = false;
	boolean outComeAndSourceId = false;
	boolean useUserAddress = false;
	boolean newTeacher = false;
	boolean newStudent = false;
	boolean student = false;
	boolean teacher = false;
	public static String className = "";
	public static UserType userType = UserType.Student;
	boolean useToken = false;
	String email = "";
	String regUserUrl;
	boolean oldTE = false;
	boolean useLti = false;
	boolean useSMB = false;

	String firstNameKey = "lis_person_name_given";
	String lastNameKey = "lis_person_name_family";
	String userNameKey = "custom_canvas_user_id";
	String mailKey = "lis_person_contact_email_primary";
	String classNameKey = "custom_user_sections";
	Map<String,String> signature = null;

	@Before
	public void setup() throws Exception {
		institutionName = institutionsName[2];
		super.setup();

		loginPage = new NewUXLoginPage(webDriver, testResultService);
		
	}

//--igb 2018.11.25 --> add new Test ------------------------------------------------	
	@Test
	@TestCaseParams(testCaseID = { "53191" }, testTimeOut = "20")
	public void testRegLoginApostrUserNameByGET() throws Exception {

		report.startStep("Generate student username for registration");
		// newStudent=true;
		String studentUserName = "stud" + dbService.sig(5);

		try {
			report.startStep("Enter ED via new RegAndLogin API - Insert New User");
			regLoginApostrUser(institutionId, studentUserName);

		} catch (Exception e) {
			testResultService.addFailTest(e.toString(), false, true);

		} finally {

			report.startStep("Close browser");
			webDriver.closeBrowser();
		}
	}
	

	private void regLoginApostrUser(String instID, String userName) throws Exception {

		String instName = institutionName;
		userFN = "T\'o\'m";
		userLN = "Sm\'i\'th";
		email = userName + "r-r" + "-@edusoft.co.il";
		char createClass = 'Y';
		char userTypeParam = 'S';

		String suffix = " " + dbService.sig(2);
		userFN = "FN " + userFN + suffix;
		userLN = "LN " + userLN + suffix;

		String baseUrl = pageHelper.CILink.split(".com")[0] + ".com/";
		String regInsertUrl = baseUrl + "RegUserAndLogin.aspx?Action=I&UserName=" + userName + "&Inst=" + instName
				+ "&FirstName=" + userFN + "&LastName=" + userLN + "&Password=12345&Email=" + email + "&Class="
				+ CanvasClassName + "&Language=" + Language + "&Link=" + CorpUrl + "&UseNameMapping=N&CreateClass="
				+ createClass + "&UserType=" + userTypeParam;

		webDriver.openUrl(regInsertUrl);
		pageHelper.skipOptin();

		studentId = dbService.getUserIdByUserName(userName, institutionId);

		if (studentId != null) {
			dbService.insertUserToAutomationTable(institutionId, studentId, userName, CanvasClassName, baseUrl);
		}

		homePage.waitHomePageloaded();
		try {

			homePage.clickOnMyProfile();
			homePage.switchToMyProfile();
			NewUxMyProfile myProfile = new NewUxMyProfile(webDriver, testResultService);

			testResultService.assertEquals(userFN, myProfile.getFirstName(), "First Name in My Profile is Incorrect.");
			testResultService.assertEquals(userLN, myProfile.getLastName(), "Last Name in My Profile is Incorrect.");
			testResultService.assertEquals(email, myProfile.getMail(), "Mail in My Profile is Incorrect.");
			testResultService.assertEquals(userName, myProfile.getUserName(), "User name in My Profile is Incorrect.");

			report.startStep("Validate User's Class data stored in DB correctly");
			testResultService.assertEquals(true, pageHelper.isUserAssignedToClass(studentId,
					dbService.getClassIdByName(CanvasClassName, institutionId)), "User not assigned to class");

			webDriver.switchToMainWindow();
			webDriver.switchToTopMostFrame();

			testResultService.assertEquals("Hello " + userFN, homePage.getUserDataText(),
					"Verify First Name in Header of Home Page");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
//--igb 2018.11.25 --> add new Test ------------------------------------------------	

	@Test
	@TestCaseParams(testCaseID = { "43954" }, testTimeOut = "20")
	public void testRegLoginNewStudentCreateClassByGET() throws Exception {

		className="";
		institutionName = institutionsName[5];//5
		pageHelper.initializeData();
		pageHelper.closeBrowserAndOpenInCognito(false);
		
		newStudent = true;
		useNewClass = true;
		useMapTable = false;
		useLti = false;

		report.startStep("Generate student username for registration");
		// String studentUserName = "stud" + dbService.sig(5);

		try {
			report.startStep("Enter ED via new RegAndLogin API - Insert New User");
			regLogin(institutionId, UserType.Student);

			report.startStep("Check User Name on Home Page after Login");
			testResultService.assertEquals(true, homePage.getUserDataText().contains(userName),
					"User Name not found on home page");

			report.startStep("Check user assigned to new class created");
			testResultService.assertEquals(true,
					pageHelper.isUserAssignedToClass(studentId, dbService.getClassIdByName(className, institutionId)),
					"Class not created or user not assigned to class");
			
			report.startStep("Check Client type and Operating system");
			testResultService.assertEquals(true, dbService.verifyClientTypeAndOperatingSystemOfProgress(studentId));

		} catch (Exception e) {
			testResultService.addFailTest(e.toString(), false, true);

		} finally {
			// pageHelper.deleteStudentsAndClass(UMMInstId, CanvasClassName);
			assignCoursesToClassWhenTestEnds = false;
			className="";
		}
		
	}

	@Test
	@TestCaseParams(testCaseID = { "43954" }, testTimeOut = "20")
	public void testRegLoginExistingStudentExsitingClassByGET() throws Exception {

		institutionName = institutionsName[0];//0
		pageHelper.initializeData();
		pageHelper.closeBrowserAndOpenInCognito(false);

		useAssignedClassToUser = true; // colman

		try {
			report.startStep("Enter ED via new RegAndLogin API - Insert New User");
			regLogin(institutionId, UserType.Student);

			report.startStep("Check User Name on Home Page after Login");
			testResultService.assertEquals(true, homePage.getUserDataText().contains(userName),
					"User Name not found on home page");
			
			report.startStep("Check user assigned to new class created");
			testResultService.assertEquals(true,
					pageHelper.isUserAssignedToClass(studentId, dbService.getClassIdByName(className, institutionId)),
					"Class not created or user not assigned to class");

			report.startStep("Check home page loaded with the correct assignment");
			homePage.waitHomePageloadedFully();
			
		} catch (Exception e) {
			testResultService.addFailTest(e.toString(), false, true);

		} finally {
			assignCoursesToClassWhenTestEnds = false;
		}
	}
	
	@Test
	@TestCaseParams(testCaseID = { "43954" }, testTimeOut = "20")
	public void testRegLoginTheSameUserAfterLogoutByGET() throws Exception {

		institutionName = institutionsName[0];
		pageHelper.initializeData();
		pageHelper.closeBrowserAndOpenInCognito(false);

		useAssignedClassToUser = true; // colman

		try {
			report.startStep("Enter ED via new RegAndLogin API - Insert New User");
			regLogin(institutionId, UserType.Student);

			verifyUserLoggedInCorrectlyAndHomePagedisplay();
			
			homePage.clickOnLogOut();
			
			webDriver.closeBrowser();
			webDriver.init();
			webDriver.maximize();
			
			webDriver.openUrl(regUserUrl);
			
			verifyUserLoggedInCorrectlyAndHomePagedisplay();
			
			
		} catch (Exception e) {
			testResultService.addFailTest(e.toString(), false, true);

		} finally {
			assignCoursesToClassWhenTestEnds = false;
		}
	}
	
	
	@Test
	@TestCaseParams(testCaseID = { "43954" }, testTimeOut = "20")
	public void testRegLoginTheSameUserAfterCloseBrowserByGET() throws Exception {

		institutionName = institutionsName[0];
		pageHelper.initializeData();
		pageHelper.closeBrowserAndOpenInCognito(false);

		useAssignedClassToUser = true; // colman

		try {
			report.startStep("Enter ED via new RegAndLogin API - Insert New User");
			regLogin(institutionId, UserType.Student);

			verifyUserLoggedInCorrectlyAndHomePagedisplay();
			
			webDriver.closeBrowser();
			webDriver.init();
			webDriver.maximize();
			
			webDriver.openUrl(regUserUrl);
			sleep(2);
			
		//	if (PageHelperService.branchCI.contains("main"))
				loginPage.verifyAndConfirmAlertMessage();
		//	else {
		//		verifyAndWaitWhileDelayedLoginProgressBar();
		//		sleep(2);	
		//	}
			verifyUserLoggedInCorrectlyAndHomePagedisplay();
			
			
		} catch (Exception e) {
			testResultService.addFailTest(e.toString(), false, true);

		} finally {
			assignCoursesToClassWhenTestEnds = false;
		}
	}
	
	@Test
	@Category(LoginPage.class)
	@TestCaseParams(testCaseID = { "30746","30745" } )
	public void testExtLoginStudentAPIGet() throws Exception {

		institutionName = institutionsName[0];
		pageHelper.initializeData();
		pageHelper.closeBrowserAndOpenInCognito(false);
		
		String[] studentd=null;
        studentd = pageHelper.getStudentsByInstitutionId(institutionId);
		sleep(1);
		useMapTable = false;
		useNewClass = false;
		userType = UserType.Student;
		
        report.startStep("Generate parameters for registration");
	        studentId = studentd[6];
	        userName = studentd[0];
			String pass = studentd[3];
			userFN = studentd[1]; 
			userLN = studentd[2];
			email = studentd[5];
			String refUrl = NewUxHomePage.CorpUrl; 
			String baseUrl = pageHelper.CILink.split(".com")[0] + ".com/";
						
		report.startStep("Check URL for ExtLoginAPI");
			String extLoginUrl = baseUrl+"extlogin.aspx?IID="+institutionId+"&UserName="+userName+"&Password="+pass+"&Link="+refUrl; 
			webDriver.openUrl(extLoginUrl);
			pageHelper.skipOptin();
			homePage.closeAllNotifications();
			homePage.waitHomePageloaded();
			
		report.startStep("Verify the User data after Login");
			verifyUserDataInProfile();
			homePage.clickOnLogOut();
			sleep(1);
		
		report.startStep("Check URL for ExtLoginAPI - Update user - incorrect username");
			String extloginUrlIncorrect = baseUrl+"extlogin.aspx?IID="+institutionId+"&UserName="+userName+"X"+"&Password="+pass+"&Link="+refUrl;  
			sleep(1);
			webDriver.openUrl(extloginUrlIncorrect);
			//sleep(4);
			
			WebElement error = webDriver.waitForElement("b", ByTypes.tagName,5,true,null);
			for(int i = 0;i<10 && error==null;i++) {
				error = webDriver.waitForElement("b", ByTypes.tagName,1,true,null);
			}
			String errorActual = error.getText();
			testResultService.assertEquals(extLoginIncorrectUserName, errorActual,"Check Error Message");		
	}

	@Test
	@Category(LoginPage.class)
	@TestCaseParams(testCaseID = { "50505" } )
	public void testExtLoginTeacherByGet() throws Exception {

		report.startStep("Open browser and get users");
			institutionName = institutionsName[0];
			pageHelper.initializeData();
			//pageHelper.closeBrowserAndOpenInCognito(false);
			
			String[] teacher=null;
			teacher = pageHelper.getTeacherInstitution(institutionId);
			
		report.startStep("Inint test data");
		 	
			userName = teacher[0];
			userFN = teacher[1]; 
			userLN = teacher[2];
			studentId = teacher[3];
			String pass = teacher[4]; 
			email = teacher[5];
			
			String refUrl = NewUxHomePage.CorpUrl; 
			String baseUrl = pageHelper.CILink.split(".com")[0] + ".com/";
					
		report.startStep("Extlogin as teacher");
			String loginUrl = baseUrl+"extlogin.aspx?IID="+institutionId+"&UserName="+userName+"&Password="+pass+"&Link="+refUrl;  
			webDriver.openUrl(loginUrl);
			pageHelper.skipOptin();
			homePage.closeAllNotifications();
			sleep(2);

		
		report.startStep("Verify redirect to referrer URL from parameters");
			//String currentUrl = "";
			//currentUrl = webDriver.waitForSpecificCurrentUrl(currentUrl, "engdis");
			tmsHome = new TmsHomePage(webDriver, testResultService);
			
		report.startStep("Logout from tms to referrer URL");
			tmsHome.switchToMainFrame();
			report.startStep("Check User Name on Home Page after Login");
			tmsHome = new TmsHomePage(webDriver, testResultService);
			homePage.closeAllNotifications();
			webDriver.switchToTopMostFrame();
			tmsHome.switchToMainFrame();
			tmsHome.checkUserDetails(userFN, userLN);
			//verifyTeacherLogedInToTms();
		//	tmsHome.clickOnExit();
			pageHelper.closeBrowserAndOpenInCognito(false);
			//currentUrl = "";
			///currentUrl = webDriver.waitForSpecificCurrentUrl(currentUrl, "engdis");
			
		report.startStep("relogin as teacher");
			webDriver.openUrl(loginUrl);
			WebElement progressBar = loginPage.getLoginProgressBarElement();
			String fullMessage = progressBar.getText();
			assertEquals("Please wait while you are being logged into the system.\nThis may take a few minutes.", fullMessage);
			verifyAndWaitWhileDelayedLoginProgressBar();
			//verifyTeacherLogedInToTms();
			homePage.closeAllNotifications();
			tmsHome.switchToMainFrame();
			report.startStep("Check User Name on Home Page after Login");
			tmsHome = new TmsHomePage(webDriver, testResultService);
			homePage.closeAllNotifications();
			webDriver.switchToTopMostFrame();
			tmsHome.switchToMainFrame();
			tmsHome.checkUserDetails(userFN, userLN);
			
		report.startStep("logout from TMS");
			tmsHome.clickOnExit();
			sleep(2);
			
	}
	
	@Test
	@TestCaseParams(testCaseID = { "43954" }, testTimeOut = "20")
	public void testExtLoginStudentByPost() throws Exception {
		
		report.startStep("Open browser and get users");
		institutionName = institutionsName[0];//0
		webDriver.closeBrowser();
		pageHelper.initializeData();
		String instToken = dbService.getApiToken(institutionId);
		className = configuration.getProperty("classname");
		String classId = dbService.getClassIdByName(className, institutionId);
		
		String testFileName = "";
		String userType = "";
		String[] user = null;
		String password = "";
	
		userType = "S"; // S = student T= Teacher
		testFileName = "ExtLoginStudentByPost_" + dbService.sig(4)+".html";
		user = pageHelper.getStudentsByInstitutionId(institutionId, classId);
		userName = user[0];
		userFN = user[1];
		password = user[3];
		studentId = user[6];

		report.startStep("Create fle and login");
		createExtLoginPostFile(password,instToken,testFileName);
		skipAllEntrancesAndVerifyStudentName();
				
	//	report.startStep("Check Client type and Operating system");
	//	testResultService.assertEquals(true, dbService.verifyClientTypeAndOperatingSystemOfProgress(studentId));
			
		report.startStep("Closing browser without logout and open new session");
		closeBrowserAndInitilizeNewSession(regUserUrl);
		webDriver.waitUntilElementAppears(webDriver.waitForElement("btnSubmit", ByTypes.name), 20);
		webDriver.waitForElement("btnSubmit", ByTypes.name).click();
		sleep(2);
				
		loginPage.verifyAndConfirmAlertMessage();
		sleep(2);
		skipAllEntrancesAndVerifyStudentName();
				
		//report.startStep("Check Client type and Operating system");
		//testResultService.assertEquals(true, dbService.verifyClientTypeAndOperatingSystemOfProgress(studentId));
				
		//	report.startStep("Confirm redirection of ED site");
		//	testResultService.assertEquals(pageHelper.linkEdStaticSite, pageHelper.getURI(), "Redirect URL is not correct");
				
		homePage.clickOnLogOut();
	}
	
	@Test
	@TestCaseParams(testCaseID = { "43954" }, testTimeOut = "20")
	public void testExtLoginTeacherByPost() throws Exception {
		
		report.startStep("Open browser and get users");
		institutionName = institutionsName[0];//0
		webDriver.closeBrowser();
		pageHelper.initializeData();
		String instToken = dbService.getApiToken(institutionId);
		className = configuration.getProperty("classname");
		
		String testFileName = "";
		String userType = "";
		String[] user = null;
		String password = "";
		

			
		report.startStep("Iteration 2, teacher scenario");
		userType = "T"; // S = student T= Teacher
		testFileName = "ExtLoginTeacherByPost_" + dbService.sig(4)+".html";
		user = pageHelper.getTeacherInstitution(institutionId);
		userName = user[0]; 
		studentId = user[3];
		password = user[4];
		userFN = user[1];
		userLN = user[2];
		useAssignedClassToUser = true;

		
		createExtLoginPostFile(password,instToken,testFileName);
			
		report.startStep("Check User Name on Home Page after Login");
		tmsHome = new TmsHomePage(webDriver, testResultService);
		homePage.closeAllNotifications();
		webDriver.switchToTopMostFrame();
		tmsHome.switchToMainFrame();
		tmsHome.checkUserDetails(userFN, userLN);
				
	//	report.startStep("Check Client type and Operating system");
	//	testResultService.assertEquals(true, dbService.verifyClientTypeAndOperatingSystemOfProgress(studentId));
				
		closeBrowserAndInitilizeNewSession(regUserUrl);
		webDriver.waitUntilElementAppears(webDriver.waitForElement("btnSubmit", ByTypes.name), 20);
		webDriver.waitForElement("btnSubmit", ByTypes.name).click();
		sleep(2);
							
		WebElement progressBar = loginPage.getLoginProgressBarElement();
		String fullMessage = progressBar.getText();
		assertEquals("Please wait while you are being logged into the system.\nThis may take a few minutes.", fullMessage);
		verifyAndWaitWhileDelayedLoginProgressBar();
			
		report.startStep("Check User Name on Home Page after Login");
		tmsHome = new TmsHomePage(webDriver, testResultService);
		homePage.closeAllNotifications();
		webDriver.switchToTopMostFrame();
		tmsHome.switchToMainFrame();
		tmsHome.checkUserDetails(userFN, userLN);
		
		report.startStep("Check Client type and Operating system");
				testResultService.assertEquals(true, dbService.verifyClientTypeAndOperatingSystemOfProgress(studentId));
				
		//	report.startStep("Confirm redirection of TMS site");
		//	testResultService.assertEquals(pageHelper.linkTmsStaticSite, pageHelper.getURI(), "Redirect URL TMS is not correct");
		
		tmsHome.clickOnExit();
	}
	
	private void createExtLoginPostFile(String password,String instToken,String testFileName) throws Exception {
		String baseUrl = pageHelper.CILink.split(".com")[0] + ".com/";
		String accessUrl = pageHelper.urlForExternalPages.split(".com")[0] + ".com/languages/";
		String chkPath = pageHelper.buildPathForExternalPages + "Languages";
		List<String> wList = new ArrayList<String>();
		wList = createHtmlTitle(wList);
		wList = createHTMLformExtLoginByPOST(wList,baseUrl,password,instToken,testFileName);
		wList =  createHtmlTail(wList);
		
	/*	String[] buildPath = chkPath.split("//");
		String path = "\\";
		for(int i=1;i<buildPath.length-1;i++) {
			path=path+"\\"+buildPath[i];
		}
		path = path +"\\Languages\\"+testFileName;*/
		textService.writeListToSmbFile(chkPath, testFileName, wList, useSMB);
		//textService.writeListToSmbFile(chkPath+"/"+testFileName, wList, netService.getDomainAuth());


		report.startStep("Get institution Remaining License for all Valid Packages");
		List<String[]> getValidInstitutionPackages = dbService.getValidInstitutionPackages(institutionId);

		String openURL = accessUrl + testFileName;
		pageHelper.closeBrowserAndOpenInCognito(false);
		webDriver.openUrl(openURL);
		webDriver.waitUntilElementAppears(webDriver.waitForElement("btnSubmit", ByTypes.name), 20);
		webDriver.waitForElement("btnSubmit", ByTypes.name).click();
		sleep(2);
		
		pageHelper.skipOptin();
		studentId = dbService.getUserIdByUserName(userName, institutionId);

		if (newTeacher || newStudent) {
			if (studentId != null) {
				dbService.insertUserToAutomationTable(institutionId, studentId, userName, CanvasClassName, baseUrl);
			}

			report.startStep("Check institution Remaining License after login");
			if (getValidInstitutionPackages != null)
				pageHelper.checkRemainingLicenseBurnForallValidPackages(getValidInstitutionPackages,institutionId,className,true);
			}
		regUserUrl = openURL;
	}

	private List<String> createHTMLformExtLoginByPOST(List<String> wList, String baseUrl,String password, String instToken, String testFileName) {
		
		wList.add("\t<form method=\"post\" action=\"" + baseUrl + "ExtLogin.aspx\"/>");
		wList.add("\t\t<input type=\"submit\" name=\"btnSubmit\" value=\"GO TO ED\" />");
		wList.add("\t\t<input type=\"hidden\" name=\"UserName\" value=\"" + userName + "\" />");
		wList.add("\t\t<input type=\"hidden\" name=\"Password\" value=\"" + password + "\" />");
		wList.add("\t\t<input type=\"hidden\" name=\"link\" value=\"" + CorpUrl + "\" />");	
		wList.add("\t\t<input type=\"hidden\" name=\"IName\" value=\"" + institutionName + "\" />");
		wList.add("\t\t<input type=\"hidden\" name=\"Token\" value=\"" + instToken + "\" />");
		
		wList.add("\t</form>");
		
		return wList;
	}

	@Test
	@TestCaseParams(testCaseID = { "43954" }, testTimeOut = "20")
	public void testRegLogin_Minal_TheSameStudentByGET() throws Exception {

		institutionName = institutionsName[0]; 
		pageHelper.initializeData();
		pageHelper.closeBrowserAndOpenInCognito(false);

		useToken=true;
		useAssignedClassToUser = true; // colman

		for (int i=0;i<2;i++){
			
				try {
					report.startStep("Enter ED via new RegAndLogin API - Insert New User");
					
					if (pageHelper.CILink.toLowerCase().contains("engdis")) // beta or production
						classId = "522820016";
					
					else if (pageHelper.CILink.toLowerCase().contains("ci-rc")) // rc
						classId = "522821422";//   522821415
					
					else if (pageHelper.CILink.toLowerCase().contains("ci-dev")) // main
						classId = "522826163";
					
					regLogin(institutionId, UserType.Student);
					verifyUserLoggedInCorrectlyAndHomePagedisplay();	
					
					if (i==0) // close browser and relogin - delayed login
					{
						report.startStep("Close broswer and relogin");	
						webDriver.closeBrowser();
						webDriver.init();
						webDriver.maximize();
						
						webDriver.openUrl(regUserUrl);
						sleep(2);
						
					//	if (PageHelperService.branchCI.contains("main"))
							loginPage.verifyAndConfirmAlertMessage();
							homePage.closeAllNotifications();
					//	else {
					//		verifyAndWaitWhileDelayedLoginProgressBar();
							//sleep(2);	
					//	}
					//	homePage.closeAllNotifications();
						//verifyAndWaitWhileDelayedLoginProgressBar();
				
						report.startStep("Check Client type and Operating system");
						testResultService.assertEquals(true, dbService.verifyClientTypeAndOperatingSystemOfProgress(studentId));
						
						homePage.clickOnLogOut();
						webDriver.closeBrowser();
						webDriver.init();
						webDriver.maximize();
					}
					
					else{  // logout and relogin
						
						homePage.clickOnLogOut();
						sleep(2);
						webDriver.openUrl(regUserUrl);
						loginPage.VerifyLoginProgressDisplay(false);
				
						verifyUserLoggedInCorrectlyAndHomePagedisplay();
					
					//report.startStep("Check Client type and Operating system");
					//testResultService.assertEquals(true, dbService.verifyClientTypeAndOperatingSystemOfProgress(studentId));
						homePage.clickOnLogOut();
					}

					
				} catch (Exception e) {
					testResultService.addFailTest(e.toString(), false, true);
		
				} finally {
					assignCoursesToClassWhenTestEnds = false;
				}
			}
			
	}
	
	private void verifyUserLoggedInCorrectlyAndHomePagedisplay() throws Exception {
		
		
		report.startStep("Check home page loaded with the correct assignment");
		homePage.closeAllNotifications();
		pageHelper.skipOnBoardingHP();
		
		homePage.waitHomePageloadedFully();
		
		report.startStep("Check User Name on Home Page after Login");
		testResultService.assertEquals(true, homePage.getUserDataText().contains(userFN),
				"User Name not found on home page");
		
		report.startStep("Check user assigned to new class created");
	
		testResultService.assertEquals(true,
				pageHelper.isUserAssignedToClass(studentId, classId),
				"Class not created or user not assigned to class");

	}

	
	@Test
	@TestCaseParams(testCaseID = { "43954" }, testTimeOut = "20")
	public void testRegLoginTeacherCreateClassByGET() throws Exception {

		institutionName = institutionsName[5];//5
		pageHelper.closeBrowserAndOpenInCognito(false);
		pageHelper.initializeData();
		// String className = "Class1_Umm Distance";

		report.startStep("Generate student username for registration");
		// String teacherUserName = "t" + dbService.sig(5);
		useNewClass = true;
		useMapTable = false;
		useLti = false;

		try {
			report.startStep("Enter ED via new RegAndLogin API - Insert New User");
			regLogin(institutionId, UserType.Teacher);

			report.startStep("Check User Name on Home Page after Login");
			tmsHome = new TmsHomePage(webDriver, testResultService);
			tmsHome.switchToMainFrame();
			tmsHome.checkUserDetails(userFN, userLN);

			report.startStep("Check user assigned to new class created");
			testResultService.assertEquals(true,
					pageHelper.isTeacherAssignedToClass(studentId, dbService.getClassIdByName(className, institutionId)), //UMMInstId)),
					"Class not created or teacher not assigned to class");
			
		//	report.startStep("Check Client type and Operating system");
		//	testResultService.assertEquals(true, dbService.verifyClientTypeAndOperatingSystemOfProgress(studentId));
			
		} catch (Exception e) {
			testResultService.addFailTest(e.toString(), false, true);

		} finally {
			// pageHelper.deleteStudentsAndClass(argInstId, CanvasClassName);
			 //dbService.deleteUserById(studentId);
			assignCoursesToClassWhenTestEnds = false;

		}

	}

	@Test
	@TestCaseParams(testCaseID = { "84922" }, testTimeOut = "20")
	public void testRegLoginExistingClassesToExistingTeacherByGET() throws Exception {

		institutionName = institutionsName[6];//6
		pageHelper.closeBrowserAndOpenInCognito(false);
		pageHelper.initializeData();
		// String className = "Class1_Umm Distance";

		report.startStep("Get one teacher classes");

		// String teacherUserName = "";

		useNewClass = false;
		newTeacher = false;
		multipleClasses = false;
		useMapTable = false;
		useLti = false;
		amountClasses = 0;
		try {
			report.startStep("Enter ED via new RegAndLogin API - Insert New User");
			regLogin(institutionId, UserType.Teacher);

			report.startStep("Check User Name on Home Page after Login");
			tmsHome = new TmsHomePage(webDriver, testResultService);
			tmsHome.switchToMainFrame();
			tmsHome.checkUserDetails(userFN, userLN);
			tmsHome.clickOnRegistration();
			sleep(1);
			tmsHome.clickOnClasses();

			report.startStep("Check teacher assigned to class");

			// get Teacher classes from DB
			List<String[]> teacherClasses = pageHelper.getTeacherClasses(studentId);
			assertEquals(amountClasses + 1, teacherClasses.size());
			
			report.startStep("Print the classes");
			for (int i = 0; i < teacherClasses.size(); i++) {
				System.out.println(teacherClasses.get(i)[1].toString());
				report.report(teacherClasses.get(i)[1].toString());
			}

		} catch (Exception e) {
			testResultService.addFailTest(e.toString(), false, true);

		} finally {
			assignCoursesToClassWhenTestEnds = false;
		}

	}

	@Test
	@TestCaseParams(testCaseID = { "84922" }, testTimeOut = "20")
	public void testRegLoginTheSameTeacherAfterCloseBroserByGET() throws Exception {

		institutionName = institutionsName[0];
		pageHelper.closeBrowserAndOpenInCognito(false);
		pageHelper.initializeData();
		
		report.startStep("Get one teacher classes");

		useAssignedClassToUser=true;

		try {
			report.startStep("Enter ED via new RegAndLogin API - Insert New User");
			regLogin(institutionId, UserType.Teacher);

			report.startStep("Verifing the user logged in");
			verifyTeacherLogedInToTms();
			
			webDriver.closeBrowser();
			webDriver.init();
			webDriver.maximize();
			
			webDriver.openUrl(regUserUrl);
			sleep(2);
			
		//	if (PageHelperService.branchCI.contains("main"))
			//	loginPage.verifyAndConfirmAlertMessage();
		//	else {
				verifyAndWaitWhileDelayedLoginProgressBar();
		//		sleep(2);	
		//	}
			verifyTeacherLogedInToTms();
			
		} catch (Exception e) {
			testResultService.addFailTest(e.toString(), false, true);

		} finally {
			assignCoursesToClassWhenTestEnds = false;
		}

	}
	
	private void verifyTeacherLogedInToTms() throws Exception {

		report.startStep("Check User Name on Home Page after Login");
		tmsHome = new TmsHomePage(webDriver, testResultService);
		homePage.closeAllNotifications();
		webDriver.switchToTopMostFrame();
		tmsHome.switchToMainFrame();
		tmsHome.checkUserDetails(userFN, userLN);
		tmsHome.clickOnRegistration();
		sleep(1);
		tmsHome.clickOnClasses();

		report.startStep("Check teacher assigned to class");
		List<String[]> teacherClasses = pageHelper.getTeacherClasses(studentId);

			for (int i = 0; i < teacherClasses.size(); i++) {
					tmsHome.checkClassNameIsDisplayed(teacherClasses.get(i)[1]);
				}
	}

	@Test
	@TestCaseParams(testCaseID = { "" }, testTimeOut = "20")
	public void testRegLoginWithMappingAssigningMultipleClassesByGet() throws Exception {
		institutionName = institutionsName[2];
		pageHelper.closeBrowserAndOpenInCognito(false);
		pageHelper.initializeData();

		report.startStep("Get the test date from the CSV file");
		List<String[]> listFromCsv = textService
				//.getStr2dimArrFromCsv("smb://"+configuration.getGlobalProperties("logserverName")+"//automation//CSV_files//MappingAssignmentMultipleClassesByGET.csv", true);
		.getStr2dimArrFromCsv("\\\\"+configuration.getGlobalProperties("logserverName")+"\\automation\\CSV_files\\MappingAssignmentMultipleClassesByGET.csv", false);
			
		for (int i = 2; i <= (listFromCsv.size() - 3); i++) {//
			
			useNewClass = Boolean.parseBoolean(listFromCsv.get(i)[0]);
			multipleClasses = Boolean.parseBoolean(listFromCsv.get(i)[1]);
			newTeacher = Boolean.parseBoolean(listFromCsv.get(i)[2]);
			newStudent = Boolean.parseBoolean(listFromCsv.get(i)[3]);
			student = Boolean.parseBoolean(listFromCsv.get(i)[4]);
			
			report.startStep("Execution Id: " + i + ",New Teacher=" + newTeacher + " ,New Classes=" + useNewClass
					+ " ,MiltipleClasses=" + multipleClasses+" newStudent =" +newStudent+" student = "+student);

			UserType userType = UserType.Teacher;
			if (student) {
				userType = UserType.Student;
			}
			classAssignmnetAmount = 2;
			useMapTable = true;
			defineUser(userType);
			storeAmountOfTeacherClases(userType);
			char createClass = assignNewOrCurrentClassesWithAmount(classAssignmnetAmount, userType);
			createUrlAndSentRequestLogin(createClass, userType);
			verifyResults(userType);
		}
	}

	@Test
	@TestCaseParams(testCaseID = { "84922" }, testTimeOut = "20")
	public void testRegLoginAssignMultipleClassesToTeacherByGET() throws Exception {

		institutionName = institutionsName[6];
		pageHelper.closeBrowserAndOpenInCognito(false);
		pageHelper.initializeData();
		boolean moodleMode = false;
		useMapTable = false;
		// report.startStep("Get one teacher classes");

		report.startStep("Get the test date from the CSV file");
		List<String[]> listFromCsv = textService
				//.getStr2dimArrFromCsv("smb://"+configuration.getGlobalProperties("logserverName")+"//automation//CSV_files//AssignMultipleClassesByGET.csv", true);
				.getStr2dimArrFromCsv("\\\\"+configuration.getGlobalProperties("logserverName")+"\\automation\\CSV_files\\AssignMultipleClassesByGET.csv", false);
		for (int i = 2; i <= (listFromCsv.size() -5); i++) {//4 starts

			useNewClass = Boolean.parseBoolean(listFromCsv.get(i)[0]);
			multipleClasses = Boolean.parseBoolean(listFromCsv.get(i)[1]);
			newTeacher = Boolean.parseBoolean(listFromCsv.get(i)[2]);
			newStudent = Boolean.parseBoolean(listFromCsv.get(i)[3]);
			student = Boolean.parseBoolean(listFromCsv.get(i)[4]);

			UserType userType = UserType.Teacher;
			if (student) {
				userType = UserType.Student;
			}
			
			report.startStep("Execution Id: " + i + ",New Teacher=" + newTeacher + " ,New Classes=" + useNewClass
					+ " ,MiltipleClasses=" + multipleClasses+" newStudent =" +newStudent+" student = "+student);
			
			classAssignmnetAmount = 2;
			defineUser(userType);
			storeAmountOfTeacherClases(userType);
			char createClass = assignNewOrCurrentClassesWithAmount(classAssignmnetAmount, userType);
			createUrlAndSentRequestLogin(createClass, userType);
			verifyResults(userType);
		}
		// define user (student or teacher) define new or get (all details,
		// FDN,LA,UserName,Email,password)
		// define new or get class and amount
		// define the url and call to reg user request get or post
		// check the result

	}

	public void storeAmountOfTeacherClases(UserType userType) throws Exception {
		List<String[]> teacherClassesBefore = null;
		
		if ((!newTeacher) && userType.name().equals("Teacher")) {
			if (studentId != null) {
				// studentId = dbService.getUserIdByUserName(userName, institutionId);
				 if (useMapTable || useLti) {
					 teacherClassesBefore = dbService.getExternalTeacherClasses(studentId);
					 sleep(1);
				 
				 } else {
					teacherClassesBefore = pageHelper.getTeacherClasses(studentId);
					sleep(1);
				 }
			}
			
			if(teacherClassesBefore!=null) {
			amountClasses = teacherClassesBefore.size();
			}
		}
	}

	public void verifyResults(UserType userType) {

		try {
			report.startStep("Check User Name on Home Page after Login");
			if (userType.name().equals("Teacher")) {
				tmsHome = new TmsHomePage(webDriver, testResultService);
				tmsHome.switchToMainFrame();
				tmsHome.checkUserDetails(userFN, userLN);
				tmsHome.clickOnRegistration();
				sleep(1);
				tmsHome.clickOnClasses();

				// get Teacher classes from DB
				List<String[]> teacherClasses = null;
				 if(useMapTable) {
				 teacherClasses = dbService.getExternalTeacherClasses(studentId);
				 }else {
				teacherClasses = pageHelper.getTeacherClasses(studentId);
				 }

				verifyPreviousClases(teacherClasses, userType);
				verifyNewClassesAreAsigned(teacherClasses);
				//tmsHome.clickOnExitTMS();
				tmsHome.clickOnExit();
				sleep(2);
				//loginPage.waitForPageToLoad();
			}
			// this is verifying student
			if (userType.name().equals("Student")) {
				verifyUserDataInProfile();
			//	homePage.logOutOfED();
				homePage.clickOnLogOut();
				sleep(2);
				//loginPage.waitForPageToLoad();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void verifyNewClassesAreAsigned(List<String[]> teacherClasses) throws Exception {

		report.startStep("Check that teacher assigned to new class or classes");
		String[] arr = className.split(";");
		boolean[] allClassesPresent = new boolean[arr.length];
		boolean stored = false;
		int index = 0;
		if (arr.length == 1) {
			for (int i = 0; i < teacherClasses.size(); i++) {
				if (teacherClasses.get(i)[0].equalsIgnoreCase(className)) {
					System.out.println("New class are stored in DB");
					stored = true;
				}
			}
		} else {
			for (int i = 0; i < arr.length; i++) {
				for (int j = 0; j < teacherClasses.size(); j++) {
					if (arr[i].equalsIgnoreCase(teacherClasses.get(j)[0])) {
						System.out.println("New class are stored in DB");
						allClassesPresent[index] = true;
						index++;
					}
				}
			}
		}
		if (arr.length == 1) {
			assertEquals(true, stored);
		} else {
			assertEquals(arr.length, allClassesPresent.length);
		}
	}

	private void verifyPreviousClases(List<String[]> teacherClasses, UserType userType) throws Exception {

		report.startStep("Check that previous classes not erased");

		if (multipleClasses && (!userType.equals("Student"))) {
			assertEquals(amountClasses + classAssignmnetAmount, teacherClasses.size());
		} else {
			assertEquals(amountClasses + 1, teacherClasses.size());
		}

	}

	public void createUrlAndSentRequestLogin(char createClass, UserType userType) {
		if(email == null || email.equals("")) {
			email = userName + "r-r" + "-@edusof-t.co.il";
		}
		String baseUrl = pageHelper.CILink.split(".com")[0] + ".com/";
		char userTypeParam = 'T';
		char useNameMapping = 'N';
		char useClassMaping = 'N';
		String nameOfClass ="";
		
		if (userType.name().equals("Student")) {
			userTypeParam = 'S';
		}
		if (useMapTable) {
			useNameMapping = 'Y';
			useClassMaping ='Y';
			nameOfClass=CanvasClassName;
		}else {
			nameOfClass=className;
		}
				
		try {

			String regInsertUrl = baseUrl + "RegUserAndLogin.aspx?Action=I&UserName=" + userName + "" + "&Inst="
					+ institutionName + "&FirstName=" + userFN + "&LastName=" + userLN + "" + "&Password="+password+"&Email="
					+ email + "&Class=" + nameOfClass + "" + "&Language=" + Language + "&Link=" + CorpUrl
					+ "&UseNameMapping=" + useNameMapping + "&CreateClass=" + createClass + "" + "&UserType="
					+ userTypeParam+ "&UseClassMapping="+useClassMaping;

			webDriver.openUrl(regInsertUrl);
			sleep(2); //if(newStudent || newTeacher ||useNewClass) {
			pageHelper.skipOptin();
			//}
			if (useMapTable && (newTeacher || newStudent)) {
				studentId = dbService.getExternalUserId(userName);
			} else {
				// dbService.getExternalUserInternalId(regInsertUrl)
				if(!useMapTable)
				studentId = dbService.getUserIdByUserName(userName, institutionId);
			}
			homePage.skipNotificationWindow();
			pageHelper.skipOnBoardingHP();

			if (newTeacher || newStudent) {
				if (studentId != null) {
					dbService.insertUserToAutomationTable(institutionId, studentId, userName, CanvasClassName, baseUrl);
				}
			}

			if (useNewClass)
				insertNewClassIntoAutomationTable(baseUrl);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		// return className;
	}

	public char assignNewOrCurrentClassesWithAmount(int amountOfClases, UserType userType) throws Exception {
		
		StringBuilder sb = new StringBuilder();
		char createClass = 'N';
		int classExtenssion = 4;

		List<String[]> classes = null;
		String typeOfUser = userType.name();

		if (multipleClasses && typeOfUser.equals("Teacher")) {
			if (useNewClass) {
				createClass = 'Y';
				for (int i = 1; i <= amountOfClases; i++) {
					String className = "Class-" + dbService.sig(classExtenssion++);
					sb.append(className);
					if (i < amountOfClases) { // it is the not last class{
						sb.append(";");
					}
				}
				className = sb.toString();
			}

			else {
				// if it's not new teacher and not new class than we need to exclude adding the
				// same classes
				if ((!newTeacher) && typeOfUser.equals("Teacher")) {
					
					if (useMapTable || useLti) {
						List<String[]> allClassesFromExternalInstitution = dbService
								.getExternalClassesNameByInstitutionId(institutionId);
						classes = dbService.getExternalTeacherClasses(studentId);
						addClasses(allClassesFromExternalInstitution, classes, amountOfClases);
					} else {
						List<String[]> allClassesOfInstitution = pageHelper
								.getclassesByInstitutionName(institutionName);
						classes = pageHelper.getTeacherClasses(studentId);
						addClasses(allClassesOfInstitution, classes, amountOfClases);
					}
				}

				else {// if it's new teacher, not new classes, but multiple classes

					if (newTeacher && typeOfUser.equals("Teacher")) {
						if (useMapTable) {
							classes = dbService.getExternalClassesNameByInstitutionId(institutionId);
							for (int i = 1; i <= amountOfClases; i++) {
								sb.append(getClassFromClassesList(classes));
								if (i < amountOfClases) {
									sb.append(";");
								}
							}
						} else {
							classes = pageHelper.getclassesByInstitutionName(institutionName);
							for (int i = 1; i <= amountOfClases; i++) {
								sb.append(getClassFromClassesList(classes));
								if (i < amountOfClases) {
									sb.append(";");
								}
							}
						}
						className = sb.toString();
						if(useMapTable) {
							StringBuilder strb = new StringBuilder();
							String arr[] = className.split(";");
							for (int i = 0; i < arr.length; i++) {
								//String classId = dbService.getClassIdByClassName(arr[i], institutionId);
								String classId = dbService.getExternalClassIdByExternalClassName(arr[i], institutionId);
								strb.append(classId);
								if (i < arr.length - 1) { // it is the not last class{
									strb.append(";");
								}
							}
							CanvasClassId = strb.toString();
						}
					}
				}
			}
		} else {
			// it's not multiple classes
			if (useNewClass) {

				if (typeOfUser.equals("Student")) {
					createClass = 'Y';
					className = "ExtClassName-" + dbService.sig(5);

				} else {
					createClass = 'Y';
					className = "ExtClassName-" + dbService.sig(5);
				}
			} 
			
			else {
				// if it's not multiple classes and assign current class
				List<String[]> allClassesOfInstitution;
				if (useMapTable || useLti) {
					allClassesOfInstitution = dbService.getExternalClassesNameByInstitutionId(institutionId);
					
					if (typeOfUser.equals("Teacher")){
						if (!newStudent && !newTeacher && useMapTable)
							classes = dbService.getExternalTeacherClasses(studentId);
						else
							classes = dbService.getExternalClassesForTeacher(studentId);	
					}
				}
				else {
					allClassesOfInstitution = pageHelper.getclassesByInstitutionName(institutionName);
					classes = pageHelper.getTeacherClasses(studentId);					
				}

				if (newStudent && typeOfUser.equals("Student")) {
					className = getClassFromClassesList(allClassesOfInstitution);
				//	if (useMapTable || useLti) {
				//		CanvasClassId = 
								//dbService.getExternalClassIdByExternalClassName(className, institutionId);
								//CanvasClassId = dbService.getClassIdByClassName(internalClassName, institutionId);
				//		CanvasClassName = className;
				//	}

				}
				if (!newStudent && typeOfUser.equals("Student")) {
					className = getClassFromClassesList(allClassesOfInstitution);
				}
				if (newTeacher && typeOfUser.equals("Teacher")) {
					className = getClassFromClassesList(allClassesOfInstitution);
				}
				
				if (!newTeacher && typeOfUser.equals("Teacher")) {

					boolean classNotAssigned = true;
					int ran = 0;
				//	classes = pageHelper.getTeacherClasses(studentId);
					for (int i = 0; i < allClassesOfInstitution.size(); i++) {
						ran = new Random().nextInt(allClassesOfInstitution.size()-1);
						for (int j = 0; j < classes.size(); j++) {
							if(useMapTable || useLti) {
								if (allClassesOfInstitution.get(ran)[1].equalsIgnoreCase(classes.get(j)[1])) {
									classNotAssigned = false;
								}
							}else {
								if (allClassesOfInstitution.get(ran)[0].equalsIgnoreCase(classes.get(j)[0])) {
									classNotAssigned = false;
							}
						}		
					}
						if (classNotAssigned) {
							
							if(useMapTable || useLti) {
								className = allClassesOfInstitution.get(ran)[1];
								CanvasClassName = className;
								CanvasClassId = allClassesOfInstitution.get(ran)[2];
										//dbService.getExternalClassIdByExternalClassName(className, institutionId);
							//	CanvasClassId = dbService.getClassIdByClassName(className, institutionId);
							}else {
								className = allClassesOfInstitution.get(ran)[1];
								CanvasClassName = className;
								CanvasClassId = allClassesOfInstitution.get(ran)[2]; 
										//dbService.getClassIdByName(className, institutionId);
							}
							break;
						}
						classNotAssigned = true;
						}
				   }
   			}
		}

		return createClass;

	}

	private void addClasses(List<String[]> allClassesOfInstitution, List<String[]> classes, int amountOfClases) throws Exception {
		int stopIterate = 1;
		boolean classNotAssigned = true;
		StringBuilder sb = new StringBuilder();
		// boolean userType = userType.name().equals("Teacher");
		int ran = 0;
		for (int i = 0; i < allClassesOfInstitution.size(); i++) {
			classNotAssigned = true;
			if(classes==null) {
				ran = new Random().nextInt(allClassesOfInstitution.size()-1);
			}else {
			for (int j = 0; j < classes.size(); j++) {
				ran = new Random().nextInt(allClassesOfInstitution.size()-1);
				if (allClassesOfInstitution.get(ran)[0].equalsIgnoreCase(classes.get(j)[0])) {
					classNotAssigned = false;
					}
				}
			}
			
			if (classNotAssigned) {
				if(useMapTable)
					sb.append(allClassesOfInstitution.get(ran)[2]);
				else {
					sb.append(allClassesOfInstitution.get(ran)[0]);
				}
				if (stopIterate < amountOfClases) {
					sb.append(";");
				}
				stopIterate++;
			}
			if (stopIterate > amountOfClases)
				break;
		}
		className = sb.toString();
		CanvasClassName = className;

		if(useMapTable) {
		if (multipleClasses) {
			StringBuilder strb = new StringBuilder();
			String arr[] = className.split(";");
			for (int i = 0; i < arr.length; i++) {
				//String classId = dbService.getClassIdByClassName(arr[i], institutionId);
				String classId = dbService.getExternalClassIdByExternalClassName(arr[i], institutionId);
				strb.append(classId);
				if (i < arr.length - 1) { // it is the not last class{
					strb.append(";");
				}
			}
			CanvasClassId = strb.toString();
		} else {
			CanvasClassId = dbService.getClassIdByClassName(className, institutionId);
			}
		}
	}

	/*
	 * for (int i = 0; i < allClassesOfInstitution.size(); i++) { classNotAssigned =
	 * true; for (int j = 0; j < classes.size(); j++) { if
	 * (allClassesOfInstitution.get(i)[0].equalsIgnoreCase(classes.get(j)[0])) {
	 * classNotAssigned = false; } } if (classNotAssigned) {
	 * sb.append(allClassesOfInstitution.get(i)[0]); if (stopIterate <
	 * amountOfClases) { sb.append(";"); } stopIterate++; } if (stopIterate >
	 * amountOfClases) break; } className = sb.toString();
	 */

	public char defineUser(UserType userType) throws Exception {

		report.addTitle("The parmeters is: Courses ints Id: " + institutionId);
		// String instName = institutionName; // dbService.getInstituteNameById(instID);

		char userTypeParam = 'S';

		switch (userType) {

		case Teacher: {
			userTypeParam = 'T';

			if (newTeacher) {
				userName = "t" + dbService.sig(5);
				userFN = userName + "-FN";
				userLN = userName + "-LN";
				email = userName + "r-r" + "-@edusof-t.co.il";
				password = "12345";

			} else {
				String[] teacher = null;
				if (useMapTable) {
					teacher = dbService.getExternalMapTeacherInstitution(institutionId);
					userName = teacher[5];
					userFN = teacher[1];
					userLN = teacher[2];
					studentId = teacher[3];
					email = teacher[4];
					password = teacher[6];
				} else {
					teacher = pageHelper.getTeacherInstitution(institutionId);
					userName = teacher[0];
					userFN = teacher[1];
					userLN = teacher[2];
					studentId = teacher[3];
					email = teacher[5];
					password = teacher[4];
				}
			}
			break;
		}

		case Student: {
			userTypeParam = 'S';
			if (newStudent) {
				userName = "stud" + dbService.sig(5);
				userFN = userName + "-FN";
				userLN = userName + "-LN";
				email = userName + "r-r" + "-@edusof-t.co.il";
				password = "12345";

			} else {
				String[] studentd = null;
				if (useMapTable) {
					studentd = dbService.getRandomExternalStudentByInstitutionId(institutionId);
					userName = studentd[5];
					userFN = studentd[1];
					userLN = studentd[2];
					email = studentd[6];
					studentId = studentd[7];
					password = studentd[3];

				} else {
					studentd = pageHelper.getStudentsByInstitutionId(institutionId);
					userName = studentd[0];
					userFN = studentd[1];
					userLN = studentd[2];
					email = studentd[5];
					studentId = studentd[6];
					password = studentd[3];
				}
			}
		}
			break;
		}

		return userTypeParam;
	}

	private void checkTeacherAssignedToClass(String className, List<String[]> teacherClasses) {

		boolean found = false;

		for (int i = 0; i < teacherClasses.size() && !found; i++) {

			if (className.equalsIgnoreCase(teacherClasses.get(i)[1]))
				found = true;
		}

		try {
			testResultService.assertEquals(true, found, "Class not assigned to Teacher");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String regLogin(String instID, UserType userType) throws Exception {

		report.report("The parmeters is: " + instID + " UserName: " + userName);
		char createClass = 'N';
		char userTypeParam = 'S';
		String className1 = "";
		String className2 = "";
	//	String teacherId = "";
		String action = "i";
		//String apiToken = dbService.getApiToken(institutionId);

		report.report("The user type is: " + userType);
		switch (userType) {

			case Teacher: {
				userTypeParam = 'T';
	
				if (newTeacher) {
					userName = "t" + dbService.sig(5);
					userFN = userName + "-FN";
					userLN = userName + "-LN";
					email = userName + new Random().nextInt(1000) + "-@edusof-t.co.il";
				} else {
					String[] teacher = pageHelper.getTeacherInstitution(institutionId);
					userName = teacher[0];
					userFN = teacher[1];
					userLN = teacher[2];
					email =  teacher[5];
					studentId = teacher[3];
					//action = "U";
					className = getTeacherClassesAsString(studentId);
					
					if (email==null||email.equalsIgnoreCase(""))
						email = "a@a.com";	
				}
				break;
			}

			case Student: {
				userTypeParam = 'S';
				if (newStudent) {
					userName = "stud" + dbService.sig(5);
					userFN = userName + "-FN";
					userLN = userName + "-LN";
					email = userName + new Random().nextInt(1000) + "-@edusof-t.co.il";
					
				} else {
	
					String[] studentd=null;
					
					if (classId.equalsIgnoreCase(""))
						studentd = pageHelper.getStudentsByInstitutionId(institutionId);
					else
						studentd = pageHelper.getStudentsByInstitutionId(institutionId,classId); // get spesific class for Minal college
						userName = studentd[0];
						userFN = studentd[1];
						userLN = studentd[2];
						email = studentd[5];
						action = "i"; 
						studentId = studentd[6];	
				
						className = dbService.getClassNameByClassId(
							dbService.getUserClassId(studentId));
					
						classId = dbService.getUserClassId(studentId);
					
						if (email==null||email.equalsIgnoreCase(""))
							email = "a@a.com";	
					}
				}
				break;
			}
		
		if (!newStudent && !newTeacher) {
			studentId = dbService.getUserIdByUserName(userName, institutionId);
			
			switch (userType) {
			case Teacher: {
				List<String[]> teacherClassesBefore = pageHelper.getTeacherClasses(studentId);
				if(teacherClassesBefore!=null) {
					amountClasses = teacherClassesBefore.size(); }
				}
			}
		}

		if (!useAssignedClassToUser)
		{
			if (multipleClasses) {
				className1 = "Class-" + dbService.sig(3);
				className2 = "Class-" + dbService.sig(4);
				className = className1 + ";" + className2;
			} else
				className = "Class-" + dbService.sig(3);
	
			if (useNewClass) {
				createClass = 'Y';
			}
			else {
				List<String[]> classes = pageHelper.getclassesByInstitutionName(institutionName);
	
				if (multipleClasses)
					className = get2DiffExistingClasses(classes);
				else
					className = getClassFromClassesList(classes);
			}
		}
		String baseUrl = pageHelper.CILink.split(".com")[0] + ".com/";

		try {
			
			report.startStep("Open the url");
			
			// set the user to logged in null in order to login correctly.
			pageHelper.setUserLoginToNull(studentId);

			report.startStep("Get institution License for all Valid Packages");
			//List<String[]> baseLineLicense = new ArrayList<>();

			classPackages = dbService.getClassAssignPackagesNew(className, institutionId);
			if (classPackages==null)
				baseLineLicense = dbService.getValidInstitutionPackages(institutionId);
			else
				baseLineLicense = dbService.getRemainingLicensesOfCertainPackage(classPackages, institutionId);

			/*List<String[]> classPackages = new ArrayList<>();
			if (!useNewClass&&userType.toString().equalsIgnoreCase("Student")&&newStudent){
				classPackages = dbService.getClassAssignPackagesNew(className, institutionId);
				if(classPackages!=null){
					validInstitutionPackages = dbService.getRemainingLicensesOfCertainPackage(classPackages, institutionId);
				}else {
					System.out.println("<==================!!! Class "+className+"HAS NO PACKAGES !!!====================>");
				}
			}*/

			regUserUrl = baseUrl + "RegUserAndLogin.aspx?Action=" + action+"&UserName=" + userName + "" + "&Inst="
					+ institutionName + "&FirstName=" + userFN + "&LastName=" + userLN + "" + "&Password=12345&Email="
					+ email + "&Class=" + className + "" + "&Language=" + Language + "&Link=" + CorpUrl
					+ "&UseNameMapping=N&CreateClass=" + createClass + "" + "&UserType=" + userTypeParam;

			if (useToken){ // minal collegue
				//regUserUrl = regUserUrl + "&token=" + apiToken;
				//regUserUrl = regUserUrl.replace("https://edusoftlearning.com/", BasicNewUxTest.CannonicalDomain);
			}
		//	if (institutionName.equalsIgnoreCase("automation") || (institutionName.equalsIgnoreCase("courses")))
		//		regUserUrl = regUserUrl.replace("ed.", "ed2.");

			webDriver.openUrl(regUserUrl);
			pageHelper.skipOptin();
			studentId = dbService.getUserIdByUserName(userName, institutionId);
			homePage.skipNotificationWindow();

			if (newTeacher || newStudent) {
				if (studentId != null) {
					dbService.insertUserToAutomationTable(institutionId, studentId, userName, CanvasClassName, baseUrl);
					pageHelper.skipOnBoardingHP();
				}

				report.startStep("Check institution License after login");
				if (baseLineLicense != null) {
					sleep(5);
						verifyLicencesBurnedAfterLogin(baseLineLicense);
				}
			}

			if (useNewClass)
				insertNewClassIntoAutomationTable(baseUrl);


		} catch (Exception e) {
			e.printStackTrace();
		}

		return className;
	}

	private String getTeacherClassesAsString(String studentId) throws Exception {
		
		StringBuilder sb = new StringBuilder();
		
		List<String[]> teacherClassesBefore = pageHelper.getTeacherClasses(studentId);
		if(teacherClassesBefore!=null) {
			String[] classesT = new String[teacherClassesBefore.size()];
			sleep(1);
			
			for (int i=0;i<teacherClassesBefore.size();i++){
				classesT[i] = teacherClassesBefore.get(i)[1];
			}
			
			report.startStep("append classes count to one string with ; class count: " + teacherClassesBefore.size());
			if (teacherClassesBefore.size() > 1){
				sb.append(String.join(";", classesT));
				className = sb.toString();
			}
			else {
				className = classesT[0];
			}
		}	
		return className;
	}

	private String get2DiffExistingClasses(List<String[]> classes) {

		String className = "";
		Random random = new Random();
		int i = random.nextInt(classes.size());

		String className1 = classes.get(i)[0];
		String className2 = classes.get(i + 1)[0];

		className = className1 + ";" + className2;

		return className;
	}

	private String getClassFromClassesList(List<String[]> classes) {
		Random random = new Random();
		int classIndex = random.nextInt(classes.size());
		
		
		if(useMapTable || useLti) {
			className = classes.get(classIndex)[1];
			CanvasClassName = classes.get(classIndex)[2];
		//	internalClassName = classes.get(classIndex)[1];
			CanvasClassId = classes.get(classIndex)[2];
			
		}else {
			className = classes.get(classIndex)[0];
		}
		return className;
	}

	@Test
	@Category(SanityTests.class)
	@TestCaseParams(testCaseID = { "51304" }, testTimeOut = "10")
	public void testCanvasNewStudentNewClass() throws Exception {
		
		//institutionName = institutionsName[23];//18
		//pageHelper.restartBrowserInNewURL(institutionName, true);
		
		useNewClass = true;
		newStudent=true;
		useMapTable=true;
		userType = UserType.Student;
		canvasTest = true;
		runCanvasTest("NewStudentNewClass");
		
		report.startStep("Check Client type and Operating system");
		testResultService.assertEquals(true, dbService.verifyClientTypeAndOperatingSystemOfProgress(studentId));
	}

	@Test
	@TestCaseParams(testCaseID = { "51306" }, testTimeOut = "10")
	public void testCanvasNewStudentOldClass() throws Exception {
		//institutionName = institutionsName[20];
		//pageHelper.restartBrowserInNewURL(institutionName, true);

		newStudent= true;
		useUserAddress = true;
		userType = UserType.Student;
		useMapTable = true;
		canvasTest = true;
		runCanvasTest("NewStudentOldClass");
	}

	@Test
	@TestCaseParams(testCaseID = { "51306" }, testTimeOut = "10")
	public void testCanvasOldStudentOldClass() throws Exception {
		//useNewClass = true;
		useSecurityCheck = true;
		newStudent= false;
		useUserAddress = false;
		userType = UserType.Student;
		useMapTable = true;
		canvasTest = true;
		runCanvasTest("OldStudentOldClass");
	}

	@Test
	@TestCaseParams(testCaseID = { "51306" }, testTimeOut = "10")
	public void testCanvasOldStudentNewClass() throws Exception {

		useNewClass = true;
		useSecurityCheck = true;
		newStudent= false;
		useUserAddress = false;
		userType = UserType.Student;
		useMapTable = true;
		canvasTest = true;
		runCanvasTest("OldStudentNewClass");
	}
	
	
	@Test
	@TestCaseParams(testCaseID = { "51308" }, testTimeOut = "10")
	public void testCanvasNewTeacherNewClass() throws Exception {
		//institutionName = institutionsName[19];
		//pageHelper.restartBrowserInNewURL(institutionName, true);
		report.startStep("Init Test Data");
		// boolean useUserAddress = false;
		useNewClass = true;
		newTeacher = true;
		userType = UserType.Teacher;
		useMapTable = true;
		runCanvasTest("NewTeacherNewClass");
	}
	

	@Test
	@TestCaseParams(testCaseID = { "" }, testTimeOut = "10")
	public void testVerifyTeacherNotExceededNumberOfClasses() throws Exception {
		
	if (pageHelper.branchCI.equalsIgnoreCase("EDUI_CI_main")||pageHelper.branchCI.contains("EDUI_CI_RC") || pageHelper.branchCI.equalsIgnoreCase("EDUI_CI_dev")) {	
	report.startStep("Get external teacher classes");
		useMapTable = true;
		List<String[]>teachers = pageHelper.getExternalTeachersWithBigAmountOfClasses(5);
			
	report.startStep("If there is a teacher with big amount of classes, leave him with only two current classes");
		if(teachers!=null && teachers.size()>0) {
			unAssignTeacherClasses(teachers);
		}
		
	report.startStep("Verify that no any teacher left with big amout of classes");
		teachers = pageHelper.getExternalTeachersWithBigAmountOfClasses(5);
		textService.assertTrue("Some teachers still heave huge amount of classes", teachers==null);
		
	report.startStep("Get regular teacher classes");
		useMapTable = false;	
		teachers = pageHelper.getTeachersOfCertainInstitutionWithBigAmountOfClasses(5,"Autolaureate");
		if(teachers!=null && teachers.size()>0) {
			unAssignTeacherClasses(teachers);
		}
	report.startStep("Verify that no any teacher left with big amout of classes");
		teachers = pageHelper.getTeachersOfCertainInstitutionWithBigAmountOfClasses(5,"Autolaureate");
		textService.assertTrue("Some teachers still heave huge amount of classes", teachers==null||teachers.size()==1);
		
		}
	}	

	private void unAssignTeacherClasses(List<String[]> teachers) throws Exception {
		String teacherId = "";
		List<String[]> clases;
		StringBuilder sb = new StringBuilder();
		
		for(String[]arr:teachers) {
			teacherId = arr[0];
			List<String[]> userName = dbService.getUserNameAndPasswordByUserId(teacherId);
			sleep(1);
			String name = userName.get(0)[0];
			if(!name.equalsIgnoreCase("autoTeacher2")) {			
			if(useMapTable) {
				clases = dbService.getExternalTeacherClasses(teacherId);
				sleep(2);
			}else {
				clases = dbService.getTeacherClasses(teacherId);
				sleep(2);
			}
			sb.append(clases.get(0)[0]+";");
			sb.append(clases.get(1)[0]+";");
			dbService.assignTeacherToClasses(sb.toString(),teacherId);
			sleep(3);
			}
		}		
	}

	@Test
	@TestCaseParams(testCaseID = { "51308" }, testTimeOut = "10")
	public void testCanvasAssignMultipleClassesToTeacher() throws Exception {

		report.startStep("Init Test Data");
		institutionName = institutionsName[2];
		userType = UserType.Teacher;
		useMapTable = true;	
		report.startStep("Get the test date from the CSV file");
		sleep(2);
		List<String[]> listFromCsv = textService
				//.getStr2dimArrFromCsv("smb://frontqa2016//AutomationFiles//automation//CSV_files//MultipleClassToTeacher_data.csv", true);
			.getStr2dimArrFromCsv("\\\\"+configuration.getGlobalProperties("logserverName")+"\\automation\\CSV_files\\MultipleClassToTeacher_data.csv", false);
		
		for (int i = 3; i <= (listFromCsv.size() - 1); i++) { //

			useNewClass = Boolean.parseBoolean(listFromCsv.get(i)[0]);
			multipleClasses = Boolean.parseBoolean(listFromCsv.get(i)[1]);
			newTeacher = Boolean.parseBoolean(listFromCsv.get(i)[2]);

			report.startStep("Execution Id: " + i + ",New Teacher=" + newTeacher + " ,New Classes=" + useNewClass
					+ " ,MiltipleClasses=" + multipleClasses);

			runCanvasTest("AssignMultipleClassesToTeacher");
		}

	}

	@Test
	@TestCaseParams(testCaseID = { "56153,80938" }, testTimeOut = "10")
	public void testCanvasLoginRoleTeacherOldClass_Without_Address() throws Exception {
		
		userType = UserType.Teacher;
		useMapTable = true;
		useUserAddress = false;
		multipleClasses = false;
		report.startStep("Init Test Data");
		runCanvasTest("NewTeacherNoAddress");

	}

	@Test
	@TestCaseParams(testCaseID = { "56176,80937" }, testTimeOut = "10")
	public void testCanvasLoginRoleTeacher_Mentor() throws Exception {

		userType = UserType.Teacher;
		useMapTable = true;
		report.startStep("Init Test Data");
		runCanvasTest("Teacher and Mentor");
	}

	@Test
	@TestCaseParams(testCaseID = { "56190" }, testTimeOut = "10")
	public void testCanvasLoginRoleInstructor_Student() throws Exception {
		userType = UserType.Teacher;
		useMapTable = true;
		report.startStep("Init Test Data");
		runCanvasTest("Teacher and Student");

	}

	@Test
	@TestCaseParams(testCaseID = { "51306" }, testTimeOut = "10")
	public void testCanvasNewStudentWithMinus_OldClass() throws Exception {
		newStudent = true;
		useUserAddress = false;
		useNameUserWithMinus = true;
		userType = UserType.Student;
		useMapTable = true;
		canvasTest = true;
		runCanvasTest("NewStudentOldClass");

	}
	
	@Test
	//@Category(SanityTests.class)
	@TestCaseParams(testCaseID = { "" }, testTimeOut = "10")
	public void testRegularReLoginAfterClosingBrowserByPOST() throws Exception {

		report.startStep("Init Test Data");
		institutionName = institutionsName[0];//0
		pageHelper.initializeData();
		useAssignedClassToUser = true;
		String createClass = "N"; // Y/N
		String action = "U"; // i = Insert U= Update
		String TestFileName = "";
		String userType = "";
		useMapTable = false;
		loginPage = new NewUXLoginPage(webDriver, testResultService);
/*
		report.startStep("Store remaining licenses before creating new user");
		baseLineLicense = dbService.getValidInstitutionPackages(institutionId);
*/
		for(int i = 0; i<2; i++) {
		
			if(i==0) {
				report.startStep("Iteration 1, student scenario");
				userType = "S"; // S = student T= Teacher
				TestFileName = "RegUser_ExistStudent_ExistClass_" + dbService.sig(4)+".html";
			}
			if(i==1) {
				report.startStep("Iteration 2, teacher scenario");
				userType = "T"; // S = student T= Teacher
				TestFileName = "RegUser_ExistTeacher_ExistClass_" + dbService.sig(4)+".html";
			}
			//createRegularRegUserAndLoginFile(createClass, creaTenewUser, userType, TestFileName);
			//report.startStep("Get institution Remaining License for all Valid Packages");
				//List<String[]> getValidInstitutionPackages = dbService.getValidInstitutionPackages(institutionId);

			report.startStep("Create html file and login");
			createRegUserAndLoginFileAndReturnURL(createClass, action, userType, TestFileName);
			//NewUxHomePage homePage = new NewUxHomePage(webDriver, testResultService);
	
			if(userType.equals("S")) {
				skipAllEntrancesAndVerifyStudentName();
				//verifyLicencesBurnedAfterLogin(getValidInstitutionPackages);
/*
				report.report("Verify License after adding and login student");
				currentLicense = dbService.getRemainingLicensesOfCertainPackage(baseLineLicense, institutionId);
				pageHelper.validateLicensesAfterUserAddedOrLoggedIn(baseLineLicense,currentLicense,1,1);
*/
				report.startStep("Closing browser without logout and open new session");
				closeBrowserAndInitilizeNewSession(regUserUrl);
				webDriver.waitUntilElementAppears(webDriver.waitForElement("btnSubmit", ByTypes.name), 20);
				webDriver.waitForElement("btnSubmit", ByTypes.name).click();
				sleep(2);

				loginPage.verifyAndConfirmAlertMessage();
				homePage.waitHomePageloadedFully();
				skipAllEntrancesAndVerifyStudentName();
/*
				report.report("Verify License after relogin student");
				currentLicense = dbService.getRemainingLicensesOfCertainPackage(classPackages, institutionId);
				pageHelper.validateLicensesAfterUserAddedOrLoggedIn(baseLineLicense,currentLicense,0,0);
*/
				homePage.clickOnLogOut();
			}
			if(userType.equals("T")) {
				verifyTeacherLogedInToTms();
			//	verifyLicencesBurnedAfterLogin(getValidInstitutionPackages);

			//	report.report("Verify License after importing students");
			//	currentLicense = dbService.getRemainingLicensesOfCertainPackage(classPackages, institutionId);
			//	pageHelper.validateLicensesAfterUserAddedOrLoggedIn(baseLineLicense,currentLicense,0,0);

				closeBrowserAndInitilizeNewSession(regUserUrl);
				webDriver.waitUntilElementAppears(webDriver.waitForElement("btnSubmit", ByTypes.name), 20);
				webDriver.waitForElement("btnSubmit", ByTypes.name).click();
				sleep(2);

				WebElement progressBar = loginPage.getLoginProgressBarElement();
				String fullMessage = progressBar.getText();
				//String statusLogin = fullMessage.toString().split(".")[0];
				assertEquals("Please wait while you are being logged into the system.\nThis may take a few minutes.", fullMessage);
				verifyAndWaitWhileDelayedLoginProgressBar();
				//sleep(120);
				verifyTeacherLogedInToTms();

			//	report.report("Verify License after importing students");
			//	currentLicense = dbService.getRemainingLicensesOfCertainPackage(classPackages, institutionId);
			//	pageHelper.validateLicensesAfterUserAddedOrLoggedIn(baseLineLicense,currentLicense,0,0);

			}
		}
	}

	private void verifyAndWaitWhileDelayedLoginProgressBar() {
		
		WebElement progressBar=null;
		try {
			progressBar = loginPage.getLoginProgressBarElement();
			
			if (progressBar!=null){
				String fullMessage = progressBar.getText();
				assertEquals("Please wait while you are being logged into the system.\nThis may take a few minutes.", fullMessage);
				
				for (int i=0;i<120 && progressBar!=null;i++){
					progressBar = loginPage.getLoginProgressBarElement();
					sleep(1);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			}
	}
	
	private void skipAllEntrancesAndVerifyStudentName() throws Exception {
		pageHelper.skipOptin();
		homePage.closeAllNotifications();
		pageHelper.skipOnBoardingHP();
		webDriver.switchToMainWindow();
		pageHelper.closeLastSessionImproperLogoutAlert();
		testResultService.assertEquals("Hello " + userFN + "", homePage.getUserDataText(),
				"First Name in Header of Home Page not match");
	}

	private void closeBrowserAndInitilizeNewSession(String url) throws Exception {
		if(oldTE) {
		closeAllWindows();
		}
		else {
		webDriver.closeBrowser();
		}
		webDriver.init();
		webDriver.maximize();
		webDriver.openUrl(url);
	}

	private void closeAllWindows() {
		Set<String> windows = webDriver.getWebDriver().getWindowHandles();
	if(windows != null) {
		for(String s:windows) {
			webDriver.getWebDriver().switchTo().window(s).close();
		}
	}
	}
	private void createRegUserAndLoginFileAndReturnURL(String createClass, String createUser, String userType,
			String TestFileName) throws Exception {
		String baseUrl = pageHelper.CILink.split(".com")[0] + ".com/";
		String accessUrl = pageHelper.urlForExternalPages.split(".com")[0] + ".com/languages/";
		String chkPath = pageHelper.buildPathForExternalPages + "Languages/";
		List<String[]> classes = null;

		if (createUser.contains("I")) {

			if (useNameUserWithMinus)
				userName = userType + "-" + dbService.sig(5);
			else
				userName = userType + dbService.sig(5);

			userFN = userFN + userName;
			userLN = userLN + userName;
			email = userName + new Random().nextInt(1000) + "@gmail.com";
		} 
		else {
			if(userType.equals("T")) {
				studentId = getExistingUser("teacher");
				className = getTeacherClassesAsString(studentId);
				if(className.isEmpty()) {
					className = getClassFromClassesList(pageHelper.getclassesByInstitutionName(institutionName));
				}
			}
			else {
				studentId = getExistingUser("student");
				List<String[]>allClassesOfInstitution = pageHelper.getclassesByInstitutionName(institutionName);
				className = getClassFromClassesList(allClassesOfInstitution);
			//	classPackages = dbService.getClassAssignPackagesNew(className, institutionId);
			}
		}


		report.startStep("Create test file -- " + TestFileName);
		// .....................................................
		List<String> wList = new ArrayList<String>();

		wList = createHtmlTitle(wList);
		wList = createFormInfoRegularRegUserAndLogin(wList, baseUrl, institutionName, className, userName, userFN,
				userLN, createClass, createUser, userType);
		wList = createHtmlTail(wList);

		textService.writeListToSmbFile(chkPath, TestFileName, wList, useSMB);

		String openURL = accessUrl + TestFileName;
		pageHelper.closeBrowserAndOpenInCognito(false);
		webDriver.openUrl(openURL);
		webDriver.waitUntilElementAppears(webDriver.waitForElement("btnSubmit", ByTypes.name), 20);
		webDriver.waitForElement("btnSubmit", ByTypes.name).click();
		sleep(2);

		pageHelper.skipOptin();
		if (!useNameUserWithMinus) {
			studentId = dbService.getUserIdByUserName(userName, institutionId);
	
			if (newTeacher || newStudent) {
				if (studentId != null)
					dbService.insertUserToAutomationTable(institutionId, studentId, userName, className,
						pageHelper.buildId);
				else
					testResultService.addFailTest("User not created", true, true);
			}
		}
		regUserUrl = openURL;
		
	}

	@Test
	@TestCaseParams(testCaseID = { "51306" }, testTimeOut = "10")
	public void testRegUser_NewStudent_NewClassByPOST() throws Exception {

		report.startStep("Init Test Data");
		institutionName = institutionsName[0];//0
		pageHelper.initializeData();

		String createClass = "Y"; // Y/N
		String createNewUser = "I"; // i = Insert U= Update
		String userType = "S"; // S = student T= Teacher
		String TestFileName = "RegUser_NewStudent_NewClass" + dbService.sig(4) + ".html";
		newStudent = true;
		useNewClass = true;
		useMapTable = false;
		useLti = false;
		
		createRegularRegUserAndLoginFile(createClass, createNewUser, userType, TestFileName);

		NewUxHomePage homePage = new NewUxHomePage(webDriver, testResultService);

		homePage.closeAllNotifications();
		sleep(1);
		pageHelper.skipOnBoardingHP();
		webDriver.switchToMainWindow();
		pageHelper.closeLastSessionImproperLogoutAlert();
		
		testResultService.assertEquals("Hello " + userFN + "", homePage.getUserDataText(),
				"First Name in Header of Home Page not match");
	}

	@Test
	@TestCaseParams(testCaseID = { "84929" }, testTimeOut = "10")
	public void testRegularRegUserAndLogin_OldTeacher_OldClassesByPOST() throws Exception {

		report.startStep("Init Test Data");
		institutionName = institutionsName[0];
		pageHelper.initializeData();

		String createClass = "N"; // Y/N
		String creaTenewUser = "U"; // i = Insert U= Update
		String userType = "T"; // S = student T= Teacher
		String TestFileName = "RegUser_OldTeacher_OldClasses_" + dbService.sig(4)+ ".html";
		// String className="";
		multipleClasses = true;

		createRegularRegUserAndLoginFile(createClass, creaTenewUser, userType, TestFileName);

		report.startStep("Check User Name on Home Page after Login");
		tmsHome = new TmsHomePage(webDriver, testResultService);
		tmsHome.switchToMainFrame();
		tmsHome.checkUserDetails(userFN, userLN);
		tmsHome.clickOnRegistration();
		sleep(1);
		tmsHome.clickOnClasses();

		// get Teacher classes from DB
		List<String[]> teacherClasses = pageHelper.getTeacherClasses(studentId);

		// get test class name;
		String[] classs = new String[teacherClasses.size()];

		if (className.contains(";"))
			classs = className.split(";");
		else
			classs[0] = className;

		for (int i = 0; i < teacherClasses.size(); i++) {
			checkTeacherAssignedToClass(classs[i], teacherClasses);
			tmsHome.checkClassNameIsDisplayed(classs[i]);
		}
	}
	
	

	@Test
	@TestCaseParams(testCaseID = { "51306" }, testTimeOut = "10")
	public void testCanvasOutComeAndSourceId() throws Exception {

		useUserAddress = true;
		outComeAndSourceId = true;
		newStudent=true;
		useMapTable=true;
		userType = UserType.Student;
		canvasTest = true;
		runCanvasTest("WithOutComeAndSourceId");
	}
	
	//@Category(SanityTests.class)
	@Test
	@TestCaseParams(testCaseID = { "" }, testTimeOut = "10")
	public void testCanvasLtiCustomize() throws Exception {

		institutionName = institutionsName[8];
		pageHelper.initializeData();
	  
		//institutionId = dbService.getInstituteIdByName(institutionName);
		//institutionName = "qailp";
		 //pageHelper.initializeData();
		useSecurityCheck = true;
		pageHelper.restartBrowserInNewURL(institutionName, true);
		useNewClass = false;
		teacher = false;
		newTeacher = false;
		newStudent = true;
		student = true;
		canvasTest = true;
		firstNameKey = "FN";
		lastNameKey = "LN";
		userNameKey = "UN";
		mailKey = "Soap";
		classNameKey = "Context_Label";
		userType = UserType.Student;
		useMapTable = true;
		useUserAddress = false;
		outComeAndSourceId = true;
		runCanvasTest("ltiCustomize");
		
	//	report.startStep("Check Client type and Operating system");
	//	testResultService.assertEquals(true, dbService.verifyClientTypeAndOperatingSystemOfProgress(studentId));
	}
	
	
	@Test
	@TestCaseParams(testCaseID = { "" }, testTimeOut = "10")
	public void testCanvasLtiCustomizeLoginAfterClosingBrowser() throws Exception {

		institutionName = institutionsName[8];
		institutionId = dbService.getInstituteIdByName(institutionName);

		// pageHelper.initializeData();
		// pageHelper.restartBrowserInNewURL(institutionName, true);
		List<String[]> listFromCsv = textService
				//.getStr2dimArrFromCsv("smb://"+configuration.getGlobalProperties("logserverName")+"//automation//CSV_files//LtiCustomize.csv", true);
		.getStr2dimArrFromCsv("\\\\"+configuration.getGlobalProperties("logserverName")+"\\automation\\CSV_files\\LtiCustomize.csv", false);
		for (int i = 1; i <= (listFromCsv.size() - 5); i++) {
			useNewClass = Boolean.parseBoolean(listFromCsv.get(i)[0]);
			teacher = Boolean.parseBoolean(listFromCsv.get(i)[1]);
			newTeacher = Boolean.parseBoolean(listFromCsv.get(i)[2]);
			newStudent = Boolean.parseBoolean(listFromCsv.get(i)[3]);
			student = Boolean.parseBoolean(listFromCsv.get(i)[4]);

			report.startStep("Execution Id: " + i + ",New Teacher = " + newTeacher + " ,New Classes = " + useNewClass
					+ " , Teacher = " + teacher +", New student = "+newStudent+",Student = "+student);

		//	UserType userType = null;
			if (student)
				userType = UserType.Student;

			if(teacher)
				userType = UserType.Teacher;

			firstNameKey = "FN";
			lastNameKey = "LN";
			userNameKey = "UN";
			mailKey = "Soap";
			classNameKey = "Context_Label";
			canvasTest = true;
			useMapTable = true;
			useUserAddress = false;
			outComeAndSourceId = true;
			runCanvasTestLtiCustomize("ltiCustomize");

			if(student) {
				oldTE = true;
				report.startStep("Closing browser without logout and open new session");
				closeBrowserAndInitilizeNewSession(regUserUrl);
				webDriver.waitUntilElementAppears(webDriver.waitForElement("btnSubmit", ByTypes.name), 20);
				webDriver.waitForElement("btnSubmit", ByTypes.name).click();
				sleep(2);
				loginPage.verifyAndConfirmAlertMessage();

				skipAllEntrancesAndVerifyStudentName();
			}
			if(teacher){
				closeBrowserAndInitilizeNewSession(regUserUrl);
				webDriver.waitUntilElementAppears(webDriver.waitForElement("btnSubmit", ByTypes.name), 20);
				webDriver.waitForElement("btnSubmit", ByTypes.name).click();
				sleep(2);

				WebElement progressBar = loginPage.getLoginProgressBarElement();
				String fullMessage = progressBar.getText();
				assertEquals("Please wait while you are being logged into the system.\nThis may take a few minutes.", fullMessage);
				verifyAndWaitWhileDelayedLoginProgressBar();
				verifyTeacherLogedInToTms();
			}
		}
	}

	private void runCanvasTestLtiCustomize(String testType) throws Exception {
		String typeOfUser = userType.name();//.toString();
		String baseUrl = pageHelper.CILink.split(".com")[0] + ".com/";
		String accessUrl = pageHelper.urlForExternalPages.split(".com")[0] + ".com/";
		String teacherId = "";

		String chkPath = pageHelper.buildPathForExternalPages + "Languages/";

		if (useNameUserWithMinus) {

			if (typeOfUser.contains("Teacher")) {
				getUser(5);
			}

			if (typeOfUser.contains("Student")){
				userName = "stud-" + dbService.sig(5);
				userFN = userFN + " " + userName;
				userLN = userLN + " " + userName;
				email = userName + "r-5" + "@gmail.com";
			}
		}

		else {
			if (typeOfUser.contains("Teacher"))

				if (newTeacher) {
					getUser(5);// "52332380012289";

				} else {
					teacherId = getExistingUser("teacher");
				}

			else {

				if (newStudent)
					getUser(5);

				else {
					studentId = getExistingUser("student");
				}
			}
		}

		if (useNewClass) {
			createNewClasses();
			
		} else {
			retrieveExistingClasses();
		}



		report.startStep("Store remaining licenses before creating new user");
		//List<String[]> baseLineLicense = new ArrayList<>();
		classPackages = dbService.getClassAssignPackagesNew(className, institutionId);
		if (classPackages==null)
			baseLineLicense = dbService.getValidInstitutionPackages(institutionId);
		else
			baseLineLicense = dbService.getRemainingLicensesOfCertainPackage(classPackages, institutionId);


		report.startStep("Create test file -- " + typeOfUser + ".html");
		String testFile = createTestCanvasFile(chkPath, baseUrl, testType);

		if (!testFile.equals("")) {
			report.startStep("Create and open URL");
			accessUrl = accessUrl + "Languages/" + testFile;
			pageHelper.closeBrowserAndOpenInCognito(false);
			webDriver.openUrl(accessUrl);
			regUserUrl = accessUrl;

			report.startStep("Verify user data according on type Of User");
			try {
				verifyUsersData(testFile, teacherId, baseUrl);

			} catch (Exception e) {
				e.printStackTrace();
			}
			// finally
			// {
			// cleanTmpInfo(CanvasClassName, userName);
			// }
		}

		report.report("Verify License after add user");
		currentLicense = dbService.getRemainingLicensesOfCertainPackage(baseLineLicense, institutionId);
		pageHelper.validateLicensesAfterUserAddedOrLoggedIn(baseLineLicense,currentLicense,0,0);
		
	}

	@Test
	@TestCaseParams(testCaseID = { "51306" }, testTimeOut = "10")
	public void testRegularRegUserAndLogin_UserNameWithMinusByPOST() throws Exception {

		report.startStep("Init Test Data");
		institutionName = institutionsName[5];
		pageHelper.initializeData();

		String createClass = "Y"; // Y/N
		String creaTenewUser = "I"; // i = Insert U= Update
		String userType = "S"; // S = student T= Teacher
		String TestFileName = "RegUser_NewStudent_NewClass_" + dbService.sig(4) + ".html";
		// String className="";
		useNameUserWithMinus = true;

		String studentUserName = createRegularRegUserAndLoginFile(createClass, creaTenewUser, userType, TestFileName);

		NewUxHomePage homePage = new NewUxHomePage(webDriver, testResultService);

		report.startStep("Verify The corret Error returned to user");
		// String expectedString = "Your user name or password is incorrect";
		// //"Error=5"
		String expectedString = "Error=5";

		report.startStep("The system erro is " + homePage.getRegUserAndLoginError());
		testResultService.assertEquals(true, homePage.getRegUserAndLoginError().contains(expectedString),
				"System didn't block user with Minus");

		report.startStep("Verify User Not created in DB");
		if (dbService.getUserIdByUserName(studentUserName, institutionId) != null)
			testResultService.addFailTest("User created while it should be not created", false, true);

		// testResultService.assertEquals(null,
		// dbService.getUserIdByUserName(studentUserName, institutionId));

	}

	@Test
	@TestCaseParams(testCaseID = { "51306" }, testTimeOut = "10")
	public void testMoodleLtiNewStudent_OldClass() throws Exception {

		// useUserAddress = true;
		// outComeAndSourceId=true;

		institutionName = institutionsName[2];//2
		pageHelper.closeBrowserAndOpenInCognito(false);
		pageHelper.initializeData();

		String baseUrl = pageHelper.CILink.split(".com")[0] + ".com/";
		String accessUrl = pageHelper.urlForExternalPages.split(".com")[0] + ".com/Languages/";
		String chkPath = pageHelper.buildPathForExternalPages + "Languages/";
		//String userName = ""; // "stud" + dbService.sig(5);
		String roles = "Learner";
		String testFile = "LtiNewStudent_OldClassMoodle" + dbService.sig(4) + ".html";
		boolean moodleMode = true;
		newStudent = true;
		useMapTable = true;
		userType = UserType.Student;
		//canvasTest = true;
		//useSecurityCheck = true;

		report.startStep("Prepare class for student HTML");
		Character newClass = assignNewOrCurrentClassesWithAmount(classAssignmnetAmount, userType);

		report.startStep("Get institution Remaining License for classAssignedPackages");
		List<String[]> getValidInstitutionPackages = dbService.getValidInstitutionPackages(institutionId);
		/*List<String[]> classPackages = dbService.getClassAssignPackagesNew(className, institutionId);
		List<String[]> remainingLicensesBeforeAddingUser = new ArrayList<>();
		if (classPackages!=null) {
			remainingLicensesBeforeAddingUser = dbService.getRemainingLicensesOfCertainPackage(classPackages, institutionId);
		}else {
			System.out.println("<==================!!! Class "+className+" HAS NO PACKAGES !!!====================>");
		}*/

		report.startStep("Create moodle HTML for entrance");
		userName = pageHelper.createMoodlePostList(institutionName, baseUrl, className, CanvasClassId, roles,
				chkPath, testFile,institutionId, true);

		pageHelper.accessViaHtmlFileAndVerifyTheLoginProcess(accessUrl + testFile, userName, userName + " Family",
				userName + " Given", className, CanvasClassId, moodleMode,useMapTable);

		report.startStep("Check institution Remaining License reduced after login");

		//pageHelper.checkRemaingLicense(getValidInstitutionPackages,className,institutionId,newStudent,useNewClass);
		if (getValidInstitutionPackages!=null) {
			verifyLicencesBurnedAfterLogin(getValidInstitutionPackages);
		}
		/*if (classPackages!=null) {
			if (newStudent && newClass.toString().equalsIgnoreCase("N")) {
				List<String[]> remainingLicensesAfterLogin = dbService.getRemainingLicensesOfCertainPackage(classPackages, institutionId);
				pageHelper.validateLicensesReducesAfterNewStudentAdded(remainingLicensesBeforeAddingUser, remainingLicensesAfterLogin);
			} else
				pageHelper.checkRemainingLicenseBurnForallValidPackages(remainingLicensesBeforeAddingUser, institutionId, className,true);
		}*/
	}


	@Test
	//@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "51308" })
	public void testCanvasNoMap_YTeacherYClass() throws Exception {

		//if (!PageHelperService.branchCI.contains("RC"))
		//{
				
		report.startStep("Init Test Data");
		institutionName = institutionsName[17];
		pageHelper.initializeData();
		pageHelper.closeBrowserAndOpenInCognito(false);
		
		useNewClass = true;
		newTeacher = true;
		userType = UserType.Teacher;
		useMapTable = false;
		runCanvasTest("NewTeacherNewClass");
		
		report.startStep("Check User:" +studentId+ " ,ExtUserName: "+userName+" ,not inserted to map table");
		verifyUserNotInsertedToMapTable(studentId);
		
		report.startStep("Check User assigned to correct class:" +className+",ExtCanvasClassId:" + CanvasClassId);
		pageHelper.checkTeacherAssignedToclass(studentId, className,institutionId);
		
		//}
	}
	
	@Test
	@TestCaseParams(testCaseID = { "51308" })
	public void testCanvasNoMap_NTeacherNClass() throws Exception {

		//if (!PageHelperService.branchCI.contains("RC"))
		//{
			report.startStep("Init Test Data");
			institutionName = institutionsName[17];
			pageHelper.initializeData();
			pageHelper.closeBrowserAndOpenInCognito(false);
			
			useLti = true;
			useNewClass = false;
			newTeacher = false;
			useMapTable = false;
			userType = UserType.Teacher;
			runCanvasTest("NTeacherNClass");
			
			report.startStep("Check User assigned to correct class name: " +className+" and ExtCanvasClassId: " + CanvasClassId);
			pageHelper.checkTeacherAssignedToclass(studentId, className,institutionId);
			
			//report.startStep("Check User:" +studentId+ " ,ExtUserName: "+userName+" ,not inserted to map table");
			//verifyUserNotInsertedToMapTable(studentId);
			
		//}
	}

	
	@Test
	//@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "51308" })
	public void testCanvasNoMap_YStudentYClass() throws Exception {

		//if (!PageHelperService.branchCI.contains("RC"))
				//(PageHelperService.branchCI.toLowerCase().contains("main") || PageHelperService.branchCI.toLowerCase().contains("rc")))
		//{
			report.startStep("Init Test Data");
			institutionName = institutionsName[17];
			pageHelper.initializeData();
			pageHelper.closeBrowserAndOpenInCognito(false);
			
			useNewClass = true;
			newStudent = true;
			userType = UserType.Student;
			useMapTable = false;
			canvasTest = false;
			useUserAddress = false;
			runCanvasTest("NewStudentNewClass");
			
			report.startStep("Check User Name on Home Page after Login");
			testResultService.assertEquals(true, homePage.getUserDataText().contains(userName),
					"User Name not found on home page");
			
			report.startStep("Check User:" +studentId+ " ,ExtUserName: "+userName+" ,not inserted to map table");
			verifyUserNotInsertedToMapTable(studentId);
			
			report.startStep("Check User assigned to correct class:" +className+",ExtCanvasClassId:" + CanvasClassId);
			pageHelper.checkUserAssignedToclass(studentId, className,institutionId);
		//}
	}
	
	@Test
	@TestCaseParams(testCaseID = { "51308" })
	public void testCanvasNoMap_NStudentNClass() throws Exception {

		//if (!PageHelperService.branchCI.contains("RC"))
		//{
			report.startStep("Init Test Data");
			institutionName = institutionsName[17];
			pageHelper.initializeData();
			pageHelper.closeBrowserAndOpenInCognito(false);
			
			useNewClass = false;
			newStudent = false;
			userType = UserType.Student;
			useLti = true;
			useMapTable = false;
			runCanvasTest("NStudentNClass");
				
			report.startStep("Check User Name on Home Page after Login");
			homePage.waitHomePageloaded();
			testResultService.assertEquals(true, homePage.getUserDataText().contains(userName),
					"User Name not found on home page");
			
			report.startStep("Check User assigned to correct class:" +className+",ExtCanvasClassId:" + CanvasClassId);
			verifyUserInsertedToNewClassCreated(studentId);
			
			//report.startStep("Check User:" +studentId+ " ,ExtUserName: "+userName+" ,not inserted to map table");
			//verifyUserNotInsertedToMapTable(studentId);
		//}
	}
	

	private void verifyUserInsertedToNewClassCreated(String studentId) {
		
		try {
			report.startStep("Check user assigned to new class created");
		
		testResultService.assertEquals(true,
				pageHelper.isUserAssignedToClass(studentId, dbService.getClassIdByName(className, institutionId)),
				"Class not created or user not assigned to class");
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void verifyUserNotInsertedToMapTable(String studentId) throws Exception{
		
		String userExists = dbService.getUserNameByUserIdFromUsersExternalMap(studentId);
		
		try {
			testResultService.assertEquals(false,(userExists != null)
					,"User: " +studentId+ " ,created in Map table");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void runCanvasTest(String testType) throws Exception {

		String baseUrl = pageHelper.CILink.split(".com")[0] + ".com/";
		String accessUrl = pageHelper.urlForExternalPages.split(".com")[0] + ".com/";
		String teacherId = "";
		String chkPath = pageHelper.buildPathForExternalPages + "Languages";
							
		if (useNameUserWithMinus) {

			if (testType.contains("Teacher")) {
				getUser(5);
			}

			if (testType.contains("Student")){
				userName = "stud-" + dbService.sig(5);
				if(useMapTable) {
					externalUserName = userName;
				}
				userFN = userFN + " " + userName;
				userLN = userLN + " " + userName;
				email = userName + "r-5" + "@gmail.com";
			}
		}

		else {
			if (testType.contains("Teacher"))
			//if (userType.name().contains("Teacher"))
				if (newTeacher) {
					getUser(5);// "52332380012289";

				} else {
					teacherId = getExistingUser("teacher");
					//sleep(2);
				}

			else {

				if (newStudent)
					getUser(5);

				else {
					studentId = getExistingUser("student");
				}
			}
		}

		if (useNewClass) {
			createNewClasses();
			
		} else {
			retrieveExistingClasses();
		}

		report.startStep("Create test file -- " + testType + ".html");
		String testFile = createTestCanvasFile(chkPath, baseUrl, testType);

		if (!testFile.equals("")) {

			//report.startStep("Get institution Remaining License for all Valid Packages");
			//List<String[]> getValidInstitutionPackages = dbService.getValidInstitutionPackages(institutionId);

			report.startStep("Get institution classes packages licenses before creating new user");
			//List<String[]> baseLineLicense = new ArrayList<>();
			classPackages = dbService.getClassAssignPackagesNew(className, institutionId);

			if (classPackages==null)
				baseLineLicense = dbService.getValidInstitutionPackages(institutionId);
			else
				baseLineLicense = dbService.getRemainingLicensesOfCertainPackage(classPackages, institutionId);


			report.startStep("Create and open URL");
			accessUrl = accessUrl + "Languages/" + testFile;
		//	pageHelper.closeBrowserAndOpenInCognito(false);
			webDriver.openUrl(accessUrl);
		//	report.addTitle(accessUrl);

			report.startStep("Verify user data according on type Of User");
				try {
					verifyUsersData(testFile, teacherId, baseUrl);

				if (baseLineLicense!=null) {
					report.startStep("Verify Licenses after login");
						verifyLicencesBurnedAfterLogin(baseLineLicense);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
	}

	private void verifyLicencesBurnedAfterLogin(List<String[]> getValidInstitutionPackages) throws Exception {
		try {
			currentLicense = dbService.getRemainingLicensesOfCertainPackage(getValidInstitutionPackages, institutionId);
			if (newStudent) {
				pageHelper.validateLicensesAfterUserAddedOrLoggedIn(getValidInstitutionPackages,currentLicense,1,1);

				/*} else {
					if (classPackages!=null) {
						//List<String[]> classPackages = dbService.getClassAssignPackagesNew(className, institutionId);
						List<String[]> remainingLicensesAfterLogin = dbService.getRemainingLicensesOfCertainPackage(classPackages, institutionId);
						pageHelper.validateLicensesReducesAfterNewStudentAdded(getValidInstitutionPackages, remainingLicensesAfterLogin);
					}
				}*/
			} else {
				//pageHelper.checkRemainingLicenseBurnForallValidPackages(getValidInstitutionPackages, institutionId, className,false);
				pageHelper.validateLicensesAfterUserAddedOrLoggedIn(getValidInstitutionPackages,currentLicense,0,0);
			}
		}catch (Exception|AssertionError err){
			System.out.println(err.getMessage());
		}
	}

	private void verifyUsersData(String testFile, String teacherId, String baseUrl) throws Exception {

		webDriver.waitUntilElementAppearsAndReturnElementByType("btnSubmit", ByTypes.name, 10); // webDriver.waitForElement("btnSubmit",ByTypes.name);
		webDriver.waitForElement("btnSubmit", ByTypes.name).click();
		sleep(2);
		
	//webDriver.refresh(); // have to be remove after Ilya fix
	//	sleep(2);
		pageHelper.skipOptin();
		homePage.closeAllNotifications();
		pageHelper.skipOnBoardingHP();
		
		if(newStudent || newTeacher) {
			
			if(useMapTable) 
				studentId = dbService.getExternalUserInternalId(userName,institutionId);
			else 
				studentId = dbService.getUserIdByUserName(userName, institutionId);
			
			if (studentId==null)
				testResultService.addFailTest("UserId not retrived from DB, UserName is: "+userName+" ", false, false);
			
			teacherId = studentId;
		
			if (studentId != null) { // userId exists created or return
				//	if (newStudent || newTeacher) { 
					report.startStep("Insert new user into table");
					teacherId = insertNewUserIntoTable(baseUrl);
			}
		}
		
		if (useNewClass)
			insertNewClassIntoAutomationTable(baseUrl);
			
		//sleep(2);
		
		if (testFile.contains("Teacher")) {
			verifyFirstAndLastName();
			verifyThatTeacherAssignedToClass(teacherId);
				// cleanGlobalValues();
		}			
		
		if (userType.name().equalsIgnoreCase("Student"))
 				verifyUserDataInProfile();
			
		if (useUserAddress) {
				verifyUserAddressIsMatch();

			}
		
		//else {
		//	testResultService.addFailTest("User Doesn't create", false, true);
	//	}
	}

	private void verifyUserAddressIsMatch() throws Exception {

		report.startStep("Verify the UserAddress is match");
		String actualAddress = pageHelper.checkUserAddressIsExist(studentId);
		testResultService.assertEquals(randomUserAddress, actualAddress);
		String userCountryId = dbService.getUserCountryIdbyUserId(studentId);
		String institutionCountryId = dbService.getInstitutionCountryId(institutionId);
		testResultService.assertEquals(institutionCountryId, userCountryId, "User Country Id is not match");

		report.startStep("Verify the other User Address not overridden");
		String[] usersResult = pageHelper.getUserWithUserAddress(institutionId, studentId);
		testResultService.assertEquals(false, randomUserAddress.equalsIgnoreCase(usersResult[1]));

	}

	private void verifyUserDataInProfile() throws Exception {

		report.startStep("Validate Data is Displayed Correctly in My Profile");
		homePage.waitHomePageloaded();
		if (newStudent) {
			while (!homePage.myProfileIsClickable()) {
				homePage.clickOnRightArrow();
			}
		}

		homePage.clickOnMyProfile();
		sleep(3);
		homePage.switchToMyProfile();
		NewUxMyProfile myProfile = new NewUxMyProfile(webDriver, testResultService);

		testResultService.assertEquals(userLN, myProfile.getLastName(), "Last Name in My Profile is Incorrect.");
		testResultService.assertEquals(userFN, myProfile.getFirstName(), "First Name in My Profile is Incorrect.");

		if (newStudent || newTeacher) // The email for Exists user not updated Bug Id 95677
			testResultService.assertEquals(email, myProfile.getMail(), "Mail in My Profile is Incorrect.");

		report.startStep("Validate User Data stored in DB correctly");
		String userIdMyProfile = myProfile.getUserName();

		List<String[]> classDB = null;
		boolean newClassPresent = false;
		if (useMapTable) {
			if (useNewClass) {
				
				if (userType.name().equalsIgnoreCase("Teacher")){
					List<String[]> classes = dbService.getTeacherClasses(studentId);
					for (int i = 0; i < classes.size(); i++) {
						if (classes.get(i)[0].equals(className))
							newClassPresent = true;
					}
					testResultService.assertEquals(true, newClassPresent, "Class is not present");
				}
				
			} 
			//externalClassName
			if (userType.name().equalsIgnoreCase("Student")) {
				report.startStep("Validate User's Class data stored in DB correctly");
				classDB = dbService.getClassFromClassExternalMapByUserId(userIdMyProfile);// HERE IS EXCEPTION
				int index = 1;
				//if(canvasTest) {
				//	index = 2;
				//}
				if(!useNewClass) {
					testResultService.assertEquals(CanvasClassId, classDB.get(0)[index+1], "External Class ID is Incorrect.");
				}
				testResultService.assertEquals(className, classDB.get(0)[index],"Class Name is Incorrect.");
			}

		}

		else {
			String userNameMyProfile = myProfile.getUserName();
			report.startStep("Validate User's Class data stored in DB correctly");
			classDB = dbService.getUserClassIdAndNamebyUserId(studentId);
			testResultService.assertEquals(true, classDB != null, "Student not asssigned to classId.");
			testResultService.assertEquals(userName, userNameMyProfile, "User name in My Profile is Incorrect.");
		}

		myProfile.close();

		webDriver.switchToMainWindow();
		webDriver.switchToTopMostFrame();

		testResultService.assertEquals("Hello " + userFN, homePage.getUserDataText(),
				"Verify First Name in Header of Home Page");

		// add verification no assigned course on home page and no assign screen display
		//dbService.checkAndturnOffTestEnviormentFlag(userIdMyProfile);
		//boolean newTestEnv = pageHelper.checkTestEnvironmentFlag(institutionId);
		//if(newTestEnv==false) {
		//	homePage.validateUnAssignedCoursesMessage();
		//	if (userType.name().equalsIgnoreCase("Student"))
		//	verifyOldPltOpened();
		//}	
	}

	private void verifyThatTeacherAssignedToClass(String teacherId) throws Exception {
		report.startStep("Check teacher assigned to class");
		// get Teacher classes from DB
		List<String[]> teacherClasses = null;
		String ExternalclassId = "";
		
		if(useMapTable) {
			teacherClasses = dbService.getExternalTeacherClasses(teacherId);
		}else {
		 teacherClasses = pageHelper.getTeacherClasses(teacherId);
		// String [] assignedClasses = className.split(";");
		}
		
		for (int i = 0; i < teacherClasses.size(); i++) {
			// for (int i = 0; i < assignedClasses.length; i++) {
			// checkTeacherAssignedToClass(assignedClasses[i], teacherClasses);
			// checkTeacherAssignedToClass(teacherClasses.get(i)[0], teacherClasses);
			
			if(useMapTable) {
				tmsHome.checkClassNameIsDisplayed(teacherClasses.get(i)[1]);
			}
			else {
				tmsHome.checkClassNameIsDisplayed(teacherClasses.get(i)[1]);
			}
			
			report.startStep("Check teacher assigned to class");

			if (useNewClass) {
				if(useMapTable) {
					ExternalclassId = dbService.getClassFromClassExternalMapByClassId(teacherClasses.get(i)[0]);
				}else {
				 ExternalclassId = dbService.getClassFromClassExternalMapByClassId(teacherClasses.get(i)[0]);
				}
				testResultService.assertEquals(true, ExternalclassId != null, "External class Id not created.");
			}

		}
		report.startStep("logout from TMS");
		tmsHome.clickOnExitTMS();
		sleep(2);

	}

	private void verifyFirstAndLastName() throws Exception {
		tmsHome = new TmsHomePage(webDriver, testResultService);
		tmsHome.waitForPageToLoad();
		tmsHome.switchToMainFrame();

		report.startStep("Check first and last name on header page");
		tmsHome.checkUserDetails(userFN, userLN);

		tmsHome.clickOnRegistration();
		sleep(1);
		tmsHome.clickOnClasses();

	}

	private String insertNewUserIntoTable(String baseUrl) throws Exception {
		
		if (useMapTable){
			studentId = dbService.getExternalUserInternalId(userName, institutionId);
			String actualExternalUserId = dbService.getExternalUserInternalId(userName, institutionId);
			testResultService.assertEquals(studentId, actualExternalUserId, "User Not created correctly in External Table");
		}
		
		String teacherId = studentId;
		dbService.insertUserToAutomationTable(institutionId, studentId, userName, CanvasClassName, baseUrl);
	
		return teacherId;
	}

	private void insertNewClassIntoAutomationTable(String baseUrl) throws Exception {
			
		String[] clasess = null;
		String[] ExtclasessId = null;
			
		if (className.contains(";"))
		{
			clasess = className.split(";");
			if(useMapTable) {
				ExtclasessId = CanvasClassId.split(";");
			}
			for (int i=0;i<clasess.length;i++){
				
				if (useMapTable) {
					classId = dbService.getClassIdByExternalClassName(clasess[i], institutionId);
					dbService.insertClassToAutomationTable(classId,clasess[i],ExtclasessId[i],institutionId, studentId,baseUrl);
				}else {
					classId = dbService.getClassIdByClassName(clasess[i], institutionId);
					dbService.insertClassToAutomationTable(classId,clasess[i],null,institutionId, studentId,baseUrl);
				}
				
			}
		}
		else{
			if (useMapTable)
				classId = dbService.getClassIdByExternalClassName(className, institutionId);
			else
				classId = dbService.getClassIdByClassName(className, institutionId);
			
			dbService.insertClassToAutomationTable(classId,className,CanvasClassId,institutionId, studentId,baseUrl);
		}
	}

	private void retrieveExistingClasses() throws Exception {
		
		if(userType.name().equalsIgnoreCase("Teacher")) {
			classAssignmnetAmount = 2;
			storeAmountOfTeacherClases(userType);
			}
		assignNewOrCurrentClassesWithAmount(classAssignmnetAmount, userType);
	}

	public void getUser(int size) throws Exception {
		if (newStudent) {
			userName = "stud" + dbService.sig(5);
			
		}
		if (newTeacher) {
			userName = "teach" + dbService.sig(5);
			
		}
		externalUserName = userName;
		userFN = userFN + " " + userName;
		userLN = userLN + " " + userName;
		email = userName + "-r" + "@gmail.com";
	}

	public String getExistingUser(String typeOfUser) {
		
		String teacherId = "";
		String[] teacher = null;
		
		if (typeOfUser.equalsIgnoreCase("teacher")) {
			 if(useMapTable) {
				 teacher = dbService.getExternalMapTeacherInstitution(institutionId);
				 userName = teacher[0];
				externalUserName = teacher[5];
				userFN = teacher[1];
				userLN = teacher[2];
				teacherId = teacher[3];
				studentId = teacherId;
				email = teacher[4];
			 }
		
		  else { 
			  teacher = pageHelper.getTeacherInstitution(institutionId);
			  userName = teacher[0]; 
			  userFN = teacher[1];
			  userLN = teacher[2]; 
			  teacherId = teacher[3];
			  studentId = teacherId;
			  email = teacher[5];
		  
		  }
		}
		 

		if (typeOfUser.equalsIgnoreCase("student")){
			 if(useMapTable) {
			String[] user = dbService.getRandomExternalStudentByInstitutionId(institutionId);
			userName = user[0];
			externalUserName = user[5];
			userFN = user[1];
			userLN = user[2];
			teacherId = user[7];
			studentId = teacherId;
			email = user[6];
			 }
			 else {
			String[] studentd = pageHelper.getStudentsByInstitutionId(institutionId);
			userName = studentd[0];
			userFN = studentd[1];
			userLN = studentd[2];
			email = studentd[5];
			studentId = studentd[6];
			teacherId = studentId;
			 }
		}
		if (email==null||email.equalsIgnoreCase(""))
			email = "a@a.com";
		return teacherId;

	}

	private void createNewClasses() throws Exception {
		// String teacherId = "";
		// if (useNewClass) {
		if(userType.name().equalsIgnoreCase("Teacher")) {
			classAssignmnetAmount = 2;
			storeAmountOfTeacherClases(userType);
			}

		String suffix = dbService.sig(5);
		if (multipleClasses) {

			String suffix1 = dbService.sig(6);
			CanvasClassName = "ExtClassName_" + suffix + ";" + "ExtClassName" + suffix1;
			CanvasClassId = "ExtClass_" + suffix + "_Id" + ";" + "ExtClass_" + suffix1 + "_Id";
			className = CanvasClassName;
		} 
		else {
			CanvasClassName = "ExtClassName_" + suffix;
			CanvasClassId = "ExtClass_" + suffix + "_Id";
			className = CanvasClassName;
		}
	}

	private String createTestCanvasFile(String chkPath, String baseUrl, String testType) throws Exception {

		String testFile = "";

		// String chkInst = dbService.getInstituteNameById(argInstId);
		
		switch (testType) {
		
		case "NewTeacherNewClass":
			testFile = "testNewTeacherNewClass_" + dbService.sig(4) + ".html";
			role = "Instructor";
			break;

		case "NewStudentNewClass":
			testFile = "testNewStudentNewClass_" + dbService.sig(4) + ".html";
			role = "Learner";
			break;
		
		case "NStudentNClass":
			testFile = "testLTI_UseMap_"+useMapTable+"_NStudent_NClass_" + dbService.sig(4) + ".html";
			role = "Learner";
			break;
	
		case "NTeacherNClass":
			testFile = "testLTI_UseMap_"+useMapTable+"_NTeacher_NClass_" + dbService.sig(4) + ".html";
			role = "Instructor";
			break;
			
		case "NewStudentOldClass":
			testFile = "testNewStudentOldClass_" + dbService.sig(4) + ".html";
			role = "Learner";
			break;

		case "OldStudentNewClass":
			testFile = "testOldStudentNewClass_" + dbService.sig(4) + ".html";
			role = "Learner";
			break;

		case "OldStudentOldClass":
			testFile = "testOldStudentOldClass_" + dbService.sig(4) + ".html";
			role = "Learner";
			break;

		case "NewStudentLongClassName":
			testFile = "testNewStudentLongClassName_" + dbService.sig(4) + ".html";
			role = "Learner";
			break;

		case "WithOutComeAndSourceId":
			testFile = "testCanvasWithOutComeAndSourceId_" + dbService.sig(4) + ".html";
			break;

		case "ltiCustomize":
			testFile = "ltiCustomize_" + dbService.sig(4) + ".html";
			if(student) {
				role = "Learner";
			}
			if(teacher) {
				role = "Instructor";
			}
			break;

		case "ArgentinaPostCanvasFile":
			testFile = "LTI_Canvas_AutoLearningPath_" + dbService.sig(4) + ".html";
			break;

		case "Teacher and Student":
			testFile = "LTI_Canvas_TeacherAndStudent_" + dbService.sig(4) + ".html";
			role = "Instructor,Student";
			break;

		case "Teacher and Mentor":
			testFile = "LTI_Canvas_TeacherAndMentor_" + dbService.sig(4) + ".html";
			role = "Instructor,Mentor";
			break;

		case "NewTeacherNoAddress":
			testFile = "LTI_Canvas_NewTeacherNoAddress" + dbService.sig(4) + ".html";
			role = "Instructor";
			break;

		case "AssignMultipleClassesToTeacher":
			testFile = "LTI_Canvas_AssignMultipleClassesToTeacher" + dbService.sig(4) + ".html";
			role = "Instructor";
			break;
		
		case "AssignPackages":
		testFile = "AssignPackages" + dbService.sig(4) + ".html";
		role = "Instructor";
		break;
		}

		if (testFile.equals(""))
			return "undefined" + dbService.sig(4) + ".html";

		// .....................................................
		List<String> wList = new ArrayList<String>();
		//Map<String,String> signature = getSignatureForSecurityLTIEntrance(wList, baseUrl, institutionName, CanvasClassName);

		wList = createHtmlTitle(wList);
		//if(useSecurityCheck){
		if (baseUrl.contains("https://ed.engdis.com")){
			signature = getSignatureForSecurityLTIEntrance(wList, baseUrl, institutionName, CanvasClassName);
			wList = createFormInfo(wList, baseUrl, institutionName, CanvasClassName,true);
		}else {
			wList = createFormInfo(wList, baseUrl, institutionName, CanvasClassName);
		}
		wList = createHtmlTail(wList);
	/*	
		String[] buildPath = chkPath.split("//");
		String path = "\\";
		for(int i=1;i<buildPath.length-1;i++) {
			path=path+"\\"+buildPath[i];
		}
		path = path +"\\Languages\\"+testFile;
	*/	
		textService.writeListToSmbFile(chkPath, testFile, wList, useSMB);
		//textService.writeListToSmbFile(chkPath +"/"+testFile, wList, netService.getDomainAuth());

		return testFile;
	}

	private Map<String, String> getSignatureForSecurityLTIEntrance(List<String> wList, String baseUrl, String institutionName, String canvasClassName)  {

		String chkUser;
		if(useMapTable) {
			chkUser = externalUserName;
		}else {
			chkUser = userName;
		}

		Map<String,String> parameters = new HashMap<>();
		parameters.put("btnSubmit","GO TO ED");
		parameters.put("oauth_nonce","iKk7CNCRo0JtuHds75AjzzRFD9z5fnOmZaUBDpOhZuw");
		parameters.put("oauth_consumer_key","ED_FP");
		if (outComeAndSourceId) {
			parameters.put("lis_outcome_service_url","https://siglo21.instructure.com/api/lti/v1/tools/190/grade_passback");
			parameters.put("lis_result_sourcedid","190-1276-17670-6241-65b27cce6f3cb6e8ca4fda409c4d8a5f3f487f77");
		}
		parameters.put("custom_edinstitution",institutionName);
		parameters.put("roles",role);
		parameters.put(lastNameKey,userLN);
		parameters.put(firstNameKey,userFN);
		parameters.put("lis_person_name_full","Full "+chkUser);
		parameters.put(mailKey,email);
		parameters.put(userNameKey,chkUser);
		parameters.put(classNameKey,className);
		parameters.put("custom_course_sections",CanvasClassId);
		parameters.put("Link",CorpUrl);
		parameters.put("oauth_timestamp","1525609571");
		Long time = System.currentTimeMillis();
		String url = pageHelper.CILink;
		url = url.split("com/")[0]+"com/entrance.aspx";
		Map<String, String> signedParameters = null;
		try {
			signedParameters = new LtiOauthSigner().signParameters(parameters, "ED_FP", "12345", url, "POST");
		} catch (LtiSigningException e) {
			throw new RuntimeException(e);
		}

		return signedParameters;
	}

	private List<String> createFormInfo(List<String> wList, String chkURL, String chkInst, String chkClass) {
		return createFormInfo(wList, chkURL, chkInst, chkClass,false);
	}

	private List<String> createFormInfo(List<String> wList, String chkURL, String chkInst, String chkClass, boolean useSecurity) {
		String chkUser;
		if(useMapTable) {
			 chkUser = externalUserName;
		}else {
			 chkUser = userName;
		}
		if(assignPackages) {
			wList.add("\t<form method=\"post\" action=\"" + chkURL + "packageassignment\">");
			wList.add("\t\t<input type=\"submit\" name=\"btnSubmit\" value=\"GO TO ASSIGN PACKAGES\" />");
		}else {
			wList.add("\t<form method=\"post\" action=\"" + chkURL + "entrance.aspx\">");
			wList.add("\t\t<input type=\"submit\" name=\"btnSubmit\" value=\"GO TO ED\" />");
		}
		//wList.add("\t\t<input type=\"submit\" name=\"btnSubmit\" value=\"GO TO ED\" />");

		wList.add(
				"\t\t<input type=\"hidden\" name=\"oauth_nonce\" value=\"iKk7CNCRo0JtuHds75AjzzRFD9z5fnOmZaUBDpOhZuw\" />");
		wList.add("\t\t<input type=\"hidden\" name=\"oauth_consumer_key\" value=\"ED_FP\" />");
		if (useSecurity) {
			String responseSignature = signature.get("oauth_signature");
			String signatureMethod = signature.get("oauth_signature_method");
			String verssion = signature.get("oauth_version");

			wList.add("\t\t<input type=\"hidden\" name=\"oauth_signature\" value=\""+responseSignature+"\" />");
			wList.add("\t\t<input type=\"hidden\" name=\"oauth_signature_method\" value=\""+signatureMethod+"\" />");
			wList.add("\t\t<input type=\"hidden\" name=\"oauth_version\" value=\""+verssion+"\" />");
		}else {
			wList.add("\t\t<input type=\"hidden\" name=\"oauth_signature\" value=\"I96FVJ658qR6yld9mA1+MziiZLY=\" />");
		}
		if (outComeAndSourceId) {
			wList.add(
					"\t\t<input type=\"hidden\" name=\"lis_outcome_service_url\" value=\"https://siglo21.instructure.com/api/lti/v1/tools/190/grade_passback\" />");
			wList.add(
					"\t\t<input type=\"hidden\" name=\"lis_result_sourcedid\" value=\"190-1276-17670-6241-65b27cce6f3cb6e8ca4fda409c4d8a5f3f487f77\" />");
		}

		wList.add("\t\t<input type=\"hidden\" name=\"custom_edinstitution\" value=\"" + chkInst + "\" />");
		wList.add("\t\t<input type=\"hidden\" name=\"roles\" value=\"" + role + "\" />");

		if (useUserAddress) {

			try {
				randomUserAddress = dbService.sig(4) + "-" + dbService.sig(3);
				wList.add("\t\t<input type=\"hidden\" name=\"custom_canvas_user_login_id\"value=\"" + randomUserAddress
						+ "\" />");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		wList.add("\t\t<input type=\"hidden\" name=\"" + lastNameKey + "\" value=\"" + userLN + "\" />");
		wList.add("\t\t<input type=\"hidden\" name=\"" + firstNameKey + "\" value=\"" + userFN + "\" />");
	
		wList.add("\t\t<input type=\"hidden\" name=\"lis_person_name_full\" value=\"Full " + chkUser + "\" />");
		wList.add(
				"\t\t<input type=\"hidden\" name=\"" + mailKey + "\" value=\"" + email + "\" />");
		wList.add("\t\t<input type=\"hidden\" name=\"" + userNameKey + "\" value=\"" + chkUser + "\" />");

		wList.add("\t\t<input type=\"hidden\" name=\"" + classNameKey + "\" value=\"" + className + "\" />"); // ClassName
		wList.add("\t\t<input type=\"hidden\" name=\"custom_course_sections\" value=\"" + CanvasClassId + "\" />"); // ClassId

		// --igb 2018.11.18 --> add new mandatory parameter
		// if(!bTeacher) -- already not mandatory --> igb 2018.11.20
		// wList.add("\t\t<input type=\"hidden\" name=\"custom_course_id\"
		// value=\"20000\" />");
		// --igb 2018.11.18 --> add new mandatory parameter
		wList.add("\t\t<input type=\"hidden\" name=\"Link\" value=\"" + CorpUrl + "\"/>");
		if (useSecurity){
			String timeStamp = signature.get("oauth_timestamp");
			wList.add("\t\t<input type=\"hidden\" name=\"oauth_timestamp\" value=\""+timeStamp+"\" />");
		}else {
			wList.add("\t\t<input type=\"hidden\" name=\"oauth_timestamp\" value=\"1525609571\" />");
		}
		wList.add("\t</form>");

		return wList;
	}

	private List<String> createHtmlTitle(List<String> wList) {
		wList.add("<!DOCTYPE html>");
		wList.add("<html>");
		wList.add("<head>");

		wList.add("\t<meta charset=\"utf-8\" />");
		wList.add("\t<title></title>");

		wList.add("</head>");
		wList.add("<body>");

		return wList;
	}

	private List<String> createHtmlTail(List<String> wList) {
		wList.add("</body>");
		wList.add("</html>");

		return wList;
	}
	// --igb 2018.08.20-------------

	private String createRegularRegUserAndLoginFile(String createClass, String createUser, String userType,
			String TestFileName) throws Exception {

		String baseUrl = pageHelper.CILink.split(".com")[0] + ".com/";
		String accessUrl = pageHelper.urlForExternalPages.split(".com")[0] + ".com/languages/";
		String chkPath = pageHelper.buildPathForExternalPages + "Languages";
		// String studentUserName="";
		List<String[]> classes = null;

		if (createUser.contains("I")) {

			if (useNameUserWithMinus)
				userName = userType + "-" + dbService.sig(5);
			else
				userName = userType + dbService.sig(5);

			userFN = userFN + userName;
			userLN = userLN + userName;
			email = userName + new Random().nextInt(1000) + "@gmail.com";
		} else {
			String[] teacher = pageHelper.getTeacherInstitution(institutionId);
			userName = teacher[0];
			userFN = teacher[1];
			userLN = teacher[2];
			email = teacher[5];
			if(email==null) {
				email = userName + new Random().nextInt(1000) + "@gmail.com";
			}
		}

		if (createClass.contains("Y"))
			className = "C_" + dbService.sig(4);

		else {
			classes = pageHelper.getclassesByInstitutionName(institutionName);
			if (multipleClasses) {
				className = get2DiffExistingClasses(classes);
			} else {
				Random random = new Random();
				int i = random.nextInt(classes.size());
				className = classes.get(i)[0];
			}
		}

		report.startStep("Create test file -- " + TestFileName);

		// .....................................................
		List<String> wList = new ArrayList<String>();

		wList = createHtmlTitle(wList);
		wList = createFormInfoRegularRegUserAndLogin(wList, baseUrl, institutionName, className, userName, userFN,
				userLN, createClass, createUser, userType);
		wList = createHtmlTail(wList);

		/*String[] buildPath = chkPath.split("//");
		String path = "\\";
		for(int i=1;i<buildPath.length-1;i++) {
			path=path+"\\"+buildPath[i];
		}
		path = path +"\\Languages\\"+TestFileName;
		boolean useSMB = false;
		textService.writeListToSmbFile(path, wList, useSMB);*/
		textService.writeListToSmbFile(chkPath, TestFileName, wList, useSMB);

		//textService.writeListToSmbFile(chkPath+"/"+TestFileName, wList, netService.getDomainAuth());

		String openURL = accessUrl + TestFileName;

		pageHelper.closeBrowserAndOpenInCognito(false);
		webDriver.openUrl(openURL);
		webDriver.waitUntilElementAppears(webDriver.waitForElement("btnSubmit", ByTypes.name), 20);

		webDriver.waitForElement("btnSubmit", ByTypes.name).click();
		sleep(2);
		
		if (!useNameUserWithMinus) {
			pageHelper.skipOptin();
			homePage.closeAllNotifications();
			studentId = dbService.getUserIdByUserName(userName, institutionId);

			if (newTeacher || newStudent) {
				if (studentId != null)
					dbService.insertUserToAutomationTable(institutionId, studentId, userName, className,
							pageHelper.buildId);
				else
					testResultService.addFailTest("User not created", true, true);
			}
		}
		
		if (useNewClass)
			insertNewClassIntoAutomationTable(baseUrl);
		
		return userName;
	}

	private List<String> createFormInfoRegularRegUserAndLogin(List<String> wList, String chkURL, String chkInst,
			String chkClass, String chkUser, String userFN, String userLN, String createClass, String createUser,
			String userType) {

		wList.add("\t<form method=\"post\" action=\"" + chkURL + "RegUserAndLogin.aspx\">");
		wList.add("\t\t<input type=\"submit\" name=\"btnSubmit\" value=\"GO TO ED\" />");
		wList.add("\t\t<input type=\"hidden\" name=\"Action\" value=\"" + createUser + "\" />");
		wList.add("\t\t<input type=\"hidden\" name=\"UserType\" value=\"" + userType + "\" />");
		wList.add("\t\t<input type=\"hidden\" name=\"UserName\" value=\"" + chkUser + "\" />");
		wList.add("\t\t<input type=\"hidden\" name=\"Inst\" value=\"" + chkInst + "\" />");
		wList.add("\t\t<input type=\"hidden\" name=\"FirstName\" value=\"" + userFN + "\" />");
		wList.add("\t\t<input type=\"hidden\" name=\"LastName\" value=\"" + userLN + "\" />");
		wList.add("\t\t<input type=\"hidden\" name=\"Password\" value=\"12345\" />");
		wList.add("\t\t<input type=\"hidden\" name=\"Email\" value=\"" + email + "\" />");
		wList.add("\t\t<input type=\"hidden\" name=\"Class\" value=\"" + chkClass + "\" />");
		wList.add("\t\t<input type=\"hidden\" name=\"CreateClass\" value=\"" + createClass + "\" />");
		wList.add("\t\t<input type=\"hidden\" name=\"Language\" value=\"English\" />");
		wList.add("\t\t<input type=\"hidden\" name=\"Link\" value=\"" + CorpUrl + "\"/>");

		wList.add("\t</form>");

		return wList;
	}

	@Test
	@Category(inProgressTests.class) 
	@TestCaseParams(testCaseID = { "43954", "50342" }, testTimeOut = "30")
	public void updateRedirectJsonFile() throws Exception{
	
		//String path="smb://ci-srv/AutomationFiles/Json/";
		String path="smb://ci/AutomationFiles/Json/";
		String fileName="RedirectConfig.json";
		String blockName="Servers"; //DUI-CI-RemoveHttpClient-20230207-2.edusoftrd.com";
		String key="Enabled";
		String value="false";
		
		textService.updateJsonFileByKeyValue(path,fileName,blockName,key,value);
	}
	
   // @Test
    //@TestCaseParams(testCaseID = { "" })
    public void testCanvasNewStudentRedirect() throws Exception {



        String path = String.format("%s%s", PageHelperService.sharePhisicalFolder,"\\Config\\RedirectConfig.json");
        String jsonpath	= String.format("$.Servers['%s']['Enabled']",PageHelperService.linkEdStaticSite);

        // How to find the testID line 362

        report.startStep("Updating Redirect config for current university");

        textService.updateFileWithJsonPath(path,
                jsonpath , true);

        newStudent= true;
        //useUserAddress = true;
        userType = UserType.Student;
        useMapTable = true;
        canvasTest = true;
        runCanvasTest("NewStudentOldClass");

        try {

            String expectedUrl = PageHelperService.linkEdStaticSite;
            report.startStep("Check Client type and Operating system");
            testResultService.assertEquals(true, dbService.verifyClientTypeAndOperatingSystemOfProgress(studentId));

            report.startStep("Check that student has appropriate URL");
            //testResultService.assertTrue("Redirect URL has not been activated", webDriver.waitForSpecificCurrentUrl("", expectedUrl).contains(expectedUrl));
            testResultService.assertEquals(true, webDriver.waitForSpecificCurrentUrl("", expectedUrl).contains(expectedUrl), 
            		"Redirect URL has not been activated");

        } catch (Exception e) {
            testResultService.addFailTest(e.toString(), false, true);

        } finally {
            // pageHelper.deleteStudentsAndClass(UMMInstId, CanvasClassName);
            report.startStep("Restoring redirect config");
            assignCoursesToClassWhenTestEnds = false;
            textService.updateFileWithJsonPath(path,
                    jsonpath , false);
        }


    }
      
    @Test
    @TestCaseParams(testCaseID = { "" })
    public void testAssignPackagesViaImportCSV_POST() throws Exception {
   
    report.startStep("Prepare test-data");
    	String chkPath = pageHelper.buildPathForExternalPages + "Languages";
		String baseUrl = pageHelper.CILink.split(".com")[0] + ".com/";
		String accessUrl = pageHelper.urlForExternalPages.split(".com")[0] + ".com/";
		String className = "";
		CanvasClassName = "";
		CanvasClassId = "";
		assignPackages = true;
		String tailNumber = dbService.sig(4);
		String newClass = "Class-" + tailNumber;
		String fileName = "AddPackageToNewClass"+tailNumber+".csv";
		//String filePath = "\\\\"+configuration.getGlobalProperties("logserverName")+"\\AutoLogs\\ToolsAndResources\\Shared\\ToeicStudentsToImport\\"+fileName;	
		//String filePath = "\\"+String.join("\\",pageHelper.buildPathForExternalPages.substring(4).split("//"))+"\\Languages\\"+fileName;
		String filePath = pageHelper.buildPathForExternalPages +"Languages\\" +fileName;
		String startPackageDate = pageHelper.getCurrentDateByFormat("M/d/yyyy");
		String[] institutionPackage = dbService.getUnExpiredInstitutionPackages(institutionId).get(0);
		String instPackId = institutionPackage[0];
		String packageName = institutionPackage[1];
		String startDate = institutionPackage[2];
			
	try {	
	report.startStep("Get admin credentials from external map");
		List<String[]> admin = dbService.getAdminFromExternalMap(institutionId);
			userName = admin.get(0)[2];
			userFN = admin.get(0)[3];
			userLN = admin.get(0)[4];
			email = admin.get(0)[5];
			
	report.startStep("Create test file AssignPackages.html");
		String testFile = createTestCanvasFile(chkPath, baseUrl, "AssignPackages");

		if (!testFile.equals("")) {
			report.startStep("Create and open URL");
			accessUrl = accessUrl + "Languages/" + testFile;
			webDriver.openUrl(accessUrl);
			webDriver.waitUntilElementAppearsAndReturnElementByType("btnSubmit", ByTypes.name, 10); // webDriver.waitForElement("btnSubmit",ByTypes.name);
			webDriver.waitForElement("btnSubmit", ByTypes.name).click();
			sleep(2);
			assignPackages = false;
		}else {
			testResultService.addFailTest("Canvas file wasn't created", true, true);
		}

	report.startStep("Verify 'AssignPackages' downloaded");	
		WebElement title = webDriver.waitForElement("h1", ByTypes.tagName);
		WebElement instuctions = webDriver.waitForElement("//div[@class='file-uploader__instructions flex justify-center gap-10 ng-star-inserted']",ByTypes.xpath);
		textService.assertEquals("Wrong title", "Assign Packages", title.getText());
		textService.assertEquals("Incorrect instructions", "Click on \"Browse Files\".", instuctions.getText().trim());
		
	report.startStep("Create CSV file to import packages");	
		CurriculumAssignPackagesPage cap = new CurriculumAssignPackagesPage(webDriver, testResultService);
		List<String[]> listToFile = cap.prepareListForCSVfile(newClass,packageName,startDate,institutionId,true);
		textService.writeArrayistToCSVFile(filePath, listToFile);//(filePath, listToFile);
		
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
		try {
			WebElement messageAfterUpload = webDriver.waitForElement("//div[@class='selected-file__upload--error ng-star-inserted']", ByTypes.xpath);
			textService.assertEquals("Upload unsuccessfull", "Upload successful", messageAfterUpload.getText().trim());
		}catch (AssertionError e) {
			testResultService.addFailTest("Endless uploading csv, successfull message not apears",true,true);
		}
		webDriver.executeJsScript("document.getElementById('file-upload').style.display='none'");
	
	report.startStep("Verify start-date updated on 'Data Base'");
		startPackageDate = pageHelper.getCurrentDateByFormat("yyyy-MM-dd");
		String[] pkg = dbService.getPackageByInstitutionPackageId(instPackId,institutionId);
		List<String> classAssignPackages = dbService.getClassAssignPackages(newClass,institutionId);
		textService.assertTrue("Start-date of package not updated on DB", pkg[1].startsWith(startDate));
		//textService.assertEquals("Class not assigned package", instPackId, classAssignPackages);
		textService.assertTrue("Start-date of package not updated on DB", classAssignPackages.contains(instPackId));
		
	}catch (Exception e) {
		testResultService.addFailTest(e.getMessage(), true, true);
		e.printStackTrace();
	}catch (AssertionError e) {
		testResultService.addFailTest(e.getMessage(), true, true);
		e.printStackTrace();
	}finally{
		File uploadedPackage= new File(filePath); 
		uploadedPackage.delete();
		assignPackages = false;
	}
		
    }
    

	
	@After
	public void tearDown() throws Exception {
		
		super.tearDown();
		
		institutionName = "";
		institutionId = "";
		classId = "";
		className = "";
		userName = "";
		userType = null;
		
		webDriver.closeBrowser();
		/*
		 * ////// this step should be removed after implementation of US !!! if
		 * (assignCoursesToClassWhenTestEnds){
		 * report.startStep("Assign All Courses to Class - Default State");
		 * pageHelper.UnlockCourseToClass(argInstId, classNameAR, courses, courses); }
		 * //////
		 */
	}
}
