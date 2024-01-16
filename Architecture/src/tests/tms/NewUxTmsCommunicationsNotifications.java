package tests.tms;

import Interfaces.TestCaseParams;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pageObjects.CreateNewNotificationPage;
import pageObjects.EdoNotificationPage;
import pageObjects.NotificationsPage;
import pageObjects.PushNotificationsPage;
import pageObjects.edo.NewUXLoginPage;
import pageObjects.edo.NewUxCommunityPage;
import pageObjects.edo.NewUxLearningArea;
import pageObjects.edo.NewUxMyProfile;
import pageObjects.tms.DashboardPage;
import pageObjects.tms.TmsHomePage;
import tests.edo.newux.SpeechRecognitionBasicTestNewUX;

import java.util.ArrayList;

public class NewUxTmsCommunicationsNotifications extends SpeechRecognitionBasicTestNewUX {

    protected NewUxCommunityPage communityPage;
    NewUXLoginPage loginPage;
    DashboardPage dashboardPage;
    TmsHomePage tmsHomePage;
    NewUxLearningArea learningArea;
    NewUxMyProfile myProfile;
    NotificationsPage notificationsPage;
    CreateNewNotificationPage createNewNotificationPage;
    EdoNotificationPage edoNotificationPage;

    String instID = "";
    String userName = "";
    String notificationName;
    boolean isDeleted = false;

    @Before
    public void setup() throws Exception {
        super.setup();
    }

    @Test
    @TestCaseParams(testCaseID = "95463")
    public void pushNotifications() throws Exception {

        institutionName = institutionsName[22];
        pageHelper.initializeData();
        String pathToNotificationsJSON = "\\\\frontqa2016\\wwwroot\\EdUiWebServices\\Resources\\";

        report.startStep("Close ED and Open TMS");
            tmsHomePage = pageHelper.closeEDAndOpenTMS();

        report.startStep("Login as tmsdomain");
            tmsHomePage.loginAsTmsdomain();

        report.startStep("Click on Communication -> Push Notification Center");
            notificationsPage = new NotificationsPage(webDriver, testResultService);
            notificationsPage.goToPushNotificationsCenter();

        report.startStep("Create new notification");
            String oldWindow = webDriver.switchToNewWindow();
            PushNotificationsPage pushNotificationsPage = new PushNotificationsPage(webDriver, testResultService);

        report.startStep("Push notification");
            String notificationName = pushNotificationsPage.createNewNotification(institutionName);
            pushNotificationsPage.pushNotification();

        report.startStep("Verify JSON, whether notification was really stored");
            JSONArray jsonArr = netService.getJsonsArray(pathToNotificationsJSON, "pushNotifications", false);
            boolean savedInJSON = verifyThatNotificationStoredInJSON(jsonArr,notificationName);
            testResultService.assertTrue("Notification didn't saved in JSON",savedInJSON);

        report.startStep("Edit notification and verify changes in JSON");
            notificationName = "EditedNotification"+dbService.sig(4);
            pushNotificationsPage.editNotification(notificationName);
            jsonArr = netService.getJsonsArray(pathToNotificationsJSON, "pushNotifications", false);
            savedInJSON = verifyThatNotificationStoredInJSON(jsonArr,notificationName);
            testResultService.assertTrue("Notification didn't saved in JSON",savedInJSON);

        report.startStep("Delete just created notification");
            pushNotificationsPage.deleteNotification();

        report.startStep("Verify that notification deleted from JSON");
            jsonArr = netService.getJsonsArray(pathToNotificationsJSON, "pushNotifications", false);
            savedInJSON = verifyThatNotificationStoredInJSON(jsonArr,notificationName);
            testResultService.assertTrue("Notification wasn't deleted", !savedInJSON);;

        report.startStep("Exit from TMS");
            webDriver.closeTab(1);
            webDriver.switchToMainWindow();
            tmsHomePage.clickOnExitTMS();

    }

