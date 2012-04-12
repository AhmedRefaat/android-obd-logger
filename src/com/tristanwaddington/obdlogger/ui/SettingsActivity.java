package com.tristanwaddington.obdlogger.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;

import com.tristanwaddington.obdlogger.R;

public class SettingsActivity extends PreferenceActivity {
    private static final String TAG = "SettingsActivity";
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }
    
    @Override
    public void onResume() {
        super.onResume();
        
        // Refresh our list of Bluetooth devices.
        refreshBluetoothDevices(BluetoothAdapter.getDefaultAdapter());
    }
    
    /**
     * Update the list of available Bluetooth devices.
     */
    private void refreshBluetoothDevices(BluetoothAdapter adapter) {
        if (adapter != null && adapter.isEnabled()) {
            List<String> entries = new ArrayList<String>();
            List<String> entryValues = new ArrayList<String>();
            
            // Query the adapter for paired devices
            Set<BluetoothDevice> pairedDevices = adapter.getBondedDevices();
            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {
                    entries.add(device.getName());
                    entryValues.add(device.getAddress());
                }
            }
            
            // Update our ListPreference entries
            ListPreference p = (ListPreference) findPreference(getString(R.string.pref_key_bluetooth_device));
            p.setEntries(entries.toArray(new String[entries.size()]));
            p.setEntryValues(entryValues.toArray(new String[entryValues.size()]));
            p.setEnabled(true);
        }
    }
}
