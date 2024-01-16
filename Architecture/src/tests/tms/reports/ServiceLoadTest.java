package tests.tms.reports;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Test;

import tests.misc.EdusoftWebTest;

;

public class ServiceLoadTest extends EdusoftWebTest {

	@Test
	public void testROIServices() throws Exception {
		String[] paramNames = new String[] { "classId", "courseId" };
		String[] paramValues = new String[] { "523408001", "8" };

		List<String[]> list = new ArrayList<>();

		list.add(new String[] { "510450002", "8" });
		list.add(new String[] { "521140192", "8" });
		list.add(new String[] { "508430055", "8" });
		list.add(new String[] { "523194001", "8" });
		list.add(new String[] { "510450028", "8" });
		list.add(new String[] { "508430044", "8" });
		list.add(new String[] { "508430046", "8" });
		list.add(new String[] { "523023160", "8" });

		String request = configuration.getProperty("tms.url");

		request += "/Services/TMSService.svc/GetUnitOverviewByUnitsSummary";

		for (int i = 0; i < list.size(); i++) {
			netService.sendHttpRequestWithParams(request, paramNames,
					list.get(i));
		}
		netService.sendHttpRequestWithParams(request, paramNames, paramValues);
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

}
