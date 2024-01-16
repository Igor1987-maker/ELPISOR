package tests.edo.newux;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;

import Enums.ByTypes;
import pageObjects.edo.NewUxCommunityPage;
import pageObjects.edo.NewUxLearningArea;
import pageObjects.edo.NewUxMyProfile;
import pageObjects.edo.NewUxStudyPlanner;
import testCategories.NonAngularLearningArea;
import testCategories.inProgressTests;
import testCategories.reg1;
import testCategories.reg2;
import testCategories.edoNewUX.CourseArea;
import testCategories.edoNewUX.UserData;
import Interfaces.TestCaseParams;
@Category(NonAngularLearningArea.class)
public class UserMenuTests extends BasicNewUxTest {
	
	NewUxMyProfile myProfile;
	NewUxStudyPlanner sPlanner;
	NewUxLearningArea learningArea;
	NewUxCommunityPage communityPage;
	String userFirstName;
		
	
	@Before
	public void setup() throws Exception {
		super.setup();
		report.startStep("Get user and login");
		getUserAndLoginNewUXClass();
		
		report.startStep("Navigate To FD");
		homePage.navigateToRequiredCourseOnHomePage("First Discoveries");
		sleep(1);
		
	}
	
	@Test
	@Category(UserData.class)
	@TestCaseParams(testCaseID = { "27355" , "37929"}, testMultiple = true)
	public void testUserMenuBasicFunction() throws Exception {
		
		report.startStep("Get User First Name");
		userFirstName = dbService.getUserFirstNameByUserId(studentId);
		
		report.startStep("Check User Menu on Home Page");
		checkUserMenuLinks(true, true, true, true);
		
		report.startStep("Navigate To My Progress Page");
		homePage.clickOnMyProgress();
				
		report.startStep("Check User Menu in My Progress");
		checkUserMenuLinks(true, true, false, true);
		
		report.startStep("Navigate To Community Page");
		homePage.clickToOpenNavigationBar();
		communityPage = homePage.openNewCommunityPage(false);
				
		report.startStep("Check User Menu in Community");
		checkUserMenuLinks(true, true, false, true);
		
		report.startStep("Navigate To Magazine Page");
		communityPage.openMagazinePage();	
		
		report.startStep("Check User Menu in Magazine Page");
		checkUserMenuLinks(true, true, false, true);
		
		report.startStep("Navigate To Idioms Page");
		homePage.openNewCommunityPage(false);
		communityPage.openIdiomsPage();
		
		report.startStep("Check User Menu in Idioms Page");
		checkUserMenuLinks(true, true, false, true);
		
		report.startStep("Navigate To Games Page");
		homePage.openNewCommunityPage(false);
		communityPage.openGamesPage();
		
		report.startStep("Check User Menu in Games Page");
		checkUserMenuLinks(true, true, false, true);
				
		report.startStep("Navigate To Learning Area");
		homePage.clickOnHomeButton();
		sleep(3);
		homePage.navigateToRequiredCourseOnHomePage(coursesNames[1]);
		sleep(3);
		homePage.clickOnUnitLessons(9);
		homePage.clickOnLesson(8, 1);
		sleep(1);
		
		report.startStep("Check User Menu in Learning Area");
		checkUserMenuLinks(false, false, true, false);
		sleep(4);
				
		report.startStep("Navigate To Test");
		learningArea.clickOnStep(6);
		
		report.startStep("Check User Menu in Test");
		checkUserMenuLinks2(false, false, false, false);
				
		
	}
	
	@Test
	//@Category(UserData.class)
	@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "27262" }, testMultiple = true)
	public void testBasicStudyPlanner() throws Exception {
		
		NewUxStudyPlanner sPlanner = new NewUxStudyPlanner(webDriver,testResultService);
		
		report.startStep("Click on My Info btn and check Study Planner link");
		homePage.clickOnUserAvatar();
		sleep(1);
		homePage.checkTextAndclickOnStudyPlanner();
		homePage.switchToStudyPlanner();
		sPlanner.verifyStudyPlannerHeader();
		
		report.startStep("Create new Plan select Basic 1 and Next");
		sPlanner.createNewPlan();
		sPlanner.switchToStudyPlannerWizard();
		sPlanner.selectBasic1Level();
		sPlanner.clickOnNext();
		
		report.startStep("Fill Plan dates and calculate");
		sPlanner.switchToStudyPlannerWizard();
		String title = webDriver.waitForElement("/html/body/div/span", ByTypes.xpath).getText();
		testResultService.assertEquals(title, "Plan Calculator");

/*	in progress by David should be continue	
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String startDate = sdf.format(date).toString();
		
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, 14);
		String endDate = sdf.format(c.getTime()).toString();
		
		webDriver.waitForElement("//tr[@id='startd']//input[@id='calcArea']", ByTypes.xpath).click();
		webDriver.waitForElement("9",ByTypes.linkText).click();
		
		//int day = c.get(Calendar.DAY_OF_WEEK);
				
		
		//List<WebElement> days = webDriver.waitForElement("day", ByTypes.className).findElements(By.xpath("//div[@class='datepicker-days']/tr[@class='day']"));
		//days.get(2).click();
		
		
		
		
		WebElement a = webDriver.waitForElement("//tr[@id='startd']//input[@id='calcArea']", ByTypes.xpath);
		a.sendKeys(startDate);
		
		    
		webDriver.waitForElement("//tr[@id='endd']//input[@id='calcArea1']", ByTypes.xpath).sendKeys(endDate);
		
		webDriver.getWebDriver().findElement(By.name("calculate2")).click();
	*/	
		
		sPlanner.close();
		
		
	}
	
