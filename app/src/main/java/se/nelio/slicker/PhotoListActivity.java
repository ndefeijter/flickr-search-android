package se.nelio.slicker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import se.nelio.slicker.flickr.Photo;
import se.nelio.slicker.flickr.Photos;
import se.nelio.slicker.flickr.PhotosApi;
import se.nelio.slicker.flickr.SearchResult;
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

        final PhotosApi photosApi = ServiceGenerator.createService(PhotosApi.class);

        final SimpleItemRecyclerViewAdapter recyclerAdapter = new SimpleItemRecyclerViewAdapter(photosApi);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.photo_list);
        recyclerView.setAdapter(recyclerAdapter);

        final SearchView searchView = (SearchView) findViewById(R.id.searchView);
        final SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                recyclerAdapter.loadMore(true, query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);

        if (null == savedInstanceState) {
            // Initial load
            recyclerAdapter.loadMore(true);
        }
    }

    public class SimpleItemRecyclerViewAdapter extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<Photo> mValues = new ArrayList<>();

        private final PhotosApi photosApi;

        private int mPage = 1;
        private int mPages;

        public SimpleItemRecyclerViewAdapter(@NonNull final PhotosApi photosApi) {
            super();

            this.photosApi = photosApi;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final View view =
                LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.photo_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.setItem(mValues.get(position));

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Context context = v.getContext();
                    final Intent intent = new Intent(context, PhotoDetailActivity.class);
                    intent.putExtra(PhotoDetailActivity.EXTRA_URL, holder.getItem().toUrl()); // TODO: which size / URL

                    context.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public boolean hasMore() {
            return mPage <= mPages;
        }

        public void loadMore(boolean reset) {
            loadMore(reset, null);
        }

        public void loadMore(final boolean reset, final String query) {
            // TODO: not already running
            if (reset) {
                mPage = 1;
                mPages = 1;
            }
            if (hasMore()) {
                if ( ! TextUtils.isEmpty(query)) {
                    final Call<SearchResult> searchCall = photosApi.search(query, mPage);
                    searchCall.enqueue(new Callback<SearchResult>() {
                        @Override
                        public void onResponse(final Call<SearchResult> call, final Response<SearchResult> response) {
                            if (null != response) {
                                final SearchResult searchResult = response.body();
                                if (null != searchResult) {
                                    loadMoreResult(reset, searchResult.photos);
                                }
                                // TODO: error body
                            }
                        }

                        @Override
                        public void onFailure(Call<SearchResult> call, Throwable t) {
                            // TODO: user friendly message
                            Toast.makeText(PhotoListActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    // TODO: Implement 'getRecent' Flickr API when no search query is available #19
                }
            }
        }

        private void loadMoreResult(final boolean reset, final Photos photos) {
            if (reset) {
                mValues.clear();
            }
            if (null != photos) {
                mPage = photos.page + 1;
                mPages = photos.pages;

                if (null != photos.photo && photos.photo.size() > 0) {
                    for (final Photo photo : photos.photo) {
                        mValues.add(photo);
                    }

                    if ( ! reset) {
                        notifyItemRangeInserted(mValues.size() - photos.photo.size(), photos.photo.size());
                    }
                }
            }
            if (reset) {
                notifyDataSetChanged();
            }
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private final View mView;
            private final TextView mIdView;
            private final TextView mContentView;
            private Photo mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.content);
            }

            public void setItem(final Photo item) {
                mItem = item;
                updateGui();
            }

            public Photo getItem() {
                return mItem;
            }

            private void updateGui() {
                if (null == mItem) {
                    mIdView.setText(null);
                    mContentView.setText(null);
                } else {
                    mIdView.setText(mItem.id);
                    mContentView.setText(mItem.toUrl());
                }
            }

            @Override
            public String toString() {
                return super.toString() + " " + mItem;
            }
        }
    }
}
