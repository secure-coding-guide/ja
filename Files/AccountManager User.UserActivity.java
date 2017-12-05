package org.jssec.android.accountmanager.user;

import java.io.IOException;

import org.jssec.android.shared.PkgCert;
import org.jssec.android.shared.Utils;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorDescription;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class UserActivity extends Activity {

    // ���p����Authenticator�̏��
    private static final String JSSEC_ACCOUNT_TYPE = "org.jssec.android.accountmanager";
    private static final String JSSEC_TOKEN_TYPE = "webservice";
    private TextView mLogView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity);
        
        mLogView = (TextView)findViewById(R.id.logview);
    }

    public void addAccount(View view) {
        logLine();
        logLine("�V�����A�J�E���g��ǉ����܂�");

        // ���|�C���g1�� Authenticator�����K�̂��̂ł��邱�Ƃ��m�F���Ă���A�J�E���g���������{����
        if (!checkAuthenticator()) return;

        AccountManager am = AccountManager.get(this);
        am.addAccount(JSSEC_ACCOUNT_TYPE, JSSEC_TOKEN_TYPE, null, null, this,
                new AccountManagerCallback<Bundle>() {
                    @Override
                    public void run(AccountManagerFuture<Bundle> future) {
                        try {
                            Bundle result = future.getResult();
                            String type = result.getString(AccountManager.KEY_ACCOUNT_TYPE);
                            String name = result.getString(AccountManager.KEY_ACCOUNT_NAME);
                            if (type != null && name != null) {
                                logLine("�ȉ��̃A�J�E���g��ǉ����܂����F");
                                logLine("�@�@�A�J�E���g���: %s", type);
                                logLine("�@�@�A�J�E���g��: %s", name);
                            } else {
                                String code = result.getString(AccountManager.KEY_ERROR_CODE);
                                String msg = result.getString(AccountManager.KEY_ERROR_MESSAGE);
                                logLine("�A�J�E���g���ǉ��ł��܂���ł���");
                                logLine("  �G���[�R�[�h %s: %s", code, msg);
                            }
                        } catch (OperationCanceledException e) {
                        } catch (AuthenticatorException e) {
                        } catch (IOException e) {
                        }
                    }
                },
                null);
    }

    public void getAuthToken(View view) {
        logLine();
        logLine("�g�[�N�����擾���܂�");

        // ���|�C���g1�� Authenticator�����K�̂��̂ł��邱�Ƃ��m�F���Ă���A�J�E���g���������{����
        if (!checkAuthenticator()) return;

        AccountManager am = AccountManager.get(this);
        Account[] accounts = am.getAccountsByType(JSSEC_ACCOUNT_TYPE);
        if (accounts.length > 0) {
            Account account = accounts[0];
            am.getAuthToken(account, JSSEC_TOKEN_TYPE, null, this,
                    new AccountManagerCallback<Bundle>() {
                        @Override
                        public void run(AccountManagerFuture<Bundle> future) {
                            try {
                                Bundle result = future.getResult();
                                String name = result.getString(AccountManager.KEY_ACCOUNT_NAME);
                                String authtoken = result.getString(AccountManager.KEY_AUTHTOKEN);
                                logLine("  %s����̃g�[�N��:", name);
                                if (authtoken != null) {
                                    logLine("    %s", authtoken);
                                } else {
                                    logLine("    �擾�ł��܂���ł���");
                                }
                            } catch (OperationCanceledException e) {
                                logLine("  ��O: %s",e.getClass().getName());
                            } catch (AuthenticatorException e) {
                                logLine("  ��O: %s",e.getClass().getName());
                            } catch (IOException e) {
                                logLine("  ��O: %s",e.getClass().getName());
                            }
                        }
                    }, null);
        } else {
            logLine("�A�J�E���g���o�^����Ă��܂���");
        }
    }

    // ���|�C���g1�� Authenticator�����K�̂��̂ł��邱�Ƃ��m�F����
    private boolean checkAuthenticator() {
        AccountManager am = AccountManager.get(this);
        String pkgname = null;
        for (AuthenticatorDescription ad : am.getAuthenticatorTypes()) {
            if (JSSEC_ACCOUNT_TYPE.equals(ad.type)) {
                pkgname = ad.packageName;
                break;
            }
        }
        
        if (pkgname == null) {
            logLine("Authenticator��������܂���");
            return false;
        }
        
        logLine("  �A�J�E���g�^�C�v�F %s", JSSEC_ACCOUNT_TYPE);
        logLine("  Authenticator�̃p�b�P�[�W���F");
        logLine("    %s", pkgname);

        if (!PkgCert.test(this, pkgname, getTrustedCertificateHash(this))) {
            logLine("  ���K��Authenticator�ł͂���܂���i�ؖ����s��v�j");
            return false;
        }
        
        logLine("  ���K��Authenticator�ł�");
        return true;
    }

    // ���K��Authenticator�A�v���̏ؖ����n�b�V���l
    // �T���v���A�v�� JSSEC CertHash Checker �ŏؖ����n�b�V���l�͊m�F�ł���
    private String getTrustedCertificateHash(Context context) {
        if (Utils.isDebuggable(context)) {
            // debug.keystore��"androiddebugkey"�̏ؖ����n�b�V���l
            return "0EFB7236 328348A9 89718BAD DF57F544 D5CCB4AE B9DB34BC 1E29DD26 F77C8255";
        } else {
            // keystore��"my company key"�̏ؖ����n�b�V���l
            return "D397D343 A5CBC10F 4EDDEB7C A10062DE 5690984F 1FB9E88B D7B3A7C2 42E142CA";
        }
    }
    
    private void log(String str) {
        mLogView.append(str);
    }
    
    private void logLine(String line) {
        log(line + "\n");
    }
    
    private void logLine(String fmt, Object... args) {
        logLine(String.format(fmt, args));
    }

    private void logLine() {
        log("\n");
    }
}
