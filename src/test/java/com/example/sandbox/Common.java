package com.example.sandbox;

import io.restassured.response.Response;
import org.springframework.boot.test.context.SpringBootTest;
import org.testng.ITestContext;
import org.testng.annotations.BeforeMethod;

import utils.report.ReportingFilter;
import utils.report.Reporter;

import java.lang.reflect.Method;
import java.util.Map;

import static io.restassured.RestAssured.given;

@SpringBootTest
public class Common extends Endpoints {

    protected ReportingFilter filter;

    @BeforeMethod(alwaysRun = true)
    public void baseBeforeMethod(ITestContext context, Method method) {
        String testName = method != null ? method.getName() : "UnnamedTest";
        context.getCurrentXmlTest().setName(testName);
        filter = new ReportingFilter(context);


        String className = method.getDeclaringClass().getSimpleName();
        String methodName = method.getName();
        String description = "";

        org.testng.annotations.Test testAnnotation = method.getAnnotation(org.testng.annotations.Test.class);
        if (testAnnotation != null) {
            description = testAnnotation.description();
        }
        System.out.println(">>> className: " + className);
        System.out.println(">>> methodName: " + methodName);
        System.out.println(">>> description: " + description);

        Reporter reporter = (Reporter) context.getAttribute(Reporter.REPORTER);
        if (reporter != null) {
            reporter.makeLeft(description, className, methodName);
        }

    }

        //----------------------------------GET----------------------------------
    public Response getUrl(String endpoint){

        return 
            given()
                .relaxedHTTPSValidation()
                .and()
                .filter(filter)
            .when()
                .get(baseUrl+endpoint)
            .then()
                .extract().response();

    }
    public Response getUrl(String endpoint, Map<String, String> queryParam ){

        return 
            given()
                .relaxedHTTPSValidation()
                .headers("correlationId","testCorrelid")
                .cookie("session_id", "abc123")
                .param("param","testParam")
                .formParam("asd","testFormParams")
                .queryParams(queryParam)
                .and()
                .filter(filter)
            .when()
                .get(baseUrl+endpoint)
            .then()
                .extract().response();

    }

    public Response getUrl(String endpoint,Map<String, String> headers,Map<String, String> queryParam ){

        return 
            given()
                .relaxedHTTPSValidation()
                .params(queryParam)
                .headers(headers)
                .and()
                .filter(filter)
            .when()
                .get(baseUrl+endpoint)
            .then()
                .extract().response();

    }

    //----------------------------------POST----------------------------------
    public Response postUrl(String endpoint,String body){

        return 
            given()
                .relaxedHTTPSValidation()
                .contentType("application/json; charset=UTF-8")
                .body(body)
                .and()
                .filter(filter)
            .when()
                .post(baseUrl+endpoint)
            .then()
                .extract().response();

    }

    //----------------------------------PUT----------------------------------
    public Response putUrl(String endpoint,String body){

        return 
            given()
                .relaxedHTTPSValidation()
                .contentType("application/json; charset=UTF-8")
                .body(body)
                .and()
                .filter(filter)
            .when()
                .put(baseUrl+endpoint)
            .then()
                .extract().response();

    }

    //----------------------------------DELETE----------------------------------
    public Response deleteUrl(String endpoint){

        return 
            given()
                .relaxedHTTPSValidation()
                .and()
                .filter(filter)
            .when()
                .delete(baseUrl+endpoint)
            .then()
                .extract().response();
    }

}

