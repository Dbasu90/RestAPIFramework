package com.demo.tests;

import com.demo.annotations.ReportAnnotation;
import com.demo.builder.RequestBuilder;
import com.demo.constants.FrameworkConstants;
import com.demo.enums.PropertiesType;
import com.demo.pojo.Book;
import com.demo.reports.ExtentLogger;
import com.demo.utils.FileUtils;
import com.demo.utils.PropertyUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class GetTests {

    @Test
    @ReportAnnotation
    public void getApiStatus(){
        Response response = RequestBuilder
                .buildRequestForGetCalls()
                .get("/status");

        response.prettyPrint();

        assertThat(response.getStatusCode()).isEqualTo(200);

    }

    @Test
    @ReportAnnotation(author = {"Debasmita"}, category ={"regression","practice"})
    public void getListOfBooks() throws IOException {
       Response response = RequestBuilder
               .buildRequestForGetCalls()
               .queryParam("type","fiction")
               .queryParam("limit",2)
               .get("/books");
        response.prettyPrint();

       ExtentLogger.logResponse(response.asPrettyString());

       assertThat(response.getStatusCode()).isEqualTo(200);
       FileUtils.storeResponseToOutputFile(FrameworkConstants.getResponseJsonFolderPath()+"response.json",response);

       ObjectMapper mapper = new ObjectMapper();
       List<Book> books = mapper.readValue(new File(FrameworkConstants.getResponseJsonFolderPath()+"response.json"), new TypeReference<>() {
       });
       Optional<Book> availableBook = books.stream().filter(Book::isAvailable).findFirst();
       assertThat(availableBook.get().getType()).isEqualTo("fiction");
    }
    
    @Test
    @ReportAnnotation(author = {"Debasmita"}, category ={"regression"})
    public void getSpecificBook(){
        int id =Integer.parseInt(PropertyUtils.getvalue(PropertiesType.BOOKID));
        Response response= RequestBuilder
                .buildRequestForGetCalls()
                .pathParam("id", id)
                .get("/books/{id}");
        response.prettyPrint();

        ExtentLogger.logResponse(response.asPrettyString());

        response.then().body(JsonSchemaValidator.matchesJsonSchema(new File(FrameworkConstants.getJsonSchemaFolderpath()+"schema.json")));
        assertThat(response.getStatusCode()).isEqualTo(200);
        assertThat(response.jsonPath().getString("current-stock")).asInt().isGreaterThan(0);

    
    }
}
