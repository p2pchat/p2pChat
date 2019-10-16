package com.example.p2pchat;

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
    // protected variables
    protected IntentFilter intentFilter = new IntentFilter();
    protected WifiP2pManager.Channel channel;
    protected WifiP2pManager manager;
    protected WifiDirectBroadcastReceiver receiver;

    // Private Variables
    private static final int PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION = 1001;
    private boolean p2pEnabled = false;

    // Getters and Setters

    /**
     * @param enabled
     *         whether or not peer to peer communication is enabled
     * @brief Sets the state of Peer to Peer communication being enabled
     */
    public void setP2pEnabled(boolean enabled)
    {
        this.p2pEnabled = enabled;
    }

    /**
     * @return a boolean indicating if peer to peer communication is enabled
     * @brief Let's us know if peer to peer is enabled
     */
    public boolean isP2pEnabled()
    {
        return p2pEnabled;
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
     * @see com.example.p2pchat.WifiDirectBroadcastReceiver
     */
    @Override
    public void onPause()
    {
        super.onPause();
        unregisterReceiver(receiver);
    }

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
        Log.e("WifiDirectActivity", "Chennel lost, implementing try again protocol suggested");
    }

    //////////////////////////////////////////////////
    //
    // ABSTRACT METHODS FOR CHILDREN CLASSES
    //
    //////////////////////////////////////////////////

    /**
     * starts a call to WifiP2pManager.discoverPeers() and describes the behavior
     * of the callback.
     * Each activity must handle their own errors in the GUI, thus this method is abstract.
     */
    protected abstract void discoverPeers();

    /**
     * Takes a list of WifiP2pDevices and displays them on the GUI
     *
     * @param wifiP2pDeviceList
     *         a list of devices that we can connect to.
     */
    public abstract void displayPeers(WifiP2pDeviceList wifiP2pDeviceList);

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
