package pageObjects.edo;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import Enums.ByTypes;
import drivers.GenericWebDriver;
import pageObjects.DragAndDropSection;
import services.TestResultService;

public class NewUxDragAndDropSection extends DragAndDropSection {

	
	private List<WebElement> sequenceDraggableItems;
	
	
	
	public NewUxDragAndDropSection(GenericWebDriver webDriver, TestResultService testResultService) throws Exception {
		super(webDriver, testResultService);
		// TODO Auto-generated constructor stub
	}

	public void dragAndDropCloseAnswerByTextToId(String text, String targetId) throws Exception {
		// WebElement answerPlaceHolderElement =
		// webDriver.waitForElement("//div[@class='TextDiv'][@id='1_1']//span",ByTypes.xpath);
		// WebElement answer =
		// webDriver.waitForElement("//div[@class='wordsBankWrapper']//div//div//div[@data-id='"
		// + id + "']", ByTypes.xpath);
		// WebElement answer =
		// webDriver.waitForElement("//div[@class='wordsBankWrapper']//div//div//div[text()='"
		// + text + "']", ByTypes.xpath);

		WebElement answer = webDriver.waitForElement(
				"//div[text()[contains(.,'" + text + "')]][contains(@class,'wordBankTile')]", ByTypes.xpath,1,false); // //div[@class='wordsBankWrapper']//div//div//div[text()[contains(.,'" + text + "')]]

		// [text()[contains(.,'Some text')]]

		WebElement answerPlaceHolderElement = webDriver
				.waitForElement("//span[contains(@class,'droptarget')][@data-id='" + targetId + "']", ByTypes.xpath);

		webDriver.dragAndDropElement(answer, answerPlaceHolderElement);

	}
	
	public void dragAndDropCloseFirstAnswer() throws Exception {
		
		WebElement answer = webDriver.waitForElement(
				"//div[@class='wordsBankWrapper']//div//div//div", ByTypes.xpath);

		WebElement answerPlaceHolderElement = webDriver
				.waitForElement("//span[contains(@class,'droptarget')][@data-id='1_1']", ByTypes.xpath);

		webDriver.dragAndDropElement(answer, answerPlaceHolderElement);

	}
	
	public void dragAndDropMatchTextToPicAnswerByTextToId(String text, int targetId) throws Exception {
		
		WebElement answer = webDriver.waitForElement(
				"//div[@class='wordsBankWrapper']//div//div//div[text()[contains(.,'" + text + "')]]", ByTypes.xpath);

		WebElement answerPlaceHolderElement = webDriver
				.waitForElement("//div[contains(@class,'droptarget')][@data-id='" + targetId + "']", ByTypes.xpath);

		webDriver.dragAndDropElement(answer, answerPlaceHolderElement);

	}
	
	public void dragAndDropMatchTextToPicFirstAnswer() throws Exception {
		
		WebElement answer = webDriver.waitForElement(
				"//div[@class='wordsBankWrapper']//div//div//div", ByTypes.xpath, false, 5);

		WebElement answerPlaceHolderElement = webDriver
				.waitForElement("//div[contains(@class,'droptarget')][@data-id='2']", ByTypes.xpath, false, 5);

		webDriver.dragAndDropElement(answer, answerPlaceHolderElement);

	}
	
	public void hoverOnMatchTextToPicAnswerByTextToId(String text) throws Exception {
		
		WebElement answer = webDriver.waitForElement(
				"//div[@class='wordsBankWrapper']//div//div//div[text()[contains(.,'" + text + "')]]", ByTypes.xpath);

		
		webDriver.hoverOnElement(answer);

	}

	public void checkCloseTileIsBackToBank(String text) throws Exception {
		webDriver.scrollToElement(webDriver.waitForElement( 
				"//div[text()[contains(.,'" + text + "')]][contains(@class,'wordBankTile')]", ByTypes.xpath)); // //div[contains(@class,'wordBankTile')][text()[contains(.,'" + text + "')]]

	}

	public void checkTileIsPlaced(String text) throws Exception {
		webDriver.waitForElement("//div[contains(@class,'wordBankTilePlaced')][text()[contains(.,'" + text + "')]]",
				ByTypes.xpath);
	}
	public void checkTileIsPlacedSequenceSentence(String text) throws Exception {
		webDriver.waitForElement("//div[contains(@class,'draggable bank')][text()[contains(.,'" + text + "')]]",
				ByTypes.xpath);
	}
	
	
	public void checkTileIsPlacedFillInTheBlank(String text) throws Exception {
		webDriver.waitForElement("//div[contains(@class,'questionText')][text()[contains(.,'" + text + "')]]",
				ByTypes.xpath);
	}
	
