package Utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentManager {
    private static ExtentReports extent;

    public static ExtentReports getInstance() {
        if (extent == null) {
            createInstance("target/extent-reports/extent-report.html");
        }
        return extent;
    }

    // Create an instance of ExtentReports and configure the report file
    public static ExtentReports createInstance(String fileName) {
        ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(fileName);
        htmlReporter.config().setTheme(Theme.STANDARD);
        htmlReporter.config().setDocumentTitle("API Test Report");
        htmlReporter.config().setEncoding("UTF-8");
        htmlReporter.config().setReportName("API Test Report");

        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
        return extent;
    }
}
