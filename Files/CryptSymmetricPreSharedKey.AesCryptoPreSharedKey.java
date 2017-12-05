package org.jssec.android.cryptsymmetricpresharedkey;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public final class AesCryptoPreSharedKey {

    // ���|�C���g1�� �����I�ɈÍ����[�h�ƃp�f�B���O��ݒ肷��
    // ���|�C���g2�� �Ǝ�łȂ�(��𖞂���)�Í��Z�p�i�A���S���Y���E���[�h�E�p�f�B���O���j���g�p����
    // Cipher �N���X�� getInstance �ɓn���p�����[�^ �i/[�Í��A���S���Y��]/[�u���b�N�Í����[�h]/[�p�f�B���O���[��])
    // �T���v���ł́A�Í��A���S���Y��=AES�A�u���b�N�Í����[�h=CBC�A�p�f�B���O���[��=PKCS7Padding
    private static final String TRANSFORMATION = "AES/CBC/PKCS7Padding";

    // �Í��A���S���Y��
    private static final String KEY_ALGORITHM = "AES";

    // IV�̃o�C�g��
    public static final int IV_LENGTH_BYTES = 16;

    // ���|�C���g3�� �\�����S�Ȓ����������𗘗p����
    // �����`�F�b�N
    private static final int MIN_KEY_LENGTH_BYTES = 16;

    private byte[] mIV = null;

    public byte[] getIV() {
        return mIV;
    }

    AesCryptoPreSharedKey(final byte[] iv) {
        mIV = iv;
    }

    AesCryptoPreSharedKey() {
    }

    public final byte[] encrypt(final byte[] keyData, final byte[] plain) {
        byte[] encrypted = null;

        try {
            // ���|�C���g1�� �����I�ɈÍ����[�h�ƃp�f�B���O��ݒ肷��
            // ���|�C���g2�� �Ǝ�łȂ�(��𖞂���)�Í��Z�p�i�A���S���Y���E���[�h�E�p�f�B���O���j���g�p����
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);

            SecretKey secretKey = generateKey(keyData);
            if (secretKey != null) {
                cipher.init(Cipher.ENCRYPT_MODE, secretKey);
                mIV = cipher.getIV();

                encrypted = cipher.doFinal(plain);
            }
        } catch (NoSuchAlgorithmException e) {
        } catch (NoSuchPaddingException e) {
        } catch (InvalidKeyException e) {
        } catch (IllegalBlockSizeException e) {
        } catch (BadPaddingException e) {
        } finally {
        }

        return encrypted;
    }

    public final byte[] decrypt(final byte[] keyData, final byte[] encrypted) {
        byte[] plain = null;

        try {
            // ���|�C���g1�� �����I�ɈÍ����[�h�ƃp�f�B���O��ݒ肷��
            // ���|�C���g2�� �Ǝ�łȂ�(��𖞂���)�Í��Z�p�i�A���S���Y���E���[�h�E�p�f�B���O���j���g�p����
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);

            SecretKey secretKey = generateKey(keyData);
            if (secretKey != null) {
                IvParameterSpec ivParameterSpec = new IvParameterSpec(mIV);
                cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);

                plain = cipher.doFinal(encrypted);
            }
        } catch (NoSuchAlgorithmException e) {
        } catch (NoSuchPaddingException e) {
        } catch (InvalidKeyException e) {
        } catch (InvalidAlgorithmParameterException e) {
        } catch (IllegalBlockSizeException e) {
        } catch (BadPaddingException e) {
        } finally {
        }

        return plain;
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
