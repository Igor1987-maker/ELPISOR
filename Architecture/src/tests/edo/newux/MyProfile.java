package tests.edo.newux;

import Interfaces.TestCaseParams;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.JavascriptExecutor;
import pageObjects.edo.*;
import testCategories.edoNewUX.ReleaseTests;
//@Category(reg1.class)
public class MyProfile extends BasicNewUxTest {
	NewUxLearningArea2 learningArea2;
	
	
	@Before
	public void setup() throws Exception {
		institutionName=institutionsName[0];
		super.setup();
		
		report.startStep("Get user and login");
		dbService.setInstitutionMyProfileGroupId(institutionId , "2");
		String url= webDriver.getUrl();
		
		webDriver.closeBrowser();
		webDriver.init();
		webDriver.maximize();
		webDriver.openUrl(url);
		
		getUserAndLoginNewUXClass();
		//homePage.waitHomePageloaded();
	}

	@Category(ReleaseTests.class)
	@Test
	@TestCaseParams(testCaseID = { "22129", "22134", "22145", "22132" },skippedBrowsers={"chromeAndroid","safari","safariMac"} )
	public void testMyProfileChangeValuesAndCloseLightbox() throws Exception {
		//webDriver.maximize();
		//report.startStep("Get user and login");
		//getUserAndLoginNewUXClass();
		
		report.startStep("Open My profile");
		homePage.clickOnMyProfile();
		homePage.switchToMyProfile();
		
		report.startStep("Change some values and close the lightbox by clicing the X button");

		NewUxMyProfile myProfile = new NewUxMyProfile(webDriver,
				testResultService);

		String oldFirstName = myProfile.getFirstName();
		String oldUserName = myProfile.getUserName();
		String oldLastname = myProfile.getLastName();
		String oldMail = myProfile.getMail();

		String newFirstName = "aaaaaaaa";
		String newLastName = "bbbbbbbb";
		String newUserName = dbService.sig(8);
		String newMail = "aaa@gmail.com";
		
		changeUserCredentials(newFirstName,newUserName,newLastName, newMail);
		myProfile.close();
		sleep(1);
		homePage.clickOnMyProfile();
		homePage.switchToMyProfile();
		sleep(2);
		
		checkUserCredentials(oldFirstName, oldUserName, oldLastname, oldMail);
		
		report.startStep("Change values again and click Update");
		changeUserCredentials(newFirstName,newUserName,newLastName, newMail);
		myProfile.clickOnUpdate();
		sleep(2);
		myProfile.close(true);
		sleep(2);
		webDriver.switchToMainWindow();
		
		report.startStep("Check changed values");
		homePage.clickOnMyProfile();
		homePage.switchToMyProfile();
		sleep(2);
		
		checkUserCredentials(newFirstName, newUserName, newLastName, newMail);
		
		report.startStep("Reset student details");
		changeUserCredentials(oldFirstName,oldUserName,oldLastname, oldMail);
		
		myProfile.clickOnUpdate();
		sleep(2);
		myProfile.close(true);
		sleep(2);
		webDriver.switchToMainWindow();
		
	}
	
	@Test
	@TestCaseParams(testCaseID = { "22139" },skippedBrowsers={"safariMac"})
	public void testChangeYourPassword() throws Exception {
		
		report.startStep("Open My profile");
		homePage.clickOnMyProfile();
		homePage.switchToMyProfile();

		NewUxMyProfile myProfile = new NewUxMyProfile(webDriver, testResultService);

		report.startStep("Click on change password and then click on cancel");
		String mainwin = myProfile.clickOnChangePassword();
		myProfile.clickOnCancel();
		sleep(1);
		webDriver.switchToMainWindow(mainwin);
		webDriver.switchToTopMostFrame();
		homePage.switchToMyProfile();

		report.startStep("Open the password dialog again and click on Submit");
		myProfile.clickOnChangePassword();
		myProfile.clickOnSubmit();
		sleep(2);
		
		report.startStep("Validate Message");
		checkAlertMessage("Please enter your old password.", false);

		report.startStep("Fill wrong old password and correct new password");
		myProfile.enterOldPassword("123456");
		myProfile.enterNewtNewPassword("1234567");
		myProfile.enterConfirmPassword("1234567");
		
		try{
			
			myProfile.clickOnSubmit();
			sleep(1);
			
			report.startStep("Validate Message");
			String msg = "Your password is incorrect.";
			checkAlertMessage(msg, true);
	
			report.startStep("Fill correct old password and invalid new password(2nd not the same)");
			myProfile.enterOldPassword("12345");
			myProfile.enterNewtNewPassword("1234567");
			myProfile.enterConfirmPassword("123456");
			myProfile.clickOnSubmit();
			sleep(2);
			
			report.startStep("Validate Message");
			checkAlertMessage("Your password and password confirmation must be identical.",false);
			report.startStep("Fill correct old password and valid new password");
			myProfile.enterOldPassword("12345");
	
			String newPass = "123456";
			myProfile.enterNewtNewPassword(newPass);
			myProfile.enterConfirmPassword(newPass);
			myProfile.clickOnSubmit();
	
			report.startStep("Logout and login");
			webDriver.switchToMainWindow(mainwin);
			myProfile.close();
			sleep(1);
			homePage.clickOnLogOut();
			webDriver.switchToMainWindow();
			webDriver.switchToTopMostFrame();
			
			NewUXLoginPage loginPage = new NewUXLoginPage(webDriver, testResultService);
			loginPage.enterUserName(dbService.getUserNameById(studentId, configuration.getInstitutionId()));
			loginPage.enterPassowrd(newPass);
			loginPage.clickOnSubmit();
			homePage.clickOnContinueButton();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			report.startStep("Reset the Password");
			dbService.changeUserPassword(studentId);	
		}
	}
	
