package tests.edo.newux;



import java.io.Closeable;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.eclipse.jetty.util.ajax.JSON;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.http.HttpClient;

import com.ning.http.client.HttpResponseBodyPart;
import com.ning.http.client.Response;

import Enums.ByTypes;
import Enums.CourseCodes;
import Enums.CourseTests;
import Enums.UserType;
import Interfaces.TestCaseParams;
import Objects.CourseTest;
import Objects.PLTTest;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.internal.http.RealResponseBody;
import pageObjects.edo.NewUXLoginPage;
import pageObjects.edo.NewUxAssessmentsPage;
import pageObjects.edo.NewUxClassicTestPage;
import pageObjects.edo.NewUxHomePage;
import pageObjects.edo.NewUxLearningArea;
import pageObjects.edo.NewUxMyProfile;
import pageObjects.edo.NewUxMyProgressPage;
import pageObjects.tms.TmsHomePage;
import services.DbService;
import services.PageHelperService;
import services.TestResultService;
import testCategories.*;

import org.apache.http.client.ResponseHandler;
import org.apache.http.impl.client.BasicResponseHandler;

@Category(AngularLearningArea.class)
public class EdUiWebServices extends BasicNewUxTest {
	
	HttpGet get;
	HttpClient httpClient;
	NewUXLoginPage loginPage;
	NewUxLearningArea learningArea;
	String instID = "";
	String jsonData= "";
	String strginData ="";
	
