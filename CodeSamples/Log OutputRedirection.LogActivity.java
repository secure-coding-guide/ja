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
        
        Log.i(LOG_TAG,"Log.i()�Ń��O�o�́i1��ځj");
        System.out.println("System.out�Ƀ��O�o��");  // �����[�X�łł̓��O�o�͂���Ȃ�
        System.err.println("System.err�Ƀ��O�o��");  // �����[�X�łł̓��O�o�͂���Ȃ�
        Log.i(LOG_TAG,"Log.i()�Ń��O�o�́i2��ځj");
    }
}
