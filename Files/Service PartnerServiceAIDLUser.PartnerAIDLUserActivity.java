package org.jssec.android.service.partnerservice.aidluser;

import org.jssec.android.service.partnerservice.aidl.IPartnerAIDLService;
import org.jssec.android.service.partnerservice.aidl.IPartnerAIDLServiceCallback;
import org.jssec.android.service.partnerservice.aidl.R;
import org.jssec.android.shared.PkgCertWhitelists;
import org.jssec.android.shared.Utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.view.View;
import android.widget.Toast;

public class PartnerAIDLUserActivity extends Activity {

    private boolean mIsBound;
    private Context mContext;
    
    private final static int MGS_VALUE_CHANGED = 1;
    
    // ���|�C���g6�� ���p��p�[�g�i�[����Service�A�v���̏ؖ������z���C�g���X�g�ɓo�^����Ă��邱�Ƃ��m�F����
    private static PkgCertWhitelists sWhitelists = null;
    private static void buildWhitelists(Context context) {
        boolean isdebug = Utils.isDebuggable(context);
        sWhitelists = new PkgCertWhitelists();
        
        // �p�[�g�i�[����Service�A�v�� org.jssec.android.service.partnerservice.aidl �̏ؖ����n�b�V���l��o�^
        sWhitelists.add("org.jssec.android.service.partnerservice.aidl", isdebug ?
                // debug.keystore��"androiddebugkey"�̏ؖ����n�b�V���l
                "0EFB7236 328348A9 89718BAD DF57F544 D5CCB4AE B9DB34BC 1E29DD26 F77C8255" :
                // keystore��"my company key"�̏ؖ����n�b�V���l
                "D397D343 A5CBC10F 4EDDEB7C A10062DE 5690984F 1FB9E88B D7B3A7C2 42E142CA");
        
        // �ȉ����l�ɑ��̃p�[�g�i�[����Service�A�v����o�^...
    }
    private static boolean checkPartner(Context context, String pkgname) {
        if (sWhitelists == null) buildWhitelists(context);
        return sWhitelists.test(context, pkgname);
    }

    // ���p��̃p�[�g�i�[����Activity�Ɋւ�����
    private static final String TARGET_PACKAGE =  "org.jssec.android.service.partnerservice.aidl";
    private static final String TARGET_CLASS = "org.jssec.android.service.partnerservice.aidl.PartnerAIDLService";

    private static class ReceiveHandler extends Handler{
        private Context mContext;

        public ReceiveHandler(Context context){
            this.mContext = context;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MGS_VALUE_CHANGED: {
                    String info = (String)msg.obj;
                    Toast.makeText(mContext, String.format("�R�[���o�b�N�Łu%s�v����M�����B", info), Toast.LENGTH_SHORT).show();
                    break;
                }
                default:
                    super.handleMessage(msg);
                    break;
            } // switch
        }

    }

    private final ReceiveHandler mHandler = new ReceiveHandler(this);

    // AIDL�Œ�`�����C���^�[�t�F�[�X�BService����̒ʒm���󂯎��B
    private final IPartnerAIDLServiceCallback.Stub mCallback =
        new IPartnerAIDLServiceCallback.Stub() {
            @Override
            public void valueChanged(String info) throws RemoteException {
                Message msg = mHandler.obtainMessage(MGS_VALUE_CHANGED, info);
                mHandler.sendMessage(msg);
            }
    };
    
    // AIDL�Œ�`�����C���^�[�t�F�[�X�BService�֒ʒm����B
    private IPartnerAIDLService mService = null;
    
    // Service�Ɛڑ����鎞�ɗ��p����R�l�N�V�����BbindService�Ŏ�������ꍇ�͕K�v�ɂȂ�B
    private ServiceConnection mConnection = new ServiceConnection() {

        // Service�ɐڑ����ꂽ�ꍇ�ɌĂ΂��
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            mService = IPartnerAIDLService.Stub.asInterface(service);
            
            try{
                // Service�ɐڑ�
                mService.registerCallback(mCallback);
                
            }catch(RemoteException e){
                // Service���ُ�I�������ꍇ
            }
            
            Toast.makeText(mContext, "Connected to service", Toast.LENGTH_SHORT).show();
        }

        // Service���ُ�I�����āA�R�l�N�V�������ؒf���ꂽ�ꍇ�ɌĂ΂��
        @Override
        public void onServiceDisconnected(ComponentName className) {
            Toast.makeText(mContext, "Disconnected from service", Toast.LENGTH_SHORT).show();
        }
    };
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.partnerservice_activity);

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
    public void onDestroy() {
        super.onDestroy();
        doUnbindService();
    }

    /**
     * Service�ɐڑ�����
     */
    private void doBindService() {
        if (!mIsBound){
            // ���|�C���g6�� ���p��p�[�g�i�[����Service�A�v���̏ؖ������z���C�g���X�g�ɓo�^����Ă��邱�Ƃ��m�F����
            if (!checkPartner(this, TARGET_PACKAGE)) {
                Toast.makeText(this, "���p�� Service �A�v���̓z���C�g���X�g�ɓo�^����Ă��Ȃ��B", Toast.LENGTH_LONG).show();
                return;
            }
            
            Intent intent = new Intent();
            
            // ���|�C���g7�� ���p��p�[�g�i�[����A�v���ɊJ�����Ă悢���Ɍ��著�M���Ă悢
            intent.putExtra("PARAM", "�p�[�g�i�[�A�v���ɊJ�����Ă悢���");
            
            // ���|�C���g8�� �����IIntent�ɂ��p�[�g�i�[����Service���Ăяo��
            intent.setClassName(TARGET_PACKAGE, TARGET_CLASS);
                 
          bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
          mIsBound = true;
        }
    }

    /**
     * Service�ւ̐ڑ���ؒf����
     */
    private void doUnbindService() {
        if (mIsBound) {
            // �o�^���Ă������W�X�^������ꍇ�͉���
            if(mService != null){
                try{
                    mService.unregisterCallback(mCallback);
                }catch(RemoteException e){
                    // Service���ُ�I�����Ă����ꍇ
                    // �T���v���ɂ������͊���
                }
            }
            
          unbindService(mConnection);
            
          Intent intent = new Intent();
        
           // ���|�C���g8�� �����IIntent�ɂ��p�[�g�i�[����Service���Ăяo��
          intent.setClassName(TARGET_PACKAGE, TARGET_CLASS);
 
          stopService(intent);
          
          mIsBound = false;
        }
    }

    /**
     * Service��������擾����
     */
    void getServiceinfo() {
        if (mIsBound && mService != null) {
            String info = null;
            
            try {
                // ���|�C���g7�� ���p��p�[�g�i�[����A�v���ɊJ�����Ă悢���Ɍ��著�M���Ă悢
                info = mService.getInfo("�p�[�g�i�[�A�v���ɊJ�����Ă悢���(method from activity)");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            // ���|�C���g9�� �p�[�g�i�[����A�v������̌��ʏ��ł����Ă��A��MIntent�̈��S�����m�F����
            // �T���v���ɂ������B�u3.2 ���̓f�[�^�̈��S�����m�F����v���Q�ƁB            
            Toast.makeText(mContext, String.format("�T�[�r�X����u%s�v���擾�����B", info), Toast.LENGTH_SHORT).show();
         }
    }
}
