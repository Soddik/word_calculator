package ru.slevyns.word_calculator.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.slevyns.word_calculator.dto.DirRequest;
import ru.slevyns.word_calculator.service.words.WordCalcService;

@Controller
@RequestMapping("directory/find")
public class DirController {
    private final WordCalcService wordCalcService;

    public DirController(WordCalcService wordCalcService) {
        this.wordCalcService = wordCalcService;
    }

    @GetMapping("words")
    public String findWords() {
        return "directory/find/words";
    }

    @PostMapping("/words")
    public String findWords(Model model, @ModelAttribute("request") DirRequest request) {
        model.addAttribute("dirRequest", request);

        var response = wordCalcService.countWords(request);
        var errors = response.errors();
        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            return "/directory/find/validation_error";
        }

        model.addAttribute("result", response);
        return "/directory/find/result";
    }
}
