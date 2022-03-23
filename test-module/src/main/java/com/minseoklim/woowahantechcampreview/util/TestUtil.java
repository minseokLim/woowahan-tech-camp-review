package com.minseoklim.woowahantechcampreview.util;

import static org.assertj.core.api.Assertions.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class TestUtil {
    public static final String ADMIN_TOKEN =
        "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiYXV0aCI6IlJPTEVfQURNSU4sUk9MRV9VU0VSIiwidHlwIjoiQUNDRVNTIiwiaWF0IjoxNjQ4M"
            + "DMzODQ2LCJleHAiOjQ2MTE2ODc2NjY0NjEyMzR9.HLpEHGX1GPxt4E7UpndSuOrYtKxVR0PsSCv-DxnzfL-PeqbHfIMcHtybm8D44LWh"
            + "z4KY-5WWBjfB0UttiZn75Q";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String CLASS_PATH = "src/test/resources/";

    private TestUtil() {
    }

    public static void assertHttpStatusCode(final ExtractableResponse<Response> response, final int httpStatusCode) {
        assertThat(response.statusCode()).isEqualTo(httpStatusCode);
    }

    public static <T> T extractResponse(final ExtractableResponse<Response> response, final Class<T> clazz) {
        return response.as(clazz);
    }

    public static void writeFile(final String filePath, final String text) throws IOException {
        try (final var writer = new BufferedWriter(new FileWriter(CLASS_PATH + filePath))) {
            writer.write(text);
        }
    }

    public static <T> T readJsonFile(final String filePath, final Class<T> clazz) throws IOException {
        final var content = readFile(filePath);
        return OBJECT_MAPPER.readValue(content, clazz);
    }

    public static String readFile(final String filePath) throws IOException {
        try (final var br = new BufferedReader(new FileReader(CLASS_PATH + filePath))) {
            return readAllLines(br);
        }
    }

    private static String readAllLines(final BufferedReader br) throws IOException {
        final var result = new StringBuilder();

        String line;
        while ((line = br.readLine()) != null) {
            result.append(line);
        }

        return result.toString();
    }
}
