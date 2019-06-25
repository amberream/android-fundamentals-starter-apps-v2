package com.amberream.whowroteit;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkUtils {

    public static final String LOG_TAG = NetworkUtils.class.getSimpleName();

    public static final String BOOK_BASE_URL = "https://www.googleapis.com/books/v1/volumes?";
    public static final String QUERY_PARAM = "q";
    public static final String MAX_RESULTS = "maxResult";
    public static final String PRINT_TYPE = "printType";

    static HttpURLConnection urlConnection;
    static BufferedReader reader;
    static String bookJSONString;

    public static String getBookInfo(String queryString) {
        try {
            Uri builtUri = Uri.parse(BOOK_BASE_URL).buildUpon().appendQueryParameter(QUERY_PARAM, queryString).appendQueryParameter(MAX_RESULTS, "10").appendQueryParameter(PRINT_TYPE, "books").build();
            URL requestUrl = new URL(builtUri.toString());
            urlConnection = (HttpURLConnection) requestUrl.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream is = urlConnection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is));

            StringBuilder builder = new StringBuilder();
            String line = reader.readLine();
            while(line != null)
            {
                builder.append(line);
                builder.append("\n");
                line = reader.readLine();
            }
            if (builder.length() == 0)
            {
                return null;
            }

            bookJSONString = builder.toString();
            Log.d(LOG_TAG, bookJSONString);
        }
        catch (IOException ioException)
        {
            ioException.printStackTrace();
        }
        finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null)
            {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return bookJSONString;
    }


}
