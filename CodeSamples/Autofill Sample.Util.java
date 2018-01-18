package org.jssec.android.autofillframework.autofillapp;

public class Util {
    private static int MIN_NAME_LEN = 1;
    private static int MAX_NAME_LEN = 16;
    private static int MIN_PASSWORD_LEN = 8;
    private static int MAX_PASSWORD_LEN = 16;

    public static boolean validateUsername(String username) {
        //適切にデータの安全性を確認すること
        if (username == null || username.length() < MIN_NAME_LEN || username.length() > MAX_NAME_LEN) {
            return false;
        }
        return true;
    }

    public static boolean validatePassword(String password) {
        //適切にデータの安全性を確認すること
        if (password == null || password.length() < MIN_PASSWORD_LEN || password.length() > MAX_PASSWORD_LEN) {
            return false;
        }
        return true;
    }
    void printLog() {

    }
}
