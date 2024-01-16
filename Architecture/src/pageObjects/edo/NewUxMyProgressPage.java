package pageObjects.edo;

import Enums.ByTypes;
import Enums.StepProgressBox;
import drivers.GenericWebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import pageObjects.GenericPage;
import services.TestResultService;

import java.util.List;
import java.util.Random;

public class NewUxMyProgressPage extends NewUxHomePage {
	
	@FindBy(className = "myProgress__title")
	public WebElement myProgressTitle;
	
	@FindBy(className = "myProgress__courseName")
	public WebElement courseName;
	
	@FindBy(className = "myProgress__courseDDL--currentCourseName")
	public WebElement courseNameWithMinimizedWindow;
	
	@FindBy(className = "myProgress__printBtn")
	public WebElement printBtn;
	
	@FindBy(id = "home__studentWidgetCompletionData")
	public WebElement courseCompletion;
		
	@FindBy(id = "home__studentWidgetTestScoresData")
	public WebElement avgTestScore;
	
	@FindBy(xpath = "//div[@class='home__studentWidgetHrMin']/span[1]")
	public WebElement timeOnTaskHR;
	
	@FindBy(xpath = "//div[@class='home__studentWidgetHrMin']/span[3]")
	public WebElement timeOnTaskMIN;
	
	@FindBy(id = "home__studentWidgetMidtermTestData")
	public WebElement midTermTestScore;
	
	@FindBy(id = "home__studentWidgetFinalTestData")
	public WebElement finalTestScore;
	
	@FindBy(id = "home__studentWidgetFinalTestReadingData")
	public WebElement TOEICReadingScore;
	
	@FindBy(id = "home__studentWidgetFinalTestListeningData")
	public WebElement TOEICListeningScore;
	
	@FindBy(id = "home__studentWidgetFinalTestViewResultsData")
	public WebElement TOEICTestViewResults;
	
	@FindBy(id = "home__studentWidgetFinalTestViewFullReportData")
	public WebElement TOEICTestViewFullReport;
					
	@FindAll(@FindBy(className = "myProgress__unitW"))
	public List <WebElement> units;
	
	@FindBy(how = How.CLASS_NAME, using = "myProgress__unitLesson")
	public List<WebElement> lessons;	
	
	@FindBy(how = How.CLASS_NAME, using = "home__studentWidgetMidtermTest")
	public List<WebElement> midTermW;	
	
	@FindBy(how = How.CLASS_NAME, using = "home__studentWidgetFinalTest")
	public List<WebElement> finalTestW;	
	
	//@FindBy(xpath = "//*[contains(@id,'myProgress__unitReflectionBtn_idx-')]")
	//public WebElement UnitReflectionBttnTooltip;	
	
	@FindBy(xpath= "//span[contains(@id,'myProgress__unitReflectionBtn_idx')]")
	public WebElement UnitReflectionBttn;	
	
	public NewUxMyProgressPage(GenericWebDriver webDriver, TestResultService testResultService) throws Exception {
		super(webDriver, testResultService);
		PageFactory.initElements(webDriver.getWebDriver(), this);
		// TODO Auto-generated constructor stub
	}

	@Override
	public GenericPage waitForPageToLoad() throws Exception {
		// TODO Auto-generated method stub
		webDriver.waitUntilElementAppears("//section[@class='myProgress__gridOW ng-scope']//div", 60);
		return null;
	}

	@Override
	public GenericPage OpenPage(String url) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public String getCourseName() throws Exception {
		webDriver.waitUntilElementAppears(courseName, 10);
		return courseName.getText();			
	}
	
	public String getCourseNameWithMinimizedWindow() throws Exception {
		webDriver.waitUntilElementAppears(courseNameWithMinimizedWindow, 10);
		return courseNameWithMinimizedWindow.getText();
	}
	
	public String getPageTitle() throws Exception {
		webDriver.waitUntilElementAppears(myProgressTitle, 10);
		return myProgressTitle.getText();		
	}
	
	public String getCourseCompletion() throws Exception {
		webDriver.waitUntilElementAppears(courseCompletion, 10);
		return courseCompletion.getText();			
	}
	
	public String getAvgTestScore() throws Exception {
		webDriver.waitUntilElementAppears(avgTestScore, 10);
		return avgTestScore.getText();		
	}
	
	public String getTimeOnTaskHours() throws Exception {
		webDriver.waitUntilElementAppears(timeOnTaskHR, 10);
		return timeOnTaskHR.getText();			
	}
	
