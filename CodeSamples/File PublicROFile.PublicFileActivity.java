package org.jssec.android.file.publicfile.readonly;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class PublicFileActivity extends Activity {

    private TextView mFileView;

    private static final String FILE_NAME = "public_file.dat";

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
            // ���|�C���g1�� �t�@�C���́A�A�v���f�B���N�g�����ɍ쐬����
            // ���|�C���g2�� �t�@�C���̃A�N�Z�X���́A���̃A�v���ɑ΂��Ă͓ǂݎ���p���[�h�ɂ���
            // �i�ǂݎ���p���[�h��MODE_WORLDREADABLE��API LEVEL 17��deplicated�ƂȂ������߁A
            //  �ɗ͎g�p�����AContentProvider�Ȃǂɂ��f�[�^�̂��������邱�Ɓj
            fos = openFileOutput(FILE_NAME, MODE_WORLD_READABLE);

            // ���|�C���g3�� �Z���V�e�B�u�ȏ��͊i�[���Ȃ�
            // ���|�C���g4�� �t�@�C���Ɋi�[������ɑ΂��ẮA���̓����Ɋւ�炸���e�̈��S�����m�F����
            // �T���v���ɂ������B�u3.2 ���̓f�[�^�̈��S�����m�F����v���Q�ƁB
            fos.write(new String("�Z���V�e�B�u�łȂ����(Public File Activity)\n")
                    .getBytes());
        } catch (FileNotFoundException e) {
            mFileView.setText(R.string.file_view);
        } catch (IOException e) {
            android.util.Log.e("PublicFileActivity", "�t�@�C���̍쐬�Ɏ��s���܂���");
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    android.util.Log.e("PublicFileActivity", "�t�@�C���̏I���Ɏ��s���܂���");
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
            fis = openFileInput(FILE_NAME);

            byte[] data = new byte[(int) fis.getChannel().size()];

            fis.read(data);

            String str = new String(data);

            mFileView.setText(str);
        } catch (FileNotFoundException e) {
            mFileView.setText(R.string.file_view);
        } catch (IOException e) {
            android.util.Log.e("PublicFileActivity", "�t�@�C���̓Ǎ��Ɏ��s���܂���");
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    android.util.Log.e("PublicFileActivity", "�t�@�C���̏I���Ɏ��s���܂���");
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

        File file = new File(this.getFilesDir() + "/" + FILE_NAME);
        file.delete();

        mFileView.setText(R.string.file_view);
    }
}
