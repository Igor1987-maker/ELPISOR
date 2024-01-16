package pageObjects.tms;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import Enums.ByTypes;
import drivers.GenericWebDriver;
import pageObjects.GenericPage;
import services.TestResultService;

public class TmsMatrixPage extends GenericPage{
	
	@FindBy(xpath = "//div[@class='grid']//td[4]")
	public List<WebElement> midTermScoresTable;
	
	
	@FindBy(className = "mtxTitle")
	public WebElement matrixTitle;  //#DDLCources
	
	@FindBy(id = "DDLCources")
	public WebElement courseLevelSelector; 
	
	@FindBy(id = "btnSend")
	public WebElement sendButton; 
	
	@FindBy(id = "btnPrint")
	public WebElement printButton; 
	
	@FindBy(className = "grid")
	public List<WebElement> allStudents; 
	
	@FindBy(className = "pagerUnselect")
	public List<WebElement> pages; 
	
	//@FindBy(xpath = "//*[@id='DataPager1']/a[5]")
	//@FindBy(xpath = "//a[normalize-space()='...']")
	@FindBy(xpath = "(//a[contains(text(),'...')])")
	public List<WebElement> nextSetOfPages;  
	
	
	public TmsMatrixPage(GenericWebDriver webDriver, TestResultService testResultService) throws Exception {
		super(webDriver, testResultService);
		// TODO Auto-generated constructor stub
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
	
	
	public void choseCourseLevel(String courseLevel) throws InterruptedException {
		new Select(courseLevelSelector).selectByVisibleText(courseLevel);
		Thread.sleep(2000);
	}
	
	public void findStudentAndSentHimCertificate(String userName) throws Exception {
		
		boolean studentFound = false;
		WebElement wantedStudent = null;
		int pageIndex=0;
	
		while(!studentFound) {
		Thread.sleep(1500);
		wantedStudent = allStudents.stream().filter(s->s.findElement(By.className("studentName"))
				.getText().equalsIgnoreCase(userName+" "+userName)).findAny().orElse(null); 
		Thread.sleep(200);
		if(wantedStudent==null) {
			if(pageIndex == pages.size()) {
				if(nextSetOfPages.size()>1) {
					nextSetOfPages.get(1).click();
					}else {
					nextSetOfPages.get(0).click();	
					}
				Thread.sleep(2000);
				pageIndex = 0;
		
				}else {
					pages.get(pageIndex).click();
					Thread.sleep(2000);
					pageIndex++;
				}
			}
		else {
			studentFound = true;
		}
		}
		int index = allStudents.indexOf(wantedStudent);
		boolean isOpen = checkLockerOpen(index);
		if(isOpen) {
			webDriver.waitForElement("baseList_ctrl"+index+"_btnItemLock", ByTypes.id).click();
			//wantedStudent.findElement(By.id("baseList_ctrl"+index+"_btnItemLock")).click();;
			}
		Thread.sleep(2);
		webDriver.waitForElement("baseList_ctrl"+index+"_btnItemSend", ByTypes.id).click();;
		//WebElement sendButton = wantedStudent.findElement(By.id("baseList_ctrl"+index+"_btnItemSend"));
		//sendButton.click();									  // baseList_ctrl  9      _btnItemSend
		/*
		for(WebElement el:allStudents) {
			WebElement e = el.findElement(By.className("studentName"));
			if(e.getText().equalsIgnoreCase(userName+" "+userName)){
				String s = e.getAttribute("id").split("[ctr,_spUserName]")[1];
				e.findElement(By.id("baseList_ctr"+s+"_btnItemSend")).click();
			}
		}
		*/
		}
		
		
	
	
	
	public void checkMatrixReportTitle() throws Exception {
		
		webDriver.waitForElementByWebElement(matrixTitle, "title", true, 20);
		String title = matrixTitle.getText().trim().toString();
		textService.assertEquals("Wrong title", "Matrix Report", title);
		
		
	}
		
	
	public void chooseCourse(String courseName) throws Exception {
		webDriver.selectElementFromComboBox("DDLCources", courseName, ByTypes.id, false);
	}
	
	public void findStudentAndClickOnScore(String userName) throws Exception{
		boolean isUserDisplayed = validateUserIsDisplayedInTable(userName);
		if (isUserDisplayed) {
			int studentIndex = getStudentIndexInTableByUserName(userName);
			if(!midTermScoresTable.get(studentIndex).getText().equals("")) {
			webDriver.ClickElement(midTermScoresTable.get(studentIndex));
			} else {
				testResultService.addFailTest("Score is not displayed for Student: " + userName, false, true);
			}
		}
	}
	
	// This function works only for one page
	public boolean validateUserIsDisplayedInTable(String userName) throws Exception{
		List<WebElement> studentsNames = new ArrayList<WebElement>();//webDriver.getWebDriver().findElements(By.xpath("//span[@class='studentName']"));
		boolean isUserFound = false;
		
		while(!isUserFound) {
		
			List<WebElement> pages = webDriver.getWebDriver().findElements(By.xpath("//*[contains(@class,'pager')]"));
			for (int j = 0; j < pages.size(); j++) {
				webDriver.ClickElement(pages.get(j));
				studentsNames = webDriver.getWebDriver().findElements(By.xpath("//span[@class='studentName']"));
			
				for (int i = 0; i < studentsNames.size(); i++) {
					String temp = studentsNames.get(i).getAttribute("title").split("\nUsername: ")[1];
					
					if (temp.equals(userName)) {
						isUserFound = true;
						break;
					}
				}
				if (isUserFound) {
					break;
				} 
			}
			
			if (!isUserFound) {
				//click "..."
				List<WebElement> nextPagesCount = webDriver.getWebDriver().findElements(By.xpath("//a[text()='...']"));
				if (nextPagesCount.size() == 2) {
					webDriver.ClickElement(nextPagesCount.get(1));
				} else {
					webDriver.ClickElement(nextPagesCount.get(0));
				}
			}
		}
		return isUserFound;
	}
	
	public int getStudentIndexInTableByUserName(String userName) {
		List<WebElement> studentsNames = webDriver.getWebDriver().findElements(By.xpath("//span[@class='studentName']"));
		int index = 0;
		for (int i = 0; i < studentsNames.size(); i++) {
			String temp = studentsNames.get(i).getAttribute("title").split("\nUsername: ")[1];
			
			if (temp.equals(userName)) {
				index = i;
				break;
			}
		}
		return index;
	}

	public int findStudentAndPrintCertificate(String userName) throws InterruptedException {
		boolean studentFound = false;
		WebElement wantedStudent = null;
		int pageIndex=0;
	
		while(!studentFound) {
		Thread.sleep(1500);
		wantedStudent = allStudents.stream().filter(s->s.findElement(By.className("studentName"))
				.getText().equalsIgnoreCase(userName+" "+userName)).findAny().orElse(null); 
		Thread.sleep(2000);
		if(wantedStudent==null) {
			if(pages != null && pageIndex == pages.size()) {
				if(nextSetOfPages != null && nextSetOfPages.size()>1) {
					nextSetOfPages.get(1).click();
					}
				else {
					if(nextSetOfPages != null)
					nextSetOfPages.get(0).click();	
					}
				Thread.sleep(2000);
				pageIndex = 0;
		
			}else {
					pages.get(pageIndex).click();
					Thread.sleep(2000);
					pageIndex++;
				}
			}
		else {
			studentFound = true;
		}
		}
		int index = allStudents.indexOf(wantedStudent);
		//wantedStudent.findElement(By.id("baseList_ctrl"+index+"_btnItemSend")).click();
		return index;
	}
	
	public boolean checkLockerOpen(int index) throws Exception {
		WebElement locker = webDriver.waitForElement("baseList_ctrl"+index+"_btnItemLock", ByTypes.id);		
		//WebElement locker = student.findElement(By.id("baseList_ctrl"+index+"_btnItemLock"));
		String title = "";
		boolean closeLocker = false;
		try {
			title = locker.getAttribute("title");
		}catch (Exception e) {
			closeLocker = true;
		}
		if(title.equalsIgnoreCase("Lock"))
			closeLocker = true;
		return closeLocker;
	}

}
