package services;

import Objects.testResult;
import drivers.GenericWebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tests.misc.EdusoftBasicTest;

import java.util.ArrayList;
import java.util.List;

@Service("testResultService")
public class TestResultService {

	List<String> results = new ArrayList<String>();
	List<String> consoleLogEntries = new ArrayList<String>();

	public List<testResult> testResults = new ArrayList<testResult>();

	@Autowired
	Reporter reporter;

	private GenericWebDriver webDriver;

	/**
	 * 
	 * @param failCause
	 * @param terminateTest
	 *            stops the test if true
	 * @param takeScreenshot
	 *            takes a screenshot if true
	 * @throws Exception
	 */
	public void addFailTest(String failCause, boolean terminateTest,
			boolean takeScreenshot) throws Exception {
		results.add(failCause);
		reporter.reportFailure("Failure added_ " + failCause);
		if (takeScreenshot) {
			webDriver.printScreen(failCause);
		}

		if (terminateTest == true) {
			webDriver.assertTrue(false);
		}

	}

	/**
	 * Adds failure without stopping the test and without taking a screenshot
	 * 
	 * @param failCause
	 */
	public void addFailTest(String failCause) {
		reporter.reportFailure("Failure added: " + failCause);

		results.add(failCause);

	}

	/**
	 * Checks if test failures accured
	 * 
	 * @return
	 */
	public boolean hasFailedResults() {
		boolean hasFailedTest = false;
		
		if (results.size() > 0) {
			hasFailedTest = true;
		}
		return hasFailedTest;
	}

	public List<String> getFailedCauses() {
		return results;
	}

	public void assertTrue(String message, boolean condition) {
		if (condition != true) {

			addFailTest(message);
		}

	}

	/**
	 * Checks if an element has a specific text. If not, element will be
	 * highlighted and screenshot will be taken
	 * 
	 * @param element
	 *            WebElement object
	 * @param expectedText
	 * @throws Exception
	 */
	public void assertElementText(WebElement element, String expectedText)
			throws Exception {
		String actualText = element.getText();

		if (!assertEquals(expectedText, actualText,
				"Text do not match for element: " + element.getAttribute("id"))) {
			webDriver.highlightElement(element);
			webDriver.printScreen("elementTextDoNotMatch");
		}

	}

	/**
	 * Add all test failures to the report
	 */
	public void printAllFailures() {
		reporter.addTitle("Failures are:");

		for (int i = 0; i < results.size(); i++) {
			reporter.report("Failure " + i + ": " + results.get(i));

		}
	}

	public boolean assertEquals(String expected, String actual) {
		return assertEquals(expected, actual, null);
	}

	public boolean assertEquals(String expected, String actual, String message) {
		return assertEquals(expected, actual, message, true);
	}

	public boolean assertEquals(String expected, String actual, String message,
			boolean takeScreenshot) {
		// reporter.report("Asserting " + expected + ". and " + actual +
		// ".");
		reporter.report("Asserting " + expected + " And " + actual);
		try {
			if (expected.equals(actual) == false) {
				
				/*if (expected.length() >= 20 || actual.length()>= 20 || message.length()>=20){
					expected = expected.substring(0, 10);
					actual = actual.substring(0, 10);
					message = message.substring(0, 10);	
				}*/
				addFailTest("Expected String was: '" + expected
						+ "' but actual string was: '" + actual + "' "
						+ message, false, takeScreenshot);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return false;
		}
		return true;
		// reporter.report("Assert passed");

	}
	
	public boolean assertEqualsWithReturn(String expected, String actual) {
		reporter.report("Asserting " + expected + "And " + actual);
		if (expected.equals(actual) == false)
				return false;
		else
			return true;

	}

	public boolean assertEquals(boolean expected, boolean actual)
			throws Exception {
		return assertEquals(expected, actual, null, false);
	}

	public boolean assertEquals(boolean expected, boolean actual, String message)
			throws Exception {

		return assertEquals(expected, actual, message, true);
	}

	public boolean assertEquals(boolean expected, boolean actual,
			String message, boolean takeScreenshot) throws Exception {
		boolean result = true;
		// reporter.report("Asserting " + expected + ". and " + actual + ".");
		if (expected != actual) {
			reporter.report(message);
			addFailTest("Expected boolean was: " + expected
					+ " but actual boolean was: " + actual + " " + message,
					false, takeScreenshot);

			// throw new ComparisonFailure("Assert failed", expected, actual);
			result = false;
			EdusoftBasicTest.failure_count++;
		}
		return result;

	}

	public boolean assertEquals(int expected, int acutal, String message)
			throws Exception {
		return assertEquals(expected, acutal, message, false);
	}

	public boolean assertEquals(int expected, int acutal, String message,
			boolean takeScreenshot) throws Exception {
		if (expected != acutal) {

			addFailTest("Expected int was: " + expected
					+ " but actual int was: " + acutal + " " + message, false,
					takeScreenshot);

			// throw new ComparisonFailure("Assert failed", expected, actual);
			return false;
		}
		return true;

	}

	public boolean assertEquals(double expected, double acutal, String message) {
		if (expected != acutal) {

			addFailTest("Expected double was: " + expected
					+ " but actual double was: " + acutal + " " + message);

			// throw new ComparisonFailure("Assert failed", expected, actual);
			return false;
		}
		return true;

	}

	public List<String> getConsoleLogEntries() {
		return consoleLogEntries;
	}

	public void setConsoleLogEntries(List<String> consoleLogEntries) {
		this.consoleLogEntries = consoleLogEntries;
	}

	public void setWebDriver(GenericWebDriver webDriver) {
		this.webDriver = webDriver;
	}

	public List<testResult> getTestResults() {
		return testResults;
	}

	public boolean assertIsNull(String marked, String message) throws Exception {
		if(marked!=null){
			addFailTest(message, true, true);
			return false;
		}
		return true;
	}
}
