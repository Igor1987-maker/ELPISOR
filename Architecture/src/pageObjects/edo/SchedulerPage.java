package pageObjects.edo;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Enums.ByTypes;
import Enums.TaskTypes;
import drivers.GenericWebDriver;
import pageObjects.GenericPage;
import services.TestResultService;

public class SchedulerPage extends GenericPage{
	
	//@FindBy(xpath = "//div[contains(@class,'Unit2')]//div[@class='home__units_lessonsLocked home__units_lessonsLocked--locked']")
	//public WebElement unitLockedSign;
	
	@FindBy(xpath="//div[contains(@class,'progressPopover__InnerWrapper')]")
	public WebElement hoverElement;
	
	@FindBy(xpath="//div[@class='endOfUnitModal--progressW']")
	public WebElement messageLA;
	
	@FindBy(tagName="h1")
	public WebElement messageLAHeader;
		
	@FindBy(xpath="//div[@class='endOfUnitModal__progressCriteriaName endOfUnitModal__progressCriteria__td--inner']")
	public List<WebElement> messageLAConditions;
	
	@FindBy(xpath="//div[@class='endOfUnitModal__progressCriteriaMinReq']")
	public WebElement messageLARequierment;
	
	@FindBy(xpath="//div[contains(@class,'endOfUnitModal__progressCriteriaValue')]//div[1]")
	public WebElement messageLAConditionValue;
	
	NewUxHomePage homePage = new NewUxHomePage(webDriver, testResultService);
	
	public SchedulerPage(GenericWebDriver webDriver, TestResultService testResultService) throws Exception {
		super(webDriver, testResultService);
		// TODO Auto-generated constructor stub
		PageFactory.initElements(webDriver.getWebDriver(), this);
	}

	@Override
	public GenericPage waitForPageToLoad() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public GenericPage OpenPage(String url) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String getTextFromHoverMessageOnSpecificUnit(int unitNum) throws Exception {
		Thread.sleep(1500);
		NewUxHomePage homePage = new NewUxHomePage(webDriver, testResultService);
		
		WebElement unitLockedSign= homePage.getUnitLockedElement(unitNum, true);
		WebElement unitLocedSignParent = webDriver.waitForElement("home__detailsTitle-"+unitNum, ByTypes.id);
		//WebElement unitLocedSignParent = webDriver.waitForElement("//*[@id='home__unitIW_"+ (unitNum-1) +"']/div[1]/div[1]/div/div", ByTypes.xpath, false, 3);
		if (unitLocedSignParent != null) {
			//webDriver.scrollToElementWith_JS_Executor(unitLocedSignParent);
			webDriver.scrollToElement(unitLocedSignParent, 0);
			
			//webDriver.hoverOnElement(unitLocedSignParent);
		} else {
			testResultService.addFailTest("Unit Locked Sign Parent in Null", false, true);
		}

		//WebElement unitLockedSign = webDriver.waitForElement("//div[contains(@class,'Unit"+unitNum+"')]//div[@class='home__units_lessonsLocked home__units_lessonsLocked--locked']", ByTypes.xpath, false, 5);
		return webDriver.getTextFromHoverMessageByHoverElement(unitLockedSign,"//div[contains(@class,'progressPopover__InnerWrapper')]");
	}
	
	public void validateHoverMessageDisplaysCorrectDetails(String hoverMessage, String expectedMessage, String[] expectedCondition) throws Exception {
		String[] messagesLines = hoverMessage.split("\n");
		testResultService.assertEquals(messagesLines[0],expectedMessage,"Expected Message is Not Displayed Correctly.");
		testResultService.assertEquals(messagesLines[1],expectedCondition[0],"Expected Condition is Not Displayed Correctly.");
		testResultService.assertEquals(messagesLines[2],expectedCondition[1],"Expected Condition Value is Not Displayed Correctly.");
	}
	
