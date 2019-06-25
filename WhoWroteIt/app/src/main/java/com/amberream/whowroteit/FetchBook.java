package com.amberream.whowroteit;

import android.os.AsyncTask;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.lang.ref.WeakReference;

/**
 * AsyncTask<Param, Progress, Result>
 */
public class FetchBook extends AsyncTask<String, Void, String> {

    private WeakReference<TextView> textViewAuthor;
    private WeakReference<TextView> textViewTitle;

    public FetchBook(TextView textViewAuthor, TextView textViewTitle) {
        this.textViewAuthor = new WeakReference<TextView>(textViewAuthor);
        this.textViewTitle = new WeakReference<TextView>(textViewTitle);
    }


    @Override
    protected String doInBackground(String... strings) {
        return NetworkUtils.getBookInfo(strings[0]);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

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
                textViewTitle.get().setText(title);
                textViewAuthor.get().setText(author);
            }
            else
            {
                textViewTitle.get().setText(R.string.no_results);
                textViewAuthor.get().setText("");
            }

        } catch (JSONException e) {
            textViewTitle.get().setText(R.string.no_results);
            textViewAuthor.get().setText("");
            e.printStackTrace();
        }
    }
}
