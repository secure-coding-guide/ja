package org.jssec.android.cryptasymmetrickey;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public final class RsaCryptoAsymmetricKey {

    // ���|�C���g1�� �����I�ɈÍ����[�h�ƃp�f�B���O��ݒ肷��
    // ���|�C���g2�� �Ǝ�łȂ�(��𖞂���)�Í��Z�p�i�A���S���Y���E���[�h�E�p�f�B���O���j���g�p����
    // Cipher �N���X�� getInstance �ɓn���p�����[�^ �i/[�Í��A���S���Y��]/[�u���b�N�Í����[�h]/[�p�f�B���O���[��])
    // �T���v���ł́A�Í��A���S���Y��=RSA�A�u���b�N�Í����[�h=NONE�A�p�f�B���O���[��=OAEPPADDING
    private static final String TRANSFORMATION = "RSA/NONE/OAEPPADDING";

    // �Í��A���S���Y��
    private static final String KEY_ALGORITHM = "RSA";

    // ���|�C���g3�� �\�����S�Ȓ����������𗘗p����
    // �����`�F�b�N
    private static final int MIN_KEY_LENGTH = 2000;

    RsaCryptoAsymmetricKey() {
    }

    public final byte[] encrypt(final byte[] plain, final byte[] keyData) {
        byte[] encrypted = null;

        try {
            // ���|�C���g1�� �����I�ɈÍ����[�h�ƃp�f�B���O��ݒ肷��
            // ���|�C���g2�� �Ǝ�łȂ�(��𖞂���)�Í��Z�p�i�A���S���Y���E���[�h�E�p�f�B���O���j���g�p����
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);

            PublicKey publicKey = generatePubKey(keyData);
            if (publicKey != null) {
                cipher.init(Cipher.ENCRYPT_MODE, publicKey);
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

    public final byte[] decrypt(final byte[] encrypted, final byte[] keyData) {
        // �{���A���������̓T�[�o�[���Ŏ������ׂ����̂ł��邪�A
        // �{�T���v���ł͓���m�F�p�ɁA�A�v�����ł��������������������B
        // ���ۂɃT���v���R�[�h�𗘗p����ꍇ�́A�A�v�����ɔ閧����ێ����Ȃ��悤�ɂ��邱�ƁB
        
        byte[] plain = null;

        try {
            // ���|�C���g1�� �����I�ɈÍ����[�h�ƃp�f�B���O��ݒ肷��
            // ���|�C���g2�� �Ǝ�łȂ�(��𖞂���)�Í��Z�p�i�A���S���Y���E���[�h�E�p�f�B���O���j���g�p����
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);

            PrivateKey privateKey = generatePriKey(keyData);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            plain = cipher.doFinal(encrypted);
        } catch (NoSuchAlgorithmException e) {
        } catch (NoSuchPaddingException e) {
        } catch (InvalidKeyException e) {
        } catch (IllegalBlockSizeException e) {
        } catch (BadPaddingException e) {
        } finally {
        }

        return plain;
    }

    private static final PublicKey generatePubKey(final byte[] keyData) {
        PublicKey publicKey = null;
        KeyFactory keyFactory = null;

        try {
            keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(keyData));
        } catch (IllegalArgumentException e) {
        } catch (NoSuchAlgorithmException e) {
        } catch (InvalidKeySpecException e) {
        } finally {
        }

        // ���|�C���g3�� �\�����S�Ȓ����������𗘗p����
        // �����̃`�F�b�N
        if (publicKey instanceof RSAPublicKey) {
            int len = ((RSAPublicKey) publicKey).getModulus().bitLength();
            if (len < MIN_KEY_LENGTH) {
                publicKey = null;
            }
        }

        return publicKey;
    }

    private static final PrivateKey generatePriKey(final byte[] keyData) {
        PrivateKey privateKey = null;
        KeyFactory keyFactory = null;

        try {
            keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(keyData));
        } catch (IllegalArgumentException e) {
        } catch (NoSuchAlgorithmException e) {
        } catch (InvalidKeySpecException e) {
        } finally {
        }

        return privateKey;
    }
}
