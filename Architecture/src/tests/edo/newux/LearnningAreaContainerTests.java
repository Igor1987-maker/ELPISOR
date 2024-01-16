package tests.edo.newux;

import java.util.List;

import net.lightbody.bmp.core.har.Har;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import Enums.ByTypes;
import Interfaces.TestCaseParams;
import pageObjects.edo.NewUxLearningArea;
import testCategories.reg2;
@Category(reg2.class)
public class LearnningAreaContainerTests extends BasicNewUxTest {

	NewUxLearningArea learningArea;
	List<String> logList;
	Har har;
//19135
	@Before
	public void setup() throws Exception {

		System.setProperty("useProxy", "true");
		super.setup();

		createUserAndLoginNewUXClass();
		learningArea = new NewUxLearningArea(webDriver, testResultService);

	}

	@Test
	@TestCaseParams(testCaseID = { "22593" })
	public void testExploreAlphbet() throws Exception {
		homePage.navigateToCourseUnitAndLesson(courseCodes, "FD", 1, 1);
		learningArea.clickOnStep(2);

		// checkIfSeeTranslationIsDisabled();

		// Lessons/s1alaa.js
		// Content/Alphabet/s1alaa/s1alaae.xml
		// Media/Alphabet/s1alaa/s1alaaew01.mp3 (ON MOUSE HOVER)
		// Media/Alphabet/s1alaa/s1alaaes01.mp3 (ON LETTER ROW CLICK)
		// Graphics/Alphabet/s1alaa/s1alaaesp01.gif

		report.startStep("Check XML file");

		report.startStep("Check audio files");

		webDriver.hoverOnElement(webDriver.waitForElement("W0", ByTypes.id));
		learningArea.checkAudioFile("MediaPlayer1", "s1alaaew01.mp3");
		webDriver.waitForElement("W0", ByTypes.id).click();
		learningArea.checkAudioFile("MediaPlayer1", "s1alaaes01.mp3");

		learningArea.checkImage("I0", "s1alaaesp01.gif");

		report.startStep("check for broken images");

		webDriver.checkForBrokenImages();

		har = webDriver.getHar();
		checkFileResponse("Media/Alphabet/s1alaa/s1alaaew01.mp3", 206);
		checkFileResponse("Media/Alphabet/s1alaa/s1alaaes01.mp3", 206);
		checkFileResponse("Graphics/Alphabet/s1alaa/s1alaaesp01.gif", 200);
		checkFileResponse("Lessons/s1alaa.js", 200);
		checkFileResponse("Content/Alphabet/s1alaa/s1alaae.xml", 200);

		// netService.checkResponseIsOK("Media/Alphabet/s1alaa/s1alaaes01.mp3",har,206);
		//
		// netService.checkResponseIsOK("Graphics/Alphabet/s1alaa/s1alaaesp01.gif",har,200);
		//
		// netService.checkResponseIsOK("Lessons/s1alaa.js",har,200);
		//
		// netService.checkResponseIsOK("Content/Alphabet/s1alaa/s1alaae.xml",har,200);

	}

	public void checkFileResponse(String filePath, int responseCode) {
		netService.checkResponseIsOK(filePath, har, responseCode);
	}

	@Test
	@TestCaseParams(testCaseID = { "22594" })
	public void testExploreAlphbetMatchingGameMedia() throws Exception {
		homePage.navigateToCourseUnitAndLesson(courseCodes, "FD", 1, 1);
		learningArea.clickOnStep(3);
		learningArea.clickOnTask(2);

		String mp3File = "s1alaap03.mp3";
		String jsFile = "s1alaa.js";
		String itemCoode = "PMService.svc/GetItem?itemId=31518&itemCode=s1alaap03";
		// Lessons/s1alaa.js
		// Graphics/Alphabet/s1alaa/s1alaap03/s1alaapg1...12.gif
		// media/Alphabet/s1alaa/s1alaap03.mp3
		// PMService.svc/GetItem?itemId=31518&itemCode=s1alaap03

		// checkIfSeeTranslationIsDisabled();

		learningArea.checkAudioFile("PMediaPlayer", mp3File);

		report.startStep("check for broken images");

		har = webDriver.getHar();
		checkFileResponse("media/Alphabet/s1alaa/s1alaap03.mp3", 206);

		for (int i = 1; i < 13; i++) {
			checkFileResponse("Graphics/Alphabet/s1alaa/s1alaap03/s1alaapg" + i
					+ ".gif", 200);
		}

		checkFileResponse(
				"PMService.svc/GetItem?itemId=31518&itemCode=s1alaap03", 200);

	}

	@Test
	@TestCaseParams(testCaseID = { "22631" })
	public void testPracticeVocabalaryOld() throws Exception {
		homePage.navigateToCourseUnitAndLesson(courseCodes, "FD", 1, 5);
		learningArea.clickOnStep(2);

		// Lessons/s1veaa.js
		// Content/Vocabulary/s1veaa/s1veaae.xml
		// Media/Vocabulary/s1veaa/s1veaaew01.mp3 (ON MOUSE HOVER)
		// Media/Vocabulary/s1veaa/s1veaaes01.mp3(ON IMAGE ROW CLICK)
		// Graphics/Vocabulary/s1veaa/s1veaaesp01.gif

		// checkIfSeeTranslationIsDisabled();

		webDriver.hoverOnElement(webDriver.waitForElement("W0", ByTypes.id));
		learningArea.checkAudioFile("MediaPlayer1", "s1veaaew01.mp3");
		webDriver.waitForElement("W0", ByTypes.id).click();
		learningArea.checkAudioFile("MediaPlayer1", "s1veaaes01.mp3");

		har = webDriver.getHar();
		checkFileResponse("Media/Vocabulary/s1veaa/s1veaaew01.mp3", 206);
		checkFileResponse("Media/Vocabulary/s1veaa/s1veaaes01.mp3", 206);

		checkFileResponse("Content/Vocabulary/s1veaa/s1veaae.xml", 200);
		checkFileResponse("Graphics/Vocabulary/s1veaa/s1veaaesp01.gif", 200);

	}

