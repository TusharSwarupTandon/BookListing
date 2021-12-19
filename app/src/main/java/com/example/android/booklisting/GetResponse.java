package com.example.android.booklisting;

import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public final class GetResponse
{
    private static final String LOG_TAG = BooksActivity.class.getName();

    private GetResponse()
    {

    }

    public static List<Book> fetchBookData(String requestURL)
    {
        URL url = createURL(requestURL);

        String jsonResponse = null;
        try
        {
            jsonResponse = makeHTTPRequest(url);
        }
        catch(IOException e)
        {
            Log.e(LOG_TAG, "Problem while making HTTP request : ", e);
        }

        return extractDataFromJSON(jsonResponse);
    }

    private static URL createURL(String stringURL)
    {
        URL url = null;
        try
        {
            url = new URL(stringURL);
        }
        catch (MalformedURLException e)
        {
            Log.e(LOG_TAG, " Error while creating url : ", e);
        }
        return url;
    }

    private static String makeHTTPRequest(URL url) throws IOException
    {
        String jsonResponse = "";

        if(url == null)
        {
            return jsonResponse;
        }

        HttpsURLConnection urlConnection = null;
        InputStream in = null;

        try
        {
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if(urlConnection.getResponseCode() == 200)
            {
                in = urlConnection.getInputStream();
                jsonResponse = readFromStream(in);
            }
            else
            {
                Log.e(LOG_TAG,"Error response code : "+ urlConnection.getResponseCode());
            }
        }
        catch (IOException e)
        {
            Log.e(LOG_TAG,"Error retrieving data", e);
        }
        finally
        {
            if(urlConnection != null)
            {
                urlConnection.disconnect();
            }
            if(in != null)
            {
                in.close();
            }

        }

        return jsonResponse;
    }

    private static String readFromStream(InputStream in) throws IOException
    {
        StringBuilder data = new StringBuilder();
        if(in != null)
        {
            InputStreamReader inRead = new InputStreamReader(in, Charset.forName("UTF-8"));
            BufferedReader br = new BufferedReader(inRead);
            String line = br.readLine();
            while (line != null)
            {
                data.append(line);
                line = br.readLine();
            }
        }
        return data.toString();
    }

    private static ArrayList<Book> extractDataFromJSON(String jsonData)
    {
        ArrayList<Book> book = new ArrayList<>();

        try
        {
            JSONObject root = new JSONObject(jsonData);
            if(root.has("items"))
            {
                JSONArray itemsArray = root.optJSONArray("items");


                for (int i = 0; i < itemsArray.length(); i++) {
                    JSONObject currentBook = itemsArray.getJSONObject(i);

                    JSONObject volumeObject = currentBook.getJSONObject("volumeInfo");

                    String title = volumeObject.getString("title");

                    StringBuilder sb = new StringBuilder("");
                    if (volumeObject.has("authors")) {
                        JSONArray authorsArray = volumeObject.getJSONArray("authors");
                        for (int j = 0; j < authorsArray.length(); j++) {
                            sb.append(authorsArray.getString(j));
                        }
                    }
                    String author = sb.toString();

                    String preview = volumeObject.getString("previewLink");

                    String thumbnail = "";
                    if (volumeObject.has("imageLinks"))
                    {
                        JSONObject imageObject = volumeObject.getJSONObject("imageLinks");
                        thumbnail = imageObject.getString("smallThumbnail").replace("http","https");
                    }

                    Book bookItem = new Book(thumbnail, title, author, preview);
                    book.add(bookItem);
                }
            }
        }
        catch (JSONException e)
        {
            Log.e(LOG_TAG, "Problem parsing JSON result", e);
        }

        return book;
    }
}
