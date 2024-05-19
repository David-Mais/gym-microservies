package com.davidmaisuradze.gymapplication.util;

import com.davidmaisuradze.gymapplication.exception.GymException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class CustomBooleanDeserializer extends StdDeserializer<Boolean> {

    public CustomBooleanDeserializer() {
        this(null);
    }

    public CustomBooleanDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Boolean deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String value = jsonParser.getText().trim().toLowerCase();

        if (!value.equals("true") && !value.equals("false")) {
            throw new GymException("Json parse error. Could not parse Boolean: " + value, "400");
        }
        return Boolean.parseBoolean(value);
    }
}
