package tests.misc;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;



import org.htmlcleaner.XPatherException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Enums.AutoParams;
import Enums.UserType;
import services.AudioService;

public class NonUiTests extends EdusoftBasicTest {

	@Before
	public void setup() throws Exception {
		super.setup();
	}

	@Test
	public void testAudio() throws Exception {

		AudioService audioService = new AudioService();
		audioService.sendSoundToVirtualMic(new File(
				//"files/audioFiles/A1SHHA_3.wav"), 16000);
				"//"+configuration.getGlobalProperties("logserverName")+"/shared/A1SHHA_3.wav"), 16000);
		
		// 8000 for recordings with SRI tool
		// System.out.println("Do not run");
	}

	// @Test
	public void testFailTest() throws Exception {
		// testResultService.addFailTest("aaaaa");

		String str1 = "dog";
		String str2 = "dog";
		testResultService.assertEquals(str1, str2);
		// testResultService.assertEquals("dog", "cat");
		// testResultService.addFailTest("ccccc");

	}

	@Test
	public void updateSlaveStatus() throws Exception, IOException {
		String text = textService.getTextForText(50);
		System.out.println(text);
	}

	@Test
	public void getLastProgress() throws Exception {
		try {
			String[] str = dbService.getClassAndCourseWithLastProgress(
					"autoTeacher", autoInstitution.getInstitutionId(),UserType.SchoolAdmin);
			System.out.println(str[0]);
			System.out.println(str[1]);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testNumberOfSegments() throws Exception,
			ParserConfigurationException, XPatherException {
		String newfileContent = textService.getTextFromFile(
				"D:\\i2srtr_new.js", Charset.defaultCharset());
		
		String oldfileContent = textService.getTextFromFile(
				"D:\\i2srtr_old.js", Charset.defaultCharset());
		String[] arr = textService.getHtmlElementFromHtmlFile(
				"//span[@class='segment']", newfileContent);
		String[] arr2 = textService.getHtmlElementFromHtmlFile(
				"//span[@class='segment']", oldfileContent);
		
		for (int i = 0; i < arr.length; i++) {
			System.out.println("segment "+i+" "+ arr[i]);
		}
		
		for (int i = 0; i < arr.length; i++) {
			System.out.println("segment "+i+" "+ arr2[i]);
		}
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

}
