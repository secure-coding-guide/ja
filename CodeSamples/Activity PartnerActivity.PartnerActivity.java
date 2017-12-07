package org.jssec.android.activity.partneractivity;

import org.jssec.android.shared.PkgCertWhitelists;
import org.jssec.android.shared.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class PartnerActivity extends Activity {
    
    // ���|�C���g4�� ���p���A�v���̏ؖ������z���C�g���X�g�ɓo�^����Ă��邱�Ƃ��m�F����
    private static PkgCertWhitelists sWhitelists = null;
    private static void buildWhitelists(Context context) {
        boolean isdebug = Utils.isDebuggable(context);
        sWhitelists = new PkgCertWhitelists();
        
        // �p�[�g�i�[�A�v�� org.jssec.android.activity.partneruser �̏ؖ����n�b�V���l��o�^
        sWhitelists.add("org.jssec.android.activity.partneruser", isdebug ?
                // debug.keystore��"androiddebugkey"�̏ؖ����n�b�V���l
                "0EFB7236 328348A9 89718BAD DF57F544 D5CCB4AE B9DB34BC 1E29DD26 F77C8255" :
                // keystore��"partner key"�̏ؖ����n�b�V���l
                "1F039BB5 7861C27A 3916C778 8E78CE00 690B3974 3EB8259F E2627B8D 4C0EC35A");
        
        // �ȉ����l�ɑ��̃p�[�g�i�[�A�v����o�^...
    }
    private static boolean checkPartner(Context context, String pkgname) {
        if (sWhitelists == null) buildWhitelists(context);
        return sWhitelists.test(context, pkgname);
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // ���|�C���g4�� ���p���A�v���̏ؖ������z���C�g���X�g�ɓo�^����Ă��邱�Ƃ��m�F����
        if (!checkPartner(this, getCallingActivity().getPackageName())) {
            Toast.makeText(this, "���p���A�v���̓p�[�g�i�[�A�v���ł͂Ȃ��B", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        
        // ���|�C���g5�� �p�[�g�i�[�A�v�������Intent�ł����Ă��A��MIntent�̈��S�����m�F����
        // �T���v���ɂ������B�u3.2 ���̓f�[�^�̈��S�����m�F����v���Q�ƁB
        Toast.makeText(this, "�p�[�g�i�[�A�v������A�N�Z�X����", Toast.LENGTH_LONG).show();
    }
    
    public void onReturnResultClick(View view) {

        // ���|�C���g6�� �p�[�g�i�[�A�v���ɊJ�����Ă悢���Ɍ���ԑ����Ă悢
        Intent intent = new Intent();
        intent.putExtra("RESULT", "�p�[�g�i�[�A�v���ɊJ�����Ă悢���");
        setResult(RESULT_OK, intent);
        finish();
    }
}
