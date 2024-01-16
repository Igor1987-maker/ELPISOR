package tests.misc;

import java.sql.SQLException;
import java.util.List;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import Interfaces.Sanity;

public class CreateStudents extends EdusoftWebTest {

	@Test
	public void createStudents() throws Exception {

		int numOfStudents = 1;
		for (int i = 0; i < numOfStudents; i++) {

//	String studentId=		pageHelper.createUSerUsingSP(configuration.getInstitutionId(),
//					"classNewUx");
			
			String studentId=pageHelper.createUSerUsingSP();
			System.out.println(studentId);
		}

	}

	@Test
	public void clearClassStudents() throws Exception {
		
		/*String institutionId = "5231685";
		String className = configuration.getProperty("classname.aeon");*/
		
		String institutionId = configuration.getInstitutionId();
		String className = configuration.getClassName();
				
		studentService.clearStudents(institutionId, className, 10);

	}

	

	@Test
	@Category(Sanity.class)
	public void createSingleStudent() throws Exception {
		System.out.println("Create student unit test");
		pageHelper.createUSerUsingSP();
	}

	@Test
	public void createMultipleClassStudents() throws Exception {
		// String []classes=new String[]{"class2","class3","class4","class1"};
		String[] classes = new String[] { "Primero", "Segundo", "Tercero",
				"Cuarto", "Quinto" };
		String instId = "6550063";

		pageHelper.addStudentsToMultileClasses(500, classes, instId);
	}

	@Test
	public void createStudentsApi() throws Exception {

		int numOfStudents = 1;
		for (int i = 0; i < numOfStudents; i++) {
			// pageHelper.createUserUsingApi(sut, userName, fname, lname, pass,
			// instId, className);
		}

	}

}
