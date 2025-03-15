package com.btcbit.note_transposer.deserializer;

import com.btcbit.note_transposer.model.Note;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class NoteDeserializer extends JsonDeserializer<Note> {

    @Override
    public Note deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        int[] values = p.readValueAs(int[].class);
        return new Note(values[0], values[1]);
    }
}
