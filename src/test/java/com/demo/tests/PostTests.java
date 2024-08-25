package com.demo.tests;

import com.demo.builder.RequestBuilder;
import com.demo.constants.FrameworkConstants;
import com.demo.enums.PropertiesType;
import com.demo.utils.FileUtils;
import com.demo.utils.PropertyUtils;
import com.demo.utils.RandomUtils;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class PostTests {

    @Test
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
    public void orderBook() throws IOException {
        String reqBody = FileUtils.readJsonFileAndReturnString(FrameworkConstants.getRequestJsonFolderpath()+"RequestBody.json")
                .replace("{Id}", PropertyUtils.getvalue(PropertiesType.BOOKID))
                .replace("{Name}", RandomUtils.getFullName());

        Response response = RequestBuilder
                .buildRequestForPostCalls()
                .header("Authorization", "Bearer " + getAccessToken() )
                .body(reqBody)
                .post("/orders");

        response.prettyPrint();

        assertThat(response.getStatusCode()).isEqualTo(201);
        assertThat(response.jsonPath().getString("created")).asBoolean().isTrue();
    }
}
