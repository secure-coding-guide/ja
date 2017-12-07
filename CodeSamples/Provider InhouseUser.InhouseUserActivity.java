package org.jssec.android.provider.inhouseuser;

import org.jssec.android.shared.PkgCert;
import org.jssec.android.shared.SigPerm;
import org.jssec.android.shared.Utils;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class InhouseUserActivity extends Activity {

    // ���p���Content Provider���
    private static final String AUTHORITY = "org.jssec.android.provider.inhouseprovider";
    private interface Address {
        public static final String PATH = "addresses";
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + PATH);
    }

    // ���Ђ�Signature Permission
    private static final String MY_PERMISSION = "org.jssec.android.provider.inhouseprovider.MY_PERMISSION";

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

    // ���p��Content Provider�̃p�b�P�[�W�����擾
    private static String providerPkgname(Context context, Uri uri) {
        String pkgname = null;
        PackageManager pm = context.getPackageManager();
        ProviderInfo pi = pm.resolveContentProvider(uri.getAuthority(), 0);
        if (pi != null) pkgname = pi.packageName;
        return pkgname;
    }

    public void onQueryClick(View view) {

        logLine("[Query]");

        // ���|�C���g9�� �Ǝ���`Signature Permission�����ЃA�v���ɂ���`����Ă��邱�Ƃ��m�F����
        if (!SigPerm.test(this, MY_PERMISSION, myCertHash(this))) {
            logLine("  �Ǝ���`Signature Permission�����ЃA�v���ɂ���`����Ă��Ȃ��B");
            return;
        }

        // ���|�C���g10�� ���p��Content Provider�A�v���̏ؖ��������Ђ̏ؖ����ł��邱�Ƃ��m�F����
        String pkgname = providerPkgname(this, Address.CONTENT_URI);
        if (!PkgCert.test(this, pkgname, myCertHash(this))) {
            logLine("  ���p�� Content Provider �͎��ЃA�v���ł͂Ȃ��B");
            return;
        }

        // ���|�C���g11�� ���Ќ���Content Provider�A�v���ɊJ�����Ă悢���Ɍ��胊�N�G�X�g�Ɋ܂߂Ă悢
        Cursor cursor = null;
        try {
            cursor = getContentResolver().query(Address.CONTENT_URI, null, null, null, null);

            // ���|�C���g12�� ���Ќ���Content Provider�A�v������̌��ʂł����Ă��A���ʃf�[�^�̈��S�����m�F����
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

        // ���|�C���g9�� �Ǝ���`Signature Permission�����ЃA�v���ɂ���`����Ă��邱�Ƃ��m�F����
        String correctHash = myCertHash(this);
        if (!SigPerm.test(this, MY_PERMISSION, correctHash)) {
            logLine("  �Ǝ���`Signature Permission�����ЃA�v���ɂ���`����Ă��Ȃ��B");
            return;
        }

        // ���|�C���g10�� ���p��Content Provider�A�v���̏ؖ��������Ђ̏ؖ����ł��邱�Ƃ��m�F����
        String pkgname = providerPkgname(this, Address.CONTENT_URI);
        if (!PkgCert.test(this, pkgname, correctHash)) {
            logLine("  ���p�� Content Provider �͎��ЃA�v���ł͂Ȃ��B");
            return;
        }

        // ���|�C���g11�� ���Ќ���Content Provider�A�v���ɊJ�����Ă悢���Ɍ��胊�N�G�X�g�Ɋ܂߂Ă悢
        ContentValues values = new ContentValues();
        values.put("pref", "�����s");
        Uri uri = getContentResolver().insert(Address.CONTENT_URI, values);

        // ���|�C���g12�� ���Ќ���Content Provider�A�v������̌��ʂł����Ă��A���ʃf�[�^�̈��S�����m�F����
        // �T���v���ɂ������B�u3.2 ���̓f�[�^�̈��S�����m�F����v���Q�ƁB
        logLine("  uri:" + uri);
    }

    public void onUpdateClick(View view) {

        logLine("[Update]");

        // ���|�C���g9�� �Ǝ���`Signature Permission�����ЃA�v���ɂ���`����Ă��邱�Ƃ��m�F����
        String correctHash = myCertHash(this);
        if (!SigPerm.test(this, MY_PERMISSION, correctHash)) {
            logLine("  �Ǝ���`Signature Permission�����ЃA�v���ɂ���`����Ă��Ȃ��B");
            return;
        }

        // ���|�C���g10�� ���p��Content Provider�A�v���̏ؖ��������Ђ̏ؖ����ł��邱�Ƃ��m�F����
        String pkgname = providerPkgname(this, Address.CONTENT_URI);
        if (!PkgCert.test(this, pkgname, correctHash)) {
            logLine("  ���p�� Content Provider �͎��ЃA�v���ł͂Ȃ��B");
            return;
        }

        // ���|�C���g11�� ���Ќ���Content Provider�A�v���ɊJ�����Ă悢���Ɍ��胊�N�G�X�g�Ɋ܂߂Ă悢
        ContentValues values = new ContentValues();
        values.put("pref", "�����s");
        String where = "_id = ?";
        String[] args = { "4" };
        int count = getContentResolver().update(Address.CONTENT_URI, values, where, args);

        // ���|�C���g12�� ���Ќ���Content Provider�A�v������̌��ʂł����Ă��A���ʃf�[�^�̈��S�����m�F����
        // �T���v���ɂ������B�u3.2 ���̓f�[�^�̈��S�����m�F����v���Q�ƁB
        logLine(String.format("  %s records updated", count));
    }

    public void onDeleteClick(View view) {

        logLine("[Delete]");

        // ���|�C���g9�� �Ǝ���`Signature Permission�����ЃA�v���ɂ���`����Ă��邱�Ƃ��m�F����
        String correctHash = myCertHash(this);
        if (!SigPerm.test(this, MY_PERMISSION, correctHash)) {
            logLine("  �Ǝ���`Signature Permission�����ЃA�v���ɂ���`����Ă��Ȃ��B");
            return;
        }

        // ���|�C���g10�� ���p��Content Provider�A�v���̏ؖ��������Ђ̏ؖ����ł��邱�Ƃ��m�F����
        String pkgname = providerPkgname(this, Address.CONTENT_URI);
        if (!PkgCert.test(this, pkgname, correctHash)) {
            logLine("  ���p�� Content Provider �͎��ЃA�v���ł͂Ȃ��B");
            return;
        }

        // ���|�C���g11�� ���Ќ���Content Provider�A�v���ɊJ�����Ă悢���Ɍ��胊�N�G�X�g�Ɋ܂߂Ă悢
        int count = getContentResolver().delete(Address.CONTENT_URI, null, null);

        // ���|�C���g12�� ���Ќ���Content Provider�A�v������̌��ʂł����Ă��A���ʃf�[�^�̈��S�����m�F����
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
