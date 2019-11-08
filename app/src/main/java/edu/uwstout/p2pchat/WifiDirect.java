package edu.uwstout.p2pchat;

import static android.os.Looper.getMainLooper;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

import androidx.annotation.NonNull;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import edu.uwstout.p2pchat.FileTransfer.ReceiverAsyncTask;
import edu.uwstout.p2pchat.FileTransfer.SendDataService;
import edu.uwstout.p2pchat.FileTransfer.UpdaterAsyncTask;

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
public final class WifiDirect implements WifiP2pManager.ChannelListener,
        WifiP2pManager.ConnectionInfoListener
{
    /**
     * Creating an observer interface so that fragments can subscribe
     * to events that matter to them so that they can respond in the GUI.
     */
    public interface PeerDiscoveryListener
    {
        /**
         * Notifies the observer that the list of peers available has changed.
         *
         * @param wifiP2pDeviceList
         *         a list of available peers
         */
        void peerListChanged(WifiP2pDeviceList wifiP2pDeviceList);

        /**
         * Notifies the observer that peer discovery failed
         *
         * @param reasonCode
         *         the reason for failure. Either WifiP2pManager.ERROR, WifiP2pManager
         *         .P2P_UNSUPPORTED, WifiP2pManager.BUSY, WifiP2pManager
         *         .NO_SERVICE_REQUESTS
         */
        void peerDiscoveryFailed(int reasonCode);

        /**
         * Notifies the observer that we have successfully connected to a peer.
         */
        void peerConnectionSucceeded();

        /**
         * notifies the observer that we were unable to connect to a peer.
         *
         * @param reasonCode
         *         the reason for failure (list of reasons unknown at this time).
         */
        void peerConnectionFailed(int reasonCode);
    }

    /**
     * Indicates if the device has Wifi P2P connections enabled.
     */
    private boolean p2pEnabled = false;
    /**
     * constant tag passed to all logging method calls.
     */
    private static final String LOG_TAG = "WifiDirect";
    /**
     * Intent filter that searches for events related to Wifi Direct.
     */
    private IntentFilter intentFilter;
    /**
     * provides access to the Wifi Direct protocol library.
     */
    private WifiP2pManager manager;
    /**
     * the channel within the WifiP2pManager
     * that we interact with for communication.
     */
    private WifiP2pManager.Channel channel;
    /**
     * listens for intent broadcasts that match
     * our intent filter and responds accordingly.
     */
    private WifiDirectBroadcastReceiver receiver;
    /**
     * Information about this device as a WifiP2pDevice.
     */
    private WifiP2pDevice thisDevice = null;
    /**
     * Information about the peer that we are connected to as a WifiP2pDevice.
     */
    private WifiP2pDevice peerDevice = null;
    /**
     * Information about our current client if we are the host.
     * Necessary for two way communication.
     */
    private InetAddress clientAddress = null;
    /**
     * The information about the current connection state.
     */
    private WifiP2pInfo info = null;
    /**
     * A list of all the peer discovery listeners
     * subscribed to the events posted by this class.
     */
    private List<WifiDirect.PeerDiscoveryListener> peerDiscoveryListeners;
    /**
     * The instance of this singleton.
     */
    private static WifiDirect instance;
    /**
     * A global, on demand context of the application,
     * needed for literally all Wifi functionality.
     */
    private final Context context;


    /**
     * Private constructor for the singleton insures that nobody can
     * create a second instance of the singleton.
     *
     * @param c
     *         the application c that this singleton should use.
     */
    private WifiDirect(@NonNull final Context c)
    {
        this.context = c.getApplicationContext();
        // set up P2P framework and helper class.
        this.manager = (WifiP2pManager) this.context
                .getSystemService(Context.WIFI_P2P_SERVICE);
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
     * @param context a Context for the application on demand
     * @return a reference to the WifiDirect singleton
     */
    public static WifiDirect getInstance(@NonNull final Context context)
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
                    localInstance = new WifiDirect(context);
                    instance = localInstance;
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
     * Sets the state of Peer to Peer communication being enabled.
     *
     * @param enabled
     *         whether or not peer to peer communication is enabled
     */
    void setP2pEnabled(final boolean enabled)
    {
        this.p2pEnabled = enabled;
    }

    /**
     * Lets us know if peer to peer is enabled.
     *
     * @return a boolean indicating if peer to peer communication is enabled
     */
    boolean isP2pEnabled()
    {
        return p2pEnabled;
    }

    /**
     * Returns a reference to the WifiP2pDevice that represents this device.
     *
     * @return a WifiP2pDevice that is this device, or null if this device doesn't have P2P enabled.
     * @see WifiP2pDevice
     */
    WifiP2pDevice getThisDevice()
    {
        // ternary operator
        return (this.isP2pEnabled()) ? this.thisDevice : null;
    }

    /**
     * Sets the details of the WifiP2pDevice that represents this device.
     *
     * @param device
     *         a WifiP2pDevice that is this device
     */
    void setThisDevice(final WifiP2pDevice device)
    {
        this.thisDevice = device;
    }

    /**
     * Returns the peer we are connected to as a WifiP2pDevice,
     * or null if we aren't connected yet.
     * @return a WifiP2pDevice that is our connected peer.
     */
    public WifiP2pDevice getPeerDevice()
    {
        return this.peerDevice;
    }

    /**
     * Sets the INetAddress of our client if this device is the host
     * of the WifiP2pGroup. Used internally, there is no getter.
     * @param client The INetAddress of our client.
     * @see InetAddress
     */
    public void setClient(InetAddress client)
    {
        this.clientAddress = client;
    }

    /**
     * Adds a PeerDiscoveryListener to the list of listeners that we
     * will notify based on relevant events.
     *
     * @param pdl
     *         a PeerDiscoveryListener that wants to be notified
     * @see WifiDirect.PeerDiscoveryListener
     */
    void subscribePeerDiscoveryListener(final WifiDirect.PeerDiscoveryListener pdl)
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
    void unsubscribePeerDiscoveryListener(final WifiDirect.PeerDiscoveryListener pdl)
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
    void resume()
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
    void pause()
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
        Log.e(LOG_TAG, "Channel lost, implementing try again protocol suggested");
        // TODO reimplement so that there is an observer protocol for this
    }

    /**
     * Required by the ConnectionInfoListener interface, this function
     * informs us when there is connection information that we need to know.
     * @param wifiP2pInfo the connection information we are interested in.
     */
    @Override
    public void onConnectionInfoAvailable(final WifiP2pInfo wifiP2pInfo)
    {
        this.info = wifiP2pInfo;

        /* Determine if this device is the group host.
        *  If so, set up the server to receive connections.
        *  If I am the client, send the host my information
        *  so that they can send me messages.
        */
        if (this.info.groupFormed && this.info.isGroupOwner)
        {
            // ReceiverAsyncTask handles the process of receiving messages.
            new ReceiverAsyncTask(this.context);
            // UpdaterAsyncTask handles the process of
            // getting client information for two-way communication.
            new UpdaterAsyncTask(this.context);
        }
        else if (this.info.groupFormed)
        {
            // ReceiverAsyncTask handles the process of receiving messages.
            new ReceiverAsyncTask(this.context);
            try
            {
                // Get the INetAddress of the current device.
                InetAddress localHost = InetAddress.getLocalHost();
                // Create an intent to send to the server.
                Intent updateIntent = new Intent(this.context, SendDataService.class);
                updateIntent.setAction(SendDataService.ACTION_UPDATE_INETADDRESS);
                updateIntent.putExtra(SendDataService.EXTRAS_INETADDRESS, localHost);
                updateIntent.putExtra(SendDataService.EXTRAS_PEER_ADDRESS,
                        this.info.groupOwnerAddress.getHostAddress());
                updateIntent.putExtra(SendDataService.EXTRAS_PEER_PORT, UpdaterAsyncTask.MAGIC_PORT);
                this.context.startService(updateIntent);
            }
            catch (UnknownHostException e)
            {
                // One-way communication is impossible
                Log.e(LOG_TAG, "Could not resolve localhost. "
                    + e.getMessage());
            }
        }
    }

    /**
     * starts a call to WifiP2pManager.discoverPeers()
     * and describes the behavior of the callback.
     */
    void discoverPeers()
    {
        // Create a local class which will handle the callbacks. Class created here because
        // we needed to be able to handle context, which is an incredibly important concept
        // in Android development and is now necessary "on demand" in the singleton.
        class ActionListenerWithContext implements WifiP2pManager.ActionListener
        {

            private Context mContext;

            private ActionListenerWithContext(final Context c)
            {
                mContext = c;
            }

            @Override
            public void onSuccess()
            {
                // Only notifies that the discovery process completed.
                // does not provide information about the discovered peers.
                // a WIFI_P2P_PEERS_CHANGED_ACTION intent is broad-casted if
                // we were successful, see WiFiDirectBroadcastManager for state
                // change code in the onReceive method.
                Log.i(LOG_TAG, "Peer Discovery reports success");
            }

            @Override
            public void onFailure(final int reasonCode)
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
                                "Operation failed because Peer to Peer connections are not supported on this device.";
                        break;
                    case WifiP2pManager.BUSY:
                        errorType =
                                "Operation failed because the framework is busy and is unable to service the request.";
                        break;
                    case WifiP2pManager.NO_SERVICE_REQUESTS:
                        errorType =
                                "Operation failed because no service channel requests were added.";
                        break;
                    default:
                        errorType = "Operation failed due to an unknown error. Reason Code: " + reasonCode;
                        break;
                }
                // log the error with a description of what went wrong.
                Log.e(LOG_TAG, "Peer Discovery Failure. Error: " + errorType);
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
    void peersHaveChanged(final WifiP2pDeviceList wifiP2pDeviceList)
    {
        for (WifiDirect.PeerDiscoveryListener pdl : this.peerDiscoveryListeners)
        {
            pdl.peerListChanged(wifiP2pDeviceList);
        }
    }

    /**
     * Notifies all listeners which have subscribed to events relating
     * to when peer discovery failed, and gives them the error code as to why.
     * The reason code are static constants from the WifiP2pManager class, namely:
     * ERROR, P2P_UNSUPPORTED, BUSY, or NO_SERVICE_REQUESTS
     *
     * @param reasonCode
     *         The reason peer discovery failed.
     */
    private void peerDiscoveryFailed(final int reasonCode)
    {
        for (PeerDiscoveryListener pdl : this.peerDiscoveryListeners)
        {
            pdl.peerDiscoveryFailed(reasonCode);
        }
    }

    /**
     * Makes a Wifi Direct connection to the device passed as input.
     *
     * @param device
     *         a WifiP2pDevice that we wish to connect to.
     */
    void connectToDevice(final WifiP2pDevice device)
    {
        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = device.deviceAddress;

        class ActionListenerWithContext implements WifiP2pManager.ActionListener
        {

            private Context mContext;

            private ActionListenerWithContext(final Context c)
            {
                mContext = c;
            }

            @Override
            public void onSuccess()
            {
                Log.i(LOG_TAG, "System reports successful connection");
                for (PeerDiscoveryListener listener
                        : WifiDirect.getInstance(mContext).peerDiscoveryListeners)
                {
                    listener.peerConnectionSucceeded();
                }
            }

            @Override
            public void onFailure(final int reason)
            {
                // TODO figure out what the reasons are?
                Log.e(LOG_TAG, "Connection failure. Reason: " + reason);
                for (PeerDiscoveryListener listener
                        : WifiDirect.getInstance(mContext).peerDiscoveryListeners)
                {
                    listener.peerConnectionFailed(reason);
                }
            }

        }

        manager.connect(channel, config, new ActionListenerWithContext(this.context));
        this.peerDevice = device;
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
    static String summarizeP2pDevice(final WifiP2pDevice device)
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

    /**
     * Sends an InMemoryFile to the host assuming that we are the client.
     * @param inMemoryFile The data that we want to send.
     */
    void sendInMemoryFile(final InMemoryFile inMemoryFile)
    {
        if (this.info != null)
        {
            // I cannot transmit the data if I don't know where it is going.
            if (this.info.isGroupOwner && this.clientAddress == null)
            {
                Log.e(LOG_TAG, "Attempted to send a message to client"
                        + " before the INetAddress was collected.");
                return; // Cancel this impossible operation.
            }
            // send data (will work the same for hosts and clients).
            Intent serviceIntent = new Intent(this.context, SendDataService.class);
            serviceIntent.setAction(SendDataService.ACTION_SEND_DATA);
            serviceIntent.putExtra(SendDataService.EXTRAS_IN_MEMORY_FILE, inMemoryFile);
            serviceIntent.putExtra(SendDataService.EXTRAS_PEER_ADDRESS,
                    this.clientAddress.getHostAddress());
            serviceIntent.putExtra(SendDataService.EXTRAS_PEER_PORT, ReceiverAsyncTask.MAGIC_PORT);
            this.context.startService(serviceIntent);
        }
        else
        {
            Log.e(LOG_TAG, "Attempted to send information before WifiP2pInfo was available.");
        }
    }
}
