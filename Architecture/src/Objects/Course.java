package Objects;

import java.util.ArrayList;
import java.util.List;

public class Course extends BasicObject {
	List<CourseUnit>courseUnits=new ArrayList<CourseUnit>();
	
	private String courseUnit;
	private String unitComponent;
	private int componentStage;
	List<Course> courses = null;
	
	
	public Course(int courseId){
//		courses = edoService.getCourses();
		setName(courses.get(courseId).getName());
		setCourseUnit(courses.get(courseId).getCourseUnits().get(0).getName());
		setUnitComponent(courses.get(courseId).getCourseUnits().get(0).getName());
		setComponentStage(Integer.valueOf( courses.get(courseId).getCourseUnits().get(0).getUnitComponent().get(0).getStageNumber()));
		
	}
	public Course(){
		
	}

	public List<CourseUnit> getCourseUnits() {
		return courseUnits;
	}

	public void setCourseUnits(List<CourseUnit> courseUnits) {
		this.courseUnits = courseUnits;
	}
	
	public void AddUnit(CourseUnit courseUnit){
		courseUnits.add(courseUnit);
	}
	public String getCourseUnit() {
		return courseUnit;
	}
	public void setCourseUnit(String courseUnit) {
		this.courseUnit = courseUnit;
	}
	public String getUnitComponent() {
		return unitComponent;
	}
	public void setUnitComponent(String unitComponent) {
		this.unitComponent = unitComponent;
	}
	public int getComponentStage() {
		return componentStage;
	}
	public void setComponentStage(int componentStage) {
		this.componentStage = componentStage;
	}

}
