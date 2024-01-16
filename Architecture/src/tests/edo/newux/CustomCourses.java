package tests.edo.newux;

import Interfaces.TestCaseParams;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import pageObjects.edo.NewUXLoginPage;
import pageObjects.edo.NewUxHomePage;
import pageObjects.tms.TmsReportsPage;
import pageObjects.toeic.ToeicLearningAreaAndProgressPage;
import pageObjects.toeic.toeicQuestionPage;
import pageObjects.toeic.toeicStartPage;
import testCategories.inProgressTests;
import java.util.ArrayList;



public class CustomCourses extends BasicNewUxTest {

    NewUXLoginPage loginPage;


    @Before
    public void setup() throws Exception {
        institutionName = institutionsName[10];
        super.setup();
    }

    ToeicLearningAreaAndProgressPage makeProgressInCoursePage;

    @Test
    @TestCaseParams(testCaseID = {"93332"})
    @Category(inProgressTests.class)
    public void testCustomizeCourseOnUnitAndReport() throws Exception {
        NewUxHomePage newUxHomePage = new NewUxHomePage(webDriver, testResultService);
        report.startStep("Init test data");
        String reportType = "Student Unit Test Score";
        String className = configuration.getProperty("classname.CustomizeCourse");
        String expectedResult = "23";
        int numOfSections = numOfSectionsAnsweredTOEIC[3];
        String pathToAnswers = "files/TOEIC/UnitTestEDExcellenceFlexibility.csv";
        studentId = pageHelper.createUSerUsingSP(institutionId, configuration.getProperty("classname.CustomizeCourse"));
        String[] user = dbService.getUserNameAndPasswordByUserId(studentId).get(0);
        try {
            report.startStep("Create new user" );
            loginPage = new NewUXLoginPage(webDriver,testResultService);
            homePage = loginPage.loginAsStudent(user[0], user[1]);
            loginPage.waitLoginAfterRestartAppPool();
            pageHelper.skipOptin();
            homePage.closeAllNotifications();
            pageHelper.skipOnBoardingHP();
            homePage.waitHomePageloadedFully();

            String courseName = newUxHomePage.getCustomCourseName();

            report.startStep("Select from dropdown  " + courseName);
            homePage.clickOnCourseList();
            homePage.scrollListLessons(courseName);
            homePage.clickCourseNameFromList(courseName);

            report.startStep("Accomplish unit test");
            makeProgressInCoursePage = new ToeicLearningAreaAndProgressPage(webDriver, testResultService);
            homePage.startUnitTestForCourse(1, courseName);

            report.startStep("Press Start New Test if 'Resume Test' Option is Present");
            toeicStartPage toeicStartpage = new toeicStartPage(webDriver, testResultService);
            toeicStartpage.pressStartNewTestInResumePopUp();
            report.startStep("Validate 'Test Your Sound' Button is Clickable");
            toeicStartpage.checkTestSoundButtonIsClickable();
            report.startStep("Validate the Welcome Text is Not Null");
            toeicStartpage.validateTheWelcomeTextIsNotNull();
            report.startStep("Click Start");
            toeicStartpage.clickStart();
            sleep(1);

            report.startStep("Initialize toeic questions page");
            toeicQuestionPage toeicQuestionpage = new toeicQuestionPage(webDriver, testResultService);
            report.startStep("Click Next");
            toeicQuestionpage.clickNext();
            report.startStep("Answer Questions");
            ArrayList<String> UnitTestAnswers2 = toeicQuestionpage.answerQuestionsInSeveralSections(numOfSections, pathToAnswers);
            report.startStep("Click Submit");
            sleep(2);
            toeicQuestionpage.submit(true);
            sleep(1);
            report.startStep("Close The Test");
            toeicQuestionpage.clickCloseButton();
            webDriver.switchToMainWindow();
            report.startStep("Log Out of ED");
            homePage.logOutOfED();

            report.startStep("Login as Admin");

            loginPage = new NewUXLoginPage(webDriver, testResultService);
            String[] adminUser = dbService.getAdminUserByInstitutionId(institutionId);
            loginPage.loginAsStudent(adminUser[0], adminUser[1]);
            sleep(1);

            homePage.waitUntilLoadingMessageIsOver();

            report.startStep("Navigate to course report page");
            TmsReportsPage tmsReportsPage = new TmsReportsPage(webDriver, testResultService);
            tmsReportsPage.goToCourseReports();
            sleep(3);
            String courseId = getCourseIdByCourseCustomCode(courseName);

            report.startStep("Select report and class");
            tmsReportsPage.selectUnitCourseReportAndClassAndCourse(reportType, className, courseId);
            sleep(2);

            report.startStep("Filter by Date Uts");
            tmsReportsPage = new TmsReportsPage(webDriver, testResultService);
            tmsReportsPage.filterByDateUtS();
            sleep(2);

            report.startStep("Verify the report is displayed with the last data");
            String lastUpdate = pageHelper.getCurrentDateByFormat("dd/MM/yyyy HH:mm").replaceAll(" \\d{2}:\\d{2}$", "");
            testResultService.assertEquals(lastUpdate, tmsReportsPage.checkUpdatedData(), "the report is not displayed");

            report.startStep("Verify test-scores");
            testResultService.assertEquals(expectedResult, tmsReportsPage.getTextFromScoreColumn());

            report.startStep("Quit Browser");
            webDriver.quitBrowser();


        } catch (Exception e) {

        } finally {
            dbService.deleteUserById(studentId);
        }
    }
}
