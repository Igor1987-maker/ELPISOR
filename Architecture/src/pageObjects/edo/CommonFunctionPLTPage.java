package pageObjects.edo;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.support.PageFactory;

import Enums.ByTypes;
import Enums.CourseCodes;
import Enums.CourseTests;
import Enums.UserType;
import Objects.CourseTest;
import drivers.GenericWebDriver;
import pageObjects.GenericPage;
import services.NetService;
import services.PageHelperService;
import services.TestResultService;
import services.TextService;

public class CommonFunctionPLTPage extends GenericPage {
	
	NewUxClassicTestPage classicTest;
	NewUxAssessmentsPage testsPage;
	
	public static final String Cannot_Take_PLT_Alert = "You cannot take the placement test at this time. If you would like to be assigned a placement test, please contact your program coordinator.";
	private static final String Language = "English";
			
	PageHelperService pageHelper = new PageHelperService();
	TextService textService;// = new TextService();
	NetService netService;
	
	public CommonFunctionPLTPage(GenericWebDriver webDriver, TestResultService testResultService) throws Exception {
		super(webDriver, testResultService);
		// TODO Auto-generated constructor stub
		PageFactory.initElements(webDriver.getWebDriver(), this);
		 //pageHelper = new PageHelperService();
		 textService = new TextService();
		 netService = new NetService();
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
	
	public void gotoCanvasPOST(String chkClass, String chkUser, String instName, String baseUrl, String chkPath) throws Exception {

		//pageHelper = new PageHelperService();
		
		//String baseUrl = pageHelper.CILink.split(".com")[0] + ".com/";
		//String chkPath = pageHelper.buildPath + "Languages/";

		String testFile = "testCanvasPOST.html";

		createTestCanvasPltFile(chkPath, baseUrl, chkClass, chkUser, testFile, instName);
		
		String chkURL = baseUrl + "Languages/" + testFile;
		
		webDriver.openUrl(chkURL);
		Thread.sleep(2000);
		
		webDriver.waitForElement("btnSubmit", ByTypes.name).click();
		Thread.sleep(2000);
		
		report.startStep("Check OptIn PrivacyCheckBox and press Continue");
		webDriver.waitForElementAndClick("optInPrivacyStatement__checkBoxW", ByTypes.className);
		webDriver.waitForElementAndClick("optInPrivacyStatement__continue", ByTypes.id);
		Thread.sleep(2000);
		
	}
	
	public void createTestCanvasPltFile(String chkPath, String baseUrl, String chkClass, 
		String chkUser, String testFile, String instName)  throws Exception {
		String instId = dbService.getInstituteIdByName(instName);
		String chkInst = dbService.getInstituteNameById(instId);
		
		//.....................................................		
		List<String> wList = new ArrayList<String>();
		
		wList = createHtmlTitle(wList);
		wList = createPltFormInfo(wList, baseUrl, chkInst, chkClass, chkUser);
		wList = createHtmlTail(wList);
		
		textService.writeListToSmbFile(chkPath + testFile, wList, netService.getDomainAuth());
	}	
		
	private List<String> createHtmlTitle(List<String> wList){
		wList.add("<!DOCTYPE html>");
		wList.add("<html>");
		wList.add("<head>");
		
		wList.add("\t<meta charset=\"utf-8\" />");
		wList.add("\t<title></title>");
		
		wList.add("</head>");
		wList.add("<body>");
		
		return  wList; 
	}
		
	private List<String> createPltFormInfo(List<String> wList, String chkURL, String chkInst, String chkClass, String chkUser) {
		wList.add("\t<form method=\"post\" action=\"" + chkURL + "entrance.aspx\">");
		wList.add("\t\t<input type=\"submit\" name=\"btnSubmit\" value=\"Form Submit\" />");
		
		wList.add("\t\t<input type=\"hidden\" name=\"oauth_nonce\" value=\"iKk7CNCRo0JtuHds75AjzzRFD9z5fnOmZaUBDpOhZuw\" />");
		wList.add("\t\t<input type=\"hidden\" name=\"oauth_consumer_key\" value=\"ED_FP\" />"); 
		wList.add("\t\t<input type=\"hidden\" name=\"oauth_signature\" value=\"I96FVJ658qR6yld9mA1+MziiZLY=\" />");
		wList.add("\t\t<input type=\"hidden\" name=\"lis_outcome_service_url\" value=\"https://siglo21.instructure.com/api/lti/v1/tools/190/grade_passback\" />");
		wList.add("\t\t<input type=\"hidden\" name=\"lis_result_sourcedid\" value=\"190-1276-17670-6241-65b27cce6f3cb6e8ca4fda409c4d8a5f3f487f77\" />");
		wList.add("\t\t<input type=\"hidden\" name=\"custom_edinstitution\" value=\"" + chkInst + "\" />");
	
	    wList.add("\t\t<input type=\"hidden\" name=\"roles\" value=\"Learner\" />");
		
		wList.add("\t\t<input type=\"hidden\" name=\"lis_person_name_family\" value=\"Edusoft 1\" />");
		wList.add("\t\t<input type=\"hidden\" name=\"lis_person_name_given\" value=\"Alumno\" />");
		wList.add("\t\t<input type=\"hidden\" name=\"lis_person_name_full\" value=\"Alumno Edusoft 1\" />");
		wList.add("\t\t<input type=\"hidden\" name=\"lis_person_contact_email_primary\" value=\"\" />");
		wList.add("\t\t<input type=\"hidden\" name=\"custom_canvas_user_id\" value=\"" + chkUser + "\" />");
		wList.add("\t\t<input type=\"hidden\" name=\"custom_user_sections\" value=\"" + chkClass + "\" />");
	    
//			wList.add("\t\t<input type=\"hidden\" name=\"custom_course_sections\" value=\"" + chkCanvasClass + "\" />");
		wList.add("\t\t<input type=\"hidden\" name=\"custom_course_sections\" value=\"123CanvasAuto\" />");

	//  igb 2018.11.20 this parameter already not mandatory (for entry before PLT) -----------			
	//	wList.add("\t\t<input type=\"hidden\" name=\"custom_course_id\" value=\"20000\" />");
	
		wList.add("\t\t<input type=\"hidden\" name=\"oauth_timestamp\" value=\"1525609571\" />");
	
		wList.add("\t</form>");

		return  wList; 
	}
	
	private List<String> createHtmlTail(List<String> wList) {
		wList.add("</body>");
		wList.add("</html>");
		
		return  wList; 
	}
	
	public String getEdUserId(String chkUser) throws Exception {
		return dbService.getExternalUserInternalId(chkUser);
	}
	
	public void setProgressInCourse(String courseId, int unitsToCompleteExceptTest, int averageTestScore, List<String> courseUnits, String studentId) throws Exception {
		
		String unitId = "undefined";
		String componentId = "undefined";
		String step_id = "undefined";
		List<String[]> step_items = null;
		
		for (int i = 0; i < unitsToCompleteExceptTest; i++) {
			
			unitId = courseUnits.get(i);
			componentId = dbService.getComponentDetailsByUnitId(unitId).get(0)[1];
			step_id = dbService.getSubComponentsDetailsByComponentId(componentId).get(0)[1];
			step_items = dbService.getSubComponentItems(step_id);
			studentService.setProgressForUnit(unitId, courseId, studentId, 0, false);
			studentService.submitTest(studentId, unitId, componentId, step_items, String.valueOf(averageTestScore), null, false);
			
		}
	
	}
	
	public String completeCourseTest(int courseSequenceInAvailableSection, CourseCodes code, CourseTests testType, int sectionsToAnswerCorrect, int totalSections) throws Exception {
		testsPage = new NewUxAssessmentsPage(webDriver, testResultService);
		
		classicTest = testsPage.clickOnStartTest("1", String.valueOf(courseSequenceInAvailableSection));
		Thread.sleep(1000);
		webDriver.closeAlertByAccept();
		Thread.sleep(1000);
		webDriver.switchToNewWindow();
		classicTest.switchToTestAreaIframe();
		Thread.sleep(1000);
		classicTest.pressOnStartTest();
		Thread.sleep(3000);
		CourseTest courseTest = classicTest.initCourseTestFromCSVFile("files/CourseTestData/CourseTest_Answers.csv", code, testType, sectionsToAnswerCorrect);
		classicTest.performCourseTest(courseTest, totalSections);
		
		report.startStep("Get score and close test");
		classicTest.switchToCompletionMessageFrame();
		Thread.sleep(1000);
		String finalScore = classicTest.getFinalScore();
		webDriver.switchToTopMostFrame();
		classicTest.switchToTestAreaIframe();
		classicTest.closeCompletionMessageAlert();
		Thread.sleep(1000);
		
		return finalScore;
	
	}
	
	public String regLogin(String instID, String userName, UserType userType, boolean createNewClass, String CorpUrl, String baseUrl, String classNameAR) throws Exception {
		
		report.addTitle("The parmeters is: " + instID + "UserName: " + userName + "Courses ints Id: " + instID); 
		String instName = dbService.getInstituteNameById(instID);
		String userFN =  userName + "FN";
		String userLN =  userName + "LN";
		String email = userName + "@edusoft.co.il";
		char createClass = 'N';
		char userTypeParam = 'S';
		//classNameAR = configuration.getProperty("arg_className");
		
		switch (userType) {
				
		case Teacher: userTypeParam = 'T';
		
		}
		
		//String classNameAR = null;
		if (createNewClass) {
			createClass = 'Y';
			classNameAR = classNameAR + dbService.sig(3);
		}
		
		//String baseUrl = pageHelper.CILink.split(".com")[0] + ".com/";
		String regInsertUrl = baseUrl+"RegUserAndLogin.aspx?Action=I&UserName="+userName+"&Inst="+instName+"&FirstName="+userFN+"&LastName="+userLN+"&Password=12345&Email="+email+"&Class="+classNameAR+"&Language="+Language+"&Link="+CorpUrl+"&UseNameMapping=N&CreateClass="+createClass+"&UserType="+userTypeParam; 
							
		webDriver.openUrl(regInsertUrl);
		pageHelper.skipOptin();
		String studentId = dbService.getUserIdByUserName(userName, instID);
		//homePage = new NewUxHomePage(webDriver, testResultService);				
		return studentId;
	}

}
