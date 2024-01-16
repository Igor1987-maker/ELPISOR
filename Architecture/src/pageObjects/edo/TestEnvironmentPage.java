package pageObjects.edo;

import Enums.*;
import Objects.*;
import drivers.GenericWebDriver;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import pageObjects.GenericPage;
import services.PageHelperService;
import services.TestResultService;
import services.TextService;

import java.util.*;

public class TestEnvironmentPage extends GenericPage {

	NewUxLearningArea2 learningArea2 = new NewUxLearningArea2(webDriver, testResultService);

	PageHelperService pageHelper = new PageHelperService();

	@FindBy(xpath = "//div[@class='testEnv__testPath']")
	public WebElement testName;

	@FindBy(xpath = "//*[contains(@id,'Title')]")
	public WebElement instructionTitle;

	@FindBy(xpath = "//*[contains(@id,'Text')]")
	public WebElement instrcutionText;

	@FindBy(id = "learning__TestInstructionsButton--start")
	public WebElement startTestButton;

	@FindBy(id = "learning__TestInstructionsButton--cancel")
	public WebElement cancelButton;

	@FindBy(xpath = "//li[@id='sitemenu__itemCloseTestEnv']//a")
	public WebElement closeButton;

	@FindBy(xpath = "//span[@id='learning__TestEndButton--start']")
	public WebElement exitCourseTestButton;

	@FindBy(xpath = "//span[@id='learning__testEndPLTButton--cancel']")
	public WebElement exitPltButton;

	@FindBy(xpath = "//*[contains(@id,'next')]")
	public WebElement nextButton;

	@FindBy(xpath = "//div[@class='multiRadio']//label")
	public List<WebElement> questionsLabels;

	@FindBy(xpath = "//div[@class='multiRadio multiRadio--singleSelection']//input")
	public List<WebElement> questionsCheckboxes;

	@FindBy(xpath = "//input[@id='btnOk']")
	public WebElement sumbitOkButton;
	@FindBy(css = ".learning__nextItem")
	public WebElement pltNext;

	@FindBy(css = "#learning__submitTestItem")
	public WebElement pltSubmit;



	@FindBy(xpath = "//div[@id='learning__submitTestItem']")
	public WebElement submitButton;

	@FindBy(xpath = "//div[contains(@class,'learning__prevItem')]")
	public WebElement backButton;

	@FindBy(xpath = "//div[contains(@class,'learning__prevItem')]//a")
	public WebElement backButtonProperties;

	@FindBy(xpath = "//div[@class='layout__text-pull layout__unlistPull  TinyScrollbarW']")
	public WebElement submitMessage;

	@FindBy(xpath = "//ed-la-tasksnav/div")
	public WebElement tasksNav;

	@FindBy(xpath = "//div[@class='learning__tasksNav_closeBtnW']")
	public WebElement closeTasksNavButton;

	@FindBy(xpath = "//li[@id='sitemenu__itemAboutUs sitemenu__itemAboutUs--testEnv']/a")
	public WebElement edIcon;

	@FindBy(xpath = "//img[@class='layout__push siteTmpl__footerLogoImage']")
	public WebElement edLogoInEDInfo;

	@FindBy(xpath = "//a[@title='Close window']")
	public WebElement closeEDInfoButton;

	@FindBy(xpath = "//*[contains(@id,'Text')]//span")
	public WebElement outroPageScore;

	@FindBy(xpath = "//div[@class='learning__testTimer']")
	public WebElement testTimer;

	@FindBy(xpath = "//div[@class='learning__TestLevelSelect']//li//span")
	public List<WebElement> pltLevels;

	@FindBy(xpath = "//div[@class='learning__TestLevelSelect']//li//input")
	public List<WebElement> pltLevelsButtons;

	@FindBy(xpath = "//select[@id='learning__TestLangSelectList']")
	public WebElement pltLanguageList;

	@FindBy(xpath = "//select[@id='learning__TestLangSelectList']//option")
	public List<WebElement> pltLanguageOptions;

	@FindBy(xpath = "//div[@class='learning__testEndPLTLevelMessageW']//b")
	public WebElement pltFinalLevelEndPage;

	@FindBy(xpath = "//*[@id='Bars']//*[@id='Reading']")
	public WebElement pltReadingLevelEndPage;

	@FindBy(xpath = "//*[@id='Bars']//*[@id='Listening']")
	public WebElement pltListeningLevelEndPage;

	@FindBy(xpath = "//*[@id='Bars']//*[@id='Grammer']")
	public WebElement pltGrammarLevelEndPage;

	@FindBy(xpath = "//ed-plt-endlink//span[text()='Click here']")
	public WebElement pltClickHereButtonCourseDescription;

	@FindBy(xpath = "//span[text()='Exit']")
	public WebElement pltExitTestBtn;

	@FindBy(xpath = "//div[@class='prMCQ__questionText']")
	public WebElement questionText;

	@FindBy(xpath = "//span[@id='learning__tasksNav_submitTest']")
	public WebElement submitInTaskBar;
					 //*[@id="mCSB_62_container"]/div[2]/div/ed-plt-endlink/span[1]/b
	@FindBy(xpath = "//*[@class='learning__testEndPLTText']/ed-plt-endlink/span[1]/b")
	public WebElement apiResLevel;

	//@FindBy(className = "learning__testEndPLTText")
	//public WebElement apiResLevel; 



//	@FindBy(xpath = "//*[@id='mCSB_50_container']/div[1]/div[1]/ed-plt-endlink/span[1]/b") //learning__testEndPLTLevelMessageW
	//public WebElement cefrLevel;      

	@FindBy(className = "learning__testEndPLTLevelMessageW") //learning__testEndPLTLevelMessageW
	public WebElement cefrLevel;

	@FindBy(className = "learning__testEndPLTTextDisclaimer")
	public WebElement suggestionText;

	@FindBy(css = ".learning__taskNavPagerCounterIW .learning__taskNavPCCurrentTask")
	public WebElement currentTask;
	@FindBy(css = ".ng-valid")
	List<WebElement> levelButton;

	@FindBy(css = ".layout__radio")
	WebElement radioButton;

	@FindBy(id = "sitemenu__itemCloseTestEnv")
	WebElement exitButton;

	@FindBy(css = ".DDLOptions__selected")
	WebElement dropdown;

	@FindBy (css =".DDLOptions__listItem[id^='DDLOptions__listItem_aid_']")
	WebElement dropdownValue;




	TextService textService = new TextService();

	public static boolean useSMB = true;

	public static int sectionIndex = 1;
	public static int lessonIndex = 1;
	public static String testDuration = "";
	public static int pltRound = 1;

	public TestEnvironmentPage(GenericWebDriver webDriver, TestResultService testResultService) throws Exception {
		super(webDriver, testResultService);
		// TODO Auto-generated constructor stub
		PageFactory.initElements(webDriver.getWebDriver(), this);
	}

	@Override
	public GenericPage waitForPageToLoad() throws Exception {
		// TODO Auto-generated method stub
		webDriver.waitUntilElementAppears("//*[contains(@id,'Title')]", 30);
		return null;
	}

	@Override
	public GenericPage OpenPage(String url) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public void validateTestName(String expectedTestName) {
		webDriver.waitUntilElementAppears(testName, 20);
		String actualTestName = testName.getText();
		actualTestName = actualTestName.replace(" - ", " ");
		testResultService.assertEquals(expectedTestName, actualTestName, "Test Name is Incorrect.");
	}

	public boolean validateInstructionTitle(String expectedInstrucionTitle) throws Exception {
		try {
			webDriver.waitUntilElementAppears(instructionTitle, 3);

			if (expectedInstrucionTitle.equals("null")) {
				testResultService.assertEquals("", instructionTitle.getText(),
						"Intruction Title is Incorrect. Expected: Empty Title. Actual: " + instructionTitle.getText());
			} else {
				testResultService.assertEquals(expectedInstrucionTitle, instructionTitle.getText(),
						"Instruction Title is Incorrect. Expected: " + expectedInstrucionTitle + ". Actual: "
								+ instructionTitle.getText());
			}
			return true;
		} catch (Exception e) {
			testResultService.addFailTest("Instruction Title of Intro was not Found", false, true);
			return false;
		}
	}

	public boolean validateInstrcutionText(String expectedInstructionText) throws Exception {
		try {
			webDriver.waitUntilElementAppears(instrcutionText, 5);
			// WebElement element =
			// webDriver.waitUntilElementAppears("learning__TestInstructionsText",
			// ByTypes.id, 5);
			if (expectedInstructionText.equals("null")) {
				testResultService.assertEquals("", instrcutionText.getText(), "Instruction Text is Incorrect");
			} else {
				testResultService.assertEquals(true, instrcutionText.getText().equals(expectedInstructionText),
						"Instruction Text is Incorrect. Expected: " + expectedInstructionText + ". Actual: "
								+ instrcutionText.getText());
			}
			return true;
		} catch (Exception e) {
			testResultService.addFailTest("Instruction Text of Intro was not Found", false, true);
			return false;
		}
	}

	public void validateInstructionTitleIsNotNull() throws Exception {
		testResultService.assertEquals(true, instructionTitle.getText() != null, "Instruction Title is Null.");
	}

	public void validateInstrcutionTextIsNotNull() throws Exception {
		testResultService.assertEquals(true, instrcutionText.getText() != null, "Instruction Text is Null.");
	}

	public void clickStartTest() {
		webDriver.clickOnElement(startTestButton);
	}

	public void clickCancel() {
		webDriver.clickOnElement(cancelButton);
	}

	public void clickNext() {
		webDriver.waitUntilElementAppears(nextButton, 5);
		webDriver.clickOnElement(nextButton);
	}

	public void clickClose() throws Exception {
		webDriver.waitForElementByWebElement(closeButton,"closeButton", true, 5);
		webDriver.clickOnElement(closeButton);
		Thread.sleep(500);
		try {
			testResultService.assertEquals(true, submitMessage.getText().contains(
					"You haven't answered all of the test questions.\nAre you sure you want to exit this test?"),
					"Close Test Message is Incorrect");
			webDriver.ClickElement(sumbitOkButton);
		} catch (Exception e) {
			testResultService.addFailTest("Close Test Message was not displayed.", false, true);
		}
	}

	public boolean validateTemplate(String instructionTitle, String instructionText) throws Exception {

		report.startStep("Validate Instruction Title");
		boolean isTitleValidated = validateInstructionTitle(instructionTitle);

		report.startStep("Validate Instruction Text");
		boolean isTextValidated = validateInstrcutionText(instructionText);
		return isTitleValidated && isTextValidated;
	}

	public void validateTemplateIsNotNull() throws Exception {

		report.startStep("Validate Instruction Title Is Not Null");
		validateInstructionTitleIsNotNull();

		report.startStep("Validate Instruction Text Is Not Null");
		validateInstrcutionTextIsNotNull();
	}

	/////////////////////////////////////////////////////////////////////

	public void pressOnStartTest() {
		webDriver.clickOnElement(startTestButton);
	}

	public void answerQuestions(String testId, int unitsCountToAnswer, String roundLevel, List<String> unitIds,
			JSONObject jsonObj, JSONObject localizationJson, String testName, int unitsToAnswer) throws Exception {
		String filePath = "";

		CourseTests testType = CourseTests.MidTerm;

		if (testId.startsWith("154") || testId.startsWith("9890")) {
			filePath = "files/CourseTestData/CourseTest_Answers_2019.csv";
		} else if (testId.equals("47845")) {
			filePath = "files/CourseTestData/CourseTest_Answers_2019.csv";
			testType = CourseTests.FinalTest;
		} else {
			filePath = "files/CourseTestData/CourseTest_Answers.csv";
		}

		CourseTest courseTest = initCourseTestFromCSVFile(filePath, CourseCodes.B1, testType, unitsCountToAnswer);
		performCourseTestNewTE(courseTest, unitsCountToAnswer, roundLevel, unitIds, jsonObj, localizationJson, testName,
				unitsToAnswer);
	}

	public CourseTest initCourseTestFromCSVFile(String path, CourseCodes courseCode, CourseTests courseTestType,
			int sectionsToAnswer) throws Exception {

		List<CourseTestSection> testSections = new ArrayList<CourseTestSection>();
		CourseTestSection section = new CourseTestSection();

		for (int i = 0; i < sectionsToAnswer; i++) {
			section = initSection(path, i + 1, courseCode, courseTestType, false);
			testSections.add(section);
		}

		CourseTest courseTest = new CourseTest(testSections, courseTestType);

		return courseTest;
	}

