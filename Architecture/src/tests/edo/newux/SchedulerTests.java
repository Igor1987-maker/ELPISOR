package tests.edo.newux;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import Enums.CourseCodes;
import Interfaces.TestCaseParams;
import Objects.CourseTest;
import pageObjects.edo.NewUXLoginPage;
import pageObjects.edo.NewUxAssessmentsPage;
import pageObjects.edo.NewUxClassicTestPage;
import pageObjects.edo.NewUxLearningArea2;
import pageObjects.edo.SchedulerPage;
import pageObjects.edo.TestEnvironmentPage;
import testCategories.inProgressTests;

public class SchedulerTests extends BasicNewUxTest {
	String expectedMessage = "This unit is locked. This unit will be available once you've fullfilled the following requirement.";
	String courseId="";
	String testId="";
	
	@Before
	public void setup() throws Exception {
		super.setup();
		
		report.startStep("Create user and login");
			String className = configuration.getProperty("classname.scheduler");
			homePage = createUserAndLoginNewUXClass(className);
		
		/*String url = "https://webuxapp-ci-20201125-3.develop.com/ED2016#/login";
		report.startStep("Open ED2016");
		webDriver.openUrl(url);
		studentId = "52332170001838";
		report.startStep("Login as Student- shira10");
		loginAsStudent(studentId, "5233217");*/
		
		report.startStep("Wait until loading message dissappers (if its displayed)");
			homePage.waitUntilLoadingMessageIsOver();
			
		report.startStep("Close All Notifications");	
			homePage.closeAllNotifications();
			sleep(1);
		
		report.startStep("Close modal pop up (if it appears)");
			homePage.closeModalPopUp();
			sleep(1);
		
		report.startStep("Skip the Walkthrough and wait to home page loaded");
			pageHelper.skipOnBoardingHP();
			sleep(1);
			homePage.waitHomePageloadedFully();
	}
	
	
	@Test
	@TestCaseParams(testCaseID = { "77239" , "77238" , "72227" })
	public void testUnitBasedTOL_Condition() throws Exception{
		
		try {
			
			/*
			 * unitSequence- the Unit Before the Locked Unit
			 * lessonNum- the Last Lesson in the Unit Before the Locked Unit
			 * expectedConditionValue- the Expected Value After Completing the End Of Unit Test of the Previous Unit
			 */
			
			// Init test data
				String courseName = coursesNames[1];
				String courseCode = courseCodes[1];
				courseId = courses[1]; 
				int unitSequence = 1;
				int lessonNum = 7;
				String[] expectedCondition = new String[]{"Time Spent on Unit","1Hrs."};
				String expectedConditionValue = "00Hrs. 00Mins.";//"0Hrs.";
				
			// Initialize scheduler page
				SchedulerPage schedulerPage = new SchedulerPage(webDriver, testResultService);
			
			// Check Scheduler Condition On Home Page and End Of Unit
				boolean conditionAppears = schedulerPage.checkSchedulerConditionOnHomePageAndEndOfUnit(courseCodes, courseName, courseCode,
						unitSequence, lessonNum, expectedMessage, expectedCondition, expectedConditionValue);
				
			// If the Hover Message Exists- Continuing the test. else- Stopping
				 if (conditionAppears) {
					 
					// Initialize learning area page	
					 	NewUxLearningArea2 learningArea2 = new NewUxLearningArea2(webDriver, testResultService);
					 
					 report.startStep("Return to Home Page");
					 	learningArea2.clickOnHomeButton();
					 	homePage.waitHomePageloaded();
					 	
					 report.startStep("Go to Course: " + courseName);
					 	homePage.navigateToRequiredCourseOnHomePage(courseName);
				
					 report.startStep("Change Time In DB");
						 List<String[]> userTol = dbService.getUserLastRecordOfTimeOnLesson(courseId, studentId);
						 String startTime = textService.updateTime(userTol.get(0)[1], "reduce", "hour", 1);
						 dbService.updateStartTimeUserTimeOnLesson(startTime, userTol.get(0)[0], studentId, courseId);
						 webDriver.refresh();
						 homePage.waitHomePageloaded();
						 
					 // check unit is not locked
						schedulerPage.validateUnitIsNotLocked(unitSequence+1);
						
					report.startStep("Check Scheduler Condition on My Progress Page");
						schedulerPage.checkUnitInMyProgressPage(unitSequence, false);
					
					report.startStep("Go to End Of Unit and Validate Sceduler Message Is Not Displayed");
						schedulerPage.goToEndOfUnitAndValidateSchedulerMsgIsNotDisplayed(courseCodes, courseCode, unitSequence, lessonNum, courseName);
				 }
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			report.startStep("Log Out");
			homePage.clickOnLogOut();
		}
	}
	
	
	@Test
	@TestCaseParams(testCaseID = { "77382" , "72227" })
	public void testUnitBasedScore_Condition() throws Exception{
		try {
			/*
			 * unitSequence- the Unit Before the Locked Unit
			 * lessonNum- the Last Lesson in the Unit Before the Locked Unit
			 * expectedFirstTestScore- the Expected Value After Completing the End Of Unit Test of the Previous Unit
			 * expectedSecondTestScore- the Expected Score After Answering One Question Correctly
			 */
			
			// Init test data
			report.startStep("Init test data");
				String courseName = coursesNames[1];
				String courseCode = courseCodes[1];
				int unitSequence = 3;
				int lessonNum = 7;
				String[] expectedCondition = new String[]{"Average Unit Test Score","10%"};
				String expectedFirstTestScore = "0%"; 
				String expectedSecondTestScore = "20%"; 
				
			// Initialize scheduler page
				SchedulerPage schedulerPage = new SchedulerPage(webDriver, testResultService);
			
			// Check Scheduler Condition on Home Page and End Of Test
			report.startStep("Check Scheduler Condition on Home Page and End Of Test");
				boolean conditionAppears = schedulerPage.checkSchedulerConditionOnHomePageAndEndOfUnit(courseCodes, courseName, courseCode,
						unitSequence, lessonNum, expectedMessage, expectedCondition, expectedFirstTestScore);
				
			// If the Hover Message Exists- Continuing the test. else- Stopping
				 if (conditionAppears) {
					 
					 // Initialize learning area page	
					 	NewUxLearningArea2 learningArea2 = new NewUxLearningArea2(webDriver, testResultService);
					 
					 // go to home page
					 report.startStep("Return to Home Page");
					 	learningArea2.clickOnHomeButton();
					 	homePage.waitHomePageloadedFully();
					 	
					 report.startStep("Go to Course: " + courseName);
					 	homePage.navigateToRequiredCourseOnHomePage(courseName);
									 	
					 // Validate Unit is Locked
						 schedulerPage.validateUnitIsLocked(unitSequence+1); 
						 sleep(2);
						 
					 // Go to End Of Unit and answer one question
						 lessonNum = 1;
						 schedulerPage.goToEndOfTest(courseCodes, courseCode, unitSequence, lessonNum, courseName,
									1, expectedSecondTestScore);
						 
					 report.startStep("Return to Home Page");
					 	learningArea2.clickOnHomeButton();
					 	homePage.waitHomePageloadedFully();
					 	
					 report.startStep("Go to Course: " + courseName);
					 	homePage.navigateToRequiredCourseOnHomePage(courseName);
					
					 // Check unit is not locked  
						schedulerPage.validateUnitIsNotLocked(unitSequence+1);
						
					report.startStep("Check Scheduler Condition on My Progress Page");
						schedulerPage.checkUnitInMyProgressPage(unitSequence, false);
						
						lessonNum = 7;
					report.startStep("Go to End Of Unit and Validate Sceduler Message Is Not Displayed");
						schedulerPage.goToEndOfUnitAndValidateSchedulerMsgIsNotDisplayed(courseCodes, courseCode, unitSequence, lessonNum, courseName);
				 }
		} catch (Exception e) {
			System.out.println(e);
			testResultService.addFailTest("Test faile due problem",false,true);
		} finally {
			report.startStep("Log Out");
			homePage.clickOnLogOut();
		}
	}
	
