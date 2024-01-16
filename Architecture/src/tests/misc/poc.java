package tests.misc;

import java.util.ArrayList;
import java.util.List;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.search.SubjectTerm;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.json.JSONObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import pageObjects.GenericPage;
import pageObjects.RecordPanel;
import pageObjects.edo.NewUXLoginPage;
import pageObjects.edo.NewUxDragAndDropSection;
import pageObjects.edo.NewUxDragAndDropSection2;
import pageObjects.edo.NewUxLearningArea;
import pageObjects.edo.NewUxLearningArea2;
import pageObjects.edo.NewUxMessagesPage;
import pageObjects.edo.NewUxStudyPlanner;
import pageObjects.tms.TeacherReadMessagePage;
import pageObjects.tms.TeacherWriteMessagePage;
import pageObjects.tms.TmsHomePage;
import services.MailService;
import services.PageHelperService;
import tests.edo.newux.BasicNewUxTest;
import tests.edo.newux.SpeechRecognitionBasicTestNewUX;
import tests.edo.newux.testDataValidation;
import Enums.ByTypes;
import Enums.FeaturesList;
import Enums.TaskTypes;
import Interfaces.TestCaseParams;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFileOutputStream;



public class poc extends BasicNewUxTest {

	private String name;
	private static final String OLD_DEFAULT_BANNER_GIF = "old_default_banner.gif";
	private static final String RECORDING_WAV = "recording.wav";
	//private static final String CI_Folder = "smb://CI-SRV//ApplicationEnvironments//";
	private static final String RECORDINGS_FOLDER = "smb://frontqa2016//SharedUpload//Attachments//"+PageHelperService.recFolder+"//Recordings//";
	
	NewUxLearningArea2 learningArea2;
	NewUxDragAndDropSection dragAndDrop;
	NewUxDragAndDropSection2 dragAndDrop2;
	NewUXLoginPage loginPage;
	NewUxMessagesPage inboxPage;
	TmsHomePage tmsHomePage;
	TeacherReadMessagePage tRead;
	TeacherWriteMessagePage tWrite;
		
	@Before
	public void setup() throws Exception {
		super.setup();
		
		
	}
		
	
	@Test
	public void createProgressForClassStudents() throws Exception {
		
		String instId = "5233217"; 
		String className = "progress";
		String courseId	= "20000";
		boolean submitTests = false; // if false - progress made without tests 
		
		pageHelper.createProgressForClassStudents(instId, className, courseId, submitTests);
		
		
	}
	
	@Test
	public void testWebApi() throws Exception {
		
		testDataValidation testData = new testDataValidation();
		
		String request = "https://edux.qa.com/WebApi/Login/st5/12345/5233217";
		//String response = netService.getXmlResponseContentByUrl(request);
		//String response = netService.sendHttpRequest(request, "GET", true, null, null);
		
		JSONObject JSONObject = netService.sendServiceRequestWebApi(request);
		String key = "StudentID";
		String expectedValue = "52332170000007";
		
		testData.verifyJsonValueByKey(JSONObject, key, expectedValue);
		
		sleep(2);
			

	}

	@Test
	public void testWebConfigChange() throws Exception {
						
		//pageHelper.setSessionTimeoutForED(3);
		pageHelper.linkEdTmsQaBuildsInCI_CMD();
					
	}	
	
