package com.demo.builder;

import com.demo.enums.PropertiesType;
import com.demo.utils.PropertyUtils;
import io.restassured.config.LogConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class RequestBuilder {

    public static RequestSpecification buildRequestForGetCalls(){
        return given()
                .baseUri(PropertyUtils.getvalue(PropertiesType.BASEURL))
                .config(RestAssuredConfig.config().logConfig(LogConfig.logConfig().blacklistHeader("Authorization","Content-Type")))
                .log()
                .all();

    }

    public static RequestSpecification buildRequestForPostCalls(){
        return given()
                .baseUri("https://simple-books-api.glitch.me")
                .config(RestAssuredConfig.config().logConfig(LogConfig.logConfig().blacklistHeader("Authorization","Content-Type")))
                .contentType(ContentType.JSON)
                .log()
                .all();

    }

}
