package pageObjects.edo;

import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.taskdefs.Sleep;
import org.apache.tools.ant.types.resources.selectors.InstanceOf;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

//import com.google.common.base.Verify;

import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.google.common.base.CaseFormat;
import com.google.sitebricks.client.Web;

import Enums.ByTypes;
import Enums.CommunityActivities;
import Enums.Levels;
import Enums.MagazineTopics;
import drivers.FirefoxWebDriver;
import drivers.GenericWebDriver;
import drivers.SafariWebDriver;
import pageObjects.GenericPage;
import services.Reporter;
import services.TestResultService;
import testCategories.edoNewUX.HomePage;


public class NewUxMagazineArticlePage extends NewUxMagazinePage {
	
	
	@FindBy(className = "magazine__articleTitle")
	public WebElement articleTitle;
	
	@FindBy(className = "magazine__articleItemTopic")
	public WebElement articleTopic;	
	
	@FindBy(className = "magazine__articleText")
	public WebElement articleText;
	
	@FindBy(className = "magazine__articleImage")
	public WebElement articleImage;
		
	@FindBy(xpath = "//li[contains(@class,'big')]")
	public WebElement bigFontTool;
	
	@FindBy(xpath = "//li[contains(@class,'small')]")
	public WebElement smallFontTool;
	
	@FindBy(id = "kw")
	public WebElement keyWordTool;
	
	@FindBy(id = "learning__prevItem")
	public WebElement prevTaskArrow;
	
	@FindBy(id = "learning__nextItem")
	public WebElement nextTaskArrow;
	
	@FindBy(id = "CheckAnswer")
	public WebElement checkAnswer;
	
	@FindBy(id = "SeeAnswer")
	public WebElement seeAnswer;
	
	@FindBy(id = "Restart")
	public WebElement clearAnswer;
	
	@FindAll(@FindBy(className = "lessonMultipleAnswer"))
	public List <WebElement> practiceAnswers;
	
