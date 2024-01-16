package pageObjects.tms;

import java.util.List;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import Enums.ByTypes;
import drivers.GenericWebDriver;
import pageObjects.GenericPage;
import pageObjects.edo.NewUXLoginPage;
import pageObjects.edo.NewUxHomePage;
import services.TestResultService;
import tests.edo.newux.BasicNewUxTest;

public class TeacherDashboard extends GenericPage {

	//@FindBy(className = "elementTable")
	//public WebElement singleClass;
	
	@FindBy(className = "body")
	public WebElement classList;
	
	@FindBy(className = "custom-label")
	public WebElement classCheckList; 
	
	@FindBy(className = "firstElement")
	public WebElement classElemnt;
	
	@FindBy(xpath ="text-gray-200 font-semibold  className1 text-xl") 
	public WebElement classNameUI;
	
	@FindBy(xpath ="//*[@id='root']/div[2]/div[1]/div[2]/div/div[3]") 
	public WebElement weeksAreaSquares;

	@FindBy(className = "title px-2 text-gray-300 font-bold")
	public WebElement weeksCount;
	 
	
	@FindBy(xpath ="//*[contains(@class,'card') and [contains(text(),'total student']")
	public WebElement totalStudentTDElement;

	@FindBy(className ="mainText")
	public List<WebElement> classCompletionAndAverageTDElement;	
	
	@FindBy(className = "calloutLabel")
	public WebElement classEIC;
	
	@FindBy(className = "text-gray-200 font-semibold  className1 text-xl")
	public List<WebElement> classesName;
	
	@FindBy(xpath = "//*/visual-container-repeat/visual-container[1]/transform/div/div[3]/div/visual-modern/div/svg/g[1]/text/tspan")
	public WebElement currentClassesFromTopWidget;
	
	@FindBy(xpath = "//strong[contains(@class,'text-xs md:text-xs lg:text-xs xl:text-sm 2xl:text-lg')]")
	public List<WebElement> MidTermAndFinalTestScore_UI;
	
	
	
	// not relevant to dashboard project
	@FindBy(tagName = "h1")
	public WebElement mainTitle;
	
	@FindBy(name = "tRecGrd")
	public WebElement recordGrade;
			
	@FindBy(how = How.ID, using = "AutoScore")
	public List<WebElement> autoScore;	
	
	@FindBy(how = How.XPATH, using = "//div[@id='RatePanel']//h4")
	public List<WebElement> scoreCategories;
		
	/*@FindAll(@FindBy(className = "magazine__articleItemW"))
	public List <WebElement> magazineArticles;*/
	
	
	protected NewUxHomePage homePage;

	// end of not relevant to teacher dashboard
	
	public TeacherDashboard(GenericWebDriver webDriver,TestResultService testResultService) throws Exception {
		super(webDriver,testResultService);
		PageFactory.initElements(webDriver.getWebDriver(), this);
		
		// TODO Auto-generated constructor stub
	}

	@Override
	public GenericPage waitForPageToLoad() throws Exception {
		//webDriver.waitForElement("Student's Recordings", ByTypes.linkText);
		return this;
	}

	@Override
	public GenericPage OpenPage(String url) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public String getTotalStudent_TeacherDashboard(){
		//WebElement element=null;
		String totalStudent="";
		
		try {
			webDriver.switchToTopMostFrame();
			//webDriver.waitUntilElementAppears(totalStudentTDElement, 5);
			
			List<WebElement> frame = webDriver.getElementsByXpath("//*[contains(@src,'https://app.powerbi.com/reportEmbed')]");
			webDriver.switchToFrame(frame.get(0));
			
			WebElement e = webDriver.waitForElement("//*/visual-modern/div/svg/g[1]/text/tspan"
					,ByTypes.xpath,false,1);
			//WebElement g = webDriver.getChildElementByXpath(e, "tspan");
			//WebElement total = webDriver.getChildElementByXpath(g, "tspan");
			//e.getTagName();
			totalStudentTDElement.getText();
			totalStudent= totalStudentTDElement.getAttribute("aria-label");
					//getText();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
				
		}
	
		return totalStudent;
	}
	
