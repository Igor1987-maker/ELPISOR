package pageObjects.edo;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.tools.ant.taskdefs.Sleep;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Enums.ByTypes;
import drivers.GenericWebDriver;
import pageObjects.GenericPage;
import services.TestResultService;

public class NewUxCertificatePopUp extends GenericPage {
	
	@FindBy(className = "FileName__newUIFileName___2kTC3")
	public WebElement certificatesTitle;
	
	//@FindBy(linkText = "Open Certificate")
	//public WebElement certificate;//*[@id="WebPalsTextAreaId"]/table/tbody/tr[8]/td[1]/a

	@FindBy(xpath = "//a[text()='Open Certificate']")
	public WebElement certificate;  //*[@id='WebPalsTextAreaId']/table/tbody/tr[8]/td[1]/a
	
	@FindBy(xpath = "//*[@id=\"WebPalsTextAreaId\"]/table/tbody/tr[7]/td[1]")
	public WebElement finalGrade;  //*[@id="WebPalsTextAreaId"]/table/tbody/tr[7]/td[1]
	
	@FindBy(css = "div[class='WebPalsTextArea'] table tbody td:first-child")
	public List<WebElement> allFieldsOfMessage;
	
	
	public NewUxCertificatePopUp(GenericWebDriver webDriver, TestResultService testResultService) throws Exception {
		super(webDriver, testResultService);
		PageFactory.initElements(webDriver.getWebDriver(), this);
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
	
	
	public void checkCertificate() throws Exception {
		
		report.startStep("Switch to internal frame");
			WebElement frame = webDriver.waitForElement("ReadWrite", ByTypes.id, "No element");
			webDriver.switchToFrame(frame);
					
			
			
		report.startStep("Check that student deserve certificate by value of his grade");
			WebElement gradesFinal = allFieldsOfMessage.stream().filter(el->el.getText().contains("Final Grade")).findAny().orElse(null); 
			Thread.sleep(2000);
			//String strGrade = finalGrade.getText();
			String strGrade = gradesFinal.getText();
			String [] arr = strGrade.split(":");
			strGrade = arr[1].trim();
			int grades = Integer.parseInt(strGrade);
			textService.assertTrue("Grades less than expected", grades>48);
		
			
			
		report.startStep("Open certificate and validate him");
			certificate.click();
			Thread.sleep(2000);
			webDriver.switchToNewWindow(3);
			String urlStudent = webDriver.getUrl();
			textService.assertTrue("Certificate not correct", urlStudent.contains("StudentCertificate.aspx"));
			webDriver.getWebDriver().close();
			//webDriver.switchToNewWindow(2);
			//String urlStudent2 = webDriver.getUrl();
		}
	
	public void checkFinalGrade() throws Exception {
		WebElement frame = webDriver.waitForElement("ReadWrite", ByTypes.id, "No element");
		webDriver.switchToFrame(frame);
		webDriver.waitForElementByWebElement(finalGrade, "final grade", false, 10);
		int grades = Integer.parseInt(finalGrade.getText());
		textService.assertTrue("Grades less than expected", grades>55);
	}
	
	

}
