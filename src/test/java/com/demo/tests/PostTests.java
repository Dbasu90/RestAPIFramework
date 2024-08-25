package com.demo.tests;

import com.demo.annotations.ReportAnnotation;
import com.demo.builder.RequestBuilder;
import com.demo.constants.FrameworkConstants;
import com.demo.enums.PropertiesType;
import com.demo.reports.ExtentLogger;
import com.demo.utils.FileUtils;
import com.demo.utils.PropertyUtils;
import com.demo.utils.RandomUtils;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class PostTests {

    public String getAccessToken() throws IOException {
        String reqBody = FileUtils.readJsonFileAndReturnString(FrameworkConstants.getRequestJsonFolderpath()+"TokenGeneration.json")
                .replace("{Name}", RandomUtils.getFullName())
                .replace("{Email}", RandomUtils.getFirstName() + "_" +RandomUtils.getId()+ "@test.com");

        Response response = RequestBuilder
                .buildRequestForPostCalls()
                .body(reqBody)
                .post("/api-clients/");

        return response.jsonPath().getString("accessToken");
    }

    @Test
    @ReportAnnotation(author = {"Debasmita"}, category ={"regression","practice"})
    public void orderBook() throws IOException {
        String reqBody = FileUtils.readJsonFileAndReturnString(FrameworkConstants.getRequestJsonFolderpath()+"RequestBody.json")
                .replace("{Id}", PropertyUtils.getvalue(PropertiesType.BOOKID))
                .replace("{Name}", RandomUtils.getFullName());

        RequestSpecification specification = RequestBuilder
                .buildRequestForPostCalls()
                .header("Authorization", "Bearer " + getAccessToken())
                .body(reqBody);
        ExtentLogger.logRequest(specification);

        Response response= specification
                .post("/orders");
        response.prettyPrint();

        ExtentLogger.logResponse(response.asPrettyString());

        assertThat(response.getStatusCode()).isEqualTo(201);
        assertThat(response.jsonPath().getString("created")).asBoolean().isTrue();
    }
}
