package com.btcbit.note_transposer.model;

import com.btcbit.note_transposer.deserializer.NoteDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@JsonDeserialize(using = NoteDeserializer.class)
public class Note {

    private int octave;
    private int noteNumber;

    @Override
    public String toString() {
        return "[%s,%s]".formatted(octave, noteNumber);
    }
}
