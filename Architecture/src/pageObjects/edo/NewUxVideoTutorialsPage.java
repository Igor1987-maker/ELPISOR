package pageObjects.edo;


import java.util.List;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import Enums.ByTypes;
import drivers.GenericWebDriver;
import pageObjects.GenericPage;
import services.TestResultService;


public class NewUxVideoTutorialsPage extends GenericPage {

    public NewUxVideoTutorialsPage(GenericWebDriver webDriver, TestResultService testResultService) throws Exception {
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

    public String VIDEO_NAME_TIME_MANAGEMENT = "Time Management";
    public String VIDEO_NAME_ASSESSMENT = "Assessment";
    public String VIDEO_WRITING = "Writing";


    public String verifyVideoTutorialsPageHeader() throws Exception {

        return webDriver.getWebDriver().findElement(By.cssSelector("#videoTutorials__header")).getText();
    }


    public WebElement checkAndGetVideoElements(String videName) throws Exception {

        WebElement element = webDriver.waitForElement("videoTutorials__list", ByTypes.className);

        List<WebElement> childElements = element.findElements(By.tagName("li"));

        if (!(childElements.size() >= 1)) {
            testResultService.addFailTest("No Video list selection");
        }

        int videoCount = childElements.size();
        Random rand = new Random();
        int value = rand.nextInt(videoCount);

        report.startStep("Verify First Video's Name");
        testResultService.assertEquals(true, childElements.get(0).getText().contains(videName), "First Video's Name is Incorrect.");
        return childElements.get(value);
    }


    public void playVideo() throws Exception {
        //webDriver.waitForElement("iconButton play",ByTypes.className).click();
        webDriver.waitForElement("/html/body/div/div[2]/div[1]/section/div/div[2]/div[2]/div/div/div/videogular/vg-overlay-play/div/div", ByTypes.xpath).click();


    }

    public void pauseVideo() throws Exception {
        //webDriver.waitForElement("iconButton pause",ByTypes.className).click();
        webDriver.waitForElement("/html/body/div/div[2]/div[1]/section/div/div[2]/div[2]/div/div/div/videogular/vg-overlay-play/div/div", ByTypes.xpath).click();

        // /html/body/div/div[2]/div[1]/section/div/div[2]/div[2]/div/div/div/videogular/vg-overlay-play/div/div
    }

    public WebElement checkAssessmentVideoExists() throws Exception {
        WebElement element = webDriver.waitForElement("videoTutorials__list", ByTypes.className);
        List<WebElement> childElements = element.findElements(By.className("videoTutorials__listItemDetailsText"));
        if (!(childElements.size() >= 1)) {
            testResultService.addFailTest("No Video list selection");
        }
        int videoCount = childElements.size();
        Random rand = new Random();
        int value = rand.nextInt(videoCount);
        report.startStep("Verify Assessments Video displays");
        boolean Found = false;

        for (int i = 0; i < childElements.size(); i++) {
            if (childElements.get(i).getText().contains(VIDEO_WRITING))
                Found = true;
        }
        testResultService.assertEquals(true, Found, "Assessments Video Exists.");
        testResultService.assertEquals(true, childElements.get(1).getText().contains(VIDEO_WRITING), "First Video's Name is Incorrect.");
        return childElements.get(value);
    }


}