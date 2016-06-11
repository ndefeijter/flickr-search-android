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

    private final String mId;
    private final int mLongestSide;

    PhotoSize(final String id, final int longestSide) {
        this.mId = id;
        this.mLongestSide = longestSide;
    }

    public String id() {
        return mId;
    }

    public static PhotoSize forLongestSide(final int longestSide) {
        for (final PhotoSize candidate : values()) {
            if (candidate.mLongestSide > longestSide) {
                return candidate;
            }
        }
        return B;
    }
}