	@Test
	@TestCaseParams(testCaseID = { "77383" , "72227" })
	public void testUnitBasedUnitCompletion_Condition() throws Exception{
		try {
			
			/*
			 * unitSequence- the Unit Before the Locked Unit
			 * lessonNum- the Last Lesson in the Unit Before the Locked Unit
			 * conditionInNum- the Condition in Number (Without % Sign)
			 * expectedConditionValue- the Expected Value After Completing the End Of Unit Test of the Previous Unit
			 */
			
			// Init test data
				String courseName = coursesNames[1];
				String courseCode = courseCodes[1];
				courseId = courses[1];
				int unitSequence = 5;
				int lessonNum = 6;
				String[] expectedCondition = new String[]{"Unit Completion","20%"};
				String expectedConditionValue = "6%"; 
				int conditionInNum = Integer.parseInt(expectedCondition[1].replace("%", ""));
								
			// Initialize scheduler page
				SchedulerPage schedulerPage = new SchedulerPage(webDriver, testResultService);
				
			// Check Scheduler Condition on Home Page and End Of Unit
				boolean conditionAppears = schedulerPage.checkSchedulerConditionOnHomePageAndEndOfUnit(courseCodes, courseName, courseCode,
						unitSequence, lessonNum, expectedMessage, expectedCondition, expectedConditionValue);
				
			// If the Hover Message Exists- Continuing the test. else- Stopping
				if (conditionAppears) {
					 
					 // Initialize learning area page	
					 	NewUxLearningArea2 learningArea2 = new NewUxLearningArea2(webDriver, testResultService);
					
					 report.startStep("Return to Home Page");
						 learningArea2.clickOnHomeButton();
						 homePage.waitHomePageloadedFully();
					 
					 report.startStep("Go to Course: " + courseName);
						homePage.navigateToRequiredCourseOnHomePage(courseName);
					 
					 report.startStep("Validate Progress On Unit " +unitSequence+ " is Less than "+conditionInNum+"% (The condition is not Fulfilled Yet).");
						 int progressOfUnit = homePage.getUnitCompletionFromProgressBar(unitSequence);
						 testResultService.assertEquals(true, progressOfUnit < conditionInNum, "Unit " + unitSequence + " progress is not less than 20%.");
					 
					 // Validate Unit is Locked
						 schedulerPage.validateUnitIsLocked(unitSequence+1); 
					//	 sleep(4);
						 
						 lessonNum = 2;
					 report.startStep("Go to Lesson: "+lessonNum+" In Unit: " + unitSequence);
						homePage.navigateToCourseUnitLessonLA2(courseCodes, courseCode, unitSequence, lessonNum, false, courseName);
					
					 report.startStep("Click Next");
					 	learningArea2.clickOnNextButton();
					 
					 report.startStep("Click On Hear All");
					 	learningArea2.clickOnHearAll();
						
					 report.startStep("Set Progress of First Lesson in Unit " + unitSequence);
						 studentService.setProgressInFirstComponentInUnit(unitSequence-1, studentId, courseId);
						 //webDriver.refresh();
						 //sleep(2);
						 
					 report.startStep("Return to Home Page");
						 learningArea2.clickOnHomeButton();
						 homePage.waitHomePageloadedFully();
					 
					 report.startStep("Go to Course: " + courseName);
						homePage.navigateToRequiredCourseOnHomePage(courseName);
					 					 
					 // Validate progress is more than conditionNum
					 report.startStep("Retrieve Progress of Unit "+unitSequence+" and Validate it is More Than "+conditionInNum+".");
						 progressOfUnit = homePage.getUnitCompletionFromProgressBar(unitSequence);
						 testResultService.assertEquals(true, progressOfUnit > conditionInNum,"Unit Progress is Not More than "+conditionInNum+". Progress:" + progressOfUnit);
					//	 sleep(2);
						 
					// Check unit is not locked 
						schedulerPage.validateUnitIsNotLocked(unitSequence+1);
						
					report.startStep("Check Scheduler Condition on My Progress Page");
						schedulerPage.checkUnitInMyProgressPage(unitSequence+1, false);
					 
					 	lessonNum = 6;
					 report.startStep("Go to End Of Unit and Validate Sceduler Message Is Not Displayed");
						schedulerPage.goToEndOfUnitAndValidateSchedulerMsgIsNotDisplayed(courseCodes, courseCode, unitSequence, lessonNum, courseName);	
				}
		} catch (Exception e) {
			System.out.println(e);
			testResultService.addFailTest("Test faile due problem");
		} finally {
			report.startStep("Log Out");
			homePage.clickOnLogOut();
		}
	}
	
