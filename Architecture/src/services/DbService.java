package services;

import Enums.UserType;
import Objects.Institution;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;


/**
 * 
 * @author omers
 *
 */
@Service("DbService")
public class DbService extends GenericService {

	private static final String SQL_SERVER_DRIVER_CLASS = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	private JdbcTemplate jdbcTemplate;
	// private final String db_connect_string =
	// "jdbc:sqlserver://BACKQA:1433;databaseName=EDODOTNet3;";
	private String db_userid = null;
	private String db_password = null;

	protected int MAX_DB_TIMEOUT = 60;

	private boolean useOfflineDB;
	private boolean useCI;
	private boolean useStaticContent;
	private boolean useStaticContentProduction;
	private boolean usingCIDB;
	private boolean useProd2DB;
	private boolean useToeicDB;
	private boolean useEdProductionDB;
	private boolean useEdBetaProductionDB;
	private boolean usingTfsDB;
	private boolean usingDWDB;
	private boolean useETSCertificatesDB = false;
	private boolean useToeicOnlineDB = false;
	public boolean useEDMerge2 = false;
	public static String currentStaticDB = "";
	public static String currentGeneralDB = "";
	public static String edMerge2DB = "";
	//public static String currentToeicOLPC_Db = "";

	boolean usingEDDB = true;

	public boolean isUsingCIDB() {
		return usingCIDB;
	}

	public void setUsingCIDB(boolean usingCIDB) {
		this.usingCIDB = usingCIDB;
	}

	public void setEdDB(boolean SetEdDB) {
		this.usingEDDB = SetEdDB;
	}

	java.util.logging.Logger logger;

	public boolean logProfiler;

	// private DataSource dataSou rce;

	public boolean isUseOfflineDB() {
		return useOfflineDB;
	}

	public boolean isUseProd2DB() {
		return useProd2DB;
	}

	public boolean isUseEdProductionDB() {
		return useEdProductionDB;
	}

	public boolean isUseEdBetaProductionDB() {
		return useEdBetaProductionDB;
	}

	public boolean isUseEdDB() {
		return usingEDDB;
	}

	public boolean isUseusingTfsDB() {
		return usingTfsDB;
	}

	public void setUseOfflineDB(boolean useOfflineDB) {
		this.useOfflineDB = useOfflineDB;
	}

	public void setUseProd2DB(boolean useProd2DB) {
		this.useProd2DB = useProd2DB;
	}

	public void setUseEdproductionDb(boolean useEdProductionDB) {
		this.useEdProductionDB = useEdProductionDB;
	}

	public void setUseEdBetaproductionDb(boolean useEdBetaProductionDB) {
		this.useEdBetaProductionDB = useEdBetaProductionDB;
	}

	public void setUsingTfsDB(boolean usingTfsDB) {
		this.usingTfsDB = usingTfsDB;
	}

	public void setUsingDWDB(boolean useDB) {
		this.usingDWDB = useDB;
	}

	@Autowired
	protected Configuration configuration;

	// private String db_connect_string = configuration
	// .getProperty("db.connection");

	// private String db_connect_string = null;

	@Autowired
	InstitutionService institutionService;

	@Autowired
	protected services.Reporter report;

	Connection conn;

	// private static final Logger logger = Logger.getLogger(DbService.class);

	public DbService() throws Exception {
		jdbcTemplate = new JdbcTemplate();

	}

	/**
	 * @param size the length of the string
	 * @return string - return a number from the current time of the system in mili
	 * seconds
	 * @throws Exception
	 */
	public String sig(int size) throws Exception {
		String str = sig();
		return str.substring(str.length() - size, str.length());
	}

	/**
	 * @return string - return a number from the current time of the system in mili
	 * seconds
	 * @throws Exception
	 */
	public String sig() throws Exception {
		return String.valueOf(System.currentTimeMillis());
	}

	/**
	 * @param sql query
	 * @return single row, simge value result
	 * @throws Exception
	 */
	public String getStringFromQuery(String sql) throws Exception {
		return getStringFromQuery(sql, 1, false, MAX_DB_TIMEOUT);
	}

	/**
	 * @param sql - use only sql queries that start with delete/update
	 * @throws Exception
	 */
	public int runDeleteUpdateSql(String sql) throws Exception {
		report.report("Query is: " + sql);
		report.report(sql);
		int res = -1;
		Statement statement = null;
		try {
			Class.forName(SQL_SERVER_DRIVER_CLASS);
			conn = getConnectionNew();
			report.report("connected");

			if (conn.isClosed() == true) {
				report.report("connection is closed");
			}

			statement = conn.createStatement();
			res = statement.executeUpdate(sql);
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (statement != null) {
				statement.close();
			}
			conn.close();
		}
		return res;
	}

	/*
	  @param store precudure text, for example: "{call GetAssignedCourses2(?) }"
	  @param param
	  @return List of Strings (single column table)
	  @throws SQLException
	 */