	public String getTimeOnTaskMinutes() throws Exception {	
		webDriver.waitUntilElementAppears(timeOnTaskMIN, 10);
		return timeOnTaskMIN.getText();			
	}
	
	public boolean isMidTermWidgetDisplayed() throws Exception {	
		if (midTermW.size() == 0) return false;
		else return midTermW.get(0).isDisplayed();	
	}
	
	public boolean isFinalTestWidgetDisplayed() throws Exception {
		if (finalTestW.size() == 0) return false;
		else return finalTestW.get(0).isDisplayed();				
	}
	
	public boolean isFinalTestFullReportDisplayed() throws Exception {
		if (TOEICTestViewFullReport == null)
			return false;
		else
			return true;	
	}
	
	public boolean isFinalTestResultsDisplayed() throws Exception {
		if (TOEICTestViewResults == null) 
			return false;
		else
			return true;	
	}
	
	public String getMidTermScore() throws Exception {	
		return midTermTestScore.getText();			
	}
	
	public String getGradebookWritingScore() throws Exception {
		WebElement writing = webDriver.waitForElement("home__studentWidgetData--writing", ByTypes.id, false, webDriver.getTimeout());
		if (writing!=null) {
			return writing.getText();
		}
		return "";
	}

	public String getGradebookZoomScore() throws Exception {		
		WebElement zoom = webDriver.waitForElement("home__studentWidgetData--zoom", ByTypes.id, false, webDriver.getTimeout());
		if (zoom!=null) {
			return zoom.getText();
		}
		return "";
	}
	
	public String getGradebookSpeakingScore() throws Exception {
		
		WebElement speaking = webDriver.waitForElement("home__studentWidgetData--speaking", ByTypes.id, false, webDriver.getTimeout());
		if (speaking!=null) {
			return speaking.getText();
		}
		return "";
	}
	
	public String getFinalTestScore() throws Exception {
		return finalTestScore.getText();		
	}
	
	public String[] getTOEICTestScore() throws Exception {
		String[] score = {"",""};
		score[0] = TOEICReadingScore.getText();
		score[1] = TOEICListeningScore.getText();
		return score;			
	}
	
	public String getUnitLabel(int unitNumber) throws Exception {
		int index = unitNumber-1;
		return units.get(index).findElement(By.className("myProgress__unitTitleLabel")).getText();		
	}
		
	public String getUnitName(int unitNumber) throws Exception {
		int index = unitNumber-1;
		return units.get(index).findElement(By.className("myProgress__unitNameLabel")).getText();		
	}
	
	public String getUnitProgressTooltip(int unitNumber) throws Exception {
		int index = unitNumber-1;
		return units.get(index).findElement(By.className("progressBar__wrapper")).getAttribute("title");		
	}
	
	public String getUnitProgressBarWidth(int unitNumber) throws Exception {
		int index = unitNumber-1;
		String style = units.get(index).findElement(By.className("progressBar__progress")).getAttribute("style");
		String value = style.replace("width: ", "").replace("%;", "");
		
		double progressValue = Double.valueOf(value);
		progressValue = textService.round(progressValue, 4);
		
		value = String.valueOf(progressValue);

		return value;			
	}
	
	public String getUnitProgressCompletionValue(int unitNumber) throws Exception {
		int index = unitNumber-1;
		return units.get(index).findElement(By.className("myProgress__unitCompletionData")).getText();		
	}
	
	public String getUnitProgressColor(int unitNumber) throws Exception {
		WebElement bar = units.get(unitNumber-1).findElement(By.className("progressBar__progress"));
		bar = bar.findElement(By.tagName("rect"));
		String color = bar.getCssValue("fill");
		return color;		
	}
	
	public boolean isUnitLocked (int unitNumber) throws Exception {
		boolean isLocked = false;
		webDriver.waitUntilElementAppears("myProgress__unitLock", ByTypes.className, 10);
		
		String unitLock = units.get(unitNumber-1).findElement(By.className("myProgress__unitLock")).getAttribute("class");
		if (unitLock.contains("locked")) isLocked = true;
		
		return isLocked;		
	}
	
	public boolean isLessonLocked (int lessonNumber) throws Exception {
		boolean isLocked = false;
		List<WebElement> statusScope = lessons.get(lessonNumber-1).findElements(By.className("myProgress__unitLesson_statusIsLockIcon"));
		if (statusScope.size()!=0){
			if (statusScope.get(0).getAttribute("title").equals("Locked")) isLocked = true;
		}
				
		return isLocked;		
	}
	
