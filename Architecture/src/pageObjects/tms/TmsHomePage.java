package pageObjects.tms;

import Enums.ByTypes;
import Enums.StepProgressBox;
import Objects.Institution;
import Objects.SchoolAdmin;
import drivers.GenericWebDriver;
import org.json.JSONObject;
import org.junit.Assert;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import pageObjects.GenericPage;
import pageObjects.edo.*;
import services.TestResultService;
import tests.edo.newux.BasicNewUxTest;
import tests.edo.newux.testDataValidation;

import java.util.ArrayList;
import java.util.List;

public class TmsHomePage extends GenericPage {

	private String mainWindow;
		
	public String[] courses = BasicNewUxTest.courses;
	
	protected testDataValidation dataFactory;
	
	protected int smallTimeOut = 5;
	
	public TmsHomePage(GenericWebDriver webDriver,
			TestResultService testResultService) throws Exception {
		super(webDriver, testResultService);
		// TODO Auto-generated constructor stub
	}
		

	@Override
	public GenericPage waitForPageToLoad() throws Exception {
		
		try {
			webDriver.switchToTopMostFrame();
			webDriver.switchToFrame("mainFrame");
			webDriver.waitUntilElementAppears("EWebNavbarBg", ByTypes.className,60);
		} catch (Exception e) {
			e.printStackTrace();
		}
		webDriver.switchToMainWindow();
		return this;
	}

	@Override
	public GenericPage OpenPage(String url) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public TmsHomePage cliclOnReports() throws Exception {
		WebElement element = webDriver.waitUntilElementAppears("Reports", ByTypes.linkText,10);
		element.click();
		
		//webDriver.waitForElement("Reports", ByTypes.linkText).click();
		return this;
	}
	
	public TmsHomePage clickOnReports() throws Exception {
		webDriver.waitUntilElementAppears("Reports", ByTypes.linkText, 20);
		webDriver.waitForElement("Reports", ByTypes.linkText, 3, false).click();
		return this;
	}

	public TmsHomePage clickOnWritingAssignments() throws Exception {

		webDriver.waitUntilElementAppears("Reports", ByTypes.linkText,30);
		webDriver.waitForElement("Reports", ByTypes.linkText).click();
		webDriver.waitForElement("Writing Assignments", ByTypes.linkText).click();
		return this;
	}

	public TmsHomePage clickOnAssignment(String assignmentPartialText)
			throws Exception {
		webDriver
				.waitForElement(assignmentPartialText, ByTypes.partialLinkText)
				.click();
		return this;
	}

	public TmsHomePage clickOnStudentAssignment(String studentName,
			String courseName) throws Exception  {

		String mainFrame = null;
		//int i=0;
		try {
			Thread.sleep(1000);
			mainFrame = webDriver.switchToFrame("ReviewRequiredReport");

			report.report("Sort by Date desc");
			sortWritingAssignmentsByDateDesc();

			report.report("Click on Student: '" + studentName + "'");
			WebElement element = webDriver.waitUntilElementAppearsAndReturnElement(
					"//td//div[contains(text(),'" + studentName + "')]",5);
			Thread.sleep(2000);
					element.click();
			Thread.sleep(2000);

			webDriver.switchToMainWindow(mainFrame);
		} catch (Exception e) {
			for (int i=0;i<=10;i++){
				Thread.sleep(1000);
				mainFrame = webDriver.switchToFrame("ReviewRequiredReport");
				
			}
		/*
			sortWritingAssignmentsByDateDesc();
			WebElement element = webDriver.waitUntilElementAppearsAndReturnElement(
					"//td//div[contains(text(),'" + studentName + "')]", 7);
			Thread.sleep(3000);
			element.click();
			
			webDriver.switchToMainWindow(mainFrame);
		 */

		}
		//Thread.sleep(2000);
		// int elapsedTime = 0;
		// int maxTime = 120;
		// WebElement td = null;
		// while (elapsedTime < maxTime) {
		// td = webDriver.getTableTdByName("//*[@id='tblTestsReportGrid']",
		// studentName);
		// if (td != null) {
		// break;
		// } else {
		// logger.info("Waiting for student assignment for another 5 seconds");
		// elapsedTime = elapsedTime + 5;
		// Thread.sleep(5000);
		// }
		// }
		//
		// // td.click();
		// webDriver.clickOnElement(td);
		
		return this;
	}

	public TmsHomePage clickOnAssignmentSummary() throws Exception {
		
		webDriver.waitForElement("Summary", ByTypes.linkText).click();
		return this;
	}

	public TmsHomePage clickOnApproveAssignmentButton() throws Exception {
		WebElement element = webDriver.waitForElement(
				"//a[@class='button blue approve']", ByTypes.xpath);
		webDriver.clickOnElement(element);
		return this;
	}

	public TmsHomePage rateAssignment(int rating) throws Exception {
		// webDriver.waitForElement("//tr//td//input[@id='" + rating + "']",
		// ByTypes.xpath).click();

		webDriver.waitForElement(
				"//div[@class='tableWrapper']//table//tbody//tr[" + rating
						+ "]//td//div//div", ByTypes.xpath).click();
		webDriver.printScreen("Rating student as teacher");
		return this;
	}

	public TmsHomePage sendFeedback() throws Exception {
		// webDriver.waitForElement("btSubmit", ByTypes.id).click();
		webDriver.waitForElement("//div[@id='btSubmit']//a", ByTypes.xpath)
				.click();
		return this;
	}
	
	public TmsHomePage closeStudentEraterAssignment() throws Exception {
		
		webDriver.waitForElement("//div[contains(@class,'lightBoxClose')]//a", ByTypes.xpath)
				.click();
		return this;
	}

	public TmsHomePage clickOnRateAssignmentButton() throws Exception {
		webDriver.waitForElement("Rate", ByTypes.linkText).click();
		return this;
	}

	public TmsHomePage clickOnTeachers() throws Exception {
		webDriver.waitForElement("Teachers", ByTypes.linkText).click();
		return this;

	}
	
	public TmsHomePage clickOnSupervisor() throws Exception {
		webDriver.waitForElement("Supervisor", ByTypes.linkText).click();
		return this;

	}

	public TmsHomePage selectInstitute(String instituteName, String id,
			boolean switchFrame) throws Exception {
		return selectInstitute(instituteName, id, true, switchFrame);
	}

	public TmsHomePage selectInstitute(String instituteName, String id)
			throws Exception {
		return selectInstitute(instituteName, id, true, true);
	}

	public TmsHomePage selectInstitute(String instituteName, String id,
			boolean clickGo, boolean switchToFormFrame) throws Exception {
		String mainWin = null;
		if (switchToFormFrame) {
			mainWin = webDriver.switchToFrame("FormFrame");
		}

		// webDriver.waitForElementAndClick("SelectSchool", ByTypes.id);
		// Thread.sleep(1000);
		// webDriver.waitForElement(
		// "//select[@id='SelectSchool']//option[@value='" + id + "#"
		// + instituteName + "']", ByTypes.xpath).click();

		webDriver.selectElementFromComboBox("SelectSchool", instituteName);
		if (clickGo == true) {
			webDriver.waitForElement("//input[@value='  GO  ']", ByTypes.xpath)
					.click();
		}
		if (switchToFormFrame) {
			webDriver.switchToMainWindow(mainWin);
		}
		return this;
	}

	public TeacherDetailsPage clickOnAddNewTeacher() throws Exception {

		webDriver.waitForElementAndClick("//input[@value='Add New Teacher']",
				ByTypes.xpath);
		//webDriver.switchToNewWindow();
		return new TeacherDetailsPage(webDriver, testResultService);

	}
	
	public TeacherDetailsPage clickOnAddNewSupervisor() throws Exception {

		webDriver.waitForElementAndClick("//input[@value='Add New Supervisor']",
				ByTypes.xpath);
		//webDriver.switchToNewWindow();
		return new TeacherDetailsPage(webDriver, testResultService);

	}

	public TmsHomePage clickOnStudents() throws Exception {
		webDriver.waitForElement("Students", ByTypes.linkText).click();
		return this;

	}

	public TmsHomePage selectClass(String className) throws Exception {
		return selectClass(className, true, true);
	}

	public TmsHomePage selectClass(String className, boolean switchFrame,
			boolean clickGo) throws Exception {

		if (switchFrame) {
			switchToFormFrame();
		}

		webDriver.selectValueFromComboBox("SelectClass", className);
		Thread.sleep(2000);
		
		if (clickGo) {
			//webDriver.waitForElement("//input[@value='  GO  ']", ByTypes.xpath).click();
			WebElement element = webDriver.waitForElement("okButton2",ByTypes.className,true,5);
			element.click();
			Thread.sleep(2000);
		}

		webDriver.switchToTopMostFrame();
		switchToMainFrame();
		Thread.sleep(2000);
		
		return this;
	}

	public TmsHomePage switchToMainFrame() throws Exception {
		webDriver.switchToFrame("mainFrame");
		return this;

	}
	
	public TmsHomePage switchToFormFrame() throws Exception {
		webDriver.switchToFrame("FormFrame");
		return this;

	}
	
	public void CheckCefrLevel () throws Exception {
		webDriver.waitForElement("//span[@class='cefrLevel']", ByTypes.xpath);
		
	}

	public void enterStudentDetails(String studentName) throws Exception {
		webDriver.switchToTopMostFrame();
		webDriver.switchToFrame("mainFrame");
		
		enterStudentFname(studentName);
		enterStudentLname(studentName);
		enterStudentUserName(studentName);
		
		webDriver.waitForElement("//input[@value='Add']", ByTypes.xpath).click();
	}

	public void enterStudentFname(String fName) throws Exception {
		webDriver.waitForElement("FirstName", ByTypes.name).sendKeys(fName);
	}

	public void enterStudentLname(String name) throws Exception {
		webDriver.waitForElement("LastName", ByTypes.name).sendKeys(name);
	}

	public void enterStudentUserName(String userName) throws Exception {
		webDriver.waitForElement("UserName", ByTypes.name).sendKeys(userName);
	}

	public TmsHomePage enterStudentPassword(String userId, String password)
			throws Exception {

		String mainWin = webDriver.switchToFrame("tableFrame");
		webDriver.waitForElement("//*[@id='info" + userId + "']/a/img",
				ByTypes.xpath).click();
		Thread.sleep(3000);
		webDriver.switchToNewWindow();

		Thread.sleep(3000);
		// webDriver.switchToParentFrame();
		String mainPopupWin = webDriver.switchToFrame("FormFrame");
	
		webDriver.waitForElement("Password", ByTypes.id).clear();
		webDriver.waitForElement("Password", ByTypes.id).sendKeys(password);
		webDriver.switchToMainWindow(mainPopupWin);
		webDriver.waitForElement("//input[@value='Submit ']", ByTypes.xpath)
				.click();
		webDriver.switchToMainWindow(mainWin);

		return this;

	}

	public TmsHomePage clickOnInstitutions() throws Exception {
		webDriver.waitForElementAndClick("Institutions", ByTypes.linkText);
		return this;
	}

	public TmsHomePage clickOnAddNewSchool() throws Exception {
		webDriver.waitForElementAndClick("//input[@value='Add New School']",
				ByTypes.xpath);

		mainWindow = webDriver.switchToNewWindow();
		Thread.sleep(1000);
		return this;

	}

