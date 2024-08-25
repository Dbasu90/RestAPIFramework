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

public class E2ETests {

    private String getAccessToken() throws IOException {
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
    public void bookOrderE2E() throws IOException {

        String accessToken = getAccessToken();

        String customer = RandomUtils.getFullName();

        //POST an order
        String reqBody = FileUtils.readJsonFileAndReturnString(FrameworkConstants.getRequestJsonFolderpath()+"RequestBody.json")
                .replace("{Id}", PropertyUtils.getvalue(PropertiesType.BOOKID))
                .replace("{Name}",customer );

        RequestSpecification specification = RequestBuilder
                .buildRequestForPostCalls()
                .header("Authorization", "Bearer " + accessToken)
                .body(reqBody);

        ExtentLogger.logRequest(specification);

        Response postResponse = specification
                .post("/orders");
        postResponse.prettyPrint();

        ExtentLogger.logResponse(postResponse.asPrettyString());

        assertThat(postResponse.getStatusCode()).isEqualTo(201);
        String orderId = postResponse.jsonPath().getString("orderId");

        //GET the order
        Response getResponse = getSpecificOrderResponse(orderId,accessToken);
        assertThat(getResponse.jsonPath().getString("customerName")).isEqualTo(customer);

        //UPDATE the order
        String patchBody = "{\n" +
                "  \"customerName\": \"Garnet Schule\"\n" +
                "}";

        Response patchResponse = RequestBuilder
                .buildRequestForPostCalls()
                .header("Authorization", "Bearer " + accessToken)
                .pathParam("orderId",orderId)
                .body(patchBody)
                .patch("/orders/{orderId}");

        assertThat(patchResponse.getStatusCode()).isEqualTo(204);

        //GET after Order Update
        Response getUpdatedResponse = getSpecificOrderResponse(orderId,accessToken);
        assertThat(getUpdatedResponse.jsonPath().getString("customerName")).isEqualTo("Garnet Schule");

        //DELETE the order
        Response deleteResponse = RequestBuilder
                .buildRequestForGetCalls()
                .header("Authorization", "Bearer " + accessToken)
                .pathParam("orderId",orderId)
                .delete("/orders/{orderId}");

        assertThat(deleteResponse.getStatusCode()).isEqualTo(204);

        //GET after Order Deletion
        Response afterDeleteResponse = getSpecificOrderResponse(orderId,accessToken);
        assertThat(afterDeleteResponse.getStatusCode()).isEqualTo(404);
        assertThat(afterDeleteResponse.jsonPath().getString("error")).isEqualTo("No order with id "+orderId);

    }

    private Response getSpecificOrderResponse(String orderId, String accessToken) {
        RequestSpecification specification = RequestBuilder
                .buildRequestForGetCalls()
                .header("Authorization", "Bearer " + accessToken)
                .pathParam("orderId",orderId);

        ExtentLogger.logRequest(specification);

        Response response = specification
                .get("/orders/{orderId}");

        ExtentLogger.logResponse(response.asPrettyString());

        response.prettyPrint();
        return response;
    }
}
