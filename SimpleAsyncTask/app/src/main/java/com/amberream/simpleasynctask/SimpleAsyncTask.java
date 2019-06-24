package com.amberream.simpleasynctask;

import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.Random;

/**
 * <Params, Progress, Result>
 *     Params -The data type of the parameters sent to the task upon executing the doInBackground() override method.
 *     Progress -The data type of the progress units published using the onProgressUpdated() override method.
 *     Result - The data type of the result delivered by the onPostExecute() override method.
 */
public class SimpleAsyncTask extends AsyncTask<Void, Integer, String> {
    WeakReference<TextView> mTextView;
    WeakReference<TextView> mProgress;

    public SimpleAsyncTask(TextView tv, TextView progressBar) {
        mTextView = new WeakReference<TextView>(tv);
        mProgress = new WeakReference<TextView>(progressBar);
    }


    @Override
    /**
     * This is where you implement the code to execute the work that is to be performed on the separate thread.R
     */
    protected String doInBackground(Void... voids) {
        Random r = new Random();
        int n = r.nextInt(11);
        int s = n * 200;

        for (int i = 0 ; i < s; i+= 100)
        {
            try {
                Thread.sleep(s);
                publishProgress(i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        return "Awake at last after sleeping " + s + " milliseconds!";
    }

    @Override
    protected void onPostExecute(String s) {
        // ui updates need to be done here (can't be done on background thread)
        mTextView.get().setText(s);
        mProgress.get().setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        mProgress.get().setText("" + values[0]);
    }
}
