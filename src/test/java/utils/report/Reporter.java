package utils.report;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.CodeLanguage;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.observer.ExtentObserver;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.BeforeSuite;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Reporter {
    public static String REPORTER = "reporter";
    private final ExtentReports extent = new ExtentReports();
    private ExtentTest left;
    private ExtentTest right;
    public static LoggerWrapper loggerWrapper;
    private final ThreadLocal<Integer> stepCounter = new ThreadLocal();

    @BeforeSuite
    public void initReporter(ITestContext context) {
        Reporter reporter = new Reporter();
        context.setAttribute(Reporter.REPORTER, reporter);
    }

    public Reporter() {
        ExtentSparkReporter spark = new ExtentSparkReporter("report/" + getTimeStamp() + "/report.html");
        spark.config().setDocumentTitle("Erste API Chapter");
        this.extent.attachReporter(new ExtentObserver[]{spark});
    }

    private static String getTimeStamp() {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd-HHmmss");
        return LocalDateTime.now().format(dateFormat);
    }

    private void addLeftNodeWithDescription(String className, String description, String methodName, String... categories) {
        this.left = this.extent.createTest(className, description);
        if (categories.length > 0) {
            this.left.assignCategory(categories);
        }

        this.addRightNode(methodName);
        this.extent.flush();
    }

    private void addRightNode(String rightName) {
        this.right = this.left.createNode(rightName);
        loggerWrapper = new LoggerWrapper(this.right, this.stepCounter);
        this.extent.flush();
    }

    public void log(String requestParams, String requestBody, String responseParams, String responseBody) {
        if (this.left == null && this.right == null) {
            this.addLeftNodeWithDescription("Before steps", "Before steps", "Before steps");
        }

        this.right.log(Status.INFO, replaceNewLineCharsToHTMLLineBreaks(requestParams));
        this.logJSON(requestBody);
        this.right.log(Status.INFO, replaceNewLineCharsToHTMLLineBreaks(responseParams));
        this.logJSON(responseBody);
    }

    private static String replaceNewLineCharsToHTMLLineBreaks(String requestOrResponseParams) {
        return requestOrResponseParams.replaceAll("\n", "<br>");
    }

    private void logJSON(String json) {
        if (!json.equals("none")) {
            Markup m = MarkupHelper.createCodeBlock(json, CodeLanguage.JSON);
            this.right.log(Status.INFO, m);
        }
    }

    public void makeLeft(String description, String className, String methodName, String... categories) {
        this.addLeftNodeWithDescription(description, className, methodName, categories);
    }

    public void addResult(ITestResult result) {
        switch (result.getStatus()) {
            case 2:
                this.left.fail("❗❗❗  ERROR:  " + result.getThrowable().getMessage());
                this.right.fail(result.getThrowable().getMessage());
                break;
            case 3:
                this.extent.removeTest(this.right);
                this.left.skip("⚠️ SKIPPED:  " + result.getThrowable().getMessage());
                break;
            default:
                this.left.pass("✔️  Test passed!");
        }

        this.extent.flush();
    }
}