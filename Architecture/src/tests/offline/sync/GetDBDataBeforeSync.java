package tests.offline.sync;

import java.util.List;

import org.junit.Test;

import tests.misc.EdusoftWebTest;
import Enums.InstallationType;
import Objects.StudentTest;

public class GetDBDataBeforeSync extends EdusoftWebTest {

	public void getDBsDataBeforeSync() throws Exception {

		List<StudentTest> offlineTests = studentService
				.getMultipleStudentsTests(new String[] {},
						InstallationType.Offline);

		List<StudentTest> onlineTests = studentService
				.getMultipleStudentsTests(new String[] {},
						InstallationType.Online);

		String offlineTestCsvPath = "files/temp/offlineTests"
				+ dbService.sig(6);
		String onlineTestCsvPath = "files/temp/onlineTests" + dbService.sig(6);

		textService.writeArrayistToCSVFile(offlineTestCsvPath,
				studentService.getStudentTestListIntoArrayList(offlineTests));
		
		textService.writeArrayistToCSVFile(onlineTestCsvPath,
				studentService.getStudentTestListIntoArrayList(onlineTests));

	}

}
