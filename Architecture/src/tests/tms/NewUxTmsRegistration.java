package tests.tms;


import Enums.ByTypes;
import Interfaces.TestCaseParams;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import pageObjects.edo.*;
import pageObjects.tms.*;
import testCategories.AngularLearningArea;
import tests.edo.newux.SpeechRecognitionBasicTestNewUX;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class NewUxTmsRegistration extends SpeechRecognitionBasicTestNewUX {

		
	NewUXLoginPage loginPage;
	DashboardPage dashboardPage;
	TmsHomePage tmsHomePage;
	TeacherDetailsPage teacherDetailedPage;
	NewUxLearningArea learningArea;
	NewUxAssessmentsPage testsPage;
	NewUxInstitutionPage ipage;
	NewUxMyProfile myProfile;
	NewUxCommunityPage communityPage;
    NewUxSelfRegistrationPage register;
    NewUxClassicTestPage pltTest;
    RegistrationClassesPage registrationClassesPage;
    RegistrationStudentsPage registrationStudentsPage;
    RegistrationTeachersPage registrationTeachersPage;
    RegistrationInstitutionsPage registrationInstitutionsPage;
    RegistrationSupervisorsPage registrationSupervisorsPage;

private String userId;

private boolean deleteGroup;
private boolean deleteUser;
private boolean deleteImportedUser;
private boolean deleteImportedTeacher;
private boolean deleteImportedClass;
private boolean deletClass;
private boolean deletInstitution;
private boolean deletePackage;
private String firstClass;
private String secondClass;
private String groupName;
private String classId;
//private String institutionId;

String[] classes= new String[2];
String[] importedClassIds = new String[classes.length];
List<String> wList = new ArrayList<String>();


	@Before
	public void setup() throws Exception {
		institutionName = institutionsName[0];
		super.setup();

		// Initialize registration students page
		registrationStudentsPage = new RegistrationStudentsPage(webDriver,testResultService);
		registrationClassesPage = new RegistrationClassesPage(webDriver,testResultService);
		registrationTeachersPage = new RegistrationTeachersPage(webDriver,testResultService);
		registrationSupervisorsPage = new RegistrationSupervisorsPage(webDriver,testResultService);
	}

	@Test
	@TestCaseParams(testCaseID = {"39284,46250,46158"})
	public void testCreateNewClass() throws Exception {
		
	report.startStep("Init Test Data");
		String className = "class" + dbService.sig(5);	
		//String institutionId = autoInstitution.getInstitutionId();
		
	report.startStep("Login as Admin");
		loginPage = new NewUXLoginPage(webDriver, testResultService);
		tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
		homePage.waitUntilLoadingMessageIsOver();
		
	// Initialize registration classes page
	//	registrationClassesPage = new RegistrationClassesPage(webDriver, testResultService);
		
	report.startStep("Click on Registration -> Classes");
		tmsHomePage.waitForPageToLoad();
		registrationClassesPage.goToRegistrationClasses();
	
	report.startStep("Add New Class");
		registrationClassesPage.addClass(className);

		deletClass=true;
		sleep(1);
		
	report.startStep("Validate Class was Created");
		tmsHomePage.checkClassNameIsDisplayed(className);
		
	report.startStep("Retrieve Class ID");
		classId = dbService.getClassIdByName(className, institutionId);

		if (classId!=null)
			dbService.insertClassToAutomationTable(classId,className,null,institutionId,null,pageHelper.CILink);

	report.startStep("Open new Class info card, Validate Name and Edit Name");
		String newClassName = registrationClassesPage.validateClassDetailsAndEdit(className);
	
	report.startStep("Verify Class Name was Updated");	
		tmsHomePage.checkClassNameIsDisplayed(newClassName);
	
	report.startStep("Delete Class");	
		registrationClassesPage.deleteClass(classId);
		
	report.startStep("Validate Class was Deleted");	
		webDriver.refresh();
		registrationClassesPage.goToRegistrationClasses();
		boolean deleteClasstatus = tmsHomePage.checkrowIsNotDisplayed(classId); // Get the status if class removed
		if (!deleteClasstatus)
			deletClass=false; // Indication to delete the class
	
}

	@Test
	@TestCaseParams(testCaseID = {""})
	public void testCreateNewStudentAndVerifyLicenses() throws Exception {

		try {
			report.startStep("Init Test Data");
			String buildId = pageHelper.buildId;
			String targetClass = configuration.getProperty("classname.nocourses");
			String targetClassId = dbService.getClassIdByName(targetClass, institutionId);
			String userName = "stud" + dbService.sig(5);
			String className = configuration.getProperty("classname.nostudents");

			report.startStep("Login as Admin");
			loginPage = new NewUXLoginPage(webDriver, testResultService);
			pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(configuration.getProperty("schoolAdmin.user"), institutionId));
			tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
			//sleep(4);
			homePage.waitUntilLoadingMessageIsOver();

			report.startStep("Store remaining licenses before creating new user");
			List<String[]> classPackages = dbService.getClassAssignPackagesNew(className, institutionId);
			List<String[]> baseLineLicense = dbService.getRemainingLicensesOfCertainPackage(classPackages, institutionId);

			/*report.startStep("Store remaining licenses before creating new user");
			List<String[]> ValidInstitutionPackages = dbService.getValidInstitutionPackages(institutionId);*/

			report.startStep("Click on Registration -> Students");
			registrationStudentsPage = new RegistrationStudentsPage(webDriver, testResultService);
			registrationStudentsPage.goToRegistrationStudents();

			report.startStep("Select Class");
			tmsHomePage.selectClass(className,true,true);

			report.startStep("Enter student details and click Add");
			tmsHomePage.enterStudentDetails(userName);//add email to student

		/*report.startStep("Get institution Remaining License for all Valid Packages");
		List<String[]> getValidInstitutionPackages = dbService.getValidInstitutionPackages(institutionId);*/

			report.startStep("Enter student details and click Add");
			registrationStudentsPage.waitUntilStudentIsAdded(userName);

			report.startStep("Retrieve student ID");
			studentId = dbService.getUserIdByUserName(userName, institutionId);
			if (studentId != null)
				dbService.insertUserToAutomationTable(institutionId,studentId,userName,targetClass,buildId);

			report.startStep("Get institution License after adding new student and Verify");
			sleep(5);
			List<String[]> currentLicense = dbService.getRemainingLicensesOfCertainPackage(classPackages, institutionId);
			pageHelper.validateLicensesAfterUserAddedOrLoggedIn(baseLineLicense, currentLicense,1,0);

		/*if (getValidInstitutionPackages!=null)
			pageHelper.checkRemainingLicenseBurnForallValidPackages(getValidInstitutionPackages,institutionId,false);*/

			report.startStep("Open Info card, Verify the Student was Added, Edit Student Name");
			String newStudentName = registrationStudentsPage.validateStudentWasAddedAndEditName(userName);

			report.startStep("Verify the new User Name");
			registrationStudentsPage.waitUntilStudentIsAdded(newStudentName);
			sleep(3);

			report.startStep("LogOut from TMS");
			tmsHomePage.clickOnExitTMS();

			report.startStep("Login with student to ED");
			loginPage = new NewUXLoginPage(webDriver, testResultService);
			loginPage.waitForPageToLoad();
			password = dbService.getUserNameAndPasswordByUserId(studentId).get(0)[1];
			webDriver.getWebDriver().getPageSource();
			homePage = loginPage.loginAsStudent(newStudentName, password);

			pageHelper.skipOptin();
			homePage.closeAllNotifications();
			pageHelper.skipOnBoardingHP();
			homePage.waitHomePageloadedFully();

			report.startStep("Get institution License after student login and Verify");
			currentLicense = dbService.getRemainingLicensesOfCertainPackage(classPackages, institutionId);
			pageHelper.validateLicensesAfterUserAddedOrLoggedIn(baseLineLicense, currentLicense,1,1);

			report.startStep("Verify whether student get package-progress");
			String date = pageHelper.getCurrentDateByFormat("yyyy-MM-dd");
			List<String[]> packageProgress = dbService.getUserPackagesProgress(studentId);
			String dayAndTimeOfStartedLicense = packageProgress.get(0)[1];
			testResultService.assertEquals(true, dayAndTimeOfStartedLicense.startsWith(date), "Wrong package progress date");

			report.startStep("LogOut from ED");
			homePage.logOutOfED();

		}catch(Exception|AssertionError err){
			testResultService.addFailTest(err.getMessage(),true,true);
		}
		finally{
			//dbService.deleteStudentByName(institutionId, userName);
		}
	}



	@Test
	@TestCaseParams(testCaseID = {"46260,46267,46268,46186"})
	public void testCreateNewStudent() throws Exception {
		
	report.startStep("Init Test Data");
		String buildId = pageHelper.buildId;
		String targetClass = configuration.getProperty("classname.nocourses");
		String targetClassId = dbService.getClassIdByName(targetClass,institutionId); //configuration.getProperty("institution.id"));
		String userName = "stud" + dbService.sig(4);
		String className = configuration.getProperty("classname.nostudents");
		
	report.startStep("Login as Admin");
		loginPage = new NewUXLoginPage(webDriver, testResultService);
		pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(configuration.getProperty("schoolAdmin.user"),institutionId));
		tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
		//sleep(4);
		homePage.waitUntilLoadingMessageIsOver();

	/*report.startStep("Store remaining licenses before creating new user");
		List<String[]> classPackages = dbService.getClassAssignPackagesNew(className, institutionId);
		List<String> remainingLicensesBeforeAddingUser = dbService.getRemainingLicensesOfCertainPackage(classPackages,institutionId);*/

	/*report.startStep("Store remaining licenses before creating new user");
		List<String[]> ValidInstitutionPackages = dbService.getValidInstitutionPackages(institutionId);*/
		
	// Initialize registration classes page
	//	registrationStudentsPage = new RegistrationStudentsPage(webDriver,testResultService);
				
	report.startStep("Click on Registration -> Students");
		registrationStudentsPage.goToRegistrationStudents();

	report.startStep("Select Class");
		sleep(3);
		tmsHomePage.selectClass(className,true,true);
	
	report.startStep("Enter student details and click Add");
		tmsHomePage.enterStudentDetails(userName);//add email to student

	/*report.startStep("Get institution Remaining License for all Valid Packages");
		List<String[]> getValidInstitutionPackages = dbService.getValidInstitutionPackages(institutionId);

	report.startStep("Get institution Remaining License after adding new student");
		List<String> remainingLicensesAfterAddingUser = dbService.getRemainingLicensesOfCertainPackage(classPackages,institutionId);*/

	report.startStep("Enter student details and click Add");
		//tmsHomePage.enterStudentDetails(userName);
		deleteUser=true;
		registrationStudentsPage.waitUntilStudentIsAdded(userName);
		sleep(5);
		
	report.startStep("Retrieve student ID");
		studentId = dbService.getUserIdByUserName(userName, institutionId);

	/*report.startStep("Check institution Remaining License reduced after login");
		validateLicensesReducesAfterNewStudentAdded(remainingLicensesBeforeAddingUser,remainingLicensesAfterAddingUser);

		if (getValidInstitutionPackages!=null)
			pageHelper.checkRemainingLicenseBurnForallValidPackages(getValidInstitutionPackages,institutionId,false);*/

	report.startStep("Open Info card, Verify the Student was Added, Edit Student Name");
		String newStudentName = registrationStudentsPage.validateStudentWasAddedAndEditName(userName);
		
	report.startStep("Verify the new User Name");
		registrationStudentsPage.waitUntilStudentIsAdded(newStudentName);
		sleep(3);
		
		// Added student move from one class to another. Wasn't covered but part of TC. 1.5.2018
	report.startStep("Move student to different class");
		registrationStudentsPage.moveStudentToOtherClass(studentId, targetClassId);
	
	report.startStep("Switch to different class");
		webDriver.switchToMainWindow();
		webDriver.switchToTopMostFrame();
		tmsHomePage.switchToMainFrame();
		tmsHomePage.selectClass(targetClass, true, true);
		sleep(2);

		registrationStudentsPage.verifyNewStudentName(newStudentName);

		if (studentId != null)
			dbService.insertUserToAutomationTable(institutionId,studentId,newStudentName,targetClass,buildId);

		if (pageHelper.getCILink().contains("engdis.com")){ // beta or production remove the student
			report.startStep("Delete Student");
			registrationStudentsPage.deleteStudent(studentId);

			report.startStep("Verify User was Deleted");
			webDriver.switchToMainWindow();
			tmsHomePage.switchToMainFrame();
			webDriver.switchToFrame("FormFrame");
			tmsHomePage.switchToStudentDetailedPage();
			boolean deleteUserstatus = tmsHomePage.checkrowIsNotDisplayed(studentId); // Get the status if user removed

			if (!deleteUserstatus)
				deleteUser=false; // Indication to delete the user in After
		}


	report.startStep("LogOut from ");
		tmsHomePage.clickOnExitTMS();
	}

	/*private void validateLicensesReducesAfterNewStudentAdded(List<String[]> remainingLicensesBeforeAddingUser, List<String[]> remainingLicensesAfterAddingUser) throws Exception {

		for (int i=0;i<remainingLicensesBeforeAddingUser.size();i++) {
			report.startStep("Verifying licence burned to packageId : "+remainingLicensesBeforeAddingUser.get(i));
			int res = Integer.parseInt(remainingLicensesBeforeAddingUser.get(i)[4])-Integer.parseInt(remainingLicensesAfterAddingUser.get(i)[4]);
			testResultService.assertEquals(1, res, "License didn't burned or burned more than should");
		}
	}*/

	@Test
	@TestCaseParams(testCaseID = {"46252,46253,46251"})
	public void testCreateNewTeacher() throws Exception {
		
		report.startStep("Init Test Data");
		String buildId = pageHelper.buildId;
			String userName = "teacher" + dbService.sig(5);
			//teacherDetailedPage = new TeacherDetailsPage(webDriver,testResultService);
		
		report.startStep("Login as Admin");
			loginPage = new NewUXLoginPage(webDriver, testResultService);
			tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
			homePage.waitUntilLoadingMessageIsOver();
			
		// Initialize registration teachers page	
		//	registrationTeachersPage = new RegistrationTeachersPage(webDriver,testResultService);
			
		report.startStep("Click on Registration -> Teachers");
			registrationTeachersPage.goToRegistrationTeachers();
			
		report.startStep("Add New Teacher");
			registrationTeachersPage.addNewTeacher(userName);
			deleteUser=true; //Flag that instruct the user need to be deleted
			
		report.startStep("Retrieve Teacher ID");	
			userId = dbService.getUserIdByUserName(userName,institutionId);
			
		report.startStep("Open Teacher created info card and Verify Details");
			registrationTeachersPage.openTeacherFormAndVerifyDetails(userName);

		if (studentId != null)
			dbService.insertUserToAutomationTable(institutionId,studentId,userName,null,buildId);
	/*
		report.startStep("Delete Teacher");
			registrationTeachersPage.deleteTeacher(userId);
			
		report.startStep("Verify Teacher was Deleted");
			returnToMainPage();
			webDriver.switchToFrame("FormFrame");
			tmsHomePage.switchToteacherDetailedPage();
			boolean deleteUserstatus = tmsHomePage.checkrowIsNotDisplayed(userId);
			if (!deleteUserstatus) 
				deleteUser=false;
	 */
	}
	
	@Test
	@TestCaseParams(testCaseID = {"45762","30718","46258","46257"})
	public void testCreateNewSupervisor() throws Exception {
		
		report.startStep("Init Test Data");
			String buildId = pageHelper.buildId;
			String supervisorName = "supervisor" + dbService.sig(5);
		
		report.startStep("Login as Admin");
			loginPage = new NewUXLoginPage(webDriver, testResultService);
			tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
			//sleep(4);
			homePage.waitUntilLoadingMessageIsOver();
			
		// Initialize registration supervisors page
		//	registrationSupervisorsPage = new RegistrationSupervisorsPage(webDriver,testResultService);
			
		report.startStep("Click on Registration -> Supervisors");
			registrationSupervisorsPage.goToRegistrationSupervisors();
		
		report.startStep("Add New Supervisor");
			registrationSupervisorsPage.addNewSupervisor(supervisorName);
			deleteUser=true;
			
		report.startStep("Retrieve Supervisor ID");
			studentId = dbService.getUserIdByUserName(supervisorName, institutionId);
			returnToMainPage();
			
		report.startStep("Verify Supervisor Details");
			registrationSupervisorsPage.verifySupervisorDetails(userId, supervisorName);
			returnToMainPage();
			sleep(2);

		if (studentId != null)
			dbService.insertUserToAutomationTable(institutionId,studentId,supervisorName,null,buildId);
/*
		report.startStep("Delete Supervisor");
			registrationSupervisorsPage.deleteSupervisor(studentId);
			
		report.startStep("Verify Supervisor was Deleted");
			returnToMainPage();
			webDriver.switchToFrame("FormFrame");
			tmsHomePage.switchToteacherDetailedPage();
			boolean deleteUserstatus = tmsHomePage.checkrowIsNotDisplayed(studentId);
			if (deleteUserstatus)
				deleteUser=true;
 */
	}

	@Test
	@TestCaseParams(testCaseID = {"46249","46248,46160","46161"})
	public void testCreateNewGroup() throws Exception {
		
		report.startStep("Init Test Data");
			groupName = "Group" + dbService.sig(4); // Name of the group about to be created
		
		report.startStep("Login as Admin");
			loginPage = new NewUXLoginPage(webDriver, testResultService);
			tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
			//sleep(4);
			homePage.waitUntilLoadingMessageIsOver();
			
		// Initialize registration classes page
		//	registrationClassesPage = new RegistrationClassesPage(webDriver,testResultService);
		
		report.startStep("Click on Registration -> Classes");
			registrationClassesPage.goToRegistrationClasses();

		report.startStep("Get first and 3th class name");
			String className1 = registrationClassesPage.classListName.get(1).getText();
			String className3 = registrationClassesPage.classListName.get(3).getText();

			firstClass = dbService.getClassIdByName(className1,institutionId);
			secondClass = dbService.getClassIdByName(className3,institutionId);

		report.startStep("Add new Group to nocourses Class");
			registrationClassesPage.addNewGroupToSpecificClass(firstClass, groupName);
			
		report.startStep("Validate Group '"+groupName+"' Is Displayed in Class '"+className1+"'");
			boolean isGroupDisplayed = registrationClassesPage.validateGroupIsDisplayedInClass(firstClass, groupName);
			if (isGroupDisplayed) {
				deleteGroup = true;
			}
			
		report.startStep("Edit newly Created Group Name");	// Added TC 46161
			groupName = registrationClassesPage.editGroup(firstClass, groupName);
			
		report.startStep("Move Group to different class");
			registrationClassesPage.moveGroupToDifferentClass(firstClass, secondClass);
			sleep(2);
			
		report.startStep("Validate Group '"+groupName+"' Is Displayed in Class '"+className3+"'");
			registrationClassesPage.validateGroupIsDisplayedInClass(secondClass, groupName);
			
		report.startStep("Delete Group");
			registrationClassesPage.deleteGroup(secondClass, groupName);
			sleep(2);

		report.startStep("Verify Group deletion");
			boolean isGroupNotDisplayed = registrationClassesPage.validateGroupIsNotDisplayedInClass(secondClass, groupName);
			if (isGroupNotDisplayed) {
				deleteGroup = false;
			}	
	}

	private void returnToMainPage() throws Exception{
		webDriver.switchToMainWindow();
		tmsHomePage.switchToMainFrame();
	}

