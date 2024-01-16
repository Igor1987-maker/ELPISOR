package tests.edo.newux;

//import java.io.IOException;

import Enums.ByTypes;
import Enums.CourseCodes;
import Enums.CourseTests;
import Enums.UserType;
import Interfaces.TestCaseParams;
import Objects.CourseTest;
import Objects.PLTTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.WebElement;
import pageObjects.edo.*;
import pageObjects.tms.TmsHomePage;
import services.PageHelperService;
import testCategories.AngularLearningArea;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Category(AngularLearningArea.class)
public class ArgentinaAdaptiveLearning extends BasicNewUxTest {

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
	
	private static String CanvasClassName = "DefaultCanvasClass";
	private static String CanvasClassId = "DefaultCanvasClass_Id";

	private static int amountClasses = 0;

	String userFN = "FN";
	String userLN = "LN";
	String role = "Learner";
	String password = "";
	boolean useNameUserWithMinus = false;
	boolean multipleClasses = false;
	int classAssignmnetAmount = 1;
	public static boolean useMapTable = false;
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
	
	String firstNameKey = "lis_person_name_given";
	String lastNameKey = "lis_person_name_family";
	String userNameKey = "custom_canvas_user_id";
	String mailKey = "lis_person_contact_email_primary";
	String classNameKey = "custom_user_sections";
	boolean useSMB = false;
	@Before
	public void setup() throws Exception {
		institutionName = institutionsName[2];
		super.setup();

		loginPage = new NewUXLoginPage(webDriver, testResultService);
		
	}
/*
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
*/
		
/*
	private void createExtLoginPostFile(String password,String instToken,String testFileName) throws Exception {
		String baseUrl = pageHelper.CILink.split(".com")[0] + ".com/";
		String accessUrl = pageHelper.urlForExternalPages.split(".com")[0] + ".com/languages/";
		String chkPath = pageHelper.buildPathForExternalPages + "Languages/";
		List<String> wList = new ArrayList<String>();
		
		wList = createHtmlTitle(wList);
		wList = createHTMLformExtLoginByPOST(wList,baseUrl,password,instToken,testFileName);
		wList =  createHtmlTail(wList);


		textService.writeListToSmbFile(chkPath + testFileName, wList, netService.getDomainAuth());
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
			dbService.insertUserToAutomationTable(institutionId, studentId, userName, className,
			pageHelper.buildId);
			}
			else {
			testResultService.addFailTest("User not created", true, true);
			}
		}
		regUserUrl = openURL;
		
	}
*/

	/*
	private List<String> createHTMLformExtLoginByPOST(List<String> wList, String baseUrl,String password, String instToken, String testFileName) {
		
		wList.add("\t<form method=\"post\" action=\"" + baseUrl + "ExtLogin.aspx\"/>");
		wList.add("\t\t<input type=\"submit\" name=\"btnSubmit\" value=\"Form Submit\" />");
		wList.add("\t\t<input type=\"hidden\" name=\"UserName\" value=\"" + userName + "\" />");
		wList.add("\t\t<input type=\"hidden\" name=\"Password\" value=\"" + password + "\" />");
		wList.add("\t\t<input type=\"hidden\" name=\"link\" value=\"" + CorpUrl + "\" />");	
		wList.add("\t\t<input type=\"hidden\" name=\"IName\" value=\"" + institutionName + "\" />");
		wList.add("\t\t<input type=\"hidden\" name=\"Token\" value=\"" + instToken + "\" />");
		
		wList.add("\t</form>");
		
		return wList;
	}
*/
/*	
	private void verifyUserLoggedInCorrectlyAndHomePagedisplay() throws Exception {
		
		
		report.startStep("Check home page loaded with the correct assignment");
		homePage.skipNotificationWindow();
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
*/
/*	
	private void verifyTeacherLogedInToTms() throws Exception {

		report.startStep("Check User Name on Home Page after Login");
		tmsHome = new TmsHomePage(webDriver, testResultService);
		homePage.skipNotificationWindow();
		webDriver.switchToTopMostFrame();
		tmsHome.switchToMainFrame();
		tmsHome.checkUserDetails(userFN, userLN);
		tmsHome.clickOnRegistration();
		sleep(1);
		tmsHome.clickOnClasses();

		report.startStep("Check teacher assigned to class");
		// get Teacher classes from DB
		List<String[]> teacherClasses = pageHelper.getTeacherClasses(studentId);
		
		if (!useAssignedClassToUser) {
			assertEquals(amountClasses + 1, teacherClasses.size());
		}
		for (int i = 0; i < teacherClasses.size(); i++) {
			System.out.println(teacherClasses.get(i)[0].toString());
		}
	}
*/
	
