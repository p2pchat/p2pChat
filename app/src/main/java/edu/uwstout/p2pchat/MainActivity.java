package edu.uwstout.p2pchat;


import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.os.Bundle;

public class MainActivity extends WifiDirectActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
