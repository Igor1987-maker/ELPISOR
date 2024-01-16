package services;



import junit.framework.TestCase;
import Enums.TestRunnerType;
import Objects.GenericTestObject;

public abstract class GenericService extends GenericTestObject {
	
	
	
//	Reporter reporter;
	TestRunnerType testRunner = null;

	public GenericService() {
		// TODO Auto-generated constructor stub
	}
	
	private void init() {
		
	}
	
	public void sleep(int seconds) throws Exception {
		Thread.sleep(seconds * 1000);
	}
	


	public void setTestRunner(TestRunnerType testRunner) {
		this.testRunner = testRunner;
	}

	public void failTest() {
		// TODO Auto-generated method stub
		
	}

}
