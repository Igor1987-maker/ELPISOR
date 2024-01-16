package pageObjects.edo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Enums.ByTypes;
import drivers.GenericWebDriver;
import pageObjects.GenericPage;
import pageObjects.tms.TeacherReadMessagePage;
import pageObjects.tms.TeacherWriteMessagePage;
import pageObjects.tms.TmsHomePage;
import services.PageHelperService;
import services.TestResultService;

public class NewUxMessagesPage extends GenericPage {

	PageHelperService pageHelper = new PageHelperService();
	
	@FindBy(xpath = "//input[contains(@name,'check')]")
	public List<WebElement> checkboxes;  //checkboxes  //tr td input[type = 'checkbox']
	
	
	@FindBy(xpath = "//a[@href='javascript:DeleteMessage()']")
	public WebElement deleteButton;
	
	
	@FindBy(xpath = "//*[text()='Course Results']")
	public List<WebElement> titleOfMessage;
	
	@FindBy(css = "tr[style] td:last-child")
	public List<WebElement> dates;//body > table:nth-child(3) > tbody:nth-child(1) > tr:nth-child(1) > td:nth-child(2) > form:nth-child(2) > div:nth-child(3) > table:nth-child(2) > tbody:nth-child(1) > tr > td:nth-child(6)
	
	
	@FindBy(css = "tr[style] a")
	public List<WebElement> messages;
	
	@FindBy(linkText = "Open Certificate")
	public WebElement certificate;
	