	@Test
	public void testConfigFileChange() throws Exception {
						
		pageHelper.setFeaturesListInAppSetByInstitution("5232282", FeaturesList.learningAsAng, "true", false);
	}	
	
	
	@Test
	public void createRecordingED() throws Exception {
		
		
		
		//NewUxStudyPlanner sPlanner = new NewUxStudyPlanner(webDriver, testResultService);
		
		String studentId = "52320670000002";  
		
		report.startStep("Init test data");
		//String courseName = coursesNames[2];
		String unitId = "20028";
		String unitName = dbService.getUnitNameById(unitId); 
		String compId = "20483";
		String compName = dbService.getComponentNameById(compId);
		String subCompId = "1";
		String segmentId = "2";
		String resName = "s1svae";
		//String resName = null;
		String autoScore = "91";
		String segmentText = "Does the rent include utilities?";
		
		report.startStep("Create and login to ED as student");
		
		//classNameSR = configuration.getProperty("classname.openSpeech");
		//String institutionId = configuration.getProperty("institution.id");
		//studentId = pageHelper.createUSerUsingSP(institutionId, classNameSR);
		//userNameSR = dbService.getUserNameById(studentId, institutionId);
		//loginPage = new NewUXLoginPage(webDriver,testResultService);
		//homePage = loginPage.loginAsStudent(userNameSR, "12345");
				
		report.startStep("Register student recording in DB");
		//simulate recording sending to teacher & saving as old SR - IE method with wav to mp3 convert
		String recId = pageHelper.sendRecordingToTeacherSR(studentId, unitId, compId, subCompId, segmentId, autoScore,resName);
		
				
		report.startStep("Create / check if user folder exists in Recordings");
		textService.getCreateFolderInPath(RECORDINGS_FOLDER, studentId);
		sleep(1);
		
		report.startStep("Copy and rename the wav recording to user folder");
		String source = "smb://" + configuration.getGlobalProperties("logserverName") + "//AutoLogs//ToolsAndResources//testFiles//" + RECORDING_WAV;
		String destination = RECORDINGS_FOLDER + studentId + "//"+ recId+ ".wav";
		textService.copyFileToFolder(source, destination, true, netService.getDomainAuth(), netService.getAuth());
		sleep(1);
						
		report.startStep("Update student recording status in DB and check it converted");
		Boolean isConverted = pageHelper.updateSaveRecordingAndCheckConvert(recId, studentId);
		if (!isConverted) {
			testResultService.addFailTest("Recording not saved or not converted", true, false);
		}
				
		/*report.startStep("Click on My Info btn and check Study Planner link");
		homePage.clickOnUserAvatar();
		sleep(1);
		homePage.checkTextAndclickOnStudyPlanner(2);
		homePage.switchToStudyPlanner();
		sPlanner.verifyStudyPlannerHeader();
		webDriver.waitForElement("//a", ByTypes.xpath).click();
		webDriver.switchToNewWindow();
		webDriver.switchToFrame("baseIFRAr");
		webDriver.waitForElement("//input[@id='slctLevel']", ByTypes.xpath).click();
		webDriver.switchToNewWindow();
		webDriver.waitForElement("navBtn", ByTypes.id).click();
		webDriver.switchToFrame("baseIFRAr");
		webDriver.waitForElement("//tr[@id='startd']//input[@id='calcArea']", ByTypes.xpath).click();
		
		//webDriver.waitForElement("Next",ByTypes.linkText).click(); //click next month
		webDriver.waitForElement("10",ByTypes.linkText).click(); //click day
		
		
		WebElement dateBox = webDriver.waitForElement("//div[contains(@class,'datepicker')]", ByTypes.xpath);
		
		//dateBox.sendKeys("10052016");
				
		dateBox.sendKeys("10/05/2016");
		
		webDriver.waitForElement("", ByTypes.xpath).isEnabled();*/
			
		sleep(3);

	}
	
	@Test
	public void deleteRecordings() throws Exception {
				
	
		String studentId = "5232282000034";  
		
		report.startStep("Init test data");
		//String courseName = coursesNames[2];
		String unitId = "20028";
		String unitName = dbService.getUnitNameById(unitId); 
		String compId = "20483";
		String compName = dbService.getComponentNameById(compId);
		String subCompId = "1";
		String segmentId = "2";
		String resName = "s1svae";
		//String resName = null;
		String autoScore = "91";
		String segmentText = "Does the rent include utilities?";
		
						
		report.startStep("Register student recording in DB");
		//simulate recording sending to teacher & saving as old SR - IE method with wav to mp3 convert
		String recId = pageHelper.sendRecordingToTeacherSR(studentId, unitId, compId, subCompId, segmentId, autoScore,resName);
		
				
		report.startStep("Create / check if user folder exists in Recordings");
		textService.getCreateFolderInPath(RECORDINGS_FOLDER, studentId);
		sleep(1);
		
		report.startStep("Copy and rename the wav recording to user folder");
		String source = "smb://" + configuration.getGlobalProperties("logserverName") + "//AutoLogs//ToolsAndResources//testFiles//" + RECORDING_WAV;
		String destination = RECORDINGS_FOLDER + studentId + "//"+ recId+ ".wav";
		textService.copyFileToFolder(source, destination, true, netService.getDomainAuth(), netService.getAuth());
		sleep(1);
						
		report.startStep("Update student recording status in DB and check it converted");
		Boolean isConverted = pageHelper.updateSaveRecordingAndCheckConvert(recId, studentId);
		if (!isConverted) {
			testResultService.addFailTest("Recording not saved or not converted", true, false);
		}
			
		report.startStep("Delete student recordings folder");
		
		textService.getDeleteFolderInPath(RECORDINGS_FOLDER, studentId +"//");
		sleep(1);

	}
	
