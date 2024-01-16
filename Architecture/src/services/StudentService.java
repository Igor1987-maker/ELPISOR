package services;

import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.swing.JProgressBar;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
//import org.omg.IOP.TAG_RMI_CUSTOM_MAX_STREAM_FORMAT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Enums.InstallationType;
import Enums.StudentObjectType;
import Objects.StudentObject;
import Objects.StudentProgress;
import Objects.StudentTest;
import tests.edo.newux.BasicNewUxTest;

@Service("StudentService")
public class StudentService extends GenericService {

	private static final String SQL_FOR_PROGRESS = "select UserId,CourseId,ItemId,LastUpdate from  progress where userId=";
	private static final String SQL_FOR_TESTS = "select  UserId, ComponentSubComponentId,CourseId,Grade,LastUpdate,Average,TimesTaken from TestResults  where userId=";

	public static int progress;
	
	protected UUID uuid;
	
	protected String randomUUID;
	
	protected static boolean progressPerTask;
	protected static boolean schedulerFlag;
	protected static boolean updateUserUnitProgress=true;
	

	@Autowired
	protected DbService dbService;

	@Autowired
	TextService textService;

	@Autowired
	Configuration configuration;

	@Autowired
	TestResultService testResultService;

	@Autowired
	Reporter report;

	List<Thread> threads = new ArrayList<Thread>();

