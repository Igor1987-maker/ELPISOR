package tests.edo.newux;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

//import com.thoughtworks.selenium.webdriven.commands.Close;

import Enums.ByTypes;
import Enums.NavBarItems;
import pageObjects.edo.NewUXLoginPage;
import pageObjects.edo.NewUxAssessmentsPage;
import pageObjects.edo.NewUxMyProfile;
import testCategories.inProgressTests;
import testCategories.notInUse;
import testCategories.reg3;
import testCategories.edoNewUX.GeneralFeatures;
import Interfaces.TestCaseParams;

@Category({reg3.class,notInUse.class})
//@Category(notInUse.class)
public class AeonCustomizeSettings extends BasicNewUxTest {


	//String aeonClassName;
	
	
	@Before
	public void setup() throws Exception {	
		institutionName= institutionsName[4];
		super.setup();
	}

	@Test
	@TestCaseParams(testCaseID = { "32180" })
	public void testAssessmentsPltNotVisibleWithUpcoming() throws Exception {
		String aeonClassName = configuration.getProperty("classname.aeon");
		NewUxAssessmentsPage testsPage = new NewUxAssessmentsPage(webDriver,
				testResultService);

		report.startStep("Create user for the test");
		studentId = pageHelper.createUSerUsingSP(institutionId, aeonClassName);
		String userName = dbService.getUserNameById(studentId, institutionId);
		
		report.startStep("Login ED");
		NewUXLoginPage loginPage = new NewUXLoginPage(webDriver, testResultService);
		homePage = loginPage.loginAsStudent(userName, "12345");
		
		report.startStep("Skip Promo message and Walkthrough tour");        
		skipPromoMessageAndWalkthroughOnEnter();
		homePage.waitHomePageloadedFully();
	//	NewUxMyProfile myProfile = new NewUxMyProfile(webDriver, testResultService);
	//	myProfile.setEnglishLanguageInMyProfile();
		
    
		report.startStep("Check that navigation bar opened and Assessments btn enabled");
		testResultService.assertEquals(true, homePage.isNavBarOpen() && homePage.isNavBarItemEnabled("sitemenu__itemTests")
				,"Verifying Vavigation bar is open");
		
	
		report.startStep("Assign B1 Final Course Test to student for future time");
		String courseId = getCourseIdByCourseCode("B1");
		//pageHelper.assignAutomatedTestToStudent(studentId, courseId,3,1,0,2);
		pageHelper.assignAutomatedTestToStudetInTestEnvironment(studentId, courseId,3,1,0,2);
		sleep(2);
				
		report.startStep("Open Assessments");
		homePage.openAssessmentsPage(false);
		
		report.startStep("Verify Placement Test NOT displayed in Available section");
		testsPage.checkPLTNotDisplayedInSectionByTestName(false);
		
		report.startStep("Open Upcoming Tests section");
		testsPage.clickOnArrowToOpenSection("2");
		//sleep(2);
									
		report.startStep("Check Counter value in ALL sections");
		testsPage.checkItemsCounterBySection("1", "0");// Available 
		testsPage.checkItemsCounterBySection("2", "1");// Upcoming
		testsPage.checkItemsCounterBySection("3", "0");// Test Results
		
		report.startStep("Verify B1 Final Course Test displayed in Upcoming section");
		testsPage.checkTestDisplayedInSectionByTestName("Basic 1 Final Test", "2", "1");
		
		report.startStep("Verify Test Date displayed for B1 Final Course Test in Upcoming section");
		String expectedTestDate = pageHelper.getStartTestDateByStudent(studentId, 3);
		String actualTestDate = testsPage.getTestDateForUpcomingTests("1");
		testResultService.assertEquals(expectedTestDate, actualTestDate,"Verifying Test Date in Upcoming Section");
				
		report.startStep("Close Assessments");
		testsPage.close();
	}
	
	@Test
	@TestCaseParams(testCaseID = { "32204","32206" })
	public void testNavigationElementsNotVisible() throws Exception {

		//This test verify also "32203","32202","32193" but they are not define as Automated and Regression = Yes
		
		
		String aeonClassName = configuration.getProperty("classname.aeon");

		report.startStep("Create user for the test");
		//institutionId = aeonInstId;
		studentId = pageHelper.createUSerUsingSP(institutionId, aeonClassName);
		userName = dbService.getUserNameById(studentId, institutionId);
		
		report.startStep("Login ED");
		NewUXLoginPage loginPage = new NewUXLoginPage(webDriver, testResultService);
		homePage = loginPage.loginAsStudent(userName, "12345");
		homePage.waitHomePageloadedFully();
		homePage.closeAllNotifications();
		
		report.startStep("Skip Promo message and Walkthrough tour");        
		skipPromoMessageAndWalkthroughOnEnter();
		
		report.startStep("Check that navigation bar opened");
		homePage.isNavBarOpen();
		homePage.isNavBarItemEnabled("sitemenu__itemTests");
		
		report.startStep("Check the Grammar book tool not visible");
		homePage.checkIfNavBarItemDisplayed(false, NavBarItems.sitemenu__itemGrammarBook);
		
		report.startStep("Check the Messages tool not visible");
		homePage.checkIfNavBarItemDisplayed(false, NavBarItems.sitemenu__itemMessages);
		
		report.startStep("Check the My Assignments tool not visible");
		homePage.checkIfNavBarItemDisplayed(false, NavBarItems.sitemenu__itemMyAssignments);
		
		report.startStep("Enter to Learning Area");
		homePage.clickOnContinueButton();	
		sleep(1);
		
		report.startStep("Skip Promo message and Walkthrough tour");        
		skipPromoMessageAndWalkthroughOnEnter();
		
		homePage.clickToOpenNavigationBar();
	
		report.startStep("Check the Grammar book tool not visible in LA");
		homePage.checkIfNavBarItemDisplayed(false, NavBarItems.sitemenu__itemGrammarBook);
		
		report.startStep("Check the Messages tool not visible in LA");
		homePage.checkIfNavBarItemDisplayed(false, NavBarItems.sitemenu__itemMessages);
		
		report.startStep("Check the My Assignments tool not visible in LA");
		homePage.checkIfNavBarItemDisplayed(false, NavBarItems.sitemenu__itemMyAssignments);
	
		
	}

