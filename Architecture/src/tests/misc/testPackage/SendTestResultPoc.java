package tests.misc.testPackage;

import org.junit.Test;

import tests.misc.EdusoftWebTest;

public class SendTestResultPoc extends EdusoftWebTest {
	
	@Test
	public void test1()throws Exception{
		pageHelper.loginAsStudent();
		

	}
	@Test
	public void test2()throws Exception{
		testResultService.addFailTest("Test 2 fails");
	}
	@Test
	public void test3()throws Exception{
		testResultService.addFailTest("Test 3 fails");
	}
	@Test
	public void test4()throws Exception{
		testResultService.addFailTest("Test 4 fails");
	}

}
