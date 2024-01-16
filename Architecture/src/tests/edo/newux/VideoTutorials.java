package tests.edo.newux;

import org.junit.*;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.WebElement;

import Interfaces.TestCaseParams;
import pageObjects.edo.NewUXLoginPage;
import testCategories.reg1;
import pageObjects.edo.NewUxVideoTutorialsPage;;

@Category(reg1.class)
public class VideoTutorials extends BasicNewUxTest {

    NewUxVideoTutorialsPage videoPage;
    NewUXLoginPage loginPage;

    @Before
    public void setup() throws Exception {
        //	institutionName =institutionsName[10];
        super.setup();
        //	pageHelper.restartBrowserInNewURL(institutionName,true);

        //webDriver.maximize();
        //report.startStep("Get user and login");
        getUserAndLoginNewUXClass();

        String className = "classNewUnits";
        //	getUserAndLoginNewUXClass(className, institutionId);
        homePage.skipNotificationWindow();
    }

    @Test
    @TestCaseParams(testCaseID = {"76581"})
    public void testVideoTutorialsPlay() throws Exception {

        //	webDriver.maximize();
        //	report.startStep("Get user and login");
        //	getUserAndLoginNewUXClass();
        //	homePage.skipNotificationWindow();

        report.startStep("Open Help and select Video Tutorials");
        homePage.clickOnVideoTutorials();
        sleep(1);
        NewUxVideoTutorialsPage videoPage = new NewUxVideoTutorialsPage(webDriver, testResultService);

        //videoPage = new NewUxVideoTutorialsPage (webDriver,testResultService);

        report.startStep("Verify that title shown and correct");
        testResultService.assertEquals("Video Tutorials", videoPage.verifyVideoTutorialsPageHeader());

        report.startStep("Verify Multiple video Display");
        WebElement element = videoPage.checkAndGetVideoElements(videoPage.VIDEO_NAME_TIME_MANAGEMENT);
        element.click();

        report.startStep("Verify that Video Plays");
        videoPage.playVideo();
        sleep(2);

        report.startStep("Verify that Video Paused");
        videoPage.pauseVideo();
        sleep(2);

        report.startStep("Log Out");
        homePage.logOutOfED();

    }

    @Test
    @TestCaseParams(testCaseID = {"76581"})
    public void verifyAssessmentsVideoExistsForNewTE() throws Exception {

        report.startStep("Open Help and select Video Tutorials");
        homePage.clickOnVideoTutorials();
        sleep(1);

        videoPage = new NewUxVideoTutorialsPage(webDriver, testResultService);

        report.startStep("Verify that title shown and correct");
        testResultService.assertEquals("Video Tutorials", videoPage.verifyVideoTutorialsPageHeader());

        report.startStep("Verify Multiple video Display");
        WebElement element = videoPage.checkAssessmentVideoExists();
        element.click();

        report.startStep("Verify that Video Plays");
        videoPage.playVideo();
        sleep(2);

        report.startStep("Verify that Video Paused");
        videoPage.pauseVideo();
        //sleep(2);

        report.startStep("Log Out");
        homePage.logOutOfED();

    }

    @Test
    @TestCaseParams(testCaseID = {"93705"})
    public void verifyToeicVideoPresentEdExcellence() throws Exception {
        institutionName = institutionsName[10];
        pageHelper.restartBrowserInNewURL(institutionName,true);
        report.startStep("Init test data");
        studentId = pageHelper.createUSerUsingSP(institutionId, configuration.getProperty("classname.CustomizeCourse"));
        String[] user = dbService.getUserNameAndPasswordByUserId(studentId).get(0);
        try {
        report.startStep("Create new user with ");
        loginPage = new NewUXLoginPage(webDriver, testResultService);
        homePage = loginPage.loginAsStudent(user[0], user[1]);
        loginPage.waitLoginAfterRestartAppPool();
        pageHelper.skipOptin();
        homePage.closeAllNotifications();
        pageHelper.skipOnBoardingHP();
        homePage.waitHomePageloadedFully();

        report.startStep("Open Help and select Video Tutorials");
        homePage.clickOnVideoTutorials();
        sleep(1);

        NewUxVideoTutorialsPage videoPage = new NewUxVideoTutorialsPage(webDriver, testResultService);

        report.startStep("Verify that title shown and correct");
        testResultService.assertEquals("Video Tutorials", videoPage.verifyVideoTutorialsPageHeader());

        report.startStep("Verify TOEIC video Display");
        videoPage.checkAndGetVideoElements(videoPage.VIDEO_NAME_ASSESSMENT);

        report.startStep("Log Out");
        homePage.logOutOfED();

        } catch (Exception e) {

        } finally {
            dbService.deleteUserById(studentId);
        }
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }
}