	@Test
	//@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "26104" })
	public void testRightClickDictionarySettings() throws Exception {
		
		// Init data
		String word = "Units";
		NewUxMyProfile myProfile = new NewUxMyProfile(webDriver, testResultService);

		report.startStep("Open My Profile");
		homePage.clickOnMyProfile();
		homePage.switchToMyProfile();
		
		report.startStep("Get the selected language");
		String actualLanguage = "English/" + myProfile.getTheSelectedMyProfileLanguages(); // for new: //select[@id='languages-select']
		
		report.startStep("Open Dictionary Settings via My Profile");
		myProfile.scrollDownMyProfile();
		myProfile.openDictionarySetting();
		webDriver.switchToNewWindow();
		webDriver.switchToFrame("content");
		
		NewUxDictionaryPage dictionaryPage = new NewUxDictionaryPage(webDriver, testResultService);
		
		report.startStep("Verify Dictionary is Enabled and Close Window");
		boolean isEnabled = dictionaryPage.validateEnableChecked();
		if (!isEnabled) {
			dictionaryPage.clickEnableButton();
			dictionaryPage.clickSave();
		} else {
			dictionaryPage.clickCancel();
		}
		webDriver.switchToMainWindow();
		homePage.switchToMyProfile();
		myProfile.clickUpdateMyProfileWithoutScroll();
		myProfile.close();
		webDriver.switchToMainWindow();
		sleep(3);
		
		report.startStep("Validate Dictionary Works When Enabled");
		boolean isWordDisplayed = myProfile.performRightClickOnGivenWord(word);
		sleep(2);
		
		NewUxDictionaryPopUpPage dictionaryPopUpPage = new NewUxDictionaryPopUpPage(webDriver, testResultService);
		
		if(isWordDisplayed) {
			dictionaryPopUpPage.validateDictionaryPopUp(word, actualLanguage);
		} else {
			testResultService.addFailTest("Wanted Word to Press is not displayed", false, true);
		}
		
		report.startStep("Close Dictionary Popup and Open My Profile");
		dictionaryPopUpPage.closeDictionaryPopup();
		homePage.clickOnMyProfile();
		homePage.switchToMyProfile();
		
		report.startStep("Open Dictionary Page");
		myProfile.scrollDownMyProfile();
		myProfile.openDictionarySetting();
		webDriver.switchToNewWindow();
		webDriver.switchToFrame("content");
		
		report.startStep("Disable the Dictionary");
		dictionaryPage.clickDisableButton();
		
		report.startStep("Click Save");
		dictionaryPage.clickSave();
		
		report.startStep("Click Update My Profile");
		webDriver.switchToMainWindow();
		homePage.switchToMyProfile();
		myProfile.clickUpdateMyProfileWithoutScroll();
		
		report.startStep("Close My Profile Page");
		myProfile.close();
		webDriver.switchToMainWindow();
		
		report.startStep("Validate Dictionary Is not Displayed When Disabled and Right Click is Pressed");
		isWordDisplayed = myProfile.performRightClickOnGivenWord(word);
		if (isWordDisplayed) //{
			checkDictionaryPopUpVisibility(false);
		//} else {
			//testResultService.addFailTest("Wanted Word to Press is not displayed", false, true);
		//}
		
		report.startStep("Restore default Dictionary settings - Enable");
		
		report.startStep("Open My Profile Page");
		homePage.clickOnMyProfile();
		homePage.switchToMyProfile();
		
		report.startStep("Open Dictionary Settings Page");
		myProfile.scrollDownMyProfile();
		myProfile.openDictionarySetting();
		webDriver.switchToNewWindow();
		webDriver.switchToFrame("content");
		
		report.startStep("Enable the Dictionary");
		dictionaryPage.clickEnableButton();;
		
		report.startStep("Click Save");
		dictionaryPage.clickSave();
		
		report.startStep("Click Update My Profile");
		webDriver.switchToMainWindow();
		homePage.switchToMyProfile();
		myProfile.clickUpdateMyProfileWithoutScroll();
		
		report.startStep("Close My Profile Page");
		myProfile.close();
		webDriver.switchToMainWindow();
	}

	
	@Test
	@TestCaseParams(testCaseID = { "83184"},skippedBrowsers={"chromeAndroid","safari","safariMac"} )
	public void testMyProfileChangeNullValues() throws Exception {
		
		NewUxMyProfile myProfile = new NewUxMyProfile(webDriver,testResultService);
		
		
		String oldFirstName = "";
		String oldUserName = "";
		String oldLastname = "";
		String oldMail = "";
		
		String[] fieldsToUpdate= {"FirstName","LastName","UserName","Email"};
		String currentField="";
		
		
		for (int i=0; i<fieldsToUpdate.length;i++ ){
			
			report.startStep("Open My profile");
			homePage.clickOnMyProfile();
			homePage.switchToMyProfile();
	
			report.startStep("Change " +fieldsToUpdate[i]+ " value to Null");	
					
			if (fieldsToUpdate[i].equalsIgnoreCase("FirstName")){
				oldFirstName = myProfile.getFirstName();
				myProfile.changeFirstName("");
				currentField = oldFirstName;
			}
			else if (fieldsToUpdate[i].equalsIgnoreCase("LastName")){
				oldLastname = myProfile.getLastName();
				myProfile.changeLastName("");
				currentField = oldLastname;
			}
			else if (fieldsToUpdate[i].equalsIgnoreCase("UserName")){
				oldUserName = myProfile.getUserName();
				myProfile.changeUserName("");
				currentField = oldUserName;
			}
		
			else if (fieldsToUpdate[i].equalsIgnoreCase("Email")){
				oldMail = myProfile.getMail();
				myProfile.changeEmail("");
				currentField = oldMail;
			}
			try{
				myProfile.clickOnUpdate();
				sleep(2);	
				
				// add message and validation
				
				myProfile.close();
				sleep(1);
				
				report.startStep("Open My profile.");
				homePage.clickOnMyProfile();
				homePage.switchToMyProfile();
				sleep(2);
				
				report.startStep("Verify the " +fieldsToUpdate[i] +" It's original and it's not null Except Email");
				checkSingleUserCredential(currentField,fieldsToUpdate[i]);
				
				myProfile.close();
				sleep(1);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				if (fieldsToUpdate[i].equalsIgnoreCase("FirstName")){
					dbService.updateUserFirstNamebyUserId(studentId, oldFirstName);
				}
				else if (fieldsToUpdate[i].equalsIgnoreCase("LastName")){
					dbService.updateUserLastNameByUserId(studentId, oldLastname);
				}
				else if (fieldsToUpdate[i].equalsIgnoreCase("LastName")){
					dbService.updateUserLastNameByUserId(studentId, oldUserName);
				}
			}
		}	
		
	}
		
