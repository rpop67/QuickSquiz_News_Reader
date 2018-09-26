package com.example.android.quicksquiz;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;


/**
 * Created by Akanksha_Rajwar on 26-09-2018.
 */

public class PostLoader extends AsyncTaskLoader<ArrayList<Post>> {
    private String mUrl = null;

    public PostLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    public ArrayList<Post> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        ArrayList<Post> News = Utils.fetchJSONData(mUrl);
        return News;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
