package com.amberream.whowroteit;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

/**
 * Extend AsyncTaskLoader mainly to handle config changes (long running & must update ui)
 */
public class BookLoader extends AsyncTaskLoader<String> {


    private final String mQueryString;

    public BookLoader(@NonNull Context context, String queryString) {
        super(context);
        mQueryString = queryString;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        // the loader won't start loading until you call forceLoad
        forceLoad();
    }

    @Nullable
    @Override
    public String loadInBackground() {
        return NetworkUtils.getBookInfo(mQueryString);
    }
}
