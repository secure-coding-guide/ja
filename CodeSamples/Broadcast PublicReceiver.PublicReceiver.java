package org.jssec.android.broadcast.publicreceiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class PublicReceiver extends BroadcastReceiver {

    private static final String MY_BROADCAST_PUBLIC =
        "org.jssec.android.broadcast.MY_BROADCAST_PUBLIC";
    
    public boolean isDynamic = false;
    private String getName() {
        return isDynamic ? "公開動的 Broadcast Receiver" : "公開静的 Broadcast Receiver";
    }
    
    @Override
    public void onReceive(Context context, Intent intent) {
        
        // ★ポイント2★ 受信Intentの安全性を確認する
        // 公開Broadcast Receiverであるため利用元アプリがマルウェアである可能性がある。
        // サンプルにつき割愛。「3.2 入力データの安全性を確認する」を参照。
        if (MY_BROADCAST_PUBLIC.equals(intent.getAction())) {
            String param = intent.getStringExtra("PARAM");
            Toast.makeText(context,
                    String.format("%s:\n「%s」を受信した。", getName(), param),
                    Toast.LENGTH_SHORT).show();
        }
        
        // ★ポイント3★ 結果を返す場合、センシティブな情報を含めない
        // 公開Broadcast Receiverであるため、
        // Broadcastの送信元アプリがマルウェアである可能性がある。
        // マルウェアに取得されても問題のない情報であれば結果として返してもよい。
        setResultCode(Activity.RESULT_OK);
        setResultData(String.format("センシティブではない情報 from %s", getName()));
        abortBroadcast();
    }
}
