package org.jssec.android.activity.privateactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class PrivateActivity extends Activity {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.private_activity);

        // ★ポイント4★ 同一アプリからのIntentであっても、受信Intentの安全性を確認する
        // サンプルにつき割愛。「3.2 入力データの安全性を確認する」を参照。
        String param = getIntent().getStringExtra("PARAM");
        Toast.makeText(this, String.format("パラメータ「%s」を受け取った。", param), Toast.LENGTH_LONG).show();
    }

    public void onReturnResultClick(View view) {
        
        // ★ポイント5★ 利用元アプリは同一アプリであるから、センシティブな情報を返送してよい
        Intent intent = new Intent();
        intent.putExtra("RESULT", "センシティブな情報");
        setResult(RESULT_OK, intent);
        finish();
    }
}
