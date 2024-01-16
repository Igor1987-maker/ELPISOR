package tests.edo.newux;

import Enums.ByTypes;
import Interfaces.TestCaseParams;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import pageObjects.edo.*;
import pageObjects.tms.TmsHomePage;
import testCategories.inProgressTests;
import testCategories.reg3;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Category(reg3.class)
public class NewUxSantilianaExtLoginTests extends BasicNewUxTest {

    NewUXLoginPage loginPage;
    NewUxLearningArea learningArea;
    NewUxLearningArea2 learningArea2;
    TmsHomePage tmsHomePage;
    public String modifiedContent;

    private static final String UserIsLoggedIn = "This username is currently logged into the system. If you feel you received this message in error, please contact your system administrator.";
    private static final String waitForLoginMessage = "Please wait while you are being logged into the system.\nThis may take a few minutes.";
    private static final String improperLoginAlert = "Your last session was closed improperly and some information might have been lost. Please close future learning sessions by using the Logout button.";
    private static final String userLoggedInMessage = "This user name is currently logged into the system.\nIf you feel you have received this message by mistake, please contact your system administrator.";

    private final String userNameLessThen5Chars = "Your name is required to be at least 5 characters";
    private final String userNameMoreThen15Chars = "Your name cannot be longer than 15 characters";
    private final String passCharsAndNumbersOnly = "The password only accepts English characters or numbers.";
    private final String missingUserName = "Please enter your user name.";
    private static final String ErrorUserName = "siteLogin__messageText";


    private final String usernameCharsAndNumbersOnly = "The user name only accepts English characters or numbers.";
    private final String passLessThen5chars = "Your password must have at least 5 characters.";
    private final String passMoreThen15chars = "";
    private final String extLoginIncorrectUserName = "Your user name or password is incorrect. Please try again.";
    public static final String retuenUrl = "https://ed.engdis.com/ed2016y#/login";
    //private String instName = "1643"; //beta
    private String instName = "10010"; //production
    private final String instPrefix = "UNOI-BR-";
    private String instId = "";
    private String token = "k9eS83sjk27ns2hZ98gDd8";
    private NewUxMyProfile myProfile;

    private String educateURL = "https://ednewb.engdis.com/santa/educatebr";
    private String richURL = "https://ednewb.engdis.com/santa/richmondbr";
    private String santaRedirectURL = "identity.santillanaconnect.com";
    private final String INSTITUTION_NAME = "EDU-BR-10001-EDUC";

    public NewUxSantilianaExtLoginTests() throws Exception {
    }


    @Before
    public void setup() throws Exception {
        super.setup();

        OpenBrowser();
/*		
		List<String[]> classes = pageHelper.getclassesByInstitutionName(instPrefix +instName);
		
		Random rand = new Random(); 
		int value = rand.nextInt(classes.size()); 

		String[] className = classes.get(value);

		instId = dbService.getInstituteIdByName(instPrefix+instName);
	
	// random users
		String[] userDetails = pageHelper.getUserNamePassworIddByInstitutionIdAndClassName
				(instId,className[0]);
				
		userName = userDetails[0];
		//String password = userDetails[1];
		studentId = userDetails[2];

	/*
		// production user
		userName = "18544444";
		//String password = "12345";
		studentId = "52358090000019";
	*/

    }

    @Test
    @TestCaseParams(testCaseID = {"89667"})
    public void openEducateLoginPage() throws Exception {
        report.startStep("Open Educate Login Page URL");

        webDriver.openUrl(pageHelper.santianaURL);

        try {
            this.checkSantaRedirectURL();
        } catch (Exception e) {
            testResultService.addFailTest(e.toString(), false, true);
        }

    }

    @Test
    @TestCaseParams(testCaseID = {"89666", "89669"})
    public void educLoginAsStudent() throws Exception {

        report.startStep("Open Educate Login Page and check redirect URL");
        webDriver.openUrl(pageHelper.santianaURL);
        try {
            checkSantaRedirectURL();
        } catch (Exception e) {
            testResultService.addFailTest(e.toString(), false, true);
        }

        report.startStep("Get user for Login");
        boolean isError = isError();
        testResultService.assertEquals(false, isError, "There is server Error");
        String userName = configuration.getGlobalProperties("santa.user");
        String password = configuration.getGlobalProperties("santa.pass");

        report.startStep("Login");
        NewUXLoginPage loginPage = new NewUXLoginPage(webDriver, testResultService);
        homePage = loginPage.SantilianaLoginPage(userName, password);


        NewUxHomePage santaHomePage = new NewUxHomePage(webDriver, testResultService);
        santaHomePage.waitHomePageloadedFully();

        report.startStep("Open my profile page");
        NewUxMyProfile santaMyProfile = santaHomePage.clickOnMyProfile();
        santaHomePage.switchToMyProfile();

        report.startStep("Check that student has appropriate first name");
        testResultService.assertEquals("Aluno TesteH", santaMyProfile.getFirstName());

        report.startStep("Check that student has appropriate last name");
        testResultService.assertEquals("EDUC", santaMyProfile.getLastName());

        report.startStep("Check that student has appropriate username");
        testResultService.assertEquals("19868946", santaMyProfile.getUserName(), "Username of the student is wrong");

        report.startStep("Close my profile popup");
        santaMyProfile.close();
        sleep(3);
        webDriver.switchToMainWindow();

        report.startStep("Logout from ED");
        homePage.clickOnLogOut();
        sleep(3);
        homePage.waitForPageToLoad();

        report.startStep("Confirm Logout Santiliana");
        loginPage.confirmLogoutSantiliana();
        loginPage.waitForPageToLoad();
    }

