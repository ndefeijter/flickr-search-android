package se.nelio.slicker.flickr;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {
    public static final String API_BASE_URL = " https://api.flickr.com/services/rest/";

    private static final OkHttpClient.Builder HTTP_CLIENT =
        new OkHttpClient.Builder()
            .addInterceptor(
                new Interceptor() {
                    @Override
                    public Response intercept(final Chain chain) throws IOException {
                        final Request originalRequest = chain.request();
                        final HttpUrl originalHttpUrl = originalRequest.url();

                        // Add general arguments to URL
                        final HttpUrl httpUrl =
                            originalHttpUrl.newBuilder()
                                .addQueryParameter("api_key", "ba87c5f7168b6277aba412a9c0ac4abc")
                                .addQueryParameter("format", "json")
                                .addQueryParameter("nojsoncallback", "1")
                                .build();

                        // Apply changes to original request
                        final Request request =
                            originalRequest.newBuilder()
                                .url(httpUrl)
                                .build();

                        return chain.proceed(request);
                    }
                }
        );

    private static final Retrofit.Builder RETROFIT =
        new Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());

    public static <S> S createService(Class<S> serviceClass) {
        Retrofit retrofit = RETROFIT.client(HTTP_CLIENT.build()).build();
        return retrofit.create(serviceClass);
    }
}
