package books.library.boklibrary;

import books.library.boklibrary.domain.Book;
import books.library.boklibrary.domain.BookRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Base64;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DeleteTests {

    @Autowired
    private BookRepository bookRepository;


    @Test
    public void DeleteBookTest() {
        try {
            Book book = bookRepository.findById((long)1).get();
            assertNotNull(book);
            URL url = new URL("http://localhost:8080/books/1/");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("DELETE");
            int status = connection.getResponseCode();
            assertFalse(bookRepository.findById((long)1).isPresent());
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

}
