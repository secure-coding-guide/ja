package org.jssec.android.provider.privateprovider;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class PrivateUserActivity extends Activity {

    public void onQueryClick(View view) {

        logLine("[Query]");

        // ★ポイント4★ 同一アプリ内へのリクエストであるから、センシティブな情報をリクエストに含めてよい
        Cursor cursor = null;
        try {
            cursor = getContentResolver().query(
                    PrivateProvider.Download.CONTENT_URI, null, null, null, null);

            // ★ポイント5★ 同一アプリ内からの結果情報であっても、受信データの安全性を確認する
            // サンプルにつき割愛。「3.2 入力データの安全性を確認する」を参照。
            if (cursor == null) {
                logLine("  null cursor");
            } else {
                boolean moved = cursor.moveToFirst();
                while (moved) {
                    logLine(String.format("  %d, %s", cursor.getInt(0), cursor.getString(1)));
                    moved = cursor.moveToNext();
                }
            }
        }
        finally {
            if (cursor != null) cursor.close();
        }
    }

    public void onInsertClick(View view) {

        logLine("[Insert]");

        // ★ポイント4★ 同一アプリ内へのリクエストであるから、センシティブな情報をリクエストに含めてよい
        Uri uri = getContentResolver().insert(PrivateProvider.Download.CONTENT_URI, null);

        // ★ポイント5★ 同一アプリ内からの結果情報であっても、受信データの安全性を確認する
        // サンプルにつき割愛。「3.2 入力データの安全性を確認する」を参照。
        logLine("  uri:" + uri);
    }

    public void onUpdateClick(View view) {

        logLine("[Update]");

        // ★ポイント4★ 同一アプリ内へのリクエストであるから、センシティブな情報をリクエストに含めてよい
        int count = getContentResolver().update(PrivateProvider.Download.CONTENT_URI, null, null, null);

        // ★ポイント5★ 同一アプリ内からの結果情報であっても、受信データの安全性を確認する
        // サンプルにつき割愛。「3.2 入力データの安全性を確認する」を参照。
        logLine(String.format("  %s records updated", count));
    }

    public void onDeleteClick(View view) {

        logLine("[Delete]");

        // ★ポイント4★ 同一アプリ内へのリクエストであるから、センシティブな情報をリクエストに含めてよい
        int count = getContentResolver().delete(
                PrivateProvider.Download.CONTENT_URI, null, null);

        // ★ポイント5★ 同一アプリ内からの結果情報であっても、受信データの安全性を確認する
        // サンプルにつき割愛。「3.2 入力データの安全性を確認する」を参照。
        logLine(String.format("  %s records deleted", count));
    }

    private TextView mLogView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mLogView = (TextView)findViewById(R.id.logview);
    }

    private void logLine(String line) {
        mLogView.append(line);
        mLogView.append("\n");
    }
}
