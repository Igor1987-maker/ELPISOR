package Objects;

import java.util.ArrayList;
import java.util.List;

public class CourseNew extends BasicObject {
	
	List<CourseUnitNew> courseUnits = new ArrayList<CourseUnitNew>();
	
	
	public List<CourseUnitNew> getCourseUnits() {
		return courseUnits;
	}

	public void setCourseUnits(List<CourseUnitNew> courseUnits) {
		this.courseUnits = courseUnits;
	}
	
	public void addUnit(CourseUnitNew courseUnit) {
		courseUnits.add(courseUnit);
	}
	
		
	

}
