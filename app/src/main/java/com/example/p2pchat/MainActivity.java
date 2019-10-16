package com.example.p2pchat;

import androidx.appcompat.app.AppCompatActivity;

import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.os.Bundle;

public class MainActivity extends WifiDirectActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * starts a call to WifiP2pManager.discoverPeers() and describes the behavior
     * of the callback.
     * Each activity must handle their own errors in the GUI, thus this method is abstract.
     */
    protected void discoverPeers() {
        // TODO implement
    }

    /**
     * Takes a list of WifiP2pDevices and displays them on the GUI
     *
     * @param wifiP2pDeviceList
     *         a list of devices that we can connect to.
     */
    public void displayPeers(WifiP2pDeviceList wifiP2pDeviceList) {
        // TODO implement
    }

    /**
     * makes a connection to the input device
     *
     * @param device
     *         the WifiP2pDevice that we want to connect to.
     */
    protected void connectToDevice(WifiP2pDevice device) {
        // TODO implement
    }
}
