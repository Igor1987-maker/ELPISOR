package pageObjects;

import java.nio.charset.Charset;

import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;

import Enums.ByTypes;
import Enums.SubComponentName;
import Objects.Course;
import pageObjects.edo.edoPlacementTestPage;
import pageObjects.tms.DashboardPage;
import pageObjects.tms.TmsHomePage;
import services.TestResultService;
import services.TextService;
import org.junit.Assert;

import drivers.GenericWebDriver;

public class EdoHomePage extends GenericPage {

	private static final String RATING1_TEXT = "Very Good!";

	private static final String RATING2_TEXT = "Good!";

	private static final String RATING3_TEXT = "Keep Working!";

	@Autowired
	TextService textService;

	public String mainWindowName = null;

	public EdoHomePage(GenericWebDriver webDriver,
			TestResultService testResultService) throws Exception {
		super(webDriver, testResultService);
		// TODO Auto-generated constructor stub
	}

	public EdoHomePage checkEraterNotice() throws Exception {
		Assert.assertTrue(webDriver.waitForElement("ERaterNotice", ByTypes.id)
				.isDisplayed());
		return this;
	}

	public EdoHomePage clickOnCourses() throws Exception {
		// webDriver.waitForElementAndClick("My Courses", ByTypes.linkText);
		// webDriver.waitForElement("//table[@id='navTable']//tbody//tr//td//a[text()='My Courses']",
		// ByTypes.xpath).click();
		String url = null;
		// if (webDriver.getSutUrl().contains("develop")) {
		url = webDriver.getSutUrl() + "Runtime/myPage.aspx";
		// } else {
		// url = webDriver.getSutUrl() + webDriver.getIntitutionName()
		// + "/Runtime/myPage.aspx";
		// }
		webDriver.openUrl(url);
		return this;
	}