    private void checkSantaRedirectURL() throws Exception {


        report.startStep("Check that student has appropriate URL");
        //testResultService.assertTrue("Redirect URL has not been activated", webDriver.waitForSpecificCurrentUrl("", expectedUrl).contains(expectedUrl));
        testResultService.assertEquals(true, webDriver.waitForSpecificCurrentUrl("", santaRedirectURL).contains(santaRedirectURL),
                "Redirect URL has not been activated");


    }


    @Test
    @Category(inProgressTests.class)
    @TestCaseParams(testCaseID = {"50505"})
    public void testSantaStudentExtLoginSanity() throws Exception {

        try {
            report.startStep("Open santa URL");
            webDriver.openUrl(educateURL);
            sleep(1);
            report.startStep("Get user for Login");
            boolean isError = isError();
            testResultService.assertEquals(false, isError, "There is server Error");
            String userName = configuration.getGlobalProperties("santa.user");
            String password = configuration.getGlobalProperties("santa.pass");

            report.startStep("Login");
            NewUXLoginPage loginPage = new NewUXLoginPage(webDriver, testResultService);
            homePage = loginPage.SantilianaLoginPage(userName, password);
            homePage.waitHomePageloadedFully();

            report.startStep("Wait until page loaded");
            NewUxHomePage santaHomePage = new NewUxHomePage(webDriver, testResultService);
            santaHomePage.waitHomePageloadedFully();
            homePage.getNavigationBarStatus();

            report.startStep("Open My profile");
            homePage.clickOnMyProfile();
            homePage.switchToMyProfile();
            NewUxMyProfile myProfile = new NewUxMyProfile(webDriver, testResultService);

            report.startStep("Verify user details on Home Page");
            testResultService.assertEquals("Aluno TesteH", myProfile.getFirstName(), "FirstName does not match");
            testResultService.assertEquals("EDUC", myProfile.getLastName(), "LastName does not match");
            myProfile.close(true);
            sleep(2);
            webDriver.switchToMainWindow();


//        report.startStep("Open Learning Area and verify it laneded");
//        webDriver.switchToMainWindow();
//        homePage.waitHomePageloadedFully();
//        learningArea2 = homePage.clickOnContinueButton2();
//        learningArea2.waitForPageToLoad();
//        learningArea2.clickOnNextButton();
//
//        homePage.clickOnHomeButton();
//        homePage.waitHomePageloadedFully();

            report.startStep("Logout from ED");
            homePage.clickOnLogOut();
            sleep(3);
            homePage.waitForPageToLoad();

            report.startStep("Confirm Logout Santiliana");
            loginPage.confirmLogoutSantiliana();
            loginPage.waitForPageToLoad();
        } catch (Exception e) {
            testResultService.addFailTest(e.toString(), false, true);

        }

    }


    @Test
    @Category(inProgressTests.class)
    @TestCaseParams(testCaseID = {"50505"})
    public void testSantaExtLoginNotExistsUserName() throws Exception {

        report.startStep("Open Educate Login Page and check redirect URL");
        webDriver.openUrl(pageHelper.santianaURL);
        try {
            checkSantaRedirectURL();
        } catch (Exception e) {
            testResultService.addFailTest(e.toString(), false, true);
        }

        report.startStep("Get user for Login");
        boolean isError = isError();
        testResultService.assertEquals(false, isError, "There is server Error");
        String password = configuration.getGlobalProperties("santa.pass");

        testResultService.assertEquals(false, isError, "There is server Error");

    }

    @Test
    @Category(inProgressTests.class)
    @TestCaseParams(testCaseID = {"50505"})
    public void testSantaExtLoginNotExistsInstitution() throws Exception {

        instName = dbService.sig(3);

        report.startStep("Open Educate Login Page and check redirect URL");
        webDriver.openUrl(pageHelper.santianaURL);

        sleep(1);

        boolean isError = isError();

        testResultService.assertEquals(false, isError, "There is server Error");
    }


