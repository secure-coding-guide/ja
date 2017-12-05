package org.jssec.android.broadcast.publicreceiver;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.widget.Toast;

public class DynamicReceiverService extends Service {

    private static final String MY_BROADCAST_PUBLIC =
        "org.jssec.android.broadcast.MY_BROADCAST_PUBLIC";
    
    private PublicReceiver mReceiver;
    
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        
        // ���J���IBroadcast Receiver��o�^����
        mReceiver = new PublicReceiver();
        mReceiver.isDynamic = true;
        IntentFilter filter = new IntentFilter();
        filter.addAction(MY_BROADCAST_PUBLIC);
        filter.setPriority(1);  // �ÓIBroadcast Receiver��蓮�IBroadcast Receiver��D�悳����
        registerReceiver(mReceiver, filter);
        Toast.makeText(this,
                "���I Broadcast Receiver ��o�^�����B",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        
        // ���J���IBroadcast Receiver��o�^��������
        unregisterReceiver(mReceiver);
        mReceiver = null;
        Toast.makeText(this,
                "���I Broadcast Receiver ��o�^���������B",
                Toast.LENGTH_SHORT).show();
    }
}
