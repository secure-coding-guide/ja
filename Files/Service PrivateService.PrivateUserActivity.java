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
    
    // �T�[�r�X�J�n
    
    public void onStartServiceClick(View v) {
          // ���|�C���g4�� ����A�v����Service�̓N���X�w��̖����IIntent�ŌĂяo��                
        Intent intent = new Intent(this, PrivateStartService.class);
          
          // ���|�C���g5�� ���p��A�v���͓���A�v���ł��邩��A�Z���V�e�B�u�ȏ��𑗐M���Ă��悢
        intent.putExtra("PARAM", "�Z���V�e�B�u�ȏ��");

        startService(intent);
    }
    
    // �T�[�r�X��~�{�^��
    public void onStopServiceClick(View v) {
        doStopService();
    }
        
    @Override
    public void onStop(){
        super.onStop();
        // �T�[�r�X���I�����Ă��Ȃ��ꍇ�͏I������
        doStopService();
    }
    // �T�[�r�X���~����
    private void doStopService() {
        // ���|�C���g4�� ����A�v����Service�̓N���X�w��̖����IIntent�ŌĂяo��                
        Intent intent = new Intent(this, PrivateStartService.class);
        stopService(intent);        
    }

    // IntentService �J�n�{�^��

    public void onIntentServiceClick(View v) {
          // ���|�C���g4�� ����A�v����Service�̓N���X�w��̖����IIntent�ŌĂяo��                
        Intent intent = new Intent(this, PrivateIntentService.class);
          
          // ���|�C���g5�� ���p��A�v���͓���A�v���ł��邩��A�Z���V�e�B�u�ȏ��𑗐M���Ă��悢
        intent.putExtra("PARAM", "�Z���V�e�B�u�ȏ��");

        startService(intent);
    }
        
}