private void checkUserMenuLinks2(boolean isMyProfileEnabled, boolean isPlannerEnabled, boolean isWalkthroughEnabled, boolean checkStudentName) throws Exception {
		
		myProfile = new NewUxMyProfile(webDriver,testResultService);
		sPlanner = new NewUxStudyPlanner(webDriver,testResultService);
		learningArea = new NewUxLearningArea(webDriver, testResultService);
		
		if (checkStudentName){
		report.startStep("Check Student Name");
		testResultService.assertEquals("Hello " + userFirstName, homePage.getUserDataText(), "User FName not found");		
		}
		
		report.startStep("Click on My Info btn and check My Profile link");
		homePage.clickOnUserAvatar();
		sleep(3);
		
		if (isMyProfileEnabled) {
		homePage.checkTextAndclickOnMyProfile(1);
		myProfile.close();
		sleep(3);
		} else if (!isMyProfileEnabled) {
			learningArea.verifyMyProfileLinkIsDisabled();
			homePage.clickOnUserAvatar();
			sleep(1);
		}
				
		report.startStep("Click on My Info btn and check Study Planner link");
		homePage.clickOnUserAvatar();
		sleep(1);
		
		if (isPlannerEnabled) {
		homePage.checkTextAndclickOnStudyPlanner();
		homePage.switchToStudyPlanner();
		sleep(2);
		sPlanner.verifyStudyPlannerHeader();
		sPlanner.close();
		sleep(2);
		} else if (!isPlannerEnabled) {
			learningArea.verifyStudyPlannerLinkIsDisabled();
			homePage.clickOnUserAvatar();
			sleep(1);
		}
		
		
		report.startStep("Check Logout link");
		homePage.clickOnUserAvatar();
		sleep(2);
		homePage.checkTextandClickOnLogoutLink();
		sleep(2);
		homePage.cancelLogOut();
		sleep(2);
		report.startStep("Click on Help btn and check Walkthrough & Sys Check links");
		sleep(2);
		homePage.clickOnHelp();
		sleep(4);
	
		
		if (isWalkthroughEnabled) homePage.checkTextandClickOnWalkthroughLink(1, false);
		else if (!isWalkthroughEnabled) learningArea.verifyWalkthroughLinkIsDisabled();
		homePage.checkTextandClickOnSysCheck(2, true);
		sleep(2);
		homePage.closeModalPopUp();
		sleep(1);
				
		report.startStep("Click on Dictionary btn and check Dictionary open");
		String searchText = "table";
		homePage.clickOnDictionary();
		homePage.inputDictionaryTextandPressSearch(searchText);
		homePage.switchToDictionaryDialog();
		homePage.verifyDictionaryResultByInputText(searchText);
		webDriver.refresh();
		sleep(2);
		
		
	}
	
	private void checkUserMenuLinks(boolean isMyProfileEnabled, boolean isPlannerEnabled, boolean isWalkthroughEnabled, boolean checkStudentName) throws Exception {
		
		myProfile = new NewUxMyProfile(webDriver,testResultService);
		sPlanner = new NewUxStudyPlanner(webDriver,testResultService);
		learningArea = new NewUxLearningArea(webDriver, testResultService);
		
		if (checkStudentName){
		report.startStep("Check Student Name");
		testResultService.assertEquals("Hello " + userFirstName, homePage.getUserDataText(), "User FName not found");		
		}
		
		report.startStep("Click on My Info btn and check My Profile link");
		homePage.clickOnUserAvatar();
		sleep(3);
		
		if (isMyProfileEnabled) {
		homePage.checkTextAndclickOnMyProfile(1);
		myProfile.close();
		sleep(3);
		} else if (!isMyProfileEnabled) {
			learningArea.verifyMyProfileLinkIsDisabled();
			homePage.clickOnUserAvatar();
			sleep(1);
		}
				
		report.startStep("Click on My Info btn and check Study Planner link");
		homePage.clickOnUserAvatar();
		sleep(1);
		
		if (isPlannerEnabled) {
		homePage.checkTextAndclickOnStudyPlanner();
		homePage.switchToStudyPlanner();
		sleep(2);
		sPlanner.verifyStudyPlannerHeader();
		sPlanner.close();
		sleep(2);
		} else if (!isPlannerEnabled) {
			learningArea.verifyStudyPlannerLinkIsDisabled();
			homePage.clickOnUserAvatar();
			sleep(1);
		}
		
		
		report.startStep("Check Logout link");
		homePage.clickOnUserAvatar();
		sleep(2);
		homePage.checkTextandClickOnLogoutLink();
		sleep(2);
		homePage.cancelLogOut();
		sleep(2);
	
		report.startStep("Click on Help btn and check Walkthrough & Sys Check links");
		sleep(2);
		homePage.clickOnHelp();
		sleep(4);
		
		webDriver.refresh();
		homePage.clickOnHelp();
		sleep(2);
		
		if (isWalkthroughEnabled) homePage.checkTextandClickOnWalkthroughLink(1, false);
		else if (!isWalkthroughEnabled) learningArea.verifyWalkthroughLinkIsDisabled();
		homePage.checkTextandClickOnSysCheck(2, true);
		sleep(2);
		homePage.closeModalPopUp();
		sleep(1);
				
		report.startStep("Click on Dictionary btn and check Dictionary open");
		String searchText = "table";
		homePage.clickOnDictionary();
		homePage.inputDictionaryTextandPressSearch(searchText);
		homePage.switchToDictionaryDialog();
		homePage.verifyDictionaryResultByInputText(searchText);
		webDriver.refresh();
		sleep(2);
		
		
	}
	
	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

}



