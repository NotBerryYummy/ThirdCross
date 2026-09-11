package thirdcross;

public final class CrosshairSize {

    private CrosshairSize() {
    }

    public static float getBaselineSize(int width, int height) {
        int size = Math.max(width, height);
        return size > 0 ? size : 15.0f;
    }

    public static int toOddSize(float size) {
        if (!Float.isFinite(size) || size <= 0.0f) {
            return 0;
        }

        int rounded = Math.max(1, Math.round(size));

        if ((rounded & 1) == 0) {
            rounded--;
        }

        return Math.max(1, rounded);
    }
}
