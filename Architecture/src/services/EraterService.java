package services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EraterService extends GenericService {

	private static final String FEEDBACK_CODES_CSV = "files/csvFiles/EraterFeedbackCodes.csv";

	@Autowired
	private TextService textService;

	@Autowired
	private DbService dbService;
	@Autowired
	private NetService netService;
	@Autowired
	Configuration configuration;

	@Autowired
	protected services.Reporter report;

	@Autowired
	TestResultService testResultService;

	List<String[]> feedbackList = new ArrayList<String[]>();

	public EraterService() {

	}

	public List<String[]> removeHiddenCodesAndConvertToFeedbackCodes(
			List<String[]> list) throws Exception {
		// this method will get the Array list created from the XML
		loadList();
		// loop on the list
		for (int i = 0; i < list.size(); i++) {
			String[] str = list.get(i);
			str[0] = str[0].replace("code=", "");
			str[0] = str[0].replace("\"", "");
			// if the code is hidden, remove it
			if (checkIfCodeValid(str[0]) == false) {
				report.report("removing unused code=" + str[0]);
				list.remove(i);
				i = i - 1;
				continue;
			}
			// convert the code to feedback
			// str[3] is the suggestion id from ETS
			str[0] = getErateFeedbackByCode(str[0], str[3]);
			list.set(i, str);
		}
		return list;
	}

	public void loadList() throws Exception {
		List<String[]> list = textService
				.getStr2dimArrFromCsv(FEEDBACK_CODES_CSV);
		feedbackList = list;
	}

	public String getErateFeedbackByCode(String code, String suggestionId)
			throws Exception {
		String feedback = null;

		for (int i = 0; i < feedbackList.size(); i++) {
			String[] arr = feedbackList.get(i);
			if (suggestionId.isEmpty()
					|| suggestionId.matches(".*\\d.*") == false) {
				if (arr[0].equals(code) && arr[3].equals("1")) {
					feedback = arr[1];
					break;
				}
			} else {
				int sugggestionid = Integer.valueOf(suggestionId);
				sugggestionid = sugggestionid + 1;
				if (arr[0].equals(code)
						&& arr[2].equals(String.valueOf(sugggestionid))) {
					feedback = arr[1];
					break;
				}
			}
		}
		return feedback;
	}

	public boolean checkIfCodeValid(String code) throws Exception {
		boolean isValid = true;
		for (int i = 0; i < feedbackList.size(); i++) {
			String[] arr = feedbackList.get(i);
			if (arr[0].equals(code) && arr[3].equals("0")) {
				isValid = false;
				break;
			}
		}
		return isValid;
	}

	public void printArrayList(List<String[]> list) {
		for (int i = 0; i < list.size(); i++) {
			String[] arr = list.get(i);
			report.report("printing the array");
			for (int j = 0; j < arr.length; j++) {

				report.report(arr[j]);

			}
			report.report(arr[0] + "," + arr[1] + "," + arr[2]);
		}
	}

	public List<String[]> sortArrayList(List<String[]> list) {
		Collections.sort(list, new Comparator<String[]>() {

			@Override
			public int compare(String[] o1, String[] o2) {
				int compareInt = o1[0].compareTo(o2[0]);
				if (compareInt == 0) {

					for (int j = 1; j < o1.length; j++) {
						compareInt = o1[j].compareTo(o2[j]);
						if (compareInt == 0) {
							break;
						}
					}
				}
				return compareInt;
			}
		});
		return list;
	}

	public boolean compareJsonAndXmlByWritingId(String writingId)
			throws Exception {
		List<String[]> xmlList = null;
		List<String[]> jsonList = null;
		String sqlxml = dbService.getStringFromQuery(
				"select EraterXML from Erater where writingId=" + writingId,
				10, false);
		// report.report("RAW XML: " + sqlxml);
		try {
			xmlList = netService.getListFromXmlNode(
					netService.getXmlFromString(sqlxml), "/WAT:DetailInfo");
			String jsonStr = dbService.getStringFromQuery(
					"select EraterJson from Erater where writingId="
							+ writingId, 10, false);
			// report.report("RAW JSON: " + jsonStr);
			jsonList = netService.getListFromJson(jsonStr, "sections",
					"details", new String[] { "feedback", "length", "offset" });
			// report.report("Printing json list",
			// EnumReportLevel.CurrentPlace);
			printArrayList(jsonList);
			// report.stopLevel();
			// jsonList = sortArrayList(jsonList);
			xmlList = removeHiddenCodesAndConvertToFeedbackCodes(xmlList);
			// xmlList = sortArrayList(xmlList);

			// report.report("Printing xml list",
			// EnumReportLevel.CurrentPlace);
			printArrayList(xmlList);
			// report.stopLevel();

			report.report("json list length is: " + jsonList.size());
			report.report("xml list length is: " + xmlList.size());
		} catch (Exception e) {
			// Assert.fail("Test failed during comparing json and xml");
			testResultService
					.addFailTest("Test failed during comparing json and xml");
		} finally {

		}
		return AssertJsonAndXmlLists(xmlList, jsonList);

	}

	public boolean AssertJsonAndXmlLists(List<String[]> xmlList,
			List<String[]> jsonList) {
		boolean unmatchFound = true;
		outerloop: for (int i = 0; i < xmlList.size(); i++) {
			String[] xmlStrArr = xmlList.get(i);
			boolean found = false;
			innerloop: for (int j = 0; j < jsonList.size(); j++) {
				String[] jsonStrArr = jsonList.get(j);
				if (jsonStrArr[0].equals(xmlStrArr[0])
						&& jsonStrArr[1].equals(xmlStrArr[1])
						&& jsonStrArr[2].equals(xmlStrArr[2])) {
					report.report("Match found for entry: " + xmlStrArr[0]
							+ " " + xmlStrArr[1] + " " + xmlStrArr[2]);
					found = true;
					break innerloop;
				}

			}
			if (found == false) {
				unmatchFound = false;
				report.report("entry not found: " + xmlStrArr[0] + " "
						+ xmlStrArr[1] + " " + xmlStrArr[2]);
			}
		}
		return unmatchFound;
	}

	public String getWritingIdByUserIdAndTextStart(String userId,
			String textStart) throws Exception {
		return getWritingIdByUserIdAndTextStart(userId, textStart, false);
	}

	public String getWritingIdByUserIdAndTextStart(String userId,
			String textStart, boolean waitFor2ndSubmission) throws Exception {
		String sql = "  select * from writing where UserId='" + userId
				+ "' and EssayText like '%" + textStart + "%' ";
		if (waitFor2ndSubmission) {
			sql += " and Submissions=2 and Reviewed=0";
		}
		String result = dbService.getStringFromQuery(sql, 60, false);

		Assert.assertFalse("writing id is null", result.equals("null"));
		return result;
	}

	public boolean checkWritingJsonInEraterTable(String writingId)
			throws Exception {
		String sql = "select EraterJson from Erater where writingid="
				+ writingId;
		String result = dbService.getStringFromQuery(sql);
		if (result.length() > 5) {
			return true;
		} else {
			return false;
		}
	}

	public String getWritingIdByUserId(String userId) throws Exception {
		String sql = "select writingId from writing where userId='" + userId
				+ "'";
		String result = dbService.getStringFromQuery(sql);
		return result;
	}

	public void checkWritingIdsByCSV() throws Exception {
		List<String[]> writingIds = textService
				.getStr2dimArrFromCsv("files/csvFiles/writingIds.csv");
		for (int i = 0; i < writingIds.size(); i++) {

			compareJsonAndXmlByWritingId(writingIds.get(i)[0]);
		}
	}

	// public List<Course> loadCoursedDetailsFromCsv() throws Exception {
	// List<String[]> courses = textService
	// .getStr2dimArrFromCsv("files/csvFiles/Courses.csv");
	// List<Course> coursesList = new ArrayList<Course>();
	// for (int i = 0; i < courses.size(); i++) {
	// Course course = new Course();
	// course.setName(courses.get(i)[0]);
	//
	// CourseUnit courseUnit = new CourseUnit();
	// courseUnit.setName(courses.get(i)[1]);
	//
	// UnitComponent unitComponent = new UnitComponent();
	// unitComponent.setName(courses.get(i)[2]);
	// unitComponent.setStageNumber(courses.get(i)[3]);
	//
	// courseUnit.addUnitComponent(unitComponent);
	//
	// course.AddUnit(courseUnit);
	//
	// coursesList.add(course);
	//
	// // courseUnit.setUnitComponent(unitComponent);
	//
	// }
	// return coursesList;
	//
	// }

	public void deleteStudentAssignments(String userId) throws Exception {

		String sqlForDeleteWritingHistory = "delete from writingHistory where writingid in(select writingid from writing where userid="
				+ userId + ")";
		String sqlForDeleteEraterHistory = "delete from EraterHistory where writingid in(select writingid from writing where userid="
				+ userId + ")";
		String sqlDeleteEraterErrorServiceLog = "delete from ERaterServiceErrorLog where writingid in (select writingid from writing where userid="
				+ userId + ")";
		String sql = "delete  from writing where userid=" + userId;
		dbService.runDeleteUpdateSql(sqlForDeleteEraterHistory);
		dbService.runDeleteUpdateSql(sqlForDeleteWritingHistory);
		dbService.runDeleteUpdateSql(sqlDeleteEraterErrorServiceLog);
		dbService.runDeleteUpdateSql(sql);
	}

	public void deleteWritngFromDb(String writingId) throws Exception {
		String sqlWritingHistory = "delete from writingHistory where writingid="
				+ writingId;
		String sqlEraterHistory = " delete from eraterHistory where writingid="
				+ writingId;
		dbService.runDeleteUpdateSql(sqlWritingHistory);
		dbService.runDeleteUpdateSql(sqlEraterHistory);

	}

	public void setEraterTeacherFirst() throws Exception {
		String instId = configuration.getProperty("institution.id");
		String sql = "update dbo.Institutions set teacherfirst=1  where institutionid="
				+ instId;
		dbService.runDeleteUpdateSql(sql);
	}

	public void setEraterTeacherLast() throws Exception {
		String instId = configuration.getProperty("institution.id");
		String sql = "update dbo.Institutions set teacherfirst=0  where institutionid="
				+ instId;
		dbService.runDeleteUpdateSql(sql);
	}

	public void checkWritingIsProcessed(String writingId) throws Exception {
		// String institutionSubmissions = dbService
		// .getStringFromQuery(" select NumberOfSubmissions from institutions where institutionid="
		// + configuration.getProperty("institution.id"));
		// String sql =
		// "select writingid from writing where Processed=1 and Submissions="
		// + institutionSubmissions
		// + " and Erater=1 and processed=1  and writingid="
		// + writingId;
		//
		// dbService.getStringFromQuery(sql);
		
		Thread.sleep(120000);

//		for (int i = 0; i < 120; i++) {
//			List<String[]> list = dbService.getStringListFromQuery("select * from writing where writingid=" + writingId, 1, 11);
//					
//			System.out.println(list.get(0)[5]+" "+list.get(0)[6]+" "+list.get(0)[7]+" "+list.get(0)[8]+" "+list.get(0)[9]+" "+list.get(0)[10]);
//			Thread.sleep(1000);
//
//		}

	}

	public void checkWritingIsReviewed(String writingId) throws Exception {
		String institutionSubmissions = dbService
				.getStringFromQuery(" select NumberOfSubmissions from institutions where institutionid="
						+ configuration.getProperty("institution.id"));
		String sql = "select writingid from writing where Processed=1 and Submissions="
				+ institutionSubmissions
				+ " and reviewed=1  and writingid="
				+ writingId;
		dbService.getStringFromQuery(sql);
	}

}
