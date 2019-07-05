package com.amberream.whowroteit;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView textViewAuthorText;
    TextView textViewTitleText;
    EditText editTextBookInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextBookInput = findViewById(R.id.bookInput);
        textViewAuthorText = findViewById(R.id.authorText);
        textViewTitleText = findViewById(R.id.titleText);
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
            new FetchBook(textViewAuthorText, textViewTitleText).execute(query);
            textViewTitleText.setText(R.string.loading);
        }
        else if (query.length() <= 0)
        {
            textViewTitleText.setText(R.string.no_search_item);
        }
        else
        {
            textViewTitleText.setText(R.string.no_network);
        }
        textViewAuthorText.setText("");
    }
}
