package com.demo.listeners;

import com.demo.annotations.ReportAnnotation;
import com.demo.reports.ExtentLogger;
import com.demo.reports.ExtentReport;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener, ISuiteListener {

    @Override
    public void onStart(ISuite suite) {
        ExtentReport.initReports();
    }
    @Override
    public void onFinish(ISuite suite) {
        ExtentReport.tearDownReports();
    }

    @Override
    public void onTestStart(ITestResult result) {
        ExtentReport.createTest(result.getName());

        String[] authors = result.getMethod().getConstructorOrMethod().getMethod().getAnnotation(ReportAnnotation.class).author();

        ExtentReport.addAuthor(authors);

        String[] category = result.getMethod().getConstructorOrMethod().getMethod().getAnnotation(ReportAnnotation.class).category();
        ExtentReport.addCategory(category);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentLogger.pass(result.getName()+" is passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentLogger.fail(String.valueOf(result.getThrowable()));
    }
}
