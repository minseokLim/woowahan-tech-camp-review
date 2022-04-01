package com.minseoklim.woowahantechcampreview.lotto.config;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class IntegerListConverter implements AttributeConverter<List<Integer>, String> {
    private static final String DELIMITER = ",";

    @Override
    public String convertToDatabaseColumn(final List<Integer> attribute) {
        return attribute.stream()
            .map(String::valueOf)
            .collect(Collectors.joining(DELIMITER));
    }

    @Override
    public List<Integer> convertToEntityAttribute(final String dbData) {
        return Arrays.stream(dbData.split(DELIMITER))
            .map(Integer::parseInt)
            .collect(Collectors.toUnmodifiableList());
    }
}
