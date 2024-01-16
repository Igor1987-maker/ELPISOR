package pageObjects.tms;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Enums.ByTypes;
import Enums.CourseCodes;
import Enums.CourseTests;
import Enums.TestQuestionType;
import Objects.CourseTest;
import Objects.CourseTestSection;
import Objects.TestQuestion;
import drivers.GenericWebDriver;
import pageObjects.GenericPage;
import pageObjects.edo.NewUxClassicTestPage;
import pageObjects.edo.NewUxLearningArea2;
import pageObjects.edo.TestEnvironmentPage;
import services.TestResultService;
import services.TextService;


public class TmsCourseTestReport extends GenericPage {

	String mainWin=null;
	String [] defaultsCompletedBySection = new String [] {"Listening Section Test Results from", "Reading Section Test Results from","Use of English Section Test Results from"};
	String [] defaultsNotCompletedBySection = new String [] {"did not complete the Listening Section", "did not complete the Reading Section","did not complete the Use of English Section"};
	List<WebElement> titles;	
	
	@FindBy(xpath = "//div[@class='viewCTTes__row']//td[@class='stext']")
	public List<WebElement> sectionsTitles;
	
	@FindBy(xpath = "//ul[@class='testResultsBtns']//li//a")
	public List<WebElement> tasksButtonLeftBar;
	
	@FindBy(xpath = "//div[contains(@class,'ADUMClass lessonMultipleAnswer')]")
	public List<WebElement> questionsAnswers;
	
	@FindBy(xpath = "//div[contains(@class,'fillInBlank')]//span")
	public List<WebElement> comboBoxAnswers;
	
	@FindBy(xpath = "//div[contains(@class,'TextDiv')]//span")
	public List<WebElement> dragAndDropMultipleAnswers;
	
	@FindBy(xpath = "//div[contains(@class,'wordsBankTable')]/div")
	public List<WebElement> wordsBankDragAndDropMultipleAnswers;
	
	@FindBy(xpath = "//div[contains(@class,'draggable       wordBankTilePlaced ')]")
	public List<WebElement> wordsBankPropertiesDragAndDropMultipleAnswers;
	
	@FindBy(xpath = "//span[contains(@class, 'learning__tasksNav_taskVX learning__tasksNav_taskVX')]")
	public List<WebElement> questionSignsInNewReport;
	
	@FindBy(xpath = "//div[contains(@class,'multiRadioWrapper')]")
	public List<WebElement> questionsAnswersNewTE;
	
	@FindBy(xpath = "//div[contains(@class,'multiRadioWrapper')]//div[contains(@class,'checkAnswerIcon')]")
	public List<WebElement> questionsAnswersSignsNewTE;
	
	@FindBy(xpath = "//div[contains(@class, 'tasksNav_task--')]")
	public List<WebElement> tasksInNavBar;
	
	@FindBy(xpath = "//div[contains(@class,'multiRadioWrapper')]")
	public List<WebElement> questionsAnswersInCorrectTabNewTE;
	
	@FindBy(xpath = "//div[contains(@class,'DDLOptionsW')]")
	public List<WebElement> comboBoxAnswersNewTE;
	
	@FindBy(xpath = "//div[contains(@class,'dnditem')]")
	public List<WebElement> dragAndDropAnswersNewTE;
	
	TestEnvironmentPage testEnvironmentPage = new TestEnvironmentPage(webDriver, testResultService);
	
