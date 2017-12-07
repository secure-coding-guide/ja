package org.jssec.android.log.outputredirection;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class LogActivity extends Activity {

    final static String LOG_TAG = "LogActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        
        Log.i(LOG_TAG,"Log.i()でログ出力（1回目）");
        System.out.println("System.outにログ出力");  // リリース版ではログ出力されない
        System.err.println("System.errにログ出力");  // リリース版ではログ出力されない
        Log.i(LOG_TAG,"Log.i()でログ出力（2回目）");
    }
}
