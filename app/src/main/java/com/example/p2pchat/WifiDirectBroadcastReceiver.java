package com.example.p2pchat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

/**
 * The WifiDirectBroadcastReceiver class listens for events related to
 * Wifi-Direct behavior, and calls the appropriate method in the WifiDirectActivity
 * that a reference of this class is coupled to.
 * @author VanderHoevenEvan (Evan Vander Hoeven)
 * @see android.content.BroadcastReceiver
 * @see WifiDirectActivity
 */
public class WifiDirectBroadcastReceiver extends BroadcastReceiver
{
    // private variables
    private WifiP2pManager manager;
    private WifiP2pManager.Channel channel;
    private WifiDirectActivity activity;

    /**
     * class constructor
     * @param manager the WifiP2pManager that the app activity is currently using
     * @param channel the WifiP2pManager that the app activity is currently using
     * @param activity the WifiDirectActivity that this BroadcastReceiver is
     *                 going to be coupled to.
     */
    public WifiDirectBroadcastReceiver(WifiP2pManager manager, WifiP2pManager.Channel channel,
            WifiDirectActivity activity)
    {
        super();
        this.manager = manager;
        this.channel = channel;
        this.activity = activity;
    }

    /**
     * Description pending
     *
     * @param context the context in which the receiver is running
     * @param intent The intent being received
     */
    @Override
    public void onReceive(Context context, Intent intent)
    {
        // Variable declaration
        String action = intent.getAction();
        WifiP2pManager.PeerListListener myPeerListListener = new WifiP2pManager.PeerListListener()
        {
            @Override
            public void onPeersAvailable(WifiP2pDeviceList wifiP2pDeviceList)
            {
                // take that device list and give it to the activity to display
                // so that the user can choose what device they want to connect to
                Log.i("WFDBroadcastReceiver", "Peers available");
                activity.displayPeers(wifiP2pDeviceList);
            }
        };

        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action))
        {
            // Check to see if Wi-Fi is enabled and notify appropriate activity
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED)
            {
                // Log the event
                Log.i("WFDBroadcastReceiver", "WIFI P2P State Changed to enabled");
                activity.setP2pEnabled(true);
            }
            else
            {
                // Log the event
                Log.i("WFDBroadcastReceiver", "WIFI P2P State Changed to disabled");
                activity.setP2pEnabled(false);
            }
        }
        else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action))
        {
            // Log the event
            Log.i("WFDBroadcastReceiver", "WIFI P2P Peers Changed");

            // Call WifiP2pManager.requestPeers() to get a list of current peers
            // request available peers from the wifi p2p manager. This is an
            // asynchronous call and the calling activity is notified with a
            // callback on PeerListListener.onPeersAvailable()
            if (manager != null)
            {
                Log.i("WFDBroadcastReceiver", "Requesting Peers");
                manager.requestPeers(channel, myPeerListListener);
            }
            else
            {
                Log.e("WFDBroadcastReceiver", "manager null, cannot retrieve list of peers");
            }
        }
        else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action))
        {
            // Log the event
            Log.i("WFDBroadcastReceiver", "WIFI P2P Connection Changed");
            // Respond to new connection or disconnections
            // Applications can use requestConnectionInfo(), requestNetworkInfo(), or
            // requestGroupInfo() to retrieve the current connection information.
        }
        else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action))
        {
            // Log the event
            Log.i("WFDBroadcastReceiver", "WIFI P2P This Device Changed");
            // Respond to this device's wifi state changing
            // Applications can use requestDeviceInfo() to retrieve the current connection information.
        }
        else
        {
            Log.e("WFDBroadcastReceiver", "Action not recognized. Action: " + action);
        }
    }
}
