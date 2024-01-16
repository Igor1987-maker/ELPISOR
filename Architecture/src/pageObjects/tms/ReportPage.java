package pageObjects.tms;

import Enums.ByTypes;
import drivers.GenericWebDriver;
import pageObjects.GenericPage;
import services.TestResultService;

public class ReportPage extends GenericPage {

	public ReportPage(GenericWebDriver webDriver,
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

	public String getSammaryPackageName() throws Exception {
		String packageNameFromSammary = webDriver
				.waitForElement(
						"//table[@id='tblLicUsageSummeryReportGrid']//tbody//tr[2]//td[2]",
						ByTypes.xpath).getText();
		return packageNameFromSammary;

	}
	
	public String getNumberOfStudentsInClass() throws Exception{
		String studentsNumber = webDriver
				.waitForElement(
						"//table[@id='tblLicUsageSummeryReportGrid']//tbody//tr[2]//td[3]",
						ByTypes.xpath).getText();
		return studentsNumber;
	}

	public String getSammaryClassName() throws Exception {
		String classNameFromSammary = webDriver
				.waitForElement(
						"//table[@id='tblLicUsageSummeryReportGrid']//tbody//tr[2]//td[1]",
						ByTypes.xpath).getText();
		return classNameFromSammary;
	}

	public String getStduentNameFromLicenseReport(String studentName)
			throws Exception {
//		index = index + 1;
		// String text = webDriver.waitForElement(
		// "//table[@id='tblLicUsageDetailsReportGrid']//tbody//tr["+ index +
		// "]//td[1]", ByTypes.xpath).getText();
		String text = webDriver.waitForElement(
				"//td[contains(.,'" + studentName + "')]", ByTypes.xpath)
				.getText();
		return text;

	}

}
