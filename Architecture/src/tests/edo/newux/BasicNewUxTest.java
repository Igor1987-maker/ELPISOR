package tests.edo.newux;

import Objects.GenericTestObject;
import org.junit.After;
import org.junit.Before;
import pageObjects.edo.NewUXLoginPage;
import pageObjects.edo.NewUxClassicTestPage;
import pageObjects.edo.NewUxHomePage;
import services.DbService;
import services.PageHelperService;
import tests.misc.EdusoftWebTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class BasicNewUxTest extends EdusoftWebTest {

	
	protected NewUxHomePage homePage;
	protected String studentId;
	public static String userName;
	public static String externalUserName;
	public static String password;

	//public String[] courses = new String[] { "20000", "20185", "20186","20187", "20188", "20189", "20190", "20191", "20192", "20193"}; // Old Courses

	// Package 10 Units
	public static String[] coursesOld = new String[] { "20000", "37322", "37312", "37319", "37311", "37310", "37316", "37317","37320", "37323" };
	public static String[] courses = new String[] { "20000", "43396", "43870", "44205", "44845", "45178", "45441", "45626","45689", "45742"};
		
	public static String[] courseCodes = new String[] { "FD", "B1", "B2", "B3", "I1","I2", "I3", "A1", "A2", "A3"};
    public static String[] coursesNames = new String[] { "First Discoveries", "Basic 1", "Basic 2", "Basic 3", "Intermediate 1", "Intermediate 2", "Intermediate 3", "Advanced 1","Advanced 2", "Advanced 3"};
    
    //For CABA courses
    public static String[] coursesCABA = new String[] {"133329", "133239"};
    public static String[] courseCodesCABA = new String[] {"EDP","EDJ"};
    public static String[] coursesNamesCABA = new String[] {"TOEFL Primary® English Learning Center", "TOEFL Junior® English Learning Center" };

    public static String[] coursesTOEIC = new String[] {"1547026","1547470", "1548014"};
    public static String[] courseCodesTOEIC = new String[] {"M1","M2","M3"};
    public static String[] coursesNamesTOEIC = new String[] {"Module 1: High-beginner", "Module 2: Intermediate", "Module 3: Advanced", "TOEIC Bridge® Official Learning and Preparation"};
    public static String[] coursesNamesTOEICOLPC = new String[] {"1", "3257", "3467" };
    public static String[] practiceTestNames = new String[] {"TOEIC Bridge® Practice Test 1", "TOEIC Bridge® Practice Test 2", "TOEIC® Practice Test 1", "TOEIC® Practice Test 2a  (half-test)", "TOEIC® Practice Test 3", "TOEIC® Practice Test 2b (half-test)", "TOEIC® Practice Test 4", "TOEIC® Practice Test 5"};
   // public static String[] coursesNamesEDExcellence = new String[] {"TOEIC Official Learning and Preparation Course 1", "TOEIC Official Learning and Preparation Course 2", "TOEIC Official Learning and Preparation Course 3", "TOEIC Bridge Official Learning and Preparation Course"};
    public static String[] coursesNamesEDExcellence = new String[] {"TOEIC® Official Learning and Preparation Course 1", "TOEIC® Official Learning and Preparation Course 2", "TOEIC® Official Learning and Preparation Course 3", "TOEIC Bridge® Official Learning and Preparation Course"};
    public static String[] coursesUMM = new String[] { "20228", "20000", "20226", "43396", "20227", "43870", "20001", "44205", "20002", "44845", "20003", "45178", "20080", "45441", "20081", "45626", "20082", "45689", "20083", "45742"};
    public static String[] coursesNamesUMM = new String[] { "Aviation 1", "First Discoveries", "Aviation 2", "Basic 1", "Aviation 3", "Basic 2", "English At Work 1", "Basic 3", "English At Work 2", "Intermediate 1", "English At Work 3", "Intermediate 2", "Tourism Pre-Basic", "Intermediate 3", "Tourism Basic", "Advanced 1", "Tourism Intermediate", "Advanced 2", "Tourism Advanced", "Advanced 3"};

    public static String[] testTypes = new String[] { "Placement Test", "Mid-Term", "Course Test", "TOEFL Primary", "TOEFL Junior", "TOEIC Bridge™ test"};
    public static String[] testTypesId = new String[] { "1", "2", "3", "4", "5"};
	
    // For Toeic test Results
    public static Integer[] numOfSectionsAnsweredTOEIC = new Integer[] {0, 1, 2, 3};
    public static String[] testScoreTOEIC = new String[] {"0", "10", "30", "40"};
   
    public static String[] institutionsName = new String[]
			{"automation", "courses", "autoarg","local" //3
			,"aeonreading","@QA-UmmVirtual","Autolaureate" //6
			,"autoargNewTE","isra-el","@QA-TBoard","ED2016" // 10
			,"EdToeic1","@QA-TBoard2","@QA-TBoard3","QaCEFR" // 14
			,"AmraQa","QaJobRediness","QaLtiNoMap","qa41" // 18
			,"Qa51","qa31","qa2020","QAExcellence","qa20","qa21"}; // 24
	public List<String[]> specialCoursesData;
	
	String[] specialCoursesIds = new String[] { courses[0], courses[1], "22", "7611", "20104" };
	
	/*
	 * protected String[] HexUnitColors = new String[] { "#188bb3", "#34b5d3",
	 * "#55bcab", "#b0c34d", "#d69150", "#f59740", "#a7383e", "#be416b",
	 * "#6e406e", "#403263", "#206586", "#1a99c6", "#55bcab" };
	 */

	protected String[] HexUnitColors = new String[] { "#e2bd64", "#96c965", "#56c87c", "#2edbc0", "#52e1df", "#61c8e2",
			"#82aef6", "#8f93de", "#ae9fea", "#db9ada", "#e99595", "#d7b679" };
	
	
	protected String[] headerColors = new String[] { "#0d4b60", "#146b7e",
			"#1e675c", "#6a791a", "#8b521d", "#b5651c", "#8a2b2f", "#942a4e",
			"#572e58", "#2d204b", "#124a65", "#105c76", "#1e675c" };
	public static String [] customCourseNames = new String[] { "Israel", "Component test Igor", "Laurete1", "Full Screen", "FITB New Template Approval", "Component title", "Dialog Course", "CABA Pilot Exam Course", "S_CC01", "Left side resource in Test", "Course of David", "Nov 10 Lesson 1", "Nov 10 Lesson 2 Story", "Firefox Nov 10 Postcard 1", "Internet Explorer Nov 10", "Chrome Blank Nov 10", "Nov 17-1", "Cheburashka on Firefox", "Cheburashka and Mushrooms", "Monkey Course", "Spanish Article", "Testing MAC", "Mac Chrome - Email", "Dec 3 - Article 2 Chrome", "Dec 3 Story Chrome", "Article - Progress", "Dec 5 - Article - Build 05-3", "Dec 8", "View Resource", "Blank Omer - Blank", "Formatting - Story - Dec 11", "Email - Bug", "Dec 12 - Blank", "Dec 15 - Article", "Dec 15 - Blank - Italian", "Dec 17 - Story", "CC - External Content", "CC - article with sound", "Jan 15 - Bug Fix - Email Fields", "Dialog", "MaxFax", "Copy from existing", "June 9 All Templates", "Audio IPAD Aug 11", "MCQMA1", "Multiple", "Multiple2", "EXPMAX", "EXPMAX1", "MeniCourse1", "MMM12", "MaxCust", "EDExcellence - Flexibility", "Flexibility"};
    public static String [] customCourseCode = new String[] {"7713", "7760", "7783", "7785", "523015156", "523028339", "523029688", "523030892", "523048849", "523086485", "523093896", "523098007", "523098021", "523098035", "523098055", "523098076", "523098972", "523098986", "523099006", "523099602", "523099845", "523102264", "523102267", "523102308", "523102334", "523103145", "523103187", "523103597", "523104598", "523104644", "523104758", "523104813", "523104987", "523105506", "523105523", "523105822", "523107029", "523107070", "523107109", "523109267", "523117637", "523120830", "523121782", "523130647", "523141798", "523141822", "523141834", "523141907", "523141927", "523141936", "523142323", "523150445", "523176661" };

	//public static String coursesInstId; //= "5233219";
	//public static String localInstId;
	//public static String aeonInstId;
	//public static String argInstId;
	//public static String UMMInstId;
	//public static String laureateInstId; //= "5233700";
	//public static String autoargNewTEInstId;
	//public static String TeacherDashBoardInstId;
	public static String CannonicalDomain="";
	//public static String ed2016InstId;
	//public static String israelInstId;
	public static Map<String, String> instIDS  = new HashMap<String, String>() {{
		put("automation", "");
		put("courses", "");
		put("qajobrediness", "");
		put("qaexcellence", "");
		put("ed2016", "");
	}};
	//protected static final String SchedularID = "5233545";
	
	//protected static final String argInstId = "5233990"; // QA static DB 
	public static String institutionName="";
	public static String institutionId="";
	public static String classId="";
	public static String internalClassName ="";
	
	protected static final String AcuityStudentUrl= "https://app.acuityscheduling.com/schedule.php?firstName=auto2&lastName=auto2&owner=16720366&calendarID=2834477&field:6088045=52337000000026";
	protected static final String AcuityTeacherUrl= "https://secure.acuityscheduling.com/login.php";
	public String CorpUrl = "https://edusoftlearning.com/";
	
	public static boolean newTe = true;
	
	
	@Before
	public void setup() throws Exception {
		// System.setProperty("useProxy", "true");
		super.setup();
		populateSpecialCoursesData();
		report.startStep("Get CI latest link and open it");
		homePage = pageHelper.openCILatestUXLink();
	}

	
	
	@After
	public void tearDown() throws Exception {
		report.startStep("Log out User");
		try {
			if (studentId != null){
				dbService.logOutUser(studentId);
			}
		}
		finally {
			super.tearDown();
			insertTestSatusToDB();
		}
	}

	private void insertTestSatusToDB_Old() {
	
		String logTestFileLink;
		
		try {
			String testStatus = getTestStatus();
			String testClass = getClass().toString();
			logTestFileLink = printLogs(); //getLogFilePath();
			String sourceBranch_CI = PageHelperService.branchCI;
			String trigger = GenericTestObject.trigger;
			testDuration = stopWatch.runtime(TimeUnit.SECONDS);
			dbService.insertDBTestStatus(testClass,testCaseName,testStatus,PageHelperService.linkED,logTestFileLink,sourceBranch_CI,trigger,testDuration);	
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	public String getCourseIdByCourseCode(String courseCode) throws Exception {
		String courseId = null;
		for (int i = 0; i < courseCodes.length; i++) {
			if (courseCodes[i].equals(courseCode)) {
				courseId = courses[i];
			}
		}
		return courseId;
	}

	public String getCourseIdByCourseCustomCode(String courseCode) throws Exception {
		String courseId = null;
		for (int i = 0; i < customCourseNames.length; i++) {
			if (customCourseNames[i].equals(courseCode)) {
				courseId =customCourseCode[i];
			}
		}
		return courseId;
	}
	
	public String getTOEICCourseIdByCourseCode(String courseCode) throws Exception {
		String courseId = null;
		for (int i = 0; i < courseCodesTOEIC.length; i++) {
			if (courseCodesTOEIC[i].equals(courseCode)) {
				courseId = coursesTOEIC[i];
			}
		}
		return courseId;
	}
	
	public String getCourseNameByCourseCode(String courseCode) throws Exception {
		String courseName = null;
		for (int i = 0; i < courseCodes.length; i++) {
			if (courseCodes[i].equals(courseCode)) {
				courseName = coursesNames[i];
			}
		}
		return courseName;
	}
	
	public String getTOEICCourseNameByCourseCode(String courseCode) throws Exception {
		String courseName = null;
		for (int i = 0; i < courseCodesTOEIC.length; i++) {
			if (courseCodesTOEIC[i].equals(courseCode)) {
				courseName = coursesNamesTOEIC[i];
			}
		}
		return courseName;
	}

	public void loginAsStudent() {

	}

	/**
	 * This method creates new user for the default class ("classNewUx") and
	 * performs a login with the new user to the latest CI build
	 * 
	 * @return
	 * @throws Exception
	 */
	public NewUxHomePage createUserAndLoginNewUXClass() throws Exception {
		return createUserAndLoginNewUXClass(configuration.getClassName());
		
	}
	
	// login for OptIn
	
	public NewUxHomePage createUserAndLoginForOptIn() throws Exception {
		return createUserAndLoginForOptIn(configuration.getClassName());
		
	}
	
	public NewUxHomePage getUserAndLoginNewUXClass() throws Exception {
		return getUserAndLoginNewUXClass(configuration.getClassName());
		
	}
	
	public NewUxHomePage getToeicOlpcUserAndLogin(String path) throws Exception {
		return getToeicUserAndLogin(path);
	}
	
	public NewUxHomePage getToeicUserAndLogin(String path) throws Exception {
		NewUxClassicTestPage classicTest;
		NewUXLoginPage loginPage = new NewUXLoginPage(webDriver,testResultService);
		
		// Define a map that contains all users data
		classicTest = new NewUxClassicTestPage(webDriver,testResultService);
		HashMap<String, String> users = classicTest.getUsersListFromCSV(path);

		// Choose random user
		int randomIndex = (int) (Math.random() * users.size());
		String randomUserName = users.keySet().toArray()[randomIndex].toString();
		//randomUserName = "001000589222";
		if(!randomUserName.startsWith("00")) {
			userName = "00"+randomUserName;
		}else {
		userName = randomUserName;
		}
		password = users.get(randomUserName);
		report.startStep("Login as Random User: " + userName + " ,Password: " + password);		
		homePage = loginPage.loginAsStudent(userName, password);
		studentId = dbService.getUserIdByUserName(userName,dbService.getInstituteIdByName(institutionsName[11]), isUseSecondEDMerge());
		
		
		Thread.sleep(1);
			return homePage;
	}

	private static boolean isUseSecondEDMerge() {
		return DbService.edMerge2DB.length() > 0;
	}

	public void deleteUsedToeicUser() throws Exception {

		if (userName.startsWith("00")) {
			userName = userName.substring(2);
		}
		List<String[]> users = textService.getStr2dimArrFromCsv(PageHelperService.userFilePath);
		for(int i =0;i<users.size();i++){
			if(users.get(i)[0].equalsIgnoreCase(userName)){
				users.remove(i);
				break;
			}
		}
		//new FileWriter(FILE_PATH, false).close(); -----> this is how to clear content of the file
		textService.writeArrayistToCSVFile(PageHelperService.userFilePath, users);

	}



	protected NewUxHomePage getUserAndLoginGeneral(String className,boolean skipOptin,boolean skipNotification)
			throws Exception {
		return getUserAndLogin(className,configuration.getInstitutionId(),skipOptin,skipNotification);
	}
	
	public NewUxHomePage getUserAndLoginLocalClassName()
			throws Exception {
		return getUserAndLoginLocalInstitutionId(configuration.getLocalclassName());
	}
	
	public String[] getLocalUser()
			throws Exception {
		return getUserFromDb(configuration.getLocalclassName(),institutionId);
	}
	
	public String[] getGeneralUser()
			throws Exception {
		return getUserFromDb(configuration.getClassName(),institutionId);
	}
	
	/**
	 * 
	 * @param className
	 * @return
	 * @throws Exception
	 */
	protected NewUxHomePage createUserAndLoginNewUXClass(String className)
			throws Exception {
		return createUserAndLoginNewUXClass(className,institutionId);
	}
	//for OptIn
	
	protected NewUxHomePage createUserAndLoginForOptIn(String className)
			throws Exception {
		return createUserAndLoginForOptIn(className,configuration.getInstitutionId());
	}
	
	protected NewUxHomePage getUserAndLoginNewUXClass(String className)
			throws Exception {
		return getUserAndLoginNewUXClass(className,institutionId);
	}

	protected NewUxHomePage getUserAndLoginLocalInstitutionId(String className)
			throws Exception {
		return getUserAndLoginNewUXClass(className,configuration.getLocalInstitutionId());
	}

	
	protected NewUxHomePage getUserAndLoginLaurateClass(String instId)
			throws Exception {
		//configuration.getProperty("lau_institutionId")
		return getUserAndLoginNewUXClass(configuration.getProperty("lau_className"),instId);
	}

	/**
	 * 
	 * @param className
	 * @param institutionid
	 *            - if you want to use other institution from the one defined in
	 *            the properties file
	 * @return
	 * @throws Exception
	 */
	protected NewUxHomePage createUserAndLoginNewUXClass(String className,
			String institutionid) throws Exception {
		
		studentId = pageHelper.createUSerUsingSP(institutionid, className);  //
		
		//dbService.setUserOptIn(studentId, true);
		
		NewUXLoginPage loginPage = new NewUXLoginPage(webDriver, testResultService);
		
		userName = dbService.getUserNameById(studentId, institutionid);
		homePage = loginPage.loginAsStudent(userName, "12345");

//		dbService.setUserOptIn(studentId, true);
		
		return homePage;
	}
	
	protected NewUxHomePage createUserAndLoginForOptIn(String className,
			String institutionid) throws Exception {
		
		studentId = pageHelper.createUSerUsingOptIn(institutionid, className);
		
		NewUXLoginPage loginPage = new NewUXLoginPage(webDriver, testResultService);
		userName = dbService.getUserNameById(studentId, institutionid);
		homePage = loginPage.loginAsStudent(userName, "12345");
		
		return homePage;
	}
	
	protected NewUxHomePage getUserAndLoginNewUXClass(String className,
			String institutionid) throws Exception {
		
		String[] userDetails = pageHelper.getUserFromDbByInstitutionIdAndClassName(institutionid, className);
		
		userName = userDetails[0];
		password = userDetails[1];
		studentId = userDetails[2];
			
		dbService.setUserOptIn(studentId, true);
		
		NewUXLoginPage loginPage = new NewUXLoginPage(webDriver, testResultService);

		homePage = loginPage.loginAsStudent(userName, password);
		report.report("UserName is : "+ userName + " ,and Password is: "+ password + " ,UserId is :" + studentId);
		sleep(1);
		homePage.closeAllNotifications();
		pageHelper.skipOnBoardingHP();
		homePage.waitHomePageloadedFully();
		
		return homePage;
	}
	/*
	 * protected NewUxHomePage getTokenFromLocalStorage() { JavascriptExecutor
	 * jsExecutor = (JavascriptExecutor)webDriver; tokenFromLocalStorage = (String)
	 * jsExecutor.executeScript("return localStorage.getItem(\"EDAPPToken\")");
	 * return homePage; }
	 */
	
	protected NewUxHomePage getUserAndLogin(String className,
			String institutionid,boolean skipOptin, Boolean skipNotifications) throws Exception {
		
		String[] userDetails = pageHelper.getUserFromDbByInstitutionIdAndClassName(institutionid, className);
		
		String userName = userDetails[0];
		String password = userDetails[1];
			   studentId = userDetails[2];
		
		if (skipOptin)
			dbService.setUserOptIn(studentId, true);
		
		NewUXLoginPage loginPage = new NewUXLoginPage(webDriver, testResultService);

		homePage = loginPage.loginAsStudent(userName, password);
		report.report("UserName is : "+ userName + " ,and Password is: "+ password + " ,UserId is :" + studentId);
				sleep(3);
		if (skipNotifications)
			homePage.closeAllNotifications();
		
		return homePage;
	}

	protected String[] getUserNamePassId(String institutionid,String className) throws Exception {
		
		String[] userDetails = pageHelper.getUserNamePassworIddByInstitutionIdAndClassName(institutionid, className);
		studentId = userDetails[2];
		
		return userDetails;
	}
	
	protected String[] getUserFromDb(String className,String institutionid) throws Exception {
		String[] userDetails = pageHelper.getUserFromDbByInstitutionIdAndClassName(institutionid, className);
		return userDetails;
	}
	
	protected String[] createUserAndReturnDetails(String institutionid,String className) throws Exception {
		String[] userDetails={};
		
		userDetails = pageHelper.createUSerUsingSPAndReturnDetails(institutionid,className);
		return userDetails;
	}

	protected NewUxHomePage loginAsStudent(String studentId) throws Exception {
		return loginAsStudent(studentId, institutionId);
	}

	protected NewUxHomePage loginAsStudent(String studentId,
			String institutionId) throws Exception {
		NewUXLoginPage loginPage = new NewUXLoginPage(webDriver,
				testResultService);
		homePage = loginPage.loginAsStudent(
				dbService.getUserNameById(studentId, institutionId), "12345");
		return homePage;
	}

	
	protected void populateSpecialCoursesData() {
				
		String[] specialCoursesClassNames = new String[] {
				configuration.getClassName(), configuration.getClassName(), configuration.getProperty("classname.enrich"),configuration.getProperty("classname.builder"),configuration.getProperty("classname.esp")};
		specialCoursesData = new ArrayList<>();

		for (int i = 0; i < specialCoursesClassNames.length; i++) {
			specialCoursesData.add(new String[] { specialCoursesClassNames[i],
					specialCoursesIds[i] });
		}

	}
	public BigDecimal roundExpectedProgress(double d) {
		return roundExpectedProgress(d, 0);
	}

	public BigDecimal roundExpectedProgress(double d, int scale) {
		
		// TODO Auto-generated method stub
		BigDecimal bigd = new BigDecimal(d);
		bigd = bigd.setScale(scale, BigDecimal.ROUND_HALF_UP);
		return bigd;
	}
	
public String[] getToeicTeacherCredentials(String path) throws Exception {

	NewUxClassicTestPage classicTest = new NewUxClassicTestPage(webDriver, testResultService);
	HashMap<String, String> users = classicTest.getUsersListFromCSV(path);
	int randomIndex = (int) (Math.random() * users.size());
	
	String randomUserName = users.keySet().toArray()[randomIndex].toString();
	String password = users.get(randomUserName);
	String[] teacher = {randomUserName,password};
	
	return teacher;

}
	
}
