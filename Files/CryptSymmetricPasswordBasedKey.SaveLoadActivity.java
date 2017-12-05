package org.jssec.android.cryptsymmetricpasswordbasedkey;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SaveLoadActivity extends Activity {
    public final static int RESULT_SUCCEEDED = 0x00;
    public final static int RESULT_FAILED = 0x01;

    private final static String filename = "encrypted.dat";

    boolean saveMode = true;
    String memo = null;

    class CryptData {
        CryptData(final byte[] iv, final byte[] salt, final byte[] data) {
            mIV = iv;
            mSalt = salt;
            mData = data;
        }

        private byte[] mIV = null;
        private byte[] mSalt = null;
        private byte[] mData = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_load);

        Intent intent = getIntent();
        String buttonTitle = intent.getStringExtra("buttonTitle");
        memo = intent.getStringExtra("memo");
        saveMode = intent.getBooleanExtra("saveMode", true);

        Button buttonAction = (Button) findViewById(R.id.buttonAction);
        buttonAction.setText(buttonTitle);
    }

    public void onAction(View view) {
        TextView textViewPassword = (TextView) findViewById(R.id.editTextPassword);
        if (textViewPassword.getEditableText() != null) {
            char[] password = toChars(textViewPassword.getEditableText());
            clearEditableText(textViewPassword.getEditableText());
            if (saveMode == true) {
                AesCryptoPBEKey cipher = new AesCryptoPBEKey();
                byte[] encrypted = cipher.encrypt(memo.getBytes(), password);
                CryptData data = new CryptData(cipher.getIV(), cipher.getSalt(), encrypted);
                save(filename, data);
                setResult(RESULT_SUCCEEDED);
            } else {
                CryptData data = load(filename);
                AesCryptoPBEKey cipher = new AesCryptoPBEKey(data.mIV, data.mSalt);
                byte[] plain = cipher.decrypt(data.mData, password);
                Intent intent = new Intent();
                if (plain == null) {
                    setResult(RESULT_FAILED);
                } else {
                    memo = new String(plain);

                    intent.putExtra("memo", memo);
                    setResult(RESULT_SUCCEEDED, intent);
                }
            }
        }
        finish();
    }

    private char[] toChars(Editable editable) {
        char[] buf = new char[editable.length()];
        editable.getChars(0, editable.length(), buf, 0);
        return buf;
    }
    
    private void clearEditableText(Editable editable) {
        if (editable != null) {
            int length = editable.length();
            // editable�̓����o�b�t�@���N���A����ꍇ�A������̌�납��replace()�ŏ㏑�����邱�Ƃɂ���āA�Ӑ}���Ȃ������̃R�s�[��}���ł���
            for (int i = 0; i < length; i++) {
                editable.replace(length - i - 1, length - i, "?");
            }
        }
    }

    private CryptData load(final String name) {
        CryptData ret = null;

        FileInputStream fileInput = null;

        try {
            int length = 0;

            fileInput = openFileInput(name);

            length = fileInput.read();
            byte[] salt = new byte[length];
            fileInput.read(salt);

            length = fileInput.read();
            byte[] iv = new byte[length];
            fileInput.read(iv);

            long lengthEnc = fileInput.getChannel().size() - salt.length - iv.length - 2;

            byte[] encrypted = new byte[(int) lengthEnc];

            fileInput.read(encrypted);

            ret = new CryptData(iv, salt, encrypted);
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        } finally {
            if (fileInput != null) {
                try {
                    fileInput.close();
                } catch (IOException e) {
                }
            }
        }

        return ret;
    }

    private boolean save(final String name, CryptData data) {
        boolean ret = false;
        FileOutputStream fileOutput = null;

        try {
            fileOutput = openFileOutput(name, Context.MODE_PRIVATE);
            fileOutput.write(data.mSalt.length);
            fileOutput.write(data.mSalt);
            fileOutput.write(data.mIV.length);
            fileOutput.write(data.mIV);
            fileOutput.write(data.mData);
            ret = true;
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        } finally {
            if (fileOutput != null) {
                try {
                    fileOutput.close();
                } catch (IOException e) {
                }
            }
        }

        return ret;
    }

}
