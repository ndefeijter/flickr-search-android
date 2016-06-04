package se.nelio.slicker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import se.nelio.slicker.flickr.PhotosApi;
import se.nelio.slicker.flickr.ServiceGenerator;

/**
 * An activity representing a list of Photos. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link PhotoDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class PhotoListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_list);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.photo_list);
        recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                // https://github.com/square/picasso/issues/457https://github.com/square/picasso/issues/457
                // Remove self
                recyclerView.removeOnLayoutChangeListener(this);

                final PhotosApi photosApi = ServiceGenerator.createService(PhotosApi.class);
                final int recyclerViewWidth = recyclerView.getWidth();
                final PhotoRecyclerViewAdapter recyclerAdapter = new PhotoRecyclerViewAdapter(PhotoListActivity.this, photosApi, recyclerViewWidth) {

                    @Override
                    public void onLoadMoreStarted() {
                        swipeRefreshLayout.setRefreshing(true);
                    }

                    @Override
                    public void onLoadMoreFinished() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                };
                recyclerView.setAdapter(recyclerAdapter);

                final SearchView searchView = (SearchView) findViewById(R.id.searchView);
                final SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        recyclerAdapter.loadMore(true, query);
                        searchView.clearFocus();
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        return false;
                    }
                };
                searchView.setOnQueryTextListener(queryTextListener);


                swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        recyclerAdapter.loadMore(true, null != searchView.getQuery() ? searchView.getQuery().toString() : null);
                    }
                });

                // TODO: only load initially, cache on rotation so current position, etc. can be maintained.
                recyclerAdapter.loadMore(true, null != searchView.getQuery() ? searchView.getQuery().toString() : null);
            }
        });
    }
}
