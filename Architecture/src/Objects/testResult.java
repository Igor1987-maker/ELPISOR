package Objects;

import java.util.List;

public class testResult {
	private String testName;
	private String testCaseId;
	List<String>failReasons;
	
	
	public testResult(String testName, String testCaseId,
			List<String> failReasons) {
		super();
		this.testName = testName;
		this.testCaseId = testCaseId;
		this.failReasons = failReasons;
	}


	public String getTestName() {
		return testName;
	}


	public String getTestCaseId() {
		return testCaseId;
	}


	public List<String> getFailReasons() {
		return failReasons;
	}
	
	
	

}
