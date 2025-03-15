package com.btcbit.note_transposer;

import com.btcbit.note_transposer.service.NoteTransposerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
@RequiredArgsConstructor
public class NoteTransposerApplication implements CommandLineRunner {

	private final NoteTransposerService noteTransposerService;

	public static void main(String[] args) {
		SpringApplication.run(NoteTransposerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		if (args.length != 3) {
			log.error("Wrong number of arguments");
			log.error("Usage: java -jar note_transposer.jar <inputFile> <semitone> <outputFile>");
			return;
		}
		String inputFile = args[0];
		int semitone = Integer.parseInt(args[1]);
		String outputFile = args[2];

		noteTransposerService.processNotes(inputFile, semitone, outputFile);
	}
}
