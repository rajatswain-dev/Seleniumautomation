package com.amazon.listeners;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import io.qameta.allure.Allure;

import lombok.extern.slf4j.Slf4j;

import com.amazon.config.Config;
import com.amazon.driver.DriverFactory;

@Slf4j
public class SuiteListener implements ISuiteListener, ITestListener, IInvokedMethodListener {


    @Override
    public void onStart(ISuite suite) {

    	Config.initConfig(suite.getXmlSuite().getAllParameters());
        Config.printTestConfigValues();
       log.info("Suite initialized successfully.");
    }

    @Override
    public void onFinish(ISuite suite) {
         log.info("Suite finished.");
    }

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult result) {
        log.info("{} - {}", result.getInstanceName(), result.getMethod().getMethodName());
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult result) {

        if (result.getStatus() == ITestResult.FAILURE || result.getStatus() == ITestResult.SKIP) {
            attachScreenshot(result);
        }

        log.info("{} - {}", result.getInstanceName(), result.getMethod().getMethodName());
    }

    private void attachScreenshot(ITestResult result) {
        try {
        	WebDriver driver = DriverFactory.getDriver(); // use existing driver

            if (driver == null) {
                log.warn("WebDriver is null. Cannot take screenshot.");
                return;
            }

            String base64Screenshot = ((TakesScreenshot) driver)
                    .getScreenshotAs(OutputType.BASE64);

            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

            String testName = result.getMethod().getMethodName();

            Allure.addAttachment(
                    testName + "_" + timestamp,
                    "image/png",
                    new java.io.ByteArrayInputStream(
                            java.util.Base64.getDecoder().decode(base64Screenshot)
                    ),
                    ".png"
            );

            log.info("Screenshot attached to Allure report.");

        } catch (Exception e) {
            log.error("Failed to capture screenshot", e);
        }
    }

    @Override
    public void onTestStart(ITestResult result) {
        log.info("{} - {} Test Started",
                result.getInstanceName(),
                result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        log.info("{} - {} Test Passed",
                result.getInstanceName(),
                result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        log.info("{} - {} Test Failed",
                result.getInstanceName(),
                result.getMethod().getMethodName());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        log.info("{} - {} Test Skipped",
                result.getInstanceName(),
                result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        log.info("{} - {} Failed but within success percentage",
                result.getInstanceName(),
                result.getMethod().getMethodName());
    }

    @Override
    public void onStart(ITestContext context) {
        log.info("{} - Execution Started", context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        log.info("{} - Execution Finished", context.getName());
    }
}
