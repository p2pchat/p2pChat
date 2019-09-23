package edu.uwstout.p2pchat;

import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public abstract class WiFiActivity extends AppCompatActivity {
    // Protected variables
    protected IntentFilter intentFilter = new IntentFilter();
    protected WifiP2pManager.Channel channel;
    protected WifiP2pManager manager;
    protected WiFiDirectBroadcastReceiver receiver;

    // Private Variables
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

    // OTHER METHODS
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

        // Because there is the potential for the response to have errors handled in the GUI,
        // discover peers ought to be called within the child class.
        this.discoverPeers();
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
}
