package com.amberream.whowroteit;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    private static final String QUERY_STRING = "queryString";
    TextView textViewAuthor;
    TextView textViewTitle;
    EditText editTextBookInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextBookInput = findViewById(R.id.bookInput);
        textViewAuthor = findViewById(R.id.authorText);
        textViewTitle = findViewById(R.id.titleText);

        if (getSupportLoaderManager() != null)
        {
            // this reconnects to an existing loader (no need to pass in the args)
            getSupportLoaderManager().initLoader(0, null, this);
        }
    }

    public void searchBooks(View view) {
        // hide the keyboard
        InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (manager != null)
        {
            manager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }

        // check for a valid network connection
        NetworkInfo networkInfo = null;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        if (connectivityManager != null)
        {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }

        String query = editTextBookInput.getText().toString();

        if (networkInfo != null && networkInfo.isConnected() && query.length() > 0) {
//            new FetchBook(textViewAuthor, textViewTitle).execute(query);

            Bundle queryBundle = new Bundle();
            queryBundle.putString(QUERY_STRING, query);
            // when we start the loader we need to pass the arguments (i.e. the query string)
            getSupportLoaderManager().restartLoader(0, queryBundle, this);

            textViewTitle.setText(R.string.loading);
        }
        else if (query.length() <= 0)
        {
            textViewTitle.setText(R.string.no_search_item);
        }
        else
        {
            textViewTitle.setText(R.string.no_network);
        }
        textViewAuthor.setText("");
    }

    /**
     * Called when you instantiate the loader.
     * Create the loader in this method.
     * @param i id
     * @param bundle args
     * @return
     */
    @NonNull
    @Override
    public Loader<String> onCreateLoader(int i, @Nullable Bundle bundle) {

        String queryString = "";
        if (bundle != null)
        {
            queryString = bundle.getString(QUERY_STRING);
        }
        return new BookLoader(this, queryString);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String s) {
        // called when the loader's task finishes

        try {
            JSONObject json = new JSONObject(s);
            JSONArray jsonArray = json.getJSONArray("items");

            String title = null;
            String author = null;

            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject book = jsonArray.getJSONObject(i);
                JSONObject volumeInfo = book.getJSONObject("volumeInfo");

                title = volumeInfo.getString("title");
                author = volumeInfo.getString("authors");

                if (author!= null && title != null)
                {
                    break;
                }
            }

            if (title != null && author != null) {
                textViewTitle.setText(title);
                textViewAuthor.setText(author);
            }
            else
            {
                textViewTitle.setText(R.string.no_results);
                textViewAuthor.setText("");
            }

        } catch (JSONException e) {
            textViewTitle.setText(R.string.no_results);
            textViewAuthor.setText("");
            e.printStackTrace();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {
        // cleans up remaining resources
    }
}
