package org.jssec.android.activity.partneruser;

import org.jssec.android.shared.PkgCertWhitelists;
import org.jssec.android.shared.Utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class PartnerUserActivity extends Activity {

    // ���|�C���g7�� ���p��p�[�g�i�[����Activity�A�v���̏ؖ������z���C�g���X�g�ɓo�^����Ă��邱�Ƃ��m�F����
    private static PkgCertWhitelists sWhitelists = null;
    private static void buildWhitelists(Context context) {
        boolean isdebug = Utils.isDebuggable(context);
        sWhitelists = new PkgCertWhitelists();
        
        // �p�[�g�i�[����Activity�A�v�� org.jssec.android.activity.partneractivity �̏ؖ����n�b�V���l��o�^
        sWhitelists.add("org.jssec.android.activity.partneractivity", isdebug ?
                // debug.keystore��"androiddebugkey"�̏ؖ����n�b�V���l
                "0EFB7236 328348A9 89718BAD DF57F544 D5CCB4AE B9DB34BC 1E29DD26 F77C8255" :
                // keystore��"my company key"�̏ؖ����n�b�V���l
                "D397D343 A5CBC10F 4EDDEB7C A10062DE 5690984F 1FB9E88B D7B3A7C2 42E142CA");
        
        // �ȉ����l�ɑ��̃p�[�g�i�[����Activity�A�v����o�^...
    }
    private static boolean checkPartner(Context context, String pkgname) {
        if (sWhitelists == null) buildWhitelists(context);
        return sWhitelists.test(context, pkgname);
    }
    
    private static final int REQUEST_CODE = 1;

    // ���p��̃p�[�g�i�[����Activity�Ɋւ�����
    private static final String TARGET_PACKAGE =  "org.jssec.android.activity.partneractivity";
    private static final String TARGET_ACTIVITY = "org.jssec.android.activity.partneractivity.PartnerActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    public void onUseActivityClick(View view) {

        // ���|�C���g7�� ���p��p�[�g�i�[����Activity�A�v���̏ؖ������z���C�g���X�g�ɓo�^����Ă��邱�Ƃ��m�F����
        if (!checkPartner(this, TARGET_PACKAGE)) {
            Toast.makeText(this, "���p�� Activity �A�v���̓z���C�g���X�g�ɓo�^����Ă��Ȃ��B", Toast.LENGTH_LONG).show();
            return;
        }
        
        try {
            Intent intent = new Intent();
            
            // ���|�C���g8�� Activity�ɑ��M����Intent�ɂ́A�t���OFLAG_ACTIVITY_NEW_TASK��ݒ肵�Ȃ�
            
            // ���|�C���g9�� ���p��p�[�g�i�[����A�v���ɊJ�����Ă悢����putExtra()���g���ꍇ�Ɍ��著�M���Ă悢
            intent.putExtra("PARAM", "�p�[�g�i�[�A�v���ɊJ�����Ă悢���");
            
            // ���|�C���g10�� �����IIntent�ɂ��p�[�g�i�[����Activity���Ăяo��
            intent.setClassName(TARGET_PACKAGE, TARGET_ACTIVITY);
            
            // ���|�C���g11�� startActivityForResult()�ɂ��p�[�g�i�[����Activity���Ăяo��
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
            
            // ���|�C���g12�� �p�[�g�i�[����A�v������̌��ʏ��ł����Ă��A��MIntent�̈��S�����m�F����
            // �T���v���ɂ������B�u3.2 ���̓f�[�^�̈��S�����m�F����v���Q�ƁB
            Toast.makeText(this,
                    String.format("���ʁu%s�v���󂯎�����B", result), Toast.LENGTH_LONG).show();
            break;
        }
    }
}