	@Test
	@TestCaseParams(testCaseID = { "77383" , "72227" })
	public void testUnitBasedUnitCompletion_Condition_ByNext() throws Exception{
		try {
			
			/*
			 * unitSequence- the Unit Before the Locked Unit
			 * lessonNum- the Last Lesson in the Unit Before the Locked Unit
			 * conditionInNum- the Condition in Number (Without % Sign)
			 * expectedConditionValue- the Expected Value After Completing the End Of Unit Test of the Previous Unit
			 */
			
			// Init test data
				String courseName = coursesNames[1];
				String courseCode = courseCodes[1];
				courseId = courses[1];
				int unitSequence = 5;
				int lessonNum = 6;
				String[] expectedCondition = new String[]{"Unit Completion","20%"};
				String expectedConditionValue = "6%"; 
				int conditionInNum = Integer.parseInt(expectedCondition[1].replace("%", ""));
				homePage.waitHomePageloaded();
								
			// Initialize scheduler page
				SchedulerPage schedulerPage = new SchedulerPage(webDriver, testResultService);
				
			// Check Scheduler Condition on Home Page and End Of Unit
				boolean conditionAppears = schedulerPage.checkSchedulerConditionOnHomePageAndEndOfUnit(courseCodes, courseName, courseCode,
						unitSequence, lessonNum, expectedMessage, expectedCondition, expectedConditionValue);
				
			// If the Hover Message Exists- Continuing the test. else- Stopping
				if (conditionAppears) {
					 
					 // Initialize learning area page	
					 	NewUxLearningArea2 learningArea2 = new NewUxLearningArea2(webDriver, testResultService);
					
					 report.startStep("Return to Home Page");
						 learningArea2.clickOnHomeButton();
						 homePage.waitHomePageloaded();
					 
					 report.startStep("Go to Course: " + courseName);
						homePage.navigateToRequiredCourseOnHomePage(courseName);
						
						String unitName = homePage.getUnitNameByIndex(unitSequence);
					 
					 report.startStep("Validate Progress On Unit " +unitSequence+ " is Less than "+conditionInNum+"% (The condition is not Fulfilled Yet).");
						 int progressOfUnit = homePage.getUnitCompletionFromProgressBar(unitSequence);
						 testResultService.assertEquals(true, progressOfUnit < conditionInNum, "Unit " + unitSequence + " progress is not less than 20%.");
					 
					 // Validate Unit is Locked
						 schedulerPage.validateUnitIsLocked(unitSequence+1); 
						 sleep(4);
						 
						lessonNum = 2;
					 report.startStep("Go to Lesson: "+lessonNum+" In Unit: " + unitSequence);
						homePage.navigateToCourseUnitLessonLA2(courseCodes, courseCode, unitSequence, lessonNum, false, courseName);
					
					 report.startStep("Click Next");
					 	learningArea2.clickOnNextButton();
					 
					 report.startStep("Click On Hear All");
					 	learningArea2.clickOnHearAll();
					 	sleep(2);
						
					 report.startStep("Set Progress of First Lesson in Unit " + unitSequence);
					 	studentService.setUserUnitProgress(false);
					 	studentService.setProgressInFirstComponentInUnit(unitSequence-1, studentId, courseId);
						sleep(3);
									
					 report.startStep("Go to Lesson 6");
					 	lessonNum = 6;
					 	learningArea2.openLessonsList();
					 	learningArea2.clickOnLessonByNumber(lessonNum);
					 	sleep(3);
					 	
					 report.startStep("Retrieve Progress");
					 	String unitId = dbService.getUnitIdByNameAndCourse(unitName, courseId);
					 	expectedConditionValue = dbService.getUserUnitProgress(studentId, unitId).get(0)[3];
					 	expectedConditionValue = expectedConditionValue.split(".0")[0] + "%";
					 	sleep(3);
					 	
					 report.startStep("Submit Test Without Answers");
						learningArea2.submitTestWithoutAnswers(true);
						
					// Validate Scheduler Msg Is Not Displayed
						//schedulerPage.validateSchedulerMsgNotDisplayedAndNextUnitOpened(unitSequence);
						schedulerPage.validateSchedulerSuccessMsgDisplayedAndNextUnitOpened(unitSequence, expectedCondition, expectedConditionValue);
				}			
		} catch (Exception e) {
			System.out.println(e);
			testResultService.addFailTest("Test faile due problem");
		} finally {
			report.startStep("Log Out");
			homePage.clickOnLogOut();
		}
	}
	
