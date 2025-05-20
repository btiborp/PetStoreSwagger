package com.example.sandbox.pet;

import com.example.sandbox.Common;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import utils.report.TestListener;

import java.util.Map;
import java.util.TreeMap;

import static com.example.sandbox.util.constans.Tags.SMOKE;


@Listeners(TestListener.class)
public class petMiscellaneousTest extends Common {

    @Test(enabled = true,groups = {SMOKE},description ="pet-extraParam")
    public void Test_Misc_qParam(){
        Map<String, String> queryParams = new TreeMap<>();
        queryParams.put("status","available");
        queryParams.put("asd","asd");
        queryParams.put("maki","kakadu");

        Response  response = getUrl(findByStatus, queryParams);
        Assert.assertEquals(response.getStatusCode(),200,"Invalid response code");
    }

    @Test(enabled = true,groups = {SMOKE},description ="pet-qParamAndHeader")
    public void Test_Header(){
        Map<String, String> queryParams = new TreeMap<>();
        queryParams.put("status","available");
        Map<String, String> headers = new TreeMap<>();
        headers.put("Mandatoyheader","BFG");

        Response  response = getUrl(findByStatus,headers,queryParams);
        Assert.assertEquals(response.getStatusCode(),200,"Invalid response code");

    }
}