	public void clickToOpenUnitLessonsProgress(int unitNumber) throws Exception {
		int index = unitNumber-1;
		webDriver.waitUntilElementAppears("myProgress__unitArrow",ByTypes.className, 30);
		
		//Thread.sleep(1500); //myProgress__unitArrow
		units.get(index).findElement(By.className("myProgress__unitArrow")).click();	
	}
	
	public void checkProgressStatusInLessonList(int lessonNumber, int progressPercent) throws Exception {
		String status = lessons.get(lessonNumber-1).findElement(By.className("myProgress__unitLesson_statusLabel")).getText();
		
		if (progressPercent == 0) {
			testResultService.assertEquals("Not Started", status, "Lesson Progress indication not valid");
		} else if (1 <= progressPercent && progressPercent < 100) {
			testResultService.assertEquals("In Progress", status, "Lesson Progress indication not valid");
		} else if (progressPercent == 100) {
			testResultService.assertEquals("Completed", status, "Lesson Progress indication not valid");
		}
	}
	
	public String getLessonLastTestScore(int lessonNumber) throws Exception {
		return lessons.get(lessonNumber-1).findElement(By.className("myProgress__unitLesson_testScore")).getText();		
	}
	
	public String getLessonName(int lessonNumber) throws Exception {
		return lessons.get(lessonNumber-1).findElement(By.className("myProgress__unitLesson_name")).getText();		
	}
	
	public void verifyStepProgressIndication(int lessonNum, int stepNum, StepProgressBox expected) throws Exception {
		
		String expStatus = "undefined";
		String expTooltip = "undefined";
		
		List<WebElement> steps = lessons.get(lessonNum-1).findElements(By.className("myProgress__unitLesson_step"));
		WebElement step = steps.get(stepNum-1);
		
		String stepStatus = step.findElement(By.className("myProgress__unitLesson_stepProgressIW")).getAttribute("class");
		webDriver.hoverOnElement(step);
		String stepTooltip = step.findElement(By.className("myProgress__unitLesson_stepToolTip_stepProgress")).getText(); 
		
		switch (expected) {
		
			case done:
				expStatus = "done";
				expTooltip = "Completed";
				break;
			case half:			
				expStatus = "inProgress";
				expTooltip = "In Progress";
				break;				
			case empty:		
				expStatus = "notStarted";
				expTooltip = "Not Started";
				break;
		}		
		
		testResultService.assertEquals(true, stepStatus.contains(expStatus), "Lesson: "+lessonNum+" Step: "+stepNum+" - step box progress indication is wrong");
		testResultService.assertEquals(true, stepTooltip.equals(expTooltip), "Lesson: "+lessonNum+" Step: "+stepNum+" - step box tooltip is wrong");	
	}
	
	public NewUxLearningArea2 clickonStepInLesson(int lessonNum, int stepNum) throws Exception {
		List<WebElement> steps = lessons.get(lessonNum-1).findElements(By.className("myProgress__unitLesson_step"));
		WebElement step = steps.get(stepNum-1);
		step.findElement(By.className("myProgress__unitLesson_stepProgressIW")).click();
		return new NewUxLearningArea2(webDriver, testResultService);
	}
	
	public void verifyStepNameInTooltip(int lessonNum, int stepNum, String expStepName) throws Exception {
		List<WebElement> steps = lessons.get(lessonNum-1).findElements(By.className("myProgress__unitLesson_step"));
		WebElement step = steps.get(stepNum-1);
				
		webDriver.hoverOnElement(step);
		String stepName = step.findElement(By.className("myProgress__unitLesson_stepToolTip_stepName")).getText(); 
				
		testResultService.assertEquals(expStepName.trim(), stepName, "Lesson: "+lessonNum+" Step: "+stepNum+" - step name is not as expected");
	}
	
	public String checkUnitTestScoreOfSpecificUnit(int unitId) throws Exception {
		return webDriver.waitForElement("//span[@class='myProgress__unitLesson_testScoreTxt--score ng-binding']", ByTypes.xpath, true, 3).getText();
	}
	
	public void validateUnitIsLocked(int unitNum) throws Exception {
		List<WebElement> units = webDriver.getWebDriver().findElements(By.xpath("//div[contains(@class,'myProgress__unitW')]/div"));
		String attribute = units.get(unitNum-1).getAttribute("className");
		testResultService.assertEquals(attribute.contains("--locked"), true,"Unit "+unitNum+" is Not Locked in My Progress page.");
	}
	
