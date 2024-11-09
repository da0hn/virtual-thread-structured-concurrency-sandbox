package dev.da0hn.structured.concurrency.api.bestprice.bookstore;

import java.util.List;

public record BestPriceResult(
    Book bookPriceDeal,
    List<Book> bookPrices
) {
}
