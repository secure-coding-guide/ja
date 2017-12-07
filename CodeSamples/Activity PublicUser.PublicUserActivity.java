package org.jssec.android.activity.publicuser;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class PublicUserActivity extends Activity {

    private static final int REQUEST_CODE = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    public void onUseActivityClick(View view) {
        
        try {
            // ���|�C���g4�� �Z���V�e�B�u�ȏ��𑗐M���Ă͂Ȃ�Ȃ�
            Intent intent = new Intent("org.jssec.android.activity.MY_ACTION");
            intent.putExtra("PARAM", "�Z���V�e�B�u�ł͂Ȃ����");
            startActivityForResult(intent, REQUEST_CODE);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "���p��Activity��������Ȃ��B", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // ���|�C���g5�� ���ʂ��󂯎��ꍇ�A���ʃf�[�^�̈��S�����m�F����
        // �T���v���ɂ������B�u3.2 ���̓f�[�^�̈��S�����m�F����v���Q�ƁB
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
        case REQUEST_CODE:
            String result = data.getStringExtra("RESULT");
            Toast.makeText(this, String.format("���ʁu%s�v���󂯎�����B", result), Toast.LENGTH_LONG).show();
            break;
        }
    }
}
