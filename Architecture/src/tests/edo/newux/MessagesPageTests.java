package tests.edo.newux;

import Interfaces.TestCaseParams;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import pageObjects.edo.NewUXLoginPage;
import pageObjects.edo.NewUxLearningArea;
import pageObjects.edo.NewUxMessagesPage;
import pageObjects.tms.TeacherReadMessagePage;
import pageObjects.tms.TeacherWriteMessagePage;
import pageObjects.tms.TmsHomePage;
import testCategories.edoNewUX.ReleaseTests;

public class MessagesPageTests extends BasicNewUxTest {

	NewUXLoginPage loginPage;
	NewUxLearningArea learningArea;
	NewUxMessagesPage inboxPage;
	TmsHomePage tmsHomePage;
	TeacherReadMessagePage tRead;
	TeacherWriteMessagePage tWrite;
	
	@Before
	public void setup() throws Exception {
		institutionName = institutionsName[0];
		super.setup();
		report.startStep("Get user and login");
		
	/*	
		institutionName = institutionsName[20]; // qa31
		pageHelper.initializeData();
		pageHelper.restartBrowserInNewURL(institutionName, true);
	*/	
		
		getUserAndLoginNewUXClass();
		//homePage.closeAllNotifications();
	}

	@Category(ReleaseTests.class)
	@Test
	//@Category(AngularLearningArea.class) //Test Isn't effected by LA, removed old LA run 20.1.2019
	@TestCaseParams(testCaseID = {"26289", "29259"})
	public void testSendMessageToTeacher() throws Exception {

		//institutionName = institutionsName[18];
		//pageHelper.restartBrowserInNewURL(institutionName, true);

		//String institutionId = configuration.getInstitutionId();
		userName = dbService.getUserNameById(studentId, institutionId);

		report.startStep("Click on Inbox icon");
		inboxPage = homePage.openInboxPage(false);
		inboxPage.switchToInboxFrame();

		report.startStep("Verify Inbox Page Title");
		inboxPage.verifyInboxPageTitle();

		report.startStep("Click on Write section");
		inboxPage.clickOnWriteSection();
		sleep(2);

		// Retrieve the teacher's name
		String tname[] = inboxPage.getAndSelectTeacher();

		report.startStep("Click on Write action");
		inboxPage.clickOnWriteAction();

		report.startStep("Check the Teacher's Name in the 'Write Message' area");
		inboxPage.checkTeacherName(tname, "writeMessage");

		report.startStep("Click on Attach and Upload file");
		String fileName = "FileToUpload.txt";
		String bodyFile = "\\\\" + configuration.getGlobalProperties("logserverName") + "\\AutoLogs\\ToolsAndResources\\Shared\\" + fileName;
		inboxPage.uploadAnAttachment(bodyFile);

		webDriver.switchToNextTab();
		webDriver.switchToPopup();
		webDriver.switchToFrame("ReadWrite");

		report.startStep("Insert Subject and Body text to mail form");
		String subject = "Subject to Teacher";
		String body = "This is the text for body";
		inboxPage.addSubjectAndText(subject, body);

		report.startStep("Click on Send message");
		inboxPage.clickOnSendMessage();

		webDriver.switchToNextTab();
		inboxPage.switchToInboxFrame();

		report.startStep("Click on Sent section");
		inboxPage.clickOnSentMessagesArea();

		report.startStep("Check message was sent to teacher");
		inboxPage.checkTeacherName(tname, "sentMessageToTeacher");
		inboxPage.checkSubject(subject, 4);
		inboxPage.checkAttachIcon();

		report.startStep("Open message via Sent section and close the tab");
		inboxPage.openMessageAndCheckNameAndSubject(tname, subject);

		String bodyFileText = "This is text file for upload.";
		inboxPage.verifyAttachedFile(bodyFileText);
		webDriver.switchToNextTab();
		inboxPage.switchToInboxFrame();
		webDriver.closeNewTab(1);
		webDriver.switchToMainWindow();

		report.startStep("Logout as a student");
		homePage.clickOnLogOut();
		loginPage = new NewUXLoginPage(webDriver, testResultService);
		//testResultService.assertEquals(false, loginPage.isSubmitButtonEnabled(), "Checking that ED Login Page displayed");
		homePage.waitToLoginArea();

		report.startStep("Restart browser in new URL");
		institutionName = institutionsName[0];
		pageHelper.restartBrowserInNewURL(institutionName, true);
		institutionId = dbService.getInstituteIdByName(institutionName);

		report.startStep("Login as Teacher");
		String teachername = dbService.getUserNameById(tname[1], institutionId);
		pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(teachername, institutionId));
		loginPage.loginAsTmsUser(teachername, "12345");
		pageHelper.skipOptin();
		homePage.closeAllNotifications();

		tmsHomePage = new TmsHomePage(webDriver, testResultService);
		tmsHomePage.waitForPageToLoad();
		tmsHomePage.switchToMainFrame();

		report.startStep("Open Communication and Teacher's Inbox page");
		inboxPage.goToInboxCommunication();

