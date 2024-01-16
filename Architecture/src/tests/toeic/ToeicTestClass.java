package tests.toeic;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.TimeoutException;

import pageObjects.toeic.toeicQuestionPage;

public class ToeicTestClass extends toeicBaseTest {

	@Before
	public void setup() throws Exception {
		super.setup();
	}

	@Test
	public void toeicPocTest() throws TimeoutException, Exception {
		openToeicTest();

		//toeicQuestionPage questionPage = ClickOnStart();
		//questionPage.answerCheckboxQuestion("q1", "a2");

	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

}
