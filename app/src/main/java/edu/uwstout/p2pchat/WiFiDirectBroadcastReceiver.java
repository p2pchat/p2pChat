package edu.uwstout.p2pchat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

/**
 * A BroadcastReceiver that notifies a WiFiActivity of important Wi-Fi p2p events.
 */
public class WiFiDirectBroadcastReceiver extends BroadcastReceiver {

    private WifiP2pManager manager;
    private WifiP2pManager.Channel channel;
    private WiFiActivity activity;

    public WiFiDirectBroadcastReceiver(WifiP2pManager manager, WifiP2pManager.Channel channel,
                                       WiFiActivity activity) {
        super();
        this.manager = manager;
        this.channel = channel;
        this.activity = activity;
    }

    /**
     * Description pending
     * @param context a Context object that does something I don't know yet.
     * @param intent an Intent object that describes what kinds of events we are listening for.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        // Variable declaration
        String action = intent.getAction();
        WifiP2pManager.PeerListListener myPeerListListener = new WifiP2pManager.PeerListListener() {
            @Override
            public void onPeersAvailable(WifiP2pDeviceList wifiP2pDeviceList) {
                // take that device list and give it to the activity to display
                // so that the user can choose what device they want to connect to
                activity.displayPeers(wifiP2pDeviceList);
            }
        };

        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            // Check to see if Wi-Fi is enabled and notify appropriate activity
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                activity.setP2pEnabled(true);
            } else {
                activity.setP2pEnabled(false);
            }
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            // Call WifiP2pManager.requestPeers() to get a list of current peers
            // request available peers from the wifi p2p manager. This is an
            // asynchronous call and the calling activity is notified with a
            // callback on PeerListListener.onPeersAvailable()
            if (manager != null) {
                manager.requestPeers(channel, myPeerListListener);
            }
            else {
                Log.e("WFDBroadcastReceiver","manager null, cannot retrieve list of peers");
            }
        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            // Respond to new connection or disconnections
            // Applications can use requestConnectionInfo(), requestNetworkInfo(), or requestGroupInfo() to retrieve the current connection information.
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            // Respond to this device's wifi state changing
            // Applications can use requestDeviceInfo() to retrieve the current connection information.
        }
    }
}
