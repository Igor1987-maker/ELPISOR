package pageObjects;

import Enums.AdministratorTitle;
import drivers.GenericWebDriver;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pageObjects.edo.NewUXLoginPage;
import pageObjects.edo.NewUxMyProfile;
import pageObjects.tms.AdministrationAndSessionPage;
import pageObjects.tms.TmsAssessmentsTestsAssignmentPage;
import pageObjects.tms.TmsHomePage;
import services.PageHelperService;
import services.TestResultService;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ToeicStudentsAdministrationPage extends GenericPage {


    TmsHomePage tmsHomePage = new TmsHomePage(webDriver, testResultService);
    public PageHelperService pageHelper;
    NewUXLoginPage loginPage;

    NewUxMyProfile myProfile;
    TmsAssessmentsTestsAssignmentPage tests;
    ToeicStudentsAdministrationPage toeicStudentsPage;
    AdministrationAndSessionPage administrationAndSessionPage;


    @FindBy(css = "[test-id='edusoft']")
    private WebElement edusoftLogo;

    @FindBy(css = "#title")
    private WebElement pageName;

    @FindBy(css = "[test-id='edexcellence']")
    private WebElement edxLogo;

    @FindBy(css = "[test-id='page-status-steps']")
    private WebElement stepsPanel;

    @FindBy(css = "[test-id='page-status-steps'] .mx-auto:nth-child(1)")
    private WebElement firstStepDiv;

    @FindBy(xpath = "//div[contains(text(),'Upload CSV file')]")//(css = "[testid='upload-csv']")
    private WebElement uploadCsvButton;

    @FindBy(css = "input[type]")
    private WebElement hiddenUploadInput;

    @FindBy(css = "[testid='download-csv']")
    private WebElement downloadCsvButton;

    @FindBy(css = "[test-id='csv-buttons-descr']")
    private WebElement csvButtonsDescriptionLabel;

    @FindBy(css = "[test-id='back-upload-page']")
    private WebElement backButton;

    @FindBy(css = "[test-id='next']")
    private WebElement nextButton;

    @FindBy(css = "[test-id='progress-bar']")
    private WebElement progressBarModal;

    @FindBy(css = "#loader-holder div.h-full")
    private WebElement progressBar;

    @FindBy(css = "#loader-holder img")
    private WebElement pencilImage;

    public ToeicStudentsAdministrationPage(GenericWebDriver driver, TestResultService testResultService) throws Exception {
        super(driver, testResultService);
        this.testResultService = testResultService;
        PageFactory.initElements(driver.getWebDriver(), this);
    }

    public void checkPageName() {
        Assert.assertEquals(pageName.getText(), "Toeic Test Student Registration");
    }

    public void checkEdusoftLogo() {
        Assert.assertTrue(edusoftLogo.isDisplayed());
    }

    public void checkEDXLogo() {
        Assert.assertTrue(edxLogo.isDisplayed());
    }

    public void checkFirstStepAppears() {
        String expectedCircleColor = "rgba(0, 128, 0, 1)"; // Replace with the expected green color in rgba format
        Assert.assertEquals(expectedCircleColor, firstStepDiv.findElement(By.cssSelector(".circles")).getCssValue("background-color"));
    }


    public ToeicUploadResultPage uploadCSV(File csvFile) throws Exception {
    	try {
           // Assert.assertTrue(uploadCsvButton.isDisplayed());
            WebElement fileInput = webDriver.getWebDriver().findElement(By.id("file-upload-input"));
            //WebElement fileInput = webDriver.getWebDriver().findElement(By.xpath("//div[contains(text(),'Upload CSV file')]"));
            //fileInput.click();
//            WebElement fileInput = webDriver.getWebDriver().findElement(By.xpath("//div[contains(text(),'Upload CSV file')]"));
//            JavascriptExecutor executor = (JavascriptExecutor) webDriver.getWebDriver();
//            executor.executeScript("arguments[0].click();", fileInput);


            /*JavascriptExecutor jsExecutor = (JavascriptExecutor) webDriver.getWebDriver();
            jsExecutor.executeScript("arguments[0].type='file';arguments[0].style.visibility='visible'; " +
              "arguments[0].style.height='100px'; arguments[0].style.width='100px'; arguments[0].style.opacity=1", fileInput);
            jsExecutor.executeScript("arguments[0].type='file';arguments[0].class=''",fileInput);
            jsExecutor.executeScript("arguments[0].setAttribute('class','')",fileInput);
            webDriver.executeJsScript("document.querySelector('input[type]').style.display='block'");
            jsExecutor.executeScript("arguments[0].style.display='block'",fileInput);
            uploadCsvButton.click();
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_ESCAPE);
            robot.keyRelease(KeyEvent.VK_ESCAPE);*/
            fileInput.sendKeys(csvFile.getAbsolutePath());


//            JavascriptExecutor fileExecutor = (JavascriptExecutor) webDriver.getWebDriver();
//            fileExecutor.executeScript("arguments[0].value = arguments[1];", fileInput, csvFile.getAbsolutePath());
        }catch (AssertionError e){
            e.printStackTrace();
        }catch (Error e){
            e.printStackTrace();
        }
        return new ToeicUploadResultPage(webDriver, testResultService, this);
    }

//    public ToeicUploadResultPage uploadCSV(File csvFile) throws Exception {
//        By uploadButtonLocator = By.xpath("//div[contains(text(),'Upload CSV file')]");
//
//        try {
//
//            WebElement fileInput = webDriver.getWebDriver().findElement(uploadButtonLocator);
//
//
//            fileInput.click();
//
//
//            JavascriptExecutor executor = (JavascriptExecutor) webDriver.getWebDriver();
//            executor.executeScript("arguments[0].setAttribute('value', arguments[1]);", fileInput, csvFile.getAbsolutePath());
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return new ToeicUploadResultPage(webDriver, testResultService, this);
//    }

    public File downloadCSV(String downloadPath, String filename) throws InterruptedException {
        downloadCsvButton.click();

        // Wait for the file to be downloaded
        TimeUnit.SECONDS.sleep(5);

        // Check if the file has been downloaded
        Path path = Paths.get(downloadPath, filename);
        boolean isFileDownloaded = Files.exists(path);

        if (isFileDownloaded) {
            System.out.println("File downloaded successfully: " + filename);
        } else {
            System.out.println("File not found: " + filename);
        }

     // Return the downloaded file as a File object
        Path filePath = Paths.get(downloadPath, filename);
        return filePath.toFile();

    }

    public void goNext() {
        nextButton.click();
    }

    public void verifyBack() {
        Assert.assertFalse(backButton.isEnabled());
    }

    public void verifyURL() {
        //TODO move to pagehelper
        String expectedURL = "https://edexcellence.edusoftrd.com/registration";
        WebDriverWait wait = new WebDriverWait(webDriver.getWebDriver(), 10);
        wait.until(ExpectedConditions.urlToBe(expectedURL));
    }

    public void checkProgressBarExists() {
        Assert.assertTrue(progressBar.isDisplayed());
    }
    public void checkProgressBarAnimation() {
        WebDriverWait wait = new WebDriverWait(webDriver.getWebDriver(), 10);

        // Check if the width of the progress bar increases
        wait.until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                String style = progressBar.getAttribute("style");
                Pattern widthPattern = Pattern.compile("width:\\s*([\\d.]+)%;");
                Matcher widthMatcher = widthPattern.matcher(style);
                if (widthMatcher.find()) {
                    double width1 = Double.parseDouble(widthMatcher.group(1));
                    try {
                        Thread.sleep(1000); // Wait for 1 second
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    style = progressBar.getAttribute("style");
                    widthMatcher = widthPattern.matcher(style);
                    if (widthMatcher.find()) {
                        double width2 = Double.parseDouble(widthMatcher.group(1));
                        return width2 > width1;
                    }
                }
                return false;
            }
        });

        // Check if the position of the pencil image increases
        wait.until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                String style = pencilImage.getAttribute("style");
                Pattern leftPattern = Pattern.compile("left:\\s*([\\d.]+)%;");
                Matcher leftMatcher = leftPattern.matcher(style);
                if (leftMatcher.find()) {
                    double left1 = Double.parseDouble(leftMatcher.group(1));
                    try {
                        Thread.sleep(1000); // Wait for 1 second
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    style = pencilImage.getAttribute("style");
                    leftMatcher = leftPattern.matcher(style);
                    if (leftMatcher.find()) {
                        double left2 = Double.parseDouble(leftMatcher.group(1));
                        return left2 > left1;
                    }
                }
                return false;
            }
        });
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

    List<String[]> list = new ArrayList<>();
    public List<String[]> prepareListForCSVfile(String userName, String email, String date, AdministratorTitle administratorTitle) {
        switch (administratorTitle) {
            case CREATE_ADMINISTRATIONS_AND_SESSIONS:

                list.add(new String[]{"Username", "Email Example: xx@xx.xxx", "Proctor Name", "Start Date . Example: dd/mm/yyyy", "Start Time. Example: xx:xxpm"});
                list.add(new String[]{userName, email, "Sally3", date, "01:00pm"});
                break;

            case CREATE_ADMINISTRATION:

                list.add(new String[]{"Username", "Email Example: xx@xx.xxx", "Start Date . Example: dd/mm/yyyy", "Start Time. Example: xx:xxpm"});
                list.add(new String[]{userName, email,  date, "01:00pm"});
        }
        return list;
    }

    public String getTitle(){
      //New design
        //return webDriver.getWebDriver().findElements(By.cssSelector(".leading-5.font-roboto")).get(2).getText();

        // Old design
        return webDriver.getWebDriver().findElements(By.cssSelector(".leading-5.font-roboto")).get(2).getText();

    }
    public AdministratorTitle getAdministrationTitle(String value ){

        for(AdministratorTitle v : AdministratorTitle.values()){
            if( v.value.equals(value)){
                return v;
            }
        }
        return null;
    }


   }

