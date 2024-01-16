package pageObjects.edo;



import Enums.ByTypes;
import drivers.GenericWebDriver;
import pageObjects.GenericPage;
import services.TestResultService;

public class WidgetObject extends GenericPage {

	public WidgetObject(GenericWebDriver webDriver,
			TestResultService testResultService) throws Exception {
		super(webDriver, testResultService);
		// TODO Auto-generated constructor stub
	}
	
	public void clickOnWidget() throws Exception{
		webDriver.waitForElement("aaa", ByTypes.xpath);
		
	}
	
	public String getButtonText() throws Exception{
	String text=	webDriver.waitForElement("aaa", ByTypes.xpath).getText();
	return text;
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

}
