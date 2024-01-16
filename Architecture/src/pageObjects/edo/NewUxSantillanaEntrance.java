package pageObjects.edo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import Enums.ByTypes;
import drivers.GenericWebDriver;
import org.openqa.selenium.support.FindBy;
import pageObjects.GenericPage;
import pageObjects.tms.TmsHomePage;
import services.TestResultService;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;


public class NewUxSantillanaEntrance extends GenericPage {

	public static String userName;
	private String newPath;
	public NewUxSantillanaEntrance(GenericWebDriver webDriver,
								   TestResultService testResultService) throws Exception {
		super(webDriver, testResultService);
		// TODO Auto-generated constructor stub
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

	public void clickOnEnterED() throws Exception {
		WebElement element = null;

		for (int i = 0; i <= 30 && element == null; i++) {
			element = webDriver.waitForElement("//div[contains(@class,'firstBTN')]", ByTypes.xpath, false, 1);
		}
		element.click();
	}

	public void clickOnEnterToefl() throws Exception {
		webDriver.waitForElement("//div[contains(@class,'secondBTN')]", ByTypes.xpath).click();

	}

	public void clickOnEnterToeflSantillana() throws Exception {
		webDriver.waitForElement("//div[contains(@class,'BTNWrapper firstBTN ng-scope')]", ByTypes.xpath).click();

	}

	public void clickOnEnterProfessionalDevelopment() throws Exception {
		webDriver.waitForElement("//div[contains(@class,'thirdBTN')]", ByTypes.xpath).click();

	}

	public TmsHomePage clickOnEnterEdAsTeacher() throws Exception {

		webDriver.waitForElement("//div[contains(@class,'firstBTN')]", ByTypes.xpath).click();
		//webDriver.waitForJqueryToFinish();

		return new TmsHomePage(webDriver, testResultService);

	}

	public void verifyToeflEnterBtnIsNotDisplayed() throws Exception {
		WebElement element = webDriver.waitForElement("//div[contains(@class,'secondBTN')]", ByTypes.xpath, false, 3);
		if (element != null) testResultService.addFailTest("Toefl btn displayed though it should not");

	}



	public String generateUniqueId(int length) {
		if (length <= 0) {
			throw new IllegalArgumentException("");
		}

		UUID uuid = UUID.randomUUID();
		String uuidString = uuid.toString();
		int remainingLength = length - uuidString.length();

		if (remainingLength <= 0) {
			return uuidString.substring(0, length);
		} else {
			StringBuilder uniqueId = new StringBuilder(uuidString);
			for (int i = 0; i < remainingLength; i++) {
				char randomChar = (char) ('a' + Math.random() * ('z' - 'a' + 1));
				uniqueId.append(randomChar);
			}

			uniqueId.insert(8, '-');
			uniqueId.insert(13, '-');
			uniqueId.insert(18, '-');
			uniqueId.insert(23, '-');

			return uniqueId.toString();
		}
	}


	public String readLoginFileContentSanta(String filePath) {
		String content = "";
		try {

			Path path = Paths.get(filePath);

			content = new String(Files.readAllBytes(path));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return content;
	}


	public void saveFileAndOpenInBrowser(String buildPathForExternalPages, String modifiedContent) throws Exception {

		String filePath =  buildPathForExternalPages + "Languages";
		byte[] contentBytes = modifiedContent.getBytes();
		newPath = filePath+"\\" + testName + ".html";

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try (DataOutputStream out = new DataOutputStream(baos)) {
			out.write(contentBytes);
		}

		byte[] bytes = baos.toByteArray();
		File file = new File(newPath);
		try (FileOutputStream output = new FileOutputStream(file)) {
			output.write(bytes);
		}

		webDriver.openUrl(file.toString());

	}

	public String getUserNameText(){
		return webDriver.getWebDriver().findElement(By.cssSelector(".home__userName.ng-binding")).getText();
	}
	@FindBy(css = "button[type='submit']")
	public WebElement sendPostRequest;

	public void clickSendPostRequest() {
		webDriver.getWebDriver().findElement(By.cssSelector("button[type='submit']")).click();
		//webDriver.ClickElement(sendPostRequest);
	}

	public String getInvalidUserNameText(){
		return  webDriver.getWebDriver().findElement(By.tagName("body")).getText();
	}

}
