package services;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import services.tfs.SnippetsSamplesConnectionAdvisor;

import com.microsoft.tfs.core.TFSTeamProjectCollection;
import com.microsoft.tfs.core.clients.workitem.WorkItem;
import com.microsoft.tfs.core.clients.workitem.WorkItemClient;
import com.microsoft.tfs.core.httpclient.Credentials;
import com.microsoft.tfs.core.httpclient.DefaultNTCredentials;
import com.microsoft.tfs.core.httpclient.UsernamePasswordCredentials;
import com.microsoft.tfs.core.util.CredentialsUtils;
import com.microsoft.tfs.core.util.URIUtils;

@Service("TfsService")
public class TfsService extends GenericService {

	private static final String USERNAME = "edusoft2k\\auto2";
	//private static final String PASSWORD = "!q2w3e4r";
	private static final String PASSWORD = "!q2w3e4r1";
	private static final String HTTP_PROXY_URL = null;
	//private static final String COLLECTION_URL = "http://vstf2013:8080/tfs/DefaultCollection";
	private static final String COLLECTION_URL = "http://azure2020:8080/tfs/DefaultCollection";
	static TFSTeamProjectCollection tfsCollection;
	//private String serverIp = "10.1.10.102";// newjenkins server
	private String serverIp = "10.1.0.213";// loadagent server
	//private String serverIp = "10.208.43.138";// loadagent server
	static public String tfsService = "tfstestcase.edusoftrd.com";
	WorkItemClient workItemClient;

	// WorkItem workItem;

	@Autowired
	Reporter report;

	@Autowired
	NetService netService;

	public void getTestPlan() {

	}

	private void initWorkItemClient() {
		if (workItemClient == null) {
			workItemClient = tfsCollection.getWorkItemClient();
		}
	}