	@Test
	@TestCaseParams(testCaseID = { "72588" , "72674" , "72225" , "72706" , "72589"})
	//@Category(inProgressTests.class)
	public void testDateAndFinalTest_Condition() throws Exception {
		
		// Init test data
			NewUxAssessmentsPage testsPage;
			String courseName = coursesNames[1];
			String courseCode = courseCodes[1]; 
			courseId = getCourseIdByCourseCode(courseCode);
			String lockedCourse = coursesNames[2];
			int firstUnitSequence = 9;
			int secondUnitSequence = 10;
			String dateToOpenUnit = "30/12/2030";
			expectedMessage = "This unit will be available on "+dateToOpenUnit+".";
			String availableTestName = coursesNames[1] + " " + pageHelper.getImplementationTypeName(2,"ED");//"Basic 1 Mid-Term Test";
			String lockedTestName = "Basic 1 Final Test";
			String[] expectedCondition = new String[]{"Midterm Test Score","5%"};
			String dateExpectedMessage = "This test will be available once you've fulfilled the following requirement:";
			int conditionInNum = Integer.parseInt(expectedCondition[1].replace("%", ""));
			//String userName= dbService.getUserNameById(studentId, institutionId);
			int lessonNum = 7;
			//sleep(4);
			
		report.startStep("Go to Course: " + courseName);
			homePage.navigateToRequiredCourseOnHomePage(courseName);
			homePage.waitHomePageloadedFully();
		// Initialize scheduler page
			SchedulerPage schedulerPage = new SchedulerPage(webDriver, testResultService);
	
		// Validate Unit is Locked (9)
			schedulerPage.validateUnitIsLocked(firstUnitSequence); 
		//	sleep(4);
		
		// Validate Unit is Locked (10)
			schedulerPage.validateUnitIsLocked(secondUnitSequence); 
		//	sleep(4);
			
		report.startStep("Validate Course: '"+lockedCourse+"' is Locked"); 
			homePage.validateCourseIsLocked(lockedCourse);
		//	sleep(4);
			
		report.startStep("Go to Course: " + courseName);
			homePage.navigateToRequiredCourseOnHomePage(courseName);
			
		// Check Scheduler Condition on Home Page (for units 9-10) - CHECK SENT NUMBERS ARE CORRECT	
			boolean messageRetrieved = schedulerPage.checkSchedulerConditionOnHomePage(firstUnitSequence-1, expectedMessage, null, true);
			schedulerPage.checkSchedulerConditionOnHomePage(secondUnitSequence-1, expectedMessage, null, true);
		
		// Initialize learning area page	
		 	NewUxLearningArea2 learningArea2 = new NewUxLearningArea2(webDriver, testResultService);

		// Only if a Message was Retrieved- we Check the Condition on End Of Unit
			if (messageRetrieved) {
				
				// Go to End Of Unit and Submit the Test Empty
					schedulerPage.goToEndOfTest(courseCodes, courseCode, firstUnitSequence-1, lessonNum, courseName, 0, null);
	
				report.startStep("Validate Scheduler Message is Displayed At The End Of Test In Unit "+(firstUnitSequence-1)+" Lesson " + lessonNum);
					expectedMessage = "The next unit is locked.";
					schedulerPage.validateDateMessageOnLA(expectedMessage);
					Thread.sleep(2000);
					
				report.startStep("Return to Home Page");
					learningArea2.clickOnHomeButton();
					homePage.waitHomePageloadedFully();
			}
			
		report.startStep("Get Current Date and Convert it to the Same Format as in the Json");
			String currentDate = pageHelper.getCurrentDateByFormat("yyyy-MM-dd");
			
		report.startStep("Convert Date to Open Unit to the Same Format as in the Json");
			String dateToOpenUnitInNewFormat = textService.convertDateToDifferentFormat(dateToOpenUnit, "dd/MM/yyyy", "yyyy-MM-dd"); // date in json: 2021-12-30T00:00:00+02:00

		report.startStep("Get Plan Json from DB");
			String planJson = dbService.getPlanJson(studentId);
			
		report.startStep("Replace the Date ("+dateToOpenUnitInNewFormat+") to the Current Date ("+currentDate+")");
			String newJson = planJson.replace(dateToOpenUnitInNewFormat, currentDate);
			
		report.startStep("run query to update the json in the DB");
			dbService.updateUserSchedulerPlanJson(newJson, studentId);
			webDriver.refresh();
			homePage.waitHomePageloadedFully();
			
		report.startStep("Go to Course: " + courseName);
			homePage.navigateToRequiredCourseOnHomePage(courseName);
			
		// Validate Unit is Not Locked (9)
			schedulerPage.validateUnitIsNotLocked(firstUnitSequence); 
			sleep(1);
		
		// Validate Unit is Not Locked (10)
			schedulerPage.validateUnitIsNotLocked(secondUnitSequence); 
			sleep(1);
		
		report.startStep("Open Assessments Page");
			testsPage = homePage.openAssessmentsPage(false);
			sleep(2);
			
		report.startStep("Validate Test: '"+availableTestName+"' is Displayed in Available Tests Section");
			boolean isTestAvailable = testsPage.checkIfTestIsDisplayedInAvailableTests(availableTestName);
			testResultService.assertEquals(true, isTestAvailable, "Test: '"+availableTestName+"' is Not Displayed in Available Tests Section.");

		report.startStep("Open Second Section");
			testsPage.clickOnArrowToOpenSection("2"); 
			sleep(1);
		
		report.startStep("Validate Test: '"+lockedTestName+"' is Displayed in Upcoming Tests Section");
			boolean isTestInUpcomingSection = testsPage.checkIfTestIsDisplayedInUpcomingTests(lockedTestName);
			testResultService.assertEquals(true, isTestInUpcomingSection, "Test: '"+lockedTestName+"' is Not Displayed in Upcoming Tests Section");
		
		report.startStep("Check Scheduler Condition on '"+lockedTestName+"' in Assessments page");
			schedulerPage.checkSchedulerConditionOnAssessmentsPage(dateExpectedMessage,expectedCondition);
		
		report.startStep("Submit Test: '" +availableTestName+ "' In Order to Complete the Condition");
			if (isTestAvailable) {
			
				report.startStep("Open First Section");
					testsPage.clickOnArrowToOpenSection("1"); 
				
				report.startStep("Start Test: '" + availableTestName + "'");
					testId = pageHelper.getAssignedTestIdForStudent(studentId,courseId, 2);
					pageHelper.checkNoDuplicateTestAssignmentInDB(studentId,courseId,2,"1");
					
				//@ToDo check the TestId is for Old TestId
					TestEnvironmentPage testEnvironmentPage = new TestEnvironmentPage(webDriver, testResultService);
					int testTypeId = 2;
					String teVersion = "0";
					report.startStep("Validate Assigned Test ID is in Relevant List from DB");
					testEnvironmentPage.validateTestRandomization(teVersion, courseId, testId, testTypeId);
				
					NewUxClassicTestPage classicTest = testsPage.clickOnStartTest("1", "2");
					webDriver.closeAlertByAccept();
					sleep(2);
					webDriver.switchToNewWindow();
					classicTest.switchToTestAreaIframe();
					classicTest.pressOnStartTest();
					sleep(3);
	
				report.startStep("Answer First Section");
					classicTest.SubmitFirstSectionWithCorrectAnswers(1,testId, false);
				
				report.startStep("Submit Remaining Sections");
					int sectionsToSubmit = Integer.parseInt(dbService.getNumberOfSectionsInTestForCourseByTestId(courseId,testId,2));
					for (int i = 0; i < sectionsToSubmit-1 ; i++) {
						classicTest.browseToLastSectionTask();
						classicTest.pressOnSubmitSection(true);
						report.startStep("Submited section: " + (i+2));
					}
			
				report.startStep("Get Score and Close Test");
					classicTest.switchToCompletionMessageFrame();
					sleep(1);
					String finalScore = classicTest.getFinalScore();
					webDriver.switchToTopMostFrame();
					classicTest.switchToTestAreaIframe();
					classicTest.closeCompletionMessageAlert();
					sleep(1);
					
					//String finalScore = classicTest.submitRemainingTestEmpty(sectionsToSubmit);
			
					if (Integer.parseInt(finalScore) > conditionInNum) {
						
						report.startStep("Open Assessments Page");
							testsPage = homePage.openAssessmentsPage(false);
							
						report.startStep("Validate Test: '"+lockedTestName+"' is Displayed in Available Tests Section After Fullfiling the Condition of " + expectedCondition[0] + ": " + expectedCondition[1]);
							isTestAvailable = testsPage.checkIfTestIsDisplayedInAvailableTests(lockedTestName);
							testResultService.assertEquals(true, isTestAvailable, "Test: '"+lockedTestName+"' is Not Displayed in Available Tests Section After Fullfiling the Condition of " + expectedCondition[0] + ": " + expectedCondition[1]);
						
						report.startStep("Start Test: '" + lockedTestName + "'");
							testsPage.clickOnStartTest("1", "2");
							webDriver.closeAlertByAccept();
							sleep(2);
							webDriver.switchToNewWindow();
							classicTest.switchToTestAreaIframe();
							classicTest.pressOnStartTest();
							/*Thread.sleep(4000);
							
						report.startStep("Exit Test: '" + lockedTestName + "'");
							webDriver.closeNewTab(2);
							Thread.sleep(2000);
							
							testId = pageHelper.getAssignedTestIdForStudent(studentId,courseId, 3);
							String newGrade = "10";
						report.startStep("Update Final Test Score To " +newGrade+ " In DB");
							dbService.updateGradeInFinalTestForSpecificUser(studentId, newGrade,testId,"3");
							dbService.updateDidTest(studentId, testId, "3", "1");
							dbService.updateCompletedTest(studentId, testId, "3", "1");
							webDriver.refresh();
							homePage.waitHomePageloaded();
							
						report.startStep("Log Out and Login");
							homePage.logOutOfED();
							NewUXLoginPage loginPage = new NewUXLoginPage(webDriver, testResultService);
							homePage = loginPage.loginAsStudent(userName, "12345");
							*/
								
							testTypeId = 3;
							
						// wait for element to load
							webDriver.waitUntilElementAppears("//div[@class='answersWrapper mcq']", 30);
							
						// get test id
							testId = pageHelper.getAssignedTestIdForStudent(studentId, courseId, testTypeId);
							
							pageHelper.checkNoDuplicateTestAssignmentInDB(studentId,courseId,2,"1");
							pageHelper.checkNoDuplicateTestAssignmentInDB(studentId,courseId, testTypeId,"1");
							
						//@ToDo check the TestId is for Old TestId
						report.startStep("Validate Assigned Test ID is in Relevant List from DB");
							testEnvironmentPage.validateTestRandomization(teVersion, courseId, testId, testTypeId);	
							
						report.startStep("Test Id: " + testId);
							
						report.startStep("Answer First Section");
							classicTest.SubmitFirstSectionWithCorrectAnswers(1,testId, false);
						
						report.startStep("Submit Remaining Sections");
							sectionsToSubmit = Integer.parseInt(dbService.getNumberOfSectionsInTestForCourseByTestId(courseId,testId,3));
							for (int i = 0; i < sectionsToSubmit-1 ; i++) {
								classicTest.browseToLastSectionTask();
								classicTest.pressOnSubmitSection(true);
								report.startStep("Submited section: " + (i+2));
							}
						
						report.startStep("Get Score and Close Test");
							classicTest.switchToCompletionMessageFrame();
							sleep(1);
							finalScore = classicTest.getFinalScore();
							webDriver.switchToTopMostFrame();
							classicTest.switchToTestAreaIframe();
							classicTest.closeCompletionMessageAlert();
							sleep(1);
							
							if (testId.equalsIgnoreCase("47845")) {
								testResultService.assertEquals("12", finalScore, "Final Test Score is Incorrect");
							} else {
								testResultService.assertEquals("14", finalScore, "Final Test Score is Incorrect");							
							}
						
						report.startStep("Validate next course (basic 2) is opened");
							//homePage.validateCourseIsNotLocked("Basic 2");
					
						report.startStep("Refresh");
							webDriver.refresh();
							homePage.waitHomePageloaded();
							
						report.startStep("Validate next course (basic 2) is opened");
							homePage.validateCourseIsNotLocked("Basic 2");
					} else {
						testResultService.addFailTest("final score is less than " + conditionInNum);
					}
				} else {
					testResultService.addFailTest("Test: '"+availableTestName+"' Does not exist. Can't Complete Condition for test: '" +lockedTestName +"'");
				}
			
		report.startStep("Log Out");
		//	homePage.clickOnLogOut();
	}
	
