package se.nelio.slicker.flickr;

import java.util.List;

public class Photos {
    public int page;
    public int pages;
    public int perpage;
    public int total;
    public List<Photo> photo;

    @Override
    public String toString() {
        return "Photos{" +
                "page=" + page +
                ", pages=" + pages +
                ", perpage=" + perpage +
                ", total=" + total +
                ", photo=" + photo +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Photos that = (Photos) o;

        if (page != that.page) return false;
        if (pages != that.pages) return false;
        if (perpage != that.perpage) return false;
        if (total != that.total) return false;
        return photo != null ? photo.equals(that.photo) : that.photo == null;

    }

    @Override
    public int hashCode() {
        int result = page;
        result = 31 * result + pages;
        result = 31 * result + perpage;
        result = 31 * result + total;
        result = 31 * result + (photo != null ? photo.hashCode() : 0);
        return result;
    }
}
