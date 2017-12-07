package org.jssec.android.sqlite;

import android.content.Intent;
import android.widget.TextView;

public class EditActivity extends SubActivity {

    protected void init(Intent intent) {

        int mRequestMode = intent.getIntExtra(CommonData.EXTRA_REQUEST_MODE, CommonData.REQUEST_NEW);

        if (mRequestMode == CommonData.REQUEST_NEW) {
            this.setTitle(R.string.ACTIVITY_TITLE_NEW);
        } else {
            this.setTitle(R.string.ACTIVITY_TITLE_EDIT);            
        }
        //画面の表示
        setContentView(R.layout.data_edit);     

        //編集モードの時はNo欄は入力不可
        if (mRequestMode == CommonData.REQUEST_EDIT) {
            ((TextView)findViewById(R.id.Field_IdNo)).setFocusable(false);
            ((TextView)findViewById(R.id.Field_IdNo)).setClickable(false);
        }
    }
    protected boolean reflectEditText() {
        return true;
    }
}
