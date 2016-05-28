package se.nelio.slicker.flickr;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PhotosApi {

    @GET("?method=flickr.photos.search")
    Call<SearchResult> search(
            @Query("text") String text,
            @Query("page") int page
    );
}
