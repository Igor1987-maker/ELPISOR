package tests.misc.testPackage;

import org.junit.Test;

import tests.misc.EdusoftWebTest;

public class secondClass extends EdusoftWebTest {

	
	@Test
	public void test5()throws Exception{
		testResultService.addFailTest("Test 5 fails");
	}
	@Test
	public void test6()throws Exception{
		testResultService.addFailTest("Test 6 fails");
	}
}
