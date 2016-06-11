package se.nelio.slicker;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import se.nelio.slicker.flickr.Photo;
import se.nelio.slicker.flickr.PhotoSize;

public class PhotoViewHolder extends RecyclerView.ViewHolder {

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final Context context = v.getContext();
            final Intent intent = new Intent(context, PhotoDetailActivity.class);
            intent.putExtra(PhotoDetailActivity.EXTRA_URL, mPhoto.toUrl()); // TODO: which size / URL -> just send the Photo

            context.startActivity(intent);
        }
    };

    private final View mView;
    private final ImageView mContentView;
    private final int mPhotoWidth;

    private Photo mPhoto;

    public PhotoViewHolder(@NonNull final View view, final int photoWidth) {
        super(view);

        mView = view;
        mContentView = (ImageView) view.findViewById(R.id.content);
        mPhotoWidth = photoWidth;
    }

    public void setItem(final Photo photo) {
        mPhoto = photo;

        updateGui();
    }

    public Photo getItem() {
        return mPhoto;
    }

    private void updateGui() {
        if (null == mPhoto) {
            mContentView.setImageBitmap(null);
            mContentView.setOnClickListener(null);
        } else {
            mContentView.getLayoutParams().width = mPhotoWidth;
            mContentView.getLayoutParams().height = mPhotoWidth;

            final PhotoSize photoSize = PhotoSize.forLongestSide(mPhotoWidth);
            Picasso.with(mView.getContext())
                    .load(mPhoto.toUrl(photoSize))
                    .tag(PicassoOnScrollListener.TAG)
                    .fit()
                    .centerCrop()
                    .into(mContentView);
            mContentView.setOnClickListener(mOnClickListener);
        }
    }

    @Override
    public String toString() {
        return "PhotoViewHolder{" +
                "mPhoto=" + mPhoto +
                ", mPhotoWidth=" + mPhotoWidth +
                '}';
    }
}
