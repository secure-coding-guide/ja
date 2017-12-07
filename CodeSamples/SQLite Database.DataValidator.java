package org.jssec.android.sqlite;

public class DataValidator {
    //入力値をチェックする
    //数字チェック
    public static boolean validateNo(String idno) {
        //null、空文字はOK
        if (idno == null || idno.length() == 0) {
           return true;
        }

        //数字であることを確認する
        try {
            if (!idno.matches("[1-9][0-9]*")) {
                //数字以外の時はエラー
                return false;
            }
        } catch (NullPointerException e) {
            //エラーを検出した
            return false;
        }

        return true;
    }

    // 文字列の長さを調べる
    public static boolean validateLength(String str, int max_length) {
       //null、空文字はOK
       if (str == null || str.length() == 0) {
               return true;
       }

       //文字列の長さがMAX以下であることを調べる
       try {
           if (str.length() > max_length) {
               //MAXより長い時はエラー
               return false;
           }
       } catch (NullPointerException e) {
           //バグ
           return false;
       }

       return true;
    }

   // 入力値チェック
    public static boolean validateData(String idno, String name, String info) {
       if (!validateNo(idno)) {
           return false;
        }
       if (!validateLength(name, CommonData.TEXT_DATA_LENGTH_MAX)) {
           return false;
        }else if (!validateLength(info, CommonData.TEXT_DATA_LENGTH_MAX)) {
           return false;
        }    
       return true;
    }
}
