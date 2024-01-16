package tests.edo.newux.REST_API;

import com.fasterxml.jackson.core.type.TypeReference;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import responseModels_of_REST_API.Course;
import responseModels_of_REST_API.InstitutionPackage;
import testCategories.edoNewUX.ReleaseTests;

import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;

public class AssignmentController extends AuthController{

	@Category(ReleaseTests.class)
	@Test
	public void getInstitutionAssignedPackages() throws Exception {
		
	try {
	report.startStep("Send API request 'Assignment/GetInstitutionAssignedPackages' endpoint");
		Type type = new TypeReference<List<InstitutionPackage>>() {}.getType();
		List<InstitutionPackage> packagesAPI = given().headers("Authorization","Bearer "+ token,"Content-Type",ContentType.JSON,"Accept", ContentType.JSON)
				.when()
				.get("Assignment/GetInstitutionAssignedPackages/"+institutionId)
				.then()
				.assertThat().statusCode(200)
				.extract().body().as(type);        
		
	report.startStep("Assert response");
		List<String[]>packages = dbService.getUnExpiredInstitutionPackages(institutionId);
		List<String>packageNames = packages.stream().map(p->p[0]).collect(Collectors.toList());
		packagesAPI.forEach(p->{textService.assertTrue("PackageId not match", packageNames.contains(p.getInstitutionPackageId().toString()));});
	
	}catch (AssertionError e) {
	    testResultService.addFailTest(e.getMessage());
	} catch (Exception e) {
		testResultService.addFailTest(e.getMessage());
	}
	}

	@Category(ReleaseTests.class)
	@Test
	public void testAssignPLT() throws Exception {

	try {
	int iteration = 1;
	while(iteration<3) {
		report.startStep("Iteration "+iteration+": Send API request 'Assignment/AssignPLT' endpoint");
			JsonPath jp = given().headers("Authorization","Bearer "+ token,"Content-Type",ContentType.JSON,"Accept", ContentType.JSON)
							.when()
							.put("Assignment/AssignPLT")
							.then()
							.assertThat().statusCode(200)
							.extract().body().jsonPath();   
			
		report.startStep("Assert response");
			String pLTAssigned = jp.getString("PLTAssigned");
			String pLTResume = jp.getString("PLTResume");
			textService.assertEquals("", "true", pLTAssigned);
			textService.assertEquals("", "false", pLTResume);
			iteration++;
			}
	}catch (AssertionError e) {
	    testResultService.addFailTest(e.getMessage());
	} catch (Exception e) {
		testResultService.addFailTest(e.getMessage());
	}
	}

	@Category(ReleaseTests.class)
	@Test
	public void testGetAssignedCourses() throws Exception {
		
		try {
		List<String[]> listOfUserCoursesFromDB = dbService.getUserAssignedCourses(studentId);
		Type type = new TypeReference<List<Course>>() {}.getType();
				
		report.startStep("Send API request 'Assignment/GetAssignedCourses' endpoint");
		List<Course> courses = given().headers("Authorization","Bearer "+ token,"Content-Type",ContentType.JSON,"Accept", ContentType.JSON)
							.when()
							.get("Assignment/GetAssignedCourses")
							.then()
							.assertThat().statusCode(200)
							.extract().body().as(type);   
			
		report.startStep("Assert response");
			
			textService.assertNotNull("No courses from API", courses);
			textService.assertNotNull("No courses from DB", listOfUserCoursesFromDB);
			boolean allCoursesPresent = true;
			if(courses != null) {
				for(String[]courseFromDB: listOfUserCoursesFromDB) {
					Course course = courses.stream().filter(c->c.getCourseId().toString().equalsIgnoreCase(courseFromDB[1])).findAny().orElse(null); 
					if(course==null) {
						allCoursesPresent = false;
						testResultService.addFailTest("Courses from DB not corespond to courses of API response", false, false);
					}
					//List<String> dBCom = dBcomponents.stream().map(s->s[0]).collect(Collectors.toList());
				}
			}
			textService.assertTrue("Courses not matches", allCoursesPresent);
		}catch (AssertionError e) {
		    testResultService.addFailTest(e.getMessage());
		} catch (Exception e) {
			testResultService.addFailTest(e.getMessage());
		}
		}

	@Category(ReleaseTests.class)
	@Test
	public void testGetInstitutionCourses() throws Exception {
		
		try {
		report.startStep("Send API request 'Assignment/GetInstitutionCourses' endpoint");
			Type type = new TypeReference<List<Course>>() {}.getType();
			//List<String[]> institutionCourses = dbService.getInstitutionCourses(institutionId);
			List<Course> courses = given().headers("Authorization","Bearer "+ token,"Content-Type",ContentType.JSON,"Accept", ContentType.JSON)
					.when()
					.get("Assignment/GetInstitutionCourses")
					.then()
					.assertThat().statusCode(200)
					.extract().body().as(type);  
		
		report.startStep("Assert response");
			textService.assertNotNull("No courses from API", courses);
		
		}catch (AssertionError e) {
			e.printStackTrace();
		    testResultService.addFailTest(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			testResultService.addFailTest(e.getMessage());
		}
			
	}
	
	
	}