	@Test	
	@TestCaseParams(testCaseID = { "32179","32218" })
	public void testAssessmentsPltNotVisibleInAvailabe() throws Exception {
			
			NewUxAssessmentsPage testsPage = new NewUxAssessmentsPage(webDriver, testResultService);
			
			//String intitutionId = aeonInstId;
			String aeonClassName = configuration.getProperty("classname.aeon");
			String midTermtestNameED = "Basic 1 " + pageHelper.getImplementationTypeName(2,"ED");
			
			report.startStep("Create user for the test");
			studentId = pageHelper.createUSerUsingSP(institutionId, aeonClassName);
			String userName = dbService.getUserNameById(studentId, institutionId);
	
			report.startStep("Login ED");
			NewUXLoginPage loginPage = new NewUXLoginPage(webDriver, testResultService);
			homePage = loginPage.loginAsStudent(userName, "12345");
			homePage.waitHomePageloaded();
			
			report.startStep("Skip Promo message and Walkthrough tour");        
					
			skipPromoMessageAndWalkthroughOnEnter();
			report.startStep("Open Assessments");
			homePage.openAssessmentsPage(false);
			//sleep(2);
			
			//pageHelper.closeLastSessionImproperLogoutAlert();	
			
			report.startStep("Check Counter value in ALL sections");
			testsPage.checkItemsCounterBySection("1", "0");// Available 
			testsPage.checkItemsCounterBySection("2", "0");// Upcoming
			testsPage.checkItemsCounterBySection("3", "0");// Test Results
			
			report.startStep("Verify Placement Test NOT displayed in Available section");
			testsPage.checkPLTNotDisplayedInSectionByTestName(false);
								
			report.startStep("Close Assessments");
			testsPage.close();
				
			report.startStep("Assign "+midTermtestNameED+" to student");
			String courseId = getCourseIdByCourseCode("B1");
			//pageHelper.assignAutomatedTestToStudent(studentId, courseId,2,0,0,3);
			int endDateOffSet = pageHelper.getEndDateOffSetOfInstitution(institutionId);
			pageHelper.assignAutomatedTestToStudetInTestEnvironment(studentId, courseId,2,0,0,endDateOffSet);
			sleep(2);
			
			report.startStep("Open Assessments");
			homePage.openAssessmentsPage(false);
			
			sleep(1); //wait to test count will update
			report.startStep("Check Counter value in ALL sections");
			testsPage.checkItemsCounterBySection("1", "1");// Available 
			testsPage.checkItemsCounterBySection("2", "0");// Upcoming
			testsPage.checkItemsCounterBySection("3", "0");// Test Results
			
			report.startStep("Verify Placement Test NOT displayed in Available section");
			testsPage.checkPLTNotDisplayedInSectionByTestName(false);
			
			report.startStep("Verify " + midTermtestNameED + " displayed in Available section");
			testsPage.checkTestDisplayedInSectionByTestName(midTermtestNameED, "1", "1");
			
			
			report.startStep("Verify counter value for " + midTermtestNameED + " in Available section");
			String [] counterValues = pageHelper.getTestRemainingTimeByStudent(studentId, 2);
			int days = Integer.parseInt(counterValues[0]);
			
			if (days != 0) {	
				//sleep(30);
				testsPage.checkDaysInCounter(counterValues[0], "1");
			}
			
			testsPage.checkHoursInCounter(counterValues[1], "1");
			testsPage.checkMinutesInCounter(counterValues[2], "1");
	
			report.startStep("Close Assessments");
			testsPage.close();
		}
	
	
	private void skipPromoMessageAndWalkthroughOnEnter() throws Exception {
				
		homePage.closeModalPopUp();
		sleep(1);
		
		pageHelper.skipOnBoardingHP();
		sleep(1);
		
	}
	
	
	@After
	public void tearDown() throws Exception {
		institutionName="";
		super.tearDown();
	}
}