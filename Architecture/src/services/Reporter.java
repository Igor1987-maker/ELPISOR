package services;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("Reporter")
public class Reporter {

	boolean stepStarted;
	boolean failedStep = false;

	private static Reporter reporter = new Reporter();
	ArrayList<String[]> reportLogs = new ArrayList<>();

	@Autowired
	TextService textService;

	static final Logger logger = Logger.getLogger(Reporter.class);

	public static Reporter getInstance() throws IOException {
		PropertyConfigurator.configure("log4j.properties");
		initLogger();

		return reporter;

	}

	// public void init() {
	// reportLogs = new ArrayList<>();
	// }

	public void report(String message, String level) {
		report(message);

	}

	public void report(String message) {
		// append text to list
		reportLogs.add(new String[] { "<p>" + message + "</p>", String.valueOf(System.currentTimeMillis())});
	}

	public void reportFailure(String message) {
		reportLogs.add(new String[] {
				"<p><font color='red'>" + message + "</font></p>",
				String.valueOf(System.currentTimeMillis()) });
		failedStep = true;
	}

//	public void startLevel(String string, EnumReportLevel current place) {
//		reportLogs.add(new String[] { string });
//
//	}

	public void startLevel(String string) {
		reportLogs.add(new String[] { string });

	}

	public void stopLevel() {
		// TODO Auto-generated method stub

	}

	public ArrayList<String[]> getReportLogs() {
		return reportLogs;
	}

	public void writelogger(String text) {

		logger.info(text);

	}

	public static void initLogger() throws IOException {
		// HTMLLayout htmlLayout=new HTMLLayout();
		Objects.Logging.HtmlLogLayout htmlLayout = new Objects.Logging.HtmlLogLayout();

		String pattern = "%r [%t] %-5p %c %x - %m%n";

		// SimpleLayout layout = new SimpleLayout();
		FileAppender appender = new FileAppender(htmlLayout,
				System.getProperty("user.dir") + "/log//current//log.html");

		// LogAppender appender =new LogAppender();
		// appender.setLayout(new HTMLLayout());
		// appender.setLayout(layout);
		logger.addAppender(appender);
	}

	public void addLink(String url) {
		addLink(url, "link");

	}

	public void addLink(String url, String linkText) {
		reportLogs.add(new String[] { "<p><a href=" + url + " target='_blank'>" + linkText
				+ "</a></p>" });

	}
	
	public void addTitle(String title) {
		addTitle(title, "black");
	}

	public void addTitle(String title, String color) {
		reportLogs.add(new String[] { "<h3 style='color:"+color+"'>" + title + "</h3>" });

	}
	
	public void addEntryInIndex(int index, String message){
		reportLogs.add(index, new String[]{message});
	}
	
	public void startStep(String stepName) throws Exception{
		startStep(stepName, true);
	}

	public void startStep(String stepName,boolean finishLastStep) throws Exception {

		if (finishLastStep) {
			if (stepStarted) {
				finishStep();
			}
		}
		String str = textService.getTextFromFile("files/htmlFiles/collapseDiv",
				Charset.defaultCharset());
		str = str.replace("%stepName%", stepName);
		reportLogs.add(new String[] { str });
		stepStarted = true;

	}

	public void finishStep() {
		if (failedStep == true) {
			setFailedStep();
		}

		reportLogs.add(new String[] { "</div></div>" });
		stepStarted = false;

	}

	private void setFailedStep() {
		// TODO Auto-generated method stub
		failedStep = false;
		for (int i = reportLogs.size() - 1; i > 0; i--) {

			if (reportLogs.get(i)[0].contains("collapsible")) {

				String str = reportLogs.get(i)[0];
				str = str.replace("<h1><span><i class=\"fa fa-caret-square-o-down\" aria-hidden=\"true\"></i>", "<h1><span style=\"color:red\"><i class=\"fa fa-exclamation-circle\" aria-hidden=\"true\"></i>");
				
				reportLogs.set(i, new String[] { str });
				break;
			}

		}

	}

	public void endLog() {
		if (stepStarted) {
			finishStep();
		}

	}
}
