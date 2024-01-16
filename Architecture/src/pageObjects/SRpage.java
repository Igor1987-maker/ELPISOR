package pageObjects;



import services.TestResultService;
import services.TextService;
import Enums.ByTypes;
import Enums.SRWordLevel;
import drivers.GenericWebDriver;

public class SRpage extends GenericPage {

	public SRpage(GenericWebDriver webDriver,TestResultService testResultService) throws Exception {
		super(webDriver,testResultService);
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
	public void CheckWordScore(String word, int expectedWordLevel,
			TextService textService) throws Exception {
		System.out.println("Checking WL of word: "+word+". Expected WL is: "+expectedWordLevel);
		boolean found = false;
		SRWordLevel wordLevel = null;
		if (expectedWordLevel <= 3) {
			wordLevel = SRWordLevel.failed;
		} else if (expectedWordLevel >=4) {
			wordLevel = SRWordLevel.success;
		}
		webDriver.waitForElement(
				"//div[@id='txtOriginal']//span[@class='"
						+ wordLevel.toString() + "'][contains(text(),"
						+ textService.resolveAprostophes(word) + ")]",
				ByTypes.xpath,webDriver.getTimeout(),false,"Word level color (class) did not matched");

	}
	public void checkWordsLevels(String[] words, String[] wordsScores,
			TextService textService) throws Exception {
		webDriver.printScreen("checkWordsLevels");
		try {
			for (int i = 0; i < words.length; i++) {
				CheckWordScore(words[i], Integer.valueOf(wordsScores[i]),
						textService);
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			testResultService.addFailTest("Word  is missing");
		}
	}
	
	public String getWordsScoring(String elementId) throws Exception {
		String str = null;
		try {
			str = webDriver.waitForElement(elementId, ByTypes.id).getAttribute(
					"value");
			System.out.println("word scoring is:" + str);
			str = str.replace(",", "");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;

	}
	
	public int getSLFromConsoleLog(String message,TextService textService){
//		LogEntries logEntries = webDriver.getConsoleLogEntries();
//		List<String[]> logList = textService
//				.getListFromLogEntries(logEntries,"result");
//		String json=logList.get(0)[0];
//		 json=message.substring(message.indexOf("{"));
//		System.out.println(json);
		return 0;
	}
	


}
