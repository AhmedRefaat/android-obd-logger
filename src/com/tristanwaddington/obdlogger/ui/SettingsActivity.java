package com.tristanwaddington.obdlogger.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.util.Log;

import com.tristanwaddington.obdlogger.Build;
import com.tristanwaddington.obdlogger.R;

/**
 * A basic implementation of {@link PreferenceActivity}.
 * 
 * @author Tristan Waddington
 */
public class SettingsActivity extends PreferenceActivity
        implements OnPreferenceClickListener {
    private static final String TAG = "SettingsActivity";
    private static final String SUPPORT_EMAIL = "support@tristanwaddington.com";
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        
        Preference preference = null;
        
        // Set the app version
        preference = findPreference(getString(R.string.pref_key_app_version));
        if (preference != null) {
            preference.setSummary(getAppVersionFromManifest(this));
        }
        
        // Set the app build
        preference = findPreference(getString(R.string.pref_key_app_build));
        if (preference != null) {
            preference.setSummary(Build.APP_BUILD);
        }
        
        // Set our preference click listeners
        preference = findPreference(getString(R.string.pref_key_send_feedback));
        if (preference != null) {
            preference.setOnPreferenceClickListener(this);
        }
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
    
    /**
     * Gets the app version string that was set in
     * the AndroidManifest.xml file.
     * 
     * @param context
     * @return The app version String from the AndroidManifest.
     */
    public static String getAppVersionFromManifest(Context context) {
        // TODO: Move this method into an open source Android utility library!
        PackageManager packageManager = context.getPackageManager();
        try {
            return packageManager.getPackageInfo(context.getPackageName(),
                    0).versionName;
        } catch (NameNotFoundException e) {
            Log.w(TAG, "Could not fetch app version from manifest!", e);
        }
        return null;
    }
    
    /**
     * Returns a String describing the device hardware including the running
     * version of Android and the app build number.
     * 
     * @param context
     * @return A String.
     */
    public static String getDeviceString(Context context) {
        // TODO: Move this method into an open source Android utility library!
        return String.format("App build: %s; Device: Android %s %s %s %s %s",
                Build.APP_BUILD, android.os.Build.VERSION.RELEASE,
                android.os.Build.BRAND, android.os.Build.MANUFACTURER,
                android.os.Build.MODEL, android.os.Build.DEVICE);
    }
    
    @Override
    public boolean onPreferenceClick(Preference preference) {
        String key = preference.getKey();
        if (key.equals(getString(R.string.pref_key_send_feedback))) {
            // Send feedback via email
            final Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("plain/text");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[] {SUPPORT_EMAIL});
            intent.putExtra(Intent.EXTRA_SUBJECT,
                    String.format("Android OBD Logger %s Support", getAppVersionFromManifest(this)));
            intent.putExtra(Intent.EXTRA_TEXT, "\n\n"+getDeviceString(this));
            startActivity(intent);
            return true;
        }
        return false;
    }
}
