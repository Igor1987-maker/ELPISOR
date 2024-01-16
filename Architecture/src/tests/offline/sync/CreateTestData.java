package tests.offline.sync;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import Enums.InstallationType;
import Interfaces.TestCaseParams;
import tests.misc.EdusoftBasicTest;

public class CreateTestData extends EdusoftBasicTest {

	public void setSubmitTestForOffline() throws Exception {
		String csvFilePath = "smb://storage//QA//Automation//testData//forOffline//input//submitTest.csv";
		List<String[]> list = new ArrayList<String[]>();
		// String sqlFilePath =
		// "\\\\storage\\QA\\Automation\\testData\\forOffline\\output";
		String sqlFilePath = "smb://storage//QA//Automation//testData//forOffline//output/submit.sql";
		String studentId = null;

		List<String> sqlTextForFile = new ArrayList<String>();

		System.out.println("Import data from csv file");
		list = textService.getStr2dimArrFromCsv(csvFilePath, true);

		System.out.println("Iterate on csv file data");
		String componentId;

		studentId = list.get(0)[15].toString();

		String tempCompId = "";
		String sqlText = "exec SubmitTest @UserId=%userId%"
				+ ",@UnitId=%unitId%,@ComponentId=%compId%,@Grade=%grade%,@Marks=%marks%,@SetId='23277|23278|23279|23280|23281|',@VisitedItems='[1][2][3][4][5]',@TimeOver=0,@UserState=0x7B2261223A5B7B2269436F6465223A22623372706C6F74303031222C22694964223A32333237372C22695479706,@TestTime=3200";
		List<String> marks = new ArrayList<String>();
		String sql;
		for (int i = 0; i < list.size(); i++) {

			componentId = list.get(i)[12].toString();

			if (!tempCompId.equals(componentId)) {
				// new component
				try {
					tempCompId = componentId;

					System.out
							.println("Create the query and add it to sqlTextForFile");
					sql = sqlText;
					sql = sql.replace("%userId%", list.get(i)[15].toString());
					sql = sql.replace("%unitId%", list.get(i)[11].toString());
					sql = sql.replace("%compId%", componentId);
					sql = sql.replace("%marks%",
							studentService.getStringFromMarks(marks));
					sql = sql.replace("%grade%",
							String.valueOf(studentService.calcGrade(marks)));

					sqlTextForFile.add(sql);
					System.out.println(sql);
					marks.clear();
				} catch (ArrayIndexOutOfBoundsException e) {
					// TODO Auto-generated catch block
					System.out
							.println("A field may be missing from the CSV file");
					testResultService
							.addFailTest("A field may be missing from the CSV file");
				}

			} else {
				// same component
				// grades.add(list.get(i)[15].toString());
				marks.add(list.get(i)[14].toString());
			}
			tempCompId = componentId;

			// textService.writeListToCsvFile(sqlFilePath, sqlTextForFile);

		}
		textService.writeListToSmbFile(sqlFilePath, sqlTextForFile,
				netService.getDomainAuth());

	}

	public void setSubmitTestForOnline() throws Exception {
		String csvFilePath = "smb://storage//QA//Automation//testData//forOnline//input//submitTest.csv";
		List<String[]> list = new ArrayList<String[]>();
		// String sqlFilePath =
		// "\\\\storage\\QA\\Automation\\testData\\forOffline\\output";
		String sqlFilePath = "smb://storage//QA//Automation//testData//forOnline//output/submit.sql";
		String studentId = null;

		List<String> sqlTextForFile = new ArrayList<String>();

		System.out.println("Import data from csv file");
		list = textService.getStr2dimArrFromCsv(csvFilePath, true);

		System.out.println("Iterate on csv file data");
		String componentId;

		studentId = list.get(0)[15].toString();

		String tempCompId = "";
		String sqlText = "exec SubmitTest @UserId=%userId%"
				+ ",@UnitId=%unitId%,@ComponentId=%compId%,@Grade=%grade%,@Marks=%marks%,@SetId='23277|23278|23279|23280|23281|',@VisitedItems='[1][2][3][4][5]',@TimeOver=0,@UserState=0x7B2261223A5B7B2269436F6465223A22623372706C6F74303031222C22694964223A32333237372C22695479706,@TestTime=3200";
		List<String> marks = new ArrayList<String>();
		String sql;
		for (int i = 0; i < list.size(); i++) {

			componentId = list.get(i)[12].toString();

			if (!tempCompId.equals(componentId)) {
				// new component
				tempCompId = componentId;

				System.out
						.println("Create the query and add it to sqlTextForFile");
				sql = sqlText;
				sql = sql.replace("%userId%", list.get(i)[15].toString());
				sql = sql.replace("%unitId%", list.get(i)[11].toString());
				sql = sql.replace("%compId%", componentId);
				sql = sql.replace("%marks%",
						studentService.getStringFromMarks(marks));
				sql = sql.replace("%grade%",
						String.valueOf(studentService.calcGrade(marks)));

				sqlTextForFile.add(sql);
				System.out.println(sql);
				marks.clear();

			} else {
				// same component
				// grades.add(list.get(i)[15].toString());
				marks.add(list.get(i)[14].toString());
			}
			tempCompId = componentId;

			// textService.writeListToCsvFile(sqlFilePath, sqlTextForFile);

		}
		textService.writeListToSmbFile(sqlFilePath, sqlTextForFile,
				netService.getDomainAuth());

	}

