package com.btcbit.note_transposer;

import com.btcbit.note_transposer.service.NoteTransposerService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class NoteTransposerApplicationTests extends NoteTransposerTestBase {

	private static final String INPUT_FILE = "src/test/resources/testdata/inputFile.json";

	@Autowired
	private NoteTransposerApplication noteTransposerApplication;

	@MockitoSpyBean
	private NoteTransposerService noteTransposerService;

	@Test
	@SneakyThrows
	void shouldGenerateOutputFileWithCorrectValues() {
		// given
		String inputFile = INPUT_FILE;
		String outputFile = "src/test/resources/testdata/output/outputFile.json";
		String[] args = {inputFile, "-3", outputFile};

		// when
		noteTransposerApplication.run(args);

		// then
		verify(noteTransposerService, times(1))
				.processNotes(inputFile, -3, outputFile);
	}

	@Test
	void shouldNotExecuteNoteTranspose() throws Exception {
		// given
		String[] args = {"inputFile.json", "2"};

		// when
		noteTransposerApplication.run(args);

		// then
		verify(noteTransposerService, times(0))
				.processNotes(anyString(), anyInt(), anyString());
	}

	@Test
	void shouldThrowNumberFormatExceptionError() {
		// given
		String[] args = {"inputFile.json", "invalid", "outputFile.json"};

		// when - then
		assertThrows(NumberFormatException.class, () -> noteTransposerApplication.run(args));
	}

}