//	@Test
	@Category(AngularLearningArea.class)
	@TestCaseParams(testCaseID = { "46272" })
	public void testImportNewStudents() throws Exception {

		report.startStep("Init test data");
		String buildId = pageHelper.buildId;
		String Path = pageHelper.buildPathForExternalPages + "Languages\\";
		String fileNameToSave = "StudentsToImport_" + dbService.sig(4) + ".txt";
		//String fileFullPath = "\\\\" + configuration.getGlobalProperties("logserverName") + "\\AutoLogs\\ToolsAndResources\\Shared\\" + fileNameToRead;
		String classForImport = configuration.getProperty("classname.nostudents");

		report.startStep("Create 3 new user name to import and save the file");
		 //students = new String[3];
		String[] students  = defineMultipleUserNames("ImportS",3);

		String filePath = Path +  fileNameToSave;
				 //"\\\\CI-SRV\\BuildsArtifacts\\EDUI_CI_dev_20231228.2\\Languages\\" + fileNameToSave;
		textService.generateTxtToImportStudents(students, filePath, classForImport);

		report.startStep("Login as Admin");
		loginPage = new NewUXLoginPage(webDriver, testResultService);
		pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(configuration.getProperty("schoolAdmin.user"), institutionId));
		tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
		sleep(1);
		homePage.waitUntilLoadingMessageIsOver();

		report.startStep("Click on Registration");
		tmsHomePage.switchToMainFrame();
		tmsHomePage.clickOnRegistration();
		sleep(1);

		report.startStep("Click on Students");
		tmsHomePage.clickOnStudents();
		sleep(1);

		report.startStep("Click On Import");
		tmsHomePage.clickOnPromotionAreaMenuButton("Import");
		sleep(3);
		webDriver.switchToPopup();

		try {
			report.startStep("Attach File and close popup");
			tmsHomePage.importFileForRegistration(filePath);
			deleteImportedUser = true;

			report.startStep("Verify student exists in class");
			sleep(3);
			webDriver.switchToMainWindow();
			tmsHomePage.switchToMainFrame();
			webDriver.switchToFrame("FormFrame");

			report.report("About to select class from combobox");
			tmsHomePage.selectClass(classForImport, false, true);

			report.report("Finished selecting class from combobox");
			tmsHomePage.switchToStudentDetailedPage();

			report.report("Verify the students imported and display in  students page");

			for (int i = 0; i < students.length; i++) {
				registrationStudentsPage.verifyUsersExistsInRegistrationPage(registrationStudentsPage.studentNames,students[i]);

				studentId = dbService.getUserIdByUserName(students[i],institutionId);
				if (studentId != null)
					dbService.insertUserToAutomationTable(institutionId,studentId,students[i],classForImport,buildId);
				}

			report.report("Verify License after importing students");
			/*List<String[]> currentLicense = dbService.getRemainingLicensesOfCertainPackage(classPackages, institutionId);
			pageHelper.validateLicensesAfterUserAddedOrLoggedIn(baseLineLicense,currentLicense,students.length,0);*/

		}
		catch (Exception|AssertionError err){
		System.out.println(err.getMessage());
		}

		if (pageHelper.getCILink().contains("engdis.com")){

			report.startStep("Delete Imported Students");
			for (int i=0;i<students.length;i++){
				webDriver.waitForElement("//*[@id='tblBody']/tr["+(i+1)+"]/td[2]", ByTypes.xpath).click();
			}

			webDriver.switchToTopMostFrame();
			webDriver.switchToFrame("mainFrame");
			tmsHomePage.clickOnPromotionAreaMenuButton("Delete");
			webDriver.closeAlertByAccept();
			sleep(1);
			webDriver.checkElementNotExist("//*[@id='tblBody']/tr/td[2]");
			deleteImportedUser = false;
		}
	}

	private String[] defineMultipleUserNames(String prefix,int count) throws Exception {

		String[] students = new String[count];

		for (int i=0; i<count; i++) {
			userName = prefix + dbService.sig(4);
			Thread.sleep(800);
			students[i] = userName;
		}
		return students;
	}

	private void copyFile(String path,String content) {

		try {
			FileWriter myWriter = new FileWriter(path);
			myWriter.write(content);
			myWriter.close();
			System.out.println("Successfully wrote to the file.");
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}

	}

	@Test
	@TestCaseParams(testCaseID = { "90150"})
	public void testAdd_TOEICcourses_To_ED_Excellence() throws Exception{
		
		report.startStep("initilaze data");
			institutionName = institutionsName[16];
			pageHelper.initializeData();
			pageHelper.closeBrowserAndOpenInCognito(false);

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
	
			
		report.startStep("Validate all TOEIC courses available in ED");
			//int count = 0;
			String actualCourse = homePage.getCurrentCourseName();
			String currCourse = actualCourse;
			List<String> allCoursesDisplayed = new ArrayList<>();
			
			do{
				if(currCourse.equals(coursesNamesTOEIC[0])
						||currCourse.equals(coursesNamesTOEIC[1])
						||currCourse.equals(coursesNamesTOEIC[2])) {
					allCoursesDisplayed.add(currCourse);
					}
				homePage.carouselNavigateNext();
				sleep(1);
				homePage.waitHomePageloadedFully();
				currCourse = homePage.getCurrentCourseName();
				
			}while(!actualCourse.equals(currCourse));
			textService.assertEquals("Wrong course name or course not assigned to EDExcellence", coursesNamesTOEIC.length, allCoursesDisplayed.size());
			
		report.startStep("LogOut from Ed and enter to TMS");
			homePage.clickOnLogOut();
			pageHelper.restartBrowserInNewURL(institutionName, true);
			loginPage = new NewUXLoginPage(webDriver, testResultService);
			pageHelper.setUserLoginToNull(dbService.getUserIdByUserName("admin",institutionId));
			tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
			homePage.closeAllNotifications();
			homePage.waitUntilLoadingMessageIsOver();
		
		report.startStep("Go to CurriculumAssignCoursesPage");
			tmsHomePage.switchToMainFrame();
			tmsHomePage.clickOnCurriculum();
			tmsHomePage.clickOnAssignCourses();
			//tmsHomePage.switchToFormFrame();
			sleep(1);
			tmsHomePage.selectClass(newClass, true, true);
			CurriculumAssignCoursesPage curriculumAssignCoursesPage = new CurriculumAssignCoursesPage(webDriver, testResultService);
			curriculumAssignCoursesPage.openOrCloseSpecificCourse(coursesNamesTOEIC[0]);
			sleep(1);
			
		report.startStep("Unassign one of the unit at TOEIC " +coursesNamesTOEIC[0]);
			curriculumAssignCoursesPage.markCheckBoxOfSpecificUnit("Personnel");
			tmsHomePage.clickOnSave();
			sleep(2);
		
		report.startStep("Exit of TMS and login to ED");
			tmsHomePage.clickOnExitTMS();
			sleep(1);
			webDriver.openUrl(regUserUrl);
			pageHelper.skipOptin();
			homePage.closeAllNotifications();
			//pageHelper.skipOnBoardingHP();
			//homePage.waitHomePageloadedFully();
			
		report.startStep("Navigate to course "+coursesNamesTOEIC[0]);	
			homePage.navigateToRequiredCourseOnHomePage(coursesNamesTOEIC[0]);
			
		report.startStep("Find locked unit and verify locked-label");		
			WebElement unitLockedSign = homePage.getUnitLockedElement(1,true);
			WebElement unitLocedSignParent = webDriver.waitForElement("home__detailsTitle-"+1, ByTypes.id);
			webDriver.scrollToElement(unitLocedSignParent, 0);
			String unitLockedLabel = unitLockedSign.getAttribute("title");
			testResultService.assertEquals("Locked", unitLockedLabel);
		
		report.startStep("Logout from ED");		
			homePage.logOutOfED();
		}
		catch(Exception e) {
			e.printStackTrace();
		}finally{
			
		report.startStep("Cleaning test data. Delete student and class");		
			dbService.deleteStudentByName(institutionId, userName);
			dbService.deleteClassByName(institutionId, newClass);
		}
		
	}
	
	/*
	@Test
	@Category(AngularLearningArea.class)
	@TestCaseParams(testCaseID = { ""})
	public void testAutoAssignmentToeicBridgeAsPLTAfterImportNewStudent() throws Exception {
		
	
	report.startStep("Generate new student and savi it in .txt file ");	
		String i = dbService.sig(4);
		userName = "St"+i;
		String fileName = "NewStudent"+i+".txt";
		//String filePath = "C:\\Users\\igorqt\\eclipse-workspace IDE\\EduAutomation___\\files\\ImportStudents\\"+fileName;
		String filePath = "smb://"+configuration.getGlobalProperties("logserverName")+"//AutoLogs//ToolsAndResources//Shared//ToeicStudentsToImport//"+fileName;
		generateNewTxtFileForImportSudents(userName,filePath);
			
	report.startStep("Login as Admin");
		pageHelper.restartBrowserInNewURL(institutionsName[16], true); 
		loginPage = new NewUXLoginPage(webDriver, testResultService);
		//institutionId = dbService.getInstituteIdByName(institutionsName[16]);
		pageHelper.setUserLoginToNull(dbService.getUserIdByUserName("admin",institutionId));
		tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
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
		//tmsHomePage.importFileForRegistration("\\\\"+configuration.getGlobalProperties("logserverName")+"\\Shared\\ToeicStudentsToImport\\stud1.txt");
		tmsHomePage.importFileForRegistration("\\\\"+configuration.getGlobalProperties("logserverName")+"\\AutoLogs\\ToolsAndResources\\ToolsAndResources\\Shared\\ToeicStudentsToImport\\"+fileName);
		deleteImportedUser = true;
		
	report.startStep("Verify that student assigned to Toeic bridge test");
		String authorizationKey = dbService.verifyStudentAssignedToToeicBridge(userName);
		textService.assertNotNull("Student wasn't assigned to the ToeicBridgePLT test", authorizationKey);
		
	report.startStep("Verify that student was assigned to correct testFormID");
		String studId = dbService.getUserIdByUserName(userName, dbService.getInstituteIdByName(institutionsName[16]));
		String formID = dbService.getTestFormIdFromToeicDB(studId);
		textService.assertEquals("Wrong formID", "4OTB5", formID);
		
	report.startStep("Exit from");	
		tmsHomePage.clickOnExitTMS();
		
		
	}

	private void generateNewTxtFileForImportSudents(String userName, String filePath) throws IOException {
		List<String> fileContains = new ArrayList<>();
		fileContains.add("FirstName	LastName	UserName	Password	Gender	Email	Class");
		fileContains.add(userName+"	"+userName+"	"+userName+"	12345	u	"+userName+"@mail.com	class200");
		//textService.writeTextToFileWithSeveralLines(filePath, fileContains);
		textService.writeListToSmbFile(filePath, fileContains, netService.getDomainAuth());
	}
*/

	@Test
	@Category(AngularLearningArea.class)
	@TestCaseParams(testCaseID = { "44234","46255" })
	public void testImportNewTeacher() throws Exception {
		
		report.startStep("Init test data");
			String fileName = "TeachersToImport_" + dbService.sig(4)+ ".txt";
		//	String fileFullPath = "\\\\"+configuration.getGlobalProperties("logserverName")+"\\AutoLogs\\ToolsAndResources\\Shared\\" + fileName;

		report.startStep("Init test data");
		String buildId = pageHelper.buildId;
		String Path = pageHelper.buildPathForExternalPages + "Languages\\";
		String fileNameToSave = "TeachersToImport_" + dbService.sig(4) + ".txt";
		//String fileFullPath = "\\\\" + configuration.getGlobalProperties("logserverName") + "\\AutoLogs\\ToolsAndResources\\Shared\\" + fileNameToRead;

		report.startStep("Create 2 new teachers name to import and save the file");
		String[] teachers = defineMultipleUserNames("Import",2);

		String filePath = Path +  fileNameToSave;
		textService.generateTxtToImportTeachers(teachers, filePath);

		report.startStep("Login as Admin");
			loginPage = new NewUXLoginPage(webDriver, testResultService);
			tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
			//sleep(5);
			homePage.waitUntilLoadingMessageIsOver();
		
		report.startStep("Click on Registration");
			tmsHomePage.switchToMainFrame();
			tmsHomePage.clickOnRegistration();
			sleep(5);
			
		report.startStep("Click on Teachers");
			tmsHomePage.clickOnTeachers();
			sleep(5);
			
		report.startStep("Click On Import");
			tmsHomePage.clickOnPromotionAreaMenuButton("Import");
			sleep(1);
			webDriver.switchToPopup();
			
		report.startStep("Attach File and close popup");
			deleteImportedTeacher = true;	
			tmsHomePage.importFileForRegistration(filePath);
			
			
		report.startStep("Refresh Teachers List");
			sleep(3);
			webDriver.switchToMainWindow();
			tmsHomePage.switchToMainFrame();
			tmsHomePage.clickOnRegistration();
			sleep(2);
			tmsHomePage.clickOnTeachers();
			sleep(2);
						
		report.startStep("Verify teacher exists in class");	
			tmsHomePage.switchToteacherDetailedPage();

		// Initialize registration students page
		//registrationStudentsPage = new RegistrationStudentsPage(webDriver,testResultService);
		// Initialize registration teachers page
		//registrationTeachersPage = new RegistrationTeachersPage(webDriver,testResultService);

			for (int i = 0; i < teachers.length; i++) {
				registrationStudentsPage.verifyUsersExistsInRegistrationPage(registrationTeachersPage.teacherNames,teachers[i]);

				studentId = dbService.getUserIdByUserName(teachers[i],institutionId);
				if (studentId != null)
					dbService.insertUserToAutomationTable(institutionId,studentId,teachers[i],null,buildId);
			}

			if (pageHelper.getCILink().contains("engdis.com")) {

				String teacherId1 = dbService.getUserIdByUserName(teachers[0], institutionId);
				String teacherId2 = String.valueOf(Long.parseLong(teachers[1]) + 1);
				//String teacherId3 = String.valueOf(Long.parseLong(teacherId2) + 1);
				String ImportedUserName = webDriver.waitForElement("un" + teacherId1, ByTypes.id).getText();
				testResultService.assertEquals(teachers[0], ImportedUserName);

				report.startStep("Delete Imported Teachers");
				webDriver.waitForElement("Check$" + teacherId1, ByTypes.id).click();
				webDriver.waitForElement("Check$" + teacherId2, ByTypes.id).click();
				//	webDriver.waitForElement("Check$" + teacherId3, ByTypes.id).click();
				webDriver.switchToTopMostFrame();
				webDriver.switchToFrame("mainFrame");
				tmsHomePage.clickOnPromotionAreaMenuButton("Delete");
				webDriver.closeAlertByAccept();
				sleep(1);
				webDriver.checkElementNotExist("//*[@id='Check$" + teacherId2 + "']");

				deleteImportedTeacher = false;
			}
	}
	
	@Test
	@Category(AngularLearningArea.class)
	@TestCaseParams(testCaseID = { "46227" })
	public void testImportNewClass() throws Exception {

		report.startStep("Init test data");
			String fileName = "ClassesToImport" + dbService.sig(4) +".txt"; 
			String fileFullPath = pageHelper.buildPathForExternalPages + "languages\\" + fileName;
			classes = defineMultipleUserNames("ImporedtClass",2);
			textService.generateTxtToImportClass(classes, fileFullPath);
		
		report.startStep("Login as Admin");
			loginPage = new NewUXLoginPage(webDriver, testResultService);
			tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
			tmsHomePage.waitForPageToLoad();
			homePage.waitUntilLoadingMessageIsOver();
		
		report.startStep("Click on Registration");
			tmsHomePage.switchToMainFrame();
			tmsHomePage.clickOnRegistration();
			sleep(1);
			
		report.startStep("Click on Classes");
			tmsHomePage.clickOnClasses();
			sleep(1);
			
		report.startStep("Click On Import");
			tmsHomePage.clickOnPromotionAreaMenuButton("Import");
			sleep(1);
			webDriver.switchToPopup();
			
		report.startStep("Attach File and close popup");
			tmsHomePage.importFileForRegistration(fileFullPath);
			deleteImportedClass = false;
			
		report.startStep("Refresh Classes List");
			sleep(2);
			webDriver.switchToMainWindow();
			tmsHomePage.switchToMainFrame();
			tmsHomePage.clickOnRegistration();
			sleep(1);
			tmsHomePage.clickOnClasses();
			sleep(1);
						
		report.startStep("Verify class exists in institution and check its combobox");
		
			tmsHomePage.switchToteacherDetailedPage();
			String ImportedClassName="";
			
			for (int i=0;i<classes.length;i++){
				importedClassIds[i] = dbService.getClassIdByName(classes[i], institutionId);
				//testResultService.assertEquals(classes[i], ImportedClassName);
				if (importedClassIds[i] != null)
					dbService.insertClassToAutomationTable(importedClassIds[i],classes[i],null,institutionId,null,pageHelper.CILink);
				else
					testResultService.addFailTest("class" + classes[i] + "not created",false,false);

				//ImportedClassName = webDriver.waitForElement("//*[@id='ClassName" + importedClassIds[i] + "']", ByTypes.xpath).getText();
				//webDriver.waitForElement("//*[@id='Check$" + importedClassIds[i] + "']", ByTypes.xpath).click();
			}

			webDriver.switchToTopMostFrame();
			webDriver.switchToFrame("mainFrame");
			tmsHomePage.clickOnPromotionAreaMenuButton("Delete");
			webDriver.closeAlertByAccept();
			sleep(1);
			
			boolean status=false;
			for (int i=0;i<classes.length;i++){
				status = webDriver.checkElementAndResponseIfExists("//*[@id='ClassName" + importedClassIds[i] + "']");
				
				if (status){
					deleteImportedClass = true;
					break;
				}
			}
	}
	/*
	private List<String> createImportClass(String fileFullPath) throws Exception {
		
		
		try {
			
			for (int i=0;i<classes.length;i++){
				classes[i]= "ImporedtClass" + dbService.sig(4); //.toString();
				wList.add(classes[i]);
				Thread.sleep(500);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return wList;
	}
*/

	@Test
	@TestCaseParams(testCaseID = { "46254" })
	public void testExportTeachers() throws Exception {
		
		report.startStep("Init test data");
				//String institutionId = configuration.getProperty("institution.id");
				
		report.startStep("Login as Admin");
			loginPage = new NewUXLoginPage(webDriver, testResultService);
			tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
			//sleep(5); 
			homePage.waitUntilLoadingMessageIsOver();

		report.startStep("Click on Teachers");
		registrationTeachersPage.goToRegistrationTeachers();
		//sleep(3);

		/*
		report.startStep("Click on Registration");
			tmsHomePage.switchToMainFrame();
			tmsHomePage.clickOnRegistration();
			sleep(5);
		*/

		report.startStep("Select All Teachers");
			tmsHomePage.switchToteacherDetailedPage();
			String teacherToVerify = registrationTeachersPage.teacherNames.get(0).getText();
			WebElement element = webDriver.getElement(By.xpath("//*[@id='con']"));
			for (int i = 1 ; element != null ; i++)
			{
				String xPathExpression = "//*[@id='con']/tr[" + i + "]/td[2]";
				try {
					element = element.findElement(By.xpath(xPathExpression));
				}
				catch (Exception e){
					element = null;
				}
				if (element != null)
				{
					element.click();
					element = webDriver.getElement(By.xpath("//*[@id='con']"));
				}
			}
		
		report.startStep("Click On Export");	 
			registrationStudentsPage.exportTool.click();
			sleep(1);
			webDriver.switchToPopup();
			
		report.startStep("Export As txt file");
			List<WebElement> chkRbtns = webDriver.getElementsByXpath("//td/input");
			chkRbtns.get(0).click();
			registrationStudentsPage.exportButton.click();
			sleep(1);
			webDriver.waitForElement("//td/a[contains(text(),'" + institutionId + ".txt')]", ByTypes.xpath).click();
			
		report.startStep("Examine created txt file");
			webDriver.switchToPopup();
			webDriver.waitForElement("/html/body/pre[contains(text(),'"+teacherToVerify+"')]", ByTypes.xpath, 15, true, "Student wasn't found in export");
			webDriver.closeNewTab(2);
			
		report.startStep("Export As csv file");
			webDriver.switchToNewWindow();
			chkRbtns = webDriver.getElementsByXpath("//td/input");
			chkRbtns.get(1).click();
			registrationStudentsPage.exportButton.click();
			sleep(1);
			webDriver.waitForElement("//td/a[contains(text(),'" + institutionId + ".csv')]", ByTypes.xpath);
			webDriver.closeNewTab(1);
			sleep(1);
	}
	
	@Test
	@TestCaseParams(testCaseID = { "46234" })
	public void testExportClasses() throws Exception {
		
		report.startStep("Init test data");
			//	String institutionId = configuration.getProperty("institution.id");
				
		report.startStep("Login as Admin");
			loginPage = new NewUXLoginPage(webDriver, testResultService);
			tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
			tmsHomePage.waitForPageToLoad();
			homePage.waitUntilLoadingMessageIsOver();
		
		report.startStep("Click on Registration");
			tmsHomePage.switchToMainFrame();
			tmsHomePage.clickOnRegistration();
			//sleep(5);
			
		report.startStep("Click on Classes");
			tmsHomePage.clickOnClasses();
			sleep(3);

			String[] exportDetails = new String[2];
			exportDetails[0] = registrationClassesPage.classListName.get(1).getText();
			exportDetails[1] = dbService.getUserByInstitutionIdAndClassName(institutionId,exportDetails[0]).get(0)[0];

		report.startStep("Select All Classes");
			tmsHomePage.switchToteacherDetailedPage();
			WebElement element = webDriver.getElement(By.xpath("//*[@id='MainContianer']"));
			for (int i = 4 ; element != null ; i+=2 )
			{
				String xPathExpression = "//*[@id='MainContianer']/table[" + i + "]/tbody/tr/td[2]";
				try {
					element = element.findElement(By.xpath(xPathExpression));
				}
				catch (Exception e){
					element = null;
				}
				if (element != null)
				{
					element.click();
					element = webDriver.getElement(By.xpath("//*[@id='MainContianer']"));
				}
			}
		
		report.startStep("Click On Export");	 
			registrationStudentsPage.exportTool.click();
			sleep(1);
			webDriver.switchToPopup();
			
		report.startStep("Export As txt file");
			List<WebElement> chkRbtns = webDriver.getElementsByXpath("//td/input");
			chkRbtns.get(0).click();
			registrationStudentsPage.exportButton.click();
			sleep(1);
			webDriver.waitForElement("//td/a[contains(text(),'" + institutionId + ".txt')]", ByTypes.xpath).click();
			
		report.startStep("Examine created txt file");
					//"stForAssessment";
			webDriver.switchToPopup();

			for (int i=0;i<exportDetails.length;i++){
				WebElement studentInTxtFile = webDriver.waitForElement("/html/body/pre[contains(text(),'"+exportDetails[i]+"')]", ByTypes.xpath, 15, false, "Student wasn't found in export");
				testResultService.assertEquals(true, studentInTxtFile != null, "text: "+exportDetails[i]+" wasn't found in export");
			}
			webDriver.closeNewTab(2);
			
		/*	This is in comment as long as bug 48703 is still active
		report.startStep("Export As csv file");
			webDriver.switchToNewWindow();
			chkRbtns = webDriver.getElementsByXpath("//td/input");
			chkRbtns.get(1).click();
			webDriver.waitForElement("//input[@value='  Export  ']", ByTypes.xpath).click();
			sleep(1);
			webDriver.waitForElement("//td/a[contains(text(),'" + institutionId + ".csv')]", ByTypes.xpath);
			webDriver.closeNewTab(1);
			sleep(1);
		*/
		
		report.startStep("Export As csv file");
			webDriver.closeNewTab(1);
			tmsHomePage.switchToteacherDetailedPage();
			registrationStudentsPage.exportTool.click();
			sleep(1);
			webDriver.switchToPopup();
			chkRbtns = webDriver.getElementsByXpath("//td/input");
			chkRbtns.get(1).click();
			registrationStudentsPage.exportButton.click();
			sleep(1);
			webDriver.waitForElement("//td/a[contains(text(),'" + institutionId + ".csv')]", ByTypes.xpath);
			webDriver.closeNewTab(1);
			sleep(1);
	}
	
	@Test
	@TestCaseParams(testCaseID = { "46273" })
	public void testExportStudents() throws Exception {
		
		report.startStep("Init test data");
			//String institutionId = configuration.getProperty("institution.id");
			String className = configuration.getProperty("classname.progress");
			String classId = dbService.getClassIdByName(className,institutionId);
				
		report.startStep("Login as Admin");
			loginPage = new NewUXLoginPage(webDriver, testResultService);
			tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
			//sleep(4);
			homePage.waitUntilLoadingMessageIsOver();
		
		report.startStep("Click on Registration");
			tmsHomePage.switchToMainFrame();
			tmsHomePage.clickOnRegistration();
			sleep(2);
			
		report.startStep("Click on Students");
			tmsHomePage.clickOnStudents();
			sleep(5);
		
		report.startStep("Select Class to Export and Press GO");
			webDriver.switchToFrame("FormFrame");
			tmsHomePage.selectClass(className, false, true);
			tmsHomePage.switchToStudentDetailedPage();
			String userNameToVerify = registrationStudentsPage.studentNames.get(0).getText();

		report.startStep("Select All Students In Class");
			WebElement element = webDriver.getElement(By.xpath("//*[@id='tblBody']"));
			for (int i = 1 ; element != null ; i++ )
			{
				String xPathExpression = "//*[@id='tblBody']/tr[" + i + "]/td[2]";
				try {
					element = element.findElement(By.xpath(xPathExpression));
				}
				catch (Exception e){
					element = null;
				}
				if (element != null)
				{
					element.click();
					element = webDriver.getElement(By.xpath("//*[@id='tblBody']"));
				}
			}
			clickonExport();

		report.startStep("Export As txt file");
			List<WebElement> chkRbtns = webDriver.getElementsByXpath("//td/input");
			chkRbtns.get(0).click();
			registrationStudentsPage.exportButton.click();
			sleep(3);
			webDriver.waitForElement("//td/a[contains(text(),'" + classId + ".txt')]", ByTypes.xpath).click();

		report.startStep("Examine created txt file");

			webDriver.switchToPopup();
			webDriver.waitForElement("/html/body/pre[contains(text(),'"+userNameToVerify+"')]", ByTypes.xpath, 15, true, "Student:" + userNameToVerify+ " wasn't found in export");
			webDriver.closeNewTab(2);
			
		report.startStep("Export As csv file");
			webDriver.switchToNewWindow();
			chkRbtns = webDriver.getElementsByXpath("//td/input");
			chkRbtns.get(1).click();
			registrationStudentsPage.exportButton.click();
			sleep(1);
			webDriver.waitForElement("//td/a[contains(text(),'" + classId + ".csv')]", ByTypes.xpath);
			webDriver.closeNewTab(1);
			sleep(1);
		
	}

	private void clickonExport() throws Exception {

		report.startStep("Click On Export");
			webDriver.switchToTopMostFrame();
			tmsHomePage.switchToMainFrame();
			//tmsHomePage.clickOnPromotionAreaMenuButton("Export");
			registrationStudentsPage.exportTool.click();
			sleep(1);
			webDriver.switchToPopup();
	}


	@Test
	@TestCaseParams(testCaseID = { "46130","46131","46132" })
	public void testCreateNewInstitution() throws Exception {
		
		report.startStep("Init Test Data");
		String institutionName = "inst" + dbService.sig(3);
		
		report.startStep("Close ED and Open TMS");
			tmsHomePage = pageHelper.closeEDAndOpenTMS();
			
		report.startStep("Login as tmsdomain");	
			tmsHomePage.loginAsTmsdomain();//Testing commentffd sffdgfgfgd

		// Initialize registration institution page
		//	registrationInstitutionsPage = new RegistrationInstitutionsPage(webDriver, testResultService);
		
		report.startStep("Click on Registration -> Institution");
			registrationInstitutionsPage.goToInstitutions();
		
		report.startStep("Add New Institution");
			registrationInstitutionsPage.addNewInstitution(institutionName);
			
		report.startStep("Retrieve Institution ID");	
			institutionId = dbService.getInstituteIdByName(institutionName);
			sleep(2);
		
		report.startStep("Verify New Institution Created");
			boolean isInstAdded = registrationInstitutionsPage.verifyInstitutionIsDisplayed(institutionName);
			if (isInstAdded) {
				deletInstitution = true;
			}

		report.startStep("Edit New Institution's Name");
			String newInstName = registrationInstitutionsPage.editInstitution(institutionName);
			
		report.startStep("Verify New Name visible");	
			registrationInstitutionsPage.verifyInstitutionIsDisplayed(newInstName);

		report.startStep("Delete New Institution");
			registrationInstitutionsPage.deleteIntitution(newInstName);
			sleep(2);
			
		report.startStep("Verify New Institution Deleted");
			boolean instNotDisplayed = registrationInstitutionsPage.verifyInstitutionIsNotDisplayed(newInstName);
			if (instNotDisplayed) {
				deletInstitution = false;
			}	
	}
	
	@Test
	@TestCaseParams(testCaseID = { "46134" })
	public void testAssignPackageToInstitution() throws Exception {
		
		report.startStep("Init test data");
			String instID = configuration.getProperty("local_institutionId");
			String instName = dbService.getInstituteNameById(instID);
			String packageName = "Custom Components/12m_12m";
		
		report.startStep("Close ED and Open TMS");
			tmsHomePage = pageHelper.closeEDAndOpenTMS();
			
		report.startStep("Login as tmsdomain");	
			tmsHomePage.loginAsTmsdomain();
			
		// Initialize registration institution page
		//	registrationInstitutionsPage = new RegistrationInstitutionsPage(webDriver, testResultService);
		
		report.startStep("Click on Registration -> Institution");
			registrationInstitutionsPage.goToInstitutions();
			
		report.startStep("Switch to Institution Packages");
			registrationInstitutionsPage.clickOnPackages();
			sleep(2);
			
		report.startStep("Select local institution");
			registrationInstitutionsPage.selectLocalInstitution(instName);
			
		report.startStep("Add New Package");
			registrationInstitutionsPage.addPackage(packageName);
			sleep(2);
			
		report.startStep("Verify Custom component package was added");
			boolean isPackageAdded = registrationInstitutionsPage.verifyPackageIsDisplayed(packageName);
			if (isPackageAdded) {
				deletePackage= true;	
			}
			
		report.startStep("Delete new package");
			registrationInstitutionsPage.deletePackage(packageName);
			sleep(2);
		
		report.startStep("Verify package was deleted");
			boolean packageNotDisplayed = registrationInstitutionsPage.verifyPackageIsNotDisplayed(packageName);
			if (packageNotDisplayed) {
				deletePackage = false;
			}
	}
	@Test
	@TestCaseParams(testCaseID = { "93244","93222" })
	public void testSetAndVerifyPLTconfigurationForInstitution() throws Exception {

		institutionName = institutionsName[22];
		pageHelper.initializeData();

		report.startStep("Close ED and Open TMS");
		tmsHomePage = pageHelper.closeEDAndOpenTMS();
		report.startStep("Login as tmsdomain");
		tmsHomePage.loginAsTmsdomain();

		report.startStep("Set up in db propertyId=48 and Value=1");
		dbService.checkAndturnOnPropertyIdFlag(institutionId, "48", "1");

		registrationInstitutionsPage = new RegistrationInstitutionsPage(webDriver, testResultService);
		report.startStep("Click on Registration -> Institution");
		registrationInstitutionsPage.goToInstitutions();
		report.startStep("Open info with " + institutionName);
		registrationInstitutionsPage.clickInfoInstitution(institutionName);

		report.startStep("Check institution PLT type checkboxes are present on Info institution page");
		testResultService.assertTrue("Checkboxes are not present", registrationInstitutionsPage.areToeikCheckboxesPresent());

		report.startStep("Select all checkboxes");
		registrationInstitutionsPage.selectAllCheckboxesIfTheyAreNotSelected();

		report.startStep("Open info with " + institutionName);
		registrationInstitutionsPage.clickInfoInstitution(institutionName);

		report.startStep("Check all selected checkboxes are chosen");
		testResultService.assertTrue("Not all checkboxes are selected", registrationInstitutionsPage.areAllCheckboxesSelected());


		report.startStep("verify in DB table name and column the value saved. ");
		String[] tests = dbService.getPlasemtTestsFromInstitutions(institutionId);
		Arrays.sort(tests);
		Assert.assertArrayEquals(registrationInstitutionsPage.expectedTests, tests);


		report.startStep("Unselect ToeicLrPLC Checkbox");
		registrationInstitutionsPage.unSelectToeicLrPLCCheckbox();

		report.startStep("Open info with " + institutionName);
		registrationInstitutionsPage.clickInfoInstitution(institutionName);

		report.startStep(" Check ToeicLrPLC checkbox are not chosen");
		testResultService.assertTrue("Not all checkboxes are selected", registrationInstitutionsPage.isLrPLCCheckboxUnSelected());

		report.startStep("Select all checkboxes");
		registrationInstitutionsPage.selectAllCheckboxesIfTheyAreNotSelected();

		tmsHomePage.clickOnExit();


	}

	@After
	public void tearDown() throws Exception {
	/*
			studentId = dbService.getUserIdByUserName(configuration.getProperty("schoolAdmin.user"),configuration.getProperty("institution.id"));
			
			if (deleteGroup){
				dbService.deleteClassGroupByName(groupName,firstClass);
				dbService.deleteClassGroupByName(groupName,secondClass);
			}
			
			if (deletInstitution){
				dbService.deleteInstitutionById(institutionId);
			}
			
			if (deleteUser)
				studentService.deleteStudentById(userId);
			
			if (deleteImportedUser) {
				String studentId = dbService.getUserIdByUserName("Importst11", configuration.getProperty("institution.id"));
				String classId = dbService.getClassIdByName(configuration.getProperty("classname.nostudents"),configuration.getProperty("institution.id"));
				String studentList = studentId + "," + String.valueOf(Long.parseLong(studentId) + 1) + "," + String.valueOf(Long.parseLong(studentId) + 2);
				dbService.deleteUserIdByClassId(classId, studentList);
			}
			
			if (deleteImportedTeacher) {
				String teacherId = dbService.getUserIdByUserName("Importt1", configuration.getProperty("institution.id"));
				String userList = teacherId + ";" + String.valueOf(Long.parseLong(teacherId) + 1) + ";" + String.valueOf(Long.parseLong(teacherId) + 2);
				dbService.deleteUserById(userList);
			}
			
			if (deleteImportedClass) {
				
				for (int i = 0; i < classes.length; i ++) {
					dbService.deleteClassById(importedClassIds[i]);
				}
			}
				
			if (deletePackage)
			{
				String instName = institutionsName[3];
				String instId = dbService.getInstituteIdByName(instName);
				String instPackageId = dbService.getInstitutionPackageIDByPackageId(instId, "1584");
				dbService.deleteInstitutionPackageById(instId, instPackageId);
			}
			
			if (deletClass)
				dbService.deleteClassById(classId);
		*/
			super.tearDown();
	}
}


