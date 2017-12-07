package org.jssec.android.service.inhouseservice.messenger;

import org.jssec.android.shared.SigPerm;
import org.jssec.android.shared.Utils;

import java.util.ArrayList;
import java.util.Iterator;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.widget.Toast;

public class InhouseMessengerService extends Service{
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
    
    
    // Service�̃N���C�A���g(�f�[�^���M��)�����X�g�ŊǗ�����
    private ArrayList<Messenger> mClients = new ArrayList<Messenger>();
    
    // �N���C�A���g����̃f�[�^����M����Ƃ��ɗ��p����Messenger
    private final Messenger mMessenger = new Messenger(new ServiceSideHandler(mClients));
    
    // �N���C�A���g����󂯎����Message����������Handler
    private static class ServiceSideHandler extends Handler{

        private ArrayList<Messenger> mClients;

        public ServiceSideHandler(ArrayList<Messenger> clients){
            this.mClients = clients;
        }

        @Override
        public void handleMessage(Message msg){
            switch(msg.what){
            case CommonValue.MSG_REGISTER_CLIENT:
                // �N���C�A���g����󂯎����Messenger��ǉ�
                mClients.add(msg.replyTo);
                break;
            case CommonValue.MSG_UNREGISTER_CLIENT:
                mClients.remove(msg.replyTo);
                break;
            case CommonValue.MSG_SET_VALUE:
                // �N���C�A���g�Ƀf�[�^�𑗂�
                sendMessageToClients(mClients);
                break;
            default:
                super.handleMessage(msg);
               break;
            }
        }
    }

    /**
     * �N���C�A���g�Ƀf�[�^�𑗂�
     */
    private static void sendMessageToClients(ArrayList<Messenger> mClients){

        // ���|�C���g6�� ���p���A�v���͎��ЃA�v���ł��邩��A�Z���V�e�B�u�ȏ���ԑ����Ă悢
        String sendValue = "�Z���V�e�B�u�ȏ��(from Service)";

        // �o�^����Ă���N���C�A���g�ցA���Ԃɑ��M����
        // ���[�v�r����remove���Ă��S�Ẵf�[�^�ɃA�N�Z�X�������̂�Iterator�𗘗p����
        Iterator<Messenger> ite = mClients.iterator();
        while(ite.hasNext()){
            try {
                Message sendMsg = Message.obtain(null, CommonValue.MSG_SET_VALUE, null);

                Bundle data = new Bundle();
                data.putString("key", sendValue);
                sendMsg.setData(data);

                Messenger next = ite.next();
                next.send(sendMsg);
                
            } catch (RemoteException e) {
                // �N���C�A���g�����݂��Ȃ��ꍇ�́A���X�g�����菜��
                ite.remove();
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {

        // ���|�C���g4�� �Ǝ���`Signature Permission�����ЃA�v���ɂ���`����Ă��邱�Ƃ��m�F����
        if (!SigPerm.test(this, MY_PERMISSION, myCertHash(this))) {
            Toast.makeText(this, "�Ǝ���`Signature Permission�����ЃA�v���ɂ���`����Ă��Ȃ��B", Toast.LENGTH_LONG).show();
            return null;
        }

        // ���|�C���g5�� ���ЃA�v�������Intent�ł����Ă��A��MIntent�̈��S�����m�F����
        // �T���v���ɂ������B�u3.2 ���̓f�[�^�̈��S�����m�F����v���Q�ƁB
        String param = intent.getStringExtra("PARAM");
        Toast.makeText(this, String.format("�p�����[�^�u%s�v���󂯎�����B", param), Toast.LENGTH_LONG).show();

        return mMessenger.getBinder();
    }

    @Override
    public void onCreate() {
        Toast.makeText(this, "Service - onCreate()", Toast.LENGTH_SHORT).show();
    }
    
    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service - onDestroy()", Toast.LENGTH_SHORT).show();
    }
}
