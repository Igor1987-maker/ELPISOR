package pageObjects;

import Enums.ByTypes;
import drivers.GenericWebDriver;
import org.joda.time.LocalDate;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import services.TestResultService;

import java.util.List;

public class ToeicAdministrationPage extends GenericPage{

    @FindBy(css = ".flex.justify-between.gap-10.pr-10 p")
    private WebElement addTestType;

    @FindBy(css = ".p-2.pl-6")
    private WebElement dropDownOfTypes;

    @FindBy(css = "app-option div div[class='select-none']")
    private List<WebElement> testTypes;

    @FindBy(xpath = "//div[text()='Add']")
    private WebElement addButton;

    @FindBy(id = "1-class")
    private WebElement dropDownOfClasses1;

    @FindBy(id = "7-class")
    private WebElement dropDownOfClasses2;

    @FindBy(id = "8-class")
    private WebElement dropDownOfClasses3;

    @FindBy(xpath = "//app-option//div[@class='select-none']")
    private List<WebElement> classes;

    @FindBy(xpath = "//p[text()='Select:']")
    private WebElement select;

    @FindBy(xpath = "//app-calendar-field[@id='8-start-date']")
    private WebElement startDateDropdown;

    @FindBy(xpath = "//app-select[@id='8-start-time']")
    private WebElement startTimeDropdown;

    @FindBy(xpath = "//app-select[@id='8-end-time']")
    private WebElement endTimeDropdown;

    @FindBy(xpath = "//app-calendar-field[@id='8-end-date']")
    private WebElement endDateDropdown;

    @FindBy(css=".items-center.border")//(xpath = "//div[contains(@class,'items-center border-2 border-green')]")//div[@id='calendar-container']//div[contains(@class,'w-full h-full hover:bg-green-200 rounded-full flex justify-center items-center border')]")//
    private WebElement currentDayInCalendar;

    @FindBy(xpath = "//app-select[@id='8-start-time']//app-option[1]")
    private WebElement startTime;

    @FindBy(xpath = "//app-select[@id='8-end-time']//app-option[1]")
    private WebElement endTime;

    @FindBy(xpath = "//div[text()=' Assign students']")
    private WebElement assignStudents;

    @FindBy(css = "p")
    private WebElement successMessage;

    @FindBy(className = "bg-primary-500")
    private WebElement doneButton;

    @FindBy(xpath = "//div[@class='tickShadow border-primary-500 border-1 border-solid absolute w-6 h-6 bg-gray-100 rounded-full transition-all left-3 bg-white']")
    private WebElement unlimitedAssignment;









    public ToeicAdministrationPage(GenericWebDriver webDriver, TestResultService testResultService) throws Exception {
        super(webDriver, testResultService);
        this.testResultService = testResultService;
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

    public void clickOnAddTestType() throws Exception {
        webDriver.waitForElementByWebElement(addTestType, "addTestType", true,5000);
        addTestType.click();
    }

    public void choseTestTypeAndClickADD(String wantedTest) throws Exception {
        webDriver.waitForElementByWebElement(dropDownOfTypes,"dropDownOfTypes",true,5000);
        dropDownOfTypes.click();
        Thread.sleep(1000);
        WebElement testType = testTypes.stream().filter(e->e.getText().trim().equalsIgnoreCase(wantedTest)).findAny().orElse(null);

        if (testType!=null){
            testType.click();
        }else {
            testResultService.addFailTest("No such kind of test",true, true);
        }
        addButton.click();
    }


    public void selectClass(String className, String testType) throws Exception {
        WebElement wantedClass = null;
        switch (testType){
            case "ED Placement":
                dropDownOfClasses1.click();
                wantedClass = classes.stream().filter(e->e.getText().trim().equalsIgnoreCase(className)).findAny().orElse(null);
                webDriver.scrollToElement(wantedClass, 0);
                wantedClass.click();
                dropDownOfClasses1.click();
                break;
            case "Toeic Placement":
                dropDownOfClasses2.click();
                wantedClass = classes.stream().filter(e -> e.getText().trim().equalsIgnoreCase(className)).findAny().orElse(null);
                webDriver.scrollToElement(wantedClass, 0);
                wantedClass.click();
                dropDownOfClasses2.click();
                break;
            case "Toeic Bridge Placement":
                dropDownOfClasses3.click();
                wantedClass = classes.stream().filter(e->e.getText().trim().equalsIgnoreCase(className)).findAny().orElse(null);
                webDriver.scrollToElement(wantedClass, 0);
                wantedClass.click();
                dropDownOfClasses3.click();
                break;
        }
       /* dropDownOfClasses.click();
        WebElement wantedClass = classes.stream().filter(e->e.getText().trim().equalsIgnoreCase(className)).findAny().orElse(null);
        webDriver.scrollToElement(wantedClass, 0);
        wantedClass.click();
        dropDownOfClasses.click();*/
    }

    public void choseStartEndDate() throws Exception {
        startDateDropdown.click();
        Thread.sleep(1000);
        //WebElement currDate = webDriver.waitForElement(".items-center.border", ByTypes.cssSelector);
       // currDate.click();
        currentDayInCalendar.click();

        startTimeDropdown.click();
        Thread.sleep(1000);
        startTime.click();

        endDateDropdown.click();
        LocalDate date = LocalDate.now();
        int today = date.getDayOfMonth();
        int day = date.plusDays(1).getDayOfMonth();
        String selectorOfEndDate = "";
        if(today>day){
            selectorOfEndDate = "//div[@class='w-full h-full hover:bg-green-200 rounded-full flex justify-center items-center text-gray-400']/span[text()='"+day+"']";
        }else{
            selectorOfEndDate = "//div[@class='w-full h-full hover:bg-green-200 rounded-full flex justify-center items-center']/span[text()='"+day+"']";
        }
        List<WebElement> days = webDriver.getElementsByXpath(selectorOfEndDate);
        days.get(0).click();
        /*if (days.size()>1){
            days.get(1).click();
        }else {
            days.get(0).click();
        }*/

        endTimeDropdown.click();
        endTime.click();



    }

    public void clickOnAssignStudents() {
        assignStudents.click();
    }

    public void waitSuccessMessage() throws Exception {
        WebElement assignStudentButton = webDriver.waitForElement("//div[text()=' Assign students']", ByTypes.xpath,3,false);
        for (int i =0;i<50&&assignStudentButton!=null;i++){
            assignStudentButton = webDriver.waitForElement("//div[text()=' Assign students']", ByTypes.xpath,3,false);
        }
        webDriver.waitForElementByWebElement(successMessage,"successMessage", true, 5000);
        testResultService.assertTrue("Students weren't assigned", successMessage.getText().trim().equalsIgnoreCase("The students were successfully assigned"));
   }

    public void clickOnDoneButton(){
        doneButton.click();
    }

    public void switchUnlimitedAssignment() throws Exception {
        webDriver.waitForElementByWebElement(unlimitedAssignment, "unlimitedAssignment",true,3000);
        unlimitedAssignment.click();
    }
}
