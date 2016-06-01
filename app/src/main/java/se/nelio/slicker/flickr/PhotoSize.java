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

    S("s", 75),
    T("t", 100),
    M("m", 240),
    Z("z", 640),
    B("b", 1024);

    private final String id;
    private final int longestSide;

    PhotoSize(final String id, final int longestSide) {
        this.id = id;
        this.longestSide = longestSide;
    }

    public String id() {
        return id;
    }

    public static PhotoSize forWidth(final int width) {
        for (final PhotoSize candidate : values()) {
            if (candidate.longestSide > width) {
                return candidate;
            }
        }
        return B;
    }
}
