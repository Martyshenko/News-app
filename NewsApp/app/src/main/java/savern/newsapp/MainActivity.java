package savern.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static savern.newsapp.R.id.swipeContainer;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private String mRequest = "http://content.guardianapis.com/search?politics/politics&api-key=fd75ba6f-4dcc-495b-9161-720bba973517";
    /**
     * Adapter for the list of news
     */
    private NewsAdapter mAdapter;
    private static final int EARTHQUAKE_LOADER_ID = 1;
    private TextView mEmptyView;
    private View mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(swipeContainer);

        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        mLoadingIndicator = findViewById(R.id.loading_spinner);

        // Find a reference to the {@link ListView} in the layout
        ListView newsListView = (ListView) findViewById(R.id.list);

        mEmptyView = (TextView) findViewById(R.id.empty_list_item);
        newsListView.setEmptyView(mEmptyView);

        mAdapter = new NewsAdapter(
                this, new ArrayList<News>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        newsListView.setAdapter(mAdapter);
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                News currentNews = mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri earthquakeUri = Uri.parse(currentNews.getUrl());

                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                getLoaderManager().restartLoader(0, null, MainActivity.this);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        initLoader();
    }


    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        return new NewsLoader(this, mRequest);
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> listOfNewses) {
        // Hide loading indicator because the data has been loaded
        mLoadingIndicator.setVisibility(View.GONE);
        // Clear the adapter of previous news data
        mAdapter.clear();

        // If there is a valid list of {@link listOfNewses}, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (listOfNewses != null && !listOfNewses.isEmpty()) {
            mAdapter.addAll(listOfNewses);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        //Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }

    private void initLoader() {
        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, MainActivity.this);
        } else {
            //Otherwise, display error
            //First, hide loading indicator so error message will be visible
            mLoadingIndicator = findViewById(R.id.loading_spinner);
            mLoadingIndicator.setVisibility(View.GONE);
            // Update empty state with no connection error message
            mEmptyView.setText(R.string.no_internet);
        }
    }


}