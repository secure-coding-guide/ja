package org.jssec.android.cryptsymmetricpasswordbasedkey;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;

public final class AesCryptoPBEKey {

    // ���|�C���g1�� �����I�ɈÍ����[�h�ƃp�f�B���O��ݒ肷��
    // ���|�C���g2�� �Ǝ�łȂ�(��𖞂���)�Í��Z�p�i�A���S���Y���E���[�h�E�p�f�B���O���j���g�p����
    // Cipher �N���X�� getInstance �ɓn���p�����[�^ �i/[�Í��A���S���Y��]/[�u���b�N�Í����[�h]/[�p�f�B���O���[��])
    // �T���v���ł́A�Í��A���S���Y��=AES�A�u���b�N�Í����[�h=CBC�A�p�f�B���O���[��=PKCS7Padding
    private static final String TRANSFORMATION = "AES/CBC/PKCS7Padding";

    // ���𐶐�����N���X�̃C���X�^���X���擾���邽�߂̕�����
    private static final String KEY_GENERATOR_MODE = "PBEWITHSHA256AND128BITAES-CBC-BC";

    // ���|�C���g3�� �p�X���[�h���献�𐶐�����ꍇ�́ASalt���g�p����
    // Salt�̃o�C�g��
    public static final int SALT_LENGTH_BYTES = 20;

    // ���|�C���g4�� �p�X���[�h���献�𐶐�����ꍇ�́A�K���ȃn�b�V���̌J��Ԃ��񐔂��w�肷��
    // PBE �Ō��𐶐�����ۂ̝��a�̌J��Ԃ���
    private static final int KEY_GEN_ITERATION_COUNT = 1024;

    // ���|�C���g5�� �\�����S�Ȓ����������𗘗p����
    // ���̃r�b�g��
    private static final int KEY_LENGTH_BITS = 128;

    private byte[] mIV = null;
    private byte[] mSalt = null;

    public byte[] getIV() {
        return mIV;
    }

    public byte[] getSalt() {
        return mSalt;
    }

    AesCryptoPBEKey(final byte[] iv, final byte[] salt) {
        mIV = iv;
        mSalt = salt;
    }

    AesCryptoPBEKey() {
        mIV = null;
        initSalt();
    }

    private void initSalt() {
        mSalt = new byte[SALT_LENGTH_BYTES];
        SecureRandom sr = new SecureRandom();
        sr.nextBytes(mSalt);
    }

    public final byte[] encrypt(final byte[] plain, final char[] password) {
        byte[] encrypted = null;

        try {
            // ���|�C���g1�� �����I�ɈÍ����[�h�ƃp�f�B���O��ݒ肷��
            // ���|�C���g2�� �Ǝ�łȂ�(��𖞂���)�Í��Z�p�i�A���S���Y���E���[�h�E�p�f�B���O���j���g�p����
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);

            // ���|�C���g3�� �p�X���[�h���献�𐶐�����ꍇ�́ASalt���g�p����
            SecretKey secretKey = generateKey(password, mSalt);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            mIV = cipher.getIV();

            encrypted = cipher.doFinal(plain);
        } catch (NoSuchAlgorithmException e) {
        } catch (NoSuchPaddingException e) {
        } catch (InvalidKeyException e) {
        } catch (IllegalBlockSizeException e) {
        } catch (BadPaddingException e) {
        } finally {
        }

        return encrypted;
    }

    public final byte[] decrypt(final byte[] encrypted, final char[] password) {
        byte[] plain = null;

        try {
            // ���|�C���g1�� �����I�ɈÍ����[�h�ƃp�f�B���O��ݒ肷��
            // ���|�C���g2�� �Ǝ�łȂ�(��𖞂���)�Í��Z�p�i�A���S���Y���E���[�h�E�p�f�B���O���j���g�p����
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);

            // ���|�C���g3�� �p�X���[�h���献�𐶐�����ꍇ�́ASalt���g�p����
            SecretKey secretKey = generateKey(password, mSalt);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(mIV);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);

            plain = cipher.doFinal(encrypted);
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

    private static final SecretKey generateKey(final char[] password, final byte[] salt) {
        SecretKey secretKey = null;
        PBEKeySpec keySpec = null;

        try {
            // ���|�C���g2�� �Ǝ�łȂ�(��𖞂���)�A���S���Y���E���[�h�E�p�f�B���O���g�p����
            // ���𐶐�����N���X�̃C���X�^���X���擾����
            // ��ł́AAES-CBC 128 �r�b�g�p�̌��� SHA256 �𗘗p���Đ������� KeyFactory ���g�p�B
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(KEY_GENERATOR_MODE);

            // ���|�C���g3�� �p�X���[�h���献�𐶐�����ꍇ�́ASalt���g�p����
            // ���|�C���g4�� �p�X���[�h���献�𐶐�����ꍇ�́A�K���ȃn�b�V���̌J��Ԃ��񐔂��w�肷��
            // ���|�C���g5�� �\�����S�Ȓ����������𗘗p����
            keySpec = new PBEKeySpec(password, salt, KEY_GEN_ITERATION_COUNT, KEY_LENGTH_BITS);
            // password�̃N���A
            Arrays.fill(password, '?');
            // ���𐶐�����
            secretKey = secretKeyFactory.generateSecret(keySpec);
        } catch (NoSuchAlgorithmException e) {
        } catch (InvalidKeySpecException e) {
        } finally {
            keySpec.clearPassword();
        }

        return secretKey;
    }
}
