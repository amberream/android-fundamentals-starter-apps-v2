package com.amberream.simpleasynctask;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static final String TEXT_STATE = "textState";
    public static final String PROGRESS = "progress";

    TextView mTextView;
    TextView mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = findViewById(R.id.textView1);
        mProgress = findViewById(R.id.progressBar);

        if (savedInstanceState != null)
        {
            mTextView.setText(savedInstanceState.getString(TEXT_STATE));
            mProgress.setText(savedInstanceState.getInt(PROGRESS, 0));
        }
    }

    public void startTask(View view) {
        mTextView.setText("Napping");
        mProgress.setText("0");
        mProgress.setVisibility(View.VISIBLE);
        new SimpleAsyncTask(mTextView, mProgress).execute();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(TEXT_STATE, mTextView.getText().toString());
    }
}
