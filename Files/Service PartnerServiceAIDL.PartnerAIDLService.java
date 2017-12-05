package org.jssec.android.service.partnerservice.aidl;

import org.jssec.android.shared.PkgCertWhitelists;
import org.jssec.android.shared.Utils;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.widget.Toast;

public class PartnerAIDLService extends Service {
       private static final int REPORT_MSG = 1;
       private static final int GETINFO_MSG = 2;
    
    // Service����N���C�A���g�ɒʒm����l
    private int mValue = 0;

    // ���|�C���g2�� ���p���A�v���̏ؖ������z���C�g���X�g�ɓo�^����Ă��邱�Ƃ��m�F����
    private static PkgCertWhitelists sWhitelists = null;
    private static void buildWhitelists(Context context) {
        boolean isdebug = Utils.isDebuggable(context);
        sWhitelists = new PkgCertWhitelists();
        
        // �p�[�g�i�[�A�v�� org.jssec.android.service.partnerservice.aidluser �̏ؖ����n�b�V���l��o�^
        sWhitelists.add("org.jssec.android.service.partnerservice.aidluser", isdebug ?
                // debug.keystore��"androiddebugkey"�̏ؖ����n�b�V���l
                "0EFB7236 328348A9 89718BAD DF57F544 D5CCB4AE B9DB34BC 1E29DD26 F77C8255" :
                // keystore��"partner key"�̏ؖ����n�b�V���l
                "1F039BB5 7861C27A 3916C778 8E78CE00 690B3974 3EB8259F E2627B8D 4C0EC35A");
        
        // �ȉ����l�ɑ��̃p�[�g�i�[�A�v����o�^...
    }
    
    private static boolean checkPartner(Context context, String pkgname) {
        if (sWhitelists == null) buildWhitelists(context);
        return sWhitelists.test(context, pkgname);
    }
    
    // �R�[���o�b�N��o�^����I�u�W�F�N�g�B
    // RemoteCallbackList �̒񋟂��郁�\�b�h�̓X���b�h�Z�[�t�ɂȂ��Ă���B
    private final RemoteCallbackList<IPartnerAIDLServiceCallback> mCallbacks =
        new RemoteCallbackList<>();

    // �R�[���o�b�N�ɑ΂���Service����f�[�^�𑗐M���邽�߂�Handler
    protected static class ServiceHandler extends Handler{

        private Context mContext;
        private RemoteCallbackList<IPartnerAIDLServiceCallback> mCallbacks;
        private int mValue = 0;

