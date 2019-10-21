package books.library.boklibrary;

import books.library.boklibrary.domain.Book;
import books.library.boklibrary.domain.BookRepository;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
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
public class CreateTests {

    @Autowired
    private BookRepository bookRepository;


    @Test
    public void AddBookTest() {
        try {
            int ilePrzed = bookRepository.findAllByTitle("Testowy tytul").size();
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            String body = "{ \"title\": \"Testowy tytul\", \"year\": 2019, \"authors\": [ { \"name\": \"Wioletta\", \"surname\": \"Willas\" } ] }";
            HttpPost request = new HttpPost("http://localhost:8080/books/");
            StringEntity params = new StringEntity(body);
            request.setEntity(params);
            request.addHeader("content-type", "application/json");
            HttpResponse result = httpClient.execute(request);
            assertEquals(ilePrzed + 1,bookRepository.findAllByTitle("Testowy tytul").size());
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

}
