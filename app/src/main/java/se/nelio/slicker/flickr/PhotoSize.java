package se.nelio.slicker.flickr;

/**
 * https://www.flickr.com/services/api/misc.urls.html
 *
 * m	small, 240 on longest side
 * s	small square 75x75
 * t	thumbnail, 100 on longest side
 * z	medium 640, 640 on longest side
 * b	large, 1024 on longest side
 */
public enum PhotoSize {

    M("m"),
    S("s"),
    T("t"),
    Z("z"),
    B("b");

    private final String id;

    PhotoSize(final String id) {
        this.id = id;
    }

    public String id() {
        return id;
    }
}
