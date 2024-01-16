package services;

import au.com.bytecode.opencsv.CSVWriter;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import jcifs.smb.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.io.FileUtils;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.json.JSONObject;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathExpressionException;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.*;

@Service
public class TextService extends GenericService {

	@Autowired
	protected NetService netService;
	
	@Autowired
	Reporter reporter;
	
	private static final com.jayway.jsonpath.Configuration configuration = com.jayway.jsonpath.Configuration.builder()
            .jsonProvider(new JacksonJsonNodeJsonProvider())
            .mappingProvider(new JacksonMappingProvider())
            .build();

	Scanner scanner;

	public TextService() {
	}

	public String getTextFromFile(String filePath, Charset encoding)
			throws Exception {
		byte[] encoded = null;
		try {
			encoded = Files.readAllBytes(Paths.get(filePath));

		} catch (IOException e) {
			report.report(e.toString());

		} finally {

		}
		return new String(encoded, encoding);
	}

	public String getFirstCharsFromCsv(int charsNumber, String filePath)
			throws Exception {
		String str = getTextFromFile(filePath, Charset.defaultCharset());
		str = str.substring(0, charsNumber);
		return str;
	}

	public List<String[]> getStr2dimArrFromCsv(String filePath)
			throws Exception {
		return getStr2dimArrFromCsv(filePath, false);
	}

	public List<String[]> getStr2dimArrFromCsv(String filePath, boolean useSmb)
			throws Exception {
		List<String[]> list = new ArrayList<String[]>();

		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		String[] str = null;

		try {

			if (useSmb) {
				SmbFile smbFile = new SmbFile(filePath,
						netService.getDomainAuth());
				br = new BufferedReader(new InputStreamReader(
						new SmbFileInputStream(smbFile)));
			} else {
				br = new BufferedReader(new FileReader(filePath));

			}

			while ((line = br.readLine()) != null) {
				str = line.split(cvsSplitBy);
				list.add(str);
			}

		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			return list;
		}

	}

