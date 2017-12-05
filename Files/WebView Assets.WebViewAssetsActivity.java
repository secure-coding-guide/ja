package org.jssec.webview.assets;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class WebViewAssetsActivity extends Activity {
    /**
     * assets内のコンテンツを表示する
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WebView webView = (WebView) findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();

        // ★ポイント1★ assetsとresディレクトリ以外の場所に配置したファイルへのアクセスを禁止にする
        webSettings.setAllowFileAccess(false);

        // ★ポイント2★ JavaScriptを有効にしてよい
        webSettings.setJavaScriptEnabled(true);

        // assets内に配置したコンテンツを表示する
        webView.loadUrl("file:///android_asset/sample/index.html");
    }
}
