package ru.slevyns.word_calculator;

import org.junit.jupiter.api.Test;
import ru.slevyns.word_calculator.dto.DirRequest;
import ru.slevyns.word_calculator.dto.DirResponse;
import ru.slevyns.word_calculator.dto.ValidationResult;
import ru.slevyns.word_calculator.dto.Word;
import ru.slevyns.word_calculator.mapper.DirMapper;

import java.nio.file.Path;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.slevyns.word_calculator.DirTestMeta.*;

class DirMapperTest {
    private final DirMapper mapper = new DirMapper();

    @Test
    void toDirectoryPath() {
        var request = new DirRequest(CONTROLLER_TEST_DIR_PATH, MIN_LENGTH, TOP_NUM);
        var directoryPath = mapper.toDirectoryPath(request);
        assertEquals(Path.of(CONTROLLER_TEST_DIR_PATH), directoryPath);
    }

    @Test
    void toResponse() {
        var words = new HashSet<Word>();
        var validationResult = new HashSet<ValidationResult>();
        var response = mapper.toResponse(words, validationResult);
        var expected = new DirResponse(words, validationResult);
        assertEquals(expected, response);
    }
}