		@Test
		//@Category(inProgressTests.class)
		@TestCaseParams(testCaseID = { "87007", "87198" })
		public void testRightClickDictionaryDisabledOnTest() throws Exception {
		
			String[] words = { "Chicago", "WNYC radio station is in", "Drag the correct answer/s into place" };
			
			report.startStep("Navigate B1-U1-L1-T1");
			learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, "B1", 1, 1);
			learningArea2.waitForPageToLoad();
			learningArea2.clickOnStep(3, false);
			
			report.startStep("Check if dictionary is enabled");
			learningArea2.clickOnDictionary();
			learningArea2.inputDictionaryTextandPressSearch("milk");
			testResultService.assertEquals(true, learningArea2.getDictionaryBtnElement().isDisplayed());
			
			report.startStep("Start test and confirm dictionaly is disabled");
			learningArea2.clickOnStartTest();
			testResultService.assertEquals(true, learningArea2.getDictionaryBtnElementWhenDisabled().isDisplayed());
			learningArea2.waitForPageToLoad();
			
			NewUxMyProfile myProfile = new NewUxMyProfile(webDriver, testResultService);
			//NewUxDictionaryPopUpPage dictionaryPopUpPage = new NewUxDictionaryPopUpPage(webDriver, testResultService);			
			
