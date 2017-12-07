package org.jssec.android.https.vulnerables;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.conn.ssl.SSLSocketFactory;

public class VulnerableSamples {
    public void emptyTrustManager() throws IOException, KeyManagementException, NoSuchAlgorithmException {
        TrustManager tm = new X509TrustManager() {
            
            @Override
            public void checkClientTrusted(X509Certificate[] chain,
                    String authType) throws CertificateException {
                // �������Ȃ� �� �ǂ�ȏؖ����ł���t����
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain,
                    String authType) throws CertificateException {
                // �������Ȃ� �� �ǂ�ȏؖ����ł���t����
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
    }
    public void emptyHostnameVerifier() {
        HostnameVerifier hv = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                // ���true��Ԃ� �� �ǂ�ȃz�X�g���ł���t����
                return true;
            }
        };
    }
    
    public void allowAllHostnameVerifier() {
        SSLSocketFactory sf = null;
        
        sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
    }
}
