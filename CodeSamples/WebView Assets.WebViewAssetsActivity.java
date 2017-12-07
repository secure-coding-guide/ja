package org.jssec.webview.assets;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class WebViewAssetsActivity extends Activity {
    /**
     * assets���̃R���e���c��\������
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WebView webView = (WebView) findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();

        // ���|�C���g1�� assets��res�f�B���N�g���ȊO�̏ꏊ�ɔz�u�����t�@�C���ւ̃A�N�Z�X���֎~�ɂ���
        webSettings.setAllowFileAccess(false);

        // ���|�C���g2�� JavaScript��L���ɂ��Ă悢
        webSettings.setJavaScriptEnabled(true);

        // assets���ɔz�u�����R���e���c��\������
        webView.loadUrl("file:///android_asset/sample/index.html");
    }
}
