package org.jssec.android.provider.temporaryuser;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class TemporaryUserActivity extends Activity {

    // Provider Activity�Ɋւ�����
    private static final String TARGET_PACKAGE =  "org.jssec.android.provider.temporaryprovider";
    private static final String TARGET_ACTIVITY = "org.jssec.android.provider.temporaryprovider.TemporaryPassiveGrantActivity";

    // ���p���Content Provider���
    private static final String AUTHORITY = "org.jssec.android.provider.temporaryprovider";
    private interface Address {
        public static final String PATH = "addresses";
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + PATH);
    }

    private static final int REQUEST_CODE = 1;

    public void onQueryClick(View view) {

        logLine("[Query]");

        Cursor cursor = null;
        try {
            if (!providerExists(Address.CONTENT_URI)) {
                logLine("  Content Provider���s��");
                return;
            }

            // ���|�C���g9�� �Z���V�e�B�u�ȏ������N�G�X�g�Ɋ܂߂Ă͂Ȃ�Ȃ�
            // ���N�G�X�g��̃A�v�����}���E�F�A�ł���\��������B
            // �}���E�F�A�Ɏ擾����Ă����̂Ȃ����ł���΃��N�G�X�g�Ɋ܂߂Ă��悢�B
            cursor = getContentResolver().query(Address.CONTENT_URI, null, null, null, null);

            // ���|�C���g10�� ���ʃf�[�^�̈��S�����m�F����
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
        } catch (SecurityException ex) {
            logLine("  ��O:" + ex.getMessage());
        }
        finally {
            if (cursor != null) cursor.close();
        }
    }

    // ���̃A�v�����ꎞ�I�ȃA�N�Z�X����v�����AContent Provider���A�v�����󓮓I�ɃA�N�Z�X����^����P�[�X
    public void onGrantRequestClick(View view) {
        Intent intent = new Intent();
        intent.setClassName(TARGET_PACKAGE, TARGET_ACTIVITY);
        try {
            startActivityForResult(intent, REQUEST_CODE);
        } catch (ActivityNotFoundException e) {
            logLine("Grant�̗v���Ɏ��s���܂����B\nTemporaryProvider���C���X�g�[������Ă��邩�m�F���Ă��������B");
        }
    }

    private boolean providerExists(Uri uri) {
        ProviderInfo pi = getPackageManager().resolveContentProvider(uri.getAuthority(), 0);
        return (pi != null);
    }

    private TextView mLogView;

    // Content Provider���A�v�����\���I�ɂ��̃A�v���ɃA�N�Z�X����^����P�[�X
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
