package org.jssec.android.broadcast.publicsender;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class PublicSenderActivity extends Activity {
    
    private static final String MY_BROADCAST_PUBLIC =
        "org.jssec.android.broadcast.MY_BROADCAST_PUBLIC";
    
    public void onSendNormalClick(View view) {
        // ���|�C���g4�� �Z���V�e�B�u�ȏ��𑗐M���Ă͂Ȃ�Ȃ�
        Intent intent = new Intent(MY_BROADCAST_PUBLIC);
        intent.putExtra("PARAM", "�Z���V�e�B�u�ł͂Ȃ���� from Sender");
        sendBroadcast(intent);
    }
    
    public void onSendOrderedClick(View view) {
        // ���|�C���g4�� �Z���V�e�B�u�ȏ��𑗐M���Ă͂Ȃ�Ȃ�
        Intent intent = new Intent(MY_BROADCAST_PUBLIC);
        intent.putExtra("PARAM", "�Z���V�e�B�u�ł͂Ȃ���� from Sender");
        sendOrderedBroadcast(intent, null, mResultReceiver, null, 0, null, null);
    }
    
    public void onSendStickyClick(View view) {
        // ���|�C���g4�� �Z���V�e�B�u�ȏ��𑗐M���Ă͂Ȃ�Ȃ�
        Intent intent = new Intent(MY_BROADCAST_PUBLIC);
        intent.putExtra("PARAM", "�Z���V�e�B�u�ł͂Ȃ���� from Sender");
        //sendStickyBroadcast���\�b�h��API Level 21��deprecated�ƂȂ���
        sendStickyBroadcast(intent);
    }

    public void onSendStickyOrderedClick(View view) {
        // ���|�C���g4�� �Z���V�e�B�u�ȏ��𑗐M���Ă͂Ȃ�Ȃ�
        Intent intent = new Intent(MY_BROADCAST_PUBLIC);
        intent.putExtra("PARAM", "�Z���V�e�B�u�ł͂Ȃ���� from Sender");
        //sendStickyBroadcast���\�b�h��API Level 21��deprecated�ƂȂ���
        sendStickyOrderedBroadcast(intent, mResultReceiver, null, 0, null, null);
    }
    
    public void onRemoveStickyClick(View view) {
        Intent intent = new Intent(MY_BROADCAST_PUBLIC);
        //removeStickyBroadcast���\�b�h��deprecated�ƂȂ��Ă���
        removeStickyBroadcast(intent);
    }

    private BroadcastReceiver mResultReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            
            // ���|�C���g5�� ���ʂ��󂯎��ꍇ�A���ʃf�[�^�̈��S�����m�F����
            // �T���v���ɂ������B�u3.2 ���̓f�[�^�̈��S�����m�F����v���Q�ƁB
            String data = getResultData();
            PublicSenderActivity.this.logLine(
                    String.format("���ʁu%s�v����M�����B", data));
        }
    };

    private TextView mLogView;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mLogView = (TextView)findViewById(R.id.logview);
    }
    
    private void logLine(String line) {
        mLogView.append(line);
        mLogView.append("\n");
    }
}