	public String getClassEIC_TeacherDashboard(){
		
		webDriver.switchToTopMostFrame();
		String ClassEIC_Text="";
		try{

			List<WebElement> elements = webDriver.getElementsByXpath("//*[contains(@src,'powerbi')]");
					
			webDriver.switchToFrame(elements.get(0));
			webDriver.switchToFrame("visual-sandbox");
			
			webDriver.waitUntilElementAppears(classEIC, 10);
			
			ClassEIC_Text = classEIC.getText();
		
		}
		catch (Exception e) {
			try {
				Thread.sleep(5000);
				ClassEIC_Text = classEIC.getText();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		
		return ClassEIC_Text;
	}
	
	public String getClassAverageCompletion_TeacherDashboard(){
		
		String classAvgCompletion=null;
		swithToClassCompletionAndAverageFrame();
		
		try{
			webDriver.waitUntilElementAppears(classCompletionAndAverageTDElement, 10);
			classAvgCompletion= classCompletionAndAverageTDElement.get(0).getText();	
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return classAvgCompletion;
	}
	
	public String getClassAverageLessonScore_TeacherDashboard(){
		
		String classAvgLessonScore=null;
		
		swithToClassCompletionAndAverageFrame();
		try{
			webDriver.waitUntilElementAppears(classCompletionAndAverageTDElement, 10);
			classAvgLessonScore= classCompletionAndAverageTDElement.get(1).getText();	
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return classAvgLessonScore;
	}
	
	private void swithToClassCompletionAndAverageFrame() {
		
		webDriver.switchToTopMostFrame();
		
		List<WebElement> elements;
		try {
			elements = webDriver.getElementsByXpath("//*[contains(@src,'powerbi')]");
			webDriver.switchToFrame(elements.get(1));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public List<String[]> getTeachersEligableListToSeeDashboard(){
		
		dbService.setUsingDWDB(true);
		
		String sql = "select td.UserId, COUNT(*) ClassesPerTerm, td.TeachersDimId, cd.InstitutionTermDimId"
				+ " FROM TeacherClassesDim tcd"
				+ " join"
				+ " TeachersEligibleToSeeDashboard ted on ted.TeachersDimId = tcd.TeachersDimId"
				+ " join"
				+ " ClassDim cd on tcd.ClassDimId = cd.ClassDimId"
				+ " join"
				+ " TeachersDim td on tcd.TeachersDimId = td.TeachersDimId"
				+ " join"
				+ " InstitutionTermsDim itd on cd.InstitutionTermDimId = itd.InstitutionTermDimId"
				+ " where GETUTCDATE() between itd.TermStartDate and itd.TermEndDate"
				+ " AND EXISTS(SELECT 1 FROM StudentDim st WHERE st.ClassDimId = tcd.ClassDimId)"
				+ " GROUP BY td.UserId, td.TeachersDimId, cd.InstitutionTermDimId"
				+ " ORDER BY UserId,ClassesPerTerm";
				
		List<String[]> teacherList = null;
		
		try {
			teacherList = dbService.getStringListFromQuery(sql, 1,true);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		finally {
			dbService.setUsingDWDB(false);
		}
		return teacherList;
		
	}
	
	
	public List<WebElement> getWeeksChildCount() {
		
		WebElement element=null;
		List<WebElement> totalWeekChild=null;
		
		
		try{
			element = webDriver.waitUntilElementAppears("totalWeeks",ByTypes.className, 5);
			
			if (element !=null)
				totalWeekChild = webDriver.getChildElementsByXpath(element, "label");
		}
		
		catch (Exception e) {
			// TODO: handle exception
		}
		return totalWeekChild;	
	}
	
	public String getTotalStudentInClassFromDb(String classDimId){
		
		dbService.setUsingDWDB(true);
		
		String sql = "select distinct count(StudentDimId) TotalStudentInClass"
				+ " from StudentDim"
				+ " where ClassDimID =" +classDimId+""
				;
				
		String totalStudentInClass = null;
		
		try {
			totalStudentInClass = dbService.getStringFromQuery(sql, 1,true);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		finally {
			dbService.setUsingDWDB(false);
		}
		return totalStudentInClass;
		
	}

	
	public List<String[]> getStudentStatusFromDB(String classDimId){
		
		dbService.setUsingDWDB(true);
		
		String sql = "Drop table if exists #InputProgress"
				+ " select distinct sd.StudentDimId"
				+ " ,CASE"
				+ " WHEN (ISNULL(cpf.TestAvg,0) + ISNULL(cpf.progress,0))/2 between 0 and 50 THEN 'lagging behind'"
				+ " WHEN (ISNULL(cpf.TestAvg,0) + ISNULL(cpf.progress,0))/2 between 51 and 75 THEN 'On Target'"
				+ " WHEN (ISNULL(cpf.TestAvg,0) + ISNULL(cpf.progress,0))/2 between 76 and 100 THEN 'Working ahead'"
				+ " END AS StudentStatus"
				+ " into #InputProgress"
				+ " from CourseProgressFact as cpf"
				+ " join StudentDim as sd"
				+ " on sd.StudentDimID = cpf.StudentDimId and sd.ClassDimID ="+classDimId+""
				+ " join ClassMainCourse as cmc on cpf.CourseDimID = cmc.CourseDimID and cpf.ClassDimID ="+classDimId+""
				+ " where sd.ClassDimID ="+classDimId+""
				+ " order by StudentStatus"
				+ " select count(StudentDimId) as StudentCount,StudentStatus"
				+ " from #InputProgress"
				+ " group by StudentStatus"
				+ " Drop table if exists #InputProgress"
				;
				
		List<String[]> studentStatus = null;
		
		try {
			studentStatus = dbService.getStringListFromQuery(sql, 1,true);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		finally {
			dbService.setUsingDWDB(false);
		}
		return studentStatus;
		
	}

	
	public List<String[]> getTeacherCredentialToLoingFromDB(){
		
		dbService.setUsingDWDB(true);
		
		String sql = "select id.CannonicalDomain,td.UserName,ed_users.password,td.UserId,id.institutionid"
				+ " from TeachersEligibleToSeeDashboard as ted"
				+ " join"
				+ " TeachersDim as td on ted.TeachersDimId = td.TeachersDimId"
				+ " join"
				+ " InstitutionDim as id on td.InstitutionDimID = id.InstitutionDimID"
				+ " join"
				+ " [EDMerge].[dbo].[USERS] as ed_users on td.userid = ed_users.userid"
				+ " Order by td.UserId"
				;
				
		List<String[]> teacherList = null;
		
		try {
			teacherList = dbService.getStringListFromQuery(sql, 1,true);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		finally {
			dbService.setUsingDWDB(false);
		}
		return teacherList;
		
	}

	public NewUxHomePage getTeacherAndLoginToTeacherDashboardByTMS(){
		
		dbService.setUsingDWDB(true);
		
		String sql = "select top 10 id.CannonicalDomain,td.UserName,ed_users.password,td.UserId,id.institutionid"
				+ " from TeachersEligibleToSeeDashboard as ted"
				+ " join"
				+ " TeachersDim as td on ted.TeachersDimId = td.TeachersDimId"
				+ " join"
				+ " InstitutionDim as id on td.InstitutionDimID = id.InstitutionDimID"
				+ " join"
				+ " [EDMerge].[dbo].[USERS] as ed_users on td.userid = ed_users.userid AND ed_users.login ='2000-01-01 00:00:00.000'"
				+ " Order by td.UserId"
				;
				
		List<String[]> teacherList = null;
		
		try {
			teacherList = dbService.getStringListFromQuery(sql, 1,true);
			
			Random random = new Random();
			int i = random.nextInt(teacherList.size());
			
			String currentUrl = teacherList.get(i)[0];
			String userName = teacherList.get(i)[1];
			String password = teacherList.get(i)[2];
			String studentId = teacherList.get(i)[3];
			BasicNewUxTest.institutionId = teacherList.get(i)[4];
			
			NewUXLoginPage loginPage = new NewUXLoginPage(webDriver, testResultService);
			
			loginPage.loginAsStudent(userName, password);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		finally {
			dbService.setUsingDWDB(false);
		}
		return homePage;
		
	}

public String getClassDimIdByClassName(String institutionId,String className){
		
		
		String realClassName="";
		
		//className = className.toUpperCase();
		
		if (className.contains(" P ")){
			realClassName = className.replace(" P ", " P.");
		}
		else if (className.contains("IU")){
			realClassName = className.replace(" ", "-");
		}
		else if (className.contains(" UDLA ")){
			realClassName = className.replace(" ", ".");;
		}
		
		String sql = "select ClassDimID"
				+ " from ClassDim"
				+ " where ClassName ='"+realClassName+"'"
				+ " and InstitutionId ="+institutionId+""
				;
				
		String classDimId = null;
		
		try {
			dbService.setUsingDWDB(true);
			classDimId = dbService.getStringFromQuery(sql, 1,true);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		finally {
			dbService.setUsingDWDB(false);
		}
		return classDimId;
		
	}


	public void switchToTeacherDashboardFrame() {
		
		try {
			//webDriver.switchToFrame("frameSet");
			webDriver.switchToFrame("teacherDashboardBIFrame");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void compareTotalStudentInClass(String classDimId,int classIndex) {
		
		List<WebElement> classNameElement = null;
		//String classDimId=null;
		
		try {
			
			WebElement element = webDriver.waitForElement("//*[contains(@class,'firstElement')]", ByTypes.xpath,false,1);
			classNameElement = webDriver.getChildElementsByXpath(element, "span");
			

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String totalStudentInClassFromDB = getTotalStudentInClassFromDb(classDimId);
		//String  totalStudentInClassFromUI = getTotalStudent_TeacherDashboard();
		//assertEquals(totalStudentInClassFromDB, totalStudentInClassFromUI,"Total Student count in class is wrong");
		
	}

	public String getclassFromDB(String classDimId, int week) {
		// TODO Auto-generated method stub
		String classEICFromDB=null;
		
		String sql = "Select "
				;
		
		
		return classEICFromDB;
	}

	public List<String[]> getTeacherClassesDimId(){
		
		String sql = "select td.TeachersDimId,tcd.ClassDimId"
			+ " from dbo.TeachersEligibleToSeeDashboard as ted"
			+ " join"
			+ " TeachersDim as td on ted.TeachersDimId = td.TeachersDimId"
			+ " join"
			+ " TeacherClassesDim as tcd on td.TeachersDimId = tcd.TeachersDimId"
			+ " join ClassDim as cd on cd.ClassDimID = tcd.ClassDimId /* and cd.Teachers = td.LastName + ' ' + td.firstName */"
			+ " and cd.InstitutionTermDimId is not null"
			+ " join"
			+ " pwbi.SettingsClassScoreUsage csuon on cd.ClassDimId = csuon.ClassDimId"
			+ " and tcd.TeachersDimId = csuon.TeacherDimId"
			+ " Order By td.TeachersDimId, tcd.ClassDimId"
			+ " --join CourseProgressFact cpf on cd.ClassDimID = cpf.ClassDimId"
			;
		
		//String sql = "Select * FROM [dbo].[TeachersEligibleToSeeDashboard]";
		List<String[]> TeacherDimAndClasses = null;
		
		try {
			dbService.setUsingDWDB(true);
			TeacherDimAndClasses = dbService.getStringListFromQuery(sql, 1, true);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			dbService.setUsingDWDB(false);
		}
		return TeacherDimAndClasses;
	}
	
	
	public String[] getClassesNameFromList() {
		
		List<WebElement> classesElement=null;
		//ArrayList<String> list = new ArrayList<String>();
		String[] classesName = null;
		
		try {
			classesElement = webDriver.getElementsByXpath("//span[contains(@class,'text-gray-200 font-semibold  className1 text-xl')]");
			classesName = new String[classesElement.size()];
			
			if (classesElement != null){		
				for (int i=0; i<classesElement.size();i++){
					classesName[i] = classesElement.get(i).getText(); 
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return classesName;
	}

	public List<String[]> getExpectedClassProgressBeforeClaculationFromDB(String teacherDimId, String classDimId, int spMode) {
	
		String sp = "EXEC pwbi.Dashboard_QA_DBO @TeacherDimId = " + teacherDimId +",@ClassDimId = " +classDimId+ ",@TermWeekNum = " + spMode;		
		List<String[]> classProgrerssFromDB = null;
		
		try {
			
			dbService.setUsingDWDB(true);
			classProgrerssFromDB = dbService.getStringListFromQuery(sp, 0, true);
	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			dbService.setUsingDWDB(false);
		}
		
		return classProgrerssFromDB;
	}

	public List<String[]> getactualClassProgressAfterClaculationFromDB(String teacherDimId, String classDimId, int mode) {
	
		String sp = "EXEC pwbi.Dashboard_QA_PWBI @TeacherDimId = " + teacherDimId +",@ClassDimId = " +classDimId+ ",@TermWeekNum = " + mode;				
		List<String[]> classProgrerssFromDB = null;
		
		try {
			
			dbService.setUsingDWDB(true);
			classProgrerssFromDB = dbService.getStringListFromQuery(sp, 0, true);
	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			dbService.setUsingDWDB(false);
		}
		
		return classProgrerssFromDB;
	}

	
	public List<String[]> getRandomTeacherListFromAllInstitutions() {
		
		String sp = "EXEC pwbi.Dashboard_QA_GetRandomTeachers";		
		List<String[]> randomTeacherListFromAllInstitutions = null;

		try {
			
			dbService.setUsingDWDB(true);
			randomTeacherListFromAllInstitutions = dbService.getStringListFromQuery(sp, 0, true);
	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			dbService.setUsingDWDB(false);
		}
		
		return randomTeacherListFromAllInstitutions;
	}
	
	public String getCurrentClassNameFromTopWidget() {
		
		String currentClassName="";
		webDriver.switchToTopMostFrame();
		
		try{

			List<WebElement> elements = webDriver.getElementsByXpath("//*[contains(@src,'powerbi')]");
					
			webDriver.switchToFrame(elements.get(1));
			//webDriver.switchToFrame("visual-sandbox");
			
			//webDriver.waitUntilElementAppears(currentClassesFromTopWidget, 2);
			WebElement e = webDriver.waitForElement("//*[contains(@class,'visual visual-card allow-deferred-rendering')])",
				//	div/div[3]/div/visual-modern/div/svg/
					//	explore-canvas/div/div[2]/div/div[2]/div[2]/visual-container-repeat
					//g[1]/text/tspan",	
					ByTypes.xpath, 1, false);
			
			currentClassName = currentClassesFromTopWidget.getTagName();
					currentClassesFromTopWidget.getText();
			
		}
		catch (Exception e) {
			e.printStackTrace();	
		}
		
		return currentClassName;
	}

	
	public String[] getMidTermAndFinalTestSCoreFromUI() {
		
		List<WebElement> elements = null;
		
		String[] score = new String[2];
		
		webDriver.switchToTopMostFrame();
		try{
			elements = MidTermAndFinalTestScore_UI;
			
			for (int i=0;i<elements.size();i++){
				score[i] = elements.get(0).getText();
				score[i] = score[i].replace("%", "");
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return score;
	}

	public void compareRetreivedDataFromDB(List<String[]> expecTedteacherClassProgress,
			List<String[]> actualTeacherClassProgress,
			String teacherDimId, String ClassDimId,String[] fields) {

		// compare all weeks data retrieved from DB per teacherDimId and classDimId
		for (int weekIndex = 0; weekIndex < expecTedteacherClassProgress.size(); weekIndex++){
			
			// compare all data fields retrieved from DB
			for (int fIndex=0; fIndex<fields.length;fIndex++){
				
				// check the gap between the values
				boolean mostEqual = checkResultNoGapOf2
						(expecTedteacherClassProgress.get(weekIndex)[fIndex]
						, actualTeacherClassProgress.get(weekIndex)[fIndex]);
				
				try {
					testResultService.assertEquals(true, mostEqual,
							"TheacherDimId="+teacherDimId+
							", ClassDimId=" +ClassDimId+ ",Week Index=" +(weekIndex+1)+ ", Field= " + fields[fIndex]+ ""
									+ " ,Expected DBO was: " + expecTedteacherClassProgress.get(weekIndex)[fIndex]+ 
									" and Actual PWBI was:" + actualTeacherClassProgress.get(weekIndex)[fIndex] ,false);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				/*
				testResultService.assertEquals
					(expecTedteacherClassProgress.get(0)[fIndex],
					actualTeacherClassProgress.get(0)[fIndex],
					"The: " + fields[fIndex]+" value doesn't match.");
				*/ 
			}
		}
		
	}
	
	public boolean checkResultNoGapOf2(String expected_dbValue,String actual_uiValue) {
		
		boolean mostEqual = false;
		
		boolean uiBigger= false;
		boolean dbBiggerThan1 = false;
		float db;
		float ui;
		
		// check no null and convert the value to float
		if (expected_dbValue != null)
			db = Float.parseFloat(expected_dbValue);
		else 
			db=0;
		
		if (actual_uiValue != null)
			ui = Float.parseFloat(actual_uiValue);
		else
			ui=0;
		
		// make a calculation to verify no wrong gap
		if (ui-db > 0.2)
			uiBigger= true;
						
		if (db-ui >= 2.0)
			dbBiggerThan1=true;
		
		// check the gap status
		if (uiBigger || dbBiggerThan1) // true means there is bug and false means no data diff
			mostEqual= false;
		else
			mostEqual= true;
		
		return mostEqual;
	}

}
