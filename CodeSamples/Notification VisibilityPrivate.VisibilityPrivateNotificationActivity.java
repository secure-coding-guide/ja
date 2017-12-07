package org.jssec.notification.visibilityPrivate;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

public class VisibilityPrivateNotificationActivity extends Activity {
    /**
     * Private �� Notification ��\������
     */
    private final int mNotificationId = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onSendNotificationClick(View view) {
        // ���|�C���g1��  �v���C�x�[�g�����܂񂾒ʒm���s���ꍇ�́A���J�p�i��ʃ��b�N���̕\���p�j��Notification ��p�ӂ���
        Notification.Builder publicNotificationBuilder = new Notification.Builder(this).setContentTitle("Notification : Public");

        if (Build.VERSION.SDK_INT >= 21)
            publicNotificationBuilder.setVisibility(Notification.VISIBILITY_PUBLIC);
        // ���|�C���g2�� ���J�p�i��ʃ��b�N���̕\���p�j�� Notification�ɂ̓v���C�x�[�g�����܂߂Ȃ�
        publicNotificationBuilder.setContentText("Visibility Public : Omitting sensitive data.");
        publicNotificationBuilder.setSmallIcon(R.drawable.ic_launcher);
        Notification publicNotification = publicNotificationBuilder.build();

        // �v���C�x�[�g�����܂� Notification ���쐬����
        Notification.Builder privateNotificationBuilder = new Notification.Builder(this).setContentTitle("Notification : Private");

        // ���|�C���g3�� �����I�� Visibility �� Private �ɐݒ肵�āANotification ���쐬����
        if (Build.VERSION.SDK_INT >= 21)
            privateNotificationBuilder.setVisibility(Notification.VISIBILITY_PRIVATE);
        // ���|�C���g4�� Visibility �� Private �̏ꍇ�A�v���C�x�[�g�����܂߂Ēʒm���Ă��悢
        privateNotificationBuilder.setContentText("Visibility Private : Including user info.");
        privateNotificationBuilder.setSmallIcon(R.drawable.ic_launcher);
        // Visibility��Private��Notification�𗘗p����ꍇ�AVisibility��Public�ɂ������J�p��Notification�����킹�Đݒ肷��
        if (Build.VERSION.SDK_INT >= 21)
            privateNotificationBuilder.setPublicVersion(publicNotification);

        Notification privateNotification = privateNotificationBuilder.build();

        // �{�T���v���ł͎������Ă��Ȃ����ANotification�ł� setContentIntent(PendingIntent intent) ���g��
        // Notification���N���b�N�����ۂ�Intent�����M�����悤�Ɏ������邱�Ƃ������B
        // ���̂Ƃ��ɐݒ肷��Intent�́A�Ăяo���R���|�[�l���g�̎�ނɍ��킹�āA
        // ���S�ȕ��@�ŌĂяo�����Ƃ��K�v�ł���i�Ⴆ�΁A�����IIntent���g���Ȃǁj
        // �e�R���|�[�l���g�̈��S�ȌĂяo�����@�͈ȉ��̍��ڂ��Q�Ƃ̂���
        // 4.1. Activity �����E���p����
        // 4.2. Broadcast ����M����E���M����
        // 4.4. Service �����E���p����

        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(mNotificationId, privateNotification);
    }
}
