package org.jssec.android.provider.publicuser;

import android.app.Activity;
import android.content.ContentValues;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class PublicUserActivity extends Activity {

    // ���p���Content Provider���
    private static final String AUTHORITY = "org.jssec.android.provider.publicprovider";
    private interface Address {
        public static final String PATH = "addresses";
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + PATH);
    }

    public void onQueryClick(View view) {

        logLine("[Query]");

        if (!providerExists(Address.CONTENT_URI)) {
            logLine("  Content Provider���s��");
            return;
        }

        // ���|�C���g4�� �Z���V�e�B�u�ȏ������N�G�X�g�Ɋ܂߂Ă͂Ȃ�Ȃ�
        // ���N�G�X�g��̃A�v�����}���E�F�A�ł���\��������B
        // �}���E�F�A�Ɏ擾����Ă����̂Ȃ����ł���΃��N�G�X�g�Ɋ܂߂Ă��悢�B
        Cursor cursor = null;
        try {
            cursor = getContentResolver().query(Address.CONTENT_URI, null, null, null, null);

            // ���|�C���g5�� ���ʃf�[�^�̈��S�����m�F����
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

        if (!providerExists(Address.CONTENT_URI)) {
            logLine("  Content Provider���s��");
            return;
        }

        // ���|�C���g4�� �Z���V�e�B�u�ȏ������N�G�X�g�Ɋ܂߂Ă͂Ȃ�Ȃ�
        // ���N�G�X�g��̃A�v�����}���E�F�A�ł���\��������B
        // �}���E�F�A�Ɏ擾����Ă����̂Ȃ����ł���΃��N�G�X�g�Ɋ܂߂Ă��悢�B
        ContentValues values = new ContentValues();
        values.put("pref", "�����s");
        Uri uri = getContentResolver().insert(Address.CONTENT_URI, values);

        // ���|�C���g5�� ���ʃf�[�^�̈��S�����m�F����
        // �T���v���ɂ������B�u3.2 ���̓f�[�^�̈��S�����m�F����v���Q�ƁB
        logLine("  uri:" + uri);
    }

    public void onUpdateClick(View view) {

        logLine("[Update]");

        if (!providerExists(Address.CONTENT_URI)) {
            logLine("  Content Provider���s��");
            return;
        }

        // ���|�C���g4�� �Z���V�e�B�u�ȏ������N�G�X�g�Ɋ܂߂Ă͂Ȃ�Ȃ�
        // ���N�G�X�g��̃A�v�����}���E�F�A�ł���\��������B
        // �}���E�F�A�Ɏ擾����Ă����̂Ȃ����ł���΃��N�G�X�g�Ɋ܂߂Ă��悢�B
        ContentValues values = new ContentValues();
        values.put("pref", "�����s");
        String where = "_id = ?";
        String[] args = { "4" };
        int count = getContentResolver().update(Address.CONTENT_URI, values, where, args);

        // ���|�C���g5�� ���ʃf�[�^�̈��S�����m�F����
        // �T���v���ɂ������B�u3.2 ���̓f�[�^�̈��S�����m�F����v���Q�ƁB
        logLine(String.format("  %s records updated", count));
    }

    public void onDeleteClick(View view) {

        logLine("[Delete]");

        if (!providerExists(Address.CONTENT_URI)) {
            logLine("  Content Provider���s��");
            return;
        }

        // ���|�C���g4�� �Z���V�e�B�u�ȏ������N�G�X�g�Ɋ܂߂Ă͂Ȃ�Ȃ�
        // ���N�G�X�g��̃A�v�����}���E�F�A�ł���\��������B
        // �}���E�F�A�Ɏ擾����Ă����̂Ȃ����ł���΃��N�G�X�g�Ɋ܂߂Ă��悢�B
        int count = getContentResolver().delete(Address.CONTENT_URI, null, null);

        // ���|�C���g5�� ���ʃf�[�^�̈��S�����m�F����
        // �T���v���ɂ������B�u3.2 ���̓f�[�^�̈��S�����m�F����v���Q�ƁB
        logLine(String.format("  %s records deleted", count));
    }

    private boolean providerExists(Uri uri) {
        ProviderInfo pi = getPackageManager().resolveContentProvider(uri.getAuthority(), 0);
        return (pi != null);
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
