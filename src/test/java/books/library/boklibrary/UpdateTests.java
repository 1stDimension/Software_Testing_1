package books.library.boklibrary;

import books.library.boklibrary.domain.Book;
import books.library.boklibrary.domain.BookRepository;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
public class UpdateTests {

    @Autowired
    private BookRepository bookRepository;

    private static void allowMethods(String... methods) {
        try {
            Field methodsField = HttpURLConnection.class.getDeclaredField("methods");

            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(methodsField, methodsField.getModifiers() & ~Modifier.FINAL);

            methodsField.setAccessible(true);

            String[] oldMethods = (String[]) methodsField.get(null);
            Set<String> methodsSet = new LinkedHashSet<>(Arrays.asList(oldMethods));
            methodsSet.addAll(Arrays.asList(methods));
            String[] newMethods = methodsSet.toArray(new String[0]);

            methodsField.set(null/*static field*/, newMethods);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    @Test
    public void RentBookTest() {
        try {
            allowMethods("PATCH");
            URL url = new URL("http://localhost:8080/books/1/rent");
            String encoding = Base64.getEncoder().encodeToString(("andrzej:qwerty").getBytes());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PATCH");
            connection.setRequestProperty  ("Authorization", "Basic " + encoding);
            int status = connection.getResponseCode();
            Book book = bookRepository.findById((long)1).get();
            assertTrue(book.isRented());
            assertEquals(200, connection.getResponseCode());
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }


    @Test
    public void ReturnBookTest() {
        try {
            allowMethods("PATCH");
            URL url = new URL("http://localhost:8080/books/1/return");
            String encoding = Base64.getEncoder().encodeToString(("andrzej:qwerty").getBytes());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PATCH");
            connection.setRequestProperty  ("Authorization", "Basic " + encoding);
            int status = connection.getResponseCode();
            Book book = bookRepository.findById((long)1).get();
            assertFalse(book.isRented());
            assertEquals(200, connection.getResponseCode());
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

}