	@Test
	@TestCaseParams(testCaseID = { "22633" })
	public void testPracticeVocabalaryOldMCQMedia() throws Exception {
		homePage.navigateToCourseUnitAndLesson(courseCodes, "FD", 1, 5);
		learningArea.clickOnStep(3);
		learningArea.clickOnTask(2);

		// Lessons/s1veaa.js
		// media/Vocabulary/s1veaa/s1veaap03.mp3
		// Graphics/Vocabulary/s1veaa/s1veaap03/s1veaap0311.......gif
		// PMService.svc/GetItem?itemId=31546&itemCode=s1veaap03

		// checkIfSeeTranslationIsDisabled();
		learningArea.clickOnMediaById("3");
		learningArea.checkAudioFile("PMediaPlayer", "s1veaap03.mp3");

		har = webDriver.getHar();
		checkFileResponse("media/Vocabulary/s1veaa/s1veaap03.mp3", 206);

		for (int i = 1; i < 13; i++) {
			checkFileResponse("Graphics/Vocabulary/s1veaa/s1veaap03/s1veaap03"
					+ i + ".gif", 200);
		}
		checkFileResponse(
				"PMService.svc/GetItem?itemId=31546&itemCode=s1veaap03", 200);

	}

	@Test
	@TestCaseParams(testCaseID = { "22643" })
	public void testPracticeVocabalaryOldCloseWhole() throws Exception {
		homePage.navigateToCourseUnitAndLesson(courseCodes, "FD", 5, 7);
		learningArea.clickOnStep(3);
		// learningArea.clickOnTask(3);

		// Graphics/Vocabulary/s1veal/s1vealp01/s1vealp01.gif
		// media/Vocabulary/s1veal/s1vealp01.mp3
		// PMService.svc/GetItem?itemId=31726&itemCode=s1vealp01

		// checkIfSeeTranslationIsDisabled();

		learningArea.checkAudioFile("PMediaPlayer", "s1vealp01.mp3");

		har = webDriver.getHar();
		checkFileResponse("media/Vocabulary/s1veal/s1vealp01.mp3", 206);
		checkFileResponse("Graphics/Vocabulary/s1veal/s1vealp01/s1vealp01.gif",
				200);

		checkFileResponse(
				"PMService.svc/GetItem?itemId=31726&itemCode=s1vealp01", 200);

	}

	@Test
	@TestCaseParams(testCaseID = { "22575" })
	public void testExploreGrammar() throws Exception {
		homePage.navigateToCourseUnitAndLesson(courseCodes, "I2", 5, 4);
		// learningArea.clickOnStep(2);

		// Lessons/i2gcun.js
		// Content/Grammar/i2gcun/i2gcun.js
		// Media/Grammar/i2gcun/i2gcune.mp3
		// Graphics/Grammar/i2gcun/i2gcunbg.gif

		learningArea.checkAudioFile("syncAudioPlayer", "i2gcune.mp3");

		har = webDriver.getHar();
		checkFileResponse("Lessons/i2gcun.js", 200);
		checkFileResponse("Content/Grammar/i2gcun/i2gcun.js", 200);
		checkFileResponse("Media/Grammar/i2gcun/i2gcune.mp3", 206);
		checkFileResponse("Graphics/Grammar/i2gcun/i2gcunbg.gif", 200);

	}
	
	@TestCaseParams(testCaseID = { "22577" })
	public void testGrammarCloseWholeDragAndDrop() throws Exception {
		homePage.navigateToCourseUnitAndLesson(courseCodes, "I2", 5, 4);
		learningArea.clickOnStep(4);
		learningArea.clickOnStartTest();
		learningArea.clickOnTask(3);

	}

	@Test
	@TestCaseParams(testCaseID = { "22578" })
	public void testPracticeGrammarCloseWholeDragAndDrop() throws Exception {
		homePage.navigateToCourseUnitAndLesson(courseCodes, "I2", 5, 4);
		learningArea.clickOnStep(2);
		// webDriver.checkWindowUsingEyes("window999");
		// Lessons/i2gcun.js
		// Media/Grammar/i2gcun/i2gcune.mp3
		// PMService.svc/GetItem?itemId=24275&itemCode=i2gcunp001

		learningArea.checkAudioFile("syncAudioPlayer", "i2gcune.mp3");

		har = webDriver.getHar();

		checkFileResponse("Lessons/i2gcun.js", 200);
		checkFileResponse("Media/Grammar/i2gcun/i2gcune.mp3", 206);
		checkFileResponse(
				"PMService.svc/GetItem?itemId=24275&itemCode=i2gcunp001", 200);

	}

