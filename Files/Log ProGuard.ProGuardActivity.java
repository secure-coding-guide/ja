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

        // ���|�C���g1�� �Z���V�e�B�u�ȏ���Log.e()/w()/i()�ASystem.out/err�ŏo�͂��Ȃ�
        Log.e(LOG_TAG, "�Z���V�e�B�u�ł͂Ȃ����(ERROR)");
        Log.w(LOG_TAG, "�Z���V�e�B�u�ł͂Ȃ����(WARN)");
        Log.i(LOG_TAG, "�Z���V�e�B�u�ł͂Ȃ����(INFO)");

        // ���|�C���g2�� �Z���V�e�B�u�ȏ������O�o�͂���ꍇ��Log.d()/v()�ŏo�͂���
        // ���|�C���g3�� Log.d()/v()�̌Ăяo���ł͖߂�l���g�p���Ȃ�(������r)
        Log.d(LOG_TAG, "�Z���V�e�B�u�ȏ��(DEBUG)");
        Log.v(LOG_TAG, "�Z���V�e�B�u�ȏ��(VERBOSE)");
    }
}
