package com.btcbit.note_transposer.service;

import com.btcbit.note_transposer.exception.NoteFileParsingException;
import com.btcbit.note_transposer.model.Note;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoteTransposerService {

    private final ObjectMapper objectMapper;

    private static final int TOTAL_NOTES_IN_OCTAVE = 12;
    private static final int MIN_NOTE_IN_MIN_OCTAVE = 10;
    private static final int MAX_NOTE_IN_MAX_OCTAVE = 1;
    private static final int MIN_OCTAVE = -3;
    private static final int MAX_OCTAVE = 5;

    public void processNotes(String inputFile, int semitone, String outputFile) {
        try {
            String content = Files.readString(Paths.get(inputFile));

            List<Note> receivedNotes = objectMapper.readValue(content, new TypeReference<>() {});
            List<Note> transposedNotes = transposeNotes(receivedNotes, semitone);

            log.info("Successfully transposed {} notes", transposedNotes.size());
            Files.write(Paths.get(outputFile), transposedNotes.toString().replace(" ", "").getBytes());
        } catch (IOException e) {
            log.error("Error processing notes: {}", e.getMessage());
            throw new NoteFileParsingException("Failed to process notes", e);
        }
    }

    private List<Note> transposeNotes(List<Note> notes, int semitone) {
        return notes.stream()
                .map(note -> transposeNote(semitone, note))
                .toList();
    }

    private Note transposeNote(int semitone, Note note) {
        int totalSemitones = note.getOctave() * TOTAL_NOTES_IN_OCTAVE + note.getNoteNumber() + semitone;
        int newOctave = totalSemitones / TOTAL_NOTES_IN_OCTAVE;
        int newNoteNumber = totalSemitones % TOTAL_NOTES_IN_OCTAVE;

        if (newNoteNumber <= 0) {
            newNoteNumber += TOTAL_NOTES_IN_OCTAVE;
            newOctave -= 1;
        }

        if (newOctave < MIN_OCTAVE || newOctave > MAX_OCTAVE ||
                (newOctave == MAX_OCTAVE && newNoteNumber > MAX_NOTE_IN_MAX_OCTAVE)
            || (newOctave == MIN_OCTAVE && newNoteNumber < MIN_NOTE_IN_MIN_OCTAVE)) {
            throw new IllegalArgumentException("Resulting note falls out of keyboard range: [%d,%d]".formatted(newOctave, newNoteNumber));
        }

        return new Note(newOctave, newNoteNumber);
    }
}
