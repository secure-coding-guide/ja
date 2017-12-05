package org.jssec.android.broadcast.publicreceiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class PublicReceiver extends BroadcastReceiver {

    private static final String MY_BROADCAST_PUBLIC =
        "org.jssec.android.broadcast.MY_BROADCAST_PUBLIC";
    
    public boolean isDynamic = false;
    private String getName() {
        return isDynamic ? "���J���I Broadcast Receiver" : "���J�ÓI Broadcast Receiver";
    }
    
    @Override
    public void onReceive(Context context, Intent intent) {
        
        // ���|�C���g2�� ��MIntent�̈��S�����m�F����
        // ���JBroadcast Receiver�ł��邽�ߗ��p���A�v�����}���E�F�A�ł���\��������B
        // �T���v���ɂ������B�u3.2 ���̓f�[�^�̈��S�����m�F����v���Q�ƁB
        if (MY_BROADCAST_PUBLIC.equals(intent.getAction())) {
            String param = intent.getStringExtra("PARAM");
            Toast.makeText(context,
                    String.format("%s:\n�u%s�v����M�����B", getName(), param),
                    Toast.LENGTH_SHORT).show();
        }
        
        // ���|�C���g3�� ���ʂ�Ԃ��ꍇ�A�Z���V�e�B�u�ȏ����܂߂Ȃ�
        // ���JBroadcast Receiver�ł��邽�߁A
        // Broadcast�̑��M���A�v�����}���E�F�A�ł���\��������B
        // �}���E�F�A�Ɏ擾����Ă����̂Ȃ����ł���Ό��ʂƂ��ĕԂ��Ă��悢�B
        setResultCode(Activity.RESULT_OK);
        setResultData(String.format("�Z���V�e�B�u�ł͂Ȃ���� from %s", getName()));
        abortBroadcast();
    }
}
