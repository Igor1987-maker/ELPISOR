package tests.tms;

import Enums.AdministratorTitle;
import Interfaces.TestCaseParams;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import pageObjects.ToeicAdministrationPage;
import pageObjects.ToeicStudentsAdministrationPage;
import pageObjects.ToeicUploadResultPage;
import pageObjects.edo.*;
import pageObjects.tms.AdministrationAndSessionPage;
import pageObjects.tms.TmsAssessmentsTestsAssignmentPage;
import pageObjects.tms.TmsHomePage;
import pageObjects.tms.TmsReportsPage;
import services.PageHelperService;
import testCategories.inProgressTests;
import tests.edo.newux.BasicNewUxTest;
import tests.edo.newux.EDExcellenceTests;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

public class EDExcellenceTMSTests extends BasicNewUxTest { //SpeechRecognitionBasicTestNewUX {

    NewUXLoginPage loginPage;
    TmsHomePage tmsHomePage;
    NewUxMyProfile myProfile;
    TmsAssessmentsTestsAssignmentPage tests;
    ToeicStudentsAdministrationPage toeicStudentsPage;
    AdministrationAndSessionPage administrationAndSessionPage;
    ToeicStudentsAdministrationPage toeicStudentsAdministrationPage;
    private String uploadFilesPath = "\\AutoLogs\\ToolsAndResources\\Shared\\CSVwithStudents\\";
    private final String testReportsClass = "Combinado";
    private final String[] expectedUnitTestHeaders = {"Full Name", "Username", "Unit Name", "Score", "Date", "Average score"};
    private final String[] expectedPraciceTestHeaders = new String[]{"Full Name", "Username", "Half Practice Test", "Full Practice Test", "Full Practice Test 2"};
    String[] pltTestTypes = {"ED Placement","Toeic Placement","Toeic Bridge Placement"};
    String date = "";

    public EDExcellenceTMSTests() throws Exception {
    }

    @Before
    public void setup() throws Exception {
        institutionName = institutionsName[22];
        super.setup();
    }

    @Test
    public void completeTOEICPltTestAndVerifyReport() throws Exception {

        /*report.startStep("Create user in class assigned to TOEIC bridge");
            String UserID = pageHelper.createUSerUsingSP(institutionId, "ToeicBridgeUnlimited");
            List<String[]> user = dbService.getUserNameAndPasswordByUserId(UserID);
            userName = user.get(0)[0];
            String password = user.get(0)[1];
            String userFirsName = user.get(0)[3];*/

        report.startStep("Create user in class assigned to TOEIC bridge");
            String baseUrl = pageHelper.CILink.split(".com")[0] + ".com/";
            userName = "TBU" + dbService.sig(4);
            String userFN = userName + "-FN";
            String userLN = userName + "-LN";
            String email = userName + new Random().nextInt(1000) + "-@edusof-t.co.il";
            String className = "ToeicBridgeUnlimited";

        try {
            String regUserUrl = baseUrl + "RegUserAndLogin.aspx?Action=I&UserName=" + userName + "" + "&Inst="
                    + institutionName + "&FirstName=" + userFN + "&LastName=" + userLN + "" + "&Password=12345&Email="
                    + email + "&Class=" + className + "" + "&Language=English&Link=" + pageHelper.linkED
                    + "&UseNameMapping=N&CreateClass=N&UserType=S";

            webDriver.openUrl(regUserUrl);

        }catch (Exception e){
        }

        report.startStep("Create user in class assigned to TOEIC bridge");
            pageHelper.skipOptin();
            homePage.closeAllNotifications();
            pageHelper.skipOnBoardingHP();
            homePage.waitForPageToLoad();
    }


    @Test
    public void assignTOEICOnlineAsPLT_Unlimited() throws Exception {

        report.startStep("Init Needed Vars");
            String className = "Class"+dbService.sig(4);
            // int testTypeId = 1; // 2 it's midterm 3 it's final
            //String courseName =  coursesNames[1];
            //String TestFullName = "Basic 1 Midterm Test";//"Basic 1 Mid-Term Test";
            //String courseId = getCourseIdByCourseCode(courseCodes[1]);
            //classId = dbService.getClassIdByName(className, coursesInstId);
            String[] institutionPackage = dbService.getUnExpiredInstitutionPackages(institutionId).get(0);
            pageHelper.createClassUsingSP("",className,institutionId,institutionPackage[1]);
            studentId = pageHelper.createUSerUsingSP(institutionId, className);
            List<String[]> user = dbService.getUserNameAndPasswordByUserId(studentId);
            userName = user.get(0)[0];
            String password = user.get(0)[1];
            String userFirsName = user.get(0)[3];

        try {
            report.startStep("Login as Admin");
                loginPage = new NewUXLoginPage(webDriver, testResultService);
                String[] adminUser = dbService.getAdminUserByInstitutionId(institutionId);
                pageHelper.setUserLoginToNull(adminUser[2]);
                sleep(1);
                tmsHomePage = loginPage.enterSchoolAdminUserAndPassword(adminUser[0], adminUser[1]);
                homePage.closeAllNotifications();

            report.startStep("Initialize tms assessments automated tests page");
                TmsAssessmentsTestsAssignmentPage tmsAssessmentsTestsAssignmentPage = new TmsAssessmentsTestsAssignmentPage(webDriver, testResultService);

            report.startStep("Go to Assessments -> Test Assignment");
                tmsAssessmentsTestsAssignmentPage.goToAssessmentTestsAssignment();
                tmsAssessmentsTestsAssignmentPage.selectFeatureAndGo("PL");
                webDriver.switchToNewWindow();

            report.startStep("Click on add test-type");
                ToeicAdministrationPage administrationPage = new ToeicAdministrationPage(webDriver, testResultService);
                administrationPage.clickOnAddTestType();

            report.startStep("Chose test-type and click 'Add'");
                administrationPage.choseTestTypeAndClickADD(pltTestTypes[1]);

            report.startStep("Select class");
                administrationPage.selectClass(className,pltTestTypes[1]);

            report.startStep("Switch unlimited assignment");
                administrationPage.switchUnlimitedAssignment();
                sleep(1);

            report.startStep("Click on assign students");
                administrationPage.clickOnAssignStudents();

            report.startStep("Wait and verify success message");
                administrationPage.waitSuccessMessage();

            report.startStep("Click on Done button");
                administrationPage.clickOnDoneButton();

            report.startStep("Switch to main tab and exit from TMS");
                webDriver.closeTab(1);
                webDriver.switchToMainWindow();
                tmsHomePage.clickOnExit();

            report.startStep("Login to ED");
                loginPage.waitForPageToLoad();
                homePage = loginPage.loginAsStudent(userName,password);
                pageHelper.skipOptin();
                homePage.closeAllNotifications();
                pageHelper.skipOnBoardingHP();
                homePage.waitForPageToLoad();

            report.startStep("Verify whether test available in assessments area");
                NewUxAssessmentsPage testsPage = homePage.openAssessmentsPage(false);
                sleep(3);
                testsPage.checkItemsCounterBySection("1", "1");
                testsPage.checkTestDisplayedInSectionByTestName(testTypes[0], "1", "1");

            report.startStep("Click on the Start test on Assessment");
                testsPage.clickStartTest(1, 1);
                sleep(4);

            report.startStep("Go through explanations before test");
                String mainWindow = webDriver.switchToNewWindow();
                TOEICTestPage toeicTestPage = new TOEICTestPage(webDriver,testResultService);
                toeicTestPage.waitTillToeicTestDownloadedAncClickOnStart();

            report.startStep("Verify whether test running");
                String parentWindowHandle = webDriver.switchToPopup();
                toeicTestPage.completeTOEICTest();
                //toeicTestPage.verifyTestIsRunning();

            report.startStep("Close test window and switch to main window");
                webDriver.getWebDriver().switchTo().window(parentWindowHandle);
                webDriver.getWebDriver().close();
                webDriver.switchToMainWindow(mainWindow);

            report.startStep("Logout from ED");
                homePage.clickOnLogOut();

        }catch (Exception | AssertionError err){
            err.printStackTrace();
            testResultService.addFailTest(err.getMessage(),true, true);

        }finally {
            dbService.deleteStudentByName(institutionId, userName);
            dbService.deleteClassByName(institutionId,className);
        }
    }

