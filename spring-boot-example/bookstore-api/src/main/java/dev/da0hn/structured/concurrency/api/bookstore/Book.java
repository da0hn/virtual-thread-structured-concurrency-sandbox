package dev.da0hn.structured.concurrency.api.bookstore;

import java.math.BigDecimal;

public record Book(
    String bookStore,
    String bookName,
    String author,
    Integer cost,
    Integer numPages,
    String link
) {
}