	@Test
	@TestCaseParams(testCaseID = { "22650" })
	public void testPracticeGrammarFillInTheBlanks() throws Exception {
		homePage.navigateToCourseUnitAndLesson(courseCodes, "B1", 2, 5);

		learningArea.clickOnStep(2);
		learningArea.clickOnTask(7);

		// Lessons/b1gntt.js
		// Media/Grammar/b1gntt/b1gntte.mp3
		// Graphics/Grammar/b1gntt/b1gnttp007/BGDM0071.gif
		// Graphics/Grammar/b1gntt/b1gnttbg.gif
		// PMService.svc/GetItem?itemId=22413&itemCode=b1gnttp007

		har = webDriver.getHar();

		checkFileResponse("Media/Grammar/b1gntt/b1gntte.mp3", 206);
		checkFileResponse("Graphics/Grammar/b1gntt/b1gnttp007/BGDM0071.gif",
				200);
		checkFileResponse("Graphics/Grammar/b1gntt/b1gnttbg.gif", 200);
		checkFileResponse(
				"PMService.svc/GetItem?itemId=22413&itemCode=b1gnttp007", 200);

	}

	@Test
	@TestCaseParams(testCaseID = { "22457" })
	public void testExploreListening() throws Exception {
		homePage.navigateToCourseUnitAndLesson(courseCodes, "A3", 6, 1);

		// Lessons/a3lrnw.js
		// Content/Listening/a3lrnw/a3lrnw.js
		// Media/Listening/a3lrnw/a3lrnwe.mp3
		// Graphics/Listening/a3lrnw/a3lrnw.jpg

		har = webDriver.getHar();
		checkFileResponse("Lessons/a3lrnw.js", 200);
		checkFileResponse("Content/Listening/a3lrnw/a3lrnw.js", 200);
		checkFileResponse("Graphics/Listening/a3lrnw/a3lrnw.jpg", 200);
		checkFileResponse("Media/Listening/a3lrnw/a3lrnwe.mp3", 206);
	
	}

	@Test
	@TestCaseParams(testCaseID = { "22459" })
	public void testPracticeListeningClassification2ColumnsDragAndDrop()
			throws Exception {
		homePage.navigateToCourseUnitAndLesson(courseCodes, "A3", 6, 1);
		learningArea.clickOnStep(2);

		// Lessons/a3lrnw.js
		// Media/Listening/a3lrnw/a3lrnwe.mp3
		// Graphics/Listening/a3lrnw/a3lrnw.jpg
		// PMService.svc/GetItem?itemId=22181&itemCode=a3lrnwp001

		har = webDriver.getHar();
		checkFileResponse("Lessons/a3lrnw.js", 200);
		checkFileResponse("Graphics/Listening/a3lrnw/a3lrnw.jpg", 200);
		checkFileResponse(
				"PMService.svc/GetItem?itemId=22181&itemCode=a3lrnwp001", 200);
		checkFileResponse("Media/Listening/a3lrnw/a3lrnwe.mp3", 206);

	}

	@Test
	@TestCaseParams(testCaseID = { "22460" })
	public void testPracticeListeningCloseDragAndDropMedia() throws Exception {
		homePage.navigateToCourseUnitAndLesson(courseCodes, "A3", 6, 1);
		learningArea.clickOnStep(2);
		learningArea.clickOnTask(1);

		// Lessons/a3lrnw.js
		// Media/Listening/a3lrnw/a3lrnwe.mp3
		// Graphics/Listening/a3lrnw/a3lrnw.jpg
		// PMService.svc/GetItem?itemId=22182&itemCode=a3lrnwp002

		har = webDriver.getHar();
		checkFileResponse("Lessons/a3lrnw.js", 200);
		checkFileResponse("Graphics/Listening/a3lrnw/a3lrnw.jpg", 200);
		checkFileResponse(
				"PMService.svc/GetItem?itemId=22182&itemCode=a3lrnwp002", 200);
		checkFileResponse("Media/Listening/a3lrnw/a3lrnwe.mp3", 206);

	}

	@Test
	@TestCaseParams(testCaseID = { "22462" })
	public void testPracticeListeningFillInTheBlanksMedia() throws Exception {
		homePage.navigateToCourseUnitAndLesson(courseCodes, "A3", 6, 1);
		learningArea.clickOnStep(2);
		learningArea.clickOnTask(2);

		// Lessons/a3lrnw.js
		// Media/Listening/a3lrnw/a3lrnwe.mp3
		// Graphics/Listening/a3lrnw/a3lrnw.jpg
		// PMService.svc/GetItem?itemId=22183&itemCode=a3lrnwp003

		har = webDriver.getHar();
		checkFileResponse("Lessons/a3lrnw.js", 200);
		checkFileResponse("Graphics/Listening/a3lrnw/a3lrnw.jpg", 200);
		checkFileResponse(
				"PMService.svc/GetItem?itemId=22183&itemCode=a3lrnwp003", 200);
		checkFileResponse("Media/Listening/a3lrnw/a3lrnwe.mp3", 206);

	}

	@Test
	@TestCaseParams(testCaseID = { "22463" })
	public void testPracticeListeningMatchingDragAndDrop() throws Exception {
		homePage.navigateToCourseUnitAndLesson(courseCodes, "A3", 6, 1);
		learningArea.clickOnStep(2);
		learningArea.clickOnTask(4);

		// Lessons/a3lrnw.js
		// Media/Listening/a3lrnw/a3lrnwe.mp3
		// Graphics/Listening/a3lrnw/a3lrnw.jpg
		// PMService.svc/GetItem?itemId=57330&itemCode=a3lrnwp05

		har = webDriver.getHar();
		
		
		checkFileResponse("Lessons/a3lrnw.js", 200);
		checkFileResponse("Graphics/Listening/a3lrnw/a3lrnw.jpg", 200);
		checkFileResponse(
				"PMService.svc/GetItem?itemId=22183&itemCode=a3lrnwp05", 200);
		checkFileResponse("Media/Listening/a3lrnw/a3lrnwe.mp3", 206);

	}

