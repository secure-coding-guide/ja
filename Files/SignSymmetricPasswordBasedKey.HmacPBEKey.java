package org.jssec.android.signsymmetricpasswordbasedkey;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public final class HmacPBEKey {

    // ���|�C���g1�� �����I�ɈÍ����[�h�ƃp�f�B���O��ݒ肷��
    // ���|�C���g2�� �Ǝ�łȂ�(��𖞂���)�Í��Z�p�i�A���S���Y���E���[�h�E�p�f�B���O���j���g�p����
    // Mac �N���X�� getInstance �ɓn���p�����[�^ �i�F�؃��[�h)
    private static final String TRANSFORMATION = "PBEWITHHMACSHA1";

    // ���𐶐�����N���X�̃C���X�^���X���擾���邽�߂̕�����
    private static final String KEY_GENERATOR_MODE = "PBEWITHHMACSHA1";

    // ���|�C���g3�� �p�X���[�h���献�𐶐�����ꍇ�́ASalt���g�p����
    // Salt�̃o�C�g��
    public static final int SALT_LENGTH_BYTES = 20;

    // ���|�C���g4�� �p�X���[�h���献�𐶐�����ꍇ�́A�K���ȃn�b�V���̌J��Ԃ��񐔂��w�肷��
    // PBE �Ō��𐶐�����ۂ̝��a�̌J��Ԃ���
    private static final int KEY_GEN_ITERATION_COUNT = 1024;

    // ���|�C���g5�� �\�����S�Ȓ����������𗘗p����
    // ���̃r�b�g��
    private static final int KEY_LENGTH_BITS = 160;

    private byte[] mSalt = null;

    public byte[] getSalt() {
        return mSalt;
    }

    HmacPBEKey() {
        initSalt();
    }

    HmacPBEKey(final byte[] salt) {
        mSalt = salt;
    }

    private void initSalt() {
        mSalt = new byte[SALT_LENGTH_BYTES];
        SecureRandom sr = new SecureRandom();
        sr.nextBytes(mSalt);
    }

    public final byte[] sign(final byte[] plain, final char[] password) {
        return calculate(plain, password);
    }

    private final byte[] calculate(final byte[] plain, final char[] password) {
        byte[] hmac = null;

        try {
            // ���|�C���g1�� �����I�ɈÍ����[�h�ƃp�f�B���O��ݒ肷��
            // ���|�C���g2�� �Ǝ�łȂ�(��𖞂���)�Í��Z�p�i�A���S���Y���E���[�h�E�p�f�B���O���j���g�p����
            Mac mac = Mac.getInstance(TRANSFORMATION);

            // ���|�C���g3�� �p�X���[�h���献�𐶐�����ꍇ�́ASalt���g�p����
            SecretKey secretKey = generateKey(password, mSalt);
            mac.init(secretKey);

            hmac = mac.doFinal(plain);
        } catch (NoSuchAlgorithmException e) {
        } catch (InvalidKeyException e) {
        } finally {
        }

        return hmac;
    }

    public final boolean verify(final byte[] hmac, final byte[] plain, final char[] password) {

        byte[] hmacForPlain = calculate(plain, password);

        if (Arrays.equals(hmac, hmacForPlain)) {
            return true;
        }
        return false;
    }

    private static final SecretKey generateKey(final char[] password, final byte[] salt) {
        SecretKey secretKey = null;
        PBEKeySpec keySpec = null;

        try {
            // ���|�C���g2�� �Ǝ�łȂ�(��𖞂���)�A���S���Y���E���[�h�E�p�f�B���O���g�p����
            // ���𐶐�����N���X�̃C���X�^���X���擾����
            // ��ł́AAES-CBC 128 �r�b�g�p�̌��� SHA1 �𗘗p���Đ������� KeyFactory ���g�p�B
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
