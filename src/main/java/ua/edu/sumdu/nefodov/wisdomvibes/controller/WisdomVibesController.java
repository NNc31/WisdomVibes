package ua.edu.sumdu.nefodov.wisdomvibes.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/quotes")
public class WisdomVibesController {

    @GetMapping("/home")
    public String index(Model model) {

        return "index";
    }

    @GetMapping("/random")
    public String randomQuote(Model model) {

        return "random-quote";
    }

    @GetMapping("/add")
    public String addQuote(Model model) {
        return "add-quote";
    }

    @GetMapping("/author/{}")
    public String findAuthor(Model model) {

        return "author";
    }
}