	public void checkTileIsPlacedInTestYourAnswerAndCorrect(String text) throws Exception {
		webDriver.waitForElement("//div[contains(@class,'wordBankTilePlaced vCheck')][text()[contains(.,'" + text + "')]]",
				ByTypes.xpath);
	}
	
	public void checkTileIsPlacedInTestYourAnswerAndCorrectFillInTheBlank(String text) throws Exception {
		webDriver.waitForElement("//div[contains(@class,'fitb')][text()[contains(.,'" + text + "')]]",
				ByTypes.xpath);
	}
	
	
	public void checkTileIsPlacedInTestYourAnswerAndWrong(String text) throws Exception {
		webDriver.waitForElement("//div[contains(@class,'wordBankTilePlaced xCheck')][text()[contains(.,'" + text + "')]]",
				ByTypes.xpath);
	}
	

	public void dragAndDropClassificationAnswerByTextToColumn(String text, int row, int column) throws Exception {
		webDriver.dragAndDropElement(
				webDriver.waitForElement("//div[@class='wordsBankTable']//div[text()[contains(.,'" + text + "')]]",
						ByTypes.xpath),
				webDriver.waitForElement("//table[@class='textTable']//tr[@id='" + row + "']//td[" + column + "]",
						ByTypes.xpath));

	}
	
