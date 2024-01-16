package Interfaces;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import Enums.Browsers;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.FIELD })
public @interface TestCaseParams {
	/**
	 * This must be set so that result will be sent to MTM. can contains more
	 * then one test case id
	 * 
	 * @return
	 */
	String[] testCaseID();

	/**
	 * Fill this if a test is not stable/should not be run on 1/some browsers.
	 * The method that actually ignore the skipped browsers is in ExtendedRunner
	 * class
	 * 
	 * @return
	 */
	String[] skippedBrowsers() default "";

	Browsers testedBrowser() default Browsers.empty;

	/**
	 * set True if you want to ignore the default test timeout (10 minutes
	 * normally)
	 * 
	 * @return
	 */
	boolean ignoreTestTimeout() default false;

	String testTimeOut() default "0";

	String envFile() default "";

	/**
	 * If you do not want to use the institution defined in the properies file,
	 * set your institution id here
	 * 
	 * @return
	 */
	String institutionId() default "";

	/**
	 * Currently not working well
	 * 
	 * @return
	 */
	boolean testMultiple() default false;

	/**
	 * for SR tests (auto allow mic in the browser
	 * @return
	 */
	boolean allowMedia() default false;

}
