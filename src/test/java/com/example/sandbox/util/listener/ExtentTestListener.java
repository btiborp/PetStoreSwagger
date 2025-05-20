package com.example.sandbox.util.listener;

import com.aventstack.extentreports.Status;
import com.example.sandbox.util.reporting.ExtentManager;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class ExtentTestListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getTestClass().getName() + "#" + result.getMethod().getMethodName();
        ExtentManager.createTest(testName)
                .log(Status.INFO, "=== START TEST ===");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentManager.getTest().log(Status.PASS, "Test passed.");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentManager.getTest().log(Status.FAIL, "Test failed: " + result.getThrowable());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentManager.getTest().log(Status.SKIP, "Test skipped.");
    }

    @Override
    public void onFinish(ITestContext context) {
        ExtentManager.flush(); // report fájl véglegesítése
    }
}
