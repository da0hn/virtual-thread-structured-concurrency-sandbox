package dev.da0hn.structured.concurrency.api.bestprice.bookstore;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.StructuredTaskScope;

@Service
public class BookRetrieverService {

    private final BookStoreProperties bookStoreProperties;

    private final RestClient restClient = RestClient.create();

    public BookRetrieverService(
        BookStoreProperties bookStoreProperties
    ) {
        this.bookStoreProperties = bookStoreProperties;
    }

    public List<Book> getBookFromAllStores(final String bookName) {
        try (var scope = new StructuredTaskScope<Book>()) {
            List<StructuredTaskScope.Subtask<Book>> tasks = new ArrayList<>();
            tasks.add(scope.fork(() -> this.getBook(this.bookStoreProperties.wonderApiUrl(), bookName)));
            tasks.add(scope.fork(() -> this.getBook(this.bookStoreProperties.mascotApiUrl(), bookName)));

            scope.join();

            tasks.stream()
                .filter(t -> t.state() == StructuredTaskScope.Subtask.State.FAILED)
                .map(StructuredTaskScope.Subtask::exception)
                .forEach(Throwable::printStackTrace);

            return tasks.stream()
                .filter(t -> t.state() == StructuredTaskScope.Subtask.State.SUCCESS)
                .map(StructuredTaskScope.Subtask::get)
                .toList();
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private Book getBook(String url, String bookName) {
        return restClient.get()
            .uri(STR."\{url}/bookstore/books", p -> p.queryParam("name", bookName).build())
            .retrieve()
            .body(Book.class);
    }

}
