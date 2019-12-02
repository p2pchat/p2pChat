package edu.uwstout.p2pchat.testing;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;

import edu.uwstout.p2pchat.WifiDirectHelpers.InetAddressListener;
import edu.uwstout.p2pchat.WifiDirectHelpers.UpdaterAsyncTask;

public final class MockUpdaterAsyncTask extends UpdaterAsyncTask
{
    private static final InetAddress mockClientAddress = InetAddress.getLoopbackAddress();

    /**
     * Getter for the mockClientAddress for comparison in our tests.
     */
    public static InetAddress getMockClientAddress()
    {
        return mockClientAddress;
    }

    /**
     * Fakes an incoming InetAddress
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
        return mockClientAddress;
    }

}
