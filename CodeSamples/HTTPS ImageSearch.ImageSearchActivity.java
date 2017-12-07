package org.jssec.android.https.imagesearch;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageSearchActivity extends Activity {

    private EditText mQueryBox;
    private TextView mMsgBox;
    private ImageView mImgBox;
    private AsyncTask<String, Void, Object> mAsyncTask ;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mQueryBox = (EditText)findViewById(R.id.querybox);
        mMsgBox = (TextView)findViewById(R.id.msgbox);
        mImgBox = (ImageView)findViewById(R.id.imageview);
    }

    @Override
    protected void onPause() {
        // ���̂���Activity���j�������\��������̂Ŕ񓯊��������L�����Z�����Ă���
        if (mAsyncTask != null) mAsyncTask.cancel(true);
        super.onPause();
    }

    public void onHttpSearchClick(View view) {
        String query = mQueryBox.getText().toString();
        mMsgBox.setText("HTTP:" + query);
        mImgBox.setImageBitmap(null);
        
        // ���O�̔񓯊��������I����ĂȂ����Ƃ�����̂ŃL�����Z�����Ă���
        if (mAsyncTask != null) mAsyncTask.cancel(true);
        
        // UI�X���b�h�ŒʐM���Ă͂Ȃ�Ȃ��̂ŁAAsyncTask�ɂ�胏�[�J�[�X���b�h�ŒʐM����
        mAsyncTask = new HttpImageSearch() {
            @Override
            protected void onPostExecute(Object result) {
                // UI�X���b�h�ŒʐM���ʂ���������
                if (result == null) {
                    mMsgBox.append("\n��O����\n");
                } else if (result instanceof Exception) {
                    Exception e = (Exception)result;
                    mMsgBox.append("\n��O����\n" + e.toString());
                } else {
                    // �T���v���ɂ��摜�\���̍ۂ̗�O�����͊���
                    byte[] data = (byte[])result;
                    Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                    mImgBox.setImageBitmap(bmp);
                }
            }
                }.execute(query);   // �����������n���Ĕ񓯊��������J�n
    }

    public void onHttpsSearchClick(View view) {
        String query = mQueryBox.getText().toString();
        mMsgBox.setText("HTTPS:" + query);
        mImgBox.setImageBitmap(null);
        
        // ���O�̔񓯊��������I����ĂȂ����Ƃ�����̂ŃL�����Z�����Ă���
        if (mAsyncTask != null) mAsyncTask.cancel(true);
        
        // UI�X���b�h�ŒʐM���Ă͂Ȃ�Ȃ��̂ŁAAsyncTask�ɂ�胏�[�J�[�X���b�h�ŒʐM����
        mAsyncTask = new HttpsImageSearch() {
            @Override
            protected void onPostExecute(Object result) {
                // UI�X���b�h�ŒʐM���ʂ���������
                if (result instanceof Exception) {
                    Exception e = (Exception)result;
                    mMsgBox.append("\n��O����\n" + e.toString());
                } else {
                    byte[] data = (byte[])result;
                    Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                    mImgBox.setImageBitmap(bmp);
                }
            }
                }.execute(query);   // �����������n���Ĕ񓯊��������J�n
    }
}
