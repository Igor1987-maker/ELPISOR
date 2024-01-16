package pageObjects.tms;

import Enums.ByTypes;
import Objects.UserObject;
import drivers.GenericWebDriver;
import pageObjects.GenericPage;
import pageObjects.LoginPage;
import services.TestResultService;

public class TmsLoginPage extends LoginPage {

	public TmsLoginPage(GenericWebDriver webDriver,TestResultService testResultService) throws Exception {
		super(webDriver,testResultService);
		
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
	
	
	public TmsHomePage Login(UserObject userObject)throws Exception{
		webDriver.waitForElement("userName",ByTypes.name,"user name text fields in TMS login").sendKeys(userObject.getUserName());
		webDriver.waitForElement("password", ByTypes.name,"password text field in TMS login").sendKeys(userObject.getPassword());
		webDriver.waitForElement("//input[@value='Login']",ByTypes.xpath).click();
		return new TmsHomePage(webDriver,testResultService);
		
	}
	



}
