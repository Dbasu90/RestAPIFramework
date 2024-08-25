package com.demo.utils;

import io.restassured.response.Response;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public final class FileUtils {

    private FileUtils(){}

    public static void storeResponseToOutputFile(String filepath, Response response) throws IOException {
        Files.write(Paths.get(filepath),response.asByteArray());
    }

    public static String readJsonFileAndReturnString(String filepath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filepath)));
    }
}
