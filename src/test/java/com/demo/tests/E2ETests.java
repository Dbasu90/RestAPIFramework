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
    public void bookOrderE2E() throws IOException {

        String accessToken = getAccessToken();

        String customer = RandomUtils.getFullName();

        String reqBody = FileUtils.readJsonFileAndReturnString(FrameworkConstants.getRequestJsonFolderpath()+"RequestBody.json")
                .replace("{Id}", PropertyUtils.getvalue(PropertiesType.BOOKID))
                .replace("{Name}",customer );

        Response postResponse = RequestBuilder
                .buildRequestForPostCalls()
                .header("Authorization", "Bearer " + accessToken)
                .body(reqBody)
                .post("/orders");

        postResponse.prettyPrint();
        assertThat(postResponse.getStatusCode()).isEqualTo(201);

        String orderId = postResponse.jsonPath().getString("orderId");

        Response getResponse = getSpecificOrderResponse(orderId,accessToken);
        assertThat(getResponse.jsonPath().getString("customerName")).isEqualTo(customer);

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

        Response getUpdatedResponse = getSpecificOrderResponse(orderId,accessToken);
        assertThat(getUpdatedResponse.jsonPath().getString("customerName")).isEqualTo("Garnet Schule");

        Response deleteResponse = RequestBuilder
                .buildRequestForGetCalls()
                .header("Authorization", "Bearer " + accessToken)
                .pathParam("orderId",orderId)
                .delete("/orders/{orderId}");

        assertThat(deleteResponse.getStatusCode()).isEqualTo(204);

        Response afterDeleteResponse = getSpecificOrderResponse(orderId,accessToken);
        assertThat(afterDeleteResponse.getStatusCode()).isEqualTo(404);
        assertThat(afterDeleteResponse.jsonPath().getString("error")).isEqualTo("No order with id "+orderId);

    }

    private Response getSpecificOrderResponse(String orderId, String accessToken) {
        Response response = RequestBuilder
                .buildRequestForGetCalls()
                .header("Authorization", "Bearer " + accessToken)
                .pathParam("orderId",orderId)
                .get("/orders/{orderId}");

        response.prettyPrint();
        return response;
    }
}
