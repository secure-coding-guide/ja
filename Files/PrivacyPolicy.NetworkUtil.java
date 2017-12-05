package org.jssec.android.privacypolicy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

public class NetworkUtil {
    public static String getCookie(String _url, String sendCookie, String key)
            throws IOException {
        String cookie = null;
        HttpURLConnection urlConnection = null;

        try {
            // �T�[�o�[�ڑ�
            urlConnection = setupRequest(_url, sendCookie);
            urlConnection.connect();

            // Cookie�擾
            cookie = getResponseCookie(urlConnection, key);
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }

        return cookie;
    }

    public static String getJSON(String _url, String sendCookie, String key)
            throws IOException, JSONException {
        String data = null;
        HttpURLConnection urlConnection = null;

        try {
            // �T�[�o�[�ڑ�
            // JSON ��M�p�̐ݒ��ǉ�
            urlConnection = setupRequest(_url, sendCookie);
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.connect();

            // ���X�|���XBODY�擾
            String jsondata = getResponseBody(urlConnection);

            // JOSN�̃p�[�X
            JSONObject rootObject = new JSONObject(jsondata);
            data = rootObject.getString(key);
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }

        return data;
    }

    public static String sendJSON(String _url, String sendCookie,
            String jsondata) throws IOException {
        String data = null;
        HttpURLConnection urlConnection = null;

        try {
            // �T�[�o�[�ڑ�
            // JSON ���M�p�̐ݒ��ǉ�
            urlConnection = setupRequest(_url, sendCookie);
            urlConnection
                    .setRequestProperty("Content-Type", "application/json");
            urlConnection.setDoOutput(true);
            urlConnection.connect();

            // JOSN�f�[�^�����N�G�X�gBODY�ɃZ�b�g
            writeRequestBody(urlConnection, jsondata);

            // ���X�|���XBODY�擾
            data = getResponseBody(urlConnection);
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }

        return data;
    }

    private static HttpURLConnection setupRequest(String _url, String sendCookie)
            throws IOException {
        HttpURLConnection urlConnection;

        URL url = new URL(_url);
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Cookie", sendCookie);
        urlConnection.setDoInput(true);

        return urlConnection;
    }

    private static String getResponseBody(HttpURLConnection urlConnection)
            throws IOException {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        try {
            // BODY�擾
            br = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream()));
            String line;
            while ((line = br.readLine()) != null)
                sb.append(line + "\n");
        } finally {
            if (br != null)
                br.close();
        }
        return sb.toString();
    }

    private static String getResponseCookie(HttpURLConnection urlConnection,
            String key) throws IOException {
        // Cookie�擾
        List<String> cookieList = urlConnection.getHeaderFields().get(
                "Set-Cookie");
        if (cookieList != null && key != null) {
            for (String tmpCookie : cookieList) {
                if (tmpCookie.startsWith(key)) {
                    return tmpCookie.split(";")[0];
                }
            }
        }
        return null;
    }

    private static void writeRequestBody(HttpURLConnection urlConnection,
            String data) throws IOException {
        OutputStream os = null;
        try {
            // Request�@BODY�p�f�[�^�̐ݒ�
            byte[] outputBytes = data.getBytes("UTF-8");
            os = urlConnection.getOutputStream();
            os.write(outputBytes);
            os.flush();
        } finally {
            if (os != null)
                os.close();
        }
    }
}
