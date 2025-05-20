package com.example.sandbox.util.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentManager {
    private static final ExtentReports extent;
    private static final ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    static {
        ExtentSparkReporter reporter = new ExtentSparkReporter("target/extent-report.html");
        extent = new ExtentReports();
        extent.attachReporter(reporter);
    }

    public static ExtentReports getExtent() {
        return extent;
    }

    public static ExtentTest createTest(String name) {
        ExtentTest extentTest = extent.createTest(name);
        test.set(extentTest);
        return extentTest;
    }

    public static ExtentTest getTest() {
        return test.get();
    }

    public static void flush() {
        extent.flush();
    }
}
