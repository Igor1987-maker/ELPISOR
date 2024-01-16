package tests.edo.newux.REST_API;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testCategories.edoNewUX.ReleaseTests;

import static io.restassured.RestAssured.given;

public class PromotionController extends AuthController{

	@Category(ReleaseTests.class)
	@Test
	public void setPromotionAndGetPromotion() throws Exception {
		
	try {
		Integer i = 0;
		while(i<2) {
		 report.startStep("Create or set promotion to institution with id: "+institutionId+" , and display promotion = "+i);
		 String setPromotionResponse = given()
					.headers("Authorization","Bearer "+ token)
					.contentType(ContentType.JSON)
					.when()
					.body("{\r\n"
							+ "    \"templateName\": \"edMagazine\",\r\n"
							+ "    \"src\": \"Images/Homepage/Promotion/Template2/ourTeachers.jpg\",\r\n"
							+ "    \"typeTitle\": \"Magazine\",\r\n"
							+ "    \"Title\": \"AAA\",\r\n"
							+ "    \"subTitle\": \"AAAA\",\r\n"
							+ "    \"Body\": \"AAAA\",\r\n"
							+ "    \"Date\": \"AAA\",\r\n"
							+ "    \"publishDate\": \"Tue Feb 07 2023\",\r\n"
							+ "    \"creationDate\": \"Tue Feb 07 2023\",\r\n"
							+ "    \"btnText\": \"AAAA\",\r\n"
							+ "    \"url\": \"google.com\",\r\n"
							+ "    \"display\": \""+i.toString()+"\",\r\n"
							+ "    \"slideId\": \"9\",\r\n"
							+ "    \"imageType\": \"\",\r\n"
							+ "    \"customURL\": \"Promotions/5232282/edMagazine9.jpg\"\r\n"
							+ "  }")
					.put("Promotion/SetPromotionSlides/"+institutionId+"/"+9)
				  	.then()
					.assertThat().statusCode(200)
					.log().body()
					.extract().body().asString();
	report.startStep("Assert that promotion was created sucssesfully");
		textService.assertEquals("Wrong response","6", setPromotionResponse);
		
	report.startStep("Get just created promotion");	
	 JsonPath jpath = given()
				.headers("Authorization","Bearer "+ token)
				.contentType(ContentType.JSON)
				.when()
				.get("Promotion/GetPromotionSlides/"+institutionId)
			  	.then()
				.assertThat().statusCode(200)
				.log().body()
				.extract().response().jsonPath();
	 String getPromotionResponse = jpath.getString("6.9.display");
	 sleep(3);
	
	 report.startStep("Validate that icon displayed eather not");
	 textService.assertEquals("Wrong response",i.toString(), getPromotionResponse);
	 i++;
		}
	}catch (AssertionError e) {
	    testResultService.addFailTest(e.getMessage());
	} catch (Exception e) {
		testResultService.addFailTest(e.getMessage());
	}
	}

	@Category(ReleaseTests.class)
	@Test
	public void getMagazineSlides() throws Exception {
	
	try {
	report.startStep("Create request model('ProgressPerTask') for API call");	
		 JsonPath jpath = given()
					.headers("Authorization","Bearer "+ token)
					.contentType(ContentType.JSON)
					.when()
					.get("Promotion/GetMagazineSlides")
				  	.then()
					.assertThat().statusCode(200)
					.log().body()
					.extract().response().jsonPath();
	 
	 report.startStep("Assert that institution heave magazines");
		String titles = jpath.getString("Title");
		textService.assertNotNull("Institution has no magazines or wrong response", titles);
	}catch (AssertionError e) {
	    testResultService.addFailTest(e.getMessage());
	} catch (Exception e) {
		testResultService.addFailTest(e.getMessage());
	}
			 
	}
	
}