	public TmsHomePage enterNewSchoolDetails(Institution institution)
			throws Exception {
		switchToFormFrame();
		webDriver.waitForElement("SchoolName", ByTypes.id).sendKeys(
				institution.getName());
		webDriver.waitForElement("Phone", ByTypes.name).sendKeys(
				institution.getPhone());
		webDriver.waitForElement("Dname", ByTypes.id).sendKeys(
				institution.getHost());

		webDriver.waitForElement("Address", ByTypes.name).sendKeys(
				institution.getAddress());

		webDriver.selectElementFromComboBox("CountryCode", ByTypes.name,
				institution.getCountry());

		webDriver.waitForElement("NumOfCustomComp", ByTypes.id).sendKeys(
				institution.getNumberOfComonents());
		if (institution.getNumberOfUsers().equals("Unlimited")) {
			webDriver.setCheckBoxState(true, "UsersLimitation");
		} else {
			webDriver.waitForElement("NumOfUsers", ByTypes.id).sendKeys(
					institution.getNumberOfUsers());
		}

		if (institution.getConcurrentUsers().equals("Unlimited")) {
			webDriver.setCheckBoxState(true, "ConcLimitation");
		} else {
			webDriver.waitForElement("NumOfConc", ByTypes.id).sendKeys(
					institution.getConcurrentUsers());
		}

		if (institution.getActiveLicences().equals("Unlimited")) {
			webDriver.setCheckBoxState(true, "ActLicLimitation");
		} else {
			webDriver.waitForElement("NumOfActLic", ByTypes.id).sendKeys(
					institution.getActiveLicences());
		}
		webDriver.waitForElement("impType", ByTypes.id).click();

		webDriver.waitForElement("contactUsVal", ByTypes.id).sendKeys(
				institution.getEmail());
		String implType = null;

		if (institution.getSchoolImpType().equals("Blended")) {
			implType = "1";
		} else if (institution.getSchoolImpType().equals("Distance")) {
			implType = "2";
		} else if (institution.getSchoolImpType().equals("Additional")) {
			implType = "3";
		}

		// switch (institution.getSchoolImpType()) {
		// case additional:
		// implType = "3";
		// ;
		// break;
		// case blended:
		// implType = "1";
		// ;
		// break;
		// case distance:
		// implType = "2";
		// break;
		// }

		webDriver.waitForElement(
				"//select[@id='impType']//option[@value=" + implType + "]",
				ByTypes.xpath).click();
		webDriver.switchToTopMostFrame();
		webDriver.waitForElement("//*[@id='Administrator']/a", ByTypes.xpath)
				.click();
		webDriver.switchToFrame("FormFrame");

		webDriver.waitForElement("FirstName", ByTypes.name).sendKeys(
				institution.getSchoolAdmin().getFirstName());
		webDriver.waitForElement("LastName", ByTypes.name).sendKeys(
				institution.getSchoolAdmin().getLastname());
		webDriver.waitForElement("UserName", ByTypes.name).sendKeys(
				institution.getSchoolAdmin().getUserName());
		webDriver.waitForElement("Password", ByTypes.name).sendKeys(
				institution.getSchoolAdmin().getPassword());
		webDriver.waitForElement("Email", ByTypes.name).sendKeys(
				institution.getSchoolAdmin().getEmail());
		webDriver.waitForElement("SalesManager", ByTypes.name).sendKeys(
				institution.getSalesManager());
		webDriver.switchToTopMostFrame();
		webDriver.waitForElement("Submitbutton", ByTypes.name).click();

		// webDriver.switchToTopMostFrame();
		return this;

	}

	public TmsHomePage clickOnClasses() throws Exception {
		WebElement element = webDriver.waitUntilElementAppears("Classes", ByTypes.linkText,10);
		element.click();
		return this;

	}

	public TmsHomePage enterClassName(String classNae) throws Exception {
		webDriver.waitForElement("ClassName", ByTypes.id).sendKeys(classNae);
		return this;

	}

	public TmsHomePage clickOnAddClass() throws Exception {
		webDriver.waitForElement("AddClass", ByTypes.name).click();
		return this;
	}

	public TmsHomePage clickOnSettings() throws Exception {
		WebElement element = webDriver.waitUntilElementAppears("Settings", ByTypes.linkText, 20);
		element.click();
		return this;

	}

	public TmsHomePage clickOnFeatures() throws Exception {
		webDriver.waitForElement("Features", ByTypes.linkText).click();
		return this;

	}
	
	public TmsHomePage clickOnGradebook() throws Exception {
		webDriver.waitForElement("Gradebook", ByTypes.linkText).click();
		return this;

	}
	
	public TmsHomePage clickOnGradebookSettings() throws Exception {
		webDriver.waitForElement("Gradebook Settings", ByTypes.linkText).click();
		return this;

	}
	
	public TmsHomePage clickOnPromotionArea() throws Exception {
		webDriver.waitForElement("Promotion Area", ByTypes.linkText).click();
		return this;

	}
	
	public TmsHomePage clickOnCommunity() throws Exception {
		webDriver.waitForElement("Community", ByTypes.linkText).click();
		return this;

	}
	
	public TmsHomePage clickOnLanguage() throws Exception {
		webDriver.waitForElement("Language", ByTypes.linkText).click();
		return this;

	}
	
	public TmsHomePage clickOnMyForums() throws Exception {
		webDriver.waitForElement("My Forums", ByTypes.linkText).click();
		return this;

	}
	
	public TmsHomePage clickOnScheduleLessons() throws Exception {
		webDriver.waitForElement("Schedule Lessons", ByTypes.linkText).click();
		return this;

	}
	
	public TmsHomePage clickOnAssignForums() throws Exception {
		webDriver.waitForElement("Assign Forums", ByTypes.linkText).click();
		return this;
	}
	
	public TmsHomePage clickOnModerateForums() throws Exception {
		webDriver.waitForElement("Moderate Forums", ByTypes.linkText).click();
		return this;
	}
	
	public TmsHomePage clickOnPromotionAreaMenuButton(String buttonName) throws Exception {
		WebElement element = webDriver.waitForElement("//span[text()='" + buttonName + "']",ByTypes.xpath, false,8);
		//WebElement element = webDriver.waitUntilElementAppears("//span[text()='" + buttonName + "']",ByTypes.xpath, 8);
		if (element != null)
			element.click();
		
		if(buttonName.equals("Delete"))
			webDriver.closeAlertByAccept();
		
		//webDriver.waitForElement("//span[text()='" + buttonName + "']", ByTypes.xpath,false,smallTimeOut).click();
		return this;
	}

	public TmsHomePage clickOnToolsButton(String buttonName) throws Exception {
		WebElement element = webDriver.waitUntilElementAppears("//span[text()='" + buttonName + "']",ByTypes.xpath, 5);
		element.click();

		if(buttonName.equals("Delete"))
			webDriver.closeAlertByAccept();

		//webDriver.waitForElement("//span[text()='" + buttonName + "']", ByTypes.xpath,false,smallTimeOut).click();
		return this;
	}

	public void addCustomImageToSlide(String fileFullPath, String attachFileElement) throws Exception {
		webDriver.waitForElement("//div[contains(@class,'tmsSlideEditor__ImageSelectorBtn')]", ByTypes.xpath)
		.click();
		Thread.sleep(1000);
		webDriver.waitForElement("//span[contains(@class,'tmsSlideEditor__ImageSelectorImgUploadBtn')]", ByTypes.xpath)
		.click();
		Thread.sleep(1000);
		webDriver.switchToPopup();
		webDriver.waitForElement(attachFileElement,ByTypes.id).sendKeys(fileFullPath);
		webDriver.waitForElement("btnUpLoad",ByTypes.id).click();
		Thread.sleep(2000);
		webDriver.waitForElement("btnAttach",ByTypes.id).click();
		Thread.sleep(1000);
		webDriver.switchToMainWindow();
		webDriver.switchToNewWindow();
		webDriver.waitForElement("//a[contains(@class,'tmsSlideEditor__link tmsSlideEditor__ImageSelectorLink')]", ByTypes.xpath)
		.click();
		Thread.sleep(1000);
	}
	
	public TmsHomePage selectFeature(String feature) throws Exception {
		webDriver
				.waitForElement("//select[@id='SelectFeature']", ByTypes.xpath)
				.click();
		webDriver.waitForElement(
				"//select[@id='SelectFeature']//option[@value='" + feature
						+ "']", ByTypes.xpath).click();
		return this;

	}
	
	public void selectClassToMoveGroup(String ClassId) throws Exception {
		webDriver.waitForElement("//select[@name='selectClass']", ByTypes.xpath).click();
		webDriver.waitForElement("//select[@name='selectClass']//option[@value='" + ClassId
				+ "']", ByTypes.xpath).click();
		
	}
	
	public TmsHomePage selectReport(String reportValue) throws Exception {
		webDriver
				.waitForElement("//select[@id='SelectReport']", ByTypes.xpath)
				.click();
		webDriver.waitForElement(
				"//select[@id='SelectReport']//option[@value='" + reportValue
						+ "']", ByTypes.xpath).click();
		return this;

	}
	
	public TmsHomePage selectPeriod(String reportValue) throws Exception {
		webDriver
				.waitForElement("//select[@id='SelectPeriod']", ByTypes.xpath)
				.click();
		webDriver.waitForElement(
				"//select[@id='SelectPeriod']//option[@value='" + reportValue
						+ "']", ByTypes.xpath).click();
		return this;

	}
	
	public TmsHomePage selectPLTReport(String reportValue) throws Exception {
		webDriver
				.waitForElement("//select[@id='SelectReport']", ByTypes.xpath)
				.click();
		webDriver.waitForElement(
				"//select[@id='SelectReport']//option[@value='" + reportValue
						+ "']", ByTypes.xpath).click();
		return this;

	}
	
	public TmsHomePage selectCourse(String courseId) throws Exception {
		
       // webDriver.selectElementFromComboBox("SelectCourse", courseID);
       // webDriver.selectElementFromComboBoByIndex("SelectCourse", 1);
		
		//WebElement element = webDriver.waitForElement("//select[@id='SelectCourse']", ByTypes.xpath);
		//webDriver.clickOnElement(element);
		webDriver.waitForElement("//select[@id='SelectCourse']//option[@value='" + courseId + "']", ByTypes.xpath).click();
		
		Thread.sleep(2000);
		return this;
	}
	
	public TeacherReadMessagePage openLatestStudentMessage() throws Exception {
		webDriver.waitForElement("//tbody[@id='tblBody']/tr/td/a", ByTypes.xpath).click();
		return new TeacherReadMessagePage(webDriver, testResultService);
	}

	public TmsHomePage clickOnSelfRegistration() throws Exception {

		if (webDriver.waitForElement("SelfRegistrationCheckBox", ByTypes.id)
				.isSelected() == false) {
			webDriver.waitForElement("SelfRegistrationCheckBox", ByTypes.id)
					.click();
			webDriver.waitForElement("insertClass", ByTypes.id).click();
		}

		return this;

	}

	public TmsHomePage selectClassForFelfRegistration(String id)
			throws Exception {
		webDriver.waitForElement("selectClass", ByTypes.id).click();

		webDriver.waitForElement(
				"//select[@id='selectClass']//option[@value='" + id + "']",
				ByTypes.xpath).click();
		return this;
	}

	public TmsHomePage clickOnSaveFeature() throws Exception {
		webDriver.waitForElement("//td[@id='SaveTd1']//span", ByTypes.xpath)
				.click();
		return this;

	}

	public TmsHomePage clickOnInstitutionPackages() throws Exception {
		webDriver.waitForElement("Institution Packages", ByTypes.linkText)
				.click();
		return this;

	}

	public TmsHomePage clickOnAddPackages() throws Exception {
		webDriver.waitForElement("//input[@value='Add Packages']",
				ByTypes.xpath).click();
		return this;
		// TODO Auto-generated method stub

	}

	public TmsHomePage selectLevel(String levelName) throws Exception {
		webDriver.waitForElement("selectLevel", ByTypes.name).click();
		Thread.sleep(1000);
		webDriver.waitForElement(
				"//select[@name='selectLevel']//option[text()='" + levelName
						+ "']", ByTypes.xpath).click();
		webDriver.waitForElement("//button[@name='go']", ByTypes.xpath).click();
		return this;

	}

	public TmsHomePage selectPackage(String packageID) throws Exception {
		webDriver.waitForElement("//table//tbody[@id='TblObj']//tr[3]//td[2]",
				ByTypes.xpath).click();
		return this;

	}

	public TmsHomePage selectPackageStartDate(String packageId, int currentDay)
			throws Exception {
		webDriver.waitForElement("//a[@name='anchor" + packageId + "']//img",
				ByTypes.xpath).click();
		// int currentDay = dbService.getCurrentDay();
		String nainWin = webDriver.switchToPopup();
		webDriver.waitForElement("//td//a[text()='" + currentDay + "']",
				ByTypes.xpath).click();
		webDriver.switchToMainWindow(nainWin);
		switchToFormFrame();
		return this;
	}

