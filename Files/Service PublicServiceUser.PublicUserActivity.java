package org.jssec.android.service.publicserviceuser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PublicUserActivity extends Activity {

    // 利用先Service情報
    private static final String TARGET_PACKAGE = "org.jssec.android.service.publicservice";
    private static final String TARGET_START_CLASS = "org.jssec.android.service.publicservice.PublicStartService";
    private static final String TARGET_INTENT_CLASS = "org.jssec.android.service.publicservice.PublicIntentService";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.publicservice_activity);
    }
    
    // サービス開始
    public void onStartServiceClick(View v) {              

        Intent intent = new Intent("org.jssec.android.service.publicservice.action.startservice");

        // ★ポイント4★ Serviceは明示的Intentで呼び出す
        intent.setClassName(TARGET_PACKAGE, TARGET_START_CLASS);
          
        // ★ポイント5★ センシティブな情報を送信してはならない
        intent.putExtra("PARAM", "センシティブではない情報");

        startService(intent);
        
     // ★ポイント6★ 結果を受け取る場合、結果データの安全性を確認する
     // 本サンプルは startService()を使ったService利用の例の為、結果情報は受け取らない
    }
    
    // サービス停止ボタン
    public void onStopServiceClick(View v) {
        doStopService();
    }
        
    // IntentService 開始ボタン

    public void onIntentServiceClick(View v) {      
        Intent intent = new Intent("org.jssec.android.service.publicservice.action.intentservice");

        // ★ポイント4★ Serviceは明示的Intentで呼び出す
        intent.setClassName(TARGET_PACKAGE, TARGET_INTENT_CLASS);

        // ★ポイント5★ センシティブな情報を送信してはならない
        intent.putExtra("PARAM", "センシティブではない情報");

        startService(intent);
    }
        
    @Override
    public void onStop(){
        super.onStop();
        // サービスが終了していない場合は終了する
        doStopService();
    }
    
    // サービスを停止する
    private void doStopService() {            
        Intent intent = new Intent("org.jssec.android.service.publicservice.action.startservice");

        // ★ポイント4★ Serviceは明示的Intentで呼び出す
        intent.setClassName(TARGET_PACKAGE, TARGET_START_CLASS);

        stopService(intent);        
    }
}
