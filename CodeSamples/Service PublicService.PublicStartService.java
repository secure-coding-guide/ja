package org.jssec.android.service.publicservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class PublicStartService extends Service{

    // Service���N������Ƃ��ɂP�񂾂��Ăяo�����
    @Override
    public void onCreate() {     
        Toast.makeText(this, this.getClass().getSimpleName() + " - xxonCreate()", Toast.LENGTH_SHORT).show();
    }

    
    // startService()���Ă΂ꂽ�񐔂����Ăяo�����
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // ���|�C���g2�� ��MIntent�̈��S�����m�F����
        // ���JActivity�ł��邽�ߗ��p���A�v�����}���E�F�A�ł���\��������B
        // �T���v���ɂ������B�u3.2 ���̓f�[�^�̈��S�����m�F����v���Q�ƁB
        String param = intent.getStringExtra("PARAM");
        Toast.makeText(this, String.format("�p�����[�^�u%s�vx���󂯎�����B", param), Toast.LENGTH_LONG).show();
        
        // ���|�C���g3�� ���ʂ�Ԃ��ꍇ�A�Z���V�e�B�u�ȏ����܂߂Ȃ�
        // �{�T���v���� startService()���g����Service���p�̗�ׁ̈A���ʏ��͕Ԃ��Ȃ�
        
        // �T�[�r�X�͖����I�ɏI��������
        // stopSelf �� stopService �����s�����Ƃ��ɃT�[�r�X���I������
        // START_NOT_STICKY �́A�����������Ȃ�����kill���ꂽ�ꍇ�Ɏ����I�ɂ͕��A���Ȃ�
        return Service.START_NOT_STICKY;
    }
    
    // Service���I������Ƃ��ɂP�񂾂��Ăяo�����
    @Override
    public void onDestroy() {
        Toast.makeText(this, this.getClass().getSimpleName() + " - onDestroy()", Toast.LENGTH_SHORT).show();
    }
    
    @Override
    public IBinder onBind(Intent intent) {
        // ���̃T�[�r�X�ɂ̓o�C���h���Ȃ�
        return null;
    }
}
