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


public class NewUxTalkIdiomsPage extends NewUxCommunityPage {
	
	@FindBy(id = "idioms__header")
	public WebElement idiomsHeader;
				
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
	
	@FindBy(className = "idioms__idiomDetails_Title")
	public WebElement idiomTextRight;
	
	@FindBy(className = "idioms__idiomExplText")
	public WebElement explanationText;
	
	@FindBy(className = "idioms__idiomExampleText")
	public WebElement exampleText;
	
	@FindBy(id = "hearLI")
	public WebElement hearBtn;
	
	@FindBy(id = "recordLI")
	public WebElement recordBtn;
					
	@FindAll(@FindBy(xpath = "//ul[@class='idioms__list']//a"))
	public List<WebElement> idioms;
	
	private static final String idioms_HeaderText = "Talking Idioms"; 
			
	public NewUxTalkIdiomsPage(GenericWebDriver webDriver, TestResultService testResultService) throws Exception {
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
	
	public void verifyIdiomsHeader() throws Exception {
		WebElement chkHeader = webDriver.waitForElement("idioms__header", ByTypes.id);
//		testResultService.assertEquals(idioms_HeaderText, idiomsHeader.getText(), "Idioms Page Title not valid");
		testResultService.assertEquals(idioms_HeaderText, chkHeader.getText(), "Idioms Page Title not valid");
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
			
	public void closeModalPopUp() throws Exception {
		
		webDriver.waitForElement("modal-close", ByTypes.className).click();
	}
	
	public void closeRecordPanel() throws Exception {
		
		webDriver.switchToMainWindow();
		closeModalPopUp();
				
	}
	
	public String getIdiomTextByIndex(int index) throws Exception {
		
		return idioms.get(index).getText();
				
	}
	
	public void verifyIdiomSelectedByIndex(int index) throws Exception {
		
		Boolean isSelected = idioms.get(index).getAttribute("class").contains("selected");
		testResultService.assertEquals(true, isSelected, "Idiom is not selected");
								
	}
	
	public String selectIdiomByIndex(int index) throws Exception {
		
		WebElement idiom = idioms.get(index);
		idiom.click();
		
		return idiom.getText();
								
	}
	
	public void verifySelectedIdiomItems(String expectedIdiom, String expectedExplanation, String expectedExample) throws Exception {
		
		testResultService.assertEquals(expectedIdiom, idiomTextRight.getText(), "Idiom text is not correct in right side");
		testResultService.assertEquals(expectedExplanation, explanationText.getText(), "Idiom explanation is not correct in right side");
		testResultService.assertEquals(expectedExample, exampleText.getText(), "Idiom example is not correct in right side");
								
	}
	
	
	
}