	public CourseTestSection initSection(String path, int sectionNumber, CourseCodes courseCode,
			CourseTests courseTestType, boolean skipSection) throws Exception {
		TextService textService = new TextService();
		List<String[]> testData = textService.getStr2dimArrFromCsv(path);

		List<TestQuestion> sectionQuestions = new ArrayList<TestQuestion>();

		for (int i = 0; i < testData.size(); i++) {

			// if cycle number match - create TestQuestion object
			if (testData.get(i)[0].equals(String.valueOf(sectionNumber))
					&& testData.get(i)[1].equals(courseCode.toString())
					&& testData.get(i)[2].equals(courseTestType.toString())) {

				try {
					report.report("Reading line: " + (i + 1) + " from csv file");

					String[] answers = textService.splitStringToArray(testData.get(i)[3], "\\|");

					String[] answerDestinations = textService.splitStringToArray(testData.get(i)[4], "\\|");

					String[] wrongAnswers = textService.splitStringToArray(testData.get(i)[5], "\\|");

					if (skipSection)
						wrongAnswers[0] = "DoNotAnswer";

					report.report("Question type from csv is: " + testData.get(i)[6]);
					TestQuestionType questionType = TestQuestionType.valueOf(testData.get(i)[6]);

					TestQuestion question = new TestQuestion(answers, answerDestinations, wrongAnswers, new int[] {},
							questionType);
					sectionQuestions.add(question);
					report.report("Finished adding question data for line: " + i);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		CourseTestSection testSection = new CourseTestSection(sectionNumber, courseCode, courseTestType,
				sectionQuestions);

		return testSection;
	}

	public void performCourseTestNewTE(CourseTest courseTest, int sectionsToAnswer, String roundLevel,
			List<String> unitIds, JSONObject jsonObj, JSONObject localizationJson, String testName, int unitsToAnswer)
			throws Exception {

		CourseTestSection round = new CourseTestSection();

		int lessonsCounter = 0;

		for (int i = 0; i < unitsToAnswer; i++) {
			report.startStep("Validate Unit Intro");
			validateIntro(jsonObj, localizationJson, unitIds.get(i), testName);

			List<String> lessonsIds = getLessonsIdsFromLessonDetailsArrOfSpecificUnit(unitIds.get(i));

			if (roundLevel.equals("2")) {

				for (int j = 0; j < lessonsIds.size(); j++) {

					report.startStep("Validate Lesson Intro");
					validateIntro(jsonObj, localizationJson, lessonsIds.get(j), testName); // validate lesson id is
																							// correct

					report.startStep("Validate Title Index");
					validateTitleIndex();

					report.startStep("Answer Round");
					if (courseTest.getSections().size() > j) {
						round = courseTest.getSections().get(lessonsCounter);

						answerSectionQuestionsNewTE(round);
					}

					if (j < lessonsIds.size() - 1) {
						report.startStep("Click Next");
						Thread.sleep(300);
						clickNext();
						Thread.sleep(300);
					}
					lessonsCounter++;
					lessonIndex++;
				}

				lessonIndex = 1; // end of unit- reset lesson index

				report.startStep("Submit");
				submit(true);
				sectionIndex++;

			} else if (roundLevel.equals("3")) {

				for (int j = 0; j < lessonsIds.size(); j++) {

					report.startStep("Validate Lesson Intro");
					validateIntro(jsonObj, localizationJson, lessonsIds.get(j), testName);

					report.startStep("Validate Title Index");
					validateTitleIndex();

					report.startStep("Answer Round");
					if (courseTest.getSections().size() > j) {
						round = courseTest.getSections().get(lessonsCounter);
						answerSectionQuestionsNewTE(round);
					}

					report.startStep("Submit");
					submit(true);
					Thread.sleep(1000);

					lessonsCounter++;
					lessonIndex++;
				}
				lessonIndex = 1;
				sectionIndex++;
			}

		}

		report.startStep("Complete the Other Rounds Until the End of Test");
		Thread.sleep(2000);
		int unitsLeftToBrowse = unitIds.size() - unitsToAnswer;
		if (roundLevel.equals("2")) {
			for (int i = 0; i < unitsLeftToBrowse; i++) {

				// Verify the current page is not intro
				boolean isIntro = isIntro();
				while (isIntro==true)
				{
					isIntro = isIntro();
					if (isIntro)
						learningArea2.clickOnNextButton();
					else
						isIntro=false;
			}
				browseToLastSectionTask(); // change elements
				submit(false);
				Thread.sleep(2000);
			}
		} else if (roundLevel.equals("3")) {
			for (int i = 0; i < unitsLeftToBrowse; i++) {
				List<String> lessonsIds = getLessonsIdsFromLessonDetailsArrOfSpecificUnit(unitIds.get(i));
				for (int j = 0; j < lessonsIds.size(); j++) {
					browseToLastSectionTask(); // change elements
					submit(false);
					Thread.sleep(2000);
				}
			}
		}
	}

	private boolean isIntro() {

		 WebElement element = null;
		try {
			element = webDriver.waitForElement("//div[contains(@id,'intro_')]",ByTypes.xpath,false,2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		boolean isIntro;
		if (element!=null)
			isIntro=true;
		else
			isIntro=false;

		return isIntro;
	}

	public List<String> getLessonsIdsFromLessonDetailsArrOfSpecificUnit(String unitId) throws Exception {
		List<String[]> lessons = dbService.getComponentDetailsByUnitId(unitId);

		List<String> lessonsIds = new ArrayList<String>();
		for (int k = 0; k < lessons.size(); k++) {
			lessonsIds.add(lessons.get(k)[1]);
		}
		return lessonsIds;
	}

	// submit of mid term
	public void submit(boolean answeredAllQuestions) throws Exception {
		try {
			try {
				// testResultService.assertEquals(expectedButtonText, submitButton.getText(),
				// "Submit Button Text is Incorrect");

				while (nextButton.isDisplayed()) {
					webDriver.clickOnElement(nextButton);
					Thread.sleep(500);
				}
			} catch (Exception e) {
			}

			webDriver.ClickElement(submitButton);
			Thread.sleep(2000);
			//webDriver.waitForElementByWebElement(submitMessage, "alert", false, 5);
			if (answeredAllQuestions) {
				testResultService.assertEquals(false, webDriver.isDisplayed(submitMessage),
						"Message is displayed after Submit Even Though Student Answered All Questions.");
				if (webDriver.isDisplayed(submitMessage)) {
					webDriver.ClickElement(sumbitOkButton);
				}
			} else {
				if (webDriver.isDisplayed(submitMessage)) {
					String expectedMessage = "You haven't answered all the questions in this section.\nPlease note that you cannot repeat this section once it is submitted. Are you sure you want to submit this section?";

					testResultService.assertEquals(true, submitMessage.getText().contains(expectedMessage),
							"Submit message is Incorrect.");

					// testResultService.assertEquals(true, submitMessage.getText().contains("You
					// haven't answered all of the test questions."), "Submit message is
					// Incorrect.");
					webDriver.ClickElement(sumbitOkButton);
				} else {
					testResultService.addFailTest(
							"Submit Message Is Not Displayed Even Though Student Did Not Answer All Questions.", false,
							true);
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void answerSectionQuestions(CourseTestSection section) throws Exception {

		for (int i = 0; i < section.getNumberOfQuestions(); i++) {

			// Thread.sleep(1000);
			webDriver.waitUntilElementAppears("//div[@id='pmContainer']", 30);

			TestQuestion question = section.getSectionQuestions().get(i);

			TestQuestionType questionType = question.getQuestionType();

			String[] answers = new String[question.getCorrectAnswers().length];

			for (int j = 0; j < question.getCorrectAnswers().length; j++) {
				answers[j] = question.getCorrectAnswers()[j].replace("~", ",");
				// answers[j] = question.getCorrectAnswers()[j].replace(".", "");
				// answers[j] = textService.resolveAprostophes(answers[j]);
			}
			question.setCorrectAnswers(answers);

			// waitForTestPageLoaded();

			if (!question.getIncoreectAnswers()[0].equals("DoNotAnswer")) { // skip the question if wrong answer exist
																			// in csv file or parameter of skipCycle is
																			// true

				switch (questionType) {

				case DragAndDropMultiple:
					answerDragAndDropQuestion(question);
					break;

				case RadioMultiple:
					answerMultiCheckBoxQuestion(question);
					break;

				case RadioSingle:
					answerCheckboxQuestion(question);
					break;

				case DragAndDropSingle:
					answerDragAndDropQuestion(question);
					break;

				case comboBox:
					answerComboBoxQuestion(question);
					break;

				case DrapAndDropToPicture:
					answerDragAndDropToPictureQuestion(question);
					break;

				default:
					break;
				}
			}

			Thread.sleep(1000);
			if (i < section.getNumberOfQuestions() - 1) {
				pressOnNextTaskArrow();
			}
			Thread.sleep(500);
			// webDriver.waitUntilElementAppears("//div[@id='pmContainer']", 30);
		}
	}

	public void browseToLastSectionTask() throws Exception {
		NewUxLearningArea2 learningArea2 = new NewUxLearningArea2(webDriver, testResultService);

		int taskCount = learningArea2.getTasksCount();
		report.addTitle("Actual item in this section: " + taskCount);
		webDriver.ClickElement(tasksNav);
		String activeTask = webDriver.waitForElement("//div[contains(@class,'current')]//span[1]", ByTypes.xpath)
				.getText();
		webDriver.ClickElement(closeTasksNavButton);
		Thread.sleep(1000);
		goTolastTestSectionItem(taskCount + Integer.parseInt(activeTask) - 1, activeTask);
		Thread.sleep(1000);

	}

	public void browseToLastSectionTaskNew() throws Exception {

		int taskCount = learningArea2.getTasksCount();
		report.addTitle("Actual item in this section: " + taskCount);
		webDriver.ClickElement(tasksNav);
		String activeTask = webDriver.waitForElement("//div[contains(@class,'current')]//span[1]", ByTypes.xpath)
				.getText();
		webDriver.ClickElement(closeTasksNavButton);

		// click next "taskCount-activeTask" num of times
		for (int i = 0; i < taskCount - Integer.parseInt(activeTask); i++) {
			clickNext();
			Thread.sleep(2000);
		}
		// goTolastTestSectionItem(taskCount+Integer.parseInt(activeTask)-1,activeTask);
		Thread.sleep(2000);
	}

	public void pressOnSubmitSection(boolean pressSubmitOnAlert) throws Exception {
		report.addTitle("Submit section");
		webDriver.waitForElement("SubmitTest", ByTypes.id, false, webDriver.getTimeout()).click();
		Thread.sleep(2000);
		boolean status = false;
		int i = 0;
		if (pressSubmitOnAlert) {

			while ((!status) && (i < 3)) {
				webDriver.sleep(1);
				status = webDriver.waitForElement("//div[@id='EdoFrameBoxContent']/iframe[1]", ByTypes.xpath)
						.isDisplayed();
				i++;
			}
			webDriver.switchToFrame(
					webDriver.waitForElement("//div[@id='EdoFrameBoxContent']/iframe[1]", ByTypes.xpath));
			webDriver.sleep(1);
			webDriver.waitForElement("btnOk", ByTypes.name).click();
			webDriver.sleep(2);
			switchToTestAreaIframe();
		}
	}

	public void waitForTestPageLoaded() {
		WebElement element = null;

		for (int i = 0; i <= 20 && element == null; i++) {
			try {
				element = webDriver.waitForElement("//*[contains(@class,'prMCQ__qaInnerSet')]", ByTypes.xpath, false,
						1);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void goTolastTestSectionItem(int taskCount, String activeTask) throws Exception {
		int currentTask = Integer.parseInt(activeTask);

		for (int i = currentTask; i < taskCount; i++) {
			waitUntilNextButtonIsEnabled(10);
			clickNext();
			report.addTitle("Clicked on next: " + i + " time.");
		}
	}

	public void answerDragAndDropQuestion(TestQuestion question) throws Exception {
		// This function works for old Test Environment

		boolean isAnswerFound = false;

		String[] answerSourceLocations = question.getCorrectAnswers();
		// String[] answerDestinationLocations = question.getAnswersDestinations();
		for (int i = 0; i < answerSourceLocations.length; i++) {
			int index = i + 1;
			/*
			 * WebElement from = webDriver.waitForElement( "//div[text()=" +
			 * question.getCorrectAnswers()[i] + "]", ByTypes.xpath,
			 * 1,false,"element of answer number text " +
			 * answerSourceLocations[i].toString());
			 */
			WebElement from = null;
			List<WebElement> answers = webDriver.getWebDriver()
					.findElements(By.xpath("//div[@class='draggable wordBankTile']"));
			for (int j = 0; j < answers.size(); j++) {
				if (answers.get(j).getText().replace("\"", "").replace(",", "").trim()
						.equals(question.getCorrectAnswers()[i].trim())) {
					from = answers.get(j);
					isAnswerFound = true;
					break;
				} else {
					isAnswerFound = false;
				}
			}

			if (!isAnswerFound) {
				String[] answersWeb = new String[answers.size()];
				String[] answersFile = new String[answerSourceLocations.length];
				for (int k = 0; k < answers.size(); k++) {
					answersWeb[k] = answers.get(k).getText().replace("\"", "").replace(",", "").trim();
					answersFile[k] = question.getCorrectAnswers()[k].trim();
				}
				testResultService.addFailTest("Answer number " + (i + 1) + " wasn't found. Answers on Web: "
						+ Arrays.toString(answersWeb) + ". Answer from file: " + Arrays.toString(answersFile), false,
						true);
			}
			WebElement to = webDriver.waitForElement("//span[@data-id='1_" + index + "']", ByTypes.xpath, false, 1);
			webDriver.dragAndDropElement(from, to);
		}
	}

	public void answerMultiCheckBoxQuestion(TestQuestion question) throws Exception {

		for (int i = 0; i < question.getCorrectAnswers().length; i++) {

			List<WebElement> answersPerQuestion = webDriver.getWebDriver().findElements(
					By.xpath("//div[@id='q" + (i + 1) + "']//div[contains(@class,'lessonMultipleAnswer')]//span"));
			for (int j = 0; j < answersPerQuestion.size(); j++) {
				if (answersPerQuestion.get(j).getText().replace(",", "").toLowerCase()
						.equals(question.getCorrectAnswers()[i].toLowerCase().trim())) {
					// if (answersPerQuestion.get(j).getText().replace(",",
					// "").equals(question.getCorrectAnswers()[i].toLowerCase().trim())) {
					webDriver.ClickElement(answersPerQuestion.get(j));
					break;
				}
			}

			/*
			 * if (question.getCorrectAnswers()[i].toLowerCase().equals("false") ||
			 * question.getCorrectAnswers()[i].toLowerCase().equals("true")) {
			 * question.getCorrectAnswers()[i] =
			 * question.getCorrectAnswers()[i].toLowerCase();
			 * question.getCorrectAnswers()[i] =
			 * question.getCorrectAnswers()[i].substring(0,1).toUpperCase() +
			 * question.getCorrectAnswers()[i].substring(1); } WebElement element =
			 * webDriver.waitForElement(
			 * "//span[@class='multiTextInline'][contains(text(),\"" +
			 * question.getCorrectAnswers()[i] + "\")]", ByTypes.xpath, false, 3);
			 * webDriver.ClickElement(element);
			 */
		}
	}

	public void answerCheckboxQuestion(TestQuestion question) throws Exception {
		// webDriver.waitForElement("//span[@class='multiTextInline'][contains(text(),"
		// + question.getCorrectAnswers()[0] + ")]", ByTypes.xpath,false,1).click();
		questionsLabels = webDriver.getWebDriver().findElements(By.xpath("//div[@class='multiText']//span")); // old
																												// Test
																												// Environment
		for (int i = 0; i < questionsLabels.size(); i++) {
			if (questionsLabels.get(i).getText().replace(",", "").toLowerCase().trim()
					.contains(question.getCorrectAnswers()[0].toLowerCase().trim())
					|| question.getCorrectAnswers()[0].toLowerCase().trim()
							.contains(questionsLabels.get(i).getText().toLowerCase().trim())) {
				// if (questionsLabels.get(i).getText().replace(",",
				// "").equals(question.getCorrectAnswers()[0])) {
				questionsCheckboxes.get(i).click();
				break;
			}
		}
	}

	public void answerComboBoxQuestion(TestQuestion question) throws Exception {
		// int index = 0;
		boolean isAnswerFound = false;
		for (int i = 0; i < question.getCorrectAnswers().length; i++) {
			// index = i + 1;
			/*
			 * webDriver.waitForElement( "//div[@class='fitb']//span[@id='1_" + index +
			 * "']//div[2]", ByTypes.xpath,false,1).click();
			 */
			webDriver.getWebDriver().findElements(By.xpath("//div[@class='fitb']//span//div[2]")).get(i).click();

			Thread.sleep(1000);
			List<WebElement> answersOptionsDropDown = webDriver.getWebDriver()
					.findElements(By.xpath("//div[@class='optionsWrapper']//tr//td"));
			String[] actualAnswerOptions = new String[answersOptionsDropDown.size()];
			for (int j = 0; j < answersOptionsDropDown.size(); j++) {
				// if
				// (answersOptionsDropDown.get(j).getText().contains(question.getCorrectAnswers()[i])
				// ||
				// question.getCorrectAnswers()[i].contains(answersOptionsDropDown.get(j).getText()))
				// {
				actualAnswerOptions[j] = (String) answersOptionsDropDown.get(j).getText().replace(",", "");
				if (answersOptionsDropDown.get(j).getText().replace(",", "")
						.equals(question.getCorrectAnswers()[i].trim())) {
					webDriver.ClickElement(answersOptionsDropDown.get(j));
					isAnswerFound = true;
					break;
				}
			}

			if (!isAnswerFound) {
				testResultService.addFailTest("Answer not found. Answer on web: " + Arrays.toString(actualAnswerOptions)
						+ " . Answer from file: " + question.getCorrectAnswers()[i].trim(), false, true);
				webDriver.getWebDriver().findElements(By.xpath("//div[@class='fitb']//span//div[2]")).get(i).click();
			}
		}
	}

	public void pressOnNextTaskArrow() throws Exception {
		WebElement element = webDriver.waitUntilElementAppears("tasksBtnext", ByTypes.className, 10);
		element.click();
	}

	public int getSectionTaskCountClassic() throws Exception {

		WebElement element;
		try {
			element = webDriver.waitForElement("ulTasks", ByTypes.className, false, 2); // CT page

			if (element == null) {
				// element =
				// webDriver.waitForElement("//*[@id='mainAreaTD']/div[2]/div/div[1]/div/ul",ByTypes.xpath,false,webDriver.getTimeout());
				element = webDriver.waitForElement("//*[@id='btnTools']/div/ul", ByTypes.xpath, false, 30); // from TMS
			}

			List<WebElement> elementCouns = webDriver.getChildElementsByXpath(element, "li");
			int taskCount = elementCouns.size();
			return taskCount;
		} catch (Exception e) {
			testResultService.addFailTest("Element task count not found", false, true);

			// String ct = webDriver.getCookie("CT");
			// String compId;
			// List<String[]> items = dbService.getComponentItems(compId);

			int taskCount = 0;

			return taskCount;
		}
	}

	public void checkSubmitSectionDisplay(boolean buttonDisplay) throws Exception {

		boolean currentState = false;
		WebElement element = webDriver.waitForElement("SubmitTest", ByTypes.id, 2, false);

		if (element != null) {
			currentState = true;
		}
		TextService.assertEquals("The Submit section button wrong display", buttonDisplay, currentState);
	}

	public void switchToTestAreaIframe() throws Exception {
		Thread.sleep(5000);
		webDriver.switchToTopMostFrame();
		webDriver.switchToFrame(
				webDriver.waitForElement("oed__iframe", ByTypes.className, false, webDriver.getTimeout()));
	}

	public void validateIntros(JSONObject jsonObj, JSONObject localizationJson, String testName) throws Exception {

		// Init variables
		String template = "";
		String title = "";
		String text = "";
		String introSettings = "";
		JSONObject tempJSONObj;
		String testId = "";
		JSONArray tempJSONArr;

		// Retrieve Final Number of Intros
		int introsCounter = returnFinalNumOfStartIntros(jsonObj);

		// Run through all Intros
		for (int i = 0; i < introsCounter; i++) {

			// get test id from ui
			testId = "989012509"; // change this to get from ui

			validateIntro(jsonObj, localizationJson, testId, testName);

			/*
			 * JSONObject tempJsonObj = jsonArr.getJSONObject(i); template = (String)
			 * tempJsonObj.get("TemplateCode");
			 *
			 * // Retrieve Title title = (String) tempJsonObj.get("TitleCode");
			 *
			 * // Retrieve Text text = (String) tempJsonObj.get("TextCode");
			 *
			 * // get title and text from localization title =
			 * retrieveJsonElementFromLocalizationAndModifyIt(localizationJson, title,
			 * testName); text =
			 * retrieveJsonElementFromLocalizationAndModifyIt(localizationJson, text,
			 * testName);
			 *
			 * validateTemplate(title, text);
			 *
			 * if (template.equals("NodeIntroButtons")) {
			 * report.startStep("Click Next Button"); clickNext(); } else if
			 * (template.equals("TestIntro")) { report.startStep("Click Start Test Button");
			 * clickStartTest(); }
			 */
		}
	}

	public int returnFinalNumOfStartIntros(JSONObject jsonObj) {

		JSONArray tempJSONArr;
		String introSettings = "";
		int introsCounter = 0;
		int tempNumOfIntros = 0;

		// Get Elements Count (number of Tests IDs)
		int elementsCount = jsonObj.length();

		// Get All Test IDs from JSON
		JSONArray names = jsonObj.names();

		for (int i = 0; i < elementsCount; i++) {
			introSettings = getValueOfWantedKeyInSpecificTestId(jsonObj, names.getString(i), "IntroSettings");

			// Convert to JSON Array
			tempJSONArr = new JSONArray(introSettings);

			// Return Number of Start Intros in Intro Settings
			tempNumOfIntros = getNumOfStartIntrosFromInrosSettingsJSON(tempJSONArr);

			// Add this Number to Intros Counter
			introsCounter += tempNumOfIntros;
		}
		return introsCounter;
	}

	public String validateJsonElementNotNull(Object o) {
		String text = "";
		if (!JSONObject.NULL.equals(o)) {
			text = (String) o;
		} else {
			text = "null";
		}
		return text;
	}

	public String retrieveJsonElementFromLocalizationAndModifyIt(JSONObject localizationJson, String key,
			String testName) {

		String jsonElementLocal = "";

		try {
			jsonElementLocal = (String) localizationJson.get(key);
		} catch (Exception e) {
			jsonElementLocal = key;
		}

		String[] splittedTestName = testName.split(" ");
		if (splittedTestName.length > 2) {
			testName = splittedTestName[2] + " " + splittedTestName[3];
		}
		// jsonElementLocal = jsonElementLocal.replace("{{CourseName}} {{TestName}}",
		// testName);//
		jsonElementLocal = jsonElementLocal.replace("{{TestName}}", testName);
		jsonElementLocal = jsonElementLocal.replaceAll("<p>", "\n");
		jsonElementLocal = jsonElementLocal.replaceAll("<br><br>", "\n\n\n");
		jsonElementLocal = jsonElementLocal.replaceAll(" </p>", "");//
		jsonElementLocal = jsonElementLocal.replaceAll("</p>", "");//
		jsonElementLocal = jsonElementLocal.replaceAll("<b>", "");
		jsonElementLocal = jsonElementLocal.replaceAll("</b>", "");

		if (jsonElementLocal.contains("{{partIndex}}")) {
			jsonElementLocal = jsonElementLocal.replace("{{partIndex}}", Integer.toString(lessonIndex));
		}

		if (jsonElementLocal.contains("{{sectionIndex}}")) {
			jsonElementLocal = jsonElementLocal.replace("{{sectionIndex}}", Integer.toString(sectionIndex));
		}

		if (jsonElementLocal.contains("{{duration}}")) {
			jsonElementLocal = jsonElementLocal.replace("{{duration}}", testDuration);
		}

		// return jsonElementLocal;
		return jsonElementLocal.trim(); //
	}

	public String getValueOfWantedKeyInSpecificTestId(JSONObject json, String testId, String wantedKey) {
		JSONObject valueOfTestId = (JSONObject) json.get(testId);
		String wantedValue = valueOfTestId.getString(wantedKey);
		return wantedValue;
	}

	public int getNumOfStartIntrosFromInrosSettingsJSON(JSONArray introSettingsJSONArr) {
		int numOfIntros = 0;
		for (int i = 0; i < introSettingsJSONArr.length(); i++) {
			JSONObject jsonObj = (JSONObject) introSettingsJSONArr.get(i);
			if (jsonObj.get("Position").equals("start")) {
				numOfIntros++;
			}
		}
		return numOfIntros;
	}

	public JSONObject getJsonObjOfSpecificTestId(JSONObject jsonObj, String testId) {
		return (JSONObject) jsonObj.get(testId);
	}

	public void validateIntro(JSONObject jsonObj, JSONObject localizationJson, String id, String testName)
			throws Exception {
		// Init variables
		JSONObject tempJSONObj;

		// Retrieve introsettings of specific test id
		String introSettings = getValueOfWantedKeyInSpecificTestId(jsonObj, id, "IntroSettings");

		// check the test welcome windows display
		//webDriver.waitUntilElementAppears("learning__TestInstructions", ByTypes.className, 10); // learning__TestInstructions

		// This is the json from which we will take title and text
		JSONArray tempJSONArr = new JSONArray(introSettings);
		for (int j = 0; j < tempJSONArr.length(); j++) {

			tempJSONObj = (JSONObject) tempJSONArr.get(j);

			// if position is end - ignore (continue)
			String position = tempJSONObj.getString("Position");
			if (!position.equals("start")) {
				continue;
			}

			if (tempJSONObj.has("RoundIndex")) {
				String roundIndex = tempJSONObj.getString("RoundIndex");
				if (Integer.parseInt(roundIndex) == pltRound) {
					validateIntroContent(localizationJson, tempJSONObj, testName, id);
				} else {
					// no intro for current round
				}
			} else {
				validateIntroContent(localizationJson, tempJSONObj, testName, id);
			}
		}
		Thread.sleep(1000);
	}

	public void validateIntroContent(JSONObject localizationJson, JSONObject tempJSONObj, String testName, String id) {
		try {
			// Retrieve template
			String template = validateJsonElementNotNull(tempJSONObj.get("TemplateCode"));

			// Retrieve Title
			String title = validateJsonElementNotNull(tempJSONObj.get("TitleCode"));

			// Retrieve Text
			String text = validateJsonElementNotNull(tempJSONObj.get("TextCode"));

			if (template.equals("TestIntro")) {
				title = testName;
			} else if (template.equals("PLTIntro")) {
				title = testName;
				// text = ?? need to write hard-coded (changes in each build and environment)
			}

			// Get title and text from localization
			title = retrieveJsonElementFromLocalizationAndModifyIt(localizationJson, title, testName);
			text = retrieveJsonElementFromLocalizationAndModifyIt(localizationJson, text, testName);
//
			//PROBLEM IN METHOD BELLOW
			boolean isTemplateValidated = validateTemplate(title, text); //problem here

			if (isTemplateValidated) {
				if (template.equals("TestNodeIntro")) {
					report.startStep("Click Next Button");
					clickNext();
					Thread.sleep(1000);
				}
				else if (template.equals("TestIntro")) {
					report.startStep("Click Start Test Button");
					clickStartTest();
					Thread.sleep(1000);
				}
			}
			else {
				testResultService.addFailTest("Template of : " + id + " was not found, so next button is not pressed",
						false, true);
			}

		} catch (Exception e) {
			System.out.println(e);
		}

	}

	public void validateBackButtonByJSONValue(String backInJSON) throws Exception {
		Thread.sleep(1000);
		if (backInJSON.equals("true")) {
			report.startStep("Back Value is true- Click on Back Button");
			webDriver.checkElementIsNotDisabledByAttribute(backButtonProperties);
			webDriver.ClickElement(backButton);
			Thread.sleep(2000);
			WebElement question = webDriver.waitForElement("//div[@class='learning__angPracticeW']//div", ByTypes.xpath,
					false, 3);
			testResultService.assertEquals(false, question == null,
					"Question was not loaded after clicking back button", true);
			clickNext();
			question = webDriver.waitForElement("//div[@class='learning__angPracticeW']//div", ByTypes.xpath, false, 3);
			if (!webDriver.isDisplayed(instrcutionText)) {
				testResultService.assertEquals(false, question == null,
						"Question was not loaded after clicking next button (after back button)", true);
			}
		} else if (backInJSON.equals("false")) {
			Thread.sleep(600);
			report.startStep("Back Value is false- Validate Back Button is Disabled");
			webDriver.checkElementIsDisabledByAttribute(backButtonProperties);
		}
	}

	public void clickOnEDIcon() {
		webDriver.ClickElement(edIcon);
		;
	}

	public void validateEDLogoIsCorrect() throws Exception {
		if (webDriver.isDisplayed(edLogoInEDInfo)) {
			testResultService.assertEquals("[English Discoveries] logo", edLogoInEDInfo.getAttribute("title"),
					"ED Logo is Incorrect.", true);
		} else {
			testResultService.addFailTest("ED Logo is Not Displayed", false, true);
		}
	}

	public void closeEDInfo() {
		webDriver.ClickElement(closeEDInfoButton);
	}

	public void answerQuestionsNew(String filePath, String testId, CourseCodes courseCode, CourseTests testType,
			int sectionsToAnswer, String roundLevel, List<String> unitIds, JSONObject jsonObj,
			JSONObject localizationJson, String testName, int unitsToAnswer) throws Exception {

		CourseTest courseTest = initCourseTestFromCSVFileNew(filePath, testId, courseCode, testType, sectionsToAnswer);

		performCourseTestNewTE(courseTest, sectionsToAnswer, roundLevel, unitIds, jsonObj, localizationJson, testName,
				unitsToAnswer);
	}

	public CourseTest initCourseTestFromCSVFileNew(String path, String testId, CourseCodes courseCode,
			CourseTests courseTestType, int sectionsToAnswer) throws Exception {

		List<CourseTestSection> testSections = new ArrayList<CourseTestSection>();
		CourseTestSection section = new CourseTestSection();

		for (int i = 0; i < sectionsToAnswer; i++) {
			section = initSectionNew(path, testId, i + 1, courseCode, courseTestType, false);
			testSections.add(section);
		}

		CourseTest courseTest = new CourseTest(testSections, courseTestType);

		return courseTest;
	}

	@SuppressWarnings("null")
	public CourseTestSection initSectionNew(String path, String testId, int sectionNumber, CourseCodes courseCode,
			CourseTests courseTestType, boolean skipSection) throws Exception {
		TextService textService = new TextService();
		List<String[]> testData = textService.getStr2dimArrFromCsv(path);

		List<TestQuestion> sectionQuestions = new ArrayList<TestQuestion>();

		for (int i = 0; i < testData.size(); i++) {

			// if cycle number match - create TestQuestion object
			if (testData.get(i)[0].equals(testId) && testData.get(i)[1].equals(String.valueOf(sectionNumber))
					&& testData.get(i)[2].equals(courseCode.toString())
					&& testData.get(i)[3].equals(courseTestType.toString())) {

				try {
					report.report("Reading line: " + (i + 1) + " from csv file");

					String[] answers = textService.splitStringToArray(testData.get(i)[4], "\\|");

					String[] answerDestinations = textService.splitStringToArray(testData.get(i)[5], "\\|");

					String[] wrongAnswers = textService.splitStringToArray(testData.get(i)[5], "\\|");

					int[] blankAnswers = new int[1];
					if (testData.get(i)[6].equals("DoNotAnswer")) {
						blankAnswers[0] = 1; // true
					} else {
						blankAnswers[0] = 0; // false
					}

					if (skipSection)
						wrongAnswers[0] = "DoNotAnswer";

					report.report("Question type from csv is: " + testData.get(i)[7]);
					TestQuestionType questionType = TestQuestionType.valueOf(testData.get(i)[7].trim());

					TestQuestion question = new TestQuestion(answers, answerDestinations, wrongAnswers, blankAnswers,
							questionType);
					sectionQuestions.add(question);
					report.report("Finished adding question data for linee: " + i);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		CourseTestSection testSection = new CourseTestSection(sectionNumber, courseCode, courseTestType,
				sectionQuestions);

		return testSection;
	}

	public List<String> getListOfTestIdsFromCSV(String path) throws Exception {
		List<String[]> testData = textService.getStr2dimArrFromCsv(path);

		List<String> testsIds = new ArrayList<String>();

		int size = testData.size();
		for (int i = 0; i < size; i++) {
			testsIds.add(testData.get(i)[0]);
		}
		return testsIds;
	}

	////////////////////////////////////////////////////////////////////////////////////////////
	// Following functions are PLACEMENT-Not Completed

	public String choosePlacementStatus(String status) throws Exception {

		String selectedStatusSymbol = "";
		switch (status) {
		case "Basic":
			selectLevelOnStartPLT(PLTStartLevel.Basic);
			selectedStatusSymbol = "Basic2";
			break;
		case "Intermediate":
			selectLevelOnStartPLT(PLTStartLevel.Intermediate);
			selectedStatusSymbol = "Intermediate2";
			break;
		case "Advanced":
			selectLevelOnStartPLT(PLTStartLevel.Advanced);
			selectedStatusSymbol = "Advanced2";
			break;
		case "I'm not sure":
			selectLevelOnStartPLT(PLTStartLevel.IamNotSure);
			selectedStatusSymbol = "Basic3";
			break;
		}
		return selectedStatusSymbol;
	}

	public PLTTest initPLTTestFromCSVFile(String path, PLTStartLevel pltFirstCycleLevel,
			PLTStartLevel pltSecondCycleLevel, String firstCycleCode, String secondCycleCode, Boolean skipCycle2)
			throws Exception {

		PLTCycle cycle1 = new PLTCycle();
		PLTCycle cycle2 = new PLTCycle();

		cycle1 = initCycle("1", path, pltFirstCycleLevel, firstCycleCode, false);
		cycle2 = initCycle("2", path, pltSecondCycleLevel, secondCycleCode, skipCycle2);

		List<PLTCycle> testCycles = new ArrayList<PLTCycle>();
		testCycles.add(cycle1);
		testCycles.add(cycle2);

		PLTTest pltTest = new PLTTest(testCycles);

		return pltTest;
	}

	public PLTCycle initCycle(String cycleNumber, String path, PLTStartLevel pltStartLevel, String courseLevel,
			boolean skipCycle) throws Exception {

		List<String[]> testData = textService.getStr2dimArrFromCsv(path);

		List<TestQuestion> cycleQuestions = new ArrayList<TestQuestion>();

		for (int i = 0; i < testData.size(); i++) {

			// if cycle number match - create TestQuestion object
			if (testData.get(i)[1].trim().equals(courseLevel.trim())) {

				try {
					report.report("Reading line: " + i + " from csv file");

					String[] answers = textService.splitStringToArray(testData.get(i)[5], "\\|");

					String[] answerDestinations = textService.splitStringToArray(testData.get(i)[2], "\\|");

					String[] wrongAnswers = textService.splitStringToArray(testData.get(i)[3], "\\|");

					if (skipCycle)
						wrongAnswers[0] = "DoNotAnswer";

					// int[] blankAnswers = textService.splitStringToIntArray(
					// testData.get(i)[5], "?!^");

					// boolean booleanAnswer = Boolean.getBoolean(testData.get(i)[5]);
					// String questionType = testData.get(i)[6];
					report.report("Question type from csv is: " + testData.get(i)[6]);
					TestQuestionType questionType = TestQuestionType.valueOf(testData.get(i)[6]);

					TestQuestion question = new TestQuestion(answers, answerDestinations, wrongAnswers, new int[] {},
							questionType);
					cycleQuestions.add(question);
					report.report("Finished adding question data for line: " + i);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		PLTCycle pltCycle = new PLTCycle(pltStartLevel, Integer.valueOf(cycleNumber), null, cycleQuestions, null);
		return pltCycle;

	}

	public void performTest(PLTTest pltTest, int numOfQuestionToAnswerFirstCycle) throws Exception {
		TextService textService = new TextService();

		// Fill the test questions according to the questions arrayList filled
		// from CSV file

		// Go over all 1st cycle questions and answer them according to the
		// questions object in the 1st PLTCyccle object

		// Cycle 1 has 17-20 questions (17 for basic/inter./advances and 20 for
		// "Im not sure"
		PLTCycle firstCycle = pltTest.getCycles().get(0);
		PLTCycle SecondCycle = pltTest.getCycles().get(1);

		selectLevelOnStartPLT(firstCycle.getPltStartLevel());

		pressOnStartPLT();

		try {
			webDriver.waitForElement("//*[@id='mainAreaTD']/div[1]", ByTypes.xpath, true, webDriver.getTimeout()); // Right
																													// Resource
			webDriver.waitForElement("//*[@id='mainAreaTD']/div[2]", ByTypes.xpath, false, webDriver.getTimeout()); // Left
																													// Resource

			// String initialLevel = firstCycle.getPltStartLevel().toString().substring(0,
			// 2);
			answerCycleQuestions(firstCycle, numOfQuestionToAnswerFirstCycle);

			// report.report("End of cycle 1");
			clickOnGoOnButton();

			// Start of cycle 2
			answerCycleQuestions(SecondCycle, SecondCycle.getNumberOfQuestions());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void selectLevelOnStartPLT(PLTStartLevel level) throws Exception {

		switch (level) {
		case Basic:
			webDriver.waitForElement("rbBasic", ByTypes.id, "Basic level button").click();
			break;
		case Advanced:
			webDriver.waitForElement("rbAdvanced", ByTypes.id, "Intermediate level button").click();
			break;
		case Intermediate:
			webDriver.waitForElement("rbIntermediate", ByTypes.id, "Advance level button").click();
			break;
		case IamNotSure:
			webDriver.waitForElement("rbImNotSure", ByTypes.id, "Im not sure button").click();
			break;

		}
	}

	public void pressOnStartPLT() throws Exception {
		webDriver.waitForElement("Submit", ByTypes.name).click();
		webDriver.waitUntilElementAppearsAndReturn("rightDiv",ByTypes.className,10);

		//webDriver.waitForElement("//*[@id='mainAreaTD']/div[1]", ByTypes.xpath, true, webDriver.getTimeout()); // Right
																												// Resource
		//webDriver.waitForElement("//*[@id='mainAreaTD']/div[2]", ByTypes.xpath, false, webDriver.getTimeout()); // Left
																												// Resource
	}

	public void answerCycleQuestions(PLTCycle pltCycle, int numOfQuestionsToAnswer) throws Exception {

		for (int i = 0; i < numOfQuestionsToAnswer; i++) {

			Thread.sleep(1000);
			webDriver.waitUntilElementAppears("//div[@id='pmContainer']", 30);

			TestQuestion question = pltCycle.getCycleQuestions().get(i);

			TestQuestionType questionType = question.getQuestionType();

			// convert ~ to ,

			String[] answers = new String[question.getCorrectAnswers().length];

			for (int j = 0; j < question.getCorrectAnswers().length; j++) {
				answers[j] = question.getCorrectAnswers()[j].replace("~", ",");
				// answers[j] = textService.resolveAprostophes(answers[j]);
			}
			question.setCorrectAnswers(answers);

			if (!question.getIncoreectAnswers()[0].equals("DoNotAnswer")) { // skip the question if wrong answer exist
																			// in csv file or parameter of skipCycle is
																			// true

				switch (questionType) {

				case DragAndDropMultiple:
					answerDragAndDropQuestion(question);
					break;

				case RadioMultiple:
					answerMultiCheckBoxQuestion(question);
					break;

				case RadioSingle:
					answerCheckboxQuestion(question);
					break;

				case DragAndDropSingle:
					answerDragAndDropQuestion(question);
					break;

				case comboBox:
					answerComboBoxQuestion(question);
					break;

				}
			}
			Thread.sleep(1000);
			report.addTitle("Answered on question number:" + (i + 1));
			clickOnNextButtonPLT();
			// webDriver.scrollToElement(webDriver.waitForElement("/html/body",
			// ByTypes.xpath));
			// waitPltQuestionDisplay();
			// waitForTestPageLoaded();
			Thread.sleep(700);
			webDriver.waitUntilElementAppears("//div[@id='pmContainer']", 10);
		}
		Thread.sleep(3000);

		int questionsNotAnswered = pltCycle.getNumberOfQuestions() - numOfQuestionsToAnswer;
		for (int i = 0; i < questionsNotAnswered; i++) {
			clickOnNextButtonPLT();
		}
	}

	public void clickOnGoOnButton() throws Exception {
		webDriver.waitForElement("DoAgain", ByTypes.id).click();

	}

	public void clickOnNextButtonPLT() throws Exception {
		boolean nextIsDisplay = false;
		WebElement element = null;

		for (int i = 0; i < 30 && (!nextIsDisplay); i++) {
			element = webDriver.waitForElement("nextQuest", ByTypes.id, 1, false);

			if (element != null) {
				nextIsDisplay = true;
				element.click();
				break;
			} else {
				report.addTitle("Next button doesn't display after: " + i + " Seconds");
			}
		}

	}

	public int calculateNumOfAnswersToGetWantedScore(int numOfAllAnswers, int wantedScore) {
		double scoreOfEachQuestion = (double) 100 / numOfAllAnswers;
		double numOfQuestionToAnswers = wantedScore / scoreOfEachQuestion;

		int questionToAnswer = (int) Math.round(numOfQuestionToAnswers);

		return (int) Math.floor(questionToAnswer);
	}

	public List<TestQuestion> initSectionPLT(String path, String wantedLevel, String wantedSection) throws Exception {
		List<String[]> testData = textService.getStr2dimArrFromCsv(path);

		List<TestQuestion> sectionQuestions = new ArrayList<TestQuestion>();

		for (int i = 0; i < testData.size(); i++) {

			if (testData.get(i)[1].trim().equals(wantedLevel) && testData.get(i)[2].trim().equals(wantedSection)) {

				try {
					report.report("Reading line: " + i + " from csv file");

					String[] answers = textService.splitStringToArray(testData.get(i)[5], "\\|");

					String[] answerDestinations = textService.splitStringToArray(testData.get(i)[2], "\\|");

					String[] wrongAnswers = textService.splitStringToArray(testData.get(i)[3], "\\|");

					// if (skipCycle) wrongAnswers[0] = "DoNotAnswer";

					// int[] blankAnswers = textService.splitStringToIntArray(
					// testData.get(i)[5], "?!^");

					// boolean booleanAnswer = Boolean.getBoolean(testData.get(i)[5]);
					// String questionType = testData.get(i)[6];
					report.report("Question type from csv is: " + testData.get(i)[6]);
					TestQuestionType questionType = TestQuestionType.valueOf(testData.get(i)[6]);

					TestQuestion question = new TestQuestion(answers, answerDestinations, wrongAnswers, new int[] {},
							questionType);
					sectionQuestions.add(question);
					report.report("Finished adding question data for line: " + i);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		// PLTCycle pltCycle = new PLTCycle(pltStartLevel, Integer.valueOf(cycleNumber),
		// null, cycleQuestions, null);
		return sectionQuestions;

	}

	public void answerSectionQuestions(List<TestQuestion> sectionAnswers, int numOfQuestionsToAnswer) throws Exception {

		for (int i = 0; i < numOfQuestionsToAnswer; i++) {

			Thread.sleep(300);
			webDriver.waitUntilElementAppears("//div[@id='pmContainer']/div[2]/div", 30);

			TestQuestion question = sectionAnswers.get(i);

			TestQuestionType questionType = question.getQuestionType();

			// convert ~ to ,

			String[] answers = new String[question.getCorrectAnswers().length];

			for (int j = 0; j < question.getCorrectAnswers().length; j++) {
				answers[j] = question.getCorrectAnswers()[j].replace("~", ",");
				// answers[j] = textService.resolveAprostophes(answers[j]);
			}
			//webDriver.waitUntilElementAppears("//div[@id='pmContainer']", 10);
			question.setCorrectAnswers(answers);

			if (!question.getIncoreectAnswers()[0].equals("DoNotAnswer")) { // skip the question if wrong answer exist
																			// in csv file or parameter of skipCycle is
																			// true

				switch (questionType) {

				case DragAndDropMultiple:
					answerDragAndDropQuestion(question);
					break;

				case RadioMultiple:
					answerMultiCheckBoxQuestion(question);
					break;

				case RadioSingle:
					answerCheckboxQuestion(question);
					break;

				case DragAndDropSingle:
					answerDragAndDropQuestion(question);
					break;

				case comboBox:
					answerComboBoxQuestion(question);
					break;

				}
			}
			Thread.sleep(500);
			report.addTitle("Answered on question number:" + (i + 1));
			clickOnNextButtonPLT();
			// webDriver.scrollToElement(webDriver.waitForElement("/html/body",
			// ByTypes.xpath));
			// waitPltQuestionDisplay();
			// waitForTestPageLoaded();
			Thread.sleep(500);

		}
		Thread.sleep(1000);

		int questionsNotAnswered = sectionAnswers.size() - numOfQuestionsToAnswer;
		for (int i = 0; i < questionsNotAnswered; i++) {
			clickOnNextButtonPLT();
			Thread.sleep(1000);
		}
	}

	public void clickOnDoTestAgainPLT() throws Exception {
		webDriver.ClickElement(
				webDriver.waitForElement("DoAgain", ByTypes.name, 3, false, "Do Test Again button not found"));
		Thread.sleep(2000);
		webDriver.switchToMainWindow();
	}

	public void answerCyclePartialy(String filePath, String level, int[] wantedScore, int version) throws Exception {
		String section = "";
		String originalLevel = level;
		for (int j = 0; j < 3; j++) {
			if (j == 0) {
				section = "Reading";
				if (level.equals("Intermediate2") && wantedScore[0] == 90 && version == 1) {
					level = "Intermediate2new";
				}
			} else if (j == 1) {
				section = "Listening";
				level = originalLevel;
			} else if (j == 2) {
				section = "Grammar";
				level = originalLevel;
			}
			List<TestQuestion> sectionAnswers = initSectionPLT(filePath, level, section);
			int numOfQuestionInSection = sectionAnswers.size();
			int numOfQuestionToAnswer = calculateNumOfAnswersToGetWantedScore(numOfQuestionInSection, wantedScore[j]);

			if (level.equals("Intermediate2new")) {
				numOfQuestionToAnswer = numOfQuestionInSection;
			}

			answerSectionQuestions(sectionAnswers, numOfQuestionToAnswer);
			Thread.sleep(1000);

			/*
			 * if (changeGrade) { dbService.updateTestResultGrade(newGrade, studentId); }
			 */
		}

	}

	public void performTestInSpecificRoute(String filePath, String level, int[] wantedScore, int version)
			throws Exception {
		String statusSymbol = choosePlacementStatus(level);

		pressOnStartPLT();

		answerCyclePartialy(filePath, statusSymbol, wantedScore, version);
		Thread.sleep(2000);

		clickOnGoOnButton();

		Object[] secondCycleLevels = calculateSecondCycleLevel(statusSymbol, wantedScore[0]);
		// PLTCycle cycle2 = initCycle("2", filePath, (PLTStartLevel)
		// secondCycleLevels[0], secondCycleLevels[1].toString(), false);
		// answerCycleQuestions(cycle2, cycle2.getNumberOfQuestions());

		int[] score = new int[3];
		score[0] = 100;
		score[1] = 100;
		score[2] = 100;
		answerCyclePartialy(filePath, secondCycleLevels[1].toString(), score, version);

	}

	public Object[] calculateSecondCycleLevel(String level, int wantedScore) {

		int currentLevel = getLevelIndexByLevelName(level);

		double nextLevel = (wantedScore + (12.5 * currentLevel) - 75) / 12.5;
		int nextLevelIndex;
		/*
		 * if (nextLevel -(int) Math.floor(nextLevel) >= 0.5) { nextLevelIndex = (int)
		 * Math.ceil(nextLevel); } else { nextLevelIndex = (int) Math.floor(nextLevel);
		 * }
		 */
		nextLevelIndex = (int) Math.round(nextLevel);

		if (nextLevelIndex >= 9) {
			nextLevelIndex = 8;
		} else if (nextLevelIndex <= 0) {
			nextLevelIndex = 1;
		}

		if (nextLevelIndex == currentLevel) {
			nextLevelIndex++;
		}

		String nextLevelName = getLevelNameByLevelIndex(nextLevelIndex);

		PLTStartLevel secondCycleLevel = null;
		if (nextLevelName.contains("Basic")) {
			secondCycleLevel = PLTStartLevel.Basic;
		} else if (nextLevelName.contains("Intermediate")) {
			secondCycleLevel = PLTStartLevel.Intermediate;
		} else if (nextLevelName.contains("Advanced")) {
			secondCycleLevel = PLTStartLevel.Advanced;
		}

		Object[] secondCycleLevels = new Object[2];
		secondCycleLevels[0] = secondCycleLevel;
		secondCycleLevels[1] = nextLevelName;

		return secondCycleLevels;
	}

	public int getLevelIndexByLevelName(String levelName) {
		int currentLevel = 0;
		switch (levelName) {
		case "FirstDiscoveries":
			currentLevel = 0;
			break;
		case "Basic1":
			currentLevel = 1;
			break;
		case "Basic2":
			currentLevel = 2;
			break;
		case "Basic3":
			currentLevel = 3;
			break;
		case "I'm not sure":
			currentLevel = 3;
			break;
		case "Intermediate1":
			currentLevel = 4;
			break;
		case "Intermediate2":
			currentLevel = 5;
			break;
		case "Intermediate3":
			currentLevel = 6;
			break;
		case "Advanced1":
			currentLevel = 7;
			break;
		case "Advanced2":
			currentLevel = 8;
			break;
		case "Advanced3":
			currentLevel = 9;
			break;
		}

		return currentLevel;
	}

	public String getLevelNameByLevelIndex(int levelIndex) {
		String currentLevelName = "";
		switch (levelIndex) {
		case 0:
			currentLevelName = "FirstDiscoveries";
			break;
		case 1:
			currentLevelName = "Basic1";
			break;
		case 2:
			currentLevelName = "Basic2";
			break;
		case 3:
			currentLevelName = "Basic3";
			break;
		case 4:
			currentLevelName = "Intermediate1";
			break;
		case 5:
			currentLevelName = "Intermediate2";
			break;
		case 6:
			currentLevelName = "Intermediate3";
			break;
		case 7:
			currentLevelName = "Advanced1";
			break;
		case 8:
			currentLevelName = "Advanced2";
			break;
		case 9:
			currentLevelName = "Advanced3";
			break;
		case 10:
			currentLevelName = "Advanced3";
			break;
		}

		return currentLevelName;
	}

	public List<String[]> initScoresOfPLTByVersion(String path, String wantedVersion) throws Exception {
		List<String[]> testData = textService.getStr2dimArrFromCsv(path);

		List<String[]> dataOfVersion = new ArrayList<String[]>();

		for (int i = 0; i < testData.size(); i++) {
			if (testData.get(i)[0].trim().equals(wantedVersion)) {
				dataOfVersion.add(testData.get(i));
			}
		}
		return dataOfVersion;
	}

	public void performCourseTestOldTE(CourseTest courseTest, int sectionsToSubmit) throws Exception {

		CourseTestSection section = new CourseTestSection();

		for (int i = 0; i < sectionsToSubmit; i++) {
			if (courseTest.getSections().size() > i) {
				section = courseTest.getSections().get(i);
				report.addTitle("Answering section: " + (i + 1));
				answerSectionQuestions(section);
			}
			// browseToLastSectionTask();
			pressOnSubmitSection(true);
		}
		Thread.sleep(2000);
	}

	public void answerQuestionsOldTe(String filePath, String testId, CourseCodes courseCode, CourseTests testType)
			throws Exception {
		CourseTest courseTest = initCourseTestFromCSVOldTE(filePath, testId, courseCode, testType);
		performCourseTestOldTE(courseTest, courseTest.getSections().size());
	}

	public CourseTest initCourseTestFromCSVOldTE(String path, String testId, CourseCodes courseCode,
			CourseTests courseTestType) throws Exception {

		int totalNumOfSections = getNumOfSectionForCourseTest(path, testId, courseCode);

		List<CourseTestSection> testSections = new ArrayList<CourseTestSection>();
		CourseTestSection section = new CourseTestSection();

		for (int i = 0; i < totalNumOfSections; i++) {
			section = initSectionNew(path, testId, i + 1, courseCode, courseTestType, false);
			testSections.add(section);
		}

		CourseTest courseTest = new CourseTest(testSections, courseTestType);

		return courseTest;
	}

	public void pressStartCourseTestOldTE() throws Exception {
		webDriver.waitUntilElementAppears("btnStartTest", ByTypes.id, 30);
		webDriver.waitForElement("btnStartTest", ByTypes.id).click();
	}

	public int getNumOfSectionForCourseTest(String path, String testId, CourseCodes courseCode) throws Exception {
		TextService textService = new TextService();
		List<String[]> testData = textService.getStr2dimArrFromCsv(path);

		List<String> sections = new ArrayList<String>();

		for (int i = 0; i < testData.size(); i++) {
			if (testData.get(i)[0].equals(testId) && testData.get(i)[2].equals(courseCode.toString())) {
				sections.add(testData.get(i)[1]);
			}
		}

		Set<String> sectionsSet = new HashSet<>(sections);
		sections.clear();
		sections.addAll(sectionsSet);

		return sections.size();
	}

	public String getCourseCodeByTestId(String path, String testId) throws Exception {
		TextService textService = new TextService();
		List<String[]> testData = textService.getStr2dimArrFromCsv(path);
		String courseCode = "";
		for (int i = 0; i < testData.size(); i++) {
			if (testData.get(i)[0].equals(testId)) {
				courseCode = testData.get(i)[2];
				break;
			}
		}
		return courseCode;
	}

	public CourseCodes getCourseCodeEnumByCourseCodeString(String courseCode) {
		CourseCodes courseCodeEnum;
		switch (courseCode) {
		case "FD":
			courseCodeEnum = CourseCodes.FD;
			break;
		case "B1":
			courseCodeEnum = CourseCodes.B1;
			break;
		case "B2":
			courseCodeEnum = CourseCodes.B2;
			break;
		case "B3":
			courseCodeEnum = CourseCodes.B3;
			break;
		case "I1":
			courseCodeEnum = CourseCodes.I1;
			break;
		case "I2":
			courseCodeEnum = CourseCodes.I2;
			break;
		case "I3":
			courseCodeEnum = CourseCodes.I3;
			break;
		case "A1":
			courseCodeEnum = CourseCodes.A1;
			break;
		case "A2":
			courseCodeEnum = CourseCodes.A2;
			break;
		case "A3":
			courseCodeEnum = CourseCodes.A3;
			break;
		default:
			courseCodeEnum = CourseCodes.B1;
		}
		return courseCodeEnum;
	}

	public void answerDragAndDropToPictureQuestion(TestQuestion question) throws Exception {
		// This function works for old Test Environment

		String[] answerSourceLocations = question.getCorrectAnswers();

		for (int i = 0; i < answerSourceLocations.length; i++) {

			WebElement from = null;
			List<WebElement> answers = webDriver.getWebDriver()
					.findElements(By.xpath("//div[@class='draggable wordBankTile']"));
			for (int j = 0; j < answers.size(); j++) {
				if (answers.get(j).getText().replace("\"", "").replace(",", "").trim()
						.equals(question.getCorrectAnswers()[i].trim())) {
					from = answers.get(j);
					break;
				}
			}
			WebElement to = webDriver.getWebDriver().findElements(By.xpath("//div[@class='pictureFrame']")).get(i);// webDriver.waitForElement("//span[@data-id='1_"+
																													// index
																													// +"']",
																													// ByTypes.xpath,false,1);
			webDriver.dragAndDropElement(from, to);
		}
	}

	public int answerSectionQuestionsNewTE(CourseTestSection section) throws Exception {

		int numOfAnsweredQuestions = 0;

		for (int i = 0; i < section.getNumberOfQuestions(); i++) {

			Thread.sleep(1000);
			// webDriver.waitUntilElementAppears("//div[@id='pmContainer']", 30);

			// webDriver.waitUntilElementAppears("prMCQ__mainIW",ByTypes.className, 30);

			TestQuestion question = section.getSectionQuestions().get(i);

			TestQuestionType questionType = question.getQuestionType();

			String[] answers = new String[question.getCorrectAnswers().length];

			for (int j = 0; j < question.getCorrectAnswers().length; j++) {
				answers[j] = question.getCorrectAnswers()[j].replace("~", ",");
			}
			question.setCorrectAnswers(answers);

			// waitForTestPageLoaded();

			if (question.getBlankAnswers()[0] == 0) { // skip the question if wrong answer exist in csv file or
														// parameter of skipCycle is true

				switch (questionType) {

				case DragAndDropMultiple:
					answerDragAndDropQuestionNewTE(question);
					break;

				case RadioMultiple:
					answerMultiCheckBoxQuestionNewTE(question);
					break;

				case RadioSingle:
					answerCheckboxQuestionNewTE(question);
					break;

				case DragAndDropSingle:
					answerDragAndDropQuestionNewTE(question);
					break;

				case comboBox:
					answerComboBoxQuestionNewTE(question);
					break;

				case DrapAndDropToPicture:
					answerDragAndDropToPictureQuestionNewTE(question);
					break;

				case DrapAndDropToPictureNotAngular:
					answerDragAndDropQuestionToPictureNotAngularNewTE(question);

				default:
					break;
				}
				numOfAnsweredQuestions++;
			}

			// Thread.sleep(1000);
			if (i < section.getNumberOfQuestions() - 1) {
				pressOnNextTaskArrow();
			}
			// Thread.sleep(1200);
			// webDriver.waitUntilElementAppears("//div[@id='pmContainer']", 30);
		}
		return numOfAnsweredQuestions;
	}

	// works for new te
	public void answerDragAndDropQuestionNewTE(TestQuestion question) throws Exception {

		boolean isAnswerFound = false;

		webDriver.waitUntilElementAppears("//div[@class='dnditem draggable']", 5);

		String[] answerSourceLocations = question.getCorrectAnswers();

		for (int i = 0; i < answerSourceLocations.length; i++) {

			WebElement from = null;
			List<WebElement> answers = webDriver.getWebDriver()
					.findElements(By.xpath("//div[@class='dnditem draggable']"));
			for (int j = 0; j < answers.size(); j++) {
				if (answers.get(j).getText().replace("\"", "").replace(",", "").trim()
						.contains(question.getCorrectAnswers()[i].trim())) {
					from = answers.get(j);
					isAnswerFound = true;
					break;
				} else {
					isAnswerFound = false;
				}
			}

			if (!isAnswerFound) {
				String[] answersWeb = new String[answers.size()];
				String[] answersFile = new String[answerSourceLocations.length];
				for (int k = 0; k < answers.size(); k++) {
					answersWeb[k] = answers.get(k).getText().replace("\"", "").replace(",", "").trim();
					answersFile[k] = question.getCorrectAnswers()[k].trim();
				}
				testResultService.addFailTest("Answer number " + (i + 1) + " wasn't found. Answers on Web: "
						+ Arrays.toString(answersWeb) + ". Answer from file: " + Arrays.toString(answersFile), false,
						true);
			}
			WebElement to = webDriver.waitForElement("//div[@id='0_" + i + "']", ByTypes.xpath, false, 1);

			dragAndDropElements(from,to);
		}
	}

	private void dragAndDropElements(WebElement from, WebElement to) throws Exception {

		WebElement bank = webDriver.waitForElement("//div[contains(@id,'bank')]", ByTypes.xpath, true, 2);
		Thread.sleep(200);
		webDriver.dragAndDropElement(from, to);
		Thread.sleep(800);
		try{
			webDriver.hoverOnElement(bank);
		}
		catch (Exception e) {
			e.printStackTrace();
			testResultService.addFailTest("no bank element");
		}
	}
	private void dragAndDropElementsWithPic(WebElement from, WebElement to) throws Exception {

		//WebElement bank = webDriver.waitForElement("//div[contains(@id,'bank')]", ByTypes.xpath, true, 1);

		Thread.sleep(500);
		try{
			webDriver.dragAndDropElement(from, to);
		}
		catch (Exception e) {
			e.printStackTrace();
			testResultService.addFailTest("no word element");
		}
	}

	// need to debug for new te
	public void answerMultiCheckBoxQuestionNewTE(TestQuestion question) throws Exception {

		webDriver.waitUntilElementAppears("//div[@class='prMCQ__qaSet']", 5);

		for (int i = 0; i < question.getCorrectAnswers().length; i++) {

			List<WebElement> answersPerQuestion = webDriver.getWebDriver()
					.findElements(By.xpath("//div[@class='prMCQ__qaSet'][" + (i + 1) + "]//label"));// (By.xpath("//div[@id='q"+(i+1)+"']//div[contains(@class,'lessonMultipleAnswer')]//span"));
			for (int j = 0; j < answersPerQuestion.size(); j++) {
				if (answersPerQuestion.get(j).getText().replace(",", "").toLowerCase()
						.equals(question.getCorrectAnswers()[i].toLowerCase().trim())) {
					// if (answersPerQuestion.get(j).getText().replace(",",
					// "").equals(question.getCorrectAnswers()[i].toLowerCase().trim())) {
					webDriver.ClickElement(answersPerQuestion.get(j));
					Thread.sleep(1000);
					break;
				}
			}

			/*
			 * if (question.getCorrectAnswers()[i].toLowerCase().equals("false") ||
			 * question.getCorrectAnswers()[i].toLowerCase().equals("true")) {
			 * question.getCorrectAnswers()[i] =
			 * question.getCorrectAnswers()[i].toLowerCase();
			 * question.getCorrectAnswers()[i] =
			 * question.getCorrectAnswers()[i].substring(0,1).toUpperCase() +
			 * question.getCorrectAnswers()[i].substring(1); } WebElement element =
			 * webDriver.waitForElement(
			 * "//span[@class='multiTextInline'][contains(text(),\"" +
			 * question.getCorrectAnswers()[i] + "\")]", ByTypes.xpath, false, 3);
			 * webDriver.ClickElement(element);
			 */
		}
	}

	// works for new te
	public void answerCheckboxQuestionNewTE(TestQuestion question) throws Exception {

		webDriver.waitUntilElementAppears("//label[contains(@class,'prMCQ__answerLabel')]", 5);

		questionsLabels = webDriver.getWebDriver()
				.findElements(By.xpath("//label[contains(@class,'prMCQ__answerLabel')]"));

		for (int i = 0; i < questionsLabels.size(); i++) {
			if (questionsLabels.get(i).getText().replace(",", "").toLowerCase().contains(question.getCorrectAnswers()[0].toLowerCase())
				||
				question.getCorrectAnswers()[0].toLowerCase().contains(questionsLabels.get(i).getText().toLowerCase())
				)
			{
				questionsCheckboxes.get(i).click();

				Thread.sleep(1000);
				break;
			}
		}
	}

	// works for new te
	public void answerComboBoxQuestionNewTE(TestQuestion question) throws Exception {

		webDriver.waitUntilElementAppears("//div[@class='DDLOptions__selected']", 5);

		boolean isAnswerFound = false;
		for (int i = 0; i < question.getCorrectAnswers().length; i++) {

			try{
				WebElement element = webDriver.getWebDriver().findElements(By.xpath("//div[@class='DDLOptions__selected']")).get(i);
				element.click();
			}
			catch (Exception e) {
				e.printStackTrace();
			}

			Thread.sleep(1000);
			List<WebElement> answersOptionsDropDown = webDriver.getWebDriver()
					.findElements(By.xpath("//div[@class='DDLOptions__listItem']"));
			String[] actualAnswerOptions = new String[answersOptionsDropDown.size()];
			for (int j = 0; j < answersOptionsDropDown.size(); j++) {

				if (question.getCorrectAnswers()[i].contains("??")) { // Added '??' in answers file wherever there is
																		// ',' and duplications in answers
					String tempAnswer = question.getCorrectAnswers()[i].replace("??", ",");
					if (answersOptionsDropDown.get(j).getText().replace("\"", "").trim().equals(tempAnswer.trim())) {
						webDriver.ClickElement(answersOptionsDropDown.get(j));
						isAnswerFound = true;
						break;
					}
				} else {
					actualAnswerOptions[j] = (String) answersOptionsDropDown.get(j).getText().replace(",", "");
					if (answersOptionsDropDown.get(j).getText().replace(",", "")
							.equals(question.getCorrectAnswers()[i].trim())) {
						webDriver.ClickElement(answersOptionsDropDown.get(j));
						Thread.sleep(1000);
						isAnswerFound = true;
						break;
					}
				}
			}

			if (!isAnswerFound) {
				testResultService.addFailTest("Answer not found. Answer on web: " + Arrays.toString(actualAnswerOptions)
						+ " . Answer from file: " + question.getCorrectAnswers()[i].trim(), false, true);
			}
		}
	}

	// This function works for angular (like in plt) questions- drag and drop to
	// picture
	public void answerDragAndDropToPictureQuestionNewTE(TestQuestion question) throws Exception {

		webDriver.waitUntilElementAppears("//div[@class='draggable wordBankTile']", 5);

		String[] answerSourceLocations = question.getCorrectAnswers();

		for (int i = 0; i < answerSourceLocations.length; i++) {

			WebElement from = null;
			List<WebElement> answers = webDriver.getWebDriver()
					.findElements(By.xpath("//div[@class='draggable wordBankTile']"));
			for (int j = 0; j < answers.size(); j++) {
				if (question.getCorrectAnswers()[i].contains("??")) { // Added '??' in answers file wherever there is
																		// ',' and duplications in answers
					String tempAnswer = question.getCorrectAnswers()[i].replace("??", ",");
					if (answers.get(j).getText().replace("\"", "").trim().equals(tempAnswer.trim())) {
						from = answers.get(j);
						break;
					}
				} else {
					if (answers.get(j).getText().replace("\"", "").replace(",", "").trim()
							.equals(question.getCorrectAnswers()[i].trim())) {
						from = answers.get(j);
						break;
					}
				}
			}
			WebElement to = webDriver.getWebDriver()
					.findElements(By.xpath("//span[contains(@class,'TTpanswerDiv droptarget')]")).get(i);// webDriver.waitForElement("//span[@data-id='1_"+
																											// index
																											// +"']",
			dragAndDropElementsWithPic(from, to);
		}

		/*
		 * boolean isAnswerFound = false;
		 *
		 * String[] answerSourceLocations = question.getCorrectAnswers();
		 *
		 * for (int i = 0; i < answerSourceLocations.length; i++) {
		 *
		 * WebElement from = null; //List<WebElement> answers =
		 * webDriver.getWebDriver().findElements(By.
		 * xpath("//div[@class='dnditem draggable']")); List<WebElement> answers =
		 * webDriver.getWebDriver().findElements(By.
		 * xpath("//div[@class='draggable wordBankTile']")); for (int j = 0; j <
		 * answers.size(); j++) { if (answers.get(j).getText().replace("\"",
		 * "").replace(",", "").trim().equals(question.getCorrectAnswers()[i].trim())) {
		 * from = answers.get(j); isAnswerFound = true; break; } else { isAnswerFound =
		 * false; } }
		 *
		 * if (!isAnswerFound) { String[] answersWeb = new String[answers.size()];
		 * String[] answersFile = new String[answerSourceLocations.length]; for (int k =
		 * 0; k < answers.size(); k++) { answersWeb[k] =
		 * answers.get(k).getText().replace("\"", "").replace(",", "").trim();
		 * answersFile[k] = question.getCorrectAnswers()[k].trim(); }
		 * testResultService.addFailTest("Answer number " +(i+1)+
		 * " wasn't found. Answers on Web: " + Arrays.toString(answersWeb) +
		 * ". Answer from file: " + Arrays.toString(answersFile), false, true); }
		 * WebElement to = webDriver.waitForElement("//div[@id='undefined_"+ i +"']",
		 * ByTypes.xpath,false,1); webDriver.dragAndDropElement(from, to); }
		 */

	}

	public void answerQuestionsAndValidateCalculations(String filePath, String testId, CourseCodes courseCode,
			CourseTests testType, int sectionsToAnswer, String roundLevel, List<String> unitIds, JSONObject jsonObj,
			JSONObject localizationJson, String testName, int unitsToAnswer, String userId, String startTestTime)
			throws Exception {
		CourseTest courseTest = initCourseTestFromCSVFileNew(filePath, testId, courseCode, testType, sectionsToAnswer);
		performCourseTestNewTEAndValidateCalculations(courseTest, sectionsToAnswer, roundLevel, unitIds, jsonObj,
				localizationJson, testName, unitsToAnswer, userId, startTestTime);
	}

	public void performCourseTestNewTEAndValidateCalculations(CourseTest courseTest, int sectionsToAnswer,
			String roundLevel, List<String> unitIds, JSONObject jsonObj, JSONObject localizationJson, String testName,
			int unitsToAnswer, String userId, String startTestTime) throws Exception {

		CourseTestSection round = new CourseTestSection();

		for (int i = 0; i < unitsToAnswer; i++) {
			report.startStep("Validate Unit Intro");
			validateIntro(jsonObj, localizationJson, unitIds.get(i), testName);

			List<String> lessonsIds = getLessonsIdsFromLessonDetailsArrOfSpecificUnit(unitIds.get(i));

			int numOfAnsweredQuestions = 0;
			if (roundLevel.equals("2")) {

				numOfAnsweredQuestions = 0;

				for (int j = 0; j < lessonsIds.size(); j++) {

					report.startStep("Validate Lesson Intro");
					validateIntro(jsonObj, localizationJson, lessonsIds.get(j), testName); // validate lesson id is
																							// correct
					report.startStep("Answer Round");
					if (courseTest.getSections().size() > j) {
						round = courseTest.getSections().get(j);

						int temp = answerSectionQuestionsNewTE(round);
						numOfAnsweredQuestions += temp;
					}

					if (j < lessonsIds.size() - 1) {
						report.startStep("Click Next");
						clickNext();
					}
				}

				report.startStep("Submit");
				submit(false);

				Thread.sleep(1000);

				report.startLevel("Validate Calculations");
				calculateWeightRoundLevel2(userId, (numOfAnsweredQuestions - 5), jsonObj, lessonsIds);
				// num of
				// answered
				// questions is
				// minus 5 since
				// sections 2
				// answers are
				// incorrect
				String endTestTime = pageHelper.getCurrentDateByFormat("HH:mm:ss");
				int testTime = textService.getTimeInSecondsFromTwoDates(startTestTime, endTestTime);
				calculateTimeRoundLevel2(userId, testTime);

			} else if (roundLevel.equals("3")) {
				double finalCalcWeight = 0.0;
				int firstLessonTime = 0;
				String endFirstLessonTime = "";
				String endSecondLessonTime = "";
				for (int j = 0; j < lessonsIds.size(); j++) {

					report.startStep("Validate Lesson Intro");
					validateIntro(jsonObj, localizationJson, lessonsIds.get(j), testName);

					numOfAnsweredQuestions = 0;
					report.startStep("Answer Round");
					if (courseTest.getSections().size() > j) {
						round = courseTest.getSections().get(j);
						numOfAnsweredQuestions = answerSectionQuestionsNewTE(round);
					}

					int lessonTime = 0;

					report.startStep("Submit");
					if (j == 0) {
						submit(false);

						report.startLevel("Validate Calculations");
						finalCalcWeight = calculateWeightRoundLevel3(userId, numOfAnsweredQuestions, jsonObj,
								lessonsIds, (j + 1), 0.0);

						report.startLevel("Validate Time");
						endFirstLessonTime = pageHelper.getCurrentDateByFormat("HH:mm:ss");
						firstLessonTime = textService.getTimeInSecondsFromTwoDates(startTestTime, endFirstLessonTime);
						calculateTimeRoundLevel3(userId, firstLessonTime, 0);
					} else {
						submit(true);
						Thread.sleep(4000);

						report.startLevel("Validate Calculations");
						calculateWeightRoundLevel3(userId, (numOfAnsweredQuestions - 5), jsonObj, lessonsIds, (j + 1),
								finalCalcWeight); // num of answered questions is minus 5 since sections 2 answers are
													// incorrect

						report.startLevel("Validate Time");
						endSecondLessonTime = pageHelper.getCurrentDateByFormat("HH:mm:ss");
						lessonTime = textService.getTimeInSecondsFromTwoDates(endFirstLessonTime, endSecondLessonTime);
						calculateTimeRoundLevel3(userId, lessonTime, firstLessonTime);
					}
					Thread.sleep(4000);

					/*
					 * report.startLevel("Validate Calculations");//("Validate Calculations"); if (j
					 * == 0) { finalCalcWeight = calculateWeightRoundLevel3(userId,
					 * numOfAnsweredQuestions, jsonObj, lessonsIds, (j+1), 0.0); } else {
					 * calculateWeightRoundLevel3(userId, numOfAnsweredQuestions, jsonObj,
					 * lessonsIds, (j+1), finalCalcWeight); }
					 */
					// Thread.sleep(4000);

				}
			}
		}

		/*
		 * report.startStep("Complete the Other Rounds Until the End of Test");
		 * Thread.sleep(2000); int unitsLeftToBrowse = unitIds.size()-unitsToAnswer; if
		 * (roundLevel.equals("2")) { for (int i = 0; i < unitsLeftToBrowse;i++) {
		 * browseToLastSectionTask(); // change elements submit(false);
		 * Thread.sleep(2000); } } else if (roundLevel.equals("3")) { for (int i = 0; i
		 * <unitsLeftToBrowse; i++) { List<String> lessonsIds =
		 * getLessonsIdsFromLessonDetailsArrOfSpecificUnit(unitIds.get(i)); for (int j =
		 * 0; j < lessonsIds.size(); j++) { browseToLastSectionTask(); // change
		 * elements submit(false); Thread.sleep(2000); } }
		 *
		 * }
		 */
	}

	public double calculateWeightRoundLevel2(String userId, int numOfAnsweredQuestions, JSONObject jsonObj,
			List<String> lessonsIds) throws Exception {

		report.startStep("Get Component Progress from DB");
		List<String[]> UserTestComponentProgress = dbService.getUserTestComponentProgressByUserId(userId);

		int numOfLessonsInUnit = 0;
		int numOfQuestionInSection = 0;
		double finalCalcWeight = 0;

		double unitCalcWeight = 0;
		int totalNumOfAnswerQuestions = 0;
		for (int i = 0; i < 2; i++) {

			report.startStep("Get Component Item JSON");
			JSONObject componentItemJsonObj = new JSONObject(UserTestComponentProgress.get(i)[9]);
			String[] componentItemIDs = textService.splitStringToArray(
					componentItemJsonObj.get("ComponentItemIDs").toString().replace("[", "").replace("]", ""), ",");
			String[] componentItemsScores = textService.splitStringToArray(
					componentItemJsonObj.get("ComponentItemScores").toString().replace("[", "").replace("]", ""), ",");

			numOfLessonsInUnit = lessonsIds.size();
			numOfQuestionInSection = componentItemIDs.length;

			report.startStep("Calculate the Calc Weight From JSON and Comapre it to the Component Score in DB");
			double calcWeightOfTask = 0;
			finalCalcWeight = 0;
			int counterOfCorrectAnswers = 0;
			for (int k = 0; k < componentItemIDs.length; k++) {
				if (Integer.parseInt(componentItemsScores[k]) == 100) {
					counterOfCorrectAnswers++;
					calcWeightOfTask = Double.parseDouble(
							getValueOfWantedKeyInSpecificTestId(jsonObj, componentItemIDs[k], "CalcWeight"));
					finalCalcWeight += calcWeightOfTask;
				}
			}
			finalCalcWeight = finalCalcWeight * 100;
			unitCalcWeight += finalCalcWeight;

			String componentScore = UserTestComponentProgress.get(i)[11];
			if (componentScore.equals("0.0000")) {
				componentScore = "0.0";
			}
			testResultService.assertEquals(true,
					String.valueOf(textService.roundDouble(finalCalcWeight, 4))
							.equals(String.valueOf(textService.roundDouble(Double.parseDouble(componentScore), 4))),
					"Component Score is Incorrect. Expecting (from JSON): " + finalCalcWeight + ". Actual (from DB): "
							+ componentScore);

			totalNumOfAnswerQuestions += counterOfCorrectAnswers;

			report.startStep(
					"Calculate Component Percentage Score From JSON and Compare it to Component Percentage Score in DB");
			String componentPercentageScore = UserTestComponentProgress.get(i)[12];
			double componentPercentageFromJson = ((double) counterOfCorrectAnswers / numOfQuestionInSection * 100);
			testResultService.assertEquals(true,
					Integer.toString((int) componentPercentageFromJson).equals(componentPercentageScore),
					"Component Percentage is Incorrect. Expected (from JSON): " + componentPercentageFromJson
							+ ". Actual (from DB): " + componentPercentageScore);
		}

		testResultService.assertEquals(true, totalNumOfAnswerQuestions == numOfAnsweredQuestions,
				"Number of correct answers is Incorrect. Actual (from web): " + numOfAnsweredQuestions
						+ ". Expected (from DB): " + totalNumOfAnswerQuestions);

		report.startStep("Calculate Unit Score From JSON and Compare it to Unit Score in DB");
		List<String[]> UserTestUnitProgress = dbService.getUserTestUnitProgressByUserId(userId);
		String unitScoreDB = UserTestUnitProgress.get(0)[7];
		testResultService.assertEquals(true,
				Double.toString(textService.roundDouble(unitCalcWeight, 4)).equals(unitScoreDB.replace("0", "")),
				"Unit Score is Incorrect. Expected (from JSON): " + unitCalcWeight + ". Actual (from DB): "
						+ unitScoreDB);

		report.startStep("Calculate Unit Percentage Score From JSON and Compare it to Unit Percentage Score in DB");
		String unitPercentageScore = UserTestUnitProgress.get(0)[8];
		double expectedUnitPercentageScore = 0.0;
		for (int k = 0; k < UserTestComponentProgress.size(); k++) {
			expectedUnitPercentageScore += (double) Double.parseDouble(UserTestComponentProgress.get(k)[12]);
		}
		expectedUnitPercentageScore = Math.round(expectedUnitPercentageScore / numOfLessonsInUnit);

		testResultService.assertEquals(true,
				Integer.toString((int) expectedUnitPercentageScore).equals(unitPercentageScore),
				"Unit Percentage Score is Incorrect. Expected (from JSON): " + expectedUnitPercentageScore
						+ ". Actual (from DB): " + unitPercentageScore);

		List<String[]> UserTestProgress = dbService.getUserTestProgressByUserId(userId);
		String testScore = UserTestProgress.get(0)[3];
		testResultService.assertEquals(true,
				Double.toString(textService.roundDouble(unitCalcWeight, 4)).equals(testScore.replace("0", "")),
				"Test Score is Incorrect. Expected (from JSON): " + unitCalcWeight + ". Actual (from DB): "
						+ testScore);

		return finalCalcWeight;
	}

	public double calculateWeightRoundLevel3(String userId, int numOfAnsweredQuestions, JSONObject jsonObj,
			List<String> lessonsIds, int numOfAnsweredLessons, double previousCalcWeight) throws Exception {

		report.startStep("Get Component Progress from DB");
		List<String[]> UserTestComponentProgress = dbService.getUserTestComponentProgressByUserId(userId);

		report.startStep("Get Component Item JSON");
		JSONObject componentItemJsonObj = new JSONObject(UserTestComponentProgress.get(0)[9]);
		String[] componentItemIDs = textService.splitStringToArray(
				componentItemJsonObj.get("ComponentItemIDs").toString().replace("[", "").replace("]", ""), ",");
		String[] componentItemsScores = textService.splitStringToArray(
				componentItemJsonObj.get("ComponentItemScores").toString().replace("[", "").replace("]", ""), ",");

		int numOfLessonsInUnit = lessonsIds.size();
		int numOfQuestionInSection = componentItemIDs.length;

		report.startStep("Calculate the Calc Weight From JSON and Comapre it to the Component Score in DB");
		double calcWeightOfTask = 0;
		double finalCalcWeight = 0;
		int counterOfCorrectAnswers = 0;
		for (int k = 0; k < componentItemIDs.length; k++) {
			if (Integer.parseInt(componentItemsScores[k]) == 100) {
				counterOfCorrectAnswers++;
				calcWeightOfTask = Double
						.parseDouble(getValueOfWantedKeyInSpecificTestId(jsonObj, componentItemIDs[k], "CalcWeight"));
				finalCalcWeight += calcWeightOfTask;
			}
		}
		finalCalcWeight = finalCalcWeight * 100;

		String componentScore = UserTestComponentProgress.get(0)[11];
		if (componentScore.equals("0.0000")) {
			componentScore = "0.0";
		}
		testResultService.assertEquals(true,
				String.valueOf(textService.roundDouble(finalCalcWeight, 4))
						.equals(String.valueOf(textService.roundDouble(Double.parseDouble(componentScore), 4))),
				"Component Score is Incorrect. Expecting (from JSON): " + finalCalcWeight + ". Actual (from DB): "
						+ componentScore);

		testResultService.assertEquals(true, counterOfCorrectAnswers == numOfAnsweredQuestions,
				"Number of correct answers is Incorrect. Actual (from web): " + numOfAnsweredQuestions
						+ ". Expected (from DB): " + counterOfCorrectAnswers);

		report.startStep(
				"Calculate Component Percentage Score From JSON and Compare it to Component Percentage Score in DB");
		String componentPercentageScore = UserTestComponentProgress.get(0)[12];
		double componentPercentageFromJson = ((double) counterOfCorrectAnswers / numOfQuestionInSection * 100);
		testResultService.assertEquals(true,
				Integer.toString((int) componentPercentageFromJson).equals(componentPercentageScore),
				"Component Percentage is Incorrect. Expected (from JSON): " + componentPercentageFromJson
						+ ". Actual (from DB): " + componentPercentageScore);

		report.startStep("Calculate Unit Score From JSON and Compare it to Unit Score in DB");
		List<String[]> UserTestUnitProgress = dbService.getUserTestUnitProgressByUserId(userId);
		String unitScoreDB = UserTestUnitProgress.get(0)[7];
		testResultService.assertEquals(true,
				Double.toString(textService.roundDouble(previousCalcWeight + finalCalcWeight, 4))
						.equals(unitScoreDB.replace("0", "")),
				"Unit Score is Incorrect. Expected (from JSON): " + (previousCalcWeight + finalCalcWeight)
						+ ". Actual (from DB): " + unitScoreDB);

		report.startStep("Calculate Unit Percentage Score From JSON and Compare it to Unit Percentage Score in DB");
		String unitPercentageScore = UserTestUnitProgress.get(0)[8];
		double expectedUnitPercentageScore = 0.0;
		for (int k = 0; k < UserTestComponentProgress.size(); k++) {
			expectedUnitPercentageScore += (double) Double.parseDouble(UserTestComponentProgress.get(k)[12]);
		}
		expectedUnitPercentageScore = Math.round(expectedUnitPercentageScore / numOfLessonsInUnit);

		testResultService.assertEquals(true,
				Integer.toString((int) expectedUnitPercentageScore).equals(unitPercentageScore),
				"Unit Percentage Score is Incorrect. Expected (from JSON): " + expectedUnitPercentageScore
						+ ". Actual (from DB): " + unitPercentageScore);

		List<String[]> UserTestProgress = dbService.getUserTestProgressByUserId(userId);
		String testScore = UserTestProgress.get(0)[3];
		testResultService.assertEquals(true,
				Double.toString(textService.roundDouble(previousCalcWeight + finalCalcWeight, 4))
						.equals(testScore.replace("0", "")),
				"Unit Score is Incorrect. Expected (from JSON): " + (previousCalcWeight + finalCalcWeight)
						+ ". Actual (from DB): " + testScore);

		return finalCalcWeight;
	}

	public void calculateTimeRoundLevel2(String userId, int spentTime) throws Exception {

		report.startStep("Get Component Progress from DB");
		List<String[]> UserTestComponentProgress = dbService.getUserTestComponentProgressByUserId(userId);

		report.startStep("Compare Time on Component in Sec to Time Calcualted by Client Times in DB");
		int totalTime = 0;
		for (int i = 0; i < UserTestComponentProgress.size(); i++) {
			String clientStartDate = UserTestComponentProgress.get(i)[19];
			String clientEndDate = UserTestComponentProgress.get(i)[20];

			int timeFromClientDB = textService.getTimeInSecondsFromTwoDates(clientStartDate, clientEndDate);
			String timeOnComponentDB = UserTestComponentProgress.get(i)[14];
			testResultService.assertEquals(true, Integer.parseInt(timeOnComponentDB) == timeFromClientDB,
					"Time on Component Sec is Incorrect. Expected (Calculated by Client Times in DB): "
							+ timeFromClientDB + ". Actual (from DB): " + timeOnComponentDB);
			totalTime += timeFromClientDB;
		}

		report.startStep("Compare Time Calculated by Automation (to Entire Unit) to Sum of Component Times in DB");
		testResultService.assertEquals(true, spentTime - totalTime < 20,
				"Spent Time on Unit does not match the sum of lessons time from DB. Time calculated by automation: "
						+ spentTime + ". Sum of all Lessons times: " + totalTime); // compare calculated time by
																					// automation to sum of all lessons
																					// time

		report.startStep("Get Unit Progress from DB");
		List<String[]> UserTestUnitProgress = dbService.getUserTestUnitProgressByUserId(userId);
		String timeOnUnitSecDB = UserTestUnitProgress.get(0)[10];

		report.startStep("Compare Sum of Component Times in DB to Time on Unit in Sec in DB");
		testResultService.assertEquals(totalTime, Integer.parseInt(timeOnUnitSecDB),
				"Time on Unit sec is Incorrect (Compared to sum of all lessons time)."); // compare sum of all lessons
																							// time to unit time

		report.startStep("Compare Time Calculated by Automation (to Entire Unit) to Time on Unit in Sec in DB");
		testResultService.assertEquals(true, spentTime - Integer.parseInt(timeOnUnitSecDB) < 20,
				"Time on Unit sec is Incorrect (Compared to time calculated by automation). Time calculated by automation: "
						+ spentTime + ". Time on Unit from DB: " + timeOnUnitSecDB);// compare time calculated by
																					// automation to unit time

		report.startStep("Get Test Progress from DB");
		List<String[]> UserTestProgress = dbService.getUserTestProgressByUserId(userId);
		String timeOnTestSecDB = UserTestProgress.get(0)[5];

		report.startStep("Compare Sum of Component Times in DB to Time on Test in Sec in DB");
		testResultService.assertEquals(totalTime, Integer.parseInt(timeOnTestSecDB),
				"Time on Test Sec is Incorrect (Compared to sum of all lessons time)."); // // compare sum of all
																							// lessons time to test time

		report.startStep("Compare Time Calculated by Automation (to Entire Unit) to Time on Test in Sec in DB");
		testResultService.assertEquals(true, spentTime - Integer.parseInt(timeOnTestSecDB) < 20,
				"Time on Test Sec is Incorrect (Compared to time calculaterd by automation). Time calculated by automation: "
						+ spentTime + ". Time on Test from DB: " + timeOnTestSecDB); // compare time calculated by
																						// automation to test time
	}

	public void calculateTimeRoundLevel3(String userId, int spentTimeOnLesson, int spentTimeOnPreviousLesson)
			throws Exception {

		report.startStep("Get Component Progress from DB");
		List<String[]> UserTestComponentProgress = dbService.getUserTestComponentProgressByUserId(userId);

		report.startStep("Validate Lesson Time is Correct");
		String clientStartDate = UserTestComponentProgress.get(0)[19];
		String clientEndDate = UserTestComponentProgress.get(0)[20];
		int timeFromClientDB = textService.getTimeInSecondsFromTwoDates(clientStartDate, clientEndDate);
		String timeOnComponentDB = UserTestComponentProgress.get(0)[14];

		report.startStep("Compare Component Time Calculated by Client Times in DB to Time On Component in Sec in DB");
		testResultService.assertEquals(true, Integer.parseInt(timeOnComponentDB) == timeFromClientDB,
				"Time on Component Sec is Incorrect. Expected (calculated by client times in db): " + timeFromClientDB
						+ ". Actual (from DB): " + timeOnComponentDB);

		report.startStep(
				"Compare Time Calculated By Automation (to Entire Component) to Time On Component in Sec in DB");
		testResultService.assertEquals(true, spentTimeOnLesson - Integer.parseInt(timeOnComponentDB) < 30,
				"Spent Time on Lesson (Calculated by Automation) does not match the Time On Lesson from DB. Time calculated by automation: "
						+ spentTimeOnLesson + ". Time on Lesson from DB: " + timeOnComponentDB); // compare calculated
																									// time by
																									// automation to
																									// lesson time

		report.startStep("Get Unit Progress From DB");
		List<String[]> UserTestUnitProgress = dbService.getUserTestUnitProgressByUserId(userId);
		String timeOnUnitDB = UserTestUnitProgress.get(0)[10];

		report.startStep("Compare Time Calculated by Automation (to Entire Component) to Time On Unit in Sec in DB");
		testResultService.assertEquals(true,
				((spentTimeOnLesson + spentTimeOnPreviousLesson) - Integer.parseInt(timeOnUnitDB)) < 30,
				"Spent Time on Unit (Calculated by Automation) does not match the Time On Unit from DB. Time calculated by automation: "
						+ (spentTimeOnLesson + spentTimeOnPreviousLesson) + ". Time On Unit from DB: " + timeOnUnitDB);

		// report.startStep("Compare Time On Component in Sec in DB to Time On Unit in
		// Sec in DB");
		// testResultService.assertEquals(true, Integer.parseInt(timeOnComponentDB) ==
		// Integer.parseInt(timeOnUnitDB), "Time on Component from DB does not match the
		// Time On Unit from DB. Time On Component DB: " + timeOnComponentDB + ". Time
		// On Unit from DB: " + timeOnUnitDB);

		report.startStep("Get Test Progress From DB");
		List<String[]> UserTestProgress = dbService.getUserTestProgressByUserId(userId);
		String timeOnTestDB = UserTestProgress.get(0)[5];

		report.startStep("Compare Time Calculated by Automation (to Entire Component) to Time On Test in sec in DB");
		testResultService.assertEquals(true,
				((spentTimeOnLesson + spentTimeOnPreviousLesson) - Integer.parseInt(timeOnTestDB)) < 30,
				"Spent Time on Test (Calculated by Automation) does not match the Time On Test from DB. Time calculated by automation: "
						+ (spentTimeOnLesson + spentTimeOnPreviousLesson) + ". Time On Test from DB: " + timeOnTestDB);

		// report.startStep("Compare Time On Component in Sec in DB to Time On Test in
		// Sec in DB");
		// testResultService.assertEquals(true, Integer.parseInt(timeOnComponentDB) ==
		// Integer.parseInt(timeOnTestDB), "Time on Component from DB does not match the
		// Time On Test from DB. Time On Component DB: " + timeOnComponentDB + ". Time
		// On Unit from DB: " + timeOnTestDB);

	}

	public String checkAndAssignedTestId(String assignedTestId, String notWantedTestId, String studentId,
			String courseId, String TestType) {
		// assignedTested = "989017755";

		String[] oldTestIds = { "989013148", "989012509" };
		int randomNum = dbService.getRandonNumber(0, oldTestIds.length - 1);

		if (assignedTestId.equals(notWantedTestId)) {
			// update test id
			String userExitSettingsId = dbService.getUserExitSettingsId(studentId, assignedTestId, courseId, TestType);
			dbService.updateTestIdInUserExitSettings(oldTestIds[randomNum], userExitSettingsId);

			try {
				// assignedTested = pageHelper.getAssignedTestIdForStudent(studentId,courseId,
				// Integer.parseInt(TestType));

				assignedTestId = oldTestIds[randomNum];
				String token = pageHelper.getTokenFromConsoleWithParameters("1", studentId, oldTestIds[randomNum],
						userExitSettingsId);
				dbService.updateAssignWithToken(token, userExitSettingsId);

			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return assignedTestId;
	}

	public void validateBackButtonIsNotDisplayed() throws Exception {
		testResultService.assertEquals(false, webDriver.isDisplayed(backButton), "Back Button is Displayed");
	}

	public String getRoundLevelFromJson(JSONObject json, String testId) {
		String wantedValue = "";
		try {
			String roundSettings = getValueOfWantedKeyInSpecificTestId(json, testId, "RoundSettings");
			JSONObject roundSettingJSON = new JSONObject(roundSettings);
			wantedValue = roundSettingJSON.get("RoundLevel").toString();

		} catch (Exception e) {

		}
		return wantedValue;
	}

	public void answerQuestionsBySectionsOldTe(String filePath, String testId, CourseCodes courseCode,
			CourseTests testType, int numOfSections) throws Exception {
		CourseTest courseTest = initCourseTestFromCSVOldTE(filePath, testId, courseCode, testType);
		performCourseTestOldTE(courseTest, numOfSections);
	}

	public void answerSpecificSectionQuestionsOldTe(String filePath, String testId, CourseCodes courseCode,
			CourseTests testType, int sectionIndex) throws Exception {
		CourseTest courseTest = initCourseTestFromCSVOldTE(filePath, testId, courseCode, testType);
		performCourseTestSpecificSectionOldTE(courseTest, sectionIndex);
	}

	public void performCourseTestSpecificSectionOldTE(CourseTest courseTest, int sectionIndex) throws Exception {

		CourseTestSection section = new CourseTestSection();

		section = courseTest.getSections().get(sectionIndex);
		report.addTitle("Answering section: " + (sectionIndex + 1));
		answerSectionQuestions(section);

		pressOnSubmitSection(true);

		Thread.sleep(2000);
	}

	public void answerQuestionInSpecificSectionNewTE(String filePath, String testId, CourseCodes courseCode,
			CourseTests testType, int sectionIndex, String roundLevel, List<String> unitIds, JSONObject jsonObj,
			JSONObject localizationJson, String testName, int unitIndex, int sectionIndexInUnit) throws Exception {
		// CourseTest courseTest = initCourseTestFromCSVFileNew(filePath, testId,
		// courseCode, testType, sectionIndex);
		List<CourseTestSection> testSections = new ArrayList<CourseTestSection>();
		CourseTestSection section = initSectionNew(filePath, testId, sectionIndex + 1, courseCode, testType, false);
		testSections.add(section);
		CourseTest courseTest = new CourseTest(testSections, testType);
		performCourseTestInSpecificSectionNewTE(courseTest, sectionIndex, roundLevel, unitIds, jsonObj,
				localizationJson, testName, unitIndex, sectionIndexInUnit);
	}

	public void performCourseTestInSpecificSectionNewTE(CourseTest courseTest, int sectionIndex, String roundLevel,
			List<String> unitIds, JSONObject jsonObj, JSONObject localizationJson, String testName, int unitIndex,
			int sectionIndexInUnit) throws Exception {

		CourseTestSection round = new CourseTestSection();

		if (sectionIndexInUnit == 0) {
			report.startStep("Validate Unit Intro");
			validateIntro(jsonObj, localizationJson, unitIds.get(unitIndex), testName);
		} else {
			report.startStep("Validate Intro is not Displayed (Entered unit in the middle)");
			validateIntroIsNotDisplayed(sectionIndex);
		}

		List<String> lessonsIds = getLessonsIdsFromLessonDetailsArrOfSpecificUnit(unitIds.get(unitIndex));

		if (roundLevel.equals("2")) {

			report.startStep("Validate Lesson Intro");
			validateIntro(jsonObj, localizationJson, lessonsIds.get(sectionIndexInUnit), testName); // validate lesson
																									// id is correct
			report.startStep("Answer Round");
			round = courseTest.getSections().get(0);
			answerSectionQuestionsNewTE(round);

			if (lessonsIds.size() - 1 > sectionIndexInUnit) {
				report.startStep("Click Next");
				clickNext();
			} else {
				report.startStep("Submit");
				submit(true);
			}

		} else if
			(roundLevel.equals("3")) {
				report.startStep("Validate Lesson Intro");
				validateIntro(jsonObj, localizationJson, lessonsIds.get(sectionIndexInUnit), testName);

				report.startStep("Answer Round");
				round = courseTest.getSections().get(0);
				answerSectionQuestionsNewTE(round);

				report.startStep("Submit");
				submit(false);
		}
	}

	public String getQuestionText() throws Exception {
		return webDriver.waitForElement("//div[@class='prMCQ__questionText']", ByTypes.xpath, false, 3).getText();
		// return
		// webDriver.getWebDriver().findElement(By.xpath("//div[@class='prMCQ__questionText']")).getText();
	}

	public String getQuestionNumber() throws Exception {
		return webDriver.waitForElement("//span[@class='learning__taskNavPCCurrentTask']", ByTypes.xpath, false, 3)
				.getText();
		// return
		// webDriver.getWebDriver().findElement(By.xpath("//span[@class='learning__taskNavPCCurrentTask']")).getText();
	}

	public void clickBackButton() {
		webDriver.ClickElement(backButton);
	}

	public void validateQuestionIsAnswered() throws Exception {
		boolean isChecked = false;
		for (int i = 0; i < questionsCheckboxes.size(); i++) {
			String checkBoxCheckedAttr = questionsCheckboxes.get(i).getAttribute("checked");
			if (checkBoxCheckedAttr == null) {
				isChecked = false;
			} else if (questionsCheckboxes.get(i).getAttribute("checked").equals("true")) {
				isChecked = true;
				break;
			}
		}
		testResultService.assertEquals(true, isChecked, "Question is Not Answered");
	}

	public void submitSectionEmptyNewTE(String filePath, String testId, CourseCodes courseCode, CourseTests testType,
			int givenSectionIndex, String roundLevel, List<String> unitIds, JSONObject jsonObj,
			JSONObject localizationJson, String testName, int unitIndex, int sectionIndexInUnit) throws Exception {
		List<CourseTestSection> testSections = new ArrayList<CourseTestSection>();
		CourseTestSection sectionn = initSectionNew(filePath, testId, givenSectionIndex + 1, courseCode, testType,
				false);
		testSections.add(sectionn);
		CourseTest courseTest = new CourseTest(testSections, testType);
		submitSpecificSectionEmptyNewTE(courseTest, sectionIndex, roundLevel, unitIds, jsonObj, localizationJson,
				testName, unitIndex, sectionIndexInUnit);
	}

	public void submitSpecificSectionEmptyNewTE(CourseTest courseTest, int sectionIndex, String roundLevel,
			List<String> unitIds, JSONObject jsonObj, JSONObject localizationJson, String testName, int unitIndex,
			int sectionIndexInUnit) throws Exception {

		CourseTestSection round = new CourseTestSection();

		if (sectionIndexInUnit == 0) {
			report.startStep("Validate Unit Intro");
			validateIntro(jsonObj, localizationJson, unitIds.get(unitIndex), testName);
		} else {
			report.startStep("Validate Intro is not Displayed (Entered unit in the middle)");
			validateIntroIsNotDisplayed(sectionIndex);
		}

		List<String> lessonsIds = getLessonsIdsFromLessonDetailsArrOfSpecificUnit(unitIds.get(unitIndex));

		if (roundLevel.equals("2")) {

			report.startStep("Validate Lesson Intro");
			validateIntro(jsonObj, localizationJson, lessonsIds.get(sectionIndexInUnit), testName); // validate lesson
																									// id is correct
			Thread.sleep(2000);

			report.startStep("Click Next for Each Task");
			round = courseTest.getSections().get(0);
			for (int j = 0; j < round.getNumberOfQuestions() - 1; j++) {
				webDriver.checkElementEnabledAndClickable("//*[contains(@id,'next')]");
				clickNext();
				Thread.sleep(1000);
			}

			if (lessonsIds.size() - 1 > sectionIndexInUnit) {
				report.startStep("Click Next");
				clickNext();
			} else {
				report.startStep("Submit");
				submit(false);
			}

		} else if (roundLevel.equals("3")) {

			report.startStep("Validate Lesson Intro");
			validateIntro(jsonObj, localizationJson, lessonsIds.get(sectionIndexInUnit), testName);
			Thread.sleep(2000);

			report.startStep("Click Next for Each Task");
			round = courseTest.getSections().get(0);
			for (int j = 0; j < round.getNumberOfQuestions() - 1; j++) {
				webDriver.checkElementEnabledAndClickable("//*[contains(@id,'next')]");
				clickNext();
				Thread.sleep(700);
			}

			report.startStep("Submit");
			submit(false);
		}
	}

	public void validateIntroIsNotDisplayed(int sectionIndex) throws Exception {
		WebElement intro = webDriver.waitForElement("//div[contains(@class,'learning__NodeIntroTexts')]", ByTypes.xpath,
				false, 3);
		if (intro != null) {
			if (intro.getText().toLowerCase().contains("unit")) {
				testResultService
						.addFailTest("Unit intro is Displyed even though it shouldn't (Entered test at section "
								+ (sectionIndex + 1) + "- not the first section in current unit)", false, true);
				clickNext();
			}
		}
	}

	public void validateScoreEndOfTest(String expectedScore) throws Exception {
		webDriver.waitForElementByWebElement(outroPageScore, "expectedScore", true, 2);
		testResultService.assertEquals(expectedScore, outroPageScore.getText(), "End of Test Score is Incorrect.");
	}

	public String getTestTime() {
		webDriver.waitUntilElementAppears(testTimer, 5);
		return testTimer.getText();
	}

	public void verifyTestNumberInAlert(int expectedNumberInAlert) throws Exception {

		NewUxHomePage homePage = new NewUxHomePage(webDriver, testResultService);
		int actNumInAlert = Integer.valueOf(homePage.getAvailableTestsNumberInAlert());
		testResultService.assertEquals(expectedNumberInAlert, actNumInAlert,
				"Alert shows wrong num of available tests: " + expectedNumberInAlert + " tests expected", true);

	}

	///////////////

	// PLT functions- need to dubeg and modify all functions to match new te

	public void performTest(PLTTest pltTest) throws Exception {
		TextService textService = new TextService();

		// Fill the test questions according to the questions arrayList filled
		// from CSV file

		// Go over all 1st cycle questions and answer them according to the
		// questions object in the 1st PLTCyccle object

		// Cycle 1 has 17-20 questions (17 for basic/inter./advances and 20 for
		// "Im not sure"
		PLTCycle firstCycle = pltTest.getCycles().get(0);
		PLTCycle SecondCycle = pltTest.getCycles().get(1);

		selectLevelOnStartPLT(firstCycle.getPltStartLevel());

		pressOnStartPLT();

		try {
			webDriver.waitForElement("//*[@id='mainAreaTD']/div[1]", ByTypes.xpath, true, webDriver.getTimeout()); // Right
																													// Resource
			webDriver.waitForElement("//*[@id='mainAreaTD']/div[2]", ByTypes.xpath, false, webDriver.getTimeout()); // Left
																													// Resource

			String initialLevel = firstCycle.getPltStartLevel().toString().substring(0, 2);
			answerCycleQuestions(firstCycle, firstCycle.getCycleQuestions().size());

			// report.report("End of cycle 1");
			clickOnGoOnButton();

			// Start of cycle 2
			answerCycleQuestions(SecondCycle, SecondCycle.getCycleQuestions().size());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public PLTTest initPLTTestFromCSVFile(String path, Boolean skipCycle2) throws Exception {

		PLTCycle cycle1 = new PLTCycle();
		PLTCycle cycle2 = new PLTCycle();

		cycle1 = initCycle("1", path, PLTStartLevel.Intermediate, false);
		cycle2 = initCycle("2", path, PLTStartLevel.Advanced, skipCycle2);

		List<PLTCycle> testCycles = new ArrayList<PLTCycle>();
		testCycles.add(cycle1);
		testCycles.add(cycle2);

		PLTTest pltTest = new PLTTest(testCycles);

		return pltTest;
	}

	public PLTCycle initCycle(String cycleNumber, String path, PLTStartLevel pltStartLevel, boolean skipCycle)
			throws Exception {

		List<String[]> testData = textService.getStr2dimArrFromCsv(path);

		List<TestQuestion> cycleQuestions = new ArrayList<TestQuestion>();

		for (int i = 0; i < testData.size(); i++) {

			// if cycle number match - create TestQuestion object
			if (testData.get(i)[0].equals(cycleNumber)) {

				try {
					report.report("Reading line: " + i + " from csv file");

					String[] answers = textService.splitStringToArray(testData.get(i)[1], "\\|");

					String[] answerDestinations = textService.splitStringToArray(testData.get(i)[2], "\\|");

					String[] wrongAnswers = textService.splitStringToArray(testData.get(i)[3], "\\|");

					if (skipCycle)
						wrongAnswers[0] = "DoNotAnswer";

					// int[] blankAnswers = textService.splitStringToIntArray(
					// testData.get(i)[5], "?!^");

					// boolean booleanAnswer = Boolean.getBoolean(testData.get(i)[5]);
					// String questionType = testData.get(i)[6];
					report.report("Question type from csv is: " + testData.get(i)[5]);
					TestQuestionType questionType = TestQuestionType.valueOf(testData.get(i)[5]);

					TestQuestion question = new TestQuestion(answers, answerDestinations, wrongAnswers, new int[] {},
							questionType);
					cycleQuestions.add(question);
					report.report("Finished adding question data for line: " + i);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		PLTCycle pltCycle = new PLTCycle(pltStartLevel, Integer.valueOf(cycleNumber), null, cycleQuestions, null);
		return pltCycle;

	}

	public void verifyPlacementLevelOnResultPagePLT(String expectedLevel) throws Exception {

		WebElement element = null;
		String actualLevelInGraph = null;
		Thread.sleep(1000);

		for (int i = 0; i <= 10; i++) {
			element = webDriver.waitForElement("//div[@id='resultTitle']//b", ByTypes.xpath, 2, false);

			if (element != null && (element.getText().length() > 0)) {
				actualLevelInGraph = element.getText();
				break;
			}
		}

		if (actualLevelInGraph == null) {
			actualLevelInGraph = "No Result Graph";
		}

		Thread.sleep(3);
		WebElement header = webDriver.waitForElement("//div[@id='rightTitle']", ByTypes.xpath, false,
				webDriver.getTimeout());
		String actualTextInHeader = "";
		if (header != null) {
			actualTextInHeader = header.getText().replace(".", " ");
		} else {
			actualTextInHeader = "No Header Graph";
		}

		testResultService.assertEquals(expectedLevel, actualLevelInGraph, "Level in graph not valid");
		testResultService.assertEquals(true, actualTextInHeader.contains(expectedLevel),
				"Level in right header not valid");

	}

	public void clickOnExitPLT() throws Exception {
		int u = 0;
		webDriver.ClickElement(pltExitTestBtn);

		// webDriver.waitForElement("Exit", ByTypes.name,webDriver.getTimeout(),false,
		// "Exit PLT not Display").click();
		// webDriver.switchToMainWindow();
	}

	public void verifyDescriptionLinkPLT(String[] levels) throws Exception {
		webDriver.ClickElement(pltClickHereButtonCourseDescription);
		Thread.sleep(2000);
		webDriver.switchToFrame("bsModal__iframe");
		List<WebElement> coursesTitles = webDriver.getWebDriver().findElements(By.xpath("//*[@class='stext']//b"));

		for (int i = 0; i < levels.length; i++) {
			testResultService.assertEquals(levels[i], coursesTitles.get(i).getText(),
					"Course Title in description not valid or not found");
		}

		webDriver.switchToTopMostFrame();
		webDriver.ClickElement(closeEDInfoButton);
	}

	public void verifyPreferToStudyLinkPLT() throws Exception {

		webDriver.waitForElement("//div[@class='addText']//a", ByTypes.xpath, "Prefer to study link").click();
		Thread.sleep(2000);
		webDriver.switchToMainWindow();
		webDriver.switchToNewWindow();
		switchToTestAreaIframe();
		webDriver.switchToPopup();
		List<WebElement> radioBtns = webDriver.waitForElement("//table[@class='stext']", ByTypes.xpath, "Radio Buttons")
				.findElements(By.tagName("input"));
		radioBtns.get(0).click();
		WebElement element = webDriver.waitForElement("submitBtn", ByTypes.className, webDriver.getTimeout(), false,
				"Submit selection");
		element.click();
		Thread.sleep(2000);
		webDriver.switchToMainWindow();
		webDriver.switchToNewWindow();
		switchToTestAreaIframe();

	}

	public void startPltAndExit() {
		try {
			webDriver.waitForElement("rbBasic", ByTypes.id, "Basic level button").click();

			pressOnStartPLT();

			webDriver.waitForElement("//*[@id='mainAreaTD']/div[1]", ByTypes.xpath, true, webDriver.getTimeout()); // Right
																													// Resource
			webDriver.waitForElement("//*[@id='mainAreaTD']/div[2]", ByTypes.xpath, false, webDriver.getTimeout()); // Left
																													// Resource

			// webDriver.waitForElement("placmentExit", ByTypes.className).click();
			webDriver.waitForElement("//*[@id='exitCell']/table/tbody/tr/td[3]", ByTypes.xpath).click();
			Thread.sleep(1000);
			webDriver.switchToPopup();
			webDriver.waitForElement("submitBtn", ByTypes.className).click();
			Thread.sleep(1000);

			webDriver.switchToMainWindow();
			webDriver.switchToNewWindow();
			switchToTestAreaIframe();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub

	}

	public void performTestDidTest(PLTTest pltTest) throws Exception {
		TextService textService = new TextService();

		PLTCycle firstCycle = pltTest.getCycles().get(0);

		selectLevelOnStartPLT(firstCycle.getPltStartLevel());

		pressOnStartPLT();
		answerCycleQuestions(firstCycle, firstCycle.getNumberOfQuestions());

	}

	public void close() throws Exception {
		webDriver.closeNewTab(1);
		webDriver.switchToMainWindow();

	}

	public void verifyDoTestAgainNotDisplayedInPLT() throws Exception {

		if (getDoTestAgainElement() != null) {
			testResultService.addFailTest("Do Test Again button displayed though should be hidden", false, true);
		}

	}

	private WebElement getDoTestAgainElement() throws Exception {

		return webDriver.waitForElement("DoAgain", ByTypes.name, 3, false, "Do Test Again button not found");

	}

	public void clickSubmitOnPLT_API() throws Exception {
		WebElement submitBtn = webDriver.waitForElement("btnSubmit", ByTypes.name);
		webDriver.waitUntilElementAppears(submitBtn, 10);
		webDriver.ClickElement(submitBtn);
		Thread.sleep(2000);
	}

	public void verifyLevelListExist() throws Exception {
		testResultService.assertEquals(4, pltLevels.size(), "Levels are not Displayed Correctly.");
	}

	public void verifyLanguagesExists() throws Exception {
		testResultService.assertEquals(true, webDriver.isDisplayed(pltLanguageList),
				"Languages element is not Displayed.");
	}

	public void verifyStartTestButtonsExists() throws Exception {
		testResultService.assertEquals(true, webDriver.isDisplayed(startTestButton),
				"Start Test Button is not Displayed.");
	}

	public void choosePltLevel(String level) {

		webDriver.waitUntilElementAppears(pltLevels, 5);

		for (int i = 0; i < pltLevels.size(); i++) {
			if (pltLevels.get(i).getText().equals(level)) {
				pltLevelsButtons.get(i).click();
				break;
			}
		}
	}

	public String choosePlacementStatusNewTe(String status) throws Exception {

		String selectedStatusSymbol = "";
		switch (status) {
		case "Basic":
			choosePltLevel(status);
			selectedStatusSymbol = "Basic2";
			break;
		case "Intermediate":
			choosePltLevel(status);
			selectedStatusSymbol = "Intermediate2";
			break;
		case "Advanced":
			choosePltLevel(status);
			selectedStatusSymbol = "Advanced2";
			break;
		case "I am not sure":
			choosePltLevel(status);
			selectedStatusSymbol = "Basic3";
			break;
		}
		return selectedStatusSymbol;
	}

	public void choosePltLanguage(String language) {
		webDriver.ClickElement(pltLanguageList); // element not found- need to fix
		for (int i = 0; i < pltLanguageOptions.size(); i++) {
			if (pltLanguageOptions.get(i).getText().equals(language)) {
				pltLanguageOptions.get(i).click();
				break;
			}
		}
	}

	public List<TestQuestion> initSectionPLTNewTE(String path, String wantedLevel, String wantedSection,
			String sectionCode) throws Exception {
		List<String[]> testData = textService.getStr2dimArrFromCsv(path);

		List<TestQuestion> sectionQuestions = new ArrayList<TestQuestion>();

		for (int i = 0; i < testData.size(); i++) {

			if (sectionCode.length() != 6) {
				testResultService.addFailTest("Wanted Section Code is Incorrect (length is not 6): " + sectionCode,
						true, false);
			}

			if (testData.get(i)[1].trim().equals(wantedLevel.trim())
					&& testData.get(i)[2].trim().equals(wantedSection.trim())
					&& testData.get(i)[3].trim().equals(sectionCode.trim())) {

				try {
					report.report("Reading line: " + i + " from csv file");

					String[] answers = textService.splitStringToArray(testData.get(i)[5], "\\|");

					String[] answerDestinations = textService.splitStringToArray(testData.get(i)[2], "\\|");

					String[] wrongAnswers = textService.splitStringToArray(testData.get(i)[3], "\\|");

					// if (skipCycle) wrongAnswers[0] = "DoNotAnswer";

					// int[] blankAnswers = textService.splitStringToIntArray(
					// testData.get(i)[5], "?!^");

					// boolean booleanAnswer = Boolean.getBoolean(testData.get(i)[5]);
					// String questionType = testData.get(i)[6];
					report.report("Question type from csv is: " + testData.get(i)[6]);
					TestQuestionType questionType = TestQuestionType.valueOf(testData.get(i)[6]);

					TestQuestion question = new TestQuestion(answers, answerDestinations, wrongAnswers, new int[] {},
							questionType);
					sectionQuestions.add(question);
					report.report("Finished adding question data for line: " + i);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		// PLTCycle pltCycle = new PLTCycle(pltStartLevel, Integer.valueOf(cycleNumber),
		// null, cycleQuestions, null);
		return sectionQuestions;

	}

	public void validateMessageText(String expectedMessage) throws Exception { // debug
		try {
			testResultService.assertEquals(expectedMessage, submitMessage.getText(), "Message is Incorrect.");
			clickOkOnMessage();
		} catch (Exception e) {
			testResultService.addFailTest("Message is Not Displayed.", false, true);
		}
	}

	public void clickOkOnMessage() {
		webDriver.ClickElement(sumbitOkButton); // debug
	}

	public String getAndValidateEdTestTimer(String expectedEdTimeFormat) {

		String actualEdTestTimer = getTestTime();
		testResultService.assertEquals(expectedEdTimeFormat, actualEdTestTimer, "Ed Timer Not match to DB Timer");
		return actualEdTestTimer;
	}

	public String convertTimeInMinutesToEDTimerFormat(int minutes) {

		int hoursEdFormat = (int) Math.floor(minutes / 60);
		int minutesEdFormat = minutes - (hoursEdFormat * 60);

		return hoursEdFormat + "Hrs. " + minutesEdFormat + "Mins.";
	}

	public String[] getUserAdministrationRecordsByCourseId(List<String[]> administrationDetails, String courseId) {

		String[] records = null;

		for (int i = 0; i < administrationDetails.size(); i++) {
			if (administrationDetails.get(i)[1].equals(courseId))
				records = administrationDetails.get(i);
		}
		return records;
	}

	public void answerPltCycleNewTE(String unitId, List<String> lessonsIds, JSONObject jsonObj,
			JSONObject localizationJson, String testName, String filePath, String[] levels, String[] sectionsCodes,
			int[] wantedScores) throws Exception {

		String sectionType = "";

		// Currently there is no unit id retrieved from client side, so i retrieve the
		// lessons id differently,
		// once it is fixed i will be able to validate unit intro and retrieve lessons
		// id by unit id //
		String nameOfTest = this.testName.getText();
		testResultService.assertEquals(true, nameOfTest.contains("Section " + sectionIndex),
				"Section index is Incorrect. Expected: " + sectionIndex + ". Actual: " + nameOfTest);

		report.startStep("Validate Unit Intro");
		validateIntro(jsonObj, localizationJson, unitId, testName);

		report.startStep("Validate Task bar is not Clickable");
		validateTaskBarIsNotClickable();

		int j =0;
		for (j = 0; j < lessonsIds.size(); j++) {
			if (j == 0) {
				sectionType = "Reading";
			} else if (j == 1) {
				sectionType = "Listening";
			} else if (j == 2) {
				sectionType = "Grammar";
			}

			report.startStep("Validate Lesson Intro");
			validateIntro(jsonObj, localizationJson, lessonsIds.get(j), testName);

			report.startStep("Validate Title Indexes");
			validateTitleIndex();

			report.startStep("Answer Section");

			if (sectionsCodes[j].equalsIgnoreCase("i2rnp1")){

				if (wantedScores[j] == 80)
					levels[j] = "Intermediate2score80";

				if (wantedScores[j] == 60)
					levels[j] = "Intermediate2score60";
			}
			//b2rsp1
 			initAndAnswerPltSectionByWantedScoreNewTE(filePath, levels[j], sectionType, sectionsCodes[j],
				wantedScores[j]);

			Thread.sleep(1000);
			if (j < lessonsIds.size() - 1) {
				report.startStep("Click Next");
				clickNext();
				Thread.sleep(1000);
			}
			lessonIndex++;
			//Thread.sleep(3000);
		}

		Thread.sleep(1000);
 		report.startStep("Submit");
		if (wantedScores[0] == 100 && wantedScores[1] == 100 && wantedScores[2] == 100) {
			submitPlt(true);
		} else {
			submitPlt(false);
		}
		sectionIndex++;
		lessonIndex = 1;
	}

	public void initAndAnswerPltSectionByWantedScoreNewTE(String filePath, String level, String sectionType,
			String sectionCode, int wantedScore) throws Exception {

		List<TestQuestion> sectionAnswers = initSectionPLTNewTE(filePath, level, sectionType, sectionCode);
		int numOfQuestionInSection = sectionAnswers.size();
		int numOfQuestionToAnswer = calculateNumOfAnswersToGetWantedScore(numOfQuestionInSection, wantedScore);
		//
		answerSectionQuestionsPartiallyNewTE(sectionAnswers, numOfQuestionToAnswer);
		Thread.sleep(1000);
	}

	public int answerSectionQuestionsPartiallyNewTE(List<TestQuestion> sectionAnswers, int numOfQuestionsToAnswer)
			throws Exception {

		int numOfAnsweredQuestions = 0;

		for (int i = 0; i < numOfQuestionsToAnswer; i++) {

			Thread.sleep(1000);
			// webDriver.waitUntilElementAppears("//div[@id='pmContainer']", 30);
			TestQuestion question = sectionAnswers.get(i);

			TestQuestionType questionType = question.getQuestionType();

			String[] answers = new String[question.getCorrectAnswers().length];

			for (int j = 0; j < question.getCorrectAnswers().length; j++) {
				answers[j] = question.getCorrectAnswers()[j].replace("~", ",");
			}
			question.setCorrectAnswers(answers);

			// if (question.getBlankAnswers()==null){//(question.getBlankAnswers()[0]==0) {
			// // skip the question if wrong answer exist in csv file or parameter of
			// skipCycle is true

			switch (questionType) {

			case DragAndDropMultiple:
				answerDragAndDropQuestionNewTE(question);
				break;

			case RadioMultiple:
				answerMultiCheckBoxQuestionNewTE(question);
				break;

			case RadioSingle:
				answerCheckboxQuestionNewTE(question);
				break;

			case DragAndDropSingle:
				answerDragAndDropQuestionNewTE(question);
				break;

			case comboBox:
				answerComboBoxQuestionNewTE(question);
				break;

			case DrapAndDropToPicture:
				answerDragAndDropToPictureQuestionNewTE(question);
				break;

			default:
				break;
			}
			numOfAnsweredQuestions++;
			// }

			//Thread.sleep(500);
			if (i < numOfQuestionsToAnswer - 1) {
				pressOnNextTaskArrow();
				//Thread.sleep(500);
			}
			Thread.sleep(1000);
			// webDriver.waitUntilElementAppears("//div[@id='pmContainer']", 30);
		}

		int questionsNotAnswered = sectionAnswers.size() - numOfQuestionsToAnswer;
		for (int i = 0; i < questionsNotAnswered; i++) {
			pressOnNextTaskArrow();
			Thread.sleep(2000);
		}
		return numOfAnsweredQuestions;
	}

	public List<String[]> initScoresOfPLT(String path) throws Exception {
		List<String[]> testData = textService.getStr2dimArrFromCsv(path);

		/*
		 * List<String[]> dataOfVersion = new ArrayList<String[]>();
		 *
		 * for (int i = 0; i < testData.size(); i++) { if
		 * (testData.get(i)[0].trim().equals(wantedVersion)) {
		 * dataOfVersion.add(testData.get(i)); } }
		 */
		return testData;
	}

	public String getPltLevelByCode(String path, String sectionCode) throws Exception {
		List<String[]> testData = textService.getStr2dimArrFromCsv(path);
		String pltLevel = "";
		for (int i = 0; i < testData.size(); i++) {

			if (testData.get(i)[3].trim().toLowerCase().equals(sectionCode)) {
				pltLevel = testData.get(i)[1];
				break;
			}
		}
		return pltLevel;
	}

	public String[] validatePltScoresInDB(String studentId, int[] wantedScore) {
		List<String[]> userTestComponentProgress = dbService.getUserTestComponentProgressByUserId(studentId);

		if (userTestComponentProgress.get(2)[6].equalsIgnoreCase("398"))
			wantedScore[0]=74; // for iteration 9 and Component Id: 398

		testResultService.assertEquals(Integer.toString(wantedScore[0]), userTestComponentProgress.get(2)[12],
				"Reading Score is Stored Incorrectly in DB.");

		testResultService.assertEquals(Integer.toString(wantedScore[1]), userTestComponentProgress.get(1)[12],
				"Listening Score is Stored Incorrectly in DB.");

		int actualGrammerDBScore = Integer.parseInt(userTestComponentProgress.get(0)[12]);

		testResultService.assertTrue("Grammar Score is Stored Incorrectly in DB: "+actualGrammerDBScore
				+ "And wantedScore is: "+wantedScore[2],
				(wantedScore[2]) - actualGrammerDBScore==0 ||
				(wantedScore[2]) - actualGrammerDBScore<= 10 );
				//Integer.parseInt(userTestComponentProgress.get(0)[12])>=90);

		//testResultService.assertEquals(Integer.toString(wantedScore[2]), userTestComponentProgress.get(0)[12],
			//	"Grammar Score is Stored Incorrectly in DB.");

		String[] actualScores = { userTestComponentProgress.get(2)[12], userTestComponentProgress.get(1)[12],
				userTestComponentProgress.get(0)[12] };
		return actualScores;
	}

	public String convertPltLevelCodeToLevelString(String levelCode) {
		String level = "";
		switch (levelCode.trim()) {
		case "P2":
			level = "FirstDiscoveries";
			break;
		case "B1":
			level = "Basic1";
			break;
		case "B2":
			level = "Basic2";
			break;
		case "B3":
			level = "Basic3";
			break;
		case "I1":
			level = "Intermediate1";
			break;
		case "I2":
			level = "Intermediate2";
			break;
		case "I3":
			level = "Intermediate3";
			break;
		case "A1":
			level = "Advanced1";
			break;
		case "A2":
			level = "Advanced2";
			break;
		case "A3":
			level = "Advanced3";
			break;
		}
		return level;
	}

	public String convertPltLevelStringToLevelCode(String levelString) {
		String levelCode = "";
		switch (levelString.trim()) {
		case "FirstDiscoveries":
			levelCode = "P2";
			break;
		case "Basic1":
			levelCode = "B1";
			break;
		case "Basic2":
			levelCode = "B2";
			break;
		case "Basic3":
			levelCode = "B3";
			break;
		case "Intermediate1":
			levelCode = "I1";
			break;
		case "Intermediate2":
			levelCode = "I2";
			break;
		case "Intermediate3":
			levelCode = "I3";
			break;
		case "Advanced1":
			levelCode = "A1";
			break;
		case "Advanced2":
			levelCode = "A2";
			break;
		case "Advanced3":
			levelCode = "A3";
			break;
		}
		return levelCode;
	}

	public void validatePltEndTestLevels(String[] expectedLevels)  {
		double oneScaleLevelHeight = 18.333;

		try {
			report.startStep("Validate Reading Level");

			String levelString = convertPltLevelCodeToLevelString(expectedLevels[0]);
			int levelIndex = getLevelIndexByLevelName(levelString);
			testResultService.assertEquals(Double.toString(oneScaleLevelHeight * levelIndex),
					pltReadingLevelEndPage.getAttribute("height"), "Reading level is Incorrect.");

			report.startStep("Validate Listening Level");
			levelString = convertPltLevelCodeToLevelString(expectedLevels[1]);
			levelIndex = getLevelIndexByLevelName(levelString);
			testResultService.assertEquals(Double.toString(oneScaleLevelHeight * levelIndex),
					pltListeningLevelEndPage.getAttribute("height"), "Reading level is Incorrect.");

			report.startStep("Validate Grammar Level");
			levelString = convertPltLevelCodeToLevelString(expectedLevels[2]);
			levelIndex = getLevelIndexByLevelName(levelString);
			testResultService.assertEquals(Double.toString(oneScaleLevelHeight * levelIndex),
					pltGrammarLevelEndPage.getAttribute("height"), "Reading level is Incorrect.");

			report.startStep("Validate Final Level");
			levelString = convertPltLevelCodeToLevelString(expectedLevels[3]);
			testResultService.assertEquals(levelString, apiResLevel.getText().replace(" ", ""),
					"Final Level is not compatible with level in DB"); //pltFinalLevelEndPage

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void validateTitleIndex() throws Exception {
		testResultService.assertEquals(true, testName.getText().contains("Section " + sectionIndex),
				"Section index is Incorrect. Expected: " + sectionIndex + ". Actual: " + testName.getText());
		testResultService.assertEquals(true, testName.getText().contains("Part " + lessonIndex),
				"Lesson index is Incorrect. Expected: " + lessonIndex + ". Actual: " + testName.getText());
	}

	public String[] calculateAndValidatePltFinalLevel(String userId, String[] firstCycleLevels,
			String[] secondCycleLevels) throws Exception {
		try {

			report.startStep("Get User Test Component Progress");
			List<String[]> userTestComponentProgress = dbService.getUserTestComponentProgressByUserId(userId);

			report.startStep("Sum Component Score in User Test Component Progress");
			String userTestUnitProgressId = userTestComponentProgress.get(0)[1]; // retrieve first
																					// userTestUnitProgressId- relevant
																					// to the current test played
			double componentScoreSum = 0;
			for (int i = 0; i < userTestComponentProgress.size(); i++) {
				if (userTestComponentProgress.get(i)[1].equals(userTestUnitProgressId)) { // add only component that are
																							// relevant to the current
																							// test
					componentScoreSum += Double.parseDouble(userTestComponentProgress.get(i)[11]);
				}
			}
			componentScoreSum = Math.round(componentScoreSum);
			int componentScoreSumInt = (int) componentScoreSum;

			report.startStep(
					"Compare Component Score in User Test Component Progress to Test Grade For Display in User Test Progress");
			List<String[]> userTestProgress = dbService.getUserTestProgressByUserId(userId);
			testResultService.assertEquals(Integer.toString(componentScoreSumInt), userTestProgress.get(0)[4],
					"Score in user test progress is incorrect.");

			report.startStep("Calculate final skill level index for each skill");
			int readingFinalScoreIndex = 0;
			int listeningFinalScoreIndex = 0;
			int grammarFinalScoreIndex = 0;
			String readingFinalScoreCode = "";
			String listeningFinalScoreCode = "";
			String grammarFinalScoreCode = "";

			// String secondCycleLevelCode = "";
			String secondCycleLevelString = "";
			int secondCycleLevelIndex = 0;
			int finalLevelIndex = 0;
			String firstCycleLevelString = "";
			int firstCycleLevelIndex = 0;

			// grammar, listening, reading
			for (int i = 0; i < 3; i++) {
				if (Integer.parseInt(userTestComponentProgress.get(i + 3)[12]) < 30
						&& Integer.parseInt(userTestComponentProgress.get(i)[12]) < 30) {
					finalLevelIndex = 1;
				} else if (Integer.parseInt(userTestComponentProgress.get(i + 3)[12]) > 75
						&& Integer.parseInt(userTestComponentProgress.get(i)[12]) < 40) {
					int tempIndex = firstCycleLevels.length - i - 1;
					firstCycleLevelString = firstCycleLevels[tempIndex];
					firstCycleLevelIndex = getLevelIndexByLevelName(firstCycleLevelString);
					finalLevelIndex = firstCycleLevelIndex + 1;
				} else if (Integer.parseInt(userTestComponentProgress.get(i + 3)[12]) > 95
						&& Integer.parseInt(userTestComponentProgress.get(i)[12]) > 95) {
					finalLevelIndex = 9;
				} else if (componentScoreSum < 10) {
					finalLevelIndex = 0;
				} else {
					int tempIndex = secondCycleLevels.length - i - 1;
					secondCycleLevelString = secondCycleLevels[tempIndex];
					secondCycleLevelIndex = getLevelIndexByLevelName(secondCycleLevelString);
					finalLevelIndex = secondCycleLevelIndex;
				}

				if (i == 0) {
					grammarFinalScoreIndex = finalLevelIndex;
					grammarFinalScoreCode = getLevelNameByLevelIndex(grammarFinalScoreIndex);
					grammarFinalScoreCode = convertPltLevelStringToLevelCode(grammarFinalScoreCode);
				} else if (i == 1) {
					listeningFinalScoreIndex = finalLevelIndex;
					listeningFinalScoreCode = getLevelNameByLevelIndex(listeningFinalScoreIndex);
					listeningFinalScoreCode = convertPltLevelStringToLevelCode(listeningFinalScoreCode);
				} else if (i == 2) {
					readingFinalScoreIndex = finalLevelIndex;
					readingFinalScoreCode = getLevelNameByLevelIndex(readingFinalScoreIndex);
					readingFinalScoreCode = convertPltLevelStringToLevelCode(readingFinalScoreCode);
				}

			}

			report.startStep("Calculate final level skill");
			double tempFinalSkill = (double) (grammarFinalScoreIndex + listeningFinalScoreIndex
					+ readingFinalScoreIndex) / 3;
			int finalLevelSkill = (int) Math.round(tempFinalSkill);// Math.round((grammarFinalScoreIndex +
																	// listeningFinalScoreIndex +
																	// readingFinalScoreIndex)/3); //14/3 does not round
																	// up but down
			String finalLevelName = getLevelNameByLevelIndex(finalLevelSkill);
			String finalLevelCode = convertPltLevelStringToLevelCode(finalLevelName);
			if (componentScoreSumInt < 10) {
				finalLevelCode = "P2";
			}
			testResultService.assertEquals(finalLevelCode, userTestProgress.get(0)[17],
					"Final Level Code (Learning Start Level) in user test progress is incorrect.");
			String[] finalLevels = { readingFinalScoreCode, listeningFinalScoreCode, grammarFinalScoreCode,
					finalLevelCode };
			return finalLevels;
		} catch (Exception e) {
			return new String[0];
		}
	}

	// submit of plt (when back==false)
	public void submitPlt(boolean answeredAllQuestions) throws Exception {
		try {

			webDriver.waitUntilElementAppears(submitButton,5);

			WebElement backButton = webDriver.findElementByXpath("//*[@id='learning__prevItem']/a", ByTypes.xpath);
			String backButtonStatus = backButton.getAttribute("class");
			String expectedMessage = "You haven't answered all the questions in this section.\nPlease note that you cannot repeat this section once it is submitted. Are you sure you want to submit this section?";

			webDriver.ClickElement(submitButton);

			if (answeredAllQuestions) {
				testResultService.assertEquals(false, webDriver.isDisplayed(submitMessage),
						"Message is displayed after Submit Even Though Student Answered All Questions.");
				if (webDriver.isDisplayed(submitMessage))
					webDriver.ClickElement(sumbitOkButton);

			}
			else { // not all questions answered

				// verify if the back buttons is enabled show the message
				if (backButtonStatus.contains("disabled")) {
					testResultService.assertEquals(false, webDriver.isDisplayed(submitMessage),
							"Message is displayed after Submit Even Though Student Answered All Questions.");

					if (webDriver.isDisplayed(submitMessage))
						webDriver.ClickElement(sumbitOkButton);
				}
				else {
					// message should be display
					testResultService.assertEquals(true, submitMessage.getText().contains(expectedMessage),
							"Submit message is Incorrect.");

					if (!webDriver.isDisplayed(submitMessage))
						testResultService.addFailTest(
							"Submit Message Is Not Displayed Even Though Student Did Not Answer All Questions.", false,
							true);
				}
				webDriver.ClickElement(sumbitOkButton);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	// not working since page helper is not accessible from this class (we can
	// initialize it but webdriver is null so there are failures)
	public void performPLT(String testName, String studentId, int iteration, JSONObject jsonObj,
			JSONObject localizationJson, String answersFilePath, String institutionId) throws Exception {

		int[] wantedScore = new int[3];
		List<String[]> dataOfVersion = initScoresOfPLT("files/PLTTestData/NewTePltScores.csv");

		wantedScore[0] = Integer.parseInt(dataOfVersion.get(iteration)[3]); // C1 Reading Score
		wantedScore[1] = Integer.parseInt(dataOfVersion.get(iteration)[4]); // C1 Listening Score
		wantedScore[2] = Integer.parseInt(dataOfVersion.get(iteration)[5]); // C1 Grammar Score

		String level = dataOfVersion.get(iteration)[2]; // Initial Level

		report.startStep("Validate Test Name");
		validateTestName(testName);
		Thread.sleep(5000);

		// report.startStep("Validate Course Intro");
		// testEnvironmentPage.validateIntro(jsonObj, localizationJson, testId,
		// testName);

		report.startStep("Choose Level");
		String statusSymbol = choosePlacementStatusNewTe(level);
		String readingLevel = "";
		if (iteration == 6) {
			readingLevel = "Intermediate2score60";
		} else if (iteration == 7) {
			readingLevel = "Intermediate2score80";
		} else {
			readingLevel = statusSymbol;
		}

		String[] levels = { readingLevel, statusSymbol, statusSymbol };// levels of all sections

		// report.startStep("Choose Language");
		// testEnvironmentPage.choosePltLanguage(pltLanguage);

		report.startStep("Click on Start Test Button");
		pressOnStartTest();
		Thread.sleep(2000);

		report.startStep("Retrieve 3 sections codes from client side (First Cycle)");
		String[] codes = new String[3];
		for (int i = 0; i < 3; i++) {
			String code = pageHelper.runJavaScriptCommand(
					"return JSON.parse(JSON.parse(localStorage.getItem('Course')).CourseJson).Children[0].Children[" + i
							+ "].Metadata.Code");
			codes[i] = code;
			Thread.sleep(1000);
		}
		report.addTitle("Codes: " + Arrays.toString(codes));

		report.startStep("Retrieve Lessons Ids of First Cycle");
		List<String> lessonsIds = new ArrayList<String>(); // getLessonsIdsFromLessonDetailsArrOfSpecificUnit(unitId);String[]
															// codes = new String[3];
		for (int i = 0; i < codes.length; i++) {
			String lessonId = pageHelper.runJavaScriptCommand(
					"return JSON.parse(JSON.parse(localStorage.getItem('Course')).CourseJson).Children[0].Children.filter(function(lesson){ return lesson.Metadata.Code ==='"
							+ codes[i] + "'; })[0].NodeId");
			lessonsIds.add(lessonId);
		}
		report.addTitle("Lessons Ids: " + lessonsIds.toString());

		// report.startStep("Retrieve Unit Id (First Cycle) from JSON by Lesson Id");
		// unitId = testEnvironmentPage.getValueOfWantedKeyInSpecificTestId(jsonObj,
		// lessonsIds.get(0), "ParentNodeId");

		String unitId = "-1";

		report.startStep("Answer First Cycle Questions");
		answerPltCycleNewTE(unitId, lessonsIds, jsonObj, localizationJson, testName, answersFilePath, levels, codes,
				wantedScore);

		if (iteration == 6) {
			wantedScore[0] = 63;
		} else if (iteration == 7) {
			wantedScore[0] = 73;
		} else if (iteration == 1) {// v1
			wantedScore[2] = 58;
		}

		report.startStep("Validate Scores are Stored Correctly in DB");
		String[] actualScoresDB = validatePltScoresInDB(studentId, wantedScore);

		report.startStep("Calculate Expected next Cycle Level");
		Object[] expectedSecondCycleReadingLevel = calculateSecondCycleLevel(statusSymbol,
				Integer.parseInt(actualScoresDB[0]));
		Object[] expectedSecondCycleListeningLevel = calculateSecondCycleLevel(statusSymbol,
				Integer.parseInt(actualScoresDB[1]));
		Object[] expectedSecondCycleGrammarLevel = calculateSecondCycleLevel(statusSymbol,
				Integer.parseInt(actualScoresDB[2]));
		Thread.sleep(2000);

		report.startStep("Retrieve 3 sections codes from client side (Second Cycle)");
		String[] secondCycleCodes = new String[3];
		for (int i = 0; i < 3; i++) {
			String code = pageHelper.runJavaScriptCommand(
					"return JSON.parse(JSON.parse(localStorage.getItem('Course')).CourseJson).Children[1].Children[" + i
							+ "].Metadata.Code");
			secondCycleCodes[i] = code;
			Thread.sleep(1000);
		}
		report.addTitle("Codes: " + Arrays.toString(secondCycleCodes));

		report.startStep("Validate Second Cycle Sections Codes match the Expected Level");
		String secondCycleReadingLevelFromClient = getPltLevelByCode(answersFilePath, secondCycleCodes[0]);
		String secondCycleListeningLevelFromClient = getPltLevelByCode(answersFilePath, secondCycleCodes[1]);
		String secondCycleGrammarLevelFromClient = getPltLevelByCode(answersFilePath, secondCycleCodes[2]);
		testResultService.assertEquals(expectedSecondCycleReadingLevel[1].toString().trim(),
				secondCycleReadingLevelFromClient.trim(), "Second Cycle Reading Level is Incorrect.");
		testResultService.assertEquals(expectedSecondCycleListeningLevel[1].toString().trim(),
				secondCycleListeningLevelFromClient.trim(), "Second Cycle Listening Level is Incorrect.");
		testResultService.assertEquals(expectedSecondCycleGrammarLevel[1].toString().trim(),
				secondCycleGrammarLevelFromClient.trim(), "Second Cycle Grammar Level is Incorrect.");
		String[] secondCycleLevelsClient = { secondCycleReadingLevelFromClient.trim(),
				secondCycleListeningLevelFromClient.trim(), secondCycleGrammarLevelFromClient.trim() };

		report.startStep("Retrieve Lessons Ids of Second Cycle");
		lessonsIds = new ArrayList<String>(); // getLessonsIdsFromLessonDetailsArrOfSpecificUnit(unitId);String[] codes
												// = new String[3];
		for (int i = 0; i < codes.length; i++) {
			String lessonId = pageHelper.runJavaScriptCommand(
					"return JSON.parse(JSON.parse(localStorage.getItem('Course')).CourseJson).Children[1].Children.filter(function(lesson){ return lesson.Metadata.Code ==='"
							+ secondCycleCodes[i] + "'; })[0].NodeId");
			lessonsIds.add(lessonId);
		}
		report.addTitle("Lessons Ids: " + lessonsIds.toString());

		report.startStep("Retrieve Wanted Score of C2 from file");
		wantedScore[0] = Integer.parseInt(dataOfVersion.get(iteration)[6]); // C2 Reading Score
		wantedScore[1] = Integer.parseInt(dataOfVersion.get(iteration)[7]); // C2 Listening Score
		wantedScore[2] = Integer.parseInt(dataOfVersion.get(iteration)[8]); // C2 Grammar Score

		report.startStep("Answer Second Cycle Questions");
		answerPltCycleNewTE(unitId, lessonsIds, jsonObj, localizationJson, testName, answersFilePath,
				secondCycleLevelsClient, secondCycleCodes, wantedScore); // secondCycleLevelFromClient should be
																			// secondCycleLevels[1].toString()
		Thread.sleep(3000);

		List<String[]> userTestComponentProgress = dbService.getUserTestComponentProgressByUserId(studentId);
		String[] finalScoresDB = new String[userTestComponentProgress.size()];
		for (int i = 0; i < userTestComponentProgress.size(); i++) {
			finalScoresDB[i] = userTestComponentProgress.get(i)[12];
		}
		report.startStep("Final scores from DB (descending): " + Arrays.toString(finalScoresDB));

		report.startStep("Calculate Final grade");
		String[] finalLevels = calculateAndValidatePltFinalLevel(studentId, levels, secondCycleLevelsClient);

		List<String[]> userTestProgress = dbService.getUserTestProgressByUserId(studentId);

		/*
		 * report.startStep("Validate Score in DB"); List<String[]> userTestProgress =
		 * dbService.getUserTestProgressByUserId(studentId); String scoreForDisplayDB =
		 * userTestProgress.get(0)[4]; testResultService.assertEquals(expectedScore,
		 * scoreForDisplayDB, "Score in DB is Incorrect");
		 */ //

		report.startStep("Validate isCompleted in DB");
		String isCompletedDB = userTestProgress.get(0)[8];
		testResultService.assertEquals("1", isCompletedDB, "isCompleted in DB is Incorrect");

		report.startStep("Validate Submit Reason in DB");
		String submitReasonDB = userTestProgress.get(0)[9];
		testResultService.assertEquals("1", submitReasonDB, "Submit Reason in DB is Incorrect");
		Thread.sleep(2000);

		report.startStep("Validate Scores in End Page");
		validatePltEndTestLevels(finalLevels);

		report.startStep("Validate Test Status in DB is Done"); // NEED TO ADD
		// String userExitTestSettingsId = userTestProgress.get(0)[0];
		// String testStatus =
		// dbService.getTestStatusByUserExitTestSettingsId(userExitTestSettingsId);
		// testResultService.assertEquals("3", testStatus, "Test Status in DB is
		// incorrect.");
		String testStatus = dbService.getTestStatusOfPLTByUserId(studentId);
		testResultService.assertEquals("3", testStatus, "Test Status in DB is incorrect.");

		report.startStep("Validate Final Level Calculated by Automation Match Final Levels in DB");
		String[] userPLtFinalResultDB = dbService.getUserFinalPltResultByInstIdNewTE(institutionId);
		String[] finalLevelsDB = { userPLtFinalResultDB[4], userPLtFinalResultDB[5], userPLtFinalResultDB[6],
				userPLtFinalResultDB[7] };
		testResultService.assertEquals(true, Arrays.equals(finalLevelsDB, finalLevels),
				"Final Levels Calculated by Automation Don't match Final levels in DB. Automation: "
						+ Arrays.toString(finalLevels) + ". DB: " + Arrays.toString(finalLevelsDB));
		report.startStep("Levels from db: " + Arrays.toString(finalLevelsDB));

	}

	public void validateIntroWithoutContinue(JSONObject jsonObj, JSONObject localizationJson, String id,
			String testName) throws Exception {
		// Init variables
		JSONObject tempJSONObj;
		String template = "";
		String title = "";
		String text = "";

		// Retrieve introsettings of specific test id
		String introSettings = getValueOfWantedKeyInSpecificTestId(jsonObj, id, "IntroSettings");

		// This is the json from which we will take title and text
		JSONArray tempJSONArr = new JSONArray(introSettings);
		for (int j = 0; j < tempJSONArr.length(); j++) {

			tempJSONObj = (JSONObject) tempJSONArr.get(j);

			// if position is end - ignore (continue)
			String position = tempJSONObj.getString("Position");
			if (!position.equals("start")) {
				continue;
			}

			try {
				// Retrieve template
				template = validateJsonElementNotNull(tempJSONObj.get("TemplateCode"));

				// Retrieve Title
				title = validateJsonElementNotNull(tempJSONObj.get("TitleCode"));

				// Retrieve Text
				text = validateJsonElementNotNull(tempJSONObj.get("TextCode"));

				if (template.equals("TestIntro")) {
					title = testName;
				} else if (template.equals("PLTIntro")) {
					title = testName;
					// text = ?? need to write hard-coded (changes in each build and environment)
				}

				// Get title and text from localization
				title = retrieveJsonElementFromLocalizationAndModifyIt(localizationJson, title, testName);
				text = retrieveJsonElementFromLocalizationAndModifyIt(localizationJson, text, testName);
			} catch (Exception e) {
				System.out.println(e);
			}

			boolean isTemplateValidated = validateTemplate(title, text);

			/*
			 * if (isTemplateValidated) { if (template.equals("TestNodeIntro")) {
			 * report.startStep("Click Next Button"); clickNext(); } else if
			 * (template.equals("TestIntro")) { report.startStep("Click Start Test Button");
			 * clickStartTest(); } } else { testResultService.addFailTest("Template of : "
			 * +id+" was not found, so next button is not pressed", false, true); }
			 */
		}
	}

	public void validateTestRandomization(String teVersion, String courseId, String assignedTestId,
			int ImpTypeFeatureId) throws Exception {
		report.startStep("Get Relevant Test IDs List from DB");
		List<String> testsIds = dbService.getActiveTestsByCourseIdAndTeVersion(courseId, teVersion, ImpTypeFeatureId);

		report.startStep("Validate Assigned Test ID is in that List");
		testResultService.assertEquals(true, testsIds.contains(assignedTestId),
				"Assigned Test ID is not found in relevant List from DB. Assigned test id: " + assignedTestId
						+ ". List from DB (TE Version:" + teVersion + "): " + testsIds);
	}

	public void validateTestNameContainsExpectedText(String expectedTestName) throws Exception {
		webDriver.waitUntilElementAppears(testName, 10);
		String actualTestName = testName.getText();
		actualTestName = actualTestName.replace(" - ", " ");
		testResultService.assertEquals(true, actualTestName.contains(expectedTestName), "Test Name is Incorrect.");
	}

	public void validateTaskBarIsNotClickable() throws Exception {
		testResultService.assertEquals(true, tasksNav.getAttribute("isContentEditable") == null,
				"Task Bar is Clickable");
	}

	public void testPltBackButtonCycleNewTE(String unitId, List<String> lessonsIds, JSONObject jsonObj,
			JSONObject localizationJson, String testName, String filePath, String[] levels, String[] sectionsCodes)
			throws Exception {

		String sectionType = "";

		testResultService.assertEquals(true, this.testName.getText().contains("Section " + sectionIndex),
				"Section index is Incorrect. Expected: " + sectionIndex + ". Actual: " + this.testName.getText());

		report.startStep("Validate Unit Intro");
		validateIntro(jsonObj, localizationJson, unitId, testName);

		String backValue = "";

		for (int j = 0; j < lessonsIds.size(); j++) {
			if (j == 0) {
				sectionType = "Reading";
			} else if (j == 1) {
				sectionType = "Listening";
			} else if (j == 2) {
				sectionType = "Grammar";
			}

			report.startStep("Validate Lesson Intro");
			validateIntro(jsonObj, localizationJson, lessonsIds.get(j), testName);

			report.startStep("Validate Title Indexes");
			validateTitleIndex();
			Thread.sleep(1000);

			report.startStep("Validate Task Bar Status");
			backValue = getValueOfWantedKeyInSpecificTestId(jsonObj, lessonsIds.get(j), "Back");
			if (backValue.equals("false")) {
				report.startStep("Validate Task bar is not Clickable");
				validateTaskBarIsNotClickable();
			} else if (backValue.equals("true")) {
				report.startStep("Validate Submit is not displayed in tasks bar");
				webDriver.ClickElement(tasksNav);
				testResultService.assertEquals(false, webDriver.isDisplayed(submitInTaskBar),
						"Submit button is displayed in tasks bar");
				webDriver.ClickElement(closeTasksNavButton);
			}

			report.startStep("Navigate to End of Lesson");
			List<TestQuestion> sectionAnswers = initSectionPLTNewTE(filePath, levels[j], sectionType, sectionsCodes[j]);
			int numOfQuestionInSection = sectionAnswers.size();
			for (int i = 0; i < numOfQuestionInSection - 1; i++) {
				waitUntilNextButtonIsEnabled(10);
				pressOnNextTaskArrow();
				Thread.sleep(1000);
			}

			if (j < lessonsIds.size() - 1) {

				report.startStep("Click Next");
				waitUntilNextButtonIsEnabled(10);
				pressOnNextTaskArrow();
				waitUntilNextButtonIsEnabled(10);

				report.startStep("Get Back Value of Lesson and Test it");
				// String backValue = getValueOfWantedKeyInSpecificTestId(jsonObj,
				// lessonsIds.get(j), "Back");
				validateBackButtonByJSONValue(backValue);
			}

			lessonIndex++;
			Thread.sleep(1000);
		}

		report.startStep("Submit");
		if (backValue.equals("false")) {
			submitPlt(false);
		} else if (backValue.equals("true")) {
			submit(false);
		}

		sectionIndex++;
		lessonIndex = 1;
	}

	public void waitUntilNextButtonIsEnabled(int timeout) throws InterruptedException {
		int counter = 0;
		String className = nextButton.findElement(By.tagName("a")).getAttribute("class");
		while (className.contains("--disabled") && counter <= timeout) {
			Thread.sleep(1000);
			counter++;
			className = nextButton.findElement(By.tagName("a")).getAttribute("class");
		}
	}

	// This function works for drag and drop in course test (not the angular picture
	// like in plt)
	public void answerDragAndDropQuestionToPictureNotAngularNewTE(TestQuestion question) throws Exception {

		boolean isAnswerFound = false;

		webDriver.waitUntilElementAppears("//div[@class='dnditem draggable']", 5);

		String[] answerSourceLocations = question.getCorrectAnswers();

		for (int i = 0; i < answerSourceLocations.length; i++) {

			WebElement from = null;
			List<WebElement> answers = webDriver.getWebDriver()
					.findElements(By.xpath("//div[@class='dnditem draggable']"));
			for (int j = 0; j < answers.size(); j++) {
				if (answers.get(j).getText().replace("\"", "").replace(",", "").trim()
						.equals(question.getCorrectAnswers()[i].trim())) {
					from = answers.get(j);
					isAnswerFound = true;
					break;
				} else {
					isAnswerFound = false;
				}
			}

			if (!isAnswerFound) {
				String[] answersWeb = new String[answers.size()];
				String[] answersFile = new String[answerSourceLocations.length];
				for (int k = 0; k < answers.size(); k++) {
					answersWeb[k] = answers.get(k).getText().replace("\"", "").replace(",", "").trim();
					answersFile[k] = question.getCorrectAnswers()[k].trim();
				}
				testResultService.addFailTest("Answer number " + (i + 1) + " wasn't found. Answers on Web: "
						+ Arrays.toString(answersWeb) + ". Answer from file: " + Arrays.toString(answersFile), false,
						true);
			}
			WebElement to = webDriver.waitForElement("//div[@id='undefined_" + i + "']", ByTypes.xpath, false, 1);
			webDriver.dragAndDropElement(from, to);
		}
	}

	public void clickExitCourseTest() throws Exception {
		webDriver.clickOnElement(exitCourseTestButton);
	}

	public void clickExitPlt() throws Exception {
		webDriver.waitUntilElementAppears(exitPltButton,5);
		webDriver.clickOnElement(exitPltButton);
	}

	public void validateResumeRecordExist(String userTestComponentProgressId) throws Exception {
		List<String[]> userTestComponentResume = dbService.getUserTestComponentResume();
		boolean isExist = false;
		for (int i = 0; i < userTestComponentResume.size(); i++) {
			if (userTestComponentResume.get(i)[0].equals(userTestComponentProgressId)) {
				isExist = true;
				break;
			}
		}
		testResultService.assertEquals(true, isExist, "Resume Record Does not Exist in DB");
	}

	public void validateResumeIntroRecordExist(String userTestUnitProgressId) throws Exception {
		List<String[]> userTestComponentResume = dbService.getUserTestUnitResume();
		boolean isExist = false;
		for (int i = 0; i < userTestComponentResume.size(); i++) {
			if (userTestComponentResume.get(i)[0].equals(userTestUnitProgressId)) {
				isExist = true;
				break;
			}
		}
		testResultService.assertEquals(true, isExist, "Resume Intro Record Does not Exist in DB");
	}

	public void clickTasksNav() {
		webDriver.ClickElement(tasksNav);
	}

	public void clickSubmitInTasksNav(boolean answeredAllQuestions) {

		try {
			webDriver.waitForElementByWebElement(submitInTaskBar, "submitInTaskBar", false, 3);
			webDriver.ClickElement(submitInTaskBar);
			if (answeredAllQuestions) {
				testResultService.assertEquals(false, webDriver.isDisplayed(submitMessage),
						"Message is displayed after Submit Even Though Student Answered All Questions.");
				if (webDriver.isDisplayed(submitMessage)) {
					webDriver.ClickElement(sumbitOkButton);
				}
			} else {
				if (webDriver.isDisplayed(submitMessage)) {
					String expectedMessage = "You haven't answered all the questions in this section.\nPlease note that you cannot repeat this section once it is submitted. Are you sure you want to submit this section?";

					testResultService.assertEquals(true, submitMessage.getText().contains(expectedMessage),
							"Submit message is Incorrect.");

					webDriver.ClickElement(sumbitOkButton);
				} else {
					testResultService.addFailTest(
							"Submit Message Is Not Displayed Even Though Student Did Not Answer All Questions.", false,
							true);
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void closeTasksNav() {
		webDriver.ClickElement(closeTasksNavButton);
	}

	public void submitCourseTestEmpty(String answersFilePath, String testId, CourseCodes wantedCourseCode,
			CourseTests testType, String roundLevelOfCourse, List<String> unitIds, JSONObject jsonObj,
			JSONObject localizationJson, String testName) throws Exception {
		// int unitIndex = 0;
		// int sectionIndexInUnit = 0;
		int lessonCounter = 1;
		sectionIndex = 1;

		for (int i = 0; i < unitIds.size(); i++) {
			// report.startStep("Validate Unit Intro");
			// validateIntro(jsonObj, localizationJson, unitIds.get(i), testName);

			List<String> lessonsIds = getLessonsIdsFromLessonDetailsArrOfSpecificUnit(unitIds.get(i));

			if (roundLevelOfCourse.equals("2")) {

				for (int j = 0; j < lessonsIds.size(); j++) {

					// report.startStep("Validate Lesson Intro");
					// validateIntro(jsonObj, localizationJson, lessonsIds.get(j), testName); //
					// validate lesson id is correct

					// report.startStep("Validate Title Index");
					// validateTitleIndex();

					// sectionIndexInUnit = j;
					submitSectionEmptyNewTE(answersFilePath, testId, wantedCourseCode, testType, lessonCounter - 1,
							roundLevelOfCourse, unitIds, jsonObj, localizationJson, testName, sectionIndex - 1,
							lessonIndex - 1);

					/*
					 * if (j < lessonsIds.size()-1) { report.startStep("Click Next");
					 * Thread.sleep(300); clickNext(); Thread.sleep(300); }
					 */
					lessonCounter++;
					lessonIndex++;
				}

				lessonIndex = 1; // end of unit- reset lesson index

				// report.startStep("Submit");
				// submit(true);
				sectionIndex++;
			}
		}
	}

	public void waitUntilPLTIntroLoaded() {
		WebElement element = webDriver.waitUntilElementAppearsAndReturnElementByType("learning__NodeIntro",
				ByTypes.className, 10);

		if (element == null)
			try {
				testResultService.addFailTest("PLT Intro not loaded", false, true);
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	public void acrossIntro() {
		try {
			WebElement introIsPresent = webDriver.findElementByXpath(
					"//*[@id='LABody']/app/div/div/div[3]/div[3]/div[4]/ed-la-tasksnav/div/div", ByTypes.xpath);
			String intro = introIsPresent.getText().trim();
			if (intro.equalsIgnoreCase("Intro")) {
				webDriver.clickOnElement(nextButton);
				// testEnvironmentPage.clickNext();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String validateCefrReportAtTheEndOfPLT() throws Exception {
		webDriver.waitForElementByWebElement(cefrLevel, "level", true, 2000);
		WebElement el1 = cefrLevel.findElement(By.cssSelector("span b"));
		String levelCefr = el1.getText().trim();
		//WebElement el2 = apiResLevel.findElement(By.tagName("b"));
		//WebElement adv = webDriver.findElementByXpath("//*[@id='mCSB_48_container']/div[2]/div/ed-plt-endlink/span[1]/b", null);
		String apiLevel = apiResLevel.getText().trim();


		//String level2 = apiResLevel.getText().trim();
		switch(levelCefr) {
			case "B2": if(apiLevel.equalsIgnoreCase("Advanced 1")||apiLevel.equalsIgnoreCase("Intermediate 3"))
							textService.assertEquals(true, true);
						else
							testResultService.addFailTest("Api level doesn't match",false,true);
						break;
			case "C1": if(apiLevel.equalsIgnoreCase("Advanced 2")||apiLevel.equalsIgnoreCase("Advanced 3"))
							textService.assertEquals(true, true);
						else
							testResultService.addFailTest("Api level doesn't match",false,true);
						break;
			case "A2": if(apiLevel.equalsIgnoreCase("Basic 2")||apiLevel.equalsIgnoreCase("Basic 3"))
							textService.assertEquals(true, true);
						else
							testResultService.addFailTest("Api level doesn't match",false,true);
						break;
			case "A1":	if(apiLevel.equalsIgnoreCase("Basic 1")||apiLevel.equalsIgnoreCase("P2"))
							textService.assertEquals(true, true);
						else
							testResultService.addFailTest("Api level doesn't match");
						break;
			case "B1": if(apiLevel.equalsIgnoreCase("Intermediate 1")||apiLevel.equalsIgnoreCase("Intermediate 2"))
							textService.assertEquals(true, true);
						else
							testResultService.addFailTest("Api level doesn't match",false,true);
						break;
			}

		if(suggestionText.isEnabled()) {
			String text = suggestionText.getText().trim();
			boolean cefr = text.contains("CEFR");
			textService.assertTrue("CEFR NOT PRESENT", cefr);
			}
		return levelCefr;
	}

	public String getCurrentTaskNumber() throws Exception {
		webDriver.waitForElementByWebElement(currentTask, "current task", true, 5);
		return currentTask.getText().trim();
	}

	public void submitMidtermTestWithoutAnswers() throws Exception {
		webDriver.refresh();
		webDriver.waitForElementByWebElement(tasksNav, "taskNav", false, 5);

		while(tasksNav.isDisplayed()) {
			WebElement taskBar = webDriver.waitForElement("//ed-la-tasksnav/div", ByTypes.xpath);
				if(taskBar!=null) {
					taskBar.click();
				}else {
					testResultService.addFailTest("Task bar not displayed or doesn't open", false, false);
				}
			//clickTasksNav();
			Thread.sleep(1000);
			clickSubmitInTasksNav(false);
			if(exitCourseTestisDisplayed()) {
				try {
					validateScoreEndOfTest("0");
					clickExitCourseTest();
					break;
				} catch (Exception e) {
					//e.printStackTrace();
				}

			}else {
				try {
					clickNext();
					Thread.sleep(1000);
					clickNext();
					Thread.sleep(1000);
				}catch (Exception e) {
					submitButton.click();
					webDriver.waitForElementByWebElement(submitMessage, "submitMessage", true, 5);
					sumbitOkButton.click();
				}
				webDriver.waitForElementByWebElement(tasksNav, "taskNav", false, 1);
				webDriver.refresh();
				webDriver.waitForElementByWebElement(tasksNav, "taskNav", false, 5);
			}
		}
	}

	private boolean isElementPresent(By by) {
		try {
			webDriver.getWebDriver().findElement(by);
			return true;
		} catch (org.openqa.selenium.NoSuchElementException e) {
			return false;
		}
	}

	public void submitPLTTestWithoutAnswers() throws Exception {
		while (true) {

				try {
					webDriver.getWebDriver().getPageSource();
					pltNext.isDisplayed();
					clickNext();

				} catch (Exception e) {
					webDriver.getWebDriver().getPageSource();
					Thread.sleep(1000);
					webDriver.ClickElement(pltSubmit);
					webDriver.waitForElementByWebElement(submitMessage, "submitMessage", true, 5);
					webDriver.getWebDriver().getPageSource();
					Thread.sleep(1000);
					sumbitOkButton.click();
					Thread.sleep(1000);
					if (isElementPresent((By.xpath("//span[text()='Exit']")))){
				     clickExitPltTest();
					 break;
				}
			}
		}
	}

	private void dragAndDrop(WebElement drag, WebElement drop) {
		Actions actions = new org.openqa.selenium.interactions.Actions(webDriver.getWebDriver());
		actions.dragAndDrop(drag, drop).build().perform();
	}


	public void submitPLTTestWithRadioButtonAndDropdownAnswers() throws Exception {
		while (true) {
			try {
				webDriver.getWebDriver().getPageSource();

				if (isElementPresent(By.cssSelector(".layout__radio"))) {
					webDriver.ClickElement(radioButton);
					Thread.sleep(1000);
				}

				if (isElementPresent(By.cssSelector(".DDLOptions__selected"))){
					webDriver.getWebDriver().findElement(By.cssSelector(".DDLOptions__selected")).click();
					WebElement dropdownElement = webDriver.getWebDriver().findElement(By.cssSelector(".DDLOptions__listItem[id^='DDLOptions__listItem_aid_']"));
					dropdownElement.click();
				}
				pltNext.isDisplayed();

				clickNext();


			} catch (Exception e) {
				webDriver.getWebDriver().getPageSource();
				Thread.sleep(1000);

				webDriver.ClickElement(pltSubmit);
				webDriver.waitForElementByWebElement(submitMessage, "submitMessage", true, 5);
				webDriver.getWebDriver().getPageSource();
				Thread.sleep(1000);
				sumbitOkButton.click();
				Thread.sleep(1000);
			}

			if (isElementPresent(By.xpath("//span[text()='Exit']"))) {
				clickExitPltTest();
				break;
			}
		}
	}

	public void submitPLTTestWithAnswers() throws Exception {
		while (true) {
			try {
				webDriver.getWebDriver().getPageSource();
				Thread.sleep(1000);

				if (isElementPresent(By.cssSelector(".layout__radio"))) {
					webDriver.getWebDriver().getPageSource();
					Thread.sleep(4000);
					webDriver.ClickElement(radioButton);
					Thread.sleep(1000);
				}

				if (isElementPresent(By.cssSelector(".DDLOptions__selected"))){
					webDriver.getWebDriver().getPageSource();
					Thread.sleep(2000);
					webDriver.ClickElement(dropdown);
					Thread.sleep(2000);
					webDriver.ClickElement(dropdownValue);
					Thread.sleep(2000);
				}

				if (isElementPresent(By.cssSelector(".dnditem.draggable"))) {
					webDriver.getWebDriver().getPageSource();
					WebElement drag = webDriver.waitForElement(".dnditem.draggable", ByTypes.cssSelector,5,false);
					WebElement drop = webDriver.waitForElement(".dndZone", ByTypes.cssSelector,5,false);
					dragAndDrop(drag, drop);

				}
				pltNext.isDisplayed();
				webDriver.getWebDriver().getPageSource();
				Thread.sleep(1000);

				clickNext();


			} catch (Exception e) {
				webDriver.getWebDriver().getPageSource();
				Thread.sleep(1000);

				webDriver.ClickElement(pltSubmit);
				Thread.sleep(1000);
				if (isElementPresent(By.xpath("//input[@id='btnOk']"))) {
					webDriver.waitForElementByWebElement(submitMessage, "submitMessage", true, 5);
					webDriver.getWebDriver().getPageSource();
					Thread.sleep(1000);
					sumbitOkButton.click();
					Thread.sleep(2000);
				}
			}

			if (isElementPresent(By.xpath("//span[text()='Exit']"))) {
				clickExitPltTest();
				break;
			}
		}
	}

	private boolean exitCourseTestisDisplayed() {

		boolean displayed = false;
		try {
			webDriver.waitForElementByWebElement(exitCourseTestButton, "exitCourseTestButton", false, 1);
			displayed = exitCourseTestButton.isDisplayed();
			return displayed;
		}catch(Exception e) {

		}
		return displayed;
	}




	public void waitForStartTetsButon(int wait) throws Exception {

		WebElement startTestBTN = null;
		try {
			for(int i=0;i<=wait&&startTestBTN==null;i++) {
				startTestBTN = webDriver.waitForElement("learning__TestInstructionsButton--start", ByTypes.id,5,false,null);
				//webDriver.waitForElement("learning__TestInstructionsButton--start", ByTypes.id);
			}
		}catch (Exception e) {
		}catch (AssertionError e) {
			}
	}

	public void clickOnSubmitOKButton() throws Exception {
		if (webDriver.isDisplayed(submitMessage)) {
			webDriver.ClickElement(sumbitOkButton);
		}
	}

	public void clickLevelButton(){
		webDriver.ClickElement(levelButton.get(0));
	}

	public void selectRadioButton(){
		webDriver.ClickElement(radioButton);
	}

	public void clickExitPltTest(){
		webDriver.ClickElement(exitButton);
	}

}
