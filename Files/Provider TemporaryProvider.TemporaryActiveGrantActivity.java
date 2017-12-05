package org.jssec.android.provider.temporaryprovider;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class TemporaryActiveGrantActivity extends Activity {

    // User Activity�Ɋւ�����
    private static final String TARGET_PACKAGE =  "org.jssec.android.provider.temporaryuser";
    private static final String TARGET_ACTIVITY = "org.jssec.android.provider.temporaryuser.TemporaryUserActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.active_grant);
    }

    // Content Provider���A�v�����\���I�ɑ��̃A�v���ɃA�N�Z�X����^����P�[�X
    public void onSendClick(View view) {
        try {
            Intent intent = new Intent();

            // ���|�C���g5�� �ꎞ�I�ɃA�N�Z�X��������URI��Intent�Ɏw�肷��
            intent.setData(TemporaryProvider.Address.CONTENT_URI);

            // ���|�C���g6�� �ꎞ�I�ɋ�����A�N�Z�X������Intent�Ɏw�肷��
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            // ���|�C���g7�� �ꎞ�I�ɃA�N�Z�X��������A�v���ɖ����IIntent�𑗐M����
            intent.setClassName(TARGET_PACKAGE, TARGET_ACTIVITY);
            startActivity(intent);

        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "User Activity��������Ȃ��B", Toast.LENGTH_LONG).show();
        }
    }
}
