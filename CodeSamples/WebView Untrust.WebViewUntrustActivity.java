package org.jssec.webview.untrust;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.http.SslCertificate;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

import java.text.SimpleDateFormat;

public class WebViewUntrustActivity extends Activity {
    /*
     * ���ЊǗ��ȊO�̃R���e���c��\������ (�ȈՃu���E�U�Ƃ��ċ@�\����T���v���v���O����)
     */

    private EditText textUrl;
    private Button buttonGo;
    private WebView webView;


    // ���� Activity ���Ǝ��� URL ���N�G�X�g���n���h�����O�ł���悤�ɂ��邽�߂ɒ�`
    private class WebViewUnlimitedClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String url) {
            webView.loadUrl(url);
            textUrl.setText(url);
            return true;
        }

        // Web�y�[�W�̓ǂݍ��݊J�n����
        @Override
        public void onPageStarted(WebView webview, String url, Bitmap favicon) {
            buttonGo.setEnabled(false);
            textUrl.setText(url);
        }

        // SSL�ʐM�Ŗ�肪����ƃG���[�_�C�A���O��\�����A
        // �ڑ��𒆎~����
        @Override
        public void onReceivedSslError(WebView webview,
                SslErrorHandler handler, SslError error) {
            // ���|�C���g 1�� HTTPS �ʐM�̏ꍇ�ɂ�SSL�ʐM�̃G���[��K�؂Ƀn���h�����O����
            AlertDialog errorDialog = createSslErrorDialog(error);
            errorDialog.show();
            handler.cancel();
            textUrl.setText(webview.getUrl());
            buttonGo.setEnabled(true);
        }

        // Web�y�[�W��load���I�������\�����ꂽ�y�[�W��URL��EditText�ɕ\��������
        @Override
        public void onPageFinished(WebView webview, String url) {
            textUrl.setText(url);
            buttonGo.setEnabled(true);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        webView = (WebView) findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewUnlimitedClient());

        // ���|�C���g 2�� JavaScript��L���ɂ��Ȃ�
        // �f�t�H���g�̐ݒ��JavaScript�����ƂȂ��Ă��邪�A�����I�ɖ���������
        webView.getSettings().setJavaScriptEnabled(false);

        webView.loadUrl(getString(R.string.texturl));
        textUrl = (EditText) findViewById(R.id.texturl);
        buttonGo = (Button) findViewById(R.id.go);
    }

    public void onClickButtonGo(View v) {
        webView.loadUrl(textUrl.getText().toString());
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
                WebViewUntrustActivity.this).setTitle("SSL�ڑ��G���[")
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
