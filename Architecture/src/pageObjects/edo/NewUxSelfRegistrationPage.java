package pageObjects.edo;

import java.util.List;

import org.apache.tools.ant.taskdefs.Sleep;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.security.core.userdetails.UserDetails;

import Enums.ByTypes;
import drivers.GenericWebDriver;
import pageObjects.EdoLoginPage;
import pageObjects.GenericPage;
import services.TestResultService;


public class NewUxSelfRegistrationPage extends GenericPage {

	
	private static final String TITLE = "Registration";
	private static final String INSTRUCTION = "Please complete the form below. Required fields are marked with an asterisk (*).";
	private static final String [] LABELS = {"User Name", "First Name", "Last Name", "Password", "Confirm Password", "Gender", "E-mail Address", "Telephone", ""};
	
	public NewUxSelfRegistrationPage(GenericWebDriver webDriver,
			TestResultService testResultService) throws Exception {
		super(webDriver, testResultService);
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
	
	public void verifyFormTitle() throws Exception{
		try {
			String actualTitle = webDriver.waitForElement("utils__h1", ByTypes.className, "Get form title").getText();
			testResultService.assertEquals(TITLE, actualTitle, "Form title is not valid");
		}catch (Exception e) {
			testResultService.addFailTest("Incorrect form title in self registration pop-up "+e.getMessage(), true, true);
		}catch (AssertionError e) {
			testResultService.addFailTest("Incorrect form title in self registration pop-up "+e.getMessage(), true, true);
		}
		
	}
	
	public void verifyFormInstruction() throws Exception{
		String actualTitle = webDriver.waitForElement("//table[@class='instructions']//td", ByTypes.className, "Get form instruction").getText();
		testResultService.assertEquals(INSTRUCTION, actualTitle, "Form instruction is not valid");
		
	}
	
	public void verifyFormFieldsLabels() throws Exception {
		
		for (int i = 0; i < getFieldsLabelsAsArray().length; i++) {
			testResultService.assertEquals(LABELS[i], getFieldsLabelsAsArray()[i], "Field Label is not valid");
		}
	}
	
	private String [] getFieldsLabelsAsArray() throws Exception {
		
		List<WebElement> labelElements = webDriver.getChildElementsByXpath(webDriver.waitForElement("//table[@class='stext']/tbody", ByTypes.xpath), "//tr/td[2]");
		String [] labels= new String[labelElements.size()];
		
		for (int i = 0; i < labelElements.size(); i++) {
			labels[i] = labelElements.get(i).getText().trim();
			
		}
		
		return labels;
	}
	
	public String [] fillRegisterForm(String userName) throws Exception {
		
		String [] userDetails = {userName,userName+"FN",userName+"LN","12345",userName+"@edusoft.co.il","12345678"} ;
		
		enterStudentRegUserName(userName);
		enterStudentRegFirstName(userDetails[1]);
		enterStudentRegLastName(userDetails[2]);
		enterStudentRegPassword(userDetails[3]);
		enterStudentEmail(userDetails[4]);
		enterStudentTelephone(userDetails[5]);
						
		return userDetails;
		
		
	}
	
	public void enterStudentRegUserName(String userName) throws Exception {
		webDriver.waitForElement("UserName", ByTypes.id).sendKeys(userName);
			}

	public void enterStudentRegFirstName(String fName) throws Exception {
		webDriver.waitForElement("FirstName", ByTypes.id).sendKeys(fName);
		
	}

	public void enterStudentRegLastName (String lName) throws Exception {
		webDriver.waitForElement("LastName", ByTypes.id).sendKeys(lName);
		
	}

	public void enterStudentRegPassword(String password) throws Exception {
		webDriver.waitForElement("Password", ByTypes.id).sendKeys(password);
		webDriver.waitForElement("ConfirmPassword", ByTypes.id).sendKeys(password);
	
	}

	public void enterStudentEmail(String email) throws Exception {
		webDriver.waitForElement("Email", ByTypes.id).sendKeys(email);
		
	}
	
	public void enterStudentTelephone(String telephone) throws Exception {
		webDriver.waitForElement("Telephone", ByTypes.id).sendKeys(telephone);
		
	}
	
	public void clickOnRegister() throws Exception {
		webDriver.waitForElement("Submit", ByTypes.id).click();
		
	}
	
	
	public void close() throws Exception{
		webDriver.closeNewTab(1);
		webDriver.switchToMainWindow();
		
	}
	
	
		
	
}
