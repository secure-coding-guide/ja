package org.jssec.android.clipboard;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class ClipboardListeningActivity extends Activity {
    private static final String TAG = "ClipboardListeningActivity";
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clipboard_listening);
    }

    public void onClickStartService(View view) {
        if (view.getId() != R.id.start_service_button) {
            Log.w(TAG, "View IDが不正です");
        } else {
            ComponentName cn = startService(
                    new Intent(ClipboardListeningActivity.this, ClipboardListeningService.class));
            if (cn == null) {
                Log.e(TAG, "サービスの起動に失敗しました");
                Toast.makeText(this, "サービスの起動に失敗しました", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onClickStopService(View view) {
        if (view.getId() != R.id.stop_service_button) {
            Log.w(TAG, "View IDが不正です");
        } else {
            stopService(new Intent(ClipboardListeningActivity.this, ClipboardListeningService.class));
        }
    }
}