    @Test
    public void assignTOEICBridgeAsPLT_RestrictedTime() throws Exception {

        report.startStep("Init Needed Vars");
            String className = "Class"+dbService.sig(4);
            String[] institutionPackage = dbService.getUnExpiredInstitutionPackages(institutionId).get(0);
            pageHelper.createClassUsingSP("",className,institutionId,institutionPackage[1]);
            studentId = pageHelper.createUSerUsingSP(institutionId, className);
            String classID = dbService.getClassIdByClassName(className, institutionId);
            List<String[]> user = dbService.getUserNameAndPasswordByUserId(studentId);
            userName = user.get(0)[0];
            String password = user.get(0)[1];
            String userFirsName = user.get(0)[3];

        try {
            report.startStep("Login as Admin");
                loginPage = new NewUXLoginPage(webDriver, testResultService);
                String[] adminUser = dbService.getAdminUserByInstitutionId(institutionId);
                pageHelper.setUserLoginToNull(adminUser[2]);
                sleep(1);
                tmsHomePage = loginPage.enterSchoolAdminUserAndPassword(adminUser[0],adminUser[1]);
                homePage.closeAllNotifications();

            report.startStep("Initialize tms assessments automated tests page");
                TmsAssessmentsTestsAssignmentPage tmsAssessmentsTestsAssignmentPage = new TmsAssessmentsTestsAssignmentPage(webDriver, testResultService);

            report.startStep("Go to Assessments -> Test Assignment");
                tmsAssessmentsTestsAssignmentPage.goToAssessmentTestsAssignment();
                tmsAssessmentsTestsAssignmentPage.selectFeatureAndGo("PL");
                webDriver.switchToNewWindow();

            report.startStep("Click on add test-type");
                ToeicAdministrationPage administrationPage = new ToeicAdministrationPage(webDriver,testResultService);
                administrationPage.clickOnAddTestType();

            report.startStep("Chose test-type and click 'Add'");
                administrationPage.choseTestTypeAndClickADD(pltTestTypes[2]);

            report.startStep("Select class");
                administrationPage.selectClass(className,pltTestTypes[2]);

            report.startStep("Chose start and end date");
                administrationPage.choseStartEndDate();

            report.startStep("Click on assign students");
                administrationPage.clickOnAssignStudents();

            report.startStep("Wait and verify success message");
                administrationPage.waitSuccessMessage();

            report.startStep("Click on Done button");
                administrationPage.clickOnDoneButton();

            report.startStep("Switch to main tab and exit from TMS");
                webDriver.closeTab(1);
                webDriver.switchToMainWindow();
                tmsHomePage.clickOnExit();

            report.startStep("Login to ED");
                loginPage.waitForPageToLoad();
                homePage = loginPage.loginAsStudent(userName,password);
                pageHelper.skipOptin();
                homePage.closeAllNotifications();
                pageHelper.skipOnBoardingHP();
                homePage.waitForPageToLoad();

            report.startStep("Verify whether test available in assessments area");
                NewUxAssessmentsPage testsPage = homePage.openAssessmentsPage(false);
                sleep(3);
                testsPage.checkItemsCounterBySection("1", "1");
                testsPage.checkTestDisplayedInSectionByTestName(testTypes[0], "1", "1");

            report.startStep("Click on the Start test on Assessment");
                testsPage.clickStartTest(1, 1);
                sleep(4);

            report.startStep("Pass explanations before test");
                String mainWindow = webDriver.switchToNewWindow();
                TOEICTestPage toeicTestPage = new TOEICTestPage(webDriver,testResultService);
                toeicTestPage.waitTillToeicTestDownloadedAncClickOnStart();

            report.startStep("Verify whether test running");
                String parentWindowHandle = webDriver.switchToPopup();
                toeicTestPage.completeTOEICTest();
                //toeicTestPage.verifyTestIsRunning();

            report.startStep("Close test window and switch to main window");
                webDriver.getWebDriver().switchTo().window(parentWindowHandle);
                webDriver.getWebDriver().close();
                webDriver.switchToMainWindow(mainWindow);

           /* report.startStep("Open assessment and verify PLT scores displayed");
                homePage.openAssessmentsPage(false);
                testsPage.clickOnArrowToOpenSection("4");
                //testsPage.checkTestDisplayedInSectionByTestName(testTypes[0], "4", "1");
                testsPage.checkScoresInEDExcellenceByTest("0");
                //testsPage.checkScoreLevelPLT("1", "33");*/

            report.startStep("Close Assessments");
                testsPage.close();

            report.startStep("Logout from ED");
                homePage.clickOnLogOut();

        }catch (Exception e){
            e.printStackTrace();
            testResultService.addFailTest(e.getMessage(),true,true);
        }finally{
            dbService.deleteUserById(studentId);
            dbService.deleteClassByName(institutionId,className);
        }
    }

    @Test
    @Category(EDExcellenceTests.class)
    @TestCaseParams(testCaseID = {"43416, 93295", "94456","92339", "92869", })
    public void checkToeicBridgeStatusTestOnPltReport() throws Exception {

        report.startStep("Init Needed Vars");
        String className = "Class"+dbService.sig(4);
        String[] institutionPackage = dbService.getUnExpiredInstitutionPackages(institutionId).get(0);
        pageHelper.createClassUsingSP("",className,institutionId,institutionPackage[1]);

        studentId = pageHelper.createUSerUsingSP(institutionId, className);
        String ClassId = dbService.getClassIdByClassName(className, institutionId);
        List<String[]> user = dbService.getUserNameAndPasswordByUserId(studentId);
        userName = user.get(0)[0];
        String password = user.get(0)[1];
        String userFirsName = user.get(0)[3];

        try {
            report.startStep("Login as Admin");
            loginPage = new NewUXLoginPage(webDriver, testResultService);
            String[] adminUser = dbService.getAdminUserByInstitutionId(institutionId);
            pageHelper.setUserLoginToNull(adminUser[2]);
            sleep(1);
            tmsHomePage = loginPage.enterSchoolAdminUserAndPassword(adminUser[0], adminUser[1]);
            homePage.closeAllNotifications();

            report.startStep("Initialize tms assessments automated tests page");
            TmsAssessmentsTestsAssignmentPage tmsAssessmentsTestsAssignmentPage = new TmsAssessmentsTestsAssignmentPage(webDriver, testResultService);

            report.startStep("Go to Assessments -> Test Assignment");
            tmsAssessmentsTestsAssignmentPage.goToAssessmentTestsAssignment();
            tmsAssessmentsTestsAssignmentPage.selectFeatureAndGo("PL");
            webDriver.switchToNewWindow();

            report.startStep("Click on add test-type");
            ToeicAdministrationPage administrationPage = new ToeicAdministrationPage(webDriver, testResultService);
            administrationPage.clickOnAddTestType();

            report.startStep("Chose test-type and click 'Add'");
            administrationPage.choseTestTypeAndClickADD(pltTestTypes[2]);

            report.startStep("Select class");
            administrationPage.selectClass(className,pltTestTypes[2]);

            report.startStep("Switch unlimited assignment");
            administrationPage.switchUnlimitedAssignment();
            sleep(1);

            report.startStep("Click on assign students");
            administrationPage.clickOnAssignStudents();

            report.startStep("Wait and verify success message");
            administrationPage.waitSuccessMessage();

            report.startStep("Click on Done button");
            administrationPage.clickOnDoneButton();

            report.startStep("Switch to main tab and exit from TMS");
            webDriver.closeTab(1);
            webDriver.switchToMainWindow();
            tmsHomePage.clickOnExit();

            report.startStep("Login to ED");
            loginPage.waitForPageToLoad();
            homePage = loginPage.loginAsStudent(userName,password);
            pageHelper.skipOptin();
            homePage.closeAllNotifications();
            pageHelper.skipOnBoardingHP();
            homePage.waitForPageToLoad();
            sleep(10);
            homePage.clickOnHomeButton();
            homePage.waitHomePageloaded();

            report.startStep("Verify whether test available in assessments area");
            NewUxAssessmentsPage testsPage = homePage.openAssessmentsPage(false);
            sleep(3);
            testsPage.checkItemsCounterBySection("1", "1");
            testsPage.checkTestDisplayedInSectionByTestName(testTypes[0], "1", "1");

            report.startStep("Click on the Start test on Assessment");
            testsPage.clickStartTest(1, 1);
            sleep(4);

            report.startStep("Pass explanations before test");
            webDriver.switchToNewWindow();
            TOEICTestPage toeicTestPage = new TOEICTestPage(webDriver,testResultService);
            toeicTestPage.waitTillToeicTestDownloadedAncClickOnStart();

            report.startStep("Verify whether test running");
            String parentWindowHandle = webDriver.switchToPopup();
            toeicTestPage.verifyTestIsRunning();

            report.startStep("Close test window and switch to main window");
            webDriver.getWebDriver().close();
            webDriver.getWebDriver().switchTo().window(parentWindowHandle);
            webDriver.closeTab(1);
            webDriver.switchToMainWindow();

            report.startStep("Logout from ED");
            homePage.clickOnLogOut();

            report.startStep("Run DW to update the status");
            dbService.runDWJob(studentId, ClassId);

            report.startStep("Restart browser in new URL");
            institutionName = institutionsName[22];
            pageHelper.restartBrowserInNewURL(institutionName, true);
            institutionId = dbService.getInstituteIdByName(institutionName);

            report.startStep("Login as Admin");
            loginPage = new NewUXLoginPage(webDriver, testResultService);
            loginPage.loginAsStudent(adminUser[0], adminUser[1]);
            sleep(1);

            report.startStep("Go to Reports");
            TmsReportsPage tmsReportsPage = new TmsReportsPage(webDriver, testResultService);
            tmsHomePage.switchToMainFrame();
            tmsHomePage.clickOnReports();
            sleep(1);
            tmsHomePage.clickOnPLTReports();
            webDriver.switchToNewWindow();

            report.startStep("Click Toiec button on PLT report");
            tmsReportsPage.clickToeicTypeReportButton();
            sleep(10);

            report.startStep("Click classes dropdown");
            tmsReportsPage.clickClassesDropdown();
            sleep(2);
            tmsReportsPage.clickInputField();

            report.startStep("Filter by " +className);
            tmsReportsPage.filterByClasses(className);
            sleep(3);

            report.startStep("Select checkbox with " +className);
            tmsReportsPage.selectClassesCheckbox(className);
            sleep(5);

            report.startStep("Click out for applying the filter");
            tmsReportsPage.applyFilter();
            sleep(3);

            report.startStep("verify test status on DB");
             String resultStatus = dbService.getPltTestStatus(studentId);
             testResultService.assertEquals("Test In progress", resultStatus);

             report.startStep("verify test status on UI");
             testResultService.assertEquals("In progress", tmsReportsPage.isStatusDisplayed());
            report.startStep("Switch to main tab and exit from TMS");
            webDriver.closeTab(1);
            webDriver.switchToMainWindow();
            tmsHomePage.clickOnExit();

        }catch (Exception | AssertionError err){
            err.printStackTrace();
            testResultService.addFailTest(err.getMessage(),true, true);

        }finally {
            dbService.deleteUserById(studentId);
            dbService.deleteClassByName(institutionId,className);
        }
    }

