package org.jssec.android.accountmanager.webservice;

public class WebService {

    /**
     * �I�����C���T�[�r�X�̃A�J�E���g�Ǘ��@�\�ɃA�N�Z�X����z��
     * 
     * @param username �A�J�E���g��������
     * @param password �p�X���[�h������
     * @return �F�؃g�[�N����Ԃ�
     */
    public String login(String username, String password) {
        // ���|�C���g7�� Authenticator�ƃI�����C���T�[�r�X�Ƃ̒ʐM��HTTPS�ōs��
        // ���ۂɂ́A�T�[�o�[�Ƃ̒ʐM�������������邪�A �T���v���ɂ�����
        return getAuthToken(username, password);
    }

    private String getAuthToken(String username, String password) {
        // ���ۂɂ̓T�[�o�[����A���j�[�N���Ɛ����s�\����ۏ؂��ꂽ�l���擾���邪
        // �T���v���ɂ��A�ʐM�͍s�킸�ɌŒ�l��Ԃ�
        return "c2f981bda5f34f90c0419e171f60f45c";
    }
}
