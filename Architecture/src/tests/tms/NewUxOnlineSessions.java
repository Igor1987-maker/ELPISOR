package tests.tms;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Interfaces.TestCaseParams;
import pageObjects.CreateNewOnlineSessionPage;
import pageObjects.EdoOnlineSessionsPage;
import pageObjects.OnlineSessionsPage;
import pageObjects.edo.NewUXLoginPage;
import pageObjects.tms.TmsHomePage;
import tests.edo.newux.SpeechRecognitionBasicTestNewUX;

public class NewUxOnlineSessions  extends SpeechRecognitionBasicTestNewUX{
	@Before
	public void setup() throws Exception {
				super.setup();
	}
	
	@Test
	@TestCaseParams(testCaseID = { "75024" , "74392" , "74404" , "74401" }) // user stories:"74244" , "74245"
	public void testCreateOnlineSession() throws Exception {
		
		NewUXLoginPage loginPage = new NewUXLoginPage(webDriver,testResultService);
		TmsHomePage tmsHomePage;
		OnlineSessionsPage onlineSessions;
		CreateNewOnlineSessionPage createNewOnlineSessionPage;
		EdoOnlineSessionsPage edoOnlineSessionsPage;
		
		boolean isDeleted = false;
		String onlineSessionTitle = "QT test " + dbService.sig(5);
		String onlineSessionLink = "https://zoom.us/";
		
		try {
			
			report.startStep("Init Data");
			ArrayList<String> classesList = new ArrayList<String>();
			String className = configuration.getClassName();
		//	className = "assessmentClass";
		//	String userName = configuration.getStudentUserName();
			classesList.add(className);
		
			report.startStep("Create user and login");
			homePage = createUserAndLoginNewUXClass();
			homePage.closeAllNotifications();
			report.startStep("Retrieve User Info");
			String institutionId =  configuration.getInstitutionId();
			String userName= dbService.getUserNameById(studentId, institutionId);
			
			// Initialize edo online sessions page
			edoOnlineSessionsPage = new EdoOnlineSessionsPage(webDriver, testResultService);

			report.startStep("Check No Online Sessions Are Displayed in Edo");
			homePage.clickOnUserAvatar();
			homePage.checkTextAndclickOnOnlineSessionsWithoutClosing();
			edoOnlineSessionsPage.validateOnlineSessionsTableIsEmpty();
			homePage.closeModalPopUp();
			
			report.startStep("Logout of ED as Student");
			homePage.clickOnLogOut();
			
			report.startStep("Login as Teacher");
			pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(configuration.getProperty("teacher.username"), autoInstitution.getInstitutionId()));
			sleep(2);
			tmsHomePage = loginPage.enterTeacherUserAndPassword();
			sleep(2);
			homePage.closeAllNotifications();
			// Initialize online session page
			onlineSessions = new OnlineSessionsPage(webDriver,testResultService);
			
			report.startStep("Go To Online Sessions Page");
			onlineSessions.goToOnlineSessions();
			
			// Initialize create online session page
			createNewOnlineSessionPage = new CreateNewOnlineSessionPage(webDriver,testResultService);
			
			report.startStep("Retrieve Current Date and Create Start Date and End Date");
			String currentDate = pageHelper.getCurrentDateByFormat("yyyy-MM-dd HH:mm");
			String startDate = textService.updateTime(currentDate, "add", "hour", 3);
			String endDate = textService.updateTime(currentDate, "add", "hour", 4);
			startDate = textService.convertDateToDifferentFormat(startDate, "yyyy-MM-dd HH:mm", "dd/MM HH:mm");
			endDate = textService.convertDateToDifferentFormat(endDate, "yyyy-MM-dd HH:mm", "dd/MM HH:mm");

			report.startStep("Create Online Session");
			createNewOnlineSessionPage.createNewOnlineSession(onlineSessionTitle, className, startDate, endDate, onlineSessionLink,userName);
			
			report.startStep("Validate Online Session was Created Successfully");
			onlineSessions.checkNewOnlineSessionAppearsInTable(onlineSessionTitle);
			onlineSessions.checkOnlineSessionDetails(onlineSessionTitle, startDate, endDate, className, onlineSessionLink);
			
			
				report.startStep("Create Second Online Session");
				String secondOnlineSessionTitle = onlineSessionTitle + " (second)";
				createNewOnlineSessionPage.createNewOnlineSession(secondOnlineSessionTitle, className, startDate, endDate, onlineSessionLink,userName);
	
				// change time in db for the second online session
				report.startStep("Change the Time of the Second Online Session via DB and Refresh");
				String expiredStartDate = textService.updateTime(currentDate, "reduce", "hour", 6);
				String expiredEndDate = textService.updateTime(currentDate, "reduce", "hour", 5);
				dbService.updateTimeOfOnlineSession(secondOnlineSessionTitle, expiredStartDate, expiredEndDate);
				webDriver.refresh();
				
				report.startStep("Go To Online Sessions Page");
				onlineSessions.goToOnlineSessions();
				
				report.startStep("validate Second Online Session is Not Displayed in Table");
				onlineSessions.verifyOnlineSessionIsNotDisplayedInTheTable(secondOnlineSessionTitle);
				
			
			report.startStep("Logout of TMS as Teacher");
			tmsHomePage.clickOnExit();
			sleep(3);
			webDriver.refresh();
			
			report.startStep("Login again as the Student");
			loginPage.loginAsStudent(userName, "12345");
			homePage.closeAllNotifications();
			homePage.waitUntilLoadingMessageIsOver();
			
			report.startStep("Validate the New Online Session Is Displayed");
			homePage.clickOnUserAvatar();
			homePage.checkTextAndclickOnOnlineSessionsWithoutClosing();
			sleep(2);
			edoOnlineSessionsPage.validateOnlineSessionsAppearsInTable(onlineSessionTitle);
			edoOnlineSessionsPage.checkOnlineSessionDetailsInED(onlineSessionTitle, startDate, endDate, onlineSessionLink);
			
				report.startStep("Validate the Expired Online Session Is Not Displayed");
				homePage.clickOnUserAvatar();
				homePage.checkTextAndclickOnOnlineSessionsWithoutClosing();
				edoOnlineSessionsPage.validateOnlineSessionsDoesntAppearsInTable(secondOnlineSessionTitle);
				homePage.closeModalPopUp();
			
			report.startStep("Logout of ED as Student");
			homePage.clickOnLogOut();
			sleep(3);
			
			report.startStep("Login as Teacher");
			pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(configuration.getProperty("teacher.username"), autoInstitution.getInstitutionId()));
			sleep(2);
			tmsHomePage = loginPage.enterTeacherUserAndPassword();
			sleep(2);
			homePage.closeAllNotifications();
			
