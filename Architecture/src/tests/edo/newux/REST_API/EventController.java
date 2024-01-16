package tests.edo.newux.REST_API;

import static io.restassured.RestAssured.given;

import io.restassured.RestAssured;
import tests.edo.newux.BasicNewUxTest;

public class EventController extends BasicNewUxTest {

	//@BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://api-staging.testsys.io/event";
    }

    //@Test
    public void getAllEvents() {
//        Response response = given()
//            .when()
//                .get("/query")
//            .then()
//                .statusCode(200)
//                .body("event-description", hasItems(not(empty())))
//                .body("package-code", hasItems(not(empty())))
//                .body("form-name", hasItems(not(empty())))
//                .body("start-utc", hasItems(not(empty())))
//                .body("end-utc", hasItems(not(empty())))
//                .body("event-institutions.institution-id", hasItems(not(empty())))
//                .extract().response();

//        System.out.println("Get all events response: " + response.asString());
    }

    //@Test
    public void getEventsForInstitutionId() {
        int institutionId = 1;

		/*
		 * Response response = given() .queryParam("institution-id", institutionId)
		 * .when() .get("/query") .then() .statusCode(200)
		 * .body("event-institutions.institution-id", everyItem(equalTo(institutionId)))
		 * .extract().response();
		 */
        
//        System.out.println("Get events for institution id response: " + response.asString());
    }

   // @Test
    public void getOneEventById() {
        int eventId = 1;

		/*
		 * Response response = given() .pathParam("id", eventId) .when()
		 * .get("/events/{id}") .then() .statusCode(200) .body("event-description",
		 * not(empty())) .body("package-code", not(empty())) .body("form-name",
		 * not(empty())) .body("start-utc", not(empty())) .body("end-utc", not(empty()))
		 * .body("event-institutions.institution-id", hasItems(not(empty())))
		 * .extract().response();
		 */
//        System.out.println("Get one event by id response: " + response.asString());
    }
	
}
