package org.jssec.android.provider.temporaryprovider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class TemporaryPassiveGrantActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.passive_grant);
    }

    // �ꎞ�I�ȃA�N�Z�X�������߂Ă����A�v����Content Provider���A�v�����󓮓I�ɃA�N�Z�X����^����P�[�X
    public void onGrantClick(View view) {
        Intent intent = new Intent();

        // ���|�C���g5�� �ꎞ�I�ɃA�N�Z�X��������URI��Intent�Ɏw�肷��
        intent.setData(TemporaryProvider.Address.CONTENT_URI);

        // ���|�C���g6�� �ꎞ�I�ɋ�����A�N�Z�X������Intent�Ɏw�肷��
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        // ���|�C���g8�� �ꎞ���̗v�����A�v����Intent��ԐM����
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    public void onCloseClick(View view) {
        finish();
    }
}
