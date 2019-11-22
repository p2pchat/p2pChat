package edu.uwstout.p2pchat.WifiDirectHelpers;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * The updaterAsyncTask is run only when this device
 * is the host of the WifiP2pGroup. It listens for
 * INetAddress objects which are sent by clients.
 * The INetAddress informs the server / host on
 * how to communicate with their clients.
 *
 * @author VanderHoevenEvan (Evan Vander Hoeven)
 */
public class UpdaterAsyncTask extends AsyncTask<InetAddressListener, Object, InetAddress>
{
    /**
     * Magic number for open port.
     */
    public static final int MAGIC_PORT = 8989;
    /**
     * The tag for logging.
     */
    private static final String LOG_TAG = "UpdaterAsyncTask";
    /**
     * Keeps track of all listeners passed into the asynchronous task.
     */
    private static ArrayList<InetAddressListener> listeners;

    /**
     * Listens for incoming messages in the background.
     *
     * @param inetAddressListeners
     *         A series of InetAddressListeners
     * @return An object. Currently null.
     */
    @Override
    protected InetAddress doInBackground(InetAddressListener... inetAddressListeners)
    {
        listeners = new ArrayList<>();
        listeners.addAll(Arrays.asList(inetAddressListeners));
        try
        {
            ServerSocket serverSocket = new ServerSocket(MAGIC_PORT);
            Log.i(LOG_TAG, "Updater: Socket opened");
            // The call to accept is blocking, which is why this
            // is in a separate thread.
            Socket client = serverSocket.accept();
            // When we pass that line, we have accepted a connection.
            Log.i(LOG_TAG, "Updater: Connection Established");
            // get the INetAddress object from the InputStream
            InetAddress clientLocalHost = parseInetAddressFromInputStream(
                    client.getInputStream());
            // Update the singleton with the new Client information.
            if (clientLocalHost != null)
            {
                Log.i(LOG_TAG,
                        "Client information received. " + clientLocalHost.getCanonicalHostName());
                return clientLocalHost;
            }
        }
        catch (IOException e)
        {
            Log.e(LOG_TAG, "Error receiving data: " + e.getMessage());
        }
        catch (ClassNotFoundException e)
        {
            Log.e(LOG_TAG, "InputStream does not contain an InetAddress" + e.getMessage());
        }
        return null;
    }

    /**
     * Notifies all passed in InetAddressListeners of the updated InetAddress.
     * @param address The InetAddress of the client device.
     */
    @Override
    protected void onPostExecute(InetAddress address)
    {
        if (address == null)
            return;
        for (InetAddressListener listener: listeners)
        {
            listener.onLocalHostAvailable(address);
        }
    }

    /**
     * Converts an input stream into an InetAddress
     *
     * @param inputStream
     *         an InputStream that must contain an InetAddress
     * @return an InetAddress that was serialized in the InputStream,
     * or null if an error occurred during parsing.
     * @throws IOException
     *         if the parameter InputStream could not be opened.
     * @throws ClassNotFoundException
     *         if the InputStream does not contain an InetAddress.
     */
    public static InetAddress parseInetAddressFromInputStream(InputStream inputStream)
            throws IOException, ClassNotFoundException
    {
        ObjectInputStream ois = new ObjectInputStream(inputStream);
        InetAddress address = (InetAddress) ois.readObject();
        ois.close();
        return address;
    }
}
