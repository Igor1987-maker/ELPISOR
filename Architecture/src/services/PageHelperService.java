package services;

import Enums.*;
import Objects.*;
import com.github.romankh3.image.comparison.ImageComparison;
import com.github.romankh3.image.comparison.ImageComparisonUtil;
import com.github.romankh3.image.comparison.model.ImageComparisonResult;
import com.github.romankh3.image.comparison.model.ImageComparisonState;
import drivers.AndroidAppiumWebDriver;
import drivers.AndroidWebDriver;
import drivers.GenericWebDriver;
import drivers.HeadlessBrowser;
import org.apache.commons.lang3.ArrayUtils;
import org.imsglobal.lti.launch.LtiOauthSigner;
import org.imsglobal.lti.launch.LtiSigningException;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.LocalDateTime;
import org.json.JSONObject;
import org.openqa.selenium.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pageObjects.EdoHomePage;
import pageObjects.EdoLoginPage;
import pageObjects.edo.NewUXLoginPage;
import pageObjects.edo.NewUxHomePage;
import pageObjects.edo.NewUxMyProfile;
import pageObjects.edo.TestEnvironmentPage;
import pageObjects.tms.TmsHomePage;
import pageObjects.tms.TmsLoginPage;
import tests.edo.newux.BasicNewUxTest;
import tests.edo.newux.testDataValidation;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.*;
import java.util.regex.Pattern;

@Service("pageHelperService")
public class PageHelperService extends GenericTestObject {

	GenericWebDriver webDriver;

	@Autowired
	Configuration configuration;
	@Autowired
	TextService textService;

	@Autowired
	NetService netService;

	@Autowired
	DbService dbService;

	@Autowired
	StudentService studentService;
	
	@Autowired
	Reporter report;

	@Autowired
	HeadlessBrowser headlessBrowser;

	// TestResultService testResultService;

	// @Autowired
	// AudioService audioService;

	List<Course> courses = null;
	List<Recording> recordings = null;
	private boolean edoLogoutNeeded;
	private boolean tmsLogoutNeeded;
	private AutoInstitution autoInstitution;
	private Student student;
	private Teacher teacher;
	private Supervisor supervisor;
	private UserObject schoolAdmin;
	public boolean ciEnv = true;
	public static String buildId;
	public static String linkED;
	public static String linkTmsStaticSite;
	public static String linkEdStaticSite;
	public String ed_BuildNumber;
	public String tms_buildNumber;
	public String buildIdTMS;
	public String associationWebSite = "http://azure2020:81/";
	public String buildDefinitionED;
	public String buildDefinitionTMS;
	public String CILink;
	public String CILinkTMS;
	public static String buildPath;
	public String buildPathTMS;
	static public String branchCI;
	public String buildPathForExternalPagesSmb;
	public String buildPathForExternalPages;
	public String urlForExternalPages;
	public String  staticUrlForExternalPages;
	static public String edApiUrl="";
	static public String edUiServicesUrl="";
	static public String toeicUrl = "https://edtoeic1.edusoftrd.com"; // Internal
	static public String toeicOMSUrl = "https://toeicmng.edusoftrd.com";
	static public String userFilePath="files/TOEIC/Users.csv";
	static public String userFilePathProd = "files/TOEIC/Users_MergeProd.csv";
	static public String usersFilePathProdExternal = "files/TOEIC/UsersProduction.csv";
	static public String teachersOMSFilePath="";//="files/TOEIC/OmsTeacher_Interanl.csv";
	static public String teachersOMSFilePathProd="files/TOEIC/OmsTeacher_InteranlProd.csv";
	static public String teachersOMSFileProduction="files/TOEIC/OmsTeacher_Production.csv";
	static public String recFolder = "";
	static public String sourceBranch= "refs/heads/";
	static public String sharePhisicalFolder = "\\\\frontqa2016\\EdResDeploy\\";
	static public String ServerPath= sharePhisicalFolder+"Runtime\\Metadata\\Courses\\";
	//static public String edUiServicePath = "smb://frontqa2016//wwwroot//EdUiWebServices//";
	static public String edUiServicePath = "\\\\frontqa2016\\wwwroot\\EdUiWebServices\\";
	static public String edxUrl = "edexcellence";
	//static public String automationServer="CI-SRV";
	static public String automationServer="CI";
	//static public String batFilesRestartPoolPath = "//"+automationServer+"/automation/BatFiles/Main/";
	static public String []  batFilesRestartPoolPath= new String[2];
	//private static final String CI_Folder = "smb://CI-SRV//ApplicationEnvironments//";
	//private static final String CI_Folder = "smb://CI-SRV//ApplicationEnvironments//";
	public static String CI_FolderSmb = "smb://CI-SRV//BuildsArtifacts//";
	public static String CI_Folder = "\\\\CI-SRV\\BuildsArtifacts\\";
	//private static String CI_Folder = "smb://CI//BuildsArtifacts//";
	private static final String Static_QA_Server ="\\"+"\\frontqa2016\\";
	static public String institutionMatrixId_newTE = "";
	static public String institutionMatrixId_oldTE = "";
	static public String physicalPath ="";
	static public String checkHealthURL ="";
	static public String santianaURL ="";
	//public static final String resPathRoot = null;


	private String sutUrl;
	static public boolean directLink=false;
	
	public PageHelperService() {

	}

	public void init(GenericWebDriver webDriver,
			AutoInstitution autoInstitution, TestResultService testResultService)
			throws Exception {
		this.testResultService = testResultService;
		this.webDriver = webDriver;
		this.autoInstitution = autoInstitution;
		if (!configuration.getAutomationParam("coursesCsvFileName",
				"coursesCsvFileName").equals("")) {
			courses = loadCoursedDetailsFromCsv("files/csvFiles/"
					+ configuration.getProperty("coursesCsvFileName"));
		} else {
			courses = loadCoursedDetailsFromCsv();
		}

		recordings = loadRecordings();
		student = new Student();
		teacher = new Teacher();
		supervisor = new Supervisor();
		schoolAdmin = new UserObject();

		// check if student parameter is in maven command line
		student.setUserName(configuration.getAutomationParam("student",
				"studentCMD"));
		// if (System.getProperty("student") != null) {
		// student.setUserName(System.getProperty("student"));
		// } else {
		// student.setUserName(configuration.getProperty("student.user.name"));
		// }
		student.setPassword(configuration.getProperty("student.user.password"));

		teacher.setUserName(configuration.getAutomationParam(
				"teacher.username", "teacher"));
		teacher.setPassword(configuration.getProperty("teacher.password"));

		supervisor.setUserName(configuration.getProperty("supervisor.user"));
		// System.out.println("School admin from properties file is: "
		// + configuration.getProperty("shcoolAdmin.user"));
		schoolAdmin.setUserName(configuration.getProperty("schoolAdmin.user"));

	}

	public EdoHomePage loginAsStudent(Student student) throws Exception {
		this.student = student;
		return loginAsStudent();
	}

	public EdoHomePage loginAsStudent() throws Exception {

		EdoHomePage edoHomePage = null;
		EdoLoginPage edoLoginPage = new EdoLoginPage(webDriver,
				testResultService);
		try {

			edoLoginPage.OpenPage(getSutAndSubDomain());
			// TODO - check if there is DB access
			setUserLoginToNull(dbService.getUserIdByUserName(
					student.getUserName(), autoInstitution.getInstitutionId()));
			edoHomePage = edoLoginPage.login(student);
			// edoHomePage.waitForPageToLoad();
			edoLogoutNeeded = true;
		} catch (UnhandledAlertException e) {
			// TODO Auto-generated catch block
			webDriver.getUnexpectedAlertDetails();
		} finally {
			webDriver.closeAlertByAccept();
		}

		return edoHomePage;
	}

	public EdoHomePage loginAsTeacher() throws Exception {
		return loginAsTeacher(teacher.getUserName(),
				configuration.getProperty("institutaion.subdomain"));
	}

	public EdoHomePage loginAsTeacher(String teacherUserName,
			String instSubDomain) throws Exception {
		teacher.setUserName(teacherUserName);
		EdoLoginPage edoLoginPage = new EdoLoginPage(webDriver,
				testResultService);
		edoLoginPage.OpenPage(getSutAndSubDomain(instSubDomain));

		teacher.setPassword(configuration.getProperty("teacher.password"));
		String instId = dbService.getInstituteIdByName(instSubDomain.replace(
				".aspx", ""));
		setUserLoginToNull(dbService.getUserIdByUserName(teacherUserName,
				instId));

		EdoHomePage edoHomePage = edoLoginPage.login(teacher);
		// edoHomePage.waitForPageToLoad();
		webDriver.closeAlertByAccept();
		return edoHomePage;
	}

	public EdoHomePage loginAsSupervisor() throws Exception {
		EdoLoginPage edoLoginPage = new EdoLoginPage(webDriver,
				testResultService);
		EdoHomePage edoHomePage = null;
		try {
			edoLoginPage.OpenPage(getSutAndSubDomain());

			supervisor
					.setUserName(configuration.getProperty("supervisor.user"));
			supervisor
					.setPassword(configuration.getProperty("supervisor.pass"));
			setUserLoginToNull(dbService.getUserIdByUserName(
					supervisor.getUserName(),
					autoInstitution.getInstitutionId()));
			edoHomePage = edoLoginPage.login(supervisor);
			edoHomePage.waitForPageToLoad();
		} catch (UnhandledAlertException e) {
			// TODO Auto-generated catch block
			webDriver.closeAlertByAccept();
		}
		webDriver.closeAlertByAccept();
		return edoHomePage;

	}

	public EdoHomePage loginAsSchoolAdmin() throws Exception {
		EdoLoginPage edoLoginPage = new EdoLoginPage(webDriver,
				testResultService);
		EdoHomePage edoHomePage = null;
		try {
			edoLoginPage.OpenPage(getSutAndSubDomain());
			schoolAdmin.setPassword(configuration
					.getProperty("schoolAdmin.pass"));
			schoolAdmin.setUserName(configuration
					.getProperty("schoolAdmin.user"));
			setUserLoginToNull(dbService.getUserIdByUserName(
					schoolAdmin.getUserName(),
					autoInstitution.getInstitutionId()));
			edoHomePage = edoLoginPage.login(schoolAdmin);
			edoHomePage.waitForPageToLoad();
		} catch (UnhandledAlertException e) {
			// TODO Auto-generated catch block
			webDriver.closeAlertByAccept();
		}
		webDriver.closeAlertByAccept();
		return edoHomePage;

	}

	public TmsHomePage loginToTmsAsAdmin() throws Exception {
		TmsLoginPage tmsLoginPage = new TmsLoginPage(webDriver,
				testResultService);
		tmsLoginPage.OpenPage(getTmsUrl());
		TmsHomePage tmsHomePage = null;
		try {
			SchoolAdmin schoolAdmin = new SchoolAdmin();
			schoolAdmin.setUserName(configuration.getProperty("tmsadmin.user"));
			schoolAdmin.setPassword(configuration
					.getProperty("tmsadmin.password"));
			tmsHomePage = tmsLoginPage.Login(schoolAdmin);
			tmsHomePage.waitForPageToLoad();
		} catch (UnhandledAlertException e) {
			// TODO Auto-generated catch block
			webDriver.closeAlertByAccept();
		}
		webDriver.closeAlertByAccept();
		tmsLogoutNeeded = true;
		return tmsHomePage;

	}

	public String getSutAndSubDomain() {
		return getSutAndSubDomain(configuration
				.getProperty("institutaion.subdomain"));
	}

	public String getSutAndSubDomain(String subDomanin) {
		String str = configuration.getAutomationParam(
				AutoParams.sutUrl.toString(), AutoParams.sutUrl.toString())
				+ "//" + subDomanin;
		System.out.println("SUT is: " + str);
		return str;

	}

	public String getTmsUrl() {
		return configuration.getProperty("tms.url");
	}

	public List<Course> loadCoursedDetailsFromCsv() throws Exception {
		// "files/csvFiles/Courses.csv"
		// String filepath = configuration.getAutomationParam("coursesFilePath",
		// null);
		// if (filepath == null) {
		String filepath = "files/csvFiles/Courses.csv";
		// }
		return loadCoursedDetailsFromCsv(filepath);
	}

	public List<Course> loadCoursedDetailsFromCsv(String filePath)
			throws Exception {
		List<String[]> courses = textService.getStr2dimArrFromCsv(filePath);
		List<Course> coursesList = new ArrayList<Course>();
		for (int i = 0; i < courses.size(); i++) {
			Course course = new Course();
			course.setName(courses.get(i)[0]);

			CourseUnit courseUnit = new CourseUnit();
			courseUnit.setName(courses.get(i)[1]);

			UnitComponent unitComponent = new UnitComponent();
			unitComponent.setName(courses.get(i)[2]);
			unitComponent.setStageNumber(courses.get(i)[3]);

			courseUnit.addUnitComponent(unitComponent);

			course.AddUnit(courseUnit);

			coursesList.add(course);

			// courseUnit.setUnitComponent(unitComponent);

		}
		return coursesList;

	}
	
	
	public CourseNew setCourseUnitsByCourseId(String courseId) throws Exception {
		
		List<String[]> unitsDetails = dbService.getCourseUnitDetils(courseId);
		CourseNew course = new CourseNew();
		course.setId(courseId);
		course.setName(dbService.getCourseNameById(courseId));
		
		String unitId = "undefined";
		String unitName = "undefined";
		
		for (int i = 0; i < unitsDetails.size(); i++) {
			
			CourseUnitNew courseUnit = new CourseUnitNew();
								
			unitId = unitsDetails.get(i)[0];
			unitName = unitsDetails.get(i)[1];
			
			courseUnit = setUnitComponentsByUnitId(unitId);
			courseUnit.setName(unitName);
			courseUnit.setId(unitId);
			
			course.addUnit(courseUnit);

		}

		return course;
				
	}
	
	public CourseUnitNew setUnitComponentsByUnitId(String unitId) throws Exception {
		
		List<String[]> componentsDetails = dbService.getComponentDetailsByUnitId(unitId);
		CourseUnitNew courseUnit = new CourseUnitNew();
								
		String unitComponentId = "undefined";
		String unitComponentName = "undefined";
		String unitComponentSubject = "undefined";
		String unitComponentSkillId = "undefined";
		String unitComponentSkillName = "undefined";
		
		for (int i = 0; i < componentsDetails.size(); i++) {
			
			CourseUnitComponentNew courseUnitComponent = new CourseUnitComponentNew();
			
			unitComponentId = componentsDetails.get(i)[1];
			unitComponentName = componentsDetails.get(i)[0];
			unitComponentSubject = componentsDetails.get(i)[3];
			unitComponentSkillId = componentsDetails.get(i)[2];
			unitComponentSkillName = getSkillNameById(unitComponentSkillId);
			
			
			courseUnitComponent.setId(unitComponentId);
			courseUnitComponent.setComponentSkill(unitComponentSkillId);
			courseUnitComponent.setComponentSkillName(unitComponentSkillName);
			courseUnitComponent.setComponentSubject(unitComponentSubject);
			if (!unitComponentSubject.equalsIgnoreCase("Story") && unitComponentSkillName.equals("Grammar")) {
				courseUnitComponent.setName(unitComponentSubject + ":" + unitComponentName);
			} else {
				courseUnitComponent.setName(unitComponentName);
			}
			
			courseUnit.addUnitComponent(courseUnitComponent);

		}

		return courseUnit;
				
	}
	
	public String getSkillNameById (String skillID) throws Exception {
		
		String skill = "undefined";
		
		switch (skillID) {
		
		case "6":
			skill = Skill.Grammar.toString();
			break;
		case "2":
			skill = Skill.Reading.toString();
			break;
		case "3":
			skill = Skill.Listening.toString();
			break;
		case "4":
			skill = Skill.Speaking.toString();
			break;
		case "10":
			skill = Skill.Vocabulary.toString();
			break;
		case "11":
			skill = Skill.Alphabet.toString();
			break;
		case "12":
			skill = Skill.Writing.toString();
			break;
		
		default: skill=skill;
		
		}
		
		return skill;
				
	}
	

	public List<Course> getCourses() {
		return courses;
	}

	public Course initCouse(int courseId) {
		Course course = new Course();
		course.setName(courses.get(courseId).getName());
		course.setCourseUnit(courses.get(courseId).getCourseUnits().get(0)
				.getName());
		course.setUnitComponent(courses.get(courseId).getCourseUnits().get(0)
				.getUnitComponent().get(0).getName());
		course.setComponentStage(Integer.valueOf(courses.get(courseId)
				.getCourseUnits().get(0).getUnitComponent().get(0)
				.getStageNumber()));
		return course;
	}

	public void checkClassWasCreated(String className, String institutionId)
			throws Exception {
		String sql = "select * from Class where Name='" + className
				+ "' and institutionId=" + institutionId;
		dbService.getStringFromQuery(sql);
	}

	public void startRecording(String fileName) throws Exception {
		// TODO 1. click on the recored button
		// audioService.sendSoundToVirtualMic(new File(fileName));
	}

	public List<Recording> loadRecordings() throws Exception {
		List<String[]> recordingsCsv = textService
				.getStr2dimArrFromCsv("files/csvFiles/recordingResults.csv");
		List<Recording> recordings = new ArrayList<Recording>();
		for (int i = 0; i < recordingsCsv.size(); i++) {
			Recording recording = new Recording();
			recording.setId(recordingsCsv.get(i)[0]);

			String[] Wlevels = textService.splitStringToArray(
					recordingsCsv.get(i)[1], "\\|");
			String[] Slevels = textService.splitStringToArray(
					recordingsCsv.get(i)[2], "\\|");
			String[] files = textService.splitStringToArray(
					recordingsCsv.get(i)[3], "\\|");

			List<Integer> SL = new ArrayList<Integer>();
			List<String[]> WL = new ArrayList<String[]>();
			List<File> recFiles = new ArrayList<File>();

			for (int j = 0; j < Wlevels.length; j++) {
				WL.add(textService.splitStringToArray(Wlevels[j]));
				SL.add(Integer.valueOf(Slevels[j]));
				//recFiles.add(new File("files/audioFiles/" + files[j]));
				recFiles.add(new File("//"+configuration.getGlobalProperties("logserverName")+"/AutoLogs/ToolsAndResources/shared/files/audioFiles/" + files[j]));
			}
			recording.setWL(WL);
			recording.setSL(SL);
			recording.setRecordingFiles(recFiles);

			recordings.add(recording);

		}

		return recordings;

	}

	public List<Recording> getRecordings() {
		return recordings;
	}

	public void logOut() throws Exception {
		// webDriver.waitForElement("Log Out", ByTypes.linkText).click();
		// // webDriver.switchToFrame("lastAct");
		// webDriver.switchToFrame(webDriver.waitForElement(
		// "//iframe[contains(@src,'LogOut')]", ByTypes.xpath));
		// // webDriver.closeAlertByAccept();
		// webDriver.waitForElement("btnOk", ByTypes.id).click();

	}

	public boolean isLogoutNeeded() {
		return edoLogoutNeeded;
	}

	public void setLogoutNeeded(boolean logoutNeeded) {
		this.edoLogoutNeeded = logoutNeeded;
	}

	public void setUserLoginToNull(String id) throws Exception {
		String sql = "Update users set logedin = null where userid=" + id;
		dbService.runDeleteUpdateSql(sql);
	}

	public String[] getClassesForImport(String institutionId) throws Exception {
		String sql = "select top 1 Name from class where institutionId="
				+ institutionId + "  order by Name ";
		List<String> classes = dbService.getArrayListFromQuery(sql, 5);
		String[] classesStr = classes.toArray(new String[classes.size()]);
		return classesStr;
	}

	public String[] getStudentsForExporte(String objectName, int count,
			String institutionId, String by) throws Exception {
		String sql = "select top " + count + " " + by + " from " + objectName
				+ " where institutionId=" + institutionId + " order by " + by;
		List<String> objects = dbService.getArrayListFromQuery(sql, 5);
		String[] objectsArr = objects.toArray(new String[objects.size()]);
		return objectsArr;
	}

	public String[] convertStudentIdsToNames(String[] students)
			throws Exception {
		String[] studentNames = new String[students.length];
		for (int i = 0; i < students.length; i++) {
			studentNames[i] = dbService.getUserNameById(students[i],
					autoInstitution.getInstitutionId());
		}
		return studentNames;
	}

	public List shuffleList(List list) throws Exception {

		long seed = System.nanoTime();
		Collections.shuffle(list, new Random(seed));
		return list;

	}

