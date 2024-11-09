package dev.da0hn.structured.concurrency.api.bookstore;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@Profile("WONDER")
public class WonderBookCollection implements BookCollection {

    private final String storeName;

    private List<Book> books;

    public WonderBookCollection(
        @Value("${book.store.name}") String storeName
    ) {
        this.storeName = storeName;
    }

    @Override
    public Book findBook(String name) {
        return this.books.stream()
            .filter(book -> book.bookName().equals(name))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Book not found"));
    }

    @PostConstruct
    public void initialize() {
        this.books = List.of(
            new Book(this.storeName, "And Then There Were None", "Agatha Christie", 9, 272, bookUrl("And Then There Were None")),
            new Book(this.storeName, "The Hobbit", "J.R.R. Tolkien", 12, 310, bookUrl("The Hobbit")),
            new Book(this.storeName, "Dream of the Red Chamber", "Cao Xueqin", 23, 800, bookUrl("Dream of the Red Chamber")),
            new Book(this.storeName, "The Lion, the Witch and the Wardrobe", "C.S. Lewis", 15, 208, bookUrl("The Lion, the Witch and the Wardrobe")),
            new Book(this.storeName, "She: A History of Adventure", "H. Rider Haggard", 10, 320, bookUrl("She: A History of Adventure")),
            new Book(this.storeName, "The Da Vinci Code", "Dan Brown", 17, 689, bookUrl("The Da Vinci Code")),
            new Book(this.storeName, "The Catcher in the Rye", "J.D. Salinger", 12, 234, bookUrl("The Catcher in the Rye")),
            new Book(this.storeName, "The Alchemist", "Paulo Coelho", 15, 208, bookUrl("The Alchemist"))
        );
    }

    private String bookUrl(String title) {
        return "http://mascot:8081/store/book?name=" + URLEncoder.encode(title, StandardCharsets.UTF_8);
    }


}
