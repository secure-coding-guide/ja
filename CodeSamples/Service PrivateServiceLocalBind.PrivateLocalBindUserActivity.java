package org.jssec.android.service.privateservice.localbind;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Toast;

public class PrivateLocalBindUserActivity extends Activity {

    private boolean mIsBound;
    private Context mContext;

    // Service�Ɏ�������C���^�[�t�F�[�X�́AIPrivateLocalBindService �N���X�Ƃ��Ē�`�Ă���
    private IPrivateLocalBindService mServiceInterface;
    
    // Service�Ɛڑ����鎞�ɗ��p����R�l�N�V�����BbindService�Ŏ�������ꍇ�͕K�v�ɂȂ�B
    private ServiceConnection mConnection = new ServiceConnection() {

        // Service�ɐڑ����ꂽ�ꍇ�ɌĂ΂��
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            mServiceInterface = ((PrivateLocalBindService.LocalBinder)service).getService();
            Toast.makeText(mContext, "Connect to service", Toast.LENGTH_SHORT).show();
        }
        // Service���ُ�I�����āA�R�l�N�V�������ؒf���ꂽ�ꍇ�ɌĂ΂��
        @Override
        public void onServiceDisconnected(ComponentName className) {
            // Service�͗��p�ł��Ȃ��̂�null���Z�b�g
            mServiceInterface = null;
            Toast.makeText(mContext, "Disconnected from service", Toast.LENGTH_SHORT).show();
        }
    };
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.privateservice_activity);

        mContext = this;
    }

    // �T�[�r�X�J�n�{�^��
    public void onStartServiceClick(View v) {
        // bindService�����s����
        doBindService();
    }
    
    // ���擾�{�^��
    public void onGetInfoClick(View v) {
        getServiceinfo();
    }
    
    // �T�[�r�X��~�{�^��
    public void onStopServiceClick(View v) {
        doUnbindService();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        doUnbindService();
    }

    /**
     * Service�ɐڑ�����
     */
    void doBindService() {
       if (!mIsBound)
        {
           // ���|�C���g1�� ����A�v����Service�̓N���X�w��̖����IIntent�ŌĂяo��  
           Intent intent = new Intent(this, PrivateLocalBindService.class);
           
           // ���|�C���g2�� ���p��A�v���͓���A�v���ł��邩��A�Z���V�e�B�u�ȏ��𑗐M���Ă��悢
            intent.putExtra("PARAM", "�Z���V�e�B�u�ȏ��(from activity)");
            
            bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
            mIsBound = true;
        }
    }

    /**
     * Service�ւ̐ڑ���ؒf����
     */
    void doUnbindService() {
        if (mIsBound) {
            unbindService(mConnection);
            mIsBound = false;
        }
    }

    /**
     * Service��������擾����
     */
    void getServiceinfo() {
        if (mIsBound) {
            String info = mServiceInterface.getInfo();
            
            Toast.makeText(mContext, String.format("�T�[�r�X����u%s�v���擾�����B", info), Toast.LENGTH_SHORT).show();
         }
    }
}