	@Test
	@TestCaseParams(testCaseID = { "22464" })
	public void testPracticeListeningOpenEnded() throws Exception {
		homePage.navigateToCourseUnitAndLesson(courseCodes, "A3", 6, 1);
		learningArea.clickOnStep(2);
		learningArea.clickOnTask(5);

		// Lessons/a3lrnw.js
		// Media/Listening/a3lrnw/a3lrnwe.mp3
		// Graphics/Listening/a3lrnw/a3lrnw.jpg
		// PMService.svc/GetItem?itemId=57331&itemCode=a3lrnwp06

		har = webDriver.getHar();
		checkFileResponse("Lessons/a3lrnw.js", 200);
		checkFileResponse("Graphics/Listening/a3lrnw/a3lrnw.jpg", 200);
		checkFileResponse(
				"PMService.svc/GetItem?itemId=22183&itemCode=a3lrnwp06", 200);
		checkFileResponse("Media/Listening/a3lrnw/a3lrnwe.mp3", 206);

	}

	@Test
	@TestCaseParams(testCaseID = { "22465" })
	public void testPracticeListeningMCQ() throws Exception {
		homePage.navigateToCourseUnitAndLesson(courseCodes, "A3", 6, 1);
		learningArea.clickOnStep(2);
		learningArea.clickOnTask(6);

		// Lessons/a3lrnw.js
		// Media/Listening/a3lrnw/a3lrnwe.mp3
		// Graphics/Listening/a3lrnw/a3lrnw.jpg
		// PMService.svc/GetItem?itemId=57332&itemCode=a3lrnwp07

		har = webDriver.getHar();
		checkFileResponse("Lessons/a3lrnw.js", 200);
		checkFileResponse("Graphics/Listening/a3lrnw/a3lrnw.jpg", 200);
		checkFileResponse(
				"PMService.svc/GetItem?itemId=57332&itemCode=a3lrnwp07", 200);
		checkFileResponse("Media/Listening/a3lrnw/a3lrnwe.mp3", 206);

	}

	@Test
	@TestCaseParams(testCaseID = { "22466" })
	public void testPracticeListeningMatchTextToPicDragAndDrop()
			throws Exception {
		homePage.navigateToCourseUnitAndLesson(courseCodes, "B1", 3, 1);
		learningArea.clickOnStep(2);

		// Lessons/b1lrfo.js
		// Media/Listening/b1lrfo/b1lrfoe.mp3
		// Graphics/Listening/b1lrfo/b1lrfo.jpg
		// Graphics/Listening/b1lrfo/b1lrfop01.....06
		// PMService.svc/GetItem?itemId=56893&itemCode=b1lrfop01

		har = webDriver.getHar();
		checkFileResponse("Lessons/b1lrfo.js", 200);
		checkFileResponse("Graphics/Listening/b1lrfo/b1lrfo.jpg", 200);
		checkFileResponse(
				"PMService.svc/GetItem?itemId=56893&itemCode=b1lrfop01", 200);
		checkFileResponse("Media/Listening/b1lrfo/b1lrfoe.mp3", 206);

		for (int i = 1; i < 7; i++) {
			checkFileResponse("Graphics/Listening/b1lrfo/b1lrfop0" + i, 200);
		}

	}

	@Test
	@TestCaseParams(testCaseID = { "22467" })
	public void testPracticeListeningMarkTheTrue() throws Exception {
		homePage.navigateToCourseUnitAndLesson(courseCodes, "B1", 3, 1);
		learningArea.clickOnStep(3);

		learningArea.clickOnTask(3);

		// Lessons/b1lrfo.js
		// Media/Listening/b1lrfo/b1lrfoe.mp3
		// Graphics/Listening/b1lrfo/b1lrfo.jpg
		// PMService.svc/GetItem?itemId=56894&itemCode=b1lrfop04

		har = webDriver.getHar();
		checkFileResponse("Lessons/b1lrfo.js", 200);
		checkFileResponse("Graphics/Listening/b1lrfo/b1lrfo.jpg", 200);
		checkFileResponse(
				"PMService.svc/GetItem?itemId=56894&itemCode=b1lrfop04", 200);
		checkFileResponse("Media/Listening/b1lrfo/b1lrfoe.mp3", 206);

	}

	@Test
	@TestCaseParams(testCaseID = { "22596" })
	public void testPracticeListeningMCQMedia() throws Exception {
		homePage.navigateToCourseUnitAndLesson(courseCodes, "A1", 5, 1);
		learningArea.clickOnStep(3);

		// Lessons/a1lrad.js
		// Media/Listening/a1lrad/a1lrade.mp3
		// Graphics/Listening/a1lrad/a1lrad.jpg
		// PMService.svc/GetItem?itemId=26959&itemCode=a1lradp001

		har = webDriver.getHar();

		checkFileResponse("Lessons/a1lrad.js", 200);

		checkFileResponse("Media/Listening/a1lrad/a1lrade.mp3", 206);

		checkFileResponse("Graphics/Listening/a1lrad/a1lrad.jpg", 200);

		checkFileResponse(
				"PMService.svc/GetItem?itemId=26959&itemCode=a1lradp001", 200);
	}

