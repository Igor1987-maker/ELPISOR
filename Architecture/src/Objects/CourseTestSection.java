package Objects;

import java.util.ArrayList;
import java.util.List;

import Enums.CourseCodes;
import Enums.CourseTests;
import Enums.PLTCycleType;
import Enums.PLTStartLevel;

public class CourseTestSection extends BasicObject {
	
	private int sectionNumber;
	private CourseCodes courseCode;
	private CourseTests courseTestType;
	private List<TestQuestion> sectionQuestions = new ArrayList<>();

	public int getSectionNumber() {
		return sectionNumber;
	}

	public CourseCodes getCourseCode() {
		return courseCode;
	}
	
	public CourseTests getcourseTestType() {
		return courseTestType;
	}

	public List<TestQuestion> getSectionQuestions() {
		return sectionQuestions;
	}
	
	public CourseTestSection (int sectionNumber, CourseCodes courseCode, CourseTests courseTestType, List<TestQuestion> sectionQuestions) {
		
		super();
		
		this.sectionNumber = sectionNumber;
		this.courseCode = courseCode;
		this.courseTestType = courseTestType;
		this.sectionQuestions = sectionQuestions;
		
	}

	public CourseTestSection() {
		// TODO Auto-generated constructor stub
	}
	
	public int getNumberOfQuestions()throws Exception{
		return sectionQuestions.size();
	}
	
	public void setQuestions(List<TestQuestion>questions){
		this.sectionQuestions.addAll(questions);
	}

}
