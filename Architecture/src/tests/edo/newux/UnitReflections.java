package tests.edo.newux;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import Interfaces.TestCaseParams;
import pageObjects.edo.NewUxLearningArea2;
import pageObjects.edo.NewUxMyProgressPage;
import pageObjects.edo.NewUxUnitReflectionsPage;
import testCategories.reg1;

@Category(reg1.class)
public class UnitReflections extends BasicNewUxTest {
	
	NewUxMyProgressPage myProgress;
	NewUxUnitReflectionsPage unitReflections;
	NewUxLearningArea2 learningArea2;
	String reflectionSavedMessages = "Your reflections were saved successfully";
	
	@Before
	public void setup() throws Exception {
		super.setup();
		report.startStep("Get user and login");
		homePage = createUserAndLoginNewUXClass();
		homePage.closeAllNotifications();
		homePage.waitHomePageloadedFully();
	}
		
	@Test
	@TestCaseParams(testCaseID = { "76581" })
	public void UnitReflectionLinkViaMyProgress() throws Exception {
		
		try {
			
			String courseId = courses[0];
			report.startStep("Go to My Progress Page");
	        homePage.clickOnMyProgress();
	        myProgress = new NewUxMyProgressPage(webDriver,testResultService);
	        myProgress.waitUntilPageIsLoaded();
	        
	        report.startStep("Retrieve Random Unit Number");
	        int randUnitNum = myProgress.getRandomUnitNum();
	        String unitName = myProgress.getUnitName(randUnitNum);
	        String unitId = dbService.getUnitIdByNameAndCourse(unitName, courseId);
	        List<String> lessonIds = dbService.getLessonsIdsBySequnce(unitId);
	        
	        report.startStep("Click on Unit "+randUnitNum+" in My Progress Page");
	        myProgress.clickToOpenUnitLessonsProgress(randUnitNum);
	    	sleep(1);
	    	
	        report.startStep("Verify Unit Reflection Tolltip is 'Not Started' by Hovering over Unit reflection button");
	        myProgress.verifyUnitReflectionbuttonInTooltip("Unit Reflections Not Started");
	        sleep(1);
	        
	        report.startStep("Verify Unit Reflection Button Exists and Click on it");
	        myProgress.clickOnUnitReflectionBttnInMyProgress();
	        sleep(1);
        
	        // Initialize reflection page
        	unitReflections = new NewUxUnitReflectionsPage(webDriver,testResultService);
        	
        	report.startStep("Wait for Unit Reflection Page to be Loaded");
        	unitReflections.waitForUnitReflectionPageToBeLoaded();
      
	        report.startStep("Set Reflection Rate of 2 Values");
	    	learningArea2 = new NewUxLearningArea2(webDriver,testResultService);
	    	List<String> actualScores = unitReflections.setUnitReflection(2); // out of max row span

	    	report.startStep("Verify Success Alert Exists and Click OK");
	    	unitReflections.validateAlertMessage(reflectionSavedMessages);
	    	
	    	report.startStep("Retrieve lesson Ids and scores list");
	    	List<String[]> expectedLessonsAndScores = unitReflections.getLessonScoreList(lessonIds, actualScores);
	    	
	    	report.startStep("Retrieve User Reflection Id");
	    	String userReflectionId = dbService.getUserReflectionId(studentId);
	    	
	    	report.startStep("Retrieve User Reflection Data");
	    	List<String[]> reflectionsData = dbService.getUserReflectionData(studentId, userReflectionId);

	    	report.startStep("Validate the Number of Stored Records is Correct");
	    	unitReflections.validateNumOfStoredRecords(2, reflectionsData.size());
	    	
	    	report.startStep("Verify Data was Stored Correctly in DB- Unit Progress is NULL");
	    	List<String> userReflectionUnitProgress = unitReflections.getUnitProgressFromReflectionsData(reflectionsData);
	    	unitReflections.checkDbDataIsNull(userReflectionUnitProgress);
	    	
	    	report.startStep("Verify Data was Stored Correctly in DB- Time On Unit Seconds is NULL");
	    	List<String> userReflectionTimeOnUnitSeconds = unitReflections.getTimeOnUnitSecondsFromReflectionsData(reflectionsData);
	    	unitReflections.checkDbDataIsNull(userReflectionTimeOnUnitSeconds);
	    	
	    	report.startStep("Verify Data was Stored Correctly in DB- Component Test Average is NULL");
	    	List<String> userReflectionComponentTestAverage = unitReflections.getComponentTestAverageFromReflectionsData(reflectionsData);
	    	unitReflections.checkDbDataIsNull(userReflectionComponentTestAverage);

	    	report.startStep("Verify Data was Stored Correctly in DB- Lesson Ids And Score");
	    	List<String> userReflectionLessonIds = unitReflections.getLessonIdsFromReflectionsData(reflectionsData);
	    	List<String> userReflectionScore = unitReflections.getScoreFromReflectionsData(reflectionsData);
	    	unitReflections.validateScoresAndLessonsWereStoredCorrectly(userReflectionLessonIds, userReflectionScore, expectedLessonsAndScores);
	    	
	    	report.startStep("Verify Data was Stored Correctly in DB- Date");
	    	List<String> userReflectionCreateDateUTC = unitReflections.getDateFromReflectionsData(reflectionsData);
	    	unitReflections.checkDbDateIsCorrect(userReflectionCreateDateUTC);
	    	
	    	report.startStep("Verify Data was Stored Correctly in DB- Unit Id");
	    	List<String> userReflectionUnitId = unitReflections.getUnitIdsFromReflectionsData(reflectionsData);
	    	unitReflections.checkDbDataIsCorrectSingleValue(userReflectionUnitId, unitId);
	    	
    	 	report.startStep("Return to My Progress Page"); 
    	 	unitReflections.clickOnMyProgressReflectionLink();
    	 	
	        report.startStep("Verify Unit "+randUnitNum+" is Expanded");
    	 	myProgress.verifyUnitCourseIsExpand(coursesNames[0],randUnitNum);
    	 	
	        report.startStep("Verify Unit Reflection Tolltip is 'In Progress' by Hovering over Unit reflection button");
    	 	myProgress.verifyUnitReflectionbuttonInTooltip("Unit Reflections In Progress");
    	 
    	 	report.startStep("Click Unit Reflection Button");
    	 	myProgress.clickOnUnitReflectionBttnInMyProgress();
    	 	
    	 	report.startStep("Wait for Unit Reflection Page to be Loaded");
        	unitReflections.waitForUnitReflectionPageToBeLoaded();
        	
        	report.startStep("Set Reflection Rate of All Values");
    	 	int Rowcount = unitReflections.getUnitReflectionMaxrows();
    	 	actualScores = unitReflections.setUnitReflection(Rowcount);
    	 	
    	 	report.startStep("Verify Success Alert Exists and Click OK");
	    	unitReflections.validateAlertMessage(reflectionSavedMessages);
    	 	
	    	report.startStep("Retrieve lesson Ids and scores list");
	    	expectedLessonsAndScores = unitReflections.getLessonScoreList(lessonIds, actualScores);
	    	
	    	report.startStep("Retrieve User Reflection Id");
    	 	userReflectionId = dbService.getUserReflectionId(studentId);
    	 	
    	 	report.startStep("Retrieve User Reflection Data");
	    	reflectionsData = dbService.getUserReflectionData(studentId, userReflectionId);
    	 	
	    	report.startStep("Validate the Number of Stored Records is Correct");
	    	unitReflections.validateNumOfStoredRecords(Rowcount, reflectionsData.size());
	    	
    	 	report.startStep("Verify Data was Stored Correctly in DB- Unit Progress is NULL");
	    	userReflectionUnitProgress = unitReflections.getUnitProgressFromReflectionsData(reflectionsData);
	    	unitReflections.checkDbDataIsNull(userReflectionUnitProgress);
	    	
	    	report.startStep("Verify Data was Stored Correctly in DB- Time On Unit Seconds is NULL");
	    	userReflectionTimeOnUnitSeconds = unitReflections.getTimeOnUnitSecondsFromReflectionsData(reflectionsData);
	    	unitReflections.checkDbDataIsNull(userReflectionTimeOnUnitSeconds);
	    	
	    	report.startStep("Verify Data was Stored Correctly in DB- Component Test Average is NULL");
	    	userReflectionComponentTestAverage = unitReflections.getComponentTestAverageFromReflectionsData(reflectionsData);
	    	unitReflections.checkDbDataIsNull(userReflectionComponentTestAverage);

	    	report.startStep("Verify Data was Stored Correctly in DB- Lesson Ids And Score");
	    	userReflectionLessonIds = unitReflections.getLessonIdsFromReflectionsData(reflectionsData);
	    	userReflectionScore = unitReflections.getScoreFromReflectionsData(reflectionsData);
	    	unitReflections.validateScoresAndLessonsWereStoredCorrectly(userReflectionLessonIds, userReflectionScore, expectedLessonsAndScores);
	    	
	    	report.startStep("Verify Data was Stored Correctly in DB- Date");
	    	userReflectionCreateDateUTC = unitReflections.getDateFromReflectionsData(reflectionsData);
	    	unitReflections.checkDbDateIsCorrect(userReflectionCreateDateUTC);
	    	
	    	report.startStep("Verify Data was Stored Correctly in DB- Unit Id");
	    	userReflectionUnitId = unitReflections.getUnitIdsFromReflectionsData(reflectionsData);
	    	unitReflections.checkDbDataIsCorrectSingleValue(userReflectionUnitId, unitId);
	    
    	 	report.startStep("Return to My Progress Page"); 
    	 	unitReflections.clickOnMyProgressReflectionLink();
    	 	
	        report.startStep("Verify Unit "+randUnitNum+" is Expanded");
    	 	myProgress.verifyUnitCourseIsExpand(coursesNames[0],randUnitNum);
    	 	
	        report.startStep("Verify Unit Reflection Tolltip is 'Completed' by Hovering over Unit reflection button");
       	 	myProgress.verifyUnitReflectionbuttonInTooltip("Unit Reflections Completed");
 	 	
       	 	report.startStep("Log Out");
       	 	homePage.logOutOfED();	
		} catch(Exception e) {
			System.out.println(e);
		}
	}
	