	public List<String> getDataFromSP(String sp, BigDecimal param) throws SQLException {

		List<String> data = new ArrayList<String>();
		try {
			conn = getConnectionNew();
			CallableStatement proc = conn.prepareCall(sp);
			proc.setBigDecimal("UserId", param);
			ResultSet rs = proc.executeQuery();

			while (rs.next()) {
				String course = String.valueOf(rs.getInt(1));
				String name = rs.getString(2);
				data.add(course);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}
		return data;

	}

	/**
	 * @param sql       query
	 * @param intervals not used, can be 0
	 * @param columns   number of columns in the returned table
	 * @return list of 2nd array of strings (multi columns table)
	 * @throws Exception
	 */
	public List<String[]> getStringListFromQuery(String sql, int intervals, int columns) throws Exception {
		List<String[]> list = new ArrayList<String[]>();

		StringBuilder sb = new StringBuilder();
		sb.append("Query is: ");
		sb.append(sql);
		sb.append(". Max db time out is: ");
		sb.append(MAX_DB_TIMEOUT);
		report.report(sb.toString());

		// report.report("Query is: " + sql + ". Max db time out is: " +
		// MAX_DB_TIMEOUT);
		ResultSet rs = null;
		Statement statement = null;
		String str = null;
		int elapsedTimeInSec = 0;

		try {
			Class.forName(SQL_SERVER_DRIVER_CLASS);
			conn = getConnectionNew(); // getConnection();
			// report.report("connected");
			if (conn.isClosed() == true) {
				report.report("connection is closed");
			}

			outerloop:
			while (elapsedTimeInSec < MAX_DB_TIMEOUT) {
				statement = conn.createStatement();
				rs = statement.executeQuery(sql);

				// ResultSetMetaData rsmd = rs.getMetaData();
				// columns=rsmd.getColumnCount();

				while (rs.next()) {

					String[] strArr = new String[columns];
					for (int i = 1; i <= columns; i++) {
						strArr[i - 1] = rs.getString(i);
					}
					list.add(strArr);
				}
				if (list.size() == 0) {
					elapsedTimeInSec++;
					Thread.sleep(1000);

				} else {
					break outerloop;
				}
			}

			conn.close();
		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			try {

			} catch (Exception e) {
			}
			if (statement != null) {
				statement.close();
			}
			conn.close();
		}
		return list;
	}

	public List<String[]> getStringListFromQuery(String sql, int intervals, boolean allowNull) throws Exception {
		List<String[]> list = new ArrayList<String[]>();

		StringBuilder sb = new StringBuilder();
		sb.append("Query is: ");
		sb.append(sql);
		sb.append(". Max db time out is: ");
		sb.append(MAX_DB_TIMEOUT);
		report.report(sb.toString());

		// report.report("Query is: " + sql + ". Max db time out is: " +
		// MAX_DB_TIMEOUT);
		ResultSet rs = null;
		Statement statement = null;
		String str = "";
		int elapsedTimeInSec = 0;

		try {
			Class.forName(SQL_SERVER_DRIVER_CLASS);
			conn = getConnectionNew(); // getConnection();
			// report.report("connected");
			if (conn.isClosed() == true) {
				report.report("connection is closed");
			}

			outerloop:
			while (elapsedTimeInSec < MAX_DB_TIMEOUT) {
				statement = conn.createStatement();
				rs = statement.executeQuery(sql);

				ResultSetMetaData rsmd = rs.getMetaData();
				int columns = rsmd.getColumnCount();

				while (rs.next()) {

					String[] strArr = new String[columns];
					for (int i = 1; i <= columns; i++) {
						strArr[i - 1] = rs.getString(i);
						str  = str + rs.getString(i) +",";
					}
					list.add(strArr);

				}
				if (list.size() == 0) {
					elapsedTimeInSec++;
					Thread.sleep(1000);
					list = null;
					break outerloop;

				} else {
					break outerloop;
				}
			}

			conn.close();
			report.report(str);
		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			try {
				if (rs == null && allowNull == false) {
					Assert.fail("Query result is null. Query was: " + sql);
				} else if (rs == null && allowNull == true) {
					conn.close();
					statement.close();
					return null;
				}

			} catch (Exception e) {
			}
			if (statement != null) {
				statement.close();
			}
			conn.close();
		}
		return list;
	}

	/**
	 * @param sql
	 * @param columns mumber of columns
	 * @return list of 2nd array of strings (multi columns table)
	 * @throws IOException
	 * @throws Exception
	 */
	public List<String[]> getListFromPrepairedStmt(String sql, int columns) throws IOException, Exception {
		// report.report(sql);
		conn = getConnectionNew();
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.executeUpdate(); // do not use execute() here otherwise you may get
		// the error
		// The statement must be executed before
		// any results can be obtained on the next
		// getGeneratedKeys statement.
		List<String[]> list = new ArrayList<String[]>();
		ResultSet rs = ps.getGeneratedKeys();
		while (rs.next()) {
			String[] str = new String[columns];
			for (int i = 0; i < columns; i++) {
				str[i] = rs.getString(i + 1);
			}
			list.add(str);
		}
		conn.close();
		return list;

	}

	/**
	 * @param sql
	 * @param intervals not used
	 * @return list of strings (single column table)
	 * @throws Exception
	 */
	public List<String> getArrayListFromQuery(String sql, int intervals) throws Exception {
		List<String> list = new ArrayList<String>();

		StringBuilder sb = new StringBuilder();
		sb.append("Query is: ");
		sb.append(sql);
		sb.append(". Max db time out is: ");
		sb.append(MAX_DB_TIMEOUT);
		report.report(sb.toString());

		// report.report("Query is: " + sql + ". Max db time out is: " +
		// MAX_DB_TIMEOUT);
		report.report(sql);
		ResultSet rs = null;
		Statement statement = null;
		String str = null;

		try {
			Class.forName(SQL_SERVER_DRIVER_CLASS);
			conn = getConnectionNew();
			report.report("connected");
			if (conn.isClosed() == true) {
				report.report("connection is closed");
			}
			statement = conn.createStatement();
			rs = statement.executeQuery(sql);
			while (rs.next()) {
				list.add(rs.getString(1));
				report.report(rs.getString(1));
			}

			conn.close();
		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			try {

			} catch (Exception e) {
			} finally {
				conn.close();
			}
			if (statement != null) {
				statement.close();
			}
		}
		return list;
	}

	public String getStringFromQuery(String sql, int intervals, boolean allowNull) {
		String result = null;
		try {
			result = getStringFromQuery(sql, intervals, allowNull, MAX_DB_TIMEOUT);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public String getStringFromQuery(String sql, int intervals, boolean allowNull, int maxTimeout) throws Exception {
		// report.report(configuration.getProperty("db.connection"));

		StringBuilder sb = new StringBuilder();
		sb.append("Query is: ");
		sb.append(sql);
		sb.append(". Max db time out is: ");
		sb.append(maxTimeout);
		report.report(sb.toString());

		// report.report("Query is: " + sql + ". Max db time out is: " +
		// MAX_DB_TIMEOUT);
		// db_userid = configuration.getProperty("db.connection.username");
		// db_password = configuration.getProperty("db.connection.password");
		// report.report(db_userid + " " + db_password);
		// report.report(sql);
		ResultSet rs = null;
		Statement statement = null;
		String str = null;
		int elapsedTimeInSec = 0;

		try {
			Class.forName(SQL_SERVER_DRIVER_CLASS);
			// report.report("DB user id is: " + db_userid);
			/*
			 * if (useToeicDB) { conn = getToeicOlpcConnString(); }
			 */
			// conn = getConnection();
			conn = getConnectionNew();
			// report.report("connected");

			if (conn.isClosed() == true) {
				report.report("connection is closed");
			}

			statement = conn.createStatement();
			outerloop:
			while (elapsedTimeInSec < maxTimeout) {
				rs = statement.executeQuery(sql);
				while (rs.next()) {
					// report.report(rs.getString(1));
					str = rs.getString(1);

				}
				if (str != null) {
					report.report("DB result found: '" + str + "'");

					break outerloop;

				} else {
					Thread.sleep(intervals * 1000);
					report.report("Waiting for DB. sleeping for " + intervals + " seconds and the response is: " + str);
					elapsedTimeInSec = elapsedTimeInSec + intervals;
				}
			}

		} catch (Exception e) {

			e.printStackTrace();
			statement.close();
			conn.close();

		} finally {

			try {

				if (str == null && allowNull == false) {
					Assert.fail("Query result is null. Query was: " + sql);
				} else if (str == null && allowNull == true) {
					return null;
				}

			} catch (Exception e) {

			}

			if (statement != null) {
				statement.close();
			}
			conn.close();
		}
		return str;
	}

	public void closeConnection() throws SQLException {
		conn.close();
	}

	/*public String getUserIdByUserName(String userName, String institutionId, boolean useSecondEDMerge) throws Exception {
		String userId = "";
		if (currentGeneralDB.equalsIgnoreCase("edProduction") && useSecondEDMerge == true) {
			useEDMerge2 = useSecondEDMerge;
		}
		try {
			userId = getUserIdByUserName(userName, institutionId);
			useEDMerge2 = false;
		} catch (Exception e) {
			e.printStackTrace();
			testResultService.addFailTest(e.getMessage(), true, true);
			useEDMerge2 = false;
		} finally {
			useEDMerge2 = false;
		}
		return userId;
	}*/

	public String getUserIdByUserName(String userName, String institutionId, boolean useSecondEDMerge) throws Exception {
		String userId = "";
		if (useSecondEDMerge) {
			useEDMerge2 = useSecondEDMerge;
		}
		try {
			userId = getUserIdByUserName(userName, institutionId);
			useEDMerge2 = false;
		} catch (Exception e) {
			e.printStackTrace();
			testResultService.addFailTest(e.getMessage(), true, true);
			useEDMerge2 = false;
		} finally {
			useEDMerge2 = false;
		}
		return userId;
	}

	public String getUserIdByUserName(String userName, String institutionId) throws Exception {

		String filter = "UserName";

		if (useOfflineDB)
			filter = "FirstName";

		StringBuilder sb = new StringBuilder();
		sb.append("select UserId from users where ");
		sb.append(filter);
		sb.append("='");
		sb.append(userName);
		sb.append("' and institutionid=");
		sb.append(institutionId);

		// String sql = "select UserId from users where "+ filter +" ='" + userName + "'
		// and institutionid=" + institutionId;
		String sql = sb.toString();
		String result = getStringFromQuery(sql, 1, true, 5);

		return result;
	}

	public String getUserNameById(String id, String instId) throws Exception {

		String filter = "UserName";

		if (useOfflineDB)
			filter = "FirstName";

		String sql = "select " + filter + " from users where userId=" + id + " and institutionid=" + instId;

		String result = getStringFromQuery(sql);

		return result;
	}

	public void removeMatrixManualGradesForStudent(String userId) throws Exception {
		runDeleteUpdateSql("delete from MatrixManualGrades where UserId = '" + userId + "'");
	}

	public void setMatrixUserLockState(String userId, String teacherAdminId, String state, String CourseId)
			throws Exception {
		String SP = "exec SetUserLock " + userId + ",'" + CourseId + "'," + state + ",'" + teacherAdminId + "'";
		runStorePrecedure(SP);
	}

	public String getValueFromOutBoxBySubject(String value, String subject) throws Exception {
		String sql = "select top 1 " + value + " from outbox where subject = '" + subject + "' order by MessageId desc";
		return getStringFromQuery(sql);
	}

	public void deleteOutboxByMessageId(String Id) throws Exception {
		runDeleteUpdateSql("delete from outbox where MessageID = " + Id);
	}

	// for resume test 22.08.18

	public String getCTCompleted(String testId, String userId) throws Exception {
		String sql = "select  CTCompleted from UserExitTestSettingsLog  where testId=" + testId + " AND userId='"
				+ userId + "'";
		String result = getStringFromQuery(sql);
		return result;
	}

	public String getStudentCourseTestScore(String userId, String courseId, String testId, int testType)
			throws Exception {
		String sql = "select  Grade from UserExitTestSettingsLog  where courseid ='" + courseId + "' and testId ='"
				+ testId + "' and ImpTypeFeatureId=" + testType + " and userId='" + userId + "'";
		String result = getStringFromQuery(sql);
		return result;
	}

	public String[] getStudentFinalTestData(String userId, String courseId, String testId, int testType)
			throws Exception {
		String sql = "select  TestDate,Grade,CTCompleted,LastVisitedItemId from UserExitTestSettingsLog  where courseid ='"
				+ courseId + "' and testId ='" + testId + "' and ImpTypeFeatureId=" + testType + " and userId='"
				+ userId + "'";
		List<String[]> result = getStringListFromQuery(sql, 0, 4);

		String[] row = result.get(0);

		return row;
	}

	public void deleteExitTestForUserId(String userId) throws Exception {
		String sql = "delete from UserExitTestSettings where UserId = " + userId + ";";
		runDeleteUpdateSql(sql);
	}

	public void deleteExitTestForEntireClass(String classId, int testType) throws Exception {
		String sql = "delete from UserExitTestSettings where UserExitTestSettingsId in "
				+ "(SELECT UserExitTestSettings.UserExitTestSettingsId FROM ClassUsers Inner JOIN "
				+ "UserExitTestSettings On ClassUsers.UserId = UserExitTestSettings.userId where "
				+ "ClassUsers.ClassId = " + classId + " and UserExitTestSettings.ImpTypeFeatureId = " + testType + ")";
		runDeleteUpdateSql(sql);
	}

	public List<String> getAssignedExitTestOfEntireClass(String classId, int testType) throws Exception {
		String sql = "SELECT UserExitTestSettings.TestId FROM ClassUsers "
				+ "INNER JOIN UserExitTestSettings ON UserExitTestSettings.UserId = ClassUsers.UserId "
				+ "WHERE (ClassUsers.ClassId = " + classId + ") and ImpTypeFeatureId = " + testType;
		return getArrayListFromQuery(sql, 1);
	}

	public String getLastestEraterProcessedStatus(String studentId) throws Exception {
		String sql = "select top 1 Processed from Writing where userid =" + studentId + " order by WritingId desc";
		String Processed = getStringFromQuery(sql);
		return Processed;
	}

	public String getUserModifiedValue(String userID) throws Exception {
		String sql = "select IsNull([Modified], \'00:00\') as [Modified] from [Users] where [UserId]=" + userID;
		return getStringFromQuery(sql);
	}

	public void resetUserModifiedValue(String userID) throws Exception {
		String sql = "update [Users] set [Modified]=null where [UserId]=" + userID;
		runDeleteUpdateSql(sql);
	}

	public String getLatestUserIdByInstandClass(String InstId, String ClassId) throws Exception {
		String sql = "select top 1 [UserId] from [Users] join [Class] on [Users].[InstitutionId]=[Class].[InstitutionId] "
				+ "where [Users].[InstitutionId]=" + InstId + " and [Visible]=1 and [UserTypeId] = 1 and [ClassId]="
				+ ClassId + " order by [UserId]";
		return getStringFromQuery(sql);
	}

	public String getStudentAssignedTestId(String studentId, int impTypeFeatureId) throws Exception {
		String sql = "select TestId from UserExitTestSettings where userId = " + studentId + " and ImpTypeFeatureId = "
				+ impTypeFeatureId;

		return getStringFromQuery(sql);
	}

	public String getStudentAssignedTestEndDate(String studentId, int impTypeFeatureId) throws Exception {
		String sql = "select EndDate from UserExitTestSettings where UserId = " + studentId + " and ImpTypeFeatureId = "
				+ impTypeFeatureId;
		return getStringFromQuery(sql);
	}

	public String getApiToken(String instid) {
		String sql = "select ApiToken from institutions where institutionid='" + instid + "'";
		String result = "";
		try {
			result = getStringFromQuery(sql);
		} catch (Exception e) {
			System.out.println(e);
		}

		return result;
	}

	// for timecheking resume test

	public void SetTestTime(String userId, int minutes) throws Exception {
		String sp = null;
		sp = "Update UserExitTestSettings Set Minutes =" + minutes + " where userId= " + userId;
		runStorePrecedure(sp);
	}

	/* change the query */
	public void FullPathProperties(String institutionID) throws Exception {
		String sp = null;
		sp = "insert institutionproperties Set value = 3, propertyID = 8 where institution = " + institutionID;
		runStorePrecedure(sp);

	}

	/* change the query */
	public void RestoreFullPathProperties(String institutionID) throws Exception {
		String sp = null;
		sp = "Delete institutionproperties Set value = 3, propertyID = 8 where institution = " + institutionID;
		runStorePrecedure(sp);

	}

	public String CTLastVisitedItemID(String testId, String userId, int impType) throws Exception {
		String sql = "select  LastVisitedItemId from UserExitTestSettingsLog  where testId =" + testId
				+ " AND ImpTypeFeatureId=" + impType + " AND userId=" + userId;
		String result = getStringFromQuery(sql);
		return result;
	}

	public String getAdminIdByInstId(String institutionId) throws Exception {

		String sql = "select AdministratorId from Institutions where institutionid=" + institutionId;

		String result = getStringFromQuery(sql);

		return result;
	}

	public String getInstPropertyValueByProperyId(String instID, int propertyId) throws Exception {

		String sql = "select [value] from InstitutionProperties where institutionID = " + instID + " and propertyId = "
				+ propertyId;
		String result = getStringFromQuery(sql, 1, true, 5);
		return result;
	}

	public void deleteInstitutionForumByTitle(String title) throws Exception {

		String forumId = getStringFromQuery("select InstitutionForumId from InstitutionForums where title = '" + title
				+ "' and institutionId = " + configuration.getProperty("institution.id"));
		runStorePrecedure("exec DeleteInstitutionForum '" + forumId + "'");
	}

	public void unassignForumForClass(String classId, String userId) throws Exception {
		// UserId = Institution administrator
		String SP = "exec SetClassForums " + classId + ",''," + userId;
		runStorePrecedure(SP);
	}

	public void setInstitutionPLTSettings(String instId, String PTResult, String PTOnlyOnce, String PTShow)
			throws Exception {

		String SP = "exec SetInstitutionPTVisibility " + instId + "," + PTResult + "," + PTOnlyOnce + "," + PTShow;
		runStorePrecedure(SP);
	}

	public List<String[]> getUsersDidPLT(String instId) throws Exception {

		String sql = "select top 10 u.username,u.Password,u.UserId " + "from PlacementTestGrades as ptg "
				+ "inner join users as u on ptg.UserId = u.UserId " + "where u.InstitutionId =" + instId
				+ " and u.Visible = 1 " + "and (u.LogedIn is null or u.LogedIn = '2000-01-01 00:00:00.000')";

		List<String[]> result = getStringListFromQuery(sql, 0, 3);

		return result;
	}

	public String getFirstInstitutionForumTitle(String instId) throws Exception {

		// String forumId = getStringFromQuery("select InstitutionForumId from
		// InstitutionForums where title = '" + title + "' and institutionId = " +
		// configuration.getProperty("institution.id"));
		return getStringFromQuery("select top 1 title from InstitutionForums where InstitutionId = " + instId);
	}

	public void deleteForumMessgeByTitleAndContent(String title, String messageContent) throws Exception {

		String messageId = getStringFromQuery("select ClassForumDetailId from ClassForumDetails where title = '" + title
				+ "' and PostText = '<p>" + messageContent + "</p>'");
		runStorePrecedure("exec DeleteClassForumMessages '" + messageId + "'");
	}

	public String getInstCustomComponentByOwner(String instId, String OwnerId, String compTitle) throws Exception {

		String sql = "select top 1 InstitutionComponentId from InstitutionComponents where institutionId = " + instId
				+ " and Owner = " + OwnerId + " and ComponentName = '" + compTitle + "'";
		return getStringFromQuery(sql);
	}

	public void deleteCustomComponentById(String compId) throws Exception {

		runStorePrecedure("DeleteInstitutionComponents '" + compId + "'");
	}

	public void updateOnboardingStateForUser(String userId, int areaId, boolean visited) throws Exception {

		String sql;
		if (visited)
			sql = "insert into OnBoardingStates values (" + userId + "," + areaId + ",1)";
		else
			sql = "delete from OnBoardingStates where userID = " + userId + " and areaID = " + areaId;
		runDeleteUpdateSql(sql);
	}

	public List<String[]> getUserByInstitutionIdAndClassName(String institutionId, String className) throws Exception {

		String sql = "undefined";

		if (!useOfflineDB) {
			sql = "select top 10 u.username,u.Password,u.UserId"
					+ " from users as u inner join ClassUsers as CU ON u.UserId = CU.UserId"
					+ " inner join Class as C ON C.ClassId = CU.ClassId" + " where u.InstitutionId = '" + institutionId
					+ "'" + " and u.Visible = 1 "
					+ " and u.LevelId = 1 and (u.LogedIn = '2000-01-01 00:00:00.000' or u.LogedIn is null)"
					+ " and u.Username like 'stud%' and u.Password = '12345'"
					//	+ " and u.RegistrationDate >= DATEADD(MONTH,-8,GETDATE())" + 
					+ " and C.Name ='" + className + "'"
					+ " Order by UserId ASC";
		} else {
			sql = "select top 10 u.FirstName,12345,u.UserId from users as u inner join ClassUsers as CU ON u.UserId = CU.UserId inner join Class as C ON C.ClassId = CU.ClassId where u.InstitutionId = '"
					+ institutionId
					+ "' and u.Visible = 1 and u.LanguageSupportLevelId = 1 and u.LevelId = 1 and u.LogedIn is NULL and u.FirstName like 'stud%' and u.Password is NULL and C.Name ='"
					+ className + "' Order by UserId ASC";
		}

		List<String[]> result = getStringListFromQuery(sql, 1, true);

		return result;
	}

	public List<String[]> getUserNamePassworIddByInstitutionIdAndClassName(String institutionId, String className) {

		String sql = "undefined";

		// String dbUsedForLogin = getDbConnString();
		// report.report("The DB used for login is: " + dbUsedForLogin.toString());

		sql = "select top 10 u.username,u.Password,u.UserId from users as u "
				+ "inner join ClassUsers as CU ON u.UserId = CU.UserId "
				+ "inner join Class as C ON C.ClassId = CU.ClassId " + "where u.InstitutionId = '" + institutionId + "'"
				+ " and u.Visible = 1 and (u.LogedIn = '2000-01-01 00:00:00.000' or u.LogedIn is null)"
				+ " and u.RegistrationDate >= DATEADD(MONTH,-8,GETDATE())" + " and C.Name ='" + className
				+ "' Order by UserId ASC";

		List<String[]> result = null;
		try {
			result = getStringListFromQuery(sql, 1, true);

			if (result == null) {

				sql = "select top 10 u.username,u.Password,u.UserId from users as u "
						+ "inner join ClassUsers as CU ON u.UserId = CU.UserId "
						+ "inner join Class as C ON C.ClassId = CU.ClassId " + "where u.InstitutionId = '"
						+ institutionId + "'" + ""
						+ "-- and u.RegistrationDate >= DATEADD(MONTH,-8,GETDATE())"
						+ " and u.Visible = 1 and C.Name ='" + className + "'" + " Order by UserId ASC";

				try {
					result = getStringListFromQuery(sql, 1, true);

				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
		}

		return result;
	}

	public String getInstituteNameById(String id) {
		String sql = "select name from institutions where visible = 1 and institutionId=" + id;
		String result = "";
		try {
			result = getStringFromQuery(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public String getInstituteIdByName(String name) throws Exception {
		String sql = "select institutionId from institutions where name='" + name + "' and visible=1";
		String result = getStringFromQuery(sql);
		return result;
	}

	public String[] getInstituteIdAndCannonicalDomainByName(String name) throws Exception {
		String sql = "select institutionId,CannonicalDomain from institutions where name='" + name + "' and visible=1";
		List<String[]> resultFromDB = getStringListFromQuery(sql,1,true);
		return resultFromDB.get(0);
	}

	public String getInstituteMyProfileGroupIdByName(String id) throws Exception {
		String sql = "select MyProfileGroupId from [EDMerge].[dbo].[Institutions] where institutionId='" + id
				+ "' and visible=1";
		String result = getStringFromQuery(sql);
		return result;
	}

	public void setInstitutionMyProfileGroupId(String instId, String MyProfileGroupId) throws Exception {
		runDeleteUpdateSql(
				"update Institutions set MyProfileGroupId = " + MyProfileGroupId + " where institutionId = " + instId);
	}

	public String getClassLanguageSupportLevel(String classId) throws Exception {
		return getStringFromQuery("select LanguageSupportLevelId from class where classID = " + classId);
	}

	public void updateClassLanguageSupportLevel(String classId, String supportLeveLId) throws Exception {
		runDeleteUpdateSql(
				"update class set LanguageSupportLevelId = " + supportLeveLId + " where classId = " + classId);
	}

	public String getUserStudyPlanSettingsByUserId(String id) throws Exception {
		String sql = "select StudyPlanSettingsId from [EDMerge].[dbo].[users] where Userid ='" + id + "'";
		String result = getStringFromQuery(sql);
		return result;
	}

	public void setUserStudyPlanSettingsForUser(String userId, String StudyPlanSettingsId) throws Exception {
		String sql = "update users set StudyPlanSettingsId = " + StudyPlanSettingsId + " where Userid = " + userId;
		runDeleteUpdateSql(sql);
	}

	public void updateUserFirstNamebyUserId(String userId, String firstName) {
		String sql = "update users set FirstName = '" + firstName + "' where Userid = " + userId;
		try {
			runDeleteUpdateSql(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void updateUserNameByUserId(String userId, String newUserName) {
		String sql = "update users set UserName = '" + newUserName + "' where Userid = " + userId;
		try {
			runDeleteUpdateSql(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getSantilianaUrlForTeacherOrAdminLogin(String institutionName) throws Exception {
		String sql = "select CannonicalDomain from Institutions where Name= '"+institutionName+"'";
		String result = getStringFromQuery(sql);

		return result;
	}

	public String getTeacherSantilianaUserName(String institutionName) throws Exception {
		String sql = "SELECT TOP 1 U.FirstName\n" +
				"FROM Users U\n" +
				"JOIN Institutions I ON U.InstitutionId = I.InstitutionID\n" +
				"WHERE I.Name = '" + institutionName + "' AND U.UserTypeId = 3;";
		String result = getStringFromQuery(sql);
		return result;
	}

	public String getTeacherSantilianaPassword(String institutionName) throws Exception {
		String sql = "SELECT TOP 1 U.Password\n" +
				"FROM Users U\n" +
				"JOIN Institutions I ON U.InstitutionId = I.InstitutionID\n" +
				"WHERE I.Name = '" + institutionName + "' AND U.UserTypeId = 3;";
		String result = getStringFromQuery(sql);
		return result;

	}

	public String getStudentSantilianaUserName() throws Exception {
		String sql = "SELECT TOP 1 [UserName]\n" +
				"FROM [EDMerge].[dbo].[SantillanaSsoUsers]\n" +
				"ORDER BY [UpdatedOn] DESC;";
		return getStringFromQuery(sql);
	}

	public void setCommunicationModeForInstitution(String instId, String CommunicationModeId) throws Exception {
		String SP = "exec SetInstitutionCommunicationMode " + instId + "," + CommunicationModeId;
		runStorePrecedure(SP);

	}
	public void runDWJob(String userId, String classId) throws Exception {

		String SP = "EXEC [dbo].[Automation_DWUpdate] @UserId = '" + userId + "', @ClassId = '" + classId + "'";;
		runStorePrecedure(SP);

	}

	public String getPltTestStatus(String userId) throws Exception {
		String sql = "SELECT TestStatuses.TestStatusDescription\n" +
				"FROM UserTestAdministrations\n" +
				"JOIN TestStatuses ON UserTestAdministrations.TestStatusId = TestStatuses.TestStatusId\n" +
				"WHERE UserTestAdministrations.UserId = '" + userId + "'";
		String result = getStringFromQuery(sql);

		return result;

	}

	public void verifyInstitutionCreated(Institution institution) throws Exception {
		String sql = "select institutionId from institutions where Name='" + institution.getName()
				+ "' and cannonicalDomain='" + institution.getHost() + "'";
		String result = getStringFromQuery(sql);
		report.report("Institution id is: " + result);
	}

	public String getCannonicalDomainByInstId(String institutionId) {

		String sql = "select CannonicalDomain from institutions where InstitutionId=" + institutionId
				+ " and Visible=1";
		String result = null;
		try {
			result = getStringFromQuery(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public String getClassIdByName(String className, String institutionId) {

		// String institutionId =
		// institutionService.getInstitution().getInstitutionId();

		String sql = "select classId from class where Name='" + className + "' and institutionId=" + institutionId + "";
		String result = null;
		try {
			result = getStringFromQuery(sql, 1, true, 1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public void deleteUserIdByClassId(String ClassId, String userId) throws Exception {
		String sp = "exec DeleteFilteredUsers " + ClassId + ",'C',0,'" + userId + "',null,null,1";
		runStorePrecedure(sp);
	}

	public int getCurrentDay() throws Exception {
		Calendar cal = Calendar.getInstance();
		int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);

		String dayOfMonthStr = String.valueOf(dayOfMonth);

		return dayOfMonth;
	}

	public String getDbConnString() {
		String dbConn = configuration.getAutomationParam("db", "db");

		// if dbConn = null, it means the test runs manually and not from Jenkins

		if (dbConn.isEmpty() || dbConn.equalsIgnoreCase(null)) {
			if (isUseEdProductionDB()) {
				return configuration.getProperty("db.connection.edproduction");

			} else if (isUseEdBetaProductionDB()) {
				return configuration.getProperty("db.connection.edbeta");

			} else if (isUsingCIDB()) {
				return configuration.getProperty("db.connection.ci");

			} else if (isUseProd2DB()) {
				return configuration.getProperty("db.connection.prod2");
			}
			return configuration.getAutomationParam("db.connection.ci", "db");
		} else {

			if (dbConn.equalsIgnoreCase("EdProduction")) {
				setUseEdproductionDb(true);
				return configuration.getProperty("db.connection.edproduction");

			} else if (dbConn.equalsIgnoreCase("EdBeta")) {
				setUseEdBetaproductionDb(true);
				return configuration.getProperty("db.connection.edbeta");

			} else if (dbConn.equalsIgnoreCase("cloud")) {
				setUsingCIDB(true);
				return configuration.getProperty("db.connection.ci");

			} else if (dbConn.equalsIgnoreCase("offline")) {
				setUseOfflineDB(true);
				return configuration.getProperty("db.connection.offline");

			} else if (dbConn.contains("CI-DB")) {
				setUsingCIDB(true);
				return dbConn;

			} else if (dbConn.equalsIgnoreCase("prod2")) {
				setUseProd2DB(true);
				return configuration.getProperty("db.connection.prod2");
			}

		}
		return dbConn;
	}

	public String getOfflineConnString() {
		String conString = configuration.getGlobalProperties("offlinedb");
		// conString = conString.replace("%machine%",
		// configuration.getGlobalProperties("offline.ip"));
		return conString;
	}

	public String getToeicOlpcConnString() {
		String conString = configuration.getProperty("db.connectionToeicOlpc");
		// conString = conString.replace("%machine%",
		// configuration.getGlobalProperties("offline.ip"));
		return conString;
	}

	public int getUsedLicensesPerClass(String className) {
		// TODO Auto-generated method stub
		return 0;
	}

	public String[] getClassAndCourseWithLastProgress(String teacherName, String instId, UserType userType)
			throws Exception {

		String[] output = new String[2];

		String sql = "select Top(1) userId,CourseId from Progress where UserId in( select distinct  UserId from ClassUsers ";

		if (userType.equals(UserType.Teacher)) {
			String sqlAsTeacher = " where ClassId in(select ClassId from dbo.TeacherClasses where UserPermissionsId=(select up.UserPermissionsId from dbo.UserPermissions as up, users as u where  up.userId=u.userId and u.userName='"
					+ teacherName + "' and u.institutionId=" + instId + ") ))";
			sql = sql + sqlAsTeacher;
		} else if (userType.equals(UserType.SchoolAdmin)) {
			String asSchoolAdmin = "where ClassId in( select ClassId  from class where institutionId=" + instId + "))";
			sql = sql + asSchoolAdmin;
		}

		sql = sql + "			order by LastUpdate desc";

		List<String[]> results = getStringListFromQuery(sql, 5, 2);

		String StudentId = results.get(0)[0];
		String sqlForClassName = "select c.name from classUsers as cu , class as c where cu.ClassId=c.ClassId  and cu.userId="
				+ StudentId;
		output[0] = getStringFromQuery(sqlForClassName);

		String sqlForCourseName = "select Name from course where CourseId=" + results.get(0)[1];

		output[1] = getStringFromQuery(sqlForCourseName);

		return output;

	}

	public String getNumberOfStudentsInClass(String className, String institutionId) throws Exception {
		String sql = "  select count(*) from classUsers as cu,class as c where cu.classId=c.classId and c.name='"
				+ className + "' and c.InstitutionId=" + institutionId;
		String result = getStringFromQuery(sql);
		return result;
	}

	public List<String[]> getListFromStoreRrecedure(String sql) throws IOException, Exception {

		List<String[]> rsList = new ArrayList<String[]>();
		List<String[]> strList = new ArrayList<String[]>();

		report.report("SQL query was: " + sql);
		try {
			conn = getConnectionNew();

			report.report("connected");
			if (conn.isClosed() == true) {
				report.report("connection is closed");
			}
			// statement = conn.createStatement();

			CallableStatement statement = conn.prepareCall(sql);
			// for (int i = 0; i < params.length; i++) {
			// statement.setString(i, params[i]);
			// }

			boolean results = statement.execute();

			int rsCount = 0;
			while (results) {

				ResultSet rs = statement.getResultSet();
				ResultSetMetaData rsmd = rs.getMetaData();

				int columnsNumber = rsmd.getColumnCount();

				while (rs.next()) {
					String[] strArr = new String[columnsNumber];
					for (int i = 0; i < columnsNumber; i++) {
						strArr[i] = rs.getString(i + 1);
					}
					strList.add(strArr);
				}

				rs.close();
				//rsList.add(strList);
				results = statement.getMoreResults();
			}
			statement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			conn.close();
		}
		return strList;
	}

	// s}

	public String getCourseIdByName(String courseName) throws Exception {
		String sql = "select CourseId from course where name='" + courseName + "'";
		return getStringFromQuery(sql);

	}

	public List<String[]> getClassScores(String classId, String courseId) throws Exception {
		String sql = "select  avg( tr.Grade) from testResults as tr  where tr.userId in( select distinct  UserId from ClassUsers where ClassId="
				+ classId + " and courseId=" + courseId + " )group by ComponentSubComponentId";

		List<String[]> results = getStringListFromQuery(sql, 1, 1);
		return results;
	}

	public int getRandonNumber(int min, int max) {
		Random r = new Random();
		int i1 = r.nextInt(max - min + 1) + min;
		return i1;
	}

	private Connection getConnection() throws SQLException, Exception, IOException {

		db_userid = configuration.getProperty("db.connection.username");
		db_password = configuration.getProperty("db.connection.password");

		if (isUseEdProductionDB() || isUseEdBetaProductionDB()) {
			db_userid = configuration.getProperty("db.connection.username.production");
			db_password = configuration.getProperty("db.connection.password.production");
			conn = DriverManager.getConnection(getDbConnString(), db_userid, db_password);
			// setUseStaticContentProduction(true);

		} else if (logProfiler != false) {
			initDbLogger();
		} else if (isUseOfflineDB()) {

			conn = DriverManager.getConnection(getDbConnString(), db_userid, db_password);

		} else if (isUseusingTfsDB()) {

			conn = DriverManager.getConnection(configuration.getProperty("db.connection.tfs"), db_userid, db_password);

		} else if (isUseStaticContent()) {

			conn = DriverManager.getConnection(configuration.getProperty("db.connection.static"), db_userid,
					db_password);

		} else if (isUseStaticContentProduction()) {

			conn = DriverManager.getConnection(configuration.getProperty("db.connection.static.production"), db_userid,
					db_password);

		} else if (isUseToeicOlpc()) {

			conn = DriverManager.getConnection(configuration.getProperty("db.connection.static"), db_userid,
					db_password);
		} else {

			conn = DriverManager.getConnection(getDbConnString(), db_userid, db_password);
		}

		// report.addTitle("Method: getConnection(), The connection is: " + conn + ", DB
		// UserName: " + db_userid + ", DB Password: " + db_password);
		return conn;
	}

	private Connection getConnectionNew() throws SQLException, Exception, IOException {

		String DBconnectionString = "";

		db_userid = configuration.getProperty("db.connection.username");
		db_password = configuration.getProperty("db.connection.password");
		DBconnectionString = configuration.getProperty("db.connection");

		if (DBconnectionString != null) // it's not progress tool but ed automation
			conn = DriverManager.getConnection(DBconnectionString, db_userid, db_password);

		if (isUseusingTfsDB()) {
			DBconnectionString = configuration.getProperty("db.connection.tfs");
			conn = DriverManager.getConnection(DBconnectionString, db_userid, db_password);
			return conn;
		}

		if (
				(currentGeneralDB.equalsIgnoreCase("edProduction")
						|| currentGeneralDB.equalsIgnoreCase("edBetaProduction")
						|| currentGeneralDB.equalsIgnoreCase("edProduction2")
						|| currentGeneralDB.equalsIgnoreCase("edProductionArab")
				) && isUseEdDB()
		) {
			db_userid = configuration.getProperty("db.connection.username.production");
			db_password = configuration.getProperty("db.connection.password.production");

			if (currentStaticDB.equalsIgnoreCase("production")) { // static content of production
				DBconnectionString = configuration.getProperty("db.connection.static.production");
				conn = DriverManager.getConnection(DBconnectionString, db_userid, db_password);
			} else { //

				if (currentGeneralDB.equalsIgnoreCase("edProduction")) { // Ed Production only

					if (usingDWDB) {
						DBconnectionString = configuration.getProperty("db.connection.edproduction.DW");
						conn = DriverManager.getConnection(DBconnectionString, db_userid, db_password);
					} else if (useEDMerge2) {
						DBconnectionString = configuration.getProperty("db.connection.edproduction2");
						conn = DriverManager.getConnection(DBconnectionString, db_userid, db_password);
					} else {
						DBconnectionString = configuration.getProperty("db.connection.edproduction");
						if (useETSCertificatesDB)
							DBconnectionString = configuration.getProperty("db.connection.EtsCertificatesProduction");
						conn = DriverManager.getConnection(DBconnectionString, db_userid, db_password);
					}
				}

				if (currentGeneralDB.equalsIgnoreCase("edBetaProduction")) {
					DBconnectionString = configuration.getProperty("db.connection.edbeta");
					if (useETSCertificatesDB)
						DBconnectionString = configuration.getProperty("db.connection.EtsCertificatesBeta");
					conn = DriverManager.getConnection(DBconnectionString, db_userid, db_password);
				}

				if (currentGeneralDB.equalsIgnoreCase("edProductionArab")) {
					DBconnectionString = configuration.getProperty("db.connection.edproductionArab");
					conn = DriverManager.getConnection(DBconnectionString, db_userid, db_password);
				}

				if (currentGeneralDB.equalsIgnoreCase("edProduction2")) {
					DBconnectionString = configuration.getProperty("db.connection.edproduction2");
					conn = DriverManager.getConnection(DBconnectionString, db_userid, db_password);
				}
			}

			return conn;
		} else if ((currentGeneralDB.equalsIgnoreCase("ci") || currentGeneralDB.equalsIgnoreCase("prod2")) && isUseEdDB()) {

			if (currentStaticDB.equalsIgnoreCase("internal")) {
				DBconnectionString = configuration.getProperty("db.connection.static");
				conn = DriverManager.getConnection(DBconnectionString, db_userid, db_password);
			} else {

				if (currentGeneralDB.equalsIgnoreCase("ci")) { // Ed ci olny
					DBconnectionString = configuration.getProperty("db.connection.ci");
					if (useETSCertificatesDB)
						DBconnectionString = configuration.getProperty("db.connection.EtsCertificate");
					conn = DriverManager.getConnection(DBconnectionString, db_userid, db_password);

				} else { // ED prod2 only
					DBconnectionString = configuration.getProperty("db.connection.prod2");
					if (useETSCertificatesDB)
						DBconnectionString = configuration.getProperty("db.connection.EtsCertificateRC");
					conn = DriverManager.getConnection(DBconnectionString, db_userid, db_password);
				}
			}

			return conn;
		} else if (currentGeneralDB.equalsIgnoreCase("TOEIC_OLPC") || isUseToeicOlpc()) {
			if (currentGeneralDB.equalsIgnoreCase("edProduction") || currentGeneralDB.equalsIgnoreCase("edBetaProduction")) {
				db_userid = configuration.getProperty("db.connection.username.production");
				db_password = configuration.getProperty("db.connection.password.production");
			}

			DBconnectionString = checkAndReturnRelevantToicConnection();
			conn = DriverManager.getConnection(DBconnectionString, db_userid, db_password);
			return conn;
		} else if (isUseOfflineDB()) {

			conn = DriverManager.getConnection(DBconnectionString, db_userid, db_password);
			return conn;
		} else if (logProfiler != false) {
			initDbLogger();
		}
		return conn;

		// report.addTitle("The connection is: " + DBconnectionString + ", DB UserName:
		// " + db_userid + ", DB Password: " + db_password);
	}

	private String checkAndReturnRelevantToicConnection() {
		String DBconnectionString = "";

		if (useToeicOnlineDB) {
			switch (currentGeneralDB) {
				case "ci":
					DBconnectionString = configuration.getProperty("db.connectionToeicOnlineNewQA");
					break;
				case "prod2":
					DBconnectionString = configuration.getProperty("db.connectionToeicOnlineRC");
					break;
				case "edProduction":
					DBconnectionString = configuration.getProperty("db.connection.ToeicOnlineProduction");
					break;
				case "edBetaProduction":
					DBconnectionString = configuration.getProperty("db.connection.ToeicOnlineBeta");
					break;
			}
			return DBconnectionString;
		}

		if (currentGeneralDB.equalsIgnoreCase("ci")) {
			DBconnectionString = configuration.getProperty("db.connectionToeicOlpc");
		}
		if (currentGeneralDB.equalsIgnoreCase("prod2")){
			DBconnectionString = configuration.getProperty("db.connectionToeicOlpcRC");
		}
		if (currentGeneralDB.equalsIgnoreCase("edProduction") //|| currentGeneralDB.equalsIgnoreCase("edBetaProduction")
				|| currentGeneralDB.equalsIgnoreCase("edProduction2")) {
			DBconnectionString = configuration.getProperty("db.connectionToeicOlpcProduction");
			db_userid = configuration.getProperty("db.connection.username.production");
			db_password = configuration.getProperty("db.connection.password.production");
		}

		if (currentGeneralDB.equalsIgnoreCase("edBetaProduction"))
			DBconnectionString = configuration.getProperty("db.connectionToeicOlpcBeta");


		return DBconnectionString;
	}

	private void initDbLogger() throws IOException, Exception {
		if (logger == null) {
			logger = java.util.logging.Logger.getLogger("com.microsoft.sqlserver.jdbc");
			FileHandler fh;
			fh = new FileHandler("files/log" + sig(6) + ".txt");
			logger.addHandler(fh);
			SimpleFormatter formatter = new SimpleFormatter();
			fh.setFormatter(formatter);
			logger.setLevel(Level.FINE);
		}
	}

	// public void runStorePrecedure(String precedureName, String... params)
	// throws SQLException {
	// String SPsql = "EXEC " + precedureName + " ?,?,?"; // for stored proc
	// // taking 2
	// // parameters
	// conn = getConnection();
	// CallableStatement statement = conn.prepareCall(SPsql);
	// // statement.registerOutParameter(0, params[0]);
	// }

	public void runStorePrecedure(String sp) throws SQLException {
		runStorePrecedure(sp, false, false);
	}

	public void runStorePrecedure(String sp, boolean executeQuery) throws SQLException {
		runStorePrecedure(sp, executeQuery, false);
	}

	public void runStorePrecedure(String sp, boolean executeQuery, boolean executeBatch) throws SQLException {
		try {
			conn = getConnectionNew();
			CallableStatement statement = conn.prepareCall(sp);

			report.report("SP was: " + sp);
			// System.out.println(sp);
			if (executeBatch == false) {
				if (executeQuery == true) {
					// for offline DB
					statement.executeQuery(); // returns results- good to know the records
				} else {
					statement.executeUpdate(); // Doesn't return results
				}
			} else {
				PreparedStatement ps = conn.prepareStatement(sp);
				ps.execute();
				conn.commit();
				ps.clearBatch();
			}

		} catch (SQLServerException e) {
			// testResultService.addFailTest("SQL server exception: "+e.getMessage());
			report.reportFailure("SQL server exception: " + e.getMessage());
			System.out.println("Check that class has no expired package or remainig");
			e.printStackTrace();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (!conn.isClosed()) {
				conn.close();
			}
		}
	}

	public List<String[]> getUnitNamesByCourse(String courseId) throws Exception {
		String sql = "	select UnitName,unitId from Units where CourseId=" + courseId + " order by Sequence";

		return getStringListFromQuery(sql, 1, true);
	}

	public void setInstitutionLastOfflineSyncToNull(String institutionId) throws Exception {
		String sql = "update Institutions set LastOfflineSync =null where InstitutionId=" + institutionId;
		runDeleteUpdateSql(sql);

	}

	public String getUserFirstNameByUserId(String userId) {

		String sql = "select FirstName from Users where UserId=" + userId;

		String firstName = "";
		try {
			firstName = getStringFromQuery(sql, 1, true, 1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (firstName == null)
			return "null";
		else
			return firstName;
	}

	public String getUserLastNameByUserId(String userId) {

		String sql = "select LastName from Users where UserId=" + userId;

		String lastName = "";

		try {
			lastName = getStringFromQuery(sql, 1, true, 1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (lastName == null)
			return "null";
		else
			return lastName;
	}

	public List<String[]> getCourseItems(String courseId) throws Exception {
		return getCourseItems(courseId, false, false);
	}

	public List<String[]> getCourseItems(String courseId, boolean useTestItems, boolean useBothTestAndProgressItems)
			throws Exception {
		String testItemQuery = "";
		if (useBothTestAndProgressItems == false) {
			if (useTestItems == true) {
				testItemQuery = "SubComponentId=3 and";
			} else {
				testItemQuery = "SubComponentId!=3 and";
			}
		}
		String sql = "select ItemId from Item where ComponentSubComponentId in (select ComponentSubComponentId from ComponentSubComponents where "
				+ testItemQuery
				+ " ComponentId in(select ComponentId from UnitComponents where UnitId in (select unitId from units where CourseId="
				+ courseId + ")))";

		List<String[]> items = getStringListFromQuery(sql, 1, 1);
		return items;

	}

	public List<String[]> getMultipleCoursesItems(List<String> courseIds) throws Exception {
		List<String[]> items = new ArrayList<String[]>();
		for (int i = 0; i < courseIds.size(); i++) {
			items.addAll(getCourseItems(courseIds.get(i)));
		}
		return items;
	}

	public List<String> getCourseTestItems(String courseId) throws Exception {
		String sql = "select ItemId from Item where ComponentSubComponentId in (select ComponentSubComponentId from ComponentSubComponents where ComponentId in(select ComponentId from UnitComponents where UnitId in (select unitId from units where CourseId="
				+ courseId + ")))";
		List<String> items = getArrayListFromQuery(sql, 1);
		return items;

	}

	public List<String[]> getUnitItems(String unitId) throws Exception {
		return getUnitItems(unitId, false, false);
	}

	public List<String[]> getUnitItems(String unitId, boolean useTestItems, boolean useBothProgressAndTestItems)
			throws Exception {

		String testItemsSql = "";
		if (!useBothProgressAndTestItems) {
			if (!useTestItems) {
				testItemsSql = "SubComponentId!=3 and";
			} else {
				testItemsSql = "SubComponentId=3 and";
			}
		}
		String sql = "select ItemId from Item where ComponentSubComponentId in (select ComponentSubComponentId from ComponentSubComponents where "
				+ testItemsSql + " ComponentId in(select ComponentId from UnitComponents where UnitId=" + unitId + "))";

		List<String[]> items = getStringListFromQuery(sql, 1, 1);
		return items;

	}

	public List<String> getCourseUnits(String courseId) throws Exception {
		String sql = "select unitId from units where CourseId=" + courseId + " order by Sequence ";
		List<String> units = getArrayListFromQuery(sql, 1);
		return units;

	}

	// Returns the old units id for the new courses (B1 - A3)
	public List<String> getCourseBaseUnits(String courseId) throws Exception {
		String sql = "select BaseUnitId from units where CourseId=" + courseId + " order by Sequence ";
		List<String> units = getArrayListFromQuery(sql, 1);
		return units;

	}

	public List<String[]> getCourseUnitDetils(String courseId) throws Exception {
		String sql = "select UnitId,UnitName from units where CourseId=" + courseId + " order by Sequence ";
		List<String[]> units = getStringListFromQuery(sql, 1, 2);
		return units;
	}

	public List<String[]> getComponentItems(String componentsId) throws Exception {
		return getComponentItems(componentsId, false);

	}

	public List<String[]> getComponentItems(String componentsId, boolean useTestItemss) throws Exception {
		return getComponentItems(componentsId, useTestItemss, false);
	}

	public List<String[]> getComponentItems(String componentsId, boolean useTestItems,
											boolean userBothTestAndProgressItems) throws Exception {

		String ignoreTestItems = "";
		if (!useTestItems) {
			ignoreTestItems = " SubComponentId<>3 and ";
		} else if (!userBothTestAndProgressItems) {
			ignoreTestItems = " SubComponentId=3 and ";
		}

		String sql = "select ItemId from Item where ComponentSubComponentId in (select ComponentSubComponentId from ComponentSubComponents where "
				+ ignoreTestItems + " ComponentId =" + componentsId + ")";
		List<String[]> items = getStringListFromQuery(sql, 1, 1);
		return items;
	}

	public boolean checkIfComponentHasTest(String componentsId) {

		String sql = "select SubComponentId from ComponentSubComponents" + " where ComponentId = " + componentsId
				+ " and SubComponentId = 3";

		boolean hasTest = false;
		String result = null;
		try {
			result = getStringFromQuery(sql, 1, true, 1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (result != null) {
			if (result.equalsIgnoreCase("3"))
				hasTest = true;
		}

		return hasTest;
	}

	public List<String[]> getComponentDetailsByComponentId(String componentId) throws Exception {

		String sql = "select Component.Name, Component.ComponentId, Skill.SkillId, Subject.Name FROM Component"
				+ " inner join Subject ON Component.SubjectId = Subject.SubjectId"
				+ " inner join Skill ON Skill.SkillId = Component.SkillId" + " where Component.ComponentId ="
				+ componentId;

		return getStringListFromQuery(sql, 1, 4);
	}

	public List<String[]> getComponentDetailsByUnitId(String unitId) throws Exception {

		String sql = "select Component.Name,Component.ComponentId, Skill.SkillId, Subject.Name from Component "
				+ " inner join Skill on Component.SkillId=Skill.SkillId" + " inner join Subject"
				+ " on Component.SubjectId=Subject.SubjectId" + " inner join UnitComponents"
				+ " on UnitComponents.ComponentId=Component.ComponentId" + " where UnitComponents.UnitId=" + unitId
				+ " order by UnitComponents.Sequence";

		return getStringListFromQuery(sql, 1, false);
	}

	public List<String[]> getComponentDetailsByCourseId(String courseId,String unitId) throws Exception {

		String sql = "select comp.componentid,comp.Name"
				+ " from Component as comp"
				+ " inner join UnitComponents as uc on uc.ComponentId = comp.ComponentId"
				+ " inner join units as u on u.UnitId = uc.UnitId"
				+ " where u.UnitId = "+ unitId +" and u.CourseId =" + courseId
				+ " order by uc.Sequence";

		return getStringListFromQuery(sql, 1, false);
	}

	public List<String[]> getComponentDetailsByCourseId(String courseId) throws Exception {

		String sql = "select Component.Name,Component.ComponentId, Skill.SkillId, Subject.Name FROM Course "
				+ " inner join Units ON Course.CourseId = Units.CourseId"
				+ " inner join unitcomponents ON Units.Unitid = Unitcomponents.Unitid"
				+ " inner join Component inner join Subject ON Component.SubjectId = Subject.SubjectId"
				+ " ON UnitComponents.ComponentId = Component.ComponentId"
				+ " inner join Skill ON Skill.SkillId = Component.SkillId" + " where Course.CourseId =" + courseId
				+ " group by Component.Name, Component.ComponentId, Skill.SkillId, Subject.Name, Units.UnitId";

		return getStringListFromQuery(sql, 1, false);
	}

	public List<String[]> getLatestMagazineArticlesSet() throws Exception {

		String sql = "SELECT TOP 9 Articles.Title, Articles.ArticleText, ArticleTopics.Name as Topic, Level.Name as Level, Articles.Code, Articles.MainFeature FROM Articles "
				+ " inner join ArticleTopics" + " on Articles.ArticleTopicId = ArticleTopics.ArticleTopicId"
				+ " inner join Level" + " on Articles.LevelId = Level.LevelId"
				+ " order by Articles.PublicationDate desc, Articles.MainFeature desc, Articles.ArticleId asc";

		return getStringListFromQuery(sql, 1, 6);
	}

	public List<String[]> getMagazineArticlesSetByMonthYear(String month, String year) throws Exception {

		String sql = "SELECT Articles.Title, Articles.ArticleText, ArticleTopics.Name as Topic, Level.Name as Level, Articles.Code, Articles.MainFeature FROM Articles "
				+ " inner join ArticleTopics" + " on Articles.ArticleTopicId = ArticleTopics.ArticleTopicId"
				+ " inner join Level" + " on Articles.LevelId = Level.LevelId"
				+ " where Datename(m,PublicationDate) = '" + month + "' and YEAR (PublicationDate) = " + year
				+ " order by Articles.ArticleId desc";

		return getStringListFromQuery(sql, 1, 6);
	}

	public List<String[]> getLatestMagazinePromotion() throws Exception {

		String sql = "SELECT distinct top 3 Title,PublicationDate " + " FROM Articles"
				+ " order by PublicationDate desc";

		return getStringListFromQuery(sql, 1, 2);
	}

	public String getFirstItemInComponent(String componentId) throws Exception {
		String sql = "select TOP 1 * from Item where ComponentSubComponentId in (select ComponentSubComponentId from ComponentSubComponents where ComponentId =20448) and Instructions is not null ORDER BY Sequence asc";
		String item = getStringFromQuery(sql);
		return item;

	}

	public String getLastItemInComponent(String componentId) throws Exception {
		String sql = "select TOP 1 * from Item where ComponentSubComponentId in (select ComponentSubComponentId from ComponentSubComponents where ComponentId =20448) and Instructions is not null ORDER BY Sequence desc";
		String item = getStringFromQuery(sql);
		return item;

	}

	public String getComponentItemBySequence(String componentId, String seqIndex) throws Exception {
		String sql = "select ItemId from Item where ComponentSubComponentId in (select ComponentSubComponentId from ComponentSubComponents where ComponentId ="
				+ componentId + ") and Instructions is not null and Sequence=" + seqIndex;
		String item = getStringFromQuery(sql);
		return item;
	}

	public List<String[]> getInstitutionCourses(String institutionId) throws Exception {
		String sql = " select Name from Course where CourseId in(select courseId from PackageDetails where PackageId in ( select PackageId from InstitutionPackages where InstitutionId="
				+ institutionId + "  and EndDate<CURRENT_TIMESTAMP)) order by Sequence";
		report.report(sql);
		// return getArrayListFromQuery(sql, 1);
		return getStringListFromQuery(sql, 1, 1);
	}

	public List<String[]> getUserAssignedCourses(String userId) throws Exception {

		List<String[]> rsList = getListFromStoreRrecedure("EXEC GetAssignedCourses2 " + userId);
		//List<String[]> list = rsList.get(0);

		for (int i = 0; i < rsList.size(); i++) {

			String[] courseDetails = rsList.get(i);

			for (int start = 0, end = courseDetails.length - 1; start <= end; start++, end--) {

				String temp = courseDetails[start];
				courseDetails[start] = courseDetails[end];
				courseDetails[end] = temp;

			}
		}

		return rsList; // returns list of string arrays ["CoureName","CourseID"]

	}

	public String getUnitIdByNameAndCourse(String unitName, String CourseId) throws Exception {
		String sql = "select UnitId from units where unitName = '" + unitName + "' and CourseId = " + CourseId;
		return getStringFromQuery(sql);
	}

	public List<String[]> getSubComponentsDetailsByComponentId(String componentId) throws Exception {

		String sqlCheck = "undefined";
		String sql = "undefined";

		// check DB version of SubComponents tables
		sqlCheck = "IF EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'StepNames') select 'true' else select 'false'";
		boolean isNewDbVersion = Boolean.parseBoolean(getStringFromQuery(sqlCheck));

		if (isNewDbVersion) {
			sql = "select sn.StepName, csc.ComponentSubComponentId from ComponentSubComponents csc inner join StepNames sn on csc.StepNameId = sn.StepNameId inner join SubComponent sc on csc.SubComponentId = sc.SubComponentId where csc.ComponentId="
					+ componentId
					+ " and csc.SubComponentId=sc.SubComponentId and csc.Visible = 1 order by isNull (csc.StepSequence, sc.Sequence)";

		} else {
			sql = "select isNull (ComponentSubComponents.StepName, SubComponent.Name),ComponentSubComponents.ComponentSubComponentId from SubComponent,ComponentSubComponents where ComponentSubComponents.ComponentId="
					+ componentId
					+ " and ComponentSubComponents.SubComponentId=SubComponent.SubComponentId and ComponentSubComponents.Visible = 1 order by isNull (ComponentSubComponents.StepSequence, SubComponent.Sequence)";
		}

		List<String[]> subComponents = getStringListFromQuery(sql, 1, 2);
		return subComponents;
	}

	public List<String[]> getSubComponentItems(String subComponentId) throws Exception {
		// String sql = "select distinct ItemId,Code from item,ItemType where
		// ComponentSubComponentId=" + subComponentId;
		String sql = "select distinct ItemId,Code, ItemTypeId, Sequence from item where ComponentSubComponentId="
				+ subComponentId + " order by Sequence asc";
		// return getStringListFromQuery(sql, 1, 2);
		return getStringListFromQuery(sql, 1, 3);
	}

	public String getUserClassId(String studentId) throws Exception {
		// TODO Auto-generated method stub
		String sql = " select classId from ClassUsers where UserId=" + studentId;
		System.out.println(sql);
		return getStringFromQuery(sql, 1, true, 1);
	}

	public List<String[]> getTeacherClassId(String userId) throws Exception {

		List<String[]> classesList = null;

		String sql = "select ClassId"
				+ " from TeacherClasses"
				+ " inner join UserPermissions on UserPermissions.UserPermissionsId = TeacherClasses.UserPermissionsId"
				+ " where UserPermissions.UserId = "
				+ userId;
		classesList = getStringListFromQuery(sql, 1, true);

		return classesList;
	}

	public String getClassNameByClassId(String classId) throws Exception {
		// TODO Auto-generated method stub
		String sql = "select Name from class where classId = " + classId;
		return getStringFromQuery(sql, 1, true, 1);
	}

	public List<String[]> getClassCourses(String classId) throws Exception {
		// String sp = " exec GetClassCoursePlan " + classId;
		// return getListFromSP(sp,1);

		String sql = " select Name,CourseId from Course where CourseId in(select CourseId from PackageDetails where PackageId in(select PackageId from InstitutionPackages where InstitutionPackageId in( select InstitutionPackageId from ClassAssignedPackages where ClassId="
				+ classId + " )))  order by Sequence";
		return getStringListFromQuery(sql, 1, 2);
	}

	public List<String> getClassCoursesByClassId(String classId) throws Exception {
		// String sp = " exec GetClassCoursePlan " + classId;
		// return getListFromSP(sp,1);

		String sql = " select CourseId from Course where CourseId in(select CourseId from PackageDetails where PackageId in(select PackageId from InstitutionPackages where InstitutionPackageId in( select InstitutionPackageId from ClassAssignedPackages where ClassId="
				+ classId + " )))  order by Sequence";
		return getArrayListFromQuery(sql, 1);
	}

	public List<String> getStudentAssignedCourses(String userId) throws Exception {

		BigDecimal _userId = new BigDecimal(userId);
		String sql = "{call GetAssignedCourses2(?) }";
		return getDataFromSP(sql, _userId);

	}

	public String getInstitutionIdByUserId(String studentId) throws Exception {
		// TODO Auto-generated method stub
		String sql = "select InstitutionId from Users where UserId=" + studentId;
		return getStringFromQuery(sql);
	}

	// public List<String> getUnitComponents(String string) {
	// // TODO Auto-generated method stub
	// return null;
	// }

	public String getCourseNameById(String courseId) throws Exception {
		String sql = "select name from course where courseId=" + courseId;
		return getStringFromQuery(sql);

	}

	public void setUserLog(String userId) throws Exception {
		String sp = "exec SetUserLog " + userId;
		runStorePrecedure(sp);
	}

	public List<String> getInstitutionStudents(String institutioId) throws Exception {
		String sql = "select userId from Users where InstitutionId=" + institutioId;
		return getArrayListFromQuery(sql, 1);
	}

	public List<String> getInstitutionStudentsWithProgress(String institutioId, String courseId) throws Exception {
		String sql = "select distinct userId from Progress where UserId in (select userId from Users where InstitutionId="
				+ institutioId + ") and CourseId=" + courseId;
		return getArrayListFromQuery(sql, 1);
	}

	public String getCourseIdByUnitId(String unitId) throws Exception {
		String sql = " select courseId from Units where UnitId=" + unitId;
		return getStringFromQuery(sql);
	}

	public String getLessonNameByCourseUnitAndLessonNumber(String courseId, int unitNumber, int lessonNumber)
			throws Exception {
		List<String> courseUnits = getCourseUnits(courseId);
		List<String[]> components = getComponentDetailsByUnitId(courseUnits.get(unitNumber));
		// String lessonName = components.get(lessonNumber-1)[0];
		String lessonName = getLessonNameBySkill(components.get(lessonNumber - 1));
		return lessonName;

	}

	public String getNumberOfSectionsInTestForCourseByCourseId(String courseId, int testType) throws Exception {
		String sql = "SELECT COUNT (Component.ComponentId) from testBank inner join course "
				+ "ON testBank.TestId = Course.courseId inner join Units Units "
				+ "ON Course.CourseId = Units.CourseId inner join unitcomponents "
				+ "ON Units.Unitid = Unitcomponents.Unitid inner join Component "
				+ "On UnitComponents.ComponentId = Component.ComponentId " + "where TestBank.[CourseId] = " + courseId
				+ " and testBank.Active = " + 1 + "  and ImpTypeFeatureId = " + testType;
		return getStringFromQuery(sql);
	}

	public String getNumberOfSectionsInTestForCourseByTestId(String courseId, String testId, int testType)
			throws Exception {
		String sql = "SELECT COUNT (Component.ComponentId) from testBank inner join course "
				+ "ON testBank.TestId = Course.courseId inner join Units Units "
				+ "ON Course.CourseId = Units.CourseId inner join unitcomponents "
				+ "ON Units.Unitid = Unitcomponents.Unitid inner join Component "
				+ "On UnitComponents.ComponentId = Component.ComponentId " + "where TestBank.[CourseId] = " + courseId
				+ " and TestBank.[TestId] = " + testId + " and " + "ImpTypeFeatureId = " + testType;
		return getStringFromQuery(sql);
	}

	// Not ready - Should be continue
	public String getTestIdByCourseId(String courseId, String testType, String randome) throws Exception {
		String sql = "EXEC GetCandidateCourseTestId @UserId,@AssignCourseId,@ImpTypeFeatureId,@TestId";
		return getStringFromQuery(sql);
	}

	public String getLessonNameBySkill(String[] compDetails) {
		String expectedLessonText;
		if (compDetails[2].equals("6")) {
			// if grammar
			expectedLessonText = compDetails[3] + ": " + compDetails[0];
		} else {
			expectedLessonText = compDetails[0];
		}
		return expectedLessonText;
	}

	public String getItemIdByItemCode(String itemCode) throws Exception {
		String sql = "select distinct ItemId,Code from item,ItemType where code='" + itemCode + "'";
		return getStringFromQuery(sql);
	}

	public String getLessonItemsNumber(String lessonId) throws Exception {
		String sql = "select COUNT(itemId) from Item where ComponentSubComponentId in  (select ComponentSubComponentId from ComponentSubComponents where ComponentId="
				+ lessonId + ")";
		return getStringFromQuery(sql);
	}

	public String getUnitNameById(String unitId) throws Exception {
		String sql = "select UnitName from Units where UnitId=" + unitId;
		return getStringFromQuery(sql);
	}

	public String getComponentNameById(String componentId) throws Exception {
		String sql = "select Name from Component where ComponentId=" + componentId;
		return getStringFromQuery(sql);
	}

	public int getSchoolPassingGrade(String institutionId) throws Exception {
		String sql = " select passinggrade from Institutions where InstitutionId = " + institutionId
				+ " and Visible = 1";
		String result = getStringFromQuery(sql);
		return Integer.valueOf(result);
	}

	public String getStudentTimeOnTaskOLD(String courseId, String studentId) throws Exception {
		// --igb 2018.06.06-------
		/*
		 * String rowCount = getStringFromQuery(
		 * "SELECT count(*) FROM Units INNER JOIN VisitedComponents ON Units.UnitId = VisitedComponents.UnitId INNER JOIN UserLog ON VisitedComponents.UserLogId = UserLog.UserLogId INNER JOIN Users ON UserLog.UserId = Users.UserId where Units.CourseId = "
		 * + courseId + " and Users.UserId = " + studentId);
		 *
		 * if(rowCount.equals("0")) return "00:00";
		 */
		// --igb 2018.06.06-------------

		String timeOntask = getStringFromQuery(
				"SELECT  ISNULL(CONVERT(varchar, DATEADD(SECOND , sum (VisitedComponents.Seconds), 0), 108),'00:00:00') as TOTAL FROM Units INNER JOIN VisitedComponents ON Units.UnitId = VisitedComponents.UnitId INNER JOIN UserLog ON VisitedComponents.UserLogId = UserLog.UserLogId INNER JOIN Users ON UserLog.UserId = Users.UserId where Units.CourseId = "
						+ courseId + " and Users.UserId = " + studentId);
		// .substring(0, 5);

		return timeOntask;
	}

	public String getStudentTimeOnTask(String courseId, String studentId) throws Exception {
		// --igb 2018.06.06-------
		/*
		 * String rowCount = getStringFromQuery(
		 * "SELECT count(*) FROM Units INNER JOIN VisitedComponents ON Units.UnitId = VisitedComponents.UnitId INNER JOIN UserLog ON VisitedComponents.UserLogId = UserLog.UserLogId INNER JOIN Users ON UserLog.UserId = Users.UserId where Units.CourseId = "
		 * + courseId + " and Users.UserId = " + studentId + " and Units.Sequence = " +
		 * i);
		 *
		 * if(rowCount.equals("0")) return "00:00";
		 */
		// --igb 2018.06.06-------------

		String timeOntask = getStringFromQuery(
				"SELECT  ISNULL(CONVERT(varchar, DATEADD(SECOND , sum (VisitedComponents.Seconds), 0), 108),'00:00:00') as TOTAL FROM Units INNER JOIN VisitedComponents ON Units.UnitId = VisitedComponents.UnitId INNER JOIN UserLog ON VisitedComponents.UserLogId = UserLog.UserLogId INNER JOIN Users ON UserLog.UserId = Users.UserId where Units.CourseId = "
						+ courseId + " and Users.UserId = " + studentId + "");
		// .substring(0, 5);

		return timeOntask;
	}

	public String getStudentTimeOnLessonInSecondsInUnit(String studentId, String courseId, String unitsSequence) throws Exception {

		String sql = "SELECT ISNULL(CONVERT(varchar, DATEADD(SECOND,SUM(DATEDIFF(SECOND,TOL.StartServiceTimeStamp,TOL.EndServiceTimeStamp)),0),108),'null') as Total"
				+ " FROM TimeOnLesson as TOL" + " INNER JOIN UnitComponents as UC ON UC.ComponentId = TOL.LessonId"
				+ " INNER JOIN Units on Uc.UnitId = Units.UnitId"
				+ " INNER JOIN Course ON Units.CourseId = Course.CourseId" + " WHERE TOL.UserId = " + studentId
				+ " AND Course.CourseId =" + courseId + " AND Units.sequence =" + unitsSequence + "";

		String timeOnUnit = getStringFromQuery(sql);

		return timeOnUnit;
	}

	public List<String[]> getClassTimeOnTaskInSecondsBySkill(String className, String courseId, String skillName)
			throws Exception {

		/*
		 * String sql = ";with cte as(" +
		 * " SELECT Users.UserId,sum(VisitedComponents.Seconds)/60.0 as Total" +
		 * " FROM VisitedComponents" +
		 * " INNER JOIN Component ON VisitedComponents.ComponentId = Component.ComponentId"
		 * +
		 * " INNER JOIN UnitComponents ON Component.ComponentId = UnitComponents.ComponentId"
		 * + " INNER JOIN Units ON UnitComponents.UnitId = Units.UnitId" +
		 * " INNER JOIN Course ON Course.CourseId = Units.CourseId" +
		 * " INNER JOIN UserLog ON VisitedComponents.UserLogId = UserLog.UserLogId" +
		 * " INNER JOIN Users ON UserLog.UserId = Users.UserId" +
		 * " INNER JOIN ClassUsers ON Users.UserId = ClassUsers.UserId" +
		 * " INNER JOIN Class ON ClassUsers.ClassId = Class.ClassId" +
		 * " INNER JOIN Skill ON Skill.SkillId = Component.SkillId" +
		 * " WHERE Course.CourseId = "+ courseId + " AND Class.Name =+"+"'" + className
		 * +"'"+ " and Skill.Name ="+"'"+ skillName+ "'"+"" + " GROUP BY Users.UserId)"
		 * +
		 * " SELECT ISNULL(CONVERT(varchar, DATEADD(MINUTE,Sum(Total)/count(UserId),0),108),'null') as Total from cte"
		 * ;
		 */

		String sql = ";with cte as"
				+ " (SELECT Skill.Name AS SkillName, SUM(VisitedComponents.Seconds)/COUNT(DISTINCT(UserLog.UserId)) AS Seconds ,COUNT(DISTINCT(UserLog.UserId)) AS UnitUsersCount"
				+ " FROM  VisitedComponents INNER JOIN" + " Units ON VisitedComponents.UnitId = Units.UnitId INNER JOIN"
				+ " UserLog ON VisitedComponents.UserLogId = UserLog.UserLogId INNER JOIN"
				+ " ClassUsers ON UserLog.UserId = ClassUsers.UserId INNER JOIN"
				+ " UnitComponents ON Units.UnitId = UnitComponents.UnitId AND VisitedComponents.ComponentId = UnitComponents.ComponentId INNER JOIN"
				+ " Component ON VisitedComponents.ComponentId = Component.ComponentId AND UnitComponents.ComponentId = Component.ComponentId INNER JOIN"
				+ " Skill ON Component.SkillId = Skill.SkillId INNER JOIN"
				+ " Class ON Class.ClassId = ClassUsers.ClassId" + " WHERE Units.CourseId = " + courseId
				+ " AND Class.Name =" + "'" + className + "'" + " and Skill.Name =" + "'" + skillName + "'" + ""
				+ " GROUP BY Skill.Name)"
				+ " SELECT MAX(cte.Seconds) / 3600 AS [Hours], (MAX(cte.Seconds) / 60) % 60  AS [Minutes] from cte";

		List<String[]> timeOnTaskBySkill = getStringListFromQuery(sql, 1, 2);

		return timeOnTaskBySkill;
	}

	public List<String[]> getClassTimeOnTaskInSecondsByUnit(String className, String courseId, int unitSequence)
			throws Exception {

		/*
		 * String sql = ";with cte as(" +
		 * " SELECT Users.UserId,sum(VisitedComponents.Seconds)/60.0 as Total" +
		 * " FROM VisitedComponents" +
		 * " INNER JOIN Component ON VisitedComponents.ComponentId = Component.ComponentId"
		 * +
		 * " INNER JOIN UnitComponents ON Component.ComponentId = UnitComponents.ComponentId"
		 * + " INNER JOIN Units ON UnitComponents.UnitId = Units.UnitId" +
		 * " INNER JOIN Course ON Course.CourseId = Units.CourseId" +
		 * " INNER JOIN UserLog ON VisitedComponents.UserLogId = UserLog.UserLogId" +
		 * " INNER JOIN Users ON UserLog.UserId = Users.UserId" +
		 * " INNER JOIN ClassUsers ON Users.UserId = ClassUsers.UserId" +
		 * " INNER JOIN Class ON ClassUsers.ClassId = Class.ClassId" +
		 * " WHERE Course.CourseId = "+ courseId + " AND Class.Name ="+"'"+ className
		 * +"'"+ " and Units.Sequence ="+unitSequence+" " + " GROUP BY Users.UserId)" +
		 * " SELECT ISNULL(CONVERT(varchar, DATEADD(MINUTE,Sum(Total)/count(UserId),0),108),'null') as Total from cte"
		 * ;
		 *
		 * String timeOnTaskByUnit = getStringFromQuery(sql);
		 *
		 * return timeOnTaskByUnit;
		 */
		String sql = ";with cte as (" + " SELECT UnitId, AVG(Seconds) AS Seconds, COUNT(*) AS UnitUsersCount"
				+ " FROM (" + " SELECT SUM(Seconds) AS Seconds, UnitId, UserID" + " FROM("
				+ " SELECT VisitedComponents.Seconds, a.UserId AS UserId, a.UnitId" + " FROM  VisitedComponents"
				+ " INNER JOIN  UserLog ON VisitedComponents.UserLogId = UserLog.UserLogId"
				+ " INNER JOIN  Units ON VisitedComponents.UnitId = Units.UnitId and units.Sequence=" + unitSequence
				+ "" + " RIGHT JOIN  (SELECT ClassUsers.UserId, Units_1.UnitId"
				+ " FROM   ClassUsers  join class on ClassUsers.ClassId = Class.ClassId"
				+ " CROSS JOIN  Units AS Units_1" + " WHERE Units_1.CourseId =" + courseId + " AND class.Name =" + "'"
				+ className + "'" + ") AS a" + " ON Units.UnitId = a.UnitId" + " AND a.UserId = UserLog.UserId"
				+ " AND Units.CourseId = 20000)a" + " GROUP BY UserID, UnitId) b" + " WHERE b.Seconds is NOT NULL"
				+ " GROUP by UnitId)"

				+ " SELECT MAX(cte.Seconds) / 3600 AS [Hours], (MAX(cte.Seconds) / 60) % 60 AS [Minutes] from cte";

		List<String[]> timeOnTaskByUnit = getStringListFromQuery(sql, 1, 2);

		return timeOnTaskByUnit;

	}

	public List<String[]> getClassTimeOnLessonInSecondsBySkill(String className, String courseId, String skillName)
			throws Exception {

		/*
		 * String sql =
		 * ";WITH cte AS(SELECT tol.UserId, round(SUM(DATEDIFF(ss, TOL.StartServiceTimeStamp, TOL.EndServiceTimeStamp))/60,0) as Total"
		 * + " FROM TimeOnLesson as TOL" +
		 * " INNER JOIN UnitComponents as UC ON UC.ComponentId = TOL.LessonId" +
		 * " INNER JOIN Units on Uc.UnitId = Units.UnitId" +
		 * " INNER join Component on uc.ComponentId = Component.ComponentId" +
		 * " INNER JOIN Course ON Units.CourseId = Course.CourseId" +
		 * " INNER JOIN Skill on Component.SkillId = Skill.SkillId" +
		 * " INNER JOIN ClassUsers as cu on cu.UserId = tol.UserId" +
		 * " INNER JOIN Class on Class.ClassId = cu.ClassId" +
		 * " WHERE Course.CourseId ="+ courseId +" AND class.Name ="+"'"+ className
		 * +"'"+" AND Skill.Name ="+"'"+ skillName +"'"+"" + "GROUP BY tol.UserId)" +
		 * "SELECT ISNULL(CONVERT(varchar, DATEADD(MINUTE,Sum(Total)/count(Userid),0),108),'null') as Total from cte"
		 * ;
		 */

		String sql = ";with cte as"
				+ " (SELECT s.Name AS SkillName, SUM(DATEDIFF(ss, tl.StartServiceTimeStamp, tl.EndServiceTimeStamp))/COUNT(DISTINCT tl.UserId) AS Seconds, COUNT(DISTINCT tl.UserId) AS UnitUsersCount"
				+ " FROM TimeOnLesson tl join" + " ClassUsers cu on tl.UserId = cu.UserId join"
				+ " UnitComponents uc on uc.ComponentId = tl.LessonId join"
				+ " Component c on c.ComponentId = uc.ComponentId join" + " Skill s on c.SkillId = s.SkillId join"
				+ " Units u on u.UnitId = uc.UnitId join" + " Course co on co.CourseId = u.CourseId join"
				+ " class on class.classid = cu.classid" + " WHERE co.CourseId =" + courseId + " AND class.Name =" + "'"
				+ className + "'" + " AND s.Name =" + "'" + skillName + "'" + "" + " GROUP BY s.Name) "
				+ " SELECT MAX(cte.Seconds) / 3600 AS [Hours], (MAX(cte.Seconds) / 60) % 60  AS [Minutes] from cte";

		List<String[]> timeOnLessonBySkill = getStringListFromQuery(sql, 1, 2);

		// timeOnLessonBySkill= data

		// String timeOnLessonBySkill = getStringFromQuery(sql);

		return timeOnLessonBySkill;
	}

	public List<String[]> getClassTimeOnLessonInSecondsByUnit(String className, String courseId, int unitSequence)
			throws Exception {


		String sql	= " select isnull(avg(uup.TimeOnUnitSeconds)/3600,0) AS [Hours],"
		+ " isnull((avg(uup.TimeOnUnitSeconds)/60 % 60),0) AS [Minutes]"
		+ " from ClassUsers as cu"
		+ " inner join users as u on cu.UserId = u.UserId"
		+ " inner join Class as c on cu.ClassId = c.ClassId"
		+ " inner join UserUnitProgress as uup on uup.UserId = u.UserId"
		+ " inner join Units on units.UnitId = uup.UnitId and u.UserId = uup.UserId"
		+ " inner join Course on course.CourseId = units.CourseId and units.UnitId = uup.UnitId"
		+ " where c.Name =" + "'" + className + "'"
		+ " and Course.CourseId = " + courseId + " and units.Sequence=" +unitSequence
		+ " group by uup.UnitId,units.Sequence"
		+ " order by units.Sequence";
/*
		String sql = " ;with cte as" + " (SELECT UnitId, AVG(Seconds) AS Seconds, COUNT(*) AS UnitUsersCount"
				+ " FROM (" + " SELECT SUM(Seconds) AS Seconds, UnitId, UserID" + " FROM("
				+ " select DATEDIFF(ss, tl.StartServiceTimeStamp, tl.EndServiceTimeStamp) as Seconds, tl.UserId, u.UnitId"
				+ " from TimeOnLesson tl join" + " UnitComponents uc on tl.LessonId = uc.ComponentId join"
				+ " Units u on u.UnitId = uc.UnitId and u.CourseId = tl.CourseId and u.Sequence=" + unitSequence
				+ " RIGHT JOIN  (SELECT ClassUsers.UserId, Units_1.UnitId"
				+ " FROM   ClassUsers join class on class.ClassId = ClassUsers.ClassId"
				+ " CROSS JOIN  Units AS Units_1" + " WHERE (class.name =" + "'" + className + "'"
				+ ") AND (Units_1.CourseId = " + courseId + ")) AS a" + " ON u.UnitId = a.UnitId"
				+ " AND a.UserId = tl.UserId" + " AND u.CourseId = " + courseId + ")a" + " GROUP BY UserID, UnitId) b"
				+ " WHERE b.Seconds is NOT NULL" + " GROUP by UnitId)"

				+ " SELECT MAX(cte.Seconds) / 3600 AS [Hours], (MAX(cte.Seconds) / 60) % 60 AS [Minutes] from cte";
*/
		List<String[]> timeOnLessonByUnit = getStringListFromQuery(sql, 0, true);

		return timeOnLessonByUnit;

	}

	public List<String> getSkillInCourse(String courseId) throws Exception {

		String sql = "SELECT Distinct Skill.Name" + " FROM Skill"
				+ " INNER JOIN Component on Skill.SkillId = Component.SkillId"
				+ " INNER JOIN UnitComponents as UC ON UC.ComponentId = Component.ComponentId"
				+ " INNER JOIN Units on Uc.UnitId = Units.UnitId"
				+ " INNER JOIN Course ON Units.CourseId = Course.CourseId" + " WHERE Course.CourseId =" + courseId + "";

		List<String> timeOnTaskBySkill = getArrayListFromQuery(sql, 0);

		return timeOnTaskBySkill;
	}

	public int getStudentTimeOnLessonInSeconds(String studentId, String lessonId) throws Exception {

		String timeOnLesson = getStringFromQuery(
				" select ISNULL(CONVERT(varchar,DATEADD(SECOND,SUM(DATEDIFF(SECOND,StartServiceTimeStamp,EndServiceTimeStamp)),0),108),'00:00:00') from TimeOnLesson where UserId = "
						+ studentId + " and LessonId = " + lessonId);

		return Integer.parseInt(timeOnLesson);
	}

	public String getStudentTimeOnLessonInSecondsInCourse(String studentId, String courseId) throws Exception {
		String timeOnCourse = getStringFromQuery(
				"select ISNULL(CONVERT(varchar,DATEADD(SECOND,SUM(DATEDIFF(SECOND,StartServiceTimeStamp,EndServiceTimeStamp)),0),108),'00:00:00')"
						+ " from TimeOnLesson where UserId = "
						+ studentId + " and CourseId = " + courseId);
		return timeOnCourse;
	}

	public void deleteStudentTimeOnLesson(String studentId) throws Exception {

		runDeleteUpdateSql("delete TimeOnLesson where UserId = " + studentId);

	}

	public void deleteStudentTimeOnTask(String studentId, String courseId) throws Exception {

		runDeleteUpdateSql(
				"DELETE VisitedComponents FROM VisitedComponents AS vc INNER JOIN UserLog AS ul ON vc.UserLogId = ul.UserLogId"
						+ " INNER JOIN UnitComponents uc ON uc.ComponentId = vc.ComponentId"
						+ " INNER JOIN Units ON Units.UnitId = uc.UnitId"
						+ " INNER JOIN Course ON Units.CourseId = Course.CourseId" + " WHERE ul.UserId =" + studentId
						+ " AND Course.CourseId = " + courseId);

		runDeleteUpdateSql("Delete VisitedComponentSubComponents where userId =" + studentId);
	}

	public void deleteStudentRecordScore(String studentId, String unitId, String compId) throws Exception {

		String sqlQuery = "update UserSRRecords "
				+ "set Grade = Null, TeacherId = Null, RaterDateTime = Null, PhonemeRate = Null, IntonationRate = Null, PronunRate = Null "
				+ "where StudentId = " + studentId + " and UnitId = " + unitId + " and ComponentId = " + compId;
		runDeleteUpdateSql(sqlQuery);

	}

	public List<String> getClassStudents(String classId) throws Exception {
		String sql = "select Users.UserName from ClassUsers,Users where ClassId=" + classId
				+ " and ClassUsers.UserId=Users.UserId";
		return getArrayListFromQuery(sql, 1);
	}

	public List<String> getClassStudentsID(String classId) throws Exception {
		String sql = "select Users.UserId from ClassUsers,Users where ClassId=" + classId
				+ " and ClassUsers.UserId=Users.UserId";
		return getArrayListFromQuery(sql, 1);
	}

	public void moveStudentToArchive(String userName) throws SQLException {
		String sp = "exec dbo.APIMoveUser " + configuration.getInstitutionId() + ",'" + userName + "','autoArchive'";
		runStorePrecedure(sp);

	}

	public void deleteStudentByName(String instID, String userName) throws SQLException {
		String sp = "exec dbo.DeleteUserByUserName " + "'" + userName + "'," + instID;
		runStorePrecedure(sp, false);

	}

	public void deleteUserById(String userId) throws SQLException {
		String sp = "exec dbo.DeleteUser " + "'" + userId + ";'";
		runStorePrecedure(sp);

	}

	public void deleteInstitutionById(String instId) throws SQLException {
		String sp = "exec dbo.DeleteInstitution " + "'" + instId + "';";
		runStorePrecedure(sp);

	}

	public void deleteInstitutionPackageById(String instId, String packageId) throws SQLException {
		String sp = "exec dbo.DeleteInstitutionPackage '" + packageId + "', '" + instId + "';";
		runStorePrecedure(sp);

	}

	public void deleteClassGroupById(String groupId) throws SQLException {
		String sp = "exec dbo.DeleteGroups 'Groups.groupid=" + groupId + "';";
		runStorePrecedure(sp);

	}

	public void deleteClassGroupByName(String groupName, String ClassId) throws Exception {
		String groupId = getStringFromQuery(
				"select GroupId from groups where name = '" + groupName + "' and classid = '" + ClassId + "';");
		String sp = "exec dbo.DeleteGroups 'Groups.groupid=" + groupId + "';";
		runStorePrecedure(sp);

	}

	public void deleteClassByName(String instID, String className) throws Exception {

		String classId = getClassIdByName(className, instID);
		String sp = "exec DeleteClasses " + classId;
		runStorePrecedure(sp);

	}

	public void deleteClassById(String classId) throws Exception {

		String sp = "exec dbo.DeleteClasses 'class.classid=" + classId + "'";
		runStorePrecedure(sp);

	}

	public void enableMultiTestForInstitution(String instID) throws Exception {
		String query = "insert into InstitutionProperties (institutionID, PropertyId, Value) values " + "('" + instID
				+ "','2','1');";
		runDeleteUpdateSql(query);
	}

	public void disableMultiTestForInstitution(String instID) throws Exception {
		String query = "delete InstitutionProperties where institutionId = " + instID + " and PropertyId = 2";
		runDeleteUpdateSql(query);
	}

	public void addTestToCourseByIds(String CourseId, String TestId, int TestType) throws Exception {
		String query = "insert into TestBank (CourseId,TestID,ImpTypeFeatureId,Active) values" + " ('" + CourseId
				+ "','" + TestId + "','" + TestType + "','1');";
		runDeleteUpdateSql(query);

	}

	public void removeTestFromCourseByIds(String CourseId, String TestId, int TestType) throws Exception {
		String query = "delete TestBank where CourseId = " + CourseId + " and testId = " + TestId
				+ " and ImpTypeFeatureId = " + TestType;
		runDeleteUpdateSql(query);
	}

	public void removeTestResults(String studentId, String CourseId, int TestType) throws Exception {
		String query = "";

		query = "delete TestResults Where userid =" + studentId;
		runDeleteUpdateSql(query);

		query = "delete trs from TestResultsState as trs"
				+ " join testresults as tr on trs.TestResultsId = tr.TestResultsId" + " where tr.UserId =" + studentId;

		runDeleteUpdateSql(query);
	}

	public void removeUserExitTestSettings(String studentId, String CourseId, int TestType) throws Exception {
		String query = "delete UserExitTestSettings where userid = " + studentId + " and CourseId = " + CourseId
				+ " and ImpTypeFeatureId =" + TestType;
		runDeleteUpdateSql(query);
	}

	public void setClassCourseSequence(String classId, String courseSequence) throws Exception {
		String SP = "SetClassCourseSequence " + classId + ", ' " + courseSequence + "'";
		runStorePrecedure(SP, true, true);
	}

	public List<String> getClassCourseSequence(String classId) throws Exception {
		String sql = "select courseId from ClassCourseSequence where classId = " + classId + " order by sequence";
		return getArrayListFromQuery(sql, 1);
	}

	public boolean isUseCI() {
		return useCI;
	}

	public void setUseCI(boolean useCI) {
		this.useCI = useCI;
	}

	public boolean isUseStaticContent() {
		return useStaticContent;
	}

	public boolean isUseStaticContentProduction() {
		return useStaticContentProduction;
	}

	public void setUseStaticContent(boolean isUseStaticContent) {
		this.useStaticContent = isUseStaticContent;
	}

	public void setUseStaticContentProduction(boolean isUseStaticContentProduction) {
		this.useStaticContentProduction = isUseStaticContentProduction;
	}

	public void setUseToeicOlpc(boolean UseToeicOlpc) {
		this.useToeicDB = UseToeicOlpc;
	}

	public boolean isUseToeicOlpc() {
		return useToeicDB;
	}

	public java.util.logging.Logger getLogger() {
		return logger;
	}

	public boolean isLogProfiler() {
		return logProfiler;
	}

	/**
	 * @param logProfiler - use True in order to use sql profiler during the test
	 *                    and save the log to a file
	 */
	public void setLogProfiler(boolean logProfiler) {
		this.logProfiler = logProfiler;
	}

	public String getComponentIdByItemId(String itemId) throws Exception {
		String lessonId = getStringFromQuery(
				"select ComponentSubComponents.ComponentId from ComponentSubComponents inner join Item on Item.ComponentSubComponentId = ComponentSubComponents.ComponentSubComponentId where Item.ItemId = "
						+ itemId);
		return lessonId;
	}

	public String getGradebookCoulmnTypeIdByInstIdAndName(String instId, String Name) throws Exception {
		String TypeId = getStringFromQuery("select top 1 ID from GradeBookColumnTypes where InstitutionId = '" + instId
				+ "' and Name = '" + Name + "'");
		return TypeId;
	}

	public String getGradebookFirstCoulmnIdByClassCourseAndType(String classId, String courseId, String ColumnTypeId)
			throws Exception {
		// String ColumnId = getStringFromQuery("select top 1 ID from GradeBookColumns
		// where CourseId = " + courseId +" and classID = " + classId + " and
		// GradeBookColumnTypeId = " + ColumnTypeId);
		String ColumnId = getStringFromQuery("select top 1 ID from GradeBookColumns where CourseId = " + courseId
				+ " and GradeBookColumnTypeId = " + ColumnTypeId);

		return ColumnId;
	}

	public String getGradebookLastCoulmnIdByClassCourseAndType(String classId, String courseId, String ColumnTypeId)
			throws Exception {
		String ColumnId = getStringFromQuery("select top 1 ID from GradeBookColumns where CourseId = '" + courseId
				+ "' and GradeBookColumnTypeId = " + ColumnTypeId + " order by ID desc");
		return ColumnId;
	}

	public void deleteStudentgradebookGrades(String studentID) throws Exception {
		runDeleteUpdateSql("delete from GradeBookGrades where studentID = " + studentID);
	}

	public void deletegradebookColumns(String courseId, String classID) throws Exception {
		runDeleteUpdateSql("delete from GradeBookColumns where CourseId = " + courseId + " and classID = " + classID);
	}

	public void changeUserPassword(String studentId) throws SQLException {
		String sp = "exec SetUserPassword " + studentId + ",'12345'";
		runStorePrecedure(sp);
	}

	public void logOutUser(String studentId) throws SQLException {

		String sp = "exec logout " + studentId;
		runStorePrecedure(sp);

	}

	public String getLastUserLogOut(String studentId) throws Exception {

		String sql = "select top 1 LogOut from UserLog where UserId = " + studentId + " Order By UserLogId desc";
		return getStringFromQuery(sql);
	}

	public void changeUserCommunityLevel(String level, String studentId) throws SQLException {
		int levelId = 1; // default Basic

		switch (level) {
			case "Basic":
				levelId = 1;
				break;
			case "Intermediate":
				levelId = 2;
				break;
			case "Advanced":
				levelId = 3;
				break;
		}
		String sp = "Update Users SET LevelId =" + levelId + " Where UserId = " + studentId;
		runStorePrecedure(sp);

	}

	public void setInstitutionContactUsEMail(String institutionId, String email) throws Exception {

		String sql = "undefined";

		if (!email.equalsIgnoreCase("NULL")) {
			sql = "update Institutions set ContactUs = '" + email + "' where InstitutionId = " + institutionId;
		} else {
			sql = "update Institutions set ContactUs = " + email + " where InstitutionId = " + institutionId;
		}

		runDeleteUpdateSql(sql);

	}

	//--igb 2018.06.25---------------------------
	public void setUserOptIn(String studentId, boolean bState) throws Exception {

		String sp = null;

		if (bState == true)
			sp = "Update Users SET OptIn=1 where UserId=" + studentId;
		else
			sp = "Update Users SET OptIn=0 where UserId=" + studentId;

		runStorePrecedure(sp);
	}
//--igb 2018.06.25---------	

	public String getStudentTimeInCourse(String studentId, String edToLState, String CourseId) throws Exception {

		String sql = "";
		if (edToLState.equalsIgnoreCase("lesson"))
			sql = "GetTimeInCourseByLesson " + CourseId + ", " + studentId;
		else
			sql = "GetTimeInCourse " + CourseId + ", " + studentId;
		return getStringFromQuery(sql);
	}

	public String getOptinTeacher(String instID) throws Exception {

		String sql = " select userid from users where institutionid = " + instID + " AND UserName like '%optinTeacher%'";
		String teacherid = getStringFromQuery(sql, 1, true, MAX_DB_TIMEOUT);

		return teacherid;

	}

	public String getInstitutionPackageIDByPackageId(String instID, String packageId) throws Exception {

		String sql = "select top 1 InstitutionPackageid from InstitutionPackages where PackageId = " + packageId
				+ " and institutionid = " + instID + " order by InstitutionPackageId desc";
		return getStringFromQuery(sql);
	}

	public String getRemainingLicensesByPackageId(String instID, String packageId) throws Exception {

		String sql = "select top 1 Remaining from InstitutionPackages where PackageId = " + packageId
				+ " and institutionid = " + instID + " order by InstitutionPackageId desc";
		return getStringFromQuery(sql);
	}

	public String getUserSRid(String studentId) throws Exception {

		String rowCount = getStringFromQuery("SELECT count(*) FROM [UserSRRecords] where [StudentId]=" + studentId);

		if (rowCount.equals("0"))
			return "0";

		String recId = getStringFromQuery("SELECT [ID] FROM [UserSRRecords] where [StudentId]=" + studentId);

		return recId;
	}

	public String getExternalUserInternalId(String userName) throws Exception {

		String sql = "SELECT [UserId] FROM [UsersExternalMap] WHERE [ExternalUserName]='" + userName + "'";
		return getStringFromQuery(sql, 1, true, 2);
	}

	public String getExternalUserInternalId(String userName, String institutionId) throws Exception {

		String sql = "SELECT [UserId] FROM [UsersExternalMap] WHERE institutionId = " + institutionId
				+ " and [ExternalUserName]=\'" + userName + "\'";
		return getStringFromQuery(sql, 1, true, 5);
	}

	public void deleteExternalUserByUserName(String userName) throws Exception {

		String sql = "DELETE FROM [UsersExternalMap] WHERE [ExternalUserName]=\'" + userName + "\'";
		runDeleteUpdateSql(sql);
	}

	public void deleteExternalClassByName(String className) throws Exception {

		String sql = "DELETE FROM [ClassExternalMap] WHERE [ExternalClassName]=\'" + className + "\'";
		runDeleteUpdateSql(sql);
	}

	public void deleteUserIdFromClass(String userId) throws Exception {

		String sql = "DELETE FROM [ClassUsers] WHERE [UserId]=\'" + userId + "\'";
		runDeleteUpdateSql(sql);
	}

	public String getToeicDistributorTestCount(String distributorId, String db) throws Exception {

		String sql = "select count(*) from DistributorTestBank where DistributorId =" + distributorId;
		return getStringFromQuery(sql);
	}

	public String getCusstomUrl(String customUrl) {
		String sql = "select InstitutionHost from InstitutionGroups where InstitutionHost like '%" + customUrl + "%'";
		String url = null;
		try {
			url = getStringFromQuery(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return url;
	}

	public int getInstitutionFeatureFlag(String institutionId, String featureId) {
		// TODO Auto-generated method stub
		// String sql = "select value from InstitutionProperties where institutionid ="
		// + institutionId + " AND PropertyId =" + featureId;
		String sql = "Exec GetInstitutionPropertyValue " + institutionId + "," + featureId;

		String rs = "0";

		try {
			rs = getStringFromQuery(sql, 1, true, 1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (rs == null)
			rs = "0";

		return Integer.parseInt(rs);
	}

	public String getUnitIdByItemId(String courseId, String itemId) {
		String sql = "select u.unitid" + " from units as u" + " inner join UnitComponents as uc on u.unitid=uc.UnitId"
				+ " inner join Component as co on co.ComponentId = uc.ComponentId"
				+ " inner join ComponentSubComponents as csc on co.ComponentId = csc.ComponentId"
				+ " inner join Item  as i on i.ComponentSubComponentId = csc.ComponentSubComponentId"
				+ " inner join course as c on u.CourseId = c.CourseId" + " where i.ItemId  =" + itemId
				+ " and c.CourseId =" + courseId;

		String unitId = null;

		try {
			unitId = getStringFromQuery(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return unitId;
	}

	public List<String[]> getUserLastRecordOfTimeOnLesson(String courseId, String studentId) {
		String sql = "select top 1 TimeOnLessonId,StartServiceTimeStamp from TimeOnLesson " + " where CourseId ="
				+ courseId + " and userid =" + studentId + " " + " order by TimeOnLessonId desc";

		List<String[]> userTimeOnLesson = null;
		try {
			userTimeOnLesson = getStringListFromQuery(sql, 1, 2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return userTimeOnLesson;

	}

	public void updateStartTimeUserTimeOnLesson(String startTime, String timeOnLessonId, String studentId,
												String courseId) {
		String sql = "update TimeOnLesson set StartServiceTimeStamp = '" + startTime + "' where courseId=" + courseId
				+ " AND userId = " + studentId + " AND TimeOnLessonId =" + timeOnLessonId;
		try {
			runDeleteUpdateSql(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getPlanJson(String userId) {
		String sql = "select SchedulerPlanJson from UserSchedulerPlans where userid = " + userId; // 52322820285622

		String planJson = null;

		try {
			planJson = getStringFromQuery(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return planJson;
	}

	public void updateUserSchedulerPlanJson(String json, String userId) {
		String sql = "Update UserSchedulerPlans set SchedulerPlanJson = '" + json + "'  where userid = " + userId;

		try {
			runDeleteUpdateSql(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void updateGradeInFinalTestForSpecificUser(String studentId, String newGrade, String testId,
													  String impType) {
		String sql = "update UserExitTestSettingsLog set Grade = " + newGrade + " where UserId = " + studentId
				+ " AND TestId=" + testId + " and ImpTypeFeatureId =" + impType;
		try {
			runDeleteUpdateSql(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void updateDidTest(String studentId, String testId, String impType, String didTest) {
		String sql = "update UserExitTestSettings set DidTest = " + didTest + " where UserId = " + studentId
				+ " AND TestId=" + testId + " and ImpTypeFeatureId =" + impType;
		try {
			runDeleteUpdateSql(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void updateCompletedTest(String studentId, String testId, String impType, String completed) {
		String sql = "update UserExitTestSettingsLog set CTCompleted = " + completed + " where UserId = " + studentId
				+ " AND TestId=" + testId + " and ImpTypeFeatureId =" + impType;
		try {
			runDeleteUpdateSql(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getPlanIdOfUser(String studentId) {
		String sql = "select SchedulerPlanId from UserSchedulerPlans where userid =" + studentId;

		String planId = null;

		try {
			planId = getStringFromQuery(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return planId;
	}

	public void moveUserToClass(String institutionId, String userName, String className) throws SQLException {
		try {
			String sp = "exec dbo.APIMoveUser " + institutionId + ",'" + userName + "','" + className + "' ";
			runStorePrecedure(sp);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void updateTimeOfOnlineSession(String onlineSessionTitle, String updatedStartDate, String updatedEndDate) {
		String sql = "update OnlineSessions set StartDate = '" + updatedStartDate + "' , EndDate = '" + updatedEndDate
				+ "' where Title = '" + onlineSessionTitle + "'";
		try {
			runDeleteUpdateSql(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getInitialPLTLevel(String userId) {
		String sql = "Select top 1 initialLevel from PlacementTestGrades where userid= " + userId
				+ " Order by PlacementTestGradesId desc";
		String initialPltLevel = "";
		try {
			initialPltLevel = getStringFromQuery(sql);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return initialPltLevel;

	}

	public void unauthorizeCourseToClass(String classId, String courseSequence) throws Exception {
		String SP = "UnauthorizeCoursesToClass " + classId + ", ' " + courseSequence + "',0";
		runStorePrecedure(SP, true, true);
	}

	public String getUserReflectionId(String userId) {
		String sql = "SELECT top 1 UnitReflectionId FROM UnitReflections WHERE UserId = " + userId
				+ " ORDER BY CreateDateUTC desc";
		String reflectionId = "";
		try {
			reflectionId = getStringFromQuery(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return reflectionId;
	}

	public List<String> getSpecificColumnFromUserReflection(String userId, String reflectionId, String columnName)
			throws Exception {
		String sql = "SELECT UnitReflections." + columnName + " "
				+ "FROM UnitReflections INNER JOIN LessonReflections ON UnitReflections.UnitReflectionId = LessonReflections.UnitReflectionId "
				+ "WHERE (UnitReflections.UserId =" + userId + " and UnitReflections.UnitReflectionId=" + reflectionId
				+ ")";

		List<String> userReflectionData = new ArrayList<String>();
		try {
			userReflectionData = getArrayListFromQuery(sql, 1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return userReflectionData;
	}

	public List<String[]> getUserReflectionData(String userId, String reflectionId) throws Exception {
		String sql = "SELECT UnitReflections.UnitId, UnitReflections.UnitProgress, UnitReflections.TimeOnUnitSeconds, UnitReflections.ComponentTestAverage, LessonReflections.LessonId,LessonReflections.Score, UnitReflections.CreateDateUTC "
				+ "FROM UnitReflections INNER JOIN LessonReflections ON UnitReflections.UnitReflectionId = LessonReflections.UnitReflectionId "
				+ "WHERE (UnitReflections.UserId =" + userId + " and UnitReflections.UnitReflectionId=" + reflectionId
				+ ")";

		List<String[]> userReflectionData = new ArrayList<String[]>();
		try {
			userReflectionData = getStringListFromQuery(sql, 0, 7);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return userReflectionData;
	}

	public List<String[]> getUserUnitProgress(String userId, String unitId) throws Exception {
		String sql = "select * from UserUnitProgress where userid = " + userId + " and UnitId = " + unitId;
		List<String[]> unitProgress = new ArrayList<String[]>();
		try {
			unitProgress = getStringListFromQuery(sql, 0, 5);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return unitProgress;
	}

	public List<String[]> getUserUnitProgressAndTestAverage(String userId, String courseId) throws Exception {
		String sql = "SELECT ISNULL(progress,0) as Progress,ISNULL(TestAverage,0) as TestAverage,UnitId"
				+ " from UserUnitProgress where userid = "
				+ userId + " and UnitId in " + " (select UnitId from units where courseId = " + courseId + ")";
		List<String[]> unitProgress = new ArrayList<String[]>();
		try {
			unitProgress = getStringListFromQuery(sql, 0, false);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return unitProgress;
	}

	public List<String> getLessonsIdsBySequnce(String unitId) throws Exception {
		String sql = "SELECT Component.ComponentId, UnitComponents.Sequence "
				+ "FROM Component INNER JOIN UnitComponents ON Component.ComponentId = UnitComponents.ComponentId "
				+ "INNER JOIN Units ON UnitComponents.UnitId = Units.UnitId " + "WHERE (UnitComponents.UnitId = "
				+ unitId + ") order by  UnitComponents.Sequence";
		List<String> lessonsIds = new ArrayList<String>();
		try {
			lessonsIds = getArrayListFromQuery(sql, 1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lessonsIds;
	}

	public void turnOnTestEnviormentFlag(String instId) throws Exception {
		// get todays date
		PageHelperService pageHelper = new PageHelperService();
		TextService textService = new TextService();
		String currentDate = pageHelper.getCurrentDateByFormat("yyyy-MM-dd HH:MM:ss");
		currentDate = textService.updateTime(currentDate, "reduce", "day", 1);
		currentDate = currentDate.replace(" ", "T");
		String sql = "INSERT INTO [dbo].[InstitutionProperties] ([InstitutionId], [PropertyId], [Value]) " + "VALUES ("
				+ instId + ", 35, '" + currentDate + "')";
		try {
			runDeleteUpdateSql(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void checkAndturnOnTestEnviormentFlag(String instId) throws Exception {
		// get todays date
		PageHelperService pageHelper = new PageHelperService();
		TextService textService = new TextService();
		String currentDate = pageHelper.getCurrentDateByFormat("yyyy-MM-dd HH:MM:ss");
		currentDate = textService.updateTime(currentDate, "reduce", "day", 1);
		currentDate = currentDate.replace(" ", "T");

		// String sql = "INSERT INTO [dbo].[InstitutionProperties] ([InstitutionId],
		// [PropertyId], [Value]) " + "VALUES ("+instId+", 35, '"+currentDate+"')";

		String sql = "IF NOT EXISTS (select institutionid from InstitutionProperties where institutionid =" + instId
				+ " and PropertyId = 35)" + " Begin"
				+ " insert into InstitutionProperties (InstitutionId,PropertyId,Value)" + " Values (" + instId + ",35,'"
				+ currentDate + "')" + " End";

		try {
			runDeleteUpdateSql(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void checkAndturnOffTestEnviormentFlag(String instId) throws Exception {

		String sql = "IF EXISTS (select institutionid from InstitutionProperties where institutionid =" + instId
				+ " and PropertyId = 35)" + " Begin" + " delete from [InstitutionProperties] where [InstitutionId] = "
				+ instId + " and [propertyId] = 35" + " End";

		try {
			runDeleteUpdateSql(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void deletePropertyFlagforInstitution(String instId, String propertyId) throws Exception {

		String sql = "IF EXISTS (select institutionid from InstitutionProperties where institutionid =" + instId
				+ " and PropertyId = " + propertyId + ")" + " Begin"
				+ " delete from [InstitutionProperties] where [InstitutionId] = " + instId + " and [propertyId] = "
				+ propertyId + "" + " End";

		try {
			runDeleteUpdateSql(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void turnOffTestEnviormentFlag(String instId) {
		String sql = "delete from [InstitutionProperties] where [InstitutionId] = " + instId + " and [propertyId] = 35";
		try {
			runDeleteUpdateSql(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void checkAndturnOnPropertyIdFlag(String instId, String propertyId, String value) throws Exception {

		// String sql = "INSERT INTO [dbo].[InstitutionProperties] ([InstitutionId],
		// [PropertyId], [Value]) " + "VALUES ("+instId+", 35, '"+currentDate+"')";

		String sql = "IF NOT EXISTS (select institutionid from InstitutionProperties where institutionid =" + instId
				+ " and PropertyId = " + propertyId + ")" + " Begin"
				+ " insert into InstitutionProperties (InstitutionId,PropertyId,Value)" + " Values (" + instId + ","
				+ propertyId + "," + value + ")" + " End" + " ELSE" + " Update InstitutionProperties SET Value ="
				+ value + " Where institutionId = " + instId + " AND PropertyId =" + propertyId;

		try {
			runDeleteUpdateSql(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String getUserExitSettingsId(String userId, String testId, String courseId, String impTypeFeatureId) {
		String sql = "select UserExitTestSettingsid from UserExitTestSettings where userId = " + userId
				+ " and testId = " + testId + " and courseId = " + courseId + " and ImpTypeFeatureId = "
				+ impTypeFeatureId;
		String UserExitTestSettingsid = "";
		try {
			UserExitTestSettingsid = getStringFromQuery(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return UserExitTestSettingsid;
	}

	public void updateAssignWithToken(String token, String userExitTestSttingsId) {
		String sql = "update UserExitTestSettings set usertesttoken = '" + token + "' where UserExitTestSettingsId = '"
				+ userExitTestSttingsId + "'";
		try {
			runDeleteUpdateSql(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<String[]> getTestResultOfPLT_API_test(String userId) {
		String sql = "select top 1 * from PlacementTestGrades where userid = " + userId
				+ " and startdate >= CONVERT(date, getdate()) order by Date desc";
		List<String[]> testResults = new ArrayList<String[]>();
		try {
			int i = 0;
			while (testResults.isEmpty() && i < 5) {
				testResults = getStringListFromQuery(sql, 0, false);
				i++;
				sleep(1);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return testResults;
	}

	public List<String[]> getTestResultOfPLT_API_test_Grades(String userId) {
		// String sql = "select * from TestResults where userid = "+userId+" and
		// CourseId = 11 and LastUpdate >= CONVERT(date, getdate())";

		String sql = "select tr.* from TestResults as tr "
				+ " inner join ComponentSubComponents as csc on tr.ComponentSubComponentId = csc.ComponentSubComponentId "
				+ " inner join Component as comp on comp.ComponentId = csc.ComponentId" + " where tr.UserId = " + userId
				+ " and tr.CourseId = 11 and tr.LastUpdate >= CONVERT(date, getdate()) order by tr.LastUpdate";

		List<String[]> testResults = new ArrayList<String[]>();
		int i = 0;
		try {
			while (testResults.isEmpty() && i < 5) {
				testResults = getStringListFromQuery(sql, 1, 5);
				i++;
				sleep(1);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return testResults;
	}

	public List<String[]> getLessonCountForAllUnitsInCourse(String testId) {
		String sql = "SELECT Units.unitid, COUNT (Component.ComponentId) as CompCount FROM course "
				+ "inner join Units Units ON Course.CourseId = Units.CourseId "
				+ "inner join unitcomponents ON Units.Unitid = Unitcomponents.Unitid "
				+ "inner join Component On UnitComponents.ComponentId = Component.ComponentId "
				+ "WHERE course.[CourseId] = " + testId + " GROUP BY Units.unitid";

		List<String[]> lessonCountForUnits = new ArrayList<String[]>();
		try {
			lessonCountForUnits = getStringListFromQuery(sql, 1, 2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lessonCountForUnits;
	}

	public void updateTestBank(String active, String testId, String testIdBank) {
		String sql = "update TestBank set active = " + active + " where testid = " + testId + " and TestBankId = "
				+ testIdBank;
		try {
			runDeleteUpdateSql(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getTokenOfStudentWithAssignedTest(String userExitTestSettingsId, String userId) {
		String sql = "select UserTestToken from UserExitTestSettings where UserExitTestSettingsId = "
				+ userExitTestSettingsId + " and userId ='" + userId + "'";
		String token = "";
		try {
			token = getStringFromQuery(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return token;
	}

	public String getTestLengthByInstitutionName(String instName) {
		String sql = "select CourseTestLen from institutions where Visible = 1 and name = '" + instName + "'";
		String testLength = "";
		try {
			testLength = getStringFromQuery(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return testLength;
	}

	public String getCourseIdByNameAndVersion(String courseName, String version) throws Exception {
		String sql = "select CourseId from course where name='" + courseName + "' and version ='" + version + "'";
		return getStringFromQuery(sql);

	}

	public List<String[]> getUserExitTestSettings(String userId, int impTypeFeatureId) {
		String sql = "select top 1 UserExitTestSettingsid, testId, UserTestToken from UserExitTestSettings where userId = "
				+ userId + " and ImpTypeFeatureId = " + impTypeFeatureId + " order by UserExitTestSettingsid desc";
		List<String[]> UserExitTestSettings = null;
		try {
			UserExitTestSettings = getStringListFromQuery(sql, 1, 3);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return UserExitTestSettings;
	}

	public void deleteExitTestForUserIdAndTestType(String userId, int testType) throws Exception {
		String sql = "delete from UserExitTestSettings where UserId = " + userId + "and ImpTypeFeatureId = " + testType;
		runDeleteUpdateSql(sql);
	}

	public void updateUserStartedUTC(String studentId, String testId, String impType, String userStartedUTC) {
		String sql = "update UserExitTestSettings set UserStartedUTC = '" + userStartedUTC + "' where UserId = "
				+ studentId + " AND TestId=" + testId + " and ImpTypeFeatureId =" + impType;
		try {
			runDeleteUpdateSql(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void updateStartAndEndDateInUserExitTestSetting(String studentId, String testId, String impType,
														   String startDate, String endDate) {
		String sql = "update UserExitTestSettings set StartDate = '" + startDate + "' , StartDateUTC = '" + startDate
				+ "' , EndDate = '" + endDate + "' , EnddateUTC = '" + endDate + "' where UserId = " + studentId
				+ " AND TestId=" + testId + " and ImpTypeFeatureId =" + impType;
		try {
			runDeleteUpdateSql(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void insertRecordToUserExistTestSettingLog(String UserId, String CourseId, String TestId, String TestDate,
													  String ImpTypeFeatureId, String Grade, String CTCompleted, String LastVisitedItemId) throws Exception {
		String query = "insert into UserExitTestSettingsLog (UserId, CourseId, TestId, TestDate, ImpTypeFeatureId, Grade, CTCompleted, LastVisitedItemId) values "
				+ "('" + UserId + "', '" + CourseId + "', '" + TestId + "', '" + TestDate + "', '" + ImpTypeFeatureId
				+ "', '" + Grade + "', '" + CTCompleted + "', " + LastVisitedItemId + ");";
		try {
			runDeleteUpdateSql(query);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getInstitutionTimeOfCourseTestEndDate(String institutionId) {

		String sql = "select TimeForCourseTest from institutions where InstitutionId =" + institutionId;
		String timeOfCourseTest = "";

		try {
			timeOfCourseTest = getStringFromQuery(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return Integer.parseInt(timeOfCourseTest);
	}

	public void updateTestIdInUserExitSettings(String testId, String userExitTestSttingsId) {
		String sql = "update UserExitTestSettings set testid = '" + testId + "' where UserExitTestSettingsId = "
				+ userExitTestSttingsId;
		try {
			runDeleteUpdateSql(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void updateTestResultGrade(String newGrade, String userId) {
		String sql = "update TestResults set grade = " + newGrade
				+ " where testresultsid = (select top 1 testresultsid from TestResults where userid = " + userId
				+ " and courseid = 11 order by lastupdate desc)";
		try {
			runDeleteUpdateSql(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getImpTypeByTestId(String testId) {
		String sql = "select top 1 ImpTypeFeatureId from testBank where active = 1 and testId = '" + testId + "'";
		String impType = "";
		try {
			impType = getStringFromQuery(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return impType;
	}

	public List<String[]> getUserTestComponentProgressByUserId(String userId) {
		String sql = "select * from UserTestComponentProgress where userid = " + userId
				+ " order by clientenddatetime desc";
		List<String[]> UserTestComponentProgress = null;
		try {
			UserTestComponentProgress = getStringListFromQuery(sql, 1, 23);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return UserTestComponentProgress;
	}

	public List<String[]> getUserTestUnitProgressByUserId(String userId) {
		String sql = "select * from usertestunitprogress where userid = " + userId;
		List<String[]> UserTestUnitProgress = null;
		try {
			UserTestUnitProgress = getStringListFromQuery(sql, 1, 16);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return UserTestUnitProgress;
	}

	public List<String[]> getUserTestProgressByUserId(String userId) {
		String sql = "select top 1 * from usertestprogress where userid = " + userId
				+ " order by serverStartDateTime desc";

		List<String[]> UserTestUnitProgress = null;
		try {
			UserTestUnitProgress = getStringListFromQuery(sql, 1, false);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return UserTestUnitProgress;
	}

	// old te
	public List<String[]> getUserTestResultsByUserIdAndTestIdOldTe(String userId, String testId) {
		String sql = "select distinct tb.TestId, tr.*,Skill.Name,com.Code " + "from TestResults as tr "
				+ "inner join ComponentSubComponents as csc on tr.ComponentSubComponentId = csc.ComponentSubComponentId "
				+ "inner join Component as com on com.ComponentId = csc.ComponentId "
				+ "inner join Skill on skill.SkillId = com.SkillId "
				+ "inner join testbank as tb on tb.TestId = tr.CourseId " + "where tr.UserId = '" + userId
				+ "' and tb.TestId = '" + testId + "' order by tr.TestResultsId,tr.LastUpdate";

		List<String[]> UserTestResults = null;
		try {
			UserTestResults = getStringListFromQuery(sql, 1, 14);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return UserTestResults;
	}

	public String getDidTestByUserExitTestSettingsId(String userExitTestSettingsId) {
		String sql = "select didTest from UserExitTestSettings where userExitTestSettingsId = '"
				+ userExitTestSettingsId + "'";
		String didTest = "";
		try {
			didTest = getStringFromQuery(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return didTest;
	}

	public String getTimeOnLessonRecords(String studentId) {
		// TODO Auto-generated method stub
		String sql = "Select count(TimeOnLessonId) from TimeOnLesson where userId = " + studentId;

		String rowCount = null;

		try {
			rowCount = getStringFromQuery(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		return rowCount;
	}

	// new te
	public List<String[]> getUserTestResultsByUserIdAndTestIdNewTe(String userId, String userTestAdministrationId) {
		String sql = "select utcp.TestId,utcp.UserTestComponentProgressId,utcp.UserTestAdministrationId,utcp.ComponentId,utcp.UserId,utcp.UnitId,utcp.ComponentPercentageScore "
				+ ",Component.Code,Skill.Name,utcp.ServerStartDateTime " + "from UserTestComponentProgress as utcp "
				+ "inner join Component on utcp.ComponentId = Component.ComponentId "
				+ "inner join Skill on Component.SkillId = Skill.SkillId " + "where utcp.UserTestAdministrationId = "
				+ userTestAdministrationId + " and utcp.UserId = " + userId + " " + "order by utcp.ServerStartDateTime";

		List<String[]> UserTestResults = null;
		try {
			UserTestResults = getStringListFromQuery(sql, 1, 10);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return UserTestResults;
	}

	public List<String[]> getUserDetailsByScoreAndCourseIdAndInstIdnewTE(String score, String courseId, String instId) {
		String sql = "SELECT top 1 u.UserId,u.LastName,u.FirstName,u.UserName " + "FROM UserTestProgress as utp "
				+ "INNER JOIN users AS u ON u.userid = utp.UserId "
				+ "INNER JOIN institutions AS i ON i.InstitutionId = u.InstitutionId "
				+ "INNER JOIN UserExitTestSettings AS uets ON uets.UserExitTestSettingsId = utp.UserTestAdministrationId "
				+ "WHERE utp.TestGradeForDisplay = " + score + " and uets.CourseId = " + courseId
				+ " and i.InstitutionId = " + instId + " " + "ORDER BY utp.UserTestAdministrationId DESC";

		List<String[]> UserDetails = null;
		try {
			UserDetails = getStringListFromQuery(sql, 1, 4);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return UserDetails;
	}

	public void updateUserLastName(String currentLastName, String newLastName) {
		String sql = "update users set lastname='" + newLastName + "' where lastName = '" + currentLastName + "'";
		try {
			runDeleteUpdateSql(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void updateUserLastNameByUserId(String userId, String lasttName) {
		String sql = "update users set LastName = '" + lasttName + "' where Userid = " + userId;
		try {
			runDeleteUpdateSql(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<String[]> getUserDetailsByScoreAndCourseIdAndInstIdOldTE(String score, String courseId, String instId) {
		String sql = "SELECT top 1 u.UserId,u.LastName,u.FirstName,u.UserName "
				+ "FROM UserExitTestSettingsLog as uetsl " + "INNER JOIN users AS u ON u.userid = uetsl.UserId "
				+ "INNER JOIN institutions AS i ON i.InstitutionId = u.InstitutionId " + "WHERE uetsl.Grade >= " + score
				+ " and uetsl.CourseId = " + courseId + " and i.InstitutionId = " + instId + " "
				+ "ORDER BY uetsl.TestDate DESC";

		List<String[]> UserDetails = null;
		try {
			UserDetails = getStringListFromQuery(sql, 1, 4, true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return UserDetails;
	}

	public List<String[]> getUserInClassWithProgress(String instId) {

		String sql = "select distinct top 1 users.userid,units.CourseId,units.UnitName,Classusers.classId,"
				+ " UserUnitProgress.TestAverage,UserUnitProgress.Progress,UserUnitProgress.UnitId" + " from users "
				+ " join UserUnitProgress on UserUnitProgress.UserId = users.UserId"
				+ " join units on units.UnitId = UserUnitProgress.UnitId"
				+ " join classusers on ClassUsers.UserId = users.UserId"
				+ " join Course on Units.CourseId = Course.CourseId" + " where InstitutionId = " + instId
				+ " and visible = 1" + " and UserUnitProgress.TestAverage > 0" + " and UserUnitProgress.Progress > 0";

		List<String[]> UserDetails = null;
		try {
			UserDetails = getStringListFromQuery(sql, 1, 7);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return UserDetails;
	}

	public String getUserAddressByUserId(String studentId) {
		// TODO Auto-generated method stub
		String sql = "select Address from UserAddress where userid= " + studentId;

		String address = "";
		try {
			address = getStringFromQuery(sql, 1, true, 2);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return address;
	}

	public String[] getUserAddressByInstitutionId(String institutionId, String studentId) {
		String sql = "select top 10 users.userid,UserAddress.Address from UserAddress"
				+ " inner join users on users.userid =  UserAddress.userid"
				+ " where Address is not null and users.InstitutionId = " + institutionId + " and users.UserId <> "
				+ studentId;
		List<String[]> userAddresses = null;
		int value = 0;

		try {
			userAddresses = getStringListFromQuery(sql, 1, 2);

			Random rand = new Random();
			value = rand.nextInt(userAddresses.size());

			// userAddresses = userAddresses.get(value);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return userAddresses.get(value);
	}

	public String getUserCountryIdbyUserId(String studentId) {
		String sql = "Select CountryId from useraddress where userId =" + studentId;
		String countryId = "";
		try {
			countryId = getStringFromQuery(sql, 1, true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return countryId;
	}

	public String getInstitutionCountryId(String institutionId) {
		String sql = "Select CountryId from institutionAddress where institutionId =" + institutionId;
		String countryId = "";
		try {
			countryId = getStringFromQuery(sql, 1, true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return countryId;
	}

	public List<String[]> getUserClassIdAndNamebyUserId(String studentId) throws Exception {

		String sql = "select c.classId,c.name from ClassUsers as cu"
				+ " inner join class as c on c.classid = cu.classid" + " where cu.UserId=" + studentId;
		System.out.println(sql);

		List<String[]> result = getStringListFromQuery(sql, 1, true);

		return result;

	}

	public String getUserTestAdministrationByStudentId(String studentId) {
		String sql = "select TOP 1 userTestAdministrationId from UserTestAdministrations where userId =" + studentId
				+ " Order by UpdatedDateTime DESC";
		String userTestAdministration = "";
		try {
			userTestAdministration = getStringFromQuery(sql, 1, true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return userTestAdministration;

	}

	public List<String[]> getUserTestAdministrationsByStudentId(String studentId) {
		String sql = "select userTestAdministrationId,CourseId,TestId,TestDurationMinutes from UserTestAdministrations where userId ="
				+ studentId;
		List<String[]> userTestAdministration = null;
		try {
			userTestAdministration = getStringListFromQuery(sql, 1, 4);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return userTestAdministration;
	}

	public void updateAssignPltWithToken(String token, String userTestAdministrationId) {
		String sql = "update UserTestAdministrations set usertesttoken = '" + token
				+ "' where usertestadministrationid = '" + userTestAdministrationId + "'";
		try {
			runDeleteUpdateSql(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getTestLengthByTestTypeId(String testTypeId) {
		String sql = "Select TestDurationMinutes from testTypeSettings where TestTypeId = " + testTypeId;
		String testLen = "";

		try {
			testLen = getStringFromQuery(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return testLen;
	}

	public void updateCompletedInUserTestProgress(String userTestAdministrationId, String isCompleted) {
		String sql = "update usertestprogress set iscompleted = '" + isCompleted + "' where userTestAdministrationId='"
				+ userTestAdministrationId + "'";
		try {
			runDeleteUpdateSql(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String[] getUserFinalPltResultByInstId(String institutionId) {
		String sql = "select top 1 ptg.PlacementTestGradesId,ptg.UserId,u.FirstName,u.LastName, ptg.Reading,ptg.Listening,ptg.Grammar,ptg.Final,c.Name,ptg.StartDate"
				+ " from PlacementTestGrades as ptg" + " inner join  users as u on u.UserId = ptg.UserId"
				+ " inner join Institutions as i on i.InstitutionId = u.InstitutionId"
				+ " inner join classusers as cu on cu.UserId = u.UserId"
				+ " inner join class as c on c.classId = cu.ClassId" + " where i.InstitutionId = " + institutionId + ""
				+ " and ptg.PLTCompleted=1" + " and u.visible = 1" + " order by ptg.StartDate desc";

		List<String[]> userPltResult = null;
		String[] row = null;
		try {
			userPltResult = getStringListFromQuery(sql, 1, false);

			row = userPltResult.get(0);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return row;
	}

	public String[] getUserFinalPltResultByInstIdNewTE(String institutionId) {
		/*
		 * String sql1 =
		 * "select top 1 utp.UserTestAdministrationId,utp.UserId,u.FirstName,u.LastName,utps.skillId"
		 * + " ,utps.recommendedLevel,utp.LearningStartLevel" +
		 * " ,c.Name,utp.ServerEndDateTime" + " from UserTestProgress utp" +
		 * " inner join UserTestProgressBySkill as utps on utp.UserTestAdministrationId = utps.UserTestAdministrationId"
		 * + " inner join Skill on skill.SkillId = utps.skillId" +
		 * " inner join  users as u on u.UserId = utp.UserId" +
		 * " inner join Institutions as i on i.InstitutionId = u.InstitutionId" +
		 * " inner join classusers as cu on cu.UserId = u.UserId" +
		 * " inner join class as c on c.classId = cu.ClassId" + " where utp.TestId = 11"
		 * + " and utp.IsCompleted = 1" + " and i.InstitutionId = " + institutionId + ""
		 * + " order by utp.ServerEndDateTime desc";
		 */
		String sql = "select top 1 utp.UserTestAdministrationId,utp.UserId,u.FirstName,u.LastName" + ", utps.Reading"
				+ " , utps.Listening" + " , utps.Grammar" + " ,utp.LearningStartLevel"
				+ " ,c.Name as ClasName,utp.ServerEndDateTime" + " from UserTestProgress utp" + " inner join" + "	("
				+ " select UserTestAdministrationId,  recommendedLevel," + " CASE" + " WHEN skillId =2 THEN 'Reading'"
				+ " WHEN skillId =3 THEN 'Listening'" + " WHEN skillId =6 THEN 'Grammar'" + " END AS Skill"
				+ " from UserTestProgressBySkill" + " ) src" + " PIVOT" + " (" + " MAX(recommendedLevel)"
				+ " for Skill in ([Reading], [Listening], [Grammar])"
				+ " ) utps on utp.UserTestAdministrationId = utps.UserTestAdministrationId"
				+ " inner join  users as u on u.UserId = utp.UserId"
				+ " inner join Institutions as i on i.InstitutionId = u.InstitutionId"
				+ " inner join classusers as cu on cu.UserId = u.UserId"
				+ " inner join class as c on c.classId = cu.ClassId" + " where utp.TestId = 11"
				+ " and utp.IsCompleted = 1" + " and i.InstitutionId = " + institutionId + ""
				+ " order by utp.ServerEndDateTime desc";
		/*
		 * String sql =
		 * "select top 1 utp.UserTestAdministrationId,utp.UserId,u.FirstName,u.LastName, utps.Reading, utps.Listening, utps.Grammar ,utp.LearningStartLevel ,c.Name as ClasName,utp.ServerEndDateTime "
		 * + " from UserTestProgress utp " +
		 * " inner join ( select UserTestAdministrationId,  recommendedLevel, CASE WHEN skillId =2 THEN 'Reading' WHEN skillId =3 THEN 'Listening' WHEN skillId =6 THEN 'Grammar' END AS Skill "
		 * +
		 * " from UserTestProgressBySkill) src PIVOT ( MAX(recommendedLevel) for Skill in ([Reading], [Listening], [Grammar]) ) utps on utp.UserTestAdministrationId = utps.UserTestAdministrationId "
		 * + " inner join  users as u on u.UserId = utp.UserId " +
		 * " inner join Institutions as i on i.InstitutionId = u.InstitutionId " +
		 * " inner join classusers as cu on cu.UserId = u.UserId " +
		 * " inner join class as c on c.classId = cu.ClassId where utp.TestId = 11 and utp.IsCompleted = 1 and i.InstitutionId = "
		 * +institutionId+" " + " order by utp.ServerEndDateTime desc";
		 */
		List<String[]> userPltResult = null;
		String[] row = null;
		try {
			userPltResult = getStringListFromQuery(sql, 1, true);

			row = userPltResult.get(0);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return row;
	}

	// test status of mid term/final
	public String getTestStatusByUserExitTestSettingsId(String userExitTestSettingsId) {
		String sql = "select testStatus from UserTests_v where UserExitTestSettingsId='" + userExitTestSettingsId + "'";
		String testStatus = "";

		try {
			testStatus = getStringFromQuery(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return testStatus;
	}

	public void updateEndDateInUserExitTestSetting(String userExitTestSettingsId, String endDate, String endDateUTC) {
		String sql = "update UserExitTestSettings set EndDate = '" + endDate + "' , EnddateUTC = '" + endDateUTC
				+ "' where userExitTestSettingsId = " + userExitTestSettingsId;
		try {
			runDeleteUpdateSql(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * public String getTestStatusByUserAndTestId(String userId, String testId) {
	 * String sql =
	 * "select testStatus from UserTests_v where userid='"+userId+"' and testId='"
	 * +testId+"'"; String testStatus ="";
	 *
	 * try { testStatus = getStringFromQuery(sql); } catch (Exception e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } return testStatus; }
	 */

	public String getTestDurationByUserExitSettingsId(String userExitTestSettingsId) {
		String sql = "select minutes from UserExitTestSettings where userexittestsettingsid = "
				+ userExitTestSettingsId;
		String minutes = "";
		try {
			minutes = getStringFromQuery(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return minutes;
	}

	public void updateEndDateInUserTestAdministrations(String userTestAdministrationId, String endDate,
													   String endDateUTC) {
		String sql = "update UserTestAdministrations set EndClientDateTime = '" + endDate + "' , EndUtcdateTime = '"
				+ endDateUTC + "' where userTestAdministrationId = " + userTestAdministrationId;
		try {
			runDeleteUpdateSql(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getTestStatusOfPLTByUserId(String userId) {
		String sql = "select teststatusid from UserTestAdministrations where userid=" + userId
				+ " order by endClientDateTime desc";
		String testStatus = "";

		try {
			testStatus = getStringFromQuery(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return testStatus;
	}

	public void insertUserToAutomationTable(String instId, String userId, String studentUserName, String className,
											String BuildId) {
		String sql = "";
		try {
			sql = "insert into AutomationCreatedUser (InstitutionId,UserId,UserName,Class,Active,RegistrationDate,BuildId) Values ("
					+ instId + " , " + userId + ", '" + studentUserName + "' ,'" + className + "' ,1, GETDATE(),'"
					+ BuildId + "')";
			runDeleteUpdateSql(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String checkNoDuplicateTestAssignmentInDB(String studentId, String courseId, int impType) {

		String sql = "select count(*)" + " from " + " UserExitTestSettings as uets "
				+ " join users as u on u.UserId = uets.UserId "
				+ " join institutions as i on i.InstitutionId = u.InstitutionId " + " where u.UserId=" + studentId
				+ " AND CourseId=" + courseId + " AND ImpTypeFeatureId=" + impType + " ";
		/*
		 * AND TestId="+testId+" + " group by " +
		 * " i.name,uets.UserId,u.UserName,uets.CourseId,TestId,ImpTypeFeatureId " +
		 * " having " + " count(*) >0 " + " order by i.Name, UserId";
		 */
		// System.out.print(sql);
		String testcount = "";

		try {
			// List<String[]> rows = getStringListFromQuery(sql,1);
			// testcount = rows.get(0)[0];
			testcount = getStringFromQuery(sql, 1, true);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return testcount;
	}

	public void SetPLTTestTime(String userId, int minutes) throws Exception {
		String sp = null;
		sp = "Update UserTestAdministrations Set TestDurationMinutes =" + minutes + " where userId= " + userId;
		runStorePrecedure(sp);
	}

	public String getTestLengthByInstitution(String instId, String testTypeId) {
		String sql = "Select TestDurationMinutes from institutionTestSettings where institutionId = " + instId
				+ " and testtypeid= " + testTypeId;
		String testLen = "";

		try {
			testLen = getStringFromQuery(sql, 1, true, 10);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return testLen;
	}

	public String getDaysByInstitution(String instId, String testTypeId) {
		String sql = "Select TestAvailabilityDays from institutionTestSettings where institutionId = " + instId
				+ " and testtypeid= " + testTypeId;
		String days = "";

		try {
			days = getStringFromQuery(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return days;
	}

	public List<String> getActiveTestsByCourseIdAndTeVersion(String courseId, String teVersion, int ImpTypeFeatureId) {
		String sql = "select testid from testbank where courseid=" + courseId + " and active=1 AND ImpTypeFeatureId ="
				+ ImpTypeFeatureId + " and testenvironmentversion = " + teVersion;

		List<String> testsIds = null;
		try {
			testsIds = getArrayListFromQuery(sql, 1);// getStringListFromQuery(sql, 1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return testsIds;
	}

	public void updateSubmitReasonInUserTestProgress(String userTestAdministrationId, String submitReasonId) {
		String sql = "update usertestprogress set submitReasonId = '" + submitReasonId
				+ "' where userTestAdministrationId='" + userTestAdministrationId + "'";
		try {
			runDeleteUpdateSql(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void updateLearningStartLevelInUserTestProgress(String userTestAdministrationId, String learningStartLevel) {
		String sql = "update usertestprogress set LearningStartLevel = '" + learningStartLevel
				+ "' where userTestAdministrationId='" + userTestAdministrationId + "'";
		try {
			runDeleteUpdateSql(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<String> getResumeComponentRecords(String userTestComponentProgressId) {
		String sql = "select * from UserTestComponentResume where userTestComponentProgressId = "
				+ userTestComponentProgressId;
		List<String> resumeRecords = null;
		try {
			resumeRecords = getArrayListFromQuery(sql, 1);// getStringListFromQuery(sql, 1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resumeRecords;
	}

	public List<String> getResumeUnitsRecords(String userTestUnitProgressId) {
		String sql = "select * from UserTestUnitIntroResume where userTestUnitProgressId = " + userTestUnitProgressId;
		List<String> resumeRecords = null;
		try {
			resumeRecords = getArrayListFromQuery(sql, 1);// getStringListFromQuery(sql, 1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resumeRecords;
	}

	public void updateTestStatusByUserExitTestSettingsId(String userExitTestSettingsId, String wantedTestStatus) {
		String sql = "update UserTests_v set testStatus = '" + wantedTestStatus + "' where UserExitTestSettingsId='"
				+ userExitTestSettingsId + "'";
		try {
			runDeleteUpdateSql(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<String[]> getStringListFromQuery(String sql, int intervals, int columns, boolean allowNull)
			throws Exception {
		List<String[]> list = new ArrayList<String[]>();

		StringBuilder sb = new StringBuilder();
		sb.append("Query is: ");
		sb.append(sql);
		sb.append(". Max db time out is: ");
		sb.append(MAX_DB_TIMEOUT);
		report.report(sb.toString());

		// report.report("Query is: " + sql + ". Max db time out is: " +
		// MAX_DB_TIMEOUT);
		ResultSet rs = null;
		Statement statement = null;
		// String str = null;
		boolean isEmpty = false;
		int elapsedTimeInSec = 0;

		try {
			Class.forName(SQL_SERVER_DRIVER_CLASS);
			conn = getConnectionNew(); // getConnection();
			// report.report("connected");
			if (conn.isClosed() == true) {
				report.report("connection is closed");
			}

			outerloop:
			while (elapsedTimeInSec < MAX_DB_TIMEOUT) {
				statement = conn.createStatement();
				rs = statement.executeQuery(sql);

				// ResultSetMetaData rsmd = rs.getMetaData();
				// columns=rsmd.getColumnCount();

				while (rs.next()) {

					String[] strArr = new String[columns];
					for (int i = 1; i <= columns; i++) {
						strArr[i - 1] = rs.getString(i);
					}
					list.add(strArr);

				}
				if (list.size() == 0) {
					elapsedTimeInSec++;
					Thread.sleep(1000);

				} else {
					isEmpty = true;
					break outerloop;
				}
			}

			conn.close();
		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			try {
				if (!isEmpty && !allowNull) {
					Assert.fail("Query result is null. Query was: " + sql);
				} else if (!isEmpty && allowNull) {
					return null;
				}

			} catch (Exception e) {

			}

			if (statement != null) {
				statement.close();
			}
			conn.close();
		}
		return list;
	}

	public List<String[]> getUserTestAdministrationsDetailsByStudentId(String studentId) {
		String sql = "select * from UserTestAdministrations where userId =" + studentId;
		List<String[]> userTestAdministration = null;
		try {
			userTestAdministration = getStringListFromQuery(sql, 1, true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return userTestAdministration;
	}

	public String gettestSettingsJson() {
		String sql = "select top 1 testSettingJSON from usertestprogress";
		String json = "";

		try {
			json = getStringFromQuery(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}

	public void updateStartAndEndDateInUserTestAdministration(String studentId, String testId, String impType,
															  String startDate, String endDate) {
		String sql = "update UserTestAdministrations set StartUTCDateTime = '" + startDate
				+ "' , StartClientDateTime = '" + startDate + "' , EndUTCDateTime = '" + endDate
				+ "' , EndClientDateTime = '" + endDate + "' where UserId = " + studentId + " AND TestId=" + testId
				+ " and testTypeId =" + impType;
		try {
			runDeleteUpdateSql(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void updateTestStatusIdByUserTestAdministrationId(String userTestAdministrationId, String testStatusId) {
		String sql = "update UserTestAdministrations set teststatusid = " + testStatusId
				+ " where usertestadministrationid='" + userTestAdministrationId + "'";
		try {
			runDeleteUpdateSql(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<String[]> getTestResultOfPLT_API_testNewTE(String userId) {
		String sql = "select top 1 * from UserPLTResult_V where userid = " + userId; // and startdate >= CONVERT(date,
		// getdate()) order by Date
		// desc";
		List<String[]> testResults = null;
		new ArrayList<String[]>();
		try {
			// int i=0;
			// while (testResults.isEmpty() && i<5){
			testResults = getStringListFromQuery(sql, 1, true);
			// i++;
			// sleep(1);
			// }

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return testResults;
	}

	public String selectUserStartedUTCByUserExitTestSettingsId(String userExitTestSettingsId) {
		String sql = "select UserStartedUTC from UserExitTestSettings where UserExitTestSettingsId = "
				+ userExitTestSettingsId;
		String UserStartedUTC = "";
		try {
			UserStartedUTC = getStringFromQuery(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return UserStartedUTC;
	}

	public List<String[]> getUserTestComponentResume() {
		String sql = "select * from UserTestComponentResume";
		List<String[]> userTestComponentResume = null;
		try {
			userTestComponentResume = getStringListFromQuery(sql, 1, true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return userTestComponentResume;
	}

	public List<String[]> getUserTestUnitResume() {
		String sql = "select * from UserTestUnitIntroResume";
		List<String[]> userTestUnitResume = null;
		try {
			userTestUnitResume = getStringListFromQuery(sql, 1, true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return userTestUnitResume;
	}

	public String getUserNameByUserIdFromUsersExternalMap(String userId) {
		// studentId = dbService.getUserIdByUserName(userName, institutionId);

		String sql = "select ExternalUserName from UsersExternalMap"
				+ " where UserId=" + userId;

		String actualUserName = null;

		try {
			actualUserName = getStringFromQuery(sql, 1, true, 1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return actualUserName;
	}

	public List<String[]> getClassFromClassExternalMapByUserId(String userId) {
		String sql = "select cem.ClassId as EDClassId,c.Name as EdClassName,cem.ExternalClassName as ExtClassId"
				+ " from classusers as cu inner join class as c on cu.classid = c.ClassId inner join ClassExternalMap as cem on cem.ClassId = c.ClassId "
				+ " where cu.userid = " + userId;
		List<String[]> className = null;
		try {
			className = getStringListFromQuery(sql, 1, true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return className;
	}

	public List<String[]> getTeacherClasses(String userId) {

		//List<List> rsList = getListFromStoreRrecedure("EXEC GetTeacherClasses " + userId);
		//List<String[]> list = rsList.get(0);

		String sp = "EXEC GetTeacherClasses " + userId;
		List<String[]> teacherClasses = null;

		try {
			teacherClasses = getStringListFromQuery(sp, 1, true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//				ListFromStoreRrecedure(sp);
/*
		for (int i = 0; i < list.size(); i++) {

			String[] classDetails = list.get(i);

			
			for (int start = 0, end = classDetails.length - 1; start <= end; start++, end--) {

				String temp = classDetails[start];
				classDetails[start] = classDetails[end];
				classDetails[end] = temp;

			}
		}
*/
		return teacherClasses; // returns list of string arrays [ClassId","ClassName"]

	}

	public String getClassFromClassExternalMapByClassId(String classId) {
		String sql = "Select ExternalClassName FROM ClassExternalMap WHERE ClassId=" + classId;
		String ExternalClassName = "";

		try {
			ExternalClassName = getStringFromQuery(sql, 1, true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ExternalClassName;
	}

	public String getClassIdByExternalClassName(String className, String instId) {
		String sql = "select c.classid from class as c"
				+ " inner join institutions as i on i.institutionid = c.institutionid"
				+ " inner join ClassExternalMap as cem on c.ClassId = cem.ClassId" + " where i.institutionid  = "
				+ instId + " and c.Name  = '" + className + "' and i.visible = 1";

		String ExternalClassId = "";

		try {
			ExternalClassId = getStringFromQuery(sql, 1, true, 2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ExternalClassId;
	}

	public String getClassIdByClassName(String className, String instId) {
		String sql = "select c.classid from class as c"
				//+ " inner join institutions as i on i.institutionid = c.institutionid"
				+ " where c.institutionid = " + instId + " and c.Name  = '" + className + "'";

		String classId = "";

		try {
			classId = getStringFromQuery(sql, 1, true, 2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return classId;
	}

	public String getDidTestByUserId(String userId) {
		String sql = "select didTest from UserExitTestSettings where userid = '" + userId + "'";
		String didTest = "";
		try {
			didTest = getStringFromQuery(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return didTest;
	}

	public String getskillNameByComponentId(String componentId) {
		String skillName = "";
		String sql = "select Skill.Name from Skill" + " inner join Component on Component.SkillId = Skill.SkillId"
				+ " where Component.ComponentId = " + componentId;

		try {
			skillName = getStringFromQuery(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return skillName;
	}

	public String getStudentsCountWithInvalidToken(String classId) {
		String count = "";
		String sql = "select count(uta.UserId) " + " from UserTestAdministrations as uta"
				+ " join ClassUsers as cu on cu.UserId = uta.UserId" + " where cu.ClassId = " + classId
				+ " and LEN(uta.UserTestToken)<20";

		try {
			count = getStringFromQuery(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return count;
	}

	public void deleteUsersTokens(String classId) {
		String sql = "delete uta" + " from UserTestAdministrations as uta"
				+ " join ClassUsers as cu on cu.UserId = uta.UserId" + " where cu.ClassId = " + classId;
		try {
			runDeleteUpdateSql(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void insertDBTestStatus(String testClass, String testCaseName, String testStatus, String build,
								   String logFilePath, String sourceBranch, String triggr, long testDuration) {

		String sql = "EXEC InsertAutomationTestStatus '" + testClass + "'," + testCaseName + "," + testStatus + ""
				+ ",'" + build + "','" + logFilePath + "','" + sourceBranch + "','" + triggr + "'," + testDuration + " ";

		try {
			runStorePrecedure(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public List<String[]> getClassesNameByInstitutionId(String instName) {

		List<String[]> classes = null;

		String sql = "select c.Name from class as c"
				+ " inner join institutions as i on i.institutionid = c.institutionid" + " where i.name  = '" + instName
				+ "' and i.visible = 1"
				+ " and c.name not in ('ClassNoTeacher','classNewUx')";

		try {
			classes = getStringListFromQuery(sql, 1, true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return classes;
	}

	public List<String[]> getTeacherInstitution(String instid) {
		String sp = "Exec GetTeachers " + instid;

		List<String[]> teachers = null;
		try {
			teachers = getStringListFromQuery(sp, 1, true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return teachers;
	}

	public String[] getuserPersonalDetails(String studentId) {

		String sql = "Select FirstName,LastName from Users where userId =" + studentId;
		List<String[]> user = null;
		try {
			user = getStringListFromQuery(sql, 1, true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return user.get(0);
	}

	public String getStudentLogOutValue(String studentId) {
		String LogoutValue = "";
		String sql = "select LogedIn" + " from Users " + " where userid = " + studentId + "";

		try {
			LogoutValue = getStringFromQuery(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return LogoutValue;
	}

	public String[] getRandomStudentByInstitutionId(String institutionId) {

		List<String[]> students = null;

		String sql = "Select top 20 u.UserName,FirstName,LastName,Password,u.InstitutionId,email,u.userid"
				+ " FROM Users as u"
				+ " inner join Institutions as i on i.InstitutionId = u.InstitutionId and i.AdministratorId <> u.UserId"
				// + " inner join UserPermissions as up on up.userid <> u.userid"
				+ " inner join classusers as cu on u.userid = cu.userid"
				+ " inner join class as c on c.classid = cu.classid" + " WHERE u.visible=1 AND u.InstitutionId=" + institutionId + " AND u.UserName LIKE '%stud%'"
				//+ " and u.RegistrationDate >= DATEADD(MONTH,-8,GETDATE())"
				+ " and (u.LogedIn is null or u.LogedIn = '2000-01-01 00:00:00.000')";

		try {
			students = getStringListFromQuery(sql, 1, true);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Random random = new Random();
		int i = random.nextInt(students.size());
		return students.get(i);
	}

	public String[] getStudentFromGroup(String classId) {

		List<String[]> students = new ArrayList<>();

		String sql = "select c.classid,c.Name,g.GroupId,g.Name,u.UserId,u.UserName,u.Password" + " from class as c "
				+ " inner join ClassUsers as cu on c.ClassId = cu.ClassId"
				+ " inner join GroupUsers as gu on cu.ClassUserId = gu.ClassUserId"
				+ " inner join Users as u on cu.UserId = u.UserId" + " inner join Groups as g on gu.GroupId = g.GroupId"
				+ " where c.ClassId=" + classId;

		try {
			students = getStringListFromQuery(sql, 0, true);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (students == null) {
			return null;
		}
		if (students.size() >= 1) {
			Random random = new Random();
			int i = random.nextInt(students.size());
			return students.get(i);
		}
		return null;
	}

	public String[] getExternalMapTeacherInstitution(String institutionId) {
		List<String[]> teachers = null;
		Random random = new Random();
		String[] selectedTeacher = null;

		String sql = "SELECT u.UserName, u.FirstName, u.LastName, u.Userid, u.Email"
				+ " ,uem.ExternalUserName,u.[Password]" + " FROM UserPermissions up" + " INNER JOIN"
				+ " Users u ON up.UserId = u.Userid" + " inner join"
				+ " [UsersExternalMap] as uem on u.UserId = uem.UserId" + " WHERE (up.InstitutionId = " + institutionId
				+ ")" + " AND (u.UserName not like '%-%' AND u.UserName not like 'autoTeacher2' AND uem.ExternalUserName not like '%-%')"
				+ " AND (up.PermissionId = 4)" + " AND (u.Visible = 1)" + " ORDER BY u.LastName,u.FirstName";

		try {
			teachers = getStringListFromQuery(sql, 1, true);
			int i = random.nextInt(teachers.size() - 1);
			selectedTeacher = teachers.get(i);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return selectedTeacher;
	}

	public String[] getRandomExternalStudentByInstitutionId(String institutionId) {

		List<String[]> students = null;

		String sql = "Select top 10 u.UserName,u.FirstName,u.LastName,u.Password,u.InstitutionId"
				+ " ,uem.ExternalUserName,u.Email,uem.userid" + " FROM Users as u"
				+ " inner join  [UsersExternalMap] as uem on u.UserId = uem.UserId" + " WHERE u.visible=1"
				+ " AND (u.UserName not like '%-%' AND uem.ExternalUserName not like '%-%')" + " AND u.InstitutionId="
				+ institutionId + "AND u.FirstName LIKE '%stud%'";

		try {
			students = getStringListFromQuery(sql, 1, true);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Random random = new Random();
		int i = random.nextInt(students.size());
		return students.get(i);
	}

	public List<String[]> getExternalTeacherClasses(String userId) throws Exception {

		String sql = "SELECT Class.ClassId as EdClassId, Class.Name as ExtClassName,cem.ExternalClassName as ExtClassId"
				+ " FROM UserPermissions INNER JOIN"
				+ " TeacherClasses ON UserPermissions.UserPermissionsId = TeacherClasses.UserPermissionsId"
				+ " INNER JOIN Class ON TeacherClasses.ClassId = Class.ClassId"
				+ " INNER JOIN ClassExternalMap as cem on class.ClassId = cem.ClassId"
				+ " WHERE UserPermissions.UserId =" + userId + "" + " ORDER BY NAME";

		List<String[]> list = getStringListFromQuery(sql, 1, true);
		// ("EXEC GetTeacherClasses " + userId);
		// List<String[]> list = rsList.get(0);
		/*
		 * for (int i = 0; i < list.size(); i++) {
		 *
		 * String [] classDetails = list.get(i);
		 *
		 * for (int start = 0, end = classDetails.length - 1; start <= end; start++,
		 * end--) {
		 *
		 * String temp = classDetails [start]; classDetails [start] = classDetails[end];
		 * classDetails [end] = temp;
		 *
		 * } }
		 */
		return list; // returns list of string arrays [ClassId","ClassName"]

	}


	public List<String[]> getExternalClassesForTeacher(String userId) throws Exception {

		String sql = "select class.ClassId as EdClassId,class.Name as EDclassName,cem.ExternalClassName as ExtClassId"
				+ " from UserPermissions as up"
				+ " inner join TeacherClasses tc on up.UserPermissionsId = tc.UserPermissionsId"
				+ " inner join ClassExternalMap as cem on tc.ClassId = cem.ClassId"
				+ " inner join class on class.ClassId = tc.ClassId"
				+ " WHERE up.UserId =" + userId + "" + " ORDER BY NAME";

		List<String[]> list = getStringListFromQuery(sql, 1, true);

		return list; // returns list of string arrays [ClassId","ClassName"]
	}


	public List<String[]> getExternalClassesNameByInstitutionId(String instID) {

		List<String[]> classes = null;

		String sql = " select c.classid,c.Name as ExtClassName,cem.ExternalClassName as ExtCanvasClassId"
				+ " from class as c"
				+ " inner join institutions as i on i.institutionid = c.institutionid"
				+ " inner join ClassExternalMap as cem on c.ClassId = cem.ClassId"
				+ " where i.institutionid  = '"
				+ instID + "' and i.visible = 1 and c.name not in ('ClassNoTeacher','classNewUx')";

		try {
			classes = getStringListFromQuery(sql, 1, true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return classes;
	}


	public String getExternalUserId(String userName) throws Exception {
		String sql = "select UserId from Users where FirstName like '" + userName + "%'";
		return getStringFromQuery(sql, 1, true, 2);

	}

	public String[] getRandomStudentByInstitutionId(String institutionId, String classId) {
		List<String[]> students = null;

		String sql = "Select top 10 u.UserName,FirstName,LastName,Password,u.InstitutionId,email,u.userid"
				+ " FROM Users as u"
				+ " inner join Institutions as i on i.InstitutionId = u.InstitutionId and i.AdministratorId <> u.UserId"
				+ " inner join UserPermissions as up on up.userid <> u.userid"
				+ " inner join classusers as cu on u.userid = cu.userid"
				+ " inner join class as c on c.classid = cu.classid" + " WHERE u.visible=1 AND u.InstitutionId="
				+ institutionId + " and c.classId =" + classId
				//+ " and u.RegistrationDate >= DATEADD(MONTH,-10,GETDATE())"
				+ " and (u.LogedIn is null or u.LogedIn = '2000-01-01 00:00:00.000')";
		;

		try {
			students = getStringListFromQuery(sql, 1, true);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Random random = new Random();
		int i = random.nextInt(students.size());
		return students.get(i);

	}

	public String[] getGroupNameAndGroupIdOfSpecificClass(String classId) throws Exception {

		List<String[]> groups = null;
		String sql = "select * from Groups where ClassId = " + classId;

		try {
			groups = getStringListFromQuery(sql, 1, true);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Random random = new Random();
		int i = random.nextInt(groups.size());
		return groups.get(i);
	}

	public void assignStudentToGroup(String sql) throws Exception {
		List<List> rsList = new ArrayList<List>();
		report.report("SQL query was: " + sql);
		try {
			conn = getConnectionNew();

			report.report("connected");
			if (conn.isClosed() == true) {
				report.report("connection is closed");
			}
			// statement = conn.createStatement();

			CallableStatement statement = conn.prepareCall(sql);
			// for (int i = 0; i < params.length; i++) {
			// statement.setString(i, params[i]);
			// }

			boolean results = statement.execute();

			int rsCount = 0;
			while (results) {
				List<String[]> strList = new ArrayList<String[]>();
				ResultSet rs = statement.getResultSet();
				ResultSetMetaData rsmd = rs.getMetaData();

				int columnsNumber = rsmd.getColumnCount();

				while (rs.next()) {
					String[] strArr = new String[columnsNumber];
					for (int i = 0; i < columnsNumber; i++) {
						strArr[i] = rs.getString(i + 1);
					}
					strList.add(strArr);
				}

				rs.close();
				rsList.add(strList);
				results = statement.getMoreResults();
			}
			statement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			conn.close();
		}
	}

	public String[] getStudentAfterPltAndNoProgressForAuthomaticLearningPath(String institutionId) throws Exception {

		List<String[]> students = null;
		int i = 0;

		String sql = "select u.UserId,uem.ExternalUserName,u.FirstName,u.LastName,u.Password,u.Email,"
				+ " class.name as ClassName,cem.ExternalClassName as CanvasClassId"
				+ " from users as u"
				+ " inner join PlacementTestGrades as ptg on u.UserId = ptg.UserId"
				+ " inner join UsersExternalMap as uem on u.UserId = uem.UserId"
				+ " inner join ClassUsers as cu on cu.UserId = u.UserId"
				+ " inner join class on class.classid = cu.classid"
				+ " inner join ClassExternalMap as cem on cem.ClassId = cu.ClassId"
				+ " left join UserCourseProgress as ucp on u.userid = ucp.userid"
				+ " WHERE ptg.PLTCompleted=1"
				+ " and ucp.PercentageDone is null"
				+ " and ptg.PLTVersion=1 and u.institutionid =" + institutionId
				+ " and ptg.Final <> 'A3'"
				+ " and ptg.Final = 'I3'"
				+ " and u.Visible = 1";

		try {
			students = getStringListFromQuery(sql, 1, true);

			if (students != null) {
				Random random = new Random();
				i = random.nextInt(students.size());
				return students.get(i);
			} else
				return null;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	public String[] getStudentAfterPltAndNoProgressForAuthomaticLearningPathNewTE(String institutionId, String expectedFinalLevel) throws Exception {

		List<String[]> students = null;
		int i = 0;

		String sql = "select u.UserId,uem.ExternalUserName,u.FirstName,u.LastName,u.Password,u.Email,"
				+ " class.name as ClassName,cem.ExternalClassName as CanvasClassId"
				+ " from users as u"
				+ " inner join UserTestProgress as utp on u.UserId = utp.UserId"
				+ " inner join UsersExternalMap as uem on u.UserId = uem.UserId"
				+ " inner join ClassUsers as cu on cu.UserId = u.UserId"
				+ " inner join class on class.classid = cu.classid"
				+ " inner join ClassExternalMap as cem on cem.ClassId = cu.ClassId"
				+ " left join UserCourseProgress as ucp on u.userid = ucp.userid"
				+ " WHERE utp.IsCompleted=1"
				+ " and ucp.PercentageDone is null"
				+ " and u.institutionid =" + institutionId
				+ " and utp.LearningStartLevel <> 'A3'"
				+ " and utp.LearningStartLevel = '" + expectedFinalLevel + "'"
				+ " and SubmitReasonId = 1"
				+ " and utp.TestId = 11"
				+ " and u.Visible = 1";

		try {
			students = getStringListFromQuery(sql, 1, true);

			if (students != null) {
				Random random = new Random();
				i = random.nextInt(students.size());
				return students.get(i);
			} else
				return null;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	public void deleteUserProgressFromTOEIC(String sql) {

		try {

			setEdDB(false);
			setUseToeicOlpc(true);

			runDeleteUpdateSql(sql);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			setEdDB(true);
			setUseToeicOlpc(false);
		}

	}

	public List<String[]> getUserProgressFromTOEIC(String sql) {

		List<String[]> progress = null;

		try {
			//if(PageHelperService.branchCI.contains("EdProduction"))
			//if(currentGeneralDB.equalsIgnoreCase("TOEIC_OLPC"))
			setEdDB(false);
			setUseToeicOlpc(true);
			progress = getStringListFromQuery(sql, 1, true);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			setEdDB(true);
			setUseToeicOlpc(false);
		}
		return progress;
	}

	public void resetStudentProgressInTOEIC(String userName, String courseId, String sql) {

		sql = sql.replace("%userName%", userName);
		sql = sql.replace("%courseId%", courseId);

		try {
			deleteUserProgressFromTOEIC(sql);
			//	List<String[]> rsList = dbService.getListFromPrepairedStmt(sql, 1);
			//	if (rsList.get(0)[0].equals("0"))
			//		success = true;
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public List<String[]> getUserNameAndPasswordByUserId(String id) throws Exception {
		String sql = "select UserName,Password,UserId,FirstName,Email,InstitutionId from users where UserId = " + id + "";

		List<String[]> student = getStringListFromQuery(sql, 0, true);
		return student;
	}

	public String getExternalClassIdByExternalClassName(String className, String institutionId) throws Exception {
		String sql = "select classId from ClassExternalMap where ExternalClassName = '" + className + "' and InstitutionId = " + institutionId + "";
		String id = getStringFromQuery(sql, 0, true);

		return id;
	}

	public void checkTheUserInsertedToLtiGrades(String studentId, String expectedGrade) {

		String sql = "select * from LtiGrades where userid =" + studentId + "";
		String[] expectedValueFromDb = {"" + studentId + "", "ED_FP", "190-1276-17670-6241-65b27cce6f3cb6e8ca4fda409c4d8a5f3f487f77", "https://siglo21.instructure.com/api/lti/v1/tools/190/grade_passback", expectedGrade, null, null};

		try {
			report.startStep("Verify the User Inserted to LtiGrades Table");

			List<String[]> userGrade = getStringListFromQuery(sql, 1, true);
			String[] actualValueFromDB = userGrade.get(0);

			for (int i = 0; i < actualValueFromDB.length; i++) {
				testResultService.assertEquals(expectedValueFromDb[i], actualValueFromDB[i], "value not match: ", false);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void assignTeacherToClasses(String classesId, String teacherId) throws Exception {
		String sql = "exec SetTeacherClasses " + teacherId + ",'" + classesId + "'";
		runStorePrecedure(sql);
		//String result = getStringFromQuery(sql,0,true);
		//int e = 0;

	}

	public boolean verifyClientTypeAndOperatingSystemOfProgress(String studentId) throws Exception {

		String sql = "select top 1 OperatingSystem, ClientType, Login from UserLog where UserId = " + studentId + " Order By Login desc";

		List<String[]> clientTypeAndOperatingSystem = getStringListFromQuery(sql, 0, true);
		String[] cTOpSys = clientTypeAndOperatingSystem.get(0);

		if (cTOpSys[0] != null && cTOpSys[1] != null) {
			return true;
		} else {
			return false;
		}

	}

	public List<String[]> getToeicTestObjectives(String instId) throws Exception {
		String sql = "select * from InstitutionToeicTestObjectives where InstitutionId=" + instId;
		return getStringListFromQuery(sql, 0, true);
	}

	public String verifyStudentAssignedToToeicBridge(String userName) throws Exception {
		String sql = "select t.AuthorizationKey from TOEICAssignedTest as t inner join users as u on t.UserId=u.UserId"
				+ " where u.UserName = '" + userName + "' and u.Visible=1";
		return getStringFromQuery(sql, 0, true);
	}

	public String[] getToeicScoresAndCertificate(String sql) throws Exception {
		List<String[]> item = null;
		useETSCertificatesDB = true;
		try {
			item = getStringListFromQuery(sql, 0, true);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			useETSCertificatesDB = false;
		}
		return item.get(0);
	}

	public List<String[]> getUnExpiredInstitutionPackages(String instId) throws Exception {
		List<String[]> item = null;
		//String sql = "select InstitutionPackageId from InstitutionPackages where institutionId="+instId+" and enddate >= CONVERT(date, getdate())";
		String sql = "select ip.InstitutionPackageId,p.Name,ip.StartDate from InstitutionPackages as ip inner join packages as p "
				+ "on ip.PackageId=p.PackageId where ip.InstitutionId = " + instId + " and ip.EndDate>= CONVERT(date, getdate())";

		item = getStringListFromQuery(sql, 1, true);
		//item = getArrayListFromQuery(sql, 0);
		return item;
	}

	public List<String[]> getInstitutionPackages(String instId) throws Exception {
		List<String[]> packages = null;
		//String sql = "EXEC GetInstitutionPackages "+instId;
		String sql = "select ip.InstitutionPackageId,p.Name, ip.StartDate, COUNT(u.userid) AS Used " +
				"from InstitutionPackages as ip " +
				"LEFT join packages as p on ip.PackageId=p.PackageId " +
				"LEFT join PackageProgress as pp on ip.InstitutionPackageId = pp.InstitutionPackageId " +
				"LEFT join users as u ON pp.UserId = u.Userid " +
				"where ip.InstitutionId = "+instId+" and ip.EndDate>= CONVERT(date, getdate()) " +
				"GROUP BY ip.InstitutionPackageId,p.Name,ip.StartDate";
		try {
			//packages = getListFromStoreRrecedure(sql);
			packages = getStringListFromQuery(sql, 1, true);
		}catch (Exception e){

		}

		return packages;
	}

	public List<String[]> getStartDateOfInstitutionPackage(String instId, String packageName) throws Exception {
		List<String[]> item = null;

		String sql = "select ip.StartDate,ip.InstitutionPackageId from InstitutionPackages as ip inner join  packages as p on p.PackageId=ip.PackageId "
				+ "where ip.InstitutionId = " + instId + " and p.Name = '" + packageName + "'";
		item = getStringListFromQuery(sql, 1, true);
		return item;
	}

	public String addMinutesToUTCtime(int i) throws Exception {
		String sql = "select DATEADD(MINUTE," + i + ",getutcdate())";
		return getStringFromQuery(sql);
	}

	public void updateEnd_UTC_DateInUserExitTestSetting(String expirationPLTtime, String studentId) throws Exception {
		String sql = "update UserExitTestSettings set EndDateUTC = '" + expirationPLTtime + "'  where userid = " + studentId;

		try {
			runDeleteUpdateSql(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getTestFormIdFromToeicDB(String userId) {

		String sql = "select formid from UsersForms where userId = " + userId;
		String response = "";
		try {

			setEdDB(false);
			setUseToeicOlpc(true);
			useToeicOnlineDB = true;
			response = getStringFromQuery(sql);
			//runDeleteUpdateSql(sql);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			setEdDB(true);
			setUseToeicOlpc(false);
			useToeicOnlineDB = false;
		}
		return response;
	}

	public String getUserTetsToken(String studentId) throws Exception {
		String sql = "select UserTestToken from UserTestAdministrations where userid = " + studentId;
		return getStringFromQuery(sql);
	}

	public List<String[]> getStudentsCompletedUnit(String institutionId) {

		List<String[]> students = null;
		String sql = "select top 10 u.username,u.password,u.userid,uup.unitid,c.Name,c.CourseId"
				+ " from users as u"
				+ " inner join UserUnitProgress as uup on u.UserId = uup.UserId"
				+ " inner join units on units.UnitId = uup.UnitId"
				+ " inner join course as c on c.CourseId = units.CourseId"
				+ " where u.Visible = 1 and uup.Progress = 100"
				+ " and u.InstitutionId = " + institutionId + ""
				+ " order by uup.LastUpdate desc";

		try {
			students = getStringListFromQuery(sql, 1, true);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return students;
	}


	public List<String[]> getStudentsCompletedCourse(String institutionId) {

		List<String[]> students = null;
		String sql = "select u.username,u.password,u.userid,c.Name,c.CourseId"
				+ " from users as u"
				+ " inner join UserCourseProgress as ucp on u.UserId = ucp.UserId"
				+ " inner join course as c on c.CourseId = ucp.CourseId"
				+ " where u.Visible = 1 and ucp.PercentageDone = 100"
				+ " and u.InstitutionId = " + institutionId + ""
				+ " order by ucp.ProgressLastUpdate desc";

		try {
			students = getStringListFromQuery(sql, 1, true);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return students;
	}

	public void deleteLastToeicTestEvent() throws Exception {
		String sql = "delete from TOEICTestEvents where ToeicTestEventId in (select ToeicTestEventId from TOEICTestEvents "
				+ "where EventClientDate >= CONVERT(date, getdate()) and EventClientDate < (select DATEADD(day,1,getdate())))";
		int res = runDeleteUpdateSql(sql);

	}

	public List<String[]> getStudentCourseProgress(String studentId) {
		//String sql = "select ucp.PercentageDone, ucp.TestAvg, uup.TimeOnUnitSeconds from UserCourseProgress as ucp inner join "
		//	+ "UserUnitProgress as uup on ucp.UserId=uup.UserId where ucp.userid  = "+studentId;
		String sql = "select PercentageDone, TestAvg from UserCourseProgress where userid = " + studentId;
		List<String[]> unitProgress = new ArrayList<String[]>();
		try {
			unitProgress = getStringListFromQuery(sql, 1, false);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return unitProgress;
	}

	public void insertClassToAutomationTable(String classId, String ClassName, String CanvasClassId, String institutionId, String studentId,
											 String baseUrl) {
		String sp = "exec InsertAutomationCreatedClass " + classId + ",'" + ClassName + "','" + CanvasClassId + "'," + institutionId + "," + studentId + ",'" + baseUrl + "'," + testCaseName + " ";

		try {
			runStorePrecedure(sp);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getCourseAliasNameById(String courseId) {

		String sql = "select Name,CourseAlias from course where courseId=" + courseId;
		List<String[]> courseNames = new ArrayList<String[]>();
		String courseAliasName = null;


		try {
			courseNames = getStringListFromQuery(sql, 1, true);
			courseAliasName = courseNames.get(0)[1];

			if (courseNames.get(0)[1] == null)
				courseAliasName = courseNames.get(0)[0];

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return courseAliasName;
	}

	public List<String[]> getAdminFromExternalMap(String institutionId) {

		List<String[]> admin = new ArrayList<String[]>();
		//	String sql = "select uep.ExternalUserName,u.FirstName,u.LastName,u.Email from UsersExternalMap as uep inner join users "
		//		+ "as u on uep.UserId=u.UserId where  uep.ExternalUserName='admin' and uep.InstitutionId = "+institutionId;

		String sql = "select eum.*,u.FirstName,u.LastName,u.Email"
				+ " from UsersExternalMap as eum"
				+ " inner join users as u on eum.userid = u.userid"
				+ " inner join institutions as i on i.InstitutionId = u.InstitutionId and i.AdministratorId = u.UserId"
				+ " where i.institutionid = " + institutionId;
		try {
			admin = getStringListFromQuery(sql, 1, true);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return admin;
	}

	public String[] getPackageByInstitutionPackageId(String instPackId, String institutionId) {
		List<String[]> pkg = new ArrayList<String[]>();
		String sql = "select * from InstitutionPackages where institutionId = " + institutionId + " and InstitutionPackageId = " + instPackId;

		try {
			pkg = getStringListFromQuery(sql, 1, true);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return pkg.get(0);
	}

	public List<String> getClassAssignPackages(String newClass, String institutionId) {

		List<String> classPackages = new ArrayList<>();
		String sql = "select cap.InstitutionPackageId from ClassAssignedPackages as cap"
				 + " inner join class as c on cap.ClassId=c.ClassId"
				 + " where c.Name= '" + newClass + "' and c.InstitutionId = " + institutionId;

		try {
			classPackages = getArrayListFromQuery(sql, 1);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return classPackages;

	}

	public List<String[]> getClassAssignPackagesNew(String className, String institutionId) {

		List<String[]> classPackages = new ArrayList<>();
		/*String sql = "select cap.InstitutionPackageId from ClassAssignedPackages as cap inner join class as c "
				+ "on cap.ClassId=c.ClassId where c.Name= '" + className + "' and c.InstitutionId = " + institutionId;*/

		String sql = "select cap.InstitutionPackageId from ClassAssignedPackages as cap"
				+ " inner join class as c on cap.ClassId=c.ClassId"
				+ " inner join InstitutionPackages as ip on cap.InstitutionPackageId = ip.InstitutionPackageId"
				+ " where c.Name in ('" + className + "') and c.InstitutionId = " +institutionId+" and ip.EndDate>GETDATE()"
				+ " group by cap.InstitutionPackageId";

		try {
			classPackages = getStringListFromQuery(sql,1,true);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return classPackages;

	}

	public List<String[]> getClassesAssignToPackages(String institutionId) {

		List<String[]> classPackages = new ArrayList<>();

		String sql =
		/*
				"select cap.InstitutionPackageId from ClassAssignedPackages as cap"
				+ " inner join class as c on cap.ClassId=c.ClassId"
				+ " inner join InstitutionPackages as ip on cap.InstitutionPackageId = ip.InstitutionPackageId"
				+ " where c.Name in ('" + className + "') and c.InstitutionId = " +institutionId+" and ip.EndDate>GETDATE()"
				+ " group by cap.InstitutionPackageId";
		*/

		"select distinct Class.name,cap.ClassId"
		+ " from InstitutionPackages as ip"
		+ "inner join ClassAssignedPackages as cap on ip.InstitutionPackageId = cap.InstitutionPackageId"
		+ "inner join class on class.ClassId = cap.ClassId"
		+ " where ip.InstitutionId = " +institutionId+" and ip.EndDate>GETDATE() and class.Name like '%class-%'"
		+ " order by ClassId";

		try {
			classPackages = getStringListFromQuery(sql,1,true);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return classPackages;

	}

	public void switchPltTakeOnlyOnceFlag(String institutionId, String state) throws Exception {
		String sql = "";
		if (state.equalsIgnoreCase("ON")) {
			sql = "update institutions set PTTakenOnce = 1 where institutionId = " + institutionId;
		} else if (state.equalsIgnoreCase("OFF")) {
			sql = "update institutions set PTTakenOnce = 0 where institutionId = " + institutionId;
		} else {
			testResultService.addFailTest("You made mistake in query to set flag 'PTTakenOnce'", true, false);
		}

		try {
			runDeleteUpdateSql(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getAdministartionOfCertificationTest(String adminDate) {
		String result = "";
		String sql = "select AdministrationId from Administrations where name like '%" + adminDate + " - " + adminDate + "'";
		useETSCertificatesDB = true;
		try {
			result = getStringFromQuery(sql);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			useETSCertificatesDB = false;
		}
		return result;
	}

	public List<String[]> getSessionAndAdminID(int range, String instId) {
		List<String[]> result = null;
		useETSCertificatesDB = true;
		//String sql = "select s.ItsSessionCode, a.ItsAdministrationId, i.ItsInstitutionId from Sessions as s inner join Administrations as a on s.AdministrationId=a.AdministrationId inner join Institutions as i on a.InstitutionId=i.InstitutionId where a.Name='"+name+"'";
		String sql = "select s.ItsSessionCode, a.ItsAdministrationId, i.ItsInstitutionId from Sessions as s inner join Administrations as a " +
				"on s.AdministrationId=a.AdministrationId inner join Institutions as i on a.InstitutionId=i.InstitutionId where " +
				"a.CreatedOn < DATEADD(dd,-" + range + ",getdate()) and a.EdOwnerId like '" + instId + "%'";

		try {
			result = getStringListFromQuery(sql, 1, true);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			useETSCertificatesDB = false;
		}
		return result;


	}

	public void deleteITSAdministrations(String sql, String institutionId, int rangeOfDays) {

		sql = sql.replace("%InstitutionId%", institutionId);
		sql = sql.replace("%rangeOfDays%", String.valueOf(rangeOfDays));
		useETSCertificatesDB = true;
		try {
			runDeleteUpdateSql(sql);
			//	List<String[]> rsList = dbService.getListFromPrepairedStmt(sql, 1);
			//	if (rsList.get(0)[0].equals("0"))
			//		success = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			useETSCertificatesDB = false;
		}

	}

	public String[] getTMSDomainUser() throws Exception {
		String sql = "select u.username,u.password,i.cannonicaldomain from users as u inner join institutions as i on u.InstitutionId=i.InstitutionId where u.UserName like '%tmsdomain%' and i.InstitutionId=3000025";
		List<String[]> list = null;
		try {
			list = getStringListFromQuery(sql, 1, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list.get(0);
	}

	public String[] getAdminUserByInstitutionId(String institutionId) {
		String sql = "select UserName,Password,UserId from users as u inner join Institutions as i on u.UserId=i.AdministratorId where i.InstitutionId = "+institutionId;
		List<String[]> list = null;
		try {
			list = getStringListFromQuery(sql, 1, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list.get(0);
	}

	public String[] getPlasemtTestsFromInstitutions(String institutionId) {
		String sql = "select testtypename from testtypes as tt inner join InstitutionPLTestTypes as tp on tt.TestTypeId=tp.TestTypeId where tp.institutionid=" + institutionId;
		List<String> resultList = new ArrayList<>();

		try {
			List<String[]> list = getStringListFromQuery(sql, 1, true);

			for (String[] row : list) {
				resultList.add(row[0]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return resultList.toArray(new String[0]);
	}

	public String getUserIdFromETSCertificateDB() {

		String sql = "select EdStudentId from Students where TestResultId is not null";
		String res = "";
		useETSCertificatesDB = true;
		List<String> list = new ArrayList<>();
		try {
			list = getArrayListFromQuery(sql, 1);

			//	List<String[]> rsList = dbService.getListFromPrepairedStmt(sql, 1);
			//	if (rsList.get(0)[0].equals("0"))
			//		success = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			useETSCertificatesDB = false;
		}
		useETSCertificatesDB = false;
		return list.get(0);
	}

	public String[] getStudentWithPLTResults(String institutionId) throws Exception {
		List<String[]> stud = null;
		String sql = "select u.username,u.UserId from users as u inner join  UserTestAdministrations as uta " +
				"on u.UserId=uta.UserId where u.InstitutionId="+institutionId+" and u.Visible=1 " +
				"and uta.TestStatusId=3 and uta.TestTypeId=1";

		try {
			stud = getStringListFromQuery(sql,1,true);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		if (stud==null){
			testResultService.addFailTest("No students", true, true);
		}
		return stud.get(new Random().nextInt(stud.size()-1));
	}

	public List<String[]> getValidInstitutionPackages(String institutionId) {

		List<String[]> res = null;
		String sql = "select InstitutionPackageId,institutionId,PackageId,NumberOfPackages,Remaining,UsedLicenses" +
				" from InstitutionPackages where InstitutionId="+institutionId+" and EndDate >=GETDATE()" +
				" order by InstitutionPackageId";
		try {
			res = getStringListFromQuery(sql,1,true);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return res;

	}

	public List<String[]> getUserPackagesProgress(String studentId) {

		List<String[]> res = null;
		String sql = "select InstitutionPackageId,StartDate from PackageProgress where UserId ="+studentId+
				" order by InstitutionPackageId";

		try {
			res = getStringListFromQuery(sql,1,true);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return res;
	}

	public List<String[]>getRemainingLicensesOfCertainPackage(List<String[]> classPackages, String institutionId) throws Exception {
			String packages = "";
			String[] arr = new String[classPackages.size()];
			if (classPackages.size()>1) {
				for (int i = 0; i < classPackages.size(); i++) {
					arr[i] = classPackages.get(i)[0];
				}
				packages = String.join(",", arr);
			}else {
				packages = classPackages.get(0)[0];
			}
			String sql = "select InstitutionPackageId,institutionId,PackageId,NumberOfPackages,Remaining,UsedLicenses " +
					"from InstitutionPackages where " +
					"InstitutionId=" + institutionId + " and EndDate >=GETDATE() " +
					"and InstitutionPackageId in ("+packages+")";
		return getStringListFromQuery(sql, 1,true);
	}
}

	