	public void validateMessageOnLA(String expectedMessage, String[] expectedCondition, String expectedConditionValue) throws Exception {
		//boolean isMessageDisplayed = webDriver.isDisplayed(messageLA);
		WebElement msg = webDriver.waitForElement("//h1[@class='endOfUnitModal--progressDetailsTitle']", ByTypes.xpath, false, 10);
		
		if (!msg.getText().equals("")) {
			testResultService.assertEquals(expectedMessage, messageLAHeader.getText(),"Expected Message is Not Displayed Correctly.");
			testResultService.assertEquals(expectedCondition[0], messageLAConditions.get(0).getText(),"Expected Condition is Not Displayed Correctly.");
			testResultService.assertEquals(true, messageLARequierment.getText().contains(expectedCondition[1]),"Expected Condition Value is Not Displayed Correctly.");
			testResultService.assertEquals(expectedConditionValue,messageLAConditionValue.getText().trim(),"Expected Condition Value is Not " + expectedConditionValue);
		
			// close message
			webDriver.waitForElement("//a[@class='modal-close']", ByTypes.xpath, true, 4).click();
		} else {
			testResultService.addFailTest("Scheduler message: '"+expectedMessage+"' is not displayed.");
		}
	}
	
	public void validateDateMessageOnLA(String expectedMessage) throws Exception {
		//boolean isMessageDisplayed = webDriver.isDisplayed(messageLA);
		WebElement msg = webDriver.waitForElement("//h1[@class='endOfUnitModal--progressDetailsTitle']", ByTypes.xpath, false, 5);
		if (!msg.getText().equals("")) {
			testResultService.assertEquals(messageLAHeader.getText(),expectedMessage,"Expected Message is Not Displayed Correctly.");
			
			// close message
			webDriver.waitForElement("//a[@class='modal-close']", ByTypes.xpath, true, 4).click();
		} else {
			testResultService.addFailTest("Scheduler message: '"+expectedMessage+"' is not displayed.",false,true);
		}
	}
	
	public boolean checkSchedulerConditionOnHomePageAndEndOfUnit(String[] courseCodes, String courseName, String courseCode, int unitSequence, int lessonNum,
			String expectedMessage, String[] expectedCondition, String expectedConditionValue) throws Exception {
		
		//Thread.sleep(1000);
		report.startStep("Go to Course: " + courseName);
			homePage.navigateToRequiredCourseOnHomePage(courseName);
			homePage.waitHomePageloadedFully();
			
		// Check Scheduler Condition on Home Page
			boolean messageRetrieved = checkSchedulerConditionOnHomePage(unitSequence, expectedMessage, expectedCondition, false);
		
		report.startStep("Check Scheduler Condition on My Progress Page");
			checkUnitInMyProgressPage(unitSequence+1, true);
			
		// Only if a Message was Retrieved- we Check the Condition on End Of Unit
			if (messageRetrieved) {
				
				// Go to End Of Unit and Submit the Test Empty
					goToEndOfTest(courseCodes, courseCode, unitSequence, lessonNum, courseName,
							0, null);
	
				report.startStep("Validate Scheduler Message is Displayed At The End Of Test In Unit "+unitSequence+" Lesson 1");
					expectedMessage = "The next unit is locked.";
					validateMessageOnLA(expectedMessage, expectedCondition, expectedConditionValue);
					Thread.sleep(500);
			}
			return messageRetrieved;
	}
	
	public void validateUnitIsLocked(int unitSequence) throws Exception {

		 report.startStep("Validate Unit " +(unitSequence)+" is Locked");
		 Object lockedSignOnUnit = homePage.getUnitLockedElement(unitSequence, false);// sending 4 gets unit 4
		 testResultService.assertEquals(true, lockedSignOnUnit!=null,"Unit " +(unitSequence)+" is Not Locked");	
	}
	
	public void validateUnitIsNotLocked(int unitSequence) throws Exception {

		 report.startStep("Validate Unit " +(unitSequence)+" is Not Locked");
		 
		 WebElement lockedSignOnUnit = homePage.getUnitLockedElement(unitSequence, false);// sending 4 gets unit 4
		 
		 testResultService.assertEquals(true, lockedSignOnUnit==null,"Unit " +(unitSequence)+" is Still Locked");	 
	}
	