	public List<StudentObject> getStudentObjectsList(String studentId,
			InstallationType type, StudentObjectType objectType) {

		List<StudentObject> studentObjects = null;

		String sql = null;
		int numOfColumns = 0;

		try {
			if (type == InstallationType.Offline) {
				dbService.setUseOfflineDB(true);

			}

			switch (objectType) {
			case Progress:
				sql = SQL_FOR_PROGRESS + studentId;
				numOfColumns = 4;
				break;
			case Tests:

			default:
				break;
			}

			List<String[]> records = dbService.getStringListFromQuery(sql, 1,
					numOfColumns);
			String[] columns = new String[records.get(0).length];

			for (int i = 0; i < records.size(); i++) {
				String[] str = new String[columns.length];
				for (int j = 0; j < columns.length; j++) {
					str[j] = records.get(i)[j];
				}

				StudentObject studentObject = new StudentObject(str);
				studentObjects.add(studentObject);

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (type == InstallationType.Offline) {
				dbService.setUseOfflineDB(false);

			}
		}
		return studentObjects;

	}

	public List<StudentTest> getMultipleStudentsTests(String[] studentIds,
			InstallationType type) throws Exception {
		List<StudentTest> list = new ArrayList<StudentTest>();
		for (int i = 0; i < studentIds.length; i++) {
			list.addAll(getStudentTests(studentIds[i], type));
		}
		return list;
	}

	public List<StudentProgress> getMultipleStudentsProgress(
			String[] studentIds, InstallationType type) throws Exception {
		return getMultipleStudentsProgress(studentIds, type,
				configuration.getProperty("institution.id"));
	}

	public List<StudentProgress> getMultipleStudentsProgress(
			String[] studentIds, InstallationType type, String institutionId)
			throws Exception {
		List<StudentProgress> list = new ArrayList<StudentProgress>();
		for (int i = 0; i < studentIds.length; i++) {
			List<StudentProgress> studentProgress = getStudentProgress(
					studentIds[i], type, institutionId);
			if (studentProgress.size() > 0) {
				list.addAll(studentProgress);
			}

		}
		return list;
	}

	public List<StudentTest> getStudentTests(String studentId,
			InstallationType type) {

		List<StudentTest> studentTests = new ArrayList<StudentTest>();
		String sql = SQL_FOR_TESTS + studentId;
		try {
			if (type == InstallationType.Offline) {
				dbService.setUseOfflineDB(true);

			}

			List<String[]> testsRecords = dbService.getStringListFromQuery(sql,
					1, 7);
			if (testsRecords.size() > 0) {
				for (int i = 0; i < testsRecords.size(); i++) {

					DateTimeFormatter formatter = DateTimeFormat
							.forPattern("yyyy-MM-dd HH:mm:ss.S");
					DateTime dt = formatter
							.parseDateTime(testsRecords.get(i)[4]);

					StudentTest studentTest = new StudentTest(
							testsRecords.get(i)[0], testsRecords.get(i)[1],
							testsRecords.get(i)[2], testsRecords.get(i)[3], dt,
							testsRecords.get(i)[5], testsRecords.get(i)[6]);
					studentTests.add(studentTest);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (type == InstallationType.Offline) {
				dbService.setUseOfflineDB(false);

			}
		}
		return studentTests;

	}

	public List<StudentProgress> getStudentProgress(String studentId,
			InstallationType type, String institutionId) throws Exception {
		List<StudentProgress> progressList = new ArrayList<StudentProgress>();
		String sql = SQL_FOR_PROGRESS + studentId;
		try {
			if (type == InstallationType.Offline) {
				dbService.setUseOfflineDB(true);
				sql = sql + " and Synchronized is not null";

			}
			// if(type==InstallationType.Online){
			// sql = sql + " and institutionId="+institutionId;
			// }
			List<String[]> progressRecords = dbService.getStringListFromQuery(
					sql, 1, 4);
			if (progressRecords.size() > 0) {
				for (int i = 0; i < progressRecords.size(); i++) {

					// System.out.println(progressRecords.get(i)[0]);
					// System.out.println(progressRecords.get(i)[1]);
					// System.out.println(progressRecords.get(i)[2]);
					// System.out.println(progressRecords.get(i)[3]);

					DateTimeFormatter formatter = DateTimeFormat
							.forPattern("yyyy-MM-dd HH:mm:ss.S");

					DateTime dt = formatter.parseDateTime(progressRecords
							.get(i)[3]);

					StudentProgress progress = new StudentProgress(
							progressRecords.get(i)[0],
							progressRecords.get(i)[1],
							progressRecords.get(i)[2], dt);

					progressList.add(progress);

				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (type == InstallationType.Offline) {
				dbService.setUseOfflineDB(false);

			}
		}

		return progressList;

	}

	public List<String[]> getStudentTestListIntoArrayList(List<StudentTest> list) {
		List<String[]> arrList = new ArrayList<String[]>();
		for (int i = 0; i < list.size(); i++) {
			arrList.add(list.get(i).getStringArr());
		}
		return arrList;
	}

	public List<String[]> getStudentProgressListIntoArrayList(
			List<StudentProgress> list) {
		List<String[]> arrList = new ArrayList<String[]>();
		for (int i = 0; i < list.size(); i++) {
			arrList.add(list.get(i).getStringArr());
		}
		return arrList;
	}

	public List<StudentProgress> getMultipleStudentsProgressFromCsvFile(
			String csvPath) throws Exception {
		List<StudentProgress> list = new ArrayList<StudentProgress>();
		List<String[]> csvList = textService.getStr2dimArrFromCsv(csvPath);
		for (int i = 0; i < csvList.size(); i++) {
			StudentProgress studentProgress = new StudentProgress(
					csvList.get(i)[0], csvList.get(i)[1], csvList.get(i)[2],
					csvList.get(i)[3], "yyyy-MM-dd HH:mm:ss");
			list.add(studentProgress);
		}
		return list;
	}

	public List<StudentTest> getMultipleStudentsTestsFromCsvFile(String csvPath)
			throws Exception {
		List<StudentTest> list = new ArrayList<StudentTest>();

		List<String[]> csvList = textService.getStr2dimArrFromCsv(csvPath);
		for (int i = 0; i < csvList.size(); i++) {

			DateTimeFormatter formatter = DateTimeFormat
					.forPattern("yyyy-MM-dd HH:mm:ss.S");
			DateTime dt = formatter.parseDateTime(csvList.get(i)[4]);
			// DateTime lastUdate;

			StudentTest studentTest = new StudentTest(csvList.get(i)[0],
					csvList.get(i)[1], csvList.get(i)[2], csvList.get(i)[3],
					dt, csvList.get(i)[5], csvList.get(i)[6]);
			list.add(studentTest);
		}
		return list;
	}

	public String[] getInstitutionStudetns(InstallationType type,
			String institutionId) {
		if (type.equals(InstallationType.Offline)) {
			dbService.setUseOfflineDB(true);
		}
		List<String[]> list = new ArrayList<String[]>();
		String[] str = null;

		try {

			String sql = "select UserId from users where InstitutionId="
					+ institutionId
					+ " and UserTypeId=1 and FirstName not in('Admin','st1') ";
			list = dbService.getStringListFromQuery(sql, 1, 1);
			str = new String[list.size()];
			for (int i = 0; i < list.size(); i++) {
				str[i] = list.get(i)[0];
			}

		} catch (Exception e) {

		} finally {
			if (type.equals(InstallationType.Offline)) {
				dbService.setUseOfflineDB(false);
			}
		}
		// System.out.println("Num of students=" + str.length);
		return str;
	}

	public int getTestNumOfQuestions(String testId) throws Exception {
		String csvPath = "files/offline/peru/TestsQuestions.csv";
		List<String[]> testsList = textService.getStr2dimArrFromCsv(csvPath);
		int questions = 0;
		for (int i = 0; i < testsList.size(); i++) {
			if (testsList.get(i)[0].equals(testId)) {
				questions++;
			}
		}
		return questions;
	}

	public List<String> createAndRunProgressSqlRecordsForStudent(
			String studentId, boolean executeQuery) throws Exception {
		List<String> spList = new ArrayList<String>();
		List<String[]> coursesDetails = textService
				.getStr2dimArrFromCsv("files/offline/peru/PeruOfflineCoursesUnits.csv");
		// String sqlText =
		// "exec SetProgress @CourseId=%courseId%,@ItemId=%itemId%,@UserId="
		// + studentId

		// + ",@Last=1,@Seconds=60,@Visited=1,@ComponentTypeId=1";
		for (int i = 0; i < coursesDetails.size(); i++) {
			// String sqlForAdd = sqlText;
			// sqlForAdd = sqlForAdd.replace("%courseId%",
			// coursesDetails.get(i)[0]);
			// sqlForAdd = sqlForAdd.replace("%itemId%",
			// coursesDetails.get(i)[1]);
			// //
			// // spList.add(sqlForAdd);
			// dbService.runStorePrecedure(sqlForAdd, executeQuery,false);
			int last;
			if (i == coursesDetails.size() - 1) {
				last = 1;
			} else {
				last = 0;
			}
			createSingleProgressRecored(studentId, coursesDetails.get(i)[0],
					coursesDetails.get(i)[1], executeQuery, last);

		}
		return spList;

	}

	public void createSingleProgressRecored(String studentId, String courseId,
			String itemId, boolean executeQuery, int last) throws Exception {
		createSingleProgressRecored(studentId, courseId, itemId, 60,
				executeQuery, last);
	}

	public void createSingleProgressRecored(String studentId, String courseId,
			String itemId, int seconds, boolean executeQuery, int last)
			throws Exception {
		
		String lessonId = dbService.getComponentIdByItemId(itemId);
		createSingleProgressRecord_NewLA(studentId, courseId, itemId, executeQuery, 1);
		
		/*
		String sqlTextSetProgress = "exec SetProgress @CourseId=" + courseId + ",@ItemId="
				+ itemId + ",@UserId=" + studentId + ",@Last=" + last
				+ ",@Seconds=" + seconds + ",@Visited=1,@ComponentTypeId=1";
				
		dbService.runStorePrecedure(sqlTextSetProgress, executeQuery, false);
		*/
		// update dbo.UserCourseUnitProgress
		
		String unitId = dbService.getUnitIdByItemId(courseId, itemId);
		SetVisitedComponents(studentId, unitId, lessonId, seconds, itemId);
		setTimeOnLessonInSeconds(studentId, courseId, lessonId, seconds);
		setUserCourseUnitProgress(courseId, unitId, studentId);
	}
	
	
	
	
	
	
	public void createSingleProgressRecord_NewLA(String studentId, String courseId,
			String itemId, boolean executeQuery, int last)
			throws Exception {
		
		String sqlTextSetProgress = "exec SetProgressPerTask @CourseId=" + courseId + ",@ItemId=" + itemId + ",@UserId=" + studentId;
		dbService.runStorePrecedure(sqlTextSetProgress, executeQuery, false);
		
		

		// update dbo.UserCourseUnitProgress
	/*	
		if (last==1){
			//String sqlTextSetUserCourseProgress = "exec SetUserCourseProgress @CourseId="+ courseId +", @UserId="+ studentId+",@Synchronize=0,@IsMyProgress=0";
			
			String unitId = dbService.getUnitIdByItemId(courseId, itemId);
			String sqlTextSetUserCourseProgress = "exec SetUserCourseUnitProgress @CourseId="+ courseId +",@UnitId="+unitId+",@UserId="+ studentId+",@Synchronize=0,@IsMyProgress=0";
			
			//dbService.runStorePrecedure(sqlTextSetUserCourseProgress,executeQuery, false);
			dbService.runStorePrecedure(sqlTextSetUserCourseProgress, true, true);
		}
	*/
				
	}

	public void setProgressForCourse(String courseId, String studentId,
			JProgressBar bar, int seconds) throws Exception {
		setProgressForCourse(courseId, studentId, null, seconds, true, true);
	}

	public void setProgressForCourse(String courseId, String studentId,
			JProgressBar bar, int seconds, boolean useTestItems,
			boolean useBothTestAndProgressItems) throws Exception {
		
		
		List<String> unitsId = dbService.getCourseUnits(courseId);
		
		for (int i=0;i<unitsId.size();i++){
			setProgressForUnit(unitsId.get(i),courseId,studentId,bar,seconds,0,useTestItems,useBothTestAndProgressItems);
			//setProgressForUnit(unitsId.get(i), courseId, studentId,seconds);
		}

	}

	public void setProgressForItems(String courseId, String studentId,
			List<String[]> items, JProgressBar bar, int seconds)
			throws Exception {
		setProgressForItems(courseId, studentId, items, seconds, bar, false);
	}

	public void setProgressForItems(final String courseId,
			final String studentId, final List<String[]> items,
			final int seconds, final JProgressBar bar, boolean useItemCode)
			throws Exception {
		
		setProgressPerTask(true);
		//checkSchedulerFlag(studentId, "3");

		// bar.setMaximum(0);
		// bar.setMaximum(items.size());
		// System.out.println("Size is: " + items.size());
		Thread t = null;
		String itemId="";
		
		String unitId = dbService.getUnitIdByItemId(courseId, items.get(0)[0]);
		String componentId = dbService.getComponentIdByItemId(items.get(0)[0]);
		
	
		for (int i = 0; i < items.size(); i++) {
			progress = i;
			// System.out.println("Progress: " + progress);
			try {
				if (useItemCode) {
					itemId = dbService.getItemIdByItemCode(items.get(progress)[0]);
				} else {
					itemId = items.get(progress)[0];
				}
				int last;
				if (i == items.size() - 1) {
					last = 1;
				} else {
					last = 0;
				}
			
				SetVisitedComponents(studentId,unitId,componentId,seconds/items.size(), itemId);
				
				if (progressPerTask)
					createSingleProgressRecord_NewLA(studentId, courseId, itemId, false, last);
				else
					createSingleProgressRecored(studentId, courseId, itemId, seconds/items.size(), false, last);

				// bar.setValue(i);
			}
			
			catch (Exception e) {
				e.printStackTrace();
			}
			final int percent = progress;
			
			
			
		}// end of for loop
		
	//	if (updateUserUnitProgress)
			// Update UserUnitProgress forScheduler users
	//		SetUserCourseUnitProgress(courseId, studentId, items.get(0)[0]);

		// use thread for progress bar
		// t=new Thread(){
		// public void run(){
		// for (int i = 0; i < items.size(); i++) {
		// progress = i;
		// System.out.println("Progress: " + progress);
		// try {
		// createSingleProgressRecored(studentId, courseId,
		// items.get(progress), seconds, false);
		// bar.setValue(i);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// final int percent = progress;
		// }//end of for loop
		//
		// }
		//
		//
		// };
		// t.start();
		// t.join();

		// frame.add(progressBar);
		// frame.pack();
		// frame.setVisible(true);

	}

	public void SetUserCourseUnitProgress(final String courseId, final String studentId, String itemId)
			throws Exception {
			
			String unitId = dbService.getUnitIdByItemId(courseId,itemId);
			setUserCourseUnitProgress(courseId,unitId,studentId);
	}

	public boolean checkSchedulerFlag(final String studentId, String featureId) throws Exception {
		String institutionId = dbService.getInstitutionIdByUserId(studentId);
		//featureId = "3"; Scheduler
		int rs = dbService.getInstitutionFeatureFlag(institutionId,featureId);
		
		if (rs==1)
			schedulerFlag=true;
		else
			schedulerFlag=false;
		
		return schedulerFlag;
	}

	public void setProgressForUnit(String unitId, String courseId,
			String StudentId) throws Exception {
		setProgressForUnit(unitId, courseId, StudentId, null, 60, 0, false,
				true);
	}

	public void setProgressForUnit(String unitId, String courseId,
			String StudentId, int lastItemsToIgnore) throws Exception {
		setProgressForUnit(unitId, courseId, StudentId, null, 60,
				lastItemsToIgnore, false, false);
	}

	public void setProgressForUnit(String unitId, String courseId,
			String StudentId, int lastItemsToIgnore,
			boolean userBothTestAndProgress) throws Exception {
		setProgressForUnit(unitId, courseId, StudentId, null, 60, lastItemsToIgnore, false, userBothTestAndProgress);
	}

	public void setProgressForUnit(String unitId, String courseId,
			String StudentId, JProgressBar bar, int seconds,
			int lastItemsToIgnore, boolean useTestItems,boolean useTestAndProgress)
					throws Exception {
		
		
		List<String> componentIds = dbService.getLessonsIdsBySequnce(unitId);
		
		for (int i=0;i<componentIds.size();i++){
			setProgressForComponents(componentIds.get(i), courseId, StudentId, bar,seconds,
					lastItemsToIgnore, useTestItems, useTestAndProgress);	
		}
 		
		// Update UserUnitProgress forScheduler users
		setUserCourseUnitProgress(courseId,unitId,StudentId);

	}

	public void setProgressForSubComponent(String subComponentId,
			String studentId, String courseId, int seconds,
			List<String[]> selectedItems, JProgressBar bar) throws Exception {
		setProgressForSubComponent(subComponentId, studentId, courseId,
				seconds, selectedItems, null, false);
	}

	public void setProgressForSubComponent(String subComponentId,
			String studentId, String courseId, int seconds,
			List<String[]> selectedItems, JProgressBar bar, boolean useItemCode)
			throws Exception {
		// List<String> items = dbService.getSubComponentItems(subComponentId);
		setProgressForItems(courseId, studentId, selectedItems, seconds, bar,
				useItemCode);
	}

	public void setProgressForComponents(String componentId, String courseId,
			String studentId, JProgressBar bar, int seconds,
			int lastItemsToIgnore, boolean useBothTestAndProgressItems)
			throws Exception {

		setProgressForComponents(componentId, courseId, studentId, bar,
				seconds, lastItemsToIgnore, false, useBothTestAndProgressItems);
	}

	public void setProgressForComponents(String componentId, String courseId,
			String studentId, JProgressBar bar, int seconds,
			int lastItemsToIgnore, boolean setTestItemsProgress,
			boolean userBothTestAndProgressItems) throws Exception {
		
		List<String[]> items = dbService.getComponentItems(componentId,false, userBothTestAndProgressItems);
		setProgressForItems(courseId, studentId, items, bar, seconds);
		
		if (userBothTestAndProgressItems){
			
			if (dbService.checkIfComponentHasTest(componentId))
			{
			List<String[]> testItems = dbService.getComponentItems(componentId,true,false);
			String unitId="";
			unitId = dbService.getUnitIdByItemId(courseId, testItems.get(0)[0]);
			submitTest(studentId, unitId, componentId, testItems, true);
			}		
		}
		setTimeOnLessonInSeconds(studentId, courseId, componentId, seconds);
	}
	

	// public void setProgressForCompenentInSequence(String componentId,
	// String courseId, String studentId, String sequence,
	// JProgressBar bar, int seconds) throws Exception {
	// String itemId = dbService.getComponentItemBySequence(componentId,
	// sequence);
	// List<String[]> items = new ArrayList<>();
	// items.add(itemId);
	// setProgressForItems(courseId, studentId, items, bar, seconds);
	// }

	// public void setProgressForComponentFirstItem(String componentId,
	// String courseId, String studentId, JProgressBar bar, int seconds)
	// throws Exception {
	// String itemId = dbService.getLastItemInComponent(componentId);
	// List<String> items = new ArrayList<>();
	// items.add(itemId);
	// setProgressForItems(courseId, studentId, items, bar, seconds);
	// }

	// public void setProgressForComponentLastItem(String componentId,
	// String courseId, String studentId, JProgressBar bar, int seconds)
	// throws Exception {
	// String itemId = dbService.getFirstItemInComponent(componentId);
	// List<String> items = new ArrayList<>();
	// items.add(itemId);
	// setProgressForItems(courseId, studentId, items, bar, seconds);
	// }

	public void createAndRunSetSubmitTestSqlRecordsForStudent(String studentId,
			boolean offlineDB) throws Exception {
		List<String[]> coursesDetails = textService
				.getStr2dimArrFromCsv("files/offline/peru/PeruOfflineCoursesUnits.csv");

		String sql = "exec SubmitTest @UserId="
				+ studentId
				+ ",@UnitId=%unitId%,@ComponentId=%compId%,@Grade=%grade%,@Marks='%marks%',@SetId='23277|23278|23279|23280|23281|',@VisitedItems='[1][2][3][4][5]',@TimeOver=0,@UserState=0x7B2261223A5B7B2269436F6465223A22623372706C6F74303031222C22694964223A32333237372C22695479706,@TestTime=3200";

		for (int i = 0; i < coursesDetails.size(); i++) {
			int grade = dbService.getRandonNumber(0, 100);
			List<String> marks = generateMarks(getTestNumOfQuestions(coursesDetails
					.get(i)[1]));
			String sp = sql;
			sp = sp.replace("%unitId%", coursesDetails.get(i)[0]);
			sp = sp.replace("%compId%", coursesDetails.get(i)[1]);
			sp = sp.replace("%marks%", getStringFromMarks(marks));
			sp = sp.replace("%grade%", String.valueOf(calcGrade(marks)));

			// System.out.println(sp);
			dbService.runStorePrecedure(sp, offlineDB);
		}

	}

	private List<String> generateMarks(int testNumOfQuestions) {
		// TODO Auto-generated method stub
		List<String> marks = new ArrayList<String>();
		for (int i = 0; i < testNumOfQuestions; i++) {
			marks.add(String.valueOf(dbService.getRandonNumber(30, 100)));
		}
		return marks;
	}

	public String getStringFromMarks(List<String> marks) {
		String result = "";
		for (int i = 0; i < marks.size(); i++) {
			result += marks.get(i) + "|";
		}
		// result += "'";
		return result;
	}

	public void setProgress(String studentId, String courseId, String itemId)
			throws Exception {
		setProgress(studentId, courseId, itemId, InstallationType.Online);
	}

	public void setProgress(String studentId, String courseId, String itemId,
			InstallationType type) throws Exception {

		try {
			if (type == InstallationType.Offline) {
				dbService.setUseOfflineDB(true);
			}
			String sqlText = "exec SetProgress @CourseId=" + courseId
					+ ",@ItemId=" + itemId + ",@UserId=" + studentId
					+ ",@Last=1,@Seconds=60,@Visited=1,@ComponentTypeId=1";
			if (type == InstallationType.Offline) {
				dbService.setUseOfflineDB(true);
				dbService.runStorePrecedure(sqlText, true);
			} else {
				dbService.runStorePrecedure(sqlText, false);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (type == InstallationType.Offline) {
				dbService.setUseOfflineDB(false);
			}
		}
	}

	public int calcGrade(List<String> marks) {
		int counter = marks.size();
		double sum = 0;
		for (int i = 0; i < marks.size(); i++) {
			sum += Double.valueOf(marks.get(i));
		}
		double result = sum / counter;

		result = Math.round(result);
		// System.out.println(result);
		return (int) result;
		// int result = 0;
		// switch (counter) {
		// case 0:
		// result = 0;
		// case 1:
		// result = 20;
		// case 2:
		// result = 40;
		// case 3:
		// result = 60;
		// case 4:
		// result = 80;
		// case 5:
		// result = 100;
		// }
		//
		// return result;
	}

	public void checkStudentProgress(String studentId, String courseId,
			String itemId) throws Exception {
		checkStudentProgress(studentId, courseId, itemId,
				InstallationType.Online);
	}

	public void checkStudentHasNoProgress(String studentId, String courseId,
			String itemId) throws Exception {
		checkStudentHasNoProgress(studentId, courseId, itemId,
				InstallationType.Online);
	}

	public void checkStudentHasNoProgress(String studentId, String courseId,
			String itemId, InstallationType installationType) throws Exception {
		try {
			if (installationType == InstallationType.Offline) {
				dbService.setUseOfflineDB(true);
			}
			String sql = "select progressId from Progress where UserId="
					+ studentId + " and CourseId=" + courseId + " and ItemId="
					+ itemId;
			String result = dbService.getStringFromQuery(sql, 1, true, 2);

			testResultService
					.assertEquals(false, result != null,
							"Progress was created in the database while it should not!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (installationType == InstallationType.Offline) {
				dbService.setUseOfflineDB(false);
			}
		}
	}

	public void checkStudentProgress(String studentId, String courseId,
			String itemId, InstallationType installationType) throws Exception {
		try {
			if (installationType == InstallationType.Offline) {
				dbService.setUseOfflineDB(true);
			}
			String sql = "select progressId from Progress where UserId="
					+ studentId + " and CourseId=" + courseId + " and ItemId="
					+ itemId;
			String result = dbService.getStringFromQuery(sql, 1, true,10);

			testResultService.assertEquals(true, result != null,
					"No progress was created in the database");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (installationType == InstallationType.Offline) {
				dbService.setUseOfflineDB(false);
			}
		}
	}

	public void createStudenCourseTestGrade(String studentID, String testId,
			String courseId, String unitId, String[] testComponents,
			String[] setIds, int[] numsofmarks,
			InstallationType installationType) throws Exception {

		boolean useOfflineDB = false;
		List<String> marks;
		// if (testComponents.length != grades.length) {
		// testResultService
		// .addFailTest(
		// "test components and grades arrays length is not the same. please check test inputes",
		// true, false);
		// }

		if (installationType == InstallationType.Offline) {
			dbService.setUseOfflineDB(true);
			useOfflineDB = true;
		}
		try {

			report.report("set didTest");
			String startTestSP = "exec  StartExitTest " + studentID;
			dbService.runStorePrecedure(startTestSP, true);

			report.report("set StartExitTest");
			String didTestTSP = "exec  SetDidTest " + studentID;
			if (useOfflineDB) {
				dbService.runStorePrecedure(didTestTSP, true, true);
			} else {
				dbService.runStorePrecedure(didTestTSP, true, true);
			}

			report.report("Submit test and setExitTestGrades for all components");
			String submitTestSPbase = "exec SubmitTest @UserId=%userid%,@UnitId=%unitId%,@ComponentId=%compId%,@Grade=%grade%,@Marks='%marks%',@SetId=%setIds%,@VisitedItems='%visitedItems%',@TimeOver=0,@UserState=0x7B2261223A5B7B2269436F6465223A2262317265616C743031222C22694964223A36313134352C226954797065223A35312C226D223A302C227561223A5B5D7D2C7B2269436F6465223A2262317265616C743032222C22694964223A36313134362C226954797065223A35312C226D223A302C227561223A5B5D7D2C7B2269436F6465223A2262317265616C743033222C22694964223A36313134372C226954797065223A35312C226D223A302C227561223A5B5D7D2C7B2269436F6465223A2262317265616C743034222C22694964223A36313134382C226954797065223A35312C226D223A302C227561223A5B5D7D2C7B2269436F6465223A2262317265616C743035222C22694964223A36313134392C226954797065223A35312C226D223A302C227561223A5B5D7D2C7B2269436F6465223A2262317265616C743036222C22694964223A36313135302C226954797065223A35312C226D223A302C227561223A5B5D7D2C7B2269436F6465223A2262317265616C743037222C22694964223A36313135312C226954797065223A32332C226D223A302C227561223A5B5D7D2C7B2269436F6465223A2262317265616C743038222C22694964223A36313135322C226954797065223A35312C226D223A302C227561223A5B5D7D5D2C226D223A302C2274223A33323030307D,@TestTime=32000";
			String setExitTestGradeSpbase = "exec SetExitTestGrade %userid%,%testId%,%courseId%,%grade%";
			int avgGrade = 0;
			for (int i = 0; i < testComponents.length; i++) {
				marks = generateMarks(numsofmarks[i]);
				avgGrade += calcGrade(marks);
				String grade = String.valueOf(calcGrade(marks));
				report.report("prepair and run submit test SP");
				String submitTestSp = submitTestSPbase;
				submitTestSp = submitTestSp.replace("%userid%", studentID);
				submitTestSp = submitTestSp.replace("%unitId%", unitId);
				submitTestSp = submitTestSp.replace("%grade%", grade);
				submitTestSp = submitTestSp.replace("%marks%",
						getStringFromMarks(marks));
				submitTestSp = submitTestSp.replace("%compId%",
						testComponents[i]);
				submitTestSp = submitTestSp.replace("%setIds%", setIds[i]);
				submitTestSp = submitTestSp.replace("%visitedItems%",
						generateVisitedItemsString(numsofmarks[i]));

				dbService.runStorePrecedure(submitTestSp, useOfflineDB);

				report.report("prepair and run SetExitTestGrade SP");
				String SetExitTestGradeSP = setExitTestGradeSpbase;
				SetExitTestGradeSP = SetExitTestGradeSP.replace("%userid%",
						studentID);
				SetExitTestGradeSP = SetExitTestGradeSP.replace("%testId%",
						testId);
				SetExitTestGradeSP = SetExitTestGradeSP.replace("%courseId%",
						courseId);
				SetExitTestGradeSP = SetExitTestGradeSP.replace("%grade%",
						grade);

				if (useOfflineDB) {
					dbService.runStorePrecedure(SetExitTestGradeSP,
							useOfflineDB, true);
				} else {
					dbService.runStorePrecedure(SetExitTestGradeSP,
							useOfflineDB);
				}

			}
			// System.out.println("Expected grade in DB is: " + avgGrade
			// / testComponents.length);

		} catch (Exception e) {

		} finally {
			if (installationType == InstallationType.Offline) {
				dbService.setUseOfflineDB(false);
			}
		}

	}

	// public StudentTest
	// getTestresultByCompSubCompIdCourseIdAndStudentId(String subCompId,
	// String courseId, String UserId) throws Exception {
	// String sql = "select * from TestResults where CourseId=" + courseId
	// + " and UserId=" + UserId + " and ComponentSubComponentId="
	// + subCompId;
	//
	// List<String[]> result = dbService.getStringListFromQuery(sql, 1, 10);
	// StudentTest studentTest
	// }

	public void submitTest(String studentId, String unitId, String componentId,
			List<String[]> items, boolean createProgress) throws Exception {
		
		submitTest(studentId, unitId, componentId, items, "100", null,createProgress);
	}

	public void submitTest(String studentId, String unitId, String componentId,
			List<String[]> items, String grade, List<String> marksList,
			boolean createProgress) throws Exception {
		
		submitTest(studentId, unitId, componentId, items, grade, marksList,10*items.size(), createProgress);
	}

	public void submitTest(String studentId, String unitId, String componentId,
			List<String[]> items, String grade, List<String> marksList,
			int seconds, boolean createProgress) throws Exception {

		String testItems = "";
		String visitedItems = "";
		int index = 1;
		// int grade = grade;
		String marks = "";

		for (int i = 0; i < items.size(); i++) {
			testItems += items.get(i)[0] + "|";
			visitedItems += "[" + index + "]";
			if (marksList == null) {
				marks += "100|";
			} else {
				marks += marksList.get(i) + "|";
			}

			index++;
		}

		String sql = "exec SubmitTest @UserId="
				+ studentId
				+ ",@UnitId="
				+ unitId
				+ ",@ComponentId="
				+ componentId
				+ ",@Grade="
				+ grade
				+ ",@Marks='"
				+ marks
				+ "',@SetId='"
				+ testItems
				+ "',@VisitedItems='"
				+ visitedItems
				+ "',@TimeOver=0,@UserState=0x7B2261223A5B7B2269436F6465223A22623372706C6F74303031222C22694964223A32333237372C22695479706,@TestTime="
				+ seconds;
		// System.out.println(sql);
		dbService.runStorePrecedure(sql);
		
		//no matter what it the passing grade, each test make a progress
		//int passingGrade = dbService.getSchoolPassingGrade(dbService.getInstitutionIdByUserId(studentId));
		
	
		//if (createProgress && Integer.valueOf(grade) >= passingGrade) {
		
		String courseId = dbService.getCourseIdByUnitId(unitId);
		
		//if (createProgress){ // the design is that the test make a progress
			setProgressForItems(courseId, studentId, items, null, 60);
		//}
		//}
		
		setUserCourseUnitProgress(courseId, unitId, studentId);
	}

	public List<String> getMarksByGrade(int grade, int length) {
		// TODO Auto-generated method stub
		List<String> marks = new ArrayList<String>();
		double level = 100 / length;
		for (double i = 1; i < length + 1; i++) {
			if (i * level <= grade) {
				marks.add("100");
			} else {
				marks.add("0");
			}
		}
		// System.out.println(marks.toString());
		return marks;

	}

	public List<String> getCourseUnits(String courseId) throws Exception {
		String sql = "select unitId from units where CourseId=" + courseId + " Order by sequence";
		return dbService.getArrayListFromQuery(sql, 1);
	}

	public List<String> getUnitComponents(String unitId) throws Exception {
		String sql = "select ComponentId from UnitComponents  where UnitId="
				+ unitId + " order by Sequence";
		return dbService.getArrayListFromQuery(sql, 1);
	}

	public List<String> getUnitTestComponents(String unitId) throws Exception {
		/*String sql = "select ComponentId from UnitComponents  where UnitId="
				+ unitId
				+ " and ComponentId in( select ComponentId  from ComponentSubComponents where"
				+ " SubComponentId=3 and ComponentId "
				+ " in(select ComponentId from UnitComponents where UnitId="
				+ unitId + "))";
		*/
		String sql = "SELECT UnitComponents.ComponentId FROM UnitComponents"
				+" INNER JOIN ComponentSubComponents ON UnitComponents.ComponentId = ComponentSubComponents.ComponentId"
				+" WHERE (ComponentSubComponents.SubComponentId = 3) AND (UnitComponents.UnitId =" + unitId +")"
				+" ORDER BY UnitComponents.Sequence";
		
		return dbService.getArrayListFromQuery(sql, 1);
	}

	public List<String> getCourseTestComponents(String courseId)
			throws Exception {
		String sql = "(select ComponentId from ComponentSubComponents where SubComponentId=3 and ComponentId in(select ComponentId from UnitComponents where UnitId in (select unitId from units where CourseId="
				+ courseId + ")))";
		return dbService.getArrayListFromQuery(sql, 1);
	}

	// public List<String> getComponentTestItems(String componentId)
	// throws Exception {
	// String sql =
	// "select itemId from item where ComponentSubComponentId=(select ComponentSubComponentId from ComponentSubComponents where SubComponentId=3 and ComponentId="
	// + componentId + ")";
	// return dbService.getArrayListFromQuery(sql, 1);
	// }

	public List<String[]> getComponentTestItems(String componentId)
			throws Exception {
		String sql = "select itemId from item where ComponentSubComponentId=(select ComponentSubComponentId from ComponentSubComponents where SubComponentId=3 and ComponentId="
				+ componentId + ")";
		return dbService.getStringListFromQuery(sql, 1, 1);
	}

	private String generateVisitedItemsString(int numbderOfItems) {
		// TODO Auto-generated method stub
		String str = "";
		for (int i = 1; i < numbderOfItems + 1; i++) {
			str += "[" + i + "]";
		}
		// str+="'";

		return str;
	}

	public void deleteStudent(String studentId, String institutionId)
			throws Exception {
		String sp = "exec DeleteFilteredUsers " + institutionId + ",'C',0,'"
				+ studentId + "',null,null,1";
		dbService.runStorePrecedure(sp);
	}

	public void deleteStudentById(String userId) throws Exception {
		dbService.deleteUserById(userId);
	}
	
	
	public String[] getStudentsComponentIdByCourseAndUnit(String className,
			int courseSeq, int unitSeq, int componentSeq) throws Exception {

		String[] returndValues = new String[2];
		List<String[]> courses;
		String courseId = null;
		String componentId = null;
		try {
			courses = dbService.getClassCourses(dbService.getClassIdByName(
					className, configuration.getInstitutionId()));
			courseId = courses.get(courseSeq)[1];
			List<String> units = dbService.getCourseUnits(courseId);
			String unitId = units.get(unitSeq);

			componentId = dbService.getComponentDetailsByUnitId(unitId).get(
					componentSeq)[1];
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		returndValues[0] = componentId;
		returndValues[1] = courseId;
		return returndValues;

	}

	public void setUserProgress(String courseId, String studentId)
			throws SQLException {
		String SP = "exec SetUserCourseProgress " + courseId + "," + studentId+ ",1";
		dbService.runStorePrecedure(SP, true);

	}

	public boolean resetStudentCourseProgress(String studentId, String courseId, String sql)
			throws Exception {
		
		boolean success = false;
		
		//String instId = dbService.getInstitutionIdByUserId(studentId);
		
		/*String studentUserName = dbService.getUserNameById(studentId, instId);
		String courseName = dbService.getCourseNameById(courseid);
		String SP = "exec dbo.APIResetStudentProgressInCourse "
				+ studentUserName + ",'" + courseName + "'," + instId;
		dbService.runStorePrecedure(SP);*/

		sql = sql.replace("%studentId%", studentId);
		sql = sql.replace("%courseId%", courseId);
		sql = sql.replace("%instId%", BasicNewUxTest.institutionId);

		List<String[]> rsList = dbService.getListFromPrepairedStmt(sql, 1);
		if (rsList.get(0)[0].equals("0")) success = true;
				
		return success;
		
	}

	public boolean resetProgressForAllStudents(String institutioId, String courseId, String sql)
			throws Exception {
		
		List<String> studentIds = dbService.getInstitutionStudentsWithProgress(institutioId, courseId);
		boolean success = false;
		int failCount = 0;
		for (int i = 0; i < studentIds.size(); i++) {
			if (checkStudentProgress(studentIds.get(i).toString(), courseId)) {
				success = resetStudentCourseProgress(studentIds.get(i).toString(), courseId, sql);
				if (!success) failCount++;			
			}

		}
		if (failCount>0) success = false;
		return success;
	}

	private boolean checkStudentProgress(String studentId, String courseId)
			throws Exception {
		String sql = "select * from Progress where UserId=" + studentId
				+ " and CourseId=" + courseId;

		String result = dbService.getStringFromQuery(sql, 1, true, 1);
		if (result == null) {
			// System.out.println("progress not found");
			return false;
		} else {
			// System.out.println("progress found");
			return true;
		}
	}

	public String getStudentClass(String studentId) throws Exception {
		String sql = "select classId from ClassUsers where UserId=" + studentId;
		return dbService.getStringFromQuery(sql);
	}

	public void submitTestsForCourse(String studentId, String courseId,
			boolean createProgress) throws Exception {
		List<String> units = getCourseUnits(courseId);
		for (int j = 0; j < units.size(); j++) {
			List<String> components = getUnitTestComponents(units.get(j));
			for (int f = 0; f < components.size(); f++) {
				List<String[]> testItems = getComponentTestItems(components
						.get(f));
				if (testItems.size() > 0) {
					submitTest(studentId, units.get(j), components.get(f),
							testItems, createProgress);
				}
			}
		}
	}

	public void setProgressInLastUnitInCourse(String courseId,
			String studentId, int itemsToIgnore) throws Exception {
		List<String> units = getCourseUnits(courseId);
		setProgressForUnit(units.get(units.size() - 1), courseId, studentId,
				null, 60, itemsToIgnore, false, false);
	}

	public void setProgressInFirstUnitInCourse(String courseId, String studentId)
			throws Exception {

		setProgressInFirstUnitInCourse(courseId, studentId, 0);
	}

	// public void setProgressInFirstUnitInCourse(String courseId,
	// String studentId, int itemsToIgnore) throws Exception {
	// setProgressInFirstUnitInCourse(courseId, studentId, itemsToIgnore);
	// }

	public void setProgressInFirstUnitInCourse(String courseId,
			String studentId, int lastItemsToIgnore) throws Exception {
		List<String> units = getCourseUnits(courseId);
		setProgressForUnit(units.get(0), courseId, studentId, null, 60,
				lastItemsToIgnore, false, true);
	}

	public void setProgressInLastComponentInUnit(int unitIndex,
			String studentId, String courseId) throws Exception {
		String unitId = getCourseUnits(courseId).get(unitIndex);
		List<String> components = getUnitComponents(unitId);
		setProgressForComponents(components.get(components.size() - 1),
				courseId, studentId, null, 60, 0, true);
	}

	public void setProgressInFirstComponentInUnit(int unitIndex,
			String studentId, String courseId) throws Exception {
		String unitId = getCourseUnits(courseId).get(unitIndex);
		List<String> components = getUnitComponents(unitId);
		setProgressForComponents(components.get(0), courseId, studentId, null,
				60, 0, true);
	}
	
	

	public void setProgressInLastSubcomponentInComponent(int unitIndex,
			int componentIndex, String courseId, String studentId)
			throws Exception {

		String unitId = getCourseUnits(courseId).get(unitIndex);
		String componentId = getUnitComponents(unitId).get(componentIndex);
		List<String[]> subComponents = dbService
				.getSubComponentsDetailsByComponentId(componentId);
		String subComponentId = subComponents.get(subComponents.size() - 1)[1];
		setProgressForSubComponent(subComponentId, studentId, courseId, 60,
				dbService.getSubComponentItems(subComponentId), null);
	}

	public void setProgressInFirstSubcomponentInComponent(int unitIndex,
			int componentIndex, String courseId, String studentId)
			throws Exception {

		String unitId = getCourseUnits(courseId).get(unitIndex);
		String componentId = getUnitComponents(unitId).get(componentIndex);
		List<String[]> subComponents = dbService
				.getSubComponentsDetailsByComponentId(componentId);
		String subComponentId = subComponents.get(0)[1];
		setProgressForSubComponent(subComponentId, studentId, courseId, 60,
				dbService.getSubComponentItems(subComponentId), null);
	}

	public double calcLessonExpectedProgress(String lessonId,
			double completedItems) throws NumberFormatException, Exception {
		double numOfItems = Double.valueOf(dbService
				.getLessonItemsNumber(lessonId));
		return completedItems / numOfItems;

	}

	public double calcSubComponentExpecteProgress(String subCompId,
			String studentId) throws Exception {
		double completedIems = 0;
		List<String[]> subComponentItems = dbService
				.getSubComponentItems(subCompId);
		// for (int i = 0; i < subComponentItems.size(); i++) {
		// if (checkItemProgress(subComponentItems.get(i)[0], studentId)) {
		// completedIems = completedIems + 1;
		// }
		// }
		completedIems = Double.valueOf(getProgressCountBySubComponentId(
				subCompId, studentId));
		double numOfItems = Double.valueOf(subComponentItems.size());
		double avg = completedIems / numOfItems;
		if (avg > 0.8) {
			avg = 1.0;
		}
		return avg;
	}

	public String getProgressCountBySubComponentId(String subCompId,
			String studentId) throws Exception {
		String sql = " select COUNT(*) from progress inner join item on Progress.ItemId=Item.ItemId"
				+ " inner join ComponentSubComponents on Item.ComponentSubComponentId=ComponentSubComponents.ComponentSubComponentId "
				+ " where ComponentSubComponents.ComponentSubComponentId="
				+ subCompId + " and Progress.UserId=" + studentId;

		return dbService.getStringFromQuery(sql);

	}

	public double calcComponentExpectedProgress(String componentId,
			String studentId) throws Exception {
		double sum = 0;
		List<String[]> subComponents = dbService
				.getSubComponentsDetailsByComponentId(componentId);
		for (int i = 0; i < subComponents.size(); i++) {
			sum += calcSubComponentExpecteProgress(subComponents.get(i)[1],
					studentId);
			sleep(1);
		}
		return sum / subComponents.size();
	}

	public boolean checkItemProgress(String itemId, String studentId)
			throws Exception {
		String sql = "select * from Progress where UserId=" + studentId
				+ " and ItemId=" + itemId;
		String result = dbService.getStringFromQuery(sql, 1, true, 1);
		if (result == null) {
			return false;
		} else {
			return true;
		}
	}

	public double calcUnitExpectedProgress(String unit, String studentId)
			throws Exception {
		// TODO Auto-generated method stub
		report.startStep("Calc progress for unit: " + unit);
		double sum = 0;
		List<String[]> components = dbService.getComponentDetailsByUnitId(unit);
		sleep(1);
		for (int i = 0; i < components.size(); i++) {
			sum += calcComponentExpectedProgress(components.get(i)[1],studentId);
			sleep(1);
		}
		double progress = sum / components.size();
		sleep(1);
		progress = progress * 100;
		return progress;
	}
	
	public double calcCourseExpectedProgress(String courseId, String studentId)
			throws Exception {
		report.startStep("Calc progress for course: " + courseId);
		
		String sql = "exec getusercourseProgress "+courseId+","+studentId+""; 
		
		List<String[]> courseProgressArray=  dbService.getStringListFromQuery(sql, 1, false);
		String courseCompletion = courseProgressArray.get(0)[0];
		double progress =  Double.parseDouble(courseCompletion);
		
		//exec getusercourseProgress 20000,52322820194787
/*		
		double sum = 0;

		List<String[]> components = dbService.getComponentDetailsByCourseId(courseId);
		for (int i = 0; i < components.size(); i++) {
			sum += calcComponentExpectedProgress(components.get(i)[1], studentId);
			Thread.sleep(300);
		}
*/
		//List<String> units = dbService.getCourseUnits(courseId);
// do remove the comment
/*		
		List<String[]> unitsUserProgress = dbService.getUserUnitProgressAndTestAverage(studentId,courseId);
		
		for (int i=0; i<unitsUserProgress.size(); i++){
			//sum += calcUnitExpectedProgress(units.get(i), studentId);
			sum += Double.parseDouble(unitsUserProgress.get(i)[0]); 
		}
*/
				
//		double progress = sum / components.size();
//		progress = progress * 100;
	
		return progress;
	}
	

	public void checkStudentTestResult(String studentId, String compSubComp)
			throws Exception {
		String sql = "select TestResultsId from TestResults where ComponentSubComponentId="
				+ compSubComp + " and UserId=" + studentId;

		String result = dbService.getStringFromQuery(sql, 1, true, 5);
		if (result == null) {
			testResultService.addFailTest("Test result not found in the DB");
		}

	}
	
	public void checkStudentHasNoTestProgress(String studentId, String compSubComp)
			throws Exception {
		String sql = "select TestResultsId from TestResults where ComponentSubComponentId="
				+ compSubComp + " and UserId=" + studentId;

		String result = dbService.getStringFromQuery(sql, 1, true, 5);
		if (result != null) {
			testResultService.addFailTest("Test Result found in the DB");
		}

	}

	public void clearStudents(String institutionId, String className, int studentsToLeave) throws Exception, SQLException {
			
	
		List<String> students = dbService.getClassStudents(dbService
				.getClassIdByName(className, institutionId));

		for (int i = 0; i < students.size(); i++) {
			if (i > studentsToLeave-1) {
				//dbService.moveStudentToArchive(students.get(i));
				dbService.deleteStudentByName(institutionId, students.get(i));
			}
		}
	}
	
/**
 * 
 * @param studentId
 * @return true=student is logged in, false=user is logged out
 * @throws Exception
 */
	
	public boolean getUserLoginStatus(String studentId) throws Exception {
		String sql = "select LogedIn from Users  where UserId=" + studentId;
		String result = dbService.getStringFromQuery(sql);
		if (result.equals("2000-01-01 00:00:00.0")) {
			return false;
		} else {
			return true;
		}

	}
	
	public String getStudentCompletionCalculated (String studentID, String courseID) throws Exception {
		
		String sql = "select PercentageDone from UserCourseProgress where UserId = "+ studentID +" and CourseId = " + courseID;
		
		String result = dbService.getStringFromQuery(sql);
		
		return result;
	}
	
	public String getStudentTestAvgCalculated (String studentID, String courseID) throws Exception {
		
		String sql = "select TestAvg from UserCourseProgress where UserId = "+ studentID +" and CourseId = " + courseID;
		
		String result = dbService.getStringFromQuery(sql);
		
		return result;
	}
	
	public List<String[]> removeItemsFromStepList(List<String[]> step_items, int howManyToRemove) throws Exception {
		
		for (int i = 1; i <= howManyToRemove; i++) {
			 	step_items.remove(step_items.size()-i);
			}
		
		return step_items;
	}
	
	public void setTimeOnLesson(String userID, String courseId, String lessonId, int timeOnLessonInMinutes) throws Exception {
		
		uuid = UUID.randomUUID();
		randomUUID = uuid.toString();
	    
		uuid = UUID.randomUUID();
		String TabIdGuid = uuid.toString();
	 
		Instant unixTimeStamp = Instant.now();
	    long startTimeStamp = unixTimeStamp.toEpochMilli();
	    long endTimeStamp = unixTimeStamp.plusSeconds(timeOnLessonInMinutes*60).toEpochMilli();
	      
	    Timestamp starTtime=new Timestamp(startTimeStamp);  
	    Timestamp endTime=new Timestamp(endTimeStamp);
		
		String sql = "exec SetTimeOnLesson @UserId=" + userID + ",@LessonId=" + lessonId + ",@CourseId=" + courseId 
				+ ",@UserSessionId=" + "'" + randomUUID + "'"
				+ ",@LessonSessionId=" + "'" + randomUUID + "'"
				+ ",@StartTimeStamp=" + startTimeStamp
				+ ",@EndTimeStamp=" + endTimeStamp
				+ ",@StartServiceTimeStamp=" +"'" + starTtime + "'"
				+ ",@EndServiceTimeStamp=" + "'" + endTime + "'"			
				+ ",@StartTabId=" +"'" + TabIdGuid + "'"
				+ ",@EndTabId=" + "'" + TabIdGuid + "'"
				;
			
		dbService.runStorePrecedure(sql, false, false);
	}
	
public void setTimeOnLessonInSeconds(String userID, String courseId, String lessonId, int timeOnLessonInSeconds) throws Exception {
		
		uuid = UUID.randomUUID();
		randomUUID = uuid.toString();
	    
		//String sql = "Select Top 1 SessionId from UserLog Where UserId = " + userID + " Order By UserLogId DESC";
		//String UserSessionId = dbService.getStringFromQuery(sql);
		
		uuid = UUID.randomUUID();
		String TabIdGuid = uuid.toString();
	 
		Instant unixTimeStamp = Instant.now();
	    long startTimeStamp = unixTimeStamp.toEpochMilli();
	    long endTimeStamp = unixTimeStamp.plusSeconds(timeOnLessonInSeconds).toEpochMilli();
	    
	    Timestamp starTtime=new Timestamp(startTimeStamp);  
	    Timestamp endTime=new Timestamp(endTimeStamp);
	    
	    //Date date = new Date();
        //String timeStamp = new Timestamp(date.getTime()).toString();
	
			String sql = "exec SetTimeOnLesson @UserId=" + userID + ",@LessonId=" + lessonId + ",@CourseId=" + courseId 
					+ ",@UserSessionId=" + "'" + randomUUID + "'"
					+ ",@LessonSessionId=" + "'" + randomUUID + "'"
					+ ",@StartTimeStamp=" + startTimeStamp
					+ ",@EndTimeStamp=" + endTimeStamp
					+ ",@StartServiceTimeStamp=" +"'" + starTtime + "'"
					+ ",@EndServiceTimeStamp=" + "'" + endTime + "'"			
					+ ",@StartTabId=" +"'" + TabIdGuid + "'"
					+ ",@EndTabId=" + "'" + TabIdGuid + "'"
					;
				
		dbService.runStorePrecedure(sql, true, true);
	}
	
	public boolean isProgressPerTask() {
		return progressPerTask;
	}

	public void setProgressPerTask(boolean progressPerTask) {
		StudentService.progressPerTask = progressPerTask;
	}
	
	public void setUserCourseUnitProgress(String courseId, String unitId, String studentId) {
		//String sql = "Exec SetUserCourseUnitProgress " +  courseId + "," + unitId + "," + studentId;
		
		String sql = "exec SetUserCourseUnitProgress @CourseId="+ courseId +",@UnitId="+unitId+",@UserId="+ studentId+",@Synchronize=0,@IsMyProgress=0";
		try {
			dbService.runStorePrecedure(sql, true, true);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setUserUnitProgress(boolean setUserUnitProgress) {
		StudentService.updateUserUnitProgress = setUserUnitProgress;
	}
	
	
	public void SetVisitedComponents(String studentId,String unitId, String componentId,int seconds,String itemId) {
		
		String sql = "exec SetVisitedComponents @UserId="+studentId+",@UnitId="+unitId+",@ComponentId="+componentId+",@Seconds="+seconds+",@ItemId="+itemId+"";
		
		try {
			dbService.runStorePrecedure(sql, true, true);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	
}
