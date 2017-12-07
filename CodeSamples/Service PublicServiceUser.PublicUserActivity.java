package org.jssec.android.service.publicserviceuser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PublicUserActivity extends Activity {

    // ���p��Service���
    private static final String TARGET_PACKAGE = "org.jssec.android.service.publicservice";
    private static final String TARGET_START_CLASS = "org.jssec.android.service.publicservice.PublicStartService";
    private static final String TARGET_INTENT_CLASS = "org.jssec.android.service.publicservice.PublicIntentService";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.publicservice_activity);
    }
    
    // �T�[�r�X�J�n
    public void onStartServiceClick(View v) {              

        Intent intent = new Intent("org.jssec.android.service.publicservice.action.startservice");

        // ���|�C���g4�� Service�͖����IIntent�ŌĂяo��
        intent.setClassName(TARGET_PACKAGE, TARGET_START_CLASS);
          
        // ���|�C���g5�� �Z���V�e�B�u�ȏ��𑗐M���Ă͂Ȃ�Ȃ�
        intent.putExtra("PARAM", "�Z���V�e�B�u�ł͂Ȃ����");

        startService(intent);
        
     // ���|�C���g6�� ���ʂ��󂯎��ꍇ�A���ʃf�[�^�̈��S�����m�F����
     // �{�T���v���� startService()���g����Service���p�̗�ׁ̈A���ʏ��͎󂯎��Ȃ�
    }
    
    // �T�[�r�X��~�{�^��
    public void onStopServiceClick(View v) {
        doStopService();
    }
        
    // IntentService �J�n�{�^��

    public void onIntentServiceClick(View v) {      
        Intent intent = new Intent("org.jssec.android.service.publicservice.action.intentservice");

        // ���|�C���g4�� Service�͖����IIntent�ŌĂяo��
        intent.setClassName(TARGET_PACKAGE, TARGET_INTENT_CLASS);

        // ���|�C���g5�� �Z���V�e�B�u�ȏ��𑗐M���Ă͂Ȃ�Ȃ�
        intent.putExtra("PARAM", "�Z���V�e�B�u�ł͂Ȃ����");

        startService(intent);
    }
        
    @Override
    public void onStop(){
        super.onStop();
        // �T�[�r�X���I�����Ă��Ȃ��ꍇ�͏I������
        doStopService();
    }
    
    // �T�[�r�X���~����
    private void doStopService() {            
        Intent intent = new Intent("org.jssec.android.service.publicservice.action.startservice");

        // ���|�C���g4�� Service�͖����IIntent�ŌĂяo��
        intent.setClassName(TARGET_PACKAGE, TARGET_START_CLASS);

        stopService(intent);        
    }
}