	public Student getStudent() {
		return student;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void calculateSLbyWL(String[] WL, String SL) {
		Double wordLevels = 0.0;
		for (int i = 0; i < WL.length; i++) {
			wordLevels += Integer.valueOf(WL[i]);
		}
		wordLevels = wordLevels / WL.length;
		wordLevels = Math.ceil(wordLevels);
		int wl = wordLevels.intValue();
		// System.out.println("Rounded avg: " + wl);

		// System.out.println("testResultService:"+testResultService.toString());
		testResultService.assertEquals(String.valueOf(wl), SL,
				"Sentence level do not match");

	}

	public void addStudentsToMultileClasses(int numOfStudents,
			String[] classNames, String instId) throws Exception {

		int studentsPerClass = numOfStudents / classNames.length;

		for (int i = 0; i < classNames.length; i++) {
			for (int j = 0; j < studentsPerClass; j++) {
				addStudent("student" + dbService.sig(4), classNames[i], instId);
			}

		}
	}

	public void addStudent(String studentName) throws Exception {
		addStudent(studentName, configuration.getProperty("classname"));
	}

	public void addStudent(String studentName, String className)
			throws Exception {
		addStudent(studentName, className,
				configuration.getProperty("institution.id"));
	}

	public void addStudent(String studentName, String className, String instId)
			throws Exception {

		// ************Using API to create the user

		createUserUsingApi(configuration.getProperty("sut.url"), studentName,
				studentName, studentName, "12345", instId, className);
	}

	public String createUSerUsingSP(String instId, String className) throws Exception
			 {
		String studentUserName = "stud" + dbService.sig(5);
		createUSerUsingSP(instId, studentUserName, studentUserName,
				studentUserName, "12345", studentUserName + "@edusoft.co.il",
				className);
		
		String userId = dbService.getUserIdByUserName(studentUserName, instId);
		
		if (userId == null) {
			testResultService.addFailTest("User was not created", true, false);
		}
	//--igb 2018.06.26-------------
		else {
			try {
				setOnBoardingToVisited(userId);
				dbService.setUserOptIn(userId, true);
				
				dbService.insertUserToAutomationTable(instId,userId,studentUserName,className,linkED);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	//--igb 2018.06.26-------------
		
		report.report("UserName is: "+ studentUserName + "UserId is:" + userId);
		return userId;
	}
	public String createUSerUsingOptIn(String instId, String className)
			throws Exception {
		String studentUserName = "stud" + dbService.sig(8);
		createUSerUsingSP(instId, studentUserName, studentUserName,
				studentUserName, "12345", studentUserName + "@edusoft.co.il",
				className);
		
		String userId = dbService.getUserIdByUserName(studentUserName, instId);
		
		if (userId == null) {
			testResultService.addFailTest("User was not created", true, false);
		}
		report.report("UserName is: "+ studentUserName + "UserId is:" + userId);
		
		return userId;
	}
	
	/**
	 * 
	 * @return User id
	 * @throws Exception
	 */
	public String createUSerUsingSP() throws Exception {
		return createUSerUsingSP(configuration.getInstitutionId(),
				configuration.getClassName());
	}

	public void createUSerUsingSP(String institutionId, String userName,
			String fname, String lname, String pass, String email,
			String className)  {
		String sp = "exec APIInsertUser " + institutionId + ",'" + userName
				+ "','" + fname + "','" + lname + "','" + pass + "','" + email
				+ "','" + className + "'";
		try {
			dbService.runStorePrecedure(sp, false, false);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setOnBoardingToVisited(String userId) throws Exception {
			String sqlQuery = "insert into OnBoardingStates (UserId, AreaId,ShowState) values "
					+ "('" + userId + "','1','1')," + "('" + userId + "','2','1')," 
					+ "('" + userId + "','3','1')," + "('" + userId + "','4','1'),"
					+ "('" + userId + "','5','1')," + "('" + userId + "','6','1'),"
					+ "('" + userId + "','7','1')," + "('" + userId + "','8','1'),"
					+ "('" + userId + "','9','1');";
			dbService.runDeleteUpdateSql(sqlQuery);
			
			//String sp = "exec SetOnBoardingUserState " + userId + ",'1','1'";
			//dbService.runStorePrecedure(sp, false, false);
	}

	public void createUserUsingApi(String sut, String userName, String fname,
			String lname, String pass, String instId, String className)
			throws Exception {

		String request = sut + "/api/template/InsertUser.aspx?InstId=" + instId
				+ "&ClassName=" + className + "&UserName=" + userName
				+ "&FirstName=" + fname + "&LastName=" + lname + "&Password="
				+ pass + "&Email=test40@edusoft.co.il";

		// NetService netService = new NetService();
		netService.sendHttpRequest(request);

	}

	public void createClassUsingSP(String sut, String className,
			String institutionId, String packageName) throws Exception {

		// String request = sut + "/api/template/CreateClass.aspx?InstId="
		// + institutionId + "&ClassName=" + className + "&PackageName="
		// + packageName + "&startDate=" + startDate;
		// System.out.println(request);
		// netService.sendHttpRequest(request);
		String date = getCurrentDateByFormat("yyyy/MM/d");
		//getDateString()
		String sp = "APICreateClass " + institutionId + ",'" + className
				+ "','" + packageName + "','" + date + "'";
		dbService.runStorePrecedure(sp, true, false);
	}

	public boolean[] randomizeCorrectAndIncorrectAnswers(int answersNumber) {
		int rand = dbService.getRandonNumber(1, 1000);
		System.out.println("Randon number is: " + rand);
		boolean[] questions = new boolean[answersNumber];

		// set all answersToBeTrue
		for (int i = 0; i < answersNumber; i++) {
			questions[i] = true;
		}

		if (rand < 200) {
			// test score will be 0
			for (int i = 0; i < questions.length; i++) {
				questions[i] = false;
			}

		} else if (rand > 201 && rand < 400) {
			// test score will be 25
			for (int i = 0; i < questions.length * 0.75; i++) {
				questions[i] = false;
			}
		} else if (rand > 401 && rand < 600) {
			// test score will be 50
			for (int i = 0; i < questions.length * 0.5; i++) {
				questions[i] = false;
			}
			// test score will be 75
		} else if (rand > 601 && rand < 800) {
			for (int i = 0; i < questions.length * 0.25; i++) {
				questions[i] = false;
			}
		} else if (rand > 801 && rand <= 1000) {
			// test score will be 100

		}
		return questions;
	}

	public void waitForDateTime(DateTime timeToWaitFor)
			throws InterruptedException {
		LocalDateTime localDateTime = new LocalDateTime();

		Duration myDuration = new Duration(timeToWaitFor.toDateTime(),
				localDateTime.toDateTime());
		System.out.println("Seconds left: " + myDuration.getStandardSeconds());
		Thread.sleep(myDuration.getMillis() + 60000);
	}

	public NewUXLoginPage openCILatestLoginPage() throws Exception {
		try {
			headlessBrowser.init(webDriver.getRemoteMachine(), false);

			headlessBrowser.openUrl("http://vstf2013:9010/WebUX");

			String link = headlessBrowser
					.waitForElement(
							"//div[@class='container']//table//tbody//tr[1]//td//div[1]//div//a",
							ByTypes.xpath).getAttribute("href");
			link = link + "/qa";
			CILink = link;
			webDriver.openUrl(link);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} finally {
			headlessBrowser.closeBrowser();

		}
		return new NewUXLoginPage(webDriver, testResultService);
	}

	public String getCILatestUXLink() throws Exception {
		// headlessBrowser.init(webDriver.getRemoteMachine(), false);
		//
		// headlessBrowser.openUrl("http://vstf2013:9010/WebUX");
		//
		// String link = getLatestCILinkUX();
		String link = getCILatestLinkFromDb("WEBUX_CI",sourceBranch);
		// String link =
		// "http://ci-srv:9010/WebUX_CI_20150331.4/automation#/login";

		if (webDriver instanceof AndroidWebDriver) {
			link = link.replace("ci-srv", "10.1.0.14");
		}
		return link;
	}

	public NewUxHomePage openCILatestUXLink() throws Exception {

		
		
		try {
			linkED = "";
			String linkTMS = "https://tmsux.edusoftrd.com";
			buildId = "WebUx"; // valid for static QA site 
			buildIdTMS = "TmsUx"; // valid for static QA site 	
			buildPath = "smb://frontqa2016//wwwroot//WebUx//"; // valid for static QA site 
			buildPathTMS = "smb://frontqa2016//TmsUx//"; // valid for static QA site
			buildPathForExternalPagesSmb = "smb://frontqa2016//EdResDeploy//"; // valid for External Login static QA site
			buildPathForExternalPages = "\\\\frontqa2016\\EdResDeploy\\"; // valid for External Login static QA site
			//physicalPath = "dev2008/EdoNet300Res";
			physicalPath = "frontqa2016/EdResDeployDev";
			//physicalPath = "CI-SRV/AutomationFiles/TestFiles/Main";
			urlForExternalPages = "https://edresdeploy.edusoftrd.com/";
			staticUrlForExternalPages = "https://edresdeploydev.edusoftrd.com/";
			DbService.currentGeneralDB= "ci"; //"prod2";
			edApiUrl = "http://edapi.edusoftrd.com";
			edUiServicesUrl	= "https://eduiservices.edusoftrd.com";
			edUiServicePath = "smb://frontqa2016/wwwroot/EdUiWebServices/";	
			//edUiServicePath = "\\\\frontqa2016\\wwwroot\\EdUiWebServices\\";
			branchCI = configuration.getAutomationParam("ci", "prod2");
			automationServer = configuration.getGlobalProperties("logserverName");
			linkEdStaticSite = "edux.edusoftrd.com";
			
			//branchCI = configuration.getAutomationParam("ci", "WebUXApp-Prod_CI");
			//String profile = configuration.getAutomationParam("-P", "-P");
			//report.addTitle("The profile is: "+profile);
			String offlineIP = configuration.getAutomationParam("remote.machine", "machine").split(":")[1].replace("//", "");
			
			// is it direct link?
			if (branchCI.contains(".com")){
				linkED = branchCI;
				branchCI = getCIBrnachName();
			}
	
	
											
			else if (branchCI.equalsIgnoreCase("WebUXApp_CI")) {		
				
				buildDefinitionED = "WebUXApp_CI";
				buildDefinitionTMS = "TMSUX_Dev";
				
				linkED = getCILatestLinkFromDb(buildDefinitionED,sourceBranch + "main");
				linkTMS = getCILatestLinkFromDb(buildDefinitionTMS,sourceBranch + "Content"); 
				
				setBuildPathCI(linkED);
				setBuildPathCI_TMS(linkTMS);
				buildPathForExternalPages = buildPath;
				urlForExternalPages  = linkED;
				//dbService.setUsingCIDB(true);
				DbService.currentGeneralDB="ci";
				
				
				
			} else if (branchCI.equalsIgnoreCase("WebUXAppProd_CI")) {		
				
				buildDefinitionED = "WebUXAppProd_CI";
				buildDefinitionTMS = "TMSUX2_Prod";
				
				linkED = getCILatestLinkFromDb(buildDefinitionED,sourceBranch + "main");
				linkTMS = getCILatestLinkFromDb(buildDefinitionTMS,sourceBranch + "Content"); 
				
				setBuildPathCI(linkED);
				setBuildPathCI_TMS(linkTMS);
				buildPathForExternalPages = buildPath;
				urlForExternalPages  = linkED;
				//dbService.setUseProd2DB(true);
				DbService.currentGeneralDB="prod2";
				
			
			} else if (branchCI.equalsIgnoreCase("EDUI_CI")) {		
				
				buildDefinitionED = "EDUI_CI";
				buildDefinitionTMS = "TMSUX_Dev";
	
				linkED = getCILatestLinkFromDb(buildDefinitionED,sourceBranch + "main");
				linkTMS = getCILatestLinkFromDb(buildDefinitionTMS,sourceBranch + "Content"); 
				
				setBuildPathCI(linkED);
				setBuildPathCI_TMS(linkTMS);
				buildPathForExternalPages = buildPath;
				urlForExternalPages  = linkED;
				//dbService.setUseProd2DB(true);
				DbService.currentGeneralDB="ci";
				recFolder="EDUX";
				
			} else if (branchCI.equalsIgnoreCase("EDUI_CI_main")) {		
				
				buildDefinitionED = "EDUI_CI_main";
				buildDefinitionTMS = "TMS_CI_Content";
	
				linkED = getCILatestLinkFromDb(buildDefinitionED,sourceBranch + "main");
				linkTMS = getCILatestLinkFromDb(buildDefinitionTMS,sourceBranch + "Content"); 
				
				setBuildPathCI(linkED);
				setBuildPathCI_TMS(linkTMS);
				buildPathForExternalPages = buildPath;
				urlForExternalPages  = linkED;
				//dbService.setUseProd2DB(true);
				DbService.currentGeneralDB="ci";
				recFolder="EDUX";
				//batFilesRestartPoolPath = "//10.1.0.213/automation/BatFiles/Main/";
				userFilePath = "files/TOEIC/Users.csv";
				teachersOMSFilePath = "files/TOEIC/OmsTeacher_Interanl.csv";
				institutionMatrixId_oldTE = "698";
				institutionMatrixId_newTE = "640";
				edUiServicesUrl	= "https://eduiservices.edusoftrd.com";
				edxUrl = "https://edexcellence.edusoftrd.com";
			
			}else if (branchCI.equalsIgnoreCase("EDUI_CI_dev")) {		
					
					buildDefinitionED = "EDUI_CI_dev";
					buildDefinitionTMS = "TMS_CI_dev";
	
					linkED = getCILatestLinkFromDb(buildDefinitionED,sourceBranch + "dev");
					linkTMS = getCILatestLinkFromDb(buildDefinitionTMS,sourceBranch + "dev"); 
					
					setBuildPathCI(linkED);
					setBuildPathCI_TMS(linkTMS);
					buildPathForExternalPages = buildPath;
					urlForExternalPages  = linkED;
					//dbService.setUseProd2DB(true);
					DbService.currentGeneralDB="ci";
					recFolder="EDUX";
					//batFilesRestartPoolPath = "files/RestartAppPool/RestartDevServices.bat";
					batFilesRestartPoolPath [0]= "files/RestartAppPool/StopMainServices.bat";
					batFilesRestartPoolPath [1]= "files/RestartAppPool/StartMainServices.bat";
					userFilePath = "files/TOEIC/Users.csv";
					teachersOMSFilePath = "files/TOEIC/OmsTeacher_Interanl.csv";
					institutionMatrixId_oldTE = "698";
					institutionMatrixId_newTE = "640";
					edUiServicesUrl	= "https://eduiservices.edusoftrd.com";
					edxUrl ="https://edexcellencedev.edusoftrd.com";
					// RC > "https://edexcellence.edusoftrd.com";
					checkHealthURL ="https://eduiservices.edusoftrd.com/_health";
					//"https://eduiservices-dafb5063.edusoftrd.com/_health";
			}
			else if (branchCI.equalsIgnoreCase("EDUI_CI_RC_2023_12")) {

				buildDefinitionED = "EDUI_CI_RC_2023_12";
				buildDefinitionTMS = "TMS_CI_RC_2023_12";

				linkED = getCILatestLinkFromDb(buildDefinitionED,sourceBranch + "RC_2023_12");
				linkTMS = getCILatestLinkFromDb(buildDefinitionTMS,sourceBranch + "RC_2023_12");

				setBuildPathCI(linkED);
				setBuildPathCI_TMS(linkTMS);
				buildPathForExternalPages = buildPath;
				urlForExternalPages  = linkED;
				linkEdStaticSite = "eduxrc.edusoftrd.com";
				linkTmsStaticSite = "tmsuxrc.edusoftrd.com";
				DbService.currentGeneralDB="prod2";
				recFolder="EDPROD2";
				edApiUrl = "http://edapiprod.edusoftrd.com";
				sharePhisicalFolder = "\\\\frontqa2016\\EdResDeploy\\";
				ServerPath= sharePhisicalFolder+"Runtime\\Metadata\\Courses\\";
				edUiServicesUrl	= "https://eduiservicesprod.edusoftrd.com";
				edUiServicePath = "smb://frontqa2016//wwwroot//EdUiProdWebServices//";
				//batFilesRestartPoolPath = "//"+automationServer+"/automation/BatFiles/CI_Prod/";
				batFilesRestartPoolPath [0]= "files/RestartAppPool/StopRCservices.bat";
				batFilesRestartPoolPath [1]= "files/RestartAppPool/StartRCservices.bat";
				userFilePath = "files/TOEIC/Users_MergeProd.csv";
				teachersOMSFilePath = "files/TOEIC/OmsTeacher_InternalProd.csv";
				institutionMatrixId_oldTE = "1636";
				institutionMatrixId_newTE = "637";
				physicalPath = "frontqa2016/EdResDeploy";
				//physicalPath = "CI-SRV/AutomationFiles/TestFiles/RC";
				toeicUrl = "https://edtoeicrc.edusoftrd.com";
				checkHealthURL = "https://eduiservicesprod.edusoftrd.com/_health";
				edxUrl = "https://edexcellence.edusoftrd.com";
			}
			else if (branchCI.equalsIgnoreCase("EDUI_CI_RC_2023_13")) {

				buildDefinitionED = "EDUI_CI_RC_2023_13";
				buildDefinitionTMS = "TMS_CI_RC_2023_12";

				linkED = getCILatestLinkFromDb(buildDefinitionED,sourceBranch + "RC_2023_13");
				Thread.sleep(1000);
				linkTMS = getCILatestLinkFromDb(buildDefinitionTMS,sourceBranch + "RC_2023_12");

				setBuildPathCI(linkED);
				setBuildPathCI_TMS(linkTMS);
				buildPathForExternalPages = buildPath;
				urlForExternalPages  = linkED;
				linkEdStaticSite = "eduxrc.edusoftrd.com";
				linkTmsStaticSite = "tmsuxrc.edusoftrd.com";
				DbService.currentGeneralDB="prod2";
				recFolder="EDPROD2";
				edApiUrl = "http://edapiprod.edusoftrd.com";
				sharePhisicalFolder = "\\\\frontqa2016\\EdResDeploy\\";
				ServerPath= sharePhisicalFolder+"Runtime\\Metadata\\Courses\\";
				edUiServicesUrl	= "https://eduiservicesprod.edusoftrd.com";
				edUiServicePath = "smb://frontqa2016//wwwroot//EdUiProdWebServices//";
				//batFilesRestartPoolPath = "//"+automationServer+"/automation/BatFiles/CI_Prod/";
				batFilesRestartPoolPath [0]= "files/RestartAppPool/StopRCservices.bat";
				batFilesRestartPoolPath [1]= "files/RestartAppPool/StartRCservices.bat";
				userFilePath = "files/TOEIC/Users_MergeProd.csv";
				teachersOMSFilePath = "files/TOEIC/OmsTeacher_InternalProd.csv";
				institutionMatrixId_oldTE = "1636";
				institutionMatrixId_newTE = "637";
				physicalPath = "frontqa2016/EdResDeploy";
				//physicalPath = "CI-SRV/AutomationFiles/TestFiles/RC";
				toeicUrl = "https://edtoeicrc.edusoftrd.com";
				checkHealthURL = "https://eduiservicesprod.edusoftrd.com/_health";
				edxUrl = "https://edexcellence.edusoftrd.com";
			}
			else if (branchCI.equalsIgnoreCase("EDUI_Prod_CI")) {		
			
				buildDefinitionED = "EDUI_Prod_CI";
				buildDefinitionTMS = "TMSUX2_Prod";
	
				linkED = getCILatestLinkFromDb(buildDefinitionED,sourceBranch + "Prod");
				linkTMS = getCILatestLinkFromDb(buildDefinitionTMS,sourceBranch + "Content2"); 
				
				setBuildPathCI(linkED);
				setBuildPathCI_TMS(linkTMS);
				buildPathForExternalPages = buildPath;
				urlForExternalPages  = linkED;
				DbService.currentGeneralDB="prod2";
				recFolder="EDPROD2";
				sharePhisicalFolder = "\\\\frontqa2016\\EdResDeploy\\";
				ServerPath= sharePhisicalFolder+"Runtime\\Metadata\\Courses\\";
				
			} else if (branchCI.equalsIgnoreCase("offline")) {		
				
				linkED = "http://localhost/ed51/placeholder";
				linkTMS = "http://localhost/tms51";
				buildPath = "smb://"+offlineIP+"//ed51//";
				buildPathTMS = "smb://"+offlineIP+"//tms51//";
											
			} else if (branchCI.equalsIgnoreCase("TOEIC_MGMT_CI")) {		
				
					buildDefinitionED = "TOEIC_MGMT_CI";				
					linkED = getCILatestLinkFromDb(buildDefinitionED,sourceBranch + "TOEIC_MGMT_CI");
					setBuildPathCI(linkED);	
		
			} else if (branchCI.equalsIgnoreCase("TOEIC_OLPC")) {		
				
				buildDefinitionED = "toeic_olpc";
						
				linkED = getCILatestLinkFromDb(buildDefinitionED,sourceBranch +"toeic_oppc");
				setBuildPathCI(linkED);
				//dbService.setUseToeicOlpc(true);
				DbService.currentGeneralDB="TOEIC_OLPC";
				
				
			} else if (branchCI.equalsIgnoreCase("EdProduction")) {		
					

				linkED = "https://tmpwed.engdis.com/automation";
				linkTMS = "https://TMPVTMS.engdis.com";
				edUiServicesUrl = "https://tmpwedservices.engdis.com";
				toeicUrl = "https://TMPWEDTOEIC.engdis.com";


				//linkED = "https://ed.engdis.com/automation";
				//linkTMS = "https://edtms.engdis.com";
				//dbService.setUseEdproductionDb(true);
				edApiUrl = "https://edapi.engdis.com";
				DbService.currentGeneralDB="edProduction";
				DbService.edMerge2DB="edproduction2";
				//toeicUrl = "https://edtoeic.engdis.com";
				userFilePath = "files/TOEIC/UsersProduction.csv";
				teachersOMSFilePath = "files/TOEIC/OmsTeacher_Production.csv";
				toeicOMSUrl = "http://www.toeicmanagement.com/#/login";
				//edUiServicesUrl	= "https://edservices.engdis.com";
				edxUrl = "https://edexcellence.engdis.com";
				checkHealthURL = "https://edservices.engdis.com/_health";
				santianaURL ="https://ed.engdis.com/santa/educatebr";
							
			} else if (branchCI.equalsIgnoreCase("EdProduction2")) {		
				
				/*linkED = "https://ed2.engdis.com/automation";
				linkTMS = "https://tms2.engdis.com";
				//dbService.setUseEdproductionDb(true);
				edApiUrl = "https://edapi.engdis.com";
				DbService.currentGeneralDB="edProduction2";
				toeicUrl = "https://edtoeic.engdis.com";
				userFilePath = "files/TOEIC/UsersProduction.csv";
				teachersOMSFilePath = "files/TOEIC/OmsTeacher_Production.csv";
				toeicOMSUrl = "http://www.toeicmanagement.com/#/login";
				edUiServicesUrl	= "https://eduiwebservices20.engdis.com";*/

				linkED = "https://ed20.engdis.com/automation";
				linkTMS = "https://tms20.engdis.com";
				//dbService.setUseEdproductionDb(true);
				edApiUrl = "https://edapi20.engdis.com";
				DbService.currentGeneralDB="edProduction2";
				toeicUrl = "https://edtoeic.engdis.com";
				userFilePath = "files/TOEIC/UsersProduction.csv";
				teachersOMSFilePath = "files/TOEIC/OmsTeacher_Production.csv";
				toeicOMSUrl = "http://www.toeicmanagement.com/#/login";
				edUiServicesUrl	= "https://eduiwebservices20.engdis.com";
				userFilePath = "files/TOEIC/UsersProduction.csv";
				teachersOMSFilePath = "files/TOEIC/OmsTeacher_Production.csv";
				toeicOMSUrl = "http://www.toeicmanagement.com/#/login";
				//edUiServicesUrl	= "https://eduiwebservices20.engdis.com";

			} else if (branchCI.equalsIgnoreCase("EDProductionArab")) {		
				
				//linkED = "https://ediscoverbeta.engdis.com/AmraQa";
				linkED = "https://ediscoverbetagate.engdis.com/automation";
				//linkTMS = "http://tmsdiscoverbeta.engdis.com";
				linkTMS = "http://edtms.engdis.com";
				//dbService.setUseEdproductionDb(true);
				edApiUrl = "https://edapibeta.engdis.com";
				DbService.currentGeneralDB="edProductionArab";
				
			} else if (branchCI.equalsIgnoreCase("EdBeta")) {		
				
				/*linkED = "https://ednewb.engdis.com/automation";
				linkTMS = "https://edtmsbeta.engdis.com";
				//linkED = "https://test-v4.engdis.com/automation";
				//dbService.setUseEdBetaproductionDb(true);
				DbService.currentGeneralDB="edBetaProduction";
				edApiUrl = "https://edapibeta.engdis.com";
				
				toeicUrl = "https://edtoeicbeta.engdis.com"; // beta";
				userFilePath = "files/TOEIC/UsersBeta.csv";
				toeicOMSUrl = "http://toeicmgmt.engdis.com/#/login";
				teachersOMSFilePath ="files/TOEIC/OmsTeacher_beta.csv";
				edUiServicesUrl	= "https://edservicesbeta.engdis.com";
				checkHealthURL = "https://edservicesbeta.engdis.com/_health";//
				santianaURL ="https://ednewb.engdis.com/santa/educatebr";
				edxUrl = "https://EDexcellencebeta.engdis.com";*/

//=======================================================================================================

				linkED = "https://stgw-ed.engdis.com/automation";
				linkTMS = "https://stgv-tms.engdis.com";
				//linkED = "https://test-v4.engdis.com/automation";
				//dbService.setUseEdBetaproductionDb(true);
				DbService.currentGeneralDB="edBetaProduction";
				edApiUrl = "https://stgw-edapi.engdis.com";

				toeicUrl = "https://stgw-edtoeic.engdis.com"; // beta";
				userFilePath = "files/TOEIC/stagging.csv";
				toeicOMSUrl = "https://stgv-toeicmanagement.engdis.com/#/login";
				teachersOMSFilePath ="files/TOEIC/staggingTeachers.csv";
				//edUiServicesUrl	= "https://stg-edservices.engdis.com/";
				edUiServicesUrl	= "https://eastus2-stag-winwebappv7-ed.azurewebsites.net/";
				//checkHealthURL = "https://stg-edservices.engdis.com/_health";//
				checkHealthURL = "https://eastus2-stag-winwebappv7-ed.azurewebsites.net/_health";//
				santianaURL ="https://stg-ed.engdis.com/santa/educatebr";
				edxUrl = "https://stg-edexcellence.engdis.com";
				
			} else ciEnv = false; 
						
			if ((webDriver instanceof AndroidWebDriver) || (webDriver instanceof AndroidAppiumWebDriver)) {
				linkED = linkED.replace("ci-srv", "192.168.39.70");
			}
			
			if (branchCI.equalsIgnoreCase("offline"))
				linkED = linkED.replace("placeholder", configuration.getProperty("institution.name"));
					
			initializeData();
			
			if (!(branchCI.contains("TOEIC"))|| (branchCI.contains("TOEFL") )){
				
				if (branchCI.isEmpty())
					linkED = "https://edux.edusoftrd.com/"; // take link of static QA site for January 2016 ED Release
				
				if (!(branchCI.contains("EdProduction") || branchCI.contains("EdBeta") && !(branchCI.contains("Arab")))) // in case of CI
					linkED = linkED.split(".com")[0] + ".com/" + BasicNewUxTest.institutionName; //configuration.getProperty("institution.name");
				
				else // in case of production
					{
					String instName = BasicNewUxTest.institutionName;
					instName = instName.replace("@", "");
			    	linkED = "https://" + BasicNewUxTest.CannonicalDomain; // we comment it for redirect testing
					}
			}			
			
			String instId = System.getProperty("instId");
			
			if (!(instId == null)) {
			//	String instName = dbService.getInstituteNameById(instId);
				//linkED = linkED.replace("@automation", instName);
			}
			
			System.clearProperty("instId");
			CILink = linkED;
			CILinkTMS = linkTMS.replaceAll(".com/", ".com");
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block

		} finally {
			headlessBrowser.quitBrowser();
			System.clearProperty("instId");
			webDriver.openUrl(linkED);

		}
		return new NewUxHomePage(webDriver, testResultService);
	}

	public void initializeData() throws Exception {
		String instDetails[] = null;

		if (BasicNewUxTest.institutionName.equalsIgnoreCase(""))
			BasicNewUxTest.institutionName=BasicNewUxTest.institutionsName[0];

		instDetails = dbService.getInstituteIdAndCannonicalDomainByName(BasicNewUxTest.institutionName);
		BasicNewUxTest.institutionId =  instDetails[0];
		BasicNewUxTest.CannonicalDomain = instDetails[1];
	}

	public String getCILatestLinkFromDb(String branch,String sourceBranch) throws Exception {

		List<String[]> records = null;
		String link = null;
		String sql = "";
		
		if (directLink && branch.toLowerCase().contains("ed"))
			sql = "SELECT TOP 1 URL,BuildNumber,Server FROM [ApplicationEnvironments].[dbo].[Environments]  where url like '%"+linkED+"%'";
		else{ 
			
			sql = "IF NOT EXISTS"
					+ " ("
					+		" SELECT TOP 1 URL FROM [ApplicationEnvironments].[dbo].[Environments]  where Buildnumber like '%"+branch+"%' AND SourceBranch = '"+sourceBranch+"' AND UsedBy = 'QA' order by CreationDate desc"
					+	" )"
					+ " Begin"
					+		" UPDATE [ApplicationEnvironments].[dbo].[Environments] SET UsedBy = 'qa' where url =" 
					+			" (" 
					+			" SELECT TOP 1 URL FROM [ApplicationEnvironments].[dbo].[Environments]  where Buildnumber like '%"+branch+"%' AND SourceBranch = '"+sourceBranch+"' order by CreationDate desc"
					+			" )"
					+ 		" SELECT TOP 1 URL,BuildNumber,ApplicationPath,Server FROM [ApplicationEnvironments].[dbo].[Environments]  where Buildnumber like '%"+branch+"%' AND SourceBranch = '"+sourceBranch+"' and UsedBy = 'QA' order by CreationDate desc"
					+ " End"
					+	" ELSE"
					+		" SELECT TOP 1 URL,BuildNumber,ApplicationPath,Server FROM [ApplicationEnvironments].[dbo].[Environments]  where Buildnumber like '%"+branch+"%' AND SourceBranch = '"+sourceBranch+"' and UsedBy = 'QA' order by CreationDate desc"
					;
		}
			try {	
				dbService.setUsingTfsDB(true);
				records = dbService.getStringListFromQuery(sql, 1,true);
				
				if (records!= null){
					link = records.get(0)[0];
					
					if (link.toLowerCase().contains("edui-")){
						//link.toLowerCase().contains("WebUX") ||
						ed_BuildNumber = records.get(0)[1];
						//automationServer = records.get(0)[3];
						//CI_Folder = "smb://"+automationServer+"//BuildsArtifacts//";
					}
					if (link.toLowerCase().contains("tms-"))
						tms_buildNumber = records.get(0)[1];
				}
			}
			
			finally {
				dbService.setUsingTfsDB(false);
			}
		
		return link;
	}
	
	public TmsHomePage closeEDAndOpenTMS() throws Exception {
		webDriver.closeBrowser();
		String buildDefinitionTMS = "";
		//branchCI = configuration.getAutomationParam("ci", "prod2");
		/*if (branchCI.equalsIgnoreCase("ci")) {
			buildDefinitionTMS = "TMSUX_Dev";}
		
		else if (branchCI.equalsIgnoreCase("prod2")) {		
			buildDefinitionTMS = "TMSUX2_Prod";}
		
		else if (branchCI.equalsIgnoreCase("WebUXApp_CI") || branchCI.equalsIgnoreCase("EDUI_CI") || branchCI.equalsIgnoreCase("EDUI_CI_main")) {		
			buildDefinitionTMS = "TMSUX_Dev";}
		
		else if (branchCI.equalsIgnoreCase("WebUXAppProd_CI")) {		
			buildDefinitionTMS = "TMSUX2_Prod";}
		
		else if (branchCI.equalsIgnoreCase("EDUI_CI_Prod")) {		
			buildDefinitionTMS = "Content2";}

		else if (branchCI.contains("EDUI_CI_RC")) {
			String temp = branchCI;
			buildDefinitionTMS = temp.replace("EDUI_CI_","");}
		
		else if (branchCI.equalsIgnoreCase("EDUI_CI_Main") || branchCI.equalsIgnoreCase("EDUI_CI_dev")) {		
			buildDefinitionTMS = "dev";}
		
		String linkTMS = getCILatestLinkFromDb(buildDefinitionTMS,sourceBranch + buildDefinitionTMS);
		*/

		webDriver.init();
		Thread.sleep(2000);
		webDriver.maximize();
		webDriver.openUrl(CILinkTMS);
		webDriver.switchToMainWindow();
		
		return new TmsHomePage(webDriver, testResultService);

	}
	
	public void setBuildUsedByQA(String branch,String sourceBranch) throws Exception {
		dbService.setUsingTfsDB(true);
		String filterSQL = "(select top 1 url FROM [ApplicationEnvironments].[dbo].[Environments]  where BuildNumber like '%"+branch+"%' AND SourceBranch = '"+sourceBranch+"' order by CreationDate desc)";
		String updateSQL = "update [ApplicationEnvironments].[dbo].[Environments] set UsedBy = 'QA' where url = ";
		try {
			
			dbService.runDeleteUpdateSql(updateSQL+filterSQL);
			
		}

		finally {
			dbService.setUsingTfsDB(false);
		}
		
	}
	
	public String getBuildId(String branch,String sourceBranch) throws Exception {
		
		
		String Ci_buildId="";
		
		String sql = "Exec GetOrUpdateLastBuildIdAsQA " + "'"+branch+"'" + "," + "'"+sourceBranch+"'"
				//+ " '+sourceBranch+'"
				; 
		//+ "'"+branch+'" "; 
				//+ "+ " '"+sourceBranch+"'";
/*
		String a = "declare @lastBuild varchar(50)"
		+ " SET @lastBuild = (SELECT TOP 1 BuildID FROM [ApplicationEnvironments].[dbo].[Environments]"
		+ " where Buildnumber like '%"+branch+"%' AND SourceBranch = '"+sourceBranch+"' order by CreationDate desc)"

		+ " IF NOT EXISTS("
				+ " SELECT TOP 1 BuildID FROM [ApplicationEnvironments].[dbo].[Environments]"
				+ " where BuildID = @lastBuild AND UsedBy = 'QA')"
				+ " Begin"
					+ " UPDATE [ApplicationEnvironments].[dbo].[Environments] SET UsedBy = 'QA'"
					+ " where Buildid = @lastBuild"
				+ " End"
					+ " SELECT TOP 1 BuildID FROM [ApplicationEnvironments].[dbo].[Environments]"
					+ " where BuildID = @lastBuild"
					;
*/	
		try {
			//List<List>  Ci_buildId1 = null;
			
			dbService.setUsingTfsDB(true);
			//Ci_buildId = dbService.getStringFromQuery(sql);
			Ci_buildId = dbService.getStringFromQuery(sql,1,true);
		}

		finally {
			dbService.setUsingTfsDB(false);
		}
		
		return Ci_buildId;
		
	}
	
	public void setBuildPathCI (String CIlink) throws Exception {
		buildId = CIlink.split(".develop")[0].split("//")[1].replace("-", "_");
		int last_ = buildId.lastIndexOf("_");
		String buildNum = buildId.substring(last_).replace("_", ".");
		buildId = buildId.substring(0, last_) + buildNum;
		buildPath = CI_Folder + ed_BuildNumber +"\\";
	}
	
	public String setAndReturnBuildPathCI (String CIlink) throws Exception {
		buildId = CIlink.split(".develop")[0].replace("-", "_");
		int last_ = buildId.lastIndexOf("_");
		String buildNum = buildId.substring(last_).replace("_", ".");
		buildId = buildId.substring(0, last_) + buildNum;
		buildPath = CI_Folder + buildId +"//";
		
		return buildPath; 
	}
	
	public void setBuildPathCI_TMS (String CIlink)  {
		try {
			
			buildIdTMS = CIlink.split(".develop")[0].split("//")[1].replace("-", "_");
			int last_ = buildIdTMS.lastIndexOf("_");
			String buildNum = buildIdTMS.substring(last_).replace("_", ".");
			buildIdTMS = buildIdTMS.substring(0, last_) + buildNum;
		
			buildIdTMS = tms_buildNumber; //all 3 lines above not relevant and can be removed
			buildPathTMS = CI_Folder + buildIdTMS +"//";
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	
	public String getTMSLinkfromEdBuid (String CIlink) throws Exception {
		
		
		String tmsLink=null;
		setBuildPathCI_TMS(tmsLink);
		
		buildIdTMS = CIlink.split(".develop")[0].split("//")[1].replace("-", "_");
		int last_ = buildIdTMS.lastIndexOf("_");
		String buildNum = buildIdTMS.substring(last_).replace("_", ".");
		buildIdTMS = buildIdTMS.substring(0, last_) + buildNum;
		
		buildPathTMS = CI_Folder + buildIdTMS +"//";
		
		return buildPath; 
	}
	
	public List<String[]> getLatestMagazineArticlesFromStaticDB() throws Exception {
		checkAndSetCurrentstaticContentConnectionString(true);
		List<String[]> articlesSet = null;
		try {
			
			articlesSet = dbService.getLatestMagazineArticlesSet();
		
		} catch (Exception e) {
		// TODO Auto-generated catch block
			
		}
		finally {
			checkAndSetCurrentstaticContentConnectionString(false);
		}
		return articlesSet;

	}
	
	public List<String[]> getLatestMagazineArticlesFromStaticDB(String Promotion) throws Exception {
		checkAndSetCurrentstaticContentConnectionString(true);
		List<String[]> articlesSet = null;
		try {
			
			articlesSet = dbService.getLatestMagazinePromotion();
		
		} catch (Exception e) {
		// TODO Auto-generated catch block
		}
		finally {
			checkAndSetCurrentstaticContentConnectionString(false);
		}
		return articlesSet;

	}
	
	public List<String[]> getMagazineArticlesByMonthAndYearFromStaticDB(String month, String year) throws Exception {
		checkAndSetCurrentstaticContentConnectionString(true);
		List<String[]> articlesSet = null;
		try {
			
			articlesSet = dbService.getMagazineArticlesSetByMonthYear(month, year);
		
		} catch (Exception e) {
		// TODO Auto-generated catch block
		}
		finally {
			checkAndSetCurrentstaticContentConnectionString(false);
		}
		return articlesSet;

	}

	public String getLatestCILinkUX() throws Exception {
		String link = headlessBrowser
				.waitForElement(
						"//div[@class='container']//table//tbody//tr[1]//td//div[1]//div//a",
						ByTypes.xpath, "CI latest build link").getAttribute(
						"href");
		String instId = System.getProperty("instId");
		System.clearProperty("instId");
		if (instId == null) {
			instId = configuration.getInstitutionId();
		}
		String instName = dbService.getInstituteNameById(instId);
		buildId = link.replace("http://ci-srv:9010/", "");
		buildId = buildId.replace("/qa", "");

		link = link.replace("qa", instName);

		// System.out.println(buildId);

		return link;
	}

	public NewUxHomePage getCILatestUXLink(String version) throws Exception {

		webDriver.openUrl("http://ci-srv:9010/WebUX_CI_" + version + "/#/home");
		return new NewUxHomePage(webDriver, testResultService);

	}

	public String getDateString() {
		java.text.SimpleDateFormat df1 = new java.text.SimpleDateFormat("MM");

		String str = "";
		str += Calendar.getInstance().get(Calendar.YEAR);
		str += df1.format(Calendar.getInstance().getTime());
		str += Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		return str;
	}

	public void setUserLangSupport(String userId, int supportLevel, int langId)
			throws SQLException {
		String sp = "exec SetUserLanguageSettings " + userId + ","+ supportLevel + "," + langId;
		dbService.runStorePrecedure(sp);
	}
	
	public void setUserLangSupportLevel(String userId, int supportLevel)
			throws SQLException {
		String sp = "Update Users SET LanguageSupportLevelId =" +  supportLevel + " Where UserId = " + userId;
		dbService.runStorePrecedure(sp);
	}
	
	
	public void updateCommunityVersion(String institutionId)
			throws Exception {
		
		String sql = "select CommunityVersion+1 from institutions where institutionid = "+institutionId;
		String nextCommunityVersion = dbService
				.getStringFromQuery(sql);
		
 
		String sp = "Update institutions set  CommunityVersion = "+ nextCommunityVersion + " where institutionid = "+institutionId;
		dbService.runStorePrecedure(sp);
	}
	
	public void SetInstLangFullSupport(String InstId, String Language)
			throws Exception {

		/*String InstID = 
				dbService
				.getStringFromQuery("select InstitutionId from Institutions where Name like '"
						+ InstId + "' and Visible = 1");
		*/
		String LangID = dbService
				.getStringFromQuery("select LanguageId from Language where Description = '"
						+ Language + "'");
		
		String sp = "exec SetInstitutionLanguageSettings "+ InstId + ", " + 3 + ", " + LangID + "";
		dbService.runStorePrecedure(sp);
		// ("update Institutions set LanguageId = (select LanguageId from Language where Description = '"+Language+"'), LanguageSupportLevelId = 3 where Name = '"+InstName+"' and Visible = 1");
	}

	public void SetInstLangOnlyEnglish(String InstName) throws Exception {

		String InstID = dbService
				.getStringFromQuery("select InstitutionId from Institutions where Name like '"
						+ InstName + "' and Visible = 1");
		dbService.runStorePrecedure("exec SetInstitutionLanguageSettings "
				+ InstID + ", " + 0 + ", 3");
		// ("update Institutions set LanguageSupportLevelId = 0 where Name = '"+InstName+"' and Visible = 1");

	}
	
	public void SetInstLangLowSupport(String InstName) throws Exception {

		String InstID = dbService
				.getStringFromQuery("select InstitutionId from Institutions where Name like '"
						+ InstName + "' and Visible = 1");
		dbService.runStorePrecedure("exec SetInstitutionLanguageSettings "
				+ InstID + ", " + 1 + ", 3");
		// ("update Institutions set LanguageSupportLevelId = 0 where Name = '"+InstName+"' and Visible = 1");

	}

	public int[] getRGBColorFromHexString(String hex) throws Exception {
		int[] rgb = new int[3];
		rgb[0] = Color.decode(hex).getRed();
		rgb[1] = Color.decode(hex).getGreen();
		rgb[2] = Color.decode(hex).getBlue();

		return rgb;

	}
	
	public void LockCourseToClass(String className, String courseID)

			throws Exception {

		LockCourseToClass(configuration.getInstitutionId(), className, courseID);

	}
	
	public void LockCourseToClass(String institutionId, String className, String courseID)

			throws Exception {
		
		String ClassID = dbService
				.getStringFromQuery("select ClassId from Class where Name = '"
						+ className + "' and InstitutionId = " + institutionId + "");
		dbService.runStorePrecedure("exec UnauthorizeCoursesToClass " + ClassID
				+ ",'" + courseID + ";',0");

	}
	
	public void LockMultipleCoursesToClass(String institutionId, String className, String [] coursesID)

			throws Exception {
		
		String coursesParam = coursesID[0]+";";
		
		for (int i = 1; i < coursesID.length; i++) {
			coursesParam += coursesID[i] + ";";
		}
		
		String ClassID = dbService
				.getStringFromQuery("select ClassId from Class where Name = '"
						+ className + "' and InstitutionId = " + institutionId + "");
		dbService.runStorePrecedure("exec UnauthorizeCoursesToClass " + ClassID
				+ ",'" + coursesParam + "',0");

	}
	
	public void UnlockCourseToClass(String institutionId, String className, String [] coursesID, String [] coursesToUnlockID)

			throws Exception {
		
		String[] coursesToRemainUnassigned = coursesID;
		
		for (int i = 0; i < coursesToUnlockID.length; i++) {
			coursesToRemainUnassigned = ArrayUtils.removeElement(coursesToRemainUnassigned, coursesToUnlockID[i]);
		}
		
	
		String coursesParam = "";
		if (coursesToRemainUnassigned.length != 0) {
			coursesParam = coursesToRemainUnassigned[0]+";";

		}
			
		for (int i = 1; i < coursesToRemainUnassigned.length; i++) {
			coursesParam += coursesToRemainUnassigned[i] + ";";
		}
			
		String ClassID = dbService
				.getStringFromQuery("select ClassId from Class where Name = '"
						+ className + "' and InstitutionId = " + institutionId + "");
		dbService.runStorePrecedure("exec UnauthorizeCoursesToClass " + ClassID
				+ ",'" + coursesParam + "',0");
		

	}

	public void LockUnitToClass(String ClassName, String CourseID, int UnitNum)
			throws Exception {

		String InstID = configuration.getInstitutionId();
		String ClassID = dbService
				.getStringFromQuery("select ClassId from Class where Name = '"
						+ ClassName + "' and InstitutionId = " + InstID + "");
		List<String[]> units = dbService.getCourseUnitDetils(CourseID);
		UnitNum = UnitNum - 1;
		String UnitID = units.get(UnitNum)[0];
		dbService.runStorePrecedure("exec UnauthorizeUnitsToClass " + ClassID
				+ ",'" + CourseID + ":1;','" + UnitID + ";',0");

	}

	public void LockLessonToClass(String className, String courseID,
			int UnitNum, int LessonNum) throws Exception {

		String InstID = configuration.getInstitutionId();
		String ClassID = dbService
				.getStringFromQuery("select ClassId from Class where Name = '"
						+ className + "' and InstitutionId = " + InstID + "");

		List<String[]> units = dbService.getCourseUnitDetils(courseID);
		UnitNum = UnitNum - 1;
		String UnitID = units.get(UnitNum)[0];

		List<String[]> components = dbService
				.getComponentDetailsByUnitId(UnitID);
		LessonNum = LessonNum - 1;
		String LessonID = components.get(LessonNum)[1];

		dbService.runStorePrecedure("exec UnauthorizeComponentsToClass "
				+ ClassID + ",'" + UnitID + ";','" + UnitID + ":" + LessonID
				+ ":C;" + UnitID + ":" + LessonID + ":T;',0");

	}

	public void UnlockCourseUnitLessonsToClass(String className,
			String courseID, int unitNum) throws Exception {

		String InstID = configuration.getInstitutionId();
		String ClassID = dbService
				.getStringFromQuery("select ClassId from Class where Name = '"
						+ className + "' and InstitutionId = " + InstID + "");
		List<String[]> units = dbService.getCourseUnitDetils(courseID);
		unitNum = unitNum - 1;
		String UnitID = units.get(unitNum)[0];
		dbService.runStorePrecedure("exec UnauthorizeCoursesToClass " + ClassID
				+ ",'',0");
		dbService.runStorePrecedure("exec UnauthorizeUnitsToClass " + ClassID
				+ ",'" + courseID + ":0;','',0");
		dbService.runStorePrecedure("exec UnauthorizeComponentsToClass "
				+ ClassID + ",'" + UnitID + ";','',0");

	}
	
	public void assignAutomatedTestToStudent (String studentId, String courseId, int testTypeId, int startDateOffset, int startMinuteOffset, int endDateOffset)
			throws Exception {
		
		Date date = new Date();
		Calendar startTest = Calendar.getInstance();
		Calendar endTest = Calendar.getInstance();
		DateFormat dateFormatBasic = new SimpleDateFormat("yyyyMMdd");
		DateFormat dateFormatLocal = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
		String clientLocalHour = dateFormatLocal.format(date);
				
		startTest.setTime(date);
		startTest.add(Calendar.DATE, startDateOffset);
		String startDate = dateFormatBasic.format(startTest.getTime()).toString();
		
		int startMinutes = 0;
		
			if (startDateOffset == 0 && startMinuteOffset == 0) startMinutes = 1;
				
			else {
					int currentHours = startTest.get(Calendar.HOUR_OF_DAY);
					int currentMinutes = startTest.get(Calendar.MINUTE);
					startMinutes = currentHours * 60 + currentMinutes + startMinuteOffset;
				}
			int currentHours = startTest.get(Calendar.HOUR_OF_DAY);
			int currentMinutes = startTest.get(Calendar.MINUTE);
			int endtimeutc= currentHours *60 + currentMinutes +1+ startMinuteOffset;
		endTest.setTime(startTest.getTime());
		endTest.add(Calendar.DATE, endDateOffset);
		String endDate = dateFormatBasic.format(endTest.getTime()).toString();
				
	
		dbService.runStorePrecedure("exec SetUserExitTestSettings " + studentId	+ ","+courseId+",'"+ startDate + "',"+startMinutes+",'" + endDate + "','"+endtimeutc+"','"+clientLocalHour+"',120,"+testTypeId);
		
	}
	
	
	public void submitCourseTestSection (String studentId, String unitId, String componentId,String lastItemId )
			throws Exception {
		
		String sql = "exec SubmitCourseTest"
				+ " @UserId=" + studentId + ",@UnitId=" + unitId + ",@ComponentId=" + componentId + ","
				+ " @Grade=20,@Marks='100|0|0|0|0|',@SetId='61186|61187|61188|61189|61190|',@VisitedItems='[1][2][3][4][5]',@TimeOver=0,"
				+ " @UserState=0x7B2261223A5B7B22694964223A36313138362C2269436F6465223A22623372656163743031222C226954797065223A32352C227561223A5B7B22714964223A312C22614964223A5B5B312C315D5D2C22624964223A5B312C332C342C325D2C2277223A6E756C6C7D5D2C226D223A3130307D2C7B22694964223A36313138372C2269436F6465223A22623372656163743032222C226954797065223A32352C227561223A5B7B22714964223A312C22614964223A5B5B312C335D5D2C22624964223A5B312C332C342C325D2C2277223A6E756C6C7D5D2C226D223A307D2C7B22694964223A36313138382C2269436F6465223A22623372656163743033222C226954797065223A32352C227561223A5B7B22714964223A312C22614964223A5B5B312C335D5D2C22624964223A5B312C332C325D2C2277223A6E756C6C7D5D2C226D223A307D2C7B22694964223A36313138392C2269436F6465223A22623372656163743034222C226954797065223A32352C227561223A5B7B22714964223A312C22614964223A5B5B312C335D5D2C22624964223A5B322C332C315D2C2277223A6E756C6C7D5D2C226D223A307D2C7B22694964223A36313139302C2269436F6465223A22623372656163743035222C226954797065223A32352C227561223A5B7B22714964223A312C22614964223A5B5B312C345D5D2C22624964223A5B322C332C312C345D2C2277223A6E756C6C7D5D2C226D223A307D5D2C2274223A3631332C226D223A307D,"
				+ " @TestTime=613,@LastItemId="+lastItemId;
		
		dbService.runStorePrecedure(sql);	  
		
	}
	
	public String [] getTestRemainingTimeByStudent (String studentId, int testTypeId)
			throws Exception {
		
		Date date = new Date();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						
		String endDateFromDB = dbService.getStringFromQuery("select EndDate from UserExitTestSettings where UserId = "+studentId+" and ImpTypeFeatureId = "+testTypeId);
		endDateFromDB = endDateFromDB.substring(0, endDateFromDB.indexOf("."));
		Date endDate = df.parse(endDateFromDB);
		
		long duration = endDate.getTime() - date.getTime();
	/*			
		long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(duration);
		long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
		long diffInHours = TimeUnit.MILLISECONDS.toHours(duration);
		long diffInDays = TimeUnit.MILLISECONDS.toDays(duration);
	*/		
		//duration -= 10 * 1000; // reduce 10 second from duration
		//String days = String.format("%1d", (duration / (1000 * 60 * 60 * 24)-1)); //actually the timer and days in ED starts to count down.
		String days = String.format("%1d", (duration / (1000 * 60 * 60 * 24))); //actually the timer and days in ED starts to count down.
		
		if (days.equals("-1") || days.equals("0")) {
			days = "";
		}
		
		duration -= 10*1000;
		String hh = String.format("%02d", (duration / (1000 * 60 * 60)) % 24);
		String mm = String.format("%02d", (duration / (1000 * 60)) % 60);
		String ss = String.format("%02d", (duration / 1000) % 60);
		
		String[] counterValues = new String[] { days, hh, mm, ss };
		
		return counterValues;
						
	}
	
	public String getAssignedTestIdForStudent (String studentId, String courseId, int testTypeId)
	{
		String testid = null;
		String sql = "SELECT TestId From UserExitTestSettings Where Userid= " + studentId	+" AND CourseId= "+ courseId +" AND ImpTypeFeatureId= "+testTypeId;
		try {
			testid = dbService.getStringFromQuery(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		return testid;
	}
	
	public String[] getAssignedCourseTestIdForStudent(String studentId, String courseId, int testTypeId) throws Exception {
		List<String[]> testAdministration = null;
		
		String sql = "SELECT UserExitTestSettingsId,CourseId,TestId,Minutes"
				+ " From UserExitTestSettings Where Userid= " + studentId	+" AND CourseId= "+ courseId +" AND ImpTypeFeatureId= "+testTypeId;
		
		testAdministration = dbService.getStringListFromQuery(sql, 1, 4);
		
		String[] record = new String[4];
		
		for (int i=0 ; i < record.length;i++){
			 record[i] =  testAdministration.get(0)[i];
		}
				
		return record;
	}
	
	
	
	public String getDidTestIdForStudent (String studentId, String courseId, int testTypeId)
			throws Exception {
		String testid = null;
		String sql = "SELECT TestId From UserExitTestSettingslog Where Userid= " + studentId +" AND CourseId= "+ courseId +" AND ImpTypeFeatureId= "+testTypeId;
		testid = dbService.getStringFromQuery(sql);
				
		return testid;
	}
	
	public String getStartTestDateByStudent (String studentId, int testTypeId)
			throws Exception {
		
		
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DateFormat dfSpecial = new SimpleDateFormat("dd/MM/yyyy");
						
		String startDateFromDB = dbService.getStringFromQuery("select StartDate from UserExitTestSettings where UserId = "+studentId+" and ImpTypeFeatureId = "+testTypeId);
		startDateFromDB = startDateFromDB.substring(0, startDateFromDB.indexOf("."));
		Date startDate = df.parse(startDateFromDB);
		String startDateFinal = dfSpecial.format(startDate);
		
		return startDateFinal;
						
	}
	
	public String [] getSantillanaUserDetailsByUserName (String santillanaUserName)
			throws Exception {
				
		String userNameId = dbService.getStringFromQuery("select su.userId from SantillanaUsers as su inner join users  as u on u.userid = su.userid where u.visible = 1 and su.UserName = '"+santillanaUserName+"'");
		
		String userFirstName = dbService.getUserFirstNameByUserId(userNameId);
		String userLastName = dbService.getUserLastNameByUserId(userNameId);
				
		String[] userDetails = new String[] { userNameId, userFirstName, userLastName };
		
		return userDetails;
						
	}
	
	public void deleteSantillanaUserByUserId(String userid)
			throws Exception {
	
		dbService.runStorePrecedure("exec DeleteUser '"+userid+";'");
		dbService.runDeleteUpdateSql("delete from SantillanaUsers where UserId = "+ userid);
	
	}
	
	public void closeLastSessionImproperLogoutAlert() throws Exception {
		
		WebElement getLastSessionAlert = webDriver.waitForElement("//div[@id='alertModal']", ByTypes.xpath, false, 3);
		if (getLastSessionAlert != null)
			//getLastSessionAlert.click();
			webDriver.waitForElementAndClick("btnOk", ByTypes.id);
		
	}
	
	public List<String[]> getInstitutionAllCoursesNames (String institutionId)
			throws Exception {
				
		List<String[]> rsList = dbService.getListFromStoreRrecedure("EXEC GetInstitutionCourseList " + institutionId);
		//List<String[]> list = rsList.get(0);
				
	/*	String[] courses = new String[list.size()];
		for (int count = 0; count < courses.length; count++) {
			courses[count] = list.get(count)[1];
		}
	*/					
		return rsList;
						
	}
		
	public String [] getUserAssignedCourses (String userId)
			throws Exception {
				
		List<String[]> rsList = dbService.getListFromStoreRrecedure("EXEC GetAssignedCourses2 " + userId);
		//List<String[]> list = rsList.get(0);
		
				
		String[] courses = new String[rsList.size()];
		for (int count = 0; count < courses.length; count++) {
			courses[count] = rsList.get(count)[0];
		}
						
		return courses;
						
	}

	public String [] getUserAssignedCourses (String userId, boolean useSecondaryDB)
			throws Exception {

		List<String[]> rsList = null;
		try {
			if (dbService.currentGeneralDB.equalsIgnoreCase("edProduction") && useSecondaryDB == true) {
				dbService.useEDMerge2 = true;
				rsList = dbService.getListFromStoreRrecedure("EXEC GetAssignedCourses2 " + userId);
			} else {
				rsList = dbService.getListFromStoreRrecedure("EXEC GetAssignedCourses2 " + userId);
			}
			//List<String[]> list = rsList.get(0);
			String[] courses = new String[rsList.size()];
			for (int count = 0; count < courses.length; count++) {
				courses[count] = rsList.get(count)[0];
			}
			return courses;

		}catch (Exception e){
			dbService.useEDMerge2 = false;
			e.printStackTrace();
		}
		return null;

	}
	
	public String sendRecordingToTeacherSR(String userId, String unitId, String CompId, String subCompId, String segmentId,String autoScore, String resName)
			throws Exception {
		
		//RegistUserSRRec @UserId=5232282000034,@UnitId=43457,@ComponentId=43460,@SubComponentId=1,@SegmentId=3,@Score=91,@ReNew=1,@NewSR=0,@IsMp3	
		String sqlParameters = "EXEC RegistUserSRRec " + userId + "," + unitId + "," + CompId + "," + subCompId + "," + segmentId + "," + autoScore + ",";
		sqlParameters = sqlParameters + "0,0,0,";
		String sqlFinal = sqlParameters + resName;
		
		List<String[]> rsList = dbService.getListFromStoreRrecedure(sqlFinal);
		//List<String[]> list = rsList.get(0);
						
		String recId = rsList.get(0)[0];
								
		return recId;
	}
	
	public Boolean updateSaveRecordingAndCheckConvert(String recId, String userId) throws Exception {
		
		Boolean converted = false;
				
		dbService.runStorePrecedure("EXEC UpdSRRecSaveStat " + recId);
		
     // webDriver.sleep(120);
	 //--- igb 2018.08.08-----------------------
		if(!isUserMp3RecExist(userId, recId, 240))
			return false;
	 //--- igb 2018.08.08----
		
		String sql = "select Converted from UserSRRecords where Converted = 1 and ID = " + recId + " and StudentId = " + userId;
		String sqlCheck = dbService.getStringFromQuery(sql, 20, false); 
		
		if (sqlCheck!=null) converted = true;
						
		return converted;
	}
	
	public void deleteRecordingsRegisterInDB(String userId) throws Exception {
				
		dbService.runDeleteUpdateSql("delete from UserSRRecords where StudentId = " + userId);
	}
	
	public void deleteCourseByCourseOwnerId(String courseId, String ownerId) throws Exception {
		
		dbService.runStorePrecedure("exec DeleteCourse " + courseId + "," + ownerId, true);
	}
	
	public void deleteStudentRecordingsFolder(String recFolder, String studentId) throws Exception {
		
		textService.getDeleteFolderInPath(recFolder, studentId +"//");
	}
	
//--igb 2018.08.08------------------------------
	private boolean isUserMp3RecExist(String studentId, String recId, int maxWaitSec) throws Exception {
		
		String RECORDINGS_FOLDER = "smb://frontqa2016//SharedUpload//Attachments//"+PageHelperService.recFolder+"//Recordings//";
		
	    int waitTime = 0;
	    
	    
	    while(waitTime < maxWaitSec + 1)
	    {
	       if(textService.checkIfFileExist(RECORDINGS_FOLDER.replaceAll("smb:", "") + "/" + studentId + "/" + recId + ".mp3"))
	    	   return true;
	       
	       Thread.sleep(10 * 1000);
	       
	       waitTime += 10;
	    }
	   report.report("Timeout of " + waitTime + " sec reached"); 
	   
 	   return false;
	}
//--igb 2018.08.08-------
	
	
	public String getItemIdByCourseUnitLessonStepAndTask(String courseId, int unitIndex, int lessonIndex,
			int StepIndex, int taskIndex) throws Exception {
		// TODO Auto-generated method stub
		String unitId = dbService.getCourseUnits(courseId).get(unitIndex - 1);

		// lesson id
		String compId = dbService.getComponentDetailsByUnitId(unitId).get(lessonIndex - 1)[1];
		// step id
		String SubComp = dbService.getSubComponentsDetailsByComponentId(compId).get(StepIndex - 1)[1];

		String itemId = dbService.getSubComponentItems(SubComp).get(taskIndex - 1)[0];

		// System.out.println(itemId);
		return itemId;
	}
	
	
				
	public void checkColor(String actualColor, String expectedHexColor)
			throws Exception {

		if (actualColor.startsWith("rgb")) {

			int[] rgbFromHex = getRGBColorFromHexString(expectedHexColor);

			int[] rgbFromCss = new int[3];
			actualColor = actualColor.replace("a", "");
			String[] numbers = actualColor.replace("rgb(", "").replace(")", "")
					.split(",");
			rgbFromCss[0] = Integer.parseInt(numbers[0].trim());
			rgbFromCss[1] = Integer.parseInt(numbers[1].trim());
			rgbFromCss[2] = Integer.parseInt(numbers[2].trim());

			if (testResultService.assertEquals(true,
					Arrays.equals(rgbFromHex, rgbFromCss)) == false) {
				report.report("Color is not as expected: .expected RGB color was: "
						+ rgbFromHex[0]
						+ rgbFromHex[1]
						+ rgbFromHex[2]
						+ " and actual RGB color was: "
						+ rgbFromCss[0]
						+ rgbFromCss[1] + rgbFromCss[2]);

				webDriver.printScreen();
			}

		}

		else {
			testResultService.assertEquals(expectedHexColor, actualColor,
					"Color do not match");
		}

	}

	public String getCILink() {
		return CILink;
	}

	
	public String[] getUserFromDbByInstitutionIdAndClassName(String institutionId,String className) throws Exception {
	
		List<String[]> result = dbService.getUserByInstitutionIdAndClassName(institutionId,className);
		
		int listCount = result.size();
		if (listCount == 0) {
			report.report("No user returned");
		}
		Random rand = new Random(); 
		int value = rand.nextInt(listCount); 

		return result.get(value);
	}
	
	public String[] getUserNamePassworIddByInstitutionIdAndClassName(String institutionId,String className) throws Exception {
		
		List<String[]> result = dbService.getUserNamePassworIddByInstitutionIdAndClassName(institutionId,className);
		
		int listCount = result.size();
		if (listCount == 0) {
			report.report("No user returned");
		}
		Random rand = new Random(); 
		int value = rand.nextInt(listCount); 

		return result.get(value);
	}
	
public String[] getUsersDidPlt(String institutionId) throws Exception {
		
		List<String[]> result = dbService.getUsersDidPLT(institutionId);
		
		String[] oneRowResult = getOneDbRow(result);
		
		/*
		int listCount = result.size();
		if (listCount == 0) {
			report.report("No user returned");
		}
		Random rand = new Random(); 
		int value = rand.nextInt(listCount); 
*/
		return oneRowResult;
	}
	
	
	private String[] getOneDbRow(List<String[]> result) {
		int listCount = result.size();
		if (listCount == 0) {
			report.report("No user returned");
		}
		Random rand = new Random(); 
		int value = rand.nextInt(listCount);
		
		String[] oneRow = result.get(value);
		
	return oneRow;
}

	public void changeUserCommunityLevel(String level,String studentId) throws Exception {
		dbService.changeUserCommunityLevel(level,studentId);
	}

	public void checkConsoleLogsForErrors(List<String> logList) {
		for (int i = 0; i < logList.size(); i++) {
			if (logList.get(i).contains("404")) {
				testResultService.addFailTest("Browser console error: "
						+ logList.get(i));
			}
		} 

	}
	
	public List<String[]> checkTaskForNetErrorsAddToList(List<String[]> updatedlist, int timeOutInSec, String courseName, int unitIndex,int lessonIndex,int stepIndex,int taskIndex) throws Exception {
		
		
		try {
			//webDriver.startProxyLister("https://edux.qa.com/");
			webDriver.startProxyLister("https://WebUX-NovRel-20161103-1.develop.com/");
			
			Thread.sleep(timeOutInSec * 1000);
			updatedlist = netService.addNetworkErrorsToList(webDriver.getHar(), updatedlist, courseName, unitIndex, lessonIndex, stepIndex, taskIndex);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return updatedlist;

	}
	
	public List<String[]> checkTaskForConsoleErrorsAddToList(List<String[]> updatedlist, String courseName, int unitIndex,int lessonIndex,int stepIndex,int taskIndex) throws Exception {
			
		
		Thread.sleep(1000);
		String consoleLog = null;
				
		List <String> consoleErrors = webDriver.getConsoleLogs("404", true);
		if (!consoleErrors.isEmpty()) consoleLog = consoleErrors.toString();
		else consoleLog = null;
		
		if (consoleLog != null && !consoleLog.contains("_spa.js") && !consoleLog.contains("GetPromotionSlides")) {
			
			String errorToReport = "NETWORK ERROR FOUND: ----- "+courseName+" - "+"Unit "+unitIndex+" - "+"Lesson "+ lessonIndex +" - "+"Step "+stepIndex+" - "+"Task "+ taskIndex;
			
			testResultService.addFailTest(errorToReport);
			System.out.println(errorToReport);
			System.out.println(consoleLog);
			updatedlist.add(new String[]{courseName, String.valueOf(unitIndex), String.valueOf(lessonIndex), String.valueOf(stepIndex), String.valueOf(taskIndex), consoleLog});				
		}
				
		
		return updatedlist;

	}
	
	
	public void disableSeeTestAnswersToClass(String className, String institutionId) throws Exception {

		String classID = dbService.getClassIdByName(className, institutionId);
		dbService.runStorePrecedure("exec SetClassTestSettings " + classID + ",55,0,0");

	}
	
	public void enableSeeTestAnswersToClass(String className, String institutionId) throws Exception {

		String classID = dbService.getClassIdByName(className, institutionId);
		dbService.runStorePrecedure("exec SetClassTestSettings " + classID + ",55,1,0");

	}
	
	public void enableAllowTestOnlyOnceToClass(String className, String institutionId) throws Exception {

		String classID = dbService.getClassIdByName(className, institutionId);
		dbService.runStorePrecedure("exec SetClassTestSettings " + classID + ",55,1,1");

	}
	
	public void disableAllowTestOnlyOnceToClass(String className, String institutionId) throws Exception {

		String classID = dbService.getClassIdByName(className, institutionId);
		dbService.runStorePrecedure("exec SetClassTestSettings " + classID + ",55,1,0");

	}
	
	public void sendMessageByDb(String teacherId, String studentId,String subject,String bodyText) throws Exception{
		String sql = "exec SendWebPalMessage '" + teacherId + "','" + studentId + ";',N'" + subject + "',N'<p><br>" + bodyText +"</p>',0,0";
		dbService.runStorePrecedure(sql);
	}
	
	public void assignPltToInstitution(String institutionId) throws Exception {
		
		dbService.runStorePrecedure("exec SetInstitutionPTVisibility " + institutionId + ",True,False,True");

	}
	
	public void unassignPltToInstitution(String institutionId) throws Exception {
		
		dbService.runStorePrecedure("exec SetInstitutionPTVisibility " + institutionId + ",True,False,False");

	}
	
	public void disableSelfRegistration(String institutionId) throws Exception {
		
		dbService.runStorePrecedure("exec SetSelfRegistrationSettings " + institutionId + ",null,0,0");

	}
	
	public void enableSelfRegistrationNoAddToClassNoPLT(String institutionId) throws Exception {
		
		dbService.runStorePrecedure("exec SetSelfRegistrationSettings " + institutionId + ",null,0,1");
	//	updateCommunityVersion(institutionId);

	}
	
	public void enableSelfRegistrationWithAddToClassNoPLT(String institutionId, String classId) throws Exception {
		
		dbService.runStorePrecedure("exec SetSelfRegistrationSettings " + institutionId + "," + classId + ",0,1");
		updateCommunityVersion(institutionId);

	}
	
	public void enableSelfRegistrationNoAddToClassWithPLT(String institutionId) throws Exception {
		
		dbService.runStorePrecedure("exec SetSelfRegistrationSettings " + institutionId + ",null,1,1");
		updateCommunityVersion(institutionId);

	}
	
	public void enableSelfRegistrationAddToClassWithPLT(String institutionId, String classId) throws Exception  {
		
		try {
			dbService.runStorePrecedure("exec SetSelfRegistrationSettings " 
					+ institutionId + "," + classId + ",1,1");
		//	updateCommunityVersion(institutionId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public boolean isUserAssignedToClass(String userId, String classId) throws Exception {
		String assignedClassId= "";
		assignedClassId = dbService.getUserClassId(userId); // student
		boolean isAssigned = assignedClassId.equals(classId);
		
		return isAssigned;
	}
	
	public boolean isTeacherAssignedToClass(String userId, String classId) throws Exception {
		
		List<String[]> assignedClassesId = dbService.getTeacherClassId(userId);
		
		boolean isAssigned = false;
		
		for (int i=0;i<assignedClassesId.size();i++){
		
			if (classId.equals(assignedClassesId.get(i)[0])){
				isAssigned=true;
				break;
			}
		}
		
		return isAssigned;
	}
	
	public void deleteStudentsAndClass(String instID, String className) throws Exception {
		
		studentService.clearStudents(instID, className, 0);
		dbService.deleteClassByName(instID, className);			
	}
		
	
	public void checkUserNotAssignedToClasses(String userId) throws Exception {
		
		String sql = "select count(ClassId) from ClassUsers where UserId = "+ userId;
		String number = dbService.getStringFromQuery(sql, 1, true);
		testResultService.assertEquals("0", number, "Student assigned to class");
	}
	
	public String getCurrentDateByFormat(String dateFormat) throws Exception {
	
		Date date = new Date();
		DateFormat df = new SimpleDateFormat(dateFormat);
		String expSubDate = df.format(date);
	
		return expSubDate;
		
	}
	
	public void setSessionTimeoutForED(int timeoutInMin) throws Exception {
				
		//channgeSet array format = {xPath, attributeName, new value}
		
		String path = buildPath + "web.config";
		String xPath = "//forms[@cookieless='UseCookies']";
		String attrName = "timeout";
		String newValue = String.valueOf(timeoutInMin);
		
		List <String[]> changeSet = new ArrayList<String[]>();
		String [] elementED1 = {xPath, attrName, newValue}; 
		changeSet.add(elementED1);
				
		textService.modifySmbXmlFile(path, changeSet);
		
	}
	
	public void linkEdTmsQaBuildsInCI() throws Exception {
				
		//smb path for ED & TMS web.config files}
		String pathED = buildPath + "web.config";
		String pathTMS = buildPathTMS + "web.config";

		//create scope for ED web.config changes
		//channgeSet array format = {xPath, attributeName, new value}
		List <String[]> changeSetED = new ArrayList<String[]>();
		String [] elementED1 = {"//add[@key='WebAPIAllowCorsPath']","value", CILinkTMS}; 
		String [] elementED2 = {"//add[@key='TmsHost']","value", CILinkTMS.replace("http://", "")+"/"}; 
		changeSetED.add(elementED1);
		changeSetED.add(elementED2);
		
		//create scope for TMS web.config changes
		//channgeSet array format = {xPath, attributeName, new value}
		List <String[]> changeSetTMS = new ArrayList<String[]>();
		
		String ciPath = "undefined";
		if(ciEnv)
			//ciPath = "\\"+"\\ci-srv\\ApplicationEnvironments\\";
			ciPath = "\\"+"\\ci\\BuildsArtifacts\\";
		else
			ciPath = Static_QA_Server;
				
		String baseUrl = CILink.split(".com")[0] + ".com";
		String [] elementTMS1 = {"//add[@key='ed_path']","value", baseUrl}; 
		String [] elementTMS2 = {"//add[@key='ed_DataJS']","value", buildId + "\\Runtime\\DataJS\\"}; 
		String [] elementTMS3 = {"//add[@key='ed_LookAndFeel']","value", ciPath + buildId + "\\"}; 
		//String [] elementTMS4 = {"//add[@key='ed_Institutions']","value", ciPath + buildId +"\\Institutions"};
		//String [] elementTMS5 = {"//add[@key='UpLoadDir_LF']","value", ciPath + buildId +"\\Institutions"};
		String [] elementTMS6 = {"//add[@key='UrlUserRecSrv']","value", CILinkTMS + "/UserRecordSrv/Srv01.svc/"};
		changeSetTMS.add(elementTMS1);
		changeSetTMS.add(elementTMS2);
		changeSetTMS.add(elementTMS3);
		//changeSetTMS.add(elementTMS4);
		//changeSetTMS.add(elementTMS5);
		changeSetTMS.add(elementTMS6);
				
		//modify ED and TMS web.config files to associate sites
		textService.modifySmbXmlFile(pathED, changeSetED);
		textService.modifySmbXmlFile(pathTMS, changeSetTMS);
		
	}
	
	public void linkEdTmsQaBuildsInCI_CMD() throws Exception {
						
		//String command = "\\\\vstf2013\\EdoTmsAssociator\\bin\\EDOTMSMatcher.Console.exe " + buildIdTMS + " " + buildId;
		//String command = "\\\\vstf2013\\EdoTmsAssociator\\bin\\EDOTMSMatcher.Console.exe " + buildIdTMS + " " + ed_BuildNumber;
		String command = "\\\\azure2020\\EDOTMSMatcher\\bin\\EDOTMSMatcher.Console.exe " + buildIdTMS + " " + ed_BuildNumber;


		Process process = null;
		
		try
		{
		    process = Runtime.getRuntime().exec(command);
		} catch (IOException e)
		{
		    e.printStackTrace();
		    
		}
		
		BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
		// read the output from the command
		//System.out.println("Here is the standard output of the command:\n");
		String s = null;
		while ((s = stdInput.readLine()) != null) {
		    System.out.println(s);
		}
	}

//--igb 2018.03.04 ---------------	
	public boolean getFeatureStatePerInstallation(FeaturesList fl) throws Exception {
		String smbPath = buildPath + "Config//featuresList.json"; 		
		String source = textService.getSmbFileContent(smbPath);
		
		JSONObject js = new JSONObject(source);
		
		return (js.get(fl.toString()).toString().equalsIgnoreCase("true"));
	}
//--igb 2018.03.04 ---------------	
		
	public void setFeaturesListPerInstallation(FeaturesList fl, String newValue) throws Exception {
		
		String smbPath = buildPath + "Config//featuresList.json";	
		report.addTitle("The build path is: " + buildPath);
		//String smbPath = "smb://frontqa3//WebUx//Config//featuresList.json"; 		
		String source = textService.getSmbFileContent(smbPath);
		JSONObject js = new JSONObject(source);
		
		testDataValidation data = new testDataValidation();
		
		if (!js.get(fl.toString()).toString().equalsIgnoreCase(newValue)) {
			
			js = data.setJsonValueByKeyInFeaturesList(js, fl.toString(), newValue);
			textService.writeSmbFileWithText(smbPath, js.toString());
		}
		
	}
	
	public void setFeaturesListInAppSetByInstitution(String instID, FeaturesList fl, String newValue, boolean removeKey) throws Exception {
		
		String instDataPath = textService.getWebConfigAppSettingsValuByKey(buildPath, "institutionsDataPath");
		// added to support external app set js in dev2008
		String rootPath = textService.getWebConfigAppSettingsValuByKey(buildPath, "resPhysicalPath");
				
		//This line support all settings of institution data path
		//String smbPath = "smb:" + instDataPath.replace("@institutionId", instID).replace("\\","/"); 		
		 
		//added to support external app set js in dev2008
		String smbPath = "smb:" + (rootPath + instDataPath.replace("@institutionId", instID)).replace("\\","/");
		
		String source = textService.getSmbFileContent(smbPath);
		JSONObject js = new JSONObject(source);
		JSONObject instObj = js.getJSONObject("institutions");
		String key = fl.toString();
		
		testDataValidation data = new testDataValidation();
		
		if (removeKey) {
			
			js = data.removeJsonKeyInAppSet(js, key);
			textService.writeSmbFileWithText(smbPath, js.toString());
					
		} else if (instObj.has(key))  {
							
			if (!instObj.get(key).toString().equalsIgnoreCase(newValue)) {
			
				js = data.setJsonValueByKeyInAppSet(js, key, newValue);
				textService.writeSmbFileWithText(smbPath, js.toString());
			}
				
		} else {
			
				js = data.setJsonValueByKeyInAppSet(js, key, newValue);
				textService.writeSmbFileWithText(smbPath, js.toString());
						
		}
		
	}
	
	public void createProgressForClassStudents(String instId, String className, String courseId, boolean submitTests) throws Exception {
		
		String classId = dbService.getClassIdByName(className, instId);
		List<String> classStudents = dbService.getClassStudentsID(classId);
		
		for (int i = 0; i < classStudents.size(); i++) {
			studentService.setProgressForCourse(courseId, classStudents.get(i), null, 60);
			
			if (submitTests) {
				studentService.submitTestsForCourse(classStudents.get(i), courseId, false);
				}
		}
	}
	public void skipOnBoardingTour() throws Exception {

		//WebElement getOnBoarding = webDriver.waitUntilElementAppearsAndReturnElement("//div[contains(@class,'onBoarding__main')]",5);
		WebElement getOnBoarding = webDriver.waitUntilElementAppears("onBoarding__navigation", ByTypes.className, 3);

		if (getOnBoarding != null) {
			for (int n = 0; n < 12; n++) {
				Thread.sleep(500);
				webDriver.sendKey(Keys.ARROW_RIGHT);

			}
		}

	}

	public void checkAudioFileLoadedByFolderName(String mediaPlayerId, String folderText) throws Exception {


		WebElement audioElement = webDriver.waitForElement("//audio[@id='" + mediaPlayerId + "']", ByTypes.xpath,
				webDriver.getTimeout(), true, "audio element", 1000, expectedConditions.precence);

		String fileName = webDriver.getElementSrc(audioElement);
		testResultService.assertEquals(true, fileName.contains(".mp3"), "Audio file not found or not loaded");
		testResultService.assertEquals(true, fileName.contains(folderText), "Media folder not valid or not found");

	}

	public void skipOnBoardingHP() throws Exception {
		
		WebElement getOnBoarding = null;
				
			for(int i = 0;i<5&&getOnBoarding==null;i++) {				
				getOnBoarding = webDriver.waitUntilElementAppears("onBoarding__navigation", ByTypes.className, 3);
			}
		//WebElement element = webDriver.waitUntilElementAppearsAndReturnElement("//div[contains(@class,'onBoarding__instrunctions')]", 3);
		
		//WebElement getOnBoarding = webDriver.waitForElement("//div[contains(@class,'onBoarding__instrunctions')]", ByTypes.xpath, false,5); // this was the previous
		
		//boolean onBoradingStatus = getOnBoarding.isDisplayed();
		
		if (getOnBoarding != null) {
		for (int n = 0; n < 15; n++) {
			Thread.sleep(500);
			webDriver.sendKey(Keys.ARROW_RIGHT);
			
			}
		}

	}
	
	public void setInstitutionSupportCEFR(String instId, boolean showCEFR) throws Exception {
		
		short flag  = 0;
		
		if (showCEFR)
			flag = 1;
		
		String sql = "update Institutions set CEFR = "+ flag +" where InstitutionId = "+ instId;
		dbService.runDeleteUpdateSql(sql);
		
		
	}
	
	public String getWebConfigKey(String product, String key) throws Exception{

		String path = buildPath;		
		
		if(product.equalsIgnoreCase("tms"))
		{
			path = buildPathTMS;
		}
		
		
		return textService.getWebConfigAppSettingsValuByKey(path, key);
		
	}
	
	//--igb 2018.06.11--------------------------
		public void reWriteWebConfigKey(String EdOrTms, String chkKey, String setKeyValue) throws Exception {
			
			
			
			
			if((chkKey == null) || chkKey.isEmpty()) return;
			if((EdOrTms == null) || EdOrTms.isEmpty()) return;
			if((setKeyValue == null) || setKeyValue.isEmpty()) return;
			
			if(!EdOrTms.equalsIgnoreCase("ed") && !EdOrTms.equalsIgnoreCase("tms")) return;
			
			String chkPath = buildPath;		
			
			if(EdOrTms.equalsIgnoreCase("tms"))
			{
			   chkPath = buildPathTMS;
			}
			
			
			String chkKeyValue = null;
			
			try
			{
				chkKeyValue = textService.getWebConfigAppSettingsValuByKey(chkPath, chkKey);
			}
			catch(Exception ex)
			{
			}
			
			
			if((chkKeyValue != null ) && !chkKeyValue.isEmpty() && !chkKeyValue.equalsIgnoreCase(setKeyValue))
			{
 				List<String[]> rewrList = new ArrayList<String[]>();
				
 				String [] rewrEl = {"//add[@key='" + chkKey + "']", "value", setKeyValue}; 
 				
 				rewrList.add(rewrEl);
 				
 				textService.modifySmbXmlFile(chkPath + "web.config", rewrList);
			}
			
		}
		
		
		public void reWriteWebConfigKeyByPath(String path, String chkKey, String setKeyValue) throws Exception {
			
			if((chkKey == null) || chkKey.isEmpty()) return;
			if((path == null) || path.isEmpty()) return;
			if((setKeyValue == null) || setKeyValue.isEmpty()) return;
			
 			List<String[]> rewrList = new ArrayList<String[]>();
 			String [] rewrEl = {"//add[@key='" + chkKey + "']", "value", setKeyValue}; 
 			rewrList.add(rewrEl);	
 			textService.modifySmbXmlFile(path + "web.config", rewrList);
		}

		public String getCustomUrl(String url) {
			String customUrl = dbService.getCusstomUrl(url);
			return customUrl;
		}

		public void skipOptin() {
			WebElement element=null;
			try {
				
				for (int i=1; i<=5 && element==null;i++){
				
				//new WebDriverWait(webDriver.getWebDriver(), 1, 1)
				//	.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.className("optInPrivacyStatement__checkBoxW")));
					element = webDriver.waitForElement("optInPrivacyStatement__checkBoxW", ByTypes.className, false, 2);
				
					if (element != null){
						webDriver.waitForElementAndClick("optInPrivacyStatement__checkBoxW", ByTypes.className);
						Thread.sleep(500);
						//webDriver.waitForElementAndClick("optInPrivacyStatement__continue", ByTypes.id);
						webDriver.waitForElement("optInPrivacyStatement__continue", ByTypes.id).click();
					}	
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	public void restartBrowser() throws Exception {
		webDriver.quitBrowser();
		Thread.sleep(1000);
		webDriver.init();
		Thread.sleep(1000);
		webDriver.maximize();
	}
	
	public String runJavaScriptCommand(String command) {
		try {
			JavascriptExecutor js = (JavascriptExecutor) webDriver.getWebDriver();  
			String result =(String)js.executeScript(command).toString();
			//js.executeScript(command);
			return result;
		} catch (Exception e) {
			return null;
		}
	}
	
	public void runJavaScriptCommandWithArguments(String command, WebElement element) {
		JavascriptExecutor js = (JavascriptExecutor) webDriver.getWebDriver();  
		js.executeScript(command, element);	
	}
		
	public void assignAutomatedTestToStudetInTestEnvironment(String studentId, String courseId,
			int testTypeId, int startDateOffset, int startMinuteOffset, int endDateOffset)
			throws Exception {
		
		Date date = new Date();
		Calendar startTest = Calendar.getInstance();
		Calendar endTest = Calendar.getInstance();
		DateFormat dateFormatBasic = new SimpleDateFormat("yyyyMMdd");
		DateFormat dateFormatLocal = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
		String clientLocalHour = dateFormatLocal.format(date);
				
		startTest.setTime(date);
		startTest.add(Calendar.DATE, startDateOffset);
		String startDate = dateFormatBasic.format(startTest.getTime()).toString();
		
		int startMinutes = 0;
		
			if (startDateOffset == 0 && startMinuteOffset == 0)
				startMinutes = 1;
				
			else {
					int currentHours = startTest.get(Calendar.HOUR_OF_DAY);
					int currentMinutes = startTest.get(Calendar.MINUTE);
					startMinutes = currentHours * 60 + currentMinutes + startMinuteOffset;
				}
			
			int currentHours = startTest.get(Calendar.HOUR_OF_DAY);
			int currentMinutes = startTest.get(Calendar.MINUTE);
			int endtimeutc= currentHours *60 + currentMinutes +3+ startMinuteOffset;
			
		endTest.setTime(startTest.getTime());
		endTest.add(Calendar.DATE, endDateOffset);
		String endDate = dateFormatBasic.format(endTest.getTime()).toString();
		
		boolean newTe= checkIfInstValueIsNewTe();
		
		String testLengthDB = "";
		if (newTe) {
			// take from inst settings. if null take default
			String testLengthByInst = dbService.getTestLengthByInstitution(BasicNewUxTest.institutionId, Integer.toString(testTypeId));
			if (testLengthByInst == null) {
				testLengthDB = dbService.getTestLengthByTestTypeId(Integer.toString(testTypeId));// new te
			} else {
				testLengthDB = testLengthByInst;
			}
		} else {
			testLengthDB = dbService.getTestLengthByInstitutionName(BasicNewUxTest.institutionName);//old te
		}
		String sp =  "exec SetUserExitTestSettings " + studentId	+ ","+courseId+",'"+ startDate + "',"+startMinutes+",'" + endDate + "','"+endtimeutc+"','"+clientLocalHour+"',"+testLengthDB+","+testTypeId;
		dbService.runStorePrecedure(sp);
		
		// retrieve test id (?), and userExitSettingsId
		
		//report.startStep("Return Test Id");
		String testId = getAssignedTestIdForStudent(studentId,courseId, testTypeId);
		String userExitSettingsId = dbService.getUserExitSettingsId(studentId, testId, courseId, Integer.toString(testTypeId));

		// retrieve token
		String token = getTokenFromConsoleWithParameters("1", studentId, testId, userExitSettingsId);
		
		// run update query
		dbService.updateAssignWithToken(token, userExitSettingsId);
		
		TestEnvironmentPage  testEnvironmentPage = new TestEnvironmentPage(webDriver, testResultService);
		testEnvironmentPage.testDuration = dbService.getTestDurationByUserExitSettingsId(userExitSettingsId);

	}
	
	public String getTokenFromConsoleWithParameters(String encodeOrDecode, String userId, String testId, String UserExitTestSettingId) throws Exception{
		//String command = "//storage/storage/RD/QA/Automation/JwtBuilder/Console/v1.0.0.1/JwtBuilderConsole.exe " + encodeOrDecode + " " + userId + " " + testId  + " " + UserExitTestSettingId;
		report.addTitle("Type of action :" + encodeOrDecode + "; UserId :" + userId + "; Test Id: " + testId + "; UserExitTestSettingId: " + UserExitTestSettingId);
		
		//String command = "//davidm/AutomationTools/JwtBuilder/Console/v1.0.0.1/JwtBuilderConsole.exe " + encodeOrDecode + " " + userId + " " + testId  + " " + UserExitTestSettingId;
		String command = "//"+automationServer+"/AutoLogs/ToolsAndResources/TokenForCourseTest/JwtBuilder/Console/v1.0.0.1/JwtBuilderConsole.exe " + encodeOrDecode + " " + userId + " " + testId  + " " + UserExitTestSettingId; // loadagent jenkins
		
		return runConsoleProgram(command);
	}
	
	public String runConsoleProgram(String command) throws Exception{
		Process process = null;
		
		try {
		    process = Runtime.getRuntime().exec(command);
		} catch (IOException e) {
		    e.printStackTrace(); 
		    report.startStep("CMD error: " + e);
		}
		
		BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
		// read the output from the command
		//System.out.println("Here is the standard output of the command:\n");
		
		String result = "";
		String s = null;
		while ((s = stdInput.readLine()) != null) {
		    System.out.println(s);
		    result += s;
		}
		report.startStep("CMD result: " + result);
		return result;
	}
	
	public String getPartialScreensShot(int xLocation, int yLocation, int size1, int size2) throws IOException{
		
		File screen = ((TakesScreenshot) webDriver.getWebDriver()).getScreenshotAs(OutputType.FILE);
		BufferedImage img = ImageIO.read(screen);

        BufferedImage dest = img.getSubimage(xLocation, yLocation, size1, size2);
        ImageIO.write(dest, "png", screen);  
        return screen.toString();
	}
	
	public void compareScreenShots(String expectedImgPath, String actualImgPath) throws Exception {
	    BufferedImage expectedImage = ImageComparisonUtil.readImageFromResources(expectedImgPath);
		BufferedImage actualImage = ImageComparisonUtil.readImageFromResources(actualImgPath);

	    // Create ImageComparison object and compare the images
	    ImageComparisonResult imageComparisonResult = new ImageComparison(actualImage, expectedImage).compareImages();
	    //ImageComparisonState i = imageComparisonResult.getImageComparisonState();
	    //System.out.println(i.toString());
	    // Check the result
	    testResultService.assertEquals(true,imageComparisonResult.getImageComparisonState().equals(ImageComparisonState.MATCH),"Conflict sign is Incorrect.");
	    //assertEquals(ImageComparisonState.MATCH, imageComparisonResult.getImageComparisonState());
		
	}
	
	public boolean validateListIsOrdered(String descOrAsc, ArrayList<String> columnList) throws Exception {
		descOrAsc = descOrAsc.toLowerCase();
		boolean isSorted = false;
		
		for (int i = 0; i < columnList.size(); i++) {
			for (int j = i+1; j < columnList.size(); j++) {
				if (descOrAsc.equals("desc")) {
					if (columnList.get(i).toLowerCase().compareTo(columnList.get(j).toLowerCase()) > 0) {
						isSorted = true;
					} else if (columnList.get(i).toLowerCase().compareTo(columnList.get(j).toLowerCase()) < 0) {
						isSorted = false;
						break;
					} else {
						isSorted = true;
						continue;
					}	
				} else if (descOrAsc.equals("asc")) {
					if (columnList.get(i).toLowerCase().compareTo(columnList.get(j).toLowerCase()) > 0) {
						isSorted = false;
						break;
					} else if (columnList.get(i).toLowerCase().compareTo(columnList.get(j).toLowerCase()) < 0) {
						isSorted = true;
					} else {
						isSorted = true;
						continue;
					}
				}
			}	
			if (!isSorted) {
				break;
			}
		}
		
		testResultService.assertEquals(true, isSorted, "List is not Sorted " + descOrAsc +". List: " + columnList, true);
		return isSorted;
		
		// different way that checks only asc
		/*List<String> sortedList = new ArrayList<String>(columnList);
		Collections.sort(columnList);
		return sortedList.equals(columnList);*/
	}
	
	public Date getDateByStringAndFormat(String dateAsString, String dateFormat) throws Exception {
		Date date = new SimpleDateFormat(dateFormat).parse(dateAsString); 
		return date;
	}
	
	public List<String> getDatesOnlyFromDateTimeList(List<String> datesTimesList) {
		List<String> datesList = new ArrayList();
		for (int i = 0 ; i < datesTimesList.size(); i++) {
			datesList.add(datesTimesList.get(i).split(" ")[0]);
		}
		return datesList;
	}
	
	public int getEndDateOffSetOfInstitution(String institutionId) {
		int defaultEndDate = dbService.getInstitutionTimeOfCourseTestEndDate(institutionId);
		return defaultEndDate;
	}
	
	public boolean validateDatesListIsOrdered(String descOrAsc, ArrayList<String> columnList) throws Exception {
		descOrAsc = descOrAsc.toLowerCase();
		boolean isSorted = false;
		
		for (int i = 0; i < columnList.size(); i++) {
			for (int j = i+1; j < columnList.size(); j++) {
				if (descOrAsc.equals("desc")) {
					if (columnList.get(i).equals("") || columnList.get(j).equals("")) {
						// date is empty
					} else {
						if (Integer.parseInt(columnList.get(i).split("/")[2]) - Integer.parseInt(columnList.get(j).split("/")[2]) > 0 ||
							Integer.parseInt(columnList.get(i).split("/")[1]) - Integer.parseInt(columnList.get(j).split("/")[1]) > 0 ||
							Integer.parseInt(columnList.get(i).split("/")[0]) - Integer.parseInt(columnList.get(j).split("/")[0]) > 0) {
								isSorted = true;
						} else if (Integer.parseInt(columnList.get(i).split("/")[2]) - Integer.parseInt(columnList.get(j).split("/")[2]) < 0 ||
									Integer.parseInt(columnList.get(i).split("/")[1]) - Integer.parseInt(columnList.get(j).split("/")[1]) < 0 ||
									Integer.parseInt(columnList.get(i).split("/")[0]) - Integer.parseInt(columnList.get(j).split("/")[0]) < 0) {
							isSorted = false;
							break;
						} else if (Integer.parseInt(columnList.get(i).split("/")[2]) - Integer.parseInt(columnList.get(j).split("/")[2]) == 0 ||
									Integer.parseInt(columnList.get(i).split("/")[1]) - Integer.parseInt(columnList.get(j).split("/")[1]) == 0 ||
									Integer.parseInt(columnList.get(i).split("/")[0]) - Integer.parseInt(columnList.get(j).split("/")[0]) == 0) {
							isSorted = true;
							continue;
						}	
					}
				} else if (descOrAsc.equals("asc")) {
					if (columnList.get(i).equals("") || columnList.get(j).equals("")) {
						// date is empty
					} else {
						if (Integer.parseInt(columnList.get(i).split("/")[2]) - Integer.parseInt(columnList.get(j).split("/")[2]) > 0 ||
							Integer.parseInt(columnList.get(i).split("/")[1]) - Integer.parseInt(columnList.get(j).split("/")[1]) > 0 ||
							Integer.parseInt(columnList.get(i).split("/")[0]) - Integer.parseInt(columnList.get(j).split("/")[0]) > 0) {
							isSorted = false;
							break;
						} else if (Integer.parseInt(columnList.get(i).split("/")[2]) - Integer.parseInt(columnList.get(j).split("/")[2]) < 0 ||
								Integer.parseInt(columnList.get(i).split("/")[1]) - Integer.parseInt(columnList.get(j).split("/")[1]) < 0 ||
								Integer.parseInt(columnList.get(i).split("/")[0]) - Integer.parseInt(columnList.get(j).split("/")[0]) < 0) {
							isSorted = true;
						} else if (Integer.parseInt(columnList.get(i).split("/")[2]) - Integer.parseInt(columnList.get(j).split("/")[2]) == 0 ||
								Integer.parseInt(columnList.get(i).split("/")[1]) - Integer.parseInt(columnList.get(j).split("/")[1]) == 0 ||
								Integer.parseInt(columnList.get(i).split("/")[0]) - Integer.parseInt(columnList.get(j).split("/")[0]) == 0) {
							isSorted = true;
							continue;
						}
					}
				}
			}	
			if (!isSorted) {
				break;
			}
		}
		
		testResultService.assertEquals(true, isSorted, "List is not Sorted " + descOrAsc +". List: " + columnList, true);
		return isSorted;
		
		// different way that checks only asc
		/*List<String> sortedList = new ArrayList<String>(columnList);
		Collections.sort(columnList);
		return sortedList.equals(columnList);*/
	}

	public void checkAndSetCurrentstaticContentConnectionString(boolean useStaticDb) {
		
		if (useStaticDb){

			if (DbService.currentGeneralDB.equalsIgnoreCase("ci") || DbService.currentGeneralDB.equalsIgnoreCase("prod2")){
				DbService.currentStaticDB="internal";
					
			} else if (DbService.currentGeneralDB.equalsIgnoreCase("production") || DbService.currentGeneralDB.equalsIgnoreCase("edProduction")){
				DbService.currentStaticDB="production";
			}
		}
		else{
			DbService.currentStaticDB="";
		}
	}
	
	public void restartBrowserInNewURL(String instName, boolean changeURL) throws Exception{
		
        initializeData();
       
        String url = webDriver.getUrl();
        if (changeURL) {
        	if (BasicNewUxTest.CannonicalDomain.equalsIgnoreCase("")){
        		url= "https://" + BasicNewUxTest.CannonicalDomain;
        	}
        	else{
        		url = url.split(".com")[0] + ".com/" + instName;
        		//webDriver.openIncognitoChromeWindow();
        	}
        }
       
        webDriver.quitBrowser();
        webDriver.init();
        webDriver.maximize();
        webDriver.openUrl(url);
	}

public void restartBrowserInNewURL(String instName, boolean changeURL,boolean opencongnito) throws Exception{
		
        initializeData();
       
        String url = webDriver.getUrl();
        if (changeURL) {
        	if (!BasicNewUxTest.CannonicalDomain.equalsIgnoreCase("")){
        		url= "https://" + BasicNewUxTest.CannonicalDomain;
        	}
        	else{
        		url = url.split(".com")[0] + ".com/" + instName;
        	}
        }
       
        if (opencongnito)
        	webDriver.openIncognitoChromeWindow();
        
        webDriver.quitBrowser();
        webDriver.init();
        webDriver.maximize();
        webDriver.openUrl(url);
	}


	public void updateInstitutionMyProfileSettings(String instId, String MyProfileGroupId) {
		try {
			dbService.runDeleteUpdateSql("update Institutions set MyProfileGroupId = " + MyProfileGroupId + " where institutionId = " + instId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// TODO Auto-generated method stub
		
	}

	public String checkUserAddressIsExist(String studentId) throws Exception {
		String userAddress = dbService.getUserAddressByUserId(studentId);
		
		if (userAddress==null)
			testResultService.addFailTest("User Address is empty", false, false);
		
		return userAddress;
	}

	public String[] getUserWithUserAddress(String institutionId, String studentId) {
		String[] userAddress = dbService.getUserAddressByInstitutionId(institutionId,studentId);
		return userAddress;
	}
	
	public void selectOptionFromList(String listId, String optionToSelect) {
		try{
		
		WebElement list = webDriver.getWebDriver().findElement(By.xpath("//select[@id='"+listId+"']"));
		//webDriver.waitForElement(listId, ByTypes.id)
		List<WebElement> listOptions = webDriver.getWebDriver().findElements(By.xpath("//select[@id='"+listId+"']//option"));
		webDriver.ClickElement(list);
		for (int i = 0; i < listOptions.size(); i++) {
			if (listOptions.get(i).getText().equals(optionToSelect)) {
				webDriver.ClickElement(listOptions.get(i));
			}
		}
		}catch(Exception e){}
	}
	
	public void assignPltTestToStudetInTestEnvironment(String studentId, int testTypeId, 
			String testId, String ownerId, String instId, String classId) throws Exception {
		
		String sql = "declare @p14 dbo.OrganizationStructurePath "
				+ "insert into @p14 values("+instId+","+classId+",NULL,"+studentId+") "
				+ "exec TestAdministrations_Save "
				+ "@TestAdministrationId=NULL, "
				+ "@Name=N'Test Administration 7/12/2021, 4:28:38 PM', "
				+ "@CourseId=11, "
				+ "@TestId="+testId+", "
				+ "@OwnerId="+ownerId+", "
				+ "@TestTypeId="+testTypeId+", "
				+ "@TestDurationMinutes=60, "
				+ "@TestLicenseId=NULL, "
				+ "@AdministrationSourceId=1, "
				+ "@StartUTCDateTime='1753-01-01 00:00:00', "
				+ "@EndUTCDateTime='9999-12-31 00:00:00', "
				+ "@StartClientDateTime='1753-01-01 00:00:00', "
				+ "@EndClientDateTime='9999-12-31 00:00:00', "
				+ "@ParticipantsPaths=@p14";
		
		dbService.runStorePrecedure(sql);
		
		// Return User Test Administration Id
		//String testId = getAssignedTestIdForStudent(studentId,courseId, testTypeId);
		String userTestAdministrationId = dbService.getUserTestAdministrationByStudentId(studentId);//getUserExitSettingsId(studentId, testId, courseId, Integer.toString(testTypeId));

		// Retrieve Token
		String token = getTokenFromConsoleWithParameters("1", studentId, testId, userTestAdministrationId);
		
		// Update Token in DB
		dbService.updateAssignPltWithToken(token, userTestAdministrationId);
	}
	

	public void checkNoDuplicateTestAssignmentInDB(String studentId, String courseId, int impType,String expectedTestCount) {
		// TODO Auto-generated method stub
		String actualTestCount = dbService.checkNoDuplicateTestAssignmentInDB(studentId,courseId,impType);
		assertEquals("test assignment count is more than expected", expectedTestCount, actualTestCount);	
	}
	
	public String getLaterDate(String firstDate, String secondDate, String format) throws ParseException {
		String laterDate = "";
		
		SimpleDateFormat sdformat = new SimpleDateFormat(format);
		Date first = sdformat.parse(firstDate);
		Date second = sdformat.parse(secondDate);
		if (first.compareTo(second) > 0) {
			// first is later
			laterDate = firstDate;
		} else if (first.compareTo(second) < 0) {
			// second is later
			laterDate = secondDate;
		} else {
			// equal
			laterDate = firstDate;
		}
		
		return laterDate;
	}
	
	public boolean checkIfInstValueIsNewTe() throws Exception {
		Boolean newTe = true;
		String instPropertValue = dbService.getInstPropertyValueByProperyId(BasicNewUxTest.institutionId, 35);
		if (instPropertValue == null) {
			newTe = false;
		} else {
			
			instPropertValue = instPropertValue.split("T")[0];
			//instPropertValue = textService.convertDateToDifferentFormat(instPropertValue, "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd");

			String currentDate = getCurrentDateByFormat("yyyy-MM-dd");
			
			String laterDate = getLaterDate(instPropertValue, currentDate, "yyyy-MM-dd");
			if (laterDate.equals(currentDate)) {
				newTe = true;
			} else if (laterDate.equals(instPropertValue)){
				newTe = false;
			}
		}
		return newTe;
	}
	
	public String[] createUSerUsingSPAndReturnDetails(String instId, String className)
			throws Exception {
		String studentUserName = "stud" + dbService.sig(8);
		createUSerUsingSP(instId, studentUserName, studentUserName,
				studentUserName, "12345", studentUserName + "@edusoft.co.il",
				className);
		
		String userId = dbService.getUserIdByUserName(studentUserName, instId);
		
		if (userId == null) {
			testResultService.addFailTest("User was not created", true, false);
		}

		else {
			setOnBoardingToVisited(userId);
			dbService.setUserOptIn(userId, true);
			dbService.insertUserToAutomationTable(instId,userId,studentUserName,className,buildId);
		}
		
		report.report("UserName is: "+ studentUserName + "UserId is:" + userId);
		String userDetails[] = {studentUserName,"12345",userId};
		
		return userDetails;
	}

	public void open_PLT_API(String institutionName, String className, String apiToken,boolean newUser) {
		
		String baseUrl = CILink.split(".com")[0] + ".com/";
		
		String accessUrl = urlForExternalPages;
		accessUrl = accessUrl.split(".com")[0] + ".com/" + "Languages";
		
		String accessPath = buildPathForExternalPages + "Languages"; //pageHelper.buildPath
		
		String testFile="";
		
		try {
			testFile = "pltapi_" + dbService.sig(5) + ".html";
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//String returnUrl = "https://www.yahoo.com/";
		String returnUrl = staticUrlForExternalPages + "Languages/" + "viewPLT.html";
		
		//apiplt1
		List<String> html = generateHTML(baseUrl + "PlacementTestEntry.aspx", institutionName, className,apiToken,returnUrl,newUser);
		
		try {
			textService.writeListToSmbFile(accessPath, testFile, html, false);
			//textService.writeListToSmbFile(accessPath+"/"+testFile, html, netService.getDomainAuth());
			String openURL = accessUrl +"/"+testFile;
			
			closeBrowserAndOpenInCognito(false);
			String institutionId = dbService.getInstituteIdByName(institutionName);

			webDriver.openUrl(openURL);
			Thread.sleep(2000);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void closeBrowserAndOpenInCognito(boolean incognito) throws Exception {
		
		webDriver.closeBrowser();
		if (incognito) {
			webDriver.openIncognitoChromeWindow();
		}
		webDriver.init();
		webDriver.maximize();
		//webDriver.getWebDriver().manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
	}

	public List<String> generateHTML(String path,String instName,String className,String apiToken, String returnUrl,boolean newUser) {
		String[] user = null;
		//boolean getUser=newUser;
		//userName="";
		String userId ="";
		try {
			
			if (newUser){
				userId = createUSerUsingSP(BasicNewUxTest.institutionId, className);
				BasicNewUxTest.userName = dbService.getUserNameAndPasswordByUserId(userId).get(0)[0];
				//user = getUserNamePassworIddByInstitutionIdAndClassName(BasicNewUxTest.institutionId,className);
				//BasicNewUxTest.userName = user[0];
			}
			else{
				BasicNewUxTest.userName = "stud" + dbService.sig(5);
				//user = createUserAndReturnDetails(institutionId,className);
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	
		List<String> wList = new ArrayList<String>();
		wList.add("<!DOCTYPE html>");
		wList.add("<html>");
		wList.add("<head>");
		
		wList.add("\t<meta charset=\"utf-8\" />");
		wList.add("\t<title></title>");
		
		wList.add("</head>");
		wList.add("<body>");
		
		wList.add("\t<form method=\"post\" action=\""+path+"\">");
		wList.add("\t\t<input type=\"hidden\" name=\"IName\" value=\""+instName+"\"/>");
		wList.add("\t\t<input type=\"hidden\" name=\"Token\" value=\""+apiToken+"\"/>");
		wList.add("\t\t<input type=\"hidden\" name=\"UserName\" value=\"" + BasicNewUxTest.userName + "\"/>");
		wList.add("\t\t<input type=\"hidden\" name=\"Language\" value=\"English\"/>");
		wList.add("\t\t<input type=\"hidden\" name=\"ReturnUrl\" value=\"" + returnUrl + "\"/>");
		wList.add("\t\t<input type=\"hidden\" name=\"Proctoring\" value=\"false\"/>");
		wList.add("\t\t<input type=\"hidden\" name=\"PushNotification\" value=\"http://lib.ru\"/>");
		wList.add("\t\t<input type=\"submit\" name=\"btnSubmit\"/>");
		wList.add("\t</form>");
	
		wList.add("</body>");
		wList.add("</html>");
		
		return wList;
	}

	public String createMoodlePostList(String institutionName,String chkURL,String className,String CanvasClassId, String roles,String chkPath,String testFile, String institutionId,boolean useSecurityCheck) throws Exception  {
		
		String[] user = null;
		boolean getUser=false;
		String userName="";
		
		try {
			
			if (getUser){
				user = getUserNamePassworIddByInstitutionIdAndClassName(institutionId,className);
				userName = BasicNewUxTest.userName = user[0];
			}
			else
				userName = BasicNewUxTest.userName = "stud" + dbService.sig(5);
				
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<String> wList = new ArrayList<String>();
		wList = createHtmlTitle(wList);




		if (chkURL.contains("engdis.com")){
		//if (useSecurityCheck){
			Map<String,String> signature = getSignatureForSecurityLTIEntrance(wList, chkURL, institutionName, CanvasClassId, roles, userName,className);
			wList.add("\t<form method=\"post\" action=\"" + chkURL + "entrance.aspx\">");
			for (String key:signature.keySet()){
				String value = signature.get(key);
				if (key.equalsIgnoreCase("btnSubmit")){
					wList.add("\t\t<input type=\"submit\" name=\""+key+"\" value=\""+value+"\" />");
				}else {
					wList.add("\t\t<input type=\"hidden\" name=\"" + key + "\" value=\"" + value + "\" />");
				}
			}

			/*String responseSignature = signature.get("oauth_signature");
			String signatureMethod = signature.get("oauth_signature_method");
			String verssion = signature.get("oauth_version");

			wList.add("\t<form method=\"post\" action=\"" + chkURL + "entrance.aspx\">");
			wList.add("\t\t<input type=\"submit\" name=\"btnSubmit\" value=\"GO TO ED\" />");
			wList.add("\t\t<input type=\"hidden\" name=\"oauth_nonce\" value=\"ff78b85272e20ff61f28b5151c7a2979\" />");
			wList.add("\t\t<input type=\"hidden\" name=\"oauth_consumer_key\" value=\"ED_FP\" />");
			wList.add("\t\t<input type=\"hidden\" name=\"oauth_signature\" value=\""+responseSignature+"\" />");
			wList.add("\t\t<input type=\"hidden\" name=\"lis_outcome_service_url\" value=\"https://campus.upi.ac.cr/lms/mod/lti/service.php\" />");
			wList.add("\t\t<input type=\"hidden\" name=\"custom_edinstitution\" value=\"" + institutionName + "\" />");
			wList.add("\t\t<input type=\"hidden\" name=\"roles\" value=\"" + roles + "\" />");
			wList.add("\t\t<input type=\"hidden\" name=\"lis_person_name_family\" value=\"" + userName + " Family" + "\" />");
			wList.add("\t\t<input type=\"hidden\" name=\"lis_person_name_given\" value=\"" + userName + " Given" + "\" />");
			wList.add("\t\t<input type=\"hidden\" name=\"lis_person_name_full\" value=\"" + userName + " Full" + "\" />");
			wList.add("\t\t<input type=\"hidden\" name=\"lis_person_contact_email_primary\" value=\"" + userName + "@edusoft.co.il\" />");
			wList.add("\t\t<input type=\"hidden\" name=\"oauth_timestamp\" value=\"1547994161\" />");
			wList.add("\t\t<input type=\"hidden\" name=\"user_id\" value=\"" + userName + "\" />");

			wList.add("\t\t<input type=\"hidden\" name=\"lis_result_sourcedid\" value=\"aaa$^-\" />");
			wList.add("\t\t<input type=\"hidden\" name=\"ext_user_username\" value=\"" + userName + "\" />");

			wList.add("\t\t<input type=\"hidden\" name=\"custom_user_sections\" value=\"" + className + "\" />"); //ClassName
			wList.add("\t\t<input type=\"hidden\" name=\"context_title\" value=\"" + CanvasClassId + "\" />"); //ClassId

			wList.add("\t\t<input type=\"hidden\" name=\"launch_presentation_locale\" value=\"es\" />");
			wList.add("\t\t<input type=\"hidden\" name=\"ext_lms\" value=\"moodle-2\" />");
			wList.add("\t\t<input type=\"hidden\" name=\"tool_consumer_info_product_family_code\" value=\"moodle\" />");
			wList.add("\t\t<input type=\"hidden\" name=\"tool_consumer_info_version\" value=\"2020061501.14\" />");
			wList.add("\t\t<input type=\"hidden\" name=\"oauth_callback\" value=\"about:blank\" />");
			wList.add("\t\t<input type=\"hidden\" name=\"lti_version\" value=\"LTI-1p0\" />");
			wList.add("\t\t<input type=\"hidden\" name=\"oauth_version\" value=\""+verssion+"\" />");
			wList.add("\t\t<input type=\"hidden\" name=\"lti_message_type\" value=\"basic-lti-launch-request\" />");
			wList.add("\t\t<input type=\"hidden\" name=\"tool_consumer_instance_guid\" value=\"campus.upi.ac.cr\" />");
			wList.add("\t\t<input type=\"hidden\" name=\"tool_consumer_instance_name\" value=\"Campus UPI\" />");
			wList.add("\t\t<input type=\"hidden\" name=\"tool_consumer_instance_description\" value=\"Campus Universidad Politécnica Internacional\" />");
			wList.add("\t\t<input type=\"hidden\" name=\"launch_presentation_document_target\" value=\"window\" />");
			//wList.add("\t\t<input type=\"hidden\" name=\"launch_presentation_return_url\" value=\"https://campus.upi.ac.cr/lms/mod/lti/return.php?course=206&launch_container=4&instanceid=24&sesskey=WA6SQfINSW\"  />");
			wList.add("\t\t<input type=\"hidden\" name=\"oauth_signature_method\" value=\""+signatureMethod+"\" />");*/


		}else {

			wList.add("\t<form method=\"post\" action=\"" + chkURL + "entrance.aspx\">");
			wList.add("\t\t<input type=\"submit\" name=\"btnSubmit\" value=\"GO TO ED\" />");
			wList.add("\t\t<input type=\"hidden\" name=\"oauth_nonce\" value=\"ff78b85272e20ff61f28b5151c7a2979\" />");
			wList.add("\t\t<input type=\"hidden\" name=\"oauth_consumer_key\" value=\"ED_FP\" />");
			wList.add("\t\t<input type=\"hidden\" name=\"oauth_signature\" value=\"iX8HpGEYxe2jwBttwTx5WoAL3Rs=\" />");
			wList.add("\t\t<input type=\"hidden\" name=\"lis_outcome_service_url\" value=\"https://campus.upi.ac.cr/lms/mod/lti/service.php\" />");
			wList.add("\t\t<input type=\"hidden\" name=\"custom_edinstitution\" value=\"" + institutionName + "\" />");
			wList.add("\t\t<input type=\"hidden\" name=\"roles\" value=\"" + roles + "\" />");
			wList.add("\t\t<input type=\"hidden\" name=\"lis_person_name_family\" value=\"" + userName + " Family" + "\" />");
			wList.add("\t\t<input type=\"hidden\" name=\"lis_person_name_given\" value=\"" + userName + " Given" + "\" />");
			wList.add("\t\t<input type=\"hidden\" name=\"lis_person_name_full\" value=\"" + userName + " Full" + "\" />");
			wList.add("\t\t<input type=\"hidden\" name=\"lis_person_contact_email_primary\" value=\"" + userName + "@edusoft.co.il\" />");
			wList.add("\t\t<input type=\"hidden\" name=\"oauth_timestamp\" value=\"1547994161\" />");
			wList.add("\t\t<input type=\"hidden\" name=\"user_id\" value=\"" + userName + "\" />");

			wList.add("\t\t<input type=\"hidden\" name=\"lis_result_sourcedid\" value=\"aaa$^-\" />");
			wList.add("\t\t<input type=\"hidden\" name=\"ext_user_username\" value=\"" + userName + "\" />");

			wList.add("\t\t<input type=\"hidden\" name=\"custom_user_sections\" value=\"" + className + "\" />"); //ClassName
			wList.add("\t\t<input type=\"hidden\" name=\"context_title\" value=\"" + CanvasClassId + "\" />"); //ClassId

			wList.add("\t\t<input type=\"hidden\" name=\"launch_presentation_locale\" value=\"es\" />");
			wList.add("\t\t<input type=\"hidden\" name=\"ext_lms\" value=\"moodle-2\" />");
			wList.add("\t\t<input type=\"hidden\" name=\"tool_consumer_info_product_family_code\" value=\"moodle\" />");
			wList.add("\t\t<input type=\"hidden\" name=\"tool_consumer_info_version\" value=\"2020061501.14\" />");
			wList.add("\t\t<input type=\"hidden\" name=\"oauth_callback\" value=\"about:blank\" />");
			wList.add("\t\t<input type=\"hidden\" name=\"lti_version\" value=\"LTI-1p0\" />");
			wList.add("\t\t<input type=\"hidden\" name=\"lti_message_type\" value=\"basic-lti-launch-request\" />");
			wList.add("\t\t<input type=\"hidden\" name=\"tool_consumer_instance_guid\" value=\"campus.upi.ac.cr\" />");
			wList.add("\t\t<input type=\"hidden\" name=\"tool_consumer_instance_name\" value=\"Campus UPI\" />");
			wList.add("\t\t<input type=\"hidden\" name=\"tool_consumer_instance_description\" value=\"Campus Universidad Politécnica Internacional\" />");
			wList.add("\t\t<input type=\"hidden\" name=\"launch_presentation_document_target\" value=\"window\" />");
			wList.add("\t\t<input type=\"hidden\" name=\"launch_presentation_return_url\" value=\"https://campus.upi.ac.cr/lms/mod/lti/return.php?course=206&launch_container=4&instanceid=24&sesskey=WA6SQfINSW\"  />");
			wList.add("\t\t<input type=\"hidden\" name=\"oauth_signature_method\" value=\"HMAC-SHA1\" />");
		}
        wList = createHtmlTail(wList);
        
  
        try {
        	/*String[] buildPath = chkPath.split("//");
    		String path = "\\";
    		for(int i=1;i<buildPath.length-1;i++) {
    			path=path+"\\"+buildPath[i];
    		}
    		path = path +"\\Languages\\"+testFile;
    		boolean useSMB = false;
    		textService.writeListToSmbFile(path, wList, false);*/
			textService.writeListToSmbFile(chkPath, testFile, wList, false);
			//textService.writeListToSmbFile(chkPath + testFile, wList, netService.getDomainAuth());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return userName;
	}

	private Map<String, String> getSignatureForSecurityLTIEntrance(List<String> wList, String chkURL, String institutionName, String canvasClassId,
																									String roles, String userName,String className) {

		Map<String,String> parameters = new HashMap<>();
		parameters.put("btnSubmit","GO TO ED");
		//parameters.put("oauth_nonce","ff78b85272e20ff61f28b5151c7a2979");
		//parameters.put("oauth_consumer_key","ED_FP");
		parameters.put("lis_outcome_service_url","https://campus.upi.ac.cr/lms/mod/lti/service.php");
		parameters.put("custom_edinstitution",institutionName);
		parameters.put("roles",roles);
		parameters.put("lis_person_name_family",userName + " Family" );
		parameters.put("lis_person_name_given",userName + " Given");
		parameters.put("lis_person_name_full",userName + " Full");
		parameters.put("lis_person_contact_email_primary",userName + "@edusoft.co.il");
		//parameters.put("oauth_timestamp","1547994161");
		parameters.put("user_id",userName);
		parameters.put("lis_result_sourcedid","aaa$^-");
		parameters.put("ext_user_username",userName);
		parameters.put("custom_user_sections",className);
		parameters.put("context_title",canvasClassId);
		parameters.put("launch_presentation_locale","es");
		parameters.put("ext_lms","moodle-2");
		parameters.put("tool_consumer_info_product_family_code","moodle");
		parameters.put("tool_consumer_info_version","2020061501.14");
		parameters.put("oauth_callback","about:blank");
		parameters.put("lti_version","LTI-1p0");
		parameters.put("lti_message_type","basic-lti-launch-request");
		parameters.put("tool_consumer_instance_guid","campus.upi.ac.cr");
		parameters.put("tool_consumer_instance_name","Campus UPI");
		parameters.put("tool_consumer_instance_description","Campus Universidad ABC Internacional");
		parameters.put("launch_presentation_document_target","window");
		//parameters.put("launch_presentation_return_url","https://campus.upi.ac.cr/lms/mod/lti/return.php?course=206&launch_container=4&instanceid=24&sesskey=WA6SQfINSW");
		//parameters.put("oauth_signature_method","HMAC-SHA1");




		//wList.add("\t\t<input type=\"hidden\" name=\"lti_version\" value=\"LTI-1p0\" />");
		//wList.add("\t\t<input type=\"hidden\" name=\"lti_message_type\" value=\"basic-lti-launch-request\" />");

		//wList.add("\t\t<input type=\"hidden\" name=\"oauth_signature_method\" value=\"HMAC-SHA1\" />");
		String timestamp = String.valueOf(new Timestamp(System.currentTimeMillis()));
		//Long time = System.currentTimeMillis();
		String url = chkURL;
		url = url.split("com/")[0]+"com/entrance.aspx";
		Map<String, String> signedParameters = null;
		try {
			signedParameters = new LtiOauthSigner().signParameters(parameters, "ED_FP", "12345", url, "POST");
		} catch (LtiSigningException e) {
			throw new RuntimeException(e);
		}

		return signedParameters;
	}

	/*
        public void closeBrowserAndOpenInCognitoWindow() {
            try {
                webDriver.quitBrowser();
                webDriver.openIncognitoChromeWindow();
                webDriver.init();
                Thread.sleep(2);
                webDriver.maximize();
                Thread.sleep(2);

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    */
	public void insertRecordToUserTestProgressPLT(String studentId, String userExitTestSettingsId,
			String pltStartLevel) throws Exception {
		
		String testSettingJSON = dbService.gettestSettingsJson();
		String currentDateServer = getCurrentDateByFormat("yyyy-MM-dd hh:mm:ss.sss");
		String currentDateClient = getCurrentDateByFormat("yyyy-MM-dd hh:mm:ss");
		String sql = "exec UserTestProgress_Insert "
				+ "@UserId="+studentId+","
				+ "@TestId=11,"
				+ "@CourseId=11,"
				+ "@TestTypeId=1,"
				+ "@UserExitTestSettingsId="+userExitTestSettingsId+","
				+ "@ServerStartTime='"+currentDateServer+"'," //2021-09-14 11:17:31.150
				+ "@ClientStartDateTime='"+currentDateClient+"'," //2021-09-14 11:17:31
				+ "@TestSettingJSON=N'"+testSettingJSON+"',"
				+ "@PLTStartLevel='"+pltStartLevel+"'";//B2
		
		dbService.runStorePrecedure(sql);
	}
	
	public String getImplementationTypeName(int impTypeId,String area) {
		
		String recordRow="";
		String sql = "select ImpTypeFeatureEDName,ImpTypeFeatureDisplay from ImplementationTypeFeatures where ImpTypeFeatureId=" +impTypeId;
		
		try {	
			List<String[]> recordList = dbService.getStringListFromQuery(sql,2,true);
			
			if (area.equalsIgnoreCase("ED"))
				recordRow = recordList.get(0)[0];
			
			else if (area.equalsIgnoreCase("Display"))
				recordRow = recordList.get(0)[1];
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return recordRow;
	}
	
	public String getWindowsUserName() throws IOException{
		String userName="";
		try {
			userName = runConsoleProgram("cmd /c runas /auto: echo %USERNAME%"); // whoami
			if(userName.contains("\\")) {
				userName = userName.split(Pattern.quote(File.separator))[1];
			}
		}
		
		catch(Exception e) {
			
		}
		return userName;
	}

	
	private List<String> createHtmlTitle(List<String> wList)
	{
		wList.add("<!DOCTYPE html>");
		wList.add("<html>");
		wList.add("<head>");
		
		wList.add("\t<meta charset=\"utf-8\" />");
		wList.add("\t<title></title>");
		
		wList.add("</head>");
		wList.add("<body>");
		
		return  wList; 
	}
	
	private List<String> createHtmlTail(List<String> wList)
	{
		wList.add("</form>");
		wList.add("</body>");
		wList.add("</html>");
		
		return  wList; 
	}

	public void accessViaHtmlFileAndVerifyTheLoginProcess(String testFilePath,String userName,String lastName,String firstName,String className,String CanvasClassId,boolean moodleMode,boolean useMapTable) throws TimeoutException, Exception {

		report.startStep("Get institution Remaining License for all Valid Packages");
		webDriver.openUrl(testFilePath);
		
		try
		{
			
			webDriver.waitUntilElementAppearsAndReturnElementByType("btnSubmit", ByTypes.name , 10); //webDriver.waitForElement("btnSubmit", ByTypes.name);
			webDriver.waitForElement("btnSubmit", ByTypes.name).click();
			Thread.sleep(3000);
	
			skipOptin();
			
			
			webDriver.switchToMainWindow();
			webDriver.switchToTopMostFrame();
			
			NewUxHomePage homePage = new NewUxHomePage(webDriver, testResultService);			
			homePage.waitHomePageloaded();
		
			report.startStep("Validate the user First name in Home page header");
			testResultService.assertEquals("Hello " + firstName, homePage.getUserDataText(), "Verify First Name in Header of Home Page");
			
	
			
			if (!testFilePath.contains("Teacher")) {//(chkTestType.equalsIgnoreCase("ltiCustomize")) {
				
				report.startStep("Validate Data is Displayed Correctly in My Profile");
				homePage.waitHomePageloaded();
				
				homePage.clickOnMyProfile();
				homePage.switchToMyProfile();
				NewUxMyProfile myProfile = new NewUxMyProfile(webDriver, testResultService);
				
				testResultService.assertEquals(lastName, myProfile.getLastName(), "Last Name in My Profile is Incorrect.");
				testResultService.assertEquals(firstName, myProfile.getFirstName(), "First Name in My Profile is Incorrect.");
				testResultService.assertEquals(userName+"@edusoft.co.il", myProfile.getMail(), "Mail in My Profile is Incorrect.");
				
				report.startStep("Validate User Data stored in DB correctly");
				String userIdMyProfile = myProfile.getUserName();
				String userNameDB = dbService.getUserNameByUserIdFromUsersExternalMap(userIdMyProfile);
				
				String userPrefix = "";
				if (moodleMode)
					userPrefix = "Moodle-";
				
				testResultService.assertEquals(userPrefix + userName, userNameDB, "User name External Maaping is Incorrect.");
				
				report.startStep("Validate User's Class data stored in DB correctly");
				List<String[]> classDB = dbService.getClassFromClassExternalMapByUserId(userIdMyProfile);
				testResultService.assertEquals(CanvasClassId, classDB.get(0)[2], "External Class ID is Incorrect.");
				
				if(useMapTable) {
					testResultService.assertEquals(className,classDB.get(0)[1],"Class Name is Incorrect.");  // dbService.getClassFromClassExternalMapByClassId(classDB.get(0)[0]), "Class Name is Incorrect.");
				}
				else {
					testResultService.assertEquals(className, dbService.getClassNameByClassId(classDB.get(0)[0]), "Class Name is Incorrect.");
				}
				myProfile.close();
				//webDriver.closeNewTab(1);
				webDriver.switchToNewWindow();
				webDriver.getWebDriver().close();
				webDriver.switchToMainWindow();
				webDriver.switchToTopMostFrame();
				homePage.clickOnLogOut();
				
			}
		}
		catch(Exception e)
		{
		}
		
	}

	public void verifyAndChangeInstitutionToNewTE(String instId) {

		try {
			dbService.checkAndturnOnTestEnviormentFlag(instId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public List<String[]> getclassesByInstitutionName(String instName) {
		List<String[]> classes = null;
		
		try {
			classes = dbService.getClassesNameByInstitutionId(instName);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return classes;
	}
	
	public String[] getTeacherInstitution(String instId) {
		List<String[]> teachers = null;
		Random random = new Random();
		String[] selectedTeacher = null;
		String teacherName = "";
		try {
			do {
			teachers = dbService.getTeacherInstitution(instId);
			int i = random.nextInt(teachers.size());
			selectedTeacher = teachers.get(i);
			teacherName = selectedTeacher[0];
			}
			while(teacherName.equalsIgnoreCase("autoTeacher2"));
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return selectedTeacher;
	}
	
	public void SetBuildQAInCI_CMD(String action, String buildId) throws Exception {
		
		//String command = "\\\\vstf2013\\EdoTmsAssociator\\bin\\EDOTMSMatcher.Console.exe " + buildIdTMS + " " + buildId;
		//String command = "\\\\vstf2013\\EdoTmsAssociator\\bin\\EDOTMSMatcher.Console.exe " + buildIdTMS + " " + ed_BuildNumber;
		
		//String command = "\\\\davidm\\automation\\buildLocker\\buildLocker.exe " + action + " " + buildId;
		//String command = "\\\\davidm\\AutomationTools\\SeleniumServer\\SetQABuild.bat " + action + " " + buildId;
		
		
		//String command = "\\\\10.1.0.213\\automation\\buildLocker\\buildLocker.exe " + action + " " + buildId;
	String command = "\\\\"+automationServer+"\\automation\\buildLocker\\SetQABuild.bat " + action + " " + buildId;
		//String command = "\\\\10.208.43.135\\automation\\buildLocker\\SetQABuild.bat " + action + " " + buildId;
		//\\10.1.0.213\automation\buildLocker
		Process process = null;
		
		try
		{
		    process = Runtime.getRuntime().exec(command);
		    
		} catch (IOException e)
		{
		    e.printStackTrace();
		    
		}
		
		BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
		// read the output from the command
		//System.out.println("Here is the standard output of the command:\n");
		String s = null;
		while ((s = stdInput.readLine()) != null) {
			
			String a = process.getOutputStream().toString();
		    System.out.println(s);
		    System.out.println(a);
		    report.startStep(a);
		    report.startStep(s);
		}
	}

	public List<String[]> getTeacherClasses(String teacherId) {
		
		
		List<String[]> teacherClasses = null;
		
		//String sql = "EXEC GetTeacherClasses " + teacherId;
		try {
			//teacherClasses = dbService.getStringListFromQuery(sql, 1, true);
			teacherClasses = dbService.getTeacherClasses(teacherId);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				 
		
		return teacherClasses;
	}
	
	public String[] getStudentAssignedToGroup(String classId) {
		String[] student = null;
		student = dbService.getStudentFromGroup(classId);
		if(student==null) {
			return null;
		}
	return student;
	}
	
	

	public String[] getStudentsByInstitutionId(String institutionId) {
		
		String[] selectedStudent = null;
		
		selectedStudent = dbService.getRandomStudentByInstitutionId(institutionId);
		
		return selectedStudent;
	}

	
	public String[] getStudentsByInstitutionId(String institutionId,String classId) {
		
		String[] selectedStudent = null;
		
		selectedStudent = dbService.getRandomStudentByInstitutionId(institutionId,classId);
		
		return selectedStudent;
	}

	public String getCurrentUrl() throws Exception {
		return webDriver.getUrl();
		
	}
	public String getURI() throws URISyntaxException, Exception
	{
		URI myUri = new URI(getCurrentUrl());    //parse(getCurrentUrl());
		String shortURL = myUri.getHost().toString();
		return shortURL;
	}

	public void linkEdTmsQaBuildsInCIBy_CI_WebSite() throws Exception {
		
		report.startStep("Open the ED-CI associator");
		webDriver.navigate(associationWebSite);
		Thread.sleep(5000);
		report.startStep("Selecting all the fields and click associate. TMS: " +buildIdTMS+ ",ED: " + ed_BuildNumber);
		webDriver.selectValueFromComboBox("tmsBuildDefinition", "TMS_CI", ByTypes.id);			//selectTMS_build_Definition
		Thread.sleep(5000);
		webDriver.selectValueFromComboBox("tmsBuild",buildIdTMS, ByTypes.id);					//select_TMSbuild_Number  "TMS_CI_Content_20220529.1"
		Thread.sleep(5000);
		webDriver.selectValueFromComboBox("edbuildDefinition", "EDUI_CI", ByTypes.id);			//select_EDbuild_Definition
		Thread.sleep(5000);
		webDriver.selectValueFromComboBox("edBuild",ed_BuildNumber, ByTypes.id);				//select_EDbuild_Number  EDUI_CI_main_20220612.1
		Thread.sleep(5000);
		webDriver.waitForElement("//*[text() = 'Associate!']", ByTypes.xpath).click();
		
		report.startStep("Wait and retrieve from alert message");
		
		String sucsess = webDriver.getAlertText(10);
		
		if (sucsess.contains("Success!"))
			webDriver.closeAlertByAccept();
		else {
			report.reportFailure("Assiciation failed. TMS: "+buildIdTMS+" ED: " + ed_BuildNumber);
			webDriver.closeAlertByAccept();
		}
		webDriver.closeBrowser();
		/*
		WebDriverWait wait = new WebDriverWait(webDriver.getWebDriver(), 10);
		Alert alert = wait.until(ExpectedConditions.alertIsPresent()); 
		String textFromAlert = alert.getText();
		assertTrue(textFromAlert.contains("Success!"));
		alert.accept();
		*/
		//String textFromAlert = webDriver.getAlertText(10);
		/*	WebElement selectTMSbuildDefinition = webDriver.waitUntilElementAppearsAndReturnElementByType("tmsBuildDefinition", ByTypes.id, 10);
		Select select = new Select(selectTMSbuildDefinition);
		select.selectByVisibleText("TMS_CI");
		
		WebElement selectTMSbuildNumber = webDriver.waitUntilElementAppearsAndReturnElementByType("tmsBuild", ByTypes.id, 10);
		select = new Select(selectTMSbuildNumber);
		select.selectByVisibleText("TMS_CI_Content_20220529.1");
		
		WebElement selectEDbuildDefinition = webDriver.waitUntilElementAppearsAndReturnElementByType("edbuildDefinition", ByTypes.id, 10);
		select = new Select(selectEDbuildDefinition);
		select.selectByVisibleText("EDUI_CI");
		
		WebElement selectEDbuildNumber = webDriver.waitUntilElementAppearsAndReturnElementByType("edBuild", ByTypes.id, 10);
		select = new Select(selectEDbuildNumber);
		select.selectByVisibleText("EDUI_CI_main_20220612.1");
		*/
	
	}

	public void assignStudentToGroup(String classId, String userID, String groupId) throws Exception {
		String sql = "MoveUsersToGroup "+classId+",'C',0,"+userID+",null,null,"+groupId;
		//dbService.getStringFromQuery(sql);
		dbService.assignStudentToGroup(sql);
		
	}
	public boolean checkTestEnvironmentFlag(String institutionId) throws Exception {
	String date = dbService.getInstPropertyValueByProperyId(institutionId,35);
		if (date == null) {
			return false;
		} else {
			//String fulldate = flag.get(0)[2];
			int year = Integer.parseInt(date.substring(0, 4));
			int month = Integer.parseInt(date.substring(6, 7));
			int currentMonth = LocalDate.now().getMonth().getValue();
			int currentYear = LocalDate.now().getYear();
			System.out.println(year + " " + month + " " + currentMonth + " " + currentYear);
			if (year >= currentYear && month > currentMonth) {
				return false;
			}
			// DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");

		}
		return true;
	}

	public void deleteUserProgressFromTOEICbyCourseIDandUserName(String userName, String courseId) {
	
		String componentProgress = "delete cp"
				+ " from ComponentProgress as cp"
				+ " inner join UnitComponents as uc on cp.ComponentId = uc.ComponentId"
				+ " inner join Units as u on u.UnitId = uc.UnitId"
				+ " inner join Course as c on c.CourseId = u.CourseId"
				+ " inner join UserProfile as up on up.UserId = cp.UserId"
				+ " where c.CourseId = "+courseId+" and up.UserName = '"+userName+"'";
		
		String unitProgress = "delete up"
				+ " from UnitProgress as up"
				+ " inner join units as u on u.unitid = up.UnitId"
				+ " inner join Course as c on c.CourseId = u.CourseId"
				+ " inner join UserProfile as upro on upro.UserId = up.UserId"
				+ " where c.CourseId = "+courseId+" and upro.UserName = '"+userName+"'";
		
		String courseProgress = "delete crp"
				+ " from CourseProgress as crp"
				+ " inner join UserProfile as upro on upro.UserId = crp.UserId"
				+ " where crp.CourseId = "+courseId+" and upro.UserName = '"+userName+"'";
		
		String[] progress = {componentProgress, unitProgress, courseProgress,};
		try {
		
		for(String s:progress) {
			dbService.deleteUserProgressFromTOEIC(s);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			dbService.usingEDDB = true;
			dbService.setUseToeicOlpc(false);
		}
	}

	public List<String[]> getUserProgressByUserNameAndCourseIdFromTOEIC(String userName, String courseId) {
		String sql = "SELECT UserProfile.UserId, UserProfile.UserName,"
				+ " CourseProgress.CourseId,CourseProgress.Percentage as CourseCompletion"
				+ " ,CourseProgress.TimeInvestedMinutes as CourseInvestedTime, CourseProgress.SessionMinutes as CourseSessionMinutes"
				+ " ,UnitProgress.Percentage AS UnitComletion, ComponentProgress.Percentage AS ComponentCompletion"
				+ " ,ComponentProgress.Grade as CompopnentGrade, ComponentProgress.TimeInvestedMinutes AS ComponentInvestedTime"
				+ " ,UnitProgress.UnitId,ComponentProgress.ComponentId"
				+ " FROM CourseProgress"
				+ " INNER JOIN UnitProgress ON CourseProgress.UserId = UnitProgress.UserId"
				+ " INNER JOIN ComponentProgress ON CourseProgress.UserId = ComponentProgress.UserId"
				+ " INNER JOIN UserProfile ON CourseProgress.UserId = UserProfile.UserId"
				+ " WHERE UserProfile.UserName = '"+userName+"' and CourseProgress.CourseId = "+courseId
				;
		
		return dbService.getUserProgressFromTOEIC(sql);
		
	}
	
	public String readFile(String fileInMainRes)
            throws IOException, URISyntaxException {

          int ch;
          //JOptionPane.showMessageDialog(null, "Action failed 1");
          //URL url = this.getClass().getResource("/deleteStudentProgress.txt");
          InputStream is = getClass().getResourceAsStream(fileInMainRes);

          //JOptionPane.showMessageDialog(null, "Action failed 2" + url.toString());
          StringBuilder sb = new StringBuilder();

          while((ch = is.read()) != -1) {
              sb.append((char)ch);

          }
          return sb.toString();
  }

	public String readFromTXTfile(String path) {
		StringBuilder sb = new StringBuilder();
		try {
			File file = new File(path);
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line = "";
			while(line!=null) {
				line = br.readLine();
				if(line!=null) {
					if(!line.equals("")) {
						if(!line.contains("--"))
						sb.append(line);
					}
				}
			}
			br.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return  sb.toString();
		
	}
	
	
	
	public void setEnglishLanguageInMyProfile() throws Exception {
		String start = "div[class='BSslide item text-center ng-isolate-scope active'] a[class='layout__btnAction ng-binding']";
		webDriver.waitForElement(start, ByTypes.xpath, true, 10);
		//NewUxHomePage.checkTextAndclickOnMyProfile(2);
		
	}

	public void resetStudentCourseProgressInTOEIC(String userName, String toeicCourseId, String sql) {
		dbService.resetStudentProgressInTOEIC(userName, toeicCourseId,sql);
		
	
	}

	public String[] getStudentWithProgressFinalGrade(String institutionId) throws Exception {
		
		String sql = "select UserId from MatrixFinalGrades where FinalGrade > 48 and InstitutionMatrixId = "+institutionId+" and CourseId = 43396";
		
		List<String[]> student = dbService.getStringListFromQuery(sql, 0, true);
		String[]id = student.get(new Random().nextInt(student.size()));
		student = dbService.getUserNameAndPasswordByUserId(id[0]);
		
		return student.get(0);
	}

	public String getClassNameOfParticularUser(String studentId) throws Exception {
		
		String sql = " select Class.Name from class INNER JOIN ClassUsers "
				+ "ON Class.ClassId = ClassUsers.ClassId where ClassUsers.UserId = "+studentId+"";
		String className = dbService.getStringFromQuery(sql);
		return className;
	}

	public double getNumberOfLessonsInCourse(List<String[]> courseUnits) throws Exception {
	
		int totalLessons = 0;
		for (int j = 0; j < courseUnits.size(); j++) {
			totalLessons = totalLessons + dbService.getComponentDetailsByUnitId(courseUnits.get(j)[0]).size();
		}
		return totalLessons;
		
	}

	public String getInternalClassId(String className, String institutionId) throws Exception {
		String sql = "select * from class where institutionid  = "+institutionId+" and Name  = '"+className+"'";
		String classId = dbService.getStringFromQuery(sql);
		
		return classId;
	}
	
public static String getCIBrnachName() {
		
		String branchName = PageHelperService.branchCI;
		
		if (branchName.contains(".com")){
			
			directLink = true;

			if (branchName.toLowerCase().contains("edui-ci-main")){
				branchName = "EDUI_CI_main";
			}
			if (branchName.toLowerCase().contains("edui-ci-dev")){
				branchName = "EDUI_CI_dev";
			}
			if (branchName.toLowerCase().contains("ci-rc-2023-12")){
				branchName = "EDUI_CI_RC_2023_12";
			}
			if (branchName.toLowerCase().contains("ci-rc-2023-11")){
				branchName = "EDUI_CI_RC_2023_11";
			}
		}
		
		return branchName;
	}

	public String[] getMatrixStudentsWithProgress(String instId) throws Exception {
		
		String sql = "select u.UserId,u.UserName,u.Password,c.Name,c.ClassId\r\n"
				+ " from InstitutionMatrix as im\r\n"
				+ " inner join MatrixFinalGrades as mfg on im.InstitutionMatrixId = mfg.InstitutionMatrixId and im.institutionid = "+instId+"\r\n"
				+ " inner join users as u on u.UserId = mfg.UserId\r\n"
				+ " inner join ClassUsers as cu on cu.UserId = u.UserId\r\n"
				+ " inner join class as c on c.ClassId = cu.ClassId\r\n"
				+ " where im.InstitutionMatrixName like 'default%'\r\n"
				+ " and mfg.FinalGrade > 20\r\n"
				+ " and u.Visible = 1";
		
		List<String[]> student = dbService.getStringListFromQuery(sql,0,true);
		return student.get(0);
		//return student.get(new Random().nextInt(student.size()-1));
	}

	public List<String[]> getExternalTeachersWithBigAmountOfClasses(int i) throws Exception {
		
		String sql = "SELECT  UserPermissions.UserId, count(UserPermissions.UserId) as amountOfClases FROM UserPermissions INNER JOIN	\r\n"
				+ "	TeacherClasses ON UserPermissions.UserPermissionsId = TeacherClasses.UserPermissionsId\r\n"
				+ "	INNER JOIN Class ON TeacherClasses.ClassId = Class.ClassId\r\n"
				+ " inner join ClassExternalMap as cem on class.ClassId = cem.ClassId"
				+ " group by UserPermissions.UserId having count(UserPermissions.UserId)>"+i+"\r\n";
	
		List<String[]> teachers = dbService.getStringListFromQuery(sql, 0, true);
	
		return teachers;
	}

	public List<String[]> getTeachersOfCertainInstitutionWithBigAmountOfClasses(int i, String institutionName) throws Exception {
		
		String institutionId = dbService.getInstituteIdByName(institutionName);
		
		String sql = "select UserPermissions.UserId,count(UserPermissions.UserId) from UserPermissions INNER JOIN\r\n"
				+ " TeacherClasses ON UserPermissions.UserPermissionsId = TeacherClasses.UserPermissionsId INNER JOIN Users as u on UserPermissions.UserId = u.Userid\r\n"
				+ "	where u.institutionid = "+institutionId+" group by UserPermissions.UserId having count(UserPermissions.UserId)>"+i;
		List<String[]> teachers = dbService.getStringListFromQuery(sql, 0, true);
		return teachers;
	}

	public List<String[]> getLastVisitedItem(String userId) throws Exception {
		
		String sql = "select * from userlastvisiteditem as ulvi inner join item as i on ulvi.ItemId = i.ItemId where userid = "+userId;
		List<String[]> item = dbService.getStringListFromQuery(sql, 0, true);
		return item;
	}

	public String[] getStudentScoresAndCertificateURL(String testTakerID) throws Exception {
		String sql = "select tr.ScoreListening,tr.ScoreReading,tt.FirstName,tt.LastName,tr.Certificate_URL_Identifier, tr.Certificate_Id from TestResults as tr "
				+ "inner join TestTakers as tt on tr.TestTakerId=tt.TestTakerId where tr.TestTakerId="+testTakerID;
				
			return dbService.getToeicScoresAndCertificate(sql);
	}
	
	public void checkUserAssignedToclass(String studentId, String className, String institutionId) throws Exception {
		
		testResultService.assertEquals(true,
				isUserAssignedToClass(studentId, dbService.getClassIdByName(className, institutionId)),
				"Class not created or user not assigned to class");
	}
	
	public void checkTeacherAssignedToclass(String studentId, String className, String institutionId) throws Exception {
		
		testResultService.assertEquals(true,
				isTeacherAssignedToClass(studentId, dbService.getClassIdByName(className, institutionId)),
				"Class not created or user not assigned to class");
	}
	
	public String[] getStudentCompletedUnitProgress(String institutionId) throws Exception{
		
		List<String[]> students = dbService.getStudentsCompletedUnit(institutionId);
		
		Random random = new Random();
		int i = random.nextInt(students.size());
		return students.get(i);
	}
	
	public String[] getStudentCompletedCourseProgress(String institutionId) throws Exception{
		
		List<String[]> students = dbService.getStudentsCompletedCourse(institutionId);
		
		Random random = new Random();
		int i = random.nextInt(students.size());
		return students.get(i);
	}

	public int calculateTimeOnTaskFromDB(int timeInSeconds) {
		int amountOfMInutes = timeInSeconds/60;
		int surrenderTime = timeInSeconds%60;
		if(surrenderTime>0) {
			amountOfMInutes++;
		}
		return amountOfMInutes;
	}

	public void restartBrowserSameUrl() throws Exception {
		
		String url = webDriver.getUrl();
		restartBrowser();
		webDriver.openUrl(url);
		Thread.sleep(1000);
	}
	
	public void restartWebServiceApplicationPool() {
		try {
			//runConsoleProgram(PageHelperService.batFilesRestartPoolPath + "StopAppPoolRemotely.bat");//files/RestartAppPool/PSrestartEdUIWebServices.bat
			runConsoleProgram(PageHelperService.batFilesRestartPoolPath[0]);
			Thread.sleep(4);
			runConsoleProgram(PageHelperService.batFilesRestartPoolPath[1]);
			Thread.sleep(4);
		//	runConsoleProgram(PageHelperService.batFilesRestartPoolPath + "StartAppPoolRemotely.bat");
		//	Thread.sleep(4);
		
		} catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	public void setSessionExpirationTime(String time) {
		
		textService.updateJsonFileByKeyValue(PageHelperService.edUiServicePath,"appsettings","Jwt","ExpirationInMinutes",time);
		
	}

	public void changeKeyValueInLocalStorage(String key, String value){
		try {
			runJavaScriptCommand("localStorage.setItem('"+key+"','"+value+"')");
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	public void clearLocalStorage(){
		try {
			runJavaScriptCommand("localStorage.clear()");
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	public String getPathToJustDownloadedFile() throws Exception {

		((JavascriptExecutor) webDriver.getWebDriver()).executeScript("window.open()");
		String currWindow = webDriver.switchToNewWindow(2);
		webDriver.openUrl("chrome://downloads");
		JavascriptExecutor js = (JavascriptExecutor) webDriver.getWebDriver();
		Thread.sleep(2000);
		WebElement downloadedCSV = (WebElement) js.executeScript("return document.querySelector('downloads-manager').shadowRoot.querySelector('#mainContainer > iron-list > downloads-item').shadowRoot.querySelector('#show')");
		String pathToDownloadedFile = downloadedCSV.getAttribute("title");
		webDriver.closeTab(2);
		webDriver.switchToMainWindow(currWindow);

		return pathToDownloadedFile;
	}

	/*public void checkRemainingLicenseBurnForallValidPackages(List<String[]> getValidPackagesForInstitution,String institutionId,boolean equal) throws Exception {

		List<String[]> currentRemaingLisence = dbService.getValidInstitutionPackages(institutionId);

		int reduceRemainLicense=0;

		if (!equal)
			reduceRemainLicense=1;

		int originalRemain=0;
		int actualRemain=0;

		for (int i=0;i< getValidPackagesForInstitution.size();i++){
			originalRemain = Integer.parseInt(getValidPackagesForInstitution.get(i)[4]);
			actualRemain = Integer.parseInt(currentRemaingLisence.get(i)[4]);

			testResultService.assertEquals((originalRemain-reduceRemainLicense),actualRemain,
					"InstitutionPackage: " +currentRemaingLisence.get(i)[0]+ " not updated");
		}

	}*/

	public void validateLicensesReducesAfterNewStudentAdded(List<String[]> remainingLicensesBeforeAddingUser, List<String[]> remainingLicensesAfterAddingUser) throws Exception {

		for (int i=0;i<remainingLicensesBeforeAddingUser.size();i++) {
			report.startStep("Verifying licence burned to packageId : "+remainingLicensesBeforeAddingUser.get(i)[3]);

			int rem = Integer.parseInt(remainingLicensesBeforeAddingUser.get(i)[4])-Integer.parseInt(remainingLicensesAfterAddingUser.get(i)[4]);
			testResultService.assertEquals(1, rem, "License remaning didn't reduced or burned more than should");

			int used = Integer.parseInt(remainingLicensesBeforeAddingUser.get(i)[5])+1;
			testResultService.assertEquals(Integer.parseInt(remainingLicensesAfterAddingUser.get(i)[5]), used, "License Used didn't burned or burned more than should");
		}
	}

	public void validateLicensesAfterUserAddedOrLoggedIn(List<String[]> baseLineLicenses,List<String[]> currentLicense,int expectedGapRem,int expectedGapUsed) throws Exception {

		for (int i=0;i<baseLineLicenses.size();i++) {
			report.startStep("Verifying licence burned to packageId : "+baseLineLicenses.get(i)[3]);

			int actualRem = Integer.parseInt(baseLineLicenses.get(i)[4])-Integer.parseInt(currentLicense.get(i)[4]);
			testResultService.assertEquals(expectedGapRem, actualRem, "License remaning didn't reduced or burned more than should");

			int actualUsed = Integer.parseInt(currentLicense.get(i)[5])-Integer.parseInt(baseLineLicenses.get(i)[5]);
			testResultService.assertEquals(expectedGapUsed,actualUsed, "License Used didn't burned or burned more than should");
		}
	}

	public void checkUserPackageProgressLicenseBurnForValidPackages(String institutionId,String studentId) throws Exception {

		List<String[]> validInstitutionPackages = dbService.getValidInstitutionPackages(institutionId);
		List<String[]> studentPackageProgress = dbService.getUserPackagesProgress(studentId);

		testResultService.assertEquals(validInstitutionPackages.size(),studentPackageProgress.size(),
				"Count of Institution Packages and User Progress Packages not equal");

		for (int i=0;i< validInstitutionPackages.size();i++){

			testResultService.assertEquals(validInstitutionPackages.get(i)[0],studentPackageProgress.get(i)[0],
					"InstitutionPackage: " +validInstitutionPackages.get(i)[0]+ " not correct or exists in UserPackageProgress");
		}

	}

	/*public void verifyLicencesBurnedAfterLogin(List<String[]> getValidInstitutionPackages ,List<String[]> classPackages){
		try {
			if (newStudent) {
				//report.startStep("Check institution Remaining license after login");
				if (useNewClass) {
					pageHelper.checkRemainingLicenseBurnForallValidPackages(getValidInstitutionPackages, institutionId, false);
				} else {
					if (classPackages!=null) {
						List<String[]> remainingLicensesAfterLogin = dbService.getRemainingLicensesOfCertainPackage(classPackages, institutionId);
						pageHelper.validateLicensesReducesAfterNewStudentAdded(getValidInstitutionPackages, remainingLicensesAfterLogin);
					}
				}
			} else {
				pageHelper.checkRemainingLicenseBurnForallValidPackages(getValidInstitutionPackages, institutionId, true);
			}
		}catch (Exception|AssertionError err){
			System.out.println(err.getMessage());
		}


	}*/

	public void checkRemaingLicense(List<String[]> getValidInstitutionPackages, String className, String institutionId,boolean newStudent,boolean useNewClass) throws Exception {

		if (newStudent){
			report.startStep("Check institution Remaining License reduced after login");
			if (getValidInstitutionPackages!=null)
				checkRemainingLicenseBurnForallValidPackages(getValidInstitutionPackages,institutionId,className,false);
		}
		else
			checkRemainingLicenseBurnForallValidPackages(getValidInstitutionPackages,institutionId,className,useNewClass);

	}

	public void checkRemainingLicenseBurnForallValidPackages(List<String[]> getValidPackagesForInstitution,String institutionId,String className,boolean reduceLicence) throws Exception {
		if (className.contains(";")) {
			className = className.replace(";", "','");
		}
		List<String[]> currentInstitutionPackages = dbService.getValidInstitutionPackages(institutionId);
		List<String[]> currentClassPackage = dbService.getClassAssignPackagesNew(className, institutionId);

		int expectedReductionOfLicences = 0;
		if (reduceLicence)
			expectedReductionOfLicences = 1;

		int originalRemain = 0;
		int actualRemain = 0;
		int originalUsed = 0;
		int actualUsed = 0;

		if (currentClassPackage!=null) {
			for (int i = 0; i < currentClassPackage.size(); i++) {// class count

				for (int p = 0; p < currentInstitutionPackages.size(); p++) {// inst size

					if (Integer.parseInt(getValidPackagesForInstitution.get(p)[0])
							== Integer.parseInt(currentClassPackage.get(i)[0])) {

						// Check remaining license
						originalRemain = Integer.parseInt(getValidPackagesForInstitution.get(p)[4]);
						actualRemain = Integer.parseInt(currentInstitutionPackages.get(p)[4]);
						testResultService.assertEquals(expectedReductionOfLicences, originalRemain - actualRemain,
								"InstitutionPackageId: " + getValidPackagesForInstitution.get(p)[0]
										+ " Remaining License should be: " + (originalRemain-expectedReductionOfLicences)
										+ " but actual is: " + (actualRemain));

						// Check Used license
						originalUsed = Integer.parseInt(getValidPackagesForInstitution.get(p)[5]);
						actualUsed = Integer.parseInt(currentInstitutionPackages.get(p)[5]);
						testResultService.assertEquals(expectedReductionOfLicences, actualUsed - originalUsed,
								"InstitutionPackageId: " + getValidPackagesForInstitution.get(p)[0]+
										" Used License should be: " + (originalUsed+expectedReductionOfLicences)
										+ " but actual is: " + (actualUsed));

						break;
					}
				}
			}
		}else {
			System.out.println("<==================!!! Class "+className+" HAS NO PACKAGES !!!====================>");
			testResultService.addFailTest("Class without packages",false,false);
		}
	}






}
