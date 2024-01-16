package pageObjects.tms;

import Enums.ByTypes;
import drivers.GenericWebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import pageObjects.GenericPage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static java.lang.Thread.sleep;

public class TmsReportsPage extends GenericPage {

    @FindBy(xpath = "//select[@id='SelectReport']/option")
    public List<WebElement> reportTypes;

    @FindBy(xpath = "//tbody[@id='tblBody']//tr//td[4]//span[2]")
    public List<WebElement> usersNamesTable;

    @FindBy(xpath = "//tbody[@id='tblBody']//tr//td[7]//span[2]")
    public List<WebElement> statusesTable;

    @FindBy(xpath = "//span[@class='pagerLinkActive']")
    public WebElement currentPageTable;

    @FindBy(xpath = "//table[@id='tblRecordsReportGrid']//td[1][@class='tdt']")
    public List<WebElement> studentsNamesInStudentsRecordings;

    @FindBy(id = "jqgh_tblTestsReportGrid_LastUpdateStr")
    public WebElement dataUtcTableFilterButton;

    @FindBy(id = "jqgh_tblTestsReportGrid_Final1LastScore")
    public WebElement lastScoreButton;

    @FindBy(css = "td[aria-describedby='tblTestsReportGrid_Grade']")
    public WebElement scoreColumn;

    @FindBy(css = "td.cursor-pointer")
    WebElement firstNameRow;
    @FindBy(css = ".bg-white")
    List<WebElement> inputClassFiled;
    @FindBy(id = "StatusData")
    public WebElement statusColumn;
    @FindBy(css = "#apply-select-view")
    public WebElement applyFiltering;
    @FindBy(css = ".w-8")
    public WebElement classesCheckbox;
    @FindBy(css = ".w-8")
    public List<WebElement> statusCheckbox;

    @FindBy(css = ".h-10.text-lg")
    public List<WebElement> reportTypeButton;


    public TmsReportsPage(GenericWebDriver webDriver, services.TestResultService testResultService) throws Exception {
        super(webDriver, testResultService);
        // TODO Auto-generated constructor stub
        PageFactory.initElements(webDriver.getWebDriver(), this);
    }

    TmsHomePage tmsHomePage = new TmsHomePage(webDriver, testResultService);

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

    public void goToCourseReports() throws Exception {
        tmsHomePage.switchToMainFrame();
        tmsHomePage.clickOnReports();
        tmsHomePage.clickOnCourseReports();
    }

    public void selectCourseReport(String courseName) throws Exception {
        webDriver.selectElementFromComboBox("SelectReport", courseName);
        sleep(1000);
    }

    public void selectCourseReportAndClassAndCourse(String reportType, String className, String courseId) throws Exception {
        tmsHomePage.switchToFormFrame();

        selectCourseReport(reportType);

        report.startStep("Select Class and press GO");
        tmsHomePage.selectClass(className, false, true);

        report.startStep("Select course");
        String courseNameDb = dbService.getCourseNameById(courseId);
        selectCourseByCourseName(courseNameDb);

        tmsHomePage.switchToCourseTestsFrame();
    }

    public void selectUnitCourseReportAndClassAndCourse(String reportType, String className, String courseId) throws Exception {
        tmsHomePage.switchToFormFrame();
        selectCourseReport(reportType);
        report.startStep("Select Class and press GO");
        tmsHomePage.selectClass(className, false, true);
        report.startStep("Select course");
        String courseNameDb = dbService.getCourseNameById(courseId);
        sleep(1);
        selectCourseByCourseName(courseNameDb);
        sleep(1);
        tmsHomePage.switchToGrandBookReportFrame();
    }

    public void selectCourseReportAndClass(String reportType, String className) throws Exception {
        tmsHomePage.switchToFormFrame();
        selectCourseReport(reportType);
        report.startStep("Select Class and press GO");
        tmsHomePage.selectClass(className, false, true);
        //tmsHomePage.switchToGrandBookReportFrame();
    }


    public void selectCourseByCourseName(String courseName) throws Exception {
        webDriver.selectElementFromComboBox("SelectCourse", courseName, ByTypes.id, false);
    }

    public int getNumberOfPagesInReportsTable() throws Exception {
        String tempNumOfPages;
        int totalPageNum = 0;
        WebElement pagesCount = webDriver.waitForElement("//span[@id='sp_1_divGridPager']", ByTypes.xpath, false, 5);
        if (pagesCount != null) {
            tempNumOfPages = pagesCount.getText();
            if (tempNumOfPages.equals("")) {
                testResultService.addFailTest("Students Table is not Displayed.", true, true);
            } else {
                totalPageNum = Integer.parseInt(pagesCount.getText());
            }
        } else {
            testResultService.addFailTest("Students Table is not Displayed.", true, true);
        }
        return totalPageNum;
    }

