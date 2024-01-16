package tests.edo.newux;

import java.sql.Driver;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.apache.tools.ant.taskdefs.WaitFor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;

import Enums.ByTypes;
import pageObjects.edo.NewUxCommunityPage;
import pageObjects.edo.NewUxLearningArea;
import pageObjects.edo.NewUxLearningArea2;
import pageObjects.edo.NewUxMyProfile;
import pageObjects.edo.NewUxStudyPlanner;
import testCategories.AngularLearningArea;
import testCategories.inProgressTests;
import testCategories.reg1;
import testCategories.reg2;
import testCategories.edoNewUX.CourseArea;
import testCategories.edoNewUX.UserData;
import Interfaces.TestCaseParams;

@Category(AngularLearningArea.class)
public class UserMenuTests2 extends BasicNewUxTest {
	
	NewUxMyProfile myProfile;
	NewUxStudyPlanner sPlanner;
	NewUxLearningArea2 learningArea2;
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
	
	//test for SalesHP
	@Test
	@TestCaseParams(testCaseID = { "50799" }, testMultiple = true)
	public void testSalesHP() throws Exception{
		
		report.startStep("Init test data");
			List<WebElement> courseList;
			int courseToSelect = 5;
			
		report.startStep("Get Current Course");
			String currentCourse = homePage.getCurrentCourseName();
		
		report.startStep("Click on Course List");
			homePage.clickOnCourseList();
			sleep(1);
		
		report.startStep("Take currently selected course from list and compare");
			String selectedCourse = homePage.getCourseListSelectedCourse();
			testResultService.assertEquals(currentCourse,selectedCourse,"Selected course isn't visible");
		
		report.startStep("Navigate to different course");	
			courseList = homePage.getCourseListCourseELements();
			if (currentCourse.equals("Intermediate 2"))
				courseToSelect = 4;
			String targetCourse = courseList.get(courseToSelect).getText();
			courseList.get(courseToSelect).click(); 
			sleep(2);
		
		report.startStep("Make sure course was changed in both HP and course list");
			currentCourse = homePage.getCurrentCourseName();
			testResultService.assertEquals(currentCourse,targetCourse,"Move to selected course didn't happen");
			homePage.clickOnCourseList();
			sleep(2);
			selectedCourse = homePage.getCourseListSelectedCourse();
			testResultService.assertEquals(selectedCourse,targetCourse,"List highlight wasn't updated");
		
	}
	
	@Test
	@Category(UserData.class)
	@TestCaseParams(testCaseID = { "27355" , "37929"}, testMultiple = true)
	public void testUserMenuBasicFunction() throws Exception {
		
		report.startStep("Get User First Name");
		userFirstName = dbService.getUserFirstNameByUserId(studentId);
		
		report.startStep("Check User Menu on Home Page");
		checkUserMenuLinks(true, true, true, true,true,true);
		
		report.startStep("Navigate To My Progress Page");
		homePage.clickOnMyProgress();
				
		report.startStep("Check User Menu in My Progress");
		checkUserMenuLinks(true, true, false, true,false,true);
		
		report.startStep("Navigate To Community Page");
		homePage.clickToOpenNavigationBar();
		communityPage = homePage.openNewCommunityPage(false);
				
		report.startStep("Check User Menu in Community");
		checkUserMenuLinks(true, true, false, true,false,true);
		
		report.startStep("Navigate To Magazine Page");
		communityPage.openMagazinePage();	
		
		report.startStep("Check User Menu in Magazine Page");
		checkUserMenuLinks(true, true, false, true,false,true);
		
	//	report.startStep("Navigate To Idioms Page");
	//	homePage.openNewCommunityPage(false);
	//	communityPage.openIdiomsPage();
		
	//	report.startStep("Check User Menu in Idioms Page");
	//	checkUserMenuLinks(true, true, false, true,false,true);
		
	//	report.startStep("Navigate To Games Page");
	//	homePage.openNewCommunityPage(false);
	//	communityPage.openGamesPage();
		
	//	report.startStep("Check User Menu in Games Page");
	//	checkUserMenuLinks(true, true, false, true,false,true);
				
		report.startStep("Navigate To Learning Area");
		homePage.clickOnHomeButton();
		homePage.waitHomePageloaded();
		
		homePage.navigateToRequiredCourseOnHomePage(coursesNames[1]);
		sleep(3);
		homePage.clickOnUnitLessons(9);
		homePage.clickOnLesson(8, 1);
		sleep(10);
		
		report.startStep("Check User Menu in Learning Area");
		checkUserMenuLinks(false, false, true, false,false,false);
				
		report.startStep("Navigate To Test");
		learningArea2.clickOnStep(6, false);
		
		report.startStep("Check User Menu in Test");
		checkUserMenuLinks(false, false, false, false,false,false);
				
		
	}
	