    @Test
    @Category(EDExcellenceTests.class)
    @TestCaseParams(testCaseID = {"43416, 93295", "94456","92339", "92869", "93291" })
    public void checkEDPltStatusTestOnPltReport() throws Exception {

        report.startStep("Init Needed Vars");
        String className = "Class" + dbService.sig(4);
        String[] institutionPackage = dbService.getUnExpiredInstitutionPackages(institutionId).get(0);
        pageHelper.createClassUsingSP("", className, institutionId, institutionPackage[1]);

        studentId = pageHelper.createUSerUsingSP(institutionId, className);
        String ClassId = dbService.getClassIdByClassName(className, institutionId);
        List<String[]> user = dbService.getUserNameAndPasswordByUserId(studentId);
        userName = user.get(0)[0];
        String password = user.get(0)[1];

        try {
            report.startStep("Login as Admin");
            loginPage = new NewUXLoginPage(webDriver, testResultService);
            String[] adminUser = dbService.getAdminUserByInstitutionId(institutionId);
            pageHelper.setUserLoginToNull(adminUser[2]);
            sleep(1);
            tmsHomePage = loginPage.enterSchoolAdminUserAndPassword(adminUser[0], adminUser[1]);
            homePage.closeAllNotifications();

            report.startStep("Initialize tms assessments automated tests page");
            TmsAssessmentsTestsAssignmentPage tmsAssessmentsTestsAssignmentPage = new TmsAssessmentsTestsAssignmentPage(webDriver, testResultService);

            report.startStep("Go to Assessments -> Test Assignment");
            tmsAssessmentsTestsAssignmentPage.goToAssessmentTestsAssignment();
            tmsAssessmentsTestsAssignmentPage.selectFeatureAndGo("PL");
            webDriver.switchToNewWindow();

            report.startStep("Click on add test-type");
            ToeicAdministrationPage administrationPage = new ToeicAdministrationPage(webDriver, testResultService);
            administrationPage.clickOnAddTestType();

            report.startStep("Chose test-type and click 'Add'");
            administrationPage.choseTestTypeAndClickADD(pltTestTypes[0]);

            report.startStep("Select class");
            administrationPage.selectClass(className, pltTestTypes[0]);

            report.startStep("Switch unlimited assignment");
            administrationPage.switchUnlimitedAssignment();
            sleep(1);

            report.startStep("Click on assign students");
            administrationPage.clickOnAssignStudents();

            report.startStep("Wait and verify success message");
            administrationPage.waitSuccessMessage();

            report.startStep("Click on Done button");
            administrationPage.clickOnDoneButton();

            report.startStep("Switch to main tab and exit from TMS");
            webDriver.closeTab(1);
            webDriver.switchToMainWindow();
            tmsHomePage.clickOnExit();
            report.startStep("Run DW to update the status");
            dbService.runDWJob(studentId, ClassId);

            report.startStep("Login to ED");
            loginPage.waitForPageToLoad();
            homePage = loginPage.loginAsStudent(userName, password);
            pageHelper.skipOptin();
            homePage.closeAllNotifications();
            pageHelper.skipOnBoardingHP();
            homePage.waitForPageToLoad();
            sleep(10);
            homePage.clickOnHomeButton();
            homePage.waitHomePageloaded();

            report.startStep("Init Data");

            String TITLE_PLT = "ED Placement";

            TestEnvironmentPage testEnvironmentPage = new TestEnvironmentPage(webDriver, testResultService);

            report.startStep("Verify whether test available in assessments area");
            NewUxAssessmentsPage testsPage = homePage.openAssessmentsPage(false);
            sleep(3);
            testsPage.checkItemsCounterBySection("1", "1");
            testsPage.checkTestDisplayedInSectionByTestName(testTypes[0], "1", "1");

            report.startStep("Click on the Start test on Assessment");
            testsPage.clickStartTest(1, 1);
            sleep(4);

            studentId = dbService.getUserIdByUserName(BasicNewUxTest.userName, institutionId);
            List<String[]> userTestAdministrations = dbService.getUserTestAdministrationsDetailsByStudentId(studentId);
            String userTestTokenDB = userTestAdministrations.get(0)[7];
            testResultService.assertEquals(false, userTestTokenDB == null, "User Test Token is NULL");

            report.startStep("Verify Title");
            testEnvironmentPage.validateTestName(TITLE_PLT);
            sleep(1);

            report.startStep("Perform PLT Test");
            testEnvironmentPage.clickLevelButton();
            testEnvironmentPage.pressOnStartTest();
            testEnvironmentPage.clickNext();
            testEnvironmentPage.selectRadioButton();

            report.startStep("Click Exit PLT");
            testEnvironmentPage.clickExitPltTest();
            testEnvironmentPage.clickOnSubmitOKButton();

            report.startStep("Logout from ED");
            homePage.clickOnLogOut();

            report.startStep("Run DW to update the status");
            dbService.runDWJob(studentId, ClassId);

            report.startStep("Restart browser in new URL");
            institutionName = institutionsName[22];
            pageHelper.restartBrowserInNewURL(institutionName, true);
            institutionId = dbService.getInstituteIdByName(institutionName);

            report.startStep("Login as Admin");
            loginPage = new NewUXLoginPage(webDriver, testResultService);
            loginPage.loginAsStudent(adminUser[0], adminUser[1]);
            sleep(1);

            report.startStep("Go to Reports");
            TmsReportsPage tmsReportsPage = new TmsReportsPage(webDriver, testResultService);
            tmsHomePage.switchToMainFrame();
            tmsHomePage.clickOnReports();
            sleep(1);
            tmsHomePage.clickOnPLTReports();
            webDriver.switchToNewWindow();

            sleep(40);

            report.startStep("Click classes dropdown");
            tmsReportsPage.clickClassesDropdown();
            sleep(2);
            tmsReportsPage.clickInputField();

            report.startStep("Filter by " + className);
            tmsReportsPage.filterByClasses(className);
            sleep(3);

            report.startStep("Select checkbox with " + className);
            tmsReportsPage.selectClassesCheckbox(className);
            sleep(5);

            report.startStep("Click out for applying the filter");
            tmsReportsPage.applyFilter();
            sleep(3);

            report.startStep("Verify test status on DB");
            String resultStatus = dbService.getPltTestStatus(studentId);
            testResultService.assertEquals("Test In progress", resultStatus);

            report.startStep("Verify test status on UI");
            testResultService.assertEquals("In progress", tmsReportsPage.isStatusDisplayed());


        } catch (Exception | AssertionError err) {
            err.printStackTrace();
            testResultService.addFailTest(err.getMessage(), true, true);

        } finally {

            dbService.deleteUserById(studentId);
            dbService.deleteClassByName(institutionId, className);
        }
    }