	public String getAutomationStatus(int testCaseId) {
		initWorkItemClient();

		WorkItem workItem = workItemClient.getWorkItemByID(testCaseId);

		String state = null;
		try {
			state = workItem.getFields()
					.getField("Microsoft.VSTS.TCM.AutomationStatus").getValue()
					.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return state;
	}

	/**
	 * 
	 * @param testCaseID
	 * @param desiredStatus
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public void updateTestCaseAutomationState(int testCaseID,
			String desiredStatus) throws ClientProtocolException, IOException {

		System.out.println("Desired status: " + desiredStatus);
		initWorkItemClient();

		WorkItem workItem = workItemClient.getWorkItemByID(testCaseID);

		String state = getAutomationStatus(testCaseID);

		String stateToUpdate = null;
		switch (desiredStatus) {
		case "In Planning":
			stateToUpdate = "Planned";
			break;

		case "Automated":
			stateToUpdate = "Automated";
			break;

		case "Not Automated":
			stateToUpdate = "Not%20Automated";
			break;

		case "NotStable":
			stateToUpdate = "Not%20Stable";
			break;

		case "InProgress":
			stateToUpdate = "In%20Progress";

		}
		if (stateToUpdate != null && stateToUpdate.length() > 0) {

			System.out.println(" status to update: " + stateToUpdate);

			// String currentStatus = workItem.getFields()
			// .getField("Microsoft.VSTS.TCM.AutomationStatus").getValue()
			// .toString();
			// System.out.println("Current= " + currentStatus);
			//
			// if (!currentStatus.equals(stateToUpdate)) {
			// workItem.getFields()
			// .getField("Microsoft.VSTS.TCM.AutomationStatus")
			// .setValue(stateToUpdate);
			// workItem.save();
			// }

			String url = "http://" + serverIp
					+ "/service.asmx/updateAutomationStatus?newStatus="
					+ stateToUpdate + "&testCaseId=" + testCaseID;

			HttpGet get = new HttpGet(url);
			HttpClient client = new DefaultHttpClient();
			HttpResponse responseGet = client.execute(get);
		}

	}

	/**
	 * 
	 * @param testCaseId
	 * @return Test case name from TFS
	 */
	public String getTestCaseTitle(int testCaseId) {
		try {
			initWorkItemClient();

			WorkItem workItem = workItemClient.getWorkItemByID(testCaseId);

			return workItem.getTitle();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return null;
		}

	}

	public String getTastCaseLink(int testCaseId) {
		initWorkItemClient();

		WorkItem workItem = workItemClient.getWorkItemByID(testCaseId);

		return workItem.getURI();
	}

//	public static TFSTeamProjectCollection connectToTFS() {
//		// TFSTeamProjectCollection tpc = null;
//		Credentials credentials;
//
//		Properties props = System.getProperties();
//		props.setProperty("com.microsoft.tfs.jni.native.base-directory",
//				"C:\\automation\\TFSSDK");
//
//		// In case no username is provided and the current platform supports
//		// default credentials, use default credentials
//		if ((USERNAME == null || USERNAME.length() == 0)
//				&& CredentialsUtils.supportsDefaultCredentials()) {
//			credentials = new DefaultNTCredentials();
//		} else {
//			credentials = new UsernamePasswordCredentials(USERNAME, PASSWORD);
//		}
//
//		URI httpProxyURI = null;
//
//		if (HTTP_PROXY_URL != null && HTTP_PROXY_URL.length() > 0) {
//			try {
//				httpProxyURI = new URI(HTTP_PROXY_URL);
//			} catch (URISyntaxException e) {
//				report.report(e.toString());
//			}
//		}
//
//		SnippetsSamplesConnectionAdvisor connectionAdvisor = new SnippetsSamplesConnectionAdvisor(
//				httpProxyURI);
//
//		tfsCollection = new TFSTeamProjectCollection(
//				URIUtils.newURI(COLLECTION_URL), credentials, connectionAdvisor);
//		// System.out.println("Finished connected to TFS");
//		return tfsCollection;
//	}

	public String getWorkItemTitle(String testCaseId) throws Exception {
		//HttpClient client = new DefaultHttpClient();
		//String serverIp = "10.1.0.213";
		//String serverIp = tfsService; "tfstestcase.edusoftrd.com";
		//private String serverIp = "10.1.0.213";// loadagent server
		//serverIp = "10.208.43.138";// loadagent server
		
		
		String getURL = "http://" + tfsService
				+ "/service.asmx/getWorkItemStatus?testCaseId=" + testCaseId;

		// HttpGet get = new HttpGet(getURL);
		// HttpResponse responseGet = client.execute(get);
		// System.out.println(responseGet);
		String testCaseTitle = netService.getXmlResponseContentByUrl(getURL);
		
		return testCaseTitle;
		// netService.getListFromXmlNode(document, "//string");
		//
		// System.out.println(netService.getListFromXmlNode(document,
		// "//sring").get(0)[0]);

	}

	/**
	 * 
	 * @param planId
	 * @param testCaseId
	 * @param configId
	 *            the configuration id of the browser
	 * @param passed
	 *            true/false
	 * @param reportToTfs
	 */
	public void updateTestResults(String planId, String testCaseId,
			String configId, boolean passed, String reportToTfs) {
		try {

			if (planId != null && testCaseId != null && configId != null
					&& reportToTfs.equals("true")) {
				HttpClient client = new DefaultHttpClient();
				String getURL = "http://" + serverIp
						+ "/service.asmx/runTestNew?planId=" + planId
						+ "&testCaseId=" + testCaseId + "&configId=" + configId
						+ "&passed=" + String.valueOf(passed) + "&logFile="
						+ "";

				report.report("Request to tfs service was: " + getURL);
				HttpGet get = new HttpGet(getURL);
				HttpResponse responseGet = client.execute(get);

				if (responseGet.getStatusLine().getStatusCode() != 200)
					report.report("response was: " + responseGet);
				
				HttpEntity resEntityGet = responseGet.getEntity();
				if (resEntityGet != null) {
					String body = EntityUtils.toString(resEntityGet, "UTF-8");
					report.report("GET RESPONSE " + body);
				}
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
