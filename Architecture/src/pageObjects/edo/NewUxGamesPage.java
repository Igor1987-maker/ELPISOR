package pageObjects.edo;

import java.security.PublicKey;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;

import com.google.common.base.CaseFormat;
import com.google.sitebricks.client.Web;

import Enums.ByTypes;
import Enums.CommunityActivities;
import Enums.Games;
import Enums.Levels;
import Enums.MagazineTopics;
import drivers.FirefoxWebDriver;
import drivers.GenericWebDriver;
import drivers.SafariWebDriver;
import pageObjects.GenericPage;
import services.Reporter;
import services.TestResultService;
import testCategories.edoNewUX.HomePage;


public class NewUxGamesPage extends NewUxCommunityPage {
				
	@FindBy(className = "games__indexpageTitle")
	public WebElement gamesMainTitle;
		
	@FindBy(xpath = "//li[contains(@id,'magazine__navLevel_Basic')]")
	public WebElement levelBasic;
	
	@FindBy(xpath = "//li[contains(@id,'magazine__navLevel_Intermediate')]")
	public WebElement levelIntermediate;
	
	@FindBy(xpath = "//li[contains(@id,'magazine__navLevel_Advanced')]")
	public WebElement levelAdvanced;
			
	@FindBy(className = "community__breadCrumbs")
	public WebElement breadCrumbs;
	
	@FindBy(linkText = "Community")
	public WebElement breadCrumbs_CommunityLink;
	
	@FindBy(linkText = "Games")
	public WebElement breadCrumbs_Games;
		
	@FindAll(@FindBy(className = "community__breadCrumbsItem"))
	public List<WebElement> breadCrumbs_Elements;
		
	@FindAll(@FindBy(className = "community__indexpageSecItemW"))
	public List <WebElement> gamesElements;
		