	public void goToEndOfTest(String[] courseCodes, String courseCode, int unitSequence,
			int lessonNum, String courseName, int numOfQuestionsToAnswers, String expectedSecondTestScore) throws Exception {
		
		report.startStep("Go to Lesson: "+lessonNum+" In Unit: " + unitSequence);
		NewUxLearningArea2 learningArea2 = homePage.navigateToCourseUnitLessonLA2(courseCodes, courseCode, unitSequence, lessonNum, false, courseName);
		
		// Initialize learning area page	
			//NewUxLearningArea2 learningArea2 = new NewUxLearningArea2(webDriver, testResultService);
			
			switch (numOfQuestionsToAnswers) {
				case 0:
					
					learningArea2.submitTestWithoutAnswers(true);
					break;
					
				case 1:
					
					report.startStep("Go to step Test");
				 	learningArea2.clickOnStep(3, false); 
				
				 	report.startStep("Click on Start Test");
				 	learningArea2.clickOnStartTest();
				
				 	report.startStep("Go To Task 3 and Answer It");
					learningArea2.openTaskBar();
					learningArea2.ClickOnTask(3); // new
					//learningArea2.clickOnTask(2);// failing
					NewUxDragAndDropSection2 dragAndDrop2 = new NewUxDragAndDropSection2(webDriver, testResultService);
					dragAndDrop2.dragAndDropAnswerByTextToTarget("every day", 1, TaskTypes.Close);
				
					report.startStep("Click Submit");
				 	learningArea2.submitTest(true);
				
				 	report.startStep("Validate The Score is " + expectedSecondTestScore);
				 	String actualScore = learningArea2.getTestScore();
				 	testResultService.assertEquals(expectedSecondTestScore, actualScore, "Test Score is Not Correct");
				
					break;
			}
	}
	
	
	
	public void goToEndOfUnitAndValidateSchedulerMsgIsNotDisplayed(String [] courseCodes, String courseCode, int unitSequence, int lessonNum, String courseName) throws Exception {
		// go to end of unit and validate scheduler message is not displayed
		goToEndOfTest(courseCodes, courseCode, unitSequence, lessonNum, courseName,
				0, null);
		
		validateSchedulerMsgNotDisplayedAndNextUnitOpened(unitSequence);
	}
	
	public void validateSchedulerMsgNotDisplayedAndNextUnitOpened(int unitSequence) throws Exception {
		report.startStep("Validate Scheduler Message is Not Displayed");
		boolean isNotDisplayed = validateMessageOnLAIsNotDisplayed();
	//	Thread.sleep(1000);
		
		if (isNotDisplayed) {
			report.startStep("Validate Next Unit is Opened");
			validateNextUnitIsOpened(unitSequence);
		} else { // msg is displayed
			testResultService.addFailTest("Scheduler Message is Displayed Even Though the Condition is Completed.");
		}
	}
	
	public void validateNextUnitIsOpened(int unitSequence) throws Exception {
		NewUxLearningArea2 learningArea2 = new NewUxLearningArea2(webDriver, testResultService);
		learningArea2.openLessonsList();
		String unitTitle = learningArea2.getUnitTitleInLessonList();
		String expectedUnit = Integer.toString(unitSequence+1);
		testResultService.assertEquals(true, unitTitle.contains(expectedUnit),"Unit " + expectedUnit +" Was Not Opened.");
		learningArea2.closeLessonsList();
	}
	
	public void validateSchedulerSuccessMsgDisplayedAndNextUnitOpened(int unitSequence, String[] expectedCondition, String expectedConditionValue) throws Exception {
		report.startStep("Validate Scheduler Message is Displayed");
		validateMessageOnLA("Well Done!", expectedCondition, expectedConditionValue);
		Thread.sleep(1000);
	
		report.startStep("Validate Next Unit is Opened");
		validateNextUnitIsOpened(unitSequence);
	}
	
