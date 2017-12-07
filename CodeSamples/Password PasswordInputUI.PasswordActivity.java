package org.jssec.android.password.passwordinputui;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

public class PasswordActivity extends Activity {

    // ��ԕۑ��p�̃L�[
    private static final String KEY_DUMMY_PASSWORD = "KEY_DUMMY_PASSWORD";

    // Activity����View
    private EditText mPasswordEdit;
    private CheckBox mPasswordDisplayCheck;

    // �p�X���[�h���_�~�[�\������\���t���O
    private boolean mIsDummyPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password_activity);

        // �X�N���[���L���v�`���𖳌�������
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);

        // View�̎擾
        mPasswordEdit = (EditText) findViewById(R.id.password_edit);
        mPasswordDisplayCheck = (CheckBox) findViewById(R.id.password_display_check);

        // �O����̓p�X���[�h�����邩
        if (getPreviousPassword() != null) {
            // ���|�C���g4�� Activity�����\�����ɑO����͂����p�X���[�h������ꍇ�A
            // �O����̓p�X���[�h�̌����𐄑�����Ȃ��悤�Œ茅���́������Ń_�~�[�\������

            // �\���̓_�~�[�p�X���[�h�ɂ���
            mPasswordEdit.setText("**********");
            // �p�X���[�h���͎��Ƀ_�~�[�p�X���[�h���N���A���邽�߁A�e�L�X�g�ύX���X�i�[��ݒ�
            mPasswordEdit.addTextChangedListener(new PasswordEditTextWatcher());
            // �_�~�[�p�X���[�h�t���O��ݒ肷��
            mIsDummyPassword = true;
        }

        // �p�X���[�h��\������I�v�V�����̃`�F�b�N�ύX���X�i�[��ݒ�
        mPasswordDisplayCheck
                .setOnCheckedChangeListener(new OnPasswordDisplayCheckedChangeListener());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // ��ʂ̏c���ύX��Activity���Đ�������Ȃ��悤�w�肵���ꍇ�ɂ͕s�v
        // Activity�̏�ԕۑ�
        outState.putBoolean(KEY_DUMMY_PASSWORD, mIsDummyPassword);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // ��ʂ̏c���ύX��Activity���Đ�������Ȃ��悤�w�肵���ꍇ�ɂ͕s�v
        // Activity�̏�Ԃ̕���
        mIsDummyPassword = savedInstanceState.getBoolean(KEY_DUMMY_PASSWORD);
    }

    /**
     * �p�X���[�h����͂����ꍇ�̏���
     */
    private class PasswordEditTextWatcher implements TextWatcher {

        public void beforeTextChanged(CharSequence s, int start, int count,
                int after) {
            // ���g�p
        }

        public void onTextChanged(CharSequence s, int start, int before,
                int count) {
            // ���|�C���g6�� �O����̓p�X���[�h���_�~�[�\�����Ă���Ƃ��A���[�U�[���p�X���[�h����͂��悤��
            // �����ꍇ�A�O����̓p�X���[�h���N���A���A���[�U�[�̓��͂�V���ȃp�X���[�h�Ƃ��Ĉ���
            if (mIsDummyPassword) {
                // �_�~�[�p�X���[�h�t���O��ݒ肷��
                mIsDummyPassword = false;
                // �p�X���[�h����͂������������ɂ���
                CharSequence work = s.subSequence(start, start + count);
                mPasswordEdit.setText(work);
                // �J�[�\���ʒu���ŏ��ɖ߂�̂ōŌ�ɂ���
                mPasswordEdit.setSelection(work.length());
            }
        }

        public void afterTextChanged(Editable s) {
            // ���g�p
        }

    }

    /**
     * �p�X���[�h�̕\���I�v�V�����`�F�b�N��ύX�����ꍇ�̏���
     */
    private class OnPasswordDisplayCheckedChangeListener implements
            OnCheckedChangeListener {

        public void onCheckedChanged(CompoundButton buttonView,
                boolean isChecked) {
            // ���|�C���g5�� �O����̓p�X���[�h���_�~�[�\�����Ă���Ƃ��A�u�p�X���[�h��\���v�����ꍇ�A
            // �O����̓p�X���[�h���N���A���āA�V�K�Ƀp�X���[�h����͂ł����ԂƂ���
            if (mIsDummyPassword && isChecked) {
                // �_�~�[�p�X���[�h�t���O��ݒ肷��
                mIsDummyPassword = false;
                // �p�X���[�h����\���ɂ���
                mPasswordEdit.setText(null);
            }

            // �J�[�\���ʒu���ŏ��ɖ߂�̂ō��̃J�[�\���ʒu���L������
            int pos = mPasswordEdit.getSelectionStart();

            // ���|�C���g2�� �p�X���[�h�𕽕��\������I�v�V������p�ӂ���
            // InputType�̍쐬
            int type = InputType.TYPE_CLASS_TEXT;
            if (isChecked) {
                // �`�F�b�NON���͕����\��
                type |= InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;
            } else {
                // �`�F�b�NOFF���̓}�X�N�\��
                type |= InputType.TYPE_TEXT_VARIATION_PASSWORD;
            }

            // �p�X���[�hEditText��InputType��ݒ�
            mPasswordEdit.setInputType(type);

            // �J�[�\���ʒu��ݒ肷��
            mPasswordEdit.setSelection(pos);
        }

    }

    // �ȉ��̃��\�b�h�̓A�v���ɍ��킹�Ď������邱��

    /**
     * �O����̓p�X���[�h���擾����
     *
     * @return �O����̓p�X���[�h
     */
    private String getPreviousPassword() {
        // �ۑ��p�X���[�h�𕜋A���������ꍇ�Ƀp�X���[�h�������Ԃ�
        // �p�X���[�h��ۑ����Ȃ��p�r�ł�null��Ԃ�
        return "hirake5ma";
    }

    /**
     * �L�����Z���{�^���̉�������
     *
     * @param view
     */
    public void onClickCancelButton(View view) {
        // Activity�����
        finish();
    }

    /**
     * OK�{�^���̉�������
     *
     * @param view
     */
    public void onClickOkButton(View view) {
        // password��ۑ�����Ƃ��F�؂Ɏg���Ƃ��K�v�ȏ������s��

        String password = null;

        if (mIsDummyPassword) {
            // �Ō�܂Ń_�~�[�p�X���[�h�\���������ꍇ�͑O����̓p�X���[�h���m��p�X���[�h�Ƃ���
            password = getPreviousPassword();
        } else {
            // �_�~�[�p�X���[�h�\������Ȃ��ꍇ�̓��[�U�[���̓p�X���[�h���m��p�X���[�h�Ƃ���
            password = mPasswordEdit.getText().toString();
        }

        // �p�X���[�h��Toast�\������
        Toast.makeText(this, "password is \"" + password + "\"",
                Toast.LENGTH_SHORT).show();

        // Activity�����
        finish();
    }
}
