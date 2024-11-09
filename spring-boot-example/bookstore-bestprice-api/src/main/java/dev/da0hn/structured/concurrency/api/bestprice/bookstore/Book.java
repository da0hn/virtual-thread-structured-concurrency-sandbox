package dev.da0hn.structured.concurrency.api.bestprice.bookstore;

public record Book(
    String bookStore,
    String bookName,
    String author,
    Integer cost,
    Integer numPages,
    String link
) {
}
