package org.jssec.android.service.privateservice;

import android.app.IntentService;
import android.content.Intent;
import android.widget.Toast;

public class PrivateIntentService extends IntentService{
    /**
     * IntentService���p�������ꍇ�A���������̃R���X�g���N�^��K���p�ӂ���B
     * ���ꂪ�����ꍇ�A�G���[�ɂȂ�B
     */
    public PrivateIntentService() {
        super("PrivateIntentService");
    }

    // Service���N������Ƃ��ɂP�񂾂��Ăяo�����
    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, this.getClass().getSimpleName() + " - onCreate()", Toast.LENGTH_SHORT).show();
    }
    
    // Service�ōs���������������̃��\�b�h�ɋL�q����
    @Override
    protected void onHandleIntent(Intent intent) {      
        // ���|�C���g2�� ����A�v�������Intent�ł����Ă��A��MIntent�̈��S�����m�F����
        // �T���v���ɂ������B�u3.2 ���̓f�[�^�̈��S�����m�F����v���Q�ƁB
        String param = intent.getStringExtra("PARAM");
        Toast.makeText(this, String.format("�p�����[�^�u%s�v���󂯎�����B", param), Toast.LENGTH_LONG).show();

        // �ʃX���b�h�ɂȂ��Ă���̂ŁA���Ԃ̂����鏈�����s���Ă��ǂ�
        try {
            // 5�b�ԃX���[�v
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Service���I������Ƃ��ɂP�񂾂��Ăяo�����
    @Override
    public void onDestroy() {
        Toast.makeText(this, this.getClass().getSimpleName() + " - onDestroy()", Toast.LENGTH_SHORT).show();
    }
    
}
