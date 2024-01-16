package pageObjects.tms;

import drivers.GenericWebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pageObjects.GenericPage;
import services.TestResultService;

import java.util.List;

public class AdministrationAndSessionPage extends GenericPage {

    @FindBy(tagName = "h2")
    public List<WebElement> administrations;

    @FindBy(xpath = "//app-administration-item[1]/div/section/h2")
    public WebElement firstAdministration;

    @FindBy(xpath = "//div/app-list-overview/main/div[2]/app-administration-item/div/section/div[2]/div[1]/div")
    public List<WebElement> administrationNames;

    @FindBy(xpath = "//img[contains(@class,'cursor-pointer group-hover')]")
    public WebElement openSession;

    @FindBy(css = "tbody tr td")
    public List<WebElement> studentData;

    @Override
    public GenericPage waitForPageToLoad() throws Exception {
        return null;
    }

    @Override
    public GenericPage OpenPage(String url) throws Exception {
        return null;
    }

    public AdministrationAndSessionPage(GenericWebDriver webDriver, TestResultService testResultService) throws Exception {
        super(webDriver, testResultService);
        PageFactory.initElements(webDriver.getWebDriver(), this);
    }


    public void waitTillAdministrationsBeVisible() throws Exception {
        //webDriver.waitForElementByWebElement(administrations.get(0), "FirstAdministration", true, 20);
       // webDriver.waitForElementByWebElement(firstAdministration, "FirstAdministration", true, 20);
        int i = 0;
        WebElement el = null;
        while(el==null&&i<5) {
            el = new WebDriverWait(webDriver.getWebDriver(), 20).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//app-administration-item[1]/div/section/h2")));
            if (el == null) {
                el = new WebDriverWait(webDriver.getWebDriver(), 20).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//app-administration-item[1]/div/section/h2")));
            }
            i++;
        }
    }

    public void clickOnWantedAdministration(String name) throws Exception {
        new WebDriverWait(webDriver.getWebDriver(), 20).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//app-administration-item[1]/div/section/div[2]/div[1]/div")));
        Thread.sleep(2000);
        //webDriver.waitForElementByWebElement(administrationNames.get(0), "AdministrationNames", true, 20);
        WebElement wantedAdministration = administrationNames.stream().filter(a->a.getText().equalsIgnoreCase(name)).findAny().orElse(null);
           if (wantedAdministration==null){
               wantedAdministration = administrationNames.stream().filter(a->a.getText().equalsIgnoreCase(name)).findAny().orElse(null);
           }
           if (wantedAdministration==null){
            testResultService.addFailTest("AdministrationMissed", true,true);
           }else {
                webDriver.scrollToElement(wantedAdministration, 0);
                Thread.sleep(1000);
                wantedAdministration.click();
           }
    }

    public void openFirstBottomSession() throws Exception {
        webDriver.waitForElementByWebElement(openSession,"openSession",true,5);
        openSession.click();
       //webDriver.waitForElementByWebElement(studentData.get(6),"StudentData",true,5);
    }

    public void getStudentDataAndVerifyScores() throws Exception {
        //webDriver.waitForElementByWebElement(studentData.get(7),"studentData",true,5);
        new WebDriverWait(webDriver.getWebDriver(), 10).until(ExpectedConditions.elementToBeClickable(By.cssSelector("h1:first-child")));
        String scores = studentData.get(7).getText();
        testResultService.assertEquals("31/100",scores,"Wrong scores");
    }


}
