package org.jssec.android.permission.permissionrequestingpermissionatruntime;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_CODE_READ_CONTACTS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button)findViewById(R.id.button);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        readContacts();
    }

    private void readContacts() {
        // ���|�C���g3�� Permission���A�v���ɕt�^����Ă��邩�m�F����
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            // Permission���t�^����Ă��Ȃ�
            // ���|�C���g4�� Permission��v������(���[�U�[�ɋ������߂�_�C�A���O��\������)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CODE_READ_CONTACTS);
        } else {
            // Permission�����łɕt�^����Ă���
            showContactList();
        }
    }

    // ���[�U�[�I���̌��ʂ��󂯂�R�[���o�b�N���\�b�h
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_READ_CONTACTS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission�̗��p��������Ă���̂ŁA�A������𗘗p���鏈�������s�ł���
                    showContactList();
                } else {
                    // Permission�̗��p��������Ă��Ȃ����߁A�A������𗘗p���鏈���͎��s�ł��Ȃ�
                    // ���|�C���g5�� Permission�̗��p��������Ă��Ȃ��ꍇ�̏�������������
                    Toast.makeText(this, String.format("�A����̗��p��������Ă��܂���"), Toast.LENGTH_LONG).show();
                }
                return;
        }
    }

    // �A����ꗗ��\��
    private void showContactList() {
        // ContactListActivity���N��
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), ContactListActivity.class);
        startActivity(intent);
    }
}
