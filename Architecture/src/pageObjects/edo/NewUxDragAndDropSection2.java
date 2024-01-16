package pageObjects.edo;


import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;

import Enums.ByTypes;
import Enums.TaskTypes;
import drivers.GenericWebDriver;
import services.TestResultService;

public class NewUxDragAndDropSection2 extends NewUxLearningArea2 {
	
	@FindBy(id = "bankContainer")
	public WebElement bank;
	
	@FindAll(@FindBy(className = "regContainer"))
	public List <WebElement> targetColumns;
	
	@FindAll(@FindBy(className = "prCLZ__regContainer"))
	public List <WebElement> closeAnswerTargets;
	
	@FindAll(@FindBy(className = "prMT_T2T__regContainer"))
	public List <WebElement> matchAnswerTargets;
	
	@FindAll(@FindBy(className = "dndZone"))
	public List <WebElement> MTTPAnswerTargets;
	
	@FindAll(@FindBy(className = "prTextToPic_text"))
	public List <WebElement> MTTPtile;
	
	@FindAll(@FindBy(className = "dnditem"))
	public List <WebElement> tiles;
	
	@FindBy(css = ".draggable.wordBankTile")
	public List<WebElement> draggable; //
	
	@FindBy(css = ".TTpanswerDiv.droptarget")
	public List<WebElement> droppable;
	
	
	
		
	public NewUxDragAndDropSection2(GenericWebDriver webDriver, TestResultService testResultService) throws Exception {
		super(webDriver, testResultService);
		PageFactory.initElements(new AjaxElementLocatorFactory(webDriver.getWebDriver(), 10), this);
	}

	public void checkTileIsInBank(String text) throws Exception {
	
		bank.findElement(By.xpath("//div[text()[contains(.,'" + text + "')]]"));
	
	}
	
	public void checkTilesIsInBankMTTP (String text) throws Exception {
		
		bank.findElement(By.xpath(".//div/ed-la-dndzone/div/ed-la-dnditem[1]/div/div[2]/span"));
	}

