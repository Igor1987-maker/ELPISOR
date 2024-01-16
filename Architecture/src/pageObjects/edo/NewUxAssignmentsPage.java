package pageObjects.edo;

import org.apache.tools.ant.taskdefs.Sleep;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import Enums.ByTypes;
import Enums.WritingStatus;
import Enums.expectedConditions;
import drivers.GenericWebDriver;
import pageObjects.EdoHomePage;
import pageObjects.GenericPage;
import services.TestResultService;
import services.TextService;

public class NewUxAssignmentsPage extends GenericPage {

	public String mainWindowName = null;
		
	private static final String RATING1_TEXT = "Very Good!";

	private static final String RATING2_TEXT = "Good!";

	private static final String RATING3_TEXT = "Keep Working!";
	
	private static final String STATISTICS_INSTRUCTION_FIRST = "Your writing assignment has been evaluated. Click 'More Details' to review feedback. You can correct your writing on the right-hand panel and submit it for another evaluation.";
	
	private static final String STATISTICS_INSTRUCTION_SECOND = "Your writing assignment has been evaluated. Click 'More Details' to review feedback.";
		
	private static final String GENERAL_FEEDBACK_INSTRUCTION_FIRST = "Your writing assignment has been evaluated. You can correct your writing on the right-hand panel and submit it for another evaluation.";
	
	private static final String GENERAL_FEEDBACK_INSTRUCTION_SECOND = "Your writing assignment has been evaluated.";
	
