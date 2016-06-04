package se.nelio.slicker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.squareup.picasso.Picasso;

/**
 * Used for pausing downloads while scrolling.
 * <p>
 * Based on https://nullpointer.wtf/android/image-loading-with-picasso/.
 */
public class PicassoOnScrollListener extends RecyclerView.OnScrollListener {

    public static final Object TAG = new Object();
    private static final int SETTLING_DELAY = 500;

    private final Picasso mPicasso;

    private Runnable mSettlingResumeRunnable;

    public PicassoOnScrollListener(@NonNull final Context context) {
        this.mPicasso = Picasso.with(context.getApplicationContext());
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int scrollState) {
        if (RecyclerView.SCROLL_STATE_IDLE == scrollState) {
            recyclerView.removeCallbacks(mSettlingResumeRunnable);
            mPicasso.resumeTag(TAG);
        } else if (RecyclerView.SCROLL_STATE_SETTLING == scrollState) {
            mSettlingResumeRunnable = new Runnable() {
                @Override
                public void run() {
                    mPicasso.resumeTag(TAG);
                }
            };
            recyclerView.postDelayed(mSettlingResumeRunnable, SETTLING_DELAY);
        } else {
            mPicasso.pauseTag(TAG);
        }
    }
}