	@Test
	@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "" } )
	public void testUserToken() throws Exception {
		
		report.startStep("Get User Toekn");
		String userToekn = ""; //getUserToekn();
		String controller = "Auth";
		String parameter = "GetUserTokenBeforeLogin/SelfRegistration";
		
	//	userToekn = edUiServicesApiGetAnonymousToken(parameter,controller);
	//	if (userToekn.isEmpty() || userToekn.length()<100)
	//		testResultService.addFailTest("User Token is not valid");
	}
	
	@Test
	@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "" } )
	public void edUiServicesApiSetInstitutionLanguages() throws Exception {
		
		report.startStep("Get User Toekn");
		String controller = "Auth";
		String parameter = "GetUserTokenBeforeLogin/SelfRegistration";
		//String userToekn = edUiServicesApiGetResponse(parameter,controller);
		
		controller = "Institution";
		parameter = "SetInstitutionLanguage";
		//
		edUiServicesApi(parameter,null,1,controller);
		
	}
	
	@Test
	@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "" } )
	public void testGetPromotionSlides() throws Exception {

		String controller = "Promotion/GetPromotionSlides";		
		String expectedResult = "\"src\": \"Images/Homepage/Promotion/Template2";
		String parameter = institutionId;
		int sleepAfterRequest=10;
				
		edUiServicesApi(parameter,expectedResult,sleepAfterRequest,controller);
		
	}		
	

	@Test
	@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "" } )
	public void testGetClassId() throws Exception {
		String controller = "Class/GetClassId";	
		String className = configuration.getProperty("classname");
		String expectedResult = dbService.getClassIdByName(className, institutionId);
	//	String tokenUrl="GetClassId/" + institutionId + "/"+className;
		String parameter = institutionId+"/"+className;
		int sleepAfterRequest=5;
		edUiServicesApi(parameter,expectedResult,sleepAfterRequest,controller);
	}
	
	@Test
	@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "" } )
	public void testCheckForNotSuspendedUserExistence() throws Exception {
		String controller = "Registration/CheckForNotSuspendedUserExistence";	
		String userName = configuration.getProperty("student.user.name");
		String expectedResult = dbService.getUserIdByUserName(userName, institutionId);
	//	String tokenUrl="GetClassId/" + institutionId + "/classNewUx";
		String parameter = userName+"/"+institutionId;
		int sleepAfterRequest=5;
		String responseFormat="string";
		edUiServicesApi(parameter,expectedResult,sleepAfterRequest,controller);
	
		}
	
	
	public void edUiServicesApi(String parameter, String expectedResult, int sleepAfterRequest,String controller) throws Exception{
		
		report.startStep("Init test data");
		
		if (institutionId.equalsIgnoreCase("")){
			institutionId = configuration.getProperty("institution.id"); // in case use default "automation" institution
		}

		String userToekn = ""; //getUserToekn();
	
	//	if (userToekn.isEmpty() || userToekn.length()<100)
	//		testResultService.addFailTest("User Token is null or invalid");	
		
		userToekn = "Bearer " + userToekn;

			
		String baseUrl = PageHelperService.edUiServicesUrl + "/api/"+controller+"/";
			
		baseUrl = baseUrl+parameter;
		//String ApiToken= dbService.getApiToken(institutionId);
		sleep(2);
		OkHttpClient client = new OkHttpClient();
		int resStatusExpected=200;
		
	report.startStep("Send request");
	
		Request request = new Request.Builder()
				.url(baseUrl)
				.get()
				.addHeader("Authorization", userToekn)
				.build();
		sleep(sleepAfterRequest);
		report.report("Request for API is: " + request);
		testResultService.getFailedCauses();
		
		
		report.startStep("Get response body");
		
		Closeable responses = null;
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
		
	//	if (responseFormat.equalsIgnoreCase("json"))
			//jsonData = ((okhttp3.Response) responses).body().string();
		
	//	else if (responseFormat.equalsIgnoreCase("string"))
	//		strginData = ((okhttp3.Response) responses).body().string();
			
/*		
		for (int i=0; jsonData.equalsIgnoreCase("") && i<webDriver.getTimeout(); i++){
			jsonData = ((okhttp3.Response) responses).body().string();
			sleep(1);
		}
*/		
		report.report("Received data from the API and check the Body, response body is: " + jsonData);
		sleep(sleepAfterRequest);
		checkResponceContainsExpectedresult(((okhttp3.Response) responses).body().string()); //(expectedResult);
		
		report.startStep("Check the response status");
	
		//okhttp3.Response response = client.newCall(request).execute();
		//sleep(2);
		int resStatusActual = ((okhttp3.Response) responses).code(); //response.code();
		report.report("Result status from the API call is: " + resStatusActual);
		testResultService.assertEquals(resStatusActual, resStatusExpected, "Failed on WebApi: HTTP code Status wrong");
	}
	

	@Test
	@Category(inProgressTests.class)
	@TestCaseParams(testCaseID = { "84446" } )
	public void tokenUpdateByActionAfterHalfOfSessionTimeLeft() throws Exception {
		report.startStep("Set expiration time to service appjson settings (minutes)");
			textService.updateJsonFileByKeyValue(PageHelperService.edUiServicePath,"appsettings","Jwt","ExpirationInMinutes","2");
			pageHelper.runConsoleProgram(PageHelperService.batFilesRestartPoolPath + "StopAppPoolRemotely.bat");
		sleep(2);
			pageHelper.runConsoleProgram(PageHelperService.batFilesRestartPoolPath + "StartAppPoolRemotely.bat");
		sleep(2);
	
		try {
			report.startStep("Get user and login");
			getUserAndLoginNewUXClass();
		//	homePage.closeAllNotifications();
		//	homePage.waitHomePageloaded();
			
			report.startStep("Saving auth token");
			String firstToken = pageHelper.runJavaScriptCommand("return localStorage.getItem(\"EDAPPToken\")");
			System.out.println(firstToken);
							
			report.startStep("Waiting utill more than half of the session is over");
			sleep(65);
			homePage.clickOnMyProfile();
			sleep(2);
			String secondToken = pageHelper.runJavaScriptCommand("return localStorage.getItem(\"EDAPPToken\")");
			System.out.println(secondToken);
				Assert.assertNotEquals(firstToken,secondToken,"Token not refreshed!!!");
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		finally{
			
			textService.updateJsonFileByKeyValue(PageHelperService.edUiServicePath,"appsettings","Jwt","ExpirationInMinutes","120");
			//Runtime.getRuntime().exec("C:/automation/BatFiles/StopAppPoolRemotely.bat");
			pageHelper.runConsoleProgram(PageHelperService.batFilesRestartPoolPath + "StopAppPoolRemotely.bat");
			sleep(2);
			pageHelper.runConsoleProgram(PageHelperService.batFilesRestartPoolPath + "StartAppPoolRemotely.bat");
			sleep(2);
			
		}
	}
	//PageHelperService phs = new PageHelperService();
	//phs.runConsoleProgram("C:/automation/BatFiles/StopAppPoolRemotely.bat");
	//sleep(2);
	//phs.runConsoleProgram("C:/automation/BatFiles/StartAppPoolRemotely.bat");
	//sleep(2);
	//NewUxHomePage nhp = new NewUxHomePage(webDriver, testResultService);
	//String firstToken = nhp.getTokenFromLS();
	//getTokenFromLocalStorage();
	//String firstToken = tokenFromLocalStorage;
	//JavascriptExecutor jsExecutor = (JavascriptExecutor)webDriver;
	//String itemFromStorage = (String) jsExecutor.executeScript("return localStorage.getItem(\"EDAPPToken\")");
	//window.localStorage.getItem(key);
	//		String firstToken = getUserToken();
	//String secondToken = nhp.getTokenFromLS();
	//String secondToken = getUserToken();

	public String getUserToken() throws Exception {
		// TODO Auto-generated method stub
		
		String[] userDetails = null;
		String responsecontent="";
		String token="";
		
		try {
			userDetails = pageHelper.getUserFromDbByInstitutionIdAndClassName(institutionId, configuration.getProperty("classname"));
		
			String userName = userDetails[0];
			String password = userDetails[1];
			studentId = userDetails[2];
			
			
			String requestUrl = PageHelperService.edUiServicesUrl + "/api/auth?needOptIn=false";
			
			StringBuilder sb = new StringBuilder();	
						
			  sb.append("{userName:\"" + userName + "\",");
			  sb.append("password:\"" + password + "\",");
			  sb.append("institutionId:"+institutionId+"}");
			    
			  responsecontent = netService.sendServiceRequestWebApiPostString(requestUrl, sb.toString());
			  token = extractTokenFromResponse(responsecontent);
			  
			  report.report("The full request is: " + requestUrl);
			  report.report("The request's body is: " + sb.toString());
			  report.report("The full response is: " + responsecontent);
			  report.report("The response token is: " + token);
			  
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  
		  System.out.println(responsecontent);
		  System.out.println(token);
		  System.out.println();
		  
		  return token;
		  
	}



	private String extractTokenFromResponse(String responsecontent) {

		  String[] responseArray1 = responsecontent.split(":");
		  String[] responseArray2 = responseArray1[3].split(","); 
		  String token= responseArray2[0].replace("\"", "");
		  
		return token;
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
	@After
	public void tearDown() throws Exception {
			super.tearDown();
	}
	
}
