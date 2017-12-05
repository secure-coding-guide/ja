package org.jssec.android.broadcast.publicsender;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class PublicSenderActivity extends Activity {
    
    private static final String MY_BROADCAST_PUBLIC =
        "org.jssec.android.broadcast.MY_BROADCAST_PUBLIC";
    
    public void onSendNormalClick(View view) {
        // ★ポイント4★ センシティブな情報を送信してはならない
        Intent intent = new Intent(MY_BROADCAST_PUBLIC);
        intent.putExtra("PARAM", "センシティブではない情報 from Sender");
        sendBroadcast(intent);
    }
    
    public void onSendOrderedClick(View view) {
        // ★ポイント4★ センシティブな情報を送信してはならない
        Intent intent = new Intent(MY_BROADCAST_PUBLIC);
        intent.putExtra("PARAM", "センシティブではない情報 from Sender");
        sendOrderedBroadcast(intent, null, mResultReceiver, null, 0, null, null);
    }
    
    public void onSendStickyClick(View view) {
        // ★ポイント4★ センシティブな情報を送信してはならない
        Intent intent = new Intent(MY_BROADCAST_PUBLIC);
        intent.putExtra("PARAM", "センシティブではない情報 from Sender");
        //sendStickyBroadcastメソッドはAPI Level 21でdeprecatedとなった
        sendStickyBroadcast(intent);
    }

    public void onSendStickyOrderedClick(View view) {
        // ★ポイント4★ センシティブな情報を送信してはならない
        Intent intent = new Intent(MY_BROADCAST_PUBLIC);
        intent.putExtra("PARAM", "センシティブではない情報 from Sender");
        //sendStickyBroadcastメソッドはAPI Level 21でdeprecatedとなった
        sendStickyOrderedBroadcast(intent, mResultReceiver, null, 0, null, null);
    }
    
    public void onRemoveStickyClick(View view) {
        Intent intent = new Intent(MY_BROADCAST_PUBLIC);
        //removeStickyBroadcastメソッドはdeprecatedとなっている
        removeStickyBroadcast(intent);
    }

    private BroadcastReceiver mResultReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            
            // ★ポイント5★ 結果を受け取る場合、結果データの安全性を確認する
            // サンプルにつき割愛。「3.2 入力データの安全性を確認する」を参照。
            String data = getResultData();
            PublicSenderActivity.this.logLine(
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