    @Test
    @Category(EDExcellenceTests.class)
    @TestCaseParams(testCaseID = {"94392"})
    public void verifyExportPLTReport() throws Exception {

        report.startStep("Init Needed Vars");
        String className = "Class" + dbService.sig(4);
        String[] institutionPackage = dbService.getUnExpiredInstitutionPackages(institutionId).get(0);
        pageHelper.createClassUsingSP("", className, institutionId, institutionPackage[1]);

        studentId = pageHelper.createUSerUsingSP(institutionId, className);
        String ClassId = dbService.getClassIdByClassName(className, institutionId);
        List<String[]> user = dbService.getUserNameAndPasswordByUserId(studentId);
        userName = user.get(0)[0];
        String password = user.get(0)[1];
        report.startStep("Restart browser in new URL");
        institutionName = institutionsName[22];
        pageHelper.restartBrowserInNewURL(institutionName, true);
        institutionId = dbService.getInstituteIdByName(institutionName);

        report.startStep("Login as Admin");
        loginPage = new NewUXLoginPage(webDriver, testResultService);
        String[] adminUser = dbService.getAdminUserByInstitutionId(institutionId);
        pageHelper.setUserLoginToNull(adminUser[2]);
        sleep(1);
        tmsHomePage = loginPage.enterSchoolAdminUserAndPassword(adminUser[0], adminUser[1]);
        homePage.closeAllNotifications();

        report.startStep("Go to Reports");
        TmsReportsPage tmsReportsPage = new TmsReportsPage(webDriver, testResultService);
        tmsHomePage.switchToMainFrame();
        tmsHomePage.clickOnReports();
        sleep(1);
        tmsHomePage.clickOnPLTReports();
        webDriver.switchToNewWindow();
        sleep(10);

        report.startStep("Select Done status dropdown");
        tmsReportsPage.clickStatusDropdown();
        sleep(2);
        tmsReportsPage.clickDone();

        report.startStep("Click out for applying the filter");
        tmsReportsPage.applyFilter();
        sleep(3);

        report.startStep("Download and switch to Excel");
        tmsReportsPage.clickOnExportReport();
        String pathToDownloadedFile = pageHelper.getPathToJustDownloadedFile();
        List<String[]> listFromCsv = textService.getStr2dimArrFromCsv(pathToDownloadedFile);

        report.startStep("Check data on UI and Excel");
        String[] expectedData = listFromCsv.get(1);
        tmsReportsPage.compareData(webDriver.getWebDriver(), "tr", expectedData);

        report.startStep("Switch to main tab and exit from TMS");
        webDriver.closeTab(1);
        webDriver.switchToMainWindow();
        tmsHomePage.clickOnExit();
    }

    @Test
    @TestCaseParams(testCaseID = {"43416, 93295", "94456","92339", "92869", "93291" })
    @Category(EDExcellenceTests.class)
    public void checkEDPltStatusDoneOnPltReport() throws Exception {

        report.startStep("Init Needed Vars");
        String className = "Class" + dbService.sig(4);
        String[] institutionPackage = dbService.getUnExpiredInstitutionPackages(institutionId).get(0);
        pageHelper.createClassUsingSP("", className, institutionId, institutionPackage[1]);

        studentId = pageHelper.createUSerUsingSP(institutionId, className);
        String ClassId = dbService.getClassIdByClassName(className, institutionId);
        List<String[]> user = dbService.getUserNameAndPasswordByUserId(studentId);
        userName = user.get(0)[0];
        String password = user.get(0)[1];

        try {
            report.startStep("Login as Admin");
            loginPage = new NewUXLoginPage(webDriver, testResultService);
            String[] adminUser = dbService.getAdminUserByInstitutionId(institutionId);
            pageHelper.setUserLoginToNull(adminUser[2]);
            sleep(1);
            tmsHomePage = loginPage.enterSchoolAdminUserAndPassword(adminUser[0], adminUser[1]);
            homePage.closeAllNotifications();

            report.startStep("Initialize tms assessments automated tests page");
            TmsAssessmentsTestsAssignmentPage tmsAssessmentsTestsAssignmentPage = new TmsAssessmentsTestsAssignmentPage(webDriver, testResultService);

            report.startStep("Go to Assessments -> Test Assignment");
            tmsAssessmentsTestsAssignmentPage.goToAssessmentTestsAssignment();
            tmsAssessmentsTestsAssignmentPage.selectFeatureAndGo("PL");
            webDriver.switchToNewWindow();

            report.startStep("Click on add test-type");
            ToeicAdministrationPage administrationPage = new ToeicAdministrationPage(webDriver, testResultService);
            administrationPage.clickOnAddTestType();

            report.startStep("Chose test-type and click 'Add'");
            administrationPage.choseTestTypeAndClickADD(pltTestTypes[0]);

            report.startStep("Select class");
            administrationPage.selectClass(className, pltTestTypes[0]);

            report.startStep("Switch unlimited assignment");
            administrationPage.switchUnlimitedAssignment();
            sleep(1);

            report.startStep("Click on assign students");
            administrationPage.clickOnAssignStudents();

            report.startStep("Wait and verify success message");
            administrationPage.waitSuccessMessage();

            report.startStep("Click on Done button");
            administrationPage.clickOnDoneButton();

            report.startStep("Switch to main tab and exit from TMS");
            webDriver.closeTab(1);
            webDriver.switchToMainWindow();
            tmsHomePage.clickOnExit();
            report.startStep("Run DW to update the status");
            dbService.runDWJob(studentId, ClassId);

            report.startStep("Login to ED");
            loginPage.waitForPageToLoad();
            homePage = loginPage.loginAsStudent(userName, password);
            pageHelper.skipOptin();
            homePage.closeAllNotifications();
            pageHelper.skipOnBoardingHP();
            homePage.waitForPageToLoad();
            sleep(10);
            homePage.clickOnHomeButton();
            homePage.waitHomePageloaded();

            report.startStep("Init Data");

            String TITLE_PLT = "ED Placement";

            TestEnvironmentPage testEnvironmentPage = new TestEnvironmentPage(webDriver, testResultService);

            report.startStep("Verify whether test available in assessments area");
            NewUxAssessmentsPage testsPage = homePage.openAssessmentsPage(false);
            sleep(3);
            testsPage.checkItemsCounterBySection("1", "1");
            testsPage.checkTestDisplayedInSectionByTestName(testTypes[0], "1", "1");

            report.startStep("Click on the Start test on Assessment");
            testsPage.clickStartTest(1, 1);
            sleep(4);

            studentId = dbService.getUserIdByUserName(BasicNewUxTest.userName, institutionId);
            List<String[]> userTestAdministrations = dbService.getUserTestAdministrationsDetailsByStudentId(studentId);
            String userTestTokenDB = userTestAdministrations.get(0)[7];
            testResultService.assertEquals(false, userTestTokenDB == null, "User Test Token is NULL");

            report.startStep("Verify Title");
            testEnvironmentPage.validateTestName(TITLE_PLT);
            sleep(1);

            report.startStep("Perform PLT Test");
            testEnvironmentPage.clickLevelButton();
            testEnvironmentPage.pressOnStartTest();
            testEnvironmentPage.clickNext();
            testEnvironmentPage.selectRadioButton();
            testEnvironmentPage.submitPLTTestWithoutAnswers();

            report.startStep("Logout from ED");
            homePage.clickOnLogOut();

            report.startStep("Run DW to update the status");
            dbService.runDWJob(studentId, ClassId);

            report.startStep("Restart browser in new URL");
            institutionName = institutionsName[22];
            pageHelper.restartBrowserInNewURL(institutionName, true);
            institutionId = dbService.getInstituteIdByName(institutionName);

            report.startStep("Login as Admin");
            loginPage = new NewUXLoginPage(webDriver, testResultService);
            loginPage.loginAsStudent(adminUser[0], adminUser[1]);
            sleep(1);

            report.startStep("Go to Reports");
            TmsReportsPage tmsReportsPage = new TmsReportsPage(webDriver, testResultService);
            tmsHomePage.switchToMainFrame();
            tmsHomePage.clickOnReports();
            sleep(1);
            tmsHomePage.clickOnPLTReports();
            webDriver.switchToNewWindow();

            sleep(40);

            report.startStep("Click classes dropdown");
            tmsReportsPage.clickClassesDropdown();
            sleep(2);
            tmsReportsPage.clickInputField();

            report.startStep("Filter by " + className);
            tmsReportsPage.filterByClasses(className);
            sleep(3);

            report.startStep("Select checkbox with " + className);
            tmsReportsPage.selectClassesCheckbox(className);
            sleep(5);

            report.startStep("Click out for applying the filter");
            tmsReportsPage.applyFilter();
            sleep(3);

            report.startStep("Verify test status on DB");
            String resultStatus = dbService.getPltTestStatus(studentId);
            testResultService.assertEquals("Test is Done", resultStatus);

            report.startStep("Verify test status on UI");
            testResultService.assertEquals("Done", tmsReportsPage.isStatusDisplayed());

        } catch (Exception | AssertionError err) {
            err.printStackTrace();
            testResultService.addFailTest(err.getMessage(), true, true);

        } finally {

            dbService.deleteUserById(studentId);
            dbService.deleteClassByName(institutionId, className);
        }
    }



