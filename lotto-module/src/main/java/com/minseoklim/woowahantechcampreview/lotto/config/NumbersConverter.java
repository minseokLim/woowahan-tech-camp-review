package com.minseoklim.woowahantechcampreview.lotto.config;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.minseoklim.woowahantechcampreview.lotto.domain.Numbers;

@Converter
public class NumbersConverter implements AttributeConverter<Numbers, String> {
    private static final String DELIMITER = ",";

    @Override
    public String convertToDatabaseColumn(final Numbers attribute) {
        if (attribute != null) {
            return attribute.getValues().stream()
                .map(String::valueOf)
                .collect(Collectors.joining(DELIMITER));
        }
        return null;
    }

    @Override
    public Numbers convertToEntityAttribute(final String dbData) {
        if (dbData != null) {
            final List<Integer> numbers = Arrays.stream(dbData.split(DELIMITER))
                .map(Integer::parseInt)
                .collect(Collectors.toUnmodifiableList());
            return new Numbers(numbers);
        }
        return null;
    }
}
