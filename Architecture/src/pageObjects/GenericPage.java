package pageObjects;

import Objects.GenericTestObject;
import drivers.GenericWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import services.Configuration;
import services.DbService;
import services.Reporter;
import services.TestResultService;

public abstract class GenericPage extends GenericTestObject {
	@Autowired
	protected GenericWebDriver webDriver;
	
//	@Autowired
//	protected Configuration configuration;
	
//	@Autowired
	protected TestResultService  testResultService;
	
//	@Autowired
//	protected AudioService audioService;
	
	protected DbService dbService;
	

	private String sutUrl=null;
	
	protected Configuration configuration;
	
	protected Reporter report;

	
	protected static final Logger logger = LoggerFactory.getLogger(GenericPage.class);
	
	public GenericPage(GenericWebDriver webDriver,TestResultService testResultService) throws Exception{
		this.webDriver=webDriver;
		this.testResultService=testResultService;
		this.report=webDriver.getReporter();
		this.configuration=webDriver.getConfiguration();
		dbService=new DbService();
		
		//ctx = new ClassPathXmlApplicationContext("beans.xml");
		//dbService = (DbService) ctx.getBean("DbService");
//		System.out.println(report.toString());
//		this.dbService=webDriver.getDbService();
//		textService=new TextService();
		
		ctx = new ClassPathXmlApplicationContext("beans.xml");
		report = (services.Reporter) ctx.getBean("Reporter");
		configuration = (Configuration) ctx.getBean("configuration");
		dbService = (DbService) ctx.getBean("DbService");
		
	
	}
	public abstract GenericPage waitForPageToLoad()throws Exception;
	public abstract GenericPage OpenPage(String url)throws Exception;
	public String getSutUrl() {
		return sutUrl;
	}
	public void setSutUrl(String sutUrl) {
		this.sutUrl = sutUrl;
	}
}
