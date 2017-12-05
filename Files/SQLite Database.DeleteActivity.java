package org.jssec.android.sqlite;

import android.content.Intent;

public class DeleteActivity extends SubActivity {

    protected void init(Intent intent) {
        this.setTitle(R.string.ACTIVITY_TITLE_DELETE);            

        //‰æ–Ê‚Ì•\Ž¦
        setContentView(R.layout.data_delete);       
    }
    protected boolean reflectEditText() {
        return false;
    }
}
