package scorex.util;

public class Arrays {

    private Arrays() {}

    public static byte[] copyOf(byte[] original, int newLength) {
        byte[] copy = new byte[newLength];
        System.arraycopy(original, 0, copy, 0,
                         Math.min(original.length, newLength));
        return copy;
    }

    public static void fill(int[] a, int val) {
        for (int i = 0, len = a.length; i < len; i++)
            a[i] = val;
    }
}
