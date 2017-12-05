package org.jssec.android.https.privatecertificate;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class PrivateCertificateHttpsActivity extends Activity {

    private EditText mUrlBox;
    private TextView mMsgBox;
    private ImageView mImgBox;
    private AsyncTask<String, Void, Object> mAsyncTask ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mUrlBox = (EditText)findViewById(R.id.urlbox);
        mMsgBox = (TextView)findViewById(R.id.msgbox);
        mImgBox = (ImageView)findViewById(R.id.imageview);
    }
    
    @Override
    protected void onPause() {
        // ���̂���Activity���j�������\��������̂Ŕ񓯊��������L�����Z�����Ă���
        if (mAsyncTask != null) mAsyncTask.cancel(true);
        super.onPause();
    }
    
    public void onClick(View view) {
        String url = mUrlBox.getText().toString();
        mMsgBox.setText(url);
        mImgBox.setImageBitmap(null);
        
        // ���O�̔񓯊��������I����ĂȂ����Ƃ�����̂ŃL�����Z�����Ă���
        if (mAsyncTask != null) mAsyncTask.cancel(true);
        
        // UI�X���b�h�ŒʐM���Ă͂Ȃ�Ȃ��̂ŁAAsyncTask�ɂ�胏�[�J�[�X���b�h�ŒʐM����
        mAsyncTask = new PrivateCertificateHttpsGet(this) {
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
        }.execute(url); // URL��n���Ĕ񓯊��������J�n
    }
}
