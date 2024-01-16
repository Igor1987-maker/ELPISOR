package services;

import java.lang.annotation.Annotation;
import java.util.List;

import junitparams.internal.TestMethod;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import Enums.Browsers;
import Interfaces.TestCaseParams;

;
/**
 * Used to override the Junit basic runner
 * @author omers
 * DO NOT TOUCH!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 */
public class ExtendedRunner extends BlockJUnit4ClassRunner {

	private String testId;
	String defaultTestTimeOut = "10";
	int browsersCnt = 0;
	String[] browsers = new String[] { "IE 9", "IE 10", "IE 11", "chrome",
			"firefox" };



	public ExtendedRunner(Class<?> klass)
			throws org.junit.runners.model.InitializationError {
		super(klass);

	}

	public String getTestId() {
		return testId;
	}

	public void setTestId(String testId) {
		this.testId = testId;
	}

	@Override
	protected void validateInstanceMethods(List<Throwable> errors) {
		validatePublicVoidNoArgMethods(After.class, false, errors);
		validatePublicVoidNoArgMethods(Before.class, false, errors);
		validateTestMethods(errors);

	}

	private int count;

	// String testPrefix="_";

	public int nextCount() {
		return count++;
	}

	public int count() {
		return count;
	}

	public void getBrowserParam(TestMethod method) {
		if (method.parametersSets().length > 0) {

//			System.out.println("Got browser param: "
//					+ method.parametersSets()[count].toString());
			System.setProperty("browserParam",
					method.parametersSets()[nextCount()].toString());
		}
	}
	/**
	 * used to get test method paramters and use them
	 */
	@Override
	protected Statement methodBlock(FrameworkMethod method) {

		System.setProperty("testCaseName", method.getName());

		TestCaseParams params = method.getAnnotation(TestCaseParams.class);
		if (params != null) {

			String testCaseIds = "";
			for (int i = 0; i < params.testCaseID().length; i++) {
				if (i > 0) {
					testCaseIds += ",";
				}
				testCaseIds += params.testCaseID()[i];
			}

			System.setProperty("testCaseId", testCaseIds);

			if (params.testedBrowser() != Browsers.empty) {
				System.setProperty("browserParam", params.testedBrowser()
						.toString());

			}

			if (params.ignoreTestTimeout() == true) {
				System.setProperty("ignoreTineOut", "true");
			}

			if (!params.envFile().equals("")) {
				System.setProperty("nevFileParam", params.envFile());
			}
			if (!params.institutionId().equals("")) {

				System.setProperty("instId", params.institutionId());
			}
			

		}
		try {
			if (params.equals(null)) {
				System.setProperty("testTimeOut", defaultTestTimeOut);
			} else {
				if (!params.testTimeOut().equals("0")) {
					System.setProperty("testTimeOut", params.testTimeOut());
				} else if (params.ignoreTestTimeout() == true) {
					System.setProperty("ignoreTimeOuts", "true");
					System.setProperty("testTimeOut", "9999");
				}

				else {
					System.setProperty("testTimeOut", defaultTestTimeOut);
				}
			}
		}

		catch (NullPointerException e) {
			System.setProperty("testTimeOut", defaultTestTimeOut);
		}

//		System.out.println("Timeout is set to: "
//				+ System.getProperty("testTimeOut") + " minutes");

		return super.methodBlock(method);
	}

	@Override
	protected void validatePublicVoidNoArgMethods(
			Class<? extends Annotation> annotation, boolean isStatic,
			List<Throwable> errors) {
		List<FrameworkMethod> methods = getTestClass().getAnnotatedMethods(
				annotation);

	}

	private Description describeRepeatTest(FrameworkMethod method,
			String browser) {

		Description description = Description.createSuiteDescription(
				testName(method) + " [" + browser + " browser]",
				method.getAnnotations());

		for (int i = 0; i <= browsers.length; i++) {

			description.addChild(Description.createTestDescription(
					getTestClass().getJavaClass(), testName(method)));

		}
		return description;
	}

	/**
	 * used to skip browsers
	 */
	@Override
	protected void runChild(FrameworkMethod method, RunNotifier notifier) {

		String userBrowser = System.getProperty("browserCMD");
//		System.out.println(" ******************* Browser from maven: "
//				+ userBrowser);
		Description description = describeChild(method);

		TestCaseParams params = method.getAnnotation(TestCaseParams.class);
		boolean testIgnored = false;
		if (params != null) {
			
			
			if(params.allowMedia()==true){
				System.setProperty("chromeMedia", "true");
			}

			if (params.skippedBrowsers().length > 0 && userBrowser != null) {
				for (int i = 0; i < params.skippedBrowsers().length; i++) {
					if (userBrowser.equals(params.skippedBrowsers()[i])) {
						notifier.fireTestIgnored(description);
						testIgnored = true;
						System.out
								.println("Test ignoreed  *******************");
						break;
					}
				}
			}
		}
		if (testIgnored == false) {
			super.runChild(method, notifier);
		}

	}

}

// }
