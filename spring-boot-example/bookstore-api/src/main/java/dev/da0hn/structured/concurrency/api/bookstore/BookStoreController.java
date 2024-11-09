package dev.da0hn.structured.concurrency.api.bookstore;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/bookstore")
public class BookStoreController {

    private final BookCollection bookCollection;

    public BookStoreController(BookCollection bookCollection) { this.bookCollection = bookCollection; }

    @GetMapping("/books")
    public Book getBooksByName(@RequestParam("name") String name) throws InterruptedException {
        final var book = this.bookCollection.findBook(name);
        TimeUnit.SECONDS.sleep(5);
        return book;
    }

}
