package org.jssec.android.service.inhouseservice.messengeruser;

public class CommonValue {
    /**
     * クライアントとして登録する時のコマンド。
     * Message.replyToフィールドにクライアントのMessengerをセットする。 
     */
    public static final int MSG_REGISTER_CLIENT = 1;

    /**
     * クライアントの登録解除を行う場合のコマンド。
     * Message.replyToフィールドにクライアントのMessengerをセットする。 
     */
    public static final int MSG_UNREGISTER_CLIENT = 2;

    /**
     * Serviceの保持している値を、登録されているクライアントに送信するコマンド。
     */
    public static final int MSG_SET_VALUE = 3;
}
