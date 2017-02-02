package savern.newsapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


public final class QueryUtils {


    private QueryUtils() {

    }

    public static List<News> fetchNewsData(String requestUrl) {
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
        }
        // Extract relevant fields from the JSON response and return a {@link List<Books>} List
        return extractNews(jsonResponse);
    }


    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
        }
        return url;
    }


    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<News> extractNews(String jsonResponse) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }
        // Create an empty ArrayList that we can start adding news to
        List<News> news = new ArrayList<>();
        try {
            // Create a JSONObject from the JSON response string
            JSONObject jsonObj = new JSONObject(jsonResponse);
            // Extract the JSONObject associated with the key called "response",
            JSONObject response = jsonObj.getJSONObject("response");
            // Extract the JSONArray associated with the key called "docs",
            JSONArray results = response.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                // Get a single news item at position i within the list of news
                JSONObject resultsJSONObject = results.getJSONObject(i);

                String title = "";
                if (resultsJSONObject.has("webTitle")) {
                    title = resultsJSONObject.getString("webTitle");
                }

                String section = "";
                if (resultsJSONObject.has("sectionName")) {
                    section = resultsJSONObject.getString("sectionName");
                }

                String url = resultsJSONObject.getString("webUrl");

                String date = "";
                if (resultsJSONObject.has("webPublicationDate")) {
                    date = resultsJSONObject.getString("webPublicationDate");
                }

                // Create a new {@link News} object with the author, and title from the JSON response.
                News newsItem = new News(date.substring(0, 10), title, section, url);
                // Add the new {@link News} to the list of news.
                news.add(newsItem);
            }
            return news;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return news;
    }

}