	@Test
	@TestCaseParams(testCaseID = { "27791"})
	public void testOnBoardingUserActivation() throws Exception {
		
		
		report.startStep("Init test data");
			//webDriver.setSize(1500,900);
			String expectedStart = "To navigate through the program, use the links in the navigation bar.";
			String expectedEnd = "Click Walkthrough to see the tour again.";
		
		report.startStep("Launch On-Boarding tour");
			homePage.clickOnWalkthrough();
		
		report.startStep("Close On-Boarding via close button");	
			webDriver.waitForElement("//a[contains(@class, 'onBoarding__close')]", ByTypes.xpath).click();
		
		report.startStep("Launch On-Boarding tour");
			homePage.clickOnWalkthrough();	
			
		report.startStep("Check text of first bubble");
			String actualText = webDriver.waitForElement("//div[contains(@class, 'onBoarding__instrunctionsIW')]", ByTypes.xpath).getText();
			testResultService.assertEquals(expectedStart, actualText,"Wrong opening message");
			//String continueButtonText;
			
		report.startStep("Navigate to last bubble of On-Boarding");
		
		WebElement element = null;
		WebElement html = null;
		
		for (int i=1; i<11; i++){
			element = webDriver.waitForElement("//a[contains(@class, 'onBoarding__next')]", ByTypes.xpath,false,2);
		if (element != null)
				element.click();
		else{
			html = webDriver.findElementByXpath("/html", ByTypes.xpath);
			html.sendKeys(Keys.ARROW_RIGHT);
			i++;
			}
		}	

		report.startStep("Check text of last bubble");
			actualText = webDriver.waitForElement("//div[contains(@class, 'onBoarding__instrunctionsIW')]", ByTypes.xpath).getText();
			testResultService.assertEquals(expectedEnd, actualText,"Wrong opening message");
			
		report.startStep("Close On-Boarding");
			webDriver.waitForElement("//a[contains(@class, 'onBoarding__next')]", ByTypes.xpath).click();
		
		report.startStep("Make sure On-Boarding was closed");
				webDriver.checkElementNotExist("//div[contains(@class, 'onBoarding__instrunctionsIW')]");
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
	
	private void checkUserMenuLinks(boolean isMyProfileEnabled, boolean isPlannerEnabled, boolean isWalkthroughEnabled, boolean checkStudentName, boolean isNotificationEnabled,boolean isOnlineSessionEnabled) throws Exception {
		
		myProfile = new NewUxMyProfile(webDriver,testResultService);
		sPlanner = new NewUxStudyPlanner(webDriver,testResultService);
		learningArea2 = new NewUxLearningArea2(webDriver, testResultService);
		
		if (checkStudentName){
		report.startStep("Check Student Name");
		testResultService.assertEquals("Hello " + userFirstName, homePage.getUserDataText(), "User FName not found");		
		}
		
		report.startStep("Click on My Info btn and check My Profile link");
		homePage.clickOnUserAvatar();
		//sleep(2);
		
		if (isMyProfileEnabled) {
		homePage.checkTextAndclickOnMyProfile(1);
		myProfile.close();
		sleep(2);
		} else if (!isMyProfileEnabled) {
			learningArea2.verifyMyProfileLinkIsDisabled();
			homePage.clickOnUserAvatar();
			sleep(1);
		}
		
		report.startStep("Check Online Session and click on it");
		homePage.clickOnUserAvatar();
		sleep(1);
		if (isOnlineSessionEnabled){
			homePage.checkTextAndclickOnLineSession();
			//sleep(1);
		} else if (!isOnlineSessionEnabled){
			learningArea2.verifyOnlineSessionLinkIsDisabled();
			//homePage.clickOnUserAvatar();
			//sleep(1);
		}
		
		report.startStep("Check Notifications and click on it");
		homePage.clickOnUserAvatar();
		sleep(1);
		if (isNotificationEnabled){
			homePage.checkTextAndclickOnNotifications();
		} else if (!isNotificationEnabled){
			learningArea2.verifyNotificationsLinkIsDisabled();
			//homePage.clickOnUserAvatar();
		}
		
		report.startStep("Click on My Info btn and check Study Planner link");
			//webDriver.printScreen("Before opening user avatar menu "); // too many time process 
			homePage.clickOnUserAvatar();
			sleep(1);
		
		if (isPlannerEnabled) {
			homePage.checkTextAndclickOnStudyPlanner();
			homePage.switchToStudyPlanner();
			sPlanner.verifyStudyPlannerHeader();
			sPlanner.close();
			sleep(2);
		} else if (!isPlannerEnabled) {
			learningArea2.verifyStudyPlannerLinkIsDisabled();
			homePage.clickOnUserAvatar();
			//sleep(1);
		}
		
		
		report.startStep("Check Logout link");
		homePage.clickOnUserAvatar();
		sleep(1);
		homePage.checkTextandClickOnLogoutLink();
		homePage.cancelLogOut();
		sleep(1);
		
		report.startStep("Click on Help btn and check Walkthrough & Sys Check links");
		homePage.clickOnHelp();
		sleep(1);
		
		if (isWalkthroughEnabled) 
			homePage.checkTextandClickOnWalkthroughLink(1, false);
		else if (!isWalkthroughEnabled)
			learningArea2.verifyWalkthroughLinkIsDisabled();
		
		report.startStep("Click on Help btn and check Sys Check link");
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



