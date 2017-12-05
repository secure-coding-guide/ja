package org.jssec.android.service.publicservice;

import android.app.IntentService;
import android.content.Intent;
import android.widget.Toast;

public class PublicIntentService extends IntentService{

    /**
     * IntentService���p�������ꍇ�A���������̃R���X�g���N�^��K���p�ӂ���B
     * ���ꂪ�����ꍇ�A�G���[�ɂȂ�B
     */
    public PublicIntentService() {
        super("CreatingTypeBService");
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
        // ���|�C���g2�� ��MIntent�̈��S�����m�F����
        // ���JActivity�ł��邽�ߗ��p���A�v�����}���E�F�A�ł���\��������B
        // �T���v���ɂ������B�u3.2 ���̓f�[�^�̈��S�����m�F����v���Q�ƁB
        String param = intent.getStringExtra("PARAM");
        Toast.makeText(this, String.format("�p�����[�^�u%s�v���󂯎�����B", param), Toast.LENGTH_LONG).show();
    }


    // Service���I������Ƃ��ɂP�񂾂��Ăяo�����
    @Override
    public void onDestroy() {
        Toast.makeText(this, this.getClass().getSimpleName() + " - onDestroy()", Toast.LENGTH_SHORT).show();
    }
    
}
