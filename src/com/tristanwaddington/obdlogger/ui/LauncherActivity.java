package com.tristanwaddington.obdlogger.ui;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.tristanwaddington.obdlogger.R;

/**
 * <p>The primary Activity for this application.</p>
 * 
 * <p>When started will determine if Bluetooth is supported and
 * enabled. Will prompt the user for action if Bluetooth is not
 * currently enabled.</p>
 * 
 * @author Tristan Waddington
 */
public class LauncherActivity extends Activity {
    private static final String TAG = "LauncherActivity";
    private static final int BT_REQUEST_CODE = 1;
    
    private boolean mCancelBluetoothConnect = false;;
    
    private BluetoothAdapter mBluetoothAdapter;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // Get our Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        switch (requestCode) {
        case BT_REQUEST_CODE:
            if (resultCode == RESULT_OK) {
                Log.d(TAG, "Bluetooth enabled!");
            } else {
                Log.d(TAG, "Bluetooth *not* enabled!");
                mCancelBluetoothConnect = true;
            }
        }
    }
    
    @Override
    public void onResume() {
        super.onResume();
        
        if (mBluetoothAdapter != null) {
            if (!mBluetoothAdapter.isEnabled() && !mCancelBluetoothConnect) {
                // Prompt the user if Bluetooth is not enabled.
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intent, BT_REQUEST_CODE);
            }
        } else {
            // TODO: Show a Dialog here presenting the user
            //       with more specific information.
            Toast.makeText(this, R.string.error_bluetooth_not_supported,
                            Toast.LENGTH_LONG).show();
        }
    }
    
    @Override
    public void onPause() {
        super.onPause();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
        case R.id.settings:
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return false;
    }
}