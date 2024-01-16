package tests.tms.dashboard;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.WebElement;

import Enums.UserType;
import Interfaces.TestCaseParams;
import pageObjects.EdoHomePage;
import pageObjects.tms.DashboardPage;
import tests.edo.newux.BasicNewUxTest;
import tests.misc.EdusoftWebTest;

public class DashboardBasicTest extends BasicNewUxTest {

	protected String classWithLastProgress;
	protected String courseWithLastProgress;

	public static final String FIRST_DISCOVERIES = "First Discoveries";
	public static final String classForCacheTest = "classForCacheTest";

	private static final int DASHBOARDWIDTH = 1024;

	@Before
	public void setup() throws Exception {
		super.setup();
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}
	
	@Ignore
	public void getClassAndCourseWithLastProgress(String teacherName,
			UserType userType) throws Exception {
		String[] str = dbService.getClassAndCourseWithLastProgress(teacherName,
				autoInstitution.getInstitutionId(), userType);
		classWithLastProgress = str[0];
		courseWithLastProgress = str[1];
		courseWithLastProgress = courseWithLastProgress.replaceAll("\\s+$", "");
	}

	@Ignore
	public List<String[]> getAvgScoresByClassIdAndCourseId(String className,
			String courseName) throws Exception {

		String classId = dbService.getClassIdByName(className,
				autoInstitution.getInstitutionId());

		String courseId = dbService.getCourseIdByName(courseName);
		List<String[]> scores = dbService.getClassScores(classId, courseId);
		return scores;
	}

	@Ignore
	public String[] getClassPltScores(String className,
			String institutionName) throws Exception {
		String sql = textService.getTextFromFile(
				"files/sqlFiles/getPltScore.txt", Charset.defaultCharset());

		sql = sql.replace("%classPara%", className);
		sql = sql.replace("%institutionParam%", institutionName);

		List<String[]> rsList = dbService.getListFromStoreRrecedure(sql);
		//List<String[]> list = rsList.get(2);
		return rsList.get(2);
	}

	@Ignore
	public List<String[]> getClassCompletion(String className,
			String institutionName, String courseId) throws Exception {
		String sql = textService.getTextFromFile(
				"files/sqlFiles/getClassCompletion.txt",
				Charset.defaultCharset());
		sql = sql.replace("%classParam%", className);
		sql = sql.replace("%instNameParam%", institutionName);
		sql = sql.replace("%courseId%", courseId);

		List<String[]> rsList = dbService.getListFromPrepairedStmt(sql, 4);
		return rsList;

	}

	@Ignore
	public int getNumberOfStudentWithPltScores(List<String[]> list) {
		int count = 0;
		for (int i = 0; i < list.size(); i++) {
			count += Integer.parseInt(list.get(i)[0]);
		}
		System.out.println("with plt:" + count);
		return count;

	}

	@Ignore
	public static int getDashboardwidth() {
		return DASHBOARDWIDTH;
	}

	@Ignore
	public void selectClassAndCourse(String className, String courseName) throws Exception{
		DashboardPage dashboardPage=new DashboardPage(webDriver, testResultService);
		dashboardPage.selectClassInDashBoard(className);
		sleep(2);
		dashboardPage.selectCourseInDashboard(courseName);
		dashboardPage.clickOnDashboardGoButton();
	}
	
	@Ignore
	public void waitForLoadingofAllWidgets(){
		
	}
	
	@Ignore
	public void waitForDashboardCacheToExpire(String filePath) throws Exception{
		File file = new File(filePath);
		if (file.exists()) {
			String text = textService.getTextFromFile(filePath,
					Charset.defaultCharset());
			long lastReportTime = Long.parseLong(text);
			long currentTime = System.currentTimeMillis();
			long timeAfterLastReport = currentTime - lastReportTime;
			if (timeAfterLastReport < 60000) {
				// wait
				System.out.println("Sleeping test - waiting for cache to end");
				Thread.sleep(60000 - timeAfterLastReport);

			}

		}
	}
}
