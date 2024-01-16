package pageObjects.tms;

import Enums.ByTypes;
import drivers.GenericWebDriver;
import pageObjects.GenericPage;
import services.TestResultService;

public class TeacherWriteMessagePage extends GenericPage {

	String mainWin=null;
	
	public TeacherWriteMessagePage(GenericWebDriver webDriver,TestResultService testResultService) throws Exception {
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
	
	public void enterTextIntoMessageBody(String input) throws Exception{
		webDriver.swithcToFrameAndSendKeys("//body[@id='tinymce']", input, "editorTextArea_ifr");
		
	}
	
	public void enterSubject(String subject) throws Exception{
		
		webDriver.waitForElement("subject", ByTypes.id, "Enter Subject").sendKeys(subject);
		
	}
	
	public TeacherReadMessagePage clickOnSend() throws Exception{
		
		webDriver.waitForElementAndClick("//input[@value='Send']", ByTypes.xpath);
		
		return new TeacherReadMessagePage(webDriver,testResultService);
	}
	
	public TeacherReadMessagePage clickOnCancel() throws Exception{
		
		webDriver.waitForElementAndClick("//input[@value='Cancel']", ByTypes.xpath);
		
		return new TeacherReadMessagePage(webDriver,testResultService);
	}
	
	public String getUserListInMessage() throws Exception{
		webDriver.switchToFrame("toDiv");
		String userList = webDriver.waitForElement("nameList", ByTypes.id).getText();
		String[] list = userList.split(" ");
		return list[0];
	}

}