		tmsHomePage.switchToTableFrame();

		report.startStep("Verify the teacher received the correct message from student");
		//tmsHomePage.verifyTMSAttachmentIcon();
		inboxPage.verifySenderStudentName(userName);
		inboxPage.verifySubject(subject);

		report.startStep("Open mail from Student");
		tRead = tmsHomePage.openLatestStudentMessage();
		sleep(1);
		webDriver.switchToNextTab();

		report.startStep("Verify Subject");
		String actualSubject = tRead.getStudentSubjectAnswerText();
		testResultService.assertEquals(subject, actualSubject, "Test not match");

		report.startStep("Verify teacher recieved the attachment");
		inboxPage.verifyAndClickAttachfileExistInTMSReadMessage(fileName);
		webDriver.switchToNewWindow(2);
		inboxPage.verifyFileuploadedText(bodyFileText);
		webDriver.closeNewTab(2);
		webDriver.switchToNextTab();

		report.startStep("Verify Body text");
		String actualBody = tRead.getBodyMessageFromTeacher();
		inboxPage.verifyStudentSubjectMessage(actualBody, body);

		report.startStep("Reply to The Student From The Teacher");
		String subjectFromTeacher = "Automated subject";
		String bodyResponseFromTeacher = "Automated body answer form teacher";
		inboxPage.replyToStudentFromTeacher(userName, subjectFromTeacher, bodyResponseFromTeacher, bodyFile);

		webDriver.switchToMainWindow();
		tmsHomePage.switchToMainFrame();

		report.startStep("Logout as teacher");
		tmsHomePage.clickOnExit();
		webDriver.switchToTopMostFrame();
		testResultService.assertEquals(false, loginPage.isSubmitButtonEnabled(), "Checking that ED Login Page displayed");
		//	sleep(3);

		report.startStep("Restart browser in new URL");
		institutionName = institutionsName[0];
		pageHelper.restartBrowserInNewURL(institutionName, true);
		institutionId = dbService.getInstituteIdByName(institutionName);

		report.startStep("Login as student and verify received mail from teacher");
		loginPage.loginAsStudent(userName, "12345");
		homePage.closeAllNotifications();
		homePage.waitHomePageloaded();

		report.startStep("Move to Inbox and check the message received from Teacher");
		inboxPage = homePage.openInboxPage(false);
		sleep(5);
		inboxPage.switchToInboxFrame();
		inboxPage.checkTeacherName(tname, "sentMessageToTeacher");
		inboxPage.checkSubject(subjectFromTeacher, 5);
		sleep(5);

		report.startStep("Open message via Inbox section");
		inboxPage.openMessageAndCheckNameAndSubject(tname, subjectFromTeacher);
		webDriver.closeNewTab(1);
	}

	
	@Test
	@TestCaseParams(testCaseID = { "74847", "74839", "74836" })
	public void testVerifyMessagesCount() throws Exception{
		
	// Retrieve the data of the logged student
	//String institutionId = configuration.getInstitutionId();
	//String studentUserNameOR = dbService.getUserNameById(studentId, institutionId);

		//createUserAndLoginNewUXClass();
		//getUserAndLoginNewUXClass();
		/*
		loginPage = new NewUXLoginPage(webDriver, testResultService);
		loginPage.enterUserName("stud22648094");
		loginPage.enterPassowrd("12345");
		loginPage.clickOnSubmit();
		studentId = "52322820310878";
		*/
		
		
		report.startStep("Retrieve current number of messages");
		int currectMessagesCounter = homePage.retrieveMessagesCount();
		sleep(1);
		
		report.startStep("Validate Messages Counter is " + currectMessagesCounter);
		homePage.validateInboxCounter(currectMessagesCounter);
		
		String teacherId = dbService.getUserIdByUserName(configuration.getProperty("teacher.username"), autoInstitution.getInstitutionId());
		
		report.startStep("Send 2 Messages From the Teacher to the Student");
		pageHelper.sendMessageByDb(teacherId,studentId,"This is the subject","This is the text%");
		sleep(2);
		pageHelper.sendMessageByDb(teacherId,studentId,"This is the subject 2","This is the text 2");
		
		report.startStep("Refresh Home Page");
		sleep(2);
		webDriver.refresh();
		homePage.waitHomePageloadedFully();
		
		report.startStep("Validate Messages Counter Got Up by 2");
		homePage.validateInboxCounter(currectMessagesCounter+2);
		
		report.startStep("Open Inbox Page to Reset the Counter");
		inboxPage = homePage.openInboxPage(false);	
		inboxPage.waitInboxPageLoaded();
		Thread.sleep(3000);
		
		report.startStep("Close the Inbox Page (Go to Home Page)");
		webDriver.closeNewTab(1);
		Thread.sleep(7000);
		
		report.startStep("Validate Messages Counter is 0");
		homePage.validateInboxCounter(0);
	}
	
	@After
	public void tearDown() throws Exception {
		super.tearDown();
		userName="";
	}
}
