package pageObjects.edo;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;






//import org.apache.tools.ant.taskdefs.Sleep;
//import org.apache.xpath.operations.Bool;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

//import com.google.common.base.Verify;





import org.xbill.DNS.NULLRecord;

import Enums.ByTypes;
import Enums.CommunityActivities;
import drivers.GenericWebDriver;
import pageObjects.GenericPage;
import services.Reporter;
import services.TestResultService;

public class NewUxCommunityPage extends GenericPage {

	private static final String intoText = ", welcome to our English learners community. Contact people from all over the world and communicate with them in English.";
	private static final String forumTitle = "Discussion Time Forum";
	private static final String forumContent = "Join the Discussion Time forums to exchange information and opinions with other learners about any of your favorite topics.";
	private static final String forumSummary = "Join the Discussion Time Forums to exchange information and opinions with other learners about any of your favorite topics.";
	private static final String forumImg = "forum.gif";

	private static final String magazineTitle = "Magazine";
	protected static final String magazineNewTitle = "Discoveries Magazine";
	private static final String magazineContent = "Read English Online magazine, for all the latest news and views. Written in English at your level.";
	private static final String magazineSummary = "Read Discoveries Magazine for all the latest news and views. Written in English at your level.";
	private static final String magazineImg = "magazine.gif";

	private static final String bbcLearningTitle = "BBC Learning English";
	private static final String bbcLearningContent = "Edusoft in cooperation with BBC Learning...";
	private static final String bbcImg = "BBCSmall.gif";

	private static final String webPalTitle = "Web Pals";
	private static final String webPalsContent = "Connect with other English students and find the perfect pal!";
	private static final String webPalsImg = "WebPals.gif";

	private static final String gamesTitle = "Games";
	private static final String gamesContent = "Practice your English by playing word games.";
	private static final String wordZoneImg = "WordZone.gif";
	private static final String wordZoneTitle = "WordZone";
	private static final String wordZoneContent = "undefined";

	private static final String talkingIdiomsTitle = "Talking Idioms";
	private static final String talkingIdiomsContent = "Impress your friends and teachers by using idioms next time you speak English!";
	private static final String talkingIdiomsImg = "idioms.gif";

	private static final String communityForumDiscLocal = "#This feature is not available because you are not connected to Edusoft's Global Community.";
	
	private static final String newCommunityBannerImagePath = "/Images/Community/community_header_overlay.png";

	private int smallTimeOut = 5;
	
	public Reporter reporter;
	

