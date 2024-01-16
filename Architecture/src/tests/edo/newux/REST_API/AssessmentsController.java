package tests.edo.newux.REST_API;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runners.MethodSorters;
import testCategories.edoNewUX.ReleaseTests;

import static io.restassured.RestAssured.given;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AssessmentsController extends AuthController {
	
	public static String userTestToken;
	
	@Before
	//@Test
	public void assignPLT() throws Exception {
		
	try {
		report.startStep("Assign PLT to student via 'Assignment/AssignPLT' API endpoint");
		//report.startStep("Send API request 'Assignment/AssignPLT' endpoint");
		JsonPath jp = given().headers("Authorization","Bearer "+ token,"Content-Type",ContentType.JSON,"Accept", ContentType.JSON)
				.when()
				.put("Assignment/AssignPLT")
				.then()
				.assertThat().statusCode(200)
				.extract().body().jsonPath();   
		jp.prettyPrint();
		
	}catch (AssertionError e) {
	    testResultService.addFailTest(e.getMessage());
		e.printStackTrace();
	} catch (Exception e) {
		testResultService.addFailTest(e.getMessage());
		e.printStackTrace();
	}
		
	}

	@Category(ReleaseTests.class)
	@Test
	public void getTestIntroAndStartPLT() throws Exception {
		
	try {
		report.startStep("Send API request 'Assignment/GetTestIntro' endpoint");
		JsonPath jp = given().headers("Authorization","Bearer "+ token) 
				.when()
				.get("Assessments/GetTestIntro/11/1")
				.then()
				.assertThat().statusCode(200)
				.extract().body().jsonPath();  
		
		report.startStep("Retrieve userTestToken and pass him to further tests");
		userTestToken = jp.getString("UserTestToken");
		int timeOnTest = jp.getInt("TestDuration");
		
		report.startStep("Verify time on test has correct value");
		textService.assertEquals("Wrong remaining time test", 60, timeOnTest);
		
		report.startStep("Start PLT test via 'Assessments/StartTest/' API endpoint");
		long time = System.currentTimeMillis()/1000;  //1694674929
		//String testToken = dbService.getUserTetsToken(studentId);
		jp = given().headers("Authorization","Bearer "+ token) 
						.when()
						//.get("Assessments/StartTest/"+userTestToken+"/"+time+"/11/1/0/eng")
						.get("Assessments/StartTest/"+time+"/11/1/0/eng?userTestToken="+userTestToken)
						.then()
						.assertThat().statusCode(200)
						.log().all()
						.extract().body().jsonPath();  
		
		report.startStep("Verify correct test was started");
		String testName = jp.getString("CourseTreeRoot.Name");
		textService.assertEquals("Wrong test name", "Placement Test", testName);
		
	}catch (AssertionError e) {
	    testResultService.addFailTest(e.getMessage());
	} catch (Exception e) {
		testResultService.addFailTest(e.getMessage());
	}
	}
	
	
	
	/*@Test
	public void testC_startPLTtest() throws Exception {
		
		//testB_getTestIntro();
		long time = System.currentTimeMillis()/1000;
		JsonPath jp = given().headers("Authorization","Bearer "+ token) 
						.when()
						.get("Assessments/StartTest/"+userTestToken+"/"+time+"/11/1/0/eng")
						.then()
						.assertThat().statusCode(200)
						.extract().body().jsonPath();  
		String testName = jp.getString("CourseTreeRoot.Name");
		textService.assertEquals("Wrong test name", "Placement Test", testName);
	}
	*/
	
	
	
}
