package tests.edo.newux;

import java.awt.image.BufferedImage;
import java.util.List;

import javax.imageio.ImageIO;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.WebDriver;

import Interfaces.TestCaseParams;
import pageObjects.edo.NewUxHomePage;
import pageObjects.edo.NewUxLearningArea;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.comparison.ImageDiff;
import ru.yandex.qatools.ashot.comparison.ImageDiffer;
import ru.yandex.qatools.ashot.screentaker.ScreenTaker;
import ru.yandex.qatools.ashot.screentaker.ViewportPastingStrategy;
import testCategories.reg1;


public class Demo extends BasicNewUxTest {
	
	
	@Before
	public void setup()throws Exception{
		enableProxy();
		super.setup();
	}

	// @Test
	// public void test() throws Exception {
	// createUserAndLoginNewUXClass();
	// newUxHomePage.navigateToCourse(4);
	//
	// homePage.clickOnUnitLessons(8);
	// String lessonName = homePage.getLessonText(7, 5, "0000");
	//
	// String expectedLessonText;
	//
	// List<String[]> components = dbService
	// .getComponentDetailsByUnitId("22185");
	// expectedLessonText = dbService.getLessonNameBySkill(components.get(5));
	//
	// testResultService.assertEquals(expectedLessonText.replace("  ", " "),
	// lessonName, "");
	// }

	// @Test
	// @TestCaseParams(testCaseID = { "21223" }, institutionId = "5232272")
	// public void navigate() throws Exception {
	// String institutionId = "5232272";
	// String classId = dbService.getClassIdByName("classAviation",
	// institutionId);
	//
	// studentId = pageHelper
	// .createUSerUsingSP(institutionId, "classAviation");
	//
	// loginAsStudent(studentId, institutionId);
	//
	// loginAsStudent(studentId);
	//
	// }

	@Test
	public void ashotTest() throws Exception {
		createUserAndLoginNewUXClass();

		sleep(10);
		// Screenshot myScreenshot1 = new AShot().takeScreenshot(webDriver
		// .getWebDriver());

//		Screenshot homepage = new AShot().shootingStrategy(
//				new ViewportPastingStrategy(1)).takeScreenshot(
//				webDriver.getWebDriver());
		
		BufferedImage image=ImageIO.read(webDriver.printScreenAsFile());
		webDriver.saveImage("chromeBase", image);
		
	System.out.println(webDriver.printScreen("chromeBase"));	;

		// newUxHomePage.navigateToCourse(2); //22139
		sleep(4);
		// save

		// Screenshot myScreenshot2 = new AShot().takeScreenshot(webDriver
		// .getWebDriver());

//		ImageDiff diff = new ImageDiffer().makeDiff(myScreenshot1.getImage(),
//				webDriver.loadImage("C:\\s1.png"));
//		BufferedImage img = diff.getDiffImage();

//		 webDriver.saveImage("hp_chrome", homepage.getImage());
		// webDriver.saveImage("s2", myScreenshot2.getImage());

//		webDriver.saveImage("diff", img);//22895

	}

	@Test
	@TestCaseParams(testCaseID = { "20556" })//22960
	public void progressForLastUnit() throws Exception {
		createUserAndLoginNewUXClass();

		studentService.setProgressInFirstUnitInCourse("20000", studentId, 0);
		studentService.setProgressInLastUnitInCourse("20185", studentId, 0);
		System.out.println();
	}
	
	@Test
	@TestCaseParams(testCaseID = { "20606" })//19881
	public void progressFoit() throws Exception {
//		createUserAndLoginNewUXClass();
//
//		studentService.setProgressInFirstUnitInCourse("20000", studentId, 0);
//		studentService.setProgressInLastUnitInCourse("20185", studentId, 0);
//		System.out.println();
		
		createUserAndLoginNewUXClass();
		
		netService.checkForNetworkErrors(webDriver.getHar());

//		NewUxHomePage homePage = new NewUxHomePage(webDriver, testResultService);
//		homePage.navigateToCourseUnitAndLesson(courseCodes, "B2", 2, 2, true,studentId);
		
		webDriver.printConsoleLogs(null, false);
	}
	@After
	public void tearDown() throws Exception {
			super.tearDown();
	}
}
