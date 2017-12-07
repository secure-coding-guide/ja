package org.jssec.android.broadcast.privatereceiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class PrivateSenderActivity extends Activity {

    public void onSendNormalClick(View view) {
        // ★ポイント4★ 同一アプリ内Receiverはクラス指定の明示的IntentでBroadcast送信する
        Intent intent = new Intent(this, PrivateReceiver.class);

        // ★ポイント5★ 送信先は同一アプリ内Receiverであるため、センシティブな情報を送信してよい
        intent.putExtra("PARAM", "センシティブな情報 from Sender");
        sendBroadcast(intent);
    }
    
    public void onSendOrderedClick(View view) {
        // ★ポイント4★ 同一アプリ内Receiverはクラス指定の明示的IntentでBroadcast送信する
        Intent intent = new Intent(this, PrivateReceiver.class);

        // ★ポイント5★ 送信先は同一アプリ内Receiverであるため、センシティブな情報を送信してよい
        intent.putExtra("PARAM", "センシティブな情報 from Sender");
        sendOrderedBroadcast(intent, null, mResultReceiver, null, 0, null, null);
    }
    
    private BroadcastReceiver mResultReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            
            // ★ポイント6★ 同一アプリ内Receiverからの結果情報であっても、受信データの安全性を確認する
            // サンプルにつき割愛。「3.2 入力データの安全性を確認する」を参照。
            String data = getResultData();
            PrivateSenderActivity.this.logLine(
                    String.format("結果「%s」を受信した。", data));
        }
    };
    
    private TextView mLogView;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mLogView = (TextView)findViewById(R.id.logview);
    }
    
    private void logLine(String line) {
        mLogView.append(line);
        mLogView.append("\n");
    }
}
