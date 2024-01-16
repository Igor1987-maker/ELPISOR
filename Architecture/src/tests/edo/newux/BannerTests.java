package tests.edo.newux;

import java.io.IOException;

import org.apache.http.client.utils.URIBuilder;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.server.handler.FindElement;
import org.openqa.selenium.support.FindBy;

import Enums.ByTypes;
import Interfaces.TestCaseParams;
import jcifs.smb.SmbFile;
import testCategories.AngularLearningArea;
import testCategories.inProgressTests;
//import pageObjects.edo.NewUXLoginPage;
import testCategories.reg1;
import testCategories.reg2;
import tests.edo.newux.BasicNewUxTest;
import tests.misc.EdusoftWebTest;
import pageObjects.edo.NewUXLoginPage;
import pageObjects.edo.NewUxHomePage;
import pageObjects.edo.NewUxLearningArea2;
import services.PageHelperService;

@Category(reg2.class)
public class BannerTests extends BasicNewUxTest {

	NewUXLoginPage loginPage;
	String banner;
	NewUxLearningArea2 learningArea2;
	
	private static final String OLD_DEFAULT_BANNER_GIF = "old_default_banner.gif";
	private static final String OLD_CUSTOM_BANNER_GIF = "old_custom_banner.gif";
	private static String CUSTOML_BANNER_PATH ="";
	private static String resPhysicalPath = "";
	
	@Before
	public void setup() throws Exception {
		super.setup();
		// pageHelper.getLatestCILinkUX();

	}
/*
	@Test
	@TestCaseParams(testCaseID = { "21240" })
	public void testBannerOldCustom() throws Exception {

		testBanner(OLD_CUSTOM_BANNER_GIF);

	}
*/

/*
	@Test
	@TestCaseParams(testCaseID = { "21243" })
	public void testBannerOldEdusoftDefault() throws Exception {

		testBanner(OLD_DEFAULT_BANNER_GIF);

	}
*/
	
/*
	@Test
	@TestCaseParams(testCaseID = { "21242" })
	public void testNewEdusoftDefaultBanner() throws Exception {
		
		report.startStep("Open login page");
		loginPage = new NewUXLoginPage(webDriver, testResultService);
		sleep(3);
		loginPage.verifyDefaultBannerExist();
		webDriver.refresh();
		sleep(3);
		loginPage.verifyDefaultBannerExist();
	}
 */
	
	@Test
	@TestCaseParams(testCaseID = { "21242","21240" })
	public void testNewEdusoftDefaultAndCustomBanner() throws Exception {
		
		CUSTOML_BANNER_PATH = textService.getWebConfigAppSettingsValuByKey(PageHelperService.buildPath, "ResRootURL");
		resPhysicalPath = textService.getWebConfigAppSettingsValuByKey(PageHelperService.buildPath, "resPhysicalPath");

		
		try{
			loginPage = new NewUXLoginPage(webDriver, testResultService);
		/*
			copyBannertoCustomInstitutionFolderNoSmb();
			sleep(2);
			
			boolean fileStatus = textService.deleteFileIPath(resPhysicalPath + "Institutions\\" + institutionId + "\\institutionHeader.jpg");
			if (fileStatus){
				report.startStep("Custom file was exists and t deleted");
				webDriver.refresh();
				homePage.waitToLoginArea();
			}
			
			report.startStep("Open login page");
			loginPage = new NewUXLoginPage(webDriver, testResultService);
			//sleep(1);
			loginPage.verifyDefaultBannerExist(NewUXLoginPage.GENERAL_BANNER_PATH);
		*/
			report.startStep("Copy Custom Banner");
			copyBannertoCustomInstitutionFolderNoSmb();
			sleep(2);
			webDriver.refresh();
			sleep(2);
			//homePage.waitToLoginArea();
			homePage.waitToBannerLoaded();
			loginPage.verifyDefaultBannerExist(CUSTOML_BANNER_PATH);
			
		}
		catch (Exception e) {
			e.fillInStackTrace();
			}
		
		finally {
			boolean fileStatus = textService.deleteFileIPath(resPhysicalPath + "Institutions\\" + institutionId + "\\institutionHeader.jpg");
			sleep(3);
			report.startStep("Custom file deleted: " + fileStatus);
			
			report.startStep("Default Banner verifications");
				//webDriver.refresh();
				pageHelper.restartBrowserInNewURL(institutionName, false);
				Thread.sleep(2000);
				webDriver.refresh();
				Thread.sleep(3000);
				homePage.waitToBannerLoaded();
				loginPage.verifyDefaultBannerExist(NewUXLoginPage.GENERAL_BANNER_PATH);
		}
		
	}
	

