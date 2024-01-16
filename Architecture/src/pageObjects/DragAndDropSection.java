package pageObjects;

import org.openqa.selenium.WebElement;

import Enums.ByTypes;
import services.TestResultService;
import drivers.GenericWebDriver;

public class DragAndDropSection extends EdoHomePage {

	public DragAndDropSection(GenericWebDriver webDriver,
			TestResultService testResultService) throws Exception {
		super(webDriver, testResultService);
		// TODO Auto-generated constructor stub
	}

	public void dragClassificationAnswerByTextFromExistingAnswer(String text,
			int rowSource, int colSource, int rowDest, int colDest)
			throws Exception {
		WebElement from = webDriver.waitForElement(
				"//div[@class='emptyBank']//div[text()='" + text + "']",
				ByTypes.xpath);

		// TODO - change source element to
		// //div[@class='emptyBank']//div[text()='" + text + "']"
		WebElement to = webDriver.waitForElement(
				"//table[@class='textTable']//tr[@id='" + rowDest + "']//td["
						+ colDest + "]", ByTypes.xpath);

		webDriver.dragAndDropElement(from, to);

	}

	public void checkDragElementIsPlaced(String text) throws Exception {
		webDriver.waitForElement(
				"//div[contains(@class,'wordBankTilePlaced')][text()='" + text
						+ "']", ByTypes.xpath);

	}

	public void dragClassificationAnswerToBank(String text) throws Exception {
		WebElement from = webDriver.waitForElement(
				"//div[contains(@class,'draggable')][text()='" + text + "']",
				ByTypes.xpath);

		WebElement to = webDriver.waitForElement(
				"//div[@class='emptyBank']//div[text()='" + text + "']",
				ByTypes.xpath);
		webDriver.dragAndDropElement(from, to);

	}

	public void dragAnserToElementByXpath(String text, String xpath,
			String placeHolderContent) throws Exception {
		xpath = xpath.format(xpath, placeHolderContent);
		WebElement from = webDriver.waitForElement(
				"//div[@id='TTpTablePlaceHolder']//div//div[text()='" + text
						+ "']", ByTypes.xpath);
		WebElement to = webDriver.waitForElement(xpath, ByTypes.xpath);

		webDriver.dragAndDropElement(from, to);

	}

	public void checkDragElementLocationCloze(String answerIndex,
			String answerId) throws Exception {
		webDriver.waitForElement("//div[@class='TextDiv']//span[" + answerIndex
				+ "][@ans='" + answerId + "']", ByTypes.xpath);

	}

	public void dragClassificationAnswerToBankCloze(String text)
			throws Exception {
		WebElement from = webDriver.waitForElement(
				"//div[@id='TTpTablePlaceHolder']//div//div[text()[contains(.,'" + text + "')]]", ByTypes.xpath);
		
		WebElement to = webDriver.waitForElement(
				"//div[@id='TTpTablePlaceHolder']//div[1]", ByTypes.xpath);
		// TODO Auto-generated method stub
		webDriver.dragAndDropElement(from, to);

	}

	public void checkDragElementLocationPicture(String index, String answerId)
			throws Exception {
		webDriver.waitForElement("//div[@id='ListPlaceHolder']//div[" + index
				+ "][@ans='" + answerId + "']", ByTypes.xpath);

	}

	public void dragSeqSentence(String text, int index) throws Exception {
		WebElement from = webDriver.waitForElement("//div[text()='" + text
				+ "']", ByTypes.xpath);
		WebElement to = webDriver.waitForElement(
				"//table[@class='textTable']//tr[" + index + "]//div[2]",
				ByTypes.xpath);

		webDriver.dragAndDropElement(from, to);

	}

	public void checkSeqSentenceCorrectAnswer(String text, int index)
			throws Exception {
		webDriver.waitForElement("//table[@class='textTable']//tbody//tr["
				+ index + "]//td//div[2]//div//div[text()='" + text
				+ "'][contains(@class,'vCheck')]", ByTypes.xpath);

	}

	public void checkSeqSentenceInCorrectAnswer(String text, int index)
			throws Exception {
		webDriver.waitForElement("//table[@class='textTable']//tbody//tr["
				+ index + "]//td//div[2]//div//div[text()='" + text
				+ "'][contains(@class,'xCheck')]", ByTypes.xpath);

	}

	public void dragImageToPlace(String imageId, int row, int column)
			throws Exception {

		WebElement from = webDriver.waitForElement(
				"//div[@class='draggable bank'][@id='" + imageId + "']",
				ByTypes.xpath);

		WebElement to = webDriver.waitForElement(
				"//div[@class='allPicturesWrapper sixItem']//div[" + row
						+ "]//div[" + column + "]//div[2]//div", ByTypes.xpath);

		webDriver.dragAndDropElement(from, to);
	}

	public void dragClassificationAnswerByTextFromBank(String answer, int row,
			int column) throws Exception {
		webDriver.dragAndDropElement(webDriver.waitForElement(
				"//div[@class='emptyBank']//div[text()='" + answer + "']",
				ByTypes.xpath), webDriver.waitForElement(
				"//table[@class='textTable']//tr[@id='" + row + "']//td["
						+ column + "]", ByTypes.xpath));

	}

	public void checkDragAndDropCorrectAnswer(String answer) throws Exception {
		webDriver.waitForElement(
				"//div[@class='emptyBank']//div[contains(@class,'vCheck')][text()='"
						+ answer + "']", ByTypes.xpath);

	}

	public void checkDragAndDropCorrectAnswerCloze(String answer)
			throws Exception {
		webDriver
				.waitForElement(
						"//div[@id='TTpTablePlaceHolder']//div//div[contains(@class,'vCheck')][text()='"
								+ answer + "']", ByTypes.xpath);

	}
	
	public void checkDragAndDropWrongAnswerCloze(String answer)
			throws Exception {
		webDriver
				.waitForElement(
						"//div[@id='TTpTablePlaceHolder']//div//div[contains(@class,'xCheck')][text()='"
								+ answer + "']", ByTypes.xpath);

	}
	

	public void checkDragElementLocation(String text, String answerId, int row,
			int col) throws Exception {
		webDriver.waitForElement("//table[@class='textTable']//tr[@id='" + row
				+ "']//td[" + col + "][@ans=" + answerId + "]", ByTypes.xpath);

	}

	public void checkDragElementIsBackToBank(String text) throws Exception {
		webDriver.waitForElement("//div[@class='emptyBank']//div[text()='"
				+ text + "']", ByTypes.xpath);

	}

	public void checkDragElementIsBackToBankCloze(String text) throws Exception {
		// wordBankTile
		webDriver
				.waitForElement(
						"//div[contains(@class,'wordBankTile')][text()='"
								+ text + "']", ByTypes.xpath);

	}

	public void checkDraggedImageCorrectAnswer(String id, int row, int col)
			throws Exception {
		webDriver
				.waitForElement(
						"// div[@class='allPicturesWrapper sixItem']//div["
								+ row + "]//div[" + col
								+ "]//div[1][contains(@class,'vCheck')]",
						ByTypes.xpath);

	}

	public void checkDragElementLocationInTable(String ansId, String tr,
			String td) throws Exception {
		webDriver.waitForElement(
				"//table[@class='textTable']//tbody//tr[@id='" + tr
						+ "']//td[" + td + "][@ans='" + ansId + "']",
				ByTypes.xpath);

	}
}
