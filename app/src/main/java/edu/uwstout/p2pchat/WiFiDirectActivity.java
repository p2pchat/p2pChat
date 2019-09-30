package edu.uwstout.p2pchat;

import android.Manifest;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public abstract class WiFiDirectActivity extends AppCompatActivity implements WifiP2pManager.ChannelListener
{
    // Protected variables
    protected IntentFilter intentFilter = new IntentFilter();
    protected WifiP2pManager.Channel channel;
    protected WifiP2pManager manager;
    protected WiFiDirectBroadcastReceiver receiver;

    // Private Variables
    private static final int PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION = 1001;
    private boolean p2pEnabled = false;

    // Getters and Setters
    public void setP2pEnabled(boolean enabled) { this.p2pEnabled = enabled; }
    public boolean isP2pEnabled() { return p2pEnabled; }

    // LIFECYCLE FUNCTIONS
    /** register the BroadcastReceiver with the intent values to be matched */
    @Override
    public void onResume() {
        super.onResume();
        receiver = new WiFiDirectBroadcastReceiver(manager, channel, this);
        registerReceiver(receiver, intentFilter);
    }

    /** unregister the BroadcastReceiver so that wi-fi events are not taking up computer time */
    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    /**
     * Lifecycle function which instantiates the GUI for
     * first time setup
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Normally, I'd declare the content view here,
        // but that shall be left to the children classes.

        this.manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        this.channel = manager.initialize(this, getMainLooper(), null);
        this.receiver = new WiFiDirectBroadcastReceiver(this.manager, this.channel, this);

        // Get intents for changes related to using WiFiDirect
        this.intentFilter = new IntentFilter();
        this.intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        this.intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        this.intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        this.intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        // Check if the app has permissions to use location data, and ask for it if we don't.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    WiFiDirectActivity.PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION);
            // We don't handle the response here, we
        }

        // Because there is the potential for the response to have errors handled in the GUI,
        // discover peers ought to be created within the child class. (it's declared abstract)
        this.discoverPeers();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        // Use of switch case to allow us to expand this method later if needed.
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.e("WiFiDirectActivity",
                            "Coarse Location permission not granted. Unable to use WiFi Direct features");
                    finish(); // closes the activity
                }
                break;
            default:
                Log.e("WiFiDirectActivity", "Unhandled permissions result: " + requestCode);
        }
    }

    /**
     * Required by the ChannelListener interface. Responds to the channel
     * disconnecting in the application.
     */
    @Override
    public void onChannelDisconnected() {
        // default implementation is merely letting the user know.
        // overriding would be recommended to try and connect again.
        Toast.makeText(this, "Channel lost", Toast.LENGTH_SHORT).show();
        Log.e("WiFiDirectActivity", "Channel lost, implementing try again protocol suggested.");
    }

    /**
     * starts a call to WiFiP2pManager.discoverPeers() and describes the behavior of the callback.
     * Each activity must handle their own errors, thus this method is abstract.
     */
    protected abstract void discoverPeers();

    /**
     * takes a list of WiFi P2P devices and displays them on the GUI
     */
    protected abstract void displayPeers(WifiP2pDeviceList wifiP2pDeviceList);

    /**
     * makes a connection to the selected device
     * @param device the information about the device we want to connect to.
     */
    protected abstract void connectToDevice(WifiP2pDevice device);
}