	@FindBy(className = "FileName__newUIFileName___2kTC3")
	public WebElement certificatesTitle;
	
	
	public NewUxMessagesPage(GenericWebDriver webDriver, TestResultService testResultService) throws Exception {
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
	
	
	
	
	public NewUxMessagesPage checkTitleDateAndCertificateOfMessage() throws Exception {
		report.startStep("Check if Course report messages are present");
			boolean messageIsPresent = titleOfMessage.size()>0;
			boolean isItFreshMessage = false;
		
		report.startStep("Check date of message");
		if(messageIsPresent) {
		   String date = dates.get(0).getText().trim();
			if(date != null && !date.equalsIgnoreCase("")) {
				LocalDate currentDate = LocalDate.now();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
				String curDate = currentDate.format(formatter);
				isItFreshMessage = date.equals(curDate);
				}
			textService.assertEquals("No fresh message", true, isItFreshMessage);
	
		
		report.startStep("Hold message window and temporary switch to popUp");	
			ArrayList<String> windows = new ArrayList<>(webDriver.getWebDriver().getWindowHandles());
			String messageWindow = windows.get(1);
			messages.get(0).click();
			ArrayList<String> windows2 = new ArrayList<>(webDriver.getWebDriver().getWindowHandles());
			for(String s:windows) {
				windows2.remove(s);
			}
			webDriver.switchToMainWindow(windows2.get(0));
			//webDriver.switchToPopup();
		
		report.startStep("Check certificate");	
			NewUxCertificatePopUp certificatePopUp = new NewUxCertificatePopUp(webDriver, testResultService);
			//certificatePopUp.checkFinalGrade();
			certificatePopUp.checkCertificate();
		
		report.startStep("Close certificate window and switch to message window");	
			webDriver.getWebDriver().switchTo().window(windows2.get(0)).close();
			webDriver.getWebDriver().switchTo().window(messageWindow);
			}
		return this;
	}
	
	public void deleteAllMessages() throws Exception {
	    switchToInboxFrame();
		//String url = webDriver.getUrl();

		int countOfMessages=titleOfMessage.size();
		List<WebElement> pages = webDriver.getElementsByXpath("//*[contains(@id,'refPage')]");

		for (int i=1;i< pages.size();i++) {

			List<WebElement> countOfCheckInCurPage = webDriver.getElementsByXpath("//*[@id='tblInbox']/tbody/tr[(not(contains(@style,'display: none')))]") ;

			if (countOfCheckInCurPage.contains(checkboxes))
				checkboxes.get(i).click();

			//List<WebElement> a = countOfCheckInCurPage.stream().filter(ab->ab.findElements(By.xpath("//input[contains(@name,'check')]")));
			//webDriver.getElementsByXpath("//input[contains(@name,'check')]");

			//List<WebElement> countOfCheckInCurPage = checkboxes.stream().filter(ei->ei.getAttribute("style")) findElements(By.XPATH, "//*[not(@disabled) and(not(contains(@class,'disabled'))) and(not(contains(.,'display: none'))) and(not(@type='hidden'))]");

			for (int c=0; c <= countOfCheckInCurPage.size()-1;c++)
				checkboxes.get(c).click();

			i++;
			pages.get(i).click();
		}
		deleteButton.click();
		webDriver.closeAlertByAccept();
		Thread.sleep(2000);
	/*
		while(titleOfMessage.size()>0) {
			checkboxes.forEach(e->e.click());
			Thread.sleep(1000);
			deleteButton.click();
			webDriver.closeAlertByAccept();
			Thread.sleep(2000);
		}
	 */
		boolean noMoreMessages = true;
		try {
			noMoreMessages = titleOfMessage.size()>0;
		}catch (Exception e) {
			//e.printStackTrace();
		}
		textService.assertEquals("No fresh message", false, noMoreMessages);
		
	}
	

	public void switchToInboxFrame() throws Exception {
			WebElement element = webDriver.waitUntilElementAppears("oed__iframe", ByTypes.className,20);
//			webDriver.switchToFrame(webDriver.waitForElement("oed__iframe", ByTypes.className, false, 15));
			webDriver.switchToFrame(element);
	}
	
	public void verifyInboxPageTitle() throws Exception {

		testResultService.assertTrue("The Inbox Page wasn't displayed properly.",
				webDriver.waitForElement("h1", ByTypes.tagName, false, webDriver.getTimeout()).getText().equals("Contact Your Teacher"));		
	}
	
	public void close() throws Exception {
		webDriver.closeNewTab(2);
		webDriver.switchToMainWindow();
	}
	
	public void clickOnElement(String pathValue) throws Exception{
		WebElement element = webDriver.waitForElement(pathValue, ByTypes.xpath, true, webDriver.getTimeout());
		Thread.sleep(2500);  // Element exists but isn't available so sleep was added.
		element.click();
	}
	
	public String[] getAndSelectTeacher() throws Exception{
		WebElement element = webDriver.waitForElement("//*[@id='tblInbox']/tbody/tr[5]/td[1]/input", ByTypes.xpath);
		String name = element.getAttribute("value");
		String tname[] = name.split("%");
		element.click();
		return tname;
	}

	public void checkTeacherName(String[] tname,String areaToCheck) throws Exception{
		String to= "";	
		switch (areaToCheck){
		
		case "writeMessage":
			to = webDriver.waitForElement("to", ByTypes.id).getAttribute("value");
		break;
		
		case "sentMessageToTeacher":
			to = webDriver.waitForElement("//*[@id='tblInbox']/tbody/tr[5]/td[3]/a", ByTypes.xpath,webDriver.getTimeout(),false).getAttribute("textContent"); // title
		break;
		
		case "sentMessageBodyForm":
			to = webDriver.waitForElement("/html/body/div[1]/table[2]/tbody/tr[2]/td[2]/b",ByTypes.xpath).getText();
		break;
		}
		testResultService.assertEquals(true, tname[0].contains(to), "The Teacher name not match");
		
	}

	public void checkSubject(String text, int tdOffSet) throws Exception{
		String title = webDriver.waitForElement("//*[@id='tblInbox']/tbody/tr[5]/td["+ tdOffSet +"]", ByTypes.xpath).getText();
		testResultService.assertEquals(title, text, "The Subject not match");
		
	}
	
	public String getInboxMessageText() throws Exception{
		String body = webDriver.waitForElement("WebPalsTextAreaId", ByTypes.id).getText();
		return body;
		
	}

	public void clickOnWriteSection() throws Exception{
		webDriver.waitForElement("//*[@id='mainAreaTD']/table/tbody/tr[2]/td/table/tbody/tr/td[2]/a",ByTypes.xpath).click();
		return;
	}
		
	public void clickOnWriteAction() throws Exception{
		webDriver.waitForElement("//*[@id='mainAreaTD']/form/div[1]/table/tbody/tr[2]/td[2]/div/table/tbody/tr/td/a",ByTypes.xpath).click();
		Thread.sleep(3000);
		webDriver.switchToPopup();
		webDriver.switchToFrame("ReadWrite");
		return;
	}
	
	public void clickOnAttachIcon() throws Exception{
		webDriver.waitForElement("//a[@id='editorTextArea_btnAttach']/img",ByTypes.xpath).click();
		return;
	}
	
	public void clickOnAttachButton() throws Exception{
		webDriver.waitForElement("//*[@id='btnAttach']",ByTypes.xpath).click();
		return;
	}

	public void clickOnSendMessage() throws Exception{
		webDriver.waitForElement("//*[@id='form1']/div[1]/table/tbody/tr[2]/td[2]/div/table/tbody/tr/td[1]",ByTypes.xpath).click();
		return;
	}
	
	public void clickOnSentMessagesArea() throws Exception{
		webDriver.waitForElement("//*[@id='mainAreaTD']/table/tbody/tr[2]/td/table/tbody/tr/td[3]/a",ByTypes.xpath).click();
		return;
	}
	
	public void uploadAnAttachment(String filePath) throws Exception {
		clickOnAttachIcon();
		Thread.sleep(1000);
		webDriver.switchToPopup();
	
		String elementId = "SelectFile"; 
		Thread.sleep(2000);
		webDriver.insertText(filePath,elementId);
		
		// Click on Upload
		clickOnElement("//*[@id='btnUpLoad']");
		Thread.sleep(4000);
		
		// Click on Attach and close the form
		clickOnAttachButton();
		Thread.sleep(6000);
	}
	
	public void addSubjectAndText(String subject,String body) throws Exception {
		String elementId = "strsubject";
		webDriver.insertText(subject,elementId);
		webDriver.switchToFrame("editorTextArea_ifr");
		
		elementId = "tinymce";
		webDriver.insertText(body,elementId);
		
		webDriver.switchToTopMostFrame();
		webDriver.switchToFrame("ReadWrite");
	}
	
	public void replyToStudentFromTeacher(String studentUserNameOR, String subjectFromTeacher, 
			String bodyResponseFromTeacher, String attachmentPath) throws Exception {
		
		TeacherReadMessagePage tRead = new TeacherReadMessagePage(webDriver, testResultService);
		
		// Reply mail to Student
		TeacherWriteMessagePage tWrite = tRead.clickOnReply();
		webDriver.switchToPopup();	
		String actualUserList = tWrite.getUserListInMessage();		
		assertEquals("User Lists not match", studentUserNameOR, actualUserList);
		
		// Write response from Teacher to Student
		webDriver.switchToTopMostFrame();	
		
		tWrite.enterSubject(subjectFromTeacher);
		tWrite.enterTextIntoMessageBody(bodyResponseFromTeacher);

	    // Upload file attachment by Teacher and Verify it uploaded
		uploadAnAttachment(attachmentPath);
		
		webDriver.switchToMainWindow();
		Thread.sleep(1000);
		webDriver.switchToPopup();
	
		// Send message from Teacher to Student
		tRead = tWrite.clickOnSend();
		webDriver.switchToNextTab();
		tRead.clickOnClose();
		Thread.sleep(1000);	
	}
	
	public void verifyAndClickFileUploadedInTMS(String fileName) throws Exception {
		// TODO Auto-generated method stub
		WebElement element = webDriver.waitForElement("//*[@id='tbFileListNodes']/tr/td[1]/a", ByTypes.xpath);
		boolean actual = element.getText().contains(fileName);
		testResultService.assertEquals(true, actual, "Uploaded file not displyed");
		element.click();
	}
	
	public void goToInboxCommunication() throws Exception {
		try {
			TmsHomePage tmsHomePage = new TmsHomePage(webDriver, testResultService);
			tmsHomePage.clickOnCommunication();
			Thread.sleep(3000);
			tmsHomePage.clickOnInbox();
			Thread.sleep(3000);
		} catch (Exception e) {
			testResultService.addFailTest("FAIL: Go to Inbox in Communications. Error: " + e);
		}
	}
	
	public void verifySenderStudentName(String studentUserNameOR) throws Exception{
		WebElement element = webDriver.waitUntilElementAppears("//tbody[@id='tblBody']/tr/td[5]", ByTypes.xpath,10);
		String stuName = element.getAttribute("title"); //webDriver.waitForElement("//tbody[@id='tblBody']/tr/td[5]", ByTypes.xpath).getAttribute("title");
		String[] studentName = stuName.split(" ");
		assertEquals("Student name is invalid", studentUserNameOR, studentName[0]);
	}
	
	public void verifySubject(String subject) throws Exception{
		String actualSubject = webDriver.waitForElement("//tbody[@id='tblBody']/tr/td[8]/div", ByTypes.xpath).getText();
		assertEquals("Wrong Subject", subject, actualSubject);
	}
	
	public void verifyAndClickAttachfileExistInTMSReadMessage(String fileName) throws Exception {
		WebElement element = webDriver.waitForElement("//*[@id='attachtd']/a", ByTypes.xpath);
		boolean actual = element.getText().contains(fileName);
		testResultService.assertEquals(true, actual, "Uploaded file not displyed");
		element.click();
	}
	
	public void verifyFileuploadedText(String bodyFileText) throws Exception {
		String actualBodyMessage = webDriver.waitForElement("/html/body/pre", ByTypes.xpath).getText();
		testResultService.assertEquals(bodyFileText, actualBodyMessage, "The body text message not math");
	}
	
	public void verifyStudentSubjectMessage(String actual,String expected) throws Exception{
		assertEquals("Subject message not match", expected, actual); 
	}
	
	public void openMessageAndCheckNameAndSubject(String[] tname,String expectedSubject)throws Exception{
		
		clickOnElement("//table[@id='tblInbox']/tbody/tr[5]/td[3]/a");
		webDriver.switchToPopup();
		webDriver.switchToFrame("ReadWrite");
		
		// Verify Subject Text
		String actualSubject = webDriver.waitForElement("/html/body/div[1]/table[2]/tbody/tr[3]/td[2]/b", ByTypes.xpath).getText();
		testResultService.assertEquals(expectedSubject, actualSubject, "Text not match");
		
		// Verify Teacher Name
		checkTeacherName(tname,"sentMessageBodyForm");
	}
	
	public void checkAttachIcon()throws Exception{
		WebElement element = webDriver.waitForElement("//*[@id='atch']", ByTypes.xpath,1,false);
		
		if (element==null)
			testResultService.addFailTest("Attached Icon not display", false, true);
	}
	
	public void verifyAttachedFile(String expectedText)throws Exception{
		
		WebElement element = webDriver.waitForElement("//*[@id='attachtd']/a", ByTypes.xpath);
		element.click();
		Thread.sleep(2000);
		webDriver.switchToNewWindow(3);
		
		String actualText = webDriver.waitForElement("/html/body/pre", ByTypes.xpath,false,webDriver.getTimeout()).getText();
		testResultService.assertEquals(actualText, expectedText, "The Text not match");
	
		webDriver.closeNewTab(1);
		webDriver.closeNewTab(1);
	}
	
	public void waitInboxPageLoaded() throws Exception{
		switchToInboxFrame();
		
		WebElement element=null;
		
		for (int i=0; i<=20 && element==null;i++){
				try {
					element = webDriver.waitForElement("supportForm", ByTypes.className,false,1);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
}
