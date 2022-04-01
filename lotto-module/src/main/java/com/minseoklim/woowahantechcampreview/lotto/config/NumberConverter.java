package com.minseoklim.woowahantechcampreview.lotto.config;

import javax.persistence.AttributeConverter;

import com.minseoklim.woowahantechcampreview.lotto.domain.Number;

public class NumberConverter implements AttributeConverter<Number, Integer> {

    @Override
    public Integer convertToDatabaseColumn(final Number attribute) {
        if (attribute != null) {
            return attribute.getValue();
        }
        return null;
    }

    @Override
    public Number convertToEntityAttribute(final Integer dbData) {
        if (dbData != null) {
            return Number.of(dbData);
        }
        return null;
    }
}
