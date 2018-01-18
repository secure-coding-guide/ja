package org.jssec.android.autofillframework.autofillapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.EditText;

import org.jssec.android.autofillframework.R;

public class DisableAutofillActivity extends AppCompatActivity {

    private EditText mUsernameEditText;
    private EditText mPasswordEditText;

    private ActionMode.Callback mActionModeCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.disable_autofill_activity);

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
        mActionModeCallback = new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                removeAutofillFromMenu(menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                removeAutofillFromMenu(menu);
                return true;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
            }
        };

        //�t���[�e�B���O�c�[���o�[����u�������́v���폜����
        setMenu();
    }

    void setMenu() {
        if (mActionModeCallback == null) {
            return;
        }
         //Activity�Ɋ܂܂��S�Ă̕ҏW�\��TextView�ɂ��ăZ�b�g���邱��
         mUsernameEditText.setCustomInsertionActionModeCallback(mActionModeCallback);
         mPasswordEditText.setCustomInsertionActionModeCallback(mActionModeCallback);
    }

    //Menu�̊K�w�������āu�������́v���폜����
    void removeAutofillFromMenu(Menu menu) {
        if (menu.findItem(android.R.id.autofill) != null) {
            menu.removeItem(android.R.id.autofill);
        }

        for (int i=0; i<menu.size(); i++) {
            SubMenu submenu = menu.getItem(i).getSubMenu();
            if (submenu != null) {
                removeAutofillFromMenu(submenu);
            }
        }
    }

    private void login() {
        String username = mUsernameEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();

        //View����擾�����f�[�^�̈��S�����m�F����
        if (!Util.validateUsername(username) || Util.validatePassword(password)) {
            //�K�؂ȃG���[����������
        }

        //�T�[�o�[��username, password�𑗐M

        finish();
    }

    private void resetFields() {
        mUsernameEditText.setText("");
        mPasswordEditText.setText("");
    }

}