	@Test
	@TestCaseParams(testCaseID = { "22635" })
	public void testPracticeListeningTV() throws Exception {
		homePage.navigateToCourseUnitAndLesson(courseCodes, "FD", 2, 1);
		learningArea.clickOnStep(2);

		// Lessons/s1ltaa.js
		// Content/Listening/s1ltaa/s1ltaa.js
		// Media/Listening/s1ltaa/s1ltaae.flv

		har = webDriver.getHar();
		checkFileResponse("Lessons/s1ltaa.js", 200);

		checkFileResponse("Content/Listening/s1ltaa/s1ltaa.js", 200);

		checkFileResponse("Media/Listening/s1ltaa/s1ltaae.flv", 200);

	}

	@TestCaseParams(testCaseID = { "22640" })
	public void testPractiveListeningOpenEndedSegments() throws Exception {

	}

	@Test
	@TestCaseParams(testCaseID = { "22642" })
	public void testPracticeListeningTVCloseDragAndDropMedia() throws Exception {
		homePage.navigateToCourseUnitAndLesson(courseCodes, "A1", 1, 1);
		learningArea.clickOnStep(2);

		// Lessons/a1lttr.js
		// Media/Listening/a1lttr/a1lttre.flv
		// PMService.svc/GetItem?itemId=26811&itemCode=a1lttrp001

		har = webDriver.getHar();
		checkFileResponse("Lessons/a1lttr.js", 200);
		checkFileResponse("Media/Listening/a1lttr/a1lttre.flv", 200);
		checkFileResponse(
				"PMService.svc/GetItem?itemId=26811&itemCode=a1lttrp001", 200);

	}

	@Test
	@TestCaseParams(testCaseID = { "22648" })
	public void testPracticeListeningTVMCQMedia() throws Exception {
		homePage.navigateToCourseUnitAndLesson(courseCodes, "FD", 5, 1);
		learningArea.clickOnStep(3);

		// Lessons/s1ltad.js
		// media/Listening/s1ltad/s1ltadp03.mp3
		// Media/Listening/s1ltad/s1ltade.flv
		// PMService.svc/GetItem?itemId=31696&itemCode=s1ltadp03

		har = webDriver.getHar();

		checkFileResponse("Lessons/s1ltad.js", 200);
		checkFileResponse("media/Listening/s1ltad/s1ltadp03.mp3", 206);
		checkFileResponse("Media/Listening/s1ltad/s1ltade.flv", 200);
		checkFileResponse(
				"PMService.svc/GetItem?itemId=31696&itemCode=s1ltadp03", 200);

	}

	@Test
	@TestCaseParams(testCaseID = { "22651" })
	public void testPracticeListeningOpenEndedSegmentsMedia() throws Exception {
		homePage.navigateToCourseUnitAndLesson(courseCodes, "FD", 2, 1);
		learningArea.clickOnStep(3);

		// Lessons/s1ltaa.js
		// Content/Listening/s1ltaa/page1/bg.jpg
		// Localization/Content/spa/Listening/s1ltaar_ins_spa.xml (IF LOCALIZED)
		// Content/Listening/s1ltaa/Page1/bubble1.mp3 (IF PLAY PRESSED)
		// Content/Listening/s1ltaa/Page2/bubble1.mp3 (IF PAGE 2 & PLAY PRESSED)

		har = webDriver.getHar();

		checkFileResponse("Lessons/s1ltaa.js", 200);
		checkFileResponse("Content/Listening/s1ltaa/page1/bg.jpg", 200);

	}

	@Test
	@TestCaseParams(testCaseID = { "22468" })
	public void testExploreReading() throws Exception {
		homePage.navigateToCourseUnitAndLesson(courseCodes, "A3", 5, 2);

		// Lessons/a3rsmg.js
		// Content/Reading/a3rsmg/a3rsmg.js
		// Media/Reading/a3rsmg/a3rsmge.mp3

		har = webDriver.getHar();

		checkFileResponse("Lessons/a3rsmg.js", 200);
		checkFileResponse("Media/Reading/a3rsmg/a3rsmge.mp3", 200);

	}

	@Test
	@TestCaseParams(testCaseID = { "22472" })
	public void testExploreReadingSwquenceImageDragAndDrop() throws Exception {
		homePage.navigateToCourseUnitAndLesson(courseCodes, "A3", 5, 2);

		learningArea.clickOnStep(2);
		learningArea.clickOnTask(2);

		// Lessons/a3rsmg.js
		// Media/Reading/a3rsmg/a3rsmge.mp3
		// Graphics/Reading/a3rsmg/a3rsmgp001....006
		// PMService.svc/GetItem?itemId=22135&itemCode=a3rsmgp002

		har = webDriver.getHar();

		checkFileResponse("Lessons/a3rsmg.js", 200);
		checkFileResponse("Media/Reading/a3rsmg/a3rsmge.mp3", 206);
		checkFileResponse(
				"PMService.svc/GetItem?itemId=22135&itemCode=a3rsmgp002", 200);
		for (int i = 1; i < 7; i++) {
			checkFileResponse("Graphics/Reading/a3rsmg/a3rsmgp00" + i, 200);
		}

	}

	@Test
	@TestCaseParams(testCaseID = { "22473" })
	public void testExploreReadingFreeWriteERater() throws Exception {
		homePage.navigateToCourseUnitAndLesson(courseCodes, "A3", 5, 2);

		learningArea.clickOnStep(2);
		learningArea.clickOnTask(5);

		// Lessons/a3rsmg.js
		// Media/Reading/a3rsmg/a3rsmge.mp3
		// PMService.svc/GetItem?itemId=57328&itemCode=a3rsmgp06

		har = webDriver.getHar();

		checkFileResponse("Lessons/a3rsmg.js", 200);
		checkFileResponse("Media/Reading/a3rsmg/a3rsmge.mp3", 206);
		checkFileResponse(
				"PMService.svc/GetItem?itemId=57328&itemCode=a3rsmgp06", 200);

	}