	@Test
	@TestCaseParams(testCaseID = { "76826" , "76797" , "76960" })
	public void UnitReflectionLink() throws Exception {
		
		int lessonNum = 7;
		int unitSequence = 1;
		String courseName = coursesNames[1];
		String courseCode = courseCodes[1];
		String unitName = "Meet A Rock Star";
		String courseId = courses[1];//dbService.getCourseIdByName("First Discoveries");
    	String unitId = dbService.getUnitIdByNameAndCourse(unitName, courseId);
        List<String> lessonIds = dbService.getLessonsIdsBySequnce(unitId);
      
		report.startStep("Go to Lesson: "+lessonNum+" In Unit: " + unitSequence);
		homePage.navigateToCourseUnitLessonLA2(courseCodes, courseCode, unitSequence, lessonNum, false, courseName);
	
		// Initialize learning area page	
		learningArea2 = new NewUxLearningArea2(webDriver, testResultService);
		learningArea2.waitToLearningAreaLoaded();
		report.startStep("Submit Test Without Answers");
		learningArea2.submitTestWithoutAnswers(false);
		
		// Initialize reflection page
    	unitReflections = new NewUxUnitReflectionsPage (webDriver,testResultService);
    	
    	report.startStep("Wait for Unit Reflection Page to be Loaded");
    	unitReflections.waitForUnitReflectionPageToBeLoaded();
    	
        report.startStep("Set Reflection Rate of All Values");
        int Rowcount = unitReflections.getUnitReflectionMaxrows();
    	List<String> actualScores = unitReflections.setUnitReflection(Rowcount); // out of max row span
    	sleep(3);
    	
    	report.startStep("Verify Success Alert Exists and Click OK");
    	unitReflections.validateAlertMessage(reflectionSavedMessages);
    	sleep(3);
    	
    	report.startStep("Retrieve lesson Ids and scores list");
    	List<String[]> expectedLessonsAndScores = unitReflections.getLessonScoreList(lessonIds, actualScores);
    	
    	report.startStep("Retrieve User Unit Progress From DB");
    	learningArea2.clickOnNextButton();
    	learningArea2.clickOnBackButton();
    	List<String[]> unitProgressData = dbService.getUserUnitProgress(studentId, unitId);
    	
    	report.startStep("Validate User Unit Progress Is Correct");
    	String[] details = unitReflections.checkUnitProgressData(unitProgressData);
    	String progress = details[0];
    	String testAverage = details[1];
    	//sleep(8);
    	report.startStep("Retrieve User Reflection Id");
    	String userReflectionId = dbService.getUserReflectionId(studentId);
    	
    	report.startStep("Retrieve User Reflection Data");
    	List<String[]> reflectionsData = dbService.getUserReflectionData(studentId, userReflectionId);

    	report.startStep("Validate the Number of Stored Records is Correct");
    	unitReflections.validateNumOfStoredRecords(Rowcount, reflectionsData.size());
    	
    	report.startStep("Verify Data was Stored Correctly in DB- Unit Progress");
    	List<String> userReflectionUnitProgress = unitReflections.getUnitProgressFromReflectionsData(reflectionsData);
    	unitReflections.checkDbDataIsCorrectSingleValue(userReflectionUnitProgress, progress);
    	
    	report.startStep("Verify Data was Stored Correctly in DB- Time On Unit Seconds is Not NULL");
    	List<String> userReflectionTimeOnUnitSeconds = unitReflections.getTimeOnUnitSecondsFromReflectionsData(reflectionsData);
    	unitReflections.checkDbDataIsNotNull(userReflectionTimeOnUnitSeconds);
    	
    	report.startStep("Verify Data was Stored Correctly in DB- Component Test Average");
    	List<String> userReflectionComponentTestAverage = unitReflections.getComponentTestAverageFromReflectionsData(reflectionsData);
    	unitReflections.checkDbDataIsCorrectSingleValue(userReflectionComponentTestAverage, testAverage);
    	
    	report.startStep("Verify Data was Stored Correctly in DB- Lesson Ids And Score");
    	List<String> userReflectionLessonIds = unitReflections.getLessonIdsFromReflectionsData(reflectionsData);
    	List<String> userReflectionScore = unitReflections.getScoreFromReflectionsData(reflectionsData);
    	unitReflections.validateScoresAndLessonsWereStoredCorrectly(userReflectionLessonIds, userReflectionScore, expectedLessonsAndScores);
    	
    	report.startStep("Verify Data was Stored Correctly in DB- Date");
    	List<String> userReflectionCreateDateUTC = unitReflections.getDateFromReflectionsData(reflectionsData);
    	unitReflections.checkDbDateIsCorrect(userReflectionCreateDateUTC);
    	
    	report.startStep("Verify Data was Stored Correctly in DB- Unit Id");
    	List<String> userReflectionUnitId = unitReflections.getUnitIdsFromReflectionsData(reflectionsData);
    	unitReflections.checkDbDataIsCorrectSingleValue(userReflectionUnitId, unitId);
   
    	report.startStep("Log Out");
   	 	homePage.logOutOfED();
		
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

}
