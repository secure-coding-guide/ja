package org.jssec.android.provider.temporaryprovider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class TemporaryPassiveGrantActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.passive_grant);
    }

    // 一時的なアクセス許可を求めてきたアプリにContent Provider側アプリが受動的にアクセス許可を与えるケース
    public void onGrantClick(View view) {
        Intent intent = new Intent();

        // ★ポイント5★ 一時的にアクセスを許可するURIをIntentに指定する
        intent.setData(TemporaryProvider.Address.CONTENT_URI);

        // ★ポイント6★ 一時的に許可するアクセス権限をIntentに指定する
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        // ★ポイント8★ 一時許可の要求元アプリにIntentを返信する
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    public void onCloseClick(View view) {
        finish();
    }
}