	@Test
	@TestCaseParams(testCaseID = { "22574" })
	public void testPracticeReadingSequenceSentenceDragAndDrop()
			throws Exception {

		homePage.navigateToCourseUnitAndLesson(courseCodes, "A1", 6, 2);
		learningArea.clickOnStep(2);
//		learningArea.clickOnTask(1);

		// Lessons/a1rlpe.js
		// Media/Reading/a1rlpe/a1rlpee.mp3
		// PMService.svc/GetItem?itemId=27003&itemCode=a1rlpep002

		har = webDriver.getHar();
		checkFileResponse("Lessons/a1rlpe.js", 200);
		checkFileResponse("Media/Reading/a1rlpe/a1rlpee.mp3", 206);
		checkFileResponse(
				"PMService.svc/GetItem?itemId=27002&itemCode=a1rlpep002", 200);

	}

	@Test
	@TestCaseParams(testCaseID = { "22634" })
	public void testExploreReadingLimited() throws Exception {
		homePage.navigateToCourseUnitAndLesson(courseCodes, "FD", 2, 3);
		learningArea.clickOnStep(2);

		// Lessons/s1rdaa.js
		// Content/Reading/s1rdaa/s1rdaa.js
		// Media/Reading/s1rdaa/s1rdaae.mp3

		har = webDriver.getHar();
		checkFileResponse("Lessons/s1rdaa.js", 200);
		checkFileResponse("Content/Reading/s1rdaa/s1rdaa.js", 200);
		checkFileResponse("Media/Reading/s1rdaa/s1rdaae.mp3", 206);

	}

	@Test
	@TestCaseParams(testCaseID = { "22641" })
	public void testPracticeReadingClassification3ColumnsDragAndDrop()
			throws Exception {
		homePage.navigateToCourseUnitAndLesson(courseCodes, "I2", 5, 2);

		learningArea.clickOnStep(2);
		learningArea.clickOnTask(1);

		// Lessons/i2rsts.js
		// Media/Reading/i2rsts/i2rstse.mp3
		// PMService.svc/GetItem?itemId=24265&itemCode=i2rstsp002

		har = webDriver.getHar();

		checkFileResponse("Lessons/i2rsts.js", 200);
		checkFileResponse("Media/Reading/i2rsts/i2rstse.mp3", 206);
		checkFileResponse(
				"PMService.svc/GetItem?itemId=24265&itemCode=i2rstsp002", 200);

	}

	@TestCaseParams(testCaseID = { "22647" })
	public void testPracticeReadingMCQ() throws Exception {
		// newUxHomePage.navigateToCourseUnitAndLesson(courseCodes, courseCode,
		// unitNumber, lessonNumber)
	}

	@Test
	@TestCaseParams(testCaseID = { "22474" })
	public void testPrepareSpeakingMedia() throws Exception {
		homePage.navigateToCourseUnitAndLesson(courseCodes, "FD", 3, 2);

		// Runtime/Lessons/s1svab.js
		// Content/Speaking/s1svab/page1/bg.jpg
		// Content/Speaking/s1svab/Page1/bubble1.mp3 (ON PRESS PLAY BTN)

		har = webDriver.getHar();
		checkFileResponse("Runtime/Lessons/s1svab.js", 200);
		checkFileResponse("Content/Speaking/s1svab/page1/bg.jpg", 200);
		checkFileResponse("Content/Speaking/s1svab/Page1/bubble1.mp3", 206);

	}

	@Test
	@TestCaseParams(testCaseID = { "22475" })
	public void testExploreSpeakingTV() throws Exception {
		homePage.navigateToCourseUnitAndLesson(courseCodes, "FD", 3, 2);

		learningArea.clickOnStep(2);

		// Runtime/Lessons/s1svab.js
		// Content/Speaking/s1svab/s1svab.js
		// Media/Speaking/s1svab/s1svabe.flv

		har = webDriver.getHar();
		checkFileResponse("Runtime/Lessons/s1svab.js", 200);

		checkFileResponse("Content/Speaking/s1svab/s1svab.js", 200);

		checkFileResponse("Media/Speaking/s1svab/s1svabe.flv", 200);

	}

	@Test
	@TestCaseParams(testCaseID = { "22568" })
	public void testPracticeSpeakingMCQMedia() throws Exception {
		homePage.navigateToCourseUnitAndLesson(courseCodes, "FD", 3, 2);

		learningArea.clickOnStep(3);

		// Lessons/s1svab.js
		// Media/Speaking/s1svab/s1svabe.flv
		// Media/Speaking/s1svab/s1svabp01.mp3
		// PMService.svc/GetItem?itemId=31612&itemCode=s1svabp01

		har = webDriver.getHar();

		checkFileResponse("Lessons/s1svab.js", 200);

		checkFileResponse("Media/Speaking/s1svab/s1svabe.flv", 200);

		checkFileResponse("Media/Speaking/s1svab/s1svabp01.mp3", 200);

		checkFileResponse(
				"PMService.svc/GetItem?itemId=31612&itemCode=s1svabp01", 200);

	}

