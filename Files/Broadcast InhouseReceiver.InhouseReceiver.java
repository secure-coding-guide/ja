package org.jssec.android.broadcast.inhousereceiver;

import org.jssec.android.shared.SigPerm;
import org.jssec.android.shared.Utils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class InhouseReceiver extends BroadcastReceiver {

    // ���Ђ�Signature Permission
    private static final String MY_PERMISSION = "org.jssec.android.broadcast.inhousereceiver.MY_PERMISSION";

    // ���Ђ̏ؖ����̃n�b�V���l
    private static String sMyCertHash = null;
    private static String myCertHash(Context context) {
        if (sMyCertHash == null) {
            if (Utils.isDebuggable(context)) {
                // debug.keystore��"androiddebugkey"�̏ؖ����n�b�V���l
                sMyCertHash = "0EFB7236 328348A9 89718BAD DF57F544 D5CCB4AE B9DB34BC 1E29DD26 F77C8255";
            } else {
                // keystore��"my company key"�̏ؖ����n�b�V���l
                sMyCertHash = "D397D343 A5CBC10F 4EDDEB7C A10062DE 5690984F 1FB9E88B D7B3A7C2 42E142CA";
            }
        }
        return sMyCertHash;
    }

    private static final String MY_BROADCAST_INHOUSE =
        "org.jssec.android.broadcast.MY_BROADCAST_INHOUSE";

    public boolean isDynamic = false;
    private String getName() {
        return isDynamic ? "���Ќ��蓮�I Broadcast Receiver" : "���Ќ���ÓI Broadcast Receiver";
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        // ���|�C���g6�� �Ǝ���`Signature Permission�����ЃA�v���ɂ���`����Ă��邱�Ƃ��m�F����
        if (!SigPerm.test(context, MY_PERMISSION, myCertHash(context))) {
            Toast.makeText(context, "�Ǝ���`Signature Permission�����ЃA�v���ɂ���`����Ă��Ȃ��B", Toast.LENGTH_LONG).show();
            return;
        }

        // ���|�C���g7�� ���ЃA�v�������Broadcast�ł����Ă��A��MIntent�̈��S�����m�F����
        // �T���v���ɂ������B�u3.2 ���̓f�[�^�̈��S�����m�F����v���Q�ƁB
        if (MY_BROADCAST_INHOUSE.equals(intent.getAction())) {
            String param = intent.getStringExtra("PARAM");
            Toast.makeText(context,
                    String.format("%s:\n�u%s�v����M�����B", getName(), param),
                    Toast.LENGTH_SHORT).show();
        }

        // ���|�C���g8�� ���M���͎��ЃA�v���ł��邩��A�Z���V�e�B�u�ȏ���ԑ����Ă悢
        setResultCode(Activity.RESULT_OK);
        setResultData(String.format("�Z���V�e�B�u�ȏ�� from %s", getName()));
        abortBroadcast();
    }
}
