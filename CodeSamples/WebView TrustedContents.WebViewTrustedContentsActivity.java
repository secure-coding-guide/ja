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
                // ★ポイント1★ WebViewのSSL通信エラーを適切にハンドリングする
                // SSLエラーが発生した場合には、SSLエラーが発生した旨をユーザに通知する
                AlertDialog dialog = createSslErrorDialog(error);
                dialog.show();

                // ★ポイント1★ WebViewのSSL通信エラーを適切にハンドリングする
                // SSLエラーが発生した場合、有効期限切れなど証明書に不備があるか、
                // もしくは中間者攻撃を受けている可能性があるので、安全のために接続を中止する。
                handler.cancel();
            }
        });

        // ★ポイント2★ WebViewのJavaScriptを有効にしてもよい
        // 以下のコードでは、loadUrl()で自社管理コンテンツを読みこむことを想定している。
        webView.getSettings().setJavaScriptEnabled(true);

        // ★ポイント3★ WebViewで表示するURLをHTTPSプロトコルだけに限定する
        // ★ポイント4★ WebViewで表示するURLを自社管理コンテンツだけに限定する
        webView.loadUrl("https://url.to.your.contents/");
    }

    private AlertDialog createSslErrorDialog(SslError error) {
        // ダイアログに表示するエラーメッセージ
        String errorMsg = createErrorMessage(error);
        // ダイアログのOKボタン押下時の挙動
        DialogInterface.OnClickListener onClickOk = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setResult(RESULT_OK);
            }
        };
        // ダイアログの作成
        AlertDialog dialog = new AlertDialog.Builder(
                WebViewTrustedContentsActivity.this).setTitle("SSL接続エラー")
                .setMessage(errorMsg).setPositiveButton("OK", onClickOk)
                .create();
        return dialog;
    }

    private String createErrorMessage(SslError error) {
        SslCertificate cert = error.getCertificate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        StringBuilder result = new StringBuilder()
        .append("サイトのセキュリティ証明書が信頼できません。接続を終了しました。\n\nエラーの原因\n");
        switch (error.getPrimaryError()) {
        case SslError.SSL_EXPIRED:
            result.append("証明書の有効期限が切れています。\n\n終了時刻=")
            .append(dateFormat.format(cert.getValidNotAfterDate()));
            return result.toString();
        case SslError.SSL_IDMISMATCH:
            result.append("ホスト名が一致しません。\n\nCN=")
            .append(cert.getIssuedTo().getCName());
            return result.toString();
        case SslError.SSL_NOTYETVALID:
            result.append("証明書はまだ有効ではありません\n\n開始時刻=")
            .append(dateFormat.format(cert.getValidNotBeforeDate()));
            return result.toString();
        case SslError.SSL_UNTRUSTED:
            result.append("証明書を発行した認証局が信頼できません\n\n認証局\n")
            .append(cert.getIssuedBy().getDName());
            return result.toString();
        default:
            result.append("原因不明のエラーが発生しました");
            return result.toString();
        }
    }



}
