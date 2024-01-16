package tests.edo.newux;


import Enums.ByTypes;
import Interfaces.TestCaseParams;
import io.restassured.http.ContentType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.http.HttpClient;
import pageObjects.edo.NewUXLoginPage;
import pageObjects.edo.NewUxLearningArea;
import services.PageHelperService;
import testCategories.edoNewUX.SanityTests;
import testCategories.inProgressTests;

import java.util.List;

import static io.restassured.RestAssured.given;

//@Category(AngularLearningArea.class)
public class WebApi extends BasicNewUxTest {
	
	HttpGet get;
	HttpClient httpClient;
	NewUXLoginPage loginPage;
	NewUxLearningArea learningArea;
	String instID = "";
	String jsonData= "";
	
	@Test
	@TestCaseParams(testCaseID = { "41937" } )
	public void testWebApiGetClassesInInstitution() throws Exception {
		
		String expectedClass= "autoProgress"; //"522820628";
		String tokenUrl= "GetClassesInInstitution/" + institutionId; //5232282";
		int sleepAfterRequest=5;
		//String cookie= webDriver.getCookie("Community");
		//System.out.println(cookie.indexOf(1));
		//sleep(5);
		testApi(tokenUrl,expectedClass,sleepAfterRequest);
		
	}
	
	@Test
	@TestCaseParams(testCaseID = { "41960" } )
	public void testWebApiMoveUser() throws Exception {

		createUserAndLoginNewUXClass();
		//String institutionId = configuration.getInstitutionId();
		String userName = dbService.getUserNameById(studentId, institutionId);
		String targetClassId = dbService.getClassIdByName(configuration.getProperty("classname.caba"), institutionId);
		String expectedResult="0"; //change after sp on QA
		String tokenUrl="MoveUser/"+ institutionId + "/" + userName + "/" + targetClassId;
		int sleepAfterRequest=5;
		testApi(tokenUrl,expectedResult,sleepAfterRequest);
		String actualClassId = dbService.getUserClassId(studentId);
		
		report.startStep("Verify the User Moved to Target ClassId: " + targetClassId);
			testResultService.assertEquals(targetClassId, actualClassId, "User Not Moved to: " + targetClassId +" + ClassId");
	
	}
	
	
	@Test
	@TestCaseParams(testCaseID = { "45758" } )
	public void testWebApiGetStudentsInInstitutionDetailed() throws Exception {
		String expectedResult="UserName"; //change after sp on QA
		instID= configuration.getProperty("lau_institutionId");

		//instID="5232282"; // for debug "automation"
		
		String tokenUrl="GetStudentsInInstitutionDetailed/" + instID;
		int sleepAfterRequest=5;
		testApi(tokenUrl,expectedResult,sleepAfterRequest);
		
	}
	
	@Test
	@TestCaseParams(testCaseID = { "41940" } )
	public void testWebApiGetStudentsInClass() throws Exception {
		//String instId= institutionId;
		String className = configuration.getProperty("classname");
		
		String[] userDetails = pageHelper.getUserFromDbByInstitutionIdAndClassName
				(institutionId, className);
			   studentId = userDetails[2];
			   String classId = dbService.getClassIdByName(className, institutionId);
		String expectedResult= studentId;
		
		String tokenUrl="GetStudentsInClass/" + institutionId + "/" + classId;
		
		int sleepAfterRequest=3;
		testApi(tokenUrl,expectedResult,sleepAfterRequest);
		
	}
	
	@Test
	@TestCaseParams(testCaseID = { "41958" } )
	public void testWebApiGetStudentCourseTestAverage() throws Exception {
		String expectedResult= "First Discoveries";
		//String expectedResult= "OK";
		String tokenUrl="GetStudentCourseTestAverage/" + institutionId + "/auto2";
		int sleepAfterRequest=5;
		testApi(tokenUrl,expectedResult,sleepAfterRequest);
	
		}
	
	@Test
	@TestCaseParams(testCaseID = { "41959" } )
	public void testWebApiGetStudentCourseTimeOnTask() throws Exception {
		String expectedResult= "Minutes";
		String tokenUrl="GetStudentCourseTimeOnTask/"+ institutionId + "/auto2";
		int sleepAfterRequest=5;
		testApi(tokenUrl, expectedResult,sleepAfterRequest);
		
	}
	
	@Test
	@TestCaseParams(testCaseID = { "41943" } )
	public void testWebApiGetStudentPLTResults() throws Exception {
		 
		String expectedResult="Reading";
		String tokenUrl="GetStudentPLTResults/" + institutionId +"/auto2";
		int sleepAfterRequest=5;
		testApi(tokenUrl,expectedResult,sleepAfterRequest);
		
	}
	
	@Test
	@TestCaseParams(testCaseID = { "45096" } )
	public void testWebApiGetMatrixScore() throws Exception {
		String expectedResult="auto10";
		String tokenUrl="GetMatrixScore/" + institutionId + "/20000/522820007";
		int sleepAfterRequest=5;
		testApi(tokenUrl,expectedResult,sleepAfterRequest);
		
	}
	
