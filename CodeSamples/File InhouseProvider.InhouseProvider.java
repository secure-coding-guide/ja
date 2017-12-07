package org.jssec.android.file.inhouseprovider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.jssec.android.shared.SigPerm;
import org.jssec.android.shared.Utils;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

public class InhouseProvider extends ContentProvider {

    private static final String FILENAME = "sensitive.txt";

    // ���Ђ�Signature Permission
    private static final String MY_PERMISSION = "org.jssec.android.file.inhouseprovider.MY_PERMISSION";

    // ���Ђ̏ؖ����̃n�b�V���l
    private static String sMyCertHash = null;

    private static String myCertHash(Context context) {
        if (sMyCertHash == null) {
            if (Utils.isDebuggable(context)) {
                // debug.keystore��"androiddebugkey"�̏ؖ����n�b�V���l
                sMyCertHash = "0EFB7236 328348A9 89718BAD DF57F544 D5CCB4AE B9DB34BC 1E29DD26 F77C8255";
            } else {
                // keystore��"my company key"�̏ؖ����n�b�V���l
                sMyCertHash = "D397D343 A5CBC10F 4EDDEB7C A10062DE 5690984F 1FB9E88B D7B3A7C2 42E142CA";
            }
        }
        return sMyCertHash;
    }

    @Override
    public boolean onCreate() {
        File dir = getContext().getFilesDir();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(dir, FILENAME));
            // ���|�C���g1�� ���p���A�v���͎��ЃA�v���ł��邩��A�Z���V�e�B�u�ȏ���ۑ����Ă悢
            fos.write(new String("�Z���V�e�B�u�ȏ��").getBytes());

        } catch (IOException e) {
            android.util.Log.e("InHouseProvider", "�t�@�C���ۑ��Ɏ��s���܂���");
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                android.util.Log.e("InHouseProvider", "�t�@�C���I���Ɏ��s���܂���");
            }
        }

        return true;
    }

    @Override
    public ParcelFileDescriptor openFile(Uri uri, String mode)
            throws FileNotFoundException {

        // �Ǝ���`Signature Permission�����ЃA�v���ɂ���`����Ă��邱�Ƃ��m�F����
        if (!SigPerm
                .test(getContext(), MY_PERMISSION, myCertHash(getContext()))) {
            throw new SecurityException(
                    "�Ǝ���`Signature Permission�����ЃA�v���ɂ���`����Ă��Ȃ��B");
        }

        File dir = getContext().getFilesDir();
        File file = new File(dir, FILENAME);

        // �T���v���̂��ߓǂݎ���p����ɕԂ�
        int modeBits = ParcelFileDescriptor.MODE_READ_ONLY;
        return ParcelFileDescriptor.open(file, modeBits);
    }

    @Override
    public String getType(Uri uri) {
        return "";
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
            String[] selectionArgs) {
        return 0;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }
}
