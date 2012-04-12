package com.tristanwaddington.obdlogger.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.util.Log;

import com.tristanwaddington.obdlogger.R;

public class SettingsActivity extends PreferenceActivity {
    private static final String TAG = "SettingsActivity";
    
    private BluetoothAdapter mBluetoothAdapter;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        
        // Get our BluetoothAdapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        
        // TODO: Include non-paired devices in the list!
        if (mBluetoothAdapter.isEnabled()) {
            List<String> entries = new ArrayList<String>();
            List<String> entryValues = new ArrayList<String>();
            
            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {
                    entries.add(device.getName());
                    entryValues.add(device.getAddress());
                }
            }
            
            ListPreference p = (ListPreference) findPreference(getString(R.string.pref_key_bluetooth_device));
            p.setEntries(entries.toArray(new String[entries.size()]));
            p.setEntryValues(entryValues.toArray(new String[entryValues.size()]));
            p.setEnabled(true);
        }
    }
}
