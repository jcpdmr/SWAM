package com.swam.commons.mongodb;

import java.math.BigDecimal;

import org.bson.Document;
import org.oristool.eulero.modeling.stochastictime.DeterministicTime;
import org.oristool.eulero.modeling.stochastictime.ErlangTime;
import org.oristool.eulero.modeling.stochastictime.ExponentialTime;
import org.oristool.eulero.modeling.stochastictime.SIRIOType;
import org.oristool.eulero.modeling.stochastictime.StochasticTime;
import org.oristool.eulero.modeling.stochastictime.UniformTime;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;

@Component
@ReadingConverter
public class StochasticTimeReadConverter implements Converter<Document, StochasticTime> {

    @Override
    public StochasticTime convert(Document source) {
        SIRIOType sirioType = SIRIOType.valueOf(source.getString("type"));

        boolean containsK = source.containsKey("k");
        boolean containsRate = source.containsKey("rate");
        boolean containsValue = source.containsKey("value");
        if (sirioType.equals(SIRIOType.UNI)) {
            return new UniformTime(source.getDouble("EFT"), source.getDouble("LFT"));
        } else if (sirioType.equals(SIRIOType.DET) && containsValue) {
            return new DeterministicTime((new BigDecimal(source.getString("EFT"))));
        } else if (sirioType.equals(SIRIOType.EXP) && (!containsK) && (containsRate)) {
            return new ExponentialTime(new BigDecimal(source.getString("rate")));
        } else if (sirioType.equals(SIRIOType.EXPO) && (containsK) && (containsRate)) {
            return new ErlangTime(source.getInteger("k"), source.getDouble("rate"));
        } else {
            throw new IllegalArgumentException("Unsupported StochasticTime type: " + sirioType
                    + ". Is this type of StochasticTime handled by the ReadConverter or should it be added?");
        }
    }
}
