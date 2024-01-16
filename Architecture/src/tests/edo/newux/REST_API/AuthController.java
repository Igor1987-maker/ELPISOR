package tests.edo.newux.REST_API;

import static io.restassured.RestAssured.given;

import java.util.List;

import org.junit.After;
import org.junit.Before;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import requestModels_of_REST_API.User;
import services.PageHelperService;
import tests.edo.newux.BasicNewUxTest;

public class AuthController extends BasicNewUxTest {

	
public static String token;
	
	
	@Before
	public void preCondition() throws Exception {
		
	try {
		String url = PageHelperService.edUiServicesUrl+"/api/";
		RestAssured.baseURI = url;
		report.startStep("Getting URL to webServices: "+url);
		//"https://eduiservicesprod.edusoftrd.com/api/";
		
		report.startStep("Create user within SP for further login");
		studentId = pageHelper.createUSerUsingSP();  
		List<String[]> user = dbService.getUserNameAndPasswordByUserId(studentId);
		userName = user.get(0)[0];
		String password = user.get(0)[1]; 
		
		report.startStep("Create user for login via API");
		User newUser = new User();//institutionId,userName,password
		newUser.setInstitutionId(institutionId);
		newUser.setUserName(userName);
		newUser.setPassword(password);
		
		report.startStep("Api endpoint /Auth/, get token");
		token = given()
				.contentType(ContentType.JSON)
				.body(newUser)
				.when()
				.post("Auth?needOptIn=false")
				.then()
				.assertThat().statusCode(200)
				.extract().response().jsonPath().getString("UserInfo.Token");
		report.startStep("Assert that token is not null");
		textService.assertNotNull("The token is null", token);	
	
	}catch (AssertionError e) {
	    testResultService.addFailTest(e.getMessage());
	} catch (Exception e) {
		testResultService.addFailTest(e.getMessage());
	}
		//Assert.assertNotNull("null", token);
		
	}
	
	@After
	public void postCondition() throws Exception {
		report.startStep("Set user login to null and close the browser");
		dbService.deleteStudentByName(institutionId, userName);
		//pageHelper.setUserLoginToNull(studentId);
		//super.tearDown();
	}
	
}
