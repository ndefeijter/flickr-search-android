package se.nelio.slicker.flickr;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import retrofit2.Call;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

@RunWith(JUnit4.class)
public class PhotosApiTest {

    private PhotosApi photosApi;

    @Before
    public void before() {
        photosApi = ServiceGenerator.createService(PhotosApi.class);
    }

    @Test
    public void search_on_real_server() throws IOException {

        final Call<SearchResult> searchResultCall = photosApi.search("Philips", 1);
        final SearchResult searchResult = searchResultCall.execute().body();

        assertThat(searchResult, is(not(nullValue(SearchResult.class))));
        verifyPhotos(searchResult.photos);
    }

    @Test
    public void getRecent_on_real_server() throws IOException {

        final Call<SearchResult> searchResultCall = photosApi.getRecent(1);
        final SearchResult searchResult = searchResultCall.execute().body();

        assertThat(searchResult, is(not(nullValue(SearchResult.class))));
        verifyPhotos(searchResult.photos);
    }

    private static void verifyPhotos(final Photos photos) throws IOException {
        assertThat(photos, is(not(nullValue(Photos.class))));
        assertThat(photos.page, is(equalTo(1)));
        assertThat(photos.pages, is(greaterThanOrEqualTo(1)));
        assertThat(photos.perpage, is(100));
        assertThat(photos.total, is(greaterThanOrEqualTo(1)));
        assertThat(photos.photo, is(not(nullValue(List.class))));

        assertThat(photos.photo, is(not(empty())));
        for (final Photo photo : photos.photo) {
            assertThat(photo, is(not(nullValue(Photo.class))));
            assertThat(photo.farm, is(not(isEmptyOrNullString())));
            assertThat(photo.id, is(not(isEmptyOrNullString())));
            assertThat(photo.secret, is(not(isEmptyOrNullString())));
            assertThat(photo.server, is(not(isEmptyOrNullString())));
        }
        verifyPhotoUrl(photos.photo.get(0).toUrl());
        for (PhotoSize photoSize : PhotoSize.values()) {
            verifyPhotoUrl(photos.photo.get(0).toUrl(photoSize));
        }
    }

    private static void verifyPhotoUrl(final String url) throws IOException {
        final URL photoUrl = new URL(url);
        final HttpURLConnection photoConnection = (HttpURLConnection) photoUrl.openConnection();
        try {
            assertThat(photoConnection.getResponseCode(), is(200));

        } finally {
            photoConnection.disconnect();
        }
    }
}
