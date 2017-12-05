package org.jssec.android.file.privatefile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class PrivateUserActivity extends Activity {

    private TextView mFileView;

    private static final String FILE_NAME = "private_file.dat";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user);
        mFileView = (TextView) findViewById(R.id.file_view);
    }

    private void callFileActivity() {
        Intent intent = new Intent();
        intent.setClass(this, PrivateFileActivity.class);

        startActivity(intent);
    }

    /**
     * �t�@�C��Activity�̌Ăяo������
     * 
     * @param view
     */
    public void onCallFileActivityClick(View view) {
        callFileActivity();
    }

    /**
     * �t�@�C���̓ǂݍ��ݏ���
     * 
     * @param view
     */
    public void onReadFileClick(View view) {
        FileInputStream fis = null;
        try {
            fis = openFileInput(FILE_NAME);

            byte[] data = new byte[(int) fis.getChannel().size()];

            fis.read(data);

            // ���|�C���g4�� �t�@�C���Ɋi�[���ꂽ���ɑ΂��ẮA���̓����Ɋւ�炸���e�̈��S�����m�F����
            // �T���v���ɂ������B�u3.2 ���̓f�[�^�̈��S�����m�F����v���Q�ƁB
            String str = new String(data);

            mFileView.setText(str);
        } catch (FileNotFoundException e) {
            mFileView.setText(R.string.file_view);
        } catch (IOException e) {
            android.util.Log.d("PrivateFileActivity", "�t�@�C���̓Ǎ��Ɏ��s���܂���");
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    android.util.Log.d("PrivateFileActivity", "�t�@�C���̏I���Ɏ��s���܂���");
                }
            }
        }
    }

    /**
     * �t�@�C���̒ǋL����
     * 
     * @param view
     */
    public void onWriteFileClick(View view) {
        FileOutputStream fos = null;
        try {
            // ���|�C���g1�� �t�@�C���́A�A�v���P�[�V�����f�B���N�g�����ɍ쐬����
            // ���|�C���g2�� �t�@�C���̃A�N�Z�X���́A���̃A�v�������p�ł��Ȃ��悤�Ƀv���C�x�[�g���[�h�ɂ���
            fos = openFileOutput(FILE_NAME, MODE_APPEND);

            // ���|�C���g3�� �Z���V�e�B�u�ȏ����i�[���邱�Ƃ��ł���
            // ���|�C���g4�� �t�@�C���Ɋi�[������ɑ΂��ẮA���̓����Ɋւ�炸���e�̈��S�����m�F����
            // �T���v���ɂ������B�u3.2 ���̓f�[�^�̈��S�����m�F����v���Q�ƁB
            fos.write(new String("�Z���V�e�B�u�ȏ��(User Activity)\n").getBytes());
        } catch (FileNotFoundException e) {
            mFileView.setText(R.string.file_view);
        } catch (IOException e) {
            android.util.Log.d("PrivateFileActivity", "�t�@�C���̍쐬�Ɏ��s���܂���");
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    android.util.Log.d("PrivateFileActivity", "�t�@�C���̏I���Ɏ��s���܂���");
                }
            }
        }

        callFileActivity();
    }

}
