package org.jssec.android.provider.partnerprovider;

import java.util.List;

import org.jssec.android.shared.PkgCertWhitelists;
import org.jssec.android.shared.Utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;

public class PartnerProvider extends ContentProvider {

    public static final String AUTHORITY = "org.jssec.android.provider.partnerprovider";
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.org.jssec.contenttype";
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.org.jssec.contenttype";

    // Content Provider���񋟂���C���^�[�t�F�[�X�����J
    public interface Download {
        public static final String PATH = "downloads";
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + PATH);
    }
    public interface Address {
        public static final String PATH = "addresses";
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + PATH);
    }

    // UriMatcher
    private static final int DOWNLOADS_CODE = 1;
    private static final int DOWNLOADS_ID_CODE = 2;
    private static final int ADDRESSES_CODE = 3;
    private static final int ADDRESSES_ID_CODE = 4;
    private static UriMatcher sUriMatcher;
    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, Download.PATH, DOWNLOADS_CODE);
        sUriMatcher.addURI(AUTHORITY, Download.PATH + "/#", DOWNLOADS_ID_CODE);
        sUriMatcher.addURI(AUTHORITY, Address.PATH, ADDRESSES_CODE);
        sUriMatcher.addURI(AUTHORITY, Address.PATH + "/#", ADDRESSES_ID_CODE);
    }

    // DB���g�p�����ɌŒ�l��Ԃ���ɂ��Ă��邽�߁Aquery���\�b�h�ŕԂ�Cursor�����O�ɒ�`
    private static MatrixCursor sAddressCursor = new MatrixCursor(new String[] { "_id", "pref" });
    static {
        sAddressCursor.addRow(new String[] { "1", "�k�C��" });
        sAddressCursor.addRow(new String[] { "2", "�X" });
        sAddressCursor.addRow(new String[] { "3", "���" });
    }
    private static MatrixCursor sDownloadCursor = new MatrixCursor(new String[] { "_id", "path" });
    static {
        sDownloadCursor.addRow(new String[] { "1", "/sdcard/downloads/sample.jpg" });
        sDownloadCursor.addRow(new String[] { "2", "/sdcard/downloads/sample.txt" });
    }

    // ���|�C���g2�� ���p���A�v���̏ؖ������z���C�g���X�g�ɓo�^����Ă��邱�Ƃ��m�F����
    private static PkgCertWhitelists sWhitelists = null;
    private static void buildWhitelists(Context context) {
        boolean isdebug = Utils.isDebuggable(context);
        sWhitelists = new PkgCertWhitelists();

        // �p�[�g�i�[�A�v�� org.jssec.android.provider.partneruser �̏ؖ����n�b�V���l��o�^
        sWhitelists.add("org.jssec.android.provider.partneruser", isdebug ?
                // debug.keystore��"androiddebugkey"�̏ؖ����n�b�V���l
                "0EFB7236 328348A9 89718BAD DF57F544 D5CCB4AE B9DB34BC 1E29DD26 F77C8255" :
                // keystore��"partner key"�̏ؖ����n�b�V���l
                "1F039BB5 7861C27A 3916C778 8E78CE00 690B3974 3EB8259F E2627B8D 4C0EC35A");

        // �ȉ����l�ɑ��̃p�[�g�i�[�A�v����o�^...
    }
    private static boolean checkPartner(Context context, String pkgname) {
        if (sWhitelists == null) buildWhitelists(context);
        return sWhitelists.test(context, pkgname);
    }
    // ���p���A�v���̃p�b�P�[�W�����擾
    private String getCallingPackage(Context context) {
        String pkgname;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            pkgname = super.getCallingPackage();
        } else {
            pkgname = null;
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<RunningAppProcessInfo> procList = am.getRunningAppProcesses();
            int callingPid = Binder.getCallingPid();
            if (procList != null) {
                for (RunningAppProcessInfo proc : procList) {
                    if (proc.pid == callingPid) {
                        pkgname = proc.pkgList[proc.pkgList.length - 1];
                        break;
                    }
                }
            }
        }

        return pkgname;
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public String getType(Uri uri) {

        switch (sUriMatcher.match(uri)) {
        case DOWNLOADS_CODE:
        case ADDRESSES_CODE:
            return CONTENT_TYPE;

        case DOWNLOADS_ID_CODE:
        case ADDRESSES_ID_CODE:
            return CONTENT_ITEM_TYPE;

        default:
            throw new IllegalArgumentException("Invalid URI�F" + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sortOrder) {

        // ���|�C���g2�� ���p���A�v���̏ؖ������z���C�g���X�g�ɓo�^����Ă��邱�Ƃ��m�F����
        if (!checkPartner(getContext(), getCallingPackage(getContext()))) {
            throw new SecurityException("���p���A�v���̓p�[�g�i�[�A�v���ł͂Ȃ��B");
        }

        // ���|�C���g3�� �p�[�g�i�[�A�v������̃��N�G�X�g�ł����Ă��A�p�����[�^�̈��S�����m�F����
        // �����ł�uri���z��͈͓̔��ł��邱�Ƃ��AUriMatcher#match()��switch case�Ŋm�F���Ă���B
        // �u3.2 ���̓f�[�^�̈��S�����m�F����v���Q�ƁB
        // ���|�C���g4�� �p�[�g�i�[�A�v���ɊJ�����Ă悢���Ɍ���ԑ����Ă悢
        // query�̌��ʂ��p�[�g�i�[�A�v���ɊJ�����Ă悢��񂩂ǂ����̓A�v������B
        switch (sUriMatcher.match(uri)) {
        case DOWNLOADS_CODE:
        case DOWNLOADS_ID_CODE:
            return sDownloadCursor;

        case ADDRESSES_CODE:
        case ADDRESSES_ID_CODE:
            return sAddressCursor;

        default:
            throw new IllegalArgumentException("Invalid URI�F" + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        // ���|�C���g2�� ���p���A�v���̏ؖ������z���C�g���X�g�ɓo�^����Ă��邱�Ƃ��m�F����
        if (!checkPartner(getContext(), getCallingPackage(getContext()))) {
            throw new SecurityException("���p���A�v���̓p�[�g�i�[�A�v���ł͂Ȃ��B");
        }

        // ���|�C���g3�� �p�[�g�i�[�A�v������̃��N�G�X�g�ł����Ă��A�p�����[�^�̈��S�����m�F����
        // �����ł�uri���z��͈͓̔��ł��邱�Ƃ��AUriMatcher#match()��switch case�Ŋm�F���Ă���B
        // �u3.2 ���̓f�[�^�̈��S�����m�F����v���Q�ƁB
        // ���|�C���g4�� �p�[�g�i�[�A�v���ɊJ�����Ă悢���Ɍ���ԑ����Ă悢
        // Insert���ʁA���Ԃ����ID���p�[�g�i�[�A�v���ɊJ�����Ă悢��񂩂ǂ����̓A�v������B
        switch (sUriMatcher.match(uri)) {
        case DOWNLOADS_CODE:
            return ContentUris.withAppendedId(Download.CONTENT_URI, 3);

        case ADDRESSES_CODE:
            return ContentUris.withAppendedId(Address.CONTENT_URI, 4);

        default:
            throw new IllegalArgumentException("Invalid URI�F" + uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
            String[] selectionArgs) {

        // ���|�C���g2�� ���p���A�v���̏ؖ������z���C�g���X�g�ɓo�^����Ă��邱�Ƃ��m�F����
        if (!checkPartner(getContext(), getCallingPackage(getContext()))) {
            throw new SecurityException("���p���A�v���̓p�[�g�i�[�A�v���ł͂Ȃ��B");
        }

        // ���|�C���g3�� �p�[�g�i�[�A�v������̃��N�G�X�g�ł����Ă��A�p�����[�^�̈��S�����m�F����
        // �����ł�uri���z��͈͓̔��ł��邱�Ƃ��AUriMatcher#match()��switch case�Ŋm�F���Ă���B
        // �u3.2 ���̓f�[�^�̈��S�����m�F����v���Q�ƁB
        // ���|�C���g4�� �p�[�g�i�[�A�v���ɊJ�����Ă悢���Ɍ���ԑ����Ă悢
        // Update���ꂽ���R�[�h�����Z���V�e�B�u�ȈӖ��������ǂ����̓A�v������B
        switch (sUriMatcher.match(uri)) {
        case DOWNLOADS_CODE:
            return 5;   // update���ꂽ���R�[�h����Ԃ�

        case DOWNLOADS_ID_CODE:
            return 1;

        case ADDRESSES_CODE:
            return 15;

        case ADDRESSES_ID_CODE:
            return 1;

        default:
            throw new IllegalArgumentException("Invalid URI�F" + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        // ���|�C���g2�� ���p���A�v���̏ؖ������z���C�g���X�g�ɓo�^����Ă��邱�Ƃ��m�F����
        if (!checkPartner(getContext(), getCallingPackage(getContext()))) {
            throw new SecurityException("���p���A�v���̓p�[�g�i�[�A�v���ł͂Ȃ��B");
        }

        // ���|�C���g3�� �p�[�g�i�[�A�v������̃��N�G�X�g�ł����Ă��A�p�����[�^�̈��S�����m�F����
        // �����ł�uri���z��͈͓̔��ł��邱�Ƃ��AUriMatcher#match()��switch case�Ŋm�F���Ă���B
        // �u3.2 ���̓f�[�^�̈��S�����m�F����v���Q�ƁB
        // ���|�C���g4�� �p�[�g�i�[�A�v���ɊJ�����Ă悢���Ɍ���ԑ����Ă悢
        // Delete���ꂽ���R�[�h�����Z���V�e�B�u�ȈӖ��������ǂ����̓A�v������B
        switch (sUriMatcher.match(uri)) {
        case DOWNLOADS_CODE:
            return 10;  // delete���ꂽ���R�[�h����Ԃ�

        case DOWNLOADS_ID_CODE:
            return 1;

        case ADDRESSES_CODE:
            return 20;

        case ADDRESSES_ID_CODE:
            return 1;

        default:
            throw new IllegalArgumentException("Invalid URI�F" + uri);
        }
    }
}