	@Category(inProgressTests.class) //need SP APIGetCoursesInInstitution 
	@Test
	@TestCaseParams(testCaseID = { "43954", "50342" } )
	public void testWebApiGetCoursesInInstitution() throws Exception {
		
		String expectedCourse= "20000";
		String tokenUrl="GetCoursesInInstitution/" + institutionId; //5232282";
		int sleepAfterRequest=3;
		
		testApi(tokenUrl,expectedCourse,sleepAfterRequest);
		
	}
	
	@Test
	@TestCaseParams(testCaseID = { "41954" } )
	public void testWebApiGetStudentCourseCompletion() throws Exception {
		String expectedResult= "First Discoveries";
		//String expectedResult= "OK";
		String tokenUrl="GetStudentCourseCompletion/" + institutionId + "/auto2";
		int sleepAfterRequest=5;
		testApi(tokenUrl,expectedResult,sleepAfterRequest);
		
	}
	
	@Test
	@TestCaseParams(testCaseID = { "49006" } )
	public void testWebApiGetUnitPerformance_User() throws Exception {
		String instId= institutionId;
		List<String[]> users = dbService.getUserInClassWithProgress(instId);
		String tokenUrl="GetUnitPerformance/" + instId +"/S/" + users.get(0)[0] + "/" + users.get(0)[1];
		
		String expectedUserId= users.get(0)[0];
		
		String expectedUnitResult= users.get(0)[2];
		String expectedUnitAverageResult= users.get(0)[4];
		String expectedUnitPercentage= users.get(0)[5];
		
		String[] expectedUnitPercentageResult = expectedUnitPercentage.split("\\.");
		
		int sleepAfterRequest=5;
		testApi(tokenUrl,expectedUserId,sleepAfterRequest);
		
		checkResponceContainsExpectedresult(expectedUnitResult);
		checkResponceContainsExpectedresult(expectedUnitAverageResult);
		checkResponceContainsExpectedresult(expectedUnitPercentageResult[0]);
		checkResponceContainsExpectedresult("Midterm Test");
		checkResponceContainsExpectedresult("Final Test");
	}

	@Test
	@TestCaseParams(testCaseID = { "49006" } )
	public void testWebApiGetUnitPerformance_class() throws Exception {
		String instId= institutionId;
		List<String[]> users = dbService.getUserInClassWithProgress(instId);
		String tokenUrl="GetUnitPerformance/" + instId +"/C/" + users.get(0)[3] + "/" + users.get(0)[1];
	
		int sleepAfterRequest=5;
		
		String expectedUserId= users.get(0)[0];
		
		String expectedUnitResult= users.get(0)[2];
		String expectedUnitAverageResult= users.get(0)[4];
		String expectedUnitPercentage= users.get(0)[5];
		
		String[] expectedUnitPercentageResult = expectedUnitPercentage.split("\\.");
		
		testApi(tokenUrl,expectedUserId,sleepAfterRequest);
		
		checkResponceContainsExpectedresult(expectedUnitResult);
		checkResponceContainsExpectedresult(expectedUnitAverageResult);
		checkResponceContainsExpectedresult(expectedUnitPercentageResult[0]);
		checkResponceContainsExpectedresult("Midterm Test");
		checkResponceContainsExpectedresult("Final Test");
	}
	
	public void testApi(String tokenUrl, String expectedResult, int sleepAfterRequest) throws Exception{
		
	report.startStep("Init test data");
		
	if (institutionId.equalsIgnoreCase("")){
		institutionId = configuration.getProperty("institution.id"); // in case use default "automation" institution
	}
		//String baseUrl= "http://edapi.edusoftrd.com/WebApi/External/";
	String baseUrl = PageHelperService.edApiUrl + "/WebApi/External/";
			
		tokenUrl = baseUrl+tokenUrl;
		String ApiToken= dbService.getApiToken(institutionId);
		//sleep(2);
		OkHttpClient client = new OkHttpClient();
		int resStatusExpected=200;
		
	report.startStep("Send request");
	
		Request request = new Request.Builder()
				.url(tokenUrl)
				.get()
				.addHeader("token", ApiToken)
				.build();
		//sleep(sleepAfterRequest);
		report.report("Request for API is: " + request);
		testResultService.getFailedCauses();
		
		
		report.startStep("Get response body");
		
		//Closeable responses = null;
		okhttp3.Response responses = null;
		
		try {
			report.report("Before populating responses");
			responses = client.newCall(request).execute();
			report.report("After populating responses");
		}
		catch (Exception e) {
			//testResultService.addFailTest("Error getting response from API", false, false);
			sleep(sleepAfterRequest);
			report.report("Second attempt at getting API response");
			responses = client.newCall(request).execute();			
		}
		sleep(2);
		jsonData = responses.body().string();
		//jsonData = ((okhttp3.Response) responses).body().string();
		
		
		for (int i=0; jsonData.equalsIgnoreCase("") && i<webDriver.getTimeout(); i++){
			jsonData = ((okhttp3.Response) responses).body().string();
			sleep(1);
		}
		
		report.report("Received data from the API and check the Body, response body is: " + jsonData);
		//sleep(sleepAfterRequest);
		checkResponceContainsExpectedresult(expectedResult);
		
		report.startStep("Check the response status");
	
		//okhttp3.Response response = client.newCall(request).execute();
		//sleep(2);
		int resStatusActual = ((okhttp3.Response) responses).code(); //response.code();
		report.report("Result status from the API call is: " + resStatusActual);
		testResultService.assertEquals(resStatusActual, resStatusExpected, "Failed on WebApi: HTTP code Status wrong");
	}

