package books.library.boklibrary;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReadTests {

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
        BoklibraryApplication.main(names);
        String dockerFilePath = "src/main/docker/dockerfile";

    }

    @After
    public void teardownDB() {
        boolean isWindows = System.getProperty("os.name")
                .toLowerCase().startsWith("windows");
        String dockerExecCommand = "docker stop myDatabase";
        Process process;
        try {
            if (isWindows) {
                process = Runtime.getRuntime()
                        .exec(String.format("cmd.exe %s", dockerExecCommand));
            } else {
                process = Runtime.getRuntime()
                        .exec(String.format("sh %s", dockerExecCommand));
            }
            int exitCode = process.waitFor();
            assert exitCode == 0;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Before
    public void setUpDB() {
        boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
        String dockerExecCommand = "docker run --rm --name myDatabase -d -p 5432:5432 -e POSTGRES_PASSWORD=mysecretpassword my_postgres";
        Process process;
        try {
            if (isWindows) {
                process = Runtime.getRuntime().exec(String.format("cmd.exe /c %s", dockerExecCommand));
            } else {
                process = Runtime.getRuntime().exec(String.format("sh -c %s", dockerExecCommand));
            }
            int exitCode = process.waitFor();
            assert exitCode == 0;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

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
            assertEquals("PopioĹ‚ek", dagmaraPopiolek.getString("surname"));

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
    public void ReadBooksTest() {
        try {
            JSONArray books = getJsonArray("http://localhost:8080/books");
            assertEquals(4, books.length());
            for (int i = 0; i < books.length(); i++) {
                JSONObject entry = books.getJSONObject(i);
                JSONArray tags = entry.getJSONArray("tags");
                JSONArray authors = entry.getJSONArray("authors");
                assertFalse(entry.getBoolean("rented"));

                switch (Integer.parseInt(entry.getString("id"))) {
                    case 4:
                        assertEquals("Calineczka", entry.getString("title"));
                        assertEquals(1992, entry.getInt("year"));

                        assertEquals(1, authors.length());
                        JSONObject dagmaraPopiloek = authors.getJSONObject(0);
                        assertEquals("Dagmara", dagmaraPopiloek.getString("name"));
                        assertEquals("PopioĹ‚ek", dagmaraPopiloek.getString("surname"));

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
    public void ReadBooksWithHorrotTagTest() {
        try {
            String requestUrl = "http://localhost:8080/tags/horror/books";
            JSONObject jsonObject = getJsonObject(requestUrl);
            JSONObject embedded = jsonObject.getJSONObject("_embedded");
            JSONArray books = embedded.getJSONArray("books");

            assertEquals(1, books.length());
            JSONObject book = books.getJSONObject(0);

            {
                assertEquals("Harry Potter", book.getString("title"));
                assertEquals(2012, book.getInt("year"));
                assertFalse(book.getBoolean("rented"));

            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void ReadBooksWithKomediaTagTest() {
        try {
            String requestUrl = "http://localhost:8080/tags/komedia/books";
            JSONObject jsonObject = getJsonObject(requestUrl);
            JSONObject embedded = jsonObject.getJSONObject("_embedded");
            JSONArray books = embedded.getJSONArray("books");

            assertEquals(0, books.length());

        } catch (IOException | JSONException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

}