	public NewUxAssignmentsPage(GenericWebDriver webDriver,
			TestResultService testResultService) throws Exception {
		super(webDriver, testResultService);
		// TODO Auto-generated constructor stub
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
	
	public void clickOnMyWritingsTab(boolean checkNotification) throws Exception {
		
		if (checkNotification) webDriver.waitForElement("//li[@id='liWriting']/div[@class='generalNotice']", ByTypes.xpath, "Notification sign (!) not displayed");
		//webDriver.waitForElement("spWriting", ByTypes.id).click();
		webDriver.clickOnElement(webDriver.waitForElement("spWriting", ByTypes.id));

	}
	
	public void selectCourseInMyWritings(String courseName, boolean checkNotification)	throws Exception {
		
		WebElement td = webDriver.getTableTdByName("//*[@id='myWritingTbl']", courseName);
		if (checkNotification) td.findElement(By.xpath("//following-sibling::td/div[2][@class='generalNotice']"));
		//td.click();
		webDriver.clickOnElement(td);
	}
	
	public void verifyWritingStatus(WritingStatus expStatus, String expDate, boolean expectNotification)	throws Exception {
		
		
		switch (expStatus) {
		
		case autoFeedback: 
			
			WebElement status = webDriver.waitForElement("//tr[@class='ui-subgrid']//tr[@id='s_1_1']/td[2]/div", ByTypes.xpath, "Writing status");
			String actStatus = status.getAttribute("class");
			webDriver.hoverOnElement(status);
			String actTooltip = webDriver.waitForElement("ttStatus", ByTypes.id, "Status tooltip").getText();
			
			WebElement link = webDriver.waitForElement("//tr[@class='ui-subgrid']//tr[@id='s_1_1']/td[4]", ByTypes.xpath, "Writing link");
			String actLink = link.getText();
					
			WebElement date = webDriver.waitForElement("//tr[@class='ui-subgrid']//tr[@id='s_1_1']/td[3]", ByTypes.xpath, "Writing date");
			String actDate = date.getText();
			
			testResultService.assertEquals("status3", actStatus, "Status is wrong");
			testResultService.assertEquals("Automated Feedback", actTooltip, "Status tooltip is wrong");
			testResultService.assertEquals("See feedback and try again", actLink, "Link is wrong");
			testResultService.assertEquals(expDate, actDate, "Date is wrong");
		
		break;
		
		case submitted: 
			
			status = webDriver.waitForElement("//tr[@class='ui-subgrid']//tr[@id='s_1_1']/td[2]/div", ByTypes.xpath, "Writing status");
			actStatus = status.getAttribute("class");
			webDriver.hoverOnElement(status);
			actTooltip = webDriver.waitForElement("ttStatus", ByTypes.id, "Status tooltip").getText();
			
			link = webDriver.waitForElement("//tr[@class='ui-subgrid']//tr[@id='s_1_1']/td[4]", ByTypes.xpath, "Writing link");
			actLink = link.getText();
					
			date = webDriver.waitForElement("//tr[@class='ui-subgrid']//tr[@id='s_1_1']/td[3]", ByTypes.xpath, "Writing date");
			actDate = date.getText();
			
			testResultService.assertEquals("status2", actStatus, "Status is wrong");
			testResultService.assertEquals("Submitted", actTooltip, "Status tooltip is wrong");
			testResultService.assertEquals("View", actLink, "Link is wrong");
			testResultService.assertEquals(expDate, actDate, "Date is wrong");
		
		break;
		
		
		case teacherFeedback: 
			
			status = webDriver.waitForElement("//tr[@class='ui-subgrid']//tr[@id='s_1_1']/td[2]/div", ByTypes.xpath, "Writing status");
			actStatus = status.getAttribute("class");
			webDriver.hoverOnElement(status);
			actTooltip = webDriver.waitForElement("ttStatus", ByTypes.id, "Status tooltip").getText();
			
			link = webDriver.waitForElement("//tr[@class='ui-subgrid']//tr[@id='s_1_1']/td[4]", ByTypes.xpath, "Writing link");
			actLink = link.getText();
					
			date = webDriver.waitForElement("//tr[@class='ui-subgrid']//tr[@id='s_1_1']/td[3]", ByTypes.xpath, "Writing date");
			actDate = date.getText();
			
			testResultService.assertEquals("status3", actStatus, "Status is wrong");
			testResultService.assertEquals("Automated Feedback", actTooltip, "Status tooltip is wrong");
			testResultService.assertEquals("See Feedback", actLink, "Link is wrong");
			testResultService.assertEquals(expDate, actDate, "Date is wrong");
		
		break;
		
		
		default: testResultService.addFailTest("Writing status undefined",false, false);
			
		
		}
			
		WebElement notice =	webDriver.waitForElement("//tr[@class='ui-subgrid']//tr[@id='s_1_1']/td[4]/div[@class='generalNotice']", ByTypes.xpath, 3, false,"Notification (!) not found");
		
		if (expectNotification && notice == null) testResultService.addFailTest("Notification (!) not found", false, true);
		else if (!expectNotification && notice != null) testResultService.addFailTest("Notification (!) displayed though should not", false, true);
				
		
	}
	
	public void clickOnSeeFeedbackTryAgainInMyWritings() throws Exception {
		
		webDriver.waitForElement("See feedback and try again", ByTypes.linkText).click();
		
	}
	
	public void clickOnViewInMyWritings() throws Exception {
		
		webDriver.waitForElement("View", ByTypes.linkText).click();
		
	}
	
	public void switchToFeedbackFrameInMyWritings() throws Exception {
		mainWindowName = webDriver.switchToFrame(webDriver.waitForElement(
				"cboxIframe", ByTypes.className));
		
	}
	
	public void clickOnFeedbackMoreDetails() throws Exception {
		
		webDriver.waitForElement("//div[@id='okButton']//a", ByTypes.xpath).click();
		
	}
	
	public void editFeedbackAssignmentText(String newText, boolean isOpenedInAssignmentsPage)
			throws Exception {
		
		webDriver.swithcToFrameAndSendKeys("//*[@id='tinymce']", newText,
				false, "elm1_ifr");
				
		webDriver.sleep(1);
		webDriver.switchToFrame("bsModal__iframe");
		if (isOpenedInAssignmentsPage) switchToFeedbackFrameInMyWritings();
			
	}

	public void clickOnFeedbackSubmitBtn() throws Exception {
		/*webDriver.switchToFrame("bsModal__iframe");
		switchToFeedbackFrameInMyWritings();*/
		webDriver.waitForElement("//div[@id='btSubmit']/a", ByTypes.xpath).click();
		Thread.sleep(3000);
		approveEraterPopup();
		Thread.sleep(2000);
		webDriver.closeAlertByAccept();
		Thread.sleep(2000);
		webDriver.switchToMainWindow();
		webDriver.switchToFrame("bsModal__iframe");
				
	}
	
	public void clickOnFeedbackSavedraftBtn() throws Exception {
		webDriver.waitForElement("elm1_save_", ByTypes.id).click();
		webDriver.closeAlertByAccept();
	}
	
	public void clickOnFeedbackSubmitInLearningArea() throws Exception {
		
		webDriver.waitForElement("//div[@id='btSubmit']/a", ByTypes.xpath).click();
		Thread.sleep(1000);
		}
	
	
	public void clickOnSeeFeedback() throws Exception {
		webDriver.waitForElement("See Feedback", ByTypes.linkText).click();
				
	}
	
	public void clickOnFeedbackPageMenu() throws Exception {
		
		webDriver.waitForElement("openMenuBT", ByTypes.className).click();
						
	}
	
	public void clickOnGeneralFeedbackInMenu() throws Exception {
		
		webDriver.waitForElement("General Feedback", ByTypes.linkText).click();
						
	}
	
	public void closeItemInMenu() throws Exception {
		
		webDriver.waitForElement("closeBtContent", ByTypes.className).click();
						
	}
	
	public void clickOnExploreTextInMenu() throws Exception {
		
		webDriver.waitForElement("Explore Text", ByTypes.linkText).click();
						
	}
	
	
	public String getExploreTextInMenu() throws Exception {
		
		return webDriver.waitForElement("//div[contains(@class,'menuItemContent')]", ByTypes.xpath).getText();
				
	}
	
	public void clickOnPrevVersionsInMenu() throws Exception {
		
		webDriver.waitForElement("Previous Versions", ByTypes.linkText).click();
						
	}
	
	public void verifyVersionsDetails(Boolean isFirstFeedback, String currentDate) throws Exception {
		
		if (isFirstFeedback) {
		
			String firstVersionDetails = webDriver.waitForElement("//ul[@id='versions']/li", ByTypes.xpath).getText();
			testResultService.assertEquals("Version #1   " + currentDate, firstVersionDetails.substring(0,23), "Version # 1 details not correct");
					
		} else {
			
			String firstVersionDetails = webDriver.waitForElement("//ul[@id='versions']/li", ByTypes.xpath).getText();
			testResultService.assertEquals("Version #2   " + currentDate, firstVersionDetails.substring(0,23), "Version #2 details not correct");
					
			String secondVersionDetails = webDriver.waitForElement("//ul[@id='versions']/li[2]", ByTypes.xpath).getText();
			testResultService.assertEquals("Version #1   " + currentDate, secondVersionDetails.substring(0,23), "Version #1 details not correct");
			
		}
		
	}
	
	public void clickOnPrinterVersionInMenu() throws Exception {
		
		webDriver.waitForElement("Printer Friendly Version", ByTypes.linkText).click();
						
	}
	
	public void validateStatisticsPopUp(String expectedNumOfWords, boolean isFirstFeedback) throws Exception {
		
		WebElement elementStat = webDriver.waitForElement("generalFeedbackFirst", ByTypes.className);
		Thread.sleep(2000);
		String actualInstruction = elementStat.findElement(By.className("eraterNote")).getText();
		String actualNumOfWord = elementStat.findElement(By.xpath("//span[1]")).getText();
		String actualHeader = elementStat.findElement(By.tagName("h3")).getText();
		
		if (!isFirstFeedback) actualHeader = elementStat.findElement(By.xpath("//h3[2]")).getText();
				
		
		testResultService.assertEquals("Statistics", actualHeader, "Header is not valid");
		testResultService.assertEquals(expectedNumOfWords, actualNumOfWord, "Words count does not match to learning area value on submit");
		
		if (isFirstFeedback) testResultService.assertEquals(STATISTICS_INSTRUCTION_FIRST, actualInstruction, "Instruction text is not valid");
		else testResultService.assertEquals(STATISTICS_INSTRUCTION_SECOND, actualInstruction, "Instruction text is not valid");		
	}
	
	public void validateStatisticsInGeneralFeedback(String expectedNumOfWords, boolean isFirstFeedback) throws Exception {
		
		WebElement elementStat = webDriver.waitForElement("menuItemContent", ByTypes.className);
		
		String actualInstruction = elementStat.findElement(By.className("eraterNote")).getText();
		String actualNumOfWord = elementStat.findElement(By.tagName("span")).getText();
		String actualHeader = elementStat.findElement(By.tagName("h3")).getText();
		
		if (!isFirstFeedback) actualHeader = elementStat.findElements(By.tagName("h3")).get(1).getText();
		
		testResultService.assertEquals("Statistics", actualHeader, "Header is not valid");
		testResultService.assertEquals(expectedNumOfWords, actualNumOfWord, "Words count does not match to learning area value on submit");
		
		if (isFirstFeedback) testResultService.assertEquals(GENERAL_FEEDBACK_INSTRUCTION_FIRST, actualInstruction, "Instruction text is not valid");
		else testResultService.assertEquals(GENERAL_FEEDBACK_INSTRUCTION_SECOND, actualInstruction, "Instruction text is not valid");
	}
	
	public void checkRatingFromTeacher(int rating, Boolean isOpenedFromMenu) throws Exception {
			
		String grade = "undefined";
		
		if (isOpenedFromMenu) grade = webDriver.waitForElement("//div[@class='menuItemContent']/div/div", ByTypes.xpath).getText();	
		else grade = webDriver.waitForElement("//div[@class='choosenGrade']", ByTypes.xpath).getText();

		switch (rating) {
		case 1:
			Assert.assertEquals(RATING1_TEXT, grade);
			break;
		case 2:
			Assert.assertEquals(RATING2_TEXT, grade);
			break;

		case 3:
			Assert.assertEquals(RATING3_TEXT, grade);
			break;

		}
				

	}
	
	public void close() throws Exception {
		
		WebElement Xbuton = webDriver.waitUntilElementAppearsAndReturnElementByType("//div[contains(@class,'lightBoxClose')]/a",ByTypes.xpath,10);
		webDriver.clickOnElement(Xbuton);
		webDriver.switchToMainWindow();
		

	}
		
	public String selectRecordingByCourseUnitAndLessonNameAndGetScore(String courseName, String unitName, String lessonName, int recordingNumber) throws Exception {

		//TextService textService = new TextService();
		
		webDriver.switchToMainWindow();
		webDriver.switchToTopMostFrame();
		webDriver.switchToFrame("bsModal__iframe");
		
		WebElement course = webDriver.waitForElement("//span[text()[contains(.,'"+courseName+"')]]", ByTypes.xpath, false, webDriver.getTimeout());// /preceding-sibling::span
		webDriver.clickOnElement(course);
		Thread.sleep(1000);
		WebElement unit = webDriver.waitForElement("//span[text()[contains(.,'"+unitName+"')]]", ByTypes.xpath, false, webDriver.getTimeout());
		webDriver.clickOnElement(unit);
		Thread.sleep(1000);
		WebElement lesson = webDriver.waitForElement("//li[text()[contains(.,'"+lessonName+"')]]", ByTypes.xpath, false, webDriver.getTimeout());
		webDriver.clickOnElement(lesson);//.findElement(By.xpath(".//span[1]")));
		Thread.sleep(1000);
		WebElement recRadioBtn = lesson.findElement(By.xpath(".//ul/div["+recordingNumber+"]/div/input"));
		webDriver.clickOnElement(recRadioBtn);
		String score = lesson.findElement(By.xpath(".//ul/div["+recordingNumber+"]/div[5]")).getText();
		
		return score;
	}
	
	public void clickOnPlay() throws Exception {
		
		WebElement button = webDriver.waitForElement("playImg",ByTypes.id, "Play btn of media player");
		webDriver.clickOnElement(button);
		

	}
		
	public void checkForTeacherCommentById(String commentID, String commentText) throws Exception {
		
			webDriver.waitForElement("//span[@class='"+commentID+"']", ByTypes.xpath)
					.click();
			String actualText = webDriver.waitForElement(
					"//div[@id='comments']//div[2]", ByTypes.xpath).getText();
			boolean commentOK = testResultService.assertEquals(commentText,
					actualText, "Teacher comment text not found or do not match");
			if (commentOK == false) {
				webDriver.printScreen("Comment do not match");
			}
		
	}
	
	public void closeWritingFeedbackPage() throws Exception {
		
		webDriver.waitForElement("//div[contains(@class,'lightBoxClose')]//a", ByTypes.xpath, "Close feedback page").click();
		
	}
	
	public String getERaterWordsCountOnSubmit() throws Exception {
		
		return webDriver.waitForElement("wordsNumber", ByTypes.className, "Words count not found").getText();
		
	}
	
	
	private void approveEraterPopup() throws Exception {
		
		// When running on main, following line should be in comment. When running on prod2, following line should not be in connemt  
		//webDriver.switchToFrame(webDriver.waitForElement("//div[@id='EdoFrameBoxContent']//iframe", ByTypes.xpath));
		webDriver.waitForElement("btnOk", ByTypes.id).click();

	}
	
}
