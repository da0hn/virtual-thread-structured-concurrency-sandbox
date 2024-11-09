package dev.da0hn.structured.concurrency.api.bestprice.bookstore;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ws-client.bookstore")
public record BookStoreProperties(
    String wonderApiUrl,
    String mascotApiUrl
) {

}
