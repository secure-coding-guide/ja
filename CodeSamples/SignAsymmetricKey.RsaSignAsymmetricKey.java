package org.jssec.android.signasymmetrickey;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public final class RsaSignAsymmetricKey {

    // ���|�C���g1�� �����I�ɈÍ����[�h�ƃp�f�B���O��ݒ肷��
    // ���|�C���g2�� �Ǝ�łȂ�(��𖞂���)�Í��Z�p�i�A���S���Y���E���[�h�E�p�f�B���O���j���g�p����
    // Cipher �N���X�� getInstance �ɓn���p�����[�^ �i/[�Í��A���S���Y��]/[�u���b�N�Í����[�h]/[�p�f�B���O���[��])
    // �T���v���ł́A�Í��A���S���Y��=RSA�A�u���b�N�Í����[�h=NONE�A�p�f�B���O���[��=OAEPPADDING
    private static final String TRANSFORMATION = "SHA256withRSA";
    
    // �Í��A���S���Y��
    private static final String KEY_ALGORITHM = "RSA";
    
    // ���|�C���g3�� �\�����S�Ȓ����������𗘗p����
    // �����`�F�b�N
    private static final int MIN_KEY_LENGTH = 2000;

    RsaSignAsymmetricKey() {
    }
    
    public final byte[] sign(final byte[] plain, final byte[] keyData) {
        // �{���A���������̓T�[�o�[���Ŏ������ׂ����̂ł��邪�A
        // �{�T���v���ł͓���m�F�p�ɁA�A�v�����ł��������������������B
        // ���ۂɃT���v���R�[�h�𗘗p����ꍇ�́A�A�v�����ɔ閧����ێ����Ȃ��悤�ɂ��邱�ƁB

        byte[] sign = null;

        try {
            // ���|�C���g1�� �����I�ɈÍ����[�h�ƃp�f�B���O��ݒ肷��
            // ���|�C���g2�� �Ǝ�łȂ�(��𖞂���)�Í��Z�p�i�A���S���Y���E���[�h�E�p�f�B���O���j���g�p����
            Signature signature = Signature.getInstance(TRANSFORMATION);

            PrivateKey privateKey = generatePriKey(keyData);
            signature.initSign(privateKey);
            signature.update(plain);

            sign = signature.sign();
        } catch (NoSuchAlgorithmException e) {
        } catch (InvalidKeyException e) {
        } catch (SignatureException e) {
        } finally {
        }

        return sign;
    }

    public final boolean verify(final byte[] sign, final byte[] plain, final byte[] keyData) {

        boolean ret = false;
        
        try {
            // ���|�C���g1�� �����I�ɈÍ����[�h�ƃp�f�B���O��ݒ肷��
            // ���|�C���g2�� �Ǝ�łȂ�(��𖞂���)�Í��Z�p�i�A���S���Y���E���[�h�E�p�f�B���O���j���g�p����
            Signature signature = Signature.getInstance(TRANSFORMATION);

            PublicKey publicKey = generatePubKey(keyData);
            signature.initVerify(publicKey);                
            signature.update(plain);
            
            ret = signature.verify(sign);
                        
        } catch (NoSuchAlgorithmException e) {
        } catch (InvalidKeyException e) {
        } catch (SignatureException e) {
        } finally {
        }

        return ret;
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