	@Test
	@TestCaseParams(testCaseID = { "22571" })
	public void testPracticeSpeakingMCQMediaSR() throws Exception {
		homePage.navigateToCourseUnitAndLesson(courseCodes, "FD", 3, 2);

		learningArea.clickOnStep(3);
		learningArea.clickOnTask(2);

		// Lessons/s1svab.js
		// Media/Speaking/s1svab/s1svabe.flv
		// media/Speaking/s1svab/s1svabp02.mp3
		// PMService.svc/GetItem?itemId=31613&itemCode=s1svabp02

		har = webDriver.getHar();

		checkFileResponse("Lessons/s1svab.js", 200);

		checkFileResponse("Media/Speaking/s1svab/s1svabe.flv", 200);

		checkFileResponse("media/Speaking/s1svab/s1svabp02.mp3", 206);

		checkFileResponse(
				"PMService.svc/GetItem?itemId=31613&itemCode=s1svabp02", 200);

	}

	@Test
	@TestCaseParams(testCaseID = { "22572" })
	public void testInteract1SpeakingSR() throws Exception {
		homePage.navigateToCourseUnitAndLesson(courseCodes, "FD", 3, 2);

		learningArea.clickOnStep(4);

		// Lessons/s1svab.js
		// media/Speaking/s1svab/s1svabv.flv
		// media/Speaking/s1svab/s1svabv.mp3
		// Content/speaking/s1svab/s1svabv.js?temId=31615&itemCode=s1svabv

		har = webDriver.getHar();

		checkFileResponse("Lessons/s1svab.js", 200);

		checkFileResponse("media/Speaking/s1svab/s1svabv.flv", 200);

		checkFileResponse("media/Speaking/s1svab/s1svabv.mp3", 206);

		checkFileResponse(
				"Content/speaking/s1svab/s1svabv.js?temId=31615&itemCode=s1svabv",
				200);

	}

	@Test
	@TestCaseParams(testCaseID = { "22573" })
	public void testInteract2SpeakingSR() throws Exception {
		homePage.navigateToCourseUnitAndLesson(courseCodes, "FD", 3, 2);

		learningArea.clickOnStep(5);

		// Lessons/s1svab.js
		// media/Speaking/s1svab/s1svabw.flv
		// media/Speaking/s1svab/s1svabw.mp3
		// Content/speaking/s1svab/s1svabw.js?itemId=31616&itemCode=s1svabw

		har = webDriver.getHar();

		checkFileResponse("Lessons/s1svab.js", 200);

		checkFileResponse("media/Speaking/s1svab/s1svabw.flv", 200);

		checkFileResponse("media/Speaking/s1svab/s1svabw.mp3", 206);

		checkFileResponse(
				"Content/speaking/s1svab/s1svabw.js?itemId=31616&itemCode=s1svabw",
				200);

	}

	@Test
	@TestCaseParams(testCaseID = { "22616" })
	public void testExploreSpeaking() throws Exception {
		homePage.navigateToCourseUnitAndLesson(courseCodes, "B3", 4, 3);

		// Lessons/b3ssjp.js
		// Content/Speaking/b3ssjp/b3ssjp.js
		// Media/Speaking/b3ssjp/b3ssjpe.mp3
		// Graphics/Speaking/b3ssjp/b3ssjp.jpg

		har = webDriver.getHar();

		checkFileResponse("Lessons/b3ssjp.js", 200);

		checkFileResponse("Content/Speaking/b3ssjp/b3ssjp.js", 200);

		checkFileResponse("Media/Speaking/b3ssjp/b3ssjpe.mp3", 206);

		checkFileResponse("Graphics/Speaking/b3ssjp/b3ssjp.jpg", 200);

	}

	@Test
	@TestCaseParams(testCaseID = { "22619" })
	public void testPractice1SpeakingSR() throws Exception {
		homePage.navigateToCourseUnitAndLesson(courseCodes, "B3", 4, 3);

		learningArea.clickOnStep(2);

		// Lessons/b3ssjp.js
		// media/Speaking/b3ssjp/b3ssjpe.mp3
		// Content/speaking/b3ssjp/b3ssjpn.js?itemId=23414&itemCode=b3ssjpn

		har = webDriver.getHar();

		checkFileResponse("Lessons/b3ssjp.js", 200);

		checkFileResponse("media/Speaking/b3ssjp/b3ssjpe.mp3", 206);

		checkFileResponse(
				"Content/speaking/b3ssjp/b3ssjpn.js?itemId=23414&itemCode=b3ssjpn",
				200);

	}

	@Test
	@TestCaseParams(testCaseID = { "22621" })
	public void testPractice2SpeakingSR() throws Exception {
		homePage.navigateToCourseUnitAndLesson(courseCodes, "B3", 4, 3);

		learningArea.clickOnStep(3);

		// Lessons/b3ssjp.js
		// media/Speaking/B3SSJP/B3SSJPMC000.mp3 (LOADED AFTER PRESS START)
		// Content/speaking/b3ssjp/b3ssjpm.js?itemId=23415&itemCode=b3ssjpm
		// Graphics/Speaking/b3ssjp/b3ssjp.jpg

		har = webDriver.getHar();

		checkFileResponse("Lessons/b3ssjp.js", 200);

		checkFileResponse("media/Speaking/B3SSJP/B3SSJPMC000.mp3", 206);

		checkFileResponse(
				"Content/speaking/b3ssjp/b3ssjpm.js?itemId=23415&itemCode=b3ssjpm",
				200);

		checkFileResponse("Graphics/Speaking/b3ssjp/b3ssjp.jpg", 200);

	}

