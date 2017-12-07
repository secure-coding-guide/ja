package org.jssec.android.signsymmetricpresharedkey;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public final class HmacPreSharedKey {

    // ���|�C���g1�� �����I�ɈÍ����[�h�ƃp�f�B���O��ݒ肷��
    // ���|�C���g2�� �Ǝ�łȂ�(��𖞂���)�Í��Z�p�i�A���S���Y���E���[�h�E�p�f�B���O���j���g�p����
    // Mac �N���X�� getInstance �ɓn���p�����[�^ �i�F�؃��[�h)
    private static final String TRANSFORMATION = "HmacSHA256";

    // �Í��A���S���Y��
    private static final String KEY_ALGORITHM = "HmacSHA256";

    // ���|�C���g3�� �\�����S�Ȓ����������𗘗p����
    // �����`�F�b�N
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
            // ���|�C���g1�� �����I�ɈÍ����[�h�ƃp�f�B���O��ݒ肷��
            // ���|�C���g2�� �Ǝ�łȂ�(��𖞂���)�Í��Z�p�i�A���S���Y���E���[�h�E�p�f�B���O���j���g�p����
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
            // ���|�C���g3�� �\�����S�Ȓ����������𗘗p����
            if (keyData.length >= MIN_KEY_LENGTH_BYTES) {
                // ���|�C���g2�� �Ǝ�łȂ�(��𖞂���)�Í��Z�p�i�A���S���Y���E���[�h�E�p�f�B���O���j���g�p����
                secretKey = new SecretKeySpec(keyData, KEY_ALGORITHM);
            }
        } catch (IllegalArgumentException e) {
        } finally {
        }

        return secretKey;
    }
}
