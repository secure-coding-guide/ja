package org.jssec.android.service.privateservice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PrivateUserActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.privateservice_activity);
    }
    
    // サービス開始
    
    public void onStartServiceClick(View v) {
          // ★ポイント4★ 同一アプリ内Serviceはクラス指定の明示的Intentで呼び出す                
        Intent intent = new Intent(this, PrivateStartService.class);
          
          // ★ポイント5★ 利用先アプリは同一アプリであるから、センシティブな情報を送信してもよい
        intent.putExtra("PARAM", "センシティブな情報");

        startService(intent);
    }
    
    // サービス停止ボタン
    public void onStopServiceClick(View v) {
        doStopService();
    }
        
    @Override
    public void onStop(){
        super.onStop();
        // サービスが終了していない場合は終了する
        doStopService();
    }
    // サービスを停止する
    private void doStopService() {
        // ★ポイント4★ 同一アプリ内Serviceはクラス指定の明示的Intentで呼び出す                
        Intent intent = new Intent(this, PrivateStartService.class);
        stopService(intent);        
    }

    // IntentService 開始ボタン

    public void onIntentServiceClick(View v) {
          // ★ポイント4★ 同一アプリ内Serviceはクラス指定の明示的Intentで呼び出す                
        Intent intent = new Intent(this, PrivateIntentService.class);
          
          // ★ポイント5★ 利用先アプリは同一アプリであるから、センシティブな情報を送信してもよい
        intent.putExtra("PARAM", "センシティブな情報");

        startService(intent);
    }
        
}
