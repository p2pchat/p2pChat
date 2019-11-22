package edu.uwstout.p2pchat.WifiDirectHelpers;

import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.VisibleForTesting;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;

public class LocalHostHelper extends AsyncTask<InetAddressListener, Object, InetAddress>
{
    @VisibleForTesting
    protected static ArrayList<InetAddressListener> listeners;

    /**
     * Constant for logging.
     */
    private static String LOG_TAG = "LocalHostHelper";

    /**
     * Handled in an asynchronous fashion, this method determines the
     * device's InetAddtess.
     * @param inetAddressListeners A series of InetAddressListeners which will be notified
     *                             when the InetAddress is ready.
     * @return The InetAddress of the device.
     */
    @Override
    protected InetAddress doInBackground(InetAddressListener... inetAddressListeners)
    {
        listeners = new ArrayList<>();
        listeners.addAll(Arrays.asList(inetAddressListeners));
        try
        {
            // Get the INetAddress of the current device.
            return InetAddress.getLocalHost();
        }
        catch (UnknownHostException e)
        {
            // One-way communication is impossible
            Log.e(LOG_TAG, "Could not resolve localhost. "
                    + e.getMessage());
            return null;
        }
    }

    /**
     * Notifies all passed in InetAddressListeners of the updated InetAddress.
     * @param address The InetAddress of the current device.
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
}
