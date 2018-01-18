package org.jssec.android.autofillframework.autofillapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import org.jssec.android.autofillframework.R;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonCredential = (Button) findViewById(R.id.button_credential);
        buttonCredential.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CredentialActivity.class);
                startActivity(intent);
            }
        });

        Button buttonDisableForOtherService = (Button) findViewById(R.id.button_disable_for_other_service);
        buttonDisableForOtherService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DisableForOtherServiceActivity.class);
                startActivity(intent);
            }
        });

        Button buttonDisableAutofill = (Button) findViewById(R.id.button_disable_autofill);
        buttonDisableAutofill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DisableAutofillActivity.class);
                startActivity(intent);
            }
        });
    }

}
