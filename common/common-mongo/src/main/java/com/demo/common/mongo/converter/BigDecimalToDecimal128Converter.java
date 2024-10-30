package com.demo.common.mongo.converter;

import lombok.NoArgsConstructor;
import org.bson.types.Decimal128;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

import java.math.BigDecimal;

@WritingConverter
@NoArgsConstructor
public class BigDecimalToDecimal128Converter implements Converter<BigDecimal, Decimal128> {

    public Decimal128 convert(BigDecimal bigDecimal) {
        return bigDecimal == null ? null : new Decimal128(bigDecimal);
    }
}
