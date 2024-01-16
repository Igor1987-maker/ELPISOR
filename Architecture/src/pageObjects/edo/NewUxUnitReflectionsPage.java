package pageObjects.edo;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.openqa.selenium.WebElement;

import drivers.GenericWebDriver;
import pageObjects.GenericPage;
import services.TestResultService;

import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Enums.ByTypes;

public class NewUxUnitReflectionsPage extends NewUxHomePage {
	
	//@FindBy(className = "unitReflection__cell unitReflection__cellUnit")
	//public WebElement unitReflectionTitle;

	//@FindBy(xpath = "//*[@id=\"mainAreaTD\"]/ed-la-dummyitem/ng-component/div/div/table[2]/tbody/tr/td[2]")
	//public WebElement unitReflectionSubTitle;
	
	//@FindBy(xpath = "//*[contains(@id,'myProgress__unitReflectionBtn_idx-')]")
	//public WebElement UnitReflectionBttnTooltip;
	
	//@FindBy(className = "unitReflection__cell unitReflection__cellRadio")
	//public List<WebElement> radioBtn;
	
	@FindBy(xpath = "//*[contains(@class,'unitReflection__cell unitReflection__cellRadio')]//input")
	public List<WebElement> radioButtons;
	
	@FindBy(id = "unitReflection__save")
	public WebElement saveBtn;
	
	//@FindBy(xpath = "//*[@id=\"mainAreaTD\"]/ed-la-dummyitem/ng-component/div/div/table[3]/tbody/tr/td[1]/span")
	//public WebElement myProgressLink;
	
	//@FindBy(xpath = "//*[@id=\"alertModal\"]/div/div")
	//public WebElement SaveAlert;

	@FindBy(id = "btnOk")
	public WebElement okButton;

	@FindBy(className = "unitReflection__gotoMyProgress")
	public WebElement returnToMyProgressButton;
	
	@FindBy(xpath = "//div[@class='modal-body']")
	public WebElement alertMessage;
	
	@FindBy(id = "unitReflection__save")
	public WebElement saveButton;
	