    public void selectMatricReportAndClickGo(String reportType, String className) throws Exception {
        tmsHomePage.switchToFormFrame();
        selectCourseReport(reportType);

        report.startStep("Select Class and press GO");
        tmsHomePage.selectClass(className, false, true);

        webDriver.switchToNewWindow();

        //webDriver.selectElementFromComboBox("DDLCources", courseName, ByTypes.id, false);

    }

    public void goToPlacementTestReports() throws Exception {
        tmsHomePage.switchToMainFrame();
        tmsHomePage.clickOnReports();
        tmsHomePage.clickOnPLTReports();
    }

    public void choosePltReportTypeAndClassAndClickGo(String reportType, String className) throws Exception {
        tmsHomePage.switchToFormFrame();
        selectCourseReport(reportType);

        report.startStep("Select Class and press GO");
        tmsHomePage.selectClass(className, false, true);
        //	webDriver.waitForElement("//input[@value='  GO  ']", ByTypes.xpath).click();
        webDriver.switchToTopMostFrame();
        tmsHomePage.switchToMainFrame();
    }

    public void choosePltReportTypeAndClassAndTimeFrameAndClickGo(String reportType, String className, String timePeriod) throws Exception {
        tmsHomePage.switchToFormFrame();
        selectCourseReport(reportType);

        report.startStep("Select Class and press GO");
        tmsHomePage.selectClassAndTimeFrame(className, timePeriod, false, true);
        webDriver.switchToTopMostFrame();
        tmsHomePage.switchToMainFrame();
    }

    public void validateStatusReport(String userId, String expectedStatus, String instId) throws Exception {
        // get user name by user id
        String userName = dbService.getUserNameById(userId, instId);

        report.startStep("Validate user: " + userName + " has status: " + expectedStatus);
        validateStatus(userName, expectedStatus);
    }

    public void validateReportTypeIsNotInReportList(String reportType) throws Exception {
        tmsHomePage.switchToFormFrame();
        boolean isExist = false;
        for (int i = 0; i < reportTypes.size(); i++) {
            if (reportTypes.get(i).getText().equals(reportType)) {
                isExist = true;
                break;
            }
        }

        testResultService.assertEquals(false, isExist, "Report Type: '" + reportType + "' was Found in Reports List");
    }

    public int getUserIndexInTable(String userName) throws Exception {

        webDriver.switchToFrame("PLTStatusReport");

        TmsAssessmentsTestsAssignmentPage tmsAssessmentsTestsAssignmentPage = new TmsAssessmentsTestsAssignmentPage(webDriver, testResultService);
        boolean userFound = false;
        int totalPageNum = tmsAssessmentsTestsAssignmentPage.getNumberOfPagesInTable();
        //String userId="";
        int index = 0;

        for (int j = 0; j < totalPageNum; j++) {

            webDriver.switchToFrame("tableFrame");

            if (usersNamesTable.size() == 0) {
                testResultService.addFailTest("Students Table is not Displayed.", true, true);
            }

            for (int i = 0; i < usersNamesTable.size(); i++) {
                if (usersNamesTable.get(i).getAttribute("textContent").equals(userName)) {
                    index = i;
                    userFound = true;
                    //userId = usersNamesTable.get(i).getAttribute("id");
                    break;
                }
            }

            if (userFound) {
                webDriver.switchToTopMostFrame();
                tmsHomePage.switchToMainFrame();
                webDriver.switchToFrame("PLTStatusReport");
                break;
            } else {
                //testResultService.addFailTest("Student "+userName+" was not found", true, true);
            }

            webDriver.switchToTopMostFrame();
            tmsHomePage.switchToMainFrame();
            webDriver.switchToFrame("PLTStatusReport");
            webDriver.ClickElement(tmsAssessmentsTestsAssignmentPage.nextPageInTableButton);
        }
        return index;
    }

    public void validateStatus(String userName, String expectedStatus) throws Exception {
        int userIndex = getUserIndexInTable(userName);
        webDriver.switchToFrame("tableFrame");
        testResultService.assertEquals(true, statusesTable.get(userIndex).getAttribute("textContent").equals(expectedStatus), "Test status of user: " + userName + " is incorrect. Expected: " + expectedStatus + ". Actual: " + statusesTable.get(userIndex).getAttribute("textContent"));
        webDriver.switchToTopMostFrame();
        tmsHomePage.switchToMainFrame();
        webDriver.switchToFrame("PLTStatusReport");
        navigateToFirstPageInTable();
        webDriver.switchToTopMostFrame();
        tmsHomePage.switchToMainFrame();
    }

