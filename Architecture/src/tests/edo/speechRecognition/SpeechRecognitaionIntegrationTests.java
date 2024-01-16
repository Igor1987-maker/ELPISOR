package tests.edo.speechRecognition;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import pageObjects.EdoHomePage;
import tests.misc.EdusoftWebTest;

public class SpeechRecognitaionIntegrationTests extends EdusoftWebTest {

	@Before
	public void setup() throws Exception {
		super.setup();

	}

	// Test case 13317
	// SR: Interact 1 - Play the Complete Conversation
	@Test
	public void testInteract1PlayThecompleteConversation() throws Exception {
		startStep("Init test data");
		this.testCaseId = "13317";

		startStep("Login to EDO as student");

		startStep("Go to course unit - interact 1");

		startStep("Click on right arrow");

		startStep("Complete dialog");

		startStep("Click on the Play button");

		startStep("Start assertions");

		startStep("Check that the complete dialog plays automaticly");

		startStep("Check that the recording plays normally");

		startStep("Check that every played sentence is highlighted");

		startStep("Check that all buttons are dispable until playback ends");

	}

	// Test case 13410
	// Record your self panel opens
	@Test
	public void testTalkingIdiomsRecordYourselfPanelOpens() throws Exception {
		startStep("Init test data");

		startStep("Login as student to EDO");
		EdoHomePage edoHomePage = pageHelper.loginAsStudent();

		startStep("Go to Talkong idiums");
		edoHomePage.clickOnCommunity();
		edoHomePage.clickOnTalkingIdioms();
		startStep("Click on Recored the idiums");

	}

	// Test case 13411
	// When Hear button pressed

	@Test
	public void testTalkingIdiomsHearButtonPressed() throws Exception {
		startStep("Init test data");

		startStep("Login as student to EDO");
		EdoHomePage edoHomePage = pageHelper.loginAsStudent();

		startStep("Go to Talkong idiums");
		edoHomePage.clickOnCommunity();
		edoHomePage.clickOnTalkingIdioms();
		startStep("Click on Hear ");
	}
	
	//Test case 13412
	//When Record button is pressed
	@Test
	public void testTalkingIdiomsRecordButtonPressed()throws Exception{
		startStep("Init test data");

		startStep("Login as student to EDO");
		EdoHomePage edoHomePage = pageHelper.loginAsStudent();

		startStep("Go to Talkong idiums");
		edoHomePage.clickOnCommunity();
		edoHomePage.clickOnTalkingIdioms();
	}
	
	//Test case 13413
	//Recording list result behaviour
	@Test
	public void testTalkingIdiomsRecordingListResultsBehavior()throws Exception{
		
		startStep("Login to EDO");
		
		startStep("Open the recording panel");
		
		startStep("Click on the record button");
		
		startStep("finish the recording and wait for 2 seconds");
		
		startStep("Start assertions");
		
		startStep("Check that word level feedback is displayed");
		
		startStep("Check that sentence level is displayed");
		
		startStep("Check the item added to the recordings list");

	}
	//Test case id 13414
	//Send to Teacher
	@Test
	public void testRecordAndSendToTeacher()throws Exception{
		startStep("Init test data");
		
		startStep("Login to EDO as student");
		
		startStep("Go to Course unit and click on Explore");
		
		startStep("Click on Record yourself");
		
		startStep("Start test assertions");
		
		startStep("Check that recording panel opens");
		
		startStep("Check that the text 'Click hear to listen' appears");
		
		startStep("Check that Hear and Record buttons are displayed and enabeld");
		
		startStep("Check that the text you have chosen is displayed in the middle of the panel");
		
		startStep("Check that the Send to teacher button is displayed and enabled");
		
		
		
		
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();

	}

}
