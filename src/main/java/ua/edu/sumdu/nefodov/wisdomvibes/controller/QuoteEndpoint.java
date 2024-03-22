package ua.edu.sumdu.nefodov.wisdomvibes.controller;

import jakarta.xml.bind.JAXBElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import ua.edu.sumdu.nefodov.wisdomvibes.jaxb.*;
import ua.edu.sumdu.nefodov.wisdomvibes.model.Quote;
import ua.edu.sumdu.nefodov.wisdomvibes.repository.QuoteRepository;

import java.util.List;
import java.util.Random;

@Endpoint
public class QuoteEndpoint {
    private static final String NAMESPACE_URI = "http://www.wisdomvibes.com/quotes";

    private final QuoteRepository quoteRepository;

    @Autowired
    public QuoteEndpoint(QuoteRepository quoteRepository) {
        this.quoteRepository = quoteRepository;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "randomQuoteRequest")
    @ResponsePayload
    public RandomQuoteResponse randomQuote() {
        RandomQuoteResponse randomQuoteResponse = new RandomQuoteResponse();
        QuoteType response = new QuoteType();
        long quoteCnt = quoteRepository.count();
        if (quoteCnt > 0) {
            long randomLong = new Random().nextLong(quoteCnt + 1);
            Quote quote = quoteRepository.findById(randomLong).orElse(null);
            response.setId(quote.getId());
            response.setText(quote.getText());
            response.setAuthor(quote.getAuthor());
            randomQuoteResponse.setQuote(response);
            return randomQuoteResponse;
        } else {
            return null;
        }
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "addQuoteRequest")
    @ResponsePayload
    public JAXBElement<QuoteType> addQuote(@RequestPayload JAXBElement<AddQuoteRequest> addQuoteRequestJAXBElement) {
        AddQuoteRequest addQuoteRequest = addQuoteRequestJAXBElement.getValue();
        Quote quote = new Quote();
        quote.setText(addQuoteRequest.getText());
        quote.setAuthor(addQuoteRequest.getAuthor());
        quote = quoteRepository.save(quote);
        QuoteType response = new QuoteType();
        response.setId(quote.getId());
        response.setText(quote.getText());
        response.setAuthor(quote.getAuthor());
        ObjectFactory objectFactory = new ObjectFactory();
        return objectFactory.createAddQuoteResponse(response);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "findAllAuthorsRequest")
    @ResponsePayload
    public FindAllAuthorsResponse findAllAuthors() {
        FindAllAuthorsResponse findAllAuthorsResponse = new FindAllAuthorsResponse();
        findAllAuthorsResponse.getAuthor().addAll(quoteRepository.findDistinctAuthors());
        return findAllAuthorsResponse;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "findByAuthorRequest")
    @ResponsePayload
    public JAXBElement<QuoteListType> findByAuthor(@RequestPayload JAXBElement<FindByAuthorRequest> findByAuthorRequestJAXBElement) {
        FindByAuthorRequest findByAuthorRequest = findByAuthorRequestJAXBElement.getValue();
        String author = findByAuthorRequest.getAuthor();
        List<Quote> quotes = quoteRepository.findByAuthor(author);
        QuoteListType quoteListType = new QuoteListType();

        for (Quote quote : quotes) {
            QuoteType quoteType = new QuoteType();
            quoteType.setId(quote.getId());
            quoteType.setText(quote.getText());
            quoteType.setAuthor(quote.getAuthor());
            quoteListType.getQuote().add(quoteType);
        }
        ObjectFactory objectFactory = new ObjectFactory();
        return objectFactory.createFindByAuthorResponse(quoteListType);
    }
}