	public void setStudentProgressForOffline() throws Exception {
		// String csvFilePath =
		// "\\\\storage\\QA\\Automation\\testData\\forOffline\\input\\setProgress.csv";

		String csvFilePath = "smb://storage//QA//Automation//testData//forOffline//input//setProgress.csv";

		List<String[]> list = new ArrayList<String[]>();
		// String sqlFilePath =
		// "\\\\storage\\QA\\Automation\\testData\\forOffline\\output";
		String sqlFilePath = "smb://storage//QA//Automation//testData//forOffline//output/setProgress.sql";
		String studentId = null;

		List<String> sqlTextForFile = new ArrayList<String>();

		System.out.println("Import data from csv file");
		list = textService.getStr2dimArrFromCsv(csvFilePath, true);
		studentId = list.get(0)[14];
		System.out.println("Iterate on csv file data");
		String componentId;
		String sqlText = "exec SetProgress @CourseId=%courseId%,@ItemId=%itemId%,@UserId="
				+ studentId
				+ ",@Last=1,@Seconds=60,@Visited=1,@ComponentTypeId=1";

		List<String> marks = new ArrayList<String>();
		String sql;
		for (int i = 0; i < list.size(); i++) {
			sql = sqlText;
			sql = sql.replace("%courseId%", list.get(i)[10]);
			sql = sql.replace("%itemId%", list.get(i)[13]);
			sqlTextForFile.add(sql);
			System.out.println(sql);
		}
		textService.writeListToSmbFile(sqlFilePath, sqlTextForFile,
				netService.getDomainAuth());

	}

	public void setStudentProgressForOnline() throws Exception {
		String csvFilePath = "smb://storage//QA//Automation//testData//forOnline//input//setProgress.csv";
		List<String[]> list = new ArrayList<String[]>();
		// String sqlFilePath =
		// "\\\\storage\\QA\\Automation\\testData\\forOffline\\output";
		String sqlFilePath = "smb://storage//QA//Automation//testData//forOnline//output/setProgress.sql";
		String studentId = null;

		List<String> sqlTextForFile = new ArrayList<String>();

		System.out.println("Import data from csv file");
		list = textService.getStr2dimArrFromCsv(csvFilePath, true);
		studentId = list.get(0)[14];
		System.out.println("Iterate on csv file data");
		String componentId;
		String sqlText = "exec SetProgress @CourseId=%courseId%,@ItemId=%itemId%,@UserId=%userId%"

				+ ",@Last=1,@Seconds=60,@Visited=1,@ComponentTypeId=1";

		List<String> marks = new ArrayList<String>();
		String sql;
		for (int i = 0; i < list.size(); i++) {
			sql = sqlText;
			sql = sql.replace("%userId%", list.get(i)[14]);
			sql = sql.replace("%courseId%", list.get(i)[10]);
			sql = sql.replace("%itemId%", list.get(i)[13]);
			sqlTextForFile.add(sql);
			System.out.println(sql);
		}
		textService.writeListToSmbFile(sqlFilePath, sqlTextForFile,
				netService.getDomainAuth());

	}

	// public List<String> createProgressForMultipleStudents(
	// InstallationType type, String isstitutionId) throws Exception {
	// String[] students = studentService.getInstitutionStudetns(type,
	// isstitutionId);
	// List<String> spList = new ArrayList<String>();
	// for (int i = 0; i < students.length; i++) {
	// spList.addAll(studentService
	// .createAndRunProgressSqlRecordsForStudent(students[i]));
	// }
	// return spList;
	// }

