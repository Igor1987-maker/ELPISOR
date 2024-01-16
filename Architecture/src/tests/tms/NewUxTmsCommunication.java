package tests.tms;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import Enums.ByTypes;
import Interfaces.TestCaseParams;
import pageObjects.CreateNewForumPage;
import pageObjects.CreateNewMessageInModerateForumPage;
import pageObjects.TMSForumPage;
import pageObjects.TMSModerateForumsPage;
import pageObjects.edo.NewUXLoginPage;
import pageObjects.edo.NewUxLearningArea;
import pageObjects.edo.NewUxMyProfile;
import pageObjects.edo.NewUxCommunityPage;
import pageObjects.tms.DashboardPage;
import pageObjects.tms.TmsHomePage;
import tests.edo.newux.SpeechRecognitionBasicTestNewUX;

public class NewUxTmsCommunication extends SpeechRecognitionBasicTestNewUX {

	protected NewUxCommunityPage communityPage;	
	NewUXLoginPage loginPage;
	DashboardPage dashboardPage;
	TmsHomePage tmsHomePage;
	NewUxLearningArea learningArea;
	NewUxMyProfile myProfile;
	
	TMSForumPage tmsForumPage;
	CreateNewForumPage createNewForumPage;
	TMSModerateForumsPage tmsModerateForumsPage;
	CreateNewMessageInModerateForumPage createNewMessageInModerateForumPage;
	
	//String instID = "";
	String userName ="";

	private boolean deleteForum;
	private boolean deleteMessage;
	private boolean unassignForum;
	private String forumNameForDeletion = "";
	private String messageTitle;
	private String messageContent;
	
	@Before
	public void setup() throws Exception {
		super.setup();
		
	}
	
	@Test
	@TestCaseParams(testCaseID = { "44340" })
	public void testCreateNewForum() throws Exception {
		
		report.startStep("Init Test Data");
		String forumName = "Forum" + dbService.sig(3);
		forumNameForDeletion = forumName;
		String firstMessage = "Title" + dbService.sig(3);
		//String modifiedForumName = "";
		//instID = configuration.getProperty("institution.id");
		pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(configuration.getProperty("schoolAdmin.user"),institutionId));
		
		report.startStep("Login as Admin");
		loginPage = new NewUXLoginPage(webDriver, testResultService);
		tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
		homePage.waitUntilLoadingMessageIsOver();
		
		// Initialize tms forum page
		tmsForumPage = new TMSForumPage(webDriver, testResultService);
		
		report.startStep("Go to My Forums");
		tmsForumPage.goToForums();
		
		// Initialize create new forum page
		createNewForumPage = new CreateNewForumPage(webDriver,testResultService);
		
		report.startStep("Create New Forum");
		List<WebElement> textInputs = createNewForumPage.createNewForum(forumName, firstMessage);
		
		boolean isForumDisplayed = false;
		try{
			deleteForum = true;
			
			report.startStep("Edit forum name");
			forumNameForDeletion = createNewForumPage.editForum(textInputs, forumName);
		
			report.startStep("Verify new Forum Name Appears");
			isForumDisplayed = tmsForumPage.validateForumIsDisplayedInList(forumNameForDeletion);
	
		} catch (Exception e) {
			testResultService.addFailTest(e.toString(), false, true);
			dbService.deleteForumMessgeByTitleAndContent(messageTitle, messageContent);
		}
	
		if (isForumDisplayed) {
			report.startStep("Delete Newly Created Forum");	
			tmsForumPage.deleteForum(forumNameForDeletion);
			sleep(3);
				
			report.startStep("Verify Forum Deletion");
			boolean isDeleted = tmsForumPage.validateForumIsNotDisplayedInList(forumNameForDeletion);
			if (isDeleted) {
				deleteForum = false;
			}
			sleep(3);
		} else {
			testResultService.addFailTest("forum name was not edited. not performing delete");
		}
			
