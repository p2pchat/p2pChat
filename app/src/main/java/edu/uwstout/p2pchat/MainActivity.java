package edu.uwstout.p2pchat;

import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Collection;

public class MainActivity extends WiFiActivity {

    private LinearLayout peerListLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get references to the GUI objects
        peerListLayout = findViewById(R.id.peerListLayout);
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

    @Override
    public void displayPeers(WifiP2pDeviceList wifiP2pDeviceList) {
        // Take the list of P2P devices and display relevant information on the screen

        // Parse the device list and put their details as textViews
        Collection<WifiP2pDevice> peers = wifiP2pDeviceList.getDeviceList();
        int viewID = 1;

        for (WifiP2pDevice device :
                peers) {
            // create a TextView for the WiFiP2p device
            TextView label = new TextView(this);
            label.setText("Testing");
            label.setId(viewID++);
            label.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            // add some way of letting us know if the user taps the description

            // add the TextView to the view
        }
    }

} // end of class
