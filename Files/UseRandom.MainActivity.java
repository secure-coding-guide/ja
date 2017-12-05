package org.jssec.android.userandom;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void onGenerate(View view) {
        SecureRandom random = new SecureRandom();

        byte[] randomBuf = new byte [128];
        
        random.nextBytes(randomBuf);
        
        // Use the random bytes to do something cryptographic such as generating salt, iv or key.
        
        String randomHex = encodeHex(randomBuf);
        
        textToView(randomHex, R.id.textViewRandom);
    }

    public void onGenerate2(View view) {
        try {
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            byte[] randomBuf = new byte [128];
            
            random.nextBytes(randomBuf);
            
            // Use the random bytes to do something cryptographic such as generating salt, iv or key.
            
            String randomHex = encodeHex(randomBuf);

            textToView(randomHex, R.id.textViewRandom);
        } catch (NoSuchAlgorithmException e) {
            errorToView(e, R.id.textViewRandom);
        }
    }

    public void onGenerate3(View view) {
        try {
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "Crypto");
            byte[] randomBuf = new byte [128];
            
            random.nextBytes(randomBuf);
            
            // Use the random bytes to do something cryptographic such as generating salt, iv or key.
            
            String randomHex = encodeHex(randomBuf);
            
            textToView(randomHex, R.id.textViewRandom);
        } catch (NoSuchAlgorithmException e) {
            errorToView(e, R.id.textViewRandom);
        } catch (NoSuchProviderException e) {
            errorToView(e, R.id.textViewRandom);
        }
    }
    
    private void textToView(String text, int viewId) {
        TextView textView = (TextView)findViewById(viewId);
        
        if (textView != null)
            textView.setText(text); 
    }
    
    private void errorToView(Exception e, int viewId)
    {
        textToView(e.getLocalizedMessage(), viewId);
    }

    private static String encodeHex(byte[] data) {
        if (data == null)
            return null;
        final String digit = "0123456789ABCDEF";
        StringBuilder sb = new StringBuilder();
        for (byte b : data) {
            int h = (b >> 4) & 15;
            int l = b & 15;
            sb.append(digit.charAt(h));
            sb.append(digit.charAt(l));
        }
        return sb.toString();
    }
}