	@TestCaseParams(testCaseID = { "22638" })
	public void testExploreSpeakingSP() throws Exception {
		// newUxHomePage.navigateToCourseUnitAndLesson(courseCodes, courseCode,
		// unitNumber, lessonNumber)
	}

	@Test
	@TestCaseParams(testCaseID = { "22588" })
	public void testExploreVovabalaryNew() throws Exception {
		homePage.navigateToCourseUnitAndLesson(courseCodes, "B1", 2, 6);

		// Lessons/b1veaf.js
		// Content/Vocabulary/b1veaf/b1veafe.xml
		// Media/Vocabulary/b1veaf/b1veafee01.mp3 (ONLY ON PRESS HEAR)
		// Graphics/Vocabulary/b1veaf/b1veafep01.jpg

		har = webDriver.getHar();

		checkFileResponse("Lessons/b1veaf.js", 200);

		checkFileResponse("Content/Vocabulary/b1veaf/b1veafe.xml", 200);

		checkFileResponse("Media/Vocabulary/b1veaf/b1veafee01.mp3", 206);

		checkFileResponse("Graphics/Vocabulary/b1veaf/b1veafep01.jpg", 200);

	}

	@Test
	@TestCaseParams(testCaseID = { "22589" })
	public void testPracticeVocabularyNewMatchingGame() throws Exception {
		homePage.navigateToCourseUnitAndLesson(courseCodes, "B1", 2, 6);

		learningArea.clickOnStep(2);

		// Lessons/b1veaf.js
		// Graphics/Vocabulary/b1veaf/b1veafp01.....12/b1veafpg1.gif
		// PMService.svc/GetItem?itemId=62690&itemCode=b1veafp01

		har = webDriver.getHar();

		checkFileResponse("Lessons/b1veaf.js", 200);

		checkFileResponse(
				"PMService.svc/GetItem?itemId=62690&itemCode=b1veafp01", 200);

		for (int i = 1; i < 13; i++) {
			checkFileResponse("Graphics/Vocabulary/b1veaf/b1veafp0" + i, 200);
		}

	}

	@Test
	@TestCaseParams(testCaseID = { "22590" })
	public void testPracticeVacabularyNewOpenEndedMedia() throws Exception {
		homePage.navigateToCourseUnitAndLesson(courseCodes, "B1", 2, 6);

		learningArea.clickOnStep(2);

		learningArea.clickOnTask(2);

		// media/Vocabulary/b1veaf/b1veafp03.mp3
		// PMService.svc/GetItem?itemId=62692&itemCode=b1veafp03

		har = webDriver.getHar();

		checkFileResponse("media/Vocabulary/b1veaf/b1veafp03.mp3", 206);

		checkFileResponse(
				"PMService.svc/GetItem?itemId=62692&itemCode=b1veafp03", 200);

	}

	@Test
	@TestCaseParams(testCaseID = { "22626" })
	public void testPrapareWriting() throws Exception {
		homePage.navigateToCourseUnitAndLesson(courseCodes, "FD", 2, 4);

		// Lessons/s1weaa.js
		// Content/Writing/s1weaa/s1weaar_ins.xml
		// Content/Writing/s1weaa/page1/bg.jpg

		har = webDriver.getHar();

		checkFileResponse("Lessons/s1weaa.js", 200);

		checkFileResponse("Content/Writing/s1weaa/s1weaar_ins.xml", 200);

		checkFileResponse("Content/Writing/s1weaa/page1/bg.jpg", 200);

	}

	@Test
	@TestCaseParams(testCaseID = { "22627" })
	public void testExploreWriting() throws Exception {
		homePage.navigateToCourseUnitAndLesson(courseCodes, "FD", 2, 4);

		learningArea.clickOnStep(2);

		// Lessons/s1weaa.js
		// Content/Writing/s1weaa/s1weaa.js
		// Media/Writing/s1weaa/s1weaae.mp3

		har = webDriver.getHar();

		checkFileResponse("Lessons/s1weaa.js", 200);

		checkFileResponse("Content/Writing/s1weaa/s1weaa.js", 200);

		checkFileResponse("Media/Writing/s1weaa/s1weaae.mp3", 206);

	}

	@Test
	@TestCaseParams(testCaseID = { "22628" })
	public void testExploreWritingFreeWrite() throws Exception {
		homePage.navigateToCourseUnitAndLesson(courseCodes, "FD", 2, 4);

		learningArea.clickOnStep(3);

		learningArea.clickOnTask(2);
		//
		// Lessons/s1weaa.js
		// Media/Writing/s1weaa/s1weaae.mp3
		// PMService.svc/GetItem?itemId=31574&itemCode=s1weaap03

		har = webDriver.getHar();

		checkFileResponse("Lessons/s1weaa.js", 200);

		checkFileResponse("Media/Writing/s1weaa/s1weaae.mp3", 206);

		checkFileResponse(
				"PMService.svc/GetItem?itemId=31574&itemCode=s1weaap03", 200);

	}

	private void checkIfSeeTranslationIsDisabled() throws Exception {
		boolean isSeeTranslationEnabled = learningArea
				.checkIfSeeTranslationIsEnabled();

		testResultService.assertEquals(false, isSeeTranslationEnabled,
				"See translation was not disabled", true);
	}

	@After
	public void tearDown() throws Exception {

		webDriver.checkForBrokenImages();
		// logList = webDriver.getConsoleLogs("", false);
		// pageHelper.checkConsoleLogsForErrors(logList);
		super.tearDown();
	}

}
