package org.jssec.android.privacypolicynocomprehensive;

import org.jssec.android.privacypolicynocomprehensive.R;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class WebViewAssetsActivity extends Activity {
    // ���|�C���g5�� �A�v���E�v���C�o�V�[�|���V�[�T�v�ł�assets�t�H���_���ɔz�u���Ă���
    private static final String ABST_PP_URL = "file:///android_asset/PrivacyPolicy/app-policy-abst-privacypolicy-1.0.html";
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        WebView webView = (WebView) findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();

        webSettings.setAllowFileAccess(false);

        webView.loadUrl(ABST_PP_URL);
    }
}
