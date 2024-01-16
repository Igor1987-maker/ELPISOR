package tests.misc;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import Interfaces.TestCaseParams;
import testCategories.unstableTests;

public class MavenTestsClass extends EdusoftWebTest {
	
	
	@Test
	@TestCaseParams(testCaseID = { "20556" })
	@Category(unstableTests.class)
	public void test1()throws Exception{
		
	}
	
	@Test
	@TestCaseParams(testCaseID = { "20606" })
	@Category(unstableTests.class)
	public void test2()throws Exception{
		
	}
	
	@Test
	@TestCaseParams(testCaseID = { "21633" })
	@Category(unstableTests.class)
	public void test3()throws Exception{
		
	}

}
