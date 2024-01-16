package tests.edo.newux.REST_API;

import Objects.SantilianaCreateSchoolClassStudentObject;
import io.restassured.http.ContentType;

import org.junit.Test;

import tests.edo.newux.BasicNewUxTest;

import java.util.ArrayList;

import static io.restassured.RestAssured.given;

public class SantillianaCreateStudent extends BasicNewUxTest {


//    @Test
//    public void createSantilianaSchoolClassStudent() throws Exception {
//        String url = "https://localhost:5101/api/Santillana/RichmondSSO";
//        String  token = "";
//        SantilianaCreateSchoolClassStudentObject santObject = new SantilianaCreateSchoolClassStudentObject();
//        String userName = "stud" + dbService.sig(5);
//
//        santObject.user = new SantilianaCreateSchoolClassStudentObject.User();
//        santObject.user.id = "user_id";
//        santObject.user.username =userName + santObject.user.id;
//        santObject.user.firstname = userName + "-FN";
//        santObject.user.lastname = userName + "-LN";
//        santObject.user.email = "your_email@example.com";
//        santObject.user.role = "user_role";
//        santObject.user.city = "user_city";
//        santObject.user.residence_country_code = "residence_country_code";
//        santObject.user.date_of_birth = "your_date_of_birth";
//        santObject.user.gender = "user_gender";
//        santObject.user.native_country_code = "native_country_code";
//        santObject.user.native_language_code = "native_language_code";
//
//        santObject.school = new SantilianaCreateSchoolClassStudentObject.School();
//        santObject.school.id = "school_id";
//        santObject.school.name = "school_name";
//        santObject.school.country = "school_country";
//        santObject.school.businessDivision = "school_business_division";
//
//
//        santObject.classItems = new ArrayList<>();
//        SantilianaCreateSchoolClassStudentObject.ClassItem classItem = new SantilianaCreateSchoolClassStudentObject.ClassItem();
//        classItem.id = "class_id";
//        classItem.name = "class_name";
//        classItem.packageValue = "class_package";
//        classItem.numberOfStudents = 42;
//        santObject.classItems.add(classItem);
//
//        try {
//                report.startStep("Create school, class, student and assign student to the class");
//                String setPromotionResponse = given()
//                        .headers("Authorization", "Bearer " + token)
//                        .contentType(ContentType.JSON)
//                        .when()
//                        .body(santObject)
//                        .post(url)
//                        .then()
//                        .assertThat().statusCode(200)
//                        .log().body()
//                        .extract().body().asString();
//
//        } catch (AssertionError e) {
//            testResultService.addFailTest(e.getMessage());
//        } catch (Exception e) {
//            testResultService.addFailTest(e.getMessage());
//        }
//    }
//
}

// createSantilianaSchoolClassStudent and check data on DB and check the End data

// @Test login api with user cred

// @Test login ui with user cred


//@Test assign valid package for each country (15+)

//Negative
//@Test assign the wrong country-package-got error

//@Test create stud with wrong Name(without part of ID)
//@Test create school with wrong Name(without part of ID)
//@Test create class with wrong Name(without part of ID)