	// public List<String> createAndRunProgressSqlRecordsForStudent(
	// String studentId) throws Exception {
	// List<String> spList = new ArrayList<String>();
	// List<String[]> coursesDetails = textService
	// .getStr2dimArrFromCsv("files/offline/peru/PeruOfflineCoursesUnits.csv");
	// String sqlText =
	// "exec SetProgress @CourseId=%courseId%,@ItemId=%itemId%,@UserId="
	// + studentId
	//
	// + ",@Last=1,@Seconds=60,@Visited=1,@ComponentTypeId=1";
	// for (int i = 0; i < coursesDetails.size(); i++) {
	// String sqlForAdd = sqlText;
	// sqlForAdd = sqlForAdd.replace("%courseId%",
	// coursesDetails.get(i)[0]);
	// sqlForAdd = sqlForAdd.replace("%itemId%", coursesDetails.get(i)[1]);
	// //
	// // spList.add(sqlForAdd);
	// dbService.runStorePrecedure(sqlForAdd);
	//
	// }
	// return spList;
	//
	// }

	@Test
	@TestCaseParams(testCaseID = { "" }, testTimeOut = "10000")
	public void unitTestcreateProgressSqlRecordsForStudent() throws Exception {
		// String studentId="655054700082";
		// String instId="5231878";
		// String
		// []studentIds=studentService.getInstitutionStudetns(InstallationType.Online,
		// instId);
		// String filePath="files/offline/peru/"+instId+".sql";
		// File file=new File(filePath);
		// //clear file
		// PrintWriter writer = new PrintWriter(file);
		// writer.print("");
		// writer.close();
		// for(int i=0;i<studentIds.length;i++){
		// textService.writeListToCsvFile(filePath,
		// createProgressSqlRecordsForStudent(studentIds[i]),true);
		// }
		// createProgressForMultipleStudents(InstallationType.Online,
		// "5231874");
		boolean executeQuery;
		boolean offlineDB = false;

		if (offlineDB) {
			executeQuery = true;
		} else {
			executeQuery = false;
		}

		dbService.setUseOfflineDB(offlineDB);

		String[] students = new String[] { "655041700230" };
		// String[]students=studentService.getInstitutionStudetns(InstallationType.Offline,
		// "6550370");
		dbService.setUseOfflineDB(offlineDB);
		for (int i = 0; i < students.length; i++) {
			studentService.createAndRunProgressSqlRecordsForStudent(
					students[i], executeQuery);
		}

	}

	@Test
	public void unitTestcreateTestResultsSqlRecordsForStudent()
			throws Exception {
		// String studentId="655054700082";
		// String instId="5231878";
		// String
		// []studentIds=studentService.getInstitutionStudetns(InstallationType.Online,
		// instId);
		// String filePath="files/offline/peru/"+instId+".sql";
		// File file=new File(filePath);
		// //clear file
		// PrintWriter writer = new PrintWriter(file);
		// writer.print("");
		// writer.close();
		// for(int i=0;i<studentIds.length;i++){
		// textService.writeListToCsvFile(filePath,
		// createProgressSqlRecordsForStudent(studentIds[i]),true);
		// }
		// createProgressForMultipleStudents(InstallationType.Online,
		// "5231874");
		boolean executeQuery;
		boolean offlineDB = false;

		if (offlineDB) {
			executeQuery = true;
		} else {
			executeQuery = false;
		}

		String[] students = new String[] { "655041700230" };
		// String[]students=studentService.getInstitutionStudetns(InstallationType.Offline,
		// "6550370");
		if (offlineDB) {
			dbService.setUseOfflineDB(true);
		}

		for (int i = 0; i < students.length; i++) {
			// studentService.createAndRunSetSubmitTestSqlRecordsForStudent(
			// students[i], executeQuery);
			String classId = studentService.getStudentClass(students[i]);
			List<String[]> courses = dbService.getClassCourses(classId);

			for (int h = 0; h < courses.size(); h++) {
				String courseName = courses.get(h)[0];
				// textInfo.setText("Creating test for course: " + courseName);
				String courseId = courses.get(h)[1];
				studentService.submitTestsForCourse(students[i], courseId,true);
			}
		}

	}

	

