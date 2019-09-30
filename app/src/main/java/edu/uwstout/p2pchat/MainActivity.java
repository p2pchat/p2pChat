package edu.uwstout.p2pchat;

import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends WiFiDirectActivity
{

    private LinearLayout peerListLayout;
    private Button refreshButton;
    private WifiP2pDevice[] peers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get references to the GUI objects
        peerListLayout = findViewById(R.id.peerListLayout);
        refreshButton = findViewById(R.id.refresh_button);

        // Create a listener which will ask for a new list of peers
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                discoverPeers();
            }
        });
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
        peers = wifiP2pDeviceList.getDeviceList().toArray(new WifiP2pDevice[0]);

        // Log some information for sanity check
        Log.i("MainActivity","Size of peer list: " + peers.length);
        // Clear the old peer list
        peerListLayout.removeAllViews();
        // create a new peer list
        int idTracker = 0;

        for (WifiP2pDevice device : peers) {
            // create a TextView for the WiFiP2p device
            TextView label = new TextView(this);
            label.setText(device.toString());
            label.setId(idTracker++);
            label.setClickable(true);
            label.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            // add an onClickListener
            label.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    connectToDevice(peers[view.getId()]);
                }
            });
            // add the TextView to the view
            peerListLayout.addView(label);
        }
    }

    protected void connectToDevice(WifiP2pDevice device)
    {
        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = device.deviceAddress;
        manager.connect(channel, config, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {
                Log.i("MainActivity", "System reports successful connection");
            }

            @Override
            public void onFailure(int reason) {
                // TODO figure out what the reasons are?
                Log.e("MainActivity","Connection failure. Reason: " + reason);
            }
        });
    }

} // end of class
