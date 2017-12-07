package org.jssec.android.log.proguard;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class ProGuardActivity extends Activity {

    final static String LOG_TAG = "ProGuardActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proguard);

        // ★ポイント1★ センシティブな情報はLog.e()/w()/i()、System.out/errで出力しない
        Log.e(LOG_TAG, "センシティブではない情報(ERROR)");
        Log.w(LOG_TAG, "センシティブではない情報(WARN)");
        Log.i(LOG_TAG, "センシティブではない情報(INFO)");

        // ★ポイント2★ センシティブな情報をログ出力する場合はLog.d()/v()で出力する
        // ★ポイント3★ Log.d()/v()の呼び出しでは戻り値を使用しない(代入や比較)
        Log.d(LOG_TAG, "センシティブな情報(DEBUG)");
        Log.v(LOG_TAG, "センシティブな情報(VERBOSE)");
    }
}
