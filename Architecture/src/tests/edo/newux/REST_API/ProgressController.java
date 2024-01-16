package tests.edo.newux.REST_API;

import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import requestModels_of_REST_API.ProgressPerTask;
import testCategories.edoNewUX.ReleaseTests;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static io.restassured.RestAssured.given;

public class ProgressController extends AuthController{

	@Category(ReleaseTests.class)
	@Test
	public void setProgressPerTask() throws Exception {
	
		report.startStep("Create request model('ProgressPerTask') for API call");
		ProgressPerTask ppt = new ProgressPerTask();
		ppt.setCourseId(43396);
		ppt.setItemId(22311);
			
	try {
		int i = 0;
		while(i<2) {
		report.startStep("Send API request 'Progress/SetProgressPerTask' endpoint");
		String response =given().headers("Authorization","Bearer "+ token,"Content-Type",ContentType.JSON,"Accept", ContentType.JSON)
			.when()
			.body(ppt)
		  	.post("Progress/SetProgressPerTask")
			.then()
			.log().all()//.extract().response();
			.assertThat().statusCode(201)
			.contentType(ContentType.JSON)
			.assertThat().body(Matchers.equalTo("0")).extract().body().asString();
		//report.startStep("Assert response");
			//textService.assertEquals("Wrong status code", 201, response.statusCode());	
		 	//textService.assertEquals("Wrong response", "0",response.body().asString());	
			i++;
			}
		}
		catch (AssertionError e) {
		    testResultService.addFailTest(e.getMessage());
		} catch (Exception e) {
			testResultService.addFailTest(e.getMessage());
		}
	
	}

	@Category(ReleaseTests.class)
	@Test
	public void setUserCourseUnitProgress() throws Exception {
		
	try {
		report.startStep("Send API request 'Progress/SetUserCourseUnitProgress' endpoint");
			String response =given().headers("Authorization","Bearer "+ token,"Content-Type",ContentType.JSON,"Accept", ContentType.JSON)
				.when()
				.body("{\r\n"
						+ "  \"courseId\": 43870,\r\n"
						+ "  \"unitId\": 43871,\r\n"
						+ "  \"componentId\": 43872,\r\n"
						+ "  \"toeicSessionKey\": \"3fa85f64-5717-4562-b3fc-2c963f66afa6\"\r\n"
						+ "}")
			  	.post("Progress/SetUserCourseUnitProgress")
				.then()
				.assertThat().statusCode(201)
				.extract().header("date").toString();
		
		report.startStep("Assert header date response with current date");
			String []partsOfResp = response.split(" ");
			String actualRespDate = partsOfResp[1]+" "+partsOfResp[2]+" "+partsOfResp[3];
			LocalDate currentDate = LocalDate.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
			String curDate = currentDate.format(formatter);
			textService.assertEquals("Wrong response", curDate,actualRespDate);	
		}catch (AssertionError e) {
		    testResultService.addFailTest(e.getMessage());
		} catch (Exception e) {
			testResultService.addFailTest(e.getMessage());
		}
				
	}

	@Category(ReleaseTests.class)
	@Test
	public void setUserCourseUnitComponentProgress() throws Exception {
		
	try {
		report.startStep("Send API request 'Progress/SetUserCourseUnitProgress' endpoint");
			String response =given().headers("Authorization","Bearer "+ token,"Content-Type",ContentType.JSON,"Accept", ContentType.JSON)
				.when()
				.put("Progress/SetUserCourseUnitComponentProgress?courseId=43870&unitId=43871&lessonId=105&testScoreThreshold=0")
				.then()
				.assertThat().statusCode(201)
				.contentType(ContentType.JSON)
				.assertThat().body(Matchers.equalTo("0")).extract().body().asString();
		
		report.startStep("Assert response");
		 	textService.assertEquals("Wrong response", "0",response);	
	}catch (AssertionError e) {
	    testResultService.addFailTest("===== "+e.getMessage()+" =====");
	} catch (Exception e) {
		testResultService.addFailTest(e.getMessage());
	}
		
	}
	
}
