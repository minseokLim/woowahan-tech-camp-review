package com.minseoklim.woowahantechcampreview.util;

import static org.assertj.core.api.Assertions.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class TestUtil {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private TestUtil() {
    }

    public static void assertHttpStatus(final ExtractableResponse<Response> response, final HttpStatus httpStatus) {
        assertThat(response.statusCode()).isEqualTo(httpStatus.value());
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> extractResponseAsMap(final ExtractableResponse<Response> response) {
        return response.as(Map.class);
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> extractJsonFileAsMap(final String filePath) {
        try {
            final var content = readFile(filePath);
            return OBJECT_MAPPER.createParser(content).readValueAs(Map.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String readFile(final String filePath) {
        try {
            final var br = getBufferedReader(filePath);
            return readAllLines(br);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static BufferedReader getBufferedReader(final String filePath) throws IOException {
        final var in = new ClassPathResource(filePath).getInputStream();
        return new BufferedReader(new InputStreamReader(in));
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