	public void dragAndDropClassificationFirstAnswer() throws Exception {
		webDriver.dragAndDropElement(
				webDriver.waitForElement("//div[@class='wordsBankTable']//div",
						ByTypes.xpath, false, 5),
				webDriver.waitForElement("//table[@class='textTable']//tr[@id='1']//td[2]",
						ByTypes.xpath, false, 5));
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
		
	public void dragAndDropSequenceAnswerByTextToPlaceInOrder(String text, int placeInOrder) throws Exception {
		
		WebElement dragItem = sequenceDraggableItems.stream().filter(el->el.getText().trim().contains(text)).findAny().orElse(null);
		//Thread.sleep(1000);
		webDriver.dragAndDropElement(dragItem,
				//webDriver.waitForElement("//div[@class='draggable bank' and text()[contains(.,'"+text+"')]]",
				//		ByTypes.xpath),
				//webDriver.waitForElement("//table[@class='textTable']//td[@id='" + placeInOrder + "']",
				//		ByTypes.xpath));
		
		webDriver.waitForElement("prSeq__zone--txt_"+placeInOrder,//("//*[@id= '"+placeInOrder+"' ]/div[2]",
				ByTypes.id));
		sequenceDraggableItems = webDriver.getElementsByXpath("//div[@class='dnditem draggable']");
		//Thread.sleep(1000);
	}
	
	public void dragAndDropSequenceAnswerByImageNameToPlaceInOrderNewSeq(String imageName, String placeInOrder) throws Exception {
		webDriver.dragAndDropElement(
				webDriver.waitForElement("//div[@class='dnditemImage' and contains(@style,'"+imageName+"')]",
						ByTypes.xpath),
				webDriver.waitForElement("//div[contains(@class,'prSeq__mainIW prSeq__mainIW--img')]//div[@id='" + placeInOrder + "']",
						ByTypes.xpath));

	}
	
	public void dragAndDropSequenceAnswerByImageNameToPlaceInOrderNewSeqText(String imageName, String placeInOrder) throws Exception {

		WebElement answer = webDriver.waitForElement("//*[@class='dnditem draggable' and @ans_id='"+imageName+"']",
				ByTypes.xpath);
		
		WebElement placeHolder = webDriver.waitForElement("//*[@id='" + placeInOrder + "']",
				ByTypes.xpath);
		
		webDriver.dragAndDropElement(answer,placeHolder);
				
		/*webDriver.dragAndDropElement(
				webDriver.waitForElement("//*[@class='dnditem draggable' and @ans_id='"+imageName+"']",
						ByTypes.xpath),
				webDriver.waitForElement("//*[@id='" + placeInOrder + "']",
						ByTypes.xpath));
		*/

	}
	
	  public void dragAndDropSequenceAnswerByImageNameToPlaceInOrder(String imageName, int placeInOrder) throws Exception {
          webDriver.dragAndDropElement(
                                          webDriver.waitForElement("//div[@class='draggable bank' and contains(@style,'"+imageName+"')]",
                                                                          ByTypes.xpath),
                                          webDriver.waitForElement("//div[contains(@class,'allPicturesWrapper')]//div[@seqnum='" + placeInOrder + "']",
                                                                          ByTypes.xpath));

}

	public void checkDragAndDropCorrectAnswer(String text) throws Exception {
		webDriver.waitForElement("//div[@id='TTpTablePlaceHolder']//div//div[contains(@class,'wordBankTilePlaced vCheck')][text()[contains(.,'" + text + "')]]", ByTypes.xpath);
		//webDriver.waitForElement("//div[@id='TTpTablePlaceHolder']//div//div[contains(@class,'wordBankTilePlaced vCheck')][text()='" + text + "']", ByTypes.xpath);
	}
	
	public void checkDragAndDropCorrectAnswerForSequenceSentence(String text) throws Exception {
		
		webDriver.waitForElement("//div[contains(@class,'draggable bank vCheck')][text()[contains(.,'" + text + "')]]", ByTypes.xpath,webDriver.getTimeout(), true,"Answer is not marked as correct");
		
	}
	
	public void checkDragAndDropAnswerForNewSeq(String imageName, boolean isAnswerCorrect) throws Exception {
		if (isAnswerCorrect) webDriver.waitForElement("//div[@class='dnditem dnditem--v']/div [ contains(@style,'"+imageName+"')]", ByTypes.xpath,webDriver.getTimeout(), true,"Answer is not marked as correct");
		else webDriver.waitForElement("//div[@class='dnditem dnditem--x']/div [ contains(@style,'"+imageName+"')]", ByTypes.xpath,webDriver.getTimeout(), true,"Answer is not marked as wrong");
	}
	
	public void checkDragAndDropAnswerForNewSeqText(String imageName, boolean isAnswerCorrect) throws Exception {
		if (isAnswerCorrect) 
			webDriver.waitForElement("//*[@class='dnditem dnditem--v'][@ans_id='"+imageName+"']", ByTypes.xpath,webDriver.getTimeout(), true,"Answer is not marked as correct");
		else 
			webDriver.waitForElement("//div[@class='dnditem dnditem--x'][@ans_id='"+imageName+"']", ByTypes.xpath,webDriver.getTimeout(), true,"Answer is not marked as wrong");
	}

	 public void checkDragAndDropAnswerForSequenceImage(String imageName, boolean isAnswerCorrect) throws Exception {
         if (isAnswerCorrect) webDriver.waitForElement("//div[@class='draggable bank' and @style='"+imageName+"']/../../../div[contains(@class,'vCheck')]", ByTypes.xpath,webDriver.getTimeout(), true,"Answer is not marked as correct");
         else webDriver.waitForElement("//div[@class='draggable bank' and @style='"+imageName+"']/../../../div[contains(@class,'xCheck')]", ByTypes.xpath,webDriver.getTimeout(), true,"Answer is not marked as wrong");
}
	
	public void checkDragAndDropWrongAnswer(String text) throws Exception {
		webDriver.waitForElement(
				"//div[@id='TTpTablePlaceHolder']//div//div[contains(@class,'xCheck')][text()[contains(.,'" + text
						+ "')]]",
				ByTypes.xpath);
	}
	
	public void checkDragAndDropWrongAnswerForSequenceSentence(String text) throws Exception {
		
		webDriver.waitForElement("//div[contains(@class,'draggable bank xCheck')][text()[contains(.,'" + text + "')]]", ByTypes.xpath,webDriver.getTimeout(), true,"Answer is not marked as wrong");
		
	}

	public void dragAndDropReadingSequenceSentenceNewSeq() throws Exception {
		
		Random random = new Random();
		int min = 0;
		int max = 2;
		int rnd = random.nextInt((max - min) + 1) + min;
		//WebElement webElementTo;
		//WebElement webElementFrom;
		WebElement webElementFrom = sequenceDraggableItems.get(rnd);
		WebElement webElementTo = sequenceDraggableItems.get(rnd);
			
			while (webElementFrom.equals(webElementTo)) {
				
				rnd = random.nextInt((max - min) + 1) + min;
				webElementTo = sequenceDraggableItems.get(rnd);
			}
			
			webDriver.dragAndDropElement(webElementFrom, webElementTo);
			rnd = random.nextInt((max - min) + 1) + min;
		}

	
	public void retrieveDraggableSegments() throws Exception {
		
		//sequenceDraggableItems = webDriver.waitForElement("textTable", ByTypes.className, true, 15).findElements(By.xpath("//div[contains(@class, 'draggable')]"));
		sequenceDraggableItems = webDriver.getElementsByXpath("//div[@class='dnditem draggable']");
		//dnditem draggable
	}
	
	 public void retrieveDraggableImages() throws Exception {
         
         sequenceDraggableItems = webDriver.waitForElement("//div[contains(@class,'allPicturesWrapper')]", ByTypes.xpath, true, 15).findElements(By.xpath("//div[contains(@class, 'draggable')]"));
}
	public void retrieveDraggableNewSeq() throws Exception {
		
		sequenceDraggableItems = webDriver.waitForElement("//div[contains(@class,'prSeq__mainIW prSeq__mainIW--img')]", ByTypes.xpath, true, 15).findElements(By.xpath("//div[contains(@class, 'draggable')]"));
	}
	
public void retrieveDraggableNewSeqText() throws Exception {
		
		sequenceDraggableItems = webDriver.waitForElement("//div[contains(@class,'prSeq__mainIW prSeq__mainIW--txt')]", ByTypes.xpath, true, 15).findElements(By.xpath("//div[contains(@class, 'draggable')]"));
	}
	
	public void verifySixDraggableSegmentsInOrder() throws Exception {
        
        testResultService.assertEquals(true, sequenceDraggableItems.size() == 6, "The amount of the dragable items in the practice was incorrect", true);
}
	
	public void verifyNumOfDraggableSegmentsInOrder(int num) throws Exception {
        
        testResultService.assertEquals(true, sequenceDraggableItems.size() == num, "The amount of the dragable items in the practice was incorrect", true);
}

	public void verifySixDraggableSegmentsInOrderNewSeq() throws Exception {
		
		testResultService.assertEquals(true, sequenceDraggableItems.size() == 6, "The amount of the dragable items in the practice was incorrect", true);
	}

	
	   public void dragAndDropReadingSequenceSentence() throws Exception {
           
           Random random = new Random();
           int min = 0;
           int max = 5;
           int rnd = random.nextInt((max - min) + 1) + min;
           WebElement webElementTo;
           
           for (WebElement webElementFrom : sequenceDraggableItems) {
                           webElementTo = sequenceDraggableItems.get(rnd);
                           
                           while (webElementFrom.equals(webElementTo)) {
                                           rnd = random.nextInt((max - min) + 1) + min;
                                           webElementTo = sequenceDraggableItems.get(rnd);
                           }
                           
                           webDriver.dragAndDropElement(webElementFrom, webElementTo);
                           rnd = random.nextInt((max - min) + 1) + min;
           }
}
	public void verifyDragAndDropsSentenceRandomPlacementAfterRefresh() throws Exception {
					
		List<WebElement> refreshedDragAndDropAnswers = webDriver.waitForElement("textTable", ByTypes.className, true, 15).findElements(By.xpath("//div[contains(@class, 'draggable')]"));
		testResultService.assertEquals(false, sequenceDraggableItems.equals(refreshedDragAndDropAnswers), "Refresh did not randomized the Drag & Drop answers list order", true);
		sequenceDraggableItems = refreshedDragAndDropAnswers; 
	}
	
	 public void verifyDragAndDropsImagesRandomPlacementAfterRefresh() throws Exception {
         
         List<WebElement> refreshedDragAndDropAnswers = webDriver.waitForElement("//div[contains(@class,'allPicturesWrapper')]", ByTypes.xpath, true, 15).findElements(By.xpath("//div[contains(@class, 'draggable')]"));
         testResultService.assertEquals(false, sequenceDraggableItems.equals(refreshedDragAndDropAnswers), "Refresh did not randomized the Drag & Drop answers list order", true);
         sequenceDraggableItems = refreshedDragAndDropAnswers; 
}
	public void verifyDragAndDropsImagesRandomPlacementAfterRefreshNewSeq() throws Exception {
		
		List<WebElement> refreshedDragAndDropAnswers = webDriver.waitForElement("//div[contains(@class,'prSeq__mainIW prSeq__mainIW--img')]", ByTypes.xpath, true, 15).findElements(By.xpath("//div[contains(@class, 'draggable')]"));
		testResultService.assertEquals(false, sequenceDraggableItems.equals(refreshedDragAndDropAnswers), "Refresh did not randomized the Drag & Drop answers list order", true);
		sequenceDraggableItems = refreshedDragAndDropAnswers; 
	}
public void verifyDragAndDropsImagesRandomPlacementAfterRefreshNewSeqText() throws Exception {
		
		List<WebElement> refreshedDragAndDropAnswers = webDriver.waitForElement("//div[contains(@class,'prSeq__mainIW prSeq__mainIW--txt')]", ByTypes.xpath, true, 15).findElements(By.xpath("//div[contains(@class, 'draggable')]"));
		testResultService.assertEquals(false, sequenceDraggableItems.equals(refreshedDragAndDropAnswers), "Refresh did not randomized the Drag & Drop answers list order", true);
		sequenceDraggableItems = refreshedDragAndDropAnswers; 
	}
	
	public String getFirstTileText() throws Exception {
		String text = webDriver.waitForElement("//div[contains(@class,'wordBankTile')]", ByTypes.xpath).getText();
		return text;
	}
	
	
	
	
	
}