	@Test
	public void enterTMS() throws Exception {
		
				
		report.startStep("Open New UX Login Page");
		NewUXLoginPage loginPage = new NewUXLoginPage(webDriver, testResultService);
		pageHelper.setUserLoginToNull(dbService.getUserIdByUserName(configuration.getProperty("teacher.username"),configuration.getProperty("institution.id")));

		report.startStep("Login as Teacher");
		loginPage.enterTeacherUserAndPassword();

		report.startStep("Verify user details on TMS home page");
		TmsHomePage tms = new TmsHomePage(webDriver, testResultService);
		tms.switchToMainFrame();
		tms.checkUserDetails("autoTeacher2", "autoTeacher2"); 
		
		tms.clickOnRegistration();
		tms.clickOnStudents();
		tms.selectClass("class2");
		tms.switchToTableFrame();
		tms.getStudentsFirstLastNamesListAsArray();
		sleep(3);

	}
	
	
	@Test
	public void failedTest() throws Exception {
		report.startStep("step1");
		report.report("11111111111");
		// testResultService.addFailTest("ssssds");22675

		report.finishStep();

		report.startStep("step2");
		report.report("2222222222222");
		// testResultService.addFailTest("45454545");
		report.finishStep();

		report.startStep("step3");
		report.report("33333");
		testResultService.addFailTest("45454545");
		// report.finishStep();
		;
		// webDriver.printScreen("testPrintScreen");

	}

	@Test
	public void testDict() throws Exception {
		
		dictionaryService.loadDictionaryFile("files\\dictFiles\\ES\\newUx\\newUxHomePage_ES.properties");
		String str = dictionaryService.getProperty("contact_us");
		System.out.println(dictionaryService.getProperty("contact_us"));
		int ch;
		StringBuffer strContent = new StringBuffer("");
		FileInputStream input = new FileInputStream("files\\sqlFiles\\deleteStudentProgress.txt");
		while( (ch = input.read()) != -1)
	        strContent.append((char)ch);
	 	System.out.println(strContent.toString());
		sleep(1);
		
	}

	@Test
	public void navigateToTask() throws Exception {
		
		getUserAndLoginNewUXClass();
		
		dragAndDrop2 = new NewUxDragAndDropSection2(webDriver, testResultService);
		
		String[] words = new String[] { "verb", "a necklace", "a car", "a suitcase", "some money", "a woman", "a man", "a bank", "a store", "a wallet" };
						
		learningArea2 = homePage.navigateToTask("B2", 10, 1, 5, 2);
		
		//dragAndDrop2.dragAndDropCloseAnswerByTextToTarget(words[0], 1);
		dragAndDrop2.dragAndDropAnswerByTextToTarget(words[0], 2, TaskTypes.Matching);
		
		sleep(1);
		
	}

	@Test
	public void makeDir() throws Exception {
		// textService.getCreateFolderInPath("smb://CI-SRV//ApplicationEnvironments//WebUX_CI_20150503.3//Institutions",
		// "22222");
		dbService.getComponentDetailsByUnitId("22226");
	}

	@Test
	public void startJenkinsJob() throws Exception {
		netService
				.sendHttpRequest(
						"http://newjenkins:8080/view/New%20UX/job/new%20ux%20-%20chrome/build?token=CI12345",
						"POST", false, "omer", "tamar2010");
	}

	@Test
	@TestCaseParams(testCaseID = { "21303" })
	public void updateTestResults() throws Exception {
		testResultService.assertEquals(true, false);
	}

	@Test
	@TestCaseParams(testCaseID = { "" })
	public void setTimeOnLesson() throws Exception {
		
		studentService.setTimeOnLesson("52332170000182", "43396", "60", 5);
		
	}

	@Test
	public void testSP() throws Exception {

		String sp = "exec UpdateSuccessRecognitionData 12345, 'success'";
		dbService.runStorePrecedure(sp);

	}

	@Test
	@TestCaseParams(testCaseID = { "45454545" })
	public void testNavigfate() throws Exception {
		Assert.assertEquals("Impersonal Statements: Empty Subject",
				"Impersonal Statements: Empty Subject");

	}

	@Test
	@TestCaseParams(testCaseID = { "11111" }, testTimeOut = "40")
	public void getProgress() throws Exception {
		// System.out.println(studentService.calcSubComponentExpecteProgress(
		// "21008","52322190004498"));

		// String studentId = pageHelper.createUSerUsingSP();
		// studentService.setProgressForComponents("20453", "20000", studentId,
		// null, 60, 2);
		// studentService.setProgressForUnit("20023", "20000",
		// studentId,null,60,3);

		// System.out.println(studentService.calcComponentExpectedProgress(
		// "20453", studentId));

		// studentService.setProgressForUnit("20023", "20000",
		// "52322260000006",null,60,0);

		// studentService.submitTestsForCourse(pageHelper.createUSerUsingSP(),
		// "20191");
		// dbService.getCourseItems("20185", false);
		// studentService.setProgressForCourse("20000", "52322260000015", null,
		// 60); 21694

		System.out.println(studentService.calcSubComponentExpecteProgress(
				"6167", "52322190006774"));
		;
		// studentService.submitTest(studentId, unitId, componentId, items);
		// studentService.submitTestsForCourse(students[i], courseId);

		;
	}