        public ServiceHandler(Context context, RemoteCallbackList<IPartnerAIDLServiceCallback> callback, int value){
            this.mContext = context;
            this.mCallbacks = callback;
            this.mValue = value;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REPORT_MSG: {
                    if(mCallbacks == null){
                        return;
                    }
                    // �ʒm���J�n����
                    // beginBroadcast()�́AgetBroadcastItem()�Ŏ擾�\�ȃR�s�[���쐬���Ă���
                    final int N = mCallbacks.beginBroadcast();
                    for (int i = 0; i < N; i++) {
                        IPartnerAIDLServiceCallback target = mCallbacks.getBroadcastItem(i);
                        try {
                            // ���|�C���g5�� �p�[�g�i�[�A�v���ɊJ�����Ă悢���Ɍ��著�M���Ă悢
                            target.valueChanged("�p�[�g�i�[�A�v���ɊJ�����Ă悢���(callback from Service) No." + (++mValue));

                        } catch (RemoteException e) {
                            // RemoteCallbackList���R�[���o�b�N���Ǘ����Ă���̂ŁA�����ł�unregeister���Ȃ�
                            // RemoteCallbackList.kill()�ɂ���đS�ĉ��������
                        }
                    }
                    // finishBroadcast()�́AbeginBroadcast()�Ƒ΂ɂȂ鏈��
                    mCallbacks.finishBroadcast();

                    // 10�b��ɌJ��Ԃ�
                    sendEmptyMessageDelayed(REPORT_MSG, 10000);
                    break;
                }
                case GETINFO_MSG: {
                    if(mContext != null) {
                        Toast.makeText(mContext,
                                (String) msg.obj, Toast.LENGTH_LONG).show();
                    }
                    break;
                }
                default:
                    super.handleMessage(msg);
                    break;
            } // switch
        }
    }

    protected final ServiceHandler mHandler = new ServiceHandler(this, mCallbacks, mValue);

    // AIDL�Œ�`�����C���^�[�t�F�[�X
    private final IPartnerAIDLService.Stub mBinder = new IPartnerAIDLService.Stub() {
        private boolean checkPartner() {
            Context ctx = PartnerAIDLService.this;
            if (!PartnerAIDLService.checkPartner(ctx, Utils.getPackageNameFromUid(ctx, getCallingUid()))) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(PartnerAIDLService.this, "���p���A�v���̓p�[�g�i�[�A�v���ł͂Ȃ��B", Toast.LENGTH_LONG).show();
                    }
                });
                return false;
            }
           return true;
        }
        public void registerCallback(IPartnerAIDLServiceCallback cb) {
            // ���|�C���g2�� ���p���A�v���̏ؖ������z���C�g���X�g�ɓo�^����Ă��邱�Ƃ��m�F����
          if (!checkPartner()) {
                return;
            }
          if (cb != null) mCallbacks.register(cb);
        }
        public String getInfo(String param) {

            // ���|�C���g2�� ���p���A�v���̏ؖ������z���C�g���X�g�ɓo�^����Ă��邱�Ƃ��m�F����
          if (!checkPartner()) {
                return null;
            }
            // ���|�C���g4�� �p�[�g�i�[�A�v�������Intent�ł����Ă��A��MIntent�̈��S�����m�F����
            // �T���v���ɂ������B�u3.2 ���̓f�[�^�̈��S�����m�F����v���Q�ƁB
          Message msg = new Message();
          msg.what = GETINFO_MSG;
          msg.obj = String.format("�p�[�g�i�[�A�v������̃��\�b�h�Ăяo���B�u%s�v����M�����B", param);
          PartnerAIDLService.this.mHandler.sendMessage(msg);
            
            // ���|�C���g5�� �p�[�g�i�[�A�v���ɊJ�����Ă悢���Ɍ���ԑ����Ă悢
            return "�p�[�g�i�[�A�v���ɊJ�����Ă悢���(method from Service)";
        }
        
        public void unregisterCallback(IPartnerAIDLServiceCallback cb) {
            // ���|�C���g2�� ���p���A�v���̏ؖ������z���C�g���X�g�ɓo�^����Ă��邱�Ƃ��m�F����
           if (!checkPartner()) {
                return;
            }
 
           if (cb != null) mCallbacks.unregister(cb);
        }
    };
    
    @Override
    public IBinder onBind(Intent intent) {
        // ���|�C���g3�� onBind�ŌĂяo�������p�[�g�i�[���ǂ������ʂł��Ȃ�
        // AIDL �Œ�`�������\�b�h�̌Ăяo�����Ƀ`�F�b�N���K�v�ɂȂ�B

        return mBinder;
    }
    
    @Override
    public void onCreate() {
        Toast.makeText(this, this.getClass().getSimpleName() + " - onCreate()", Toast.LENGTH_SHORT).show();       

        // Service�����s���̊Ԃ́A����I�ɃC���N�������g����������ʒm����
        mHandler.sendEmptyMessage(REPORT_MSG);

    }
    
    @Override
    public void onDestroy() {
        Toast.makeText(this, this.getClass().getSimpleName() + " - onDestroy()", Toast.LENGTH_SHORT).show();
        
        // �R�[���o�b�N��S�ĉ�������
        mCallbacks.kill();
        
        mHandler.removeMessages(REPORT_MSG);
    }

}
