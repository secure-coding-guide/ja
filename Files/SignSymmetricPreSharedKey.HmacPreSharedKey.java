package org.jssec.android.signsymmetricpresharedkey;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public final class HmacPreSharedKey {

    // ★ポイント1★ 明示的に暗号モードとパディングを設定する
    // ★ポイント2★ 脆弱でない(基準を満たす)暗号技術（アルゴリズム・モード・パディング等）を使用する
    // Mac クラスの getInstance に渡すパラメータ （認証モード)
    private static final String TRANSFORMATION = "HmacSHA256";

    // 暗号アルゴリズム
    private static final String KEY_ALGORITHM = "HmacSHA256";

    // ★ポイント3★ 十分安全な長さを持つ鍵を利用する
    // 鍵長チェック
    private static final int MIN_KEY_LENGTH_BYTES = 16;

    HmacPreSharedKey() {
    }

    public final byte[] sign(final byte[] plain, final byte[] keyData) {
        return calculate(plain, keyData);
    }

    public final byte[] calculate(final byte[] plain, final byte[] keyData) {
        byte[] hmac = null;
        int count = 0;

        try {
            // ★ポイント1★ 明示的に暗号モードとパディングを設定する
            // ★ポイント2★ 脆弱でない(基準を満たす)暗号技術（アルゴリズム・モード・パディング等）を使用する
            Mac mac = Mac.getInstance(TRANSFORMATION);

            SecretKey secretKey = generateKey(keyData);
            if (secretKey != null) {
                mac.init(secretKey);

                hmac = mac.doFinal(plain);

                StringBuilder sb = new StringBuilder();

                for (int i = 0; i < hmac.length; i++) {
                    //System.out.println(Integer.toHexString(hmac[i] & 0xff));
                    sb.append(Integer.toHexString(hmac[i] & 0xff));
                    count++;
                }

                count = 0;
            }
        } catch (NoSuchAlgorithmException e) {
        } catch (InvalidKeyException e) {
        } finally {
        }

        return hmac;
    }

    public final boolean verify(final byte[] hmac, final byte[] plain, final byte[] keyData) {
        byte[] hmacForPlain = calculate(plain, keyData);

        if (hmacForPlain != null && Arrays.equals(hmac, hmacForPlain)) {
            return true;
        }

        return false;
    }

    private static final SecretKey generateKey(final byte[] keyData) {
        SecretKey secretKey = null;

        try {
            // ★ポイント3★ 十分安全な長さを持つ鍵を利用する
            if (keyData.length >= MIN_KEY_LENGTH_BYTES) {
                // ★ポイント2★ 脆弱でない(基準を満たす)暗号技術（アルゴリズム・モード・パディング等）を使用する
                secretKey = new SecretKeySpec(keyData, KEY_ALGORITHM);
            }
        } catch (IllegalArgumentException e) {
        } finally {
        }

        return secretKey;
    }
}
