package tests.WebApi;

import org.json.JSONObject;
import org.junit.Test;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import com.gargoylesoftware.htmlunit.protocol.about.Handler;

import Enums.ByTypes;
import net.lightbody.bmp.core.har.Har;

import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


import pageObjects.edo.NewUxLearningArea;
import tests.edo.newux.LearnningAreaContainerTests;
import tests.edo.newux.testDataValidation;
import tests.misc.EdusoftBasicTest;
import tests.misc.EdusoftWebTest;

public class RestWebApiTests extends EdusoftBasicTest {


	
	private boolean enableProxy;

	@Test
	public void testWebApi() throws Exception {
		
		testDataValidation testData = new testDataValidation();
		
		//String request = "https://edux.qa.com/WebApi/Login/st5/12345/5233217";
		
		String request = "http://eddatagathering-qa.azurewebsites.net/recording/";
		//String response = netService.getXmlResponseContentByUrl(request);
		//String response = netService.sendHttpRequest(request, "GET", true, null, null);
		
		String jsonToPost = "{\"SessionKey\":\"automation\",\"ActivityType\":\"Login\",\"ActivityName\":\"1\",\"ActivityParams\":{\"UserName\":\"st5\",\"LocalTimezone\":\"2\"}}";
		
		JSONObject JSONObject = netService.sendServiceRequestWebApiPost(request, jsonToPost);
		String key = "StudentID";
		String expectedValue = "52332170000007";
		
		testData.verifyJsonValueByKey(JSONObject, key, expectedValue);
		
		sleep(2);
			

	}


	@Test
	public void testToeicPltApi() throws Exception {
		
		
		
		String requestUrl = "http://toeicolpc.qa.com/IntroPLT.aspx";
		
		StringBuilder sb = new StringBuilder();	
				
		  sb.append("epaId=8997AB6E-C975-4331-BCE0-D72D2F48B8DA&");  
		  sb.append("token=8997AB6E-C975-4331-BCE0-D72D2F48B8DA&");  
		  sb.append("uid=000000005&");
		  sb.append("url=http://toeicolpc.qa.com/viewPLTResult.aspx");
	
		  
		  HttpResponse response = netService.sendServiceRequestPost(requestUrl, sb.toString());
		  
		  //InputStream inputStream =  (InputStream) response.getEntity().getContent();
		  
		  System.out.println(response);
		  System.out.println();
		  //System.out.println(inputStream);
		  //String uidExpected = "UId=008000000003";
		  //netService.checkResponse(response,uidExpected);
		
	}

	
	@Test
	public void testGetStudentCoursesApi() throws Exception {
		
		
		
		String requestUrl = "https://edux.qa.com/ClassicEDO/api/template/GetStudentCourses.aspx";
		//String requestUrl = "https://webux-ci-20180426-4.develop.com/ClassicEDO/api/template/GetStudentCourses.aspx";
		  //StringBuilder sb = new StringBuilder();	
		  //sb.append("UserId=5233217000029");
		
		  String[] userId={"UserId"};
		  String[] value= {"5233217000029"};
		  
		  HttpResponse response = netService.sendHttpRequestWithParamsGet(requestUrl, userId, value);
		  
		  //InputStream inputStream =  response.getEntity().getContent();
		  
		  System.out.println(response);
		  //System.out.println(inputStream);
		  System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
		  //String uidExpected = "UId=008000000003";
		  //netService.checkResponse(response,uidExpected);
		  		
	}

	
	@Test
	public void testGetStudentCoursesApiTEMP() throws Exception {
		
		
		
		//String requestUrl = "";
		//String requestUrl = "https://webux-ci-20180426-4.develop.com/ClassicEDO/api/template/GetStudentCourses.aspx";
		  //StringBuilder sb = new StringBuilder();	
		  //sb.append("UserId=5233217000029");
		
		  //String[] userId={"UserId"};
		  //String[] value= {"5233217000029"};
		  
		  //HttpResponse response = netService.sendHttpRequestWithParamsGet(requestUrl, userId, value);
		  
		  //InputStream inputStream =  response.getEntity().getContent();
		  
		  
		  //System.out.println(inputStream);
		  
		  //String uidExpected = "UId=008000000003";
		  //netService.checkResponse(response,uidExpected);
		  
		  
		//enableProxy();
		//webDriver.startProxyServer();
		//webDriver.printProxyOutputToFile();
			
		  CloseableHttpClient httpclient = HttpClients.createDefault();
		  
		  HttpGet httpget = new HttpGet("https://edux.qa.com/ClassicEDO/api/template/GetStudentCourses.aspx?UserId=5233217000029"); // Internal
		  //HttpGet httpget = new HttpGet("https://ed.engdis.com/ClassicEDO/api/template/GetStudentCourses.aspx?UserId=52341070000006"); // External
		  
		  
		  CloseableHttpResponse response = httpclient.execute(httpget);
		  ResponseHandler<String> handler = new BasicResponseHandler();
		  String body = handler.handleResponse(response);
		  boolean exists = body.contains("CourseId=");
		  	assertEquals("Response body text not match", true, exists);
		  
		  //System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
		  //System.out.println(response);

		 // String body = httpclient.execute(httpget, handler);
		  
		  
		  //HttpEntity entity = response.getEntity();
		 // String a = EntityUtils.toString(response.getEntity());
		    
		  //InputStream instream = entity.getContent();
		        
		  
		   //System.out.println(instream);
		   //instream.close();
		  //System.out.println(a);
		  
		 /* 
		  XMLReader myReader = XMLReaderFactory.createXMLReader();
		  myReader.setContentHandler((ContentHandler) handler);
		  myReader.parse(new InputSource(new URL(body).openStream()));
		  
		  */
		  
		  System.out.println(body);
		  response.close();
		  
		 
	}

	

}
