package responseModels_of_REST_API;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Course {

	@JsonProperty("CourseId") 
    private Integer courseId;
	private String courseName;
    @JsonProperty("PictureId") 
    private String pictureId;
    @JsonProperty("CEFRName") 
    private String cEFRName;
    @JsonProperty("LicenseExpiration")
    private String licenseExpiration;
    private String coursePlan;
	@JsonProperty("ToeicCourseId")
	private Integer toeicCourseId;
	
    public Course() {
    	
    }
    
    
    public Course(Integer courseId, String courseName, String pictureId, String cEFRName, String licenseExpiration,
			String coursePlan) {
		super();
		this.courseId = courseId;
		this.courseName = courseName;
		this.pictureId = pictureId;
		this.cEFRName = cEFRName;
		this.licenseExpiration = licenseExpiration;
		this.coursePlan = coursePlan;
	}


	public Integer getCourseId() {
		return courseId;
	}


	public String getCourseName() {
		return courseName;
	}


	public String getPictureId() {
		return pictureId;
	}


	public String getcEFRName() {
		return cEFRName;
	}


	public String getLicenseExpiration() {
		return licenseExpiration;
	}


	public String getCoursePlan() {
		return coursePlan;
	}


	public void setCourseId(Integer courseId) {
		this.courseId = courseId;
	}


	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}


	public void setPictureId(String pictureId) {
		this.pictureId = pictureId;
	}


	public void setcEFRName(String cEFRName) {
		this.cEFRName = cEFRName;
	}


	public void setLicenseExpiration(String licenseExpiration) {
		this.licenseExpiration = licenseExpiration;
	}


	public void setCoursePlan(String coursePlan) {
		this.coursePlan = coursePlan;
	}
    
    
    
    
    
    
    
}