	public EdoHomePage clickOnCourseByName(String courseName) throws Exception {
		try {
			report.report("Clicking on couse: " + courseName);
			webDriver.waitForElementAndClick(courseName, ByTypes.linkText);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this;
	}

	public EdoHomePage waitForCourseDetailsToBeDisplayed(String courseName)
			throws Exception {
		Assert.assertEquals(
				courseName,
				webDriver.waitForElement("//*[@id='mainAreaTD']/div/h1",
						ByTypes.xpath).getText());

		return this;
	}

	public EdoHomePage waitForUnitDetailsToBeDisplayed(String unitName)
			throws Exception {
		Assert.assertEquals(
				unitName,
				webDriver.waitForElement("//*[@id='mainAreaTD']/div/h1",
						ByTypes.xpath).getText());
		return this;
	}

	public EdoHomePage tempLogout() throws Exception {
		webDriver.waitForElement("//*[@id='navTable']/tbody/tr/td[4]/a",
				ByTypes.xpath).click();
		Thread.sleep(3000);
		webDriver.switchToNewWindow();
		// webDriver.closeAlertByAccept();
		webDriver.waitForElement("btnOk", ByTypes.id).click();
		return this;
	}

	public EdoHomePage clickOnCourseUnit(String unitName) throws Exception {
		webDriver.waitForElement(unitName, ByTypes.linkText).click();
		return this;

	}

	public EdoHomePage clickOntUnitComponent(String componentName,
			String componentType) throws Exception {
		webDriver.waitForElement(componentName, ByTypes.linkText).click();

		webDriver.waitForElement(componentType, ByTypes.linkText).click();
		return this;
	}

	public EdoHomePage ClickOnComponentsStage(int stageNumber) throws Exception {
		// stageNumber = stageNumber - 1;
		if (stageNumber > 5) {
			clickOnNextComponent(stageNumber);
		}
		// else{
		// clickOnNextComponent(4);
		// }
		stageNumber = stageNumber - 1;
		webDriver.waitForElement(
				"//ul[@class='ulTasks']//li[@ind='" + stageNumber + "']//a",
				ByTypes.xpath).click();

		return this;
	}

	public EdoHomePage clickOnNextComponent(int iterations) throws Exception {

		for (int i = 0; i < iterations; i++) {
			// webDriver.waitForElement("//a[@class='tasksBtnext']",
			// ByTypes.xpath)
			// .click();
			webDriver.waitForElementAndClick("//a[@class='tasksBtnext']",
					ByTypes.xpath);
			Thread.sleep(1500);
		}
		return this;
	}

	public EdoHomePage clickOnLastComponent(int iterations) throws Exception {

		for (int i = 0; i < iterations; i++) {
			webDriver
					.waitForElement("//a[@class='tasksBtprev']", ByTypes.xpath)
					.click();
			Thread.sleep(500);
		}
		return this;
	}

	public EdoHomePage registerStudent(String firstName, String LastName,
			String UserName, String Password, int classNumber) throws Exception {
		String mainFrame = webDriver.switchToFrame(1);

		webDriver.waitForElement("fe1", ByTypes.id).sendKeys(firstName);
		webDriver.waitForElement("fe2", ByTypes.id).sendKeys(LastName);
		webDriver.waitForElement("fe3", ByTypes.id).sendKeys(UserName);
		webDriver.waitForElement("fe4", ByTypes.id).sendKeys(Password);
		webDriver.waitForElement(
				"//input[@type='radio'][@value='_" + classNumber + "']",
				ByTypes.xpath).click();
		webDriver.waitForElement("/html/body/div[3]/div[2]/div/div[3]/a",
				ByTypes.xpath).click();

		String alertText = webDriver.getAlertText(60);
		Assert.assertTrue(alertText
				.contains("The user has been successfully registered."));
		return this;
	}

	public EdoHomePage AddWritingAssignmentWithOutSubmit(String text,
			TextService textService) throws Exception {
		// String assayText = textService.getTextFromFile(assayTextFileName,
		// Charset.defaultCharset());
		String assayText = text;
		// textService.setClipboardText(assayText);
		webDriver.swithcToFrameAndSendKeys("//body[@id='tinymce']", assayText,
				"elm1_ifr");

		// webDriver.waitForElement("elm1_ifr", ByTypes.id.toString());
		// String mainWindow = webDriver.switchToFrame("elm1_ifr");
		// WebElement textArea =
		// webDriver.waitForElement("//body[@id='tinymce']",
		// ByTypes.xpath);
		// webDriver.pasteTextFromClipboard(textArea);
		// webDriver.switchToMainWindow(mainWindow);
		return this;
	}

	public EdoHomePage submitWritingAssignment(String assayTextFileName,
			TextService textService) throws Exception {
		return submitWritingAssignment(assayTextFileName, textService, null);
	}

	public EdoHomePage submitWritingAssignment(String assayTextFileName,
			TextService textService, String timeStamp) throws Exception {
		String assayText = textService.getTextFromFile(assayTextFileName,Charset.defaultCharset());

		// System.out.println("Clipboard text is set to: " + assayText);
		// textService.setClipboardText(assayText);
		webDriver.swithcToFrameAndSendKeys("//body[@id='tinymce']", timeStamp
				+ assayText, "elm1_ifr");
		Thread.sleep(3000);
		// String mainWindow = webDriver.switchToFrame("elm1_ifr");
		// WebElement textArea =
		// webDriver.waitForElement("//body[@id='tinymce']",
		// ByTypes.xpath);
		// webDriver.pasteTextFromClipboard(textArea);
		// webDriver.switchToMainWindow(mainWindow);
		Thread.sleep(3000);
		// webDriver.waitForElement("//a[@title='Submit']",
		// ByTypes.xpath).click();
		// webDriver.closeAlertByAccept();
		// webDriver.closeAlertByAccept();
		clickOnSubmitAssignment();
		return this;
	}

	public EdoHomePage clickOnSubmitAssignment() throws Exception {
		webDriver.waitForElement("//a[@title='Submit']", ByTypes.xpath).click();
		Thread.sleep(3000);
		// webDriver.closeAlertByAccept();
		approveEraterPopup();
		Thread.sleep(3000);
		webDriver.closeAlertByAccept();
		Thread.sleep(3000);
		return this;
	}

	private void approveEraterPopup() throws Exception {
		webDriver.switchToFrame(webDriver.waitForElement(
				"//div[@id='EdoFrameBoxContent']//iframe", ByTypes.xpath));
		webDriver.waitForElement("btnOk", ByTypes.id).click();

	}

	public String getAssignmentTextFromEditor() throws Exception {
		String mainWindow = webDriver.switchToFrame("elm1_ifr");
		String text = webDriver.waitForElement("//body[@id='tinymce']",
				ByTypes.xpath).getText();
		webDriver.switchToMainWindow(mainWindow);
		return text;
	}

	public EdoHomePage checkEraterNoticeAppear() throws Exception {
		Assert.assertTrue(webDriver.waitForElement("ERaterNotice", ByTypes.id)
				.isDisplayed());
		return this;
	}

	public EdoHomePage clickOnMyAssignments() throws Exception {
		webDriver.waitForElementAndClick("myAssignments", ByTypes.id);
		return this;
	}

	public EdoHomePage switchToFrameByClassName(String className)
			throws Exception {
		mainWindowName = webDriver.switchToFrame(webDriver.waitForElement(
				className, ByTypes.className));
		return this;

	}

	public EdoHomePage switchToAssignmentsFrame() throws Exception {
		mainWindowName = webDriver.switchToFrame(webDriver.waitForElement(
				"cboxIframe", ByTypes.className));
		return this;

	}

	public EdoHomePage clickOnWritingAssignmentsTab(String courseName)
			throws Exception {
		webDriver.waitForElement("spWriting", ByTypes.id).click();
		WebElement td = webDriver.getTableTdByName("//*[@id='myWritingTbl']",
				courseName);
		td.click();
		// webDriver.waitForElement(courseName,
		// ByTypes.partialLinkText.toString()).click();
		// webDriver.waitForElement("View", ByTypes.linkText).click();
		return this;
	}

	public EdoHomePage clickToViewAssignment(String courseName)
			throws Exception {
		webDriver.waitForElement(courseName, ByTypes.partialLinkText).click();
		webDriver.waitForElement("View", ByTypes.linkText).click();
		return this;
	}

	@Override
	public GenericPage waitForPageToLoad() throws Exception {

		// int waitTimeOut = 20;
		// int elapsedTime = 0;
		// while (elapsedTime < waitTimeOut) {
		// try {
		// Boolean pageLoaded = webDriver.waitForElement("navTable",
		// ByTypes.id, 10, false).isDisplayed();
		// if (pageLoaded == true) {
		// break;
		// }
		// if (webDriver.waitForElement("cont", ByTypes.id).isDisplayed()) {
		// System.out.println("progress bar is displayed");
		// Thread.sleep(5000);
		// elapsedTime = elapsedTime + 5;
		// System.out.println("Slepping for 5 seconds");
		// continue;
		// }
		// } catch (Exception e) {
		// try {
		// System.out
		// .println("Catched exception in waitForPageToLoad");
		// logger.info("Checking if user is logged in already");
		//
		// // String userIsLogged = webDriver.waitForElement(
		// // "//div[@class='insTxt']", ByTypes.xpath, false,
		// // webDriver.getTimeout()).getText();
		// // if (userIsLogged
		// // .contains("This username is currently logged")) {
		// // Assert.fail("User was logged in already");
		// // }
		// System.out.println("trying to close popup");
		// webDriver.closeAlertByAccept();
		// break;
		// } catch (NoSuchWindowException no) {
		// System.out.println("Catched NoSuchWindowException");
		// }
		//
		// catch (Exception j) {
		// System.out.println("Catched exception jjjjj");
		// }
		//
		// continue;
		// }
		//
		// }

		// webDriver.waitForElement("//*[@id='mainAreaTD']", ByTypes.xpath,
		// "Page was not loaded");
	
		return this;
	}

	public GenericPage openTeachersCorner() throws Exception {
		return clickOnTeachersCorner(false);
	}

	public GenericPage clickOnTeachersCorner(boolean showDashboard)
			throws Exception {
		try {
//			webDriver.printScreen("OpeningTeachersCorner");
			webDriver.waitForElement("//a[@href='javascript:gotoTMS()']",
					ByTypes.xpath).click();
			
			Thread.sleep(2000);
			webDriver.switchToNewWindow();
			System.out.println("URL:" + webDriver.getUrl());
			webDriver.switchToFrame(0);
		} catch (UnhandledAlertException e) {
			// TODO Auto-generated catch block
			webDriver.closeAlertByAccept();
		}
		// webDriver.waitForElement("a5", ByTypes.id).click();
		// webDriver.waitForElement("//a[@href='../Report/writingAssignments.aspx']",
		// ByTypes.xpath.toString()).click();
		if (showDashboard) {
			return new DashboardPage(webDriver, testResultService);
		} else {
			return new TmsHomePage(webDriver, testResultService);
		}
	}

	public EdoHomePage addWritingAssignment(Course course, String textFile)
			throws Exception {
		clickOnCourses();
		// String courseName = "Basic 3 2012";
		clickOnCourseByName(course.getName());
		waitForCourseDetailsToBeDisplayed(course.getName());
		clickOnCourseUnit(course.getCourseUnits().get(0).getName());
		clickOntUnitComponent(course.getCourseUnits().get(0).getUnitComponent()
				.get(0).getName(), "Practice");
		ClickOnComponentsStage(Integer.valueOf((course.getCourseUnits().get(0)
				.getUnitComponent().get(0).getStageNumber())));
		Thread.sleep(10000);
		submitWritingAssignment(textFile, textService);
		return this;
	}

	public EdoHomePage addWritingAssignmentWithoutSubmitting(Course course,
			String text, TextService textService) throws Exception {
		clickOnCourses();
		// String courseName = "Basic 3 2012";
		clickOnCourseByName(course.getName());
		// waitForCourseDetailsToBeDisplayed(course.getName());
		clickOnCourseUnit(course.getCourseUnits().get(0).getName());
		clickOntUnitComponent(course.getCourseUnits().get(0).getUnitComponent()
				.get(0).getName(), "Practice");
		Thread.sleep(5000);
		ClickOnComponentsStage(Integer.valueOf((course.getCourseUnits().get(0)
				.getUnitComponent().get(0).getStageNumber())));

		AddWritingAssignmentWithOutSubmit(text, textService);

		return this;
	}

	public EdoHomePage clickToSaveAssignment() throws Exception {
		WebElement saveBtn = webDriver.waitForElement("elm1_save_", ByTypes.id);
		saveBtn.click();
		String alertText = webDriver.getAlertText(10);
		// Assert.assertEquals("Your draft has been saved.", alertText);
		testResultService.assertEquals("Your draft has been saved.", alertText,
				"Alert was not displayed");
		webDriver.closeAlertByAccept();
		webDriver
				.waitForElement(
						"//a[@id='elm1_save_']//span[@class='mceIcon mce_save_ disable']",
						ByTypes.xpath);
		return this;
	}

	@Override
	public GenericPage OpenPage(String url) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public EdoHomePage clickOnSeeFeedback() throws Exception {
		webDriver.waitForElement("See Feedback", ByTypes.linkText).click();
		;
		return this;
	}

	public EdoHomePage clickOnSeeFeedbackAndTryAgsin() throws Exception {
		webDriver
				.waitForElement("See feedback and try again", ByTypes.linkText)
				.click();
		;
		return this;
	}

	public EdoHomePage clickOnFeedbackMoreDetails() throws Exception {
		webDriver.waitForElement("//div[@id='okButton']//a", ByTypes.xpath)
				.click();
		;
		return this;

	}

	public EdoHomePage editFeedbackAssignmentText(String newText)
			throws Exception {
		webDriver.switchToMainWindow(mainWindowName);
		webDriver.switchToFrame(webDriver.waitForElement(
				"//div[@id='cboxLoadedContent']//iframe", ByTypes.xpath));
		webDriver.switchToFrame(webDriver.waitForElement(
				"//div[@id='cboxLoadedContent']//iframe", ByTypes.xpath));
		webDriver.swithcToFrameAndSendKeys("//*[@id='tinymce']", newText,
				false, "elm1_ifr");
		return this;

	}

	public EdoHomePage clickOnFeedbackSubmitBtn() throws Exception {
		webDriver.switchToFrame(webDriver.waitForElement(
				"//div[@id='cboxLoadedContent']//iframe", ByTypes.xpath));
		webDriver.switchToFrame(webDriver.waitForElement(
				"//div[@id='cboxLoadedContent']//iframe", ByTypes.xpath));

		webDriver.waitForElement("//div[@id='btSubmit']/a", ByTypes.xpath)
				.click();
		Thread.sleep(3000);
		approveEraterPopup();
		// webDriver.closeAlertByAccept();
		Thread.sleep(3000);
		// webDriver.switchToAlert();
		// webDriver.sendKey(Keys.ENTER);
		webDriver.closeAlertByAccept();
		Thread.sleep(5000);
		return this;
	}

	public EdoHomePage checkRatingFromTeacher(int rating) throws Exception {
		// String mainWin =
		// webDriver.switchToFrame(webDriver.waitForElement("//iframe[class='cboxIframe']",
		// ByTypes.xpath));
		// webDriver.switchToFrame(webDriver.waitForElement("//iframe[class='cboxIframe']",
		// ByTypes.xpath));
		// webDriver.switchToFrame(webDriver.waitForElement(
		// "cboxIframe", ByTypes.className.toString()));
		report.report("Rating is: " + rating);
		String grade = webDriver.waitForElement("//div[@class='choosenGrade']",
				ByTypes.xpath).getText();

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
		// webDriver.switchToMainWindow(mainWin);
		return this;

	}

	public EdoHomePage clickOnPlayButton() throws Exception {
		webDriver.waitForElementAndClick("CTrackerPlayBtn", ByTypes.id);
		return this;

	}

	public EdoHomePage clickOnSeeExplanation() throws Exception {
		// webDriver.waitForElementAndClick("seeExplanation", ByTypes.id);
		webDriver.waitForElement("seeExplanation", ByTypes.id).click();
		return this;

	}

	public EdoHomePage checkExplanationPopupOpens() throws Exception {

		String mainWin = webDriver.switchToNewWindow();

		// webDriver.waitForElement("See Explanation",
		// ByTypes.partialLinkText.toString()).isDisplayed();
		webDriver.waitForElement("PopupTitle", ByTypes.className);
		org.junit.Assert.assertEquals("See Explanation", webDriver
				.waitForElement("PopupTitle", ByTypes.className).getText());
		webDriver.waitForElement("Close Window", ByTypes.linkText).click();
		;
		webDriver.switchToMainWindow(mainWin);
		return this;
	}

	public EdoHomePage clickOnSeeText() throws Exception {
		webDriver.waitForElement("SeeText", ByTypes.id).click();
		;
		return this;
	}

	public EdoHomePage checkSeeTextPopupOpens() throws Exception {
		String mainWin = webDriver.switchToPopup();
		// webDriver.waitForElement("PopupTitle",ByTypes.className.toString()).getText();
		org.junit.Assert.assertEquals("See Text",
				webDriver.waitForElement("PopupTitle", ByTypes.className)
						.getText());
		webDriver.waitForElement("Close Window", ByTypes.linkText).click();
		;
		webDriver.switchToMainWindow(mainWin);

		return this;

	}

	public EdoHomePage clickOnPracticeTab() throws Exception {
		webDriver.waitForElement("//ul[@class='ulSteps']//li[@id=2]",
				ByTypes.xpath).click();
		return this;

	}

	public EdoHomePage dragAnswerToElement(String dataId, WebElement toElement)
			throws Exception {
		WebElement answer = webDriver.waitForElement("//div[@data-id='"
				+ dataId + "']", ByTypes.xpath);
		// WebElement questionFiled = webDriver.waitForElement(
		// "//span[@data-id='1_1']", ByTypes.xpath);
		webDriver.dragAndDropElement(answer, toElement);
		return this;

	}

	public EdoHomePage dragAnswer(String dataId) throws Exception {
		WebElement answer = webDriver.waitForElement("//div[@data-id='"
				+ dataId + "']", ByTypes.xpath);
		WebElement questionFiled = webDriver.waitForElement(
				"//span[@data-id='1_1']", ByTypes.xpath);
		webDriver.dragAndDropElement(answer, questionFiled);
		return this;

	}

	public EdoHomePage clickOnClearAnswer() throws Exception {
		webDriver.waitForElement("Restart", ByTypes.id).click();
		;
		return this;

	}

	public EdoHomePage checkIfAnswerIsCleared(String answerId) throws Exception {
		webDriver.checkElementNotExist("//span[@data-id='1_1'][@ans='"
				+ answerId + "']");
		return this;

	}

	public EdoHomePage clickOnTestTab() throws Exception {
		webDriver.waitForElement("//ul[@class='ulSteps']//li[@id=3]",
				ByTypes.xpath).click();
		;
		return this;

	}

	public EdoHomePage clickOnStartTest() throws Exception {
		webDriver.waitForElement("//div[@id='testIntro']//a", ByTypes.xpath)
				.click();
		;
		return this;

	}

	public EdoHomePage clickOnSubmitTest() throws Exception {
		webDriver.waitForElement("Submit", ByTypes.linkText).click();
		;
		return this;
	}

	public EdoHomePage approveTest() throws Exception {

		String mainWin = webDriver.switchToNewWindow(1);
		webDriver.waitForElement("btnOk", ByTypes.id).click();
		;
		webDriver.switchToMainWindow();
		return this;
	}

	public EdoHomePage checkStudentScore(String score) throws Exception {
		String actualScore = webDriver.waitForElement(
				"//span[@class='TestCongratulation']", ByTypes.xpath).getText();
		// Assert.assertEquals("Score is incorrect or not found", score,
		// actualScore);
		testResultService.assertEquals(score, actualScore);

		return this;
	}

	public EdoHomePage clickOnExploreTab() throws Exception {
		webDriver.waitForElement("//ul[@class='ulSteps']//li[@id=1]",
				ByTypes.xpath).click();
		return this;

	}

	public EdoHomePage checkUnitHeaderText(String expectedHeaderText)
			throws Exception {
		String text = webDriver.waitForElement(
				"//div[@class='mainAreaDivShort']//h1", ByTypes.xpath)
				.getText();
		Assert.assertEquals("Text not found", text, expectedHeaderText);
		return this;

	}

	public void markVocabRadioBtnAnswer(String questionId, String answer)
			throws Exception {
		webDriver
				.waitForElement(
						"//div[contains(@id,'q"
								+ questionId
								+ "')][@name='txt_div']//div//div//div//span[contains(text(),'"
								+ answer + "')]", ByTypes.xpath).click();

	}

	public void checkVocabRadioBtnAnswer(String questionId, boolean trueAnswer,
			String answer) throws Exception {
		String check;
		if (trueAnswer == true) {
			check = "vCheck";
		} else {
			check = "xCheck";
		}
		// webDriver.waitForElement("//div[contains(@id,'q"+questionId+"')][contains(@class,'"+check+"']",
		// ByTypes.xpath);
		webDriver.waitForElement(
				"//div[contains(@id,'q" + questionId
						+ "')][@name='txt_div'][contains(@class,'" + check
						+ "')]//div//div//div//span[contains(text(),'" + answer
						+ "')]", ByTypes.xpath);
	}

	public EdoHomePage clickOnCheckAnswers() throws Exception {
		webDriver.waitForElement("CheckAnswer", ByTypes.id).click();
		return this;

	}

	public EdoHomePage checkInputFieldIsDisplayed(String id) throws Exception {
		webDriver.waitForElement("//input[@id='" + id + "']", ByTypes.xpath,
				20, true).isDisplayed();
		return this;

	}

	public EdoHomePage ClickComponentStage(String index) throws Exception {

		webDriver.waitForElement(
				"//ul[@class='ulTasks']//li[@ind='" + index + "']",
				ByTypes.xpath).click();
		return this;
	}

	public EdoHomePage typeAnswerInInputField(String id, String answerText)
			throws Exception {
		webDriver.waitForElement("//input[@id='" + id + "']", ByTypes.xpath,
				20, true).sendKeys(answerText);
		return this;

	}

	public EdoHomePage checkTextInputAnswer(String id, boolean trueAnswer)
			throws Exception {

		String check;
		if (trueAnswer == true) {
			check = "vCheck";
		} else {
			check = "xCheck";
		}
		// webDriver.waitForElement("//div[contains(@id,'q"+questionId+"')][contains(@class,'"+check+"']",
		// ByTypes.xpath);
		webDriver.waitForElement("//div[@id='res" + id + "'][contains(@class,'"
				+ check + "')]", ByTypes.xpath,
				"Correct/incorrect answer do not match expected");

		return this;
	}

	public void clickOnCommunity() throws Exception {
		webDriver.waitForElement("Community", ByTypes.linkText).click();

	}

	public void clickOnTalkingIdioms() throws Exception {
		webDriver.waitForElement("//td//img[contains(@src,'idioms')]",
				ByTypes.xpath).click();

	}

	public void checkForTeacherComment(String commentID, String commentText)
			throws Exception {
		webDriver.waitForElement("//span[@id='"+commentID+"']//span", ByTypes.xpath)
				.click();
		String actualText = webDriver.waitForElement(
				"//div[@id='comments']//div[2]", ByTypes.xpath).getText();
		boolean commentOK = testResultService.assertEquals(commentText,
				actualText, "Teacher comment text not found or do not match");
		if (commentOK == false) {
			webDriver.printScreen("Comment do not match");
		}
	}

	public void checkAsStudentFeedbackComment(String commentId,
			boolean shouldBeUnderlined, String commentText) throws Exception {

		// WebElement element = webDriver.waitForElement(commentId, ByTypes.id);
		// Assert.assertEquals(commentText, element.getText());
		if (shouldBeUnderlined) {
			webDriver.waitForElement("//span[@id='" + commentId
					+ "'][contains(@class,'underline')]", ByTypes.xpath);
		} else {
			webDriver.checkElementNotExist("//span[@id='" + commentId
					+ "'][contains(@class,'underline')]");
		}

	}

	public void clickOnSeeScript() throws Exception {
		webDriver.waitForElement("seeAll", ByTypes.id).click();

	}

	public void checkScript(String textToFind) throws Exception {

		WebElement webElement = webDriver.waitForElement(
				"//div[@id='textContainer']", ByTypes.xpath);
		WebElement childElement = webDriver.getChildElementByXpath(webElement,
				".//p");

		String text = childElement.getText();
		Assert.assertTrue(text.contains(textToFind));

	}

	public void selectAnswerFromDropDown(String questonId, String answerText)
			throws Exception {
		webDriver.waitForElement("//span[@id='" + questonId + "']//div[2]",
				ByTypes.xpath).click();

		WebElement element = webDriver.waitForElement("//div[@selectedspan='"
				+ questonId + "']", ByTypes.xpath);
		WebElement childElement = webDriver.getChildElementByXpath(element,
				"//td[text()='" + answerText + "']");
		childElement.click();

	}

	public void checkDropDownAnswer(String id, boolean isCorrect)
			throws Exception {
		if (isCorrect) {
			webDriver.waitForElement("//span[@id='" + id
					+ "'][contains(@class,'vCheck')]", ByTypes.xpath);
		} else {
			webDriver.waitForElement("//span[@id='" + id
					+ "'][contains(@class,'xCheck')]", ByTypes.xpath);
		}

	}

	public boolean checkIfCreateNewPlanAppear() throws Exception {
		WebElement createPlan = webDriver.waitForElement("newPlanSP",
				ByTypes.id, false, webDriver.getTimeout());
		if (createPlan == null) {
			return false;
		} else {
			return true;
		}

	}

	public RecordPanel clickOnRecordYourself() throws Exception {
		webDriver.waitForElementAndClick("open_srp", ByTypes.id);
		// webDriver.waitForJSFunctionToEnd("pause(duration, func, isDebug);");
		// webDriver.executeJsScript("debug=true;");
		// webDriver.executeJsScript("alert(debug)");
		return new RecordPanel(webDriver, testResultService);

	}

	public RecordPanel clickOnRecordYourselfInVocabulary() throws Exception {
		webDriver.waitForElementAndClick("tempLink2", ByTypes.id);
		return new RecordPanel(webDriver, testResultService);

	}

	public void selectIdiomFromList(String id) throws Exception {
		webDriver.waitForElement("//ul[@id='idiomList']//li[4]", ByTypes.xpath)
				.click();

	}

	public void clickOnRecordFromIdioms() throws Exception {
		webDriver.waitForElement("//*[@id='mainAreaTD']/div[1]/div/a",
				ByTypes.xpath).click();
		webDriver.waitForElement("recordLI", ByTypes.id).click();

	}

	public RecordPanel clickOnPronunciation() throws Exception {
		webDriver.waitForElement("pf", ByTypes.id).click();
		return new RecordPanel(webDriver, testResultService);
	}

	public void clickOnVocabText(String text) throws Exception {
		webDriver.waitForElement(text, ByTypes.id).click();
	}

	public void selectTextFromContainer(int index) throws Exception {
		webDriver.waitForElement(
				"//div[@id='textContainer']//span[" + index + "]",
				ByTypes.xpath).click();

	}

	public void clickOnTextAreaAndCheckThatCommentIsNotDislayed()
			throws Exception {
		clickOnTextArea(20, 20);
		Thread.sleep(2000);
		webDriver
				.checkElementNotExist("//div[@id='comments']//div[@class='commentTitle']");

	}

	public void clickOnTextArea(int x, int y) throws Exception {
		WebElement assayText = webDriver.waitForElement(
				"//div[@id='essayText']//div[1]//div", ByTypes.xpath);
		webDriver.clickOnElementWithOffset(assayText, x, y);

	}

	public edoPlacementTestPage clickOnPLTTests() throws Exception {
		webDriver
				.waitForElement(
						"//table[@id='eduobj_PlTest']//tbody//tr[1]//td",
						ByTypes.xpath).click();
		Thread.sleep(500);
		;
		webDriver.switchToNewWindow();
		return new edoPlacementTestPage(webDriver, testResultService);
	}

	public void navigateToCourseUnitComponent(Course course,
			SubComponentName subComponentName) throws Exception {
		clickOnCourses();
		clickOnCourseByName(course.getName());
		// waitForCourseDetailsToBeDisplayed(course.getName());
		String courseUnit = course.getCourseUnits().get(0).getName();
		clickOnCourseUnit(courseUnit);
		String unitComponent = course.getCourseUnits().get(0)
				.getUnitComponent().get(0).getName();
		clickOntUnitComponent(unitComponent, subComponentName.toString());
	}

	public void clickOnSeeAnswer() throws Exception {
		webDriver.waitForElement("SeeAnswer", ByTypes.id).click();

	}

	public void selectTestRadioAnswer(String answerId) throws Exception {
		webDriver.waitForElement(
				"//div[@id='" + answerId + "']//div//div//div//input",
				ByTypes.xpath).click();
		;

	}

	public WebElement getDropQuestionByQnum(String string) throws Exception {
		return webDriver.waitForElement("//span[@qnum='" + string + "']",
				ByTypes.xpath);
	}

	public String getTestScore() throws Exception {
		String actualScore = webDriver.waitForElement(
				"//span[@class='TestCongratulation']", ByTypes.xpath).getText();
		return actualScore;
	}

}
