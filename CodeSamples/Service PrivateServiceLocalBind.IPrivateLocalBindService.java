package org.jssec.android.service.privateservice.localbind;

/**
 * Serviceが提供するメソッドを定義する。
 * 本クラス内のメソッドは、Activityから呼び出す。
 */
public interface IPrivateLocalBindService {

    /**
     * センシティブな情報（文字列）を取得する
     */
    String getInfo();
}
