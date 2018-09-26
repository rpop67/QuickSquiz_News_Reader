package com.example.android.quicksquiz;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<ArrayList<Post>> {
    private static final int POST_LOADER_ID = 1;
    PostAdapter newsAdapter;
    ListView listView;
    TextView emptyTextView;
    RelativeLayout RelView;
    ProgressBar loadingIndicator;
    View view;

    private static final String REQUEST_URL = "https://content.guardianapis.com/search?api-key=56cc9867-3495-4cba-a70e-22c05c892e64&show-tags=contributor";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadingIndicator = findViewById(R.id.loading_indicator);
        emptyTextView = findViewById(R.id.empty_view);
        RelView = findViewById(R.id.relView);
        listView = findViewById(R.id.list);

        listView.setEmptyView(emptyTextView);

        ConnectivityManager connmanager=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connmanager.getActiveNetworkInfo();
        RelView.setVisibility(View.INVISIBLE);


        // Initialize the loader. Pass in the int ID constant defined above and pass in null for
        // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
        // because this activity implements the LoaderCallbacks interface).
        if(networkInfo!=null && networkInfo.isConnected())
        {
            RelView.setVisibility(View.VISIBLE);
            startProgress(view);
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(POST_LOADER_ID, null, this);
        }
        else if(networkInfo==null)
        {

            RelView.setVisibility(View.GONE);
            emptyTextView.setText("No Internet Connection");
        }

    }

    public void startProgress(View view) {

        loadingIndicator.setProgress(0);
        new Thread(new Task()).start();
    }

    class Task implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i <= 10; i++) {
                final int value = 10 * i;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                loadingIndicator.setProgress(value);

            }
        }

    }

    @Override
    public Loader<ArrayList<Post>> onCreateLoader(int id, Bundle args) {
        PostLoader postSync = new PostLoader(this, REQUEST_URL);
        return postSync;
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Post>> loader, ArrayList<Post> news) {
        RelView.setVisibility(View.GONE);
        emptyTextView.setText("You have reached end. No new stuff for now!");
        newsAdapter = new PostAdapter(this, news);

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (news != null && !news.isEmpty()) {
            newsAdapter.addAll(news);
        }

        // Get a reference to the ListView, and attach the adapter to the listView.
        listView.setAdapter(newsAdapter);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Post>> loader) {
        //newsAdapter.clear();
    }


}
