package Stepsdefinition;

import Utilities.ExtentManager;
import Utilities.ExtentTestManager;
import Utilities.PdfReportUtil;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

public class Hooks {

	private ExtentReports extent;
	private ExtentTest test;

	@Before
	public void setUp(Scenario scenario) {
		extent = ExtentManager.getInstance();
		test = extent.createTest(scenario.getName());
		ExtentTestManager.setTest(test);
	}

	@After
	public void tearDown(Scenario scenario) {
		if (scenario.isFailed()) {
			ExtentTestManager.getTest().fail("Scenario failed");
		} else {
			ExtentTestManager.getTest().pass("Scenario passed");
		}
		extent.flush();
		// Convert the HTML report to PDF after the test execution
		PdfReportUtil.convertHtmlToPdf("target/extent-reports/extent-report.html", "target/extent-reports/extent-report.pdf");
	}
}