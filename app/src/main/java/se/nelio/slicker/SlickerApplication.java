package se.nelio.slicker;

import android.app.Application;

import com.squareup.picasso.Picasso;

public class SlickerApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Picasso.Builder picassoBuilder = new Picasso.Builder(this);
        picassoBuilder.loggingEnabled(BuildConfig.DEBUG);
        picassoBuilder.indicatorsEnabled(BuildConfig.DEBUG);
        // Use default memory and disk caching options for now.
        Picasso.setSingletonInstance(picassoBuilder.build());
    }
}
