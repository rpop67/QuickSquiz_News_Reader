package com.example.android.quicksquiz;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<ArrayList<Post>> {
    private static final int POST_LOADER_ID = 1;
    PostAdapter newsAdapter;
    ListView listView;
    TextView emptyTextView;
    RelativeLayout RelView;
    ProgressBar loadingIndicator;
    View view;

    private static final String REQUEST_URL = "https://content.guardianapis.com/search";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadingIndicator = findViewById(R.id.loading_indicator);
        emptyTextView = findViewById(R.id.empty_view);
        RelView = findViewById(R.id.relView);
        listView = findViewById(R.id.list);
        listView.setEmptyView(emptyTextView);
        ConnectivityManager connmanager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connmanager.getActiveNetworkInfo();
        RelView.setVisibility(View.INVISIBLE);

        // Initialize the loader. Pass in the int ID constant defined above and pass in null for
        // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
        // because this activity implements the LoaderCallbacks interface).
        if (networkInfo != null && networkInfo.isConnected()) {
            RelView.setVisibility(View.VISIBLE);
            startProgress(view);
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(POST_LOADER_ID, null, this);
        } else if (networkInfo == null) {
            RelView.setVisibility(View.GONE);
            emptyTextView.setText("No Internet Connection");
        }
    }

    //*******************MENU**********************
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the Options Menu we specified in XML
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, Settings.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //******************MENU********************

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
                if (i == 10)
                    i = 0;
                loadingIndicator.setProgress(value);
            }
        }
    }

    @Override
    public Loader<ArrayList<Post>> onCreateLoader(int id, Bundle args) {
        String arr[] = {getString(R.string.football_default), getString(R.string.sport_default), getString(R.string.country_default), getString(R.string.politics_default), getString(R.string.music_default)};
        Set<String> newsChoiceDef = new HashSet<>(Arrays.asList(arr));

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        Set<String> newsChoice = sharedPrefs.getStringSet(getString(R.string.keyTopic), newsChoiceDef);
        StringBuilder sections = new StringBuilder();
        boolean first = true;
        for (String section : newsChoice) {
            if (first)
                first = false;
            else
                sections.append(",");
            sections.append(section);
        }

        String newsCount = sharedPrefs.getString(getString(R.string.keyPosts), "60");
        Uri mainUri = Uri.parse(REQUEST_URL);
        Uri.Builder uriBuild = mainUri.buildUpon();
        uriBuild.appendQueryParameter(getString(R.string.apikey), getString(R.string.apiKey));

        if (!sections.toString().isEmpty()) {
            uriBuild.appendQueryParameter("q", sections.toString());
        }

        uriBuild.appendQueryParameter(getString(R.string.format), getString(R.string.json));
        uriBuild.appendQueryParameter(getString(R.string.showField), getString(R.string.thumbnail));
        uriBuild.appendQueryParameter(getString(R.string.pageSize), newsCount);
        uriBuild.appendQueryParameter(getString(R.string.showTags), getString(R.string.contributor));

        return new PostLoader(this, uriBuild.toString());
        //resolved preferences
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Post>> loader, ArrayList<Post> news) {
        RelView.setVisibility(View.GONE);
        emptyTextView.setText(getString(R.string.noNewStuff));
        newsAdapter = new PostAdapter(this, news);

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (news != null && !news.isEmpty()) {
            listView.setAdapter(newsAdapter);
        } else if (news == null) {
            Log.i("TAG", getString(R.string.onLoadMsg));
        }
        // Get a reference to the ListView, and attach the adapter to the listView.
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Post>> loader) {
        newsAdapter.clear();
    }


}
