package tests.edo.newux.REST_API;

import io.restassured.http.ContentType;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import responseModels_of_REST_API.UserProfileResponse;
import testCategories.edoNewUX.ReleaseTests;

import java.util.List;

import static io.restassured.RestAssured.given;

public class UserProfileController extends AuthController {

	@Category(ReleaseTests.class)
	@Test
	public void getUsersProfile() throws Exception {
		
	try {
		report.startStep("Send API request 'UsersProfile' endpoint");
		UserProfileResponse upr = given()
				.headers("Authorization","Bearer "+ token)
				.contentType(ContentType.JSON)
				.when()
				.get("UserProfile")
			  	.then()
				.assertThat().statusCode(200)
				.log().body()
				.extract().body().as(UserProfileResponse.class);
				
				
		
		List<String[]> usersData = dbService.getUserNameAndPasswordByUserId(studentId);
		String name = upr.getUserName();	 	
		String email = upr.getEmail();
		report.startStep("Assert response with data from DB");
		textService.assertEquals("Wrong response", usersData.get(0)[0],name);	
		textService.assertEquals("Wrong response", usersData.get(0)[4],email);	
	}catch (AssertionError e) {
	    testResultService.addFailTest(e.getMessage());
	} catch (Exception e) {
		testResultService.addFailTest(e.getMessage());
	}
	}
}
