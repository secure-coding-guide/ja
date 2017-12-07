package org.jssec.android.broadcast.privatereceiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class PrivateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        
        // ★ポイント2★ 同一アプリ内から送信されたBroadcastであっても、受信Intentの安全性を確認する
        // サンプルにつき割愛。「3.2 入力データの安全性を確認する」を参照。
        String param = intent.getStringExtra("PARAM");
        Toast.makeText(context,
                String.format("「%s」を受信した。", param),
                Toast.LENGTH_SHORT).show();
        
        // ★ポイント3★ 送信元は同一アプリ内であるから、センシティブな情報を返送してよい
        setResultCode(Activity.RESULT_OK);
        setResultData("センシティブな情報 from Receiver");
        abortBroadcast();
    }
}