	@Test
	@TestCaseParams(testCaseID = { "72588" , "72674" , "72225" , "72706" , "72589"})
	//@Category(inProgressTests.class)
	public void testDateAndFinalTest_Condition_ByNext() throws Exception {
		
		// Init test data
			NewUxAssessmentsPage testsPage;
			String courseName = coursesNames[1];
			String courseCode = courseCodes[1]; 
			courseId = getCourseIdByCourseCode(courseCode);
			String lockedCourse = coursesNames[2];
			int firstUnitSequence = 9;
			int secondUnitSequence = 10;
			String dateToOpenUnit = "30/12/2030";
			expectedMessage = "This unit will be available on "+dateToOpenUnit+".";
			String availableTestName = coursesNames[1] + " " + pageHelper.getImplementationTypeName(2,"ED");//"Basic 1 Mid-Term Test";
			String lockedTestName = "Basic 1 Final Test";
			String[] expectedCondition = new String[]{"Midterm Test Score","10%"};
			String dateExpectedMessage = "This test will be available once you've fulfilled the following requirement:";
			int conditionInNum = Integer.parseInt(expectedCondition[1].replace("%", ""));
			String userName= dbService.getUserNameById(studentId, institutionId);
			int lessonNum = 7;
			
		report.startStep("Go to Course: " + courseName);
			homePage.navigateToRequiredCourseOnHomePage(courseName);
		
		// Initialize scheduler page
			SchedulerPage schedulerPage = new SchedulerPage(webDriver, testResultService);
	
		// Validate Unit is Locked (9)
			report.startStep("Validate unit is locked");
			schedulerPage.validateUnitIsLocked(firstUnitSequence); 
			sleep(2);
		
		// Validate Unit is Locked (10)
			report.startStep("Validate unit is locked");
			schedulerPage.validateUnitIsLocked(secondUnitSequence); 
			Thread.sleep(1000);
			
		report.startStep("Validate Course: '"+lockedCourse+"' is Locked"); 
			homePage.validateCourseIsLocked(lockedCourse);
			sleep(1);
			
		report.startStep("Go to Course: " + courseName);
			homePage.navigateToRequiredCourseOnHomePage(courseName);
			
		// Check Scheduler Condition on Home Page (for units 9-10) - CHECK SENT NUMBERS ARE CORRECT	
			report.startStep("Check Scheduler Condition on Home Page");
			boolean messageRetrieved = schedulerPage.checkSchedulerConditionOnHomePage(firstUnitSequence-1, expectedMessage, null, true);
			sleep(2);
			schedulerPage.checkSchedulerConditionOnHomePage(secondUnitSequence-1, expectedMessage, null, true);
		
		// Initialize learning area page	
		 	NewUxLearningArea2 learningArea2 = new NewUxLearningArea2(webDriver, testResultService);

		// Only if a Message was Retrieved- we Check the Condition on End Of Unit
			if (messageRetrieved) {
				
				// Go to End Of Unit and Submit the Test Empty
					schedulerPage.goToEndOfTest(courseCodes, courseCode, firstUnitSequence-1, lessonNum, courseName, 0, null);
	
				report.startStep("Validate Scheduler Message is Displayed At The End Of Test In Unit "+(firstUnitSequence-1)+" Lesson " + lessonNum);
					expectedMessage = "The next unit is locked.";
					schedulerPage.validateDateMessageOnLA(expectedMessage);
					Thread.sleep(1000);
					
				report.startStep("Return to Home Page");
					learningArea2.clickOnHomeButton();
					homePage.waitHomePageloadedFully();
			}
			
		report.startStep("Get Current Date and Convert it to the Same Format as in the Json");
			String currentDate = pageHelper.getCurrentDateByFormat("yyyy-MM-dd");
			
		report.startStep("Convert Date to Open Unit to the Same Format as in the Json");
			String dateToOpenUnitInNewFormat = textService.convertDateToDifferentFormat(dateToOpenUnit, "dd/MM/yyyy", "yyyy-MM-dd"); // date in json: 2021-12-30T00:00:00+02:00

		report.startStep("Get Plan Json from DB");
			String planJson = dbService.getPlanJson(studentId);
			
		report.startStep("Replace the Date ("+dateToOpenUnitInNewFormat+") to the Current Date ("+currentDate+")");
			String newJson = planJson.replace(dateToOpenUnitInNewFormat, currentDate);
			
		lessonNum = 2;
		report.startStep("Go to Lesson: "+lessonNum+" In Unit: " + firstUnitSequence);
			homePage.navigateToCourseUnitLessonLA2(courseCodes, courseCode, firstUnitSequence-1, lessonNum, false, courseName);
			
		report.startStep("Click Next");
		 	learningArea2.clickOnNextButton();
		 
		report.startStep("Click On Hear All");
		 	learningArea2.clickOnHearAll();
			
		report.startStep("Run Query to Update the JSON in the DB");
			dbService.updateUserSchedulerPlanJson(newJson, studentId);
	
		report.startStep("Go to Lesson 7");
		 	lessonNum = 7;
		 	learningArea2.openLessonsList();
		 	learningArea2.clickOnLessonByNumber(lessonNum);
		 	
		report.startStep("Submit Test Without Answers");
			learningArea2.submitTestWithoutAnswers(true);
			sleep(3);
				
		report.startStep("Validate Next Unit is Opened");
			schedulerPage.validateNextUnitIsOpened(firstUnitSequence-1);
			sleep(1);
			
		report.startStep("Return to Home Page");
		 learningArea2.clickOnHomeButton();
		 homePage.waitHomePageloadedFully();
			
		report.startStep("Go to Course: " + courseName);
			homePage.navigateToRequiredCourseOnHomePage(courseName);
			
		// Validate Unit is Not Locked (9)
			schedulerPage.validateUnitIsNotLocked(firstUnitSequence); 
			sleep(2);
		
		// Validate Unit is Not Locked (10)
			schedulerPage.validateUnitIsNotLocked(secondUnitSequence); 
			//sleep(1);
		
		report.startStep("Open Assessments Page");
			testsPage = homePage.openAssessmentsPage(false);
			
		report.startStep("Validate Test: '"+availableTestName+"' is Displayed in Available Tests Section");
			boolean isTestAvailable = testsPage.checkIfTestIsDisplayedInAvailableTests(availableTestName);
			testResultService.assertEquals(true, isTestAvailable, "Test: '"+availableTestName+"' is Not Displayed in Available Tests Section.");

		report.startStep("Open Second Section");
			testsPage.clickOnArrowToOpenSection("2"); 
			sleep(1);
		
		report.startStep("Validate Test: '"+lockedTestName+"' is Displayed in Upcoming Tests Section");
			boolean isTestInUpcomingSection = testsPage.checkIfTestIsDisplayedInUpcomingTests(lockedTestName);
			testResultService.assertEquals(true, isTestInUpcomingSection, "Test: '"+lockedTestName+"' is Not Displayed in Upcoming Tests Section");
		
		report.startStep("Check Scheduler Condition on '"+lockedTestName+"' in Assessments page");
			schedulerPage.checkSchedulerConditionOnAssessmentsPage(dateExpectedMessage,expectedCondition);
		
		report.startStep("Submit Test: '" +availableTestName+ "' In Order to Complete the Condition");
			if (isTestAvailable) {
			
				report.startStep("Open First Section");
					testsPage.clickOnArrowToOpenSection("1"); 
				
				int testTypeId = 2;
				
				report.startStep("Start Test: '" + availableTestName + "'");
					testId = pageHelper.getAssignedTestIdForStudent(studentId, courseId, testTypeId);
				
					pageHelper.checkNoDuplicateTestAssignmentInDB(studentId, courseId, testTypeId,"1");
				
				//@ToDo check the TestId is for Old TestId
					TestEnvironmentPage testEnvironmentPage = new TestEnvironmentPage(webDriver, testResultService);
					String teVersion = "0";
				report.startStep("Validate Assigned Test ID is in Relevant List from DB");
					testEnvironmentPage.validateTestRandomization(teVersion, courseId, testId, testTypeId);
				
				
				//Can be removed after deploy new courses
				//TestEnvironmentPage testEnvironmentPage = new TestEnvironmentPage(webDriver, testResultService);
				String notWantedTestId = "989017755";
				testId = testEnvironmentPage.checkAndAssignedTestId(testId, notWantedTestId, studentId, courseId, "2");
				
				pageHelper.checkNoDuplicateTestAssignmentInDB(studentId,courseId,2,"1");
				
				NewUxClassicTestPage classicTest = testsPage.clickOnStartTest("1", "2");
				webDriver.closeAlertByAccept();
				sleep(2);
				webDriver.switchToNewWindow();
				classicTest.switchToTestAreaIframe();
				classicTest.pressOnStartTest();
				classicTest.waitForTestPageLoaded();
	
				report.startStep("Answer First Section");
					classicTest.SubmitFirstSectionWithCorrectAnswers(1,testId, false);
				
				report.startStep("Submit Remaining Sections");
					int sectionsToSubmit = Integer.parseInt(dbService.getNumberOfSectionsInTestForCourseByTestId(courseId,testId,2));
					for (int i = 0; i < sectionsToSubmit-1 ; i++) {
						classicTest.browseToLastSectionTask();
						classicTest.pressOnSubmitSection(true);
						report.startStep("Submited section: " + (i+2));
					}
			
				report.startStep("Get Score and Close Test");
					classicTest.switchToCompletionMessageFrame();
					sleep(1);
					String finalScore = classicTest.getFinalScore();
					//webDriver.switchToTopMostFrame();
					//classicTest.switchToTestAreaIframe();
					classicTest.closeCompletionMessageAlert();
					sleep(1);
					
					//String finalScore = classicTest.submitRemainingTestEmpty(sectionsToSubmit);
				
					if (Integer.parseInt(finalScore) > conditionInNum) {
						
						report.startStep("Open Assessments Page");
							testsPage = homePage.openAssessmentsPage(false);
							
						report.startStep("Validate Test: '"+lockedTestName+"' is Displayed in Available Tests Section After Fullfiling the Condition of " + expectedCondition[0] + ": " + expectedCondition[1]);
							isTestAvailable = testsPage.checkIfTestIsDisplayedInAvailableTests(lockedTestName);
							testResultService.assertEquals(true, isTestAvailable, "Test: '"+lockedTestName+"' is Not Displayed in Available Tests Section After Fullfiling the Condition of " + expectedCondition[0] + ": " + expectedCondition[1]);
					
						// inserted here the Repeat
						int repeatTest=2;
						for (int iteration=1;iteration<=repeatTest;iteration++) {
						
							report.startStep("Start Test: '" + lockedTestName + "'");
								testsPage.clickOnStartTest("1", "2");
								webDriver.closeAlertByAccept();
								sleep(1);
								webDriver.switchToNewWindow();
								classicTest.switchToTestAreaIframe();
								classicTest.pressOnStartTest();
							//	Thread.sleep(4000);
							
						// wait for element to load
							//webDriver.waitUntilElementAppears("//div[@class='answersWrapper mcq']", 30);
							
								testTypeId = 3;
						// get test id
							testId = pageHelper.getAssignedTestIdForStudent(studentId, courseId, testTypeId);
							
							pageHelper.checkNoDuplicateTestAssignmentInDB(studentId,courseId,2,"1");
							pageHelper.checkNoDuplicateTestAssignmentInDB(studentId,courseId, testTypeId,"1");
							
					//@ToDo check the TestId is for Old TestId
						report.startStep("Validate Assigned Test ID is in Relevant List from DB");
						testEnvironmentPage.validateTestRandomization(teVersion, courseId, testId, testTypeId);
							
							
							report.startStep("Test Id: " + testId);
							
							if (iteration==1) {
								
								report.startStep("Submit Remaining Sections");
								sectionsToSubmit = Integer.parseInt(dbService.getNumberOfSectionsInTestForCourseByTestId(courseId,testId,3));
								for (int i = 0; i < sectionsToSubmit; i++) {
									classicTest.browseToLastSectionTask();
									classicTest.pressOnSubmitSection(true);
									report.startStep("Submited section: " + (i+2));
								}
								
							} else {
								
							report.startStep("Answer First Section");
								classicTest.SubmitFirstSectionWithCorrectAnswers(1,testId, false);
							
							report.startStep("Submit Remaining Sections");
								sectionsToSubmit = Integer.parseInt(dbService.getNumberOfSectionsInTestForCourseByTestId(courseId,testId,3));
								for (int i = 0; i < sectionsToSubmit-1 ; i++) {
									classicTest.browseToLastSectionTask();
									classicTest.pressOnSubmitSection(true);
									report.startStep("Submited section: " + (i+2));
								}
							}
							
							report.startStep("Get Score and Close Test");
								classicTest.switchToCompletionMessageFrame();
								sleep(1);
								finalScore = classicTest.getFinalScore();
								//webDriver.switchToTopMostFrame();
								//classicTest.switchToTestAreaIframe();
								
								if (iteration==1){
									testResultService.assertEquals("0", finalScore, "Final Test Score is Incorrect");
								} else {
									
									if (testId.equalsIgnoreCase("47845"))
										testResultService.assertEquals("12", finalScore, "Final Test Score is Incorrect");
									else
										testResultService.assertEquals("14", finalScore, "Final Test Score is Incorrect");
								}
								
								classicTest.closeCompletionMessageAlert();
								sleep(2);
								
								if (iteration==1){
									report.startStep("Open Assessments Page");
									testsPage = homePage.openAssessmentsPage(false);
									
									pageHelper.checkNoDuplicateTestAssignmentInDB(studentId,courseId,2,"1");
									pageHelper.checkNoDuplicateTestAssignmentInDB(studentId,courseId,3,"1");
									
								}
						}
							
							//report.startStep("Validate next course (basic 2) is opened"); // These lines in comment from 02/06/2021 due bug 78582. we can return them after the bug fix
							//homePage.validateCourseIsNotLocked("Basic 2");
					
						report.startStep("Refresh");
							webDriver.refresh();
							
						report.startStep("Validate next course (basic 2) is opened");
							homePage.validateCourseIsNotLocked("Basic 2");
					
							
						/*report.startStep("Exit Test: '" + lockedTestName + "'");
							webDriver.closeNewTab(2);
							Thread.sleep(2000);
							
							testId = pageHelper.getAssignedTestIdForStudent(studentId,courseId, 3);
							String newGrade = "10";
						report.startStep("Update Final Test Score To " +newGrade+ " In DB");
							dbService.updateGradeInFinalTestForSpecificUser(studentId, newGrade,testId,"3");
							dbService.updateDidTest(studentId, testId, "3", "1");
							dbService.updateCompletedTest(studentId, testId, "3", "1");
							webDriver.refresh();
							homePage.waitHomePageloaded();
							
						report.startStep("Log Out and Login");
							homePage.logOutOfED();
							NewUXLoginPage loginPage = new NewUXLoginPage(webDriver, testResultService);
							homePage = loginPage.loginAsStudent(userName, "12345");
							
						report.startStep("Validate next course (basic 2) is opened");
							homePage.validateCourseIsNotLocked("Basic 2");*/
					} else {
						testResultService.addFailTest("final score is less than " + conditionInNum);
					}
				} else {
					testResultService.addFailTest("Test: '"+availableTestName+"' Does not exist. Can't Complete Condition for test: '" +lockedTestName +"'");
				}
				
			//report.startStep("Log Out");
			//	homePage.clickOnLogOut();
	}
	
