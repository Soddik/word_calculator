package ru.slevyns.word_calculator.service.matcher.impl;

import org.springframework.stereotype.Service;
import ru.slevyns.word_calculator.dto.Word;
import ru.slevyns.word_calculator.service.matcher.TextRegexMatcherService;

import java.util.HashMap;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class TextRegexMatcherImpl implements TextRegexMatcherService {
    private static final String DEFAULT_REGEX_TEMPLATE = "[A-Za-zА-Яа-я]{%d,}";
    private int minLength = DEFAULT_MIN_WORD_LENGTH;
    private Pattern pattern = Pattern.compile(String.format(DEFAULT_REGEX_TEMPLATE, minLength), Pattern.UNICODE_CHARACTER_CLASS);

    public TextRegexMatcherImpl() {
    }

    public TextRegexMatcherImpl(int minLength) {
        setMinLength(minLength);
    }

    @Override
    public Set<Word> findMatches(String text) {
        var matcher = pattern.matcher(text.toLowerCase());
        var matches = new HashMap<String, Integer>();
        while (matcher.find()) {
            matches.compute(
                    matcher.group(),
                    (k, v) -> v == null
                            ? 1
                            : v + 1
            );
        }

        return matches.entrySet().stream()
                .map(e -> new Word(e.getKey(), e.getValue()))
                .collect(Collectors.toUnmodifiableSet());
    }

    public void setMinLength(int newMinLength) {
        if (minLength != newMinLength) {
            this.minLength = newMinLength;
            recompilePattern(newMinLength);
        }
    }

    private void recompilePattern(int newMaxLength) {
        pattern = Pattern.compile(String.format(DEFAULT_REGEX_TEMPLATE, newMaxLength));
    }
}

