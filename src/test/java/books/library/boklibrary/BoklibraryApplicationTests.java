package books.library.boklibrary;

//import lombok.var;

import books.library.boklibrary.domain.*;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
@RunWith(SpringRunner.class)
@SpringBootTest
public class BoklibraryApplicationTests {

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

    @BeforeClass
    public static void setUpApp() {
        String[] names = {"z", "y", "z"};
        BoklibraryApplication.main(names);
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
	public void AddBookTest() {
		try {

            URL url = new URL("http://localhost:8080/books/");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            int status = connection.getResponseCode();
//			assertEquals(4, authors.length());
//			JSONObject andrzejSapkowski = authors.getJSONObject(0);
//			//Check if andrzejSapkowski is adrzejSapkowski
//			assertEquals("Andrzej", andrzejSapkowski.getString("name"));
//			assertEquals("Sapkowski", andrzejSapkowski.getString("surname"));
//
//			JSONObject wiolettaWillas = authors.getJSONObject(1);
//			//Check if wiolettaWillas is Wiolletta Willas
//			assertEquals("Wioletta", wiolettaWillas.getString("name"));
//			assertEquals("Willas", wiolettaWillas.getString("surname"));
//
//			JSONObject dagmaraPopiolek = authors.getJSONObject(2);
//			//Check if dagmaraPopiolek is Dagmara Popiołek
//			assertEquals("Dagmara", dagmaraPopiolek.getString("name"));
//			assertEquals("Popiołek", dagmaraPopiolek.getString("surname"));
//
//			JSONObject ziemowitTracz = authors.getJSONObject(3);
//			//Check if ziemowitTracz is Ziemowit tracz
//			assertEquals("Ziemowit", ziemowitTracz.getString("name"));
//			assertEquals("Tracz", ziemowitTracz.getString("surname"));



		} catch (IOException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

    @Test
    public void contextLoads() {
    }

}
