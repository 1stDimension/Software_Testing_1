package books.library.boklibrary;

//import lombok.var;

import books.library.boklibrary.domain.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.Base64;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BoklibraryApplicationTests {

    @Autowired
    private BookRepository bookRepository;


    private JSONObject getJsonObject(String requestUrl) throws IOException, JSONException {
        URL url = new URL(requestUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        int status = connection.getResponseCode();

        BufferedReader in = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder stringBuilder = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            stringBuilder.append(inputLine);
        }
        connection.disconnect();
        in.close();

        String response = stringBuilder.toString();
        return new JSONObject(response);
    }

    private JSONArray getJsonArray(String requestUrl) throws IOException, JSONException {
        URL url = new URL(requestUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        int status = connection.getResponseCode();

        BufferedReader in = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder stringBuilder = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            stringBuilder.append(inputLine);
        }
        connection.disconnect();
        in.close();

        String response = stringBuilder.toString();
        return new JSONArray(response);
    }

    @BeforeClass
    public static void setUpApp() {
        String[] names = {"z", "y", "z"};
//        BoklibraryApplication.main(names);
        String dockerFilePath = "src/main/docker/dockerfile";

    }

//    @After
//    public void teardownDB() {
//        boolean isWindows = System.getProperty("os.name")
//                .toLowerCase().startsWith("windows");
//        String dockerExecCommand = "docker stop myDatabase";
//        Process process;
//        try {
//            if (isWindows) {
//                process = Runtime.getRuntime()
//                        .exec(String.format("cmd.exe %s", dockerExecCommand));
//            } else {
//                process = Runtime.getRuntime()
//                        .exec(String.format("sh %s", dockerExecCommand));
//            }
//            int exitCode = process.waitFor();
//            assert exitCode == 0;
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
//    }

//    @Before
//    public void setUpDB() {
//        boolean isWindows = System.getProperty("os.name")
//                .toLowerCase().startsWith("windows");
//        String dockerExecCommand = "docker run --rm --name myDatabase -d -p 5432:5432 -e POSTGRES_PASSWORD=mysecretpassword my_postgres";
//        Process process;
//        try {
//            if (isWindows) {
//                process = Runtime.getRuntime()
//                        .exec(String.format("cmd.exe %s", dockerExecCommand));
//            } else {
//                process = Runtime.getRuntime()
//                        .exec(String.format("sh %s", dockerExecCommand));
//            }
//            int exitCode = process.waitFor();
//            assert exitCode == 0;
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
//    }

    @Test
    public void ReadAuthorsTest() {
        try {
            JSONObject jsonObject = getJsonObject("http://localhost:8080/authors/");
            JSONObject embedded = jsonObject.getJSONObject("_embedded");
            JSONArray authors = embedded.getJSONArray("authors");

            assertEquals(4, authors.length());
            JSONObject andrzejSapkowski = authors.getJSONObject(0);
            //Check if andrzejSapkowski is adrzejSapkowski
            assertEquals("Andrzej", andrzejSapkowski.getString("name"));
            assertEquals("Sapkowski", andrzejSapkowski.getString("surname"));

            JSONObject wiolettaWillas = authors.getJSONObject(1);
            //Check if wiolettaWillas is Wiolletta Willas
            assertEquals("Wioletta", wiolettaWillas.getString("name"));
            assertEquals("Willas", wiolettaWillas.getString("surname"));

            JSONObject dagmaraPopiolek = authors.getJSONObject(2);
            //Check if dagmaraPopiolek is Dagmara Popiołek
            assertEquals("Dagmara", dagmaraPopiolek.getString("name"));
            assertEquals("Popiołek", dagmaraPopiolek.getString("surname"));

            JSONObject ziemowitTracz = authors.getJSONObject(3);
            //Check if ziemowitTracz is Ziemowit tracz
            assertEquals("Ziemowit", ziemowitTracz.getString("name"));
            assertEquals("Tracz", ziemowitTracz.getString("surname"));


        } catch (IOException | JSONException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void ReadBooksTEST() {
        try {
            JSONArray books = getJsonArray("http://localhost:8080/books");
            assertEquals(4, books.length());
            for (int i = 0; i < books.length(); i++) {
                JSONObject entry = books.getJSONObject(i);
                JSONArray tags = entry.getJSONArray("tags");
                JSONArray authors = entry.getJSONArray("authors");


                switch (Integer.parseInt(entry.getString("id"))) {
                    case 4:
                        assertEquals("Calineczka", entry.getString("title"));
                        assertEquals(1992, entry.getInt("year"));

                        assertEquals(1, authors.length());
                        JSONObject dagmaraPopiloek = authors.getJSONObject(0);
                        assertEquals("Dagmara", dagmaraPopiloek.getString("name"));
                        assertEquals("Popiołek", dagmaraPopiloek.getString("surname"));

                        assertEquals(3, tags.length());
                        assertEquals("fantastyka", tags.getString(0));
                        assertEquals("groza", tags.getString(1));
                        assertEquals("sensacyjne", tags.getString(2));
                        break;
                    case 3:
                        assertEquals("Rambo", entry.getString("title"));
                        assertEquals(2014, entry.getInt("year"));

                        assertEquals(1, authors.length());
                        JSONObject ramboAuthor = authors.getJSONObject(0);
                        assertEquals("Wioletta", ramboAuthor.getString("name"));
                        assertEquals("Willas", ramboAuthor.getString("surname"));

                        assertEquals(1, tags.length());
                        assertEquals("fantastyka", tags.getString(0));
                        break;
                    case 2:
                        assertEquals("Harry Potter 2", entry.getString("title"));
                        assertEquals(2015, entry.getInt("year"));

                        assertEquals(1, authors.length());
                        JSONObject harryPotter2 = authors.getJSONObject(0);
                        assertEquals("Andrzej", harryPotter2.getString("name"));
                        assertEquals("Sapkowski", harryPotter2.getString("surname"));

                        assertEquals(1, tags.length());
                        assertEquals("fantastyka", tags.getString(0));
                        break;
                    case 1:
                        assertEquals("Harry Potter", entry.getString("title"));
                        assertEquals(2012, entry.getInt("year"));

                        assertEquals(1, authors.length());
                        JSONObject harryPotter = authors.getJSONObject(0);
                        assertEquals("Andrzej", harryPotter.getString("name"));
                        assertEquals("Sapkowski", harryPotter.getString("surname"));

                        assertEquals(2, tags.length());
                        assertEquals("horror", tags.getString(0));
                        assertEquals("fantastyka", tags.getString(1));
                        break;
                }
            }
        } catch (IOException | JSONException e) {
            fail(e.getMessage());
        }
    }

	@Test
	public void AddBookTest() {
		try {

            URL url = new URL("http://localhost:8080/books/");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            int status = connection.getResponseCode();
		} catch (IOException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

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
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void DeleteBookTest() {
//        try {
//            bookRepository.URL url = new URL("http://localhost:8080/books/1/");
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("DELETE");
//        } catch (IOException e) {
//            e.printStackTrace();
//            fail(e.getMessage());
//        }

    }


}
