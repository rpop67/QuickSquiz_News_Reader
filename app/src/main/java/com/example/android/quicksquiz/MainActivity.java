package com.example.android.quicksquiz;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.os.Bundle;
import android.content.AsyncTaskLoader;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
    implements LoaderCallbacks<ArrayList<Post>>{
    private static final int POST_LOADER_ID = 1;
    PostAdapter newsAdapter;
    PostLoader postSync;


    private static  final String REQUEST_URL="https://content.guardianapis.com/search?api-key=56cc9867-3495-4cba-a70e-22c05c892e64&show-tags=contributor";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LoaderManager loaderManager = getLoaderManager();

        // Initialize the loader. Pass in the int ID constant defined above and pass in null for
        // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
        // because this activity implements the LoaderCallbacks interface).
        loaderManager.initLoader(POST_LOADER_ID, null, this);

    }

        @Override
        public Loader<ArrayList<Post>> onCreateLoader ( int id, Bundle args){
            PostLoader postSync = new PostLoader(this, REQUEST_URL);
            return postSync;
        }

        @Override
        public void onLoadFinished (Loader < ArrayList < Post >> loader, ArrayList < Post > news){

            newsAdapter = new PostAdapter(this, news);

            // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
            // data set. This will trigger the ListView to update.
            if (news != null && !news.isEmpty()) {
                newsAdapter.addAll(news);
            }

            // Get a reference to the ListView, and attach the adapter to the listView.
           ListView listView = findViewById(R.id.list);
           listView.setAdapter(newsAdapter);


        }


        @Override
        public void onLoaderReset (Loader < ArrayList < Post >> loader) {
            //newsAdapter.clear();
        }





}
