package testSuites;


import Interfaces.TestCaseParams;
import Objects.Course;
import org.junit.Before;
import org.junit.Test;
import pageObjects.edo.NewUXLoginPage;
import pageObjects.edo.NewUxAssignmentsPage;
import pageObjects.edo.NewUxLearningArea2;
import pageObjects.tms.TmsHomePage;
import tests.edo.newux.BasicNewUxTest;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

//@RunWith(Parameterized.class)
public class RunMultipleTimes extends BasicNewUxTest {

    NewUXLoginPage loginPage;
    NewUxLearningArea2 learningArea2;
    NewUxAssignmentsPage assignPage;
    TmsHomePage tmsHomePage;

    List<Course> courses = null;
    List<String> writingIdForDelete = new ArrayList<String>();

    String writingId = null;
    String newWritingId = null;
    private static final String erater_task_instruction = "Type your answer in the text box and click submit.";

    //--igb 2018.08.12-------------
    private static final String textFile = "files/assayFiles/text2.txt";

    boolean bTestFailed = false;  // igb 2018.08.06


    @Before
    public void setup() throws Exception {
        //institutionName=institutionsName[19];
        super.setup();

    }

   /* @Parameterized.Parameters
    public static Object[][] data(){
        return new Object[3][];
    }

    public RunMultipleTimes(){

    }

    @Test
    public void runThreeTimes(){
        System.out.println("run");
    }*/

    @Test
    public void runSevTimes() throws Exception {
        for (int i = 0;i<3;i++){
            testMyAssigmentAlert();
        }
    }


    @Test
    @TestCaseParams(testCaseID = "40884", skippedBrowsers = {"firefox"})
    public void testMyAssigmentAlert()throws Exception {

        bTestFailed = false;  // igb 2018.08.06

        try {
            report.startStep("Init test data");
            int courseIndex = 1;
            int unitNumber = 7;
            int lessonNumber = 1;
            int stepNumber = 2;
            int taskNumber = 5;

            report.startStep("Using file: " + textFile);

            report.startStep("Create and login to ED as student");
            String classNameER = configuration.getProperty("classname.openSpeech");
            //String institutionId = institutionId; //configuration.getInstitutionId();
            studentId = pageHelper.createUSerUsingSP(institutionId, classNameER);
            String studentUserNameER = dbService.getUserNameById(studentId, institutionId);

            loginPage = new NewUXLoginPage(webDriver,testResultService);
            homePage = loginPage.loginAsStudent(studentUserNameER, "12345");
            homePage.closeAllNotifications();
            homePage.waitHomePageloadedFully();

            report.startStep("Navigate to E-Rater Task in B2-U1-L2");
            learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, courseCodes[courseIndex], unitNumber, lessonNumber);
            sleep(1);
            learningArea2.clickOnStep(stepNumber);
            learningArea2.clickOnTaskByNumber(taskNumber);
            sleep(1);

            report.startStep("Get header & left side text");
            //String header1 = learningArea2.getLessonNameFromHeader();
            //String resourceText1 = learningArea2.checkThatReadingTextIsDisplayed2();
            //sleep(2);

            report.startStep("Verify instruction text");
            report.report("Component cookie is: " + webDriver.getCookie("Component"));
            report.report("Course cookie is: " + webDriver.getCookie("Course"));
            report.report("Erater cookie is: " + webDriver.getCookie("ERater"));
            learningArea2.VerificationOfQuestionInstruction(erater_task_instruction);

            report.startStep("Submit text with unique label");
            String assayText = textService.getTextFromFile(textFile, Charset.defaultCharset());

            String uniquelabel = "";

            for(int i = 0; i < 8; i++) {
                uniquelabel += (char)(Math.random() * 26 + 97);
            }

            String textToSubmit = uniquelabel + " " + assayText;
            learningArea2.submitTextToERater(textToSubmit);

            report.startStep("Check Submit btn is disabled");
            learningArea2.checkThatSubmitEraterBtnIsDisabled();

            report.startStep("Check automation evaluation attempt indication");
            String attemptNum = learningArea2.getAttemptNumberOfAutomatedEvaluation();
            testResultService.assertEquals("1", attemptNum, "Current attempt count not correct");

            report.startStep("Get words count");
            String wordsCountLA = learningArea2.getERaterWordsCountOnSubmit();

            report.startStep("Checking the xml and json");
            writingId = eraterService.getWritingIdByUserIdAndTextStart(studentId, uniquelabel);
            writingIdForDelete.add(writingId);
            //sleep(5);

            report.startStep("Open and Close My Assignment Page and check assigment alert on");
            learningArea2.clickToOpenNavigationBar();
            sleep(2);

            for (int i = 0 ; i < 180 ; i++) {  // omz 14.10.2018
                if (dbService.getLastestEraterProcessedStatus(studentId).equals("1"))
                {
                    sleep(1);
                    break;
                }
                else
                    sleep(1);
            }
            //sleep(5);
            assignPage = learningArea2.openAssignmentsPage(false);
            sleep(5);
            assignPage.close();
            sleep(5);
            learningArea2.waitToAssignmentAlert();

            report.startStep("Open My Assignment Page and select Course with latest assignment");
            assignPage = learningArea2.openAssignmentsPage(false);
            assignPage.clickOnMyWritingsTab(true);
            assignPage.selectCourseInMyWritings(coursesNames[courseIndex], true);

            report.startStep("Click on See Feedback link");
            assignPage.clickOnSeeFeedbackTryAgainInMyWritings();
            sleep(1);
            assignPage.switchToFeedbackFrameInMyWritings();
            sleep(2);

            report.startStep("Validate Statistics Pop Up");
            assignPage.validateStatisticsPopUp(wordsCountLA, true);
            sleep(1);

            report.startStep("Click on More Details button and check assigment alert off");
            assignPage.clickOnFeedbackMoreDetails();
            sleep(1);
            learningArea2.clickOnOKButton();
            assignPage.clickOnFeedbackSubmitBtn();
            assignPage.close();
            learningArea2.withOutAlert(true);

            pageHelper.restartBrowserInNewURL(institutionName,true);
        }
        catch(Exception e)
        {
            bTestFailed = true;
            report.reportFailure(e.getMessage());
        }
    }

}
