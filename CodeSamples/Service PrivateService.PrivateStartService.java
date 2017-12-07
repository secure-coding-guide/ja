package org.jssec.android.service.privateservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class PrivateStartService extends Service{    
    // Service���N������Ƃ��ɂP�񂾂��Ăяo�����
    @Override
    public void onCreate() {       
        Toast.makeText(this, this.getClass().getSimpleName() + " - onCreate()", Toast.LENGTH_SHORT).show();
    }

    // startService()���Ă΂ꂽ�񐔂����Ăяo�����
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // ���|�C���g2�� ����A�v�������Intent�ł����Ă��A��MIntent�̈��S�����m�F����
        // �T���v���ɂ������B�u3.2 ���̓f�[�^�̈��S�����m�F����v���Q�ƁB
        String param = intent.getStringExtra("PARAM");
        Toast.makeText(this, String.format("�p�����[�^�u%s�v���󂯎�����B", param), Toast.LENGTH_LONG).show();
        
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
