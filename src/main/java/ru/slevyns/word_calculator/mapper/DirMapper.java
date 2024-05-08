package ru.slevyns.word_calculator.mapper;

import org.springframework.stereotype.Service;
import ru.slevyns.word_calculator.dto.DirRequest;
import ru.slevyns.word_calculator.dto.DirResponse;
import ru.slevyns.word_calculator.dto.ValidationResult;
import ru.slevyns.word_calculator.dto.Word;

import java.nio.file.Path;
import java.util.Set;

/**
 * Mapper for directory operations task
 */
@Service
public class DirMapper {
    public Path toDirectoryPath(DirRequest request) {
        return Path.of(request.dirPath());
    }

    public DirResponse toResponse(Set<Word> words, Set<ValidationResult> validationResult) {
        return new DirResponse(words, validationResult);
    }
}