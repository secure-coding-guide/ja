package org.jssec.android.autofillframework.autofillservice.settings;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import org.jssec.android.autofillframework.R;
import org.jssec.android.autofillframework.autofillapp.Util;
import org.jssec.android.autofillframework.autofillservice.Database;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        findViewById(R.id.button_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearAll();
            }
        });
    };

    private void clearAll() {
        Database db = new Database(this);
        db.clearAll();

        Toast.makeText(this, "Cleared all data!", Toast.LENGTH_LONG).show();
    }
}
