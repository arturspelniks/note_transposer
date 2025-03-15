package com.btcbit.note_transposer.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.btcbit.note_transposer.NoteTransposerTestBase;
import com.btcbit.note_transposer.exception.NoteFileParsingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

class NoteTransposerServiceTests extends NoteTransposerTestBase {

    private static final String INPUT_FILE = "src/test/resources/testdata/inputFile.json";
    private static final String OUTPUT_FILE = "src/test/resources/testdata/output/outputFile.json";

    @Autowired
    private NoteTransposerService noteTransposerService;

    @MockitoSpyBean
    private ObjectMapper objectMapper;

    @Test
    void processNotesWithValidInput() throws IOException {
        // given
        String expectedContent = "[[1,10],[2,3],[1,10],[2,5],[1,10],[2,6],[1,10],[2,3],[1,10],[2,5],[1,10],[2,6],[1,10],[2,8],[1,10],[2,5],[1,10],[2,6],[1,10],[2,8],[1,10],[2,10],[1,10],[2,6],[1,10],[2,8],[1,10],[2,10],[1,10],[2,11],[1,10],[2,8],[1,10],[2,10],[1,10],[2,6],[1,10],[2,8],[1,10],[2,5],[1,10],[2,6],[1,10],[2,3],[1,10],[2,5],[1,10],[2,2],[1,10],[2,3],[1,10],[1,10],[1,10],[1,11],[1,10],[1,8],[1,10],[1,10],[1,10],[1,6],[1,10],[1,8],[1,10],[1,5],[1,10],[1,6],[1,10],[1,3],[1,10],[1,8],[1,10],[1,5],[1,10],[1,6],[1,10],[1,3],[1,10],[1,5],[1,10],[1,2],[1,10],[1,3]]";

        // when
        noteTransposerService.processNotes(INPUT_FILE, -3, OUTPUT_FILE);

        // then
        String actualContent = Files.readString(Paths.get(OUTPUT_FILE));
        assertEquals(expectedContent, actualContent);
    }

    private static Stream<Arguments> shouldThrowNoteParsingExceptionForInvalidInputFile() {
        // given
        return Stream.of(
                Arguments.of("invalid/path/inputFile.json"),
                Arguments.of("src/test/resources/testdata/emptyInputFile.json"),
                Arguments.of("src/test/resources/testdata/invalidInputFile.json")
        );
    }

    @MethodSource
    @ParameterizedTest
    void shouldThrowNoteParsingExceptionForInvalidInputFile(String inputFile) {
        // when - then
        assertThrows(NoteFileParsingException.class, () -> noteTransposerService.processNotes(inputFile, -3, OUTPUT_FILE));
    }

    private static Stream<Arguments> shouldThrowIllegalArgumentExceptionWhenNoteNotFound() {
        // given
        return Stream.of(
                Arguments.of(INPUT_FILE, 100, "Resulting note falls out of keyboard range: [10,5]"),
                Arguments.of("src/test/resources/testdata/invalidInputFileFirstOctave.json", -1, "Resulting note falls out of keyboard range: [-3,9]"),
                Arguments.of("src/test/resources/testdata/invalidInputFileLastOctave.json", 1, "Resulting note falls out of keyboard range: [5,2]")
        );
    }

    @MethodSource
    @ParameterizedTest
    void shouldThrowIllegalArgumentExceptionWhenNoteNotFound(String inputFile, int semitone, String expectedErrorMessage) {
        // when - then
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> noteTransposerService.processNotes(inputFile, semitone, OUTPUT_FILE));
        assertEquals(expectedErrorMessage, exception.getMessage());
    }
}