	public void checkTileIsPlaced(String text, TaskTypes type) throws Exception {
		
		/*// these lines works and can be replaced the following block
		WebElement elementText= null;
		elementText = webDriver.waitForElement("prCLZ__question", ByTypes.className,false,1);
		elementText.findElement(By.xpath(".//div[text()[contains(.,'" + text + "')]]"));
		*/
		
		WebElement element= null;
		List <WebElement> targets = getTargetsListByTaskType(type);
						
		for (int i = 0; i < targets.size(); i++) {
			try {
				element = targets.get(i).findElement(By.xpath(".//div[text()[contains(.,'" + text + "')]]"));
				
				if (element!=null)
					break;
			} catch (Exception e) {
				//e.printStackTrace();
				//report.addTitle("Word no found in target:" + i+1);
			}
		}
		
		testResultService.assertEquals(true, element!=null, "Tile is not found in any drop target");
		
	}
	
	
	public void checkTileIsPlaced(String[] text, TaskTypes type) throws Exception {
		
		WebElement element= null;
		List <WebElement> targets = getTargetsListByTaskType(type);
						
		for (int i = 0; i < targets.size(); i++) {
			try {
				element = targets.get(i).findElement(By.xpath(".//div[text()[contains(.,'" + text[i] + "')]]"));
				if (element==null)
					testResultService.addFailTest("Word not found", false, true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		testResultService.assertEquals(true, element!=null, "Tile is not found in any drop target");
		
	}
	
	public void checkTileIsPlaced(String text, TaskTypes type, int column) throws Exception {
		
		WebElement element= null;
		List <WebElement> targets = getTargetsListByTaskType(type);
						
		//for (int i = 0; i < targets.size(); i++) {
			try {
				element = targets.get(column).findElement(By.xpath(".//div[text()[contains(.,'" + text + "')]]"));
				//if (element!=null)
					//break;
			} catch (Exception e) {
				e.printStackTrace();
			}
		//}
		
		testResultService.assertEquals(true, element!=null, "Tile is not found in any drop target");
		
	}

	public void checkTileIsPlacedMTTP(String text, TaskTypes type) throws Exception {
		
		WebElement element= null;
		List <WebElement> targets = getTargetsListByTaskType(type);
						
		for (int i = 0; i < targets.size(); i++) {
			try {
				//element = targets.get(i).findElement(By.xpath(".//div//ed-la-practicearea/div[2]/ed-la-text-to-picture/div[1]/div[1]/ed-la-dndzone/div/ed-la-dnditem/div/div[2]/span"));
				element = targets.get(i).findElement(By.className("prTextToPic_text"));
				if (element!=null) break;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		testResultService.assertEquals(true, element!=null, "Tile is not found in any drop target");
		
	}
		
	public void dragAndDropAnswerByTextToTarget(String text, int targetNumber, TaskTypes type) throws Exception {
		
		List <WebElement> targets = getTargetsListByTaskType(type);		
		WebElement tileToDrag = bank.findElement(By.xpath(".//div[text()[contains(.,'" + text + "')]]"));
		WebElement target = targets.get(targetNumber-1);
		
		webDriver.dragAndDropElement(tileToDrag, target);
	}
	
	public void scrollCustomElement(TaskTypes type, int y) throws Exception{
		WebElement scroll = null;
		
		switch(type){
		
		case  Close: case Classification:	scroll = webDriver.waitForElement("//*[@id='rightDiv']/ed-la-practicearea/div[2]/ed-la-dndcloze/div/div/div[1]/div/div[2]/div/div", ByTypes.xpath,false, smallTimeOut);
			break;
		case MTTP:
			break;
		case Matching:
			break;
		
		case MCQ:
		case OpenEnded:
		case FillInTheBlank:
			scroll = webDriver.waitForElement("//*[@id='rightDiv']/ed-la-practicearea/div[2]/ng-component/div/div/div[2]/div/div", ByTypes.xpath,false, smallTimeOut);
			break;
		
		default:
			break;
		}
		
			//WebElement scroll = webDriver.waitForElement("//*[@id='rightDiv']/ed-la-practicearea/div[2]/ed-la-dndcloze/div/div/div[1]/div/div[2]/div/div", ByTypes.xpath);
			
			if (scroll != null){
				webDriver.dragScrollElement(scroll, y);
				webDriver.sleep(1);
			}	
	}
	
	public void dragAndDropAnswerByIndexToTarget(int targetNumber, TaskTypes type, int index) throws Exception {
		
		List <WebElement> targets = getTargetsListByTaskType(type);		
		WebElement tileToDrag = bank.findElement(By.xpath("//*[@id='undefined_2']/ed-la-dnditem["+ index +"]/div")); // take always the first top tile
		WebElement target = targets.get(targetNumber-1);
		webDriver.dragAndDropElement(tileToDrag, target);
	}
	
    public void dragAndDropAnswerByTextToTargetMTTP(String text, int targetNumber, TaskTypes type) throws Exception {
		
		List <WebElement> targets = getTargetsListByTaskType(type);	
	   // WebElement tileToDrag = bank.findElement(By.xpath(".//div/ed-la-dndzone/div/ed-la-dnditem[1]/div/div[2]/span"));
		 WebElement tileToDrag = bank.findElement(By.xpath(".//div/ed-la-dndzone/div/ed-la-dnditem[contains(.,'" + text + "')]"));
		WebElement target = targets.get(targetNumber-1);
		webDriver.dragAndDropElement(tileToDrag, target);
	}
    

	public void dragAndDropAnswerByTextToBank(String text, TaskTypes type) throws Exception {
		
		List <WebElement> targets = getTargetsListByTaskType(type);
				
		WebElement tileToDrag = null;
		
		for (int i = 0; i < targets.size(); i++) {
			
			try {
				tileToDrag = targets.get(i).findElement(By.xpath(".//div[text()[contains(.,'" + text + "')]]"));
				if (tileToDrag!=null) break;
			} catch (Exception e) {
				//e.printStackTrace();
			}	
		}
				
		webDriver.dragAndDropElement(tileToDrag, bank);	
	}
	
	public void dragAndDropAnswerByTextToBankMTTP(String text, TaskTypes type) throws Exception {
		
		List <WebElement> targets = getTargetsListByTaskType(type);
				
		WebElement tileToDrag = null;
		
		for (int i = 0; i < targets.size(); i++) {
			
			try {
				//tileToDrag = targets.get(i).findElement(By.xpath(".//div/ed-la-dndzone/[contains(.,'" + text + "')]"));
				tileToDrag = targets.get(i).findElement(By.xpath(".//div[contains(.,'" + text + "')]"));
				if (tileToDrag!=null) break;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
				
		webDriver.dragAndDropElement(tileToDrag, bank);
		
	}
		
	public void dragAndDropFirstAnswer(TaskTypes type) throws Exception {
		
		List <WebElement> targets = getTargetsListByTaskType(type);
		if(targets.size()==0 || type.toString().equalsIgnoreCase("Droppable")) {
		//if(type.toString().equalsIgnoreCase("Droppable")) {
			webDriver.dragAndDropElement(draggable.get(0), droppable.get(0));
		}else {
			webDriver.dragAndDropElement(tiles.get(0), targets.get(0));
		}
	}
		
	public void checkTileCounterStateIsActive(int columnNumber, int itemNumber) throws Exception {
	
		testResultService.assertEquals(true, getItemCounterState(columnNumber,itemNumber).equalsIgnoreCase("active"), "Counter of item "+itemNumber+" in column "+columnNumber+" is not active(full)");
		
	}
	
	public void checkTileCounterStateIsEmpty(int columnNumber, int itemNumber) throws Exception {
		
		testResultService.assertEquals(true, getItemCounterState(columnNumber,itemNumber).isEmpty(), "Counter of item "+itemNumber+" in column "+columnNumber+" is not empty");
	}
		
	public void checkDragAndDropAnswerMark(String answer, int targetNum, boolean isCorrect, TaskTypes type) throws Exception {
			
		List <WebElement> targets = getTargetsListByTaskType(type);
		
		try {
			if (isCorrect)
				targets.get(targetNum-1).findElement(By.xpath(".//div[contains(@class,'v')][text()='" + answer + "']"));
		//	if (isCorrect) targets.get(targetNum-1).findElement(By.xpath(".//div[contains(@class,'v')][text()[contains(.,'" + answer + "')]"));
			
			
			else 
				targets.get(targetNum-1).findElement(By.xpath(".//div[contains(@class,'x')][text()='" + answer + "']"));
			//else targets.get(targetNum-1).findElement(By.xpath(".//div[contains(@class,'x')][contains(.,'" + answer + "')]"));
		} catch (Exception e) {
			testResultService.addFailTest("Answer mark is not found or not correct", false, true);
		}
		
	}
	
	
	public void checkDragAndDropAnswerMarkMTTP(String answer, int targetNum, boolean isCorrect, TaskTypes type) throws Exception {
		
		List <WebElement> targets = getTargetsListByTaskType(type);
		
		try {
			if (isCorrect) targets.get(targetNum-1).findElement(By.className("prTextToPic_text"));

			else targets.get(targetNum-1).findElement(By.className("prTextToPic_text"));
			
		} catch (Exception e) {
			testResultService.addFailTest("Answer mark is not found or not correct", false, true);
		}
		
	}
			
	private String getItemCounterState(int columnNumber, int itemNumber) throws Exception {
				
		return targetColumns.get(columnNumber-1).findElements(By.xpath(".//div[@class='itemsCounter']//li")).get(itemNumber-1).getAttribute("class");
						
	}
	
	private List <WebElement> getTargetsListByTaskType (TaskTypes type) throws Exception {
		
		List <WebElement> targets = null;
		
		switch (type) {
		
			case Classification: targets = targetColumns; break;
			case Close: targets = closeAnswerTargets; break;
			case Matching: targets = matchAnswerTargets; break;
			case MTTP: targets = MTTPAnswerTargets; break;
			case Droppable: targets = droppable; break;
		
			default: testResultService.addFailTest("Task type undefined or targets not found", false, true);
		
		}
		
		return targets;
						
	}
	
	// relevant for drag and drop of one question only
	public void validateSingleDragAndDropAnswerSign(String answer, boolean isCorrect) throws Exception {
		WebElement draggedAnswer = webDriver.getWebDriver().findElement(By.xpath("//div[@class='prCLZ__regContainer']//div[contains(@class,'dnditem')]"));
		if (isCorrect) {
			testResultService.assertEquals(true, draggedAnswer.getText().equals(answer), "Dragged Answser is Incorrect. Expected: " + answer + ". Actual: " + draggedAnswer.getText());
			testResultService.assertEquals(true, draggedAnswer.getAttribute("className").contains("--v"), "Answer Has X sign even though it is correct.");
		} else {
			testResultService.assertEquals(true, draggedAnswer.getAttribute("className").contains("--x"), "Answer Has V sign even though it is incorrect.");
		}
	}
	
	// relevant for drag and drop of one question only
	public void validateSingleDragAndDropAnswerInCorrectTab(String answer) throws Exception {
		WebElement draggedAnswer = webDriver.getWebDriver().findElement(By.xpath("//div[@class='prCLZ__regContainer']//div[contains(@class,'dnditem')]"));
		testResultService.assertEquals(answer, draggedAnswer.getText(), "Answer in Correct Tab is Incorrect.");
	}
	
	public void validateDragAndfDropNotAnswered(String[] expectedWordsInBank) throws Exception {
		// validate question is not answered
		WebElement draggedAnswer = webDriver.waitForElement("//div[@class='prCLZ__regContainer']//div[contains(@class,'dnditem')]", ByTypes.xpath, false, 5);
		testResultService.assertEquals(true, draggedAnswer == null, "Question is answered (not empty as expected");
		
		// validate expected words are found in bank
		List<WebElement> answersOptionsWeb = webDriver.getWebDriver().findElements(By.xpath("//div[@class='dnditem']"));
		int counter = 0;
		for (int i = 0; i < answersOptionsWeb.size(); i++) {
			for (int j = 0; j < expectedWordsInBank.length; j++) {
			
				if (answersOptionsWeb.get(i).getText().equals(expectedWordsInBank[j])) {
					counter++;
				}
			}
		}
		testResultService.assertEquals(true, counter == expectedWordsInBank.length, "Not All Expected Words are Found in Word Bank");
	}

	public boolean verifyDragAndDropAnswerStored(TaskTypes type) throws Exception {
		boolean answerStored = false;
		List <WebElement> targets = getTargetsListByTaskType(type);
		String attribute = targets.get(0).getAttribute("class");
		if(attribute.contains("noLine")) {
		 	answerStored = true;
		}
		return answerStored;		
	}
			
	
}
