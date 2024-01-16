package pageObjects.edo;

import Enums.ByTypes;
import Enums.Levels;
import Enums.MagazineTopics;
import drivers.FirefoxWebDriver;
import drivers.GenericWebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import pageObjects.GenericPage;
import services.TestResultService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class NewUxMagazinePage extends NewUxCommunityPage {
	
	
	NewUxMagazineArticlePage articlePage;
	
	public int articlesCount = 0;
		
	@FindBy(className = "magazine__mainTitle")
	public WebElement magazineMainTitle;

	@FindBy(className = "magazine__mainTitleIssue")
	public WebElement issueMonth;
		
	@FindBy(xpath = "//li[contains(@id,'magazine__navLevel_Basic')]")
	public WebElement levelBasic;
	
	@FindBy(xpath = "//li[contains(@id,'magazine__navLevel_Intermediate')]")
	public WebElement levelIntermediate;
	
	@FindBy(xpath = "//li[contains(@id,'magazine__navLevel_Advanced')]")
	public WebElement levelAdvanced;
	
	@FindBy(linkText = "Archive")
	public WebElement archiveLink;
	
	@FindBy(className = "community__breadCrumbs")
	public WebElement breadCrumbs;
	
	@FindBy(linkText = "Community")
	public WebElement breadCrumbs_CommunityLink;
	
	@FindBy(linkText = "Discoveries Magazine")
	public WebElement breadCrumbs_DiscMagazineLink;
	
	@FindAll(@FindBy(className = "community__breadCrumbsItem"))
	public List<WebElement> breadCrumbs_Elements;
	
	@FindBy(linkText = "Topics")
	public WebElement topicsLink;
	
	@FindBy(id = "magazine__mainTitleTopic")
	public WebElement topicTitle;
	
	@FindBy(id = "magazine__getMoreItems")
	public WebElement loadMoreLink;	
	
	@FindBy(id = "magazine__mainTitleIssueRowCount")
	public WebElement issuesCountInTopic;	
	
	@FindAll(@FindBy(className = "magazine__articleItemW"))
	public List <WebElement> magazineArticles;
	
	@FindBy(xpath = "//input[@type='search']")
	public WebElement searchBar;
	
	@FindBy(xpath = "//*[@id='magazine__header']/div[2]/span")
	public WebElement searchButton;	
	
	@FindBy(xpath = "//*[@id='alertModal']/div/div/div/div[3]/div")
	public WebElement alertMessage;	
	
	@FindBy(id = "btnOk")
	public WebElement continueButton;
	
	public NewUxMagazinePage(GenericWebDriver webDriver, TestResultService testResultService) throws Exception {
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
	
	public void verifyCorrectSearchBarResult() throws Exception {  //works only on main
	
	report.startStep("Fill less than 3 characters to searchbar and click on Search");
		webDriver.waitForElementByWebElement(searchBar, "Search bar", true, 10);
		searchBar.click();
		searchBar.clear();
		searchBar.sendKeys("dg");
		searchButton.click();
		Thread.sleep(2000);
		
	report.startStep("Verify worning message of incorrect, short parameter");
		webDriver.waitForElementByWebElement(alertMessage, "Worning after wrong input to search bar", true, 10);
		String message = alertMessage.getText().toString();
		textService.assertEquals("Wrong message", "Please enter a word with at least 3 letters.", message);
		continueButton.click();
		
	report.startStep("Fill correct name to search");
		webDriver.waitForElementByWebElement(searchBar, "Search bar", true, 10);	
		Thread.sleep(2000);
		searchBar.click();
		searchBar.clear();
		searchBar.sendKeys("dog");
		searchButton.click();
		
	report.startStep("Verify all articles founded according wanted name");
		Thread.sleep(2000);
		for(WebElement el:magazineArticles) {
			WebElement title = el.findElement(By.className("magazine__articleItemTitle"));
			String textOfTitle = title.getText().toString().toLowerCase();
			//testResultService.addFailTest("delete progress failed", false, false);
			textService.assertTrue("Article not correct",textOfTitle.contains("dog"));
		}
	}
		
	public void verifyMagazineMainTitle() throws Exception {
		
		String actualTitle = getMagazineMainTitle();
		testResultService.assertEquals(magazineNewTitle, actualTitle, "Magazine Page Title not valid");
		
	}
	
	public void verifyMagazineCurrentIssueMonth() throws Exception {
		
		String currentMonthYear = getCurrentMonthYear();
		String actualIssue = issueMonth.getText();
		testResultService.assertEquals(currentMonthYear, actualIssue, "Magazine Issue Month/Year not valid");
		
	}
	
	public void verifyArchiveMagazineIssueMonth(String expectedMonth, String expectedYear) throws Exception {
		
		String expectedMonthYear = expectedMonth + " " + expectedYear;
		String actualIssue = issueMonth.getText();
		testResultService.assertEquals(expectedMonthYear, actualIssue, "Magazine Issue Month/Year not valid");
		
	}
	
	public void clickOnLevel(Levels level) throws Exception {
		
		switch (level) {
		
		case Basic:
			levelBasic.click();
		break;
		
		case Intermediate:
			levelIntermediate.click();
		break;

		case Advanced:
			levelAdvanced.click();
		break;

		}
		
		
	}
	
	public void verifyCommunityLevelSelected(Levels expectedLevel) throws Exception {
		
		Boolean isSelected = false;
		
		switch (expectedLevel) {
				
		case Basic:
			isSelected = levelBasic.getAttribute("class").contains("selected");
		break;
		
		case Intermediate:
			isSelected = levelIntermediate.getAttribute("class").contains("selected");
		break;

		case Advanced:
			isSelected = levelAdvanced.getAttribute("class").contains("selected");
		break;
	
		}
		
		testResultService.assertEquals(true, isSelected, "Community level selection is not valid");
		
	}
		
	public void verifyMagazineArticlePreviewByLevel(Levels level, List<String[]> expectedArticleSet) throws Exception {
			
		List<String[]> filteredSet = filterArticlesSetByLevel(level, expectedArticleSet);
		String expectedTitle = null;
		String expectedText = null;
		String expectedTopic = null;
		String expectedImageId = null;
		String actualText = null;
		String actualTopic = null;
		Boolean isImageIdInSrc = false; 
		Boolean isReadMoreDisplayed = false;
		String [] actualTitles = new String [magazineArticles.size()];
		WebElement articleToTest = null;
		int textVerifyLength = 13;
		
		webDriver.sleep(2);
		
		for (int i = 0; i < magazineArticles.size(); i++) {
			actualTitles[i] = magazineArticles.get(i).findElement(By.className("magazine__articleItemTitle")).getText();
			//actualTitles[i] = actualTitles[i].replace("\"", "");
		}
		
		
		for(int i = 0; i < filteredSet.size(); i++) {
			
			expectedTitle = filteredSet.get(i)[0].replace("&quot;", "").trim();
			
//			expectedText = filteredSet.get(i)[1].replace("<P>","").substring(0,textVerifyLength);
			expectedText = filteredSet.get(i)[1].replace("<P>","").replace("<BR>","").replace("&quot;", "\"").substring(0,textVerifyLength);
						
			expectedTopic = filteredSet.get(i)[2];
			
			expectedImageId = filteredSet.get(i)[4];
			
			int count = 0;
			int index = 0;
			
			while (!expectedTitle.substring(0,5).equals(actualTitles[count].substring(0,5)) && count <= actualTitles.length) {
				count++;
				index = count;
			}
				
			articleToTest = magazineArticles.get(index);
			
			actualText = getArticlePreviewText(articleToTest).getText().substring(0, textVerifyLength);
			
			actualTopic = getArticleTopic(articleToTest).getText();
			
			isImageIdInSrc = getArticleImage(articleToTest).getCssValue("background-image").contains(expectedImageId);
			
			isReadMoreDisplayed = getArticleReadButton(articleToTest).isDisplayed();
			
			
			testResultService.assertEquals(expectedTitle, actualTitles[index], "Article title does not match");
			//testResultService.assertEquals(expectedText, actualText, "Article text does not match");
			testResultService.assertTrue("Article text does not match", expectedText.contains(actualText));
			testResultService.assertEquals(expectedTopic, actualTopic, "Article topic does not match");
			testResultService.assertEquals(true, isImageIdInSrc, "Article image not found or does not match DB value");
			testResultService.assertEquals(true, isReadMoreDisplayed, "Article Read More btn not displayed");

		}
		
	}
	
	public List<WebElement> getArticlesOnPage() throws Exception {
		
		return magazineArticles;
				
	}
	
	public WebElement getArticleTitle (WebElement article) throws Exception {
				
		return article.findElement(By.className("magazine__articleItemTitle"));
				
	}
	
	public WebElement getArticlePreviewText (WebElement article) throws Exception {
		webDriver.waitUntilElementAppears("magazine__articleItemText", ByTypes.className, 10);
		return article.findElement(By.className("magazine__articleItemText"));
				
	}
	
	public WebElement getArticleImage (WebElement article) throws Exception {
		
		return article.findElement(By.className("magazine__articleItemImage"));
				
	}
	
	public WebElement getArticleReadButton (WebElement article) throws Exception {
		
		return article.findElement(By.linkText("Read More"));
				
	}
	
	public WebElement getArticleTopic (WebElement article) throws Exception {
		
		return article.findElement(By.className("magazine__articleItemTopic"));
				
	}
	
	public WebElement getArticleIssueDate (WebElement article) throws Exception {
		
		return article.findElement(By.className("magazine__articleItemDate"));
				
	}
	
	public String getMagazineMainTitle() throws Exception {
		
		return magazineMainTitle.getText().split(issueMonth.getText())[0].trim();
				
	}
	
	public int getNumberOfIssuesInTopic() throws Exception {
		
		String numberS = issuesCountInTopic.getText().split(" ")[0];
		int numberI = Integer.parseInt(numberS);
		
		return numberI;
				
	}
	
	public List<String []> verifyArticlesPreviewInTopicByLevel(Levels level, String topicName, List<String []> errorList) throws Exception {
		
		boolean isBroken = false;
		String date = null;
		String title = null;
		String comLevel = level.toString();
		
		for (int i = 0; i < magazineArticles.size(); i++) {
			
			isBroken = magazineArticles.get(i).findElement(By.className("magazine__articleItemText")).getText().isEmpty();
			
			if (isBroken) {
				date = magazineArticles.get(i).findElement(By.className("magazine__articleItemDate")).getText();
				title = magazineArticles.get(i).findElement(By.className("magazine__articleItemTitle")).getText();
				testResultService.addFailTest("Preview text not displayed in issue : ----- "+date+" / "+title+" / "+topicName+ " / "+ comLevel);
				errorList.add(new String[]{date, title, topicName, comLevel});				
			}
			
			
			
		}
			
		//articlesCount = articlesCount + magazineArticles.size();
				
		return errorList;
				
	}
	
	public List<String []> verifyEveryArticlePageInTopicByLevel(Levels level, String topicName, List<String []> errorList) throws Exception {
		
		boolean isBroken = false;
		String date = null;
		String title = null;
		String comLevel = level.toString();
		List <String> consoleParseErrors;
		String consoleLog = null;
		String url = null;
		
		//for (int i = 0; i < magazineArticles.size(); i++) {
		int countArticleToTest= 1;
		for (int i = 0; i <= countArticleToTest; i++) {
			webDriver.scrollToElement(magazineArticles.get(i)); // no need to enter all Articles
			Thread.sleep(1000);
			
			articlePage = clickOnReadMoreByIndex(i);
			//Thread.sleep(3000);
			
			url = webDriver.getUrl();
			
			/*clickOnLevel(level); // level state not saved in article - need to wait fix
			webDriver.sleep(1);*/
			
			isBroken = !articlePage.isArticleTextDisplayed();
			consoleParseErrors = webDriver.getConsoleLogs("$parse", true);
			if (!consoleParseErrors.isEmpty()) consoleLog = consoleParseErrors.toString();
			else consoleLog = null;
			
			if (isBroken || consoleLog != null) {
				date = articlePage.getArticleDate();
				title = articlePage.articleTitle.getText();
				testResultService.addFailTest("Article not displayed or has errors in issue : ----- "+date+" / "+title+" / "+ topicName +" / "+ comLevel);
				errorList.add(new String[]{date, title, topicName, comLevel, url});				
			}
			
			webDriver.getWebDriver().navigate().back();
			Thread.sleep(300);
			webDriver.refresh();
			
							
			clickOnReadMoreLinkInTopic();
			Thread.sleep(300);
			
			//if (i == (magazineArticles.size()-1)){
			if (i == countArticleToTest){
			webDriver.scrollToTopOfPage();
			Thread.sleep(1000);
			}
		}
			
		articlesCount = articlesCount + magazineArticles.size();
		
		return errorList;
				
	}
		
	public void verifyMainFeatureDisplayedFirst(List<String[]> articlesSet) throws Exception {
		
		String actualFirstTitle = magazineArticles.get(0).findElement(By.className("magazine__articleItemTitle")).getText().trim();
		String expectedFirstTitle = null;
		for (int j = 0; j < articlesSet.size(); j++) {
			if (articlesSet.get(j)[5].trim().equals("1"))
				expectedFirstTitle = articlesSet.get(j)[0].trim();
		}
		
		testResultService.assertEquals(expectedFirstTitle.trim(), actualFirstTitle.trim(),
				"Main Feature article not first from left");
				
	}
	
	public void verifyArticlesHighlightedOnHover() throws Exception {
				
		Boolean hasBackground = false;
		Boolean hasBorder = false;
		WebElement readMoreBtn = null;
		String btnColor = "undefined";
		
		for (int i = 0; i < magazineArticles.size(); i++) {
		
			webDriver.hoverOnElement(magazineArticles.get(i));
			hasBackground = !magazineArticles.get(i).getCssValue("background-color").isEmpty();
			
			if (webDriver instanceof FirefoxWebDriver) {
				hasBorder = !magazineArticles.get(i).getCssValue("border-top-color").contains("transparent");
			}
			else {
				hasBorder = !magazineArticles.get(i).getCssValue("border").isEmpty();
			}
			
			testResultService.assertEquals(true, hasBackground, "Article has no background on hover");
			testResultService.assertEquals(true, hasBorder, "Article has no border on hover");
			
			webDriver.sleep(1);
			
			readMoreBtn = magazineArticles.get(i).findElement((By.linkText("Read More")));
			webDriver.hoverOnElement(readMoreBtn);
			btnColor = readMoreBtn.getCssValue("background-color");
			
			testResultService.assertEquals("rgba(57, 207, 96, 1)", btnColor, "Button color is not valid after hover");
			
			webDriver.sleep(1);
			
		}
				
				
	}
	
	public void clickOnArchive() throws Exception {
		
		archiveLink.click();
				
	}
	
	public List<WebElement> clickOnTopics() throws Exception {
		
		//topicsLink.click();
		WebElement element = webDriver.waitUntilElementAppearsAndReturnElementByType("Topics", ByTypes.linkText, 10);
		element.click();
		Thread.sleep(1000);
		
		return getTopicsElements();
				
	}
		
	public void selectMonthAndYearInArchive(String month, String year, boolean clickOK) throws Exception {
				
		navigateToRequiredYearInCalPicker(year);
				
		WebElement monthElement = getMonthsElementInCalPicker().findElement(By.xpath("//span[text()='"+month+"']"));
		monthElement.click();
		webDriver.sleep(1);
		
		if (clickOK) clickOkInArchive();
		
	}
	
	public void clickOkInArchive() throws Exception {
		
		webDriver.waitForElement("calpicker__goBtn", ByTypes.className).click();
		
	}
	
	public void verifyCurrentMonthYearSelectedOnArchiveOpen() throws Exception {
		
		String selectedYear = getYearsElementInCalPicker().findElement(By.xpath("//li[contains(@class,'current')]")).getText();
		String selectedMonth = getMonthsElementInCalPicker().findElement(By.xpath("//span[contains(@class,'selected')]")).getText();
		
		String currentYear = getCurrentMonthYear().split(" ")[1];
		String currentMonth = getCurrentMonthYear().split(" ")[0];
		
		testResultService.assertEquals(currentYear, selectedYear, "Currrent year not selected");
		testResultService.assertEquals(currentMonth, selectedMonth, "Currrent month not selected");
		
	}
	
	public void verifyRightArrowCalPickerDisabled() throws Exception {
						
		testResultService.assertEquals(true, getNextYearArrowElementInCalPicker().getAttribute("class").contains("disabled"), "Next btn not disabled");
		
	}
	
	public void verifyFutureMonthsInCalPickerDisabled() throws Exception {
		
		List<WebElement> allMonths = getMonthsElementInCalPicker().findElements(By.className("calpicker__month"));
		String currentMonth = getCurrentMonthYear().split(" ")[0];
		Boolean isCurrentMonth = false;
		int found = 0;
		
		for (int i = 0; i < allMonths.size(); i++) {
			
			if (found == 1) {
				testResultService.assertEquals(true, allMonths.get(i).getAttribute("class").contains("disabled"), "Future month not disabled");
			}
			isCurrentMonth = allMonths.get(i).getText().equals(currentMonth);
			if (isCurrentMonth) found ++; 
		}
		
	}
	
	public void clickOnBackYearArrowInCalPicker(int timesToClick) throws Exception {
		
		for (int i = 0; i < timesToClick; i++) {
			getPrevYearArrowElementInCalPicker().click();
			webDriver.sleep(1);
		}
		
	}
		
	public void navigateToRequiredYearInCalPicker(String yearToFind) throws Exception {
		
		String actualYear = getSelectedYearInCalPicker();
		int count = 0;
		while (count < 15 && !(actualYear.equals(yearToFind))) {
		getPrevYearArrowElementInCalPicker().click();
		Thread.sleep(1000);
		actualYear = getSelectedYearInCalPicker();
		count++;
		} 
		
		count = 0;
		while (count < 15 && !(actualYear.equals(yearToFind))) {
		getNextYearArrowElementInCalPicker().click();
		Thread.sleep(1000);
		actualYear = getSelectedYearInCalPicker();
		count++;
		} 	
				
		
	}
	
	public String clickOnSpecificTopic(MagazineTopics topic) throws Exception {
		
		List<WebElement> allTopics = getTopicsElements();
		WebElement topicToSelect = null;
				
		for (int i = 0; i < allTopics.size(); i++) {
			
			if (allTopics.get(i).getText().contains(topic.toString())) {
				topicToSelect = allTopics.get(i);
								
			}
			
		}
			String topicName = topicToSelect.getText();	
			topicToSelect.click();
		
		
		return topicName;
				
	}
	
	public List<WebElement> getTopicsElements() throws Exception {
		webDriver.waitUntilElementAppears("topicpicker__topicItem", ByTypes.className, 5);
		return webDriver.waitForElement("topicpicker__topics",ByTypes.className).findElements(By.className("topicpicker__topicItem"));
				
	}
	
	public void verifyTopicTitle(String expectedTitle) throws Exception {
		webDriver.waitUntilElementAppears(topicTitle,10);
		Thread.sleep(1500);
		testResultService.assertEquals(expectedTitle, topicTitle.getText(), "Topic title on page is not valid");
				
	}
	
	public void verifyArticleElementsDisplayed(WebElement article) throws Exception {
		
		testResultService.assertEquals(true, getArticleIssueDate(article).isDisplayed(), "Issue date not found");
		testResultService.assertEquals(true, getArticleTitle(article).isDisplayed(), "Title not found");
		testResultService.assertEquals(true, getArticleImage(article).isDisplayed(), "Image not found");
		testResultService.assertEquals(true, getArticlePreviewText(article).isDisplayed(), "Preview text not found");
		testResultService.assertEquals(true, getArticleReadButton(article).isDisplayed(), "Read More btn not found");
	}
	
	public void verifyIssuesSortedByDateInTopics(List<WebElement> articles) throws Exception {
		
		String dateString = "undefined";
		Date date = null; 
		
		String nextDateString = "undefined";
		Date nextDate = null; 
				
		for (int i = 0; i < articles.size(); i++) {
		
			if (i == (articles.size()-1)) break;
			
			dateString = getArticleIssueDate(articles.get(i)).getText();
			date = getDateFromIssueDateString(dateString);
			
			nextDateString = getArticleIssueDate(articles.get(i+1)).getText();
			nextDate = getDateFromIssueDateString(nextDateString);
			
			if (date.before(nextDate)) testResultService.addFailTest("Issues not sorted by date from latest to oldest", false, true);
						
							
		}
		
	}
	
	public void clickOnReadMoreLinkInTopic() throws Exception {
		//loadMoreLink.click();
		webDriver.waitUntilElementAppears("magazine__getMoreItems", ByTypes.id,10);
		webDriver.waitForElement("magazine__getMoreItems", ByTypes.id, "Read More link not found").click();
		
	}
	
	public NewUxMagazineArticlePage clickOnReadMoreByIndex (int index) throws Exception {
		webDriver.waitUntilElementAppears(magazineArticles, 10);
		magazineArticles.get(index).findElement(By.linkText("Read More")).click();
		webDriver.waitUntilElementAppears("//*[@id='mainContent']/section/div[3]/div[2]/div/div[1]/div[1]/div/div[3]/div[2]/div",ByTypes.xpath, 10);
		
		return new NewUxMagazineArticlePage(webDriver, testResultService);
				
	}
	
	public void waitMagazineArticleLoaded() throws Exception {
		webDriver.waitUntilElementAppears("magazine__mainTitleW",ByTypes.className, 5);		
	}
	
	
	
	private String getCurrentMonthYear() throws Exception {
	
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy", Locale.US);
		String currentMonthYear = sdf.format(date);
	
		return currentMonthYear;
	}
	
	private Date getDateFromIssueDateString(String monthYear) throws Exception {
			
		DateFormat format = new SimpleDateFormat("MMMM yyyy", Locale.US);
		Date date = format.parse(monthYear);
				
		return date;
		
	}
		
	private WebElement getNextYearArrowElementInCalPicker() throws Exception {
			
		return webDriver.waitForElement("calpicker__arrowNext", ByTypes.id, "Right arrow in calpicker");
	}
	
	private WebElement getPrevYearArrowElementInCalPicker() throws Exception {
		
		return webDriver.waitForElement("calpicker__arrowBack", ByTypes.id, "Left arrow in calpicker");
	}
	
	private WebElement getYearsElementInCalPicker() throws Exception {
		
		return webDriver.waitForElement("calpicker__yearsIW",ByTypes.className);
				
	}
	
	private String getSelectedYearInCalPicker() throws Exception {
		
		return getYearsElementInCalPicker().findElement(By.xpath("//li[contains(@class,'current')]")).getText();
				
	}
	
	private WebElement getMonthsElementInCalPicker() throws Exception {
		
		return webDriver.waitForElement("calpicker__months",ByTypes.className);
				
	}
		
	private List<String[]> filterArticlesSetByLevel(Levels level, List<String[]> articlesSet) throws Exception {
			
		List<String[]> filteredSet = new ArrayList<String[]>();
		
		for (int i = 0; i < articlesSet.size(); i++) {
			if (articlesSet.get(i)[3].equals(level.toString())) filteredSet.add(articlesSet.get(i));
						
		}
		
		return filteredSet;
				
	}
	
	
	
}
