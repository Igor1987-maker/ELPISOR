package tests.misc;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Objects.Student;
import pageObjects.EdoHomePage;
import pageObjects.EdoLoginPage;

public class TestsErater extends EdusoftWebTest {

	@Before
	public void setup() throws Exception {
		super.setup();
	}

	@Test
	public void testSubmitTextToEraterAndCheckJsonInDb1() throws Exception {
		submitTextToErater("files/assayFiles/text1.txt", "Basic 3 2012",
				"Tickets, Please!", "Tickets, Please!", 7);
	}

	@Test
	public void testSubmitTextToEraterAndCheckJsonInDb2() throws Exception {
		submitTextToErater("files/assayFiles/text2.txt", "Basic 1 2012",
				"Meet A Rock Star", "Meet Me!", 5);
	}
//
	@Test
	public void testSubmitTextToEraterAndCheckJsonInDb3() throws Exception {
		submitTextToErater("files/assayFiles/text3.txt", "Basic 1 2012",
				"At The Restaurant", "New Mexican Restaurant", 5);
	}
//
	@Test
	public void testSubmitTextToEraterAndCheckJsonInDb4() throws Exception {
		submitTextToErater("files/assayFiles/text4.txt", "Basic 1 2012",
				"On A Business Trip", "More Women Traveling", 6);
	}
//
	@Test
	public void testSubmitTextToEraterAndCheckJsonInDb5() throws Exception {
		submitTextToErater("files/assayFiles/text5.txt", "Basic 1 2012",
				"Going Out", "Richard's Romance", 6);
	}
//
	@Test
	public void testSubmitTextToEraterAndCheckJsonInDb6() throws Exception {
		submitTextToErater("files/assayFiles/text6.txt", "Basic 1 2012",
				"About People", "Monaco", 6);
	}
//
//	@Test
//	public void testSubmitTextToEraterAndCheckJsonInDb7() throws Exception {
//		submitTextToErater("files/assayFiles/text7.txt", "Basic 1 2012",
//				"For Sale", "Car for Sale", 5);
//	}
//
//	@Test
//	public void testSubmitTextToEraterAndCheckJsonInDb8() throws Exception {
//		submitTextToErater("files/assayFiles/text8.txt", "Basic 1 2012",
//				"On The Move", "Susan's Train Ride", 6);
//	}
//
//	@Test
//	public void testSubmitTextToEraterAndCheckJsonInDb9() throws Exception {
//
//		submitTextToErater("files/assayFiles/text9.txt", "Basic 2 2012",
//				"Buying And Selling", "Wrong Color", 5);
//	}
//
//	@Test
//	public void testSubmitTextToEraterAndCheckJsonInDb10() throws Exception {
//		submitTextToErater("files/assayFiles/text10.txt", "Basic 2 2012",
//				"Healthy Eating", "Dieters Are Feeling Great!", 5);
//	}
//
//	@Test
//	public void testSubmitTextToEraterAndCheckJsonInDb11() throws Exception {
//		submitTextToErater("files/assayFiles/text11.txt", "Basic 2 2012",
//				"Getting Help", "Clean-House Agency", 5);
//	}
//
//	@Test
//	public void testSubmitTextToEraterAndCheckJsonInDb12() throws Exception {
//		submitTextToErater("files/assayFiles/text12.txt", "Basic 2 2012",
//				"Enjoy Your Meal!", "Sale at Shopright", 6);
//	}
//
//	@Test
//	public void testSubmitTextToEraterAndCheckJsonInDb13() throws Exception {
//		submitTextToErater("files/assayFiles/text13.txt", "Basic 2 2012",
//				"A Bad Day", "Grumble's Department Store", 6);
//	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

	public void submitTextToErater(String textFile, String courseName,
			String courseUnit, String unitComponent, int unitStage)
			throws Exception {
		report.report("Login to Edo");
		report.report("using file: " + textFile);
		EdoLoginPage edoLoginPage = new EdoLoginPage(webDriver,testResultService);
		edoLoginPage.OpenPage(getSutAndSubDomain());

		Student student = new Student();
		student.setUserName(configuration.getProperty("student.user.name"));
		student.setPassword(configuration.getProperty("student.user.password"));
		student.setId(dbService.getUserIdByUserName(student.getUserName(),autoInstitution.getInstitutionId()));

//		String userName = config.getProperty("student.user.name");
		// edoLoginPage.typeUserNameAndPass(userName,
		// config.getProperty("student.user.password"));
		// EdoHomePage edoHomePage = edoLoginPage.submitLogin();
		EdoHomePage edoHomePage = edoLoginPage.login(student);
		report.stopLevel();

		report.report("Open home page and start a writing drill");
		edoHomePage.waitForPageToLoad();
		edoHomePage.clickOnCourses();
		// String courseName = "Basic 3 2012";
		edoHomePage.clickOnCourseByName(courseName);
		edoHomePage.waitForCourseDetailsToBeDisplayed(courseName);
		edoHomePage.clickOnCourseUnit(courseUnit);
		edoHomePage.clickOntUnitComponent(unitComponent, "Practice");
		edoHomePage.ClickOnComponentsStage(unitStage);
		Thread.sleep(10000);
		edoHomePage.submitWritingAssignment(textFile, textService);
		// System.out.println("sleeping for 60 seconds");
		// Thread.sleep(60000);

		report.report("start checking the xml and json");
		String userId = dbService.getUserIdByUserName(student.getUserName(),autoInstitution.getInstitutionId());
		String textStart = textService.getFirstCharsFromCsv(10, textFile);
		String writingId = eraterService.getWritingIdByUserIdAndTextStart(
				userId, textStart);
		report.report("using file: " + textFile);
		eraterService.compareJsonAndXmlByWritingId(writingId);
		report.stopLevel();
	}

}
