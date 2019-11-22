package edu.uwstout.p2pchat.WifiDirectHelpers;

import android.os.AsyncTask;
import android.util.Log;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;

public final class LocalHostHelper extends AsyncTask<LocalHostHelper.InetAddressListener, Object, InetAddress>
{
    public interface InetAddressListener {
        /**
         * Notifies the listener that the INetAddress is available.
         */
        public void onLocalHostAvailable(InetAddress address);
    }

    private static ArrayList<InetAddressListener> listeners;

    /**
     * Constant for logging.
     */
    private static String LOG_TAG = "LocalHostHelper";

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

    @Override
    protected void onPostExecute(InetAddress address)
    {
        for (InetAddressListener listener: listeners)
        {
            listener.onLocalHostAvailable(address);
        }
    }
}
