package org.jssec.android.provider.temporaryprovider;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class TemporaryActiveGrantActivity extends Activity {

    // User Activityに関する情報
    private static final String TARGET_PACKAGE =  "org.jssec.android.provider.temporaryuser";
    private static final String TARGET_ACTIVITY = "org.jssec.android.provider.temporaryuser.TemporaryUserActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.active_grant);
    }

    // Content Provider側アプリが能動的に他のアプリにアクセス許可を与えるケース
    public void onSendClick(View view) {
        try {
            Intent intent = new Intent();

            // ★ポイント5★ 一時的にアクセスを許可するURIをIntentに指定する
            intent.setData(TemporaryProvider.Address.CONTENT_URI);

            // ★ポイント6★ 一時的に許可するアクセス権限をIntentに指定する
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            // ★ポイント7★ 一時的にアクセスを許可するアプリに明示的Intentを送信する
            intent.setClassName(TARGET_PACKAGE, TARGET_ACTIVITY);
            startActivity(intent);

        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "User Activityが見つからない。", Toast.LENGTH_LONG).show();
        }
    }
}
