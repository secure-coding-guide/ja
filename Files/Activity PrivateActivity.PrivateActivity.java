package org.jssec.android.activity.privateactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class PrivateActivity extends Activity {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.private_activity);

        // ���|�C���g4�� ����A�v�������Intent�ł����Ă��A��MIntent�̈��S�����m�F����
        // �T���v���ɂ������B�u3.2 ���̓f�[�^�̈��S�����m�F����v���Q�ƁB
        String param = getIntent().getStringExtra("PARAM");
        Toast.makeText(this, String.format("�p�����[�^�u%s�v���󂯎�����B", param), Toast.LENGTH_LONG).show();
    }

    public void onReturnResultClick(View view) {
        
        // ���|�C���g5�� ���p���A�v���͓���A�v���ł��邩��A�Z���V�e�B�u�ȏ���ԑ����Ă悢
        Intent intent = new Intent();
        intent.putExtra("RESULT", "�Z���V�e�B�u�ȏ��");
        setResult(RESULT_OK, intent);
        finish();
    }
}