    private boolean verifyThatNotificationStoredInJSON(JSONArray jsonArr, String notificationName) {
        JSONObject jsonObj = new JSONObject();
        boolean savedInJSON = false;
        for (int i = 0; i < jsonArr.length(); i++) {
            jsonObj = (JSONObject) jsonArr.get(i);
            String notificationTitle = (String) jsonObj.get("notificationTitle");
            if (notificationTitle.equalsIgnoreCase(notificationName)) {
                savedInJSON = true;
                break;
            }
        }
        return savedInJSON;
    }

    @Test
    @TestCaseParams(testCaseID = {"73808", "73691", "73735", "73742"})
    public void testCreateMessage() throws Exception {

        try {

            // Test Data
            notificationName = "Qt test (name) - " + dbService.sig(5);
            String notificationDate = "This is a date - " + dbService.sig(5);
            String notificationTitle = "This is a title - " + dbService.sig(5);
            String notificationMessage = "This is a test message - " + dbService.sig(5);

            ArrayList<String> classesList = new ArrayList<String>();
            String className = configuration.getNotificationClassName();
            classesList.add(className);

            ArrayList<String> rolesList = new ArrayList<String>();
            rolesList.add("Teacher");
            rolesList.add("Student");

            // Initialize login page
            loginPage = new NewUXLoginPage(webDriver, testResultService);

            report.startStep("Login as Admin");
            tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
            homePage.waitUntilLoadingMessageIsOver();

            // Initialize notifications page
            notificationsPage = new NotificationsPage(webDriver, testResultService);

            // Go to notification center
            notificationsPage.goToNotificationCenter();

            // Initialize create new notification page
            createNewNotificationPage = new CreateNewNotificationPage(webDriver, testResultService);

            // Create new notification
            createNewNotificationPage.createNewNotification(notificationName, "admin", classesList, rolesList, notificationDate, notificationTitle, notificationMessage);
            sleep(3);

            report.startStep("Verify the created message is in hidden status");
            notificationsPage.checkNewNotificationAppearsHiddenInTable(notificationName);

            report.startStep("Publish the Notification");
            notificationsPage.publishNotification(notificationName);
            sleep(1);

            report.startStep("Logout of TMS as Admin");
            tmsHomePage.clickOnExitTMS();

            report.startStep("Login to ED as Student");
            //getUserAndLoginNewUXClass();
            getUserAndLoginGeneral(className, true, false);
            sleep(3);

            // Initialize ed notification page
            edoNotificationPage = new EdoNotificationPage(webDriver, testResultService);

            // Validate the notification appears in log in page, press don't show again, close the notification
            edoNotificationPage.checkNotificationIsDisplayed(0, notificationDate, notificationTitle, notificationMessage, null, true, true);

            // Close modal pop up if it appears
            homePage.closeModalPopUp();

            report.startStep("Logout of ED as Student");
            homePage.clickOnLogOut();

            report.startStep("Login as Teacher");
            pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(configuration.getProperty("teacher.username"), autoInstitution.getInstitutionId()));
            sleep(2);
            tmsHomePage = loginPage.enterTeacherUserAndPassword();
            sleep(2);

            // Validate the notification appears in log in page and close the notification
            edoNotificationPage.checkNotificationIsDisplayed(0, notificationDate, notificationTitle, notificationMessage, null, false, true);

            // Go to notification center
            notificationsPage.goToNotificationCenter();

            // Validate that the message does not appear in the table
            report.startStep("Verify Notification is Not Shown in The Table");
            notificationsPage.verifyNotificationIsNotDisplayedInTheTable(notificationName);

            report.startStep("Logout of TMS as Teacher");
            tmsHomePage.clickOnExitTMS();

            report.startStep("Login as Admin");
            tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
            homePage.waitUntilLoadingMessageIsOver();

            // Go to notification center
            notificationsPage.goToNotificationCenter();

            report.startStep("Delete the Notification");
            notificationsPage.deleteNotification(notificationName);
            sleep(1);

            report.startStep("Verify the Notification is Deleted");
            isDeleted = notificationsPage.verifyNotificationIsNotDisplayedInTheTable(notificationName);

            report.startStep("Logout of TMS as Admin");
            tmsHomePage.clickOnExitTMS();
            sleep(2);

            report.startStep("Login as Teacher");
            pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(configuration.getProperty("teacher.username"), autoInstitution.getInstitutionId()));
            sleep(2);
            tmsHomePage = loginPage.enterTeacherUserAndPassword();
            sleep(2);

