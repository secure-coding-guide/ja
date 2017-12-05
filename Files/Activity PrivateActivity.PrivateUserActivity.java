package org.jssec.android.activity.privateactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class PrivateUserActivity extends Activity {

    private static final int REQUEST_CODE = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity);
    }
    
    public void onUseActivityClick(View view) {
        
        // ★ポイント6★ Activityに送信するIntentには、フラグFLAG_ACTIVITY_NEW_TASKを設定しない
        // ★ポイント7★ 同一アプリ内Activityはクラス指定の明示的Intentで呼び出す
        Intent intent = new Intent(this, PrivateActivity.class);
        
        // ★ポイント8★ 利用先アプリは同一アプリであるから、センシティブな情報をputExtra()を使う場合に限り送信してもよい
        intent.putExtra("PARAM", "センシティブな情報");
        
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) return;
        
        switch (requestCode) {
        case REQUEST_CODE:
            String result = data.getStringExtra("RESULT");
            
            // ★ポイント9★ 同一アプリ内Activityからの結果情報であっても、受信データの安全性を確認する
            // サンプルにつき割愛。「3.2 入力データの安全性を確認する」を参照。
            Toast.makeText(this, String.format("結果「%s」を受け取った。", result), Toast.LENGTH_LONG).show();
            break;
        }
    }
}
