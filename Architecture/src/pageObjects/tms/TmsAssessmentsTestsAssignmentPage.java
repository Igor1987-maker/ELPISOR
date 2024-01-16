package pageObjects.tms;

import Enums.ByTypes;
import drivers.GenericWebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import pageObjects.GenericPage;
import services.PageHelperService;
import services.TestResultService;

import java.util.ArrayList;
import java.util.List;

public class TmsAssessmentsTestsAssignmentPage extends GenericPage {
	
	PageHelperService pageHelper = new PageHelperService();
	
	@FindBy(xpath="//tbody[@name='tblBody']//tr//td[2]")
	public List<WebElement> usersFirstNames;
	
	@FindBy(xpath="//tbody[@name='tblBody']//tr//td[3]")
	public List<WebElement> usersLastNames;
	
	@FindBy(xpath="//tbody[@name='tblBody']//tr//td[4]")
	public List<WebElement> usersNames;
	
	@FindBy(xpath="//tbody[@name='tblBody']//tr//td[1]")
	public List<WebElement> usersCheckboxes;
	
	@FindBy(xpath="//tbody[@name='tblBody']//tr//td[1]//input")
	public List<WebElement> usersCheckboxesInput;
	
	@FindBy(xpath="//td[@class='resultRoot']//tr//td[2]")
	public List<WebElement> testLevelsNames;
	
	@FindBy(xpath="//td[@class='resultRoot']//tr//td[1]//input")
	public List<WebElement> testLevelsCheckBoxes;
	
	@FindBy(xpath="//td[@class='resultRoot']//tr//td[1]//input")
	public List<WebElement> testLevelsCheckBoxesInput;

	@FindBy(xpath="//tbody[@name='tblBody']//tr//td[8]//span[1]")
	public List<WebElement> usersStatuses;
	
	@FindBy(xpath="//tbody[@name='tblBody']//tr//td[7]")
	public List<WebElement> usersStatusesTablePLT;
	
	@FindBy(xpath="//tbody[@name='tblBody']//tr//td[8]/span[1]")
	public List<WebElement> usersStatusesTableMidTerm;
	
	@FindBy(xpath="//input[contains(@id,'iCalendar')]")
	public List<WebElement> startAndEndDate;
	
	@FindBy(xpath="//input[@id='hour']")
	public WebElement startHour;
	
	@FindBy(xpath="//input[@id='minute']")
	public WebElement startMinute;
	
	@FindBy(xpath="//input[@id='Ehour']")
	public WebElement endHour;
	
	@FindBy(xpath="//input[@id='Eminute']")
	public WebElement endMinute;
	
	@FindBy(id="Tlen")
	public WebElement testLength;
	
	@FindBy(id="testAdministration__timer")
	public WebElement testLengthTE;
	
	@FindBy(xpath="//td[@name='selectionObj']")
	public WebElement selectStudentsOptionsCursor;
	
	@FindBy(xpath="//td[@id='scopeAll']")
	public WebElement selectAllStudentsInScopeOption;
	
	@FindBy(id="totalPageNum")
	public WebElement totalPageNumInTable;
	
	@FindBy(id="nextTd")
	public WebElement nextPageInTableButton;
	
	@FindBy(id="backTd")
	public WebElement backPageInTableButton;
	
	@FindBy(xpath="//tbody[@name='tblBody']//tr//td[5]")
	public List<WebElement> testsNamesTable;
	
	@FindBy(xpath="//tbody[@name='tblBody']//tr//td[6]")
	public List<WebElement> startDatesTable;
	
	@FindBy(xpath="//tbody[@name='tblBody']//tr//td[7]")
	public List<WebElement> endDatesTable;
	
	@FindBy(xpath="//div[@class='confirm__text']")
	public WebElement conflictsMessage;
	
	@FindBy(xpath="//div[@class='confirm__action confirm__actionOk']")
	public WebElement checkConflictsButton;
	
	@FindBy(xpath="//div[@class='confirm__action confirm__actionCancel']")
	public WebElement saveAnywayButton;
	
	@FindBy(xpath="//table[@id='refy']//tr[1]//td[@id='refx']//a[1]")
	public WebElement byClassButtonRight;
	
	@FindBy(xpath="//table[@id='refy']//tr[1]//td[@id='refx']//a[2]")
	public WebElement byInstitutionButtonRight;
	
	@FindBy(xpath="//input[@id='Days']")
	public WebElement daysInstitutionSettings;
	
	@FindBy(xpath="//tbody//td[@id='tblFName']")
	public WebElement firstNameColumnHeader;
	
	@FindBy(xpath="//tbody//td[@id='tblLName']")
	public WebElement lastNameColumnHeader;
	
	@FindBy(xpath="//tbody//td[@id='tblUN']")
	public WebElement userNameColumnHeader;
	
	@FindBy(xpath="//tbody//td[@id='tblTN']")
	public WebElement testNameColumnHeader;
	
	@FindBy(xpath="//tbody//td[@id='tblETS']")
	public WebElement startDateColumnHeader;
	
	@FindBy(xpath="//tbody//td[@id='tblETE']")
	public WebElement endDateColumnHeader;
	
	@FindBy(xpath="//tbody//td[@id='tblTRT']")
	public WebElement statusColumnHeader;
	
	@FindBy(xpath="//td[@name='selectionObj']")
	public WebElement arrowOfSelectionOptionsTable;
	
	@FindBy(xpath="//td[@id='pageAll']")
	public WebElement selectAllInPageButton;
	
	@FindBy(xpath="//td[@id='pageClear']")
	public WebElement clearAllInPageButton;
	
	@FindBy(xpath="//td[@id='scopeAll']")
	public WebElement selectAllInScopeButton;
	
	@FindBy(xpath="//td[@id='scopeClear']")
	public WebElement clearAllInScopeButton;
	
	//@FindBy(className = "assessmentsConflictLegend__row assessmentsConflictLegend__caption")// //div[@class='assessmentsConflictLegend__main']
	//public WebElement conflictsLegend;
	
	@FindBy(xpath = "//div[@class='assessmentsConflictLegend__main']")// 
	public WebElement conflictsLegend;
	
	@FindBy(xpath="/html/body/div[2]/div[2]/div/div/p")
	public WebElement pltClassesList;

	@FindBy(xpath = "/html/body/div[2]/div[2]/div/div/div/div/p[1]")
	public WebElement okClass_btn;
	