	@Test
	// improved
	public void setCourseTestGradesOffline() throws Exception {
		String studentId = "655004100096";
		String courseId = "3797";
		String testId = "20164";
		// String testId="20144";
		String unitId = "22015";
		int[] numsofmarks = new int[] { 5, 5, 5, 5, 8 };
		String[] setIds = new String[] { "'61122|61123|61124|61125|61126|'",
				"'61128|61129|61130|61131|61132|'",
				"'61134|61135|61136|61137|61138|'",
				"'61140|61141|61142|61143|61144|'",
				"'61145|61146|61147|61148|61149|61150|61151|61152|'" };
		// String[]grades=new String[]{"0","20","40","60","40"};
		// String []marks=new
		// String[]{"'0|0|0|0|0|0|0|0|'","'100|0|0|0|0|'","'100|0|0|0|100|'","'100|0|100|0|100|'","'100|0|0|0|100|'"};
		// String []testComp=new
		// String[]{"27260","27257","27258","27256","27259"};
		String[] testComp = new String[] { "27256", "27257", "27258", "27259",
				"27260" };

		studentService
				.createStudenCourseTestGrade(studentId, testId, courseId,
						unitId, testComp, setIds, numsofmarks,
						InstallationType.Offline);
	}

	@Test
	public void setCourseTestGradesOnline() throws Exception {
		String studentId = "655020700154";
		String courseId = "3797";
		String testId = "20164";
		// String testId="20144";
		String unitId = "22015";
		int[] numsofmarks = new int[] { 5, 5, 5, 5, 8 };
		String[] setIds = new String[] { "'61122|61123|61124|61125|61126|'",
				"'61128|61129|61130|61131|61132|'",
				"'61134|61135|61136|61137|61138|'",
				"'61140|61141|61142|61143|61144|'",
				"'61145|61146|61147|61148|61149|61150|61151|61152|'" };
		// String[]grades=new String[]{"0","20","40","60","40"};
		// String []marks=new
		// String[]{"'0|0|0|0|0|0|0|0|'","'100|0|0|0|0|'","'100|0|0|0|100|'","'100|0|100|0|100|'","'100|0|0|0|100|'"};
		// String []testComp=new
		// String[]{"27260","27257","27258","27256","27259"};
		String[] testComp = new String[] { "27256", "27257", "27258", "27259",
				"27260" };

		studentService.createStudenCourseTestGrade(studentId, testId, courseId,
				unitId, testComp, setIds, numsofmarks, InstallationType.Online);
	}

	@Test
	public void setCourseMidTermTestGradesOnline() throws Exception {
		String studentId = "655041000126";
		String courseId = "3797";
		String testId = "20144";
		String unitId = "22014";
		String[] setIds = new String[] { "'61090|61091|61092|61093|61094|'",
				"'61096|61097|61098|61099|61100|'",
				"'61102|61103|61104|61105|61106|'",
				"'61108|61109|61110|61111|61112|'",
				"'61113|61114|61115|61116|61117|61118|61119|61120|'" };
		int[] numsofmarks = new int[] { 5, 5, 5, 5, 8 };
		// String[]grades=new String[]{"0","20","40","60","40"};
		// String []marks=new
		// String[]{"'0|0|0|0|0|0|0|0|'","'100|0|0|0|0|'","'100|0|0|0|100|'","'100|0|100|0|100|'","'100|0|0|0|100|'"};
		String[] testComp = new String[] { "27251", "27252", "27253", "27254",
				"27255" };

		studentService.createStudenCourseTestGrade(studentId, testId, courseId,
				unitId, testComp, setIds, numsofmarks, InstallationType.Online);
	}

	@Test
	public void setCourseMidTermTestGradesOffline() throws Exception {
		String studentId = "655004100129";
		String courseId = "3797";
		String testId = "20144";
		String unitId = "22014";
		String[] setIds = new String[] { "'61090|61091|61092|61093|61094|'",
				"'61096|61097|61098|61099|61100|'",
				"'61102|61103|61104|61105|61106|'",
				"'61108|61109|61110|61111|61112|'",
				"'61113|61114|61115|61116|61117|61118|61119|61120|'" };
		int[] numsofmarks = new int[] { 5, 5, 5, 5, 8 };
		// String[]grades=new String[]{"0","20","40","60","40"};
		// String []marks=new
		// String[]{"'0|0|0|0|0|0|0|0|'","'100|0|0|0|0|'","'100|0|0|0|100|'","'100|0|100|0|100|'","'100|0|0|0|100|'"};
		String[] testComp = new String[] { "27251", "27252", "27253", "27254",
				"27255" };

		studentService
				.createStudenCourseTestGrade(studentId, testId, courseId,
						unitId, testComp, setIds, numsofmarks,
						InstallationType.Offline);
	}
}
