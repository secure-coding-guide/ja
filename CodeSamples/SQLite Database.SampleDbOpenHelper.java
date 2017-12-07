package org.jssec.android.sqlite;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class SampleDbOpenHelper extends SQLiteOpenHelper {
    private SQLiteDatabase           mSampleDb;             //��舵���f�[�^���i�[����f�[�^�x�[�X

    public static SampleDbOpenHelper newHelper(Context context)
    {
        //���|�C���g1�� DB�쐬�ɂ�SQLiteOpenHelper���g�p����
        return new SampleDbOpenHelper(context);
    }

    public SQLiteDatabase getDb() {
        return mSampleDb;
    }

    //Writable���[�h��DB���J��
    public void openDatabaseWithHelper() {
        try {
            if (mSampleDb != null && mSampleDb.isOpen()) {
                if (!mSampleDb.isReadOnly())// ���ɓǂݏ����\�ŃI�[�v���ς�
                    return;
                mSampleDb.close();
              }
            mSampleDb  = getWritableDatabase(); //���̒i�K�ŃI�[�v�������
        } catch (SQLException e) {
            //�f�[�^�x�[�X�\�z�Ɏ��s�����ꍇ���O�o��
            Log.e(mContext.getClass().toString(), mContext.getString(R.string.DATABASE_OPEN_ERROR_MESSAGE));
            Toast.makeText(mContext, R.string.DATABASE_OPEN_ERROR_MESSAGE, Toast.LENGTH_LONG).show();
        }
    }

    //ReadOnly���[�h��DB���J��
    public void openDatabaseReadOnly() {
        try {
            if (mSampleDb != null && mSampleDb.isOpen()) {
                if (mSampleDb.isReadOnly())// ����ReadOnly�ŃI�[�v���ς�
                    return;
                mSampleDb.close();
            }
            SQLiteDatabase.openDatabase(mContext.getDatabasePath(CommonData.DBFILE_NAME).getPath(), null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLException e) {
            //�f�[�^�x�[�X�\�z�Ɏ��s�����ꍇ���O�o��
            Log.e(mContext.getClass().toString(), mContext.getString(R.string.DATABASE_OPEN_ERROR_MESSAGE));
            Toast.makeText(mContext, R.string.DATABASE_OPEN_ERROR_MESSAGE, Toast.LENGTH_LONG).show();
        }
    }

    //Database Close
    public void closeDatabase() {
        try {
            if (mSampleDb != null && mSampleDb.isOpen()) {
                mSampleDb.close();
            }
        } catch (SQLException e) {
            //�f�[�^�x�[�X�\�z�Ɏ��s�����ꍇ���O�o��
            Log.e(mContext.getClass().toString(), mContext.getString(R.string.DATABASE_CLOSE_ERROR_MESSAGE));
            Toast.makeText(mContext, R.string.DATABASE_CLOSE_ERROR_MESSAGE, Toast.LENGTH_LONG).show();
        }
    }

    //Context���o���Ă���
    private Context mContext;

    //�e�[�u���쐬�R�}���h
    private static final String CREATE_TABLE_COMMANDS
            = "CREATE TABLE " + CommonData.TABLE_NAME + " ("
            + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "idno INTEGER UNIQUE, "
            + "name VARCHAR(" + CommonData.TEXT_DATA_LENGTH_MAX + ") NOT NULL, "
            + "info VARCHAR(" + CommonData.TEXT_DATA_LENGTH_MAX + ")"
            + ");";

    public SampleDbOpenHelper(Context context) {
        super(context, CommonData.DBFILE_NAME, null, CommonData.DB_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_TABLE_COMMANDS);  //DB�\�z�R�}���h�̎��s
        } catch (SQLException e) {
            //�f�[�^�x�[�X�\�z�Ɏ��s�����ꍇ���O�o��
            Log.e(this.getClass().toString(), mContext.getString(R.string.DATABASE_CREATE_ERROR_MESSAGE));
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // �f�[�^�x�[�X�̃o�[�W�����A�b�v���Ɏ��s�����A�f�[�^�ڍs�Ȃǂ̏������L�q����
    }

}
