package org.jssec.android.activity.inhouseuser;

import org.jssec.android.shared.PkgCert;
import org.jssec.android.shared.SigPerm;
import org.jssec.android.shared.Utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class InhouseUserActivity extends Activity {

    // ���p���Activity���
    private static final String TARGET_PACKAGE =  "org.jssec.android.activity.inhouseactivity";
    private static final String TARGET_ACTIVITY = "org.jssec.android.activity.inhouseactivity.InhouseActivity";

    // ���Ђ�Signature Permission
    private static final String MY_PERMISSION = "org.jssec.android.activity.inhouseactivity.MY_PERMISSION";

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

    private static final int REQUEST_CODE = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void onUseActivityClick(View view) {

        // ���|�C���g11�� �Ǝ���`Signature Permission�����ЃA�v���ɂ���`����Ă��邱�Ƃ��m�F����
        if (!SigPerm.test(this, MY_PERMISSION, myCertHash(this))) {
            Toast.makeText(this, "�Ǝ���`Signature Permission�����ЃA�v���ɂ���`����Ă��Ȃ��B", Toast.LENGTH_LONG).show();
            return;
        }

        // ���|�C���g12�� ���p��A�v���̏ؖ��������Ђ̏ؖ����ł��邱�Ƃ��m�F����
        if (!PkgCert.test(this, TARGET_PACKAGE, myCertHash(this))) {
            Toast.makeText(this, "���p��A�v���͎��ЃA�v���ł͂Ȃ��B", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            Intent intent = new Intent();

            // ���|�C���g13�� ���p��A�v���͎��ЃA�v���ł��邩��A�Z���V�e�B�u�ȏ���putExtra()���g���ꍇ�Ɍ��著�M���Ă��悢
            intent.putExtra("PARAM", "�Z���V�e�B�u�ȏ��");

            // ���|�C���g14�� �����IIntent�ɂ�莩�Ќ���Activity���Ăяo��
            intent.setClassName(TARGET_PACKAGE, TARGET_ACTIVITY);
            startActivityForResult(intent, REQUEST_CODE);
        }
        catch (ActivityNotFoundException e) {
            Toast.makeText(this, "���p��Activity��������Ȃ��B", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) return;

        switch (requestCode) {
        case REQUEST_CODE:
            String result = data.getStringExtra("RESULT");

            // ���|�C���g15�� ���ЃA�v������̌��ʏ��ł����Ă��A��MIntent�̈��S�����m�F����
            // �T���v���ɂ������B�u3.2 ���̓f�[�^�̈��S�����m�F����v���Q�ƁB
            Toast.makeText(this, String.format("���ʁu%s�v���󂯎�����B", result), Toast.LENGTH_LONG).show();
            break;
        }
    }
}