	public void validateUnitIsNotLocked(int unitNum) throws Exception {
		List<WebElement> units = webDriver.getWebDriver().findElements(By.xpath("//div[contains(@class,'myProgress__unitW')]/div"));
		String attribute = units.get(unitNum-1).getAttribute("className");
		testResultService.assertEquals(attribute.contains("--locked"), false,"Unit "+unitNum+" is Locked in My Progress page.");
	}
	
	public void clickOnUnitReflectionBttnInMyProgress() throws Exception {
		webDriver.ClickElement(UnitReflectionBttn);	
	}
	
	public void verifyUnitReflectionbuttonInTooltip(String unitReflectionToolTip) throws Exception {
		String bttnText = webDriver.getTextFromHoverMessageByHoverElement(UnitReflectionBttn, "//span[contains(@class,'myProgress__unitLesson_reflectionToolTip')]");
		testResultService.assertEquals(unitReflectionToolTip,bttnText.replaceAll("\n", " "));
	}

	public void verifyUnitCourseIsExpand(String expectedcourseName, int expectedUnitIndex) throws Exception {
		testResultService.assertEquals(expectedcourseName,getCourseName(), "Expected Coourse Name not expand");
		testResultService.assertEquals(expectedUnitIndex, getUnitIndexIsExpand(), "Expected Unit Index is not expand");
	}
	
	public int getUnitIndexIsExpand() throws Exception {
		List<WebElement> element =  webDriver.getElementsByXpath("//*[contains(@class,'myProgress__unitArrow myProgress__unitArrow')]"); 
		int unitIndex=0;
		
		for (int i=0;i<=element.size()-1;i++) {
			if (element.get(i).getAttribute("class").contains("open")) {
				unitIndex = i;
				break;
			}
		}
		return unitIndex+1;
	}	
	
	public int getRandomUnitNum() {
		int numOfUnits = webDriver.getWebDriver().findElements(By.className("myProgress__unitArrow")).size();
		Random rand = new Random();
		return rand.nextInt(numOfUnits)+1;
	}
	
	public void waitUntilPageIsLoaded() throws Exception {
		webDriver.waitUntilElementAppears("//*[@class='myProgress__titleW']", 10);
		/*
		WebElement element = webDriver.waitForElement(, ByTypes.xpath, false, 5);
		int counter = 0;
		while (element == null && counter < 20) {
			element = webDriver.waitForElement("//*[@class='myProgress__titleW']", ByTypes.xpath, false, 5);
			counter++;
		}
		*/
	}


	public String getCertainProgressFromProgressPageByValue(String value, String courseName) throws Exception {
		String xpath = "";
		List<WebElement> unitsProgress = null;
		if(value.equalsIgnoreCase("UnitProgress")) {
			xpath = "//*[@class='myProgress__unitCompletionData ng-binding']";
		}
		if(value.equalsIgnoreCase("TestScore")) {
			xpath = "//*[@class='myProgress__unitLesson_testScore ng-binding']";
		}
		if(value.equalsIgnoreCase("CourseProgress")) {
			xpath = "//div[@id='home__studentWidgetCompletionData']";
		}
		try {
			Thread.sleep(1500);
			unitsProgress = webDriver.getElementsByXpath(xpath);

		}catch (Exception | AssertionError err) {
			List<WebElement> courses = webDriver.getElementsByXpath("//div[@class='myProgress__courseTitleText ng-binding']");
			WebElement el = courses.stream().filter(c -> c.getText().equalsIgnoreCase(courseName)).findAny().orElse(null);
			WebElement scroll = webDriver.waitForElement("//div[@class='mCustomScrollBox mCS-onLightBackground mCSB_vertical mCSB_inside']/div[@class='mCSB_container']", ByTypes.xpath);
			String elementToSetScroll = scroll.getAttribute("id");
			webDriver.executeJsScript("document.getElementById('" + elementToSetScroll + "').setAttribute('style', 'position: relative; top: -320px; left: 0px;')");
			if (el!=null) {
				el.click();
			}
			Thread.sleep(1500);
			unitsProgress = webDriver.getElementsByXpath(xpath);
		}
		String prgs = unitsProgress.get(0).getText();
		prgs = prgs.replace("%", "");
		return prgs;
	}

	public void verifyWhetherCourseOpened(String s) {
	}
}
