package com.tenPines.configuration;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class JsonDateSerializer extends JsonSerializer<LocalDate> {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T03:00:00.000Z'");

    @Override
    public void serialize(LocalDate date, JsonGenerator generator,
                          SerializerProvider provider) throws IOException {

        String dateString = date.format(formatter);
        generator.writeString(dateString);
    }
}
