package org.jssec.android.permission.permissionrequestingpermissionatruntime;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_CODE_READ_CONTACTS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button)findViewById(R.id.button);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        readContacts();
    }

    private void readContacts() {
        // ★ポイント3★ Permissionがアプリに付与されているか確認する
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            // Permissionが付与されていない
            // ★ポイント4★ Permissionを要求する(ユーザーに許可を求めるダイアログを表示する)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CODE_READ_CONTACTS);
        } else {
            // Permissionがすでに付与されている
            showContactList();
        }
    }

    // ユーザー選択の結果を受けるコールバックメソッド
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_READ_CONTACTS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permissionの利用が許可されているので、連絡先情報を利用する処理を実行できる
                    showContactList();
                } else {
                    // Permissionの利用が許可されていないため、連絡先情報を利用する処理は実行できない
                    // ★ポイント5★ Permissionの利用が許可されていない場合の処理を実装する
                    Toast.makeText(this, String.format("連絡先の利用が許可されていません"), Toast.LENGTH_LONG).show();
                }
                return;
        }
    }

    // 連絡先一覧を表示
    private void showContactList() {
        // ContactListActivityを起動
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), ContactListActivity.class);
        startActivity(intent);
    }
}
