package dev.da0hn.structured.concurrency.api.bestprice.bookstore;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;

@RestController
@RequestMapping("/virtual-store")
public class BestPriceBookController {

    private final BookRetrieverService bookRetrieverService;

    public BestPriceBookController(BookRetrieverService bookRetrieverService) { this.bookRetrieverService = bookRetrieverService; }

    @GetMapping("/book")
    public BestPriceResult getBestPrice(
        @RequestParam("name") String name
    ) {
        final var books = bookRetrieverService.getBookFromAllStores(name);

        final var bestPriceBook = books.stream()
            .min(Comparator.comparing(Book::cost))
            .orElseThrow();

        return new BestPriceResult(bestPriceBook, books);
    }

}
