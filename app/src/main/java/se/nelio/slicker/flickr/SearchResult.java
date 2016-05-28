package se.nelio.slicker.flickr;

public class SearchResult {

    public Photos photos;

    @Override
    public String toString() {
        return "SearchResult{" +
                "photos=" + photos +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final SearchResult that = (SearchResult) o;

        return photos != null ? photos.equals(that.photos) : that.photos == null;

    }

    @Override
    public int hashCode() {
        return photos != null ? photos.hashCode() : 0;
    }
}
