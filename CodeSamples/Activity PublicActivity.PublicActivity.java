package org.jssec.android.activity.publicactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class PublicActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // ���|�C���g2�� ��MIntent�̈��S�����m�F����
        // ���JActivity�ł��邽�ߗ��p���A�v�����}���E�F�A�ł���\��������B
        // �T���v���ɂ������B�u3.2 ���̓f�[�^�̈��S�����m�F����v���Q�ƁB
        String param = getIntent().getStringExtra("PARAM");
        Toast.makeText(this, String.format("�p�����[�^�u%s�v���󂯎�����B", param), Toast.LENGTH_LONG).show();
    }

    public void onReturnResultClick(View view) {
        
        // ���|�C���g3�� ���ʂ�Ԃ��ꍇ�A�Z���V�e�B�u�ȏ����܂߂Ȃ�
        // ���JActivity�ł��邽�ߗ��p���A�v�����}���E�F�A�ł���\��������B
        // �}���E�F�A�Ɏ擾����Ă����̂Ȃ����ł���Ό��ʂƂ��ĕԂ��Ă��悢�B

        Intent intent = new Intent();
        intent.putExtra("RESULT", "�Z���V�e�B�u�ł͂Ȃ����");
        setResult(RESULT_OK, intent);
        finish();
    }
}
