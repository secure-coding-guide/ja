package org.jssec.android.file.publicfile.readonly;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class PublicFileActivity extends Activity {

    private TextView mFileView;

    private static final String FILE_NAME = "public_file.dat";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file);

        mFileView = (TextView) findViewById(R.id.file_view);
    }

    /**
     * ファイルの作成処理
     * 
     * @param view
     */
    public void onCreateFileClick(View view) {
        FileOutputStream fos = null;
        try {
            // ★ポイント1★ ファイルは、アプリディレクトリ内に作成する
            // ★ポイント2★ ファイルのアクセス権は、他のアプリに対しては読み取り専用モードにする
            // （読み取り専用モードのMODE_WORLDREADABLEはAPI LEVEL 17でdeplicatedとなったため、
            //  極力使用せず、ContentProviderなどによるデータのやり取りをすること）
            fos = openFileOutput(FILE_NAME, MODE_WORLD_READABLE);

            // ★ポイント3★ センシティブな情報は格納しない
            // ★ポイント4★ ファイルに格納する情報に対しては、その入手先に関わらず内容の安全性を確認する
            // サンプルにつき割愛。「3.2 入力データの安全性を確認する」を参照。
            fos.write(new String("センシティブでない情報(Public File Activity)\n")
                    .getBytes());
        } catch (FileNotFoundException e) {
            mFileView.setText(R.string.file_view);
        } catch (IOException e) {
            android.util.Log.e("PublicFileActivity", "ファイルの作成に失敗しました");
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    android.util.Log.e("PublicFileActivity", "ファイルの終了に失敗しました");
                }
            }
        }

        finish();
    }

    /**
     * ファイルの読み込み処理
     * 
     * @param view
     */
    public void onReadFileClick(View view) {
        FileInputStream fis = null;
        try {
            fis = openFileInput(FILE_NAME);

            byte[] data = new byte[(int) fis.getChannel().size()];

            fis.read(data);

            String str = new String(data);

            mFileView.setText(str);
        } catch (FileNotFoundException e) {
            mFileView.setText(R.string.file_view);
        } catch (IOException e) {
            android.util.Log.e("PublicFileActivity", "ファイルの読込に失敗しました");
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    android.util.Log.e("PublicFileActivity", "ファイルの終了に失敗しました");
                }
            }
        }
    }

    /**
     * ファイルの削除処理
     * 
     * @param view
     */
    public void onDeleteFileClick(View view) {

        File file = new File(this.getFilesDir() + "/" + FILE_NAME);
        file.delete();

        mFileView.setText(R.string.file_view);
    }
}
