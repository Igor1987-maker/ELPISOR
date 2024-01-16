package Objects;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import services.TextService;

public class StudentTest {

	String userId;
	String componentSubComponentId;
	String courseId;
	String grade;
	DateTime lastUpdate;
	String avarage;
	String timesTaken;

	public StudentTest(String userId, String componentSubComponentId,
			String courseId, String grade, DateTime lastUpdate, String avarage,
			String timesTaken) {
		super();
		this.userId = userId;
		this.componentSubComponentId = componentSubComponentId;
		this.courseId = courseId;
		this.grade = grade;
//		DateTimeFormatter formatter = DateTimeFormat
//				.forPattern("yyyy-MM-dd HH:mm:ss.S");
//		DateTime dt = formatter.parseDateTime(lastUpdate);
		this.lastUpdate = lastUpdate;
//		this.lastUpdate = lastUpdate;
		this.avarage = avarage;
		this.timesTaken = timesTaken;
	}

	public String getUserId() {
		return userId;
	}

	public String getComponentSubComponentId() {
		return componentSubComponentId;
	}

	public String getCourseId() {
		return courseId;
	}

	public String getGrade() {
		return grade;
	}

	public DateTime getLastUpdate() {
		return lastUpdate;
	}

	public String getAvarage() {
		return avarage;
	}

	public String getTimesTaken() {
		return timesTaken;
	}

	public String[] getStringArr() {
		return new String[] { this.userId, this.componentSubComponentId,
				this.courseId, this.grade, this.lastUpdate.toString("yyyy-MM-dd HH:mm:ss.S"), this.avarage,
				this.timesTaken };
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		TextService textService=new TextService();
		
		String str=textService.printStringArray(getStringArr());
		return str;
	}
	
	

}
