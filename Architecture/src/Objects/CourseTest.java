package Objects;

import java.util.List;

import Enums.CourseTests;

public class CourseTest extends BasicObject {
	
	List <CourseTestSection> sections;
	CourseTests courseTestType;

	public CourseTests getCourseTestType() {
		return courseTestType;
	}

	public void setCourseTestType(CourseTests courseTestType) {
		this.courseTestType = courseTestType;
	}

	public List<CourseTestSection> getSections() {
		return sections;
	}

	public CourseTest(List<CourseTestSection> sections, CourseTests courseTestType) {
		super();
		this.sections = sections;
		this.courseTestType = courseTestType;
	}
	
	
	

	

}
