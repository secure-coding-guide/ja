package org.jssec.android.service.inhouseservice.messengeruser;

import org.jssec.android.shared.PkgCert;
import org.jssec.android.shared.SigPerm;
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
import android.os.Messenger;
import android.os.RemoteException;
import android.view.View;
import android.widget.Toast;

public class InhouseMessengerUserActivity extends Activity {

    private boolean mIsBound;
    private Context mContext;

    // ���p���Activity���
    private static final String TARGET_PACKAGE =  "org.jssec.android.service.inhouseservice.messenger";
    private static final String TARGET_CLASS = "org.jssec.android.service.inhouseservice.messenger.InhouseMessengerService";

    // ���Ђ�Signature Permission
    private static final String MY_PERMISSION = "org.jssec.android.service.inhouseservice.messenger.MY_PERMISSION";

    // ���Ђ̏ؖ����̃n�b�V���l
    private static String sMyCertHash = null;
    private static String myCertHash(Context context) {
        if (sMyCertHash == null) {
            if (Utils.isDebuggable(context)) {
                // debug.keystore��"androiddebugkey"�̏ؖ����n�b�V���l
                sMyCertHash = "0EFB7236 328348A9 89718BAD DF57F544 D5CCB4AE B9DB34BC 1E29DD26 F77C8255";
            } else {
                // keystore��"my company key"�̏ؖ����n�b�V���l
                sMyCertHash = "D397D343 A5CBC10F 4EDDEB7C A10062DE 5690984F 1FB9E88B D7B3A7C2 42E142CA";
            }
        }
        return sMyCertHash;
    }

    // Service����f�[�^����M����Ƃ��ɗ��p����Messenger
    private Messenger mServiceMessenger = null;

    // Service�Ƀf�[�^�𑗐M����Ƃ��ɗ��p����Messenger
    private final Messenger mActivityMessenger = new Messenger(new ActivitySideHandler());

    // Service����󂯎����Message����������Handler
    private class ActivitySideHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CommonValue.MSG_SET_VALUE:
                    Bundle data = msg.getData();
                    String info = data.getString("key");
                    // ���|�C���g13�� ���ЃA�v������̌��ʏ��ł����Ă��A�l�̈��S�����m�F����
                    // �T���v���ɂ������B�u3.2 ���̓f�[�^�̈��S�����m�F����v���Q�ƁB
                    Toast.makeText(mContext, String.format("�T�[�r�X����u%s�v���擾�����B", info),
                            Toast.LENGTH_SHORT).show();
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    // Service�Ɛڑ����鎞�ɗ��p����R�l�N�V�����BbindService�Ŏ�������ꍇ�͕K�v�ɂȂ�B
    private ServiceConnection mConnection = new ServiceConnection() {

        // Service�ɐڑ����ꂽ�ꍇ�ɌĂ΂��
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            mServiceMessenger = new Messenger(service);
            Toast.makeText(mContext, "Connect to service", Toast.LENGTH_SHORT).show();

            try {
                // Service�Ɏ�����Messenger��n��
                Message msg = Message.obtain(null, CommonValue.MSG_REGISTER_CLIENT);
                msg.replyTo = mActivityMessenger;
                mServiceMessenger.send(msg);
            } catch (RemoteException e) {
                // Service���ُ�I�����Ă����ꍇ
            }
        }

        // Service���ُ�I�����āA�R�l�N�V�������ؒf���ꂽ�ꍇ�ɌĂ΂��
        @Override
        public void onServiceDisconnected(ComponentName className) {
            mServiceMessenger = null;
            Toast.makeText(mContext, "Disconnected from service", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.publicservice_activity);

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
        if (!mIsBound){
            // ���|�C���g9�� �Ǝ���`Signature Permission�����ЃA�v���ɂ���`����Ă��邱�Ƃ��m�F����
            if (!SigPerm.test(this, MY_PERMISSION, myCertHash(this))) {
                Toast.makeText(this, "�Ǝ���`Signature Permission�����ЃA�v���ɂ���`����Ă��Ȃ��B", Toast.LENGTH_LONG).show();
                return;
            }

            // ���|�C���g10�� ���p��A�v���̏ؖ��������Ђ̏ؖ����ł��邱�Ƃ��m�F����
            if (!PkgCert.test(this, TARGET_PACKAGE, myCertHash(this))) {
                Toast.makeText(this, "���p��T�[�r�X�͎��ЃA�v���ł͂Ȃ��B", Toast.LENGTH_LONG).show();
                return;
            }

          Intent intent = new Intent();

            // ���|�C���g11�� ���p��A�v���͎��ЃA�v���ł��邩��A�Z���V�e�B�u�ȏ��𑗐M���Ă��悢
          intent.putExtra("PARAM", "�Z���V�e�B�u�ȏ��");

            // ���|�C���g12�� �����IIntent�ɂ�莩�Ќ���Service���Ăяo��
          intent.setClassName(TARGET_PACKAGE, TARGET_CLASS);

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
        if (mServiceMessenger != null) {
            try {
                  // ���̑��M��v������
                Message msg = Message.obtain(null, CommonValue.MSG_SET_VALUE);
                mServiceMessenger.send(msg);
            } catch (RemoteException e) {
                // Service���ُ�I�����Ă����ꍇ
            }
         }
    }

}
