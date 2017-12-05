package org.jssec.android.cryptasymmetrickey;

public class Utils {

    public static final byte[] decodeHex(String src) {
        // ’·‚³‚ª‹ô”‚Å‚È‚¢ê‡‚Í¸”s
        if (src.length() % 2 != 0) {
            return null;
        }
        byte[] buf = new byte[src.length() / 2];
        for (int i = 0; i < src.length(); i += 2) {
            byte b1 = Byte.parseByte(src.substring(i, i + 1), 16);
            byte b2 = Byte.parseByte(src.substring(i + 1, i + 2), 16);
            buf[i / 2] = b2;
            buf[i / 2] |= b1 << 4;
        }
        return buf;
    }

    public static String encodeHex(byte[] data) {
        if (data == null)
            return null;
        final String digit = "0123456789ABCDEF";
        StringBuilder sb = new StringBuilder();
        for (byte b : data) {
            int h = (b >> 4) & 15;
            int l = b & 15;
            sb.append(digit.charAt(h));
            sb.append(digit.charAt(l));
        }
        return sb.toString();
    }

}
