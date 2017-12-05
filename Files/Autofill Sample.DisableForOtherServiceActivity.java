package org.jssec.android.autofillframework.autofillapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
            //����p�b�P�[�W����Autofill service�𗘗p���Ȃ��ꍇ�́A�S�Ă�View��Autofill�̑ΏۊO�ɂ���
            rootView.setImportantForAutofill(View.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS);
        } else {
            //����p�b�P�[�W����Autofill service�𗘗p����ꍇ�́A�S�Ă�View��Autofill�̑Ώۂɂ���
            //�����View�ɑ΂���View#setImportantForAutofill()���Ăяo�����Ƃ��\
            View rootView = this.getWindow().getDecorView();
            rootView.setImportantForAutofill(View.IMPORTANT_FOR_AUTOFILL_AUTO);
        }
    }
    private void login() {
        String username = mUsernameEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();

        //View����擾�����f�[�^�̈��S�����m�F����
        if (!Util.validateUsername(username) || !Util.validatePassword(password)) {
            //�K�؂ȃG���[����������
        }
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
        String status = "���Ђ�autofill service��--�ł�";
        if (mIsAutofillEnabled) {
            status = "����p�b�P�[�W����autofill service���L���ł�";
        } else {
            status = "����p�b�P�[�W����autofill service�������ł�";
        }
        statusView.setText(status);
    }
}
