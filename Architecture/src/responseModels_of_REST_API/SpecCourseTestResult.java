package responseModels_of_REST_API;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SpecCourseTestResult {
	
	public void setTestId(int testId) {
		this.testId = testId;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public void setDateOfSubmission(String dateOfSubmission) {
		this.dateOfSubmission = dateOfSubmission;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public void setRecommendedLevel(String recommendedLevel) {
		this.recommendedLevel = recommendedLevel;
	}

	public void setTestType(String testType) {
		this.testType = testType;
	}

	public SpecCourseTestResult() {
		
	}
	
	    public SpecCourseTestResult(int testId, String testName, String courseName, String dateOfSubmission, String score,
			String recommendedLevel, String testType) {
		super();
		this.testId = testId;
		this.testName = testName;
		this.courseName = courseName;
		this.dateOfSubmission = dateOfSubmission;
		this.score = score;
		this.recommendedLevel = recommendedLevel;
		this.testType = testType;
	}
		@JsonProperty("TestId") 
	    public int testId;
	    @JsonProperty("TestName") 
	    public String testName;
	    @JsonProperty("CourseName") 
	    public String courseName;
	    @JsonProperty("DateOfSubmission") 
	    public String dateOfSubmission;
	    @JsonProperty("Score") 
	    public String score;
	    @JsonProperty("RecommendedLevel") 
	    public String recommendedLevel;
	    @JsonProperty("TestType") 
	    public String testType;
	
	
	
}
