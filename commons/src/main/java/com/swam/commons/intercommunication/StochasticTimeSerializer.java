package com.swam.commons.intercommunication;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

import org.oristool.eulero.modeling.stochastictime.DeterministicTime;
import org.oristool.eulero.modeling.stochastictime.ErlangTime;
import org.oristool.eulero.modeling.stochastictime.ExponentialTime;
import org.oristool.eulero.modeling.stochastictime.StochasticTime;

public class StochasticTimeSerializer extends JsonSerializer<StochasticTime> {

    @Override
    public void serialize(StochasticTime value, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {

        // Start JSON object
        gen.writeStartObject();

        // Serialize common fields of StochasticTime
        gen.writeStringField("type", value.getType().toString());
        gen.writeNumberField("eft", value.getEFT());
        gen.writeNumberField("lft", value.getLFT());

        // Serialize fields needed for specific classes
        if (value instanceof ErlangTime erlangTime) {
            gen.writeNumberField("k", erlangTime.getK());
            gen.writeNumberField("rate", erlangTime.getRate());
        }
        if (value instanceof ExponentialTime exponentialTime) {
            gen.writeNumberField("rate", exponentialTime.getRate());
        }
        if (value instanceof DeterministicTime deterministicTime) {
            gen.writeNumberField("value", deterministicTime.getEFT());
        }

        // Terminate JSON object
        gen.writeEndObject();
    }
}
