package tests.tms.dashboard;

import java.io.File;
import java.nio.charset.Charset;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import pageObjects.EdoHomePage;
import pageObjects.EdoLoginPage;
import pageObjects.tms.DashboardPage;

public class DashboardPerformanceTest extends DashboardBasicTest {

	private static final String DASHBOARD_CACHE_PARAM = "//configuration//appSettings//add[@key='DashboardCacheObjectExpirationInMinutes']";
	private static final String WEBCONFIGPATH = "\\\\frontqa3\\TMS_ProdNov14\\web.config";

	
	public void testDashboardLoadingTomeFirstLoad() throws Exception {
		// test loading with no cache
		webDriver.printScreen();
		int loadingTrashhole = 10;
		int loadingTrashholeWithCache = 10;

		// class:
		String classId = "700065";
		// course:
		String courseId = "8";
		String tempFilePath = "files/temp/" + classId + "_" + courseId + ".txt";
		String teacherUserName = "autoTeacher";
		String institutionName = "INFOP.aspx";

		startStep("Check that caching is set to 1 minute");
		Document document = netService.getXmlFromFile(WEBCONFIGPATH);

		NodeList idNodeList = netService.getNodesFromXml(DASHBOARD_CACHE_PARAM,
				document);
		System.out.println("Value of caching is:"
				+ idNodeList.item(0).getAttributes().getNamedItem("value")
						.getNodeValue());
		int cachingTineout = Integer.valueOf(idNodeList.item(0).getAttributes()
				.getNamedItem("value").getNodeValue());
		testResultService.assertEquals(1, cachingTineout,
				"Caching value is not set to 1 in webconfig");
		startStep("Check the last time the report was loaded");

		waitForDashboardCacheToExpire(tempFilePath);

		// check if reportFile exist

		// save file in this stracture: %classId%_%courseId%_%timeStamp%
		startStep("Login as teacher and open the dashboard");
		long startTime = System.currentTimeMillis();
		EdoHomePage edoHomePage = pageHelper.loginAsTeacher(teacherUserName,
				institutionName);
		webDriver.waitForJqueryToFinish();
		DashboardPage dashboardPage = (DashboardPage) edoHomePage
				.clickOnTeachersCorner(true);
		webDriver.waitForJqueryToFinish();
		long endTime = System.currentTimeMillis();
		textService.writeTxtFileWithText(tempFilePath, String.valueOf(endTime));
		long elapsed = endTime - startTime;
		int actualLoadingTime = (int) elapsed / 1000;
		System.out.println("Elapsed time (in MS) is: " + elapsed);

		webDriver.printScreen("Loading finished");

		startStep("Check that report is loaded after " + loadingTrashhole
				+ " seconds");
		testResultService.assertTrue(
				"Loading time it too long. Loading time was :"
						+ actualLoadingTime
						+ "seconds while expecte loading time was:"
						+ loadingTrashhole + " seconds",
				actualLoadingTime < loadingTrashhole);

		startStep("Load the dashboard again(with cache");
		startTime = System.currentTimeMillis();
		webDriver.refresh();
		webDriver.waitForJqueryToFinish();
		endTime = System.currentTimeMillis();
		textService.writeTxtFileWithText(tempFilePath, String.valueOf(endTime));
		elapsed = endTime - startTime;
		actualLoadingTime = (int) elapsed / 1000;
		System.out.println("Elapsed time (in MS) is: " + elapsed);

		

		startStep("Check that report is loaded after " + loadingTrashholeWithCache
				+ " seconds");
		testResultService.assertTrue(
				"Loading time it too long. Loading time was :"
						+ actualLoadingTime
						+ "seconds while expecte loading time was:"
						+ loadingTrashhole + " seconds",
				actualLoadingTime < loadingTrashholeWithCache);

	}
}
