package edu.uwstout.p2pchat;

import static android.os.Looper.getMainLooper;

import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * WifiDirect is a Singleton which handles the process of communicating
 * with other devices via WifiDirect protocols. WifiDirect handles events
 * such as peer discovery, connecting to peers, sending and receiving data
 * from peers, and so on.
 *
 * The singleton gangs of four design pattern helps in this design because
 * the device can only have one connection functioning, and tying the process
 * of connectivity to a single GUI would reduce flexibility as the application
 * became more complex.
 *
 * @author VanderHoevenEvan (Evan Vander Hoeven)
 */
public class WifiDirect implements WifiP2pManager.ChannelListener
{
    /*
        Creating an observer interface so that fragments can subscribe
        to events that matter to them so that they can respond in the GUI.
     */
    public interface PeerDiscoveryListener
    {
        void peerListChanged(WifiP2pDeviceList wifiP2pDeviceList);

        void peerDiscoveryFailed(int reasonCode);
    }

    // PRIVATE VARIABLES
    private boolean p2pEnabled = false;
    private IntentFilter intentFilter = new IntentFilter();
    private WifiP2pManager manager;
    private WifiP2pManager.Channel channel;
    private WifiDirectBroadcastReceiver receiver;
    private WifiP2pDevice thisDevice = null;
    private List<WifiDirect.PeerDiscoveryListener> peerDiscoveryListeners;
    private static WifiDirect instance;
    private final Context context;


    /**
     * Private constructor for the singleton insures that nobody can
     * create a second instance of the singleton
     */
    private WifiDirect(@NonNull Context context)
    {
        this.context = context.getApplicationContext();
        // set up P2P framework and helper class.
        this.manager = (WifiP2pManager) this.context.getSystemService(Context.WIFI_P2P_SERVICE);
        this.channel = manager.initialize(this.context, getMainLooper(), null);
        this.receiver = new WifiDirectBroadcastReceiver(this.manager, this.channel);

        // Get intents for changes related to using WifiDirect
        this.intentFilter = new IntentFilter();
        this.intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        this.intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        this.intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        this.intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        // Instantiate an empty list of listeners
        this.peerDiscoveryListeners = new ArrayList<>();

        // TODO I should re-evaluate how peer discovery should occur.
        //  There is potential to have it be a continuously run process
        //  which the other users turn off and on when needed.
    }

