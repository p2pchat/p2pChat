package edu.uwstout.p2pchat.testing;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;

import edu.uwstout.p2pchat.WifiDirectHelpers.InetAddressListener;
import edu.uwstout.p2pchat.WifiDirectHelpers.LocalhostAsyncTask;

public final class MockLocalhostAsyncTask extends LocalhostAsyncTask
{
    private static final InetAddress mockLocalHost = InetAddress.getLoopbackAddress();

    /**
     * Getter for the mockLocalHost for comparison in our tests.
     */
    public static InetAddress getMockLocalHost()
    {
        return mockLocalHost;
    }

    /**
     * Handled in an asynchronous fashion, this method gets
     * a mocked INetAddress.
     *
     * @param inetAddressListeners
     *         A series of InetAddressListeners which will be notified
     *         when the InetAddress is ready.
     * @return The InetAddress of the device.
     */
    @Override
    protected InetAddress doInBackground(InetAddressListener... inetAddressListeners)
    {
        listeners = new ArrayList<>();
        listeners.addAll(Arrays.asList(inetAddressListeners));
        return mockLocalHost;
    }
}
