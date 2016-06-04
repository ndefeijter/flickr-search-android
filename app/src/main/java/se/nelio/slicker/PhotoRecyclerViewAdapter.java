package se.nelio.slicker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class PhotoRecyclerViewAdapter extends RecyclerView.Adapter<PhotoViewHolder> {

    private final List<Photo> mValues = new ArrayList<>();

    private final Context mContext;
    private final PhotosApi mPhotosApi;
    private final int mPhotoWidth;

    private int mPage;
    private int mPages;

    public PhotoRecyclerViewAdapter(@NonNull final Context context, @NonNull final PhotosApi photosApi, final int photoWidth) {
        super();

        mContext = context;
        mPhotosApi = photosApi;
        mPhotoWidth = photoWidth;
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view =
            LayoutInflater.from(parent.getContext())
                .inflate(R.layout.photo_list_content, parent, false);
        return new PhotoViewHolder(view, mPhotoWidth);
    }

    @Override
    public void onBindViewHolder(final PhotoViewHolder holder, final int position) {
        holder.setItem(mValues.get(position));
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public boolean hasMore() {
        return mPage < mPages;
    }

    public boolean isLoadingMore() {
        return false;
    }

    public void loadMore(final boolean reset) {
        loadMore(reset, null);
    }

    public void loadMore(final boolean reset, final String query) {
        // TODO: cancel current if already running

        onLoadMoreStarted();
        if (reset) {
            mPage = 0;
            mPages = Integer.MAX_VALUE;
        }
        if (hasMore()) {
            final Call<SearchResult> searchCall;
            if ( ! TextUtils.isEmpty(query)) {
                searchCall = mPhotosApi.search(query, mPage + 1);
            } else {
                searchCall = mPhotosApi.getRecent(mPage + 1);
            }
            searchCall.enqueue(new Callback<SearchResult>() {
                @Override
                public void onResponse(final Call<SearchResult> call, final Response<SearchResult> response) {
                    onLoadMoreFinished();
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
                    onLoadMoreFinished();
                    // TODO: user friendly message
                    Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            onLoadMoreFinished();
        }
    }

    public void onLoadMoreStarted() {
        // Empty default implementation
    }

    public void onLoadMoreFinished() { // TODO: network error, see #13
        // Empty default implementation
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
}
