package pageObjects.edo;

import Enums.ByTypes;
import drivers.GenericWebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import pageObjects.GenericPage;
import services.TestResultService;

import java.util.List;

public class NewUxVocabularyPage extends GenericPage {

    @FindBy(className = "learning__vocabulary--card")
    public List<WebElement> cards;

    @FindBy(css = "h3")
    public WebElement vocabularyTitle;

    @FindBy(tagName = "ed-check-icon")
    public List<WebElement> checkIcons;

    @FindBy(tagName = "ed-bookmark-icon")
    public List<WebElement> bookMarkIcons;

    @FindBy(tagName = "ed-arrow-right-icon")
    public WebElement arrowNext;

    @FindBy(tagName = "ed-close-icon")
    public WebElement closeIcon;

    @FindBy(css = ".learning__vocabulary--card-modal-container ed-bookmark-icon")
    public WebElement bookMarkOfOpenCard;


    @FindBy(css = ".learning__vocabulary--popup-container h2")
    public WebElement popUpTaskCompleted;

    @FindBy(css = ".learning__vocabulary--popup-container h3")
    public WebElement exploredWords;

    @FindBy(css = ".learning__vocabulary--popup-container h4")
    public WebElement popUpQuestion;

    @FindBy(css = "button[class='learning__vocabulary--popup-btn-secondary']")
    public WebElement returnToWordListBTN;

    @FindBy(css = "button[class='learning__vocabulary--popup-btn-primary learning__vocabulary--ml-6']")
    public WebElement continueToPracticeActivitiesBTN;

    public NewUxVocabularyPage(GenericWebDriver webDriver, TestResultService testResultService) throws Exception {
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
        webDriver.waitForElementByWebElement(vocabularyTitle, "vocabularyTitle", true, 5000);
        testResultService.assertEquals("Look at the word list. Listen to the recordings and practice saying the words. Mark any words you would like to review later.",
                vocabularyTitle.getText(), "Wrong title on vocabulary page");
    }

    public void clickOnCardsAndVerifyWhetherTheyMarked() throws Exception {

        try {
            cards.get(0).click();
            Thread.sleep(500);
            bookMarkOfOpenCard.click();
            //bookMarkIcons.get(0).click(); // here is the problem
            arrowNext.click();
            Thread.sleep(300);
            bookMarkOfOpenCard.click();
            //bookMarkIcons.get(1).click();
            closeIcon.click();
            Thread.sleep(300);

            for (int i = 0; i < bookMarkIcons.size(); i++) {
                String marked = bookMarkIcons.get(i).getAttribute("ng-reflect-is-bookmarked");
                String checked = checkIcons.get(i).getAttribute("class");
                if (i < 2) {
                    testResultService.assertEquals("true", marked, "Incorrect state of marked icon");
                    testResultService.assertEquals("learning__vocabulary--flex-end", checked, "Visited card not marked");
                } else {
                    //testResultService.assertEquals("false", marked, "Incorrect state of marked icon");// is null
                    testResultService.assertIsNull(marked, "Incorrect state of marked icon");
                    testResultService.assertTrue("Unvisited card marked as visited", checked.contains("hidden"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            testResultService.addFailTest(e.getMessage(), true, true);
        } catch (AssertionError e) {
            e.printStackTrace();
            testResultService.addFailTest(e.getMessage(), true, true);
        }
    }

    public void clickOnCardByOrderNumber(int cardNumber) {
        cards.get(cardNumber - 1).click();
    }

    public void walkThroughAllCards() {

        WebElement el = null;
        try {
            while (el == null) {
                arrowNext.click();
                el = webDriver.waitForElement(".learning__vocabulary--popup-container h2", ByTypes.cssSelector, false, 2);
            }
        } catch (Exception e) {

        }

    }

    public void verifyMessagesOfCompletedTask() {
        String s = popUpTaskCompleted.getText();
        testResultService.assertEquals("Well Done!", popUpTaskCompleted.getText(), "");
        s = exploredWords.getText();
        testResultService.assertEquals("You have explored 10 out of 10 new words.", exploredWords.getText(), "");
        s = popUpQuestion.getText();
        testResultService.assertEquals("Do you want to return to the word list or continue to the practice activities?", popUpQuestion.getText(), "");
    }

    public void clickOnReturnButton() {
        returnToWordListBTN.click();
    }


    public void verifyAllCardsAreChecked() throws Exception {
        try {
            checkIcons.forEach(e -> testResultService.assertEquals("learning__vocabulary--flex-end", e.getAttribute("class"), "Visited card not marked"));
        }catch (Exception e){
            testResultService.addFailTest(e.getMessage(),true,true);
        }catch (AssertionError e){
            testResultService.addFailTest(e.getMessage(),true,true);
        }
            //  e.getAttribute("class").equalsIgnoreCase("learning__vocabulary--flex-end"));
            //String checked = checkIcons.get(0).getAttribute("class");
            //testResultService.assertEquals("learning__vocabulary--flex-end", checked, "Visited card not marked");
        }
    }