	private void checkResponceContainsExpectedresult(String expectedResult) {

		 boolean response3=jsonData.contains(expectedResult);
			if (!response3)
				try {
					testResultService.addFailTest("Response Body doesn't contains the expected string:" + expectedResult, false, false);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	}

	@Test
	public void getCreateClass() throws Exception {
		String baseUrl = PageHelperService.edApiUrl + "/WebApi/External/";
		String response = "";
		try {
			response = given().headers("token", "3F19CC14-EDC1-4EBB-B8CB-84B752FA2D58", "Content-Type", ContentType.JSON, "Accept", ContentType.JSON)
					.when()
					.body("{\r\n"
							+ "  \"InstitutionId\": \"5232282\",\r\n"
							+ "  \"ClassName\": \"Igor-112\",\r\n"
							//+ "  \"PackageName\": \"FD-A3_10_Units\",\r\n"
							+ "  \"PackageName\": \"FD-A3_ED10/12m_18m\",\r\n"
							//+ "  \"StartDate\": \"20231019\"\r\n"
							+ "  \"StartDate\": \"20241231\"\r\n"
							+ "}")
					//.post("https://edapi.edusoftrd.com/webapi/external/CreateClass")
					.post("https://stgw-edapi.engdis.com/webapi/external/CreateClass")
					.then()
					.assertThat().statusCode(200)
					.extract().response().asString();
		}catch (Exception|AssertionError err){
			err.printStackTrace();
			System.out.println(err.getMessage());
		}

		testResultService.assertTrue("Wrong response", response.contains("Class already exists"));
		System.out.println(response);
	}


	@Test
	public void getStudentPLTResults() throws Exception {
		// test relevant only for production environment
		if (pageHelper.CILink.contains("ed.engdis.com")) {
			institutionId = dbService.getInstituteIdByName(institutionsName[1]);
			String apiToken = dbService.getApiToken(institutionId);
			String[] stud = dbService.getStudentWithPLTResults(institutionId);

			String response = given().headers("Token", apiToken, "Content-Type", ContentType.JSON, "Accept", ContentType.JSON)
					.when()
					.get("https://edapi.engdis.com/WebApi/External/GetStudentPLTResults/" + institutionId + "/" + stud[0])
					.then()
					.assertThat().statusCode(200)
					.extract().response().asString();

			System.out.println(response);
			testResultService.assertTrue("Wrong response", response.contains("Listening"));
			testResultService.assertTrue("Wrong response", response.contains("Reading"));
			testResultService.assertTrue("Wrong response", response.contains("Grammar"));
		}
	}
	
	@Test
	@Category(SanityTests.class)
	@TestCaseParams(testCaseID = { "" })
	public void testHealthStateCheck() throws TimeoutException, Exception {
				
		try {
		report.startStep("Navigate to Health-url");
			webDriver.openUrl(pageHelper.checkHealthURL);
		
		report.startStep("Retrieve response element from HTML body");
			WebElement response = webDriver.waitForElement("body pre", ByTypes.cssSelector);
			
		report.startStep("Extraxt from response webElement JSON object and verify status of response");
			JSONObject jsonObj = new JSONObject(response.getText());
			String status = (String) jsonObj.get("Status");
			textService.assertEquals("Wrong status of response", status, "Healthy");
		
		report.startStep("Obtain array from JSON");
			JSONArray arr = jsonObj.getJSONArray("Info");
		
		report.startStep("Verify status of REDIS");
			JSONObject services = (JSONObject) arr.get(0);
			String redisStatus = (String) services.get("Status");
			textService.assertEquals("Bad status of services", redisStatus, "Healthy");
		
		report.startStep("Verify status of DataBase");
			JSONObject dataBase = (JSONObject) arr.get(1);
			String dBStatus = (String) dataBase.get("Status");
			textService.assertEquals("Bad status of data Base", dBStatus, "Healthy");
			
		}catch (Exception e) {
			e.printStackTrace();
			testResultService.addFailTest(e.getMessage(), true, true);
		}catch (AssertionError e) {
			e.printStackTrace();
			testResultService.addFailTest(e.getMessage(), true, true);
		}
	}
	
	
	
	
	@After
	public void tearDown() throws Exception {
			super.tearDown();
	}
/*
	@Test
	@TestCaseParams(testCaseID = { "41958" } )
	public void testWebApiGetStudentCourseTestAverage() throws Exception {
		//String expectedResult= "First Discoveries";
		String expectedResult= "OK";
		String tokenUrl="GetStudentCourseTestAverage/" + institutionId + "/auto2";
		int sleepAfterRequest=5;
		testApi(tokenUrl,expectedResult,sleepAfterRequest);
	
		}
		*/
}
