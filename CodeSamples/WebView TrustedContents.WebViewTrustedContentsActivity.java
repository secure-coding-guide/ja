package org.jssec.webview.trustedcontents;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.http.SslCertificate;
import android.net.http.SslError;
import android.os.Bundle;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.text.SimpleDateFormat;

public class WebViewTrustedContentsActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        WebView webView = (WebView) findViewById(R.id.webView);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view,
                    SslErrorHandler handler, SslError error) {
                // ���|�C���g1�� WebView��SSL�ʐM�G���[��K�؂Ƀn���h�����O����
                // SSL�G���[�����������ꍇ�ɂ́ASSL�G���[�����������|�����[�U�ɒʒm����
                AlertDialog dialog = createSslErrorDialog(error);
                dialog.show();

                // ���|�C���g1�� WebView��SSL�ʐM�G���[��K�؂Ƀn���h�����O����
                // SSL�G���[�����������ꍇ�A�L�������؂�ȂǏؖ����ɕs�������邩�A
                // �������͒��ԎҍU�����󂯂Ă���\��������̂ŁA���S�̂��߂ɐڑ��𒆎~����B
                handler.cancel();
            }
        });

        // ���|�C���g2�� WebView��JavaScript��L���ɂ��Ă��悢
        // �ȉ��̃R�[�h�ł́AloadUrl()�Ŏ��ЊǗ��R���e���c��ǂ݂��ނ��Ƃ�z�肵�Ă���B
        webView.getSettings().setJavaScriptEnabled(true);

        // ���|�C���g3�� WebView�ŕ\������URL��HTTPS�v���g�R�������Ɍ��肷��
        // ���|�C���g4�� WebView�ŕ\������URL�����ЊǗ��R���e���c�����Ɍ��肷��
        webView.loadUrl("https://url.to.your.contents/");
    }

    private AlertDialog createSslErrorDialog(SslError error) {
        // �_�C�A���O�ɕ\������G���[���b�Z�[�W
        String errorMsg = createErrorMessage(error);
        // �_�C�A���O��OK�{�^���������̋���
        DialogInterface.OnClickListener onClickOk = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setResult(RESULT_OK);
            }
        };
        // �_�C�A���O�̍쐬
        AlertDialog dialog = new AlertDialog.Builder(
                WebViewTrustedContentsActivity.this).setTitle("SSL�ڑ��G���[")
                .setMessage(errorMsg).setPositiveButton("OK", onClickOk)
                .create();
        return dialog;
    }

    private String createErrorMessage(SslError error) {
        SslCertificate cert = error.getCertificate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        StringBuilder result = new StringBuilder()
        .append("�T�C�g�̃Z�L�����e�B�ؖ������M���ł��܂���B�ڑ����I�����܂����B\n\n�G���[�̌���\n");
        switch (error.getPrimaryError()) {
        case SslError.SSL_EXPIRED:
            result.append("�ؖ����̗L���������؂�Ă��܂��B\n\n�I������=")
            .append(dateFormat.format(cert.getValidNotAfterDate()));
            return result.toString();
        case SslError.SSL_IDMISMATCH:
            result.append("�z�X�g������v���܂���B\n\nCN=")
            .append(cert.getIssuedTo().getCName());
            return result.toString();
        case SslError.SSL_NOTYETVALID:
            result.append("�ؖ����͂܂��L���ł͂���܂���\n\n�J�n����=")
            .append(dateFormat.format(cert.getValidNotBeforeDate()));
            return result.toString();
        case SslError.SSL_UNTRUSTED:
            result.append("�ؖ����𔭍s�����F�؋ǂ��M���ł��܂���\n\n�F�؋�\n")
            .append(cert.getIssuedBy().getDName());
            return result.toString();
        default:
            result.append("�����s���̃G���[���������܂���");
            return result.toString();
        }
    }



}
