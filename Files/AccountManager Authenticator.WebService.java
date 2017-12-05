package org.jssec.android.accountmanager.webservice;

public class WebService {

    /**
     * オンラインサービスのアカウント管理機能にアクセスする想定
     * 
     * @param username アカウント名文字列
     * @param password パスワード文字列
     * @return 認証トークンを返す
     */
    public String login(String username, String password) {
        // ★ポイント7★ Authenticatorとオンラインサービスとの通信はHTTPSで行う
        // 実際には、サーバーとの通信処理を実装するが、 サンプルにつき割愛
        return getAuthToken(username, password);
    }

    private String getAuthToken(String username, String password) {
        // 実際にはサーバーから、ユニーク性と推測不可能性を保証された値を取得するが
        // サンプルにつき、通信は行わずに固定値を返す
        return "c2f981bda5f34f90c0419e171f60f45c";
    }
}
