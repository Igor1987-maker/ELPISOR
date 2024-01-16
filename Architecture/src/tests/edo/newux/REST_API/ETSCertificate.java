package tests.edo.newux.REST_API;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.junit.Before;
import org.junit.Test;
import tests.edo.newux.BasicNewUxTest;

import java.util.List;

import static io.restassured.RestAssured.given;

public class ETSCertificate extends BasicNewUxTest {

    private static String tokenETS;

    @Before
    public void getAuthToken() {
        // RestAssured.baseURI = "https://identity-staging.testsys.io/connect/token";
        JsonPath jpath = given().contentType("application/x-www-form-urlencoded; charset=utf-8")
                .formParam("grant_type", "client_credentials")
                .formParam("scope", "https://api-staging.testsys.io")
                .formParam("client_id", "FCC4F863-3888-49B7-8425-F6306538FAA9 ")
                .formParam("client_secret", "3MAgUE2SH0RivQOjpML8FuZs")
                //.body("")
                .when()
                .post("https://identity-staging.testsys.io/connect/token")
                .then()
                .assertThat().statusCode(200)
                .extract().response().jsonPath();
        tokenETS = jpath.getString("access_token");
        System.out.println(tokenETS);

    }


    @Test
    public void deleteSessionAndAdministration() throws Exception {

        report.startStep("Get institutionId of inst that must be cleaned");
        int rangeOfDays = 0;
        institutionId = dbService.getInstituteIdByName(institutionsName[16]);

        report.startStep("Get all administrations ans sessions created " + rangeOfDays + " days before");
        List<String[]> result = dbService.getSessionAndAdminID(rangeOfDays, institutionId); //"QaJobRediness 57 TOEIC 18/07/2023 - 18/07/2023"
        if (result != null && result.size() != 0) {
            for (int i = 0; i < result.size(); i++) {

                String sessionId = result.get(i)[0];
                String adminId = result.get(i)[1];
                String instIdITS = result.get(i)[2];

                report.startStep("Delete retrieved session (id = " + sessionId + ") on ITS platform using API endpoint");
                String sessionDeleted = given().headers("Authorization", "Bearer " + tokenETS, "Content-Type", ContentType.JSON, "Accept", ContentType.JSON)
                        .when()
                        .delete("https://api-staging.testsys.io/v2/remote/sessions/delete?program-id=238&session-code=" + sessionId)
                        .then()
                        .assertThat().statusCode(200)
                        .extract().response().asString();
                System.out.println(sessionDeleted);

                report.startStep("Delete retrieved administration (id = " + adminId + ") on ITS platform using API endpoint");
                //List<Header> administrationDeleted ;
                String status = given().headers("Authorization", "Bearer " + tokenETS, "Content-Type", ContentType.JSON, "Accept", ContentType.JSON)
                        .when()
                        .delete("https://api-staging.testsys.io/event/close?program-id=238&program-institution-id=2083179&event-id=" + adminId)
                        .then()
                        .assertThat().statusCode(200)
                        .extract().response().jsonPath().getString("status");
                System.out.println(status);
            }

            report.startStep("Delete all administrations and sessions created " + rangeOfDays + " days before from ED side");
            String sql = pageHelper.readFromTXTfile("files/sqlFiles/CleanETSCertificates.txt");//files/sqlFiles/CleanETSCertificates.txt
            dbService.deleteITSAdministrations(sql, institutionId, rangeOfDays);
        }

    }


}
