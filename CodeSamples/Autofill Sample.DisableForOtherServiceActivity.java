package org.jssec.android.autofillframework.autofillapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.autofill.AutofillManager;
import android.widget.EditText;
import android.widget.TextView;

import org.jssec.android.autofillframework.R;

public class DisableForOtherServiceActivity extends AppCompatActivity {
    private boolean mIsAutofillEnabled = false;

    private EditText mUsernameEditText;
    private EditText mPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.disable_for_other_service_activity);

        mUsernameEditText = (EditText)findViewById(R.id.field_username);
        mPasswordEditText = (EditText)findViewById(R.id.field_password);

        findViewById(R.id.button_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        findViewById(R.id.button_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetFields();
            }
        });
        //このActivityではフローティングツールバーの対応をしていないため、
        // 「自動入力」の選択でAutofill機能が利用可能になります。
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateAutofillStatus();

        if (!mIsAutofillEnabled) {
            View rootView = this.getWindow().getDecorView();
            //同一パッケージ内のAutofill serviceを利用しない場合は、全てのViewをAutofillの対象外にする
            rootView.setImportantForAutofill(View.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS);
        } else {
            //同一パッケージ内のAutofill serviceを利用する場合は、全てのViewをAutofillの対象にする
            //特定のViewに対してView#setImportantForAutofill()を呼び出すことも可能
            View rootView = this.getWindow().getDecorView();
            rootView.setImportantForAutofill(View.IMPORTANT_FOR_AUTOFILL_AUTO);
        }
    }
    private void login() {
        String username = mUsernameEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();

        //Viewから取得したデータの安全性を確認する
        if (!Util.validateUsername(username) || !Util.validatePassword(password)) {
            //適切なエラー処理をする
        }

        //サーバーにusername, passwordを送信

        finish();
    }

    private void resetFields() {
        mUsernameEditText.setText("");
        mPasswordEditText.setText("");
    }

    private void updateAutofillStatus() {
        AutofillManager mgr = getSystemService(AutofillManager.class);

        mIsAutofillEnabled = mgr.hasEnabledAutofillServices();

        TextView statusView = (TextView) findViewById(R.id.label_autofill_status);
        String status = "自社のautofill serviceが--です";
        if (mIsAutofillEnabled) {
            status = "同一パッケージ内のautofill serviceが有効です";
        } else {
            status = "同一パッケージ内のautofill serviceが無効です";
        }
        statusView.setText(status);
    }
}