	public TmsCourseTestReport(GenericWebDriver webDriver,TestResultService testResultService) throws Exception {
		super(webDriver ,testResultService);
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
	
	public void verifySectionsTitleMidTerm(String userName, String testDate, boolean isFirstSectionCompleted, boolean isSecondSectionCompleted, boolean isThirdSectionCompleted) throws Exception {
		
		titles = webDriver.getElementsByXpath("//td[@class='stext']");
			
		String [] section1 = formatExpectedSectionTitle(userName, 1, testDate);
		
		String expCS1 = section1[1];
		String expNS1 = section1[1];
		String actS1 = section1[1];
		
		String [] section2 = formatExpectedSectionTitle(userName, 2, testDate);
		
		String expCS2 = section2[1];
		String expNS2 = section2[1];
		String actS2 = section2[1];
		
		String [] section3 = formatExpectedSectionTitle(userName, 3, testDate);
		
		String expCS3 = section3[0];
		String expNS3 = section3[0];
		String actS3 = section3[0];
		
		if (isFirstSectionCompleted)
			testResultService.assertEquals(expCS1, actS1, "Title of section 1 is not correct");
		else testResultService.assertEquals(expNS1, actS1, "Title of section 1 is not correct");
		
		if (isSecondSectionCompleted)
			testResultService.assertEquals(expCS2, actS2, "Title of section 2 is not correct");
		else testResultService.assertEquals(expNS2, actS2, "Title of section 2 is not correct");
		
		if (isThirdSectionCompleted)
			testResultService.assertEquals(expCS3, actS3, "Title of section 3 is not correct");
		else testResultService.assertEquals(expNS3, actS3, "Title of section 3 is not correct");
		
	}
	
	public void verifySectionsTitleComponentTest (String userName, String testDate, boolean isFirstSectionCompleted, boolean isSecondSectionCompleted) throws Exception {
		
		titles = webDriver.getElementsByXpath("//td[@class='stextLeft']");
				
		String expectedFirstTest = userName + " " + userName + " First Test " + testDate;
		String expectedLastTest = userName + " " + userName + " Last Test " + testDate;
		
		if (isFirstSectionCompleted) testResultService.assertEquals(expectedFirstTest, titles.get(0).getText().trim(), "Title of section 1 is not correct");
		if (isSecondSectionCompleted) testResultService.assertEquals(expectedLastTest, titles.get(1).getText().trim(), "Title of section 2 is not correct");
				
		
	}
	
	
	private String [] formatExpectedSectionTitle (String userName, int sectionNumber, String testDate) throws Exception {
		
		int sectionIndex = sectionNumber-1;
		
		String expectedTitleCompleted = userName + userName + "\'s" + defaultsCompletedBySection[sectionIndex]+ testDate;
		expectedTitleCompleted = expectedTitleCompleted.replaceAll(" ", "").trim().toString();
		
		String expectedTitleNotCompleted = userName + userName + defaultsNotCompletedBySection[sectionIndex];
		expectedTitleNotCompleted = expectedTitleNotCompleted.replaceAll(" ", "").trim().toString();
		
		String actual = titles.get(sectionIndex).getText().replaceAll(" ", "").trim().toString();
		
		String [] titlesUnderTest = new String [] {expectedTitleCompleted,expectedTitleNotCompleted, actual};
		
	return titlesUnderTest;
	
	}
	
	
	public void clickAndVerifyResultViewPerQuestionMCQ (int qIndex, boolean isCorrect, boolean isDone, boolean verifyCorrectTab) throws Exception {
		
		String qValue = null;
		String aValue = null;
		
		if (isCorrect)  {
			qValue = "rightAnswer"; 
			aValue = "vCheck";
		}
		else {
			qValue = "wrongAnswer";
			aValue = "xCheck";
		}
		
		webDriver.waitForElement("//a[@ind='"+qIndex+"'][@class='"+qValue+"']", ByTypes.xpath, "Press on question and check mark").click();
		webDriver.waitForElement("//ul[@class='ulTasks']/li["+(qIndex+1)+"][contains(@class,'active')]", ByTypes.xpath,1,false, "Check Question tab synchronized");
		if (isDone)
			webDriver.waitForElement("//div[contains(@class,'ADUM')][contains(@class,'"+aValue+"')]", ByTypes.xpath,1,false, "Check Your Answer view");
		
		if (verifyCorrectTab) {
			clickOnCorrectAnswerTab();
			boolean isCorrectHighlighted = webDriver.waitForElement("//div[contains(@class,'ADUM')][contains(@class,'vCheck')]", ByTypes.xpath,1,false, "Check Correct Answer view").getAttribute("class").startsWith("c");
		
		if (!isCorrectHighlighted)
			testResultService.addFailTest("Correct Answer not highlighted");
		}
		
	}
	
	public void switchToTestResultsFrameBySection(int sectionNumber) throws Exception {
		
		webDriver.switchToFrame(webDriver.waitForElement("test"+(sectionNumber-1), ByTypes.id));
		
	}
	
	public void switchToFirstComponentTestResultFrame() throws Exception {
		
		webDriver.switchToFrame(webDriver.waitForElement("FT", ByTypes.id));
		
	}
	
	public void clickOnOpenSectionIconBySection(int sectionNumber) throws Exception {
		
		webDriver.waitForElement("img"+(sectionNumber-1), ByTypes.id).click();
		
	}
	
	public void clickOnCorrectAnswerTab() throws Exception {
		
		webDriver.waitForElement("Correct Answer", ByTypes.linkText).click();
		
	}
		
	public TmsHomePage close() throws Exception{
				
			webDriver.closeNewTab(1);
			webDriver.switchToMainWindow();
				
		return new TmsHomePage(webDriver,testResultService);
	}
	
	public int validateSectionsTitles(String userId, String testId, String userName, String date, List<String[]> userTestResults, int coulmnIndex) throws Exception{
		String[] sectionsTypes = getSectionsTypeByUserIdAndTestId(userId, testId, userTestResults, coulmnIndex);
		String expectedTitle = "";
		String actualTitle = "";
		String userLastName = dbService.getUserLastNameByUserId(userId);
		
		for (int i = 0; i < sectionsTypes.length; i++){
			expectedTitle = userName + " " + userLastName + "'s " + sectionsTypes[i] + " Section Test Results from " + date;
			actualTitle = sectionsTitles.get(i).getText();
			testResultService.assertEquals(expectedTitle, actualTitle.replaceAll("  ",""), "Title number " + (i+1) + " is Incorrect");
		}
		return sectionsTypes.length;
	}
	
	public String[] getSectionsTypeByUserIdAndTestId(String userId, String testId, List<String[]> userTestResults, int columnIndex) {
		String[] sectionsType = new String[userTestResults.size()];
		for (int i = 0; i < userTestResults.size(); i++) {
			if (userTestResults.get(i)[columnIndex].equals("Grammar") || userTestResults.get(i)[columnIndex].equals("Vocabulary")) {
				userTestResults.get(i)[columnIndex] = "Use of English";
			}
			sectionsType[i] = userTestResults.get(i)[columnIndex];
		}
		return sectionsType;
	}
	
	public void checkAnswerOfTask(int taskIndex, boolean isCorrect, String[] correctAnswer, TestQuestionType questionType, String wrongAnswer) throws Exception {
		
		tasksButtonLeftBar.get(taskIndex).click();
		//webDriver.waitUntilElementAppears("//div[@id='pmContainer']", 10);
		String taskSign = tasksButtonLeftBar.get(taskIndex).getAttribute("className");
		
		String finalWrongAnswer= null;
		
		switch (questionType) {
			case RadioSingle:
				validateAnswerRadioSingle(isCorrect, taskSign, correctAnswer[0], wrongAnswer);
				break;
			case DragAndDropMultiple:
				validateAnswerDragAndDropMultiple(isCorrect, taskSign, correctAnswer, wrongAnswer);
				break;
			case comboBox:
				finalWrongAnswer = getWrongPartsOfWrongAnswer(String.join(",", correctAnswer), wrongAnswer);
				validateAnswerComboBox(isCorrect, taskSign, correctAnswer, finalWrongAnswer);
				break;
			case RadioMultiple:
				break;
			case DragAndDropSingle:
				break;
			case TrueFalse:
				break;
			case DrapAndDropToPicture:
				break;
			default:
				break;
		}	
	}
	
	public void validateVSignDisplayedNextToCorrectAnswer(String correctAnswer) throws Exception{
		for (int i = 0; i < questionsAnswers.size(); i++) {
			if (questionsAnswers.get(i).getText().contains(correctAnswer)) {
				testResultService.assertEquals(true, questionsAnswers.get(i).getAttribute("className").contains("vCheck"),"V Sign is not displyed next to correct answer");
				break;
			}
		}
	}
	
	public void validateVSignIsNotDisplayedNextToCorrectAnswer(String correctAnswer) throws Exception{
		for (int i = 0; i < questionsAnswers.size(); i++) {
			if (questionsAnswers.get(i).getText().contains(correctAnswer)) {
				testResultService.assertEquals(false, questionsAnswers.get(i).getAttribute("className").contains("vCheck"),"V Sign is displyed next to correct answer (even though it should be incorrect)");
				break;
			}
		}
	}
	
	public void validateXSignDisplayedNextToWrongAnswer(String wrongAnswer) throws Exception{
		for (int i = 0; i < questionsAnswers.size(); i++) {
			if (questionsAnswers.get(i).getText().contains(wrongAnswer)) {
				testResultService.assertEquals(true, questionsAnswers.get(i).getAttribute("className").contains("xCheck"),"V Sign is not displyed next to correct answer");
				break;
			}
		}
	}

	public void validateAnswerRadioSingle(boolean isCorrect, String taskSign, String correctAnswer, String wrongAnswer) throws Exception{
		if (isCorrect) {
			
			report.startStep("Validate V Sign is Displayed next to Current Task on Left Bar");
			testResultService.assertEquals("rightAnswer", taskSign, "Task Sign in Left Bar is Incorrect");
			
			report.startStep("Validate V Sign is Displayed next to Correct Answer");
			validateVSignDisplayedNextToCorrectAnswer(correctAnswer);
			
			report.startStep("Open Correct Answer Tab and Validate V Sign is Displayed next to Correct Answer");
			clickOnCorrectAnswerTab();
			validateVSignDisplayedNextToCorrectAnswer(correctAnswer);
			
		} else {
			
			wrongAnswer = wrongAnswer.split("~")[0];
			
			report.startStep("Validate X Sign is Displayed next to Current Task on Left Bar");
			testResultService.assertEquals("wrongAnswer", taskSign, "Task Sign in Left Bar is Incorrect");
			
			report.startStep("Validate V Sign is Not Displayed next to Correct Answer");
			validateVSignIsNotDisplayedNextToCorrectAnswer(correctAnswer);
			
			if (!wrongAnswer.equals("")) {
				validateXSignDisplayedNextToWrongAnswer(wrongAnswer);
			}
			
			report.startStep("Open Correct Answer Tab and Validate V Sign is Displayed next to Correct Answer");
			clickOnCorrectAnswerTab();
			validateVSignDisplayedNextToCorrectAnswer(correctAnswer);
						
		}	
	}
	
	// need to debug
	public void validateAnswerComboBox(boolean isCorrect, String taskSign, String[] correctAnswer, String wrongAnswer) throws Exception{
		if (isCorrect) { // debug
			
			report.startStep("Validate V Sign is Displayed next to Current Task on Left Bar");
			testResultService.assertEquals("rightAnswer", taskSign, "Task Sign in Left Bar is Incorrect");
			
			report.startStep("Validate V Sign is Displayed next to Correct Answer");
			validateComboBoxAnswers(correctAnswer);
			
			report.startStep("Open Correct Answer Tab and Validate V Sign is Displayed next to Correct Answer");
			clickOnCorrectAnswerTab();
			validateComboBoxAnswers(correctAnswer);
			
		} else {
			
			//report.startStep("Validate X Sign is Displayed next to Current Task on Left Bar");
			//testResultService.assertEquals("wrongAnswer", taskSign, "Task Sign in Left Bar is Incorrect");
			
			if (wrongAnswer.equals("")) {
				
				report.startStep("Validate X Sign is Displayed next to Current Task on Left Bar");
				testResultService.assertEquals("wrongAnswer", taskSign, "Task Sign in Left Bar is Incorrect");
				
				validateAllComboBoxAnswersAreEmpty(); 
			} else {
				
				report.startStep("Validate VX Sign is Displayed next to Current Task on Left Bar");
				testResultService.assertEquals("halfAnswer", taskSign, "Task Sign in Left Bar is Incorrect");
				Thread.sleep(500);
			
				// when a wrong answer will be selected a red sign will be displayed (?)
				// so here i validate that the answer that is wrong has a red sign (i have the wrong answer as string)
				validateWrongComboBoxHasXSign(wrongAnswer);
			}
			
			report.startStep("Open Correct Answer Tab and Validate V Sign is Displayed next to Correct Answer");
			clickOnCorrectAnswerTab();
			validateComboBoxAnswers(correctAnswer);
						
		}
	}
	
	public void validateComboBoxAnswers(String[] correctAnswer){
		for (int i = 0; i < comboBoxAnswers.size(); i++) {
			testResultService.assertEquals(correctAnswer[i], comboBoxAnswers.get(i).getText(), "Question number " + (i+1) +" is Incorrect");
		}
	}
	
	public void validateAllComboBoxAnswersAreEmpty(){
		for (int i = 0; i < comboBoxAnswers.size(); i++) {
			testResultService.assertEquals("", comboBoxAnswers.get(i).getText().trim(), "Question number " + (i+1) +" is not Empty");
		}
	}
	
	public void validateWrongComboBoxHasXSign(String wrongAnswers) throws Exception{
		String[] wrongAnswersSeperated = wrongAnswers.split(",");
		for (int i = 0; i < comboBoxAnswers.size(); i++) {
			for (int j = 0; j < wrongAnswersSeperated.length; j++) {
				if (comboBoxAnswers.get(i).getText().equals(wrongAnswersSeperated[j])){
					testResultService.assertEquals(true, comboBoxAnswers.get(i).getAttribute("className").contains("xCheck"), "Wrong Answer: "+wrongAnswersSeperated[j]+",does not have x sign");
				}
			}
		}
	}
	
	// need to debug
	// wrong answer in drag and drop will be the answer that was not draggeed- meaning it is still in the words bank
	public void validateAnswerDragAndDropMultiple(boolean isCorrect, String taskSign, String[] correctAnswer, String wrongAnswer) throws Exception{
		if (isCorrect) { // debug
			
			report.startStep("Validate V Sign is Displayed next to Current Task on Left Bar");
			testResultService.assertEquals("rightAnswer", taskSign, "Task Sign in Left Bar is Incorrect");
			
			report.startStep("Validate V Sign is Displayed next to Correct Answer");
			validateDragAndDropMultipleAnswers(correctAnswer);
			
			report.startStep("Open Correct Answer Tab and Validate V Sign is Displayed next to Correct Answer");
			clickOnCorrectAnswerTab();
			validateDragAndDropMultipleAnswers(correctAnswer);
			
		} else {
			String finalWrongAnswers = "";
			
			if (wrongAnswer.equals("~DidNotAnswer")) { // if no answer was dragged
				
				report.startStep("Validate X Sign is Displayed next to Current Task on Left Bar");
				testResultService.assertEquals("wrongAnswer", taskSign, "Task Sign in Left Bar is Incorrect");
				
				report.startStep("Validate all Answers are Empty");
				validateAllDragAndDropMultipleAnswersAreEmpty(); 
				
			} else if (wrongAnswer.split("~")[1].equals("swappedAnswers")){ // if answers were swapped
				
				report.startStep("Validate VX Sign is Displayed next to Current Task on Left Bar");
				testResultService.assertEquals("halfAnswer", taskSign, "Task Sign in Left Bar is Incorrect");
				
				report.startStep("Get Wrong Answers and Validate all Wrong Answers have X Sign");
				finalWrongAnswers = getWrongPartsOfWrongAnswer(String.join(",", correctAnswer), wrongAnswer);
				validateWrongDragAndDropHasXSign(finalWrongAnswers);
				
			} else if (wrongAnswer.split("~")[1].equals("partiallyAnswered")) { // if question was partially answered
				
				report.startStep("Validate VX Sign is Displayed next to Current Task on Left Bar");
				testResultService.assertEquals("halfAnswer", taskSign, "Task Sign in Left Bar is Incorrect");
				
				report.startStep("Get Answers that Weren't Dragged and Validate They are Found in Words Bank");
				finalWrongAnswers = getWrongPartsOfWrongAnswer(String.join(",", correctAnswer), wrongAnswer);
				validateWordIsFoundInWordBank(finalWrongAnswers); 
			} /*else if (wrongAnswer.split("~")[1].equals("swappedAndPartially")){
				report.startStep("Validate VX Sign is Displayed next to Current Task on Left Bar");
				testResultService.assertEquals("halfAnswer", taskSign, "Task Sign in Left Bar is Incorrect");
				
				finalWrongAnswers = getWrongPartsOfWrongAnswer(String.join(",", correctAnswer), wrongAnswer);

				validateWrongDragAndDropHasXSign(finalWrongAnswers);
			}*/
			
			//report.startStep("Open Correct Answer Tab and Validate V Sign is Displayed next to Correct Answer");
			//clickOnCorrectAnswerTab();
			//validateDragAndDropMultipleAnswers(correctAnswer); // currently not located
						
		}
	}
	
	// not working- need to change location of elements
	public void validateDragAndDropMultipleAnswers(String[] correctAnswer){
		for (int i = 0; i < dragAndDropMultipleAnswers.size(); i++) {
			testResultService.assertEquals(correctAnswer[i], dragAndDropMultipleAnswers.get(i).getText(), "Question number " + (i+1) +" is Incorrect");
		}
	}
	
	// debug
	public void validateWordIsFoundInWordBank(String wrongAnswer) throws Exception{
		
		String[] seperatedWrongAnswers = wrongAnswer.split(",");
		for (int j = 0; j < seperatedWrongAnswers.length; j++) {
			boolean isFound = false;
			for (int i = 0; i < wordsBankDragAndDropMultipleAnswers.size(); i++) {
				if (wordsBankDragAndDropMultipleAnswers.get(i).getText().equals(wrongAnswer)) {
					isFound = true;
					break;
				}
			}
			testResultService.assertEquals(true, isFound, "Answer that wasn't answered is not found in words bank");
		}
	}
	
	public void validateAllDragAndDropMultipleAnswersAreEmpty(){
		for (int i = 0; i < dragAndDropMultipleAnswers.size(); i++) {
			testResultService.assertEquals("", dragAndDropMultipleAnswers.get(i).getText().trim(), "Question number " + (i+1) +" is not Empty");
		}
	}
	
	public void validateWrongDragAndDropHasXSign(String wrongAnswer) throws Exception {
		String[] wrongAnswersSeperated = wrongAnswer.split(",");
					
		for (int i = 0; i < wordsBankPropertiesDragAndDropMultipleAnswers.size(); i++) {
			for (int j = 0; j < wrongAnswersSeperated.length; j++) {
				if (wordsBankPropertiesDragAndDropMultipleAnswers.get(i).getText().equals(wrongAnswersSeperated[j])){
					testResultService.assertEquals(true, wordsBankPropertiesDragAndDropMultipleAnswers.get(i).getAttribute("className").contains("xCheck"), "Wrong Answer: "+wrongAnswersSeperated[j]+",does not have x sign");
				}
			}
		}
	}
	
	public String getWrongAnswerFromFile(int sectionNumber, String correctAnswer, String wrongAnswerFilePath, String testId) throws Exception{
		
		TextService textService = new TextService();
		
		correctAnswer = correctAnswer.replaceAll(",", "|");
		
		//String wrongAnswerFilePath = "files/CourseTestData/MidTermWrongAnswers.csv";
		List<String[]> wrongAnswers = textService.getStr2dimArrFromCsv(wrongAnswerFilePath);
		
		String wrongAnswer = "";
		String wrongType = "";
		
		for (int i = 0; i < wrongAnswers.size(); i++) {
			if (wrongAnswers.get(i)[0].equals(testId) && wrongAnswers.get(i)[1].equals(String.valueOf(sectionNumber)) && wrongAnswers.get(i)[2].equals(correctAnswer)) {
				wrongAnswer = wrongAnswers.get(i)[3];
				wrongType = wrongAnswers.get(i)[5];
				break;
			}
		}
		
		return wrongAnswer + "~" + wrongType;
	}
	
	public String getWrongPartsOfWrongAnswer(String correctAnswer, String wrongAnswer) {
		if (wrongAnswer.equals("~DidNotAnswer")){
			wrongAnswer = "";
			return wrongAnswer;
		} else {
			
			String[] seperatedCorrectAnswers = correctAnswer.split(",");
			String[] seperatedWrongAnswers = wrongAnswer.split("~")[0].replace("|",",").split(",");
	
			List<String> finalWrongAnswers = new ArrayList<String>();
			if (seperatedCorrectAnswers.length == seperatedWrongAnswers.length) {
				for (int i = 0; i < seperatedCorrectAnswers.length; i++) {
					if (!seperatedCorrectAnswers[i].equals(seperatedWrongAnswers[i])) {
						finalWrongAnswers.add(seperatedWrongAnswers[i]);
					}
				}
			} else {
				for (int i = 0; i < seperatedCorrectAnswers.length; i++) {
					finalWrongAnswers.add(seperatedCorrectAnswers[i]);
				}
				
				for (int i = 0; i < seperatedCorrectAnswers.length; i++) {
					for (int j =0; j <seperatedWrongAnswers.length; j++) {
						if (seperatedCorrectAnswers[i].equals(seperatedWrongAnswers[j])) {
							finalWrongAnswers.remove(i);
							break;
						}
					}
				}
			}
		
			return String.join(",", finalWrongAnswers);
		}
	}
	
	public void validateResultsOldTe(String userId, String testId, String userName, String wrongAnswerFile) throws Exception{
		boolean isNewTe = false;
	
		//webDriver.waitUntilElementAppears("//div[@id='mainAreaTD']", 30); 
		Thread.sleep(2000);
		
		int columnIndex = 0;
		int testDateCoulmnIndex = 0;
		List<String[]> userTestResults = new ArrayList<String[]>();
		if (isNewTe) {
			String userTestAdministrationId = dbService.getUserTestProgressByUserId(userId).get(0)[0];
			userTestResults = dbService.getUserTestResultsByUserIdAndTestIdNewTe(userId, userTestAdministrationId);
			columnIndex = 8;
			testDateCoulmnIndex = 9;
		} else {
			userTestResults = dbService.getUserTestResultsByUserIdAndTestIdOldTe(userId, testId);
			columnIndex = 12;
			testDateCoulmnIndex = 6;
		}
		String testDate = userTestResults.get(0)[testDateCoulmnIndex];
		testDate = getTestDateCorrectForm(testDate);
		
		int numOfSections = validateSectionsTitles(userId, testId, userName, testDate, userTestResults, columnIndex);
		NewUxClassicTestPage classicTest = new NewUxClassicTestPage(webDriver,testResultService);
		
		// get answers from file
		TestEnvironmentPage testEnvironmentPage = new TestEnvironmentPage(webDriver, testResultService);
		String answerFilePath = "files/CourseTestData/MidTerm_Answers.csv";
		CourseTest courseTest = testEnvironmentPage.initCourseTestFromCSVFileNew(answerFilePath, testId, CourseCodes.B1, CourseTests.MidTerm, numOfSections);
		CourseTestSection round = new CourseTestSection();
		
		// get sections scores
		//List<String[]> userTestResults = dbService.getUserTestResultsByUserIdAndTestIdOldTe(userId, testId);
		String[] sectionScores = new String[userTestResults.size()];
		for (int i = 0; i < userTestResults.size(); i++) {
			sectionScores[i] = userTestResults.get(i)[5];
		}
		
		for (int i = 1; i <= numOfSections; i++) {
			report.startStep("Check Test Results View in Section " + i);
			switchToTestResultsFrameBySection(i);
			Thread.sleep(1000);
			
			report.startStep("Validate Section Score is Correct");
			String sectionScoreWeb = getSectionScoreFromWeb();
			testResultService.assertEquals("Your total score is: " + sectionScores[i-1], sectionScoreWeb, "Section Score is Incorrect. Excpected: " + sectionScores[i-1] +". Actual: " + sectionScoreWeb);
			
			int taskCount = classicTest.getSectionTaskCountClassic();
			
			round = courseTest.getSections().get(i-1);
			boolean isCorrect = false;
			
			for (int j = 0; j < taskCount; j++) {
				
				TestQuestion question = round.getSectionQuestions().get(j);
				
				TestQuestionType questionType = question.getQuestionType();
							
				String[] answers = new String[question.getCorrectAnswers().length];
				
				for (int k = 0; k < question.getCorrectAnswers().length; k++) {
					answers[k] = question.getCorrectAnswers()[k].replace("~", ",");
				}
				question.setCorrectAnswers(answers);
				
				String wrongAnswer = getWrongAnswerFromFile(i, String.join(",", answers), wrongAnswerFile, testId); 
				if (wrongAnswer.equals("~")) {
					isCorrect = true;
					wrongAnswer = "";
				}
				
				checkAnswerOfTask(j, isCorrect, answers, questionType,wrongAnswer);
			}
			
			if (i < numOfSections) {
				webDriver.switchToTopMostFrame();
				clickOnOpenSectionIconBySection(i+1);
			}
		}
	}
	
	public void validateResultsNewTe(String userId, String testId, String userName, String wrongAnswerFile) throws Exception{
		boolean isNewTe = true;
		
		//webDriver.waitUntilElementAppears("//div[@id='mainAreaTD']", 30); 
		Thread.sleep(2000);
		
		int scoreColumnIndex = 0;
		int sectionTypeColumnIndex = 0;
		List<String[]> userTestResults = new ArrayList<String[]>();
		if (isNewTe) {
			String userTestAdministrationId = dbService.getUserTestProgressByUserId(userId).get(0)[0];
			userTestResults = dbService.getUserTestResultsByUserIdAndTestIdNewTe(userId, userTestAdministrationId);
			sectionTypeColumnIndex = 8;
			scoreColumnIndex = 6;
		} else {
			userTestResults = dbService.getUserTestResultsByUserIdAndTestIdOldTe(userId, testId);
			sectionTypeColumnIndex = 12;
			scoreColumnIndex = 5;
		}
		String testDate = userTestResults.get(0)[9];
		testDate = getTestDateCorrectForm(testDate);
		
		int numOfSections = validateSectionsTitles(userId, testId, userName, testDate, userTestResults, sectionTypeColumnIndex);
		NewUxClassicTestPage classicTest = new NewUxClassicTestPage(webDriver,testResultService);
		
		report.startStep("Get Answers from File");
		TestEnvironmentPage testEnvironmentPage = new TestEnvironmentPage(webDriver, testResultService);
		String answerFilePath = "files/CourseTestData/MidTerm_Answers.csv";
		CourseTest courseTest = testEnvironmentPage.initCourseTestFromCSVFileNew(answerFilePath, testId, CourseCodes.B1, CourseTests.MidTerm, numOfSections);
		CourseTestSection round = new CourseTestSection();
		
		report.startStep("Get scores from DB");
		String[] sectionScores = new String[userTestResults.size()];
		for (int i = 0; i < userTestResults.size(); i++) {
			sectionScores[i] = userTestResults.get(i)[scoreColumnIndex];
		}
		
		for (int i = 1; i <= numOfSections; i++) {
			report.startStep("Check Test Results View in Section " + i);
			switchToTestResultsFrameBySection(i);
			Thread.sleep(1000);

			report.startStep("Validate Section Score is Correct");
			String sectionScoreWeb = getSectionScoreFromWeb();
			testResultService.assertEquals("Your total score is: " + sectionScores[i-1], sectionScoreWeb, "Section Score is Incorrect. Excpected: " + sectionScores[i-1] +". Actual: " + sectionScoreWeb);
			
			int taskCount = classicTest.getSectionTaskCountClassic();
			
			round = courseTest.getSections().get(i-1);
			boolean isCorrect = false;
			
			for (int j = 0; j < taskCount; j++) {
				
				TestQuestion question = round.getSectionQuestions().get(j);
				
				TestQuestionType questionType = question.getQuestionType();
							
				String[] answers = new String[question.getCorrectAnswers().length];
				
				for (int k = 0; k < question.getCorrectAnswers().length; k++) {
					answers[k] = question.getCorrectAnswers()[k].replace("~", ",");
				}
				question.setCorrectAnswers(answers);
				
				String wrongAnswer = getWrongAnswerFromFile(i, String.join(",", answers), wrongAnswerFile, testId); 
				if (wrongAnswer.equals("~")) {
					isCorrect = true;
					wrongAnswer = "";
				}
				
				checkAnswerOfTask(j, isCorrect, answers, questionType,wrongAnswer);
			}
			
			if (i < numOfSections) {
				webDriver.switchToTopMostFrame();
				clickOnOpenSectionIconBySection(i+1);
			}
		}
	}
		
	public String getTestDateCorrectForm(String originalTestDate) throws ParseException{
		String[] splittedDateTime = originalTestDate.split(" ");
		TextService textService = new TextService();
		String correctFormDate = textService.convertDateToDifferentFormat(splittedDateTime[0], "yyyy-MM-dd", "dd/MM/yyyy"); 
		return correctFormDate;
	}
	
	public String getSectionScoreFromWeb() throws InterruptedException{
		WebElement currentSectionScore = webDriver.getWebDriver().findElement(By.xpath("//*[@id='btnTools']/div"));
		String currentSectionScoreString = currentSectionScore.getText();
		Thread.sleep(1000);
		String[] splittedScore = currentSectionScoreString.split("\n");
		currentSectionScoreString = splittedScore[splittedScore.length-1];
		return currentSectionScoreString;
	}
	
	public void validateResultsNewTeNew(String userId, String testId, String userName, String wrongAnswerFile) throws Exception{
		boolean isNewTe = true;
		
		//webDriver.waitUntilElementAppears("//div[@id='mainAreaTD']", 30); 
		Thread.sleep(2000);
		
		int scoreColumnIndex = 0;
		int sectionTypeColumnIndex = 0;
		List<String[]> userTestResults = new ArrayList<String[]>();
		if (isNewTe) {
			String userTestAdministrationId = dbService.getUserTestProgressByUserId(userId).get(0)[0];
			userTestResults = dbService.getUserTestResultsByUserIdAndTestIdNewTe(userId, userTestAdministrationId);
			sectionTypeColumnIndex = 8;
			scoreColumnIndex = 6;
		} else {
			userTestResults = dbService.getUserTestResultsByUserIdAndTestIdOldTe(userId, testId);
			sectionTypeColumnIndex = 12;
			scoreColumnIndex = 5;
		}
		String testDate = userTestResults.get(0)[9];
		testDate = getTestDateCorrectForm(testDate);
		
		int numOfSections = validateSectionsTitles(userId, testId, userName, testDate, userTestResults, sectionTypeColumnIndex);
		//NewUxClassicTestPage classicTest = new NewUxClassicTestPage(webDriver,testResultService);
		
		report.startStep("Get Answers from File");
		TestEnvironmentPage testEnvironmentPage = new TestEnvironmentPage(webDriver, testResultService);
		String answerFilePath = "files/CourseTestData/MidTerm_Answers.csv";
		CourseTest courseTest = testEnvironmentPage.initCourseTestFromCSVFileNew(answerFilePath, testId, CourseCodes.B1, CourseTests.MidTerm, numOfSections);
		CourseTestSection round = new CourseTestSection();
		
		report.startStep("Get scores from DB");
		String[] sectionScores = new String[userTestResults.size()];
		for (int i = 0; i < userTestResults.size(); i++) {
			sectionScores[i] = userTestResults.get(i)[scoreColumnIndex];
		}
		
		for (int i = 1; i <= numOfSections; i++) {
			report.startStep("Check Test Results View in Section " + i);
			switchToTestResultsFrameBySection(i);
			Thread.sleep(1000);

			report.startStep("Validate Section Score is Correct");
			String sectionScoreWeb = getSectionScoreFromWebNew();
			testResultService.assertEquals(sectionScores[i-1]+"%", sectionScoreWeb, "Section Score is Incorrect. Excpected: " + sectionScores[i-1] +". Actual: " + sectionScoreWeb);
			
			report.startStep("Click Review");
			clickReviewTasksInReport();
			
			report.startStep("Click On Task Bar");
			testEnvironmentPage.clickTasksNav();
			Thread.sleep(1500);
	
			report.startStep("Retrieve Task Count");
			NewUxLearningArea2 learningArea2 = new NewUxLearningArea2(webDriver, testResultService);
			int taskCount = learningArea2.getTasksCount();
			
			report.startStep("Close Task Bar");
			Thread.sleep(2000);
			testEnvironmentPage.closeTasksNav();
			Thread.sleep(1500);
			
			round = courseTest.getSections().get(i-1);
			boolean isCorrect = false;
			
			for (int j = 0; j < taskCount; j++) {
				
				TestQuestion question = round.getSectionQuestions().get(j);
				
				TestQuestionType questionType = question.getQuestionType();
							
				String[] answers = new String[question.getCorrectAnswers().length];
				
				for (int k = 0; k < question.getCorrectAnswers().length; k++) {
					answers[k] = question.getCorrectAnswers()[k].replace("~", ",");
				}
				question.setCorrectAnswers(answers);
				
				String wrongAnswer = getWrongAnswerFromFile(i, String.join(",", answers), wrongAnswerFile, testId); 
				if (wrongAnswer.equals("~")) {
					isCorrect = true;
					wrongAnswer = "";
				}
				
				checkAnswerOfTaskNewTE(j, isCorrect, answers, questionType,wrongAnswer);
			}
			
			if (i < numOfSections) {
				webDriver.switchToTopMostFrame();
				Thread.sleep(1000);
				clickOnOpenSectionIconBySection(i+1);
				Thread.sleep(2000);
			}
		}
	}
	
	public String getSectionScoreFromWebNew() {
		WebElement currentSectionScore = webDriver.getWebDriver().findElement(By.xpath("//div[@class='learning__testScoreResult']"));
		String currentSectionScoreString = currentSectionScore.getText();
		return currentSectionScoreString;
	}
	
	public void clickReviewTasksInReport() {
		WebElement review = webDriver.getWebDriver().findElement(By.xpath("//a[@class='learning__pnItemLink learning__testReviewLink']"));
		webDriver.ClickElement(review);
	}
	
	public void checkAnswerOfTaskNewTE(int taskIndex, boolean isCorrect, String[] correctAnswer, TestQuestionType questionType, String wrongAnswer) throws Exception {
		
		String finalWrongAnswer= null;
		
		switch (questionType) {
			case RadioSingle:
				validateAnswerRadioSingleNewTE(isCorrect, taskIndex, correctAnswer[0], wrongAnswer);
				break;
			case DragAndDropMultiple:
				validateAnswerDragAndDropMultipleNewTE(isCorrect, taskIndex, correctAnswer, wrongAnswer);
				break;
			case comboBox:
				finalWrongAnswer = getWrongPartsOfWrongAnswer(String.join(",", correctAnswer), wrongAnswer);
				validateAnswerComboBoxNewTE(isCorrect, taskIndex, correctAnswer, finalWrongAnswer);
				break;
			case RadioMultiple:
				break;
			case DragAndDropSingle:
				break;
			case TrueFalse:
				break;
			case DrapAndDropToPicture:
				break;
			default:
				break;
		}	
	}
	
	public void validateAnswerRadioSingleNewTE(boolean isCorrect, int taskIndex, String correctAnswer, String wrongAnswer) throws Exception{
		
		//TestEnvironmentPage testEnvironmentPage = new TestEnvironmentPage(webDriver, testResultService);
		
		report.startStep("Open Task Bar");
		testEnvironmentPage.clickTasksNav();
		
		report.startStep("Get Task Sign From Web");
		String taskSign = questionSignsInNewReport.get(taskIndex).getAttribute("className");
		
		if (isCorrect) {
			
			report.startStep("Validate V Sign is Displayed next to Current Task on Nav Bar");
			testResultService.assertEquals(true, taskSign.contains("--vCheck"), "Task Sign in Nav Bar is Incorrect");
			
			report.startStep("Close Task Bar (By Clicking on Wanted Task)");
			clickOnSpecificTaskInReport(taskIndex);
			
			report.startStep("Validate V Sign is Displayed next to Correct Answer");
			validateVSignDisplayedNextToCorrectAnswerNewTE(correctAnswer);
			
			report.startStep("Open Correct Answer Tab and Validate V Sign is Displayed next to Correct Answer");
			clickOnCorrectAnswerTab();
			validateCorrectAnswerInCorrectAnswerTab(correctAnswer);
			
		} else {
			
			wrongAnswer = wrongAnswer.split("~")[0];
			
			report.startStep("Validate X Sign is Displayed next to Current Task on Nav Bar");
			testResultService.assertEquals(true, taskSign.contains("--xCheck"), "Task Sign in Nav Bar is Incorrect");
			
			report.startStep("Close Task Bar (By Clicking on Wanted Task)");
			clickOnSpecificTaskInReport(taskIndex);
			
			report.startStep("Validate V Sign is Not Displayed next to Correct Answer");
			validateVSignIsNotDisplayedNextToCorrectAnswerNewTE(correctAnswer);
			
			if (!wrongAnswer.equals("")) {
				validateXSignDisplayedNextToWrongAnswerNewTE(wrongAnswer);
			}
			
			report.startStep("Open Correct Answer Tab and Validate V Sign is Displayed next to Correct Answer");
			clickOnCorrectAnswerTab();
			validateCorrectAnswerInCorrectAnswerTab(correctAnswer);
						
		}	
	}
	
	public void validateVSignDisplayedNextToCorrectAnswerNewTE(String correctAnswer) throws Exception{
		for (int i = 0; i < questionsAnswersNewTE.size(); i++) {
			if (questionsAnswersNewTE.get(i).getText().contains(correctAnswer)) {
				testResultService.assertEquals(true, questionsAnswersNewTE.get(i).getAttribute("className").contains("--checked"),"Answer is Not Checked");
				testResultService.assertEquals(true, questionsAnswersSignsNewTE.get(i).getAttribute("className").contains("--v"),"V Sign is not displyed next to correct answer");
				break;
			}
		} 
	}
	
	public void validateVSignIsNotDisplayedNextToCorrectAnswerNewTE(String correctAnswer) throws Exception{
		for (int i = 0; i < questionsAnswersNewTE.size(); i++) {
			if (questionsAnswersNewTE.get(i).getText().contains(correctAnswer)) {
				testResultService.assertEquals(false, questionsAnswersNewTE.get(i).getAttribute("className").contains("--checked"),"V Sign is displyed next to correct answer (even though it should be incorrect)");
				break;
			}
		}
	}
	
	public void validateXSignDisplayedNextToWrongAnswerNewTE(String wrongAnswer) throws Exception{
		for (int i = 0; i < questionsAnswersNewTE.size(); i++) {
			if (questionsAnswersNewTE.get(i).getText().contains(wrongAnswer)) {
				testResultService.assertEquals(true, questionsAnswersNewTE.get(i).getAttribute("className").contains("--checked"),"Wrong Answer is not Marked (Even though it was chosen by student)");
				testResultService.assertEquals(true, questionsAnswersSignsNewTE.get(i).getAttribute("className").contains("--x"),"X Sign is not displyed next to Incorrect Answer");

				break;
			}
		}
	}
	
	// This function clicks on wanted task, and if the wanted task is the current task, close nav bar button will be clicked
	public void clickOnSpecificTaskInReport(int taskIndex) throws Exception{
		
		TestEnvironmentPage testEnvironmentPage = new TestEnvironmentPage(webDriver, testResultService);
		//Thread.sleep(2000);
		
		boolean isCurrent = false;
		String taskAttr = tasksInNavBar.get(taskIndex).getAttribute("className");
		if (taskAttr.contains("--current")) {
			isCurrent = true;
		}
		
		if (isCurrent) {
			Thread.sleep(2000);
			testEnvironmentPage.closeTasksNav(); 
		} else {
			webDriver.ClickElement(tasksInNavBar.get(taskIndex));
		}
	}
	
	public void validateCorrectAnswerInCorrectAnswerTab(String correctAnswer) throws Exception {
		for (int i = 0; i < questionsAnswersInCorrectTabNewTE.size(); i++) {
			if (questionsAnswersInCorrectTabNewTE.get(i).getText().contains(correctAnswer)) {
				testResultService.assertEquals(true, questionsAnswersInCorrectTabNewTE.get(i).getAttribute("className").contains("correct"),"Correct Answer in 'Correct Answer' Tab is Not Marked");
				break;
			}
		} 
	}
	
	public void validateAnswerDragAndDropMultipleNewTE(boolean isCorrect, int taskIndex, String[] correctAnswer, String wrongAnswer) throws Exception{
		
		report.startStep("Open Task Bar");
		testEnvironmentPage.clickTasksNav();
		
		report.startStep("Get Task Sign From Web");
		String taskSign = questionSignsInNewReport.get(taskIndex).getAttribute("className");
		
		if (isCorrect) {
			
			report.startStep("Validate V Sign is Displayed next to Current Task on Nav Bar");
			testResultService.assertEquals(true, taskSign.contains("--vCheck"), "Task Sign in Nav Bar is Incorrect");
			
			report.startStep("Close Task Bar (By Clicking on Wanted Task)");
			clickOnSpecificTaskInReport(taskIndex);
			
			report.startStep("Validate V Sign is Displayed next to Correct Answer");
			validateDragAndDropMultipleAnswersNewTE(correctAnswer);//
			
			report.startStep("Open Correct Answer Tab and Validate V Sign is Displayed next to Correct Answer");
			clickOnCorrectAnswerTab();
			validateDragAndDropMultipleAnswersNewTE(correctAnswer);//
		
		} else {
			String finalWrongAnswers = "";
			
			if (wrongAnswer.equals("~DidNotAnswer")) { // if no answer was dragged
				
				report.startStep("Validate X Sign is Displayed next to Current Task on Nav Bar");
				testResultService.assertEquals(true, taskSign.contains("--xCheck"), "Task Sign in Nav Bar is Incorrect");
				
				report.startStep("Close Task Bar (By Clicking on Wanted Task)");
				clickOnSpecificTaskInReport(taskIndex);
				
				report.startStep("Validate all Answers are Empty");
				validateAllDragAndDropMultipleAnswersAreEmptyNewTE(); //
				
			} else if (wrongAnswer.split("~")[1].equals("swappedAnswers")){ // if answers were swapped
				
				report.startStep("Validate VX Sign is Displayed next to Current Task on Nav Bar");
				testResultService.assertEquals(true, taskSign.contains("--vxCheck"), "Task Sign in Nav Bar is Incorrect");
				
				report.startStep("Close Task Bar (By Clicking on Wanted Task)");
				clickOnSpecificTaskInReport(taskIndex);
				
				report.startStep("Get Wrong Answers and Validate all Wrong Answers have X Sign");
				finalWrongAnswers = getWrongPartsOfWrongAnswer(String.join(",", correctAnswer), wrongAnswer);
				validateWrongDragAndDropHasXSignNewTE(finalWrongAnswers);
				
			} else if (wrongAnswer.split("~")[1].equals("partiallyAnswered")) { // if question was partially answered
				
				report.startStep("Validate VX Sign is Displayed next to Current Task on Nav Bar");
				testResultService.assertEquals(true, taskSign.contains("--vxCheck"), "Task Sign in Nav Bar is Incorrect");
				
				report.startStep("Close Task Bar (By Clicking on Wanted Task)");
				clickOnSpecificTaskInReport(taskIndex);
				
				report.startStep("Get Answers that Weren't Dragged and Validate They are Found in Words Bank");
				finalWrongAnswers = getWrongPartsOfWrongAnswer(String.join(",", correctAnswer), wrongAnswer);
				validateWordIsFoundInWordBankNewTE(finalWrongAnswers); 
			} /*else if (wrongAnswer.split("~")[1].equals("swappedAndPartially")){
				report.startStep("Validate VX Sign is Displayed next to Current Task on Left Bar");
				testResultService.assertEquals("halfAnswer", taskSign, "Task Sign in Left Bar is Incorrect");
				
				finalWrongAnswers = getWrongPartsOfWrongAnswer(String.join(",", correctAnswer), wrongAnswer);

				validateWrongDragAndDropHasXSign(finalWrongAnswers);
			}*/
			
			//report.startStep("Open Correct Answer Tab and Validate V Sign is Displayed next to Correct Answer");
			//clickOnCorrectAnswerTab();
			//validateDragAndDropMultipleAnswers(correctAnswer); // currently not located
						
		}
	}
	
	public void validateDragAndDropMultipleAnswersNewTE(String[] correctAnswer){
		for (int i = 0; i < dragAndDropMultipleAnswers.size(); i++) {
			testResultService.assertEquals(correctAnswer[i], dragAndDropAnswersNewTE.get(i).getText(), "Question number " + (i+1) +" is Incorrect");
		}
	}
	
	public void validateAllDragAndDropMultipleAnswersAreEmptyNewTE(){
		for (int i = 0; i < dragAndDropAnswersNewTE.size(); i++) {
			testResultService.assertEquals("", dragAndDropAnswersNewTE.get(i).getText().trim(), "Question number " + (i+1) +" is not Empty");
		}
	}
	
	public void validateWrongDragAndDropHasXSignNewTE(String wrongAnswer) throws Exception {
		String[] wrongAnswersSeperated = wrongAnswer.split(",");
				
		boolean hasScrolled = false;
		//WebElement scroller = null;
		for (int i = 0; i < dragAndDropAnswersNewTE.size(); i++) {
			for (int j = 0; j < wrongAnswersSeperated.length; j++) {
				if (dragAndDropAnswersNewTE.get(i).getText().equals("") && !hasScrolled){
					// scroll down
					//scroller = webDriver.getWebDriver().findElement(By.xpath("//div[@id='mCSB_1_dragger_vertical']"));
					//webDriver.dragScrollElement(scroller, 110);
					scrollToButtonOfAnswerReport();
					hasScrolled = true;
				}
				if (dragAndDropAnswersNewTE.get(i).getText().equals(wrongAnswersSeperated[j])){
					testResultService.assertEquals(true, dragAndDropAnswersNewTE.get(i).getAttribute("className").contains("--x"), "Wrong Answer: "+wrongAnswersSeperated[j]+",does not have x sign");
				}
			}
		}
		
		//webDriver.dragScrollElement(scroller, -110);
		scrollToTopOfAnswerReport();
	}
	
	// need to change func
	public void validateWordIsFoundInWordBankNewTE(String wrongAnswer) throws Exception{
		
		String[] seperatedWrongAnswers = wrongAnswer.split(",");
		for (int j = 0; j < seperatedWrongAnswers.length; j++) {
			boolean isFound = false;
			for (int i = 0; i < wordsBankDragAndDropMultipleAnswers.size(); i++) {
				if (wordsBankDragAndDropMultipleAnswers.get(i).getText().equals(wrongAnswer)) {
					isFound = true;
					break;
				}
			}
			testResultService.assertEquals(true, isFound, "Answer that wasn't answered is not found in words bank");
		}
	}
	
	public void validateAnswerComboBoxNewTE(boolean isCorrect, int taskIndex, String[] correctAnswer, String wrongAnswer) throws Exception{
		
		report.startStep("Open Task Bar");
		testEnvironmentPage.clickTasksNav();
		
		report.startStep("Get Task Sign From Web");
		String taskSign = questionSignsInNewReport.get(taskIndex).getAttribute("className");
		
		if (isCorrect) { // debug
			
			report.startStep("Validate V Sign is Displayed next to Current Task on Nav Bar");
			testResultService.assertEquals(true, taskSign.contains("--vCheck"), "Task Sign in Nav Bar is Incorrect");
			
			report.startStep("Close Task Bar (By Clicking on Wanted Task)");
			clickOnSpecificTaskInReport(taskIndex);
			
			report.startStep("Validate V Sign is Displayed next to Correct Answer");
			validateComboBoxAnswersNewTE(correctAnswer);
			
			report.startStep("Open Correct Answer Tab and Validate V Sign is Displayed next to Correct Answer");
			clickOnCorrectAnswerTab();
			validateComboBoxAnswersNewTE(correctAnswer);
			
		} else {
			
			//report.startStep("Validate X Sign is Displayed next to Current Task on Left Bar");
			//testResultService.assertEquals("wrongAnswer", taskSign, "Task Sign in Left Bar is Incorrect");
			
			if (wrongAnswer.equals("")) {
				
				report.startStep("Validate X Sign is Displayed next to Current Task on Nav Bar");
				testResultService.assertEquals(true, taskSign.contains("--xCheck"), "Task Sign in Nav Bar is Incorrect");
				
				report.startStep("Close Task Bar (By Clicking on Wanted Task)");
				clickOnSpecificTaskInReport(taskIndex);
				
				validateAllComboBoxAnswersAreEmptyNewTE(); 
			} else {
				
				report.startStep("Validate VX Sign is Displayed next to Current Task on Nav Bar");
				testResultService.assertEquals(true, taskSign.contains("--vxCheck"), "Task Sign in Nav Bar is Incorrect");
				Thread.sleep(500);
				
				report.startStep("Close Task Bar (By Clicking on Wanted Task)");
				clickOnSpecificTaskInReport(taskIndex);
			
				// when a wrong answer will be selected a red sign will be displayed (?)
				// so here i validate that the answer that is wrong has a red sign (i have the wrong answer as string)
				validateWrongComboBoxHasXSignNewTE(wrongAnswer);
			}
			
			report.startStep("Open Correct Answer Tab and Validate V Sign is Displayed next to Correct Answer");
			clickOnCorrectAnswerTab();
			validateComboBoxAnswersNewTE(correctAnswer);
						
		}
	}
	
	public void validateComboBoxAnswersNewTE(String[] correctAnswer) throws InterruptedException{
		for (int i = 0; i < comboBoxAnswersNewTE.size(); i++) {
			if (comboBoxAnswersNewTE.get(i).getText().equals("")) {
				//WebElement scroller = webDriver.getWebDriver().findElement(By.xpath("//div[@id='mCSB_1_dragger_vertical']"));
				//webDriver.dragScrollElement(scroller, 80);
				scrollToButtonOfAnswerReport();
			}
			testResultService.assertEquals(correctAnswer[i], comboBoxAnswersNewTE.get(i).getText(), "Question number " + (i+1) +" is Incorrect");
		}
		scrollToTopOfAnswerReport();
	}
	
	public void validateAllComboBoxAnswersAreEmptyNewTE(){
		for (int i = 0; i < comboBoxAnswersNewTE.size(); i++) {
			testResultService.assertEquals("", comboBoxAnswersNewTE.get(i).getText().trim(), "Question number " + (i+1) +" is not Empty");
		}
	}
	
	public void validateWrongComboBoxHasXSignNewTE(String wrongAnswers) throws Exception{
		String[] wrongAnswersSeperated = wrongAnswers.split(",");
		boolean hasScrolled = false;
		for (int i = 0; i < comboBoxAnswersNewTE.size(); i++) {
			for (int j = 0; j < wrongAnswersSeperated.length; j++) {
				if (comboBoxAnswersNewTE.get(i).getText().equals("") && !hasScrolled) {
					scrollToButtonOfAnswerReport();
					hasScrolled = true;
				}
				if (comboBoxAnswersNewTE.get(i).getText().equals(wrongAnswersSeperated[j])){
					testResultService.assertEquals(true, comboBoxAnswersNewTE.get(i).getAttribute("className").contains("--incorrect"), "Wrong Answer: "+wrongAnswersSeperated[j]+", does not have x sign");
				}
			}
		}
		scrollToTopOfAnswerReport();
	}
	
	public void scrollToButtonOfAnswerReport() throws InterruptedException{
		WebElement element = webDriver.getElement(By.xpath("//div[@class='mCSB_dragger_bar']"));
		WebElement scrollContainer = webDriver.getElement(By.xpath("//div[@class='mCSB_draggerContainer']"));
		WebElement lessonListScrollContainer = webDriver.getElement(By.xpath("//div[contains(@class,'mCustomScrollbar')]"));
		if (!lessonListScrollContainer.getAttribute("class").contains("mCS_no_scrollbar"))
		{
			int scrollContainerHeight = scrollContainer.getSize().height;
			int height = element.getSize().height;
			webDriver.dragScrollElement(element, scrollContainerHeight - height);
		}
		Thread.sleep(2000);
	}
	
	public void scrollToTopOfAnswerReport() throws InterruptedException{
		Thread.sleep(1000);
		WebElement element = webDriver.getElement(By.xpath("//div[@class='mCSB_dragger_bar']"));
		WebElement scrollContainer = webDriver.getElement(By.xpath("//div[@class='mCSB_draggerContainer']"));
		WebElement lessonListScrollContainer = webDriver.getElement(By.xpath("//div[contains(@class,'mCustomScrollbar')]"));
		if (!lessonListScrollContainer.getAttribute("class").contains("mCS_no_scrollbar"))
		{
			int scrollContainerHeight = scrollContainer.getSize().height;
			int height = element.getSize().height;
			webDriver.dragScrollElement(element, -(scrollContainerHeight - height));
		}
		Thread.sleep(1000);
	}
	
	public List<String> getStudentsNamesFromCourseTestReportTable() throws Exception {
		webDriver.switchToTopMostFrame();
		TmsHomePage tmsHomePage = new TmsHomePage(webDriver, testResultService);
		tmsHomePage.switchToMainFrame();
		webDriver.switchToFrame("CourseTestsReport");
		
		List<String> studentsNamesTable = new ArrayList();
		List<WebElement> studentsNames = webDriver.getWebDriver().findElements(By.xpath("//td[@class='tdt'][1]"));// //td[@class='tdt']//div
		TmsReportsPage tmsReportsPage = new TmsReportsPage(webDriver, testResultService);
		int numOfPages = tmsReportsPage.getNumberOfPagesInReportsTable();
		for (int j = 0; j < numOfPages; j++) {
			for (int i = 0; i < studentsNames.size(); i++) {
				//studentsNamesTable.add(studentsNames.get(i).getText());
				studentsNamesTable.add(studentsNames.get(i).getAttribute("title").split("Username")[0].replace("Name: ",""));
			}
			webDriver.waitForElement("//td[@id='next_divGridPager']", ByTypes.xpath,true, 3).click();
			studentsNames = webDriver.getWebDriver().findElements(By.xpath("//td[@class='tdt'][1]")); // //td[@class='tdt']//div
		}
		
		webDriver.waitForElement("//td[@id='first_divGridPager']", ByTypes.xpath, true, webDriver.getTimeout()).click();
		return studentsNamesTable;
	}
	
	public List<String> getStudentsFirstNamesFromStudentsNamesList(List<String> studentsNames) {
		List<String> studentsFirstNames = new ArrayList();
		for (int i = 0; i < studentsNames.size(); i++) {
			studentsFirstNames.add(studentsNames.get(i).split(" ")[1]);
		}
		return studentsFirstNames;
	}
	
	public List<String> getStudentsLastNamesFromStudentsNamesList(List<String> studentsNames) {
		List<String> studentsLastNames = new ArrayList();
		for (int i = 0; i < studentsNames.size(); i++) {
			studentsLastNames.add(studentsNames.get(i).split(" ")[0]);
		}
		return studentsLastNames;
	}
	
	public List<String> getMidTermFinalGradesFromCourseTestReportTable() throws Exception {
		List<String> midTermFinalGradesTable = new ArrayList();
		List<WebElement> midTermFinalGrades = webDriver.getWebDriver().findElements(By.xpath("//td[@class='tdt'][2]/span"));
		TmsReportsPage tmsReportsPage = new TmsReportsPage(webDriver, testResultService);
		int numOfPages = tmsReportsPage.getNumberOfPagesInReportsTable();
		for (int j = 0; j < numOfPages; j++) {
			for (int i = 0; i < midTermFinalGrades.size(); i++) {
				midTermFinalGradesTable.add(midTermFinalGrades.get(i).getText().trim());	
			}
			webDriver.waitForElement("//td[@id='next_divGridPager']", ByTypes.xpath,true, 3).click();
			midTermFinalGrades = webDriver.getWebDriver().findElements(By.xpath("//td[@class='tdt'][2]/span"));
		}
		webDriver.waitForElement("//td[@id='first_divGridPager']", ByTypes.xpath, true, webDriver.getTimeout()).click();
		return midTermFinalGradesTable;
	}
	
	public List<String> getCourseTestFinalGradesFromCourseTestReportTable() throws Exception {
		List<String> courseTestFinalGradesTable = new ArrayList();
		List<WebElement> courseTestFinalGrades = webDriver.getWebDriver().findElements(By.xpath("//td[@class='tdt'][3]//span"));
		TmsReportsPage tmsReportsPage = new TmsReportsPage(webDriver, testResultService);
		int numOfPages = tmsReportsPage.getNumberOfPagesInReportsTable();
		for (int j = 0; j < numOfPages; j++) {
			for (int i = 0; i < courseTestFinalGrades.size(); i++) {
				courseTestFinalGradesTable.add(courseTestFinalGrades.get(i).getText().trim());	
			}
			webDriver.waitForElement("//td[@id='next_divGridPager']", ByTypes.xpath,true, 3).click();
			courseTestFinalGrades = webDriver.getWebDriver().findElements(By.xpath("//td[@class='tdt'][3]//span"));
		}
		webDriver.waitForElement("//td[@id='first_divGridPager']", ByTypes.xpath, true, webDriver.getTimeout()).click();
		return courseTestFinalGradesTable;
	}
	
}