	@FindBy(xpath="/html/body/div[2]/div[4]/div/div/p")
	public WebElement pltStudentsList;
	
	@FindBy(xpath = "/html/body/div[2]/div[3]/div/div/div/div/p[1]")
	public WebElement okGroup_btn;
		
	@FindBy(xpath="/html/body/div[2]/div[3]/div/div")
	public WebElement pltGroupList;
	
	
	@FindBy(xpath = "/html/body/div[2]/div[4]/div/div/div/div/p[1]")
	public WebElement okStudent_btn;

	@FindBy(name = "Submit222223")//(className = "okButton2")
	public WebElement registrationPageBtn;

	@FindBy(id = "ptDisplay")
	WebElement checkboxAssignPlacementTestsToStudents;
	
	TmsHomePage tmsHomePage = new TmsHomePage(webDriver, testResultService);

	public TmsAssessmentsTestsAssignmentPage(GenericWebDriver webDriver, TestResultService testResultService)
			throws Exception {
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

		
	public void goToAssessmentTestsAssignment() throws Exception{
		report.startStep("Click on Assessment");
		webDriver.switchToTopMostFrame();
		tmsHomePage.switchToMainFrame();
		tmsHomePage.clickOnAssessment();
		Thread.sleep(3000);
		
		webDriver.closeAlertByAccept(2);
		
		report.startStep("Click on Test Assignment");
		//tmsHomePage.clickOnAutomatedTests();
		tmsHomePage.clickOnTestsAssignment();
		Thread.sleep(2000);
	}
	
	public void selectFeatureClassAndUser(String feature, String className, String UserFullName, String groupName) throws Exception {
		webDriver.switchToTopMostFrame();
		tmsHomePage.switchToMainFrame();
		tmsHomePage.switchToFormFrame();
		
		report.startStep("Select Feature");
		tmsHomePage.selectFeature(feature);
		
		report.startStep("Select Class");
		webDriver.selectElementFromComboBox("SelectClass", className);
		//SelectGroup
		if(groupName!=null) {
		report.startStep("SelectGroup");
		webDriver.selectElementFromComboBox("SelectGroup", groupName);
		}
		report.startStep("Select User");
		if(UserFullName!=null) {
		webDriver.selectElementFromComboBox("SelectUser", UserFullName);
		//webDriver.selectElementFromComboBox("SelectTestType", testType);
		}
		report.startStep("Click on Go");
		tmsHomePage.clickOnGo();
		Thread.sleep(2000);
		
		webDriver.switchToTopMostFrame();
		tmsHomePage.switchToMainFrame();
	}
	
	public String clickUserCheckbox(String userName) throws Exception {
		Thread.sleep(2000);
		boolean userFound = false;
		int totalPageNum = getNumberOfPagesInTable();
		String userId="";
		String nameOfUser = "";
		for (int j = 0; j < totalPageNum; j++) {
			
			webDriver.switchToFrame("tableFrame");
			
			if(usersNames.size() == 0) {
				testResultService.addFailTest("Students Table is not Displayed.", true, true);
			}

			List<WebElement> users = null;

			for (int i = 0; i < usersNames.size(); i++) {
				users = usersNames.get(i).findElements(By.tagName("span"));
				nameOfUser = users.get(0).getText();
				if (nameOfUser.equals(userName)) {
					usersCheckboxes.get(i).click();
					userFound = true;
					userId = usersNames.get(i).getAttribute("id");
					break;
				}
			}
			
			if (userFound) {
				webDriver.switchToTopMostFrame();
				tmsHomePage.switchToMainFrame();
				break;
			} else {
				//testResultService.addFailTest("Student "+userName+" was not found", true, true);
			}

			webDriver.switchToTopMostFrame();
			tmsHomePage.switchToMainFrame();
			webDriver.ClickElement(nextPageInTableButton);
		}
		if (userId.equals("") || userId.equals(null)) {
			testResultService.addFailTest("Student "+userName+" was not found", true, true);
			return "";
		} else {
			return userId.substring(2);
		}
	}

	public void selectUserCheckbox() throws Exception {
		webDriver.switchToFrame("tableFrame");
		webDriver.getWebDriver().findElement(By.xpath("//tbody[@name='tblBody']//tr//td[1]")).click();
	}
	
	public void selectTestLevel(String wantedTestLevel) throws Exception{
		//webDriver.waitForElement("//input[@type='radio'][@value='43396']", ByTypes.xpath).click();

		for (int i = 0; i < testLevelsNames.size(); i++) {
			if (testLevelsNames.get(i).getText().contains(wantedTestLevel)) {
				testLevelsCheckBoxes.get(i).click();
			}
		}
	}
	
	public void setCorrectTime() throws Exception{
		setDateAsTodayById("anchorA");
		setDateAsTodayById("anchorB");
		setHourValueForTest("Ehour","23");
		setHourValueForTest("Eminute","59");
	}
	
	private void setDateAsTodayById(String field) throws Exception {
		webDriver.waitForElement(field, ByTypes.id).click();
		webDriver.switchToPopup();
		webDriver.waitForElement("//a[@class='todaylink']", ByTypes.xpath).click();
		webDriver.switchToMainWindow();
		tmsHomePage.switchToMainFrame();
	}
	
	private void setHourValueForTest(String field, String Text) throws Exception {
		
		//webDriver.waitForElement("//*[@id='" +  field + "']", ByTypes.xpath).clear();
		webDriver.waitForElement("//*[@id='" +  field + "']", ByTypes.xpath).sendKeys("\u0008\u0008");
		webDriver.waitForElement("//*[@id='" +  field + "']", ByTypes.xpath).sendKeys(Text);
	}
	
	public void validateAllStudentsCheckboxesAreUnchecked() throws Exception{
		
		int totalPageNum = getNumberOfPagesInTable();
		
		for (int j = 0; j < totalPageNum; j++) {
			
			webDriver.switchToFrame("tableFrame");
			
			if(usersNames.size() == 0) {
				testResultService.addFailTest("Students Table is not Displayed.", true, true);
			}
			
			for (int i = 0; i < usersCheckboxesInput.size(); i++) {
				testResultService.assertEquals(null, usersCheckboxesInput.get(i).getAttribute("checked"),"User Checkbox is Checked for User: " + usersNames.get(i));
			}
			
			webDriver.switchToTopMostFrame();
			tmsHomePage.switchToMainFrame();
			if (j == totalPageNum-1) {
				// return to first page
				for (int k = 0; k < totalPageNum-1; k++) {
					webDriver.ClickElement(backPageInTableButton);	
				}
			} else {
				webDriver.ClickElement(nextPageInTableButton);
			}
		}
	}
	
	public void checkInProgressStudentAreDisabled() throws Exception{
		
		int totalPageNum = getNumberOfPagesInTable();
		
		for (int j = 0; j < totalPageNum; j++) {
			
			webDriver.switchToFrame("tableFrame");
			
			if(usersNames.size() == 0) {
				testResultService.addFailTest("Students Table is not Displayed.", true, true);
			}
			
			for (int i = 0; i < usersStatuses.size(); i++) {
				if (usersStatuses.get(i).getText().equals("In progress")) {
					webDriver.checkElementIsDisabled(usersCheckboxesInput.get(i));
				} else {
					webDriver.checkElementIsNotDisabled(usersCheckboxesInput.get(i));	
				}
			}
			
			webDriver.switchToTopMostFrame();
			tmsHomePage.switchToMainFrame();
			if (j == totalPageNum-1) {
				// return to first page
				for (int k = 0; k < totalPageNum-1; k++) {
					webDriver.ClickElement(backPageInTableButton);	
				}
				waitUntilFirstPageIsReached();
			} else {
				webDriver.ClickElement(nextPageInTableButton);
			}
		}
	}
	
	public void validateStartAndEndDateAreCorrect(String expectedStartDate, String expectedEndDate) throws Exception{
		testResultService.assertEquals(true, startAndEndDate.get(0).getAttribute("value").equals(expectedStartDate), "Start Date is Incorrect.");
		testResultService.assertEquals(true, startAndEndDate.get(1).getAttribute("value").equals(expectedEndDate), "End Date is Incorrect.");
	}
	
	public void validateStartAndEndTimeAreCorrect(String expectedStartHour, String expectedStartMinute, String expectedEndHour, String expectedEndMinute) throws Exception {
		testResultService.assertEquals(true, startHour.getAttribute("value").equals(expectedStartHour), "Start Hour is Incorrect. Expected: " + expectedStartHour);
		testResultService.assertEquals(true, startMinute.getAttribute("value").equals(expectedStartMinute), "Start Minute is Incorrect. Expected: " + expectedStartMinute);
		testResultService.assertEquals(true, endHour.getAttribute("value").equals(expectedEndHour), "End Hour is Incorrect. Expected: " + expectedEndHour);
		testResultService.assertEquals(true, endMinute.getAttribute("value").equals(expectedEndMinute), "End Minute is Incorrect. Expected: " + expectedEndMinute);
	}
	
	public void validateNoCourseIsChosen(){		
		for (int i = 0; i < testLevelsCheckBoxesInput.size(); i++) {
			testResultService.assertEquals(null, testLevelsCheckBoxesInput.get(i).getAttribute("checked"),"Course Checkbox is Checked for Course: " + testLevelsNames.get(i));
		}
	}
	
	public void setDate(String elementId, String day) throws Exception {
		webDriver.waitForElement(elementId, ByTypes.id).click();
		webDriver.switchToPopup();
		List<WebElement> dayElement = webDriver.getWebDriver().findElements(By.xpath("//*[a="+day+"]//a[@class='calthismonth']")); //webDriver.waitForElement("//*[a="+day+"]", ByTypes.xpath,true, 4);
		if (webDriver.isDisplayed(dayElement.get(0))) {
			webDriver.ClickElement(dayElement.get(0));
		}
		webDriver.switchToMainWindow();
		tmsHomePage.switchToMainFrame();
	}
	
	public void setTime(String startDate, String endDate, String startTime, String endTime) throws Exception{
		
		report.startStep("Set Start Date and Time");
		setDate("anchorA", startDate.split("/")[0]);
		setHourValueForTest("hour", startTime.split(":")[0]);
		setHourValueForTest("minute", startTime.split(":")[1]);
		
		report.startStep("Set End Date and Time");
		setDate("anchorB", endDate.split("/")[0]);
		setHourValueForTest("Ehour", endTime.split(":")[0]);
		setHourValueForTest("Eminute", endTime.split(":")[1]);
	}
	
	public void validateMessageContent(String expectedMessage) throws Exception {
		try {
			String actualText = webDriver.getPopUpText().toLowerCase();
			testResultService.assertEquals(true, actualText.contains(expectedMessage.toLowerCase()),"Message is Incorrect. Expected: '"+expectedMessage+"'. Actual: '"+actualText+"'");
			webDriver.closeAlertByAccept();
		} catch(Exception e) {
			testResultService.addFailTest("Pop Up With Message is Not Displayed", false, true);
		}
	}
	
	public void setCourseTestdurationInMinutes(String testDurationInMinutes){
		try {
			
			webDriver.switchToFrame("tableFrame");
			testLengthTE.clear();
			testLengthTE.sendKeys(testDurationInMinutes);
			
			webDriver.switchToTopMostFrame();
		    tmsHomePage.switchToMainFrame();
			//webDriver.waitForElement(testLengthTE,ByTypes.id).sendKeys(testDurationInMinutes);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setOldTMSCourseTestdurationInMinutes(String testDurationInMinutes){
		try {
			
			//webDriver.switchToFrame("tableFrame");
			testLength.clear();
			testLength.sendKeys(testDurationInMinutes);
			
			//webDriver.switchToTopMostFrame();
		    //tmsHomePage.switchToMainFrame();
			//webDriver.waitForElement(testLengthTE,ByTypes.id).sendKeys(testDurationInMinutes);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void goToAssignTestPageAndChooseDetails(String courseName, String className, String UserFullName,String groupName, boolean refresh) throws Exception{
		
		if (refresh) {
			report.startStep("Refresh");
			webDriver.refresh();
		}
		
		report.startStep("Go to Assessments -> Automated Tests");
		goToAssessmentTestsAssignment();
		
		report.startStep("Select Feature & Class & User");	
		selectFeatureClassAndUser(courseName, className, UserFullName,groupName);
	
	}
	
	public void validateTestLength(String instName){
		String testLengthDB = dbService.getTestLengthByInstitutionName(instName);
		testResultService.assertEquals(testLengthDB, testLength.getAttribute("value"), "Test Length is Incorrect");	
	}
	
	public void validateTestLengthTestEnvironment(String testTypeId) {
		try {
			String testLengthDB = dbService.getTestLengthByTestTypeId(testTypeId);
			if (testTypeId.equals("1")) { // placement - new te		
				webDriver.switchToFrame("tableFrame");
				testResultService.assertEquals(testLengthDB, testLengthTE.getAttribute("value"), "Test Length is Incorrect");
				webDriver.switchToTopMostFrame();
			    tmsHomePage.switchToMainFrame();
			} else if (testTypeId.equals("2")) { // mid term - old te 
				testResultService.assertEquals(testLengthDB, testLength.getAttribute("value"), "Test Length is Incorrect");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public void validateOldTMsTestLength(String instName) {
		try {
			//String testLengthDB = dbService.getTestLengthByTestTypeId(testTypeId);
			String testLengthDB = dbService.getTestLengthByInstitutionName(instName);
			testResultService.assertEquals(testLengthDB, testLength.getAttribute("value"), "Test Length is Incorrect");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public void selectFeatureAndClass(String feature, String className) throws Exception {
		webDriver.switchToTopMostFrame();
		tmsHomePage.switchToMainFrame();
		tmsHomePage.switchToFormFrame();
		
		report.startStep("Select Feature");
		tmsHomePage.selectFeature(feature);
		
		report.startStep("Select Class");
		webDriver.selectElementFromComboBox("SelectClass", className);
		
		report.startStep("Click on Go");
		tmsHomePage.clickOnGo();
		Thread.sleep(2000);
		
		webDriver.switchToTopMostFrame();
		tmsHomePage.switchToMainFrame();
	}
	
	public String selectUserWithSpecificStatus(String status, String courseName) throws Exception {
		String userId = "";
		boolean userFound = false;
		int totalPageNum = Integer.parseInt(totalPageNumInTable.getText());
		for (int j = 0; j < totalPageNum; j++) {
			
			webDriver.switchToFrame("tableFrame");
	
			for (int i = 0; i < usersNames.size(); i++) {
				if (usersStatuses.get(i).getText().equals(status) && testsNamesTable.get(i).getText().contains(courseName)) {
					usersCheckboxes.get(i).click();
					userFound = true;
					userId = usersNames.get(i).getAttribute("id");
					break;
				}
			}
			
			if (userFound) {
				webDriver.switchToTopMostFrame();
				tmsHomePage.switchToMainFrame();
				break;
			}
			
			webDriver.switchToTopMostFrame();
			tmsHomePage.switchToMainFrame();
			webDriver.ClickElement(nextPageInTableButton);
		}
		
		if (userId.equals("") || userId.equals(null)) {
			return "";
		} else {
			return userId.substring(2);
		}
	}
	
	public String assignTestToStudentInSpecificStatus(String status, String startDate, String endDate, String startTime, String endTime, String courseName) throws Exception{
		report.startStep("Select Student in Status: " + status);
		String courseNameTable = null;
		if (status.equals("Unassigned")) {
			courseNameTable = "";
		} else {
			courseNameTable = courseName;
		}
		String userId = selectUserWithSpecificStatus(status, courseNameTable);
		
		report.startStep("Set Correct Time");
		setTime(startDate, endDate, startTime, endTime);
		
		report.startStep("Set Test Level");
		selectTestLevel(courseName);
		
		report.startStep("Click Save");
		tmsHomePage.clickOnSave();
		return userId;
	}
	
	public void validateTestIsAssignedInTMS(String userName, String testName, String startDate, String endDate) throws Exception{
		int index = 0;
			
		webDriver.switchToFrame("tableFrame");

		for (int i = 0; i < usersNames.size(); i++) {
			if (usersNames.get(i).getAttribute("title").equals(userName)) {
				index = i;
				break;
			}
		}
		
		testResultService.assertEquals(testName.trim(), testsNamesTable.get(index).getAttribute("title").trim(), "Test Name of Student: "+userName+" is Incorrect.", true);
		testResultService.assertEquals(true, startDatesTable.get(index).getAttribute("title").contains(startDate), "Start Date of Student: "+userName+" is Incorrect. Expected: " +startDate+ ". Actual: " +startDatesTable.get(index).getAttribute("title"), true);
		testResultService.assertEquals(true, endDatesTable.get(index).getAttribute("title").contains(endDate), "End Date of Student: "+userName+" is Incorrect. Expected: " +endDate+ ". Actual: " +endDatesTable.get(index).getAttribute("title"), true);
		testResultService.assertEquals("Assigned", usersStatuses.get(index).getText(), "Status of Student: "+userName+" is Incorrect.", true);
	
		webDriver.switchToTopMostFrame();
		tmsHomePage.switchToMainFrame();
	}
	
	public boolean validateNonPopupMessageContent(String expectedMessage) throws Exception {
		try {
			WebElement iframe = webDriver.waitForElement("//iframe[contains(@src,'Conflicts')]", ByTypes.xpath, false, 5);
			if (iframe == null) {
				return false;
			} else {
				webDriver.switchToFrame(iframe);
				testResultService.assertEquals(true, conflictsMessage.getText().contains(expectedMessage),"Message is Incorrect.");
				webDriver.switchToTopMostFrame();
				tmsHomePage.switchToMainFrame();
				return true;
			}
		} catch(Exception e) {
			testResultService.addFailTest("Pop Up With Message is Not Displayed", false, true);
			return false;
		}
	}
	
	public void clickCheckConflictsButton() throws Exception{
		WebElement iframe = webDriver.waitForElement("//iframe[contains(@src,'Conflicts')]", ByTypes.xpath);
		webDriver.switchToFrame(iframe);
		webDriver.ClickElement(checkConflictsButton);
		webDriver.switchToTopMostFrame();
		tmsHomePage.switchToMainFrame();
	}

	public void clickSaveAnyWayButton() throws Exception{
		WebElement iframe = webDriver.waitForElement("//iframe[contains(@src,'Conflicts')]", ByTypes.xpath);
		webDriver.switchToFrame(iframe);
		webDriver.ClickElement(saveAnywayButton);
		webDriver.switchToTopMostFrame();
		tmsHomePage.switchToMainFrame();
	}

	public String getLocationOfConflictByUser(String userId) throws Exception{
		// Get iframe Location
		WebElement iframe = webDriver.waitForElement("tableFrame", ByTypes.id);
		Point iframeLocation = iframe.getLocation();
		
		// Set x, y 
		int x = iframeLocation.getX();
		int y = iframeLocation.getY();
		
		Point conflictLocation = null;
		
		int totalPageNum = getNumberOfPagesInTable();
		
		for (int j = 0; j < totalPageNum; j++) {
			
			webDriver.switchToFrame("tableFrame");
			
			if (usersNames.size() == 0) {
				testResultService.addFailTest("Students Table is not Displayed.", true, true);
			}
			
			// Get Conflict Location
			WebElement wantedUser = webDriver.waitForElement("tr" +userId, ByTypes.id, false, 5);
			if (wantedUser != null) {
				conflictLocation = wantedUser.getLocation();
				break;
			}
			
			webDriver.switchToTopMostFrame();
			tmsHomePage.switchToMainFrame();
			if (j == totalPageNum-1) {
				// return to first page
				for (int k = 0; k < totalPageNum-1; k++) {
					webDriver.ClickElement(backPageInTableButton);	
				}
			} else {
				webDriver.ClickElement(nextPageInTableButton);
			}
		}
		
		
		//webDriver.switchToFrame("tableFrame");
		
		// Get Conflict Location
		//Point conflictLocation = webDriver.waitForElement("tr" +userId, ByTypes.id).getLocation();
		
		// Update x, y
		x += conflictLocation.getX();
		y += conflictLocation.getY();
		
		// Move x to ::before element
		x -= 20;
		
		webDriver.switchToTopMostFrame();
		tmsHomePage.switchToMainFrame();
		
		return x+","+y;
	}
	
	public int getNumberOfPagesInTable() throws Exception{
		String tempNumOfPages;
		int totalPageNum = 0;
		if (webDriver.isDisplayed(totalPageNumInTable)) {
			tempNumOfPages = totalPageNumInTable.getText();
			if (tempNumOfPages.equals("")) {
				testResultService.addFailTest("Students Table is not Displayed.", true, true);
			} else {
				totalPageNum = Integer.parseInt(totalPageNumInTable.getText());
			}	
		} else {
			testResultService.addFailTest("Students Table is not Displayed.", true, true);
		}
		return totalPageNum;
	}
	
	public int getDaysInInstitutionSettings(String testType) throws Exception{
			
		tmsHomePage.clickOnTestsConfiguration();
		webDriver.closeAlertByAccept();
		
		webDriver.switchToTopMostFrame();
		tmsHomePage.switchToMainFrame();
		tmsHomePage.switchToFormFrame();
		tmsHomePage.selectFeature(testType);
		tmsHomePage.clickOnGo();
		
		webDriver.switchToTopMostFrame();
		tmsHomePage.switchToMainFrame();
	
		String days = daysInstitutionSettings.getAttribute("value");
		
		tmsHomePage.clickOnTestsAssignment();;
		return Integer.parseInt(days);
	}
	
	public void validateNonPopupMessageIsNotDisplayed() throws Exception {
		try {
			WebElement iframe = webDriver.waitForElement("//iframe[contains(@src,'Conflicts')]", ByTypes.xpath, false, 5);
			testResultService.assertEquals(false, webDriver.isDisplayed(iframe));
		} catch(Exception e) {
			testResultService.addFailTest("Pop Up With Message is Displayed", false, true);
		}
	}
	
	public void goToAssignTestPageAndChooseFeatureAndClass(String courseName, String className, boolean refresh) throws Exception{
		
		if (refresh) {
			report.startStep("Refresh");
			webDriver.refresh();
		}
		
		report.startStep("Go to Assessments -> Automated Tests");
		goToAssessmentTestsAssignment();
		
		report.startStep("Select Feature & Class");	
		selectFeatureAndClass(courseName, className);
	}
	
	public void checkInProgressStudentCheckboxesAndValidateNotChecked() throws Exception{
		
		int totalPageNum = getNumberOfPagesInTable();
		
		for (int j = 0; j < totalPageNum; j++) {
			
			webDriver.switchToFrame("tableFrame");
			
			if(usersNames.size() == 0) {
				testResultService.addFailTest("Students Table is not Displayed.", true, true);
			}
			
			for (int i = 0; i < usersStatuses.size(); i++) {
				if (usersStatuses.get(i).getText().equals("In progress")) {
					webDriver.ClickElement(usersCheckboxes.get(i));
					validateCheckBoxAtIndexIsNotChecked(i);
				}
			}
			
			webDriver.switchToTopMostFrame();
			tmsHomePage.switchToMainFrame();
			if (j == totalPageNum-1) {
				uncheckAllCheckedStudentsInPage();
				// return to first page
				for (int k = 0; k < totalPageNum-1; k++) {
					uncheckAllCheckedStudentsInPage();
					webDriver.ClickElement(backPageInTableButton);	
				}
				waitUntilFirstPageIsReached();
			} else {
				webDriver.ClickElement(nextPageInTableButton);
			}
		}
	}
	
	public void validateCheckBoxAtIndexIsNotChecked(int index) throws Exception{
		// i need to check what is the requirement (should the checkbox be checked and greyed or only greyed?)
		// if checked and greyed this is the relevant code:
		/*String isChecked = usersCheckboxesInput.get(index).getAttribute("checked");
		String isDisabled = usersCheckboxesInput.get(index).getAttribute("disabled");
		if (isChecked.equals("true") && isDisabled.equals("true")) {
			testResultService.assertEquals("true", usersCheckboxesInput.get(index).getAttribute("checked"),"Checkbox at Row: " +(index+1)+ " is Checked", true);
			testResultService.assertEquals("true", usersCheckboxesInput.get(index).getAttribute("disabled"),"Checkbox at Row: " +(index+1)+ " is Checked", true);	
		} else {
			testResultService.addFailTest("Checkbox is not checked or not disabled", false, true);
		}*/
		
		// if only greyed- this is the relevant code:
		//testResultService.assertEquals(true, usersCheckboxesInput.get(index).getAttribute("checked")==null,"Checkbox at Row: " +(index+1)+ " is Checked", true);
		//testResultService.assertEquals("true", usersCheckboxesInput.get(index).getAttribute("disabled"),"Checkbox at Row: " +(index+1)+ " is Checked", true);
		testResultService.assertEquals("true", usersCheckboxesInput.get(index).getAttribute("disabled"),"Checkbox at Row: " +(index+1)+ " is Checked", true);
		
	}
	
	public void validateCheckBoxAtIndexIsChecked(int index){
		testResultService.assertEquals("true", usersCheckboxesInput.get(index).getAttribute("checked"),"Checkbox at Row: " +(index+1)+ " is Not Checked", true);
	}
	
	public void uncheckAllCheckedStudentsInPage() throws Exception{
		webDriver.switchToFrame("tableFrame");
		String tempAttribute = "";
		for (int i = 0; i < usersCheckboxesInput.size(); i++) {
			tempAttribute = usersCheckboxesInput.get(i).getAttribute("checked");
			if (tempAttribute == null) {
				continue;
			} else if (tempAttribute.equals("true")) {
				webDriver.ClickElement(usersCheckboxes.get(i));
			}
		}
		webDriver.switchToTopMostFrame();
		tmsHomePage.switchToMainFrame();
	}
	
	public void validateInProgressStudentAreNotSelected() throws Exception{
		
		int totalPageNum = getNumberOfPagesInTable();
		
		for (int j = 0; j < totalPageNum; j++) {
			
			webDriver.switchToFrame("tableFrame");
			
			if(usersNames.size() == 0) {
				testResultService.addFailTest("Students Table is not Displayed.", true, true);
			}
			
			for (int i = 0; i < usersStatuses.size(); i++) {
				if (usersStatuses.get(i).getText().equals("In progress")) {
					validateCheckBoxAtIndexIsNotChecked(i);
				} else {
					validateCheckBoxAtIndexIsChecked(i);
				}
			}
			
			webDriver.switchToTopMostFrame();
			tmsHomePage.switchToMainFrame();
			if (j == totalPageNum-1) {
				// return to first page
				for (int k = 0; k < totalPageNum-1; k++) {
					webDriver.ClickElement(backPageInTableButton);	
				}
				waitUntilFirstPageIsReached();
			} else {
				webDriver.ClickElement(nextPageInTableButton);
			}
		}
	}
	
	public ArrayList<String> getListFromPltStatusReportTableByCoulmn(String column) throws Exception {
		webDriver.switchToFrame("PLTStatusReport");
		
		column = column.toLowerCase();
		List<String> columnList = new ArrayList<String>();
		List<WebElement> list = new ArrayList<WebElement>();
		String attribute = "";
		switch(column){
			case "first name":
				list = usersFirstNames;
				attribute = "title";
				break;
			case "last name":
				list = usersLastNames;
				attribute = "title";
				break;
			case "user name":
				list = usersNames;
				attribute = "title";//"textContent";
				break;
			case "test name":
				list = testsNamesTable;
				attribute = "title";
				break;
			case "start date":
				list = startDatesTable;
				attribute = "title";
				break;
			case "end date":
				list = endDatesTable;
				attribute = "title";
				break;
			case "status":
				list = usersStatusesTablePLT;
				attribute = "title";	
				break;
		}
		
		int totalPageNum = getNumberOfPagesInTable();
		
		for (int j = 0; j < totalPageNum; j++) {
			
			webDriver.switchToFrame("tableFrame");
			
			if(usersNames.size() == 0) {
				testResultService.addFailTest("Students Table is not Displayed.", true, true);
			}
			
			for (int i = 0; i < list.size(); i++) {
				String temp = list.get(i).getAttribute(attribute);
				if (temp.equals("")) {
					columnList.add(list.get(i).getText());
				} else {
					columnList.add(list.get(i).getAttribute(attribute));
				}
			}
			
			webDriver.switchToTopMostFrame();
			tmsHomePage.switchToMainFrame();
			webDriver.switchToFrame("PLTStatusReport");
			if (j == totalPageNum-1) {
				// return to first page
				for (int k = 0; k < totalPageNum-1; k++) {
					webDriver.ClickElement(backPageInTableButton);	
					Thread.sleep(300);
				}
				waitUntilFirstPageIsReached();
			} else {
				webDriver.ClickElement(nextPageInTableButton);
			}
		}
		
		webDriver.switchToTopMostFrame();
		tmsHomePage.switchToMainFrame();
		
		/*webDriver.switchToFrame("tableFrame");
		for (int i = 0; i < list.size(); i++) {
			columnList.add(list.get(i).getAttribute(attribute));
		}
		webDriver.switchToTopMostFrame();
		tmsHomePage.switchToMainFrame();*/
		return (ArrayList<String>) columnList;
	}
	
	public void checkIfTableIsEmpty() throws Exception {
		webDriver.switchToFrame("tableFrame");
		
		if(usersNames.size() == 0) {
			testResultService.addFailTest("Students Table is not Displayed.", true, true);
		}
		
		webDriver.switchToTopMostFrame();
		tmsHomePage.switchToMainFrame();
	}
	
	public void clickOnColumnHeader(String columnName) throws Exception {
		switch(columnName.toLowerCase()){
			case "first name":
				webDriver.ClickElement(firstNameColumnHeader);
				break;
			case "last name":
				webDriver.ClickElement(lastNameColumnHeader);
				break;
			case "user name":
				webDriver.ClickElement(userNameColumnHeader);
				break;
			case "test name":
				webDriver.ClickElement(testNameColumnHeader);
				break;
			case "start date":
				webDriver.ClickElement(startDateColumnHeader);
				break;
			case "end date":
				webDriver.ClickElement(endDateColumnHeader);
				break;
			case "status":
				webDriver.ClickElement(statusColumnHeader);
				break;
		}
		checkIfTableIsEmpty();
	}
	
	public void selectAllStudentsInPage() throws InterruptedException{
		webDriver.ClickElement(selectStudentsOptionsCursor);
		webDriver.ClickElement(selectAllInPageButton);
		Thread.sleep(1000);
	}
	
	public void unselectAllStudentsInPage() throws InterruptedException{
		webDriver.ClickElement(selectStudentsOptionsCursor);
		webDriver.ClickElement(clearAllInPageButton);
		Thread.sleep(1000);
	}
	
	public void selectAllStudentsInScope() throws InterruptedException{
		webDriver.ClickElement(selectStudentsOptionsCursor);
		Thread.sleep(2000);
		webDriver.ClickElement(selectAllStudentsInScopeOption);
		Thread.sleep(1000);
	}
	
	public void unselectAllStudentsInScope() throws InterruptedException{
		webDriver.ClickElement(selectStudentsOptionsCursor);
		webDriver.ClickElement(clearAllInScopeButton);
		Thread.sleep(1000);
	}
	
	public void validateInProgressStudentInCurrentPageAreNotSelected() throws Exception{
		
		webDriver.switchToFrame("tableFrame");
		
		if(usersNames.size() == 0) {
			testResultService.addFailTest("Students Table is not Displayed.", true, true);
		}
		
		for (int i = 0; i < usersStatuses.size(); i++) {
			if (usersStatuses.get(i).getText().equals("In progress")) {
				validateCheckBoxAtIndexIsNotChecked(i);
			} else {
				validateCheckBoxAtIndexIsChecked(i);
			}
		}
		
		webDriver.switchToTopMostFrame();
		tmsHomePage.switchToMainFrame();
	}
	
	public void waitUntilFirstPageIsReached() throws Exception {
		try {
			boolean isActive = false;
			WebElement firstPageElement = webDriver.waitForElement("//td[@id='pagesList']//span[1]", ByTypes.xpath, false, 1);
			isActive = firstPageElement.getAttribute("className").contains("Active");
			int counter = 0;
			while (!isActive && counter < 30) {
				firstPageElement = webDriver.waitForElement("//td[@id='pagesList']//span[1]", ByTypes.xpath, false, 1);
				isActive = firstPageElement.getAttribute("className").contains("Active");
				counter++;
			}
		} catch (Exception e) {
			
		}
	}
	
	public void validateConflictsLegendIsDisplayed() throws Exception {
		testResultService.assertEquals(true, webDriver.isDisplayed(conflictsLegend)," Conflicts Legend is Not Displayed");
	
	}
	
	public String assignTestToStudentWithBasic1MTAssigned(String testLevel, String startDate, String endDate, String startTime, String endTime, String courseName) throws Exception{
		report.startStep("Select Student in Status Assigned and Test: " +testLevel+ " MId-Term");
		String userId = selectUserWithSpecificStatusAndTest("Assigned", testLevel);
		//if (userId.equals("")) {
		//	userId = selectUserWithSpecificStatusAndTest("Assigned", "B1");
		//}
		
		report.startStep("Set Correct Time");
		setTime(startDate, endDate, startTime, endTime);
		
		report.startStep("Set Test Level");
		selectTestLevel(courseName);
		
		report.startStep("Click Save");
		tmsHomePage.clickOnSave();
		return userId;
	}
	
	public String selectUserWithSpecificStatusAndTest(String status, String testLevel) throws Exception {
		String userId = "";
		boolean userFound = false;
		int totalPageNum = Integer.parseInt(totalPageNumInTable.getText());
		for (int j = 0; j < totalPageNum; j++) {
			
			webDriver.switchToFrame("tableFrame");
	
			for (int i = 0; i < usersNames.size(); i++) {
				if (usersStatuses.get(i).getText().equals(status) && testsNamesTable.get(i).getAttribute("title").contains(testLevel)) {
					usersCheckboxes.get(i).click();
					userFound = true;
					userId = usersNames.get(i).getAttribute("id");
					break;
				}
			}
			
			if (userFound) {
				webDriver.switchToTopMostFrame();
				tmsHomePage.switchToMainFrame();
				break;
			}
			
			webDriver.switchToTopMostFrame();
			tmsHomePage.switchToMainFrame();
			webDriver.ClickElement(nextPageInTableButton);
		}
		
		if (userId.equals("") || userId.equals(null)) {
			return "";
		} else {
			return userId.substring(2);
		}
	}
	
	public void selectFeatureAndGo(String feature) throws Exception {
		webDriver.switchToTopMostFrame();
		tmsHomePage.switchToMainFrame();
		tmsHomePage.switchToFormFrame();
		
		report.startStep("Select Feature");
		tmsHomePage.selectFeature(feature);
		
		//report.startStep("Select Class");
		//webDriver.selectElementFromComboBox("SelectClass", className);
		
		report.startStep("Click on Go");
		tmsHomePage.clickOnGo();
		Thread.sleep(2000);
		
		webDriver.switchToTopMostFrame();
		tmsHomePage.switchToMainFrame();
	}
	
	public void selectPltClass(String className) throws Exception {
		
		try {
			webDriver.switchToFrame("tableFrame");
			webDriver.ClickElement(pltClassesList);
			ArrayList<WebElement> dropdownOptions = (ArrayList<WebElement>) webDriver.getWebDriver().findElements(By.className("opt"));
	

			for (int j = 0; j < dropdownOptions.size(); j++) {
				if (dropdownOptions.get(j).getText().equals(className)) {
					dropdownOptions.get(j).click();
					break;
				}
			}
			
			webDriver.ClickElement(okClass_btn);	
		} catch (Exception e) {
			testResultService.addFailTest("FAIL: Choose Classes From DropDown List. Error: " + e);
		}
		
		webDriver.switchToTopMostFrame();
		tmsHomePage.switchToMainFrame();
	}
	
	public void selectPltGroup(String groupName) throws Exception {
		try {
			webDriver.switchToFrame("tableFrame");
			webDriver.ClickElement(pltGroupList);
			ArrayList<WebElement> dropdownOptions = (ArrayList<WebElement>) webDriver.getWebDriver().findElements(By.className("opt"));
	

			for (int j = 0; j < dropdownOptions.size(); j++) {
				if (dropdownOptions.get(j).getText().equals(groupName)) {
					dropdownOptions.get(j).click();
					break;
				}
			}
			
			webDriver.ClickElement(okGroup_btn);	
		} catch (Exception e) {
			testResultService.addFailTest("FAIL: Choose Classes From DropDown List. Error: " + e);
		}
		
		webDriver.switchToTopMostFrame();
		tmsHomePage.switchToMainFrame();
		
		
	}
	
	public void selectPltStudent(String studentName) throws Exception {
		try {
			webDriver.switchToFrame("tableFrame");
			webDriver.ClickElement(pltStudentsList);
			ArrayList<WebElement> dropdownOptions = (ArrayList<WebElement>) webDriver.getWebDriver().findElements(By.className("opt"));
	

			for (int j = 0; j < dropdownOptions.size(); j++) {
				if (dropdownOptions.get(j).getText().contains(studentName)) {
					dropdownOptions.get(j).click();
					break;
				}
			}
			
			webDriver.ClickElement(okStudent_btn);	
		} catch (Exception e) {
			testResultService.addFailTest("FAIL: Choose Classes From DropDown List. Error: " + e);
		}
		
		webDriver.switchToTopMostFrame();
		tmsHomePage.switchToMainFrame();
	}
	
	/*public void selectOptionFromList(String listId, String optionToSelect) {
		try{
		
		WebElement list = webDriver.getWebDriver().findElement(By.xpath("//select[@id='"+listId+"']"));
		webDriver.ClickElement(list);
		List<WebElement> listOptions = webDriver.getWebDriver().findElements(By.xpath("//select[@id='"+listId+"']//option"));
		
		for (int i = 0; i < listOptions.size(); i++) {
			if (listOptions.get(i).getText().equals(optionToSelect)) {
				webDriver.ClickElement(listOptions.get(i));
			}
		}
		}catch(Exception e){}
	}*/

	public void validateTestLengthTestEnvironmentNotDefault(String testTypeId, String expectedTime) {
		
		try {
			if (testTypeId.equals("1")) { // placement - new te
				webDriver.switchToFrame("tableFrame");
				testResultService.assertEquals(expectedTime, testLengthTE.getAttribute("value"), "Test Length is Incorrect");
				webDriver.switchToTopMostFrame();
			    tmsHomePage.switchToMainFrame();			
			} else if (testTypeId.equals("2")) { // mid term - old te 
				testResultService.assertEquals(expectedTime, testLength.getAttribute("value"), "Test Length is Incorrect");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public ArrayList<String> getListFromAssignCourseTestTableByCoulmn(String column) throws Exception {
		
		column = column.toLowerCase();
		List<String> columnList = new ArrayList<String>();
		List<WebElement> list = new ArrayList<WebElement>();
		String attribute = "";
		switch(column){
			case "first name":
				list = usersFirstNames;
				attribute = "title";
				break;
			case "last name":
				list = usersLastNames;
				attribute = "title";
				break;
			case "user name":
				list = usersNames;
				attribute = "title";//"textContent";
				break;
			case "test name":
				list = testsNamesTable;
				attribute = "title";
				break;
			case "start date":
				list = startDatesTable;
				attribute = "title";
				break;
			case "end date":
				list = endDatesTable;
				attribute = "title";
				break;
			case "status":
				list = usersStatusesTableMidTerm;
				attribute = "textContent";	
				break;
		}
		
		int totalPageNum = getNumberOfPagesInTable();
		
		for (int j = 0; j < totalPageNum; j++) {
			
			webDriver.switchToFrame("tableFrame");
			
			if(usersNames.size() == 0) {
				testResultService.addFailTest("Students Table is not Displayed.", true, true);
			}
			
			for (int i = 0; i < list.size(); i++) {
				String temp = list.get(i).getAttribute(attribute);
				if (temp.equals("")) {
					columnList.add(list.get(i).getText());
				} else {
					columnList.add(list.get(i).getAttribute(attribute));
				}
			}
			
			webDriver.switchToTopMostFrame();
			tmsHomePage.switchToMainFrame();
			//webDriver.switchToFrame("PLTStatusReport");
			if (j == totalPageNum-1) {
				// return to first page
				for (int k = 0; k < totalPageNum-1; k++) {
					webDriver.ClickElement(backPageInTableButton);	
					Thread.sleep(300);
				}
				waitUntilFirstPageIsReached();
			} else {
				webDriver.ClickElement(nextPageInTableButton);
			}
		}
		
		webDriver.switchToTopMostFrame();
		tmsHomePage.switchToMainFrame();
		
		/*webDriver.switchToFrame("tableFrame");
		for (int i = 0; i < list.size(); i++) {
			columnList.add(list.get(i).getAttribute(attribute));
		}
		webDriver.switchToTopMostFrame();
		tmsHomePage.switchToMainFrame();*/
		return (ArrayList<String>) columnList;
	}

	public void goToTestConfiguration() throws Exception {
		report.startStep("Click on Assessment");
		webDriver.switchToTopMostFrame();
		tmsHomePage.switchToMainFrame();
		tmsHomePage.clickOnAssessment();
		Thread.sleep(3000);
		
		webDriver.closeAlertByAccept(2);
		
		report.startStep("Click on Test Configuration");
		tmsHomePage.clickOnTestsConfiguration();
		Thread.sleep(2000);
	}

	public void selectTestObjectives(String test, String typeOfTest1, String typeOfTest2, boolean secondIteration) throws Exception {
	
		webDriver.switchToTopMostFrame();
		tmsHomePage.switchToMainFrame();
		tmsHomePage.switchToFormFrame();
		
		webDriver.selectElementFromComboBox("SelectFeature", test);
		tmsHomePage.clickOnGo();
		tmsHomePage.switchToMainFrame();
		webDriver.selectElementFromComboBox("objectiveId1", typeOfTest1);
		webDriver.selectElementFromComboBox("objectiveId2", typeOfTest2);
		webDriver.waitForElement("isProctored2", ByTypes.id).click();;
		if(secondIteration) {
			webDriver.setCheckBoxState(true, "isTeacherAllowed");
		}
		
		report.startStep("Click on Save");
		tmsHomePage.clickOnSave();
		Thread.sleep(2000);
		
	}


	public void selectTypeOfTOEICAssignment(String feature1, String feature2) throws Exception {
		webDriver.switchToTopMostFrame();
		tmsHomePage.switchToMainFrame();
		tmsHomePage.switchToFormFrame();

		report.startStep("Select Features");
			//tmsHomePage.selectFeature(feature1);
			webDriver.selectElementFromComboBox("SelectFeature", feature1);
			Thread.sleep(500);
			webDriver.selectElementFromComboBox("SelectNewSchool", feature2);
			//tmsHomePage.selectFeature(feature2);

	}

	public void clickOnRegistrationPageButton() {
		registrationPageBtn.click();
	}

	public void selectAssignPlacementTestsToStudents(){
		if (!checkboxAssignPlacementTestsToStudents.isSelected()){
			checkboxAssignPlacementTestsToStudents.click();
		}
	}

	public void unSelectAssignPlacementTestsToStudents(){
		if (checkboxAssignPlacementTestsToStudents.isSelected()){
			checkboxAssignPlacementTestsToStudents.click();
		}
	}
}