		report.startStep("Logout of TMS");
		tmsHomePage.clickOnExit();
	}

	@Test
	@TestCaseParams(testCaseID = { "44341" })
	public void testAssignExistingForum() throws Exception {
		
		report.startStep("Init Test Data");
		
			String className = configuration.getProperty("classname.progress");
			String studentName = configuration.getProperty("student.user.name");
			String studentPassword = configuration.getProperty("student.user.password");
			String expectedIntroText = "This forum is used to test assign TC 44341";
			//instID = configuration.getProperty("institution.id");
			pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(configuration.getProperty("schoolAdmin.user"),institutionId));
		
		report.startStep("Login as Admin");
			loginPage = new NewUXLoginPage(webDriver, testResultService);
			tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
			homePage.waitUntilLoadingMessageIsOver();
		
		report.startStep("Click on Communication");
			tmsHomePage.switchToMainFrame();
			tmsHomePage.clickOnCommunication();
			sleep(3);
		
		report.startStep("Click on Assign Forums");
			tmsHomePage.clickOnAssignForums();
			sleep(2);
		
		report.startStep("Select Class to Assign Forum");
			tmsHomePage.switchToFormFrame();
			tmsHomePage.selectClass(className, false, true);
			
		report.startStep("Assign Forum");
			webDriver.switchToTopMostFrame();
			tmsHomePage.switchToMainFrame();
			clickCheckBoxForum();
			tmsHomePage.clickOnPromotionAreaMenuButton("Save");
			unassignForum = true;
		
		report.startStep("Verify Forum Status with Reset button");
			tmsHomePage.clickOnPromotionAreaMenuButton("Reset");
			sleep(3);
			testResultService.assertEquals(true, checkBoxForumStatus().isSelected());
		
		report.startStep("Enter Preview and verify intro text");
			clickonForumPreview();
			checkForumWindowIntroText(expectedIntroText); 
			
			webDriver.closeNewTab(1);
			webDriver.switchToMainWindow();
			tmsHomePage.switchToMainFrame();
			
		report.startStep("Logout of TMS");
			tmsHomePage.clickOnExit();
			testResultService.assertEquals(false, loginPage.isSubmitButtonEnabled(), "Checking that Login Page displayed");
			
		report.startStep("Login to ED");
			pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(configuration.getProperty("student.user.name"),institutionId));
			loginPage.loginAsStudent(studentName,studentPassword);
			sleep(3);
			homePage.closeModalPopUp();
			
		report.startStep("Navigate to forum page");
			communityPage = homePage.openNewCommunityPage(false);
			communityPage.openForumPage();
			sleep(3);
			webDriver.switchToNextTab();
			webDriver.printScreen();
			webDriver.switchToFrame(webDriver.getElement(By.className("oed__iframe")));

		report.startStep("Verify Forum appears in forum page");
			checkForumAssignedtoStudent("Assign Test");
			
		report.startStep("Logout of ED");
			webDriver.closeNewTab(1);
			homePage.clickOnLogOut();
			testResultService.assertEquals(false, loginPage.isSubmitButtonEnabled(), "Checking that Login Page displayed");
			
		report.startStep("Login to TMS");
			tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
		
		report.startStep("Click On Communication");
			tmsHomePage.switchToMainFrame();
			tmsHomePage.clickOnCommunication();
			sleep(3);
			
		report.startStep("Click On Assign Forum");
			tmsHomePage.clickOnAssignForums();
			sleep(2);
			
		report.startStep("Select Class to remove forum");
			tmsHomePage.switchToFormFrame();
			tmsHomePage.selectClass(className, false, true);
		
		report.startStep("Remove Forum From Class");
			webDriver.switchToTopMostFrame();
			tmsHomePage.switchToMainFrame();
			clickCheckBoxForum();
			tmsHomePage.clickOnPromotionAreaMenuButton("Save");
			unassignForum = false;
			
		report.startStep("Verify Forum Removal Via Reset");
			tmsHomePage.clickOnPromotionAreaMenuButton("Reset");
			sleep(1);
			testResultService.assertEquals(false, checkBoxForumStatus().isSelected());
	}


	@Test
	@TestCaseParams(testCaseID = { "44342" })
	public void testModerateForum() throws Exception {
		
		report.startStep("Init Test Data");
		String className = configuration.getProperty("classname.nostudents");
		String institutionId = configuration.getProperty("institution.id");
		String forumName = dbService.getFirstInstitutionForumTitle(institutionId);
		messageTitle = "title" + dbService.sig(3);
		messageContent = "Content" + dbService.sig(10);
		//instID = configuration.getProperty("institution.id");
		pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(configuration.getProperty("schoolAdmin.user"),institutionId));
	
		report.startStep("Login as Admin");
		loginPage = new NewUXLoginPage(webDriver, testResultService);
		tmsHomePage = loginPage.enterSchoolAdminUserAndPassword();
		homePage.waitUntilLoadingMessageIsOver();
		
		// Initialize tms moderate forum page
		tmsModerateForumsPage = new TMSModerateForumsPage(webDriver, testResultService);
		
		report.startStep("Click on Communication");
		tmsModerateForumsPage.goToModerateForums();
		
		report.startStep("Select Class and Forum to Moderate");
		tmsModerateForumsPage.selectClassAndForum(className, forumName);
		
		// Initialize tms create new message in moderate forum page
		createNewMessageInModerateForumPage = new CreateNewMessageInModerateForumPage(webDriver, testResultService);
		
		report.startStep("Open Message Creation Window");
		createNewMessageInModerateForumPage.addNewMessage(messageTitle, messageContent);
		deleteMessage = true;
		
		try {		
			report.startStep("Refresh page (Click on go button) to see message");
			tmsHomePage.switchToFormFrame();
			tmsModerateForumsPage.clickGoButton();
			sleep(2);
			webDriver.switchToMainWindow();
			tmsHomePage.switchToMainFrame();
			
			report.startStep("Compare message title and expand message");
			tmsModerateForumsPage.checkMessageWasAddedCorrectly(messageTitle, messageContent);
			
			report.startStep("Exapnd message again and delete it");
			tmsModerateForumsPage.deleteMessage(messageTitle);
	
		} catch (Exception e) {
			testResultService.addFailTest(e.toString(), false, true);
			dbService.deleteForumMessgeByTitleAndContent(messageTitle, messageContent);
			deleteMessage = false;
		}

		report.startStep("Refresh page (Click on go button)");
		webDriver.switchToTopMostFrame();
		tmsHomePage.switchToMainFrame();
		tmsHomePage.switchToFormFrame();
		tmsModerateForumsPage.clickGoButton();
		sleep(2);	
		
		report.startStep("Verify message was deleted");
		webDriver.switchToTopMostFrame();
		tmsHomePage.switchToMainFrame();
		tmsModerateForumsPage.validateMessageIsNotDisplayedInList(messageTitle);
	
		report.startStep("Logout of TMS");
		tmsHomePage.clickOnExit();
		testResultService.assertEquals(false, loginPage.isSubmitButtonEnabled(), "Checking that Login Page displayed");
	}
	
	private void clickCheckBoxForum() throws Exception {
	
		WebElement element = checkBoxForumStatus();
		element.click();
		//webDriver.waitForElement("//*[@id='ti13429']/..", ByTypes.xpath).click();
		}
		
	private WebElement checkBoxForumStatus() throws Exception {
		WebElement element = webDriver.waitForElement("//*[@id='con']/tr/td[3]/input", ByTypes.xpath);
		return element;
	}
	
	private void clickonForumPreview() throws Exception {
		webDriver.waitForElement("//*[@id='con']/tr/td/a", ByTypes.xpath).click();
		webDriver.switchToPopup();
		sleep(5);
		webDriver.switchToFrame(webDriver.getElement(By.id("threadMessages")));
	}	
	
	private void checkForumAssignedtoStudent(String expectedTitle) throws Exception {
		// TODO Auto-generated method stub
		String actualForumName = "";
		List<WebElement> forumContainers = webDriver.getElementsByXpath("//*[@id='topicsCont']/div/ol/li/a");
		report.report("Forum list size is: " + forumContainers.size());
		try {
			actualForumName = forumContainers.get(8).getText().substring(0, 11);  // First 8 values are global forum
		}
		catch (Exception e) {
			actualForumName = forumContainers.get(0).getText().substring(0, 11);  // In case global forum disabled
		}
		//actualForumName = actualForumName.substring(0, actualForumName.length() - 4);
		testResultService.assertEquals(expectedTitle,actualForumName);
	}
	
	private void checkForumWindowIntroText(String expectedIntroText) throws Exception {
		// TODO Auto-generated method stub
		String actualIntroText = webDriver.waitForElement("//*[@id='con']/tr/td/table[2]/tbody/tr/td", ByTypes.xpath).getText();
		testResultService.assertEquals(expectedIntroText,actualIntroText);
	}
	
	@After
	public void tearDown() throws Exception {
		studentId = dbService.getUserIdByUserName(configuration.getProperty("schoolAdmin.user"),configuration.getProperty("institution.id"));
		
		if (deleteForum) {
			dbService.deleteInstitutionForumByTitle(forumNameForDeletion);
		}
	/*	
		if (deleteMessage) {
			dbService.deleteForumMessgeByTitleAndContent(messageTitle, messageContent);
		}
	*/
		if (unassignForum) {
			String userId = dbService.getUserIdByUserName(configuration.getProperty("schoolAdmin.user"), institutionId);
			String classId = dbService.getClassIdByName(configuration.getProperty("classname.progress"), institutionId);
			dbService.unassignForumForClass(classId, userId);
		}
		
		super.tearDown();
	}
	
}
