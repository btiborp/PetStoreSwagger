package com.example.sandbox.util.listener;

import org.testng.ITestListener;
import org.testng.ITestResult;

import com.example.sandbox.util.reporting.ReportingFilter;

public class TestLogger implements ITestListener {

    private final ReportingFilter logger = new ReportingFilter();

    @Override
    public void onTestStart(ITestResult result) {
        logger.logStart(result);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        logger.logEnd(result);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        logger.logEnd(result);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        logger.logEnd(result);
    }
}
