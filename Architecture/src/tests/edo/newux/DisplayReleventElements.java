package tests.edo.newux;

import java.util.Arrays;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.WebElement;

import pageObjects.edo.NewUXLoginPage;
import pageObjects.edo.NewUxHomePage;
import testCategories.unstableTests;
import testCategories.edoNewUX.CourseArea;
import Enums.ByTypes;
import Interfaces.TestCaseParams;
//20834
public class DisplayReleventElements extends BasicNewUxTest {

	@Test
	@Category(unstableTests.class)
	@TestCaseParams(testCaseID = { "20363", "20407" }, testTimeOut = "30", testMultiple = true)
	public void testRelevanElementPerUnit() throws Exception {
		report.startStep("Create new student");
		String studentId = pageHelper.createUSerUsingSP(
				configuration.getInstitutionId(), configuration.getClassName());
		NewUXLoginPage loginPage = new NewUXLoginPage(webDriver,
				testResultService);

		String userName = dbService.getUserNameById(studentId,
				configuration.getInstitutionId());

		report.startStep("Login with new student");
		NewUxHomePage homePage = loginPage.loginAsStudent(userName, "12345");

		for (int i = 0; i < courses.length; i++) {

			report.startStep("Testing course: " + courses[i]);
			checkCourseUnits(homePage, courses[i]);
			// homePage.closeLastOpenLessonsElement();
			homePage.scrollUp();
			// sleep(3);
			homePage.carouselNavigateNext();
			report.finishStep();

		}

	}

	private void checkCourseUnits(NewUxHomePage homePage, String courseId)
			throws Exception {
		report.startStep("Check Course units for course: " + courseId);

		List<WebElement> unitsElements = homePage.getUnitsElements();

		List<String[]> units = dbService.getCourseUnitDetils(courseId);
		testResultService.assertEquals(unitsElements.size(), units.size(),
				"Units elements and DB units are not in the same size");
		int rowCounter = 0;
		for (int i = 0; i < unitsElements.size(); i++) {

			rowCounter++;
			if (rowCounter == 4) {
				rowCounter = 1;
			}
			int index = i + 1;

			String unitId = homePage.getUnitIdTextByIndex(index);

			testResultService.assertEquals(String.valueOf(index), unitId,
					"Unit id is not displayed");

			String unitName = homePage.getUnitNameByIndex(index);

			testResultService.assertEquals(
					units.get(i)[1].trim().replace("  ", " "), unitName.trim(),
					"Unit name in course: " + courseId + ", unit: " + i);

			String style = homePage.getUnitStyleByIndex(index);

			testResultService.assertEquals(
					true,
					style.contains("Runtime/Graphics/Units/U_"
							+ units.get(i)[0] + ".jpg"));

			WebElement element = homePage.getUnitElementByIndex(index);

			String rgbColor = element.getCssValue("border-color");

			if (webDriver.getBrowserName() != "firefox") {
				pageHelper.checkColor(rgbColor, HexUnitColors[i]);
			}
			List<String[]> components = dbService
					.getComponentDetailsByUnitId(units.get(i)[0]);

			homePage.clickOnUnitLessons(index);
			// int unitIndex = i + 1;
//			System.out.println("Number of lessons: " + components.size());
			// boolean scrolled = false;
			for (int j = 0; j < components.size(); j++) {

				String expectedLessonText;
				expectedLessonText = dbService.getLessonNameBySkill(components
						.get(j));
				expectedLessonText=expectedLessonText.trim();

				String lessonName = homePage.getLessonText(i, j, courseId);
				if (lessonName != null) {
					lessonName = lessonName.trim();
				}
				testResultService.assertEquals(
						expectedLessonText.replace("  ", " "), lessonName,
						"Lesson name in course: " + courseId + " , Unit:" + i
								+ ", lesson: " + j);

			}

			// click again to close unit lessons
			homePage.clickOutsideUnitLessons(index);

			report.finishStep();

		}
	}

}