	public List<String> getListFromCsv(String filePath, int colIndex)
			throws Exception {
		List<String> list = new ArrayList<String>();

		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		String[] str = null;

		try {

			br = new BufferedReader(new FileReader(filePath));
			while ((line = br.readLine()) != null) {
				str = line.split(cvsSplitBy);
				list.add(str[colIndex]);
			}

		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {

		}
		br.close();
		return list;

	}

	public String[] getStrArrayFromCsv(String filePath, int index)
			throws Exception {
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		String[] strArr = new String[700];
		String[] str = null;
		int i = 0;
		ArrayList<String> list = null;
		try {

			br = new BufferedReader(new FileReader(filePath));
			while ((line = br.readLine()) != null) {

				// use comma as separator
				str = line.split(cvsSplitBy);
				strArr[i] = str[index];
				// System.out.println(strArr[i]);
				i++;

			}

		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			return strArr;
		}
	}

	public void setClipboardText(String text) throws Exception {
		StringSelection selection = new StringSelection(text);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(selection, selection);
	}

	public String[] splitStringToArray(String str, String ignorChars) {
		String[] result = null;
		try {
			// System.out.println("Splitting string: " + str + " to array."
			// + System.currentTimeMillis());
			result = str.split("(" + ignorChars + ")");
		} catch (Exception e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
		}
		return result;
	}

	public int[] splitStringToIntArray(String str, String ignorChars) {
		String[] result = null;
		try {
			// System.out.println("Splitting string: " + str + " to array."
			// + System.currentTimeMillis());
			result = str.split("(" + ignorChars + ")");
		} catch (Exception e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
		}
		int[] intArr = new int[result.length];
		for (int i = 0; i < result.length; i++) {
			intArr[i] = Integer.parseInt(result[i]);
		}
		return intArr;
	}

	public String[] splitStringToArray(String str) {
		return splitStringToArray(str, "?!^");
	}

	public void typeText(String text) throws Exception {
		Robot robot = new Robot();
		byte[] bytes = text.getBytes();
		for (byte b : bytes) {
			int code = b;
			// keycode only handles [A-Z] (which is ASCII decimal [65-90])
			if (code > 96 && code < 123) {
				code = code - 32;
				robot.delay(100);
				robot.keyPress(code);
				robot.keyRelease(code);
			} else if (code == 58) {
				robot.keyPress(KeyEvent.VK_SHIFT);
				robot.keyPress(KeyEvent.VK_SEMICOLON);
				robot.keyRelease(KeyEvent.VK_SEMICOLON);
				robot.keyRelease(KeyEvent.VK_SHIFT);
			} else if (code == 92) {
				robot.keyPress(KeyEvent.VK_BACK_SLASH);
				robot.keyRelease(KeyEvent.VK_BACK_SLASH);
			} else if (code == 46) {
				robot.keyPress(KeyEvent.VK_PERIOD);
				robot.keyRelease(KeyEvent.VK_PERIOD);
			} else if (code == 45) {
				robot.keyPress(KeyEvent.VK_MINUS);
				robot.keyRelease(KeyEvent.VK_MINUS);
			}

		}
	}

	public boolean searchForTextInFile(String text, URL url) throws Exception {
		report.report("Text to find is: " + text);
		boolean found = false;
		scanner = new Scanner(url.openStream());
		try {
			while (scanner.hasNextLine()) {
				String lineTex = scanner.nextLine();
				// System.out.println(scanner.nextLine());
				// report.report(scanner.nextLine());
				if (lineTex.contains(text)) {
					found = true;
					break;
				}
			}
		} catch (Exception e) {
			report.report("Text: " + text + " was not found");
			e.printStackTrace();
		}
		if (found == false) {
			report.report("Text: " + text + " was not found");
		}
		return found;

	}

	public String resolveAprostophes(String item) {
		if (!item.contains("'")) {
			return "'" + item + "'";
		}
		StringBuilder finalString = new StringBuilder();
		finalString.append("concat('");
		finalString.append(item.replace("'", "',\"'\",'"));
		finalString.append("')");
		return finalString.toString();
	}
	
	public String escapeApostrophes(String text) {
		if (!text.contains("'")) {
			return text;
		}
		return text.replace("'", "\'");
	}

	public String printStringArray(String[] str) {
		return printStringArray(str, "|");
	}

	public String printStringArray(String[] str, String seperator) {
		String output = "";
		for (int i = 0; i < str.length; i++) {
			output = output + seperator + str[i];
		}
		// System.out.println("Strings are:" + output);
		reporter.report("String are:" + output);
		return output;
	}

	public String getLineFromTextFile(File file, String textToFind)
			throws IOException {
		String textLine = null;
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line;
		while ((line = br.readLine()) != null) {
			if (line.contains(textToFind)) {
				textLine = line;
				// System.out.println("line found:" + line);
				break;
			}

		}
		br.close();
		if (textLine == null) {
			System.out.println("Line with text: " + textToFind
					+ " was not found");
		}

		return textLine;
	}

	public String[] getHtmlElementFromHtmlFile(String xpathToFind,
			String fileContent) throws ParserConfigurationException,
			XPathExpressionException, XPatherException {
		TagNode tagNode = new HtmlCleaner().clean(fileContent);
		Object[] segments = tagNode.evaluateXPath(xpathToFind);
		String[] arr = new String[segments.length];
		for (int i = 0; i < segments.length; i++) {
			TagNode segment = (TagNode) segments[i];
			System.out.println(segment.getText());
			arr[i] = segment.getText().toString();
			System.out.println();
		}
		return arr;
	}

	public boolean checkIfFileExist(String path) {
		File file = new File(path);
		if (!file.exists()) {
			System.out.println("File: " + path + " was not found");
			return false;
		} else
			System.out.println("File exist");
		return true;

	}

	public String[] trimLowerCaseAndRemoveChars(String[] strArr) {
		for (int i = 0; i < strArr.length; i++) {
			strArr[i] = strArr[i].trim();
			strArr[i] = strArr[i].toLowerCase();
			strArr[i] = strArr[i].replace("?", "");
			strArr[i] = strArr[i].replace("!", "");
			strArr[i] = strArr[i].replace(",", "");
			strArr[i] = strArr[i].replace(".", "");
			strArr[i] = strArr[i].replace("-", " ");
			strArr[i] = strArr[i].replace(";", "");
			strArr[i] = strArr[i].replace("\\", "");
			strArr[i] = strArr[i].replace("\"", "");
			strArr[i] = strArr[i].trim();
		}

		return strArr;
	}

	public void writeArrayistToCSVFile(String path, List<String[]> list)
			throws IOException {
		String csv = path;
		CSVWriter writer = new CSVWriter(new FileWriter(csv), ',',
				CSVWriter.NO_QUOTE_CHARACTER);
		writer.writeAll(list);

		writer.close();
	}

	public void writeListToHtmlFile(List<String[]> list, boolean isSmb,
			String path) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(baos);
		out.writeBytes("<html>");

		out.writeBytes(getTextFromFile("files/htmlFiles/head",
				Charset.defaultCharset()));
		out.writeBytes(getTextFromFile("files/htmlFiles/pageTitle",
				Charset.defaultCharset()));
		
		out.writeBytes("<body>");

		for (int i = 0; i < list.size(); i++) {
			// out.writeBytes("<p>");
			out.writeBytes(list.get(i)[0]);
			// if (list.get(i).length > 1) {
			// out.writeBytes(list.get(i)[1]);
			// }

			// out.writeBytes("</p>");
			// out.writeBytes("<br>");
		}
		
		out.writeBytes(getTextFromFile("files/htmlFiles/JqueryCollapse.txt", Charset.defaultCharset()));
		
		out.writeBytes("</body></html>");
		
		byte[] bytes = baos.toByteArray();

		if (isSmb) {
			//SmbFile smbFile = new SmbFile(path, netService.getAuth());
			SmbFile smbFile = new SmbFile(path, netService.getDomainAuth());
			SmbFileOutputStream output = new SmbFileOutputStream(smbFile);
			output.write(bytes);
		} else {
			
			/*byte[] bytes = baos.toByteArray();
			File file = new File(newPath);
			FileOutputStream output = new FileOutputStream(file);
			output.write(bytes);*/
			
			try {
			File file = new File(path);
			FileOutputStream output = new FileOutputStream(file);
			output.write(bytes);
			
			}catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public void writeListToSmbFile(String path, List<String> list,
			NtlmPasswordAuthentication auth) throws IOException {
		
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream out = new DataOutputStream(baos);
			String nl = System.getProperty("line.separator");
			for (String element : list) {
				// out.writeUTF(element);
				out.writeBytes(element);
				out.writeBytes(nl);
			}
			byte[] bytes = baos.toByteArray();

			writeBtyesToSmbFIle(path, auth, bytes);
		} catch (SmbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
		
	public File writeListToSmbFile(String path, String testName, List<String> list, boolean useSMB) throws Exception {
		//NtlmAuthenticator auth = new NtlmAuthenticator("edusoft2k\\auto2", "!q2w3e4r1");
		
	//	useSMB=true;
	//	path = "smb://10.1.0.213//ToolsAndResources/";
		String newPath="";
		File file = null;
		
		if(useSMB) {
			writeListToSmbFile(path +"/"+ testName, list, netService.getDomainAuth());	
			}
		else {

			if (path.toLowerCase().contains("smb")){
				String[] buildPath = path.split("//");
				newPath = "\\";
				for(int i=1;i<buildPath.length;i++) {
					newPath=newPath+"\\"+buildPath[i];
				}
			}else{
			newPath = path;
			}

			newPath+="\\"+testName;

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream out = new DataOutputStream(baos);
			String nl = System.getProperty("line.separator");
			
			for (int i = 0; i < list.size(); i++) {
				out.writeBytes(list.get(i));
				out.writeBytes(nl);
				}
			
			byte[] bytes = baos.toByteArray();
			file = new File(newPath);
			FileOutputStream output = new FileOutputStream(file);
			output.write(bytes);
			//writeListToHtmlFile(list), false, path);
			}
		return file;
	}

	public void writeListToFile(String path, String testName, List<String> list) throws Exception {


			String targetPath = path +"\\"+ testName;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream out = new DataOutputStream(baos);
			String nl = System.getProperty("line.separator");

			for (int i = 0; i < list.size(); i++) {
				out.writeBytes(list.get(i));
				out.writeBytes(nl);
			}

			byte[] bytes = baos.toByteArray();
			File file = new File(targetPath);
			FileOutputStream output = new FileOutputStream(file);
			output.write(bytes);
	}

	private void writeBtyesToSmbFIle(String path,
			NtlmPasswordAuthentication auth, byte[] bytes)
			throws MalformedURLException, SmbException, UnknownHostException,
			IOException {
		SmbFile smbFile = new SmbFile(path, auth);
		SmbFileOutputStream output = new SmbFileOutputStream(smbFile);
		output.write(bytes);
		output.close();
	}

	public List<String> getListFromLogEntries(LogEntries logEntries) {
		return getListFromLogEntries(logEntries, null, true);
	}

	public List<String> getListFromLogEntries(LogEntries logEntries,
			String filter, boolean useFilter) {
		List<String> strList = new ArrayList<String>();
		for (LogEntry entry : logEntries) {

			// String str = new String { entry.getMessage(),
			// String.valueOf(entry.getTimestamp()),
			// entry.getLevel().toString() };

			String str = entry.getMessage() + " "
					+ String.valueOf(entry.getTimestamp()) + " "
					+ entry.getLevel().toString();
			/*if (filter != null && entry.getMessage().contains(filter)
					|| useFilter == false && filter == null) {
				strList.add(str);
			}
			*/ //original
			
			if (filter != null && entry.getMessage().contains(filter)
					&& useFilter == true) {
				strList.add(str);
			} else if (useFilter == false) { 
				strList.add(str);
			}
			
			
		}
		return strList;
	}

	public void copyFileToFolder(String sourcePath, String destinationPath)
			throws IOException {
		copyFileToFolder(sourcePath, destinationPath, false, null);

	}

	public void copyFileToFolder(String sourcePath, String destinationPath,
			boolean useSMB, NtlmPasswordAuthentication auth) throws IOException {
		copyFileToFolder(sourcePath, destinationPath, useSMB, auth, auth);
	}

	public void copyFileToFolder(String sourcePath, String destinationPath,
			boolean useSMB, NtlmPasswordAuthentication auth,
			NtlmPasswordAuthentication sourceAuth) throws IOException {
		
		try {

			if (useSMB == true) {
				
				SmbFile smbFile = new SmbFile(destinationPath, auth);
				SmbFileOutputStream outputStream = new SmbFileOutputStream(smbFile);
				
				/*if (sourceAuth.equals(auth) && useSMB == false) {
					// File file=new File(sourcePath);
					// BufferedInputStream inputStream=new
					// BufferedInputStream(file);
					// FileInputStream inputStream=file.
					Path path = Paths.get(sourcePath);
					outputStream.write(Files.readAllBytes(path));
				} else {*/
				
				SmbFile sourceFile = new SmbFile(sourcePath, sourceAuth);
				outputStream.write(getBytes(sourceFile.getInputStream()));
				outputStream.close();
				
			} else {
				FileUtils.copyFileToDirectory(new File(sourcePath), new File(destinationPath));
								
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	public static byte[] getBytes(InputStream is) throws IOException {

		int len;
		int size = 1024;
		byte[] buf;

		if (is instanceof ByteArrayInputStream) {
			size = is.available();
			buf = new byte[size];
			len = is.read(buf, 0, size);
		} else {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			buf = new byte[size];
			while ((len = is.read(buf, 0, size)) != -1)
				bos.write(buf, 0, len);
			buf = bos.toByteArray();
		}
		return buf;
	}

	public String getTextForText(int chars) {

		String lipsum = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum";
		return lipsum.substring(0, chars);
	}

	public void writeListToCsvFile(String path, List<String> list)
			throws IOException {
		writeListToCsvFile(path, list, false);
	}

	public void writeArrayListToCSVFile(String path, List<String[]> list,
			boolean useSMB) throws IOException {
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(baos);
		String nl = System.getProperty("line.separator");
		for (String[] element : list) {
			for (int i = 0; i < element.length; i++) {
				// out.writeUTF(element);
				out.writeBytes(element[i]);
				if (i<element.length-1) {
					out.writeBytes(",");
				}
			}
			out.writeBytes(nl);
		}
		byte[] bytes = baos.toByteArray();
		if(useSMB) {
			writeBtyesToSmbFIle(path, netService.getDomainAuth(), bytes);
			path=path.replace("smb://", "http://");
			reporter.addLink(path,"performance log");
		}else {
			String[] buildPath = path.split("//");
			String newPath = "\\";
			for(int i=1;i<buildPath.length;i++) {
				newPath=newPath+"\\"+buildPath[i];
			}
			newPath+="\\"+testName;
			
			File file = new File(newPath);
			FileOutputStream outputStream = new FileOutputStream(file, useSMB);
			outputStream.write(bytes);
			path=path.replace("smb://", "http://");
			reporter.addLink(path,"performance log");
		}
		
		// SmbFile smbFile = new SmbFile(path, auth);
		// SmbFileOutputStream output = new SmbFileOutputStream(smbFile);
		// output.write(bytes);

//		
		
	}

	public void writeListToCsvFile(String path, List<String> list,
			boolean append) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(baos);
		String nl = System.getProperty("line.separator");
		for (String element : list) {
			// out.writeUTF(element);
			out.writeBytes(element);
			out.writeBytes(nl);
		}
		byte[] bytes = baos.toByteArray();

		// SmbFile smbFile = new SmbFile(path, auth);
		// SmbFileOutputStream output = new SmbFileOutputStream(smbFile);
		// output.write(bytes);

		File file = new File(path);

		FileOutputStream outputStream = new FileOutputStream(file, append);
		outputStream.write(bytes);

	}

	public void writeTxtFileWithText(String path, String text)
			throws IOException {
		File file = new File(path);
		FileOutputStream outputStream = new FileOutputStream(file);
		outputStream.write(text.getBytes());
		outputStream.close();
	}

	public List<String> readTxtFileLineByLine(String path){

		BufferedReader reader;
		List<String> file = new ArrayList<String>();

		try {
			reader = new BufferedReader(new FileReader(path));
			String line = reader.readLine();

			while (line != null) {
				System.out.println(line);
				file.add(line);
				// read next line
				line = reader.readLine();

			}

			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}

	public void writeSmbFileWithText(String smbPath, String text)
			throws IOException {
		SmbFile file = new SmbFile(smbPath, netService.getDomainAuth());
		SmbFileOutputStream outputStream = new SmbFileOutputStream(file);
		outputStream.write(text.getBytes());
		outputStream.close();
	}
	
	public void writeTextToFileWithSeveralLines(String filePath, List<String> text) {
	//	 File folder = new File("C:\\Users\\igorqt\\eclipse-workspace IDE\\EduAutomation___\\files\\ImportStudents");
	  //      if (folder.exists() == false) {
	  //          folder.mkdirs();
	  //      }
	       // String folderPath = "C:\\Users\\igorqt\\eclipse-workspace IDE\\EduAutomation___\\files\\ImportStudents";
	        //String filePath = folderPath + "\\" + fileName;
	        File file = new File(filePath);
	        if (!file.exists()) {
	            try {
	                file.createNewFile();
	            } catch (IOException e) {
	                throw new RuntimeException(e);
	            }
	        }
	        try {
	            FileWriter fw = new FileWriter(file);
	            BufferedWriter bw = new BufferedWriter(fw);
	           // String toFile = "";
	            for(String toFile:text) {
	            	bw.write(toFile);
	                bw.newLine();
	            }
	            bw.close();

	        } catch (IOException e) {
	            throw new RuntimeException(e);
	        }

	    }
	
		
	public String getSmbFileContent(String path) throws IOException {
		
		SmbFile smbFile = new SmbFile(path, netService.getDomainAuth());
		String smbContent = readFileContent(smbFile);
		
		/*path = path.replace("smb:", "");
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, Charset.defaultCharset());*/
		
		return smbContent;

	}
	
	public void modifySmbXmlFile(String smbPath, List<String[]> channgeSet) throws Exception {
		
		//channgeSet array format = {xPath, attributeName, new value}
		
		SmbFile smbFile = new SmbFile(smbPath, netService.getDomainAuth());
		InputStream inputStream = new SmbFileInputStream(smbFile);
					    
		try {
			
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	
			docFactory.setIgnoringElementContentWhitespace(true);
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(inputStream);
			inputStream.close();
				
			for (int i = 0; i < channgeSet.size(); i++) {
				NodeList nodeList = netService.getNodesFromXml(channgeSet.get(i)[0],doc);
				Node nodeAttr = null;
				if (nodeList != null && nodeList.getLength() == 1) {
					nodeAttr = nodeList.item(0).getAttributes().getNamedItem(channgeSet.get(i)[1]);

				} else testResultService.addFailTest("XML element not found or not unique", true, false);
		
				// update node attribute
				nodeAttr.setTextContent(channgeSet.get(i)[2]);
			}
			
			
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			
			//transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		   			
			DOMSource source = new DOMSource(doc);
					
			SmbFileOutputStream outputStream = new SmbFileOutputStream(smbFile);
						
			StreamResult result = new StreamResult(outputStream);					
			transformer.transform(source, result);
			outputStream.close();

		   } catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		   } catch (TransformerException tfe) {
			tfe.printStackTrace();
		   } catch (IOException ioe) {
			ioe.printStackTrace();
		   } catch (SAXException sae) {
			sae.printStackTrace();
		   }
	    

	}
	
	public String getWebConfigAppSettingsValuByKey(String buildPath, String key) throws Exception {
	
		String path = buildPath + "web.config";
		SmbFile smbFile = new SmbFile(path, netService.getDomainAuth());
		InputStream inputStream = new SmbFileInputStream(smbFile);
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		docFactory.setIgnoringElementContentWhitespace(true);
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(inputStream);
		inputStream.close();
				
		NodeList nl = netService.getNodesFromXml("//add[@key='"+key+"']", doc);
		String value = nl.item(0).getAttributes().getNamedItem("value").getNodeValue();
	
		return value;
		
	}
	
	public void getCreateFolderInPath(String path, String subFolder)
			throws Exception {
		SmbFile smbFile = new SmbFile(path + "\\" + subFolder,
				netService.getDomainAuth());
		if (smbFile.exists() == false) {
			System.out.println("Create folder");
			smbFile.mkdir();
		} else {
			System.out.println("folder existed");
		}
	}
	
	public boolean getDeleteFolderInPath(String path, String folderToDelete)
			throws Exception {
		
		Boolean removed = false;
		
		SmbFile smbFile = new SmbFile(path + "\\" + folderToDelete,
				netService.getDomainAuth());
		if (smbFile.exists() == false) {
			
			System.out.println("Folder does not exist!");
			
		} else if (smbFile.getPath() != path){
			
			smbFile.delete();
			removed = true;
		}
		return removed;
	}
	
	
	public boolean deleteFileIPath(String path) throws Exception {
			
			try  
			{         
				File f= new File(path);           //file to be delete  
				if(f.delete())                      //returns Boolean value  
				{  
					System.out.println(f.getName() + " deleted");   //getting and printing the file name 
					return true;
				}  
				else  
				{  
					System.out.println("failed"); 
					return false;
				}  
			}  
			catch(Exception e)  
			{  
				e.printStackTrace();
				return false;
			}
	}
	
	//@SuppressWarnings("unchecked")
	public void changeExpirationTime(int time) {
		 JSONObject jsonObj = new JSONObject();
			try {
					
			jsonObj = netService.getJsonObject(PageHelperService.edUiServicePath, "appsettings");
			JSONObject jwt = (JSONObject) jsonObj.getJSONObject("Jwt");
			System.out.println(jwt);
			jwt.put("ExpirationInMinutes", time);
			jsonObj.put("Jwt", jwt);
			writeToJsonByProtocol(PageHelperService.edUiServicePath+"appsettings.json", jsonObj, netService.getDomainAuth());
				
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void updateJsonFileByKeyValue(String path, String fileName,String blockName, String key, String value) {
		 JSONObject jsonObj = new JSONObject();
		// JSONObject blockObject= new JSONObject(blockName);
		 
			try {
					
			jsonObj = netService.getJsonObject(path,fileName,false);
			JSONObject blockObject = (JSONObject) jsonObj.getJSONObject(blockName);
			//blockObject = (JSONObject) jsonObj.getJSONObject(blockName);
			blockObject.put(key, value);
			jsonObj.put(blockName, blockObject);
			writeToJsonByProtocol(path + fileName +".json", jsonObj, netService.getDomainAuth(), false);
		
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void updateFileWithJsonPath (String path, String selector, Object value) {
		
	    File file = new File(path);
	    //report.report(String.format("Path to the file: %s", path));
	   
        DocumentContext data;
        try {
            data = JsonPath.using(configuration).parse(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Object result = data.set(selector, value).json();

        try (PrintWriter printWriter = new PrintWriter(file)){
            printWriter.write(result.toString());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
	    
	}
	
	public Object readFileWithJsonPath (String path, String selector) {

		File file = new File(path);
		//report.report(String.format("Path to the file: %s", path));

		Object result;
		DocumentContext data;
		try {
			data = JsonPath.using(configuration).parse(file);
			result = data.read(selector);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return result;

	}
	
	public void writeToJsonByProtocol(String path,JSONObject json,NtlmPasswordAuthentication auth) {
		writeToJsonByProtocol( path, json, auth, true);
	}
	
	public void writeToJsonByProtocol(String path,JSONObject json,NtlmPasswordAuthentication auth, boolean useSMB) {
		 
		String jsonStr = json.toString();
		try {
			if(useSMB) {
				 SmbFile smbFile = new SmbFile(path,auth);
				 SmbFileOutputStream smbfos = new SmbFileOutputStream(smbFile);
				 smbfos.write(jsonStr.getBytes());
				 System.out.println("Expiration time of session time out is changed !");
			}else {
				String[] buildPath = path.split("/");
				String newPath = "\\";
				for(int i=2;i<buildPath.length;i++) {
					newPath=newPath+"\\"+buildPath[i];
				}
				//newPath+="\\"+testName;
				File file = new File(newPath);
				FileOutputStream output = new FileOutputStream(file);
				output.write(jsonStr.getBytes());
			}
			 
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public String readFileContent(File file) {
		BufferedReader reader = null;
		StringBuilder stringBuilder = new StringBuilder();
		try {
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(file)));
		} catch(Exception e) {
			e.getMessage();
		}
		String lineReader = null;
		{
			try {
				while ((lineReader = reader.readLine()) != null) {
					String newLine = lineReader.trim();			
					boolean comment = newLine.startsWith("//");
					//boolean comment2 = lineReader.contains("//");
					
					if(comment!=true) {
					stringBuilder.append(lineReader).append("\n");
					}
				}
			} catch (IOException exception) {
				exception.printStackTrace();
			} finally {
				try {
					reader.close();
				} catch (IOException ex) {
					// Logger.getLogger(SmbConnect.class.getName()).log(Level.SEVERE,
					// null, ex);
					System.out.println(ex.toString());
				}
			}
		}
		return stringBuilder.toString();		
	}
	
	
	public String readFileContent(SmbFile sFile) {
		BufferedReader reader = null;
		StringBuilder stringBuilder = new StringBuilder();
		try {
			reader = new BufferedReader(new InputStreamReader(
					new SmbFileInputStream(sFile)));
		} catch (SmbException ex) {
			System.out.println(ex.toString());
		} catch (MalformedURLException ex) {
			// Logger.getLogger(SmbConnect.class.getName()).log(Level.SEVERE,
			// null, ex);
			System.out.println(ex.toString());
		} catch (UnknownHostException ex) {
			// Logger.getLogger(SmbConnect.class.getName()).log(Level.SEVERE,
			// null, ex);
			System.out.println(ex.toString());
		}
		String lineReader = null;
		{
			try {
				while ((lineReader = reader.readLine()) != null) {
					String newLine = lineReader.trim();			
					boolean comment = newLine.startsWith("//");
					//boolean comment2 = lineReader.contains("//");
					
					if(comment!=true) {
					stringBuilder.append(lineReader).append("\n");
					}
				}
			} catch (IOException exception) {
				exception.printStackTrace();
			} finally {
				try {
					reader.close();
				} catch (IOException ex) {
					// Logger.getLogger(SmbConnect.class.getName()).log(Level.SEVERE,
					// null, ex);
					System.out.println(ex.toString());
				}
			}
		}
		return stringBuilder.toString();
	}

	public static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		long factor = (long) Math.pow(10, places);
		value = value * factor;
		// long tmp = Math.round(value);
		double tmp = Math.floor(value);
		return (double) tmp / factor;
	}
	
	public String updateTime(String originalTime, String action, String unitToChange, int value) throws ParseException {
		DateTimeFormatter formatter;
		if (originalTime.length() == 23) {
			formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"); 
		} else if (originalTime.length() == 16) {
			formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"); 
		} else if (originalTime.length() == 19) {
			formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 
		} else if (originalTime.length() == 10) {
			formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); 
		} else {
			formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SS"); 
		}
		LocalDateTime dateTime = LocalDateTime.parse(originalTime, formatter);
		
		switch (unitToChange.toLowerCase()) {
			case "hour":
				if (action.toLowerCase().equals("add")) {
					dateTime = dateTime.plusHours(value);
				} else if (action.toLowerCase().equals("reduce")) {
					dateTime = dateTime.minusHours(value);
				}
				break;
			case "minute":
				if (action.toLowerCase().equals("add")) {
					dateTime = dateTime.plusMinutes(value);
				} else if (action.toLowerCase().equals("reduce")) {
					dateTime = dateTime.minusMinutes(value);
				}
				break;
				
			case "second":
				if (action.toLowerCase().equals("add")) {
					dateTime = dateTime.plusSeconds(value);
				} else if (action.toLowerCase().equals("reduce")) {
					dateTime = dateTime.minusSeconds(value);
				}
				break;
			case "day":
				if (action.toLowerCase().equals("add")) {
					dateTime = dateTime.plusDays(value);
				} else if (action.toLowerCase().equals("reduce")) {
					dateTime = dateTime.minusDays(value);
				}	
		}
		String updatedTime = dateTime.format(formatter);
		return updatedTime;
	}
	
	public String convertDateToDifferentFormat(String date, String initDateFormat, String endDateFormat) throws ParseException {
		Date initDate = new SimpleDateFormat(initDateFormat).parse(date);
	    SimpleDateFormat formatter = new SimpleDateFormat(endDateFormat);
	    String parsedDate = formatter.format(initDate);
	    return parsedDate;
	    
		/*try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format); 
			LocalDateTime dateTime = LocalDateTime.parse(date, formatter);
		} catch (Exception e) {
			System.out.println(e);
		}*/
		//return null;
		
	}
	
	public double roundDouble(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = BigDecimal.valueOf(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}

	public int getTimeInSecondsFromTwoDates(String startDate, String endDate) throws ParseException {
		
		//String timeStart = startDate;
		//String timeEnd = endDate;
		if (startDate.contains(" ") && endDate.contains(" ")) {
			startDate = startDate.split(" ")[1];
			startDate = startDate.substring(0,startDate.length()-2);
			endDate = endDate.split(" ")[1];
			endDate = endDate.substring(0,endDate.length()-2);
		} 
	
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	    Date reference = dateFormat.parse("00:00:00");
	    Date date = dateFormat.parse(startDate);
	    long startSeconds = (date.getTime() - reference.getTime()) / 1000L;
	    
	    date = dateFormat.parse(endDate);
	    long endSeconds = (date.getTime() - reference.getTime()) / 1000L;
	    
	    return (int) (endSeconds - startSeconds);
	}
	
	public String getRandomString(int stringLength) {
	    
		String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < stringLength) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
	}
	
	public String readContentFile (String path){

		// Declaring a string variable
		String str = null;
		
		 // File path is passed as parameter
        File file = new File(path);
 
        // Note:  Double backquote is to avoid compiler
        // interpret words
        // like \test as \t (ie. as a escape sequence)
 
        // Creating an object of BufferedReader class
        BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 
       
        // Condition holds true till
        // there is character in a string
        try {
			while ((str = br.readLine()) != null)
			    // Print the string
			    System.out.println(str);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return str;
	}
	
	public static String[] getCSVFileHeaders(File csvFile) {
	    List<String> headers = new ArrayList<>();

	    try (Reader reader = new FileReader(csvFile);
				CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {
	        headers.addAll(parser.getHeaderNames());
	    } catch (IOException e) {
	        e.printStackTrace();
	    }

	    return headers.toArray(new String[headers.size()]);
	}

	public void generateTxtToImportStudents(String[] students, String filePath,String className) {
			List<String> fileListContains = new ArrayList<>();
			fileListContains.add("FirstName	LastName	UserName	Password	Gender	Email	Class");
			for (String student:students) {
				fileListContains.add(student + "	" + student + "	" + student + "	12345	u	" + student + "@mail.com	"+className);
			}
			writeTextToFileWithSeveralLines(filePath, fileListContains);
	}

	public void generateTxtToImportTeachers(String[] teachers, String filePath) {
		List<String> fileListContains = new ArrayList<>();
		fileListContains.add("FirstName	LastName	UserName	Password	Gender	Email");
		for (String student:teachers) {
			fileListContains.add(student + "	" + student + "	" + student + "	12345	u	" + student + "@mail.com");
		}
		writeTextToFileWithSeveralLines(filePath, fileListContains);
	}
	public void generateTxtToImportClass(String[] classes, String filePath) {
		List<String> fileListContains = new ArrayList<>();
	//	fileListContains.add("Class");
		for (int i=0;i<classes.length;i++) {
			fileListContains.add(classes[i]);
		}
		writeTextToFileWithSeveralLines(filePath, fileListContains);
	}
}
