package com.example.p2pchat;

import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends WiFiActivity {

    // Class variables
    private IntentFilter intentFilter = new IntentFilter();
    private WifiP2pManager.Channel channel;
    private WifiP2pManager manager;
    private WiFiDirectBroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    } // end of onCreate

    public void discoverPeers() {
        // Discover a list of peers we can interact with
        manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                // Only notifies that the discovery process completed.
                // does not provide information about the discovered peers.
                // a WIFI_P2P_PEERS_CHANGED_ACTION intent is broad-casted if
                // we were successful, see WiFiDirectBroadcastManager for state change code.
                Log.i("MainActivity", "Peer Discovery reports success");
            }

            @Override
            public void onFailure(int reasonCode) {
                // Turn the error code into a human readable message.
                // Other error handling may occur in this code.
                String errorType;
                switch (reasonCode) {
                    case WifiP2pManager.ERROR:
                        errorType = "Operation Failed due to an internal error.";
                        break;
                    case WifiP2pManager.P2P_UNSUPPORTED:
                        errorType = "Operation failed because Peer to Peer connections are not supported on this device.";
                        break;
                    case WifiP2pManager.BUSY:
                        errorType = "Operation failed because the framework is busy and is unable to service the request.";
                        break;
                    case WifiP2pManager.NO_SERVICE_REQUESTS:
                        errorType = "Operation failed because no service channel requests were added.";
                        break;
                    default:
                        errorType = "Operation failed due to an unknown error. Reason Code: " + reasonCode;
                        break;
                }
                // log the error with a description of what went wrong.
                Log.e("MainActivity","Peer Discovery Failure. Error: " + errorType);
            }
        });
    }

} // end of class
