package org.jssec.android.accountmanager.authenticator;

import org.jssec.android.accountmanager.webservice.WebService;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

public class LoginActivity extends AccountAuthenticatorActivity {
    private static final String TAG = AccountAuthenticatorActivity.class.getSimpleName();
    private String mReAuthName = null;
    private EditText mNameEdit = null;
    private EditText mPassEdit = null;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        
        // �A���[�g�A�C�R���\��
        requestWindowFeature(Window.FEATURE_LEFT_ICON);
        setContentView(R.layout.login_activity);
        getWindow().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,
                android.R.drawable.ic_dialog_alert);

        // widget�������Ă���
        mNameEdit = (EditText) findViewById(R.id.username_edit);
        mPassEdit = (EditText) findViewById(R.id.password_edit);
        
        // ���|�C���g3�� ���O�C�����Activity�͌��JActivity�Ƃ��đ��̃A�v������̍U���A�N�Z�X��z�肷��
        // �O�����͂�Intent#extras��String�^��RE_AUTH_NAME������������Ȃ�
        // ���̊O������String��TextEdit#setText()�AWebService#login()�Anew Account()��
        // �����Ƃ��ēn����邪�A�ǂ�ȕ����񂪗^�����Ă���肪�N���Ȃ����Ƃ��m�F���Ă���
        mReAuthName = getIntent().getStringExtra(JssecAuthenticator.RE_AUTH_NAME);
        if (mReAuthName != null) {
            // ���[�U�[���w���LoginActivity���Ăяo���ꂽ�̂ŁA���[�U�[����ҏW�s�Ƃ���
            mNameEdit.setText(mReAuthName);
            mNameEdit.setInputType(InputType.TYPE_NULL);
            mNameEdit.setFocusable(false);
            mNameEdit.setEnabled(false);
        }
    }

    // ���O�C���{�^���������Ɏ��s�����
    public void handleLogin(View view) {
        String name = mNameEdit.getText().toString();
        String pass = mPassEdit.getText().toString();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(pass)) {
            // ���͒l���s���ł���ꍇ�̏���
            setResult(RESULT_CANCELED);
            finish();
        }

        // ���͂��ꂽ�A�J�E���g���ɂ��I�����C���T�[�r�X�Ƀ��O�C������
        WebService web = new WebService();
        String authToken = web.login(name, pass);
        if (TextUtils.isEmpty(authToken)) {
            // �F�؂����s�����ꍇ�̏���
            setResult(RESULT_CANCELED);
            finish();
        }
        
        //�@�ȉ��A���O�C���������̏���

        // ���|�C���g5�� �A�J�E���g����F�؃g�[�N���Ȃǂ̃Z���V�e�B�u�ȏ��̓��O�o�͂��Ȃ�
        Log.i(TAG, "WebService login succeeded");


        if (mReAuthName == null) {
            // ���O�C�����������A�J�E���g��AccountManager�ɓo�^����
            // ���|�C���g6�� Account Manager�Ƀp�X���[�h��ۑ����Ȃ�
            AccountManager am = AccountManager.get(this);
            Account account = new Account(name, JssecAuthenticator.JSSEC_ACCOUNT_TYPE);
            am.addAccountExplicitly(account, null, null);
            am.setAuthToken(account, JssecAuthenticator.JSSEC_AUTHTOKEN_TYPE, authToken);
            Intent intent = new Intent();
            intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, name);
            intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE,
                    JssecAuthenticator.JSSEC_ACCOUNT_TYPE);
            setAccountAuthenticatorResult(intent.getExtras());
            setResult(RESULT_OK, intent);
        } else {
            // �F�؃g�[�N����ԋp����
            Bundle bundle = new Bundle();
            bundle.putString(AccountManager.KEY_ACCOUNT_NAME, name);
            bundle.putString(AccountManager.KEY_ACCOUNT_TYPE,
                    JssecAuthenticator.JSSEC_ACCOUNT_TYPE);
            bundle.putString(AccountManager.KEY_AUTHTOKEN, authToken);
            setAccountAuthenticatorResult(bundle);
            setResult(RESULT_OK);
        }
        finish();
    }
}