	public NewUxMagazineArticlePage(GenericWebDriver webDriver, TestResultService testResultService) throws Exception {
		super(webDriver, testResultService);
		PageFactory.initElements(webDriver.getWebDriver(), this);
		//PageFactory.initElements(webDriver, this);
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
		
	public Boolean isArticleTextDisplayed() throws Exception {
		
		return !articleText.getText().isEmpty();
	}
	
	public String getArticleDate() throws Exception {
		
		return issueMonth.getText();
	}
	
	public void verifyMainArticleByLevel(Levels level, List<String[]> expectedArticleSet) throws Exception {
		
		List<String[]> filteredSet = filterMainArticleByLevel(level, expectedArticleSet);
		String expectedTitle = null;
		String expectedText = null;
		String expectedTopic = null;
		String expectedImageId = null;
		String actualText = null;
		String actualTitle = null;
		String actualTopic = null;
		Boolean isImageIdInSrc = false; 
		
		expectedTitle = filteredSet.get(0)[0].trim();
//		expectedText = filteredSet.get(0)[1].replace("<P>","").substring(0,20);
		expectedText = filteredSet.get(0)[1].replace("<P>","").replace("<BR>","").replace("<EM>","").replace("&quot;","\"").substring(0,20);
		expectedTopic = filteredSet.get(0)[2];
		expectedImageId = filteredSet.get(0)[4];
										
		actualTitle = getArticleTitle().getText();
		actualText = getArticleText().getText().substring(0, 20);
		actualTopic = getArticleTopic().getText();
		isImageIdInSrc = getArticleImage().getAttribute("src").contains(expectedImageId);
			
		testResultService.assertEquals(expectedTitle.trim(), actualTitle.trim(), "Article title does not match");
		testResultService.assertEquals(expectedText.trim(), actualText.trim(), "Article text does not match");
		testResultService.assertEquals(expectedTopic.trim(), actualTopic.trim(), "Article topic does not match");
		testResultService.assertEquals(true, isImageIdInSrc, "Article image not found or does not match DB value");
	}
		
	public WebElement getArticleTitle () throws Exception {
		
		return articleTitle;
				
	}
	
	public WebElement getArticleText () throws Exception {
		
		return articleText;
				
	}
	
	public WebElement getArticleImage () throws Exception {
		
		return articleImage;
				
	}
		
	public WebElement getArticleTopic () throws Exception {
		
		return articleTopic;
				
	}
	
	public void verifyLargeFontEnabled() throws Exception {
		
		Boolean isLarge = webDriver.waitForElement("magazine__articleOW", ByTypes.className).getAttribute("class").contains("largeFont");
		testResultService.assertEquals(true, isLarge, "Large font is not enabled");
		
	}
	
	public void clickOnKeyWordAndVerifyGlossary(int index) throws Exception {
		
		WebElement element = getAllKeyWordsElements().get(index);
		String keyWordToTest = element.getText().toLowerCase().trim();
		element.click();
		webDriver.switchToPopup();
		String glossaryWord = webDriver.waitForElement("title0", ByTypes.className).getText().toLowerCase().trim();
		webDriver.closeNewTab(2);
		webDriver.switchToMainWindow();
//--omz 4.3.2019 Handling the scenario 'glossary' being the singular form of keyword in case of 'ies' file extension	
		//testResultService.assertEquals(true, keyWordToTest.contains(glossaryWord), "Key Word does not match to glossary word opened");
		if (true!=keyWordToTest.contains(glossaryWord))
			if (true!=keyWordToTest.replace("ies","y").contains(glossaryWord))
				testResultService.addFailTest("Key word doesn't match glossary word",false,true);
//--omz 4.3.2019 end		
		
	}
	
	public boolean isKeyWordHighlighted() throws Exception {
		
		return articleText.getAttribute("class").contains("keyWordsVisible");

	}
	
	public void selectPracticeAnswerByIndex (int answerIndex) throws Exception {
				
		/*if (webDriver.getBrowserName().equals("chrome")) {
			practiceAnswers.get(answerIndex).findElement(By.tagName("input")).click();
		} else {
			practiceAnswers.get(answerIndex).findElement(By.tagName("label")).click();
		}*/
		
		practiceAnswers.get(answerIndex).findElement(By.tagName("input")).click();
	}
	
	public int verifyAnswerRadioSelectedCorrect () throws Exception {
		
		Boolean found = false;
		int correctAnsIndex = 100;
		
		for (int i = 0; i < practiceAnswers.size(); i++) {
			Thread.sleep(500);
			found = practiceAnswers.get(i)
					.findElement(By.className("radioTextWrapper"))
					.getAttribute("class").contains("selected");
			
			if (found) correctAnsIndex = i;
			
		}		
		testResultService.assertEquals(true, (correctAnsIndex!=100), "Correct Answer is Not Selected");
		return correctAnsIndex;
	}
	
	public void verifyAnswerRadioMark (int answerIndex, boolean isExpectedCorrect) throws Exception {
		
		Boolean asExpected = false;
		if (isExpectedCorrect) {
			asExpected = practiceAnswers.get(answerIndex).findElement(By.className("radioTextWrapper")).getAttribute("class").contains("selected vCheck");
		} else if (!isExpectedCorrect) {
			asExpected = practiceAnswers.get(answerIndex).findElement(By.className("radioTextWrapper")).getAttribute("class").contains("selected xCheck");
		}
		testResultService.assertEquals(true, asExpected, "Answer is not marked as expected");
	}
	
	public void verifyAnswerRadioUnselected() throws Exception {
		
		Boolean found = false;
		int correctAnsIndex = 100;
		
		for (int i = 0; i < practiceAnswers.size(); i++) {
			found = practiceAnswers.get(i)
					.findElement(By.className("radioTextWrapper"))
					.getAttribute("class").contains("selected");
			
			if (found) correctAnsIndex = i;
			
		}		
		testResultService.assertEquals(false, (correctAnsIndex!=100), "Correct Answer is Not Selected");
		
	}
	
	public void clickOnCheckAnswer() throws Exception {
		checkAnswer.click();
	}
		
	public void clickOnSeeAnswer() throws Exception {
		seeAnswer.click();
	}

	public void clickOnClearAnswer() throws Exception {
		clearAnswer.click();
	}
	
	
	private List<WebElement> getAllKeyWordsElements() throws Exception {
		
		return articleText.findElements(By.className("kw"));
		
	}
	
	private List<String[]> filterMainArticleByLevel(Levels level, List<String[]> articlesSet) throws Exception {
		
		List<String[]> filteredSet = new ArrayList<String[]>();
		
		for (int i = 0; i < articlesSet.size(); i++) {
			if (articlesSet.get(i)[3].equals(level.toString()) && articlesSet.get(i)[5].equals("1")) filteredSet.add(articlesSet.get(i));
						
		}
		
		return filteredSet;
	}
	
}