	@Test
	@TestCaseParams(testCaseID = { "423432" }, ignoreTestTimeout = true)
	// @Category(ignoredTests.class)
	public void getStudetnCourses() throws Exception {
		// dbService.getStudentAssignedCourses("52322190000838");

		String userId = pageHelper.createUSerUsingSP();
		studentService.setProgressForCourse("20000", userId, null, 60);
	}

	public void test(int a, int b, int c) {
		test(a, b, c, 0);
	}

	public void test(int a, int b, int c, int d) {

	}

	@Test
	public void testEmailAPI() throws Exception {

		
		//mailService.sendMail("igors@edusoftlearning.com", "igors@edusoftlearning.com", "test", "test");
		
		//String HOST = "10.1.0.246";
		//String HOST = "smtp.office365.com";
		String HOST = "smtp.office365.com";
		
		/* String USER = "omers@edusoftlearning.com";
        String PASSWORD = "Tamar2018$";*/
        
        String USER = "igors@edusoftlearning.com";
        String PASSWORD = "Fuxo6604";
        String PORT = "587";
        //String PASSWORD = "Sazonki**";
		
        Properties props = new Properties();
		//create properties field
		//Properties props = System.getProperties();
		        
		/*//IMAP
		props.put("mail.store.protocol", "imap");
        props.put("mail.imap.host", HOST);
        props.put("mail.imap.port", PORT);
        //props.put("mail.imap.port", PORT);
        props.put("mail.imap.starttls.enable", "true");*/
        
        /*//POP3
        //props.put("mail.store.protocol", "pop3");
        props.put("mail.pop3.host", HOST);
        //props.setProperty("mail.pop3.port", PORT);
        props.put("mail.pop3.port", PORT);
        //props.put("mail.pop3.starttls.enable", "true");
        // props.put("mail.smtp.user", USER);

        props.put("mail.pop3.socketFactory.port", PORT);
        props.put("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.pop3.socketFactory.fallback", "false");*/
       
       
        //SMTP
        props.put("mail.smtp.host", HOST);
        props.put("mail.smtp.port", PORT);
        props.put("mail.smtp.user", USER);

        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.debug", "true");

        props.put("mail.smtp.socketFactory.port", PORT);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
               
        Session session = Session.getDefaultInstance(props,null);
      //Session session = Session.getInstance(props, null);
        
        session.setDebug(true);
                  
        //Store store = session.getStore();
        //Store store = session.getStore("smtp");
        //Store store = session.getStore("imap");
        
        /*Store store = session.getStore("smtp");
        store.connect(HOST, USER, PASSWORD);*/
      
        
        Transport transport = session.getTransport("smtp");
        transport.connect(HOST, USER, PASSWORD);
        
        
        sleep(3);
        
            /*Folder folder = store.getFolder("INBOX");
            folder.open(Folder.READ_WRITE);

            System.out.println("Total Message:" + folder.getMessageCount());
            System.out.println("Unread Message:"
                    + folder.getUnreadMessageCount());
            
            Message[] messages = null;
            boolean isMailFound = false;
            Message mailFromGod= null;

            //Search for mail from God
            for (int i = 0; i < 5; i++) {
                messages = folder.search(new SubjectTerm(
                        "Welcome Test"),
                        folder.getMessages());
                //Wait for 10 seconds
                if (messages.length == 0) {
                    Thread.sleep(10000);
                }
            }

            //Search for unread mail from God
            //This is to avoid using the mail for which 
            //Registration is already done
            for (Message mail : messages) {
                if (!mail.isSet(Flags.Flag.SEEN)) {
                    mailFromGod = mail;
                    System.out.println("Message Count is: "
                            + mailFromGod.getMessageNumber());
                    isMailFound = true;
                }
            }

            //Test fails if no unread mail was found from God
            if (!isMailFound) {
                throw new Exception(
                        "Could not find new mail from God :-(");
            
            //Read the content of mail and launch registration URL                
            } else {
                String line;
                StringBuffer buffer = new StringBuffer();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(mailFromGod
                                .getInputStream()));
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                System.out.println(buffer);

                //Your logic to split the message and get the Registration URL goes here
                String registrationURL = buffer.toString().split("&amp;gt;http://www.god.de/members/?")[0]
                        .split("href=")[1];
                System.out.println(registrationURL);                            
            }*/
     }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@After
	public void tearDown() throws Exception {
		//studentService.setProgressInFirstComponentInUnit(0, studentId, "20000");
		super.tearDown();

	}
	
	
}
