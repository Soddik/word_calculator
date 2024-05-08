package ru.slevyns.word_calculator;

import org.junit.jupiter.api.Test;
import ru.slevyns.word_calculator.dto.DirRequest;
import ru.slevyns.word_calculator.dto.ValidationResult;
import ru.slevyns.word_calculator.service.validation.ValidationService;
import ru.slevyns.word_calculator.service.validation.impl.DirValidationService;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.slevyns.word_calculator.DirTestMeta.*;
import static ru.slevyns.word_calculator.util.DirValidationMeta.*;

class DirValidationServiceTest {
    private final ValidationService<DirRequest> validationService = new DirValidationService();

    @Test
    void validateRequest_AllRequestParamsInvalid_ReturnAllErrors() {
        var request = new DirRequest("", -1, -1);
        var dirError = new ValidationResult(DIR_PATH_ERROR, DIR_PATH_PARAM);
        var minWordError = new ValidationResult(MUST_BE_GREATER_THAN_ERROR.formatted(MIN_WORD_PARAM), MIN_WORD_PARAM);
        var topNError = new ValidationResult(MUST_BE_GREATER_THAN_ERROR.formatted(TOP_N_PARAM), TOP_N_PARAM);

        var result = validationService.validate(request);

        assertEquals(3, result.size());
        assertTrue(result.containsAll(Set.of(dirError, minWordError, topNError)));
    }

    @Test
    void validateRequest_AllRequestParamsValid_ReturnZeroErrors() {
        var request = new DirRequest(CONTROLLER_TEST_DIR_PATH, MIN_LENGTH, TOP_NUM);

        var result = validationService.validate(request);

        assertTrue(result.isEmpty());
    }
}