    @Test
    @TestCaseParams(testCaseID = {""})
    @Category(EDExcellenceTests.class)
    public void createAdministrationAndSession() throws Exception {

        report.startStep("Login as Admin");
        loginPage = new NewUXLoginPage(webDriver, testResultService);
        pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(configuration.getProperty("schoolAdmin.user"), institutionId));
        sleep(1);
        tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
        homePage.closeAllNotifications();

        report.startStep("Go to Assessments -> Test Assignment");
        TmsAssessmentsTestsAssignmentPage tmsAssessmentsTestsAssignmentPage = new TmsAssessmentsTestsAssignmentPage(webDriver, testResultService);
        tmsAssessmentsTestsAssignmentPage.goToAssessmentTestsAssignment();

        report.startStep("Select type of TOEIC assignment");
        tmsAssessmentsTestsAssignmentPage.selectTypeOfTOEICAssignment("TOEIC Test (ITS)", "Toeic");
        tmsAssessmentsTestsAssignmentPage.clickOnRegistrationPageButton();

        report.startStep("Switch to TOEIC TEST STUDENT REGISTRATION");
        webDriver.switchToPopup();
        toeicStudentsPage = new ToeicStudentsAdministrationPage(webDriver, testResultService);

        report.startStep("Create CSV with student and store it in folder 'Languages'");
        ToeicUploadResultPage toeicUploadResultPage = new ToeicUploadResultPage(webDriver, testResultService, toeicStudentsPage);
        report.startStep("Click Method dropdown");
        sleep(2);
        toeicUploadResultPage.clickMethodsDropdown();
        report.startStep("Select BYOP Method ");
        toeicUploadResultPage.selectByopMethod();
        sleep(2);
        toeicUploadResultPage.clickOnNextButton();
        sleep(5);

