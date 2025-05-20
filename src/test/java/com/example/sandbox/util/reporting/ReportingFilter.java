package com.example.sandbox.util.reporting;

import org.testng.ITestResult;

public class ReportingFilter {

    public void logStart(ITestResult result) {
        String testClass = result.getTestClass().getName();
        String testMethod = result.getMethod().getMethodName();

        System.out.println("=== START TEST: " + testClass + "#" + testMethod + " ===");
    }

    public void logEnd(ITestResult result) {
        String status;
        switch (result.getStatus()) {
            case ITestResult.SUCCESS -> status = "PASSED";
            case ITestResult.FAILURE -> status = "FAILED";
            case ITestResult.SKIP -> status = "SKIPPED";
            default -> status = "UNKNOWN";
        }

        System.out.println("--- END TEST: " + result.getMethod().getMethodName() +
                " → STATUS: " + status + " ---");
    }
}
