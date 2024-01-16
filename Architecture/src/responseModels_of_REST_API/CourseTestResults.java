package responseModels_of_REST_API;

import java.util.List;

public class CourseTestResults {
	public List<SpecCourseTestResult> getListOfCourseResults() {
		return listOfCourseResults;
	}

	public void setListOfCourseResults(List<SpecCourseTestResult> listOfCourseResults) {
		this.listOfCourseResults = listOfCourseResults;
	}

	public CourseTestResults() {
		
	}
	
	public CourseTestResults(List<SpecCourseTestResult> listOfCourseResults) {
		super();
		this.listOfCourseResults = listOfCourseResults;
	}

	private List<SpecCourseTestResult> listOfCourseResults;

	
	
	
}