	@Test
	@Category(inProgressTests.class) // Test completed. developed completly. By design the move does not function as required
	@TestCaseParams(testCaseID = { "53120" })
	 public void testSchedulerMoveUserToClass() throws Exception {
		
		String courseName = coursesNames[1];
		
		report.startStep("Get Plan Id Before Moving Student to a New Class");
			String planId = dbService.getPlanIdOfUser(studentId);
		
		report.startStep("Log Out");
			homePage.logOutOfED();
		
		// Init Data
			//String institutionId =  configuration.getInstitutionId();
			String userName= dbService.getUserNameById(studentId, institutionId);
			String targetClassName = configuration.getProperty("classname.scheduler2");
			//String classId = dbService.getClassIdByName(className, institutionId);
		
		report.startStep("Move User to Class: " + targetClassName);
			dbService.moveUserToClass(institutionId, userName, targetClassName);
		
		report.startStep("Log In As the Same Student to the New Class");
			NewUXLoginPage loginPage = new NewUXLoginPage(webDriver, testResultService);
			homePage = loginPage.loginAsStudent(userName, "12345");
			//sleep(3);
			homePage.waitHomePageloaded();
		
		report.startStep("Get Plan Id After Moving Student to a New Class");
			String newPlanId = dbService.getPlanIdOfUser(studentId);
			sleep(3);
			
		report.startStep("Validate Plan Id Changed After Moving Student to a New Class");
			testResultService.assertEquals(false, planId.equals(newPlanId), "Plan Id Was Not Updated");
		
		report.startStep("Go to Course: " + courseName);
			homePage.navigateToRequiredCourseOnHomePage(courseName);
		
		// Initialize scheduler page
			SchedulerPage schedulerPage = new SchedulerPage(webDriver, testResultService);
			
		// Validate Unit is Not Locked (1)
			schedulerPage.validateUnitIsNotLocked(1); 
			sleep(4);
			
		// Validate Unit is Not Locked (2)
			schedulerPage.validateUnitIsNotLocked(2); 
			sleep(4);
			
		// Validate Unit is Locked (3) tol
			schedulerPage.validateUnitIsLocked(3); 
			sleep(4);
			
		// Validate Unit is Not Locked (4)
			schedulerPage.validateUnitIsNotLocked(4); 
			sleep(4);
			
		// Validate Unit is Locked (5) score
			schedulerPage.validateUnitIsLocked(5); 
			sleep(4);
			
		// Validate Unit is Locked (6) date
			schedulerPage.validateUnitIsLocked(6); 
			sleep(4);
			
		//report.startStep("Validate course (basic 2) is opened");
			//homePage.validateCourseIsNotLocked("Basic 2"); // currently waiting for max to update to plan so this course will be opened
			
		report.startStep("Log Out");
			homePage.clickOnLogOut();	
	 }
	