			report.startStep("Validate Dictionary Disabled");
			for (int i = 0; i < words.length; i++) {
				boolean isPopUpDisplayed = myProfile.performRightClickOnGivenWord(words[i]);
				sleep(1);
				checkDictionaryPopUpVisibility(isPopUpDisplayed);
				//dictionaryPopUpPage.closeDictionaryPopup();
			}

	}
		
	private void checkAlertMessage(String expectedMessage, boolean contains)
			throws Exception {
		String alertText = webDriver.getAlertText(5);
		if (contains == false) {
			testResultService.assertEquals(expectedMessage, alertText,
					"Wrong error message");
		} else {
			testResultService.assertEquals(true,
					alertText.contains(expectedMessage),
					"Alert text did not contained the expetced text");
		}
		webDriver.closeAlertByAccept();
	}
	
	public void changeUserCredentials(String firstName,String userName,String lastName, String mail) throws Exception{
		NewUxMyProfile myProfile = new NewUxMyProfile(webDriver,
				testResultService);
		
		myProfile.changeFirstName(firstName);
		myProfile.changeUserName(userName);
		myProfile.changeLastName(lastName);
		myProfile.changeEmail(mail);
	}
	
	private void checkUserCredentials(String firstName,String userName,String lastName, String mail) throws Exception {
		
		NewUxMyProfile myProfile = new NewUxMyProfile(webDriver, testResultService);
		
		String currentFirstName = myProfile.getFirstName();
		String currentUserName = myProfile.getUserName();
		String currentLastName = myProfile.getLastName();
		String currentMail = myProfile.getMail();

		report.startStep("Check that no values are changed");
		testResultService.assertEquals(firstName, currentFirstName,
				"First name does not match");

		testResultService.assertEquals(userName, currentUserName,
				"First name does not match");

		testResultService.assertEquals(lastName, currentLastName,
				"First name does not match");
		
		testResultService.assertEquals(mail, currentMail,
				"First name does not match");
		
	}
	
	
	public void checkSingleUserCredential(String expectedValue,String feildType) throws Exception {
		
		NewUxMyProfile myProfile = new NewUxMyProfile(webDriver,testResultService);

		report.startStep("Check that:" +feildType +" it's correct");
		
		if (feildType.equalsIgnoreCase("FirstName")){	
			String currentFirstNameDB = dbService.getUserFirstNameByUserId(studentId);
			String currentFirstNameUI = myProfile.getFirstName();
			testResultService.assertEquals(currentFirstNameUI, currentFirstNameDB, " "+feildType+ " does not match");

		}
		else if (feildType.equalsIgnoreCase("LastName")){
			//String currentLastName = dbService.getUserLastNameByUserId(studentId); //myProfile.getLastName();
			
			String currentLastNameDB = dbService.getUserLastNameByUserId(studentId);
			String currentLastNameUI = myProfile.getLastName();
			
			testResultService.assertEquals(currentLastNameUI, currentLastNameDB, " "+feildType+ " does not match");
		}
		else if (feildType.equalsIgnoreCase("UserName")){
			String currentUserName =  dbService.getUserNameById(studentId, institutionId); //myProfile.getUserName();
			
			if (!currentUserName.equalsIgnoreCase(expectedValue))
				dbService.updateUserNameByUserId(studentId, expectedValue);
			
			testResultService.assertEquals(expectedValue, currentUserName, " "+feildType+ " does not match");
		}
	
		else if (feildType.equalsIgnoreCase("Email")){
			
			String currentUserEmail = myProfile.getMail();
			testResultService.assertEquals("", currentUserEmail, " "+feildType+ " does not match");
		}
		
	}
	
	public boolean checkDictionaryPopUpVisibility(boolean expectedVisibility) throws Exception{
		boolean isVisible = false;
		
		JavascriptExecutor js = (JavascriptExecutor) webDriver.getWebDriver();  
		String jsResult =(String)js.executeScript("return $('a.DictionaryGo').is(':visible')").toString();;
		
		if (jsResult.equals("true")) {
			isVisible = true;
		} else if (jsResult.equals("false")) {
			isVisible = false;
		}
		testResultService.assertEquals(expectedVisibility, isVisible, "Dictionary Popup Visibility is Incorrect.");
		return isVisible;
	}
	
	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}
}
