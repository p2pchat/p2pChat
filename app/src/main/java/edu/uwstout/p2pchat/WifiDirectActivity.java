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
import androidx.core.app.ActivityCompat;

import java.util.List;

/**
 * The WiFi Direct Activity class is any activity that needs to use
 * WiFi Direct protocols. The abstract class handles a lot of the
 * boilerplate code, while requiring methods which ought to use
 * the GUI, such as handling the events with the user.
 *
 * @author VanderHoevenEvan (Evan Vander Hoeven)
 */
public abstract class WifiDirectActivity extends AppCompatActivity implements
        WifiP2pManager.ChannelListener,
        ActivityCompat.OnRequestPermissionsResultCallback
{
    /*
        creating an observer interface so that fragments can subscribe to
        events that matter to them so that they can respond in the GUI.
     */
    public interface PeerDiscoveryListener {
        void peerListChanged(WifiP2pDeviceList wifiP2pDeviceList);
        void peerDiscoveryFailed(int reasonCode);
    }


    // protected variables
    protected IntentFilter intentFilter = new IntentFilter();
    protected WifiP2pManager.Channel channel;
    protected WifiP2pManager manager;
    protected WifiDirectBroadcastReceiver receiver;

    // Private Variables
    private static final int PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION = 1001;
    private boolean p2pEnabled = false;
    private WifiP2pDevice thisDevice = null;
    private List<PeerDiscoveryListener> peerDiscoveryListeners;

    //////////////////////////////////////////////////
    //
    // GETTERS AND SETTERS
    //
    //////////////////////////////////////////////////

    /**
     * Sets the state of Peer to Peer communication being enabled
     *
     * @param enabled
     *         whether or not peer to peer communication is enabled
     */
    public void setP2pEnabled(boolean enabled)
    {
        this.p2pEnabled = enabled;
    }

    /**
     * Let's us know if peer to peer is enabled
     *
     * @return a boolean indicating if peer to peer communication is enabled
     */
    public boolean isP2pEnabled()
    {
        return p2pEnabled;
    }

    /**
     * Returns a reference to the WifiP2pDevice that represents this device
     *
     * @return a WifiP2pDevice that is this device, or null if this device doesn't have P2P enabled
     * @see WifiP2pDevice
     */
    public WifiP2pDevice getThisDevice()
    {
        if (this.isP2pEnabled())
        {
            return this.thisDevice;
        }
        else
        {
            return null;
        }
    }

    /**
     * Sets the details of the WifiP2pDevice that represents this device
     * @param thisDevice a WifiP2pDevice that is this device
     */
    public void setThisDevice(WifiP2pDevice thisDevice)
    {
        this.thisDevice = thisDevice;
    }

    /**
     * Adds a PeerDiscoveryListener to the list of listeners that we
     * will notify based on relevant events.
     * @see PeerDiscoveryListener
     * @param pdl a PeerDiscoveryListener that wants to be notified
     */
    public void subscribePeerDiscoveryListener(PeerDiscoveryListener pdl)
    {
        this.peerDiscoveryListeners.add(pdl);
    }

    /**
     * Removes a PeerDiscoveryListener to the list of listeners that we
     * will notify based on relevant events.
     * @see PeerDiscoveryListener
     * @param pdl a PeerDiscoveryListener that no longer wants to be notified
     */
    public void unsubscribePeerDiscoveryListener(PeerDiscoveryListener pdl)
    {
        this.peerDiscoveryListeners.remove(pdl);
    }

    //////////////////////////////////////////////////
    //
    // ANDROID LIFECYCLE FUNCTIONS
    //
    //////////////////////////////////////////////////

    /**
     * Registers the receiver with the intent values to be matched
     *
     * @see android.app.Activity
     */
    @Override
    public void onResume()
    {
        super.onResume();
        receiver = new WifiDirectBroadcastReceiver(manager, channel, this);
        registerReceiver(receiver, intentFilter);
    }

    /**
     * Unregister the WifiDirectBroadcastReceiver so that Wifi events don't take
     * processing time
     *
     * @see android.app.Activity
     * @see edu.uwstout.p2pchat.WifiDirectBroadcastReceiver
     */
    @Override
    public void onPause()
    {
        super.onPause();
        unregisterReceiver(receiver);
    }

    /**
     * A lifecycle function that handles one-time initiation when the activity
     * is instantiated for the first time.
     * @param savedInstanceState
     */
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // If this weren't an abstract class, we would declare the content view here

        // set up P2P framework and helper class.
        this.manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        this.channel = manager.initialize(this, getMainLooper(), null);
        this.receiver = new WifiDirectBroadcastReceiver(this.manager, this.channel, this);

        // Get intents for changes related to using WifiDirect
        this.intentFilter = new IntentFilter();
        this.intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        this.intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        this.intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        this.intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        // Check if the app has permissions to use location data, and ask for it if we don't.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    WifiDirectActivity.PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION);
            // We don't handle the response here, we handle it in onRequestPermissions result
        }

        // discoverPeers is an abstract method because it can encounter errors,
        // which ought to be handled within the GUI
        this.discoverPeers();

    }

    //////////////////////////////////////////////////
    //
    // WIFI DIRECT PROTOCOL METHODS
    //
    //////////////////////////////////////////////////

    /**
     * Required by the ChannelListener interface. Responds to the channel
     * disconnecting within the application.
     *
     * @see android.net.wifi.p2p.WifiP2pManager.ChannelListener
     */
    @Override
    public void onChannelDisconnected()
    {
        // default implementation is merely letting the user know
        // overriding would be recommended to try and connect again.
        Toast.makeText(this, "Channel lost", Toast.LENGTH_SHORT).show();
        Log.e("WifiDirectActivity", "Channel lost, implementing try again protocol suggested");
    }

    /**
     * starts a call to WifiP2pManager.discoverPeers() and describes the behavior
     * of the callback.
     * Each activity must handle their own errors in the GUI, thus this method is abstract.
     */
    public void discoverPeers() {
        // Create a callback listener


        // Discover a list of peers we can interact with
        manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                // Only notifies that the discovery process completed.
                // does not provide information about the discovered peers.
                // a WIFI_P2P_PEERS_CHANGED_ACTION intent is broad-casted if
                // we were successful, see WiFiDirectBroadcastManager for state change code.
                Log.i("WifiDirectActivity", "Peer Discovery reports success");
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
                Log.e("WifiDirectActivity","Peer Discovery Failure. Error: " + errorType);
                // Tell the PeerDiscoveryListeners that peer discovery has gone wrong.
//                for (PeerDiscoveryListener pdl : ) {
////                    pdl.peerDiscoveryFailed(reasonCode);
////                }
            }
        });
    }

    public void peersHaveChanged(WifiP2pDeviceList wifiP2pDeviceList) {
        for (PeerDiscoveryListener pdl : this.peerDiscoveryListeners) {
            pdl.peerListChanged(wifiP2pDeviceList);
        }
    }

    //////////////////////////////////////////////////
    //
    // ABSTRACT METHODS FOR CHILDREN CLASSES
    //
    //////////////////////////////////////////////////

    /**
     * makes a connection to the input device
     *
     * @param device
     *         the WifiP2pDevice that we want to connect to.
     */
    protected abstract void connectToDevice(WifiP2pDevice device);

    //////////////////////////////////////////////////
    //
    // OTHER METHODS
    //
    //////////////////////////////////////////////////

    /**
     * A callback where the user has granted or denied the app permissions to use the location
     * (required for WifiDirect)
     *
     * @param requestCode
     *         an int representing the request granted
     * @param permissions
     *         an array of strings representing the requested permissions
     * @param grantResults
     *         an array of ints parallel to the permissions parameter. Either
     *         PERMISSION_GRANTED or PERMISSION_DENIED
     * @see androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull
            int[] grantResults)
    {
        // Use of switch case to allow us to expand this method later if needed.
        switch (requestCode)
        {
            case PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED)
                {
                    Log.e("WiFiDirectActivity",
                            "Coarse Location permission not granted. Unable to use WiFi Direct " +
                                    "features");
                    finish(); // closes the activity
                }
                break;
            default:
                Log.e("WiFiDirectActivity", "Unhandled permissions result: " + requestCode);
        }
    }

    /**
     * Takes a WifiP2pDevice and returns a summary of the essential details
     * of that device as a string.
     *
     * @param device
     *         The WifiP2pDevice that we wish to summarize
     * @return a string which describes a subset of all the details of a WifiP2pDevice which is
     * meant to be displayed on the GUI for the user.
     * @see WifiP2pDevice
     */
    public static String summarizeP2pDevice(WifiP2pDevice device)
    {
        return device.deviceName + "\n"
                + device.deviceAddress + "\n"
                + device.primaryDeviceType + "\n"
                + device.secondaryDeviceType;
    }
} // end of class
