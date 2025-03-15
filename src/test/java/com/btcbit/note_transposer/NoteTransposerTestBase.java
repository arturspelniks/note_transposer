package com.btcbit.note_transposer;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Slf4j
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NoteTransposerTestBase {

    @AfterAll
    void cleanupOutputFolder() throws IOException {
        Path outputFolder = Paths.get("src/test/resources/testdata/output");
        try (Stream<Path> paths = Files.walk(outputFolder)) {
            paths.filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .forEach(file -> {
                        if (!file.delete()) {
                            log.warn("Failed to delete {}: ", file);
                        }
                    });
        }
    }
}
