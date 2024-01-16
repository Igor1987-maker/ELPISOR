package pageObjects.edo;

import Enums.ByTypes;
import drivers.GenericWebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import pageObjects.GenericPage;
import services.TestResultService;

import java.util.List;

public class TOEICTestPage extends GenericPage {

    @FindBy(className = "StartBT")
    public WebElement startTestButton;


    @FindBy(className = "h1")
    public WebElement topTitleInTestPopUp;

    @FindBy(css = ".nextBT a")
    public WebElement nextButton;


    public TOEICTestPage(GenericWebDriver webDriver, TestResultService testResultService) throws Exception {
        super(webDriver, testResultService);
        PageFactory.initElements(webDriver.getWebDriver(), testResultService);
    }

    @Override
    public GenericPage waitForPageToLoad() throws Exception {
        return null;
    }

    @Override
    public GenericPage OpenPage(String url) throws Exception {
        return null;
    }


    public void waitTillToeicTestDownloadedAncClickOnStart() {
        WebElement mainElement = null;
        try {
            for (int i = 0; i < 5 && mainElement == null; i++) {
                mainElement = webDriver.waitForElement("StartBT", ByTypes.className, false, 5);
            }
            if (mainElement != null) {
                while (mainElement != null) {
                    mainElement.click();
                    //Thread.sleep(200); //here you must chose the language (id =LanguagesUser)
                    mainElement = webDriver.waitForElement("StartBT", ByTypes.className, false, 2);
                    WebElement selectCountry = webDriver.waitForElement("Country", ByTypes.id, false, 1);
                    if (selectCountry != null) {
                        webDriver.selectElementFromComboBox("Country", "Albania");
                        Thread.sleep(200);
                        webDriver.selectElementFromComboBox("Languages", "Albanian");
                    }
                    ;
                }
            }

        } catch (Exception e) {
        }


    }


    public void verifyTestIsRunning() throws Exception {

        WebElement title = webDriver.waitForElement("h1", ByTypes.tagName, true, 7);
        if (title == null) {
            testResultService.addFailTest("Test won't start", true, true);
        }
        List<WebElement> answers = null;
        int i = 0;
        Thread.sleep(4000);
        do {
            try {
                answers = webDriver.getElementsByXpath("//div[@class='answer ']/input[@type='radio']");
            } catch (Exception | AssertionError e) {
            }
            Thread.sleep(2000);
        } while (answers.size() == 0 && i < 20);


        if (answers != null) {
            answers.get(0).click();
        } else {
            testResultService.addFailTest("Test not started", true, true);
        }

        //input[contains(@name,'r')]
        //webDriver.waitForElementByWebElement(topTitleInTestPopUp, "topTitleInTestPopUp", true, 5000);
    }

