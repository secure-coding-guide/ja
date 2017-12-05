package org.jssec.android.sqlite;

public class DataValidator {
    //���͒l���`�F�b�N����
    //�����`�F�b�N
    public static boolean validateNo(String idno) {
        //null�A�󕶎���OK
        if (idno == null || idno.length() == 0) {
           return true;
        }

        //�����ł��邱�Ƃ��m�F����
        try {
            if (!idno.matches("[1-9][0-9]*")) {
                //�����ȊO�̎��̓G���[
                return false;
            }
        } catch (NullPointerException e) {
            //�G���[�����o����
            return false;
        }

        return true;
    }

    // ������̒����𒲂ׂ�
    public static boolean validateLength(String str, int max_length) {
       //null�A�󕶎���OK
       if (str == null || str.length() == 0) {
               return true;
       }

       //������̒�����MAX�ȉ��ł��邱�Ƃ𒲂ׂ�
       try {
           if (str.length() > max_length) {
               //MAX��蒷�����̓G���[
               return false;
           }
       } catch (NullPointerException e) {
           //�o�O
           return false;
       }

       return true;
    }

   // ���͒l�`�F�b�N
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
