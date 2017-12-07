package org.jssec.android.provider.partneruser;

import org.jssec.android.shared.PkgCertWhitelists;
import org.jssec.android.shared.Utils;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class PartnerUserActivity extends Activity {

    // ���p���Content Provider���
    private static final String AUTHORITY = "org.jssec.android.provider.partnerprovider";
    private interface Address {
        public static final String PATH = "addresses";
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + PATH);
    }

    // ���|�C���g5�� ���p��p�[�g�i�[����Content Provider�A�v���̏ؖ������z���C�g���X�g�ɓo�^����Ă��邱�Ƃ��m�F����
    private static PkgCertWhitelists sWhitelists = null;
    private static void buildWhitelists(Context context) {
        boolean isdebug = Utils.isDebuggable(context);
        sWhitelists = new PkgCertWhitelists();

        // �p�[�g�i�[����Content Provider�A�v�� org.jssec.android.provider.partnerprovider �̏ؖ����n�b�V���l��o�^
        sWhitelists.add("org.jssec.android.provider.partnerprovider", isdebug ?
                // debug.keystore��"androiddebugkey"�̏ؖ����n�b�V���l
                "0EFB7236 328348A9 89718BAD DF57F544 D5CCB4AE B9DB34BC 1E29DD26 F77C8255" :
                // keystore��"my company key"�̏ؖ����n�b�V���l
                "D397D343 A5CBC10F 4EDDEB7C A10062DE 5690984F 1FB9E88B D7B3A7C2 42E142CA");

        // �ȉ����l�ɑ��̃p�[�g�i�[����Content Provider�A�v����o�^...
    }
    private static boolean checkPartner(Context context, String pkgname) {
        if (sWhitelists == null) buildWhitelists(context);
        return sWhitelists.test(context, pkgname);
    }
    // uri��AUTHORITY�Ƃ���Content Provider�̃p�b�P�[�W�����擾
    private String providerPkgname(Uri uri) {
        String pkgname = null;
        ProviderInfo pi = getPackageManager().resolveContentProvider(uri.getAuthority(), 0);
        if (pi != null) pkgname = pi.packageName;
        return pkgname;
    }

    public void onQueryClick(View view) {

        logLine("[Query]");

        // ���|�C���g5�� ���p��p�[�g�i�[����Content Provider�A�v���̏ؖ������z���C�g���X�g�ɓo�^����Ă��邱�Ƃ��m�F����
        if (!checkPartner(this, providerPkgname(Address.CONTENT_URI))) {
            logLine("  ���p�� Content Provider �A�v���̓z���C�g���X�g�ɓo�^����Ă��Ȃ��B");
            return;
        }

        // ���|�C���g6�� �p�[�g�i�[����Content Provider�A�v���ɊJ�����Ă悢���Ɍ��胊�N�G�X�g�Ɋ܂߂Ă悢
        Cursor cursor = null;
        try {
            cursor = getContentResolver().query(Address.CONTENT_URI, null, null, null, null);

            // ���|�C���g7�� �p�[�g�i�[����Content Provider�A�v������̌��ʂł����Ă��A���ʃf�[�^�̈��S�����m�F����
            // �T���v���ɂ������B�u3.2 ���̓f�[�^�̈��S�����m�F����v���Q�ƁB
            if (cursor == null) {
                logLine("  null cursor");
            } else {
                boolean moved = cursor.moveToFirst();
                while (moved) {
                    logLine(String.format("  %d, %s", cursor.getInt(0), cursor.getString(1)));
                    moved = cursor.moveToNext();
                }
            }
        }
        finally {
            if (cursor != null) cursor.close();
        }
    }

    public void onInsertClick(View view) {

        logLine("[Insert]");

        // ���|�C���g5�� ���p��p�[�g�i�[����Content Provider�A�v���̏ؖ������z���C�g���X�g�ɓo�^����Ă��邱�Ƃ��m�F����
        if (!checkPartner(this, providerPkgname(Address.CONTENT_URI))) {
            logLine("  ���p�� Content Provider �A�v���̓z���C�g���X�g�ɓo�^����Ă��Ȃ��B");
            return;
        }

        // ���|�C���g6�� �p�[�g�i�[����Content Provider�A�v���ɊJ�����Ă悢���Ɍ��胊�N�G�X�g�Ɋ܂߂Ă悢
        ContentValues values = new ContentValues();
        values.put("pref", "�����s");
        Uri uri = getContentResolver().insert(Address.CONTENT_URI, values);

        // ���|�C���g7�� �p�[�g�i�[����Content Provider�A�v������̌��ʂł����Ă��A���ʃf�[�^�̈��S�����m�F����
        // �T���v���ɂ������B�u3.2 ���̓f�[�^�̈��S�����m�F����v���Q�ƁB
        logLine("  uri:" + uri);
    }

    public void onUpdateClick(View view) {

        logLine("[Update]");

        // ���|�C���g5�� ���p��p�[�g�i�[����Content Provider�A�v���̏ؖ������z���C�g���X�g�ɓo�^����Ă��邱�Ƃ��m�F����
        if (!checkPartner(this, providerPkgname(Address.CONTENT_URI))) {
            logLine("  ���p�� Content Provider �A�v���̓z���C�g���X�g�ɓo�^����Ă��Ȃ��B");
            return;
        }

        // ���|�C���g6�� �p�[�g�i�[����Content Provider�A�v���ɊJ�����Ă悢���Ɍ��胊�N�G�X�g�Ɋ܂߂Ă悢
        ContentValues values = new ContentValues();
        values.put("pref", "�����s");
        String where = "_id = ?";
        String[] args = { "4" };
        int count = getContentResolver().update(Address.CONTENT_URI, values, where, args);

        // ���|�C���g7�� �p�[�g�i�[����Content Provider�A�v������̌��ʂł����Ă��A���ʃf�[�^�̈��S�����m�F����
        // �T���v���ɂ������B�u3.2 ���̓f�[�^�̈��S�����m�F����v���Q�ƁB
        logLine(String.format("  %s records updated", count));
    }

    public void onDeleteClick(View view) {

        logLine("[Delete]");

        // ���|�C���g5�� ���p��p�[�g�i�[����Content Provider�A�v���̏ؖ������z���C�g���X�g�ɓo�^����Ă��邱�Ƃ��m�F����
        if (!checkPartner(this, providerPkgname(Address.CONTENT_URI))) {
            logLine("  ���p�� Content Provider �A�v���̓z���C�g���X�g�ɓo�^����Ă��Ȃ��B");
            return;
        }

        // ���|�C���g6�� �p�[�g�i�[����Content Provider�A�v���ɊJ�����Ă悢���Ɍ��胊�N�G�X�g�Ɋ܂߂Ă悢
        int count = getContentResolver().delete(Address.CONTENT_URI, null, null);

        // ���|�C���g7�� �p�[�g�i�[����Content Provider�A�v������̌��ʂł����Ă��A���ʃf�[�^�̈��S�����m�F����
        // �T���v���ɂ������B�u3.2 ���̓f�[�^�̈��S�����m�F����v���Q�ƁB
        logLine(String.format("  %s records deleted", count));
    }

    private TextView mLogView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mLogView = (TextView)findViewById(R.id.logview);
    }

    private void logLine(String line) {
        mLogView.append(line);
        mLogView.append("\n");
    }
}
