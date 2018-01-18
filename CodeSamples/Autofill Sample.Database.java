package org.jssec.android.autofillframework.autofillservice;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import static android.content.Context.MODE_PRIVATE;

import com.google.gson.Gson;

import java.util.ArrayList;

public class Database {
    private static final String TAG = "JssecAutofillSample";

    private Context mContext = null;
    static String DEFAULT_CREDLIST_STRING = "";
    static String CREDENTIAL_LIST = "credlist";
    static String PREF_FILENAME = "preference";

    ///////////////////////////////////////////
    // inner class
    public class Credential {
        public String username;
        public String pass;

        public Credential(String username, String pass) {
            this.username = username;
            this.pass = pass;
        }
    }
    private class CredentialList {
        private ArrayList<Credential> mCredentials;

        public CredentialList() {
            mCredentials = new ArrayList<>();
        }
        boolean add(Credential cred) {
            return mCredentials.add(cred);
        }
        public ArrayList<Credential> getAllList() {
            return mCredentials;
        }
        public void clearAll() {
            mCredentials.clear();
        }
        int getSize() {
            return mCredentials.size();
        }
    }
    ///////////////////////////////////////////

    public Database(Context context) {
        mContext = context;
    }

    public boolean add(String username, String pass) {
        //本来、情報の機密度に応じて適切に保存するべきである。
        SharedPreferences pref = mContext.getSharedPreferences(PREF_FILENAME,MODE_PRIVATE);
        String credString = pref.getString(CREDENTIAL_LIST, DEFAULT_CREDLIST_STRING);

        Gson gson = new Gson();
        CredentialList credList = createCredentialListFromString(gson, credString);

        //追加
        boolean ret = credList.add(new Credential(username, pass));
        Log.d(TAG, "Database::add() num=" + credList.getSize());
        credString = gson.toJson(credList);

        SharedPreferences.Editor editor = pref.edit();
        editor.putString(CREDENTIAL_LIST, credString);
        editor.commit();

        return ret;
    }
    public ArrayList<Credential> getAllList() {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_FILENAME,MODE_PRIVATE);
        String credString = pref.getString(CREDENTIAL_LIST, DEFAULT_CREDLIST_STRING);

        Gson gson = new Gson();
        CredentialList credList = createCredentialListFromString(gson, credString);

        return credList.getAllList();
    }

    public void clearAll() {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_FILENAME,MODE_PRIVATE);
        String credString = pref.getString(CREDENTIAL_LIST, DEFAULT_CREDLIST_STRING);

        Gson gson = new Gson();
        CredentialList credList = createCredentialListFromString(gson, credString);

        //クリア
        credList.clearAll();
        Log.d(TAG, "Database::clearAll() num=" + credList.getSize());
        credString = gson.toJson(credList);

        SharedPreferences.Editor editor = pref.edit();
        editor.putString(CREDENTIAL_LIST, credString);
        editor.commit();
    }

    private CredentialList createCredentialListFromString(Gson gson, String credString) {
        CredentialList credList = null;
        if (credString == null || credString.length() == 0) {
            //CredentialListはDBに最初にアクセスしたときに作成される
            credList = new CredentialList();
        } else {
            credList = gson.fromJson(credString, CredentialList.class);
        }
        return credList;
    }
}
