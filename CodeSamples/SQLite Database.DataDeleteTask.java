package org.jssec.android.sqlite.task;

import org.jssec.android.sqlite.CommonData;
import org.jssec.android.sqlite.DataValidator;
import org.jssec.android.sqlite.MainActivity;
import org.jssec.android.sqlite.R;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

public class DataDeleteTask extends  AsyncTask<String, Void, Void>  {
    private SQLiteDatabase      mSampleDb;
    private MainActivity    mActivity;

    public DataDeleteTask(SQLiteDatabase db, MainActivity activity) {
        mSampleDb = db;
        mActivity = activity;
    }

    @Override
    protected Void doInBackground(String... params) {
        String  idno = params[0];
        
        //★ポイント3★ アプリケーション要件に従って入力値をチェックする
       if (!DataValidator.validateNo(idno))
        {
            return null;
        }

        String whereArgs[] = {idno};
        try {
            //★ポイント2★ プレースホルダを使用する
            mSampleDb.delete(CommonData.TABLE_NAME,"idno = ?", whereArgs);
        } catch (SQLException e) {
            Log.e(DataDeleteTask.class.toString(), mActivity.getString(R.string.UPDATING_ERROR_MESSAGE)); 
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void v) {
        //DBデータの表示
        mActivity.showDbData();
    }
}
