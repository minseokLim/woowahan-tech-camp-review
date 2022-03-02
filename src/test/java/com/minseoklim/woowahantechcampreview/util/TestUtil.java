package com.minseoklim.woowahantechcampreview.util;

import static org.assertj.core.api.Assertions.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class TestUtil {
    public static final String ADMIN_TOKEN =
        "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJwcmluY2lwYWwiLCJhdXRoIjoiUk9MRV9BRE1JTixST0xFX1VTRVIiLCJpYXQiOjE2NDU0OTY1MzAsI"
            + "mV4cCI6NDYxMTY4NzY2MzkyMzkxOH0.GeK1OPaTVHm2RP-VrDV-Nfbk3ytwN5JFJ-Rudt48Cdwoq-8sm_0fDrrxMNTZpE7oO_5KzJW8G"
            + "E7Y_BctGK0JOA";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String CLASS_PATH = "src/test/resources/";

    private TestUtil() {
    }

    public static void assertHttpStatus(final ExtractableResponse<Response> response, final HttpStatus httpStatus) {
        assertThat(response.statusCode()).isEqualTo(httpStatus.value());
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
