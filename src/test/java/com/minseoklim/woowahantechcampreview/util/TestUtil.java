package com.minseoklim.woowahantechcampreview.util;

import static org.assertj.core.api.Assertions.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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

    public static <T> T extractResponse(final ExtractableResponse<Response> response, final Class<T> clazz) {
        return response.as(clazz);
    }

    public static <T> T readJsonFile(final String filePath, final Class<T> clazz) {
        try {
            final var content = readFile(filePath);
            return OBJECT_MAPPER.readValue(content, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String readFile(final String filePath) throws IOException {
        final var br = getBufferedReader(filePath);
        return readAllLines(br);
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
