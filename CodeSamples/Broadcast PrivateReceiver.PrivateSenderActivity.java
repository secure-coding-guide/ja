package org.jssec.android.broadcast.privatereceiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class PrivateSenderActivity extends Activity {

    public void onSendNormalClick(View view) {
        // ���|�C���g4�� ����A�v����Receiver�̓N���X�w��̖����IIntent��Broadcast���M����
        Intent intent = new Intent(this, PrivateReceiver.class);

        // ���|�C���g5�� ���M��͓���A�v����Receiver�ł��邽�߁A�Z���V�e�B�u�ȏ��𑗐M���Ă悢
        intent.putExtra("PARAM", "�Z���V�e�B�u�ȏ�� from Sender");
        sendBroadcast(intent);
    }
    
    public void onSendOrderedClick(View view) {
        // ���|�C���g4�� ����A�v����Receiver�̓N���X�w��̖����IIntent��Broadcast���M����
        Intent intent = new Intent(this, PrivateReceiver.class);

        // ���|�C���g5�� ���M��͓���A�v����Receiver�ł��邽�߁A�Z���V�e�B�u�ȏ��𑗐M���Ă悢
        intent.putExtra("PARAM", "�Z���V�e�B�u�ȏ�� from Sender");
        sendOrderedBroadcast(intent, null, mResultReceiver, null, 0, null, null);
    }
    
    private BroadcastReceiver mResultReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            
            // ���|�C���g6�� ����A�v����Receiver����̌��ʏ��ł����Ă��A��M�f�[�^�̈��S�����m�F����
            // �T���v���ɂ������B�u3.2 ���̓f�[�^�̈��S�����m�F����v���Q�ƁB
            String data = getResultData();
            PrivateSenderActivity.this.logLine(
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
