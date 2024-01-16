package services;

import jcifs.smb.*;
import jcifs.util.Base64;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.core.har.HarEntry;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.*;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.*;
import java.net.*;
import java.sql.SQLXML;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("NetService")
public class NetService extends GenericService {

	@Autowired
	protected services.Reporter report;

	@Autowired
	Configuration configuration;

	@Autowired
	TextService textService;

	@Autowired
	DbService dbService;

	public NetService() {
	};

	private Map<String, String> map;

	public Document getXmlFromString(String xmlString) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		Document document = null;
		try {
			builder = factory.newDocumentBuilder();
			document = builder.parse(new InputSource(new StringReader(xmlString)));
		} catch (Exception e) {
			e.printStackTrace();
			// Assert.fail("Test failed during getting xml form sring");
		} finally {

		}
		return document;
	}

	public Document getXmlFromFile(String filePath) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		Document document = null;
		try {
			builder = factory.newDocumentBuilder();
			document = builder.parse(filePath);
		} catch (Exception e) {
			e.printStackTrace();
			// Assert.fail("Test failed during getting xml form sring");
		} finally {

		}
		return document;
	}

	public Document getXMLDocFromSQLXML(SQLXML sqlxml) throws Exception {
		InputStream binaryStream = sqlxml.getBinaryStream();
		DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document result = parser.parse(binaryStream);
		return result;
	}

	public List<String[]> getListFromXmlNode(Document document, String Xpath) throws Exception {

		List<String[]> arrList = new ArrayList<String[]>();
		// XPathFactory xPathfactory = XPathFactory.newInstance();
		// XPath xpath = xPathfactory.newXPath();
		// XPathExpression expr = xpath.compile(Xpath);
		// NodeList nl=(NodeList)expr.evaluate(document,XPathConstants.NODESET);
		NodeList nl = document.getChildNodes();

		NodeList childNodes = nl.item(0).getChildNodes();

		NodeList detailsNodeList = childNodes.item(2).getChildNodes();
		report.report("length of details node list is: " + detailsNodeList.getLength());
		for (int j = 0; j < detailsNodeList.getLength(); j++) {
			if (detailsNodeList.item(j).getAttributes() != null
					&& detailsNodeList.item(j).getAttributes().getNamedItem("code") != null) {
				String[] arr = new String[4];
				arr[0] = detailsNodeList.item(j).getAttributes().getNamedItem("code").toString();
				// arr[1] = detailsNodeList.item(j).getAttributes()
				// .getNamedItem("structure").toString();
				arr[1] = detailsNodeList.item(j).getChildNodes().item(0).getAttributes().getNamedItem("length")
						.toString();
				arr[1] = arr[1].replace("length=", "");
				arr[1] = arr[1].replace("\"", "");
				arr[2] = detailsNodeList.item(j).getChildNodes().item(0).getAttributes().getNamedItem("offset")
						.toString();
				arr[2] = arr[2].replace("offset=", "");
				arr[2] = arr[2].replace("\"", "");
				arr[3] = detailsNodeList.item(j).getChildNodes().item(0).getTextContent();
				arr[3] = arr[3].replace("*", "");
				arrList.add(arr);
				// detailsNodeList.item(j).getChildNodes().item(1).getAttributes().getNamedItem("offset");
			}
		}

		return arrList;
	}

	public NodeList getNodesFromXml(String expressionXpath, Document document) throws Exception {
		XPath xPath = XPathFactory.newInstance().newXPath();
		NodeList nodeList = (NodeList) xPath.compile(expressionXpath).evaluate(document, XPathConstants.NODESET);
		return nodeList;

	}

	public List<String[]> getListFromJson(String jsonString, String firstArrayName, String secondArrayName,
			String[] names) throws Exception {
		List<String[]> arrList = new ArrayList<String[]>();
		JSONObject jsonObject = new JSONObject(jsonString);
		try {
			JSONArray jsonArray = jsonObject.getJSONArray(firstArrayName);
			for (int i = 0; i < jsonArray.length(); i++) {
				String[] str = new String[names.length];
				JSONObject detailsObj = jsonArray.getJSONObject(i);
				JSONArray feedbacks = detailsObj.getJSONArray(secondArrayName);
				for (int j = 0; j < feedbacks.length(); j++) {

					JSONObject feedbackObject = feedbacks.getJSONObject(j);
					str = new String[names.length];
					for (int y = 0; y < names.length; y++) {

						if (names[y].equals("length") || names[y].equals("offset")) {
							str[y] = String.valueOf(feedbackObject.getInt(names[y]));

						} else {
							str[y] = feedbackObject.getString(names[y]);
						}

					}
					if (str[0] != null) {
						arrList.add(str);
					}

				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return arrList;
	}

	public NodeList getNodeChilds(Node node) throws Exception {
		NodeList nl = node.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {

		}
		return nl;

	}

	public static void listAllAttributes(Element element) {

		// get a map containing the attributes of this node
		NamedNodeMap attributes = element.getAttributes();

		// get the number of nodes in this map
		int numAttrs = attributes.getLength();

		for (int i = 0; i < numAttrs; i++) {
			Attr attr = (Attr) attributes.item(i);

			String attrName = attr.getNodeName();
			String attrValue = attr.getNodeValue();

		}
	}

	public void compareLists(List<String[]> listA, List<String[]> listB) throws Exception {

	}

	public String sendHttpRequest(String request, String requestMethod) throws Exception {
		return sendHttpRequest(request, requestMethod, false, null, null);
	}

	public String sendHttpRequest(String request, String requestMethod, boolean json, String user, String password)
			throws Exception {
		// report.report("Request is: " + request);
		String response = "";
		URL url = new URL(request);
		HttpURLConnection httpCon = null;
		OutputStreamWriter out = null;
		
		try {

			httpCon = (HttpURLConnection) url.openConnection();
			httpCon.connect();

			if (!user.equals(null) && !password.equals(null)) {
				String userpass = user + ":" + password;
				String basicAuth = "Basic " + new String(new Base64().encode(userpass.getBytes()));
				httpCon.setRequestProperty("Authorization", basicAuth);
			}
			httpCon.setDoOutput(true);
			httpCon.setDoInput(true);
			if (json) {
				httpCon.setRequestProperty("content-type", "application/json");
			}
			if (!requestMethod.equals("")) {
				httpCon.setRequestMethod(requestMethod);
			}
			out = new OutputStreamWriter(httpCon.getOutputStream());
						
			// System.out.println(httpCon.getResponseCode()) ;
			// report.report("Get response code: " + httpCon.getResponseCode());
			System.out.println(httpCon.getResponseCode());
			// report.report(httpCon.getResponseMessage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// report.report("Get response message: " +
			// httpCon.getResponseMessage());
			
			response = httpCon.getResponseMessage();
			out.close();
			
		}

		return response;

	}

	public void sendServiceRequest(String requestText) {

		long start = System.currentTimeMillis();
		try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
			HttpGet request = new HttpGet(requestText);

			// StringEntity params = new StringEntity(body);
			request.addHeader("content-type", "text/html");

			// request.setEntity(params);
			HttpResponse result = httpClient.execute(request);
			// if()
			report.report(request.toString());
			report.report(result.getStatusLine().toString());
			long end = System.currentTimeMillis();
			long time = end - start;
			System.out.println("Time spent: " + time);

		} catch (IOException ex) {
		}

	}
	
	public HttpResponse sendReceviceServiceRequest(String requestText) {

		long start = System.currentTimeMillis();
		HttpResponse response = null;
		try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
			HttpGet request = new HttpGet(requestText);

			response = httpClient.execute(request);
					
			report.report(request.toString());
			report.report(response.getStatusLine().toString());
			long end = System.currentTimeMillis();
			long time = end - start;
			System.out.println("Time spent: " + time);

		} catch (IOException ex) {
		}
		
		return response;
	}
	
	public JSONObject sendServiceRequestWebApi(String requestText) {

		JSONObject json = null;
		long start = System.currentTimeMillis();
		try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
			
			HttpGet request = new HttpGet(requestText);
			
			request.addHeader("content-type", "application/json");

			HttpResponse result = httpClient.execute(request);

			report.report(request.toString());
			HttpEntity entity = result.getEntity();
			
			 if (entity != null) {
				 String responseString = EntityUtils.toString(entity, "UTF-8");
				 json = new JSONObject(responseString);
			 } else {
				 testResultService.addFailTest("Cannot get JSON from response", false, false);
			 }
			
			//System.out.println(responseString);
						
			long end = System.currentTimeMillis();
			long time = end - start;
			System.out.println("Time spent: " + time);
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return json;
	}
	
	public JSONObject sendServiceRequestWebApiPost(String requestText, String bodyToPost) {

		JSONObject json = null;
		long start = System.currentTimeMillis();
		try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
						
			HttpPost request = new HttpPost(requestText);
			StringEntity jsonEntity = new StringEntity(bodyToPost);
			
			request.addHeader("content-type", "application/json");
			request.setEntity(jsonEntity);

			HttpResponse result = httpClient.execute(request);

			report.report(request.toString());
			
			HttpEntity entity = result.getEntity();
			
			 if (entity != null && result.getStatusLine().toString().contains("200 OK")) {
				 String responseString = EntityUtils.toString(entity, "UTF-8");
				 json = new JSONObject(responseString);
			 } else {
				 testResultService.addFailTest("Cannot get JSON from response or request failed", false, false);
			 }
									
			long end = System.currentTimeMillis();
			long time = end - start;
			System.out.println("Time spent: " + time);
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return json;
	}
	
	public String sendServiceRequestWebApiPostString(String requestText, String bodyToPost) {

		long start = System.currentTimeMillis();
		String responseString ="";

		try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
			
			
			
			HttpPost request = new HttpPost(requestText);
			StringEntity jsonEntity = new StringEntity(bodyToPost);
			
			request.addHeader("Content-Type","application/json");
			request.setEntity(jsonEntity);

			HttpResponse result = httpClient.execute(request);

			report.report(request.toString());
			
			HttpEntity entity = result.getEntity();
			
			 if (entity != null && result.getStatusLine().toString().contains("200 OK")) {
				 responseString = EntityUtils.toString(entity, "UTF-8");
			 } else {
				 testResultService.addFailTest("Cannot get JSON from response or request failed", false, false);
			 }
									
			long end = System.currentTimeMillis();
			long time = end - start;
			System.out.println("Time spent: " + time);
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return responseString;
	}
	
	public HttpResponse sendServiceRequestPost(String requestUrl, String bodyToPost) {
		

		  
				  
		//long start = System.currentTimeMillis();
		HttpResponse responseBody=null;
		try (CloseableHttpClient httpClient = HttpClientBuilder.create().build())
		{
			
			HttpPost request = new HttpPost(requestUrl);
			StringEntity Entity = new StringEntity(bodyToPost);
			
			
			//request.addHeader("Accept: text/html","application/xhtml+xml");
			request.addHeader("Content-Type","application/json");
			
			request.setEntity(Entity);
			responseBody  = httpClient.execute(request);
			
			report.report(request.toString());
			
			HttpEntity entity = responseBody.getEntity();
			
			System.out.println(responseBody.getAllHeaders());
			System.out.println(entity);
			
			InputStream ins =  entity.getContent();
			System.out.println(ins);
			 
			if (!(entity != null && responseBody.getStatusLine().toString().contains("200 OK")));
				 testResultService.addFailTest("Cannot get String from response or request failed", false, false);
		
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return responseBody;
	}
	
	
	
	public String sendHttpRequest(String request) throws Exception {
		return sendHttpRequest(request, "POST");

	}

	public void sendHttpRequestWithParams(String baseRequest, String[] paramsNames, String[] paramValues)
			throws Exception {
		String request = baseRequest + "?";
		for (int i = 0; i < paramsNames.length; i++) {
			if (i > 0) {
				request += "&";
			}
			request += paramsNames[i] + "=" + paramValues[i];
		}

		sendServiceRequest(request);
		// sendHttpRequest(request, "", true);
	}

	public HttpResponse sendHttpRequestWithParamsGet(String baseRequest, String[] paramsNames, String[] paramValues)
			throws Exception {
		String request = baseRequest + "?";
		for (int i = 0; i < paramsNames.length; i++) {
			if (i > 0) {
				request += "&";
			}
			request += paramsNames[i] + "=" + paramValues[i];
		}

		
		HttpResponse httpResponse =  sendReceviceServiceRequest(request);
		// sendHttpRequest(request, "", true);
		
		return httpResponse;
	}
	
	public void updateSlaveStatus(String slaveName, String text) throws IOException, UnsupportedEncodingException {
		// String statusFolder = "\\\\10.1.0.66\\slavesStatus\\";
		String smbFolder = "smb://10.1.0.83/slavesStatus/" + slaveName + ".txt";
		// NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication("",
		// "automation", "tamar2010");
		NtlmPasswordAuthentication auth = getAuth();
		SmbFile smbFile = new SmbFile(smbFolder, auth);

		SmbFileOutputStream outputStream = new SmbFileOutputStream(smbFile);
		outputStream.write(text.getBytes());

		// if file does not exist, create file
		// File file = new File(statusFolder + slaveName + ".txt");
		// if (file.exists()) {
		// System.out.println("file exist");
		// PrintWriter writer = new PrintWriter(statusFolder + slaveName
		// + ".txt", "UTF-8");
		//
		// writer.print(text);
		// writer.close();
		// } else {
		// System.out.println("file dose not exist. creating file");
		// PrintWriter writer = new PrintWriter(statusFolder + slaveName
		// + ".txt", "UTF-8");
		// writer.print(text);
		// writer.close();
		// }
		// if file exist, update status

	}

	public boolean checkAllSlaveStatus() throws Exception {
		// read all files from \\\\10.1.0.66\\slavesStatus\\ and check that all
		// has "true" in them
		TextService textService = new TextService();
		boolean status = false;
		// File folder = new File("\\\\10.1.0.66\\slavesStatus\\");
		String path = "smb://10.1.0.83/slavesStatus/";
		String[] listOfFiles;
		StringBuilder builder = null;
		NtlmPasswordAuthentication auto = getAuth();
		SmbFile smbFile = new SmbFile(path, auto);
		listOfFiles = smbFile.list();
		boolean[] slavesStatus = new boolean[listOfFiles.length];
		while (checkBooleanArr(slavesStatus) == false) {
			for (int i = 0; i < listOfFiles.length; i++) {
				// read status from file
				// String text = textService.getTextFromFile(
				// listOfFiles[i].getAbsolutePath(),
				// Charset.defaultCharset());
				// System.out.println("Text is: "+text);
				// SmbFileInputStream fileInputStream=new
				// SmbFileInputStream(path+listOfFiles[i]);

				// String text=fileInputStream.toString();

				try {
					builder = new StringBuilder();
					SmbFile file = new SmbFile(path + listOfFiles[i], auto);
					builder = readFileContent(file, builder);
				} catch (Exception e) {

				}
				String text = builder.toString();

				// System.out.println("Text is:" + text);
				if (text.equals("ready")) {
					System.out.println("Slave: " + listOfFiles[i] + " is ready");
					slavesStatus[i] = true;
				} else {
					// System.out.println("slave " + listOfFiles[i]
					// + " not ready. Sleeping for 5 seconds");
					Thread.sleep(5000);
					slavesStatus[i] = false;
				}
			}
		}

		System.out.println("All servers are ready");
		return true;
	}

	private StringBuilder readFileContent(SmbFile file, StringBuilder builder) throws IOException {
		BufferedReader reader = null;
		reader = new BufferedReader(new InputStreamReader(new SmbFileInputStream(file)));
		String lineReader = null;
		try {
			while ((lineReader = reader.readLine()) != null) {
				builder.append(lineReader);
			}
		} catch (Exception e) {

		} finally {
			reader.close();
		}
		return builder;
	}

	boolean checkBooleanArr(boolean[] arr) {
		boolean arrStatus = true;
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] == false) {
				arrStatus = false;
			}
		}
		return arrStatus;
	}

	public NtlmPasswordAuthentication getAuth() {
		String user = configuration.getGlobalProperties("automationServer.user");
		String pass = configuration.getGlobalProperties("automationServer.pass");
		NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication("", user, pass);
		return auth;
	}

	public NtlmPasswordAuthentication getDomainAuth() {

		String user = configuration.getGlobalProperties("domain.user");
		String pass = configuration.getGlobalProperties("domain.pass");
		NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication("",
				user, pass);		
		// System.out.println(user);
		//NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication("",
			//	configuration.getGlobalProperties("domain.user"), configuration.getGlobalProperties("domain.pass"));
		return auth;
	}

	public SmbFile[] getFilesInFolder(String jsonFilesRoot) throws IOException {
		
			//List<SmbFile> filesList = new ArrayList<SmbFile>();
			SmbFile folder = new SmbFile(jsonFilesRoot, getDomainAuth());
			return folder.listFiles();
		}
	
	public File[] getFilesInFolderWithoutSMB(String jsonFilesRoot) throws IOException {	
		String [] arr = jsonFilesRoot.split("/");
		jsonFilesRoot = "\\\\";
		for(int i=2;i<arr.length;i++) {
			jsonFilesRoot += arr[i]+"\\";
		}
		File folder = new File(jsonFilesRoot);
		return folder.listFiles();

	}
