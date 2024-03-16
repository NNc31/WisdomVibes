package ua.edu.sumdu.nefodov.wisdomvibes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ua.edu.sumdu.nefodov.wisdomvibes.model.Quote;
import ua.edu.sumdu.nefodov.wisdomvibes.repository.QuoteRepository;

import java.util.List;
import java.util.Random;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api")
public class RestController {

    private final QuoteRepository quoteRepository;

    @Autowired
    public RestController(QuoteRepository quoteRepository) {
        this.quoteRepository = quoteRepository;
    }

    @GetMapping(value ="/random", produces = "application/json")
    public Quote randomQuote() {
        long quoteCnt = quoteRepository.count();
        if (quoteCnt > 0) {
            long randomLong = new Random().nextLong(quoteCnt + 1);
            return quoteRepository.findById(randomLong).orElse(null);
        } else {
            return null;
        }
    }

    @PostMapping("/add")
    public Quote addQuote(@RequestBody Quote newQuote) {
        return quoteRepository.save(newQuote);
    }

    @GetMapping(value = "/author/{author}", produces = "application/json")
    public List<Quote> findByAuthor(@PathVariable String author) {
        author = author.replace('_', ' ').strip();
        return quoteRepository.findByAuthor(author);
    }

    @GetMapping(value = "/authors", produces = "application/json")
    public List<String> findAllAuthors() {
        return quoteRepository.findDistinctAuthors();
    }
}
