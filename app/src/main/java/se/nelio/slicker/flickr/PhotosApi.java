package se.nelio.slicker.flickr;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PhotosApi {

    /**
     * https://www.flickr.com/services/api/flickr.photos.search.html
     */
    @GET("?method=flickr.photos.search")
    Call<SearchResult> search(
            @Query("text") String text,
            @Query("page") int page
    );

    /**
     * https://www.flickr.com/services/api/flickr.photos.getRecent.html
     */
    @GET("?method=flickr.photos.getRecent")
    Call<SearchResult> getRecent(
            @Query("page") int page
    );
}