	public void storeAmountOfTeacherClases(UserType userType) throws Exception {
		List<String[]> teacherClassesBefore = null;
		if ((!newTeacher) && userType.name().equals("Teacher")) {
			if (studentId != null) {
				// studentId = dbService.getUserIdByUserName(userName, institutionId);
				 if (useMapTable) {
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
		
		if (userType.name().equals("Student")) {
			userTypeParam = 'S';
		}
		if (useMapTable) {
			useNameMapping = 'Y';
			useClassMaping ='Y';
			}
		try {

			String regInsertUrl = baseUrl + "RegUserAndLogin.aspx?Action=I&UserName=" + userName + "" + "&Inst="
					+ institutionName + "&FirstName=" + userFN + "&LastName=" + userLN + "" + "&Password="+password+"&Email="
					+ email + "&Class=" + className + "" + "&Language=" + Language + "&Link=" + CorpUrl
					+ "&UseNameMapping=" + useNameMapping + "&CreateClass=" + createClass + "" + "&UserType="
					+ userTypeParam+ "&UseClassMapping="+useClassMaping;

			webDriver.openUrl(regInsertUrl);
			//if(newStudent || newTeacher ||useNewClass) {
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
				// className2 = "Class-" + dbService.sig(4);
				// className = className1 + ";" + className2;
				className = sb.toString();

			}

			else {
				// if it's not new teacher and not new class than we need to exclude adding the
				// same classes
				if ((!newTeacher) && typeOfUser.equals("Teacher")) {
					if (useMapTable) {
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
					className = "Class-" + dbService.sig(5);

				} else {
					createClass = 'Y';
					className = "Class-" + dbService.sig(5);
				}
			} else {
				// if it's not multiple classes and assign current class
				List<String[]> allClassesOfInstitution;
				if (useMapTable) {
					allClassesOfInstitution = dbService.getExternalClassesNameByInstitutionId(institutionId);
					if (!newStudent && !newTeacher)
						classes = dbService.getExternalTeacherClasses(studentId);

				} else {
					allClassesOfInstitution = pageHelper.getclassesByInstitutionName(institutionName);
					classes = pageHelper.getTeacherClasses(studentId);					
				}

				if (newStudent && typeOfUser.equals("Student")) {
					className = getClassFromClassesList(allClassesOfInstitution);
					if (useMapTable) {
						CanvasClassId = dbService.getExternalClassIdByExternalClassName(className, institutionId);
					//	CanvasClassId = dbService.getClassIdByClassName(internalClassName, institutionId);
						CanvasClassName = className;
					}

				}
				if (!newStudent && typeOfUser.equals("Student")) {
					className = getClassFromClassesList(allClassesOfInstitution);
					if (useMapTable) {
						CanvasClassId = dbService.getClassIdByClassName(internalClassName, institutionId);
						CanvasClassName = className;
					}
				}

				/*
				 * if (!newStudent && typeOfUser.equals("Student")) { if (classes != null) { for
				 * (int i = 0; i < allClassesOfInstitution.size(); i++) {
				 * if(!classes.get(0)[0].equalsIgnoreCase(allClassesOfInstitution.get(i)[0])){
				 * className = allClassesOfInstitution.get(i)[0]; break; } if(useMapTable) {
				 * CanvasClassId = classes.get(0)[2]; } } }
				 * 
				 * }
				 */

				// className = getClassFromClassesList(allClassesOfInstitution);
				// return createClass;

				/*
				 * boolean classNotAssigned = true; // classes =
				 * pageHelper.getTeacherClasses(studentId); for (int i = 0; i <
				 * allClassesOfInstitution.size(); i++) {
				 * 
				 * for (int j = 0; j < classes.size(); j++) { if
				 * (allClassesOfInstitution.get(i)[0].equalsIgnoreCase(classes.get(j)[0])) {
				 * classNotAssigned = false; } } if (classNotAssigned) { className =
				 * allClassesOfInstitution.get(i)[0]; break; } } }
				 */

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
							if(useMapTable) {
								if (allClassesOfInstitution.get(ran)[2].equalsIgnoreCase(classes.get(j)[0])) {
									classNotAssigned = false;
								}
							}else {
								if (allClassesOfInstitution.get(ran)[0].equalsIgnoreCase(classes.get(j)[0])) {
									classNotAssigned = false;
							}
						}		
					}
						if (classNotAssigned) {
							
							if(useMapTable) {
								className = allClassesOfInstitution.get(ran)[2];
								CanvasClassName = className;
								CanvasClassId = dbService.getExternalClassIdByExternalClassName(className, institutionId);
							//	CanvasClassId = dbService.getClassIdByClassName(className, institutionId);
							}else {
								className = allClassesOfInstitution.get(ran)[0];
								CanvasClassName = className;
								CanvasClassId = dbService.getClassIdByName(className, institutionId);
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

			if (className.equalsIgnoreCase(teacherClasses.get(i)[0]))
				found = true;
		}

		try {
			testResultService.assertEquals(true, found, "Class not assigned to Teacher");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	@TestCaseParams(testCaseID = { "43954", "50342" }, testTimeOut = "30")
	public void test03_ArgentinaAdaptiveFlowGetPltA3AndFullAssignment() throws Exception {

		try {
			newStudent=true;
			report.startStep("Init Test Data");
			String testFile = "ArgentinaPostCanvasFile";
			int startCourseIndex = 9; // "A3";
			String PltResult = coursesNames[startCourseIndex];		
			courseUnits = dbService.getCourseUnits(coursesOld[startCourseIndex]);
			className = configuration.getProperty("arg_className");

			report.startStep("Generate student username for registration");
			externalUserName = "stud" + dbService.sig(4);
			userName=externalUserName;

			// Start of User Story 43738:Siglo21: Set Starting Course by PLT results
			report.startStep("Enter ED via CanvasPOST - Insert New User");
			pageHelper.closeBrowserAndOpenInCognito(false);
			gotoCanvasPOST(className, userName, testFile);

			studentId = getEdUserId(externalUserName);

			
			report.startStep("Check Home Page message on enter");
			homePage.verifyHomePageMessageStartCourseByPLT();

			report.startStep("Switch to opened window");
			classicTest = new NewUxClassicTestPage(webDriver, testResultService);
			sleep(2);
			webDriver.switchToNewWindow();
			report.startStep("Switch to test frame");
			classicTest.switchToTestAreaIframe();
			classicTest.verifyTitlePLT();
			//sleep(2);

		
			// to get Advanced 3 change the boolean to false
			report.startStep("Perform PLT test - skip Cycle 2 - get I3 placement");
			PLTTest pltTest = classicTest.initPLTTestFromCSVFile("files/PLTTestData/PLT_ED_Answers_NEW.csv", false);
			classicTest.performTest(pltTest);
			
			sleep(1);
			report.startStep("Verify expected placement level on test end");
			classicTest.verifyPlacementLevelOnResultPagePLT(PltResult);

			report.startStep("Verify 'Do Test Again' btn not displayed");
			classicTest.verifyDoTestAgainNotDisplayedInPLT();

			report.startStep("Exit PLT");
			classicTest.clickOnExitPLT();
			sleep(2);

			report.startStep("Press on Start PLT again");
			pageHelper.skipOnBoardingTour();
			testsPage = homePage.openAssessmentsPage(false);
			//classicTest = testsPage.clickOnStartTest("1", "1");
			
			report.startStep("Verify Placement Test NOT displayed in Available section");
			testsPage.checkPLTNotDisplayedInSectionByTestName(false);

/*
			report.startStep("Verify and close browser alert");
			String alertText = webDriver.getAlertText(3);
			webDriver.closeAlertByAccept();
			testResultService.assertEquals(Cannot_Take_PLT_Alert, alertText, "Checking 'Cannot take PLT' Alert");
*/
			report.startStep("Verify PLT not opened again");
			webDriver.checkNumberOfTabsOpened(1);

			report.startStep("Check Tests Results section");
			webDriver.refresh();
			homePage.waitHomePageloaded();
			testsPage = homePage.openAssessmentsPage(false);
			testsPage.clickOnArrowToOpenSection("3");
			sleep(1);
			testsPage.checkSubmissionDateForTests("1");
			testsPage.checkScoreLevelPLT("1", PltResult);

			report.startStep("Close Assessments page");
			testsPage.close();

			/*
			 * ////// this step should be removed after implementation of US !!!
			 * report.startStep("Assign I3 to class");
			 * pageHelper.UnlockCourseToClass(argInstId, classNameAR, courses, new String[]
			 * {courses[startCourseIndex]}); webDriver.refresh(); sleep(1); //////
			 */

			
			report.startStep("Check all 10 courses assigned on Home Page");
			
			/*
			homePage.checkCourseCarouselArrowsNotDisplayed();
			testResultService.assertEquals(startCourseName, homePage.getCurrentCourseName(),
					"Starting course on Home Page is not valid or not found."
					+ " Make sure ilp.json mapping for PLT results (LevelMapping section) is either configured correctly or deafault value.");
			 */
			
			homePage.verifyAllCoursesAssignedAndOpenOnHomePage();
			
			dbService.checkTheUserInsertedToLtiGrades(studentId,"99");
			
			
			// End of User Story 43738:Siglo21: Set Starting Course by PLT results	
			
			} finally {
				report.startStep("In case of test failure, don't forget to verify ilp.json file wasn't modified");
				report.report("ilp.json path: "+PageHelperService.sharePhisicalFolder+"Institutions\\" + institutionId);
			}
		}
	
	@Test
	@TestCaseParams(testCaseID = { "43954", "50342" }, testTimeOut = "30")
	public void test01_ArgentinaAdaptiveFlowGetCourseAssignByPlt() throws Exception {

		try {
			newStudent=true;
			useMapTable=true;
			report.startStep("Init Test Data");
			String testFile = "01ArgentinaPostCanvasFile";
			int startCourseIndex = 6; // "I3";
			String startCourseName = coursesNames[startCourseIndex];
			courseUnits = dbService.getCourseUnits(coursesOld[startCourseIndex]);
			className = configuration.getProperty("arg_className");
			
			report.startStep("Generate student username for registration");
			externalUserName = "stud" + dbService.sig(4);
			userName = externalUserName;
			// Start of User Story 43738:Siglo21: Set Starting Course by PLT results

			report.startStep("Enter ED via CanvasPOST - Insert New User");
			pageHelper.closeBrowserAndOpenInCognito(false);
			gotoCanvasPOST(className, userName, testFile);
			homePage.waitHomePageloaded();
			
			report.startStep("Check Home Page message on enter");
			homePage.verifyHomePageMessageStartCourseByPLT();

			report.startStep("Switch to opened window");
			classicTest = new NewUxClassicTestPage(webDriver, testResultService);
			sleep(2);
			webDriver.switchToNewWindow();
			report.startStep("Switch to test frame");
			classicTest.switchToTestAreaIframe();
			classicTest.verifyTitlePLT();
			sleep(2);

		
			// to get Advanced 3 change the boolean to false
			report.startStep("Perform PLT test - skip Cycle 2 - get I3 placement");
			PLTTest pltTest = classicTest.initPLTTestFromCSVFile("files/PLTTestData/PLT_ED_Answers_NEW.csv", true);
			classicTest.performTest(pltTest);
			
			sleep(1);
			report.startStep("Verify expected placement level on test end");
			classicTest.verifyPlacementLevelOnResultPagePLT(startCourseName);

			report.startStep("Verify 'Do Test Again' btn not displayed");
			classicTest.verifyDoTestAgainNotDisplayedInPLT();

			report.startStep("Exit PLT");
			classicTest.clickOnExitPLT();
			sleep(2);

			report.startStep("Press on Start PLT again");
			pageHelper.skipOnBoardingTour();
			testsPage = homePage.openAssessmentsPage(false);
			//classicTest = testsPage.clickOnStartTest("1", "1");
			
			report.startStep("Verify Placement Test NOT displayed in Available section");
			testsPage.checkPLTNotDisplayedInSectionByTestName(false);

	/*		report.startStep("Verify and close browser alert");
			String alertText = webDriver.getAlertText(3);
			webDriver.closeAlertByAccept();
			testResultService.assertEquals(Cannot_Take_PLT_Alert, alertText, "Checking 'Cannot take PLT' Alert");
	 */
			report.startStep("Verify PLT not opened again");
			webDriver.checkNumberOfTabsOpened(1);

			report.startStep("Check Tests Results section");
			webDriver.refresh();
			homePage.waitHomePageloaded();
			testsPage = homePage.openAssessmentsPage(false);
			testsPage.clickOnArrowToOpenSection("3");
			sleep(1);
			testsPage.checkSubmissionDateForTests("1");
			testsPage.checkScoreLevelPLT("1", startCourseName);

			report.startStep("Close Assessments page");
			testsPage.close();

			report.startStep("Check ONLY starting course available on Home Page");
			homePage.checkCourseCarouselArrowsNotDisplayed();
			testResultService.assertEquals(startCourseName, homePage.getCurrentCourseName(),
					"Starting course on Home Page is not valid or not found. Make sure ilp.json mapping for PLT results (LevelMapping section) is either configured correctly or deafault value.");
			} finally {
				report.startStep("In case of test failure, don't forget to verify ilp.json file wasn't modified");
				report.report("ilp.json path: "+PageHelperService.sharePhisicalFolder+"Institutions\\" + institutionId);
			}
		}
	
	@Test
	@TestCaseParams(testCaseID = { "43954", "50342" }, testTimeOut = "30")
	public void test02_ArgentinaGetCTByProgressAndNextCourse() throws Exception {

		try {
			useMapTable=true;
			report.startStep("Init Test Data and Get User From DB");
			String[] user = dbService.getStudentAfterPltAndNoProgressForAuthomaticLearningPath(institutionId);
			studentId = user[0];
			userName = user[1];
			externalUserName = user[1];
			userFN = user[2];
			userLN = user[3];
			email = user[5];
			className = user[6];
			CanvasClassId = user[7];
			
			String testFile = "02ArgentinaPostCanvasFile" + dbService.sig(4);
			
			int startCourseIndex = 6; // "I3";
			String courseId = coursesOld[startCourseIndex];
			courseUnits = dbService.getCourseUnits(coursesOld[startCourseIndex]);
			String courseTestName = "Intermediate 3 Final Test";
			int courseProgressPassingGrade = 3;
			int testAvgScorePassingGrade = 100;
			int courseTestScorePassingGrade = 25;

			report.startStep("Enter ED via CanvasPOST - Insert New User");
			pageHelper.closeBrowserAndOpenInCognito(false);
			gotoCanvasPOST(className, externalUserName, testFile);
			pageHelper.skipOnBoardingTour();
			
			report.startStep("Enter course and create NOT passing progress - completion < " + courseProgressPassingGrade
					+ "% / score avg < " + testAvgScorePassingGrade + "%");

			//homePage.enterToLAByMainButton();
			//sleep(2);
			//setProgressInCourse(courseId, 1, 15);

			homePage.waitHomePageloadedFully();
			learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "I3", 1, 2);
			learningArea2.clickOnNextButton();
			learningArea2.clickOnHearAll();

			report.startStep("Navigate to Home Page and check Course Criteria NOT Achieved");
			// homePage.clickToOpenNavigationBar();
			homePage.clickOnHomeButton();
			homePage.waitHomePageloadedFully();
			int courseCompletion = Integer.valueOf(homePage.getCompletionWidgetValue());
			int avgTestScore = Integer.valueOf(homePage.getScoreWidgetValue());

			if (courseCompletion > courseProgressPassingGrade || avgTestScore > testAvgScorePassingGrade) {
				testResultService.addFailTest("Course Progress is not as expected on this stage", false, true);
			}

			report.startStep("Open Assessments and check NO course test assigned");
			testsPage = homePage.openAssessmentsPage(false);
			testResultService.assertEquals(false, testsPage.isCourseTestAvailable(courseTestName),
					"Course Test displayed though should not. Verify progress and Avg score values in ilp file.");
			testsPage.close();
			sleep(2);

			report.startStep("Enter course again and create passing progress - completion > "
					+ courseProgressPassingGrade + "% / score avg > " + testAvgScorePassingGrade + "%");
			homePage.clickOnContinueButton();
			sleep(2);
		//	pageHelper.skipOnBoardingTour();
			//setProgressInCourse(courseId, 3, 50); // old

			//setProgressInCourseUnitIndex(courseId, 2, 50, 1);

			String unitId= courseUnits.get(0);
			List<String[]> unitComponents = dbService.getComponentDetailsByCourseId(courseId,unitId);
			studentService.setProgressForComponents(unitComponents.get(0)[0],courseId,studentId,null,30,1,true);
			studentService.setUserCourseUnitProgress(courseId, unitId, studentId);


			report.startStep("Navigate to Home Page check Course Criteria Achieved");
			// homePage.clickToOpenNavigationBar();
			homePage.clickOnHomeButton();
			homePage.waitHomePageloadedFully();
			courseCompletion = Integer.valueOf(homePage.getCompletionWidgetValue());
			avgTestScore = Integer.valueOf(homePage.getScoreWidgetValue());

			if (courseCompletion < courseProgressPassingGrade
					|| avgTestScore < testAvgScorePassingGrade) {
				testResultService.addFailTest("Course Progress is not as expected on this stage", false, true);
			}

			report.startStep("Open Assessments and check I3 Final Test assigned");
			homePage.openAssessmentsPage(false);
			// sleep(2);
			testResultService.assertEquals(true, testsPage.isCourseTestAvailable(courseTestName),
					"Course Test NOT displayed though should be. Make sure progress and avg score criteria in ilp.json file configured correctly.");

			report.startStep("Complete Course Test with NOT passing grade < " + courseTestScorePassingGrade + "");

			String testId = pageHelper.getAssignedTestIdForStudent(studentId, courseId, 3);
			int sectionsToSubmit = Integer
					.parseInt(dbService.getNumberOfSectionsInTestForCourseByTestId(courseId, testId, 3));

			report.startStep("Verify Placement Test NOT displayed in Available section");
			testsPage.checkPLTNotDisplayedInSectionByTestName(false);
			
			int testSequence = testsPage.getTestSequenceByCourseId(courseId);
			
			if (testSequence != 1) { // the PLT not to be shown
				testResultService.addFailTest(
						"Tests sequnce is Incorrect. Excpected: 1. course test.",
						false, true);
			}

			// 8 units // for
			int courseTestScore = Integer.valueOf(
					completeCourseTest(testSequence, CourseCodes.I3, CourseTests.FinalTest, 1, sectionsToSubmit));

			if (courseTestScore > courseTestScorePassingGrade) {
				testResultService.addFailTest("Course Test score is not as expected on this stage", false, true);
			}

			report.startStep("Open Assessments and check I3 Final Test still Assigned");
			homePage.openAssessmentsPage(false);
			// sleep(2);
			testResultService.assertEquals(true, testsPage.isCourseTestAvailable(courseTestName),
					"Course Test NOT displayed though should be. Make sure progress and avg score criteria in ilp.json file configured correctly.");
			testsPage.close();

			report.startStep("Verify the Home pgae and json loaded after make a progress and back to home");
			homePage.clickOnContinueButton();
			NewUxLearningArea2 learningArea2 = new NewUxLearningArea2(webDriver, testResultService);
			learningArea2.clickOnNextButton();
			homePage.clickOnHomeButton();
			homePage.waitHomePageloadedFully();

			homePage.openAssessmentsPage(false);
			testResultService.assertEquals(true, testsPage.isCourseTestAvailable(courseTestName),
					"Course Test NOT displayed though should be. Make sure progress and avg score criteria in ilp.json file configured correctly.");

			report.startStep("Complete Course Test with passing grade > " + courseTestScorePassingGrade + "");
			testSequence = testsPage.getTestSequenceByCourseId(courseId);
			if (testSequence != 1) {
				testResultService.addFailTest(
						"Tests sequnce is Incorrect. Excpected: 1. course test.",
						false, true);
			}

			// 5 is the number of section of 8 units
			courseTestScore = Integer
					.valueOf(completeCourseTest(testSequence, CourseCodes.I3, CourseTests.FinalTest, 4, 5));

			testId = pageHelper.getAssignedTestIdForStudent(studentId, courseId, 3); // report that test is assigned

			if (courseTestScore < courseTestScorePassingGrade) {
				testResultService.addFailTest("Course Test score is not as expected on this stage", false, true);
			}

			report.startStep("Open Assessments and check I3 Final Test NOT Assigned");
			homePage.openAssessmentsPage(false);
			// sleep(2);
			testResultService.assertEquals(false, testsPage.isCourseTestAvailable(courseTestName),
					"Course Test displayed though should not. Make sure progress and avg score criteria in ilp.json file configured correctly.");
			testsPage.close();
			sleep(2);
			
			report.startStep("Click on right arrow and verify Next course added on Home Page");
			sleep(1);
			homePage.carouselNavigateNext();
			sleep(2);
			String currentCourseName = homePage.getCurrentCourseName();
			testResultService.assertEquals(currentCourseName, coursesNames[7],
					"Course on Home Page is not valid or not found.");
			

			/*********************************************************************/
		} finally {
			report.startStep("In case of test failure, don't forget to verify ilp.json file wasn't modified");
			report.report("ilp.json path: "+PageHelperService.sharePhisicalFolder+"Institutions\\" + institutionId);
		}
	}
	

	private void setProgressInCourse(String courseId, int unitsToCompleteExceptTest, int averageTestScore)
			throws Exception {

		String unitId = "undefined";
		String componentId = "undefined";
		String step_id = "undefined";
		List<String[]> step_items = null;

		for (int i = 0; i < unitsToCompleteExceptTest; i++) {

			unitId = courseUnits.get(i);
			componentId = dbService.getComponentDetailsByUnitId(unitId).get(0)[1];
			step_id = dbService.getSubComponentsDetailsByComponentId(componentId).get(0)[1];
			step_items = dbService.getSubComponentItems(step_id);
			studentService.setProgressForUnit(unitId, courseId, studentId, 0, false);
			studentService.submitTest(studentId, unitId, componentId, step_items, String.valueOf(averageTestScore),
					null, true);

		}

	}

	private void setProgressInCourseUnitIndex(String courseId, int unitsToCompleteExceptTest, int averageTestScore,int startUnitIndex)
			throws Exception {

		String unitId = "undefined";
		String componentId = "undefined";
		String step_id = "undefined";
		List<String[]> step_items = null;

		for (int i = startUnitIndex; i <= unitsToCompleteExceptTest; i++) {

			unitId = courseUnits.get(i);
			componentId = dbService.getComponentDetailsByUnitId(unitId).get(0)[1];
			step_id = dbService.getSubComponentsDetailsByComponentId(componentId).get(0)[1];
			step_items = dbService.getSubComponentItems(step_id);
			studentService.setProgressForUnit(unitId, courseId, studentId, 0, false);
			studentService.submitTest(studentId, unitId, componentId, step_items, String.valueOf(averageTestScore),
					null, true);

		}

	}

	
	private String completeCourseTest(int courseSequenceInAvailableSection, CourseCodes code, CourseTests testType,
			int sectionsToAnswerCorrect, int totalSections) throws Exception {

		classicTest = testsPage.clickOnStartTest("1", String.valueOf(courseSequenceInAvailableSection));
		sleep(1);
		webDriver.closeAlertByAccept();
		sleep(3);
		webDriver.switchToNewWindow();
		classicTest.switchToTestAreaIframe();
		sleep(1);
		classicTest.pressOnStartTest();
		sleep(3);
		CourseTest courseTest = classicTest.initCourseTestFromCSVFile("files/CourseTestData/CourseTest_Answers.csv",
				code, testType, sectionsToAnswerCorrect);
		classicTest.performCourseTest(courseTest, totalSections);

		report.startStep("Get score and close test");
		classicTest.switchToCompletionMessageFrame();
		sleep(1);
		String finalScore = classicTest.getFinalScore();
		webDriver.switchToTopMostFrame();
		classicTest.switchToTestAreaIframe();
		classicTest.closeCompletionMessageAlert();
		sleep(1);

		return finalScore;

	}

	public String regLogin(String instID, UserType userType) throws Exception {

		report.addTitle("The parmeters is: " + instID + "UserName: " + userName + "Courses ints Id: " + institutionId);
		char createClass = 'Y';
		char userTypeParam = 'S';
		// String className="";
		String className1 = "";
		String className2 = "";
		String teacherId = "";
		String action = "i";
		String apiToken = dbService.getApiToken(institutionId);
		// userFN = userName + "-FN";
		// userLN = userName + "-LN";

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
				
				if (email.equalsIgnoreCase(""))
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
						dbService.getUserClassId(studentId) 
				);
				
				classId = dbService.getUserClassId(studentId);
				
				if (email.equalsIgnoreCase(""))
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
				amountClasses = teacherClassesBefore.size();
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

		if (useNewClass)
			createClass = 'Y';

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

			regUserUrl = baseUrl + "RegUserAndLogin.aspx?Action=" + action+"&UserName=" + userName + "" + "&Inst="
					+ institutionName + "&FirstName=" + userFN + "&LastName=" + userLN + "" + "&Password=12345&Email="
					+ email + "&Class=" + className + "" + "&Language=" + Language + "&Link=" + CorpUrl
					+ "&UseNameMapping=N&CreateClass=" + createClass + "" + "&UserType=" + userTypeParam;

			if (useToken){ // minal collegue
				regUserUrl = regUserUrl + "&token=" + apiToken;
				//regUserUrl = regUserUrl.replace("https://edusoftlearning.com/", BasicNewUxTest.CannonicalDomain);
			}
			if (institutionName.equalsIgnoreCase("automation") || (institutionName.equalsIgnoreCase("courses")))
				regUserUrl = regUserUrl.replace("ed.", "ed2.");
			
			webDriver.openUrl(regUserUrl);
			pageHelper.skipOptin();
			studentId = dbService.getUserIdByUserName(userName, institutionId);
			homePage.skipNotificationWindow();
			
			
			if (newTeacher || newStudent) {
				if (studentId != null) {
					dbService.insertUserToAutomationTable(institutionId, studentId, userName, CanvasClassName, baseUrl);
				}
			}

		} catch (Exception e) {

		}

		return className;
	}

	private String getTeacherClassesAsString(String studentId) {
		
		StringBuilder sb = new StringBuilder();
		
		List<String[]> teacherClassesBefore = pageHelper.getTeacherClasses(studentId);
		String[] classesT = new String[teacherClassesBefore.size()];
		
		
		for (int i=0;i<teacherClassesBefore.size();i++){
			classesT[i] = teacherClassesBefore.get(i)[0];
		}
		
		sb.append(String.join(";", classesT));
		className = sb.toString();
		
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
		String className = classes.get(classIndex)[0];
		if(useMapTable) {
			className = classes.get(classIndex)[2];
			internalClassName = classes.get(classIndex)[0];
		}
		

		return className;
	}

	@Test
	@TestCaseParams(testCaseID = { "43954", "50342" }, testTimeOut = "30")
	public void testUmmAdaptiveFlow() throws Exception {

		report.startStep("Init Test Data");
		institutionName = institutionsName[5];
		pageHelper.restartBrowserInNewURL(institutionName,true);
		
		int startCourseIndex = 5; // "I3 for PLT. Tourism Pre-Basic for Course";
		String pltResult = coursesNames[startCourseIndex];
		String courseId = coursesUMM[startCourseIndex * 2 + 2];
		String startCourseName = coursesNamesUMM[startCourseIndex * 2 + 2];
		courseUnits = dbService.getCourseUnits(coursesUMM[startCourseIndex * 2 + 2]);
		String courseTestName = "Tourism Pre-Basic Final Test";
		// String courseTestName = coursesNamesUMM[3] +" Mid-term Final Test";
		int courseProgressPassingGrade = 50;
		int testAvgScorePassingGrade = 50;
		int courseTestScorePassingGrade = 80;
		// classNameAR = "Class1_Umm Distance";
		// String[] section1_CorrectAns = {"New York","Her first flight was
		// delayed.","True","False","The flight is nonstop."};

		String createClass = "N"; // Y/N
		String createNewUser = "I"; // i = Insert U= Update
		String userType = "S"; // S = student T= Teacher
		String TestFileName = "UMM_Regular_RegUserPost_" + dbService.sig(4) + ".html";
		// String studentUserName="";
		CanvasClassName = "Class1-Umm Distance"; // Class1_Umm Distance

		report.startStep("Generate student username for registration");
		// String studentUserName = "stud" + dbService.sig(8);
		// Start of User Story 43738:Siglo21: Set Starting Course by PLT results

		report.startStep("Enter ED via new RegAndLogin API - Insert New User");

		externalUserName = userType + dbService.sig(5);
		createRegularRegUserAndLoginFile(createClass, createNewUser, userType, TestFileName);

		// regLogin(UMMInstId, studentUserName, UserType.Student, false); // Login by
		// Query String
		sleep(2);

		report.startStep("Check Home Page message on enter");
		homePage.verifyHomePageMessageStartCourseByPLT();

		report.startStep("Check PLT opens");
		classicTest = new NewUxClassicTestPage(webDriver, testResultService);
		sleep(1);
		webDriver.switchToNewWindow();
		classicTest.switchToTestAreaIframe();
		classicTest.verifyTitlePLT();
		sleep(2);

		report.startStep("Perform PLT test - skip Cycle 2 - get I2 placement");
		// PLTTest pltTest =
		// classicTest.initPLTTestFromCSVFile("files/PLTTestData/PLT_ED_Answers_NEW.csv",
		// true);
		// classicTest.performTest(pltTest);

		// Added by David

		TestEnvironmentPage testEnvironmentPage = new TestEnvironmentPage(webDriver, testResultService);
		int version = 1;
		int iteration = 2;
		String filePath = "";

		int[] wantedScore = new int[3];
		List<String[]> dataOfVersion = testEnvironmentPage.initScoresOfPLTByVersion("files/PLTTestData/ScoresNew.csv",
				Integer.toString(version));

		wantedScore[0] = Integer.parseInt(dataOfVersion.get(iteration - 1)[3]); // C1 Reading Score
		wantedScore[1] = Integer.parseInt(dataOfVersion.get(iteration - 1)[4]); // C1 Listening Score
		wantedScore[2] = Integer.parseInt(dataOfVersion.get(iteration - 1)[5]); // C1 Grammar Score

		switch (version) {
		case 1:
			// inst = institutionName; //"automation";
			// classNameAttr = "classname";
			filePath = "files/PLTTestData/PLT_V1_NEW.csv";
			break;
		/*
		 * case 2: inst = "local"; classNameAttr = "local_className"; filePath =
		 * "files/PLTTestData/PLT_V2_NEW.csv"; break; case 3: inst = "courses";
		 * classNameAttr = "classname.enrich"; filePath =
		 * "files/PLTTestData/PLT_V3_NEW.csv"; break;
		 */
		}
		String level = dataOfVersion.get(iteration - 1)[2];
		testEnvironmentPage.performTestInSpecificRoute(filePath, level, wantedScore, version);

		// End Added by David

		report.startStep("Verify expected placement level on test end");
		classicTest.verifyPlacementLevelOnResultPagePLT(pltResult);

		report.startStep("Verify 'Do Test Again' btn not displayed");
		classicTest.verifyDoTestAgainNotDisplayedInPLT();

		report.startStep("Exit PLT");
		classicTest.clickOnExitPLT();
		sleep(2);

		report.startStep("Press on Start PLT again");
		pageHelper.skipOnBoardingTour();
		testsPage = homePage.openAssessmentsPage(false);
		//classicTest = testsPage.clickOnStartTest("1", "1");
		
		report.startStep("Verify Placement Test NOT displayed in Available section");
		testsPage.checkPLTNotDisplayedInSectionByTestName(false);
		
/*
		report.startStep("Verify and close browser alert");
		String alertText = webDriver.getAlertText(3);
		webDriver.closeAlertByAccept();
		testResultService.assertEquals(Cannot_Take_PLT_Alert, alertText, "Checking 'Cannot take PLT' Alert");
*/
		report.startStep("Verify PLT not opened again");
		webDriver.checkNumberOfTabsOpened(1);

		report.startStep("Check Tests Results section");
		webDriver.refresh();
		testsPage = homePage.openAssessmentsPage(false);
		// sleep(1);
		testsPage.clickOnArrowToOpenSection("3");
		sleep(1);
		testsPage.checkSubmissionDateForTests("1");
		testsPage.checkScoreLevelPLT("1", pltResult);

		report.startStep("Close Assessments page");
		testsPage.close();

		report.startStep("Check ONLY starting course available on Home Page");
		pageHelper.skipOnBoardingHP();
//			homePage.checkCourseCarouselArrowsNotDisplayed();
		testResultService.assertEquals(startCourseName, homePage.getCurrentCourseName(),
				"Starting course on Home Page is not valid or not found");

		// End of User Story 43738:Siglo21: Set Starting Course by PLT results

		/*********************************************************************/

		// Start of User Story 43742:Siglo21: Assign Test if Course Criteria Achieved

		report.startStep("Enter course and create NOT passing progress - completion < 60% / score avg < 40%");
		homePage.clickOnContinueButton();
		sleep(2);
	//	pageHelper.skipOnBoardingTour();
		setProgressInCourse(courseId, 1, 30);
		NewUxLearningArea2 learningArea2 = new NewUxLearningArea2(webDriver, testResultService);
		learningArea2.waitToLearningAreaLoaded();
		learningArea2.clickOnNextButton();
		learningArea2.clickPlayNewPlayer();

		report.startStep("Navigate to Home Page and check Course Criteria NOT Achieved");
		// homePage.clickToOpenNavigationBar();
		homePage.clickOnHomeButton();
		homePage.waitHomePageloadedFully();
		int courseCompletion = Integer.valueOf(homePage.getCompletionWidgetValue());
		int avgTestScore = Integer.valueOf(homePage.getScoreWidgetValue());

		if (courseCompletion > courseProgressPassingGrade || avgTestScore > testAvgScorePassingGrade) {
			testResultService.addFailTest("Course Progress is not as expected on this stage", false, true);
		}

		report.startStep("Open Assessments and check NO course test assigned");
		testsPage = homePage.openAssessmentsPage(false);
		// sleep(2);
		testResultService.assertEquals(false, testsPage.isCourseTestAvailable(courseTestName),
				"Course Test displayed though should not");
		testsPage.close();
		sleep(1);

		report.startStep("Enter course again and create passing progress - completion > 40% / score avg > 50%");
		homePage.clickOnContinueButton();
		learningArea2.clickOnNextButton();
		// sleep(2);
	//	pageHelper.skipOnBoardingTour();
		submitTestForUnit(courseId, 1, 100);

		// for (int unitIndex=2;unitIndex<=3;unitIndex++){
		studentService.setProgressForUnit(courseUnits.get(2), courseId, studentId);
		// }

		report.startStep("Navigate to Home Page check Course Criteria Achieved");
		// homePage.clickToOpenNavigationBar();
		homePage.clickOnHomeButton();
		homePage.waitHomePageloaded();

		courseCompletion = Integer.valueOf(homePage.getCompletionWidgetValue());
		avgTestScore = Integer.valueOf(homePage.getScoreWidgetValue());

		if (courseCompletion < courseProgressPassingGrade || avgTestScore < testAvgScorePassingGrade) {
			testResultService.addFailTest("Course Progress is not as expected on this stage", false, true);
		}
		sleep(1);

		report.startStep("Open Assessments and check Tourism Pre-Basic Final Test assigned");
		testsPage = homePage.openAssessmentsPage(false);

		testResultService.assertEquals(true, testsPage.isCourseTestAvailable(courseTestName),
				"Course Test NOT displayed though should be");

		// End of User Story 43742:Siglo21: Assign Test if Course Criteria Achieved

		/*********************************************************************/

		// Start of User Story 43744:Siglo21: Assign Next Course if Course Criteria
		// Achieved
		// Start of User Story 43743:Siglo21: Re-Assign Test if Test Failed

		report.startStep("Verify Placement Test NOT displayed in Available section");
		testsPage.checkPLTNotDisplayedInSectionByTestName(false);
		
		report.startStep("Complete Course Test with NOT passing grade < 70");
		int testSequence = testsPage.getTestSequenceByCourseId(courseId);
		if (testSequence != 1) {
			testResultService.addFailTest(
					"Tests sequnce is Incorrect. Excpected: 1. course test.",
					false, true);
		}
		int courseTestScore = Integer
				.valueOf(completeCourseTest(testSequence, CourseCodes.B1, CourseTests.MidTerm, 1, 3));
		if (courseTestScore > courseTestScorePassingGrade) {
			testResultService.addFailTest("Course Test score is not as expected on this stage", false, true);
		}

		report.startStep("Open Assessments and check Tourism Pre-Basic Final Test still Assigned");
		homePage.openAssessmentsPage(false);
		// sleep(2);
		testResultService.assertEquals(true, testsPage.isCourseTestAvailable(courseTestName),
				"Course Test NOT displayed though should be");

		report.startStep("Complete Course Test with passing grade > 70");
		testSequence = testsPage.getTestSequenceByCourseId(courseId);
		if (testSequence != 1) {
			testResultService.addFailTest(
					"Tests sequnce is Incorrect. Excpected: 1. Placement test, 2. course test. Actual: 1. course test, 2. Placement test",
					false, true);
		}
		courseTestScore = Integer.valueOf(completeCourseTest(testSequence, CourseCodes.B1, CourseTests.MidTerm, 3, 3));
		if (courseTestScore < courseTestScorePassingGrade) {
			testResultService.addFailTest("Course Test score is not as expected on this stage", false, true);
		}

		report.startStep("Open Assessments and check Tourism Pre-Basic Final Test NOT Assigned");
		homePage.openAssessmentsPage(false);
		// sleep(2);
		testResultService.assertEquals(false, testsPage.isCourseTestAvailable(courseTestName),
				"Course Test displayed though should not");
		testsPage.close();
		sleep(2);

		report.startStep("Click on right arrow and verify Next course added on Home Page");
		homePage.carouselNavigateNext();
		sleep(2);
		testResultService.assertEquals(coursesNamesUMM[startCourseIndex * 2 + 3], homePage.getCurrentCourseName(),
				"Next course on Home Page is not valid or not found");

		// End of User Story 43744:Siglo21: Assign Next Course if Course Criteria
		// Achieved
		// End of User Story 43743:Siglo21: Re-Assign Test if Test Failed

		/*********************************************************************/
	}

	private void submitTestForUnit(String courseId, int i, int averageTestScore) {
		String unitId = "undefined";
		String componentId = "undefined";
		String step_id = "undefined";
		List<String[]> step_items = null;

		try {

			unitId = courseUnits.get(i);
			componentId = dbService.getComponentDetailsByUnitId(unitId).get(0)[1];
			step_id = dbService.getSubComponentsDetailsByComponentId(componentId).get(0)[1];
			step_items = dbService.getSubComponentItems(step_id);
			
			studentService.submitTest(studentId, unitId, componentId, step_items, String.valueOf(averageTestScore),
					null, true);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	@TestCaseParams(testCaseID = { "56109" }, testTimeOut = "30")
	public void testUmmFullAssignmentFollowingPLTResult() throws Exception {

		report.startStep("Init Test Data");

		institutionName = institutionsName[5];
		newStudent = true;
		pageHelper.restartBrowserInNewURL (institutionName,true);
		//pageHelper.initializeData();

		int startCourseIndex = 9; // A3 grade for PLT.
		String pltResult = coursesNames[startCourseIndex];
		// CanvasClassName = "Class1_Umm Distance";

		report.startStep("Generate student username for registration");
		// String studentUserName = "stud" + dbService.sig(6);

		report.startStep("Enter ED via new RegAndLogin API - Insert New User, the Inst Id is: " + institutionId);
		CanvasClassName = regLogin(institutionId, UserType.Student);

		report.startStep("Check Home Page message on enter");
		homePage.verifyHomePageMessageStartCourseByPLT();

		report.startStep("Check PLT opens");
		classicTest = new NewUxClassicTestPage(webDriver, testResultService);
		webDriver.switchToNewWindow();
		classicTest.switchToTestAreaIframe();
		classicTest.verifyTitlePLT();
		sleep(2);

		report.startStep("Perform PLT test - get A3 placement");
		PLTTest pltTest = classicTest.initPLTTestFromCSVFile("files/PLTTestData/PLT_ED_Answers_NEW.csv", false);
		classicTest.performTest(pltTest);

		report.startStep("Verify expected placement level on test end");
		classicTest.verifyPlacementLevelOnResultPagePLT(pltResult);

		report.startStep("Verify 'Do Test Again' btn not displayed");
		classicTest.verifyDoTestAgainNotDisplayedInPLT();
		sleep(2);

		report.startStep("Exit PLT");
		classicTest.clickOnExitPLT();
		sleep(3);

		report.startStep("Check all ilp courses are available for learning");
		pageHelper.skipOnBoardingTour();
		sleep(1);
		// homePage.navigateToRequiredCourseOnHomePage(coursesNamesUMM[0]);

		homePage.clickOnCourseList();
		Thread.sleep(1000);

		List<WebElement> courseList = homePage.getCourseListCourseELements();
		testResultService.assertEquals(coursesNamesUMM.length, courseList.size(), "not all cousres opened");

		// homePage.navigateToRequiredCourseByList(coursesNamesUMM[0]);
		/*
		 * for(int i = 0; i < 20; i++) {
		 * testResultService.assertEquals(coursesNamesUMM[i],
		 * homePage.getCurrentCourseName(), "Coure don't match: " + coursesNamesUMM[i]);
		 * homePage.carouselNavigateNext(); sleep(1); }
		 */
	}
/*
	private void unAssignTeacherClasses(List<String[]> teachers) throws Exception {
		String teacherId = "";
		List<String[]> clases;
		StringBuilder sb = new StringBuilder();
		
		for(String[]arr:teachers) {
			teacherId = arr[0];
			List<String[]> userName = dbService.getUserNameAndPasswordByUserId(teacherId);
			String name = userName.get(0)[0];
			if(!name.equalsIgnoreCase("autoTeacher2")) {
			if(useMapTable) {
				clases = dbService.getExternalTeacherClasses(teacherId);
			}else {
				clases = dbService.getTeacherClasses(teacherId);
			}
			sb.append(clases.get(0)[0]+";");
			sb.append(clases.get(1)[0]+";");
			dbService.assignTeacherToClasses(sb.toString(),teacherId);
			}
		}
	}
*/
	/*
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
*/
	/*
	private void skipAllEntrancesAndVerifyStudentName() throws Exception {
		pageHelper.skipOptin();
		homePage.skipNotificationWindow();
	//	sleep(3);
		pageHelper.skipOnBoardingHP();
		webDriver.switchToMainWindow();
		pageHelper.closeLastSessionImproperLogoutAlert();
		testResultService.assertEquals("Hello " + userFN + "", homePage.getUserDataText(),
				"First Name in Header of Home Page not match");
		//homePage.waitHomePageloadedFully();
		
	}
 */
	
	/*
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
*/
	
/*
	private void closeAllWindows() {
			Set<String> windows = webDriver.getWebDriver().getWindowHandles();
		if(windows != null) {
			for(String s:windows) {
				webDriver.getWebDriver().switchTo().window(s).close();
			}
		}
	}
*/
	
/*
	private void createRegUserAndLoginFileAndReturnURL(String createClass, String createUser, String userType,
			String TestFileName) throws Exception {
		String baseUrl = pageHelper.CILink.split(".com")[0] + ".com/";
		String accessUrl = pageHelper.urlForExternalPages.split(".com")[0] + ".com/languages/";
		String chkPath = pageHelper.buildPathForExternalPages + "Languages/";
		// String studentUserName="";
		//List<String[]> classes = null;

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
			}
			else {
				studentId = getExistingUser("student");
				List<String[]>allClassesOfInstitution = pageHelper.getclassesByInstitutionName(institutionName);
				className = getClassFromClassesList(allClassesOfInstitution);
			}
		}

		report.startStep("Create test file -- " + TestFileName);

		// .....................................................
		List<String> wList = new ArrayList<String>();

		wList = createHtmlTitle(wList);
		wList = createFormInfoRegularRegUserAndLogin(wList, baseUrl, institutionName, className, userName, userFN,
				userLN, createClass, createUser, userType);
		wList = createHtmlTail(wList);

		textService.writeListToSmbFile(chkPath + TestFileName, wList, netService.getDomainAuth());

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
*/
	/*
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
		
	}
*/
	
	private void gotoCanvasPOST(String chkClass, String chkUser, String testFile) throws Exception {

		String baseUrl = pageHelper.CILink.split(".com")[0] + ".com/";
		String filePath = pageHelper.buildPathForExternalPages + "Languages";
		String accessFile = pageHelper.urlForExternalPages.split(".com")[0] + ".com/" + "Languages";
		testFile = testFile + "_CanvasPOST_" + dbService.sig(4) + ".html";

		String classId = dbService.getClassIdByClassName(className, institutionId);
		CanvasClassId = dbService.getClassFromClassExternalMapByClassId(classId);
		
		createTestCanvasPltFile(filePath, baseUrl, chkClass, chkUser, testFile);

		String openURL = accessFile+"/"+testFile;

		report.startStep("Get institution Remaining License for all Valid Packages");
		//List<String[]> getValidInstitutionPackages = dbService.getValidInstitutionPackages(institutionId);
			List<String[]> classPackages = dbService.getClassAssignPackagesNew(className, institutionId);
			List<String[]> remainingLicensesBeforeAddingUser = new ArrayList<>();
			if (classPackages!=null) {
				remainingLicensesBeforeAddingUser = dbService.getRemainingLicensesOfCertainPackage(classPackages, institutionId);
			}else {
				testResultService.addFailTest("ClassHasNoPackages",false,false);
			}

		webDriver.openUrl(openURL);
		webDriver.waitUntilElementAppears(webDriver.waitForElement("btnSubmit", ByTypes.name), 20);
	try {
		webDriver.waitForElement("btnSubmit", ByTypes.name).click();
		sleep(2);

		if (newStudent || newTeacher) {
			report.startStep("Check OptIn PrivacyCheckBox and press Continue");
			pageHelper.skipOptin();
			sleep(2);
			studentId = getEdUserId(userName);

			if (studentId != null)
				dbService.checkTheUserInsertedToLtiGrades(studentId, null);

			if (newStudent&&remainingLicensesBeforeAddingUser != null) {
				report.startStep("Check institution Remaining License reduced after login");
					//pageHelper.checkRemainingLicenseBurnForallValidPackages(getValidInstitutionPackages,institutionId,false);
					report.startStep("Get institution Remaining License after adding new student");
					List<String[]> remainingLicensesAfterAddingUser = dbService.getRemainingLicensesOfCertainPackage(classPackages, institutionId);
					pageHelper.validateLicensesReducesAfterNewStudentAdded(remainingLicensesBeforeAddingUser, remainingLicensesAfterAddingUser);
				}
			} else {
				pageHelper.checkRemainingLicenseBurnForallValidPackages(remainingLicensesBeforeAddingUser, institutionId, className,true);
			}
	}catch (Exception e) {
		e.printStackTrace();
	}
	finally {
		if (studentId != null)
			dbService.insertUserToAutomationTable(institutionId, studentId, userName, CanvasClassName, baseUrl);
	}
}
	
	
	private void createTestCanvasPltFile(String chkPath, String baseUrl, String chkClass, String chkUser,
			String testFile) throws Exception {

		List<String> wList = new ArrayList<String>();

		wList = createHtmlTitle(wList);
		wList = createPltFormInfo(wList, baseUrl, institutionName, chkClass, chkUser);
		wList = createHtmlTail(wList);
		textService.writeListToSmbFile(chkPath, testFile, wList, false);
		//textService.writeListToSmbFile(chkPath +"/"+ testFile, wList, netService.getDomainAuth());
	}

	private List<String> createPltFormInfo(List<String> wList, String chkURL, String chkInst, String chkClass,
			String chkUser) {
		wList.add("\t<form method=\"post\" action=\"" + chkURL + "entrance.aspx\">");
		wList.add("\t\t<input type=\"submit\" name=\"btnSubmit\" value=\"GO TO ED\" />");

		wList.add(
				"\t\t<input type=\"hidden\" name=\"oauth_nonce\" value=\"iKk7CNCRo0JtuHds75AjzzRFD9z5fnOmZaUBDpOhZuw\" />");
		wList.add("\t\t<input type=\"hidden\" name=\"oauth_consumer_key\" value=\"ED_FP\" />");
		wList.add("\t\t<input type=\"hidden\" name=\"oauth_signature\" value=\"I96FVJ658qR6yld9mA1+MziiZLY=\" />");
		wList.add(
				"\t\t<input type=\"hidden\" name=\"lis_outcome_service_url\" value=\"https://siglo21.instructure.com/api/lti/v1/tools/190/grade_passback\" />");
		wList.add(
				"\t\t<input type=\"hidden\" name=\"lis_result_sourcedid\" value=\"190-1276-17670-6241-65b27cce6f3cb6e8ca4fda409c4d8a5f3f487f77\" />");
		wList.add("\t\t<input type=\"hidden\" name=\"custom_edinstitution\" value=\"" + chkInst + "\" />");

		wList.add("\t\t<input type=\"hidden\" name=\"roles\" value=\"Learner\" />");

		wList.add("\t\t<input type=\"hidden\" name=\"lis_person_name_family\" value=\"FN_" + userName + "\" />");
		wList.add("\t\t<input type=\"hidden\" name=\"lis_person_name_given\" value=\"Given_" + userName + "\" />");
		wList.add("\t\t<input type=\"hidden\" name=\"lis_person_name_full\" value=\"Full_" + userName + "\" />");
		wList.add("\t\t<input type=\"hidden\" name=\"lis_person_contact_email_primary\" value=\"\" />");
		wList.add("\t\t<input type=\"hidden\" name=\"custom_canvas_user_id\" value=\"" + userName + "\" />");
		wList.add("\t\t<input type=\"hidden\" name=\"custom_user_sections\" value=\"" + className + "\" />");

//			wList.add("\t\t<input type=\"hidden\" name=\"custom_course_sections\" value=\"" + chkCanvasClass + "\" />");
		wList.add("\t\t<input type=\"hidden\" name=\"custom_course_sections\" value=\"" + CanvasClassId + "\" />");

		// igb 2018.11.20 this parameter already not mandatory (for entry before PLT)
		// -----------
		// wList.add("\t\t<input type=\"hidden\" name=\"custom_course_id\"
		// value=\"20000\" />");
		wList.add("\t\t<input type=\"hidden\" name=\"Link\" value=\"" + CorpUrl + "\"/>");
		wList.add("\t\t<input type=\"hidden\" name=\"oauth_timestamp\" value=\"1525609571\" />");

		wList.add("\t</form>");

		return wList;
	}
	
	private String getEdUserId(String chkUser) throws Exception {

		return dbService.getExternalUserInternalId(chkUser);
	}

	// --igb 2018.11.18---------------

	public void runCanvasTest(String testType) throws Exception {

		String baseUrl = pageHelper.CILink.split(".com")[0] + ".com/";
		//String baseUrl = "https://EDUI-CI-RC-2022-08-20220823-1.edusoftrd.com/";
		String accessUrl = pageHelper.urlForExternalPages.split(".com")[0] + ".com/";
		//String accessUrl = "https://EDUI-CI-RC-2022-08-20220823-1.edusoftrd.com/";
		String teacherId = "";
		String chkPath = pageHelper.buildPathForExternalPages + "Languages"; 
		//String chkPath = "smb://CI-SRV//BuildsArtifacts//EDUI_CI_RC_2022_08_20220823.1//Languages/";
							
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
				if (newTeacher) {
					getUser(5);

				} else {
					teacherId = getExistingUser("teacher");
					sleep(2);
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
			report.startStep("Create and open URL");
			accessUrl = accessUrl + "Languages/" + testFile;
		//	pageHelper.closeBrowserAndOpenInCognito(false);
			webDriver.openUrl(accessUrl);
			
			
		
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
	}

	private void verifyUsersData(String testFile, String teacherId, String baseUrl) throws Exception {

		webDriver.waitUntilElementAppearsAndReturnElementByType("btnSubmit", ByTypes.name, 10); // webDriver.waitForElement("btnSubmit",ByTypes.name);
		webDriver.waitForElement("btnSubmit", ByTypes.name).click();

		sleep(2);
		pageHelper.skipOptin();
		homePage.skipNotificationWindow();
		pageHelper.skipOnBoardingHP();
		if(newStudent || newTeacher) {
			if(useMapTable) {
				studentId = dbService.getExternalUserInternalId(userName);
			}else {
				studentId = dbService.getUserIdByUserName(userName, institutionId);
			teacherId = studentId;
			}
		}

		if (studentId != null) { // userId exists created or return
			if (newStudent || newTeacher) { 
				report.startStep("Insert new user into table");
				teacherId = insertNewUserIntoTable(baseUrl);

			}
		}
			
		sleep(2);
		
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
		// studentId = dbService.getExternalUserInternalId(studentUserName,
		// institutionId);
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

	//	String userNameDB = "";
		report.startStep("Validate Data is Displayed Correctly in My Profile");
		homePage.waitHomePageloaded();
		if (newStudent) {
			while (!homePage.myProfileIsClickable()) {
				homePage.clickOnRightArrow();
			}
			// homePage.refreshHomePage();
			// homePage.clickWithJS();
		}

		homePage.clickOnMyProfile();
		homePage.switchToMyProfile();
		NewUxMyProfile myProfile = new NewUxMyProfile(webDriver, testResultService);

		testResultService.assertEquals(userLN, myProfile.getLastName(), "Last Name in My Profile is Incorrect.");
		testResultService.assertEquals(userFN, myProfile.getFirstName(), "First Name in My Profile is Incorrect.");
		testResultService.assertEquals(email, myProfile.getMail(), "Mail in My Profile is Incorrect.");

		// verifyUserDataStoredInDB(myProfile);
		report.startStep("Validate User Data stored in DB correctly");
		String userIdMyProfile = myProfile.getUserName();

		List<String[]> classDB = null;
		boolean newClassPresent = false;
		if (useMapTable) {
			if (useNewClass) {
				
				if (userType.name().equalsIgnoreCase("Teacher")){
					// List<String[]> classes = dbService.getExternalTeacherClasses(studentId);
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
				// dbService.getUserNameByUserIdFromUsersExternalMap(userIdMyProfile);
				report.startStep("Validate User's Class data stored in DB correctly");
				classDB = dbService.getClassFromClassExternalMapByUserId(userIdMyProfile);// HERE IS EXCEPTION
				int index = 0;
				if(canvasTest) {
					index = 1;
				}
				if(!useNewClass) {
					testResultService.assertEquals(CanvasClassId, classDB.get(0)[index], "External Class ID is Incorrect.");
				}
				testResultService.assertEquals(className, classDB.get(0)[index+1],"Class Name is Incorrect.");
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
	}
	
	private void verifyThatTeacherAssignedToClass(String teacherId) throws Exception {
		report.startStep("Check teacher assigned to class");
		
		List<String[]> teacherClasses = null;
		String ExternalclassId = "";
		if(useMapTable) {
			teacherClasses = dbService.getExternalTeacherClasses(teacherId);
					}else {
		 teacherClasses = pageHelper.getTeacherClasses(teacherId);
		
		}
		for (int i = 0; i < teacherClasses.size(); i++) {
		
			if(useMapTable) {
				tmsHome.checkClassNameIsDisplayed(teacherClasses.get(i)[1]);
			}else {
			tmsHome.checkClassNameIsDisplayed(teacherClasses.get(i)[0]);
			}
			report.startStep("Check teacher assigned to class");

			if (useNewClass) {
				if(useMapTable) {
					ExternalclassId = dbService.getClassFromClassExternalMapByClassId(teacherClasses.get(i)[0]);
				}else {
				 ExternalclassId = dbService.getClassFromClassExternalMapByClassId(teacherClasses.get(i)[1]);
				}
				testResultService.assertEquals(true, ExternalclassId != null, "External class Id not created.");
			}

		}
		report.startStep("logout from TMS");
		tmsHome.clickOnExitTMS();
		sleep(4);

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
		studentId = dbService.getExternalUserInternalId(userName, institutionId);
		String teacherId = studentId;
		dbService.insertUserToAutomationTable(institutionId, studentId, userName, CanvasClassName, baseUrl);
		String actualExternalUserId = dbService.getExternalUserInternalId(userName, institutionId);
		testResultService.assertEquals(studentId, actualExternalUserId, "User Not created correctly in External Table");
		return teacherId;
	}

	private void retrieveExistingClasses() throws Exception {
		if(userType.name().equalsIgnoreCase("Teacher")) {
			classAssignmnetAmount = 2;
			storeAmountOfTeacherClases(userType);
			}
		assignNewOrCurrentClassesWithAmount(classAssignmnetAmount, userType);

		// List<String[]> classes = null;
		// List<String[]> classDetails = null;
		// String typeOfUser = UserType.Teacher.name();

		/*
		 * if (multipleClasses) {
		 * 
		 * if ((!newTeacher) && typeOfUser.equals("Teacher")) { if (useMapTable) {
		 * List<String[]> allClassesFromExternalInstitution =
		 * dbService.getExternalClassesNameByInstitutionId(institutionId); classes =
		 * dbService.getExternalTeacherClasses(studentId);
		 * addClasses(allClassesFromExternalInstitution, classes, 2); }
		 * 
		 * else { List<String[]> allClassesOfInstitution =
		 * pageHelper.getclassesByInstitutionName(institutionName); classes =
		 * pageHelper.getTeacherClasses(studentId);
		 * addClasses(allClassesOfInstitution,classes, 2); } } }else { classDetails =
		 * getDiffExistingClassesAndClassIds(classes, 1); CanvasClassName =
		 * classDetails.get(0)[1]; CanvasClassId = classDetails.get(0)[2]; }
		 */

		/*
		 * List<String[]> classes = null; List<String[]> classDetails = null; //
		 * 
		 * if (newTeacher) classes =
		 * dbService.getExternalClassesNameByInstitutionId(institutionId); else classes
		 * = dbService.getExternalTeacherClasses(teacherId);
		 * 
		 * if (multipleClasses) {
		 * 
		 * if (classes.size() > 2) { classDetails =
		 * getDiffExistingClassesAndClassIds(classes, 2); CanvasClassName =
		 * classDetails.get(0)[1] + ";" + classDetails.get(1)[1]; CanvasClassId =
		 * classDetails.get(0)[2] + ";" + classDetails.get(1)[2]; } else { classDetails
		 * = getDiffExistingClassesAndClassIds(classes, 1); CanvasClassName =
		 * classDetails.get(0)[1]; CanvasClassId = classDetails.get(0)[2]; } } else {
		 * classDetails = getDiffExistingClassesAndClassIds(classes, 1); CanvasClassName
		 * = classDetails.get(0)[1]; CanvasClassId = classDetails.get(0)[2]; }
		 */
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
		  
		  } }
		 

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
		if (email.equalsIgnoreCase(""))
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
			CanvasClassName = "Class_" + suffix + ";" + "Class_" + suffix1;
			CanvasClassId = "Class_" + suffix + "_Id" + ";" + "Class_" + suffix1 + "_Id";
			className = CanvasClassName;
		} else {
			CanvasClassName = "Class_" + suffix;
			CanvasClassId = "Class_" + suffix + "_Id";
			className = CanvasClassName;
		}
	}
/*
	private void cleanGlobalValues() {
		userFN = "FN";
		userLN = "LN";
		userName = "";
		studentId = "";
	}
*/
	/*
	private void cleanTmpInfo(String chkClass, String chkUser) throws Exception {

		String chkUserId = dbService.getExternalUserInternalId(chkUser, institutionId);
		if (chkUserId == null) {
			chkUserId = studentId;
		}
	}
*/
	
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
			break;

		case "NewStudentOldClass":
			testFile = "testNewStudentOldClass_" + dbService.sig(4) + ".html";
			break;

		case "NewStudentLongClassName":
			testFile = "testNewStudentLongClassName_" + dbService.sig(4) + ".html";
			break;

		case "WithOutComeAndSourceId":
			testFile = "WithOutComeAndSourceId_" + dbService.sig(4) + ".html";
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
		}

		if (testFile.equals(""))
			return "undefined";

		// .....................................................
		List<String> wList = new ArrayList<String>();

		wList = createHtmlTitle(wList);
		wList = createFormInfo(wList, baseUrl, institutionName, CanvasClassName);
		wList = createHtmlTail(wList);
		textService.writeListToSmbFile(chkPath, testFile, wList, false);
		//textService.writeListToSmbFile(chkPath +"/"+ testFile, wList, netService.getDomainAuth());

		return testFile;
	}

	private List<String> createFormInfo(List<String> wList, String chkURL, String chkInst, String chkClass
			) {
		String chkUser;
		if(useMapTable) {
			 chkUser = externalUserName;
		}else {
			 chkUser = userName;
		}
		wList.add("\t<form method=\"post\" action=\"" + chkURL + "entrance.aspx\">");
		wList.add("\t\t<input type=\"submit\" name=\"btnSubmit\" value=\"Form Submit\" />");
		wList.add(
				"\t\t<input type=\"hidden\" name=\"oauth_nonce\" value=\"iKk7CNCRo0JtuHds75AjzzRFD9z5fnOmZaUBDpOhZuw\" />");
		wList.add("\t\t<input type=\"hidden\" name=\"oauth_consumer_key\" value=\"ED_FP\" />");
		wList.add("\t\t<input type=\"hidden\" name=\"oauth_signature\" value=\"I96FVJ658qR6yld9mA1+MziiZLY=\" />");

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
		wList.add("\t\t<input type=\"hidden\" name=\"Link\" value=\"" + CorpUrl + "\"/>");
		wList.add("\t\t<input type=\"hidden\" name=\"oauth_timestamp\" value=\"1525609571\" />");

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

	private String createRegularRegUserAndLoginFile(String createClass, String createUser, String userType,
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

		//textService.writeListToSmbFile(chkPath + TestFileName, wList, netService.getDomainAuth());
		textService.writeListToSmbFile(chkPath, TestFileName, wList, useSMB);

		String openURL = accessUrl + TestFileName;

		pageHelper.closeBrowserAndOpenInCognito(false);

		report.startStep("Get institution Remaining License for all Valid Packages");
			List<String[]> getValidInstitutionPackages = dbService.getValidInstitutionPackages(institutionId);
			List<String[]> classPackages = new ArrayList<>();
			if (userType.equalsIgnoreCase("S")&&createClass.equalsIgnoreCase("N")) {
				//List<String[]> getValidInstitutionPackages = dbService.getValidInstitutionPackages(institutionId);

				classPackages = dbService.getClassAssignPackagesNew(className, institutionId);
				if (classPackages!=null) {
					getValidInstitutionPackages = dbService.getRemainingLicensesOfCertainPackage(classPackages, institutionId);
				}else {
					System.out.println("<==================!!! Class "+className+"HAS NO PACKAGES !!!====================>");
				}
			}

		webDriver.openUrl(openURL);
		webDriver.waitUntilElementAppears(webDriver.waitForElement("btnSubmit", ByTypes.name), 20);

		webDriver.waitForElement("btnSubmit", ByTypes.name).click();
		sleep(2);

		if (!useNameUserWithMinus) {
			pageHelper.skipOptin();

			studentId = dbService.getUserIdByUserName(userName, institutionId);

			if (newTeacher || newStudent) {
				if (studentId != null)
					dbService.insertUserToAutomationTable(institutionId, studentId, userName, className,
							PageHelperService.buildId);
				else
					testResultService.addFailTest("User not created", true, true);

				if (newStudent) {
					report.startStep("Check institution Remaining License reduced after login");
					if (getValidInstitutionPackages != null && classPackages!=null) {
						List<String[]> remainingLicensesAfterLogin = dbService.getRemainingLicensesOfCertainPackage(classPackages, institutionId);
						pageHelper.validateLicensesReducesAfterNewStudentAdded(getValidInstitutionPackages, remainingLicensesAfterLogin);
						//pageHelper.checkRemainingLicenseBurnForallValidPackages(getValidInstitutionPackages,institutionId,false);
					}
				} /*else {
					pageHelper.checkRemainingLicenseBurnForallValidPackages(getValidInstitutionPackages, institutionId, true);
				}*/
			}
		}
		if (useNewClass || createClass.equalsIgnoreCase("Y")){
			classId = dbService.getClassIdByClassName(className, institutionId);
			dbService.insertClassToAutomationTable(classId, className, CanvasClassId, institutionId, studentId, baseUrl);
		}
			
		
		return userName;
	}


	private List<String> createFormInfoRegularRegUserAndLogin(List<String> wList, String chkURL, String chkInst,
			String chkClass, String chkUser, String userFN, String userLN, String createClass, String createUser,
			String userType) {

		wList.add("\t<form method=\"post\" action=\"" + chkURL + "RegUserAndLogin.aspx\">");
		wList.add("\t\t<input type=\"submit\" name=\"btnSubmit\" value=\"Form Submit\" />");
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