    @Test
    @Category(inProgressTests.class)
    @TestCaseParams(testCaseID = {"50505"})
    public void testSantaExtLogin() throws Exception {

        report.startStep("Open Educate Login Page and check redirect URL");
        webDriver.openUrl(pageHelper.santianaURL);

        sleep(1);

        boolean isError = isError();

        testResultService.assertEquals(false, isError, "There is server Error");
    }


    @Test
    @Category(inProgressTests.class)
    @TestCaseParams(testCaseID = {""})
    public void testSantaInt2023LoginLogout() throws Exception {

        try {
            report.startStep("Open santa URL");
            webDriver.openUrl(pageHelper.santianaURL);
            sleep(1);
            report.startStep("Get user for Login");
            boolean isError = isError();
            testResultService.assertEquals(false, isError, "There is server Error");
            String userName = configuration.getGlobalProperties("santa.user");
            String password = configuration.getGlobalProperties("santa.pass");

            report.startStep("Login");
            NewUXLoginPage loginPage = new NewUXLoginPage(webDriver, testResultService);
            homePage = loginPage.SantilianaLoginPage(userName, password);
            homePage.waitHomePageloadedFully();

            report.startStep("Logout from ED");
            homePage.clickOnLogOut();
            sleep(3);
            homePage.waitForPageToLoad();

            report.startStep("Confirm Logout Santiliana");
            loginPage.confirmLogoutSantiliana();
            loginPage.waitForPageToLoad();

        } catch (Exception e) {
            testResultService.addFailTest(e.toString(), false, true);

        }

    }

    @Test
    @Category(inProgressTests.class)
    @TestCaseParams(testCaseID = {})
    public void loginValidationFields() throws Exception {

        report.startStep("Initial phase");
        loginPage = new NewUXLoginPage(webDriver, testResultService);
        webDriver.openUrl(pageHelper.santianaURL);

        report.startStep("Test Blank user name");

        loginPage.SantilianaLoginPage("aaaa�zzz", "moderna");
        loginPage.clearUserNameSantiliana();
        loginPage.SantilianaLoginPage("alunoeduc.teste8", "123$4444");
        loginPage.clearUserNameSantiliana();

        report.startStep("Get user for Login");
        boolean isError = isError();
        testResultService.assertEquals(false, isError, "There is server Error");
        String userName = configuration.getGlobalProperties("santa.user");
        String password = configuration.getGlobalProperties("santa.pass");

        report.startStep("Login");
        NewUXLoginPage loginPage = new NewUXLoginPage(webDriver, testResultService);
        homePage = loginPage.SantilianaLoginPage(userName, password);
        homePage.waitHomePageloadedFully();

        report.startStep("Logout from ED");
        homePage.clickOnLogOut();
        sleep(3);
        homePage.waitForPageToLoad();

        report.startStep("Confirm Logout Santiliana");
        loginPage.confirmLogoutSantiliana();
        loginPage.waitForPageToLoad();

    }