	public NewUxCommunityPage(GenericWebDriver webDriver, TestResultService testResultService) throws Exception {
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

	public void SwitchToCommunityIFrame() throws Exception {

		webDriver.switchToFrame(webDriver.waitForElement("oed__iframe", ByTypes.className, true, webDriver.getTimeout()));
	}

	public void VerifyCommunityPagesTitle() throws Exception {

		testResultService.assertTrue("The Community Page wasn't displayed properly.",
				webDriver.waitForElement("h1", ByTypes.tagName).getText().equals("Community"));
	}

	public void VerifyCommunityPageIntroText(String studentName) throws Exception {
		testResultService.assertTrue("Community Pages wasn't displayed properly or the intro text was incorrect.",
				webDriver.waitForElement("tdOpeningMsg", ByTypes.id).getText().equals(studentName + intoText));
	}

	public void CommunitySectionTableVerification() throws Exception {
		WebElement mainAreavDiv = webDriver.waitForElement("mainAreaDiv", ByTypes.className, true, webDriver.getTimeout());


		VerifyCommunityMainAreaElements(forumImg, forumTitle, forumContent, mainAreavDiv.findElement(By.id("ct1")));
		VerifyCommunityMainAreaElements(magazineImg, magazineTitle, magazineContent,
				mainAreavDiv.findElement(By.id("ct2")));
		VerifyCommunityMainAreaElements(bbcImg, bbcLearningTitle, bbcLearningContent,
				mainAreavDiv.findElement(By.id("ct3")));
		VerifyCommunityMainAreaElements(webPalsImg, webPalTitle, webPalsContent,
				mainAreavDiv.findElement(By.id("ct4")));
		VerifyCommunityMainAreaElements(wordZoneImg, wordZoneTitle, wordZoneContent,
				mainAreavDiv.findElement(By.id("ct5")));
		VerifyCommunityMainAreaElements(talkingIdiomsImg, talkingIdiomsTitle, talkingIdiomsContent,
				mainAreavDiv.findElement(By.id("ct6")));
	}

	private void VerifyCommunityMainAreaElements(String imageName, String elementTitle, String elementTextContent,
			WebElement mainAreaElement) throws Exception {
		// Add logging steps
		WebElement imgElement = mainAreaElement.findElement(By.tagName("img"));

		testResultService.assertEquals(true, webDriver.isImageLoaded(imgElement),
				"The Image element was not loaded properly");
		testResultService.assertEquals(true, imgElement.getAttribute("src").contains(imageName),
				"The name of the displayed image was not as expected");

		testResultService.assertEquals(elementTitle,
				mainAreaElement.findElement(By.className("communitySectionTitle")).getText(),
				"Elements Title contained incorrect text");
		testResultService.assertEquals(elementTextContent,
				mainAreaElement.findElement(By.className("communitySectionContent")).getText(),
				"Elements Content contained incorrect text");
		testResultService.assertTrue("", mainAreaElement.findElement(By.className("communityLinks")).isDisplayed());
	}

	public void ValidateCommunityLinksAndPages() throws Exception {
		webDriver.waitForElement("ct1", ByTypes.id, true, webDriver.getTimeout()).findElement(By.className("communityLinks")).click();
		//VerifySiteNavigation("Forum"); // REMOVED FOR NEW COMMUNITY DEFINITION
		ForumPageContentVerification();
		ReturnToMainCommunityPage();

		webDriver.waitForElement("ct2", ByTypes.id, true, webDriver.getTimeout()).findElement(By.className("communityLinks")).click();
		VerifySiteNavigation("Magazine");
		MagazinePageContentVerification();
		ReturnToMainCommunityPage();

		webDriver.waitForElement("ct3", ByTypes.id, true, webDriver.getTimeout()).findElement(By.className("communityLinks")).click();
		webDriver.getTimeout();
		VerifySiteNavigation("BBC Learning English");
		ReturnToMainCommunityPage();

		webDriver.waitForElement("ct4", ByTypes.id, true, webDriver.getTimeout()).findElement(By.className("communityLinks")).click();
		VerifySiteNavigation("Web Pals");
		ReturnToMainCommunityPage();

		webDriver.waitForElement("ct5", ByTypes.id, true, webDriver.getTimeout()).findElement(By.className("communityLinks")).click();
		VerifySiteNavigation("WordZone");
		ReturnToMainCommunityPage();

		webDriver.waitForElement("ct6", ByTypes.id, true, webDriver.getTimeout()).findElement(By.className("communityLinks")).click();
		VerifySiteNavigation("Talking Idioms");
		TalkingIdiomsCommunityPageVerification();
		ReturnToMainCommunityPage();
	}

	private void TalkingIdiomsCommunityPageVerification() throws Exception {
		Thread.sleep(10000);
		WebElement idiomContainer = webDriver.waitForElement("idiomContainer", ByTypes.id, true,
				webDriver.getTimeout());
		List<WebElement> idiomsList = idiomContainer.findElements(By.tagName("li"));

		testResultService.assertEquals(false, idiomsList.isEmpty(), "The list of Idioms was empty while it should ");
	}

	private void WebPalsCommunityPage() throws Exception {
		testResultService.assertEquals(true, webDriver.waitForElement("tblInbox", ByTypes.id).isDisplayed(),
				"The Inbox table in the Community Web Pals page wasn't displayed");
	}

	private void BBCLearningEnglishCommunityPageVerification() throws Exception {

		WebElement webElement = webDriver.waitForElement("//*[@id='t1']", ByTypes.xpath, true, webDriver.getTimeout());
		WebElement webElement2 = webDriver.waitForElement("t2", ByTypes.id, true, webDriver.getTimeout());

		testResultService.assertEquals("The Flatmates -",
				webDriver.waitForElement("t1", ByTypes.id, webDriver.getTimeout(), true).getAttribute("value"),
				"The first title of the BBC Learning English page was incorrect");
		testResultService.assertEquals("6 Minute English -",
				webDriver.waitForElement("t2", ByTypes.id, true, webDriver.getTimeout()).getAttribute("value"),
				"The second title of the BBC Learning English page was incorrect");
	}

	private void MagazinePageContentVerification() throws Exception {

		testResultService.assertEquals("Advanced",
				webDriver.waitForElement("level", ByTypes.id, true, webDriver.getTimeout()).getAttribute("value"),
				"The Site Level did not match the level defined in the user profile");
		testResultService.assertTrue("The Title of the magazine was not displayed", webDriver.waitForElement("//div[contains(@class, 'stext articleTitle')]", ByTypes.xpath,
						webDriver.getTimeout(), true).isDisplayed());
	}

	private void ReturnToMainCommunityPage() throws Exception {
		//webDriver.waitForElement("siteNav", ByTypes.id).findElement(By.tagName("a")).click();
		webDriver.refresh();
		webDriver.sleep(2);
		SwitchToCommunityIFrame();
	}

	public void ForumPageContentVerification() throws Exception {

		testResultService.assertEquals(true,
				webDriver.waitForElement("//img[contains(@src, 'logoBig.gif')]", ByTypes.xpath).isDisplayed(),
				"The Logo image wasn't displayed as expected");
		testResultService.assertEquals(true,
				webDriver.waitForElement("//img[contains(@src, 'textLogoBig.gif')]", ByTypes.xpath).isDisplayed(),
				"The TextLogo image wasn't displayed as expected");

		WebElement communityDiscussion = webDriver.waitForElement("topicsCont", ByTypes.id, true,
				webDriver.getTimeout());
		List<WebElement> cdListItems = communityDiscussion.findElements(By.tagName("li"));

		if (cdListItems.isEmpty()) {
			testResultService.assertEquals(communityForumDiscLocal, communityDiscussion.getText(),
					"The displayed text in the Community Disscusion section was not as expected");
		} else {
			for (WebElement webElement : cdListItems) {
				testResultService.assertEquals(true, webElement.findElement(By.tagName("a")).isDisplayed(),
						"The expected Community Discussion links were not displayed as expected");
			}
		}
	}
		
	private void VerifySiteNavigation(String currentPage) throws Exception {
		String siteNavigation = webDriver.waitForElement("siteNav", ByTypes.id, webDriver.getTimeout(), false, "The site navigation wasn't displayed on page").getText();
		
		testResultService.assertEquals("Community>>" + currentPage, siteNavigation,
				"Site Navigation text was displayed incorrectly");
	}
	
	public void verifyOldBreadcrumbNavigationNotDisplayed() throws Exception {
		WebElement element = webDriver.waitForElement("siteNav", ByTypes.id, smallTimeOut, false, "The site navigation");
		
		if(element!=null) testResultService.addFailTest("Navigation was displayed though it should not");
	}
	
	public void clickOnForumCommunityDiscussionByPartialLink(String partialLink) throws Exception {
		WebElement element = webDriver.waitForElement(partialLink, ByTypes.partialLinkText, "Discussion link not found");
		element.click();		
	}
	
	public void clickOnForumHomeLink() throws Exception {
		WebElement element = webDriver.waitForElement("Discussion Time Home", ByTypes.partialLinkText, "Forum home link not found");
		element.click();		
	}
	
	public WebElement getAddNewMessageBtn() throws Exception {
		return webDriver.waitForElement("Add New Message", ByTypes.linkText, "Add New Message btn in forum not found");
	}
	
	public void VerifyMagazineNavigationLinkPromoSection() throws Exception {
		testResultService.assertEquals("Advanced",
				webDriver.waitForElement("level", ByTypes.id, true, webDriver.getTimeout()).getAttribute("value"),
				"The Site Level did not match the level defined in the user profile");
		testResultService.assertEquals(true, webDriver.waitForElement("//div[contains(@class, 'stext articleTitle')]",
				ByTypes.xpath, webDriver.getTimeout(), true).isDisplayed(), "Magazines Title wasn't displayed");
	}
	
	public void openTalkingIdiomsPage() throws Exception {
		
		webDriver.waitForElement("ct6", ByTypes.id, true, webDriver.getTimeout()).findElement(By.className("communityLinks")).click();
		
	}
	
	public void selectIdiomFromList(String id) throws Exception {
		webDriver.waitForElement("//ul[@id='idiomList']//li["+id+"]", ByTypes.xpath).click();

	}

	public void clickOnRecordFromIdioms() throws Exception {
		webDriver.waitForElement("//*[@id='mainAreaTD']/div[1]/div/a",
				ByTypes.xpath).click();
		webDriver.waitForElement("recordLI", ByTypes.id).click();

	}
	
	public void switchToRecordPanel() throws Exception {
		webDriver.switchToFrame(webDriver.waitForElement("cboxIframe", ByTypes.className));
		

	}
	
	public void checkThatForumLinkNotDisplayed() throws Exception {
		webDriver.waitForElement("mainAreaDiv", ByTypes.className);
		WebElement element = webDriver.waitForElement("ct1", ByTypes.id, false, smallTimeOut);
		if (element != null) {
			testResultService.addFailTest("Forum link was displayed while it should have been hidden", false, true);
		}
	}
	
	public void checkThatWebPalsLinkNotDisplayed() throws Exception {
		webDriver.waitForElement("mainAreaDiv", ByTypes.className);
		WebElement element = webDriver.waitForElement("ct4", ByTypes.id, false, smallTimeOut);
		if (element != null) {
			testResultService.addFailTest("Web Pals link was displayed while it should have been hidden", false, true);
		}
	}
	
	private WebElement getNewCommunityBannerElement() throws Exception {
		
		return webDriver.waitForElement("community__header", ByTypes.className, true, smallTimeOut);
		
	}
	
	public void verifyNewCommunityBanner() throws Exception {
		
		boolean isActualImageMatch = getNewCommunityBannerElement().getCssValue("background-image").contains(newCommunityBannerImagePath);
		testResultService.assertEquals(true, isActualImageMatch, "Banner image does not match or not displayed");
	}
	
	public void verifyNewCommunityFrontPageBlocks(CommunityActivities activity) throws Exception {
		
		WebElement parentElement = webDriver.waitForElement("//div[contains(@class,'community__IPSecItem_"+activity+"')]", ByTypes.xpath, " ---- "+activity.toString()+" element not found");
		String actualTitle = parentElement.findElement(By.className("community__indexpageSecItemTitle")).getText();
		String actualSummary = parentElement.findElement(By.className("community__indexpageSecItemSummary")).getText();	
		testResultService.assertEquals(true, getContinueLinkElementByActivity(activity).isDisplayed(), "------"+activity.toString()+" Continue link not displayed");
		
		switch (activity) {
				
		case forum:
			
			testResultService.assertEquals(forumTitle, actualTitle, "Forum title does not match or not found");
			testResultService.assertEquals(forumSummary, actualSummary, "Forum summary does not match or not found");
						
			break;
	
		case magazine:
						
			testResultService.assertEquals(magazineNewTitle, actualTitle, "Magazine title does not match or not found");
			testResultService.assertEquals(magazineSummary, actualSummary, "Magazine summary does not match or not found");
					
			break;

		case games:
			
			testResultService.assertEquals(gamesTitle, actualTitle, "Games title does not match or not found");
			testResultService.assertEquals(gamesContent, actualSummary, "Games summary does not match or not found");
						
			break;

		case idioms:
									
			testResultService.assertEquals(talkingIdiomsTitle, actualTitle, "TalkingIdioms title does not match or not found");
			testResultService.assertEquals(talkingIdiomsContent, actualSummary, "TalkingIdioms summary does not match or not found");
						
			break;
			
		default: testResultService.addFailTest("Community Activity undefined");
			
		}
				
	
	}
		
	public NewUxMagazinePage openMagazinePage() throws Exception {
		
		getContinueLinkElementByActivity(CommunityActivities.magazine).click();
		webDriver.sleep(2);
		
		return new NewUxMagazinePage(webDriver, testResultService);
				
	}
	
	public NewUxGamesPage openGamesPage() throws Exception {
		
		getContinueLinkElementByActivity(CommunityActivities.games).click();
		webDriver.sleep(2);
		
		return new NewUxGamesPage(webDriver, testResultService);
				
	}
	
	public NewUxTalkIdiomsPage openIdiomsPage() throws Exception {
		
		getContinueLinkElementByActivity(CommunityActivities.idioms).click();
		webDriver.sleep(2);
		
		return new NewUxTalkIdiomsPage(webDriver, testResultService);
				
	}
	
	public String openForumPage() throws Exception {
		
		getContinueLinkElementByActivity(CommunityActivities.forum).click();
		webDriver.sleep(1); // Increased sleep to ridiculous value for Backqa2008
		String mainWin = webDriver.switchToNewWindow();
		SwitchToCommunityIFrame();
		return mainWin;		
	}
	
	public void verifyCommunityActivityNotDisplayed(CommunityActivities activity) throws Exception {
		
		if (getContinueLinkElementByActivity(activity) != null){
			testResultService.addFailTest(activity.toString() + " displayed though it should not");
		}
		
				
	}
	
	private WebElement getContinueLinkElementByActivity(CommunityActivities activity) throws Exception {
				
		WebElement parentElement = webDriver.waitForElement("//div[contains(@class,'community__IPSecItem_"+activity.toString()+"')]", ByTypes.xpath, smallTimeOut,false, " ---- "+activity.toString()+" element not found");
		if (parentElement == null) return null;
		else return parentElement.findElement(By.linkText("Continue"));
				
	}
	
	
	
	public void close() throws Exception {
		webDriver.closeNewTab(2);
		webDriver.switchToMainWindow();
	}
	
	
	public void validateMagazineNameAndLevelAreCorrect(String magazineName, String level) throws Exception {
		
		testResultService.assertTrue("The Magazine Name wasn't displayed properly.",
				webDriver.waitForElement("h2", ByTypes.tagName).getText().equals(magazineName));
		
		ArrayList<WebElement> levels = (ArrayList<WebElement>) webDriver.getWebDriver().findElements(By.xpath("//*[contains(@id,\"magazine__navLevel\")]"));
		for (int i = 0; i < levels.size(); i++) {
			if (levels.get(i).getText().equals(level)) {
				testResultService.assertEquals(true, levels.get(i).getAttribute("className").contains("--selected"), "Check the wanted level is selected");
				break;
			}
		}
	}
}