            report.startStep("Verify the deleted Notification is not shown");
            edoNotificationPage.checkNotificationIsNotDisplayed(notificationTitle);

            tmsHomePage.switchToMainFrame();

            report.startStep("Logout of TMS as Teacher");
            tmsHomePage.clickOnExitTMS();

            report.startStep("Login to ED as Student");
            //getUserAndLoginNewUXClass();
            getUserAndLoginGeneral(className, true, false);
            sleep(3);

            report.startStep("Verify the deleted Notification is not shown");
            edoNotificationPage.checkNotificationIsNotDisplayed(notificationTitle);

            // Close the modal pop up if it appears
            homePage.closeModalPopUp();

            report.startStep("Logout of ED as Student");
            homePage.clickOnLogOut();
        } catch (Exception e) {

        } finally {

            if (!isDeleted) {

                report.startStep("Close browser and open again");
                webDriver.quitBrowser();
                sleep(2);
                webDriver.init();
                sleep(2);
                webDriver.maximize();
                //sleep(2);

                report.startStep("Login again with the same user");
                webDriver.openUrl(pageHelper.CILink);

                report.startStep("Login as Admin");
                tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();

                notificationsPage = new NotificationsPage(webDriver, testResultService);

                // Go to notification center
                notificationsPage.goToNotificationCenter();

                report.startStep("Delete the Notification");
                notificationsPage.deleteNotification(notificationName);
                sleep(1);

                report.startStep("Verify the Notification is Deleted");
                notificationsPage.verifyNotificationIsNotDisplayedInTheTable(notificationName);

                report.startStep("Logout of TMS as Admin");
                tmsHomePage.clickOnExitTMS();
                sleep(2);
            }
        }
    }

    @Test
    @TestCaseParams(testCaseID = {"74475", "74993"})
    public void testKeepMessages() throws Exception {

        try {

            // Test Data
            notificationName = "Qt test (name) - " + dbService.sig(5);
            String notificationDate = "This is a date - " + dbService.sig(5);
            String notificationTitle = "This is a title - " + dbService.sig(5);
            String notificationMessage = "This is a test message - " + dbService.sig(5);

            ArrayList<String> classesList = new ArrayList<String>();
            String className = configuration.getNotificationClassName();
            classesList.add(className);

            ArrayList<String> rolesList = new ArrayList<String>();
            rolesList.add("Teacher");
            rolesList.add("Student");

            // Initialize login page
            loginPage = new NewUXLoginPage(webDriver, testResultService);

            report.startStep("Login as Admin");
            tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
            homePage.waitUntilLoadingMessageIsOver();

            // Initialize notifications page
            notificationsPage = new NotificationsPage(webDriver, testResultService);

            // Go to notification center
            notificationsPage.goToNotificationCenter();

            // Initialize create new notification page
            createNewNotificationPage = new CreateNewNotificationPage(webDriver, testResultService);

            // Create new notification
            createNewNotificationPage.createNewNotification(notificationName, "admin", classesList, rolesList, notificationDate, notificationTitle, notificationMessage);
            sleep(3);

            report.startStep("Verify the created message is in hidden status");
            notificationsPage.checkNewNotificationAppearsHiddenInTable(notificationName);

            report.startStep("Publish the Notification");
            notificationsPage.publishNotification(notificationName);
            sleep(1);

            // Create Another Notification
            createNewNotificationPage.createNewNotification(notificationName + "(2)", "admin", classesList, rolesList, notificationDate + "(2)", notificationTitle + "(2)", notificationMessage + "(2)");
            sleep(3);

            report.startStep("Verify the created message is in hidden status");
            notificationsPage.checkNewNotificationAppearsHiddenInTable(notificationName + "(2)");

            report.startStep("Publish the Notification");
            notificationsPage.publishNotification(notificationName + "(2)");
            sleep(1);

            report.startStep("Logout of TMS as Admin");
            tmsHomePage.clickOnExitTMS();
            sleep(2);

            report.startStep("Login to ED as Student");
            getUserAndLoginGeneral(className, true, false);
            sleep(3);
            String currentStudentId = studentId;
            String institutionid = dbService.getInstitutionIdByUserId(currentStudentId);
            String currentUserName = dbService.getUserNameById(studentId, institutionid);

            // Initialize ed notification page
            edoNotificationPage = new EdoNotificationPage(webDriver, testResultService);

            // Validate the notification appears in log in page, press don't show again, close the notification
            edoNotificationPage.checkNotificationIsDisplayed(0, notificationDate + "(2)", notificationTitle + "(2)", notificationMessage + "(2)", null, true, false);

            // move to second notification
            edoNotificationPage.moveToNextNotification();

            // Validate the notification appears in log in page, press don't show again, close the notification
            edoNotificationPage.checkNotificationIsDisplayed(0, notificationDate, notificationTitle, notificationMessage, null, false, true);

            report.startStep("Close modal pop up if it appears");
            homePage.closeModalPopUp();

            report.startStep("Open Notification again From Menu");
            homePage.clickOnUserAvatar();
            homePage.checkTextAndclickOnNotificationsWithoutClosing();


            //report.startStep("Validate the notification we clicked to not show again is not displayed.");
            //edoNotificationPage.checkNotificationIsNotDisplayed(notificationDate+"(2)");

            // Validate the notification appears in log in page, press don't show again, close the notification
            edoNotificationPage.checkNotificationIsDisplayed(0, notificationDate + "(2)", notificationTitle + "(2)", notificationMessage + "(2)", null, false, false);

            // move to second notification
            edoNotificationPage.moveToNextNotification();

            report.startStep("Validate the notification is Displayed The Same Way");
            edoNotificationPage.checkNotificationIsDisplayed(1, notificationDate, notificationTitle, notificationMessage, null, false, true);

            report.startStep("Logout of ED as Student");
            homePage.clickOnLogOut();

            report.startStep("Login again as Student");
            //getUserAndLoginGeneral(className,true,false);
            loginPage.loginAsStudent(currentUserName, "12345");
            homePage.waitUntilLoadingMessageIsOver();

            report.startStep("Check only one notification is displayed");
            edoNotificationPage.checkNotificationIsDisplayed(0, notificationDate, notificationTitle, notificationMessage, null, false, true);

            report.startStep("Logout of ED as Student");
            homePage.clickOnLogOut();

            // log in as admin again and delete the notification
            report.startStep("Login as Admin");
            tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
            homePage.waitUntilLoadingMessageIsOver();

            // Go to notification center
            notificationsPage.goToNotificationCenter();

            report.startStep("Delete the Notification");
            notificationsPage.deleteNotification(notificationName + "(2)");
            sleep(1);

            report.startStep("Delete the Notification");
            notificationsPage.deleteNotification(notificationName);
            sleep(1);

            report.startStep("Verify the Notification is Deleted");
            isDeleted = notificationsPage.verifyNotificationIsNotDisplayedInTheTable(notificationName);

            report.startStep("Logout of TMS as Admin");
            tmsHomePage.clickOnExitTMS();
            sleep(2);

        } catch (Exception e) {

        } finally {
            if (!isDeleted) {

                report.startStep("Close browser and open again");
                webDriver.quitBrowser();
                sleep(2);
                webDriver.init();
                sleep(2);
                webDriver.maximize();
                //sleep(2);

                report.startStep("Login again with the same user");
                webDriver.openUrl(pageHelper.CILink);

                report.startStep("Login as Admin");
                tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();

                notificationsPage = new NotificationsPage(webDriver, testResultService);

                // Go to notification center
                notificationsPage.goToNotificationCenter();

                report.startStep("Delete the Notification");
                notificationsPage.deleteNotification(notificationName + "(2)");
                sleep(1);

                report.startStep("Delete the Notification");
                notificationsPage.deleteNotification(notificationName);
                sleep(1);

                report.startStep("Verify the Notification is Deleted");
                notificationsPage.verifyNotificationIsNotDisplayedInTheTable(notificationName);

                report.startStep("Logout of TMS as Admin");
                tmsHomePage.clickOnExitTMS();
                sleep(2);
            }
        }
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }
}
