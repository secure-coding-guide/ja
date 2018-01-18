package org.jssec.android.autofillframework.autofillapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import org.jssec.android.autofillframework.autofillapp.Util.*;

import org.jssec.android.autofillframework.R;

import static android.view.View.IMPORTANT_FOR_AUTOFILL_NO;

public class CredentialActivity extends AppCompatActivity {
    private EditText mUsernameEditText;
    private EditText mPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.credential_activity);

        mUsernameEditText = (EditText) findViewById(R.id.field_username);
        mPasswordEditText = (EditText) findViewById(R.id.field_password);

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
    }

    private void login() {
        String username = mUsernameEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();

        //Viewから取得したデータの安全性を確認する
        if (!Util.validateUsername(username) || Util.validatePassword(password)) {
            //適切なエラー処理をする
        }
        //サーバーにusername, passwordを送信

        finish();
    }


    private void resetFields() {
        mUsernameEditText.setText("");
        mPasswordEditText.setText("");
    }
}
