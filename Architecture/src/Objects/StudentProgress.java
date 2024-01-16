package Objects;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import services.TextService;

public class StudentProgress extends StudentObject {

	String userId;
	String courseId;
	String itemId;
	DateTime lastUpdate;

	public StudentProgress(String userId, String courseId, String itemId,
			DateTime lastUpdate) {

		// return new StudentProgress(userId, courseId, itemId, lastUpdate,
		// "yyyy-MM-dd HH:mm:ss.S");

		super();
		this.userId = userId;
		this.courseId = courseId;
		this.itemId = itemId;

		// System.out.println(dateTime.toString("dd-MMM-yy hh:mm:ss aa"));
		//
		// DateTimeFormatter formatter = DateTimeFormat
		// .forPattern("yyyy-MM-dd hh:mm:ss.S");

//		DateTimeFormatter formatter = DateTimeFormat
//				.forPattern("yyyy-MM-dd HH:mm:ss.S");
//		
//
//		DateTime dt = formatter.parseDateTime(lastUpdate);
		this.lastUpdate = lastUpdate;
	}

	public StudentProgress(String userId, String courseId, String itemId,
			String lastUpdate, String pattern) {
		super();
		this.userId = userId;
		this.courseId = courseId;
		this.itemId = itemId;

		// System.out.println(dateTime.toString("dd-MMM-yy hh:mm:ss aa"));
		//
		// DateTimeFormatter formatter = DateTimeFormat
		// .forPattern("yyyy-MM-dd hh:mm:ss.S");

		DateTimeFormatter formatter = DateTimeFormat.forPattern(pattern);

		DateTime dt = formatter.parseDateTime(lastUpdate);
		this.lastUpdate = dt;
	}
	
	

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		TextService textService=new TextService();
		return textService.printStringArray(getStringArr());
	}

	public String[] getStringArr() {
		// TODO Auto-generated method stub
		return new String[] { this.userId, this.courseId, this.itemId,
				this.lastUpdate.toString("yyyy-MM-dd HH:mm:ss") };
	}

	public String getUserId() {
		return userId;
	}

	public String getCourseId() {
		return courseId;
	}

	public String getItemId() {
		return itemId;
	}

	public DateTime getLastUpdate() {
		return lastUpdate;
	}

}