	public TmsHomePage enterPackageQuantity(String packageId, String amount)
			throws Exception {
		webDriver.waitForElement("number" + packageId, ByTypes.name).sendKeys(
				amount);
		return this;

	}

	public TmsHomePage clickOnSubmitButton() throws Exception {
		webDriver.waitForElement("Submitbutton", ByTypes.name).click();
		return this;

	}

	public TmsHomePage checkPackageExist(String packageName) throws Exception {
		webDriver.waitForElement(
				"//tbody//tr//td[text()='" + packageName + "']", ByTypes.xpath)
				.isDisplayed();
		return this;

	}

	public TmsHomePage checkClassNameIsDisplayed(String className)
			throws Exception {
		WebElement element = webDriver.waitUntilElementAppears(
				"//tbody//tr//td//a[text()='" + className + "']", ByTypes.xpath,10);

		if (element==null)
			testResultService.addFailTest("class name not display:" + className,false,true);

		return this;
	}
	
	public boolean checkrowIsNotDisplayed(String detail) throws Exception {		
		WebElement element = webDriver.waitForElement("//*[contains(@id,'" + detail +"')]", ByTypes.xpath, false,1);
		if (element != null)
		{
			testResultService.addFailTest("User not removed", false, false);
			return true; //The system need to delete the user
		}
		return false;
	}

	
	public void clickOnInfoCard(String xpath, String Id) throws Exception {
	//webDriver.waitForElement("//*[@id='info" + userId + "']/td/a",ByTypes.xpath).click();
	  webDriver.waitForElement("//*[@id='"+ xpath + Id + "']/td/a",ByTypes.xpath,"User info card not found").click();
	}
	

	public TmsHomePage openAuthoringToolInfocard(String xpath, String Id)
			throws Exception {
		//webDriver.waitForElement("//*[@id='comp23516']/td[2]/a", ByTypes.xpath).click();
		webDriver.waitForElement("//*[@id='"+ xpath + Id + "']/td[2]/a",ByTypes.xpath,"Info card not found").click();
	
		return this;
	}
	
	public void checkRemoveCommentButtonStatus(boolean status,
			boolean switchToFrames) throws Exception {

		if (switchToFrames) {
			switchToMainFrame();
			webDriver.switchToFrame(webDriver.waitForElement(
					"//iframe[@class='cboxIframe']", ByTypes.xpath));
		}
		if (status == false) {
			webDriver.waitForElement(
					"//a[@id='butRemove'][@class='button remove disabled']",
					ByTypes.xpath);
		} else {
			webDriver.waitForElement(
					"//a[@id='butRemove'][@class='button remove']",
					ByTypes.xpath);
		}

	}

	public String selectFeedbackComment(String commentId) throws Exception {
		webDriver.waitForElement(commentId, ByTypes.id).click();
		String commentText = webDriver.waitForElement(commentId, ByTypes.id)
				.getText();
		return commentText;

	}

	public void clickTheRemoveCommentButton() throws Exception {
		webDriver.waitForElement("butRemove", ByTypes.id).click();
		;

	}

	public void checkIfCommentedTextIsInderlined(String commentId,
			boolean underline) throws Exception {

		if (underline) {
			webDriver.waitForElement("//id[@" + commentId+ "][contains(@class,'underline')]", ByTypes.xpath);
		} else {
			webDriver.checkElementNotExist("//id[@" + commentId
					+ "][contains(@class,'underline')]");
		}

	}

	public void checkCommentTitle(boolean underLine) {
		// webDriver.waitForElement("//div[@id='comments']//div[1]",
		// ByTypes.xpath)
		// TODO

	}

	public void clickOnXFeedback() throws Exception {
		webDriver.switchToFrame("mainFrame");
		String mainFrame = webDriver.switchToFrame(webDriver.waitForElement("//div[@id='cboxLoadedContent']//iframe", ByTypes.xpath));
		WebElement popup =  webDriver.waitForElement("//div[@class='right closeBt']", ByTypes.xpath,false,smallTimeOut);
			if (popup!=null) popup.click();		
		webDriver.switchToMainWindow(mainFrame);

	}

	public void clickOnCurriculum() throws Exception {
		//webDriver.waitForElement("Curriculum", ByTypes.linkText).click();
		
		WebElement element = webDriver.waitUntilElementAppears("Curriculum", ByTypes.linkText, 10);
		
		if (element!=null)
			element.click();

	}
	
	public void clickOnCommunication() throws Exception {
		
		WebElement element = webDriver.waitUntilElementAppears("Communication", ByTypes.linkText, 10);
		
		if (element!=null)
			element.click();
		
		//webDriver.waitForElement("Communication", ByTypes.linkText).click();
	}
	
	public void clickOnAssessment() throws Exception {
		webDriver.waitUntilElementAppears("Assessment", ByTypes.linkText, 121);
		webDriver.waitForElement("Assessment", ByTypes.linkText).click();

	}
	
	public void clickOnResources() throws Exception {
		//webDriver.waitForElement("Resources", ByTypes.linkText).click();
		
		WebElement element = webDriver.waitUntilElementAppears("Resources", ByTypes.linkText, 10);
		
		if (element!=null)
			element.click();

	}
	
	public void clickOnGuidelines() throws Exception {
		webDriver.waitForElement("Guidelines", ByTypes.linkText).click();
	}
	
	public void clickOnWorksheets() throws Exception {
		webDriver.waitForElement("Worksheets", ByTypes.linkText).click();
	}
	
	public void clickOnLessonPlans() throws Exception {
		webDriver.waitForElement("Lesson Plans", ByTypes.linkText).click();
	}
	
	public void clickOnExploreTexts() throws Exception {
		webDriver.waitForElement("Explore Texts", ByTypes.linkText).click();
	}
	
	public void clickOnWordLists() throws Exception {
		webDriver.waitForElement("Word Lists", ByTypes.linkText).click();
	}
	
	public void clickOnProjects() throws Exception {
		webDriver.waitForElement("Projects", ByTypes.linkText).click();
	}
	
	public void clickOnRolePlays() throws Exception {
		webDriver.waitForElement("Role-Plays", ByTypes.linkText).click();
	}
	
	public void clickOnRubrics() throws Exception {
		webDriver.waitForElement("Rubrics", ByTypes.linkText).click();
	}
	
	public void clickOnExitTests() throws Exception {
		webDriver.waitForElement("Exit Tests", ByTypes.linkText).click();
	}
	
	public void clickOnScopeAndSequence() throws Exception {
		webDriver.waitForElement("Scope and Sequence", ByTypes.linkText).click();
	}
	
	public void clickOnAssignPackages() throws Exception {
		//webDriver.waitForElement("Assign Packages", ByTypes.linkText).click();
		
		WebElement element = webDriver.waitUntilElementAppears("Assign Packages", ByTypes.linkText, 5);
		
		if (element!=null)
			element.click();
		
		
	}
	
	public void clickOnAssignCourses() throws Exception {
		webDriver.waitForElement("Assign Courses", ByTypes.linkText).click();
		
	}
	
	public void clickOnViewAllCourses() throws Exception {
		webDriver.waitForElement("View All Courses", ByTypes.linkText).click();
		
	}
	
	public void openPackageByName(String packageName) throws Exception {
		
		webDriver.waitForElement(packageName, ByTypes.partialLinkText).click();
		Thread.sleep(1000);		
	}
	
	public void clickOnAutomatedTests() throws Exception {
		webDriver.waitForElement("Automated Tests", ByTypes.linkText, true, webDriver.getTimeout()).click();	
	}
	
	public void clickOnAuthoringTool() throws Exception {
		webDriver.waitForElement("Authoring Tool", ByTypes.linkText, true, webDriver.getTimeout()).click();	
	}
	
	public void clickOnTestsAssignment() throws Exception {
		webDriver.waitForElement("Test Assignment", ByTypes.linkText, true, webDriver.getTimeout()).click();	
	}
	
	public void clickOnTestsConfiguration() throws Exception {
		webDriver.waitForElement("Test Configuration", ByTypes.linkText, true, webDriver.getTimeout()).click();	//Tests Configuration
	}

	public void markClassForPackageAssignment(String classId, String packageId)
			throws Exception {

		WebElement classCheckBox = webDriver.waitForElement("checkBoxClass"
				+ classId + packageId, ByTypes.id, false, 10);

		if (classCheckBox == null) {
			// move to next class page
			webDriver.waitForElement("//td[@id='pagesList6543']//span[2]",
					ByTypes.xpath).click();
			classCheckBox = webDriver.waitForElement("checkBoxClass" + classId
					+ packageId, ByTypes.id, false, 10);
			if (classCheckBox == null) {
				webDriver.waitForElement("//td[@id='pagesList6543']//span[3]",
						ByTypes.xpath).click();
				classCheckBox = webDriver.waitForElement("checkBoxClass"
						+ classId + packageId, ByTypes.id, false, 10);
			}
			if (classCheckBox == null) {
				Assert.fail("Class not found");
			}
		}
		webDriver.waitForElement("checkBoxClass" + classId + packageId,
				ByTypes.id).click();
	}

	public void clickOnTeacherFeedbackContinueButton() throws Exception {
		webDriver.waitForElement("Continue", ByTypes.linkText,
				"Problem finding Continue button in teacher's feedback")
				.click();
	}

	public void clickOnImport() throws Exception {
		webDriver.switchToTopMostFrame();
		switchToMainFrame();
		webDriver.waitForElement("//span[text()='Import']", ByTypes.xpath)
				.click();

	}

	public void clickOnPopupChooseFile() throws Exception {
		webDriver.waitForElement("flUpload", ByTypes.id).click();
	}

	public void clickOnExport() throws Exception {
		webDriver.switchToTopMostFrame();
		switchToMainFrame();
		webDriver.waitForElement("//span[text()='Export']", ByTypes.xpath)
				.click();

	}
	
	public void clickOnDelete() throws Exception {
		webDriver.switchToTopMostFrame();
		switchToMainFrame();
		webDriver.waitForElement("//span[text()='Delete']", ByTypes.xpath)
				.click();
	}

	public void markClassForExport(String classId) throws Exception {
		webDriver.waitForElement("Check$" + classId, ByTypes.id).click();
	}
	
	public void markTeacher(String teacherId) throws Exception {
		webDriver.waitForElement("Check$" + teacherId, ByTypes.name).click();
	}
	
	public void markSupervisor(String supervisorId) throws Exception {
		webDriver.waitForElement("Check$" + supervisorId, ByTypes.name).click();
	}
	
