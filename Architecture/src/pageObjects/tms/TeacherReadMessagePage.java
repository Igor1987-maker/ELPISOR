package pageObjects.tms;

import Enums.ByTypes;
import drivers.GenericWebDriver;
import pageObjects.GenericPage;
import services.TestResultService;

public class TeacherReadMessagePage extends GenericPage {

	String mainWin=null;
	
	public TeacherReadMessagePage(GenericWebDriver webDriver,TestResultService testResultService) throws Exception {
		super(webDriver ,testResultService);
		// TODO Auto-generated constructor stub
	}

	@Override
	public GenericPage waitForPageToLoad() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GenericPage OpenPage(String url) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String getStudentAnswerText() throws Exception{
		
		String body = webDriver.waitForElement("//div[@name='stext']", ByTypes.xpath).getText();
		String answer = body.split("A:")[1];
		
		return answer;
	}
	
	public String getBodyMessageFromTeacher() throws Exception{
		String body = webDriver.waitForElement("//div[@name='stext']", ByTypes.xpath).getText();
		return body;
	}
	public String getStudentSubjectAnswerText() throws Exception{
		
		String subject = webDriver.waitForElement("	/html/body/div/table[2]/tbody/tr/td[1]/table[1]/tbody/tr[3]/td[2]", ByTypes.xpath).getText();		
		return subject;
	}

			
	public TeacherWriteMessagePage clickOnReply() throws Exception{
		
		webDriver.waitForElementAndClick("//input[@value='Reply']", ByTypes.xpath);
		
		return new TeacherWriteMessagePage(webDriver,testResultService);
	}
	
	public TmsHomePage clickOnClose() throws Exception{
		
		webDriver.waitForElementAndClick("//input[@value='Close']", ByTypes.xpath);
		
		return new TmsHomePage(webDriver,testResultService);
	}

	
	
}