        String filePath ="";
        try{
            filePath = prepareDataToRegistrationFileAndSaving();
           // sleep(4);

            report.startStep("Upload CSV and create new administration");
            sleep(5);
            ToeicUploadResultPage uploadResultPage = toeicStudentsPage.uploadCSV(new File(filePath));
            sleep(24);
            toeicUploadResultPage.clickOnNextButton();

            report.startStep("Create Administration");
            uploadResultPage.clickOnCreateAdministration();
            sleep(7);
            uploadResultPage.verifyWhetherSessionWasCreated();

            report.startStep("Verify administration was storedOnDB");
            String administrationId = dbService.getAdministartionOfCertificationTest(date);
            textService.assertTrue("Administration not stored on DB", administrationId!=null);

            report.startStep("Delete administration from DB");
            report.startStep("Delete administration on ITS side");

            report.startStep("Exit from TMS");
            webDriver.switchToMainWindow();
            tmsHomePage.clickOnExit();


        }catch (Exception e){
            e.printStackTrace();

        }catch (AssertionError e){
            e.printStackTrace();
        }finally {
            File registeredStudent = new File(filePath);
            registeredStudent.delete();
            boolean fileStillExist = registeredStudent.exists();
            textService.assertEquals("File not deleted", false, fileStillExist);
        }


    }

    @Test
    @TestCaseParams(testCaseID = {""})
    @Category(EDExcellenceTests.class)
    public void createAdministrationStandardMethod() throws Exception {

        report.startStep("Login as Admin");
            loginPage = new NewUXLoginPage(webDriver, testResultService);
            pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(configuration.getProperty("schoolAdmin.user"), institutionId));
            sleep(1);
            tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
            homePage.closeAllNotifications();

        report.startStep("Go to Assessments -> Test Assignment");
        TmsAssessmentsTestsAssignmentPage tmsAssessmentsTestsAssignmentPage = new TmsAssessmentsTestsAssignmentPage(webDriver, testResultService);
        tmsAssessmentsTestsAssignmentPage.goToAssessmentTestsAssignment();

        report.startStep("Select type of TOEIC assignment");
        tmsAssessmentsTestsAssignmentPage.selectTypeOfTOEICAssignment("TOEIC Test (ITS)", "Toeic");
        tmsAssessmentsTestsAssignmentPage.clickOnRegistrationPageButton();

        report.startStep("Switch to TOEIC TEST STUDENT REGISTRATION");
        webDriver.switchToPopup();
        toeicStudentsPage = new ToeicStudentsAdministrationPage(webDriver, testResultService);

        report.startStep("Create CSV with student and store it in folder 'Languages'");
        ToeicUploadResultPage toeicUploadResultPage = new ToeicUploadResultPage(webDriver, testResultService, toeicStudentsPage);

        toeicUploadResultPage.clickOnNextButton();
        sleep(5);

        String filePath ="";
        try{
            filePath = prepareDataToRegistrationFileAndSaving();
            sleep(4);


            report.startStep("Upload CSV and create new administration");
            sleep(5);
            ToeicUploadResultPage uploadResultPage = toeicStudentsPage.uploadCSV(new File(filePath));
            sleep(10);
            uploadResultPage.clickOnNextButton();

            report.startStep("Create session");
            String adminName = uploadResultPage.retrieveCreatedAdministrationName();
            uploadResultPage.clickOnCreateAdministration();
            sleep(7);
            uploadResultPage.verifyWhetherSessionWasCreated();


            report.startStep("Verify administration was storedOnDB");
            String administrationId = dbService.getAdministartionOfCertificationTest(date);
            textService.assertTrue("Administration not stored on DB", administrationId!=null);

            report.startStep("Delete administration from DB");
            report.startStep("Delete administration on ITS side");

            report.startStep("Exit from TMS");
            webDriver.switchToMainWindow();
            tmsHomePage.clickOnExit();


        }catch (Exception e){
            e.printStackTrace();

        }catch (AssertionError e){
            e.printStackTrace();
        }finally {
            File registeredStudent = new File(filePath);
            registeredStudent.delete();
            boolean fileStillExist = registeredStudent.exists();
            textService.assertEquals("File not deleted", false, fileStillExist);
        }


    }

    private String prepareDataToRegistrationFileAndSaving() throws Exception {
            String[] student = pageHelper.getStudentsByInstitutionId(institutionId);
            userName = student[0];
            String email = student[5];
            date = pageHelper.getCurrentDateByFormat("yyyy-MM-dd HH:mm:ss");
            LocalDateTime dateTime = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            dateTime = dateTime.plusDays(1);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            date = dateTime.format(formatter);
            String fileName = "StudentToUpload" + dbService.sig(4) + ".csv";
            String filePath = "\\" + String.join("\\", pageHelper.buildPathForExternalPages.substring(4).split("//")) + "\\Languages\\" + fileName;  //String fileName = "AddPackageToNewClass"+tailNumber+".csv";
            ToeicStudentsAdministrationPage toeicStudentsAdministrationPage = new ToeicStudentsAdministrationPage(webDriver, testResultService);
            AdministratorTitle title = toeicStudentsAdministrationPage.getAdministrationTitle(toeicStudentsAdministrationPage.getTitle());
            List<String[]> list = toeicStudentsPage.prepareListForCSVfile(userName, email, date, title );
            textService.writeArrayistToCSVFile(filePath, list);
        return filePath;
    }


    @Test
    @TestCaseParams(testCaseID = {"90679", "90676", "90674"})
    @Category(inProgressTests.class)
    public void verifyToeicOnlineTMSRedirect() throws Exception {

        report.startStep("Login as Admin");
        pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(configuration.getProperty("schoolAdmin.user"), institutionId));
        loginPage = new NewUXLoginPage(webDriver, testResultService);
        tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
        sleep(2);

        tmsHomePage.switchToMainFrame();

        report.startStep("Go to Test Assignment");
        tmsHomePage.clickOnAssessment();
        tmsHomePage.clickOnTestsAssignment();

        report.startStep("Select Toeic Test");
        tmsHomePage.switchToFormFrame();
        tmsHomePage.selectFeature("TS");

        report.startStep("Select any test type");
        webDriver.selectElementFromComboBox("SelectNewSchool", "Toeic");

        report.startStep("Go and verify redirection to appropriate URL");
        tmsHomePage.clickOnGo();
        webDriver.switchToTab(1);

        testResultService.assertEquals(true, webDriver.waitForSpecificCurrentUrl(String.format("%s/registration", PageHelperService.edxUrl)).contains("edexcellence"),
                "Redirect URL has not been activated");

    }

    @Test
    @TestCaseParams(testCaseID = {"90679", "90676"})
    @Category(inProgressTests.class)
    public void verifyToeicBridgeTMSRedirect() throws Exception {

        report.startStep("Login as Admin");
        pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(configuration.getProperty("schoolAdmin.user"), institutionId));
        loginPage = new NewUXLoginPage(webDriver, testResultService);
        tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
        sleep(2);

        tmsHomePage.switchToMainFrame();

        report.startStep("Go to Test Assignment");
        tmsHomePage.clickOnAssessment();
        tmsHomePage.clickOnTestsAssignment();

        report.startStep("Select Toeic Test");
        tmsHomePage.switchToFormFrame();
        tmsHomePage.selectFeature("TS");

        report.startStep("Select any test type");
        webDriver.selectElementFromComboBox("SelectNewSchool", "Toeic Bridge");

        report.startStep("Go and verify redirection to appropriate URL");
        tmsHomePage.clickOnGo();
        webDriver.switchToTab(1);

        testResultService.assertEquals(true, webDriver.waitForSpecificCurrentUrl(String.format("%s/registration", PageHelperService.edxUrl)).contains("edexcellence"),
                "Redirect URL has not been activated");

    }

    @Test
    @TestCaseParams(testCaseID = {"90675"})
    @Category(inProgressTests.class)
    public void openToeicTestAsTeacher() throws Exception {

        report.startStep("Login as Teacher");
        pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(configuration.getProperty("teacher.username"), autoInstitution.getInstitutionId()));
        loginPage = new NewUXLoginPage(webDriver, testResultService);
        sleep(2);
        tmsHomePage = loginPage.enterTeacherUserAndPassword();
        sleep(2);

        tmsHomePage.switchToMainFrame();

        report.startStep("Go to Test Assignment");
        tmsHomePage.clickOnAssessment();
        tmsHomePage.clickOnTestsAssignment();

        report.startStep("Select Toeic Test");
        tmsHomePage.switchToFormFrame();
        tmsHomePage.selectFeature("TS");

        //Need help here
        report.startStep("Verify available test types");

        //TODO add all elements methods

        report.startStep("Select any test type");

        webDriver.selectElementFromComboBox("SelectNewSchool", "Toeic Bridge");


        report.startStep("Go and verify redirection to appropriate URL");
        tmsHomePage.clickOnGo();

        //web driver switch to another tab
        webDriver.switchToTab(1);
       // webDriver.closeTab(0);

        testResultService.assertEquals(true, webDriver.waitForSpecificCurrentUrl(String.format("%s/registration", PageHelperService.edxUrl)).contains("edexcellence"),
                "Redirect URL has not been activated");

    }


    // The test are not passing because the table is dropped
    @Test
    @TestCaseParams(testCaseID = {"90521", "90523", "90524"})
    @Category(inProgressTests.class)
    public void verifyOMSPracticeTestReport() throws Exception {

        report.startStep("Login as Admin");
        loginPage = new NewUXLoginPage(webDriver, testResultService);
        pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(configuration.getProperty("schoolAdmin.user"), institutionId));
        sleep(1);
        tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
        homePage.closeAllNotifications();
        sleep(2);

        tmsHomePage.switchToMainFrame();

        report.startStep("Go to Practice test report");
        tmsHomePage.clickOnReports();
        tmsHomePage.clickOnCourseReports();

        report.startStep("90521 - Open practice test report");
        tmsHomePage.selectCourseReport("Practice Test Report");
        tmsHomePage.selectClass(testReportsClass);
        tmsHomePage.clickOnGo();

        //Need help here
        report.startStep("90523 - Verify report columns");

        tmsHomePage.verifyTestReportColumns(expectedPraciceTestHeaders);
        tmsHomePage.verifyPracticeTestReportScoreColumns();

        report.startStep("90524 - Verify report score tooltip");
        tmsHomePage.verifyTestReportTableValues();


    }

    // The test are not passing because the table is dropped
    @Test
    @TestCaseParams(testCaseID = {"90708", "90709"})
    @Category(inProgressTests.class)
    public void verifyStudentUnitTestReport() throws Exception {

        report.startStep("Login as Admin");
        loginPage = new NewUXLoginPage(webDriver, testResultService);
        pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(configuration.getProperty("schoolAdmin.user"), institutionId));
        sleep(1);
        tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
        homePage.closeAllNotifications();
        sleep(2);

        tmsHomePage.switchToMainFrame();

        report.startStep("Go to Practice test report");
        tmsHomePage.clickOnReports();
        tmsHomePage.clickOnCourseReports();

        report.startStep("90708 - Open student unit test report");
        tmsHomePage.selectCourseReport("Student Unit Test Score");
        tmsHomePage.selectClass(testReportsClass, false, true);

        //Need help here
        report.startStep("90709 - Verify report columns");

        tmsHomePage.verifyTestReportColumns(expectedUnitTestHeaders);

    }

    @Test
    @TestCaseParams(testCaseID = {"" })
    @Category(inProgressTests.class)
    public void checkThePLTCantReassignIfAllowPlacementConfigurationSelected() throws Exception {

        report.startStep("Restart browser in new URL");
        institutionName = institutionsName[22];
        pageHelper.restartBrowserInNewURL(institutionName, true);
        institutionId = dbService.getInstituteIdByName(institutionName);

        report.startStep("Init Needed Vars");
        String className = "Class" + dbService.sig(4);
        String[] institutionPackage = dbService.getUnExpiredInstitutionPackages(institutionId).get(0);
        pageHelper.createClassUsingSP("", className, institutionId, institutionPackage[1]);

        studentId = pageHelper.createUSerUsingSP(institutionId, className);
        String ClassId = dbService.getClassIdByClassName(className, institutionId);
        List<String[]> user = dbService.getUserNameAndPasswordByUserId(studentId);
        userName = user.get(0)[0];
        String password = user.get(0)[1];

        try {
            report.startStep("Login as Admin");
            loginPage = new NewUXLoginPage(webDriver, testResultService);
            String[] adminUser = dbService.getAdminUserByInstitutionId(institutionId);
            pageHelper.setUserLoginToNull(adminUser[2]);
            sleep(1);
            tmsHomePage = loginPage.enterSchoolAdminUserAndPassword(adminUser[0], adminUser[1]);
            homePage.closeAllNotifications();

            report.startStep("Initialize tms assessments automated tests page");
            TmsAssessmentsTestsAssignmentPage tmsAssessmentsTestsAssignmentPage = new TmsAssessmentsTestsAssignmentPage(webDriver, testResultService);

            report.startStep("Go to Assessments -> Test Assignment");
            tmsAssessmentsTestsAssignmentPage.goToAssessmentTestsAssignment();
            tmsAssessmentsTestsAssignmentPage.selectFeatureAndGo("PL");
            webDriver.switchToNewWindow();

            report.startStep("Click on add test-type");
            ToeicAdministrationPage administrationPage = new ToeicAdministrationPage(webDriver, testResultService);
            administrationPage.clickOnAddTestType();

            report.startStep("Chose test-type and click 'Add'");
            administrationPage.choseTestTypeAndClickADD(pltTestTypes[0]);

            report.startStep("Select class");
            administrationPage.selectClass(className, pltTestTypes[0]);

            report.startStep("Switch unlimited assignment");
            administrationPage.switchUnlimitedAssignment();
            sleep(1);

            report.startStep("Click on assign students");
            administrationPage.clickOnAssignStudents();

            report.startStep("Wait and verify success message");
            administrationPage.waitSuccessMessage();

            report.startStep("Click on Done button");
            administrationPage.clickOnDoneButton();

            report.startStep("Switch to main tab and exit from TMS");
            webDriver.closeTab(1);
            webDriver.switchToMainWindow();
            report.startStep("Go to test configuration");
            tmsAssessmentsTestsAssignmentPage.goToTestConfiguration();


            report.startStep("Select PT");
            tmsAssessmentsTestsAssignmentPage.selectFeatureAndGo("PT");
            tmsAssessmentsTestsAssignmentPage.selectAssignPlacementTestsToStudents();

            report.startStep("Go to Assessments -> Test Assignment");
            tmsAssessmentsTestsAssignmentPage.goToAssessmentTestsAssignment();
            tmsAssessmentsTestsAssignmentPage.selectFeatureAndGo("PL");
            webDriver.switchToNewWindow();


            //      report.startStep("Click on add test-type");
            //            ToeicAdministrationPage administrationPage = new ToeicAdministrationPage(webDriver, testResultService);
            //            administrationPage.clickOnAddTestType();
            //
            //            report.startStep("Chose test-type and click 'Add'");
            //            administrationPage.choseTestTypeAndClickADD(pltTestTypes[0]);
            //
            //            report.startStep("Select class");
            //            administrationPage.selectClass(className, pltTestTypes[0]);
            //
            //            report.startStep("Switch unlimited assignment");
            //            administrationPage.switchUnlimitedAssignment();
            //            sleep(1);
            //
            //            report.startStep("Click on assign students");
            //            administrationPage.clickOnAssignStudents();
            //
            //            report.startStep("Wait and verify success message");
            //            administrationPage.waitSuccessMessage();
            //TODO check notification that not impossible to reassign PLT test


            //   report.startStep("Switch to main tab and exit from TMS");
            //            webDriver.closeTab(1);
            //            webDriver.switchToMainWindow();
            //            tmsHomePage.clickOnExit();



        } catch (Exception | AssertionError err) {
            err.printStackTrace();
            testResultService.addFailTest(err.getMessage(), true, true);

        } finally {

            dbService.deleteUserById(studentId);
            dbService.deleteClassByName(institutionId, className);
        }
    }


    @Test
    @TestCaseParams(testCaseID = {"" })
    @Category(inProgressTests.class)
    public void checkThePLTReassignIfAllowPlacementConfigurationNotSelected() throws Exception {

        report.startStep("Init Needed Vars");
        String className = "Class" + dbService.sig(4);
        String[] institutionPackage = dbService.getUnExpiredInstitutionPackages(institutionId).get(0);
        pageHelper.createClassUsingSP("", className, institutionId, institutionPackage[1]);

        studentId = pageHelper.createUSerUsingSP(institutionId, className);
        String ClassId = dbService.getClassIdByClassName(className, institutionId);
        List<String[]> user = dbService.getUserNameAndPasswordByUserId(studentId);
        userName = user.get(0)[0];
        String password = user.get(0)[1];

        try {
            report.startStep("Login as Admin");
            loginPage = new NewUXLoginPage(webDriver, testResultService);
            String[] adminUser = dbService.getAdminUserByInstitutionId(institutionId);
            pageHelper.setUserLoginToNull(adminUser[2]);
            sleep(1);
            tmsHomePage = loginPage.enterSchoolAdminUserAndPassword(adminUser[0], adminUser[1]);
            homePage.closeAllNotifications();

            report.startStep("Initialize tms assessments automated tests page");
            TmsAssessmentsTestsAssignmentPage tmsAssessmentsTestsAssignmentPage = new TmsAssessmentsTestsAssignmentPage(webDriver, testResultService);

            report.startStep("Go to Assessments -> Test Assignment");
            tmsAssessmentsTestsAssignmentPage.goToAssessmentTestsAssignment();
            tmsAssessmentsTestsAssignmentPage.selectFeatureAndGo("PL");
            webDriver.switchToNewWindow();

            report.startStep("Click on add test-type");
            ToeicAdministrationPage administrationPage = new ToeicAdministrationPage(webDriver, testResultService);
            administrationPage.clickOnAddTestType();

            report.startStep("Chose test-type and click 'Add'");
            administrationPage.choseTestTypeAndClickADD(pltTestTypes[0]);

            report.startStep("Select class");
            administrationPage.selectClass(className, pltTestTypes[0]);

            report.startStep("Switch unlimited assignment");
            administrationPage.switchUnlimitedAssignment();
            sleep(1);

            report.startStep("Click on assign students");
            administrationPage.clickOnAssignStudents();

            report.startStep("Wait and verify success message");
            administrationPage.waitSuccessMessage();

            report.startStep("Click on Done button");
            administrationPage.clickOnDoneButton();

            report.startStep("Switch to main tab and exit from TMS");
            webDriver.closeTab(1);
            webDriver.switchToMainWindow();
            report.startStep("Go to test configuration");
            tmsAssessmentsTestsAssignmentPage.goToTestConfiguration();


            report.startStep("Select PT");
            tmsAssessmentsTestsAssignmentPage.selectFeatureAndGo("PT");
            tmsAssessmentsTestsAssignmentPage.unSelectAssignPlacementTestsToStudents();

            report.startStep("Go to Assessments -> Test Assignment");
            tmsAssessmentsTestsAssignmentPage.goToAssessmentTestsAssignment();
            tmsAssessmentsTestsAssignmentPage.selectFeatureAndGo("PL");
            webDriver.switchToNewWindow();


            //      report.startStep("Click on add test-type");
            //            ToeicAdministrationPage administrationPage = new ToeicAdministrationPage(webDriver, testResultService);
            //            administrationPage.clickOnAddTestType();
            //
            //            report.startStep("Chose test-type and click 'Add'");
            //            administrationPage.choseTestTypeAndClickADD(pltTestTypes[0]);
            //
            //            report.startStep("Select class");
            //            administrationPage.selectClass(className, pltTestTypes[0]);
            //
            //            report.startStep("Switch unlimited assignment");
            //            administrationPage.switchUnlimitedAssignment();
            //            sleep(1);
            //
            //            report.startStep("Click on assign students");
            //            administrationPage.clickOnAssignStudents();
            //
            //            report.startStep("Wait and verify success message");
            //            administrationPage.waitSuccessMessage();
            //TODO check notification that PLT was reassign successfully


            //   report.startStep("Switch to main tab and exit from TMS");
            //            webDriver.closeTab(1);
            //            webDriver.switchToMainWindow();
            //            tmsHomePage.clickOnExit();

            //    report.startStep("Login to ED");
            //            loginPage.waitForPageToLoad();
            //            homePage = loginPage.loginAsStudent(userName, password);
            //            pageHelper.skipOptin();
            //            homePage.closeAllNotifications();
            //            pageHelper.skipOnBoardingHP();
            //            homePage.waitForPageToLoad();
            //            sleep(10);
            //            homePage.clickOnHomeButton();
            //            homePage.waitHomePageloaded();
            //
            //            report.startStep("Init Data");
            //
            //            String TITLE_PLT = "ED Placement";
            //
            //            TestEnvironmentPage testEnvironmentPage = new TestEnvironmentPage(webDriver, testResultService);
            //
            //            report.startStep("Verify whether test available in assessments area");
            //            NewUxAssessmentsPage testsPage = homePage.openAssessmentsPage(false);
            //            sleep(3);
            //            testsPage.checkItemsCounterBySection("1", "1");
            //            testsPage.checkTestDisplayedInSectionByTestName(testTypes[0], "1", "1");

            // report.startStep("Logout from ED");
            // homePage.clickOnLogOut();



        } catch (Exception | AssertionError err) {
            err.printStackTrace();
            testResultService.addFailTest(err.getMessage(), true, true);

        } finally {

            dbService.deleteUserById(studentId);
            dbService.deleteClassByName(institutionId, className);
        }
    }

    @Test
    @TestCaseParams(testCaseID = {"" })
    @Category(inProgressTests.class)
    public void checkLastStudentProgressDisplayedOnPltReport() throws Exception {

        report.startStep("Init Needed Vars");
        String className = "Class"+dbService.sig(4);
        String[] institutionPackage = dbService.getUnExpiredInstitutionPackages(institutionId).get(0);
        pageHelper.createClassUsingSP("",className,institutionId,institutionPackage[1]);

        studentId = pageHelper.createUSerUsingSP(institutionId, className);
        String ClassId = dbService.getClassIdByClassName(className, institutionId);
        List<String[]> user = dbService.getUserNameAndPasswordByUserId(studentId);
        userName = user.get(0)[0];
        String password = user.get(0)[1];
        String userFirsName = user.get(0)[3];

        try {
            report.startStep("Login as Admin");
            loginPage = new NewUXLoginPage(webDriver, testResultService);
            String[] adminUser = dbService.getAdminUserByInstitutionId(institutionId);
            pageHelper.setUserLoginToNull(adminUser[2]);
            sleep(1);
            tmsHomePage = loginPage.enterSchoolAdminUserAndPassword(adminUser[0], adminUser[1]);
            homePage.closeAllNotifications();

            report.startStep("Initialize tms assessments automated tests page");
            TmsAssessmentsTestsAssignmentPage tmsAssessmentsTestsAssignmentPage = new TmsAssessmentsTestsAssignmentPage(webDriver, testResultService);

            report.startStep("Go to Assessments -> Test Assignment");
            tmsAssessmentsTestsAssignmentPage.goToAssessmentTestsAssignment();
            tmsAssessmentsTestsAssignmentPage.selectFeatureAndGo("PL");
            webDriver.switchToNewWindow();

            report.startStep("Click on add test-type");
            ToeicAdministrationPage administrationPage = new ToeicAdministrationPage(webDriver, testResultService);
            administrationPage.clickOnAddTestType();

            report.startStep("Chose test-type and click 'Add'");
            administrationPage.choseTestTypeAndClickADD(pltTestTypes[2]);

            report.startStep("Select class");
            administrationPage.selectClass(className,pltTestTypes[2]);

            report.startStep("Switch unlimited assignment");
            administrationPage.switchUnlimitedAssignment();
            sleep(1);

            report.startStep("Click on assign students");
            administrationPage.clickOnAssignStudents();

            report.startStep("Wait and verify success message");
            administrationPage.waitSuccessMessage();

            report.startStep("Click on Done button");
            administrationPage.clickOnDoneButton();

            report.startStep("Switch to main tab and exit from TMS");
            webDriver.closeTab(1);
            webDriver.switchToMainWindow();
            tmsHomePage.clickOnExit();

            report.startStep("Login to ED");
            loginPage.waitForPageToLoad();
            homePage = loginPage.loginAsStudent(userName,password);
            pageHelper.skipOptin();
            homePage.closeAllNotifications();
            pageHelper.skipOnBoardingHP();
            homePage.waitForPageToLoad();
            sleep(10);
            homePage.clickOnHomeButton();
            homePage.waitHomePageloaded();

            report.startStep("Verify whether test available in assessments area");
            NewUxAssessmentsPage testsPage = homePage.openAssessmentsPage(false);
            sleep(3);
            testsPage.checkItemsCounterBySection("1", "1");
            testsPage.checkTestDisplayedInSectionByTestName(testTypes[0], "1", "1");

            report.startStep("Click on the Start test on Assessment");
            testsPage.clickStartTest(1, 1);
            sleep(4);

            report.startStep("Pass explanations before test");
            webDriver.switchToNewWindow();
            TOEICTestPage toeicTestPage = new TOEICTestPage(webDriver,testResultService);
            toeicTestPage.waitTillToeicTestDownloadedAncClickOnStart();

            report.startStep("Verify whether test running");
            String parentWindowHandle = webDriver.switchToPopup();
            toeicTestPage.verifyTestIsRunning();

            report.startStep("Close test window and switch to main window");
            webDriver.getWebDriver().close();
            webDriver.getWebDriver().switchTo().window(parentWindowHandle);
            webDriver.closeTab(1);
            webDriver.switchToMainWindow();

            report.startStep("Logout from ED");
            homePage.clickOnLogOut();

            report.startStep("Run DW to update the status");
            dbService.runDWJob(studentId, ClassId);

            report.startStep("Restart browser in new URL");
            institutionName = institutionsName[22];
            pageHelper.restartBrowserInNewURL(institutionName, true);
            institutionId = dbService.getInstituteIdByName(institutionName);

            report.startStep("Login as Admin");
            loginPage = new NewUXLoginPage(webDriver, testResultService);
            loginPage.loginAsStudent(adminUser[0], adminUser[1]);
            sleep(1);

            report.startStep("Go to Reports");
            TmsReportsPage tmsReportsPage = new TmsReportsPage(webDriver, testResultService);
            tmsHomePage.switchToMainFrame();
            tmsHomePage.clickOnReports();
            sleep(1);
            tmsHomePage.clickOnPLTReports();
            webDriver.switchToNewWindow();

            report.startStep("Click Toiec button on PLT report");
            tmsReportsPage.clickToeicTypeReportButton();
            sleep(10);

            report.startStep("Click classes dropdown");
            tmsReportsPage.clickClassesDropdown();
            sleep(2);
            tmsReportsPage.clickInputField();

            report.startStep("Filter by " +className);
            tmsReportsPage.filterByClasses(className);
            sleep(3);

            report.startStep("Select checkbox with " +className);
            tmsReportsPage.selectClassesCheckbox(className);
            sleep(5);

            report.startStep("Click out for applying the filter");
            tmsReportsPage.applyFilter();
            sleep(3);

            report.startStep("Verify test status on DB");
            String resultStatus = dbService.getPltTestStatus(studentId);
            testResultService.assertEquals("Test In progress", resultStatus);

            report.startStep("verify test status on UI");
            testResultService.assertEquals("In progress", tmsReportsPage.isStatusDisplayed());

            report.startStep("Switch to main tab");

            webDriver.closeTab(1);
            webDriver.switchToMainWindow();


//            report.startStep("Go to Assessments -> Test Assignment");
//            tmsAssessmentsTestsAssignmentPage.goToAssessmentTestsAssignment();
//            tmsAssessmentsTestsAssignmentPage.selectFeatureAndGo("PL");
//            webDriver.switchToNewWindow();
//
//            report.startStep("Click on add test-type");
//            administrationPage.clickOnAddTestType();
//
//            report.startStep("Chose test-type and click 'Add'");
//            administrationPage.choseTestTypeAndClickADD(pltTestTypes[0]);
//
//            report.startStep("Select class");
//            administrationPage.selectClass(className, pltTestTypes[0]);
//
//            report.startStep("Switch unlimited assignment");
//            administrationPage.switchUnlimitedAssignment();
//            sleep(1);
//
//            report.startStep("Click on assign students");
//            administrationPage.clickOnAssignStudents();
//
//            report.startStep("Wait and verify success message");
//            administrationPage.waitSuccessMessage();
//
//            report.startStep("Click on Done button");
//            administrationPage.clickOnDoneButton();
//

//            report.startStep("Login to ED");
//            loginPage.waitForPageToLoad();
//            homePage = loginPage.loginAsStudent(userName, password);
//            pageHelper.skipOptin();
//            homePage.closeAllNotifications();
//            pageHelper.skipOnBoardingHP();
//            homePage.waitForPageToLoad();
//            sleep(10);
//            homePage.clickOnHomeButton();
//            homePage.waitHomePageloaded();
//
//            report.startStep("Init Data");
//
//            String TITLE_PLT = "ED Placement";
//
//            TestEnvironmentPage testEnvironmentPage = new TestEnvironmentPage(webDriver, testResultService);
//
//            report.startStep("Verify whether test available in assessments area");
//            NewUxAssessmentsPage testsPage = homePage.openAssessmentsPage(false);
//            sleep(3);
//            testsPage.checkItemsCounterBySection("1", "1");
//            testsPage.checkTestDisplayedInSectionByTestName(testTypes[0], "1", "1");
//
//            report.startStep("Click on the Start test on Assessment");
//            testsPage.clickStartTest(1, 1);
//            sleep(4);
//
//            studentId = dbService.getUserIdByUserName(BasicNewUxTest.userName, institutionId);
//            List<String[]> userTestAdministrations = dbService.getUserTestAdministrationsDetailsByStudentId(studentId);
//            String userTestTokenDB = userTestAdministrations.get(0)[7];
//            testResultService.assertEquals(false, userTestTokenDB == null, "User Test Token is NULL");
//
//            report.startStep("Verify Title");
//            testEnvironmentPage.validateTestName(TITLE_PLT);
//            sleep(1);
//
//            report.startStep("Perform PLT Test");
//            testEnvironmentPage.clickLevelButton();
//            testEnvironmentPage.pressOnStartTest();
//            testEnvironmentPage.clickNext();
//            testEnvironmentPage.selectRadioButton();
//            testEnvironmentPage.submitPLTTestWithoutAnswers();
//
//            report.startStep("Logout from ED");
//            homePage.clickOnLogOut();
//
//            report.startStep("Run DW to update the status");
//            dbService.runDWJob(UserID, ClassId);
//
//            report.startStep("Restart browser in new URL");
//            institutionName = institutionsName[22];
//            pageHelper.restartBrowserInNewURL(institutionName, true);
//            institutionId = dbService.getInstituteIdByName(institutionName);
//
//            report.startStep("Login as Admin");
//            loginPage = new NewUXLoginPage(webDriver, testResultService);
//            loginPage.loginAsStudent(adminUser[0], adminUser[1]);
//            sleep(1);
//
//            report.startStep("Go to Reports");
//            TmsReportsPage tmsReportsPage = new TmsReportsPage(webDriver, testResultService);
//            tmsHomePage.switchToMainFrame();
//            tmsHomePage.clickOnReports();
//            sleep(1);
//            tmsHomePage.clickOnPLTReports();
//            webDriver.switchToNewWindow();
//
//            sleep(40);
//
//            report.startStep("Click classes dropdown");
//            tmsReportsPage.clickClassesDropdown();
//            sleep(2);
//            tmsReportsPage.clickInputField();
//
//            report.startStep("Filter by " + className);
//            tmsReportsPage.filterByClasses(className);
//            sleep(3);
//
//            report.startStep("Select checkbox with " + className);
//            tmsReportsPage.selectClassesCheckbox(className);
//            sleep(5);
//
//            report.startStep("Click out for applying the filter");
//            tmsReportsPage.applyFilter();
//            sleep(3);
//
//            report.startStep("Verify test status on DB");
//            resultStatus = dbService.getPltTestStatus(UserID);
//            testResultService.assertEquals("Test is Done", resultStatus);
//
//            report.startStep("Verify test status on UI");
//            testResultService.assertEquals("Done", tmsReportsPage.isStatusDisplayed());



//            report.startStep("Switch to main tab and exit from TMS");
//            webDriver.closeTab(1);
//            webDriver.switchToMainWindow();
//            tmsHomePage.clickOnExit();





        } catch (Exception | AssertionError err) {
            err.printStackTrace();
            testResultService.addFailTest(err.getMessage(), true, true);

        } finally {

            dbService.deleteUserById(studentId);
            dbService.deleteClassByName(institutionId, className);
        }
    }




}
