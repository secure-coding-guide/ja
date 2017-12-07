package org.jssec.android.activity.publicactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class PublicActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // ★ポイント2★ 受信Intentの安全性を確認する
        // 公開Activityであるため利用元アプリがマルウェアである可能性がある。
        // サンプルにつき割愛。「3.2 入力データの安全性を確認する」を参照。
        String param = getIntent().getStringExtra("PARAM");
        Toast.makeText(this, String.format("パラメータ「%s」を受け取った。", param), Toast.LENGTH_LONG).show();
    }

    public void onReturnResultClick(View view) {
        
        // ★ポイント3★ 結果を返す場合、センシティブな情報を含めない
        // 公開Activityであるため利用元アプリがマルウェアである可能性がある。
        // マルウェアに取得されても問題のない情報であれば結果として返してもよい。

        Intent intent = new Intent();
        intent.putExtra("RESULT", "センシティブではない情報");
        setResult(RESULT_OK, intent);
        finish();
    }
}
