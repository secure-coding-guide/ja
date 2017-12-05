package org.jssec.android.activity.privateactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class PrivateUserActivity extends Activity {

    private static final int REQUEST_CODE = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity);
    }
    
    public void onUseActivityClick(View view) {
        
        // ���|�C���g6�� Activity�ɑ��M����Intent�ɂ́A�t���OFLAG_ACTIVITY_NEW_TASK��ݒ肵�Ȃ�
        // ���|�C���g7�� ����A�v����Activity�̓N���X�w��̖����IIntent�ŌĂяo��
        Intent intent = new Intent(this, PrivateActivity.class);
        
        // ���|�C���g8�� ���p��A�v���͓���A�v���ł��邩��A�Z���V�e�B�u�ȏ���putExtra()���g���ꍇ�Ɍ��著�M���Ă��悢
        intent.putExtra("PARAM", "�Z���V�e�B�u�ȏ��");
        
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) return;
        
        switch (requestCode) {
        case REQUEST_CODE:
            String result = data.getStringExtra("RESULT");
            
            // ���|�C���g9�� ����A�v����Activity����̌��ʏ��ł����Ă��A��M�f�[�^�̈��S�����m�F����
            // �T���v���ɂ������B�u3.2 ���̓f�[�^�̈��S�����m�F����v���Q�ƁB
            Toast.makeText(this, String.format("���ʁu%s�v���󂯎�����B", result), Toast.LENGTH_LONG).show();
            break;
        }
    }
}