    public void fulfillTOEICTest() throws Exception {

    report.startStep("Verify whether title displayed");
        WebElement title = webDriver.waitForElement("h1", ByTypes.tagName, true, 7);
        if (title == null) {
            testResultService.addFailTest("Test won't start", true, true);
        }
        List<WebElement> answers = null;
        int i = 0;
        Thread.sleep(4000);

    report.startStep("reduce playback time of audio");
        webDriver.executeJsScript("setInterval(function(){v = document.getElementById('MediaPlayer');v.playbackRate = 12;})");


    report.startStep("Answer the first half of the questions without clicking Next");
        List<WebElement> blocksOfAnswers = webDriver.getElementsByXpath("//div[@class='answers']");
        do {
            if (blocksOfAnswers.size() == 1) {
                try {
                    answers = webDriver.getElementsByXpath("//div[@class='answer ']/input[@type='radio']");
                    answers.get(0).click();
                } catch (Exception | AssertionError e) {
                }
            } else {
                for (int j = 1; j <= blocksOfAnswers.size(); j++) {
                    try {
                        //WebElement subElement = blocksOfAnswers.get(j).findElement(By.xpath("//div[@class='answer ']/input[@type='radio']"));
                        WebElement subElement = webDriver.waitForElement("//div[@class='answers']["+j+"]/div[@class='answer '][1]/input[@type='radio']", ByTypes.xpath,false,2);
                        if (j>2){
                            webDriver.scrollToElement(subElement);
                        }

                        subElement.click();
                        Thread.sleep(500);
                    } catch (Exception | AssertionError e) {break;
                    }
                }
            }
            //webDriver.waitForElement("//div[@class='answers'][1]",ByTypes.xpath);
            try {
                Thread.sleep(5000);
                blocksOfAnswers = webDriver.getElementsByXpath("//div[@class='answers']");
            } catch (Exception | AssertionError e) {
            }
        } while (blocksOfAnswers != null);

    report.startStep("Walk trough instructions between sections");
        while (blocksOfAnswers == null) {
            webDriver.waitForElement(".nextBT a", ByTypes.cssSelector).click();
            Thread.sleep(500);
            try {
                blocksOfAnswers = webDriver.getElementsByXpath("//div[@class='answers']");
            } catch (Exception | AssertionError err) {
            }
        }

    report.startStep("Answer the second half of the questions by clicking Next");
        WebElement nextBtn = webDriver.waitForElement(".nextBT a", ByTypes.cssSelector);
        do {
            if (blocksOfAnswers.size() == 1) {
                try {
                    answers = webDriver.getElementsByXpath("//div[@class='answer ']/input[@type='radio']");
                    answers.get(0).click();
                } catch (Exception | AssertionError e) {
                }
            } else {
                for (int j = 0; j < blocksOfAnswers.size(); j++) {
                    WebElement subElement = blocksOfAnswers.get(j).findElement(By.xpath("//div[@class='answer ']/input[@type='radio']"));
                    if (j>2){
                        webDriver.scrollToElement(subElement);
                    }
                    subElement.click();
                    Thread.sleep(200);
                }
            }
            nextButton.click();
            Thread.sleep(500);
            //webDriver.waitForElement("//div[@class='answers'][1]",ByTypes.xpath);
            try {
                blocksOfAnswers = webDriver.getElementsByXpath("//div[@class='answers']");
            } catch (Exception | AssertionError e) {
                if (webDriver.waitForElement(".nextBT a", ByTypes.cssSelector)!=null){
                    nextButton.click();
                }
            }
        } while (blocksOfAnswers != null);


        //Next steps
        //1. click on continue
        //2. chose "yes" (selector -> //*[text()='Yes'])
        //3.click on continue
        //4.click on continue
        //5.switch to main or first window
        }

    public void completeTOEICTest() throws Exception {

        String JSToPassFirstSection = "setInterval(function(){$('input').click();v = document.getElementById('MediaPlayer');v.playbackRate = 8;TimeOutAfterItem = 1;TimeOutIntro=1;TimeOutFromPart3=1;},100)";
        String JSToPassSecondSection = "setInterval(function(){$('input').click();$('.nextBT').click();},500)";

        report.startStep("Execute first JS script and wait till first section of TOEIC test passed");
            webDriver.executeJsScript(JSToPassFirstSection);
            waitForButtonCertainTime(".nextBT a",ByTypes.cssSelector, 20);

        report.startStep("Execute second JS script and wait till second section of TOEIC test passed");
            webDriver.executeJsScript(JSToPassSecondSection);
            WebElement submitBTN = waitForButtonCertainTime(".StartBT a",ByTypes.cssSelector, 20);

        report.startStep("Submit the test and complete all agreements");
            submitBTN.click();
            WebElement continueBTN = webDriver.waitForElement(".StartBT", ByTypes.cssSelector);
            int s = 0;
            while(continueBTN!=null&&s<4) {
                try {
                    s++;
                    continueBTN.click();
                    if (s==1){
                        WebElement yesBtn = webDriver.waitForElement("//*[text()='Yes']", ByTypes.xpath,5,false);
                        yesBtn.click();
                    }
                    continueBTN = webDriver.waitForElement(".StartBT", ByTypes.cssSelector,2,false);
                }catch (Exception|AssertionError err){
                }
            }
    }



    public WebElement waitForButtonCertainTime(String selector, ByTypes selectorType, int iterations) throws InterruptedException {
        WebElement button = null;
        int i = 0;
        while(button==null&&i<iterations){
            try {
                button = webDriver.waitForElement(selector, selectorType,10,false);
            }catch (Exception|AssertionError err) {}
            Thread.sleep(2000);
            i++;
        }
        return button;
    }
}