    /**
     * Globally accessible method provides a reference to the singleton
     * to call its functions.
     *
     * @return a reference to the WifiDirect singleton
     */
    public static WifiDirect getInstance(@NonNull Context context)
    {
        // double-check locking implemented for multi-threaded environments
        WifiDirect localInstance = instance;
        if (localInstance == null)
        {
            synchronized (WifiDirect.class)
            {
                localInstance = instance;
                if (localInstance == null)
                {
                    instance = localInstance = new WifiDirect(context);
                }
            }
        }
        return localInstance;
    }

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
        // ternary operator
        return (this.isP2pEnabled()) ? this.thisDevice : null;
    }

    /**
     * Sets the details of the WifiP2pDevice that represents this device
     *
     * @param thisDevice
     *         a WifiP2pDevice that is this device
     */
    public void setThisDevice(WifiP2pDevice thisDevice)
    {
        this.thisDevice = thisDevice;
    }

    /**
     * Adds a PeerDiscoveryListener to the list of listeners that we
     * will notify based on relevant events.
     *
     * @param pdl
     *         a PeerDiscoveryListener that wants to be notified
     * @see WifiDirect.PeerDiscoveryListener
     */
    public void subscribePeerDiscoveryListener(WifiDirect.PeerDiscoveryListener pdl)
    {
        // check to make sure that there are no repeats
        if (!this.peerDiscoveryListeners.contains(pdl))
        {
            this.peerDiscoveryListeners.add(pdl);
        }
    }

    /**
     * Removes a PeerDiscoveryListener to the list of listeners that we
     * will notify based on relevant events.
     *
     * @param pdl
     *         a PeerDiscoveryListener that no longer wants to be notified
     * @see WifiDirect.PeerDiscoveryListener
     */
    public void unsubscribePeerDiscoveryListener(WifiDirect.PeerDiscoveryListener pdl)
    {
        this.peerDiscoveryListeners.remove(pdl);
    }

    //////////////////////////////////////////////////
    //
    // ANDROID LIFECYCLE FUNCTIONS
    //
    // While the singleton is not an activity or a
    // fragment, it will have to interact with those
    // classes. Which means that it needs to provide
    // functionality intended for lifecycle functions.
    // The Singleton should still operate correctly
    // even if these functions are never called, but
    // if they are, they must be called in pairs.
    //
    //////////////////////////////////////////////////

    /**
     * Registers the receiver with the intent values to be matched. Intended to be called within
     * the Activity.onResume() Android lifecycle method
     *
     * @see android.app.Activity
     */
    public void resume()
    {
        receiver = new WifiDirectBroadcastReceiver(manager, channel);
        context.registerReceiver(receiver, intentFilter);
    }

    /**
     * Unregister the WifiDirectBroadcastReceiver so that Wifi events don't take
     * processing time when the app is not in focus.
     *
     * @see android.app.Activity
     * @see edu.uwstout.p2pchat.WifiDirectBroadcastReceiver
     */
    public void pause()
    {
        context.unregisterReceiver(receiver);
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
        Log.e("WifiDirect", "Channel lost, implementing try again protocol suggested");
        // TODO reimplement so that there is an observer protocol for this
    }

    /**
     * starts a call to WifiP2pManager.discoverPeers() and describes the behavior
     * of the callback.
     * Each activity must handle their own errors in the GUI, thus this method is abstract.
     */
    public void discoverPeers()
    {
        // Create a local class which will handle the callbacks. Class created here because
        // we needed to be able to handle context, which is an incredibly important concept
        // in Android development and is now necessary "on demand" in the singleton.
        class ActionListenerWithContext implements WifiP2pManager.ActionListener
        {

            private Context mContext;

            private ActionListenerWithContext(Context context)
            {
                mContext = context;
            }

            @Override
            public void onSuccess()
            {
                // Only notifies that the discovery process completed.
                // does not provide information about the discovered peers.
                // a WIFI_P2P_PEERS_CHANGED_ACTION intent is broad-casted if
                // we were successful, see WiFiDirectBroadcastManager for state
                // change code in the onReceive method.
                Log.i("WifiDirect", "Peer Discovery reports success");
            }

            @Override
            public void onFailure(int reasonCode)
            {
                // Turn the error code into a human readable message.
                // Other error handling may occur in this code.
                String errorType;
                switch (reasonCode)
                {
                    case WifiP2pManager.ERROR:
                        errorType = "Operation Failed due to an internal error.";
                        break;
                    case WifiP2pManager.P2P_UNSUPPORTED:
                        errorType =
                                "Operation failed because Peer to Peer connections are not " +
                                        "supported on this device.";
                        break;
                    case WifiP2pManager.BUSY:
                        errorType =
                                "Operation failed because the framework is busy and is unable to " +
                                        "service the request.";
                        break;
                    case WifiP2pManager.NO_SERVICE_REQUESTS:
                        errorType =
                                "Operation failed because no service channel requests were added.";
                        break;
                    default:
                        errorType = "Operation failed due to an unknown error. Reason Code: " +
                                reasonCode;
                        break;
                }
                // log the error with a description of what went wrong.
                Log.e("WifiDirect", "Peer Discovery Failure. Error: " + errorType);
                // Tell the PeerDiscoveryListeners that peer discovery has gone wrong.
                WifiDirect.getInstance(mContext).peerDiscoveryFailed(reasonCode);
            }
        }

        // create an instance of the local class created
        ActionListenerWithContext actionListenerWithContext =
                new ActionListenerWithContext(this.context);

        // Discover a list of peers we can interact with
        manager.discoverPeers(channel, actionListenerWithContext);
    }

    /**
     * Notifies all listeners which have subscribed to events relating
     * to when peers have changed, and passes them the new list of peers.
     *
     * @param wifiP2pDeviceList
     *         the new list of peers available.
     */
    public void peersHaveChanged(WifiP2pDeviceList wifiP2pDeviceList)
    {
        for (WifiDirect.PeerDiscoveryListener pdl : this.peerDiscoveryListeners)
        {
            pdl.peerListChanged(wifiP2pDeviceList);
        }
    }

    /**
     * Notifies all listeners which have subscribed to events relating
     * to when peer discovery failed, and gives them the error code as to why.
     * The reason code are static constants from the WifiP2pManager class, namely
     * ERROR, P2P_UNSUPPORTED, BUSY, or NO_SERVICE_REQUESTS
     *
     * @param reasonCode
     *         The reason peer discovery failed.
     */
    public void peerDiscoveryFailed(int reasonCode)
    {
        for (PeerDiscoveryListener pdl : this.peerDiscoveryListeners)
        {
            pdl.peerDiscoveryFailed(reasonCode);
        }
    }

    /**
     * Makes a Wifi Direct connection to the device passed as input
     *
     * @param device
     *         a WifiP2pDevice that we wish to connect to.
     * @return a boolean indicating if the connection was successful.
     */
    public void connectToDevice(WifiP2pDevice device)
    {
        // TODO implementation pending
    }

    //////////////////////////////////////////////////
    //
    // OTHER METHODS
    //
    //////////////////////////////////////////////////

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
        String summary = device.deviceName + "\n"
                + device.deviceAddress + "\n"
                + device.primaryDeviceType + "\n";
        // A device is required to have all of the above, but the
        // secondary device type is nullable.
        if (device.secondaryDeviceType != null)
        {
            summary += device.secondaryDeviceType;
        }

        return summary;
    }
}
