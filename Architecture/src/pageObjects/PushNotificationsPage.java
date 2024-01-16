package pageObjects;

import Enums.ByTypes;
import drivers.GenericWebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import services.TestResultService;
import services.TextService;

import java.nio.charset.Charset;
import java.util.List;

public class PushNotificationsPage extends GenericPage{

    @FindBy(id = "title")
    public WebElement title;

    @FindBy(tagName = "app-button")
    public WebElement addNew;

    @FindBy(xpath = "//input[@placeholder='Title']")
    public WebElement titleField;

    @FindBy(xpath = "//input[@placeholder='Message']")
    public WebElement messageText;

    @FindBy(xpath = "//div[text()='Save']")
    public WebElement saveBTN;

    @FindBy(xpath = "//div[@class='flex-max relative']")
    public WebElement dropDownOfInstitutions;

    @FindBy(xpath = "//*[@id='push-notifications-classes']/span/div[2]/app-field/div[2]")
    public WebElement dropDownOfClasses;  //div[@class='flex-max relative']/app-field/div[contains(@class='p-2 pl-6 mt-2')]

    @FindBy(xpath = "//app-option/div/div[@class='select-none']")
    public List<WebElement> institutionsList;

    @FindBy(xpath = "//app-option/div/div[2]")
    public List<WebElement> classesList;

    @FindBy(xpath = "//p[@class='uppercase text-xl text-gray-600']")
    public WebElement addNotificationTitle;


    private static final String textMessageFile = "files/assayFiles/170_Characters.txt";

    public PushNotificationsPage(GenericWebDriver webDriver, TestResultService testResultService) throws Exception {
        super(webDriver, testResultService);
        PageFactory.initElements(webDriver.getWebDriver(), this);
    }

    @Override
    public GenericPage waitForPageToLoad() throws Exception {
        return null;
    }

    @Override
    public GenericPage OpenPage(String url) throws Exception {
        return null;
    }

    public void verifyTitle() throws Exception {
        webDriver.waitForElementByWebElement(title,"title",true,2000);
        testResultService.assertEquals("PUSH NOTIFICATIONS",title.getText().trim(),"Incorrect title");
    }

    public String createNewNotification(String institutionName) throws Exception {

        report.startStep("Verify title of 'Push notification' window");
            verifyTitle();

        report.startStep("Click on add notifications");
            addNew.click();
            Thread.sleep(2000);

        report.startStep("Chose institution from dropDown");
            dropDownOfInstitutions.click();
            WebElement wantedInstitution = institutionsList.stream().filter(e -> e.getText().trim().equalsIgnoreCase(institutionName)).findAny().orElse(null);
            Thread.sleep(2000);
            webDriver.scrollToElement(wantedInstitution, 0);
            Thread.sleep(700);
            wantedInstitution.click();
            //dropDownOfInstitutions.click();

        report.startStep("Chose class from dropDown");
            String notificationClass = configuration.getGlobalProperties("notificationClass");
            dropDownOfClasses.click();
            WebElement wantedClass = classesList.stream().filter(e -> e.getText().trim().equalsIgnoreCase(notificationClass)).findAny().orElse(null);
            webDriver.scrollToElement(wantedClass, 0);
            Thread.sleep(700);
            wantedClass.click();
            try {
                report.startStep("Close drop-down with JS"); //Talk with Sophia about it
                    WebElement we = webDriver.waitForElement("apply-select-view",ByTypes.id);
                    webDriver.clickOnElementByJavaScript(we);
                //addNotificationTitle.click();
                //titleField.click();
            }catch (Exception|AssertionError err){
            }

        report.startStep("Populate title and message fields");
            String notificationName = "PushNote"+dbService.sig(4);
            titleField.click();
            titleField.clear();
            titleField.sendKeys(notificationName);

        report.startStep("Populate message with exceeded amount of characters and get alert");
            textService = new TextService();
            String notificationMessage = textService.getTextFromFile(textMessageFile, Charset.defaultCharset());
            messageText.click();
            messageText.clear();
            messageText.sendKeys(notificationMessage+"1");

        report.startStep("Verify alert text appears");
           WebElement alertOfExceededText = webDriver.waitForElement("//div[@class='text-red-600']", ByTypes.xpath,2, false, null);
           if (alertOfExceededText!=null) {
               testResultService.assertTrue("Alert text incorrect", alertOfExceededText.getText().contains("Notification title should not exceed 240"));
           }

        report.startStep("Populate message with proper amount of characters");
            messageText.click();
            messageText.clear();
            messageText.sendKeys(notificationMessage);
            Thread.sleep(2000);

        report.startStep("Click on save");
            saveBTN.click();
            Thread.sleep(1500);

        return notificationName;
    }

    public void pushNotification() throws Exception {
        WebElement sendButton = webDriver.waitForElement("//tr[1]/td[@class='flex justify-between item mb-5']/img[1]",ByTypes.xpath);
        sendButton.click();
        Thread.sleep(1000);

        WebElement pushNotification = webDriver.waitForElement("//div[contains(text(),'Push notification')]",ByTypes.xpath);
        pushNotification.click();

        Thread.sleep(1000);
        WebElement status = webDriver.waitForElement("//tr[1]/td[3]",ByTypes.xpath);
        testResultService.assertEquals("Published", status.getText().trim(),"Wrong status after publishing",true);
    }

    public void deleteNotification() throws Exception {
        WebElement trashIcon = webDriver.waitForElement("//tr[1]/td[@class='flex justify-between item mb-5']/img[3]",ByTypes.xpath);
        trashIcon.click();
        //Thread.sleep(1000);
        WebElement deleteNotificationBTN = webDriver.waitForElement("//div[contains(text(),'Delete notification')]",ByTypes.xpath);
        deleteNotificationBTN.click();
        Thread.sleep(500);
    }

    public void editNotification(String notificationName) throws Exception {

        report.startStep("Click on pencilIcon");
            WebElement pencilIcon = webDriver.waitForElement("//tr[1]/td[@class='flex justify-between item mb-5']/img[2]",ByTypes.xpath);
            pencilIcon.click();

        report.startStep("Change notification title");
            webDriver.waitForElementByWebElement(titleField,"titleField",true,1500);
            titleField.click();
            titleField.clear();
            titleField.sendKeys(notificationName);

        report.startStep("Click on save");
            saveBTN.click();
            Thread.sleep(1500);
    }



}