	/*public void validateDateSchedulerSuccessMsgDisplayedAndNextUnitOpened(int unitSequence) throws Exception {
		report.startStep("Validate Scheduler Message is Displayed");
		validateDateMessageOnLA("Well Done!");
		Thread.sleep(1000);
	
		report.startStep("Validate Next Unit is Opened");
		validateNextUnitIsOpened(unitSequence);
	}*/
	
	public boolean validateMessageOnLAIsNotDisplayed() throws Exception {
		try {
			WebElement msg = webDriver.waitForElement("//h1[@class='endOfUnitModal--progressDetailsTitle']", ByTypes.xpath, false, 5);
		//	Thread.sleep(1000);
			boolean isDisplayed = webDriver.isDisplayed(msg);
			testResultService.assertEquals(false, isDisplayed,"Scheduler Message is Displayed at The End Of Unit Even Though The Condition Is Completed.");	
			return !isDisplayed;
		}catch (Exception e) {
			System.out.println(e);
			return false;
		}
	}
	
	public boolean checkSchedulerConditionOnHomePage(int unitSequence,String expectedMessage, String[] expectedCondition, boolean isDate) throws Exception {
		report.startStep("Retrieve meesage from hover progress");	
		String hoverText = getTextFromHoverMessageOnSpecificUnit(unitSequence+1);
		Thread.sleep(500);
	
		if (hoverText == null) {
			testResultService.addFailTest("Skipping Test. No Message Retrieved from Hover on Lock Sign.");
			return false;
		} else {
			if (isDate) {
				report.startStep("Validate Progress Message Of Date is Displayed Correctly");
				validateHoverMessageDisplaysCorrectDateDetails(hoverText, expectedMessage);
			} else {
				report.startStep("Validate Progress Message is Displayed Correctly");
				validateHoverMessageDisplaysCorrectDetails(hoverText, expectedMessage, expectedCondition);
			}
			return true;
		}
	}
	
	public void validateHoverMessageDisplaysCorrectDateDetails(String hoverMessage, String expectedMessage) throws Exception {
		String[] messagesLines = hoverMessage.split("\n");
		testResultService.assertEquals(messagesLines[0],expectedMessage,"Expected Message is Not Displayed Correctly.");
	}
	
	public void checkSchedulerConditionOnAssessmentsPage(String expectedMessage, String[] expectedCondition) throws Exception {
				
		WebElement unitLockedSign = webDriver.waitForElement("//td[@class='assessments__testDate']/ed-lock-icon", ByTypes.xpath,false, 5); 
		String hoverText = webDriver.getTextFromHoverMessageByClickAndHoldElement(unitLockedSign);
		
		if (hoverText == null) {
			testResultService.addFailTest("No Message was Retrieved from Hovering Lock Sign. Skipping test.");
		} else {
			report.startStep("Validate Progress Message is Displayed Correctly");
			validateHoverMessageDisplaysCorrectDetails(hoverText, expectedMessage, expectedCondition);
		}
		
		webDriver.hoverOnElement(webDriver.waitForElement("//div[contains(@class,'assessments__section upComingTests')]//tbody/tr/td[2]", ByTypes.xpath));
	}
	
	public void checkUnitInMyProgressPage(int unitNum, boolean checkUnitIsLocked) throws Exception {
		
		//webDriver.scrollToElementWith_JS_Executor(webDriver.waitForElement("carouselStartBtnW", ByTypes.className, 1,false, "My Progress button not found"));
		webDriver.scrollToElement(webDriver.waitForElement("carouselStartBtnW", ByTypes.className, 1,false, "My Progress button not found"));
		homePage.clickOnMyProgress();
		
		NewUxMyProgressPage myProgress = new NewUxMyProgressPage(webDriver, testResultService);
		myProgress.waitForPageToLoad();
		
		Thread.sleep(1000);
		
		// check unit is locked / not locked
		if (checkUnitIsLocked) {
			myProgress.validateUnitIsLocked(unitNum);
		} else {
			myProgress.validateUnitIsNotLocked(unitNum);
		}
	
		// return to home page
		myProgress.goToHomepageThroughMenu();
	}
}
