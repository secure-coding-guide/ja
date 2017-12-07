package org.jssec.android.service.privateservice.localbind;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

public class PrivateLocalBindService extends Service
implements IPrivateLocalBindService{   
    /**
     * Service�ɐڑ����邽�߂̃N���X
     */
    public class LocalBinder extends Binder {
        PrivateLocalBindService getService() {
            return PrivateLocalBindService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // ���|�C���g3�� ����A�v�������Intent�ł����Ă��A��MIntent�̈��S�����m�F����
        // �T���v���ɂ������B�u3.2 ���̓f�[�^�̈��S�����m�F����v���Q�ƁB
        String param = intent.getStringExtra("PARAM");
        Toast.makeText(this, String.format("�p�����[�^�u%s�v���󂯎�����B", param), Toast.LENGTH_LONG).show();
        
        return new LocalBinder();
    }

    @Override
    public void onCreate() {
        Toast.makeText(this, this.getClass().getSimpleName() + " - onCreate()", Toast.LENGTH_SHORT).show();
    }
    
    @Override
    public void onDestroy() {
        Toast.makeText(this, this.getClass().getSimpleName() + " - onDestroy()", Toast.LENGTH_SHORT).show();
    }
    
    // �p�ӂ����C���^�[�t�F�[�X
    @Override
    public String getInfo() {
        // ���|�C���g2�� ���p���A�v���͓���A�v���ł��邩��A�Z���V�e�B�u�ȏ���Ԃ��Ă��悢
        return "�Z���V�e�B�u�ȏ��(from Service)";
    }
}
