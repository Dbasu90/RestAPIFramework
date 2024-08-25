package com.demo.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Book {

    @JsonProperty("id")
    private int bookId;

    @JsonProperty("name")
    private String bookName;

    private String author;

    private String isbn;

    private String type;

    private Double price;

    @JsonProperty("current-stock")
    private int stock;

    private boolean available;
}
