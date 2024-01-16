package tests.edo.newux;

import io.restassured.http.ContentType;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.ParallelComputer;
import org.junit.runner.JUnitCore;

import static io.restassured.RestAssured.given;

public class WebApiParallelExecution{
    @Test
    public void runAllTests() {
        Class<?>[] classes = { ParallelTest1.class, ParallelTest2.class, ParallelTest3.class, ParallelTest4.class};

        // ParallelComputer(true,true) will run all classes and methods
        // in parallel.  (First arg for classes, second arg for methods)
        JUnitCore.runClasses(new ParallelComputer(true, true), classes);
    }

    public static class ParallelTest1 {
        @Test
        public void test1a() throws Exception {
            System.out.println(Thread.currentThread().getName());
            for (int i = 0;i<100;i++) {
                createClassAPI("Igor-112");
            }

        }

        @Test
        public void test1b() throws Exception {
            for (int i = 0;i<100;i++) {
                createClassAPI("Igor-112");
            }
        }
    }

    public static class ParallelTest2 {
        @Test
        public void test2a() throws Exception {
            System.out.println(Thread.currentThread().getName());
            for (int i = 0;i<100;i++) {
                createClassAPI("Igor-112");
            }
        }

        @Test
        public void test2b() throws Exception {
            for (int i = 0;i<100;i++) {
                createClassAPI("Igor-112");
            }
        }
    }

    public static class ParallelTest3 {
        @Test
        public void test3a() throws Exception {
            System.out.println(Thread.currentThread().getName());
            for (int i = 0; i < 100; i++) {
                createClassAPI("Igor-112");
            }
        }
        @Test
        public void test3b() throws Exception {
            for (int i = 0;i<100;i++) {
                createClassAPI("Igor-112");
            }
        }
    }
    public static class ParallelTest4 {
        @Test
        public void test4a() throws Exception {
            System.out.println(Thread.currentThread().getName());
            for (int i = 0; i < 100; i++) {
                createClassAPI("Igor-112");
            }
        }
        @Test
        public void test4b() throws Exception {
            for (int i = 0;i<100;i++) {
                createClassAPI("Igor-112");
            }
        }
    }

    public static void createClassAPI(String className) throws Exception{

    try {

        //String s = PageHelperService.edApiUrl;
        String response = given().headers("token", "3F19CC14-EDC1-4EBB-B8CB-84B752FA2D58", "Content-Type", ContentType.JSON, "Accept", ContentType.JSON)
                .when()
                .body("{\r\n"
                        + "  \"InstitutionId\": \"5232282\",\r\n"
                        + "  \"ClassName\": \""+className+"\",\r\n"
                        + "  \"PackageName\": \"FD-A3_10_Units\",\r\n"
                        + "  \"StartDate\": \"20231019\"\r\n"
                        + "}")
                .post("https://edapi.edusoftrd.com/webapi/external/CreateClass")
                .then()
                .assertThat().statusCode(200)
                .extract().response().asString();

        Assert.assertTrue(response.contains("Class already exists"));
        System.out.println(response);

        }catch (AssertionError | Exception err){
            System.out.println( err.getMessage());
            err.printStackTrace();

        }
    }


}