	public void markClassForDelete(String classId) throws Exception {
		try {
			
			webDriver.switchToTopMostFrame();
			webDriver.switchToFrame("mainFrame");	
			//WebElement pagesNum = webDriver.waitForElement("totalPageNum", ByTypes.id, false, smallTimeOut);
			WebElement pagesNum = webDriver.waitForElement("mainInfoBtn", ByTypes.id, false, smallTimeOut);
			webDriver.hoverOnElement(pagesNum);
			String classcount = webDriver.waitForElement("classNumber", ByTypes.id, false, smallTimeOut).getText();
			
		//	String classcount1 = pagesNum.getText();
//			switchToStudentDetailedPage();
			int num = Integer.parseInt(classcount);
		
			WebElement element = null;
			for (int i = 0; i < num || element!=null; i++) {
				element = webDriver.waitForElement("Check$" + classId, ByTypes.name, false, smallTimeOut);
				if (element == null) {
					webDriver.switchToTopMostFrame();
					webDriver.switchToFrame("mainFrame");	
					webDriver.waitForElement("nextTd", ByTypes.id).click();
					Thread.sleep(1000);
					switchToStudentDetailedPage();
				} else {
					break;
				}
			}
			if (element != null) {
				element.click();
			} else {
				testResultService.addFailTest("The class '"+classId+"' is not found");
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		//webDriver.waitForElement("Check$" + classId, ByTypes.name).click();
	}

	
	public void selectExportFormat(String fileType) throws Exception {
		webDriver.waitForElement("//input[@value='" + fileType + "']",
				ByTypes.xpath).click();

	}

	public void clickOnExportButtonInPopup() throws Exception {
		webDriver.waitForElement("btnUpload", ByTypes.name).click();

	}

	public String getExportFileName() throws Exception {
		String fileName = webDriver.waitForElement(
				"//td[@class='sinstructions']//a", ByTypes.xpath).getText();
		return fileName;
	}

	public void selectAllStudents() throws Exception {
		webDriver.waitForElement("selectionObj", ByTypes.id).click();
		webDriver.waitForElement("pageAll", ByTypes.id).click();

	}

	public String[] getStudentsForExport(int count) throws Exception {

		String[] studentsId = new String[count];
		String studentId = null;
		int j = 0;
		for (int i = 1; i < count + 1; i++) {
			studentId = webDriver.waitForElement(
					"//tbody[@id='tblBody']//tr[" + i + "]", ByTypes.xpath)
					.getAttribute("id");
			studentId = studentId.substring(2);
			studentsId[j] = studentId;
			j++;
		}
		return studentsId;
	}

	public void switchToTableFrame() throws Exception {
		webDriver.switchToFrame("tableFrame");

	}

	public void clickOnHomePage() throws Exception {
		webDriver.waitForElement("Home Page", ByTypes.linkText).click();

	}

	public void ClickTheHomeButton() throws Exception {
		webDriver.waitForElement("Home", ByTypes.linkText).click();
	}

	public void selectHomePageObject(String objectName) throws Exception {
		webDriver.waitForElementAndClick("cpselect_Mode", ByTypes.name);
		webDriver.waitForElement(
				"//select[@name='cpselect_Mode']//option[contains(text(),'"
						+ objectName + "')]", ByTypes.xpath).click();

	}

	public void selectInstituteInSettings(String institutionName,
			String institutionId, boolean clickGo) throws Exception {
		String mainWin = webDriver.switchToFrame("FormFrame");
		webDriver.waitForElementAndClick("cpselect_Inst", ByTypes.name);
		Thread.sleep(1000);
		webDriver.waitForElement(
				"//select[@name='cpselect_Inst']//option[@value='"
						+ institutionId + "#" + institutionName + "']",
				ByTypes.xpath).click();
		if (clickGo == true) {
			webDriver.waitForElement("//input[@value='  GO  ']", ByTypes.xpath)
					.click();
		}
		webDriver.switchToMainWindow(mainWin);

	}

	public void selectScreenArea(String elements) throws Exception {
		webDriver.waitForElementAndClick("cpselect_Sect", ByTypes.name);
		webDriver.waitForElement(
				"//select[@name='cpselect_Sect']//option[contains(text(),'"
						+ elements + "')]", ByTypes.xpath).click();

	}

	public void checkAddCommentButtonStatus(boolean enabled) throws Exception {
		switchToMainFrame();

		webDriver.switchToFrame(webDriver.waitForElement(
				"//iframe[@class='cboxIframe']", ByTypes.xpath));
		webDriver.waitForElement("butAdd", ByTypes.id).isEnabled();

	}

	public void clickAddCommentButton() throws Exception {
		webDriver.waitForElement("butAdd", ByTypes.id).click();

	}

	public void clickOnTextArea(int x, int y) throws Exception {
		WebElement assayText = webDriver.waitForElement(
				"//div[@id='essayText']//div[1]//div", ByTypes.xpath, false, webDriver.getTimeout());
		if (assayText!=null) {
			webDriver.clickOnElementWithOffset(assayText, x, y);
		}
	}

	public void enterTeacherCommentText(String commentText) throws Exception {
		webDriver.waitForElement("editedComments", ByTypes.id).click();
		webDriver.waitForElement("editedComments", ByTypes.id).sendKeys(
				commentText);

	}

	public void clickAddCommentDoneButton() throws Exception {
		webDriver.waitForElement("butDone", ByTypes.id).click();

	}

	public void swithchToCboxFrame() throws Exception {
		webDriver.switchToFrame(webDriver.waitForElement(
				"//iframe[@class='cboxIframe']", ByTypes.xpath));

	}

	public void clickOnRemoveCommentButton() throws Exception {
		webDriver.waitForElement("butRemove", ByTypes.id).click();
		;

	}

	public void clickOnCourseReports() throws Exception {
		webDriver.waitForElement("Course Reports", ByTypes.linkText, 5, false).click();

	}
	public void clickOnPLTReports() throws Exception {
		//webDriver.waitForElement("Placement Test Reports", ByTypes.linkText).click();
		
		WebElement element = webDriver.waitUntilElementAppears("Placement Test Reports", ByTypes.linkText, 10);
		
		if (element!=null)
			element.click();

	}
	
	public void clickOnPLTSummary() throws Exception {
		//webDriver.waitForElement("Placement Test Reports", ByTypes.linkText).click();
		
		WebElement element = webDriver.waitUntilElementAppears("Placement Test Reports", ByTypes.linkText, 10);
		
		if (element!=null)
			element.click();


	}
	
	
	public void clickOnInbox() throws Exception {
		
		WebElement element = webDriver.waitUntilElementAppears("Inbox", ByTypes.linkText, 10);
		
		if (element!=null)
			element.click();

	}
	
	public void clickOnSentItems() throws Exception {
		//webDriver.waitForElement("Sent Items", ByTypes.linkText).click();
		
		WebElement element = webDriver.waitUntilElementAppears("Sent Items", ByTypes.linkText, 10);
		
		if (element!=null)
			element.click();

	}


	public void selectCourseReport(String reportName) throws Exception {
		String mainWin = webDriver.switchToFrame("FormFrame");
		webDriver.waitForElementAndClick("SelectReport", ByTypes.id);
		Thread.sleep(1000);
		webDriver.waitForElement(
				"//select[@id='SelectReport']//option[contains(text(),'"
						+ reportName + "')]", ByTypes.xpath).click();

	}

	public void selectPackageByName(String packageName) throws Exception {
		// webDriver.waitForElementAndClick("SelectInstPackage", ByTypes.id);
		// Thread.sleep(2000);
		// webDriver.waitForElement(
		// "//select[@id='SelectInstPackage']//option[contains(text(),'"
		// + packageName + "')]", ByTypes.xpath).click();

		//webDriver.selectElementFromComboBox("SelectInstPackage", packageName);
		webDriver.selectValueFromComboBox("SelectInstPackage", packageName);
		Thread.sleep(3000);

	}

	public void clickOnGo() throws Exception {
		try {
			webDriver.waitForElement("Submit222223", ByTypes.name).click();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public StudentRecordingsPage openStudentRecordPageByPressOnGo() throws Exception {
		
		webDriver.switchToTopMostFrame();
		switchToMainFrame();
		switchToFormFrame();
				
		try {
			webDriver.waitForElement("Submit222223", ByTypes.name).click();
					

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new StudentRecordingsPage(webDriver, testResultService);
	}

	public void switchToReportFrame() throws Exception {
		switchToMainFrame();
		webDriver.switchToFrame("licenseUsageReport");

	}
	
	public void switchToReviewRecordingsFrame() throws Exception {
		
		switchToMainFrame();
		webDriver.switchToFrame(webDriver.waitForElement("reviewRecordingsReport", ByTypes.id));

	}
	
	public void switchToCourseTestsFrame() throws Exception {
		webDriver.switchToFrame(webDriver.waitForElement("CourseTestsReport", ByTypes.id));

	}

	public void switchToGrandBookReportFrame() throws Exception {
		webDriver.switchToFrame(webDriver.waitForElement("GradeBookReport", ByTypes.id));

	}
	
	public void switchToSearchFrame() throws Exception {
		webDriver.switchToFrame("searchFrame");

	}
	
	public void switchToOptionsFrame() throws Exception {
		webDriver.switchToFrame("options");

	}

	public void selectTeacher(String teacherName) throws Exception {
		// webDriver.waitForElementAndClick("SelectTeacher", ByTypes.id);
		// Thread.sleep(1000);
		// webDriver.waitForElementAndClick(
		// "//select[@id='SelectTeacher']//option[contains(text(),'"
		// + teacherName + "')]", ByTypes.xpath);

		webDriver.selectElementFromComboBox("SelectTeacher", teacherName, true);

	}

	public void clickOnLicenses() throws Exception {
		webDriver.waitForElement("Licenses", ByTypes.linkText).click();

	}

	public void selecStudent(String studentUserName) throws Exception {
	//	if (switchFrame) {
			switchToFormFrame();
		//}
		webDriver.selectElementFromComboBox("SelectUser", studentUserName, true);
		Thread.sleep(2000);
	}

	public void deactivateStudent() throws Exception {
		webDriver.waitForElement("//input[@type='radio'][@id='InActive']",
				ByTypes.xpath).click();

	}

	public void clickOnSave() throws Exception {
		webDriver.waitForElement("//td[@id='SaveTd0']//span", ByTypes.xpath)
				.click();
		Thread.sleep(1000);
	}

	public void checkForReportResults() throws Exception {
		WebElement element = webDriver.waitForElement("resultRoot", ByTypes.id,false,webDriver.getTimeout());
		element.isDisplayed();
	}

	public String getSelectedCourseInReport() throws Exception {
		String course = webDriver.getSelectedValueFromComboBox("SelectCourse");
		return course;
	}

	public String getSelectedClassInReport() throws Exception {
		webDriver.switchToTopMostFrame();
		switchToMainFrame();
		switchToFormFrame();
		String className = webDriver.getSelectedValueFromComboBox("SelectClass");
		webDriver.switchToTopMostFrame();
		switchToMainFrame();
		return className;
	}

	public void clickOnInstitutionDetails(String testSchoolId) throws Exception {
		webDriver.waitForElement(
				"//tbody[@id='con']//tr[@id='tr" + testSchoolId
						+ "']//td//a//img", ByTypes.xpath).click();

	}

	public void setActiveLicencesUnlimited(boolean setChecked) throws Exception {
		// get status
		boolean isChecked = webDriver.waitForElement("ActLicLimitation",
				ByTypes.id, "access license checkbox").isSelected();
		if (isChecked) {
			if (!setChecked) {
				// set uncheck
				webDriver.waitForElement("ActLicLimitation", ByTypes.id,
						"access license checkbox").click();
			}
		} else {
			if (setChecked) {
				webDriver.waitForElement("ActLicLimitation", ByTypes.id,
						"access license checkbox").click();
			}
		}

	}

	public void setNumberOfActiveLicences(String number) throws Exception {
		webDriver.waitForElement("NumOfActLic", ByTypes.id).clear();
		webDriver.waitForElement("NumOfActLic", ByTypes.id).sendKeys(number);

	}

	public void clickOnInstSettingSubmitButton() throws Exception {
		webDriver.waitForElement("Submitbutton", ByTypes.id).click();

	}

	public String getPopupText() {
		return webDriver.getAlertText(10);

	}

	public void checkPopupText(String string) {
		// TODO Auto-generated method stub

	}

	public void clickOnSyncStartButton() throws Exception {
		webDriver.waitForElement("//a[text()='Start']", ByTypes.xpath).click();

	}

	public void clickOnSyncSyncronizeButton() throws Exception {
		webDriver.waitForElement("//a[text()='Synchronize']", ByTypes.xpath)
				.click();

	}

	public boolean checkForSyncFile(String institutionId, String sutUrl)
			throws Exception {
		// String xpath = "//a[@href='" + sutUrl + "/sync/" + institutionId
		// + "/output/edo_offline_sync.zip']";
		// WebElement element = webDriver.waitForElement(xpath, ByTypes.xpath);

		WebElement element = webDriver.waitForElement("edo_offline_sync.zip",
				ByTypes.linkText);
		return element.isDisplayed();
	}

	public void clickOnRegistration() throws Exception {
		WebElement element = webDriver.waitUntilElementAppears("Registration", ByTypes.linkText,10);
		element.click();
	}

	public void createInstitution(String name, String phone,
			String concurrentUsers, String numOfComponents,
			String numberOfUsers, String impType, String host,
			String adminUserName, String adminPass, String adminEmail,
			String address, String country, String activeLicenses,
			String adminFirstName, String adminLastName, String contactUsEmail,
			String salesManager) throws Exception {

		clickOnAddNewSchool();

		Institution institution = new Institution();
		institution.setName(name);

		institution.setPhone(phone);
		institution.setConcurrentUsers(concurrentUsers);
		institution.setNumberOfComonents(numOfComponents);
		institution.setNumberOfUsers(numberOfUsers);
		institution.setSchoolImpType(impType);
		institution.setHost(host);
		institution.setSalesManager(salesManager);
		institution.setActiveLicenes(activeLicenses);
		institution.setCountry(country);
		institution.setAddress(address);
		SchoolAdmin schoolAdmin = new SchoolAdmin();
		// String adminUserName = adminUserName;
		schoolAdmin.setUserName(adminUserName);
		schoolAdmin.setName(adminFirstName);
		schoolAdmin.setPassword(adminPass);
		schoolAdmin.setEmail(adminEmail);
		schoolAdmin.setFirstName(adminFirstName);
		schoolAdmin.setLastname(adminLastName);
		institution.setSchoolAdmin(schoolAdmin);
		institution.setEmail(contactUsEmail);
		enterNewSchoolDetails(institution);



		webDriver.switchToMainWindow(mainWindow);
		webDriver.switchToFrame("mainFrame");
		// webDriver.switchToTopMostFrame();

		// dbService.verifyInstitutionCreated(institution);

	}

	public String getMainWindow() {
		return mainWindow;
	}

	public void setMainWindow(String mainWindow) {
		this.mainWindow = mainWindow;
	}

	public void sortWritingAssignmentsByDateDesc() throws Exception {
		WebElement element = webDriver.waitUntilElementAppears("jqgh_tblTestsReportGrid_date", ByTypes.id,7);
		Thread.sleep(2000);
		element.click();
		Thread.sleep(3000);
		webDriver.waitForElement("jqgh_tblTestsReportGrid_date", ByTypes.id).click();
		Thread.sleep(3000);
	}
	
	public void sortPromotionSlideByCreationDateDesc() throws Exception {
		webDriver.waitForElement("jqgh_promotionSlidesGrid_creationDate", ByTypes.id).click();
		Thread.sleep(2000);
		webDriver.waitForElement("jqgh_promotionSlidesGrid_creationDate", ByTypes.id).click();
		Thread.sleep(1000);
	}
	
	
	public void checkUserDetails(String expectedFN, String expectedLN) throws Exception {
		try {
			String expectedUserDetails = expectedFN + " " + expectedLN.trim();
			WebElement element = webDriver.waitUntilElementAppearsAndReturnElement("//li[contains(@class,'userNav_userName')]", 15);
			String userDetails = element.getText();
			int beginFN = userDetails.indexOf("o") + 2;
			String actualUserDetails = userDetails.substring(beginFN).trim();
			testResultService.assertEquals(expectedUserDetails, actualUserDetails, "User Details are not correct");
		} catch (Exception | AssertionError err) {
			err.printStackTrace();
			testResultService.addFailTest(err.getMessage(),true,true);
		}
	}
	public String [] getCoursesListAsArray() throws Exception {
		
		//List<WebElement> coursesElements = webDriver.getChildElementsByXpath(webDriver.waitForElement("TblObj", ByTypes.id), "//tr[contains(@id,'Course')]");
		//List<WebElement> coursesElements = webDriver.waitForElement("TblObj", ByTypes.id).findElements(By.xpath( "//tr[contains(@id,'Course')]"));
		List<WebElement> coursesElements = webDriver.waitForElement("//td[@class='resultereabg']", ByTypes.xpath).findElements(By.xpath( "//tr[contains(@id,'Course')]"));
		
		String [] coursesArray = new String[coursesElements.size()];
		
		for (int i = 0; i < coursesElements.size(); i++) {
			coursesArray[i] = coursesElements.get(i).getText().replace("   ", "");
		}
		
		return coursesArray;
	}
	
	public String [] getStudentsFirstLastNamesListAsArray() throws Exception {
		
		List<WebElement> firstNameElements = webDriver.getChildElementsByXpath(webDriver.waitForElement("tblBody", ByTypes.id), "//tr[contains(@id,'tr')]/td[4]/span[1]");
		List<WebElement> lastNameElements = webDriver.getChildElementsByXpath(webDriver.waitForElement("tblBody", ByTypes.id), "//tr[contains(@id,'tr')]/td[3]/span[1]");
		String [] firstNameArray = new String[firstNameElements.size()];
		String [] lastNameArray = new String[firstNameElements.size()];
		String [] studentFirstLastName = new String[firstNameElements.size()];
		
		for (int i = 0; i < firstNameElements.size(); i++) {
			firstNameArray[i] = firstNameElements.get(i).getText().trim();
			lastNameArray[i] = lastNameElements.get(i).getText().trim();
			studentFirstLastName[i] = firstNameArray[i] + " " + lastNameArray[i];
		}
		
		return studentFirstLastName;
	}
	
	public String getStudentRecordGradeByName(String userName) throws Exception {
		
		String actGrade = "undefined";
		actGrade = webDriver.waitForElement("//td[contains(@title,'"+userName+"')]/following-sibling::td[contains(@title,'Basic 2 --> Housing --> ')]/following-sibling::td[2]", ByTypes.xpath, "Student gradenot found").getAttribute("title");
		//actGrade = webDriver.waitForElement("//td[contains(@title,'"+userName+"')]/following-sibling::td[3]", ByTypes.xpath, "Student gradenot found").getAttribute("title");
				
		return actGrade;
	}
	
	public void openPreviewByCourseUnitAndLesson(String[] courseCodes, String courseCode, int unitNumber,
			int lessonNumber) throws Exception {

		String courseId = courses[getCourseIndexByCode(courseCodes, courseCode)];
		
		webDriver.waitForElement("btnCourse"+ courseId, ByTypes.id).click();
		Thread.sleep(1000);
		webDriver.waitForElement("//tr[@id='Course_"+courseId+"']/following-sibling::tr["+unitNumber+"]/td[7]/a", ByTypes.xpath).click();
		Thread.sleep(1000);
		webDriver.waitForElement("//tr[@id='Course_"+courseId+"']/following-sibling::tr["+unitNumber+"]/following-sibling::tr["+lessonNumber+"]/td[5]/a/img", ByTypes.xpath).click();
		Thread.sleep(1000);		
	}
	
	public NewUxLearningArea2 openPreviewByCourseUnitLessonLA2(String[] courseCodes, String courseCode, int unitNumber,
			int lessonNumber) throws Exception {

		String courseId = courses[getCourseIndexByCode(courseCodes, courseCode)];
		
		webDriver.waitForElement("btnCourse"+ courseId, ByTypes.id).click();
		Thread.sleep(1000);
		webDriver.waitForElement("//tr[@id='Course_"+courseId+"']/following-sibling::tr["+unitNumber+"]/td[7]/a", ByTypes.xpath).click();
		Thread.sleep(1000);
		webDriver.waitForElement("//tr[@id='Course_"+courseId+"']/following-sibling::tr["+unitNumber+"]/following-sibling::tr["+lessonNumber+"]/td[5]/a/img", ByTypes.xpath).click();
		Thread.sleep(1000);	
		
		return new NewUxLearningArea2(webDriver, testResultService);
		
	}
	
	public void openStudentStepProgressByUnitLessonId(String unitId, String lessonId) throws Exception {

				
		webDriver.waitForElement("//tr[@id='"+unitId+"']/td/a", ByTypes.xpath).click();
		Thread.sleep(1000);
		webDriver.waitForElement("//tr[@id='"+unitId+"_"+lessonId+"']/td/a", ByTypes.xpath).click();
		Thread.sleep(1000);		
	}
	
	public void verifyStepProgressIndication(String unitId, String lessonId, int stepNum, StepProgressBox expected) throws Exception {
		
		WebElement steps =  webDriver.waitForElement("//tr[@id='"+unitId+"_"+lessonId+"_"+"steps"+"']", ByTypes.xpath);
		List<WebElement> progressBars = steps.findElements(By.className("MyProgressDoneBox"));
		String progressImg = progressBars.get(stepNum-1).findElement(By.tagName("img")).getAttribute("src");
		String progressImgWidth = progressBars.get(stepNum-1).findElement(By.tagName("img")).getAttribute("width");
		String expImg = "undefined";
		String expWidth = "undefined";
		
		switch (expected) {
		
		case done:
			
			expImg = "Done.gif";
			expWidth = "12";
			break;
			
		case half:
			
			expImg = "Done.gif";
			expWidth = "7";
			break;
			
		case empty:
	
			expImg = "Transparent.gif";
			expWidth = "12";
			break;
		}		
		
		testResultService.assertEquals(true, progressImg.contains(expImg), "Step box progress indication is wrong. Step Num: " + stepNum + "Expected bar:" + expected);
		testResultService.assertEquals(true, progressImgWidth.equals(expWidth), "Step box progress indication is wrong. Step Num: " + stepNum + "Expected bar:" + expected);
	}
	
	public void verifyStepBoxTooltip(String unitId, String lessonId, int stepNum, String expStepName) throws Exception {
		
		WebElement stepBoxTooltipElement =  webDriver.waitForElement("//tr[@id='"+unitId+"_"+lessonId+"_"+"steps"+"']//tr/td["+stepNum+"]", ByTypes.xpath);
		String actualTooltip = stepBoxTooltipElement.getAttribute("title");
		String actualStepName = actualTooltip.split(":")[0];
				
		testResultService.assertEquals(expStepName, actualStepName, "Step Name does not match to expected");
	}
	
	
	public String openUnitReportAndPressOnTestScore(String unitName, String lessonName) throws Exception {
		
		webDriver.waitForElement("//td[text()='"+unitName+"']/preceding-sibling::td//a", ByTypes.xpath).click();
		webDriver.waitForElement("//*[@id='testAnswers']/div/div[1]", ByTypes.xpath, webDriver.getTimeout(), false, "Test score element not found.");
		//Thread.sleep(1000);
		WebElement scoreElement = webDriver.waitForElement("//td[text()='"+lessonName+"']/following-sibling::td[2]", ByTypes.xpath,false,smallTimeOut);
		scoreElement.click();
		
		
		return scoreElement.getText();
	}
	
	public int getCourseIndexByCode(String[] courseCodes, String courseCode) {
		int index = 0;
		for (int i = 0; i < courseCodes.length; i++) {
			if (courseCode.equals(courseCodes[i])) {
				index = i;
				break;
			}
		}
		return index;
	}
	
	public NewUxHomePage clickOnAccessToEd() throws Exception {
		
		//webDriver.waitForElement("Access to Student Platform", ByTypes.linkText).click();
		WebElement element = webDriver.waitUntilElementAppears("Access to Student Platform", ByTypes.linkText, 10);
		
		if (element!=null)
			element.click();

		
		return new NewUxHomePage(webDriver, testResultService);
	}
	
	public void clickOnExit() throws Exception {
		webDriver.switchToMainWindow();
		webDriver.switchToFrame("mainFrame");
		WebElement element =  webDriver.waitUntilElementAppearsAndReturnElementByType("Exit", ByTypes.linkText,10);
		element.click();
		// swith to ED window
		webDriver.switchToMainWindow();
		//verifyExitNoDisplay();
	}
	
	//new
	public void clickOnExitTMS() throws Exception {	
		webDriver.switchToMainWindow();
		webDriver.switchToFrame("mainFrame");
		webDriver.waitForElement("Exit", ByTypes.linkText,smallTimeOut,false).click();
		
		//verifyExitNoDisplay();
		//Thread.sleep(500);
		//NewUXLoginPage loginPage = new NewUXLoginPage(webDriver, testResultService);
		//boolean isSubmitEnabled = loginPage.isSubmitButtonEnabled();
		//testResultService.assertEquals(false, isSubmitEnabled, "Checking that Login Page displayed");
		//Thread.sleep(1000);
	}
	
	public void verifyExitNoDisplay() throws Exception {
		WebElement element = webDriver.waitForElement("Exit", ByTypes.linkText,false,smallTimeOut);
		
		//menu__userNavItemLink
		
		//for (int i =0; i< 3 && element != null; i++){
			//element = webDriver.waitForElement("Exit", ByTypes.linkText,false,3);
		//}
		if (element != null){
			testResultService.addFailTest("TMS Page still not closed");
		}
	}
	
	public void checkUncheckAssignPLT() throws Exception {
		webDriver.waitForElement("ptDisplay", ByTypes.id).click();

	}
	
	public boolean isPltSettingsDisplayed() throws Exception {
		WebElement element = webDriver.waitForElement("PTSettings", ByTypes.id, smallTimeOut,  false);
		if (element==null) {
			return false;
			}
		else {
			return true;
			}

	}
	
	public void clickOnSearch() throws Exception {
		webDriver.switchToTopMostFrame();
		switchToMainFrame();
		webDriver.waitForElement("//span[text()='Search']", ByTypes.xpath)
				.click();

	}
	
	
	public void findStudentInSearchSection(String className, String studentName) throws Exception {
		webDriver.switchToTopMostFrame();
		switchToSearchFrame();
		switchToOptionsFrame();
		webDriver.waitForElement("pName", ByTypes.name).sendKeys(studentName);
		webDriver.selectElementFromComboBox("SelectClass", className);
		Thread.sleep(2000);
		webDriver.waitForElement("//input[@value='Find']", ByTypes.xpath).click();
		Thread.sleep(3000);
		webDriver.switchToTopMostFrame();
		switchToSearchFrame();
		switchToFormFrame();
		webDriver.waitForElement("//img[@alt='Action']", ByTypes.xpath).click();
		Thread.sleep(3000);
		webDriver.switchToTopMostFrame();
		switchToSearchFrame();
		webDriver.waitForElement("//table[@class='SearchBg']//td[8]/a", ByTypes.xpath).click();
		
		webDriver.switchToTopMostFrame();
				
		
	}
	
	
	public void findCustomComponent(String freeText, String lesson) throws Exception {
		
		webDriver.waitForElement("LookFor", ByTypes.name).sendKeys(freeText);
		Thread.sleep(1000);
		webDriver.selectElementFromComboBox("SelectSkill", lesson);
		Thread.sleep(1000);
		webDriver.waitForElement("//input[@value='Find']", ByTypes.xpath).click();
		Thread.sleep(5000);
	}
	
	public String pressOnMidTermTestScore(String userName) throws Exception {
		boolean isUserFound = validateUserIsDisplayedInReportTable(userName);
		String score = "";
		if (isUserFound) {
			WebElement scoreLink = webDriver.waitForElement("//td[contains(@title,'"+userName+" "+userName+"')]//following-sibling::td[@aria-describedby='tblTestsReportGrid_midTermGrade']/span", ByTypes.xpath,false,webDriver.getTimeout());
	
			//WebElement scoreLink = webDriver.waitForElement("//td[contains(@title,'"+userName+" "+userName+"')]", ByTypes.xpath);
			score = scoreLink.getText();
			scoreLink.click();
		} else {
			testResultService.addFailTest("User: " + userName + " was not found");
		} 
		
		return score;
	}
	
	public String MidTermTestScoreResumeMode(String userName) throws Exception {
		boolean isUserFound = validateUserIsDisplayedInReportTable(userName);
		String score= "";
		if (isUserFound) {
			WebElement scoreLink = webDriver.waitForElement("//td[contains(@title,'"+userName+" "+userName+"')]//following-sibling::td[@aria-describedby='tblTestsReportGrid_midTermGrade']/span", ByTypes.xpath);
			
			if (scoreLink.getText().trim().equals(score)){
				return score;
			} else {	
				webDriver.failTest();
			}
		}
		return score;
	}
	
	public String getLegendText() throws Exception {
		
		WebElement element = webDriver.waitUntilElementAppearsAndReturnElement("//table[@class='legend']",15);
		String legendText = element.getText();

		//webDriver.waitForElement("//table[@class='legend']", ByTypes.xpath).getText();
		return legendText;
	}
	
	public List<String[]> getStudentUnitsCompletionInTableByName (String studentName) throws Exception {
		
		webDriver.waitUntilElementAppears("//td[contains(.,'"+studentName+"')]",10);
				
		WebElement userLine = webDriver.waitForElement("//td[contains(.,'"+studentName+"')]", ByTypes.xpath).findElement(By.xpath(".."));
		List<WebElement> units = userLine.findElements(By.className("ReportscourseBox"));
	
		
		List<String[]> unitNumCompletion = new ArrayList<>(); 
		String progress;
		
		for (int i = 0; i < units.size(); i++) {
			
			progress = units.get(i).findElement(By.xpath("..")).getAttribute("title").split("Completion: ")[1].replace("%", "");
			String [] unitProgress = {String.valueOf(i+1), progress};
			unitNumCompletion.add(unitProgress);
			
		}
		
		return unitNumCompletion;
		
		
	}
	
	public List<String[]> getStudentUnitsScoreInTableByName (String studentName) throws Exception {
		
		WebElement userLine = webDriver.waitForElement("//td[contains(.,'"+studentName+"')]", ByTypes.xpath).findElement(By.xpath(".."));
		List<WebElement> units = userLine.findElements(By.className("ReportscourseBox"));
	
		
		List<String[]> unitNumScore = new ArrayList<>(); 
		String progress;
		
		for (int i = 0; i < units.size(); i++) {
			
			progress = units.get(i).findElement(By.xpath("..")).getText();
			String [] unitProgress = {String.valueOf(i+1), progress};
			unitNumScore.add(unitProgress);
			
		}
		
		return unitNumScore;
		
		
	}
	
	public String getStudentTotalTimeOnTask (String studentName) throws Exception {
		
		WebElement userLine = webDriver.waitForElement("//td[contains(.,'"+studentName+"')]", ByTypes.xpath).findElement(By.xpath(".."));
		List<WebElement> values = userLine.findElements(By.className("stext"));
		String tot = values.get(values.size()-1).getText();	
		
		//tot = tot.split(":")[0] +":"+ tot.split(":")[1];
		
		return tot;
		
		
	}
	
	public String getStudentTotalTimeOnLesson (String studentName) throws Exception {
		
		WebElement userLine = webDriver.waitForElement("//td[contains(.,'"+studentName+"')]", ByTypes.xpath).findElement(By.xpath(".."));
		List<WebElement> values = userLine.findElements(By.className("stext"));
		String tot = values.get(values.size()-1).getText();	
			
		return tot;
	}
	
	public NewUxMyProfile clickOnMyProfile() throws Exception {
		
		WebElement myProfile = webDriver.waitForElement("My Profile",ByTypes.linkText,"My Profile link not found");
		myProfile.click();
		
		webDriver.sleep(2);  // igb 2018.11.13
		
		return new NewUxMyProfile(webDriver, testResultService);
	}	
	
	public void clickOnCourseBuilder() throws Exception {
		webDriver.waitForElement("Course Builder", ByTypes.linkText).click();

	}
	
	public void clickOnCourseSequence() throws Exception {
		webDriver.waitForElement("Course Sequence", ByTypes.linkText).click();

	}
	
	public TmsCreateNewCoursePage clickOnNewInCourseBuilder() throws Exception {
		webDriver.waitForElement("//span[text()='New']", ByTypes.xpath).click();
		return new TmsCreateNewCoursePage(webDriver, testResultService);
	}
	
	public void clickOnSaveInCourseBuilder() throws Exception {
		webDriver.waitForElement("//span[text()='Save']", ByTypes.xpath).click();
		Thread.sleep(3000);
	}
	
	public TmsCreateNewCoursePage clickOnOpenInCourseBuilder() throws Exception {
		webDriver.waitForElement("//span[text()='Open']", ByTypes.xpath).click();
		return new TmsCreateNewCoursePage(webDriver, testResultService);
	}
	
	public void checkCourseAndDeleteInCourseBuilder() throws Exception {
		webDriver.waitForElement("chkCourse", ByTypes.name).click();
		Thread.sleep(1000);
		webDriver.waitForElement("//span[text()='Delete']", ByTypes.xpath).click();
		webDriver.closeAlertByAccept();		
		
	}
	
	public void clickOnNewUnitInCourseBuilder() throws Exception {
		webDriver.waitForElement("newUnit", ByTypes.name).click();
		
	}
	
	public void clickOnNewComponentInCourseBuilder(int unitNumber) throws Exception {
		webDriver.waitForElement("newComponentToUnit"+unitNumber, ByTypes.name).click();
		
	}
	
	public void selectComponentCourseInCourseBuilder(int unitNumber,int lessonNumber, String courseName) throws Exception {
		//webDriver.selectElementFromComboBox("selectLevel"+unitNumber+"_"+lessonNumber, ByTypes.id, courseName);
		webDriver.selectValueFromComboBox("selectLevel"+unitNumber+"_"+lessonNumber, courseName, ByTypes.id);
	}
	
	public void selectComponentSkillInCourseBuilder(int unitNumber,int lessonNumber, String skill) throws Exception {
		//webDriver.selectElementFromComboBox("selectSkill"+unitNumber+"_"+lessonNumber, ByTypes.id, skill);
		webDriver.selectValueFromComboBox("selectSkill"+unitNumber+"_"+lessonNumber, skill, ByTypes.id);
	}
	
	public void selectComponentNameInCourseBuilder(int unitNumber,int lessonNumber, String componentName) throws Exception {
		//webDriver.selectElementFromComboBox("selectComp"+unitNumber+"_"+lessonNumber, ByTypes.id, componentName);
		webDriver.selectValueFromComboBox("selectComp"+unitNumber+"_"+lessonNumber, componentName, ByTypes.id);
	}
	
	public void verifyLessonPreviewInCourseBuilder(int unitNumber,int lessonNumber, NewUxLearningArea learnArea) throws Exception {
		
		WebElement compElement = webDriver.waitForElement("TRComp"+unitNumber+"_"+lessonNumber, ByTypes.id);
		String expUnitName = webDriver.waitForElement("unitName"+unitNumber, ByTypes.name).getAttribute("value");
		String expLessonName = webDriver.getSelectedValueFromComboBox("selectComp"+unitNumber+"_"+lessonNumber);
		
		WebElement element = webDriver.waitForElement("//tr[@id='TRComp"+unitNumber+"_"+lessonNumber+"']/td/a", ByTypes.xpath,false,webDriver.getTimeout());
				element.click();
		webDriver.switchToNextTab();
		Thread.sleep(3000);
		learnArea.checkUnitLessonStepNameOnLanding("Unit "+unitNumber+": "+ expUnitName, expLessonName, "Explore");
								
		learnArea.clickOnStep(2);
		learnArea.clickOnNextButton();
		webDriver.closeNewTab(1);
		webDriver.switchToMainWindow();
		int w = 0;
				
	}
	
	public void enterUnitNameInCourseBuilder(int unitNumber, String name) throws Exception {
		webDriver.waitForElement("unitName"+unitNumber, ByTypes.name).sendKeys(name);
	}
	
	public void verifyCourseNameInCourseBuilder(String expCourseName) throws Exception {
		String courseHeader = webDriver.waitForElement("courseNameTD", ByTypes.id).getText();
		testResultService.assertEquals(true, courseHeader.startsWith(expCourseName),"Course name in header does not match to new course");
				
	}
	
	public void verifyCourseVersionInCourseBuilder(String expVersion) throws Exception {
		String courseHeader = webDriver.waitForElement("courseNameTD", ByTypes.id).getText();
		String actVersion = courseHeader.split(":")[1].trim();//
		testResultService.assertEquals(expVersion, actVersion,"Course version in header does not match to new course");
		
	}
	
	
	public List<String> getDefaultUnitsInNewCourseOfBuilder () throws Exception {
		
		List<WebElement> units = webDriver.waitForElement("courseBody", ByTypes.id).findElements(By.xpath("//input[contains(@name,'unitName')]"));
				
		List<String> unitNames = new ArrayList<>(); 
		
		for (int i = 0; i < units.size(); i++) {
			
			unitNames.add(units.get(i).getAttribute("value"));
			
		}
		
		return unitNames;
		
		
	}
	
	public List<String[]> getDefaultComponentsInNewCourseOfBuilder (int unitNumber) throws Exception {
									
		List<WebElement> components = webDriver.waitForElement("unitBody"+ unitNumber, ByTypes.id).findElements(By.tagName("tr"));
				
		List<String[]> compFields = new ArrayList<>(); 
					
		for (int i = 0; i < components.size(); i++) {
			
			int lessonNumber = i+1;
			
			String compCourse = webDriver.getSelectedValueFromComboBox("selectLevel" + unitNumber + "_" + lessonNumber);
			String compSkill = webDriver.getSelectedValueFromComboBox("selectSkill" + unitNumber + "_" + lessonNumber);
			String compName = webDriver.getSelectedValueFromComboBox("selectComp" + unitNumber + "_" + lessonNumber);
			String compId = webDriver.getSelectedAttributeValueFromComboBox("selectComp" + unitNumber + "_" + lessonNumber);
			
			String[] fields = { compCourse, compSkill, compName, compId };
			
			compFields.add(fields);
		}
		
		return compFields;
				
	}


	public void verifyInfoCardDetails(String tagname, String expectName) throws Exception {
		// TODO Auto-generated method stub
		
		String actualName = webDriver.waitForElement(tagname, ByTypes.name).getAttribute("value");
		testResultService.assertEquals(expectName, actualName);
	}
	
	public void switchToStudentDetailedPage() throws Exception {
		webDriver.switchToTopMostFrame();
		webDriver.switchToFrame("mainFrame");	
		webDriver.switchToFrame("tableFrame");
	}
	
	public void ClickToGo () throws Exception {
		webDriver.waitForElement("//input[@value='  GO  ']", ByTypes.xpath)
		.click();
	}
	
	public void switchToteacherDetailedPage() throws Exception {
		webDriver.switchToTopMostFrame();
		webDriver.switchToFrame("mainFrame");
	}
	
	public void verifyTMSAttachmentIcon() throws Exception{
		boolean attach = webDriver.waitForElement("//tbody[@id='tblBody']/tr/td[3]//img", ByTypes.xpath).getAttribute("src").contains("attach");
		testResultService.assertEquals(true, attach, "No Attach Icon display", true);
	}
	
	public void verifyCefrInPLtWidgetList(String [] coursesNamesToCheck) throws Exception{
		
		List <WebElement> courses = webDriver.waitForElement("PTWidgetWrapper", ByTypes.id).findElements(By.xpath("//div[contains(@class,'legendItem')]"));
		
		dataFactory = new testDataValidation();
		
		JSONObject map = dataFactory.getCefrMapAsJson();
				
		for (int i = 0; i < courses.size(); i++) {
		
			String courseName = courses.get(i).getText();
			testResultService.assertEquals(true, courseName.contains(map.getString(coursesNamesToCheck[i])), "PLT LIST --- CEFR INDICATION is NOT CORRECT for COURSE: " + courseName, true);
		}
	}
	
	public void verifyCefrInDashboardHeader() throws Exception{
		
		String  currentCourseName = webDriver.waitForElement("spnCourse", ByTypes.id).getText();
		
		dataFactory = new testDataValidation();
		
		JSONObject map = dataFactory.getCefrMapAsJson();
		
		String courseFormatted = currentCourseName.split("CEFR")[0].replace("[", "").trim();
		
		testResultService.assertEquals(true, currentCourseName.contains(map.getString(courseFormatted)), "DASHBOARD --- CEFR INDICATION is NOT CORRECT for COURSE: " + currentCourseName, true);
		
	}
	
	
	public void verifyCefrInAssignCourses(String [] coursesNamesToCheck) throws Exception{
				
		dataFactory = new testDataValidation();
		
		JSONObject map = dataFactory.getCefrMapAsJson();
		
		String courseCefrLevel = "undefined";
		String courseUnderTest = "undefined";
				
		for (int i = 0; i < coursesNamesToCheck.length; i++) {
		
			courseUnderTest = coursesNamesToCheck[i];
			courseCefrLevel = webDriver.waitForElement("//a[contains(@id,'btnCourse')][text()[contains(.,'"+courseUnderTest+"')]]//span[@class='cefrLevel']", ByTypes.xpath).getText();
			testResultService.assertEquals(true, courseCefrLevel.equalsIgnoreCase(map.getString(courseUnderTest)), "Assign Courses --- CEFR INDICATION is NOT CORRECT for COURSE: " + courseUnderTest, true);
		}
	}
	
	public void verifyCefrInAssessments(String [] coursesNamesToCheck) throws Exception{
		
		dataFactory = new testDataValidation();
		
		JSONObject map = dataFactory.getCefrMapAsJson();
		
		String courseCefrLevel = "undefined";
		String courseUnderTest = "undefined";
				
		for (int i = 0; i < coursesNamesToCheck.length; i++) {
		
			courseUnderTest = coursesNamesToCheck[i];
			courseCefrLevel = webDriver.waitForElement("//tr/td[text()='"+courseUnderTest+"']//span[@class='cefrLevel']", ByTypes.xpath).getText();
			testResultService.assertEquals(true, courseCefrLevel.equalsIgnoreCase(map.getString(courseUnderTest)), "Assessments --- CEFR INDICATION is NOT CORRECT for COURSE: " + courseUnderTest, true);
		}
	}
	
	public void verifyCefrInViewAllCourses(String [] coursesNamesToCheck) throws Exception{
		
		dataFactory = new testDataValidation();
		
		JSONObject map = dataFactory.getCefrMapAsJson();
		
		String courseCefrLevel = "undefined";
		String courseNameUnderTest = "undefined";
				
		for (int i = 0; i < coursesNamesToCheck.length; i++) {
		
			courseNameUnderTest = coursesNamesToCheck[i];
						
			courseCefrLevel = webDriver.waitForElement("//td[text()[contains(.,'"+courseNameUnderTest+"')]]//span[@class='cefrLevel']", ByTypes.xpath).getText();
			
			testResultService.assertEquals(true, courseCefrLevel.equalsIgnoreCase(map.getString(courseNameUnderTest)), "View All Courses --- CEFR INDICATION is NOT CORRECT for COURSE: " + courseNameUnderTest, true);
		}
	}
	
	public void verifyCefrInCourseBuilderDropDownList(String [] coursesNamesToCheck, String selectID) throws Exception{
		
		dataFactory = new testDataValidation();
		
		JSONObject map = dataFactory.getCefrMapAsJson();
		
		String courseNameUnderTest = "undefined";
		String optionUnderTest = "undefined";
		
		Select select = new Select(webDriver.waitForElement(selectID, ByTypes.id));
		List<WebElement> options = select.getOptions();
				
		for (int i = 0; i < coursesNamesToCheck.length; i++) {
		
			courseNameUnderTest = coursesNamesToCheck[i];
			
			for (int j = 0; j < options.size(); j++) {
				optionUnderTest = (options.get(j).getText());
				if (optionUnderTest.contains(courseNameUnderTest)) break;
			}
			
			testResultService.assertEquals(true, optionUnderTest.contains(map.getString(courseNameUnderTest)), "Course Builder New Course --- CEFR INDICATION is NOT CORRECT for COURSE: " + courseNameUnderTest, true);
		}
	}

//--igb 2018.02.20 ----------------------------------------	
	public WebElement getFirstInfoNode() throws Exception{
// 	 	List<WebElement> chkEl  = webDriver.getElementsByXpath("//img[contains(@src,'info.gif')]");
		List<WebElement> chkEl  = webDriver.getElementsByXpath("//img[contains(translate(@src, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'),'info.gif')]");
	 	
   	 	WebElement chkParent = chkEl.get(0).findElement(By.xpath(".."));
		
// 	    if(!chkParent.getTagName().equals("td")) {
   	    if(chkParent.getTagName().equals("a")) {
   	    	return chkParent;
   	    }
   	    
   	    if(chkParent.getTagName().equals("td") && (!chkParent.getAttribute("class").equals("printNo"))) {
   	    	return chkParent;
   	    }

    	return chkEl.get(1).findElement(By.xpath("..")); 
	}

	public WebElement getLastInfoNode() throws Exception{
//	 	List<WebElement> chkEl  = webDriver.getElementsByXpath("//img[contains(@src,'info.gif')]");
		List<WebElement> chkEl  = webDriver.getElementsByXpath("//img[contains(translate(@src, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'),'info.gif')]");
	 	
	 	return chkEl.get(chkEl.size() - 1).findElement(By.xpath("..")); 
	}
	
	public WebElement getFirstExpandNode(boolean bParent) throws Exception{
//	 	List<WebElement> chkEl  = webDriver.getElementsByXpath("//img[contains(@src,'open.gif')]");
		List<WebElement> chkEl  = webDriver.getElementsByXpath("//img[contains(translate(@src, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'),'open.gif')]");
	 	
	 	if(!bParent) {
	 		return chkEl.get(0);
	 	}
	 	else {
	 		return chkEl.get(0).findElement(By.xpath(".."));
	 	}
	}
//--igb 2018.02.20 -----
	
	
	public void verifyAssignedElementToTeacher(String role) throws Exception{

		String actualRole;
		
		if (role.equals("Class")){
			actualRole = webDriver.waitForElement("//*[@id='listRight']/div[1]/label",ByTypes.xpath).getText();			
		
			if (actualRole.isEmpty()){
			testResultService.addFailTest(role + " not Assigned");	
				}
		}
		else if (role.equals("Teacher")){
			actualRole = webDriver.waitForElement("//*[@name='listRight']/option[1]",ByTypes.xpath).getText();			
			
			if (actualRole.isEmpty()){
				testResultService.addFailTest(role + " not Assigned");			
				}
		}	
	}

//--omz 2018.05.14 -----


	public void loginAsTmsdomain() throws Exception{
		String[] tmsDomain = dbService.getTMSDomainUser();
		webDriver.waitForElement("//input[contains(@name, 'userName')]", ByTypes.xpath).sendKeys(tmsDomain[0]);
		webDriver.waitForElement("//input[contains(@name, 'password')]", ByTypes.xpath).sendKeys(tmsDomain[1]);
		webDriver.waitForElement("//input[contains(@name, 'Domain')]", ByTypes.xpath).clear();
		webDriver.waitForElement("//input[contains(@name, 'Domain')]", ByTypes.xpath).sendKeys(tmsDomain[2]);
		webDriver.waitForElement("//input[contains(@name, 'Submit22')]", ByTypes.xpath).click();
		Thread.sleep(1000);

	}
		
	public void importFileForRegistration(String fileFullPath) throws Exception{
		
		
		webDriver.switchToFrame("FormFrame");
		webDriver.waitForElement("flUpload", ByTypes.id).sendKeys(fileFullPath);
		webDriver.switchToMainWindow();
		webDriver.switchToNewWindow();
		webDriver.waitForElement("//input[@value='  Import  ']", ByTypes.xpath).click();
		Thread.sleep(2000);

		String flag = dbService.getInstPropertyValueByProperyId(BasicNewUxTest.institutionId,47);

		if (flag != null && flag.equals("1"))
		{
			try {
				Alert alert = new WebDriverWait(webDriver.getWebDriver(), 25).until(ExpectedConditions.alertIsPresent());
				//Alert alert = webDriver.getWebDriver().switchTo().alert();
				String alertText = alert.getText();
				if (!alertText.isEmpty()) {
					if (!alertText.equalsIgnoreCase("TOEIC placement tests assigned successfully."))
						testResultService.addFailTest(alertText, true, true);
				}
			} catch (AssertionError aerr) {
				testResultService.addFailTest(aerr.getMessage(), true, true);
			} catch (Exception e) {
				testResultService.addFailTest(e.getMessage(), true, true);
			}
		}
		webDriver.printScreen(); //After upload. Added for Prod 2 debugging.
		webDriver.switchToFrame("FormFrame");
		webDriver.waitForElement("//*[@id='uploadRez']/td/span[contains(text(),'successfully imported.')]",ByTypes.xpath);
		webDriver.switchToMainWindow();
		webDriver.switchToNewWindow();
		webDriver.waitForElement("//input[@value='    OK     ']", ByTypes.xpath).click();
	}
	
	public void clickOnNotificationCenter() throws Exception {
		webDriver.waitForElement("Notification Center", ByTypes.linkText).click();
	}

	public void clickOnOnlineSessions() throws Exception {
		webDriver.waitForElement("Online Sessions", ByTypes.linkText).click();
	}
	
	public boolean validateUserIsDisplayedInReportTable(String userName) throws Exception{
		List<WebElement> studentsNames = webDriver.getWebDriver().findElements(By.xpath("//td[@class='tdt']//div"));
		boolean isUserFound = false;
		TmsReportsPage tmsReportsPage = new TmsReportsPage(webDriver, testResultService);
		int numOfPages = tmsReportsPage.getNumberOfPagesInReportsTable();
		for (int j = 0; j < numOfPages; j++) {
			for (int i = 0; i < studentsNames.size(); i++) {
				if (studentsNames.get(i).getText().equals(userName +" "+ userName)) {
					isUserFound = true;
					break;
				}
			}
			if (isUserFound) {
				break;
			} 
			webDriver.waitForElement("//td[@id='next_divGridPager']", ByTypes.xpath,true, 3).click();
			studentsNames = webDriver.getWebDriver().findElements(By.xpath("//td[@class='tdt']//div"));
		}
		return isUserFound;
	}
	
	public TmsHomePage verifyTestReportColumns(String[] expectedColumns) {

		try {
			webDriver.switchToFrame(webDriver.getElement(By.id("GradeBookReport")));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		// Find the table using your provided selector
		WebDriverWait wait = new WebDriverWait(webDriver.getWebDriver(), 30);
		WebElement table = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#gview_tblTestsReportGrid .ui-jqgrid-htable")));

		// Find the header elements within the table
		List<WebElement> headers = table.findElements(By.tagName("th"));

		// Validate the size of headers is same as expectedColumns
		Assert.assertEquals("The number of table headers does not match the number of expected columns.", expectedColumns.length, headers.size());

		// Loop through each header element
		for(int i = 0; i < headers.size(); i++) {
			// Get the text from the header element
			String headerText = headers.get(i).getText();

			// Verify that the text matches the corresponding entry in your expected columns
			Assert.assertEquals("Header text at index " + i + " does not match the expected text. Expected: " + expectedColumns[i] + ", but found: " + headerText, expectedColumns[i], headerText);
		}

		return this;
	}
	
	public TmsHomePage verifyTestReportTableValues() {

		try {
			webDriver.switchToFrame(webDriver.getElement(By.id("GradeBookReport")));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		WebDriverWait wait = new WebDriverWait(webDriver.getWebDriver(), 30);
		WebElement table = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#tblTestsReportGrid")));

		// Find the rows within the table
		List<WebElement> rows = table.findElements(By.tagName("tr"));

		// Loop through each row
		for(WebElement row: rows) {

			// Find the cells within the row
			List<WebElement> cells = row.findElements(By.tagName("td"));

			// Check column conditions
			for(int i = 0; i < cells.size(); i++) {

				String cellText = cells.get(i).getText();

				switch(i) {
					// First two columns always contain text data
					case 0:
					case 1:
						Assert.assertFalse("Column " + (i + 1) + " is empty but it should contain text data.", cellText.isEmpty());
						break;

					// Third column should contain text
					case 2:
						if(!cellText.isEmpty()) {
							Assert.assertFalse("Column " + (i + 1) + " is empty but it should contain text data.", cellText.isEmpty());
						}
						break;

					// Fourth column should contain numbers
					case 3:
						if(!cellText.isEmpty()) {
							Assert.assertTrue("Column " + (i + 1) + " doesn't contain a number.", cellText.matches("\\d+"));
						}
						break;

					// Fifth column should contain dates in a specific format
					case 4:
						if(!cellText.isEmpty()) {
							Assert.assertTrue("Column " + (i + 1) + " doesn't contain a date in the format 'dd/MM/yyyy HH:mm'.",
									cellText.matches("\\d{2}/\\d{2}/\\d{4} \\d{2}:\\d{2}"));
						}
						break;

					// Sixth column should contain numbers
					case 5:
						if(!cellText.isEmpty()) {
							Assert.assertTrue("Column " + (i + 1) + " doesn't contain a number.", cellText.matches("\\d+"));
						}
						break;

					// Default case can be used if we want to add any default checks for other columns
					default:
						// any default checks can go here
						break;
				}
			}
		}
		return this;
	}

	public TmsHomePage verifyTestReportHeaders() {

		try {
			webDriver.switchToFrame(webDriver.getElement(By.id("GradeBookReport")));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		WebDriverWait wait = new WebDriverWait(webDriver.getWebDriver(), 30);
		WebElement table = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#gview_tblTestsReportGrid .ui-jqgrid-htable")));
		List<WebElement> headerRows = table.findElements(By.cssSelector("tr.ui-jqgrid-labels"));
		Assert.assertEquals("Unexpected number of header rows", 2, headerRows.size());

		List<WebElement> mainHeaders = headerRows.get(0).findElements(By.tagName("th"));
		List<WebElement> subHeaders = headerRows.get(1).findElements(By.tagName("th"));

		for(int i = 0; i < mainHeaders.size(); i++) {
			String mainHeaderText = mainHeaders.get(i).getText();
			System.out.println("Main header: " + mainHeaderText);

			if(mainHeaderText.equals("Half Practice Test") || mainHeaderText.equals("Full Practice Test") || mainHeaderText.equals("Full Practice Test 2")) {
				List<WebElement> thisSubHeaders = subHeaders.subList(i, i+3);  // get the next three subheaders
				Assert.assertEquals("Attempts", thisSubHeaders.get(0).getText());
				Assert.assertEquals("Highest Score", thisSubHeaders.get(1).getText());
				Assert.assertEquals("Last Score", thisSubHeaders.get(2).getText());
			}
		}

		// remember to switch back from the iframe at the end
		webDriver.getWebDriver().switchTo().defaultContent();
		return this;
	}

	public TmsHomePage verifyPracticeTestReportScoreColumns() {

		try {
			webDriver.switchToFrame(webDriver.getElement(By.id("GradeBookReport")));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		WebDriverWait wait = new WebDriverWait(webDriver.getWebDriver(), 30);
		WebElement table = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#gview_tblTestsReportGrid .ui-jqgrid-htable")));
		List<WebElement> headerRows = table.findElements(By.cssSelector("tr.ui-jqgrid-labels"));
		Assert.assertEquals("Unexpected number of header rows", 2, headerRows.size());

		List<WebElement> mainHeaders = headerRows.get(0).findElements(By.tagName("th"));
		List<WebElement> subHeaders = headerRows.get(1).findElements(By.tagName("th"));

		for(int i = 0; i < mainHeaders.size(); i++) {
			String mainHeaderText = mainHeaders.get(i).getText();
			System.out.println("Main header: " + mainHeaderText);

			if(mainHeaderText.equals("Half Practice Test") || mainHeaderText.equals("Full Practice Test") || mainHeaderText.equals("Full Practice Test 2")) {
				List<WebElement> thisSubHeaders = subHeaders.subList(i, i+3);  // get the next three subheaders
				Assert.assertEquals("Attempts", thisSubHeaders.get(0).getText());
				Assert.assertEquals("Highest Score", thisSubHeaders.get(1).getText());
				Assert.assertEquals("Last Score", thisSubHeaders.get(2).getText());
			}
		}

		// remember to switch back from the iframe at the end
		webDriver.getWebDriver().switchTo().defaultContent();

		return this;
	}

	
	public TmsHomePage selectClassAndTimeFrame(String className, String timePeriod, boolean switchFrame,
			boolean clickGo) throws Exception {

		if (switchFrame) {
			switchToFormFrame();
		}

		webDriver.selectValueFromComboBox("SelectClass", className);
		Thread.sleep(1000);
		
		webDriver.selectValueFromComboBox("SelectPeriod", timePeriod);
		Thread.sleep(1000);
		
		if (clickGo) {
			webDriver.waitForElement("//input[@value='  GO  ']", ByTypes.xpath).click();
			Thread.sleep(2000);
		}

		webDriver.switchToTopMostFrame();
		switchToMainFrame();
		Thread.sleep(1000);
		
		return this;
	}
	
	public void clickOnPrint() throws Exception {
		webDriver.switchToTopMostFrame();
		switchToMainFrame();
		//webDriver.waitForElement("//span[text()='Print']", ByTypes.xpath)
		//		.click();
		
		List<WebElement> printBtns = webDriver.getWebDriver().findElements(By.xpath("//span[text()='Print']"));
		webDriver.ClickElement(printBtns.get(0));
	}
	
	public void markStudentForDelete(String userId) throws Exception{
		webDriver.switchToTopMostFrame();
		webDriver.switchToFrame("mainFrame");	
		String pagesNum = webDriver.waitForElement("totalPageNum", ByTypes.id, false, smallTimeOut).getText();
		
		boolean isFound = false;
		List<WebElement> usersNames; 
		List<WebElement> usersCheckBoxes; 
		for (int i = 0; i < Integer.parseInt(pagesNum); i++) {
			
			webDriver.switchToFrame("tableFrame");
			usersNames = webDriver.getWebDriver().findElements(By.xpath("//tbody[@id='tblBody']//tr//td[5]"));
			usersCheckBoxes =  webDriver.getWebDriver().findElements(By.xpath("//tbody[@id='tblBody']//tr//td[2]//input[@type='checkbox']"));
			for(int j = 0; j < usersNames.size(); j++) {
				if (usersNames.get(j).getAttribute("id").contains(userId)){
					usersCheckBoxes.get(j).click();
					isFound = true;
					break;
				}
			}
			
			if (isFound) {
				webDriver.switchToTopMostFrame();
				webDriver.switchToFrame("mainFrame");
				break;
				
			} else {
				webDriver.switchToTopMostFrame();
				webDriver.switchToFrame("mainFrame");	
				webDriver.waitForElement("nextTd", ByTypes.id).click();
				usersNames = webDriver.getWebDriver().findElements(By.xpath("//tbody[@id='tblBody']//tr//td[5]"));
			}
		}
	}


	public void clickOnCourseBasedOn() {
		webDriver.findElementByXpath("//input[@value='yes']",ByTypes.xpath).click();
	}


	public TmsHomePage clickOnAddStudets() throws Exception {
		webDriver.waitForElement("btnAddToeicTestStudents", ByTypes.id).click();;
		return this;
	}


	public void waitForSaveButtonAndClick() throws Exception {
		try {
			WebElement saveBtn = webDriver.waitForElement("//td[@id='SaveTd0']//span", ByTypes.xpath, 5, true);
			for(int i = 0;i<3&&saveBtn==null;i++) {
				saveBtn = webDriver.waitForElement("//td[@id='SaveTd0']//span", ByTypes.xpath, 5, true);
			}
			
			saveBtn.click();
			Thread.sleep(1000);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}


	public WebElement waitForAuthorizationKeyToBeDisplayed() throws Exception {
		WebElement authorizationKey = webDriver.waitForElement(".ui-widget-content.jqgrow.ui-row-ltr td:nth-child(6)", ByTypes.cssSelector,10,true);
		for(int i = 0;i<5&&authorizationKey==null;i++) {
			authorizationKey = webDriver.waitForElement(".ui-widget-content.jqgrow.ui-row-ltr td:nth-child(6)", ByTypes.cssSelector,10,true);
		}
		return authorizationKey;
	}


	public void clickOnTestOverview() throws Exception {
		webDriver.waitUntilElementAppears("Test Overview", ByTypes.linkText, 121);
		webDriver.waitForElement("Test Overview", ByTypes.linkText).click();
	}
}
