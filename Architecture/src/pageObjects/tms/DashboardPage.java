package pageObjects.tms;

import java.awt.Container;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import Enums.ByTypes;
import drivers.GenericWebDriver;
import pageObjects.GenericPage;
import services.TestResultService;

public class DashboardPage extends TmsHomePage {

	private static final String SELECT_COURSE = "selectCourse";
	private static final String SELECT_CLASS = "selectClass";
	private static final String SELECT_TEACHER = "selectTeacher";

	private static final String GOBUTTONID = "goButton";

	public DashboardPage(GenericWebDriver webDriver,
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

	public void selectClassInDashBoard(String className) throws Exception {
		//webDriver.selectElementFromComboBox(SELECT_CLASS, className);
		webDriver.waitUntilElementAppears("selectClass", ByTypes.id,5);
		webDriver.selectValueFromComboBox("selectClass", className, ByTypes.id);
		
		/*WebElement element = webDriver.waitForElement("SelectClass", ByTypes.id);
		element.click();
		element.sendKeys(className);*/
	}

	public void selectCourseInDashboard(String courseName) throws Exception {
		webDriver.waitUntilElementAppears("selectCourse", ByTypes.id, 10);
		webDriver.selectValueFromComboBox("selectCourse", courseName, ByTypes.id);
		
		//WebElement element = webDriver.waitForElement("SelectCourse", ByTypes.id);
		//element.click();
		//Thread.sleep(1000);
		//element.sendKeys(courseName);
	}

	public void selectCourseInDashboardByIndex(int index) throws Exception {
		webDriver.selectElementFromComboBoByIndex(SELECT_COURSE, index);
	}

	public void clickOnDashboardGoButton() throws Exception {
		webDriver.waitForElement(GOBUTTONID, ByTypes.id).click();
	}

	public String getSelectedClass() throws Exception {
		return webDriver.getSelectedValueFromComboBox(SELECT_CLASS);
	}

	public String getSelectedCourse() throws Exception {
		String course = webDriver.getSelectedValueFromComboBox(SELECT_COURSE);
		course = course.replaceAll("\\s+$", "");
		return course;
	}
	
	public void clickTmsHome() throws Exception {
		webDriver.waitForElement("homelink", ByTypes.id).click();

	}

	public void selectTeacherInDashboard(String teacherName) throws Exception {
		webDriver.selectElementFromComboBox(SELECT_TEACHER, teacherName, true);

	}

	public WebElement getWidgetElement(int row, int col, String title)
			throws Exception {
		WebElement element = webDriver
				.waitForElement("//div[@class='dashboard']//div[" + row
						+ "]//div[" + col + "]", ByTypes.xpath);
		return element;
	}

	public boolean checkIfWidgetHasData(int row, int col) throws Exception {
		boolean hasData = true;
	//	WebElement element = webDriver.waitForElement(
	//			"//div[@class='dashboard']//div[" + row + "]//div[" + col + "]//[div[contains(@class,'nodata')]'"
	//			, ByTypes.xpath,false, smallTimeOut);
		
		WebElement element = webDriver.waitForElement("completaionRateBarData",ByTypes.className
				,false,smallTimeOut);
		if (element == null) {
			webDriver.printScreen("Chart has no data");
			hasData = false;
		}
		return hasData;
	}

	public String getWidgetTitle(int row, int col) throws Exception {
		String title = webDriver.waitForElement(
				"//div[@class='dashboard']//div[" + row + "]//div[" + col
						+ "]//div//div[1]//span", ByTypes.xpath).getText();
		return title;
	}

	public void clickOnClassTestScoreReport() throws Exception {
		webDriver.waitForElement("successWidgetBtn", ByTypes.id).click();

	}

	public void clickOnCompletionWidgetButton() throws Exception {
		//webDriver.waitForElement("completionRateBtn", ByTypes.id).click();
		webDriver.clickOnElement(webDriver.waitForElement("completionRateBtn", ByTypes.id));

	}

	public void clickOnPltWidgetButton() throws Exception {
		webDriver.waitForElement("PTWidgetBtn", ByTypes.id).click();

	}

	public WebElement getNumberOfStudentsPerClass() throws Exception {
		WebElement element = webDriver.waitForElement(
				"//div[@class='studentsCounterNumber']", ByTypes.xpath);

		return element;
	}

	public String getAvgScorePerUnitClassTestScore(int unitNumber)
			throws Exception {
		WebElement element = webDriver.waitUntilElementAppears("//div[@id='successWidget']//div[contains(@class,'point-"
						+ unitNumber + "')]",ByTypes.xpath, 30);
		
	//	String score = webDriver.waitForElement(
	//			"//div[@id='successWidget']//div[contains(@class,'point-"
	//					+ unitNumber + "')]", ByTypes.xpath, true, 20)
	//			.getText();
		
		String score = element.getText();
		
		return score;

	}
	
	public List<WebElement> getAvgScoreUnitsElements()
			throws Exception {
		List<WebElement> elements = webDriver.waitForElement("successWidget", ByTypes.id).findElements(By.xpath("//div[contains(@class,'jqplot-point-label jqplot-series-1')]"));
		return elements;
	}

	public void hideSelectionBar() throws Exception {
		// webDriver.hoverOnElement(
		// webDriver.waitForElement("tmsDashNavHandle", ByTypes.className), 50,
		// 50);
		// Thread.sleep(500);
		// if selection bar is shown, wait few seconds to and check if it
		// becomes hidden. if not - click the handle
		if (!isSelectionBarShown())
			clickDashBoardHandle();
		//webDriver.waitForElement("tmsDashNavHandle", ByTypes.id).click();
	}
	
	public void openSelectionBar() throws Exception {
		
		if (!isSelectionBarShown()) {
				// click the handle
				clickDashBoardHandle();
		}
		
		Thread.sleep(1000);
	}

	public void clickDashBoardHandle() throws Exception {
		webDriver.waitForElement("tmsDashNavHandle", ByTypes.id).click();

	}

	private boolean isSelectionBarShown() throws Exception {
		String path="//div[@id='tmsDashNavHandle'][contains(@class,'handleOpen')]";
		WebElement handleShow = webDriver.waitUntilElementAppears(path, ByTypes.xpath, 3);
		
		if (handleShow != null) {
			return true;
		} else {
			return false;
		}

	}

	public void hoverOnHeaderAndSelectFromClassCombo(String value)
			throws Exception {
		WebElement hoverElement = webDriver.waitForElement("tmsDefaultBar",
				ByTypes.id);
		// WebElement selectElement= webDriver.waitForElement(SELECT_CLASS,
		// ByTypes.xpath);

		webDriver.HoverOnElementAndmoveToComboBoxElementAndSelectValue(
				hoverElement, SELECT_CLASS, value);
	}

	public String getPltWidgetContent() throws Exception {
		String content = webDriver.getElementHTML(webDriver.waitForElement(
				"PTWidgetCenter", ByTypes.id));
		return content;
	}

	public void hoverOnPltWidget() throws Exception {

		WebElement webElement = webDriver.waitForElement(
				"//div[@id='PTWidget']//canvas[5]", ByTypes.xpath);
		int width = Integer.parseInt(webElement.getAttribute("width"));
		int height = Integer.parseInt(webElement.getAttribute("height"));
//		System.out.println("width: " + width + " Height: " + height);

		webDriver.hoverOnElement(webDriver.waitForElement(
				"//div[@id='PTWidget']//canvas[5]", ByTypes.xpath),
				10 + width / 2, height / 4);
		webDriver.printScreen("onhover");

	}

	public String getNumberOfStudentsPerPltLevel() throws Exception {
		String text = webDriver.waitForElement(
				"//div[@id='PTWidgetCenter']//div[@class='coursePercentage']",
				ByTypes.xpath).getText();
		return text;
	}

	public String getPltComletedStudents() throws Exception,
			NumberFormatException {
		String pltStudents = webDriver.waitForElement("PTWidgetTotalNumber",
				ByTypes.id).getText();
		return pltStudents;
	}

	public void hoverOnClassScoreTooltip(int scoreIndex) throws Exception {
		WebElement tooltip = webDriver.waitForElement(
				"//div[contains(@class,'point-" + scoreIndex + "')]",
				ByTypes.xpath);
		webDriver.hoverOnElement(tooltip);

	}

	public void checkClassScoreToolipContent(String unitName, String grade,
			String numOfStudents) throws Exception {
		WebElement tooltip = webDriver.waitForElement("//div[@role='tooltip']",
				ByTypes.xpath);
	

	}

	public String getWidgetWidth(int row, int col) throws Exception {
		String actualWidth = webDriver
				.waitForElement(
						"//div[@class='dashboard']//div[" + row + "]//div["
								+ col + "]", ByTypes.xpath)
				.getCssValue("width");

		return actualWidth;

	}

	public void checkThatClassComboBoxIsNotDisplayed() throws Exception {
		webDriver.checkElementNotExist("//select[@id='" + SELECT_CLASS + "']");

	}

	public String[] getClassTestScoreResults() throws Exception {
		WebElement successWidget = webDriver.waitForElement("successWidget",
				ByTypes.id);
		List<WebElement> points = webDriver.getChildElementsByXpath(
				successWidget, "//div[contains(@class,'jqplot-point-label')]");
		String[] scores = new String[points.size()];
		for (int i = 0; i < points.size(); i++) {
			scores[i] = points.get(i).getText();
		}
		return scores;
	}

	public String getDashboardLastUpdateDateAndTime() throws Exception {
		String date = webDriver.waitForElement(
				"//div[@class='LastUpdatedDate']", ByTypes.xpath).getText();
		String time = webDriver.waitForElement(
				"//div[@class='LastUpdatedHrs']", ByTypes.xpath).getText();
		return date + " " + time;
	}

	public boolean getDashboardGoButtonStatus() throws Exception {
		WebElement element = webDriver.waitForElement(GOBUTTONID, ByTypes.id);
		webDriver.highlightElement(element);
		webDriver.printScreen("Dashboard go button");
		if (element.isEnabled()) {
			return true;
		} else {
			return false;
		}

	}

	public void clickOnTimeOnTaskReport() throws Exception {
		webDriver.waitForElement("TimeOnTask", ByTypes.id).click();

	}

	public String getTpsWidgetTitle() throws Exception {
		return webDriver.waitForElement("unitsBut", ByTypes.id).getText();

	}

	public String getClassLabelText() throws Exception {
		return webDriver.waitForElement("spnClass", ByTypes.id).getText();
	}

	public String getCourseLabelText() throws Exception {
		return webDriver.waitForElement("spnCourse", ByTypes.id).getText();
	}

	public String getNumberOfStudentsInCompletiionChart(int index)
			throws Exception {
		String value = webDriver.waitForElement(
				"//div[@id='completionRate']//div[contains(@class,'jqplot-point-"
						+ index + "')]", ByTypes.xpath).getText();
		
		return value;
	}

	public WebElement getTPSUnitElement(int index) throws Exception {
		WebElement element = webDriver.waitForElement(
				"//div[@id='unitsWrapper']//div[1]//div[1]//div[" + index
						+ "]//div[2]//div//div", ByTypes.xpath);
		return element;
	}

	public String getTPSUnitScore(WebElement element) throws Exception {
		WebElement webElement = element.findElement(By.xpath("//div[2]"));
		return webElement.getText();
	}

	public String getTPSUnitNameTooltip(int index) throws Exception {
		WebElement child = webDriver.waitForElement(
				"//div[@id='unitsWrapper']//div[1]//div[1]//div[" + index
						+ "]//div[2]//div//div//div[1]", ByTypes.xpath);
		return child.getAttribute("data-tooltip");
	}
	
	public String getTPSUnitAvgTime(int index) throws Exception {
		WebElement element = webDriver.findElementByXpath("//div[@id='unitsWrapper']//div[1]//div[1]//div[" + index	+ "]//div[2]//div//div//div[3]", ByTypes.xpath);
		
		if (element.getAttribute("class").contains("avgDuration nodata")){
			return "null";
		 }
		 else
			 return element.getText();
	}

	
	public String getTPSSkillAvgTime(int index) throws Exception {
		WebElement element = webDriver.findElementByXpath("//div[@id='skillsWrapper']//div[1]//div[1]//div[" + index	+ "]//div[2]//div//div//div[3]", ByTypes.xpath);
		
		if (element.getAttribute("class").contains("avgDuration nodata")){
			return "null";
		 }
		 else
			 return element.getText();
	}
	
	public String getSkillOnPage(int index) throws Exception {
		WebElement element = webDriver.waitForElement("//div[@id='skillsWrapper']//div[1]//div[1]//div[" + index	+ "]//div[2]//div//div//div", ByTypes.xpath, 20, true, null);
		String skillName = element.getAttribute("id");
		skillName = skillName.substring(15);
		
		return skillName;
	}
	
	
	public void clickTPSNextButton() throws Exception {
		WebElement button = webDriver.waitForElement("//button[@class='slick-next']", ByTypes.xpath);
		webDriver.hoverOnElement(button);
		button.click();
	}

	public boolean getDashboardNavBarDisplayStatus() throws Exception {
		return webDriver.waitForElement("tmsDashNav", ByTypes.id, 120, true)
				.isDisplayed();
	}

	public void clickOnUnitOverViewByskill() throws Exception {
		webDriver.waitForElement("skillBut", ByTypes.id).click();
		if (webDriver.waitForElement("skillBut", ByTypes.id).getAttribute("class").equals("graphWidgetWrapper--tab")){
			Thread.sleep(4000);
			webDriver.waitForElement("skillBut", ByTypes.id).click();
		}
	}
	
	public void waitToSelectionBar(boolean status) throws Exception {
		
		WebElement element = webDriver.waitUntilElementAppears("tmsDashNav", ByTypes.id, 20);
		boolean currentStatus=false;
	
		for (int i=0;i<20 && currentStatus==false;i++){
			
			if (status)
				currentStatus = element.getAttribute("style").equalsIgnoreCase("top: -7px;");
			else
				currentStatus = element.getAttribute("style").equalsIgnoreCase("top: -58px;");
			
			Thread.sleep(1000);
		}
	
	}

	
	
}
