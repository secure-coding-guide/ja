package org.jssec.android.broadcast.privatereceiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class PrivateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        
        // ���|�C���g2�� ����A�v�������瑗�M���ꂽBroadcast�ł����Ă��A��MIntent�̈��S�����m�F����
        // �T���v���ɂ������B�u3.2 ���̓f�[�^�̈��S�����m�F����v���Q�ƁB
        String param = intent.getStringExtra("PARAM");
        Toast.makeText(context,
                String.format("�u%s�v����M�����B", param),
                Toast.LENGTH_SHORT).show();
        
        // ���|�C���g3�� ���M���͓���A�v�����ł��邩��A�Z���V�e�B�u�ȏ���ԑ����Ă悢
        setResultCode(Activity.RESULT_OK);
        setResultData("�Z���V�e�B�u�ȏ�� from Receiver");
        abortBroadcast();
    }
}