	private static final String gamesTitle = "Games";
	private static final String crosswordsTitle = "Crossword";
	private static final String crosswordsSummary = "Play Crossword and use the clues to guess all the words on the board.";
	private static final String crosswordsSource = "Vendors/games/crossword/index.html";
	private static final String snowmanTitle = "Snowman";
	private static final String snowmanSummary = "Play Snowman and use the hints to guess the words.";
	private static final String snowmanSource = "Runtime/Games/snowman/snowman.html";
	private static final String wordSearchTitle = "Word Search";
	private static final String wordSearchSummary = "Play Word Search and see if you can find all the hidden words.";
	private static final String wordSearchSource = "Runtime/Games/WordSearch/wordSearch.html";
	
	
	public NewUxGamesPage(GenericWebDriver webDriver, TestResultService testResultService) throws Exception {
		super(webDriver, testResultService);
		//PageFactory.initElements(webDriver.getWebDriver(), this);
		PageFactory.initElements(new AjaxElementLocatorFactory(webDriver.getWebDriver(), 5), this);
	
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
		
	public void verifyGamesMainTitle() throws Exception {
				
		testResultService.assertEquals(gamesTitle, gamesMainTitle.getText(), "Games Page Title not valid");
		
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
	
	public void verifyGamesElements(Games game) throws Exception {
		
				
		switch (game) {
				
		case crosswords:
			
			WebElement element =  gamesElements.get(0);
			testResultService.assertEquals(crosswordsTitle, getGameTitleByElement(element), game.toString().toUpperCase()+" Title not found / wrong");
			testResultService.assertEquals(crosswordsSummary, getGameSummaryByElement(element), game.toString().toUpperCase()+" Summary not found / wrong");
			testResultService.assertEquals(true, getGamePlayButtonByElement(element).isDisplayed(), game.toString().toUpperCase()+ " btn not displayed");
			
			
		break;
		
		case snowman:
			
			element =  gamesElements.get(1);
			testResultService.assertEquals(snowmanTitle, getGameTitleByElement(element), game.toString().toUpperCase()+" Title not found / wrong");
			testResultService.assertEquals(snowmanSummary, getGameSummaryByElement(element), game.toString().toUpperCase()+" Summary not found / wrong");
			testResultService.assertEquals(true, getGamePlayButtonByElement(element).isDisplayed(), game.toString().toUpperCase()+ " btn not displayed");
			
		break;

		case wordSearch:
			
			element =  gamesElements.get(2);
			testResultService.assertEquals(wordSearchTitle, getGameTitleByElement(element), game.toString().toUpperCase()+" Title not found / wrong");
			testResultService.assertEquals(wordSearchSummary, getGameSummaryByElement(element), game.toString().toUpperCase()+" Summary not found / wrong");
			testResultService.assertEquals(true, getGamePlayButtonByElement(element).isDisplayed(), game.toString().toUpperCase()+ " btn not displayed");
						
		break;
	
		}
				
	}
	
	public void clickOnGameAndCheckItLoaded(Games game) throws Exception {
		
		
		
		switch (game) {
				
		case crosswords:
			
			clickOnPlayByGame(game);
			testResultService.assertEquals(crosswordsTitle, breadCrumbs_Elements.get(2).getText(), game.toString().toUpperCase()+" current breadcrumb label not valid");
			webDriver.waitForElement("//iframe[@src='"+crosswordsSource+"']", ByTypes.xpath, game.toString().toUpperCase()+" source not found");
			
			
		break;
		
		case snowman:
			
			clickOnPlayByGame(game);
			testResultService.assertEquals(snowmanTitle, breadCrumbs_Elements.get(2).getText(), game.toString().toUpperCase()+" current breadcrumb label not valid");
			webDriver.waitForElement("//iframe[@src='"+snowmanSource+"']", ByTypes.xpath, game.toString().toUpperCase()+" source not found");
			
		break;

		case wordSearch:
			
			clickOnPlayByGame(game);			
			testResultService.assertEquals(wordSearchTitle, breadCrumbs_Elements.get(2).getText(), game.toString().toUpperCase()+" current breadcrumb label not valid");
			webDriver.waitForElement("//iframe[@src='"+wordSearchSource+"']", ByTypes.xpath, game.toString().toUpperCase()+" source not found");
					
		break;
	
		}
				
	}
		
	public String getGameTitleByElement (WebElement game) throws Exception {
		
		return game.findElement(By.className("community__indexpageSecItemTitle")).getText();
				
	}
	
	public String getGameSummaryByElement (WebElement game) throws Exception {
		
		return game.findElement(By.className("community__indexpageSecItemSummary")).getText();
				
	}
	
	public WebElement getGamePlayButtonByElement (WebElement game) throws Exception {
		
		return game.findElement(By.linkText("Play"));
				
	}
	
	public void clickOnPlayByGame (Games game) throws Exception {
		
		switch (game) {
		
		case crosswords:
			
			WebElement element =  gamesElements.get(0);
			getGamePlayButtonByElement(element).click();
						
		break;
		
		case snowman:
			
			element =  gamesElements.get(1);
			getGamePlayButtonByElement(element).click();
			
		break;

		case wordSearch:
			
			element =  gamesElements.get(2);
			getGamePlayButtonByElement(element).click();
			
		break;
	
		}
		
		Thread.sleep(2000);
				
	}
	
	public void verifyGamesHighlightedOnHover() throws Exception {
				
		Boolean hasBackground = false;
		Boolean hasBorder = false;
		WebElement playBtn = null;
		String btnColor = "undefined";
		
		for (int i = 0; i < gamesElements.size(); i++) {
		
			webDriver.hoverOnElement(gamesElements.get(i));
			hasBackground = !gamesElements.get(i).getCssValue("background-color").isEmpty();
			
			if (webDriver instanceof FirefoxWebDriver) {
				hasBorder = !gamesElements.get(i).getCssValue("border-top-color").contains("transparent");
			}
			else {
				hasBorder = !gamesElements.get(i).getCssValue("border").isEmpty();
			}
			
			testResultService.assertEquals(true, hasBackground, "Game has no background on hover");
			testResultService.assertEquals(true, hasBorder, "Game has no border on hover");
			
			webDriver.sleep(1);
			
			playBtn = gamesElements.get(i).findElement((By.linkText("Play")));
			webDriver.hoverOnElement(playBtn);
			btnColor = playBtn.getCssValue("background-color");
			
			testResultService.assertEquals("rgba(57, 207, 96, 1)", btnColor, "Button color is not valid after hover");
			
			webDriver.sleep(1);
			
		}
				
				
	}
	
	
	
	
	
}