	@Test
	@Category(inProgressTests.class) // Test completed. developed completly. By design the move does not function as required
	@TestCaseParams(testCaseID = { "73505" })
	 public void testSchedulerMoveUserToClassWithoutPlan() throws Exception {
		
		String courseName = coursesNames[1];
		
		//report.startStep("Get Plan Id Before Moving Student to a New Class");
			//String planId = dbService.getPlanIdOfUser(studentId);
		
		report.startStep("Log Out");
			homePage.logOutOfED();
		
		// Init Data
			//String institutionId =  institutionId;
			String userName= dbService.getUserNameById(studentId, institutionId);
			String targetClassName = configuration.getProperty("classname");
			//String classId = dbService.getClassIdByName(className, institutionId);
		
		report.startStep("Move User to Class: " + targetClassName);
			dbService.moveUserToClass(institutionId, userName, targetClassName);
		
		report.startStep("Log In As the Same Student to the New Class");
			NewUXLoginPage loginPage = new NewUXLoginPage(webDriver, testResultService);
			homePage = loginPage.loginAsStudent(userName, "12345");
			//sleep(3);
			homePage.waitHomePageloaded();
		
		//report.startStep("Get Plan Id After Moving Student to a New Class");
			//String newPlanId = dbService.getPlanIdOfUser(studentId);
			//sleep(3);
			
		//report.startStep("Validate Plan Id Changed After Moving Student to a New Class");
			//testResultService.assertEquals(false, planId.equals(newPlanId), "Plan Id Was Not Updated");
		
		report.startStep("Go to Course: " + courseName);
			homePage.navigateToRequiredCourseOnHomePage(courseName);
		
		// Initialize scheduler page
			SchedulerPage schedulerPage = new SchedulerPage(webDriver, testResultService);
			
		// Validate Unit is Not Locked (1)
			schedulerPage.validateUnitIsNotLocked(1); 
			sleep(4);
			
		// Validate Unit is Not Locked (2)
			schedulerPage.validateUnitIsNotLocked(2); 
			sleep(4);
			
		// Validate Unit is Not Locked (3) 
			schedulerPage.validateUnitIsNotLocked(3); 
			sleep(4);
			
		// Validate Unit is Not Locked (4)
			schedulerPage.validateUnitIsNotLocked(4); 
			sleep(4);
			
		// Validate Unit is Not Locked (5) 
			schedulerPage.validateUnitIsNotLocked(5); 
			sleep(4);
			
		// Validate Unit is Not Locked (6) 
			schedulerPage.validateUnitIsNotLocked(6); 
			sleep(4);
			
		report.startStep("Validate course (basic 2) is opened");
			homePage.validateCourseIsNotLocked("Basic 2"); // currently waiting for max to update to plan so this course will be opened
			
		report.startStep("Log Out");
			homePage.clickOnLogOut();	
	 }
	
	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}
}