/*
	private void printSOAPResponse(SOAPMessage soapResponse) throws TransformerException, SOAPException {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		Source sourceContent = soapResponse.getSOAPPart().getContent();
		System.out.print("\nResponse SOAP Message = ");
		StreamResult result = new StreamResult(System.out);
		transformer.transform(sourceContent, result);

	}
*/
	public void sendTfsHttpPost(String sig) {
		try {
			HttpClient client = new DefaultHttpClient();
			String getURL = "http://localhost/tfsService/autoTfsService.asmx/runTestNew?planId=124&testCaseId=20556&suiteName="
					+ sig + "&passed=true";
			HttpGet get = new HttpGet(getURL);
			// HttpPost post=new HttpPost(getURL);
			HttpResponse responseGet = client.execute(get);
			HttpEntity resEntityGet = responseGet.getEntity();
			if (resEntityGet != null) {
				// do something with the response
				// Log.i("GET RESPONSE", EntityUtils.toString(resEntityGet));
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}

	}

	public boolean sendIMageRequest(String url) throws ProtocolException, Exception {
		URL u = new URL(url);
		HttpURLConnection h = (HttpURLConnection) u.openConnection();
		h.setRequestMethod("GET");
		h.connect();
		// System.out.println(h.getResponseCode());
		;
		return true;

	}

	public void clearLogFiles(int ageInWeeks) throws IOException {

		String logsPath = "smb://" + configuration.getLogerver() + "//Logs//automationLogs//";
		String screenShotsPath = "smb://" + configuration.getLogerver() + "//Logs//automationScreenshots//";
		
		deleteOldFilesFromFolder(logsPath, ageInWeeks);
		deleteOldFilesFromFolder(screenShotsPath, ageInWeeks);
	}
	
	public void clearWebDriverTempFolder(int ageInDays) throws IOException {
		
		String tempPath = "smb://" + configuration.getLogerver() + "//Users//IT-dep//AppData//Local//Temp//";
		deleteOldFilesFromFolder(tempPath, ageInDays);		
		
	}
	
	private void deleteOldFilesFromFolder(String path, int ageInDays) throws MalformedURLException, SmbException {
		SmbFile logFolder = new SmbFile(path, getAuth());
		// System.out.println(logFolder.listFiles().length);
		SmbFile[] filesList = logFolder.listFiles();
		int dayInMs = 86400000;
		
		for (int i = 0; i < filesList.length; i++) {
			SmbFile file = filesList[i];
			// System.out.println(file.createTime());
			System.out.println("Current time" + System.currentTimeMillis());
			
			//if (filesList[i].createTime() <= System.currentTimeMillis() - 1296000000) {
			
				
			if (filesList[i].createTime() <= System.currentTimeMillis() - (ageInDays*dayInMs)) {
				// System.out.println("file create time"+
				// filesList[i].createTime());
				// System.out.println("Current time"+
				// System.currentTimeMillis()/1000);

			
				try {
					filesList[i].delete();
					System.out.println(filesList[i].getName() + " File as deleted");
				} catch (Exception e) {
					// TODO: handle exception
				}
				
			}
		}
	}

	public void checkForNetworkErrors(Har har) {
		for (int i = 0; i < har.getLog().getEntries().size(); i++) {
			HarEntry entry = har.getLog().getEntries().get(i);
			String code = String.valueOf(entry.getResponse().getStatus());
			if (code.charAt(0) == '4' || code.charAt(0) == '5') {
				System.out.println(entry.getResponse().getStatusText());
				testResultService.addFailTest(entry.getResponse().getStatusText() + "  " + entry.getRequest().getUrl());
				System.out.println(entry.getRequest().getUrl());
				
				
			}
		}
	}
	
	public List<String[]> addNetworkErrorsToList(Har har,  List<String[]> list, String courseName, int unitIndex, int lessonIndex, int stepIndex, int taskIndex ) {
		for (int i = 0; i < har.getLog().getEntries().size(); i++) {
			HarEntry entry = har.getLog().getEntries().get(i);
			String code = String.valueOf(entry.getResponse().getStatus());
			if (code.charAt(0) == '4' || code.charAt(0) == '5') {
			//if (code.charAt(2) == '6' ) {
				System.out.println(entry.getResponse().getStatusText());
				testResultService.addFailTest(entry.getResponse().getStatusText() + "  " + entry.getRequest().getUrl() + ": Course "+courseName+" - Unit "+unitIndex+ " - Lesson "+lessonIndex+" - Step "+stepIndex+ " - Task "+taskIndex);
				System.out.println(entry.getRequest().getUrl());
				
				list.add(new String[] { entry.getRequest().getUrl(), String.valueOf(entry.getResponse().getStatus()), entry.getResponse().getStatusText(), "Course "+courseName+" - Unit "+unitIndex+ " - Lesson "+lessonIndex+" - Step "+stepIndex+ " - Task "+taskIndex });
				
			}
		}
		
		return list;
	}
	
	public void printSlowRequests(Har har, long trashhold, String name) throws Exception {
		double sum = 0;
		List<String[]> list = new ArrayList<>();

		for (int i = 0; i < har.getLog().getEntries().size(); i++) {

			HarEntry entry = har.getLog().getEntries().get(i);

			sum = sum + entry.getTime();

			if (entry.getTime() >= trashhold) {
				// System.out.println(entry.getRequest().getUrl()+"
				// "+entry.getTime());
				// report.report("Request: " + entry.getRequest().getUrl()
				// + " Time(in ms)" + entry.getTime());
				double time = entry.getTime();

				String[] strArr = new String[] { entry.getRequest().getUrl(), String.valueOf(time), name };
				list.add(strArr);
			}

		}
		/*
		 * //Summary of all request, under comment till we can calculate what
		 * sync and unsync String[] strArrSum = new String[] { "Summary",
		 * String.valueOf(sum) }; list.add(strArrSum);
		 */
		textService.writeArrayListToCSVFile(
				"smb://" + configuration.getLogerver() + "/UXPerformanceLog/perfLog" + dbService.sig() + name + ".csv",
				list, false);
	}

	public String getXmlResponseContentByUrl(String getURL) throws MalformedURLException, IOException, Exception {
		URL url = new URL(getURL);
		NodeList nl;
		try {
			URLConnection con = url.openConnection();
			InputStream in = con.getInputStream();
			String encoding = con.getContentEncoding();
			encoding = encoding == null ? "UTF-8" : encoding;
			String body = IOUtils.toString(in, encoding);
			// System.out.println(body);

			Document document = getXmlFromString(body);
			nl = document.getChildNodes();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return null;
		}

		return nl.item(0).getTextContent();
	}

	public void checkResponseIsOK(String string, Har har, int expectedCode) {
		boolean found = false;
		for (int i = 0; i < har.getLog().getEntries().size(); i++) {
			HarEntry entry = har.getLog().getEntries().get(i);
			if (entry.getRequest().getUrl().contains(string)) {
				if (!(entry.getResponse().getStatus() == expectedCode)) {
					testResultService.addFailTest("Did not get 200/OK response for file: " + string
							+ " , response code was: " + entry.getResponse().getStatus());
				} else {
					found = true;
				}
				break;
			}
		}
		if (found == false) {
			testResultService.addFailTest("request for file was not found: " + string);
		}

	}
	
	public void checkResponse(HttpResponse bodyString,String value) {
			if (!bodyString.getAllHeaders().toString().contains((value))) 
					testResultService.addFailTest("The expected value: " + value + " Not exits");
	}
	
	public JSONArray getJsonsArray(String jsonsFolderPath, String jsonFileName) throws IOException{
		JSONArray jsonArr = new JSONArray();
		try {
			
			// Retrieve All JSON files in the Folder
			SmbFile[] introFiles = getFilesInFolder(jsonsFolderPath);
			
			// Retrieve the index of the wanted JSON
			int index = retrieveFileIndexByName(introFiles, jsonFileName);
			
			// Get JSON Object from File
			String json = textService.readFileContent(introFiles[index]);
			jsonArr = new JSONArray(json);
			
		} catch (Exception e) {
			System.out.println(e);
		}
		return jsonArr;
	}

	public JSONArray getJsonsArray(String jsonsFolderPath, String jsonFileName, boolean useSmb) throws IOException {
		JSONArray jsonArr = new JSONArray();
		try {
			if (useSmb) {
				jsonArr = getJsonsArray(jsonsFolderPath, jsonFileName);
			} else {
				File[] files = new File(jsonsFolderPath).listFiles();//getFilesInFolderWithoutSMB(jsonsFolderPath);
				int index = retrieveFileIndexByName(files, jsonFileName);
				String json = textService.readFileContent(files[index]);
				jsonArr = new JSONArray(json);
			}
		}catch (Exception e){
			System.out.println(e);
		}

		return jsonArr;
	}

	public JSONObject getJsonObject(String jsonsFolderPath, String jsonFileName, boolean useSMB) throws IOException{
		JSONObject jsonObj = new JSONObject();
		try {
			String json ="";
			// Retrieve All JSON files in the Folder
			if(useSMB) {
				SmbFile[] introFiles = getFilesInFolder(jsonsFolderPath);	
				// Retrieve the index of the wanted JSON
				int index = retrieveFileIndexByName(introFiles, jsonFileName);
					
				// Get JSON Object from File
				json = textService.readFileContent(introFiles[index]);
			}else {
				File[] files = getFilesInFolderWithoutSMB(jsonsFolderPath);
				int index = retrieveFileIndexByName(files, jsonFileName);
				json = textService.readFileContent(files[index]);
			}
			jsonObj = new JSONObject(json);
			int y =5;
		} catch (Exception e) {
			System.out.println(e);
		}
		return jsonObj;
	}
	
	
	public JSONObject getJsonObject(String jsonsFolderPath, String jsonFileName) throws IOException{
		return getJsonObject(jsonsFolderPath, jsonFileName, true);
	}
	
	
	public int retrieveFileIndexByName(SmbFile[] files, String fileName){
		int index = 0;
		for (int i = 0; i < files.length; i++) {
			
			
			  String file = files[i].getName(); 
			  boolean correctFileName = file.contains(fileName);
			  boolean notCorrectFileName = file.contains("Development");
			  
			  if(correctFileName==true && notCorrectFileName==false) {
				  index = i; 
				  break;
			  }
				/*
				 * String[] partsOfFile = file.split("."); System.out.println(partsOfFile[0]);
				 * 
				 * if(partsOfFile[0].contains(fileName)&&(!partsOfFile[1].contains("Development"
				 * ))) { index = i; break; }
				 */
			 
				/*
				 * if (files[i].getName().contains(fileName)) { index = i; break; }
				 */
		}
		return index;
	}
	
	public int retrieveFileIndexByName(File[] files, String fileName){
		int index = 0;
		for (int i = 0; i < files.length; i++) {			
			  String file = files[i].getName(); 
			  boolean correctFileName = file.contains(fileName);
			  boolean notCorrectFileName = file.contains("Development");
			  
			  if(correctFileName==true && notCorrectFileName==false) {
				  index = i; 
				  break;
			  }				
		}
		return index;
	}
}
