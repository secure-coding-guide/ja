package org.jssec.android.activity.singleinstanceactivity;

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

        // ��MIntent�̈��S�����m�F����
        // �T���v���ɂ������B�u3.2 ���̓f�[�^�̈��S�����m�F����v���Q�ƁB
        String param = getIntent().getStringExtra("PARAM");
        Toast.makeText(this, String.format("�p�����[�^�u%s�v���󂯎�����B", param), Toast.LENGTH_LONG).show();
    }

    public void onReturnResultClick(View view) {    
        Intent intent = new Intent();
        intent.putExtra("RESULT", "�Z���V�e�B�u�ȏ��");
        setResult(RESULT_OK, intent);
        finish();
    }
}
