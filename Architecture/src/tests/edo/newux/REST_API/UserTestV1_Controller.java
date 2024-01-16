package tests.edo.newux.REST_API;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import requestModels_of_REST_API.SaveUserTest;
import requestModels_of_REST_API.SaveUserTest.Answer;
import testCategories.edoNewUX.ReleaseTests;

import java.util.ArrayList;

import static io.restassured.RestAssured.given;

public class UserTestV1_Controller extends AuthController {

	@Category(ReleaseTests.class)
	@Test
	public void saveUserTest() throws Exception {
		
	try {
		report.startStep("Create request model('SaveUserTest') for API call");
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		SaveUserTest sut = generatePOJOclass();
		
		int iteration = 0;
		int ststusCode = 200;
			while(iteration<=2) {
			//if(i==2) {
			//	ststusCode=429;
			//}
		report.startStep("Send API request 'UserTestV1/SaveUserTest' endpoint");
			JsonPath jPath = given().headers("Authorization","Bearer "+ token,"Content-Type",ContentType.JSON,"Accept", ContentType.JSON)
				.when()
				.body(mapper.writerFor(SaveUserTest.class).writeValueAsBytes(sut))
				.post("UserTestV1/SaveUserTest/43397/60/false")
				.then()
				.contentType(ContentType.JSON)
				.assertThat().statusCode(ststusCode)
				.extract().body().jsonPath();
		
	 String expected = jPath.getString("finalMark");
	 report.startStep("Assert response");
	 textService.assertEquals("Wrong response", "0",expected);	
	 iteration++;
			}
	}catch (AssertionError e) {
	    testResultService.addFailTest(e.getMessage());
	} catch (Exception e) {
		testResultService.addFailTest(e.getMessage());
	}
}
	
	
	private SaveUserTest generatePOJOclass() {
		SaveUserTest sut = new SaveUserTest();
		Answer answ = sut.new Answer();
		ArrayList<int[]> arr = new ArrayList<>();
		int[] i = {0};
		arr.add(i);
		answ.setItemId(0);
		answ.setItemAnswers(arr);
		answ.setMark(1);
		ArrayList<Answer> arrAnsw = new ArrayList<>();
		arrAnsw.add(answ);
		sut.setAnswers(arrAnsw);
		sut.setTestTime(0);
		return sut;
	}
	
}
