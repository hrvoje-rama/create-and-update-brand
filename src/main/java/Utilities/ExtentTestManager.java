package Utilities;

import com.aventstack.extentreports.ExtentTest;
public class ExtentTestManager {
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    public static ExtentTest getTest() {
        return test.get();  // Retrieve the test instance for the current thread
    }

    public static void setTest(ExtentTest extentTest) {
        test.set(extentTest);  // Set the test instance for the current thread
    }
}