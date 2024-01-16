package pageObjects.edo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.plaf.basic.BasicBorders.RadioButtonBorder;

import org.apache.tools.ant.taskdefs.Sleep;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.security.core.userdetails.UserDetails;

import Enums.ByTypes;
import Enums.CourseCodes;
import Enums.CourseTests;
import Enums.PLTStartLevel;
import Enums.TestQuestionType;
import Enums.expectedConditions;
import Objects.CourseTest;
import Objects.CourseTestSection;
import Objects.PLTCycle;
import Objects.PLTTest;
import Objects.TestQuestion;
import drivers.GenericWebDriver;
import pageObjects.EdoLoginPage;
import pageObjects.GenericPage;
import services.Reporter;
import services.TestResultService;
import services.TextService;
import tests.edo.newux.AssessmentsTests;


public class NewUxClassicTestPage extends GenericPage {

	Reporter report = new Reporter(); 
	TextService textService = new TextService();
	
	private static final String TITLE_PLT = "Placement Test";
	
	
	public NewUxClassicTestPage(GenericWebDriver webDriver,
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
		
	public void switchToTestAreaIframe() throws Exception{
		Thread.sleep(2000);
		webDriver.switchToTopMostFrame();
		
		webDriver.waitUntilElementAppears("oed__iframe", ByTypes.className, 60);
		webDriver.switchToFrame(webDriver.waitForElement("oed__iframe", ByTypes.className,false,3));
		
	}
	
	public void switchToCompletionMessageFrame() throws Exception{
		webDriver.switchToFrame(webDriver.waitForElement("cboxIframe", ByTypes.className));
		
	}
	
	public void closeCompletionMessageAlert(){
		//webDriver.waitForElement("cboxClose", ByTypes.id).click();
		try {
			switchToTestAreaIframe();
			switchToCompletionMessageFrame();
			WebElement element = webDriver.waitForElement("//input[@name='btnOk']", ByTypes.xpath).findElement(By.xpath("//input[@name='btnOk']"));
			webDriver.clickOnElementByJavaScript(element);
			
			webDriver.switchToMainWindow();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getFinalScore() throws Exception{
		String score = webDriver.waitForElement("//div[@class='hebDir']/b", ByTypes.xpath, "Getting score").getText();
		return score;
	}
	
	public void verifyTitlePLT() throws Exception{
		WebElement element=null;
		
		for (int i=0; i<10 && element == null;i++){
			element = webDriver.waitForElement("placmentBanner", ByTypes.className,1,false, "PLT doesn't open: " + (i+1) + " time");	//placmentBanner //div[@class='testEnv__testPath']
		}
		
		String testName=element.getText();
		testResultService.assertEquals(TITLE_PLT, testName.trim(),"PLT test name not found or not correct");
		
	}
	
	public void selectLevelOnStartPLT(PLTStartLevel level) throws Exception{
		
			switch (level) {
			case Basic:
				webDriver.waitForElement("rbBasic", ByTypes.id,
						"Basic level button").click();
				break;
			case Advanced:
				webDriver.waitForElement("rbAdvanced", ByTypes.id,
						"Intermediate level button").click();
				break;
			case Intermediate:
				webDriver.waitForElement("rbIntermediate", ByTypes.id,
						"Advance level button").click();
				break;
			case IamNotSure:
				webDriver.waitForElement("rbImNotSure", ByTypes.id,
						"Im not sure button").click();
				break;

			}
		
	
	}
	
	public void pressOnStartPLT() {
		try {
			webDriver.waitUntilElementAppears("Submit", ByTypes.name,10);
			webDriver.waitForElement("Submit", ByTypes.name).click();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
	}
	
	public void pressOnStartTest() throws Exception{
		webDriver.waitUntilElementAppears("btnStartTest", ByTypes.id, 60);
		webDriver.waitForElement("btnStartTest", ByTypes.id).click();
	}
	
	
	public void exitDuringPLT(boolean isOpenedFromAssessmentsPage) throws Exception{
		webDriver.waitForElement("//div[@id='exitCell']//td[3]", ByTypes.xpath).click();
		webDriver.switchToPopup();
		webDriver.waitForElement("submitBtn", ByTypes.className).click();
		webDriver.switchToMainWindow();
		webDriver.switchToNewWindow();

		if (isOpenedFromAssessmentsPage)
			switchToTestAreaIframe();

		webDriver.waitForElement("placmentExit", ByTypes.className).click();
		webDriver.switchToMainWindow();
		
	
	}
	
	public void pressOnNextTaskArrow() throws Exception{
		webDriver.waitForElement("tasksBtnext", ByTypes.className).click();
		
	}
	
	public void pressOnPrevTaskArrow() throws Exception{
		webDriver.waitForElement("tasksBtprev", ByTypes.className).click();
		
	}
	
	public void browseToLastSectionTask() {
		int taskCount;
		try {
			taskCount = getSectionTaskCountClassic();
	
			report.addTitle("Actual item in this section: " + taskCount);
			//String activeTask1 = webDriver.waitForElement("/html/body/div[4]/div[1]/div[2]/div/div[1]/div/div/span",ByTypes.xpath).getText();
			String  activeTask = webDriver.waitForElement("//li[contains(@class,'active')]/a",ByTypes.xpath).getText(); 
			
			if (!activeTask.equalsIgnoreCase(Integer.toString(taskCount))){
				goTolastTestSectionItem(taskCount,activeTask);
				
				/*
					for (int i =1; i<taskCount; i++){
						sleep(1);
						checkSubmitSectionDisplay(false);
						pressOnNextTaskArrow();
						report.addTitle("Clicked on next: " + i + " time.");
					}
					Thread.sleep(2000);
					//checkSubmitSectionDisplay(true);
				}
				activeTask = webDriver.waitForElement("//li[contains(@class,'active')]/a",ByTypes.xpath).getText(); 
				if (!activeTask.equalsIgnoreCase(Integer.toString(taskCount))){
					
					int currentTask = Integer.parseInt(activeTask);
					
					for (int i =currentTask; i<taskCount; i++){
						sleep(1);
						pressOnNextTaskArrow();
				}
				*/
					Thread.sleep(1000);
					//checkSubmitSectionDisplay(true);
					}
				
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	
	private void goTolastTestSectionItem(int taskCount, String activeTask) throws Exception {
		
		int currentTask = Integer.parseInt(activeTask);
		//
		//WebElement element = webDriver.waitForElement("ulTasks",ByTypes.className,false,2); // CT page
		//List<WebElement> elementCouns= webDriver.getChildElementsByXpath(element,"li");
		
		List<WebElement> elementN = null;
		
		//List<WebElement> elementCounsN= webDriver.getChildElementsByXpath(elementN,"li");
	
		//WebElement enabledokButton = driver.findElement(By.xpath("//button[text() = 'OK' and not(@aria-disabled = 'false')]"));
		
		
		int pageInthisSection = (int)Math.ceil(taskCount/5.0);
		
		//int goToTask = taskCount-(5*pageInthisSection) % 5;
		
		//for (int i=1;i<=pageInthisSection;i++){			
			//elementN = webDriver.getWebDriver().findElements(By.xpath("//*[@id='mainAreaTD']/div[2]/div/div[1]/div/ul/li[not(@style = 'display:none')]"));
		
			elementN = webDriver.getWebDriver().findElements(By.className("unvisited"));
			WebElement lastTask = null;
			int nextSetPages = 0;
			
			if(pageInthisSection>1) {
				for(int i = 1;i<pageInthisSection;i++) {
					elementN.get(4).findElement(By.tagName("a")).click();
					Thread.sleep(1000);
					pressOnNextTaskArrow();
				}
				Thread.sleep(1000);
				elementN.get(elementN.size()-1).findElement(By.tagName("a")).click();
				Thread.sleep(1000);
			}else {
				elementN.get(elementN.size()-1).findElement(By.tagName("a")).click();
				Thread.sleep(1000);
			}
			
			
			
		//	===================================================
		/*
			if(elementN.size()>5) {
				lastTask = elementN.get(4);
				nextSetPages = elementN.size() - 5;
			}else {
				lastTask = elementN.get(elementN.size()-1);
				
			}
			
			try{
				lastTask.click();	
				Thread.sleep(2000);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			
			if(nextSetPages>0) {
				pressOnNextTaskArrow();
				Thread.sleep(2000);
				elementN.get(elementN.size()-1).click();
				Thread.sleep(2000);
			}
			*/
			
			/*
			
			List<WebElement> pagesAfterArrow = webDriver.getWebDriver().findElements(By.cssSelector(".unvisited.active"));
			if(pagesAfterArrow!=null)
			{
				pressOnNextTaskArrow();
				Thread.sleep(2000);
				pagesAfterArrow.get(pagesAfterArrow.size()-1).click();
				sleep(2);
				
			}
			*/
			
	/*		
			Thread.sleep(2000);
			WebElement arrow = webDriver.waitForElement("tasksBtnext", ByTypes.className);
			boolean isArrowEnable = false;
			String arr = "true";
			try {
				arr = arrow.getAttribute("disabled");
			}catch (Exception e) {
				isArrowEnable = true;
			}
			
			while(arr==null) {
				arrow.click();
				sleep(2);
				elementN = webDriver.getWebDriver().findElements(By.xpath("//*[@id='mainAreaTD']/div[2]/div/div[1]/div/ul/li[not(@style = 'display:none')]"));
				lastTask = elementN.get(elementN.size()-1);
				lastTask.click();
				isArrowEnable = false;
				try {
					arrow.getAttribute("disabled");
				}catch (Exception e) {
					isArrowEnable = true;
				}
			}	
			
		*/		
				
			//*[@id="mainAreaTD"]/div[2]/div/div[1]/div/div/text()[2]
		//	if (i>=1 && i<pageInthisSection){
		//		pressOnNextTaskArrow();
		//		elementN = null;
		//		sleep(1);
		//	}
				
		

		//
		
		
/*	
		for (int i =currentTask; i<taskCount; i++){	
			checkSubmitSectionDisplay(false);
			pressOnNextTaskArrow();
			sleep(2);
			report.addTitle("Clicked on next: " + i + " time.");
		}
*/	
		//Thread.sleep(2000);
		//checkSubmitSectionDisplay(true);
		//activeTask = webDriver.waitForElement("//li[contains(@class,'active')]/a",ByTypes.xpath).getText(); 
				
		//if (!activeTask.equalsIgnoreCase(Integer.toString(taskCount)))
		//	goTolastTestSectionItem(taskCount,activeTask);
		
	}
	public int getSectionTaskCountClassic() throws Exception {
		
		WebElement element;
		try{
			element = webDriver.waitForElement("ulTasks",ByTypes.className,false,2); // CT page
			
			if (element == null){
				 //element = webDriver.waitForElement("//*[@id='mainAreaTD']/div[2]/div/div[1]/div/ul",ByTypes.xpath,false,webDriver.getTimeout());
			    element = webDriver.waitForElement("//*[@id='btnTools']/div/ul",ByTypes.xpath,false,30); // from TMS
			}
			
			List<WebElement> elementCouns= webDriver.getChildElementsByXpath(element,"li");
			int taskCount = elementCouns.size();
			return taskCount;
		
		} catch (Exception e) {
			testResultService.addFailTest("Element task count not found", false, true);	
			
			//String ct = webDriver.getCookie("CT");
			//String compId;
			//List<String[]> items = dbService.getComponentItems(compId);
			
			int taskCount =  0;
			
			return taskCount;
		}
	}
	
	private void sleep(int i) {
		// TODO Auto-generated method stub
		
	}


	public void pressOnSubmitSection (boolean pressSubmitOnAlert) throws Exception{
		WebElement element=null;
		
		for (int i=0;i<=20 && element==null;i++){
			element = webDriver.waitForElement("SubmitTest", ByTypes.id,false,1);
		}
		element.click();
		
		//webDriver.waitForElement("SubmitTest", ByTypes.id,false,webDriver.getTimeout()).click();
		Thread.sleep(2000);
		boolean status = false;
		int i = 0;
		if (pressSubmitOnAlert) {
			
			while ((!status) && (i<3)){
				webDriver.sleep(1);
				status = webDriver.waitForElement("//div[@id='EdoFrameBoxContent']/iframe[1]", ByTypes.xpath).isDisplayed();
				i++;
			}
			webDriver.switchToFrame(webDriver.waitForElement("//div[@id='EdoFrameBoxContent']/iframe[1]", ByTypes.xpath,false,3));
			webDriver.sleep(1);
			webDriver.waitForElement("btnOk", ByTypes.name).click();
			webDriver.sleep(2);
			switchToTestAreaIframe();
		}
	}
	
	public void selectCorrectAnswerMCQ () throws Exception {
		
		WebElement answersWrapper = webDriver.waitForElement("//div[contains(@class,'lessonAnswersWrapper')]", ByTypes.xpath);
		List<WebElement> answers = webDriver.getChildElementsByXpath(answersWrapper, "//div[contains(@class,'ADUM')]");
		int count=0;
				
		while (count < answers.size()){
			
			boolean isCorrect = answers.get(count).getAttribute("class").startsWith("c");
			
			if (isCorrect) {
				answers.get(count).findElement(By.className("multiRadio")).click();
				break;
			}
		
			count++;
		
		}
			
	}
	
	public void selectWrongAnswerMCQ () throws Exception {
		
		WebElement answersWrapper = webDriver.waitForElement("//div[contains(@class,'lessonAnswersWrapper')]", ByTypes.xpath);
		List<WebElement> answers = webDriver.getChildElementsByXpath(answersWrapper, "//div[contains(@class,'ADUM')]");
		int count=0;
				
		while (count < answers.size()){
			
			boolean isWrong = !answers.get(count).getAttribute("class").startsWith("c");
			
			if (isWrong) {
				answers.get(count).findElement(By.className("multiRadio")).click();
				break;
			}
		
			count++;
		
		}
			
	}
	
	public void close() throws Exception{
		webDriver.closeNewTab(1);
		webDriver.switchToMainWindow();
		
	}
	
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
			waitForTestPageLoaded();
			//webDriver.waitForElement("//*[@id='mainAreaTD']/div[1]", ByTypes.xpath, true, webDriver.getTimeout()); // Right Resource
			//webDriver.waitForElement("//*[@id='mainAreaTD']/div[2]", ByTypes.xpath, false, webDriver.getTimeout()); // Left Resource
			
			String initialLevel = firstCycle.getPltStartLevel().toString().substring(0, 2);
			answerCycleQuestions(firstCycle);

			// report.report("End of cycle 1");
			clickOnGoOnButton();
			waitForTestPageLoaded();
			
			// Start of cycle 2
			answerCycleQuestions(SecondCycle);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	/// For Did test
	
	public void performTestDidTest(PLTTest pltTest) throws Exception {
		TextService textService = new TextService();
		
		
		PLTCycle firstCycle = pltTest.getCycles().get(0);
		
		selectLevelOnStartPLT(firstCycle.getPltStartLevel());
		
		pressOnStartPLT();
		answerCycleQuestions(firstCycle);


	}
	
	public void performCourseTest(CourseTest courseTest, int sectionsToSubmit) throws Exception {
				
		CourseTestSection section = new CourseTestSection();
		
		for (int i = 0; i < sectionsToSubmit; i++) {
			if (courseTest.getSections().size() > i) {
				section = courseTest.getSections().get(i);
				answerSectionQuestions(section);
			}
			browseToLastSectionTask();
			pressOnSubmitSection(true);
			Thread.sleep(2000);
		}			
		

	}

	public void clickOnGoOnButton() throws Exception {
		webDriver.waitForElement("DoAgain", ByTypes.id).click();

	}
	
	public void answerCycleQuestions(PLTCycle pltCycle) throws Exception {
		
		for (int i = 0; i < pltCycle.getNumberOfQuestions(); i++) {
			
			Thread.sleep(1000);
			
			TestQuestion question = pltCycle.getCycleQuestions().get(i);
			
			TestQuestionType questionType = question.getQuestionType();

			// convert ~ to ,
			
						
			String[] answers = new String[question.getCorrectAnswers().length];
			
			for (int j = 0; j < question.getCorrectAnswers().length; j++) {
				answers[j] = question.getCorrectAnswers()[j].replace("~", ",");
				answers[j] = textService.resolveAprostophes(answers[j]);
			}
			waitForTestPageLoaded();
			question.setCorrectAnswers(answers);
			
			if (!question.getIncoreectAnswers()[0].equals("DoNotAnswer")) { // skip the question if wrong answer exist in csv file or parameter of skipCycle is true
			
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
			sleep(1);
			report.addTitle("Answered on question number:" + (i+1));
			clickOnNextButtonPLT();
			//webDriver.scrollToElement(webDriver.waitForElement("/html/body", ByTypes.xpath));
			//waitPltQuestionDisplay();
			

			//Thread.sleep(1000);
		}
		Thread.sleep(1000);
	}
	
	public void waitPltQuestionDisplay() {
		
		String questionText="";
		int i;
		for (i=0;i<=30 && questionText.equalsIgnoreCase("");i++){				
					try {
						questionText =  webDriver.waitForElement("//div[contains(@class,'answersWrapper')]", ByTypes.xpath, false, 1).getText();
						//questionText = element.getText();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	
				}
		report.addTitle("amswers area found after: " + i + " sendond");
		}
	
	
	public void answerSectionQuestions(CourseTestSection section) throws Exception {
		
		for (int i = 0; i < section.getNumberOfQuestions(); i++) {
			
			Thread.sleep(1000);
			
			TestQuestion question = section.getSectionQuestions().get(i);
			
			TestQuestionType questionType = question.getQuestionType();

			// convert ~ to ,
			
						
			String[] answers = new String[question.getCorrectAnswers().length];
			
			for (int j = 0; j < question.getCorrectAnswers().length; j++) {
				answers[j] = question.getCorrectAnswers()[j].replace("~", ",");
				answers[j] = textService.resolveAprostophes(answers[j]);
			}
			question.setCorrectAnswers(answers);
			
			waitForTestPageLoaded();
			
			if (!question.getIncoreectAnswers()[0].equals("DoNotAnswer")) { // skip the question if wrong answer exist in csv file or parameter of skipCycle is true
			
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
			pressOnNextTaskArrow();

		}
	}
	
	public void answerMultiCheckBoxQuestion(TestQuestion question)
			throws Exception {
		for (int i = 0; i < question.getCorrectAnswers().length; i++) {
			webDriver.waitForElement(
					"//span[@class='multiTextInline'][contains(text(),"
							+ question.getCorrectAnswers()[i] + ")]",
					ByTypes.xpath).click();
		}

	}
	
	public void selectAnswerByTextMCQ(String text)
			throws Exception {
		
		
		//webDriver.waitForElement("//span[@class='multiTextInline'][contains(text()," + text.getCorrectAnswers()[0] + ")]", ByTypes.xpath).click();
		
		webDriver.waitForElement("//span[@class='multiTextInline'][contains(text()'"+ text +"')]",ByTypes.xpath).click();
		
			
			//webDriver.waitForElement("//*[@id='q1a1']/div/div/div",ByTypes.xpath).click();
	}

	public void dragAnswer(String answerId, String questionLocationId)
			throws Exception {
		WebElement from = webDriver.waitForElement(
				"//div[@data-id='" + answerId + "']",
				ByTypes.xpath,
				"element of answer number text "
						+ questionLocationId.toString());
		WebElement to = webDriver.waitForElement("//span[@data-id='"
				+ questionLocationId + "']", ByTypes.xpath);
		webDriver.dragAndDropElement(from, to);

	}

	public void answerDragAndDropQuestion(TestQuestion question)
			throws Exception {	
		
		String[] answerSourceLocations = question.getCorrectAnswers();
		String[] answerDestinationLocations = question.getAnswersDestinations();
		for (int i = 0; i < answerSourceLocations.length; i++) {
			int index = i + 1;
			WebElement from = webDriver.waitForElement(
					"//div[text()=" + question.getCorrectAnswers()[i] + "]",
					ByTypes.xpath,
					1,false,"element of answer number text "
							+ answerSourceLocations[i].toString());
			WebElement to = webDriver.waitForElement("//span[@data-id='1_"
					+ index + "']", ByTypes.xpath,false,1);
			webDriver.dragAndDropElement(from, to);
		}
		
	}
	
	public void answerCheckboxQuestion(TestQuestion question) throws Exception {
		/*List<WebElement> answers = webDriver.getWebDriver().findElements(By.xpath("//div[@class='multiText']"));
		List<WebElement> answersCheckboxes =  webDriver.getWebDriver().findElements(By.xpath("//input[@class='lessonMultipleCheck']"));
		for (int i = 0; i < answers.size(); i++) {
			if (answers.get(i).getText().toLowerCase().contains(question.getCorrectAnswers()[0].toLowerCase()) || question.getCorrectAnswers()[0].toLowerCase().contains(answers.get(i).getText().toLowerCase())) {
				answersCheckboxes.get(i).click();
				break;
			}
		}*/
		
		webDriver.waitForElement("//span[@class='multiTextInline'][contains(text()," + question.getCorrectAnswers()[0] + ")]", ByTypes.xpath,false,1).click();

	}

	public void answerComboBoxQuestion(TestQuestion question) throws Exception {
		int index = 0;
		for (int i = 0; i < question.getCorrectAnswers().length; i++) {
			index = i + 1;
			WebElement element = webDriver.waitForElement("//div[@class='fitb']//span[@id='1_" + index + "']//div[2]",ByTypes.xpath,false,1);
			element.click();
			Thread.sleep(1000);
			webDriver.waitForElement(
					"//div[@class='optionsWrapper']//table//tbody//tr//td[contains(text(),"+ question.getCorrectAnswers()[i] + ")]",
					ByTypes.xpath,false,1).click();

		}

	}
	
	public void clickOnNextButtonPLT() throws Exception {
		boolean nextIsDisplay = false;
		WebElement element = null;
		
		for (int i =0; i<30 && (!nextIsDisplay) ;i++){
			element = webDriver.waitForElement("nextQuest", ByTypes.id,1,false);
			
			if (element !=null)
			{
				nextIsDisplay = true;
				element.click();
				break;
			}
			else{
				report.addTitle("Next button doesn't display after: " + i + " Seconds");
			}
		}
		
	}
	
	public void playMedia() throws Exception {
		webDriver.waitForElement("CTrackerPlayBtn", ByTypes.id,
				"Play media button").click();

	}
	
	public void verifyPlacementLevelOnResultPagePLT(String expectedLevel) throws Exception {
		
		WebElement element=null;
		String actualLevelInGraph=null;
		sleep(1);
	
		element = webDriver.waitUntilElementAppears("//div[@id='resultTitle']//b", ByTypes.xpath, 10);
		
			
		if (element != null && (element.getText().length()>0)) {
			actualLevelInGraph = element.getText();
		}
		
		if (actualLevelInGraph == null){
			actualLevelInGraph = "No Result Graph";
		}
		
		sleep(3);
		WebElement header = webDriver.waitForElement("//div[@id='rightTitle']", ByTypes.xpath, false, webDriver.getTimeout());
		String actualTextInHeader="";
		if (header != null){
			actualTextInHeader = header.getText().replace(".", " ");	
		}else
		{actualTextInHeader = "No Header Graph";}
		
		
		testResultService.assertEquals(expectedLevel, actualLevelInGraph, "Level in graph not valid");
		testResultService.assertEquals(true, actualTextInHeader.contains(expectedLevel), "Level in right header not valid");
		
		if(AssessmentsTests.useCEFR) {
			WebElement text = webDriver.waitForElement("rightDisclaimer", ByTypes.id, false, webDriver.getTimeout());
			String suggestionText = text.getText();
			testResultService.assertEquals(true, suggestionText.contains("CEFR"), "CEFR not exist");
		}
	}
	
	public void verifyDescriptionLinkPLT(String [] levels) throws Exception {
	
		webDriver.waitForElement("//div[@id='rightTitle']//a", ByTypes.xpath, "Description link").click();
		Thread.sleep(2000);
		webDriver.switchToPopup();
		List <WebElement> coursesTitles = webDriver.waitForElement("stext", ByTypes.className).findElements(By.tagName("b"));
		
		for (int i = 0; i < levels.length; i++) {
			testResultService.assertEquals(levels[i], coursesTitles.get(i).getText(), "Course Title in description not valid or not found");
		}
		
		webDriver.closeNewTab(1);
		switchToTestAreaIframe();
		
	}
	
	public void verifyPreferToStudyLinkPLT() throws Exception {
		
		webDriver.waitForElement("//div[@class='addText']//a", ByTypes.xpath, "Prefer to study link").click();
		Thread.sleep(2000);
		webDriver.switchToMainWindow();
		webDriver.switchToNewWindow();
		switchToTestAreaIframe();
		webDriver.switchToPopup();
		List <WebElement> radioBtns = webDriver.waitForElement("//table[@class='stext']", ByTypes.xpath, "Radio Buttons").findElements(By.tagName("input"));
		radioBtns.get(0).click();
		WebElement element = webDriver.waitForElement("submitBtn", ByTypes.className,webDriver.getTimeout(),false, "Submit selection");
		element.click();
		Thread.sleep(2000);
		webDriver.switchToMainWindow();
		webDriver.switchToNewWindow();
		switchToTestAreaIframe();
		
	}
	
	public void clickOnExitPLT() throws Exception {
		
		WebElement element = webDriver.waitUntilElementAppears("Exit", ByTypes.name,10);
		//WebElement element = webDriver.waitUntilElementAppears("learning__testEndPLTButtonW--cancel", ByTypes.className,10);
		if (element!=null)
			element.click();
		else
			testResultService.addFailTest( "Exit PLT not Display", false, true);

		webDriver.switchToMainWindow();
	}	
	
	public void clickOnDoTestAgainPLT() throws Exception {
		
		getDoTestAgainElement().click();
		Thread.sleep(2000);
		webDriver.switchToMainWindow();
		
		
	}	
	
	public void verifyDoTestAgainNotDisplayedInPLT() throws Exception {
		
		if (getDoTestAgainElement()!= null) {
			testResultService.addFailTest("Do Test Again button displayed though should be hidden", false, true);
		}
		
		
	}	
	
	private WebElement getDoTestAgainElement() throws Exception {
		
		return webDriver.waitForElement("DoAgain", ByTypes.name, 3, false, "Do Test Again button not found");
				
	}	
	
	public CourseTest initCourseTestFromCSVFile(String path, CourseCodes courseCode, CourseTests courseTestType, int sectionsToAnswer) throws Exception {
		
		List<CourseTestSection> testSections = new ArrayList<CourseTestSection>();
		CourseTestSection section = new CourseTestSection();
		
		for (int i = 0; i < sectionsToAnswer; i++) {
			
			section = initSection(path, i+1, courseCode, courseTestType, false);
			testSections.add(section);
		}
		
		CourseTest courseTest = new CourseTest(testSections, courseTestType);
		
		return courseTest;
	}
	
	public CourseTestSection initSection(String path, int sectionNumber, CourseCodes courseCode, CourseTests courseTestType, boolean skipSection) throws Exception {
		
		List<String[]> testData = textService.getStr2dimArrFromCsv(path);
	
		List<TestQuestion> sectionQuestions = new ArrayList<TestQuestion>();
		
		for (int i = 0; i < testData.size(); i++) {
			
			// if cycle number match - create TestQuestion object
			if (testData.get(i)[0].equals(String.valueOf(sectionNumber)) && testData.get(i)[1].equals(courseCode.toString()) && testData.get(i)[2].equals(courseTestType.toString())) {
				
				try {
					report.report("Reading line: "+(i+1)+" from csv file");
					
					String[] answers = textService.splitStringToArray(testData.get(i)[3],"\\|");
					
					String[] answerDestinations = textService.splitStringToArray(testData.get(i)[4],"\\|");
					
					String[] wrongAnswers = textService.splitStringToArray(testData.get(i)[5],"\\|");
					
					if (skipSection) wrongAnswers[0] = "DoNotAnswer";
						
	//			int[] blankAnswers = textService.splitStringToIntArray(
	//					testData.get(i)[5], "?!^");
	
	//			boolean booleanAnswer = Boolean.getBoolean(testData.get(i)[5]);
					// String questionType = testData.get(i)[6];
					report.report("Question type from csv is: "+testData.get(i)[6]);
					TestQuestionType questionType = TestQuestionType
							.valueOf(testData.get(i)[6]);
	
					TestQuestion question = new TestQuestion(answers,
							answerDestinations, wrongAnswers, new int[]{},
							 questionType);
					sectionQuestions.add(question);
					report.report("Finished adding question data for line: "+i);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		CourseTestSection testSection = new CourseTestSection (sectionNumber, courseCode, courseTestType, sectionQuestions);
		
		return testSection;
	
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
			
	public PLTCycle initCycle(String cycleNumber, String path,
			PLTStartLevel pltStartLevel, boolean skipCycle) throws Exception {
		
		List<String[]> testData = textService.getStr2dimArrFromCsv(path);
	
		List<TestQuestion> cycleQuestions = new ArrayList<TestQuestion>();
		
		for (int i = 0; i < testData.size(); i++) {
			
			// if cycle number match - create TestQuestion object
			if (testData.get(i)[0].equals(cycleNumber)) {
				
				try {
					report.report("Reading line: "+i+" from csv file");
					
					String[] answers = textService.splitStringToArray(testData.get(i)[1],"\\|");
					
					String[] answerDestinations = textService.splitStringToArray(testData.get(i)[2],"\\|");
					
					String[] wrongAnswers = textService.splitStringToArray(testData.get(i)[3],"\\|");
					
					if (skipCycle) wrongAnswers[0] = "DoNotAnswer";
						
	//			int[] blankAnswers = textService.splitStringToIntArray(
	//					testData.get(i)[5], "?!^");
	
	//			boolean booleanAnswer = Boolean.getBoolean(testData.get(i)[5]);
					// String questionType = testData.get(i)[6];
					report.report("Question type from csv is: "+testData.get(i)[5]);
					TestQuestionType questionType = TestQuestionType
							.valueOf(testData.get(i)[5]);
	
					TestQuestion question = new TestQuestion(answers,
							answerDestinations, wrongAnswers, new int[]{},
							 questionType);
					cycleQuestions.add(question);
					report.report("Finished adding question data for line: "+i);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		PLTCycle pltCycle = new PLTCycle(pltStartLevel,
				Integer.valueOf(cycleNumber), null, cycleQuestions, null);
		return pltCycle;
	
	}
	
	public void checkSubmitSectionDisplay (boolean buttonDisplay) throws Exception {
		
		boolean currentState = false;
		WebElement element = webDriver.waitForElement("SubmitTest", ByTypes.id,1, false);
		
		if (element != null){
			currentState = true;
		}
		TextService.assertEquals("The Submit section button wrong display", buttonDisplay, currentState);
	}
	
	public ArrayList<String> getAnswersArrayBySectionNumber(String sectionNumber, String path) throws Exception {
		List<String[]> testData = textService.getStr2dimArrFromCsv(path);

		ArrayList<String> answersArray = new ArrayList<String>();
		int size = testData.size();
		for (int i = 0; i < size; i++) {
			if (testData.get(i)[3].equals(sectionNumber)) {
				answersArray.add(testData.get(i)[4]);
			}
		}
		return answersArray;
	}
	
	public HashMap<String, String> getUsersListFromCSV(String path) throws Exception {
		List<String[]> testData = textService.getStr2dimArrFromCsv(path);
		
		HashMap<String, String> users = new HashMap<String, String>();
		
		int size = testData.size();
		for (int i = 0; i < size; i++) {
			users.put(testData.get(i)[0], testData.get(i)[1]);
		}
		return users;
	}


	public void startPltAndExit() {
		try {
			webDriver.waitForElement("rbBasic", ByTypes.id, "Basic level button").click();
			
			pressOnStartPLT();
			waitForTestPageLoaded();

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
	
	public void SubmitFirstSectionWithCorrectAnswers(int section,String testId, boolean closeTest) throws Exception{
		
		String filePath = "";
		
		CourseTests testType = CourseTests.MidTerm;
		
		if (testId.equals("989012509")) {
			filePath = "files/CourseTestData/CourseTest_Answers_2019.csv";
		} else if (testId.equals("989013148")) {
			filePath = "files/CourseTestData/CourseTest_Answers_B1_MT_989013148.csv";
		} else if (testId.equals("47845")) {
			filePath = "files/CourseTestData/CourseTest_Answers_2019.csv";
			testType = CourseTests.FinalTest;	
		} else if (testId.equals("1540731")) {
			filePath = "files/CourseTestData/CourseTest_Answers_B1_1540731.csv";
			testType = CourseTests.FinalTest;
		} else {
			filePath = "files/CourseTestData/CourseTest_Answers.csv";
		}
		
		
		CourseTest courseTest = initCourseTestFromCSVFile(filePath, CourseCodes.B1, testType, section);
		performCourseTest(courseTest, section);
		
		if (closeTest) {
			close();
		}
	}
	
	public String submitRemainingTestEmpty(int sectionsToSubmit) throws Exception {
		for (int i = 0; i < sectionsToSubmit-1 ; i++) {
			browseToLastSectionTask();
			pressOnSubmitSection(true);
			report.startStep("Submited section: " + (i+2));
		}
	
	report.startStep("Get score and close test");
		switchToCompletionMessageFrame();
		sleep(1);
		String finalScore = getFinalScore();
		webDriver.switchToTopMostFrame();
		switchToTestAreaIframe();
		closeCompletionMessageAlert();
		sleep(1);
		
		return finalScore;
	}
	
	public String getTitleOfPlacementTestInSpecificLevel(PLTStartLevel level, String studentId) throws Exception {
		
		//report.startStep("Choose "+level+" level");
		selectLevelOnStartPLT(level);
		pressOnStartPLT();
				
		// retrieve title
		String title = webDriver.waitForElement("textContainer", ByTypes.id, false, webDriver.getTimeout()).getText();
		title = title.split("\n")[0];
		
		// click exit test and exit on the popup
		clickExitPltAndClosePopUp();
		
		// click do test again
		clickDoPltTestAgain();

		return title;
	}
	
	public void clickExitPltAndClosePopUp() throws Exception {
		webDriver.waitForElement("//*[@id='exitCell']/table/tbody/tr/td[3]", ByTypes.xpath).click();
		Thread.sleep(1500);
		webDriver.switchToPopup();
		webDriver.waitForElement("submitBtn", ByTypes.className).click();
		Thread.sleep(1500);
	}
	
	public void clickDoPltTestAgain() throws Exception {
		webDriver.switchToMainWindow();
		webDriver.switchToNewWindow();
		switchToTestAreaIframe();
		getDoTestAgainElement().click();
		Thread.sleep(1000);
	}
	
	public void clickExitPltButton() throws Exception {
		webDriver.switchToMainWindow();
		webDriver.switchToNewWindow();
		switchToTestAreaIframe();
		webDriver.waitForElement("//div[@id='exitCell']//tr", ByTypes.xpath).click();
		Thread.sleep(1000);
		webDriver.switchToMainWindow();
	}
	
	public void validateTitlesAreCorrect(String[] expectedTitles, String[] actualTitles) {
		PLTStartLevel[] levels= new PLTStartLevel[]{PLTStartLevel.Basic, PLTStartLevel.Intermediate, PLTStartLevel.Advanced, PLTStartLevel.IamNotSure};
		for (int i = 0; i < levels.length; i++) {
			testResultService.assertEquals(expectedTitles[i], actualTitles[i],"Title is not displayed Correctly in Level: "+levels[i]+".");
		}
	}
	
	public void startPltTest() throws Exception {
		NewUxAssessmentsPage testsPage;
		NewUxHomePage homePage = new NewUxHomePage(webDriver, testResultService);
		
		//report.startStep("Open Assessments Page");
		testsPage = homePage.openAssessmentsPage(false);
				
		//report.startStep("Click Start Test on the First Test");
		testsPage.clickStartTest(1, 1);
		sleep(1);
	
		//report.startStep("Check PLT opens");
		webDriver.switchToNewWindow();
		switchToTestAreaIframe();
		verifyTitlePLT();
		sleep(2);
	}


	public void waitForTestPageLoaded() {
		WebElement element =null;
		
		for (int i=0;i<=20 && element==null;i++){
			try {
				element = webDriver.waitForElement("//*[contains(@class,'answersWrapper')]", ByTypes.xpath, false, 1);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public void clickSubmitOnPLT_API() throws Exception{
		webDriver.waitForElement("btnSubmit", ByTypes.name).click();
		Thread.sleep(2000);
	}
	
	public void verifyLevelListExist() throws Exception {
		List<WebElement> levels = (List<WebElement>) webDriver.getWebDriver().findElements(By.xpath("//tr[@class='levels']//input[contains(@id,'rb')]"));
		testResultService.assertEquals("4", Integer.toString(levels.size()),"Levels are not Displayed Correctly."); 
	}
	
	public void verifyLanguagesExists() throws Exception {
		WebElement languages= webDriver.waitForElement("//select[@name='languageCh']",ByTypes.xpath,false, 10);
		testResultService.assertEquals(true, webDriver.isDisplayed(languages), "Languages element is not Displayed.");
	}
	
	public void verifyStartTestButtonsExists() throws Exception{
		WebElement startTestButton = webDriver.waitForElement("Submit", ByTypes.name);
		testResultService.assertEquals(true, webDriver.isDisplayed(startTestButton), "Start Test Button is not Displayed.");
	}
	
	public void validateTimer(int expectedTime) throws Exception {
		WebElement timer = webDriver.waitForElement("remTime", ByTypes.id);
		String time = timer.getText();
		testResultService.assertEquals(convertTimeInMinutesToEDTimerFormatOldTe(expectedTime), time, "Start Test Button is not Displayed.");

	}
	
	public String convertTimeInMinutesToEDTimerFormatOldTe(int minutes){

		int hoursEdFormat = (int) Math.floor(minutes/60);
		int minutesEdFormat = minutes - (hoursEdFormat*60);
		
		return hoursEdFormat +":"+minutesEdFormat;
	}


	public void clickOnExitPLTnewTE() throws Exception {
		
		WebElement element = webDriver.waitUntilElementAppears("learning__testEndPLTButtonW--cancel", ByTypes.className,10);
		if (element!=null)
			element.click();
		else
			testResultService.addFailTest( "Exit PLT not Display", false, true);
		
	}


	public void verifyMidtermTitleOldTE(String testFullName) throws Exception {
		WebElement title = webDriver.waitForElement("welcomeTitle", ByTypes.className);
		String midtermTitle = title.getText();
		textService.assertTrue("Wrong title, or title missing", midtermTitle.contains("Mid-term Test"));
	}
}
