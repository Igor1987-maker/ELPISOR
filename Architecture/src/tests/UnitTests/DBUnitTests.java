package tests.UnitTests;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import Interfaces.TestCaseParams;
import tests.misc.EdusoftBasicTest;

public class DBUnitTests extends EdusoftBasicTest {

	
	@Before
	public void setup()throws Exception{
		super.setup();
	}
	
	@Test
	@TestCaseParams(testCaseID = { "20606" },skippedBrowsers={"chrome"})
	
	public void testGetResultFromQuery()throws Exception
	{
		String sql="select Top 1 * from Users ";
		String result=dbService.getStringFromQuery(sql);
		Assert.assertNotNull(result);
	}
	
	@After
	public void tearDown()throws Exception{
		
	}

}