    private boolean isError() {

        String actualtext = null;
        try {
            actualtext = webDriver.waitForElement("/html/body", ByTypes.xpath, false, 1).getText().toLowerCase();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return actualtext.toLowerCase().contains("server error")
                || actualtext.toLowerCase().contains("redirected you too many times");

    }


    @Test
    @Category(inProgressTests.class)
    @TestCaseParams(testCaseID = {"50505"})
    public void testSantaExtLoginInCorrectToken() throws Exception {

        String token = dbService.sig(8);

        report.startStep("Initial phase");
        loginPage = new NewUXLoginPage(webDriver, testResultService);
        webDriver.openUrl(pageHelper.santianaURL+token+"");

        sleep(1);
        boolean isError = isError();
        testResultService.assertEquals(false, isError, "There is erver Error");
    }

    protected NewUxHomePage createUserAndLoginForOptIn(String className,
                                                       String institutionid) throws Exception {

        studentId = pageHelper.createUSerUsingOptIn(institutionid, className);

        //dbService.setUserOptIn(studentId, true);

        NewUXLoginPage loginPage = new NewUXLoginPage(webDriver, testResultService);

        homePage = loginPage.loginAsStudent(
                dbService.getUserNameById(studentId, institutionid), "12345");

//		dbService.setUserOptIn(studentId, true);

        return homePage;
    }


    @Test
    @Category(inProgressTests.class)
    @TestCaseParams(testCaseID = {"18290"})
    public void testSantaImprperStudentLogout() throws Exception {

        report.startStep("Open santa URL");
        webDriver.openUrl(pageHelper.santianaURL);
        sleep(1);
        report.startStep("Get user for Login");
        boolean isError = isError();
        testResultService.assertEquals(false, isError, "There is server Error");
        String userName = configuration.getGlobalProperties("santa.user");
        String password = configuration.getGlobalProperties("santa.pass");

        report.startStep("Login");
        NewUXLoginPage loginPage = new NewUXLoginPage(webDriver, testResultService);
        homePage = loginPage.SantilianaLoginPage(userName, password);
        homePage.waitHomePageloadedFully();

        report.startStep("Close the browser");
        webDriver.closeBrowser();

        report.startStep("Open the browser and login with the same credentials");
        OpenBrowser();
        webDriver.openUrl(pageHelper.santianaURL);
        homePage = loginPage.SantilianaLoginPage(userName, password);

        report.startStep("Check login alert");
        sleep(4);
        loginPage.clickLoginOnThisDevise();
        homePage.waitHomePageloadedFully();
        sleep(1);

        report.startStep("Logout from ED");
        homePage.clickOnLogOut();
        sleep(3);
        homePage.waitForPageToLoad();

        report.startStep("Confirm Logout Santiliana");
        loginPage.confirmLogoutSantiliana();
        loginPage.waitForPageToLoad();

    }

    @Test
    @Category(inProgressTests.class)
    @TestCaseParams(testCaseID = {"18290"})
    public void testSantaExtLoginTeacherSanity() throws Exception {

        report.startStep("Get teacher credentials and ED Url");
        String url = dbService.getSantilianaUrlForTeacherOrAdminLogin(INSTITUTION_NAME);
        String teacherUsername = dbService.getTeacherSantilianaUserName(INSTITUTION_NAME);
        String teacherPassword = dbService.getTeacherSantilianaPassword(INSTITUTION_NAME);
        String edUrl= "https://"+url;
        webDriver.openUrl(edUrl);

        report.startStep("Login");
        NewUXLoginPage loginPage = new NewUXLoginPage(webDriver, testResultService);
        homePage = loginPage.loginAsStudent(teacherUsername, teacherPassword);
        sleep(1);
        homePage.waitUntilLoadingMessageIsOver();
        TmsHomePage tmsHomePage = new TmsHomePage(webDriver, testResultService);
        tmsHomePage.waitForPageToLoad();

        report.startStep("Open Curriculum");
        tmsHomePage.switchToMainFrame();
        tmsHomePage.clickOnCurriculum();
        sleep(2);

        report.startStep("Click On My Profile");
        myProfile = tmsHomePage.clickOnMyProfile();
        webDriver.switchToPopup();

        report.startStep("Verify The UserName");
        String act_UserName = myProfile.getUserName();
        testResultService.assertEquals(teacherUsername, act_UserName, "The expected UserName doesn't match to actual UserName");

    }


    private void closeBrowserAndOpenAgain() throws Exception {

        webDriver.quitBrowser();
        sleep(2);
        webDriver.init();
        sleep(2);
        webDriver.maximize();
        sleep(2);

    }

    private void verifyUserDetailsInToeflTMS(String firstName, String lastName, boolean executeLogout) throws Exception {

        String expectedUserDetails = "Welcome, " + firstName + " " + lastName.trim();
        String actualUserDetails = webDriver.waitForElement("cxtWellcome", ByTypes.id).getText();
        testResultService.assertEquals(expectedUserDetails, actualUserDetails, "User Details are not correct");

        if (executeLogout) {

            report.startStep("Logout");
            webDriver.waitForElement("Logout", ByTypes.linkText).click();
            webDriver.closeAlertByAccept(webDriver.getTimeout());
        }
    }


    private void verifyExtLoginParametersAfterLogin(boolean redirectOnLogout) throws Exception {

        report.startStep("Verify First Name is correct on Home Page");
        NewUxHomePage homePage = new NewUxHomePage(webDriver, testResultService);

        String[] userDetails = dbService.getuserPersonalDetails(studentId);

        String firstName = userDetails[0];
        String lastName = userDetails[1];

        testResultService.assertEquals("Hello " + firstName, homePage.getUserDataText(), "Verify First Name in Header of Home Page");

        report.startStep("Open My profile");
        homePage.clickOnMyProfile();
        homePage.switchToMyProfile();
        sleep(1);

        report.startStep("Check user parameters");
        NewUxMyProfile myProfile = new NewUxMyProfile(webDriver, testResultService);


        testResultService.assertEquals(userName, myProfile.getUserName(), "Username does not match");
        testResultService.assertEquals(firstName, myProfile.getFirstName(), "FirstName does not match");
        testResultService.assertEquals(lastName, myProfile.getLastName(), "LastName does not match");
        myProfile.close(true);
        sleep(1);

        if (redirectOnLogout) {
            report.startStep("Logout");
            homePage.clickOnLogOut();
            sleep(3);

            report.startStep("Verify redirect to referrer URL from parameters");
            String currentUrl = "";
            currentUrl = webDriver.waitForSpecificCurrentUrl(currentUrl, "close.html");
            //testResultService.assertEquals(homePage.CorpUrl, currentUrl,"Referrer site not displayed");
            testResultService.assertEquals(true, currentUrl.contains("close.html"), "Referrer site not displayed");
        }
    }

    public String readLoginFileContentSanta(String filePath) {
        String content = "";
        try {

            Path path = Paths.get(filePath);

            content = new String(Files.readAllBytes(path));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }


    @Test
    @Category(inProgressTests.class)
    public void createSchoolClassStudAndLogin() throws Exception {

        report.startStep("Generate data for Brazil Richmond");
        String baseUrl = pageHelper.CILink.split(".com")[0] + ".com/";
        NewUxSantillanaEntrance newUxSantillanaEntrance = new NewUxSantillanaEntrance(webDriver, testResultService);

        report.startStep("Generate unique id for User");
        String newUserId = newUxSantillanaEntrance.generateUniqueId(36);
        String newSchoolId = newUxSantillanaEntrance.generateUniqueId(36);
        String newClassId = newUxSantillanaEntrance.generateUniqueId(36);
        String[] parts = newUserId.split("-");
        String userName = parts[parts.length - 1];

        report.startStep("Read Santiliana Post File and modify the data");
        String filePath = "files/htmlFiles/Santa_POST_HTML_FORM.html";
        File file = new File(filePath);
        String fileContent = textService.readFileContent(file);
        modifiedContent = fileContent.replace("%username%", userName);
        modifiedContent = modifiedContent.replace("%firstname%", userName + "Fn");
        modifiedContent = modifiedContent.replace("%lastname%", userName + "LN");
        modifiedContent = modifiedContent.replace("%newUserId%", newUserId)
                .replace("%newSchoolId%", newSchoolId)
                .replace("%newClassId%", newClassId);
        modifiedContent = modifiedContent.replace("%accessUrl%", baseUrl + "santillanaRegisterUserAndLogin");
        modifiedContent = modifiedContent.replace("%countryCode%", "BR");
        modifiedContent = modifiedContent.replace("%packageName%", "BRP4");
        modifiedContent = modifiedContent.replace("%businessDivision%", "Richmond");

        report.startStep("Save file to the system");
        List<String> wList = new ArrayList<>();
        wList.add(modifiedContent);
        String testName = "Santillianna" + dbService.sig(4) + ".html";
        File fileHTML = textService.writeListToSmbFile(pageHelper.buildPathForExternalPages + "Languages", testName, wList, false);
        webDriver.openUrl(fileHTML.toString());

        report.startStep("Check the student logged in");
        newUxSantillanaEntrance.clickSendPostRequest();
        pageHelper.skipOptin();
        homePage.closeAllNotifications();
        pageHelper.skipOnBoardingHP();
        homePage.waitHomePageloadedFully();
        testResultService.assertEquals("Hello " + userName + "Fn", newUxSantillanaEntrance.getUserNameText());

        report.startStep("Check my Profile");
        homePage.clickOnMyProfile();
        sleep(3);
        homePage.switchToMyProfile();
        NewUxMyProfile myProfile = new NewUxMyProfile(webDriver, testResultService);
        testResultService.assertEquals(userName + "LN", myProfile.getLastName(), "Last Name in My Profile is Incorrect.");
        testResultService.assertEquals(userName + "Fn", myProfile.getFirstName(), "First Name in My Profile is Incorrect.");

        report.startStep("Check user created on DB");
        String userNameDB = dbService.getStudentSantilianaUserName();
        testResultService.assertEquals(userName, userNameDB, "User Names on test and DB are different");

    }

    @Test
    @Category(inProgressTests.class)
    public void testUserCantLoginWithInvalidUserName() throws Exception {

        String baseUrl = pageHelper.CILink.split(".com")[0] + ".com/";
        NewUxSantillanaEntrance newUxSantillanaEntrance = new NewUxSantillanaEntrance(webDriver, testResultService);

        report.startStep("Generate unique id for User");
        String newUserId = newUxSantillanaEntrance.generateUniqueId(36);
        String newSchoolId = newUxSantillanaEntrance.generateUniqueId(36);
        String newClassId = newUxSantillanaEntrance.generateUniqueId(36);
        String[] parts = newUserId.split("-");
        String invalidUserName = "st"+ parts[parts.length - 1];

        report.startStep("Read Santiliana Post File and modify the data");
        String filePath = "files/htmlFiles/Santa_POST_HTML_FORM.html";
        File file = new File(filePath);
        String fileContent = textService.readFileContent(file);
        modifiedContent = fileContent.replace("%username%", invalidUserName);
        modifiedContent = modifiedContent.replace("%firstname%", invalidUserName + "Fn");
        modifiedContent = modifiedContent.replace("%lastname%", invalidUserName + "LN");
        modifiedContent = modifiedContent.replace("%newUserId%", newUserId)
                .replace("%newSchoolId%", newSchoolId)
                .replace("%newClassId%", newClassId);
        modifiedContent = modifiedContent.replace("%accessUrl%", baseUrl + "santillanaRegisterUserAndLogin");
        modifiedContent = modifiedContent.replace("%countryCode%", "MX");
        modifiedContent = modifiedContent.replace("%packageName%", "MUJ53");
        modifiedContent = modifiedContent.replace("%businessDivision%", "UNOi");

        report.startStep("Save file to the system");
        List<String> wList = new ArrayList<>();
        wList.add(modifiedContent);
        String testName = "Santillianna" + dbService.sig(4) + ".html";
        File fileHTML = textService.writeListToSmbFile(pageHelper.buildPathForExternalPages + "Languages", testName, wList, false);
        webDriver.openUrl(fileHTML.toString());

        report.startStep("Check error message");
        newUxSantillanaEntrance.clickSendPostRequest();
        testResultService.assertEquals("Error=Invalid username", newUxSantillanaEntrance.getInvalidUserNameText());


    }


    @Test
    @Category(inProgressTests.class)
    public void testSantillianaTwiceUserLoginGuatemala() throws Exception {

        report.startStep("Generate data for Guatemala RichComp");
        String baseUrl = pageHelper.CILink.split(".com")[0] + ".com/";
        NewUxSantillanaEntrance newUxSantillanaEntrance = new NewUxSantillanaEntrance(webDriver, testResultService);

        report.startStep("Generate unique id for User");
        String newUserId = newUxSantillanaEntrance.generateUniqueId(36);
        String newSchoolId = newUxSantillanaEntrance.generateUniqueId(36);
        String newClassId = newUxSantillanaEntrance.generateUniqueId(36);
        String[] parts = newUserId.split("-");
        String userName = parts[parts.length - 1];

        report.startStep("Read Santiliana Post File and modify the data");
        String filePath = "files/htmlFiles/Santa_POST_HTML_FORM.html";
        File file = new File(filePath);
        String fileContent = textService.readFileContent(file);
        modifiedContent = fileContent.replace("%username%", userName);
        modifiedContent = modifiedContent.replace("%firstname%", userName + "Fn");
        modifiedContent = modifiedContent.replace("%lastname%", userName + "LN");
        modifiedContent = modifiedContent.replace("%newUserId%", newUserId)
                .replace("%newSchoolId%", newSchoolId)
                .replace("%newClassId%", newClassId);
        modifiedContent = modifiedContent.replace("%accessUrl%", baseUrl + "santillanaRegisterUserAndLogin");
        modifiedContent = modifiedContent.replace("%countryCode%", "GT");
        modifiedContent = modifiedContent.replace("%packageName%", "GRJ41");
        modifiedContent = modifiedContent.replace("%businessDivision%", "Rich/Comp");

        report.startStep("Save file to the system");
        List<String> wList = new ArrayList<>();
        wList.add(modifiedContent);
        String testName = "Santillianna" + dbService.sig(4) + ".html";
        File fileHTML = textService.writeListToSmbFile(pageHelper.buildPathForExternalPages + "Languages", testName, wList, false);
        webDriver.openUrl(fileHTML.toString());

        report.startStep("Check the student logged in");
        newUxSantillanaEntrance.clickSendPostRequest();
        pageHelper.skipOptin();
        homePage.closeAllNotifications();
        pageHelper.skipOnBoardingHP();
        homePage.waitHomePageloadedFully();
        testResultService.assertEquals("Hello " + userName + "Fn", newUxSantillanaEntrance.getUserNameText());

        report.startStep("Check my Profile");
        homePage.clickOnMyProfile();
        sleep(3);
        homePage.switchToMyProfile();
        NewUxMyProfile myProfile = new NewUxMyProfile(webDriver, testResultService);
        testResultService.assertEquals(userName + "LN", myProfile.getLastName(), "Last Name in My Profile is Incorrect.");
        testResultService.assertEquals(userName + "Fn", myProfile.getFirstName(), "First Name in My Profile is Incorrect.");

        report.startStep("Check user created on DB");
        String userNameDB = dbService.getStudentSantilianaUserName();
        testResultService.assertEquals(userName, userNameDB, "User Names on test and DB are different");

        report.startStep("Quit Browser and open url");
        webDriver.quitBrowser();
        webDriver.init();
        webDriver.maximize();
        webDriver.openUrl(fileHTML.toString());


        report.startStep("Send Post Request");
        newUxSantillanaEntrance.clickSendPostRequest();

        report.startStep("Check login alert");
        NewUXLoginPage loginPage = new NewUXLoginPage(webDriver, testResultService);
        sleep(10);
        loginPage.clickLoginOnThisDevise();
        homePage.waitHomePageloadedFully();
        sleep(1);
        report.startStep("Check the user logged in");
        testResultService.assertEquals("Hello " + userName + "Fn", newUxSantillanaEntrance.getUserNameText());

    }

    @Test
    @Category(inProgressTests.class)
    public void createSchoolClassStudAndLoginMexico() throws Exception {

        report.startStep("Generate data for Mexico");

        String baseUrl = pageHelper.CILink.split(".com")[0] + ".com/";
        NewUxSantillanaEntrance newUxSantillanaEntrance = new NewUxSantillanaEntrance(webDriver, testResultService);

        report.startStep("Generate unique id for User");
        String newUserId = newUxSantillanaEntrance.generateUniqueId(36);
        String newSchoolId = newUxSantillanaEntrance.generateUniqueId(36);
        String newClassId = newUxSantillanaEntrance.generateUniqueId(36);
        String[] parts = newUserId.split("-");
        String userName = parts[parts.length - 1];

        report.startStep("Read Santiliana Post File and modify the data");
        String filePath = "files/htmlFiles/Santa_POST_HTML_FORM.html";
        File file = new File(filePath);
        String fileContent = textService.readFileContent(file);
        modifiedContent = fileContent.replace("%username%", userName);
        modifiedContent = modifiedContent.replace("%firstname%", userName + "Fn");
        modifiedContent = modifiedContent.replace("%lastname%", userName + "LN");
        modifiedContent = modifiedContent.replace("%newUserId%", newUserId)
                .replace("%newSchoolId%", newSchoolId)
                .replace("%newClassId%", newClassId);
        modifiedContent = modifiedContent.replace("%accessUrl%", baseUrl + "santillanaRegisterUserAndLogin");
        modifiedContent = modifiedContent.replace("%countryCode%", "MX");
        modifiedContent = modifiedContent.replace("%packageName%", "MUJ53");
        modifiedContent = modifiedContent.replace("%businessDivision%", "UNOi");

        report.startStep("Save file to the system");
        List<String> wList = new ArrayList<>();
        wList.add(modifiedContent);
        String testName = "Santillianna" + dbService.sig(4) + ".html";
        File fileHTML = textService.writeListToSmbFile(pageHelper.buildPathForExternalPages + "Languages", testName, wList, false);
        webDriver.openUrl(fileHTML.toString());

        report.startStep("Check the student logged in");
        newUxSantillanaEntrance.clickSendPostRequest();
        pageHelper.skipOptin();
        homePage.closeAllNotifications();
        pageHelper.skipOnBoardingHP();
        homePage.waitHomePageloadedFully();
        testResultService.assertEquals("Hello " + userName + "Fn", newUxSantillanaEntrance.getUserNameText());

        report.startStep("Check my Profile");
        homePage.clickOnMyProfile();
        sleep(3);
        homePage.switchToMyProfile();
        NewUxMyProfile myProfile = new NewUxMyProfile(webDriver, testResultService);
        testResultService.assertEquals(userName + "LN", myProfile.getLastName(), "Last Name in My Profile is Incorrect.");
        testResultService.assertEquals(userName + "Fn", myProfile.getFirstName(), "First Name in My Profile is Incorrect.");

        report.startStep("Check user created on DB");
        String userNameDB = dbService.getStudentSantilianaUserName();
        testResultService.assertEquals(userName, userNameDB, "User Names on test and DB are different");

    }

    @Test
    @Category(inProgressTests.class)
    public void createSchoolClassStudAndLoginPA() throws Exception {

        report.startStep("Generate data for Panama");
        String baseUrl = pageHelper.CILink.split(".com")[0] + ".com/";
        NewUxSantillanaEntrance newUxSantillanaEntrance = new NewUxSantillanaEntrance(webDriver, testResultService);

        report.startStep("Generate unique id for User");
        String newUserId = newUxSantillanaEntrance.generateUniqueId(36);
        String newSchoolId = newUxSantillanaEntrance.generateUniqueId(36);
        String newClassId = newUxSantillanaEntrance.generateUniqueId(36);
        String[] parts = newUserId.split("-");
        String userName = parts[parts.length - 1];

        report.startStep("Read Santiliana Post File and modify the data");
        String filePath = "files/htmlFiles/Santa_POST_HTML_FORM.html";
        File file = new File(filePath);
        String fileContent = textService.readFileContent(file);
        modifiedContent = fileContent.replace("%username%", userName);
        modifiedContent = modifiedContent.replace("%firstname%", userName + "Fn");
        modifiedContent = modifiedContent.replace("%lastname%", userName + "LN");
        modifiedContent = modifiedContent.replace("%newUserId%", newUserId)
                .replace("%newSchoolId%", newSchoolId)
                .replace("%newClassId%", newClassId);
        modifiedContent = modifiedContent.replace("%accessUrl%", baseUrl + "santillanaRegisterUserAndLogin");
        modifiedContent = modifiedContent.replace("%countryCode%", "PA");
        modifiedContent = modifiedContent.replace("%packageName%", "PCJ55");
        modifiedContent = modifiedContent.replace("%businessDivision%", "Compartir");

        report.startStep("Save file to the system");
        List<String> wList = new ArrayList<>();
        wList.add(modifiedContent);
        String testName = "Santillianna" + dbService.sig(4) + ".html";
        File fileHTML = textService.writeListToSmbFile(pageHelper.buildPathForExternalPages + "Languages", testName, wList, false);
        webDriver.openUrl(fileHTML.toString());

        report.startStep("Check the student logged in");
        newUxSantillanaEntrance.clickSendPostRequest();
        pageHelper.skipOptin();
        homePage.closeAllNotifications();
        pageHelper.skipOnBoardingHP();
        homePage.waitHomePageloadedFully();
        testResultService.assertEquals("Hello " + userName + "Fn", newUxSantillanaEntrance.getUserNameText());

        report.startStep("Check my Profile");
        homePage.clickOnMyProfile();
        sleep(3);
        homePage.switchToMyProfile();
        NewUxMyProfile myProfile = new NewUxMyProfile(webDriver, testResultService);
        testResultService.assertEquals(userName + "LN", myProfile.getLastName(), "Last Name in My Profile is Incorrect.");
        testResultService.assertEquals(userName + "Fn", myProfile.getFirstName(), "First Name in My Profile is Incorrect.");

        report.startStep("Check user created on DB");
        String userNameDB = dbService.getStudentSantilianaUserName();
        testResultService.assertEquals(userName, userNameDB, "User Names on test and DB are different");

    }

    @Test
    @Category(inProgressTests.class)
    public void createSchoolClassStudAndLoginBR() throws Exception {

        report.startStep("Generate data for Brazil Educate");
        String baseUrl = pageHelper.CILink.split(".com")[0] + ".com/";
        NewUxSantillanaEntrance newUxSantillanaEntrance = new NewUxSantillanaEntrance(webDriver, testResultService);

        report.startStep("Generate unique id for User");
        String newUserId = newUxSantillanaEntrance.generateUniqueId(36);
        String newSchoolId = newUxSantillanaEntrance.generateUniqueId(36);
        String newClassId = newUxSantillanaEntrance.generateUniqueId(36);
        String[] parts = newUserId.split("-");
        String userName = parts[parts.length - 1];

        report.startStep("Read Santiliana Post File and modify the data");
        String filePath = "files/htmlFiles/Santa_POST_HTML_FORM.html";
        File file = new File(filePath);
        String fileContent = textService.readFileContent(file);
        modifiedContent = fileContent.replace("%username%", userName);
        modifiedContent = modifiedContent.replace("%firstname%", userName + "Fn");
        modifiedContent = modifiedContent.replace("%lastname%", userName + "LN");
        modifiedContent = modifiedContent.replace("%newUserId%", newUserId)
                .replace("%newSchoolId%", newSchoolId)
                .replace("%newClassId%", newClassId);
        modifiedContent = modifiedContent.replace("%accessUrl%", baseUrl + "santillanaRegisterUserAndLogin");
        modifiedContent = modifiedContent.replace("%countryCode%", "BR");
        modifiedContent = modifiedContent.replace("%packageName%", "BEI9");
        modifiedContent = modifiedContent.replace("%businessDivision%", "Educate");

        report.startStep("Save file to the system");
        List<String> wList = new ArrayList<>();
        wList.add(modifiedContent);
        String testName = "Santillianna" + dbService.sig(4) + ".html";
        File fileHTML = textService.writeListToSmbFile(pageHelper.buildPathForExternalPages + "Languages", testName, wList, false);
        webDriver.openUrl(fileHTML.toString());

        report.startStep("Check the student logged in");
        newUxSantillanaEntrance.clickSendPostRequest();
        pageHelper.skipOptin();
        homePage.closeAllNotifications();
        pageHelper.skipOnBoardingHP();
        homePage.waitHomePageloadedFully();
        testResultService.assertEquals("Hello " + userName + "Fn", newUxSantillanaEntrance.getUserNameText());

        report.startStep("Check my Profile");
        homePage.clickOnMyProfile();
        sleep(3);
        homePage.switchToMyProfile();
        NewUxMyProfile myProfile = new NewUxMyProfile(webDriver, testResultService);
        testResultService.assertEquals(userName + "LN", myProfile.getLastName(), "Last Name in My Profile is Incorrect.");
        testResultService.assertEquals(userName + "Fn", myProfile.getFirstName(), "First Name in My Profile is Incorrect.");

        report.startStep("Check user created on DB");
        String userNameDB = dbService.getStudentSantilianaUserName();
        testResultService.assertEquals(userName, userNameDB, "User Names on test and DB are different");

    }






    public void OpenBrowser() throws Exception {
        report.startStep("Open browser");
        webDriver.quitBrowser();
        //webDriver.openIncognitoChromeWindow();
        sleep(1);
        webDriver.init();
        sleep(2);
        webDriver.maximize();
        sleep(2);
    }


    @After
    public void tearDown() throws Exception {

        super.tearDown();

    }


}