    public void navigateToFirstPageInTable() throws Exception {
        TmsAssessmentsTestsAssignmentPage tmsAssessmentsTestsAssignmentPage = new TmsAssessmentsTestsAssignmentPage(webDriver, testResultService);

        int currentPageNum = Integer.parseInt(currentPageTable.getAttribute("textContent"));
        // return to first page
        for (int k = 0; k < currentPageNum; k++) {
            webDriver.ClickElement(tmsAssessmentsTestsAssignmentPage.backPageInTableButton);
        }
        tmsAssessmentsTestsAssignmentPage.waitUntilFirstPageIsReached();
    }

    public int getStartIndexFromExcel(List<String[]> exportedData) {
        int startIndex = 0;
        for (int i = 3; i < exportedData.size() - 3; i++) {
            if (!exportedData.get(i)[0].trim().equals("")) {
                startIndex = i;
                break;
            }
        }
        return startIndex + 1;
    }

    public List<String> getColumnListFromExcel(List<String[]> exportedData, String columnName, String reportType) throws IOException {
        int excelColumnIndex = 0;
        if (reportType.equals("Placement Test Status")) {
            excelColumnIndex = getColumnIndexPltStatusReport(columnName);
        } else if (reportType.equals("Course Tests Report")) {
            excelColumnIndex = getColumnIndexCourseTestReport(columnName);
        }

        int startIndex = getStartIndexFromExcel(exportedData);

        List<String> columnList = new ArrayList<String>();
        for (int i = startIndex; i < exportedData.size(); i++) {
            try {
                String tempValue = "";
                //columnList.add(exportedData.get(i)[excelColumnIndex].split("\t")[0]);
                if (exportedData.get(i)[0].split("\t")[excelColumnIndex].contains(".0000")) {
                    tempValue = exportedData.get(i)[0].split("\t")[excelColumnIndex].replace(".0000", "");
                } else {
                    tempValue = exportedData.get(i)[0].split("\t")[excelColumnIndex];
                }
                columnList.add(tempValue);
            } catch (Exception e) {
                columnList.add("");
            }
        }
        return columnList;
    }

    public int getColumnIndexPltStatusReport(String columnName) {
        int excelColumnIndex = 0;
        switch (columnName) {
            case "First Name":
                excelColumnIndex = 0;
                break;
            case "Last Name":
                excelColumnIndex = 1;
                break;
            case "User Name":
                excelColumnIndex = 2;
                break;
            case "Start Date":
                excelColumnIndex = 3;
                break;
            case "End Date":
                excelColumnIndex = 4;
                break;
            case "Test Status":
                excelColumnIndex = 5;
                break;
        }
        return excelColumnIndex;
    }

    public int getColumnIndexCourseTestReport(String columnName) {
        int excelColumnIndex = 0;
        switch (columnName) {
            case "First Name":
                excelColumnIndex = 0;
                break;
            case "Last Name":
                excelColumnIndex = 1;
                break;
            case "User Name":
                excelColumnIndex = 2;
                break;
            case "Midterm Test Final Grade":
                excelColumnIndex = 3;
                break;
            case "Course Test Final Grade":
                excelColumnIndex = 4;
                break;
        }
        return excelColumnIndex;
    }
	
	
	/*public ArrayList<String> getListFromTableByCoulmn(String column) throws Exception {
		
		TmsAssessmentsTestsAssignmentPage tmsAssessmentsAutomatedTestsPage = new TmsAssessmentsTestsAssignmentPage(webDriver, testResultService);

		column = column.toLowerCase();
		List<String> columnList = new ArrayList<String>();
		List<WebElement> list = new ArrayList<WebElement>();
		String attribute = "";
		switch(column){
			case "first name":
				list = tmsAssessmentsAutomatedTestsPage.usersFirstNames;
				attribute = "title";
				break;
			case "last name":
				list = tmsAssessmentsAutomatedTestsPage.usersLastNames;
				attribute = "title";
				break;
			case "user name":
				list = tmsAssessmentsAutomatedTestsPage.usersNames;
				attribute = "textContent";
				break;
			case "test name":
				list = tmsAssessmentsAutomatedTestsPage.testsNamesTable;
				attribute = "title";
				break;
			case "start date":
				list = tmsAssessmentsAutomatedTestsPage.startDatesTable;
				attribute = "title";
				break;
			case "end date":
				list = tmsAssessmentsAutomatedTestsPage.endDatesTable;
				attribute = "title";
				break;
			case "status":
				list = tmsAssessmentsAutomatedTestsPage.usersStatuses;
				attribute = "textContent";	
				break;
		}
		
		int totalPageNum = tmsAssessmentsAutomatedTestsPage.getNumberOfPagesInTable();
		
		for (int j = 0; j < totalPageNum; j++) {
			
			webDriver.switchToFrame("tableFrame");
			
			if(tmsAssessmentsAutomatedTestsPage.usersNames.size() == 0) {
				testResultService.addFailTest("Students Table is not Displayed.", true, true);
			}
			
			for (int i = 0; i < list.size(); i++) {
				//columnList.add(list.get(i).getAttribute(attribute));
				columnList.add(list.get(i).getText());
			}
			
			webDriver.switchToTopMostFrame();
			tmsHomePage.switchToMainFrame();
			if (j == totalPageNum-1) {
				// return to first page
				for (int k = 0; k < totalPageNum-1; k++) {
					webDriver.ClickElement(tmsAssessmentsAutomatedTestsPage.backPageInTableButton);	
				}
				tmsAssessmentsAutomatedTestsPage.waitUntilFirstPageIsReached();
			} else {
				webDriver.ClickElement(tmsAssessmentsAutomatedTestsPage.nextPageInTableButton);
			}
		}
		return (ArrayList<String>) columnList;
	}*/

