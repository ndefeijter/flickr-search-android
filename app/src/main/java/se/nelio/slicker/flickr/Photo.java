package se.nelio.slicker.flickr;

import android.support.annotation.NonNull;

public class Photo {

    public String id;
    public String secret;
    public String server;
    public String farm;

    public String toUrl() {
        // See https://www.flickr.com/services/api/misc.urls.html
        return String.format("https://farm%s.staticflickr.com/%s/%s_%s.jpg", farm, server, id, secret);
    }

    public String toUrl(@NonNull PhotoSize size) {
        // See https://www.flickr.com/services/api/misc.urls.html
        return String.format("https://farm%s.staticflickr.com/%s/%s_%s_%s.jpg", farm, server, id, secret, size.id());
    }

    @Override
    public String toString() {
        return "Photo{" +
                "id='" + id + "', " +
                "secret='" + secret + "', " +
                "server='" + server + "', " +
                "farm='" + farm + "'" +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Photo that = (Photo) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (secret != null ? !secret.equals(that.secret) : that.secret != null) return false;
        if (server != null ? !server.equals(that.server) : that.server != null) return false;
        return farm != null ? farm.equals(that.farm) : that.farm == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (secret != null ? secret.hashCode() : 0);
        result = 31 * result + (server != null ? server.hashCode() : 0);
        result = 31 * result + (farm != null ? farm.hashCode() : 0);
        return result;
    }
}
