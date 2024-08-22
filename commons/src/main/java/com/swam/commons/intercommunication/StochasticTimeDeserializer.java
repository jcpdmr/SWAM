package com.swam.commons.intercommunication;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.math.BigDecimal;

import org.oristool.eulero.modeling.stochastictime.DeterministicTime;
import org.oristool.eulero.modeling.stochastictime.ErlangTime;
import org.oristool.eulero.modeling.stochastictime.ExponentialTime;
import org.oristool.eulero.modeling.stochastictime.SIRIOType;
import org.oristool.eulero.modeling.stochastictime.StochasticTime;
import org.oristool.eulero.modeling.stochastictime.UniformTime;

public class StochasticTimeDeserializer extends JsonDeserializer<StochasticTime> {

    @Override
    public StochasticTime deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {

        // Read JSON Tree
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        JsonNode rootNode = mapper.readTree(jp);

        // The "type" field contains information about the concrete class type
        SIRIOType sirioType = SIRIOType.valueOf(rootNode.get("type").asText());

        // Istanciate sub-class based on "type" field
        if (sirioType.equals(SIRIOType.UNI)) {
            return new UniformTime(rootNode.get("eft").asDouble(), rootNode.get("lft").asDouble());
        } else if (sirioType.equals(SIRIOType.DET)) {
            return new DeterministicTime(BigDecimal.valueOf(rootNode.get("value").asDouble()));
        } else if (sirioType.equals(SIRIOType.EXP)) {
            return new ExponentialTime(BigDecimal.valueOf(rootNode.get("rate").asDouble()));
        } else if (sirioType.equals(SIRIOType.EXPO)) {
            return new ErlangTime(rootNode.get("k").asInt(), rootNode.get("rate").asDouble());
        } else {
            throw new IllegalArgumentException("Unknown StochasticTime type: " + sirioType
                    + ". Is this type of StochasticTime handled by the deserializer or should it be added?");
        }
    }
}
