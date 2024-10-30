package com.demo.common.mongo.converter;

import lombok.NoArgsConstructor;
import org.bson.types.Decimal128;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import java.math.BigDecimal;

@ReadingConverter
@NoArgsConstructor
public class Decimal128ToBigDecimalConverter implements Converter<Decimal128, BigDecimal> {

    public BigDecimal convert(Decimal128 decimal128) {
        return decimal128 == null ? null : decimal128.bigDecimalValue();
    }

}