	private void testBanner(String fileName) throws Exception, IOException {
		System.out.println("Build id:" + PageHelperService.buildId);

		report.startStep("Check if institution folder exist in CI");
		textService.getCreateFolderInPath(PageHelperService.buildPath + "//Institutions", institutionId);
		
		textService.copyFileToFolder(
				"smb://" + configuration.getGlobalProperties("logserverName")
						+ "//AutoLogs//ToolsAndResources//testFiles//" + fileName, PageHelperService.buildPath + "//Institutions//"
						+ institutionId + "//banner.gif",
				true, netService.getDomainAuth(), netService.getDomainAuth());
		
		//copyBannertoCustomInstitutionFolder();
	

		//SmbFile smbFile = new SmbFile("smb://" + configuration.getLogerver() + "/"+SCREENSHOT_FOLDER+"/" + sFileName, netService.getDomainAuth());
		
		report.startStep("Open the login page");
		//webDriver.deleteCookiesAndRefresh();
		//String url = webDriver.getUrl();
		//webDriver.closeBrowser();
		webDriver.openIncognitoChromeWindow();
		pageHelper.restartBrowser();
		sleep(2);
		
		pageHelper.openCILatestUXLink();
		banner = getBannerLinkByFileName(fileName);
		assertBanner(fileName);
		report.startStep("refresh login page");

		webDriver.refresh();
		banner = getBannerLinkByFileName(fileName);
		assertBanner(fileName);

		report.startStep("Login and logout");
		getUserAndLoginNewUXClass();
		//homePage.waitHomePageloaded();
		homePage.clickOnLogOut();
		sleep(5);
		//webDriver.switchToMainWindow();
		webDriver.switchToTopMostFrame();
		banner = getBannerLinkByFileName(fileName);
		assertBanner(fileName);
	}

	private void copyBannertoCustomInstitutionFolder() {
		
		try {
			report.startStep("Copy the custom banner");	
			//	String source = "smb://" + configuration.getGlobalProperties("logserver") + "//testFiles//institutionHeader.jpg";
				//rootPath = textService.getWebConfigAppSettingsValuByKey(pageHelper.buildPath, "resPhysicalPath") + "Institutions//" + institutionId+"";
				//resPathRoot = textService.getWebConfigAppSettingsValuByKey(pageHelper.buildPath, "resPathRoot");
				
				String target_rootPath = "smb://"+PageHelperService.physicalPath+"//Institutions//"+ institutionId +"";
				//String a = pageHelper.buildPath + "//Institutions//"+ institutionId + "//banner.gif";
				
				
				textService.copyFileToFolder(
						"smb://" + configuration.getGlobalProperties("logserverName")+ "//AutoLogs//ToolsAndResources//testFiles//institutionHeader.jpg"
						,target_rootPath,true, netService.getDomainAuth(), netService.getDomainAuth()
						);
				
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
private void copyBannertoCustomInstitutionFolderNoSmb() {
		
		try {
			report.startStep("Copy the custom banner");	
			//	String source = "smb://" + configuration.getGlobalProperties("logserver") + "//testFiles//institutionHeader.jpg";
				//rootPath = textService.getWebConfigAppSettingsValuByKey(pageHelper.buildPath, "resPhysicalPath") + "Institutions//" + institutionId+"";
				//resPathRoot = textService.getWebConfigAppSettingsValuByKey(pageHelper.buildPath, "resPathRoot");
				
				String target_rootPath = resPhysicalPath +"Institutions\\"+ institutionId +"";
				//String a = pageHelper.buildPath + "//Institutions//"+ institutionId + "//banner.gif";
				
				
				textService.copyFileToFolder(
						"\\\\" + configuration.getGlobalProperties("logserverName")+ "\\AutoLogs\\ToolsAndResources\\testFiles\\institutionHeader.jpg"
						,target_rootPath,false, netService.getDomainAuth(), netService.getDomainAuth()
						);
		
				//target_rootPath = target_rootPath + "\\institutionHeader.jpg"; //pageHelper.buildPath+"institutionHeader.jpg";
				
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

	private void assertBanner(String filename) {
		if (filename.equals(OLD_CUSTOM_BANNER_GIF)) {
			testResultService.assertTrue("Custom banner not found", banner.contains("institutions/"+configuration.getInstitutionId()+"/banner.gif"));
			
		} else if (filename.equals(OLD_DEFAULT_BANNER_GIF)) {
			testResultService.assertEquals("siteLogin__header login__defaultHeaderNew", banner, "Banner class for old default banner is incorrect");
			
		} else testResultService.addFailTest("Custom banner behavior is invalid");
				
		
	}

	private String getBannerLinkByFileName(String fileName) throws Exception {
		if (fileName.equals(OLD_CUSTOM_BANNER_GIF)) {
			return webDriver.waitForElement("//a[@id='siteLogin__OldHeaderLink']//img", ByTypes.xpath).getAttribute("src");
			
		} else if (fileName.equals(OLD_DEFAULT_BANNER_GIF)) {
			return webDriver.waitForElement("//header[contains(@class,'siteLogin__header')]", ByTypes.xpath).getAttribute("class"); // //header[@id='siteLogin__header']
		}
		return null;
	}

	
	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}
}
