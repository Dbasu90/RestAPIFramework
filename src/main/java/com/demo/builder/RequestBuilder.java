package com.demo.builder;

import com.demo.enums.PropertiesType;
import com.demo.utils.PropertyUtils;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class RequestBuilder {

    public static RequestSpecification buildRequestForGetCalls(){
        return given()
                .baseUri(PropertyUtils.getvalue(PropertiesType.BASEURL))
                .log()
                .all();

    }

    public static RequestSpecification buildRequestForPostCalls(){
        return given()
                .baseUri("https://simple-books-api.glitch.me")
                .contentType(ContentType.JSON)
                .log()
                .all();

    }

}
