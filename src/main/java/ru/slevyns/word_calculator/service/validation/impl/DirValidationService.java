package ru.slevyns.word_calculator.service.validation.impl;

import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import ru.slevyns.word_calculator.dto.DirRequest;
import ru.slevyns.word_calculator.dto.ValidationResult;
import ru.slevyns.word_calculator.service.validation.ValidationService;

import java.util.HashSet;
import java.util.Set;

import static ru.slevyns.word_calculator.util.DirValidationMeta.*;

@Service
public class DirValidationService implements ValidationService<DirRequest> {
    @Override
    public Set<ValidationResult> validate(DirRequest request) {
        var result = new HashSet<ValidationResult>();
        validateDir(result, request.dirPath());
        validateMinWordLength(result, request.minLength());
        validateTopN(result, request.topN());

        return result;
    }

    private void validateDir(Set<ValidationResult> result, String dirPath) {
        if (Strings.isBlank(dirPath)) {
            result.add(new ValidationResult(DIR_PATH_ERROR, DIR_PATH_PARAM));
        }
    }

    private void validateMinWordLength(Set<ValidationResult> result, int minWordLength) {
        if (minWordLength <= 0) {
            result.add(new ValidationResult(
                    MUST_BE_GREATER_THAN_ERROR.formatted(MIN_WORD_PARAM),
                    MIN_WORD_PARAM)
            );
        }
    }

    private void validateTopN(Set<ValidationResult> result, int topN) {
        if (topN <= 0) {
            result.add(new ValidationResult(
                    MUST_BE_GREATER_THAN_ERROR.formatted(TOP_N_PARAM),
                    TOP_N_PARAM)
            );
        }
    }
}
