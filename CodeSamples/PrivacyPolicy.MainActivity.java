package org.jssec.android.privacypolicy;

import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;
import org.jssec.android.privacypolicy.ConfirmFragment.DialogListener;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;

import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener, DialogListener {
    private static final String BASE_URL = "https://www.example.com/pp";
    private static final String GET_ID_URI = BASE_URL + "/get_id.php";
    private static final String SEND_DATA_URI = BASE_URL + "/send_data.php";
    private static final String DEL_ID_URI = BASE_URL + "/del_id.php";

    private static final String ID_KEY = "id";
    private static final String LOCATION_KEY = "location";
    private static final String NICK_NAME_KEY = "nickname";

    private static final String PRIVACY_POLICY_COMPREHENSIVE_AGREED_KEY = "privacyPolicyComprehensiveAgreed";
    private static final String PRIVACY_POLICY_DISCRETE_TYPE1_AGREED_KEY = "privacyPolicyDiscreteType1Agreed";

    private static final String PRIVACY_POLICY_PREF_NAME = "privacypolicy_preference";
    private static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 257;

    private String UserId = "";
    private LocationClient mLocationClient = null;

    private final int DIALOG_TYPE_COMPREHENSIVE_AGREEMENT = 1;
    private final int DIALOG_TYPE_PRE_CONFIRMATION = 2;

    private static final int VERSION_TO_SHOW_COMPREHENSIVE_AGREEMENT_ANEW = 1;

    private TextWatcher watchHandler = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            boolean buttonEnable = (s.length() > 0);

            MainActivity.this.findViewById(R.id.buttonStart).setEnabled(buttonEnable);
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ���[�U�[���ʗpID���T�[�o�[����擾����
        new GetDataAsyncTask().execute();

        findViewById(R.id.buttonStart).setEnabled(false);
        ((TextView) findViewById(R.id.editTextNickname)).addTextChangedListener(watchHandler);

        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode == ConnectionResult.SUCCESS) {
            mLocationClient = new LocationClient(this, this, this);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences pref = getSharedPreferences(PRIVACY_POLICY_PREF_NAME, MODE_PRIVATE);
        int privacyPolicyAgreed = pref.getInt(PRIVACY_POLICY_COMPREHENSIVE_AGREED_KEY, -1);

        if (privacyPolicyAgreed <= VERSION_TO_SHOW_COMPREHENSIVE_AGREEMENT_ANEW) {
            // ���|�C���g1�� ����N����(�A�b�v�f�[�g��)�ɁA�A�v�����������p�ҏ��̑��M�ɂ��ĕ���ӂ𓾂�
            // �A�b�v�f�[�g���ɂ��ẮA�V�������p�ҏ��������悤�ɂȂ����ꍇ�ɂ̂݁A�ēx����ӂ𓾂�K�v������B
            ConfirmFragment dialog = ConfirmFragment.newInstance(R.string.privacyPolicy, R.string.agreePrivacyPolicy, DIALOG_TYPE_COMPREHENSIVE_AGREEMENT);
            dialog.setDialogListener(this);
            FragmentManager fragmentManager = getSupportFragmentManager();
            dialog.show(fragmentManager, "dialog");
        }

        // Location���擾�p
        if (mLocationClient != null) {
            mLocationClient.connect();
        }
    }

    @Override
    protected void onStop() {
        if (mLocationClient != null) {
            mLocationClient.disconnect();
        }
        super.onStop();
    }

    public void onSendToServer(View view) {
        // �T�d�Ȏ�舵�������߂��闘�p�ҏ��𑗐M�ɂ��Ċ��ɓ��ӂ𓾂Ă��邩�m�F����
        // ���ۂɂ͑��M������̎�ʖ��ɓ��ӂ𓾂�K�v�����邱�Ƃɒ��ӂ��邱��
        SharedPreferences pref = getSharedPreferences(PRIVACY_POLICY_PREF_NAME, MODE_PRIVATE);
        int privacyPolicyAgreed = pref.getInt(PRIVACY_POLICY_DISCRETE_TYPE1_AGREED_KEY, -1);
        if (privacyPolicyAgreed <= VERSION_TO_SHOW_COMPREHENSIVE_AGREEMENT_ANEW) {
            // ���|�C���g3�� �T�d�Ȏ�舵�������߂��闘�p�ҏ��𑗐M����ꍇ�́A�ʂɃ��[�U�[�̓��ӂ𓾂�
            ConfirmFragment dialog = ConfirmFragment.newInstance(R.string.sendLocation, R.string.cofirmSendLocation, DIALOG_TYPE_PRE_CONFIRMATION);
            dialog.setDialogListener(this);
            FragmentManager fragmentManager = getSupportFragmentManager();
            dialog.show(fragmentManager, "dialog");
        } else {
            // ���Ӎς݂̂��߁A���M�������J�n����
            onPositiveButtonClick(DIALOG_TYPE_PRE_CONFIRMATION);
        }
    }

    public void onPositiveButtonClick(int type) {
        if (type == DIALOG_TYPE_COMPREHENSIVE_AGREEMENT) {
            // ���|�C���g1�� ����N����(�A�b�v�f�[�g��)�ɁA�A�v�����������p�ҏ��̑��M�ɂ��ĕ���ӂ𓾂�
            SharedPreferences.Editor pref = getSharedPreferences(PRIVACY_POLICY_PREF_NAME, MODE_PRIVATE).edit();
            pref.putInt(PRIVACY_POLICY_COMPREHENSIVE_AGREED_KEY, getVersionCode());
            pref.apply();
        } else if (type == DIALOG_TYPE_PRE_CONFIRMATION) {
            // ���|�C���g3�� �T�d�Ȏ�舵�������߂��闘�p�ҏ��𑗐M����ꍇ�́A�ʂɃ��[�U�[�̓��ӂ𓾂�
            if (mLocationClient != null && mLocationClient.isConnected()) {
                Location currentLocation = mLocationClient.getLastLocation();
                if (currentLocation != null) {
                    String locationData = "Latitude:" + currentLocation.getLatitude() + ", Longitude:" + currentLocation.getLongitude();
                    String nickname = ((TextView) findViewById(R.id.editTextNickname)).getText().toString();

                    Toast.makeText(MainActivity.this, this.getClass().getSimpleName() + "\n - nickname : " + nickname + "\n - location : " + locationData, Toast.LENGTH_SHORT).show();

                    new SendDataAsyncTack().execute(SEND_DATA_URI, UserId, locationData, nickname);
                }
            }
            // ���ӂ𓾂��|�A��Ԃ�ۑ�����
            // ���ۂɂ͑��M������̎�ʖ��ɓ��ӂ𓾂�K�v�����邱�Ƃɒ��ӂ��邱��
            SharedPreferences.Editor pref = getSharedPreferences(PRIVACY_POLICY_PREF_NAME, MODE_PRIVATE).edit();
            pref.putInt(PRIVACY_POLICY_DISCRETE_TYPE1_AGREED_KEY, getVersionCode());
            pref.apply();
        }
    }

    public void onNegativeButtonClick(int type) {
        if (type == DIALOG_TYPE_COMPREHENSIVE_AGREEMENT) {
            // ���|�C���g2�� ���[�U�[�̕���ӂ������Ă��Ȃ��ꍇ�́A���p�ҏ��̑��M�͂��Ȃ�
            // �T���v���A�v���ł̓A�v���P�[�V�������I������
            finish();
        } else if (type == DIALOG_TYPE_PRE_CONFIRMATION) {
            // ���|�C���g4�� ���[�U�[�̌ʓ��ӂ������Ă��Ȃ��ꍇ�́A�Y�����̑��M�͂��Ȃ�
            // ���[�U�[���ӂ������Ȃ������̂ŉ������Ȃ�
        }
    }

    private int getVersionCode() {
        int versionCode = -1;
        PackageManager packageManager = this.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(this.getPackageName(), PackageManager.GET_ACTIVITIES);
            versionCode = packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            // ��O�����͊���
        }

        return versionCode;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_show_pp:
                // ���|�C���g5�� ���[�U�[���A�v���E�v���C�o�V�[�|���V�[���m�F�ł����i��p�ӂ���
                Intent intent = new Intent();
                intent.setClass(this, WebViewAssetsActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_del_id:
                // ���|�C���g6�� ���M�����������[�U�[����ɂ��폜�����i��p�ӂ���
                new SendDataAsyncTack().execute(DEL_ID_URI, UserId);
                return true;
            case R.id.action_donot_send_id:
                // ���|�C���g7�� ���[�U�[����ɂ�藘�p�ҏ��̑��M���~�����i��p�ӂ���

                // ���p�ҏ��̑��M���~�����ꍇ�A����ӂɊւ��铯�ӂ͔j�����ꂽ���̂Ƃ���
                SharedPreferences.Editor pref = getSharedPreferences(PRIVACY_POLICY_PREF_NAME, MODE_PRIVATE).edit();
                pref.putInt(PRIVACY_POLICY_COMPREHENSIVE_AGREED_KEY, 0);
                pref.apply();

                // �{�T���v���ł͗��p�ҏ��𑗐M���Ȃ��ꍇ�A���[�U�[�ɒ񋟂���@�\�������Ȃ邽��
                // ���̒i�K�ŃA�v�����I������B���̏����̓A�v�����̓s���ɍ��킹�ĕύX���邱�ƁB
                String message  = getString(R.string.stopSendUserData);
                Toast.makeText(MainActivity.this, this.getClass().getSimpleName() + " - " + message, Toast.LENGTH_SHORT).show();
                finish();

                return true;
        }

        return false;
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        if (mLocationClient != null && mLocationClient.isConnected()) {
            Location currentLocation = mLocationClient.getLastLocation();
            if (currentLocation != null) {
                String locationData = "Latitude \t: " + currentLocation.getLatitude() + "\n\tLongitude \t: " + currentLocation.getLongitude();

                String text = "\n" + getString(R.string.your_location_title) + "\n\t" + locationData;

                TextView appText = (TextView) findViewById(R.id.appText);
                appText.setText(text);
            }
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (result.hasResolution()) {
            try {
                result.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDisconnected() {
        mLocationClient = null;
    }

    private class GetDataAsyncTask extends AsyncTask<String, Void, String> {
        private String extMessage = "";

        @Override
        protected String doInBackground(String... params) {
            // ���|�C���g8�� ���p�ҏ��̕R�Â��ɂ�UUID/cookie�𗘗p����
            // �{�T���v���ł̓T�[�o�[���Ő�������ID�𗘗p����
            SharedPreferences sp = getSharedPreferences(PRIVACY_POLICY_PREF_NAME, MODE_PRIVATE);
            UserId = sp.getString(ID_KEY, null);
            if (UserId == null) {
                // SharedPreferences���Ƀg�[�N�������݂��Ȃ��߁A�T�[�o�[����ID�����񂹂�B
                try {
                    UserId = NetworkUtil.getCookie(GET_ID_URI, "", "id");
                } catch (IOException e) {
                    // �ؖ����G���[�Ȃǂ̗�O���L���b�`����
                    extMessage = e.toString();
                }

                // ���񂹂�ID��SharedPreferences�ɕۑ�����B
                sp.edit().putString(ID_KEY, UserId).commit();
            }
            return UserId;
        }

        @Override
        protected void onPostExecute(final String data) {
            String status = (data != null) ? "success" : "error";
            Toast.makeText(MainActivity.this, this.getClass().getSimpleName() + " - " + status + " : " + extMessage, Toast.LENGTH_SHORT).show();
        }
    }

    private class SendDataAsyncTack extends AsyncTask<String, Void, Boolean> {
        private String extMessage = "";

        @Override
        protected Boolean doInBackground(String... params) {
            String url = params[0];
            String id = params[1];
            String location = params.length > 2 ? params[2] : null;
            String nickname = params.length > 3 ? params[3] : null;

            Boolean result = false;
            try {
                JSONObject jsonData = new JSONObject();
                jsonData.put(ID_KEY, id);
                if (location != null)
                    jsonData.put(LOCATION_KEY, location);

                if (nickname != null)
                    jsonData.put(NICK_NAME_KEY, nickname);

                NetworkUtil.sendJSON(url, "", jsonData.toString());

                result = true;
            } catch (IOException e) {
                // �ؖ����G���[�Ȃǂ̗�O���L���b�`����
                extMessage = e.toString();
            } catch (JSONException e) {
                extMessage = e.toString();
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            String status = result ? "Success" : "Error";
            Toast.makeText(MainActivity.this, this.getClass().getSimpleName() + " - " + status + " : " + extMessage, Toast.LENGTH_SHORT).show();
        }
    }
}