	public NewUxUnitReflectionsPage(GenericWebDriver webDriver, TestResultService testResultService) throws Exception {
		super(webDriver, testResultService);
		PageFactory.initElements(webDriver.getWebDriver(), this);
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
	
	public void rateCheckbox() throws Exception {
		webDriver.ClickElement(radioButtons.get(0));	
	}
	
	public void clickOK() throws Exception {
		webDriver.ClickElement(okButton);
	}

	public void clickOnMyProgressReflectionLink() throws Exception{
		returnToMyProgressButton.click();
	}
	
	public void validateAlertMessage(String expectedMessage) throws Exception {
		testResultService.assertEquals(true, alertMessage.getText().contains(expectedMessage),"Alert Message is Incorrect.");
		clickOK();
	}

	public void checkDbDataIsNull(List<String> reflectionData) {
		for (int i = 0; i < reflectionData.size(); i++) {
			testResultService.assertEquals(null, reflectionData.get(i),"Reflection Data is not Null at row " + (i+1));
		}
	}
	
	public void checkDbDataIsNotNull(List<String> reflectionData) throws Exception {
		for (int i = 0; i < reflectionData.size(); i++) {
			testResultService.assertEquals(true, reflectionData.get(i) != null,"Reflection Data is Null at row " + (i+1));
		}
	}
	
	public List<String> setUnitReflection(int fillingRow) {
		List<String> scores = new ArrayList<String>();
		try {
			
			int rateIndex = 0;
			Random rand = new Random(); 
			
			for (int rowIndex = 0; rowIndex < fillingRow; rowIndex++) {
				rateIndex = rand.nextInt(3)+1;
				webDriver.waitForElement("//*[contains(@class,'idx_"+rowIndex+"_rate_"+ rateIndex +"')]",ByTypes.xpath, false, 1).click();
				scores.add(Integer.toString(rateIndex));
			}
			Thread.sleep(2000);
			webDriver.ClickElement(saveButton);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return scores;
	}
	
	public void checkDbDateIsCorrect(List<String> reflectionData) {
		Instant time = Instant.now();
		for (int i = 0; i < reflectionData.size(); i++) {
			testResultService.assertEquals(reflectionData.get(i).split(" ")[0], time.toString().split("T")[0],"Time is Incorrect at row " + (i+1));
		}
	}
	
	public void checkDbDataIsCorrectList(List<String> reflectionData, List<String> expectedValue) {
		if (reflectionData.size() == expectedValue.size()) {
			for (int i = 0; i < reflectionData.size(); i++) {
				testResultService.assertEquals(reflectionData.get(i), expectedValue.get(i),"Value is Incorrect at row " + (i+1));
			}
		} else {
			testResultService.addFailTest("The Number of Records Saved in Reflections Data is Incorrect. Saved: "+reflectionData.size()+". Actual: "+expectedValue.size());
		}
	}
	
	public void validateNumOfStoredRecords(int expectedNumOfRecords, int actualNumOfRecords) throws Exception {
		testResultService.assertEquals(Integer.toString(expectedNumOfRecords), Integer.toString(actualNumOfRecords), "The Nuber of Stored Records in DB is Incorrect");
	}
	
	public void checkDbDataIsCorrectSingleValue(List<String> reflectionData, String expectedValue) {
		for (int i = 0; i < reflectionData.size(); i++) {
			testResultService.assertEquals(reflectionData.get(i), expectedValue,"Value is Incorrect at row " + (i+1));
		}	
	}
	
	public void waitForUnitReflectionPageToBeLoaded() throws Exception {
		/*
		WebElement element = webDriver.waitForElement("//*[contains(@class,'unitReflection')]", ByTypes.xpath, false, 10);
		int counter = 0;
		while (element == null && counter < 20) {
			element = webDriver.waitForElement("//*[contains(@class,'unitReflection')]", ByTypes.xpath, false, 10);
			counter++;
		}
		*/
		boolean reflectionDisplay = webDriver.waitUntilElementAppears("//*[contains(@class,'unitReflection')]", 10);
		
		if (!reflectionDisplay)
			testResultService.addFailTest("Unit Reflection doesn't dosplay", true, true);
	}
	
	public int getUnitReflectionMaxrows() {
		int rowCount=0;
		try {
			rowCount = Integer.parseInt(webDriver.waitForElement("unitReflection_unitImageCell", ByTypes.className, false, 3).getAttribute("rowspan"));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rowCount;
	}
	
	public List<String> getUnitIdsFromReflectionsData(List<String[]> reflectionsData) {
		List<String> units = new ArrayList<String>();
		for (int i = 0; i < reflectionsData.size(); i++) {
			units.add(reflectionsData.get(i)[0]);
		}
		return units;
	}
	
	public List<String> getUnitProgressFromReflectionsData(List<String[]> reflectionsData) {
		List<String> unitProgress = new ArrayList<String>();
		for (int i = 0; i < reflectionsData.size(); i++) {
			unitProgress.add(reflectionsData.get(i)[1]);
		}
		return unitProgress;
	}
	
	public List<String> getTimeOnUnitSecondsFromReflectionsData(List<String[]> reflectionsData) {
		List<String> timeOnUnitSeconds = new ArrayList<String>();
		for (int i = 0; i < reflectionsData.size(); i++) {
			String str = reflectionsData.get(i)[2];
			timeOnUnitSeconds.add(str);
		}
		return timeOnUnitSeconds;
	}
	
	public List<String> getComponentTestAverageFromReflectionsData(List<String[]> reflectionsData) {
		List<String> componentTestAverage = new ArrayList<String>();
		for (int i = 0; i < reflectionsData.size(); i++) {
			componentTestAverage.add(reflectionsData.get(i)[3]);
		}
		return componentTestAverage;
	}
	
	public List<String> getLessonIdsFromReflectionsData(List<String[]> reflectionsData) {
		List<String> componentTestAverage = new ArrayList<String>();
		for (int i = 0; i < reflectionsData.size(); i++) {
			componentTestAverage.add(reflectionsData.get(i)[4]);
		}
		return componentTestAverage;
	}
	
	public List<String> getScoreFromReflectionsData(List<String[]> reflectionsData) {
		List<String> score = new ArrayList<String>();
		for (int i = 0; i < reflectionsData.size(); i++) {
			score.add(reflectionsData.get(i)[5]);
		}
		return score;
	}
	
	public List<String> getDateFromReflectionsData(List<String[]> reflectionsData) {
		List<String> date = new ArrayList<String>();
		for (int i = 0; i < reflectionsData.size(); i++) {
			date.add(reflectionsData.get(i)[6]);
		}
		return date;
	}
	
	public String[] checkUnitProgressData(List<String[]> unitProgressDetails) throws Exception {
		String[] details = new String[2];
		String progress = unitProgressDetails.get(0)[3];
		String testAverage = unitProgressDetails.get(0)[4];
		details[0] = progress;
		details[1] = testAverage;
		Instant time = Instant.now();
		
		testResultService.assertEquals(time.toString().split("T")[0], unitProgressDetails.get(0)[2].split(" ")[0]);
		testResultService.assertEquals(true, progress != null, "progress is null",false);
		testResultService.assertEquals(true, testAverage != null, "test average is null",false);
		return details;
	}
	
	public List<String[]> getLessonScoreList(List<String> lessons, List<String> scores) {
		
		List<String[]> newList = new ArrayList<String[]>();
		String[] temp = new String[2];
		for (int i = 0; i < scores.size(); i++) {
			temp[0] = lessons.get(i);
			temp[1] = scores.get(i);	
			newList.add(temp);
			temp = new String[2];
		}
		return newList;	
	}
	
	public void validateScoresAndLessonsWereStoredCorrectly(List<String> lessonIds, List<String> scores, List<String[]> expectedLessonsAndScores) {
		for (int i = 0; i < expectedLessonsAndScores.size(); i++) {
			for (int j = 0; j < lessonIds.size(); j++) {
				if (expectedLessonsAndScores.get(i)[0].equals(lessonIds.get(j))) {
					testResultService.assertEquals(expectedLessonsAndScores.get(i)[1],scores.get(j));
					break;
				}
			}	
		}
		
		
		//for (int i = 0; i < expectedLessonsAndScores.size(); i++) {
		//	testResultService.assertEquals(expectedLessonsAndScores.get(i)[0], lessonIds.get(i),"Lesson id is incorrect");
		//	testResultService.assertEquals(expectedLessonsAndScores.get(i)[1], scores.get(i),"Score is incorrect");
		//}	
	}
}
