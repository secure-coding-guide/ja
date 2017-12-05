package org.jssec.android.sqlite.task;

import org.jssec.android.sqlite.CommonData;
import org.jssec.android.sqlite.DataValidator;
import org.jssec.android.sqlite.MainActivity;
import org.jssec.android.sqlite.R;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;


//�f�[�^�����^�X�N
public class DataSearchTask extends AsyncTask<String, Void, Cursor> {
    private MainActivity    mActivity;
    private SQLiteDatabase      mSampleDB;

    public DataSearchTask(SQLiteDatabase db, MainActivity activity) {
        mSampleDB = db;
        mActivity = activity;
    }

    @Override
    protected Cursor doInBackground(String... params) {
        String  idno = params[0];
        String  name = params[1];
        String  info = params[2];
        String  cols[]  =   {"_id", "idno","name","info"};

        Cursor cur;

        //���|�C���g3�� �A�v���v���ɏ]���ē��͒l���`�F�b�N����
        if (!DataValidator.validateData(idno, name, info))
        {
            return null;
        }

        //�������S��null��������S����������j
        if ((idno == null || idno.length() == 0) &&
                (name == null || name.length() == 0) &&
                (info == null || info.length() == 0) ) {
            try {
                cur = mSampleDB.query(CommonData.TABLE_NAME, cols, null, null, null, null, null);
            } catch (SQLException e) {
                Log.e(DataSearchTask.class.toString(), mActivity.getString(R.string.SEARCHING_ERROR_MESSAGE));
                return null;
            }
            return cur;
        }

        //No���w�肳��Ă�����No�Ō���
        if (idno != null && idno.length() > 0) {
            String selectionArgs[] = {idno};

            try {
                //���|�C���g2�� �v���[�X�z���_���g�p����
                cur = mSampleDB.query(CommonData.TABLE_NAME, cols, "idno = ?", selectionArgs, null, null, null);
            } catch (SQLException e) {
                Log.e(DataSearchTask.class.toString(), mActivity.getString(R.string.SEARCHING_ERROR_MESSAGE));
                return null;
            }
            return cur;
        }

        //Name���w�肳��Ă�����Name�Ŋ��S��v����
        if (name != null && name.length() > 0) {
            String selectionArgs[] = {name};
            try {
                //���|�C���g2�� �v���[�X�z���_���g�p����
                cur = mSampleDB.query(CommonData.TABLE_NAME, cols, "name = ?", selectionArgs, null, null, null);
            } catch (SQLException e) {
                Log.e(DataSearchTask.class.toString(), mActivity.getString(R.string.SEARCHING_ERROR_MESSAGE));
                return null;
            }
            return cur;
        }

        //����ȊO�̏ꍇ��info�������ɂ��ĕ�����v����
        String argString = info.replaceAll("@", "@@"); //���͂Ƃ��Ď󂯎����info����$���G�X�P�[�v
        argString = argString.replaceAll("%", "@%"); //���͂Ƃ��Ď󂯎����info����%���G�X�P�[�v
        argString = argString.replaceAll("_", "@_"); //���͂Ƃ��Ď󂯎����info����_���G�X�P�[�v
        String selectionArgs[] = {argString};

        try {
            //���|�C���g2�� �v���[�X�z���_���g�p����
            cur = mSampleDB.query(CommonData.TABLE_NAME, cols, "info LIKE '%' || ? || '%' ESCAPE '@'", selectionArgs, null, null, null);
        } catch (SQLException e) {
            Log.e(DataSearchTask.class.toString(), mActivity.getString(R.string.SEARCHING_ERROR_MESSAGE));
            return null;
        }
        return cur;
    }

    @Override
    protected void onPostExecute(Cursor resultCur) {
        mActivity.updateCursor(resultCur);
    }
}
