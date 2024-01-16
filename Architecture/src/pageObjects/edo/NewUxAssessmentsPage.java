package pageObjects.edo;

import Enums.ByTypes;
import drivers.GenericWebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.util.Assert;
import pageObjects.GenericPage;
import services.TestResultService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewUxAssessmentsPage extends GenericPage {

		
	private static final String X_Button_Xpath = "//a[@class='modal-close']";
	
	private static final String PLT = "Placement Test";

	public NewUxAssessmentsPage(GenericWebDriver webDriver,
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

	public void close() throws Exception {
		
		WebElement Xbuton = webDriver.waitForElement(X_Button_Xpath, ByTypes.xpath, "Assessments X button");
		Thread.sleep(1500);
		
		//Xbuton.click();
		webDriver.clickOnElementByJavaScript(Xbuton);
			

	}

	public void verifyAssessmentsPageHeader() throws Exception {
		String header = webDriver.waitForElement(
				"//h2[contains(@class,'modal-title')]", ByTypes.xpath,false,10)
				.getText();
		testResultService.assertEquals("Assessment Center", header,
				"Verifying  Assessments Page Header");
	}

	public void checkItemsCounterBySection(String sectionNum,
			String itemsInSectionNum) throws Exception {

		String actualItemsNum = webDriver.waitForElement(
				"//div[contains(@class,'assessments__main')]/div[" + sectionNum
						+ "]//div[contains(@class,'titleCount')]",
				ByTypes.xpath).getText();
		testResultService.assertEquals(itemsInSectionNum, actualItemsNum,
				"Verifying Items Counter in Section" + sectionNum);
		
	}

//	public void checkTestDisplayedInSectionByTestName(String expectedTestName,
//			String sectionNum, String testSequence) throws Exception {
//
//		WebElement testName = webDriver.waitForElement(
//						"//div[contains(@class,'assessments__main')]/div["
//								+ sectionNum
//								+ "]//table[contains(@class,'assessments__table')]//tr["
//								+ testSequence
//								+ "]//td[contains(@class,'assessments__testName')]", ByTypes.xpath,2,false);
//
//		String actualTestName = testName.getText();
//
//		//actualTestName = actualTestName.substring(actualTestName.length()-expectedTestName.length());
//		testResultService.assertEquals(expectedTestName, actualTestName, "Verifying Test Displayed in Section" + sectionNum, true);
//	}



	public boolean checkTestDisplayedInSectionByTestName(String expectedTestName,String sectionNum,String testSequence) throws Exception {
		WebElement testName = webDriver.waitForElement(
				"//div[contains(@class,'assessments__main')]/div["
						+ sectionNum
						+ "]//table[contains(@class,'assessments__table')]//tr["
						+ testSequence
						+ "]//td[contains(@class,'assessments__testName')]", ByTypes.xpath,2,false);

		if (testName==null){
			NewUxClassicTestPage newUxClassicTestPage = new NewUxClassicTestPage(webDriver, testResultService);
			newUxClassicTestPage.close();
			NewUxHomePage homePage = new NewUxHomePage(webDriver,testResultService);
			homePage.openAssessmentsPage(false);

			String actualTestName = testName.getText();

			return actualTestName.equals(expectedTestName);

		}  else {

			return true;

		}
	}

			
	public void checkTestDisplayedInSectionByTestNameAndCourseId(String expectedTestName,
			String sectionNum, String courseId) throws Exception {
		
		List<WebElement> tests = webDriver.getWebDriver().findElements(By.xpath("//div[contains(@class,'assessments__section availableTests')]//tbody/tr/td[2]"));
		
		String actualTestName="";
		actualTestName = tests.get(0).getText();

		testResultService.assertEquals(expectedTestName, actualTestName, "Verifying Test Displayed in Section" + sectionNum, true);			
	}
	
	
	public void checkStartTestBtnDisplayedByTest(String sectionNum, String testSequence) throws Exception {

		WebElement element = getStartTestElementByTest(sectionNum, testSequence);
		testResultService.assertEquals("Start Test", element.getText(),"Verifying Start Test btn text in section: " + sectionNum);

	}
	
	private WebElement getStartTestElementByTest(String sectionNum, String testSequence) throws Exception {
	
		WebElement element = webDriver.waitForElement("//div[contains(@class,'assessments__main')]/div["+ sectionNum+ "]//table[contains(@class,'assessments__table')]//tr["+ testSequence+ "]//a[contains(@class,'btnAction')]",ByTypes.xpath,5,false);
		
		return element;
	}
	
	private WebElement getStartTestElementByTestName(String testname,String courseId) throws Exception {
		WebElement element=null;
		//WebElement element = webDriver.waitForElement("//div[contains(@class,'assessments__main')]/div["+ sectionNum+ "]//table[contains(@class,'assessments__table')]//tr["+ testSequence+ "]//a[contains(@class,'btnAction')]",ByTypes.xpath,3,false);
		
		return element;
	}
	
	public void checkDaysInCounter(String daysInCounter, String testSequence) throws Exception {
	
	try {
		WebElement element = webDriver.waitUntilElementAppearsAndReturnElement("//div[contains(@class,'assessments__main')]/div[1]//table[contains(@class,'assessments__table')]//tr["+ testSequence+ "]//div[contains(@class,'timeCellDays')]", 5);
		String actualDays = element.getText(); 
		testResultService.assertEquals(false, actualDays.isEmpty(),"Verifying days in counter, Check the EndDate not more then the Max days to dislay PropertyId=11 and TimeForCourseTest inInstitution table");
	}catch (Exception e) {
			testResultService.addFailTest(e.getMessage(), true, true);
	}catch (AssertionError e) {
		testResultService.addFailTest(e.getMessage(), true, true);
	}
		//testResultService.assertEquals(daysInCounter, actualDays,"Verifying days in counter, Check the EndDate not more then the Max days to dislay PropertyId=11 and TimeForCourseTest inInstitution table");
		
// we need to fix the the day counter sometime is 3 and some to 2 depends on how fast is run
	}
	
	public void checkHoursInCounter(String hoursInCounter, String testSequence) throws Exception {
		try {
		String actualHH = webDriver.waitForElement("//div[contains(@class,'assessments__main')]/div[1]//table[contains(@class,'assessments__table')]//tr["+ testSequence+ "]//div[contains(@class,'timeCellHours')]", ByTypes.xpath).getText();

		//testResultService.assertEquals(hoursInCounter, actualHH,"Verifying hours in counter");
		//testResultService.assertEquals(false, actualHH.isEmpty(),"Verifying hours in counter");
		testResultService.assertEquals(false, actualHH.isEmpty(),"Verifying hours in counter");
	}catch (Exception e) {
		testResultService.addFailTest(e.getMessage(), true, true);
	}catch (AssertionError e) {
		testResultService.addFailTest(e.getMessage(), true, true);
	}
	}
	
	public void checkMinutesInCounter(String minutesInCounter, String testSequence) throws Exception {
		try {
		int actualMinutes = getMinutesInCounter(testSequence);
		int expectedMinutes = Integer.parseInt(minutesInCounter);
		
		if (actualMinutes != expectedMinutes && actualMinutes+1 != expectedMinutes && actualMinutes-1 != expectedMinutes){
		testResultService.addFailTest("Minutes value in counter not in an expected range");}
	}catch (Exception e) {
		testResultService.addFailTest(e.getMessage(), true, true);
	}catch (AssertionError e) {
		testResultService.addFailTest(e.getMessage(), true, true);
		}
	}
	
	public int getMinutesInCounter (String testSequence) throws Exception {

		String actualMin = webDriver.waitForElement("//div[contains(@class,'assessments__main')]/div[1]"
				+ "//table[contains(@class,'assessments__table')]//tr["+ testSequence+ "]"
						+ "//div[contains(@class,'timeCellMinutes')]", ByTypes.xpath).getText();
		int actualMinutes = Integer.parseInt(actualMin);
		
		return actualMinutes;

	}
	
	public int getSecondsInCounter (String testSequence) throws Exception {

		String actualMin = webDriver.waitForElement("//div[contains(@class,'assessments__main')]/div[1]"
				+ "//table[contains(@class,'assessments__table')]//tr["+ testSequence+ "]"
						+ "//div[contains(@class,'timeCellSeconds')]", ByTypes.xpath).getText();
		int actualSeconds = Integer.parseInt(actualMin);
		
		return actualSeconds;

	}
	
	
	public String getTestDateForUpcomingTests (String testSequence) throws Exception {

		String actualTestDate = webDriver.waitForElement("//div[contains(@class,'assessments__main')]/div[2]//table[contains(@class,'assessments__table')]//tr["+ testSequence+"]/td[3]", ByTypes.xpath).getText().trim();

		return actualTestDate;

	}
	
	public void clickOnArrowToOpenSection(String sectionNum) throws Exception {
		try {
			//11webDriver.waitForElement("//div[contains(@class,'assessments__main')]/div["+ sectionNum+ "]//div[contains(@class,'titleBtn')]",ByTypes.xpath).click();
			List<WebElement> arrows = webDriver.getWebDriver().findElements(By.xpath("//div[contains(@class,'layout__push assessments__titleBtn assessments__titleBtn')]"));
			
			webDriver.ClickElement(arrows.get(Integer.parseInt(sectionNum)-1));
			Thread.sleep(2000);
		} catch (Exception e) {
			testResultService.addFailTest("Section is Disabled.", true, true);
		}
	}
	
	public NewUxClassicTestPage clickOnStartTest(String sectionNum, String testSequence) throws Exception {

		WebElement element = getStartTestElementByTest(sectionNum, testSequence);
		
		if (element!=null)
			element.click();
		else
			testResultService.addFailTest("Test button doesn't appear on ED Assessment", true, true);
		
		return new NewUxClassicTestPage(webDriver,testResultService);
	}
	
	public int getAndValidateCourseTestSequence(String courseId,int expectedSequence) throws Exception {

		int actualSequence = getTestSequenceByCourseId(courseId);
		testResultService.assertEquals(expectedSequence, actualSequence, "TestId: " + courseId+ " sequence is not correct");
		
		return actualSequence;
	}
	
	public int getTestSequenceByCourseId(String courseId) {
		List<WebElement> tests = webDriver.getWebDriver().findElements(By.xpath("//div[contains(@class,'assessments__section availableTests')]//tbody/tr"));
	
		int sequence = 0;
		for (int i = 0; i <tests.size();i++) {
			if (tests.get(i).getAttribute("id").contains(courseId)) {
				sequence = i; 
				break;
			}
		}
		return sequence+1;
	}

	public String getDaysText(){
		return webDriver.getWebDriver().findElement(By.cssSelector(".assessments__timeCellDays")).getText();
	}
	public String getMinutesText(){
		return webDriver.getWebDriver().findElement(By.cssSelector(".assessments__timeCellMinutes")).getText();
	}
	public String getSecondsText(){
		return webDriver.getWebDriver().findElement(By.cssSelector(".assessments__timeCellSeconds")).getText();
	}


	public boolean checkTimeDisplay() {
		try {
			return getDaysText().matches("\\d+") && getMinutesText().matches("\\d+") && getSecondsText().matches("\\d+");
		} catch (Exception e) {
			return false;
		}
	}


	public NewUxClassicTestPage getClassicObject() throws Exception {
		
		return new NewUxClassicTestPage(webDriver,testResultService);
	}
	
	public String checkSubmissionDateForTests (String testSequence) throws Exception {

		String actualSubDate = webDriver.waitForElement("//div[contains(@class,'assessments__main')]/div[3]//table[contains(@class,'assessments__table')]//tr["+ testSequence+ "]/td[3]", ByTypes.xpath).getText();
		Date date = new Date();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String expectedSubDate = df.format(date);
		testResultService.assertEquals(expectedSubDate, actualSubDate,"Verifying Submission Date in Test Results");
		
		return actualSubDate;
	}
	
	public String checkSubmissionDateForPastTests(String testSequence, String expectedDate) throws Exception {

		//String actualSubDate = webDriver.waitForElement("//div[contains(@class,'assessments__main')]/div[3]//table[contains(@class,'assessments__table')]//tr["+ testSequence+ "]/td[3]", ByTypes.xpath).getText();
		//testResultService.assertEquals(expectedDate, actualSubDate,"Expected and actual test dates aren't identical");
		
		List<WebElement> dates = webDriver.getWebDriver().findElements(By.xpath("//span[@class='ng-binding ng-scope']"));//td[@class='assessments__testDate']"));
		String actualSubDate = dates.get(Integer.parseInt(testSequence)-1).getText();
		testResultService.assertEquals(expectedDate, actualSubDate,"Expected and actual test dates aren't identical");

		return actualSubDate;
	}
	
	public String checkScoreByTest (String testSequence , String expectedScore) throws Exception {
		String actualScore = "";
		try {
			WebElement score = webDriver.waitForElement("//div[contains(@class,'assessments__main')]/div[3]//table[contains(@class,'assessments__table')]//tr["+ testSequence+ "]//div[contains(@class,'testScoreData')]", ByTypes.xpath, false, 5);
			if (score != null) {																											                                            									
				actualScore = score.getText();
				testResultService.assertEquals(expectedScore, actualScore,"Verifying Score in Test Results");
			} else {
				testResultService.addFailTest("Test was not found in test result section", false, true);
			}
		} catch (Exception e) {
			testResultService.addFailTest("Test was not found in test result section", false, true);
		}
		return actualScore;
	}
	
	public void checkScoreForTOEICTest(String testSequence , String expectedReadingScore, String expectedListeningScore) throws Exception {
		String actualReadingScore = "";
		String actualListeningScore = "";
		
		/*List<WebElement> scores = webDriver.getWebDriver().findElements(By.xpath("//div[@class='testScoreRange']"));
		actualReadingScore = scores.get(0).getText();
		actualListeningScore = scores.get(1).getText();*/
		
		// I'm very optimistic regarding the next lines, as I assume xpath will be used instead
		actualReadingScore = webDriver.waitForElement("ReadingScore", ByTypes.id).getText();
		actualListeningScore = webDriver.waitForElement("ReadingScore", ByTypes.id).getText();
		testResultService.assertEquals(expectedReadingScore, actualReadingScore,"Reading score wasn't as expected");
		testResultService.assertEquals(expectedListeningScore, actualListeningScore,"Listening score wasn't as expected");
	}
	
	public boolean checkViewFullReportDisplayed(String testSequence) throws Exception {
		try {
			//WebElement viewFullReport = webDriver.waitForElement("//div[contains(@class,'assessments__main')]/div[3]//table[contains(@class,'assessments__table')]//tr["+ testSequence+ "]//div[contains(@class,'viewFullReport')]", ByTypes.xpath);
			List<WebElement> viewFullReportButtons = webDriver.getWebDriver().findElements(By.xpath("//div[contains(@class,'assessments__viewFullReport')]/a"));
			webDriver.scrollToElement(viewFullReportButtons.get(Integer.parseInt(testSequence)-1));
			boolean isDisplayed = webDriver.isDisplayed(viewFullReportButtons.get(Integer.parseInt(testSequence)-1));
			testResultService.assertEquals(true, isDisplayed);
			return isDisplayed;
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean checkViewResultsDisplayed(String testSequence) throws Exception {
		try {
			//WebElement viewResults = webDriver.waitForElement("//div[contains(@class,'assessments__main')]/div[3]//table[contains(@class,'assessments__table')]//tr["+ testSequence+ "]//div[contains(@class,'viewResults')]", ByTypes.xpath);
			List<WebElement> viewResultsButtons = webDriver.getWebDriver().findElements(By.xpath("//div[contains(@class,'assessments__viewResults')]/a"));
			webDriver.scrollToElement(viewResultsButtons.get(Integer.parseInt(testSequence)-1));
			boolean isDisplayed = webDriver.isDisplayed(viewResultsButtons.get(Integer.parseInt(testSequence)-1));
			testResultService.assertEquals(true, isDisplayed);
			return isDisplayed;
		} catch (Exception e) {
			return false;
		}
	}
	
	public void clickViewFullReport(int testSequence) throws Exception {
		//List<WebElement> viewFullReportButtons = webDriver.getWebDriver().findElements(By.xpath("//div[contains(@class,'assessments__viewFullReport')]/a"));
		//webDriver.ClickElement(viewFullReportButtons.get(testSequence-1));
		//webDriver.waitForElementAndClick("//*[@id='courseId_']/td[3]/div[1]/a", ByTypes.xpath, 10);
		webDriver.waitForElementAndClick(".assessments__viewFullReport.ng-scope a", ByTypes.cssSelector, 10);
	}
	
	public void clickViewResults(int testSequence) throws Exception {
		List<WebElement> viewResultsButtons = webDriver.getWebDriver().findElements(By.xpath("//div[contains(@class,'assessments__viewResults')]/a"));
		//*[@id='courseId_']/td[3]/div[2]/a
		webDriver.ClickElement(viewResultsButtons.get(testSequence-1));
		
	}
	
	public void checkScoreLevelPLT (String testSequence , String expectedScore) throws Exception {
		try {
			String actualScore;
			WebElement score = webDriver.waitForElement("//div[contains(@class,'assessments__main')]/div[3]//table[contains(@class,'assessments__table')]//tr["+ testSequence+ "]//td[contains(@class,'testScore')]/span", ByTypes.xpath, false, 5);
			actualScore = score.getText();
			if (!expectedScore.contains(" ")) {
				actualScore = actualScore.replace(" ","");
			}
			testResultService.assertEquals(expectedScore, actualScore,"Verifying Score in Test Results");
		} catch (Exception e) {
			testResultService.addFailTest("Plt score is not displayed", false, true);
		}
	}
	
	
	public void checkPLTNotDisplayedInSectionByTestName(boolean expectedVisibility) throws Exception {

		WebElement testName = webDriver.waitForElement("//div[contains(@class,'assessments__main')]/div[1]//table[contains(@class,'assessments__table')]//tr[1]//td[contains(@class,'assessments__testName')]", ByTypes.xpath, 2, false);
		if (expectedVisibility && testName!=null) {
			
			testResultService.assertEquals(PLT, testName.getText(), "PLT name is not valid");
			
		} else if (!expectedVisibility && testName!=null) {
			if (testName.getText().equals(PLT)) testResultService.addFailTest("PLT displayed though it should not");
		}		
		
	}
	
	public boolean isCourseTestAvailable(String courseTestName) throws Exception {

		webDriver.waitUntilElementAppears("assessments__testName", ByTypes.className, 3);
		
		List<WebElement> tests = webDriver.waitForElement("//div[contains(@class,'availableTests')]", ByTypes.xpath, 3, false).findElements(By.className("assessments__testName"));
		boolean isAvailable = false;
		
		for (int i = 0; i < tests.size(); i++) {
			if (tests.get(i).getText().equalsIgnoreCase(courseTestName)) { 
				isAvailable = true;
				break;
			}
		}
		
		return isAvailable;	
	}
	
	public void checkLockedIconAssessment(boolean expected) throws Exception {
		
		boolean elementExists = false;
		WebElement LockExists = webDriver.waitForElement("//*[@id='mainCtrl']/div[3]/div/div/div[2]/div[2]/div/div/div/div[2]/table/tbody/tr[1]/td[3]/ed-lock-icon/svg", ByTypes.xpath,5, false);	
		

		
		if (LockExists != null) {
			elementExists = true;
		}
		else
			elementExists = false;
		
		
		testResultService.assertEquals(expected, elementExists, "Wrong Assessment Lock icon display");
		
	}
	
	public String clickStartTest(int sectionNum, int testSequence) throws Exception {
		WebElement testName = webDriver.waitForElement(
				"//div[contains(@class,'assessments__main')]/div["
						+ sectionNum
						+ "]//table[contains(@class,'assessments__table')]//tr["
						+ testSequence
						+ "]//td[contains(@class,'assessments__testName')]", ByTypes.xpath,false,5);
				
		String actualTestName = testName.getText();
		ArrayList<WebElement> startTestButtons = (ArrayList<WebElement>) webDriver.getWebDriver().findElements(By.xpath("//td[contains(@class,\"assessments__testlink\")]/a"));
		startTestButtons.get(testSequence-1).click();
		return actualTestName;
	}
	
	public String clickStartTest(int sectionNum, int testSequence,boolean click) throws Exception {
		WebElement testName = webDriver.waitForElement(
				"//div[contains(@class,'assessments__main')]/div["
						+ sectionNum
						+ "]//table[contains(@class,'assessments__table')]//tr["
						+ testSequence
						+ "]//td[contains(@class,'assessments__testName')]", ByTypes.xpath,false,5);
				
		String actualTestName = testName.getText();
				
		if (click){
			ArrayList<WebElement> startTestButtons = (ArrayList<WebElement>) webDriver.getWebDriver().findElements(By.xpath("//td[contains(@class,\"assessments__testlink\")]/a"));
			startTestButtons.get(testSequence-1).click();
		}
			
		return actualTestName;
	}
	
	public boolean closeErrorPopUpIfItsDisplayed() throws Exception {
		try {
			String popMsg = webDriver.getPopUpTextIfExists();
			if (popMsg==null) {
				return false;
			} else {
				testResultService.addFailTest("Error Message Is Displyed: " + popMsg, true, true);
				webDriver.closeAlertByAccept();
				return true;
			}	
		} catch(Exception e) {
			return false;
		}
		
		/*boolean isDisplayed = false;
		WebElement popup = webDriver.waitForElement("//div[@class='modal-content']", ByTypes.xpath,false, 3);
		if (popup != null) {
			isDisplayed = true;
			webDriver.waitForElement("btnOk", ByTypes.id).click();
		}
		return isDisplayed;*/
	}
	
	public void checkScoreForToeicTestIsDisplayed() throws Exception {
		
		ArrayList<WebElement> scoresCanvas = (ArrayList<WebElement>) webDriver.getWebDriver().findElements(By.id("testScoreRangeCanvas"));
		// check the list contains two elements- one for reading scores and one for listening
		testResultService.assertEquals(2, scoresCanvas.size(), "The Scores Elements are not displayed");
	}

	public boolean checkIfTestIsDisplayedInAvailableTests(String testName) throws Exception {

		List<WebElement> tests = null;
		for (int i=0;i<=10 && tests==null;i++){
			tests = webDriver.getWebDriver().findElements(By.xpath("//div[contains(@class,'assessments__section availableTests')]//tbody/tr/td[2]"));
			webDriver.sleep(500);
		}

		boolean isDisplayed = false;
		for (int i = 0; i <tests.size();i++) {
			if (tests.get(i).getText().contains(testName)) {
				isDisplayed = true; 
				break;
			}
		}

		if(!isDisplayed&&testName.equalsIgnoreCase("TOEIC Bridge™ test")) {
			webDriver.scrollToElement(tests.get(tests.size()-1));
			WebElement toeicBridgeTets = webDriver.waitForElement("//td[@class='assessments__testName ng-scope']/span[text()='TOEIC Bridge™ test']", ByTypes.xpath);
			if(toeicBridgeTets.getText().equalsIgnoreCase(testName)) {
				isDisplayed = true; 
			}
		}
		return isDisplayed;
	}
	
	public boolean checkIfTestIsDisplayedInUpcomingTests(String testName) {
		List<WebElement> tests = webDriver.getWebDriver().findElements(By.xpath("//div[contains(@class,'assessments__section upComingTests')]//tbody/tr/td[2]"));
		boolean isDisplayed = false;
		for (int i = 0; i < tests.size();i++) {
			if (tests.get(i).getText().contains(testName)) {
				isDisplayed = true; 
				break;
			} 
		}
		return isDisplayed;
	}
	
	public void validateButtonTextAndClickIt(int testSequence, String expectedButtoName) throws Exception{
		ArrayList<WebElement> startTestButtons = (ArrayList<WebElement>) webDriver.getWebDriver().findElements(By.xpath("//td[contains(@class,\"assessments__testlink\")]/a"));
		testResultService.assertEquals(true, startTestButtons.get(testSequence-1).getText().contains(expectedButtoName),"Resume Test Button is not Displayed.");
		startTestButtons.get(testSequence-1).click();
	}
	
	public String[] getTestsListInAvailableTestsSection() {
		List<WebElement> tests = webDriver.getWebDriver().findElements(By.xpath("//div[contains(@class,'assessments__section availableTests')]//tbody/tr/td[2]"));
		String[] testsNames = new String[tests.size()];
		for (int i = 0; i <tests.size(); i++) {
			testsNames[i] = tests.get(i).getText();			
		}
		return testsNames;
	}
	
	public void waitUntilPLTNewTE_LevelSelectiionLoaded() {
		WebElement element = webDriver.waitUntilElementAppearsAndReturnElementByType("learning__TestLevelLang", ByTypes.className, 10);
		
		if (element==null)
			try {
				testResultService.addFailTest("PLT Level Selection page not loaded", false, true);
			} catch (Exception e) {
				e.printStackTrace();
			}	
	}

	public String checkScoresInEDExcellenceByTest(String expectedScore) throws Exception {
		String actualScore = "";
		try {
			WebElement score = webDriver.waitForElement("//div[@class='assessments__testScoreData ng-binding ng-scope']", ByTypes.xpath, false, 5);
			if (score != null) {																											                                            									
				actualScore = score.getText();
				testResultService.assertEquals(expectedScore, actualScore,"Verifying Score in Test Results");
			} else {
				testResultService.addFailTest("Test was not found in test result section", false, true);
			}
		} catch (Exception e) {
			testResultService.addFailTest("Test was not found in test result section", false, true);
		}
		return actualScore;
	}

	public String clickStartTestByName(String wantedTest) throws Exception {
		
	report.startStep("Wait till section will be open");
		webDriver.waitForElement("//table[@data-ng-show='showAvailablePracticeTestsTable']//th[@class='assessments__testName ng-binding'][normalize-space()='Name']", ByTypes.xpath);
	
	report.startStep("Found all test-names");
		List<WebElement> testNames = webDriver.getElementsByXpath("//table[@data-ng-show='showAvailablePracticeTestsTable']//td[@class='assessments__testName ng-scope']");
		List<WebElement> startButtons = webDriver.getElementsByXpath("//table[@data-ng-show='showAvailablePracticeTestsTable']//a[@class='layout__btnAction layout__btnActionSmall ng-binding ng-scope']");
		webDriver.scrollToElement(testNames.get(7));
	
	report.startStep("Find wanted test");
		WebElement test = testNames.stream().filter(t->t.getText().equals(wantedTest)).findAny().orElse(null);
		if(test==null) {
			testResultService.addFailTest("Test "+wantedTest+", not found", true, true);
		}
		
	report.startStep("Click on start-test");
		String startedTest = test.getText();
		int orderNumber = testNames.indexOf(test);
		startButtons.get(orderNumber).click();
		return startedTest;
	}

	public DigitalCertificatePage clickOnViewResultsOfCertificationTest() throws Exception {

		DigitalCertificatePage certificatePage = null;
		try {

			//WebElement testResults = webDriver.waitForElement(".assessments__testScorelink", ByTypes.cssSelector);
			//WebElement testR = webDriver.waitForElement("//*[text()='Bridge Listening and Reading Test']", ByTypes.xpath);
			List<WebElement> arrows = webDriver.getWebDriver().findElements(By.xpath("//div[contains(@class,'layout__push assessments__titleBtn assessments__titleBtn')]"));
			webDriver.scrollToElement(arrows.get(arrows.size()-1));

			List<WebElement> testNumbers = webDriver.getWebDriver().findElements(By.className("assessments__testNumber"));
			webDriver.scrollToElement(testNumbers.get(testNumbers.size()-1));

			List<WebElement> toeicBridgeTest = webDriver.getWebDriver().findElements(By.xpath("//*[contains(text(),'Listening and Reading Test')]"));
			//WebElement toeicBridgeTest = webDriver.waitForElement("//*[contains(text(),'Listening and Reading Test')]", ByTypes.xpath); //td[@class='assessments__testName']/span[contains(text(),'Listening and Reading Test')]
			Assert.notNull(toeicBridgeTest, "Missed test or incorrect test name");   //Listening and Reading Test
			WebElement testResults = webDriver.waitForElement("//div[@class='assessments__testScorelink']/a", ByTypes.xpath, true,5);
			testResults.click();
			certificatePage = new DigitalCertificatePage(webDriver, testResultService);
			webDriver.switchToNewWindow();
			certificatePage.waitCertificateDownloaded();

		}catch (Exception | AssertionError e){
			testResultService.addFailTest("No link to certification test result",true,true);
		}
		return certificatePage;
	}

	public int getNumberOfWantedSection(String wantedSection) {
		//webDriver.getElementsByLocator(".layout__pull.assessments__titleText.ng-binding",ByTypes.cssSelector);
		List<WebElement> sections = webDriver.getWebDriver().findElements(By.cssSelector(".layout__pull.assessments__titleText.ng-binding"));
		for (WebElement el:sections){
			if(el.getText().trim().equalsIgnoreCase(wantedSection)){
				return (sections.indexOf(el))+1;
			}
		}
		//webDriver.findElementByXpath(".layout__pull.assessments__titleText.ng-binding")
		return -1;
	}
}
