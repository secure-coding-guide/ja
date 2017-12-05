package org.jssec.android.file.externalfile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ExternalFileActivity extends Activity {

    private TextView mFileView;

    private static final String TARGET_TYPE = "external";

    private static final String FILE_NAME = "external_file.dat";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file);

        mFileView = (TextView) findViewById(R.id.file_view);
    }

    /**
     * �t�@�C���̍쐬����
     * 
     * @param view
     */
    public void onCreateFileClick(View view) {
        FileOutputStream fos = null;
        try {
            // ���|�C���g1�� �Z���V�e�B�u�ȏ��͊i�[���Ȃ�
            // ���|�C���g2�� �A�v�����Ƀ��j�[�N�ȃf�B���N�g���Ƀt�@�C����z�u����
            File file = new File(getExternalFilesDir(TARGET_TYPE), FILE_NAME);
            fos = new FileOutputStream(file, false);

            // ���|�C���g3�� �t�@�C���Ɋi�[������ɑ΂��ẮA���̓����Ɋւ�炸���e�̈��S�����m�F����
            // �T���v���ɂ������B�u3.2 ���̓f�[�^�̈��S�����m�F����v���Q�ƁB
            fos.write(new String("�Z���V�e�B�u�łȂ����(External File Activity)\n")
                    .getBytes());
        } catch (FileNotFoundException e) {
            mFileView.setText(R.string.file_view);
        } catch (IOException e) {
            android.util.Log.e("ExternalFileActivity", "�t�@�C���̓Ǎ��Ɏ��s���܂���");
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    android.util.Log.e("ExternalUserActivity", "�t�@�C���̏I���Ɏ��s���܂���");
                }
            }
        }

        finish();
    }

    /**
     * �t�@�C���̓ǂݍ��ݏ���
     * 
     * @param view
     */
    public void onReadFileClick(View view) {
        FileInputStream fis = null;
        try {
            File file = new File(getExternalFilesDir(TARGET_TYPE), FILE_NAME);
            fis = new FileInputStream(file);

            byte[] data = new byte[(int) fis.getChannel().size()];

            fis.read(data);

            // ���|�C���g3�� �t�@�C���Ɋi�[���ꂽ���ɑ΂��ẮA���̓����Ɋւ�炸���e�̈��S�����m�F����
            // �T���v���ɂ������B�u3.2 ���̓f�[�^�̈��S�����m�F����v���Q�ƁB
            String str = new String(data);

            mFileView.setText(str);
        } catch (FileNotFoundException e) {
            mFileView.setText(R.string.file_view);
        } catch (IOException e) {
            android.util.Log.e("ExternalFileActivity", "�t�@�C���̓Ǎ��Ɏ��s���܂���");
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    android.util.Log.e("ExternalFileActivity", "�t�@�C���̏I���Ɏ��s���܂���");
                }
            }
        }
    }

    /**
     * �t�@�C���̍폜����
     * 
     * @param view
     */
    public void onDeleteFileClick(View view) {

        File file = new File(getExternalFilesDir(TARGET_TYPE), FILE_NAME);
        file.delete();

        mFileView.setText(R.string.file_view);
    }
}
