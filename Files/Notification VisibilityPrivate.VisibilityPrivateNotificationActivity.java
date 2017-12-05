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
     * Private な Notification を表示する
     */
    private final int mNotificationId = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onSendNotificationClick(View view) {
        // ★ポイント1★  プライベート情報を含んだ通知を行う場合は、公開用（画面ロック時の表示用）のNotification を用意する
        Notification.Builder publicNotificationBuilder = new Notification.Builder(this).setContentTitle("Notification : Public");

        if (Build.VERSION.SDK_INT >= 21)
            publicNotificationBuilder.setVisibility(Notification.VISIBILITY_PUBLIC);
        // ★ポイント2★ 公開用（画面ロック時の表示用）の Notificationにはプライベート情報を含めない
        publicNotificationBuilder.setContentText("Visibility Public : Omitting sensitive data.");
        publicNotificationBuilder.setSmallIcon(R.drawable.ic_launcher);
        Notification publicNotification = publicNotificationBuilder.build();

        // プライベート情報を含む Notification を作成する
        Notification.Builder privateNotificationBuilder = new Notification.Builder(this).setContentTitle("Notification : Private");

        // ★ポイント3★ 明示的に Visibility を Private に設定して、Notification を作成する
        if (Build.VERSION.SDK_INT >= 21)
            privateNotificationBuilder.setVisibility(Notification.VISIBILITY_PRIVATE);
        // ★ポイント4★ Visibility が Private の場合、プライベート情報を含めて通知してもよい
        privateNotificationBuilder.setContentText("Visibility Private : Including user info.");
        privateNotificationBuilder.setSmallIcon(R.drawable.ic_launcher);
        // VisibilityがPrivateのNotificationを利用する場合、VisibilityをPublicにした公開用のNotificationを合わせて設定する
        if (Build.VERSION.SDK_INT >= 21)
            privateNotificationBuilder.setPublicVersion(publicNotification);

        Notification privateNotification = privateNotificationBuilder.build();

        // 本サンプルでは実装していないが、Notificationでは setContentIntent(PendingIntent intent) を使い
        // Notificationをクリックした際にIntentが送信されるように実装することが多い。
        // このときに設定するIntentは、呼び出すコンポーネントの種類に合わせて、
        // 安全な方法で呼び出すことが必要である（例えば、明示的Intentを使うなど）
        // 各コンポーネントの安全な呼び出し方法は以下の項目を参照のこと
        // 4.1. Activity を作る・利用する
        // 4.2. Broadcast を受信する・送信する
        // 4.4. Service を作る・利用する

        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(mNotificationId, privateNotification);
    }
}
