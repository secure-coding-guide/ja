package org.jssec.android.provider.privateprovider;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class PrivateUserActivity extends Activity {

    public void onQueryClick(View view) {

        logLine("[Query]");

        // ���|�C���g4�� ����A�v�����ւ̃��N�G�X�g�ł��邩��A�Z���V�e�B�u�ȏ������N�G�X�g�Ɋ܂߂Ă悢
        Cursor cursor = null;
        try {
            cursor = getContentResolver().query(
                    PrivateProvider.Download.CONTENT_URI, null, null, null, null);

            // ���|�C���g5�� ����A�v��������̌��ʏ��ł����Ă��A��M�f�[�^�̈��S�����m�F����
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

        // ���|�C���g4�� ����A�v�����ւ̃��N�G�X�g�ł��邩��A�Z���V�e�B�u�ȏ������N�G�X�g�Ɋ܂߂Ă悢
        Uri uri = getContentResolver().insert(PrivateProvider.Download.CONTENT_URI, null);

        // ���|�C���g5�� ����A�v��������̌��ʏ��ł����Ă��A��M�f�[�^�̈��S�����m�F����
        // �T���v���ɂ������B�u3.2 ���̓f�[�^�̈��S�����m�F����v���Q�ƁB
        logLine("  uri:" + uri);
    }

    public void onUpdateClick(View view) {

        logLine("[Update]");

        // ���|�C���g4�� ����A�v�����ւ̃��N�G�X�g�ł��邩��A�Z���V�e�B�u�ȏ������N�G�X�g�Ɋ܂߂Ă悢
        int count = getContentResolver().update(PrivateProvider.Download.CONTENT_URI, null, null, null);

        // ���|�C���g5�� ����A�v��������̌��ʏ��ł����Ă��A��M�f�[�^�̈��S�����m�F����
        // �T���v���ɂ������B�u3.2 ���̓f�[�^�̈��S�����m�F����v���Q�ƁB
        logLine(String.format("  %s records updated", count));
    }

    public void onDeleteClick(View view) {

        logLine("[Delete]");

        // ���|�C���g4�� ����A�v�����ւ̃��N�G�X�g�ł��邩��A�Z���V�e�B�u�ȏ������N�G�X�g�Ɋ܂߂Ă悢
        int count = getContentResolver().delete(
                PrivateProvider.Download.CONTENT_URI, null, null);

        // ���|�C���g5�� ����A�v��������̌��ʏ��ł����Ă��A��M�f�[�^�̈��S�����m�F����
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
