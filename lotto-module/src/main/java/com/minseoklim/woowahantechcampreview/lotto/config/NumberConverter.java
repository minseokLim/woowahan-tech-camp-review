package com.minseoklim.woowahantechcampreview.lotto.config;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.minseoklim.woowahantechcampreview.lotto.domain.Number;

@Converter
public class NumberConverter implements AttributeConverter<List<Number>, String> {
    private static final String DELIMITER = ",";

    @Override
    public String convertToDatabaseColumn(final List<Number> attribute) {
        return attribute.stream()
            .map(String::valueOf)
            .collect(Collectors.joining(DELIMITER));
    }

    @Override
    public List<Number> convertToEntityAttribute(final String dbData) {
        return Arrays.stream(dbData.split(DELIMITER))
            .map(it -> new Number(Integer.parseInt(it)))
            .collect(Collectors.toUnmodifiableList());
    }
}
