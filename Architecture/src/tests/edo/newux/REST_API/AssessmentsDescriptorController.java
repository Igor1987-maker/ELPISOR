package tests.edo.newux.REST_API;

import io.restassured.http.ContentType;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testCategories.edoNewUX.ReleaseTests;

import java.util.List;

import static io.restassured.RestAssured.given;

public class AssessmentsDescriptorController extends AuthController{

	@Category(ReleaseTests.class)
	@Test
	public void getSpecCourseTestsResult() throws Exception {
	
	try {
		report.startStep("Send API request 'AssessmentsDescriptor/GetSpecCourseTestsResult' endpoint");
		//CourseTestResults ctr
		String response = given().headers("Authorization","Bearer "+ token,"Content-Type",ContentType.JSON,"Accept", ContentType.JSON)
			.when()
			.get("AssessmentsDescriptor/GetSpecCourseTestsResult/43396")
			.then()
			.assertThat().statusCode(200)
			.extract().body().asString();//.as(CourseTestResults.class);            
		report.startStep("Assert response");
		
		//List<SpecCourseTestResult> lst= ctr.getListOfCourseResults();
		//String res = response.toString();
		TestCase.assertEquals("Wrong response", "[]",response);	
	}catch (AssertionError e) {
	    testResultService.addFailTest(e.getMessage());
	} catch (Exception e) {
		testResultService.addFailTest(e.getMessage());
	}
		}

	@Category(ReleaseTests.class)
	@Test
	public void verifyUserHasPlacementTest() throws Exception {
		
	try {
		report.startStep("Retrieve students with PLT results");
		List<String[]> students = dbService.getUsersDidPLT(institutionId);  //.getTestResultOfPLT_API_test(studentId);
		String studId = students.get(0)[2];
		
		report.startStep("Send API request 'AssessmentsDescriptor/UserHasPlacementTest' endpoint");
		String response = given().headers("Authorization","Bearer "+ token,"Content-Type",ContentType.JSON,"Accept", ContentType.JSON)
			.when()
			.get("AssessmentsDescriptor/UserHasPlacementTest/"+studId)
			.then()
			.assertThat().statusCode(200)
			.extract().body().asString();         
		
		report.startStep("Assert response");
		textService.assertEquals("Wrong response", "true",response);	
	
	}catch (AssertionError e) {
	    testResultService.addFailTest(e.getMessage());
	} catch (Exception e) {
		testResultService.addFailTest(e.getMessage());
	}
		//Calendar calendar = Calendar.getInstance();
	    //long timeMilli2 = calendar.getTimeInMillis();
	    //long time = System.currentTimeMillis()/1000;
	    //int timeInt = (int) System.currentTimeMillis();
	}
	
	
	
}
