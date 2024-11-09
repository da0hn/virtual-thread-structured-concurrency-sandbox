package dev.da0hn.structured.concurrency.api.bestprice.bookstore;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;

@RestController
@RequestMapping("/virtual-store")
public class BestPriceBookController {

    public static final ScopedValue<RestCallStatistics> TIME_MAP = ScopedValue.newInstance();

    private final BookRetrieverService bookRetrieverService;

    public BestPriceBookController(BookRetrieverService bookRetrieverService) { this.bookRetrieverService = bookRetrieverService; }

    @GetMapping("/book")
    public BestPriceResult getBestPrice(
        @RequestParam("name") String name
    ) throws Exception {
        final var restCallStatistics = new RestCallStatistics();

        final var initialTime = Instant.now();
        final var books = ScopedValue.callWhere(
            TIME_MAP,
            restCallStatistics,
            () -> bookRetrieverService.getBookFromAllStores(name)
        );

        final var bestPriceBook = books.stream()
            .min(Comparator.comparing(Book::cost))
            .orElseThrow();
        ChronoUnit.MILLIS.between(initialTime, Instant.now());
        restCallStatistics.addTiming("virtual-store", ChronoUnit.MILLIS.between(initialTime, Instant.now()));

        return new BestPriceResult(bestPriceBook, books, restCallStatistics);
    }

}