    public String openStudentRecordingInStudentsRecordingsReport(int index) throws Exception {
        webDriver.switchToFrame("reviewRecordingsReport");
        webDriver.ClickElement(studentsNamesInStudentsRecordings.get(index));
        return studentsNamesInStudentsRecordings.get(index).getText();
    }

    public void filterByDateUtS() {
        Actions actions = new Actions(webDriver.getWebDriver());
        actions.doubleClick(dataUtcTableFilterButton).perform();
        //webDriver.getWebDriver().findElement(dataUtcTableFilterButton).click();
        //webDriver.clickOnElement(dataUtcTableFilterButton);
    }

    public void filterByFullPracticeLastScore() throws Exception {
        tmsHomePage.switchToGrandBookReportFrame();
        Actions actions = new Actions(webDriver.getWebDriver());
        actions.doubleClick(lastScoreButton).perform();

    }

    public String getTextFromScoreColumn() {
        return webDriver.getTextFromElement(scoreColumn);

    }

    public String checkUpdatedData() {
        return webDriver.getWebDriver().findElement(By.cssSelector("[aria-describedby='tblTestsReportGrid_LastUpdateStr']")).getText().replaceAll(" \\d{2}:\\d{2}$", "");
    }

    public WebElement returnRowStudent(String userName) throws InterruptedException {
        try {
            List<WebElement> rows = webDriver.getWebDriver().findElements(By.cssSelector("tr[role='row']"));

            for (int i = 0; i < rows.size(); i++) {
                WebElement row = rows.get(i);
                if (row.getText().contains(userName)) {
                    return row;
                }
            }
            WebElement nextButton = webDriver.getWebDriver().findElement(By.cssSelector(".ui-icon.ui-icon-seek-next"));
            if (nextButton.isEnabled()) {
                nextButton.click();
                return returnRowStudent(userName);
            }
        } catch (NoSuchElementException e) {

            e.printStackTrace();
        }

        return null;
    }

    public String getAttemptResultFullPractice(WebElement row) {
        return row.findElements(By.cssSelector("td")).get(5).getText();
    }

    public String getLostScoreFullPractice(WebElement row) {
        return row.findElements(By.cssSelector("td")).get(6).getText();
    }

    public void clickToeicTypeReportButton() {
        webDriver.ClickElement(reportTypeButton.get(1));
    }

    public void clickClassesDropdown() {
        webDriver.ClickElement(inputClassFiled.get(2));
    }

    public void filterByClasses(String className) throws Exception {
        webDriver.waitForElement(".ng-touched", ByTypes.cssSelector, "Can't filter by class column").sendKeys(className);

    }

    public void selectClassesCheckbox(String className) throws InterruptedException {
        webDriver.ClickElement(classesCheckbox);

    }

    public void applyFilter() throws InterruptedException {
        webDriver.ClickElement(applyFiltering);

    }

    public String isStatusDisplayed() {
        return webDriver.getTextFromElement(statusColumn);
    }

    public void clickInputField() {
        webDriver.ClickElement(inputClassFiled.get(1));
    }

    public void clickOnExportReport() throws Exception {
        WebElement exportPLT =  webDriver.waitForElement("//*[contains(text(),'Export')]", ByTypes.xpath);
        exportPLT.click();
        sleep(2);
    }

    public void clickStatusDropdown(){
        webDriver.ClickElement(inputClassFiled.get(5));

    }

    public void clickDone(){
        webDriver.ClickElement(statusCheckbox.get(2));
    }
    public static boolean compareData(WebDriver driver, String cssSelector, String[] expectedData) {

        List<WebElement> uiElements = driver.findElements(By.cssSelector(cssSelector));

        WebElement secondElement = uiElements.get(1);

        String[] uiRowData = secondElement.getText().split("\t");

        if (uiRowData.length != expectedData.length) {
            return false;
        }

        for (int j = 0; j < uiRowData.length; j++) {
            if (!uiRowData[j].trim().equals(expectedData[j].trim())) {
                return false;
            }
        }

        return true;
    }
}
