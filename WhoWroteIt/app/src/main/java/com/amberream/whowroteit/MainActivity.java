package com.amberream.whowroteit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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
        String query = editTextBookInput.getText().toString();
        new FetchBook(textViewAuthorText, textViewTitleText).execute(query);
    }
}