			report.startStep("Go To Online Sessions Page");
			onlineSessions.goToOnlineSessions();
			sleep(1);
			
			report.startStep("Delete the Online Session");
			onlineSessions.deleteOnlineSession(onlineSessionTitle);
			sleep(2);
			
			report.startStep("Verify the Online Session is Deleted");
			isDeleted = onlineSessions.verifyOnlineSessionIsNotDisplayedInTheTable(onlineSessionTitle);
			sleep(2);
			
			report.startStep("Logout of TMS as Teacher");
			tmsHomePage.clickOnExit();
			sleep(3);
			webDriver.refresh();
		
			report.startStep("Login again as the Student");
			loginPage.loginAsStudent(userName, "12345");
			homePage.closeAllNotifications();
			homePage.waitUntilLoadingMessageIsOver();
			homePage.waitHomePageloaded();
			
			report.startStep("Validate the Online Session Is Not Displayed");
			homePage.clickOnUserAvatar();
			homePage.checkTextAndclickOnOnlineSessionsWithoutClosing();
			edoOnlineSessionsPage.validateOnlineSessionsDoesntAppearsInTable(onlineSessionTitle);
			homePage.closeModalPopUp();
			
			report.startStep("Logout of ED as Student");
			homePage.clickOnLogOut();
			sleep(3);
			
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			
			if (!isDeleted) {
			
				report.startStep("Close browser and open again");
				webDriver.quitBrowser();
				sleep(2);
				webDriver.init();
				sleep(2);
				webDriver.maximize();
			
				report.startStep("Login again with the same user");
				webDriver.openUrl(pageHelper.CILink);
				
				report.startStep("Login as Teacher");
				pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(configuration.getProperty("teacher.username"), autoInstitution.getInstitutionId()));
				sleep(2);
				tmsHomePage = loginPage.enterTeacherUserAndPassword();
				sleep(2);
				homePage.closeAllNotifications();
				// Initialize online session page
				onlineSessions = new OnlineSessionsPage(webDriver,testResultService);
				
				report.startStep("Go To Online Sessions Page");
				onlineSessions.goToOnlineSessions();
				
				report.startStep("Delete the Online Session");
				onlineSessions.deleteOnlineSession(onlineSessionTitle);
				sleep(1);
				
				report.startStep("Verify the Online Session is Deleted");
				onlineSessions.verifyOnlineSessionIsNotDisplayedInTheTable(onlineSessionTitle);
				
				report.startStep("Logout of TMS as Teacher");
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
